package com.treadly.Treadly.UI.TreadlyConnect.DeviceConnect;

import android.app.AlertDialog;
import android.bluetooth.BluetoothA2dp;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothProfile;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.AudioManager;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.method.LinkMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.treadly.Treadly.Data.Listeners.TreadlyServiceResponseEventAdapter;
import com.treadly.Treadly.Data.Managers.AppActivityManager;
import com.treadly.Treadly.Data.Managers.TreadlyActivationManager;
import com.treadly.Treadly.Data.Managers.TreadlyServiceManager;
import com.treadly.Treadly.Data.Model.UserInfo;
import com.treadly.Treadly.MainActivity;
import com.treadly.Treadly.R;
import com.treadly.Treadly.UI.Onboarding.NotFound.TreadlyOnboardingNotFoundFragment;
import com.treadly.Treadly.UI.Onboarding.SingleUser.TreadlyOnboardingSingleUserFragment;
import com.treadly.Treadly.UI.TreadlyConnect.DeviceConnect.TreadlyDeviceConnectStatusFragment;
import com.treadly.Treadly.UI.TreadlyConnect.DeviceConnect.TreadlyDeviceConnectStatusListAdapter;
import com.treadly.Treadly.UI.TreadlyConnect.TreadlyDeviceConnectFragment;
import com.treadly.Treadly.UI.Util.ActivityUtil;
import com.treadly.Treadly.UI.Util.BaseClasses.BaseFragment;
import com.treadly.Treadly.UI.Util.IndexedPagerSnapHelper;
import com.treadly.Treadly.UI.Util.SharedPreferences;
import com.treadly.client.lib.sdk.Listeners.DeviceActivationEventListener;
import com.treadly.client.lib.sdk.Listeners.DeviceConnectionEventAdapter;
import com.treadly.client.lib.sdk.Listeners.RequestEventAdapter;
import com.treadly.client.lib.sdk.Listeners.RequestEventListener;
import com.treadly.client.lib.sdk.Managers.ActivationManager;
import com.treadly.client.lib.sdk.Model.ActivationInfo;
import com.treadly.client.lib.sdk.Model.AuthenticateReferenceCodeInfo;
import com.treadly.client.lib.sdk.Model.AuthenticationState;
import com.treadly.client.lib.sdk.Model.BluetoothConnectionState;
import com.treadly.client.lib.sdk.Model.ComponentInfo;
import com.treadly.client.lib.sdk.Model.ComponentType;
import com.treadly.client.lib.sdk.Model.DeviceConnectionEvent;
import com.treadly.client.lib.sdk.Model.DeviceInfo;
import com.treadly.client.lib.sdk.Model.DeviceStatus;
import com.treadly.client.lib.sdk.TreadlyClientLib;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.function.Consumer;
import org.otwebrtc.MediaStreamTrack;

/* loaded from: classes2.dex */
public class TreadlyDeviceConnectStatusFragment extends BaseFragment {
    private static final int SCAN_TIMEOUT = 15000;
    private static final int SETTLE_TIMEOUT = 200;
    public static final String TAG = "DEVICE_CONNECT_STATUS";
    private TreadlyDeviceConnectStatusListAdapter adapter;
    private ComponentInfo bleComponent;
    String btAudioPassword;
    List<String> btDeviceList;
    private String deviceAddress;
    private RecyclerView deviceListView;
    private String deviceName;
    Timer initScanTimer;
    private ImageButton leftNavArrow;
    public TreadlyDeviceConnectFragment.TreadlyDeviceConnectConnectFragmentEventListener listener;
    BluetoothA2dp mA2dpService;
    AudioManager mAudioManager;
    BluetoothAdapter mBtAdapter;
    FrameLayout noDevicesFoundView;
    private DeviceInfo onboardDeviceInfo;
    Button rescanButton;
    Timer rescanTimer;
    private ImageButton rightNavArrow;
    private boolean scanning;
    Timer settleTimer;
    private IndexedPagerSnapHelper snapHelper;
    private int currentIndex = 0;
    private boolean isScrolling = false;
    private boolean navButtonPressed = false;
    public boolean isOnboarding = false;
    public boolean onboardingDeviceFound = false;
    public DeviceInfo foundOnboardDevice = null;
    public boolean isReconnecting = false;
    public DeviceInfo connectedDeviceInfo = null;
    BluetoothDevice connectedBluetoothDevice = null;
    public String targetConnectDeviceName = null;
    public boolean isConnecting = false;
    boolean connectButtonPressed = false;
    private boolean haveReturned = false;
    boolean isSettleTimeout = true;
    private final TreadlyDeviceConnectStatusListAdapter.ConnectDeviceEventListener adapterListener = new TreadlyDeviceConnectStatusListAdapter.ConnectDeviceEventListener() { // from class: com.treadly.Treadly.UI.TreadlyConnect.DeviceConnect.TreadlyDeviceConnectStatusFragment.5
        @Override // com.treadly.Treadly.UI.TreadlyConnect.DeviceConnect.TreadlyDeviceConnectStatusListAdapter.ConnectDeviceEventListener
        public void connectDeviceSelected(DeviceInfo deviceInfo) {
            TreadlyDeviceConnectStatusFragment.this.connectButtonPressed = true;
            TreadlyDeviceConnectStatusFragment.this.connectDevice(deviceInfo);
        }

        @Override // com.treadly.Treadly.UI.TreadlyConnect.DeviceConnect.TreadlyDeviceConnectStatusListAdapter.ConnectDeviceEventListener
        public void disconnectDeviceSelected(DeviceInfo deviceInfo) {
            TreadlyDeviceConnectStatusFragment.this.disconnectDevice(deviceInfo);
        }

        @Override // com.treadly.Treadly.UI.TreadlyConnect.DeviceConnect.TreadlyDeviceConnectStatusListAdapter.ConnectDeviceEventListener
        public void deviceHelpPressed(DeviceInfo deviceInfo) {
            String str;
            switch (deviceInfo.getGen()) {
                case gen1:
                case gen2:
                    str = "To connect to Treadly’s speed control First place the unit in pair mode by tapping 3 times at the speed up zone of the belt.";
                    break;
                default:
                    str = "To connect to Treadly’s speed control First place the unit in pair mode by pressing the pause button for 5 seconds.";
                    break;
            }
            TreadlyDeviceConnectStatusFragment.this.showBaseAlert("Speed Control", str);
        }

        @Override // com.treadly.Treadly.UI.TreadlyConnect.DeviceConnect.TreadlyDeviceConnectStatusListAdapter.ConnectDeviceEventListener
        public void connectAudioSelected(String str) {
            TreadlyDeviceConnectStatusFragment.this.connectButtonPressed = true;
            TreadlyDeviceConnectStatusFragment.this.connectAudioDevice(str);
        }

        @Override // com.treadly.Treadly.UI.TreadlyConnect.DeviceConnect.TreadlyDeviceConnectStatusListAdapter.ConnectDeviceEventListener
        public void disconnectAudioSelected(String str) {
            TreadlyDeviceConnectStatusFragment.this.disconnectAudioDevice(str);
        }

        @Override // com.treadly.Treadly.UI.TreadlyConnect.DeviceConnect.TreadlyDeviceConnectStatusListAdapter.ConnectDeviceEventListener
        public void audioHelpPressed(String str) {
            TreadlyDeviceConnectStatusFragment.this.showBaseAlert("Speakers", (TreadlyDeviceConnectStatusFragment.this.btAudioPassword == null || TreadlyDeviceConnectStatusFragment.this.adapter.connectedDeviceInfo == null || !DeviceInfo.parseDeviceId(str).equals(TreadlyDeviceConnectStatusFragment.this.adapter.connectedDeviceInfo.getDeviceId())) ? "Please connect to your Treadly speed control first to view your speakers password." : String.format("To connect to Treadly’s speakers, use the phone Bluetooth setting to discover the speakers and enter the password %s", TreadlyDeviceConnectStatusFragment.this.btAudioPassword));
        }
    };
    private final DeviceConnectionEventAdapter deviceConnectionListener = new AnonymousClass6();
    private final LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(), 0, false);
    BroadcastReceiver mReceiver = new AnonymousClass7();
    BluetoothProfile.ServiceListener mA2dpListener = new AnonymousClass8();
    private boolean registerChecked = false;
    private boolean hasSingleUserMode = false;
    private boolean hasWifiConfiguration = false;
    private Timer onboardingContinueTimer = null;
    private boolean pendingChanges = false;
    private boolean isActivationInProgress = false;
    private boolean pendingActivationBleEnable = false;
    private boolean presentedUnprocessedDialog = false;
    private boolean authenticationStateUpdated = false;
    private boolean activationCodeTextFieldValid = false;
    private boolean authenticationStateChecked = false;
    private boolean presentedActivationDialog = false;
    private ActivationInfo activationInfo = null;
    private AlertDialog startActivationAlert = null;
    private AlertDialog submitActivationAlert = null;
    private AlertDialog completeActivationAlert = null;
    private AlertDialog errorActivationAlert = null;
    private AlertDialog finishActivationAlert = null;
    private AuthenticationState authState = AuthenticationState.unknown;
    private DeviceStatus currentDeviceStatusInfo = null;
    private TextWatcher textWatcher = new TextWatcher() { // from class: com.treadly.Treadly.UI.TreadlyConnect.DeviceConnect.TreadlyDeviceConnectStatusFragment.9
        @Override // android.text.TextWatcher
        public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {
        }

        @Override // android.text.TextWatcher
        public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
        }

        @Override // android.text.TextWatcher
        public void afterTextChanged(Editable editable) {
            if (editable == null) {
                TreadlyDeviceConnectStatusFragment.this.activationCodeTextFieldValid = false;
                TreadlyDeviceConnectStatusFragment.this.updateActivationAction();
                return;
            }
            TreadlyDeviceConnectStatusFragment.this.activationCodeTextFieldValid = !editable.toString().isEmpty();
            TreadlyDeviceConnectStatusFragment.this.updateActivationAction();
        }
    };
    private boolean singleUserModeDisplayed = false;
    boolean displayingOnboardingNoDeviceFound = false;
    DeviceActivationEventListener deviceActivationListener = new AnonymousClass13();
    RequestEventListener deviceRequestListener = new AnonymousClass14();

    void setScanning(boolean z) {
        this.scanning = z;
        ActivityUtil.runOnUiThread(getActivity(), new Runnable() { // from class: com.treadly.Treadly.UI.TreadlyConnect.DeviceConnect.-$$Lambda$TreadlyDeviceConnectStatusFragment$mM1PnT8Gol2KfLlhHPzsZh9NbPc
            @Override // java.lang.Runnable
            public final void run() {
                TreadlyDeviceConnectStatusFragment.lambda$setScanning$0(TreadlyDeviceConnectStatusFragment.this);
            }
        });
    }

    public static /* synthetic */ void lambda$setScanning$0(TreadlyDeviceConnectStatusFragment treadlyDeviceConnectStatusFragment) {
        treadlyDeviceConnectStatusFragment.rescanButton.setText(treadlyDeviceConnectStatusFragment.scanning ? R.string.scanning_label : R.string.rescan_label);
        treadlyDeviceConnectStatusFragment.rescanButton.setAlpha(treadlyDeviceConnectStatusFragment.scanning ? 0.3f : 1.0f);
        treadlyDeviceConnectStatusFragment.rescanButton.setEnabled(!treadlyDeviceConnectStatusFragment.scanning);
    }

    @Override // androidx.fragment.app.Fragment
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        addBackStackCallback();
        MainActivity mainActivity = (MainActivity) getActivity();
        if (mainActivity != null) {
            this.mAudioManager = (AudioManager) mainActivity.getSystemService(MediaStreamTrack.AUDIO_TRACK_KIND);
            mainActivity.registerReceiver(this.mReceiver, new IntentFilter("android.bluetooth.a2dp.profile.action.CONNECTION_STATE_CHANGED"));
            this.mBtAdapter = BluetoothAdapter.getDefaultAdapter();
            this.mBtAdapter.getProfileProxy(getContext(), this.mA2dpListener, 2);
        }
    }

    @Override // androidx.fragment.app.Fragment
    public View onCreateView(@NonNull LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        return layoutInflater.inflate(R.layout.fragment_treadly_device_connect_status, viewGroup, false);
    }

    @Override // com.treadly.Treadly.UI.Util.BaseClasses.BaseFragment, androidx.fragment.app.Fragment
    public void onViewCreated(@NonNull View view, @Nullable Bundle bundle) {
        super.onViewCreated(view, bundle);
        this.adapter = new TreadlyDeviceConnectStatusListAdapter(getContext(), new ArrayList(), this.adapterListener);
        this.deviceListView = (RecyclerView) view.findViewById(R.id.device_connect_status_device_list);
        this.deviceListView.setAdapter(this.adapter);
        this.deviceListView.setLayoutManager(this.layoutManager);
        this.snapHelper = new IndexedPagerSnapHelper();
        this.snapHelper.attachToRecyclerView(this.deviceListView);
        this.deviceListView.addOnScrollListener(new AnonymousClass1());
        View inflate = LayoutInflater.from(getContext()).inflate(R.layout.fragment_treadly_device_connect_status_no_device_found, (ViewGroup) null, true);
        this.rescanButton = (Button) inflate.findViewById(R.id.device_connect_status_no_device_found_rescan_button);
        this.rescanButton.setOnClickListener(new View.OnClickListener() { // from class: com.treadly.Treadly.UI.TreadlyConnect.DeviceConnect.-$$Lambda$TreadlyDeviceConnectStatusFragment$UNT8GHxj8HxvJc1QQ34I-Yx_0WE
            @Override // android.view.View.OnClickListener
            public final void onClick(View view2) {
                TreadlyDeviceConnectStatusFragment.this.scanDevices(view2);
            }
        });
        ((TextView) inflate.findViewById(R.id.device_connect_status_no_device_found_item_3)).setMovementMethod(LinkMovementMethod.getInstance());
        this.noDevicesFoundView = (FrameLayout) view.findViewById(R.id.device_connect_status_no_device_found_view);
        this.noDevicesFoundView.addView(inflate);
        this.noDevicesFoundView.setVisibility(4);
        Button button = (Button) view.findViewById(R.id.device_connect_status_skip_button);
        button.setOnClickListener(new View.OnClickListener() { // from class: com.treadly.Treadly.UI.TreadlyConnect.DeviceConnect.-$$Lambda$TreadlyDeviceConnectStatusFragment$lwsBlbe9ZtgNqgnLyexMGGFUkpQ
            @Override // android.view.View.OnClickListener
            public final void onClick(View view2) {
                TreadlyDeviceConnectStatusFragment.this.popBackStack();
            }
        });
        button.setVisibility(8);
        ImageView imageView = (ImageView) view.findViewById(R.id.device_connect_status_back_arrow);
        imageView.setOnClickListener(new View.OnClickListener() { // from class: com.treadly.Treadly.UI.TreadlyConnect.DeviceConnect.-$$Lambda$TreadlyDeviceConnectStatusFragment$P7Gthp8sgNuwY8XL4dX_Ncn6DD0
            @Override // android.view.View.OnClickListener
            public final void onClick(View view2) {
                TreadlyDeviceConnectStatusFragment.this.popBackStack();
            }
        });
        this.leftNavArrow = (ImageButton) view.findViewById(R.id.device_connect_status_left_arrow);
        this.leftNavArrow.setOnClickListener(new View.OnClickListener() { // from class: com.treadly.Treadly.UI.TreadlyConnect.DeviceConnect.-$$Lambda$TreadlyDeviceConnectStatusFragment$woQW62goZUXo0wIeAtC_FM0lLig
            @Override // android.view.View.OnClickListener
            public final void onClick(View view2) {
                TreadlyDeviceConnectStatusFragment.this.previousPressed(view2);
            }
        });
        this.rightNavArrow = (ImageButton) view.findViewById(R.id.device_connect_status_right_arrow);
        this.rightNavArrow.setOnClickListener(new View.OnClickListener() { // from class: com.treadly.Treadly.UI.TreadlyConnect.DeviceConnect.-$$Lambda$TreadlyDeviceConnectStatusFragment$zu_ubwAUyStd8BXNIER5YQOXS54
            @Override // android.view.View.OnClickListener
            public final void onClick(View view2) {
                TreadlyDeviceConnectStatusFragment.this.nextPressed(view2);
            }
        });
        boolean hasPreviouslyPairedDevice = TreadlyActivationManager.shared.hasPreviouslyPairedDevice();
        if (!this.isOnboarding) {
            if (this.foundOnboardDevice != null) {
                this.adapter.addDevice(this.foundOnboardDevice);
            }
            imageView.setVisibility(hasPreviouslyPairedDevice ? 0 : 4);
            return;
        }
        imageView.setVisibility(4);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* renamed from: com.treadly.Treadly.UI.TreadlyConnect.DeviceConnect.TreadlyDeviceConnectStatusFragment$1  reason: invalid class name */
    /* loaded from: classes2.dex */
    public class AnonymousClass1 extends RecyclerView.OnScrollListener {
        AnonymousClass1() {
        }

        @Override // androidx.recyclerview.widget.RecyclerView.OnScrollListener
        public void onScrollStateChanged(RecyclerView recyclerView, final int i) {
            super.onScrollStateChanged(recyclerView, i);
            ActivityUtil.runOnUiThread(TreadlyDeviceConnectStatusFragment.this.getActivity(), new Runnable() { // from class: com.treadly.Treadly.UI.TreadlyConnect.DeviceConnect.-$$Lambda$TreadlyDeviceConnectStatusFragment$1$kZwuyvR4XsTay87VEpat_lrqctI
                @Override // java.lang.Runnable
                public final void run() {
                    TreadlyDeviceConnectStatusFragment.AnonymousClass1.lambda$onScrollStateChanged$0(TreadlyDeviceConnectStatusFragment.AnonymousClass1.this, i);
                }
            });
        }

        public static /* synthetic */ void lambda$onScrollStateChanged$0(AnonymousClass1 anonymousClass1, int i) {
            if (i == 0) {
                if (TreadlyDeviceConnectStatusFragment.this.navButtonPressed) {
                    TreadlyDeviceConnectStatusFragment.this.navButtonPressed = false;
                    return;
                }
                TreadlyDeviceConnectStatusFragment.this.currentIndex = TreadlyDeviceConnectStatusFragment.this.snapHelper.currentIndex;
                TreadlyDeviceConnectStatusFragment.this.updateNavArrows();
                return;
            }
            TreadlyDeviceConnectStatusFragment.this.isScrolling = i == 1;
        }
    }

    @Override // androidx.fragment.app.Fragment
    public void onStart() {
        super.onStart();
        TreadlyClientLib.shared.addRequestEventListener(this.deviceRequestListener);
        if (this.isOnboarding) {
            ActivationManager.shared.addActivationEventListener(this.deviceActivationListener);
        }
    }

    @Override // androidx.fragment.app.Fragment
    public void onResume() {
        super.onResume();
        returnedSetup();
    }

    @Override // com.treadly.Treadly.UI.Util.BaseClasses.BaseFragment
    public void onFragmentReturning() {
        super.onFragmentReturning();
        returnedSetup();
    }

    @Override // androidx.fragment.app.Fragment
    public void onStop() {
        super.onStop();
        this.haveReturned = false;
        TreadlyClientLib.shared.removeDeviceConnectionEventListener(this.deviceConnectionListener);
        TreadlyClientLib.shared.removeRequestEventListener(this.deviceRequestListener);
        if (this.isOnboarding) {
            ActivationManager.shared.removeActivationEventListener(this.deviceActivationListener);
        }
        setScanning(false);
        stopInitScanTimer();
        stopRescanTimer();
        stopSettleTimer();
    }

    @Override // com.treadly.Treadly.UI.Util.BaseClasses.BaseFragment, androidx.fragment.app.Fragment
    public void onDetach() {
        super.onDetach();
        if (!this.hasSingleUserMode) {
            showBottomNavigation(true);
        }
        dismissLoading();
    }

    private void returnedSetup() {
        if (this.haveReturned) {
            return;
        }
        this.haveReturned = true;
        if (this.isOnboarding && this.displayingOnboardingNoDeviceFound) {
            popBackStack();
            return;
        }
        TreadlyClientLib.shared.addDeviceConnectionEventListener(this.deviceConnectionListener);
        this.singleUserModeDisplayed = false;
        this.currentIndex = 0;
        this.deviceListView.scrollToPosition(this.currentIndex);
        updateViews();
        if (TreadlyClientLib.shared.isDeviceStatusConnected() && this.connectedDeviceInfo != null) {
            setConnectedDevice(this.connectedDeviceInfo);
            TreadlyClientLib.shared.getBtAudioPassword();
        } else {
            initScanning();
        }
        hideBottomNavigation(true);
    }

    void initScanning() {
        stopInitScanTimer();
        if (TreadlyClientLib.shared.getBluetoothConnectionSTate() == BluetoothConnectionState.poweredOn) {
            scanDevices();
            return;
        }
        this.initScanTimer = new Timer();
        this.initScanTimer.schedule(new TimerTask() { // from class: com.treadly.Treadly.UI.TreadlyConnect.DeviceConnect.TreadlyDeviceConnectStatusFragment.2
            @Override // java.util.TimerTask, java.lang.Runnable
            public void run() {
                ActivityUtil.runOnUiThread(TreadlyDeviceConnectStatusFragment.this.getActivity(), new Runnable() { // from class: com.treadly.Treadly.UI.TreadlyConnect.DeviceConnect.TreadlyDeviceConnectStatusFragment.2.1
                    @Override // java.lang.Runnable
                    public void run() {
                        TreadlyDeviceConnectStatusFragment.this.initScanning();
                    }
                });
            }
        }, 100L);
    }

    void stopInitScanTimer() {
        if (this.initScanTimer != null) {
            this.initScanTimer.cancel();
        }
        this.initScanTimer = null;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void scanDevices(View view) {
        scanDevices();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void scanDevices() {
        stopRescanTimer();
        rescanDevices();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void stopScan() {
        if (this.scanning) {
            TreadlyClientLib.shared.stopScanning();
            setScanning(false);
        }
    }

    void rescanDevices() {
        rescanDevices(false);
    }

    void rescanDevices(boolean z) {
        startSettleTimer();
        if (this.adapter != null) {
            this.adapter.clearDevices(z);
        }
        if (!z) {
            updateViews();
        }
        TreadlyClientLib.shared.startScanning(15000L, false);
        scanBondedBluetoothDevices();
        setScanning(true);
        startRescanTimer();
    }

    void startRescanTimer() {
        stopRescanTimer();
        this.rescanTimer = new Timer();
        this.rescanTimer.schedule(new TimerTask() { // from class: com.treadly.Treadly.UI.TreadlyConnect.DeviceConnect.TreadlyDeviceConnectStatusFragment.3
            @Override // java.util.TimerTask, java.lang.Runnable
            public void run() {
                if (TreadlyDeviceConnectStatusFragment.this.connectButtonPressed) {
                    TreadlyDeviceConnectStatusFragment.this.startRescanTimer();
                }
                TreadlyDeviceConnectStatusFragment.this.rescanDevices(true);
            }
        }, 15000L);
    }

    void stopRescanTimer() {
        if (this.rescanTimer != null) {
            this.rescanTimer.cancel();
            this.rescanTimer.purge();
        }
        this.rescanTimer = null;
    }

    void startSettleTimer() {
        stopSettleTimer();
        this.isSettleTimeout = true;
        this.settleTimer = new Timer();
        this.settleTimer.schedule(new AnonymousClass4(), 200L);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* renamed from: com.treadly.Treadly.UI.TreadlyConnect.DeviceConnect.TreadlyDeviceConnectStatusFragment$4  reason: invalid class name */
    /* loaded from: classes2.dex */
    public class AnonymousClass4 extends TimerTask {
        AnonymousClass4() {
        }

        @Override // java.util.TimerTask, java.lang.Runnable
        public void run() {
            if (TreadlyDeviceConnectStatusFragment.this.isSettleTimeout) {
                TreadlyDeviceConnectStatusFragment.this.isSettleTimeout = false;
                ActivityUtil.runOnUiThread(TreadlyDeviceConnectStatusFragment.this.getActivity(), new Runnable() { // from class: com.treadly.Treadly.UI.TreadlyConnect.DeviceConnect.-$$Lambda$TreadlyDeviceConnectStatusFragment$4$WkPmw7iKzl7Z4F9V-rxE-ExoEEk
                    @Override // java.lang.Runnable
                    public final void run() {
                        TreadlyDeviceConnectStatusFragment.AnonymousClass4.lambda$run$0(TreadlyDeviceConnectStatusFragment.AnonymousClass4.this);
                    }
                });
            }
        }

        public static /* synthetic */ void lambda$run$0(AnonymousClass4 anonymousClass4) {
            TreadlyDeviceConnectStatusFragment.this.adapter.notifyDataSetChanged();
            TreadlyDeviceConnectStatusFragment.this.updateViews();
        }
    }

    void stopSettleTimer() {
        this.isSettleTimeout = false;
        if (this.settleTimer != null) {
            this.settleTimer.cancel();
        }
        this.settleTimer = null;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void connectDevice(DeviceInfo deviceInfo) {
        if (this.listener != null) {
            this.listener.onConnectDevice(deviceInfo);
        }
        showLoading();
        this.isConnecting = true;
        if (TreadlyActivationManager.shared.hasPreviouslyPairedDevice()) {
            TreadlyClientLib.shared.connectDevice(deviceInfo);
            return;
        }
        this.targetConnectDeviceName = deviceInfo.getName();
        scanDeviceSilently();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void disconnectDevice(DeviceInfo deviceInfo) {
        TreadlyClientLib.shared.disconnect();
    }

    private void scanDeviceSilently() {
        TreadlyClientLib.shared.startScanning(15000L, false);
        setScanning(true);
    }

    private void setConnectedDevice(DeviceInfo deviceInfo) {
        this.adapter.connectedDeviceInfo = deviceInfo;
        this.adapter.clearDevices();
        this.adapter.addDevice(deviceInfo);
        scanBondedBluetoothDevices();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void clearConnectedDevice() {
        this.connectedDeviceInfo = null;
        this.adapter.connectedDeviceInfo = null;
        this.adapter.notifyDataSetChanged();
    }

    public void dismissDeviceConnectPage() {
        ActivityUtil.popBackStackImmediate(getActivity());
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* renamed from: com.treadly.Treadly.UI.TreadlyConnect.DeviceConnect.TreadlyDeviceConnectStatusFragment$6  reason: invalid class name */
    /* loaded from: classes2.dex */
    public class AnonymousClass6 extends DeviceConnectionEventAdapter {
        AnonymousClass6() {
        }

        @Override // com.treadly.client.lib.sdk.Listeners.DeviceConnectionEventAdapter, com.treadly.client.lib.sdk.Listeners.DeviceConnectionEventListener
        public void onDeviceConnectionChanged(final DeviceConnectionEvent deviceConnectionEvent) {
            super.onDeviceConnectionChanged(deviceConnectionEvent);
            TreadlyDeviceConnectStatusFragment.this.dismissLoading();
            if (deviceConnectionEvent == null || deviceConnectionEvent.getStatus() == null) {
                return;
            }
            ActivityUtil.runOnUiThread(TreadlyDeviceConnectStatusFragment.this.getActivity(), new Runnable() { // from class: com.treadly.Treadly.UI.TreadlyConnect.DeviceConnect.-$$Lambda$TreadlyDeviceConnectStatusFragment$6$IexuKnGY94XtafoCrzOP-ywUCyI
                @Override // java.lang.Runnable
                public final void run() {
                    TreadlyDeviceConnectStatusFragment.AnonymousClass6.lambda$onDeviceConnectionChanged$0(TreadlyDeviceConnectStatusFragment.AnonymousClass6.this, deviceConnectionEvent);
                }
            });
        }

        public static /* synthetic */ void lambda$onDeviceConnectionChanged$0(AnonymousClass6 anonymousClass6, DeviceConnectionEvent deviceConnectionEvent) {
            switch (deviceConnectionEvent.getStatus()) {
                case notConnected:
                    TreadlyDeviceConnectStatusFragment.this.setScanning(false);
                    if (deviceConnectionEvent.getDeviceInfo() != null) {
                        if (TreadlyDeviceConnectStatusFragment.this.connectedDeviceInfo != null && TreadlyDeviceConnectStatusFragment.this.connectedDeviceInfo.getName().equals(deviceConnectionEvent.getDeviceInfo().getName())) {
                            TreadlyDeviceConnectStatusFragment.this.clearConnectedDevice();
                            TreadlyDeviceConnectStatusFragment.this.setScanning(true);
                            TreadlyDeviceConnectStatusFragment.this.scanDevices();
                            break;
                        } else if (TreadlyDeviceConnectStatusFragment.this.connectButtonPressed) {
                            TreadlyDeviceConnectStatusFragment.this.connectButtonPressed = false;
                            TreadlyDeviceConnectStatusFragment.this.toPairModeError(deviceConnectionEvent.getDeviceInfo());
                            break;
                        }
                    } else if (TreadlyDeviceConnectStatusFragment.this.connectedDeviceInfo != null) {
                        TreadlyDeviceConnectStatusFragment.this.clearConnectedDevice();
                        TreadlyDeviceConnectStatusFragment.this.setScanning(true);
                        TreadlyDeviceConnectStatusFragment.this.scanDevices();
                        break;
                    } else if (TreadlyDeviceConnectStatusFragment.this.adapter.getItemCount() == 0 && !TreadlyDeviceConnectStatusFragment.this.isOnboarding && TreadlyDeviceConnectStatusFragment.this.connectButtonPressed) {
                        TreadlyDeviceConnectStatusFragment.this.toPairModeError(deviceConnectionEvent.getDeviceInfo());
                        break;
                    }
                    break;
                case connected:
                    if (TreadlyDeviceConnectStatusFragment.this.isOnboarding) {
                        TreadlyDeviceConnectStatusFragment.this.connectedDeviceInfo = deviceConnectionEvent.getDeviceInfo();
                        TreadlyDeviceConnectStatusFragment.this.processOnboardingConnected(TreadlyDeviceConnectStatusFragment.this.connectedDeviceInfo);
                        break;
                    }
                    break;
            }
            TreadlyDeviceConnectStatusFragment.this.connectButtonPressed = false;
            TreadlyDeviceConnectStatusFragment.this.isConnecting = false;
        }

        @Override // com.treadly.client.lib.sdk.Listeners.DeviceConnectionEventAdapter, com.treadly.client.lib.sdk.Listeners.DeviceConnectionEventListener
        public void onDeviceConnectionDeviceDiscovered(final DeviceInfo deviceInfo) {
            super.onDeviceConnectionDeviceDiscovered(deviceInfo);
            if (TreadlyDeviceConnectStatusFragment.this.targetConnectDeviceName != null && deviceInfo.getName() != null && TreadlyDeviceConnectStatusFragment.this.targetConnectDeviceName.equals(deviceInfo.getName())) {
                TreadlyClientLib.shared.connectDevice(deviceInfo);
                TreadlyDeviceConnectStatusFragment.this.targetConnectDeviceName = null;
            }
            if (deviceInfo == null || deviceInfo.getName() == null) {
                return;
            }
            ActivityUtil.runOnUiThread(TreadlyDeviceConnectStatusFragment.this.getActivity(), new Runnable() { // from class: com.treadly.Treadly.UI.TreadlyConnect.DeviceConnect.-$$Lambda$TreadlyDeviceConnectStatusFragment$6$wGDT1kXQEqaJQVRGOq69QKJPDiA
                @Override // java.lang.Runnable
                public final void run() {
                    TreadlyDeviceConnectStatusFragment.AnonymousClass6.lambda$onDeviceConnectionDeviceDiscovered$1(TreadlyDeviceConnectStatusFragment.AnonymousClass6.this, deviceInfo);
                }
            });
        }

        public static /* synthetic */ void lambda$onDeviceConnectionDeviceDiscovered$1(AnonymousClass6 anonymousClass6, DeviceInfo deviceInfo) {
            if (TreadlyDeviceConnectStatusFragment.this.adapter.addDevice(deviceInfo)) {
                TreadlyDeviceConnectStatusFragment.this.updateViews();
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void nextPressed(View view) {
        nextPressed();
    }

    private void nextPressed() {
        if (!this.isScrolling && this.currentIndex < Math.max(this.adapter.getItemCount() + 1, 0)) {
            this.isScrolling = true;
            this.navButtonPressed = true;
            this.currentIndex++;
            ActivityUtil.runOnUiThread(getActivity(), new Runnable() { // from class: com.treadly.Treadly.UI.TreadlyConnect.DeviceConnect.-$$Lambda$TreadlyDeviceConnectStatusFragment$Hov0yDvzSMtUcvpatvE3MlYVxiU
                @Override // java.lang.Runnable
                public final void run() {
                    TreadlyDeviceConnectStatusFragment.lambda$nextPressed$3(TreadlyDeviceConnectStatusFragment.this);
                }
            });
        }
    }

    public static /* synthetic */ void lambda$nextPressed$3(TreadlyDeviceConnectStatusFragment treadlyDeviceConnectStatusFragment) {
        treadlyDeviceConnectStatusFragment.deviceListView.smoothScrollToPosition(treadlyDeviceConnectStatusFragment.currentIndex);
        treadlyDeviceConnectStatusFragment.updateViews();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void previousPressed(View view) {
        previousPressed();
    }

    private void previousPressed() {
        if (!this.isScrolling && this.currentIndex > 0) {
            this.isScrolling = true;
            this.navButtonPressed = true;
            this.currentIndex--;
            ActivityUtil.runOnUiThread(getActivity(), new Runnable() { // from class: com.treadly.Treadly.UI.TreadlyConnect.DeviceConnect.-$$Lambda$TreadlyDeviceConnectStatusFragment$lYepS3wE7mqocEA9D6H1BCQ09gw
                @Override // java.lang.Runnable
                public final void run() {
                    TreadlyDeviceConnectStatusFragment.lambda$previousPressed$4(TreadlyDeviceConnectStatusFragment.this);
                }
            });
        }
    }

    public static /* synthetic */ void lambda$previousPressed$4(TreadlyDeviceConnectStatusFragment treadlyDeviceConnectStatusFragment) {
        treadlyDeviceConnectStatusFragment.deviceListView.smoothScrollToPosition(treadlyDeviceConnectStatusFragment.currentIndex);
        treadlyDeviceConnectStatusFragment.updateViews();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void updateNavArrows() {
        if (this.adapter.getItemCount() <= 1) {
            this.rightNavArrow.setVisibility(8);
            this.leftNavArrow.setVisibility(8);
        } else if (this.currentIndex == this.adapter.getItemCount() - 1) {
            this.rightNavArrow.setVisibility(0);
            this.leftNavArrow.setVisibility(0);
            this.rightNavArrow.setColorFilter(getResources().getColor(R.color.dark_2, null));
            this.leftNavArrow.setColorFilter(getResources().getColor(R.color.white, null));
        } else if (this.currentIndex == 0) {
            this.rightNavArrow.setVisibility(0);
            this.leftNavArrow.setVisibility(0);
            this.rightNavArrow.setColorFilter(getResources().getColor(R.color.white, null));
            this.leftNavArrow.setColorFilter(getResources().getColor(R.color.dark_2, null));
        } else {
            this.rightNavArrow.setVisibility(0);
            this.leftNavArrow.setVisibility(0);
            this.rightNavArrow.setColorFilter(getResources().getColor(R.color.white, null));
            this.leftNavArrow.setColorFilter(getResources().getColor(R.color.white, null));
        }
    }

    void updateViews() {
        ActivityUtil.runOnUiThread(getActivity(), new Runnable() { // from class: com.treadly.Treadly.UI.TreadlyConnect.DeviceConnect.-$$Lambda$TreadlyDeviceConnectStatusFragment$vqvEmSz4YPVUCwrdTYjVxrzXi7w
            @Override // java.lang.Runnable
            public final void run() {
                TreadlyDeviceConnectStatusFragment.lambda$updateViews$5(TreadlyDeviceConnectStatusFragment.this);
            }
        });
    }

    public static /* synthetic */ void lambda$updateViews$5(TreadlyDeviceConnectStatusFragment treadlyDeviceConnectStatusFragment) {
        treadlyDeviceConnectStatusFragment.updateNavArrows();
        if (treadlyDeviceConnectStatusFragment.isSettleTimeout) {
            return;
        }
        treadlyDeviceConnectStatusFragment.noDevicesFoundView.setVisibility((treadlyDeviceConnectStatusFragment.isOnboarding || treadlyDeviceConnectStatusFragment.adapter.getItemCount() > 0) ? 4 : 0);
        if (treadlyDeviceConnectStatusFragment.isOnboarding && treadlyDeviceConnectStatusFragment.adapter.getItemCount() == 0) {
            treadlyDeviceConnectStatusFragment.toOnboardingNoDeviceFound();
        } else {
            treadlyDeviceConnectStatusFragment.dismissOnboardingNoDeviceFound();
        }
    }

    void toPairModeError(DeviceInfo deviceInfo) {
        TreadlyDeviceConnectStatusPairModeFragment treadlyDeviceConnectStatusPairModeFragment = new TreadlyDeviceConnectStatusPairModeFragment();
        treadlyDeviceConnectStatusPairModeFragment.deviceType = deviceInfo.getGen();
        addFragmentToStack((Fragment) treadlyDeviceConnectStatusPairModeFragment, true);
    }

    void scanBondedBluetoothDevices() {
        this.mBtAdapter.getBondedDevices().forEach(new Consumer() { // from class: com.treadly.Treadly.UI.TreadlyConnect.DeviceConnect.-$$Lambda$TreadlyDeviceConnectStatusFragment$rqI8yZ8Y-regDcRQzBSj5SJd_l4
            @Override // java.util.function.Consumer
            public final void accept(Object obj) {
                TreadlyDeviceConnectStatusFragment.lambda$scanBondedBluetoothDevices$6(TreadlyDeviceConnectStatusFragment.this, (BluetoothDevice) obj);
            }
        });
    }

    public static /* synthetic */ void lambda$scanBondedBluetoothDevices$6(TreadlyDeviceConnectStatusFragment treadlyDeviceConnectStatusFragment, BluetoothDevice bluetoothDevice) {
        if (!treadlyDeviceConnectStatusFragment.adapter.isTreadlyAudioDevice(bluetoothDevice.getName()) || treadlyDeviceConnectStatusFragment.adapter.audioDeviceList.contains(bluetoothDevice)) {
            return;
        }
        treadlyDeviceConnectStatusFragment.adapter.audioDeviceList.add(bluetoothDevice);
    }

    void connectAudioDevice(String str) {
        if (this.adapter.audioDeviceList == null) {
            return;
        }
        for (BluetoothDevice bluetoothDevice : this.adapter.audioDeviceList) {
            if (str.equals(bluetoothDevice.getName())) {
                try {
                    this.mA2dpService.getClass().getMethod("connect", BluetoothDevice.class).invoke(this.mA2dpService, bluetoothDevice);
                    return;
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        openBluetoothMenu();
    }

    void openBluetoothMenu() {
        Intent intent = new Intent();
        intent.setAction("android.settings.BLUETOOTH_SETTINGS");
        startActivity(intent);
    }

    void updatePassword(int[] iArr) {
        this.btAudioPassword = "";
        for (int i : iArr) {
            this.btAudioPassword = this.btAudioPassword.concat(String.valueOf(i));
        }
    }

    void disconnectAudioDevice(String str) {
        if (this.adapter.connectAudioDevice == null || !this.adapter.connectAudioDevice.getName().equals(str)) {
            return;
        }
        try {
            this.mA2dpService.getClass().getMethod("disconnect", BluetoothDevice.class).invoke(this.mA2dpService, this.adapter.connectAudioDevice);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* renamed from: com.treadly.Treadly.UI.TreadlyConnect.DeviceConnect.TreadlyDeviceConnectStatusFragment$7  reason: invalid class name */
    /* loaded from: classes2.dex */
    public class AnonymousClass7 extends BroadcastReceiver {
        AnonymousClass7() {
        }

        @Override // android.content.BroadcastReceiver
        public void onReceive(Context context, final Intent intent) {
            if (intent.getAction().equals("android.bluetooth.a2dp.profile.action.CONNECTION_STATE_CHANGED")) {
                int intExtra = intent.getIntExtra("android.bluetooth.profile.extra.STATE", 0);
                if (intExtra == 2) {
                    ActivityUtil.runOnUiThread(TreadlyDeviceConnectStatusFragment.this.getActivity(), new Runnable() { // from class: com.treadly.Treadly.UI.TreadlyConnect.DeviceConnect.-$$Lambda$TreadlyDeviceConnectStatusFragment$7$1e2MYDHmbCuZh8C10uWI-PqXnjM
                        @Override // java.lang.Runnable
                        public final void run() {
                            TreadlyDeviceConnectStatusFragment.AnonymousClass7.lambda$onReceive$0(TreadlyDeviceConnectStatusFragment.AnonymousClass7.this, intent);
                        }
                    });
                } else if (intExtra == 0) {
                    ActivityUtil.runOnUiThread(TreadlyDeviceConnectStatusFragment.this.getActivity(), new Runnable() { // from class: com.treadly.Treadly.UI.TreadlyConnect.DeviceConnect.-$$Lambda$TreadlyDeviceConnectStatusFragment$7$Qmijqf0xMQDMLJykXSIlyvEK82g
                        @Override // java.lang.Runnable
                        public final void run() {
                            TreadlyDeviceConnectStatusFragment.AnonymousClass7.lambda$onReceive$1(TreadlyDeviceConnectStatusFragment.AnonymousClass7.this);
                        }
                    });
                }
            }
        }

        public static /* synthetic */ void lambda$onReceive$0(AnonymousClass7 anonymousClass7, Intent intent) {
            TreadlyDeviceConnectStatusFragment.this.adapter.connectAudioDevice = (BluetoothDevice) intent.getParcelableExtra("android.bluetooth.device.extra.DEVICE");
            TreadlyDeviceConnectStatusFragment.this.adapter.notifyDataSetChanged();
        }

        public static /* synthetic */ void lambda$onReceive$1(AnonymousClass7 anonymousClass7) {
            TreadlyDeviceConnectStatusFragment.this.adapter.connectAudioDevice = null;
            TreadlyDeviceConnectStatusFragment.this.adapter.notifyDataSetChanged();
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* renamed from: com.treadly.Treadly.UI.TreadlyConnect.DeviceConnect.TreadlyDeviceConnectStatusFragment$8  reason: invalid class name */
    /* loaded from: classes2.dex */
    public class AnonymousClass8 implements BluetoothProfile.ServiceListener {
        AnonymousClass8() {
        }

        @Override // android.bluetooth.BluetoothProfile.ServiceListener
        public void onServiceConnected(int i, BluetoothProfile bluetoothProfile) {
            if (i == 2) {
                TreadlyDeviceConnectStatusFragment.this.mA2dpService = (BluetoothA2dp) bluetoothProfile;
                TreadlyDeviceConnectStatusFragment.this.mA2dpService.getConnectedDevices().forEach(new Consumer() { // from class: com.treadly.Treadly.UI.TreadlyConnect.DeviceConnect.-$$Lambda$TreadlyDeviceConnectStatusFragment$8$FArRbY5Rcs_eezP6ES-8bPdEbBE
                    @Override // java.util.function.Consumer
                    public final void accept(Object obj) {
                        TreadlyDeviceConnectStatusFragment.AnonymousClass8.lambda$onServiceConnected$0(TreadlyDeviceConnectStatusFragment.AnonymousClass8.this, (BluetoothDevice) obj);
                    }
                });
            }
        }

        public static /* synthetic */ void lambda$onServiceConnected$0(AnonymousClass8 anonymousClass8, BluetoothDevice bluetoothDevice) {
            if (bluetoothDevice.getName() != null) {
                TreadlyDeviceConnectStatusFragment.this.adapter.connectAudioDevice = bluetoothDevice;
            }
        }

        @Override // android.bluetooth.BluetoothProfile.ServiceListener
        public void onServiceDisconnected(int i) {
            if (i == 2) {
                TreadlyDeviceConnectStatusFragment.this.adapter.connectAudioDevice = null;
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void processOnboardingConnected(DeviceInfo deviceInfo) {
        if (TreadlyClientLib.shared.getDeviceComponentList()) {
            this.deviceName = deviceInfo.getName();
            this.onboardDeviceInfo = deviceInfo;
            SharedPreferences.shared.storeConnectedDeviceName(this.deviceName);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void setAuthenticationState(AuthenticationState authenticationState) {
        if (this.isOnboarding) {
            this.authState = authenticationState;
            if (this.authState == null || this.authState == AuthenticationState.active) {
                return;
            }
            TreadlyActivationManager.shared.handleAuthenticationState(this.deviceName, AuthenticationState.active);
            initializeSingleUserMode();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void setAuthenticationState() {
        if (this.isOnboarding && this.bleComponent != null) {
            if (this.bleComponent.getVersionInfo() != null && this.bleComponent.getVersionInfo().getVersionInfo().isGen2()) {
                TreadlyClientLib.shared.getAuthenticationState();
                return;
            }
            TreadlyActivationManager.shared.handleAuthenticationState(this.deviceName, AuthenticationState.active);
            initializeSingleUserMode();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void initializeSingleUserMode() {
        if (!this.hasSingleUserMode) {
            TreadlyServiceManager.getInstance().isOnboarding = false;
            if (TreadlyClientLib.shared.isDeviceConnected()) {
                TreadlyClientLib.shared.disconnect();
            }
            startOnboardingContinueTimer();
        } else if (this.singleUserModeDisplayed) {
        } else {
            this.singleUserModeDisplayed = true;
            this.connectButtonPressed = false;
            final TreadlyOnboardingSingleUserFragment treadlyOnboardingSingleUserFragment = new TreadlyOnboardingSingleUserFragment();
            treadlyOnboardingSingleUserFragment.onboarding = true;
            treadlyOnboardingSingleUserFragment.deviceAddress = this.deviceAddress;
            treadlyOnboardingSingleUserFragment.hasWifiConfiguration = this.hasWifiConfiguration;
            treadlyOnboardingSingleUserFragment.connectedDeviceInfo = this.onboardDeviceInfo;
            TreadlyServiceManager.getInstance().getSingleUserMode(this.deviceAddress, new TreadlyServiceResponseEventAdapter() { // from class: com.treadly.Treadly.UI.TreadlyConnect.DeviceConnect.TreadlyDeviceConnectStatusFragment.10
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
                    TreadlyDeviceConnectStatusFragment.this.stopScan();
                    TreadlyDeviceConnectStatusFragment.this.haveReturned = false;
                    TreadlyDeviceConnectStatusFragment.this.addFragmentToStack(treadlyOnboardingSingleUserFragment, TreadlyOnboardingSingleUserFragment.TAG, TreadlyDeviceConnectStatusFragment.TAG, true);
                }
            });
        }
    }

    private void startOnboardingContinueTimer() {
        stopOnboardingContinueTimer();
        this.onboardingContinueTimer = new Timer();
        this.onboardingContinueTimer.schedule(new TimerTask() { // from class: com.treadly.Treadly.UI.TreadlyConnect.DeviceConnect.TreadlyDeviceConnectStatusFragment.11
            @Override // java.util.TimerTask, java.lang.Runnable
            public void run() {
                TreadlyDeviceConnectStatusFragment.this.finishOnboarding();
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
        TreadlyServiceManager.getInstance().updateOnboardingStatus(new TreadlyServiceResponseEventAdapter() { // from class: com.treadly.Treadly.UI.TreadlyConnect.DeviceConnect.TreadlyDeviceConnectStatusFragment.12
            @Override // com.treadly.Treadly.Data.Listeners.TreadlyServiceResponseEventAdapter, com.treadly.Treadly.Data.Listeners.TreadlyServiceResponseEventListener
            public void onSuccess(String str) {
                super.onSuccess(str);
                MainActivity mainActivity = (MainActivity) TreadlyDeviceConnectStatusFragment.this.getActivity();
                if (str != null || mainActivity == null) {
                    TreadlyDeviceConnectStatusFragment.this.pendingChanges = false;
                    TreadlyDeviceConnectStatusFragment.this.showBaseAlert("Oops!", "There was an error. Please try again");
                    return;
                }
                TreadlyDeviceConnectStatusFragment.this.clearBackStack();
                mainActivity.toConnect();
            }
        });
    }

    private void toOnboardingNoDeviceFound() {
        if (!this.isOnboarding || this.displayingOnboardingNoDeviceFound) {
            return;
        }
        this.displayingOnboardingNoDeviceFound = true;
        TreadlyOnboardingNotFoundFragment treadlyOnboardingNotFoundFragment = new TreadlyOnboardingNotFoundFragment();
        treadlyOnboardingNotFoundFragment.inviteCodeListener = new TreadlyOnboardingNotFoundFragment.InviteCodeListener() { // from class: com.treadly.Treadly.UI.TreadlyConnect.DeviceConnect.-$$Lambda$TreadlyDeviceConnectStatusFragment$Dvcky-MVNrppE4vDofT86OSYGrw
            @Override // com.treadly.Treadly.UI.Onboarding.NotFound.TreadlyOnboardingNotFoundFragment.InviteCodeListener
            public final void onInviteCodePressed() {
                TreadlyDeviceConnectStatusFragment.this.finishOnboarding();
            }
        };
        treadlyOnboardingNotFoundFragment.tryAgainListener = new TreadlyOnboardingNotFoundFragment.TryAgainListener() { // from class: com.treadly.Treadly.UI.TreadlyConnect.DeviceConnect.-$$Lambda$d3Wb5EVPtZCSbG5SdsJcSDB0jJY
            @Override // com.treadly.Treadly.UI.Onboarding.NotFound.TreadlyOnboardingNotFoundFragment.TryAgainListener
            public final void onTryAgainPressed() {
                TreadlyDeviceConnectStatusFragment.this.popBackStack();
            }
        };
        addFragmentToStack((Fragment) treadlyOnboardingNotFoundFragment, true);
    }

    private void dismissOnboardingNoDeviceFound() {
        if (this.displayingOnboardingNoDeviceFound) {
            popBackStack();
            this.displayingOnboardingNoDeviceFound = false;
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* renamed from: com.treadly.Treadly.UI.TreadlyConnect.DeviceConnect.TreadlyDeviceConnectStatusFragment$13  reason: invalid class name */
    /* loaded from: classes2.dex */
    public class AnonymousClass13 implements DeviceActivationEventListener {
        AnonymousClass13() {
        }

        @Override // com.treadly.client.lib.sdk.Listeners.DeviceActivationEventListener
        public void onDeviceActivationStartResponse(final ActivationInfo activationInfo) {
            String str;
            if (TreadlyDeviceConnectStatusFragment.this.isOnboarding && !TreadlyDeviceConnectStatusFragment.this.isActivationInProgress) {
                if (activationInfo != null) {
                    TreadlyDeviceConnectStatusFragment.this.activationInfo = activationInfo;
                    TreadlyDeviceConnectStatusFragment.this.startActivationAlert.setTitle(activationInfo.activationTitle);
                    TreadlyDeviceConnectStatusFragment.this.startActivationAlert.setMessage(activationInfo.activationMessage);
                    TreadlyDeviceConnectStatusFragment.this.startActivationAlert.setButton(-1, "Activate", new DialogInterface.OnClickListener() { // from class: com.treadly.Treadly.UI.TreadlyConnect.DeviceConnect.-$$Lambda$TreadlyDeviceConnectStatusFragment$13$h82jcPbm4TNT_6HLvglkljnq1wg
                        @Override // android.content.DialogInterface.OnClickListener
                        public final void onClick(DialogInterface dialogInterface, int i) {
                            TreadlyDeviceConnectStatusFragment.AnonymousClass13.lambda$onDeviceActivationStartResponse$1(TreadlyDeviceConnectStatusFragment.AnonymousClass13.this, dialogInterface, i);
                        }
                    });
                    TreadlyDeviceConnectStatusFragment.this.startActivationAlert.setButton(-2, "Dismiss", new DialogInterface.OnClickListener() { // from class: com.treadly.Treadly.UI.TreadlyConnect.DeviceConnect.-$$Lambda$TreadlyDeviceConnectStatusFragment$13$BdmTTPZB-H7MkF2O3qTLvu_U7lc
                        @Override // android.content.DialogInterface.OnClickListener
                        public final void onClick(DialogInterface dialogInterface, int i) {
                            TreadlyDeviceConnectStatusFragment.AnonymousClass13.lambda$onDeviceActivationStartResponse$2(TreadlyDeviceConnectStatusFragment.AnonymousClass13.this, dialogInterface, i);
                        }
                    });
                    ActivityUtil.runOnUiThread(TreadlyDeviceConnectStatusFragment.this.getActivity(), new Runnable() { // from class: com.treadly.Treadly.UI.TreadlyConnect.DeviceConnect.-$$Lambda$TreadlyDeviceConnectStatusFragment$13$stgPFYuatdk-2Oiw0QfH2ncXLO0
                        @Override // java.lang.Runnable
                        public final void run() {
                            TreadlyDeviceConnectStatusFragment.this.startActivationAlert.show();
                        }
                    });
                    String str2 = TreadlyDeviceConnectStatusFragment.this.activationInfo.activationSubmitMessage;
                    if (!TreadlyClientLib.shared.isDeviceStatusConnected()) {
                        if (TreadlyDeviceConnectStatusFragment.this.bleComponent != null) {
                            if (TreadlyDeviceConnectStatusFragment.this.bleComponent.getVersionInfo().getVersionInfo().isGen2()) {
                                str = str2 + "\n\nStatus: Disconnected\nPlease reconnect";
                            } else {
                                str = str2 + "\n\nStatus: Disconnected\nPlease tap 3 times in the constant section to start the pairing process";
                            }
                        } else {
                            str = str2 + "\n\nStatus: Disconnected\nPlease reconnect";
                        }
                    } else {
                        str = str2 + "\n\nStatus: Connected";
                    }
                    TreadlyDeviceConnectStatusFragment.this.submitActivationAlert.setTitle(activationInfo.activationSubmitTitle);
                    TreadlyDeviceConnectStatusFragment.this.submitActivationAlert.setMessage(str);
                    TreadlyDeviceConnectStatusFragment.this.submitActivationAlert.setButton(-3, "Dismiss", new DialogInterface.OnClickListener() { // from class: com.treadly.Treadly.UI.TreadlyConnect.DeviceConnect.-$$Lambda$TreadlyDeviceConnectStatusFragment$13$GTYo58St3vHDIqQEVbaB5banqv0
                        @Override // android.content.DialogInterface.OnClickListener
                        public final void onClick(DialogInterface dialogInterface, int i) {
                            TreadlyDeviceConnectStatusFragment.AnonymousClass13.lambda$onDeviceActivationStartResponse$4(TreadlyDeviceConnectStatusFragment.AnonymousClass13.this, dialogInterface, i);
                        }
                    });
                    if (activationInfo.activationUrl != null && !activationInfo.activationUrl.isEmpty() && Uri.parse(activationInfo.activationUrl) != null) {
                        TreadlyDeviceConnectStatusFragment.this.submitActivationAlert.setButton(-3, "Open", new DialogInterface.OnClickListener() { // from class: com.treadly.Treadly.UI.TreadlyConnect.DeviceConnect.-$$Lambda$TreadlyDeviceConnectStatusFragment$13$lD_jC0XFk4GSxb5ux5yDm8oB27k
                            @Override // android.content.DialogInterface.OnClickListener
                            public final void onClick(DialogInterface dialogInterface, int i) {
                                TreadlyDeviceConnectStatusFragment.AnonymousClass13.lambda$onDeviceActivationStartResponse$5(TreadlyDeviceConnectStatusFragment.AnonymousClass13.this, activationInfo, dialogInterface, i);
                            }
                        });
                    }
                    if (TreadlyDeviceConnectStatusFragment.this.getContext() != null) {
                        final AppCompatEditText appCompatEditText = new AppCompatEditText(TreadlyDeviceConnectStatusFragment.this.getContext());
                        appCompatEditText.setHint("Enter activation code");
                        appCompatEditText.addTextChangedListener(TreadlyDeviceConnectStatusFragment.this.textWatcher);
                        TreadlyDeviceConnectStatusFragment.this.submitActivationAlert.setView(appCompatEditText);
                        TreadlyDeviceConnectStatusFragment.this.submitActivationAlert.setButton(-1, "Activate", new DialogInterface.OnClickListener() { // from class: com.treadly.Treadly.UI.TreadlyConnect.DeviceConnect.-$$Lambda$TreadlyDeviceConnectStatusFragment$13$LvtbHpwcC5Dj79Oi5bTjhaig9Bg
                            @Override // android.content.DialogInterface.OnClickListener
                            public final void onClick(DialogInterface dialogInterface, int i) {
                                TreadlyDeviceConnectStatusFragment.AnonymousClass13.lambda$onDeviceActivationStartResponse$6(TreadlyDeviceConnectStatusFragment.AnonymousClass13.this, appCompatEditText, dialogInterface, i);
                            }
                        });
                        TreadlyDeviceConnectStatusFragment.this.updateActivationAction();
                        TreadlyDeviceConnectStatusFragment.this.completeActivationAlert.setTitle(activationInfo.activationSuccessTitle);
                        TreadlyDeviceConnectStatusFragment.this.completeActivationAlert.setMessage(activationInfo.activationSuccessMessage);
                        TreadlyDeviceConnectStatusFragment.this.completeActivationAlert.setButton(-3, "Dismiss", new DialogInterface.OnClickListener() { // from class: com.treadly.Treadly.UI.TreadlyConnect.DeviceConnect.-$$Lambda$TreadlyDeviceConnectStatusFragment$13$goinKTYO9bIELb7KYtwtUqsTUNc
                            @Override // android.content.DialogInterface.OnClickListener
                            public final void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.dismiss();
                            }
                        });
                        TreadlyDeviceConnectStatusFragment.this.errorActivationAlert.setTitle(activationInfo.activationErrorTitle);
                        TreadlyDeviceConnectStatusFragment.this.errorActivationAlert.setMessage(activationInfo.activationErrorMessage);
                        TreadlyDeviceConnectStatusFragment.this.errorActivationAlert.setButton(-3, "Dismiss", new DialogInterface.OnClickListener() { // from class: com.treadly.Treadly.UI.TreadlyConnect.DeviceConnect.-$$Lambda$TreadlyDeviceConnectStatusFragment$13$GnegY3cAnebVP-fLq7PBh_8a3ks
                            @Override // android.content.DialogInterface.OnClickListener
                            public final void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.dismiss();
                            }
                        });
                        TreadlyDeviceConnectStatusFragment.this.finishActivationAlert.setTitle("Activation");
                        TreadlyDeviceConnectStatusFragment.this.finishActivationAlert.setMessage("Finishing activation. Please wait");
                        return;
                    }
                    return;
                }
                TreadlyDeviceConnectStatusFragment.this.showBaseAlert("Error", "Error starting activation process");
            }
        }

        public static /* synthetic */ void lambda$onDeviceActivationStartResponse$1(final AnonymousClass13 anonymousClass13, DialogInterface dialogInterface, int i) {
            TreadlyDeviceConnectStatusFragment.this.isActivationInProgress = true;
            ActivityUtil.runOnUiThread(TreadlyDeviceConnectStatusFragment.this.getActivity(), new Runnable() { // from class: com.treadly.Treadly.UI.TreadlyConnect.DeviceConnect.-$$Lambda$TreadlyDeviceConnectStatusFragment$13$P0P7OUS3YJone5irKOyNp-kJzd8
                @Override // java.lang.Runnable
                public final void run() {
                    TreadlyDeviceConnectStatusFragment.AnonymousClass13.lambda$null$0(TreadlyDeviceConnectStatusFragment.AnonymousClass13.this);
                }
            });
        }

        public static /* synthetic */ void lambda$null$0(AnonymousClass13 anonymousClass13) {
            TreadlyDeviceConnectStatusFragment.this.submitActivationAlert.show();
            TreadlyDeviceConnectStatusFragment.this.updateActivationAlert();
        }

        public static /* synthetic */ void lambda$onDeviceActivationStartResponse$2(AnonymousClass13 anonymousClass13, DialogInterface dialogInterface, int i) {
            TreadlyDeviceConnectStatusFragment.this.startActivationAlert.dismiss();
            TreadlyDeviceConnectStatusFragment.this.isActivationInProgress = false;
        }

        public static /* synthetic */ void lambda$onDeviceActivationStartResponse$4(AnonymousClass13 anonymousClass13, DialogInterface dialogInterface, int i) {
            TreadlyDeviceConnectStatusFragment.this.submitActivationAlert.dismiss();
            TreadlyDeviceConnectStatusFragment.this.submitActivationAlert = null;
            TreadlyDeviceConnectStatusFragment.this.isActivationInProgress = false;
            TreadlyDeviceConnectStatusFragment.this.initializeSingleUserMode();
        }

        public static /* synthetic */ void lambda$onDeviceActivationStartResponse$5(AnonymousClass13 anonymousClass13, ActivationInfo activationInfo, DialogInterface dialogInterface, int i) {
            Intent intent = new Intent();
            intent.setAction("android.intent.action.VIEW");
            intent.addCategory("android.intent.category.BROWSABLE");
            intent.setData(Uri.parse(activationInfo.activationUrl));
            TreadlyDeviceConnectStatusFragment.this.startActivity(intent);
        }

        public static /* synthetic */ void lambda$onDeviceActivationStartResponse$6(AnonymousClass13 anonymousClass13, AppCompatEditText appCompatEditText, DialogInterface dialogInterface, int i) {
            if (appCompatEditText.getText() != null && TreadlyDeviceConnectStatusFragment.this.bleComponent != null) {
                TreadlyClientLib.shared.submitActivationCode(appCompatEditText.getText().toString(), TreadlyDeviceConnectStatusFragment.this.bleComponent);
            }
            TreadlyDeviceConnectStatusFragment.this.submitActivationAlert.dismiss();
            TreadlyDeviceConnectStatusFragment.this.submitActivationAlert = null;
        }

        @Override // com.treadly.client.lib.sdk.Listeners.DeviceActivationEventListener
        public void onDeviceActivationSubmitResponse(boolean z) {
            if (z) {
                TreadlyDeviceConnectStatusFragment.this.pendingActivationBleEnable = true;
                TreadlyDeviceConnectStatusFragment.this.setAuthenticationState();
                return;
            }
            TreadlyDeviceConnectStatusFragment.this.pendingActivationBleEnable = false;
            if (TreadlyDeviceConnectStatusFragment.this.errorActivationAlert == null) {
                return;
            }
            TreadlyDeviceConnectStatusFragment.this.errorActivationAlert.setOnShowListener(new DialogInterface.OnShowListener() { // from class: com.treadly.Treadly.UI.TreadlyConnect.DeviceConnect.-$$Lambda$TreadlyDeviceConnectStatusFragment$13$86OPldzhUVz1mG29juP-d1KTolQ
                @Override // android.content.DialogInterface.OnShowListener
                public final void onShow(DialogInterface dialogInterface) {
                    TreadlyDeviceConnectStatusFragment.this.isActivationInProgress = false;
                }
            });
            ActivityUtil.runOnUiThread(TreadlyDeviceConnectStatusFragment.this.getActivity(), new Runnable() { // from class: com.treadly.Treadly.UI.TreadlyConnect.DeviceConnect.-$$Lambda$TreadlyDeviceConnectStatusFragment$13$GZKt1XAPdrUl-_RsUmECqE8RQaY
                @Override // java.lang.Runnable
                public final void run() {
                    TreadlyDeviceConnectStatusFragment.this.errorActivationAlert.show();
                }
            });
        }

        @Override // com.treadly.client.lib.sdk.Listeners.DeviceActivationEventListener
        public void onDeviceAuthenticateReferenceCodeInfoResponse(final boolean z, final AuthenticateReferenceCodeInfo authenticateReferenceCodeInfo) {
            ActivityUtil.runOnUiThread(TreadlyDeviceConnectStatusFragment.this.getActivity(), new Runnable() { // from class: com.treadly.Treadly.UI.TreadlyConnect.DeviceConnect.-$$Lambda$TreadlyDeviceConnectStatusFragment$13$qZgzmNSp-956ReE8ktPJTgNZSc0
                @Override // java.lang.Runnable
                public final void run() {
                    TreadlyDeviceConnectStatusFragment.AnonymousClass13.lambda$onDeviceAuthenticateReferenceCodeInfoResponse$11(TreadlyDeviceConnectStatusFragment.AnonymousClass13.this, z, authenticateReferenceCodeInfo);
                }
            });
        }

        public static /* synthetic */ void lambda$onDeviceAuthenticateReferenceCodeInfoResponse$11(AnonymousClass13 anonymousClass13, boolean z, AuthenticateReferenceCodeInfo authenticateReferenceCodeInfo) {
            if (!z || authenticateReferenceCodeInfo == null || authenticateReferenceCodeInfo.referenceCode == null) {
                TreadlyDeviceConnectStatusFragment.this.presentedUnprocessedDialog = false;
            } else {
                AlertDialog.Builder builder = new AlertDialog.Builder(TreadlyDeviceConnectStatusFragment.this.getContext());
                if (authenticateReferenceCodeInfo.title != null) {
                    builder.setTitle(authenticateReferenceCodeInfo.title);
                }
                if (authenticateReferenceCodeInfo.message != null) {
                    builder.setMessage(authenticateReferenceCodeInfo.message);
                }
                builder.setNegativeButton("Dismiss", (DialogInterface.OnClickListener) null);
                builder.create().show();
            }
            TreadlyClientLib.shared.disconnect();
            TreadlyActivationManager.shared.reset();
        }

        @Override // com.treadly.client.lib.sdk.Listeners.DeviceActivationEventListener
        public void onDeviceAuthenticationStateResponse(AuthenticationState authenticationState) {
            ActivityUtil.runOnUiThread(TreadlyDeviceConnectStatusFragment.this.getActivity(), new Runnable() { // from class: com.treadly.Treadly.UI.TreadlyConnect.DeviceConnect.-$$Lambda$TreadlyDeviceConnectStatusFragment$13$EhbVY8WgVvun9dlYZjyPVu9ppIY
                @Override // java.lang.Runnable
                public final void run() {
                    TreadlyDeviceConnectStatusFragment.AnonymousClass13.lambda$onDeviceAuthenticationStateResponse$12(TreadlyDeviceConnectStatusFragment.AnonymousClass13.this);
                }
            });
        }

        public static /* synthetic */ void lambda$onDeviceAuthenticationStateResponse$12(AnonymousClass13 anonymousClass13) {
            TreadlyDeviceConnectStatusFragment.this.authenticationStateUpdated = true;
            TreadlyDeviceConnectStatusFragment.this.setAuthenticationState();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void updateActivationAlert() {
        String str;
        if (this.isActivationInProgress) {
            DeviceStatus deviceStatus = this.currentDeviceStatusInfo;
            boolean isDeviceConnected = TreadlyClientLib.shared.isDeviceConnected();
            if (this.submitActivationAlert != null) {
                updateActivationAction();
                String str2 = this.activationInfo.activationSubmitMessage;
                if (!isDeviceConnected) {
                    if (this.bleComponent == null) {
                        str = str2 + "\n\nStatus: Disconnected\nPlease reconnect";
                    } else if (this.bleComponent.getVersionInfo().getVersionInfo().isGen2()) {
                        str = str2 + "\n\nStatus: Disconnected\nPlease reconnect";
                    } else {
                        str = str2 + "\n\nStatus: Disconnected\nPlease tap 3 times in the constant section to start the pairing process";
                    }
                } else {
                    str = str2 + "\n\nStatus: Connected";
                }
                this.submitActivationAlert.setMessage(str);
            }
            if (this.pendingActivationBleEnable && !isDeviceConnected && this.errorActivationAlert != null) {
                this.errorActivationAlert.setOnShowListener(new DialogInterface.OnShowListener() { // from class: com.treadly.Treadly.UI.TreadlyConnect.DeviceConnect.-$$Lambda$TreadlyDeviceConnectStatusFragment$UAKyPJnASWa_zAPC3odP59i_7Sc
                    @Override // android.content.DialogInterface.OnShowListener
                    public final void onShow(DialogInterface dialogInterface) {
                        TreadlyDeviceConnectStatusFragment.lambda$updateActivationAlert$7(TreadlyDeviceConnectStatusFragment.this, dialogInterface);
                    }
                });
                ActivityUtil.runOnUiThread(getActivity(), new Runnable() { // from class: com.treadly.Treadly.UI.TreadlyConnect.DeviceConnect.-$$Lambda$TreadlyDeviceConnectStatusFragment$Hpwa2oOBEd9y0foj7LgnfQeCR0Y
                    @Override // java.lang.Runnable
                    public final void run() {
                        TreadlyDeviceConnectStatusFragment.this.errorActivationAlert.show();
                    }
                });
            }
            if (this.pendingActivationBleEnable) {
                if (this.bleComponent != null && this.bleComponent.getVersionInfo().getVersionInfo().isGen2()) {
                    if (this.authState == AuthenticationState.active) {
                        if (this.completeActivationAlert != null && !this.completeActivationAlert.isShowing()) {
                            this.completeActivationAlert.setOnShowListener(new DialogInterface.OnShowListener() { // from class: com.treadly.Treadly.UI.TreadlyConnect.DeviceConnect.-$$Lambda$TreadlyDeviceConnectStatusFragment$0xTZZdIel_Egtjdt-NgLYgkP8KE
                                @Override // android.content.DialogInterface.OnShowListener
                                public final void onShow(DialogInterface dialogInterface) {
                                    TreadlyDeviceConnectStatusFragment.this.pendingActivationBleEnable = false;
                                }
                            });
                            ActivityUtil.runOnUiThread(getActivity(), new Runnable() { // from class: com.treadly.Treadly.UI.TreadlyConnect.DeviceConnect.-$$Lambda$TreadlyDeviceConnectStatusFragment$9rXd5RAKVTC4O1GCnGc7DOnCWAE
                                @Override // java.lang.Runnable
                                public final void run() {
                                    TreadlyDeviceConnectStatusFragment.this.completeActivationAlert.show();
                                }
                            });
                        }
                        this.isActivationInProgress = false;
                    }
                } else if (deviceStatus == null || !deviceStatus.isBleEnabled()) {
                } else {
                    if (this.completeActivationAlert != null && !this.completeActivationAlert.isShowing()) {
                        this.completeActivationAlert.setOnShowListener(new DialogInterface.OnShowListener() { // from class: com.treadly.Treadly.UI.TreadlyConnect.DeviceConnect.-$$Lambda$TreadlyDeviceConnectStatusFragment$sjF_IjCV8k2RuIwO-ziwZBr5wn4
                            @Override // android.content.DialogInterface.OnShowListener
                            public final void onShow(DialogInterface dialogInterface) {
                                TreadlyDeviceConnectStatusFragment.this.pendingActivationBleEnable = false;
                            }
                        });
                        ActivityUtil.runOnUiThread(getActivity(), new Runnable() { // from class: com.treadly.Treadly.UI.TreadlyConnect.DeviceConnect.-$$Lambda$TreadlyDeviceConnectStatusFragment$QnV3Sd869TwHhmlCyQaL32OX8aI
                            @Override // java.lang.Runnable
                            public final void run() {
                                TreadlyDeviceConnectStatusFragment.this.completeActivationAlert.show();
                            }
                        });
                    }
                    this.isActivationInProgress = false;
                }
            }
        }
    }

    public static /* synthetic */ void lambda$updateActivationAlert$7(TreadlyDeviceConnectStatusFragment treadlyDeviceConnectStatusFragment, DialogInterface dialogInterface) {
        treadlyDeviceConnectStatusFragment.pendingActivationBleEnable = false;
        treadlyDeviceConnectStatusFragment.isActivationInProgress = false;
    }

    void updateActivationAction() {
        if (this.submitActivationAlert.getButton(-1) != null) {
            this.submitActivationAlert.getButton(-1).setEnabled(TreadlyClientLib.shared.isDeviceConnected() && this.activationCodeTextFieldValid && this.bleComponent != null);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* renamed from: com.treadly.Treadly.UI.TreadlyConnect.DeviceConnect.TreadlyDeviceConnectStatusFragment$14  reason: invalid class name */
    /* loaded from: classes2.dex */
    public class AnonymousClass14 extends RequestEventAdapter {
        AnonymousClass14() {
        }

        @Override // com.treadly.client.lib.sdk.Listeners.RequestEventAdapter, com.treadly.client.lib.sdk.Listeners.RequestEventListener
        public void onRequestGetDeviceComponentsListResponse(boolean z, ComponentInfo[] componentInfoArr) {
            super.onRequestGetDeviceComponentsListResponse(z, componentInfoArr);
            if (z) {
                AppActivityManager.shared.componentList = Arrays.asList(componentInfoArr);
                TreadlyDeviceConnectStatusFragment.this.registerChecked = false;
                Arrays.asList(componentInfoArr).forEach(new Consumer() { // from class: com.treadly.Treadly.UI.TreadlyConnect.DeviceConnect.-$$Lambda$TreadlyDeviceConnectStatusFragment$14$EzzPNkdPewGndEffk5kzPeCHbGI
                    @Override // java.util.function.Consumer
                    public final void accept(Object obj) {
                        TreadlyDeviceConnectStatusFragment.AnonymousClass14.lambda$onRequestGetDeviceComponentsListResponse$0(TreadlyDeviceConnectStatusFragment.AnonymousClass14.this, (ComponentInfo) obj);
                    }
                });
            }
        }

        public static /* synthetic */ void lambda$onRequestGetDeviceComponentsListResponse$0(AnonymousClass14 anonymousClass14, ComponentInfo componentInfo) {
            if (componentInfo.getType() == ComponentType.bleBoard) {
                TreadlyDeviceConnectStatusFragment.this.deviceAddress = componentInfo.getId();
                if (TreadlyDeviceConnectStatusFragment.this.isOnboarding) {
                    TreadlyDeviceConnectStatusFragment.this.hasSingleUserMode = componentInfo.getVersionInfo().getVersionInfo().hasSingleUserMode();
                    TreadlyDeviceConnectStatusFragment.this.hasWifiConfiguration = componentInfo.getVersionInfo().getVersionInfo().hasWifiConfiguration();
                }
                TreadlyDeviceConnectStatusFragment.this.bleComponent = componentInfo;
                TreadlyDeviceConnectStatusFragment.this.setAuthenticationState();
            }
        }

        @Override // com.treadly.client.lib.sdk.Listeners.RequestEventAdapter, com.treadly.client.lib.sdk.Listeners.RequestEventListener
        public void onRequestGetAuthenticationState(final boolean z, final AuthenticationState authenticationState) {
            super.onRequestGetAuthenticationState(z, authenticationState);
            if (TreadlyDeviceConnectStatusFragment.this.isOnboarding) {
                ActivityUtil.runOnUiThread(TreadlyDeviceConnectStatusFragment.this.getActivity(), new Runnable() { // from class: com.treadly.Treadly.UI.TreadlyConnect.DeviceConnect.-$$Lambda$TreadlyDeviceConnectStatusFragment$14$lfmzXNBJDpk2Z42FhGGHfUEJPtY
                    @Override // java.lang.Runnable
                    public final void run() {
                        TreadlyDeviceConnectStatusFragment.AnonymousClass14.lambda$onRequestGetAuthenticationState$1(TreadlyDeviceConnectStatusFragment.AnonymousClass14.this, z, authenticationState);
                    }
                });
            }
        }

        public static /* synthetic */ void lambda$onRequestGetAuthenticationState$1(AnonymousClass14 anonymousClass14, boolean z, AuthenticationState authenticationState) {
            boolean z2 = false;
            if (!z || !TreadlyDeviceConnectStatusFragment.this.bleComponent.getVersionInfo().getVersionInfo().isGen2()) {
                if (z || !TreadlyDeviceConnectStatusFragment.this.bleComponent.getVersionInfo().getVersionInfo().isGen2()) {
                    z2 = true;
                } else {
                    TreadlyActivationManager.shared.handleAuthenticationState(TreadlyDeviceConnectStatusFragment.this.deviceName, AuthenticationState.disabled);
                    TreadlyDeviceConnectStatusFragment.this.setAuthenticationState(AuthenticationState.disabled);
                    TreadlyDeviceConnectStatusFragment.this.authenticationStateChecked = true;
                }
            } else {
                TreadlyActivationManager.shared.handleAuthenticationState(TreadlyDeviceConnectStatusFragment.this.deviceName, authenticationState);
                TreadlyDeviceConnectStatusFragment.this.setAuthenticationState(authenticationState);
                if (!TreadlyDeviceConnectStatusFragment.this.authenticationStateUpdated || authenticationState == AuthenticationState.active || authenticationState == AuthenticationState.unknown) {
                    z2 = true;
                } else {
                    TreadlyDeviceConnectStatusFragment.this.presentedActivationDialog = true;
                    TreadlyClientLib.shared.startActivation();
                }
                if (!TreadlyDeviceConnectStatusFragment.this.presentedUnprocessedDialog && TreadlyDeviceConnectStatusFragment.this.authenticationStateUpdated && authenticationState == AuthenticationState.unknown && TreadlyDeviceConnectStatusFragment.this.bleComponent != null) {
                    TreadlyDeviceConnectStatusFragment.this.presentedUnprocessedDialog = true;
                    TreadlyClientLib.shared.getAuthenticateReferenceCode(TreadlyDeviceConnectStatusFragment.this.bleComponent);
                }
                if (authenticationState == AuthenticationState.unknown) {
                    TreadlyActivationManager.shared.handleAuthenticationState(TreadlyDeviceConnectStatusFragment.this.deviceName, AuthenticationState.disabled);
                    TreadlyDeviceConnectStatusFragment.this.setAuthenticationState(AuthenticationState.disabled);
                }
            }
            if (z2) {
                TreadlyDeviceConnectStatusFragment.this.initializeSingleUserMode();
            }
        }

        @Override // com.treadly.client.lib.sdk.Listeners.RequestEventAdapter, com.treadly.client.lib.sdk.Listeners.RequestEventListener
        public void onRequestGetBtAudioPAssword(boolean z, int[] iArr) {
            super.onRequestGetBtAudioPAssword(z, iArr);
            if (z && iArr.length > 0) {
                TreadlyDeviceConnectStatusFragment.this.updatePassword(iArr);
            } else {
                TreadlyDeviceConnectStatusFragment.this.showBaseAlert("Error", "Error getting Bluetooth audio password");
            }
        }
    }
}
