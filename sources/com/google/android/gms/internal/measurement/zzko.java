package com.google.android.gms.internal.measurement;

import java.io.IOException;

/* loaded from: classes.dex */
public final class zzko extends zzaby<zzko> {
    private static volatile zzko[] zzatd;
    public String name = null;
    public String zzajf = null;
    public Long zzate = null;
    private Float zzarb = null;
    public Double zzarc = null;

    public zzko() {
        this.zzbww = null;
        this.zzbxh = -1;
    }

    public static zzko[] zzlm() {
        if (zzatd == null) {
            synchronized (zzacc.zzbxg) {
                if (zzatd == null) {
                    zzatd = new zzko[0];
                }
            }
        }
        return zzatd;
    }

    public final boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (obj instanceof zzko) {
            zzko zzkoVar = (zzko) obj;
            if (this.name == null) {
                if (zzkoVar.name != null) {
                    return false;
                }
            } else if (!this.name.equals(zzkoVar.name)) {
                return false;
            }
            if (this.zzajf == null) {
                if (zzkoVar.zzajf != null) {
                    return false;
                }
            } else if (!this.zzajf.equals(zzkoVar.zzajf)) {
                return false;
            }
            if (this.zzate == null) {
                if (zzkoVar.zzate != null) {
                    return false;
                }
            } else if (!this.zzate.equals(zzkoVar.zzate)) {
                return false;
            }
            if (this.zzarb == null) {
                if (zzkoVar.zzarb != null) {
                    return false;
                }
            } else if (!this.zzarb.equals(zzkoVar.zzarb)) {
                return false;
            }
            if (this.zzarc == null) {
                if (zzkoVar.zzarc != null) {
                    return false;
                }
            } else if (!this.zzarc.equals(zzkoVar.zzarc)) {
                return false;
            }
            return (this.zzbww == null || this.zzbww.isEmpty()) ? zzkoVar.zzbww == null || zzkoVar.zzbww.isEmpty() : this.zzbww.equals(zzkoVar.zzbww);
        }
        return false;
    }

    public final int hashCode() {
        int i = 0;
        int hashCode = (((((((((((getClass().getName().hashCode() + 527) * 31) + (this.name == null ? 0 : this.name.hashCode())) * 31) + (this.zzajf == null ? 0 : this.zzajf.hashCode())) * 31) + (this.zzate == null ? 0 : this.zzate.hashCode())) * 31) + (this.zzarb == null ? 0 : this.zzarb.hashCode())) * 31) + (this.zzarc == null ? 0 : this.zzarc.hashCode())) * 31;
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
        if (this.zzajf != null) {
            zza += zzabw.zzc(2, this.zzajf);
        }
        if (this.zzate != null) {
            zza += zzabw.zzc(3, this.zzate.longValue());
        }
        if (this.zzarb != null) {
            this.zzarb.floatValue();
            zza += zzabw.zzaq(4) + 4;
        }
        if (this.zzarc != null) {
            this.zzarc.doubleValue();
            return zza + zzabw.zzaq(5) + 8;
        }
        return zza;
    }

    @Override // com.google.android.gms.internal.measurement.zzaby, com.google.android.gms.internal.measurement.zzace
    public final void zza(zzabw zzabwVar) throws IOException {
        if (this.name != null) {
            zzabwVar.zzb(1, this.name);
        }
        if (this.zzajf != null) {
            zzabwVar.zzb(2, this.zzajf);
        }
        if (this.zzate != null) {
            zzabwVar.zzb(3, this.zzate.longValue());
        }
        if (this.zzarb != null) {
            zzabwVar.zza(4, this.zzarb.floatValue());
        }
        if (this.zzarc != null) {
            zzabwVar.zza(5, this.zzarc.doubleValue());
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
            } else if (zzuw == 18) {
                this.zzajf = zzabvVar.readString();
            } else if (zzuw == 24) {
                this.zzate = Long.valueOf(zzabvVar.zzuz());
            } else if (zzuw == 37) {
                this.zzarb = Float.valueOf(Float.intBitsToFloat(zzabvVar.zzva()));
            } else if (zzuw == 41) {
                this.zzarc = Double.valueOf(Double.longBitsToDouble(zzabvVar.zzvb()));
            } else if (!super.zza(zzabvVar, zzuw)) {
                return this;
            }
        }
    }
}
