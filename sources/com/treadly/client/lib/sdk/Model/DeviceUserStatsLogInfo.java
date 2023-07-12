package com.treadly.client.lib.sdk.Model;

import java.util.Date;

/* loaded from: classes2.dex */
public class DeviceUserStatsLogInfo {
    public float finalDistance;
    public int finalSteps;
    public float initialDistance;
    public int initialSteps;
    public Date initialTimestamp;
    public boolean isUnclaimed;
    public int logId;
    public DeviceUserStatsLogSegmentInfo[] segments;
    public int version;

    public DeviceUserStatsLogInfo(int i, int i2, Date date, int i3, int i4, float f, float f2) {
        this.logId = i;
        this.version = i2;
        this.initialTimestamp = date;
        this.initialSteps = i3;
        this.finalSteps = i4;
        this.initialDistance = f;
        this.finalDistance = f2;
        this.segments = null;
    }

    public DeviceUserStatsLogInfo(int i, int i2, int i3) {
        this.logId = i;
        this.initialSteps = i2;
        this.finalSteps = i3;
        this.version = -1;
        this.initialTimestamp = null;
        this.initialDistance = -1.0f;
        this.finalDistance = -1.0f;
        this.segments = null;
        this.isUnclaimed = true;
    }
}
