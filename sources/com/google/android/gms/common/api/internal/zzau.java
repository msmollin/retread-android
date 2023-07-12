package com.google.android.gms.common.api.internal;

import android.os.Bundle;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.Api;
import com.google.android.gms.common.api.Result;
import com.google.android.gms.common.api.internal.BaseImplementation;
import java.util.Collections;

/* loaded from: classes.dex */
public final class zzau implements zzbc {
    private final zzbd zzhf;

    public zzau(zzbd zzbdVar) {
        this.zzhf = zzbdVar;
    }

    @Override // com.google.android.gms.common.api.internal.zzbc
    public final void begin() {
        for (Api.Client client : this.zzhf.zzil.values()) {
            client.disconnect();
        }
        this.zzhf.zzfq.zzim = Collections.emptySet();
    }

    @Override // com.google.android.gms.common.api.internal.zzbc
    public final void connect() {
        this.zzhf.zzbc();
    }

    @Override // com.google.android.gms.common.api.internal.zzbc
    public final boolean disconnect() {
        return true;
    }

    @Override // com.google.android.gms.common.api.internal.zzbc
    public final <A extends Api.AnyClient, R extends Result, T extends BaseImplementation.ApiMethodImpl<R, A>> T enqueue(T t) {
        this.zzhf.zzfq.zzgo.add(t);
        return t;
    }

    @Override // com.google.android.gms.common.api.internal.zzbc
    public final <A extends Api.AnyClient, T extends BaseImplementation.ApiMethodImpl<? extends Result, A>> T execute(T t) {
        throw new IllegalStateException("GoogleApiClient is not connected yet.");
    }

    @Override // com.google.android.gms.common.api.internal.zzbc
    public final void onConnected(Bundle bundle) {
    }

    @Override // com.google.android.gms.common.api.internal.zzbc
    public final void onConnectionSuspended(int i) {
    }

    @Override // com.google.android.gms.common.api.internal.zzbc
    public final void zza(ConnectionResult connectionResult, Api<?> api, boolean z) {
    }
}
