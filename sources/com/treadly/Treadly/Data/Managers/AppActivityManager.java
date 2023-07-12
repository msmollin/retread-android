package com.treadly.Treadly.Data.Managers;

import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import androidx.annotation.NonNull;
import com.treadly.Treadly.Data.Managers.AppActivityManager;
import com.treadly.Treadly.Data.Model.AppActivityInfo;
import com.treadly.Treadly.Data.Model.AppActivityType;
import com.treadly.Treadly.Data.Model.TreadlyNetworkResponse;
import com.treadly.Treadly.Data.Model.UserTokenInfo;
import com.treadly.Treadly.Data.Utility.RequestUtil;
import com.treadly.client.lib.sdk.Managers.TreadlyLogManager;
import com.treadly.client.lib.sdk.Model.ComponentInfo;
import com.treadly.client.lib.sdk.Model.VersionInfo;
import java.io.PrintStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;
import org.joda.time.DateTimeConstants;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/* loaded from: classes2.dex */
public class AppActivityManager {
    private static final String APP_ACTIVITY_GET_TODAY_ROUTE = "https://env-staging.treadly.co:8080/vendor/app/activity/v1/today";
    private static final String APP_ACTIVITY_LOG_ROUTE = "https://env-staging.treadly.co:8080/vendor/app/activity/v1/log";
    private static final String errorCouldNotConnectWithServer = "Could not connect with server";
    private static final String errorUserNotLoggedIn = "Error: No user currently logged in";
    private static final String keyActivityType = "activity_type";
    private static final String keyAppDevice = "app_device";
    private static final String keyAppVersion = "app_version";
    private static final String keyCreatedAt = "created_at";
    private static final String keyCustomboardVersion = "customboard_version";
    private static final String keyData = "data";
    private static final String keyDeviceAddress = "device_address";
    private static final String keyGenDescription = "gen_description";
    private static final String keyHeaderAuthorization = "Authorization";
    private static final String keyMainboardVersion = "mainboard_version";
    private static final String keyMotorControllerVersion = "motorcontroller_version";
    private static final String keyReferenceId = "reference_id";
    private static final String keyRemoteVersion = "remote_version";
    private static final String keyTimezoneOffset = "timezone_offset";
    private static final String keyUserId = "user_id";
    private static final String keyUserName = "user_name";
    private static final String keyUuid = "uuid";
    private static final String keyWatchVersion = "watch_version";
    public static final AppActivityManager shared = new AppActivityManager();
    public List<ComponentInfo> componentList = new ArrayList();

    /* loaded from: classes2.dex */
    public interface AppActivityManagerHasDailyGoalResponse {
        void onResponse(String str, boolean z);
    }

    /* loaded from: classes2.dex */
    public interface AppActivityManagerLogResponse {
        void onResponse(String str, List<AppActivityInfo> list);
    }

    /* loaded from: classes2.dex */
    public interface AppActivityManagerResponse {
        void onResponse(String str);
    }

    private static String getAppVersion() {
        return "1.1.8";
    }

    private AppActivityManager() {
    }

    public void clear() {
        this.componentList = new ArrayList();
    }

    public void handleAppLogin() {
        sendLogActivity(AppActivityType.appStart, null, null, null, null, null, null, getAppVersion(), TreadlyLogManager.shared.getUuid(), null, new AppActivityManagerResponse() { // from class: com.treadly.Treadly.Data.Managers.AppActivityManager.1
            @Override // com.treadly.Treadly.Data.Managers.AppActivityManager.AppActivityManagerResponse
            public void onResponse(String str) {
                PrintStream printStream = System.out;
                printStream.println("HandleAppLogin: " + str);
            }
        });
    }

    public void handleDeviceConnected() {
        sendAppActivity(AppActivityType.deviceConnected);
    }

    public void handleDeviceDisconnected() {
        sendAppActivity(AppActivityType.deviceDisconnected);
    }

    public void handleTabSelected(int i) {
        AppActivityType appActivityType;
        switch (i) {
            case 0:
                appActivityType = AppActivityType.homeTabSelected;
                break;
            case 1:
                appActivityType = AppActivityType.groupTabSelected;
                break;
            case 2:
                appActivityType = AppActivityType.connectTabSelected;
                break;
            case 3:
                appActivityType = AppActivityType.inboxTabSelected;
                break;
            case 4:
                appActivityType = AppActivityType.profileTabSelected;
                break;
            default:
                appActivityType = null;
                break;
        }
        if (appActivityType == null) {
            return;
        }
        sendAppActivity(appActivityType);
    }

    public void sendGroupCreatedSelected() {
        sendAppActivity(AppActivityType.createGroupSelected);
    }

    public void sendDailyGoal50Event() {
        sendAppActivity(AppActivityType.dailyGoal50);
    }

    public void sendDailyGoal100Event() {
        sendAppActivity(AppActivityType.dailyGoal100);
    }

    public void sendDailyGoalUpdatedEvent(AppActivityManagerResponse appActivityManagerResponse) {
        sendAppActivity(AppActivityType.dailyGoalUpdated, appActivityManagerResponse);
    }

    private void sendAppActivity(AppActivityType appActivityType) {
        sendAppActivity(appActivityType, new AppActivityManagerResponse() { // from class: com.treadly.Treadly.Data.Managers.-$$Lambda$AppActivityManager$nl_BM9_GVxGyZ7TcgBOTGCNAfJE
            @Override // com.treadly.Treadly.Data.Managers.AppActivityManager.AppActivityManagerResponse
            public final void onResponse(String str) {
                AppActivityManager.lambda$sendAppActivity$0(str);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static /* synthetic */ void lambda$sendAppActivity$0(String str) {
        if (str != null) {
            PrintStream printStream = System.out;
            printStream.println("sendAppActivity: " + str);
        }
    }

    private void sendAppActivity(AppActivityType appActivityType, AppActivityManagerResponse appActivityManagerResponse) {
        String str = null;
        String str2 = null;
        String str3 = null;
        String str4 = null;
        String str5 = null;
        String str6 = null;
        String str7 = null;
        for (ComponentInfo componentInfo : this.componentList) {
            if (componentInfo != null) {
                switch (componentInfo.getType()) {
                    case bleBoard:
                        str2 = componentInfo.getVersionInfo().getVersion();
                        str = componentInfo.getId();
                        str7 = getGenDescription(componentInfo);
                        continue;
                    case mainBoard:
                        str3 = componentInfo.getVersionInfo().getVersion();
                        continue;
                    case motorController:
                        str4 = componentInfo.getVersionInfo().getVersion();
                        continue;
                    case watch:
                        str5 = componentInfo.getVersionInfo().getVersion();
                        continue;
                    case remote:
                        str6 = componentInfo.getVersionInfo().getVersion();
                        continue;
                }
            }
        }
        sendLogActivity(appActivityType, str, str2, str3, str4, str5, str6, getAppVersion(), TreadlyLogManager.shared.getUuid(), str7, appActivityManagerResponse);
    }

    private void sendLogActivity(AppActivityType appActivityType, String str, String str2, String str3, String str4, String str5, String str6, String str7, String str8, String str9, final AppActivityManagerResponse appActivityManagerResponse) {
        try {
            JSONObject jSONObject = new JSONObject();
            if (appActivityType != null) {
                jSONObject.put(keyActivityType, appActivityType.getValue());
            }
            if (str != null) {
                jSONObject.put(keyDeviceAddress, str);
            }
            if (str2 != null) {
                jSONObject.put(keyCustomboardVersion, str2);
            }
            if (str3 != null) {
                jSONObject.put(keyMainboardVersion, str3);
            }
            if (str4 != null) {
                jSONObject.put(keyMotorControllerVersion, str4);
            }
            if (str5 != null) {
                jSONObject.put(keyWatchVersion, str5);
            }
            if (str6 != null) {
                jSONObject.put(keyRemoteVersion, str6);
            }
            if (str7 != null) {
                jSONObject.put(keyAppVersion, str7);
            }
            if (str8 != null) {
                jSONObject.put(keyUuid, str8);
            }
            if (str9 != null) {
                jSONObject.put(keyGenDescription, str9);
            }
            jSONObject.put(keyAppDevice, getAppDevice());
            JSONObject jSONObject2 = new JSONObject();
            UserTokenInfo userTokenInfo = TreadlyServiceManager.getInstance().tokenInfo;
            if (userTokenInfo != null && userTokenInfo.token != null) {
                jSONObject2.put(keyHeaderAuthorization, userTokenInfo.token);
            }
            RequestUtil.shared.postJson(APP_ACTIVITY_LOG_ROUTE, jSONObject, jSONObject2, new RequestUtil.RequestUtilListener() { // from class: com.treadly.Treadly.Data.Managers.-$$Lambda$AppActivityManager$tbKnqw19JUmFsK7GQ7H4GgmGGPs
                @Override // com.treadly.Treadly.Data.Utility.RequestUtil.RequestUtilListener
                public final void onResponse(TreadlyNetworkResponse treadlyNetworkResponse) {
                    AppActivityManager.lambda$sendLogActivity$3(AppActivityManager.AppActivityManagerResponse.this, treadlyNetworkResponse);
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
            runOnMain(new Runnable() { // from class: com.treadly.Treadly.Data.Managers.-$$Lambda$AppActivityManager$4osSYeVxsTvVRur89L6-6eqdS4Y
                @Override // java.lang.Runnable
                public final void run() {
                    AppActivityManager.AppActivityManagerResponse.this.onResponse("Error");
                }
            });
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static /* synthetic */ void lambda$sendLogActivity$3(final AppActivityManagerResponse appActivityManagerResponse, TreadlyNetworkResponse treadlyNetworkResponse) throws JSONException {
        if (!treadlyNetworkResponse.ok() || treadlyNetworkResponse.response == null) {
            runOnMain(new Runnable() { // from class: com.treadly.Treadly.Data.Managers.-$$Lambda$AppActivityManager$aughXfWMImKV0XfO3U5xKL-oRlE
                @Override // java.lang.Runnable
                public final void run() {
                    AppActivityManager.AppActivityManagerResponse.this.onResponse(AppActivityManager.errorCouldNotConnectWithServer);
                }
            });
            return;
        }
        final boolean equals = treadlyNetworkResponse.response.optString("status").equals("ok");
        runOnMain(new Runnable() { // from class: com.treadly.Treadly.Data.Managers.-$$Lambda$AppActivityManager$V4fnlTu06oqgchGblON2SXimekU
            @Override // java.lang.Runnable
            public final void run() {
                AppActivityManager.AppActivityManagerResponse appActivityManagerResponse2 = AppActivityManager.AppActivityManagerResponse.this;
                boolean z = equals;
                appActivityManagerResponse2.onResponse(r1 ? null : "Error");
            }
        });
    }

    @NonNull
    public JSONObject getAppActivityJsonPayload() {
        return getAppActivityJsonPayload(null);
    }

    @NonNull
    public JSONObject getAppActivityJsonPayload(AppActivityType appActivityType) {
        ComponentInfo componentInfo = null;
        ComponentInfo componentInfo2 = null;
        ComponentInfo componentInfo3 = null;
        ComponentInfo componentInfo4 = null;
        ComponentInfo componentInfo5 = null;
        for (ComponentInfo componentInfo6 : this.componentList) {
            if (componentInfo6 != null) {
                switch (componentInfo6.getType()) {
                    case bleBoard:
                        componentInfo = componentInfo6;
                        continue;
                    case mainBoard:
                        componentInfo2 = componentInfo6;
                        continue;
                    case motorController:
                        componentInfo3 = componentInfo6;
                        continue;
                    case watch:
                        componentInfo4 = componentInfo6;
                        continue;
                    case remote:
                        componentInfo5 = componentInfo6;
                        continue;
                }
            }
        }
        try {
            JSONObject jSONObject = new JSONObject();
            if (appActivityType != null) {
                jSONObject.put(keyActivityType, appActivityType);
            }
            if (componentInfo != null) {
                jSONObject.put(keyDeviceAddress, componentInfo.getId());
                jSONObject.put(keyCustomboardVersion, componentInfo.getVersionInfo().getVersion());
                jSONObject.put(keyGenDescription, getGenDescription(componentInfo));
            }
            if (componentInfo2 != null) {
                jSONObject.put(keyMainboardVersion, componentInfo2.getVersionInfo().getVersion());
            }
            if (componentInfo3 != null) {
                jSONObject.put(keyMotorControllerVersion, componentInfo3.getVersionInfo().getVersion());
            }
            if (componentInfo4 != null) {
                jSONObject.put(keyWatchVersion, componentInfo4.getVersionInfo().getVersion());
            }
            if (componentInfo5 != null) {
                jSONObject.put(keyRemoteVersion, componentInfo5.getVersionInfo().getVersion());
            }
            jSONObject.put(keyAppVersion, getAppVersion());
            jSONObject.put(keyUuid, TreadlyLogManager.shared.getUuid());
            jSONObject.put(keyAppDevice, getAppDevice());
            return jSONObject;
        } catch (Exception e) {
            e.printStackTrace();
            return new JSONObject();
        }
    }

    static void runOnMain(Runnable runnable) {
        new Handler(Looper.getMainLooper()).post(runnable);
    }

    public void hasDailyGoal50(final AppActivityManagerHasDailyGoalResponse appActivityManagerHasDailyGoalResponse) {
        getAppActivityLogs(AppActivityType.dailyGoal50, new AppActivityManagerLogResponse() { // from class: com.treadly.Treadly.Data.Managers.AppActivityManager.2
            @Override // com.treadly.Treadly.Data.Managers.AppActivityManager.AppActivityManagerLogResponse
            public void onResponse(String str, final List<AppActivityInfo> list) {
                if (str == null && list != null) {
                    AppActivityManager.runOnMain(new Runnable() { // from class: com.treadly.Treadly.Data.Managers.AppActivityManager.2.1
                        @Override // java.lang.Runnable
                        public void run() {
                            appActivityManagerHasDailyGoalResponse.onResponse(null, list.size() > 0);
                        }
                    });
                } else {
                    AppActivityManager.runOnMain(new Runnable() { // from class: com.treadly.Treadly.Data.Managers.AppActivityManager.2.2
                        @Override // java.lang.Runnable
                        public void run() {
                            appActivityManagerHasDailyGoalResponse.onResponse("Error", false);
                        }
                    });
                }
            }
        });
    }

    public void hasDailyGoal100(final AppActivityManagerHasDailyGoalResponse appActivityManagerHasDailyGoalResponse) {
        getAppActivityLogs(AppActivityType.dailyGoal100, new AppActivityManagerLogResponse() { // from class: com.treadly.Treadly.Data.Managers.AppActivityManager.3
            @Override // com.treadly.Treadly.Data.Managers.AppActivityManager.AppActivityManagerLogResponse
            public void onResponse(String str, final List<AppActivityInfo> list) {
                if (str == null && list != null) {
                    AppActivityManager.runOnMain(new Runnable() { // from class: com.treadly.Treadly.Data.Managers.AppActivityManager.3.1
                        @Override // java.lang.Runnable
                        public void run() {
                            appActivityManagerHasDailyGoalResponse.onResponse(null, list.size() > 0);
                        }
                    });
                } else {
                    AppActivityManager.runOnMain(new Runnable() { // from class: com.treadly.Treadly.Data.Managers.AppActivityManager.3.2
                        @Override // java.lang.Runnable
                        public void run() {
                            appActivityManagerHasDailyGoalResponse.onResponse("Error", false);
                        }
                    });
                }
            }
        });
    }

    private boolean hasDailyGoal(List<AppActivityInfo> list, List<AppActivityInfo> list2) {
        if (list == null || list2 == null) {
            return false;
        }
        List<AppActivityInfo> sortList = sortList(list);
        List<AppActivityInfo> sortList2 = sortList(list2);
        if (sortList.size() <= 0 || sortList2.size() <= 0) {
            return sortList2.size() > 0;
        }
        AppActivityInfo appActivityInfo = sortList.get(sortList.size() - 1);
        AppActivityInfo appActivityInfo2 = sortList2.get(sortList2.size() - 1);
        return appActivityInfo2 == null || appActivityInfo == null || appActivityInfo.createdAt == null || appActivityInfo2.createdAt == null || appActivityInfo.createdAt.compareTo(appActivityInfo2.createdAt) < 0;
    }

    private List<AppActivityInfo> sortList(List<AppActivityInfo> list) {
        ArrayList arrayList = new ArrayList(list);
        Collections.sort(arrayList, new Comparator<AppActivityInfo>() { // from class: com.treadly.Treadly.Data.Managers.AppActivityManager.4
            @Override // java.util.Comparator
            public int compare(AppActivityInfo appActivityInfo, AppActivityInfo appActivityInfo2) {
                if (appActivityInfo.createdAt == null || appActivityInfo2.createdAt == null || appActivityInfo.createdAt.getTime() == appActivityInfo2.createdAt.getTime()) {
                    return 0;
                }
                return appActivityInfo.createdAt.getTime() > appActivityInfo2.createdAt.getTime() ? 1 : -1;
            }
        });
        return arrayList;
    }

    private static void getAppActivityLogs(AppActivityType appActivityType, final AppActivityManagerLogResponse appActivityManagerLogResponse) {
        try {
            JSONObject jSONObject = new JSONObject();
            jSONObject.put(keyActivityType, appActivityType.getValue());
            jSONObject.put(keyTimezoneOffset, getTimezoneOffset());
            JSONObject jSONObject2 = new JSONObject();
            UserTokenInfo userTokenInfo = TreadlyServiceManager.getInstance().tokenInfo;
            if (userTokenInfo != null && userTokenInfo.token != null) {
                jSONObject2.put(keyHeaderAuthorization, userTokenInfo.token);
            }
            RequestUtil.shared.postJson(APP_ACTIVITY_GET_TODAY_ROUTE, jSONObject, jSONObject2, new RequestUtil.RequestUtilListener() { // from class: com.treadly.Treadly.Data.Managers.AppActivityManager.5
                @Override // com.treadly.Treadly.Data.Utility.RequestUtil.RequestUtilListener
                public void onResponse(TreadlyNetworkResponse treadlyNetworkResponse) throws JSONException {
                    if (treadlyNetworkResponse.ok() && treadlyNetworkResponse.response != null) {
                        final List parseAppActivityLogs = AppActivityManager.parseAppActivityLogs(treadlyNetworkResponse.response);
                        AppActivityManager.runOnMain(new Runnable() { // from class: com.treadly.Treadly.Data.Managers.AppActivityManager.5.2
                            @Override // java.lang.Runnable
                            public void run() {
                                AppActivityManagerLogResponse.this.onResponse(parseAppActivityLogs != null ? null : "Error", parseAppActivityLogs);
                            }
                        });
                        return;
                    }
                    AppActivityManager.runOnMain(new Runnable() { // from class: com.treadly.Treadly.Data.Managers.AppActivityManager.5.1
                        @Override // java.lang.Runnable
                        public void run() {
                            AppActivityManagerLogResponse.this.onResponse(AppActivityManager.errorCouldNotConnectWithServer, null);
                        }
                    });
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
            runOnMain(new Runnable() { // from class: com.treadly.Treadly.Data.Managers.AppActivityManager.6
                @Override // java.lang.Runnable
                public void run() {
                    AppActivityManagerLogResponse.this.onResponse("Error", null);
                }
            });
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static List<AppActivityInfo> parseAppActivityLogs(JSONObject jSONObject) {
        if (jSONObject == null) {
            return null;
        }
        try {
            JSONArray optJSONArray = jSONObject.optJSONArray("data");
            if (optJSONArray == null) {
                return null;
            }
            ArrayList arrayList = new ArrayList();
            for (int i = 0; i < optJSONArray.length(); i++) {
                JSONObject jSONObject2 = optJSONArray.getJSONObject(i);
                AppActivityType fromValue = AppActivityType.fromValue(jSONObject2.optInt(keyActivityType, -1));
                String optString = jSONObject2.optString(keyCreatedAt);
                if (fromValue != null && optString != null) {
                    arrayList.add(new AppActivityInfo(fromValue, getDateFromTimestamp(optString, "yyyy-MM-dd HH:mm:ss"), jSONObject2.optString(keyDeviceAddress), jSONObject2.optString(keyCustomboardVersion), jSONObject2.optString(keyMainboardVersion), jSONObject2.optString(keyMotorControllerVersion), jSONObject2.optString(keyWatchVersion), jSONObject2.optString(keyRemoteVersion), jSONObject2.optString(keyAppVersion), jSONObject2.optString(keyUuid), jSONObject2.optString(keyGenDescription), jSONObject2.optInt(keyReferenceId, -1)));
                }
            }
            return arrayList;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private static String getGenDescription(ComponentInfo componentInfo) {
        if (componentInfo == null) {
            return null;
        }
        return isGen3(componentInfo) ? "Gen3" : isGen2(componentInfo) ? "Gen2" : "Gen1";
    }

    private static boolean isGen3(ComponentInfo componentInfo) {
        if (componentInfo == null || componentInfo.getVersionInfo() == null) {
            return false;
        }
        VersionInfo versionInfo = new VersionInfo(3, 0, 0);
        return componentInfo.getVersionInfo().isGreaterThan(versionInfo) || componentInfo.getVersionInfo().isEqual(versionInfo);
    }

    private static boolean isGen2(ComponentInfo componentInfo) {
        if (componentInfo == null || componentInfo.getVersionInfo() == null) {
            return false;
        }
        VersionInfo versionInfo = new VersionInfo(2, 0, 0);
        return componentInfo.getVersionInfo().isGreaterThan(versionInfo) || componentInfo.getVersionInfo().isEqual(versionInfo);
    }

    private static String getAppDevice() {
        return Build.MANUFACTURER + " " + Build.MODEL + " : " + Build.VERSION.RELEASE;
    }

    private static Date getDateFromTimestamp(String str, String str2) {
        if (str == null || str2 == null) {
            return null;
        }
        try {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat(str2, Locale.getDefault());
            simpleDateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
            return simpleDateFormat.parse(str);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private static int getTimezoneOffset() {
        return getLocaleTimeZone(TimeZone.getDefault());
    }

    private static int getLocaleTimeZone(TimeZone timeZone) {
        return timeZone.getRawOffset() / DateTimeConstants.MILLIS_PER_HOUR;
    }
}
