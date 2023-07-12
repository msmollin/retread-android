package com.google.android.gms.common.api.internal;

import android.content.Context;
import androidx.annotation.NonNull;
import com.google.android.gms.auth.api.signin.internal.Storage;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;

/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: classes.dex */
public final class zzaz implements ResultCallback<Status> {
    private final /* synthetic */ zzav zzit;
    private final /* synthetic */ StatusPendingResult zziv;
    private final /* synthetic */ boolean zziw;
    private final /* synthetic */ GoogleApiClient zzix;

    /* JADX INFO: Access modifiers changed from: package-private */
    public zzaz(zzav zzavVar, StatusPendingResult statusPendingResult, boolean z, GoogleApiClient googleApiClient) {
        this.zzit = zzavVar;
        this.zziv = statusPendingResult;
        this.zziw = z;
        this.zzix = googleApiClient;
    }

    @Override // com.google.android.gms.common.api.ResultCallback
    public final /* synthetic */ void onResult(@NonNull Status status) {
        Context context;
        Status status2 = status;
        context = this.zzit.mContext;
        Storage.getInstance(context).removeSavedDefaultGoogleSignInAccount();
        if (status2.isSuccess() && this.zzit.isConnected()) {
            this.zzit.reconnect();
        }
        this.zziv.setResult(status2);
        if (this.zziw) {
            this.zzix.disconnect();
        }
    }
}
