package com.treadly.client.lib.sdk.Model;

/* loaded from: classes2.dex */
public class SpeedInfo {
    private float maximumSpeed;
    private float minimumSpeed;
    private float targetSpeed;
    private DistanceUnits units;

    public SpeedInfo(float f, float f2, float f3, DistanceUnits distanceUnits) {
        this.targetSpeed = f;
        this.maximumSpeed = f2;
        this.minimumSpeed = f3;
        this.units = distanceUnits;
    }

    public float getTargetSpeed() {
        return this.targetSpeed;
    }

    public float getMaximumSpeed() {
        return this.maximumSpeed;
    }

    public float getMinimumSpeed() {
        return this.minimumSpeed;
    }

    public DistanceUnits getUnits() {
        return this.units;
    }
}
