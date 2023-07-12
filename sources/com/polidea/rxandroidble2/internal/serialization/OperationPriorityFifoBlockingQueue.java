package com.polidea.rxandroidble2.internal.serialization;

import java.util.Iterator;
import java.util.concurrent.PriorityBlockingQueue;

/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: classes.dex */
public class OperationPriorityFifoBlockingQueue {
    private final PriorityBlockingQueue<FIFORunnableEntry> q = new PriorityBlockingQueue<>();

    public void add(FIFORunnableEntry fIFORunnableEntry) {
        this.q.add(fIFORunnableEntry);
    }

    public FIFORunnableEntry<?> take() throws InterruptedException {
        return this.q.take();
    }

    public FIFORunnableEntry<?> takeNow() {
        return this.q.poll();
    }

    public boolean isEmpty() {
        return this.q.isEmpty();
    }

    public boolean remove(FIFORunnableEntry fIFORunnableEntry) {
        Iterator<FIFORunnableEntry> it = this.q.iterator();
        while (it.hasNext()) {
            FIFORunnableEntry next = it.next();
            if (next == fIFORunnableEntry) {
                return this.q.remove(next);
            }
        }
        return false;
    }
}
