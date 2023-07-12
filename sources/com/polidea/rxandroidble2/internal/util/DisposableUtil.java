package com.polidea.rxandroidble2.internal.util;

import io.reactivex.ObservableEmitter;
import io.reactivex.Observer;
import io.reactivex.SingleEmitter;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.observers.DisposableSingleObserver;

/* loaded from: classes.dex */
public class DisposableUtil {
    private DisposableUtil() {
    }

    public static <T> DisposableSingleObserver<T> disposableSingleObserverFromEmitter(final SingleEmitter<T> singleEmitter) {
        return new DisposableSingleObserver<T>() { // from class: com.polidea.rxandroidble2.internal.util.DisposableUtil.1
            @Override // io.reactivex.SingleObserver
            public void onSuccess(T t) {
                SingleEmitter.this.onSuccess(t);
            }

            @Override // io.reactivex.SingleObserver
            public void onError(Throwable th) {
                SingleEmitter.this.tryOnError(th);
            }
        };
    }

    public static <T> DisposableObserver<T> disposableObserverFromEmitter(final ObservableEmitter<T> observableEmitter) {
        return new DisposableObserver<T>() { // from class: com.polidea.rxandroidble2.internal.util.DisposableUtil.2
            @Override // io.reactivex.Observer
            public void onNext(T t) {
                ObservableEmitter.this.onNext(t);
            }

            @Override // io.reactivex.Observer
            public void onError(Throwable th) {
                ObservableEmitter.this.tryOnError(th);
            }

            @Override // io.reactivex.Observer
            public void onComplete() {
                ObservableEmitter.this.onComplete();
            }
        };
    }

    public static <T> DisposableSingleObserver<T> disposableSingleObserverFromEmitter(final ObservableEmitter<T> observableEmitter) {
        return new DisposableSingleObserver<T>() { // from class: com.polidea.rxandroidble2.internal.util.DisposableUtil.3
            @Override // io.reactivex.SingleObserver
            public void onSuccess(T t) {
                ObservableEmitter.this.onNext(t);
                ObservableEmitter.this.onComplete();
            }

            @Override // io.reactivex.SingleObserver
            public void onError(Throwable th) {
                ObservableEmitter.this.tryOnError(th);
            }
        };
    }

    public static <T> DisposableObserver<T> disposableObserver(final Observer<T> observer) {
        return new DisposableObserver<T>() { // from class: com.polidea.rxandroidble2.internal.util.DisposableUtil.4
            @Override // io.reactivex.Observer
            public void onNext(T t) {
                Observer.this.onNext(t);
            }

            @Override // io.reactivex.Observer
            public void onError(Throwable th) {
                Observer.this.onError(th);
            }

            @Override // io.reactivex.Observer
            public void onComplete() {
                Observer.this.onComplete();
            }
        };
    }
}
