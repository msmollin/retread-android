package com.google.android.gms.common.api.internal;

import com.google.android.gms.common.ConnectionResult;

/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: classes.dex */
public final class zzbz implements Runnable {
    private final /* synthetic */ zzby zzlx;

    /* JADX INFO: Access modifiers changed from: package-private */
    public zzbz(zzby zzbyVar) {
        this.zzlx = zzbyVar;
    }

    @Override // java.lang.Runnable
    public final void run() {
        zzcb zzcbVar;
        zzcbVar = this.zzlx.zzlw;
        zzcbVar.zzg(new ConnectionResult(4));
    }
}
