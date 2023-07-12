package com.google.android.gms.internal.measurement;

/* loaded from: classes.dex */
final class zzgy implements Runnable {
    private final /* synthetic */ zzdz zzane;
    private final /* synthetic */ zzgn zzanf;
    private final /* synthetic */ zzeu zzank;

    /* JADX INFO: Access modifiers changed from: package-private */
    public zzgy(zzgn zzgnVar, zzeu zzeuVar, zzdz zzdzVar) {
        this.zzanf = zzgnVar;
        this.zzank = zzeuVar;
        this.zzane = zzdzVar;
    }

    @Override // java.lang.Runnable
    public final void run() {
        zzjr zzjrVar;
        zzjr zzjrVar2;
        zzjrVar = this.zzanf.zzajp;
        zzjrVar.zzkx();
        zzjrVar2 = this.zzanf.zzajp;
        zzjrVar2.zzb(this.zzank, this.zzane);
    }
}
