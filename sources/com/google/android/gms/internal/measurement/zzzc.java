package com.google.android.gms.internal.measurement;

/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: classes.dex */
public class zzzc extends zzzb {
    protected final byte[] zzbrc;

    /* JADX INFO: Access modifiers changed from: package-private */
    public zzzc(byte[] bArr) {
        this.zzbrc = bArr;
    }

    @Override // com.google.android.gms.internal.measurement.zzyw
    public final boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if ((obj instanceof zzyw) && size() == ((zzyw) obj).size()) {
            if (size() == 0) {
                return true;
            }
            if (obj instanceof zzzc) {
                zzzc zzzcVar = (zzzc) obj;
                int zzsx = zzsx();
                int zzsx2 = zzzcVar.zzsx();
                if (zzsx == 0 || zzsx2 == 0 || zzsx == zzsx2) {
                    return zza(zzzcVar, 0, size());
                }
                return false;
            }
            return obj.equals(this);
        }
        return false;
    }

    @Override // com.google.android.gms.internal.measurement.zzyw
    public int size() {
        return this.zzbrc.length;
    }

    @Override // com.google.android.gms.internal.measurement.zzyw
    protected final int zza(int i, int i2, int i3) {
        return zzzr.zza(i, this.zzbrc, zzsy(), i3);
    }

    @Override // com.google.android.gms.internal.measurement.zzzb
    final boolean zza(zzyw zzywVar, int i, int i2) {
        if (i2 > zzywVar.size()) {
            int size = size();
            StringBuilder sb = new StringBuilder(40);
            sb.append("Length too large: ");
            sb.append(i2);
            sb.append(size);
            throw new IllegalArgumentException(sb.toString());
        } else if (i2 > zzywVar.size()) {
            int size2 = zzywVar.size();
            StringBuilder sb2 = new StringBuilder(59);
            sb2.append("Ran off end of other: 0, ");
            sb2.append(i2);
            sb2.append(", ");
            sb2.append(size2);
            throw new IllegalArgumentException(sb2.toString());
        } else if (zzywVar instanceof zzzc) {
            zzzc zzzcVar = (zzzc) zzywVar;
            byte[] bArr = this.zzbrc;
            byte[] bArr2 = zzzcVar.zzbrc;
            int zzsy = zzsy() + i2;
            int zzsy2 = zzsy();
            int zzsy3 = zzzcVar.zzsy();
            while (zzsy2 < zzsy) {
                if (bArr[zzsy2] != bArr2[zzsy3]) {
                    return false;
                }
                zzsy2++;
                zzsy3++;
            }
            return true;
        } else {
            return zzywVar.zzb(0, i2).equals(zzb(0, i2));
        }
    }

    @Override // com.google.android.gms.internal.measurement.zzyw
    public byte zzae(int i) {
        return this.zzbrc[i];
    }

    @Override // com.google.android.gms.internal.measurement.zzyw
    public final zzyw zzb(int i, int i2) {
        int zzb = zzb(0, i2, size());
        return zzb == 0 ? zzyw.zzbqx : new zzyz(this.zzbrc, zzsy(), zzb);
    }

    protected int zzsy() {
        return 0;
    }
}
