package com.google.android.gms.internal.measurement;

import com.google.android.gms.measurement.AppMeasurement;

/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: classes.dex */
public final class zzhs implements Runnable {
    private final /* synthetic */ zzhk zzanw;
    private final /* synthetic */ AppMeasurement.ConditionalUserProperty zzaob;

    /* JADX INFO: Access modifiers changed from: package-private */
    public zzhs(zzhk zzhkVar, AppMeasurement.ConditionalUserProperty conditionalUserProperty) {
        this.zzanw = zzhkVar;
        this.zzaob = conditionalUserProperty;
    }

    @Override // java.lang.Runnable
    public final void run() {
        this.zzanw.zzc(this.zzaob);
    }
}
