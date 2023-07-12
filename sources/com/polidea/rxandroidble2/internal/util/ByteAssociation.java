package com.polidea.rxandroidble2.internal.util;

import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattDescriptor;
import androidx.annotation.NonNull;
import java.util.Arrays;
import java.util.UUID;

/* loaded from: classes.dex */
public class ByteAssociation<T> {
    public final T first;
    public final byte[] second;

    public ByteAssociation(@NonNull T t, byte[] bArr) {
        this.first = t;
        this.second = bArr;
    }

    public boolean equals(Object obj) {
        if (obj instanceof ByteAssociation) {
            ByteAssociation byteAssociation = (ByteAssociation) obj;
            return Arrays.equals(byteAssociation.second, this.second) && byteAssociation.first.equals(this.first);
        }
        return false;
    }

    public int hashCode() {
        return Arrays.hashCode(this.second) ^ this.first.hashCode();
    }

    public String toString() {
        String simpleName;
        if (this.first instanceof BluetoothGattCharacteristic) {
            simpleName = BluetoothGattCharacteristic.class.getSimpleName() + "(" + ((BluetoothGattCharacteristic) this.first).getUuid().toString() + ")";
        } else if (this.first instanceof BluetoothGattDescriptor) {
            simpleName = BluetoothGattDescriptor.class.getSimpleName() + "(" + ((BluetoothGattDescriptor) this.first).getUuid().toString() + ")";
        } else if (this.first instanceof UUID) {
            simpleName = UUID.class.getSimpleName() + "(" + this.first.toString() + ")";
        } else {
            simpleName = this.first.getClass().getSimpleName();
        }
        return getClass().getSimpleName() + "[first=" + simpleName + ", second=" + Arrays.toString(this.second) + "]";
    }

    public static <T> ByteAssociation<T> create(T t, byte[] bArr) {
        return new ByteAssociation<>(t, bArr);
    }
}
