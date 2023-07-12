package com.google.android.gms.common.api.internal;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;

/* loaded from: classes.dex */
public final class GooglePlayServicesUpdatedReceiver extends BroadcastReceiver {
    private Context mContext;
    private final Callback zzkt;

    /* loaded from: classes.dex */
    public static abstract class Callback {
        public abstract void zzv();
    }

    public GooglePlayServicesUpdatedReceiver(Callback callback) {
        this.zzkt = callback;
    }

    @Override // android.content.BroadcastReceiver
    public final void onReceive(Context context, Intent intent) {
        Uri data = intent.getData();
        if ("com.google.android.gms".equals(data != null ? data.getSchemeSpecificPart() : null)) {
            this.zzkt.zzv();
            unregister();
        }
    }

    public final synchronized void unregister() {
        if (this.mContext != null) {
            this.mContext.unregisterReceiver(this);
        }
        this.mContext = null;
    }

    public final void zzc(Context context) {
        this.mContext = context;
    }
}
