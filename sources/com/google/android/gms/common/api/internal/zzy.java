package com.google.android.gms.common.api.internal;

import android.util.Log;
import androidx.annotation.NonNull;
import androidx.collection.ArrayMap;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.AvailabilityException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import java.util.Collections;
import java.util.Map;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;

/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: classes.dex */
public final class zzy implements OnCompleteListener<Map<zzh<?>, String>> {
    private final /* synthetic */ zzw zzgu;

    private zzy(zzw zzwVar) {
        this.zzgu = zzwVar;
    }

    @Override // com.google.android.gms.tasks.OnCompleteListener
    public final void onComplete(@NonNull Task<Map<zzh<?>, String>> task) {
        Lock lock;
        Lock lock2;
        boolean z;
        zzw zzwVar;
        ConnectionResult connectionResult;
        boolean z2;
        Map map;
        Map map2;
        boolean zza;
        Map map3;
        Map map4;
        ConnectionResult connectionResult2;
        zzav zzavVar;
        ConnectionResult connectionResult3;
        Condition condition;
        Map map5;
        Map map6;
        ConnectionResult zzai;
        Map map7;
        Map map8;
        Map map9;
        lock = this.zzgu.zzga;
        lock.lock();
        try {
            z = this.zzgu.zzgp;
            if (z) {
                if (task.isSuccessful()) {
                    zzw zzwVar2 = this.zzgu;
                    map7 = this.zzgu.zzgg;
                    zzwVar2.zzgq = new ArrayMap(map7.size());
                    map8 = this.zzgu.zzgg;
                    for (zzv zzvVar : map8.values()) {
                        map9 = this.zzgu.zzgq;
                        map9.put(zzvVar.zzm(), ConnectionResult.RESULT_SUCCESS);
                    }
                } else {
                    if (task.getException() instanceof AvailabilityException) {
                        AvailabilityException availabilityException = (AvailabilityException) task.getException();
                        z2 = this.zzgu.zzgn;
                        if (z2) {
                            zzw zzwVar3 = this.zzgu;
                            map = this.zzgu.zzgg;
                            zzwVar3.zzgq = new ArrayMap(map.size());
                            map2 = this.zzgu.zzgg;
                            for (zzv zzvVar2 : map2.values()) {
                                Object zzm = zzvVar2.zzm();
                                ConnectionResult connectionResult4 = availabilityException.getConnectionResult(zzvVar2);
                                zza = this.zzgu.zza(zzvVar2, connectionResult4);
                                if (zza) {
                                    map3 = this.zzgu.zzgq;
                                    connectionResult4 = new ConnectionResult(16);
                                } else {
                                    map3 = this.zzgu.zzgq;
                                }
                                map3.put(zzm, connectionResult4);
                            }
                        } else {
                            this.zzgu.zzgq = availabilityException.zzl();
                        }
                        zzwVar = this.zzgu;
                        connectionResult = this.zzgu.zzai();
                    } else {
                        Log.e("ConnectionlessGAC", "Unexpected availability exception", task.getException());
                        this.zzgu.zzgq = Collections.emptyMap();
                        zzwVar = this.zzgu;
                        connectionResult = new ConnectionResult(8);
                    }
                    zzwVar.zzgt = connectionResult;
                }
                map4 = this.zzgu.zzgr;
                if (map4 != null) {
                    map5 = this.zzgu.zzgq;
                    map6 = this.zzgu.zzgr;
                    map5.putAll(map6);
                    zzw zzwVar4 = this.zzgu;
                    zzai = this.zzgu.zzai();
                    zzwVar4.zzgt = zzai;
                }
                connectionResult2 = this.zzgu.zzgt;
                if (connectionResult2 == null) {
                    this.zzgu.zzag();
                    this.zzgu.zzah();
                } else {
                    zzw.zza(this.zzgu, false);
                    zzavVar = this.zzgu.zzgj;
                    connectionResult3 = this.zzgu.zzgt;
                    zzavVar.zzc(connectionResult3);
                }
                condition = this.zzgu.zzgl;
                condition.signalAll();
            }
        } finally {
            lock2 = this.zzgu.zzga;
            lock2.unlock();
        }
    }
}
