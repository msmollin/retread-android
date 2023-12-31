package com.google.android.gms.common.stats;

import android.os.Bundle;

/* loaded from: classes.dex */
public class StatisticalEventTrackerProvider {
    private static StatisticalEventTracker zzyp;

    /* loaded from: classes.dex */
    public interface StatisticalEventTracker {
        int getLogLevel(int i);

        Bundle getOptions();

        boolean isEnabled();

        void registerEvent(ConnectionEvent connectionEvent);

        void registerEvent(StatsEvent statsEvent);

        void registerEvent(WakeLockEvent wakeLockEvent);
    }

    private StatisticalEventTrackerProvider() {
    }

    public static StatisticalEventTracker getImpl() {
        return zzyp;
    }

    public static void setImpl(StatisticalEventTracker statisticalEventTracker) {
        zzyp = statisticalEventTracker;
    }
}
