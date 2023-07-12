package com.treadly.client.lib.sdk.Model;

/* loaded from: classes2.dex */
public enum PairingModeTriggerType {
    none(0),
    ir(1),
    handrail(2);
    
    private int value;

    PairingModeTriggerType(int i) {
        this.value = i;
    }

    public int value() {
        return this.value;
    }

    public static PairingModeTriggerType fromValue(int i) {
        PairingModeTriggerType[] values;
        for (PairingModeTriggerType pairingModeTriggerType : values()) {
            if (pairingModeTriggerType.value == i) {
                return pairingModeTriggerType;
            }
        }
        return null;
    }
}
