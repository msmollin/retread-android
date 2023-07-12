package com.google.android.gms.common.api.internal;

import android.os.Looper;
import androidx.annotation.NonNull;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.Api;
import com.google.android.gms.common.internal.BaseGmsClient;
import com.google.android.gms.common.internal.Preconditions;
import java.lang.ref.WeakReference;
import java.util.concurrent.locks.Lock;

/* loaded from: classes.dex */
final class zzal implements BaseGmsClient.ConnectionProgressReportCallbacks {
    private final Api<?> mApi;
    private final boolean zzfo;
    private final WeakReference<zzaj> zzhw;

    public zzal(zzaj zzajVar, Api<?> api, boolean z) {
        this.zzhw = new WeakReference<>(zzajVar);
        this.mApi = api;
        this.zzfo = z;
    }

    @Override // com.google.android.gms.common.internal.BaseGmsClient.ConnectionProgressReportCallbacks
    public final void onReportServiceBinding(@NonNull ConnectionResult connectionResult) {
        zzbd zzbdVar;
        Lock lock;
        Lock lock2;
        boolean zze;
        boolean zzar;
        zzaj zzajVar = this.zzhw.get();
        if (zzajVar == null) {
            return;
        }
        Looper myLooper = Looper.myLooper();
        zzbdVar = zzajVar.zzhf;
        Preconditions.checkState(myLooper == zzbdVar.zzfq.getLooper(), "onReportServiceBinding must be called on the GoogleApiClient handler thread");
        lock = zzajVar.zzga;
        lock.lock();
        try {
            zze = zzajVar.zze(0);
            if (zze) {
                if (!connectionResult.isSuccess()) {
                    zzajVar.zzb(connectionResult, this.mApi, this.zzfo);
                }
                zzar = zzajVar.zzar();
                if (zzar) {
                    zzajVar.zzas();
                }
            }
        } finally {
            lock2 = zzajVar.zzga;
            lock2.unlock();
        }
    }
}
