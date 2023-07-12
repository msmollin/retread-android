package com.polidea.rxandroidble2.internal.connection;

import com.polidea.rxandroidble2.RxBleConnection;
import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.functions.Function;

/* loaded from: classes.dex */
public class NoRetryStrategy implements RxBleConnection.WriteOperationRetryStrategy {
    @Override // io.reactivex.ObservableTransformer
    /* renamed from: apply */
    public ObservableSource<RxBleConnection.WriteOperationRetryStrategy.LongWriteFailure> apply2(Observable<RxBleConnection.WriteOperationRetryStrategy.LongWriteFailure> observable) {
        return observable.flatMap(new Function<RxBleConnection.WriteOperationRetryStrategy.LongWriteFailure, Observable<RxBleConnection.WriteOperationRetryStrategy.LongWriteFailure>>() { // from class: com.polidea.rxandroidble2.internal.connection.NoRetryStrategy.1
            @Override // io.reactivex.functions.Function
            public Observable<RxBleConnection.WriteOperationRetryStrategy.LongWriteFailure> apply(RxBleConnection.WriteOperationRetryStrategy.LongWriteFailure longWriteFailure) {
                return Observable.error(longWriteFailure.getCause());
            }
        });
    }
}
