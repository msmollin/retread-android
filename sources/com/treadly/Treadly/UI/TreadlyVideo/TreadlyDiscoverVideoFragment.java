package com.treadly.Treadly.UI.TreadlyVideo;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Point;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.VideoView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.PopupMenu;
import androidx.fragment.app.FragmentActivity;
import com.bambuser.broadcaster.BroadcastPlayer;
import com.bambuser.broadcaster.PlayerState;
import com.bambuser.broadcaster.SurfaceViewWithAutoAR;
import com.squareup.picasso.Picasso;
import com.treadly.Treadly.Data.Managers.TreadlyEventManager;
import com.treadly.Treadly.Data.Managers.TreadlyVideoArchiveEventManager;
import com.treadly.Treadly.Data.Model.UserComment;
import com.treadly.Treadly.Data.Model.UserInfo;
import com.treadly.Treadly.Data.Model.VideoLikeInfo;
import com.treadly.Treadly.Data.Model.VideoLikeUserInfo;
import com.treadly.Treadly.R;
import com.treadly.Treadly.UI.TreadlyProfile.ReportUser.TreadlyProfileReportContentFragment;
import com.treadly.Treadly.UI.TreadlyProfile.ReportUser.TreadlyReportCategory;
import com.treadly.Treadly.UI.TreadlyVideo.Data.BroadcastViewerServiceHelper;
import com.treadly.Treadly.UI.TreadlyVideo.Data.VideoServiceHelper;
import com.treadly.Treadly.UI.TreadlyVideo.Data.VideoServiceVideoInfo;
import com.treadly.Treadly.UI.TreadlyVideo.PublicStream.PublicStreamMessagingView.TreadlyPublicStreamMessagingViewController;
import com.treadly.Treadly.UI.Util.ActivityUtil;
import com.treadly.Treadly.UI.Util.BaseClasses.BaseFragment;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.TimeZone;
import java.util.Timer;
import java.util.TimerTask;

/* loaded from: classes2.dex */
public class TreadlyDiscoverVideoFragment extends BaseFragment {
    public static final String TAG = "TREADLY_VIDEO_DISCOVER";
    public VideoServiceVideoInfo archiveInfo;
    public VideoLikeInfo archiveLikeInfo;
    BroadcastPlayer broadcastPlayer;
    TreadlyPublicStreamMessagingViewController commentController;
    List<UserComment> commentList;
    List<UserComment> commentListComplete;
    Timer commentTimer;
    ImageButton deleteButton;
    TextView deleteLabel;
    public DeleteVideoListener deleteVideoListener;
    View fragmentView;
    public boolean isCurrentUser;
    public boolean isPublic;
    private MediaPlayer.OnPreparedListener listener;
    Display mDefaultDisplay;
    private MediaController mc;
    ImageButton reportButton;
    SurfaceViewWithAutoAR surfaceView;
    public Uri thumbnail;
    public Uri uri;
    private VideoView videoView;
    BroadcastPlayer.Observer broadcastObserver = new BroadcastPlayer.Observer() { // from class: com.treadly.Treadly.UI.TreadlyVideo.TreadlyDiscoverVideoFragment.3
        @Override // com.bambuser.broadcaster.BroadcastPlayer.Observer
        public void onStateChange(PlayerState playerState) {
        }

        @Override // com.bambuser.broadcaster.BroadcastPlayer.Observer
        public void onBroadcastLoaded(boolean z, int i, int i2) {
            ((ImageView) TreadlyDiscoverVideoFragment.this.fragmentView.findViewById(R.id.video_player_placeholder)).setVisibility(8);
            Point screenSize = TreadlyDiscoverVideoFragment.this.getScreenSize();
            TreadlyDiscoverVideoFragment.this.surfaceView.setCropToParent(((double) Math.abs((((float) screenSize.x) / ((float) screenSize.y)) - (((float) i) / ((float) i2)))) < 0.2d);
        }
    };
    int videoLikesSequence = 0;
    int lastPosition = 0;

    /* loaded from: classes2.dex */
    public interface DeleteVideoListener {
        void didDeleteVideo();
    }

    boolean canDisplayComments() {
        return true;
    }

    @Override // androidx.fragment.app.Fragment
    public void onResume() {
        super.onResume();
        hideBottomNavigation();
        initVideoView();
        initComment();
    }

    @Override // androidx.fragment.app.Fragment
    public void onDestroy() {
        super.onDestroy();
        showBottomNavigation();
    }

    @Override // androidx.fragment.app.Fragment
    public void onPause() {
        super.onPause();
        handleOnVideoPause();
    }

    @Override // androidx.fragment.app.Fragment
    @Nullable
    public View onCreateView(@NonNull LayoutInflater layoutInflater, @Nullable ViewGroup viewGroup, @Nullable Bundle bundle) {
        this.mDefaultDisplay = ((FragmentActivity) Objects.requireNonNull(getActivity())).getWindowManager().getDefaultDisplay();
        return layoutInflater.inflate(R.layout.fragment_treadly_video_player, viewGroup, false);
    }

    @Override // com.treadly.Treadly.UI.Util.BaseClasses.BaseFragment, androidx.fragment.app.Fragment
    public void onViewCreated(@NonNull View view, @Nullable Bundle bundle) {
        this.fragmentView = view;
        initCommentController();
        this.deleteButton = (ImageButton) view.findViewById(R.id.discover_video_delete_button);
        this.deleteLabel = (TextView) view.findViewById(R.id.discover_video_delete_label);
        this.reportButton = (ImageButton) view.findViewById(R.id.discover_video_report_button);
        if (!this.isCurrentUser) {
            this.reportButton.setOnClickListener(new View.OnClickListener() { // from class: com.treadly.Treadly.UI.TreadlyVideo.TreadlyDiscoverVideoFragment.1
                @Override // android.view.View.OnClickListener
                public void onClick(View view2) {
                    TreadlyDiscoverVideoFragment.this.onReportPressed();
                }
            });
            this.deleteButton.setVisibility(8);
            this.deleteLabel.setVisibility(8);
            return;
        }
        this.reportButton.setVisibility(8);
        this.deleteButton.setOnClickListener(new View.OnClickListener() { // from class: com.treadly.Treadly.UI.TreadlyVideo.TreadlyDiscoverVideoFragment.2
            @Override // android.view.View.OnClickListener
            public void onClick(View view2) {
                TreadlyDiscoverVideoFragment.this.deletePressed();
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void onReportPressed() {
        PopupMenu reportPopup = getReportPopup(this.reportButton);
        reportPopup.inflate(R.menu.report_video_popup);
        reportPopup.show();
    }

    private PopupMenu getReportPopup(View view) {
        PopupMenu popupMenu = new PopupMenu(getContext(), view);
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() { // from class: com.treadly.Treadly.UI.TreadlyVideo.TreadlyDiscoverVideoFragment.4
            @Override // androidx.appcompat.widget.PopupMenu.OnMenuItemClickListener
            public boolean onMenuItemClick(MenuItem menuItem) {
                if (menuItem.getItemId() != R.id.profile_report_video_popup) {
                    return false;
                }
                TreadlyProfileReportContentFragment treadlyProfileReportContentFragment = new TreadlyProfileReportContentFragment();
                treadlyProfileReportContentFragment.contentId = TreadlyDiscoverVideoFragment.this.archiveInfo.id;
                treadlyProfileReportContentFragment.category = TreadlyReportCategory.video;
                TreadlyDiscoverVideoFragment.this.getActivity().getSupportFragmentManager().beginTransaction().addToBackStack(null).add(R.id.activity_fragment_container_empty, treadlyProfileReportContentFragment).commit();
                return true;
            }
        });
        return popupMenu;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public Point getScreenSize() {
        if (this.mDefaultDisplay == null && getActivity() != null) {
            this.mDefaultDisplay = getActivity().getWindowManager().getDefaultDisplay();
        }
        Point point = new Point();
        try {
            this.mDefaultDisplay.getClass().getMethod("getRealSize", Point.class).invoke(this.mDefaultDisplay, point);
        } catch (Exception unused) {
            this.mDefaultDisplay.getSize(point);
        }
        return point;
    }

    void hideVideoView() {
        this.videoView = (VideoView) this.fragmentView.findViewById(R.id.discover_video);
        this.videoView.setVisibility(8);
    }

    void initVideoView() {
        this.videoView = (VideoView) this.fragmentView.findViewById(R.id.discover_video);
        this.mc = new MediaController(getContext());
        this.mc.setAnchorView(this.videoView);
        this.mc.setMediaPlayer(this.videoView);
        this.videoView.setVideoURI(this.uri);
        this.videoView.setMediaController(this.mc);
        final ImageView imageView = (ImageView) this.fragmentView.findViewById(R.id.video_player_placeholder);
        Picasso.get().load(this.thumbnail).noFade().fit().into(imageView);
        this.listener = new MediaPlayer.OnPreparedListener() { // from class: com.treadly.Treadly.UI.TreadlyVideo.TreadlyDiscoverVideoFragment.5
            @Override // android.media.MediaPlayer.OnPreparedListener
            public void onPrepared(MediaPlayer mediaPlayer) {
                mediaPlayer.setOnInfoListener(new MediaPlayer.OnInfoListener() { // from class: com.treadly.Treadly.UI.TreadlyVideo.TreadlyDiscoverVideoFragment.5.1
                    @Override // android.media.MediaPlayer.OnInfoListener
                    public boolean onInfo(MediaPlayer mediaPlayer2, int i, int i2) {
                        Log.d(TreadlyDiscoverVideoFragment.TAG, "onInfo, what = " + i);
                        if (i == 3 && TreadlyDiscoverVideoFragment.this.videoView.isPlaying()) {
                            imageView.setVisibility(8);
                            return true;
                        }
                        return false;
                    }
                });
            }
        };
        TreadlyEventManager.getInstance().subscribeVideoArchive(this.archiveInfo.id);
        TreadlyEventManager.getInstance().addVideoArchiveDelegate(new TreadlyVideoArchiveEventManager.TreadlyVideoArchiveEventDelegate() { // from class: com.treadly.Treadly.UI.TreadlyVideo.TreadlyDiscoverVideoFragment.6
            @Override // com.treadly.Treadly.Data.Managers.TreadlyVideoArchiveEventManager.TreadlyVideoArchiveEventDelegate
            public void onReceiveVideoArchiveLike(String str, final Integer num) {
                if (TreadlyDiscoverVideoFragment.this.archiveInfo == null || TreadlyDiscoverVideoFragment.this.archiveInfo.id == null || !str.equals(TreadlyDiscoverVideoFragment.this.archiveInfo.id)) {
                    return;
                }
                ActivityUtil.runOnUiThread(TreadlyDiscoverVideoFragment.this.getActivity(), new Runnable() { // from class: com.treadly.Treadly.UI.TreadlyVideo.TreadlyDiscoverVideoFragment.6.1
                    @Override // java.lang.Runnable
                    public void run() {
                        TreadlyDiscoverVideoFragment.this.commentController.setLikes(num.intValue());
                    }
                });
            }
        });
        fetchVideoLike(this.archiveInfo.id, this.archiveInfo.serviceId);
        this.videoView.setOnPreparedListener(this.listener);
        this.videoView.start();
    }

    void initCommentController() {
        if (canDisplayComments()) {
            this.commentController = new TreadlyPublicStreamMessagingViewController(getContext(), this.fragmentView, null);
            this.commentController.readOnly = true;
            this.commentController.commentControllerListener = new TreadlyPublicStreamMessagingViewController.CommentControllerListener() { // from class: com.treadly.Treadly.UI.TreadlyVideo.TreadlyDiscoverVideoFragment.7
                @Override // com.treadly.Treadly.UI.TreadlyVideo.PublicStream.PublicStreamMessagingView.TreadlyPublicStreamMessagingViewController.CommentControllerListener
                public void didEndTyping() {
                }

                @Override // com.treadly.Treadly.UI.TreadlyVideo.PublicStream.PublicStreamMessagingView.TreadlyPublicStreamMessagingViewController.CommentControllerListener
                public void didSendMessage(String str) {
                }

                @Override // com.treadly.Treadly.UI.TreadlyVideo.PublicStream.PublicStreamMessagingView.TreadlyPublicStreamMessagingViewController.CommentControllerListener
                public void didStartTyping() {
                }

                @Override // com.treadly.Treadly.UI.TreadlyVideo.PublicStream.PublicStreamMessagingView.TreadlyPublicStreamMessagingViewController.CommentControllerListener
                public void didPressLike() {
                    TreadlyDiscoverVideoFragment.this.handleVideoLiked();
                }

                @Override // com.treadly.Treadly.UI.TreadlyVideo.PublicStream.PublicStreamMessagingView.TreadlyPublicStreamMessagingViewController.CommentControllerListener
                public void didPressLikeCount() {
                    TreadlyDiscoverVideoFragment.this.handleVideoLikeCountPressed();
                }
            };
        }
    }

    void handleVideoLikeCountPressed() {
        if (this.archiveInfo.id == null) {
            return;
        }
        this.videoLikesSequence++;
        final int i = this.videoLikesSequence;
        VideoServiceHelper.getVideoArchiveLikeUserInfo(this.archiveInfo.id, this.archiveInfo.serviceId, new VideoServiceHelper.VideoLikeListener() { // from class: com.treadly.Treadly.UI.TreadlyVideo.TreadlyDiscoverVideoFragment.8
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
                if (str != null || list == null || list.isEmpty() || TreadlyDiscoverVideoFragment.this.videoLikesSequence != i) {
                    return;
                }
                TreadlyDiscoverVideoFragment.this.displayVideoLikesUserList(list);
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

    void initComment() {
        if (this.archiveInfo == null) {
            return;
        }
        BroadcastViewerServiceHelper.getViewerComments(this.archiveInfo.serviceId, new BroadcastViewerServiceHelper.BroadcastViewerServiceHelperListener() { // from class: com.treadly.Treadly.UI.TreadlyVideo.TreadlyDiscoverVideoFragment.9
            @Override // com.treadly.Treadly.UI.TreadlyVideo.Data.BroadcastViewerServiceHelper.BroadcastViewerServiceHelperListener
            public void onGetViewers(String str, List<UserInfo> list) {
            }

            @Override // com.treadly.Treadly.UI.TreadlyVideo.Data.BroadcastViewerServiceHelper.BroadcastViewerServiceHelperListener
            public void onViewerUpdate(String str, List<String> list, Boolean bool) {
            }

            @Override // com.treadly.Treadly.UI.TreadlyVideo.Data.BroadcastViewerServiceHelper.BroadcastViewerServiceHelperListener
            public void onGetViewerComments(String str, List<UserComment> list) {
                if (str != null) {
                    return;
                }
                TreadlyDiscoverVideoFragment.this.handleOnReceiveComments(list);
            }
        });
    }

    void handleOnReceiveComments(List<UserComment> list) {
        this.commentListComplete = list;
        reloadReceiveComments();
    }

    void checkRewind() {
        int currentPosition = this.videoView.getCurrentPosition();
        if (this.lastPosition <= currentPosition) {
            this.lastPosition = currentPosition;
            Log.d("WILLG", "No Rewind");
            return;
        }
        Log.d("WILLG", "Found Rewind");
        this.lastPosition = currentPosition;
        reloadReceiveComments();
    }

    void reloadReceiveComments() {
        if (this.commentListComplete == null) {
            return;
        }
        ArrayList arrayList = new ArrayList(this.commentListComplete);
        Collections.reverse(arrayList);
        this.commentList = arrayList;
        ActivityUtil.runOnUiThread(getActivity(), new Runnable() { // from class: com.treadly.Treadly.UI.TreadlyVideo.TreadlyDiscoverVideoFragment.10
            @Override // java.lang.Runnable
            public void run() {
                TreadlyDiscoverVideoFragment.this.commentController.clearMessages();
            }
        });
        handleOnVideoPlay();
    }

    void startCommentTimer() {
        this.commentTimer = new Timer();
        if (this.commentList.isEmpty()) {
            stopCommentTimer();
        } else {
            this.commentTimer.schedule(new TimerTask() { // from class: com.treadly.Treadly.UI.TreadlyVideo.TreadlyDiscoverVideoFragment.11
                @Override // java.util.TimerTask, java.lang.Runnable
                public void run() {
                    TreadlyDiscoverVideoFragment.this.onCommentTimerTrigger();
                }
            }, 0L, 500L);
        }
    }

    void onCommentTimerTrigger() {
        processComments();
    }

    void processComments() {
        ActivityUtil.runOnUiThread(getActivity(), new Runnable() { // from class: com.treadly.Treadly.UI.TreadlyVideo.TreadlyDiscoverVideoFragment.12
            @Override // java.lang.Runnable
            public void run() {
                if (TreadlyDiscoverVideoFragment.this.archiveInfo == null) {
                    return;
                }
                if (TreadlyDiscoverVideoFragment.this.videoView.isPlaying()) {
                    TreadlyDiscoverVideoFragment.this.checkRewind();
                }
                if (TreadlyDiscoverVideoFragment.this.commentList.isEmpty()) {
                    return;
                }
                ArrayList<UserComment> arrayList = new ArrayList(TreadlyDiscoverVideoFragment.this.commentList);
                Collections.reverse(arrayList);
                new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss", Locale.US);
                for (final UserComment userComment : arrayList) {
                    if (TreadlyDiscoverVideoFragment.this.convertTimeStampToDate(TreadlyDiscoverVideoFragment.this.convertDateToServiceTimestamp(userComment.createdAt)).getTime() - TreadlyDiscoverVideoFragment.this.archiveInfo.createdAt.getTime() <= TreadlyDiscoverVideoFragment.this.videoView.getCurrentPosition()) {
                        ActivityUtil.runOnUiThread(TreadlyDiscoverVideoFragment.this.getActivity(), new Runnable() { // from class: com.treadly.Treadly.UI.TreadlyVideo.TreadlyDiscoverVideoFragment.12.1
                            @Override // java.lang.Runnable
                            public void run() {
                                TreadlyDiscoverVideoFragment.this.commentController.addMessage(userComment);
                            }
                        });
                        TreadlyDiscoverVideoFragment.this.commentList.remove(userComment);
                    }
                }
            }
        });
    }

    void stopCommentTimer() {
        if (this.commentTimer == null) {
            return;
        }
        this.commentTimer.cancel();
        this.commentTimer = null;
    }

    void handleOnVideoPlay() {
        startCommentTimer();
    }

    void handleOnVideoPause() {
        stopCommentTimer();
    }

    void handleVideoLiked() {
        final Boolean bool = this.archiveLikeInfo.isLike;
        final String str = this.archiveInfo.id;
        final String str2 = this.archiveInfo.serviceId;
        if (bool == null || str == null || str2 == null) {
            return;
        }
        if (bool.booleanValue()) {
            VideoServiceHelper.deleteVideoArchiveLike(str, str2, new VideoServiceHelper.VideoResponseListener() { // from class: com.treadly.Treadly.UI.TreadlyVideo.TreadlyDiscoverVideoFragment.13
                @Override // com.treadly.Treadly.UI.TreadlyVideo.Data.VideoServiceHelper.VideoResponseListener
                public void onResponse(String str3) {
                    if (str3 != null) {
                        TreadlyDiscoverVideoFragment.this.fetchVideoLike(str, str2);
                    }
                }
            });
        } else {
            VideoServiceHelper.createVideoArchiveLike(str, str2, new VideoServiceHelper.VideoResponseListener() { // from class: com.treadly.Treadly.UI.TreadlyVideo.TreadlyDiscoverVideoFragment.14
                @Override // com.treadly.Treadly.UI.TreadlyVideo.Data.VideoServiceHelper.VideoResponseListener
                public void onResponse(String str3) {
                    if (str3 != null) {
                        TreadlyDiscoverVideoFragment.this.fetchVideoLike(str, str2);
                    }
                }
            });
        }
        ActivityUtil.runOnUiThread(getActivity(), new Runnable() { // from class: com.treadly.Treadly.UI.TreadlyVideo.TreadlyDiscoverVideoFragment.15
            @Override // java.lang.Runnable
            public void run() {
                TreadlyDiscoverVideoFragment.this.commentController.setIsLiked(!bool.booleanValue(), true);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void fetchVideoLike(String str, String str2) {
        VideoServiceHelper.getVideoArchiveLikeInfo(str, str2, new VideoServiceHelper.VideoLikeListener() { // from class: com.treadly.Treadly.UI.TreadlyVideo.TreadlyDiscoverVideoFragment.16
            @Override // com.treadly.Treadly.UI.TreadlyVideo.Data.VideoServiceHelper.VideoLikeListener
            public void onCreateVideoLike(String str3) {
            }

            @Override // com.treadly.Treadly.UI.TreadlyVideo.Data.VideoServiceHelper.VideoLikeListener
            public void onDeleteVideoLike(String str3) {
            }

            @Override // com.treadly.Treadly.UI.TreadlyVideo.Data.VideoServiceHelper.VideoLikeListener
            public void onGetVideoLikeUserInfo(String str3, List<VideoLikeUserInfo> list) {
            }

            @Override // com.treadly.Treadly.UI.TreadlyVideo.Data.VideoServiceHelper.VideoLikeListener
            public void onGetVideoLikeInfo(String str3, final VideoLikeInfo videoLikeInfo) {
                if (str3 != null || videoLikeInfo == null) {
                    return;
                }
                ActivityUtil.runOnUiThread(TreadlyDiscoverVideoFragment.this.getActivity(), new Runnable() { // from class: com.treadly.Treadly.UI.TreadlyVideo.TreadlyDiscoverVideoFragment.16.1
                    @Override // java.lang.Runnable
                    public void run() {
                        TreadlyDiscoverVideoFragment.this.setVideoLikeInfo(videoLikeInfo);
                    }
                });
            }
        });
    }

    void setVideoLikeInfo(VideoLikeInfo videoLikeInfo) {
        this.archiveLikeInfo = videoLikeInfo;
        if (videoLikeInfo == null) {
            this.commentController.setLikes(-1);
            this.commentController.setIsLiked(false, true);
            return;
        }
        this.commentController.setLikes(this.archiveLikeInfo.likeCount.intValue());
        this.commentController.setIsLiked(this.archiveLikeInfo.isLike.booleanValue(), true);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public String convertDateToServiceTimestamp(Date date) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS", Locale.getDefault());
        simpleDateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
        String format = simpleDateFormat.format(date);
        return format + "'Z'";
    }

    /* JADX INFO: Access modifiers changed from: private */
    public Date convertTimeStampToDate(String str) {
        if (str == null) {
            return null;
        }
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS", Locale.getDefault());
        simpleDateFormat.setTimeZone(TimeZone.getDefault());
        try {
            return simpleDateFormat.parse(str);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public void deletePressed() {
        new AlertDialog.Builder(getContext()).setTitle("Delete Video").setMessage("Are you sure you want to delete this video?").setPositiveButton("Yes", new DialogInterface.OnClickListener() { // from class: com.treadly.Treadly.UI.TreadlyVideo.TreadlyDiscoverVideoFragment.17
            @Override // android.content.DialogInterface.OnClickListener
            public void onClick(DialogInterface dialogInterface, int i) {
                TreadlyDiscoverVideoFragment.this.deleteVideo();
            }
        }).setNegativeButton("No", (DialogInterface.OnClickListener) null).show();
    }

    public void deleteVideo() {
        if (this.archiveInfo == null || this.archiveInfo.id == null) {
            return;
        }
        VideoServiceHelper.deleteVideo(this.archiveInfo.id, new VideoServiceHelper.VideoResponseListener() { // from class: com.treadly.Treadly.UI.TreadlyVideo.TreadlyDiscoverVideoFragment.18
            @Override // com.treadly.Treadly.UI.TreadlyVideo.Data.VideoServiceHelper.VideoResponseListener
            public void onResponse(String str) {
                if (str == null) {
                    TreadlyDiscoverVideoFragment.this.deleteVideoListener.didDeleteVideo();
                } else {
                    TreadlyDiscoverVideoFragment.this.showDeleteError();
                }
            }
        });
    }

    public void showDeleteError() {
        new AlertDialog.Builder(getContext()).setTitle("Delete Error").setMessage("There was an error deleting the video").setNeutralButton("Dismiss", (DialogInterface.OnClickListener) null).show();
    }
}
