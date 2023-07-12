package com.treadly.Treadly.UI.TreadlyConnect.Video;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import androidx.fragment.app.Fragment;
import com.treadly.Treadly.Data.Delegates.TreadlyUserEventAdapter;
import com.treadly.Treadly.Data.Delegates.TreadlyUserEventDelegate;
import com.treadly.Treadly.Data.Listeners.TreadlyServiceResponseEventAdapter;
import com.treadly.Treadly.Data.Managers.TreadlyEventManager;
import com.treadly.Treadly.Data.Managers.TreadlyServiceManager;
import com.treadly.Treadly.Data.Model.UserInfo;
import com.treadly.Treadly.MainActivity;
import com.treadly.Treadly.R;
import com.treadly.Treadly.UI.TreadlyConnect.TreadlyConnectFragment;
import com.treadly.Treadly.UI.TreadlyConnect.Video.TreadlyInviteRequestDialog;
import com.treadly.Treadly.UI.TreadlyVideo.Data.BroadcastInviteServiceHelper;
import com.treadly.Treadly.UI.TreadlyVideo.Data.InviteServiceHelper;
import com.treadly.Treadly.UI.TreadlyVideo.Data.InviteServiceInviteInfo;
import com.treadly.Treadly.UI.TreadlyVideo.PrivateStream.TreadlyPrivateStreamHostFragment;
import com.treadly.Treadly.UI.TreadlyVideo.PublicStream.PublicStreamOptions.TreadlyPublicStreamOptionsFragment;
import com.treadly.Treadly.UI.TreadlyVideo.PublicStream.PublicStreamViewer.TreadlyPublicStreamViewerFragment;
import java.util.List;

/* loaded from: classes2.dex */
public class TreadlyConnectVideoController {
    private Context context;
    public TreadlyInviteRequestDialog inviteAlert;
    private TreadlyConnectVideoControllerListener listener;
    private String TAG = "ConnectVideoController";
    private boolean hasStarted = false;
    private boolean isCheckingPendingInvites = false;
    private boolean isCheckingPendingPublicStreamInvites = false;
    private InviteServiceInviteInfo presentedInvite = new InviteServiceInviteInfo("", "");
    TreadlyUserEventDelegate userEventAdapter = new TreadlyUserEventAdapter() { // from class: com.treadly.Treadly.UI.TreadlyConnect.Video.TreadlyConnectVideoController.6
        @Override // com.treadly.Treadly.Data.Delegates.TreadlyUserEventAdapter, com.treadly.Treadly.Data.Delegates.TreadlyUserEventDelegate
        public void onReceiveUserVideoBoardcastInviteRequest(UserInfo userInfo) {
            System.out.println("SPAM!!!: receiveboard");
            TreadlyConnectVideoController.this.checkPendingPublicStreamInvites();
        }

        @Override // com.treadly.Treadly.Data.Delegates.TreadlyUserEventAdapter, com.treadly.Treadly.Data.Delegates.TreadlyUserEventDelegate
        public void onReceiveUserVideoPrivateInviteRequest(UserInfo userInfo) {
            super.onReceiveUserVideoPrivateInviteRequest(userInfo);
            System.out.println("SPAM!!!: receivepriv");
            TreadlyConnectVideoController.this.checkPendingInvites();
        }

        @Override // com.treadly.Treadly.Data.Delegates.TreadlyUserEventAdapter, com.treadly.Treadly.Data.Delegates.TreadlyUserEventDelegate
        public void onReceiveUserVideoPrivateInviteDeleted(UserInfo userInfo) {
            super.onReceiveUserVideoPrivateInviteDeleted(userInfo);
        }
    };

    /* loaded from: classes2.dex */
    public interface TreadlyConnectVideoControllerListener {
        void presentAlert(AlertDialog alertDialog);

        void presentFragment(Fragment fragment);
    }

    public TreadlyConnectVideoController(Context context, TreadlyConnectVideoControllerListener treadlyConnectVideoControllerListener) {
        this.context = context;
        this.listener = treadlyConnectVideoControllerListener;
        start();
    }

    public void start() {
        if (this.hasStarted) {
            return;
        }
        TreadlyEventManager.getInstance().addUserDelegate(this.userEventAdapter);
        this.hasStarted = true;
    }

    public void stop() {
        if (this.hasStarted) {
            TreadlyEventManager.getInstance().removeUserDelegate(this.userEventAdapter);
            this.hasStarted = false;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void checkPendingInvites() {
        Log.d(this.TAG, "checkPendingInvites");
        if (this.isCheckingPendingInvites) {
            return;
        }
        String userId = TreadlyServiceManager.getInstance().getUserId();
        String str = this.TAG;
        Log.d(str, "userId: " + userId);
        if (userId == null) {
            return;
        }
        this.isCheckingPendingInvites = true;
        InviteServiceHelper.getPendingInviteInfoList(userId, new AnonymousClass1());
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* renamed from: com.treadly.Treadly.UI.TreadlyConnect.Video.TreadlyConnectVideoController$1  reason: invalid class name */
    /* loaded from: classes2.dex */
    public class AnonymousClass1 implements InviteServiceHelper.InviteServiceHelperInfoList {
        AnonymousClass1() {
        }

        @Override // com.treadly.Treadly.UI.TreadlyVideo.Data.InviteServiceHelper.InviteServiceHelperInfoList
        public void onResponse(String str, List<InviteServiceInviteInfo> list) {
            if (list == null || list.size() <= 0) {
                Log.d(TreadlyConnectVideoController.this.TAG, "Not using invite");
                TreadlyConnectVideoController.this.dismissInvite();
                TreadlyConnectVideoController.this.isCheckingPendingInvites = false;
                return;
            }
            InviteServiceInviteInfo inviteServiceInviteInfo = list.get(0);
            InviteServiceInviteInfo inviteServiceInviteInfo2 = TreadlyConnectVideoController.this.presentedInvite;
            if (TreadlyConnectVideoController.this.inviteAlert != null && inviteServiceInviteInfo.userIdFrom.equals(inviteServiceInviteInfo2.userIdFrom) && inviteServiceInviteInfo.userIdTo.equals(inviteServiceInviteInfo2.userIdTo)) {
                Log.i(TreadlyConnectVideoController.this.TAG, "Already presented");
                TreadlyConnectVideoController.this.dismissInvite();
                return;
            }
            TreadlyServiceManager.getInstance().refreshUsersInfo(new C00311(inviteServiceInviteInfo));
        }

        /* renamed from: com.treadly.Treadly.UI.TreadlyConnect.Video.TreadlyConnectVideoController$1$1  reason: invalid class name and collision with other inner class name */
        /* loaded from: classes2.dex */
        class C00311 extends TreadlyServiceResponseEventAdapter {
            final /* synthetic */ InviteServiceInviteInfo val$inviteInfo;

            C00311(InviteServiceInviteInfo inviteServiceInviteInfo) {
                this.val$inviteInfo = inviteServiceInviteInfo;
            }

            @Override // com.treadly.Treadly.Data.Listeners.TreadlyServiceResponseEventAdapter, com.treadly.Treadly.Data.Listeners.TreadlyServiceResponseEventListener
            public void onSuccess(String str) {
                if (TreadlyConnectVideoController.this.inviteAlert != null) {
                    return;
                }
                UserInfo userInfoById = TreadlyServiceManager.getInstance().getUserInfoById(this.val$inviteInfo.userIdFrom);
                if (userInfoById != null) {
                    TreadlyConnectVideoController.this.presentInviteAlert(this.val$inviteInfo, userInfoById.name);
                } else {
                    InviteServiceHelper.declineInvite(this.val$inviteInfo.userIdFrom, this.val$inviteInfo.userIdTo, new InviteServiceHelper.InviteServiceHelperRequestBasicUsersPost() { // from class: com.treadly.Treadly.UI.TreadlyConnect.Video.TreadlyConnectVideoController.1.1.1
                        @Override // com.treadly.Treadly.UI.TreadlyVideo.Data.InviteServiceHelper.InviteServiceHelperRequestBasicUsersPost
                        public void onResponse(String str2) {
                            TreadlyConnectVideoController.this.isCheckingPendingInvites = false;
                            ((MainActivity) TreadlyConnectVideoController.this.context).runOnUiThread(new Runnable() { // from class: com.treadly.Treadly.UI.TreadlyConnect.Video.TreadlyConnectVideoController.1.1.1.1
                                @Override // java.lang.Runnable
                                public void run() {
                                    TreadlyConnectVideoController.this.checkPendingInvites();
                                }
                            });
                        }
                    });
                }
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void dismissInvite() {
        if (this.inviteAlert != null) {
            this.inviteAlert.dismiss();
        }
        this.inviteAlert = null;
        this.presentedInvite = null;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void presentInviteAlert(InviteServiceInviteInfo inviteServiceInviteInfo, String str) {
        Log.i(this.TAG, "Presenting invite alert");
        this.inviteAlert = new TreadlyInviteRequestDialog();
        this.inviteAlert.name = str;
        this.inviteAlert.listener = new AnonymousClass2(inviteServiceInviteInfo);
        this.presentedInvite = inviteServiceInviteInfo;
        this.inviteAlert.show(((MainActivity) this.context).getSupportFragmentManager(), "inviteAlert");
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* renamed from: com.treadly.Treadly.UI.TreadlyConnect.Video.TreadlyConnectVideoController$2  reason: invalid class name */
    /* loaded from: classes2.dex */
    public class AnonymousClass2 implements TreadlyInviteRequestDialog.TreadlyInviteRequestListener {
        final /* synthetic */ InviteServiceInviteInfo val$inviteInfo;

        AnonymousClass2(InviteServiceInviteInfo inviteServiceInviteInfo) {
            this.val$inviteInfo = inviteServiceInviteInfo;
        }

        @Override // com.treadly.Treadly.UI.TreadlyConnect.Video.TreadlyInviteRequestDialog.TreadlyInviteRequestListener
        public void onAccept() {
            InviteServiceHelper.acceptInvite(this.val$inviteInfo.userIdFrom, this.val$inviteInfo.userIdTo, new InviteServiceHelper.InviteServiceHelperRequestBasicUsersPost() { // from class: com.treadly.Treadly.UI.TreadlyConnect.Video.TreadlyConnectVideoController.2.1
                @Override // com.treadly.Treadly.UI.TreadlyVideo.Data.InviteServiceHelper.InviteServiceHelperRequestBasicUsersPost
                public void onResponse(String str) {
                    if (str == null) {
                        TreadlyConnectVideoController.this.presentJoinLiveStream(AnonymousClass2.this.val$inviteInfo.userIdFrom);
                    } else {
                        String str2 = TreadlyConnectVideoController.this.TAG;
                        Log.e(str2, "Error on accept: " + str);
                    }
                    TreadlyConnectVideoController.this.isCheckingPendingInvites = false;
                    TreadlyConnectVideoController.this.dismissInvite();
                }
            });
        }

        @Override // com.treadly.Treadly.UI.TreadlyConnect.Video.TreadlyInviteRequestDialog.TreadlyInviteRequestListener
        public void onDecline() {
            InviteServiceHelper.declineInvite(this.val$inviteInfo.userIdFrom, this.val$inviteInfo.userIdTo, new InviteServiceHelper.InviteServiceHelperRequestBasicUsersPost() { // from class: com.treadly.Treadly.UI.TreadlyConnect.Video.TreadlyConnectVideoController.2.2
                @Override // com.treadly.Treadly.UI.TreadlyVideo.Data.InviteServiceHelper.InviteServiceHelperRequestBasicUsersPost
                public void onResponse(String str) {
                    TreadlyConnectVideoController.this.isCheckingPendingInvites = false;
                    ((MainActivity) TreadlyConnectVideoController.this.context).runOnUiThread(new Runnable() { // from class: com.treadly.Treadly.UI.TreadlyConnect.Video.TreadlyConnectVideoController.2.2.1
                        @Override // java.lang.Runnable
                        public void run() {
                            TreadlyConnectVideoController.this.checkPendingInvites();
                        }
                    });
                    TreadlyConnectVideoController.this.dismissInvite();
                }
            });
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void checkPendingPublicStreamInvites() {
        String userId;
        if (this.isCheckingPendingPublicStreamInvites || (userId = TreadlyServiceManager.getInstance().getUserId()) == null) {
            return;
        }
        this.isCheckingPendingPublicStreamInvites = true;
        BroadcastInviteServiceHelper.getPendingBroadcastInviteInfoList(userId, new AnonymousClass3());
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* renamed from: com.treadly.Treadly.UI.TreadlyConnect.Video.TreadlyConnectVideoController$3  reason: invalid class name */
    /* loaded from: classes2.dex */
    public class AnonymousClass3 implements BroadcastInviteServiceHelper.BroadcastInviteResponseListener {
        @Override // com.treadly.Treadly.UI.TreadlyVideo.Data.BroadcastInviteServiceHelper.BroadcastInviteResponseListener
        public void onSuccess(String str) {
        }

        AnonymousClass3() {
        }

        @Override // com.treadly.Treadly.UI.TreadlyVideo.Data.BroadcastInviteServiceHelper.BroadcastInviteResponseListener
        public void onInviteInfoList(String str, List<InviteServiceInviteInfo> list) {
            if (list == null || list.size() <= 0) {
                TreadlyConnectVideoController.this.isCheckingPendingPublicStreamInvites = false;
            } else {
                TreadlyServiceManager.getInstance().refreshUsersInfo(new AnonymousClass1(list.get(0)));
            }
        }

        /* renamed from: com.treadly.Treadly.UI.TreadlyConnect.Video.TreadlyConnectVideoController$3$1  reason: invalid class name */
        /* loaded from: classes2.dex */
        class AnonymousClass1 extends TreadlyServiceResponseEventAdapter {
            final /* synthetic */ InviteServiceInviteInfo val$inviteInfo;

            AnonymousClass1(InviteServiceInviteInfo inviteServiceInviteInfo) {
                this.val$inviteInfo = inviteServiceInviteInfo;
            }

            @Override // com.treadly.Treadly.Data.Listeners.TreadlyServiceResponseEventAdapter, com.treadly.Treadly.Data.Listeners.TreadlyServiceResponseEventListener
            public void onSuccess(String str) {
                UserInfo userInfoById = TreadlyServiceManager.getInstance().getUserInfoById(this.val$inviteInfo.userIdFrom);
                if (userInfoById != null) {
                    TreadlyConnectVideoController.this.presentPublicStreamInvite(this.val$inviteInfo, userInfoById.name);
                } else {
                    BroadcastInviteServiceHelper.declineBroadcastInvite(this.val$inviteInfo.userIdFrom, this.val$inviteInfo.userIdTo, new BroadcastInviteServiceHelper.BroadcastInviteResponseListener() { // from class: com.treadly.Treadly.UI.TreadlyConnect.Video.TreadlyConnectVideoController.3.1.1
                        @Override // com.treadly.Treadly.UI.TreadlyVideo.Data.BroadcastInviteServiceHelper.BroadcastInviteResponseListener
                        public void onInviteInfoList(String str2, List<InviteServiceInviteInfo> list) {
                        }

                        @Override // com.treadly.Treadly.UI.TreadlyVideo.Data.BroadcastInviteServiceHelper.BroadcastInviteResponseListener
                        public void onSuccess(String str2) {
                            TreadlyConnectVideoController.this.isCheckingPendingPublicStreamInvites = false;
                            TreadlyConnectVideoController.runOnMain(new Runnable() { // from class: com.treadly.Treadly.UI.TreadlyConnect.Video.TreadlyConnectVideoController.3.1.1.1
                                @Override // java.lang.Runnable
                                public void run() {
                                    TreadlyConnectVideoController.this.checkPendingPublicStreamInvites();
                                }
                            });
                        }
                    });
                }
            }
        }
    }

    public void presentPublicStreamInvite(final InviteServiceInviteInfo inviteServiceInviteInfo, String str) {
        AlertDialog.Builder title = new AlertDialog.Builder(this.context).setTitle("Public Stream Invite");
        title.setMessage(str + " has invited you to join their public live stream").setPositiveButton("Accept", new DialogInterface.OnClickListener() { // from class: com.treadly.Treadly.UI.TreadlyConnect.Video.TreadlyConnectVideoController.5
            @Override // android.content.DialogInterface.OnClickListener
            public void onClick(DialogInterface dialogInterface, int i) {
                BroadcastInviteServiceHelper.acceptBroadcastInvite(inviteServiceInviteInfo.userIdFrom, inviteServiceInviteInfo.userIdTo, new BroadcastInviteServiceHelper.BroadcastInviteResponseListener() { // from class: com.treadly.Treadly.UI.TreadlyConnect.Video.TreadlyConnectVideoController.5.1
                    @Override // com.treadly.Treadly.UI.TreadlyVideo.Data.BroadcastInviteServiceHelper.BroadcastInviteResponseListener
                    public void onInviteInfoList(String str2, List<InviteServiceInviteInfo> list) {
                    }

                    @Override // com.treadly.Treadly.UI.TreadlyVideo.Data.BroadcastInviteServiceHelper.BroadcastInviteResponseListener
                    public void onSuccess(String str2) {
                        if (str2 == null) {
                            TreadlyConnectVideoController.this.toPublicStreamViewerFragment(inviteServiceInviteInfo.userIdFrom);
                        }
                        TreadlyConnectVideoController.this.isCheckingPendingPublicStreamInvites = false;
                    }
                });
            }
        }).setNegativeButton("Decline", new AnonymousClass4(inviteServiceInviteInfo)).show();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* renamed from: com.treadly.Treadly.UI.TreadlyConnect.Video.TreadlyConnectVideoController$4  reason: invalid class name */
    /* loaded from: classes2.dex */
    public class AnonymousClass4 implements DialogInterface.OnClickListener {
        final /* synthetic */ InviteServiceInviteInfo val$inviteInfo;

        AnonymousClass4(InviteServiceInviteInfo inviteServiceInviteInfo) {
            this.val$inviteInfo = inviteServiceInviteInfo;
        }

        @Override // android.content.DialogInterface.OnClickListener
        public void onClick(DialogInterface dialogInterface, int i) {
            BroadcastInviteServiceHelper.declineBroadcastInvite(this.val$inviteInfo.userIdFrom, this.val$inviteInfo.userIdTo, new BroadcastInviteServiceHelper.BroadcastInviteResponseListener() { // from class: com.treadly.Treadly.UI.TreadlyConnect.Video.TreadlyConnectVideoController.4.1
                @Override // com.treadly.Treadly.UI.TreadlyVideo.Data.BroadcastInviteServiceHelper.BroadcastInviteResponseListener
                public void onInviteInfoList(String str, List<InviteServiceInviteInfo> list) {
                }

                @Override // com.treadly.Treadly.UI.TreadlyVideo.Data.BroadcastInviteServiceHelper.BroadcastInviteResponseListener
                public void onSuccess(String str) {
                    TreadlyConnectVideoController.this.isCheckingPendingPublicStreamInvites = false;
                    TreadlyConnectVideoController.runOnMain(new Runnable() { // from class: com.treadly.Treadly.UI.TreadlyConnect.Video.TreadlyConnectVideoController.4.1.1
                        @Override // java.lang.Runnable
                        public void run() {
                            TreadlyConnectVideoController.this.checkPendingPublicStreamInvites();
                        }
                    });
                }
            });
        }
    }

    public void presentGoLive(boolean z, UserInfo userInfo) {
        toPrivateStreamHostFragment(TreadlyServiceManager.getInstance().getUserId(), true, z, userInfo);
    }

    public void onGoLiveButtonPressed(boolean z) {
        presentGoLive(z, null);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void presentJoinLiveStream(String str) {
        Log.i(this.TAG, "presentJoinLiveStream");
        toPrivateStreamHostFragment(str, false, false, null);
    }

    public void toPrivateStreamHostFragment(String str, boolean z, boolean z2, UserInfo userInfo) {
        TreadlyPrivateStreamHostFragment treadlyPrivateStreamHostFragment = new TreadlyPrivateStreamHostFragment();
        treadlyPrivateStreamHostFragment.userId = str;
        treadlyPrivateStreamHostFragment.isHost = z;
        treadlyPrivateStreamHostFragment.pendingUserInvite = userInfo;
        ((MainActivity) this.context).getSupportFragmentManager().beginTransaction().addToBackStack(TreadlyConnectFragment.TAG).add(R.id.activity_fragment_container_empty, treadlyPrivateStreamHostFragment).commit();
    }

    public void onGoLivePublicStreamPressed() {
        ((MainActivity) this.context).getSupportFragmentManager().beginTransaction().addToBackStack(TreadlyConnectFragment.TAG).add(R.id.activity_fragment_container_empty, new TreadlyPublicStreamOptionsFragment()).commit();
    }

    public void toPublicStreamViewerFragment(String str) {
        TreadlyPublicStreamViewerFragment treadlyPublicStreamViewerFragment = new TreadlyPublicStreamViewerFragment();
        treadlyPublicStreamViewerFragment.userId = str;
        ((MainActivity) this.context).getSupportFragmentManager().beginTransaction().addToBackStack(TreadlyConnectFragment.TAG).add(R.id.activity_fragment_container_empty, treadlyPublicStreamViewerFragment).commit();
    }

    public void inviteFriend(UserInfo userInfo) {
        presentGoLive(false, userInfo);
    }

    static void runOnMain(Runnable runnable) {
        new Handler(Looper.getMainLooper()).post(runnable);
    }
}
