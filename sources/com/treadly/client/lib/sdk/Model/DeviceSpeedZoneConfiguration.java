package com.treadly.client.lib.sdk.Model;

/* loaded from: classes2.dex */
public class DeviceSpeedZoneConfiguration {
    private int accelerationZoneEnd;
    private int accelerationZoneStart;
    private int decelerationZoneEnd;
    private int decelerationZoneStart;

    public DeviceSpeedZoneConfiguration(int i, int i2, int i3, int i4) {
        this.accelerationZoneEnd = i;
        this.accelerationZoneStart = i2;
        this.decelerationZoneEnd = i3;
        this.decelerationZoneStart = i4;
    }

    public int getAccelerationZoneEnd() {
        return this.accelerationZoneEnd;
    }

    public int getAccelerationZoneStart() {
        return this.accelerationZoneStart;
    }

    public int getDecelerationZoneEnd() {
        return this.decelerationZoneEnd;
    }

    public int getDecelerationZoneStart() {
        return this.decelerationZoneStart;
    }
}
