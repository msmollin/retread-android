package com.google.android.gms.stats;

import com.facebook.common.time.Clock;
import com.google.android.gms.stats.WakeLock;

/* loaded from: classes.dex */
final class zza implements WakeLock.Configuration {
    @Override // com.google.android.gms.stats.WakeLock.Configuration
    public final long getMaximumTimeout(String str, String str2) {
        return Clock.MAX_TIME;
    }

    @Override // com.google.android.gms.stats.WakeLock.Configuration
    public final boolean isWorkChainsEnabled() {
        return false;
    }
}
