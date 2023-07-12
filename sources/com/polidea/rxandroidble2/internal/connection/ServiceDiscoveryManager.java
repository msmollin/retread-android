package com.polidea.rxandroidble2.internal.connection;

import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattService;
import androidx.annotation.NonNull;
import bleshadow.javax.inject.Inject;
import com.polidea.rxandroidble2.RxBleDeviceServices;
import com.polidea.rxandroidble2.internal.operations.OperationsProvider;
import com.polidea.rxandroidble2.internal.operations.TimeoutConfiguration;
import com.polidea.rxandroidble2.internal.serialization.ConnectionOperationQueue;
import io.reactivex.Maybe;
import io.reactivex.Single;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.functions.Predicate;
import io.reactivex.internal.functions.Functions;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subjects.BehaviorSubject;
import io.reactivex.subjects.Subject;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;

/* JADX INFO: Access modifiers changed from: package-private */
@ConnectionScope
/* loaded from: classes.dex */
public class ServiceDiscoveryManager {
    private final BluetoothGatt bluetoothGatt;
    private Single<RxBleDeviceServices> deviceServicesObservable;
    private final OperationsProvider operationProvider;
    private final ConnectionOperationQueue operationQueue;
    private Subject<TimeoutConfiguration> timeoutBehaviorSubject = BehaviorSubject.create().toSerialized();
    private boolean hasCachedResults = false;

    /* JADX INFO: Access modifiers changed from: package-private */
    @Inject
    public ServiceDiscoveryManager(ConnectionOperationQueue connectionOperationQueue, BluetoothGatt bluetoothGatt, OperationsProvider operationsProvider) {
        this.operationQueue = connectionOperationQueue;
        this.bluetoothGatt = bluetoothGatt;
        this.operationProvider = operationsProvider;
        reset();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public Single<RxBleDeviceServices> getDiscoverServicesSingle(final long j, final TimeUnit timeUnit) {
        if (this.hasCachedResults) {
            return this.deviceServicesObservable;
        }
        return this.deviceServicesObservable.doOnSubscribe(new Consumer<Disposable>() { // from class: com.polidea.rxandroidble2.internal.connection.ServiceDiscoveryManager.1
            @Override // io.reactivex.functions.Consumer
            public void accept(Disposable disposable) throws Exception {
                ServiceDiscoveryManager.this.timeoutBehaviorSubject.onNext(new TimeoutConfiguration(j, timeUnit, Schedulers.computation()));
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void reset() {
        this.hasCachedResults = false;
        this.deviceServicesObservable = getListOfServicesFromGatt().map(wrapIntoRxBleDeviceServices()).switchIfEmpty(getTimeoutConfiguration().flatMap(scheduleActualDiscoveryWithTimeout())).doOnSuccess(Functions.actionConsumer(new Action() { // from class: com.polidea.rxandroidble2.internal.connection.ServiceDiscoveryManager.3
            @Override // io.reactivex.functions.Action
            public void run() throws Exception {
                ServiceDiscoveryManager.this.hasCachedResults = true;
            }
        })).doOnError(Functions.actionConsumer(new Action() { // from class: com.polidea.rxandroidble2.internal.connection.ServiceDiscoveryManager.2
            @Override // io.reactivex.functions.Action
            public void run() {
                ServiceDiscoveryManager.this.reset();
            }
        })).cache();
    }

    @NonNull
    private Function<List<BluetoothGattService>, RxBleDeviceServices> wrapIntoRxBleDeviceServices() {
        return new Function<List<BluetoothGattService>, RxBleDeviceServices>() { // from class: com.polidea.rxandroidble2.internal.connection.ServiceDiscoveryManager.4
            @Override // io.reactivex.functions.Function
            public RxBleDeviceServices apply(List<BluetoothGattService> list) {
                return new RxBleDeviceServices(list);
            }
        };
    }

    private Maybe<List<BluetoothGattService>> getListOfServicesFromGatt() {
        return Single.fromCallable(new Callable<List<BluetoothGattService>>() { // from class: com.polidea.rxandroidble2.internal.connection.ServiceDiscoveryManager.6
            @Override // java.util.concurrent.Callable
            public List<BluetoothGattService> call() {
                return ServiceDiscoveryManager.this.bluetoothGatt.getServices();
            }
        }).filter(new Predicate<List<BluetoothGattService>>() { // from class: com.polidea.rxandroidble2.internal.connection.ServiceDiscoveryManager.5
            @Override // io.reactivex.functions.Predicate
            public boolean test(List<BluetoothGattService> list) {
                return list.size() > 0;
            }
        });
    }

    @NonNull
    private Single<TimeoutConfiguration> getTimeoutConfiguration() {
        return this.timeoutBehaviorSubject.firstOrError();
    }

    @NonNull
    private Function<TimeoutConfiguration, Single<RxBleDeviceServices>> scheduleActualDiscoveryWithTimeout() {
        return new Function<TimeoutConfiguration, Single<RxBleDeviceServices>>() { // from class: com.polidea.rxandroidble2.internal.connection.ServiceDiscoveryManager.7
            @Override // io.reactivex.functions.Function
            public Single<RxBleDeviceServices> apply(TimeoutConfiguration timeoutConfiguration) {
                return ServiceDiscoveryManager.this.operationQueue.queue(ServiceDiscoveryManager.this.operationProvider.provideServiceDiscoveryOperation(timeoutConfiguration.timeout, timeoutConfiguration.timeoutTimeUnit)).firstOrError();
            }
        };
    }
}
