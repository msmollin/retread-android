package com.google.android.gms.common.api.internal;

import com.google.android.gms.common.api.internal.GooglePlayServicesUpdatedReceiver;
import java.lang.ref.WeakReference;

/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: classes.dex */
public final class zzbb extends GooglePlayServicesUpdatedReceiver.Callback {
    private WeakReference<zzav> zziy;

    /* JADX INFO: Access modifiers changed from: package-private */
    public zzbb(zzav zzavVar) {
        this.zziy = new WeakReference<>(zzavVar);
    }

    @Override // com.google.android.gms.common.api.internal.GooglePlayServicesUpdatedReceiver.Callback
    public final void zzv() {
        zzav zzavVar = this.zziy.get();
        if (zzavVar == null) {
            return;
        }
        zzavVar.resume();
    }
}
