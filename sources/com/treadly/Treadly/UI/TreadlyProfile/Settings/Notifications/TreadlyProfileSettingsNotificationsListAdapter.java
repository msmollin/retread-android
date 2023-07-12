package com.treadly.Treadly.UI.TreadlyProfile.Settings.Notifications;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.CompoundButton;
import android.widget.ExpandableListView;
import android.widget.TextView;
import androidx.appcompat.widget.SwitchCompat;
import com.facebook.share.internal.ShareConstants;
import com.treadly.Treadly.Data.Model.UserNotificationSettingInfo;
import com.treadly.Treadly.R;
import com.treadly.Treadly.UI.TreadlyProfile.Settings.Notifications.TreadlyProfileSettingsNotificationsFragment;
import java.util.ArrayList;
import java.util.List;

/* loaded from: classes2.dex */
public class TreadlyProfileSettingsNotificationsListAdapter extends BaseExpandableListAdapter {
    private Context context;
    protected TreadlyProfileSettingsNotificationsListener listener;
    protected UserNotificationSettingInfo userNotifications;
    protected List<TreadlyProfileSettingsNotificationsFragment.NotificationSettingsSection> sectionTypes = new ArrayList();
    protected List<TreadlyProfileSettingsNotificationsFragment.NotificationSettingsGoals> goalTypes = new ArrayList();
    protected List<TreadlyProfileSettingsNotificationsFragment.NotificationSettingsGroups> groupTypes = new ArrayList();
    protected List<TreadlyProfileSettingsNotificationsFragment.NotificationSettingsFriends> friendTypes = new ArrayList();

    /* loaded from: classes2.dex */
    protected interface TreadlyProfileSettingsNotificationsListener {
        void friendTypeSwitched(TreadlyProfileSettingsNotificationsFragment.NotificationSettingsFriends notificationSettingsFriends, boolean z);

        void goalTypeSwitched(TreadlyProfileSettingsNotificationsFragment.NotificationSettingsGoals notificationSettingsGoals, boolean z);

        void groupTypeSwitched(TreadlyProfileSettingsNotificationsFragment.NotificationSettingsGroups notificationSettingsGroups, boolean z);
    }

    @Override // android.widget.ExpandableListAdapter
    public Object getChild(int i, int i2) {
        return null;
    }

    @Override // android.widget.ExpandableListAdapter
    public long getChildId(int i, int i2) {
        return i2;
    }

    @Override // android.widget.ExpandableListAdapter
    public Object getGroup(int i) {
        return null;
    }

    @Override // android.widget.ExpandableListAdapter
    public long getGroupId(int i) {
        return i;
    }

    @Override // android.widget.ExpandableListAdapter
    public boolean hasStableIds() {
        return false;
    }

    @Override // android.widget.ExpandableListAdapter
    public boolean isChildSelectable(int i, int i2) {
        return false;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public TreadlyProfileSettingsNotificationsListAdapter(Context context, TreadlyProfileSettingsNotificationsListener treadlyProfileSettingsNotificationsListener) {
        this.context = context;
        this.listener = treadlyProfileSettingsNotificationsListener;
    }

    @Override // android.widget.ExpandableListAdapter
    public int getGroupCount() {
        return this.sectionTypes.size();
    }

    @Override // android.widget.ExpandableListAdapter
    public int getChildrenCount(int i) {
        switch (this.sectionTypes.get(i)) {
            case goals:
                return this.goalTypes.size();
            case groups:
                return this.groupTypes.size();
            case friends:
                return this.friendTypes.size();
            default:
                return 0;
        }
    }

    @Override // android.widget.ExpandableListAdapter
    public View getGroupView(int i, boolean z, View view, ViewGroup viewGroup) {
        if (this.sectionTypes.get(i) == null) {
            return null;
        }
        if (view == null) {
            LayoutInflater layoutInflater = (LayoutInflater) this.context.getSystemService("layout_inflater");
            if (layoutInflater == null) {
                return null;
            }
            view = layoutInflater.inflate(R.layout.list_view_treadly_profile_settings_default_header, viewGroup, false);
            DefaultHeaderHolder defaultHeaderHolder = new DefaultHeaderHolder();
            defaultHeaderHolder.textView = (TextView) view.findViewById(R.id.header_text_view);
            defaultHeaderHolder.subTextView = (TextView) view.findViewById(R.id.sub_header_text_view);
            view.setTag(defaultHeaderHolder);
        }
        ((ExpandableListView) viewGroup).expandGroup(i);
        DefaultHeaderHolder defaultHeaderHolder2 = (DefaultHeaderHolder) view.getTag();
        defaultHeaderHolder2.textView.setText(titleForHeader(i));
        defaultHeaderHolder2.subTextView.setVisibility(8);
        return view;
    }

    @Override // android.widget.ExpandableListAdapter
    public View getChildView(int i, int i2, boolean z, View view, ViewGroup viewGroup) {
        TreadlyProfileSettingsNotificationsFragment.NotificationSettingsSection notificationSettingsSection = this.sectionTypes.get(i);
        if (notificationSettingsSection == null) {
            return null;
        }
        if (view == null) {
            LayoutInflater layoutInflater = (LayoutInflater) this.context.getSystemService("layout_inflater");
            if (layoutInflater == null) {
                return null;
            }
            view = layoutInflater.inflate(R.layout.list_view_treadly_profile_settings_default_switch_row, viewGroup, false);
        }
        switch (notificationSettingsSection) {
            case goals:
                return getGoalRow(i2, view);
            case groups:
                return getGroupRow(i2, view);
            case friends:
                return getFriendRow(i2, view);
            default:
                return view;
        }
    }

    private String titleForHeader(int i) {
        switch (this.sectionTypes.get(i)) {
            case goals:
                return "GOALS";
            case groups:
                return "GROUPS";
            case friends:
                return ShareConstants.PEOPLE_IDS;
            default:
                return "";
        }
    }

    private View getGoalRow(int i, View view) {
        if (view == null) {
            return null;
        }
        DefaultRowHolder defaultRowHolder = (DefaultRowHolder) view.getTag();
        if (defaultRowHolder == null) {
            defaultRowHolder = new DefaultRowHolder();
            defaultRowHolder.textView = (TextView) view.findViewById(R.id.item_text_view);
            defaultRowHolder.switchItem = (SwitchCompat) view.findViewById(R.id.item_switch);
            view.setTag(defaultRowHolder);
        }
        boolean z = true;
        switch (this.goalTypes.get(i)) {
            case dailyGoal50:
                defaultRowHolder.textView.setText(R.string.profile_setting_notification_daily_goal_50_title);
                defaultRowHolder.switchItem.setOnCheckedChangeListener(null);
                SwitchCompat switchCompat = defaultRowHolder.switchItem;
                if (this.userNotifications == null || !this.userNotifications.dailyGoal50Achieved) {
                    z = false;
                }
                switchCompat.setChecked(z);
                defaultRowHolder.switchItem.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() { // from class: com.treadly.Treadly.UI.TreadlyProfile.Settings.Notifications.TreadlyProfileSettingsNotificationsListAdapter.1
                    @Override // android.widget.CompoundButton.OnCheckedChangeListener
                    public void onCheckedChanged(CompoundButton compoundButton, boolean z2) {
                        if (TreadlyProfileSettingsNotificationsListAdapter.this.listener == null) {
                            return;
                        }
                        TreadlyProfileSettingsNotificationsListAdapter.this.listener.goalTypeSwitched(TreadlyProfileSettingsNotificationsFragment.NotificationSettingsGoals.dailyGoal50, z2);
                    }
                });
                break;
            case dailyGoal100:
                defaultRowHolder.textView.setText(R.string.profile_setting_notification_daily_goal_100_title);
                defaultRowHolder.switchItem.setOnCheckedChangeListener(null);
                SwitchCompat switchCompat2 = defaultRowHolder.switchItem;
                if (this.userNotifications == null || !this.userNotifications.dailyGoal100Achieved) {
                    z = false;
                }
                switchCompat2.setChecked(z);
                defaultRowHolder.switchItem.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() { // from class: com.treadly.Treadly.UI.TreadlyProfile.Settings.Notifications.TreadlyProfileSettingsNotificationsListAdapter.2
                    @Override // android.widget.CompoundButton.OnCheckedChangeListener
                    public void onCheckedChanged(CompoundButton compoundButton, boolean z2) {
                        if (TreadlyProfileSettingsNotificationsListAdapter.this.listener == null) {
                            return;
                        }
                        TreadlyProfileSettingsNotificationsListAdapter.this.listener.goalTypeSwitched(TreadlyProfileSettingsNotificationsFragment.NotificationSettingsGoals.dailyGoal100, z2);
                    }
                });
                break;
        }
        return view;
    }

    private View getGroupRow(int i, View view) {
        if (view == null) {
            return null;
        }
        DefaultRowHolder defaultRowHolder = (DefaultRowHolder) view.getTag();
        if (defaultRowHolder == null) {
            defaultRowHolder = new DefaultRowHolder();
            defaultRowHolder.textView = (TextView) view.findViewById(R.id.item_text_view);
            defaultRowHolder.switchItem = (SwitchCompat) view.findViewById(R.id.item_switch);
            view.setTag(defaultRowHolder);
        }
        switch (this.groupTypes.get(i)) {
            case groupRequestApproval:
                defaultRowHolder.textView.setText("Group request / approvals");
                defaultRowHolder.switchItem.setOnCheckedChangeListener(null);
                defaultRowHolder.switchItem.setChecked(true);
                break;
            case newPost:
                defaultRowHolder.textView.setText("New post to your group");
                defaultRowHolder.switchItem.setOnCheckedChangeListener(null);
                defaultRowHolder.switchItem.setChecked(true);
                break;
            case workoutSchedule:
                defaultRowHolder.textView.setText("Workout schedule");
                defaultRowHolder.switchItem.setOnCheckedChangeListener(null);
                defaultRowHolder.switchItem.setChecked(true);
                break;
            case replyToPost:
                defaultRowHolder.textView.setText("Reply to your post");
                defaultRowHolder.switchItem.setOnCheckedChangeListener(null);
                defaultRowHolder.switchItem.setChecked(true);
                break;
            case replyToComment:
                defaultRowHolder.textView.setText("Reply to your comment");
                defaultRowHolder.switchItem.setOnCheckedChangeListener(null);
                defaultRowHolder.switchItem.setChecked(true);
                break;
            case likeToPost:
                defaultRowHolder.textView.setText("Like to your post");
                defaultRowHolder.switchItem.setOnCheckedChangeListener(null);
                defaultRowHolder.switchItem.setChecked(true);
                break;
        }
        return view;
    }

    private View getFriendRow(int i, View view) {
        if (view == null) {
            return null;
        }
        DefaultRowHolder defaultRowHolder = (DefaultRowHolder) view.getTag();
        if (defaultRowHolder == null) {
            defaultRowHolder = new DefaultRowHolder();
            defaultRowHolder.textView = (TextView) view.findViewById(R.id.item_text_view);
            defaultRowHolder.switchItem = (SwitchCompat) view.findViewById(R.id.item_switch);
            view.setTag(defaultRowHolder);
        }
        switch (this.friendTypes.get(i)) {
            case friendRequests:
                defaultRowHolder.textView.setText("Friend requests");
                defaultRowHolder.switchItem.setOnCheckedChangeListener(null);
                defaultRowHolder.switchItem.setChecked(true);
                break;
            case videoInvite:
                defaultRowHolder.textView.setText("Video invite");
                defaultRowHolder.switchItem.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() { // from class: com.treadly.Treadly.UI.TreadlyProfile.Settings.Notifications.TreadlyProfileSettingsNotificationsListAdapter.3
                    @Override // android.widget.CompoundButton.OnCheckedChangeListener
                    public void onCheckedChanged(CompoundButton compoundButton, boolean z) {
                        compoundButton.setChecked(z);
                    }
                });
                break;
            case videoLike:
                defaultRowHolder.textView.setText("Like to your video");
                defaultRowHolder.switchItem.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() { // from class: com.treadly.Treadly.UI.TreadlyProfile.Settings.Notifications.TreadlyProfileSettingsNotificationsListAdapter.4
                    @Override // android.widget.CompoundButton.OnCheckedChangeListener
                    public void onCheckedChanged(CompoundButton compoundButton, boolean z) {
                        compoundButton.setChecked(z);
                    }
                });
                break;
        }
        return view;
    }

    /* loaded from: classes2.dex */
    private class DefaultHeaderHolder {
        private TextView subTextView;
        private TextView textView;

        private DefaultHeaderHolder() {
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes2.dex */
    public class DefaultRowHolder {
        private SwitchCompat switchItem;
        private TextView textView;

        private DefaultRowHolder() {
        }
    }
}
