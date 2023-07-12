package com.google.android.gms.internal.measurement;

import java.util.List;
import java.util.Map;

/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: classes.dex */
public final class zzjs implements zzfm {
    private final /* synthetic */ String zzaqt;
    private final /* synthetic */ zzjr zzaqu;

    /* JADX INFO: Access modifiers changed from: package-private */
    public zzjs(zzjr zzjrVar, String str) {
        this.zzaqu = zzjrVar;
        this.zzaqt = str;
    }

    @Override // com.google.android.gms.internal.measurement.zzfm
    public final void zza(String str, int i, Throwable th, byte[] bArr, Map<String, List<String>> map) {
        this.zzaqu.zza(i, th, bArr, this.zzaqt);
    }
}
