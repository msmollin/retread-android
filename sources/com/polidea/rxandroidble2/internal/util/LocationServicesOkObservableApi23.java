package com.polidea.rxandroidble2.internal.util;

import android.annotation.TargetApi;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import bleshadow.javax.inject.Inject;
import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposables;
import io.reactivex.functions.Action;
import java.util.concurrent.atomic.AtomicBoolean;

@TargetApi(19)
/* loaded from: classes.dex */
public class LocationServicesOkObservableApi23 extends Observable<Boolean> {
    private final Context context;
    private final LocationServicesStatus locationServicesStatus;

    /* JADX INFO: Access modifiers changed from: package-private */
    @Inject
    public LocationServicesOkObservableApi23(Context context, LocationServicesStatus locationServicesStatus) {
        this.context = context;
        this.locationServicesStatus = locationServicesStatus;
    }

    @Override // io.reactivex.Observable
    protected void subscribeActual(final Observer<? super Boolean> observer) {
        boolean isLocationProviderOk = this.locationServicesStatus.isLocationProviderOk();
        final AtomicBoolean atomicBoolean = new AtomicBoolean(isLocationProviderOk);
        final BroadcastReceiver broadcastReceiver = new BroadcastReceiver() { // from class: com.polidea.rxandroidble2.internal.util.LocationServicesOkObservableApi23.1
            @Override // android.content.BroadcastReceiver
            public void onReceive(Context context, Intent intent) {
                boolean isLocationProviderOk2 = LocationServicesOkObservableApi23.this.locationServicesStatus.isLocationProviderOk();
                if (atomicBoolean.compareAndSet(!isLocationProviderOk2, isLocationProviderOk2)) {
                    observer.onNext(Boolean.valueOf(isLocationProviderOk2));
                }
            }
        };
        this.context.registerReceiver(broadcastReceiver, new IntentFilter("android.location.MODE_CHANGED"));
        observer.onSubscribe(Disposables.fromAction(new Action() { // from class: com.polidea.rxandroidble2.internal.util.LocationServicesOkObservableApi23.2
            @Override // io.reactivex.functions.Action
            public void run() throws Exception {
                LocationServicesOkObservableApi23.this.context.unregisterReceiver(broadcastReceiver);
            }
        }));
        observer.onNext(Boolean.valueOf(isLocationProviderOk));
    }
}
