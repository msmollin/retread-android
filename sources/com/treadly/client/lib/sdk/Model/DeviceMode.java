package com.treadly.client.lib.sdk.Model;

/* loaded from: classes2.dex */
public enum DeviceMode {
    AWAKE(0),
    ACTIVE(1),
    IDLE(2),
    UNKNOWN(3);
    
    private int value;

    DeviceMode(int i) {
        this.value = i;
    }

    public int value() {
        return this.value;
    }

    public static DeviceMode fromValue(int i) {
        DeviceMode[] values;
        for (DeviceMode deviceMode : values()) {
            if (deviceMode.value == i) {
                return deviceMode;
            }
        }
        return null;
    }
}
