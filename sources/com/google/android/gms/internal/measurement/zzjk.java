package com.google.android.gms.internal.measurement;

/* loaded from: classes.dex */
final class zzjk implements Runnable {
    private final /* synthetic */ long zzadj;
    private final /* synthetic */ zzjh zzapx;

    /* JADX INFO: Access modifiers changed from: package-private */
    public zzjk(zzjh zzjhVar, long j) {
        this.zzapx = zzjhVar;
        this.zzadj = j;
    }

    @Override // java.lang.Runnable
    public final void run() {
        this.zzapx.zzaf(this.zzadj);
    }
}
