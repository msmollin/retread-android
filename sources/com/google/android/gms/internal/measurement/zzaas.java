package com.google.android.gms.internal.measurement;

/* loaded from: classes.dex */
final class zzaas {
    private static final zzaaq zzbto = zzuc();
    private static final zzaaq zzbtp = new zzaar();

    /* JADX INFO: Access modifiers changed from: package-private */
    public static zzaaq zzua() {
        return zzbto;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static zzaaq zzub() {
        return zzbtp;
    }

    private static zzaaq zzuc() {
        try {
            return (zzaaq) Class.forName("com.google.protobuf.NewInstanceSchemaFull").getDeclaredConstructor(new Class[0]).newInstance(new Object[0]);
        } catch (Exception unused) {
            return null;
        }
    }
}
