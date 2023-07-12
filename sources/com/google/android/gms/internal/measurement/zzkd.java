package com.google.android.gms.internal.measurement;

import java.io.IOException;

/* loaded from: classes.dex */
public final class zzkd extends zzaby<zzkd> {
    private static volatile zzkd[] zzark;
    public Integer zzarl = null;
    public zzkh[] zzarm = zzkh.zzlh();
    public zzke[] zzarn = zzke.zzlf();

    public zzkd() {
        this.zzbww = null;
        this.zzbxh = -1;
    }

    public static zzkd[] zzle() {
        if (zzark == null) {
            synchronized (zzacc.zzbxg) {
                if (zzark == null) {
                    zzark = new zzkd[0];
                }
            }
        }
        return zzark;
    }

    public final boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (obj instanceof zzkd) {
            zzkd zzkdVar = (zzkd) obj;
            if (this.zzarl == null) {
                if (zzkdVar.zzarl != null) {
                    return false;
                }
            } else if (!this.zzarl.equals(zzkdVar.zzarl)) {
                return false;
            }
            if (zzacc.equals(this.zzarm, zzkdVar.zzarm) && zzacc.equals(this.zzarn, zzkdVar.zzarn)) {
                return (this.zzbww == null || this.zzbww.isEmpty()) ? zzkdVar.zzbww == null || zzkdVar.zzbww.isEmpty() : this.zzbww.equals(zzkdVar.zzbww);
            }
            return false;
        }
        return false;
    }

    public final int hashCode() {
        int i = 0;
        int hashCode = (((((((getClass().getName().hashCode() + 527) * 31) + (this.zzarl == null ? 0 : this.zzarl.hashCode())) * 31) + zzacc.hashCode(this.zzarm)) * 31) + zzacc.hashCode(this.zzarn)) * 31;
        if (this.zzbww != null && !this.zzbww.isEmpty()) {
            i = this.zzbww.hashCode();
        }
        return hashCode + i;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.google.android.gms.internal.measurement.zzaby, com.google.android.gms.internal.measurement.zzace
    public final int zza() {
        int zza = super.zza();
        if (this.zzarl != null) {
            zza += zzabw.zzf(1, this.zzarl.intValue());
        }
        if (this.zzarm != null && this.zzarm.length > 0) {
            int i = zza;
            for (int i2 = 0; i2 < this.zzarm.length; i2++) {
                zzkh zzkhVar = this.zzarm[i2];
                if (zzkhVar != null) {
                    i += zzabw.zzb(2, zzkhVar);
                }
            }
            zza = i;
        }
        if (this.zzarn != null && this.zzarn.length > 0) {
            for (int i3 = 0; i3 < this.zzarn.length; i3++) {
                zzke zzkeVar = this.zzarn[i3];
                if (zzkeVar != null) {
                    zza += zzabw.zzb(3, zzkeVar);
                }
            }
        }
        return zza;
    }

    @Override // com.google.android.gms.internal.measurement.zzaby, com.google.android.gms.internal.measurement.zzace
    public final void zza(zzabw zzabwVar) throws IOException {
        if (this.zzarl != null) {
            zzabwVar.zze(1, this.zzarl.intValue());
        }
        if (this.zzarm != null && this.zzarm.length > 0) {
            for (int i = 0; i < this.zzarm.length; i++) {
                zzkh zzkhVar = this.zzarm[i];
                if (zzkhVar != null) {
                    zzabwVar.zza(2, zzkhVar);
                }
            }
        }
        if (this.zzarn != null && this.zzarn.length > 0) {
            for (int i2 = 0; i2 < this.zzarn.length; i2++) {
                zzke zzkeVar = this.zzarn[i2];
                if (zzkeVar != null) {
                    zzabwVar.zza(3, zzkeVar);
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
                this.zzarl = Integer.valueOf(zzabvVar.zzuy());
            } else if (zzuw == 18) {
                int zzb = zzach.zzb(zzabvVar, 18);
                int length = this.zzarm == null ? 0 : this.zzarm.length;
                zzkh[] zzkhVarArr = new zzkh[zzb + length];
                if (length != 0) {
                    System.arraycopy(this.zzarm, 0, zzkhVarArr, 0, length);
                }
                while (length < zzkhVarArr.length - 1) {
                    zzkhVarArr[length] = new zzkh();
                    zzabvVar.zza(zzkhVarArr[length]);
                    zzabvVar.zzuw();
                    length++;
                }
                zzkhVarArr[length] = new zzkh();
                zzabvVar.zza(zzkhVarArr[length]);
                this.zzarm = zzkhVarArr;
            } else if (zzuw == 26) {
                int zzb2 = zzach.zzb(zzabvVar, 26);
                int length2 = this.zzarn == null ? 0 : this.zzarn.length;
                zzke[] zzkeVarArr = new zzke[zzb2 + length2];
                if (length2 != 0) {
                    System.arraycopy(this.zzarn, 0, zzkeVarArr, 0, length2);
                }
                while (length2 < zzkeVarArr.length - 1) {
                    zzkeVarArr[length2] = new zzke();
                    zzabvVar.zza(zzkeVarArr[length2]);
                    zzabvVar.zzuw();
                    length2++;
                }
                zzkeVarArr[length2] = new zzke();
                zzabvVar.zza(zzkeVarArr[length2]);
                this.zzarn = zzkeVarArr;
            } else if (!super.zza(zzabvVar, zzuw)) {
                return this;
            }
        }
    }
}
