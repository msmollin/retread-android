package com.google.android.gms.internal.measurement;

import java.util.concurrent.atomic.AtomicReference;

/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: classes.dex */
public final class zzhp implements Runnable {
    private final /* synthetic */ AtomicReference zzanv;
    private final /* synthetic */ zzhk zzanw;

    /* JADX INFO: Access modifiers changed from: package-private */
    public zzhp(zzhk zzhkVar, AtomicReference atomicReference) {
        this.zzanw = zzhkVar;
        this.zzanv = atomicReference;
    }

    @Override // java.lang.Runnable
    public final void run() {
        this.zzanw.zzfx().zza(this.zzanv);
    }
}
