package com.treadly.client.lib.sdk.Model;

import java.util.Date;

/* loaded from: classes2.dex */
public class DeviceUserStatsLogSegmentInfo {
    public float speed;
    public Date timestamp;

    public DeviceUserStatsLogSegmentInfo(Date date, float f) {
        this.timestamp = date;
        this.speed = f;
    }
}
