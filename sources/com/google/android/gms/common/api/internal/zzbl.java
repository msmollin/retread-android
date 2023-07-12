package com.google.android.gms.common.api.internal;

import com.google.android.gms.common.api.internal.GoogleApiManager;
import com.google.android.gms.common.internal.BaseGmsClient;

/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: classes.dex */
public final class zzbl implements BaseGmsClient.SignOutCallbacks {
    final /* synthetic */ GoogleApiManager.zza zzkk;

    /* JADX INFO: Access modifiers changed from: package-private */
    public zzbl(GoogleApiManager.zza zzaVar) {
        this.zzkk = zzaVar;
    }

    @Override // com.google.android.gms.common.internal.BaseGmsClient.SignOutCallbacks
    public final void onSignOutComplete() {
        GoogleApiManager.this.handler.post(new zzbm(this));
    }
}
