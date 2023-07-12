package com.google.android.gms.internal.measurement;

import android.content.SharedPreferences;
import androidx.annotation.WorkerThread;
import com.google.android.gms.common.internal.Preconditions;

/* loaded from: classes.dex */
public final class zzfw {
    private String value;
    private boolean zzakp;
    private final /* synthetic */ zzfr zzakq;
    private final String zzakv;
    private final String zzny;

    public zzfw(zzfr zzfrVar, String str, String str2) {
        this.zzakq = zzfrVar;
        Preconditions.checkNotEmpty(str);
        this.zzny = str;
        this.zzakv = null;
    }

    @WorkerThread
    public final void zzbs(String str) {
        SharedPreferences zziy;
        if (zzka.zzs(str, this.value)) {
            return;
        }
        zziy = this.zzakq.zziy();
        SharedPreferences.Editor edit = zziy.edit();
        edit.putString(this.zzny, str);
        edit.apply();
        this.value = str;
    }

    @WorkerThread
    public final String zzjg() {
        SharedPreferences zziy;
        if (!this.zzakp) {
            this.zzakp = true;
            zziy = this.zzakq.zziy();
            this.value = zziy.getString(this.zzny, null);
        }
        return this.value;
    }
}
