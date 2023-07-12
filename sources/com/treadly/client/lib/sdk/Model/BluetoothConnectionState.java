package com.treadly.client.lib.sdk.Model;

/* loaded from: classes2.dex */
public enum BluetoothConnectionState {
    unknown(0),
    poweredOn(1),
    poweredOff(2);
    
    private int value;

    BluetoothConnectionState(int i) {
        this.value = i;
    }

    public int value() {
        return this.value;
    }

    public static BluetoothConnectionState fromValue(int i) {
        BluetoothConnectionState[] values;
        for (BluetoothConnectionState bluetoothConnectionState : values()) {
            if (bluetoothConnectionState.value == i) {
                return bluetoothConnectionState;
            }
        }
        return null;
    }
}
