package com.polidea.rxandroidble2.internal.connection;

import bleshadow.javax.inject.Inject;
import bleshadow.javax.inject.Named;
import com.polidea.rxandroidble2.exceptions.BleGattException;
import com.polidea.rxandroidble2.exceptions.BleGattOperationType;
import io.reactivex.Observable;
import io.reactivex.disposables.SerialDisposable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Predicate;

@ConnectionScope
/* loaded from: classes.dex */
class MtuWatcher implements ConnectionSubscriptionWatcher, MtuProvider, Consumer<Integer> {
    private Integer currentMtu;
    private final Observable<Integer> mtuObservable;
    private final SerialDisposable serialSubscription = new SerialDisposable();

    /* JADX INFO: Access modifiers changed from: package-private */
    @Inject
    public MtuWatcher(RxBleGattCallback rxBleGattCallback, @Named("GATT_MTU_MINIMUM") int i) {
        this.mtuObservable = rxBleGattCallback.getOnMtuChanged().retry(new Predicate<Throwable>() { // from class: com.polidea.rxandroidble2.internal.connection.MtuWatcher.1
            @Override // io.reactivex.functions.Predicate
            public boolean test(Throwable th) {
                return (th instanceof BleGattException) && ((BleGattException) th).getBleGattOperationType() == BleGattOperationType.ON_MTU_CHANGED;
            }
        });
        this.currentMtu = Integer.valueOf(i);
    }

    @Override // com.polidea.rxandroidble2.internal.connection.MtuProvider
    public int getMtu() {
        return this.currentMtu.intValue();
    }

    @Override // com.polidea.rxandroidble2.internal.connection.ConnectionSubscriptionWatcher
    public void onConnectionSubscribed() {
        this.serialSubscription.set(this.mtuObservable.subscribe(this, new Consumer<Throwable>() { // from class: com.polidea.rxandroidble2.internal.connection.MtuWatcher.2
            @Override // io.reactivex.functions.Consumer
            public void accept(Throwable th) {
            }
        }));
    }

    @Override // com.polidea.rxandroidble2.internal.connection.ConnectionSubscriptionWatcher
    public void onConnectionUnsubscribed() {
        this.serialSubscription.dispose();
    }

    @Override // io.reactivex.functions.Consumer
    public void accept(Integer num) {
        this.currentMtu = num;
    }
}
