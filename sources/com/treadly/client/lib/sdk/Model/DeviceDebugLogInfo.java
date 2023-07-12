package com.treadly.client.lib.sdk.Model;

/* loaded from: classes2.dex */
public class DeviceDebugLogInfo {
    public int count;
    public DeviceDebugLogEvent[] events;
    public int version;

    public DeviceDebugLogInfo(int i, int i2, DeviceDebugLogEvent[] deviceDebugLogEventArr) {
        this.version = i;
        this.count = i2;
        this.events = deviceDebugLogEventArr;
    }
}
