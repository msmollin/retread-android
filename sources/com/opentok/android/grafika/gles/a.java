package com.opentok.android.grafika.gles;

import android.annotation.TargetApi;
import android.opengl.EGL14;
import android.opengl.EGLSurface;
import com.opentok.android.OtLog;

@TargetApi(19)
/* loaded from: classes.dex */
public class a {
    protected EglCore a;
    private EGLSurface b = EGL14.EGL_NO_SURFACE;
    private final OtLog.LogToken c = new OtLog.LogToken(this);

    /* JADX INFO: Access modifiers changed from: protected */
    public a(EglCore eglCore) {
        this.a = eglCore;
    }

    public void a() {
        this.a.a(this.b);
    }

    public void a(Object obj) {
        if (this.b != EGL14.EGL_NO_SURFACE) {
            throw new IllegalStateException("surface already created");
        }
        this.b = this.a.a(obj);
    }

    public void b() {
        this.a.b(this.b);
        this.b = EGL14.EGL_NO_SURFACE;
    }

    public boolean c() {
        boolean c = this.a.c(this.b);
        if (!c) {
            this.c.d("WARNING: swapBuffers() failed", new Object[0]);
        }
        return c;
    }
}
