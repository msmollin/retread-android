package com.opentok.android.grafika.gles;

import android.annotation.TargetApi;
import android.graphics.SurfaceTexture;
import android.view.Surface;

@TargetApi(19)
/* loaded from: classes.dex */
public class WindowSurface extends a {
    private Surface d;
    private boolean e;

    public WindowSurface(EglCore eglCore, SurfaceTexture surfaceTexture) {
        super(eglCore);
        a(surfaceTexture);
    }

    public void d() {
        b();
        Surface surface = this.d;
        if (surface != null) {
            if (this.e) {
                surface.release();
            }
            this.d = null;
        }
    }
}
