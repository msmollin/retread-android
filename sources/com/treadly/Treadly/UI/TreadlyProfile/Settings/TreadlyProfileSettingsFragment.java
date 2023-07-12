package com.treadly.Treadly.UI.TreadlyProfile.Settings;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import com.treadly.Treadly.Data.Listeners.TreadlyServiceResponseEventAdapter;
import com.treadly.Treadly.Data.Managers.AppActivityManager;
import com.treadly.Treadly.Data.Managers.DeviceUserStatsLogManager;
import com.treadly.Treadly.Data.Managers.RunningSessionManager;
import com.treadly.Treadly.Data.Managers.TreadlyServiceManager;
import com.treadly.Treadly.Data.Model.UserRunningSessionInfo;
import com.treadly.Treadly.MainActivity;
import com.treadly.Treadly.R;
import com.treadly.Treadly.UI.FirmwareUpdate.FirmwareUpdateSsidFragment;
import com.treadly.Treadly.UI.TreadlyAccount.LoginActivity;
import com.treadly.Treadly.UI.TreadlyAccount.TreadlyAccountChangeUsernameFragment;
import com.treadly.Treadly.UI.TreadlyActivity.UnclaimedActivityList.TreadlyActivityUnclaimedFragment;
import com.treadly.Treadly.UI.TreadlyProfile.Settings.About.TreadlyProfileSettingsAboutFragment;
import com.treadly.Treadly.UI.TreadlyProfile.Settings.Admin.TreadlyProfileSettingsAdminFragment;
import com.treadly.Treadly.UI.TreadlyProfile.Settings.IdleConfig.TreadlyProfileSettingsIdleConfigFragment;
import com.treadly.Treadly.UI.TreadlyProfile.Settings.Maintenance.TreadlyProfileSettingsMaintenance;
import com.treadly.Treadly.UI.TreadlyProfile.Settings.Notifications.TreadlyProfileSettingsNotificationsFragment;
import com.treadly.Treadly.UI.TreadlyProfile.Settings.PairedPhones.TreadlyProfileSettingsPairedPhonesFragment;
import com.treadly.Treadly.UI.TreadlyProfile.Settings.PairingMode.TreadlyProfileSettingsPairingModeFragment;
import com.treadly.Treadly.UI.TreadlyProfile.Settings.SingleUserMode.TreadlyProfileSettingsSingleUserModeFragment;
import com.treadly.Treadly.UI.TreadlyProfile.Settings.SpeakerPassword.TreadlyProfileSettingsSpeakerPasswordFragment;
import com.treadly.Treadly.UI.TreadlyProfile.Settings.TreadlyProfileSettingsFragment;
import com.treadly.Treadly.UI.TreadlyProfile.Settings.TreadlyProfileSettingsListController;
import com.treadly.Treadly.UI.TreadlyProfile.Settings.UpdatePassword.TreadlyProfileSettingsUpdatePasswordFragment;
import com.treadly.Treadly.UI.TreadlyProfile.Settings.WifiSetup.TreadlyProfileSettingsWifiSetupFragment;
import com.treadly.Treadly.UI.TreadlyProfile.TreadlyProfileEditFragment;
import com.treadly.Treadly.UI.Util.ActivityUtil;
import com.treadly.Treadly.UI.Util.BaseClasses.BaseFragment;
import com.treadly.Treadly.UI.Util.NormalWebViewActivity;
import com.treadly.Treadly.UI.Util.OnBackPressedListener;
import com.treadly.Treadly.UI.Util.SharedPreferences;
import com.treadly.client.lib.sdk.Listeners.DeviceConnectionEventListener;
import com.treadly.client.lib.sdk.Listeners.DeviceUserStatsLogEventAdapter;
import com.treadly.client.lib.sdk.Listeners.DeviceUserStatsLogEventListener;
import com.treadly.client.lib.sdk.Listeners.OtaUpdateRequestEventAdapter;
import com.treadly.client.lib.sdk.Listeners.OtaUpdateRequestEventListener;
import com.treadly.client.lib.sdk.Listeners.RequestEventAdapter;
import com.treadly.client.lib.sdk.Listeners.RequestEventListener;
import com.treadly.client.lib.sdk.Model.ComponentInfo;
import com.treadly.client.lib.sdk.Model.ComponentType;
import com.treadly.client.lib.sdk.Model.DeviceConnectionEvent;
import com.treadly.client.lib.sdk.Model.DeviceConnectionStatus;
import com.treadly.client.lib.sdk.Model.DeviceInfo;
import com.treadly.client.lib.sdk.Model.DeviceStatus;
import com.treadly.client.lib.sdk.Model.DeviceStatusCode;
import com.treadly.client.lib.sdk.Model.DeviceUserStatsUnclaimedLogInfo;
import com.treadly.client.lib.sdk.Model.DistanceUnits;
import com.treadly.client.lib.sdk.Model.FirmwareVersion;
import com.treadly.client.lib.sdk.Model.IrMode;
import com.treadly.client.lib.sdk.Model.PairingModeTriggerType;
import com.treadly.client.lib.sdk.Model.TotalStatus;
import com.treadly.client.lib.sdk.Model.VersionInfo;
import com.treadly.client.lib.sdk.Model.WifiApInfo;
import com.treadly.client.lib.sdk.TreadlyClientLib;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/* loaded from: classes2.dex */
public class TreadlyProfileSettingsFragment extends BaseFragment implements DeviceConnectionEventListener {
    public static final String TAG = "TREADLY_PROFILE_SETTINGS";
    private TreadlyProfileSettingsAboutFragment aboutFragment;
    private List<ComponentInfo> deviceComponents;
    private DeviceStatus deviceStatus;
    private TreadlyProfileSettingsListController settingsListController;
    private ExpandableListView settingsListView;
    private DistanceUnits unitInfo;
    private boolean aboutSelected = false;
    boolean waitingForComponents = false;
    boolean waitingForPairingMode = false;
    boolean waitingForIdlePause = false;
    private int unitStatusCount = 0;
    private int pairingModeStatusCount = 0;
    private int idlePauseStatusCount = 0;
    private PairingModeTriggerType pairingModeType = null;
    private Boolean idlePauseEnabled = null;
    private Byte idlePauseTimeout = null;
    private boolean removeDelegates = true;
    private boolean checkingOtaUpdate = false;
    private boolean isExpectingApInfo = false;
    ComponentInfo bleComponent = null;
    TreadlyProfileSettingsPairingModeFragment pairingModeFragment = null;
    OtaUpdateRequestEventListener otaUpdateRequestEventListener = new AnonymousClass2();
    RequestEventListener requestEventListener = new AnonymousClass3();
    private boolean expectingUnclaimedCheck = false;
    private final DeviceUserStatsLogEventListener deviceUserStatsLogEventListener = new AnonymousClass4();

    private void handleDeviceDisconnected() {
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void settingsSectionPressed(TreadlyProfileSettingsListController.SettingsSectionTypes settingsSectionTypes) {
    }

    @Override // com.treadly.client.lib.sdk.Listeners.DeviceConnectionEventListener
    public void onDeviceConnectionDeviceDiscovered(DeviceInfo deviceInfo) {
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void setBleComponent(ComponentInfo componentInfo) {
        this.bleComponent = componentInfo;
        if (this.settingsListController != null) {
            this.settingsListController.setBleComponent(componentInfo);
        }
    }

    @Override // androidx.fragment.app.Fragment
    @Nullable
    public View onCreateView(@NonNull LayoutInflater layoutInflater, @Nullable ViewGroup viewGroup, @Nullable Bundle bundle) {
        final MainActivity mainActivity = (MainActivity) getActivity();
        mainActivity.setOnBackPressedListener(new OnBackPressedListener() { // from class: com.treadly.Treadly.UI.TreadlyProfile.Settings.-$$Lambda$TreadlyProfileSettingsFragment$SLvG43Vn91awgaUhhgN7G_xCw7g
            @Override // com.treadly.Treadly.UI.Util.OnBackPressedListener
            public final void backAction() {
                MainActivity.this.getSupportFragmentManager().popBackStack();
            }
        });
        return layoutInflater.inflate(R.layout.fragment_treadly_profile_settings, viewGroup, false);
    }

    @Override // com.treadly.Treadly.UI.Util.BaseClasses.BaseFragment, androidx.fragment.app.Fragment
    public void onViewCreated(@NonNull View view, @Nullable Bundle bundle) {
        ((TextView) view.findViewById(R.id.header_title)).setText(R.string.settings_label);
        ((ImageView) view.findViewById(R.id.back_arrow)).setOnClickListener(new View.OnClickListener() { // from class: com.treadly.Treadly.UI.TreadlyProfile.Settings.-$$Lambda$TreadlyProfileSettingsFragment$py47BykNkh1_pCRBU53h_G7Amm8
            @Override // android.view.View.OnClickListener
            public final void onClick(View view2) {
                TreadlyProfileSettingsFragment.this.popBackStack();
            }
        });
        this.settingsListView = (ExpandableListView) view.findViewById(R.id.settings_list_view);
        initList();
        addBackStackCallback();
    }

    @Override // androidx.fragment.app.Fragment
    public void onStart() {
        super.onStart();
        TreadlyClientLib.shared.addRequestEventListener(this.requestEventListener);
        TreadlyClientLib.shared.addDeviceConnectionEventListener(this);
        TreadlyClientLib.shared.addOtaUpdateRequestEventListener(this.otaUpdateRequestEventListener);
        TreadlyClientLib.shared.addDeviceUserStatsLogEventListener(this.deviceUserStatsLogEventListener);
        TreadlyClientLib.shared.getDeviceComponentList();
        this.isExpectingApInfo = TreadlyClientLib.shared.getOtaConfigSettings();
    }

    @Override // androidx.fragment.app.Fragment
    public void onResume() {
        super.onResume();
    }

    @Override // com.treadly.Treadly.UI.Util.BaseClasses.BaseFragment, androidx.fragment.app.Fragment
    public void onDetach() {
        super.onDetach();
        if (getActivity() != null) {
            MainActivity mainActivity = (MainActivity) getActivity();
            mainActivity.setOnBackPressedListener(null);
            mainActivity.showBottomNavigationView();
        }
    }

    @Override // androidx.fragment.app.Fragment
    public void onStop() {
        super.onStop();
        if (this.removeDelegates) {
            TreadlyClientLib.shared.removeRequestEventListener(this.requestEventListener);
            TreadlyClientLib.shared.removeOtaUpdateRequestEventListener(this.otaUpdateRequestEventListener);
            TreadlyClientLib.shared.removeDeviceUserStatsLogEventListener(this.deviceUserStatsLogEventListener);
        }
        this.removeDelegates = true;
        this.expectingUnclaimedCheck = false;
        TreadlyClientLib.shared.removeDeviceConnectionEventListener(this);
    }

    @Override // com.treadly.Treadly.UI.Util.BaseClasses.BaseFragment
    public void onFragmentReturning() {
        super.onFragmentReturning();
        hideBottomNavigation();
        onStart();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void pairingModeChanged(PairingModeTriggerType pairingModeTriggerType) {
        TreadlyClientLib.shared.setPairingModeTriggerType(pairingModeTriggerType);
        this.pairingModeStatusCount = 2;
        this.waitingForPairingMode = true;
        this.pairingModeType = pairingModeTriggerType;
        this.settingsListController.pairingModeType = pairingModeTriggerType;
        this.pairingModeFragment.pairingModeType = pairingModeTriggerType;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* renamed from: com.treadly.Treadly.UI.TreadlyProfile.Settings.TreadlyProfileSettingsFragment$1  reason: invalid class name */
    /* loaded from: classes2.dex */
    public class AnonymousClass1 implements TreadlyProfileSettingsListController.TreadlyProfileSettingsEventListener {
        @Override // com.treadly.Treadly.UI.TreadlyProfile.Settings.TreadlyProfileSettingsListController.TreadlyProfileSettingsEventListener
        public void didChangeHealthApp() {
        }

        @Override // com.treadly.Treadly.UI.TreadlyProfile.Settings.TreadlyProfileSettingsListController.TreadlyProfileSettingsEventListener
        public void didPressOldSettings() {
        }

        @Override // com.treadly.Treadly.UI.TreadlyProfile.Settings.TreadlyProfileSettingsListController.TreadlyProfileSettingsEventListener
        public void didPressPrivateVideoCompositionOptions() {
        }

        @Override // com.treadly.Treadly.UI.TreadlyProfile.Settings.TreadlyProfileSettingsListController.TreadlyProfileSettingsEventListener
        public void didPressUnitSwitch(boolean z) {
        }

        AnonymousClass1() {
        }

        @Override // com.treadly.Treadly.UI.TreadlyProfile.Settings.TreadlyProfileSettingsListController.TreadlyProfileSettingsEventListener
        public void didPressSystemVersion() {
            if (TreadlyClientLib.shared.isDeviceConnected()) {
                TreadlyProfileSettingsFragment.this.aboutSelected = true;
                TreadlyProfileSettingsFragment.this.waitingForComponents = true;
                TreadlyProfileSettingsFragment.this.waitingForPairingMode = TreadlyProfileSettingsFragment.this.pairingModeType == null;
                TreadlyProfileSettingsFragment.this.waitingForIdlePause = TreadlyProfileSettingsFragment.this.idlePauseEnabled == null;
                TreadlyClientLib.shared.getDeviceComponentList();
                return;
            }
            TreadlyProfileSettingsFragment.this.toAboutPage();
        }

        @Override // com.treadly.Treadly.UI.TreadlyProfile.Settings.TreadlyProfileSettingsListController.TreadlyProfileSettingsEventListener
        public void didPressChangePassword() {
            TreadlyProfileSettingsFragment.this.addFragmentToStack((Fragment) new TreadlyProfileSettingsUpdatePasswordFragment(), true);
        }

        @Override // com.treadly.Treadly.UI.TreadlyProfile.Settings.TreadlyProfileSettingsListController.TreadlyProfileSettingsEventListener
        public void didPressChangeUsername() {
            TreadlyAccountChangeUsernameFragment treadlyAccountChangeUsernameFragment = new TreadlyAccountChangeUsernameFragment();
            treadlyAccountChangeUsernameFragment.fromSettings = true;
            treadlyAccountChangeUsernameFragment.userInfo = TreadlyServiceManager.getInstance().getUserInfo();
            TreadlyProfileSettingsFragment.this.addFragmentToStack((Fragment) treadlyAccountChangeUsernameFragment, true);
        }

        @Override // com.treadly.Treadly.UI.TreadlyProfile.Settings.TreadlyProfileSettingsListController.TreadlyProfileSettingsEventListener
        public void didPressLogout() {
            TreadlyProfileSettingsFragment.this.logout();
        }

        @Override // com.treadly.Treadly.UI.TreadlyProfile.Settings.TreadlyProfileSettingsListController.TreadlyProfileSettingsEventListener
        public void didPressSpeakerPassword() {
            if (TreadlyProfileSettingsFragment.this.getActivity() != null) {
                TreadlyProfileSettingsFragment.this.getActivity().getSupportFragmentManager().beginTransaction().addToBackStack(null).add(R.id.activity_fragment_container_empty, new TreadlyProfileSettingsSpeakerPasswordFragment()).commit();
            }
        }

        @Override // com.treadly.Treadly.UI.TreadlyProfile.Settings.TreadlyProfileSettingsListController.TreadlyProfileSettingsEventListener
        public void didPressCheckOta() {
            TreadlyProfileSettingsFragment.this.checkUpdatesPressed();
        }

        @Override // com.treadly.Treadly.UI.TreadlyProfile.Settings.TreadlyProfileSettingsListController.TreadlyProfileSettingsEventListener
        public void didPressPairingModeType(boolean z) {
            if (TreadlyProfileSettingsFragment.this.pairingModeFragment == null) {
                TreadlyProfileSettingsFragment.this.pairingModeFragment = new TreadlyProfileSettingsPairingModeFragment();
                TreadlyProfileSettingsFragment.this.pairingModeFragment.listener = new TreadlyProfileSettingsPairingModeFragment.TreadlyProfileSettingsPairingModeFragmentListener() { // from class: com.treadly.Treadly.UI.TreadlyProfile.Settings.-$$Lambda$TreadlyProfileSettingsFragment$1$CEeYxMGyuXOwKY4Z1SutmFBVsBE
                    @Override // com.treadly.Treadly.UI.TreadlyProfile.Settings.PairingMode.TreadlyProfileSettingsPairingModeFragment.TreadlyProfileSettingsPairingModeFragmentListener
                    public final void didSelectPairingModeType(PairingModeTriggerType pairingModeTriggerType) {
                        TreadlyProfileSettingsFragment.this.pairingModeChanged(pairingModeTriggerType);
                    }
                };
            }
            TreadlyProfileSettingsFragment.this.pairingModeFragment.pairingModeType = TreadlyProfileSettingsFragment.this.pairingModeType;
            TreadlyProfileSettingsFragment.this.removeDelegates = false;
            if (TreadlyProfileSettingsFragment.this.getActivity() != null) {
                TreadlyProfileSettingsFragment.this.getActivity().getSupportFragmentManager().beginTransaction().addToBackStack(null).add(R.id.activity_fragment_container_empty, TreadlyProfileSettingsFragment.this.pairingModeFragment).commit();
            }
        }

        @Override // com.treadly.Treadly.UI.TreadlyProfile.Settings.TreadlyProfileSettingsListController.TreadlyProfileSettingsEventListener
        public void didPressMaintenance() {
            if (TreadlyProfileSettingsFragment.this.getActivity() != null) {
                TreadlyProfileSettingsFragment.this.getActivity().getSupportFragmentManager().beginTransaction().addToBackStack(null).add(R.id.activity_fragment_container_empty, new TreadlyProfileSettingsMaintenance()).commit();
            }
        }

        @Override // com.treadly.Treadly.UI.TreadlyProfile.Settings.TreadlyProfileSettingsListController.TreadlyProfileSettingsEventListener
        public void didPressIdleConfig() {
            if (TreadlyProfileSettingsFragment.this.idlePauseEnabled == null || TreadlyProfileSettingsFragment.this.idlePauseTimeout == null) {
                return;
            }
            TreadlyProfileSettingsIdleConfigFragment treadlyProfileSettingsIdleConfigFragment = new TreadlyProfileSettingsIdleConfigFragment();
            treadlyProfileSettingsIdleConfigFragment.isIdlePauseEnabled = TreadlyProfileSettingsFragment.this.idlePauseEnabled.booleanValue();
            treadlyProfileSettingsIdleConfigFragment.timeout = TreadlyProfileSettingsFragment.this.idlePauseTimeout.byteValue();
            treadlyProfileSettingsIdleConfigFragment.listener = new TreadlyProfileSettingsIdleConfigFragment.TreadlyProfileSettingsIdleConfigFragmentListener() { // from class: com.treadly.Treadly.UI.TreadlyProfile.Settings.-$$Lambda$TreadlyProfileSettingsFragment$1$wn03I6h-MtHY9fTrJA4BA2zyA4c
                @Override // com.treadly.Treadly.UI.TreadlyProfile.Settings.IdleConfig.TreadlyProfileSettingsIdleConfigFragment.TreadlyProfileSettingsIdleConfigFragmentListener
                public final void didUpdateIdlePauseConfig(boolean z, byte b) {
                    TreadlyProfileSettingsFragment.AnonymousClass1.lambda$didPressIdleConfig$1(TreadlyProfileSettingsFragment.AnonymousClass1.this, z, b);
                }
            };
            if (TreadlyProfileSettingsFragment.this.getActivity() != null) {
                TreadlyProfileSettingsFragment.this.getActivity().getSupportFragmentManager().beginTransaction().addToBackStack(null).add(R.id.activity_fragment_container_empty, treadlyProfileSettingsIdleConfigFragment).commit();
            }
        }

        public static /* synthetic */ void lambda$didPressIdleConfig$1(AnonymousClass1 anonymousClass1, boolean z, byte b) {
            TreadlyProfileSettingsFragment.this.idlePauseEnabled = Boolean.valueOf(z);
            TreadlyProfileSettingsFragment.this.idlePauseTimeout = Byte.valueOf(b);
            TreadlyProfileSettingsFragment.this.idlePauseStatusCount = 2;
        }

        @Override // com.treadly.Treadly.UI.TreadlyProfile.Settings.TreadlyProfileSettingsListController.TreadlyProfileSettingsEventListener
        public void didPressPairedPhones() {
            if (TreadlyProfileSettingsFragment.this.getActivity() != null) {
                TreadlyProfileSettingsFragment.this.getActivity().getSupportFragmentManager().beginTransaction().addToBackStack(null).add(R.id.activity_fragment_container_empty, new TreadlyProfileSettingsPairedPhonesFragment()).commit();
            }
        }

        @Override // com.treadly.Treadly.UI.TreadlyProfile.Settings.TreadlyProfileSettingsListController.TreadlyProfileSettingsEventListener
        public void didPressUnclaimedList() {
            if (TreadlyProfileSettingsFragment.this.expectingUnclaimedCheck) {
                return;
            }
            if (TreadlyProfileSettingsFragment.this.bleComponent.getVersionInfo().isGreaterThan(new VersionInfo(3, 42, 0)) || TreadlyProfileSettingsFragment.this.bleComponent.getVersionInfo().isEqual(new VersionInfo(3, 42, 0))) {
                TreadlyProfileSettingsFragment.this.expectingUnclaimedCheck = TreadlyServiceManager.getInstance().getUnclaimedUserStatsLogInfo(TreadlyProfileSettingsFragment.this.bleComponent.getId(), new C00391());
            } else {
                TreadlyProfileSettingsFragment.this.expectingUnclaimedCheck = TreadlyClientLib.shared.getUnclaimedUserStatsLogInfo();
            }
            ((MainActivity) TreadlyProfileSettingsFragment.this.getActivity()).showLoadingDialog(false);
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        /* renamed from: com.treadly.Treadly.UI.TreadlyProfile.Settings.TreadlyProfileSettingsFragment$1$1  reason: invalid class name and collision with other inner class name */
        /* loaded from: classes2.dex */
        public class C00391 extends TreadlyServiceResponseEventAdapter {
            C00391() {
            }

            @Override // com.treadly.Treadly.Data.Listeners.TreadlyServiceResponseEventAdapter, com.treadly.Treadly.Data.Listeners.TreadlyServiceResponseEventListener
            public void onGetUnclaimedLogs(String str, final List<UserRunningSessionInfo> list) {
                super.onGetUnclaimedLogs(str, list);
                TreadlyProfileSettingsFragment.this.dismissLoading();
                if (TreadlyProfileSettingsFragment.this.expectingUnclaimedCheck) {
                    TreadlyProfileSettingsFragment.this.expectingUnclaimedCheck = false;
                    ActivityUtil.runOnUiThread(TreadlyProfileSettingsFragment.this.getActivity(), new Runnable() { // from class: com.treadly.Treadly.UI.TreadlyProfile.Settings.-$$Lambda$TreadlyProfileSettingsFragment$1$1$47t3rxj1jifWUPMJ68O1JuhRh8M
                        @Override // java.lang.Runnable
                        public final void run() {
                            TreadlyProfileSettingsFragment.AnonymousClass1.C00391.lambda$onGetUnclaimedLogs$0(TreadlyProfileSettingsFragment.AnonymousClass1.C00391.this, list);
                        }
                    });
                    return;
                }
                MainActivity mainActivity = (MainActivity) TreadlyProfileSettingsFragment.this.getActivity();
                if (mainActivity != null) {
                    mainActivity.closeLoadingDialog();
                }
            }

            public static /* synthetic */ void lambda$onGetUnclaimedLogs$0(C00391 c00391, List list) {
                TreadlyActivityUnclaimedFragment treadlyActivityUnclaimedFragment = new TreadlyActivityUnclaimedFragment();
                treadlyActivityUnclaimedFragment.unclaimedSessionInfos = list;
                treadlyActivityUnclaimedFragment.deviceAddress = TreadlyProfileSettingsFragment.this.bleComponent.getId();
                treadlyActivityUnclaimedFragment.isSessionInfo = true;
                TreadlyProfileSettingsFragment.this.addFragmentToStack(treadlyActivityUnclaimedFragment, TreadlyActivityUnclaimedFragment.TAG, TreadlyProfileSettingsFragment.TAG, true);
            }
        }

        @Override // com.treadly.Treadly.UI.TreadlyProfile.Settings.TreadlyProfileSettingsListController.TreadlyProfileSettingsEventListener
        public void didPressSingleUserMode() {
            if (TreadlyProfileSettingsFragment.this.getActivity() != null) {
                TreadlyProfileSettingsSingleUserModeFragment treadlyProfileSettingsSingleUserModeFragment = new TreadlyProfileSettingsSingleUserModeFragment();
                treadlyProfileSettingsSingleUserModeFragment.deviceAddress = TreadlyProfileSettingsFragment.this.bleComponent.getId();
                if (TreadlyProfileSettingsFragment.this.getActivity() != null) {
                    ((MainActivity) TreadlyProfileSettingsFragment.this.getActivity()).getSupportFragmentManager().beginTransaction().addToBackStack(null).add(R.id.activity_fragment_container_empty, treadlyProfileSettingsSingleUserModeFragment).commit();
                }
            }
        }

        @Override // com.treadly.Treadly.UI.TreadlyProfile.Settings.TreadlyProfileSettingsListController.TreadlyProfileSettingsEventListener
        public void didPressGeneralRow(TreadlyProfileSettingsListController.GeneralSectionTypes generalSectionTypes) {
            TreadlyProfileSettingsFragment.this.generalSectionPressed(generalSectionTypes);
        }

        @Override // com.treadly.Treadly.UI.TreadlyProfile.Settings.TreadlyProfileSettingsListController.TreadlyProfileSettingsEventListener
        public void didPressAboutRow(TreadlyProfileSettingsListController.AboutSectionTypes aboutSectionTypes) {
            TreadlyProfileSettingsFragment.this.aboutSectionPressed(aboutSectionTypes);
        }

        @Override // com.treadly.Treadly.UI.TreadlyProfile.Settings.TreadlyProfileSettingsListController.TreadlyProfileSettingsEventListener
        public void didPressAppRow(TreadlyProfileSettingsListController.AppSectionTypes appSectionTypes) {
            TreadlyProfileSettingsFragment.this.appSectionPressed(appSectionTypes);
        }

        @Override // com.treadly.Treadly.UI.TreadlyProfile.Settings.TreadlyProfileSettingsListController.TreadlyProfileSettingsEventListener
        public void didPressSettingsRow(TreadlyProfileSettingsListController.SettingsSectionTypes settingsSectionTypes) {
            TreadlyProfileSettingsFragment.this.settingsSectionPressed(settingsSectionTypes);
        }

        @Override // com.treadly.Treadly.UI.TreadlyProfile.Settings.TreadlyProfileSettingsListController.TreadlyProfileSettingsEventListener
        public void didPressUpdateWifi() {
            if (TreadlyProfileSettingsFragment.this.getActivity() != null) {
                ((MainActivity) TreadlyProfileSettingsFragment.this.getActivity()).getSupportFragmentManager().beginTransaction().addToBackStack(null).add(R.id.activity_fragment_container_empty, new TreadlyProfileSettingsWifiSetupFragment()).commit();
            }
        }
    }

    private void initList() {
        this.settingsListController = new TreadlyProfileSettingsListController(getContext(), this.settingsListView, new AnonymousClass1());
        this.settingsListController.bleComponent = this.bleComponent;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void generalSectionPressed(TreadlyProfileSettingsListController.GeneralSectionTypes generalSectionTypes) {
        if (generalSectionTypes != null && AnonymousClass5.$SwitchMap$com$treadly$Treadly$UI$TreadlyProfile$Settings$TreadlyProfileSettingsListController$GeneralSectionTypes[generalSectionTypes.ordinal()] == 1) {
            notificationsPressed();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void aboutSectionPressed(TreadlyProfileSettingsListController.AboutSectionTypes aboutSectionTypes) {
        Uri parse;
        String str = "";
        switch (aboutSectionTypes) {
            case privacyPolicy:
                parse = Uri.parse("https://treadly.co/app-privacy");
                str = "Privacy Policy";
                break;
            case termsAndCondition:
                parse = Uri.parse("https://treadly.co/app-terms");
                str = "Terms and Conditions";
                break;
            case supportCenter:
                parse = Uri.parse("https://support.treadly.co/hc/en-us");
                str = "Support Center";
                break;
            default:
                parse = null;
                break;
        }
        if (parse != null) {
            Intent intent = new Intent(getActivity(), NormalWebViewActivity.class);
            intent.putExtra("title", str);
            intent.putExtra("url", parse.toString());
            startActivity(intent);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* renamed from: com.treadly.Treadly.UI.TreadlyProfile.Settings.TreadlyProfileSettingsFragment$5  reason: invalid class name */
    /* loaded from: classes2.dex */
    public static /* synthetic */ class AnonymousClass5 {
        static final /* synthetic */ int[] $SwitchMap$com$treadly$Treadly$UI$TreadlyProfile$Settings$TreadlyProfileSettingsListController$GeneralSectionTypes;

        static {
            try {
                $SwitchMap$com$treadly$Treadly$UI$TreadlyProfile$Settings$TreadlyProfileSettingsListController$AppSectionTypes[TreadlyProfileSettingsListController.AppSectionTypes.faq.ordinal()] = 1;
            } catch (NoSuchFieldError unused) {
            }
            try {
                $SwitchMap$com$treadly$Treadly$UI$TreadlyProfile$Settings$TreadlyProfileSettingsListController$AppSectionTypes[TreadlyProfileSettingsListController.AppSectionTypes.whatsNew.ordinal()] = 2;
            } catch (NoSuchFieldError unused2) {
            }
            try {
                $SwitchMap$com$treadly$Treadly$UI$TreadlyProfile$Settings$TreadlyProfileSettingsListController$AppSectionTypes[TreadlyProfileSettingsListController.AppSectionTypes.appSettings.ordinal()] = 3;
            } catch (NoSuchFieldError unused3) {
            }
            try {
                $SwitchMap$com$treadly$Treadly$UI$TreadlyProfile$Settings$TreadlyProfileSettingsListController$AppSectionTypes[TreadlyProfileSettingsListController.AppSectionTypes.healthApp.ordinal()] = 4;
            } catch (NoSuchFieldError unused4) {
            }
            $SwitchMap$com$treadly$Treadly$UI$TreadlyProfile$Settings$TreadlyProfileSettingsListController$AboutSectionTypes = new int[TreadlyProfileSettingsListController.AboutSectionTypes.values().length];
            try {
                $SwitchMap$com$treadly$Treadly$UI$TreadlyProfile$Settings$TreadlyProfileSettingsListController$AboutSectionTypes[TreadlyProfileSettingsListController.AboutSectionTypes.privacyPolicy.ordinal()] = 1;
            } catch (NoSuchFieldError unused5) {
            }
            try {
                $SwitchMap$com$treadly$Treadly$UI$TreadlyProfile$Settings$TreadlyProfileSettingsListController$AboutSectionTypes[TreadlyProfileSettingsListController.AboutSectionTypes.termsAndCondition.ordinal()] = 2;
            } catch (NoSuchFieldError unused6) {
            }
            try {
                $SwitchMap$com$treadly$Treadly$UI$TreadlyProfile$Settings$TreadlyProfileSettingsListController$AboutSectionTypes[TreadlyProfileSettingsListController.AboutSectionTypes.supportCenter.ordinal()] = 3;
            } catch (NoSuchFieldError unused7) {
            }
            try {
                $SwitchMap$com$treadly$Treadly$UI$TreadlyProfile$Settings$TreadlyProfileSettingsListController$AboutSectionTypes[TreadlyProfileSettingsListController.AboutSectionTypes.rateApp.ordinal()] = 4;
            } catch (NoSuchFieldError unused8) {
            }
            $SwitchMap$com$treadly$Treadly$UI$TreadlyProfile$Settings$TreadlyProfileSettingsListController$GeneralSectionTypes = new int[TreadlyProfileSettingsListController.GeneralSectionTypes.values().length];
            try {
                $SwitchMap$com$treadly$Treadly$UI$TreadlyProfile$Settings$TreadlyProfileSettingsListController$GeneralSectionTypes[TreadlyProfileSettingsListController.GeneralSectionTypes.pushNotifications.ordinal()] = 1;
            } catch (NoSuchFieldError unused9) {
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void appSectionPressed(TreadlyProfileSettingsListController.AppSectionTypes appSectionTypes) {
        if (appSectionTypes == null) {
            return;
        }
        switch (appSectionTypes) {
            case faq:
                Intent intent = new Intent(getActivity(), NormalWebViewActivity.class);
                intent.putExtra("title", "FAQ");
                intent.putExtra("url", "https://treadly.co/app-privacy");
                startActivity(intent);
                return;
            case whatsNew:
                String format = String.format("https://treadly.co/whats-new?version=%s", "1.1.8");
                Intent intent2 = new Intent(getActivity(), NormalWebViewActivity.class);
                intent2.putExtra("title", "Whats New?");
                intent2.putExtra("url", format);
                startActivity(intent2);
                return;
            case appSettings:
                editProfileSettingsPressed();
                return;
            default:
                return;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void logout() {
        SharedPreferences.shared.storeUserEmail(null);
        SharedPreferences.shared.storeUserPassword(null);
        SharedPreferences.shared.storeFacebookToken(null);
        SharedPreferences.shared.storeFacebookUserId(null);
        TreadlyClientLib.shared.disconnect();
        TreadlyServiceManager.getInstance().logout();
        RunningSessionManager.getInstance().reset();
        DeviceUserStatsLogManager.getInstance().clear();
        AppActivityManager.shared.clear();
        MainActivity mainActivity = (MainActivity) getActivity();
        if (mainActivity != null) {
            mainActivity.clearConnectFragment();
            startActivity(new Intent(mainActivity, LoginActivity.class));
            mainActivity.finish();
        }
    }

    private void editProfileSettingsPressed() {
        TreadlyProfileEditFragment treadlyProfileEditFragment = new TreadlyProfileEditFragment();
        treadlyProfileEditFragment.fromLogin = false;
        if (getActivity() != null) {
            ((MainActivity) getActivity()).getSupportFragmentManager().beginTransaction().addToBackStack(null).add(R.id.activity_fragment_container_empty, treadlyProfileEditFragment).commit();
        }
    }

    private void notificationsPressed() {
        if (getActivity() == null) {
            return;
        }
        getActivity().getSupportFragmentManager().beginTransaction().add(R.id.activity_fragment_container_empty, new TreadlyProfileSettingsNotificationsFragment()).addToBackStack(null).commit();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* renamed from: com.treadly.Treadly.UI.TreadlyProfile.Settings.TreadlyProfileSettingsFragment$2  reason: invalid class name */
    /* loaded from: classes2.dex */
    public class AnonymousClass2 extends OtaUpdateRequestEventAdapter {
        AnonymousClass2() {
        }

        @Override // com.treadly.client.lib.sdk.Listeners.OtaUpdateRequestEventAdapter, com.treadly.client.lib.sdk.Listeners.OtaUpdateRequestEventListener
        public void onOtaUpdateFirmwareVersionAvailable(final boolean z, final FirmwareVersion firmwareVersion, final String[] strArr, boolean z2) {
            if (TreadlyProfileSettingsFragment.this.checkingOtaUpdate) {
                ActivityUtil.runOnUiThread(TreadlyProfileSettingsFragment.this.getActivity(), new Runnable() { // from class: com.treadly.Treadly.UI.TreadlyProfile.Settings.-$$Lambda$TreadlyProfileSettingsFragment$2$xHsZPOKbCleVKkvBRFAIkbMfpPg
                    @Override // java.lang.Runnable
                    public final void run() {
                        TreadlyProfileSettingsFragment.AnonymousClass2.lambda$onOtaUpdateFirmwareVersionAvailable$1(TreadlyProfileSettingsFragment.AnonymousClass2.this, z, firmwareVersion, strArr);
                    }
                });
            }
        }

        public static /* synthetic */ void lambda$onOtaUpdateFirmwareVersionAvailable$1(final AnonymousClass2 anonymousClass2, boolean z, FirmwareVersion firmwareVersion, String[] strArr) {
            TreadlyProfileSettingsFragment.this.checkingOtaUpdate = false;
            TreadlyProfileSettingsFragment.this.dismissLoading();
            if (TreadlyProfileSettingsFragment.this.getActivity() == null) {
                return;
            }
            if (z && firmwareVersion != null) {
                String format = String.format("An update to %s is available. Please press \"Update\" to begin", firmwareVersion.version);
                if (strArr != null) {
                    int length = strArr.length;
                    String str = "\n";
                    for (int i = 0; i < length; i++) {
                        str = str.concat(String.format("\n• %s", strArr[i]));
                    }
                    format = format + str;
                }
                TreadlyProfileSettingsFragment.this.setBaseAlert(new AlertDialog.Builder(TreadlyProfileSettingsFragment.this.getContext()).setTitle("Update Check").setMessage(format).setPositiveButton("Update", new DialogInterface.OnClickListener() { // from class: com.treadly.Treadly.UI.TreadlyProfile.Settings.-$$Lambda$TreadlyProfileSettingsFragment$2$sHwmsT9YfK-nxKtcNwc-pne_CSw
                    @Override // android.content.DialogInterface.OnClickListener
                    public final void onClick(DialogInterface dialogInterface, int i2) {
                        TreadlyProfileSettingsFragment.this.addFragmentToStack((Fragment) new FirmwareUpdateSsidFragment(), true);
                    }
                }).setNegativeButton("Dismiss", (DialogInterface.OnClickListener) null));
                return;
            }
            TreadlyProfileSettingsFragment.this.showBaseAlert("Firmware Update Check 👍", "No new update was found. Your Treadly has the latest firmware installed.");
        }

        @Override // com.treadly.client.lib.sdk.Listeners.OtaUpdateRequestEventAdapter, com.treadly.client.lib.sdk.Listeners.OtaUpdateRequestEventListener
        public void onOtaUpdateWiFiApResponse(WifiApInfo wifiApInfo) {
            if (TreadlyProfileSettingsFragment.this.isExpectingApInfo) {
                TreadlyProfileSettingsFragment.this.isExpectingApInfo = false;
                if (wifiApInfo.persistent) {
                    if (wifiApInfo.ssid.replace("\u0000", "").isEmpty()) {
                        TreadlyProfileSettingsFragment.this.settingsListController.setDeviceStatus(DeviceStatusCode.WIFI_ERROR);
                    } else {
                        TreadlyProfileSettingsFragment.this.settingsListController.setDeviceStatus(wifiApInfo.connected ? DeviceStatusCode.NO_ERROR : DeviceStatusCode.WIFI_ERROR);
                    }
                }
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* renamed from: com.treadly.Treadly.UI.TreadlyProfile.Settings.TreadlyProfileSettingsFragment$3  reason: invalid class name */
    /* loaded from: classes2.dex */
    public class AnonymousClass3 extends RequestEventAdapter {
        AnonymousClass3() {
        }

        @Override // com.treadly.client.lib.sdk.Listeners.RequestEventAdapter, com.treadly.client.lib.sdk.Listeners.RequestEventListener
        public void onRequestStatusResponse(boolean z, DeviceStatus deviceStatus) {
            if (z && deviceStatus != null) {
                if (TreadlyProfileSettingsFragment.this.unitInfo != deviceStatus.getDistanceUnits()) {
                    if (TreadlyProfileSettingsFragment.this.unitStatusCount <= 0) {
                        TreadlyProfileSettingsFragment.this.unitStatusCount = 2;
                    } else {
                        TreadlyProfileSettingsFragment.this.unitStatusCount -= 2;
                    }
                } else {
                    TreadlyProfileSettingsFragment.this.unitStatusCount = 0;
                }
                if (TreadlyProfileSettingsFragment.this.pairingModeStatusCount == 0) {
                    PairingModeTriggerType pairingModeTriggerType = deviceStatus.getPairingModeTriggerType();
                    TreadlyProfileSettingsFragment.this.pairingModeType = pairingModeTriggerType;
                    TreadlyProfileSettingsFragment.this.waitingForPairingMode = false;
                    if (TreadlyProfileSettingsFragment.this.settingsListController != null) {
                        TreadlyProfileSettingsFragment.this.settingsListController.pairingModeType = pairingModeTriggerType;
                    }
                }
                TreadlyProfileSettingsFragment.this.idlePauseTimeout = Byte.valueOf(deviceStatus.getPauseAfterIdleTimeout());
                TreadlyProfileSettingsFragment.this.idlePauseEnabled = Boolean.valueOf(deviceStatus.isPauseAfterIdleEnabled());
                TreadlyProfileSettingsFragment.this.waitingForIdlePause = false;
                if (!TreadlyProfileSettingsFragment.this.aboutSelected || TreadlyProfileSettingsFragment.this.waitingForComponents || TreadlyProfileSettingsFragment.this.waitingForPairingMode) {
                    return;
                }
                TreadlyProfileSettingsFragment.this.toAboutPage();
            }
        }

        @Override // com.treadly.client.lib.sdk.Listeners.RequestEventAdapter, com.treadly.client.lib.sdk.Listeners.RequestEventListener
        public void onRequestGetDeviceComponentsListResponse(final boolean z, final ComponentInfo[] componentInfoArr) {
            ActivityUtil.runOnUiThread(TreadlyProfileSettingsFragment.this.getActivity(), new Runnable() { // from class: com.treadly.Treadly.UI.TreadlyProfile.Settings.-$$Lambda$TreadlyProfileSettingsFragment$3$UE0baFYczspKXVrPt674y9nsRes
                @Override // java.lang.Runnable
                public final void run() {
                    TreadlyProfileSettingsFragment.AnonymousClass3.lambda$onRequestGetDeviceComponentsListResponse$0(TreadlyProfileSettingsFragment.AnonymousClass3.this, componentInfoArr, z);
                }
            });
        }

        public static /* synthetic */ void lambda$onRequestGetDeviceComponentsListResponse$0(AnonymousClass3 anonymousClass3, ComponentInfo[] componentInfoArr, boolean z) {
            if (componentInfoArr == null || !z) {
                return;
            }
            int length = componentInfoArr.length;
            int i = 0;
            while (true) {
                if (i >= length) {
                    break;
                }
                ComponentInfo componentInfo = componentInfoArr[i];
                if (componentInfo.getType() == ComponentType.bleBoard) {
                    TreadlyProfileSettingsFragment.this.setBleComponent(componentInfo);
                    break;
                }
                i++;
            }
            TreadlyProfileSettingsFragment.this.deviceComponents = Arrays.asList(componentInfoArr);
            TreadlyProfileSettingsFragment.this.waitingForComponents = false;
            if (TreadlyProfileSettingsFragment.this.aboutSelected && !TreadlyProfileSettingsFragment.this.waitingForPairingMode && !TreadlyProfileSettingsFragment.this.waitingForIdlePause) {
                TreadlyProfileSettingsFragment.this.toAboutPage();
            }
            TreadlyProfileSettingsFragment.this.isExpectingApInfo = TreadlyClientLib.shared.getOtaConfigSettings();
        }
    }

    @Override // com.treadly.client.lib.sdk.Listeners.DeviceConnectionEventListener
    public void onDeviceConnectionChanged(final DeviceConnectionEvent deviceConnectionEvent) {
        ActivityUtil.runOnUiThread(getActivity(), new Runnable() { // from class: com.treadly.Treadly.UI.TreadlyProfile.Settings.-$$Lambda$TreadlyProfileSettingsFragment$ytnmTv22nKDqPHKXQjBhR-YZEu8
            @Override // java.lang.Runnable
            public final void run() {
                TreadlyProfileSettingsFragment.lambda$onDeviceConnectionChanged$2(TreadlyProfileSettingsFragment.this, deviceConnectionEvent);
            }
        });
    }

    public static /* synthetic */ void lambda$onDeviceConnectionChanged$2(TreadlyProfileSettingsFragment treadlyProfileSettingsFragment, DeviceConnectionEvent deviceConnectionEvent) {
        if (deviceConnectionEvent != null && (deviceConnectionEvent.getStatus() == DeviceConnectionStatus.notConnected || deviceConnectionEvent.getStatus() == DeviceConnectionStatus.disconnecting)) {
            treadlyProfileSettingsFragment.setBleComponent(null);
            treadlyProfileSettingsFragment.deviceStatus = null;
            treadlyProfileSettingsFragment.pairingModeType = null;
            treadlyProfileSettingsFragment.deviceComponents = new ArrayList();
            treadlyProfileSettingsFragment.handleDeviceDisconnected();
            treadlyProfileSettingsFragment.dismissBaseAlert();
        } else if (deviceConnectionEvent.getStatus() == DeviceConnectionStatus.connected) {
            treadlyProfileSettingsFragment.isExpectingApInfo = TreadlyClientLib.shared.getOtaConfigSettings();
        }
    }

    void checkUpdatesPressed() {
        if (this.checkingOtaUpdate) {
            return;
        }
        HandlerThread handlerThread = new HandlerThread("ServiceThread");
        handlerThread.start();
        new Handler(handlerThread.getLooper()).post(new Runnable() { // from class: com.treadly.Treadly.UI.TreadlyProfile.Settings.-$$Lambda$TreadlyProfileSettingsFragment$X4WrUt6PAPV4oRNUtouH2i5WmPI
            @Override // java.lang.Runnable
            public final void run() {
                TreadlyProfileSettingsFragment.lambda$checkUpdatesPressed$3(TreadlyProfileSettingsFragment.this);
            }
        });
    }

    public static /* synthetic */ void lambda$checkUpdatesPressed$3(final TreadlyProfileSettingsFragment treadlyProfileSettingsFragment) {
        if (treadlyProfileSettingsFragment.bleComponent == null || !TreadlyClientLib.shared.checkFirmwareUpdates(treadlyProfileSettingsFragment.bleComponent)) {
            return;
        }
        treadlyProfileSettingsFragment.checkingOtaUpdate = true;
        ActivityUtil.runOnUiThread(treadlyProfileSettingsFragment.getActivity(), new Runnable() { // from class: com.treadly.Treadly.UI.TreadlyProfile.Settings.-$$Lambda$TreadlyProfileSettingsFragment$8faTc97SVdVwnXfUY9kvUxuOBLM
            @Override // java.lang.Runnable
            public final void run() {
                TreadlyProfileSettingsFragment.this.showLoading();
            }
        });
    }

    void toAboutPage() {
        this.aboutSelected = false;
        this.aboutFragment = new TreadlyProfileSettingsAboutFragment();
        this.aboutFragment.handrailStatus = this.pairingModeType;
        if (this.deviceComponents != null) {
            this.aboutFragment.components = this.deviceComponents;
        }
        this.aboutFragment.isPauseAfterIdleEnabled = this.idlePauseEnabled != null ? this.idlePauseEnabled.booleanValue() : false;
        this.aboutFragment.fragmentListener = new TreadlyProfileSettingsAboutFragment.TreadlyProfileSettingsAboutFragmentListener() { // from class: com.treadly.Treadly.UI.TreadlyProfile.Settings.-$$Lambda$TreadlyProfileSettingsFragment$AIuO2YN3a296dNT0RTjucE5LZyY
            @Override // com.treadly.Treadly.UI.TreadlyProfile.Settings.About.TreadlyProfileSettingsAboutFragment.TreadlyProfileSettingsAboutFragmentListener
            public final void adminSettingsSelected() {
                TreadlyProfileSettingsFragment.this.toAdminSettings();
            }
        };
        if (getActivity() != null) {
            ((MainActivity) getActivity()).getSupportFragmentManager().beginTransaction().addToBackStack(null).add(R.id.activity_fragment_container_empty, this.aboutFragment, TreadlyProfileSettingsAboutFragment.TAG).commit();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void toAdminSettings() {
        FragmentActivity activity = getActivity();
        if (activity == null) {
            return;
        }
        TreadlyProfileSettingsAdminFragment treadlyProfileSettingsAdminFragment = new TreadlyProfileSettingsAdminFragment();
        boolean z = true;
        treadlyProfileSettingsAdminFragment.emergencyHandrailStopEnabled = this.deviceStatus != null && this.deviceStatus.isEmergencyHandrailEnabled();
        if (this.deviceStatus == null || !this.deviceStatus.isBleEnabled()) {
            z = false;
        }
        treadlyProfileSettingsAdminFragment.bleEnabled = z;
        treadlyProfileSettingsAdminFragment.irMode = this.deviceStatus != null ? this.deviceStatus.getIrMode() : IrMode.GEN_1;
        treadlyProfileSettingsAdminFragment.fragmentListener = new TreadlyProfileSettingsAdminFragment.TreadlyProfileSettingsAdminFragmentListener() { // from class: com.treadly.Treadly.UI.TreadlyProfile.Settings.-$$Lambda$TreadlyProfileSettingsFragment$DulT05Zva5eXI7QT_jo_X7xZDGQ
            @Override // com.treadly.Treadly.UI.TreadlyProfile.Settings.Admin.TreadlyProfileSettingsAdminFragment.TreadlyProfileSettingsAdminFragmentListener
            public final void onStop() {
                TreadlyProfileSettingsFragment.lambda$toAdminSettings$4(TreadlyProfileSettingsFragment.this);
            }
        };
        TotalStatus totalStatus = new TotalStatus();
        if (this.deviceStatus != null) {
            totalStatus.totalSteps = this.deviceStatus.getTotalSteps();
            totalStatus.totalDistance = this.deviceStatus.getTotalDistance();
            totalStatus.distanceUnits = this.deviceStatus.getDistanceUnits();
        } else {
            totalStatus.totalDistance = 0.0f;
            totalStatus.totalSteps = 0;
            totalStatus.distanceUnits = DistanceUnits.MI;
        }
        treadlyProfileSettingsAdminFragment.totalStatus = totalStatus;
        activity.getSupportFragmentManager().beginTransaction().addToBackStack(null).add(R.id.activity_fragment_container_empty, treadlyProfileSettingsAdminFragment, TreadlyProfileSettingsAdminFragment.TAG).commit();
    }

    public static /* synthetic */ void lambda$toAdminSettings$4(TreadlyProfileSettingsFragment treadlyProfileSettingsFragment) {
        if (treadlyProfileSettingsFragment.aboutFragment != null) {
            treadlyProfileSettingsFragment.aboutFragment.onStart();
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* renamed from: com.treadly.Treadly.UI.TreadlyProfile.Settings.TreadlyProfileSettingsFragment$4  reason: invalid class name */
    /* loaded from: classes2.dex */
    public class AnonymousClass4 extends DeviceUserStatsLogEventAdapter {
        AnonymousClass4() {
        }

        @Override // com.treadly.client.lib.sdk.Listeners.DeviceUserStatsLogEventAdapter, com.treadly.client.lib.sdk.Listeners.DeviceUserStatsLogEventListener
        public void onDeviceUserStatsUnclaimedUserStatsInfo(final DeviceUserStatsUnclaimedLogInfo[] deviceUserStatsUnclaimedLogInfoArr) {
            if (TreadlyProfileSettingsFragment.this.expectingUnclaimedCheck) {
                TreadlyProfileSettingsFragment.this.expectingUnclaimedCheck = false;
                ActivityUtil.runOnUiThread(TreadlyProfileSettingsFragment.this.getActivity(), new Runnable() { // from class: com.treadly.Treadly.UI.TreadlyProfile.Settings.-$$Lambda$TreadlyProfileSettingsFragment$4$TqYnHOeiv8lGbMluG6gkG7ufZPQ
                    @Override // java.lang.Runnable
                    public final void run() {
                        TreadlyProfileSettingsFragment.AnonymousClass4.lambda$onDeviceUserStatsUnclaimedUserStatsInfo$0(TreadlyProfileSettingsFragment.AnonymousClass4.this, deviceUserStatsUnclaimedLogInfoArr);
                    }
                });
            }
        }

        public static /* synthetic */ void lambda$onDeviceUserStatsUnclaimedUserStatsInfo$0(AnonymousClass4 anonymousClass4, DeviceUserStatsUnclaimedLogInfo[] deviceUserStatsUnclaimedLogInfoArr) {
            TreadlyActivityUnclaimedFragment treadlyActivityUnclaimedFragment = new TreadlyActivityUnclaimedFragment();
            treadlyActivityUnclaimedFragment.unclaimedActivities = Arrays.asList(deviceUserStatsUnclaimedLogInfoArr);
            TreadlyProfileSettingsFragment.this.addFragmentToStack(treadlyActivityUnclaimedFragment, TreadlyActivityUnclaimedFragment.TAG, TreadlyProfileSettingsFragment.TAG, true);
        }
    }
}
