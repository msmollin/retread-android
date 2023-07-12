package com.google.android.gms.common.api.internal;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.google.android.gms.common.ConnectionResult;
import java.util.concurrent.locks.Lock;

/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: classes.dex */
public final class zzt implements zzbq {
    private final /* synthetic */ zzr zzgc;

    private zzt(zzr zzrVar) {
        this.zzgc = zzrVar;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public /* synthetic */ zzt(zzr zzrVar, zzs zzsVar) {
        this(zzrVar);
    }

    @Override // com.google.android.gms.common.api.internal.zzbq
    public final void zzb(int i, boolean z) {
        Lock lock;
        Lock lock2;
        boolean z2;
        ConnectionResult connectionResult;
        ConnectionResult connectionResult2;
        zzbd zzbdVar;
        lock = this.zzgc.zzga;
        lock.lock();
        try {
            z2 = this.zzgc.zzfz;
            if (!z2) {
                connectionResult = this.zzgc.zzfy;
                if (connectionResult != null) {
                    connectionResult2 = this.zzgc.zzfy;
                    if (connectionResult2.isSuccess()) {
                        this.zzgc.zzfz = true;
                        zzbdVar = this.zzgc.zzfs;
                        zzbdVar.onConnectionSuspended(i);
                    }
                }
            }
            this.zzgc.zzfz = false;
            this.zzgc.zza(i, z);
        } finally {
            lock2 = this.zzgc.zzga;
            lock2.unlock();
        }
    }

    @Override // com.google.android.gms.common.api.internal.zzbq
    public final void zzb(@Nullable Bundle bundle) {
        Lock lock;
        Lock lock2;
        lock = this.zzgc.zzga;
        lock.lock();
        try {
            this.zzgc.zza(bundle);
            this.zzgc.zzfx = ConnectionResult.RESULT_SUCCESS;
            this.zzgc.zzaa();
        } finally {
            lock2 = this.zzgc.zzga;
            lock2.unlock();
        }
    }

    @Override // com.google.android.gms.common.api.internal.zzbq
    public final void zzc(@NonNull ConnectionResult connectionResult) {
        Lock lock;
        Lock lock2;
        lock = this.zzgc.zzga;
        lock.lock();
        try {
            this.zzgc.zzfx = connectionResult;
            this.zzgc.zzaa();
        } finally {
            lock2 = this.zzgc.zzga;
            lock2.unlock();
        }
    }
}
