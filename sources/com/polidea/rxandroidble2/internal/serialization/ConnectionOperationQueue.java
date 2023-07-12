package com.polidea.rxandroidble2.internal.serialization;

import com.polidea.rxandroidble2.exceptions.BleException;

/* loaded from: classes.dex */
public interface ConnectionOperationQueue extends ClientOperationQueue {
    void terminate(BleException bleException);
}
