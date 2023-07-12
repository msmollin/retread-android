package com.treadly.client.lib.sdk.Model;

/* loaded from: classes2.dex */
public enum TemperatureStatusCode {
    NO_ERROR(0),
    TEMP_STATUS_STOP(1),
    TEMP_STATUS_REDUCE_SPEED(2),
    TEMP_STATUS_UNKNOWN(3);
    
    private int value;

    TemperatureStatusCode(int i) {
        this.value = i;
    }

    public int value() {
        return this.value;
    }

    public static TemperatureStatusCode fromValue(int i) {
        TemperatureStatusCode[] values;
        for (TemperatureStatusCode temperatureStatusCode : values()) {
            if (temperatureStatusCode.value == i) {
                return temperatureStatusCode;
            }
        }
        return null;
    }
}
