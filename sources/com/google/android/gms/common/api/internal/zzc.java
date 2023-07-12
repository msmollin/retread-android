package com.google.android.gms.common.api.internal;

import android.os.DeadObjectException;
import android.os.RemoteException;
import androidx.annotation.NonNull;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.common.api.internal.GoogleApiManager;
import com.google.android.gms.tasks.TaskCompletionSource;

/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: classes.dex */
public abstract class zzc<T> extends zzb {
    protected final TaskCompletionSource<T> zzdu;

    public zzc(int i, TaskCompletionSource<T> taskCompletionSource) {
        super(i);
        this.zzdu = taskCompletionSource;
    }

    @Override // com.google.android.gms.common.api.internal.zzb
    public void zza(@NonNull Status status) {
        this.zzdu.trySetException(new ApiException(status));
    }

    @Override // com.google.android.gms.common.api.internal.zzb
    public final void zza(GoogleApiManager.zza<?> zzaVar) throws DeadObjectException {
        Status zza;
        Status zza2;
        try {
            zzb(zzaVar);
        } catch (DeadObjectException e) {
            zza2 = zzb.zza(e);
            zza(zza2);
            throw e;
        } catch (RemoteException e2) {
            zza = zzb.zza(e2);
            zza(zza);
        } catch (RuntimeException e3) {
            zza(e3);
        }
    }

    @Override // com.google.android.gms.common.api.internal.zzb
    public void zza(@NonNull zzaa zzaaVar, boolean z) {
    }

    @Override // com.google.android.gms.common.api.internal.zzb
    public void zza(@NonNull RuntimeException runtimeException) {
        this.zzdu.trySetException(runtimeException);
    }

    protected abstract void zzb(GoogleApiManager.zza<?> zzaVar) throws RemoteException;
}
