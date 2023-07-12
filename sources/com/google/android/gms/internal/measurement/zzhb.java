package com.google.android.gms.internal.measurement;

/* loaded from: classes.dex */
final class zzhb implements Runnable {
    private final /* synthetic */ zzdz zzane;
    private final /* synthetic */ zzgn zzanf;
    private final /* synthetic */ zzjx zzanl;

    /* JADX INFO: Access modifiers changed from: package-private */
    public zzhb(zzgn zzgnVar, zzjx zzjxVar, zzdz zzdzVar) {
        this.zzanf = zzgnVar;
        this.zzanl = zzjxVar;
        this.zzane = zzdzVar;
    }

    @Override // java.lang.Runnable
    public final void run() {
        zzjr zzjrVar;
        zzjr zzjrVar2;
        zzjrVar = this.zzanf.zzajp;
        zzjrVar.zzkx();
        zzjrVar2 = this.zzanf.zzajp;
        zzjrVar2.zzc(this.zzanl, this.zzane);
    }
}
