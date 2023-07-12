package com.google.android.gms.internal.measurement;

/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: classes.dex */
public final class zzih implements Runnable {
    private final /* synthetic */ zzif zzaov;
    private final /* synthetic */ zzie zzaow;

    /* JADX INFO: Access modifiers changed from: package-private */
    public zzih(zzif zzifVar, zzie zzieVar) {
        this.zzaov = zzifVar;
        this.zzaow = zzieVar;
    }

    @Override // java.lang.Runnable
    public final void run() {
        this.zzaov.zza(this.zzaow);
        this.zzaov.zzaol = null;
        this.zzaov.zzfx().zzb((zzie) null);
    }
}
