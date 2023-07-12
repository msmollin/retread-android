package com.google.android.gms.common.api.internal;

import android.os.IBinder;
import android.os.RemoteException;
import com.google.android.gms.common.api.Api;
import com.google.android.gms.common.api.Result;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.common.api.internal.BaseImplementation;
import com.google.android.gms.common.util.VisibleForTesting;
import java.util.Collections;
import java.util.Map;
import java.util.Set;
import java.util.WeakHashMap;

/* loaded from: classes.dex */
public final class zzck {
    public static final Status zzmm = new Status(8, "The connection to Google Play services was lost");
    private static final BasePendingResult<?>[] zzmn = new BasePendingResult[0];
    private final Map<Api.AnyClientKey<?>, Api.Client> zzil;
    @VisibleForTesting
    final Set<BasePendingResult<?>> zzmo = Collections.synchronizedSet(Collections.newSetFromMap(new WeakHashMap()));
    private final zzcn zzmp = new zzcl(this);

    public zzck(Map<Api.AnyClientKey<?>, Api.Client> map) {
        this.zzil = map;
    }

    public final void release() {
        BasePendingResult[] basePendingResultArr;
        for (BasePendingResult basePendingResult : (BasePendingResult[]) this.zzmo.toArray(zzmn)) {
            com.google.android.gms.common.api.zzc zzcVar = null;
            basePendingResult.zza((zzcn) null);
            if (basePendingResult.zzo() != null) {
                basePendingResult.setResultCallback(null);
                IBinder serviceBrokerBinder = this.zzil.get(((BaseImplementation.ApiMethodImpl) basePendingResult).getClientKey()).getServiceBrokerBinder();
                if (basePendingResult.isReady()) {
                    basePendingResult.zza(new zzcm(basePendingResult, null, serviceBrokerBinder, null));
                } else {
                    if (serviceBrokerBinder == null || !serviceBrokerBinder.isBinderAlive()) {
                        basePendingResult.zza((zzcn) null);
                    } else {
                        zzcm zzcmVar = new zzcm(basePendingResult, null, serviceBrokerBinder, null);
                        basePendingResult.zza(zzcmVar);
                        try {
                            serviceBrokerBinder.linkToDeath(zzcmVar, 0);
                        } catch (RemoteException unused) {
                        }
                    }
                    basePendingResult.cancel();
                    zzcVar.remove(basePendingResult.zzo().intValue());
                }
            } else if (!basePendingResult.zzw()) {
            }
            this.zzmo.remove(basePendingResult);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public final void zzb(BasePendingResult<? extends Result> basePendingResult) {
        this.zzmo.add(basePendingResult);
        basePendingResult.zza(this.zzmp);
    }

    public final void zzce() {
        for (BasePendingResult basePendingResult : (BasePendingResult[]) this.zzmo.toArray(zzmn)) {
            basePendingResult.zzb(zzmm);
        }
    }
}
