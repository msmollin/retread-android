package com.google.android.gms.tasks;

/* loaded from: classes.dex */
final class zzn implements Runnable {
    private final /* synthetic */ Task zzafn;
    private final /* synthetic */ zzm zzafx;

    /* JADX INFO: Access modifiers changed from: package-private */
    public zzn(zzm zzmVar, Task task) {
        this.zzafx = zzmVar;
        this.zzafn = task;
    }

    @Override // java.lang.Runnable
    public final void run() {
        Object obj;
        OnSuccessListener onSuccessListener;
        OnSuccessListener onSuccessListener2;
        obj = this.zzafx.mLock;
        synchronized (obj) {
            onSuccessListener = this.zzafx.zzafw;
            if (onSuccessListener != null) {
                onSuccessListener2 = this.zzafx.zzafw;
                onSuccessListener2.onSuccess(this.zzafn.getResult());
            }
        }
    }
}
