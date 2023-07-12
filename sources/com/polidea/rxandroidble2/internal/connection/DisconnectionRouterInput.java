package com.polidea.rxandroidble2.internal.connection;

import com.polidea.rxandroidble2.exceptions.BleDisconnectedException;
import com.polidea.rxandroidble2.exceptions.BleGattException;

/* loaded from: classes.dex */
interface DisconnectionRouterInput {
    void onDisconnectedException(BleDisconnectedException bleDisconnectedException);

    void onGattConnectionStateException(BleGattException bleGattException);
}
