package com.treadly.client.lib.sdk.Model;

/* loaded from: classes2.dex */
public enum IrMode {
    GEN_1(0),
    GEN_2(1),
    UNKNOWN(100);
    
    private int value;

    IrMode(int i) {
        this.value = i;
    }

    public int value() {
        return this.value;
    }

    public static IrMode fromValue(int i) {
        IrMode[] values;
        for (IrMode irMode : values()) {
            if (irMode.value == i) {
                return irMode;
            }
        }
        return null;
    }
}
