package com.treadly.Treadly.UI.TreadlyVideo.PublicStream.PublicStreamHost;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.media.MediaScannerConnection;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.OrientationEventListener;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatImageButton;
import androidx.fragment.app.FragmentTransaction;
import com.bambuser.broadcaster.BroadcastStatus;
import com.bambuser.broadcaster.Broadcaster;
import com.bambuser.broadcaster.CameraError;
import com.bambuser.broadcaster.ConnectionError;
import com.bambuser.broadcaster.SurfaceViewWithAutoAR;
import com.google.android.gms.common.internal.ImagesContract;
import com.treadly.Treadly.Data.Constants.NetworkConstants;
import com.treadly.Treadly.Data.Delegates.TreadlyVideoBroadcastEventAdapter;
import com.treadly.Treadly.Data.Delegates.TreadlyVideoBroadcastEventDelegate;
import com.treadly.Treadly.Data.Listeners.TreadlyServiceResponseEventAdapter;
import com.treadly.Treadly.Data.Listeners.TreadlyVideoInviteListener;
import com.treadly.Treadly.Data.Managers.RunningSessionManager;
import com.treadly.Treadly.Data.Managers.TreadlyActivationManager;
import com.treadly.Treadly.Data.Managers.TreadlyEventManager;
import com.treadly.Treadly.Data.Managers.TreadlyServiceManager;
import com.treadly.Treadly.Data.Model.StreamPermission;
import com.treadly.Treadly.Data.Model.UserComment;
import com.treadly.Treadly.Data.Model.UserInfo;
import com.treadly.Treadly.Data.Model.VideoLikeInfo;
import com.treadly.Treadly.Data.Model.VideoLikeUserInfo;
import com.treadly.Treadly.MainActivity;
import com.treadly.Treadly.R;
import com.treadly.Treadly.UI.TreadlyConnect.TreadlyConnectFragment;
import com.treadly.Treadly.UI.TreadlyConnect.TreadlyConnectStartState;
import com.treadly.Treadly.UI.TreadlyVideo.Data.BroadcastInviteServiceHelper;
import com.treadly.Treadly.UI.TreadlyVideo.Data.BroadcastViewerServiceHelper;
import com.treadly.Treadly.UI.TreadlyVideo.Data.InviteServiceInviteInfo;
import com.treadly.Treadly.UI.TreadlyVideo.Data.VideoServiceHelper;
import com.treadly.Treadly.UI.TreadlyVideo.Data.VideoServiceUserStatsInfo;
import com.treadly.Treadly.UI.TreadlyVideo.Data.VideoServiceVideoService;
import com.treadly.Treadly.UI.TreadlyVideo.Data.VideoUploaderManager;
import com.treadly.Treadly.UI.TreadlyVideo.PublicStream.Components.HostInfo.TreadlyPublicStreamUserStatsInfoView;
import com.treadly.Treadly.UI.TreadlyVideo.PublicStream.PublicStreamHost.PublicStreamViewers.TreadlyPublicStreamViewersController;
import com.treadly.Treadly.UI.TreadlyVideo.PublicStream.PublicStreamMessagingView.TreadlyPublicStreamMessagingViewController;
import com.treadly.Treadly.UI.TreadlyVideo.TreadlyVideoInviteFriend;
import com.treadly.Treadly.UI.TreadlyVideo.TreadlyVideoSliderFragment;
import com.treadly.Treadly.UI.TreadmillControl.Data.TreadlyControlContent;
import com.treadly.Treadly.UI.TreadmillControl.TreadlyTreadmillController;
import com.treadly.Treadly.UI.Util.ActivityUtil;
import com.treadly.Treadly.UI.Util.BaseClasses.BaseFragment;
import com.treadly.Treadly.UI.Util.OnBackPressedListener;
import com.treadly.client.lib.sdk.Listeners.DeviceConnectionEventListener;
import com.treadly.client.lib.sdk.Model.AuthenticationState;
import com.treadly.client.lib.sdk.Model.ComponentInfo;
import com.treadly.client.lib.sdk.Model.ComponentType;
import com.treadly.client.lib.sdk.Model.DeviceConnectionEvent;
import com.treadly.client.lib.sdk.Model.DeviceConnectionStatus;
import com.treadly.client.lib.sdk.Model.DeviceInfo;
import com.treadly.client.lib.sdk.Model.DeviceStatus;
import com.treadly.client.lib.sdk.Model.DistanceUnits;
import com.treadly.client.lib.sdk.Model.HandrailStatus;
import com.treadly.client.lib.sdk.Model.VersionInfo;
import com.treadly.client.lib.sdk.TreadlyClientLib;
import java.io.File;
import java.io.PrintStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import org.json.JSONException;

/* loaded from: classes2.dex */
public class TreadlyPublicStreamHostFragment extends BaseFragment {
    public static final String TAG = "PublicStreamHostFragment";
    public boolean allowComments;
    public boolean allowVoiceMessage;
    private AppCompatImageButton broadcastButton;
    public Broadcaster broadcaster;
    TreadlyPublicStreamMessagingViewController commentController;
    Display defaultDisplay;
    private String deviceName;
    ImageButton flipCameraButton;
    View hostFragmentView;
    ImageButton inviteFriendsButton;
    private boolean isConnected;
    private Date localFileDate;
    public String localFileName;
    public String localFilePath;
    OrientationEventListener orientationEventListener;
    public SurfaceViewWithAutoAR publicBroadcastSurface;
    public String scheduleId;
    private TreadlyVideoSliderFragment sliderFragment;
    private FrameLayout sliderFrameLayout;
    private Button speedDownButton;
    private Button speedUpButton;
    VideoLikeInfo streamLikeInfo;
    private TreadlyControlContent treadlyContent;
    private TreadlyTreadmillController treadlyControl;
    UserInfo userInfo;
    private TreadlyPublicStreamUserStatsInfoView userStatsInfoView;
    TreadlyPublicStreamViewersController viewersViewController;
    public Integer workoutId;
    public boolean isInitialized = false;
    public String userId = "";
    public StreamPermission streamPermission = StreamPermission.publicStream;
    public boolean isPublic = true;
    public String apiKey = "";
    public String kSessionId = "";
    public String kToken = "";
    boolean isFinished = false;
    public String broadcastId = null;
    DeviceConnectionStatus deviceConnectState = DeviceConnectionStatus.notConnected;
    boolean isTyping = false;
    int videoLikesSequence = 0;
    Broadcaster.UplinkSpeedObserver uplinkSpeedObserver = new Broadcaster.UplinkSpeedObserver() { // from class: com.treadly.Treadly.UI.TreadlyVideo.PublicStream.PublicStreamHost.TreadlyPublicStreamHostFragment.9
        @Override // com.bambuser.broadcaster.Broadcaster.UplinkSpeedObserver
        public void onUplinkTestComplete(long j, boolean z) {
            if (TreadlyPublicStreamHostFragment.this.broadcaster.canStartBroadcasting()) {
                TreadlyPublicStreamHostFragment.this.lockCurrentOrientation();
                TreadlyPublicStreamHostFragment.this.initLocalRecording();
                TreadlyPublicStreamHostFragment.this.broadcaster.startBroadcast();
                TreadlyPublicStreamHostFragment.this.localFileDate = new Date(System.currentTimeMillis());
            }
        }
    };
    public boolean broadcastStopped = false;
    private Broadcaster.Observer broadcasterObserver = new Broadcaster.Observer() { // from class: com.treadly.Treadly.UI.TreadlyVideo.PublicStream.PublicStreamHost.TreadlyPublicStreamHostFragment.10
        @Override // com.bambuser.broadcaster.Broadcaster.Observer
        public void onBroadcastInfoAvailable(String str, String str2) {
        }

        @Override // com.bambuser.broadcaster.Broadcaster.Observer
        public void onCameraError(CameraError cameraError) {
        }

        @Override // com.bambuser.broadcaster.Broadcaster.Observer
        public void onCameraPreviewStateChanged() {
        }

        @Override // com.bambuser.broadcaster.Broadcaster.Observer
        public void onChatMessage(String str) {
        }

        @Override // com.bambuser.broadcaster.Broadcaster.Observer
        public void onConnectionError(ConnectionError connectionError, String str) {
        }

        @Override // com.bambuser.broadcaster.Broadcaster.Observer
        public void onResolutionsScanned() {
        }

        @Override // com.bambuser.broadcaster.Broadcaster.Observer
        public void onStreamHealthUpdate(int i) {
        }

        @Override // com.bambuser.broadcaster.Broadcaster.Observer
        public void onConnectionStatusChange(BroadcastStatus broadcastStatus) {
            if (broadcastStatus == BroadcastStatus.STARTING) {
                TreadlyPublicStreamHostFragment.this.publicBroadcastSurface.setKeepScreenOn(true);
            } else if (broadcastStatus == BroadcastStatus.IDLE) {
                TreadlyPublicStreamHostFragment.this.publicBroadcastSurface.setKeepScreenOn(false);
                Log.i("CONNECTION:", broadcastStatus.toString());
            } else if (broadcastStatus != BroadcastStatus.FINISHING || TreadlyPublicStreamHostFragment.this.broadcastId == null) {
            } else {
                Log.i("CONNECTION:", broadcastStatus.toString());
                try {
                    VideoServiceHelper.stopBambuserSessionBroadcast(TreadlyPublicStreamHostFragment.this.broadcastId, TreadlyPublicStreamHostFragment.this.streamPermission, TreadlyPublicStreamHostFragment.this.scheduleId, new VideoServiceHelper.VideoStopSessionListener() { // from class: com.treadly.Treadly.UI.TreadlyVideo.PublicStream.PublicStreamHost.TreadlyPublicStreamHostFragment.10.1
                        @Override // com.treadly.Treadly.UI.TreadlyVideo.Data.VideoServiceHelper.VideoStopSessionListener
                        public void onResponse(String str) {
                            if (str == null) {
                                TreadlyPublicStreamHostFragment.this.broadcastId = null;
                                if (TreadlyPublicStreamHostFragment.this.getActivity() == null) {
                                    return;
                                }
                                TreadlyPublicStreamHostFragment.this.getActivity().getSupportFragmentManager().popBackStack(TreadlyConnectFragment.TAG, 1);
                            }
                        }
                    });
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }

        @Override // com.bambuser.broadcaster.Broadcaster.Observer
        public void onBroadcastIdAvailable(String str) {
            TreadlyPublicStreamHostFragment.this.broadcastId = str;
            VideoUploaderManager.shared.storeVideoFileNameAlias(TreadlyPublicStreamHostFragment.this.localFileName, VideoUploaderManager.getFileName(TreadlyPublicStreamHostFragment.this.broadcastId, String.valueOf(TreadlyPublicStreamHostFragment.this.workoutId), VideoServiceVideoService.bambuser, TreadlyPublicStreamHostFragment.this.localFileDate, TreadlyPublicStreamHostFragment.this.userId));
            try {
                VideoServiceHelper.startBambuserSessionBroadcast(TreadlyPublicStreamHostFragment.this.userId, TreadlyPublicStreamHostFragment.this.broadcastId, TreadlyPublicStreamHostFragment.this.streamPermission, TreadlyPublicStreamHostFragment.this.workoutId, TreadlyPublicStreamHostFragment.this.scheduleId, new VideoServiceHelper.VideoStartSessionListener() { // from class: com.treadly.Treadly.UI.TreadlyVideo.PublicStream.PublicStreamHost.TreadlyPublicStreamHostFragment.10.2
                    @Override // com.treadly.Treadly.UI.TreadlyVideo.Data.VideoServiceHelper.VideoStartSessionListener
                    public void onResponse(String str2, String str3, String str4) {
                        TreadlyPublicStreamHostFragment.this.broadcastId = str3;
                        if (str3 != null) {
                            TreadlyEventManager.getInstance().joinVideoBroadcast(str3);
                            TreadlyEventManager.getInstance().addVideoBroadcastDelegate(TreadlyPublicStreamHostFragment.this.broadcastEventAdapter);
                            TreadlyPublicStreamHostFragment.this.getInitialViewerInformation();
                            TreadlyPublicStreamHostFragment.this.fetchVideoLike(str3);
                        }
                    }
                });
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    };
    boolean mediaSafe = false;
    TreadlyVideoBroadcastEventDelegate broadcastEventAdapter = new TreadlyVideoBroadcastEventAdapter() { // from class: com.treadly.Treadly.UI.TreadlyVideo.PublicStream.PublicStreamHost.TreadlyPublicStreamHostFragment.13
        @Override // com.treadly.Treadly.Data.Delegates.TreadlyVideoBroadcastEventAdapter, com.treadly.Treadly.Data.Delegates.TreadlyVideoBroadcastEventDelegate
        public void onUserJoinVideoBroadcast(String str, final UserInfo userInfo) {
            final UserComment userComment = new UserComment(userInfo, "Joined");
            ActivityUtil.runOnUiThread(TreadlyPublicStreamHostFragment.this.getActivity(), new Runnable() { // from class: com.treadly.Treadly.UI.TreadlyVideo.PublicStream.PublicStreamHost.TreadlyPublicStreamHostFragment.13.1
                @Override // java.lang.Runnable
                public void run() {
                    TreadlyPublicStreamHostFragment.this.commentController.addMessage(userComment);
                    TreadlyPublicStreamHostFragment.this.updateHostView(TreadlyPublicStreamHostFragment.this.userInfo);
                    TreadlyPublicStreamHostFragment.this.handleUserJoined(userInfo);
                    TreadlyPublicStreamHostFragment.this.updateHostInfo(TreadlyPublicStreamHostFragment.this.userInfo);
                    TreadlyPublicStreamHostFragment.this.viewersViewController.userJoined(userInfo);
                    TreadlyPublicStreamHostFragment.this.updateHostView(TreadlyPublicStreamHostFragment.this.userInfo);
                }
            });
        }

        @Override // com.treadly.Treadly.Data.Delegates.TreadlyVideoBroadcastEventAdapter, com.treadly.Treadly.Data.Delegates.TreadlyVideoBroadcastEventDelegate
        public void onUserLeaveVideoBroadcast(String str, final UserInfo userInfo) {
            final UserComment userComment = new UserComment(userInfo, "Left");
            ActivityUtil.runOnUiThread(TreadlyPublicStreamHostFragment.this.getActivity(), new Runnable() { // from class: com.treadly.Treadly.UI.TreadlyVideo.PublicStream.PublicStreamHost.TreadlyPublicStreamHostFragment.13.2
                @Override // java.lang.Runnable
                public void run() {
                    TreadlyPublicStreamHostFragment.this.commentController.addMessage(userComment);
                    TreadlyPublicStreamHostFragment.this.updateHostView(TreadlyPublicStreamHostFragment.this.userInfo);
                    TreadlyPublicStreamHostFragment.this.handleUserLeft(userInfo);
                    TreadlyPublicStreamHostFragment.this.updateHostInfo(TreadlyPublicStreamHostFragment.this.userInfo);
                    TreadlyPublicStreamHostFragment.this.viewersViewController.userLeft(userInfo);
                    TreadlyPublicStreamHostFragment.this.updateHostView(TreadlyPublicStreamHostFragment.this.userInfo);
                }
            });
        }

        @Override // com.treadly.Treadly.Data.Delegates.TreadlyVideoBroadcastEventAdapter, com.treadly.Treadly.Data.Delegates.TreadlyVideoBroadcastEventDelegate
        public void onReceiveVideoBroadcastUserComment(String str, final UserComment userComment) {
            ActivityUtil.runOnUiThread(TreadlyPublicStreamHostFragment.this.getActivity(), new Runnable() { // from class: com.treadly.Treadly.UI.TreadlyVideo.PublicStream.PublicStreamHost.TreadlyPublicStreamHostFragment.13.3
                @Override // java.lang.Runnable
                public void run() {
                    TreadlyPublicStreamHostFragment.this.commentController.addMessage(userComment);
                }
            });
        }

        @Override // com.treadly.Treadly.Data.Delegates.TreadlyVideoBroadcastEventAdapter, com.treadly.Treadly.Data.Delegates.TreadlyVideoBroadcastEventDelegate
        public void onReceiveVideoBroadcastLike(String str, final int i) {
            if (TreadlyPublicStreamHostFragment.this.broadcastId != null && str.equals(TreadlyPublicStreamHostFragment.this.broadcastId)) {
                ActivityUtil.runOnUiThread(TreadlyPublicStreamHostFragment.this.getActivity(), new Runnable() { // from class: com.treadly.Treadly.UI.TreadlyVideo.PublicStream.PublicStreamHost.TreadlyPublicStreamHostFragment.13.4
                    @Override // java.lang.Runnable
                    public void run() {
                        TreadlyPublicStreamHostFragment.this.commentController.setLikes(i);
                    }
                });
            }
        }
    };
    private TreadlyConnectStartState startState = TreadlyConnectStartState.notConnected;
    private DeviceConnectionStatus connectState = DeviceConnectionStatus.notConnected;
    private boolean isDeviceConnected = false;
    private AuthenticationState authenticationState = AuthenticationState.unknown;
    public ComponentInfo[] componentList = new ComponentInfo[0];
    private TreadlyVideoSliderFragment.TreadlyVideoSliderFragmentEventListener sliderAdapater = new TreadlyVideoSliderFragment.TreadlyVideoSliderFragmentEventListener() { // from class: com.treadly.Treadly.UI.TreadlyVideo.PublicStream.PublicStreamHost.TreadlyPublicStreamHostFragment.17
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
            if (TreadlyPublicStreamHostFragment.this.treadlyControl != null) {
                TreadlyPublicStreamHostFragment.this.treadlyControl.pause();
            }
        }

        @Override // com.treadly.Treadly.UI.TreadlyVideo.TreadlyVideoSliderFragment.TreadlyVideoSliderFragmentEventListener
        public void onSetSpeed(float f) {
            Log.i("WG", "onSetSpeed: " + f);
            if (TreadlyPublicStreamHostFragment.this.treadlyControl != null) {
                TreadlyPublicStreamHostFragment.this.treadlyControl.setSpeed(f);
            }
        }
    };
    private DeviceConnectionEventListener deviceConnectionListener = new DeviceConnectionEventListener() { // from class: com.treadly.Treadly.UI.TreadlyVideo.PublicStream.PublicStreamHost.TreadlyPublicStreamHostFragment.18
        @Override // com.treadly.client.lib.sdk.Listeners.DeviceConnectionEventListener
        public void onDeviceConnectionChanged(DeviceConnectionEvent deviceConnectionEvent) {
        }

        @Override // com.treadly.client.lib.sdk.Listeners.DeviceConnectionEventListener
        public void onDeviceConnectionDeviceDiscovered(DeviceInfo deviceInfo) {
        }
    };
    private List<UserInfo> friendsList = new ArrayList();
    private List<UserInfo> viewerList = new ArrayList();

    private int getScreenOrientation(int i, int i2) {
        return i2 == 2 ? (i == 0 || i == 1) ? 0 : 8 : (i == 0 || i == 3) ? 1 : 9;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void updateHostView(UserInfo userInfo) {
    }

    @Override // androidx.fragment.app.Fragment
    public void onCreate(@Nullable Bundle bundle) {
        super.onCreate(bundle);
        initController();
        if (getActivity() != null) {
            ((MainActivity) getActivity()).setOnBackPressedListener(new OnBackPressedListener() { // from class: com.treadly.Treadly.UI.TreadlyVideo.PublicStream.PublicStreamHost.TreadlyPublicStreamHostFragment.1
                @Override // com.treadly.Treadly.UI.Util.OnBackPressedListener
                public void backAction() {
                    TreadlyPublicStreamHostFragment.this.getActivity().getSupportFragmentManager().popBackStack(TreadlyConnectFragment.TAG, 1);
                }
            });
        }
    }

    @Override // androidx.fragment.app.Fragment
    public void onResume() {
        super.onResume();
        hideBottomNavigation();
        onActivityResume();
        VideoUploaderManager.context = getContext();
        VideoUploaderManager.shared.stop();
    }

    @Override // androidx.fragment.app.Fragment
    public void onPause() {
        super.onPause();
        onActivityPause();
    }

    @Override // androidx.fragment.app.Fragment
    public void onDestroy() {
        super.onDestroy();
        onActivityDestroy();
        if (getActivity() != null) {
            ((MainActivity) getActivity()).setOnBackPressedListener(null);
        }
        try {
            VideoUploaderManager.shared.start();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override // androidx.fragment.app.Fragment
    @Nullable
    public View onCreateView(@NonNull LayoutInflater layoutInflater, @Nullable ViewGroup viewGroup, @Nullable Bundle bundle) {
        hideBottomNavigation();
        TreadlyClientLib.shared.addDeviceConnectionEventListener(this.deviceConnectionListener);
        return layoutInflater.inflate(R.layout.fragment_treadly_video_host_broadcast, viewGroup, false);
    }

    @Override // com.treadly.Treadly.UI.Util.BaseClasses.BaseFragment, androidx.fragment.app.Fragment
    public void onDetach() {
        super.onDetach();
        TreadlyEventManager.getInstance().leaveVideoBroadcast(this.broadcastId);
        TreadlyEventManager.getInstance().removeVideoBroadcastDelegate(this.broadcastEventAdapter);
        TreadlyClientLib.shared.removeDeviceConnectionEventListener(this.deviceConnectionListener);
        showBottomNavigation();
    }

    public void setDeviceConnected(boolean z) {
        this.isDeviceConnected = z;
    }

    public void setDeviceConnectState(DeviceConnectionStatus deviceConnectionStatus) {
        setDeviceConnected(this.deviceConnectState == DeviceConnectionStatus.connected && this.authenticationState == AuthenticationState.active);
    }

    @Override // com.treadly.Treadly.UI.Util.BaseClasses.BaseFragment, androidx.fragment.app.Fragment
    public void onViewCreated(@NonNull View view, @Nullable Bundle bundle) {
        this.hostFragmentView = view;
        refreshUsers();
        initServiceForHost();
        initCommentController();
        initViewersView();
        this.defaultDisplay = getActivity().getWindowManager().getDefaultDisplay();
        this.orientationEventListener = new OrientationEventListener(getContext()) { // from class: com.treadly.Treadly.UI.TreadlyVideo.PublicStream.PublicStreamHost.TreadlyPublicStreamHostFragment.2
            @Override // android.view.OrientationEventListener
            public void onOrientationChanged(int i) {
                if (TreadlyPublicStreamHostFragment.this.broadcaster == null || !TreadlyPublicStreamHostFragment.this.broadcaster.canStartBroadcasting()) {
                    return;
                }
                TreadlyPublicStreamHostFragment.this.broadcaster.setRotation(TreadlyPublicStreamHostFragment.this.defaultDisplay.getRotation());
            }
        };
        this.isInitialized = true;
        initTreadmillControl(view);
        initHostStatsInfoView(view);
    }

    public void initViewersView() {
        this.viewersViewController = new TreadlyPublicStreamViewersController();
    }

    public void getInitialViewerInformation() {
        if (this.broadcastId == null) {
            return;
        }
        BroadcastViewerServiceHelper.getViewers(this.broadcastId, new BroadcastViewerServiceHelper.BroadcastViewerServiceHelperListener() { // from class: com.treadly.Treadly.UI.TreadlyVideo.PublicStream.PublicStreamHost.TreadlyPublicStreamHostFragment.3
            @Override // com.treadly.Treadly.UI.TreadlyVideo.Data.BroadcastViewerServiceHelper.BroadcastViewerServiceHelperListener
            public void onGetViewerComments(String str, List<UserComment> list) {
            }

            @Override // com.treadly.Treadly.UI.TreadlyVideo.Data.BroadcastViewerServiceHelper.BroadcastViewerServiceHelperListener
            public void onViewerUpdate(String str, List<String> list, Boolean bool) {
            }

            @Override // com.treadly.Treadly.UI.TreadlyVideo.Data.BroadcastViewerServiceHelper.BroadcastViewerServiceHelperListener
            public void onGetViewers(String str, List<UserInfo> list) {
                if (str != null || list == null) {
                    return;
                }
                TreadlyPublicStreamHostFragment.this.viewersViewController.setInitialViewers(list);
            }
        });
    }

    public void onActivityPause() {
        this.broadcaster.onActivityPause();
        Log.i("CONNECTION:", "Pausing...");
    }

    public void onActivityResume() {
        this.orientationEventListener.enable();
        this.broadcaster.setCameraSurface(this.publicBroadcastSurface);
        this.broadcaster.setRotation(this.defaultDisplay.getRotation());
        this.broadcaster.onActivityResume();
    }

    public void onActivityDestroy() {
        this.broadcaster.onActivityDestroy();
    }

    public void initServiceForHost() {
        this.publicBroadcastSurface = (SurfaceViewWithAutoAR) this.hostFragmentView.findViewById(R.id.host_public_broadcast);
        this.publicBroadcastSurface.setCropToParent(false);
        this.broadcaster = new Broadcaster(getActivity(), NetworkConstants.SERVICE_BAMBUSER_APPLICATION_ID, this.broadcasterObserver);
        this.broadcaster.setUplinkSpeedObserver(this.uplinkSpeedObserver);
        this.broadcaster.setTitle("My Workout");
        this.broadcaster.setTalkbackMixin(false);
        this.broadcaster.switchCamera();
        this.broadcaster.setSaveOnServer(false);
        this.broadcaster.setAuthor(this.userId);
        this.broadcaster.setUplinkSpeedObserver(this.uplinkSpeedObserver);
        if (this.workoutId != null) {
            Broadcaster broadcaster = this.broadcaster;
            broadcaster.setAuthor(this.userId + "-workout-" + this.workoutId);
        }
        initButtons();
    }

    void initButtons() {
        this.broadcastButton = (AppCompatImageButton) this.hostFragmentView.findViewById(R.id.end_workout_button);
        this.broadcastButton.setOnClickListener(new View.OnClickListener() { // from class: com.treadly.Treadly.UI.TreadlyVideo.PublicStream.PublicStreamHost.TreadlyPublicStreamHostFragment.4
            @Override // android.view.View.OnClickListener
            public void onClick(View view) {
                TreadlyPublicStreamHostFragment.this.broadcastStopped = true;
                TreadlyPublicStreamHostFragment.this.broadcaster.stopBroadcast();
            }
        });
        this.flipCameraButton = (ImageButton) this.hostFragmentView.findViewById(R.id.switch_camera_button);
        this.flipCameraButton.setOnClickListener(new View.OnClickListener() { // from class: com.treadly.Treadly.UI.TreadlyVideo.PublicStream.PublicStreamHost.TreadlyPublicStreamHostFragment.5
            @Override // android.view.View.OnClickListener
            public void onClick(View view) {
                TreadlyPublicStreamHostFragment.this.broadcaster.switchCamera();
            }
        });
        this.inviteFriendsButton = (ImageButton) this.hostFragmentView.findViewById(R.id.friend_invite_button);
        this.inviteFriendsButton.setOnClickListener(new View.OnClickListener() { // from class: com.treadly.Treadly.UI.TreadlyVideo.PublicStream.PublicStreamHost.TreadlyPublicStreamHostFragment.6
            @Override // android.view.View.OnClickListener
            public void onClick(View view) {
                TreadlyPublicStreamHostFragment.this.inviteFriends();
            }
        });
    }

    private void initCommentController() {
        if (this.allowComments) {
            this.commentController = new TreadlyPublicStreamMessagingViewController(getContext(), this.hostFragmentView, true);
            this.commentController.isStreamer = true;
            this.commentController.commentControllerListener = new TreadlyPublicStreamMessagingViewController.CommentControllerListener() { // from class: com.treadly.Treadly.UI.TreadlyVideo.PublicStream.PublicStreamHost.TreadlyPublicStreamHostFragment.7
                @Override // com.treadly.Treadly.UI.TreadlyVideo.PublicStream.PublicStreamMessagingView.TreadlyPublicStreamMessagingViewController.CommentControllerListener
                public void didStartTyping() {
                    TreadlyPublicStreamHostFragment.this.isTyping = true;
                }

                @Override // com.treadly.Treadly.UI.TreadlyVideo.PublicStream.PublicStreamMessagingView.TreadlyPublicStreamMessagingViewController.CommentControllerListener
                public void didEndTyping() {
                    TreadlyPublicStreamHostFragment.this.isTyping = false;
                }

                @Override // com.treadly.Treadly.UI.TreadlyVideo.PublicStream.PublicStreamMessagingView.TreadlyPublicStreamMessagingViewController.CommentControllerListener
                public void didPressLike() {
                    TreadlyPublicStreamHostFragment.this.handleStreamLiked();
                }

                @Override // com.treadly.Treadly.UI.TreadlyVideo.PublicStream.PublicStreamMessagingView.TreadlyPublicStreamMessagingViewController.CommentControllerListener
                public void didPressLikeCount() {
                    TreadlyPublicStreamHostFragment.this.handleVideoLikeCountPressed();
                }

                @Override // com.treadly.Treadly.UI.TreadlyVideo.PublicStream.PublicStreamMessagingView.TreadlyPublicStreamMessagingViewController.CommentControllerListener
                public void didSendMessage(String str) {
                    UserInfo currentUserInfo = TreadlyPublicStreamHostFragment.this.getCurrentUserInfo();
                    if (currentUserInfo == null || TreadlyPublicStreamHostFragment.this.broadcastId == null) {
                        return;
                    }
                    TreadlyPublicStreamHostFragment.this.commentController.addMessage(new UserComment(currentUserInfo, str));
                    TreadlyEventManager.getInstance().sendVideoBroadcastComment(TreadlyPublicStreamHostFragment.this.broadcastId, str);
                }
            };
        }
    }

    UserInfo getCurrentUserInfo() {
        if (TreadlyServiceManager.getInstance().getUserId() == null) {
            return null;
        }
        return TreadlyServiceManager.getInstance().getUserInfoById(this.userId);
    }

    void handleVideoLikeCountPressed() {
        if (this.broadcastId == null) {
            return;
        }
        this.videoLikesSequence++;
        final int i = this.videoLikesSequence;
        VideoServiceHelper.getVideoBroadcastLikeUserInfo(this.broadcastId, new VideoServiceHelper.VideoLikeListener() { // from class: com.treadly.Treadly.UI.TreadlyVideo.PublicStream.PublicStreamHost.TreadlyPublicStreamHostFragment.8
            @Override // com.treadly.Treadly.UI.TreadlyVideo.Data.VideoServiceHelper.VideoLikeListener
            public void onCreateVideoLike(String str) {
            }

            @Override // com.treadly.Treadly.UI.TreadlyVideo.Data.VideoServiceHelper.VideoLikeListener
            public void onDeleteVideoLike(String str) {
            }

            @Override // com.treadly.Treadly.UI.TreadlyVideo.Data.VideoServiceHelper.VideoLikeListener
            public void onGetVideoLikeInfo(String str, VideoLikeInfo videoLikeInfo) {
            }

            public int hashCode() {
                return super.hashCode();
            }

            @Override // com.treadly.Treadly.UI.TreadlyVideo.Data.VideoServiceHelper.VideoLikeListener
            public void onGetVideoLikeUserInfo(String str, List<VideoLikeUserInfo> list) {
                if (str != null || list == null || list.isEmpty() || TreadlyPublicStreamHostFragment.this.videoLikesSequence != i) {
                    return;
                }
                TreadlyPublicStreamHostFragment.this.displayVideoLikesUserList(list);
            }
        });
    }

    boolean isAlreadyViewing(UserInfo userInfo) {
        PrintStream printStream = System.out;
        printStream.println("VIEWERS: Host " + this.viewersViewController.viewers);
        for (UserInfo userInfo2 : this.viewersViewController.viewers) {
            if (userInfo2.id.equals(userInfo.id)) {
                return true;
            }
        }
        return false;
    }

    void displayVideoLikesUserList(List<VideoLikeUserInfo> list) {
        AlertDialog.Builder title = new AlertDialog.Builder(getContext()).setTitle("Likes");
        String[] strArr = new String[list.size()];
        int i = 0;
        for (VideoLikeUserInfo videoLikeUserInfo : list) {
            strArr[i] = videoLikeUserInfo.userName;
            i++;
        }
        title.setItems(strArr, (DialogInterface.OnClickListener) null);
        title.setNeutralButton("Dismiss", (DialogInterface.OnClickListener) null);
        title.show();
    }

    File getRecordingPath() {
        return getActivity().getExternalFilesDir(Environment.DIRECTORY_MOVIES);
    }

    String getRecordingFilename() {
        return VideoUploaderManager.getFileName(ImagesContract.LOCAL, this.workoutId.toString(), VideoServiceVideoService.bambuser, new Date(), this.userId);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void initLocalRecording() {
        if (this.broadcaster == null || !this.broadcaster.hasLocalMediaCapability()) {
            return;
        }
        File recordingPath = getRecordingPath();
        if (recordingPath == null) {
            Toast.makeText(getContext(), "Can't store local copy, external storage unavailable", 0).show();
            return;
        }
        new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss", Locale.US);
        File file = new File(recordingPath, getRecordingFilename() + ".mp4");
        this.localFileName = file.getName();
        this.broadcaster.storeLocalMedia(file, new Broadcaster.LocalMediaObserver() { // from class: com.treadly.Treadly.UI.TreadlyVideo.PublicStream.PublicStreamHost.TreadlyPublicStreamHostFragment.11
            @Override // com.bambuser.broadcaster.Broadcaster.LocalMediaObserver
            public void onLocalMediaError() {
                Toast.makeText(TreadlyPublicStreamHostFragment.this.getContext(), "Failed to write to video file. Storage/memory full?", 1).show();
            }

            @Override // com.bambuser.broadcaster.Broadcaster.LocalMediaObserver
            public void onLocalMediaClosed(String str) {
                if (str == null || !str.endsWith(".mp4")) {
                    return;
                }
                MediaScannerConnection.scanFile(TreadlyPublicStreamHostFragment.this.getContext(), new String[]{str}, null, null);
                TreadlyPublicStreamHostFragment.this.mediaSafe = true;
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void lockCurrentOrientation() {
        getActivity().setRequestedOrientation(getScreenOrientation(getActivity().getWindowManager().getDefaultDisplay().getRotation(), getResources().getConfiguration().orientation));
    }

    public void refreshUsers() {
        TreadlyServiceManager.getInstance().refreshUsersInfo(new TreadlyServiceResponseEventAdapter() { // from class: com.treadly.Treadly.UI.TreadlyVideo.PublicStream.PublicStreamHost.TreadlyPublicStreamHostFragment.12
            @Override // com.treadly.Treadly.Data.Listeners.TreadlyServiceResponseEventAdapter, com.treadly.Treadly.Data.Listeners.TreadlyServiceResponseEventListener
            public void onSuccess(String str) {
                if (str != null) {
                    System.out.println("Error refreshing users");
                }
            }
        });
    }

    private void setStartState(TreadlyConnectStartState treadlyConnectStartState) {
        this.startState = treadlyConnectStartState;
        if (this.sliderFragment != null) {
            this.sliderFragment.setStartState(treadlyConnectStartState);
        }
    }

    private void setConnectState(DeviceConnectionStatus deviceConnectionStatus) {
        this.connectState = deviceConnectionStatus;
        setIsDeviceConnected(this.connectState == DeviceConnectionStatus.connected && this.authenticationState == AuthenticationState.active);
    }

    private void setIsDeviceConnected(boolean z) {
        this.isDeviceConnected = z;
        if (this.sliderFragment != null) {
            this.sliderFragment.isConnected = this.isConnected;
        }
        handleDeviceConnectedState(this.isDeviceConnected);
    }

    private void setAuthenticationState(AuthenticationState authenticationState) {
        if (authenticationState == this.authenticationState) {
            return;
        }
        setIsDeviceConnected(this.connectState == DeviceConnectionStatus.connected && this.authenticationState == AuthenticationState.active);
    }

    private void setAuthenticationState() {
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

    boolean isGen2() {
        if ((getBleComponent() == null) || (getBleComponent().getVersionInfo() == null)) {
            return false;
        }
        VersionInfo versionInfo = new VersionInfo(2, 0, 0);
        return getBleComponent().getVersionInfo().isGreaterThan(versionInfo) || getBleComponent().getVersionInfo().isEqual(versionInfo);
    }

    private void handleDeviceConnectedState(boolean z) {
        if (TreadlyServiceManager.getInstance().getUserId().isEmpty()) {
        }
    }

    private void initTreadmillControl(View view) {
        this.sliderFrameLayout = (FrameLayout) view.findViewById(R.id.video_slider_layout);
        this.sliderFrameLayout.setClipToOutline(true);
        this.sliderFragment = new TreadlyVideoSliderFragment();
        this.sliderFragment.parentFragmentPublic = this;
        this.sliderFragment.listener = this.sliderAdapater;
        if (getActivity() != null) {
            FragmentTransaction beginTransaction = getActivity().getSupportFragmentManager().beginTransaction();
            TreadlyVideoSliderFragment treadlyVideoSliderFragment = this.sliderFragment;
            TreadlyVideoSliderFragment treadlyVideoSliderFragment2 = this.sliderFragment;
            beginTransaction.add(R.id.video_slider_layout, treadlyVideoSliderFragment, TreadlyVideoSliderFragment.TAG).commit();
        }
        setConnectState(this.isDeviceConnected ? DeviceConnectionStatus.connected : DeviceConnectionStatus.notConnected);
        if (this.connectState == DeviceConnectionStatus.connected) {
            this.treadlyControl.setAuthenticationState();
            setAuthenticationState();
        }
        this.speedUpButton = (Button) view.findViewById(R.id.public_speed_up);
        this.speedUpButton.setOnClickListener(new View.OnClickListener() { // from class: com.treadly.Treadly.UI.TreadlyVideo.PublicStream.PublicStreamHost.TreadlyPublicStreamHostFragment.14
            @Override // android.view.View.OnClickListener
            public void onClick(View view2) {
                TreadlyPublicStreamHostFragment.this.treadlyControl.speedUp();
            }
        });
        this.speedDownButton = (Button) view.findViewById(R.id.public_speed_down);
        this.speedDownButton.setOnClickListener(new View.OnClickListener() { // from class: com.treadly.Treadly.UI.TreadlyVideo.PublicStream.PublicStreamHost.TreadlyPublicStreamHostFragment.15
            @Override // android.view.View.OnClickListener
            public void onClick(View view2) {
                Log.i("TreadmillController", "in speeddown");
                TreadlyPublicStreamHostFragment.this.treadlyControl.speedDown();
            }
        });
    }

    private void initController() {
        this.treadlyContent = new TreadlyControlContent();
        this.treadlyControl = new TreadlyTreadmillController(this.treadlyContent);
        this.treadlyControl.adapter = new TreadlyTreadmillController.TreadlyTreadmillAdapter() { // from class: com.treadly.Treadly.UI.TreadlyVideo.PublicStream.PublicStreamHost.TreadlyPublicStreamHostFragment.16
            @Override // com.treadly.Treadly.UI.TreadmillControl.TreadlyTreadmillController.TreadlyTreadmillAdapter
            public void didChangeDeviceConnection(boolean z) {
            }

            @Override // com.treadly.Treadly.UI.TreadmillControl.TreadlyTreadmillController.TreadlyTreadmillAdapter
            public void didChangeStatus(DeviceStatus deviceStatus) {
                if (TreadlyPublicStreamHostFragment.this.authenticationState == AuthenticationState.active) {
                }
            }

            @Override // com.treadly.Treadly.UI.TreadmillControl.TreadlyTreadmillController.TreadlyTreadmillAdapter
            public void didChangeContent(TreadlyControlContent treadlyControlContent) {
                TreadlyPublicStreamHostFragment.this.authenticationState = treadlyControlContent.authenticationState;
                if (treadlyControlContent.authenticationState == AuthenticationState.active) {
                    TreadlyPublicStreamHostFragment.this.sliderFragment.currentDeviceStatusInfo = TreadlyPublicStreamHostFragment.this.treadlyControl.status;
                    if (!TreadlyPublicStreamHostFragment.this.sliderFragment.isPanning) {
                        TreadlyPublicStreamHostFragment.this.sliderFragment.setSpeedLabels((float) treadlyControlContent.speedValue);
                    }
                    TreadlyPublicStreamHostFragment.this.sendCurrentUserStats(treadlyControlContent);
                } else if (treadlyControlContent.authenticationState == AuthenticationState.unknown) {
                    TreadlyPublicStreamHostFragment.this.treadlyControl.setAuthenticationState();
                }
            }
        };
    }

    public void setVideoLikeInfo(VideoLikeInfo videoLikeInfo) {
        this.streamLikeInfo = videoLikeInfo;
        if (videoLikeInfo == null) {
            this.commentController.setLikes(-1);
            this.commentController.setIsLiked(false, true);
            return;
        }
        this.commentController.setLikes(videoLikeInfo.likeCount.intValue());
        this.commentController.setIsLiked(videoLikeInfo.isLike.booleanValue(), true);
    }

    void fetchVideoLike(String str) {
        VideoServiceHelper.getVideoBroadcastLikeInfo(this.broadcastId, new VideoServiceHelper.VideoLikeListener() { // from class: com.treadly.Treadly.UI.TreadlyVideo.PublicStream.PublicStreamHost.TreadlyPublicStreamHostFragment.19
            @Override // com.treadly.Treadly.UI.TreadlyVideo.Data.VideoServiceHelper.VideoLikeListener
            public void onCreateVideoLike(String str2) {
            }

            @Override // com.treadly.Treadly.UI.TreadlyVideo.Data.VideoServiceHelper.VideoLikeListener
            public void onDeleteVideoLike(String str2) {
            }

            @Override // com.treadly.Treadly.UI.TreadlyVideo.Data.VideoServiceHelper.VideoLikeListener
            public void onGetVideoLikeUserInfo(String str2, List<VideoLikeUserInfo> list) {
            }

            @Override // com.treadly.Treadly.UI.TreadlyVideo.Data.VideoServiceHelper.VideoLikeListener
            public void onGetVideoLikeInfo(String str2, final VideoLikeInfo videoLikeInfo) {
                if (str2 != null || videoLikeInfo == null) {
                    return;
                }
                ActivityUtil.runOnUiThread(TreadlyPublicStreamHostFragment.this.getActivity(), new Runnable() { // from class: com.treadly.Treadly.UI.TreadlyVideo.PublicStream.PublicStreamHost.TreadlyPublicStreamHostFragment.19.1
                    @Override // java.lang.Runnable
                    public void run() {
                        TreadlyPublicStreamHostFragment.this.setVideoLikeInfo(videoLikeInfo);
                    }
                });
            }
        });
    }

    void handleStreamLiked() {
        if (this.streamLikeInfo == null || this.streamLikeInfo.isLike == null || this.broadcastId == null) {
            return;
        }
        if (this.streamLikeInfo.isLike.booleanValue()) {
            VideoServiceHelper.deleteVideoBroadcastLike(this.broadcastId, new VideoServiceHelper.VideoResponseListener() { // from class: com.treadly.Treadly.UI.TreadlyVideo.PublicStream.PublicStreamHost.TreadlyPublicStreamHostFragment.20
                @Override // com.treadly.Treadly.UI.TreadlyVideo.Data.VideoServiceHelper.VideoResponseListener
                public void onResponse(String str) {
                    if (str == null) {
                        TreadlyPublicStreamHostFragment.this.fetchVideoLike(TreadlyPublicStreamHostFragment.this.broadcastId);
                    }
                }
            });
        } else {
            VideoServiceHelper.createVideoBroadcastLike(this.broadcastId, new VideoServiceHelper.VideoResponseListener() { // from class: com.treadly.Treadly.UI.TreadlyVideo.PublicStream.PublicStreamHost.TreadlyPublicStreamHostFragment.21
                @Override // com.treadly.Treadly.UI.TreadlyVideo.Data.VideoServiceHelper.VideoResponseListener
                public void onResponse(String str) {
                    if (str == null) {
                        TreadlyPublicStreamHostFragment.this.fetchVideoLike(TreadlyPublicStreamHostFragment.this.broadcastId);
                    }
                }
            });
        }
        ActivityUtil.runOnUiThread(getActivity(), new Runnable() { // from class: com.treadly.Treadly.UI.TreadlyVideo.PublicStream.PublicStreamHost.TreadlyPublicStreamHostFragment.22
            @Override // java.lang.Runnable
            public void run() {
                TreadlyPublicStreamHostFragment.this.commentController.setIsLiked(!TreadlyPublicStreamHostFragment.this.streamLikeInfo.isLike.booleanValue(), true);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void inviteFriends() {
        try {
            TreadlyServiceManager.getInstance().getFriendsInfo(new TreadlyServiceResponseEventAdapter() { // from class: com.treadly.Treadly.UI.TreadlyVideo.PublicStream.PublicStreamHost.TreadlyPublicStreamHostFragment.23
                @Override // com.treadly.Treadly.Data.Listeners.TreadlyServiceResponseEventAdapter, com.treadly.Treadly.Data.Listeners.TreadlyServiceResponseEventListener
                public void onUserFriendsInfo(String str, List<UserInfo> list) throws JSONException {
                    if (str != null || list == null || list.size() <= 0) {
                        return;
                    }
                    ArrayList arrayList = new ArrayList();
                    TreadlyPublicStreamHostFragment.this.friendsList = list;
                    for (UserInfo userInfo : list) {
                        if (!TreadlyPublicStreamHostFragment.this.isAlreadyViewing(userInfo)) {
                            arrayList.add(userInfo);
                        }
                    }
                    TreadlyPublicStreamHostFragment.this.presentInviteView(arrayList);
                }
            });
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void presentInviteView(List<UserInfo> list) {
        final TreadlyVideoInviteFriend treadlyVideoInviteFriend = new TreadlyVideoInviteFriend();
        treadlyVideoInviteFriend.setFriendsList(list);
        treadlyVideoInviteFriend.setOnClickListener(new TreadlyVideoInviteListener() { // from class: com.treadly.Treadly.UI.TreadlyVideo.PublicStream.PublicStreamHost.TreadlyPublicStreamHostFragment.24
            @Override // com.treadly.Treadly.Data.Listeners.TreadlyVideoInviteListener
            public void onResponse(UserInfo userInfo) {
                TreadlyPublicStreamHostFragment.this.handleInviteFriend(userInfo);
                treadlyVideoInviteFriend.dismiss();
            }
        });
        treadlyVideoInviteFriend.show(getActivity().getSupportFragmentManager(), "videoInviteList");
    }

    public void handleInviteFriend(UserInfo userInfo) {
        BroadcastInviteServiceHelper.requestBroadcastInvite(this.userId, userInfo.id, new BroadcastInviteServiceHelper.BroadcastInviteResponseListener() { // from class: com.treadly.Treadly.UI.TreadlyVideo.PublicStream.PublicStreamHost.TreadlyPublicStreamHostFragment.25
            @Override // com.treadly.Treadly.UI.TreadlyVideo.Data.BroadcastInviteServiceHelper.BroadcastInviteResponseListener
            public void onInviteInfoList(String str, List<InviteServiceInviteInfo> list) {
            }

            @Override // com.treadly.Treadly.UI.TreadlyVideo.Data.BroadcastInviteServiceHelper.BroadcastInviteResponseListener
            public void onSuccess(String str) {
                if (str == null) {
                    System.out.println("BROADCASTINVITE: SUCCESS");
                }
                PrintStream printStream = System.out;
                printStream.println("BROADCASTINVITE: " + str);
            }
        });
    }

    private void initHostStatsInfoView(View view) {
        this.userStatsInfoView = (TreadlyPublicStreamUserStatsInfoView) view.findViewById(R.id.host_info_view);
        updateHostInfo(null);
        getHostInfo();
    }

    private void getHostInfo() {
        TreadlyServiceManager.getInstance().refreshUserInfo(new TreadlyServiceResponseEventAdapter() { // from class: com.treadly.Treadly.UI.TreadlyVideo.PublicStream.PublicStreamHost.TreadlyPublicStreamHostFragment.26
            @Override // com.treadly.Treadly.Data.Listeners.TreadlyServiceResponseEventAdapter, com.treadly.Treadly.Data.Listeners.TreadlyServiceResponseEventListener
            public void onUserInfo(String str, UserInfo userInfo) {
                if (str == null && userInfo != null) {
                    TreadlyPublicStreamHostFragment.this.userInfo = userInfo;
                }
                TreadlyPublicStreamHostFragment.this.getInitialViewers();
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void getInitialViewers() {
        BroadcastViewerServiceHelper.getViewers(this.broadcastId, new BroadcastViewerServiceHelper.BroadcastViewerServiceHelperAdapter() { // from class: com.treadly.Treadly.UI.TreadlyVideo.PublicStream.PublicStreamHost.TreadlyPublicStreamHostFragment.27
            @Override // com.treadly.Treadly.UI.TreadlyVideo.Data.BroadcastViewerServiceHelper.BroadcastViewerServiceHelperAdapter, com.treadly.Treadly.UI.TreadlyVideo.Data.BroadcastViewerServiceHelper.BroadcastViewerServiceHelperListener
            public void onGetViewers(String str, List<UserInfo> list) {
                if (str == null && list != null) {
                    TreadlyPublicStreamHostFragment.this.viewerList = list;
                }
                TreadlyPublicStreamHostFragment.this.updateHostInfo(TreadlyPublicStreamHostFragment.this.userInfo);
            }
        });
        updateHostInfo(this.userInfo);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void sendCurrentUserStats(TreadlyControlContent treadlyControlContent) {
        final VideoServiceUserStatsInfo videoServiceUserStatsInfo = new VideoServiceUserStatsInfo(TreadlyServiceManager.getInstance().getUserId(), TreadlyServiceManager.getInstance().getName(), RunningSessionManager.getInstance().calories, treadlyControlContent.walkDistance, treadlyControlContent.stepsCount, treadlyControlContent.speedValue, treadlyControlContent.averageSpeed, treadlyControlContent.speedDataSet, treadlyControlContent.speedUnits, treadlyControlContent.elapsedTime);
        String str = this.broadcastId;
        if (str != null) {
            TreadlyEventManager.getInstance().sendVideoBroadcastCurrentUserStats(str, videoServiceUserStatsInfo);
        }
        ActivityUtil.runOnUiThread(getActivity(), new Runnable() { // from class: com.treadly.Treadly.UI.TreadlyVideo.PublicStream.PublicStreamHost.TreadlyPublicStreamHostFragment.28
            @Override // java.lang.Runnable
            public void run() {
                TreadlyPublicStreamHostFragment.this.updateHostStats(videoServiceUserStatsInfo);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void updateHostInfo(UserInfo userInfo) {
        int size = this.viewerList.size();
        if (userInfo != null) {
            this.userStatsInfoView.setHostInfo(userInfo, size);
            this.userStatsInfoView.setVisibility(0);
            return;
        }
        this.userStatsInfoView.setVisibility(8);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void updateHostStats(VideoServiceUserStatsInfo videoServiceUserStatsInfo) {
        this.userStatsInfoView.setHostStats(videoServiceUserStatsInfo);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void handleUserJoined(UserInfo userInfo) {
        boolean z;
        Iterator<UserInfo> it = this.viewerList.iterator();
        while (true) {
            if (!it.hasNext()) {
                z = false;
                break;
            }
            UserInfo next = it.next();
            if (next.id != null && userInfo != null && userInfo.id != null && userInfo.id.equals(next.id)) {
                z = true;
                break;
            }
        }
        if (z) {
            return;
        }
        this.viewerList.add(userInfo);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void handleUserLeft(UserInfo userInfo) {
        for (UserInfo userInfo2 : new ArrayList(this.viewerList)) {
            if (userInfo2.id != null && userInfo != null && userInfo.id != null && userInfo.id.equals(userInfo2.id)) {
                this.viewerList.remove(userInfo2);
                return;
            }
        }
    }
}
