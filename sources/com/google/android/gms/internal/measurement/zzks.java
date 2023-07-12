package com.google.android.gms.internal.measurement;

import java.io.IOException;

/* loaded from: classes.dex */
public final class zzks extends zzaby<zzks> {
    private static volatile zzks[] zzaum;
    public Long zzaun = null;
    public String name = null;
    public String zzajf = null;
    public Long zzate = null;
    private Float zzarb = null;
    public Double zzarc = null;

    public zzks() {
        this.zzbww = null;
        this.zzbxh = -1;
    }

    public static zzks[] zzlo() {
        if (zzaum == null) {
            synchronized (zzacc.zzbxg) {
                if (zzaum == null) {
                    zzaum = new zzks[0];
                }
            }
        }
        return zzaum;
    }

    public final boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (obj instanceof zzks) {
            zzks zzksVar = (zzks) obj;
            if (this.zzaun == null) {
                if (zzksVar.zzaun != null) {
                    return false;
                }
            } else if (!this.zzaun.equals(zzksVar.zzaun)) {
                return false;
            }
            if (this.name == null) {
                if (zzksVar.name != null) {
                    return false;
                }
            } else if (!this.name.equals(zzksVar.name)) {
                return false;
            }
            if (this.zzajf == null) {
                if (zzksVar.zzajf != null) {
                    return false;
                }
            } else if (!this.zzajf.equals(zzksVar.zzajf)) {
                return false;
            }
            if (this.zzate == null) {
                if (zzksVar.zzate != null) {
                    return false;
                }
            } else if (!this.zzate.equals(zzksVar.zzate)) {
                return false;
            }
            if (this.zzarb == null) {
                if (zzksVar.zzarb != null) {
                    return false;
                }
            } else if (!this.zzarb.equals(zzksVar.zzarb)) {
                return false;
            }
            if (this.zzarc == null) {
                if (zzksVar.zzarc != null) {
                    return false;
                }
            } else if (!this.zzarc.equals(zzksVar.zzarc)) {
                return false;
            }
            return (this.zzbww == null || this.zzbww.isEmpty()) ? zzksVar.zzbww == null || zzksVar.zzbww.isEmpty() : this.zzbww.equals(zzksVar.zzbww);
        }
        return false;
    }

    public final int hashCode() {
        int i = 0;
        int hashCode = (((((((((((((getClass().getName().hashCode() + 527) * 31) + (this.zzaun == null ? 0 : this.zzaun.hashCode())) * 31) + (this.name == null ? 0 : this.name.hashCode())) * 31) + (this.zzajf == null ? 0 : this.zzajf.hashCode())) * 31) + (this.zzate == null ? 0 : this.zzate.hashCode())) * 31) + (this.zzarb == null ? 0 : this.zzarb.hashCode())) * 31) + (this.zzarc == null ? 0 : this.zzarc.hashCode())) * 31;
        if (this.zzbww != null && !this.zzbww.isEmpty()) {
            i = this.zzbww.hashCode();
        }
        return hashCode + i;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.google.android.gms.internal.measurement.zzaby, com.google.android.gms.internal.measurement.zzace
    public final int zza() {
        int zza = super.zza();
        if (this.zzaun != null) {
            zza += zzabw.zzc(1, this.zzaun.longValue());
        }
        if (this.name != null) {
            zza += zzabw.zzc(2, this.name);
        }
        if (this.zzajf != null) {
            zza += zzabw.zzc(3, this.zzajf);
        }
        if (this.zzate != null) {
            zza += zzabw.zzc(4, this.zzate.longValue());
        }
        if (this.zzarb != null) {
            this.zzarb.floatValue();
            zza += zzabw.zzaq(5) + 4;
        }
        if (this.zzarc != null) {
            this.zzarc.doubleValue();
            return zza + zzabw.zzaq(6) + 8;
        }
        return zza;
    }

    @Override // com.google.android.gms.internal.measurement.zzaby, com.google.android.gms.internal.measurement.zzace
    public final void zza(zzabw zzabwVar) throws IOException {
        if (this.zzaun != null) {
            zzabwVar.zzb(1, this.zzaun.longValue());
        }
        if (this.name != null) {
            zzabwVar.zzb(2, this.name);
        }
        if (this.zzajf != null) {
            zzabwVar.zzb(3, this.zzajf);
        }
        if (this.zzate != null) {
            zzabwVar.zzb(4, this.zzate.longValue());
        }
        if (this.zzarb != null) {
            zzabwVar.zza(5, this.zzarb.floatValue());
        }
        if (this.zzarc != null) {
            zzabwVar.zza(6, this.zzarc.doubleValue());
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
                this.zzaun = Long.valueOf(zzabvVar.zzuz());
            } else if (zzuw == 18) {
                this.name = zzabvVar.readString();
            } else if (zzuw == 26) {
                this.zzajf = zzabvVar.readString();
            } else if (zzuw == 32) {
                this.zzate = Long.valueOf(zzabvVar.zzuz());
            } else if (zzuw == 45) {
                this.zzarb = Float.valueOf(Float.intBitsToFloat(zzabvVar.zzva()));
            } else if (zzuw == 49) {
                this.zzarc = Double.valueOf(Double.longBitsToDouble(zzabvVar.zzvb()));
            } else if (!super.zza(zzabvVar, zzuw)) {
                return this;
            }
        }
    }
}
