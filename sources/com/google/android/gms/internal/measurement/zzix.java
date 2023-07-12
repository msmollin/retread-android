package com.google.android.gms.internal.measurement;

/* loaded from: classes.dex */
final class zzix implements Runnable {
    private final /* synthetic */ zzey zzapm;
    private final /* synthetic */ zziw zzapn;

    /* JADX INFO: Access modifiers changed from: package-private */
    public zzix(zziw zziwVar, zzey zzeyVar) {
        this.zzapn = zziwVar;
        this.zzapm = zzeyVar;
    }

    @Override // java.lang.Runnable
    public final void run() {
        synchronized (this.zzapn) {
            zziw.zza(this.zzapn, false);
            if (!this.zzapn.zzape.isConnected()) {
                this.zzapn.zzape.zzge().zzit().log("Connected to service");
                this.zzapn.zzape.zza(this.zzapm);
            }
        }
    }
}
