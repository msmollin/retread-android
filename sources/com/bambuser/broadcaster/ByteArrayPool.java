package com.bambuser.broadcaster;

import java.lang.ref.SoftReference;
import java.util.ArrayList;

/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: classes.dex */
public final class ByteArrayPool {
    private final ArrayList<SoftReference<byte[]>> mBufferList;
    private final int mSize;

    /* JADX INFO: Access modifiers changed from: package-private */
    public ByteArrayPool() {
        this(10);
    }

    ByteArrayPool(int i) {
        this.mSize = i;
        this.mBufferList = new ArrayList<>(i);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public synchronized void add(byte[] bArr) {
        int i;
        if (bArr == null) {
            return;
        }
        int size = this.mBufferList.size();
        if (size < this.mSize) {
            this.mBufferList.add(new SoftReference<>(bArr));
            return;
        }
        while (i < size) {
            byte[] bArr2 = this.mBufferList.get(i).get();
            i = (bArr2 != null && bArr2.length >= bArr.length) ? i + 1 : 0;
            this.mBufferList.set(i, new SoftReference<>(bArr));
            return;
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public synchronized byte[] getBuffer(int i) {
        int i2 = 0;
        while (i2 < this.mBufferList.size()) {
            byte[] bArr = this.mBufferList.get(i2).get();
            if (bArr != null) {
                if (bArr.length >= i) {
                    this.mBufferList.remove(i2);
                    return bArr;
                }
                i2++;
            } else {
                this.mBufferList.remove(i2);
            }
        }
        return new byte[i];
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public synchronized void clear() {
        this.mBufferList.clear();
    }
}
