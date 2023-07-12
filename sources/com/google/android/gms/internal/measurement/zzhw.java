package com.google.android.gms.internal.measurement;

import java.util.concurrent.atomic.AtomicReference;

/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: classes.dex */
public final class zzhw implements Runnable {
    private final /* synthetic */ AtomicReference zzanv;
    private final /* synthetic */ zzhk zzanw;

    /* JADX INFO: Access modifiers changed from: package-private */
    public zzhw(zzhk zzhkVar, AtomicReference atomicReference) {
        this.zzanw = zzhkVar;
        this.zzanv = atomicReference;
    }

    @Override // java.lang.Runnable
    public final void run() {
        synchronized (this.zzanv) {
            AtomicReference atomicReference = this.zzanv;
            zzef zzgg = this.zzanw.zzgg();
            atomicReference.set(Long.valueOf(zzgg.zza(zzgg.zzfv().zzah(), zzew.zzahr)));
            this.zzanv.notify();
        }
    }
}
