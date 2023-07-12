package com.google.android.gms.common.api.internal;

import android.os.DeadObjectException;
import android.os.RemoteException;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.google.android.gms.common.Feature;
import com.google.android.gms.common.api.Api;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.common.api.internal.GoogleApiManager;
import com.google.android.gms.tasks.TaskCompletionSource;

/* loaded from: classes.dex */
public final class zzf<ResultT> extends zzb {
    private final TaskCompletionSource<ResultT> zzdu;
    private final TaskApiCall<Api.AnyClient, ResultT> zzdy;
    private final StatusExceptionMapper zzdz;

    public zzf(int i, TaskApiCall<Api.AnyClient, ResultT> taskApiCall, TaskCompletionSource<ResultT> taskCompletionSource, StatusExceptionMapper statusExceptionMapper) {
        super(i);
        this.zzdu = taskCompletionSource;
        this.zzdy = taskApiCall;
        this.zzdz = statusExceptionMapper;
    }

    @Nullable
    public final Feature[] getRequiredFeatures() {
        return this.zzdy.zzca();
    }

    public final boolean shouldAutoResolveMissingFeatures() {
        return this.zzdy.shouldAutoResolveMissingFeatures();
    }

    @Override // com.google.android.gms.common.api.internal.zzb
    public final void zza(@NonNull Status status) {
        this.zzdu.trySetException(this.zzdz.getException(status));
    }

    @Override // com.google.android.gms.common.api.internal.zzb
    public final void zza(GoogleApiManager.zza<?> zzaVar) throws DeadObjectException {
        Status zza;
        try {
            this.zzdy.doExecute(zzaVar.zzae(), this.zzdu);
        } catch (DeadObjectException e) {
            throw e;
        } catch (RemoteException e2) {
            zza = zzb.zza(e2);
            zza(zza);
        } catch (RuntimeException e3) {
            zza(e3);
        }
    }

    @Override // com.google.android.gms.common.api.internal.zzb
    public final void zza(@NonNull zzaa zzaaVar, boolean z) {
        zzaaVar.zza(this.zzdu, z);
    }

    @Override // com.google.android.gms.common.api.internal.zzb
    public final void zza(@NonNull RuntimeException runtimeException) {
        this.zzdu.trySetException(runtimeException);
    }
}
