package com.google.android.gms.internal.measurement;

/* loaded from: classes.dex */
final class zziz implements Runnable {
    private final /* synthetic */ zziw zzapn;
    private final /* synthetic */ zzey zzapo;

    /* JADX INFO: Access modifiers changed from: package-private */
    public zziz(zziw zziwVar, zzey zzeyVar) {
        this.zzapn = zziwVar;
        this.zzapo = zzeyVar;
    }

    @Override // java.lang.Runnable
    public final void run() {
        synchronized (this.zzapn) {
            zziw.zza(this.zzapn, false);
            if (!this.zzapn.zzape.isConnected()) {
                this.zzapn.zzape.zzge().zzis().log("Connected to remote service");
                this.zzapn.zzape.zza(this.zzapo);
            }
        }
    }
}
