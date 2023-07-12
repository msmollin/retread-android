package com.polidea.rxandroidble2.internal.util;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.ObservableTransformer;

/* loaded from: classes.dex */
public class ObservableUtil {
    private static final ObservableTransformer<?, ?> IDENTITY_TRANSFORMER = new ObservableTransformer<Object, Object>() { // from class: com.polidea.rxandroidble2.internal.util.ObservableUtil.1
        @Override // io.reactivex.ObservableTransformer
        /* renamed from: apply */
        public ObservableSource<Object> apply2(Observable<Object> observable) {
            return observable;
        }
    };

    private ObservableUtil() {
    }

    public static <T> Observable<T> justOnNext(T t) {
        return Observable.never().startWith((Observable) t);
    }

    public static <T> ObservableTransformer<T, T> identityTransformer() {
        return (ObservableTransformer<T, T>) IDENTITY_TRANSFORMER;
    }
}
