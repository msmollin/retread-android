package com.google.android.gms.internal.measurement;

/* loaded from: classes.dex */
public abstract class zzze {
    private static volatile boolean zzbrg = false;
    int zzbrd;
    private int zzbre;
    private boolean zzbrf;

    private zzze() {
        this.zzbrd = 100;
        this.zzbre = Integer.MAX_VALUE;
        this.zzbrf = false;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static zzze zza(byte[] bArr, int i, int i2, boolean z) {
        zzzg zzzgVar = new zzzg(bArr, i, i2);
        try {
            zzzgVar.zzaf(i2);
            return zzzgVar;
        } catch (zzzt e) {
            throw new IllegalArgumentException(e);
        }
    }

    public abstract int zzsz();
}
