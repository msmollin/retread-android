package com.google.android.gms.common.api.internal;

import com.google.android.gms.common.api.Api;

/* loaded from: classes.dex */
final class zzbm implements Runnable {
    private final /* synthetic */ zzbl zzkm;

    /* JADX INFO: Access modifiers changed from: package-private */
    public zzbm(zzbl zzblVar) {
        this.zzkm = zzblVar;
    }

    @Override // java.lang.Runnable
    public final void run() {
        Api.Client client;
        client = this.zzkm.zzkk.zzka;
        client.disconnect();
    }
}
