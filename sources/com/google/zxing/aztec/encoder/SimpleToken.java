package com.google.zxing.aztec.encoder;

import com.google.zxing.common.BitArray;

/* loaded from: classes.dex */
final class SimpleToken extends Token {
    private final short bitCount;
    private final short value;

    /* JADX INFO: Access modifiers changed from: package-private */
    public SimpleToken(Token token, int i, int i2) {
        super(token);
        this.value = (short) i;
        this.bitCount = (short) i2;
    }

    @Override // com.google.zxing.aztec.encoder.Token
    void appendTo(BitArray bitArray, byte[] bArr) {
        bitArray.appendBits(this.value, this.bitCount);
    }

    public String toString() {
        int i = (this.value & ((1 << this.bitCount) - 1)) | (1 << this.bitCount);
        return "<" + Integer.toBinaryString((1 << this.bitCount) | i).substring(1) + '>';
    }
}
