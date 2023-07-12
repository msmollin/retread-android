package com.treadly.Treadly.UI.TreadlyVideo.PrivateStream;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.res.TypedArray;
import android.media.AudioManager;
import android.os.Bundle;
import android.os.Environment;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.internal.view.SupportMenu;
import androidx.fragment.app.FragmentActivity;
import com.facebook.appevents.AppEventsConstants;
import com.github.mikephil.charting.utils.Utils;
import com.google.android.gms.common.internal.ImagesContract;
import com.opentok.android.AudioDeviceManager;
import com.opentok.android.BaseVideoCapturer;
import com.opentok.android.BaseVideoRenderer;
import com.opentok.android.OpentokError;
import com.opentok.android.Publisher;
import com.opentok.android.PublisherKit;
import com.opentok.android.Session;
import com.opentok.android.Stream;
import com.opentok.android.Subscriber;
import com.treadly.Treadly.Data.Delegates.TreadlyVideoPrivateEventDelegate;
import com.treadly.Treadly.Data.Listeners.TreadlyServiceResponseEventAdapter;
import com.treadly.Treadly.Data.Listeners.TreadlyVideoInviteListener;
import com.treadly.Treadly.Data.Managers.AppActivityManager;
import com.treadly.Treadly.Data.Managers.RunningSessionManager;
import com.treadly.Treadly.Data.Managers.TreadlyEventManager;
import com.treadly.Treadly.Data.Managers.TreadlyServiceManager;
import com.treadly.Treadly.Data.Model.TrainerModeState;
import com.treadly.Treadly.Data.Model.UserInfo;
import com.treadly.Treadly.Data.Model.UserProfileInfo;
import com.treadly.Treadly.Data.Model.UserTrainerMode;
import com.treadly.Treadly.Data.Model.UserVideoPrivateStateInfo;
import com.treadly.Treadly.Data.Model.UsersTrainerModes;
import com.treadly.Treadly.R;
import com.treadly.Treadly.UI.TreadlyConnect.DeviceConnect.TreadlyDeviceConnectStatusFragment;
import com.treadly.Treadly.UI.TreadlyConnect.TreadlyConnectStartState;
import com.treadly.Treadly.UI.TreadlyConnect.TreadlyDeviceConnectFragment;
import com.treadly.Treadly.UI.TreadlyVideo.Data.InviteServiceHelper;
import com.treadly.Treadly.UI.TreadlyVideo.Data.VideoConstrainHelper;
import com.treadly.Treadly.UI.TreadlyVideo.Data.VideoServiceHelper;
import com.treadly.Treadly.UI.TreadlyVideo.Data.VideoServiceSessionInfo;
import com.treadly.Treadly.UI.TreadlyVideo.Data.VideoServiceUserStatsInfo;
import com.treadly.Treadly.UI.TreadlyVideo.Data.VideoServiceVideoService;
import com.treadly.Treadly.UI.TreadlyVideo.Data.VideoUploaderManager;
import com.treadly.Treadly.UI.TreadlyVideo.PrivateStream.TreadlyPrivateStreamHostFragment;
import com.treadly.Treadly.UI.TreadlyVideo.PrivateStream.VideoComponents.BTAudioDialog;
import com.treadly.Treadly.UI.TreadlyVideo.PrivateStream.VideoComponents.TreadlyPrivateStreamPauseView;
import com.treadly.Treadly.UI.TreadlyVideo.PrivateStream.VideoComponents.TreadlyVideoAudioCapturer;
import com.treadly.Treadly.UI.TreadlyVideo.PrivateStream.VideoComponents.TreadlyVideoCameraCapturer;
import com.treadly.Treadly.UI.TreadlyVideo.TreadlyVideoInviteFriend;
import com.treadly.Treadly.UI.TreadlyVideo.TreadlyVideoSliderFragment;
import com.treadly.Treadly.UI.TreadlyVideo.VideoUserStats.TreadlyVideoStatsViewController;
import com.treadly.Treadly.UI.TreadmillControl.Data.TreadlyControlContent;
import com.treadly.Treadly.UI.TreadmillControl.Data.TreadlyRunState;
import com.treadly.Treadly.UI.TreadmillControl.TreadlyTreadmillController;
import com.treadly.Treadly.UI.Util.ActivityUtil;
import com.treadly.Treadly.UI.Util.BaseClasses.BaseFragment;
import com.treadly.Treadly.UI.Util.SharedPreferences;
import com.treadly.client.lib.sdk.Listeners.DeviceConnectionEventListener;
import com.treadly.client.lib.sdk.Listeners.RequestEventAdapter;
import com.treadly.client.lib.sdk.Managers.TreadlyLogManager;
import com.treadly.client.lib.sdk.Model.AuthenticationState;
import com.treadly.client.lib.sdk.Model.BluetoothConnectionState;
import com.treadly.client.lib.sdk.Model.ComponentInfo;
import com.treadly.client.lib.sdk.Model.ComponentType;
import com.treadly.client.lib.sdk.Model.ConnectionStatusCode;
import com.treadly.client.lib.sdk.Model.DeviceConnectionEvent;
import com.treadly.client.lib.sdk.Model.DeviceConnectionStatus;
import com.treadly.client.lib.sdk.Model.DeviceInfo;
import com.treadly.client.lib.sdk.Model.DeviceStatus;
import com.treadly.client.lib.sdk.Model.DistanceUnits;
import com.treadly.client.lib.sdk.Model.HandrailStatus;
import com.treadly.client.lib.sdk.TreadlyClientLib;
import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.Timer;
import java.util.TimerTask;
import org.json.JSONException;

/* loaded from: classes2.dex */
public class TreadlyPrivateStreamHostFragment extends BaseFragment {
    private static final int REQUEST_ENABLE_BT = 1;
    public static final String TAG = "privateStreamHost";
    private static TreadlyVideoAudioCapturer audioCapturer = null;
    private static TreadlyVideoCameraCapturer cameraCapturer = null;
    private static final String clientStreamKey = "client_stream";
    private static final String hostStreamKey = "host_stream";
    private static final double kmToMi = 0.6214d;
    private static String mApiKey = "";
    private static String mSessionId = "";
    private static String mToken = "";
    private static final double miToKm = 1.6093d;
    String activationTarget;
    private BTAudioDialog audioDialog;
    private boolean autoConnected;
    private Button connectButton;
    private String deviceName;
    private ImageButton endWorkoutButton;
    private ImageButton friendInviteButton;
    private boolean isConnected;
    public boolean isHost;
    String localFileName;
    private AudioManager mAudioManager;
    private ConstraintLayout mContainer;
    private Publisher mPublisher;
    private Session mSession;
    private ImageButton muteAudioButton;
    public UserInfo pendingUserInvite;
    private ImageButton recordButton;
    private TreadlyVideoSliderFragment sliderFragment;
    private FrameLayout sliderFrameLayout;
    private ImageButton speakerButton;
    private Button statsButton;
    private ImageButton switchCameraButton;
    String targetTreadmillName;
    private TreadlyControlContent treadlyContent;
    private TreadlyTreadmillController treadlyControl;
    private TreadlyVideoStatsViewController treadlyStatsController;
    private ImageButton turnOffCameraButton;
    public String userId = "";
    private DeviceConnectionStatus connectState = DeviceConnectionStatus.notConnected;
    private boolean isDeviceConnected = false;
    private AuthenticationState authenticationState = AuthenticationState.unknown;
    private TreadlyConnectStartState startState = TreadlyConnectStartState.notConnected;
    private final TreadlyVideoSliderFragment.TreadlyVideoSliderFragmentEventListener sliderAdapater = new TreadlyVideoSliderFragment.TreadlyVideoSliderFragmentEventListener() { // from class: com.treadly.Treadly.UI.TreadlyVideo.PrivateStream.TreadlyPrivateStreamHostFragment.3
        @Override // com.treadly.Treadly.UI.TreadlyVideo.TreadlyVideoSliderFragment.TreadlyVideoSliderFragmentEventListener
        public boolean getValueForHandrail(float f, DistanceUnits distanceUnits, HandrailStatus handrailStatus) {
            return false;
        }

        @Override // com.treadly.Treadly.UI.TreadlyVideo.TreadlyVideoSliderFragment.TreadlyVideoSliderFragmentEventListener
        public void onImpactOccurred() {
        }

        @Override // com.treadly.Treadly.UI.TreadlyVideo.TreadlyVideoSliderFragment.TreadlyVideoSliderFragmentEventListener
        public void onPowerOnDevice() {
        }

        @Override // com.treadly.Treadly.UI.TreadlyVideo.TreadlyVideoSliderFragment.TreadlyVideoSliderFragmentEventListener
        public void onShowAlert(String str, String str2) {
        }

        @Override // com.treadly.Treadly.UI.TreadlyVideo.TreadlyVideoSliderFragment.TreadlyVideoSliderFragmentEventListener
        public void onStopPressed() {
        }

        @Override // com.treadly.Treadly.UI.TreadlyVideo.TreadlyVideoSliderFragment.TreadlyVideoSliderFragmentEventListener
        public void onPowerOffDevice() {
            if (TreadlyPrivateStreamHostFragment.this.treadlyControl != null) {
                TreadlyPrivateStreamHostFragment.this.treadlyControl.pause();
                TreadlyPrivateStreamHostFragment.this.sliderFragment.setStartState(TreadlyConnectStartState.stopping);
            }
        }

        @Override // com.treadly.Treadly.UI.TreadlyVideo.TreadlyVideoSliderFragment.TreadlyVideoSliderFragmentEventListener
        public void onSetSpeed(float f) {
            if (TreadlyPrivateStreamHostFragment.this.treadlyControl != null) {
                TreadlyPrivateStreamHostFragment.this.treadlyControl.setSpeed(f);
            }
        }
    };
    private final ArrayList<TreadlyPrivateVideoSubscriber> subscribers = new ArrayList<>();
    private final Session.SessionListener sessionListener = new AnonymousClass4();
    private final PublisherKit.PublisherListener publisherListener = new AnonymousClass5();
    private String currentUserId = "";
    private List<UserInfo> currentFriends = new ArrayList();
    private String mBroadcastId = "";
    private final HashMap<String, Timer> inviteTimers = new HashMap<>();
    boolean isRecording = false;
    private boolean endWorkoutSelected = false;
    private final boolean hostEnded = false;
    private boolean isLeaving = false;
    private final TreadlyVideoPrivateEventDelegate videoPrivateEventListener = new AnonymousClass12();
    double currentSpeed = Utils.DOUBLE_EPSILON;
    private boolean enableRecording = false;
    private ArrayList<UserVideoPrivateStateInfo> userStates = new ArrayList<>();
    private final DeviceConnectionEventListener deviceAdapter = new DeviceConnectionEventListener() { // from class: com.treadly.Treadly.UI.TreadlyVideo.PrivateStream.TreadlyPrivateStreamHostFragment.13
        @Override // com.treadly.client.lib.sdk.Listeners.DeviceConnectionEventListener
        public void onDeviceConnectionChanged(DeviceConnectionEvent deviceConnectionEvent) {
            if (deviceConnectionEvent.getStatus() == DeviceConnectionStatus.connected) {
                TreadlyPrivateStreamHostFragment.this.setConnectState(DeviceConnectionStatus.connected);
                TreadlyPrivateStreamHostFragment.this.treadlyControl.setAuthenticationState();
            } else if (deviceConnectionEvent.getStatus() == DeviceConnectionStatus.disconnecting) {
                TreadlyPrivateStreamHostFragment.this.setConnectState(DeviceConnectionStatus.notConnected);
                TreadlyPrivateStreamHostFragment.this.setAuthenticationState(AuthenticationState.unknown);
                TreadlyPrivateStreamHostFragment.this.treadlyControl.mAuthenticationState = AuthenticationState.unknown;
            }
        }

        @Override // com.treadly.client.lib.sdk.Listeners.DeviceConnectionEventListener
        public void onDeviceConnectionDeviceDiscovered(DeviceInfo deviceInfo) {
            TreadlyPrivateStreamHostFragment.this.handleDeviceFound(deviceInfo);
        }
    };
    private final RequestEventAdapter requestEventAdapter = new RequestEventAdapter() { // from class: com.treadly.Treadly.UI.TreadlyVideo.PrivateStream.TreadlyPrivateStreamHostFragment.14
        @Override // com.treadly.client.lib.sdk.Listeners.RequestEventAdapter, com.treadly.client.lib.sdk.Listeners.RequestEventListener
        public void onRequestStatusResponse(boolean z, DeviceStatus deviceStatus) {
            super.onRequestStatusResponse(z, deviceStatus);
            if (z && deviceStatus != null && TreadlyPrivateStreamHostFragment.this.connectState == DeviceConnectionStatus.connected && TreadlyPrivateStreamHostFragment.this.isConnected) {
                TreadlyPrivateStreamHostFragment.this.sliderFragment.currentDeviceStatusInfo = deviceStatus;
            }
        }
    };
    private final DeviceConnectionEventListener deviceConnectionListener = new DeviceConnectionEventListener() { // from class: com.treadly.Treadly.UI.TreadlyVideo.PrivateStream.TreadlyPrivateStreamHostFragment.15
        @Override // com.treadly.client.lib.sdk.Listeners.DeviceConnectionEventListener
        public void onDeviceConnectionChanged(DeviceConnectionEvent deviceConnectionEvent) {
        }

        @Override // com.treadly.client.lib.sdk.Listeners.DeviceConnectionEventListener
        public void onDeviceConnectionDeviceDiscovered(DeviceInfo deviceInfo) {
        }
    };
    public ComponentInfo[] componentList = new ComponentInfo[0];

    /* JADX INFO: Access modifiers changed from: private */
    public void setConnectState(DeviceConnectionStatus deviceConnectionStatus) {
        this.connectState = deviceConnectionStatus;
        setIsDeviceConnected(this.connectState == DeviceConnectionStatus.connected && this.authenticationState == AuthenticationState.active);
        this.treadlyStatsController.setDeviceConnectState(this.connectState);
        setConnectButton(deviceConnectionStatus);
    }

    private void setIsDeviceConnected(boolean z) {
        this.isDeviceConnected = z;
        if (this.sliderFragment != null) {
            this.sliderFragment.isConnected = this.isDeviceConnected;
        }
        handleDeviceConnectedState(this.isDeviceConnected);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void setAuthenticationState(AuthenticationState authenticationState) {
        if (authenticationState == this.authenticationState) {
            return;
        }
        setIsDeviceConnected(this.connectState == DeviceConnectionStatus.connected && this.authenticationState == AuthenticationState.active);
        this.treadlyStatsController.setAuthenticationState(this.authenticationState);
    }

    @Override // androidx.fragment.app.Fragment
    public void onCreate(@Nullable Bundle bundle) {
        super.onCreate(bundle);
        initController();
        initStatsView();
        setIsDeviceConnected(TreadlyClientLib.shared.isDeviceConnected());
    }

    @Override // androidx.fragment.app.Fragment
    @Nullable
    public View onCreateView(@NonNull LayoutInflater layoutInflater, @Nullable ViewGroup viewGroup, @Nullable Bundle bundle) {
        initServiceRole();
        updateControls();
        getUserInfo();
        getFriendsInfo();
        TreadlyClientLib.shared.addDeviceConnectionEventListener(this.deviceConnectionListener);
        return layoutInflater.inflate(R.layout.fragment_treadly_private_stream_host, viewGroup, false);
    }

    @Override // com.treadly.Treadly.UI.Util.BaseClasses.BaseFragment, androidx.fragment.app.Fragment
    public void onViewCreated(@NonNull View view, @Nullable Bundle bundle) {
        this.mContainer = (ConstraintLayout) view.findViewById(R.id.video_container);
        initMenuButtons(view);
        this.statsButton = (Button) view.findViewById(R.id.stats_button);
        this.statsButton.setOnClickListener(new View.OnClickListener() { // from class: com.treadly.Treadly.UI.TreadlyVideo.PrivateStream.-$$Lambda$TreadlyPrivateStreamHostFragment$j516HHuEuXXqIGknABvEGAy_dRA
            @Override // android.view.View.OnClickListener
            public final void onClick(View view2) {
                TreadlyPrivateStreamHostFragment.this.treadlyStatsController.show();
            }
        });
        this.connectButton = (Button) view.findViewById(R.id.private_connect_button);
        this.connectButton.setOnClickListener(new View.OnClickListener() { // from class: com.treadly.Treadly.UI.TreadlyVideo.PrivateStream.-$$Lambda$TreadlyPrivateStreamHostFragment$CUi6hJ_V5xE8woZryxyFubuyDjo
            @Override // android.view.View.OnClickListener
            public final void onClick(View view2) {
                TreadlyPrivateStreamHostFragment.this.onConnectButtonPressed();
            }
        });
        this.sliderFrameLayout = (FrameLayout) view.findViewById(R.id.video_slider_layout);
        this.sliderFrameLayout.setClipToOutline(true);
        this.sliderFragment = new TreadlyVideoSliderFragment();
        this.sliderFragment.parentFragmentPrivate = this;
        this.sliderFragment.listener = this.sliderAdapater;
        if (getActivity() != null) {
            getActivity().getSupportFragmentManager().beginTransaction().add(R.id.video_slider_layout, this.sliderFragment, TreadlyVideoSliderFragment.TAG).commit();
        }
        setConnectState(this.isDeviceConnected ? DeviceConnectionStatus.connected : DeviceConnectionStatus.notConnected);
        if (this.connectState == DeviceConnectionStatus.connected) {
            this.treadlyControl.setAuthenticationState();
        }
        startPublisherPreview();
        this.mContainer.addView(this.mPublisher.getView());
        updateLayout();
    }

    @Override // androidx.fragment.app.Fragment
    public void onStart() {
        super.onStart();
        TreadlyClientLib.shared.addRequestEventListener(this.requestEventAdapter);
        TreadlyClientLib.shared.addDeviceConnectionEventListener(this.deviceConnectionListener);
        TreadlyClientLib.shared.getDeviceStatus();
    }

    @Override // androidx.fragment.app.Fragment
    public void onResume() {
        super.onResume();
        if (this.mSession != null) {
            this.mSession.onResume();
            TreadlyEventManager.getInstance().unpauseVideoPrivate(mSessionId);
        }
        hideBottomNavigation();
        VideoUploaderManager.context = getContext();
        VideoUploaderManager.shared.stop();
    }

    @Override // androidx.fragment.app.Fragment
    public void onPause() {
        super.onPause();
        if (this.mSession == null) {
            return;
        }
        this.mSession.onPause();
        TreadlyEventManager.getInstance().pauseVideoPrivate(mSessionId);
        try {
            VideoUploaderManager.shared.start();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override // androidx.fragment.app.Fragment
    public void onDestroy() {
        Log.d(TAG, "onDestroy");
        TreadlyEventManager.getInstance().removeVideoPrivateDelegate(this.videoPrivateEventListener);
        disconnectSession();
        TreadlyClientLib.shared.removeDeviceConnectionEventListener(this.deviceConnectionListener);
        if (!mSessionId.isEmpty()) {
            TreadlyEventManager.getInstance().leaveVideoPrivate(mSessionId);
        }
        this.treadlyControl.releaseTreadmillControl();
        showBottomNavigation();
        super.onDestroy();
    }

    private void initController() {
        this.treadlyContent = new TreadlyControlContent();
        this.treadlyControl = new TreadlyTreadmillController(this.treadlyContent);
        this.treadlyControl.adapter = new TreadlyTreadmillController.TreadlyTreadmillAdapter() { // from class: com.treadly.Treadly.UI.TreadlyVideo.PrivateStream.TreadlyPrivateStreamHostFragment.1
            @Override // com.treadly.Treadly.UI.TreadmillControl.TreadlyTreadmillController.TreadlyTreadmillAdapter
            public void didChangeDeviceConnection(boolean z) {
            }

            @Override // com.treadly.Treadly.UI.TreadmillControl.TreadlyTreadmillController.TreadlyTreadmillAdapter
            public void didChangeStatus(DeviceStatus deviceStatus) {
                if (TreadlyPrivateStreamHostFragment.this.authenticationState == AuthenticationState.active) {
                }
            }

            @Override // com.treadly.Treadly.UI.TreadmillControl.TreadlyTreadmillController.TreadlyTreadmillAdapter
            public void didChangeContent(TreadlyControlContent treadlyControlContent) {
                TreadlyPrivateStreamHostFragment.this.authenticationState = treadlyControlContent.authenticationState;
                if (treadlyControlContent.authenticationState == AuthenticationState.active) {
                    TreadlyPrivateStreamHostFragment.this.setCurrentSpeed((float) treadlyControlContent.speedValue, (float) treadlyControlContent.queuedSpeed);
                    TreadlyPrivateStreamHostFragment.this.sendCurrentUserStats(treadlyControlContent);
                } else if (treadlyControlContent.authenticationState == AuthenticationState.unknown) {
                    TreadlyPrivateStreamHostFragment.this.treadlyControl.setAuthenticationState();
                }
            }
        };
    }

    private void initStatsView() {
        String userId = TreadlyServiceManager.getInstance().getUserId();
        if (userId == null) {
            return;
        }
        this.treadlyStatsController = new TreadlyVideoStatsViewController(getContext());
        this.treadlyStatsController.isHost = this.isHost;
        this.treadlyStatsController.hostId = this.userId;
        this.treadlyStatsController.currentUserId = userId;
        this.treadlyStatsController.adapter = new TreadlyVideoStatsViewController.VideoStatsAdapter() { // from class: com.treadly.Treadly.UI.TreadlyVideo.PrivateStream.TreadlyPrivateStreamHostFragment.2
            @Override // com.treadly.Treadly.UI.TreadlyVideo.VideoUserStats.TreadlyVideoStatsViewController.VideoStatsAdapter
            public void studentModeDidChange(boolean z) {
            }

            @Override // com.treadly.Treadly.UI.TreadlyVideo.VideoUserStats.TreadlyVideoStatsViewController.VideoStatsAdapter
            public void trainerModeDidChange(boolean z) {
            }

            @Override // com.treadly.Treadly.UI.TreadlyVideo.VideoUserStats.TreadlyVideoStatsViewController.VideoStatsAdapter
            public void trainerModeStateChanged(boolean z, boolean z2) {
            }

            @Override // com.treadly.Treadly.UI.TreadlyVideo.VideoUserStats.TreadlyVideoStatsViewController.VideoStatsAdapter
            public void studentModeRequested(TrainerModeState trainerModeState, String str) {
                if (TreadlyPrivateStreamHostFragment.mSessionId == null || TreadlyPrivateStreamHostFragment.mSessionId.isEmpty()) {
                    return;
                }
                Log.d("trainer", "Sending student mode request: " + trainerModeState);
                TreadlyEventManager.getInstance().sendVideoPrivateStudentModeRequest(TreadlyPrivateStreamHostFragment.mSessionId, trainerModeState, str);
            }

            @Override // com.treadly.Treadly.UI.TreadlyVideo.VideoUserStats.TreadlyVideoStatsViewController.VideoStatsAdapter
            public void trainerModeRequested(TrainerModeState trainerModeState) {
                if (TreadlyPrivateStreamHostFragment.mSessionId == null || TreadlyPrivateStreamHostFragment.mSessionId.isEmpty()) {
                    return;
                }
                Log.d("trainer", "Sending trainer mode request: " + trainerModeState);
                TreadlyEventManager.getInstance().sendVideoPrivateTrainerModeRequest(TreadlyPrivateStreamHostFragment.mSessionId, trainerModeState);
            }

            @Override // com.treadly.Treadly.UI.TreadlyVideo.VideoUserStats.TreadlyVideoStatsViewController.VideoStatsAdapter
            public void studentModeResponded(TrainerModeState trainerModeState, String str) {
                if (TreadlyPrivateStreamHostFragment.mSessionId == null || TreadlyPrivateStreamHostFragment.mSessionId.isEmpty()) {
                    return;
                }
                TreadlyEventManager.getInstance().sendVideoPrivateStudentModeResponse(TreadlyPrivateStreamHostFragment.mSessionId, trainerModeState, str);
            }

            @Override // com.treadly.Treadly.UI.TreadlyVideo.VideoUserStats.TreadlyVideoStatsViewController.VideoStatsAdapter
            public void trainerModeResponded(TrainerModeState trainerModeState) {
                if (TreadlyPrivateStreamHostFragment.mSessionId == null || TreadlyPrivateStreamHostFragment.mSessionId.isEmpty()) {
                    return;
                }
                Log.d("trainer", "Sending trainer mode response: " + trainerModeState);
                TreadlyEventManager.getInstance().sendVideoPrivateTrainerModeResponse(TreadlyPrivateStreamHostFragment.mSessionId, trainerModeState);
            }

            @Override // com.treadly.Treadly.UI.TreadlyVideo.VideoUserStats.TreadlyVideoStatsViewController.VideoStatsAdapter
            public void broadcastUserTrainerModes(List<UsersTrainerModes> list) {
                if (!TreadlyPrivateStreamHostFragment.this.isHost || TreadlyPrivateStreamHostFragment.mSessionId == null || TreadlyPrivateStreamHostFragment.mSessionId.isEmpty()) {
                    return;
                }
                TreadlyEventManager.getInstance().sendVideoPrivateUsersTrainerModes(TreadlyPrivateStreamHostFragment.mSessionId, list);
            }
        };
        if (this.isHost) {
            String name = TreadlyServiceManager.getInstance().getName();
            String userId2 = TreadlyServiceManager.getInstance().getUserId();
            if (name != null || userId2 != null) {
                this.treadlyStatsController.handleUserJoined(new UserInfo(userId2, name, ""));
            }
        }
        String name2 = TreadlyServiceManager.getInstance().getName();
        if (name2 != null) {
            this.treadlyStatsController.addInitialUserStats(new UserInfo(userId, name2, ""));
        }
        this.treadlyStatsController.updateUserDeviceConnectionState(userId, this.isDeviceConnected);
    }

    private void initServiceRole() {
        if (this.isHost) {
            initServiceForHost();
        } else {
            initServiceForClient();
        }
    }

    private void initServiceForHost() {
        VideoServiceHelper.getCredentials(new VideoServiceHelper.VideoServiceCredentialsListener() { // from class: com.treadly.Treadly.UI.TreadlyVideo.PrivateStream.-$$Lambda$TreadlyPrivateStreamHostFragment$hy-UOECSK9B5IaWIeMrvpDN--ws
            @Override // com.treadly.Treadly.UI.TreadlyVideo.Data.VideoServiceHelper.VideoServiceCredentialsListener
            public final void onResponse(String str, String str2) {
                TreadlyPrivateStreamHostFragment.lambda$initServiceForHost$4(TreadlyPrivateStreamHostFragment.this, str, str2);
            }
        });
    }

    public static /* synthetic */ void lambda$initServiceForHost$4(final TreadlyPrivateStreamHostFragment treadlyPrivateStreamHostFragment, String str, final String str2) {
        if (str == null || str2 == null) {
            try {
                VideoServiceHelper.createSession(treadlyPrivateStreamHostFragment.userId, new VideoServiceHelper.VideoServiceSessionListener() { // from class: com.treadly.Treadly.UI.TreadlyVideo.PrivateStream.-$$Lambda$TreadlyPrivateStreamHostFragment$-HQ_jwVA_TPDWtPxWJcsr52srjA
                    @Override // com.treadly.Treadly.UI.TreadlyVideo.Data.VideoServiceHelper.VideoServiceSessionListener
                    public final void onResponse(String str3, String str4) {
                        TreadlyPrivateStreamHostFragment.lambda$null$3(TreadlyPrivateStreamHostFragment.this, str2, str3, str4);
                    }
                });
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    public static /* synthetic */ void lambda$null$3(final TreadlyPrivateStreamHostFragment treadlyPrivateStreamHostFragment, final String str, String str2, final String str3) throws JSONException {
        if (str2 == null || str3 != null) {
            VideoServiceHelper.getSessionToken(str3, true, true, new VideoServiceHelper.VideoServiceSessionTokenListener() { // from class: com.treadly.Treadly.UI.TreadlyVideo.PrivateStream.-$$Lambda$TreadlyPrivateStreamHostFragment$VfAoO5coH8pzdiiwWgk_0Gg0BSw
                @Override // com.treadly.Treadly.UI.TreadlyVideo.Data.VideoServiceHelper.VideoServiceSessionTokenListener
                public final void onResponse(String str4, String str5) {
                    TreadlyPrivateStreamHostFragment.lambda$null$2(TreadlyPrivateStreamHostFragment.this, str, str3, str4, str5);
                }
            });
        }
    }

    public static /* synthetic */ void lambda$null$2(TreadlyPrivateStreamHostFragment treadlyPrivateStreamHostFragment, String str, String str2, String str3, String str4) {
        if (str3 == null || str4 == null) {
            mApiKey = str;
            mSessionId = str2;
            mToken = str4;
            treadlyPrivateStreamHostFragment.doConnect();
        }
    }

    private void initServiceForClient() {
        VideoServiceHelper.getCredentials(new VideoServiceHelper.VideoServiceCredentialsListener() { // from class: com.treadly.Treadly.UI.TreadlyVideo.PrivateStream.-$$Lambda$TreadlyPrivateStreamHostFragment$Gd6I0IJD_BEOsbHo_ZOQoveGeNo
            @Override // com.treadly.Treadly.UI.TreadlyVideo.Data.VideoServiceHelper.VideoServiceCredentialsListener
            public final void onResponse(String str, String str2) {
                TreadlyPrivateStreamHostFragment.lambda$initServiceForClient$7(TreadlyPrivateStreamHostFragment.this, str, str2);
            }
        });
    }

    public static /* synthetic */ void lambda$initServiceForClient$7(final TreadlyPrivateStreamHostFragment treadlyPrivateStreamHostFragment, String str, final String str2) {
        if (str != null) {
            Log.e(TAG, "getCredentials: " + str);
            return;
        }
        VideoServiceHelper.getSessionInfoByUser(treadlyPrivateStreamHostFragment.userId, new VideoServiceHelper.VideoServiceSessionInfoListener() { // from class: com.treadly.Treadly.UI.TreadlyVideo.PrivateStream.-$$Lambda$TreadlyPrivateStreamHostFragment$A09M53yjgYYUqvtm1iHwj0EaQpI
            @Override // com.treadly.Treadly.UI.TreadlyVideo.Data.VideoServiceHelper.VideoServiceSessionInfoListener
            public final void onResponse(String str3, VideoServiceSessionInfo videoServiceSessionInfo) {
                TreadlyPrivateStreamHostFragment.lambda$null$6(TreadlyPrivateStreamHostFragment.this, str2, str3, videoServiceSessionInfo);
            }
        });
    }

    public static /* synthetic */ void lambda$null$6(final TreadlyPrivateStreamHostFragment treadlyPrivateStreamHostFragment, final String str, String str2, final VideoServiceSessionInfo videoServiceSessionInfo) {
        if (str2 != null) {
            Log.e(TAG, "getSessionInfoByUser: " + str2);
            return;
        }
        try {
            VideoServiceHelper.getSessionToken(videoServiceSessionInfo.sessionId, true, false, new VideoServiceHelper.VideoServiceSessionTokenListener() { // from class: com.treadly.Treadly.UI.TreadlyVideo.PrivateStream.-$$Lambda$TreadlyPrivateStreamHostFragment$sDRKIIGKiuighHBZMlrl4EH5p_M
                @Override // com.treadly.Treadly.UI.TreadlyVideo.Data.VideoServiceHelper.VideoServiceSessionTokenListener
                public final void onResponse(String str3, String str4) {
                    TreadlyPrivateStreamHostFragment.lambda$null$5(TreadlyPrivateStreamHostFragment.this, str, videoServiceSessionInfo, str3, str4);
                }
            });
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public static /* synthetic */ void lambda$null$5(TreadlyPrivateStreamHostFragment treadlyPrivateStreamHostFragment, String str, VideoServiceSessionInfo videoServiceSessionInfo, String str2, String str3) {
        if (str2 != null) {
            Log.e(TAG, "getSessionToken: " + str2);
            return;
        }
        mApiKey = str;
        mSessionId = videoServiceSessionInfo.sessionId;
        mToken = str3;
        treadlyPrivateStreamHostFragment.doConnect();
    }

    private void initMenuButtons(View view) {
        this.friendInviteButton = (ImageButton) view.findViewById(R.id.friend_invite_button);
        this.friendInviteButton.setOnClickListener(new View.OnClickListener() { // from class: com.treadly.Treadly.UI.TreadlyVideo.PrivateStream.-$$Lambda$TreadlyPrivateStreamHostFragment$YPv6eRgYpy1WYEbHNfmD89R147k
            @Override // android.view.View.OnClickListener
            public final void onClick(View view2) {
                TreadlyPrivateStreamHostFragment.this.handleMenuClick();
            }
        });
        this.recordButton = (ImageButton) view.findViewById(R.id.record_button);
        this.recordButton.setOnClickListener(new View.OnClickListener() { // from class: com.treadly.Treadly.UI.TreadlyVideo.PrivateStream.-$$Lambda$TreadlyPrivateStreamHostFragment$jgh71YQTDMGadRiNNAWlz4mJ8X0
            @Override // android.view.View.OnClickListener
            public final void onClick(View view2) {
                TreadlyPrivateStreamHostFragment.this.handleRecordClick();
            }
        });
        this.speakerButton = (ImageButton) view.findViewById(R.id.speaker_button);
        this.audioDialog = new BTAudioDialog(getContext());
        this.speakerButton.setOnClickListener(new View.OnClickListener() { // from class: com.treadly.Treadly.UI.TreadlyVideo.PrivateStream.-$$Lambda$TreadlyPrivateStreamHostFragment$35KtyiBnk5G0yHlPTlVu2Wl2JYg
            @Override // android.view.View.OnClickListener
            public final void onClick(View view2) {
                TreadlyPrivateStreamHostFragment.this.audioDialog.show();
            }
        });
        this.muteAudioButton = (ImageButton) view.findViewById(R.id.mute_audio_button);
        this.muteAudioButton.setOnClickListener(new View.OnClickListener() { // from class: com.treadly.Treadly.UI.TreadlyVideo.PrivateStream.-$$Lambda$TreadlyPrivateStreamHostFragment$Mx2jexaup8ZOFfmUF9cY0A6sUo0
            @Override // android.view.View.OnClickListener
            public final void onClick(View view2) {
                TreadlyPrivateStreamHostFragment.this.onMuteAudioPressed();
            }
        });
        this.switchCameraButton = (ImageButton) view.findViewById(R.id.switch_camera_button);
        this.switchCameraButton.setOnClickListener(new View.OnClickListener() { // from class: com.treadly.Treadly.UI.TreadlyVideo.PrivateStream.-$$Lambda$TreadlyPrivateStreamHostFragment$9BZl92wxH4tm_BtF3dL1QsnpJ3I
            @Override // android.view.View.OnClickListener
            public final void onClick(View view2) {
                TreadlyPrivateStreamHostFragment.this.onToggleCameraPressed();
            }
        });
        this.turnOffCameraButton = (ImageButton) view.findViewById(R.id.turn_off_camera_button);
        this.turnOffCameraButton.setOnClickListener(new View.OnClickListener() { // from class: com.treadly.Treadly.UI.TreadlyVideo.PrivateStream.-$$Lambda$TreadlyPrivateStreamHostFragment$qHUYM3FIN0W36uEBS1MsO5X625E
            @Override // android.view.View.OnClickListener
            public final void onClick(View view2) {
                TreadlyPrivateStreamHostFragment.this.onToggleCameraOnPressed();
            }
        });
        this.endWorkoutButton = (ImageButton) view.findViewById(R.id.end_workout_button);
        this.endWorkoutButton.setOnClickListener(new View.OnClickListener() { // from class: com.treadly.Treadly.UI.TreadlyVideo.PrivateStream.-$$Lambda$TreadlyPrivateStreamHostFragment$cNdsrpFcVXxpXAYjzyy04KhMB1o
            @Override // android.view.View.OnClickListener
            public final void onClick(View view2) {
                TreadlyPrivateStreamHostFragment.lambda$initMenuButtons$14(TreadlyPrivateStreamHostFragment.this, view2);
            }
        });
    }

    public static /* synthetic */ void lambda$initMenuButtons$14(TreadlyPrivateStreamHostFragment treadlyPrivateStreamHostFragment, View view) {
        treadlyPrivateStreamHostFragment.endWorkoutSelected = true;
        treadlyPrivateStreamHostFragment.displayEndWorkoutConfirmation();
    }

    private void setStartState(TreadlyConnectStartState treadlyConnectStartState) {
        this.startState = treadlyConnectStartState;
        if (this.sliderFragment != null) {
            this.sliderFragment.setStartState(treadlyConnectStartState);
        }
    }

    private void startPublisherPreview() {
        if (audioCapturer == null) {
            audioCapturer = new TreadlyVideoAudioCapturer(getContext());
            AudioDeviceManager.setAudioDevice(audioCapturer);
        }
        Publisher.Builder builder = new Publisher.Builder(getContext());
        builder.name(getPublisherName());
        builder.frameRate(Publisher.CameraCaptureFrameRate.FPS_30);
        builder.resolution(Publisher.CameraCaptureResolution.HIGH);
        if (cameraCapturer == null) {
            cameraCapturer = new TreadlyVideoCameraCapturer(getContext(), Publisher.CameraCaptureResolution.HIGH, Publisher.CameraCaptureFrameRate.FPS_30);
            builder.capturer((BaseVideoCapturer) cameraCapturer);
        }
        this.mPublisher = builder.build();
        this.mPublisher.setPublisherListener(this.publisherListener);
        this.mPublisher.setStyle(BaseVideoRenderer.STYLE_VIDEO_SCALE, BaseVideoRenderer.STYLE_VIDEO_FILL);
        this.mPublisher.getView().setId(R.id.publisher_view_id);
        this.mPublisher.startPreview();
    }

    private void bringButtonsToFront() {
        this.friendInviteButton.bringToFront();
        this.recordButton.bringToFront();
        this.speakerButton.bringToFront();
        this.muteAudioButton.bringToFront();
        this.switchCameraButton.bringToFront();
        this.turnOffCameraButton.bringToFront();
        this.statsButton.bringToFront();
        this.endWorkoutButton.bringToFront();
        this.sliderFrameLayout.bringToFront();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* renamed from: com.treadly.Treadly.UI.TreadlyVideo.PrivateStream.TreadlyPrivateStreamHostFragment$4  reason: invalid class name */
    /* loaded from: classes2.dex */
    public class AnonymousClass4 implements Session.SessionListener {
        AnonymousClass4() {
        }

        @Override // com.opentok.android.Session.SessionListener
        public void onConnected(Session session) {
            Log.i(TreadlyPrivateStreamHostFragment.TAG, "Session Connected: " + TreadlyPrivateStreamHostFragment.mSessionId);
            ActivityUtil.runOnUiThread(TreadlyPrivateStreamHostFragment.this.getActivity(), new Runnable() { // from class: com.treadly.Treadly.UI.TreadlyVideo.PrivateStream.-$$Lambda$TreadlyPrivateStreamHostFragment$4$13pMAWalrsdnJv_5TBd43CZ7HIg
                @Override // java.lang.Runnable
                public final void run() {
                    TreadlyPrivateStreamHostFragment.AnonymousClass4.lambda$onConnected$0(TreadlyPrivateStreamHostFragment.AnonymousClass4.this);
                }
            });
        }

        public static /* synthetic */ void lambda$onConnected$0(AnonymousClass4 anonymousClass4) {
            TreadlyPrivateStreamHostFragment.this.doPublish();
            TreadlyPrivateStreamHostFragment.this.isConnected = true;
            TreadlyPrivateStreamHostFragment.this.startBroadcast();
        }

        @Override // com.opentok.android.Session.SessionListener
        public void onDisconnected(Session session) {
            Log.i(TreadlyPrivateStreamHostFragment.TAG, "Session Disconnected");
            TreadlyPrivateStreamHostFragment.this.isConnected = false;
            TreadlyPrivateStreamHostFragment.this.mSession = null;
        }

        @Override // com.opentok.android.Session.SessionListener
        public void onStreamReceived(Session session, Stream stream) {
            Log.i(TreadlyPrivateStreamHostFragment.TAG, "Stream Received");
            TreadlyPrivateStreamHostFragment.this.doSubscribe(stream);
            TreadlyPrivateStreamHostFragment.this.updateLayout();
        }

        @Override // com.opentok.android.Session.SessionListener
        public void onStreamDropped(Session session, Stream stream) {
            Log.i(TreadlyPrivateStreamHostFragment.TAG, "Stream Dropped");
            Iterator it = TreadlyPrivateStreamHostFragment.this.subscribers.iterator();
            while (it.hasNext()) {
                TreadlyPrivateVideoSubscriber treadlyPrivateVideoSubscriber = (TreadlyPrivateVideoSubscriber) it.next();
                if (treadlyPrivateVideoSubscriber.getSubscriber().getStream().getStreamId().equals(stream.getStreamId())) {
                    TreadlyPrivateStreamHostFragment.this.mContainer.removeView(treadlyPrivateVideoSubscriber.getView());
                    treadlyPrivateVideoSubscriber.setView(null);
                    treadlyPrivateVideoSubscriber.dismissPauseView();
                    it.remove();
                }
            }
            for (int i = 0; i < TreadlyPrivateStreamHostFragment.this.subscribers.size(); i++) {
                ((TreadlyPrivateVideoSubscriber) TreadlyPrivateStreamHostFragment.this.subscribers.get(i)).setResId(TreadlyPrivateStreamHostFragment.this.getResIdForSubscriberIndex(i));
            }
            ActivityUtil.runOnUiThread(TreadlyPrivateStreamHostFragment.this.getActivity(), new Runnable() { // from class: com.treadly.Treadly.UI.TreadlyVideo.PrivateStream.-$$Lambda$TreadlyPrivateStreamHostFragment$4$6Ahgb5HC9PAqBYBJ6slrAoWo4sY
                @Override // java.lang.Runnable
                public final void run() {
                    TreadlyPrivateStreamHostFragment.this.updateLayout();
                }
            });
        }

        @Override // com.opentok.android.Session.SessionListener
        public void onError(Session session, OpentokError opentokError) {
            Log.e(TreadlyPrivateStreamHostFragment.TAG, "Session Error: " + opentokError.getMessage());
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* renamed from: com.treadly.Treadly.UI.TreadlyVideo.PrivateStream.TreadlyPrivateStreamHostFragment$5  reason: invalid class name */
    /* loaded from: classes2.dex */
    public class AnonymousClass5 implements PublisherKit.PublisherListener {
        AnonymousClass5() {
        }

        @Override // com.opentok.android.PublisherKit.PublisherListener
        public void onStreamCreated(PublisherKit publisherKit, Stream stream) {
            Log.i(TreadlyPrivateStreamHostFragment.TAG, "Publisher onStreamCreated");
        }

        @Override // com.opentok.android.PublisherKit.PublisherListener
        public void onStreamDestroyed(PublisherKit publisherKit, Stream stream) {
            Log.i(TreadlyPrivateStreamHostFragment.TAG, "Publisher onStreamDestroyed");
            Iterator it = TreadlyPrivateStreamHostFragment.this.subscribers.iterator();
            while (it.hasNext()) {
                TreadlyPrivateStreamHostFragment.this.mContainer.removeView(((TreadlyPrivateVideoSubscriber) it.next()).getView());
            }
            TreadlyPrivateStreamHostFragment.this.subscribers.removeAll(TreadlyPrivateStreamHostFragment.this.subscribers);
            ActivityUtil.runOnUiThread(TreadlyPrivateStreamHostFragment.this.getActivity(), new Runnable() { // from class: com.treadly.Treadly.UI.TreadlyVideo.PrivateStream.-$$Lambda$TreadlyPrivateStreamHostFragment$5$Uh5daKBBdnxCyb7obHSwtEO-rwM
                @Override // java.lang.Runnable
                public final void run() {
                    TreadlyPrivateStreamHostFragment.this.updateLayout();
                }
            });
        }

        @Override // com.opentok.android.PublisherKit.PublisherListener
        public void onError(PublisherKit publisherKit, OpentokError opentokError) {
            Log.e(TreadlyPrivateStreamHostFragment.TAG, "Publisher error: " + opentokError.getMessage());
        }
    }

    private void disconnectSession() {
        if (this.mSession == null) {
            return;
        }
        if (this.subscribers.size() > 0) {
            Iterator<TreadlyPrivateVideoSubscriber> it = this.subscribers.iterator();
            while (it.hasNext()) {
                TreadlyPrivateVideoSubscriber next = it.next();
                if (next.getSubscriber() != null) {
                    this.mSession.unsubscribe(next.getSubscriber());
                    next.getSubscriber().destroy();
                }
                it.remove();
            }
        }
        if (this.mPublisher != null) {
            this.mSession.unpublish(this.mPublisher);
            this.mContainer.removeView(this.mPublisher.getView());
            this.mPublisher.destroy();
            this.mPublisher = null;
        }
        this.mSession.disconnect();
    }

    private void getUserInfo() {
        this.currentUserId = TreadlyServiceManager.getInstance().getUserId();
        TreadlyServiceManager.getInstance().getUserProfileInfo(this.currentUserId, new TreadlyServiceResponseEventAdapter() { // from class: com.treadly.Treadly.UI.TreadlyVideo.PrivateStream.TreadlyPrivateStreamHostFragment.6
            @Override // com.treadly.Treadly.Data.Listeners.TreadlyServiceResponseEventAdapter, com.treadly.Treadly.Data.Listeners.TreadlyServiceResponseEventListener
            public void onUserProfile(String str, UserProfileInfo userProfileInfo) {
                if (str != null || userProfileInfo == null) {
                    return;
                }
                TreadlyPrivateStreamHostFragment.this.treadlyContent.dailyCaloriesGoal = userProfileInfo.caloriesGoal();
            }
        });
    }

    private void getFriendsInfo() {
        try {
            TreadlyServiceManager.getInstance().getFriendsInfo(new TreadlyServiceResponseEventAdapter() { // from class: com.treadly.Treadly.UI.TreadlyVideo.PrivateStream.TreadlyPrivateStreamHostFragment.7
                @Override // com.treadly.Treadly.Data.Listeners.TreadlyServiceResponseEventAdapter, com.treadly.Treadly.Data.Listeners.TreadlyServiceResponseEventListener
                public void onUserFriendsInfo(String str, List<UserInfo> list) throws JSONException {
                    if (list != null) {
                        TreadlyPrivateStreamHostFragment.this.currentFriends = list;
                    }
                }
            });
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void doConnect() {
        Looper.prepare();
        Session.Builder sessionOptions = new Session.Builder(getContext(), mApiKey, mSessionId).sessionOptions(new Session.SessionOptions() { // from class: com.treadly.Treadly.UI.TreadlyVideo.PrivateStream.TreadlyPrivateStreamHostFragment.8
            @Override // com.opentok.android.Session.SessionOptions
            public boolean useTextureViews() {
                return true;
            }
        });
        Looper.loop();
        this.mSession = sessionOptions.build();
        this.mSession.setSessionListener(this.sessionListener);
        this.mSession.connect(mToken);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void doDisconnect() {
        if (this.isConnected) {
            this.isConnected = false;
            this.mSession.disconnect();
            FragmentActivity activity = getActivity();
            if (activity != null) {
                activity.getSupportFragmentManager().popBackStack();
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void doPublish() {
        this.mSession.publish(this.mPublisher);
        if (this.pendingUserInvite != null) {
            handleInviteFriend(this.pendingUserInvite);
        }
        initMqttConnection();
    }

    private void initMqttConnection() {
        if (mSessionId.isEmpty()) {
            return;
        }
        TreadlyEventManager.getInstance().addVideoPrivateDelegate(this.videoPrivateEventListener);
        TreadlyEventManager.getInstance().joinVideoPrivate(mSessionId);
        if (TreadlyClientLib.shared.isDeviceConnected()) {
            TreadlyEventManager.getInstance().sendVideoPrivateTreadmillConnected(mSessionId);
        } else {
            TreadlyEventManager.getInstance().sendVideoPrivateTreadmillNotConnected(mSessionId);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void doSubscribe(Stream stream) {
        Log.d(TAG, "do subscriber");
        final Subscriber build = new Subscriber.Builder(getContext(), stream).build();
        String userIdFromSubscriber = getUserIdFromSubscriber(build);
        if (userIdFromSubscriber != null) {
            final TreadlyPrivateVideoSubscriber subscriberById = getSubscriberById(userIdFromSubscriber);
            if (subscriberById != null) {
                Log.d(TAG, "Found user state, Updating");
                ActivityUtil.runOnUiThread(getActivity(), new Runnable() { // from class: com.treadly.Treadly.UI.TreadlyVideo.PrivateStream.-$$Lambda$TreadlyPrivateStreamHostFragment$l0VqjnL4TUnY2AuiXAoBq64CDbg
                    @Override // java.lang.Runnable
                    public final void run() {
                        TreadlyPrivateStreamHostFragment.lambda$doSubscribe$15(TreadlyPrivateStreamHostFragment.this, subscriberById, build);
                    }
                });
            } else {
                Log.d(TAG, "User state not found, creating new subscriber");
                final TreadlyPrivateVideoSubscriber treadlyPrivateVideoSubscriber = new TreadlyPrivateVideoSubscriber(userIdFromSubscriber, build, true);
                this.subscribers.add(treadlyPrivateVideoSubscriber);
                ActivityUtil.runOnUiThread(getActivity(), new Runnable() { // from class: com.treadly.Treadly.UI.TreadlyVideo.PrivateStream.-$$Lambda$TreadlyPrivateStreamHostFragment$Uciw_1ZDDJMJRCg7AawAyCiYVQ8
                    @Override // java.lang.Runnable
                    public final void run() {
                        TreadlyPrivateStreamHostFragment.this.mContainer.addView(treadlyPrivateVideoSubscriber.getView());
                    }
                });
            }
        }
        this.mSession.subscribe(build);
    }

    public static /* synthetic */ void lambda$doSubscribe$15(TreadlyPrivateStreamHostFragment treadlyPrivateStreamHostFragment, TreadlyPrivateVideoSubscriber treadlyPrivateVideoSubscriber, Subscriber subscriber) {
        treadlyPrivateVideoSubscriber.setSubscriber(subscriber);
        treadlyPrivateStreamHostFragment.mContainer.addView(treadlyPrivateVideoSubscriber.getView());
        treadlyPrivateStreamHostFragment.updateLayout();
    }

    private void cleanupSubscriber(TreadlyPrivateVideoSubscriber treadlyPrivateVideoSubscriber, Iterator<TreadlyPrivateVideoSubscriber> it) {
        Log.d(TAG, "cleanupSubscriber");
        this.mContainer.removeView(treadlyPrivateVideoSubscriber.getView());
        treadlyPrivateVideoSubscriber.dismissPauseView();
        updateLayout();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void startBroadcast() {
        try {
            VideoServiceHelper.startSessionBroadcast(mSessionId, null, null, null, new VideoServiceHelper.VideoStartSessionListener() { // from class: com.treadly.Treadly.UI.TreadlyVideo.PrivateStream.TreadlyPrivateStreamHostFragment.9
                @Override // com.treadly.Treadly.UI.TreadlyVideo.Data.VideoServiceHelper.VideoStartSessionListener
                public void onResponse(String str, String str2, String str3) {
                    TreadlyPrivateStreamHostFragment.this.mBroadcastId = str2;
                }
            });
        } catch (JSONException e) {
            Log.e(TAG, "startBroadcast error: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void handleMenuClick() {
        onInviteButtonPressed();
    }

    private void onInviteButtonPressed() {
        if (mSessionId.isEmpty()) {
            return;
        }
        ArrayList arrayList = new ArrayList();
        if (this.currentFriends.size() > 0) {
            for (UserInfo userInfo : this.currentFriends) {
                if ((getUserState(userInfo.id) == null) & (this.inviteTimers.get(userInfo.id) == null)) {
                    arrayList.add(userInfo);
                }
            }
        }
        TreadlyVideoInviteFriend treadlyVideoInviteFriend = new TreadlyVideoInviteFriend();
        treadlyVideoInviteFriend.setFriendsList(arrayList);
        treadlyVideoInviteFriend.setOnClickListener(new TreadlyVideoInviteListener() { // from class: com.treadly.Treadly.UI.TreadlyVideo.PrivateStream.-$$Lambda$TreadlyPrivateStreamHostFragment$JM13gtnB-bN9PLAwKHinLxb54DY
            @Override // com.treadly.Treadly.Data.Listeners.TreadlyVideoInviteListener
            public final void onResponse(UserInfo userInfo2) {
                TreadlyPrivateStreamHostFragment.this.handleInviteFriend(userInfo2);
            }
        });
        try {
            treadlyVideoInviteFriend.show(getActivity().getSupportFragmentManager(), "videoInviteFriend");
        } catch (NullPointerException unused) {
            Log.e(TAG, "Invite dialog produced null pointer exception");
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void handleInviteFriend(final UserInfo userInfo) {
        InviteServiceHelper.requestInvite(mSessionId, this.userId, userInfo.id, new InviteServiceHelper.InviteServiceHelperInviteListener() { // from class: com.treadly.Treadly.UI.TreadlyVideo.PrivateStream.-$$Lambda$TreadlyPrivateStreamHostFragment$YPXMlqq2uyco66DI1UtPgXcH7g4
            @Override // com.treadly.Treadly.UI.TreadlyVideo.Data.InviteServiceHelper.InviteServiceHelperInviteListener
            public final void onResponse(String str) {
                TreadlyPrivateStreamHostFragment.lambda$handleInviteFriend$18(TreadlyPrivateStreamHostFragment.this, userInfo, str);
            }
        });
    }

    public static /* synthetic */ void lambda$handleInviteFriend$18(TreadlyPrivateStreamHostFragment treadlyPrivateStreamHostFragment, UserInfo userInfo, String str) {
        if (str == null) {
            Log.d(TAG, "Successfully Invited : " + userInfo.id);
            treadlyPrivateStreamHostFragment.createInviteTimeout(treadlyPrivateStreamHostFragment.userId, userInfo.id);
            return;
        }
        Log.e(TAG, str);
    }

    private void createInviteTimeout(final String str, final String str2) {
        if (this.inviteTimers.get(str2) != null) {
            try {
                ((Timer) Objects.requireNonNull(this.inviteTimers.get(str2))).cancel();
            } catch (NullPointerException unused) {
                Log.e(TAG, "inviteTimer with " + str2 + " has null pointer");
            }
        }
        Timer timer = new Timer();
        timer.schedule(new TimerTask() { // from class: com.treadly.Treadly.UI.TreadlyVideo.PrivateStream.TreadlyPrivateStreamHostFragment.10
            @Override // java.util.TimerTask, java.lang.Runnable
            public void run() {
                TreadlyPrivateStreamHostFragment.this.inviteTimedout(str, str2);
            }
        }, 20000L);
        this.inviteTimers.put(str2, timer);
        updateControls();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void inviteTimedout(String str, String str2) {
        Log.i(TAG, "Invite timed out for: " + str2);
        UserVideoPrivateStateInfo userState = getUserState(str2);
        if (userState != null && userState.status.equals("connecting")) {
            InviteServiceHelper.deleteInvite(str, str2, new InviteServiceHelper.InviteServiceHelperInviteListener() { // from class: com.treadly.Treadly.UI.TreadlyVideo.PrivateStream.-$$Lambda$TreadlyPrivateStreamHostFragment$C6n2KJt_b2ytpPN4JppEaXcOrWc
                @Override // com.treadly.Treadly.UI.TreadlyVideo.Data.InviteServiceHelper.InviteServiceHelperInviteListener
                public final void onResponse(String str3) {
                    TreadlyPrivateStreamHostFragment.lambda$inviteTimedout$19(str3);
                }
            });
        }
        this.inviteTimers.remove(str2);
        updateControls();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static /* synthetic */ void lambda$inviteTimedout$19(String str) {
        if (str == null) {
            Log.d(TAG, "Successfully deleted invite");
            return;
        }
        Log.e(TAG, "Error deleting invite: " + str);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void handleRecordClick() {
        if (this.isRecording) {
            stopRecording();
        } else {
            startRecording();
        }
    }

    private void startRecording() {
        if (this.isHost) {
            File recordingPath = getRecordingPath();
            if (recordingPath == null) {
                Toast.makeText(getContext(), "Can't store local copy, external storage unavailable", 0).show();
                return;
            }
            Date date = new Date(System.currentTimeMillis());
            File file = new File(recordingPath, getRecordingFilename() + ".mp4");
            this.localFileName = file.getName();
            VideoUploaderManager.shared.storeVideoFileNameAlias(this.localFileName, VideoUploaderManager.getFileName(mSessionId, AppEventsConstants.EVENT_PARAM_VALUE_NO, VideoServiceVideoService.tokbox, date, this.userId));
            cameraCapturer.startVideoRecoding(file);
            this.recordButton.setColorFilter(SupportMenu.CATEGORY_MASK);
            this.isRecording = true;
        }
    }

    private void stopRecording() {
        cameraCapturer.stopVideoRecording();
        this.recordButton.setColorFilter(0);
        this.isRecording = false;
    }

    File getRecordingPath() {
        if (getActivity() != null) {
            return getActivity().getExternalFilesDir(Environment.DIRECTORY_MOVIES);
        }
        return null;
    }

    String getRecordingFilename() {
        return VideoUploaderManager.getFileName(ImagesContract.LOCAL, AppEventsConstants.EVENT_PARAM_VALUE_NO, VideoServiceVideoService.tokbox, new Date(), this.userId);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void onToggleCameraPressed() {
        if (!mSessionId.isEmpty() && this.isConnected) {
            this.mPublisher.cycleCamera();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void onMuteAudioPressed() {
        if (!mSessionId.isEmpty() && this.isConnected) {
            this.mPublisher.setPublishAudio(!this.mPublisher.getPublishAudio());
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void onToggleCameraOnPressed() {
        if (!mSessionId.isEmpty() && this.isConnected) {
            this.mPublisher.setPublishVideo(!this.mPublisher.getPublishVideo());
        }
    }

    private void onAudioButtonPressed() {
        Log.i("Audio", "Button Pressed");
        if (getActivity() == null) {
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void onConnectButtonPressed() {
        switch (this.connectState) {
            case notConnected:
                toDeviceTableController();
                return;
            case connected:
                new AlertDialog.Builder(getActivity()).setTitle("Disconnect").setMessage("Are you sure you want to disconnect?").setNegativeButton("No", (DialogInterface.OnClickListener) null).setPositiveButton("Yes", new DialogInterface.OnClickListener() { // from class: com.treadly.Treadly.UI.TreadlyVideo.PrivateStream.-$$Lambda$TreadlyPrivateStreamHostFragment$8N_WGbrj94DFgq4E9nIk7AE-rSU
                    @Override // android.content.DialogInterface.OnClickListener
                    public final void onClick(DialogInterface dialogInterface, int i) {
                        TreadlyPrivateStreamHostFragment.lambda$onConnectButtonPressed$20(TreadlyPrivateStreamHostFragment.this, dialogInterface, i);
                    }
                }).show();
                return;
            case connecting:
                TreadlyClientLib.shared.disconnect();
                setConnectState(DeviceConnectionStatus.notConnected);
                return;
            default:
                if (TreadlyClientLib.shared.isDeviceConnected()) {
                    return;
                }
                setConnectState(DeviceConnectionStatus.notConnected);
                return;
        }
    }

    public static /* synthetic */ void lambda$onConnectButtonPressed$20(TreadlyPrivateStreamHostFragment treadlyPrivateStreamHostFragment, DialogInterface dialogInterface, int i) {
        TreadlyClientLib.shared.disconnect();
        treadlyPrivateStreamHostFragment.setConnectState(DeviceConnectionStatus.notConnected);
        treadlyPrivateStreamHostFragment.setAuthenticationState(AuthenticationState.unknown);
    }

    private void displayEndWorkoutConfirmation() {
        if (!this.isDeviceConnected || this.treadlyControl.runState == TreadlyRunState.STOPPED || this.treadlyControl.runState == TreadlyRunState.STOPPING) {
            this.isLeaving = true;
            if (getActivity() != null) {
                getActivity().getSupportFragmentManager().popBackStack();
                return;
            }
            return;
        }
        new AlertDialog.Builder(getActivity()).setTitle("endworkout").setMessage("Do you want to stop\nthe treadmill as well?").setPositiveButton("Yes", new DialogInterface.OnClickListener() { // from class: com.treadly.Treadly.UI.TreadlyVideo.PrivateStream.-$$Lambda$TreadlyPrivateStreamHostFragment$3e7aDlME7-xnasLCppTyKXflMvQ
            @Override // android.content.DialogInterface.OnClickListener
            public final void onClick(DialogInterface dialogInterface, int i) {
                TreadlyPrivateStreamHostFragment.lambda$displayEndWorkoutConfirmation$21(TreadlyPrivateStreamHostFragment.this, dialogInterface, i);
            }
        }).setNegativeButton("No", new DialogInterface.OnClickListener() { // from class: com.treadly.Treadly.UI.TreadlyVideo.PrivateStream.-$$Lambda$TreadlyPrivateStreamHostFragment$O1FN-tQUhY-rJCm_0rWcjh9PmX8
            @Override // android.content.DialogInterface.OnClickListener
            public final void onClick(DialogInterface dialogInterface, int i) {
                TreadlyPrivateStreamHostFragment.lambda$displayEndWorkoutConfirmation$22(TreadlyPrivateStreamHostFragment.this, dialogInterface, i);
            }
        }).setNeutralButton("Dismiss", new DialogInterface.OnClickListener() { // from class: com.treadly.Treadly.UI.TreadlyVideo.PrivateStream.-$$Lambda$TreadlyPrivateStreamHostFragment$X8t_D4NDYjNofRoz1VfTYJ31vW0
            @Override // android.content.DialogInterface.OnClickListener
            public final void onClick(DialogInterface dialogInterface, int i) {
                TreadlyPrivateStreamHostFragment.lambda$displayEndWorkoutConfirmation$23(TreadlyPrivateStreamHostFragment.this, dialogInterface, i);
            }
        }).show();
    }

    public static /* synthetic */ void lambda$displayEndWorkoutConfirmation$21(TreadlyPrivateStreamHostFragment treadlyPrivateStreamHostFragment, DialogInterface dialogInterface, int i) {
        treadlyPrivateStreamHostFragment.treadlyControl.stopDevice();
        treadlyPrivateStreamHostFragment.isLeaving = true;
        if (treadlyPrivateStreamHostFragment.getActivity() != null) {
            treadlyPrivateStreamHostFragment.getActivity().getSupportFragmentManager().popBackStack();
        }
    }

    public static /* synthetic */ void lambda$displayEndWorkoutConfirmation$22(TreadlyPrivateStreamHostFragment treadlyPrivateStreamHostFragment, DialogInterface dialogInterface, int i) {
        treadlyPrivateStreamHostFragment.isLeaving = true;
        if (treadlyPrivateStreamHostFragment.getActivity() != null) {
            treadlyPrivateStreamHostFragment.getActivity().getSupportFragmentManager().popBackStack();
        }
    }

    public static /* synthetic */ void lambda$displayEndWorkoutConfirmation$23(TreadlyPrivateStreamHostFragment treadlyPrivateStreamHostFragment, DialogInterface dialogInterface, int i) {
        if (treadlyPrivateStreamHostFragment.getActivity() != null) {
            treadlyPrivateStreamHostFragment.getActivity().getSupportFragmentManager().popBackStack();
            return;
        }
        treadlyPrivateStreamHostFragment.isLeaving = false;
        treadlyPrivateStreamHostFragment.endWorkoutSelected = false;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void sendCurrentUserStats(TreadlyControlContent treadlyControlContent) {
        if (this.isConnected) {
            VideoServiceUserStatsInfo videoServiceUserStatsInfo = new VideoServiceUserStatsInfo(TreadlyServiceManager.getInstance().getUserId(), TreadlyServiceManager.getInstance().getName(), RunningSessionManager.getInstance().calories, treadlyControlContent.walkDistance, treadlyControlContent.stepsCount, treadlyControlContent.speedValue, treadlyControlContent.averageSpeed, treadlyControlContent.speedDataSet, treadlyControlContent.speedUnits, treadlyControlContent.elapsedTime);
            this.treadlyStatsController.updateRunSessionInfo(videoServiceUserStatsInfo);
            TreadlyEventManager.getInstance().sendVideoPrivateCurrentUserStats(mSessionId, videoServiceUserStatsInfo);
        }
    }

    private void toDeviceTableController() {
        if (getActivity() == null) {
            return;
        }
        TreadlyDeviceConnectStatusFragment treadlyDeviceConnectStatusFragment = new TreadlyDeviceConnectStatusFragment();
        treadlyDeviceConnectStatusFragment.listener = new TreadlyDeviceConnectFragment.TreadlyDeviceConnectConnectFragmentEventListener() { // from class: com.treadly.Treadly.UI.TreadlyVideo.PrivateStream.TreadlyPrivateStreamHostFragment.11
            @Override // com.treadly.Treadly.UI.TreadlyConnect.TreadlyDeviceConnectFragment.TreadlyDeviceConnectConnectFragmentEventListener
            public void onConnectDevice(DeviceInfo deviceInfo) {
                TreadlyPrivateStreamHostFragment.this.setConnectState(DeviceConnectionStatus.connected);
            }
        };
        addFragmentToStack(treadlyDeviceConnectStatusFragment, TreadlyDeviceConnectStatusFragment.TAG);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* renamed from: com.treadly.Treadly.UI.TreadlyVideo.PrivateStream.TreadlyPrivateStreamHostFragment$12  reason: invalid class name */
    /* loaded from: classes2.dex */
    public class AnonymousClass12 implements TreadlyVideoPrivateEventDelegate {
        AnonymousClass12() {
        }

        @Override // com.treadly.Treadly.Data.Delegates.TreadlyVideoPrivateEventDelegate
        public void onUserJoinVideoPrivateEvent(String str, UserInfo userInfo) {
            TreadlyPrivateStreamHostFragment.this.treadlyStatsController.addFriendStats(userInfo);
            if (TreadlyPrivateStreamHostFragment.this.isHost) {
                TreadlyPrivateStreamHostFragment.this.treadlyStatsController.handleUserJoined(userInfo);
            }
        }

        @Override // com.treadly.Treadly.Data.Delegates.TreadlyVideoPrivateEventDelegate
        public void onUserLeaveVideoPrivateEvent(String str, UserInfo userInfo) {
            TreadlyPrivateStreamHostFragment.this.treadlyStatsController.removeFriendStats(userInfo.id);
            if (TreadlyPrivateStreamHostFragment.this.isHost) {
                TreadlyPrivateStreamHostFragment.this.treadlyStatsController.handleUserLeft(userInfo);
            }
        }

        @Override // com.treadly.Treadly.Data.Delegates.TreadlyVideoPrivateEventDelegate
        public void onUserPauseVideoPrivateEvent(String str, UserInfo userInfo) {
            final TreadlyPrivateVideoSubscriber subscriber = TreadlyPrivateStreamHostFragment.this.getSubscriber(userInfo.id);
            if (subscriber != null) {
                ActivityUtil.runOnUiThread(TreadlyPrivateStreamHostFragment.this.getActivity(), new Runnable() { // from class: com.treadly.Treadly.UI.TreadlyVideo.PrivateStream.-$$Lambda$TreadlyPrivateStreamHostFragment$12$WxKSsvdz6vkFaHxKzB4X05DJmG8
                    @Override // java.lang.Runnable
                    public final void run() {
                        TreadlyPrivateStreamHostFragment.AnonymousClass12.lambda$onUserPauseVideoPrivateEvent$0(TreadlyPrivateStreamHostFragment.AnonymousClass12.this, subscriber);
                    }
                });
            }
        }

        public static /* synthetic */ void lambda$onUserPauseVideoPrivateEvent$0(AnonymousClass12 anonymousClass12, TreadlyPrivateVideoSubscriber treadlyPrivateVideoSubscriber) {
            treadlyPrivateVideoSubscriber.addPauseView();
            TreadlyPrivateStreamHostFragment.this.updateLayout();
        }

        @Override // com.treadly.Treadly.Data.Delegates.TreadlyVideoPrivateEventDelegate
        public void onUserUnpauseVideoPrivateEvent(String str, UserInfo userInfo) {
            final TreadlyPrivateVideoSubscriber subscriber = TreadlyPrivateStreamHostFragment.this.getSubscriber(userInfo.id);
            if (subscriber != null) {
                ActivityUtil.runOnUiThread(TreadlyPrivateStreamHostFragment.this.getActivity(), new Runnable() { // from class: com.treadly.Treadly.UI.TreadlyVideo.PrivateStream.-$$Lambda$TreadlyPrivateStreamHostFragment$12$LMo7F22wEuiDebDOon_bdVIOsm4
                    @Override // java.lang.Runnable
                    public final void run() {
                        TreadlyPrivateStreamHostFragment.AnonymousClass12.lambda$onUserUnpauseVideoPrivateEvent$1(TreadlyPrivateStreamHostFragment.AnonymousClass12.this, subscriber);
                    }
                });
            }
        }

        public static /* synthetic */ void lambda$onUserUnpauseVideoPrivateEvent$1(AnonymousClass12 anonymousClass12, TreadlyPrivateVideoSubscriber treadlyPrivateVideoSubscriber) {
            treadlyPrivateVideoSubscriber.dismissPauseView();
            TreadlyPrivateStreamHostFragment.this.updateLayout();
        }

        @Override // com.treadly.Treadly.Data.Delegates.TreadlyVideoPrivateEventDelegate
        public void onReceiveVideoPrivateUserStatsEvent(String str, VideoServiceUserStatsInfo videoServiceUserStatsInfo) {
            if (videoServiceUserStatsInfo != null) {
                TreadlyPrivateStreamHostFragment.this.treadlyStatsController.updateFriendRunSession(videoServiceUserStatsInfo);
                if (TreadlyPrivateStreamHostFragment.this.treadlyStatsController.getStudentMode() == TrainerModeState.active) {
                    UsersTrainerModes currentTrainerUserMode = TreadlyPrivateStreamHostFragment.this.treadlyStatsController.getCurrentTrainerUserMode();
                    if (currentTrainerUserMode == null || currentTrainerUserMode.user.id.equals(videoServiceUserStatsInfo.userId)) {
                        if (TreadlyPrivateStreamHostFragment.this.treadlyControl.speedUnits == videoServiceUserStatsInfo.speedUnits) {
                            TreadlyPrivateStreamHostFragment.this.treadlyControl.setSpeed(videoServiceUserStatsInfo.speed);
                            return;
                        } else if (TreadlyPrivateStreamHostFragment.this.treadlyControl.speedUnits != DistanceUnits.MI || videoServiceUserStatsInfo.speedUnits != DistanceUnits.KM) {
                            if (TreadlyPrivateStreamHostFragment.this.treadlyControl.speedUnits == DistanceUnits.KM && videoServiceUserStatsInfo.speedUnits == DistanceUnits.MI) {
                                TreadlyPrivateStreamHostFragment.this.treadlyControl.setSpeed(videoServiceUserStatsInfo.speed * TreadlyPrivateStreamHostFragment.miToKm);
                                return;
                            }
                            return;
                        } else {
                            TreadlyPrivateStreamHostFragment.this.treadlyControl.setSpeed(videoServiceUserStatsInfo.speed * TreadlyPrivateStreamHostFragment.kmToMi);
                            return;
                        }
                    }
                    return;
                }
                return;
            }
            Log.e("Private::onReceive", "stats is null");
        }

        @Override // com.treadly.Treadly.Data.Delegates.TreadlyVideoPrivateEventDelegate
        public void onReceiveVideoPrivateTrainerModeRequest(String str, UserTrainerMode userTrainerMode) {
            Log.d(TreadlyPrivateStreamHostFragment.TAG, String.format("Received Trainer Mode Request: from %s", userTrainerMode.user.name));
            TreadlyPrivateStreamHostFragment.this.treadlyStatsController.handleTrainerModeRequest(userTrainerMode);
        }

        @Override // com.treadly.Treadly.Data.Delegates.TreadlyVideoPrivateEventDelegate
        public void onReceiveVideoPrivateTrainerModeResponse(String str, UserTrainerMode userTrainerMode) {
            Log.d(TreadlyPrivateStreamHostFragment.TAG, String.format("Received Trainer Mode Response: from %s", userTrainerMode.user.name));
            TreadlyPrivateStreamHostFragment.this.treadlyStatsController.handleTrainerModeResponse(userTrainerMode);
        }

        @Override // com.treadly.Treadly.Data.Delegates.TreadlyVideoPrivateEventDelegate
        public void onReceiveVideoPrivateStudentModeRequest(String str, UserTrainerMode userTrainerMode, String str2) {
            Log.d(TreadlyPrivateStreamHostFragment.TAG, String.format("Received Student Mode Request: from %s", userTrainerMode.user.name));
            TreadlyPrivateStreamHostFragment.this.treadlyStatsController.handleStudentModeRequest(str2, userTrainerMode);
        }

        @Override // com.treadly.Treadly.Data.Delegates.TreadlyVideoPrivateEventDelegate
        public void onReceiveVideoPrivateStudentModeResponse(String str, UserTrainerMode userTrainerMode, String str2) {
            Log.d(TreadlyPrivateStreamHostFragment.TAG, String.format("Received Student Mode Request: from %s", userTrainerMode.user));
            TreadlyPrivateStreamHostFragment.this.treadlyStatsController.handleStudentModeResponse(str2, userTrainerMode);
        }

        @Override // com.treadly.Treadly.Data.Delegates.TreadlyVideoPrivateEventDelegate
        public void onReceiveVideoPrivateUsersTrainerModes(String str, List<UsersTrainerModes> list) {
            Log.d(TreadlyPrivateStreamHostFragment.TAG, "Receive video private users trainer modes count: " + list.size());
            TreadlyPrivateStreamHostFragment.this.treadlyStatsController.handleUserTrainerModes(list);
        }

        @Override // com.treadly.Treadly.Data.Delegates.TreadlyVideoPrivateEventDelegate
        public void onReceiveVideoPrivateUserTreadmillConnected(String str, UserInfo userInfo) {
            Log.d(TreadlyPrivateStreamHostFragment.TAG, "onReceiveVideoPrivateUserTreadmillConnected");
            TreadlyPrivateStreamHostFragment.this.treadlyStatsController.updateUserDeviceConnectionState(userInfo.id, true);
        }

        @Override // com.treadly.Treadly.Data.Delegates.TreadlyVideoPrivateEventDelegate
        public void onReceiveVideoPrivateUserTreadmillNotConnected(String str, UserInfo userInfo) {
            Log.d(TreadlyPrivateStreamHostFragment.TAG, "onReceiveVideoPrivateUserTreadmillNotConnected");
            TreadlyPrivateStreamHostFragment.this.treadlyStatsController.updateUserDeviceConnectionState(userInfo.id, false);
        }

        @Override // com.treadly.Treadly.Data.Delegates.TreadlyVideoPrivateEventDelegate
        public void onReceiveVideoPrivateClientForceDisconnectEvent(String str) {
            Log.d(TreadlyPrivateStreamHostFragment.TAG, "onReceiveVideoPrivateClientForceDisconnectEvent");
            if (TreadlyPrivateStreamHostFragment.this.isHost) {
                return;
            }
            TreadlyPrivateStreamHostFragment.this.doDisconnect();
        }

        @Override // com.treadly.Treadly.Data.Delegates.TreadlyVideoPrivateEventDelegate
        public void onReceiveVideoPrivateUsersStateChanged(String str, ArrayList<UserVideoPrivateStateInfo> arrayList) {
            Log.d(TreadlyPrivateStreamHostFragment.TAG, "onReceiveVideoPrivateUsersStateChanged");
            TreadlyPrivateStreamHostFragment.this.syncUserStates(arrayList);
            TreadlyPrivateStreamHostFragment.this.treadlyStatsController.syncUserStates(arrayList);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void setCurrentSpeed(float f, float f2) {
        if (f2 == Utils.DOUBLE_EPSILON) {
            this.currentSpeed = f;
            if (this.sliderFragment != null) {
                this.sliderFragment.setSpeed(f);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public TreadlyPrivateVideoSubscriber getSubscriber(String str) {
        Iterator<TreadlyPrivateVideoSubscriber> it = this.subscribers.iterator();
        while (it.hasNext()) {
            TreadlyPrivateVideoSubscriber next = it.next();
            if (next.getSubscriber() != null && next.getSubscriber().getStream() != null) {
                String[] split = next.getSubscriber().getStream().getName().split("-");
                if (split.length == 2 && str.equals(split[1])) {
                    return next;
                }
            }
        }
        return null;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public int getResIdForSubscriberIndex(int i) {
        TypedArray obtainTypedArray = getResources().obtainTypedArray(R.array.subscriber_view_ids);
        int resourceId = obtainTypedArray.getResourceId(i, 0);
        obtainTypedArray.recycle();
        return resourceId;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void updateLayout() {
        VideoConstrainHelper videoConstrainHelper = new VideoConstrainHelper(R.id.video_container);
        int size = this.subscribers.size();
        if (size == 0) {
            Log.d(TAG, "updateLayout(): size==0");
            videoConstrainHelper.layoutViewFullScreen(R.id.publisher_view_id);
            updateControls();
        } else {
            if (size == 1) {
                Log.d(TAG, "updateLayout(): size==1");
                videoConstrainHelper.layoutViewAboveView(R.id.publisher_view_id, getResIdForSubscriberIndex(0));
                videoConstrainHelper.layoutViewWithTopBound(R.id.publisher_view_id, R.id.video_container);
                videoConstrainHelper.layoutViewWithBottomBound(getResIdForSubscriberIndex(0), R.id.video_container);
                videoConstrainHelper.layoutViewAllContainerWide(R.id.publisher_view_id, R.id.video_container);
                videoConstrainHelper.layoutViewAllContainerWide(getResIdForSubscriberIndex(0), R.id.video_container);
                videoConstrainHelper.layoutViewHeightPercent(R.id.publisher_view_id, 0.5f);
                videoConstrainHelper.layoutViewHeightPercent(getResIdForSubscriberIndex(0), 0.5f);
            } else if (size % 2 == 0) {
                Log.d(TAG, "updateLayout(): size % 2");
                float f = 1.0f / ((size / 2) + 1);
                videoConstrainHelper.layoutViewWithTopBound(R.id.publisher_view_id, R.id.video_container);
                videoConstrainHelper.layoutViewAllContainerWide(R.id.publisher_view_id, R.id.video_container);
                videoConstrainHelper.layoutViewHeightPercent(R.id.publisher_view_id, f);
                for (int i = 0; i < size; i += 2) {
                    if (i == 0) {
                        videoConstrainHelper.layoutViewAboveView(R.id.publisher_view_id, getResIdForSubscriberIndex(i));
                        videoConstrainHelper.layoutViewAboveView(R.id.publisher_view_id, getResIdForSubscriberIndex(i + 1));
                    } else {
                        videoConstrainHelper.layoutViewAboveView(getResIdForSubscriberIndex(i - 2), getResIdForSubscriberIndex(i));
                        videoConstrainHelper.layoutViewAboveView(getResIdForSubscriberIndex(i - 1), getResIdForSubscriberIndex(i + 1));
                    }
                    int i2 = i + 1;
                    videoConstrainHelper.layoutTwoViewsOccupyingAllRow(getResIdForSubscriberIndex(i), getResIdForSubscriberIndex(i2));
                    videoConstrainHelper.layoutViewHeightPercent(getResIdForSubscriberIndex(i), f);
                    videoConstrainHelper.layoutViewHeightPercent(getResIdForSubscriberIndex(i2), f);
                }
                videoConstrainHelper.layoutViewWithBottomBound(getResIdForSubscriberIndex(size - 2), R.id.video_container);
                videoConstrainHelper.layoutViewWithBottomBound(getResIdForSubscriberIndex(size - 1), R.id.video_container);
            } else {
                Log.d(TAG, "updateLayout(): size odd??");
                float f2 = 1.0f / ((size + 1) / 2);
                videoConstrainHelper.layoutViewWithTopBound(R.id.publisher_view_id, R.id.video_container);
                videoConstrainHelper.layoutViewHeightPercent(R.id.publisher_view_id, f2);
                videoConstrainHelper.layoutViewWithTopBound(getResIdForSubscriberIndex(0), R.id.video_container);
                videoConstrainHelper.layoutViewHeightPercent(getResIdForSubscriberIndex(0), f2);
                videoConstrainHelper.layoutTwoViewsOccupyingAllRow(R.id.publisher_view_id, getResIdForSubscriberIndex(0));
                for (int i3 = 1; i3 < size; i3 += 2) {
                    if (i3 == 1) {
                        videoConstrainHelper.layoutViewAboveView(R.id.publisher_view_id, getResIdForSubscriberIndex(i3));
                        videoConstrainHelper.layoutViewHeightPercent(R.id.publisher_view_id, f2);
                        videoConstrainHelper.layoutViewAboveView(getResIdForSubscriberIndex(0), getResIdForSubscriberIndex(i3 + 1));
                        videoConstrainHelper.layoutViewHeightPercent(getResIdForSubscriberIndex(0), f2);
                    } else {
                        int i4 = i3 - 2;
                        videoConstrainHelper.layoutViewAboveView(getResIdForSubscriberIndex(i4), getResIdForSubscriberIndex(i3));
                        videoConstrainHelper.layoutViewHeightPercent(getResIdForSubscriberIndex(i4), f2);
                        int i5 = i3 - 1;
                        videoConstrainHelper.layoutViewAboveView(getResIdForSubscriberIndex(i5), getResIdForSubscriberIndex(i3 + 1));
                        videoConstrainHelper.layoutViewHeightPercent(getResIdForSubscriberIndex(i5), f2);
                    }
                    videoConstrainHelper.layoutTwoViewsOccupyingAllRow(getResIdForSubscriberIndex(i3), getResIdForSubscriberIndex(i3 + 1));
                }
                videoConstrainHelper.layoutViewWithBottomBound(getResIdForSubscriberIndex(size - 2), R.id.video_container);
                videoConstrainHelper.layoutViewWithBottomBound(getResIdForSubscriberIndex(size - 1), R.id.video_container);
            }
        }
        videoConstrainHelper.applyToLayout(this.mContainer, true);
        bringButtonsToFront();
    }

    private void updateControls() {
        this.subscribers.size();
        this.enableRecording = this.subscribers.size() == 0;
    }

    private TreadlyPrivateVideoSubscriber getSubscriberById(String str) {
        Iterator<TreadlyPrivateVideoSubscriber> it = this.subscribers.iterator();
        while (it.hasNext()) {
            TreadlyPrivateVideoSubscriber next = it.next();
            if (next.id.equals(str)) {
                return next;
            }
        }
        return null;
    }

    private UserVideoPrivateStateInfo getUserState(String str) {
        Iterator<UserVideoPrivateStateInfo> it = this.userStates.iterator();
        while (it.hasNext()) {
            UserVideoPrivateStateInfo next = it.next();
            if (next.id.equals(str)) {
                return next;
            }
        }
        return null;
    }

    private String getUserIdFromSubscriber(Subscriber subscriber) {
        String name = subscriber.getStream().getName();
        if (name == null || name.isEmpty()) {
            return null;
        }
        String[] split = name.split("-");
        if (split.length == 2) {
            return split[1];
        }
        return null;
    }

    private String getPublisherName() {
        String userId = TreadlyServiceManager.getInstance().getUserId();
        String sessionName = getSessionName((userId == null || userId.isEmpty()) ? "" : "", this.isHost);
        Log.d(TAG, "sessionName: " + sessionName);
        return sessionName;
    }

    private String getSessionName(String str, boolean z) {
        Object[] objArr = new Object[2];
        objArr[0] = z ? hostStreamKey : clientStreamKey;
        objArr[1] = str;
        return String.format("%s-%s", objArr);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void syncUserStates(ArrayList<UserVideoPrivateStateInfo> arrayList) {
        Log.i(TAG, "Syncing user states");
        String userId = TreadlyServiceManager.getInstance().getUserId();
        int size = this.userStates.size();
        this.userStates = arrayList;
        Iterator<UserVideoPrivateStateInfo> it = arrayList.iterator();
        while (it.hasNext()) {
            UserVideoPrivateStateInfo next = it.next();
            if (!next.id.equals(userId)) {
                TreadlyPrivateVideoSubscriber subscriberById = getSubscriberById(next.id);
                if (subscriberById != null) {
                    Log.i(TAG, "Updating user");
                    Log.i(TAG, "userState: " + next.status);
                    subscriberById.setUserInfo(next);
                } else {
                    Log.i(TAG, "New user");
                    this.subscribers.add(new TreadlyPrivateVideoSubscriber(next, this.isHost));
                }
            }
        }
        Iterator<TreadlyPrivateVideoSubscriber> it2 = this.subscribers.iterator();
        while (it2.hasNext()) {
            TreadlyPrivateVideoSubscriber next2 = it2.next();
            if (getUserState(next2.id) == null) {
                this.mContainer.removeView(next2.getView());
                next2.setView(null);
                next2.dismissPauseView();
                it2.remove();
            }
        }
        for (int i = 0; i < this.subscribers.size(); i++) {
            this.subscribers.get(i).setResId(getResIdForSubscriberIndex(i));
        }
        if (this.userStates.size() != size) {
            ActivityUtil.runOnUiThread(getActivity(), new Runnable() { // from class: com.treadly.Treadly.UI.TreadlyVideo.PrivateStream.-$$Lambda$TreadlyPrivateStreamHostFragment$M9SYpOk95hV_o4x_qkToqfcCJGw
                @Override // java.lang.Runnable
                public final void run() {
                    TreadlyPrivateStreamHostFragment.this.updateLayout();
                }
            });
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void handleDeviceFound(DeviceInfo deviceInfo) {
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

    private boolean checkBleEnabled() {
        return TreadlyClientLib.shared.getBluetoothConnectionSTate() == BluetoothConnectionState.poweredOn;
    }

    private void handleDeviceConnectedState(boolean z) {
        String userId = TreadlyServiceManager.getInstance().getUserId();
        if (userId.isEmpty()) {
            return;
        }
        this.treadlyStatsController.updateUserDeviceConnectionState(userId, z);
        sendTreadmillConnectionStatus(z);
    }

    private void sendTreadmillConnectionStatus(boolean z) {
        if (mSessionId.isEmpty()) {
            return;
        }
        if (z) {
            TreadlyEventManager.getInstance().sendVideoPrivateTreadmillConnected(mSessionId);
        } else {
            TreadlyEventManager.getInstance().sendVideoPrivateTreadmillNotConnected(mSessionId);
        }
    }

    private void autoConnect() {
        Log.d(TAG, "in autoConnect");
        String connectedDeviceName = SharedPreferences.shared.getConnectedDeviceName();
        if (connectedDeviceName == null || connectedDeviceName.isEmpty()) {
            return;
        }
        this.activationTarget = connectedDeviceName;
        this.targetTreadmillName = connectedDeviceName;
        TreadlyClientLib.shared.startScanning(30000L);
        setConnectState(DeviceConnectionStatus.connected);
    }

    private void setConnectButton(DeviceConnectionStatus deviceConnectionStatus) {
        boolean z = this.connectState == DeviceConnectionStatus.connected;
        switch (deviceConnectionStatus) {
            case notConnected:
            case disconnecting:
                this.connectButton.setText(R.string.connect);
                break;
            case connected:
                this.connectButton.setText(R.string.connected);
                break;
            case connecting:
                this.connectButton.setText(R.string.connecting);
                break;
        }
        this.connectButton.setBackgroundResource(z ? R.drawable.border_button_background : R.drawable.border_button_background_dc);
    }

    private void handleConnectionChange(DeviceConnectionEvent deviceConnectionEvent) {
        int i = AnonymousClass16.$SwitchMap$com$treadly$client$lib$sdk$Model$DeviceConnectionStatus[deviceConnectionEvent.getStatus().ordinal()];
        if (i == 4) {
            setConnectState(DeviceConnectionStatus.notConnected);
            setAuthenticationState(AuthenticationState.unknown);
            this.deviceName = null;
            TreadlyLogManager.shared.finishLogging();
            return;
        }
        switch (i) {
            case 1:
                setConnectState(DeviceConnectionStatus.notConnected);
                setAuthenticationState(AuthenticationState.unknown);
                this.deviceName = null;
                TreadlyLogManager.shared.finishLogging();
                if (deviceConnectionEvent.getStatusCode() == ConnectionStatusCode.errorInvalidVersion) {
                    Log.e(TAG, "Error: The version of hte Treadly product is not supported by the app ");
                }
                if (!this.autoConnected) {
                    autoConnect();
                }
                if (deviceConnectionEvent.getDeviceInfo() == null) {
                    return;
                }
                AppActivityManager.shared.handleDeviceConnected();
                return;
            case 2:
                setConnectState(this.connectState);
                this.deviceName = deviceConnectionEvent.getDeviceInfo().getName();
                if (this.deviceName != null && this.deviceName.isEmpty()) {
                    this.deviceName = "";
                }
                TreadlyClientLib.shared.getDeviceComponentList();
                SharedPreferences.shared.storeConnectedDeviceName(this.deviceName != null ? this.deviceName : "");
                this.autoConnected = true;
                return;
            default:
                return;
        }
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
    /* loaded from: classes2.dex */
    public class TreadlyPrivateVideoSubscriber {
        public String id;
        public boolean isHost;
        private TreadlyPrivateStreamPauseView pauseView;
        private Publisher publisher;
        private Subscriber subscriber;
        private View view;
        private UserVideoPrivateStateInfo userInfo = null;
        private int resId = -1;

        public void setSubscriber(Subscriber subscriber) {
            if (subscriber == null) {
                return;
            }
            Log.i("private", "setSubscriber");
            this.subscriber = subscriber;
            setView(this.subscriber.getView());
        }

        public Subscriber getSubscriber() {
            return this.subscriber;
        }

        public void setPublisher(Publisher publisher) {
            this.publisher = publisher;
            setView(this.publisher.getView());
        }

        public Publisher getPublisher() {
            return this.publisher;
        }

        public void setView(View view) {
            if (this.view != view && this.view != null) {
                TreadlyPrivateStreamHostFragment.this.mContainer.removeView(this.view);
            }
            this.view = view;
            if (this.resId <= -1 || this.view == null) {
                return;
            }
            Log.d("private", "setView(): resId=" + this.resId);
            this.view.setId(this.resId);
        }

        public View getView() {
            return this.view;
        }

        public TreadlyPrivateStreamPauseView getPauseView() {
            return this.pauseView;
        }

        public void setUserInfo(UserVideoPrivateStateInfo userVideoPrivateStateInfo) {
            this.userInfo = userVideoPrivateStateInfo;
            updateState();
        }

        public UserVideoPrivateStateInfo getUserInfo() {
            return this.userInfo;
        }

        public void setResId(int i) {
            this.resId = i;
            if (this.view != null) {
                Log.d(TreadlyPrivateStreamHostFragment.TAG, "setResId: resId=" + i);
                this.view.setId(this.resId);
            }
        }

        public TreadlyPrivateVideoSubscriber(String str, boolean z) {
            this.id = str;
            this.isHost = z;
            setView(new View(TreadlyPrivateStreamHostFragment.this.getContext()));
        }

        public TreadlyPrivateVideoSubscriber(UserVideoPrivateStateInfo userVideoPrivateStateInfo, boolean z) {
            this.id = userVideoPrivateStateInfo.id;
            this.isHost = z;
            setView(new View(TreadlyPrivateStreamHostFragment.this.getContext()));
            setUserInfo(userVideoPrivateStateInfo);
        }

        public TreadlyPrivateVideoSubscriber(String str, Subscriber subscriber, boolean z) {
            this.id = str;
            this.isHost = z;
            setSubscriber(subscriber);
        }

        public TreadlyPrivateVideoSubscriber(String str, Publisher publisher, boolean z) {
            this.id = str;
            this.isHost = z;
            setPublisher(publisher);
        }

        private void updateState() {
            if (this.userInfo == null) {
                return;
            }
            Log.d("private", "updating state");
            if (this.userInfo.status.equals("connecting")) {
                Log.i("private", "connecting state");
                ActivityUtil.runOnUiThread(TreadlyPrivateStreamHostFragment.this.getActivity(), new Runnable() { // from class: com.treadly.Treadly.UI.TreadlyVideo.PrivateStream.-$$Lambda$TreadlyPrivateStreamHostFragment$TreadlyPrivateVideoSubscriber$mS6DJWcKQXKw82CSWZcGwWeEOPA
                    @Override // java.lang.Runnable
                    public final void run() {
                        TreadlyPrivateStreamHostFragment.TreadlyPrivateVideoSubscriber.lambda$updateState$0(TreadlyPrivateStreamHostFragment.TreadlyPrivateVideoSubscriber.this);
                    }
                });
            } else if (this.userInfo.status.equals("pause") && this.pauseView == null) {
                Log.i("private", "paused state");
                ActivityUtil.runOnUiThread(TreadlyPrivateStreamHostFragment.this.getActivity(), new Runnable() { // from class: com.treadly.Treadly.UI.TreadlyVideo.PrivateStream.-$$Lambda$TreadlyPrivateStreamHostFragment$TreadlyPrivateVideoSubscriber$beHBjIylzq6WdQQ2bgBEwpedrG8
                    @Override // java.lang.Runnable
                    public final void run() {
                        TreadlyPrivateStreamHostFragment.TreadlyPrivateVideoSubscriber.lambda$updateState$1(TreadlyPrivateStreamHostFragment.TreadlyPrivateVideoSubscriber.this);
                    }
                });
            } else if (this.userInfo.status.equals("active")) {
                Log.i("private", "active state");
                ActivityUtil.runOnUiThread(TreadlyPrivateStreamHostFragment.this.getActivity(), new Runnable() { // from class: com.treadly.Treadly.UI.TreadlyVideo.PrivateStream.-$$Lambda$TreadlyPrivateStreamHostFragment$TreadlyPrivateVideoSubscriber$michVIssidEXFki_K4y5fHsT35s
                    @Override // java.lang.Runnable
                    public final void run() {
                        TreadlyPrivateStreamHostFragment.TreadlyPrivateVideoSubscriber.lambda$updateState$2(TreadlyPrivateStreamHostFragment.TreadlyPrivateVideoSubscriber.this);
                    }
                });
            }
        }

        public static /* synthetic */ void lambda$updateState$0(TreadlyPrivateVideoSubscriber treadlyPrivateVideoSubscriber) {
            treadlyPrivateVideoSubscriber.dismissPauseView();
            TreadlyPrivateStreamHostFragment.this.updateLayout();
        }

        public static /* synthetic */ void lambda$updateState$1(TreadlyPrivateVideoSubscriber treadlyPrivateVideoSubscriber) {
            treadlyPrivateVideoSubscriber.addPauseView();
            TreadlyPrivateStreamHostFragment.this.updateLayout();
        }

        public static /* synthetic */ void lambda$updateState$2(TreadlyPrivateVideoSubscriber treadlyPrivateVideoSubscriber) {
            treadlyPrivateVideoSubscriber.dismissPauseView();
            TreadlyPrivateStreamHostFragment.this.updateLayout();
        }

        public void addPauseView() {
            dismissPauseView();
            if (this.view == null || this.resId == -1) {
                return;
            }
            Log.d("private", "addPauseView resId: " + this.resId);
            this.pauseView = new TreadlyPrivateStreamPauseView(TreadlyPrivateStreamHostFragment.this.getContext());
            this.pauseView.setAvatar(this.userInfo.avatarURL());
            this.pauseView.setId(this.resId);
            TreadlyPrivateStreamHostFragment.this.mContainer.addView(this.pauseView);
            this.pauseView.bringToFront();
        }

        public void dismissPauseView() {
            if (this.pauseView != null) {
                TreadlyPrivateStreamHostFragment.this.mContainer.removeView(this.pauseView);
                this.pauseView = null;
            }
        }
    }
}
