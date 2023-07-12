package com.google.android.gms.internal.measurement;

/* loaded from: classes.dex */
final class zzgs implements Runnable {
    private final /* synthetic */ zzgn zzanf;
    private final /* synthetic */ zzed zzang;

    /* JADX INFO: Access modifiers changed from: package-private */
    public zzgs(zzgn zzgnVar, zzed zzedVar) {
        this.zzanf = zzgnVar;
        this.zzang = zzedVar;
    }

    @Override // java.lang.Runnable
    public final void run() {
        zzjr zzjrVar;
        zzjr zzjrVar2;
        zzjrVar = this.zzanf.zzajp;
        zzjrVar.zzkx();
        zzjrVar2 = this.zzanf.zzajp;
        zzed zzedVar = this.zzang;
        zzdz zzcb = zzjrVar2.zzcb(zzedVar.packageName);
        if (zzcb != null) {
            zzjrVar2.zzb(zzedVar, zzcb);
        }
    }
}
