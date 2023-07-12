package com.google.android.gms.common.internal.service;

import android.os.RemoteException;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.common.api.internal.BaseImplementation;

/* loaded from: classes.dex */
public final class CommonApiImpl implements CommonApi {

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes.dex */
    public static class zza extends BaseCommonServiceCallbacks {
        private final BaseImplementation.ResultHolder<Status> mResultHolder;

        public zza(BaseImplementation.ResultHolder<Status> resultHolder) {
            this.mResultHolder = resultHolder;
        }

        @Override // com.google.android.gms.common.internal.service.BaseCommonServiceCallbacks, com.google.android.gms.common.internal.service.ICommonCallbacks
        public final void onDefaultAccountCleared(int i) throws RemoteException {
            this.mResultHolder.setResult(new Status(i));
        }
    }

    @Override // com.google.android.gms.common.internal.service.CommonApi
    public final PendingResult<Status> clearDefaultAccount(GoogleApiClient googleApiClient) {
        return googleApiClient.execute(new zzb(this, googleApiClient));
    }
}
