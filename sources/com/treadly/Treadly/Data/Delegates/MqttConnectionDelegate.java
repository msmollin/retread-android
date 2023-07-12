package com.treadly.Treadly.Data.Delegates;

/* loaded from: classes.dex */
public interface MqttConnectionDelegate {
    void onConnectionFailed();

    void onConnectionLost();

    void onConnectionSucceeded();

    void onPublishMessageSent();

    void onReceiveMessage(String str, String str2);
}
