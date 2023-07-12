package com.google.android.gms.internal.measurement;

/* loaded from: classes.dex */
final class zzgq implements Runnable {
    private final /* synthetic */ zzdz zzane;
    private final /* synthetic */ zzgn zzanf;
    private final /* synthetic */ zzed zzang;

    /* JADX INFO: Access modifiers changed from: package-private */
    public zzgq(zzgn zzgnVar, zzed zzedVar, zzdz zzdzVar) {
        this.zzanf = zzgnVar;
        this.zzang = zzedVar;
        this.zzane = zzdzVar;
    }

    @Override // java.lang.Runnable
    public final void run() {
        zzjr zzjrVar;
        zzjr zzjrVar2;
        zzjrVar = this.zzanf.zzajp;
        zzjrVar.zzkx();
        zzjrVar2 = this.zzanf.zzajp;
        zzjrVar2.zzb(this.zzang, this.zzane);
    }
}
