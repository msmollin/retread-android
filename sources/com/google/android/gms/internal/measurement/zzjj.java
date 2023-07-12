package com.google.android.gms.internal.measurement;

import androidx.annotation.WorkerThread;

/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: classes.dex */
public final class zzjj extends zzem {
    private final /* synthetic */ zzjh zzapx;

    /* JADX INFO: Access modifiers changed from: package-private */
    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public zzjj(zzjh zzjhVar, zzhi zzhiVar) {
        super(zzhiVar);
        this.zzapx = zzjhVar;
    }

    @Override // com.google.android.gms.internal.measurement.zzem
    @WorkerThread
    public final void run() {
        this.zzapx.zzkk();
    }
}
