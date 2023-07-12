package com.treadly.Treadly.UI.TreadlyVideo.VideoUserStats;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.PopupMenu;
import android.widget.PopupWindow;
import android.widget.TextView;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.github.mikephil.charting.utils.Utils;
import com.treadly.Treadly.Data.Model.TrainerModeEnabledState;
import com.treadly.Treadly.Data.Model.TrainerModeState;
import com.treadly.Treadly.Data.Model.UserInfo;
import com.treadly.Treadly.Data.Model.UserTrainerMode;
import com.treadly.Treadly.Data.Model.UserVideoPrivateStateInfo;
import com.treadly.Treadly.Data.Model.UsersTrainerModes;
import com.treadly.Treadly.R;
import com.treadly.Treadly.UI.TreadlyVideo.Data.VideoServiceHelper;
import com.treadly.Treadly.UI.TreadlyVideo.Data.VideoServiceUserStatsInfo;
import com.treadly.client.lib.sdk.Model.AuthenticationState;
import com.treadly.client.lib.sdk.Model.DeviceConnectionStatus;
import com.treadly.client.lib.sdk.Model.DistanceUnits;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.function.BiConsumer;

/* loaded from: classes2.dex */
public class TreadlyVideoStatsViewController {
    static HashMap<String, Boolean> userDeviceConnectionStates = new HashMap<>();
    static HashMap<String, Boolean> userDeviceConnectionStatesVerified = new HashMap<>();
    Activity activity;
    public VideoStatsAdapter adapter;
    PopupWindow blurBackgroundPopup;
    View blurView;
    Context context;
    public String currentUserId;
    PopupWindow datePickerPopup;
    View datePickerView;
    TextView descriptionPara;
    View dividerTwo;
    public String hostId;
    ImageButton popupExitButton;
    TextView popupTitle;
    View popupView;
    PopupWindow popupWindow;
    TreadlyVideoStatsAdapter statsAdapter;
    RecyclerView statsRecycler;
    Button trainerModeButton;
    PopupMenu trainerModeMenu;
    private CharSequence[] trainerNames;
    VideoServiceUserStatsInfo userStats;
    TextView warningText;
    public final String TAG = getClass().getSimpleName();
    AlertDialog trainerModeAlert = null;
    HashMap<String, UserInfo> friendsUserInfoLookup = new HashMap<>();
    HashMap<String, VideoServiceUserStatsInfo> friendsStatsInfoLookup = new HashMap<>();
    List<VideoServiceUserStatsInfo> friendsStats = new ArrayList();
    List<UsersTrainerModes> usersTrainerModes = new ArrayList();
    UsersTrainerModes pendingTrainerModeUser = null;
    String pendingStudentModeTargetId = null;
    TrainerModeState trainerMode = TrainerModeState.inactive;
    TrainerModeState studentMode = TrainerModeState.inactive;
    public boolean isHost = false;
    DeviceConnectionStatus deviceConnectState = DeviceConnectionStatus.notConnected;
    AuthenticationState authenticationState = AuthenticationState.unknown;

    /* loaded from: classes2.dex */
    public interface VideoStatsAdapter {
        void broadcastUserTrainerModes(List<UsersTrainerModes> list);

        void studentModeDidChange(boolean z);

        void studentModeRequested(TrainerModeState trainerModeState, String str);

        void studentModeResponded(TrainerModeState trainerModeState, String str);

        void trainerModeDidChange(boolean z);

        void trainerModeRequested(TrainerModeState trainerModeState);

        void trainerModeResponded(TrainerModeState trainerModeState);

        void trainerModeStateChanged(boolean z, boolean z2);
    }

    public void setTrainerMode(TrainerModeState trainerModeState) {
        if (this.trainerMode != trainerModeState && this.adapter != null) {
            this.adapter.trainerModeDidChange(trainerModeState == TrainerModeState.active);
        }
        this.trainerMode = trainerModeState;
    }

    public TrainerModeState getTrainerMode() {
        return this.trainerMode;
    }

    public void setStudentMode(TrainerModeState trainerModeState) {
        if (this.studentMode != trainerModeState && this.adapter != null) {
            this.adapter.studentModeDidChange(trainerModeState == TrainerModeState.active);
        }
        this.studentMode = trainerModeState;
    }

    public TrainerModeState getStudentMode() {
        return this.studentMode;
    }

    public void setDeviceConnectState(DeviceConnectionStatus deviceConnectionStatus) {
        this.deviceConnectState = deviceConnectionStatus;
        this.statsAdapter = new TreadlyVideoStatsAdapter(this.context, this.friendsStats);
    }

    public void setAuthenticationState(AuthenticationState authenticationState) {
        this.authenticationState = authenticationState;
    }

    public TreadlyVideoStatsViewController(Context context) {
        this.context = context;
    }

    public void show() {
        initializeWindow();
        showListPopup();
    }

    private void initializeWindow() {
        LayoutInflater layoutInflater = (LayoutInflater) this.context.getSystemService("layout_inflater");
        this.popupView = layoutInflater.inflate(R.layout.video_stats, (ViewGroup) null);
        this.popupTitle = (TextView) this.popupView.findViewById(R.id.video_stats_title);
        this.popupExitButton = (ImageButton) this.popupView.findViewById(R.id.video_stats_x);
        this.popupExitButton.setOnClickListener(new View.OnClickListener() { // from class: com.treadly.Treadly.UI.TreadlyVideo.VideoUserStats.-$$Lambda$TreadlyVideoStatsViewController$QMLVRAYU1cYTNujb9AeFyOKcCAA
            @Override // android.view.View.OnClickListener
            public final void onClick(View view) {
                TreadlyVideoStatsViewController.this.popupWindow.dismiss();
            }
        });
        this.trainerModeButton = (Button) this.popupView.findViewById(R.id.video_stats_trainer_mode_button);
        this.dividerTwo = this.popupView.findViewById(R.id.video_stats_divider_two);
        if (!isTrainerModeEnabled()) {
            this.trainerModeButton.setVisibility(8);
            this.dividerTwo.setVisibility(8);
        } else {
            this.trainerModeButton.setVisibility(0);
            this.dividerTwo.setVisibility(0);
            if (this.studentMode == TrainerModeState.inactive && this.trainerMode == TrainerModeState.inactive) {
                this.trainerModeButton.setOnClickListener(new View.OnClickListener() { // from class: com.treadly.Treadly.UI.TreadlyVideo.VideoUserStats.-$$Lambda$TreadlyVideoStatsViewController$OTkeh2xG0d4yTZyhPiv1AkEwTG4
                    @Override // android.view.View.OnClickListener
                    public final void onClick(View view) {
                        r0.runOnMain(new Runnable() { // from class: com.treadly.Treadly.UI.TreadlyVideo.VideoUserStats.-$$Lambda$TreadlyVideoStatsViewController$xP1kjcMOizqglCcOE2Ev9ui4jjI
                            @Override // java.lang.Runnable
                            public final void run() {
                                r0.handleTrainerModePressed(r0.popupView, TreadlyVideoStatsViewController.this.trainerModeButton);
                            }
                        });
                    }
                });
            } else if (this.studentMode == TrainerModeState.active) {
                this.trainerModeButton.setOnClickListener(new View.OnClickListener() { // from class: com.treadly.Treadly.UI.TreadlyVideo.VideoUserStats.-$$Lambda$TreadlyVideoStatsViewController$of1_GaeYJYkedUbDVLDNPEyRq5Y
                    @Override // android.view.View.OnClickListener
                    public final void onClick(View view) {
                        r0.runOnMain(new $$Lambda$TreadlyVideoStatsViewController$6jpYnjipSzX83Hp_1_n2wiFbG48(TreadlyVideoStatsViewController.this));
                    }
                });
                this.trainerModeButton.setText(R.string.end_student_mode);
            } else if (this.trainerMode == TrainerModeState.active) {
                this.trainerModeButton.setOnClickListener(new View.OnClickListener() { // from class: com.treadly.Treadly.UI.TreadlyVideo.VideoUserStats.-$$Lambda$TreadlyVideoStatsViewController$L5AtbYNNhiVbGMjnrBbUSWrB7dA
                    @Override // android.view.View.OnClickListener
                    public final void onClick(View view) {
                        r0.runOnMain(new $$Lambda$TreadlyVideoStatsViewController$PmMgIB5KKuXNJEYveYaE5jusAWQ(TreadlyVideoStatsViewController.this));
                    }
                });
                this.trainerModeButton.setText(R.string.end_trainer_mode);
            }
        }
        this.statsRecycler = (RecyclerView) this.popupView.findViewById(R.id.video_stats_recycler);
        this.statsAdapter.updateStats(this.friendsStats);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this.context);
        linearLayoutManager.setOrientation(1);
        this.statsRecycler.setLayoutManager(linearLayoutManager);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(this.statsRecycler.getContext(), linearLayoutManager.getOrientation());
        dividerItemDecoration.setDrawable(ContextCompat.getDrawable(this.context, R.drawable.unclaimed_list_divider));
        this.statsRecycler.addItemDecoration(dividerItemDecoration);
        this.statsRecycler.setAdapter(this.statsAdapter);
        this.blurView = layoutInflater.inflate(R.layout.blur_popup, (ViewGroup) null);
        this.blurView.setOnClickListener(new View.OnClickListener() { // from class: com.treadly.Treadly.UI.TreadlyVideo.VideoUserStats.-$$Lambda$TreadlyVideoStatsViewController$LznXGNgj1tz8w3bCZQL4sxkOWsY
            @Override // android.view.View.OnClickListener
            public final void onClick(View view) {
                TreadlyVideoStatsViewController.this.popupWindow.dismiss();
            }
        });
        this.blurBackgroundPopup = new PopupWindow(this.blurView, -1, -1);
        this.popupWindow = new PopupWindow(this.popupView, (int) this.context.getResources().getDimension(R.dimen._275sdp), (int) this.context.getResources().getDimension(R.dimen._430sdp));
        this.popupWindow.setTouchable(true);
        this.popupWindow.setFocusable(true);
        this.popupWindow.setBackgroundDrawable(ContextCompat.getDrawable(this.context, R.drawable.unclaimed_list_background));
        this.popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() { // from class: com.treadly.Treadly.UI.TreadlyVideo.VideoUserStats.-$$Lambda$TreadlyVideoStatsViewController$wDhX1WyXIKyx9TV7QtklIOsRyG8
            @Override // android.widget.PopupWindow.OnDismissListener
            public final void onDismiss() {
                TreadlyVideoStatsViewController.this.handleDismiss();
            }
        });
    }

    public void handleDismiss() {
        if (this.blurBackgroundPopup != null) {
            this.blurBackgroundPopup.dismiss();
        }
        if (this.datePickerPopup != null) {
            this.datePickerPopup.dismiss();
        }
    }

    public void handleTrainerModePressed(View view, Button button) {
        PopupMenu trainerModePopupMenu = getTrainerModePopupMenu(view, button);
        this.trainerModeMenu = trainerModePopupMenu;
        trainerModePopupMenu.inflate(R.menu.trainer_mode_popup);
        trainerModePopupMenu.show();
    }

    /* renamed from: com.treadly.Treadly.UI.TreadlyVideo.VideoUserStats.TreadlyVideoStatsViewController$1 */
    /* loaded from: classes2.dex */
    public class AnonymousClass1 implements PopupMenu.OnMenuItemClickListener {
        AnonymousClass1() {
            TreadlyVideoStatsViewController.this = r1;
        }

        @Override // android.widget.PopupMenu.OnMenuItemClickListener
        @SuppressLint({"NonConstantResourceId"})
        public boolean onMenuItemClick(MenuItem menuItem) {
            int itemId = menuItem.getItemId();
            if (itemId == R.id.student_mode) {
                TreadlyVideoStatsViewController.this.runOnMain(new Runnable() { // from class: com.treadly.Treadly.UI.TreadlyVideo.VideoUserStats.-$$Lambda$TreadlyVideoStatsViewController$1$6ZeSrNIb2JnauchUoLWn56eMZEI
                    @Override // java.lang.Runnable
                    public final void run() {
                        TreadlyVideoStatsViewController.this.onStudentModePressed();
                    }
                });
                return true;
            } else if (itemId != R.id.trainer_mode) {
                return false;
            } else {
                TreadlyVideoStatsViewController.this.runOnMain(new Runnable() { // from class: com.treadly.Treadly.UI.TreadlyVideo.VideoUserStats.-$$Lambda$TreadlyVideoStatsViewController$1$kScLa2-00R923FGzSK04_WKrbR4
                    @Override // java.lang.Runnable
                    public final void run() {
                        TreadlyVideoStatsViewController.this.onTrainerModePressed();
                    }
                });
                return true;
            }
        }
    }

    private PopupMenu getTrainerModePopupMenu(View view, Button button) {
        PopupMenu popupMenu = new PopupMenu(this.context, button);
        popupMenu.setOnMenuItemClickListener(new AnonymousClass1());
        return popupMenu;
    }

    public void showListPopup() {
        this.blurBackgroundPopup.showAsDropDown(this.blurView);
        this.popupWindow.showAtLocation(this.popupView, 48, 0, (int) this.context.getResources().getDimension(R.dimen._60sdp));
    }

    public void onTrainerModePressed() {
        switch (this.trainerMode) {
            case inactive:
                sendStopStudentModeRequest();
                cancelStudentModeRequest();
                sendStartTrainerModeRequest();
                if (this.trainerModeButton != null) {
                    this.trainerModeButton.setText(R.string.end_trainer_mode);
                    this.trainerModeButton.setOnClickListener(new View.OnClickListener() { // from class: com.treadly.Treadly.UI.TreadlyVideo.VideoUserStats.-$$Lambda$TreadlyVideoStatsViewController$IBx-9duO49L4oDms_q-m8x0UJ2k
                        @Override // android.view.View.OnClickListener
                        public final void onClick(View view) {
                            r0.runOnMain(new $$Lambda$TreadlyVideoStatsViewController$PmMgIB5KKuXNJEYveYaE5jusAWQ(TreadlyVideoStatsViewController.this));
                        }
                    });
                    return;
                }
                return;
            case active:
                sendStopTrainerModeRequest();
                if (this.trainerModeButton != null) {
                    this.trainerModeButton.setText(R.string.start_trainer_mode);
                    this.trainerModeButton.setOnClickListener(new View.OnClickListener() { // from class: com.treadly.Treadly.UI.TreadlyVideo.VideoUserStats.-$$Lambda$TreadlyVideoStatsViewController$FcFpuyj2r6aJgUbIJuy4xsFESwU
                        @Override // android.view.View.OnClickListener
                        public final void onClick(View view) {
                            r0.runOnMain(new Runnable() { // from class: com.treadly.Treadly.UI.TreadlyVideo.VideoUserStats.-$$Lambda$TreadlyVideoStatsViewController$2XVm9cnx7yucTqtmJtpTaIhMYtI
                                @Override // java.lang.Runnable
                                public final void run() {
                                    r0.handleTrainerModePressed(r0.popupView, TreadlyVideoStatsViewController.this.trainerModeButton);
                                }
                            });
                        }
                    });
                    return;
                }
                return;
            default:
                Log.i(this.TAG, "Not supported");
                return;
        }
    }

    public void onStudentModePressed() {
        switch (this.studentMode) {
            case inactive:
                runOnMain(new Runnable() { // from class: com.treadly.Treadly.UI.TreadlyVideo.VideoUserStats.-$$Lambda$TreadlyVideoStatsViewController$dlq2f8n6t2T3XTr36CSvN_NpfEE
                    @Override // java.lang.Runnable
                    public final void run() {
                        TreadlyVideoStatsViewController.this.showTrainerModeList();
                    }
                });
                return;
            case active:
                sendStopStudentModeRequest();
                if (this.trainerModeButton != null) {
                    this.trainerModeButton.setText(R.string.start_trainer_mode);
                    this.trainerModeButton.setOnClickListener(new View.OnClickListener() { // from class: com.treadly.Treadly.UI.TreadlyVideo.VideoUserStats.-$$Lambda$TreadlyVideoStatsViewController$82Av88vxsHELVHt3faxDY4nObB4
                        @Override // android.view.View.OnClickListener
                        public final void onClick(View view) {
                            r0.runOnMain(new Runnable() { // from class: com.treadly.Treadly.UI.TreadlyVideo.VideoUserStats.-$$Lambda$TreadlyVideoStatsViewController$oHT9yN2vHYcU-ELSKppB8YYS_D4
                                @Override // java.lang.Runnable
                                public final void run() {
                                    r0.handleTrainerModePressed(r0.popupView, TreadlyVideoStatsViewController.this.trainerModeButton);
                                }
                            });
                        }
                    });
                    return;
                }
                return;
            default:
                Log.i(this.TAG, "Not supported");
                return;
        }
    }

    public void showTrainerModeList() {
        ArrayList arrayList = new ArrayList();
        for (UsersTrainerModes usersTrainerModes : this.usersTrainerModes) {
            if (usersTrainerModes.trainerModeState == TrainerModeState.inactive && usersTrainerModes.studentModeState == TrainerModeState.inactive && !usersTrainerModes.user.id.equals(this.currentUserId) && isUserConnectedToDevice(usersTrainerModes.user.id)) {
                arrayList.add(usersTrainerModes.user.name);
            }
        }
        this.trainerNames = (CharSequence[]) arrayList.toArray(new CharSequence[0]);
        new AlertDialog.Builder(this.context).setTitle("Select Trainer").setItems(this.trainerNames, new DialogInterface.OnClickListener() { // from class: com.treadly.Treadly.UI.TreadlyVideo.VideoUserStats.-$$Lambda$TreadlyVideoStatsViewController$ZdhBPCQPvwBT6ZN67cQIyH_ypyo
            @Override // android.content.DialogInterface.OnClickListener
            public final void onClick(DialogInterface dialogInterface, int i) {
                TreadlyVideoStatsViewController.lambda$showTrainerModeList$12(TreadlyVideoStatsViewController.this, dialogInterface, i);
            }
        }).create().show();
    }

    public static /* synthetic */ void lambda$showTrainerModeList$12(TreadlyVideoStatsViewController treadlyVideoStatsViewController, DialogInterface dialogInterface, int i) {
        for (UsersTrainerModes usersTrainerModes : treadlyVideoStatsViewController.usersTrainerModes) {
            if (usersTrainerModes.user.name.contentEquals(treadlyVideoStatsViewController.trainerNames[i])) {
                treadlyVideoStatsViewController.sendStartStudentModeRequest(usersTrainerModes.user.id);
            }
        }
        if (treadlyVideoStatsViewController.trainerModeButton != null) {
            treadlyVideoStatsViewController.trainerModeButton.setText(R.string.end_student_mode);
            treadlyVideoStatsViewController.trainerModeButton.setOnClickListener(new View.OnClickListener() { // from class: com.treadly.Treadly.UI.TreadlyVideo.VideoUserStats.-$$Lambda$TreadlyVideoStatsViewController$HVrazRH03xfyb-6d_VDr6p72Jq4
                @Override // android.view.View.OnClickListener
                public final void onClick(View view) {
                    r0.runOnMain(new $$Lambda$TreadlyVideoStatsViewController$6jpYnjipSzX83Hp_1_n2wiFbG48(TreadlyVideoStatsViewController.this));
                }
            });
        }
    }

    private void sendStartStudentModeRequest(String str) {
        if (this.studentMode == TrainerModeState.inactive) {
            if (this.adapter != null) {
                this.adapter.studentModeRequested(TrainerModeState.active, str);
            }
            this.pendingStudentModeTargetId = str;
        }
    }

    private void sendStopStudentModeRequest() {
        if (this.studentMode == TrainerModeState.active) {
            String str = this.pendingStudentModeTargetId;
            if (str == null) {
                str = "";
            }
            if (this.adapter != null) {
                this.adapter.studentModeRequested(TrainerModeState.inactive, str);
            }
            this.pendingStudentModeTargetId = null;
        }
    }

    private void cancelStudentModeRequest() {
        if (this.studentMode != TrainerModeState.pending || this.pendingStudentModeTargetId == null) {
            return;
        }
        if (this.adapter != null) {
            this.adapter.studentModeRequested(TrainerModeState.inactive, this.pendingStudentModeTargetId);
        }
        this.pendingStudentModeTargetId = null;
    }

    private void sendStartTrainerModeRequest() {
        if (this.trainerMode != TrainerModeState.inactive || this.adapter == null) {
            return;
        }
        this.adapter.trainerModeRequested(TrainerModeState.active);
    }

    private void sendStopTrainerModeRequest() {
        if (this.trainerMode != TrainerModeState.active || this.adapter == null) {
            return;
        }
        this.adapter.trainerModeRequested(TrainerModeState.inactive);
    }

    private void cancelTrainerModeRequest() {
        if (this.trainerMode != TrainerModeState.pending || this.adapter == null) {
            return;
        }
        this.adapter.trainerModeRequested(TrainerModeState.inactive);
    }

    public void resendTrainerModeRequest() {
        if (this.trainerMode != TrainerModeState.active || this.adapter == null) {
            return;
        }
        this.adapter.trainerModeRequested(TrainerModeState.active);
    }

    public void enableTrainerModeButtons(boolean z) {
        this.trainerModeButton.setVisibility(z ? 0 : 4);
        this.dividerTwo.setVisibility(z ? 0 : 4);
    }

    public void runOnMain(Runnable runnable) {
        new Handler(Looper.getMainLooper()).post(runnable);
    }

    public void updateRunSessionInfo(VideoServiceUserStatsInfo videoServiceUserStatsInfo) {
        videoServiceUserStatsInfo.order = 1;
        this.userStats = videoServiceUserStatsInfo;
        this.friendsStatsInfoLookup.put(videoServiceUserStatsInfo.userId, videoServiceUserStatsInfo);
        this.friendsStats = new ArrayList(this.friendsStatsInfoLookup.values());
        runOnMain(new Runnable() { // from class: com.treadly.Treadly.UI.TreadlyVideo.VideoUserStats.-$$Lambda$TreadlyVideoStatsViewController$GNTlSYNR3-jIZIto7F4bMv4XKOE
            @Override // java.lang.Runnable
            public final void run() {
                TreadlyVideoStatsViewController.lambda$updateRunSessionInfo$13(TreadlyVideoStatsViewController.this);
            }
        });
    }

    public static /* synthetic */ void lambda$updateRunSessionInfo$13(TreadlyVideoStatsViewController treadlyVideoStatsViewController) {
        treadlyVideoStatsViewController.statsAdapter.updateStats(treadlyVideoStatsViewController.friendsStats);
        treadlyVideoStatsViewController.statsAdapter.notifyDataSetChanged();
    }

    public void updateFriendRunSession(VideoServiceUserStatsInfo videoServiceUserStatsInfo) {
        if (this.friendsStatsInfoLookup.get(videoServiceUserStatsInfo.userId) != null) {
            videoServiceUserStatsInfo.order = this.friendsStatsInfoLookup.get(videoServiceUserStatsInfo.userId).order;
        }
        this.friendsStatsInfoLookup.put(videoServiceUserStatsInfo.userId, videoServiceUserStatsInfo);
        this.friendsStats = new ArrayList(this.friendsStatsInfoLookup.values());
        runOnMain(new Runnable() { // from class: com.treadly.Treadly.UI.TreadlyVideo.VideoUserStats.-$$Lambda$TreadlyVideoStatsViewController$cvX0DR-JL_Hl7f0E3ZU2bycc0_o
            @Override // java.lang.Runnable
            public final void run() {
                TreadlyVideoStatsViewController.lambda$updateFriendRunSession$14(TreadlyVideoStatsViewController.this);
            }
        });
    }

    public static /* synthetic */ void lambda$updateFriendRunSession$14(TreadlyVideoStatsViewController treadlyVideoStatsViewController) {
        treadlyVideoStatsViewController.statsAdapter.updateStats(treadlyVideoStatsViewController.friendsStats);
        treadlyVideoStatsViewController.statsAdapter.notifyDataSetChanged();
    }

    public void addFriendStats(UserInfo userInfo) {
        String str = this.TAG;
        Log.d(str, "adding friend: " + userInfo.name);
        VideoServiceUserStatsInfo initialStats = getInitialStats(userInfo);
        initialStats.order = this.friendsStatsInfoLookup.size() + 1;
        this.friendsStatsInfoLookup.put(userInfo.id, initialStats);
        this.friendsStats = new ArrayList(this.friendsStatsInfoLookup.values());
        runOnMain(new Runnable() { // from class: com.treadly.Treadly.UI.TreadlyVideo.VideoUserStats.-$$Lambda$TreadlyVideoStatsViewController$FJXSkeAoQIIOVOOSwp-eXntU2CI
            @Override // java.lang.Runnable
            public final void run() {
                TreadlyVideoStatsViewController.lambda$addFriendStats$15(TreadlyVideoStatsViewController.this);
            }
        });
    }

    public static /* synthetic */ void lambda$addFriendStats$15(TreadlyVideoStatsViewController treadlyVideoStatsViewController) {
        treadlyVideoStatsViewController.statsAdapter.updateStats(treadlyVideoStatsViewController.friendsStats);
        treadlyVideoStatsViewController.statsAdapter.notifyDataSetChanged();
    }

    public void removeFriendStats(String str) {
        final VideoServiceUserStatsInfo videoServiceUserStatsInfo = this.friendsStatsInfoLookup.get(str);
        if (videoServiceUserStatsInfo != null) {
            this.friendsStatsInfoLookup.forEach(new BiConsumer() { // from class: com.treadly.Treadly.UI.TreadlyVideo.VideoUserStats.-$$Lambda$TreadlyVideoStatsViewController$KoxuDqPzOBlvOfFl39AoQBWbK-8
                @Override // java.util.function.BiConsumer
                public final void accept(Object obj, Object obj2) {
                    TreadlyVideoStatsViewController.lambda$removeFriendStats$16(VideoServiceUserStatsInfo.this, (String) obj, (VideoServiceUserStatsInfo) obj2);
                }
            });
        }
        this.friendsUserInfoLookup.remove(str);
        this.friendsStatsInfoLookup.remove(str);
        userDeviceConnectionStates.remove(str);
        this.friendsStats = new ArrayList(this.friendsStatsInfoLookup.values());
        runOnMain(new Runnable() { // from class: com.treadly.Treadly.UI.TreadlyVideo.VideoUserStats.-$$Lambda$TreadlyVideoStatsViewController$h1PV6o-1yyRHFRSb7l2ou8SImfg
            @Override // java.lang.Runnable
            public final void run() {
                TreadlyVideoStatsViewController.lambda$removeFriendStats$17(TreadlyVideoStatsViewController.this);
            }
        });
    }

    public static /* synthetic */ void lambda$removeFriendStats$16(VideoServiceUserStatsInfo videoServiceUserStatsInfo, String str, VideoServiceUserStatsInfo videoServiceUserStatsInfo2) {
        if (videoServiceUserStatsInfo2.order > videoServiceUserStatsInfo.order) {
            videoServiceUserStatsInfo2.order--;
        }
    }

    public static /* synthetic */ void lambda$removeFriendStats$17(TreadlyVideoStatsViewController treadlyVideoStatsViewController) {
        treadlyVideoStatsViewController.statsAdapter.updateStats(treadlyVideoStatsViewController.friendsStats);
        treadlyVideoStatsViewController.statsAdapter.notifyDataSetChanged();
    }

    public void addInitialUserStats(UserInfo userInfo) {
        VideoServiceUserStatsInfo initialStats = getInitialStats(userInfo);
        this.userStats = initialStats;
        this.userStats.order = 1;
        this.friendsStatsInfoLookup.put(userInfo.id, initialStats);
        this.friendsStats = new ArrayList(this.friendsStatsInfoLookup.values());
    }

    private VideoServiceUserStatsInfo getInitialStats(UserInfo userInfo) {
        return new VideoServiceUserStatsInfo(userInfo.id, userInfo.name, 0, Utils.DOUBLE_EPSILON, 0, Utils.DOUBLE_EPSILON, Utils.DOUBLE_EPSILON, new ArrayList(), DistanceUnits.MI, 0);
    }

    public void syncUserStates(List<UserVideoPrivateStateInfo> list) {
        for (UserVideoPrivateStateInfo userVideoPrivateStateInfo : list) {
            setUserDeviceConnectionState(userVideoPrivateStateInfo.id, userVideoPrivateStateInfo.isTreadmillConnected);
            Log.d("trainer", "sync userStates id: " + userVideoPrivateStateInfo.id + " : " + userVideoPrivateStateInfo.isTreadmillConnected);
        }
    }

    public void setUserDeviceConnectionState(String str, boolean z) {
        Boolean orDefault = userDeviceConnectionStatesVerified.getOrDefault(str, null);
        if (orDefault != null) {
            z = orDefault.booleanValue();
        }
        userDeviceConnectionStates.put(str, Boolean.valueOf(z));
        if (this.isHost) {
            updateUserStatesByConnection();
            updateCurrentTrainerModeState();
            updateCurrentStudentModes();
            if (this.adapter != null) {
                this.adapter.broadcastUserTrainerModes(this.usersTrainerModes);
            }
        }
    }

    public void handleUserJoined(UserInfo userInfo) {
        if (this.isHost) {
            for (UsersTrainerModes usersTrainerModes : this.usersTrainerModes) {
                if (usersTrainerModes.user.id.equals(userInfo.id)) {
                    usersTrainerModes.studentModeState = TrainerModeState.inactive;
                    usersTrainerModes.trainerModeState = TrainerModeState.inactive;
                    if (this.adapter != null) {
                        this.adapter.broadcastUserTrainerModes(this.usersTrainerModes);
                        return;
                    } else {
                        Log.e(this.TAG, "Adapter not set");
                        return;
                    }
                }
            }
            this.usersTrainerModes.add(new UsersTrainerModes(userInfo, TrainerModeState.inactive, TrainerModeState.inactive));
            if (this.adapter != null) {
                this.adapter.broadcastUserTrainerModes(this.usersTrainerModes);
            } else {
                Log.e(this.TAG, "Adapter not set");
            }
            checkTrainerModeEnabled(userInfo);
        }
    }

    public void handleUserLeft(UserInfo userInfo) {
        if (this.isHost) {
            Iterator<UsersTrainerModes> it = this.usersTrainerModes.iterator();
            while (true) {
                if (!it.hasNext()) {
                    break;
                } else if (it.next().user.id.equals(userInfo.id)) {
                    it.remove();
                    break;
                }
            }
            userDeviceConnectionStatesVerified.remove(userInfo.id);
            updateUserStatesByConnection();
            updateCurrentTrainerModeState();
            updateCurrentStudentModes();
            if (this.adapter != null) {
                this.adapter.broadcastUserTrainerModes(this.usersTrainerModes);
            } else {
                Log.e(this.TAG, "Adapter not set");
            }
        }
    }

    public void handleStudentModeRequest(final String str, final UserTrainerMode userTrainerMode) {
        if (!this.currentUserId.equals(str) || userTrainerMode.state != TrainerModeState.active) {
            Log.d("trainer", "current: " + this.currentUserId);
            Log.d("trainer", "targetUserId: " + str);
            Log.d("trainer", "state: " + userTrainerMode.state);
        } else if (isUserConnectedToDevice(str)) {
            runOnMain(new Runnable() { // from class: com.treadly.Treadly.UI.TreadlyVideo.VideoUserStats.-$$Lambda$TreadlyVideoStatsViewController$NhmMLGRQawxMl2jchVtk0C2wJ1E
                @Override // java.lang.Runnable
                public final void run() {
                    TreadlyVideoStatsViewController.this.showTrainerModeInvite(r1.user.name, userTrainerMode.user.avatarURL(), str, false);
                }
            });
        } else {
            Log.d("trainer", "targetUser " + str + " not connected");
        }
        if (this.isHost) {
            switch (userTrainerMode.state) {
                case inactive:
                    boolean z = false;
                    for (UsersTrainerModes usersTrainerModes : this.usersTrainerModes) {
                        if (usersTrainerModes.user.id.equals(userTrainerMode.user.id)) {
                            usersTrainerModes.trainerModeState = TrainerModeState.inactive;
                            usersTrainerModes.studentModeState = TrainerModeState.inactive;
                        } else if (usersTrainerModes.studentModeState == TrainerModeState.pending || usersTrainerModes.studentModeState == TrainerModeState.active) {
                            z = true;
                        }
                    }
                    if (!z) {
                        for (UsersTrainerModes usersTrainerModes2 : this.usersTrainerModes) {
                            usersTrainerModes2.trainerModeState = TrainerModeState.inactive;
                            usersTrainerModes2.studentModeState = TrainerModeState.inactive;
                        }
                        break;
                    }
                    break;
                case active:
                    for (UsersTrainerModes usersTrainerModes3 : this.usersTrainerModes) {
                        if (usersTrainerModes3.user.id.equals(userTrainerMode.user.id)) {
                            usersTrainerModes3.trainerModeState = TrainerModeState.inactive;
                            usersTrainerModes3.studentModeState = TrainerModeState.pending;
                        } else if (usersTrainerModes3.user.id.equals(str)) {
                            usersTrainerModes3.trainerModeState = TrainerModeState.inactive;
                            usersTrainerModes3.studentModeState = TrainerModeState.invited;
                        }
                    }
                    break;
                default:
                    Log.i(this.TAG, "State not supported");
                    break;
            }
            updateUserStatesByConnection();
            updateCurrentTrainerModeState();
            if (this.adapter != null) {
                this.adapter.broadcastUserTrainerModes(this.usersTrainerModes);
            }
        }
    }

    public void handleTrainerModeRequest(final UserTrainerMode userTrainerMode) {
        if (!this.currentUserId.equals(userTrainerMode.user.id) && this.studentMode == TrainerModeState.inactive && this.trainerMode == TrainerModeState.inactive && userTrainerMode.state == TrainerModeState.active && isUserConnectedToDevice(this.currentUserId)) {
            runOnMain(new Runnable() { // from class: com.treadly.Treadly.UI.TreadlyVideo.VideoUserStats.-$$Lambda$TreadlyVideoStatsViewController$DE0szPBR8Pchg-SVynH8_EWvMVQ
                @Override // java.lang.Runnable
                public final void run() {
                    TreadlyVideoStatsViewController.this.showTrainerModeInvite(r1.user.name, userTrainerMode.user.avatarURL(), null, true);
                }
            });
        }
        if (this.isHost) {
            Log.d("trainer", "state change to: " + userTrainerMode.state);
            switch (userTrainerMode.state) {
                case inactive:
                    UsersTrainerModes currentTrainerUserMode = getCurrentTrainerUserMode();
                    boolean equals = currentTrainerUserMode != null ? currentTrainerUserMode.user.id.equals(userTrainerMode.user.id) : false;
                    for (UsersTrainerModes usersTrainerModes : this.usersTrainerModes) {
                        if (equals || usersTrainerModes.user.id.equals(userTrainerMode.user.id)) {
                            usersTrainerModes.trainerModeState = TrainerModeState.inactive;
                            usersTrainerModes.studentModeState = TrainerModeState.inactive;
                        }
                    }
                    break;
                case active:
                    for (UsersTrainerModes usersTrainerModes2 : this.usersTrainerModes) {
                        if (!usersTrainerModes2.user.id.equals(userTrainerMode.user.id) && usersTrainerModes2.studentModeState == TrainerModeState.inactive) {
                            if (isUserConnectedToDevice(usersTrainerModes2.user.id)) {
                                usersTrainerModes2.studentModeState = TrainerModeState.invited;
                                usersTrainerModes2.trainerModeState = TrainerModeState.inactive;
                            }
                        } else if (usersTrainerModes2.trainerModeState == TrainerModeState.inactive && usersTrainerModes2.studentModeState == TrainerModeState.inactive) {
                            usersTrainerModes2.trainerModeState = TrainerModeState.pending;
                            this.pendingTrainerModeUser = usersTrainerModes2;
                        }
                    }
                    break;
                default:
                    Log.i(this.TAG, "Not supported");
                    break;
            }
            updateUserStatesByConnection();
            updateCurrentTrainerModeState();
            if (this.adapter != null) {
                this.adapter.broadcastUserTrainerModes(this.usersTrainerModes);
            }
        }
    }

    public void handleStudentModeResponse(String str, UserTrainerMode userTrainerMode) {
        if (this.isHost) {
            switch (userTrainerMode.state) {
                case inactive:
                    for (UsersTrainerModes usersTrainerModes : this.usersTrainerModes) {
                        usersTrainerModes.studentModeState = TrainerModeState.inactive;
                        usersTrainerModes.trainerModeState = TrainerModeState.inactive;
                    }
                    break;
                case active:
                    boolean z = false;
                    for (UsersTrainerModes usersTrainerModes2 : this.usersTrainerModes) {
                        if (usersTrainerModes2.studentModeState == TrainerModeState.pending) {
                            usersTrainerModes2.studentModeState = TrainerModeState.active;
                            usersTrainerModes2.trainerModeState = TrainerModeState.inactive;
                            z = true;
                        }
                    }
                    for (UsersTrainerModes usersTrainerModes3 : this.usersTrainerModes) {
                        if (usersTrainerModes3.user.id.equals(str)) {
                            usersTrainerModes3.studentModeState = TrainerModeState.inactive;
                            usersTrainerModes3.trainerModeState = z ? TrainerModeState.active : TrainerModeState.inactive;
                        }
                    }
                    break;
                default:
                    Log.i(this.TAG, "State not supported");
                    break;
            }
            updateUserStatesByConnection();
            updateCurrentTrainerModeState();
            if (this.adapter != null) {
                this.adapter.broadcastUserTrainerModes(this.usersTrainerModes);
            }
        }
    }

    public void handleTrainerModeResponse(UserTrainerMode userTrainerMode) {
        if (this.isHost) {
            switch (userTrainerMode.state) {
                case inactive:
                    for (UsersTrainerModes usersTrainerModes : this.usersTrainerModes) {
                        if (usersTrainerModes.user.id.equals(userTrainerMode.user.id)) {
                            usersTrainerModes.trainerModeState = TrainerModeState.inactive;
                            usersTrainerModes.studentModeState = TrainerModeState.inactive;
                        }
                    }
                    break;
                case active:
                    UsersTrainerModes currentTrainerUserMode = getCurrentTrainerUserMode();
                    if (currentTrainerUserMode != null) {
                        for (UsersTrainerModes usersTrainerModes2 : this.usersTrainerModes) {
                            if (usersTrainerModes2.user.id.equals(userTrainerMode.user.id)) {
                                usersTrainerModes2.studentModeState = TrainerModeState.active;
                                usersTrainerModes2.trainerModeState = TrainerModeState.inactive;
                            }
                        }
                        for (UsersTrainerModes usersTrainerModes3 : this.usersTrainerModes) {
                            if (usersTrainerModes3.user.id.equals(currentTrainerUserMode.user.id)) {
                                usersTrainerModes3.studentModeState = TrainerModeState.inactive;
                                usersTrainerModes3.trainerModeState = TrainerModeState.active;
                            }
                        }
                        break;
                    }
                    break;
                default:
                    Log.i(this.TAG, "Not supported");
                    break;
            }
            updateUserStatesByConnection();
            updateCurrentTrainerModeState();
            if (this.adapter != null) {
                this.adapter.broadcastUserTrainerModes(this.usersTrainerModes);
            }
        }
    }

    public void handleUserTrainerModes(List<UsersTrainerModes> list) {
        this.usersTrainerModes = list;
        for (UsersTrainerModes usersTrainerModes : this.usersTrainerModes) {
            if (usersTrainerModes.user.id.equals(this.currentUserId)) {
                setTrainerMode(usersTrainerModes.trainerModeState);
                setStudentMode(usersTrainerModes.studentModeState);
                if (this.trainerMode != TrainerModeState.invited && this.studentMode != TrainerModeState.invited) {
                    dismissTrainerModeInvite();
                }
                if (this.studentMode == TrainerModeState.invited) {
                    createStudentModeInvite();
                }
                if (this.trainerMode != TrainerModeState.inactive) {
                    TrainerModeState trainerModeState = this.trainerMode;
                    TrainerModeState trainerModeState2 = TrainerModeState.invited;
                }
                if (this.studentMode != TrainerModeState.inactive) {
                    TrainerModeState trainerModeState3 = this.studentMode;
                    TrainerModeState trainerModeState4 = TrainerModeState.invited;
                }
                if (this.trainerMode == TrainerModeState.active) {
                    for (UsersTrainerModes usersTrainerModes2 : this.usersTrainerModes) {
                        if (!usersTrainerModes2.user.id.equals(this.currentUserId) || usersTrainerModes2.studentModeState != TrainerModeState.inactive || usersTrainerModes.trainerModeState != TrainerModeState.inactive) {
                        }
                    }
                }
            }
        }
        for (UsersTrainerModes usersTrainerModes3 : this.usersTrainerModes) {
            if (this.friendsStatsInfoLookup.get(usersTrainerModes3.user.id) == null) {
                addFriendStats(usersTrainerModes3.user);
            }
        }
    }

    public void checkTrainerModeEnabled(final UserInfo userInfo) {
        VideoServiceHelper.getTrainerModeEnabled(userInfo.id, new VideoServiceHelper.VideoGetTrainerModeEnabled() { // from class: com.treadly.Treadly.UI.TreadlyVideo.VideoUserStats.-$$Lambda$TreadlyVideoStatsViewController$K3K6hZcW3fYoBnAw8noILutY-GE
            @Override // com.treadly.Treadly.UI.TreadlyVideo.Data.VideoServiceHelper.VideoGetTrainerModeEnabled
            public final void onResponse(String str, TrainerModeEnabledState trainerModeEnabledState) {
                TreadlyVideoStatsViewController.lambda$checkTrainerModeEnabled$20(TreadlyVideoStatsViewController.this, userInfo, str, trainerModeEnabledState);
            }
        });
    }

    public static /* synthetic */ void lambda$checkTrainerModeEnabled$20(TreadlyVideoStatsViewController treadlyVideoStatsViewController, UserInfo userInfo, String str, TrainerModeEnabledState trainerModeEnabledState) {
        for (UsersTrainerModes usersTrainerModes : treadlyVideoStatsViewController.usersTrainerModes) {
            if (usersTrainerModes.user.id.equals(userInfo.id)) {
                usersTrainerModes.trainerModeEnabled = trainerModeEnabledState;
            }
        }
        if (treadlyVideoStatsViewController.adapter != null) {
            treadlyVideoStatsViewController.adapter.broadcastUserTrainerModes(treadlyVideoStatsViewController.usersTrainerModes);
        } else {
            Log.e(treadlyVideoStatsViewController.TAG, "Adapter not set");
        }
        treadlyVideoStatsViewController.updateTrainerModeEnabled(true);
    }

    private void updateUserStatesByConnection() {
        for (UsersTrainerModes usersTrainerModes : this.usersTrainerModes) {
            if (!isUserConnectedToDevice(usersTrainerModes.user.id)) {
                usersTrainerModes.trainerModeState = TrainerModeState.inactive;
                usersTrainerModes.studentModeState = TrainerModeState.inactive;
            }
        }
    }

    /* JADX WARN: Removed duplicated region for block: B:45:0x0013  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    private void updateCurrentTrainerModeState() {
        /*
            r6 = this;
            com.treadly.Treadly.Data.Model.UsersTrainerModes r0 = r6.getCurrentTrainerUserMode()
            if (r0 == 0) goto L60
            r1 = 0
            java.util.List<com.treadly.Treadly.Data.Model.UsersTrainerModes> r2 = r6.usersTrainerModes
            java.util.Iterator r2 = r2.iterator()
        Ld:
            boolean r3 = r2.hasNext()
            if (r3 == 0) goto L26
            java.lang.Object r3 = r2.next()
            com.treadly.Treadly.Data.Model.UsersTrainerModes r3 = (com.treadly.Treadly.Data.Model.UsersTrainerModes) r3
            com.treadly.Treadly.Data.Model.TrainerModeState r4 = r3.studentModeState
            com.treadly.Treadly.Data.Model.TrainerModeState r5 = com.treadly.Treadly.Data.Model.TrainerModeState.invited
            if (r4 == r5) goto L25
            com.treadly.Treadly.Data.Model.TrainerModeState r3 = r3.studentModeState
            com.treadly.Treadly.Data.Model.TrainerModeState r4 = com.treadly.Treadly.Data.Model.TrainerModeState.active
            if (r3 != r4) goto Ld
        L25:
            r1 = 1
        L26:
            if (r1 != 0) goto L60
            java.util.List<com.treadly.Treadly.Data.Model.UsersTrainerModes> r1 = r6.usersTrainerModes
            java.util.Iterator r1 = r1.iterator()
        L2e:
            boolean r2 = r1.hasNext()
            if (r2 == 0) goto L5d
            java.lang.Object r2 = r1.next()
            com.treadly.Treadly.Data.Model.UsersTrainerModes r2 = (com.treadly.Treadly.Data.Model.UsersTrainerModes) r2
            com.treadly.Treadly.Data.Model.UserInfo r3 = r2.user
            java.lang.String r3 = r3.id
            com.treadly.Treadly.Data.Model.UserInfo r4 = r0.user
            java.lang.String r4 = r4.id
            boolean r3 = r3.equals(r4)
            if (r3 == 0) goto L2e
            com.treadly.Treadly.Data.Model.TrainerModeState r3 = r2.trainerModeState
            com.treadly.Treadly.Data.Model.TrainerModeState r4 = com.treadly.Treadly.Data.Model.TrainerModeState.pending
            if (r3 == r4) goto L54
            com.treadly.Treadly.Data.Model.TrainerModeState r3 = r2.trainerModeState
            com.treadly.Treadly.Data.Model.TrainerModeState r4 = com.treadly.Treadly.Data.Model.TrainerModeState.active
            if (r3 != r4) goto L2e
        L54:
            com.treadly.Treadly.Data.Model.TrainerModeState r3 = com.treadly.Treadly.Data.Model.TrainerModeState.inactive
            r2.trainerModeState = r3
            com.treadly.Treadly.Data.Model.TrainerModeState r3 = com.treadly.Treadly.Data.Model.TrainerModeState.inactive
            r2.studentModeState = r3
            goto L2e
        L5d:
            r0 = 0
            r6.pendingTrainerModeUser = r0
        L60:
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.treadly.Treadly.UI.TreadlyVideo.VideoUserStats.TreadlyVideoStatsViewController.updateCurrentTrainerModeState():void");
    }

    private void updateCurrentStudentModes() {
        if (getCurrentTrainerUserMode() == null) {
            for (UsersTrainerModes usersTrainerModes : this.usersTrainerModes) {
                if (usersTrainerModes.studentModeState == TrainerModeState.active || usersTrainerModes.studentModeState == TrainerModeState.pending) {
                    usersTrainerModes.studentModeState = TrainerModeState.inactive;
                    usersTrainerModes.studentModeState = TrainerModeState.inactive;
                }
            }
        }
    }

    public void updateTrainerModeEnabled(boolean z) {
        if (isTrainerModeEnabled()) {
            return;
        }
        setStudentMode(TrainerModeState.inactive);
        setTrainerMode(TrainerModeState.inactive);
        if (this.isHost) {
            for (UsersTrainerModes usersTrainerModes : this.usersTrainerModes) {
                usersTrainerModes.studentModeState = TrainerModeState.inactive;
                usersTrainerModes.trainerModeState = TrainerModeState.inactive;
            }
            if (!z || this.adapter == null) {
                return;
            }
            this.adapter.broadcastUserTrainerModes(this.usersTrainerModes);
        }
    }

    public void updateUserDeviceConnectionState(String str, boolean z) {
        Log.d("trainer", "updateUserDeviceConnectionState: " + str + " isConnected: " + z);
        userDeviceConnectionStates.put(str, Boolean.valueOf(z));
        if (userDeviceConnectionStatesVerified.getOrDefault(str, null) == null && z) {
            setStudentModeInviteUser(str);
        }
        userDeviceConnectionStatesVerified.put(str, Boolean.valueOf(z));
        if (this.isHost) {
            updateUserStatesByConnection();
            updateCurrentTrainerModeState();
            updateCurrentStudentModes();
            if (this.adapter != null) {
                this.adapter.broadcastUserTrainerModes(this.usersTrainerModes);
            } else {
                Log.e(this.TAG, "Adapter not set");
            }
        }
    }

    public void showTrainerModeView() {
        Log.d(this.TAG, "Showing trainer mode");
    }

    public void createStudentModeInvite() {
        final UsersTrainerModes usersTrainerModes;
        Log.d("trainer", "createStudentModeInvite");
        if (this.trainerModeAlert != null) {
            return;
        }
        loop0: while (true) {
            usersTrainerModes = null;
            for (UsersTrainerModes usersTrainerModes2 : this.usersTrainerModes) {
                if (usersTrainerModes2.trainerModeState == TrainerModeState.active || usersTrainerModes2.trainerModeState == TrainerModeState.pending) {
                    if (usersTrainerModes == null) {
                        Log.d("trainer", "found trainer");
                        usersTrainerModes = usersTrainerModes2;
                    }
                }
            }
        }
        if (usersTrainerModes != null) {
            Log.d("trainer", "going to show alert");
            runOnMain(new Runnable() { // from class: com.treadly.Treadly.UI.TreadlyVideo.VideoUserStats.TreadlyVideoStatsViewController.2
                {
                    TreadlyVideoStatsViewController.this = this;
                }

                @Override // java.lang.Runnable
                public void run() {
                    TreadlyVideoStatsViewController.this.showTrainerModeInvite(usersTrainerModes.user.name, usersTrainerModes.user.avatarPath, null, true);
                }
            });
            return;
        }
        Log.d("trainer", "No trainer found");
    }

    public void showTrainerModeInvite(String str, String str2, final String str3, final boolean z) {
        if (this.trainerModeAlert != null) {
            this.trainerModeAlert.dismiss();
        }
        this.trainerModeAlert = null;
        Log.d("trainer", "showTrainerModeInvite");
        String str4 = z ? "Student Mode" : "Trainer Mode";
        StringBuilder sb = new StringBuilder();
        sb.append("%s has requested to begin ");
        sb.append(z ? "Student Mode" : "Trainer Mode");
        this.trainerModeAlert = new AlertDialog.Builder(this.context).setTitle(str4).setMessage(String.format(sb.toString(), str)).setPositiveButton("Accept", new DialogInterface.OnClickListener() { // from class: com.treadly.Treadly.UI.TreadlyVideo.VideoUserStats.-$$Lambda$TreadlyVideoStatsViewController$6xEJOcTehbU9zpiBjRWO8fgLnE0
            @Override // android.content.DialogInterface.OnClickListener
            public final void onClick(DialogInterface dialogInterface, int i) {
                TreadlyVideoStatsViewController.lambda$showTrainerModeInvite$21(TreadlyVideoStatsViewController.this, z, str3, dialogInterface, i);
            }
        }).setNegativeButton("Decline", new DialogInterface.OnClickListener() { // from class: com.treadly.Treadly.UI.TreadlyVideo.VideoUserStats.-$$Lambda$TreadlyVideoStatsViewController$-Bo-nBcignSu4udwZKYilcdZ5wE
            @Override // android.content.DialogInterface.OnClickListener
            public final void onClick(DialogInterface dialogInterface, int i) {
                TreadlyVideoStatsViewController.lambda$showTrainerModeInvite$22(TreadlyVideoStatsViewController.this, z, str3, dialogInterface, i);
            }
        }).create();
        Log.d("trainer", "showing alert");
        this.trainerModeAlert.show();
    }

    public static /* synthetic */ void lambda$showTrainerModeInvite$21(TreadlyVideoStatsViewController treadlyVideoStatsViewController, boolean z, String str, DialogInterface dialogInterface, int i) {
        if (z) {
            if (treadlyVideoStatsViewController.adapter != null) {
                treadlyVideoStatsViewController.adapter.trainerModeResponded(TrainerModeState.active);
                treadlyVideoStatsViewController.setStudentMode(TrainerModeState.active);
            }
        } else if (str != null && treadlyVideoStatsViewController.adapter != null) {
            treadlyVideoStatsViewController.adapter.studentModeResponded(TrainerModeState.active, str);
        }
        treadlyVideoStatsViewController.trainerModeAlert = null;
    }

    public static /* synthetic */ void lambda$showTrainerModeInvite$22(TreadlyVideoStatsViewController treadlyVideoStatsViewController, boolean z, String str, DialogInterface dialogInterface, int i) {
        if (z) {
            if (treadlyVideoStatsViewController.adapter != null) {
                treadlyVideoStatsViewController.adapter.trainerModeResponded(TrainerModeState.inactive);
                treadlyVideoStatsViewController.setStudentMode(TrainerModeState.inactive);
            }
        } else if (str != null && treadlyVideoStatsViewController.adapter != null) {
            treadlyVideoStatsViewController.adapter.studentModeResponded(TrainerModeState.inactive, str);
        }
        treadlyVideoStatsViewController.trainerModeAlert = null;
    }

    public UsersTrainerModes getCurrentTrainerUserMode() {
        UsersTrainerModes usersTrainerModes = null;
        for (UsersTrainerModes usersTrainerModes2 : this.usersTrainerModes) {
            if (usersTrainerModes2.trainerModeState == TrainerModeState.active || usersTrainerModes2.trainerModeState == TrainerModeState.pending) {
                if (usersTrainerModes != null) {
                    return null;
                }
                usersTrainerModes = usersTrainerModes2;
            }
        }
        return usersTrainerModes;
    }

    private void dismissTrainerModeInvite() {
        if (this.trainerModeAlert != null) {
            this.trainerModeAlert.dismiss();
        }
    }

    public boolean isTrainerModeEnabled() {
        for (UsersTrainerModes usersTrainerModes : this.usersTrainerModes) {
            if (usersTrainerModes.trainerModeEnabled == TrainerModeEnabledState.enabled) {
                return true;
            }
        }
        return false;
    }

    private void setStudentModeInviteUser(String str) {
        if (isTrainerModeInSession() && isUserConnectedToDevice(str)) {
            for (UsersTrainerModes usersTrainerModes : this.usersTrainerModes) {
                if (usersTrainerModes.user.id.equals(str) && usersTrainerModes.studentModeState == TrainerModeState.inactive && usersTrainerModes.trainerModeState == TrainerModeState.inactive) {
                    usersTrainerModes.studentModeState = TrainerModeState.invited;
                    usersTrainerModes.trainerModeState = TrainerModeState.inactive;
                }
            }
        }
    }

    /* JADX WARN: Removed duplicated region for block: B:22:0x000c  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    private boolean isTrainerModeInSession() {
        /*
            r3 = this;
            java.util.List<com.treadly.Treadly.Data.Model.UsersTrainerModes> r3 = r3.usersTrainerModes
            java.util.Iterator r3 = r3.iterator()
        L6:
            boolean r0 = r3.hasNext()
            if (r0 == 0) goto L20
            java.lang.Object r0 = r3.next()
            com.treadly.Treadly.Data.Model.UsersTrainerModes r0 = (com.treadly.Treadly.Data.Model.UsersTrainerModes) r0
            com.treadly.Treadly.Data.Model.TrainerModeState r1 = r0.trainerModeState
            com.treadly.Treadly.Data.Model.TrainerModeState r2 = com.treadly.Treadly.Data.Model.TrainerModeState.active
            if (r1 == r2) goto L1e
            com.treadly.Treadly.Data.Model.TrainerModeState r0 = r0.trainerModeState
            com.treadly.Treadly.Data.Model.TrainerModeState r1 = com.treadly.Treadly.Data.Model.TrainerModeState.pending
            if (r0 != r1) goto L6
        L1e:
            r3 = 1
            return r3
        L20:
            r3 = 0
            return r3
        */
        throw new UnsupportedOperationException("Method not decompiled: com.treadly.Treadly.UI.TreadlyVideo.VideoUserStats.TreadlyVideoStatsViewController.isTrainerModeInSession():boolean");
    }

    private boolean isUserConnectedToDevice(String str) {
        return userDeviceConnectionStates.getOrDefault(str, false).booleanValue();
    }
}
