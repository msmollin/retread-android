package com.google.android.gms.internal.measurement;

import java.io.IOException;

/* loaded from: classes.dex */
public final class zzkn extends zzaby<zzkn> {
    private static volatile zzkn[] zzasz;
    public zzko[] zzata = zzko.zzlm();
    public String name = null;
    public Long zzatb = null;
    public Long zzatc = null;
    public Integer count = null;

    public zzkn() {
        this.zzbww = null;
        this.zzbxh = -1;
    }

    public static zzkn[] zzll() {
        if (zzasz == null) {
            synchronized (zzacc.zzbxg) {
                if (zzasz == null) {
                    zzasz = new zzkn[0];
                }
            }
        }
        return zzasz;
    }

    public final boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (obj instanceof zzkn) {
            zzkn zzknVar = (zzkn) obj;
            if (zzacc.equals(this.zzata, zzknVar.zzata)) {
                if (this.name == null) {
                    if (zzknVar.name != null) {
                        return false;
                    }
                } else if (!this.name.equals(zzknVar.name)) {
                    return false;
                }
                if (this.zzatb == null) {
                    if (zzknVar.zzatb != null) {
                        return false;
                    }
                } else if (!this.zzatb.equals(zzknVar.zzatb)) {
                    return false;
                }
                if (this.zzatc == null) {
                    if (zzknVar.zzatc != null) {
                        return false;
                    }
                } else if (!this.zzatc.equals(zzknVar.zzatc)) {
                    return false;
                }
                if (this.count == null) {
                    if (zzknVar.count != null) {
                        return false;
                    }
                } else if (!this.count.equals(zzknVar.count)) {
                    return false;
                }
                return (this.zzbww == null || this.zzbww.isEmpty()) ? zzknVar.zzbww == null || zzknVar.zzbww.isEmpty() : this.zzbww.equals(zzknVar.zzbww);
            }
            return false;
        }
        return false;
    }

    public final int hashCode() {
        int i = 0;
        int hashCode = (((((((((((getClass().getName().hashCode() + 527) * 31) + zzacc.hashCode(this.zzata)) * 31) + (this.name == null ? 0 : this.name.hashCode())) * 31) + (this.zzatb == null ? 0 : this.zzatb.hashCode())) * 31) + (this.zzatc == null ? 0 : this.zzatc.hashCode())) * 31) + (this.count == null ? 0 : this.count.hashCode())) * 31;
        if (this.zzbww != null && !this.zzbww.isEmpty()) {
            i = this.zzbww.hashCode();
        }
        return hashCode + i;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.google.android.gms.internal.measurement.zzaby, com.google.android.gms.internal.measurement.zzace
    public final int zza() {
        int zza = super.zza();
        if (this.zzata != null && this.zzata.length > 0) {
            for (int i = 0; i < this.zzata.length; i++) {
                zzko zzkoVar = this.zzata[i];
                if (zzkoVar != null) {
                    zza += zzabw.zzb(1, zzkoVar);
                }
            }
        }
        if (this.name != null) {
            zza += zzabw.zzc(2, this.name);
        }
        if (this.zzatb != null) {
            zza += zzabw.zzc(3, this.zzatb.longValue());
        }
        if (this.zzatc != null) {
            zza += zzabw.zzc(4, this.zzatc.longValue());
        }
        return this.count != null ? zza + zzabw.zzf(5, this.count.intValue()) : zza;
    }

    @Override // com.google.android.gms.internal.measurement.zzaby, com.google.android.gms.internal.measurement.zzace
    public final void zza(zzabw zzabwVar) throws IOException {
        if (this.zzata != null && this.zzata.length > 0) {
            for (int i = 0; i < this.zzata.length; i++) {
                zzko zzkoVar = this.zzata[i];
                if (zzkoVar != null) {
                    zzabwVar.zza(1, zzkoVar);
                }
            }
        }
        if (this.name != null) {
            zzabwVar.zzb(2, this.name);
        }
        if (this.zzatb != null) {
            zzabwVar.zzb(3, this.zzatb.longValue());
        }
        if (this.zzatc != null) {
            zzabwVar.zzb(4, this.zzatc.longValue());
        }
        if (this.count != null) {
            zzabwVar.zze(5, this.count.intValue());
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
                int zzb = zzach.zzb(zzabvVar, 10);
                int length = this.zzata == null ? 0 : this.zzata.length;
                zzko[] zzkoVarArr = new zzko[zzb + length];
                if (length != 0) {
                    System.arraycopy(this.zzata, 0, zzkoVarArr, 0, length);
                }
                while (length < zzkoVarArr.length - 1) {
                    zzkoVarArr[length] = new zzko();
                    zzabvVar.zza(zzkoVarArr[length]);
                    zzabvVar.zzuw();
                    length++;
                }
                zzkoVarArr[length] = new zzko();
                zzabvVar.zza(zzkoVarArr[length]);
                this.zzata = zzkoVarArr;
            } else if (zzuw == 18) {
                this.name = zzabvVar.readString();
            } else if (zzuw == 24) {
                this.zzatb = Long.valueOf(zzabvVar.zzuz());
            } else if (zzuw == 32) {
                this.zzatc = Long.valueOf(zzabvVar.zzuz());
            } else if (zzuw == 40) {
                this.count = Integer.valueOf(zzabvVar.zzuy());
            } else if (!super.zza(zzabvVar, zzuw)) {
                return this;
            }
        }
    }
}
