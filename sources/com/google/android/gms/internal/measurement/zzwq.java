package com.google.android.gms.internal.measurement;

import android.database.ContentObserver;
import android.os.Handler;

/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: classes.dex */
public final class zzwq extends ContentObserver {
    private final /* synthetic */ zzwp zzbnb;

    /* JADX INFO: Access modifiers changed from: package-private */
    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public zzwq(zzwp zzwpVar, Handler handler) {
        super(null);
        this.zzbnb = zzwpVar;
    }

    @Override // android.database.ContentObserver
    public final void onChange(boolean z) {
        this.zzbnb.zzru();
        this.zzbnb.zzrw();
    }
}
