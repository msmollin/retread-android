package com.treadly.client.lib.sdk.Model;

/* loaded from: classes2.dex */
public class DeviceIrDebugEventInfo {
    public DeviceIrDebugAction action;
    public int[] adcValues;
    public int timestamp;

    public DeviceIrDebugEventInfo(DeviceIrDebugAction deviceIrDebugAction, int i, int[] iArr) {
        this.action = deviceIrDebugAction;
        this.timestamp = i;
        this.adcValues = iArr;
    }
}
