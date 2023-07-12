package com.google.android.gms.internal.measurement;

/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: classes.dex */
public final class zzia implements Runnable {
    private final /* synthetic */ zzhk zzanw;
    private final /* synthetic */ long zzaod;

    /* JADX INFO: Access modifiers changed from: package-private */
    public zzia(zzhk zzhkVar, long j) {
        this.zzanw = zzhkVar;
        this.zzaod = j;
    }

    @Override // java.lang.Runnable
    public final void run() {
        this.zzanw.zzgf().zzaki.set(this.zzaod);
        this.zzanw.zzge().zzis().zzg("Minimum session duration set", Long.valueOf(this.zzaod));
    }
}
