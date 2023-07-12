package com.daasuu.gpuv.camerarecorder.capture;

import android.graphics.SurfaceTexture;
import android.opengl.EGL14;
import android.opengl.EGLContext;
import android.opengl.EGLSurface;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

/* loaded from: classes.dex */
public class EglSurface {
    private static final boolean DEBUG = false;
    private static final String TAG = "EglWrapper";
    private final EglWrapper egl;
    private EGLSurface eglSurface;
    private final int height;
    private final int width;

    /* JADX INFO: Access modifiers changed from: package-private */
    public EglSurface(EglWrapper eglWrapper, Object obj) {
        this.eglSurface = EGL14.EGL_NO_SURFACE;
        if (!(obj instanceof SurfaceView) && !(obj instanceof Surface) && !(obj instanceof SurfaceHolder) && !(obj instanceof SurfaceTexture)) {
            throw new IllegalArgumentException("unsupported surface");
        }
        this.egl = eglWrapper;
        this.eglSurface = this.egl.createWindowSurface(obj);
        this.width = this.egl.querySurface(this.eglSurface, 12375);
        this.height = this.egl.querySurface(this.eglSurface, 12374);
    }

    public void makeCurrent() {
        this.egl.makeCurrent(this.eglSurface);
    }

    public void swap() {
        this.egl.swap(this.eglSurface);
    }

    public EGLContext getContext() {
        return this.egl.getContext();
    }

    public void release() {
        this.egl.makeDefault();
        this.egl.destroyWindowSurface(this.eglSurface);
        this.eglSurface = EGL14.EGL_NO_SURFACE;
    }

    public int getWidth() {
        return this.width;
    }

    public int getHeight() {
        return this.height;
    }
}
