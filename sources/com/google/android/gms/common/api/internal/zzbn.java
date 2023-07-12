package com.google.android.gms.common.api.internal;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.Api;
import com.google.android.gms.common.api.internal.GoogleApiManager;
import java.util.Collections;
import java.util.Map;

/* loaded from: classes.dex */
final class zzbn implements Runnable {
    private final /* synthetic */ ConnectionResult zzkl;
    private final /* synthetic */ GoogleApiManager.zzc zzkr;

    /* JADX INFO: Access modifiers changed from: package-private */
    public zzbn(GoogleApiManager.zzc zzcVar, ConnectionResult connectionResult) {
        this.zzkr = zzcVar;
        this.zzkl = connectionResult;
    }

    @Override // java.lang.Runnable
    public final void run() {
        zzh zzhVar;
        Api.Client client;
        Api.Client client2;
        if (!this.zzkl.isSuccess()) {
            Map map = GoogleApiManager.this.zzju;
            zzhVar = this.zzkr.zzhc;
            ((GoogleApiManager.zza) map.get(zzhVar)).onConnectionFailed(this.zzkl);
            return;
        }
        GoogleApiManager.zzc.zza(this.zzkr, true);
        client = this.zzkr.zzka;
        if (client.requiresSignIn()) {
            this.zzkr.zzbu();
            return;
        }
        client2 = this.zzkr.zzka;
        client2.getRemoteService(null, Collections.emptySet());
    }
}
