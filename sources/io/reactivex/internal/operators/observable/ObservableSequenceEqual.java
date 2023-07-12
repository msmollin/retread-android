package io.reactivex.internal.operators.observable;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import io.reactivex.exceptions.Exceptions;
import io.reactivex.functions.BiPredicate;
import io.reactivex.internal.disposables.ArrayCompositeDisposable;
import io.reactivex.internal.queue.SpscLinkedArrayQueue;
import java.util.concurrent.atomic.AtomicInteger;

/* loaded from: classes2.dex */
public final class ObservableSequenceEqual<T> extends Observable<Boolean> {
    final int bufferSize;
    final BiPredicate<? super T, ? super T> comparer;
    final ObservableSource<? extends T> first;
    final ObservableSource<? extends T> second;

    public ObservableSequenceEqual(ObservableSource<? extends T> observableSource, ObservableSource<? extends T> observableSource2, BiPredicate<? super T, ? super T> biPredicate, int i) {
        this.first = observableSource;
        this.second = observableSource2;
        this.comparer = biPredicate;
        this.bufferSize = i;
    }

    @Override // io.reactivex.Observable
    public void subscribeActual(Observer<? super Boolean> observer) {
        EqualCoordinator equalCoordinator = new EqualCoordinator(observer, this.bufferSize, this.first, this.second, this.comparer);
        observer.onSubscribe(equalCoordinator);
        equalCoordinator.subscribe();
    }

    /* loaded from: classes2.dex */
    static final class EqualCoordinator<T> extends AtomicInteger implements Disposable {
        private static final long serialVersionUID = -6178010334400373240L;
        final Observer<? super Boolean> actual;
        volatile boolean cancelled;
        final BiPredicate<? super T, ? super T> comparer;
        final ObservableSource<? extends T> first;
        final EqualObserver<T>[] observers;
        final ArrayCompositeDisposable resources;
        final ObservableSource<? extends T> second;
        T v1;
        T v2;

        EqualCoordinator(Observer<? super Boolean> observer, int i, ObservableSource<? extends T> observableSource, ObservableSource<? extends T> observableSource2, BiPredicate<? super T, ? super T> biPredicate) {
            this.actual = observer;
            this.first = observableSource;
            this.second = observableSource2;
            this.comparer = biPredicate;
            this.observers = r3;
            EqualObserver<T>[] equalObserverArr = {new EqualObserver<>(this, 0, i), new EqualObserver<>(this, 1, i)};
            this.resources = new ArrayCompositeDisposable(2);
        }

        boolean setDisposable(Disposable disposable, int i) {
            return this.resources.setResource(i, disposable);
        }

        void subscribe() {
            EqualObserver<T>[] equalObserverArr = this.observers;
            this.first.subscribe(equalObserverArr[0]);
            this.second.subscribe(equalObserverArr[1]);
        }

        @Override // io.reactivex.disposables.Disposable
        public void dispose() {
            if (this.cancelled) {
                return;
            }
            this.cancelled = true;
            this.resources.dispose();
            if (getAndIncrement() == 0) {
                EqualObserver<T>[] equalObserverArr = this.observers;
                equalObserverArr[0].queue.clear();
                equalObserverArr[1].queue.clear();
            }
        }

        @Override // io.reactivex.disposables.Disposable
        public boolean isDisposed() {
            return this.cancelled;
        }

        void cancel(SpscLinkedArrayQueue<T> spscLinkedArrayQueue, SpscLinkedArrayQueue<T> spscLinkedArrayQueue2) {
            this.cancelled = true;
            spscLinkedArrayQueue.clear();
            spscLinkedArrayQueue2.clear();
        }

        void drain() {
            Throwable th;
            Throwable th2;
            if (getAndIncrement() != 0) {
                return;
            }
            EqualObserver<T>[] equalObserverArr = this.observers;
            EqualObserver<T> equalObserver = equalObserverArr[0];
            SpscLinkedArrayQueue<T> spscLinkedArrayQueue = equalObserver.queue;
            EqualObserver<T> equalObserver2 = equalObserverArr[1];
            SpscLinkedArrayQueue<T> spscLinkedArrayQueue2 = equalObserver2.queue;
            int i = 1;
            while (!this.cancelled) {
                boolean z = equalObserver.done;
                if (z && (th2 = equalObserver.error) != null) {
                    cancel(spscLinkedArrayQueue, spscLinkedArrayQueue2);
                    this.actual.onError(th2);
                    return;
                }
                boolean z2 = equalObserver2.done;
                if (z2 && (th = equalObserver2.error) != null) {
                    cancel(spscLinkedArrayQueue, spscLinkedArrayQueue2);
                    this.actual.onError(th);
                    return;
                }
                if (this.v1 == null) {
                    this.v1 = spscLinkedArrayQueue.poll();
                }
                boolean z3 = this.v1 == null;
                if (this.v2 == null) {
                    this.v2 = spscLinkedArrayQueue2.poll();
                }
                boolean z4 = this.v2 == null;
                if (z && z2 && z3 && z4) {
                    this.actual.onNext(true);
                    this.actual.onComplete();
                    return;
                } else if (z && z2 && z3 != z4) {
                    cancel(spscLinkedArrayQueue, spscLinkedArrayQueue2);
                    this.actual.onNext(false);
                    this.actual.onComplete();
                    return;
                } else {
                    if (!z3 && !z4) {
                        try {
                            if (!this.comparer.test((T) this.v1, (T) this.v2)) {
                                cancel(spscLinkedArrayQueue, spscLinkedArrayQueue2);
                                this.actual.onNext(false);
                                this.actual.onComplete();
                                return;
                            }
                            this.v1 = null;
                            this.v2 = null;
                        } catch (Throwable th3) {
                            Exceptions.throwIfFatal(th3);
                            cancel(spscLinkedArrayQueue, spscLinkedArrayQueue2);
                            this.actual.onError(th3);
                            return;
                        }
                    }
                    if (z3 || z4) {
                        i = addAndGet(-i);
                        if (i == 0) {
                            return;
                        }
                    }
                }
            }
            spscLinkedArrayQueue.clear();
            spscLinkedArrayQueue2.clear();
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes2.dex */
    public static final class EqualObserver<T> implements Observer<T> {
        volatile boolean done;
        Throwable error;
        final int index;
        final EqualCoordinator<T> parent;
        final SpscLinkedArrayQueue<T> queue;

        EqualObserver(EqualCoordinator<T> equalCoordinator, int i, int i2) {
            this.parent = equalCoordinator;
            this.index = i;
            this.queue = new SpscLinkedArrayQueue<>(i2);
        }

        @Override // io.reactivex.Observer
        public void onSubscribe(Disposable disposable) {
            this.parent.setDisposable(disposable, this.index);
        }

        @Override // io.reactivex.Observer
        public void onNext(T t) {
            this.queue.offer(t);
            this.parent.drain();
        }

        @Override // io.reactivex.Observer
        public void onError(Throwable th) {
            this.error = th;
            this.done = true;
            this.parent.drain();
        }

        @Override // io.reactivex.Observer
        public void onComplete() {
            this.done = true;
            this.parent.drain();
        }
    }
}
