package com.treadly.Treadly.Data.Utility.NotificationCenter;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import java.util.HashMap;
import java.util.Map;

/* loaded from: classes2.dex */
public class NotificationCenter {
    public static void addObserver(Context context, NotificationType notificationType, BroadcastReceiver broadcastReceiver) {
        LocalBroadcastManager.getInstance(context).registerReceiver(broadcastReceiver, new IntentFilter(notificationType.name()));
    }

    public static void removeObserver(Context context, BroadcastReceiver broadcastReceiver) {
        LocalBroadcastManager.getInstance(context).unregisterReceiver(broadcastReceiver);
    }

    public static void postNotification(Context context, NotificationType notificationType, HashMap<String, String> hashMap) {
        Intent intent = new Intent(notificationType.name());
        intent.putExtra("notification", notificationType.toString());
        for (Map.Entry<String, String> entry : hashMap.entrySet()) {
            intent.putExtra(entry.getKey(), entry.getValue());
        }
        LocalBroadcastManager.getInstance(context).sendBroadcast(intent);
    }
}
