package com.treadly.client.lib.sdk.Managers;

import com.treadly.client.lib.sdk.Model.ResponseMessage;
import java.util.ArrayList;

/* loaded from: classes2.dex */
public class Packetizer {
    public static final Packetizer shared = new Packetizer();
    public int currentGroup = 0;
    public int nextGroup = 0;
    public int groupCount = 0;
    public PacketizedMessage currentMessage = null;

    private Packetizer() {
    }

    public void initialize(ResponseMessage responseMessage, byte b) {
        this.currentMessage = new PacketizedMessage(responseMessage.cmd, responseMessage.status, responseMessage.payload, b);
        this.currentGroup = 0;
        this.nextGroup = 0;
    }

    public void write(ResponseMessage responseMessage) {
        if (checkMessage(responseMessage)) {
            for (byte b : responseMessage.payload) {
                this.currentMessage.payloadList.add(Byte.valueOf(b));
            }
            this.currentGroup = responseMessage.msgId;
            this.nextGroup = responseMessage.msgId + 1;
        }
    }

    private boolean checkMessage(ResponseMessage responseMessage) {
        if (responseMessage.msgId == 0) {
            reset();
            this.groupCount = Message.getGroupCount(responseMessage.cmd);
            this.currentMessage = new PacketizedMessage(responseMessage.cmd, responseMessage.status, responseMessage.payload, (byte) this.groupCount);
            this.currentGroup = 0;
            this.nextGroup = this.currentGroup + 1;
            return false;
        } else if (responseMessage.msgId != this.nextGroup) {
            reset();
            return false;
        } else {
            return true;
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public PacketizedMessage read() {
        return this.currentMessage;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public boolean isReady() {
        return (this.groupCount == 0 || this.nextGroup != this.groupCount || this.currentMessage == null) ? false : true;
    }

    private void reset() {
        this.currentGroup = 0;
        this.nextGroup = 0;
        this.groupCount = 0;
        this.currentMessage = null;
    }

    protected byte[][] packetize(PacketizedMessage packetizedMessage) {
        return packetize(packetizedMessage, false);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public byte[][] packetize(PacketizedMessage packetizedMessage, boolean z) {
        ArrayList arrayList = new ArrayList();
        for (int i = 0; i < packetizedMessage.group; i++) {
            byte[] bArr = new byte[Message.PAYLOAD_SIZE];
            System.arraycopy(packetizedMessage.getPayload(), Message.PAYLOAD_SIZE * i, bArr, 0, Message.PAYLOAD_SIZE);
            if (z && (bArr = AesManager.shared.encrypt(bArr)) == null) {
                bArr = new byte[Message.PAYLOAD_SIZE];
            }
            arrayList.add(Message.getBaseRequest(packetizedMessage.cmd, bArr, (byte) i));
        }
        byte[][] bArr2 = new byte[arrayList.size()];
        arrayList.toArray(bArr2);
        return bArr2;
    }
}
