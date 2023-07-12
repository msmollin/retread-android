package com.google.android.gms.internal.measurement;

/* loaded from: classes.dex */
final class zzgz implements Runnable {
    private final /* synthetic */ zzgn zzanf;
    private final /* synthetic */ String zzanj;
    private final /* synthetic */ zzeu zzank;

    /* JADX INFO: Access modifiers changed from: package-private */
    public zzgz(zzgn zzgnVar, zzeu zzeuVar, String str) {
        this.zzanf = zzgnVar;
        this.zzank = zzeuVar;
        this.zzanj = str;
    }

    @Override // java.lang.Runnable
    public final void run() {
        zzjr zzjrVar;
        zzjr zzjrVar2;
        zzjrVar = this.zzanf.zzajp;
        zzjrVar.zzkx();
        zzjrVar2 = this.zzanf.zzajp;
        zzjrVar2.zzc(this.zzank, this.zzanj);
    }
}
