package com.treadly.client.lib.sdk.Model;

/* loaded from: classes2.dex */
public class UserInteractionStatus {
    public int accelerationZoneEnd;
    public int accelerationZoneStart;
    public int constantZoneEnd;
    public int constantZoneStart;
    public int decelerationZoneEnd;
    public int decelerationZoneStart;
    public boolean enabled;
    public int maxZonePosition;
    public int minZonePosition;

    public UserInteractionStatus(boolean z, int i, int i2, int i3, int i4) {
        this.enabled = z;
        this.constantZoneStart = i;
        this.constantZoneEnd = i2;
        this.maxZonePosition = i3;
        this.minZonePosition = i4;
        this.accelerationZoneEnd = i3;
        this.accelerationZoneStart = i2;
        this.decelerationZoneEnd = i;
        this.decelerationZoneStart = i4;
    }
}
