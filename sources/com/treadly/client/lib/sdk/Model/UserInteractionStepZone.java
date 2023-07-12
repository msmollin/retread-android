package com.treadly.client.lib.sdk.Model;

/* loaded from: classes2.dex */
public enum UserInteractionStepZone {
    none(0),
    decelerationZone(1),
    constantZone(2),
    accelerationZone(3);
    
    private int value;

    UserInteractionStepZone(int i) {
        this.value = i;
    }

    public int value() {
        return this.value;
    }

    public static UserInteractionStepZone fromValue(int i) {
        UserInteractionStepZone[] values;
        for (UserInteractionStepZone userInteractionStepZone : values()) {
            if (userInteractionStepZone.value == i) {
                return userInteractionStepZone;
            }
        }
        return null;
    }
}
