package com.treadly.Treadly.UI.TreadlyProfile.Settings.About;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;
import android.widget.ImageButton;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.facebook.appevents.AppEventsConstants;
import com.treadly.Treadly.MainActivity;
import com.treadly.Treadly.R;
import com.treadly.Treadly.UI.TreadlyProfile.Settings.About.TreadlyProfileSettingsAboutListAdapter;
import com.treadly.Treadly.UI.Util.BaseClasses.BaseFragment;
import com.treadly.client.lib.sdk.Listeners.RequestEventAdapter;
import com.treadly.client.lib.sdk.Listeners.RequestEventListener;
import com.treadly.client.lib.sdk.Model.ComponentInfo;
import com.treadly.client.lib.sdk.Model.ComponentType;
import com.treadly.client.lib.sdk.Model.DeviceStatus;
import com.treadly.client.lib.sdk.Model.PairingModeTriggerType;
import com.treadly.client.lib.sdk.Model.VersionInfo;
import com.treadly.client.lib.sdk.TreadlyClientLib;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import org.otwebrtc.PeerConnectionFactory;

/* loaded from: classes2.dex */
public class TreadlyProfileSettingsAboutFragment extends BaseFragment {
    public static final String TAG = "TreadlyProfileSettingsAboutFragment";
    private ExpandableListView aboutListView;
    private TreadlyProfileSettingsAboutListAdapter adapter;
    public TreadlyProfileSettingsAboutFragmentListener fragmentListener;
    private TreadlyProfileSettingsAboutListAdapter.TreadlyProfileSettingsAboutListAdapterEventListener listener = new TreadlyProfileSettingsAboutListAdapter.TreadlyProfileSettingsAboutListAdapterEventListener() { // from class: com.treadly.Treadly.UI.TreadlyProfile.Settings.About.TreadlyProfileSettingsAboutFragment.1
    };
    public List<ComponentInfo> components = new ArrayList();
    private List<AboutViewSections> sectionTypes = new ArrayList();
    public PairingModeTriggerType handrailStatus = null;
    public boolean isPauseAfterIdleEnabled = false;
    private RequestEventListener requestEventListener = new RequestEventAdapter() { // from class: com.treadly.Treadly.UI.TreadlyProfile.Settings.About.TreadlyProfileSettingsAboutFragment.2
        @Override // com.treadly.client.lib.sdk.Listeners.RequestEventAdapter, com.treadly.client.lib.sdk.Listeners.RequestEventListener
        public void onRequestStatusResponse(boolean z, DeviceStatus deviceStatus) {
            if (z || deviceStatus != null) {
                TreadlyProfileSettingsAboutFragment.this.isPauseAfterIdleEnabled = deviceStatus.isPauseAfterIdleEnabled();
            }
        }
    };

    /* loaded from: classes2.dex */
    public interface TreadlyProfileSettingsAboutFragmentListener {
        void adminSettingsSelected();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static /* synthetic */ boolean lambda$initList$1(ExpandableListView expandableListView, View view, int i, long j) {
        return true;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    /* loaded from: classes2.dex */
    public enum AboutViewSections {
        qrCode(0),
        appInfo(1),
        deviceInfo(2);
        
        private int value;

        AboutViewSections(int i) {
            this.value = i;
        }

        public static AboutViewSections fromValue(int i) {
            AboutViewSections[] values;
            for (AboutViewSections aboutViewSections : values()) {
                if (aboutViewSections.value == i) {
                    return aboutViewSections;
                }
            }
            return null;
        }

        public int getValue() {
            return this.value;
        }
    }

    @Override // androidx.fragment.app.Fragment
    @Nullable
    public View onCreateView(@NonNull LayoutInflater layoutInflater, @Nullable ViewGroup viewGroup, @Nullable Bundle bundle) {
        return layoutInflater.inflate(R.layout.fragment_profile_settings_about, viewGroup, false);
    }

    @Override // com.treadly.Treadly.UI.Util.BaseClasses.BaseFragment, androidx.fragment.app.Fragment
    public void onViewCreated(@NonNull View view, @Nullable Bundle bundle) {
        super.onViewCreated(view, bundle);
        ((TextView) view.findViewById(R.id.nav_title)).setText(R.string.about_title);
        ((ImageButton) view.findViewById(R.id.nav_back_arrow)).setOnClickListener(new View.OnClickListener() { // from class: com.treadly.Treadly.UI.TreadlyProfile.Settings.About.-$$Lambda$TreadlyProfileSettingsAboutFragment$Pl2Pk74UKyUP-SRmAXrLvH1YTrc
            @Override // android.view.View.OnClickListener
            public final void onClick(View view2) {
                TreadlyProfileSettingsAboutFragment.this.popBackStack();
            }
        });
        TreadlyClientLib.shared.addRequestEventListener(this.requestEventListener);
        this.aboutListView = (ExpandableListView) view.findViewById(R.id.about_list_view);
        initList();
    }

    @Override // androidx.fragment.app.Fragment
    public void onDestroy() {
        super.onDestroy();
        TreadlyClientLib.shared.removeRequestEventListener(this.requestEventListener);
    }

    private void initList() {
        this.adapter = new TreadlyProfileSettingsAboutListAdapter(getContext(), this.sectionTypes, this.components, this.listener, this);
        this.aboutListView.setAdapter(this.adapter);
        this.aboutListView.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() { // from class: com.treadly.Treadly.UI.TreadlyProfile.Settings.About.-$$Lambda$TreadlyProfileSettingsAboutFragment$8Rhlyi_QmepgslvMjORWheN0VQA
            @Override // android.widget.ExpandableListView.OnGroupClickListener
            public final boolean onGroupClick(ExpandableListView expandableListView, View view, int i, long j) {
                return TreadlyProfileSettingsAboutFragment.lambda$initList$1(expandableListView, view, i, j);
            }
        });
        this.aboutListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() { // from class: com.treadly.Treadly.UI.TreadlyProfile.Settings.About.-$$Lambda$TreadlyProfileSettingsAboutFragment$ULdSPJtzyh2-vx7mp86ktTGEK5s
            @Override // android.widget.ExpandableListView.OnChildClickListener
            public final boolean onChildClick(ExpandableListView expandableListView, View view, int i, int i2, long j) {
                return TreadlyProfileSettingsAboutFragment.lambda$initList$2(TreadlyProfileSettingsAboutFragment.this, expandableListView, view, i, i2, j);
            }
        });
        this.aboutListView.setChildDivider(null);
        this.aboutListView.setChildIndicator(null);
        this.aboutListView.setDivider(null);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* renamed from: com.treadly.Treadly.UI.TreadlyProfile.Settings.About.TreadlyProfileSettingsAboutFragment$3  reason: invalid class name */
    /* loaded from: classes2.dex */
    public static /* synthetic */ class AnonymousClass3 {
        static final /* synthetic */ int[] $SwitchMap$com$treadly$Treadly$UI$TreadlyProfile$Settings$About$TreadlyProfileSettingsAboutFragment$AboutViewSections = new int[AboutViewSections.values().length];

        static {
            try {
                $SwitchMap$com$treadly$Treadly$UI$TreadlyProfile$Settings$About$TreadlyProfileSettingsAboutFragment$AboutViewSections[AboutViewSections.appInfo.ordinal()] = 1;
            } catch (NoSuchFieldError unused) {
            }
            $SwitchMap$com$treadly$client$lib$sdk$Model$PairingModeTriggerType = new int[PairingModeTriggerType.values().length];
            try {
                $SwitchMap$com$treadly$client$lib$sdk$Model$PairingModeTriggerType[PairingModeTriggerType.ir.ordinal()] = 1;
            } catch (NoSuchFieldError unused2) {
            }
            try {
                $SwitchMap$com$treadly$client$lib$sdk$Model$PairingModeTriggerType[PairingModeTriggerType.handrail.ordinal()] = 2;
            } catch (NoSuchFieldError unused3) {
            }
        }
    }

    public static /* synthetic */ boolean lambda$initList$2(TreadlyProfileSettingsAboutFragment treadlyProfileSettingsAboutFragment, ExpandableListView expandableListView, View view, int i, int i2, long j) {
        if (AnonymousClass3.$SwitchMap$com$treadly$Treadly$UI$TreadlyProfile$Settings$About$TreadlyProfileSettingsAboutFragment$AboutViewSections[treadlyProfileSettingsAboutFragment.adapter.sectionTypes.get(i).ordinal()] == 1 && i2 == 3) {
            treadlyProfileSettingsAboutFragment.toAdminSettings();
            return true;
        }
        return false;
    }

    @Override // androidx.fragment.app.Fragment
    public void onStart() {
        super.onStart();
        if (this.adapter != null) {
            this.adapter.notifyDataSetChanged();
        }
    }

    @Override // androidx.fragment.app.Fragment
    public void onStop() {
        super.onStop();
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void updateSections() {
        this.sectionTypes.clear();
        if (this.components == null) {
            return;
        }
        if (isAdminMode() && TreadlyClientLib.shared.isDeviceConnected() && this.components.size() > 0) {
            String handrailPairingString = getHandrailPairingString();
            if (handrailPairingString.equals(AppEventsConstants.EVENT_PARAM_VALUE_YES) || handrailPairingString.equals(AppEventsConstants.EVENT_PARAM_VALUE_NO)) {
                this.sectionTypes.add(AboutViewSections.qrCode);
            }
        }
        this.sectionTypes.add(AboutViewSections.appInfo);
        ArrayList arrayList = new ArrayList();
        if (this.components != null && this.components.size() > 0) {
            this.sectionTypes.add(AboutViewSections.deviceInfo);
            for (ComponentInfo componentInfo : this.components) {
                if (componentInfo != null) {
                    if (componentInfo.getType() == ComponentType.motorController && !componentInfo.getVersionInfo().isEqual(new VersionInfo(0, 0, 0))) {
                        this.sectionTypes.add(AboutViewSections.deviceInfo);
                    }
                    arrayList.add(componentInfo);
                }
            }
        }
        if (this.adapter != null) {
            this.adapter.sectionTypes = this.sectionTypes;
            this.adapter.components = arrayList;
        }
    }

    private void toAdminSettings() {
        if (this.fragmentListener != null) {
            this.fragmentListener.adminSettingsSelected();
        }
    }

    public boolean isAdminMode() {
        MainActivity mainActivity = (MainActivity) getActivity();
        if (mainActivity != null) {
            return mainActivity.adminMode;
        }
        return false;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public String getMainboardVersion() {
        for (ComponentInfo componentInfo : this.components) {
            if (componentInfo != null && componentInfo.getType() == ComponentType.mainBoard) {
                return componentInfo.getVersionInfo().getVersion();
            }
        }
        return "";
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public String getDeviceAddress() {
        for (ComponentInfo componentInfo : this.components) {
            if (componentInfo != null && componentInfo.getType() == ComponentType.bleBoard) {
                return componentInfo.getId().toUpperCase();
            }
        }
        return "";
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public String getDeviceVersion() {
        for (ComponentInfo componentInfo : this.components) {
            if (componentInfo != null && componentInfo.getType() == ComponentType.bleBoard) {
                return componentInfo.getVersionInfo().getVersion();
            }
        }
        return "";
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public String getSerialNumber() {
        for (ComponentInfo componentInfo : this.components) {
            if (componentInfo != null && componentInfo.getType() == ComponentType.bleBoard) {
                return String.format(Locale.getDefault(), "%08d", Long.valueOf(componentInfo.getSerialNumber()));
            }
        }
        return "";
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public String getIdleStatus() {
        return this.isPauseAfterIdleEnabled ? PeerConnectionFactory.TRIAL_ENABLED : "Disabled";
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public String getHandrailPairingString() {
        if (this.handrailStatus == null) {
            return "";
        }
        switch (this.handrailStatus) {
            case ir:
                return AppEventsConstants.EVENT_PARAM_VALUE_NO;
            case handrail:
                return AppEventsConstants.EVENT_PARAM_VALUE_YES;
            default:
                return "";
        }
    }
}
