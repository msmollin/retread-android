package com.google.android.gms.internal.measurement;

import android.os.Bundle;
import androidx.annotation.WorkerThread;

/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: classes.dex */
public final class zzji extends zzem {
    private final /* synthetic */ zzjh zzapx;

    /* JADX INFO: Access modifiers changed from: package-private */
    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public zzji(zzjh zzjhVar, zzhi zzhiVar) {
        super(zzhiVar);
        this.zzapx = zzjhVar;
    }

    @Override // com.google.android.gms.internal.measurement.zzem
    @WorkerThread
    public final void run() {
        zzjh zzjhVar = this.zzapx;
        zzjhVar.zzab();
        zzjhVar.zzge().zzit().zzg("Session started, time", Long.valueOf(zzjhVar.zzbt().elapsedRealtime()));
        zzjhVar.zzgf().zzakk.set(false);
        zzjhVar.zzfu().zza("auto", "_s", new Bundle());
        zzjhVar.zzgf().zzakl.set(zzjhVar.zzbt().currentTimeMillis());
    }
}
