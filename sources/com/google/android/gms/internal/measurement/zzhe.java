package com.google.android.gms.internal.measurement;

/* loaded from: classes.dex */
final class zzhe implements Runnable {
    private final /* synthetic */ zzdz zzane;
    private final /* synthetic */ zzgn zzanf;

    /* JADX INFO: Access modifiers changed from: package-private */
    public zzhe(zzgn zzgnVar, zzdz zzdzVar) {
        this.zzanf = zzgnVar;
        this.zzane = zzdzVar;
    }

    @Override // java.lang.Runnable
    public final void run() {
        zzjr zzjrVar;
        zzjr zzjrVar2;
        zzjrVar = this.zzanf.zzajp;
        zzjrVar.zzkx();
        zzjrVar2 = this.zzanf.zzajp;
        zzjrVar2.zzf(this.zzane);
    }
}
