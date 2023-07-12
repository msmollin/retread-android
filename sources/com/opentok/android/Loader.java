package com.opentok.android;

/* loaded from: classes.dex */
class Loader {
    private static boolean loaded = false;
    private static final String nativeLibsCustomPath = System.getProperty("native_libs_custom_directory_absolute_path");

    Loader() {
    }

    public static synchronized void load() {
        synchronized (Loader.class) {
            if (loaded) {
                return;
            }
            String str = nativeLibsCustomPath;
            if (str != null) {
                System.load(str);
            } else {
                System.loadLibrary("opentok");
            }
            loaded = true;
        }
    }
}
