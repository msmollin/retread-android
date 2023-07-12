package com.treadly.Treadly.Data.Model;

/* loaded from: classes2.dex */
public enum TrainerModeState {
    inactive(0),
    pending(1),
    active(2),
    invited(3);
    
    private int value;

    TrainerModeState(int i) {
        this.value = i;
    }

    public static TrainerModeState fromValue(int i) {
        TrainerModeState[] values;
        for (TrainerModeState trainerModeState : values()) {
            if (trainerModeState.value == i) {
                return trainerModeState;
            }
        }
        return null;
    }

    public int getValue() {
        return this.value;
    }
}
