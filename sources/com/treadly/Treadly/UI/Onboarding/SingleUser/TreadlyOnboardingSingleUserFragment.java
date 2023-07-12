package com.treadly.Treadly.UI.Onboarding.SingleUser;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.res.ResourcesCompat;
import com.treadly.Treadly.Data.Listeners.TreadlyServiceResponseEventAdapter;
import com.treadly.Treadly.Data.Managers.DeviceUserStatsLogManager;
import com.treadly.Treadly.Data.Managers.TreadlyServiceManager;
import com.treadly.Treadly.Data.Model.UserInfo;
import com.treadly.Treadly.MainActivity;
import com.treadly.Treadly.R;
import com.treadly.Treadly.UI.TreadlyProfile.Settings.WifiSetup.TreadlyProfileSettingsWifiSetupFragment;
import com.treadly.Treadly.UI.Util.BaseClasses.BaseFragment;
import com.treadly.client.lib.sdk.Model.DeviceInfo;

/* loaded from: classes2.dex */
public class TreadlyOnboardingSingleUserFragment extends BaseFragment {
    public static final String TAG = "ONBOARDING_SINGLE_USER";
    public DeviceInfo connectedDeviceInfo;
    private ImageButton multiUserButton;
    public UserInfo ownerInfo;
    private ImageButton singleUserButton;
    public UserInfo user;
    public boolean isEnabled = false;
    public boolean singleUserSelected = false;
    public String deviceAddress = null;
    public boolean onboarding = false;
    public boolean hasWifiConfiguration = false;
    private boolean pendingChanges = false;

    private void lockToggle(UserInfo userInfo) {
    }

    @Override // androidx.fragment.app.Fragment
    @Nullable
    public View onCreateView(@NonNull LayoutInflater layoutInflater, @Nullable ViewGroup viewGroup, @Nullable Bundle bundle) {
        hideBottomNavigation(true);
        return layoutInflater.inflate(R.layout.fragment_treadly_onboarding_single_user, viewGroup, false);
    }

    @Override // com.treadly.Treadly.UI.Util.BaseClasses.BaseFragment, androidx.fragment.app.Fragment
    public void onViewCreated(@NonNull View view, @Nullable Bundle bundle) {
        super.onViewCreated(view, bundle);
        this.singleUserButton = (ImageButton) view.findViewById(R.id.onboarding_single_user_single_user_button);
        this.singleUserButton.setOnClickListener(new View.OnClickListener() { // from class: com.treadly.Treadly.UI.Onboarding.SingleUser.-$$Lambda$TreadlyOnboardingSingleUserFragment$4lh6epGwApJ0__P3PNebDPXYLjE
            @Override // android.view.View.OnClickListener
            public final void onClick(View view2) {
                TreadlyOnboardingSingleUserFragment.this.singleUserClicked(view2);
            }
        });
        this.multiUserButton = (ImageButton) view.findViewById(R.id.onboarding_single_user_multi_user_button);
        this.multiUserButton.setOnClickListener(new View.OnClickListener() { // from class: com.treadly.Treadly.UI.Onboarding.SingleUser.-$$Lambda$TreadlyOnboardingSingleUserFragment$pYohEf059WyOGHpvcHNC8tJ8vZ4
            @Override // android.view.View.OnClickListener
            public final void onClick(View view2) {
                TreadlyOnboardingSingleUserFragment.this.multiUserClicked(view2);
            }
        });
        ((Button) view.findViewById(R.id.onboarding_single_user_continue_button)).setOnClickListener(new View.OnClickListener() { // from class: com.treadly.Treadly.UI.Onboarding.SingleUser.-$$Lambda$TreadlyOnboardingSingleUserFragment$rrHCbPHdrwzQgMQ1WFZ3budMa4g
            @Override // android.view.View.OnClickListener
            public final void onClick(View view2) {
                TreadlyOnboardingSingleUserFragment.this.continueClicked(view2);
            }
        });
        ((ImageButton) view.findViewById(R.id.onboarding_single_user_back_button)).setOnClickListener(new View.OnClickListener() { // from class: com.treadly.Treadly.UI.Onboarding.SingleUser.-$$Lambda$TreadlyOnboardingSingleUserFragment$teIDMuxdsgioHFFlOQNub_iDEmo
            @Override // android.view.View.OnClickListener
            public final void onClick(View view2) {
                TreadlyOnboardingSingleUserFragment.this.backClicked(view2);
            }
        });
        if (this.isEnabled) {
            if (this.singleUserSelected) {
                setSingleUser();
            } else {
                setMultiUser();
            }
        }
    }

    @Override // com.treadly.Treadly.UI.Util.BaseClasses.BaseFragment, androidx.fragment.app.Fragment
    public void onDetach() {
        super.onDetach();
        dismissLoading();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void singleUserClicked(View view) {
        toggle(true);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void multiUserClicked(View view) {
        toggle(false);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void continueClicked(View view) {
        if (this.deviceAddress == null) {
            return;
        }
        if (this.singleUserSelected) {
            TreadlyServiceManager.getInstance().addSingleUserMode(this.deviceAddress, new TreadlyServiceResponseEventAdapter() { // from class: com.treadly.Treadly.UI.Onboarding.SingleUser.TreadlyOnboardingSingleUserFragment.1
                @Override // com.treadly.Treadly.Data.Listeners.TreadlyServiceResponseEventAdapter, com.treadly.Treadly.Data.Listeners.TreadlyServiceResponseEventListener
                public void onSuccess(String str) {
                    super.onSuccess(str);
                    if (str == null) {
                        TreadlyOnboardingSingleUserFragment.this.handleSuccessfulSwitch(true);
                    } else {
                        TreadlyOnboardingSingleUserFragment.this.handleUnsuccessfulSwitch(true);
                    }
                }
            });
        } else {
            TreadlyServiceManager.getInstance().removeSingleUserMode(this.deviceAddress, new TreadlyServiceResponseEventAdapter() { // from class: com.treadly.Treadly.UI.Onboarding.SingleUser.TreadlyOnboardingSingleUserFragment.2
                @Override // com.treadly.Treadly.Data.Listeners.TreadlyServiceResponseEventAdapter, com.treadly.Treadly.Data.Listeners.TreadlyServiceResponseEventListener
                public void onSuccess(String str) {
                    super.onSuccess(str);
                    if (str == null) {
                        TreadlyOnboardingSingleUserFragment.this.handleSuccessfulSwitch(false);
                    } else {
                        TreadlyOnboardingSingleUserFragment.this.handleUnsuccessfulSwitch(false);
                    }
                }
            });
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void backClicked(View view) {
        popBackStack();
    }

    private void toggle(boolean z) {
        if (z == this.singleUserSelected) {
            return;
        }
        this.singleUserSelected = z;
        if (z) {
            setSingleUser();
        } else {
            setMultiUser();
        }
    }

    private void setSingleUser() {
        this.singleUserButton.setColorFilter(ResourcesCompat.getColor(getResources(), R.color.white, null));
        this.multiUserButton.setColorFilter(ResourcesCompat.getColor(getResources(), R.color.dark_2, null));
    }

    private void setMultiUser() {
        this.singleUserButton.setColorFilter(ResourcesCompat.getColor(getResources(), R.color.dark_2, null));
        this.multiUserButton.setColorFilter(ResourcesCompat.getColor(getResources(), R.color.white, null));
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void handleSuccessfulSwitch(boolean z) {
        DeviceUserStatsLogManager.isSingleUser = z;
        if (this.onboarding && this.hasWifiConfiguration) {
            toWifiSetup();
        } else if (this.onboarding) {
            finishOnboarding();
        } else {
            popBackStack();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void handleUnsuccessfulSwitch(boolean z) {
        String string;
        if (z) {
            string = getString(R.string.onboard_single_user_enable_error);
        } else {
            string = getString(R.string.onboard_single_user_disable_error);
        }
        showBaseAlert("Error", string);
    }

    private void toWifiSetup() {
        TreadlyProfileSettingsWifiSetupFragment treadlyProfileSettingsWifiSetupFragment = new TreadlyProfileSettingsWifiSetupFragment();
        treadlyProfileSettingsWifiSetupFragment.isOnboarding = true;
        addFragmentToStack(treadlyProfileSettingsWifiSetupFragment, TreadlyProfileSettingsWifiSetupFragment.TAG, TAG, true);
    }

    private void finishOnboarding() {
        if (this.pendingChanges) {
            return;
        }
        this.pendingChanges = true;
        TreadlyServiceManager.getInstance().updateOnboardingStatus(new TreadlyServiceResponseEventAdapter() { // from class: com.treadly.Treadly.UI.Onboarding.SingleUser.TreadlyOnboardingSingleUserFragment.3
            @Override // com.treadly.Treadly.Data.Listeners.TreadlyServiceResponseEventAdapter, com.treadly.Treadly.Data.Listeners.TreadlyServiceResponseEventListener
            public void onSuccess(String str) {
                super.onSuccess(str);
                MainActivity mainActivity = (MainActivity) TreadlyOnboardingSingleUserFragment.this.getActivity();
                if (str != null || mainActivity == null) {
                    TreadlyOnboardingSingleUserFragment.this.pendingChanges = false;
                    TreadlyOnboardingSingleUserFragment.this.showBaseAlert("Ooops!", "There was an error. Please try again.");
                    return;
                }
                TreadlyOnboardingSingleUserFragment.this.clearBackStack();
                mainActivity.toConnect();
            }
        });
    }
}
