package com.google.android.gms.internal.measurement;

/* loaded from: classes.dex */
final class zzzl {
    private static final zzzj<?> zzbrq = new zzzk();
    private static final zzzj<?> zzbrr = zztf();

    private static zzzj<?> zztf() {
        try {
            return (zzzj) Class.forName("com.google.protobuf.ExtensionSchemaFull").getDeclaredConstructor(new Class[0]).newInstance(new Object[0]);
        } catch (Exception unused) {
            return null;
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static zzzj<?> zztg() {
        return zzbrq;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static zzzj<?> zzth() {
        if (zzbrr != null) {
            return zzbrr;
        }
        throw new IllegalStateException("Protobuf runtime is not correctly loaded.");
    }
}
