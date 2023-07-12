package com.google.android.gms.common.api.internal;

import com.google.android.gms.common.api.internal.GoogleApiManager;

/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: classes.dex */
public final class zzbj implements Runnable {
    private final /* synthetic */ GoogleApiManager.zza zzkk;

    /* JADX INFO: Access modifiers changed from: package-private */
    public zzbj(GoogleApiManager.zza zzaVar) {
        this.zzkk = zzaVar;
    }

    @Override // java.lang.Runnable
    public final void run() {
        this.zzkk.zzbk();
    }
}
