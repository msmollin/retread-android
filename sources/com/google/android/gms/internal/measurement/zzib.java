package com.google.android.gms.internal.measurement;

/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: classes.dex */
public final class zzib implements Runnable {
    private final /* synthetic */ zzhk zzanw;
    private final /* synthetic */ long zzaod;

    /* JADX INFO: Access modifiers changed from: package-private */
    public zzib(zzhk zzhkVar, long j) {
        this.zzanw = zzhkVar;
        this.zzaod = j;
    }

    @Override // java.lang.Runnable
    public final void run() {
        this.zzanw.zzgf().zzakj.set(this.zzaod);
        this.zzanw.zzge().zzis().zzg("Session timeout duration set", Long.valueOf(this.zzaod));
    }
}
