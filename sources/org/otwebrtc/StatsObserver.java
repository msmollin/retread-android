package org.otwebrtc;

/* loaded from: classes2.dex */
public interface StatsObserver {
    @CalledByNative
    void onComplete(StatsReport[] statsReportArr);
}
