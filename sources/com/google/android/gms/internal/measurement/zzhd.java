package com.google.android.gms.internal.measurement;

import java.util.List;
import java.util.concurrent.Callable;

/* loaded from: classes.dex */
final class zzhd implements Callable<List<zzjz>> {
    private final /* synthetic */ zzdz zzane;
    private final /* synthetic */ zzgn zzanf;

    /* JADX INFO: Access modifiers changed from: package-private */
    public zzhd(zzgn zzgnVar, zzdz zzdzVar) {
        this.zzanf = zzgnVar;
        this.zzane = zzdzVar;
    }

    @Override // java.util.concurrent.Callable
    public final /* synthetic */ List<zzjz> call() throws Exception {
        zzjr zzjrVar;
        zzjr zzjrVar2;
        zzjrVar = this.zzanf.zzajp;
        zzjrVar.zzkx();
        zzjrVar2 = this.zzanf.zzajp;
        return zzjrVar2.zzix().zzbb(this.zzane.packageName);
    }
}
