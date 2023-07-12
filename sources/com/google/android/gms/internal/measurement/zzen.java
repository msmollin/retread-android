package com.google.android.gms.internal.measurement;

/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: classes.dex */
public final class zzen implements Runnable {
    private final /* synthetic */ zzhi zzafk;
    private final /* synthetic */ zzem zzafl;

    /* JADX INFO: Access modifiers changed from: package-private */
    public zzen(zzem zzemVar, zzhi zzhiVar) {
        this.zzafl = zzemVar;
        this.zzafk = zzhiVar;
    }

    @Override // java.lang.Runnable
    public final void run() {
        this.zzafk.zzgd();
        if (zzgg.isMainThread()) {
            this.zzafk.zzgd().zzc(this);
            return;
        }
        boolean zzef = this.zzafl.zzef();
        zzem.zza(this.zzafl, 0L);
        if (zzef) {
            this.zzafl.run();
        }
    }
}
