package com.polidea.rxandroidble2.internal.connection;

import android.util.Log;
import bleshadow.javax.inject.Inject;
import bleshadow.javax.inject.Named;
import com.polidea.rxandroidble2.RxBleAdapterStateObservable;
import com.polidea.rxandroidble2.exceptions.BleDisconnectedException;
import com.polidea.rxandroidble2.exceptions.BleException;
import com.polidea.rxandroidble2.exceptions.BleGattException;
import com.polidea.rxandroidble2.internal.util.RxBleAdapterWrapper;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Cancellable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.functions.Predicate;
import java.util.LinkedList;
import java.util.Queue;

/* JADX INFO: Access modifiers changed from: package-private */
@ConnectionScope
/* loaded from: classes.dex */
public class DisconnectionRouter implements DisconnectionRouterInput, DisconnectionRouterOutput {
    private static final String TAG = "DisconnectionRouter";
    private Disposable adapterMonitoringDisposable;
    private final Queue<ObservableEmitter<BleException>> exceptionEmitters = new LinkedList();
    private BleException exceptionOccurred;

    /* JADX INFO: Access modifiers changed from: package-private */
    @Inject
    public DisconnectionRouter(@Named("mac-address") final String str, RxBleAdapterWrapper rxBleAdapterWrapper, Observable<RxBleAdapterStateObservable.BleAdapterState> observable) {
        this.adapterMonitoringDisposable = awaitAdapterNotUsable(rxBleAdapterWrapper, observable).map(new Function<Boolean, BleException>() { // from class: com.polidea.rxandroidble2.internal.connection.DisconnectionRouter.3
            @Override // io.reactivex.functions.Function
            public BleException apply(Boolean bool) {
                return new BleDisconnectedException(str);
            }
        }).firstElement().subscribe(new Consumer<BleException>() { // from class: com.polidea.rxandroidble2.internal.connection.DisconnectionRouter.1
            @Override // io.reactivex.functions.Consumer
            public void accept(BleException bleException) throws Exception {
                Log.d(DisconnectionRouter.TAG, "An exception received, indicating that the adapter has became unusable.");
                DisconnectionRouter.this.exceptionOccurred = bleException;
                DisconnectionRouter.this.notifySubscribersAboutException();
            }
        }, new Consumer<Throwable>() { // from class: com.polidea.rxandroidble2.internal.connection.DisconnectionRouter.2
            @Override // io.reactivex.functions.Consumer
            public void accept(Throwable th) throws Exception {
                Log.w(DisconnectionRouter.TAG, "Failed to monitor adapter state.", th);
            }
        });
    }

    private static Observable<Boolean> awaitAdapterNotUsable(RxBleAdapterWrapper rxBleAdapterWrapper, Observable<RxBleAdapterStateObservable.BleAdapterState> observable) {
        return observable.map(new Function<RxBleAdapterStateObservable.BleAdapterState, Boolean>() { // from class: com.polidea.rxandroidble2.internal.connection.DisconnectionRouter.5
            @Override // io.reactivex.functions.Function
            public Boolean apply(RxBleAdapterStateObservable.BleAdapterState bleAdapterState) {
                return Boolean.valueOf(bleAdapterState.isUsable());
            }
        }).startWith((Observable<R>) Boolean.valueOf(rxBleAdapterWrapper.isBluetoothEnabled())).filter(new Predicate<Boolean>() { // from class: com.polidea.rxandroidble2.internal.connection.DisconnectionRouter.4
            @Override // io.reactivex.functions.Predicate
            public boolean test(Boolean bool) {
                return !bool.booleanValue();
            }
        });
    }

    @Override // com.polidea.rxandroidble2.internal.connection.DisconnectionRouterInput
    public void onDisconnectedException(BleDisconnectedException bleDisconnectedException) {
        onExceptionOccurred(bleDisconnectedException);
    }

    @Override // com.polidea.rxandroidble2.internal.connection.DisconnectionRouterInput
    public void onGattConnectionStateException(BleGattException bleGattException) {
        onExceptionOccurred(bleGattException);
    }

    private void onExceptionOccurred(BleException bleException) {
        if (this.exceptionOccurred == null) {
            this.exceptionOccurred = bleException;
            notifySubscribersAboutException();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void notifySubscribersAboutException() {
        if (this.adapterMonitoringDisposable != null) {
            this.adapterMonitoringDisposable.dispose();
        }
        while (!this.exceptionEmitters.isEmpty()) {
            ObservableEmitter<BleException> poll = this.exceptionEmitters.poll();
            poll.onNext(this.exceptionOccurred);
            poll.onComplete();
        }
    }

    @Override // com.polidea.rxandroidble2.internal.connection.DisconnectionRouterOutput
    public Observable<BleException> asValueOnlyObservable() {
        return Observable.create(new ObservableOnSubscribe<BleException>() { // from class: com.polidea.rxandroidble2.internal.connection.DisconnectionRouter.6
            @Override // io.reactivex.ObservableOnSubscribe
            public void subscribe(ObservableEmitter<BleException> observableEmitter) throws Exception {
                if (DisconnectionRouter.this.exceptionOccurred != null) {
                    observableEmitter.onNext(DisconnectionRouter.this.exceptionOccurred);
                    observableEmitter.onComplete();
                    return;
                }
                DisconnectionRouter.this.storeEmitterToBeNotifiedInTheFuture(observableEmitter);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void storeEmitterToBeNotifiedInTheFuture(final ObservableEmitter<BleException> observableEmitter) {
        this.exceptionEmitters.add(observableEmitter);
        observableEmitter.setCancellable(new Cancellable() { // from class: com.polidea.rxandroidble2.internal.connection.DisconnectionRouter.7
            @Override // io.reactivex.functions.Cancellable
            public void cancel() throws Exception {
                DisconnectionRouter.this.exceptionEmitters.remove(observableEmitter);
            }
        });
    }

    @Override // com.polidea.rxandroidble2.internal.connection.DisconnectionRouterOutput
    public <T> Observable<T> asErrorOnlyObservable() {
        return (Observable<T>) asValueOnlyObservable().flatMap(new Function<BleException, Observable<T>>() { // from class: com.polidea.rxandroidble2.internal.connection.DisconnectionRouter.8
            @Override // io.reactivex.functions.Function
            public Observable<T> apply(BleException bleException) {
                return Observable.error(bleException);
            }
        });
    }
}
