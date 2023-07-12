package com.airbnb.lottie;

import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import androidx.annotation.Nullable;
import androidx.annotation.RestrictTo;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.FutureTask;

/* loaded from: classes.dex */
public class LottieTask<T> {
    public static Executor EXECUTOR = Executors.newCachedThreadPool();
    private final Set<LottieListener<Throwable>> failureListeners;
    private final Handler handler;
    @Nullable
    private volatile LottieResult<T> result;
    private final Set<LottieListener<T>> successListeners;
    private final FutureTask<LottieResult<T>> task;
    @Nullable
    private Thread taskObserver;

    @RestrictTo({RestrictTo.Scope.LIBRARY})
    public LottieTask(Callable<LottieResult<T>> callable) {
        this(callable, false);
    }

    @RestrictTo({RestrictTo.Scope.LIBRARY})
    LottieTask(Callable<LottieResult<T>> callable, boolean z) {
        this.successListeners = new LinkedHashSet(1);
        this.failureListeners = new LinkedHashSet(1);
        this.handler = new Handler(Looper.getMainLooper());
        this.result = null;
        this.task = new FutureTask<>(callable);
        if (z) {
            try {
                setResult(callable.call());
                return;
            } catch (Throwable th) {
                setResult(new LottieResult<>(th));
                return;
            }
        }
        EXECUTOR.execute(this.task);
        startTaskObserverIfNeeded();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void setResult(@Nullable LottieResult<T> lottieResult) {
        if (this.result != null) {
            throw new IllegalStateException("A task may only be set once.");
        }
        this.result = lottieResult;
        notifyListeners();
    }

    public synchronized LottieTask<T> addListener(LottieListener<T> lottieListener) {
        if (this.result != null && this.result.getValue() != null) {
            lottieListener.onResult(this.result.getValue());
        }
        this.successListeners.add(lottieListener);
        startTaskObserverIfNeeded();
        return this;
    }

    public synchronized LottieTask<T> removeListener(LottieListener<T> lottieListener) {
        this.successListeners.remove(lottieListener);
        stopTaskObserverIfNeeded();
        return this;
    }

    public synchronized LottieTask<T> addFailureListener(LottieListener<Throwable> lottieListener) {
        if (this.result != null && this.result.getException() != null) {
            lottieListener.onResult(this.result.getException());
        }
        this.failureListeners.add(lottieListener);
        startTaskObserverIfNeeded();
        return this;
    }

    public synchronized LottieTask<T> removeFailureListener(LottieListener<Throwable> lottieListener) {
        this.failureListeners.remove(lottieListener);
        stopTaskObserverIfNeeded();
        return this;
    }

    private void notifyListeners() {
        this.handler.post(new Runnable() { // from class: com.airbnb.lottie.LottieTask.1
            @Override // java.lang.Runnable
            public void run() {
                if (LottieTask.this.result == null || LottieTask.this.task.isCancelled()) {
                    return;
                }
                LottieResult lottieResult = LottieTask.this.result;
                if (lottieResult.getValue() != null) {
                    LottieTask.this.notifySuccessListeners(lottieResult.getValue());
                } else {
                    LottieTask.this.notifyFailureListeners(lottieResult.getException());
                }
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void notifySuccessListeners(T t) {
        for (LottieListener lottieListener : new ArrayList(this.successListeners)) {
            lottieListener.onResult(t);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void notifyFailureListeners(Throwable th) {
        ArrayList<LottieListener> arrayList = new ArrayList(this.failureListeners);
        if (arrayList.isEmpty()) {
            Log.w(L.TAG, "Lottie encountered an error but no failure listener was added.", th);
            return;
        }
        for (LottieListener lottieListener : arrayList) {
            lottieListener.onResult(th);
        }
    }

    private synchronized void startTaskObserverIfNeeded() {
        if (!taskObserverAlive() && this.result == null) {
            this.taskObserver = new Thread("LottieTaskObserver") { // from class: com.airbnb.lottie.LottieTask.2
                private boolean taskComplete = false;

                @Override // java.lang.Thread, java.lang.Runnable
                public void run() {
                    while (!isInterrupted() && !this.taskComplete) {
                        if (LottieTask.this.task.isDone()) {
                            try {
                                LottieTask.this.setResult((LottieResult) LottieTask.this.task.get());
                            } catch (InterruptedException | ExecutionException e) {
                                LottieTask.this.setResult(new LottieResult(e));
                            }
                            this.taskComplete = true;
                            LottieTask.this.stopTaskObserverIfNeeded();
                        }
                    }
                }
            };
            this.taskObserver.start();
            L.debug("Starting TaskObserver thread");
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public synchronized void stopTaskObserverIfNeeded() {
        if (taskObserverAlive()) {
            if (this.successListeners.isEmpty() || this.result != null) {
                this.taskObserver.interrupt();
                this.taskObserver = null;
                L.debug("Stopping TaskObserver thread");
            }
        }
    }

    private boolean taskObserverAlive() {
        return this.taskObserver != null && this.taskObserver.isAlive();
    }
}
