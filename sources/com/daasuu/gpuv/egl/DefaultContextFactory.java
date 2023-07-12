package com.daasuu.gpuv.egl;

import android.opengl.GLSurfaceView;
import android.util.Log;
import javax.microedition.khronos.egl.EGL10;
import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.egl.EGLContext;
import javax.microedition.khronos.egl.EGLDisplay;

/* loaded from: classes.dex */
public class DefaultContextFactory implements GLSurfaceView.EGLContextFactory {
    private static final int EGL_CONTEXT_CLIENT_VERSION = 12440;
    private static final String TAG = "DefaultContextFactory";
    private int EGLContextClientVersion;

    public DefaultContextFactory(int i) {
        this.EGLContextClientVersion = i;
    }

    @Override // android.opengl.GLSurfaceView.EGLContextFactory
    public EGLContext createContext(EGL10 egl10, EGLDisplay eGLDisplay, EGLConfig eGLConfig) {
        return egl10.eglCreateContext(eGLDisplay, eGLConfig, EGL10.EGL_NO_CONTEXT, this.EGLContextClientVersion != 0 ? new int[]{EGL_CONTEXT_CLIENT_VERSION, this.EGLContextClientVersion, 12344} : null);
    }

    @Override // android.opengl.GLSurfaceView.EGLContextFactory
    public void destroyContext(EGL10 egl10, EGLDisplay eGLDisplay, EGLContext eGLContext) {
        if (egl10.eglDestroyContext(eGLDisplay, eGLContext)) {
            return;
        }
        Log.e(TAG, "display:" + eGLDisplay + " context: " + eGLContext);
        throw new RuntimeException("eglDestroyContext" + egl10.eglGetError());
    }
}
