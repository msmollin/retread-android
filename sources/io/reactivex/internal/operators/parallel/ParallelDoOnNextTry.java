package io.reactivex.internal.operators.parallel;

import io.reactivex.exceptions.CompositeException;
import io.reactivex.exceptions.Exceptions;
import io.reactivex.functions.BiFunction;
import io.reactivex.functions.Consumer;
import io.reactivex.internal.functions.ObjectHelper;
import io.reactivex.internal.fuseable.ConditionalSubscriber;
import io.reactivex.internal.subscriptions.SubscriptionHelper;
import io.reactivex.parallel.ParallelFailureHandling;
import io.reactivex.parallel.ParallelFlowable;
import io.reactivex.plugins.RxJavaPlugins;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

/* loaded from: classes2.dex */
public final class ParallelDoOnNextTry<T> extends ParallelFlowable<T> {
    final BiFunction<? super Long, ? super Throwable, ParallelFailureHandling> errorHandler;
    final Consumer<? super T> onNext;
    final ParallelFlowable<T> source;

    public ParallelDoOnNextTry(ParallelFlowable<T> parallelFlowable, Consumer<? super T> consumer, BiFunction<? super Long, ? super Throwable, ParallelFailureHandling> biFunction) {
        this.source = parallelFlowable;
        this.onNext = consumer;
        this.errorHandler = biFunction;
    }

    @Override // io.reactivex.parallel.ParallelFlowable
    public void subscribe(Subscriber<? super T>[] subscriberArr) {
        if (validate(subscriberArr)) {
            int length = subscriberArr.length;
            Subscriber<? super T>[] subscriberArr2 = new Subscriber[length];
            for (int i = 0; i < length; i++) {
                Subscriber<? super T> subscriber = subscriberArr[i];
                if (subscriber instanceof ConditionalSubscriber) {
                    subscriberArr2[i] = new ParallelDoOnNextConditionalSubscriber((ConditionalSubscriber) subscriber, this.onNext, this.errorHandler);
                } else {
                    subscriberArr2[i] = new ParallelDoOnNextSubscriber(subscriber, this.onNext, this.errorHandler);
                }
            }
            this.source.subscribe(subscriberArr2);
        }
    }

    @Override // io.reactivex.parallel.ParallelFlowable
    public int parallelism() {
        return this.source.parallelism();
    }

    /* loaded from: classes2.dex */
    static final class ParallelDoOnNextSubscriber<T> implements ConditionalSubscriber<T>, Subscription {
        final Subscriber<? super T> actual;
        boolean done;
        final BiFunction<? super Long, ? super Throwable, ParallelFailureHandling> errorHandler;
        final Consumer<? super T> onNext;
        Subscription s;

        ParallelDoOnNextSubscriber(Subscriber<? super T> subscriber, Consumer<? super T> consumer, BiFunction<? super Long, ? super Throwable, ParallelFailureHandling> biFunction) {
            this.actual = subscriber;
            this.onNext = consumer;
            this.errorHandler = biFunction;
        }

        @Override // org.reactivestreams.Subscription
        public void request(long j) {
            this.s.request(j);
        }

        @Override // org.reactivestreams.Subscription
        public void cancel() {
            this.s.cancel();
        }

        @Override // io.reactivex.FlowableSubscriber, org.reactivestreams.Subscriber
        public void onSubscribe(Subscription subscription) {
            if (SubscriptionHelper.validate(this.s, subscription)) {
                this.s = subscription;
                this.actual.onSubscribe(this);
            }
        }

        @Override // org.reactivestreams.Subscriber
        public void onNext(T t) {
            if (tryOnNext(t)) {
                return;
            }
            this.s.request(1L);
        }

        @Override // io.reactivex.internal.fuseable.ConditionalSubscriber
        public boolean tryOnNext(T t) {
            if (this.done) {
                return false;
            }
            long j = 0;
            while (true) {
                try {
                    this.onNext.accept(t);
                    this.actual.onNext(t);
                    return true;
                } catch (Throwable th) {
                    Exceptions.throwIfFatal(th);
                    try {
                        j++;
                        switch ((ParallelFailureHandling) ObjectHelper.requireNonNull(this.errorHandler.apply(Long.valueOf(j), th), "The errorHandler returned a null item")) {
                            case RETRY:
                                break;
                            case SKIP:
                                return false;
                            case STOP:
                                cancel();
                                onComplete();
                                return false;
                            default:
                                cancel();
                                onError(th);
                                return false;
                        }
                    } catch (Throwable th2) {
                        Exceptions.throwIfFatal(th2);
                        cancel();
                        onError(new CompositeException(th, th2));
                        return false;
                    }
                }
            }
        }

        @Override // org.reactivestreams.Subscriber
        public void onError(Throwable th) {
            if (this.done) {
                RxJavaPlugins.onError(th);
                return;
            }
            this.done = true;
            this.actual.onError(th);
        }

        @Override // org.reactivestreams.Subscriber
        public void onComplete() {
            if (this.done) {
                return;
            }
            this.done = true;
            this.actual.onComplete();
        }
    }

    /* loaded from: classes2.dex */
    static final class ParallelDoOnNextConditionalSubscriber<T> implements ConditionalSubscriber<T>, Subscription {
        final ConditionalSubscriber<? super T> actual;
        boolean done;
        final BiFunction<? super Long, ? super Throwable, ParallelFailureHandling> errorHandler;
        final Consumer<? super T> onNext;
        Subscription s;

        ParallelDoOnNextConditionalSubscriber(ConditionalSubscriber<? super T> conditionalSubscriber, Consumer<? super T> consumer, BiFunction<? super Long, ? super Throwable, ParallelFailureHandling> biFunction) {
            this.actual = conditionalSubscriber;
            this.onNext = consumer;
            this.errorHandler = biFunction;
        }

        @Override // org.reactivestreams.Subscription
        public void request(long j) {
            this.s.request(j);
        }

        @Override // org.reactivestreams.Subscription
        public void cancel() {
            this.s.cancel();
        }

        @Override // io.reactivex.FlowableSubscriber, org.reactivestreams.Subscriber
        public void onSubscribe(Subscription subscription) {
            if (SubscriptionHelper.validate(this.s, subscription)) {
                this.s = subscription;
                this.actual.onSubscribe(this);
            }
        }

        @Override // org.reactivestreams.Subscriber
        public void onNext(T t) {
            if (tryOnNext(t) || this.done) {
                return;
            }
            this.s.request(1L);
        }

        @Override // io.reactivex.internal.fuseable.ConditionalSubscriber
        public boolean tryOnNext(T t) {
            if (this.done) {
                return false;
            }
            long j = 0;
            while (true) {
                try {
                    this.onNext.accept(t);
                    return this.actual.tryOnNext(t);
                } catch (Throwable th) {
                    Exceptions.throwIfFatal(th);
                    try {
                        j++;
                        switch ((ParallelFailureHandling) ObjectHelper.requireNonNull(this.errorHandler.apply(Long.valueOf(j), th), "The errorHandler returned a null item")) {
                            case RETRY:
                                break;
                            case SKIP:
                                return false;
                            case STOP:
                                cancel();
                                onComplete();
                                return false;
                            default:
                                cancel();
                                onError(th);
                                return false;
                        }
                    } catch (Throwable th2) {
                        Exceptions.throwIfFatal(th2);
                        cancel();
                        onError(new CompositeException(th, th2));
                        return false;
                    }
                }
            }
        }

        @Override // org.reactivestreams.Subscriber
        public void onError(Throwable th) {
            if (this.done) {
                RxJavaPlugins.onError(th);
                return;
            }
            this.done = true;
            this.actual.onError(th);
        }

        @Override // org.reactivestreams.Subscriber
        public void onComplete() {
            if (this.done) {
                return;
            }
            this.done = true;
            this.actual.onComplete();
        }
    }
}
