package com.google.android.gms.internal.measurement;

/* loaded from: classes.dex */
final class zzfq implements Runnable {
    private final /* synthetic */ boolean zzajq;
    private final /* synthetic */ zzfp zzajr;

    /* JADX INFO: Access modifiers changed from: package-private */
    public zzfq(zzfp zzfpVar, boolean z) {
        this.zzajr = zzfpVar;
        this.zzajq = z;
    }

    @Override // java.lang.Runnable
    public final void run() {
        zzjr zzjrVar;
        zzjrVar = this.zzajr.zzajp;
        zzjrVar.zzm(this.zzajq);
    }
}
