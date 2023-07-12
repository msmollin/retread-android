package com.google.android.gms.internal.measurement;

import com.google.android.gms.internal.measurement.zzaby;
import java.io.IOException;

/* loaded from: classes.dex */
public abstract class zzaby<M extends zzaby<M>> extends zzace {
    protected zzaca zzbww;

    @Override // com.google.android.gms.internal.measurement.zzace
    public /* synthetic */ Object clone() throws CloneNotSupportedException {
        zzaby zzabyVar = (zzaby) super.clone();
        zzacc.zza(this, zzabyVar);
        return zzabyVar;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.google.android.gms.internal.measurement.zzace
    public int zza() {
        if (this.zzbww != null) {
            int i = 0;
            for (int i2 = 0; i2 < this.zzbww.size(); i2++) {
                i += this.zzbww.zzau(i2).zza();
            }
            return i;
        }
        return 0;
    }

    public final <T> T zza(zzabz<M, T> zzabzVar) {
        zzacb zzat;
        if (this.zzbww == null || (zzat = this.zzbww.zzat(zzabzVar.tag >>> 3)) == null) {
            return null;
        }
        return (T) zzat.zzb(zzabzVar);
    }

    @Override // com.google.android.gms.internal.measurement.zzace
    public void zza(zzabw zzabwVar) throws IOException {
        if (this.zzbww == null) {
            return;
        }
        for (int i = 0; i < this.zzbww.size(); i++) {
            this.zzbww.zzau(i).zza(zzabwVar);
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public final boolean zza(zzabv zzabvVar, int i) throws IOException {
        int position = zzabvVar.getPosition();
        if (zzabvVar.zzak(i)) {
            int i2 = i >>> 3;
            zzacg zzacgVar = new zzacg(i, zzabvVar.zzc(position, zzabvVar.getPosition() - position));
            zzacb zzacbVar = null;
            if (this.zzbww == null) {
                this.zzbww = new zzaca();
            } else {
                zzacbVar = this.zzbww.zzat(i2);
            }
            if (zzacbVar == null) {
                zzacbVar = new zzacb();
                this.zzbww.zza(i2, zzacbVar);
            }
            zzacbVar.zza(zzacgVar);
            return true;
        }
        return false;
    }

    @Override // com.google.android.gms.internal.measurement.zzace
    public final /* synthetic */ zzace zzvf() throws CloneNotSupportedException {
        return (zzaby) clone();
    }
}
