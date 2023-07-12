package com.treadly.Treadly.UI.TreadlyProfile.Settings;

import android.content.Context;
import android.view.View;
import android.widget.ExpandableListView;
import com.treadly.Treadly.Data.Model.UserInfo;
import com.treadly.Treadly.UI.Util.SharedPreferences;
import com.treadly.client.lib.sdk.Model.ComponentInfo;
import com.treadly.client.lib.sdk.Model.DeviceStatusCode;
import com.treadly.client.lib.sdk.Model.DistanceUnits;
import com.treadly.client.lib.sdk.Model.OtaUpdateInfo;
import com.treadly.client.lib.sdk.Model.PairingModeTriggerType;
import com.treadly.client.lib.sdk.Model.VersionInfo;
import java.util.ArrayList;
import java.util.List;

/* loaded from: classes2.dex */
public class TreadlyProfileSettingsListController {
    protected TreadlyProfileSettingsListAdapter adapter;
    protected ComponentInfo bleComponent;
    protected Context context;
    private ExpandableListView listView;
    protected PairingModeTriggerType pairingModeType;
    protected DistanceUnits unitInfo;
    protected List<UserInfo> users = new ArrayList();
    protected List<GeneralSectionTypes> generalSectionTypes = new ArrayList();
    protected List<AppSectionTypes> appSectionTypes = new ArrayList();
    protected List<SettingsSectionTypes> settingsSectionTypes = new ArrayList();
    protected List<AboutSectionTypes> aboutSectionTypes = new ArrayList();
    protected List<SettingsSections> sectionTypes = new ArrayList();
    protected Boolean wifiConnected = null;
    protected DeviceStatusCode deviceStatus = null;

    /* JADX INFO: Access modifiers changed from: protected */
    /* loaded from: classes2.dex */
    public interface TreadlyProfileSettingsEventListener {
        void didChangeHealthApp();

        void didPressAboutRow(AboutSectionTypes aboutSectionTypes);

        void didPressAppRow(AppSectionTypes appSectionTypes);

        void didPressChangePassword();

        void didPressChangeUsername();

        void didPressCheckOta();

        void didPressGeneralRow(GeneralSectionTypes generalSectionTypes);

        void didPressIdleConfig();

        void didPressLogout();

        void didPressMaintenance();

        void didPressOldSettings();

        void didPressPairedPhones();

        void didPressPairingModeType(boolean z);

        void didPressPrivateVideoCompositionOptions();

        void didPressSettingsRow(SettingsSectionTypes settingsSectionTypes);

        void didPressSingleUserMode();

        void didPressSpeakerPassword();

        void didPressSystemVersion();

        void didPressUnclaimedList();

        void didPressUnitSwitch(boolean z);

        void didPressUpdateWifi();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static /* synthetic */ boolean lambda$new$0(ExpandableListView expandableListView, View view, int i, long j) {
        return true;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    /* loaded from: classes2.dex */
    public enum SettingsSectionTypes {
        speakerPassword(0),
        pairingModeType(1),
        checkUpdates(2),
        idlePauseConfig(3),
        maintenance(4),
        pairedPhones(5),
        unclaimedList(6),
        singleUserMode(7),
        updateWifiSettingsConnected(8),
        updateWifiSettingsNotConnected(9),
        getUpdateWifiSettingUnknown(10);
        
        private final int value;

        SettingsSectionTypes(int i) {
            this.value = i;
        }

        public static SettingsSectionTypes fromValue(int i) {
            SettingsSectionTypes[] values;
            for (SettingsSectionTypes settingsSectionTypes : values()) {
                if (settingsSectionTypes.value == i) {
                    return settingsSectionTypes;
                }
            }
            return null;
        }

        public int getValue() {
            return this.value;
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    /* loaded from: classes2.dex */
    public enum GeneralSectionTypes {
        systemVersion(0),
        units(1),
        pushNotifications(2),
        changePassword(3),
        theme(4),
        privateVideoComposite(5),
        changeUsername(6);
        
        private final int value;

        GeneralSectionTypes(int i) {
            this.value = i;
        }

        public static GeneralSectionTypes fromValue(int i) {
            GeneralSectionTypes[] values;
            for (GeneralSectionTypes generalSectionTypes : values()) {
                if (generalSectionTypes.value == i) {
                    return generalSectionTypes;
                }
            }
            return null;
        }

        public int getValue() {
            return this.value;
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    /* loaded from: classes2.dex */
    public enum AppSectionTypes {
        appSettings(0),
        faq(1),
        whatsNew(2),
        healthApp(3);
        
        private final int value;

        AppSectionTypes(int i) {
            this.value = i;
        }

        public static AppSectionTypes fromValue(int i) {
            AppSectionTypes[] values;
            for (AppSectionTypes appSectionTypes : values()) {
                if (appSectionTypes.value == i) {
                    return appSectionTypes;
                }
            }
            return null;
        }

        public int getValue() {
            return this.value;
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    /* loaded from: classes2.dex */
    public enum AboutSectionTypes {
        supportCenter(0),
        privacyPolicy(1),
        termsAndCondition(2),
        rateApp(3);
        
        private final int value;

        AboutSectionTypes(int i) {
            this.value = i;
        }

        public static AboutSectionTypes fromValue(int i) {
            AboutSectionTypes[] values;
            for (AboutSectionTypes aboutSectionTypes : values()) {
                if (aboutSectionTypes.value == i) {
                    return aboutSectionTypes;
                }
            }
            return null;
        }

        public int getValue() {
            return this.value;
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    /* loaded from: classes2.dex */
    public enum SettingsSections {
        general(0),
        app(1),
        settings(2),
        about(3),
        logout(4);
        
        private final int value;

        SettingsSections(int i) {
            this.value = i;
        }

        public static SettingsSections fromValue(int i) {
            SettingsSections[] values;
            for (SettingsSections settingsSections : values()) {
                if (settingsSections.value == i) {
                    return settingsSections;
                }
            }
            return null;
        }

        public int getValue() {
            return this.value;
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public TreadlyProfileSettingsListController(Context context, ExpandableListView expandableListView, TreadlyProfileSettingsEventListener treadlyProfileSettingsEventListener) {
        this.adapter = new TreadlyProfileSettingsListAdapter(context, treadlyProfileSettingsEventListener, this);
        expandableListView.setAdapter(this.adapter);
        expandableListView.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() { // from class: com.treadly.Treadly.UI.TreadlyProfile.Settings.-$$Lambda$TreadlyProfileSettingsListController$m67WM5sc_0oboy0vW9ZORfIDW2U
            @Override // android.widget.ExpandableListView.OnGroupClickListener
            public final boolean onGroupClick(ExpandableListView expandableListView2, View view, int i, long j) {
                return TreadlyProfileSettingsListController.lambda$new$0(expandableListView2, view, i, j);
            }
        });
        expandableListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() { // from class: com.treadly.Treadly.UI.TreadlyProfile.Settings.-$$Lambda$TreadlyProfileSettingsListController$REYDp9I-otXxR11Dzex-fPjo6o0
            @Override // android.widget.ExpandableListView.OnChildClickListener
            public final boolean onChildClick(ExpandableListView expandableListView2, View view, int i, int i2, long j) {
                return TreadlyProfileSettingsListController.lambda$new$1(TreadlyProfileSettingsListController.this, expandableListView2, view, i, i2, j);
            }
        });
        expandableListView.setChildDivider(null);
        expandableListView.setChildIndicator(null);
        expandableListView.setDivider(null);
        this.context = context;
        this.adapter.notifyDataSetChanged();
    }

    public static /* synthetic */ boolean lambda$new$1(TreadlyProfileSettingsListController treadlyProfileSettingsListController, ExpandableListView expandableListView, View view, int i, int i2, long j) {
        if (treadlyProfileSettingsListController.adapter != null) {
            treadlyProfileSettingsListController.adapter.didSelectItem(i, i2);
            return true;
        }
        return true;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void setBleComponent(ComponentInfo componentInfo) {
        this.bleComponent = componentInfo;
        this.adapter.notifyDataSetChanged();
    }

    protected void setWifiConnected(boolean z) {
        this.wifiConnected = Boolean.valueOf(z);
        updateSections();
        this.adapter.notifyDataSetChanged();
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void setDeviceStatus(DeviceStatusCode deviceStatusCode) {
        this.deviceStatus = deviceStatusCode;
        updateSections();
        this.adapter.notifyDataSetChanged();
    }

    protected void getData() {
        if (this.adapter != null) {
            this.adapter.notifyDataSetChanged();
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void updateSections() {
        this.sectionTypes.clear();
        this.sectionTypes.add(SettingsSections.general);
        this.sectionTypes.add(SettingsSections.app);
        updateSettingsSectionTypes();
        if (this.settingsSectionTypes.size() > 0) {
            this.sectionTypes.add(SettingsSections.settings);
        }
        this.sectionTypes.add(SettingsSections.about);
        this.sectionTypes.add(SettingsSections.logout);
        if (this.adapter != null) {
            this.adapter.sectionTypes = this.sectionTypes;
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void updateSettingsSectionTypes() {
        this.settingsSectionTypes.clear();
        if (isGen2()) {
            this.settingsSectionTypes.add(SettingsSectionTypes.checkUpdates);
        }
        if (hasSpeakerPassword()) {
            this.settingsSectionTypes.add(SettingsSectionTypes.speakerPassword);
        }
        if (hasPairingModeTriggerType()) {
            this.settingsSectionTypes.add(SettingsSectionTypes.pairingModeType);
        }
        if (hasMaintenance()) {
            this.settingsSectionTypes.add(SettingsSectionTypes.maintenance);
        }
        if (hasPairedPhones()) {
            this.settingsSectionTypes.add(SettingsSectionTypes.pairedPhones);
        }
        if (hasUnclaimedList()) {
            this.settingsSectionTypes.add(SettingsSectionTypes.unclaimedList);
        }
        if (hasSingleUserMode()) {
            this.settingsSectionTypes.add(SettingsSectionTypes.singleUserMode);
        }
        if (hasWifiOnboard()) {
            if (this.deviceStatus == null) {
                this.settingsSectionTypes.add(SettingsSectionTypes.getUpdateWifiSettingUnknown);
            } else if (this.deviceStatus == DeviceStatusCode.WIFI_ERROR) {
                this.settingsSectionTypes.add(SettingsSectionTypes.updateWifiSettingsNotConnected);
            } else if (this.deviceStatus == DeviceStatusCode.WIFI_SCANNING) {
                this.settingsSectionTypes.add(SettingsSectionTypes.getUpdateWifiSettingUnknown);
            } else {
                this.settingsSectionTypes.add(SettingsSectionTypes.updateWifiSettingsConnected);
            }
        }
        if (this.adapter != null) {
            this.adapter.settingsSectionTypes = this.settingsSectionTypes;
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void updateGeneralSectionTypes() {
        this.generalSectionTypes.clear();
        this.generalSectionTypes.add(GeneralSectionTypes.systemVersion);
        this.generalSectionTypes.add(GeneralSectionTypes.pushNotifications);
        if (isUserAccount()) {
            this.generalSectionTypes.add(GeneralSectionTypes.changeUsername);
            this.generalSectionTypes.add(GeneralSectionTypes.changePassword);
        }
        if (this.adapter != null) {
            this.adapter.generalSectionTypes = this.generalSectionTypes;
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void updateAppSectionTypes() {
        this.appSectionTypes.clear();
        this.appSectionTypes.add(AppSectionTypes.appSettings);
        this.appSectionTypes.add(AppSectionTypes.faq);
        this.appSectionTypes.add(AppSectionTypes.whatsNew);
        if (this.adapter != null) {
            this.adapter.appSectionTypes = this.appSectionTypes;
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void updateAboutSectionTypes() {
        this.aboutSectionTypes.clear();
        this.aboutSectionTypes.add(AboutSectionTypes.supportCenter);
        this.aboutSectionTypes.add(AboutSectionTypes.privacyPolicy);
        this.aboutSectionTypes.add(AboutSectionTypes.termsAndCondition);
        if (this.adapter != null) {
            this.adapter.aboutSectionTypes = this.aboutSectionTypes;
        }
    }

    private boolean hasSpeakerPassword() {
        if (this.bleComponent == null) {
            return false;
        }
        VersionInfo versionInfo = new VersionInfo(2, 14, 10);
        return this.bleComponent.getVersionInfo().isGreaterThan(versionInfo) || this.bleComponent.getVersionInfo().isEqual(versionInfo);
    }

    private boolean hasPairingModeTriggerType() {
        if (this.bleComponent == null) {
            return false;
        }
        if (isGreaterThanOrEqual(this.bleComponent, new VersionInfo(3, 0, 0))) {
            return isGreaterThanOrEqual(this.bleComponent, new VersionInfo(3, 3, 0));
        }
        return false;
    }

    private boolean isGen2() {
        return this.bleComponent != null && this.bleComponent.getVersionInfo().isGreaterThan(OtaUpdateInfo.minVersion);
    }

    private boolean hasIdlePauseConfig() {
        if (this.bleComponent == null) {
            return false;
        }
        return isGreaterThanOrEqual(this.bleComponent, new VersionInfo(3, 20, 0));
    }

    private boolean hasMaintenance() {
        return this.bleComponent != null;
    }

    private boolean hasPairedPhones() {
        if (this.bleComponent == null) {
            return false;
        }
        return isGreaterThanOrEqual(this.bleComponent, new VersionInfo(3, 26, 0));
    }

    private boolean hasUnclaimedList() {
        if (this.bleComponent == null) {
            return false;
        }
        return isGreaterThanOrEqual(this.bleComponent, new VersionInfo(3, 34, 0));
    }

    private boolean hasWifiOnboard() {
        if (this.bleComponent == null) {
            return false;
        }
        return isGreaterThanOrEqual(this.bleComponent, new VersionInfo(3, 63, 0));
    }

    private boolean hasSingleUserMode() {
        if (this.bleComponent == null) {
            return false;
        }
        return isGreaterThanOrEqual(this.bleComponent, new VersionInfo(3, 42, 0));
    }

    private boolean canOtaUpdate() {
        return this.bleComponent != null && this.bleComponent.getVersionInfo().isGreaterThan(OtaUpdateInfo.minVersion);
    }

    private boolean isUserAccount() {
        return SharedPreferences.shared.getFacebookToken() == null && SharedPreferences.shared.getFacebookUserId() == null;
    }

    private boolean isGreaterThanOrEqual(ComponentInfo componentInfo, VersionInfo versionInfo) {
        return (componentInfo == null || versionInfo == null || (!componentInfo.getVersionInfo().isGreaterThan(versionInfo) && !componentInfo.getVersionInfo().isEqual(versionInfo))) ? false : true;
    }
}
