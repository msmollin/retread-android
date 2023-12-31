package com.google.android.gms.common.util;

import android.os.SystemClock;

/* loaded from: classes.dex */
public class DefaultClock implements Clock {
    private static final DefaultClock zzzk = new DefaultClock();

    private DefaultClock() {
    }

    public static Clock getInstance() {
        return zzzk;
    }

    @Override // com.google.android.gms.common.util.Clock
    public long currentThreadTimeMillis() {
        return SystemClock.currentThreadTimeMillis();
    }

    @Override // com.google.android.gms.common.util.Clock
    public long currentTimeMillis() {
        return System.currentTimeMillis();
    }

    @Override // com.google.android.gms.common.util.Clock
    public long elapsedRealtime() {
        return SystemClock.elapsedRealtime();
    }

    @Override // com.google.android.gms.common.util.Clock
    public long nanoTime() {
        return System.nanoTime();
    }
}
