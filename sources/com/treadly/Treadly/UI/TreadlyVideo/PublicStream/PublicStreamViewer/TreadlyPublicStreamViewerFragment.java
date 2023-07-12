package com.treadly.Treadly.UI.TreadlyVideo.PublicStream.PublicStreamViewer;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatImageButton;
import androidx.fragment.app.FragmentTransaction;
import com.bambuser.broadcaster.BroadcastPlayer;
import com.bambuser.broadcaster.PlayerState;
import com.bambuser.broadcaster.SurfaceViewWithAutoAR;
import com.treadly.Treadly.Data.Constants.NetworkConstants;
import com.treadly.Treadly.Data.Delegates.TreadlyVideoBroadcastEventAdapter;
import com.treadly.Treadly.Data.Delegates.TreadlyVideoBroadcastEventDelegate;
import com.treadly.Treadly.Data.Listeners.TreadlyServiceResponseEventAdapter;
import com.treadly.Treadly.Data.Managers.RunningSessionManager;
import com.treadly.Treadly.Data.Managers.TreadlyActivationManager;
import com.treadly.Treadly.Data.Managers.TreadlyEventManager;
import com.treadly.Treadly.Data.Managers.TreadlyServiceManager;
import com.treadly.Treadly.Data.Model.StreamPermission;
import com.treadly.Treadly.Data.Model.UserComment;
import com.treadly.Treadly.Data.Model.UserInfo;
import com.treadly.Treadly.Data.Model.VideoLikeInfo;
import com.treadly.Treadly.Data.Model.VideoLikeUserInfo;
import com.treadly.Treadly.R;
import com.treadly.Treadly.UI.TreadlyConnect.TreadlyConnectFragment;
import com.treadly.Treadly.UI.TreadlyConnect.TreadlyConnectStartState;
import com.treadly.Treadly.UI.TreadlyVideo.Data.BroadcastViewerServiceHelper;
import com.treadly.Treadly.UI.TreadlyVideo.Data.VideoServiceHelper;
import com.treadly.Treadly.UI.TreadlyVideo.Data.VideoServiceSessionInfo;
import com.treadly.Treadly.UI.TreadlyVideo.Data.VideoServiceUserStatsInfo;
import com.treadly.Treadly.UI.TreadlyVideo.PublicStream.Components.HostInfo.TreadlyPublicStreamUserStatsInfoView;
import com.treadly.Treadly.UI.TreadlyVideo.PublicStream.PublicStreamMessagingView.TreadlyPublicStreamMessagingViewController;
import com.treadly.Treadly.UI.TreadlyVideo.TreadlyVideoSliderFragment;
import com.treadly.Treadly.UI.TreadmillControl.Data.TreadlyControlContent;
import com.treadly.Treadly.UI.TreadmillControl.TreadlyTreadmillController;
import com.treadly.Treadly.UI.Util.ActivityUtil;
import com.treadly.Treadly.UI.Util.BaseClasses.BaseFragment;
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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/* loaded from: classes2.dex */
public class TreadlyPublicStreamViewerFragment extends BaseFragment {
    public static final String TAG = "publicStreamViewer";
    VideoServiceSessionInfo broadcastInfo;
    BroadcastPlayer broadcastPlayer;
    TreadlyPublicStreamMessagingViewController commentController;
    private String deviceName;
    boolean hostEnded;
    private UserInfo hostInfo;
    private TreadlyPublicStreamUserStatsInfoView hostStatsInfoView;
    UserInfo hostUser;
    private boolean isConnected;
    private AppCompatImageButton leaveButton;
    private SurfaceViewWithAutoAR publicBroadcastSurface;
    PublicStreamViewerListener publicStreamViewerListener;
    private TreadlyVideoSliderFragment sliderFragment;
    private FrameLayout sliderFrameLayout;
    private Button speedDownButton;
    private Button speedUpButton;
    private TreadlyControlContent treadlyContent;
    private TreadlyTreadmillController treadlyControl;
    public String userId;
    private UserInfo userInfo;
    private TreadlyPublicStreamUserStatsInfoView userStatsInfoView;
    View viewerFragmentView;
    public StreamPermission streamPermission = StreamPermission.publicStream;
    Map<String, UserInfo> viewersLookup = new HashMap();
    Map<String, UserInfo> viewersLeft = new HashMap();
    List<UserInfo> viewers = new ArrayList();
    boolean allowComments = true;
    int sessionIndex = 0;
    boolean isTyping = false;
    int videoLikesSequence = 0;
    VideoLikeInfo streamLikeInfo = null;
    BroadcastPlayer.Observer broadcastPlayerObserver = new BroadcastPlayer.Observer() { // from class: com.treadly.Treadly.UI.TreadlyVideo.PublicStream.PublicStreamViewer.TreadlyPublicStreamViewerFragment.13
        @Override // com.bambuser.broadcaster.BroadcastPlayer.Observer
        public void onBroadcastLoaded(boolean z, int i, int i2) {
        }

        @Override // com.bambuser.broadcaster.BroadcastPlayer.Observer
        public void onStateChange(PlayerState playerState) {
            if (playerState == PlayerState.COMPLETED) {
                TreadlyPublicStreamViewerFragment.this.hostEnded = true;
                if (TreadlyPublicStreamViewerFragment.this.isLeaving) {
                    return;
                }
                TreadlyPublicStreamViewerFragment.this.displayHostEndedAlert();
            }
        }
    };
    boolean isLeaving = false;
    TreadlyVideoBroadcastEventDelegate videoBroadcastAdapter = new TreadlyVideoBroadcastEventAdapter() { // from class: com.treadly.Treadly.UI.TreadlyVideo.PublicStream.PublicStreamViewer.TreadlyPublicStreamViewerFragment.16
        @Override // com.treadly.Treadly.Data.Delegates.TreadlyVideoBroadcastEventAdapter, com.treadly.Treadly.Data.Delegates.TreadlyVideoBroadcastEventDelegate
        public void onUserJoinVideoBroadcast(String str, final UserInfo userInfo) {
            final UserComment userComment = new UserComment(userInfo, "Joined");
            ActivityUtil.runOnUiThread(TreadlyPublicStreamViewerFragment.this.getActivity(), new Runnable() { // from class: com.treadly.Treadly.UI.TreadlyVideo.PublicStream.PublicStreamViewer.TreadlyPublicStreamViewerFragment.16.1
                @Override // java.lang.Runnable
                public void run() {
                    TreadlyPublicStreamViewerFragment.this.commentController.addMessage(userComment);
                    TreadlyPublicStreamViewerFragment.this.userJoined(userInfo);
                }
            });
        }

        @Override // com.treadly.Treadly.Data.Delegates.TreadlyVideoBroadcastEventAdapter, com.treadly.Treadly.Data.Delegates.TreadlyVideoBroadcastEventDelegate
        public void onUserLeaveVideoBroadcast(String str, final UserInfo userInfo) {
            final UserComment userComment = new UserComment(userInfo, "Left");
            ActivityUtil.runOnUiThread(TreadlyPublicStreamViewerFragment.this.getActivity(), new Runnable() { // from class: com.treadly.Treadly.UI.TreadlyVideo.PublicStream.PublicStreamViewer.TreadlyPublicStreamViewerFragment.16.2
                @Override // java.lang.Runnable
                public void run() {
                    if (TreadlyPublicStreamViewerFragment.this.commentController == null) {
                        return;
                    }
                    TreadlyPublicStreamViewerFragment.this.commentController.addMessage(userComment);
                    TreadlyPublicStreamViewerFragment.this.userLeft(userInfo);
                }
            });
        }

        @Override // com.treadly.Treadly.Data.Delegates.TreadlyVideoBroadcastEventAdapter, com.treadly.Treadly.Data.Delegates.TreadlyVideoBroadcastEventDelegate
        public void onReceiveVideoBroadcastUserComment(String str, final UserComment userComment) {
            ActivityUtil.runOnUiThread(TreadlyPublicStreamViewerFragment.this.getActivity(), new Runnable() { // from class: com.treadly.Treadly.UI.TreadlyVideo.PublicStream.PublicStreamViewer.TreadlyPublicStreamViewerFragment.16.3
                @Override // java.lang.Runnable
                public void run() {
                    TreadlyPublicStreamViewerFragment.this.commentController.addMessage(userComment);
                }
            });
        }

        @Override // com.treadly.Treadly.Data.Delegates.TreadlyVideoBroadcastEventAdapter, com.treadly.Treadly.Data.Delegates.TreadlyVideoBroadcastEventDelegate
        public void onJoinedVideoBroadcast(String str) {
            TreadlyPublicStreamViewerFragment.this.getInitialViewerInformation();
        }

        @Override // com.treadly.Treadly.Data.Delegates.TreadlyVideoBroadcastEventAdapter, com.treadly.Treadly.Data.Delegates.TreadlyVideoBroadcastEventDelegate
        public void onReceiveVideoBroadcastUserStats(String str, VideoServiceUserStatsInfo videoServiceUserStatsInfo) {
            if (TreadlyPublicStreamViewerFragment.this.hostInfo == null || videoServiceUserStatsInfo == null || videoServiceUserStatsInfo.userId == null || !videoServiceUserStatsInfo.userId.equals(TreadlyPublicStreamViewerFragment.this.userId)) {
                return;
            }
            TreadlyPublicStreamViewerFragment.this.updateHostStats(videoServiceUserStatsInfo);
        }

        @Override // com.treadly.Treadly.Data.Delegates.TreadlyVideoBroadcastEventAdapter, com.treadly.Treadly.Data.Delegates.TreadlyVideoBroadcastEventDelegate
        public void onReceiveVideoBroadcastLike(String str, final int i) {
            if (TreadlyPublicStreamViewerFragment.this.broadcastInfo == null || TreadlyPublicStreamViewerFragment.this.broadcastInfo.broadcastId == null) {
                return;
            }
            ActivityUtil.runOnUiThread(TreadlyPublicStreamViewerFragment.this.getActivity(), new Runnable() { // from class: com.treadly.Treadly.UI.TreadlyVideo.PublicStream.PublicStreamViewer.TreadlyPublicStreamViewerFragment.16.4
                @Override // java.lang.Runnable
                public void run() {
                    TreadlyPublicStreamViewerFragment.this.commentController.setLikes(i);
                }
            });
        }
    };
    private TreadlyConnectStartState startState = TreadlyConnectStartState.notConnected;
    private DeviceConnectionStatus connectState = DeviceConnectionStatus.notConnected;
    private boolean isDeviceConnected = false;
    private AuthenticationState authenticationState = AuthenticationState.unknown;
    public ComponentInfo[] componentList = new ComponentInfo[0];
    private TreadlyVideoSliderFragment.TreadlyVideoSliderFragmentEventListener sliderAdapater = new TreadlyVideoSliderFragment.TreadlyVideoSliderFragmentEventListener() { // from class: com.treadly.Treadly.UI.TreadlyVideo.PublicStream.PublicStreamViewer.TreadlyPublicStreamViewerFragment.20
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
            if (TreadlyPublicStreamViewerFragment.this.treadlyControl != null) {
                TreadlyPublicStreamViewerFragment.this.treadlyControl.pause();
            }
        }

        @Override // com.treadly.Treadly.UI.TreadlyVideo.TreadlyVideoSliderFragment.TreadlyVideoSliderFragmentEventListener
        public void onSetSpeed(float f) {
            Log.i("WG", "onSetSpeed: " + f);
            if (TreadlyPublicStreamViewerFragment.this.treadlyControl != null) {
                TreadlyPublicStreamViewerFragment.this.treadlyControl.setSpeed(f);
            }
        }
    };
    private DeviceConnectionEventListener deviceConnectionListener = new DeviceConnectionEventListener() { // from class: com.treadly.Treadly.UI.TreadlyVideo.PublicStream.PublicStreamViewer.TreadlyPublicStreamViewerFragment.21
        @Override // com.treadly.client.lib.sdk.Listeners.DeviceConnectionEventListener
        public void onDeviceConnectionChanged(DeviceConnectionEvent deviceConnectionEvent) {
        }

        @Override // com.treadly.client.lib.sdk.Listeners.DeviceConnectionEventListener
        public void onDeviceConnectionDeviceDiscovered(DeviceInfo deviceInfo) {
        }
    };
    private List<UserInfo> viewerList = new ArrayList();

    /* loaded from: classes2.dex */
    public interface PublicStreamViewerListener {
        void streamDidEnd();
    }

    private int getScreenOrientation(int i, int i2) {
        return i2 == 2 ? (i == 0 || i == 1) ? 0 : 8 : (i == 0 || i == 3) ? 1 : 9;
    }

    @Override // androidx.fragment.app.Fragment
    public void onDestroy() {
        super.onDestroy();
        TreadlyEventManager.getInstance().leaveVideoBroadcast(this.broadcastInfo.broadcastId);
        TreadlyEventManager.getInstance().removeVideoBroadcastDelegate(this.videoBroadcastAdapter);
        showBottomNavigation();
    }

    @Override // androidx.fragment.app.Fragment
    public void onPause() {
        super.onPause();
        if (this.broadcastPlayer != null) {
            this.broadcastPlayer.close();
        }
        this.broadcastPlayer = null;
    }

    @Override // androidx.fragment.app.Fragment
    public void onResume() {
        super.onResume();
        hideBottomNavigation();
        if (this.broadcastInfo == null) {
            return;
        }
        initBambuserVideo(this.broadcastInfo.broadcastUrl);
    }

    @Override // androidx.fragment.app.Fragment
    @Nullable
    public View onCreateView(@NonNull LayoutInflater layoutInflater, @Nullable ViewGroup viewGroup, @Nullable Bundle bundle) {
        hideBottomNavigation();
        initController();
        return layoutInflater.inflate(R.layout.fragment_treadly_video_viewer_broadcast, viewGroup, false);
    }

    @Override // com.treadly.Treadly.UI.Util.BaseClasses.BaseFragment, androidx.fragment.app.Fragment
    public void onViewCreated(@NonNull View view, @Nullable Bundle bundle) {
        this.viewerFragmentView = view;
        getStreamInfo();
        initCommentController();
        refreshUsers();
        this.leaveButton = (AppCompatImageButton) view.findViewById(R.id.end_workout_button);
        this.leaveButton.setOnClickListener(new View.OnClickListener() { // from class: com.treadly.Treadly.UI.TreadlyVideo.PublicStream.PublicStreamViewer.TreadlyPublicStreamViewerFragment.1
            @Override // android.view.View.OnClickListener
            public void onClick(View view2) {
                TreadlyPublicStreamViewerFragment.this.getActivity().getSupportFragmentManager().popBackStack(TreadlyConnectFragment.TAG, 1);
            }
        });
        initTreadmillControl(view);
        initHostStatsInfoView(view);
    }

    public void refreshUsers() {
        TreadlyServiceManager.getInstance().refreshUsersInfo(new TreadlyServiceResponseEventAdapter() { // from class: com.treadly.Treadly.UI.TreadlyVideo.PublicStream.PublicStreamViewer.TreadlyPublicStreamViewerFragment.2
            @Override // com.treadly.Treadly.Data.Listeners.TreadlyServiceResponseEventAdapter, com.treadly.Treadly.Data.Listeners.TreadlyServiceResponseEventListener
            public void onSuccess(String str) {
                UserInfo userInfoById;
                if (str != null || (userInfoById = TreadlyServiceManager.getInstance().getUserInfoById(TreadlyPublicStreamViewerFragment.this.userId)) == null) {
                    return;
                }
                TreadlyPublicStreamViewerFragment.this.hostUser = userInfoById;
            }
        });
    }

    public void getStreamInfo() {
        VideoServiceHelper.getSessionInfoByUser(this.userId, new VideoServiceHelper.VideoServiceSessionInfoListener() { // from class: com.treadly.Treadly.UI.TreadlyVideo.PublicStream.PublicStreamViewer.TreadlyPublicStreamViewerFragment.3
            @Override // com.treadly.Treadly.UI.TreadlyVideo.Data.VideoServiceHelper.VideoServiceSessionInfoListener
            public void onResponse(String str, VideoServiceSessionInfo videoServiceSessionInfo) {
                if (str != null || videoServiceSessionInfo == null) {
                    return;
                }
                TreadlyPublicStreamViewerFragment.this.broadcastInfo = videoServiceSessionInfo;
                if (TreadlyPublicStreamViewerFragment.this.broadcastInfo.broadcastId != null) {
                    TreadlyEventManager.getInstance().joinVideoBroadcast(TreadlyPublicStreamViewerFragment.this.broadcastInfo.broadcastId);
                    TreadlyEventManager.getInstance().addVideoBroadcastDelegate(TreadlyPublicStreamViewerFragment.this.videoBroadcastAdapter);
                    TreadlyPublicStreamViewerFragment.this.fetchVideoLike(TreadlyPublicStreamViewerFragment.this.broadcastInfo.broadcastId);
                }
                if (TreadlyPublicStreamViewerFragment.this.broadcastInfo.broadcastUrl != null) {
                    TreadlyPublicStreamViewerFragment.this.initBambuserVideo(TreadlyPublicStreamViewerFragment.this.broadcastInfo.broadcastUrl);
                }
            }
        });
    }

    void fetchVideoLike(String str) {
        VideoServiceHelper.getVideoBroadcastLikeInfo(str, new VideoServiceHelper.VideoLikeListener() { // from class: com.treadly.Treadly.UI.TreadlyVideo.PublicStream.PublicStreamViewer.TreadlyPublicStreamViewerFragment.4
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
                ActivityUtil.runOnUiThread(TreadlyPublicStreamViewerFragment.this.getActivity(), new Runnable() { // from class: com.treadly.Treadly.UI.TreadlyVideo.PublicStream.PublicStreamViewer.TreadlyPublicStreamViewerFragment.4.1
                    @Override // java.lang.Runnable
                    public void run() {
                        TreadlyPublicStreamViewerFragment.this.setVideoLikeInfo(videoLikeInfo);
                    }
                });
            }
        });
    }

    public void initBambuserVideo(String str) {
        this.publicBroadcastSurface = (SurfaceViewWithAutoAR) this.viewerFragmentView.findViewById(R.id.viewer_public_broadcast);
        this.publicBroadcastSurface.setCropToParent(false);
        if (this.broadcastPlayer != null) {
            this.broadcastPlayer.close();
        }
        this.broadcastPlayer = new BroadcastPlayer(getContext(), str, NetworkConstants.SERVICE_BAMBUSER_APPLICATION_ID, this.broadcastPlayerObserver);
        this.broadcastPlayer.setSurfaceView(this.publicBroadcastSurface);
        lockCurrentOrientation();
        this.broadcastPlayer.load();
    }

    private void initCommentController() {
        if (this.allowComments) {
            this.commentController = new TreadlyPublicStreamMessagingViewController(getContext(), this.viewerFragmentView, false);
            this.commentController.isStreamer = true;
            this.commentController.commentControllerListener = new TreadlyPublicStreamMessagingViewController.CommentControllerListener() { // from class: com.treadly.Treadly.UI.TreadlyVideo.PublicStream.PublicStreamViewer.TreadlyPublicStreamViewerFragment.5
                @Override // com.treadly.Treadly.UI.TreadlyVideo.PublicStream.PublicStreamMessagingView.TreadlyPublicStreamMessagingViewController.CommentControllerListener
                public void didPressLikeCount() {
                }

                @Override // com.treadly.Treadly.UI.TreadlyVideo.PublicStream.PublicStreamMessagingView.TreadlyPublicStreamMessagingViewController.CommentControllerListener
                public void didStartTyping() {
                    TreadlyPublicStreamViewerFragment.this.isTyping = true;
                }

                @Override // com.treadly.Treadly.UI.TreadlyVideo.PublicStream.PublicStreamMessagingView.TreadlyPublicStreamMessagingViewController.CommentControllerListener
                public void didEndTyping() {
                    TreadlyPublicStreamViewerFragment.this.isTyping = false;
                }

                @Override // com.treadly.Treadly.UI.TreadlyVideo.PublicStream.PublicStreamMessagingView.TreadlyPublicStreamMessagingViewController.CommentControllerListener
                public void didPressLike() {
                    TreadlyPublicStreamViewerFragment.this.handleStreamLiked();
                }

                @Override // com.treadly.Treadly.UI.TreadlyVideo.PublicStream.PublicStreamMessagingView.TreadlyPublicStreamMessagingViewController.CommentControllerListener
                public void didSendMessage(String str) {
                    UserInfo userInfo;
                    if (TreadlyPublicStreamViewerFragment.this.broadcastInfo.broadcastId == null || (userInfo = TreadlyServiceManager.getInstance().getUserInfo()) == null) {
                        return;
                    }
                    TreadlyPublicStreamViewerFragment.this.commentController.addMessage(new UserComment(userInfo, str));
                    TreadlyEventManager.getInstance().sendVideoBroadcastComment(TreadlyPublicStreamViewerFragment.this.broadcastInfo.broadcastId, str);
                }
            };
            ActivityUtil.runOnUiThread(getActivity(), new Runnable() { // from class: com.treadly.Treadly.UI.TreadlyVideo.PublicStream.PublicStreamViewer.TreadlyPublicStreamViewerFragment.6
                @Override // java.lang.Runnable
                public void run() {
                    TreadlyPublicStreamViewerFragment.this.setVideoLikeInfo(TreadlyPublicStreamViewerFragment.this.streamLikeInfo);
                }
            });
        }
    }

    void handleVideoLikeCountPressed() {
        if (this.broadcastInfo.broadcastId == null) {
            return;
        }
        this.videoLikesSequence++;
        final int i = this.videoLikesSequence;
        VideoServiceHelper.getVideoBroadcastLikeUserInfo(this.broadcastInfo.broadcastId, new VideoServiceHelper.VideoLikeListener() { // from class: com.treadly.Treadly.UI.TreadlyVideo.PublicStream.PublicStreamViewer.TreadlyPublicStreamViewerFragment.7
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
                if (str != null || list == null || list.isEmpty() || TreadlyPublicStreamViewerFragment.this.videoLikesSequence != i) {
                    return;
                }
                TreadlyPublicStreamViewerFragment.this.displayVideoLikesUserList(list);
            }
        });
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

    void getInitialViewerInformation() {
        if (this.broadcastInfo.broadcastId == null) {
            return;
        }
        BroadcastViewerServiceHelper.getViewerComments(this.broadcastInfo.broadcastId, new BroadcastViewerServiceHelper.BroadcastViewerServiceHelperListener() { // from class: com.treadly.Treadly.UI.TreadlyVideo.PublicStream.PublicStreamViewer.TreadlyPublicStreamViewerFragment.8
            @Override // com.treadly.Treadly.UI.TreadlyVideo.Data.BroadcastViewerServiceHelper.BroadcastViewerServiceHelperListener
            public void onGetViewers(String str, List<UserInfo> list) {
            }

            @Override // com.treadly.Treadly.UI.TreadlyVideo.Data.BroadcastViewerServiceHelper.BroadcastViewerServiceHelperListener
            public void onViewerUpdate(String str, List<String> list, Boolean bool) {
            }

            @Override // com.treadly.Treadly.UI.TreadlyVideo.Data.BroadcastViewerServiceHelper.BroadcastViewerServiceHelperListener
            public void onGetViewerComments(String str, final List<UserComment> list) {
                if (str != null || list == null) {
                    return;
                }
                ActivityUtil.runOnUiThread(TreadlyPublicStreamViewerFragment.this.getActivity(), new Runnable() { // from class: com.treadly.Treadly.UI.TreadlyVideo.PublicStream.PublicStreamViewer.TreadlyPublicStreamViewerFragment.8.1
                    @Override // java.lang.Runnable
                    public void run() {
                        TreadlyPublicStreamViewerFragment.this.commentController.initializeMessages(list);
                    }
                });
            }
        });
        BroadcastViewerServiceHelper.getViewers(this.broadcastInfo.broadcastId, new BroadcastViewerServiceHelper.BroadcastViewerServiceHelperListener() { // from class: com.treadly.Treadly.UI.TreadlyVideo.PublicStream.PublicStreamViewer.TreadlyPublicStreamViewerFragment.9
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
                TreadlyPublicStreamViewerFragment.this.setInitialViewers(list);
            }
        });
    }

    void setInitialViewers(List<UserInfo> list) {
        this.viewersLookup.clear();
        for (UserInfo userInfo : list) {
            if (this.viewersLeft.get(userInfo.id) == null) {
                this.viewersLookup.put(userInfo.id, userInfo);
            }
        }
        list.addAll(this.viewersLookup.values());
    }

    void userJoined(UserInfo userInfo) {
        this.viewersLookup.put(userInfo.id, userInfo);
        this.viewersLeft.remove(userInfo.id);
        this.viewers.addAll(this.viewersLeft.values());
        updateHostInfo(this.hostInfo);
    }

    void userLeft(UserInfo userInfo) {
        this.viewersLookup.put(userInfo.id, userInfo);
        this.viewersLeft.remove(userInfo.id);
        this.viewers.addAll(this.viewersLeft.values());
        updateHostInfo(this.hostInfo);
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

    void handleStreamLiked() {
        if (this.streamLikeInfo == null || this.streamLikeInfo.isLike == null || this.broadcastInfo == null) {
            return;
        }
        if (this.streamLikeInfo.isLike.booleanValue()) {
            VideoServiceHelper.deleteVideoBroadcastLike(this.broadcastInfo.broadcastId, new VideoServiceHelper.VideoResponseListener() { // from class: com.treadly.Treadly.UI.TreadlyVideo.PublicStream.PublicStreamViewer.TreadlyPublicStreamViewerFragment.10
                @Override // com.treadly.Treadly.UI.TreadlyVideo.Data.VideoServiceHelper.VideoResponseListener
                public void onResponse(String str) {
                    if (str == null) {
                        TreadlyPublicStreamViewerFragment.this.fetchVideoLike(TreadlyPublicStreamViewerFragment.this.broadcastInfo.broadcastId);
                    }
                }
            });
        } else {
            VideoServiceHelper.createVideoBroadcastLike(this.broadcastInfo.broadcastId, new VideoServiceHelper.VideoResponseListener() { // from class: com.treadly.Treadly.UI.TreadlyVideo.PublicStream.PublicStreamViewer.TreadlyPublicStreamViewerFragment.11
                @Override // com.treadly.Treadly.UI.TreadlyVideo.Data.VideoServiceHelper.VideoResponseListener
                public void onResponse(String str) {
                    if (str == null) {
                        TreadlyPublicStreamViewerFragment.this.fetchVideoLike(TreadlyPublicStreamViewerFragment.this.broadcastInfo.broadcastId);
                    }
                }
            });
        }
        ActivityUtil.runOnUiThread(getActivity(), new Runnable() { // from class: com.treadly.Treadly.UI.TreadlyVideo.PublicStream.PublicStreamViewer.TreadlyPublicStreamViewerFragment.12
            @Override // java.lang.Runnable
            public void run() {
                TreadlyPublicStreamViewerFragment.this.commentController.setIsLiked(!TreadlyPublicStreamViewerFragment.this.streamLikeInfo.isLike.booleanValue(), true);
            }
        });
    }

    void displayEndWorkoutConfirmation() {
        if (getActivity() != null) {
            getActivity().getSupportFragmentManager().popBackStack();
        }
    }

    void displayHostEndedAlert() {
        if (this.userId.equals(TreadlyServiceManager.getInstance().getUserId())) {
            return;
        }
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        if (this.hostUser.name != null) {
            builder.setMessage(this.hostUser.name + " has ended the public workout.");
        } else {
            builder.setMessage("The host has ended the public workout.");
        }
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() { // from class: com.treadly.Treadly.UI.TreadlyVideo.PublicStream.PublicStreamViewer.TreadlyPublicStreamViewerFragment.14
            @Override // android.content.DialogInterface.OnClickListener
            public void onClick(final DialogInterface dialogInterface, int i) {
                ActivityUtil.runOnUiThread(TreadlyPublicStreamViewerFragment.this.getActivity(), new Runnable() { // from class: com.treadly.Treadly.UI.TreadlyVideo.PublicStream.PublicStreamViewer.TreadlyPublicStreamViewerFragment.14.1
                    @Override // java.lang.Runnable
                    public void run() {
                        if (!TreadlyPublicStreamViewerFragment.this.isLeaving || TreadlyPublicStreamViewerFragment.this.hostEnded) {
                            TreadlyPublicStreamViewerFragment.this.getActivity().getSupportFragmentManager().popBackStack();
                            return;
                        }
                        dialogInterface.dismiss();
                        TreadlyPublicStreamViewerFragment.this.isLeaving = false;
                        TreadlyPublicStreamViewerFragment.this.displayEndWorkoutConfirmation();
                    }
                });
            }
        });
        builder.setOnDismissListener(new DialogInterface.OnDismissListener() { // from class: com.treadly.Treadly.UI.TreadlyVideo.PublicStream.PublicStreamViewer.TreadlyPublicStreamViewerFragment.15
            @Override // android.content.DialogInterface.OnDismissListener
            public void onDismiss(DialogInterface dialogInterface) {
                dialogInterface.dismiss();
                TreadlyPublicStreamViewerFragment.this.isLeaving = false;
                TreadlyPublicStreamViewerFragment.this.displayEndWorkoutConfirmation();
            }
        });
        builder.show();
    }

    private void lockCurrentOrientation() {
        getActivity().setRequestedOrientation(getScreenOrientation(getActivity().getWindowManager().getDefaultDisplay().getRotation(), getResources().getConfiguration().orientation));
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
        this.sliderFragment.parentFragmentPublicViewer = this;
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
        this.speedUpButton.setOnClickListener(new View.OnClickListener() { // from class: com.treadly.Treadly.UI.TreadlyVideo.PublicStream.PublicStreamViewer.TreadlyPublicStreamViewerFragment.17
            @Override // android.view.View.OnClickListener
            public void onClick(View view2) {
                TreadlyPublicStreamViewerFragment.this.treadlyControl.speedUp();
            }
        });
        this.speedDownButton = (Button) view.findViewById(R.id.public_speed_down);
        this.speedDownButton.setOnClickListener(new View.OnClickListener() { // from class: com.treadly.Treadly.UI.TreadlyVideo.PublicStream.PublicStreamViewer.TreadlyPublicStreamViewerFragment.18
            @Override // android.view.View.OnClickListener
            public void onClick(View view2) {
                Log.i("TreadmillController", "in speeddown");
                TreadlyPublicStreamViewerFragment.this.treadlyControl.speedDown();
            }
        });
    }

    private void initController() {
        this.treadlyContent = new TreadlyControlContent();
        this.treadlyControl = new TreadlyTreadmillController(this.treadlyContent);
        this.treadlyControl.adapter = new TreadlyTreadmillController.TreadlyTreadmillAdapter() { // from class: com.treadly.Treadly.UI.TreadlyVideo.PublicStream.PublicStreamViewer.TreadlyPublicStreamViewerFragment.19
            @Override // com.treadly.Treadly.UI.TreadmillControl.TreadlyTreadmillController.TreadlyTreadmillAdapter
            public void didChangeDeviceConnection(boolean z) {
            }

            @Override // com.treadly.Treadly.UI.TreadmillControl.TreadlyTreadmillController.TreadlyTreadmillAdapter
            public void didChangeStatus(DeviceStatus deviceStatus) {
                if (TreadlyPublicStreamViewerFragment.this.authenticationState == AuthenticationState.active) {
                }
            }

            @Override // com.treadly.Treadly.UI.TreadmillControl.TreadlyTreadmillController.TreadlyTreadmillAdapter
            public void didChangeContent(TreadlyControlContent treadlyControlContent) {
                TreadlyPublicStreamViewerFragment.this.authenticationState = treadlyControlContent.authenticationState;
                if (treadlyControlContent.authenticationState == AuthenticationState.active) {
                    TreadlyPublicStreamViewerFragment.this.sliderFragment.currentDeviceStatusInfo = TreadlyPublicStreamViewerFragment.this.treadlyControl.status;
                    if (!TreadlyPublicStreamViewerFragment.this.sliderFragment.isPanning) {
                        TreadlyPublicStreamViewerFragment.this.sliderFragment.setSpeedLabels((float) treadlyControlContent.speedValue);
                    }
                    TreadlyPublicStreamViewerFragment.this.sendCurrentUserStats(treadlyControlContent);
                } else if (treadlyControlContent.authenticationState == AuthenticationState.unknown) {
                    TreadlyPublicStreamViewerFragment.this.treadlyControl.setAuthenticationState();
                }
            }
        };
    }

    private void initHostStatsInfoView(View view) {
        this.hostStatsInfoView = (TreadlyPublicStreamUserStatsInfoView) view.findViewById(R.id.host_info_view);
        this.userStatsInfoView = (TreadlyPublicStreamUserStatsInfoView) view.findViewById(R.id.user_info_view);
        updateHostInfo(null);
        updateUserInfo(null);
        getHostInfo();
    }

    private void getHostInfo() {
        TreadlyServiceManager.getInstance().refreshUsersInfo(new TreadlyServiceResponseEventAdapter() { // from class: com.treadly.Treadly.UI.TreadlyVideo.PublicStream.PublicStreamViewer.TreadlyPublicStreamViewerFragment.22
            @Override // com.treadly.Treadly.Data.Listeners.TreadlyServiceResponseEventAdapter, com.treadly.Treadly.Data.Listeners.TreadlyServiceResponseEventListener
            public void onSuccess(String str) {
                if (str == null) {
                    TreadlyPublicStreamViewerFragment.this.hostInfo = TreadlyServiceManager.getInstance().getUserInfoById(TreadlyPublicStreamViewerFragment.this.userId);
                }
                TreadlyPublicStreamViewerFragment.this.userInfo = TreadlyServiceManager.getInstance().getUserInfo();
                TreadlyPublicStreamViewerFragment.this.getInitialViewers();
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void getInitialViewers() {
        if (this.broadcastInfo == null || this.broadcastInfo.broadcastId == null) {
            return;
        }
        BroadcastViewerServiceHelper.getViewers(this.broadcastInfo.broadcastId, new BroadcastViewerServiceHelper.BroadcastViewerServiceHelperAdapter() { // from class: com.treadly.Treadly.UI.TreadlyVideo.PublicStream.PublicStreamViewer.TreadlyPublicStreamViewerFragment.23
            @Override // com.treadly.Treadly.UI.TreadlyVideo.Data.BroadcastViewerServiceHelper.BroadcastViewerServiceHelperAdapter, com.treadly.Treadly.UI.TreadlyVideo.Data.BroadcastViewerServiceHelper.BroadcastViewerServiceHelperListener
            public void onGetViewers(String str, List<UserInfo> list) {
                if (str == null && list != null) {
                    TreadlyPublicStreamViewerFragment.this.viewerList = list;
                }
                TreadlyPublicStreamViewerFragment.this.updateHostInfo(TreadlyPublicStreamViewerFragment.this.userInfo);
            }
        });
        updateHostInfo(this.hostInfo);
        updateUserInfo(this.userInfo);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void sendCurrentUserStats(TreadlyControlContent treadlyControlContent) {
        final VideoServiceUserStatsInfo videoServiceUserStatsInfo = new VideoServiceUserStatsInfo(TreadlyServiceManager.getInstance().getUserId(), TreadlyServiceManager.getInstance().getName(), RunningSessionManager.getInstance().calories, treadlyControlContent.walkDistance, treadlyControlContent.stepsCount, treadlyControlContent.speedValue, treadlyControlContent.averageSpeed, treadlyControlContent.speedDataSet, treadlyControlContent.speedUnits, treadlyControlContent.elapsedTime);
        ActivityUtil.runOnUiThread(getActivity(), new Runnable() { // from class: com.treadly.Treadly.UI.TreadlyVideo.PublicStream.PublicStreamViewer.TreadlyPublicStreamViewerFragment.24
            @Override // java.lang.Runnable
            public void run() {
                TreadlyPublicStreamViewerFragment.this.updateUserStats(videoServiceUserStatsInfo);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void updateHostInfo(UserInfo userInfo) {
        int size = this.viewerList.size();
        if (userInfo != null) {
            this.hostStatsInfoView.setHostInfo(userInfo, size);
            this.hostStatsInfoView.setVisibility(0);
            return;
        }
        this.hostStatsInfoView.setVisibility(8);
    }

    private void updateUserInfo(UserInfo userInfo) {
        if (userInfo != null) {
            this.userStatsInfoView.setUserInfo(userInfo);
            this.userStatsInfoView.setVisibility(0);
            return;
        }
        this.userStatsInfoView.setVisibility(8);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void updateHostStats(final VideoServiceUserStatsInfo videoServiceUserStatsInfo) {
        ActivityUtil.runOnUiThread(getActivity(), new Runnable() { // from class: com.treadly.Treadly.UI.TreadlyVideo.PublicStream.PublicStreamViewer.-$$Lambda$TreadlyPublicStreamViewerFragment$z26lnyUaviBuKIO6bzZYeXTX4fU
            @Override // java.lang.Runnable
            public final void run() {
                TreadlyPublicStreamViewerFragment.this.hostStatsInfoView.setHostStats(videoServiceUserStatsInfo);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void updateUserStats(VideoServiceUserStatsInfo videoServiceUserStatsInfo) {
        this.userStatsInfoView.setUserStats(videoServiceUserStatsInfo);
    }

    private void handleUserJoined(UserInfo userInfo) {
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

    private void handleUserLeft(UserInfo userInfo) {
        for (UserInfo userInfo2 : new ArrayList(this.viewerList)) {
            if (userInfo2.id != null && userInfo != null && userInfo.id != null && userInfo.id.equals(userInfo2.id)) {
                this.viewerList.remove(userInfo2);
                return;
            }
        }
    }
}
