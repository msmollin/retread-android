package com.google.android.gms.common.api.internal;

import androidx.annotation.WorkerThread;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.Result;
import com.google.android.gms.common.api.ResultTransform;
import java.lang.ref.WeakReference;

/* loaded from: classes.dex */
final class zzci implements Runnable {
    private final /* synthetic */ Result zzmk;
    private final /* synthetic */ zzch zzml;

    /* JADX INFO: Access modifiers changed from: package-private */
    public zzci(zzch zzchVar, Result result) {
        this.zzml = zzchVar;
        this.zzmk = result;
    }

    @Override // java.lang.Runnable
    @WorkerThread
    public final void run() {
        WeakReference weakReference;
        zzcj zzcjVar;
        zzcj zzcjVar2;
        WeakReference weakReference2;
        ResultTransform resultTransform;
        zzcj zzcjVar3;
        zzcj zzcjVar4;
        WeakReference weakReference3;
        try {
            try {
                BasePendingResult.zzez.set(true);
                resultTransform = this.zzml.zzmd;
                PendingResult onSuccess = resultTransform.onSuccess(this.zzmk);
                zzcjVar3 = this.zzml.zzmi;
                zzcjVar4 = this.zzml.zzmi;
                zzcjVar3.sendMessage(zzcjVar4.obtainMessage(0, onSuccess));
                BasePendingResult.zzez.set(false);
                zzch zzchVar = this.zzml;
                zzch.zzb(this.zzmk);
                weakReference3 = this.zzml.zzfc;
                GoogleApiClient googleApiClient = (GoogleApiClient) weakReference3.get();
                if (googleApiClient != null) {
                    googleApiClient.zzb(this.zzml);
                }
            } catch (RuntimeException e) {
                zzcjVar = this.zzml.zzmi;
                zzcjVar2 = this.zzml.zzmi;
                zzcjVar.sendMessage(zzcjVar2.obtainMessage(1, e));
                BasePendingResult.zzez.set(false);
                zzch zzchVar2 = this.zzml;
                zzch.zzb(this.zzmk);
                weakReference2 = this.zzml.zzfc;
                GoogleApiClient googleApiClient2 = (GoogleApiClient) weakReference2.get();
                if (googleApiClient2 != null) {
                    googleApiClient2.zzb(this.zzml);
                }
            }
        } catch (Throwable th) {
            BasePendingResult.zzez.set(false);
            zzch zzchVar3 = this.zzml;
            zzch.zzb(this.zzmk);
            weakReference = this.zzml.zzfc;
            GoogleApiClient googleApiClient3 = (GoogleApiClient) weakReference.get();
            if (googleApiClient3 != null) {
                googleApiClient3.zzb(this.zzml);
            }
            throw th;
        }
    }
}
