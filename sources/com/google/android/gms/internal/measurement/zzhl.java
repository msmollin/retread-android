package com.google.android.gms.internal.measurement;

import java.util.concurrent.atomic.AtomicReference;

/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: classes.dex */
public final class zzhl implements Runnable {
    private final /* synthetic */ AtomicReference zzanv;
    private final /* synthetic */ zzhk zzanw;

    /* JADX INFO: Access modifiers changed from: package-private */
    public zzhl(zzhk zzhkVar, AtomicReference atomicReference) {
        this.zzanw = zzhkVar;
        this.zzanv = atomicReference;
    }

    @Override // java.lang.Runnable
    public final void run() {
        synchronized (this.zzanv) {
            this.zzanv.set(Boolean.valueOf(this.zzanw.zzgg().zzhl()));
            this.zzanv.notify();
        }
    }
}
