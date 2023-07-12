package com.polidea.rxandroidble2.internal.connection;

import android.bluetooth.BluetoothGatt;
import androidx.annotation.NonNull;
import java.util.concurrent.atomic.AtomicReference;

@ConnectionScope
/* loaded from: classes.dex */
public class BluetoothGattProvider {
    private final AtomicReference<BluetoothGatt> reference = new AtomicReference<>();

    public BluetoothGatt getBluetoothGatt() {
        return this.reference.get();
    }

    public void updateBluetoothGatt(@NonNull BluetoothGatt bluetoothGatt) {
        this.reference.compareAndSet(null, bluetoothGatt);
    }
}
