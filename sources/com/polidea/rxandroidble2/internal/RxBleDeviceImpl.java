package com.polidea.rxandroidble2.internal;

import android.bluetooth.BluetoothDevice;
import androidx.annotation.Nullable;
import bleshadow.javax.inject.Inject;
import com.jakewharton.rxrelay2.BehaviorRelay;
import com.polidea.rxandroidble2.ConnectionSetup;
import com.polidea.rxandroidble2.RxBleConnection;
import com.polidea.rxandroidble2.RxBleDevice;
import com.polidea.rxandroidble2.Timeout;
import com.polidea.rxandroidble2.exceptions.BleAlreadyConnectedException;
import com.polidea.rxandroidble2.internal.connection.Connector;
import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.functions.Action;
import java.util.concurrent.Callable;
import java.util.concurrent.atomic.AtomicBoolean;

/* JADX INFO: Access modifiers changed from: package-private */
@DeviceScope
/* loaded from: classes.dex */
public class RxBleDeviceImpl implements RxBleDevice {
    private final BluetoothDevice bluetoothDevice;
    private final BehaviorRelay<RxBleConnection.RxBleConnectionState> connectionStateRelay;
    private final Connector connector;
    private AtomicBoolean isConnected = new AtomicBoolean(false);

    /* JADX INFO: Access modifiers changed from: package-private */
    @Inject
    public RxBleDeviceImpl(BluetoothDevice bluetoothDevice, Connector connector, BehaviorRelay<RxBleConnection.RxBleConnectionState> behaviorRelay) {
        this.bluetoothDevice = bluetoothDevice;
        this.connector = connector;
        this.connectionStateRelay = behaviorRelay;
    }

    @Override // com.polidea.rxandroidble2.RxBleDevice
    public Observable<RxBleConnection.RxBleConnectionState> observeConnectionStateChanges() {
        return this.connectionStateRelay.distinctUntilChanged().skip(1L);
    }

    @Override // com.polidea.rxandroidble2.RxBleDevice
    public RxBleConnection.RxBleConnectionState getConnectionState() {
        return this.connectionStateRelay.getValue();
    }

    @Override // com.polidea.rxandroidble2.RxBleDevice
    public Observable<RxBleConnection> establishConnection(boolean z) {
        return establishConnection(new ConnectionSetup.Builder().setAutoConnect(z).setSuppressIllegalOperationCheck(true).build());
    }

    @Override // com.polidea.rxandroidble2.RxBleDevice
    public Observable<RxBleConnection> establishConnection(boolean z, Timeout timeout) {
        return establishConnection(new ConnectionSetup.Builder().setAutoConnect(z).setOperationTimeout(timeout).setSuppressIllegalOperationCheck(true).build());
    }

    public Observable<RxBleConnection> establishConnection(final ConnectionSetup connectionSetup) {
        return Observable.defer(new Callable<ObservableSource<RxBleConnection>>() { // from class: com.polidea.rxandroidble2.internal.RxBleDeviceImpl.1
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // java.util.concurrent.Callable
            public ObservableSource<RxBleConnection> call() throws Exception {
                if (RxBleDeviceImpl.this.isConnected.compareAndSet(false, true)) {
                    return RxBleDeviceImpl.this.connector.prepareConnection(connectionSetup).doFinally(new Action() { // from class: com.polidea.rxandroidble2.internal.RxBleDeviceImpl.1.1
                        @Override // io.reactivex.functions.Action
                        public void run() {
                            RxBleDeviceImpl.this.isConnected.set(false);
                        }
                    });
                }
                return Observable.error(new BleAlreadyConnectedException(RxBleDeviceImpl.this.bluetoothDevice.getAddress()));
            }
        });
    }

    @Override // com.polidea.rxandroidble2.RxBleDevice
    @Nullable
    public String getName() {
        return this.bluetoothDevice.getName();
    }

    @Override // com.polidea.rxandroidble2.RxBleDevice
    public String getMacAddress() {
        return this.bluetoothDevice.getAddress();
    }

    @Override // com.polidea.rxandroidble2.RxBleDevice
    public BluetoothDevice getBluetoothDevice() {
        return this.bluetoothDevice;
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj instanceof RxBleDeviceImpl) {
            return this.bluetoothDevice.equals(((RxBleDeviceImpl) obj).bluetoothDevice);
        }
        return false;
    }

    public int hashCode() {
        return this.bluetoothDevice.hashCode();
    }

    public String toString() {
        return "RxBleDeviceImpl{bluetoothDevice=" + this.bluetoothDevice.getName() + '(' + this.bluetoothDevice.getAddress() + ")}";
    }
}
