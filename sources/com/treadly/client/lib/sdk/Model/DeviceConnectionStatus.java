package com.treadly.client.lib.sdk.Model;

/* loaded from: classes2.dex */
public enum DeviceConnectionStatus {
    unknown(0),
    connected(1),
    notConnected(2),
    connecting(3),
    disconnecting(4);
    
    private int value;

    DeviceConnectionStatus(int i) {
        this.value = i;
    }

    public int value() {
        return this.value;
    }

    public static DeviceConnectionStatus fromValue(int i) {
        DeviceConnectionStatus[] values;
        for (DeviceConnectionStatus deviceConnectionStatus : values()) {
            if (deviceConnectionStatus.value == i) {
                return deviceConnectionStatus;
            }
        }
        return null;
    }
}
