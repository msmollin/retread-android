package com.bambuser.broadcaster;

import android.os.SystemClock;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.atomic.AtomicInteger;

/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: classes.dex */
public final class QualityRater extends TimerTask {
    private static final int PERIOD = 250;
    private long mDroppedBytes;
    private final int[] mDroppedWindow;
    private long mHandledBytes;
    private final int[] mHandledWindow;
    private final AtomicInteger mLatestQuality;
    private final int mNumSamples;
    private volatile Observer mObserver;
    private final AtomicInteger mRecentlyDropped;
    private final AtomicInteger mRecentlyHandled;
    private final long mStartTime;
    private final Timer mTimer;
    private int mWindowPos;

    /* loaded from: classes.dex */
    interface Observer {
        void onQualityEvaluated(int i, int i2);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public QualityRater() {
        this(5);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public QualityRater(int i) {
        this.mObserver = null;
        this.mTimer = new Timer("QualityRaterTimer");
        this.mStartTime = SystemClock.uptimeMillis();
        this.mRecentlyDropped = new AtomicInteger();
        this.mRecentlyHandled = new AtomicInteger();
        this.mLatestQuality = new AtomicInteger(100);
        this.mDroppedBytes = 0L;
        this.mHandledBytes = 0L;
        this.mWindowPos = 0;
        this.mNumSamples = (i * 1000) / 250;
        this.mDroppedWindow = new int[this.mNumSamples];
        this.mHandledWindow = new int[this.mNumSamples];
        this.mTimer.scheduleAtFixedRate(this, 250L, 250L);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void stop() {
        this.mObserver = null;
        this.mTimer.cancel();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void setObserver(Observer observer) {
        this.mObserver = observer;
    }

    @Override // java.util.TimerTask, java.lang.Runnable
    public void run() {
        int i;
        this.mDroppedBytes -= this.mDroppedWindow[this.mWindowPos];
        this.mHandledBytes -= this.mHandledWindow[this.mWindowPos];
        this.mDroppedWindow[this.mWindowPos] = this.mRecentlyDropped.getAndSet(0);
        this.mHandledWindow[this.mWindowPos] = this.mRecentlyHandled.getAndSet(0);
        this.mDroppedBytes += this.mDroppedWindow[this.mWindowPos];
        long j = this.mHandledBytes;
        int[] iArr = this.mHandledWindow;
        this.mWindowPos = this.mWindowPos + 1;
        this.mHandledBytes = j + iArr[i];
        this.mWindowPos %= this.mNumSamples;
        long j2 = this.mDroppedBytes + this.mHandledBytes;
        int i2 = j2 > 0 ? (int) ((this.mHandledBytes * 100) / j2) : 100;
        this.mLatestQuality.set(i2);
        Observer observer = this.mObserver;
        if (observer != null) {
            observer.onQualityEvaluated(i2, getSeconds());
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void addBytes(int i, boolean z) {
        if (z) {
            this.mRecentlyHandled.addAndGet(i);
        } else {
            this.mRecentlyDropped.addAndGet(i);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public int getSeconds() {
        return (int) ((SystemClock.uptimeMillis() - this.mStartTime) / 1000);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public int getLatestQuality() {
        return this.mLatestQuality.get();
    }
}
