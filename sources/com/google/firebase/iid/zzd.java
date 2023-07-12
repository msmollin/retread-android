package com.google.firebase.iid;

import android.content.BroadcastReceiver;
import android.content.Intent;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

/* loaded from: classes.dex */
final class zzd {
    final Intent intent;
    private final BroadcastReceiver.PendingResult zzs;
    private boolean zzt = false;
    private final ScheduledFuture<?> zzu;

    /* JADX INFO: Access modifiers changed from: package-private */
    public zzd(Intent intent, BroadcastReceiver.PendingResult pendingResult, ScheduledExecutorService scheduledExecutorService) {
        this.intent = intent;
        this.zzs = pendingResult;
        this.zzu = scheduledExecutorService.schedule(new zze(this, intent), 9500L, TimeUnit.MILLISECONDS);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public final synchronized void finish() {
        if (!this.zzt) {
            this.zzs.finish();
            this.zzu.cancel(false);
            this.zzt = true;
        }
    }
}
