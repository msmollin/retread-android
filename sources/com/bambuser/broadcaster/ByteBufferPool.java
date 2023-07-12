package com.bambuser.broadcaster;

import java.lang.ref.SoftReference;
import java.nio.ByteBuffer;
import java.util.ArrayList;

/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: classes.dex */
public final class ByteBufferPool {
    private final ArrayList<SoftReference<ByteBuffer>> mBufferList;
    private final int mSize;

    /* JADX INFO: Access modifiers changed from: package-private */
    public ByteBufferPool() {
        this(10);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public ByteBufferPool(int i) {
        this.mSize = i;
        this.mBufferList = new ArrayList<>(i);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public synchronized void add(ByteBuffer byteBuffer) {
        int i;
        if (byteBuffer == null) {
            return;
        }
        int size = this.mBufferList.size();
        if (size < this.mSize) {
            this.mBufferList.add(new SoftReference<>(byteBuffer));
            return;
        }
        while (i < size) {
            ByteBuffer byteBuffer2 = this.mBufferList.get(i).get();
            i = (byteBuffer2 != null && byteBuffer2.capacity() >= byteBuffer.capacity()) ? i + 1 : 0;
            this.mBufferList.set(i, new SoftReference<>(byteBuffer));
            return;
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public synchronized ByteBuffer getBuffer(int i) {
        int i2 = 0;
        while (i2 < this.mBufferList.size()) {
            ByteBuffer byteBuffer = this.mBufferList.get(i2).get();
            if (byteBuffer != null) {
                if (byteBuffer.capacity() >= i) {
                    this.mBufferList.remove(i2);
                    byteBuffer.clear();
                    return byteBuffer;
                }
                i2++;
            } else {
                this.mBufferList.remove(i2);
            }
        }
        return ByteBuffer.allocate(i);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public synchronized void clear() {
        this.mBufferList.clear();
    }
}
