package com.google.android.gms.common.api.internal;

import java.util.concurrent.locks.Lock;

/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: classes.dex */
public final class zzs implements Runnable {
    private final /* synthetic */ zzr zzgc;

    /* JADX INFO: Access modifiers changed from: package-private */
    public zzs(zzr zzrVar) {
        this.zzgc = zzrVar;
    }

    @Override // java.lang.Runnable
    public final void run() {
        Lock lock;
        Lock lock2;
        lock = this.zzgc.zzga;
        lock.lock();
        try {
            this.zzgc.zzaa();
        } finally {
            lock2 = this.zzgc.zzga;
            lock2.unlock();
        }
    }
}
