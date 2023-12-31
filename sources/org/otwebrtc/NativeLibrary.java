package org.otwebrtc;

/* loaded from: classes2.dex */
class NativeLibrary {
    private static String TAG = "NativeLibrary";
    private static boolean libraryLoaded;
    private static Object lock = new Object();

    /* loaded from: classes2.dex */
    static class DefaultLoader implements NativeLibraryLoader {
        @Override // org.otwebrtc.NativeLibraryLoader
        public boolean load(String str) {
            String str2 = NativeLibrary.TAG;
            Logging.d(str2, "Loading library: " + str);
            try {
                System.loadLibrary(str);
                return true;
            } catch (UnsatisfiedLinkError e) {
                String str3 = NativeLibrary.TAG;
                Logging.e(str3, "Failed to load native library: " + str, e);
                return false;
            }
        }
    }

    NativeLibrary() {
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static void initialize(NativeLibraryLoader nativeLibraryLoader, String str) {
        synchronized (lock) {
            if (libraryLoaded) {
                Logging.d(TAG, "Native library has already been loaded.");
                return;
            }
            String str2 = TAG;
            Logging.d(str2, "Loading native library: " + str);
            libraryLoaded = nativeLibraryLoader.load(str);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static boolean isLoaded() {
        boolean z;
        synchronized (lock) {
            z = libraryLoaded;
        }
        return z;
    }
}
