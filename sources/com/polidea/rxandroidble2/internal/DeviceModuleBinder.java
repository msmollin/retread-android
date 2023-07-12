package com.polidea.rxandroidble2.internal;

import bleshadow.dagger.Binds;
import bleshadow.dagger.Module;
import com.polidea.rxandroidble2.RxBleDevice;
import com.polidea.rxandroidble2.internal.connection.Connector;
import com.polidea.rxandroidble2.internal.connection.ConnectorImpl;

@Module
/* loaded from: classes.dex */
abstract class DeviceModuleBinder {
    @Binds
    abstract Connector bindConnector(ConnectorImpl connectorImpl);

    @Binds
    abstract RxBleDevice bindDevice(RxBleDeviceImpl rxBleDeviceImpl);

    DeviceModuleBinder() {
    }
}
