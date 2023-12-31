package com.google.android.gms.common.util;

import android.os.StrictMode;

/* loaded from: classes.dex */
public class StrictModeUtils {
    public static StrictMode.ThreadPolicy setDynamiteThreadPolicy() {
        StrictMode.noteSlowCall("gcore.dynamite");
        StrictMode.ThreadPolicy threadPolicy = StrictMode.getThreadPolicy();
        StrictMode.setThreadPolicy(StrictMode.ThreadPolicy.LAX);
        return threadPolicy;
    }
}
