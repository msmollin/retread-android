package com.google.android.gms.internal.measurement;

import java.util.List;
import java.util.concurrent.Callable;

/* loaded from: classes.dex */
final class zzgu implements Callable<List<zzjz>> {
    private final /* synthetic */ zzgn zzanf;
    private final /* synthetic */ String zzanh;
    private final /* synthetic */ String zzani;
    private final /* synthetic */ String zzanj;

    /* JADX INFO: Access modifiers changed from: package-private */
    public zzgu(zzgn zzgnVar, String str, String str2, String str3) {
        this.zzanf = zzgnVar;
        this.zzanj = str;
        this.zzanh = str2;
        this.zzani = str3;
    }

    @Override // java.util.concurrent.Callable
    public final /* synthetic */ List<zzjz> call() throws Exception {
        zzjr zzjrVar;
        zzjr zzjrVar2;
        zzjrVar = this.zzanf.zzajp;
        zzjrVar.zzkx();
        zzjrVar2 = this.zzanf.zzajp;
        return zzjrVar2.zzix().zzb(this.zzanj, this.zzanh, this.zzani);
    }
}
