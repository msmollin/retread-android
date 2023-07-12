package com.treadly.Treadly.UI.PushNotifications.Firebase;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import androidx.fragment.app.FragmentActivity;
import com.google.android.gms.measurement.AppMeasurement;
import com.treadly.Treadly.Data.Managers.TreadlyServiceManager;
import com.treadly.Treadly.UI.PushNotifications.Firebase.Actions.FcmPrivateVideoInviteAction;
import com.treadly.Treadly.UI.TreadlyVideo.Data.VideoServiceHelper;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/* loaded from: classes2.dex */
public class FirebaseNotificationManager {
    private static final FirebaseNotificationManager instance = new FirebaseNotificationManager();
    private Context context;
    String deviceToken;
    List<Map<String, String>> notificationArray = new ArrayList();

    private FirebaseNotificationManager() {
    }

    public static FirebaseNotificationManager getInstance() {
        return instance;
    }

    public void setDeviceToken(String str) {
        this.deviceToken = str;
    }

    public void queueNotificationResponse(Map<String, String> map) {
        Log.d(AppMeasurement.FCM_ORIGIN, "queueNotificationResponse");
        this.notificationArray.add(map);
    }

    public Map<String, String> popNotificationResponse() {
        if (this.notificationArray.isEmpty()) {
            return null;
        }
        return this.notificationArray.remove(0);
    }

    public void handleNotificationReceive(Map<String, String> map) {
        FirebaseNotificationCategory fromString = FirebaseNotificationCategory.fromString(map.get("type"));
        if (fromString == null) {
            fromString = FirebaseNotificationCategory.none;
        }
        switch (fromString) {
            case none:
            case privateVideoInvite:
            case publicVideoInvite:
            case groupRequest:
            case groupRequestApprove:
            case groupInvite:
            case groupInviteApprove:
            case groupVideo:
            case groupPost:
            case groupPostComment:
            case groupPostSubComment:
            case groupPostLike:
            case friendRequest:
            case groupScheduleEvent:
            case dailyGoal50:
            case dailyGoal100:
            case friendOnlineStatus:
            default:
                return;
        }
    }

    public void processResponse(Map<String, String> map, FragmentActivity fragmentActivity) {
        FirebaseNotificationCategory fromString = FirebaseNotificationCategory.fromString(map.get("type"));
        if (fromString == null) {
            fromString = FirebaseNotificationCategory.none;
        }
        Log.d(AppMeasurement.FCM_ORIGIN, "processResponse: " + fromString);
        switch (fromString) {
            case none:
            case publicVideoInvite:
            case groupRequest:
            case groupRequestApprove:
            case groupInvite:
            case groupInviteApprove:
            case groupVideo:
            case groupPost:
            case groupPostComment:
            case groupPostSubComment:
            case groupPostLike:
            case friendRequest:
            case groupScheduleEvent:
            case dailyGoal50:
            case dailyGoal100:
            case friendOnlineStatus:
            default:
                return;
            case privateVideoInvite:
                FcmPrivateVideoInviteAction.handleNotification(map, fragmentActivity);
                return;
        }
    }

    public void handleReceivedResponse(Map<String, String> map, FragmentActivity fragmentActivity) {
        String userId = TreadlyServiceManager.getInstance().getUserId();
        if (userId == null || userId.isEmpty()) {
            queueNotificationResponse(map);
        } else {
            processResponse(map, fragmentActivity);
        }
    }

    public void processResponses(final FragmentActivity fragmentActivity) {
        Log.d(AppMeasurement.FCM_ORIGIN, "in processResponse");
        while (true) {
            final Map<String, String> popNotificationResponse = popNotificationResponse();
            if (popNotificationResponse == null) {
                return;
            }
            runOnMain(new Runnable() { // from class: com.treadly.Treadly.UI.PushNotifications.Firebase.-$$Lambda$FirebaseNotificationManager$VsQR-MHwT8wmNjfZ1eYXp4inAPo
                @Override // java.lang.Runnable
                public final void run() {
                    FirebaseNotificationManager.this.processResponse(popNotificationResponse, fragmentActivity);
                }
            });
        }
    }

    public void registerDeviceToken() {
        if (this.deviceToken == null || this.deviceToken.isEmpty()) {
            return;
        }
        VideoServiceHelper.registerForNotification(this.deviceToken, null, false, new VideoServiceHelper.VideoResponseListener() { // from class: com.treadly.Treadly.UI.PushNotifications.Firebase.FirebaseNotificationManager.1
            @Override // com.treadly.Treadly.UI.TreadlyVideo.Data.VideoServiceHelper.VideoResponseListener
            public void onResponse(String str) {
                if (str == null) {
                    System.out.println("Successfully registered Firebase Token!");
                    return;
                }
                PrintStream printStream = System.out;
                printStream.println("Error registering Firebase token" + str);
            }
        });
    }

    public void unregisterDeviceToken() {
        VideoServiceHelper.unregisterForNotification(new VideoServiceHelper.VideoResponseListener() { // from class: com.treadly.Treadly.UI.PushNotifications.Firebase.FirebaseNotificationManager.2
            @Override // com.treadly.Treadly.UI.TreadlyVideo.Data.VideoServiceHelper.VideoResponseListener
            public void onResponse(String str) {
                if (str == null) {
                    System.out.println("Successfully unregistered Firebase Token!");
                    return;
                }
                PrintStream printStream = System.out;
                printStream.println("Error unregistering Firebase token " + str);
            }
        });
    }

    private void runOnMain(Runnable runnable) {
        new Handler(Looper.getMainLooper()).post(runnable);
    }
}
