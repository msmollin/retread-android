package com.google.android.gms.internal.measurement;

import android.content.SharedPreferences;
import androidx.annotation.WorkerThread;
import com.google.android.gms.common.internal.Preconditions;

/* loaded from: classes.dex */
public final class zzfu {
    private long value;
    private boolean zzakp;
    private final /* synthetic */ zzfr zzakq;
    private final long zzakr;
    private final String zzny;

    public zzfu(zzfr zzfrVar, String str, long j) {
        this.zzakq = zzfrVar;
        Preconditions.checkNotEmpty(str);
        this.zzny = str;
        this.zzakr = j;
    }

    @WorkerThread
    public final long get() {
        SharedPreferences zziy;
        if (!this.zzakp) {
            this.zzakp = true;
            zziy = this.zzakq.zziy();
            this.value = zziy.getLong(this.zzny, this.zzakr);
        }
        return this.value;
    }

    @WorkerThread
    public final void set(long j) {
        SharedPreferences zziy;
        zziy = this.zzakq.zziy();
        SharedPreferences.Editor edit = zziy.edit();
        edit.putLong(this.zzny, j);
        edit.apply();
        this.value = j;
    }
}
