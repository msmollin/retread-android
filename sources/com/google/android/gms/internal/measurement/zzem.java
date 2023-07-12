package com.google.android.gms.internal.measurement;

import android.os.Handler;
import com.google.android.gms.common.internal.Preconditions;

/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: classes.dex */
public abstract class zzem {
    private static volatile Handler handler;
    private final zzhi zzafj;
    private final Runnable zzyd;
    private volatile long zzye;

    /* JADX INFO: Access modifiers changed from: package-private */
    public zzem(zzhi zzhiVar) {
        Preconditions.checkNotNull(zzhiVar);
        this.zzafj = zzhiVar;
        this.zzyd = new zzen(this, zzhiVar);
    }

    private final Handler getHandler() {
        Handler handler2;
        if (handler != null) {
            return handler;
        }
        synchronized (zzem.class) {
            if (handler == null) {
                handler = new Handler(this.zzafj.getContext().getMainLooper());
            }
            handler2 = handler;
        }
        return handler2;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static /* synthetic */ long zza(zzem zzemVar, long j) {
        zzemVar.zzye = 0L;
        return 0L;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public final void cancel() {
        this.zzye = 0L;
        getHandler().removeCallbacks(this.zzyd);
    }

    public abstract void run();

    public final boolean zzef() {
        return this.zzye != 0;
    }

    public final void zzh(long j) {
        cancel();
        if (j >= 0) {
            this.zzye = this.zzafj.zzbt().currentTimeMillis();
            if (getHandler().postDelayed(this.zzyd, j)) {
                return;
            }
            this.zzafj.zzge().zzim().zzg("Failed to schedule delayed post. time", Long.valueOf(j));
        }
    }
}
