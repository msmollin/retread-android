package com.google.android.gms.internal.measurement;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import androidx.annotation.MainThread;
import androidx.annotation.WorkerThread;
import com.google.android.gms.common.internal.Preconditions;
import com.google.android.gms.common.util.VisibleForTesting;

/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: classes.dex */
public class zzfp extends BroadcastReceiver {
    @VisibleForTesting
    private static final String zzaaw = "com.google.android.gms.internal.measurement.zzfp";
    private boolean zzaax;
    private boolean zzaay;
    private final zzjr zzajp;

    /* JADX INFO: Access modifiers changed from: package-private */
    public zzfp(zzjr zzjrVar) {
        Preconditions.checkNotNull(zzjrVar);
        this.zzajp = zzjrVar;
    }

    @Override // android.content.BroadcastReceiver
    @MainThread
    public void onReceive(Context context, Intent intent) {
        this.zzajp.zzkq();
        String action = intent.getAction();
        this.zzajp.zzge().zzit().zzg("NetworkBroadcastReceiver received action", action);
        if (!"android.net.conn.CONNECTIVITY_CHANGE".equals(action)) {
            this.zzajp.zzge().zzip().zzg("NetworkBroadcastReceiver received unknown action", action);
            return;
        }
        boolean zzex = this.zzajp.zzkn().zzex();
        if (this.zzaay != zzex) {
            this.zzaay = zzex;
            this.zzajp.zzgd().zzc(new zzfq(this, zzex));
        }
    }

    @WorkerThread
    public final void unregister() {
        this.zzajp.zzkq();
        this.zzajp.zzab();
        this.zzajp.zzab();
        if (this.zzaax) {
            this.zzajp.zzge().zzit().log("Unregistering connectivity change receiver");
            this.zzaax = false;
            this.zzaay = false;
            try {
                this.zzajp.getContext().unregisterReceiver(this);
            } catch (IllegalArgumentException e) {
                this.zzajp.zzge().zzim().zzg("Failed to unregister the network broadcast receiver", e);
            }
        }
    }

    @WorkerThread
    public final void zzeu() {
        this.zzajp.zzkq();
        this.zzajp.zzab();
        if (this.zzaax) {
            return;
        }
        this.zzajp.getContext().registerReceiver(this, new IntentFilter("android.net.conn.CONNECTIVITY_CHANGE"));
        this.zzaay = this.zzajp.zzkn().zzex();
        this.zzajp.zzge().zzit().zzg("Registering connectivity change receiver. Network connected", Boolean.valueOf(this.zzaay));
        this.zzaax = true;
    }
}
