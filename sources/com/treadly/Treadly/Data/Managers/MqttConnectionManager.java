package com.treadly.Treadly.Data.Managers;

import android.os.Handler;
import android.os.Looper;
import com.treadly.Treadly.Data.Constants.NetworkConstants;
import com.treadly.Treadly.Data.Delegates.MqttConnectionManagerDelegate;
import com.treadly.Treadly.Data.Delegates.TreadlyServiceDelegate;
import com.treadly.Treadly.Data.Model.UserInfo;
import java.util.ArrayList;
import java.util.List;
import org.eclipse.paho.client.mqttv3.IMqttToken;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.json.JSONException;

/* loaded from: classes2.dex */
public class MqttConnectionManager implements TreadlyServiceDelegate {
    private static final MqttConnectionManager instance = new MqttConnectionManager();
    private MqttConnection connection;
    private MqttCredentials credentials;
    private String host = NetworkConstants.SERVICE_MQTT_SERVER_HOST;
    private int port = NetworkConstants.SERVICE_MQTT_SERVER_PORT;
    private String username = NetworkConstants.SERVICE_MQTT_SERVER_USERNAME;
    private String password = NetworkConstants.SERVICE_MQTT_SERVER_PASSWORD;
    private List<MqttConnectionManagerDelegate> delegates = new ArrayList();

    private void connect() throws MqttException {
    }

    private void disconnect() {
    }

    public String getClientId() {
        return "";
    }

    @Override // com.treadly.Treadly.Data.Delegates.TreadlyServiceDelegate
    public void onCreateFriendInviteToken(String str, String str2) {
    }

    @Override // com.treadly.Treadly.Data.Delegates.TreadlyServiceDelegate
    public void onUserFriendsUpdate(List<UserInfo> list) throws JSONException {
    }

    @Override // com.treadly.Treadly.Data.Delegates.TreadlyServiceDelegate
    public void onUserLogout(UserInfo userInfo) {
    }

    public IMqttToken publish(String str, String str2) {
        return null;
    }

    public void subscribe(String str) {
    }

    public void unsubscribe(String str) {
    }

    public static MqttConnectionManager getInstance() {
        return instance;
    }

    private MqttConnectionManager() {
        TreadlyServiceManager.getInstance().addDelegate(this);
    }

    static void runOnMain(Runnable runnable) {
        new Handler(Looper.getMainLooper()).post(runnable);
    }

    public void onMessageReceived(String str, MqttMessage mqttMessage) {
        sendOnMessageReceived(str, mqttMessage);
    }

    private void sendOnMessageReceived(String str, MqttMessage mqttMessage) {
        for (MqttConnectionManagerDelegate mqttConnectionManagerDelegate : this.delegates) {
            mqttConnectionManagerDelegate.onReceiveMessage(str, new String(mqttMessage.getPayload()));
        }
    }

    @Override // com.treadly.Treadly.Data.Delegates.TreadlyServiceDelegate
    public void onUserLogin(UserInfo userInfo) {
        try {
            connect();
        } catch (MqttException unused) {
        }
    }

    public void addDelegate(MqttConnectionManagerDelegate mqttConnectionManagerDelegate) {
        this.delegates.add(mqttConnectionManagerDelegate);
    }

    public void removeDelegate(MqttConnectionManagerDelegate mqttConnectionManagerDelegate) {
        for (MqttConnectionManagerDelegate mqttConnectionManagerDelegate2 : this.delegates) {
            if (mqttConnectionManagerDelegate2 == mqttConnectionManagerDelegate) {
                this.delegates.remove(mqttConnectionManagerDelegate2);
            }
        }
    }
}
