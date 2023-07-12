package jp.co.cyberagent.android.gpuimage;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.SurfaceTexture;
import android.hardware.Camera;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.LinkedList;
import java.util.Queue;
import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;
import jp.co.cyberagent.android.gpuimage.GLTextureView;
import jp.co.cyberagent.android.gpuimage.GPUImage;
import jp.co.cyberagent.android.gpuimage.filter.GPUImageFilter;
import jp.co.cyberagent.android.gpuimage.util.OpenGlUtils;
import jp.co.cyberagent.android.gpuimage.util.Rotation;
import jp.co.cyberagent.android.gpuimage.util.TextureRotationUtil;

/* loaded from: classes2.dex */
public class GPUImageRenderer implements GLSurfaceView.Renderer, GLTextureView.Renderer, Camera.PreviewCallback {
    public static final float[] CUBE = {-1.0f, -1.0f, 1.0f, -1.0f, -1.0f, 1.0f, 1.0f, 1.0f};
    private static final int NO_IMAGE = -1;
    private int addedPadding;
    private GPUImageFilter filter;
    private boolean flipHorizontal;
    private boolean flipVertical;
    private IntBuffer glRgbBuffer;
    private final FloatBuffer glTextureBuffer;
    private int imageHeight;
    private int imageWidth;
    private int outputHeight;
    private int outputWidth;
    private Rotation rotation;
    public final Object surfaceChangedWaiter = new Object();
    private int glTextureId = -1;
    private SurfaceTexture surfaceTexture = null;
    private GPUImage.ScaleType scaleType = GPUImage.ScaleType.CENTER_CROP;
    private float backgroundRed = 0.0f;
    private float backgroundGreen = 0.0f;
    private float backgroundBlue = 0.0f;
    private final Queue<Runnable> runOnDraw = new LinkedList();
    private final Queue<Runnable> runOnDrawEnd = new LinkedList();
    private final FloatBuffer glCubeBuffer = ByteBuffer.allocateDirect(CUBE.length * 4).order(ByteOrder.nativeOrder()).asFloatBuffer();

    private float addDistance(float f, float f2) {
        return f == 0.0f ? f2 : 1.0f - f2;
    }

    public GPUImageRenderer(GPUImageFilter gPUImageFilter) {
        this.filter = gPUImageFilter;
        this.glCubeBuffer.put(CUBE).position(0);
        this.glTextureBuffer = ByteBuffer.allocateDirect(TextureRotationUtil.TEXTURE_NO_ROTATION.length * 4).order(ByteOrder.nativeOrder()).asFloatBuffer();
        setRotation(Rotation.NORMAL, false, false);
    }

    @Override // android.opengl.GLSurfaceView.Renderer, jp.co.cyberagent.android.gpuimage.GLTextureView.Renderer
    public void onSurfaceCreated(GL10 gl10, EGLConfig eGLConfig) {
        GLES20.glClearColor(this.backgroundRed, this.backgroundGreen, this.backgroundBlue, 1.0f);
        GLES20.glDisable(2929);
        this.filter.ifNeedInit();
    }

    @Override // android.opengl.GLSurfaceView.Renderer, jp.co.cyberagent.android.gpuimage.GLTextureView.Renderer
    public void onSurfaceChanged(GL10 gl10, int i, int i2) {
        this.outputWidth = i;
        this.outputHeight = i2;
        GLES20.glViewport(0, 0, i, i2);
        GLES20.glUseProgram(this.filter.getProgram());
        this.filter.onOutputSizeChanged(i, i2);
        adjustImageScaling();
        synchronized (this.surfaceChangedWaiter) {
            this.surfaceChangedWaiter.notifyAll();
        }
    }

    @Override // android.opengl.GLSurfaceView.Renderer, jp.co.cyberagent.android.gpuimage.GLTextureView.Renderer
    public void onDrawFrame(GL10 gl10) {
        GLES20.glClear(16640);
        runAll(this.runOnDraw);
        this.filter.onDraw(this.glTextureId, this.glCubeBuffer, this.glTextureBuffer);
        runAll(this.runOnDrawEnd);
        if (this.surfaceTexture != null) {
            this.surfaceTexture.updateTexImage();
        }
    }

    public void setBackgroundColor(float f, float f2, float f3) {
        this.backgroundRed = f;
        this.backgroundGreen = f2;
        this.backgroundBlue = f3;
    }

    private void runAll(Queue<Runnable> queue) {
        synchronized (queue) {
            while (!queue.isEmpty()) {
                queue.poll().run();
            }
        }
    }

    @Override // android.hardware.Camera.PreviewCallback
    public void onPreviewFrame(byte[] bArr, Camera camera) {
        Camera.Size previewSize = camera.getParameters().getPreviewSize();
        onPreviewFrame(bArr, previewSize.width, previewSize.height);
    }

    public void onPreviewFrame(final byte[] bArr, final int i, final int i2) {
        if (this.glRgbBuffer == null) {
            this.glRgbBuffer = IntBuffer.allocate(i * i2);
        }
        if (this.runOnDraw.isEmpty()) {
            runOnDraw(new Runnable() { // from class: jp.co.cyberagent.android.gpuimage.GPUImageRenderer.1
                @Override // java.lang.Runnable
                public void run() {
                    GPUImageNativeLibrary.YUVtoRBGA(bArr, i, i2, GPUImageRenderer.this.glRgbBuffer.array());
                    GPUImageRenderer.this.glTextureId = OpenGlUtils.loadTexture(GPUImageRenderer.this.glRgbBuffer, i, i2, GPUImageRenderer.this.glTextureId);
                    if (GPUImageRenderer.this.imageWidth != i) {
                        GPUImageRenderer.this.imageWidth = i;
                        GPUImageRenderer.this.imageHeight = i2;
                        GPUImageRenderer.this.adjustImageScaling();
                    }
                }
            });
        }
    }

    public void setUpSurfaceTexture(final Camera camera) {
        runOnDraw(new Runnable() { // from class: jp.co.cyberagent.android.gpuimage.GPUImageRenderer.2
            @Override // java.lang.Runnable
            public void run() {
                int[] iArr = new int[1];
                GLES20.glGenTextures(1, iArr, 0);
                GPUImageRenderer.this.surfaceTexture = new SurfaceTexture(iArr[0]);
                try {
                    camera.setPreviewTexture(GPUImageRenderer.this.surfaceTexture);
                    camera.setPreviewCallback(GPUImageRenderer.this);
                    camera.startPreview();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public void setFilter(final GPUImageFilter gPUImageFilter) {
        runOnDraw(new Runnable() { // from class: jp.co.cyberagent.android.gpuimage.GPUImageRenderer.3
            @Override // java.lang.Runnable
            public void run() {
                GPUImageFilter gPUImageFilter2 = GPUImageRenderer.this.filter;
                GPUImageRenderer.this.filter = gPUImageFilter;
                if (gPUImageFilter2 != null) {
                    gPUImageFilter2.destroy();
                }
                GPUImageRenderer.this.filter.ifNeedInit();
                GLES20.glUseProgram(GPUImageRenderer.this.filter.getProgram());
                GPUImageRenderer.this.filter.onOutputSizeChanged(GPUImageRenderer.this.outputWidth, GPUImageRenderer.this.outputHeight);
            }
        });
    }

    public void deleteImage() {
        runOnDraw(new Runnable() { // from class: jp.co.cyberagent.android.gpuimage.GPUImageRenderer.4
            @Override // java.lang.Runnable
            public void run() {
                GLES20.glDeleteTextures(1, new int[]{GPUImageRenderer.this.glTextureId}, 0);
                GPUImageRenderer.this.glTextureId = -1;
            }
        });
    }

    public void setImageBitmap(Bitmap bitmap) {
        setImageBitmap(bitmap, true);
    }

    public void setImageBitmap(final Bitmap bitmap, final boolean z) {
        if (bitmap == null) {
            return;
        }
        runOnDraw(new Runnable() { // from class: jp.co.cyberagent.android.gpuimage.GPUImageRenderer.5
            @Override // java.lang.Runnable
            public void run() {
                Bitmap bitmap2;
                if (bitmap.getWidth() % 2 != 1) {
                    GPUImageRenderer.this.addedPadding = 0;
                    bitmap2 = null;
                } else {
                    bitmap2 = Bitmap.createBitmap(bitmap.getWidth() + 1, bitmap.getHeight(), Bitmap.Config.ARGB_8888);
                    Canvas canvas = new Canvas(bitmap2);
                    canvas.drawARGB(0, 0, 0, 0);
                    canvas.drawBitmap(bitmap, 0.0f, 0.0f, (Paint) null);
                    GPUImageRenderer.this.addedPadding = 1;
                }
                GPUImageRenderer.this.glTextureId = OpenGlUtils.loadTexture(bitmap2 != null ? bitmap2 : bitmap, GPUImageRenderer.this.glTextureId, z);
                if (bitmap2 != null) {
                    bitmap2.recycle();
                }
                GPUImageRenderer.this.imageWidth = bitmap.getWidth();
                GPUImageRenderer.this.imageHeight = bitmap.getHeight();
                GPUImageRenderer.this.adjustImageScaling();
            }
        });
    }

    public void setScaleType(GPUImage.ScaleType scaleType) {
        this.scaleType = scaleType;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public int getFrameWidth() {
        return this.outputWidth;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public int getFrameHeight() {
        return this.outputHeight;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void adjustImageScaling() {
        float[] fArr;
        float f = this.outputWidth;
        float f2 = this.outputHeight;
        if (this.rotation == Rotation.ROTATION_270 || this.rotation == Rotation.ROTATION_90) {
            f = this.outputHeight;
            f2 = this.outputWidth;
        }
        float max = Math.max(f / this.imageWidth, f2 / this.imageHeight);
        float round = Math.round(this.imageWidth * max) / f;
        float round2 = Math.round(this.imageHeight * max) / f2;
        float[] fArr2 = CUBE;
        float[] rotation = TextureRotationUtil.getRotation(this.rotation, this.flipHorizontal, this.flipVertical);
        if (this.scaleType == GPUImage.ScaleType.CENTER_CROP) {
            float f3 = (1.0f - (1.0f / round)) / 2.0f;
            float f4 = (1.0f - (1.0f / round2)) / 2.0f;
            fArr = new float[]{addDistance(rotation[0], f3), addDistance(rotation[1], f4), addDistance(rotation[2], f3), addDistance(rotation[3], f4), addDistance(rotation[4], f3), addDistance(rotation[5], f4), addDistance(rotation[6], f3), addDistance(rotation[7], f4)};
        } else {
            fArr2 = new float[]{CUBE[0] / round2, CUBE[1] / round, CUBE[2] / round2, CUBE[3] / round, CUBE[4] / round2, CUBE[5] / round, CUBE[6] / round2, CUBE[7] / round};
            fArr = rotation;
        }
        this.glCubeBuffer.clear();
        this.glCubeBuffer.put(fArr2).position(0);
        this.glTextureBuffer.clear();
        this.glTextureBuffer.put(fArr).position(0);
    }

    public void setRotationCamera(Rotation rotation, boolean z, boolean z2) {
        setRotation(rotation, z2, z);
    }

    public void setRotation(Rotation rotation) {
        this.rotation = rotation;
        adjustImageScaling();
    }

    public void setRotation(Rotation rotation, boolean z, boolean z2) {
        this.flipHorizontal = z;
        this.flipVertical = z2;
        setRotation(rotation);
    }

    public Rotation getRotation() {
        return this.rotation;
    }

    public boolean isFlippedHorizontally() {
        return this.flipHorizontal;
    }

    public boolean isFlippedVertically() {
        return this.flipVertical;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void runOnDraw(Runnable runnable) {
        synchronized (this.runOnDraw) {
            this.runOnDraw.add(runnable);
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void runOnDrawEnd(Runnable runnable) {
        synchronized (this.runOnDrawEnd) {
            this.runOnDrawEnd.add(runnable);
        }
    }
}
