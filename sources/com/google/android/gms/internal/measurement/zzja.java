package com.google.android.gms.internal.measurement;

import android.content.ComponentName;

/* loaded from: classes.dex */
final class zzja implements Runnable {
    private final /* synthetic */ zziw zzapn;

    /* JADX INFO: Access modifiers changed from: package-private */
    public zzja(zziw zziwVar) {
        this.zzapn = zziwVar;
    }

    @Override // java.lang.Runnable
    public final void run() {
        this.zzapn.zzape.onServiceDisconnected(new ComponentName(this.zzapn.zzape.getContext(), "com.google.android.gms.measurement.AppMeasurementService"));
    }
}
