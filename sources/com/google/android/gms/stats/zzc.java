package com.google.android.gms.stats;

import com.google.android.gms.stats.WakeLock;
import java.lang.ref.WeakReference;

/* loaded from: classes.dex */
final class zzc implements Runnable {
    private final /* synthetic */ WeakReference zzaej;

    /* JADX INFO: Access modifiers changed from: package-private */
    public zzc(WeakReference weakReference) {
        this.zzaej = weakReference;
    }

    @Override // java.lang.Runnable
    public final void run() {
        WakeLock.HeldLock heldLock = (WakeLock.HeldLock) this.zzaej.get();
        if (heldLock != null) {
            heldLock.release(0);
        }
    }
}
