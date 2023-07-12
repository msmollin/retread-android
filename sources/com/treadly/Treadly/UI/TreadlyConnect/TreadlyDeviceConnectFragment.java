package com.treadly.Treadly.UI.TreadlyConnect;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentActivity;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import com.treadly.Treadly.Data.Listeners.TreadlyServiceResponseEventAdapter;
import com.treadly.Treadly.Data.Managers.AppActivityManager;
import com.treadly.Treadly.Data.Managers.TreadlyActivationManager;
import com.treadly.Treadly.Data.Managers.TreadlyServiceManager;
import com.treadly.Treadly.Data.Model.UserInfo;
import com.treadly.Treadly.MainActivity;
import com.treadly.Treadly.R;
import com.treadly.Treadly.UI.Onboarding.SingleUser.TreadlyOnboardingSingleUserFragment;
import com.treadly.Treadly.UI.TreadlyConnect.TreadlyDeviceConnectFragment;
import com.treadly.Treadly.UI.TreadlyConnect.TreadlyDeviceConnectListAdapter;
import com.treadly.Treadly.UI.Util.ActivityUtil;
import com.treadly.Treadly.UI.Util.BaseClasses.BaseFragment;
import com.treadly.Treadly.UI.Util.OnBackPressedListener;
import com.treadly.Treadly.UI.Util.SharedPreferences;
import com.treadly.client.lib.sdk.Listeners.DeviceActivationEventListener;
import com.treadly.client.lib.sdk.Listeners.DeviceConnectionEventListener;
import com.treadly.client.lib.sdk.Listeners.RequestEventAdapter;
import com.treadly.client.lib.sdk.Listeners.RequestEventListener;
import com.treadly.client.lib.sdk.Managers.ActivationManager;
import com.treadly.client.lib.sdk.Model.ActivationInfo;
import com.treadly.client.lib.sdk.Model.AuthenticateReferenceCodeInfo;
import com.treadly.client.lib.sdk.Model.AuthenticationState;
import com.treadly.client.lib.sdk.Model.ComponentInfo;
import com.treadly.client.lib.sdk.Model.ComponentType;
import com.treadly.client.lib.sdk.Model.DeviceConnectionEvent;
import com.treadly.client.lib.sdk.Model.DeviceInfo;
import com.treadly.client.lib.sdk.Model.VersionInfo;
import com.treadly.client.lib.sdk.TreadlyClientLib;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Timer;
import java.util.TimerTask;
import java.util.function.Consumer;

/* loaded from: classes2.dex */
public class TreadlyDeviceConnectFragment extends BaseFragment implements DeviceConnectionEventListener {
    private static int SCAN_TIMEOUT = 30000;
    public static final String TAG = "CONNECT_DEVICE_LIST";
    private TreadlyDeviceConnectListAdapter adapter;
    private ImageView backArrowButton;
    private ComponentInfo bleComonent;
    private DeviceInfo connectingDevice;
    private String deviceAddress;
    private ListAdapter deviceListAdapter;
    private ListView deviceListView;
    private String deviceName;
    private boolean isScanning;
    public TreadlyDeviceConnectConnectFragmentEventListener listener;
    private TextView navigationTitle;
    private DeviceInfo onboardDeviceInfo;
    private Button scanButton;
    private Button skipButton;
    SwipeRefreshLayout swipeRefreshLayout;
    public boolean isOnboarding = false;
    private String targetConnectDeviceName = null;
    private boolean isConnecting = false;
    private TreadlyDeviceConnectListAdapter.ConnectDeviceEventListener adapterListener = new TreadlyDeviceConnectListAdapter.ConnectDeviceEventListener() { // from class: com.treadly.Treadly.UI.TreadlyConnect.TreadlyDeviceConnectFragment.1
        @Override // com.treadly.Treadly.UI.TreadlyConnect.TreadlyDeviceConnectListAdapter.ConnectDeviceEventListener
        public void connectDeviceSelected(DeviceInfo deviceInfo) {
            TreadlyDeviceConnectFragment.this.connectDevice(deviceInfo);
        }
    };
    private boolean registerChecked = false;
    private boolean hasSingleUserMode = false;
    private boolean hasWifiConfiguration = false;
    private Timer onboardingContinueTimer = null;
    private boolean pendingChanges = false;
    DeviceActivationEventListener deviceActivationListener = new DeviceActivationEventListener() { // from class: com.treadly.Treadly.UI.TreadlyConnect.TreadlyDeviceConnectFragment.7
        @Override // com.treadly.client.lib.sdk.Listeners.DeviceActivationEventListener
        public void onDeviceActivationStartResponse(ActivationInfo activationInfo) {
        }

        @Override // com.treadly.client.lib.sdk.Listeners.DeviceActivationEventListener
        public void onDeviceActivationSubmitResponse(boolean z) {
        }

        @Override // com.treadly.client.lib.sdk.Listeners.DeviceActivationEventListener
        public void onDeviceAuthenticateReferenceCodeInfoResponse(boolean z, AuthenticateReferenceCodeInfo authenticateReferenceCodeInfo) {
        }

        @Override // com.treadly.client.lib.sdk.Listeners.DeviceActivationEventListener
        public void onDeviceAuthenticationStateResponse(AuthenticationState authenticationState) {
        }
    };
    RequestEventListener deviceRequestListener = new AnonymousClass8();

    /* loaded from: classes2.dex */
    public interface TreadlyDeviceConnectConnectFragmentEventListener {
        void onConnectDevice(DeviceInfo deviceInfo);
    }

    @Override // androidx.fragment.app.Fragment
    @Nullable
    public View onCreateView(@NonNull LayoutInflater layoutInflater, @Nullable ViewGroup viewGroup, @Nullable Bundle bundle) {
        final MainActivity mainActivity = (MainActivity) getActivity();
        mainActivity.blockBottomNavigation = true;
        mainActivity.setOnBackPressedListener(new OnBackPressedListener() { // from class: com.treadly.Treadly.UI.TreadlyConnect.-$$Lambda$TreadlyDeviceConnectFragment$ku77_obL7nj_wx79yKYO3KVh82k
            @Override // com.treadly.Treadly.UI.Util.OnBackPressedListener
            public final void backAction() {
                MainActivity.this.getSupportFragmentManager().popBackStack();
            }
        });
        return layoutInflater.inflate(R.layout.fragment_treadly_connect_device_connect, viewGroup, false);
    }

    @Override // com.treadly.Treadly.UI.Util.BaseClasses.BaseFragment, androidx.fragment.app.Fragment
    public void onDetach() {
        super.onDetach();
        if (!this.hasSingleUserMode && getActivity() != null) {
            MainActivity mainActivity = (MainActivity) getActivity();
            mainActivity.blockBottomNavigation = false;
            mainActivity.showBottomNavigationView();
            mainActivity.setOnBackPressedListener(null);
        }
        dismissLoading();
    }

    @Override // com.treadly.Treadly.UI.Util.BaseClasses.BaseFragment, androidx.fragment.app.Fragment
    public void onViewCreated(@NonNull View view, @Nullable Bundle bundle) {
        super.onViewCreated(view, bundle);
        this.deviceListView = (ListView) view.findViewById(R.id.device_connect_list_view);
        this.deviceListView.setNestedScrollingEnabled(true);
        this.adapter = new TreadlyDeviceConnectListAdapter(getContext(), 0, new ArrayList(), this.adapterListener);
        this.deviceListView.setAdapter((ListAdapter) this.adapter);
        TreadlyClientLib.shared.stopScanning();
        this.scanButton = (Button) view.findViewById(R.id.scan_button);
        this.scanButton.setOnClickListener(new View.OnClickListener() { // from class: com.treadly.Treadly.UI.TreadlyConnect.-$$Lambda$TreadlyDeviceConnectFragment$gd-6VZd9hp807XdVJyTS6m8Cp5g
            @Override // android.view.View.OnClickListener
            public final void onClick(View view2) {
                TreadlyDeviceConnectFragment.lambda$onViewCreated$1(TreadlyDeviceConnectFragment.this, view2);
            }
        });
        this.skipButton = (Button) view.findViewById(R.id.skip_button);
        this.skipButton.setOnClickListener(new View.OnClickListener() { // from class: com.treadly.Treadly.UI.TreadlyConnect.-$$Lambda$TreadlyDeviceConnectFragment$FvHBDH7ymFDnLvD1UGzHbwstSHY
            @Override // android.view.View.OnClickListener
            public final void onClick(View view2) {
                TreadlyDeviceConnectFragment.lambda$onViewCreated$2(TreadlyDeviceConnectFragment.this, view2);
            }
        });
        boolean hasPreviouslyPairedDevice = TreadlyActivationManager.shared.hasPreviouslyPairedDevice();
        this.skipButton.setVisibility(hasPreviouslyPairedDevice ? 0 : 8);
        this.backArrowButton = (ImageView) view.findViewById(R.id.back_arrow);
        this.backArrowButton.setOnClickListener(new View.OnClickListener() { // from class: com.treadly.Treadly.UI.TreadlyConnect.-$$Lambda$TreadlyDeviceConnectFragment$RWdTLYKpN3I_qAIzyugkxako8Zc
            @Override // android.view.View.OnClickListener
            public final void onClick(View view2) {
                TreadlyDeviceConnectFragment.lambda$onViewCreated$3(TreadlyDeviceConnectFragment.this, view2);
            }
        });
        if (!this.isOnboarding) {
            this.backArrowButton.setVisibility(hasPreviouslyPairedDevice ? 0 : 8);
        } else {
            this.backArrowButton.setVisibility(8);
        }
        this.navigationTitle = (TextView) view.findViewById(R.id.pair_title);
        this.navigationTitle.setVisibility(hasPreviouslyPairedDevice ? 0 : 8);
        if (getActivity() != null) {
            MainActivity mainActivity = (MainActivity) getActivity();
            mainActivity.hideBottomNavigationView();
            mainActivity.enableBackButton(hasPreviouslyPairedDevice);
        }
        this.swipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.unclaimed_popup_swipe_refresh_layout);
        this.swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() { // from class: com.treadly.Treadly.UI.TreadlyConnect.-$$Lambda$TreadlyDeviceConnectFragment$WhlK6Ap7WHGdqepdSqCbdXPyV2k
            @Override // androidx.swiperefreshlayout.widget.SwipeRefreshLayout.OnRefreshListener
            public final void onRefresh() {
                TreadlyDeviceConnectFragment.lambda$onViewCreated$4(TreadlyDeviceConnectFragment.this);
            }
        });
    }

    public static /* synthetic */ void lambda$onViewCreated$1(TreadlyDeviceConnectFragment treadlyDeviceConnectFragment, View view) {
        if (!treadlyDeviceConnectFragment.isScanning) {
            treadlyDeviceConnectFragment.scanButton.setText("Stop Scanning");
            treadlyDeviceConnectFragment.scanDevices();
            return;
        }
        treadlyDeviceConnectFragment.scanButton.setText("Start Scanning");
        treadlyDeviceConnectFragment.stopScan();
    }

    public static /* synthetic */ void lambda$onViewCreated$2(TreadlyDeviceConnectFragment treadlyDeviceConnectFragment, View view) {
        FragmentActivity activity = treadlyDeviceConnectFragment.getActivity();
        if (activity != null) {
            activity.getSupportFragmentManager().popBackStack();
        }
    }

    public static /* synthetic */ void lambda$onViewCreated$3(TreadlyDeviceConnectFragment treadlyDeviceConnectFragment, View view) {
        FragmentActivity activity = treadlyDeviceConnectFragment.getActivity();
        if (activity != null) {
            activity.getSupportFragmentManager().popBackStack();
        }
    }

    public static /* synthetic */ void lambda$onViewCreated$4(TreadlyDeviceConnectFragment treadlyDeviceConnectFragment) {
        if (treadlyDeviceConnectFragment.swipeRefreshLayout.isRefreshing()) {
            treadlyDeviceConnectFragment.stopScan();
        }
        treadlyDeviceConnectFragment.scanDevices();
    }

    @Override // androidx.fragment.app.Fragment
    public void onStart() {
        super.onStart();
        scanDevices();
        this.scanButton.setText("Stop Scanning");
        if (this.isOnboarding) {
            TreadlyClientLib.shared.addRequestEventListener(this.deviceRequestListener);
            ActivationManager.shared.addActivationEventListener(this.deviceActivationListener);
        }
        hideBottomNavigation();
    }

    @Override // androidx.fragment.app.Fragment
    public void onDestroy() {
        super.onDestroy();
        MainActivity mainActivity = (MainActivity) getActivity();
        if (mainActivity != null) {
            mainActivity.enableBackButton(true);
        }
        TreadlyClientLib.shared.removeDeviceConnectionEventListener(this);
        TreadlyClientLib.shared.stopScanning();
        if (this.isOnboarding) {
            TreadlyClientLib.shared.removeRequestEventListener(this.deviceRequestListener);
            ActivationManager.shared.removeActivationEventListener(this.deviceActivationListener);
        }
        this.isScanning = false;
    }

    @Override // androidx.fragment.app.Fragment
    public void onActivityCreated(@Nullable Bundle bundle) {
        super.onActivityCreated(bundle);
        if (getActivity() != null) {
            ((MainActivity) getActivity()).hideBottomNavigationView();
        }
    }

    @Override // androidx.fragment.app.Fragment
    public void onStop() {
        super.onStop();
    }

    private void scanDevices() {
        if (this.adapter != null) {
            this.adapter.clearDevices();
        }
        TreadlyClientLib.shared.addDeviceConnectionEventListener(this);
        TreadlyClientLib.shared.startScanning(30000L);
        this.isScanning = true;
        this.swipeRefreshLayout.setRefreshing(false);
    }

    private void stopScan() {
        TreadlyClientLib.shared.removeDeviceConnectionEventListener(this);
        TreadlyClientLib.shared.stopScanning();
        this.isScanning = false;
    }

    @Override // androidx.fragment.app.Fragment
    public void onAttach(Context context) {
        super.onAttach(context);
        hideBottomNavigation();
    }

    @Override // com.treadly.client.lib.sdk.Listeners.DeviceConnectionEventListener
    public void onDeviceConnectionDeviceDiscovered(DeviceInfo deviceInfo) {
        if (this.targetConnectDeviceName != null && deviceInfo.getName() != null && this.targetConnectDeviceName.equals(deviceInfo.getName())) {
            TreadlyClientLib.shared.connectDevice(deviceInfo);
            this.targetConnectDeviceName = null;
        }
        if (deviceInfo == null || deviceInfo.getName() == null) {
            return;
        }
        this.adapter.addDevice(deviceInfo);
    }

    @Override // com.treadly.client.lib.sdk.Listeners.DeviceConnectionEventListener
    public void onDeviceConnectionChanged(final DeviceConnectionEvent deviceConnectionEvent) {
        if (deviceConnectionEvent == null || deviceConnectionEvent.getStatus() == null) {
            return;
        }
        ActivityUtil.runOnUiThread(getActivity(), new Runnable() { // from class: com.treadly.Treadly.UI.TreadlyConnect.-$$Lambda$TreadlyDeviceConnectFragment$LvzjFS7rOeaQSDBLnICtBUY66iA
            @Override // java.lang.Runnable
            public final void run() {
                TreadlyDeviceConnectFragment.lambda$onDeviceConnectionChanged$5(TreadlyDeviceConnectFragment.this, deviceConnectionEvent);
            }
        });
    }

    public static /* synthetic */ void lambda$onDeviceConnectionChanged$5(TreadlyDeviceConnectFragment treadlyDeviceConnectFragment, DeviceConnectionEvent deviceConnectionEvent) {
        treadlyDeviceConnectFragment.dismissLoading();
        System.out.println("NRS :: onDeviceConnectionChanged");
        TreadlyClientLib.shared.removeDeviceConnectionEventListener(treadlyDeviceConnectFragment);
        switch (deviceConnectionEvent.getStatus()) {
            case notConnected:
                treadlyDeviceConnectFragment.scanButton.setText("Start Scanning");
                treadlyDeviceConnectFragment.isScanning = false;
                if (deviceConnectionEvent.getDeviceInfo() != null) {
                    treadlyDeviceConnectFragment.showAlert("Connection Error", "Verify the device is in pairing mode and please try again");
                    break;
                } else if (treadlyDeviceConnectFragment.isConnecting) {
                    treadlyDeviceConnectFragment.showAlert("Connection Error", "Verify the device is on and is in pairing mode and please try again");
                    break;
                }
                break;
            case connected:
                if (treadlyDeviceConnectFragment.isOnboarding) {
                    System.out.println("NRS :: is onboarding, process onboarding connected");
                    treadlyDeviceConnectFragment.processOnboardingConnected(deviceConnectionEvent.getDeviceInfo());
                    break;
                }
                break;
        }
        treadlyDeviceConnectFragment.isConnecting = false;
    }

    protected void dismissDeviceConnectPage() {
        ActivityUtil.popBackStackImmediate(getActivity());
    }

    protected void showAlert(String str, String str2) {
        if (getContext() == null) {
            return;
        }
        final AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setMessage(str2);
        builder.setTitle(str);
        builder.setNeutralButton("Dismiss", new DialogInterface.OnClickListener() { // from class: com.treadly.Treadly.UI.TreadlyConnect.TreadlyDeviceConnectFragment.2
            @Override // android.content.DialogInterface.OnClickListener
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        ActivityUtil.runOnUiThread(getActivity(), new Runnable() { // from class: com.treadly.Treadly.UI.TreadlyConnect.TreadlyDeviceConnectFragment.3
            @Override // java.lang.Runnable
            public void run() {
                builder.create().show();
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void connectDevice(DeviceInfo deviceInfo) {
        if (this.listener != null) {
            this.listener.onConnectDevice(deviceInfo);
        }
        showLoading();
        this.isConnecting = true;
        if (TreadlyActivationManager.shared.hasPreviouslyPairedDevice()) {
            TreadlyClientLib.shared.addDeviceConnectionEventListener(this);
            TreadlyClientLib.shared.connectDevice(deviceInfo);
            return;
        }
        this.targetConnectDeviceName = deviceInfo.getName();
        scanDeviceSilently();
    }

    private void scanDeviceSilently() {
        TreadlyClientLib.shared.addDeviceConnectionEventListener(this);
        TreadlyClientLib.shared.startScanning(SCAN_TIMEOUT);
        this.isScanning = true;
    }

    private void processOnboardingConnected(DeviceInfo deviceInfo) {
        if (!TreadlyClientLib.shared.getDeviceComponentList()) {
            System.out.println("NRS :: unable to get device component list");
            return;
        }
        this.deviceName = deviceInfo.getName();
        this.onboardDeviceInfo = deviceInfo;
        SharedPreferences.shared.storeConnectedDeviceName(this.deviceName);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void setAuthenticationState() {
        if (this.isOnboarding && this.bleComonent != null) {
            System.out.println("NRS :: setAuthenticationState");
            TreadlyActivationManager.shared.handleAuthenticationState(this.deviceName, AuthenticationState.active);
            initializeSingleUserMode();
        }
    }

    private void initializeSingleUserMode() {
        final TreadlyOnboardingSingleUserFragment treadlyOnboardingSingleUserFragment = new TreadlyOnboardingSingleUserFragment();
        treadlyOnboardingSingleUserFragment.onboarding = true;
        treadlyOnboardingSingleUserFragment.deviceAddress = this.deviceAddress;
        if (!this.hasSingleUserMode) {
            TreadlyServiceManager.getInstance().isOnboarding = false;
            if (TreadlyClientLib.shared.isDeviceConnected()) {
                TreadlyClientLib.shared.disconnect();
            }
            startOnboardingContinueTimer();
            return;
        }
        treadlyOnboardingSingleUserFragment.hasWifiConfiguration = this.hasWifiConfiguration;
        treadlyOnboardingSingleUserFragment.connectedDeviceInfo = this.onboardDeviceInfo;
        TreadlyServiceManager.getInstance().getSingleUserMode(this.deviceAddress, new TreadlyServiceResponseEventAdapter() { // from class: com.treadly.Treadly.UI.TreadlyConnect.TreadlyDeviceConnectFragment.4
            @Override // com.treadly.Treadly.Data.Listeners.TreadlyServiceResponseEventAdapter, com.treadly.Treadly.Data.Listeners.TreadlyServiceResponseEventListener
            public void onUserInfo(String str, UserInfo userInfo) {
                super.onUserInfo(str, userInfo);
                if (str == null && userInfo != null) {
                    treadlyOnboardingSingleUserFragment.ownerInfo = userInfo;
                    if (TreadlyServiceManager.getInstance().getUserId().toLowerCase().equals(userInfo.id.toLowerCase())) {
                        treadlyOnboardingSingleUserFragment.isEnabled = true;
                        treadlyOnboardingSingleUserFragment.singleUserSelected = true;
                    } else {
                        treadlyOnboardingSingleUserFragment.user = userInfo;
                        treadlyOnboardingSingleUserFragment.isEnabled = false;
                    }
                } else {
                    treadlyOnboardingSingleUserFragment.isEnabled = true;
                    treadlyOnboardingSingleUserFragment.singleUserSelected = false;
                }
                TreadlyDeviceConnectFragment.this.getFragmentManager().beginTransaction().replace(R.id.activity_fragment_container_empty, treadlyOnboardingSingleUserFragment, TreadlyOnboardingSingleUserFragment.TAG).commit();
            }
        });
    }

    private void startOnboardingContinueTimer() {
        stopOnboardingContinueTimer();
        System.out.println("NRS :: starting onboard continue timer");
        this.onboardingContinueTimer = new Timer();
        this.onboardingContinueTimer.schedule(new TimerTask() { // from class: com.treadly.Treadly.UI.TreadlyConnect.TreadlyDeviceConnectFragment.5
            @Override // java.util.TimerTask, java.lang.Runnable
            public void run() {
                TreadlyDeviceConnectFragment.this.finishOnboarding();
                System.out.println("NRS :: onboard continue timer expired");
            }
        }, 2000L);
    }

    private void stopOnboardingContinueTimer() {
        if (this.onboardingContinueTimer != null) {
            this.onboardingContinueTimer.cancel();
            this.onboardingContinueTimer = null;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void finishOnboarding() {
        if (this.pendingChanges) {
            return;
        }
        this.pendingChanges = true;
        TreadlyServiceManager.getInstance().updateOnboardingStatus(new TreadlyServiceResponseEventAdapter() { // from class: com.treadly.Treadly.UI.TreadlyConnect.TreadlyDeviceConnectFragment.6
            @Override // com.treadly.Treadly.Data.Listeners.TreadlyServiceResponseEventAdapter, com.treadly.Treadly.Data.Listeners.TreadlyServiceResponseEventListener
            public void onSuccess(String str) {
                super.onSuccess(str);
                PrintStream printStream = System.out;
                printStream.println("NRS :: finishOnboarding error=" + str);
                if (str != null) {
                    TreadlyDeviceConnectFragment.this.pendingChanges = false;
                    TreadlyDeviceConnectFragment.this.showAlert("Oops!", "There was an error. Please try again");
                    return;
                }
                ((MainActivity) TreadlyDeviceConnectFragment.this.getActivity()).toConnect();
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* renamed from: com.treadly.Treadly.UI.TreadlyConnect.TreadlyDeviceConnectFragment$8  reason: invalid class name */
    /* loaded from: classes2.dex */
    public class AnonymousClass8 extends RequestEventAdapter {
        AnonymousClass8() {
        }

        @Override // com.treadly.client.lib.sdk.Listeners.RequestEventAdapter, com.treadly.client.lib.sdk.Listeners.RequestEventListener
        public void onRequestGetDeviceComponentsListResponse(boolean z, ComponentInfo[] componentInfoArr) {
            super.onRequestGetDeviceComponentsListResponse(z, componentInfoArr);
            System.out.println("NRS :: onRequestGetDeviceComponentsListResponse");
            if (z) {
                System.out.println("NRS :: component success");
                AppActivityManager.shared.componentList = Arrays.asList(componentInfoArr);
                TreadlyDeviceConnectFragment.this.registerChecked = false;
                Arrays.asList(componentInfoArr).forEach(new Consumer() { // from class: com.treadly.Treadly.UI.TreadlyConnect.-$$Lambda$TreadlyDeviceConnectFragment$8$p4B4GQuvhQn00S21RHEGCp5h-BY
                    @Override // java.util.function.Consumer
                    public final void accept(Object obj) {
                        TreadlyDeviceConnectFragment.AnonymousClass8.lambda$onRequestGetDeviceComponentsListResponse$0(TreadlyDeviceConnectFragment.AnonymousClass8.this, (ComponentInfo) obj);
                    }
                });
            }
        }

        public static /* synthetic */ void lambda$onRequestGetDeviceComponentsListResponse$0(AnonymousClass8 anonymousClass8, ComponentInfo componentInfo) {
            if (componentInfo.getType() == ComponentType.bleBoard) {
                TreadlyDeviceConnectFragment.this.deviceAddress = componentInfo.getId();
                if (TreadlyDeviceConnectFragment.this.isOnboarding) {
                    boolean z = true;
                    TreadlyDeviceConnectFragment.this.hasSingleUserMode = componentInfo.getVersionInfo().isGreaterThan(new VersionInfo(3, 42, 0)) || componentInfo.getVersionInfo().isEqual(new VersionInfo(3, 42, 0));
                    TreadlyDeviceConnectFragment treadlyDeviceConnectFragment = TreadlyDeviceConnectFragment.this;
                    if (!componentInfo.getVersionInfo().isGreaterThan(new VersionInfo(3, 63, 0)) && !componentInfo.getVersionInfo().isEqual(new VersionInfo(3, 63, 0))) {
                        z = false;
                    }
                    treadlyDeviceConnectFragment.hasWifiConfiguration = z;
                }
                TreadlyDeviceConnectFragment.this.bleComonent = componentInfo;
                TreadlyDeviceConnectFragment.this.setAuthenticationState();
            }
        }
    }
}
