package com.opentok.android;

import android.content.Context;

/* loaded from: classes.dex */
class Utils {
    static boolean otcEngineInitialized;

    static {
        Loader.load();
        otcEngineInitialized = false;
    }

    Utils() {
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static int booleanToInt(boolean z) {
        return z ? 1 : 0;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static void initOtcEngine(Context context, BaseAudioDevice baseAudioDevice, boolean z, boolean z2, boolean z3, boolean z4) {
        if (otcEngineInitialized) {
            return;
        }
        init_otc_engine(context, baseAudioDevice, z, z2, z3, z4);
        otcEngineInitialized = true;
    }

    static native void init_otc_engine(Context context, BaseAudioDevice baseAudioDevice, boolean z, boolean z2, boolean z3, boolean z4);

    /* JADX INFO: Access modifiers changed from: package-private */
    public static boolean intToBoolean(int i) {
        return i != 0;
    }
}
