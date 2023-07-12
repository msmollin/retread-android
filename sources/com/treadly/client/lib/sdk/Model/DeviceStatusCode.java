package com.treadly.client.lib.sdk.Model;

/* loaded from: classes2.dex */
public enum DeviceStatusCode {
    NO_ERROR(0),
    HIGH_TEMPERATURE(1),
    WIFI_SCANNING(2),
    POWER_CYCLE_REQUIRED(3),
    REQUEST_ERROR(4),
    WIFI_NOT_CONNECTED_ERROR(5),
    WIFI_ERROR(6);
    
    private int value;

    DeviceStatusCode(int i) {
        this.value = i;
    }

    public int value() {
        return this.value;
    }

    public static DeviceStatusCode fromValue(int i) {
        DeviceStatusCode[] values;
        for (DeviceStatusCode deviceStatusCode : values()) {
            if (deviceStatusCode.value == i) {
                return deviceStatusCode;
            }
        }
        return null;
    }
}
