package com.google.android.gms.internal.measurement;

import android.os.Bundle;

/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: classes.dex */
public final class zzig implements Runnable {
    private final /* synthetic */ boolean zzaos;
    private final /* synthetic */ zzie zzaot;
    private final /* synthetic */ zzie zzaou;
    private final /* synthetic */ zzif zzaov;

    /* JADX INFO: Access modifiers changed from: package-private */
    public zzig(zzif zzifVar, boolean z, zzie zzieVar, zzie zzieVar2) {
        this.zzaov = zzifVar;
        this.zzaos = z;
        this.zzaot = zzieVar;
        this.zzaou = zzieVar2;
    }

    @Override // java.lang.Runnable
    public final void run() {
        if (this.zzaos && this.zzaov.zzaol != null) {
            this.zzaov.zza(this.zzaov.zzaol);
        }
        if ((this.zzaot != null && this.zzaot.zzaoj == this.zzaou.zzaoj && zzka.zzs(this.zzaot.zzaoi, this.zzaou.zzaoi) && zzka.zzs(this.zzaot.zzul, this.zzaou.zzul)) ? false : true) {
            Bundle bundle = new Bundle();
            zzif.zza(this.zzaou, bundle, true);
            if (this.zzaot != null) {
                if (this.zzaot.zzul != null) {
                    bundle.putString("_pn", this.zzaot.zzul);
                }
                bundle.putString("_pc", this.zzaot.zzaoi);
                bundle.putLong("_pi", this.zzaot.zzaoj);
            }
            this.zzaov.zzfu().zza("auto", "_vs", bundle);
        }
        this.zzaov.zzaol = this.zzaou;
        this.zzaov.zzfx().zzb(this.zzaou);
    }
}
