package com.treadly.Treadly.Data.Managers;

import org.eclipse.paho.client.mqttv3.DisconnectedBufferOptions;
import org.eclipse.paho.client.mqttv3.IMqttActionListener;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.IMqttMessageListener;
import org.eclipse.paho.client.mqttv3.IMqttToken;
import org.eclipse.paho.client.mqttv3.MqttAsyncClient;
import org.eclipse.paho.client.mqttv3.MqttCallbackExtended;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;

/* loaded from: classes2.dex */
public class MqttConnection implements IMqttActionListener, MqttCallbackExtended, IMqttMessageListener {
    private static final String CLIENT_ID_BASE = "AndroidMqtt";
    private static final int MAX_INFLIGHT = 10;
    private MqttConnectionListener connectionListener;
    private final MqttCredentials credentials;
    private MqttDeliveryListener deliveryListener;
    private final MqttAsyncClient mqttClient;

    /* loaded from: classes2.dex */
    interface MqttConnectionListener {
        void connectionComplete(boolean z, String str);

        void connectionFailure(IMqttToken iMqttToken, Throwable th);

        void connectionLost(Throwable th);

        void connectionSuccess(IMqttToken iMqttToken);
    }

    /* loaded from: classes2.dex */
    interface MqttDeliveryListener {
        void deliveryComplete(IMqttDeliveryToken iMqttDeliveryToken);
    }

    /* loaded from: classes2.dex */
    interface MqttSubscriptionListener {
        void messageArrived(String str, MqttMessage mqttMessage);

        void subscriptionFailure(IMqttToken iMqttToken, Throwable th);

        void subscriptionSuccess(IMqttToken iMqttToken);
    }

    public MqttConnection(String str, MqttCredentials mqttCredentials) throws MqttException {
        this.credentials = mqttCredentials;
        this.mqttClient = new MqttAsyncClient(str, generateClientId(), null);
    }

    public IMqttToken connect() {
        try {
            this.mqttClient.setCallback(this);
            return this.mqttClient.connect(getConnectOptions(), null, this);
        } catch (MqttException e) {
            e.printStackTrace();
            return null;
        }
    }

    public void disconnect() {
        if (isConnected()) {
            try {
                this.mqttClient.disconnect();
            } catch (MqttException e) {
                e.printStackTrace();
            }
        }
    }

    public boolean isConnected() {
        return this.mqttClient.isConnected();
    }

    public void setMqttConnectionListener(MqttConnectionListener mqttConnectionListener) {
        this.connectionListener = mqttConnectionListener;
    }

    public void setDeliveryListener(MqttDeliveryListener mqttDeliveryListener) {
        this.deliveryListener = mqttDeliveryListener;
    }

    public void topicSubscribe(String str, final MqttSubscriptionListener mqttSubscriptionListener) {
        try {
            this.mqttClient.subscribe(str, 1, (Object) null, new IMqttActionListener() { // from class: com.treadly.Treadly.Data.Managers.MqttConnection.1
                @Override // org.eclipse.paho.client.mqttv3.IMqttActionListener
                public void onSuccess(IMqttToken iMqttToken) {
                    if (mqttSubscriptionListener != null) {
                        mqttSubscriptionListener.subscriptionSuccess(iMqttToken);
                    }
                }

                @Override // org.eclipse.paho.client.mqttv3.IMqttActionListener
                public void onFailure(IMqttToken iMqttToken, Throwable th) {
                    if (mqttSubscriptionListener != null) {
                        mqttSubscriptionListener.subscriptionFailure(iMqttToken, th);
                    }
                }
            });
            this.mqttClient.subscribe(str, 1, new IMqttMessageListener() { // from class: com.treadly.Treadly.Data.Managers.MqttConnection.2
                @Override // org.eclipse.paho.client.mqttv3.IMqttMessageListener
                public void messageArrived(String str2, MqttMessage mqttMessage) throws Exception {
                    if (mqttSubscriptionListener != null) {
                        mqttSubscriptionListener.messageArrived(str2, mqttMessage);
                    }
                }
            });
        } catch (MqttException e) {
            System.err.println("Exception whilst subscribing");
            e.printStackTrace();
        }
    }

    public IMqttToken topicUnsubscribe(String str) {
        try {
            return this.mqttClient.unsubscribe(str);
        } catch (MqttException e) {
            e.printStackTrace();
            return null;
        }
    }

    public IMqttToken publishMessage(String str, String str2) {
        try {
            MqttMessage mqttMessage = new MqttMessage();
            mqttMessage.setPayload(str2.getBytes());
            return this.mqttClient.publish(str, mqttMessage);
        } catch (MqttException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override // org.eclipse.paho.client.mqttv3.IMqttActionListener
    public void onSuccess(IMqttToken iMqttToken) {
        this.mqttClient.setBufferOpts(getDisconnectedBufferOptions());
        if (this.connectionListener != null) {
            this.connectionListener.connectionSuccess(iMqttToken);
        }
    }

    @Override // org.eclipse.paho.client.mqttv3.IMqttActionListener
    public void onFailure(IMqttToken iMqttToken, Throwable th) {
        if (this.connectionListener != null) {
            this.connectionListener.connectionFailure(iMqttToken, th);
        }
    }

    @Override // org.eclipse.paho.client.mqttv3.MqttCallbackExtended
    public void connectComplete(boolean z, String str) {
        if (this.connectionListener != null) {
            this.connectionListener.connectionComplete(z, str);
        }
    }

    @Override // org.eclipse.paho.client.mqttv3.MqttCallback
    public void connectionLost(Throwable th) {
        if (this.connectionListener != null) {
            this.connectionListener.connectionLost(th);
        }
    }

    @Override // org.eclipse.paho.client.mqttv3.MqttCallback, org.eclipse.paho.client.mqttv3.IMqttMessageListener
    public void messageArrived(String str, MqttMessage mqttMessage) throws Exception {
        System.out.println("MqttConnection::messageArrived()");
    }

    @Override // org.eclipse.paho.client.mqttv3.MqttCallback
    public void deliveryComplete(IMqttDeliveryToken iMqttDeliveryToken) {
        if (this.deliveryListener != null) {
            this.deliveryListener.deliveryComplete(iMqttDeliveryToken);
        }
    }

    private MqttConnectOptions getConnectOptions() {
        MqttConnectOptions mqttConnectOptions = new MqttConnectOptions();
        mqttConnectOptions.setAutomaticReconnect(false);
        mqttConnectOptions.setCleanSession(true);
        mqttConnectOptions.setMaxInflight(10);
        if (this.credentials != null) {
            mqttConnectOptions.setUserName(this.credentials.username);
            mqttConnectOptions.setPassword(this.credentials.password.toCharArray());
        }
        return mqttConnectOptions;
    }

    private DisconnectedBufferOptions getDisconnectedBufferOptions() {
        DisconnectedBufferOptions disconnectedBufferOptions = new DisconnectedBufferOptions();
        disconnectedBufferOptions.setBufferEnabled(true);
        disconnectedBufferOptions.setBufferSize(100);
        disconnectedBufferOptions.setPersistBuffer(false);
        disconnectedBufferOptions.setDeleteOldestMessages(false);
        return disconnectedBufferOptions;
    }

    public String getClientId() {
        if (isConnected()) {
            return this.mqttClient.getClientId();
        }
        return null;
    }

    private static String generateClientId() {
        return CLIENT_ID_BASE + System.currentTimeMillis();
    }
}
