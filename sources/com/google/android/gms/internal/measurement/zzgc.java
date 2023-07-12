package com.google.android.gms.internal.measurement;

/* loaded from: classes.dex */
final class zzgc implements Runnable {
    private final /* synthetic */ zzgl zzalb;
    private final /* synthetic */ zzfg zzalc;

    /* JADX INFO: Access modifiers changed from: package-private */
    public zzgc(zzgb zzgbVar, zzgl zzglVar, zzfg zzfgVar) {
        this.zzalb = zzglVar;
        this.zzalc = zzfgVar;
    }

    @Override // java.lang.Runnable
    public final void run() {
        if (this.zzalb.zzjp() == null) {
            this.zzalc.zzim().log("Install Referrer Reporter is null");
        } else {
            this.zzalb.zzjp().zzjh();
        }
    }
}
