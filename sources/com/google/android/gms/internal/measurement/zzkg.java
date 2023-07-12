package com.google.android.gms.internal.measurement;

import java.io.IOException;

/* loaded from: classes.dex */
public final class zzkg extends zzaby<zzkg> {
    public Integer zzarz = null;
    public Boolean zzasa = null;
    public String zzasb = null;
    public String zzasc = null;
    public String zzasd = null;

    public zzkg() {
        this.zzbww = null;
        this.zzbxh = -1;
    }

    /* JADX INFO: Access modifiers changed from: private */
    @Override // com.google.android.gms.internal.measurement.zzace
    /* renamed from: zzd */
    public final zzkg zzb(zzabv zzabvVar) throws IOException {
        while (true) {
            int zzuw = zzabvVar.zzuw();
            if (zzuw == 0) {
                return this;
            }
            if (zzuw == 8) {
                int position = zzabvVar.getPosition();
                try {
                    int zzuy = zzabvVar.zzuy();
                    if (zzuy < 0 || zzuy > 4) {
                        StringBuilder sb = new StringBuilder(46);
                        sb.append(zzuy);
                        sb.append(" is not a valid enum ComparisonType");
                        throw new IllegalArgumentException(sb.toString());
                        break;
                    }
                    this.zzarz = Integer.valueOf(zzuy);
                } catch (IllegalArgumentException unused) {
                    zzabvVar.zzam(position);
                    zza(zzabvVar, zzuw);
                }
            } else if (zzuw == 16) {
                this.zzasa = Boolean.valueOf(zzabvVar.zzux());
            } else if (zzuw == 26) {
                this.zzasb = zzabvVar.readString();
            } else if (zzuw == 34) {
                this.zzasc = zzabvVar.readString();
            } else if (zzuw == 42) {
                this.zzasd = zzabvVar.readString();
            } else if (!super.zza(zzabvVar, zzuw)) {
                return this;
            }
        }
    }

    public final boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (obj instanceof zzkg) {
            zzkg zzkgVar = (zzkg) obj;
            if (this.zzarz == null) {
                if (zzkgVar.zzarz != null) {
                    return false;
                }
            } else if (!this.zzarz.equals(zzkgVar.zzarz)) {
                return false;
            }
            if (this.zzasa == null) {
                if (zzkgVar.zzasa != null) {
                    return false;
                }
            } else if (!this.zzasa.equals(zzkgVar.zzasa)) {
                return false;
            }
            if (this.zzasb == null) {
                if (zzkgVar.zzasb != null) {
                    return false;
                }
            } else if (!this.zzasb.equals(zzkgVar.zzasb)) {
                return false;
            }
            if (this.zzasc == null) {
                if (zzkgVar.zzasc != null) {
                    return false;
                }
            } else if (!this.zzasc.equals(zzkgVar.zzasc)) {
                return false;
            }
            if (this.zzasd == null) {
                if (zzkgVar.zzasd != null) {
                    return false;
                }
            } else if (!this.zzasd.equals(zzkgVar.zzasd)) {
                return false;
            }
            return (this.zzbww == null || this.zzbww.isEmpty()) ? zzkgVar.zzbww == null || zzkgVar.zzbww.isEmpty() : this.zzbww.equals(zzkgVar.zzbww);
        }
        return false;
    }

    public final int hashCode() {
        int i = 0;
        int hashCode = (((((((((((getClass().getName().hashCode() + 527) * 31) + (this.zzarz == null ? 0 : this.zzarz.intValue())) * 31) + (this.zzasa == null ? 0 : this.zzasa.hashCode())) * 31) + (this.zzasb == null ? 0 : this.zzasb.hashCode())) * 31) + (this.zzasc == null ? 0 : this.zzasc.hashCode())) * 31) + (this.zzasd == null ? 0 : this.zzasd.hashCode())) * 31;
        if (this.zzbww != null && !this.zzbww.isEmpty()) {
            i = this.zzbww.hashCode();
        }
        return hashCode + i;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.google.android.gms.internal.measurement.zzaby, com.google.android.gms.internal.measurement.zzace
    public final int zza() {
        int zza = super.zza();
        if (this.zzarz != null) {
            zza += zzabw.zzf(1, this.zzarz.intValue());
        }
        if (this.zzasa != null) {
            this.zzasa.booleanValue();
            zza += zzabw.zzaq(2) + 1;
        }
        if (this.zzasb != null) {
            zza += zzabw.zzc(3, this.zzasb);
        }
        if (this.zzasc != null) {
            zza += zzabw.zzc(4, this.zzasc);
        }
        return this.zzasd != null ? zza + zzabw.zzc(5, this.zzasd) : zza;
    }

    @Override // com.google.android.gms.internal.measurement.zzaby, com.google.android.gms.internal.measurement.zzace
    public final void zza(zzabw zzabwVar) throws IOException {
        if (this.zzarz != null) {
            zzabwVar.zze(1, this.zzarz.intValue());
        }
        if (this.zzasa != null) {
            zzabwVar.zza(2, this.zzasa.booleanValue());
        }
        if (this.zzasb != null) {
            zzabwVar.zzb(3, this.zzasb);
        }
        if (this.zzasc != null) {
            zzabwVar.zzb(4, this.zzasc);
        }
        if (this.zzasd != null) {
            zzabwVar.zzb(5, this.zzasd);
        }
        super.zza(zzabwVar);
    }
}
