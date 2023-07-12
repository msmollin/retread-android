package com.google.android.gms.internal.measurement;

import java.io.IOException;

/* loaded from: classes.dex */
public final class zzki extends zzaby<zzki> {
    public Integer zzash = null;
    public String zzasi = null;
    public Boolean zzasj = null;
    public String[] zzask = zzach.zzbxq;

    public zzki() {
        this.zzbww = null;
        this.zzbxh = -1;
    }

    /* JADX INFO: Access modifiers changed from: private */
    @Override // com.google.android.gms.internal.measurement.zzace
    /* renamed from: zze */
    public final zzki zzb(zzabv zzabvVar) throws IOException {
        while (true) {
            int zzuw = zzabvVar.zzuw();
            if (zzuw == 0) {
                return this;
            }
            if (zzuw == 8) {
                int position = zzabvVar.getPosition();
                try {
                    int zzuy = zzabvVar.zzuy();
                    if (zzuy < 0 || zzuy > 6) {
                        StringBuilder sb = new StringBuilder(41);
                        sb.append(zzuy);
                        sb.append(" is not a valid enum MatchType");
                        throw new IllegalArgumentException(sb.toString());
                        break;
                    }
                    this.zzash = Integer.valueOf(zzuy);
                } catch (IllegalArgumentException unused) {
                    zzabvVar.zzam(position);
                    zza(zzabvVar, zzuw);
                }
            } else if (zzuw == 18) {
                this.zzasi = zzabvVar.readString();
            } else if (zzuw == 24) {
                this.zzasj = Boolean.valueOf(zzabvVar.zzux());
            } else if (zzuw == 34) {
                int zzb = zzach.zzb(zzabvVar, 34);
                int length = this.zzask == null ? 0 : this.zzask.length;
                String[] strArr = new String[zzb + length];
                if (length != 0) {
                    System.arraycopy(this.zzask, 0, strArr, 0, length);
                }
                while (length < strArr.length - 1) {
                    strArr[length] = zzabvVar.readString();
                    zzabvVar.zzuw();
                    length++;
                }
                strArr[length] = zzabvVar.readString();
                this.zzask = strArr;
            } else if (!super.zza(zzabvVar, zzuw)) {
                return this;
            }
        }
    }

    public final boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (obj instanceof zzki) {
            zzki zzkiVar = (zzki) obj;
            if (this.zzash == null) {
                if (zzkiVar.zzash != null) {
                    return false;
                }
            } else if (!this.zzash.equals(zzkiVar.zzash)) {
                return false;
            }
            if (this.zzasi == null) {
                if (zzkiVar.zzasi != null) {
                    return false;
                }
            } else if (!this.zzasi.equals(zzkiVar.zzasi)) {
                return false;
            }
            if (this.zzasj == null) {
                if (zzkiVar.zzasj != null) {
                    return false;
                }
            } else if (!this.zzasj.equals(zzkiVar.zzasj)) {
                return false;
            }
            if (zzacc.equals(this.zzask, zzkiVar.zzask)) {
                return (this.zzbww == null || this.zzbww.isEmpty()) ? zzkiVar.zzbww == null || zzkiVar.zzbww.isEmpty() : this.zzbww.equals(zzkiVar.zzbww);
            }
            return false;
        }
        return false;
    }

    public final int hashCode() {
        int i = 0;
        int hashCode = (((((((((getClass().getName().hashCode() + 527) * 31) + (this.zzash == null ? 0 : this.zzash.intValue())) * 31) + (this.zzasi == null ? 0 : this.zzasi.hashCode())) * 31) + (this.zzasj == null ? 0 : this.zzasj.hashCode())) * 31) + zzacc.hashCode(this.zzask)) * 31;
        if (this.zzbww != null && !this.zzbww.isEmpty()) {
            i = this.zzbww.hashCode();
        }
        return hashCode + i;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.google.android.gms.internal.measurement.zzaby, com.google.android.gms.internal.measurement.zzace
    public final int zza() {
        int zza = super.zza();
        if (this.zzash != null) {
            zza += zzabw.zzf(1, this.zzash.intValue());
        }
        if (this.zzasi != null) {
            zza += zzabw.zzc(2, this.zzasi);
        }
        if (this.zzasj != null) {
            this.zzasj.booleanValue();
            zza += zzabw.zzaq(3) + 1;
        }
        if (this.zzask == null || this.zzask.length <= 0) {
            return zza;
        }
        int i = 0;
        int i2 = 0;
        for (int i3 = 0; i3 < this.zzask.length; i3++) {
            String str = this.zzask[i3];
            if (str != null) {
                i2++;
                i += zzabw.zzfm(str);
            }
        }
        return zza + i + (i2 * 1);
    }

    @Override // com.google.android.gms.internal.measurement.zzaby, com.google.android.gms.internal.measurement.zzace
    public final void zza(zzabw zzabwVar) throws IOException {
        if (this.zzash != null) {
            zzabwVar.zze(1, this.zzash.intValue());
        }
        if (this.zzasi != null) {
            zzabwVar.zzb(2, this.zzasi);
        }
        if (this.zzasj != null) {
            zzabwVar.zza(3, this.zzasj.booleanValue());
        }
        if (this.zzask != null && this.zzask.length > 0) {
            for (int i = 0; i < this.zzask.length; i++) {
                String str = this.zzask[i];
                if (str != null) {
                    zzabwVar.zzb(4, str);
                }
            }
        }
        super.zza(zzabwVar);
    }
}
