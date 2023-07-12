package com.treadly.Treadly.UI.PushNotifications.Firebase;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Build;
import android.util.Log;
import com.google.android.gms.measurement.AppMeasurement;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.treadly.Treadly.MainActivity;
import com.treadly.Treadly.R;
import java.util.Map;

/* loaded from: classes2.dex */
public class TreadlyFirebaseMessagingService extends FirebaseMessagingService {
    private static final String TAG = "FCM Service";
    private static final String TREADLY_CHANNEL_ID = "fcm_default_channel";
    private static final String TREADLY_CHANNEL_NAME = "Treadly";
    private Notification.Builder notification;
    private Integer notificationId = null;
    private NotificationManager notificationManager;
    private PendingIntent pendingIntent;

    @Override // android.app.Service
    public void onCreate() {
        super.onCreate();
        Log.d("notification", "onCreate");
        Log.d("notification", "onCreateEnd");
    }

    private void setupNotification() {
        this.notificationManager = (NotificationManager) getSystemService("notification");
        if (Build.VERSION.SDK_INT >= 26) {
            this.notificationManager.createNotificationChannel(new NotificationChannel(TREADLY_CHANNEL_ID, TREADLY_CHANNEL_NAME, 4));
        }
    }

    @Override // com.google.firebase.messaging.FirebaseMessagingService
    public void onMessageReceived(RemoteMessage remoteMessage) {
        Log.d(AppMeasurement.FCM_ORIGIN, "onMessageReceived begin");
        Log.d(AppMeasurement.FCM_ORIGIN, "onMessageReceived end");
    }

    @Override // com.google.firebase.messaging.FirebaseMessagingService
    public void onDeletedMessages() {
        super.onDeletedMessages();
    }

    private void displayNotification(RemoteMessage remoteMessage) {
        Log.d(AppMeasurement.FCM_ORIGIN, "display notification");
        String clickAction = remoteMessage.getNotification().getClickAction();
        if (clickAction == null) {
            return;
        }
        if (clickAction.equals(FirebaseNotificationCategory.none.name())) {
            this.notificationId = 0;
        }
        if (this.notificationId == null) {
            return;
        }
        this.notification = createNotification();
        this.pendingIntent = createIntent(remoteMessage);
        this.notification.setContentTitle(remoteMessage.getNotification().getTitle());
        this.notification.setContentText(remoteMessage.getNotification().getBody());
        this.notification.setContentIntent(this.pendingIntent);
        this.notificationManager.notify(this.notificationId.intValue(), this.notification.build());
    }

    private PendingIntent createIntent(RemoteMessage remoteMessage) {
        Intent intent = new Intent(this, MainActivity.class);
        intent.setFlags(268566528);
        for (Map.Entry<String, String> entry : remoteMessage.getData().entrySet()) {
            intent.putExtra(entry.getKey(), entry.getValue());
        }
        return PendingIntent.getActivity(this, 0, intent, 134217728);
    }

    private Notification.Builder createNotification() {
        return new Notification.Builder(getBaseContext(), TREADLY_CHANNEL_ID).setAutoCancel(true).setSmallIcon(R.mipmap.treadly_app_icon);
    }
}
