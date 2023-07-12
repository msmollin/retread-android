package com.google.android.gms.internal.measurement;

import java.util.List;
import java.util.concurrent.Callable;

/* loaded from: classes.dex */
final class zzgv implements Callable<List<zzed>> {
    private final /* synthetic */ zzdz zzane;
    private final /* synthetic */ zzgn zzanf;
    private final /* synthetic */ String zzanh;
    private final /* synthetic */ String zzani;

    /* JADX INFO: Access modifiers changed from: package-private */
    public zzgv(zzgn zzgnVar, zzdz zzdzVar, String str, String str2) {
        this.zzanf = zzgnVar;
        this.zzane = zzdzVar;
        this.zzanh = str;
        this.zzani = str2;
    }

    @Override // java.util.concurrent.Callable
    public final /* synthetic */ List<zzed> call() throws Exception {
        zzjr zzjrVar;
        zzjr zzjrVar2;
        zzjrVar = this.zzanf.zzajp;
        zzjrVar.zzkx();
        zzjrVar2 = this.zzanf.zzajp;
        return zzjrVar2.zzix().zzc(this.zzane.packageName, this.zzanh, this.zzani);
    }
}
