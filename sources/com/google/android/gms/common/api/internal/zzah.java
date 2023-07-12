package com.google.android.gms.common.api.internal;

/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: classes.dex */
public final class zzah extends zzbe {
    private final /* synthetic */ zzag zzhh;

    /* JADX INFO: Access modifiers changed from: package-private */
    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public zzah(zzag zzagVar, zzbc zzbcVar) {
        super(zzbcVar);
        this.zzhh = zzagVar;
    }

    @Override // com.google.android.gms.common.api.internal.zzbe
    public final void zzaq() {
        this.zzhh.onConnectionSuspended(1);
    }
}
