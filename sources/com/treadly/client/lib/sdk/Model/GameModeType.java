package com.treadly.client.lib.sdk.Model;

/* loaded from: classes2.dex */
public enum GameModeType {
    ACTIVE(0),
    PASSIVE(1);
    
    private int value;

    GameModeType(int i) {
        this.value = i;
    }

    public int value() {
        return this.value;
    }

    public static GameModeType fromValue(int i) {
        GameModeType[] values;
        for (GameModeType gameModeType : values()) {
            if (gameModeType.value == i) {
                return gameModeType;
            }
        }
        return null;
    }
}
