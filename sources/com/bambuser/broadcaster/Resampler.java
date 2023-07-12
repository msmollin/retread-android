package com.bambuser.broadcaster;

import android.util.Log;

/* loaded from: classes.dex */
final class Resampler {
    private static final String LOGTAG = "Resampler";
    private final int mChannels;
    private final int mInputRate;
    private final int mOutputRate;
    private long privData;

    /* JADX INFO: Access modifiers changed from: package-private */
    public native void close();

    native int enableDelay();

    /* JADX INFO: Access modifiers changed from: package-private */
    public native int getBytesConsumed();

    native void init(int i, int i2, int i3);

    /* JADX INFO: Access modifiers changed from: package-private */
    public native int resample(byte[] bArr, int i, int i2, byte[] bArr2, int i3, boolean z);

    static {
        try {
            Class.forName("com.bambuser.broadcaster.NativeUtils");
        } catch (ClassNotFoundException e) {
            Log.w(LOGTAG, "ClassNotFoundException", e);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public Resampler(int i, int i2) {
        this.privData = 0L;
        this.mInputRate = i;
        this.mOutputRate = i2;
        this.mChannels = 1;
        init(i, i2, 1);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public Resampler(int i, int i2, int i3) {
        this.privData = 0L;
        this.mInputRate = i;
        this.mOutputRate = i2;
        this.mChannels = i3;
        init(i, i2, i3);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public int getInputRate() {
        return this.mInputRate;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public int getOutputRate() {
        return this.mOutputRate;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public int getChannels() {
        return this.mChannels;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public int resample(byte[] bArr, int i, int i2, byte[] bArr2, int i3) {
        return resample(bArr, i, i2, bArr2, i3, false);
    }

    protected void finalize() {
        close();
    }
}
