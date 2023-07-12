package com.google.android.gms.tasks;

/* loaded from: classes.dex */
final class zzj implements Runnable {
    private final /* synthetic */ Task zzafn;
    private final /* synthetic */ zzi zzaft;

    /* JADX INFO: Access modifiers changed from: package-private */
    public zzj(zzi zziVar, Task task) {
        this.zzaft = zziVar;
        this.zzafn = task;
    }

    @Override // java.lang.Runnable
    public final void run() {
        Object obj;
        OnCompleteListener onCompleteListener;
        OnCompleteListener onCompleteListener2;
        obj = this.zzaft.mLock;
        synchronized (obj) {
            onCompleteListener = this.zzaft.zzafs;
            if (onCompleteListener != null) {
                onCompleteListener2 = this.zzaft.zzafs;
                onCompleteListener2.onComplete(this.zzafn);
            }
        }
    }
}
