package com.google.android.gms.tasks;

/* loaded from: classes.dex */
final class zzl implements Runnable {
    private final /* synthetic */ Task zzafn;
    private final /* synthetic */ zzk zzafv;

    /* JADX INFO: Access modifiers changed from: package-private */
    public zzl(zzk zzkVar, Task task) {
        this.zzafv = zzkVar;
        this.zzafn = task;
    }

    @Override // java.lang.Runnable
    public final void run() {
        Object obj;
        OnFailureListener onFailureListener;
        OnFailureListener onFailureListener2;
        obj = this.zzafv.mLock;
        synchronized (obj) {
            onFailureListener = this.zzafv.zzafu;
            if (onFailureListener != null) {
                onFailureListener2 = this.zzafv.zzafu;
                onFailureListener2.onFailure(this.zzafn.getException());
            }
        }
    }
}
