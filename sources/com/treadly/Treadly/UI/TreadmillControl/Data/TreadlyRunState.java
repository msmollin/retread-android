package com.treadly.Treadly.UI.TreadmillControl.Data;

/* loaded from: classes2.dex */
public enum TreadlyRunState {
    STOPPED(0),
    STOPPING(1),
    STARTING(2),
    STARTED(3);
    
    private int value;

    TreadlyRunState(int i) {
        this.value = i;
    }

    public static TreadlyRunState fromValue(int i) {
        TreadlyRunState[] values;
        for (TreadlyRunState treadlyRunState : values()) {
            if (treadlyRunState.value == i) {
                return treadlyRunState;
            }
        }
        return null;
    }

    public int getValue() {
        return this.value;
    }
}
