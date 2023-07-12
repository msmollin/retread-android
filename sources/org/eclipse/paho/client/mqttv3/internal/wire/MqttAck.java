package org.eclipse.paho.client.mqttv3.internal.wire;

/* loaded from: classes2.dex */
public abstract class MqttAck extends MqttWireMessage {
    @Override // org.eclipse.paho.client.mqttv3.internal.wire.MqttWireMessage
    protected byte getMessageInfo() {
        return (byte) 0;
    }

    public MqttAck(byte b) {
        super(b);
    }

    @Override // org.eclipse.paho.client.mqttv3.internal.wire.MqttWireMessage
    public String toString() {
        StringBuffer stringBuffer = new StringBuffer(String.valueOf(super.toString()));
        stringBuffer.append(" msgId ");
        stringBuffer.append(this.msgId);
        return stringBuffer.toString();
    }
}
