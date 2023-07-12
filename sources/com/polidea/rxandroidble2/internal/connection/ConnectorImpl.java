package com.polidea.rxandroidble2.internal.connection;

import android.bluetooth.BluetoothGatt;
import bleshadow.javax.inject.Inject;
import bleshadow.javax.inject.Named;
import com.polidea.rxandroidble2.ConnectionSetup;
import com.polidea.rxandroidble2.RxBleConnection;
import com.polidea.rxandroidble2.internal.connection.ConnectionComponent;
import com.polidea.rxandroidble2.internal.serialization.ClientOperationQueue;
import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.Scheduler;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import java.util.Set;
import java.util.concurrent.Callable;

/* loaded from: classes.dex */
public class ConnectorImpl implements Connector {
    private final Scheduler callbacksScheduler;
    private final ClientOperationQueue clientOperationQueue;
    private final ConnectionComponent.Builder connectionComponentBuilder;

    @Inject
    public ConnectorImpl(ClientOperationQueue clientOperationQueue, ConnectionComponent.Builder builder, @Named("bluetooth_callbacks") Scheduler scheduler) {
        this.clientOperationQueue = clientOperationQueue;
        this.connectionComponentBuilder = builder;
        this.callbacksScheduler = scheduler;
    }

    @Override // com.polidea.rxandroidble2.internal.connection.Connector
    public Observable<RxBleConnection> prepareConnection(final ConnectionSetup connectionSetup) {
        return Observable.defer(new Callable<ObservableSource<RxBleConnection>>() { // from class: com.polidea.rxandroidble2.internal.connection.ConnectorImpl.1
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // java.util.concurrent.Callable
            public ObservableSource<RxBleConnection> call() throws Exception {
                ConnectionComponent build = ConnectorImpl.this.connectionComponentBuilder.connectionModule(new ConnectionModule(connectionSetup)).build();
                final Set<ConnectionSubscriptionWatcher> connectionSubscriptionWatchers = build.connectionSubscriptionWatchers();
                return ConnectorImpl.obtainRxBleConnection(build).delaySubscription(ConnectorImpl.this.enqueueConnectOperation(build)).mergeWith(ConnectorImpl.observeDisconnections(build)).doOnSubscribe(new Consumer<Disposable>() { // from class: com.polidea.rxandroidble2.internal.connection.ConnectorImpl.1.2
                    @Override // io.reactivex.functions.Consumer
                    public void accept(Disposable disposable) throws Exception {
                        for (ConnectionSubscriptionWatcher connectionSubscriptionWatcher : connectionSubscriptionWatchers) {
                            connectionSubscriptionWatcher.onConnectionSubscribed();
                        }
                    }
                }).doFinally(new Action() { // from class: com.polidea.rxandroidble2.internal.connection.ConnectorImpl.1.1
                    @Override // io.reactivex.functions.Action
                    public void run() throws Exception {
                        for (ConnectionSubscriptionWatcher connectionSubscriptionWatcher : connectionSubscriptionWatchers) {
                            connectionSubscriptionWatcher.onConnectionUnsubscribed();
                        }
                    }
                }).subscribeOn(ConnectorImpl.this.callbacksScheduler).unsubscribeOn(ConnectorImpl.this.callbacksScheduler);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static Observable<RxBleConnection> obtainRxBleConnection(final ConnectionComponent connectionComponent) {
        return Observable.fromCallable(new Callable<RxBleConnection>() { // from class: com.polidea.rxandroidble2.internal.connection.ConnectorImpl.2
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // java.util.concurrent.Callable
            public RxBleConnection call() throws Exception {
                return ConnectionComponent.this.rxBleConnection();
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static Observable<RxBleConnection> observeDisconnections(ConnectionComponent connectionComponent) {
        return connectionComponent.gattCallback().observeDisconnect();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public Observable<BluetoothGatt> enqueueConnectOperation(ConnectionComponent connectionComponent) {
        return this.clientOperationQueue.queue(connectionComponent.connectOperation());
    }
}
