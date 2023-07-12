package com.google.android.gms.internal.measurement;

import java.io.IOException;

/* loaded from: classes.dex */
public final class zzkp extends zzaby<zzkp> {
    public zzkq[] zzatf = zzkq.zzln();

    public zzkp() {
        this.zzbww = null;
        this.zzbxh = -1;
    }

    public final boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (obj instanceof zzkp) {
            zzkp zzkpVar = (zzkp) obj;
            if (zzacc.equals(this.zzatf, zzkpVar.zzatf)) {
                return (this.zzbww == null || this.zzbww.isEmpty()) ? zzkpVar.zzbww == null || zzkpVar.zzbww.isEmpty() : this.zzbww.equals(zzkpVar.zzbww);
            }
            return false;
        }
        return false;
    }

    public final int hashCode() {
        return ((((getClass().getName().hashCode() + 527) * 31) + zzacc.hashCode(this.zzatf)) * 31) + ((this.zzbww == null || this.zzbww.isEmpty()) ? 0 : this.zzbww.hashCode());
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.google.android.gms.internal.measurement.zzaby, com.google.android.gms.internal.measurement.zzace
    public final int zza() {
        int zza = super.zza();
        if (this.zzatf != null && this.zzatf.length > 0) {
            for (int i = 0; i < this.zzatf.length; i++) {
                zzkq zzkqVar = this.zzatf[i];
                if (zzkqVar != null) {
                    zza += zzabw.zzb(1, zzkqVar);
                }
            }
        }
        return zza;
    }

    @Override // com.google.android.gms.internal.measurement.zzaby, com.google.android.gms.internal.measurement.zzace
    public final void zza(zzabw zzabwVar) throws IOException {
        if (this.zzatf != null && this.zzatf.length > 0) {
            for (int i = 0; i < this.zzatf.length; i++) {
                zzkq zzkqVar = this.zzatf[i];
                if (zzkqVar != null) {
                    zzabwVar.zza(1, zzkqVar);
                }
            }
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
                int length = this.zzatf == null ? 0 : this.zzatf.length;
                zzkq[] zzkqVarArr = new zzkq[zzb + length];
                if (length != 0) {
                    System.arraycopy(this.zzatf, 0, zzkqVarArr, 0, length);
                }
                while (length < zzkqVarArr.length - 1) {
                    zzkqVarArr[length] = new zzkq();
                    zzabvVar.zza(zzkqVarArr[length]);
                    zzabvVar.zzuw();
                    length++;
                }
                zzkqVarArr[length] = new zzkq();
                zzabvVar.zza(zzkqVarArr[length]);
                this.zzatf = zzkqVarArr;
            } else if (!super.zza(zzabvVar, zzuw)) {
                return this;
            }
        }
    }
}
