package com.daasuu.gpuv.player;

import android.graphics.SurfaceTexture;
import android.opengl.GLES20;
import android.opengl.Matrix;
import android.util.Log;
import android.view.Surface;
import com.daasuu.gpuv.egl.EglUtil;
import com.daasuu.gpuv.egl.GlFrameBufferObjectRenderer;
import com.daasuu.gpuv.egl.GlFramebufferObject;
import com.daasuu.gpuv.egl.GlPreviewFilter;
import com.daasuu.gpuv.egl.GlSurfaceTexture;
import com.daasuu.gpuv.egl.filter.GlFilter;
import com.daasuu.gpuv.egl.filter.GlLookUpTableFilter;
import com.google.android.exoplayer2.SimpleExoPlayer;
import javax.microedition.khronos.egl.EGLConfig;

/* loaded from: classes.dex */
public class GPUPlayerRenderer extends GlFrameBufferObjectRenderer implements SurfaceTexture.OnFrameAvailableListener {
    private static final String TAG = "GPUPlayerRenderer";
    private GlFramebufferObject filterFramebufferObject;
    private GlFilter glFilter;
    private final GPUPlayerView glPreview;
    private boolean isNewFilter;
    private GlPreviewFilter previewFilter;
    private GlSurfaceTexture previewTexture;
    private SimpleExoPlayer simpleExoPlayer;
    private int texName;
    private boolean updateSurface = false;
    private float[] MVPMatrix = new float[16];
    private float[] ProjMatrix = new float[16];
    private float[] MMatrix = new float[16];
    private float[] VMatrix = new float[16];
    private float[] STMatrix = new float[16];
    private float aspectRatio = 1.0f;

    /* JADX INFO: Access modifiers changed from: package-private */
    public GPUPlayerRenderer(GPUPlayerView gPUPlayerView) {
        Matrix.setIdentityM(this.STMatrix, 0);
        this.glPreview = gPUPlayerView;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void setGlFilter(final GlFilter glFilter) {
        this.glPreview.queueEvent(new Runnable() { // from class: com.daasuu.gpuv.player.GPUPlayerRenderer.1
            @Override // java.lang.Runnable
            public void run() {
                if (GPUPlayerRenderer.this.glFilter != null) {
                    GPUPlayerRenderer.this.glFilter.release();
                    if (GPUPlayerRenderer.this.glFilter instanceof GlLookUpTableFilter) {
                        ((GlLookUpTableFilter) GPUPlayerRenderer.this.glFilter).releaseLutBitmap();
                    }
                    GPUPlayerRenderer.this.glFilter = null;
                }
                GPUPlayerRenderer.this.glFilter = glFilter;
                GPUPlayerRenderer.this.isNewFilter = true;
                GPUPlayerRenderer.this.glPreview.requestRender();
            }
        });
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
        this.previewFilter = new GlPreviewFilter(this.previewTexture.getTextureTarget());
        this.previewFilter.setup();
        this.simpleExoPlayer.setVideoSurface(new Surface(this.previewTexture.getSurfaceTexture()));
        Matrix.setLookAtM(this.VMatrix, 0, 0.0f, 0.0f, 5.0f, 0.0f, 0.0f, 0.0f, 0.0f, 1.0f, 0.0f);
        synchronized (this) {
            this.updateSurface = false;
        }
        if (this.glFilter != null) {
            this.isNewFilter = true;
        }
        GLES20.glGetIntegerv(3379, iArr, 0);
    }

    @Override // com.daasuu.gpuv.egl.GlFrameBufferObjectRenderer
    public void onSurfaceChanged(int i, int i2) {
        String str = TAG;
        Log.d(str, "onSurfaceChanged width = " + i + "  height = " + i2);
        this.filterFramebufferObject.setup(i, i2);
        this.previewFilter.setFrameSize(i, i2);
        if (this.glFilter != null) {
            this.glFilter.setFrameSize(i, i2);
        }
        this.aspectRatio = i / i2;
        Matrix.frustumM(this.ProjMatrix, 0, -this.aspectRatio, this.aspectRatio, -1.0f, 1.0f, 5.0f, 7.0f);
        Matrix.setIdentityM(this.MMatrix, 0);
    }

    @Override // com.daasuu.gpuv.egl.GlFrameBufferObjectRenderer
    public void onDrawFrame(GlFramebufferObject glFramebufferObject) {
        synchronized (this) {
            if (this.updateSurface) {
                this.previewTexture.updateTexImage();
                this.previewTexture.getTransformMatrix(this.STMatrix);
                this.updateSurface = false;
            }
        }
        if (this.isNewFilter) {
            if (this.glFilter != null) {
                this.glFilter.setup();
                this.glFilter.setFrameSize(glFramebufferObject.getWidth(), glFramebufferObject.getHeight());
            }
            this.isNewFilter = false;
        }
        if (this.glFilter != null) {
            this.filterFramebufferObject.enable();
            GLES20.glViewport(0, 0, this.filterFramebufferObject.getWidth(), this.filterFramebufferObject.getHeight());
        }
        GLES20.glClear(16384);
        Matrix.multiplyMM(this.MVPMatrix, 0, this.VMatrix, 0, this.MMatrix, 0);
        Matrix.multiplyMM(this.MVPMatrix, 0, this.ProjMatrix, 0, this.MVPMatrix, 0);
        this.previewFilter.draw(this.texName, this.MVPMatrix, this.STMatrix, this.aspectRatio);
        if (this.glFilter != null) {
            glFramebufferObject.enable();
            GLES20.glClear(16384);
            this.glFilter.draw(this.filterFramebufferObject.getTexName(), glFramebufferObject);
        }
    }

    @Override // android.graphics.SurfaceTexture.OnFrameAvailableListener
    public synchronized void onFrameAvailable(SurfaceTexture surfaceTexture) {
        this.updateSurface = true;
        this.glPreview.requestRender();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void setSimpleExoPlayer(SimpleExoPlayer simpleExoPlayer) {
        this.simpleExoPlayer = simpleExoPlayer;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void release() {
        if (this.glFilter != null) {
            this.glFilter.release();
        }
        if (this.previewTexture != null) {
            this.previewTexture.release();
        }
    }
}
