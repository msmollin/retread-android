package com.google.android.gms.internal.measurement;

import java.io.IOException;

/* loaded from: classes.dex */
public final class zzkr extends zzaby<zzkr> {
    public long[] zzauk = zzach.zzbxm;
    public long[] zzaul = zzach.zzbxm;

    public zzkr() {
        this.zzbww = null;
        this.zzbxh = -1;
    }

    public final boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (obj instanceof zzkr) {
            zzkr zzkrVar = (zzkr) obj;
            if (zzacc.equals(this.zzauk, zzkrVar.zzauk) && zzacc.equals(this.zzaul, zzkrVar.zzaul)) {
                return (this.zzbww == null || this.zzbww.isEmpty()) ? zzkrVar.zzbww == null || zzkrVar.zzbww.isEmpty() : this.zzbww.equals(zzkrVar.zzbww);
            }
            return false;
        }
        return false;
    }

    public final int hashCode() {
        return ((((((getClass().getName().hashCode() + 527) * 31) + zzacc.hashCode(this.zzauk)) * 31) + zzacc.hashCode(this.zzaul)) * 31) + ((this.zzbww == null || this.zzbww.isEmpty()) ? 0 : this.zzbww.hashCode());
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.google.android.gms.internal.measurement.zzaby, com.google.android.gms.internal.measurement.zzace
    public final int zza() {
        int zza = super.zza();
        if (this.zzauk != null && this.zzauk.length > 0) {
            int i = 0;
            for (int i2 = 0; i2 < this.zzauk.length; i2++) {
                i += zzabw.zzao(this.zzauk[i2]);
            }
            zza = zza + i + (this.zzauk.length * 1);
        }
        if (this.zzaul == null || this.zzaul.length <= 0) {
            return zza;
        }
        int i3 = 0;
        for (int i4 = 0; i4 < this.zzaul.length; i4++) {
            i3 += zzabw.zzao(this.zzaul[i4]);
        }
        return zza + i3 + (this.zzaul.length * 1);
    }

    @Override // com.google.android.gms.internal.measurement.zzaby, com.google.android.gms.internal.measurement.zzace
    public final void zza(zzabw zzabwVar) throws IOException {
        if (this.zzauk != null && this.zzauk.length > 0) {
            for (int i = 0; i < this.zzauk.length; i++) {
                zzabwVar.zza(1, this.zzauk[i]);
            }
        }
        if (this.zzaul != null && this.zzaul.length > 0) {
            for (int i2 = 0; i2 < this.zzaul.length; i2++) {
                zzabwVar.zza(2, this.zzaul[i2]);
            }
        }
        super.zza(zzabwVar);
    }

    @Override // com.google.android.gms.internal.measurement.zzace
    public final /* synthetic */ zzace zzb(zzabv zzabvVar) throws IOException {
        int zzaf;
        while (true) {
            int zzuw = zzabvVar.zzuw();
            if (zzuw == 0) {
                return this;
            }
            if (zzuw != 8) {
                if (zzuw == 10) {
                    zzaf = zzabvVar.zzaf(zzabvVar.zzuy());
                    int position = zzabvVar.getPosition();
                    int i = 0;
                    while (zzabvVar.zzvc() > 0) {
                        zzabvVar.zzuz();
                        i++;
                    }
                    zzabvVar.zzam(position);
                    int length = this.zzauk == null ? 0 : this.zzauk.length;
                    long[] jArr = new long[i + length];
                    if (length != 0) {
                        System.arraycopy(this.zzauk, 0, jArr, 0, length);
                    }
                    while (length < jArr.length) {
                        jArr[length] = zzabvVar.zzuz();
                        length++;
                    }
                    this.zzauk = jArr;
                } else if (zzuw == 16) {
                    int zzb = zzach.zzb(zzabvVar, 16);
                    int length2 = this.zzaul == null ? 0 : this.zzaul.length;
                    long[] jArr2 = new long[zzb + length2];
                    if (length2 != 0) {
                        System.arraycopy(this.zzaul, 0, jArr2, 0, length2);
                    }
                    while (length2 < jArr2.length - 1) {
                        jArr2[length2] = zzabvVar.zzuz();
                        zzabvVar.zzuw();
                        length2++;
                    }
                    jArr2[length2] = zzabvVar.zzuz();
                    this.zzaul = jArr2;
                } else if (zzuw == 18) {
                    zzaf = zzabvVar.zzaf(zzabvVar.zzuy());
                    int position2 = zzabvVar.getPosition();
                    int i2 = 0;
                    while (zzabvVar.zzvc() > 0) {
                        zzabvVar.zzuz();
                        i2++;
                    }
                    zzabvVar.zzam(position2);
                    int length3 = this.zzaul == null ? 0 : this.zzaul.length;
                    long[] jArr3 = new long[i2 + length3];
                    if (length3 != 0) {
                        System.arraycopy(this.zzaul, 0, jArr3, 0, length3);
                    }
                    while (length3 < jArr3.length) {
                        jArr3[length3] = zzabvVar.zzuz();
                        length3++;
                    }
                    this.zzaul = jArr3;
                } else if (!super.zza(zzabvVar, zzuw)) {
                    return this;
                }
                zzabvVar.zzal(zzaf);
            } else {
                int zzb2 = zzach.zzb(zzabvVar, 8);
                int length4 = this.zzauk == null ? 0 : this.zzauk.length;
                long[] jArr4 = new long[zzb2 + length4];
                if (length4 != 0) {
                    System.arraycopy(this.zzauk, 0, jArr4, 0, length4);
                }
                while (length4 < jArr4.length - 1) {
                    jArr4[length4] = zzabvVar.zzuz();
                    zzabvVar.zzuw();
                    length4++;
                }
                jArr4[length4] = zzabvVar.zzuz();
                this.zzauk = jArr4;
            }
        }
    }
}
