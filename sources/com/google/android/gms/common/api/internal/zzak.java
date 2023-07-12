package com.google.android.gms.common.api.internal;

import android.content.Context;
import com.google.android.gms.common.GoogleApiAvailabilityLight;

/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: classes.dex */
public final class zzak implements Runnable {
    private final /* synthetic */ zzaj zzhv;

    /* JADX INFO: Access modifiers changed from: package-private */
    public zzak(zzaj zzajVar) {
        this.zzhv = zzajVar;
    }

    @Override // java.lang.Runnable
    public final void run() {
        GoogleApiAvailabilityLight googleApiAvailabilityLight;
        Context context;
        googleApiAvailabilityLight = this.zzhv.zzgk;
        context = this.zzhv.mContext;
        googleApiAvailabilityLight.cancelAvailabilityErrorNotifications(context);
    }
}
