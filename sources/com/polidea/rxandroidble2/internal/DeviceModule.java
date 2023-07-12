package com.polidea.rxandroidble2.internal;

import android.bluetooth.BluetoothDevice;
import bleshadow.dagger.Module;
import bleshadow.dagger.Provides;
import bleshadow.javax.inject.Named;
import com.jakewharton.rxrelay2.BehaviorRelay;
import com.polidea.rxandroidble2.RxBleConnection;
import com.polidea.rxandroidble2.internal.connection.ConnectionComponent;
import com.polidea.rxandroidble2.internal.connection.ConnectionStateChangeListener;
import com.polidea.rxandroidble2.internal.operations.TimeoutConfiguration;
import com.polidea.rxandroidble2.internal.util.RxBleAdapterWrapper;
import io.reactivex.Scheduler;
import java.util.concurrent.TimeUnit;

@Module(subcomponents = {ConnectionComponent.class})
/* loaded from: classes.dex */
public class DeviceModule {
    public static final String CONNECT_TIMEOUT = "connect-timeout";
    private static final int DEFAULT_CONNECT_TIMEOUT = 35;
    private static final int DEFAULT_DISCONNECT_TIMEOUT = 10;
    private static final int DEFAULT_OPERATION_TIMEOUT = 30;
    public static final String DISCONNECT_TIMEOUT = "disconnect-timeout";
    public static final String MAC_ADDRESS = "mac-address";
    public static final String OPERATION_TIMEOUT = "operation-timeout";
    final String macAddress;

    /* JADX INFO: Access modifiers changed from: package-private */
    public DeviceModule(String str) {
        this.macAddress = str;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Provides
    public BluetoothDevice provideBluetoothDevice(RxBleAdapterWrapper rxBleAdapterWrapper) {
        return rxBleAdapterWrapper.getRemoteDevice(this.macAddress);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Named(MAC_ADDRESS)
    @Provides
    public String provideMacAddress() {
        return this.macAddress;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Named(CONNECT_TIMEOUT)
    @Provides
    public static TimeoutConfiguration providesConnectTimeoutConf(@Named("timeout") Scheduler scheduler) {
        return new TimeoutConfiguration(35L, TimeUnit.SECONDS, scheduler);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Named(DISCONNECT_TIMEOUT)
    @Provides
    public static TimeoutConfiguration providesDisconnectTimeoutConf(@Named("timeout") Scheduler scheduler) {
        return new TimeoutConfiguration(10L, TimeUnit.SECONDS, scheduler);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @DeviceScope
    @Provides
    public static BehaviorRelay<RxBleConnection.RxBleConnectionState> provideConnectionStateRelay() {
        return BehaviorRelay.createDefault(RxBleConnection.RxBleConnectionState.DISCONNECTED);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @DeviceScope
    @Provides
    public static ConnectionStateChangeListener provideConnectionStateChangeListener(final BehaviorRelay<RxBleConnection.RxBleConnectionState> behaviorRelay) {
        return new ConnectionStateChangeListener() { // from class: com.polidea.rxandroidble2.internal.DeviceModule.1
            @Override // com.polidea.rxandroidble2.internal.connection.ConnectionStateChangeListener
            public void onConnectionStateChange(RxBleConnection.RxBleConnectionState rxBleConnectionState) {
                BehaviorRelay.this.accept(rxBleConnectionState);
            }
        };
    }
}
