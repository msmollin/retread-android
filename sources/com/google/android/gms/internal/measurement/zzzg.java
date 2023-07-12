package com.google.android.gms.internal.measurement;

/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: classes.dex */
public final class zzzg extends zzze {
    private final byte[] buffer;
    private int limit;
    private int pos;
    private final boolean zzbrh;
    private int zzbri;
    private int zzbrj;
    private int zzbrk;

    private zzzg(byte[] bArr, int i, int i2, boolean z) {
        super();
        this.zzbrk = Integer.MAX_VALUE;
        this.buffer = bArr;
        this.limit = i2 + i;
        this.pos = i;
        this.zzbrj = this.pos;
        this.zzbrh = z;
    }

    private final void zzta() {
        this.limit += this.zzbri;
        int i = this.limit - this.zzbrj;
        if (i <= this.zzbrk) {
            this.zzbri = 0;
            return;
        }
        this.zzbri = i - this.zzbrk;
        this.limit -= this.zzbri;
    }

    public final int zzaf(int i) throws zzzt {
        if (i >= 0) {
            int zzsz = i + zzsz();
            int i2 = this.zzbrk;
            if (zzsz <= i2) {
                this.zzbrk = zzsz;
                zzta();
                return i2;
            }
            throw zzzt.zztm();
        }
        throw zzzt.zztn();
    }

    @Override // com.google.android.gms.internal.measurement.zzze
    public final int zzsz() {
        return this.pos - this.zzbrj;
    }
}
