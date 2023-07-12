package com.bambuser.broadcaster;

import com.facebook.imagepipeline.producers.HttpUrlConnectionNetworkFetcher;

/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: classes.dex */
public final class InfoFrame {
    volatile int mHeight;
    volatile int mPixelFormat;
    volatile int mRotation;
    volatile int mWidth;
    volatile long mTimestamp = 0;
    volatile int mIndex = 0;
    volatile int mFrameRate = HttpUrlConnectionNetworkFetcher.HTTP_DEFAULT_TIMEOUT;

    /* JADX INFO: Access modifiers changed from: package-private */
    public InfoFrame(int i, int i2, int i3, int i4) {
        this.mPixelFormat = i;
        this.mWidth = i2;
        this.mHeight = i3;
        this.mRotation = i4;
    }
}
