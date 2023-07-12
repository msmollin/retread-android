package com.google.android.gms.internal.measurement;

/* loaded from: classes.dex */
final class zzga implements Runnable {
    private final /* synthetic */ zzfz zzakz;

    /* JADX INFO: Access modifiers changed from: package-private */
    public zzga(zzfz zzfzVar) {
        this.zzakz = zzfzVar;
    }

    @Override // java.lang.Runnable
    public final void run() {
        this.zzakz.zzaky.zzc(this.zzakz.zzaky.zzjj());
    }
}
