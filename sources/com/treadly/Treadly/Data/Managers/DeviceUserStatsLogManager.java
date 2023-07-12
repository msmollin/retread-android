package com.treadly.Treadly.Data.Managers;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.net.Uri;
import android.os.Handler;
import android.os.Looper;
import com.github.mikephil.charting.utils.Utils;
import com.treadly.Treadly.Data.Delegates.TreadlyServiceDelegate;
import com.treadly.Treadly.Data.Listeners.TreadlyServiceResponseEventAdapter;
import com.treadly.Treadly.Data.Model.UserInfo;
import com.treadly.Treadly.Data.Model.UserProfileInfo;
import com.treadly.Treadly.Data.Model.UserRunningSessionInfo;
import com.treadly.Treadly.Data.Model.UserRunningSessionParticipantInfo;
import com.treadly.Treadly.Data.Model.UserRunningSessionSegmentInfo;
import com.treadly.Treadly.MainActivity;
import com.treadly.Treadly.UI.TreadlyActivity.UnclaimedActivityList.TreadlyActivityUnclaimedListView;
import com.treadly.Treadly.UI.TreadlyDashboard.Tools.DeviceStatusVerification;
import com.treadly.Treadly.UI.TreadlyVideo.Data.VideoServiceHelper;
import com.treadly.Treadly.UI.Util.ActivityUtil;
import com.treadly.client.lib.sdk.Listeners.DeviceUserStatsLogEventListener;
import com.treadly.client.lib.sdk.Model.DeviceStatus;
import com.treadly.client.lib.sdk.Model.DeviceUserStatsLogInfo;
import com.treadly.client.lib.sdk.Model.DeviceUserStatsLogSegmentInfo;
import com.treadly.client.lib.sdk.Model.DeviceUserStatsUnclaimedLogInfo;
import com.treadly.client.lib.sdk.Model.DistanceUnits;
import com.treadly.client.lib.sdk.Model.VersionInfo;
import com.treadly.client.lib.sdk.TreadlyClientLib;
import java.io.File;
import java.io.PrintStream;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

/* loaded from: classes2.dex */
public class DeviceUserStatsLogManager implements TreadlyServiceDelegate, DeviceUserStatsLogEventListener {
    public static final int appStatsLoggingVersion = 100;
    public boolean isLogging;
    public TreadlyActivityUnclaimedListView listView;
    private static DeviceUserStatsLogManager instance = new DeviceUserStatsLogManager();
    public static final VersionInfo deviceStatsLoggingMinVersion = new VersionInfo(2, 14, 0);
    public static boolean isSingleUser = false;
    public static boolean singleUserChecked = false;
    public VersionInfo customBoardVersion = null;
    private int sequence = 0;
    private DeviceUserStatsLogInfo currentDeviceStatsLog = null;
    private List<DeviceUserStatsLogSegmentInfo> currentDeviceStatsSegments = new ArrayList();
    private Integer initialSteps = null;
    private Double initialDistance = null;
    private Date initialTimestamp = null;
    private Integer finalSteps = null;
    private Double finalDistance = null;
    private Date finalTimestamp = null;
    public int activityCounter = 0;
    public final int activityCounterMax = 10;
    private Timer activeLogRequestTimer = new Timer();
    public MainActivity activity = null;
    public Uri pendingActivityImage = null;
    public String pendingActivityComment = null;

    /* loaded from: classes2.dex */
    public interface DeviceUserStatsResultsListener {
        void onResults(String str);
    }

    double calculateBmr(int i, double d, double d2, boolean z) {
        double d3 = z ? (((d * 4.536d) + (d2 * 15.88d)) - (i * 5.0d)) + 5.0d : (((d * 4.536d) + (d2 * 15.88d)) - (i * 5.0d)) - 161.0d;
        return d3 >= Utils.DOUBLE_EPSILON ? d3 : Utils.DOUBLE_EPSILON;
    }

    double calculateMetsPerHourKmh(double d) {
        if (d <= 3.2d) {
            return 2.8d;
        }
        if (d <= 4.0d) {
            return 3.0d;
        }
        if (d <= 4.8d) {
            return 3.5d;
        }
        if (d <= 5.6d) {
            return 4.3d;
        }
        if (d <= 6.4d) {
            return 5.0d;
        }
        if (d <= 7.2d) {
            return 7.0d;
        }
        if (d <= 8.0d) {
            return 8.0d;
        }
        return Utils.DOUBLE_EPSILON;
    }

    double calculateMetsPerHourMph(double d) {
        if (d <= 2.0d) {
            return 2.8d;
        }
        if (d <= 2.5d) {
            return 3.0d;
        }
        if (d <= 3.0d) {
            return 3.5d;
        }
        if (d <= 3.5d) {
            return 4.3d;
        }
        if (d <= 4.0d) {
            return 5.0d;
        }
        if (d <= 4.5d) {
            return 7.0d;
        }
        if (d <= 5.0d) {
            return 8.0d;
        }
        return Utils.DOUBLE_EPSILON;
    }

    @Override // com.treadly.Treadly.Data.Delegates.TreadlyServiceDelegate
    public void onCreateFriendInviteToken(String str, String str2) {
    }

    @Override // com.treadly.client.lib.sdk.Listeners.DeviceUserStatsLogEventListener
    public void onDeviceUserStatsClaimUnclaimedUserStatsInfoResponse(boolean z) {
    }

    @Override // com.treadly.Treadly.Data.Delegates.TreadlyServiceDelegate
    public void onUserFriendsUpdate(List<UserInfo> list) {
    }

    public void reset() {
    }

    public void sendPendingActivity(DeviceUserStatsResultsListener deviceUserStatsResultsListener) {
    }

    public static DeviceUserStatsLogManager getInstance() {
        return instance;
    }

    private DeviceUserStatsLogManager() {
        TreadlyServiceManager.getInstance().addDelegate(this);
        TreadlyClientLib.shared.addDeviceUserStatsLogEventListener(this);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void processLogData(DeviceUserStatsLogInfo deviceUserStatsLogInfo) {
        processLogData(deviceUserStatsLogInfo, false, null);
    }

    private void processLogData(DeviceUserStatsLogInfo deviceUserStatsLogInfo, boolean z) {
        processLogData(deviceUserStatsLogInfo, z, null);
    }

    private void processLogData(final DeviceUserStatsLogInfo deviceUserStatsLogInfo, final boolean z, final String str) {
        String userId = TreadlyServiceManager.getInstance().getUserId();
        if (userId == null) {
            return;
        }
        TreadlyServiceManager.getInstance().getUserProfileInfo(userId, new TreadlyServiceResponseEventAdapter() { // from class: com.treadly.Treadly.Data.Managers.DeviceUserStatsLogManager.1
            @Override // com.treadly.Treadly.Data.Listeners.TreadlyServiceResponseEventAdapter, com.treadly.Treadly.Data.Listeners.TreadlyServiceResponseEventListener
            public void onUserProfile(String str2, UserProfileInfo userProfileInfo) {
                if (userProfileInfo != null) {
                    DeviceUserStatsLogManager.this.processLogDataWithUserProfile(deviceUserStatsLogInfo, userProfileInfo, z, str);
                }
            }
        });
    }

    public void processUnclaimedLogData(DeviceUserStatsUnclaimedLogInfo[] deviceUserStatsUnclaimedLogInfoArr) {
        if (deviceUserStatsUnclaimedLogInfoArr == null) {
            return;
        }
        showUnclaimedActivityList(deviceUserStatsUnclaimedLogInfoArr);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void processLogDataWithUserProfile(final DeviceUserStatsLogInfo deviceUserStatsLogInfo, UserProfileInfo userProfileInfo, final boolean z, final String str) {
        Date date;
        float f;
        char c;
        float f2 = deviceUserStatsLogInfo.finalDistance - deviceUserStatsLogInfo.initialDistance;
        int i = deviceUserStatsLogInfo.finalSteps - deviceUserStatsLogInfo.initialSteps;
        Date date2 = deviceUserStatsLogInfo.initialTimestamp;
        int length = deviceUserStatsLogInfo.segments.length;
        if (length > 0) {
            Date date3 = deviceUserStatsLogInfo.segments[length - 1].timestamp;
            char c2 = 0;
            float time = ((float) (date3.getTime() - deviceUserStatsLogInfo.initialTimestamp.getTime())) / 1000.0f;
            int i2 = 0;
            while (i2 < deviceUserStatsLogInfo.segments.length) {
                int i3 = i2 + 1;
                if (i3 < deviceUserStatsLogInfo.segments.length) {
                    DeviceUserStatsLogSegmentInfo deviceUserStatsLogSegmentInfo = deviceUserStatsLogInfo.segments[i2];
                    DeviceUserStatsLogSegmentInfo deviceUserStatsLogSegmentInfo2 = deviceUserStatsLogInfo.segments[i3];
                    if (deviceUserStatsLogSegmentInfo.speed != Utils.DOUBLE_EPSILON || deviceUserStatsLogSegmentInfo2.speed <= Utils.DOUBLE_EPSILON) {
                        c = 0;
                    } else {
                        c = 0;
                        time -= ((float) (deviceUserStatsLogSegmentInfo2.timestamp.getTime() - deviceUserStatsLogSegmentInfo.timestamp.getTime())) / 1000.0f;
                    }
                } else {
                    c = c2;
                }
                c2 = c;
                i2 = i3;
            }
            date = date3;
            f = time;
        } else {
            date = date2;
            f = 0.0f;
        }
        Date date4 = date;
        final UserRunningSessionInfo userRunningSessionInfo = new UserRunningSessionInfo(date4, calculateCalories(getAgeFromBirthYear(userProfileInfo.birthdateYear), userProfileInfo.height, userProfileInfo.weight, userProfileInfo.gender, f > 0.0f ? f2 / (f / 3600.0d) : 0.0d, (int) f, i, DistanceUnits.MI), f2, f, i);
        for (DeviceUserStatsLogSegmentInfo deviceUserStatsLogSegmentInfo3 : deviceUserStatsLogInfo.segments) {
            userRunningSessionInfo.segments.add(new UserRunningSessionSegmentInfo(deviceUserStatsLogSegmentInfo3.timestamp, 0, deviceUserStatsLogSegmentInfo3.speed));
        }
        userRunningSessionInfo.segments.add(new UserRunningSessionSegmentInfo(date4, i, Utils.DOUBLE_EPSILON));
        VideoServiceHelper.getRunningSessionParticipants(new Date(deviceUserStatsLogInfo.initialTimestamp.getTime()), new Date(date4.getTime()), new VideoServiceHelper.videoParticipantsListener() { // from class: com.treadly.Treadly.Data.Managers.DeviceUserStatsLogManager.2
            @Override // com.treadly.Treadly.UI.TreadlyVideo.Data.VideoServiceHelper.videoParticipantsListener
            public void onParticipantsInfoResponse(String str2, ArrayList<UserRunningSessionParticipantInfo> arrayList) {
                if (arrayList != null) {
                    DeviceUserStatsLogManager.this.saveRunningSession(userRunningSessionInfo, deviceUserStatsLogInfo, arrayList, z, str);
                }
            }
        });
    }

    private int getAgeFromBirthYear(int i) {
        return Calendar.getInstance().get(1) - i;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void saveRunningSession(UserRunningSessionInfo userRunningSessionInfo, final DeviceUserStatsLogInfo deviceUserStatsLogInfo, ArrayList<UserRunningSessionParticipantInfo> arrayList, final boolean z, String str) {
        TreadlyServiceManager.getInstance().sendDeviceRunningSession(userRunningSessionInfo, arrayList, str, new TreadlyServiceResponseEventAdapter() { // from class: com.treadly.Treadly.Data.Managers.DeviceUserStatsLogManager.3
            @Override // com.treadly.Treadly.Data.Listeners.TreadlyServiceResponseEventAdapter, com.treadly.Treadly.Data.Listeners.TreadlyServiceResponseEventListener
            public void onSuccess(String str2) {
                if (str2 == null) {
                    DeviceUserStatsLogManager.this.onSaveRunningSession(deviceUserStatsLogInfo, z);
                    RunningSessionManager.getInstance().checkDailyGoals();
                }
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void onSaveRunningSession(final DeviceUserStatsLogInfo deviceUserStatsLogInfo, final boolean z) {
        runOnMain(new Runnable() { // from class: com.treadly.Treadly.Data.Managers.-$$Lambda$DeviceUserStatsLogManager$Uj2g5DBg02V21k7PY6X44Pj07M0
            @Override // java.lang.Runnable
            public final void run() {
                DeviceUserStatsLogManager.lambda$onSaveRunningSession$0(z, deviceUserStatsLogInfo);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static /* synthetic */ void lambda$onSaveRunningSession$0(boolean z, DeviceUserStatsLogInfo deviceUserStatsLogInfo) {
        if (z) {
            return;
        }
        TreadlyClientLib.shared.removeDeviceUserStatsLog(deviceUserStatsLogInfo.logId);
    }

    int calculateCalories(int i, double d, double d2, String str, double d3, int i2, int i3, DistanceUnits distanceUnits) {
        if (i3 <= 0) {
            return 0;
        }
        double calculateBmr = ((calculateBmr(i, d2, d, str.equals("male")) * calculateMets(d3, distanceUnits, i2)) / 24.0d) * 1.0d;
        if (calculateBmr >= Utils.DOUBLE_EPSILON) {
            return (int) calculateBmr;
        }
        return 0;
    }

    double calculateMets(double d, DistanceUnits distanceUnits, int i) {
        return (i / 3600.0d) * (distanceUnits == DistanceUnits.MI ? calculateMetsPerHourMph(d) : calculateMetsPerHourKmh(d));
    }

    /* JADX INFO: Access modifiers changed from: private */
    public String convertToCompatibleDeviceUserId(String str) {
        if (str.length() == 24) {
            return str;
        }
        try {
            MessageDigest messageDigest = MessageDigest.getInstance("SHA-1");
            messageDigest.reset();
            messageDigest.update(str.getBytes("utf-8"));
            return String.format("%040x", new BigInteger(1, messageDigest.digest())).substring(0, 24);
        } catch (Exception e) {
            return e.toString();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void showUnclaimedActiveLogAlert() {
        if (TreadlyActivationManager.shared.hasActivatedDevice()) {
            showUnclaimedActiveLogAlert("New Activity", "New active run session available. Do you want to claim it?");
        }
    }

    public void clear() {
        clearLogging();
        this.customBoardVersion = null;
        this.activityCounter = 0;
    }

    @Override // com.treadly.Treadly.Data.Delegates.TreadlyServiceDelegate
    public void onUserLogin(final UserInfo userInfo) {
        runOnMain(new Runnable() { // from class: com.treadly.Treadly.Data.Managers.DeviceUserStatsLogManager.4
            @Override // java.lang.Runnable
            public void run() {
                TreadlyClientLib.shared.setDeviceUserStatsLogs(DeviceUserStatsLogManager.this.convertToCompatibleDeviceUserId(userInfo.id), true);
                DeviceUserStatsLogManager.isSingleUser = false;
                DeviceUserStatsLogManager.singleUserChecked = false;
            }
        });
    }

    @Override // com.treadly.Treadly.Data.Delegates.TreadlyServiceDelegate
    public void onUserLogout(final UserInfo userInfo) {
        runOnMain(new Runnable() { // from class: com.treadly.Treadly.Data.Managers.DeviceUserStatsLogManager.5
            @Override // java.lang.Runnable
            public void run() {
                TreadlyClientLib.shared.setDeviceUserStatsLogs(DeviceUserStatsLogManager.this.convertToCompatibleDeviceUserId(userInfo.id), false);
            }
        });
    }

    @Override // com.treadly.client.lib.sdk.Listeners.DeviceUserStatsLogEventListener
    public void onDeviceUserStatsLogReady(final int i) {
        runOnMain(new Runnable() { // from class: com.treadly.Treadly.Data.Managers.DeviceUserStatsLogManager.6
            @Override // java.lang.Runnable
            public void run() {
                TreadlyClientLib.shared.getDeviceUserStatsLog(i);
            }
        });
    }

    @Override // com.treadly.client.lib.sdk.Listeners.DeviceUserStatsLogEventListener
    public void onDeviceUserStatsLogData(final DeviceUserStatsLogInfo deviceUserStatsLogInfo) {
        runOnMain(new Runnable() { // from class: com.treadly.Treadly.Data.Managers.DeviceUserStatsLogManager.7
            @Override // java.lang.Runnable
            public void run() {
                DeviceUserStatsLogManager.this.processLogData(deviceUserStatsLogInfo);
            }
        });
    }

    @Override // com.treadly.client.lib.sdk.Listeners.DeviceUserStatsLogEventListener
    public void onDeviceUserStatsLogReadyV2(final int i) {
        runOnMain(new Runnable() { // from class: com.treadly.Treadly.Data.Managers.-$$Lambda$DeviceUserStatsLogManager$E_9sxupDkZy7VC8n2cGnlJgj2cQ
            @Override // java.lang.Runnable
            public final void run() {
                TreadlyClientLib.shared.getDeviceUserStatsLogV2(i);
            }
        });
    }

    /* renamed from: com.treadly.Treadly.Data.Managers.DeviceUserStatsLogManager$8  reason: invalid class name */
    /* loaded from: classes2.dex */
    class AnonymousClass8 extends TreadlyServiceResponseEventAdapter {
        AnonymousClass8() {
        }

        @Override // com.treadly.Treadly.Data.Listeners.TreadlyServiceResponseEventAdapter, com.treadly.Treadly.Data.Listeners.TreadlyServiceResponseEventListener
        public void onSendDeviceLogSession(String str, String str2, String str3, UserRunningSessionInfo userRunningSessionInfo) {
            super.onSendDeviceLogSession(str, str2, str3, userRunningSessionInfo);
            if (str == null) {
                DeviceUserStatsLogManager.runOnMain(new Runnable() { // from class: com.treadly.Treadly.Data.Managers.-$$Lambda$DeviceUserStatsLogManager$8$7zOgf8ARSMR4g9slZSSMZn3fz9Y
                    @Override // java.lang.Runnable
                    public final void run() {
                        TreadlyClientLib.shared.removeDeviceUserStatsLogV2(0);
                    }
                });
                RunningSessionManager.getInstance().checkDailyGoals();
            }
            String userId = TreadlyServiceManager.getInstance().getUserId();
            if (userId == null || str2 == null || str3 == null || !userId.equals(str2)) {
                return;
            }
            DeviceUserStatsLogManager.this.sendActivityPost(str3);
        }
    }

    @Override // com.treadly.client.lib.sdk.Listeners.DeviceUserStatsLogEventListener
    public void onDeviceUserStatsLogDataV2(byte[] bArr) {
        TreadlyServiceManager.getInstance().sendDeviceLogSession(bArr, new AnonymousClass8());
    }

    @Override // com.treadly.client.lib.sdk.Listeners.DeviceUserStatsLogEventListener
    public void onDeviceUserStatsUnclaimedActiveLog() {
        runOnMain(new Runnable() { // from class: com.treadly.Treadly.Data.Managers.DeviceUserStatsLogManager.9
            @Override // java.lang.Runnable
            public void run() {
                if (!DeviceUserStatsLogManager.singleUserChecked) {
                    DeviceUserStatsLogManager.this.activeLogRequestTimer = new Timer();
                    DeviceUserStatsLogManager.this.activeLogRequestTimer.schedule(new AnonymousClass1(), 1000L);
                } else if (DeviceUserStatsLogManager.isSingleUser) {
                } else {
                    DeviceUserStatsLogManager.this.showUnclaimedActiveLogAlert();
                }
            }

            /* renamed from: com.treadly.Treadly.Data.Managers.DeviceUserStatsLogManager$9$1  reason: invalid class name */
            /* loaded from: classes2.dex */
            class AnonymousClass1 extends TimerTask {
                AnonymousClass1() {
                }

                @Override // java.util.TimerTask, java.lang.Runnable
                public void run() {
                    if (DeviceUserStatsLogManager.isSingleUser) {
                        return;
                    }
                    final DeviceUserStatsLogManager deviceUserStatsLogManager = DeviceUserStatsLogManager.this;
                    DeviceUserStatsLogManager.runOnMain(new Runnable() { // from class: com.treadly.Treadly.Data.Managers.-$$Lambda$DeviceUserStatsLogManager$9$1$wzJSiDSkqu7r9bvsftWWjvSwPbk
                        @Override // java.lang.Runnable
                        public final void run() {
                            DeviceUserStatsLogManager.this.showUnclaimedActiveLogAlert();
                        }
                    });
                }
            }
        });
    }

    @Override // com.treadly.client.lib.sdk.Listeners.DeviceUserStatsLogEventListener
    public void onDeviceUserStatsUnclaimedUserStatsInfo(DeviceUserStatsUnclaimedLogInfo[] deviceUserStatsUnclaimedLogInfoArr) {
        runOnMain(new Runnable() { // from class: com.treadly.Treadly.Data.Managers.DeviceUserStatsLogManager.10
            @Override // java.lang.Runnable
            public void run() {
            }
        });
    }

    static void runOnMain(Runnable runnable) {
        new Handler(Looper.getMainLooper()).post(runnable);
    }

    protected void showUnclaimedActiveLogAlert(String str, String str2) {
        if (this.activity == null) {
            return;
        }
        final AlertDialog.Builder builder = new AlertDialog.Builder(this.activity);
        builder.setMessage(str2);
        builder.setTitle(str);
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() { // from class: com.treadly.Treadly.Data.Managers.DeviceUserStatsLogManager.11
            @Override // android.content.DialogInterface.OnClickListener
            public void onClick(DialogInterface dialogInterface, int i) {
                TreadlyClientLib.shared.claimDeviceUserStatsActiveLog();
                RunningSessionManager.getInstance().sentPendingActivity = false;
                DeviceUserStatsLogManager.this.sendPendingActivity(new DeviceUserStatsResultsListener() { // from class: com.treadly.Treadly.Data.Managers.DeviceUserStatsLogManager.11.1
                    @Override // com.treadly.Treadly.Data.Managers.DeviceUserStatsLogManager.DeviceUserStatsResultsListener
                    public void onResults(String str3) {
                        RunningSessionManager.getInstance().sentPendingActivity = Boolean.valueOf(str3 == null);
                    }
                });
            }
        });
        builder.setNeutralButton("No", new DialogInterface.OnClickListener() { // from class: com.treadly.Treadly.Data.Managers.DeviceUserStatsLogManager.12
            @Override // android.content.DialogInterface.OnClickListener
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        ActivityUtil.runOnUiThread(this.activity, new Runnable() { // from class: com.treadly.Treadly.Data.Managers.DeviceUserStatsLogManager.13
            @Override // java.lang.Runnable
            public void run() {
                builder.create().show();
            }
        });
    }

    protected void showUnclaimedActivityList(DeviceUserStatsUnclaimedLogInfo[] deviceUserStatsUnclaimedLogInfoArr) {
        if (this.activity == null) {
            return;
        }
        ArrayList arrayList = new ArrayList(Arrays.asList(deviceUserStatsUnclaimedLogInfoArr));
        arrayList.sort(new Comparator<DeviceUserStatsUnclaimedLogInfo>() { // from class: com.treadly.Treadly.Data.Managers.DeviceUserStatsLogManager.14
            @Override // java.util.Comparator
            public int compare(DeviceUserStatsUnclaimedLogInfo deviceUserStatsUnclaimedLogInfo, DeviceUserStatsUnclaimedLogInfo deviceUserStatsUnclaimedLogInfo2) {
                return deviceUserStatsUnclaimedLogInfo.sequence < deviceUserStatsUnclaimedLogInfo2.sequence ? 1 : -1;
            }
        });
        this.listView = new TreadlyActivityUnclaimedListView(this.activity, arrayList);
        this.listView.showListPopup();
    }

    protected void processUnclaimedSessionData(List<UserRunningSessionInfo> list, String str) {
        if (this.activity == null) {
            return;
        }
        list.sort(new Comparator() { // from class: com.treadly.Treadly.Data.Managers.-$$Lambda$DeviceUserStatsLogManager$WbXRy8CxvyrUh4c5_hvWl798bQ0
            @Override // java.util.Comparator
            public final int compare(Object obj, Object obj2) {
                return DeviceUserStatsLogManager.lambda$processUnclaimedSessionData$2((UserRunningSessionInfo) obj, (UserRunningSessionInfo) obj2);
            }
        });
        this.listView = new TreadlyActivityUnclaimedListView(this.activity, list, true);
        this.listView.deviceAddress = str;
        this.listView.showListPopup();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static /* synthetic */ int lambda$processUnclaimedSessionData$2(UserRunningSessionInfo userRunningSessionInfo, UserRunningSessionInfo userRunningSessionInfo2) {
        return Integer.parseInt(userRunningSessionInfo2.log_id) - Integer.parseInt(userRunningSessionInfo.log_id);
    }

    protected void showInfoAlert(String str, String str2) {
        if (this.activity == null) {
            return;
        }
        final AlertDialog.Builder builder = new AlertDialog.Builder(this.activity);
        builder.setMessage(str2);
        builder.setTitle(str);
        builder.setNeutralButton("Dismiss", new DialogInterface.OnClickListener() { // from class: com.treadly.Treadly.Data.Managers.DeviceUserStatsLogManager.15
            @Override // android.content.DialogInterface.OnClickListener
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        ActivityUtil.runOnUiThread(this.activity, new Runnable() { // from class: com.treadly.Treadly.Data.Managers.DeviceUserStatsLogManager.16
            @Override // java.lang.Runnable
            public void run() {
                builder.create().show();
            }
        });
    }

    public void handleUnclaimedLogData(DeviceUserStatsUnclaimedLogInfo[] deviceUserStatsUnclaimedLogInfoArr) {
        this.activity.closeLoadingDialog();
        processUnclaimedLogData(deviceUserStatsUnclaimedLogInfoArr);
    }

    public void handleUnclaimedSessionData(List<UserRunningSessionInfo> list, String str) {
        this.activity.closeLoadingDialog();
        processUnclaimedSessionData(list, str);
    }

    public boolean startLogging(VersionInfo versionInfo, DeviceStatus deviceStatus) {
        if (supportDeviceStartLogging(versionInfo)) {
            return false;
        }
        Float f = DeviceStatusVerification.shared.distance;
        Integer num = DeviceStatusVerification.shared.steps;
        DistanceUnits distanceUnits = DeviceStatusVerification.shared.distanceUnits;
        if (f == null || num == null || distanceUnits == null) {
            return false;
        }
        this.customBoardVersion = versionInfo;
        this.currentDeviceStatsSegments.clear();
        this.initialSteps = num;
        this.initialDistance = Double.valueOf(distanceUnits == DistanceUnits.MI ? f.floatValue() : f.floatValue() * 0.6214d);
        this.initialTimestamp = new Date();
        this.isLogging = true;
        return true;
    }

    public void appendLogs(DeviceStatus deviceStatus) {
        if (this.isLogging) {
            DistanceUnits distanceUnits = DeviceStatusVerification.shared.distanceUnits;
            Float f = DeviceStatusVerification.shared.distance;
            Integer num = DeviceStatusVerification.shared.steps;
            if (num == null || f == null || num.intValue() < 0 || f.floatValue() < Utils.DOUBLE_EPSILON) {
                return;
            }
            float targetSpeed = deviceStatus.getSpeedInfo().getTargetSpeed();
            if (targetSpeed < deviceStatus.getSpeedInfo().getMinimumSpeed() || targetSpeed == Utils.DOUBLE_EPSILON || targetSpeed > deviceStatus.getSpeedInfo().getMaximumSpeed()) {
                return;
            }
            if (distanceUnits == DistanceUnits.KM) {
                targetSpeed *= 0.6214f;
            }
            if (this.currentDeviceStatsSegments.size() == 0) {
                this.currentDeviceStatsSegments.add(new DeviceUserStatsLogSegmentInfo(new Date(), targetSpeed));
            } else {
                Float valueOf = Float.valueOf(this.currentDeviceStatsSegments.get(this.currentDeviceStatsSegments.size() - 1).speed);
                if (valueOf != null && valueOf.floatValue() != targetSpeed) {
                    this.currentDeviceStatsSegments.add(new DeviceUserStatsLogSegmentInfo(new Date(), targetSpeed));
                }
            }
            this.finalSteps = num;
            this.finalDistance = Double.valueOf(distanceUnits == DistanceUnits.MI ? f.floatValue() : f.floatValue() * 0.6214d);
        }
    }

    public void finishLogging() {
        int i;
        if (!this.isLogging) {
            clearLogging();
        } else if (this.currentDeviceStatsSegments.size() <= 0) {
            clearLogging();
        } else {
            Date date = this.initialTimestamp;
            Integer num = this.initialSteps;
            Double d = this.initialDistance;
            Integer num2 = this.finalSteps;
            Double d2 = this.finalDistance;
            VersionInfo versionInfo = this.customBoardVersion;
            if (date == null || num == null || d == null || num2 == null || d2 == null || versionInfo == null) {
                clearLogging();
                return;
            }
            int max = Integer.max(num.intValue(), 0);
            double max2 = Double.max(d.doubleValue(), Utils.DOUBLE_EPSILON);
            int max3 = Integer.max(num2.intValue(), 0);
            double max4 = Double.max(d2.doubleValue(), Utils.DOUBLE_EPSILON);
            if (max4 - max2 < Utils.DOUBLE_EPSILON) {
                max4 = 0.0d;
                max2 = 0.0d;
            }
            if (max3 - max < 0) {
                max = 0;
                i = 0;
            } else {
                i = max3;
            }
            DeviceUserStatsLogInfo deviceUserStatsLogInfo = new DeviceUserStatsLogInfo(this.sequence, 100, date, max, i, (float) max2, (float) max4);
            deviceUserStatsLogInfo.segments = (DeviceUserStatsLogSegmentInfo[]) this.currentDeviceStatsSegments.toArray(new DeviceUserStatsLogSegmentInfo[0]);
            processLogData(deviceUserStatsLogInfo, true, getGenTypeString(versionInfo));
            clearLogging();
        }
    }

    public void clearLogging() {
        this.isLogging = false;
        this.currentDeviceStatsSegments.clear();
        this.initialSteps = null;
        this.initialDistance = null;
        this.finalSteps = null;
        this.finalDistance = null;
    }

    public boolean supportDeviceStartLogging(VersionInfo versionInfo) {
        return versionInfo.isGreaterThan(deviceStatsLoggingMinVersion) || versionInfo.isEqual(deviceStatsLoggingMinVersion);
    }

    public static String getGenTypeString(VersionInfo versionInfo) {
        VersionInfo versionInfo2 = new VersionInfo(3, 0, 0);
        VersionInfo versionInfo3 = new VersionInfo(2, 0, 0);
        return (versionInfo.isGreaterThan(versionInfo2) || versionInfo.isEqual(versionInfo2)) ? "Gen-3" : (versionInfo.isGreaterThan(versionInfo3) || versionInfo.isEqual(versionInfo3)) ? "Gen-2" : "Gen-1";
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void sendActivityPost(final String str) {
        if (this.pendingActivityImage != null) {
            TreadlyServiceManager.getInstance().sendActivtyPostImage(str, new File(this.pendingActivityImage.getPath()), new TreadlyServiceResponseEventAdapter() { // from class: com.treadly.Treadly.Data.Managers.DeviceUserStatsLogManager.17
                @Override // com.treadly.Treadly.Data.Listeners.TreadlyServiceResponseEventAdapter, com.treadly.Treadly.Data.Listeners.TreadlyServiceResponseEventListener
                public void onUrlResponse(String str2, String str3) {
                    super.onUrlResponse(str2, str3);
                    if (str2 != null) {
                        PrintStream printStream = System.out;
                        printStream.println("Error sending activity post image" + str2);
                    }
                    DeviceUserStatsLogManager.this.pendingActivityImage = null;
                    TreadlyServiceManager.getInstance().sendActivtyPost(str, DeviceUserStatsLogManager.this.pendingActivityComment, str3, new TreadlyServiceResponseEventAdapter() { // from class: com.treadly.Treadly.Data.Managers.DeviceUserStatsLogManager.17.1
                        @Override // com.treadly.Treadly.Data.Listeners.TreadlyServiceResponseEventAdapter, com.treadly.Treadly.Data.Listeners.TreadlyServiceResponseEventListener
                        public void onSuccess(String str4) {
                            super.onSuccess(str4);
                            if (str4 != null) {
                                PrintStream printStream2 = System.out;
                                printStream2.println("Error sending activity post comment: " + str4);
                            }
                            DeviceUserStatsLogManager.this.pendingActivityComment = null;
                        }
                    });
                }
            });
        } else if (this.pendingActivityComment != null) {
            TreadlyServiceManager.getInstance().sendActivtyPost(str, this.pendingActivityComment, null, new TreadlyServiceResponseEventAdapter() { // from class: com.treadly.Treadly.Data.Managers.DeviceUserStatsLogManager.18
                @Override // com.treadly.Treadly.Data.Listeners.TreadlyServiceResponseEventAdapter, com.treadly.Treadly.Data.Listeners.TreadlyServiceResponseEventListener
                public void onSuccess(String str2) {
                    super.onSuccess(str2);
                    if (str2 != null) {
                        PrintStream printStream = System.out;
                        printStream.println("Error sending activity post comment: " + str2);
                    }
                    DeviceUserStatsLogManager.this.pendingActivityComment = null;
                }
            });
        }
    }

    public void clearPendingActivityPost() {
        this.pendingActivityImage = null;
        this.pendingActivityComment = null;
    }
}
