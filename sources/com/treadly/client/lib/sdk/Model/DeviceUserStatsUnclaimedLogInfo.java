package com.treadly.client.lib.sdk.Model;

/* loaded from: classes2.dex */
public class DeviceUserStatsUnclaimedLogInfo extends DeviceUserStatsLogInfo {
    public int sequence;
    public int totalSequence;

    public DeviceUserStatsUnclaimedLogInfo(int i, int i2, int i3, int i4, int i5) {
        super(i, i4, i5);
        this.sequence = i2;
        this.totalSequence = i3;
    }
}
