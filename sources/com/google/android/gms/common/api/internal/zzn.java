package com.google.android.gms.common.api.internal;

import android.app.Dialog;
import com.google.android.gms.common.api.internal.GooglePlayServicesUpdatedReceiver;

/* loaded from: classes.dex */
final class zzn extends GooglePlayServicesUpdatedReceiver.Callback {
    private final /* synthetic */ Dialog zzex;
    private final /* synthetic */ zzm zzey;

    /* JADX INFO: Access modifiers changed from: package-private */
    public zzn(zzm zzmVar, Dialog dialog) {
        this.zzey = zzmVar;
        this.zzex = dialog;
    }

    @Override // com.google.android.gms.common.api.internal.GooglePlayServicesUpdatedReceiver.Callback
    public final void zzv() {
        this.zzey.zzew.zzt();
        if (this.zzex.isShowing()) {
            this.zzex.dismiss();
        }
    }
}
