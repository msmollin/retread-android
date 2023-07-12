package com.treadly.client.lib.sdk.Model;

/* loaded from: classes2.dex */
public enum DeviceIrDebugAction {
    none(0),
    start(1),
    append(2),
    end(3);
    
    private int value;

    DeviceIrDebugAction(int i) {
        this.value = i;
    }

    public int value() {
        return this.value;
    }

    public static DeviceIrDebugAction fromValue(int i) {
        DeviceIrDebugAction[] values;
        for (DeviceIrDebugAction deviceIrDebugAction : values()) {
            if (deviceIrDebugAction.value == i) {
                return deviceIrDebugAction;
            }
        }
        return null;
    }
}
