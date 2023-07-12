package com.google.android.gms.internal.measurement;

/* loaded from: classes.dex */
final class zzaai {
    private static final zzaag zzbtg = zztv();
    private static final zzaag zzbth = new zzaah();

    /* JADX INFO: Access modifiers changed from: package-private */
    public static zzaag zztt() {
        return zzbtg;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static zzaag zztu() {
        return zzbth;
    }

    private static zzaag zztv() {
        try {
            return (zzaag) Class.forName("com.google.protobuf.MapFieldSchemaFull").getDeclaredConstructor(new Class[0]).newInstance(new Object[0]);
        } catch (Exception unused) {
            return null;
        }
    }
}
