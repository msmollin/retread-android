package com.google.android.gms.internal.measurement;

import android.content.SharedPreferences;
import android.util.Pair;
import androidx.annotation.WorkerThread;
import com.facebook.common.time.Clock;
import com.google.android.gms.common.internal.Preconditions;
import com.google.android.gms.common.util.VisibleForTesting;

/* loaded from: classes.dex */
public final class zzfv {
    private final long zzabj;
    private final /* synthetic */ zzfr zzakq;
    @VisibleForTesting
    private final String zzaks;
    private final String zzakt;
    private final String zzaku;

    private zzfv(zzfr zzfrVar, String str, long j) {
        this.zzakq = zzfrVar;
        Preconditions.checkNotEmpty(str);
        Preconditions.checkArgument(j > 0);
        this.zzaks = String.valueOf(str).concat(":start");
        this.zzakt = String.valueOf(str).concat(":count");
        this.zzaku = String.valueOf(str).concat(":value");
        this.zzabj = j;
    }

    @WorkerThread
    private final void zzfh() {
        SharedPreferences zziy;
        this.zzakq.zzab();
        long currentTimeMillis = this.zzakq.zzbt().currentTimeMillis();
        zziy = this.zzakq.zziy();
        SharedPreferences.Editor edit = zziy.edit();
        edit.remove(this.zzakt);
        edit.remove(this.zzaku);
        edit.putLong(this.zzaks, currentTimeMillis);
        edit.apply();
    }

    @WorkerThread
    private final long zzfj() {
        SharedPreferences zziy;
        zziy = this.zzakq.zziy();
        return zziy.getLong(this.zzaks, 0L);
    }

    @WorkerThread
    public final void zzc(String str, long j) {
        SharedPreferences zziy;
        SharedPreferences zziy2;
        SharedPreferences zziy3;
        this.zzakq.zzab();
        if (zzfj() == 0) {
            zzfh();
        }
        if (str == null) {
            str = "";
        }
        zziy = this.zzakq.zziy();
        long j2 = zziy.getLong(this.zzakt, 0L);
        if (j2 <= 0) {
            zziy3 = this.zzakq.zziy();
            SharedPreferences.Editor edit = zziy3.edit();
            edit.putString(this.zzaku, str);
            edit.putLong(this.zzakt, 1L);
            edit.apply();
            return;
        }
        long j3 = j2 + 1;
        boolean z = (this.zzakq.zzgb().zzlc().nextLong() & Clock.MAX_TIME) < Clock.MAX_TIME / j3;
        zziy2 = this.zzakq.zziy();
        SharedPreferences.Editor edit2 = zziy2.edit();
        if (z) {
            edit2.putString(this.zzaku, str);
        }
        edit2.putLong(this.zzakt, j3);
        edit2.apply();
    }

    @WorkerThread
    public final Pair<String, Long> zzfi() {
        long abs;
        SharedPreferences zziy;
        SharedPreferences zziy2;
        this.zzakq.zzab();
        this.zzakq.zzab();
        long zzfj = zzfj();
        if (zzfj == 0) {
            zzfh();
            abs = 0;
        } else {
            abs = Math.abs(zzfj - this.zzakq.zzbt().currentTimeMillis());
        }
        if (abs < this.zzabj) {
            return null;
        }
        if (abs > (this.zzabj << 1)) {
            zzfh();
            return null;
        }
        zziy = this.zzakq.zziy();
        String string = zziy.getString(this.zzaku, null);
        zziy2 = this.zzakq.zziy();
        long j = zziy2.getLong(this.zzakt, 0L);
        zzfh();
        return (string == null || j <= 0) ? zzfr.zzajs : new Pair<>(string, Long.valueOf(j));
    }
}
