package io.reactivex.internal.operators.observable;

import io.reactivex.ObservableSource;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import io.reactivex.exceptions.Exceptions;
import io.reactivex.internal.disposables.DisposableHelper;
import io.reactivex.internal.disposables.EmptyDisposable;
import io.reactivex.internal.functions.Functions;
import io.reactivex.internal.functions.ObjectHelper;
import java.util.Collection;
import java.util.concurrent.Callable;

/* loaded from: classes2.dex */
public final class ObservableToList<T, U extends Collection<? super T>> extends AbstractObservableWithUpstream<T, U> {
    final Callable<U> collectionSupplier;

    public ObservableToList(ObservableSource<T> observableSource, int i) {
        super(observableSource);
        this.collectionSupplier = Functions.createArrayList(i);
    }

    public ObservableToList(ObservableSource<T> observableSource, Callable<U> callable) {
        super(observableSource);
        this.collectionSupplier = callable;
    }

    @Override // io.reactivex.Observable
    public void subscribeActual(Observer<? super U> observer) {
        try {
            this.source.subscribe(new ToListObserver(observer, (Collection) ObjectHelper.requireNonNull(this.collectionSupplier.call(), "The collectionSupplier returned a null collection. Null values are generally not allowed in 2.x operators and sources.")));
        } catch (Throwable th) {
            Exceptions.throwIfFatal(th);
            EmptyDisposable.error(th, observer);
        }
    }

    /* loaded from: classes2.dex */
    static final class ToListObserver<T, U extends Collection<? super T>> implements Observer<T>, Disposable {
        final Observer<? super U> actual;
        U collection;
        Disposable s;

        ToListObserver(Observer<? super U> observer, U u) {
            this.actual = observer;
            this.collection = u;
        }

        @Override // io.reactivex.Observer
        public void onSubscribe(Disposable disposable) {
            if (DisposableHelper.validate(this.s, disposable)) {
                this.s = disposable;
                this.actual.onSubscribe(this);
            }
        }

        @Override // io.reactivex.disposables.Disposable
        public void dispose() {
            this.s.dispose();
        }

        @Override // io.reactivex.disposables.Disposable
        public boolean isDisposed() {
            return this.s.isDisposed();
        }

        @Override // io.reactivex.Observer
        public void onNext(T t) {
            this.collection.add(t);
        }

        @Override // io.reactivex.Observer
        public void onError(Throwable th) {
            this.collection = null;
            this.actual.onError(th);
        }

        @Override // io.reactivex.Observer
        public void onComplete() {
            U u = this.collection;
            this.collection = null;
            this.actual.onNext(u);
            this.actual.onComplete();
        }
    }
}
