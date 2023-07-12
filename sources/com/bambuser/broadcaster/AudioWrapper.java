package com.bambuser.broadcaster;

/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: classes.dex */
public final class AudioWrapper {
    volatile byte[] mBuffer;
    volatile int mIndex = 0;
    volatile long mTimestamp;

    /* JADX INFO: Access modifiers changed from: package-private */
    public AudioWrapper(byte[] bArr) {
        this.mBuffer = bArr;
    }
}
