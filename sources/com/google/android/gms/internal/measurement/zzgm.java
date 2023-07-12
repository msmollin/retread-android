package com.google.android.gms.internal.measurement;

/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: classes.dex */
public final class zzgm implements Runnable {
    private final /* synthetic */ zzhj zzana;
    private final /* synthetic */ zzgl zzanb;

    /* JADX INFO: Access modifiers changed from: package-private */
    public zzgm(zzgl zzglVar, zzhj zzhjVar) {
        this.zzanb = zzglVar;
        this.zzana = zzhjVar;
    }

    @Override // java.lang.Runnable
    public final void run() {
        this.zzanb.zza(this.zzana);
        this.zzanb.start();
    }
}
