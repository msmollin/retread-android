package com.google.android.gms.internal.measurement;

/* loaded from: classes.dex */
final class zzyz extends zzzc {
    private final int zzbra;
    private final int zzbrb;

    /* JADX INFO: Access modifiers changed from: package-private */
    public zzyz(byte[] bArr, int i, int i2) {
        super(bArr);
        zzb(i, i + i2, bArr.length);
        this.zzbra = i;
        this.zzbrb = i2;
    }

    @Override // com.google.android.gms.internal.measurement.zzzc, com.google.android.gms.internal.measurement.zzyw
    public final int size() {
        return this.zzbrb;
    }

    @Override // com.google.android.gms.internal.measurement.zzzc, com.google.android.gms.internal.measurement.zzyw
    public final byte zzae(int i) {
        int size = size();
        if (((size - (i + 1)) | i) < 0) {
            if (i < 0) {
                StringBuilder sb = new StringBuilder(22);
                sb.append("Index < 0: ");
                sb.append(i);
                throw new ArrayIndexOutOfBoundsException(sb.toString());
            }
            StringBuilder sb2 = new StringBuilder(40);
            sb2.append("Index > length: ");
            sb2.append(i);
            sb2.append(", ");
            sb2.append(size);
            throw new ArrayIndexOutOfBoundsException(sb2.toString());
        }
        return this.zzbrc[this.zzbra + i];
    }

    @Override // com.google.android.gms.internal.measurement.zzzc
    protected final int zzsy() {
        return this.zzbra;
    }
}
