package com.treadly.client.lib.sdk.Model;

/* loaded from: classes2.dex */
public enum DistanceUnits {
    KM(0),
    MI(1);
    
    private int value;

    DistanceUnits(int i) {
        this.value = i;
    }

    public int value() {
        return this.value;
    }

    public static DistanceUnits fromValue(int i) {
        DistanceUnits[] values;
        for (DistanceUnits distanceUnits : values()) {
            if (distanceUnits.value == i) {
                return distanceUnits;
            }
        }
        return null;
    }
}
