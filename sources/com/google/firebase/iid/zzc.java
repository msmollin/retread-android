package com.google.firebase.iid;

import android.content.Intent;

/* loaded from: classes.dex */
final class zzc implements Runnable {
    private final /* synthetic */ Intent zzp;
    private final /* synthetic */ Intent zzq;
    private final /* synthetic */ zzb zzr;

    /* JADX INFO: Access modifiers changed from: package-private */
    public zzc(zzb zzbVar, Intent intent, Intent intent2) {
        this.zzr = zzbVar;
        this.zzp = intent;
        this.zzq = intent2;
    }

    @Override // java.lang.Runnable
    public final void run() {
        this.zzr.zzd(this.zzp);
        this.zzr.zza(this.zzq);
    }
}
