package com.google.android.gms.internal.measurement;

import java.util.List;
import java.util.Map;

/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: classes.dex */
public final class zzjt implements zzfm {
    private final /* synthetic */ zzjr zzaqu;

    /* JADX INFO: Access modifiers changed from: package-private */
    public zzjt(zzjr zzjrVar) {
        this.zzaqu = zzjrVar;
    }

    @Override // com.google.android.gms.internal.measurement.zzfm
    public final void zza(String str, int i, Throwable th, byte[] bArr, Map<String, List<String>> map) {
        this.zzaqu.zzb(str, i, th, bArr, map);
    }
}
