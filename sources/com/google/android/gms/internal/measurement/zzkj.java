package com.google.android.gms.internal.measurement;

import java.io.IOException;

/* loaded from: classes.dex */
public final class zzkj extends zzaby<zzkj> {
    private static volatile zzkj[] zzasl;
    public String name = null;
    public Boolean zzasm = null;
    public Boolean zzasn = null;
    public Integer zzaso = null;

    public zzkj() {
        this.zzbww = null;
        this.zzbxh = -1;
    }

    public static zzkj[] zzli() {
        if (zzasl == null) {
            synchronized (zzacc.zzbxg) {
                if (zzasl == null) {
                    zzasl = new zzkj[0];
                }
            }
        }
        return zzasl;
    }

    public final boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (obj instanceof zzkj) {
            zzkj zzkjVar = (zzkj) obj;
            if (this.name == null) {
                if (zzkjVar.name != null) {
                    return false;
                }
            } else if (!this.name.equals(zzkjVar.name)) {
                return false;
            }
            if (this.zzasm == null) {
                if (zzkjVar.zzasm != null) {
                    return false;
                }
            } else if (!this.zzasm.equals(zzkjVar.zzasm)) {
                return false;
            }
            if (this.zzasn == null) {
                if (zzkjVar.zzasn != null) {
                    return false;
                }
            } else if (!this.zzasn.equals(zzkjVar.zzasn)) {
                return false;
            }
            if (this.zzaso == null) {
                if (zzkjVar.zzaso != null) {
                    return false;
                }
            } else if (!this.zzaso.equals(zzkjVar.zzaso)) {
                return false;
            }
            return (this.zzbww == null || this.zzbww.isEmpty()) ? zzkjVar.zzbww == null || zzkjVar.zzbww.isEmpty() : this.zzbww.equals(zzkjVar.zzbww);
        }
        return false;
    }

    public final int hashCode() {
        int i = 0;
        int hashCode = (((((((((getClass().getName().hashCode() + 527) * 31) + (this.name == null ? 0 : this.name.hashCode())) * 31) + (this.zzasm == null ? 0 : this.zzasm.hashCode())) * 31) + (this.zzasn == null ? 0 : this.zzasn.hashCode())) * 31) + (this.zzaso == null ? 0 : this.zzaso.hashCode())) * 31;
        if (this.zzbww != null && !this.zzbww.isEmpty()) {
            i = this.zzbww.hashCode();
        }
        return hashCode + i;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.google.android.gms.internal.measurement.zzaby, com.google.android.gms.internal.measurement.zzace
    public final int zza() {
        int zza = super.zza();
        if (this.name != null) {
            zza += zzabw.zzc(1, this.name);
        }
        if (this.zzasm != null) {
            this.zzasm.booleanValue();
            zza += zzabw.zzaq(2) + 1;
        }
        if (this.zzasn != null) {
            this.zzasn.booleanValue();
            zza += zzabw.zzaq(3) + 1;
        }
        return this.zzaso != null ? zza + zzabw.zzf(4, this.zzaso.intValue()) : zza;
    }

    @Override // com.google.android.gms.internal.measurement.zzaby, com.google.android.gms.internal.measurement.zzace
    public final void zza(zzabw zzabwVar) throws IOException {
        if (this.name != null) {
            zzabwVar.zzb(1, this.name);
        }
        if (this.zzasm != null) {
            zzabwVar.zza(2, this.zzasm.booleanValue());
        }
        if (this.zzasn != null) {
            zzabwVar.zza(3, this.zzasn.booleanValue());
        }
        if (this.zzaso != null) {
            zzabwVar.zze(4, this.zzaso.intValue());
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
            if (zzuw == 10) {
                this.name = zzabvVar.readString();
            } else if (zzuw == 16) {
                this.zzasm = Boolean.valueOf(zzabvVar.zzux());
            } else if (zzuw == 24) {
                this.zzasn = Boolean.valueOf(zzabvVar.zzux());
            } else if (zzuw == 32) {
                this.zzaso = Integer.valueOf(zzabvVar.zzuy());
            } else if (!super.zza(zzabvVar, zzuw)) {
                return this;
            }
        }
    }
}
