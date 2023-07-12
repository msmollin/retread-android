package com.polidea.rxandroidble2.exceptions;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

/* loaded from: classes.dex */
public class BleDisconnectedException extends BleException {
    @NonNull
    public final String bluetoothDeviceAddress;

    @Deprecated
    public BleDisconnectedException() {
        this.bluetoothDeviceAddress = "";
    }

    public BleDisconnectedException(Throwable th, @NonNull String str) {
        super(createMessage(str), th);
        this.bluetoothDeviceAddress = str;
    }

    public BleDisconnectedException(@NonNull String str) {
        super(createMessage(str));
        this.bluetoothDeviceAddress = str;
    }

    private static String createMessage(@Nullable String str) {
        return "Disconnected from " + str;
    }
}
