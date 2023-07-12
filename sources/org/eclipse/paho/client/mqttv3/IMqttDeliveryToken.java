package org.eclipse.paho.client.mqttv3;

/* loaded from: classes2.dex */
public interface IMqttDeliveryToken extends IMqttToken {
    MqttMessage getMessage() throws MqttException;
}
