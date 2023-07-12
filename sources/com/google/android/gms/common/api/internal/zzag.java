package com.google.android.gms.common.api.internal;

import android.os.Bundle;
import android.os.DeadObjectException;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.Api;
import com.google.android.gms.common.api.Result;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.common.api.internal.BaseImplementation;
import com.google.android.gms.common.internal.Preconditions;
import com.google.android.gms.common.internal.SimpleClientAdapter;

/* loaded from: classes.dex */
public final class zzag implements zzbc {
    private final zzbd zzhf;
    private boolean zzhg = false;

    public zzag(zzbd zzbdVar) {
        this.zzhf = zzbdVar;
    }

    @Override // com.google.android.gms.common.api.internal.zzbc
    public final void begin() {
    }

    @Override // com.google.android.gms.common.api.internal.zzbc
    public final void connect() {
        if (this.zzhg) {
            this.zzhg = false;
            this.zzhf.zza(new zzai(this, this));
        }
    }

    @Override // com.google.android.gms.common.api.internal.zzbc
    public final boolean disconnect() {
        if (this.zzhg) {
            return false;
        }
        if (!this.zzhf.zzfq.zzba()) {
            this.zzhf.zzf(null);
            return true;
        }
        this.zzhg = true;
        for (zzch zzchVar : this.zzhf.zzfq.zziq) {
            zzchVar.zzcc();
        }
        return false;
    }

    @Override // com.google.android.gms.common.api.internal.zzbc
    public final <A extends Api.AnyClient, R extends Result, T extends BaseImplementation.ApiMethodImpl<R, A>> T enqueue(T t) {
        return (T) execute(t);
    }

    @Override // com.google.android.gms.common.api.internal.zzbc
    public final <A extends Api.AnyClient, T extends BaseImplementation.ApiMethodImpl<? extends Result, A>> T execute(T t) {
        try {
            this.zzhf.zzfq.zzir.zzb(t);
            zzav zzavVar = this.zzhf.zzfq;
            Api.Client client = zzavVar.zzil.get(t.getClientKey());
            Preconditions.checkNotNull(client, "Appropriate Api was not requested.");
            if (client.isConnected() || !this.zzhf.zzjb.containsKey(t.getClientKey())) {
                boolean z = client instanceof SimpleClientAdapter;
                Api.SimpleClient simpleClient = client;
                if (z) {
                    simpleClient = ((SimpleClientAdapter) client).getClient();
                }
                t.run(simpleClient);
            } else {
                t.setFailedResult(new Status(17));
            }
        } catch (DeadObjectException unused) {
            this.zzhf.zza(new zzah(this, this));
        }
        return t;
    }

    @Override // com.google.android.gms.common.api.internal.zzbc
    public final void onConnected(Bundle bundle) {
    }

    @Override // com.google.android.gms.common.api.internal.zzbc
    public final void onConnectionSuspended(int i) {
        this.zzhf.zzf(null);
        this.zzhf.zzjf.zzb(i, this.zzhg);
    }

    @Override // com.google.android.gms.common.api.internal.zzbc
    public final void zza(ConnectionResult connectionResult, Api<?> api, boolean z) {
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public final void zzap() {
        if (this.zzhg) {
            this.zzhg = false;
            this.zzhf.zzfq.zzir.release();
            disconnect();
        }
    }
}
