package com.daasuu.gpuv.egl;

import android.graphics.SurfaceTexture;
import android.opengl.EGL14;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.opengl.Matrix;
import android.os.Handler;
import android.util.Size;
import com.daasuu.gpuv.camerarecorder.capture.MediaVideoEncoder;
import com.daasuu.gpuv.egl.filter.GlFilter;
import javax.microedition.khronos.egl.EGLConfig;

/* loaded from: classes.dex */
public class GlPreviewRenderer extends GlFrameBufferObjectRenderer implements SurfaceTexture.OnFrameAvailableListener {
    private Size cameraResolution;
    private GlFramebufferObject filterFramebufferObject;
    private GlFilter glFilter;
    private final GLSurfaceView glView;
    private boolean isNewShader;
    private GlPreview previewShader;
    private GlSurfaceTexture previewTexture;
    private SurfaceCreateListener surfaceCreateListener;
    private int texName;
    private MediaVideoEncoder videoEncoder;
    private final Handler handler = new Handler();
    private float[] MVPMatrix = new float[16];
    private float[] ProjMatrix = new float[16];
    private float[] MMatrix = new float[16];
    private float[] VMatrix = new float[16];
    private float[] STMatrix = new float[16];
    private int angle = 0;
    private float aspectRatio = 1.0f;
    private float scaleRatio = 1.0f;
    private float drawScale = 1.0f;
    private float gestureScale = 1.0f;
    private int updateTexImageCounter = 0;
    private int updateTexImageCompare = 0;

    /* loaded from: classes.dex */
    public interface SurfaceCreateListener {
        void onCreated(SurfaceTexture surfaceTexture);
    }

    public GlPreviewRenderer(GLSurfaceView gLSurfaceView) {
        this.glView = gLSurfaceView;
        this.glView.setEGLConfigChooser(new GlConfigChooser(false));
        this.glView.setEGLContextFactory(new GlContextFactory());
        this.glView.setRenderer(this);
        this.glView.setRenderMode(0);
        Matrix.setIdentityM(this.STMatrix, 0);
    }

    public void onStartPreview(float f, float f2, boolean z) {
        Matrix.setIdentityM(this.MMatrix, 0);
        Matrix.rotateM(this.MMatrix, 0, -this.angle, 0.0f, 0.0f, 1.0f);
        if (z) {
            if (this.glView.getMeasuredWidth() == this.glView.getMeasuredHeight()) {
                float max = Math.max(f / f2, f2 / f) * 1.0f;
                Matrix.scaleM(this.MMatrix, 0, max, max, 1.0f);
                return;
            }
            float max2 = Math.max(this.glView.getMeasuredHeight() / f, this.glView.getMeasuredWidth() / f2) * 1.0f;
            Matrix.scaleM(this.MMatrix, 0, max2, max2, 1.0f);
            return;
        }
        float measuredHeight = this.glView.getMeasuredHeight() / this.glView.getMeasuredWidth();
        float f3 = f / f2;
        if (measuredHeight >= f3) {
            Matrix.scaleM(this.MMatrix, 0, 1.0f, 1.0f, 1.0f);
            return;
        }
        float f4 = (f3 / measuredHeight) * 1.0f;
        Matrix.scaleM(this.MMatrix, 0, f4, f4, 1.0f);
    }

    public void setGlFilter(final GlFilter glFilter) {
        this.glView.queueEvent(new Runnable() { // from class: com.daasuu.gpuv.egl.GlPreviewRenderer.1
            @Override // java.lang.Runnable
            public void run() {
                if (GlPreviewRenderer.this.glFilter != null) {
                    GlPreviewRenderer.this.glFilter.release();
                }
                GlPreviewRenderer.this.glFilter = glFilter;
                GlPreviewRenderer.this.isNewShader = true;
                GlPreviewRenderer.this.glView.requestRender();
            }
        });
    }

    @Override // android.graphics.SurfaceTexture.OnFrameAvailableListener
    public void onFrameAvailable(SurfaceTexture surfaceTexture) {
        this.updateTexImageCounter++;
        this.glView.requestRender();
    }

    @Override // com.daasuu.gpuv.egl.GlFrameBufferObjectRenderer
    public void onSurfaceCreated(EGLConfig eGLConfig) {
        GLES20.glClearColor(0.0f, 0.0f, 0.0f, 1.0f);
        int[] iArr = new int[1];
        GLES20.glGenTextures(iArr.length, iArr, 0);
        this.texName = iArr[0];
        this.previewTexture = new GlSurfaceTexture(this.texName);
        this.previewTexture.setOnFrameAvailableListener(this);
        GLES20.glBindTexture(this.previewTexture.getTextureTarget(), this.texName);
        EglUtil.setupSampler(this.previewTexture.getTextureTarget(), 9729, 9728);
        GLES20.glBindTexture(3553, 0);
        this.filterFramebufferObject = new GlFramebufferObject();
        this.previewShader = new GlPreview(this.previewTexture.getTextureTarget());
        this.previewShader.setup();
        Matrix.setLookAtM(this.VMatrix, 0, 0.0f, 0.0f, 5.0f, 0.0f, 0.0f, 0.0f, 0.0f, 1.0f, 0.0f);
        if (this.glFilter != null) {
            this.isNewShader = true;
        }
        GLES20.glGetIntegerv(3379, iArr, 0);
        this.handler.post(new Runnable() { // from class: com.daasuu.gpuv.egl.GlPreviewRenderer.2
            @Override // java.lang.Runnable
            public void run() {
                if (GlPreviewRenderer.this.surfaceCreateListener != null) {
                    GlPreviewRenderer.this.surfaceCreateListener.onCreated(GlPreviewRenderer.this.previewTexture.getSurfaceTexture());
                }
            }
        });
    }

    @Override // com.daasuu.gpuv.egl.GlFrameBufferObjectRenderer
    public void onSurfaceChanged(int i, int i2) {
        this.filterFramebufferObject.setup(i, i2);
        this.previewShader.setFrameSize(i, i2);
        if (this.glFilter != null) {
            this.glFilter.setFrameSize(i, i2);
        }
        this.scaleRatio = i / i2;
        Matrix.frustumM(this.ProjMatrix, 0, -this.scaleRatio, this.scaleRatio, -1.0f, 1.0f, 5.0f, 7.0f);
    }

    @Override // com.daasuu.gpuv.egl.GlFrameBufferObjectRenderer
    public void onDrawFrame(GlFramebufferObject glFramebufferObject) {
        if (this.drawScale != this.gestureScale) {
            float f = 1.0f / this.drawScale;
            Matrix.scaleM(this.MMatrix, 0, f, f, 1.0f);
            this.drawScale = this.gestureScale;
            Matrix.scaleM(this.MMatrix, 0, this.drawScale, this.drawScale, 1.0f);
        }
        synchronized (this) {
            if (this.updateTexImageCompare != this.updateTexImageCounter) {
                while (this.updateTexImageCompare != this.updateTexImageCounter) {
                    this.previewTexture.updateTexImage();
                    this.previewTexture.getTransformMatrix(this.STMatrix);
                    this.updateTexImageCompare++;
                }
            }
        }
        if (this.isNewShader) {
            if (this.glFilter != null) {
                this.glFilter.setup();
                this.glFilter.setFrameSize(glFramebufferObject.getWidth(), glFramebufferObject.getHeight());
            }
            this.isNewShader = false;
        }
        if (this.glFilter != null) {
            this.filterFramebufferObject.enable();
        }
        GLES20.glClear(16384);
        Matrix.multiplyMM(this.MVPMatrix, 0, this.VMatrix, 0, this.MMatrix, 0);
        Matrix.multiplyMM(this.MVPMatrix, 0, this.ProjMatrix, 0, this.MVPMatrix, 0);
        this.previewShader.draw(this.texName, this.MVPMatrix, this.STMatrix, this.aspectRatio);
        if (this.glFilter != null) {
            glFramebufferObject.enable();
            GLES20.glClear(16384);
            this.glFilter.draw(this.filterFramebufferObject.getTexName(), glFramebufferObject);
        }
        synchronized (this) {
            if (this.videoEncoder != null) {
                this.videoEncoder.frameAvailableSoon(this.texName, this.STMatrix, this.MVPMatrix, this.aspectRatio);
            }
        }
    }

    public void setCameraResolution(Size size) {
        this.cameraResolution = size;
    }

    public void setVideoEncoder(final MediaVideoEncoder mediaVideoEncoder) {
        this.glView.queueEvent(new Runnable() { // from class: com.daasuu.gpuv.egl.GlPreviewRenderer.3
            @Override // java.lang.Runnable
            public void run() {
                synchronized (GlPreviewRenderer.this) {
                    if (mediaVideoEncoder != null) {
                        mediaVideoEncoder.setEglContext(EGL14.eglGetCurrentContext(), GlPreviewRenderer.this.texName);
                    }
                    GlPreviewRenderer.this.videoEncoder = mediaVideoEncoder;
                }
            }
        });
    }

    public GlSurfaceTexture getPreviewTexture() {
        return this.previewTexture;
    }

    public void setAngle(int i) {
        this.angle = i;
        if (i == 90 || i == 270) {
            this.aspectRatio = this.cameraResolution.getWidth() / this.cameraResolution.getHeight();
        } else {
            this.aspectRatio = this.cameraResolution.getHeight() / this.cameraResolution.getWidth();
        }
    }

    public void setGestureScale(float f) {
        this.gestureScale = f;
    }

    public GlFilter getFilter() {
        return this.glFilter;
    }

    public void release() {
        this.glView.queueEvent(new Runnable() { // from class: com.daasuu.gpuv.egl.GlPreviewRenderer.4
            @Override // java.lang.Runnable
            public void run() {
                if (GlPreviewRenderer.this.glFilter != null) {
                    GlPreviewRenderer.this.glFilter.release();
                }
            }
        });
    }

    public void setSurfaceCreateListener(SurfaceCreateListener surfaceCreateListener) {
        this.surfaceCreateListener = surfaceCreateListener;
    }
}
