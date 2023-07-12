package com.treadly.Treadly.Data.Model;

/* loaded from: classes2.dex */
public enum TrainerModeEnabledState {
    disabled(0),
    enabled(1),
    unknown(2);
    
    private int value;

    TrainerModeEnabledState(int i) {
        this.value = i;
    }

    public static TrainerModeEnabledState fromValue(int i) {
        TrainerModeEnabledState[] values;
        for (TrainerModeEnabledState trainerModeEnabledState : values()) {
            if (trainerModeEnabledState.value == i) {
                return trainerModeEnabledState;
            }
        }
        return null;
    }

    public int getValue() {
        return this.value;
    }
}
