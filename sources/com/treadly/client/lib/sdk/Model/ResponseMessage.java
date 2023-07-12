package com.treadly.client.lib.sdk.Model;

/* loaded from: classes2.dex */
public class ResponseMessage {
    public byte cmd;
    public byte msgId;
    public byte[] payload;
    public byte status;

    public ResponseMessage(byte b, byte b2, byte b3, byte[] bArr) {
        this.cmd = b;
        this.status = b2;
        this.msgId = b3;
        this.payload = bArr;
    }

    public int getCmd() {
        return this.cmd & 255;
    }

    public int getStatus() {
        return this.status & 255;
    }

    public int getMsgId() {
        return this.msgId & 255;
    }

    public int[] getPayload() {
        if (this.payload == null) {
            return null;
        }
        int[] iArr = new int[this.payload.length];
        for (int i = 0; i < this.payload.length; i++) {
            iArr[i] = this.payload[i] & 255;
        }
        return iArr;
    }
}
