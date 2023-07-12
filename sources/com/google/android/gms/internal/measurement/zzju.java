package com.google.android.gms.internal.measurement;

import java.util.concurrent.Callable;

/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: classes.dex */
public final class zzju implements Callable<String> {
    private final /* synthetic */ zzdz zzane;
    private final /* synthetic */ zzjr zzaqu;

    /* JADX INFO: Access modifiers changed from: package-private */
    public zzju(zzjr zzjrVar, zzdz zzdzVar) {
        this.zzaqu = zzjrVar;
        this.zzane = zzdzVar;
    }

    @Override // java.util.concurrent.Callable
    public final /* synthetic */ String call() throws Exception {
        zzdy zzg = this.zzaqu.zzgg().zzaz(this.zzane.packageName) ? this.zzaqu.zzg(this.zzane) : this.zzaqu.zzix().zzbc(this.zzane.packageName);
        if (zzg == null) {
            this.zzaqu.zzge().zzip().log("App info was null when attempting to get app instance id");
            return null;
        }
        return zzg.getAppInstanceId();
    }
}
