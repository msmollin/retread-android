package com.polidea.rxandroidble2.exceptions;

import android.bluetooth.BluetoothGattCharacteristic;
import androidx.annotation.RestrictTo;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/* loaded from: classes.dex */
public class BleCannotSetCharacteristicNotificationException extends BleException {
    public static final int CANNOT_FIND_CLIENT_CHARACTERISTIC_CONFIG_DESCRIPTOR = 2;
    public static final int CANNOT_SET_LOCAL_NOTIFICATION = 1;
    public static final int CANNOT_WRITE_CLIENT_CHARACTERISTIC_CONFIG_DESCRIPTOR = 3;
    @Deprecated
    public static final int UNKNOWN = -1;
    private final BluetoothGattCharacteristic bluetoothGattCharacteristic;
    private final int reason;

    @Retention(RetentionPolicy.SOURCE)
    /* loaded from: classes.dex */
    public @interface Reason {
    }

    private static String reasonDescription(int i) {
        switch (i) {
            case 1:
                return "Cannot set local notification";
            case 2:
                return "Cannot find client characteristic config descriptor";
            case 3:
                return "Cannot write client characteristic config descriptor";
            default:
                return "Unknown error";
        }
    }

    @Deprecated
    public BleCannotSetCharacteristicNotificationException(BluetoothGattCharacteristic bluetoothGattCharacteristic) {
        super(createMessage(bluetoothGattCharacteristic, -1));
        this.bluetoothGattCharacteristic = bluetoothGattCharacteristic;
        this.reason = -1;
    }

    @RestrictTo({RestrictTo.Scope.LIBRARY_GROUP})
    public BleCannotSetCharacteristicNotificationException(BluetoothGattCharacteristic bluetoothGattCharacteristic, int i, Throwable th) {
        super(createMessage(bluetoothGattCharacteristic, i), th);
        this.bluetoothGattCharacteristic = bluetoothGattCharacteristic;
        this.reason = i;
    }

    public BluetoothGattCharacteristic getBluetoothGattCharacteristic() {
        return this.bluetoothGattCharacteristic;
    }

    public int getReason() {
        return this.reason;
    }

    private static String createMessage(BluetoothGattCharacteristic bluetoothGattCharacteristic, int i) {
        return reasonDescription(i) + " (code " + i + ") with characteristic UUID " + bluetoothGattCharacteristic.getUuid();
    }
}
