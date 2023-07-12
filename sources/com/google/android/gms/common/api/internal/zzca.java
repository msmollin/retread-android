package com.google.android.gms.common.api.internal;

import com.google.android.gms.signin.internal.SignInResponse;

/* loaded from: classes.dex */
final class zzca implements Runnable {
    private final /* synthetic */ SignInResponse zzid;
    private final /* synthetic */ zzby zzlx;

    /* JADX INFO: Access modifiers changed from: package-private */
    public zzca(zzby zzbyVar, SignInResponse signInResponse) {
        this.zzlx = zzbyVar;
        this.zzid = signInResponse;
    }

    @Override // java.lang.Runnable
    public final void run() {
        this.zzlx.zzb(this.zzid);
    }
}
