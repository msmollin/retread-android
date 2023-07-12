package com.google.android.gms.internal.stable;

import android.database.ContentObserver;
import android.os.Handler;

/* loaded from: classes.dex */
final class zzf extends ContentObserver {
    private final /* synthetic */ zzh zzagr;

    /* JADX INFO: Access modifiers changed from: package-private */
    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public zzf(Handler handler, zzh zzhVar) {
        super(null);
        this.zzagr = zzhVar;
    }

    @Override // android.database.ContentObserver
    public final void onChange(boolean z) {
        this.zzagr.zzagu.set(true);
    }
}
