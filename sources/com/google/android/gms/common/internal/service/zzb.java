package com.google.android.gms.common.internal.service;

import android.os.RemoteException;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.internal.service.CommonApiImpl;

/* loaded from: classes.dex */
final class zzb extends zzd {
    /* JADX INFO: Access modifiers changed from: package-private */
    public zzb(CommonApiImpl commonApiImpl, GoogleApiClient googleApiClient) {
        super(googleApiClient);
    }

    @Override // com.google.android.gms.common.api.internal.BaseImplementation.ApiMethodImpl
    protected final /* synthetic */ void doExecute(CommonClient commonClient) throws RemoteException {
        ((ICommonService) commonClient.getService()).clearDefaultAccount(new CommonApiImpl.zza(this));
    }
}
