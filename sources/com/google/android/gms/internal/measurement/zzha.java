package com.google.android.gms.internal.measurement;

import java.util.concurrent.Callable;

/* loaded from: classes.dex */
final class zzha implements Callable<byte[]> {
    private final /* synthetic */ zzgn zzanf;
    private final /* synthetic */ String zzanj;
    private final /* synthetic */ zzeu zzank;

    /* JADX INFO: Access modifiers changed from: package-private */
    public zzha(zzgn zzgnVar, zzeu zzeuVar, String str) {
        this.zzanf = zzgnVar;
        this.zzank = zzeuVar;
        this.zzanj = str;
    }

    @Override // java.util.concurrent.Callable
    public final /* synthetic */ byte[] call() throws Exception {
        zzjr zzjrVar;
        zzjr zzjrVar2;
        zzjrVar = this.zzanf.zzajp;
        zzjrVar.zzkx();
        zzjrVar2 = this.zzanf.zzajp;
        return zzjrVar2.zza(this.zzank, this.zzanj);
    }
}
