package com.google.android.gms.internal.measurement;

/* loaded from: classes.dex */
final class zzjb implements Runnable {
    private final /* synthetic */ zziw zzapn;

    /* JADX INFO: Access modifiers changed from: package-private */
    public zzjb(zziw zziwVar) {
        this.zzapn = zziwVar;
    }

    @Override // java.lang.Runnable
    public final void run() {
        zzii.zza(this.zzapn.zzape, (zzey) null);
        this.zzapn.zzape.zzkg();
    }
}
