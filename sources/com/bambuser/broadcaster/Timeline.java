package com.bambuser.broadcaster;

import java.util.concurrent.atomic.AtomicBoolean;

/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: classes.dex */
public final class Timeline {
    private long mMediaTimeMillis;
    private boolean mStarted;
    private long mSystemTimeNano;

    /* JADX INFO: Access modifiers changed from: package-private */
    public synchronized void setTime(long j, long j2, boolean z) {
        this.mSystemTimeNano = j;
        this.mMediaTimeMillis = j2;
        this.mStarted = z;
        notifyAll();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public synchronized long getMediaTimeMillis() {
        if (!this.mStarted) {
            return this.mMediaTimeMillis;
        }
        return this.mMediaTimeMillis + ((System.nanoTime() - this.mSystemTimeNano) / 1000000);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public synchronized boolean isStarted() {
        return this.mStarted;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public synchronized void setStarted(boolean z) {
        if (this.mStarted && !z) {
            long nanoTime = System.nanoTime();
            this.mSystemTimeNano = nanoTime;
            this.mMediaTimeMillis += (nanoTime - this.mSystemTimeNano) / 1000000;
        }
        this.mStarted = z;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public synchronized long waitUntilMediaTime(long j, AtomicBoolean atomicBoolean) {
        while (!this.mStarted && !atomicBoolean.get()) {
            try {
                wait(100L);
            } catch (InterruptedException unused) {
            }
        }
        while (!atomicBoolean.get()) {
            long j2 = this.mSystemTimeNano + ((j - this.mMediaTimeMillis) * 1000000);
            long nanoTime = System.nanoTime();
            if (nanoTime >= j2) {
                return (nanoTime - j2) / 1000000;
            }
            long j3 = (j2 - nanoTime) / 1000000;
            if (j3 <= 0) {
                j3 = 1;
            }
            try {
                wait(j3);
            } catch (InterruptedException unused2) {
            }
        }
        return -1L;
    }
}
