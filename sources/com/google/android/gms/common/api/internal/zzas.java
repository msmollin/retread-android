package com.google.android.gms.common.api.internal;

import android.os.Bundle;
import androidx.annotation.NonNull;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.signin.SignInClient;
import java.util.concurrent.locks.Lock;

/* loaded from: classes.dex */
final class zzas implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {
    private final /* synthetic */ zzaj zzhv;

    private zzas(zzaj zzajVar) {
        this.zzhv = zzajVar;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public /* synthetic */ zzas(zzaj zzajVar, zzak zzakVar) {
        this(zzajVar);
    }

    @Override // com.google.android.gms.common.api.GoogleApiClient.ConnectionCallbacks
    public final void onConnected(Bundle bundle) {
        SignInClient signInClient;
        signInClient = this.zzhv.zzhn;
        signInClient.signIn(new zzaq(this.zzhv));
    }

    @Override // com.google.android.gms.common.api.GoogleApiClient.OnConnectionFailedListener
    public final void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Lock lock;
        Lock lock2;
        boolean zzd;
        lock = this.zzhv.zzga;
        lock.lock();
        try {
            zzd = this.zzhv.zzd(connectionResult);
            if (zzd) {
                this.zzhv.zzau();
                this.zzhv.zzas();
            } else {
                this.zzhv.zze(connectionResult);
            }
        } finally {
            lock2 = this.zzhv.zzga;
            lock2.unlock();
        }
    }

    @Override // com.google.android.gms.common.api.GoogleApiClient.ConnectionCallbacks
    public final void onConnectionSuspended(int i) {
    }
}
