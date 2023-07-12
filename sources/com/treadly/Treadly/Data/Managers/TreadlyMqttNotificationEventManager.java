package com.treadly.Treadly.Data.Managers;

import android.util.Log;
import com.treadly.Treadly.Data.Delegates.TreadlyMqttNotificationEventListener;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.eclipse.paho.client.mqttv3.IMqttToken;
import org.json.JSONObject;

/* loaded from: classes2.dex */
public class TreadlyMqttNotificationEventManager {
    public String deviceToken = null;
    private static final TreadlyMqttNotificationEventManager instance = new TreadlyMqttNotificationEventManager();
    private static List<TreadlyMqttNotificationEventListener> delegates = new ArrayList();

    public void handleMessageSent(IMqttToken iMqttToken) {
    }

    public static TreadlyMqttNotificationEventManager getInstance() {
        return instance;
    }

    public void subscribe(String str) {
        TreadlyEventHelper.subscribeTopic(getMqttNotificationTopic(str));
        this.deviceToken = str;
    }

    public void unsubscribe(String str) {
        if (str == null) {
            return;
        }
        TreadlyEventHelper.unsubscribeTopic(getMqttNotificationTopic(str));
        this.deviceToken = str;
    }

    public void handlePrepareSubscriptions() {
        if (this.deviceToken == null) {
            return;
        }
        Log.d("notification", "subscription handled");
        TreadlyEventHelper.subscribeTopic(getMqttNotificationTopic(this.deviceToken));
    }

    public void handleMessage(String str, String str2) {
        if (TreadlyEventHelper.parseMessageId(str2).intValue() != 701) {
            return;
        }
        Log.d("notification", "message received");
        String parseMqttNotificationCategory = TreadlyEventHelper.parseMqttNotificationCategory(str2);
        JSONObject parseMqttNotificationPayload = TreadlyEventHelper.parseMqttNotificationPayload(str2);
        String parseMqttNotificationTitle = TreadlyEventHelper.parseMqttNotificationTitle(str2);
        if (parseMqttNotificationTitle == null) {
            parseMqttNotificationTitle = "";
        }
        String parseMqttNotificationBody = TreadlyEventHelper.parseMqttNotificationBody(str2);
        if (parseMqttNotificationBody == null) {
            parseMqttNotificationBody = "";
        }
        if (parseMqttNotificationCategory == null || parseMqttNotificationPayload == null) {
            return;
        }
        sendOnMqttNotificationReceived(parseMqttNotificationCategory, parseMqttNotificationTitle, parseMqttNotificationBody, parseMqttNotificationPayload);
    }

    public void addDelegate(TreadlyMqttNotificationEventListener treadlyMqttNotificationEventListener) {
        delegates.add(treadlyMqttNotificationEventListener);
    }

    public void removeDelegate(TreadlyMqttNotificationEventListener treadlyMqttNotificationEventListener) {
        Iterator<TreadlyMqttNotificationEventListener> it = delegates.iterator();
        while (it.hasNext()) {
            if (treadlyMqttNotificationEventListener == it.next()) {
                it.remove();
            }
        }
    }

    private void sendOnMqttNotificationReceived(String str, String str2, String str3, JSONObject jSONObject) {
        for (TreadlyMqttNotificationEventListener treadlyMqttNotificationEventListener : delegates) {
            treadlyMqttNotificationEventListener.onMqttNotificationReceived(str, str2, str3, jSONObject);
        }
    }

    private String getMqttNotificationTopic(String str) {
        return String.format("%s/%s/%s", TreadlyEventHelper.BASE_EVENT_TOPIC, TreadlyEventHelper.NOTIFICATION_EVENT_TOPIC, str);
    }

    private String getUserAllTopic() {
        return String.format("%s/%s/all", TreadlyEventHelper.BASE_EVENT_TOPIC, TreadlyEventHelper.NOTIFICATION_EVENT_TOPIC);
    }
}
