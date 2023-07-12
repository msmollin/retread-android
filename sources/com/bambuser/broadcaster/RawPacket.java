package com.bambuser.broadcaster;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

/* loaded from: classes.dex */
class RawPacket {
    private final ByteBufferPool byteBufferPool;
    final ByteBuffer mData;
    final Observer observer;
    private boolean useConnectionQueueSize;

    /* loaded from: classes.dex */
    interface Observer {
        void onBlockEnqueued();

        void onBlockSent();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public RawPacket(ByteBufferPool byteBufferPool, int i, Observer observer) {
        this.mData = byteBufferPool != null ? byteBufferPool.getBuffer(i) : ByteBuffer.allocate(i);
        this.mData.order(ByteOrder.BIG_ENDIAN);
        this.observer = observer;
        this.byteBufferPool = byteBufferPool;
        this.useConnectionQueueSize = observer == null;
    }

    final RawPacket useConnectionQueueSize(boolean z) {
        this.useConnectionQueueSize = z;
        return this;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public RawPacket write(byte[] bArr) {
        return write(bArr, 0, bArr.length);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public RawPacket write(byte[] bArr, int i, int i2) {
        this.mData.put(bArr, i, i2);
        return this;
    }

    RawPacket write(ByteBuffer byteBuffer) {
        this.mData.put(byteBuffer);
        return this;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public final byte[] getData() {
        return getDataBuffer().array();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public ByteBuffer getDataBuffer() {
        return this.mData;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public int size() {
        return this.mData.position();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public Observer getObserver() {
        return this.observer;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void releaseToPool() {
        if (this.byteBufferPool != null) {
            this.byteBufferPool.add(this.mData);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean useConnectionQueueSize() {
        return this.useConnectionQueueSize;
    }
}
