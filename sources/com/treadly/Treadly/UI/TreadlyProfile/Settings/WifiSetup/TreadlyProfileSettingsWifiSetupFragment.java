package com.treadly.Treadly.UI.TreadlyProfile.Settings.WifiSetup;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.airbnb.lottie.LottieAnimationView;
import com.google.android.gms.common.internal.GmsClientSupervisor;
import com.treadly.Treadly.Data.Listeners.TreadlyServiceResponseEventAdapter;
import com.treadly.Treadly.Data.Managers.TreadlyServiceManager;
import com.treadly.Treadly.MainActivity;
import com.treadly.Treadly.R;
import com.treadly.Treadly.UI.FirmwareUpdate.FirmwareSsidListAdapter;
import com.treadly.Treadly.UI.TreadlyProfile.Settings.WifiSetup.TreadlyProfileSettingsWifiSetupFragment;
import com.treadly.Treadly.UI.Util.ActivityUtil;
import com.treadly.Treadly.UI.Util.BaseClasses.BaseFragment;
import com.treadly.client.lib.sdk.Listeners.DeviceConnectionEventAdapter;
import com.treadly.client.lib.sdk.Listeners.OtaUpdateRequestEventAdapter;
import com.treadly.client.lib.sdk.Listeners.OtaUpdateRequestEventListener;
import com.treadly.client.lib.sdk.Listeners.RequestEventAdapter;
import com.treadly.client.lib.sdk.Model.DeviceConnectionEvent;
import com.treadly.client.lib.sdk.Model.DeviceConnectionStatus;
import com.treadly.client.lib.sdk.Model.DeviceStatus;
import com.treadly.client.lib.sdk.Model.DeviceStatusCode;
import com.treadly.client.lib.sdk.Model.WifiApInfo;
import com.treadly.client.lib.sdk.Model.WifiStatus;
import com.treadly.client.lib.sdk.TreadlyClientLib;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

/* loaded from: classes2.dex */
public class TreadlyProfileSettingsWifiSetupFragment extends BaseFragment implements FirmwareSsidListAdapter.ItemClickListener {
    public static final String TAG = "PROFILE_WIFI_SETUP";
    FirmwareSsidListAdapter adapter;
    AlertDialog alertDialog;
    AlertDialog.Builder builder;
    AlertDialog connectedDialog;
    private ImageButton forgetPersistentButton;
    private LottieAnimationView loadingView;
    private ImageView persistentCheckmark;
    private ConstraintLayout persistentView;
    private ImageView persistentWifi;
    private TextView persistentWifiName;
    private Button scanButton;
    Button skipButton;
    RecyclerView ssidListView;
    double scanDelay = 30000.0d;
    long wifiConnectDelay = 10000;
    public boolean isOnboarding = false;
    private boolean isPendingDisconnect = false;
    private boolean pendingChanges = false;
    private boolean scanning = false;
    public boolean bringBottomNavBack = false;
    List<WifiApInfo> ssidList = new ArrayList();
    WifiApInfo selectedSsid = null;
    WifiApInfo persistentInfo = null;
    WifiSetupStage currentStage = WifiSetupStage.start;
    Timer scanTimer = new Timer();
    Timer wifiConnectTimer = new Timer();
    DeviceStatusCode currentCode = DeviceStatusCode.NO_ERROR;
    DeviceStatusCode prevCode = null;
    AlertDialog connectingDialog = null;
    AlertDialog notConnectedDialog = null;
    protected OtaUpdateRequestEventListener otaEventListener = new AnonymousClass3();
    private final RequestEventAdapter requestEventAdapter = new AnonymousClass4();
    private final DeviceConnectionEventAdapter deviceConnectionAdapter = new AnonymousClass5();

    /* loaded from: classes2.dex */
    enum WifiSetupStage {
        start,
        apScan,
        update,
        done
    }

    private int convertRSSIValue(int i) {
        return i < 170 ? R.drawable.wifi_icon_1 : i < 213 ? R.drawable.wifi_icon_2 : R.drawable.wifi_icon_3;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void setScanning(final boolean z) {
        this.scanning = z;
        ActivityUtil.runOnUiThread(getActivity(), new Runnable() { // from class: com.treadly.Treadly.UI.TreadlyProfile.Settings.WifiSetup.-$$Lambda$TreadlyProfileSettingsWifiSetupFragment$DZVDzVUIeolR8Rem9-cOisI8wNg
            @Override // java.lang.Runnable
            public final void run() {
                TreadlyProfileSettingsWifiSetupFragment.lambda$setScanning$0(TreadlyProfileSettingsWifiSetupFragment.this, z);
            }
        });
    }

    public static /* synthetic */ void lambda$setScanning$0(TreadlyProfileSettingsWifiSetupFragment treadlyProfileSettingsWifiSetupFragment, boolean z) {
        treadlyProfileSettingsWifiSetupFragment.forgetPersistentButton.setAlpha(z ? 0.3f : 1.0f);
        treadlyProfileSettingsWifiSetupFragment.forgetPersistentButton.setEnabled(!z);
        treadlyProfileSettingsWifiSetupFragment.scanButton.setAlpha(z ? 0.3f : 1.0f);
        treadlyProfileSettingsWifiSetupFragment.scanButton.setEnabled(!z);
    }

    public void setPersistentInfo(WifiApInfo wifiApInfo) {
        this.persistentInfo = wifiApInfo;
        setConnectedInfo();
    }

    private void showConnectingDialog() {
        if (this.connectedDialog != null) {
            return;
        }
        this.builder = new AlertDialog.Builder(getContext()).setMessage("Waiting for treadmill to test connection. Please Wait...");
        this.connectingDialog = this.builder.create();
        this.connectingDialog.show();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void dismissConnectingDialog() {
        if (this.connectingDialog != null) {
            this.connectingDialog.dismiss();
            this.connectedDialog = null;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void showNotConnectedDialog() {
        if (this.notConnectedDialog != null) {
            return;
        }
        this.builder = new AlertDialog.Builder(getContext()).setTitle("Wifi not connected").setMessage("Wifi was not able to connect. Would you like update the password or change the SSID").setPositiveButton("Update Password", new DialogInterface.OnClickListener() { // from class: com.treadly.Treadly.UI.TreadlyProfile.Settings.WifiSetup.-$$Lambda$TreadlyProfileSettingsWifiSetupFragment$7EKT4F-0b4-B6VTvhNapW-vqQRY
            @Override // android.content.DialogInterface.OnClickListener
            public final void onClick(DialogInterface dialogInterface, int i) {
                TreadlyProfileSettingsWifiSetupFragment.lambda$showNotConnectedDialog$1(TreadlyProfileSettingsWifiSetupFragment.this, dialogInterface, i);
            }
        }).setNegativeButton("Change SSID", new DialogInterface.OnClickListener() { // from class: com.treadly.Treadly.UI.TreadlyProfile.Settings.WifiSetup.-$$Lambda$TreadlyProfileSettingsWifiSetupFragment$E6gDJzZhCQn7pPMspbK-5OEyAaQ
            @Override // android.content.DialogInterface.OnClickListener
            public final void onClick(DialogInterface dialogInterface, int i) {
                TreadlyProfileSettingsWifiSetupFragment.lambda$showNotConnectedDialog$2(TreadlyProfileSettingsWifiSetupFragment.this, dialogInterface, i);
            }
        });
        this.notConnectedDialog = this.builder.create();
        this.notConnectedDialog.show();
    }

    public static /* synthetic */ void lambda$showNotConnectedDialog$1(TreadlyProfileSettingsWifiSetupFragment treadlyProfileSettingsWifiSetupFragment, DialogInterface dialogInterface, int i) {
        treadlyProfileSettingsWifiSetupFragment.showPasswordInput();
        treadlyProfileSettingsWifiSetupFragment.dismissNotConnectedDialog();
    }

    public static /* synthetic */ void lambda$showNotConnectedDialog$2(TreadlyProfileSettingsWifiSetupFragment treadlyProfileSettingsWifiSetupFragment, DialogInterface dialogInterface, int i) {
        treadlyProfileSettingsWifiSetupFragment.selectedSsid = null;
        treadlyProfileSettingsWifiSetupFragment.startScan();
        treadlyProfileSettingsWifiSetupFragment.dismissNotConnectedDialog();
    }

    private void dismissNotConnectedDialog() {
        if (this.notConnectedDialog != null) {
            this.notConnectedDialog.dismiss();
            this.notConnectedDialog = null;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void showConnectedDialog() {
        this.builder = new AlertDialog.Builder(getContext()).setTitle("Wifi Connected").setMessage("Wifi Connected").setNeutralButton("Dismiss", new DialogInterface.OnClickListener() { // from class: com.treadly.Treadly.UI.TreadlyProfile.Settings.WifiSetup.-$$Lambda$TreadlyProfileSettingsWifiSetupFragment$GPnFP7m24zSyGBuivMc9fgaalSg
            @Override // android.content.DialogInterface.OnClickListener
            public final void onClick(DialogInterface dialogInterface, int i) {
                ActivityUtil.runOnUiThread(r0.getActivity(), new Runnable() { // from class: com.treadly.Treadly.UI.TreadlyProfile.Settings.WifiSetup.-$$Lambda$TreadlyProfileSettingsWifiSetupFragment$3pNtI6rHaMBCuRBJObfxmQlmBjs
                    @Override // java.lang.Runnable
                    public final void run() {
                        TreadlyProfileSettingsWifiSetupFragment.this.dismissWifiSetup();
                    }
                });
            }
        });
        this.connectedDialog = this.builder.create();
        this.connectedDialog.show();
    }

    private void dismissConnectedDialog() {
        if (this.connectedDialog != null) {
            this.connectedDialog.dismiss();
        }
    }

    private void dismissAlertDialog() {
        if (this.alertDialog != null) {
            this.alertDialog.dismiss();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void dismissWifiSetup(View view) {
        dismissWifiSetup();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void dismissWifiSetup() {
        if (this.isOnboarding) {
            TreadlyServiceManager.getInstance().isOnboarding = false;
            if (TreadlyClientLib.shared.isDeviceConnected()) {
                this.isPendingDisconnect = true;
                TreadlyClientLib.shared.disconnect();
                return;
            }
            finishOnboarding();
            return;
        }
        popBackStack();
    }

    @Override // androidx.fragment.app.Fragment
    @Nullable
    public View onCreateView(@NonNull LayoutInflater layoutInflater, @Nullable ViewGroup viewGroup, @Nullable Bundle bundle) {
        return layoutInflater.inflate(R.layout.fragment_treadly_profile_settings_ota, viewGroup, false);
    }

    @Override // com.treadly.Treadly.UI.Util.BaseClasses.BaseFragment, androidx.fragment.app.Fragment
    public void onViewCreated(@NonNull View view, @Nullable Bundle bundle) {
        TreadlyClientLib.shared.addOtaUpdateRequestEventListener(this.otaEventListener);
        TreadlyClientLib.shared.addRequestEventListener(this.requestEventAdapter);
        TreadlyClientLib.shared.addDeviceConnectionEventListener(this.deviceConnectionAdapter);
        this.skipButton = (Button) view.findViewById(R.id.ssid_skip_button);
        this.skipButton.setVisibility(this.isOnboarding ? 0 : 4);
        this.skipButton.setOnClickListener(new View.OnClickListener() { // from class: com.treadly.Treadly.UI.TreadlyProfile.Settings.WifiSetup.-$$Lambda$TreadlyProfileSettingsWifiSetupFragment$Qa4ez8vbfKo0T9BTNMS8E_fuUH0
            @Override // android.view.View.OnClickListener
            public final void onClick(View view2) {
                TreadlyProfileSettingsWifiSetupFragment.this.dismissWifiSetup(view2);
            }
        });
        this.scanButton = (Button) view.findViewById(R.id.wifi_settings_scan_button);
        this.scanButton.setOnClickListener(new View.OnClickListener() { // from class: com.treadly.Treadly.UI.TreadlyProfile.Settings.WifiSetup.-$$Lambda$cCIeUPgoD5R-hLIogwbB3_o6Dmo
            @Override // android.view.View.OnClickListener
            public final void onClick(View view2) {
                TreadlyProfileSettingsWifiSetupFragment.this.startScan(view2);
            }
        });
        ((TextView) view.findViewById(R.id.nav_title)).setText(R.string.wifi_setup_title);
        ((ImageButton) view.findViewById(R.id.nav_back_arrow)).setOnClickListener(new View.OnClickListener() { // from class: com.treadly.Treadly.UI.TreadlyProfile.Settings.WifiSetup.-$$Lambda$TreadlyProfileSettingsWifiSetupFragment$D1oHet-kbdJQilTurG6xbR0uuOg
            @Override // android.view.View.OnClickListener
            public final void onClick(View view2) {
                TreadlyProfileSettingsWifiSetupFragment.this.popBackStack();
            }
        });
        this.persistentView = (ConstraintLayout) view.findViewById(R.id.wifi_settings_persistent_view);
        this.persistentWifiName = (TextView) view.findViewById(R.id.wifi_settings_persistent_name);
        this.persistentWifi = (ImageView) view.findViewById(R.id.wifi_settings_persistent_wifi_icon);
        this.persistentCheckmark = (ImageView) view.findViewById(R.id.wifi_settings_persistent_checkmark);
        this.forgetPersistentButton = (ImageButton) view.findViewById(R.id.wifi_settings_forget_button);
        this.forgetPersistentButton.setOnClickListener(new View.OnClickListener() { // from class: com.treadly.Treadly.UI.TreadlyProfile.Settings.WifiSetup.-$$Lambda$TreadlyProfileSettingsWifiSetupFragment$EMYhyDH7e6PRf4jNhYm7p7MtCOg
            @Override // android.view.View.OnClickListener
            public final void onClick(View view2) {
                TreadlyProfileSettingsWifiSetupFragment.lambda$onViewCreated$5(TreadlyProfileSettingsWifiSetupFragment.this, view2);
            }
        });
        this.ssidListView = (RecyclerView) view.findViewById(R.id.ssid_list);
        this.ssidListView.setLayoutManager(new LinearLayoutManager(getContext()));
        this.adapter = new FirmwareSsidListAdapter(getContext(), this.ssidList);
        this.adapter.setClickListener(this);
        this.ssidListView.setAdapter(this.adapter);
        this.adapter.notifyDataSetChanged();
        hideBottomNavigation();
        setPersistentInfo(null);
        initLoadingAnimation(view);
        startScan();
    }

    public static /* synthetic */ void lambda$onViewCreated$5(TreadlyProfileSettingsWifiSetupFragment treadlyProfileSettingsWifiSetupFragment, View view) {
        if (!treadlyProfileSettingsWifiSetupFragment.scanning && TreadlyClientLib.shared.clearOtaConfigSettings()) {
            treadlyProfileSettingsWifiSetupFragment.setPersistentInfo(null);
        }
    }

    @Override // com.treadly.Treadly.UI.FirmwareUpdate.FirmwareSsidListAdapter.ItemClickListener
    public void onItemClick(View view, int i) {
        WifiApInfo wifiApInfo = this.ssidList.get(i);
        if (wifiApInfo != null) {
            this.selectedSsid = new WifiApInfo(wifiApInfo.ssid, "", wifiApInfo.rssi, wifiApInfo.persistent);
            showPasswordInput();
        }
    }

    @Override // com.treadly.Treadly.UI.Util.BaseClasses.BaseFragment, androidx.fragment.app.Fragment
    public void onDetach() {
        super.onDetach();
        TreadlyClientLib.shared.removeOtaUpdateRequestEventListener(this.otaEventListener);
        TreadlyClientLib.shared.removeRequestEventListener(this.requestEventAdapter);
        TreadlyClientLib.shared.removeDeviceConnectionEventListener(this.deviceConnectionAdapter);
        stopWifiApScanTimer();
        if (this.bringBottomNavBack) {
            showBottomNavigation();
        }
    }

    void showPasswordInput() {
        Context context = getContext();
        if (context == null) {
            return;
        }
        this.builder = new AlertDialog.Builder(getContext()).setTitle("Enter SSID password").setMessage(String.format("Please enter the SSID password for %s", this.selectedSsid.ssid)).setCancelable(false);
        final AppCompatEditText appCompatEditText = new AppCompatEditText(context);
        appCompatEditText.setInputType(GmsClientSupervisor.DEFAULT_BIND_FLAGS);
        this.builder.setView(appCompatEditText);
        this.builder.setPositiveButton("Done", new DialogInterface.OnClickListener() { // from class: com.treadly.Treadly.UI.TreadlyProfile.Settings.WifiSetup.-$$Lambda$TreadlyProfileSettingsWifiSetupFragment$16GN-cRQLr9c7AyIbco7wFvSumw
            @Override // android.content.DialogInterface.OnClickListener
            public final void onClick(DialogInterface dialogInterface, int i) {
                TreadlyProfileSettingsWifiSetupFragment.lambda$showPasswordInput$6(TreadlyProfileSettingsWifiSetupFragment.this, appCompatEditText, dialogInterface, i);
            }
        });
        this.builder.setNegativeButton("Dismiss", new DialogInterface.OnClickListener() { // from class: com.treadly.Treadly.UI.TreadlyProfile.Settings.WifiSetup.-$$Lambda$TreadlyProfileSettingsWifiSetupFragment$Lmp7W02uGExVRVKDNXWG1ndu1w4
            @Override // android.content.DialogInterface.OnClickListener
            public final void onClick(DialogInterface dialogInterface, int i) {
                TreadlyProfileSettingsWifiSetupFragment.lambda$showPasswordInput$7(TreadlyProfileSettingsWifiSetupFragment.this, dialogInterface, i);
            }
        });
        this.alertDialog = this.builder.show();
    }

    public static /* synthetic */ void lambda$showPasswordInput$6(TreadlyProfileSettingsWifiSetupFragment treadlyProfileSettingsWifiSetupFragment, AppCompatEditText appCompatEditText, DialogInterface dialogInterface, int i) {
        if (appCompatEditText.getText() != null) {
            treadlyProfileSettingsWifiSetupFragment.selectedSsid.password = appCompatEditText.getText().toString();
            TreadlyClientLib.shared.otaConfigSettings(treadlyProfileSettingsWifiSetupFragment.selectedSsid);
            treadlyProfileSettingsWifiSetupFragment.hideKeyboard();
            treadlyProfileSettingsWifiSetupFragment.prevCode = treadlyProfileSettingsWifiSetupFragment.currentCode;
            treadlyProfileSettingsWifiSetupFragment.wifiConnectTimer = new Timer();
            treadlyProfileSettingsWifiSetupFragment.wifiConnectTimer.schedule(new AnonymousClass1(), treadlyProfileSettingsWifiSetupFragment.wifiConnectDelay);
            treadlyProfileSettingsWifiSetupFragment.endLoadingAnimation(true);
            treadlyProfileSettingsWifiSetupFragment.setScanning(false);
            treadlyProfileSettingsWifiSetupFragment.showLoading();
            treadlyProfileSettingsWifiSetupFragment.showConnectingDialog();
            return;
        }
        treadlyProfileSettingsWifiSetupFragment.selectedSsid = null;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* renamed from: com.treadly.Treadly.UI.TreadlyProfile.Settings.WifiSetup.TreadlyProfileSettingsWifiSetupFragment$1  reason: invalid class name */
    /* loaded from: classes2.dex */
    public class AnonymousClass1 extends TimerTask {
        AnonymousClass1() {
        }

        @Override // java.util.TimerTask, java.lang.Runnable
        public void run() {
            TreadlyProfileSettingsWifiSetupFragment.this.dismissLoading();
            TreadlyProfileSettingsWifiSetupFragment.this.endLoadingAnimation(true);
            TreadlyProfileSettingsWifiSetupFragment.this.setScanning(false);
            TreadlyProfileSettingsWifiSetupFragment.this.dismissConnectingDialog();
            if (TreadlyProfileSettingsWifiSetupFragment.this.currentCode == DeviceStatusCode.WIFI_SCANNING || TreadlyProfileSettingsWifiSetupFragment.this.currentCode == DeviceStatusCode.WIFI_ERROR) {
                ActivityUtil.runOnUiThread(TreadlyProfileSettingsWifiSetupFragment.this.getActivity(), new Runnable() { // from class: com.treadly.Treadly.UI.TreadlyProfile.Settings.WifiSetup.-$$Lambda$TreadlyProfileSettingsWifiSetupFragment$1$0XiFQpB1_W2GUm1Ja_SmSWIeE3A
                    @Override // java.lang.Runnable
                    public final void run() {
                        TreadlyProfileSettingsWifiSetupFragment.this.showNotConnectedDialog();
                    }
                });
            } else {
                ActivityUtil.runOnUiThread(TreadlyProfileSettingsWifiSetupFragment.this.getActivity(), new Runnable() { // from class: com.treadly.Treadly.UI.TreadlyProfile.Settings.WifiSetup.-$$Lambda$TreadlyProfileSettingsWifiSetupFragment$1$cY7oKvg1zHiSYgq0D93kB4V_MZo
                    @Override // java.lang.Runnable
                    public final void run() {
                        TreadlyProfileSettingsWifiSetupFragment.this.showConnectedDialog();
                    }
                });
            }
        }
    }

    public static /* synthetic */ void lambda$showPasswordInput$7(TreadlyProfileSettingsWifiSetupFragment treadlyProfileSettingsWifiSetupFragment, DialogInterface dialogInterface, int i) {
        treadlyProfileSettingsWifiSetupFragment.selectedSsid = null;
        treadlyProfileSettingsWifiSetupFragment.hideKeyboard();
    }

    public void hideKeyboard() {
        FragmentActivity activity = getActivity();
        if (activity == null) {
            return;
        }
        InputMethodManager inputMethodManager = (InputMethodManager) activity.getSystemService("input_method");
        View currentFocus = activity.getCurrentFocus();
        if (currentFocus == null) {
            currentFocus = new View(activity);
        }
        if (inputMethodManager == null) {
            return;
        }
        inputMethodManager.hideSoftInputFromWindow(currentFocus.getWindowToken(), 0);
    }

    private void setConnectedInfo() {
        if (this.persistentInfo == null) {
            ActivityUtil.runOnUiThread(getActivity(), new Runnable() { // from class: com.treadly.Treadly.UI.TreadlyProfile.Settings.WifiSetup.-$$Lambda$TreadlyProfileSettingsWifiSetupFragment$4wHyIEiANpBbHODoCRGCl_CIxgA
                @Override // java.lang.Runnable
                public final void run() {
                    TreadlyProfileSettingsWifiSetupFragment.this.persistentView.setVisibility(8);
                }
            });
        } else {
            ActivityUtil.runOnUiThread(getActivity(), new Runnable() { // from class: com.treadly.Treadly.UI.TreadlyProfile.Settings.WifiSetup.-$$Lambda$TreadlyProfileSettingsWifiSetupFragment$1GFUMYO3wq5zfnE-rHSM0R86SNU
                @Override // java.lang.Runnable
                public final void run() {
                    TreadlyProfileSettingsWifiSetupFragment.lambda$setConnectedInfo$9(TreadlyProfileSettingsWifiSetupFragment.this);
                }
            });
        }
    }

    public static /* synthetic */ void lambda$setConnectedInfo$9(TreadlyProfileSettingsWifiSetupFragment treadlyProfileSettingsWifiSetupFragment) {
        treadlyProfileSettingsWifiSetupFragment.persistentWifiName.setText(treadlyProfileSettingsWifiSetupFragment.persistentInfo.ssid);
        treadlyProfileSettingsWifiSetupFragment.persistentCheckmark.setVisibility(treadlyProfileSettingsWifiSetupFragment.persistentInfo.connected ? 0 : 4);
        treadlyProfileSettingsWifiSetupFragment.persistentView.setVisibility(0);
        treadlyProfileSettingsWifiSetupFragment.persistentWifi.setImageDrawable(treadlyProfileSettingsWifiSetupFragment.getResources().getDrawable(treadlyProfileSettingsWifiSetupFragment.convertRSSIValue(treadlyProfileSettingsWifiSetupFragment.persistentInfo.rssi), null));
    }

    private void initLoadingAnimation(View view) {
        if (view == null) {
            return;
        }
        this.loadingView = (LottieAnimationView) view.findViewById(R.id.wifi_settings_loading_animation);
        this.loadingView.setRepeatMode(1);
        this.loadingView.setSpeed(1.0f);
        this.loadingView.setRepeatCount(0);
    }

    private void startLoadingAnimation() {
        ActivityUtil.runOnUiThread(getActivity(), new Runnable() { // from class: com.treadly.Treadly.UI.TreadlyProfile.Settings.WifiSetup.-$$Lambda$TreadlyProfileSettingsWifiSetupFragment$z4cQTbyxRjtrJ5PZ5wVwcwpaTrg
            @Override // java.lang.Runnable
            public final void run() {
                TreadlyProfileSettingsWifiSetupFragment.lambda$startLoadingAnimation$10(TreadlyProfileSettingsWifiSetupFragment.this);
            }
        });
    }

    public static /* synthetic */ void lambda$startLoadingAnimation$10(TreadlyProfileSettingsWifiSetupFragment treadlyProfileSettingsWifiSetupFragment) {
        treadlyProfileSettingsWifiSetupFragment.endLoadingAnimation(true);
        treadlyProfileSettingsWifiSetupFragment.loadingView.setRepeatCount(-1);
        treadlyProfileSettingsWifiSetupFragment.loadingView.playAnimation();
        treadlyProfileSettingsWifiSetupFragment.scanButton.setText(R.string.scanning_label);
        treadlyProfileSettingsWifiSetupFragment.setScanning(true);
    }

    private void endLoadingAnimation() {
        endLoadingAnimation(false);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void endLoadingAnimation(final boolean z) {
        ActivityUtil.runOnUiThread(getActivity(), new Runnable() { // from class: com.treadly.Treadly.UI.TreadlyProfile.Settings.WifiSetup.-$$Lambda$TreadlyProfileSettingsWifiSetupFragment$XaKTC22hwYJ5qh7h_ElE_5CmxZc
            @Override // java.lang.Runnable
            public final void run() {
                TreadlyProfileSettingsWifiSetupFragment.lambda$endLoadingAnimation$11(TreadlyProfileSettingsWifiSetupFragment.this, z);
            }
        });
    }

    public static /* synthetic */ void lambda$endLoadingAnimation$11(TreadlyProfileSettingsWifiSetupFragment treadlyProfileSettingsWifiSetupFragment, boolean z) {
        treadlyProfileSettingsWifiSetupFragment.setScanning(false);
        if (z && treadlyProfileSettingsWifiSetupFragment.loadingView.getRepeatCount() == -1) {
            treadlyProfileSettingsWifiSetupFragment.loadingView.setRepeatCount(1);
        } else {
            treadlyProfileSettingsWifiSetupFragment.loadingView.cancelAnimation();
        }
        treadlyProfileSettingsWifiSetupFragment.scanButton.setText(R.string.scan_label);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void finishOnboarding() {
        if (this.pendingChanges) {
            return;
        }
        this.pendingChanges = true;
        TreadlyServiceManager.getInstance().updateOnboardingStatus(new TreadlyServiceResponseEventAdapter() { // from class: com.treadly.Treadly.UI.TreadlyProfile.Settings.WifiSetup.TreadlyProfileSettingsWifiSetupFragment.2
            @Override // com.treadly.Treadly.Data.Listeners.TreadlyServiceResponseEventAdapter, com.treadly.Treadly.Data.Listeners.TreadlyServiceResponseEventListener
            public void onSuccess(String str) {
                super.onSuccess(str);
                MainActivity mainActivity = (MainActivity) TreadlyProfileSettingsWifiSetupFragment.this.getActivity();
                if (str != null || mainActivity == null) {
                    TreadlyProfileSettingsWifiSetupFragment.this.pendingChanges = false;
                    TreadlyProfileSettingsWifiSetupFragment.this.showBaseAlert("Oops!", "There was an error. Please try again.");
                    return;
                }
                TreadlyProfileSettingsWifiSetupFragment.this.clearBackStack();
                mainActivity.toConnect();
            }
        });
    }

    /* renamed from: com.treadly.Treadly.UI.TreadlyProfile.Settings.WifiSetup.TreadlyProfileSettingsWifiSetupFragment$3  reason: invalid class name */
    /* loaded from: classes2.dex */
    class AnonymousClass3 extends OtaUpdateRequestEventAdapter {
        AnonymousClass3() {
        }

        @Override // com.treadly.client.lib.sdk.Listeners.OtaUpdateRequestEventAdapter, com.treadly.client.lib.sdk.Listeners.OtaUpdateRequestEventListener
        public void onOtaUpdateWifiScanResponse(WifiStatus wifiStatus) {
            TreadlyProfileSettingsWifiSetupFragment.this.currentStage = WifiSetupStage.apScan;
            if (wifiStatus != WifiStatus.noError) {
                TreadlyProfileSettingsWifiSetupFragment.this.dismissLoading();
                TreadlyProfileSettingsWifiSetupFragment.this.endLoadingAnimation(true);
                TreadlyProfileSettingsWifiSetupFragment.this.setScanning(false);
                TreadlyProfileSettingsWifiSetupFragment.this.showBaseAlert("Scan Error", "There was an error scanning available wifi access points. Please try again later.");
            }
        }

        @Override // com.treadly.client.lib.sdk.Listeners.OtaUpdateRequestEventAdapter, com.treadly.client.lib.sdk.Listeners.OtaUpdateRequestEventListener
        public void onOtaUpdateWiFiApResponse(WifiApInfo wifiApInfo) {
            TreadlyProfileSettingsWifiSetupFragment.this.dismissLoading();
            TreadlyProfileSettingsWifiSetupFragment.this.currentStage = WifiSetupStage.apScan;
            if (!wifiApInfo.persistent) {
                TreadlyProfileSettingsWifiSetupFragment.this.endLoadingAnimation(true);
                TreadlyProfileSettingsWifiSetupFragment.this.setScanning(false);
                for (WifiApInfo wifiApInfo2 : TreadlyProfileSettingsWifiSetupFragment.this.ssidList) {
                    if (wifiApInfo2.ssid.equals(wifiApInfo.ssid)) {
                        return;
                    }
                }
                TreadlyProfileSettingsWifiSetupFragment.this.ssidList.add(wifiApInfo);
                TreadlyProfileSettingsWifiSetupFragment.this.ssidList.sort(new Comparator() { // from class: com.treadly.Treadly.UI.TreadlyProfile.Settings.WifiSetup.-$$Lambda$TreadlyProfileSettingsWifiSetupFragment$3$jaEjnyNojoFXsXUTNpIHdbZpFWs
                    @Override // java.util.Comparator
                    public final int compare(Object obj, Object obj2) {
                        return TreadlyProfileSettingsWifiSetupFragment.AnonymousClass3.lambda$onOtaUpdateWiFiApResponse$0((WifiApInfo) obj, (WifiApInfo) obj2);
                    }
                });
                TreadlyProfileSettingsWifiSetupFragment.this.adapter.notifyDataSetChanged();
                return;
            }
            String replaceAll = wifiApInfo.ssid.replaceAll("\u0000", "");
            TreadlyProfileSettingsWifiSetupFragment treadlyProfileSettingsWifiSetupFragment = TreadlyProfileSettingsWifiSetupFragment.this;
            if (replaceAll.isEmpty()) {
                wifiApInfo = null;
            }
            treadlyProfileSettingsWifiSetupFragment.setPersistentInfo(wifiApInfo);
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        public static /* synthetic */ int lambda$onOtaUpdateWiFiApResponse$0(WifiApInfo wifiApInfo, WifiApInfo wifiApInfo2) {
            return wifiApInfo.rssi - wifiApInfo2.rssi;
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* renamed from: com.treadly.Treadly.UI.TreadlyProfile.Settings.WifiSetup.TreadlyProfileSettingsWifiSetupFragment$4  reason: invalid class name */
    /* loaded from: classes2.dex */
    public class AnonymousClass4 extends RequestEventAdapter {
        AnonymousClass4() {
        }

        @Override // com.treadly.client.lib.sdk.Listeners.RequestEventAdapter, com.treadly.client.lib.sdk.Listeners.RequestEventListener
        public void onRequestStatusResponse(boolean z, DeviceStatus deviceStatus) {
            if (z && deviceStatus != null && TreadlyClientLib.shared.isDeviceConnected()) {
                TreadlyProfileSettingsWifiSetupFragment.this.currentCode = deviceStatus.getStatusCode();
                if (TreadlyProfileSettingsWifiSetupFragment.this.prevCode == null || TreadlyProfileSettingsWifiSetupFragment.this.prevCode == TreadlyProfileSettingsWifiSetupFragment.this.currentCode || TreadlyProfileSettingsWifiSetupFragment.this.currentCode == DeviceStatusCode.WIFI_SCANNING) {
                    return;
                }
                TreadlyProfileSettingsWifiSetupFragment.this.dismissLoading();
                TreadlyProfileSettingsWifiSetupFragment.this.endLoadingAnimation(true);
                TreadlyProfileSettingsWifiSetupFragment.this.setScanning(false);
                TreadlyProfileSettingsWifiSetupFragment.this.dismissConnectingDialog();
                TreadlyProfileSettingsWifiSetupFragment.this.wifiConnectTimer.cancel();
                TreadlyProfileSettingsWifiSetupFragment.this.prevCode = null;
                if (TreadlyProfileSettingsWifiSetupFragment.this.currentCode != DeviceStatusCode.WIFI_ERROR) {
                    ActivityUtil.runOnUiThread(TreadlyProfileSettingsWifiSetupFragment.this.getActivity(), new Runnable() { // from class: com.treadly.Treadly.UI.TreadlyProfile.Settings.WifiSetup.-$$Lambda$TreadlyProfileSettingsWifiSetupFragment$4$GRly5MKJPOX2cB7lJ8K9mDs5dmU
                        @Override // java.lang.Runnable
                        public final void run() {
                            TreadlyProfileSettingsWifiSetupFragment.this.showConnectedDialog();
                        }
                    });
                } else if (TreadlyProfileSettingsWifiSetupFragment.this.currentCode == DeviceStatusCode.WIFI_ERROR) {
                    ActivityUtil.runOnUiThread(TreadlyProfileSettingsWifiSetupFragment.this.getActivity(), new Runnable() { // from class: com.treadly.Treadly.UI.TreadlyProfile.Settings.WifiSetup.-$$Lambda$TreadlyProfileSettingsWifiSetupFragment$4$FlxXOEy5bSoMuXTZG0Uj_QqG78Y
                        @Override // java.lang.Runnable
                        public final void run() {
                            TreadlyProfileSettingsWifiSetupFragment.this.showNotConnectedDialog();
                        }
                    });
                }
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* renamed from: com.treadly.Treadly.UI.TreadlyProfile.Settings.WifiSetup.TreadlyProfileSettingsWifiSetupFragment$5  reason: invalid class name */
    /* loaded from: classes2.dex */
    public class AnonymousClass5 extends DeviceConnectionEventAdapter {
        AnonymousClass5() {
        }

        @Override // com.treadly.client.lib.sdk.Listeners.DeviceConnectionEventAdapter, com.treadly.client.lib.sdk.Listeners.DeviceConnectionEventListener
        public void onDeviceConnectionChanged(DeviceConnectionEvent deviceConnectionEvent) {
            super.onDeviceConnectionChanged(deviceConnectionEvent);
            if (deviceConnectionEvent.getStatus() == DeviceConnectionStatus.notConnected || deviceConnectionEvent.getStatus() == DeviceConnectionStatus.disconnecting) {
                ActivityUtil.runOnUiThread(TreadlyProfileSettingsWifiSetupFragment.this.getActivity(), new Runnable() { // from class: com.treadly.Treadly.UI.TreadlyProfile.Settings.WifiSetup.-$$Lambda$TreadlyProfileSettingsWifiSetupFragment$5$8aWO-nlZAXYvh8fz_mitaPyV7wA
                    @Override // java.lang.Runnable
                    public final void run() {
                        TreadlyProfileSettingsWifiSetupFragment.AnonymousClass5.lambda$onDeviceConnectionChanged$0(TreadlyProfileSettingsWifiSetupFragment.AnonymousClass5.this);
                    }
                });
            }
        }

        public static /* synthetic */ void lambda$onDeviceConnectionChanged$0(AnonymousClass5 anonymousClass5) {
            if (!TreadlyProfileSettingsWifiSetupFragment.this.isOnboarding) {
                TreadlyProfileSettingsWifiSetupFragment.this.popBackStack();
            }
            if (TreadlyProfileSettingsWifiSetupFragment.this.isPendingDisconnect && TreadlyProfileSettingsWifiSetupFragment.this.isOnboarding) {
                TreadlyProfileSettingsWifiSetupFragment.this.finishOnboarding();
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void startScan(View view) {
        startScan();
    }

    void startScan() {
        if (this.scanning) {
            return;
        }
        this.ssidList.clear();
        ActivityUtil.runOnUiThread(getActivity(), new Runnable() { // from class: com.treadly.Treadly.UI.TreadlyProfile.Settings.WifiSetup.-$$Lambda$TreadlyProfileSettingsWifiSetupFragment$cKPfaMqmSXYH7lHZpET7HQBWgF0
            @Override // java.lang.Runnable
            public final void run() {
                TreadlyProfileSettingsWifiSetupFragment.this.adapter.notifyDataSetChanged();
            }
        });
        if (TreadlyClientLib.shared.startWifiApScan()) {
            startLoadingAnimation();
            startWifiApScanTimer();
        }
    }

    void startWifiApScanTimer() {
        stopWifiApScanTimer();
        this.scanTimer = new Timer();
        this.scanTimer.schedule(new TimerTask() { // from class: com.treadly.Treadly.UI.TreadlyProfile.Settings.WifiSetup.TreadlyProfileSettingsWifiSetupFragment.6
            @Override // java.util.TimerTask, java.lang.Runnable
            public void run() {
                TreadlyProfileSettingsWifiSetupFragment.this.wifiApScanTimeout();
            }
        }, (long) this.scanDelay);
    }

    void stopWifiApScanTimer() {
        this.scanTimer.cancel();
        this.scanTimer.purge();
    }

    void wifiApScanTimeout() {
        ActivityUtil.runOnUiThread(getActivity(), new Runnable() { // from class: com.treadly.Treadly.UI.TreadlyProfile.Settings.WifiSetup.-$$Lambda$TreadlyProfileSettingsWifiSetupFragment$r0QPBQ_4qAaOgRl11kzxRcBDN0A
            @Override // java.lang.Runnable
            public final void run() {
                TreadlyProfileSettingsWifiSetupFragment.lambda$wifiApScanTimeout$13(TreadlyProfileSettingsWifiSetupFragment.this);
            }
        });
    }

    public static /* synthetic */ void lambda$wifiApScanTimeout$13(TreadlyProfileSettingsWifiSetupFragment treadlyProfileSettingsWifiSetupFragment) {
        if (treadlyProfileSettingsWifiSetupFragment.ssidList.isEmpty()) {
            treadlyProfileSettingsWifiSetupFragment.dismissLoading();
            treadlyProfileSettingsWifiSetupFragment.endLoadingAnimation(true);
            treadlyProfileSettingsWifiSetupFragment.showBaseAlert("No Wi-Fi Networks Found", "Please try again");
        }
    }
}
