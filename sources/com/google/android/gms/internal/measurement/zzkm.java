package com.google.android.gms.internal.measurement;

import java.io.IOException;

/* loaded from: classes.dex */
public final class zzkm extends zzaby<zzkm> {
    private static volatile zzkm[] zzasv;
    public Integer zzarl = null;
    public zzkr zzasw = null;
    public zzkr zzasx = null;
    public Boolean zzasy = null;

    public zzkm() {
        this.zzbww = null;
        this.zzbxh = -1;
    }

    public static zzkm[] zzlk() {
        if (zzasv == null) {
            synchronized (zzacc.zzbxg) {
                if (zzasv == null) {
                    zzasv = new zzkm[0];
                }
            }
        }
        return zzasv;
    }

    public final boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (obj instanceof zzkm) {
            zzkm zzkmVar = (zzkm) obj;
            if (this.zzarl == null) {
                if (zzkmVar.zzarl != null) {
                    return false;
                }
            } else if (!this.zzarl.equals(zzkmVar.zzarl)) {
                return false;
            }
            if (this.zzasw == null) {
                if (zzkmVar.zzasw != null) {
                    return false;
                }
            } else if (!this.zzasw.equals(zzkmVar.zzasw)) {
                return false;
            }
            if (this.zzasx == null) {
                if (zzkmVar.zzasx != null) {
                    return false;
                }
            } else if (!this.zzasx.equals(zzkmVar.zzasx)) {
                return false;
            }
            if (this.zzasy == null) {
                if (zzkmVar.zzasy != null) {
                    return false;
                }
            } else if (!this.zzasy.equals(zzkmVar.zzasy)) {
                return false;
            }
            return (this.zzbww == null || this.zzbww.isEmpty()) ? zzkmVar.zzbww == null || zzkmVar.zzbww.isEmpty() : this.zzbww.equals(zzkmVar.zzbww);
        }
        return false;
    }

    public final int hashCode() {
        int i = 0;
        int hashCode = ((getClass().getName().hashCode() + 527) * 31) + (this.zzarl == null ? 0 : this.zzarl.hashCode());
        zzkr zzkrVar = this.zzasw;
        int hashCode2 = (hashCode * 31) + (zzkrVar == null ? 0 : zzkrVar.hashCode());
        zzkr zzkrVar2 = this.zzasx;
        int hashCode3 = ((((hashCode2 * 31) + (zzkrVar2 == null ? 0 : zzkrVar2.hashCode())) * 31) + (this.zzasy == null ? 0 : this.zzasy.hashCode())) * 31;
        if (this.zzbww != null && !this.zzbww.isEmpty()) {
            i = this.zzbww.hashCode();
        }
        return hashCode3 + i;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.google.android.gms.internal.measurement.zzaby, com.google.android.gms.internal.measurement.zzace
    public final int zza() {
        int zza = super.zza();
        if (this.zzarl != null) {
            zza += zzabw.zzf(1, this.zzarl.intValue());
        }
        if (this.zzasw != null) {
            zza += zzabw.zzb(2, this.zzasw);
        }
        if (this.zzasx != null) {
            zza += zzabw.zzb(3, this.zzasx);
        }
        if (this.zzasy != null) {
            this.zzasy.booleanValue();
            return zza + zzabw.zzaq(4) + 1;
        }
        return zza;
    }

    @Override // com.google.android.gms.internal.measurement.zzaby, com.google.android.gms.internal.measurement.zzace
    public final void zza(zzabw zzabwVar) throws IOException {
        if (this.zzarl != null) {
            zzabwVar.zze(1, this.zzarl.intValue());
        }
        if (this.zzasw != null) {
            zzabwVar.zza(2, this.zzasw);
        }
        if (this.zzasx != null) {
            zzabwVar.zza(3, this.zzasx);
        }
        if (this.zzasy != null) {
            zzabwVar.zza(4, this.zzasy.booleanValue());
        }
        super.zza(zzabwVar);
    }

    @Override // com.google.android.gms.internal.measurement.zzace
    public final /* synthetic */ zzace zzb(zzabv zzabvVar) throws IOException {
        zzkr zzkrVar;
        while (true) {
            int zzuw = zzabvVar.zzuw();
            if (zzuw == 0) {
                return this;
            }
            if (zzuw != 8) {
                if (zzuw == 18) {
                    if (this.zzasw == null) {
                        this.zzasw = new zzkr();
                    }
                    zzkrVar = this.zzasw;
                } else if (zzuw == 26) {
                    if (this.zzasx == null) {
                        this.zzasx = new zzkr();
                    }
                    zzkrVar = this.zzasx;
                } else if (zzuw == 32) {
                    this.zzasy = Boolean.valueOf(zzabvVar.zzux());
                } else if (!super.zza(zzabvVar, zzuw)) {
                    return this;
                }
                zzabvVar.zza(zzkrVar);
            } else {
                this.zzarl = Integer.valueOf(zzabvVar.zzuy());
            }
        }
    }
}
