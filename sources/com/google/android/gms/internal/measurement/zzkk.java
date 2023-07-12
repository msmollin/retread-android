package com.google.android.gms.internal.measurement;

import java.io.IOException;

/* loaded from: classes.dex */
public final class zzkk extends zzaby<zzkk> {
    public Long zzasp = null;
    public String zzadm = null;
    private Integer zzasq = null;
    public zzkl[] zzasr = zzkl.zzlj();
    public zzkj[] zzass = zzkj.zzli();
    public zzkd[] zzast = zzkd.zzle();

    public zzkk() {
        this.zzbww = null;
        this.zzbxh = -1;
    }

    public final boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (obj instanceof zzkk) {
            zzkk zzkkVar = (zzkk) obj;
            if (this.zzasp == null) {
                if (zzkkVar.zzasp != null) {
                    return false;
                }
            } else if (!this.zzasp.equals(zzkkVar.zzasp)) {
                return false;
            }
            if (this.zzadm == null) {
                if (zzkkVar.zzadm != null) {
                    return false;
                }
            } else if (!this.zzadm.equals(zzkkVar.zzadm)) {
                return false;
            }
            if (this.zzasq == null) {
                if (zzkkVar.zzasq != null) {
                    return false;
                }
            } else if (!this.zzasq.equals(zzkkVar.zzasq)) {
                return false;
            }
            if (zzacc.equals(this.zzasr, zzkkVar.zzasr) && zzacc.equals(this.zzass, zzkkVar.zzass) && zzacc.equals(this.zzast, zzkkVar.zzast)) {
                return (this.zzbww == null || this.zzbww.isEmpty()) ? zzkkVar.zzbww == null || zzkkVar.zzbww.isEmpty() : this.zzbww.equals(zzkkVar.zzbww);
            }
            return false;
        }
        return false;
    }

    public final int hashCode() {
        int i = 0;
        int hashCode = (((((((((((((getClass().getName().hashCode() + 527) * 31) + (this.zzasp == null ? 0 : this.zzasp.hashCode())) * 31) + (this.zzadm == null ? 0 : this.zzadm.hashCode())) * 31) + (this.zzasq == null ? 0 : this.zzasq.hashCode())) * 31) + zzacc.hashCode(this.zzasr)) * 31) + zzacc.hashCode(this.zzass)) * 31) + zzacc.hashCode(this.zzast)) * 31;
        if (this.zzbww != null && !this.zzbww.isEmpty()) {
            i = this.zzbww.hashCode();
        }
        return hashCode + i;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.google.android.gms.internal.measurement.zzaby, com.google.android.gms.internal.measurement.zzace
    public final int zza() {
        int zza = super.zza();
        if (this.zzasp != null) {
            zza += zzabw.zzc(1, this.zzasp.longValue());
        }
        if (this.zzadm != null) {
            zza += zzabw.zzc(2, this.zzadm);
        }
        if (this.zzasq != null) {
            zza += zzabw.zzf(3, this.zzasq.intValue());
        }
        if (this.zzasr != null && this.zzasr.length > 0) {
            int i = zza;
            for (int i2 = 0; i2 < this.zzasr.length; i2++) {
                zzkl zzklVar = this.zzasr[i2];
                if (zzklVar != null) {
                    i += zzabw.zzb(4, zzklVar);
                }
            }
            zza = i;
        }
        if (this.zzass != null && this.zzass.length > 0) {
            int i3 = zza;
            for (int i4 = 0; i4 < this.zzass.length; i4++) {
                zzkj zzkjVar = this.zzass[i4];
                if (zzkjVar != null) {
                    i3 += zzabw.zzb(5, zzkjVar);
                }
            }
            zza = i3;
        }
        if (this.zzast != null && this.zzast.length > 0) {
            for (int i5 = 0; i5 < this.zzast.length; i5++) {
                zzkd zzkdVar = this.zzast[i5];
                if (zzkdVar != null) {
                    zza += zzabw.zzb(6, zzkdVar);
                }
            }
        }
        return zza;
    }

    @Override // com.google.android.gms.internal.measurement.zzaby, com.google.android.gms.internal.measurement.zzace
    public final void zza(zzabw zzabwVar) throws IOException {
        if (this.zzasp != null) {
            zzabwVar.zzb(1, this.zzasp.longValue());
        }
        if (this.zzadm != null) {
            zzabwVar.zzb(2, this.zzadm);
        }
        if (this.zzasq != null) {
            zzabwVar.zze(3, this.zzasq.intValue());
        }
        if (this.zzasr != null && this.zzasr.length > 0) {
            for (int i = 0; i < this.zzasr.length; i++) {
                zzkl zzklVar = this.zzasr[i];
                if (zzklVar != null) {
                    zzabwVar.zza(4, zzklVar);
                }
            }
        }
        if (this.zzass != null && this.zzass.length > 0) {
            for (int i2 = 0; i2 < this.zzass.length; i2++) {
                zzkj zzkjVar = this.zzass[i2];
                if (zzkjVar != null) {
                    zzabwVar.zza(5, zzkjVar);
                }
            }
        }
        if (this.zzast != null && this.zzast.length > 0) {
            for (int i3 = 0; i3 < this.zzast.length; i3++) {
                zzkd zzkdVar = this.zzast[i3];
                if (zzkdVar != null) {
                    zzabwVar.zza(6, zzkdVar);
                }
            }
        }
        super.zza(zzabwVar);
    }

    @Override // com.google.android.gms.internal.measurement.zzace
    public final /* synthetic */ zzace zzb(zzabv zzabvVar) throws IOException {
        while (true) {
            int zzuw = zzabvVar.zzuw();
            if (zzuw == 0) {
                return this;
            }
            if (zzuw == 8) {
                this.zzasp = Long.valueOf(zzabvVar.zzuz());
            } else if (zzuw == 18) {
                this.zzadm = zzabvVar.readString();
            } else if (zzuw == 24) {
                this.zzasq = Integer.valueOf(zzabvVar.zzuy());
            } else if (zzuw == 34) {
                int zzb = zzach.zzb(zzabvVar, 34);
                int length = this.zzasr == null ? 0 : this.zzasr.length;
                zzkl[] zzklVarArr = new zzkl[zzb + length];
                if (length != 0) {
                    System.arraycopy(this.zzasr, 0, zzklVarArr, 0, length);
                }
                while (length < zzklVarArr.length - 1) {
                    zzklVarArr[length] = new zzkl();
                    zzabvVar.zza(zzklVarArr[length]);
                    zzabvVar.zzuw();
                    length++;
                }
                zzklVarArr[length] = new zzkl();
                zzabvVar.zza(zzklVarArr[length]);
                this.zzasr = zzklVarArr;
            } else if (zzuw == 42) {
                int zzb2 = zzach.zzb(zzabvVar, 42);
                int length2 = this.zzass == null ? 0 : this.zzass.length;
                zzkj[] zzkjVarArr = new zzkj[zzb2 + length2];
                if (length2 != 0) {
                    System.arraycopy(this.zzass, 0, zzkjVarArr, 0, length2);
                }
                while (length2 < zzkjVarArr.length - 1) {
                    zzkjVarArr[length2] = new zzkj();
                    zzabvVar.zza(zzkjVarArr[length2]);
                    zzabvVar.zzuw();
                    length2++;
                }
                zzkjVarArr[length2] = new zzkj();
                zzabvVar.zza(zzkjVarArr[length2]);
                this.zzass = zzkjVarArr;
            } else if (zzuw == 50) {
                int zzb3 = zzach.zzb(zzabvVar, 50);
                int length3 = this.zzast == null ? 0 : this.zzast.length;
                zzkd[] zzkdVarArr = new zzkd[zzb3 + length3];
                if (length3 != 0) {
                    System.arraycopy(this.zzast, 0, zzkdVarArr, 0, length3);
                }
                while (length3 < zzkdVarArr.length - 1) {
                    zzkdVarArr[length3] = new zzkd();
                    zzabvVar.zza(zzkdVarArr[length3]);
                    zzabvVar.zzuw();
                    length3++;
                }
                zzkdVarArr[length3] = new zzkd();
                zzabvVar.zza(zzkdVarArr[length3]);
                this.zzast = zzkdVarArr;
            } else if (!super.zza(zzabvVar, zzuw)) {
                return this;
            }
        }
    }
}
