package com.treadly.Treadly.UI.TreadlyProfile.Settings.Notifications;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;
import android.widget.ImageButton;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.treadly.Treadly.Data.Model.UserNotificationSettingInfo;
import com.treadly.Treadly.Data.Model.UserNotificationSettingRequest;
import com.treadly.Treadly.Data.Utility.NotificationCenter.NotificationCenter;
import com.treadly.Treadly.Data.Utility.NotificationCenter.NotificationType;
import com.treadly.Treadly.R;
import com.treadly.Treadly.UI.TreadlyProfile.Settings.Notifications.TreadlyProfileSettingsNotificationsListAdapter;
import com.treadly.Treadly.UI.TreadlyVideo.Data.VideoServiceHelper;
import com.treadly.Treadly.UI.Util.BaseClasses.BaseFragment;
import java.util.Arrays;
import java.util.HashMap;

/* loaded from: classes2.dex */
public class TreadlyProfileSettingsNotificationsFragment extends BaseFragment {
    private TreadlyProfileSettingsNotificationsListAdapter adapter;
    private ExpandableListView notificationListView;
    private ExpandableListView notificationsListView;
    private int sequence = 0;
    TreadlyProfileSettingsNotificationsListAdapter.TreadlyProfileSettingsNotificationsListener listener = new TreadlyProfileSettingsNotificationsListAdapter.TreadlyProfileSettingsNotificationsListener() { // from class: com.treadly.Treadly.UI.TreadlyProfile.Settings.Notifications.TreadlyProfileSettingsNotificationsFragment.1
        @Override // com.treadly.Treadly.UI.TreadlyProfile.Settings.Notifications.TreadlyProfileSettingsNotificationsListAdapter.TreadlyProfileSettingsNotificationsListener
        public void goalTypeSwitched(NotificationSettingsGoals notificationSettingsGoals, boolean z) {
            TreadlyProfileSettingsNotificationsFragment.this.goalTypeSwitched(notificationSettingsGoals, z);
        }

        @Override // com.treadly.Treadly.UI.TreadlyProfile.Settings.Notifications.TreadlyProfileSettingsNotificationsListAdapter.TreadlyProfileSettingsNotificationsListener
        public void groupTypeSwitched(NotificationSettingsGroups notificationSettingsGroups, boolean z) {
            TreadlyProfileSettingsNotificationsFragment.this.groupTypeSwitched(notificationSettingsGroups, z);
        }

        @Override // com.treadly.Treadly.UI.TreadlyProfile.Settings.Notifications.TreadlyProfileSettingsNotificationsListAdapter.TreadlyProfileSettingsNotificationsListener
        public void friendTypeSwitched(NotificationSettingsFriends notificationSettingsFriends, boolean z) {
            TreadlyProfileSettingsNotificationsFragment.this.friendTypeSwitched(notificationSettingsFriends, z);
        }
    };

    /* JADX INFO: Access modifiers changed from: protected */
    /* loaded from: classes2.dex */
    public enum NotificationSettingsFriends {
        friendRequests,
        videoInvite,
        videoLike
    }

    /* JADX INFO: Access modifiers changed from: protected */
    /* loaded from: classes2.dex */
    public enum NotificationSettingsGoals {
        dailyGoal50,
        dailyGoal100
    }

    /* JADX INFO: Access modifiers changed from: protected */
    /* loaded from: classes2.dex */
    public enum NotificationSettingsGroups {
        groupRequestApproval,
        newPost,
        workoutSchedule,
        replyToPost,
        replyToComment,
        likeToPost
    }

    /* JADX INFO: Access modifiers changed from: protected */
    /* loaded from: classes2.dex */
    public enum NotificationSettingsSection {
        goals,
        groups,
        friends
    }

    public void groupTypeSwitched(NotificationSettingsGroups notificationSettingsGroups, boolean z) {
    }

    @Override // androidx.fragment.app.Fragment
    @Nullable
    public View onCreateView(@NonNull LayoutInflater layoutInflater, @Nullable ViewGroup viewGroup, @Nullable Bundle bundle) {
        return layoutInflater.inflate(R.layout.fragment_treadly_profile_settings_notifications, viewGroup, false);
    }

    @Override // com.treadly.Treadly.UI.Util.BaseClasses.BaseFragment, androidx.fragment.app.Fragment
    public void onViewCreated(@NonNull View view, @Nullable Bundle bundle) {
        super.onViewCreated(view, bundle);
        ((TextView) view.findViewById(R.id.nav_title)).setText(R.string.notification_fragment_title);
        ((ImageButton) view.findViewById(R.id.nav_back_arrow)).setOnClickListener(new View.OnClickListener() { // from class: com.treadly.Treadly.UI.TreadlyProfile.Settings.Notifications.-$$Lambda$TreadlyProfileSettingsNotificationsFragment$WO1tAbH-brqD9GigYTPn_IKxgSI
            @Override // android.view.View.OnClickListener
            public final void onClick(View view2) {
                TreadlyProfileSettingsNotificationsFragment.this.popBackStack();
            }
        });
        this.notificationListView = (ExpandableListView) view.findViewById(R.id.notification_list_view);
        initList();
        loadNotificationSettings();
    }

    private void initList() {
        this.adapter = new TreadlyProfileSettingsNotificationsListAdapter(getContext(), this.listener);
        NotificationSettingsSection[] notificationSettingsSectionArr = {NotificationSettingsSection.goals};
        NotificationSettingsGoals[] notificationSettingsGoalsArr = {NotificationSettingsGoals.dailyGoal50, NotificationSettingsGoals.dailyGoal100};
        NotificationSettingsFriends[] notificationSettingsFriendsArr = {NotificationSettingsFriends.friendRequests, NotificationSettingsFriends.videoInvite, NotificationSettingsFriends.videoLike};
        this.adapter.sectionTypes = Arrays.asList(notificationSettingsSectionArr);
        this.adapter.goalTypes = Arrays.asList(notificationSettingsGoalsArr);
        this.adapter.groupTypes = Arrays.asList(new NotificationSettingsGroups[0]);
        this.adapter.friendTypes = Arrays.asList(notificationSettingsFriendsArr);
        this.notificationListView.setAdapter(this.adapter);
        this.notificationListView.setChildDivider(null);
        this.notificationListView.setChildIndicator(null);
        this.notificationListView.setDivider(null);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void loadNotificationSettings() {
        VideoServiceHelper.getNotificationSettings(new VideoServiceHelper.VideoNotificationsListener() { // from class: com.treadly.Treadly.UI.TreadlyProfile.Settings.Notifications.TreadlyProfileSettingsNotificationsFragment.2
            @Override // com.treadly.Treadly.UI.TreadlyVideo.Data.VideoServiceHelper.VideoNotificationsListener
            public void onResponse(String str, UserNotificationSettingInfo userNotificationSettingInfo) {
                TreadlyProfileSettingsNotificationsFragment.this.dismissLoading();
                if (str != null || userNotificationSettingInfo == null) {
                    return;
                }
                TreadlyProfileSettingsNotificationsFragment.this.adapter.userNotifications = userNotificationSettingInfo;
                TreadlyProfileSettingsNotificationsFragment.this.adapter.notifyDataSetChanged();
            }
        });
    }

    public void goalTypeSwitched(NotificationSettingsGoals notificationSettingsGoals, boolean z) {
        UserNotificationSettingInfo userNotificationSettingInfo;
        if (this.adapter == null || (userNotificationSettingInfo = this.adapter.userNotifications) == null) {
            return;
        }
        switch (notificationSettingsGoals) {
            case dailyGoal100:
                userNotificationSettingInfo.dailyGoal100Achieved = z;
                break;
            case dailyGoal50:
                userNotificationSettingInfo.dailyGoal50Achieved = z;
                break;
        }
        updateSettings(userNotificationSettingInfo);
    }

    public void friendTypeSwitched(NotificationSettingsFriends notificationSettingsFriends, boolean z) {
        UserNotificationSettingInfo userNotificationSettingInfo;
        if (this.adapter == null || (userNotificationSettingInfo = this.adapter.userNotifications) == null || AnonymousClass4.$SwitchMap$com$treadly$Treadly$UI$TreadlyProfile$Settings$Notifications$TreadlyProfileSettingsNotificationsFragment$NotificationSettingsFriends[notificationSettingsFriends.ordinal()] != 1) {
            return;
        }
        userNotificationSettingInfo.friendRequest = z;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* renamed from: com.treadly.Treadly.UI.TreadlyProfile.Settings.Notifications.TreadlyProfileSettingsNotificationsFragment$4  reason: invalid class name */
    /* loaded from: classes2.dex */
    public static /* synthetic */ class AnonymousClass4 {
        static final /* synthetic */ int[] $SwitchMap$com$treadly$Treadly$UI$TreadlyProfile$Settings$Notifications$TreadlyProfileSettingsNotificationsFragment$NotificationSettingsFriends = new int[NotificationSettingsFriends.values().length];

        static {
            try {
                $SwitchMap$com$treadly$Treadly$UI$TreadlyProfile$Settings$Notifications$TreadlyProfileSettingsNotificationsFragment$NotificationSettingsFriends[NotificationSettingsFriends.friendRequests.ordinal()] = 1;
            } catch (NoSuchFieldError unused) {
            }
            $SwitchMap$com$treadly$Treadly$UI$TreadlyProfile$Settings$Notifications$TreadlyProfileSettingsNotificationsFragment$NotificationSettingsGoals = new int[NotificationSettingsGoals.values().length];
            try {
                $SwitchMap$com$treadly$Treadly$UI$TreadlyProfile$Settings$Notifications$TreadlyProfileSettingsNotificationsFragment$NotificationSettingsGoals[NotificationSettingsGoals.dailyGoal100.ordinal()] = 1;
            } catch (NoSuchFieldError unused2) {
            }
            try {
                $SwitchMap$com$treadly$Treadly$UI$TreadlyProfile$Settings$Notifications$TreadlyProfileSettingsNotificationsFragment$NotificationSettingsGoals[NotificationSettingsGoals.dailyGoal50.ordinal()] = 2;
            } catch (NoSuchFieldError unused3) {
            }
        }
    }

    private void updateSettings(UserNotificationSettingInfo userNotificationSettingInfo) {
        this.sequence++;
        final int i = this.sequence;
        showLoading();
        VideoServiceHelper.updateNotificationSettings(getRequest(userNotificationSettingInfo), new VideoServiceHelper.VideoResponseListener() { // from class: com.treadly.Treadly.UI.TreadlyProfile.Settings.Notifications.TreadlyProfileSettingsNotificationsFragment.3
            @Override // com.treadly.Treadly.UI.TreadlyVideo.Data.VideoServiceHelper.VideoResponseListener
            public void onResponse(String str) {
                if (i != TreadlyProfileSettingsNotificationsFragment.this.sequence) {
                    TreadlyProfileSettingsNotificationsFragment.this.dismissLoading();
                    return;
                }
                TreadlyProfileSettingsNotificationsFragment.this.loadNotificationSettings();
                NotificationCenter.postNotification(TreadlyProfileSettingsNotificationsFragment.this.getContext(), NotificationType.onNotificationsChanged, new HashMap());
            }
        });
    }

    private UserNotificationSettingRequest getRequest(UserNotificationSettingInfo userNotificationSettingInfo) {
        return new UserNotificationSettingRequest(userNotificationSettingInfo.enable, userNotificationSettingInfo.dailyGoal50Achieved, userNotificationSettingInfo.dailyGoal100Achieved, userNotificationSettingInfo.groupRequestApproval, userNotificationSettingInfo.newPostToYourGroup, userNotificationSettingInfo.workoutSchedule, userNotificationSettingInfo.replyToYourGroupPost, userNotificationSettingInfo.replyToYourGroupComment, userNotificationSettingInfo.likeToYourVideo, userNotificationSettingInfo.likeToYourGroupPost, userNotificationSettingInfo.friendRequest, userNotificationSettingInfo.videoInvite, userNotificationSettingInfo.friendOnlineStatus);
    }
}
