package com.daasuu.gpuv.camerarecorder.capture;

import android.opengl.EGL14;
import android.opengl.EGLConfig;
import android.opengl.EGLContext;
import android.opengl.EGLDisplay;
import android.opengl.EGLSurface;
import android.util.Log;

/* loaded from: classes.dex */
public class EglWrapper {
    private static final boolean DEBUG = false;
    private static final int EGL_RECORDABLE_ANDROID = 12610;
    private static final String TAG = "EglWrapper";
    private EGLConfig eglConfig = null;
    private EGLContext eglContext = EGL14.EGL_NO_CONTEXT;
    private EGLDisplay eglDisplay = EGL14.EGL_NO_DISPLAY;
    private EGLContext defaultContext = EGL14.EGL_NO_CONTEXT;

    /* JADX INFO: Access modifiers changed from: package-private */
    public EglWrapper(EGLContext eGLContext, boolean z, boolean z2) {
        init(eGLContext, z, z2);
    }

    public void release() {
        if (this.eglDisplay != EGL14.EGL_NO_DISPLAY) {
            destroyContext();
            EGL14.eglTerminate(this.eglDisplay);
            EGL14.eglReleaseThread();
        }
        this.eglDisplay = EGL14.EGL_NO_DISPLAY;
        this.eglContext = EGL14.EGL_NO_CONTEXT;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public EglSurface createFromSurface(Object obj) {
        EglSurface eglSurface = new EglSurface(this, obj);
        eglSurface.makeCurrent();
        return eglSurface;
    }

    public EGLContext getContext() {
        return this.eglContext;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public int querySurface(EGLSurface eGLSurface, int i) {
        int[] iArr = new int[1];
        EGL14.eglQuerySurface(this.eglDisplay, eGLSurface, i, iArr, 0);
        return iArr[0];
    }

    private void init(EGLContext eGLContext, boolean z, boolean z2) {
        if (this.eglDisplay != EGL14.EGL_NO_DISPLAY) {
            throw new RuntimeException("EGL already set up");
        }
        this.eglDisplay = EGL14.eglGetDisplay(0);
        if (this.eglDisplay == EGL14.EGL_NO_DISPLAY) {
            throw new RuntimeException("eglGetDisplay failed");
        }
        int[] iArr = new int[2];
        if (!EGL14.eglInitialize(this.eglDisplay, iArr, 0, iArr, 1)) {
            this.eglDisplay = null;
            throw new RuntimeException("eglInitialize failed");
        }
        if (eGLContext == null) {
            eGLContext = EGL14.EGL_NO_CONTEXT;
        }
        if (this.eglContext == EGL14.EGL_NO_CONTEXT) {
            this.eglConfig = getConfig(z, z2);
            if (this.eglConfig == null) {
                throw new RuntimeException("chooseConfig failed");
            }
            this.eglContext = createContext(eGLContext);
        }
        EGL14.eglQueryContext(this.eglDisplay, this.eglContext, 12440, new int[1], 0);
        makeDefault();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean makeCurrent(EGLSurface eGLSurface) {
        EGLDisplay eGLDisplay = this.eglDisplay;
        if (eGLSurface == null || eGLSurface == EGL14.EGL_NO_SURFACE) {
            if (EGL14.eglGetError() == 12299) {
                Log.e(TAG, "makeCurrent:returned EGL_BAD_NATIVE_WINDOW.");
            }
            return false;
        } else if (EGL14.eglMakeCurrent(this.eglDisplay, eGLSurface, eGLSurface, this.eglContext)) {
            return true;
        } else {
            Log.w(TAG, "eglMakeCurrent:" + EGL14.eglGetError());
            return false;
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void makeDefault() {
        if (EGL14.eglMakeCurrent(this.eglDisplay, EGL14.EGL_NO_SURFACE, EGL14.EGL_NO_SURFACE, EGL14.EGL_NO_CONTEXT)) {
            return;
        }
        Log.w("TAG", "makeDefault" + EGL14.eglGetError());
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public int swap(EGLSurface eGLSurface) {
        if (EGL14.eglSwapBuffers(this.eglDisplay, eGLSurface)) {
            return 12288;
        }
        return EGL14.eglGetError();
    }

    private EGLContext createContext(EGLContext eGLContext) {
        EGLContext eglCreateContext = EGL14.eglCreateContext(this.eglDisplay, this.eglConfig, eGLContext, new int[]{12440, 2, 12344}, 0);
        checkEglError("eglCreateContext");
        return eglCreateContext;
    }

    private void destroyContext() {
        if (!EGL14.eglDestroyContext(this.eglDisplay, this.eglContext)) {
            Log.e("destroyContext", "display:" + this.eglDisplay + " context: " + this.eglContext);
            StringBuilder sb = new StringBuilder();
            sb.append("eglDestroyContex:");
            sb.append(EGL14.eglGetError());
            Log.e(TAG, sb.toString());
        }
        this.eglContext = EGL14.EGL_NO_CONTEXT;
        if (this.defaultContext != EGL14.EGL_NO_CONTEXT) {
            if (!EGL14.eglDestroyContext(this.eglDisplay, this.defaultContext)) {
                Log.e("destroyContext", "display:" + this.eglDisplay + " context: " + this.defaultContext);
                StringBuilder sb2 = new StringBuilder();
                sb2.append("eglDestroyContex:");
                sb2.append(EGL14.eglGetError());
                Log.e(TAG, sb2.toString());
            }
            this.defaultContext = EGL14.EGL_NO_CONTEXT;
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public EGLSurface createWindowSurface(Object obj) {
        try {
            return EGL14.eglCreateWindowSurface(this.eglDisplay, this.eglConfig, obj, new int[]{12344}, 0);
        } catch (IllegalArgumentException e) {
            Log.e(TAG, "eglCreateWindowSurface", e);
            return null;
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void destroyWindowSurface(EGLSurface eGLSurface) {
        if (eGLSurface != EGL14.EGL_NO_SURFACE) {
            EGL14.eglMakeCurrent(this.eglDisplay, EGL14.EGL_NO_SURFACE, EGL14.EGL_NO_SURFACE, EGL14.EGL_NO_CONTEXT);
            EGL14.eglDestroySurface(this.eglDisplay, eGLSurface);
        }
        EGLSurface eGLSurface2 = EGL14.EGL_NO_SURFACE;
    }

    private void checkEglError(String str) {
        int eglGetError = EGL14.eglGetError();
        if (eglGetError == 12288) {
            return;
        }
        throw new RuntimeException(str + ": EGL error: 0x" + Integer.toHexString(eglGetError));
    }

    private EGLConfig getConfig(boolean z, boolean z2) {
        int[] iArr = {12352, 4, 12324, 8, 12323, 8, 12322, 8, 12321, 8, 12344, 12344, 12344, 12344, 12344, 12344, 12344};
        int i = 10;
        if (z) {
            iArr[10] = 12325;
            i = 12;
            iArr[11] = 16;
        }
        if (z2) {
            int i2 = i + 1;
            iArr[i] = 12610;
            i = i2 + 1;
            iArr[i2] = 1;
        }
        for (int length = iArr.length - 1; length >= i; length--) {
            iArr[length] = 12344;
        }
        EGLConfig[] eGLConfigArr = new EGLConfig[1];
        if (!EGL14.eglChooseConfig(this.eglDisplay, iArr, 0, eGLConfigArr, 0, eGLConfigArr.length, new int[1], 0)) {
            Log.w(TAG, "unable to find RGBA8888 /  EGLConfig");
            return null;
        }
        return eGLConfigArr[0];
    }
}
