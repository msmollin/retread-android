package com.google.android.gms.internal.measurement;

/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: classes.dex */
public final class zzdv implements Runnable {
    private final /* synthetic */ String zzadi;
    private final /* synthetic */ long zzadj;
    private final /* synthetic */ zzdu zzadk;

    /* JADX INFO: Access modifiers changed from: package-private */
    public zzdv(zzdu zzduVar, String str, long j) {
        this.zzadk = zzduVar;
        this.zzadi = str;
        this.zzadj = j;
    }

    @Override // java.lang.Runnable
    public final void run() {
        this.zzadk.zza(this.zzadi, this.zzadj);
    }
}
