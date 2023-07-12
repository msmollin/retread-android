package com.bambuser.broadcaster;

import com.bambuser.broadcaster.RawPacket;
import java.nio.ByteBuffer;

/* loaded from: classes.dex */
final class MovinoPacket extends RawPacket {
    private static final int HEADERSIZE = 5;

    /* JADX INFO: Access modifiers changed from: package-private */
    public MovinoPacket(int i) {
        this(null, i, 0, null);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public MovinoPacket(int i, int i2) {
        this(null, i, i2, null);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public MovinoPacket(ByteBufferPool byteBufferPool, int i, int i2, RawPacket.Observer observer) {
        super(byteBufferPool, i2 + 5, observer);
        writeUint8(i);
        this.mData.position(5);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public MovinoPacket writeUint8(int i) {
        this.mData.put((byte) (i & 255));
        return this;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public MovinoPacket writeUint16(int i) {
        this.mData.putShort((short) (i & 65535));
        return this;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public MovinoPacket writeUint32(long j) {
        this.mData.putInt((int) (j & Movino.ONES_32));
        return this;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public MovinoPacket writeUint64(long j) {
        this.mData.putLong(j);
        return this;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public MovinoPacket writeReal64(double d) {
        this.mData.putDouble(d);
        return this;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public MovinoPacket writeReal32(float f) {
        this.mData.putFloat(f);
        return this;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // com.bambuser.broadcaster.RawPacket
    public MovinoPacket write(byte[] bArr) {
        return write(bArr, 0, bArr.length);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // com.bambuser.broadcaster.RawPacket
    public MovinoPacket write(byte[] bArr, int i, int i2) {
        this.mData.put(bArr, i, i2);
        return this;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // com.bambuser.broadcaster.RawPacket
    public MovinoPacket write(ByteBuffer byteBuffer) {
        this.mData.put(byteBuffer);
        return this;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public MovinoPacket writeBinString(byte[] bArr) {
        int length = bArr.length;
        if (length > 65535) {
            length = 65535;
        }
        writeUint16(length);
        return write(bArr, 0, length);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // com.bambuser.broadcaster.RawPacket
    public ByteBuffer getDataBuffer() {
        this.mData.putInt(1, this.mData.position() - 5);
        return this.mData;
    }

    int getType() {
        return this.mData.get(0) & 255;
    }
}
