package com.google.android.gms.internal.measurement;

import android.content.SharedPreferences;
import androidx.annotation.WorkerThread;
import com.google.android.gms.common.internal.Preconditions;

/* loaded from: classes.dex */
public final class zzft {
    private boolean value;
    private final boolean zzako;
    private boolean zzakp;
    private final /* synthetic */ zzfr zzakq;
    private final String zzny;

    public zzft(zzfr zzfrVar, String str, boolean z) {
        this.zzakq = zzfrVar;
        Preconditions.checkNotEmpty(str);
        this.zzny = str;
        this.zzako = true;
    }

    @WorkerThread
    public final boolean get() {
        SharedPreferences zziy;
        if (!this.zzakp) {
            this.zzakp = true;
            zziy = this.zzakq.zziy();
            this.value = zziy.getBoolean(this.zzny, this.zzako);
        }
        return this.value;
    }

    @WorkerThread
    public final void set(boolean z) {
        SharedPreferences zziy;
        zziy = this.zzakq.zziy();
        SharedPreferences.Editor edit = zziy.edit();
        edit.putBoolean(this.zzny, z);
        edit.apply();
        this.value = z;
    }
}
