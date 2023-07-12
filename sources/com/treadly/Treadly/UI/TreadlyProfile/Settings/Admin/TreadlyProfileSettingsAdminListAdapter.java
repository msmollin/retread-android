package com.treadly.Treadly.UI.TreadlyProfile.Settings.Admin;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.CompoundButton;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.appcompat.widget.SwitchCompat;
import androidx.fragment.app.FragmentActivity;
import com.treadly.Treadly.MainActivity;
import com.treadly.Treadly.R;
import com.treadly.Treadly.UI.TreadlyProfile.Settings.Admin.TreadlyProfileSettingsAdminFragment;
import com.treadly.Treadly.UI.TreadlyProfile.Settings.AutoRun.TreadmillAutoRunManager;
import com.treadly.client.lib.sdk.Model.AuthenticationState;
import com.treadly.client.lib.sdk.Model.IrMode;
import java.util.ArrayList;
import java.util.List;

/* loaded from: classes2.dex */
public class TreadlyProfileSettingsAdminListAdapter extends BaseExpandableListAdapter {
    private FragmentActivity activity;
    private Context context;
    private TreadlyProfileSettingsAdminFragment parent;
    protected List<TreadlyProfileSettingsAdminFragment.SectionTypes> sectionTypes = new ArrayList();
    protected List<TreadlyProfileSettingsAdminFragment.AdminRowTypes> adminRowTypes = new ArrayList();

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

    /* JADX INFO: Access modifiers changed from: protected */
    public TreadlyProfileSettingsAdminListAdapter(Context context, TreadlyProfileSettingsAdminFragment treadlyProfileSettingsAdminFragment, FragmentActivity fragmentActivity) {
        this.context = context;
        this.parent = treadlyProfileSettingsAdminFragment;
        this.activity = fragmentActivity;
    }

    @Override // android.widget.ExpandableListAdapter
    public int getGroupCount() {
        this.parent.updateSections();
        return this.sectionTypes.size();
    }

    @Override // android.widget.ExpandableListAdapter
    public int getChildrenCount(int i) {
        this.parent.updateRows();
        switch (this.sectionTypes.get(i)) {
            case settings:
                return 0;
            case admin:
                return this.adminRowTypes.size();
            default:
                return 0;
        }
    }

    @Override // android.widget.ExpandableListAdapter
    public View getGroupView(int i, boolean z, View view, ViewGroup viewGroup) {
        View view2;
        if (view == null) {
            LayoutInflater layoutInflater = (LayoutInflater) this.context.getSystemService("layout_inflater");
            if (layoutInflater == null) {
                return null;
            }
            view2 = layoutInflater.inflate(R.layout.list_view_treadly_profile_settings_default_header, viewGroup, false);
            DefaultHeaderHolder defaultHeaderHolder = new DefaultHeaderHolder();
            defaultHeaderHolder.textView = (TextView) view2.findViewById(R.id.header_text_view);
            defaultHeaderHolder.subTextView = (TextView) view2.findViewById(R.id.sub_header_text_view);
            view2.setTag(defaultHeaderHolder);
        } else {
            view2 = view;
        }
        DefaultHeaderHolder defaultHeaderHolder2 = (DefaultHeaderHolder) view2.getTag();
        ((ExpandableListView) viewGroup).expandGroup(i);
        if (AnonymousClass6.$SwitchMap$com$treadly$Treadly$UI$TreadlyProfile$Settings$Admin$TreadlyProfileSettingsAdminFragment$SectionTypes[this.sectionTypes.get(i).ordinal()] == 2) {
            defaultHeaderHolder2.textView.setText(R.string.admin_settings_title_capitalized);
            defaultHeaderHolder2.subTextView.setVisibility(8);
        }
        return view2;
    }

    @Override // android.widget.ExpandableListAdapter
    public View getChildView(int i, int i2, boolean z, View view, ViewGroup viewGroup) {
        if (this.sectionTypes.get(i) == TreadlyProfileSettingsAdminFragment.SectionTypes.admin) {
            TreadlyProfileSettingsAdminFragment.AdminRowTypes adminRowTypes = this.adminRowTypes.get(i2);
            switch (adminRowTypes) {
                case admin:
                    return getAdminSwitchRow(view, viewGroup, adminRowTypes);
                case adminEhs:
                    return getAdminSwitchRow(view, viewGroup, adminRowTypes);
                case adminEnableBle:
                    return getAdminSwitchRow(view, viewGroup, adminRowTypes);
                case adminIdleConfig:
                    return getAdminRow(view, viewGroup, adminRowTypes);
                case adminMaintenance:
                    return getAdminRow(view, viewGroup, adminRowTypes);
                case adminSetIrMode:
                    return getAdminSwitchRow(view, viewGroup, adminRowTypes);
                case adminSetAuthenticationState:
                    return getAdminRow(view, viewGroup, adminRowTypes);
                case adminSetAutoRun:
                    return getAdminRow(view, viewGroup, adminRowTypes);
                case adminSetDeviceIrDebugMode:
                    return getAdminSwitchRow(view, viewGroup, adminRowTypes);
                case adminSetTotalStatus:
                    return getAdminRow(view, viewGroup, adminRowTypes);
                default:
                    return null;
            }
        }
        return null;
    }

    @Override // android.widget.ExpandableListAdapter
    public boolean isChildSelectable(int i, int i2) {
        if (this.sectionTypes.get(i) != TreadlyProfileSettingsAdminFragment.SectionTypes.admin) {
            return false;
        }
        switch (this.adminRowTypes.get(i2)) {
            case admin:
                return false;
            case adminEhs:
                return false;
            case adminEnableBle:
                return false;
            case adminIdleConfig:
                return true;
            case adminMaintenance:
                return true;
            case adminSetIrMode:
                return false;
            case adminSetAuthenticationState:
                return true;
            case adminSetAutoRun:
                return true;
            case adminSetDeviceIrDebugMode:
                return false;
            case adminSetTotalStatus:
                return true;
            default:
                return false;
        }
    }

    private View getAdminRow(View view, ViewGroup viewGroup, TreadlyProfileSettingsAdminFragment.AdminRowTypes adminRowTypes) {
        if (view == null || view.getTag() == null || !(view.getTag() instanceof DefaultRowHolder)) {
            LayoutInflater layoutInflater = (LayoutInflater) this.context.getSystemService("layout_inflater");
            if (layoutInflater == null) {
                return null;
            }
            view = layoutInflater.inflate(R.layout.list_view_treadly_profile_settings_default_detail_row, viewGroup, false);
            DefaultRowHolder defaultRowHolder = new DefaultRowHolder();
            defaultRowHolder.textView = (TextView) view.findViewById(R.id.header_text_view);
            defaultRowHolder.detailTextView = (TextView) view.findViewById(R.id.detail_text_view);
            defaultRowHolder.detailArrow = (ImageView) view.findViewById(R.id.detail_arrow);
            view.setTag(defaultRowHolder);
        }
        DefaultRowHolder defaultRowHolder2 = (DefaultRowHolder) view.getTag();
        int i = AnonymousClass6.$SwitchMap$com$treadly$Treadly$UI$TreadlyProfile$Settings$Admin$TreadlyProfileSettingsAdminFragment$AdminRowTypes[adminRowTypes.ordinal()];
        if (i == 4) {
            defaultRowHolder2.textView.setText(R.string.idle_pause_title);
            defaultRowHolder2.detailTextView.setVisibility(8);
            defaultRowHolder2.detailArrow.setVisibility(0);
        } else if (i != 10) {
            switch (i) {
                case 7:
                    defaultRowHolder2.textView.setText(this.context.getString(R.string.authentication_state_title_row));
                    defaultRowHolder2.detailTextView.setText(getAuthenticationStateString(this.parent.currentAuthenticationState));
                    defaultRowHolder2.detailArrow.setVisibility(8);
                    break;
                case 8:
                    defaultRowHolder2.textView.setText(R.string.auto_run_mode_title);
                    defaultRowHolder2.detailTextView.setText(TreadmillAutoRunManager.shared.isRunning() ? "Running" : "");
                    defaultRowHolder2.detailArrow.setVisibility(8);
                    break;
            }
        } else {
            defaultRowHolder2.textView.setText(R.string.set_total_steps_distance_title);
            defaultRowHolder2.detailTextView.setText("");
            defaultRowHolder2.detailArrow.setVisibility(8);
        }
        return view;
    }

    private View getAdminSwitchRow(View view, ViewGroup viewGroup, TreadlyProfileSettingsAdminFragment.AdminRowTypes adminRowTypes) {
        if (view == null || view.getTag() == null || !(view.getTag() instanceof DefaultSwitchHolder)) {
            LayoutInflater layoutInflater = (LayoutInflater) this.context.getSystemService("layout_inflater");
            if (layoutInflater == null) {
                return null;
            }
            view = layoutInflater.inflate(R.layout.list_view_treadly_profile_settings_default_switch_row, viewGroup, false);
            DefaultSwitchHolder defaultSwitchHolder = new DefaultSwitchHolder();
            defaultSwitchHolder.textView = (TextView) view.findViewById(R.id.item_text_view);
            defaultSwitchHolder.switchCompat = (SwitchCompat) view.findViewById(R.id.item_switch);
            view.setTag(defaultSwitchHolder);
        }
        DefaultSwitchHolder defaultSwitchHolder2 = (DefaultSwitchHolder) view.getTag();
        MainActivity mainActivity = (MainActivity) this.activity;
        int i = AnonymousClass6.$SwitchMap$com$treadly$Treadly$UI$TreadlyProfile$Settings$Admin$TreadlyProfileSettingsAdminFragment$AdminRowTypes[adminRowTypes.ordinal()];
        if (i == 6) {
            defaultSwitchHolder2.textView.setText(R.string.average_stride_mode_title);
            defaultSwitchHolder2.switchCompat.setOnCheckedChangeListener(null);
            defaultSwitchHolder2.switchCompat.setChecked(this.parent.irMode == IrMode.GEN_2);
            defaultSwitchHolder2.switchCompat.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() { // from class: com.treadly.Treadly.UI.TreadlyProfile.Settings.Admin.TreadlyProfileSettingsAdminListAdapter.3
                @Override // android.widget.CompoundButton.OnCheckedChangeListener
                public void onCheckedChanged(CompoundButton compoundButton, boolean z) {
                    TreadlyProfileSettingsAdminListAdapter.this.parent.setIrModePressed(z);
                }
            });
        } else if (i != 9) {
            switch (i) {
                case 1:
                    boolean z = mainActivity != null ? mainActivity.adminMode : false;
                    defaultSwitchHolder2.textView.setText(this.context.getString(R.string.admin_mode_title));
                    defaultSwitchHolder2.switchCompat.setOnCheckedChangeListener(null);
                    SwitchCompat switchCompat = defaultSwitchHolder2.switchCompat;
                    if (z || this.parent.pendingAdminPassword) {
                        r0 = true;
                    }
                    switchCompat.setChecked(r0);
                    defaultSwitchHolder2.switchCompat.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() { // from class: com.treadly.Treadly.UI.TreadlyProfile.Settings.Admin.TreadlyProfileSettingsAdminListAdapter.1
                        @Override // android.widget.CompoundButton.OnCheckedChangeListener
                        public void onCheckedChanged(CompoundButton compoundButton, boolean z2) {
                            TreadlyProfileSettingsAdminListAdapter.this.parent.adminSwitchPressed(z2);
                        }
                    });
                    break;
                case 2:
                    defaultSwitchHolder2.textView.setText(this.context.getString(R.string.ehs_enabled_title));
                    defaultSwitchHolder2.switchCompat.setOnCheckedChangeListener(null);
                    defaultSwitchHolder2.switchCompat.setChecked(this.parent.emergencyHandrailStopEnabled);
                    defaultSwitchHolder2.switchCompat.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() { // from class: com.treadly.Treadly.UI.TreadlyProfile.Settings.Admin.TreadlyProfileSettingsAdminListAdapter.2
                        @Override // android.widget.CompoundButton.OnCheckedChangeListener
                        public void onCheckedChanged(CompoundButton compoundButton, boolean z2) {
                            TreadlyProfileSettingsAdminListAdapter.this.parent.ehsSwitchedPressed(z2);
                        }
                    });
                    break;
                case 3:
                    defaultSwitchHolder2.textView.setText(R.string.bluetooth_enabled_title);
                    defaultSwitchHolder2.switchCompat.setOnCheckedChangeListener(null);
                    defaultSwitchHolder2.switchCompat.setChecked(this.parent.bleEnabled);
                    defaultSwitchHolder2.switchCompat.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() { // from class: com.treadly.Treadly.UI.TreadlyProfile.Settings.Admin.TreadlyProfileSettingsAdminListAdapter.4
                        @Override // android.widget.CompoundButton.OnCheckedChangeListener
                        public void onCheckedChanged(CompoundButton compoundButton, boolean z2) {
                            TreadlyProfileSettingsAdminListAdapter.this.parent.enableBlePressed(z2);
                        }
                    });
                    break;
            }
        } else {
            defaultSwitchHolder2.textView.setText(R.string.device_ir_debug_mode_title);
            defaultSwitchHolder2.switchCompat.setOnCheckedChangeListener(null);
            defaultSwitchHolder2.switchCompat.setChecked(this.parent.deviceIrDebugEnabled);
            defaultSwitchHolder2.switchCompat.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() { // from class: com.treadly.Treadly.UI.TreadlyProfile.Settings.Admin.TreadlyProfileSettingsAdminListAdapter.5
                @Override // android.widget.CompoundButton.OnCheckedChangeListener
                public void onCheckedChanged(CompoundButton compoundButton, boolean z2) {
                    TreadlyProfileSettingsAdminListAdapter.this.parent.setDeviceIrDebugLogModePressed(z2);
                }
            });
        }
        return view;
    }

    /* loaded from: classes2.dex */
    private class DefaultHeaderHolder {
        public TextView subTextView;
        public TextView textView;

        private DefaultHeaderHolder() {
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes2.dex */
    public class DefaultRowHolder {
        public ImageView detailArrow;
        public TextView detailTextView;
        public TextView textView;

        private DefaultRowHolder() {
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes2.dex */
    public class DefaultSwitchHolder {
        public SwitchCompat switchCompat;
        public TextView textView;

        private DefaultSwitchHolder() {
        }
    }

    private String getAuthenticationStateString(AuthenticationState authenticationState) {
        switch (authenticationState) {
            case unknown:
                return "";
            case active:
                return this.context.getString(R.string.auth_state_active_capitalized);
            case disabled:
                return this.context.getString(R.string.auth_state_disabled_capitalized);
            case inactive:
                return this.context.getString(R.string.auth_state_inactive_capitalized);
            default:
                return "";
        }
    }
}
