package com.google.firebase.iid;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.util.Log;
import com.google.android.gms.common.util.VisibleForTesting;
import javax.annotation.Nullable;

@VisibleForTesting
/* loaded from: classes.dex */
final class zzar extends BroadcastReceiver {
    @Nullable
    private zzaq zzcy;

    public zzar(zzaq zzaqVar) {
        this.zzcy = zzaqVar;
    }

    @Override // android.content.BroadcastReceiver
    public final void onReceive(Context context, Intent intent) {
        if (this.zzcy != null && this.zzcy.zzaj()) {
            if (FirebaseInstanceId.zzj()) {
                Log.d("FirebaseInstanceId", "Connectivity changed. Starting background sync.");
            }
            FirebaseInstanceId.zza(this.zzcy, 0L);
            this.zzcy.getContext().unregisterReceiver(this);
            this.zzcy = null;
        }
    }

    public final void zzak() {
        if (FirebaseInstanceId.zzj()) {
            Log.d("FirebaseInstanceId", "Connectivity change received registered");
        }
        this.zzcy.getContext().registerReceiver(this, new IntentFilter("android.net.conn.CONNECTIVITY_CHANGE"));
    }
}
