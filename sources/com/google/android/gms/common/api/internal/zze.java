package com.google.android.gms.common.api.internal;

import android.os.RemoteException;
import androidx.annotation.NonNull;
import com.google.android.gms.common.api.Api;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.common.api.internal.GoogleApiManager;
import com.google.android.gms.tasks.TaskCompletionSource;

/* loaded from: classes.dex */
public final class zze extends zzc<Void> {
    private final RegisterListenerMethod<Api.AnyClient, ?> zzdw;
    private final UnregisterListenerMethod<Api.AnyClient, ?> zzdx;

    public zze(zzbv zzbvVar, TaskCompletionSource<Void> taskCompletionSource) {
        super(3, taskCompletionSource);
        this.zzdw = zzbvVar.zzlt;
        this.zzdx = zzbvVar.zzlu;
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
        this.zzdw.registerListener(zzaVar.zzae(), this.zzdu);
        if (this.zzdw.getListenerKey() != null) {
            zzaVar.zzbn().put(this.zzdw.getListenerKey(), new zzbv(this.zzdw, this.zzdx));
        }
    }
}
