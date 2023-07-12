package com.treadly.Treadly.UI.TreadlyConnect;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.bluetooth.BluetoothAdapter;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.text.Editable;
import android.text.Html;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.appcompat.widget.PopupMenu;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.exifinterface.media.ExifInterface;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import com.airbnb.lottie.LottieAnimationView;
import com.github.mikephil.charting.utils.Utils;
import com.treadly.Treadly.Data.Listeners.TreadlyServiceResponseEventAdapter;
import com.treadly.Treadly.Data.Managers.AppActivityManager;
import com.treadly.Treadly.Data.Managers.DeviceUserStatsLogManager;
import com.treadly.Treadly.Data.Managers.DynamicBannerManager;
import com.treadly.Treadly.Data.Managers.RunningSessionManager;
import com.treadly.Treadly.Data.Managers.TreadlyActivationManager;
import com.treadly.Treadly.Data.Managers.TreadlyEventHelper;
import com.treadly.Treadly.Data.Managers.TreadlyServiceManager;
import com.treadly.Treadly.Data.Model.DynamicBanner.DynamicBannerAppPage;
import com.treadly.Treadly.Data.Model.DynamicBanner.DynamicBannerInfo;
import com.treadly.Treadly.Data.Model.UserActivityInfo;
import com.treadly.Treadly.Data.Model.UserDailyGoalType;
import com.treadly.Treadly.Data.Model.UserInfo;
import com.treadly.Treadly.Data.Model.UserNotificationSettingInfo;
import com.treadly.Treadly.Data.Model.UserProfileInfo;
import com.treadly.Treadly.Data.Model.UserRunningSessionInfo;
import com.treadly.Treadly.Data.Utility.NotificationCenter.NotificationCenter;
import com.treadly.Treadly.Data.Utility.NotificationCenter.NotificationType;
import com.treadly.Treadly.MainActivity;
import com.treadly.Treadly.R;
import com.treadly.Treadly.UI.FirmwareUpdate.FirmwareUpdateSsidFragment;
import com.treadly.Treadly.UI.TreadlyActivity.ActivityCalendar.TreadlyActivityCalendarFragment;
import com.treadly.Treadly.UI.TreadlyActivity.UnclaimedActivityList.TreadlyActivityUnclaimedFragment;
import com.treadly.Treadly.UI.TreadlyConnect.AlertView.TreadlyConnectPauseView;
import com.treadly.Treadly.UI.TreadlyConnect.DailyGoalProgressView.DailyGoalProgressView;
import com.treadly.Treadly.UI.TreadlyConnect.DeviceConnect.TreadlyDeviceConnectStatusFragment;
import com.treadly.Treadly.UI.TreadlyConnect.SpeedOption.TreadlySpeedOptionView;
import com.treadly.Treadly.UI.TreadlyConnect.TreadlyConnectFragment;
import com.treadly.Treadly.UI.TreadlyConnect.TreadlyConnectSliderFragment;
import com.treadly.Treadly.UI.TreadlyConnect.TreadlyDeviceConnectFragment;
import com.treadly.Treadly.UI.TreadlyDashboard.Tools.DeviceStatusVerification;
import com.treadly.Treadly.UI.TreadlyProfile.InviteFriends.TreadlyInviteFriendsFragment;
import com.treadly.Treadly.UI.TreadlyProfile.Settings.WifiSetup.TreadlyProfileSettingsWifiSetupFragment;
import com.treadly.Treadly.UI.TreadlyProfile.TreadlyProfileUserView;
import com.treadly.Treadly.UI.TreadlyVideo.Data.InviteServiceInviteInfo;
import com.treadly.Treadly.UI.TreadlyVideo.Data.VideoServiceHelper;
import com.treadly.Treadly.UI.Util.ActivityUtil;
import com.treadly.Treadly.UI.Util.BaseClasses.BaseFragment;
import com.treadly.Treadly.UI.Util.SharedPreferences;
import com.treadly.client.lib.sdk.Listeners.DeviceActivationEventAdapter;
import com.treadly.client.lib.sdk.Listeners.DeviceActivationEventListener;
import com.treadly.client.lib.sdk.Listeners.DeviceConnectionEventListener;
import com.treadly.client.lib.sdk.Listeners.DeviceUserStatsLogEventAdapter;
import com.treadly.client.lib.sdk.Listeners.DeviceUserStatsLogEventListener;
import com.treadly.client.lib.sdk.Listeners.OtaUpdateRequestEventAdapter;
import com.treadly.client.lib.sdk.Listeners.RequestEventAdapter;
import com.treadly.client.lib.sdk.Listeners.RequestEventListener;
import com.treadly.client.lib.sdk.Listeners.UserInteractionEventListener;
import com.treadly.client.lib.sdk.Managers.ActivationManager;
import com.treadly.client.lib.sdk.Managers.TreadlyLogManager;
import com.treadly.client.lib.sdk.Model.ActivationInfo;
import com.treadly.client.lib.sdk.Model.AuthenticateReferenceCodeInfo;
import com.treadly.client.lib.sdk.Model.AuthenticationState;
import com.treadly.client.lib.sdk.Model.ComponentInfo;
import com.treadly.client.lib.sdk.Model.ComponentType;
import com.treadly.client.lib.sdk.Model.ComponentVersionInfo;
import com.treadly.client.lib.sdk.Model.ConnectionStatusCode;
import com.treadly.client.lib.sdk.Model.DeviceConnectionEvent;
import com.treadly.client.lib.sdk.Model.DeviceConnectionStatus;
import com.treadly.client.lib.sdk.Model.DeviceInfo;
import com.treadly.client.lib.sdk.Model.DeviceMode;
import com.treadly.client.lib.sdk.Model.DeviceStatus;
import com.treadly.client.lib.sdk.Model.DeviceStatusCode;
import com.treadly.client.lib.sdk.Model.DeviceUserStatsUnclaimedLogInfo;
import com.treadly.client.lib.sdk.Model.DistanceUnits;
import com.treadly.client.lib.sdk.Model.FirmwareVersion;
import com.treadly.client.lib.sdk.Model.HandrailStatus;
import com.treadly.client.lib.sdk.Model.OtaUpdateInfo;
import com.treadly.client.lib.sdk.Model.RemoteStatus;
import com.treadly.client.lib.sdk.Model.SpeedInfo;
import com.treadly.client.lib.sdk.Model.UserInteractionHandrailEvent;
import com.treadly.client.lib.sdk.Model.UserInteractionStatus;
import com.treadly.client.lib.sdk.Model.UserInteractionSteps;
import com.treadly.client.lib.sdk.Model.VersionInfo;
import com.treadly.client.lib.sdk.TreadlyClientLib;
import java.io.PrintStream;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import org.joda.time.DateTimeConstants;
import org.json.JSONException;

/* loaded from: classes2.dex */
public class TreadlyConnectFragment extends BaseFragment implements DeviceConnectionEventListener {
    private static final int REQUEST_ENABLE_BT = 1;
    public static final String TAG = "TREADLY_CONNECT_FRAGMENT";
    private static boolean isFirstStatus = true;
    private static boolean singleUserMode = false;
    boolean activationCodeTextFieldValid;
    ActivationInfo activationInfo;
    String activationTarget;
    private TreadlyConnectSliderFragment.TreadlyConnectSliderFragmentEventListener adapter;
    private boolean appActivityPendingConnect;
    private AuthenticationState authenticationState;
    private boolean authenticationStateChecked;
    private boolean authenticationStateUpdated;
    private boolean autoConnected;
    private TextView autoTextView;
    AlertDialog.Builder builder;
    private ImageButton calendarActivity;
    private TreadlyActivityCalendarFragment calendarFragment;
    private int calories;
    private TextView caloriesTextView;
    TextView centerStrideLabel;
    boolean checkingDynamicBanners;
    AlertDialog completeActivationAlert;
    private Button connectButton;
    private TextView connectStatusTextView;
    private boolean connectingViewControllerSkipPressed;
    TextView constantEndLabel;
    TextView constantStartLabel;
    private DeviceInfo currentDeviceInfo;
    private ComponentVersionInfo customBoardVersion;
    private double dailyDistance;
    private double dailyDuration;
    private int dailyGoalPercentage;
    private DailyGoalProgressView dailyGoalProgressView;
    private String dailyGoalTarget;
    private TextView dailyGoalTextView;
    private int dailySteps;
    private TextView dayOfMonth;
    DeviceActivationEventListener deviceActivationAdapter;
    private TreadlyDeviceConnectStatusFragment deviceConnectFragment;
    private String deviceName;
    AlertDialog deviceStatusAlert;
    private final DeviceUserStatsLogEventListener deviceUserStatsLogEventListener;
    boolean displayedEmergencyModeView;
    boolean displayedPauseStateView;
    private DecimalFormat distanceFormatter;
    private TextView distanceTextView;
    private TextView distanceTitleTextView;
    TreadlyConnectAlertView emergencyModeAlert;
    AlertDialog errorActivationAlert;
    private boolean errorShown;
    private boolean expectingUnclaimedActivityCheck;
    AlertDialog finishActivationAlert;
    Timer finishActivationTimer;
    TextView friendsFraction;
    int friendsOnline;
    TextView friendsOnlineButton;
    int friendsTotal;
    private Timer goalCheckTimer;
    private boolean hasCheckedOtaVersion;
    boolean haveCheckedDynamicBanners;
    private boolean isActivationInProgress;
    private boolean isCountDownRunning;
    private boolean isDeviceConnectPageVisible;
    private boolean isIrDebugOn;
    private boolean isPageVisible;
    private LottieAnimationView loadingView;
    private ImageView loadingViewBackground;
    private boolean loadingViewSetForceStart;
    private Timer loadingViewTimer;
    private int loadingViewTimerCount;
    private ScrollView mScrollView;
    private String macAddress;
    private ComponentVersionInfo mainBoardVersion;
    public View mainView;
    boolean maintenanceWarningDisplayed;
    Map<String, String> maintenanceWarningDisplayedDevices;
    private boolean newUser;
    private BroadcastReceiver notificationResponseReceiver;
    private boolean otaRequired;
    private final OtaUpdateRequestEventAdapter otaUpdateRequestEventAdapter;
    TreadlyConnectPauseView pauseStateAlert;
    LinearLayout pauseStateAlertContainer;
    boolean pendingActivationBleEnable;
    private boolean pendingDisconnect;
    boolean pendingMaintenanceReset;
    private boolean pendingPause;
    private boolean pendingStop;
    private boolean pendingTreadmillStart;
    private boolean presentedActivationDialog;
    private boolean presentedUnprocessedDialog;
    Timer reconnectTimer;
    private RemoteStatus remoteStatus;
    private TextView remoteSwitch;
    private RequestEventListener requestEventAdapter;
    TextView sequenceErrorLabel;
    TextView sequenceLabel;
    private String serialNumber;
    private boolean setSendEnableBleOffSource;
    private int showCountDownSequence;
    AlertDialog singleUserModeAlert;
    TreadlyConnectSliderFragment sliderFragment;
    private FrameLayout sliderFrameLayout;
    private ImageButton speedDownButton;
    private DecimalFormat speedFormatter;
    private ConstraintLayout speedOptionContainer;
    private TreadlySpeedOptionView speedOptionView;
    private TextView speedTextView;
    private ImageButton speedUpButton;
    AlertDialog startActivationAlert;
    private int statusCount;
    TextView stepOneLabel;
    TextView stepTwoLabel;
    private DecimalFormat stepsFormatter;
    private TextView stepsTextView;
    private TextView stepsTitleTextView;
    private boolean stopButtonGreen;
    AlertDialog submitActivationAlert;
    String targetTreadmillName;
    TextWatcher textWatcher;
    private TextView timeTextView;
    private TextView timeTitleTextView;
    Map<String, String> unclaimedListDisplayedDevices;
    public UserInteractionEventListener userInteractionListener;
    private TreadlyConnectUserInteractionView userInteractionView;
    UserNotificationSettingInfo userNotifications;
    private UserProfileInfo userProfile;
    PopupMenu videoPopupMenu;
    private ConnectionStatus connectState = ConnectionStatus.notConnected;
    private boolean isConnected = false;
    private boolean isReconnecting = false;
    private boolean switchTouched = false;
    private int remoteStatusCount = 0;
    private DeviceStatus currentDeviceStatusInfo = null;
    public ComponentInfo[] componentList = new ComponentInfo[0];
    private TreadlyConnectStartState startState = TreadlyConnectStartState.notConnected;
    private TreadlyConnectStartState lastStartState = TreadlyConnectStartState.notConnected;
    private float queuedSpeedValue = 0.0f;

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes2.dex */
    public enum ConnectionStatus {
        unknown,
        connected,
        notConnected,
        connecting,
        disconnecting,
        reconnecting
    }

    private void displayFriendsList(List<UserInfo> list, Map<String, UserActivityInfo> map) {
    }

    private void enableFriendsViews(boolean z) {
    }

    private void initFriendsViews(View view) {
    }

    private void updateFriendsListViews() {
    }

    private void updateFriendsViews() {
    }

    private void updateTreadmillRunningState(TreadlyConnectStartState treadlyConnectStartState) {
    }

    String getAppVersion() {
        return "1.1.8";
    }

    void hideInfoLoading() {
    }

    public TreadlyConnectFragment() {
        this.newUser = SharedPreferences.shared.getConnectedDeviceName() == null;
        this.setSendEnableBleOffSource = false;
        this.presentedActivationDialog = false;
        this.presentedUnprocessedDialog = false;
        this.authenticationStateChecked = false;
        this.authenticationStateUpdated = false;
        this.isPageVisible = false;
        this.isDeviceConnectPageVisible = false;
        this.pendingTreadmillStart = false;
        this.pendingDisconnect = false;
        this.calories = 0;
        this.dailyGoalPercentage = 0;
        this.dailyGoalTarget = "";
        this.adapter = new TreadlyConnectSliderFragment.TreadlyConnectSliderFragmentEventListener() { // from class: com.treadly.Treadly.UI.TreadlyConnect.TreadlyConnectFragment.1
            @Override // com.treadly.Treadly.UI.TreadlyConnect.TreadlyConnectSliderFragment.TreadlyConnectSliderFragmentEventListener
            public void onPowerOnDevice() {
                TreadlyConnectFragment.this.powerOnDevice();
            }

            @Override // com.treadly.Treadly.UI.TreadlyConnect.TreadlyConnectSliderFragment.TreadlyConnectSliderFragmentEventListener
            public void onImpactOccurred() {
                TreadlyConnectFragment.this.impactOccured(TreadlyConnectFragment.this.sliderFragment.getView());
            }

            @Override // com.treadly.Treadly.UI.TreadlyConnect.TreadlyConnectSliderFragment.TreadlyConnectSliderFragmentEventListener
            public void onPowerOffDevice() {
                if (TreadlyConnectFragment.this.pauseDevice()) {
                    TreadlyConnectFragment.this.pendingTreadmillStart = false;
                    TreadlyConnectFragment.this.pendingPause = true;
                }
            }

            @Override // com.treadly.Treadly.UI.TreadlyConnect.TreadlyConnectSliderFragment.TreadlyConnectSliderFragmentEventListener
            public void onShowAlert(String str, String str2) {
                TreadlyConnectFragment.this.showAlert(str, str2);
            }

            @Override // com.treadly.Treadly.UI.TreadlyConnect.TreadlyConnectSliderFragment.TreadlyConnectSliderFragmentEventListener
            public boolean getValueForHandrail(float f, DistanceUnits distanceUnits, HandrailStatus handrailStatus) {
                return TreadlyConnectFragment.this.valueForHandrail(f, distanceUnits, handrailStatus);
            }

            @Override // com.treadly.Treadly.UI.TreadlyConnect.TreadlyConnectSliderFragment.TreadlyConnectSliderFragmentEventListener
            public void onSetSpeed(float f) {
                TreadlyConnectFragment.this.setSpeed(f);
            }

            @Override // com.treadly.Treadly.UI.TreadlyConnect.TreadlyConnectSliderFragment.TreadlyConnectSliderFragmentEventListener
            public void onStopPressed(Button button) {
                TreadlyConnectFragment.this.onStopButtonTouchDown(button);
            }
        };
        this.checkingDynamicBanners = false;
        this.haveCheckedDynamicBanners = false;
        this.distanceFormatter = new DecimalFormat("0.0");
        this.stepsFormatter = new DecimalFormat("#,###");
        this.speedFormatter = new DecimalFormat("0.0");
        this.errorShown = false;
        this.requestEventAdapter = new AnonymousClass13();
        this.deviceActivationAdapter = new AnonymousClass14();
        this.otaUpdateRequestEventAdapter = new AnonymousClass15();
        this.textWatcher = new TextWatcher() { // from class: com.treadly.Treadly.UI.TreadlyConnect.TreadlyConnectFragment.16
            @Override // android.text.TextWatcher
            public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {
            }

            @Override // android.text.TextWatcher
            public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
            }

            @Override // android.text.TextWatcher
            public void afterTextChanged(Editable editable) {
                if (editable == null) {
                    TreadlyConnectFragment.this.activationCodeTextFieldValid = false;
                    TreadlyConnectFragment.this.updateActivationAction();
                    return;
                }
                TreadlyConnectFragment.this.activationCodeTextFieldValid = !editable.toString().isEmpty();
                TreadlyConnectFragment.this.updateActivationAction();
            }
        };
        this.authenticationState = AuthenticationState.unknown;
        this.goalCheckTimer = null;
        this.maintenanceWarningDisplayed = false;
        this.maintenanceWarningDisplayedDevices = new HashMap();
        this.pendingMaintenanceReset = false;
        this.pendingActivationBleEnable = false;
        this.unclaimedListDisplayedDevices = new HashMap();
        this.activationCodeTextFieldValid = false;
        this.reconnectTimer = new Timer();
        this.finishActivationTimer = new Timer();
        this.userNotifications = null;
        this.isIrDebugOn = false;
        this.notificationResponseReceiver = new BroadcastReceiver() { // from class: com.treadly.Treadly.UI.TreadlyConnect.TreadlyConnectFragment.36
            @Override // android.content.BroadcastReceiver
            public void onReceive(Context context, Intent intent) {
                String stringExtra = intent.getStringExtra("notification");
                if (stringExtra != null && stringExtra.equals(NotificationType.onProfileChanged.toString())) {
                    TreadlyConnectFragment.this.fetchUserProfile();
                }
                if (stringExtra != null && stringExtra.equals(NotificationType.onDailyGoalChanged.toString())) {
                    TreadlyConnectFragment.this.dailyGoalsChanged();
                }
                if (stringExtra != null && stringExtra.equals(NotificationType.onNotificationsChanged.toString())) {
                    TreadlyConnectFragment.this.fetchUserNotifications();
                }
                if (stringExtra != null && stringExtra.equals(NotificationType.didUpdateCurrentCalories.toString())) {
                    TreadlyConnectFragment.this.handleCurrentCaloriesUpdated();
                }
                if (stringExtra != null && stringExtra.equals(NotificationType.didUpdateCurrentSteps.toString())) {
                    TreadlyConnectFragment.this.handleCurrentCaloriesUpdated();
                }
                if (stringExtra != null && stringExtra.equals(NotificationType.didUpdateCurrentDistance.toString())) {
                    TreadlyConnectFragment.this.handleCurrentCaloriesUpdated();
                }
                if (stringExtra == null || !stringExtra.equals(NotificationType.onAdminMode.toString())) {
                    return;
                }
                MainActivity mainActivity = (MainActivity) TreadlyConnectFragment.this.getActivity();
                if (mainActivity == null || !mainActivity.adminMode) {
                    TreadlyConnectFragment.this.userInteractionView.setVisibility(8);
                } else {
                    TreadlyConnectFragment.this.userInteractionView.setVisibility(0);
                }
            }
        };
        this.userInteractionListener = new UserInteractionEventListener() { // from class: com.treadly.Treadly.UI.TreadlyConnect.TreadlyConnectFragment.37
            @Override // com.treadly.client.lib.sdk.Listeners.UserInteractionEventListener
            public void onUserInteractionHandrailEvent(boolean z, UserInteractionHandrailEvent userInteractionHandrailEvent) {
            }

            @Override // com.treadly.client.lib.sdk.Listeners.UserInteractionEventListener
            public void onUserInteractionStepsDetected(boolean z, final UserInteractionSteps userInteractionSteps) {
                if (!z || userInteractionSteps == null) {
                    return;
                }
                ActivityUtil.runOnUiThread(TreadlyConnectFragment.this.getActivity(), new Runnable() { // from class: com.treadly.Treadly.UI.TreadlyConnect.TreadlyConnectFragment.37.1
                    @Override // java.lang.Runnable
                    public void run() {
                        TreadlyConnectFragment.this.userInteractionView.updateSteps(userInteractionSteps);
                    }
                });
            }

            @Override // com.treadly.client.lib.sdk.Listeners.UserInteractionEventListener
            public void onUserInteractionStatus(boolean z, final UserInteractionStatus userInteractionStatus) {
                if (!z || userInteractionStatus == null) {
                    return;
                }
                ActivityUtil.runOnUiThread(TreadlyConnectFragment.this.getActivity(), new Runnable() { // from class: com.treadly.Treadly.UI.TreadlyConnect.TreadlyConnectFragment.37.2
                    @Override // java.lang.Runnable
                    public void run() {
                        TreadlyConnectFragment.this.userInteractionView.updateZones(userInteractionStatus);
                    }
                });
            }
        };
        this.friendsOnline = 0;
        this.friendsTotal = 0;
        this.expectingUnclaimedActivityCheck = false;
        this.deviceUserStatsLogEventListener = new AnonymousClass40();
        this.showCountDownSequence = -1;
        this.isCountDownRunning = false;
    }

    static /* synthetic */ int access$5808(TreadlyConnectFragment treadlyConnectFragment) {
        int i = treadlyConnectFragment.loadingViewTimerCount;
        treadlyConnectFragment.loadingViewTimerCount = i + 1;
        return i;
    }

    private void setConnectState(ConnectionStatus connectionStatus) {
        this.connectState = connectionStatus;
        setConnectButton(connectionStatus);
        PrintStream printStream = System.out;
        printStream.println("LGK :: CONNECT STATE: " + this.connectState);
        setIsConnected((connectionStatus == ConnectionStatus.connected || connectionStatus == ConnectionStatus.reconnecting) && this.authenticationState == AuthenticationState.active);
        setStopButtonVisibility();
        updateStopButton();
    }

    private void setIsConnected(boolean z) {
        this.isConnected = z;
        if (singleUserMode) {
            return;
        }
        if (this.sliderFragment != null) {
            this.sliderFragment.isConnected = z;
        }
        enableUi(z);
    }

    private void setIsReconnecting(boolean z) {
        this.isReconnecting = z;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void setCurrentDeviceStatusInfo(DeviceStatus deviceStatus) {
        this.currentDeviceStatusInfo = deviceStatus;
        if (this.sliderFragment != null) {
            this.sliderFragment.currentDeviceStatusInfo = deviceStatus;
        }
        updateStopButton();
    }

    private void setPendingStop(boolean z) {
        this.pendingStop = z;
        updateStopButton();
        boolean z2 = this.pendingStop;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void setPendingPause(boolean z) {
        this.pendingPause = z;
        updateStopButton();
        boolean z2 = this.pendingPause;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void setStartState(TreadlyConnectStartState treadlyConnectStartState) {
        this.startState = treadlyConnectStartState;
        PrintStream printStream = System.out;
        printStream.println("LGK :: START STATE: " + treadlyConnectStartState);
        if (this.sliderFragment != null) {
            this.sliderFragment.setStartState(treadlyConnectStartState);
        }
        updateSpeedSliderStartState(treadlyConnectStartState);
        updateStopButton();
    }

    private void setCalories(int i, String str) {
        this.calories = i;
        if (this.caloriesTextView != null && this.userProfile != null && this.userProfile.dailyGoalType() == UserDailyGoalType.calories) {
            this.caloriesTextView.setText(String.format(Locale.getDefault(), "%d%% Completed %s", Integer.valueOf(this.dailyGoalPercentage), str));
            RunningSessionManager.getInstance().setLastDailyGoalTarget(str);
            this.dailyGoalProgressView.setProgress(this.dailyGoalPercentage / 100.0d);
        }
        RunningSessionManager.getInstance().setLastTotalCalories(i);
    }

    private void setDailySteps(int i, String str) {
        this.dailySteps = i;
        if (this.caloriesTextView != null && this.userProfile != null && this.userProfile.dailyGoalType() == UserDailyGoalType.steps) {
            this.caloriesTextView.setText(String.format(Locale.getDefault(), "%d%% Completed %s", Integer.valueOf(this.dailyGoalPercentage), str));
            RunningSessionManager.getInstance().setLastDailyGoalTarget(str);
            this.dailyGoalProgressView.setProgress(this.dailyGoalPercentage / 100.0d);
        }
        RunningSessionManager.getInstance().setLastTotalSteps(i);
    }

    private void setDailyDistance(double d, String str) {
        this.dailyDistance = d;
        if (this.caloriesTextView != null && this.userProfile != null && this.userProfile.dailyGoalType() == UserDailyGoalType.distance) {
            this.caloriesTextView.setText(String.format(Locale.getDefault(), "%d%% Completed %s", Integer.valueOf(this.dailyGoalPercentage), str));
            RunningSessionManager.getInstance().setLastDailyGoalTarget(str);
            this.dailyGoalProgressView.setProgress(this.dailyGoalPercentage / 100.0d);
        }
        RunningSessionManager.getInstance().setLastTotalDistance(d);
    }

    private void setDailyDuration(double d, String str) {
        this.dailyDuration = d;
        if (this.caloriesTextView != null && this.userProfile != null && this.userProfile.dailyGoalType() == UserDailyGoalType.duration) {
            this.caloriesTextView.setText(String.format(Locale.getDefault(), "%d%% Completed %s", Integer.valueOf(this.dailyGoalPercentage), str));
            RunningSessionManager.getInstance().setLastDailyGoalTarget(str);
            this.dailyGoalProgressView.setProgress(this.dailyGoalPercentage / 100.0d);
        }
        RunningSessionManager.getInstance().setLastTotalDuration(d);
    }

    private void setDailyGoalPercentage(int i) {
        this.dailyGoalPercentage = i;
        RunningSessionManager.getInstance().setLastDailyGoal(i);
        if (this.dailyGoalTextView != null) {
            this.dailyGoalProgressView.setProgress(this.dailyGoalPercentage / 100.0d);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void onStopButtonTouchDown(Button button) {
        if (this.currentDeviceStatusInfo == null || this.currentDeviceStatusInfo.getPairingModeState() || !checkSessionOwner(this.currentDeviceStatusInfo) || !this.isConnected || this.pendingStop || this.pendingPause || singleUserMode || this.startState == TreadlyConnectStartState.forcedStart || this.startState == TreadlyConnectStartState.starting || this.startState == TreadlyConnectStartState.stopping || this.startState == TreadlyConnectStartState.notConnected || this.isReconnecting || this.isCountDownRunning) {
            return;
        }
        switch (this.startState) {
            case start:
                if (pauseDevice()) {
                    impactOccured(button);
                    setPendingPause(true);
                    if (this.sliderFragment != null) {
                        this.sliderFragment.finishSliderAnimation();
                        return;
                    }
                    return;
                }
                return;
            case stop:
                if (powerOnDevice()) {
                    impactOccured(button);
                    showCountDown();
                    setStartState(TreadlyConnectStartState.forcedStarting);
                    return;
                }
                return;
            default:
                impactOccured(button);
                return;
        }
    }

    @Override // androidx.fragment.app.Fragment
    public void onHiddenChanged(boolean z) {
        super.onHiddenChanged(z);
        this.isPageVisible = !z;
        if (!z) {
            showBottomNavigation();
        } else {
            hideCalendar();
        }
    }

    @Override // androidx.fragment.app.Fragment
    public void onCreate(@Nullable Bundle bundle) {
        super.onCreate(bundle);
        addConnectBackStackCallback();
        final BluetoothAdapter defaultAdapter = BluetoothAdapter.getDefaultAdapter();
        if (defaultAdapter == null) {
            return;
        }
        if (!defaultAdapter.isEnabled()) {
            startActivityForResult(new Intent("android.bluetooth.adapter.action.REQUEST_ENABLE"), 1);
        }
        BroadcastReceiver broadcastReceiver = new BroadcastReceiver() { // from class: com.treadly.Treadly.UI.TreadlyConnect.TreadlyConnectFragment.2
            @Override // android.content.BroadcastReceiver
            public void onReceive(Context context, Intent intent) {
                if (intent.getAction().equals("android.bluetooth.adapter.action.STATE_CHANGED")) {
                    int intExtra = intent.getIntExtra("android.bluetooth.adapter.extra.STATE", Integer.MIN_VALUE);
                    if (defaultAdapter.getState() == 13) {
                        final AlertDialog.Builder neutralButton = new AlertDialog.Builder(TreadlyConnectFragment.this.getContext()).setTitle("Warning").setMessage("Bluetooth is not enabled").setNeutralButton("Dismiss", (DialogInterface.OnClickListener) null);
                        ActivityUtil.runOnUiThread(TreadlyConnectFragment.this.getActivity(), new Runnable() { // from class: com.treadly.Treadly.UI.TreadlyConnect.TreadlyConnectFragment.2.1
                            @Override // java.lang.Runnable
                            public void run() {
                                neutralButton.show();
                            }
                        });
                    } else if (defaultAdapter.getState() == 12 || intExtra == 12) {
                        System.out.println("BLUETOOTH ALREADY ON");
                        TreadlyConnectFragment.this.autoConnect();
                        TreadlyConnectFragment.this.autoConnected = true;
                    }
                }
            }
        };
        if (getContext() != null) {
            getContext().registerReceiver(broadcastReceiver, new IntentFilter("android.bluetooth.adapter.action.STATE_CHANGED"));
        }
    }

    @Override // androidx.fragment.app.Fragment
    @Nullable
    public View onCreateView(@NonNull LayoutInflater layoutInflater, @Nullable ViewGroup viewGroup, @Nullable Bundle bundle) {
        RunningSessionManager.getInstance().initRunningSessionManager((MainActivity) getActivity());
        return layoutInflater.inflate(R.layout.fragment_treadly_connect, viewGroup, false);
    }

    @Override // com.treadly.Treadly.UI.Util.BaseClasses.BaseFragment, androidx.fragment.app.Fragment
    @SuppressLint({"ClickableViewAccessibility"})
    public void onViewCreated(@NonNull View view, @Nullable Bundle bundle) {
        super.onViewCreated(view, bundle);
        this.mainView = view;
        this.mScrollView = (ScrollView) view.findViewById(R.id.connect_scroll_view);
        initAlerts();
        initCountdown(view);
        this.isPageVisible = true;
        this.connectStatusTextView = (TextView) view.findViewById(R.id.connect_status_text);
        this.connectButton = (Button) view.findViewById(R.id.connect_button);
        this.connectButton.setOnClickListener(new View.OnClickListener() { // from class: com.treadly.Treadly.UI.TreadlyConnect.-$$Lambda$TreadlyConnectFragment$fhxc3DMtkja16vs09qC-FlfJvIo
            @Override // android.view.View.OnClickListener
            public final void onClick(View view2) {
                TreadlyConnectFragment.this.handleConnectButtonPressed();
            }
        });
        this.speedUpButton = (ImageButton) view.findViewById(R.id.speed_up_button);
        this.speedUpButton.setOnTouchListener(new View.OnTouchListener() { // from class: com.treadly.Treadly.UI.TreadlyConnect.-$$Lambda$TreadlyConnectFragment$_6FEpN2kFKX8GdT7CQq3MtIpqz0
            @Override // android.view.View.OnTouchListener
            public final boolean onTouch(View view2, MotionEvent motionEvent) {
                return TreadlyConnectFragment.lambda$onViewCreated$1(TreadlyConnectFragment.this, view2, motionEvent);
            }
        });
        this.speedDownButton = (ImageButton) view.findViewById(R.id.speed_down_button);
        this.speedDownButton.setOnTouchListener(new View.OnTouchListener() { // from class: com.treadly.Treadly.UI.TreadlyConnect.-$$Lambda$TreadlyConnectFragment$QfnfYd_GiBCfOvW2R_6gqFumgQQ
            @Override // android.view.View.OnTouchListener
            public final boolean onTouch(View view2, MotionEvent motionEvent) {
                return TreadlyConnectFragment.lambda$onViewCreated$2(TreadlyConnectFragment.this, view2, motionEvent);
            }
        });
        this.remoteSwitch = (TextView) view.findViewById(R.id.auto_text_view);
        this.remoteSwitch.setOnClickListener(new View.OnClickListener() { // from class: com.treadly.Treadly.UI.TreadlyConnect.TreadlyConnectFragment.3
            @Override // android.view.View.OnClickListener
            public void onClick(View view2) {
                TreadlyConnectFragment.this.setRemoteStatus();
            }
        });
        this.sliderFrameLayout = (FrameLayout) view.findViewById(R.id.slider_layout);
        this.sliderFrameLayout.setClipToOutline(true);
        this.sliderFragment = new TreadlyConnectSliderFragment();
        this.sliderFragment.parentFragment = this;
        this.sliderFragment.listener = this.adapter;
        if (getActivity() != null) {
            FragmentTransaction beginTransaction = getActivity().getSupportFragmentManager().beginTransaction();
            TreadlyConnectSliderFragment treadlyConnectSliderFragment = this.sliderFragment;
            TreadlyConnectSliderFragment treadlyConnectSliderFragment2 = this.sliderFragment;
            beginTransaction.add(R.id.slider_layout, treadlyConnectSliderFragment, TreadlyConnectSliderFragment.TAG).commit();
        }
        this.pauseStateAlertContainer = (LinearLayout) view.findViewById(R.id.pause_view_layout);
        this.pauseStateAlertContainer.setVisibility(8);
        initSpeedOptions(view);
        try {
            initUi(view);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        if (this.newUser) {
            ((MainActivity) getActivity()).hideBottomNavigationView();
            toDeviceConnectFragment();
        } else {
            this.haveCheckedDynamicBanners = false;
            checkDynamicBanners();
        }
        TreadlyActivationManager.shared.initializeTabs();
        RunningSessionManager.getInstance().context = getContext();
        handleCurrentCaloriesUpdated();
        NotificationCenter.addObserver(getContext(), NotificationType.onProfileChanged, this.notificationResponseReceiver);
        NotificationCenter.addObserver(getContext(), NotificationType.onDailyGoalChanged, this.notificationResponseReceiver);
        NotificationCenter.addObserver(getContext(), NotificationType.onNotificationsChanged, this.notificationResponseReceiver);
        NotificationCenter.addObserver(getContext(), NotificationType.onIrDebugOn, this.notificationResponseReceiver);
        NotificationCenter.addObserver(getContext(), NotificationType.onIrDebugOff, this.notificationResponseReceiver);
        NotificationCenter.addObserver(getContext(), NotificationType.onAdminMode, this.notificationResponseReceiver);
        NotificationCenter.addObserver(getContext(), NotificationType.didUpdateCurrentCalories, this.notificationResponseReceiver);
        setConnectState(ConnectionStatus.notConnected);
        autoConnect();
    }

    public static /* synthetic */ boolean lambda$onViewCreated$1(TreadlyConnectFragment treadlyConnectFragment, View view, MotionEvent motionEvent) {
        if (treadlyConnectFragment.isConnected) {
            switch (motionEvent.getAction()) {
                case 0:
                    treadlyConnectFragment.onSpeedUpButtonTouchDown();
                    break;
                case 1:
                    treadlyConnectFragment.onSpeedUpButtonTouchUp();
                    break;
            }
            return false;
        }
        return false;
    }

    public static /* synthetic */ boolean lambda$onViewCreated$2(TreadlyConnectFragment treadlyConnectFragment, View view, MotionEvent motionEvent) {
        if (treadlyConnectFragment.isConnected) {
            switch (motionEvent.getAction()) {
                case 0:
                    treadlyConnectFragment.onSpeedDownButtonTouchDown();
                    break;
                case 1:
                    treadlyConnectFragment.onSpeedDownButtonTouchUp();
                    break;
            }
            return false;
        }
        return false;
    }

    private boolean isViewVisible(View view) {
        Rect rect = new Rect();
        this.mScrollView.getDrawingRect(rect);
        float y = view.getY();
        return ((float) rect.top) < y && ((float) rect.bottom) > ((float) view.getHeight()) + y;
    }

    @Override // androidx.fragment.app.Fragment
    public void onStart() {
        super.onStart();
        MainActivity mainActivity = (MainActivity) getActivity();
        if (mainActivity != null) {
            mainActivity.showBottomNavigationView();
        }
        enableUi(this.isConnected);
        TreadlyClientLib.shared.addDeviceConnectionEventListener(this);
        TreadlyClientLib.shared.addRequestEventListener(this.requestEventAdapter);
        ActivationManager.shared.addActivationEventListener(this.deviceActivationAdapter);
        TreadlyClientLib.shared.addUserInteractionEventListener(this.userInteractionListener);
        TreadlyClientLib.shared.addDeviceUserStatsLogEventListener(this.deviceUserStatsLogEventListener);
        TreadlyClientLib.shared.addOtaUpdateRequestEventListener(this.otaUpdateRequestEventAdapter);
        fetchUserProfile();
        fetchUserNotifications();
        RunningSessionManager.getInstance().fetchDailyGoals();
        updateCalendar();
    }

    public void addConnectBackStackCallback() {
        final FragmentActivity activity = getActivity();
        if (activity == null) {
            return;
        }
        activity.getSupportFragmentManager().addOnBackStackChangedListener(new FragmentManager.OnBackStackChangedListener() { // from class: com.treadly.Treadly.UI.TreadlyConnect.-$$Lambda$TreadlyConnectFragment$0DNP7cGfMywvDWEVK3iIDP95yvY
            @Override // androidx.fragment.app.FragmentManager.OnBackStackChangedListener
            public final void onBackStackChanged() {
                TreadlyConnectFragment.lambda$addConnectBackStackCallback$3(TreadlyConnectFragment.this, activity);
            }
        });
    }

    public static /* synthetic */ void lambda$addConnectBackStackCallback$3(TreadlyConnectFragment treadlyConnectFragment, FragmentActivity fragmentActivity) {
        FragmentManager supportFragmentManager = fragmentActivity.getSupportFragmentManager();
        if (supportFragmentManager != null) {
            List<Fragment> fragments = supportFragmentManager.getFragments();
            if (fragments.size() <= 0 || fragments.get(fragments.size() - 1) != treadlyConnectFragment.sliderFragment) {
                return;
            }
            treadlyConnectFragment.onFragmentReturning();
        }
    }

    @Override // com.treadly.Treadly.UI.Util.BaseClasses.BaseFragment
    public void onFragmentReturning() {
        super.onFragmentReturning();
        checkDynamicBanners();
    }

    @Override // androidx.fragment.app.Fragment
    public void onPause() {
        super.onPause();
        if (getActivity() != null) {
            ((MainActivity) getActivity()).hideBottomNavigationView();
            stopDailyGoalsTimer();
            System.out.println("LIFECYCLE PAUSE");
        }
    }

    @Override // androidx.fragment.app.Fragment
    public void onDestroy() {
        super.onDestroy();
        NotificationCenter.removeObserver(getContext(), this.notificationResponseReceiver);
    }

    public void removeEventListeners() {
        TreadlyClientLib.shared.removeDeviceConnectionEventListener(this);
        TreadlyClientLib.shared.removeRequestEventListener(this.requestEventAdapter);
        TreadlyClientLib.shared.removeUserInteractionEventListener(this.userInteractionListener);
        TreadlyClientLib.shared.removeDeviceUserStatsLogEventListener(this.deviceUserStatsLogEventListener);
        ActivationManager.shared.removeActivationEventListener(this.deviceActivationAdapter);
        TreadlyClientLib.shared.removeOtaUpdateRequestEventListener(this.otaUpdateRequestEventAdapter);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean hasActivatedDevice() {
        return TreadlyActivationManager.shared.hasActivatedDevice();
    }

    private void toDeviceConnectFragment() {
        if (getActivity() == null || getActivity().getSupportFragmentManager() == null) {
            return;
        }
        dismissDeviceConnectPage();
        this.deviceConnectFragment = new TreadlyDeviceConnectStatusFragment();
        this.deviceConnectFragment.listener = new TreadlyDeviceConnectFragment.TreadlyDeviceConnectConnectFragmentEventListener() { // from class: com.treadly.Treadly.UI.TreadlyConnect.-$$Lambda$TreadlyConnectFragment$uF-MqXqudNQhQagAOdgqEqd-COM
            @Override // com.treadly.Treadly.UI.TreadlyConnect.TreadlyDeviceConnectFragment.TreadlyDeviceConnectConnectFragmentEventListener
            public final void onConnectDevice(DeviceInfo deviceInfo) {
                TreadlyConnectFragment.lambda$toDeviceConnectFragment$4(TreadlyConnectFragment.this, deviceInfo);
            }
        };
        this.deviceConnectFragment.connectedDeviceInfo = this.currentDeviceInfo;
        this.isDeviceConnectPageVisible = true;
        addFragmentToStack(this.deviceConnectFragment, TreadlyDeviceConnectStatusFragment.TAG, TAG, true);
    }

    public static /* synthetic */ void lambda$toDeviceConnectFragment$4(TreadlyConnectFragment treadlyConnectFragment, DeviceInfo deviceInfo) {
        if (deviceInfo != null) {
            treadlyConnectFragment.setConnectState(ConnectionStatus.connecting);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void dismissDeviceConnectPage() {
        if (this.deviceConnectFragment != null) {
            this.deviceConnectFragment.dismissDeviceConnectPage();
        }
        this.deviceConnectFragment = null;
        this.isDeviceConnectPageVisible = false;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void handleConnectButtonPressed() {
        switch (this.connectState) {
            case notConnected:
            case connected:
                toDeviceConnectFragment();
                return;
            case disconnecting:
                TreadlyClientLib.shared.disconnect();
                setConnectState(this.connectState);
                return;
            case connecting:
                toDeviceConnectFragment();
                this.autoConnected = true;
                return;
            default:
                return;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void handleConnectionChange(DeviceConnectionEvent deviceConnectionEvent) {
        MainActivity mainActivity;
        if (deviceConnectionEvent.getStatus() == DeviceConnectionStatus.connected) {
            if (this.connectingViewControllerSkipPressed) {
                return;
            }
            setConnectState(ConnectionStatus.reconnecting);
            setAuthenticationState();
            ActivityUtil.runOnUiThread(getActivity(), new Runnable() { // from class: com.treadly.Treadly.UI.TreadlyConnect.TreadlyConnectFragment.4
                @Override // java.lang.Runnable
                public void run() {
                    TreadlyConnectFragment.this.initAlerts();
                }
            });
            this.currentDeviceInfo = deviceConnectionEvent.getDeviceInfo();
            if (deviceConnectionEvent.getDeviceInfo() != null) {
                this.deviceName = deviceConnectionEvent.getDeviceInfo().getName();
            }
            TreadlyClientLib.shared.getDeviceComponentList();
            SharedPreferences.shared.storeConnectedDeviceName(this.deviceName);
            this.userInteractionView.handleDeviceConnected();
            this.autoConnected = true;
            RunningSessionManager.getInstance().fetchCurrentCalories();
            AppActivityManager.shared.handleDeviceConnected();
            this.presentedActivationDialog = false;
            this.presentedUnprocessedDialog = false;
            this.authenticationStateChecked = false;
        } else if (deviceConnectionEvent.getStatus() == DeviceConnectionStatus.disconnecting) {
            setConnectState(ConnectionStatus.notConnected);
            setAuthenticationState(AuthenticationState.unknown);
            this.deviceName = null;
            this.currentDeviceInfo = null;
            TreadlyLogManager.shared.finishLogging();
            RunningSessionManager.getInstance().stopRunSession();
            RunningSessionManager.getInstance().clearStats();
            DeviceUserStatsLogManager.getInstance().clearPendingActivityPost();
            this.hasCheckedOtaVersion = false;
            this.otaRequired = false;
            resetUi();
        } else if (deviceConnectionEvent.getStatus() == DeviceConnectionStatus.notConnected) {
            this.errorShown = false;
            setConnectState(ConnectionStatus.notConnected);
            setAuthenticationState(AuthenticationState.unknown);
            if (TreadlyActivityUnclaimedFragment.isDisplaying && (mainActivity = (MainActivity) getActivity()) != null) {
                mainActivity.getSupportFragmentManager().popBackStack(TreadlyActivityUnclaimedFragment.TAG, 1);
            }
            this.deviceName = null;
            this.currentDeviceInfo = null;
            TreadlyLogManager.shared.finishLogging();
            RunningSessionManager.getInstance().stopRunSession();
            RunningSessionManager.getInstance().clearStats();
            DeviceUserStatsLogManager.getInstance().clearPendingActivityPost();
            this.hasCheckedOtaVersion = false;
            this.otaRequired = false;
            this.userInteractionView.handleDeviceDisconnected();
            resetUi();
            if (deviceConnectionEvent.getStatusCode() == ConnectionStatusCode.errorInvalidVersion) {
                showAlert("Error", "The version of the Treadly product is not supported by the app");
            }
            if (!this.autoConnected) {
                autoConnect();
            }
            if (deviceConnectionEvent.getDeviceInfo() == null) {
                return;
            }
            if (deviceConnectionEvent.getDeviceInfo() != null) {
                AppActivityManager.shared.handleDeviceDisconnected();
            }
            this.appActivityPendingConnect = false;
            if (this.componentList != null) {
                this.componentList = new ComponentInfo[this.componentList.length];
            } else {
                this.componentList = new ComponentInfo[0];
            }
            AppActivityManager.shared.clear();
            this.presentedActivationDialog = false;
            this.presentedUnprocessedDialog = false;
            this.authenticationStateChecked = false;
            this.authenticationStateUpdated = false;
            this.pendingDisconnect = false;
        } else {
            PrintStream printStream = System.out;
            printStream.println("Status not handled: " + deviceConnectionEvent.getStatus());
        }
    }

    private void handleDeviceFound(DeviceInfo deviceInfo) {
        if (deviceInfo.getName() == null) {
            return;
        }
        String name = deviceInfo.getName();
        String str = this.targetTreadmillName;
        if (str == null) {
            return;
        }
        if (name.equals(str) || hasEqualDeviceAddresses(name, str)) {
            TreadlyClientLib.shared.connectDevice(deviceInfo);
            this.targetTreadmillName = null;
        }
    }

    private boolean hasEqualDeviceAddresses(String str, String str2) {
        return str.length() >= 6 && str2.length() >= 6 && str.toLowerCase() == str2.toLowerCase();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void autoConnect() {
        String connectedDeviceName;
        if (this.autoConnected || (connectedDeviceName = SharedPreferences.shared.getConnectedDeviceName()) == null || connectedDeviceName.isEmpty()) {
            return;
        }
        this.activationTarget = connectedDeviceName;
        this.targetTreadmillName = connectedDeviceName;
        ActivityUtil.runOnUiThread(getActivity(), new Runnable() { // from class: com.treadly.Treadly.UI.TreadlyConnect.-$$Lambda$TreadlyConnectFragment$af9klyGbxSRmNmkTe5I4_0ATBhA
            @Override // java.lang.Runnable
            public final void run() {
                TreadlyConnectFragment.lambda$autoConnect$5(TreadlyConnectFragment.this);
            }
        });
        this.autoConnected = true;
    }

    public static /* synthetic */ void lambda$autoConnect$5(TreadlyConnectFragment treadlyConnectFragment) {
        TreadlyClientLib.shared.startScanning(30000L, false);
        treadlyConnectFragment.setConnectState(ConnectionStatus.connecting);
    }

    private void resetAutoConnect() {
        this.targetTreadmillName = null;
    }

    private void setConnectButton(ConnectionStatus connectionStatus) {
        int i;
        if (getContext() == null) {
            return;
        }
        boolean z = connectionStatus == ConnectionStatus.connected || connectionStatus == ConnectionStatus.reconnecting || connectionStatus == ConnectionStatus.connecting;
        int i2 = AnonymousClass45.$SwitchMap$com$treadly$Treadly$UI$TreadlyConnect$TreadlyConnectFragment$ConnectionStatus[connectionStatus.ordinal()];
        if (i2 != 2) {
            switch (i2) {
                case 4:
                case 5:
                    i = R.string.connecting;
                    break;
                default:
                    i = R.string.disconnected;
                    break;
            }
        } else {
            i = R.string.connected;
        }
        if (this.connectStatusTextView != null) {
            this.connectStatusTextView.setText(i);
            this.connectStatusTextView.setTextColor(ContextCompat.getColor(getContext(), z ? R.color.green_2 : R.color.red_1));
        }
    }

    private void updateStartState() {
        DeviceStatus deviceStatus = this.currentDeviceStatusInfo;
        if (deviceStatus == null) {
            return;
        }
        PrintStream printStream = System.out;
        printStream.println("LGK :: START STATE : MODE : " + deviceStatus.getMode());
        if (TreadlyClientLib.shared.isDeviceConnected()) {
            TreadlyConnectStartState treadlyConnectStartState = this.startState;
            if (deviceStatus.getMode() == DeviceMode.ACTIVE) {
                float targetSpeed = deviceStatus.getSpeedInfo().getTargetSpeed();
                if (treadlyConnectStartState == TreadlyConnectStartState.forcedStart) {
                    this.lastStartState = this.startState;
                    setStartState(TreadlyConnectStartState.start);
                    if (this.sliderFragment != null) {
                        this.sliderFragment.currentSliderSpeed = targetSpeed;
                    }
                } else if (treadlyConnectStartState == TreadlyConnectStartState.forcedStarting) {
                    this.lastStartState = this.startState;
                    setStartState(TreadlyConnectStartState.starting);
                } else if (targetSpeed == Utils.DOUBLE_EPSILON) {
                    this.lastStartState = this.startState;
                    setStartState((treadlyConnectStartState == TreadlyConnectStartState.stop || treadlyConnectStartState == TreadlyConnectStartState.starting) ? TreadlyConnectStartState.starting : TreadlyConnectStartState.stopping);
                    if (this.startState != TreadlyConnectStartState.starting) {
                        DeviceStatusVerification.shared.reset();
                    }
                } else {
                    this.lastStartState = this.startState;
                    setStartState(TreadlyConnectStartState.start);
                    if (this.sliderFragment != null && !this.sliderFragment.isSliderAnimating) {
                        this.sliderFragment.currentSliderSpeed = targetSpeed;
                    }
                }
            } else {
                this.lastStartState = this.startState;
                setStartState(TreadlyConnectStartState.stop);
                DeviceStatusVerification.shared.reset();
            }
            if (this.startState == TreadlyConnectStartState.start || this.startState == TreadlyConnectStartState.stopping) {
                checkSessionOwner(deviceStatus);
            }
            updateStopButton();
            return;
        }
        resetDashboardView();
        resetSpeedView();
        updateStopButton();
    }

    private void updateSpeedSliderStartState(TreadlyConnectStartState treadlyConnectStartState) {
        if (this.startState == TreadlyConnectStartState.stop || this.startState == TreadlyConnectStartState.start || this.startState == TreadlyConnectStartState.notConnected) {
            setPendingPause(false);
            setPendingStop(false);
        }
        if (this.sliderFragment != null) {
            this.sliderFragment.updateSpeedSliderStartState(treadlyConnectStartState);
        }
    }

    private void updateStopButton() {
        setStopButtonEnabled();
        setStopButton(this.startState);
    }

    private void setStopButtonEnabled() {
        if (this.currentDeviceStatusInfo != null && !this.currentDeviceStatusInfo.getPairingModeState() && checkSessionOwner(this.currentDeviceStatusInfo) && this.isConnected && !this.pendingPause && !this.pendingStop && !singleUserMode && this.startState != TreadlyConnectStartState.forcedStart && this.startState != TreadlyConnectStartState.starting && this.startState != TreadlyConnectStartState.stopping && this.startState != TreadlyConnectStartState.notConnected && !this.isReconnecting && !this.isCountDownRunning) {
            this.sliderFragment.setStopButton(true);
        } else {
            this.sliderFragment.setStopButton(false);
        }
    }

    private void setStopButton(TreadlyConnectStartState treadlyConnectStartState) {
        Context context = getContext();
        if (context == null) {
            return;
        }
        int color = ContextCompat.getColor(context, R.color.red_1);
        int color2 = ContextCompat.getColor(context, R.color.green_1);
        int color3 = ContextCompat.getColor(context, R.color.dark_1);
        boolean isStopButtonEnabled = isStopButtonEnabled();
        boolean z = true;
        switch (this.startState) {
            case start:
                TreadlyConnectSliderFragment treadlyConnectSliderFragment = this.sliderFragment;
                if (isStopButtonEnabled) {
                    color3 = color;
                }
                treadlyConnectSliderFragment.setStopButton(isStopButtonEnabled, "PAUSE", color3);
                this.stopButtonGreen = isStopButtonEnabled;
                break;
            case stop:
                int stopButtonColor = this.sliderFragment.getStopButtonColor();
                if (stopButtonColor == color || stopButtonColor == color3) {
                    stopButtonColor = isStopButtonEnabled ? color2 : color3;
                    this.stopButtonGreen = isStopButtonEnabled;
                } else {
                    z = false;
                }
                this.sliderFragment.setStopButton(isStopButtonEnabled, "START", stopButtonColor, z);
                break;
            case notConnected:
                this.sliderFragment.setStopButton(isStopButtonEnabled, "START", color3);
                this.stopButtonGreen = false;
                break;
            case starting:
                TreadlyConnectSliderFragment treadlyConnectSliderFragment2 = this.sliderFragment;
                if (isStopButtonEnabled) {
                    color3 = color2;
                }
                treadlyConnectSliderFragment2.setStopButton(isStopButtonEnabled, "STARTING", color3);
                this.stopButtonGreen = isStopButtonEnabled;
                break;
            case stopping:
                if (this.pendingStop && !this.pendingPause) {
                    TreadlyConnectSliderFragment treadlyConnectSliderFragment3 = this.sliderFragment;
                    if (isStopButtonEnabled) {
                        color3 = color;
                    }
                    treadlyConnectSliderFragment3.setStopButton(isStopButtonEnabled, "STOPPING", color3);
                } else if (this.pendingPause && !this.pendingStop) {
                    TreadlyConnectSliderFragment treadlyConnectSliderFragment4 = this.sliderFragment;
                    if (isStopButtonEnabled) {
                        color3 = color;
                    }
                    treadlyConnectSliderFragment4.setStopButton(isStopButtonEnabled, "PAUSING", color3);
                } else {
                    TreadlyConnectSliderFragment treadlyConnectSliderFragment5 = this.sliderFragment;
                    if (isStopButtonEnabled) {
                        color3 = color;
                    }
                    treadlyConnectSliderFragment5.setStopButton(isStopButtonEnabled, "STOPPING", color3);
                }
                this.stopButtonGreen = isStopButtonEnabled;
                break;
            case forcedStart:
                int stopButtonColor2 = this.sliderFragment.getStopButtonColor();
                if (stopButtonColor2 == color2 || stopButtonColor2 == color3) {
                    stopButtonColor2 = isStopButtonEnabled ? color : color3;
                    this.stopButtonGreen = isStopButtonEnabled;
                } else {
                    z = false;
                }
                this.sliderFragment.setStopButton(isStopButtonEnabled, "PAUSE", stopButtonColor2, z);
                break;
            case forcedStarting:
                TreadlyConnectSliderFragment treadlyConnectSliderFragment6 = this.sliderFragment;
                if (isStopButtonEnabled) {
                    color3 = color2;
                }
                treadlyConnectSliderFragment6.setStopButton(isStopButtonEnabled, "STARTING", color3);
                this.stopButtonGreen = isStopButtonEnabled;
                break;
        }
        if (this.speedUpButton != null) {
            this.speedUpButton.setEnabled(this.stopButtonGreen);
        }
        if (this.speedDownButton != null) {
            this.speedDownButton.setEnabled(this.stopButtonGreen);
        }
    }

    private void setStopButtonVisibility() {
        if (AnonymousClass45.$SwitchMap$com$treadly$Treadly$UI$TreadlyConnect$TreadlyConnectFragment$ConnectionStatus[this.connectState.ordinal()] == 2) {
            this.sliderFragment.setStopButtonVisibility(true);
        } else {
            this.sliderFragment.setStopButtonVisibility(false);
        }
    }

    private boolean isStopButtonEnabled() {
        return (this.currentDeviceStatusInfo == null || this.currentDeviceStatusInfo.getPairingModeState() || !checkSessionOwner(this.currentDeviceStatusInfo) || !this.isConnected || singleUserMode || this.startState == TreadlyConnectStartState.notConnected) ? false : true;
    }

    @Override // com.treadly.client.lib.sdk.Listeners.DeviceConnectionEventListener
    public void onDeviceConnectionChanged(final DeviceConnectionEvent deviceConnectionEvent) {
        ActivityUtil.runOnUiThread(getActivity(), new Runnable() { // from class: com.treadly.Treadly.UI.TreadlyConnect.TreadlyConnectFragment.5
            @Override // java.lang.Runnable
            public void run() {
                TreadlyConnectFragment.this.handleConnectionChange(deviceConnectionEvent);
            }
        });
    }

    private void resetUi() {
        resetInfoView();
        resetSpeedView();
        resetDashboardView();
        this.sliderFragment.resetSpeedView();
        DeviceStatusVerification.shared.clear();
    }

    private void initUi(View view) throws JSONException {
        initInfoView(view);
        initFriendsViews(view);
        initDashboardView(view);
        initSpeedView(view);
        initCalendar(view);
    }

    private void enableUi(boolean z) {
        if (!singleUserMode) {
            enableInfoView(z);
        }
        enableFriendsViews(z);
    }

    void checkDynamicBanners() {
        if (this.haveCheckedDynamicBanners || this.checkingDynamicBanners) {
            return;
        }
        this.checkingDynamicBanners = true;
        System.out.println("NRS :: checking dynamic banners");
        DynamicBannerManager.getDynamicBanner(DynamicBannerAppPage.connectPage, new DynamicBannerManager.DynamicBannerResponse() { // from class: com.treadly.Treadly.UI.TreadlyConnect.-$$Lambda$TreadlyConnectFragment$ujmDe-WcSwuqFjqvKz0221POesA
            @Override // com.treadly.Treadly.Data.Managers.DynamicBannerManager.DynamicBannerResponse
            public final void onResponse(String str, List list) {
                TreadlyConnectFragment.lambda$checkDynamicBanners$7(TreadlyConnectFragment.this, str, list);
            }
        });
    }

    public static /* synthetic */ void lambda$checkDynamicBanners$7(final TreadlyConnectFragment treadlyConnectFragment, String str, final List list) {
        treadlyConnectFragment.checkingDynamicBanners = false;
        treadlyConnectFragment.haveCheckedDynamicBanners = true;
        if (str != null || list == null || list.size() <= 0) {
            return;
        }
        ActivityUtil.runOnUiThread(treadlyConnectFragment.getActivity(), new Runnable() { // from class: com.treadly.Treadly.UI.TreadlyConnect.-$$Lambda$TreadlyConnectFragment$PYk46fbyg7WUZJFsTVlo7LnmsTQ
            @Override // java.lang.Runnable
            public final void run() {
                DynamicBannerManager.getInstance().displayBannerInfo(TreadlyConnectFragment.this.getContext(), (DynamicBannerInfo) list.get(0));
            }
        });
    }

    private void initInfoView(View view) {
        if (view == null || getContext() == null) {
            return;
        }
        this.timeTextView = (TextView) view.findViewById(R.id.time_text_view);
        this.stepsTextView = (TextView) view.findViewById(R.id.steps_text_view);
        this.distanceTextView = (TextView) view.findViewById(R.id.distance_text_view);
        this.timeTitleTextView = (TextView) view.findViewById(R.id.time_title_text_view);
        this.stepsTitleTextView = (TextView) view.findViewById(R.id.steps_title_text_view);
        this.distanceTitleTextView = (TextView) view.findViewById(R.id.distance_title_text_view);
        this.timeTitleTextView.setTextColor(ContextCompat.getColor(getContext(), R.color.connect_page_title_dc));
        this.stepsTitleTextView.setTextColor(ContextCompat.getColor(getContext(), R.color.connect_page_title_dc));
        this.distanceTitleTextView.setTextColor(ContextCompat.getColor(getContext(), R.color.connect_page_title_dc));
        this.timeTextView.setTextColor(ContextCompat.getColor(getContext(), R.color.connect_page_text_dc));
        this.stepsTextView.setTextColor(ContextCompat.getColor(getContext(), R.color.connect_page_text_dc));
        this.distanceTextView.setTextColor(ContextCompat.getColor(getContext(), R.color.connect_page_text_dc));
        this.distanceFormatter.setMaximumFractionDigits(2);
        this.distanceFormatter.setMinimumFractionDigits(1);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void updateInfoView() {
        DeviceStatus deviceStatus = this.currentDeviceStatusInfo;
        if (deviceStatus == null) {
            return;
        }
        this.timeTextView.setText(convertSecondsToTimeString(deviceStatus.getSeconds()));
        this.stepsTextView.setText(this.stepsFormatter.format(deviceStatus.getSteps()));
        this.distanceTextView.setText(String.format("%s %s", this.distanceFormatter.format(deviceStatus.getDistance()), getDistanceUnitString(deviceStatus.getDistanceUnits())));
        if (this.userProfile != null && deviceStatus.getDistanceUnits() != this.userProfile.units) {
            TreadlyClientLib.shared.setUnits(this.userProfile.units);
        }
        if (this.connectState == ConnectionStatus.reconnecting) {
            setConnectState(ConnectionStatus.connected);
        }
        if (!this.errorShown && AnonymousClass45.$SwitchMap$com$treadly$client$lib$sdk$Model$DeviceStatusCode[deviceStatus.getStatusCode().ordinal()] == 1) {
            this.errorShown = true;
            showDeviceStatusCodeAlert(deviceStatus.getStatusCode());
        }
        enableUi(checkSessionOwner(deviceStatus));
        if (this.pendingDisconnect) {
            TreadlyClientLib.shared.disconnect();
        }
    }

    boolean checkSessionOwner(DeviceStatus deviceStatus) {
        VersionInfo versionInfo = new VersionInfo(3, 14, 0);
        ComponentInfo bleComponent = getBleComponent();
        if (bleComponent == null) {
            return true;
        }
        VersionInfo versionInfo2 = bleComponent.getVersionInfo().getVersionInfo();
        if ((versionInfo2.isGreaterThan(versionInfo) || versionInfo2.isEqual(versionInfo)) && deviceStatus.isPoweredOn() && deviceStatus.getSpeedInfo().getTargetSpeed() > Utils.DOUBLE_EPSILON) {
            return deviceStatus.isSessionOwnership();
        }
        return true;
    }

    private boolean supportsMaintenance() {
        ComponentInfo bleComponent = getBleComponent();
        if (bleComponent == null || bleComponent.getVersionInfo() == null) {
            return false;
        }
        VersionInfo versionInfo = bleComponent.getVersionInfo().getVersionInfo();
        if (isGen3()) {
            VersionInfo versionInfo2 = new VersionInfo(3, 31, 0);
            return versionInfo.isGreaterThan(versionInfo2) || versionInfo.isEqual(versionInfo2);
        }
        return true;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean requiresMaintenancePause() {
        ComponentInfo bleComponent = getBleComponent();
        if (bleComponent == null || bleComponent.getVersionInfo() == null) {
            return false;
        }
        VersionInfo versionInfo = bleComponent.getVersionInfo().getVersionInfo();
        VersionInfo versionInfo2 = new VersionInfo(3, 30, 1);
        return (versionInfo.isGreaterThan(versionInfo2) || versionInfo.isEqual(versionInfo2)) ? false : true;
    }

    private void enableInfoView(boolean z) {
        if (getContext() == null) {
            return;
        }
        if (z) {
            this.stepsTextView.setTextColor(ContextCompat.getColor(getContext(), R.color.connect_page_text));
            this.timeTextView.setTextColor(ContextCompat.getColor(getContext(), R.color.connect_page_text));
            this.distanceTextView.setTextColor(ContextCompat.getColor(getContext(), R.color.connect_page_text));
            if (this.speedTextView != null) {
                this.speedTextView.setTextColor(ContextCompat.getColor(getContext(), R.color.connect_page_text));
            }
            this.stepsTitleTextView.setTextColor(ContextCompat.getColor(getContext(), R.color.connect_page_steps_title));
            this.timeTitleTextView.setTextColor(ContextCompat.getColor(getContext(), R.color.connect_page_time_title));
            this.distanceTitleTextView.setTextColor(ContextCompat.getColor(getContext(), R.color.connect_page_distance_title));
            this.remoteSwitch.setEnabled(true);
            return;
        }
        this.timeTitleTextView.setTextColor(ContextCompat.getColor(getContext(), R.color.connect_page_title_dc));
        this.stepsTitleTextView.setTextColor(ContextCompat.getColor(getContext(), R.color.connect_page_title_dc));
        this.distanceTitleTextView.setTextColor(ContextCompat.getColor(getContext(), R.color.connect_page_title_dc));
        this.timeTextView.setTextColor(ContextCompat.getColor(getContext(), R.color.connect_page_text_dc));
        this.stepsTextView.setTextColor(ContextCompat.getColor(getContext(), R.color.connect_page_text_dc));
        this.distanceTextView.setTextColor(ContextCompat.getColor(getContext(), R.color.connect_page_text_dc));
        if (this.speedTextView != null) {
            this.speedTextView.setTextColor(ContextCompat.getColor(getContext(), R.color.connect_page_text_dc));
        }
        this.remoteSwitch.setEnabled(false);
    }

    private void resetInfoView() {
        this.timeTextView.setText(R.string.empty_time_text);
        this.stepsTextView.setText(R.string.zero);
        this.distanceTextView.setText(R.string.empty_distance_text);
    }

    private String convertSecondsToTimeString(int i) {
        return String.format(Locale.getDefault(), "%02d:%02d:%02d", Integer.valueOf(i / DateTimeConstants.SECONDS_PER_HOUR), Integer.valueOf(i / 60), Integer.valueOf((i % DateTimeConstants.SECONDS_PER_HOUR) % 60));
    }

    private String getDistanceUnitString(DistanceUnits distanceUnits) {
        if (distanceUnits == null) {
            return "";
        }
        switch (distanceUnits) {
            case KM:
                return "km";
            case MI:
                return "mi";
            default:
                return "";
        }
    }

    private String getSpeedUnitString(DistanceUnits distanceUnits) {
        if (distanceUnits == null) {
            return "";
        }
        switch (distanceUnits) {
            case KM:
                return "Kph";
            case MI:
                return "Mph";
            default:
                return "";
        }
    }

    private void initSpeedView(View view) {
        this.speedTextView = (TextView) view.findViewById(R.id.speed_view);
        this.speedTextView.setOnClickListener(new View.OnClickListener() { // from class: com.treadly.Treadly.UI.TreadlyConnect.-$$Lambda$TreadlyConnectFragment$oQNs5l1JmYVRB0bxWspHKaU0whE
            @Override // android.view.View.OnClickListener
            public final void onClick(View view2) {
                TreadlyConnectFragment.this.attachSpeedOptions();
            }
        });
        resetSpeedView();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void updateSpeedView() {
        DeviceStatus deviceStatus = this.currentDeviceStatusInfo;
        if (deviceStatus != null && this.isConnected) {
            float targetSpeed = deviceStatus.getSpeedInfo().getTargetSpeed();
            if (getActivity() != null) {
                ((MainActivity) getActivity()).setSpeedIconValue(targetSpeed, this.userProfile.units);
            }
            this.speedTextView.setText(this.speedFormatter.format(targetSpeed));
            if (deviceStatus.isPoweredOn() && deviceStatus.getSpeedInfo().getTargetSpeed() > 0.0f && this.queuedSpeedValue > 0.0f) {
                TreadlyClientLib.shared.setSpeed(this.queuedSpeedValue);
            }
            if (this.queuedSpeedValue != 0.0f || this.sliderFragment.isPanning) {
                return;
            }
            if (this.statusCount == 0 && !this.sliderFragment.isSliderAnimating && !this.pendingPause) {
                this.sliderFragment.setSpeedLabels(deviceStatus.getSpeedInfo().getTargetSpeed());
            } else if (this.statusCount > 0) {
                this.statusCount--;
            }
        }
    }

    protected void setSpeed(float f) {
        DeviceStatus deviceStatus = this.currentDeviceStatusInfo;
        if (deviceStatus != null && TreadlyClientLib.shared.isDeviceConnected()) {
            float maximumSpeed = f > deviceStatus.getSpeedInfo().getMaximumSpeed() ? deviceStatus.getSpeedInfo().getMaximumSpeed() : f;
            if (f < deviceStatus.getSpeedInfo().getMinimumSpeed()) {
                maximumSpeed = deviceStatus.getSpeedInfo().getMinimumSpeed();
            }
            if (deviceStatus.isPoweredOn() && deviceStatus.getSpeedInfo().getTargetSpeed() > 0.0f) {
                TreadlyClientLib.shared.setSpeed(maximumSpeed);
                this.statusCount = 2;
            } else if (this.queuedSpeedValue == 0.0f || this.startState != TreadlyConnectStartState.start || this.startState != TreadlyConnectStartState.starting) {
                boolean powerOnDevice = powerOnDevice();
                this.pendingTreadmillStart = powerOnDevice;
                this.queuedSpeedValue = maximumSpeed;
                if (powerOnDevice) {
                    showCountDown();
                }
            } else {
                this.queuedSpeedValue = maximumSpeed;
            }
        }
    }

    private void resetSpeedView() {
        this.speedTextView.setText("0.0");
        MainActivity mainActivity = (MainActivity) getActivity();
        if (this.userProfile == null) {
            return;
        }
        mainActivity.setSpeedIconValue(Utils.DOUBLE_EPSILON, this.userProfile.units);
        removeSpeedOptions();
    }

    private void onSpeedUpButtonTouchUp() {
        DeviceStatus deviceStatus;
        if (singleUserMode || (deviceStatus = this.currentDeviceStatusInfo) == null || !deviceStatus.getPairingModeState()) {
        }
    }

    private void onSpeedUpButtonTouchDown() {
        DeviceStatus deviceStatus;
        if (singleUserMode || (deviceStatus = this.currentDeviceStatusInfo) == null || deviceStatus.getPairingModeState() || !this.isConnected || !checkSessionOwner(deviceStatus)) {
            return;
        }
        if (deviceStatus.isPoweredOn() && deviceStatus.getSpeedInfo().getTargetSpeed() > 0.0f) {
            TreadlyClientLib.shared.speedUp();
        } else if (this.startState == TreadlyConnectStartState.stop && powerOnDevice()) {
            this.pendingTreadmillStart = true;
            showCountDown();
        }
        impactOccured(this.speedUpButton);
    }

    private void onSpeedDownButtonTouchUp() {
        DeviceStatus deviceStatus;
        if (singleUserMode || (deviceStatus = this.currentDeviceStatusInfo) == null || !deviceStatus.getPairingModeState()) {
        }
    }

    private void onSpeedDownButtonTouchDown() {
        DeviceStatus deviceStatus;
        if (singleUserMode || (deviceStatus = this.currentDeviceStatusInfo) == null || deviceStatus.getPairingModeState() || !this.isConnected || !checkSessionOwner(deviceStatus)) {
            return;
        }
        TreadlyClientLib.shared.speedDown();
        impactOccured(this.speedUpButton);
    }

    protected boolean powerOffDevice() {
        if (this.customBoardVersion == null) {
            return false;
        }
        if (this.customBoardVersion.isGreaterThan(new VersionInfo(2, 5, 0))) {
            return TreadlyClientLib.shared.resetPower();
        }
        return TreadlyClientLib.shared.powerDevice();
    }

    protected boolean pauseDevice() {
        if (getBleComponent() == null) {
            return false;
        }
        if (getBleComponent().getVersionInfo().getVersionInfo().isGreaterThan(new VersionInfo(2, 14, 3))) {
            return TreadlyClientLib.shared.pauseDevice();
        }
        return TreadlyClientLib.shared.powerDevice();
    }

    protected boolean powerOnDevice() {
        DeviceStatus deviceStatus = this.currentDeviceStatusInfo;
        if (deviceStatus == null || deviceStatus.isPoweredOn()) {
            return false;
        }
        return deviceStatus.getMode() == DeviceMode.IDLE ? TreadlyClientLib.shared.powerDevice() : TreadlyClientLib.shared.powerDevice();
    }

    protected boolean valueForHandrail(float f, DistanceUnits distanceUnits, HandrailStatus handrailStatus) {
        if (handrailStatus == HandrailStatus.DOWN) {
            if (distanceUnits == DistanceUnits.MI) {
                if (f > 3.7d) {
                    return false;
                }
            } else if (f > 6.0d) {
                return false;
            }
            return true;
        }
        return true;
    }

    protected void showAlert(String str, String str2) {
        if (getContext() == null) {
            return;
        }
        final AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setMessage(str2);
        builder.setTitle(str);
        builder.setNeutralButton("Dismiss", new DialogInterface.OnClickListener() { // from class: com.treadly.Treadly.UI.TreadlyConnect.TreadlyConnectFragment.6
            @Override // android.content.DialogInterface.OnClickListener
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        ActivityUtil.runOnUiThread(getActivity(), new Runnable() { // from class: com.treadly.Treadly.UI.TreadlyConnect.TreadlyConnectFragment.7
            @Override // java.lang.Runnable
            public void run() {
                builder.create().show();
            }
        });
    }

    protected void showAlert(String str, String str2, DialogInterface.OnDismissListener onDismissListener) {
        if (getContext() == null) {
            return;
        }
        final AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setMessage(str2);
        builder.setTitle(str);
        builder.setNeutralButton("Dismiss", new DialogInterface.OnClickListener() { // from class: com.treadly.Treadly.UI.TreadlyConnect.TreadlyConnectFragment.8
            @Override // android.content.DialogInterface.OnClickListener
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        builder.setOnDismissListener(onDismissListener);
        ActivityUtil.runOnUiThread(getActivity(), new Runnable() { // from class: com.treadly.Treadly.UI.TreadlyConnect.TreadlyConnectFragment.9
            @Override // java.lang.Runnable
            public void run() {
                builder.create().show();
            }
        });
    }

    private void showDeviceStatusCodeAlert(DeviceStatusCode deviceStatusCode) {
        if (deviceStatusCode == DeviceStatusCode.NO_ERROR && this.deviceStatusAlert != null) {
            this.deviceStatusAlert.setOnDismissListener(new DialogInterface.OnDismissListener() { // from class: com.treadly.Treadly.UI.TreadlyConnect.TreadlyConnectFragment.10
                @Override // android.content.DialogInterface.OnDismissListener
                public void onDismiss(DialogInterface dialogInterface) {
                    TreadlyConnectFragment.this.deviceStatusAlert = null;
                }
            });
            ActivityUtil.runOnUiThread(getActivity(), new Runnable() { // from class: com.treadly.Treadly.UI.TreadlyConnect.TreadlyConnectFragment.11
                @Override // java.lang.Runnable
                public void run() {
                    TreadlyConnectFragment.this.deviceStatusAlert.dismiss();
                }
            });
        }
        if (this.deviceStatusAlert == null && this.isPageVisible) {
            if (deviceStatusCode == DeviceStatusCode.POWER_CYCLE_REQUIRED) {
                this.deviceStatusAlert = new AlertDialog.Builder(getContext()).create();
                ActivityUtil.runOnUiThread(getActivity(), new Runnable() { // from class: com.treadly.Treadly.UI.TreadlyConnect.TreadlyConnectFragment.12
                    @Override // java.lang.Runnable
                    public void run() {
                        TreadlyConnectFragment.this.deviceStatusAlert.setTitle("Error");
                        TreadlyConnectFragment.this.deviceStatusAlert.setMessage("An error occurred. Please restart the treadmill by turning it off then back on");
                        TreadlyConnectFragment.this.deviceStatusAlert.setButton(-3, "Dismiss", new DialogInterface.OnClickListener() { // from class: com.treadly.Treadly.UI.TreadlyConnect.TreadlyConnectFragment.12.1
                            @Override // android.content.DialogInterface.OnClickListener
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.dismiss();
                            }
                        });
                        TreadlyConnectFragment.this.deviceStatusAlert.show();
                    }
                });
            }
            if (deviceStatusCode == DeviceStatusCode.WIFI_ERROR) {
                this.deviceStatusAlert = new AlertDialog.Builder(getContext()).create();
                ActivityUtil.runOnUiThread(getActivity(), new Runnable() { // from class: com.treadly.Treadly.UI.TreadlyConnect.-$$Lambda$TreadlyConnectFragment$wHN6uc_RMtXo6f7Ry_o-r2yzcGI
                    @Override // java.lang.Runnable
                    public final void run() {
                        TreadlyConnectFragment.lambda$showDeviceStatusCodeAlert$10(TreadlyConnectFragment.this);
                    }
                });
            }
        }
    }

    public static /* synthetic */ void lambda$showDeviceStatusCodeAlert$10(final TreadlyConnectFragment treadlyConnectFragment) {
        treadlyConnectFragment.deviceStatusAlert.setTitle("Setting up Wifi");
        treadlyConnectFragment.deviceStatusAlert.setMessage("A wifi error has occurred. Check the wifi connection or update the wifi settings");
        treadlyConnectFragment.deviceStatusAlert.setButton(-1, "Update", new DialogInterface.OnClickListener() { // from class: com.treadly.Treadly.UI.TreadlyConnect.-$$Lambda$TreadlyConnectFragment$A0PY3nrOySx345tM02nOzQ9Ho_Y
            @Override // android.content.DialogInterface.OnClickListener
            public final void onClick(DialogInterface dialogInterface, int i) {
                TreadlyConnectFragment.lambda$null$9(TreadlyConnectFragment.this, dialogInterface, i);
            }
        });
        treadlyConnectFragment.deviceStatusAlert.setButton(-2, "Dismiss", (DialogInterface.OnClickListener) null);
        treadlyConnectFragment.deviceStatusAlert.show();
    }

    public static /* synthetic */ void lambda$null$9(TreadlyConnectFragment treadlyConnectFragment, DialogInterface dialogInterface, int i) {
        MainActivity mainActivity = (MainActivity) treadlyConnectFragment.getActivity();
        if (mainActivity != null) {
            if (TreadlyActivityUnclaimedFragment.isDisplaying) {
                mainActivity.getSupportFragmentManager().popBackStackImmediate(TreadlyActivityUnclaimedFragment.TAG, 1);
            }
            treadlyConnectFragment.hideCalendar();
            TreadlyProfileSettingsWifiSetupFragment treadlyProfileSettingsWifiSetupFragment = new TreadlyProfileSettingsWifiSetupFragment();
            treadlyProfileSettingsWifiSetupFragment.bringBottomNavBack = true;
            mainActivity.getSupportFragmentManager().beginTransaction().addToBackStack(null).add(R.id.activity_fragment_container_empty, treadlyProfileSettingsWifiSetupFragment).commit();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void checkOtaVersion(final ComponentInfo componentInfo) {
        if (this.hasCheckedOtaVersion || this.isDeviceConnectPageVisible) {
            return;
        }
        HandlerThread handlerThread = new HandlerThread("ServiceThread");
        handlerThread.start();
        new Handler(handlerThread.getLooper()).post(new Runnable() { // from class: com.treadly.Treadly.UI.TreadlyConnect.-$$Lambda$TreadlyConnectFragment$bsADx_gpHguOSurltJfSv2q2mO8
            @Override // java.lang.Runnable
            public final void run() {
                TreadlyConnectFragment.lambda$checkOtaVersion$11(TreadlyConnectFragment.this, componentInfo);
            }
        });
    }

    public static /* synthetic */ void lambda$checkOtaVersion$11(TreadlyConnectFragment treadlyConnectFragment, ComponentInfo componentInfo) {
        if (treadlyConnectFragment.customBoardVersion.getVersionInfo().isGreaterThan(OtaUpdateInfo.minVersion)) {
            TreadlyClientLib.shared.checkFirmwareUpdates(componentInfo);
        }
    }

    @Override // com.treadly.client.lib.sdk.Listeners.DeviceConnectionEventListener
    public void onDeviceConnectionDeviceDiscovered(DeviceInfo deviceInfo) {
        handleDeviceFound(deviceInfo);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* renamed from: com.treadly.Treadly.UI.TreadlyConnect.TreadlyConnectFragment$13  reason: invalid class name */
    /* loaded from: classes2.dex */
    public class AnonymousClass13 extends RequestEventAdapter {
        AnonymousClass13() {
        }

        @Override // com.treadly.client.lib.sdk.Listeners.RequestEventAdapter, com.treadly.client.lib.sdk.Listeners.RequestEventListener
        public void onRequestStatusResponse(final boolean z, final DeviceStatus deviceStatus) {
            ActivityUtil.runOnUiThread(TreadlyConnectFragment.this.getActivity(), new Runnable() { // from class: com.treadly.Treadly.UI.TreadlyConnect.-$$Lambda$TreadlyConnectFragment$13$YWfA2p-G5Ci9hwAFAq19xjr9EmI
                @Override // java.lang.Runnable
                public final void run() {
                    TreadlyConnectFragment.AnonymousClass13.lambda$onRequestStatusResponse$0(TreadlyConnectFragment.AnonymousClass13.this, z, deviceStatus);
                }
            });
        }

        public static /* synthetic */ void lambda$onRequestStatusResponse$0(AnonymousClass13 anonymousClass13, boolean z, DeviceStatus deviceStatus) {
            if (!z || deviceStatus == null) {
                return;
            }
            if ((TreadlyConnectFragment.this.connectState == ConnectionStatus.connected || TreadlyConnectFragment.this.connectState == ConnectionStatus.reconnecting) && TreadlyConnectFragment.this.isConnected) {
                TreadlyConnectFragment.this.setCurrentDeviceStatusInfo(deviceStatus);
                DeviceStatusVerification.shared.verifyDeviceStatus(deviceStatus);
                TreadlyConnectFragment.this.updateDeviceUserStatsLogs();
                TreadlyConnectFragment.this.updateInfoView();
                TreadlyConnectFragment.this.updateSpeedView();
                TreadlyConnectFragment.this.updateDashboardView();
                TreadlyConnectFragment.this.updateLogs();
                TreadlyConnectFragment.this.updateRunSession();
                if (TreadlyConnectFragment.this.userInteractionView != null) {
                    TreadlyConnectFragment.this.userInteractionView.updateCurrent(deviceStatus.getCurrent());
                }
                boolean unused = TreadlyConnectFragment.isFirstStatus = false;
            }
        }

        @Override // com.treadly.client.lib.sdk.Listeners.RequestEventAdapter, com.treadly.client.lib.sdk.Listeners.RequestEventListener
        public void onRequestGetDeviceComponentsListResponse(boolean z, ComponentInfo[] componentInfoArr) {
            if (z) {
                TreadlyConnectFragment.this.componentList = componentInfoArr;
                for (ComponentInfo componentInfo : componentInfoArr) {
                    if (componentInfo.getType() == ComponentType.bleBoard) {
                        TreadlyConnectFragment.this.serialNumber = String.format(Locale.getDefault(), "%08d", Long.valueOf(componentInfo.getSerialNumber()));
                        TreadlyConnectFragment.this.macAddress = componentInfo.getId();
                        DeviceStatusVerification.shared.customBoardVersion = componentInfo.getVersionInfo().getVersionInfo();
                        TreadlyConnectFragment.this.customBoardVersion = componentInfo.getVersionInfo();
                        RunningSessionManager.getInstance().customboardVersion = componentInfo.getVersionInfo().getVersionInfo();
                        TreadlyConnectFragment.this.checkOtaVersion(componentInfo);
                        TreadlyConnectFragment.this.userInteractionView.deviceAddress = TreadlyConnectFragment.this.macAddress;
                        TreadlyServiceManager.getInstance().getSingleUserMode(TreadlyConnectFragment.this.macAddress, new AnonymousClass1());
                    }
                    if (componentInfo.getType() == ComponentType.mainBoard) {
                        TreadlyConnectFragment.this.mainBoardVersion = componentInfo.getVersionInfo();
                    }
                }
                AppActivityManager.shared.componentList = Arrays.asList(componentInfoArr);
            }
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        /* renamed from: com.treadly.Treadly.UI.TreadlyConnect.TreadlyConnectFragment$13$1  reason: invalid class name */
        /* loaded from: classes2.dex */
        public class AnonymousClass1 extends TreadlyServiceResponseEventAdapter {
            AnonymousClass1() {
            }

            @Override // com.treadly.Treadly.Data.Listeners.TreadlyServiceResponseEventAdapter, com.treadly.Treadly.Data.Listeners.TreadlyServiceResponseEventListener
            public void onUserInfo(String str, final UserInfo userInfo) {
                super.onUserInfo(str, userInfo);
                DeviceUserStatsLogManager.singleUserChecked = true;
                String userId = TreadlyServiceManager.getInstance().getUserId();
                if (str == null && !userId.equals(userInfo.id)) {
                    ActivityUtil.runOnUiThread(TreadlyConnectFragment.this.getActivity(), new Runnable() { // from class: com.treadly.Treadly.UI.TreadlyConnect.-$$Lambda$TreadlyConnectFragment$13$1$AEsDdJR9sWWUBhRVxbdwKr9Vmco
                        @Override // java.lang.Runnable
                        public final void run() {
                            TreadlyConnectFragment.AnonymousClass13.AnonymousClass1.lambda$onUserInfo$0(TreadlyConnectFragment.AnonymousClass13.AnonymousClass1.this, userInfo);
                        }
                    });
                    return;
                }
                if (str == null) {
                    boolean unused = TreadlyConnectFragment.singleUserMode = false;
                    DeviceUserStatsLogManager.isSingleUser = true;
                } else {
                    boolean unused2 = TreadlyConnectFragment.singleUserMode = false;
                    DeviceUserStatsLogManager.isSingleUser = false;
                }
                if (TreadlyActivationManager.shared.hasActivatedDevice()) {
                    if (TreadlyConnectFragment.this.customBoardVersion.isGreaterThan(new VersionInfo(3, 42, 0)) || TreadlyConnectFragment.this.customBoardVersion.isEqual(new VersionInfo(3, 42, 0))) {
                        TreadlyConnectFragment.this.checkUnclaimed_v2(TreadlyConnectFragment.this.macAddress);
                    } else if (TreadlyConnectFragment.this.customBoardVersion.isGreaterThan(new VersionInfo(3, 34, 0))) {
                        TreadlyConnectFragment.this.checkUnclaimed();
                    }
                } else if (TreadlyConnectFragment.this.deviceName != null) {
                    TreadlyConnectFragment.this.unclaimedListDisplayedDevices.put(TreadlyConnectFragment.this.deviceName, TreadlyConnectFragment.this.deviceName);
                }
                TreadlyConnectFragment.this.setAuthenticationState();
            }

            public static /* synthetic */ void lambda$onUserInfo$0(AnonymousClass1 anonymousClass1, UserInfo userInfo) {
                final AlertDialog.Builder message = new AlertDialog.Builder(TreadlyConnectFragment.this.getContext()).setTitle("Single User Mode Enabled").setMessage(String.format("Please contact %s to change from Single User mode to Multi-User mode", userInfo.name));
                if (!TreadlyConnectFragment.singleUserMode) {
                    FragmentActivity activity = TreadlyConnectFragment.this.getActivity();
                    message.getClass();
                    ActivityUtil.runOnUiThread(activity, new Runnable() { // from class: com.treadly.Treadly.UI.TreadlyConnect.-$$Lambda$3Qlr8fEzXfR35vEnzutL7VyLKKA
                        @Override // java.lang.Runnable
                        public final void run() {
                            message.show();
                        }
                    });
                }
                boolean unused = TreadlyConnectFragment.singleUserMode = true;
                DeviceUserStatsLogManager.isSingleUser = true;
            }
        }

        @Override // com.treadly.client.lib.sdk.Listeners.RequestEventAdapter, com.treadly.client.lib.sdk.Listeners.RequestEventListener
        public void onRequestSetSpeedResponse(boolean z) {
            if (z) {
                TreadlyConnectFragment.this.queuedSpeedValue = 0.0f;
            }
        }

        @Override // com.treadly.client.lib.sdk.Listeners.RequestEventAdapter, com.treadly.client.lib.sdk.Listeners.RequestEventListener
        public void onRequestSetAuthenticationState(final boolean z, final AuthenticationState authenticationState) {
            ActivityUtil.runOnUiThread(TreadlyConnectFragment.this.getActivity(), new Runnable() { // from class: com.treadly.Treadly.UI.TreadlyConnect.TreadlyConnectFragment.13.2
                @Override // java.lang.Runnable
                public void run() {
                    if (z && TreadlyConnectFragment.this.isGen2()) {
                        TreadlyActivationManager.shared.handleAuthenticationState(TreadlyConnectFragment.this.deviceName, authenticationState);
                        TreadlyConnectFragment.this.setAuthenticationState(authenticationState);
                    }
                }
            });
        }

        @Override // com.treadly.client.lib.sdk.Listeners.RequestEventAdapter, com.treadly.client.lib.sdk.Listeners.RequestEventListener
        public void onRequestGetAuthenticationState(final boolean z, final AuthenticationState authenticationState) {
            ActivityUtil.runOnUiThread(TreadlyConnectFragment.this.getActivity(), new Runnable() { // from class: com.treadly.Treadly.UI.TreadlyConnect.TreadlyConnectFragment.13.3
                @Override // java.lang.Runnable
                public void run() {
                    if (z && TreadlyConnectFragment.this.isGen2()) {
                        TreadlyActivationManager.shared.handleAuthenticationState(TreadlyConnectFragment.this.deviceName, authenticationState);
                        TreadlyConnectFragment.this.setAuthenticationState(authenticationState);
                        TreadlyConnectFragment.this.authenticationStateChecked = true;
                        if (!TreadlyConnectFragment.this.presentedActivationDialog && ((TreadlyConnectFragment.this.isPageVisible || TreadlyConnectFragment.this.isDeviceConnectPageVisible) && TreadlyConnectFragment.this.authenticationStateUpdated && authenticationState != AuthenticationState.active && authenticationState != AuthenticationState.unknown)) {
                            TreadlyConnectFragment.this.presentedActivationDialog = true;
                            TreadlyClientLib.shared.startActivation();
                            TreadlyConnectFragment.this.setSendEnableBleOffSource = true;
                        }
                        if (authenticationState != AuthenticationState.unknown) {
                            TreadlyConnectFragment.this.dismissDeviceConnectPage();
                        }
                        ComponentInfo bleComponent = TreadlyConnectFragment.this.getBleComponent();
                        if (!TreadlyConnectFragment.this.presentedUnprocessedDialog && TreadlyConnectFragment.this.authenticationStateUpdated && ((TreadlyConnectFragment.this.isPageVisible || TreadlyConnectFragment.this.isDeviceConnectPageVisible) && authenticationState == AuthenticationState.unknown && bleComponent != null)) {
                            TreadlyConnectFragment.this.presentedUnprocessedDialog = true;
                            TreadlyClientLib.shared.getAuthenticateReferenceCode(bleComponent);
                        }
                        if (bleComponent != null) {
                            TreadlyConnectFragment.this.checkOtaVersion(bleComponent);
                        }
                        if (authenticationState == AuthenticationState.unknown) {
                            TreadlyActivationManager.shared.handleAuthenticationState(TreadlyConnectFragment.this.deviceName, AuthenticationState.disabled);
                            TreadlyConnectFragment.this.setAuthenticationState(AuthenticationState.disabled);
                        }
                    } else if (z || !TreadlyConnectFragment.this.isGen2()) {
                        TreadlyConnectFragment.this.dismissDeviceConnectPage();
                    } else {
                        TreadlyActivationManager.shared.handleAuthenticationState(TreadlyConnectFragment.this.deviceName, AuthenticationState.disabled);
                        TreadlyConnectFragment.this.setAuthenticationState(AuthenticationState.disabled);
                        TreadlyConnectFragment.this.authenticationStateChecked = true;
                        TreadlyConnectFragment.this.dismissDeviceConnectPage();
                    }
                }
            });
        }

        @Override // com.treadly.client.lib.sdk.Listeners.RequestEventAdapter, com.treadly.client.lib.sdk.Listeners.RequestEventListener
        public void onRequestFactoryResetResponse(boolean z) {
            super.onRequestFactoryResetResponse(z);
            if (TreadlyConnectFragment.this.isPageVisible) {
                if (z) {
                    DeviceStatusVerification.shared.reset();
                    TreadlyConnectFragment.this.showAlert("Factory Reset", "Device was successfully factory reset");
                    return;
                }
                TreadlyConnectFragment.this.showAlert("Error", "Error factory resetting device, Please verify the treadmill is powered off");
            }
        }
    }

    /* renamed from: com.treadly.Treadly.UI.TreadlyConnect.TreadlyConnectFragment$14  reason: invalid class name */
    /* loaded from: classes2.dex */
    class AnonymousClass14 extends DeviceActivationEventAdapter {
        AnonymousClass14() {
        }

        @Override // com.treadly.client.lib.sdk.Listeners.DeviceActivationEventAdapter, com.treadly.client.lib.sdk.Listeners.DeviceActivationEventListener
        public void onDeviceAuthenticationStateResponse(AuthenticationState authenticationState) {
            ActivityUtil.runOnUiThread(TreadlyConnectFragment.this.getActivity(), new Runnable() { // from class: com.treadly.Treadly.UI.TreadlyConnect.TreadlyConnectFragment.14.1
                @Override // java.lang.Runnable
                public void run() {
                    TreadlyConnectFragment.this.authenticationStateUpdated = true;
                    TreadlyConnectFragment.this.setAuthenticationState();
                }
            });
        }

        @Override // com.treadly.client.lib.sdk.Listeners.DeviceActivationEventAdapter, com.treadly.client.lib.sdk.Listeners.DeviceActivationEventListener
        public void onDeviceAuthenticateReferenceCodeInfoResponse(final boolean z, final AuthenticateReferenceCodeInfo authenticateReferenceCodeInfo) {
            ActivityUtil.runOnUiThread(TreadlyConnectFragment.this.getActivity(), new Runnable() { // from class: com.treadly.Treadly.UI.TreadlyConnect.TreadlyConnectFragment.14.2
                @Override // java.lang.Runnable
                public void run() {
                    if (!z || authenticateReferenceCodeInfo == null || authenticateReferenceCodeInfo.referenceCode == null) {
                        TreadlyConnectFragment.this.presentedUnprocessedDialog = false;
                    } else {
                        AlertDialog.Builder builder = new AlertDialog.Builder(TreadlyConnectFragment.this.getContext());
                        if (authenticateReferenceCodeInfo.title != null) {
                            builder.setTitle(authenticateReferenceCodeInfo.title);
                        }
                        if (authenticateReferenceCodeInfo.message != null) {
                            builder.setMessage(authenticateReferenceCodeInfo.message);
                        }
                        builder.setNegativeButton("Dismiss", (DialogInterface.OnClickListener) null);
                        builder.create().show();
                    }
                    TreadlyClientLib.shared.disconnect();
                    TreadlyActivationManager.shared.reset();
                }
            });
        }

        @Override // com.treadly.client.lib.sdk.Listeners.DeviceActivationEventAdapter, com.treadly.client.lib.sdk.Listeners.DeviceActivationEventListener
        public void onDeviceActivationStartResponse(final ActivationInfo activationInfo) {
            String str;
            if (TreadlyConnectFragment.this.isActivationInProgress) {
                return;
            }
            if (activationInfo != null) {
                do {
                } while (!TreadlyConnectFragment.this.hasCheckedOtaVersion);
                TreadlyConnectFragment.this.activationInfo = activationInfo;
                TreadlyConnectFragment.this.startActivationAlert.setTitle(activationInfo.activationTitle);
                TreadlyConnectFragment.this.startActivationAlert.setMessage(activationInfo.activationMessage);
                TreadlyConnectFragment.this.startActivationAlert.setButton(-1, "Activate", new DialogInterface.OnClickListener() { // from class: com.treadly.Treadly.UI.TreadlyConnect.TreadlyConnectFragment.14.3
                    @Override // android.content.DialogInterface.OnClickListener
                    public void onClick(DialogInterface dialogInterface, int i) {
                        TreadlyConnectFragment.this.isActivationInProgress = true;
                        ActivityUtil.runOnUiThread(TreadlyConnectFragment.this.getActivity(), new Runnable() { // from class: com.treadly.Treadly.UI.TreadlyConnect.TreadlyConnectFragment.14.3.1
                            @Override // java.lang.Runnable
                            public void run() {
                                TreadlyConnectFragment.this.submitActivationAlert.show();
                                TreadlyConnectFragment.this.updateActivationAlert();
                            }
                        });
                    }
                });
                TreadlyConnectFragment.this.startActivationAlert.setButton(-2, "Dismiss", new DialogInterface.OnClickListener() { // from class: com.treadly.Treadly.UI.TreadlyConnect.TreadlyConnectFragment.14.4
                    @Override // android.content.DialogInterface.OnClickListener
                    public void onClick(DialogInterface dialogInterface, int i) {
                        TreadlyConnectFragment.this.startActivationAlert.dismiss();
                        TreadlyConnectFragment.this.isActivationInProgress = false;
                    }
                });
                ActivityUtil.runOnUiThread(TreadlyConnectFragment.this.getActivity(), new Runnable() { // from class: com.treadly.Treadly.UI.TreadlyConnect.TreadlyConnectFragment.14.5
                    @Override // java.lang.Runnable
                    public void run() {
                        TreadlyConnectFragment.this.startActivationAlert.show();
                    }
                });
                String str2 = TreadlyConnectFragment.this.activationInfo.activationSubmitMessage;
                if (!TreadlyClientLib.shared.isDeviceStatusConnected()) {
                    if (TreadlyConnectFragment.this.getBleComponent() == null) {
                        str = str2 + "\n\nStatus: Disconnected\nPlease reconnect";
                    } else if (TreadlyConnectFragment.this.isGen2()) {
                        str = str2 + "\n\nStatus: Disconnected\nPlease reconnect";
                    } else {
                        str = str2 + "\n\nStatus: Disconnected\nPlease tap 3 times in the constant section to start the pairing process";
                    }
                } else {
                    str = str2 + "\n\nStatus: Connected";
                }
                TreadlyConnectFragment.this.submitActivationAlert.setTitle(activationInfo.activationSubmitTitle);
                TreadlyConnectFragment.this.submitActivationAlert.setMessage(str);
                TreadlyConnectFragment.this.submitActivationAlert.setButton(-3, "Dismiss", new DialogInterface.OnClickListener() { // from class: com.treadly.Treadly.UI.TreadlyConnect.TreadlyConnectFragment.14.6
                    @Override // android.content.DialogInterface.OnClickListener
                    public void onClick(DialogInterface dialogInterface, int i) {
                        TreadlyConnectFragment.this.submitActivationAlert.dismiss();
                        TreadlyConnectFragment.this.submitActivationAlert = null;
                        TreadlyConnectFragment.this.isActivationInProgress = false;
                    }
                });
                if (activationInfo.activationUrl != null && !activationInfo.activationUrl.isEmpty() && Uri.parse(activationInfo.activationUrl) != null) {
                    TreadlyConnectFragment.this.submitActivationAlert.setButton(-3, "Open", new DialogInterface.OnClickListener() { // from class: com.treadly.Treadly.UI.TreadlyConnect.TreadlyConnectFragment.14.7
                        @Override // android.content.DialogInterface.OnClickListener
                        public void onClick(DialogInterface dialogInterface, int i) {
                            Intent intent = new Intent();
                            intent.setAction("android.intent.action.VIEW");
                            intent.addCategory("android.intent.category.BROWSABLE");
                            intent.setData(Uri.parse(activationInfo.activationUrl));
                            TreadlyConnectFragment.this.startActivity(intent);
                        }
                    });
                }
                if (TreadlyConnectFragment.this.getContext() != null) {
                    final AppCompatEditText appCompatEditText = new AppCompatEditText(TreadlyConnectFragment.this.getContext());
                    appCompatEditText.setHint("Enter activation code");
                    appCompatEditText.addTextChangedListener(TreadlyConnectFragment.this.textWatcher);
                    TreadlyConnectFragment.this.submitActivationAlert.setView(appCompatEditText);
                    TreadlyConnectFragment.this.submitActivationAlert.setButton(-1, "Activate", new DialogInterface.OnClickListener() { // from class: com.treadly.Treadly.UI.TreadlyConnect.TreadlyConnectFragment.14.8
                        @Override // android.content.DialogInterface.OnClickListener
                        public void onClick(DialogInterface dialogInterface, int i) {
                            if (appCompatEditText.getText() != null && TreadlyConnectFragment.this.getBleComponent() != null) {
                                TreadlyClientLib.shared.submitActivationCode(appCompatEditText.getText().toString(), TreadlyConnectFragment.this.getBleComponent());
                            }
                            TreadlyConnectFragment.this.submitActivationAlert.dismiss();
                            TreadlyConnectFragment.this.submitActivationAlert = null;
                        }
                    });
                    TreadlyConnectFragment.this.updateActivationAction();
                    TreadlyConnectFragment.this.completeActivationAlert.setTitle(activationInfo.activationSuccessTitle);
                    TreadlyConnectFragment.this.completeActivationAlert.setMessage(activationInfo.activationSuccessMessage);
                    TreadlyConnectFragment.this.completeActivationAlert.setButton(-3, "Dismiss", new DialogInterface.OnClickListener() { // from class: com.treadly.Treadly.UI.TreadlyConnect.TreadlyConnectFragment.14.9
                        @Override // android.content.DialogInterface.OnClickListener
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.dismiss();
                        }
                    });
                    TreadlyConnectFragment.this.errorActivationAlert.setTitle(activationInfo.activationErrorTitle);
                    TreadlyConnectFragment.this.errorActivationAlert.setMessage(activationInfo.activationErrorMessage);
                    TreadlyConnectFragment.this.errorActivationAlert.setButton(-3, "Dismiss", new DialogInterface.OnClickListener() { // from class: com.treadly.Treadly.UI.TreadlyConnect.TreadlyConnectFragment.14.10
                        @Override // android.content.DialogInterface.OnClickListener
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.dismiss();
                        }
                    });
                    TreadlyConnectFragment.this.finishActivationAlert.setTitle("Activation");
                    TreadlyConnectFragment.this.finishActivationAlert.setMessage("Finishing activation. Please wait");
                    return;
                }
                return;
            }
            TreadlyConnectFragment.this.showAlert("Error", "Error starting activation process");
        }

        @Override // com.treadly.client.lib.sdk.Listeners.DeviceActivationEventAdapter, com.treadly.client.lib.sdk.Listeners.DeviceActivationEventListener
        public void onDeviceActivationSubmitResponse(boolean z) {
            if (z) {
                TreadlyConnectFragment.this.pendingActivationBleEnable = true;
                TreadlyConnectFragment.this.setAuthenticationState();
                return;
            }
            TreadlyConnectFragment.this.pendingActivationBleEnable = false;
            if (TreadlyConnectFragment.this.errorActivationAlert == null) {
                return;
            }
            TreadlyConnectFragment.this.errorActivationAlert.setOnShowListener(new DialogInterface.OnShowListener() { // from class: com.treadly.Treadly.UI.TreadlyConnect.TreadlyConnectFragment.14.11
                @Override // android.content.DialogInterface.OnShowListener
                public void onShow(DialogInterface dialogInterface) {
                    TreadlyConnectFragment.this.isActivationInProgress = false;
                }
            });
            ActivityUtil.runOnUiThread(TreadlyConnectFragment.this.getActivity(), new Runnable() { // from class: com.treadly.Treadly.UI.TreadlyConnect.TreadlyConnectFragment.14.12
                @Override // java.lang.Runnable
                public void run() {
                    TreadlyConnectFragment.this.errorActivationAlert.show();
                }
            });
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* renamed from: com.treadly.Treadly.UI.TreadlyConnect.TreadlyConnectFragment$15  reason: invalid class name */
    /* loaded from: classes2.dex */
    public class AnonymousClass15 extends OtaUpdateRequestEventAdapter {
        AnonymousClass15() {
        }

        @Override // com.treadly.client.lib.sdk.Listeners.OtaUpdateRequestEventAdapter, com.treadly.client.lib.sdk.Listeners.OtaUpdateRequestEventListener
        public void onOtaUpdateFirmwareVersionAvailable(boolean z, FirmwareVersion firmwareVersion, String[] strArr, boolean z2) {
            if (TreadlyConnectFragment.this.hasCheckedOtaVersion) {
                return;
            }
            TreadlyConnectFragment.this.hasCheckedOtaVersion = true;
            if (z2 && z && firmwareVersion != null) {
                TreadlyConnectFragment.this.otaRequired = true;
                String format = String.format("An update to %s is available. Please press \"Update\" to begin", firmwareVersion.version);
                if (strArr != null) {
                    String str = "\n";
                    for (String str2 : strArr) {
                        str = str + String.format("\n• %s", str2);
                    }
                    new AlertDialog.Builder(TreadlyConnectFragment.this.getContext()).setTitle("Update Check").setMessage(format + str).setPositiveButton("Update", new DialogInterface.OnClickListener() { // from class: com.treadly.Treadly.UI.TreadlyConnect.-$$Lambda$TreadlyConnectFragment$15$tnt1yoDVfI2Hct3QaxDP6JgfJuw
                        @Override // android.content.DialogInterface.OnClickListener
                        public final void onClick(DialogInterface dialogInterface, int i) {
                            TreadlyConnectFragment.AnonymousClass15.lambda$onOtaUpdateFirmwareVersionAvailable$0(TreadlyConnectFragment.AnonymousClass15.this, dialogInterface, i);
                        }
                    }).setNegativeButton("Dismiss", new DialogInterface.OnClickListener() { // from class: com.treadly.Treadly.UI.TreadlyConnect.-$$Lambda$TreadlyConnectFragment$15$madTwNejtKHkYGUbYBjeXscgF8I
                        @Override // android.content.DialogInterface.OnClickListener
                        public final void onClick(DialogInterface dialogInterface, int i) {
                            TreadlyClientLib.shared.disconnect();
                        }
                    }).setOnDismissListener(new DialogInterface.OnDismissListener() { // from class: com.treadly.Treadly.UI.TreadlyConnect.-$$Lambda$TreadlyConnectFragment$15$SEQ7KEJAjtiU8DxHD8On2362Z0s
                        @Override // android.content.DialogInterface.OnDismissListener
                        public final void onDismiss(DialogInterface dialogInterface) {
                            TreadlyClientLib.shared.disconnect();
                        }
                    }).show();
                }
            }
        }

        public static /* synthetic */ void lambda$onOtaUpdateFirmwareVersionAvailable$0(AnonymousClass15 anonymousClass15, DialogInterface dialogInterface, int i) {
            TreadlyConnectFragment.this.hideBottomNavigation();
            TreadlyConnectFragment.this.hideCalendar();
            if (TreadlyConnectFragment.this.getActivity() != null) {
                FirmwareUpdateSsidFragment firmwareUpdateSsidFragment = new FirmwareUpdateSsidFragment();
                firmwareUpdateSsidFragment.updateRequired = true;
                firmwareUpdateSsidFragment.updateEventListener = new FirmwareUpdateSsidFragment.FirmwareUpdateEventListener() { // from class: com.treadly.Treadly.UI.TreadlyConnect.TreadlyConnectFragment.15.1
                    @Override // com.treadly.Treadly.UI.FirmwareUpdate.FirmwareUpdateSsidFragment.FirmwareUpdateEventListener
                    public void updateRequiredSkipped() {
                        TreadlyConnectFragment.this.pendingDisconnect = true;
                        TreadlyClientLib.shared.disconnect();
                    }
                };
                firmwareUpdateSsidFragment.returnShowBottomNav = true;
                TreadlyConnectFragment.this.getActivity().getSupportFragmentManager().beginTransaction().addToBackStack(null).add(R.id.activity_fragment_container_empty, firmwareUpdateSsidFragment).commit();
            }
        }
    }

    void reconnectActivationTimeout() {
        this.completeActivationAlert.dismiss();
    }

    void finishActivationTimeout() {
        this.finishActivationAlert.dismiss();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void setAuthenticationState(AuthenticationState authenticationState) {
        this.authenticationState = authenticationState;
        setIsConnected((this.connectState == ConnectionStatus.connected || this.connectState == ConnectionStatus.reconnecting) && authenticationState == AuthenticationState.active);
        if (this.authenticationState != null && this.authenticationState != AuthenticationState.active) {
            resetUi();
            AuthenticationState authenticationState2 = this.authenticationState;
            AuthenticationState authenticationState3 = AuthenticationState.unknown;
            if (this.connectState == ConnectionStatus.reconnecting) {
                setConnectState(ConnectionStatus.connected);
            }
        }
        updateStopButton();
    }

    void setAuthenticationState() {
        if (getBleComponent() == null) {
            return;
        }
        if (isGen2()) {
            TreadlyClientLib.shared.getAuthenticationState();
        } else if (this.deviceName == null) {
            TreadlyActivationManager.shared.handleAuthenticationState("", AuthenticationState.active);
        } else {
            TreadlyActivationManager.shared.handleAuthenticationState(this.deviceName, AuthenticationState.active);
            setAuthenticationState(AuthenticationState.active);
            ActivityUtil.runOnUiThread(getActivity(), new Runnable() { // from class: com.treadly.Treadly.UI.TreadlyConnect.TreadlyConnectFragment.17
                @Override // java.lang.Runnable
                public void run() {
                    TreadlyConnectFragment.this.dismissDeviceConnectPage();
                }
            });
        }
    }

    boolean isGen2() {
        if (getBleComponent() == null || getBleComponent().getVersionInfo() == null) {
            return false;
        }
        VersionInfo versionInfo = new VersionInfo(2, 0, 0);
        return getBleComponent().getVersionInfo().isGreaterThan(versionInfo) || getBleComponent().getVersionInfo().isEqual(versionInfo);
    }

    boolean isGen3() {
        if (getBleComponent() == null || getBleComponent().getVersionInfo() == null) {
            return false;
        }
        VersionInfo versionInfo = new VersionInfo(3, 0, 0);
        return getBleComponent().getVersionInfo().isGreaterThan(versionInfo) || getBleComponent().getVersionInfo().isEqual(versionInfo);
    }

    public ComponentInfo getBleComponent() {
        ComponentInfo[] componentInfoArr;
        if (this.componentList == null || this.componentList.length == 0) {
            return null;
        }
        for (ComponentInfo componentInfo : this.componentList) {
            if (componentInfo == null) {
                return null;
            }
            if (componentInfo.getType() == ComponentType.bleBoard) {
                return componentInfo;
            }
        }
        return null;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void setRemoteStatus() {
        if (TreadlyClientLib.shared.isDeviceConnected() && this.isConnected && this.customBoardVersion != null && this.customBoardVersion.isGreaterThan(new VersionInfo(2, 4, 0)) && this.currentDeviceStatusInfo != null) {
            switch (this.currentDeviceStatusInfo.getRemoteStatus()) {
                case automatic:
                    if (TreadlyClientLib.shared.setRemoteStatus(RemoteStatus.manual)) {
                        this.remoteStatusCount = 3;
                        this.remoteStatus = RemoteStatus.manual;
                        this.remoteSwitch.setText("M");
                        return;
                    }
                    return;
                case manual:
                    if (TreadlyClientLib.shared.setRemoteStatus(RemoteStatus.automatic)) {
                        this.remoteStatusCount = 3;
                        this.remoteStatus = RemoteStatus.automatic;
                        this.remoteSwitch.setText(ExifInterface.GPS_MEASUREMENT_IN_PROGRESS);
                        return;
                    }
                    return;
                default:
                    return;
            }
        }
    }

    public void checkDailygoals() {
        RunningSessionManager.getInstance().fetchDailyGoals();
        RunningSessionManager.getInstance().clearAnimations();
        RunningSessionManager.getInstance().checkDailyGoals();
    }

    public void fetchUserNotifications() {
        VideoServiceHelper.getNotificationSettings(new VideoServiceHelper.VideoNotificationsListener() { // from class: com.treadly.Treadly.UI.TreadlyConnect.TreadlyConnectFragment.18
            @Override // com.treadly.Treadly.UI.TreadlyVideo.Data.VideoServiceHelper.VideoNotificationsListener
            public void onResponse(String str, UserNotificationSettingInfo userNotificationSettingInfo) {
                if (str != null || userNotificationSettingInfo == null) {
                    return;
                }
                TreadlyConnectFragment.this.userNotifications = userNotificationSettingInfo;
                RunningSessionManager.getInstance().userNotifications = userNotificationSettingInfo;
            }
        });
    }

    @Override // androidx.fragment.app.Fragment
    public void onResume() {
        super.onResume();
        System.out.println("Resuming...");
        checkDailygoals();
        startDailyGoalsTimer();
        showBottomNavigation();
        updateFriendsViews();
        DeviceStatusVerification.shared.reset();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void startDailyGoalsTimer() {
        if (this.goalCheckTimer == null) {
            this.goalCheckTimer = new Timer();
            this.goalCheckTimer.schedule(new TimerTask() { // from class: com.treadly.Treadly.UI.TreadlyConnect.TreadlyConnectFragment.19
                @Override // java.util.TimerTask, java.lang.Runnable
                public void run() {
                    TreadlyConnectFragment.this.checkDailygoals();
                }
            }, 30000L, 30000L);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void stopDailyGoalsTimer() {
        if (this.goalCheckTimer != null) {
            this.goalCheckTimer.cancel();
            this.goalCheckTimer = null;
        }
    }

    void dailyGoalsChanged() {
        AppActivityManager.shared.sendDailyGoalUpdatedEvent(new AppActivityManager.AppActivityManagerResponse() { // from class: com.treadly.Treadly.UI.TreadlyConnect.TreadlyConnectFragment.20
            @Override // com.treadly.Treadly.Data.Managers.AppActivityManager.AppActivityManagerResponse
            public void onResponse(String str) {
                TreadlyConnectFragment.this.stopDailyGoalsTimer();
                TreadlyConnectFragment.this.checkDailygoals();
                TreadlyConnectFragment.this.startDailyGoalsTimer();
            }
        });
    }

    private void initDashboardView(View view) {
        this.remoteStatus = RemoteStatus.unknown;
        setStartState(TreadlyConnectStartState.notConnected);
        setIsConnected(false);
        this.caloriesTextView = (TextView) view.findViewById(R.id.calories_view);
        this.dailyGoalTextView = (TextView) view.findViewById(R.id.daily_goal_view);
        this.autoTextView = (TextView) view.findViewById(R.id.auto_text_view);
        this.dailyGoalProgressView = (DailyGoalProgressView) view.findViewById(R.id.daily_goal_progress_view);
        this.speedTextView = (TextView) view.findViewById(R.id.speed_view);
        this.caloriesTextView = (TextView) view.findViewById(R.id.calories_view);
        this.caloriesTextView.setOnClickListener(new View.OnClickListener() { // from class: com.treadly.Treadly.UI.TreadlyConnect.TreadlyConnectFragment.21
            @Override // android.view.View.OnClickListener
            public void onClick(View view2) {
                if (TreadlyConnectFragment.this.hasActivatedDevice() && TreadlyConnectFragment.this.getActivity() != null) {
                    TreadlyConnectFragment.this.getActivity().getSupportFragmentManager().beginTransaction().addToBackStack(TreadlyConnectFragment.TAG).add(R.id.activity_fragment_container_empty, new TreadlyActivityCalendarFragment(), TreadlyActivityCalendarFragment.TAG).commit();
                }
            }
        });
        this.dailyGoalTextView = (TextView) view.findViewById(R.id.daily_goal_view);
        this.speedTextView.setText("0.0");
        this.caloriesTextView.setText("");
        this.dailyGoalProgressView.setProgress(Utils.DOUBLE_EPSILON);
        TextView textView = this.dailyGoalTextView;
        textView.setText(Html.fromHtml(getString(R.string.empty_daily_goal_text) + getString(R.string.daily_goal_label)));
        this.dailyGoalProgressView.setOnClickListener(new View.OnClickListener() { // from class: com.treadly.Treadly.UI.TreadlyConnect.TreadlyConnectFragment.22
            @Override // android.view.View.OnClickListener
            public void onClick(View view2) {
                if (TreadlyConnectFragment.this.hasActivatedDevice() && TreadlyConnectFragment.this.getActivity() != null) {
                    TreadlyConnectFragment.this.getActivity().getSupportFragmentManager().beginTransaction().addToBackStack(TreadlyConnectFragment.TAG).add(R.id.activity_fragment_container_empty, new TreadlyActivityCalendarFragment(), TreadlyActivityCalendarFragment.TAG).commit();
                }
            }
        });
        this.distanceFormatter.setRoundingMode(RoundingMode.DOWN);
        this.userInteractionView = (TreadlyConnectUserInteractionView) view.findViewById(R.id.user_interaction_view);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void checkUnclaimed() {
        if (this.unclaimedListDisplayedDevices.containsKey(this.deviceName) || this.expectingUnclaimedActivityCheck || this.otaRequired) {
            return;
        }
        this.expectingUnclaimedActivityCheck = TreadlyClientLib.shared.getUnclaimedUserStatsLogInfo();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* renamed from: com.treadly.Treadly.UI.TreadlyConnect.TreadlyConnectFragment$23  reason: invalid class name */
    /* loaded from: classes2.dex */
    public class AnonymousClass23 extends TreadlyServiceResponseEventAdapter {
        final /* synthetic */ String val$device_address;

        AnonymousClass23(String str) {
            this.val$device_address = str;
        }

        @Override // com.treadly.Treadly.Data.Listeners.TreadlyServiceResponseEventAdapter, com.treadly.Treadly.Data.Listeners.TreadlyServiceResponseEventListener
        public void onGetUnclaimedLogs(String str, final List<UserRunningSessionInfo> list) {
            super.onGetUnclaimedLogs(str, list);
            if (TreadlyConnectFragment.this.expectingUnclaimedActivityCheck) {
                TreadlyConnectFragment.this.expectingUnclaimedActivityCheck = false;
                if (TreadlyConnectFragment.this.deviceName == null || TreadlyConnectFragment.this.unclaimedListDisplayedDevices.containsKey(TreadlyConnectFragment.this.deviceName) || list == null || list.size() == 0 || !TreadlyConnectFragment.this.isPageVisible || TreadlyConnectFragment.this.otaRequired) {
                    return;
                }
                FragmentActivity activity = TreadlyConnectFragment.this.getActivity();
                final String str2 = this.val$device_address;
                ActivityUtil.runOnUiThread(activity, new Runnable() { // from class: com.treadly.Treadly.UI.TreadlyConnect.-$$Lambda$TreadlyConnectFragment$23$YuQknkdeqmT8-RWLi2JT-yAJPSU
                    @Override // java.lang.Runnable
                    public final void run() {
                        TreadlyConnectFragment.AnonymousClass23.lambda$onGetUnclaimedLogs$0(TreadlyConnectFragment.AnonymousClass23.this, list, str2);
                    }
                });
                TreadlyConnectFragment.this.unclaimedListDisplayedDevices.put(TreadlyConnectFragment.this.deviceName, TreadlyConnectFragment.this.deviceName);
            }
        }

        public static /* synthetic */ void lambda$onGetUnclaimedLogs$0(AnonymousClass23 anonymousClass23, List list, String str) {
            TreadlyActivityUnclaimedFragment treadlyActivityUnclaimedFragment = new TreadlyActivityUnclaimedFragment();
            treadlyActivityUnclaimedFragment.unclaimedSessionInfos = list;
            treadlyActivityUnclaimedFragment.deviceAddress = str;
            treadlyActivityUnclaimedFragment.isSessionInfo = true;
            treadlyActivityUnclaimedFragment.showNavOnDetach = true;
            TreadlyConnectFragment.this.addFragmentToStack(treadlyActivityUnclaimedFragment, TreadlyActivityUnclaimedFragment.TAG, TreadlyConnectFragment.TAG, true);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void checkUnclaimed_v2(String str) {
        if (this.unclaimedListDisplayedDevices.containsKey(this.deviceName) || this.expectingUnclaimedActivityCheck || this.otaRequired) {
            return;
        }
        this.expectingUnclaimedActivityCheck = TreadlyServiceManager.getInstance().getUnclaimedUserStatsLogInfo(str, new AnonymousClass23(str));
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void updateDashboardView() {
        DeviceStatus deviceStatus = this.currentDeviceStatusInfo;
        if (deviceStatus == null) {
            return;
        }
        updateStartState();
        if (deviceStatus.getMaintenanceInfo().isMaintenaceRequired() && this.deviceName != null && this.maintenanceWarningDisplayedDevices.get(this.deviceName) == null && checkSessionOwner(deviceStatus) && supportsMaintenance()) {
            this.maintenanceWarningDisplayedDevices.put(this.deviceName, this.deviceName);
            this.maintenanceWarningDisplayed = true;
            showAlert("Maintenance Warning", "The Treadly product requires maintenance", new DialogInterface.OnDismissListener() { // from class: com.treadly.Treadly.UI.TreadlyConnect.TreadlyConnectFragment.24
                @Override // android.content.DialogInterface.OnDismissListener
                public void onDismiss(DialogInterface dialogInterface) {
                    if (TreadlyConnectFragment.this.requiresMaintenancePause()) {
                        TreadlyConnectFragment.this.pendingMaintenanceReset = true;
                        TreadlyConnectFragment.this.pauseDevice();
                    }
                    TreadlyClientLib.shared.resetMaintenance();
                }
            });
        }
        if (deviceStatus.getMaintenanceInfo() != null && !this.errorShown) {
            showDeviceStatusCodeAlert(deviceStatus.getStatusCode());
        }
        if (this.remoteStatusCount > 0) {
            this.remoteStatusCount--;
        } else {
            this.remoteStatus = deviceStatus.getRemoteStatus();
            if (this.remoteStatus == RemoteStatus.manual) {
                this.remoteSwitch.setText("M");
            } else if (this.remoteStatus == RemoteStatus.automatic) {
                this.remoteSwitch.setText(ExifInterface.GPS_MEASUREMENT_IN_PROGRESS);
            }
        }
        if (!this.currentDeviceStatusInfo.isBleEnabled() && !this.presentedActivationDialog && this.isPageVisible) {
            this.presentedActivationDialog = true;
            TreadlyClientLib.shared.startActivation();
            this.setSendEnableBleOffSource = true;
        }
        if (this.setSendEnableBleOffSource) {
            sendBleOffSourceLog();
        }
        updateActivationAlert();
        handleDeviceStatusStates(deviceStatus);
        if (this.pendingMaintenanceReset) {
            if (deviceStatus.getMaintenanceInfo().isMaintenaceRequired()) {
                TreadlyClientLib.shared.resetMaintenance();
            } else {
                this.pendingMaintenanceReset = false;
            }
        }
    }

    void handleDeviceStatusStates(DeviceStatus deviceStatus) {
        if (deviceStatus.getPauseState()) {
            if (this.sliderFragment != null) {
                this.sliderFragment.finishSliderAnimation();
            }
            displayPauseStateView();
        } else {
            this.displayedPauseStateView = false;
            dismissPauseStateAlert();
        }
        if (deviceStatus.getEmergencyStopState()) {
            displayEmergencyModeView();
            return;
        }
        this.displayedEmergencyModeView = false;
        dismissEmergencyStateAlert();
    }

    void displayPauseStateView() {
        ActivityUtil.runOnUiThread(getActivity(), new Runnable() { // from class: com.treadly.Treadly.UI.TreadlyConnect.-$$Lambda$TreadlyConnectFragment$_Bne853G_SRS3aCSOWnce6L2mL8
            @Override // java.lang.Runnable
            public final void run() {
                TreadlyConnectFragment.lambda$displayPauseStateView$12(TreadlyConnectFragment.this);
            }
        });
    }

    public static /* synthetic */ void lambda$displayPauseStateView$12(TreadlyConnectFragment treadlyConnectFragment) {
        if (treadlyConnectFragment.pauseStateAlert == null && !treadlyConnectFragment.displayedPauseStateView) {
            treadlyConnectFragment.displayedPauseStateView = true;
            treadlyConnectFragment.pauseStateAlert = new TreadlyConnectPauseView(treadlyConnectFragment.getContext());
            treadlyConnectFragment.pauseStateAlert.activity = treadlyConnectFragment.getActivity();
            treadlyConnectFragment.pauseStateAlert.fragment = treadlyConnectFragment;
            treadlyConnectFragment.pauseStateAlert.setLayoutParams(new LinearLayout.LayoutParams(-1, -1));
            treadlyConnectFragment.pauseStateAlertContainer.addView(treadlyConnectFragment.pauseStateAlert);
            treadlyConnectFragment.pauseStateAlertContainer.setVisibility(0);
            treadlyConnectFragment.pauseStateAlert.setStats(String.format("%d", Integer.valueOf(treadlyConnectFragment.calories)), treadlyConnectFragment.stepsTextView.getText().toString(), treadlyConnectFragment.speedFormatter.format(RunningSessionManager.getInstance().averageSpeed), treadlyConnectFragment.distanceTextView.getText().toString(), treadlyConnectFragment.dailyGoalPercentage);
            treadlyConnectFragment.pauseStateAlertContainer.setAlpha(0.0f);
            treadlyConnectFragment.pauseStateAlertContainer.animate().alpha(1.0f).setDuration(800L).setListener(null).start();
            treadlyConnectFragment.pauseStateAlert.listener = new TreadlyConnectPauseView.TreadlyConnectPauseViewListener() { // from class: com.treadly.Treadly.UI.TreadlyConnect.TreadlyConnectFragment.25
                @Override // com.treadly.Treadly.UI.TreadlyConnect.AlertView.TreadlyConnectPauseView.TreadlyConnectPauseViewListener
                public void onDidPressStop() {
                    if (TreadlyConnectFragment.this.powerOffDevice()) {
                        TreadlyConnectFragment.this.setPendingPause(true);
                    }
                    TreadlyConnectFragment.this.dismissPauseStateAlert();
                }

                @Override // com.treadly.Treadly.UI.TreadlyConnect.AlertView.TreadlyConnectPauseView.TreadlyConnectPauseViewListener
                public void onDidPressResume() {
                    if (TreadlyConnectFragment.this.sliderFragment != null && TreadlyConnectFragment.this.sliderFragment.stopButton != null) {
                        TreadlyConnectFragment.this.onStopButtonTouchDown(TreadlyConnectFragment.this.sliderFragment.stopButton);
                    }
                    TreadlyConnectFragment.this.dismissPauseStateAlert();
                }
            };
        }
    }

    void dismissPauseStateAlert() {
        if (this.pauseStateAlert != null) {
            this.pauseStateAlertContainer.removeView(this.pauseStateAlert);
        }
        if (this.pauseStateAlertContainer != null) {
            this.pauseStateAlertContainer.animate().alpha(0.1f).setDuration(400L).setListener(new AnimatorListenerAdapter() { // from class: com.treadly.Treadly.UI.TreadlyConnect.TreadlyConnectFragment.26
                @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
                public void onAnimationEnd(Animator animator) {
                    TreadlyConnectFragment.this.pauseStateAlertContainer.setVisibility(8);
                }
            }).start();
        }
        this.pauseStateAlert = null;
    }

    void displayEmergencyModeView() {
        if (this.emergencyModeAlert == null && !this.displayedEmergencyModeView) {
            this.displayedEmergencyModeView = true;
            this.emergencyModeAlert = new TreadlyConnectAlertView(getContext());
            this.emergencyModeAlert.isEmergency = true;
            ActivityUtil.runOnUiThread(getActivity(), new Runnable() { // from class: com.treadly.Treadly.UI.TreadlyConnect.TreadlyConnectFragment.27
                @Override // java.lang.Runnable
                public void run() {
                    TreadlyConnectFragment.this.emergencyModeAlert.show();
                }
            });
        }
    }

    void dismissEmergencyStateAlert() {
        if (this.emergencyModeAlert != null) {
            this.emergencyModeAlert.dismiss();
        }
        this.emergencyModeAlert = null;
    }

    private void resetDashboardView() {
        this.remoteStatus = RemoteStatus.unknown;
        setStartState(TreadlyConnectStartState.notConnected);
        if (this.deviceStatusAlert != null) {
            ActivityUtil.runOnUiThread(getActivity(), new Runnable() { // from class: com.treadly.Treadly.UI.TreadlyConnect.TreadlyConnectFragment.28
                @Override // java.lang.Runnable
                public void run() {
                    TreadlyConnectFragment.this.deviceStatusAlert.dismiss();
                    TreadlyConnectFragment.this.deviceStatusAlert = null;
                }
            });
        }
        updateActivationAlert();
        resetCalories();
        dismissPauseStateAlert();
    }

    private void onBluetoothConnectionStateChanged(DeviceStatus deviceStatus) {
        if (deviceStatus.isPoweredOn()) {
            autoConnect();
            this.autoConnected = true;
        } else if (!deviceStatus.isPoweredOn()) {
            this.autoConnected = true;
        } else {
            PrintStream printStream = System.out;
            printStream.println("Ignoring state: " + deviceStatus);
        }
    }

    private void willEnterForeground() {
        String connectedDeviceName;
        if (this.isConnected || (connectedDeviceName = SharedPreferences.shared.getConnectedDeviceName()) == null || connectedDeviceName.isEmpty()) {
            return;
        }
        this.autoConnected = false;
        autoConnect();
    }

    void initAlerts() {
        this.builder = new AlertDialog.Builder(getContext());
        if (this.submitActivationAlert == null) {
            this.submitActivationAlert = this.builder.create();
        }
        if (this.startActivationAlert == null) {
            this.startActivationAlert = this.builder.create();
        }
        if (this.errorActivationAlert == null) {
            this.errorActivationAlert = this.builder.create();
        }
        if (this.finishActivationAlert == null) {
            this.finishActivationAlert = this.builder.create();
        }
        if (this.completeActivationAlert == null) {
            this.completeActivationAlert = this.builder.create();
        }
        if (this.singleUserModeAlert == null) {
            this.singleUserModeAlert = this.builder.create();
        }
        this.deviceStatusAlert = null;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void updateActivationAlert() {
        String str;
        if (this.isActivationInProgress) {
            DeviceStatus deviceStatus = this.currentDeviceStatusInfo;
            boolean isDeviceConnected = TreadlyClientLib.shared.isDeviceConnected();
            if (this.submitActivationAlert != null) {
                updateActivationAction();
                String str2 = this.activationInfo.activationSubmitMessage;
                if (!isDeviceConnected) {
                    this.autoConnected = false;
                    autoConnect();
                    if (getBleComponent() == null) {
                        str = str2 + "\n\nStatus: Disconnected\nPlease reconnect";
                    } else if (isGen2()) {
                        str = str2 + "\n\nStatus: Disconnected\nPlease reconnect";
                    } else {
                        str = str2 + "\n\nStatus: Disconnected\nPlease tap 3 times in the constant section to start the pairing process";
                    }
                } else {
                    str = str2 + "\n\nStatus: Connected";
                }
                this.submitActivationAlert.setMessage(str);
            }
            if (this.pendingActivationBleEnable && !isDeviceConnected && this.errorActivationAlert != null) {
                this.errorActivationAlert.setOnShowListener(new DialogInterface.OnShowListener() { // from class: com.treadly.Treadly.UI.TreadlyConnect.TreadlyConnectFragment.29
                    @Override // android.content.DialogInterface.OnShowListener
                    public void onShow(DialogInterface dialogInterface) {
                        TreadlyConnectFragment.this.pendingActivationBleEnable = false;
                        TreadlyConnectFragment.this.isActivationInProgress = false;
                    }
                });
                ActivityUtil.runOnUiThread(getActivity(), new Runnable() { // from class: com.treadly.Treadly.UI.TreadlyConnect.TreadlyConnectFragment.30
                    @Override // java.lang.Runnable
                    public void run() {
                        TreadlyConnectFragment.this.errorActivationAlert.show();
                    }
                });
            }
            if (this.pendingActivationBleEnable) {
                if (isGen2()) {
                    if (this.authenticationState == AuthenticationState.active) {
                        if (this.completeActivationAlert != null && !this.completeActivationAlert.isShowing()) {
                            this.completeActivationAlert.setOnShowListener(new DialogInterface.OnShowListener() { // from class: com.treadly.Treadly.UI.TreadlyConnect.TreadlyConnectFragment.31
                                @Override // android.content.DialogInterface.OnShowListener
                                public void onShow(DialogInterface dialogInterface) {
                                    TreadlyConnectFragment.this.pendingActivationBleEnable = false;
                                }
                            });
                            ActivityUtil.runOnUiThread(getActivity(), new Runnable() { // from class: com.treadly.Treadly.UI.TreadlyConnect.TreadlyConnectFragment.32
                                @Override // java.lang.Runnable
                                public void run() {
                                    TreadlyConnectFragment.this.completeActivationAlert.show();
                                }
                            });
                        }
                        this.isActivationInProgress = false;
                    }
                } else if (deviceStatus == null || !deviceStatus.isBleEnabled()) {
                } else {
                    if (this.completeActivationAlert != null && !this.completeActivationAlert.isShowing()) {
                        this.completeActivationAlert.setOnShowListener(new DialogInterface.OnShowListener() { // from class: com.treadly.Treadly.UI.TreadlyConnect.TreadlyConnectFragment.33
                            @Override // android.content.DialogInterface.OnShowListener
                            public void onShow(DialogInterface dialogInterface) {
                                TreadlyConnectFragment.this.pendingActivationBleEnable = false;
                            }
                        });
                        ActivityUtil.runOnUiThread(getActivity(), new Runnable() { // from class: com.treadly.Treadly.UI.TreadlyConnect.TreadlyConnectFragment.34
                            @Override // java.lang.Runnable
                            public void run() {
                                TreadlyConnectFragment.this.completeActivationAlert.show();
                            }
                        });
                    }
                    this.isActivationInProgress = false;
                }
            }
        }
    }

    void updateActivationAction() {
        if (this.submitActivationAlert.getButton(-1) != null) {
            this.submitActivationAlert.getButton(-1).setEnabled(TreadlyClientLib.shared.isDeviceConnected() && this.activationCodeTextFieldValid && getBleComponent() != null);
        }
    }

    private void initCalendar(View view) {
        String format = new SimpleDateFormat("d", Locale.getDefault()).format(new Date());
        this.calendarActivity = (ImageButton) view.findViewById(R.id.activity_calendar_symbol);
        this.dayOfMonth = (TextView) view.findViewById(R.id.calendar_day_inside_symbol);
        this.dayOfMonth.setText(format);
        this.calendarActivity.setOnClickListener(new View.OnClickListener() { // from class: com.treadly.Treadly.UI.TreadlyConnect.-$$Lambda$TreadlyConnectFragment$smn7wmimLMKzMVFQwR6H6sIAiYU
            @Override // android.view.View.OnClickListener
            public final void onClick(View view2) {
                TreadlyConnectFragment.lambda$initCalendar$14(TreadlyConnectFragment.this, view2);
            }
        });
    }

    public static /* synthetic */ void lambda$initCalendar$14(final TreadlyConnectFragment treadlyConnectFragment, View view) {
        if (treadlyConnectFragment.hasActivatedDevice()) {
            treadlyConnectFragment.calendarFragment = new TreadlyActivityCalendarFragment();
            treadlyConnectFragment.calendarFragment.listener = new TreadlyActivityCalendarFragment.ActivityCalendarListener() { // from class: com.treadly.Treadly.UI.TreadlyConnect.-$$Lambda$TreadlyConnectFragment$yrY3kZtXCdX4NtOLzRTa7m5kfZg
                @Override // com.treadly.Treadly.UI.TreadlyActivity.ActivityCalendar.TreadlyActivityCalendarFragment.ActivityCalendarListener
                public final void onDetach() {
                    TreadlyConnectFragment.this.calendarFragment = null;
                }
            };
            treadlyConnectFragment.addFragmentToStack(treadlyConnectFragment.calendarFragment, TreadlyActivityCalendarFragment.TAG, TAG, true);
        }
    }

    void updateCalendar() {
        String format = new SimpleDateFormat("d", Locale.getDefault()).format(new Date());
        if (this.dayOfMonth != null) {
            this.dayOfMonth.setText(format);
        }
    }

    void hideCalendar() {
        if (this.calendarFragment != null) {
            this.calendarFragment.onHiddenChanged(true);
        }
    }

    void updateLogs() {
        DeviceStatus deviceStatus = this.currentDeviceStatusInfo;
        if (deviceStatus.isPoweredOn()) {
            if (TreadlyLogManager.shared.isLogging) {
                TreadlyLogManager.shared.appendToLog(deviceStatus);
            } else if (this.deviceName != null && this.serialNumber != null && this.macAddress != null && this.customBoardVersion != null && this.mainBoardVersion != null) {
                TreadlyLogManager.shared.startLogging(this.deviceName, this.serialNumber, this.macAddress, getAppVersion(), this.customBoardVersion.getVersion(), this.mainBoardVersion.getVersion());
            } else {
                TreadlyClientLib.shared.getDeviceComponentList();
            }
        } else if (TreadlyLogManager.shared.isLogging) {
            TreadlyLogManager.shared.finishLogging();
        }
    }

    void updateDeviceUserStatsLogs() {
        DeviceStatus deviceStatus = this.currentDeviceStatusInfo;
        if (deviceStatus.isPoweredOn()) {
            if (DeviceUserStatsLogManager.getInstance().isLogging) {
                DeviceUserStatsLogManager.getInstance().appendLogs(deviceStatus);
            } else if (DeviceStatusVerification.shared.customBoardVersion != null) {
                if (this.pendingTreadmillStart) {
                    this.pendingTreadmillStart = !DeviceUserStatsLogManager.getInstance().startLogging(DeviceStatusVerification.shared.customBoardVersion, deviceStatus);
                }
            } else {
                TreadlyClientLib.shared.getDeviceComponentList();
            }
        } else if (DeviceUserStatsLogManager.getInstance().isLogging) {
            DeviceUserStatsLogManager.getInstance().finishLogging();
        }
    }

    @SuppressLint({"DefaultLocale"})
    void updateRunSession() {
        DeviceStatus deviceStatus = this.currentDeviceStatusInfo;
        if (deviceStatus == null) {
            return;
        }
        if (deviceStatus.isPoweredOn()) {
            if (RunningSessionManager.getInstance().inProgress) {
                RunningSessionManager.getInstance().updateRunSession(deviceStatus.getSteps(), deviceStatus.getDistance(), deviceStatus.getSpeedInfo().getTargetSpeed(), deviceStatus.getSeconds(), deviceStatus.getDistanceUnits(), deviceStatus.isSessionOwnership());
                setCalories(RunningSessionManager.getInstance().calories, RunningSessionManager.getInstance().dailySteps, RunningSessionManager.getInstance().dailyDistance, RunningSessionManager.getInstance().dailyDuration);
            } else if (this.userProfile != null) {
                if (this.userNotifications == null) {
                    return;
                }
                RunningSessionManager.getInstance().startRunSession(this.userProfile, this.userNotifications, deviceStatus.getSeconds(), deviceStatus.getDistance(), deviceStatus.getSteps());
                setCalories(RunningSessionManager.getInstance().calories, RunningSessionManager.getInstance().dailySteps, RunningSessionManager.getInstance().dailyDistance, RunningSessionManager.getInstance().dailyDuration);
            }
        } else if (RunningSessionManager.getInstance().inProgress) {
            RunningSessionManager.getInstance().stopRunSession();
            setCalories(RunningSessionManager.getInstance().calories, RunningSessionManager.getInstance().dailySteps, RunningSessionManager.getInstance().dailyDistance, RunningSessionManager.getInstance().dailyDuration);
        } else {
            RunningSessionManager.getInstance().updateRunSession(deviceStatus.getSteps(), deviceStatus.getDistance(), deviceStatus.getSpeedInfo().getTargetSpeed(), deviceStatus.getSeconds(), deviceStatus.getDistanceUnits(), deviceStatus.isSessionOwnership());
            setCalories(RunningSessionManager.getInstance().calories, RunningSessionManager.getInstance().dailySteps, RunningSessionManager.getInstance().dailyDistance, RunningSessionManager.getInstance().dailyDuration);
        }
        if (this.pauseStateAlert != null) {
            this.pauseStateAlert.updateCalories(String.format("%d", Integer.valueOf(this.calories)), this.dailyGoalPercentage, this.speedFormatter.format(RunningSessionManager.getInstance().averageSpeed));
        }
    }

    void sendBleOffSourceLog() {
        DeviceStatus deviceStatus = this.currentDeviceStatusInfo;
        if (deviceStatus == null || this.deviceName == null || this.serialNumber == null || this.macAddress == null || this.customBoardVersion == null || this.mainBoardVersion == null || !TreadlyLogManager.shared.isLogging) {
            return;
        }
        this.setSendEnableBleOffSource = false;
        TreadlyLogManager.shared.startLogging(this.deviceName, this.serialNumber, this.macAddress, getAppVersion(), this.customBoardVersion.getVersion(), this.mainBoardVersion.getVersion());
        TreadlyLogManager.shared.appendToLog(deviceStatus);
        TreadlyLogManager.shared.finishLogging();
    }

    protected void impactOccured(View view) {
        if (!this.currentDeviceStatusInfo.isBleEnabled() || this.startState == TreadlyConnectStartState.notConnected || view == null) {
            return;
        }
        view.performHapticFeedback(3, 2);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void fetchUserProfile() {
        String userId = TreadlyServiceManager.getInstance().getUserId();
        if (userId != null) {
            TreadlyServiceManager.getInstance().getUserProfileInfo(userId, new TreadlyServiceResponseEventAdapter() { // from class: com.treadly.Treadly.UI.TreadlyConnect.TreadlyConnectFragment.35
                @Override // com.treadly.Treadly.Data.Listeners.TreadlyServiceResponseEventAdapter, com.treadly.Treadly.Data.Listeners.TreadlyServiceResponseEventListener
                public void onUserProfile(String str, UserProfileInfo userProfileInfo) {
                    if (str == null) {
                        TreadlyConnectFragment.this.userProfile = userProfileInfo;
                        RunningSessionManager.getInstance().userProfile = userProfileInfo;
                        TreadlyConnectFragment.this.setCalories(TreadlyConnectFragment.this.calories, TreadlyConnectFragment.this.dailySteps, TreadlyConnectFragment.this.dailyDistance, TreadlyConnectFragment.this.dailyDuration);
                    }
                }
            });
        }
    }

    private void setFriendsOnline(int i) {
        this.friendsOnline = i;
        this.friendsFraction.setText(String.format("%d/%d", Integer.valueOf(this.friendsOnline), Integer.valueOf(this.friendsTotal)));
    }

    private void setTotalFriends(int i) {
        this.friendsTotal = i;
        this.friendsFraction.setText(String.format("%d/%d", Integer.valueOf(this.friendsOnline), Integer.valueOf(this.friendsTotal)));
    }

    private void videoButtonPressed(View view) {
        if (hasActivatedDevice()) {
            PopupMenu videoPopMenu = getVideoPopMenu(view);
            this.videoPopupMenu = videoPopMenu;
            videoPopMenu.inflate(R.menu.connect_video_popup);
            videoPopMenu.show();
        }
    }

    private PopupMenu getVideoPopMenu(View view) {
        PopupMenu popupMenu = new PopupMenu(getContext(), view);
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() { // from class: com.treadly.Treadly.UI.TreadlyConnect.TreadlyConnectFragment.38
            @Override // androidx.appcompat.widget.PopupMenu.OnMenuItemClickListener
            public boolean onMenuItemClick(MenuItem menuItem) {
                if (menuItem.getItemId() != R.id.private_video) {
                    return false;
                }
                ActivityUtil.runOnUiThread(TreadlyConnectFragment.this.getActivity(), new Runnable() { // from class: com.treadly.Treadly.UI.TreadlyConnect.TreadlyConnectFragment.38.1
                    @Override // java.lang.Runnable
                    public void run() {
                    }
                });
                return true;
            }
        });
        return popupMenu;
    }

    private PopupMenu getInviteMenu(View view, final UserInfo userInfo) {
        if (getContext() == null) {
            return null;
        }
        PopupMenu popupMenu = new PopupMenu(getContext(), view);
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() { // from class: com.treadly.Treadly.UI.TreadlyConnect.TreadlyConnectFragment.39
            @Override // androidx.appcompat.widget.PopupMenu.OnMenuItemClickListener
            public boolean onMenuItemClick(MenuItem menuItem) {
                switch (menuItem.getItemId()) {
                    case R.id.friend_popup_invite /* 2131362244 */:
                        ActivityUtil.runOnUiThread(TreadlyConnectFragment.this.getActivity(), new Runnable() { // from class: com.treadly.Treadly.UI.TreadlyConnect.TreadlyConnectFragment.39.2
                            @Override // java.lang.Runnable
                            public void run() {
                            }
                        });
                        return true;
                    case R.id.friend_popup_join /* 2131362245 */:
                        return true;
                    case R.id.friend_popup_join_live /* 2131362246 */:
                        return true;
                    case R.id.friend_popup_user_profile /* 2131362247 */:
                        ActivityUtil.runOnUiThread(TreadlyConnectFragment.this.getActivity(), new Runnable() { // from class: com.treadly.Treadly.UI.TreadlyConnect.TreadlyConnectFragment.39.1
                            @Override // java.lang.Runnable
                            public void run() {
                                TreadlyProfileUserView treadlyProfileUserView = new TreadlyProfileUserView();
                                treadlyProfileUserView.userId = userInfo.id;
                                treadlyProfileUserView.userName = userInfo.name;
                                treadlyProfileUserView.userAvatar = userInfo.avatarPath;
                                treadlyProfileUserView.isCurrentUser = false;
                                if (TreadlyConnectFragment.this.getActivity() == null || TreadlyConnectFragment.this.getActivity().getSupportFragmentManager() == null) {
                                    return;
                                }
                                TreadlyConnectFragment.this.getActivity().getSupportFragmentManager().beginTransaction().addToBackStack(TreadlyConnectFragment.TAG).add(R.id.activity_fragment_container_empty, treadlyProfileUserView, TreadlyProfileUserView.TAG).commit();
                            }
                        });
                        return true;
                    default:
                        return false;
                }
            }
        });
        return popupMenu;
    }

    private InviteServiceInviteInfo getPendingInvite(String str, List<InviteServiceInviteInfo> list) {
        if (list == null) {
            return null;
        }
        for (InviteServiceInviteInfo inviteServiceInviteInfo : list) {
            if (inviteServiceInviteInfo.userIdFrom.equals(str)) {
                return inviteServiceInviteInfo;
            }
        }
        return null;
    }

    private void addFriendsButtonPressed() {
        if (hasActivatedDevice()) {
            TreadlyInviteFriendsFragment treadlyInviteFriendsFragment = new TreadlyInviteFriendsFragment();
            if (getActivity() != null) {
                getActivity().getSupportFragmentManager().beginTransaction().addToBackStack(TAG).add(R.id.activity_fragment_container_empty, treadlyInviteFriendsFragment).commit();
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* renamed from: com.treadly.Treadly.UI.TreadlyConnect.TreadlyConnectFragment$40  reason: invalid class name */
    /* loaded from: classes2.dex */
    public class AnonymousClass40 extends DeviceUserStatsLogEventAdapter {
        AnonymousClass40() {
        }

        @Override // com.treadly.client.lib.sdk.Listeners.DeviceUserStatsLogEventAdapter, com.treadly.client.lib.sdk.Listeners.DeviceUserStatsLogEventListener
        public void onDeviceUserStatsUnclaimedUserStatsInfo(final DeviceUserStatsUnclaimedLogInfo[] deviceUserStatsUnclaimedLogInfoArr) {
            if (TreadlyConnectFragment.this.expectingUnclaimedActivityCheck) {
                TreadlyConnectFragment.this.expectingUnclaimedActivityCheck = false;
                if (deviceUserStatsUnclaimedLogInfoArr == null || deviceUserStatsUnclaimedLogInfoArr.length == 0 || !TreadlyConnectFragment.this.isPageVisible || TreadlyConnectFragment.this.deviceName == null || TreadlyConnectFragment.this.unclaimedListDisplayedDevices.containsKey(TreadlyConnectFragment.this.deviceName) || TreadlyActivityUnclaimedFragment.isDisplaying) {
                    return;
                }
                ActivityUtil.runOnUiThread(TreadlyConnectFragment.this.getActivity(), new Runnable() { // from class: com.treadly.Treadly.UI.TreadlyConnect.-$$Lambda$TreadlyConnectFragment$40$f269oKmkKDNQxKZBZQxfFUGwZMI
                    @Override // java.lang.Runnable
                    public final void run() {
                        TreadlyConnectFragment.AnonymousClass40.lambda$onDeviceUserStatsUnclaimedUserStatsInfo$0(TreadlyConnectFragment.AnonymousClass40.this, deviceUserStatsUnclaimedLogInfoArr);
                    }
                });
            }
        }

        public static /* synthetic */ void lambda$onDeviceUserStatsUnclaimedUserStatsInfo$0(AnonymousClass40 anonymousClass40, DeviceUserStatsUnclaimedLogInfo[] deviceUserStatsUnclaimedLogInfoArr) {
            TreadlyActivityUnclaimedFragment treadlyActivityUnclaimedFragment = new TreadlyActivityUnclaimedFragment();
            treadlyActivityUnclaimedFragment.unclaimedActivities = Arrays.asList(deviceUserStatsUnclaimedLogInfoArr);
            treadlyActivityUnclaimedFragment.showNavOnDetach = true;
            TreadlyConnectFragment.this.addFragmentToStack(treadlyActivityUnclaimedFragment, TreadlyActivityUnclaimedFragment.TAG, TreadlyConnectFragment.TAG, true);
        }
    }

    private void initCountdown(View view) {
        if (getContext() == null) {
            return;
        }
        this.loadingView = (LottieAnimationView) view.findViewById(R.id.countdown_view);
        this.loadingView.setVisibility(8);
        this.loadingView.setClickable(true);
        this.loadingView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        this.loadingView.setSpeed(1.0f);
        this.loadingView.setRepeatCount(0);
        this.loadingView.addAnimatorUpdateListener(new ValueAnimator.AnimatorUpdateListener() { // from class: com.treadly.Treadly.UI.TreadlyConnect.TreadlyConnectFragment.41
            @Override // android.animation.ValueAnimator.AnimatorUpdateListener
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                if (valueAnimator.getAnimatedFraction() > 0.9d) {
                    float max = Math.max(1.0f - valueAnimator.getAnimatedFraction(), 0.0f) / 0.1f;
                    TreadlyConnectFragment.this.loadingViewBackground.setAlpha(max);
                    TreadlyConnectFragment.this.loadingView.setAlpha(max);
                    if (TreadlyConnectFragment.this.loadingViewSetForceStart && TreadlyConnectFragment.this.startState != TreadlyConnectStartState.notConnected) {
                        TreadlyConnectFragment.this.loadingViewSetForceStart = false;
                        TreadlyConnectFragment.this.lastStartState = TreadlyConnectFragment.this.startState;
                        TreadlyConnectFragment.this.setStartState(TreadlyConnectStartState.forcedStart);
                    }
                }
                if (valueAnimator.getAnimatedFraction() >= 1.0f) {
                    TreadlyConnectFragment.this.hideCountdown();
                }
            }
        });
        this.loadingViewBackground = (ImageView) view.findViewById(R.id.countdown_background);
        this.loadingViewBackground.setVisibility(8);
    }

    private void setIsCountDownRunning(boolean z) {
        this.isCountDownRunning = z;
        updateStopButton();
    }

    private void showCountDown() {
        if (this.loadingView != null) {
            this.loadingViewSetForceStart = true;
            this.loadingView.setVisibility(0);
            this.loadingView.setAlpha(1.0f);
            this.loadingView.playAnimation();
        }
        if (this.loadingViewBackground != null) {
            this.loadingViewBackground.setVisibility(0);
            this.loadingViewBackground.setAlpha(1.0f);
        }
        startLoadingViewTimer();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void hideCountdown() {
        if (this.loadingView != null) {
            this.loadingView.setVisibility(8);
            this.loadingView.cancelAnimation();
        }
        if (this.loadingViewBackground != null) {
            this.loadingViewBackground.setVisibility(8);
        }
        this.loadingViewSetForceStart = false;
        stopLoadingViewTimer();
    }

    private void startLoadingViewTimer() {
        stopLoadingViewTimer();
        this.loadingViewTimerCount = 0;
        this.loadingViewTimer = new Timer();
        this.loadingViewTimer.schedule(new TimerTask() { // from class: com.treadly.Treadly.UI.TreadlyConnect.TreadlyConnectFragment.42
            @Override // java.util.TimerTask, java.lang.Runnable
            public void run() {
                if (TreadlyConnectFragment.access$5808(TreadlyConnectFragment.this) < 3) {
                    TreadlyConnectFragment.this.impactOccured(TreadlyConnectFragment.this.loadingView);
                }
            }
        }, 0L, 1000L);
    }

    private void stopLoadingViewTimer() {
        if (this.loadingViewTimer != null) {
            this.loadingViewTimer.cancel();
        }
        this.loadingViewTimer = null;
    }

    /* JADX WARN: Code restructure failed: missing block: B:11:0x0075, code lost:
        if (r5 == (-1.0d)) goto L16;
     */
    /* JADX WARN: Code restructure failed: missing block: B:13:0x0078, code lost:
        if (r3 == (-1)) goto L16;
     */
    /* JADX WARN: Code restructure failed: missing block: B:15:0x007b, code lost:
        if (r0 == (-1)) goto L16;
     */
    /* JADX WARN: Code restructure failed: missing block: B:16:0x007d, code lost:
        r2 = true;
     */
    /* JADX WARN: Code restructure failed: missing block: B:17:0x0080, code lost:
        r2 = false;
     */
    /* JADX WARN: Code restructure failed: missing block: B:18:0x0081, code lost:
        r21 = r2;
     */
    /* JADX WARN: Code restructure failed: missing block: B:8:0x0070, code lost:
        if (r11 == (-1.0d)) goto L16;
     */
    /* JADX WARN: Removed duplicated region for block: B:23:0x0096  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    void resetCalories() {
        /*
            r22 = this;
            com.treadly.Treadly.Data.Managers.RunningSessionManager r0 = com.treadly.Treadly.Data.Managers.RunningSessionManager.getInstance()
            int r0 = r0.getLastTotalCalories()
            com.treadly.Treadly.Data.Managers.RunningSessionManager r1 = com.treadly.Treadly.Data.Managers.RunningSessionManager.getInstance()
            int r1 = r1.totalDailyCalories
            r2 = 0
            int r1 = java.lang.Math.max(r1, r2)
            com.treadly.Treadly.Data.Managers.RunningSessionManager r3 = com.treadly.Treadly.Data.Managers.RunningSessionManager.getInstance()
            int r3 = r3.getLastTotalSteps()
            com.treadly.Treadly.Data.Managers.RunningSessionManager r4 = com.treadly.Treadly.Data.Managers.RunningSessionManager.getInstance()
            int r4 = r4.totalDailySteps
            int r4 = java.lang.Math.max(r4, r2)
            com.treadly.Treadly.Data.Managers.RunningSessionManager r5 = com.treadly.Treadly.Data.Managers.RunningSessionManager.getInstance()
            double r5 = r5.getLastTotalDistance()
            com.treadly.Treadly.Data.Managers.RunningSessionManager r7 = com.treadly.Treadly.Data.Managers.RunningSessionManager.getInstance()
            double r7 = r7.totalDailyDistance
            r9 = 0
            double r7 = java.lang.Math.max(r7, r9)
            com.treadly.Treadly.Data.Managers.RunningSessionManager r11 = com.treadly.Treadly.Data.Managers.RunningSessionManager.getInstance()
            double r11 = r11.getLastTotalDuration()
            com.treadly.Treadly.Data.Managers.RunningSessionManager r13 = com.treadly.Treadly.Data.Managers.RunningSessionManager.getInstance()
            double r13 = r13.totalDailyDuration
            double r9 = java.lang.Math.max(r13, r9)
            com.treadly.Treadly.Data.Managers.RunningSessionManager r13 = com.treadly.Treadly.Data.Managers.RunningSessionManager.getInstance()
            com.treadly.Treadly.Data.Model.UserProfileInfo r13 = r13.userProfile
            if (r13 == 0) goto L84
            int[] r13 = com.treadly.Treadly.UI.TreadlyConnect.TreadlyConnectFragment.AnonymousClass45.$SwitchMap$com$treadly$Treadly$Data$Model$UserDailyGoalType
            com.treadly.Treadly.Data.Managers.RunningSessionManager r14 = com.treadly.Treadly.Data.Managers.RunningSessionManager.getInstance()
            com.treadly.Treadly.Data.Model.UserProfileInfo r14 = r14.userProfile
            com.treadly.Treadly.Data.Model.UserDailyGoalType r14 = r14.dailyGoalType()
            int r14 = r14.ordinal()
            r13 = r13[r14]
            r14 = -4616189618054758400(0xbff0000000000000, double:-1.0)
            r2 = -1
            r17 = 1
            switch(r13) {
                case 1: goto L7b;
                case 2: goto L78;
                case 3: goto L73;
                case 4: goto L6e;
                default: goto L6d;
            }
        L6d:
            goto L84
        L6e:
            int r2 = (r11 > r14 ? 1 : (r11 == r14 ? 0 : -1))
            if (r2 != 0) goto L80
            goto L7d
        L73:
            int r2 = (r5 > r14 ? 1 : (r5 == r14 ? 0 : -1))
            if (r2 != 0) goto L80
            goto L7d
        L78:
            if (r3 != r2) goto L80
            goto L7d
        L7b:
            if (r0 != r2) goto L80
        L7d:
            r2 = r17
            goto L81
        L80:
            r2 = 0
        L81:
            r21 = r2
            goto L86
        L84:
            r21 = 0
        L86:
            r2 = r22
            com.treadly.Treadly.UI.TreadlyConnect.TreadlyConnectStartState r13 = r2.startState
            com.treadly.Treadly.UI.TreadlyConnect.TreadlyConnectStartState r14 = com.treadly.Treadly.UI.TreadlyConnect.TreadlyConnectStartState.stop
            if (r13 != r14) goto L96
        L8e:
            r15 = r1
            r16 = r4
            r17 = r7
            r19 = r9
            goto La7
        L96:
            int r1 = java.lang.Math.max(r0, r1)
            int r4 = java.lang.Math.max(r3, r4)
            double r7 = java.lang.Math.max(r5, r7)
            double r9 = java.lang.Math.max(r11, r9)
            goto L8e
        La7:
            androidx.fragment.app.FragmentActivity r0 = r22.getActivity()
            com.treadly.Treadly.UI.TreadlyConnect.-$$Lambda$TreadlyConnectFragment$GFbDsBZd_NWSM4oUL2UFfIHV9v0 r1 = new com.treadly.Treadly.UI.TreadlyConnect.-$$Lambda$TreadlyConnectFragment$GFbDsBZd_NWSM4oUL2UFfIHV9v0
            r13 = r1
            r14 = r22
            r13.<init>()
            com.treadly.Treadly.UI.Util.ActivityUtil.runOnUiThread(r0, r1)
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.treadly.Treadly.UI.TreadlyConnect.TreadlyConnectFragment.resetCalories():void");
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* renamed from: com.treadly.Treadly.UI.TreadlyConnect.TreadlyConnectFragment$45  reason: invalid class name */
    /* loaded from: classes2.dex */
    public static /* synthetic */ class AnonymousClass45 {
        static final /* synthetic */ int[] $SwitchMap$com$treadly$client$lib$sdk$Model$DeviceStatusCode;

        static {
            try {
                $SwitchMap$com$treadly$Treadly$Data$Model$UserDailyGoalType[UserDailyGoalType.calories.ordinal()] = 1;
            } catch (NoSuchFieldError unused) {
            }
            try {
                $SwitchMap$com$treadly$Treadly$Data$Model$UserDailyGoalType[UserDailyGoalType.steps.ordinal()] = 2;
            } catch (NoSuchFieldError unused2) {
            }
            try {
                $SwitchMap$com$treadly$Treadly$Data$Model$UserDailyGoalType[UserDailyGoalType.distance.ordinal()] = 3;
            } catch (NoSuchFieldError unused3) {
            }
            try {
                $SwitchMap$com$treadly$Treadly$Data$Model$UserDailyGoalType[UserDailyGoalType.duration.ordinal()] = 4;
            } catch (NoSuchFieldError unused4) {
            }
            $SwitchMap$com$treadly$client$lib$sdk$Model$RemoteStatus = new int[RemoteStatus.values().length];
            try {
                $SwitchMap$com$treadly$client$lib$sdk$Model$RemoteStatus[RemoteStatus.automatic.ordinal()] = 1;
            } catch (NoSuchFieldError unused5) {
            }
            try {
                $SwitchMap$com$treadly$client$lib$sdk$Model$RemoteStatus[RemoteStatus.manual.ordinal()] = 2;
            } catch (NoSuchFieldError unused6) {
            }
            $SwitchMap$com$treadly$client$lib$sdk$Model$DistanceUnits = new int[DistanceUnits.values().length];
            try {
                $SwitchMap$com$treadly$client$lib$sdk$Model$DistanceUnits[DistanceUnits.KM.ordinal()] = 1;
            } catch (NoSuchFieldError unused7) {
            }
            try {
                $SwitchMap$com$treadly$client$lib$sdk$Model$DistanceUnits[DistanceUnits.MI.ordinal()] = 2;
            } catch (NoSuchFieldError unused8) {
            }
            $SwitchMap$com$treadly$client$lib$sdk$Model$DeviceStatusCode = new int[DeviceStatusCode.values().length];
            try {
                $SwitchMap$com$treadly$client$lib$sdk$Model$DeviceStatusCode[DeviceStatusCode.WIFI_NOT_CONNECTED_ERROR.ordinal()] = 1;
            } catch (NoSuchFieldError unused9) {
            }
            $SwitchMap$com$treadly$Treadly$UI$TreadlyConnect$TreadlyConnectFragment$ConnectionStatus = new int[ConnectionStatus.values().length];
            try {
                $SwitchMap$com$treadly$Treadly$UI$TreadlyConnect$TreadlyConnectFragment$ConnectionStatus[ConnectionStatus.notConnected.ordinal()] = 1;
            } catch (NoSuchFieldError unused10) {
            }
            try {
                $SwitchMap$com$treadly$Treadly$UI$TreadlyConnect$TreadlyConnectFragment$ConnectionStatus[ConnectionStatus.connected.ordinal()] = 2;
            } catch (NoSuchFieldError unused11) {
            }
            try {
                $SwitchMap$com$treadly$Treadly$UI$TreadlyConnect$TreadlyConnectFragment$ConnectionStatus[ConnectionStatus.disconnecting.ordinal()] = 3;
            } catch (NoSuchFieldError unused12) {
            }
            try {
                $SwitchMap$com$treadly$Treadly$UI$TreadlyConnect$TreadlyConnectFragment$ConnectionStatus[ConnectionStatus.connecting.ordinal()] = 4;
            } catch (NoSuchFieldError unused13) {
            }
            try {
                $SwitchMap$com$treadly$Treadly$UI$TreadlyConnect$TreadlyConnectFragment$ConnectionStatus[ConnectionStatus.reconnecting.ordinal()] = 5;
            } catch (NoSuchFieldError unused14) {
            }
            $SwitchMap$com$treadly$Treadly$UI$TreadlyConnect$TreadlyConnectStartState = new int[TreadlyConnectStartState.values().length];
            try {
                $SwitchMap$com$treadly$Treadly$UI$TreadlyConnect$TreadlyConnectStartState[TreadlyConnectStartState.start.ordinal()] = 1;
            } catch (NoSuchFieldError unused15) {
            }
            try {
                $SwitchMap$com$treadly$Treadly$UI$TreadlyConnect$TreadlyConnectStartState[TreadlyConnectStartState.stop.ordinal()] = 2;
            } catch (NoSuchFieldError unused16) {
            }
            try {
                $SwitchMap$com$treadly$Treadly$UI$TreadlyConnect$TreadlyConnectStartState[TreadlyConnectStartState.notConnected.ordinal()] = 3;
            } catch (NoSuchFieldError unused17) {
            }
            try {
                $SwitchMap$com$treadly$Treadly$UI$TreadlyConnect$TreadlyConnectStartState[TreadlyConnectStartState.starting.ordinal()] = 4;
            } catch (NoSuchFieldError unused18) {
            }
            try {
                $SwitchMap$com$treadly$Treadly$UI$TreadlyConnect$TreadlyConnectStartState[TreadlyConnectStartState.stopping.ordinal()] = 5;
            } catch (NoSuchFieldError unused19) {
            }
            try {
                $SwitchMap$com$treadly$Treadly$UI$TreadlyConnect$TreadlyConnectStartState[TreadlyConnectStartState.forcedStart.ordinal()] = 6;
            } catch (NoSuchFieldError unused20) {
            }
            try {
                $SwitchMap$com$treadly$Treadly$UI$TreadlyConnect$TreadlyConnectStartState[TreadlyConnectStartState.forcedStarting.ordinal()] = 7;
            } catch (NoSuchFieldError unused21) {
            }
        }
    }

    void setCalories(int i, int i2, double d, double d2) {
        setCalories(i, i2, d, d2, false);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void setCalories(int i, int i2, double d, double d2, boolean z) {
        int dailyGoal;
        int dailyGoal2;
        int dailyGoal3;
        boolean z2 = this.startState == TreadlyConnectStartState.stop;
        if (z2 || i >= this.calories || z) {
            setCalories(i, this.dailyGoalTarget);
        }
        if (z2 || i2 >= this.dailySteps || z) {
            setDailySteps(i2, this.dailyGoalTarget);
        }
        if (z2 || d >= this.dailyDistance || z) {
            setDailyDistance(d, this.dailyGoalTarget);
        }
        if (z2 || d2 >= this.dailyDuration || z) {
            setDailyDuration(d2, this.dailyGoalTarget);
        }
        UserProfileInfo userProfileInfo = RunningSessionManager.getInstance().userProfile;
        if (userProfileInfo == null) {
            int lastDailyGoal = RunningSessionManager.getInstance().getLastDailyGoal();
            this.dailyGoalTarget = RunningSessionManager.getInstance().getLastDailyGoalTarget();
            if (lastDailyGoal >= 0) {
                this.caloriesTextView.setText(String.format(Locale.getDefault(), "%d%% Completed %s", Integer.valueOf(lastDailyGoal), this.dailyGoalTarget));
                setDailyGoalPercentage(lastDailyGoal);
                return;
            }
            this.caloriesTextView.setText(String.format(Locale.getDefault(), "%d%% Completed %s", Integer.valueOf(this.dailyGoalPercentage), this.dailyGoalTarget));
            setDailyGoalPercentage(this.dailyGoalPercentage);
            return;
        }
        String str = "";
        double dailyGoal4 = userProfileInfo.getDailyGoal();
        switch (userProfileInfo.dailyGoalType()) {
            case calories:
                d = i;
                str = "(From " + this.stepsFormatter.format(dailyGoal) + " " + (((int) userProfileInfo.getDailyGoal()) != 1 ? TreadlyEventHelper.keyCalories : "calorie") + ")";
                break;
            case steps:
                d = i2;
                str = "(From " + this.stepsFormatter.format(dailyGoal2) + " " + (((int) userProfileInfo.getDailyGoal()) != 1 ? TreadlyEventHelper.keySteps : "step") + ")";
                break;
            case distance:
                double dailyGoal5 = userProfileInfo.getDailyGoal(false) + 1.0E-4d;
                str = "(From " + this.distanceFormatter.format(dailyGoal5) + " " + (userProfileInfo.units == DistanceUnits.MI ? "mi" : "km") + ")";
                break;
            case duration:
                str = "(From " + this.stepsFormatter.format(dailyGoal3) + " " + (((int) (userProfileInfo.getDailyGoal() / 60.0d)) != 1 ? "minutes" : "minute") + ")";
                d = d2;
                break;
            default:
                d = 0.0d;
                break;
        }
        this.dailyGoalTarget = str;
        if (dailyGoal4 > Utils.DOUBLE_EPSILON) {
            setDailyGoalPercentage((int) ((d * 100.0d) / dailyGoal4));
        } else {
            setDailyGoalPercentage(0);
        }
    }

    /* JADX WARN: Removed duplicated region for block: B:45:0x00da  */
    /* JADX WARN: Removed duplicated region for block: B:46:0x00e2  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    void handleCurrentCaloriesUpdated() {
        /*
            Method dump skipped, instructions count: 246
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: com.treadly.Treadly.UI.TreadlyConnect.TreadlyConnectFragment.handleCurrentCaloriesUpdated():void");
    }

    @Override // androidx.fragment.app.Fragment
    public void onActivityResult(int i, int i2, Intent intent) {
        super.onActivityResult(i, i2, intent);
        if (this.pauseStateAlert != null) {
            this.pauseStateAlert.handleOnActivityResult(i, i2, intent);
        }
    }

    private void initSpeedOptions(View view) {
        this.speedOptionContainer = (ConstraintLayout) view.findViewById(R.id.speed_options_container);
        this.speedOptionView = (TreadlySpeedOptionView) view.findViewById(R.id.speed_options_view);
        this.speedOptionView.listener = new TreadlySpeedOptionView.TreadlySpeedOptionViewListener() { // from class: com.treadly.Treadly.UI.TreadlyConnect.TreadlyConnectFragment.43
            @Override // com.treadly.Treadly.UI.TreadlyConnect.SpeedOption.TreadlySpeedOptionView.TreadlySpeedOptionViewListener
            public void onSpeedSelected(int i) {
                TreadlyConnectFragment.this.handleSpeedOptionPressed(i);
            }
        };
        this.speedOptionContainer.setClickable(false);
        this.speedOptionContainer.setFocusable(false);
        this.speedOptionContainer.setOnClickListener(new View.OnClickListener() { // from class: com.treadly.Treadly.UI.TreadlyConnect.-$$Lambda$TreadlyConnectFragment$xllDw6Gj02a6rMTnRUji8QYiog0
            @Override // android.view.View.OnClickListener
            public final void onClick(View view2) {
                TreadlyConnectFragment.this.removeSpeedOptions();
            }
        });
        removeSpeedOptions();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void attachSpeedOptions() {
        if (this.currentDeviceStatusInfo != null && !this.currentDeviceStatusInfo.getPairingModeState() && this.isConnected && this.connectState == ConnectionStatus.connected && checkSessionOwner(this.currentDeviceStatusInfo) && !singleUserMode) {
            if (this.speedOptionContainer != null) {
                this.speedOptionContainer.setVisibility(0);
            }
            if (this.speedOptionView != null) {
                this.speedOptionView.setVisibility(0);
                this.speedOptionView.setScaleX(0.0f);
                this.speedOptionView.setScaleY(0.0f);
                this.speedOptionView.animate().scaleX(1.0f).scaleY(1.0f).setDuration(150L).setListener(null).start();
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void removeSpeedOptions() {
        if (this.speedOptionView == null || this.speedOptionContainer == null) {
            return;
        }
        this.speedOptionView.setScaleX(1.0f);
        this.speedOptionView.setScaleY(1.0f);
        this.speedOptionView.animate().scaleX(0.1f).scaleY(0.1f).setDuration(150L).setListener(new AnimatorListenerAdapter() { // from class: com.treadly.Treadly.UI.TreadlyConnect.TreadlyConnectFragment.44
            @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
            public void onAnimationEnd(Animator animator) {
                if (TreadlyConnectFragment.this.speedOptionContainer != null) {
                    TreadlyConnectFragment.this.speedOptionContainer.setVisibility(8);
                }
                TreadlyConnectFragment.this.speedOptionView.setVisibility(8);
            }
        }).start();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void handleSpeedOptionPressed(int i) {
        if (this.sliderFragment == null || this.currentDeviceStatusInfo == null) {
            return;
        }
        this.sliderFragment.finishSliderAnimation();
        this.sliderFragment.stopSliderAnimation();
        float f = i;
        this.sliderFragment.targetSliderSpeed = f;
        HandrailStatus handrailStatus = DeviceStatusVerification.shared.handrailStatus;
        SpeedInfo speedInfo = this.currentDeviceStatusInfo.getSpeedInfo();
        if (handrailStatus == HandrailStatus.DOWN && this.sliderFragment.targetSliderSpeed > speedInfo.getMaximumSpeed()) {
            this.sliderFragment.targetSliderSpeed = speedInfo.getMaximumSpeed();
            showAlert("Warning", "The handrail must be in the up position to increase the speed further");
        }
        if (!this.sliderFragment.isSliderAnimating) {
            this.sliderFragment.currentSliderSpeed = this.currentDeviceStatusInfo.getSpeedInfo().getTargetSpeed();
        }
        if (this.startState == TreadlyConnectStartState.start) {
            this.sliderFragment.startSliderAnimationTimer(this.sliderFragment.targetSliderSpeed, this.sliderFragment.currentSliderSpeed);
        } else if (this.startState == TreadlyConnectStartState.stop) {
            this.sliderFragment.speedSliderAnimationQueued = true;
        }
        setSpeed(f);
        removeSpeedOptions();
    }
}
