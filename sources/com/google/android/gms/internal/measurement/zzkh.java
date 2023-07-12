package com.google.android.gms.internal.measurement;

import java.io.IOException;

/* loaded from: classes.dex */
public final class zzkh extends zzaby<zzkh> {
    private static volatile zzkh[] zzase;
    public Integer zzarp = null;
    public String zzasf = null;
    public zzkf zzasg = null;

    public zzkh() {
        this.zzbww = null;
        this.zzbxh = -1;
    }

    public static zzkh[] zzlh() {
        if (zzase == null) {
            synchronized (zzacc.zzbxg) {
                if (zzase == null) {
                    zzase = new zzkh[0];
                }
            }
        }
        return zzase;
    }

    public final boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (obj instanceof zzkh) {
            zzkh zzkhVar = (zzkh) obj;
            if (this.zzarp == null) {
                if (zzkhVar.zzarp != null) {
                    return false;
                }
            } else if (!this.zzarp.equals(zzkhVar.zzarp)) {
                return false;
            }
            if (this.zzasf == null) {
                if (zzkhVar.zzasf != null) {
                    return false;
                }
            } else if (!this.zzasf.equals(zzkhVar.zzasf)) {
                return false;
            }
            if (this.zzasg == null) {
                if (zzkhVar.zzasg != null) {
                    return false;
                }
            } else if (!this.zzasg.equals(zzkhVar.zzasg)) {
                return false;
            }
            return (this.zzbww == null || this.zzbww.isEmpty()) ? zzkhVar.zzbww == null || zzkhVar.zzbww.isEmpty() : this.zzbww.equals(zzkhVar.zzbww);
        }
        return false;
    }

    public final int hashCode() {
        int i = 0;
        int hashCode = ((((getClass().getName().hashCode() + 527) * 31) + (this.zzarp == null ? 0 : this.zzarp.hashCode())) * 31) + (this.zzasf == null ? 0 : this.zzasf.hashCode());
        zzkf zzkfVar = this.zzasg;
        int hashCode2 = ((hashCode * 31) + (zzkfVar == null ? 0 : zzkfVar.hashCode())) * 31;
        if (this.zzbww != null && !this.zzbww.isEmpty()) {
            i = this.zzbww.hashCode();
        }
        return hashCode2 + i;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.google.android.gms.internal.measurement.zzaby, com.google.android.gms.internal.measurement.zzace
    public final int zza() {
        int zza = super.zza();
        if (this.zzarp != null) {
            zza += zzabw.zzf(1, this.zzarp.intValue());
        }
        if (this.zzasf != null) {
            zza += zzabw.zzc(2, this.zzasf);
        }
        return this.zzasg != null ? zza + zzabw.zzb(3, this.zzasg) : zza;
    }

    @Override // com.google.android.gms.internal.measurement.zzaby, com.google.android.gms.internal.measurement.zzace
    public final void zza(zzabw zzabwVar) throws IOException {
        if (this.zzarp != null) {
            zzabwVar.zze(1, this.zzarp.intValue());
        }
        if (this.zzasf != null) {
            zzabwVar.zzb(2, this.zzasf);
        }
        if (this.zzasg != null) {
            zzabwVar.zza(3, this.zzasg);
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
                this.zzarp = Integer.valueOf(zzabvVar.zzuy());
            } else if (zzuw == 18) {
                this.zzasf = zzabvVar.readString();
            } else if (zzuw == 26) {
                if (this.zzasg == null) {
                    this.zzasg = new zzkf();
                }
                zzabvVar.zza(this.zzasg);
            } else if (!super.zza(zzabvVar, zzuw)) {
                return this;
            }
        }
    }
}
