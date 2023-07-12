package com.treadly.client.lib.sdk.Managers;

import java.util.ArrayList;
import java.util.List;

/* loaded from: classes2.dex */
public class PacketizedMessage {
    public byte cmd;
    public byte group;
    public List<Byte> payloadList = new ArrayList();
    public byte status;

    public PacketizedMessage(byte b, byte b2, byte[] bArr, byte b3) {
        this.cmd = b;
        this.status = b2;
        if (bArr != null) {
            for (byte b4 : bArr) {
                this.payloadList.add(Byte.valueOf(b4));
            }
        }
        this.group = b3;
    }

    public byte[] getPayload() {
        byte[] bArr = new byte[this.payloadList.size()];
        for (int i = 0; i < this.payloadList.size(); i++) {
            bArr[i] = this.payloadList.get(i).byteValue();
        }
        return bArr;
    }
}
