package com.treadly.client.lib.sdk.Model;

/* loaded from: classes2.dex */
public class TemperatureInfo {
    private boolean isValid;
    private TemperatureStatusCode statusCode;
    private float temperature;

    public TemperatureInfo(float f, TemperatureStatusCode temperatureStatusCode, boolean z) {
        this.temperature = f;
        this.statusCode = temperatureStatusCode;
        this.isValid = z;
    }

    public float getTemperature() {
        return this.temperature;
    }

    public TemperatureStatusCode getStatusCode() {
        return this.statusCode;
    }

    public boolean isValid() {
        return this.isValid;
    }
}
