package com.google.android.gms.common.api.internal;

import com.google.android.gms.common.api.internal.BackgroundDetector;

/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: classes.dex */
public final class zzbh implements BackgroundDetector.BackgroundStateChangeListener {
    private final /* synthetic */ GoogleApiManager zzjy;

    /* JADX INFO: Access modifiers changed from: package-private */
    public zzbh(GoogleApiManager googleApiManager) {
        this.zzjy = googleApiManager;
    }

    @Override // com.google.android.gms.common.api.internal.BackgroundDetector.BackgroundStateChangeListener
    public final void onBackgroundStateChanged(boolean z) {
        this.zzjy.handler.sendMessage(this.zzjy.handler.obtainMessage(1, Boolean.valueOf(z)));
    }
}
