package com.opentok.android;

import android.content.Context;
import android.graphics.SurfaceTexture;
import android.opengl.GLES20;
import android.opengl.Matrix;
import android.view.TextureView;
import android.view.View;
import com.opentok.android.BaseVideoRenderer;
import com.opentok.android.OtLog;
import com.opentok.android.grafika.gles.EglCore;
import com.opentok.android.grafika.gles.WindowSurface;
import java.nio.Buffer;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.ShortBuffer;
import java.util.concurrent.locks.ReentrantLock;

/* loaded from: classes.dex */
class TextureViewRenderer extends BaseVideoRenderer {
    private Renderer renderer;
    private boolean videoLastStatus;
    private final TextureView view;
    private final OtLog.LogToken log = new OtLog.LogToken(this);
    private boolean detached = false;
    private boolean mEnableVideoFit = false;

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes.dex */
    public static class Renderer extends Thread implements TextureView.SurfaceTextureListener {
        static final int COORDS_PER_VERTEX = 3;
        static final int TEXTURECOORDS_PER_VERTEX = 2;
        BaseVideoRenderer.Frame currentFrame;
        private final ShortBuffer drawListBuffer;
        EglCore eglCore;
        private final String fragmentShaderCode;
        final ReentrantLock frameLock;
        int glProgram;
        final Object lock;
        private final OtLog.LogToken log;
        private int previousViewPortHeight;
        private int previousViewPortWidth;
        final float[] scaleMatrix;
        SurfaceTexture surfaceTexture;
        private final FloatBuffer textureBuffer;
        private int textureHeight;
        final int[] textureIds;
        private int textureWidth;
        private final FloatBuffer vertexBuffer;
        private final short[] vertexIndex;
        private final String vertexShaderCode;
        private boolean videoEnabled;
        private boolean videoFitEnabled;
        private int viewportHeight;
        private int viewportWidth;
        static final float[] xyzCoords = {-1.0f, 1.0f, 0.0f, -1.0f, -1.0f, 0.0f, 1.0f, -1.0f, 0.0f, 1.0f, 1.0f, 0.0f};
        static final float[] uvCoords = {0.0f, 0.0f, 0.0f, 1.0f, 1.0f, 1.0f, 1.0f, 0.0f};

        private Renderer() {
            this.vertexShaderCode = "uniform mat4 uMVPMatrix;attribute vec4 aPosition;\nattribute vec2 aTextureCoord;\nvarying vec2 vTextureCoord;\nvoid main() {\n  gl_Position = uMVPMatrix * aPosition;\n  vTextureCoord = aTextureCoord;\n}\n";
            this.fragmentShaderCode = "precision mediump float;\nuniform sampler2D Ytex;\nuniform sampler2D Utex,Vtex;\nvarying vec2 vTextureCoord;\nvoid main(void) {\n  float nx,ny,r,g,b,y,u,v;\n  mediump vec4 txl,ux,vx;  nx=vTextureCoord[0];\n  ny=vTextureCoord[1];\n  y=texture2D(Ytex,vec2(nx,ny)).r;\n  u=texture2D(Utex,vec2(nx,ny)).r;\n  v=texture2D(Vtex,vec2(nx,ny)).r;\n  y=1.1643*(y-0.0625);\n  u=u-0.5;\n  v=v-0.5;\n  r=y+1.5958*v;\n  g=y-0.39173*u-0.81290*v;\n  b=y+2.017*u;\n  gl_FragColor=vec4(r,g,b,1.0);\n}\n";
            this.log = new OtLog.LogToken(this);
            this.lock = new Object();
            this.videoEnabled = true;
            this.videoFitEnabled = false;
            this.textureIds = new int[3];
            this.scaleMatrix = new float[16];
            this.vertexIndex = new short[]{0, 1, 2, 0, 2, 3};
            this.frameLock = new ReentrantLock();
            ByteBuffer allocateDirect = ByteBuffer.allocateDirect(xyzCoords.length * 4);
            allocateDirect.order(ByteOrder.nativeOrder());
            FloatBuffer asFloatBuffer = allocateDirect.asFloatBuffer();
            this.vertexBuffer = asFloatBuffer;
            asFloatBuffer.put(xyzCoords);
            this.vertexBuffer.position(0);
            ByteBuffer allocateDirect2 = ByteBuffer.allocateDirect(uvCoords.length * 4);
            allocateDirect2.order(ByteOrder.nativeOrder());
            FloatBuffer asFloatBuffer2 = allocateDirect2.asFloatBuffer();
            this.textureBuffer = asFloatBuffer2;
            asFloatBuffer2.put(uvCoords);
            this.textureBuffer.position(0);
            ByteBuffer allocateDirect3 = ByteBuffer.allocateDirect(this.vertexIndex.length * 2);
            allocateDirect3.order(ByteOrder.nativeOrder());
            ShortBuffer asShortBuffer = allocateDirect3.asShortBuffer();
            this.drawListBuffer = asShortBuffer;
            asShortBuffer.put(this.vertexIndex);
            this.drawListBuffer.position(0);
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void displayFrame(BaseVideoRenderer.Frame frame) {
            this.frameLock.lock();
            BaseVideoRenderer.Frame frame2 = this.currentFrame;
            if (frame2 != null) {
                frame2.destroy();
            }
            this.currentFrame = frame;
            this.frameLock.unlock();
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void enableVideoFit(boolean z) {
            this.videoFitEnabled = z;
        }

        private void initializeTexture(int i, int i2, int i3, int i4) {
            GLES20.glActiveTexture(i);
            GLES20.glBindTexture(3553, i2);
            GLES20.glTexParameterf(3553, 10241, 9728.0f);
            GLES20.glTexParameterf(3553, 10240, 9729.0f);
            GLES20.glTexParameterf(3553, 10242, 33071.0f);
            GLES20.glTexParameterf(3553, 10243, 33071.0f);
            GLES20.glTexImage2D(3553, 0, 6409, i3, i4, 0, 6409, 5121, null);
        }

        /* JADX INFO: Access modifiers changed from: private */
        public boolean isEnableVideo() {
            return this.videoEnabled;
        }

        private int loadShader(int i, String str) {
            int glCreateShader = GLES20.glCreateShader(i);
            GLES20.glShaderSource(glCreateShader, str);
            GLES20.glCompileShader(glCreateShader);
            return glCreateShader;
        }

        private void renderFrameLoop(WindowSurface windowSurface) {
            float f;
            float f2;
            while (true) {
                synchronized (this.lock) {
                    if (this.surfaceTexture == null) {
                        return;
                    }
                }
                this.frameLock.lock();
                if (this.currentFrame == null || !this.videoEnabled) {
                    GLES20.glClearColor(0.0f, 0.0f, 0.0f, 1.0f);
                    GLES20.glClear(16384);
                } else {
                    GLES20.glClearColor(0.0f, 0.0f, 0.0f, 1.0f);
                    GLES20.glClear(16384);
                    GLES20.glUseProgram(this.glProgram);
                    updateViewportSizeIfNeeded();
                    if (this.textureWidth != this.currentFrame.getWidth() || this.textureHeight != this.currentFrame.getHeight()) {
                        setupTextures(this.currentFrame);
                    }
                    updateTextures(this.currentFrame);
                    Matrix.setIdentityM(this.scaleMatrix, 0);
                    float width = this.currentFrame.getWidth() / this.currentFrame.getHeight();
                    float f3 = this.viewportWidth / this.viewportHeight;
                    if (!this.videoFitEnabled ? width < f3 : width > f3) {
                        f2 = width / f3;
                        f = 1.0f;
                    } else {
                        f = f3 / width;
                        f2 = 1.0f;
                    }
                    Matrix.scaleM(this.scaleMatrix, 0, f2 * (this.currentFrame.isMirroredX() ? -1.0f : 1.0f), f, 1.0f);
                    GLES20.glUniformMatrix4fv(GLES20.glGetUniformLocation(this.glProgram, "uMVPMatrix"), 1, false, this.scaleMatrix, 0);
                    GLES20.glDrawElements(4, this.vertexIndex.length, 5123, this.drawListBuffer);
                }
                this.frameLock.unlock();
                windowSurface.c();
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void setEnableVideo(boolean z) {
            this.videoEnabled = z;
        }

        private void setupTextures(BaseVideoRenderer.Frame frame) {
            int[] iArr = this.textureIds;
            if (iArr[0] != 0) {
                GLES20.glDeleteTextures(3, iArr, 0);
            }
            GLES20.glGenTextures(3, this.textureIds, 0);
            int width = frame.getWidth();
            int height = frame.getHeight();
            int i = (width + 1) >> 1;
            int i2 = (height + 1) >> 1;
            initializeTexture(33984, this.textureIds[0], width, height);
            initializeTexture(33985, this.textureIds[1], i, i2);
            initializeTexture(33986, this.textureIds[2], i, i2);
            this.textureWidth = frame.getWidth();
            this.textureHeight = frame.getHeight();
        }

        private void setupgl() {
            GLES20.glClearColor(0.0f, 0.0f, 0.0f, 1.0f);
            GLES20.glClear(16384);
            int loadShader = loadShader(35633, "uniform mat4 uMVPMatrix;attribute vec4 aPosition;\nattribute vec2 aTextureCoord;\nvarying vec2 vTextureCoord;\nvoid main() {\n  gl_Position = uMVPMatrix * aPosition;\n  vTextureCoord = aTextureCoord;\n}\n");
            int loadShader2 = loadShader(35632, "precision mediump float;\nuniform sampler2D Ytex;\nuniform sampler2D Utex,Vtex;\nvarying vec2 vTextureCoord;\nvoid main(void) {\n  float nx,ny,r,g,b,y,u,v;\n  mediump vec4 txl,ux,vx;  nx=vTextureCoord[0];\n  ny=vTextureCoord[1];\n  y=texture2D(Ytex,vec2(nx,ny)).r;\n  u=texture2D(Utex,vec2(nx,ny)).r;\n  v=texture2D(Vtex,vec2(nx,ny)).r;\n  y=1.1643*(y-0.0625);\n  u=u-0.5;\n  v=v-0.5;\n  r=y+1.5958*v;\n  g=y-0.39173*u-0.81290*v;\n  b=y+2.017*u;\n  gl_FragColor=vec4(r,g,b,1.0);\n}\n");
            int glCreateProgram = GLES20.glCreateProgram();
            this.glProgram = glCreateProgram;
            GLES20.glAttachShader(glCreateProgram, loadShader);
            GLES20.glAttachShader(this.glProgram, loadShader2);
            GLES20.glLinkProgram(this.glProgram);
            int glGetAttribLocation = GLES20.glGetAttribLocation(this.glProgram, "aPosition");
            int glGetAttribLocation2 = GLES20.glGetAttribLocation(this.glProgram, "aTextureCoord");
            GLES20.glVertexAttribPointer(glGetAttribLocation, 3, 5126, false, 12, (Buffer) this.vertexBuffer);
            GLES20.glEnableVertexAttribArray(glGetAttribLocation);
            GLES20.glVertexAttribPointer(glGetAttribLocation2, 2, 5126, false, 8, (Buffer) this.textureBuffer);
            GLES20.glEnableVertexAttribArray(glGetAttribLocation2);
            GLES20.glUseProgram(this.glProgram);
            GLES20.glUniform1i(GLES20.glGetUniformLocation(this.glProgram, "Ytex"), 0);
            GLES20.glUniform1i(GLES20.glGetUniformLocation(this.glProgram, "Utex"), 1);
            GLES20.glUniform1i(GLES20.glGetUniformLocation(this.glProgram, "Vtex"), 2);
            this.textureWidth = 0;
            this.textureHeight = 0;
        }

        private void updateTextures(BaseVideoRenderer.Frame frame) {
            int width = frame.getWidth();
            int height = frame.getHeight();
            int i = (width + 1) >> 1;
            int i2 = (height + 1) >> 1;
            int i3 = width * height;
            int i4 = i * i2;
            ByteBuffer buffer = frame.getBuffer();
            buffer.clear();
            if (buffer.remaining() != (i4 * 2) + i3) {
                this.textureWidth = 0;
                this.textureHeight = 0;
                return;
            }
            buffer.position(0);
            GLES20.glPixelStorei(3317, 1);
            GLES20.glPixelStorei(3333, 1);
            GLES20.glActiveTexture(33984);
            GLES20.glBindTexture(3553, this.textureIds[0]);
            GLES20.glTexSubImage2D(3553, 0, 0, 0, width, height, 6409, 5121, buffer);
            buffer.position(i3);
            GLES20.glActiveTexture(33985);
            GLES20.glBindTexture(3553, this.textureIds[1]);
            GLES20.glTexSubImage2D(3553, 0, 0, 0, i, i2, 6409, 5121, buffer);
            buffer.position(i3 + i4);
            GLES20.glActiveTexture(33986);
            GLES20.glBindTexture(3553, this.textureIds[2]);
            GLES20.glTexSubImage2D(3553, 0, 0, 0, i, i2, 6409, 5121, buffer);
        }

        private void updateViewportSizeIfNeeded() {
            if (this.previousViewPortWidth == this.viewportWidth && this.previousViewPortHeight == this.viewportHeight) {
                return;
            }
            GLES20.glViewport(0, 0, this.viewportWidth, this.viewportHeight);
            this.previousViewPortHeight = this.viewportHeight;
            this.previousViewPortWidth = this.viewportWidth;
        }

        private void waitUntilSurfaceIsReady() {
            synchronized (this.lock) {
                while (this.surfaceTexture == null) {
                    try {
                        this.lock.wait();
                    } catch (InterruptedException unused) {
                        this.log.d("Waiting for surface ready was interrupted", new Object[0]);
                    }
                }
            }
        }

        @Override // android.view.TextureView.SurfaceTextureListener
        public void onSurfaceTextureAvailable(SurfaceTexture surfaceTexture, int i, int i2) {
            synchronized (this.lock) {
                this.surfaceTexture = surfaceTexture;
                this.viewportWidth = i;
                this.viewportHeight = i2;
                this.lock.notify();
            }
        }

        @Override // android.view.TextureView.SurfaceTextureListener
        public boolean onSurfaceTextureDestroyed(SurfaceTexture surfaceTexture) {
            synchronized (this.lock) {
                this.surfaceTexture = null;
            }
            return true;
        }

        @Override // android.view.TextureView.SurfaceTextureListener
        public void onSurfaceTextureSizeChanged(SurfaceTexture surfaceTexture, int i, int i2) {
            this.viewportWidth = i;
            this.viewportHeight = i2;
        }

        @Override // android.view.TextureView.SurfaceTextureListener
        public void onSurfaceTextureUpdated(SurfaceTexture surfaceTexture) {
            synchronized (this.lock) {
                this.surfaceTexture = surfaceTexture;
            }
        }

        @Override // java.lang.Thread, java.lang.Runnable
        public void run() {
            waitUntilSurfaceIsReady();
            EglCore eglCore = new EglCore(null, 2);
            this.eglCore = eglCore;
            WindowSurface windowSurface = new WindowSurface(eglCore, this.surfaceTexture);
            windowSurface.a();
            setupgl();
            renderFrameLoop(windowSurface);
            windowSurface.d();
            this.eglCore.a();
        }
    }

    public TextureViewRenderer(Context context) {
        this.view = new TextureView(context);
        Renderer renderer = new Renderer();
        this.renderer = renderer;
        this.view.setSurfaceTextureListener(renderer);
        this.view.addOnAttachStateChangeListener(new View.OnAttachStateChangeListener() { // from class: com.opentok.android.TextureViewRenderer.1
            @Override // android.view.View.OnAttachStateChangeListener
            public void onViewAttachedToWindow(View view) {
                TextureViewRenderer.this.attachView();
            }

            @Override // android.view.View.OnAttachStateChangeListener
            public void onViewDetachedFromWindow(View view) {
                TextureViewRenderer.this.detachView();
            }
        });
        this.renderer.start();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void attachView() {
        if (this.detached) {
            this.detached = false;
            restartRenderer();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void detachView() {
        this.detached = true;
    }

    private void restartRenderer() {
        Renderer renderer = new Renderer();
        this.renderer = renderer;
        renderer.enableVideoFit(this.mEnableVideoFit);
        this.view.setSurfaceTextureListener(this.renderer);
        this.renderer.start();
    }

    @Override // com.opentok.android.BaseVideoRenderer
    public View getView() {
        return this.view;
    }

    @Override // com.opentok.android.BaseVideoRenderer
    public void onFrame(BaseVideoRenderer.Frame frame) {
        this.renderer.displayFrame(frame);
    }

    @Override // com.opentok.android.BaseVideoRenderer
    public void onPause() {
        this.videoLastStatus = this.renderer.isEnableVideo();
        this.renderer.setEnableVideo(false);
    }

    @Override // com.opentok.android.BaseVideoRenderer
    public void onResume() {
        this.renderer.setEnableVideo(this.videoLastStatus);
        if (this.renderer.isAlive()) {
            return;
        }
        restartRenderer();
    }

    @Override // com.opentok.android.BaseVideoRenderer
    public void onVideoPropertiesChanged(boolean z) {
        this.renderer.setEnableVideo(z);
    }

    @Override // com.opentok.android.BaseVideoRenderer
    public void setStyle(String str, String str2) {
        Renderer renderer;
        boolean z;
        if (BaseVideoRenderer.STYLE_VIDEO_SCALE.equals(str)) {
            if (BaseVideoRenderer.STYLE_VIDEO_FIT.equals(str2)) {
                renderer = this.renderer;
                z = true;
            } else if (!BaseVideoRenderer.STYLE_VIDEO_FILL.equals(str2)) {
                return;
            } else {
                renderer = this.renderer;
                z = false;
            }
            renderer.enableVideoFit(z);
            this.mEnableVideoFit = z;
        }
    }
}
