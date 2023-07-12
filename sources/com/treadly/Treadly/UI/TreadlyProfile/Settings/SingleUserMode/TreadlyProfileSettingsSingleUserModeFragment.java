package com.treadly.Treadly.UI.TreadlyProfile.Settings.SingleUserMode;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.Switch;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.treadly.Treadly.Data.Listeners.TreadlyServiceResponseEventAdapter;
import com.treadly.Treadly.Data.Managers.DeviceUserStatsLogManager;
import com.treadly.Treadly.Data.Managers.TreadlyServiceManager;
import com.treadly.Treadly.Data.Model.UserInfo;
import com.treadly.Treadly.Data.Model.UserProfileInfo;
import com.treadly.Treadly.MainActivity;
import com.treadly.Treadly.R;
import com.treadly.Treadly.UI.TreadlyProfile.Settings.SingleUserMode.TreadlyProfileSettingsSingleUserModeFragment;
import com.treadly.Treadly.UI.Util.ActivityUtil;
import com.treadly.Treadly.UI.Util.BaseClasses.BaseFragment;

/* loaded from: classes2.dex */
public class TreadlyProfileSettingsSingleUserModeFragment extends BaseFragment {
    public String deviceAddress;
    private Switch singleUserModeSwitch;
    private UserProfileInfo profile = null;
    private boolean multiUser = true;
    private String userId = TreadlyServiceManager.getInstance().getUserId();

    /* JADX INFO: Access modifiers changed from: package-private */
    public static /* synthetic */ void lambda$lockToggle$2(View view) {
    }

    @Override // androidx.fragment.app.Fragment
    @Nullable
    public View onCreateView(@NonNull LayoutInflater layoutInflater, @Nullable ViewGroup viewGroup, @Nullable Bundle bundle) {
        MainActivity mainActivity = (MainActivity) getActivity();
        if (mainActivity != null) {
            mainActivity.hideBottomNavigationView();
        }
        return layoutInflater.inflate(R.layout.fragment_treadly_profile_settings_single_user_mode, viewGroup, false);
    }

    @Override // com.treadly.Treadly.UI.Util.BaseClasses.BaseFragment, androidx.fragment.app.Fragment
    public void onViewCreated(@NonNull View view, @Nullable Bundle bundle) {
        super.onViewCreated(view, bundle);
        ((TextView) view.findViewById(R.id.nav_title)).setText(R.string.single_user_mode_title);
        this.singleUserModeSwitch = (Switch) view.findViewById(R.id.set_single_user_mode_switch);
        this.singleUserModeSwitch.setOnClickListener(new View.OnClickListener() { // from class: com.treadly.Treadly.UI.TreadlyProfile.Settings.SingleUserMode.-$$Lambda$TreadlyProfileSettingsSingleUserModeFragment$Hyz1CfpDbx5mfdfVKW_ltnwXB9w
            @Override // android.view.View.OnClickListener
            public final void onClick(View view2) {
                TreadlyProfileSettingsSingleUserModeFragment.this.handleSwitchToggled();
            }
        });
        ((ImageButton) view.findViewById(R.id.nav_back_arrow)).setOnClickListener(new View.OnClickListener() { // from class: com.treadly.Treadly.UI.TreadlyProfile.Settings.SingleUserMode.-$$Lambda$TreadlyProfileSettingsSingleUserModeFragment$6U2aASsrzwyRN3oJr54PuREn4qU
            @Override // android.view.View.OnClickListener
            public final void onClick(View view2) {
                TreadlyProfileSettingsSingleUserModeFragment.this.popBackStack();
            }
        });
        getProfile();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* renamed from: com.treadly.Treadly.UI.TreadlyProfile.Settings.SingleUserMode.TreadlyProfileSettingsSingleUserModeFragment$1  reason: invalid class name */
    /* loaded from: classes2.dex */
    public class AnonymousClass1 extends TreadlyServiceResponseEventAdapter {
        AnonymousClass1() {
        }

        @Override // com.treadly.Treadly.Data.Listeners.TreadlyServiceResponseEventAdapter, com.treadly.Treadly.Data.Listeners.TreadlyServiceResponseEventListener
        public void onSuccess(String str) {
            if (str == null) {
                TreadlyProfileSettingsSingleUserModeFragment.this.multiUser = true;
                TreadlyProfileSettingsSingleUserModeFragment.this.singleUserModeSwitch.setChecked(TreadlyProfileSettingsSingleUserModeFragment.this.multiUser);
                ActivityUtil.runOnUiThread(TreadlyProfileSettingsSingleUserModeFragment.this.getActivity(), new Runnable() { // from class: com.treadly.Treadly.UI.TreadlyProfile.Settings.SingleUserMode.-$$Lambda$TreadlyProfileSettingsSingleUserModeFragment$1$PvQjXlDtoO3KzJ9IRMJ_qijFqH4
                    @Override // java.lang.Runnable
                    public final void run() {
                        TreadlyProfileSettingsSingleUserModeFragment.AnonymousClass1 anonymousClass1 = TreadlyProfileSettingsSingleUserModeFragment.AnonymousClass1.this;
                        DeviceUserStatsLogManager.isSingleUser = !TreadlyProfileSettingsSingleUserModeFragment.this.multiUser;
                    }
                });
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void handleSwitchToggled() {
        if (!this.multiUser) {
            TreadlyServiceManager.getInstance().removeSingleUserMode(this.deviceAddress, new AnonymousClass1());
        } else {
            TreadlyServiceManager.getInstance().addSingleUserMode(this.deviceAddress, new AnonymousClass2());
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* renamed from: com.treadly.Treadly.UI.TreadlyProfile.Settings.SingleUserMode.TreadlyProfileSettingsSingleUserModeFragment$2  reason: invalid class name */
    /* loaded from: classes2.dex */
    public class AnonymousClass2 extends TreadlyServiceResponseEventAdapter {
        AnonymousClass2() {
        }

        @Override // com.treadly.Treadly.Data.Listeners.TreadlyServiceResponseEventAdapter, com.treadly.Treadly.Data.Listeners.TreadlyServiceResponseEventListener
        public void onSuccess(String str) {
            if (str == null) {
                TreadlyProfileSettingsSingleUserModeFragment.this.multiUser = false;
                TreadlyProfileSettingsSingleUserModeFragment.this.singleUserModeSwitch.setChecked(TreadlyProfileSettingsSingleUserModeFragment.this.multiUser);
                ActivityUtil.runOnUiThread(TreadlyProfileSettingsSingleUserModeFragment.this.getActivity(), new Runnable() { // from class: com.treadly.Treadly.UI.TreadlyProfile.Settings.SingleUserMode.-$$Lambda$TreadlyProfileSettingsSingleUserModeFragment$2$JYCpn88HkDUMZESBsc1djBSHFwM
                    @Override // java.lang.Runnable
                    public final void run() {
                        TreadlyProfileSettingsSingleUserModeFragment.AnonymousClass2 anonymousClass2 = TreadlyProfileSettingsSingleUserModeFragment.AnonymousClass2.this;
                        DeviceUserStatsLogManager.isSingleUser = !TreadlyProfileSettingsSingleUserModeFragment.this.multiUser;
                    }
                });
            }
        }
    }

    void getProfile() {
        showLoading();
        TreadlyServiceManager.getInstance().getSingleUserMode(this.deviceAddress, new TreadlyServiceResponseEventAdapter() { // from class: com.treadly.Treadly.UI.TreadlyProfile.Settings.SingleUserMode.TreadlyProfileSettingsSingleUserModeFragment.3
            @Override // com.treadly.Treadly.Data.Listeners.TreadlyServiceResponseEventAdapter, com.treadly.Treadly.Data.Listeners.TreadlyServiceResponseEventListener
            public void onUserInfo(String str, UserInfo userInfo) {
                if (str != null) {
                    TreadlyProfileSettingsSingleUserModeFragment.this.multiUser = true;
                    TreadlyProfileSettingsSingleUserModeFragment.this.updateView();
                    return;
                }
                if (userInfo.id.equals(TreadlyServiceManager.getInstance().getUserId())) {
                    TreadlyProfileSettingsSingleUserModeFragment.this.multiUser = false;
                    TreadlyProfileSettingsSingleUserModeFragment.this.updateView();
                    return;
                }
                TreadlyProfileSettingsSingleUserModeFragment.this.lockToggle(userInfo);
            }
        });
    }

    void updateView() {
        dismissLoading();
        this.singleUserModeSwitch.setChecked(this.multiUser);
    }

    void lockToggle(UserInfo userInfo) {
        this.singleUserModeSwitch.setChecked(false);
        this.singleUserModeSwitch.setOnClickListener(new View.OnClickListener() { // from class: com.treadly.Treadly.UI.TreadlyProfile.Settings.SingleUserMode.-$$Lambda$TreadlyProfileSettingsSingleUserModeFragment$y28O1yz38nH6aC7OS048yZk_5HU
            @Override // android.view.View.OnClickListener
            public final void onClick(View view) {
                TreadlyProfileSettingsSingleUserModeFragment.lambda$lockToggle$2(view);
            }
        });
        this.singleUserModeSwitch.setClickable(false);
        this.singleUserModeSwitch.setText(String.format("%s has set single user mode. Please contact them to put it in multi-user mode", userInfo.name));
        dismissLoading();
    }
}
