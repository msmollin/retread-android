package com.opentok.android;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Build;
import com.opentok.android.OtLog;

/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: classes.dex */
public class VideoRenderFactory {
    private static final OtLog.LogToken log = new OtLog.LogToken();
    private static boolean useTextureViews = false;

    VideoRenderFactory() {
    }

    @SuppressLint({"LogNotTimber"})
    public static BaseVideoRenderer constructRenderer(Context context) {
        if (!useTextureViews || Build.VERSION.SDK_INT < 19) {
            log.i("Using GLSurfaceViews for render", new Object[0]);
            return new DefaultVideoRenderer(context);
        }
        log.i("Using TextureViews for render", new Object[0]);
        return new TextureViewRenderer(context);
    }

    public static void useTextureViews(boolean z) {
        useTextureViews = z;
    }
}
