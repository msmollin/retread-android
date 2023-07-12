package com.treadly.Treadly.UI.Onboarding.Searching;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.airbnb.lottie.LottieAnimationView;
import com.treadly.Treadly.R;
import com.treadly.Treadly.UI.Onboarding.InviteCode.TreadlyOnboardingInviteCodeFragment;
import com.treadly.Treadly.UI.Onboarding.NotFound.TreadlyOnboardingNotFoundFragment;
import com.treadly.Treadly.UI.TreadlyConnect.DeviceConnect.TreadlyDeviceConnectStatusFragment;
import com.treadly.Treadly.UI.Util.ActivityUtil;
import com.treadly.Treadly.UI.Util.BaseClasses.BaseFragment;
import com.treadly.client.lib.sdk.Listeners.DeviceConnectionEventAdapter;
import com.treadly.client.lib.sdk.Listeners.DeviceConnectionEventListener;
import com.treadly.client.lib.sdk.Model.BluetoothConnectionState;
import com.treadly.client.lib.sdk.Model.DeviceConnectionEvent;
import com.treadly.client.lib.sdk.Model.DeviceConnectionStatus;
import com.treadly.client.lib.sdk.Model.DeviceInfo;
import com.treadly.client.lib.sdk.TreadlyClientLib;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

/* loaded from: classes2.dex */
public class TreadlyOnboardingSearchingFragment extends BaseFragment {
    public static final String TAG = "ONBOARDING_SEARCHING";
    List<DeviceInfo> deviceList;
    Timer initScanTimer;
    LottieAnimationView searchingAnimation;
    final int INIT_SCAN_LIMIT = 100;
    final long INIT_SCAN_TIMEOUT = 100;
    final long SCAN_TIMEOUT = 10000;
    boolean leavingPage = false;
    int initScanAttempt = 0;
    boolean isScanning = false;
    boolean showInviteButton = true;
    boolean stopNextScan = false;
    DeviceConnectionEventListener deviceConnectionListener = new DeviceConnectionEventAdapter() { // from class: com.treadly.Treadly.UI.Onboarding.Searching.TreadlyOnboardingSearchingFragment.2
        @Override // com.treadly.client.lib.sdk.Listeners.DeviceConnectionEventAdapter, com.treadly.client.lib.sdk.Listeners.DeviceConnectionEventListener
        public void onDeviceConnectionChanged(DeviceConnectionEvent deviceConnectionEvent) {
            super.onDeviceConnectionChanged(deviceConnectionEvent);
            TreadlyClientLib.shared.removeDeviceConnectionEventListener(TreadlyOnboardingSearchingFragment.this.deviceConnectionListener);
            if (TreadlyOnboardingSearchingFragment.this.isScanning && deviceConnectionEvent.getStatus() == DeviceConnectionStatus.notConnected) {
                TreadlyOnboardingSearchingFragment.this.handleNoDeviceConnectionResponse();
            }
        }

        @Override // com.treadly.client.lib.sdk.Listeners.DeviceConnectionEventAdapter, com.treadly.client.lib.sdk.Listeners.DeviceConnectionEventListener
        public void onDeviceConnectionDeviceDiscovered(DeviceInfo deviceInfo) {
            super.onDeviceConnectionDeviceDiscovered(deviceInfo);
            if (TreadlyOnboardingSearchingFragment.this.leavingPage) {
                return;
            }
            TreadlyOnboardingSearchingFragment.this.leavingPage = true;
            TreadlyClientLib.shared.removeDeviceConnectionEventListener(TreadlyOnboardingSearchingFragment.this.deviceConnectionListener);
            TreadlyOnboardingSearchingFragment.this.toConnectStatus(deviceInfo);
        }
    };

    @Override // androidx.fragment.app.Fragment
    public void onCreate(@Nullable Bundle bundle) {
        super.onCreate(bundle);
        addBackStackCallback();
        this.deviceList = new ArrayList();
    }

    @Override // androidx.fragment.app.Fragment
    @Nullable
    public View onCreateView(@NonNull LayoutInflater layoutInflater, @Nullable ViewGroup viewGroup, @Nullable Bundle bundle) {
        return layoutInflater.inflate(R.layout.fragment_treadly_onboarding_searching, viewGroup, false);
    }

    @Override // com.treadly.Treadly.UI.Util.BaseClasses.BaseFragment, androidx.fragment.app.Fragment
    public void onViewCreated(@NonNull View view, @Nullable Bundle bundle) {
        super.onViewCreated(view, bundle);
        ((ImageButton) view.findViewById(R.id.onboarding_searching_dismiss_button)).setOnClickListener(new View.OnClickListener() { // from class: com.treadly.Treadly.UI.Onboarding.Searching.-$$Lambda$TreadlyOnboardingSearchingFragment$hMeAPjOdSsFUpXGPY3evbZ9pNH4
            @Override // android.view.View.OnClickListener
            public final void onClick(View view2) {
                TreadlyOnboardingSearchingFragment.this.popBackStack();
            }
        });
        this.searchingAnimation = (LottieAnimationView) view.findViewById(R.id.onboarding_searching_animation);
    }

    @Override // com.treadly.Treadly.UI.Util.BaseClasses.BaseFragment
    public void onFragmentReturning() {
        super.onFragmentReturning();
        hideBottomNavigation();
        this.searchingAnimation.playAnimation();
        initScanning();
        this.leavingPage = false;
    }

    @Override // androidx.fragment.app.Fragment
    public void onStop() {
        super.onStop();
        TreadlyClientLib.shared.removeDeviceConnectionEventListener(this.deviceConnectionListener);
        this.searchingAnimation.pauseAnimation();
        stopInitScanTimer();
        this.leavingPage = false;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void initScanning() {
        initScanning(true);
    }

    void initScanning(boolean z) {
        stopInitScanTimer();
        if (z) {
            this.initScanAttempt = 0;
        }
        if (TreadlyClientLib.shared.getBluetoothConnectionSTate() == BluetoothConnectionState.poweredOn) {
            rescanDevices();
            return;
        }
        this.initScanTimer = new Timer();
        this.initScanTimer.schedule(new TimerTask() { // from class: com.treadly.Treadly.UI.Onboarding.Searching.TreadlyOnboardingSearchingFragment.1
            @Override // java.util.TimerTask, java.lang.Runnable
            public void run() {
                if (TreadlyOnboardingSearchingFragment.this.initScanAttempt < 100) {
                    TreadlyOnboardingSearchingFragment.this.initScanAttempt++;
                    TreadlyOnboardingSearchingFragment.this.initScanning(false);
                    return;
                }
                TreadlyOnboardingSearchingFragment.this.handleNoDeviceConnectionResponse();
            }
        }, 100L);
    }

    void stopInitScanTimer() {
        if (this.initScanTimer != null) {
            this.initScanTimer.cancel();
        }
        this.initScanTimer = null;
    }

    void rescanDevices() {
        if (this.stopNextScan) {
            this.stopNextScan = false;
            return;
        }
        this.deviceList.clear();
        TreadlyClientLib.shared.addDeviceConnectionEventListener(this.deviceConnectionListener);
        TreadlyClientLib.shared.startScanning(10000L, false);
        this.isScanning = true;
    }

    void handleNoDeviceConnectionResponse() {
        this.isScanning = false;
        TreadlyOnboardingNotFoundFragment treadlyOnboardingNotFoundFragment = new TreadlyOnboardingNotFoundFragment();
        treadlyOnboardingNotFoundFragment.tryAgainListener = new TreadlyOnboardingNotFoundFragment.TryAgainListener() { // from class: com.treadly.Treadly.UI.Onboarding.Searching.-$$Lambda$Gl6qOJ7RAmUSBscfpRAU66sQdOs
            @Override // com.treadly.Treadly.UI.Onboarding.NotFound.TreadlyOnboardingNotFoundFragment.TryAgainListener
            public final void onTryAgainPressed() {
                TreadlyOnboardingSearchingFragment.this.initScanning();
            }
        };
        treadlyOnboardingNotFoundFragment.inviteCodeListener = new TreadlyOnboardingNotFoundFragment.InviteCodeListener() { // from class: com.treadly.Treadly.UI.Onboarding.Searching.-$$Lambda$MPq60M63QRVr7-nuGs4J59bO2A8
            @Override // com.treadly.Treadly.UI.Onboarding.NotFound.TreadlyOnboardingNotFoundFragment.InviteCodeListener
            public final void onInviteCodePressed() {
                TreadlyOnboardingSearchingFragment.this.toInviteCode();
            }
        };
        treadlyOnboardingNotFoundFragment.showInviteButton = this.showInviteButton;
        addFragmentToStack(treadlyOnboardingNotFoundFragment, TreadlyOnboardingNotFoundFragment.TAG, TAG, true);
    }

    void toConnectStatus(DeviceInfo deviceInfo) {
        TreadlyDeviceConnectStatusFragment treadlyDeviceConnectStatusFragment = new TreadlyDeviceConnectStatusFragment();
        treadlyDeviceConnectStatusFragment.isOnboarding = true;
        treadlyDeviceConnectStatusFragment.foundOnboardDevice = deviceInfo;
        treadlyDeviceConnectStatusFragment.onboardingDeviceFound = true;
        addFragmentToStack(treadlyDeviceConnectStatusFragment, TreadlyDeviceConnectStatusFragment.TAG, TAG, true);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void toInviteCode() {
        this.stopNextScan = true;
        ActivityUtil.runOnUiThread(getActivity(), new Runnable() { // from class: com.treadly.Treadly.UI.Onboarding.Searching.-$$Lambda$TreadlyOnboardingSearchingFragment$g85fU2Srmkaqir8MH_htP0DHAHs
            @Override // java.lang.Runnable
            public final void run() {
                TreadlyOnboardingSearchingFragment.lambda$toInviteCode$2(TreadlyOnboardingSearchingFragment.this);
            }
        });
    }

    public static /* synthetic */ void lambda$toInviteCode$2(final TreadlyOnboardingSearchingFragment treadlyOnboardingSearchingFragment) {
        treadlyOnboardingSearchingFragment.stopInitScanTimer();
        TreadlyOnboardingInviteCodeFragment treadlyOnboardingInviteCodeFragment = new TreadlyOnboardingInviteCodeFragment();
        treadlyOnboardingInviteCodeFragment.didFailAttempts = new TreadlyOnboardingInviteCodeFragment.didFailAttemptsListener() { // from class: com.treadly.Treadly.UI.Onboarding.Searching.-$$Lambda$TreadlyOnboardingSearchingFragment$Wdp26by3sW7I7gkx5sQbwYBE8AQ
            @Override // com.treadly.Treadly.UI.Onboarding.InviteCode.TreadlyOnboardingInviteCodeFragment.didFailAttemptsListener
            public final void onResponse() {
                TreadlyOnboardingSearchingFragment.this.showInviteButton = false;
            }
        };
        treadlyOnboardingSearchingFragment.addFragmentToStack(treadlyOnboardingInviteCodeFragment, TreadlyOnboardingInviteCodeFragment.TAG, TAG, true);
    }
}
