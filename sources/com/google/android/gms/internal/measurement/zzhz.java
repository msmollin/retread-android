package com.google.android.gms.internal.measurement;

/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: classes.dex */
public final class zzhz implements Runnable {
    private final /* synthetic */ zzhk zzanw;
    private final /* synthetic */ boolean zzaoc;

    /* JADX INFO: Access modifiers changed from: package-private */
    public zzhz(zzhk zzhkVar, boolean z) {
        this.zzanw = zzhkVar;
        this.zzaoc = z;
    }

    @Override // java.lang.Runnable
    public final void run() {
        this.zzanw.zzi(this.zzaoc);
    }
}
