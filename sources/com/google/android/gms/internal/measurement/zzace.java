package com.google.android.gms.internal.measurement;

import java.io.IOException;

/* loaded from: classes.dex */
public abstract class zzace {
    protected volatile int zzbxh = -1;

    public static final <T extends zzace> T zza(T t, byte[] bArr) throws zzacd {
        return (T) zzb(t, bArr, 0, bArr.length);
    }

    public static final void zza(zzace zzaceVar, byte[] bArr, int i, int i2) {
        try {
            zzabw zzb = zzabw.zzb(bArr, 0, i2);
            zzaceVar.zza(zzb);
            zzb.zzve();
        } catch (IOException e) {
            throw new RuntimeException("Serializing to a byte array threw an IOException (should never happen).", e);
        }
    }

    private static final <T extends zzace> T zzb(T t, byte[] bArr, int i, int i2) throws zzacd {
        try {
            zzabv zza = zzabv.zza(bArr, 0, i2);
            t.zzb(zza);
            zza.zzaj(0);
            return t;
        } catch (zzacd e) {
            throw e;
        } catch (IOException e2) {
            throw new RuntimeException("Reading from a byte array threw an IOException (should never happen).", e2);
        }
    }

    public String toString() {
        return zzacf.zzc(this);
    }

    protected int zza() {
        return 0;
    }

    public void zza(zzabw zzabwVar) throws IOException {
    }

    public abstract zzace zzb(zzabv zzabvVar) throws IOException;

    @Override // 
    /* renamed from: zzvf */
    public zzace clone() throws CloneNotSupportedException {
        return (zzace) super.clone();
    }

    public final int zzvl() {
        if (this.zzbxh < 0) {
            zzvm();
        }
        return this.zzbxh;
    }

    public final int zzvm() {
        int zza = zza();
        this.zzbxh = zza;
        return zza;
    }
}
