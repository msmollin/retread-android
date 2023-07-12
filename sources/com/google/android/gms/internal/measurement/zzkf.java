package com.google.android.gms.internal.measurement;

import java.io.IOException;

/* loaded from: classes.dex */
public final class zzkf extends zzaby<zzkf> {
    private static volatile zzkf[] zzaru;
    public zzki zzarv = null;
    public zzkg zzarw = null;
    public Boolean zzarx = null;
    public String zzary = null;

    public zzkf() {
        this.zzbww = null;
        this.zzbxh = -1;
    }

    public static zzkf[] zzlg() {
        if (zzaru == null) {
            synchronized (zzacc.zzbxg) {
                if (zzaru == null) {
                    zzaru = new zzkf[0];
                }
            }
        }
        return zzaru;
    }

    public final boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (obj instanceof zzkf) {
            zzkf zzkfVar = (zzkf) obj;
            if (this.zzarv == null) {
                if (zzkfVar.zzarv != null) {
                    return false;
                }
            } else if (!this.zzarv.equals(zzkfVar.zzarv)) {
                return false;
            }
            if (this.zzarw == null) {
                if (zzkfVar.zzarw != null) {
                    return false;
                }
            } else if (!this.zzarw.equals(zzkfVar.zzarw)) {
                return false;
            }
            if (this.zzarx == null) {
                if (zzkfVar.zzarx != null) {
                    return false;
                }
            } else if (!this.zzarx.equals(zzkfVar.zzarx)) {
                return false;
            }
            if (this.zzary == null) {
                if (zzkfVar.zzary != null) {
                    return false;
                }
            } else if (!this.zzary.equals(zzkfVar.zzary)) {
                return false;
            }
            return (this.zzbww == null || this.zzbww.isEmpty()) ? zzkfVar.zzbww == null || zzkfVar.zzbww.isEmpty() : this.zzbww.equals(zzkfVar.zzbww);
        }
        return false;
    }

    public final int hashCode() {
        zzki zzkiVar = this.zzarv;
        int i = 0;
        int hashCode = ((getClass().getName().hashCode() + 527) * 31) + (zzkiVar == null ? 0 : zzkiVar.hashCode());
        zzkg zzkgVar = this.zzarw;
        int hashCode2 = ((((((hashCode * 31) + (zzkgVar == null ? 0 : zzkgVar.hashCode())) * 31) + (this.zzarx == null ? 0 : this.zzarx.hashCode())) * 31) + (this.zzary == null ? 0 : this.zzary.hashCode())) * 31;
        if (this.zzbww != null && !this.zzbww.isEmpty()) {
            i = this.zzbww.hashCode();
        }
        return hashCode2 + i;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.google.android.gms.internal.measurement.zzaby, com.google.android.gms.internal.measurement.zzace
    public final int zza() {
        int zza = super.zza();
        if (this.zzarv != null) {
            zza += zzabw.zzb(1, this.zzarv);
        }
        if (this.zzarw != null) {
            zza += zzabw.zzb(2, this.zzarw);
        }
        if (this.zzarx != null) {
            this.zzarx.booleanValue();
            zza += zzabw.zzaq(3) + 1;
        }
        return this.zzary != null ? zza + zzabw.zzc(4, this.zzary) : zza;
    }

    @Override // com.google.android.gms.internal.measurement.zzaby, com.google.android.gms.internal.measurement.zzace
    public final void zza(zzabw zzabwVar) throws IOException {
        if (this.zzarv != null) {
            zzabwVar.zza(1, this.zzarv);
        }
        if (this.zzarw != null) {
            zzabwVar.zza(2, this.zzarw);
        }
        if (this.zzarx != null) {
            zzabwVar.zza(3, this.zzarx.booleanValue());
        }
        if (this.zzary != null) {
            zzabwVar.zzb(4, this.zzary);
        }
        super.zza(zzabwVar);
    }

    @Override // com.google.android.gms.internal.measurement.zzace
    public final /* synthetic */ zzace zzb(zzabv zzabvVar) throws IOException {
        zzace zzaceVar;
        while (true) {
            int zzuw = zzabvVar.zzuw();
            if (zzuw == 0) {
                return this;
            }
            if (zzuw == 10) {
                if (this.zzarv == null) {
                    this.zzarv = new zzki();
                }
                zzaceVar = this.zzarv;
            } else if (zzuw == 18) {
                if (this.zzarw == null) {
                    this.zzarw = new zzkg();
                }
                zzaceVar = this.zzarw;
            } else if (zzuw == 24) {
                this.zzarx = Boolean.valueOf(zzabvVar.zzux());
            } else if (zzuw == 34) {
                this.zzary = zzabvVar.readString();
            } else if (!super.zza(zzabvVar, zzuw)) {
                return this;
            }
            zzabvVar.zza(zzaceVar);
        }
    }
}
