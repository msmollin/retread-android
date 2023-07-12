package com.google.android.gms.internal.measurement;

import java.io.IOException;

/* loaded from: classes.dex */
public final class zzkl extends zzaby<zzkl> {
    private static volatile zzkl[] zzasu;
    public String zzny = null;
    public String value = null;

    public zzkl() {
        this.zzbww = null;
        this.zzbxh = -1;
    }

    public static zzkl[] zzlj() {
        if (zzasu == null) {
            synchronized (zzacc.zzbxg) {
                if (zzasu == null) {
                    zzasu = new zzkl[0];
                }
            }
        }
        return zzasu;
    }

    public final boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (obj instanceof zzkl) {
            zzkl zzklVar = (zzkl) obj;
            if (this.zzny == null) {
                if (zzklVar.zzny != null) {
                    return false;
                }
            } else if (!this.zzny.equals(zzklVar.zzny)) {
                return false;
            }
            if (this.value == null) {
                if (zzklVar.value != null) {
                    return false;
                }
            } else if (!this.value.equals(zzklVar.value)) {
                return false;
            }
            return (this.zzbww == null || this.zzbww.isEmpty()) ? zzklVar.zzbww == null || zzklVar.zzbww.isEmpty() : this.zzbww.equals(zzklVar.zzbww);
        }
        return false;
    }

    public final int hashCode() {
        int i = 0;
        int hashCode = (((((getClass().getName().hashCode() + 527) * 31) + (this.zzny == null ? 0 : this.zzny.hashCode())) * 31) + (this.value == null ? 0 : this.value.hashCode())) * 31;
        if (this.zzbww != null && !this.zzbww.isEmpty()) {
            i = this.zzbww.hashCode();
        }
        return hashCode + i;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.google.android.gms.internal.measurement.zzaby, com.google.android.gms.internal.measurement.zzace
    public final int zza() {
        int zza = super.zza();
        if (this.zzny != null) {
            zza += zzabw.zzc(1, this.zzny);
        }
        return this.value != null ? zza + zzabw.zzc(2, this.value) : zza;
    }

    @Override // com.google.android.gms.internal.measurement.zzaby, com.google.android.gms.internal.measurement.zzace
    public final void zza(zzabw zzabwVar) throws IOException {
        if (this.zzny != null) {
            zzabwVar.zzb(1, this.zzny);
        }
        if (this.value != null) {
            zzabwVar.zzb(2, this.value);
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
                this.zzny = zzabvVar.readString();
            } else if (zzuw == 18) {
                this.value = zzabvVar.readString();
            } else if (!super.zza(zzabvVar, zzuw)) {
                return this;
            }
        }
    }
}
