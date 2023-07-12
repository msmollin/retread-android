package com.polidea.rxandroidble2.internal.operations;

import android.bluetooth.BluetoothGatt;
import androidx.annotation.RequiresApi;
import bleshadow.javax.inject.Inject;
import com.polidea.rxandroidble2.exceptions.BleGattCannotStartException;
import com.polidea.rxandroidble2.exceptions.BleGattOperationType;
import com.polidea.rxandroidble2.internal.SingleResponseOperation;
import com.polidea.rxandroidble2.internal.connection.RxBleGattCallback;
import io.reactivex.Scheduler;
import io.reactivex.Single;
import java.util.concurrent.TimeUnit;

/* loaded from: classes.dex */
public class ConnectionPriorityChangeOperation extends SingleResponseOperation<Long> {
    private final int connectionPriority;
    private final Scheduler delayScheduler;
    private final long operationTimeout;
    private final TimeUnit timeUnit;

    /* JADX INFO: Access modifiers changed from: package-private */
    @Inject
    public ConnectionPriorityChangeOperation(RxBleGattCallback rxBleGattCallback, BluetoothGatt bluetoothGatt, TimeoutConfiguration timeoutConfiguration, int i, long j, TimeUnit timeUnit, Scheduler scheduler) {
        super(bluetoothGatt, rxBleGattCallback, BleGattOperationType.CONNECTION_PRIORITY_CHANGE, timeoutConfiguration);
        this.connectionPriority = i;
        this.operationTimeout = j;
        this.timeUnit = timeUnit;
        this.delayScheduler = scheduler;
    }

    @Override // com.polidea.rxandroidble2.internal.SingleResponseOperation
    protected Single<Long> getCallback(RxBleGattCallback rxBleGattCallback) {
        return Single.timer(this.operationTimeout, this.timeUnit, this.delayScheduler);
    }

    @Override // com.polidea.rxandroidble2.internal.SingleResponseOperation
    @RequiresApi(api = 21)
    protected boolean startOperation(BluetoothGatt bluetoothGatt) throws IllegalArgumentException, BleGattCannotStartException {
        return bluetoothGatt.requestConnectionPriority(this.connectionPriority);
    }
}
