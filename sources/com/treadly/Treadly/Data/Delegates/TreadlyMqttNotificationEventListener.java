package com.treadly.Treadly.Data.Delegates;

import org.json.JSONObject;

/* loaded from: classes.dex */
public interface TreadlyMqttNotificationEventListener {
    void onMqttNotificationReceived(String str, String str2, String str3, JSONObject jSONObject);
}
