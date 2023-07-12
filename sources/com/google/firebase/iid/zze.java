package com.google.firebase.iid;

import android.content.Intent;
import android.util.Log;

/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: classes.dex */
public final class zze implements Runnable {
    private final /* synthetic */ Intent zzp;
    private final /* synthetic */ zzd zzv;

    /* JADX INFO: Access modifiers changed from: package-private */
    public zze(zzd zzdVar, Intent intent) {
        this.zzv = zzdVar;
        this.zzp = intent;
    }

    @Override // java.lang.Runnable
    public final void run() {
        String action = this.zzp.getAction();
        StringBuilder sb = new StringBuilder(String.valueOf(action).length() + 61);
        sb.append("Service took too long to process intent: ");
        sb.append(action);
        sb.append(" App may get closed.");
        Log.w("EnhancedIntentService", sb.toString());
        this.zzv.finish();
    }
}
