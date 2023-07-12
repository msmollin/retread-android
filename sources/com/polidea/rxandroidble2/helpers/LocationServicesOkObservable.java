package com.polidea.rxandroidble2.helpers;

import android.content.Context;
import androidx.annotation.NonNull;
import bleshadow.javax.inject.Inject;
import bleshadow.javax.inject.Named;
import com.polidea.rxandroidble2.ClientComponent;
import com.polidea.rxandroidble2.DaggerClientComponent;
import com.polidea.rxandroidble2.internal.util.DisposableUtil;
import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.observers.DisposableObserver;

/* loaded from: classes.dex */
public class LocationServicesOkObservable extends Observable<Boolean> {
    @NonNull
    private final Observable<Boolean> locationServicesOkObsImpl;

    public static LocationServicesOkObservable createInstance(@NonNull Context context) {
        return DaggerClientComponent.builder().clientModule(new ClientComponent.ClientModule(context)).build().locationServicesOkObservable();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Inject
    public LocationServicesOkObservable(@NonNull @Named("location-ok-boolean-observable") Observable<Boolean> observable) {
        this.locationServicesOkObsImpl = observable;
    }

    @Override // io.reactivex.Observable
    protected void subscribeActual(Observer<? super Boolean> observer) {
        DisposableObserver disposableObserver = DisposableUtil.disposableObserver(observer);
        observer.onSubscribe(disposableObserver);
        this.locationServicesOkObsImpl.subscribeWith(disposableObserver);
    }
}
