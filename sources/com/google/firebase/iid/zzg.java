package com.google.firebase.iid;

import android.util.Log;

/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: classes.dex */
public final class zzg implements Runnable {
    private final /* synthetic */ zzd zzx;
    private final /* synthetic */ zzf zzy;

    /* JADX INFO: Access modifiers changed from: package-private */
    public zzg(zzf zzfVar, zzd zzdVar) {
        this.zzy = zzfVar;
        this.zzx = zzdVar;
    }

    @Override // java.lang.Runnable
    public final void run() {
        zzb zzbVar;
        if (Log.isLoggable("EnhancedIntentService", 3)) {
            Log.d("EnhancedIntentService", "bg processing of the intent starting now");
        }
        zzbVar = this.zzy.zzw;
        zzbVar.zzd(this.zzx.intent);
        this.zzx.finish();
    }
}
