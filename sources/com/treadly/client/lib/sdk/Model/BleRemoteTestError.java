package com.treadly.client.lib.sdk.Model;

/* loaded from: classes2.dex */
public enum BleRemoteTestError {
    noRemoteConnected(0),
    buttonTimeout(1),
    restartRequired(2);
    
    private int value;

    BleRemoteTestError(int i) {
        this.value = i;
    }

    public int value() {
        return this.value;
    }

    public static BleRemoteTestError fromValue(int i) {
        BleRemoteTestError[] values;
        for (BleRemoteTestError bleRemoteTestError : values()) {
            if (bleRemoteTestError.value == i) {
                return bleRemoteTestError;
            }
        }
        return null;
    }
}
