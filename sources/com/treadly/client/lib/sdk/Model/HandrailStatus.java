package com.treadly.client.lib.sdk.Model;

/* loaded from: classes2.dex */
public enum HandrailStatus {
    UNKNOWN(0),
    DOWN(1),
    UP(2);
    
    private int value;

    HandrailStatus(int i) {
        this.value = i;
    }

    public int value() {
        return this.value;
    }

    public static HandrailStatus fromValue(int i) {
        HandrailStatus[] values;
        for (HandrailStatus handrailStatus : values()) {
            if (handrailStatus.value == i) {
                return handrailStatus;
            }
        }
        return null;
    }
}
