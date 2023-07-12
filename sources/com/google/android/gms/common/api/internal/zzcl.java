package com.google.android.gms.common.api.internal;

/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: classes.dex */
public final class zzcl implements zzcn {
    private final /* synthetic */ zzck zzmq;

    /* JADX INFO: Access modifiers changed from: package-private */
    public zzcl(zzck zzckVar) {
        this.zzmq = zzckVar;
    }

    @Override // com.google.android.gms.common.api.internal.zzcn
    public final void zzc(BasePendingResult<?> basePendingResult) {
        this.zzmq.zzmo.remove(basePendingResult);
    }
}
