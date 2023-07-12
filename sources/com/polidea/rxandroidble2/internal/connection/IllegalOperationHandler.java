package com.polidea.rxandroidble2.internal.connection;

import android.bluetooth.BluetoothGattCharacteristic;
import androidx.annotation.Nullable;
import com.polidea.rxandroidble2.internal.BleIllegalOperationException;

/* loaded from: classes.dex */
public abstract class IllegalOperationHandler {
    protected IllegalOperationMessageCreator messageCreator;

    @Nullable
    public abstract BleIllegalOperationException handleMismatchData(BluetoothGattCharacteristic bluetoothGattCharacteristic, int i);

    /* JADX INFO: Access modifiers changed from: package-private */
    public IllegalOperationHandler(IllegalOperationMessageCreator illegalOperationMessageCreator) {
        this.messageCreator = illegalOperationMessageCreator;
    }
}
