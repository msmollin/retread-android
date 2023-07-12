package com.google.android.gms.common.api.internal;

import com.google.android.gms.common.api.internal.GoogleApiManager;

/* loaded from: classes.dex */
final class zzbi implements Runnable {
    private final /* synthetic */ GoogleApiManager.zza zzkk;

    /* JADX INFO: Access modifiers changed from: package-private */
    public zzbi(GoogleApiManager.zza zzaVar) {
        this.zzkk = zzaVar;
    }

    @Override // java.lang.Runnable
    public final void run() {
        this.zzkk.zzbj();
    }
}
