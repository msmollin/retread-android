package com.google.android.gms.internal.measurement;

import java.io.IOException;

/* loaded from: classes.dex */
public final class zzke extends zzaby<zzke> {
    private static volatile zzke[] zzaro;
    public Integer zzarp = null;
    public String zzarq = null;
    public zzkf[] zzarr = zzkf.zzlg();
    private Boolean zzars = null;
    public zzkg zzart = null;

    public zzke() {
        this.zzbww = null;
        this.zzbxh = -1;
    }

    public static zzke[] zzlf() {
        if (zzaro == null) {
            synchronized (zzacc.zzbxg) {
                if (zzaro == null) {
                    zzaro = new zzke[0];
                }
            }
        }
        return zzaro;
    }

    public final boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (obj instanceof zzke) {
            zzke zzkeVar = (zzke) obj;
            if (this.zzarp == null) {
                if (zzkeVar.zzarp != null) {
                    return false;
                }
            } else if (!this.zzarp.equals(zzkeVar.zzarp)) {
                return false;
            }
            if (this.zzarq == null) {
                if (zzkeVar.zzarq != null) {
                    return false;
                }
            } else if (!this.zzarq.equals(zzkeVar.zzarq)) {
                return false;
            }
            if (zzacc.equals(this.zzarr, zzkeVar.zzarr)) {
                if (this.zzars == null) {
                    if (zzkeVar.zzars != null) {
                        return false;
                    }
                } else if (!this.zzars.equals(zzkeVar.zzars)) {
                    return false;
                }
                if (this.zzart == null) {
                    if (zzkeVar.zzart != null) {
                        return false;
                    }
                } else if (!this.zzart.equals(zzkeVar.zzart)) {
                    return false;
                }
                return (this.zzbww == null || this.zzbww.isEmpty()) ? zzkeVar.zzbww == null || zzkeVar.zzbww.isEmpty() : this.zzbww.equals(zzkeVar.zzbww);
            }
            return false;
        }
        return false;
    }

    public final int hashCode() {
        int i = 0;
        int hashCode = ((((((((getClass().getName().hashCode() + 527) * 31) + (this.zzarp == null ? 0 : this.zzarp.hashCode())) * 31) + (this.zzarq == null ? 0 : this.zzarq.hashCode())) * 31) + zzacc.hashCode(this.zzarr)) * 31) + (this.zzars == null ? 0 : this.zzars.hashCode());
        zzkg zzkgVar = this.zzart;
        int hashCode2 = ((hashCode * 31) + (zzkgVar == null ? 0 : zzkgVar.hashCode())) * 31;
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
        if (this.zzarq != null) {
            zza += zzabw.zzc(2, this.zzarq);
        }
        if (this.zzarr != null && this.zzarr.length > 0) {
            for (int i = 0; i < this.zzarr.length; i++) {
                zzkf zzkfVar = this.zzarr[i];
                if (zzkfVar != null) {
                    zza += zzabw.zzb(3, zzkfVar);
                }
            }
        }
        if (this.zzars != null) {
            this.zzars.booleanValue();
            zza += zzabw.zzaq(4) + 1;
        }
        return this.zzart != null ? zza + zzabw.zzb(5, this.zzart) : zza;
    }

    @Override // com.google.android.gms.internal.measurement.zzaby, com.google.android.gms.internal.measurement.zzace
    public final void zza(zzabw zzabwVar) throws IOException {
        if (this.zzarp != null) {
            zzabwVar.zze(1, this.zzarp.intValue());
        }
        if (this.zzarq != null) {
            zzabwVar.zzb(2, this.zzarq);
        }
        if (this.zzarr != null && this.zzarr.length > 0) {
            for (int i = 0; i < this.zzarr.length; i++) {
                zzkf zzkfVar = this.zzarr[i];
                if (zzkfVar != null) {
                    zzabwVar.zza(3, zzkfVar);
                }
            }
        }
        if (this.zzars != null) {
            zzabwVar.zza(4, this.zzars.booleanValue());
        }
        if (this.zzart != null) {
            zzabwVar.zza(5, this.zzart);
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
                this.zzarq = zzabvVar.readString();
            } else if (zzuw == 26) {
                int zzb = zzach.zzb(zzabvVar, 26);
                int length = this.zzarr == null ? 0 : this.zzarr.length;
                zzkf[] zzkfVarArr = new zzkf[zzb + length];
                if (length != 0) {
                    System.arraycopy(this.zzarr, 0, zzkfVarArr, 0, length);
                }
                while (length < zzkfVarArr.length - 1) {
                    zzkfVarArr[length] = new zzkf();
                    zzabvVar.zza(zzkfVarArr[length]);
                    zzabvVar.zzuw();
                    length++;
                }
                zzkfVarArr[length] = new zzkf();
                zzabvVar.zza(zzkfVarArr[length]);
                this.zzarr = zzkfVarArr;
            } else if (zzuw == 32) {
                this.zzars = Boolean.valueOf(zzabvVar.zzux());
            } else if (zzuw == 42) {
                if (this.zzart == null) {
                    this.zzart = new zzkg();
                }
                zzabvVar.zza(this.zzart);
            } else if (!super.zza(zzabvVar, zzuw)) {
                return this;
            }
        }
    }
}
