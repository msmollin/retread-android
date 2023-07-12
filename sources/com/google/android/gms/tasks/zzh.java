package com.google.android.gms.tasks;

/* loaded from: classes.dex */
final class zzh implements Runnable {
    private final /* synthetic */ zzg zzafr;

    /* JADX INFO: Access modifiers changed from: package-private */
    public zzh(zzg zzgVar) {
        this.zzafr = zzgVar;
    }

    @Override // java.lang.Runnable
    public final void run() {
        Object obj;
        OnCanceledListener onCanceledListener;
        OnCanceledListener onCanceledListener2;
        obj = this.zzafr.mLock;
        synchronized (obj) {
            onCanceledListener = this.zzafr.zzafq;
            if (onCanceledListener != null) {
                onCanceledListener2 = this.zzafr.zzafq;
                onCanceledListener2.onCanceled();
            }
        }
    }
}
