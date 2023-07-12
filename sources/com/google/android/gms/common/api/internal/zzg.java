package com.google.android.gms.common.api.internal;

import android.os.RemoteException;
import androidx.annotation.NonNull;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.common.api.internal.GoogleApiManager;
import com.google.android.gms.common.api.internal.ListenerHolder;
import com.google.android.gms.tasks.TaskCompletionSource;

/* loaded from: classes.dex */
public final class zzg extends zzc<Boolean> {
    private final ListenerHolder.ListenerKey<?> zzea;

    public zzg(ListenerHolder.ListenerKey<?> listenerKey, TaskCompletionSource<Boolean> taskCompletionSource) {
        super(4, taskCompletionSource);
        this.zzea = listenerKey;
    }

    @Override // com.google.android.gms.common.api.internal.zzc, com.google.android.gms.common.api.internal.zzb
    public final /* bridge */ /* synthetic */ void zza(@NonNull Status status) {
        super.zza(status);
    }

    @Override // com.google.android.gms.common.api.internal.zzc, com.google.android.gms.common.api.internal.zzb
    public final /* bridge */ /* synthetic */ void zza(@NonNull zzaa zzaaVar, boolean z) {
    }

    @Override // com.google.android.gms.common.api.internal.zzc, com.google.android.gms.common.api.internal.zzb
    public final /* bridge */ /* synthetic */ void zza(@NonNull RuntimeException runtimeException) {
        super.zza(runtimeException);
    }

    @Override // com.google.android.gms.common.api.internal.zzc
    public final void zzb(GoogleApiManager.zza<?> zzaVar) throws RemoteException {
        zzbv remove = zzaVar.zzbn().remove(this.zzea);
        if (remove == null) {
            this.zzdu.trySetResult(false);
            return;
        }
        remove.zzlu.unregisterListener(zzaVar.zzae(), this.zzdu);
        remove.zzlt.clearListener();
    }
}
