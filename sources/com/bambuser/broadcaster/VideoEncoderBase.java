package com.bambuser.broadcaster;

import android.util.Log;
import java.nio.ByteBuffer;

/* loaded from: classes.dex */
abstract class VideoEncoderBase {
    /* JADX INFO: Access modifiers changed from: package-private */
    public abstract void close();

    /* JADX INFO: Access modifiers changed from: package-private */
    public abstract void encode(Frame frame, DataHandler dataHandler);

    /* JADX INFO: Access modifiers changed from: package-private */
    public void finish(DataHandler dataHandler) {
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void flush(DataHandler dataHandler) {
    }

    /* loaded from: classes.dex */
    static abstract class DataHandler {
        ByteBuffer mTempBuffer;

        /* JADX INFO: Access modifiers changed from: package-private */
        public abstract void onEncodedData(ByteBuffer byteBuffer, long j, int i, boolean z);

        /* JADX INFO: Access modifiers changed from: package-private */
        public DataHandler(ByteBuffer byteBuffer) {
            this.mTempBuffer = byteBuffer;
        }
    }

    /* loaded from: classes.dex */
    static final class TimeMap {
        private static final String LOGTAG = "TimeMap";
        private final long[] mKeys;
        private final int[] mRotations;
        private final int mSize;
        private final long[] mValues;
        private int mWindowPos = 0;

        /* JADX INFO: Access modifiers changed from: package-private */
        public TimeMap(int i) {
            this.mSize = i;
            this.mKeys = new long[this.mSize];
            this.mValues = new long[this.mSize];
            this.mRotations = new int[this.mSize];
        }

        void push(long j, long j2) {
            push(j, j2, 0);
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        public void push(long j, long j2, int i) {
            this.mKeys[this.mWindowPos] = j;
            this.mRotations[this.mWindowPos] = i;
            long[] jArr = this.mValues;
            int i2 = this.mWindowPos;
            this.mWindowPos = i2 + 1;
            jArr[i2] = j2;
            this.mWindowPos %= this.mSize;
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        public int findBestIndex(long j) {
            long j2 = 0;
            int i = 0;
            for (int i2 = (this.mWindowPos - 1) + this.mSize; i2 >= this.mWindowPos; i2--) {
                int i3 = i2 % this.mSize;
                if (this.mKeys[i3] == j) {
                    return i3;
                }
                if (this.mValues[i3] > j2 && this.mKeys[i3] < j) {
                    j2 = this.mValues[i3];
                    i = i3;
                }
            }
            Log.v(LOGTAG, "key was not in map, returning fallback position based on value " + j2);
            return i;
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        public long getTime(int i) {
            return this.mValues[i];
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        public int getRotation(int i) {
            return this.mRotations[i];
        }
    }
}
