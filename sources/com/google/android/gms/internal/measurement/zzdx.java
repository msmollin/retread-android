package com.google.android.gms.internal.measurement;

/* loaded from: classes.dex */
final class zzdx implements Runnable {
    private final /* synthetic */ long zzadj;
    private final /* synthetic */ zzdu zzadk;

    /* JADX INFO: Access modifiers changed from: package-private */
    public zzdx(zzdu zzduVar, long j) {
        this.zzadk = zzduVar;
        this.zzadj = j;
    }

    @Override // java.lang.Runnable
    public final void run() {
        this.zzadk.zzl(this.zzadj);
    }
}
