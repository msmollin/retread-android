package com.polidea.rxandroidble2.internal;

import android.os.DeadObjectException;
import androidx.annotation.NonNull;
import com.polidea.rxandroidble2.exceptions.BleException;
import com.polidea.rxandroidble2.internal.operations.Operation;
import com.polidea.rxandroidble2.internal.serialization.QueueReleaseInterface;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;

/* loaded from: classes.dex */
public abstract class QueueOperation<T> implements Operation<T> {
    protected abstract void protectedRun(ObservableEmitter<T> observableEmitter, QueueReleaseInterface queueReleaseInterface) throws Throwable;

    protected abstract BleException provideException(DeadObjectException deadObjectException);

    @Override // java.lang.Comparable
    public /* bridge */ /* synthetic */ int compareTo(@NonNull Operation<?> operation) {
        return compareTo2((Operation) operation);
    }

    @Override // com.polidea.rxandroidble2.internal.operations.Operation
    public final Observable<T> run(final QueueReleaseInterface queueReleaseInterface) {
        return Observable.create(new ObservableOnSubscribe<T>() { // from class: com.polidea.rxandroidble2.internal.QueueOperation.1
            @Override // io.reactivex.ObservableOnSubscribe
            public void subscribe(ObservableEmitter<T> observableEmitter) throws Exception {
                try {
                    QueueOperation.this.protectedRun(observableEmitter, queueReleaseInterface);
                } catch (DeadObjectException e) {
                    observableEmitter.tryOnError(QueueOperation.this.provideException(e));
                } catch (Throwable th) {
                    observableEmitter.tryOnError(th);
                }
            }
        });
    }

    @Override // com.polidea.rxandroidble2.internal.operations.Operation
    public Priority definedPriority() {
        return Priority.NORMAL;
    }

    /* renamed from: compareTo  reason: avoid collision after fix types in other method */
    public int compareTo2(@NonNull Operation operation) {
        return operation.definedPriority().priority - definedPriority().priority;
    }
}
