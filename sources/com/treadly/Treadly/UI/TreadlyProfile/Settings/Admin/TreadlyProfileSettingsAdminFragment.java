package com.treadly.Treadly.UI.TreadlyProfile.Settings.Admin;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.fragment.app.FragmentActivity;
import com.github.mikephil.charting.utils.Utils;
import com.treadly.Treadly.Data.Utility.NotificationCenter.NotificationCenter;
import com.treadly.Treadly.Data.Utility.NotificationCenter.NotificationType;
import com.treadly.Treadly.MainActivity;
import com.treadly.Treadly.R;
import com.treadly.Treadly.UI.TreadlyProfile.Settings.AutoRun.TreadmillAutoRunManager;
import com.treadly.Treadly.UI.TreadlyProfile.Settings.IdleConfig.TreadlyProfileSettingsIdleConfigFragment;
import com.treadly.Treadly.UI.Util.ActivityUtil;
import com.treadly.Treadly.UI.Util.BaseClasses.BaseFragment;
import com.treadly.client.lib.sdk.Listeners.DeviceConnectionEventAdapter;
import com.treadly.client.lib.sdk.Listeners.DeviceConnectionEventListener;
import com.treadly.client.lib.sdk.Listeners.RequestEventAdapter;
import com.treadly.client.lib.sdk.Listeners.RequestEventListener;
import com.treadly.client.lib.sdk.Managers.AuthenticateHelper;
import com.treadly.client.lib.sdk.Model.AuthenticationState;
import com.treadly.client.lib.sdk.Model.ComponentInfo;
import com.treadly.client.lib.sdk.Model.ComponentType;
import com.treadly.client.lib.sdk.Model.DeviceConnectionEvent;
import com.treadly.client.lib.sdk.Model.DeviceStatus;
import com.treadly.client.lib.sdk.Model.DistanceUnits;
import com.treadly.client.lib.sdk.Model.IrMode;
import com.treadly.client.lib.sdk.Model.TotalStatus;
import com.treadly.client.lib.sdk.Model.VersionInfo;
import com.treadly.client.lib.sdk.TreadlyClientLib;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;

/* loaded from: classes2.dex */
public class TreadlyProfileSettingsAdminFragment extends BaseFragment {
    private static final int[] ADMIN_PASSWORD = {36, 57};
    public static final String TAG = "TreadlyProfileSettingsAdminFragment";
    private TreadlyProfileSettingsAdminListAdapter adapter;
    private ExpandableListView adminListView;
    public ComponentInfo bleComponent;
    private DeviceStatus deviceStatus;
    public TreadlyProfileSettingsAdminFragmentListener fragmentListener;
    public TotalStatus totalStatus;
    public DistanceUnits units;
    public boolean emergencyHandrailStopEnabled = false;
    private int emergencyHandrailStopEnabledCount = 0;
    public IrMode irMode = IrMode.UNKNOWN;
    private int irModeCount = 0;
    public boolean bleEnabled = false;
    private int bleEnabledCount = 0;
    public boolean deviceIrDebugEnabled = false;
    private int deviceIrDebugEnabledCount = 0;
    private int totalStatusCount = 0;
    public int unitStatusCount = 0;
    protected AuthenticationState currentAuthenticationState = AuthenticationState.unknown;
    protected AuthenticationState previousAuthenticationState = AuthenticationState.unknown;
    protected boolean pendingAdminPassword = false;
    private Boolean idlePauseEnabled = null;
    private Byte idlePauseTimeout = null;
    private int idlePauseStatusCount = 0;
    private RequestEventListener requestEventListener = new RequestEventAdapter() { // from class: com.treadly.Treadly.UI.TreadlyProfile.Settings.Admin.TreadlyProfileSettingsAdminFragment.13
        @Override // com.treadly.client.lib.sdk.Listeners.RequestEventAdapter, com.treadly.client.lib.sdk.Listeners.RequestEventListener
        public void onRequestStatusResponse(final boolean z, final DeviceStatus deviceStatus) {
            ActivityUtil.runOnUiThread(TreadlyProfileSettingsAdminFragment.this.getActivity(), new Runnable() { // from class: com.treadly.Treadly.UI.TreadlyProfile.Settings.Admin.TreadlyProfileSettingsAdminFragment.13.1
                @Override // java.lang.Runnable
                public void run() {
                    if (z && deviceStatus != null) {
                        TreadlyProfileSettingsAdminFragment.this.deviceStatus = deviceStatus;
                        if (TreadlyProfileSettingsAdminFragment.this.units != deviceStatus.getDistanceUnits()) {
                            if (TreadlyProfileSettingsAdminFragment.this.unitStatusCount <= 0) {
                                TreadlyProfileSettingsAdminFragment.this.unitStatusCount = 0;
                            } else {
                                TreadlyProfileSettingsAdminFragment treadlyProfileSettingsAdminFragment = TreadlyProfileSettingsAdminFragment.this;
                                treadlyProfileSettingsAdminFragment.unitStatusCount--;
                            }
                        } else {
                            TreadlyProfileSettingsAdminFragment.this.unitStatusCount = 0;
                        }
                        if (TreadlyProfileSettingsAdminFragment.this.deviceIrDebugEnabledCount <= 0) {
                            TreadlyProfileSettingsAdminFragment.this.deviceIrDebugEnabledCount = 0;
                        } else {
                            TreadlyProfileSettingsAdminFragment.access$810(TreadlyProfileSettingsAdminFragment.this);
                        }
                        if (TreadlyProfileSettingsAdminFragment.this.emergencyHandrailStopEnabled != deviceStatus.isEmergencyHandrailEnabled()) {
                            if (TreadlyProfileSettingsAdminFragment.this.emergencyHandrailStopEnabledCount <= 0) {
                                TreadlyProfileSettingsAdminFragment.this.emergencyHandrailStopEnabledCount = 0;
                            } else {
                                TreadlyProfileSettingsAdminFragment.access$910(TreadlyProfileSettingsAdminFragment.this);
                            }
                        } else {
                            TreadlyProfileSettingsAdminFragment.this.emergencyHandrailStopEnabledCount = 0;
                        }
                        if (TreadlyProfileSettingsAdminFragment.this.irMode != deviceStatus.getIrMode()) {
                            if (TreadlyProfileSettingsAdminFragment.this.irModeCount <= 0) {
                                TreadlyProfileSettingsAdminFragment.this.irModeCount = 0;
                            } else {
                                TreadlyProfileSettingsAdminFragment.access$1010(TreadlyProfileSettingsAdminFragment.this);
                            }
                        } else {
                            TreadlyProfileSettingsAdminFragment.this.irModeCount = 0;
                        }
                        if (TreadlyProfileSettingsAdminFragment.this.bleEnabled != deviceStatus.isBleEnabled()) {
                            if (TreadlyProfileSettingsAdminFragment.this.bleEnabledCount <= 0) {
                                TreadlyProfileSettingsAdminFragment.this.bleEnabledCount = 0;
                            } else {
                                TreadlyProfileSettingsAdminFragment.access$1110(TreadlyProfileSettingsAdminFragment.this);
                            }
                        } else {
                            TreadlyProfileSettingsAdminFragment.this.bleEnabledCount = 0;
                        }
                        if (TreadlyProfileSettingsAdminFragment.this.totalStatus == null || (TreadlyProfileSettingsAdminFragment.this.totalStatus.totalSteps == deviceStatus.getTotalSteps() && TreadlyProfileSettingsAdminFragment.this.totalStatus.totalDistance == deviceStatus.getTotalDistance() && TreadlyProfileSettingsAdminFragment.this.totalStatus.distanceUnits == deviceStatus.getDistanceUnits())) {
                            TreadlyProfileSettingsAdminFragment.this.totalStatusCount = 0;
                        } else if (TreadlyProfileSettingsAdminFragment.this.totalStatusCount <= 0) {
                            TreadlyProfileSettingsAdminFragment.this.totalStatusCount = 0;
                        } else {
                            TreadlyProfileSettingsAdminFragment.access$1210(TreadlyProfileSettingsAdminFragment.this);
                        }
                        if (TreadlyProfileSettingsAdminFragment.this.unitStatusCount == 0) {
                            TreadlyProfileSettingsAdminFragment.this.units = deviceStatus.getDistanceUnits();
                        }
                        if (TreadlyProfileSettingsAdminFragment.this.emergencyHandrailStopEnabledCount == 0) {
                            TreadlyProfileSettingsAdminFragment.this.emergencyHandrailStopEnabled = deviceStatus.isEmergencyHandrailEnabled();
                        }
                        if (TreadlyProfileSettingsAdminFragment.this.bleEnabledCount == 0) {
                            TreadlyProfileSettingsAdminFragment.this.bleEnabled = deviceStatus.isBleEnabled();
                        }
                        if (TreadlyProfileSettingsAdminFragment.this.irModeCount == 0) {
                            TreadlyProfileSettingsAdminFragment.this.irMode = deviceStatus.getIrMode();
                        }
                        if (TreadlyProfileSettingsAdminFragment.this.totalStatusCount == 0) {
                            TreadlyProfileSettingsAdminFragment.this.totalStatus = new TotalStatus();
                            TreadlyProfileSettingsAdminFragment.this.totalStatus.totalDistance = deviceStatus.getTotalDistance();
                            TreadlyProfileSettingsAdminFragment.this.totalStatus.totalSteps = deviceStatus.getTotalSteps();
                            TreadlyProfileSettingsAdminFragment.this.totalStatus.distanceUnits = deviceStatus.getDistanceUnits();
                        }
                        if (TreadlyProfileSettingsAdminFragment.this.deviceIrDebugEnabledCount == 0) {
                            TreadlyProfileSettingsAdminFragment.this.deviceIrDebugEnabled = deviceStatus.isDeviceIrDebugLogModeEnabled();
                        }
                        if (TreadlyProfileSettingsAdminFragment.this.adapter != null) {
                            TreadlyProfileSettingsAdminFragment.this.adapter.notifyDataSetChanged();
                        }
                        if (TreadlyProfileSettingsAdminFragment.this.idlePauseStatusCount == 0) {
                            TreadlyProfileSettingsAdminFragment.this.idlePauseTimeout = Byte.valueOf(deviceStatus.getPauseAfterIdleTimeout());
                            TreadlyProfileSettingsAdminFragment.this.idlePauseEnabled = Boolean.valueOf(deviceStatus.isPauseAfterIdleEnabled());
                        }
                    }
                }
            });
        }

        @Override // com.treadly.client.lib.sdk.Listeners.RequestEventAdapter, com.treadly.client.lib.sdk.Listeners.RequestEventListener
        public void onRequestEmergencyHandrailStopEnabledResponse(final boolean z) {
            ActivityUtil.runOnUiThread(TreadlyProfileSettingsAdminFragment.this.getActivity(), new Runnable() { // from class: com.treadly.Treadly.UI.TreadlyProfile.Settings.Admin.TreadlyProfileSettingsAdminFragment.13.2
                @Override // java.lang.Runnable
                public void run() {
                    if (z) {
                        return;
                    }
                    TreadlyProfileSettingsAdminFragment.this.emergencyHandrailStopEnabled = !TreadlyProfileSettingsAdminFragment.this.emergencyHandrailStopEnabled;
                    TreadlyProfileSettingsAdminFragment.this.adapter.notifyDataSetChanged();
                }
            });
        }

        @Override // com.treadly.client.lib.sdk.Listeners.RequestEventAdapter, com.treadly.client.lib.sdk.Listeners.RequestEventListener
        public void onRequestGetDeviceComponentsListResponse(boolean z, ComponentInfo[] componentInfoArr) {
            if (z && componentInfoArr != null) {
                for (ComponentInfo componentInfo : componentInfoArr) {
                    if (componentInfo != null && componentInfo.getType() == ComponentType.bleBoard) {
                        TreadlyProfileSettingsAdminFragment.this.bleComponent = componentInfo;
                        return;
                    }
                }
            }
        }

        @Override // com.treadly.client.lib.sdk.Listeners.RequestEventAdapter, com.treadly.client.lib.sdk.Listeners.RequestEventListener
        public void onRequestBleEnableResponse(final boolean z) {
            ActivityUtil.runOnUiThread(TreadlyProfileSettingsAdminFragment.this.getActivity(), new Runnable() { // from class: com.treadly.Treadly.UI.TreadlyProfile.Settings.Admin.TreadlyProfileSettingsAdminFragment.13.3
                @Override // java.lang.Runnable
                public void run() {
                    if (z) {
                        return;
                    }
                    TreadlyProfileSettingsAdminFragment.this.bleEnabled = !TreadlyProfileSettingsAdminFragment.this.bleEnabled;
                    TreadlyProfileSettingsAdminFragment.this.adapter.notifyDataSetChanged();
                }
            });
        }

        @Override // com.treadly.client.lib.sdk.Listeners.RequestEventAdapter, com.treadly.client.lib.sdk.Listeners.RequestEventListener
        public void onRequestSetTotalStatusResponse(final boolean z) {
            ActivityUtil.runOnUiThread(TreadlyProfileSettingsAdminFragment.this.getActivity(), new Runnable() { // from class: com.treadly.Treadly.UI.TreadlyProfile.Settings.Admin.TreadlyProfileSettingsAdminFragment.13.4
                @Override // java.lang.Runnable
                public void run() {
                    TreadlyProfileSettingsAdminFragment.this.showAlert("Set Total Status", z ? "Successfully updated the total status" : "Error setting total status");
                }
            });
        }

        @Override // com.treadly.client.lib.sdk.Listeners.RequestEventAdapter, com.treadly.client.lib.sdk.Listeners.RequestEventListener
        public void onRequestSetAuthenticationState(final boolean z, final AuthenticationState authenticationState) {
            ActivityUtil.runOnUiThread(TreadlyProfileSettingsAdminFragment.this.getActivity(), new Runnable() { // from class: com.treadly.Treadly.UI.TreadlyProfile.Settings.Admin.TreadlyProfileSettingsAdminFragment.13.5
                @Override // java.lang.Runnable
                public void run() {
                    TreadlyProfileSettingsAdminFragment.this.currentAuthenticationState = z ? authenticationState : TreadlyProfileSettingsAdminFragment.this.previousAuthenticationState;
                    TreadlyProfileSettingsAdminFragment.this.adapter.notifyDataSetInvalidated();
                }
            });
        }

        @Override // com.treadly.client.lib.sdk.Listeners.RequestEventAdapter, com.treadly.client.lib.sdk.Listeners.RequestEventListener
        public void onRequestGetAuthenticationState(final boolean z, final AuthenticationState authenticationState) {
            ActivityUtil.runOnUiThread(TreadlyProfileSettingsAdminFragment.this.getActivity(), new Runnable() { // from class: com.treadly.Treadly.UI.TreadlyProfile.Settings.Admin.TreadlyProfileSettingsAdminFragment.13.6
                @Override // java.lang.Runnable
                public void run() {
                    TreadlyProfileSettingsAdminFragment.this.currentAuthenticationState = z ? authenticationState : TreadlyProfileSettingsAdminFragment.this.previousAuthenticationState;
                    TreadlyProfileSettingsAdminFragment.this.adapter.notifyDataSetChanged();
                }
            });
        }
    };
    private DeviceConnectionEventListener deviceConnectionEventListener = new DeviceConnectionEventAdapter() { // from class: com.treadly.Treadly.UI.TreadlyProfile.Settings.Admin.TreadlyProfileSettingsAdminFragment.14
        @Override // com.treadly.client.lib.sdk.Listeners.DeviceConnectionEventAdapter, com.treadly.client.lib.sdk.Listeners.DeviceConnectionEventListener
        public void onDeviceConnectionChanged(DeviceConnectionEvent deviceConnectionEvent) {
            ActivityUtil.runOnUiThread(TreadlyProfileSettingsAdminFragment.this.getActivity(), new Runnable() { // from class: com.treadly.Treadly.UI.TreadlyProfile.Settings.Admin.TreadlyProfileSettingsAdminFragment.14.1
                @Override // java.lang.Runnable
                public void run() {
                    TreadlyProfileSettingsAdminFragment.this.adapter.notifyDataSetChanged();
                }
            });
        }
    };

    /* JADX INFO: Access modifiers changed from: protected */
    /* loaded from: classes2.dex */
    public enum AdminRowTypes {
        admin,
        adminEhs,
        adminEnableBle,
        adminIdleConfig,
        adminSetAuthenticationState,
        adminSetAutoRun,
        adminMaintenance,
        adminSetTotalStatus,
        adminSetIrMode,
        adminSetDeviceIrDebugMode
    }

    /* JADX INFO: Access modifiers changed from: protected */
    /* loaded from: classes2.dex */
    public enum SectionTypes {
        settings,
        admin
    }

    /* loaded from: classes2.dex */
    public interface TreadlyProfileSettingsAdminFragmentListener {
        void onStop();
    }

    private void handleSettingPressed(int i) {
    }

    static /* synthetic */ int access$1010(TreadlyProfileSettingsAdminFragment treadlyProfileSettingsAdminFragment) {
        int i = treadlyProfileSettingsAdminFragment.irModeCount;
        treadlyProfileSettingsAdminFragment.irModeCount = i - 1;
        return i;
    }

    static /* synthetic */ int access$1110(TreadlyProfileSettingsAdminFragment treadlyProfileSettingsAdminFragment) {
        int i = treadlyProfileSettingsAdminFragment.bleEnabledCount;
        treadlyProfileSettingsAdminFragment.bleEnabledCount = i - 1;
        return i;
    }

    static /* synthetic */ int access$1210(TreadlyProfileSettingsAdminFragment treadlyProfileSettingsAdminFragment) {
        int i = treadlyProfileSettingsAdminFragment.totalStatusCount;
        treadlyProfileSettingsAdminFragment.totalStatusCount = i - 1;
        return i;
    }

    static /* synthetic */ int access$810(TreadlyProfileSettingsAdminFragment treadlyProfileSettingsAdminFragment) {
        int i = treadlyProfileSettingsAdminFragment.deviceIrDebugEnabledCount;
        treadlyProfileSettingsAdminFragment.deviceIrDebugEnabledCount = i - 1;
        return i;
    }

    static /* synthetic */ int access$910(TreadlyProfileSettingsAdminFragment treadlyProfileSettingsAdminFragment) {
        int i = treadlyProfileSettingsAdminFragment.emergencyHandrailStopEnabledCount;
        treadlyProfileSettingsAdminFragment.emergencyHandrailStopEnabledCount = i - 1;
        return i;
    }

    @Override // androidx.fragment.app.Fragment
    @Nullable
    public View onCreateView(@NonNull LayoutInflater layoutInflater, @Nullable ViewGroup viewGroup, @Nullable Bundle bundle) {
        return layoutInflater.inflate(R.layout.fragment_treadly_profile_settings_admin, viewGroup, false);
    }

    @Override // com.treadly.Treadly.UI.Util.BaseClasses.BaseFragment, androidx.fragment.app.Fragment
    public void onViewCreated(@NonNull View view, @Nullable Bundle bundle) {
        ((TextView) view.findViewById(R.id.header_title)).setText("Admin Settings");
        ((ImageView) view.findViewById(R.id.back_arrow)).setOnClickListener(new View.OnClickListener() { // from class: com.treadly.Treadly.UI.TreadlyProfile.Settings.Admin.TreadlyProfileSettingsAdminFragment.1
            @Override // android.view.View.OnClickListener
            public void onClick(View view2) {
                FragmentActivity activity = TreadlyProfileSettingsAdminFragment.this.getActivity();
                if (activity != null) {
                    activity.getSupportFragmentManager().popBackStack();
                }
            }
        });
        this.adapter = new TreadlyProfileSettingsAdminListAdapter(getContext(), this, getActivity());
        this.adminListView = (ExpandableListView) view.findViewById(R.id.admin_list_view);
        this.adminListView.setAdapter(this.adapter);
        this.adminListView.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() { // from class: com.treadly.Treadly.UI.TreadlyProfile.Settings.Admin.TreadlyProfileSettingsAdminFragment.2
            @Override // android.widget.ExpandableListView.OnGroupClickListener
            public boolean onGroupClick(ExpandableListView expandableListView, View view2, int i, long j) {
                return true;
            }
        });
        this.adminListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() { // from class: com.treadly.Treadly.UI.TreadlyProfile.Settings.Admin.TreadlyProfileSettingsAdminFragment.3
            @Override // android.widget.ExpandableListView.OnChildClickListener
            public boolean onChildClick(ExpandableListView expandableListView, View view2, int i, int i2, long j) {
                TreadlyProfileSettingsAdminFragment.this.handleItemPressed(i, i2);
                return true;
            }
        });
        this.adminListView.setChildDivider(null);
        this.adminListView.setChildIndicator(null);
        this.adminListView.setDivider(null);
    }

    @Override // androidx.fragment.app.Fragment
    public void onStart() {
        super.onStart();
        TreadlyClientLib.shared.addDeviceConnectionEventListener(this.deviceConnectionEventListener);
        TreadlyClientLib.shared.addRequestEventListener(this.requestEventListener);
    }

    @Override // androidx.fragment.app.Fragment
    public void onResume() {
        super.onResume();
        TreadlyClientLib.shared.getDeviceComponentList();
        TreadlyClientLib.shared.getAuthenticationState();
        this.adapter.notifyDataSetChanged();
    }

    @Override // androidx.fragment.app.Fragment
    public void onStop() {
        super.onStop();
        TreadlyClientLib.shared.removeRequestEventListener(this.requestEventListener);
        TreadlyClientLib.shared.removeDeviceConnectionEventListener(this.deviceConnectionEventListener);
        if (this.fragmentListener != null) {
            this.fragmentListener.onStop();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void handleItemPressed(int i, int i2) {
        if (this.adapter == null) {
            return;
        }
        switch (this.adapter.sectionTypes.get(i)) {
            case settings:
                handleSettingPressed(i2);
                return;
            case admin:
                handleAdminPressed(i2);
                return;
            default:
                return;
        }
    }

    private void handleAdminPressed(int i) {
        switch (this.adapter.adminRowTypes.get(i)) {
            case adminSetTotalStatus:
                showSetTotalStatusDialog();
                return;
            case adminSetAuthenticationState:
                showAuthenticationAlert();
                return;
            case adminIdleConfig:
                toIdleConfigFragment();
                return;
            case adminSetAutoRun:
                if (!TreadmillAutoRunManager.shared.isRunning()) {
                    showAutoRunSetupDialog();
                    return;
                }
                TreadmillAutoRunManager.shared.stop();
                this.adapter.notifyDataSetChanged();
                return;
            default:
                return;
        }
    }

    public void updateSections() {
        this.adapter.sectionTypes.clear();
        if (TreadlyClientLib.shared.isAdminEnabled()) {
            this.adapter.sectionTypes.add(SectionTypes.admin);
        }
    }

    public void updateRows() {
        this.adapter.adminRowTypes.clear();
        this.adapter.adminRowTypes.add(AdminRowTypes.admin);
        if (isAdminMode() && TreadlyClientLib.shared.isDeviceConnected()) {
            this.adapter.adminRowTypes.add(AdminRowTypes.adminEhs);
            this.adapter.adminRowTypes.add(isGen2() ? AdminRowTypes.adminSetAuthenticationState : AdminRowTypes.adminEnableBle);
            this.adapter.adminRowTypes.add(AdminRowTypes.adminSetAutoRun);
            this.adapter.adminRowTypes.add(AdminRowTypes.adminSetIrMode);
            if (isGen2()) {
                this.adapter.adminRowTypes.add(AdminRowTypes.adminSetTotalStatus);
                if (hasDeviceIrDebugMode()) {
                    this.adapter.adminRowTypes.add(AdminRowTypes.adminSetDeviceIrDebugMode);
                }
                if (hasIdlePauseFeature()) {
                    this.adapter.adminRowTypes.add(AdminRowTypes.adminIdleConfig);
                }
            }
        }
    }

    private boolean isAdminMode() {
        MainActivity mainActivity = (MainActivity) getActivity();
        if (mainActivity != null) {
            return mainActivity.adminMode;
        }
        return false;
    }

    private boolean isGen2() {
        if (this.bleComponent == null || this.bleComponent.getVersionInfo() == null) {
            return false;
        }
        VersionInfo versionInfo = new VersionInfo(2, 0, 0);
        return this.bleComponent.getVersionInfo().isGreaterThan(versionInfo) || this.bleComponent.getVersionInfo().isEqual(versionInfo);
    }

    protected boolean hasDeviceIrDebugMode() {
        if (this.bleComponent == null || this.bleComponent.getVersionInfo() == null) {
            return false;
        }
        VersionInfo versionInfo = new VersionInfo(3, 0, 0);
        VersionInfo versionInfo2 = new VersionInfo(2, 0, 0);
        if (this.bleComponent.getVersionInfo().isGreaterThan(versionInfo)) {
            return isGreaterThanOrEqual(this.bleComponent, new VersionInfo(3, 3, 0));
        }
        if (this.bleComponent.getVersionInfo().isGreaterThan(versionInfo2)) {
            return isGreaterThanOrEqual(this.bleComponent, new VersionInfo(2, 17, 0));
        }
        return false;
    }

    private boolean hasIdlePauseFeature() {
        if (this.bleComponent == null || this.bleComponent.getVersionInfo() == null) {
            return false;
        }
        return isGreaterThanOrEqual(this.bleComponent, new VersionInfo(3, 20, 0));
    }

    private boolean isGreaterThanOrEqual(ComponentInfo componentInfo, VersionInfo versionInfo) {
        if (componentInfo == null || componentInfo.getVersionInfo() == null || versionInfo == null) {
            return false;
        }
        return componentInfo.getVersionInfo().isGreaterThan(versionInfo) || componentInfo.getVersionInfo().isEqual(versionInfo);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void adminSwitchPressed(boolean z) {
        if (z) {
            if (TreadlyClientLib.shared.isAdminEnabled()) {
                showPasswordAlert();
                return;
            } else {
                this.adapter.notifyDataSetChanged();
                return;
            }
        }
        MainActivity mainActivity = (MainActivity) getActivity();
        if (mainActivity != null) {
            mainActivity.adminMode = false;
        }
        NotificationCenter.postNotification(getContext(), NotificationType.onAdminMode, new HashMap());
    }

    private void showPasswordAlert() {
        if (getContext() == null) {
            return;
        }
        this.pendingAdminPassword = true;
        final AppCompatEditText appCompatEditText = new AppCompatEditText(getContext());
        appCompatEditText.setInputType(18);
        AlertDialog.Builder negativeButton = new AlertDialog.Builder(getContext()).setTitle("Enter Password").setMessage("Enter the password to enable admin mode").setPositiveButton("Submit", new DialogInterface.OnClickListener() { // from class: com.treadly.Treadly.UI.TreadlyProfile.Settings.Admin.TreadlyProfileSettingsAdminFragment.5
            @Override // android.content.DialogInterface.OnClickListener
            public void onClick(DialogInterface dialogInterface, int i) {
                TreadlyProfileSettingsAdminFragment.this.pendingAdminPassword = false;
                boolean equals = appCompatEditText.getText() != null ? appCompatEditText.getText().toString().equals(TreadlyProfileSettingsAdminFragment.this.generateAdminPassword()) : false;
                MainActivity mainActivity = (MainActivity) TreadlyProfileSettingsAdminFragment.this.getActivity();
                if (mainActivity != null) {
                    mainActivity.adminMode = equals;
                    if (equals) {
                        NotificationCenter.postNotification(TreadlyProfileSettingsAdminFragment.this.getContext(), NotificationType.onAdminMode, new HashMap());
                    }
                }
                if (TreadlyProfileSettingsAdminFragment.this.adapter != null) {
                    TreadlyProfileSettingsAdminFragment.this.adapter.notifyDataSetChanged();
                }
            }
        }).setNegativeButton("Dismiss", new DialogInterface.OnClickListener() { // from class: com.treadly.Treadly.UI.TreadlyProfile.Settings.Admin.TreadlyProfileSettingsAdminFragment.4
            @Override // android.content.DialogInterface.OnClickListener
            public void onClick(DialogInterface dialogInterface, int i) {
                TreadlyProfileSettingsAdminFragment.this.pendingAdminPassword = false;
                if (TreadlyProfileSettingsAdminFragment.this.adapter != null) {
                    TreadlyProfileSettingsAdminFragment.this.adapter.notifyDataSetChanged();
                }
            }
        });
        negativeButton.setCancelable(false);
        negativeButton.setView(appCompatEditText);
        negativeButton.show();
    }

    private void toIdleConfigFragment() {
        if (this.idlePauseEnabled == null || this.idlePauseTimeout == null) {
            return;
        }
        TreadlyProfileSettingsIdleConfigFragment treadlyProfileSettingsIdleConfigFragment = new TreadlyProfileSettingsIdleConfigFragment();
        treadlyProfileSettingsIdleConfigFragment.isIdlePauseEnabled = this.idlePauseEnabled.booleanValue();
        treadlyProfileSettingsIdleConfigFragment.timeout = this.idlePauseTimeout.byteValue();
        treadlyProfileSettingsIdleConfigFragment.listener = new TreadlyProfileSettingsIdleConfigFragment.TreadlyProfileSettingsIdleConfigFragmentListener() { // from class: com.treadly.Treadly.UI.TreadlyProfile.Settings.Admin.TreadlyProfileSettingsAdminFragment.6
            @Override // com.treadly.Treadly.UI.TreadlyProfile.Settings.IdleConfig.TreadlyProfileSettingsIdleConfigFragment.TreadlyProfileSettingsIdleConfigFragmentListener
            public void didUpdateIdlePauseConfig(boolean z, byte b) {
                TreadlyProfileSettingsAdminFragment.this.idlePauseEnabled = Boolean.valueOf(z);
                TreadlyProfileSettingsAdminFragment.this.idlePauseTimeout = Byte.valueOf(b);
                TreadlyProfileSettingsAdminFragment.this.idlePauseStatusCount = 2;
            }
        };
        if (getActivity() != null) {
            getActivity().getSupportFragmentManager().beginTransaction().addToBackStack(null).add(R.id.activity_fragment_container_empty, treadlyProfileSettingsIdleConfigFragment).commit();
        }
    }

    private void showAuthenticationAlert() {
        if (getContext() == null || this.currentAuthenticationState == AuthenticationState.unknown || this.bleComponent == null) {
            return;
        }
        ArrayList arrayList = new ArrayList();
        arrayList.add(getAuthenticationStateString(AuthenticationState.active));
        arrayList.add(getAuthenticationStateString(AuthenticationState.inactive));
        if (this.bleComponent.getVersionInfo().isGreaterThan(AuthenticateHelper.PRE_AUTHENTICATION_VERSION)) {
            arrayList.add(getAuthenticationStateString(AuthenticationState.disabled));
        }
        CharSequence[] charSequenceArr = new CharSequence[arrayList.size()];
        arrayList.toArray(charSequenceArr);
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Set Authentication State\nCurrent State: " + getAuthenticationStateString(this.currentAuthenticationState)).setItems(charSequenceArr, new DialogInterface.OnClickListener() { // from class: com.treadly.Treadly.UI.TreadlyProfile.Settings.Admin.TreadlyProfileSettingsAdminFragment.8
            @Override // android.content.DialogInterface.OnClickListener
            public void onClick(DialogInterface dialogInterface, int i) {
                AuthenticationState authenticationState = AuthenticationState.unknown;
                switch (i) {
                    case 0:
                        authenticationState = AuthenticationState.active;
                        break;
                    case 1:
                        authenticationState = AuthenticationState.inactive;
                        break;
                    case 2:
                        authenticationState = AuthenticationState.disabled;
                        break;
                }
                if (authenticationState == AuthenticationState.unknown || !TreadlyClientLib.shared.setAuthenticationState(authenticationState)) {
                    return;
                }
                TreadlyProfileSettingsAdminFragment.this.previousAuthenticationState = TreadlyProfileSettingsAdminFragment.this.currentAuthenticationState;
                TreadlyProfileSettingsAdminFragment.this.currentAuthenticationState = authenticationState;
                if (TreadlyProfileSettingsAdminFragment.this.adapter != null) {
                    TreadlyProfileSettingsAdminFragment.this.adapter.notifyDataSetChanged();
                }
            }
        }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() { // from class: com.treadly.Treadly.UI.TreadlyProfile.Settings.Admin.TreadlyProfileSettingsAdminFragment.7
            @Override // android.content.DialogInterface.OnClickListener
            public void onClick(DialogInterface dialogInterface, int i) {
                if (TreadlyProfileSettingsAdminFragment.this.adapter != null) {
                    TreadlyProfileSettingsAdminFragment.this.adapter.notifyDataSetChanged();
                }
            }
        }).show();
    }

    private void showSetTotalStatusDialog() {
        if (getContext() == null) {
            return;
        }
        DecimalFormat decimalFormat = new DecimalFormat("0.0");
        final AppCompatEditText appCompatEditText = new AppCompatEditText(getContext());
        appCompatEditText.setInputType(2);
        appCompatEditText.setHint("Total steps");
        appCompatEditText.setText(String.format(Locale.getDefault(), "%d", Integer.valueOf(this.totalStatus.totalSteps)));
        final AppCompatEditText appCompatEditText2 = new AppCompatEditText(getContext());
        appCompatEditText2.setInputType(8194);
        appCompatEditText2.setHint("Total distance");
        appCompatEditText2.setText(decimalFormat.format(this.totalStatus.totalDistance));
        AlertDialog.Builder negativeButton = new AlertDialog.Builder(getContext()).setTitle("Total Steps and Distance").setMessage("Enter the total steps and distance for the treadmill").setPositiveButton("Done", new DialogInterface.OnClickListener() { // from class: com.treadly.Treadly.UI.TreadlyProfile.Settings.Admin.TreadlyProfileSettingsAdminFragment.10
            @Override // android.content.DialogInterface.OnClickListener
            public void onClick(DialogInterface dialogInterface, int i) {
                if (appCompatEditText2.getText() == null || appCompatEditText.getText() == null) {
                    return;
                }
                try {
                    double parseDouble = Double.parseDouble(appCompatEditText2.getText().toString());
                    int parseInt = Integer.parseInt(appCompatEditText.getText().toString());
                    TotalStatus totalStatus = new TotalStatus();
                    totalStatus.totalSteps = parseInt;
                    totalStatus.totalDistance = (float) parseDouble;
                    totalStatus.distanceUnits = TreadlyProfileSettingsAdminFragment.this.units;
                    TreadlyClientLib.shared.setTotalStatus(totalStatus);
                } catch (Exception e) {
                    e.printStackTrace();
                    TreadlyProfileSettingsAdminFragment.this.showAlert("Error", "Error setting total steps and distance");
                }
            }
        }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() { // from class: com.treadly.Treadly.UI.TreadlyProfile.Settings.Admin.TreadlyProfileSettingsAdminFragment.9
            @Override // android.content.DialogInterface.OnClickListener
            public void onClick(DialogInterface dialogInterface, int i) {
                if (TreadlyProfileSettingsAdminFragment.this.adapter != null) {
                    TreadlyProfileSettingsAdminFragment.this.adapter.notifyDataSetChanged();
                }
            }
        });
        LinearLayout linearLayout = new LinearLayout(getContext());
        linearLayout.setOrientation(1);
        linearLayout.addView(appCompatEditText);
        linearLayout.addView(appCompatEditText2);
        negativeButton.setView(linearLayout);
        negativeButton.show();
    }

    private void showAutoRunSetupDialog() {
        if (getContext() == null) {
            return;
        }
        getContext();
        final AppCompatEditText appCompatEditText = new AppCompatEditText(getContext());
        appCompatEditText.setInputType(8194);
        appCompatEditText.setHint("Set speed");
        AlertDialog.Builder negativeButton = new AlertDialog.Builder(getContext()).setTitle("Auto Run Mode").setMessage("Set the run speed").setPositiveButton("Start", new DialogInterface.OnClickListener() { // from class: com.treadly.Treadly.UI.TreadlyProfile.Settings.Admin.TreadlyProfileSettingsAdminFragment.12
            @Override // android.content.DialogInterface.OnClickListener
            public void onClick(DialogInterface dialogInterface, int i) {
                if (appCompatEditText.getText() == null || TreadlyProfileSettingsAdminFragment.this.bleComponent == null || TreadlyProfileSettingsAdminFragment.this.bleComponent.getVersionInfo() == null) {
                    return;
                }
                double d = Utils.DOUBLE_EPSILON;
                try {
                    d = Double.parseDouble(appCompatEditText.getText().toString());
                } catch (Exception e) {
                    e.printStackTrace();
                }
                if (TreadmillAutoRunManager.shared.start(TreadlyProfileSettingsAdminFragment.this.bleComponent.getVersionInfo(), d)) {
                    TreadmillAutoRunManager.shared.showProgress(TreadlyProfileSettingsAdminFragment.this.getActivity());
                }
                MainActivity mainActivity = (MainActivity) TreadlyProfileSettingsAdminFragment.this.getActivity();
                if (mainActivity != null) {
                    mainActivity.toConnectNavBar();
                }
            }
        }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() { // from class: com.treadly.Treadly.UI.TreadlyProfile.Settings.Admin.TreadlyProfileSettingsAdminFragment.11
            @Override // android.content.DialogInterface.OnClickListener
            public void onClick(DialogInterface dialogInterface, int i) {
                if (TreadlyProfileSettingsAdminFragment.this.adapter != null) {
                    TreadlyProfileSettingsAdminFragment.this.adapter.notifyDataSetChanged();
                }
            }
        });
        negativeButton.setView(appCompatEditText);
        negativeButton.show();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void showAlert(String str, String str2) {
        if (getContext() == null) {
            return;
        }
        new AlertDialog.Builder(getContext()).setTitle(str).setMessage(str2).setNegativeButton("Dismiss", (DialogInterface.OnClickListener) null).show();
    }

    private String getAuthenticationStateString(AuthenticationState authenticationState) {
        if (getContext() == null) {
            return "";
        }
        switch (authenticationState) {
            case unknown:
                return "";
            case active:
                return getContext().getString(R.string.auth_state_active_capitalized);
            case disabled:
                return getContext().getString(R.string.auth_state_disabled_capitalized);
            case inactive:
                return getContext().getString(R.string.auth_state_inactive_capitalized);
            default:
                return "";
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public String generateAdminPassword() {
        return String.format(Locale.getDefault(), "%d%d", Integer.valueOf(ADMIN_PASSWORD[0]), Integer.valueOf(ADMIN_PASSWORD[1]));
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void ehsSwitchedPressed(boolean z) {
        if (!TreadlyClientLib.shared.setEmergencyHandrailStopEnabled(z)) {
            this.adapter.notifyDataSetChanged();
            return;
        }
        this.emergencyHandrailStopEnabled = z;
        this.emergencyHandrailStopEnabledCount = 2;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void setIrModePressed(boolean z) {
        IrMode irMode = z ? IrMode.GEN_2 : IrMode.GEN_1;
        if (!TreadlyClientLib.shared.setIrMode(irMode)) {
            this.adapter.notifyDataSetChanged();
            return;
        }
        this.irMode = irMode;
        this.irModeCount = 2;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void enableBlePressed(boolean z) {
        if (!TreadlyClientLib.shared.setBleEnable(z)) {
            this.adapter.notifyDataSetChanged();
        } else {
            this.bleEnabled = z;
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void setDeviceIrDebugLogModePressed(boolean z) {
        TreadlyClientLib.shared.setDeviceIrDebugMode(z);
        this.deviceIrDebugEnabled = z;
        this.deviceIrDebugEnabledCount = 2;
        if (z) {
            NotificationCenter.postNotification(getContext(), NotificationType.onIrDebugOn, new HashMap());
        } else {
            NotificationCenter.postNotification(getContext(), NotificationType.onIrDebugOff, new HashMap());
        }
    }
}
