package com.google.android.gms.internal.measurement;

/* loaded from: classes.dex */
final class zzyv {
    private static final Class<?> zzbqv = zzfh("libcore.io.Memory");
    private static final boolean zzbqw;

    static {
        zzbqw = zzfh("org.robolectric.Robolectric") != null;
    }

    private static <T> Class<T> zzfh(String str) {
        try {
            return (Class<T>) Class.forName(str);
        } catch (Throwable unused) {
            return null;
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static boolean zzsv() {
        return (zzbqv == null || zzbqw) ? false : true;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static Class<?> zzsw() {
        return zzbqv;
    }
}
