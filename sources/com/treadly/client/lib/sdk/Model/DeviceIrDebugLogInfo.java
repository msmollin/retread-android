package com.treadly.client.lib.sdk.Model;

/* loaded from: classes2.dex */
public class DeviceIrDebugLogInfo {
    public String customboardVersion;
    public DeviceIrDebugEventInfo[] irEvents;

    public DeviceIrDebugLogInfo(String str, DeviceIrDebugEventInfo[] deviceIrDebugEventInfoArr) {
        this.customboardVersion = str;
        this.irEvents = deviceIrDebugEventInfoArr;
    }
}
