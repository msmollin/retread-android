package com.polidea.rxandroidble2.internal.serialization;

import androidx.annotation.NonNull;
import com.polidea.rxandroidble2.internal.RxBleLog;
import com.polidea.rxandroidble2.internal.operations.Operation;
import io.reactivex.ObservableEmitter;
import io.reactivex.Observer;
import io.reactivex.Scheduler;
import io.reactivex.disposables.Disposable;
import java.util.concurrent.atomic.AtomicLong;

/* loaded from: classes.dex */
class FIFORunnableEntry<T> implements Comparable<FIFORunnableEntry> {
    private static final AtomicLong SEQUENCE = new AtomicLong(0);
    final Operation<T> operation;
    final ObservableEmitter<T> operationResultObserver;
    private final long seqNum = SEQUENCE.getAndIncrement();

    /* JADX INFO: Access modifiers changed from: package-private */
    public FIFORunnableEntry(Operation<T> operation, ObservableEmitter<T> observableEmitter) {
        this.operation = operation;
        this.operationResultObserver = observableEmitter;
    }

    @Override // java.lang.Comparable
    public int compareTo(@NonNull FIFORunnableEntry fIFORunnableEntry) {
        int compareTo = this.operation.compareTo(fIFORunnableEntry.operation);
        if (compareTo != 0 || fIFORunnableEntry.operation == this.operation) {
            return compareTo;
        }
        return this.seqNum < fIFORunnableEntry.seqNum ? -1 : 1;
    }

    public void run(QueueSemaphore queueSemaphore, Scheduler scheduler) {
        if (this.operationResultObserver.isDisposed()) {
            RxBleLog.d("The operation was about to be run but the observer had been already disposed: " + this.operation, new Object[0]);
            queueSemaphore.release();
            return;
        }
        this.operation.run(queueSemaphore).subscribeOn(scheduler).unsubscribeOn(scheduler).subscribe((Observer<T>) new Observer<T>() { // from class: com.polidea.rxandroidble2.internal.serialization.FIFORunnableEntry.1
            @Override // io.reactivex.Observer
            public void onSubscribe(Disposable disposable) {
                FIFORunnableEntry.this.operationResultObserver.setDisposable(disposable);
            }

            @Override // io.reactivex.Observer
            public void onNext(T t) {
                FIFORunnableEntry.this.operationResultObserver.onNext(t);
            }

            @Override // io.reactivex.Observer
            public void onError(Throwable th) {
                FIFORunnableEntry.this.operationResultObserver.tryOnError(th);
            }

            @Override // io.reactivex.Observer
            public void onComplete() {
                FIFORunnableEntry.this.operationResultObserver.onComplete();
            }
        });
    }
}
