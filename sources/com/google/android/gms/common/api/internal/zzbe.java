package com.google.android.gms.common.api.internal;

import java.util.concurrent.locks.Lock;

/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: classes.dex */
public abstract class zzbe {
    private final zzbc zzjg;

    /* JADX INFO: Access modifiers changed from: protected */
    public zzbe(zzbc zzbcVar) {
        this.zzjg = zzbcVar;
    }

    protected abstract void zzaq();

    public final void zzc(zzbd zzbdVar) {
        Lock lock;
        Lock lock2;
        zzbc zzbcVar;
        lock = zzbdVar.zzga;
        lock.lock();
        try {
            zzbcVar = zzbdVar.zzjc;
            if (zzbcVar == this.zzjg) {
                zzaq();
            }
        } finally {
            lock2 = zzbdVar.zzga;
            lock2.unlock();
        }
    }
}
