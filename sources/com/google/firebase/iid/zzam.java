package com.google.firebase.iid;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;

/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: classes.dex */
public final class zzam extends Handler {
    private final /* synthetic */ zzal zzck;

    /* JADX INFO: Access modifiers changed from: package-private */
    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public zzam(zzal zzalVar, Looper looper) {
        super(looper);
        this.zzck = zzalVar;
    }

    @Override // android.os.Handler
    public final void handleMessage(Message message) {
        this.zzck.zzb(message);
    }
}
