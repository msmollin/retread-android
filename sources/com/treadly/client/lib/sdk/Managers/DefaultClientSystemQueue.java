package com.treadly.client.lib.sdk.Managers;

import com.treadly.client.lib.sdk.Interfaces.ClientSystemQueue;
import java.util.Comparator;
import java.util.PriorityQueue;
import java.util.concurrent.DelayQueue;
import java.util.concurrent.Delayed;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;

/* loaded from: classes2.dex */
public class DefaultClientSystemQueue implements ClientSystemQueue {
    private static int ERROR = -1;
    private final DelayQueue<DelayedRunnable> delayQueue = new DelayQueue<>();
    private AtomicLong indexCounter = new AtomicLong(0);
    private volatile Thread backgroundThread = new Thread() { // from class: com.treadly.client.lib.sdk.Managers.DefaultClientSystemQueue.1
        PriorityQueue<DelayedRunnable> pQueue;

        {
            this.pQueue = new PriorityQueue<>(10, new DelayedRunnablePriorityComparator());
        }

        @Override // java.lang.Thread, java.lang.Runnable
        public void run() {
            while (DefaultClientSystemQueue.this.backgroundThread != null) {
                try {
                    this.pQueue.add((DelayedRunnable) DefaultClientSystemQueue.this.delayQueue.take());
                    while (true) {
                        DelayedRunnable delayedRunnable = (DelayedRunnable) DefaultClientSystemQueue.this.delayQueue.poll();
                        if (delayedRunnable == null) {
                            break;
                        }
                        this.pQueue.add(delayedRunnable);
                    }
                    while (true) {
                        DelayedRunnable poll = this.pQueue.poll();
                        if (poll != null) {
                            poll.run();
                        }
                    }
                } catch (InterruptedException unused) {
                }
            }
        }
    };

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes2.dex */
    public class DelayedRunnable implements Runnable, Delayed {
        private final long expiration;
        private final long priority;
        private final Runnable runnable;

        public DelayedRunnable(Runnable runnable, int i) {
            this.runnable = runnable;
            this.expiration = System.currentTimeMillis() + i;
            this.priority = DefaultClientSystemQueue.this.getNextIndex();
        }

        @Override // java.lang.Comparable
        public int compareTo(Delayed delayed) {
            try {
                return Long.valueOf(getDelay(TimeUnit.MILLISECONDS)).compareTo(Long.valueOf(delayed.getDelay(TimeUnit.MILLISECONDS)));
            } catch (Exception unused) {
                return DefaultClientSystemQueue.ERROR;
            }
        }

        @Override // java.util.concurrent.Delayed
        public long getDelay(TimeUnit timeUnit) {
            try {
                return timeUnit.convert(this.expiration - System.currentTimeMillis(), TimeUnit.MILLISECONDS);
            } catch (Exception unused) {
                return DefaultClientSystemQueue.ERROR;
            }
        }

        @Override // java.lang.Runnable
        public void run() {
            this.runnable.run();
        }

        public long getPriority() {
            return this.priority;
        }
    }

    /* loaded from: classes2.dex */
    private class DelayedRunnablePriorityComparator implements Comparator<DelayedRunnable> {
        private DelayedRunnablePriorityComparator() {
        }

        @Override // java.util.Comparator
        public int compare(DelayedRunnable delayedRunnable, DelayedRunnable delayedRunnable2) {
            try {
                return Long.valueOf(delayedRunnable.getPriority()).compareTo(Long.valueOf(delayedRunnable2.getPriority()));
            } catch (Exception unused) {
                return DefaultClientSystemQueue.ERROR;
            }
        }
    }

    public DefaultClientSystemQueue() {
        this.backgroundThread.start();
    }

    @Override // com.treadly.client.lib.sdk.Interfaces.ClientSystemQueue
    public void post(Runnable runnable) {
        postDelayed(runnable, 0);
    }

    @Override // com.treadly.client.lib.sdk.Interfaces.ClientSystemQueue
    public void postDelayed(Runnable runnable, int i) {
        this.delayQueue.put((DelayQueue<DelayedRunnable>) new DelayedRunnable(runnable, i));
    }

    @Override // com.treadly.client.lib.sdk.Interfaces.ClientSystemQueue
    public void stop() {
        Thread thread = this.backgroundThread;
        this.backgroundThread = null;
        if (thread != null) {
            thread.interrupt();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public long getNextIndex() {
        return this.indexCounter.getAndIncrement();
    }
}
