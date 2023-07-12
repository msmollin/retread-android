package com.google.android.gms.internal.measurement;

import java.util.concurrent.atomic.AtomicReference;

/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: classes.dex */
public final class zzhn implements Runnable {
    private final /* synthetic */ AtomicReference zzanv;
    private final /* synthetic */ zzhk zzanw;
    private final /* synthetic */ boolean zzanz;

    /* JADX INFO: Access modifiers changed from: package-private */
    public zzhn(zzhk zzhkVar, AtomicReference atomicReference, boolean z) {
        this.zzanw = zzhkVar;
        this.zzanv = atomicReference;
        this.zzanz = z;
    }

    @Override // java.lang.Runnable
    public final void run() {
        this.zzanw.zzfx().zza(this.zzanv, this.zzanz);
    }
}
