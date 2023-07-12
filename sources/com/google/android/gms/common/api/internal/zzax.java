package com.google.android.gms.common.api.internal;

import android.os.Bundle;
import com.google.android.gms.common.api.GoogleApiClient;
import java.util.concurrent.atomic.AtomicReference;

/* loaded from: classes.dex */
final class zzax implements GoogleApiClient.ConnectionCallbacks {
    private final /* synthetic */ zzav zzit;
    private final /* synthetic */ AtomicReference zziu;
    private final /* synthetic */ StatusPendingResult zziv;

    /* JADX INFO: Access modifiers changed from: package-private */
    public zzax(zzav zzavVar, AtomicReference atomicReference, StatusPendingResult statusPendingResult) {
        this.zzit = zzavVar;
        this.zziu = atomicReference;
        this.zziv = statusPendingResult;
    }

    @Override // com.google.android.gms.common.api.GoogleApiClient.ConnectionCallbacks
    public final void onConnected(Bundle bundle) {
        this.zzit.zza((GoogleApiClient) this.zziu.get(), this.zziv, true);
    }

    @Override // com.google.android.gms.common.api.GoogleApiClient.ConnectionCallbacks
    public final void onConnectionSuspended(int i) {
    }
}
