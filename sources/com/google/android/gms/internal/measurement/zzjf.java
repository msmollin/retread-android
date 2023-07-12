package com.google.android.gms.internal.measurement;

/* loaded from: classes.dex */
final class zzjf implements Runnable {
    private final /* synthetic */ Runnable zzabt;
    private final /* synthetic */ zzjr zzapt;

    /* JADX INFO: Access modifiers changed from: package-private */
    public zzjf(zzjc zzjcVar, zzjr zzjrVar, Runnable runnable) {
        this.zzapt = zzjrVar;
        this.zzabt = runnable;
    }

    @Override // java.lang.Runnable
    public final void run() {
        this.zzapt.zzkx();
        this.zzapt.zzg(this.zzabt);
        this.zzapt.zzks();
    }
}
