package com.treadly.Treadly.Data.Delegates;

import org.eclipse.paho.client.mqttv3.MqttToken;

/* loaded from: classes.dex */
public interface MqttConnectionManagerDelegate {
    void onPublishMessageSent(MqttToken mqttToken);

    void onReceiveMessage(String str, String str2);

    void prepareSubscriptions();
}
