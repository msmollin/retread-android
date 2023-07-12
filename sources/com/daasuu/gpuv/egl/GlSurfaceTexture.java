package com.daasuu.gpuv.egl;

import android.graphics.SurfaceTexture;

/* loaded from: classes.dex */
public class GlSurfaceTexture implements SurfaceTexture.OnFrameAvailableListener {
    private SurfaceTexture.OnFrameAvailableListener onFrameAvailableListener;
    private SurfaceTexture surfaceTexture;

    public int getTextureTarget() {
        return 36197;
    }

    public GlSurfaceTexture(int i) {
        this.surfaceTexture = new SurfaceTexture(i);
        this.surfaceTexture.setOnFrameAvailableListener(this);
    }

    public void setOnFrameAvailableListener(SurfaceTexture.OnFrameAvailableListener onFrameAvailableListener) {
        this.onFrameAvailableListener = onFrameAvailableListener;
    }

    public void updateTexImage() {
        this.surfaceTexture.updateTexImage();
    }

    public void getTransformMatrix(float[] fArr) {
        this.surfaceTexture.getTransformMatrix(fArr);
    }

    public SurfaceTexture getSurfaceTexture() {
        return this.surfaceTexture;
    }

    @Override // android.graphics.SurfaceTexture.OnFrameAvailableListener
    public void onFrameAvailable(SurfaceTexture surfaceTexture) {
        if (this.onFrameAvailableListener != null) {
            this.onFrameAvailableListener.onFrameAvailable(this.surfaceTexture);
        }
    }

    public void release() {
        this.surfaceTexture.release();
    }
}
