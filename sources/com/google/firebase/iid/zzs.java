package com.google.firebase.iid;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import androidx.annotation.VisibleForTesting;
import com.google.android.gms.tasks.Task;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import javax.annotation.concurrent.GuardedBy;

/* loaded from: classes.dex */
public final class zzs {
    @GuardedBy("MessengerIpcClient.class")
    private static zzs zzbf;
    private final ScheduledExecutorService zzbg;
    @GuardedBy("this")
    private zzu zzbh = new zzu(this);
    @GuardedBy("this")
    private int zzbi = 1;
    private final Context zzz;

    @VisibleForTesting
    private zzs(Context context, ScheduledExecutorService scheduledExecutorService) {
        this.zzbg = scheduledExecutorService;
        this.zzz = context.getApplicationContext();
    }

    private final synchronized <T> Task<T> zza(zzab<T> zzabVar) {
        if (Log.isLoggable("MessengerIpcClient", 3)) {
            String valueOf = String.valueOf(zzabVar);
            StringBuilder sb = new StringBuilder(String.valueOf(valueOf).length() + 9);
            sb.append("Queueing ");
            sb.append(valueOf);
            Log.d("MessengerIpcClient", sb.toString());
        }
        if (!this.zzbh.zzb(zzabVar)) {
            this.zzbh = new zzu(this);
            this.zzbh.zzb(zzabVar);
        }
        return zzabVar.zzbs.getTask();
    }

    public static synchronized zzs zzc(Context context) {
        zzs zzsVar;
        synchronized (zzs.class) {
            if (zzbf == null) {
                zzbf = new zzs(context, Executors.newSingleThreadScheduledExecutor());
            }
            zzsVar = zzbf;
        }
        return zzsVar;
    }

    private final synchronized int zzs() {
        int i;
        i = this.zzbi;
        this.zzbi = i + 1;
        return i;
    }

    public final Task<Void> zza(int i, Bundle bundle) {
        return zza(new zzaa(zzs(), 2, bundle));
    }

    public final Task<Bundle> zzb(int i, Bundle bundle) {
        return zza(new zzad(zzs(), 1, bundle));
    }
}
