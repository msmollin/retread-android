package com.daasuu.gpuv.composer;

import android.graphics.SurfaceTexture;
import android.opengl.EGL14;
import android.opengl.EGLContext;
import android.opengl.EGLDisplay;
import android.opengl.EGLSurface;
import android.opengl.GLES20;
import android.opengl.Matrix;
import android.util.Size;
import android.view.Surface;
import com.daasuu.gpuv.egl.EglUtil;
import com.daasuu.gpuv.egl.GlFramebufferObject;
import com.daasuu.gpuv.egl.GlPreviewFilter;
import com.daasuu.gpuv.egl.GlSurfaceTexture;
import com.daasuu.gpuv.egl.filter.GlFilter;

/* loaded from: classes.dex */
class DecoderSurface implements SurfaceTexture.OnFrameAvailableListener {
    private static final String TAG = "DecoderSurface";
    private static final boolean VERBOSE = false;
    private FillModeCustomItem fillModeCustomItem;
    private GlFilter filter;
    private GlFramebufferObject filterFramebufferObject;
    private boolean frameAvailable;
    private GlFramebufferObject framebufferObject;
    private Size inputResolution;
    private GlFilter normalShader;
    private Size outputResolution;
    private GlPreviewFilter previewShader;
    private GlSurfaceTexture previewTexture;
    private Surface surface;
    private int texName;
    private EGLDisplay eglDisplay = EGL14.EGL_NO_DISPLAY;
    private EGLContext eglContext = EGL14.EGL_NO_CONTEXT;
    private EGLSurface eglSurface = EGL14.EGL_NO_SURFACE;
    private Object frameSyncObject = new Object();
    private float[] MVPMatrix = new float[16];
    private float[] ProjMatrix = new float[16];
    private float[] MMatrix = new float[16];
    private float[] VMatrix = new float[16];
    private float[] STMatrix = new float[16];
    private Rotation rotation = Rotation.NORMAL;
    private FillMode fillMode = FillMode.PRESERVE_ASPECT_FIT;
    private boolean flipVertical = false;
    private boolean flipHorizontal = false;

    /* JADX INFO: Access modifiers changed from: package-private */
    public DecoderSurface(GlFilter glFilter) {
        this.filter = glFilter;
        setup();
    }

    private void setup() {
        this.filter.setup();
        this.framebufferObject = new GlFramebufferObject();
        this.normalShader = new GlFilter();
        this.normalShader.setup();
        int[] iArr = new int[1];
        GLES20.glGenTextures(iArr.length, iArr, 0);
        this.texName = iArr[0];
        this.previewTexture = new GlSurfaceTexture(this.texName);
        this.previewTexture.setOnFrameAvailableListener(this);
        this.surface = new Surface(this.previewTexture.getSurfaceTexture());
        GLES20.glBindTexture(this.previewTexture.getTextureTarget(), this.texName);
        EglUtil.setupSampler(this.previewTexture.getTextureTarget(), 9729, 9728);
        GLES20.glBindTexture(3553, 0);
        this.previewShader = new GlPreviewFilter(this.previewTexture.getTextureTarget());
        this.previewShader.setup();
        this.filterFramebufferObject = new GlFramebufferObject();
        Matrix.setLookAtM(this.VMatrix, 0, 0.0f, 0.0f, 5.0f, 0.0f, 0.0f, 0.0f, 0.0f, 1.0f, 0.0f);
        GLES20.glGetIntegerv(3379, iArr, 0);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void release() {
        if (this.eglDisplay != EGL14.EGL_NO_DISPLAY) {
            EGL14.eglDestroySurface(this.eglDisplay, this.eglSurface);
            EGL14.eglDestroyContext(this.eglDisplay, this.eglContext);
            EGL14.eglReleaseThread();
            EGL14.eglTerminate(this.eglDisplay);
        }
        this.surface.release();
        this.previewTexture.release();
        this.eglDisplay = EGL14.EGL_NO_DISPLAY;
        this.eglContext = EGL14.EGL_NO_CONTEXT;
        this.eglSurface = EGL14.EGL_NO_SURFACE;
        this.filter.release();
        this.filter = null;
        this.surface = null;
        this.previewTexture = null;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public Surface getSurface() {
        return this.surface;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void awaitNewImage() {
        synchronized (this.frameSyncObject) {
            while (!this.frameAvailable) {
                try {
                    this.frameSyncObject.wait(10000L);
                    if (!this.frameAvailable) {
                        throw new RuntimeException("Surface frame wait timed out");
                    }
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
            this.frameAvailable = false;
        }
        this.previewTexture.updateTexImage();
        this.previewTexture.getTransformMatrix(this.STMatrix);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void drawImage() {
        this.framebufferObject.enable();
        GLES20.glViewport(0, 0, this.framebufferObject.getWidth(), this.framebufferObject.getHeight());
        if (this.filter != null) {
            this.filterFramebufferObject.enable();
            GLES20.glViewport(0, 0, this.filterFramebufferObject.getWidth(), this.filterFramebufferObject.getHeight());
        }
        GLES20.glClear(16384);
        Matrix.multiplyMM(this.MVPMatrix, 0, this.VMatrix, 0, this.MMatrix, 0);
        Matrix.multiplyMM(this.MVPMatrix, 0, this.ProjMatrix, 0, this.MVPMatrix, 0);
        float f = this.flipHorizontal ? -1.0f : 1.0f;
        float f2 = this.flipVertical ? -1.0f : 1.0f;
        switch (this.fillMode) {
            case PRESERVE_ASPECT_FIT:
                float[] scaleAspectFit = FillMode.getScaleAspectFit(this.rotation.getRotation(), this.inputResolution.getWidth(), this.inputResolution.getHeight(), this.outputResolution.getWidth(), this.outputResolution.getHeight());
                Matrix.scaleM(this.MVPMatrix, 0, scaleAspectFit[0] * f, scaleAspectFit[1] * f2, 1.0f);
                if (this.rotation != Rotation.NORMAL) {
                    Matrix.rotateM(this.MVPMatrix, 0, -this.rotation.getRotation(), 0.0f, 0.0f, 1.0f);
                    break;
                }
                break;
            case PRESERVE_ASPECT_CROP:
                float[] scaleAspectCrop = FillMode.getScaleAspectCrop(this.rotation.getRotation(), this.inputResolution.getWidth(), this.inputResolution.getHeight(), this.outputResolution.getWidth(), this.outputResolution.getHeight());
                Matrix.scaleM(this.MVPMatrix, 0, scaleAspectCrop[0] * f, scaleAspectCrop[1] * f2, 1.0f);
                if (this.rotation != Rotation.NORMAL) {
                    Matrix.rotateM(this.MVPMatrix, 0, -this.rotation.getRotation(), 0.0f, 0.0f, 1.0f);
                    break;
                }
                break;
            case CUSTOM:
                if (this.fillModeCustomItem != null) {
                    Matrix.translateM(this.MVPMatrix, 0, this.fillModeCustomItem.getTranslateX(), -this.fillModeCustomItem.getTranslateY(), 0.0f);
                    float[] scaleAspectCrop2 = FillMode.getScaleAspectCrop(this.rotation.getRotation(), this.inputResolution.getWidth(), this.inputResolution.getHeight(), this.outputResolution.getWidth(), this.outputResolution.getHeight());
                    if (this.fillModeCustomItem.getRotate() == 0.0f || this.fillModeCustomItem.getRotate() == 180.0f) {
                        Matrix.scaleM(this.MVPMatrix, 0, this.fillModeCustomItem.getScale() * scaleAspectCrop2[0] * f, this.fillModeCustomItem.getScale() * scaleAspectCrop2[1] * f2, 1.0f);
                    } else {
                        Matrix.scaleM(this.MVPMatrix, 0, this.fillModeCustomItem.getScale() * scaleAspectCrop2[0] * (1.0f / this.fillModeCustomItem.getVideoWidth()) * this.fillModeCustomItem.getVideoHeight() * f, this.fillModeCustomItem.getScale() * scaleAspectCrop2[1] * (this.fillModeCustomItem.getVideoWidth() / this.fillModeCustomItem.getVideoHeight()) * f2, 1.0f);
                    }
                    Matrix.rotateM(this.MVPMatrix, 0, -(this.rotation.getRotation() + this.fillModeCustomItem.getRotate()), 0.0f, 0.0f, 1.0f);
                    break;
                }
                break;
        }
        this.previewShader.draw(this.texName, this.MVPMatrix, this.STMatrix, 1.0f);
        if (this.filter != null) {
            this.framebufferObject.enable();
            GLES20.glClear(16384);
            this.filter.draw(this.filterFramebufferObject.getTexName(), this.framebufferObject);
        }
        GLES20.glBindFramebuffer(36160, 0);
        GLES20.glViewport(0, 0, this.framebufferObject.getWidth(), this.framebufferObject.getHeight());
        GLES20.glClear(16640);
        this.normalShader.draw(this.framebufferObject.getTexName(), null);
    }

    @Override // android.graphics.SurfaceTexture.OnFrameAvailableListener
    public void onFrameAvailable(SurfaceTexture surfaceTexture) {
        synchronized (this.frameSyncObject) {
            if (this.frameAvailable) {
                throw new RuntimeException("frameAvailable already set, frame could be dropped");
            }
            this.frameAvailable = true;
            this.frameSyncObject.notifyAll();
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void setRotation(Rotation rotation) {
        this.rotation = rotation;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void setOutputResolution(Size size) {
        this.outputResolution = size;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void setFillMode(FillMode fillMode) {
        this.fillMode = fillMode;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void setInputResolution(Size size) {
        this.inputResolution = size;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void setFillModeCustomItem(FillModeCustomItem fillModeCustomItem) {
        this.fillModeCustomItem = fillModeCustomItem;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void setFlipVertical(boolean z) {
        this.flipVertical = z;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void setFlipHorizontal(boolean z) {
        this.flipHorizontal = z;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void completeParams() {
        int width = this.outputResolution.getWidth();
        int height = this.outputResolution.getHeight();
        this.framebufferObject.setup(width, height);
        this.normalShader.setFrameSize(width, height);
        this.filterFramebufferObject.setup(width, height);
        this.previewShader.setFrameSize(width, height);
        Matrix.frustumM(this.ProjMatrix, 0, -1.0f, 1.0f, -1.0f, 1.0f, 5.0f, 7.0f);
        Matrix.setIdentityM(this.MMatrix, 0);
        if (this.filter != null) {
            this.filter.setFrameSize(width, height);
        }
    }
}
