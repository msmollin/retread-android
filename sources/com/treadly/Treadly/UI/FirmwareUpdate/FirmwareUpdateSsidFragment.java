package com.treadly.Treadly.UI.FirmwareUpdate;

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
import com.treadly.Treadly.R;
import com.treadly.Treadly.UI.FirmwareUpdate.FirmwareSsidListAdapter;
import com.treadly.Treadly.UI.FirmwareUpdate.FirmwareUpdateSsidFragment;
import com.treadly.Treadly.UI.Util.ActivityUtil;
import com.treadly.Treadly.UI.Util.BaseClasses.BaseFragment;
import com.treadly.client.lib.sdk.Listeners.DeviceConnectionEventListener;
import com.treadly.client.lib.sdk.Listeners.OtaUpdateRequestEventListener;
import com.treadly.client.lib.sdk.Model.DeviceConnectionEvent;
import com.treadly.client.lib.sdk.Model.DeviceConnectionStatus;
import com.treadly.client.lib.sdk.Model.DeviceInfo;
import com.treadly.client.lib.sdk.Model.FirmwareVersion;
import com.treadly.client.lib.sdk.Model.OtaUpdateStatus;
import com.treadly.client.lib.sdk.Model.WifiApInfo;
import com.treadly.client.lib.sdk.Model.WifiStatus;
import com.treadly.client.lib.sdk.TreadlyClientLib;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

/* loaded from: classes2.dex */
public class FirmwareUpdateSsidFragment extends BaseFragment implements OtaUpdateRequestEventListener, DeviceConnectionEventListener, FirmwareSsidListAdapter.ItemClickListener {
    FirmwareSsidListAdapter adapter;
    AlertDialog alertDialog;
    AlertDialog.Builder builder;
    private LottieAnimationView loadingView;
    AlertDialog persistentDialog;
    ConstraintLayout persistentView;
    AlertDialog.Builder progressBuilder;
    AlertDialog progressDialog;
    Button scanButton;
    RecyclerView ssidListView;
    public FirmwareUpdateEventListener updateEventListener;
    double otaDelay = 180000.0d;
    double scanDelay = 30000.0d;
    double resetDelay = 5000.0d;
    WifiApInfo persistentInfo = null;
    List<WifiApInfo> ssidList = new ArrayList();
    WifiApInfo selectedSsid = null;
    Timer otaTimer = new Timer();
    Timer scanTimer = new Timer();
    OtaUpdateStage currentStage = OtaUpdateStage.start;
    boolean scanning = false;
    public boolean updateRequired = false;
    public boolean returnShowBottomNav = false;
    Timer resetTimer = new Timer();

    /* loaded from: classes2.dex */
    public interface FirmwareUpdateEventListener {
        void updateRequiredSkipped();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes2.dex */
    public enum OtaUpdateStage {
        start,
        setMode,
        apScan,
        updating,
        done
    }

    @Override // com.treadly.client.lib.sdk.Listeners.DeviceConnectionEventListener
    public void onDeviceConnectionDeviceDiscovered(DeviceInfo deviceInfo) {
    }

    @Override // com.treadly.client.lib.sdk.Listeners.OtaUpdateRequestEventListener
    public void onOtaUpdateFirmwareVersionAvailable(boolean z, FirmwareVersion firmwareVersion, String[] strArr, boolean z2) {
    }

    void setScanning(final boolean z) {
        this.scanning = z;
        ActivityUtil.runOnUiThread(getActivity(), new Runnable() { // from class: com.treadly.Treadly.UI.FirmwareUpdate.-$$Lambda$FirmwareUpdateSsidFragment$9FadTAE-XZLJTTCgQNpViw0rNgI
            @Override // java.lang.Runnable
            public final void run() {
                FirmwareUpdateSsidFragment.lambda$setScanning$0(FirmwareUpdateSsidFragment.this, z);
            }
        });
    }

    public static /* synthetic */ void lambda$setScanning$0(FirmwareUpdateSsidFragment firmwareUpdateSsidFragment, boolean z) {
        firmwareUpdateSsidFragment.scanButton.setAlpha(z ? 0.3f : 1.0f);
        firmwareUpdateSsidFragment.scanButton.setEnabled(!z);
    }

    private void dismissAlertDialog() {
        if (this.alertDialog != null) {
            this.alertDialog.dismiss();
        }
    }

    private void dismissProgressDialog() {
        if (this.progressDialog != null) {
            this.progressDialog.dismiss();
        }
    }

    @Override // androidx.fragment.app.Fragment
    @Nullable
    public View onCreateView(@NonNull LayoutInflater layoutInflater, @Nullable ViewGroup viewGroup, @Nullable Bundle bundle) {
        return layoutInflater.inflate(R.layout.fragment_treadly_profile_settings_ota, viewGroup, false);
    }

    @Override // com.treadly.Treadly.UI.Util.BaseClasses.BaseFragment, androidx.fragment.app.Fragment
    public void onViewCreated(@NonNull View view, @Nullable Bundle bundle) {
        TreadlyClientLib.shared.addOtaUpdateRequestEventListener(this);
        TreadlyClientLib.shared.addDeviceConnectionEventListener(this);
        this.ssidListView = (RecyclerView) view.findViewById(R.id.ssid_list);
        this.ssidListView.setLayoutManager(new LinearLayoutManager(getContext()));
        this.adapter = new FirmwareSsidListAdapter(getContext(), this.ssidList);
        this.adapter.setClickListener(this);
        this.ssidListView.setAdapter(this.adapter);
        this.adapter.notifyDataSetChanged();
        this.persistentView = (ConstraintLayout) view.findViewById(R.id.wifi_settings_persistent_view);
        this.persistentView.setVisibility(8);
        ((TextView) view.findViewById(R.id.wifi_settings_description)).setVisibility(8);
        ((Button) view.findViewById(R.id.ssid_skip_button)).setVisibility(4);
        ((TextView) view.findViewById(R.id.nav_title)).setText(R.string.wifi_settings_firmware_update_nav_title);
        ((ImageButton) view.findViewById(R.id.nav_back_arrow)).setOnClickListener(new View.OnClickListener() { // from class: com.treadly.Treadly.UI.FirmwareUpdate.-$$Lambda$FirmwareUpdateSsidFragment$SrIOf2ZAxfzooUK889w_xBbCPJ8
            @Override // android.view.View.OnClickListener
            public final void onClick(View view2) {
                FirmwareUpdateSsidFragment.this.popBackStack();
            }
        });
        this.scanButton = (Button) view.findViewById(R.id.wifi_settings_scan_button);
        this.scanButton.setOnClickListener(new View.OnClickListener() { // from class: com.treadly.Treadly.UI.FirmwareUpdate.-$$Lambda$Mj-u2DQLFHoaUKlH1Woa4JUDJkE
            @Override // android.view.View.OnClickListener
            public final void onClick(View view2) {
                FirmwareUpdateSsidFragment.this.startScan(view2);
            }
        });
        initLoadingAnimation(view);
        TreadlyClientLib.shared.otaUpdateSetMode();
        showLoading();
    }

    @Override // androidx.fragment.app.Fragment
    public void onResume() {
        super.onResume();
        willEnterForeground();
    }

    void willEnterForeground() {
        switch (this.currentStage) {
            case start:
            case setMode:
                startResetTimer();
                return;
            default:
                System.out.println("Not Supported");
                return;
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
        TreadlyClientLib.shared.removeOtaUpdateRequestEventListener(this);
        TreadlyClientLib.shared.removeDeviceConnectionEventListener(this);
        stopResetTimer();
        stopWifiApScanTimer();
        if (this.returnShowBottomNav) {
            showBottomNavigation();
        }
        if (this.updateRequired) {
            this.updateEventListener.updateRequiredSkipped();
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
        this.builder.setPositiveButton("Done", new DialogInterface.OnClickListener() { // from class: com.treadly.Treadly.UI.FirmwareUpdate.-$$Lambda$FirmwareUpdateSsidFragment$q3DzkjdjePHQiLpRH5KPym1pF-Q
            @Override // android.content.DialogInterface.OnClickListener
            public final void onClick(DialogInterface dialogInterface, int i) {
                FirmwareUpdateSsidFragment.lambda$showPasswordInput$2(FirmwareUpdateSsidFragment.this, appCompatEditText, dialogInterface, i);
            }
        });
        this.builder.setNegativeButton("Dismiss", new DialogInterface.OnClickListener() { // from class: com.treadly.Treadly.UI.FirmwareUpdate.-$$Lambda$FirmwareUpdateSsidFragment$G2mRX-csvSwDSc8NNT4P3TZfg7M
            @Override // android.content.DialogInterface.OnClickListener
            public final void onClick(DialogInterface dialogInterface, int i) {
                FirmwareUpdateSsidFragment.lambda$showPasswordInput$3(FirmwareUpdateSsidFragment.this, dialogInterface, i);
            }
        });
        this.alertDialog = this.builder.show();
    }

    public static /* synthetic */ void lambda$showPasswordInput$2(FirmwareUpdateSsidFragment firmwareUpdateSsidFragment, AppCompatEditText appCompatEditText, DialogInterface dialogInterface, int i) {
        if (appCompatEditText.getText() != null) {
            firmwareUpdateSsidFragment.selectedSsid.password = appCompatEditText.getText().toString();
            if (TreadlyClientLib.shared.otaUpdate(firmwareUpdateSsidFragment.selectedSsid)) {
                firmwareUpdateSsidFragment.hideKeyboard();
                firmwareUpdateSsidFragment.showOtaUpdateStartAlert();
            }
            firmwareUpdateSsidFragment.endLoadingAnimation();
            firmwareUpdateSsidFragment.setScanning(false);
            firmwareUpdateSsidFragment.showLoading();
            return;
        }
        firmwareUpdateSsidFragment.selectedSsid = null;
    }

    public static /* synthetic */ void lambda$showPasswordInput$3(FirmwareUpdateSsidFragment firmwareUpdateSsidFragment, DialogInterface dialogInterface, int i) {
        firmwareUpdateSsidFragment.selectedSsid = null;
        firmwareUpdateSsidFragment.hideKeyboard();
    }

    void showOtaUpdateStartAlert() {
        this.builder = new AlertDialog.Builder(getContext()).setTitle("Update Firmware").setMessage("Preparing to update").setCancelable(false);
        this.alertDialog = this.builder.show();
        startOtaTimer();
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
        ActivityUtil.runOnUiThread(getActivity(), new Runnable() { // from class: com.treadly.Treadly.UI.FirmwareUpdate.-$$Lambda$FirmwareUpdateSsidFragment$h_AmIhXDJjYHeagdCmfnBqmG4mE
            @Override // java.lang.Runnable
            public final void run() {
                FirmwareUpdateSsidFragment.lambda$startLoadingAnimation$4(FirmwareUpdateSsidFragment.this);
            }
        });
    }

    public static /* synthetic */ void lambda$startLoadingAnimation$4(FirmwareUpdateSsidFragment firmwareUpdateSsidFragment) {
        firmwareUpdateSsidFragment.endLoadingAnimation();
        firmwareUpdateSsidFragment.loadingView.setRepeatCount(-1);
        firmwareUpdateSsidFragment.loadingView.playAnimation();
        firmwareUpdateSsidFragment.scanButton.setText(R.string.scanning_label);
        firmwareUpdateSsidFragment.setScanning(true);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void endLoadingAnimation() {
        ActivityUtil.runOnUiThread(getActivity(), new Runnable() { // from class: com.treadly.Treadly.UI.FirmwareUpdate.-$$Lambda$FirmwareUpdateSsidFragment$QWh5JvCJagX-fSquw19a-VC6X6o
            @Override // java.lang.Runnable
            public final void run() {
                FirmwareUpdateSsidFragment.lambda$endLoadingAnimation$5(FirmwareUpdateSsidFragment.this);
            }
        });
    }

    public static /* synthetic */ void lambda$endLoadingAnimation$5(FirmwareUpdateSsidFragment firmwareUpdateSsidFragment) {
        firmwareUpdateSsidFragment.setScanning(false);
        if (firmwareUpdateSsidFragment.loadingView.getRepeatCount() == -1) {
            firmwareUpdateSsidFragment.loadingView.setRepeatCount(1);
        } else {
            firmwareUpdateSsidFragment.loadingView.cancelAnimation();
        }
        firmwareUpdateSsidFragment.scanButton.setText(R.string.scan_label);
    }

    void resetPage() {
        if (TreadlyClientLib.shared.isDeviceConnected()) {
            TreadlyClientLib.shared.otaUpdateSetMode();
            showLoading();
            return;
        }
        startResetTimer();
    }

    void startResetTimer() {
        stopResetTimer();
        this.resetTimer = new Timer();
        this.resetTimer.schedule(new TimerTask() { // from class: com.treadly.Treadly.UI.FirmwareUpdate.FirmwareUpdateSsidFragment.1
            @Override // java.util.TimerTask, java.lang.Runnable
            public void run() {
                FirmwareUpdateSsidFragment.this.resetTimerTimeout();
            }
        }, (long) this.resetDelay);
    }

    void stopResetTimer() {
        this.resetTimer.cancel();
        this.resetTimer.purge();
    }

    void resetTimerTimeout() {
        resetPage();
    }

    @Override // com.treadly.client.lib.sdk.Listeners.OtaUpdateRequestEventListener
    public void onOtaUpdateSetMode(boolean z) {
        stopResetTimer();
        this.currentStage = OtaUpdateStage.setMode;
        startScan();
    }

    @Override // com.treadly.client.lib.sdk.Listeners.OtaUpdateRequestEventListener
    public void onOtaUpdateWifiScanResponse(WifiStatus wifiStatus) {
        this.currentStage = OtaUpdateStage.apScan;
        if (wifiStatus != WifiStatus.noError) {
            dismissLoading();
            endLoadingAnimation();
            setScanning(false);
            this.builder = new AlertDialog.Builder(getContext()).setTitle("Scan Error").setMessage("There was an error scanning available wifi access points. Please try again later").setNeutralButton("Dismiss", (DialogInterface.OnClickListener) null).setCancelable(false);
            this.alertDialog = this.builder.show();
        }
    }

    @Override // com.treadly.client.lib.sdk.Listeners.OtaUpdateRequestEventListener
    public void onOtaUpdateWiFiApResponse(WifiApInfo wifiApInfo) {
        dismissLoading();
        this.currentStage = OtaUpdateStage.apScan;
        if (wifiApInfo.persistent) {
            this.persistentInfo = wifiApInfo;
            if (!showPersistentSsidAlert()) {
                this.persistentInfo = null;
            }
        }
        endLoadingAnimation();
        setScanning(false);
        for (WifiApInfo wifiApInfo2 : this.ssidList) {
            if (wifiApInfo2.ssid.equals(wifiApInfo.ssid)) {
                return;
            }
        }
        this.ssidList.add(wifiApInfo);
        this.ssidList.sort(new Comparator() { // from class: com.treadly.Treadly.UI.FirmwareUpdate.-$$Lambda$FirmwareUpdateSsidFragment$wLC6U8KR_cQIv-f6QGWk8fRD7yM
            @Override // java.util.Comparator
            public final int compare(Object obj, Object obj2) {
                return FirmwareUpdateSsidFragment.lambda$onOtaUpdateWiFiApResponse$6((WifiApInfo) obj, (WifiApInfo) obj2);
            }
        });
        if (this.persistentInfo == null) {
            this.adapter.notifyDataSetChanged();
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static /* synthetic */ int lambda$onOtaUpdateWiFiApResponse$6(WifiApInfo wifiApInfo, WifiApInfo wifiApInfo2) {
        return wifiApInfo.rssi - wifiApInfo2.rssi;
    }

    boolean showPersistentSsidAlert() {
        if (this.persistentInfo == null || this.persistentInfo.ssid == null || getContext() == null) {
            return false;
        }
        if (this.persistentDialog != null) {
            return true;
        }
        String str = this.persistentInfo.ssid;
        this.persistentDialog = new AlertDialog.Builder(getContext()).setTitle("Previous WiFi Info").setMessage("Would you like to use the previously stored WiFi password for " + str).setPositiveButton("Yes", new DialogInterface.OnClickListener() { // from class: com.treadly.Treadly.UI.FirmwareUpdate.-$$Lambda$FirmwareUpdateSsidFragment$LWVrYj3bZOIzM4qTNucIDe7NwPQ
            @Override // android.content.DialogInterface.OnClickListener
            public final void onClick(DialogInterface dialogInterface, int i) {
                FirmwareUpdateSsidFragment.lambda$showPersistentSsidAlert$7(FirmwareUpdateSsidFragment.this, dialogInterface, i);
            }
        }).setNegativeButton("No", new DialogInterface.OnClickListener() { // from class: com.treadly.Treadly.UI.FirmwareUpdate.-$$Lambda$FirmwareUpdateSsidFragment$87f1LHSaPvw3Q3rUHDUVd8i3gYI
            @Override // android.content.DialogInterface.OnClickListener
            public final void onClick(DialogInterface dialogInterface, int i) {
                FirmwareUpdateSsidFragment.lambda$showPersistentSsidAlert$8(FirmwareUpdateSsidFragment.this, dialogInterface, i);
            }
        }).setOnDismissListener(new DialogInterface.OnDismissListener() { // from class: com.treadly.Treadly.UI.FirmwareUpdate.-$$Lambda$FirmwareUpdateSsidFragment$tLQZcqTdvk3tCKeueBVm7GX_zIU
            @Override // android.content.DialogInterface.OnDismissListener
            public final void onDismiss(DialogInterface dialogInterface) {
                FirmwareUpdateSsidFragment.this.persistentDialog = null;
            }
        }).setCancelable(false).create();
        this.persistentDialog.show();
        return true;
    }

    public static /* synthetic */ void lambda$showPersistentSsidAlert$7(FirmwareUpdateSsidFragment firmwareUpdateSsidFragment, DialogInterface dialogInterface, int i) {
        if (firmwareUpdateSsidFragment.persistentInfo != null && TreadlyClientLib.shared.otaUpdate(firmwareUpdateSsidFragment.persistentInfo)) {
            firmwareUpdateSsidFragment.showOtaUpdateStartAlert();
        }
    }

    public static /* synthetic */ void lambda$showPersistentSsidAlert$8(FirmwareUpdateSsidFragment firmwareUpdateSsidFragment, DialogInterface dialogInterface, int i) {
        firmwareUpdateSsidFragment.persistentInfo = null;
        firmwareUpdateSsidFragment.adapter.notifyDataSetChanged();
    }

    @Override // com.treadly.client.lib.sdk.Listeners.OtaUpdateRequestEventListener
    public void onOtaInProgressResponse(final int i) {
        ActivityUtil.runOnUiThread(getActivity(), new Runnable() { // from class: com.treadly.Treadly.UI.FirmwareUpdate.-$$Lambda$FirmwareUpdateSsidFragment$YIZ6NjNkzZFUHE263x37p89GNq0
            @Override // java.lang.Runnable
            public final void run() {
                FirmwareUpdateSsidFragment.lambda$onOtaInProgressResponse$10(FirmwareUpdateSsidFragment.this, i);
            }
        });
    }

    public static /* synthetic */ void lambda$onOtaInProgressResponse$10(FirmwareUpdateSsidFragment firmwareUpdateSsidFragment, int i) {
        firmwareUpdateSsidFragment.currentStage = OtaUpdateStage.updating;
        firmwareUpdateSsidFragment.dismissLoading();
        firmwareUpdateSsidFragment.dismissAlertDialog();
        firmwareUpdateSsidFragment.alertDialog = null;
        String str = "The firmware is now updating.\n" + i + "%\n";
        if (firmwareUpdateSsidFragment.progressBuilder == null) {
            if (firmwareUpdateSsidFragment.getContext() == null) {
                return;
            }
            firmwareUpdateSsidFragment.progressBuilder = new AlertDialog.Builder(firmwareUpdateSsidFragment.getContext()).setTitle("Update Firmware").setMessage(str).setCancelable(false);
            firmwareUpdateSsidFragment.progressDialog = firmwareUpdateSsidFragment.progressBuilder.show();
        } else {
            firmwareUpdateSsidFragment.progressDialog.setMessage(str);
        }
        if (firmwareUpdateSsidFragment.otaTimer == null) {
            firmwareUpdateSsidFragment.startOtaTimer();
        }
    }

    @Override // com.treadly.client.lib.sdk.Listeners.OtaUpdateRequestEventListener
    public void onOtaUpdateResponse(final OtaUpdateStatus otaUpdateStatus) {
        ActivityUtil.runOnUiThread(getActivity(), new Runnable() { // from class: com.treadly.Treadly.UI.FirmwareUpdate.-$$Lambda$FirmwareUpdateSsidFragment$HVjRyaJB5cTrkbxo2uhWg41zknI
            @Override // java.lang.Runnable
            public final void run() {
                FirmwareUpdateSsidFragment.lambda$onOtaUpdateResponse$12(FirmwareUpdateSsidFragment.this, otaUpdateStatus);
            }
        });
    }

    public static /* synthetic */ void lambda$onOtaUpdateResponse$12(final FirmwareUpdateSsidFragment firmwareUpdateSsidFragment, final OtaUpdateStatus otaUpdateStatus) {
        firmwareUpdateSsidFragment.currentStage = OtaUpdateStage.done;
        firmwareUpdateSsidFragment.dismissLoading();
        firmwareUpdateSsidFragment.dismissAlertDialog();
        firmwareUpdateSsidFragment.builder = null;
        firmwareUpdateSsidFragment.alertDialog = null;
        firmwareUpdateSsidFragment.dismissProgressDialog();
        firmwareUpdateSsidFragment.progressBuilder = null;
        firmwareUpdateSsidFragment.progressDialog = null;
        firmwareUpdateSsidFragment.builder = new AlertDialog.Builder(firmwareUpdateSsidFragment.getContext()).setTitle("Update Firmware").setMessage(firmwareUpdateSsidFragment.otaStatusMessage(otaUpdateStatus)).setCancelable(false);
        if (firmwareUpdateSsidFragment.addDismissForOtaStatus(otaUpdateStatus)) {
            firmwareUpdateSsidFragment.builder.setNeutralButton("Dismiss", new DialogInterface.OnClickListener() { // from class: com.treadly.Treadly.UI.FirmwareUpdate.-$$Lambda$FirmwareUpdateSsidFragment$Nh-5tmN-0QzMJVDi9I17U_wgtRA
                @Override // android.content.DialogInterface.OnClickListener
                public final void onClick(DialogInterface dialogInterface, int i) {
                    FirmwareUpdateSsidFragment.lambda$null$11(FirmwareUpdateSsidFragment.this, otaUpdateStatus, dialogInterface, i);
                }
            });
        }
        if (otaUpdateStatus == OtaUpdateStatus.versionSuccess || otaUpdateStatus == OtaUpdateStatus.versionError) {
            firmwareUpdateSsidFragment.stopOtaTimer();
        } else if (firmwareUpdateSsidFragment.otaTimer == null) {
            firmwareUpdateSsidFragment.startOtaTimer();
        }
        firmwareUpdateSsidFragment.alertDialog = firmwareUpdateSsidFragment.builder.show();
    }

    public static /* synthetic */ void lambda$null$11(FirmwareUpdateSsidFragment firmwareUpdateSsidFragment, OtaUpdateStatus otaUpdateStatus, DialogInterface dialogInterface, int i) {
        firmwareUpdateSsidFragment.builder = null;
        if (otaUpdateStatus == OtaUpdateStatus.noError || otaUpdateStatus == OtaUpdateStatus.versionSuccess) {
            firmwareUpdateSsidFragment.updateRequired = false;
            if (firmwareUpdateSsidFragment.getActivity() != null) {
                firmwareUpdateSsidFragment.getActivity().getSupportFragmentManager().popBackStack();
                return;
            }
            return;
        }
        firmwareUpdateSsidFragment.persistentInfo = null;
        firmwareUpdateSsidFragment.startScan();
    }

    String otaStatusMessage(OtaUpdateStatus otaUpdateStatus) {
        int value = otaUpdateStatus.value();
        switch (otaUpdateStatus) {
            case noError:
                return "Update complete. Waiting to reconnect";
            case otaError:
                return "Update Error. Please try again later. " + value;
            case otaErrorUnspecified:
                return "Unknown Error. " + value;
            case otaAuthExpired:
            case otaAssocExpire:
                return "Wi-Fi Error. Connection timed out. " + value;
            case otaAuthLeave:
                return "Wi-Fi Error. Lost connection to Wi-Fi router. " + value;
            case otaAssocTooMany:
                return "Wi-Fi Error. Wi-Fi router max connections exceeded. " + value;
            case otaNotAuthenticated:
                return "Wi-Fi Error. Failed to authenticate with router. " + value;
            case otaNotAssociated:
                return "Wi-Fi Error. Failed to connect to router. " + value;
            case otaAssocLeave:
                return "Incorrect Wi-Fi password. " + value;
            case otaAssocNotAuthenticated:
                return "Wi-Fi Error. Incompatible with router. " + value;
            case otaDisassocPowerCapBad:
                return "Wi-Fi Error. Incompatible with router. " + value;
            case otaDisassocSupChanBad:
                return "Wi-Fi Error. Incompatible with router. " + value;
            case otaIEInvalid:
                return "Wi-Fi Error. Incompatible with router. " + value;
            case otaMicFailure:
                return "Wi-Fi Error. Incompatible with router. " + value;
            case ota4WayHandshakeTimeout:
                return "Incorrect Wi-Fi password. " + value;
            case otaGroupKeyUpdateTimeout:
                return "Incorrect Wi-Fi password. " + value;
            case otaIEIn4WayDiffers:
                return "Wi-Fi Error. Incompatible with router. " + value;
            case otaGroupCipherInvalid:
                return "Wi-Fi Error. Incompatible with router. " + value;
            case otaPairwiseCipherInvalid:
                return "Wi-Fi Error. Incompatible with router. " + value;
            case otaAkmpInvalid:
                return "Wi-Fi Error. Incompatible with router. " + value;
            case otaUnsuppRsnIEVersion:
                return "Wi-Fi Error. Incompatible with router. " + value;
            case otaInvalidRsnIECap:
                return "Wi-Fi Error. Incompatible with router. " + value;
            case ota8021XAuthFailed:
                return "Wi-Fi Error. Incompatible with router. " + value;
            case otaCipherSuiteRejected:
                return "Wi-Fi Error. Incompatible with router. " + value;
            case otaBeaconTimeout:
                return "Wi-Fi Error. Lost connection to router. " + value;
            case otaNoApFound:
                return "Wi-Fi Error. Can't find Wi-Fi router. " + value;
            case otaAuthFail:
                return "Wi-Fi Error. Failed to connect to router " + value;
            case otaAssocFail:
                return "Wi-Fi Error. Failed to connect to router " + value;
            case otaHandshakeTimeout:
                return "Incorrect Wi-Fi password. " + value;
            case otaNoIp:
                return "Wi-Fi Error. Router not configured correctly. " + value;
            case otaDownloadFail:
                return "Could not download update. " + value;
            case reconnectError:
                return "Could not reconnect to the treadmill to verify the update. " + value;
            case versionError:
                return "Firmware update was not successful. " + value;
            case versionSuccess:
                return "Firmware update was successful";
            default:
                return "";
        }
    }

    boolean addDismissForOtaStatus(OtaUpdateStatus otaUpdateStatus) {
        return otaUpdateStatus != OtaUpdateStatus.noError;
    }

    void startOtaTimer() {
        stopOtaTimer();
        this.otaTimer = new Timer();
        this.otaTimer.schedule(new AnonymousClass2(), (long) this.otaDelay);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* renamed from: com.treadly.Treadly.UI.FirmwareUpdate.FirmwareUpdateSsidFragment$2  reason: invalid class name */
    /* loaded from: classes2.dex */
    public class AnonymousClass2 extends TimerTask {
        AnonymousClass2() {
        }

        @Override // java.util.TimerTask, java.lang.Runnable
        public void run() {
            ActivityUtil.runOnUiThread(FirmwareUpdateSsidFragment.this.getActivity(), new Runnable() { // from class: com.treadly.Treadly.UI.FirmwareUpdate.-$$Lambda$FirmwareUpdateSsidFragment$2$rqBWl-jlWJXFylrssxg36TpzxzY
                @Override // java.lang.Runnable
                public final void run() {
                    FirmwareUpdateSsidFragment.AnonymousClass2.lambda$run$0(FirmwareUpdateSsidFragment.AnonymousClass2.this);
                }
            });
        }

        public static /* synthetic */ void lambda$run$0(AnonymousClass2 anonymousClass2) {
            FirmwareUpdateSsidFragment.this.endLoadingAnimation();
            FirmwareUpdateSsidFragment.this.otaTimeout();
        }
    }

    void stopOtaTimer() {
        if (this.otaTimer != null) {
            this.otaTimer.cancel();
            this.otaTimer.purge();
        }
    }

    void otaTimeout() {
        if (this.builder != null) {
            this.builder.show().dismiss();
        }
        dismissAlertDialog();
        dismissProgressDialog();
        this.builder = null;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void startScan(View view) {
        startScan();
    }

    void startScan() {
        this.ssidList.clear();
        ActivityUtil.runOnUiThread(getActivity(), new Runnable() { // from class: com.treadly.Treadly.UI.FirmwareUpdate.-$$Lambda$FirmwareUpdateSsidFragment$zljc3datQ4Sxk7czzBHSJCkfbLg
            @Override // java.lang.Runnable
            public final void run() {
                FirmwareUpdateSsidFragment.this.adapter.notifyDataSetChanged();
            }
        });
        if (TreadlyClientLib.shared.startWifiApScan()) {
            showLoading();
            startLoadingAnimation();
            startWifiApScanTimer();
        }
    }

    void startWifiApScanTimer() {
        stopWifiApScanTimer();
        this.scanTimer = new Timer();
        this.scanTimer.schedule(new TimerTask() { // from class: com.treadly.Treadly.UI.FirmwareUpdate.FirmwareUpdateSsidFragment.3
            @Override // java.util.TimerTask, java.lang.Runnable
            public void run() {
                FirmwareUpdateSsidFragment.this.wifiApScanTimeout();
            }
        }, (long) this.scanDelay);
    }

    void stopWifiApScanTimer() {
        this.scanTimer.cancel();
        this.scanTimer.purge();
    }

    void wifiApScanTimeout() {
        ActivityUtil.runOnUiThread(getActivity(), new Runnable() { // from class: com.treadly.Treadly.UI.FirmwareUpdate.-$$Lambda$FirmwareUpdateSsidFragment$GYBvZvCSkOpmlOgLV1YC5p-j28M
            @Override // java.lang.Runnable
            public final void run() {
                FirmwareUpdateSsidFragment.lambda$wifiApScanTimeout$14(FirmwareUpdateSsidFragment.this);
            }
        });
    }

    public static /* synthetic */ void lambda$wifiApScanTimeout$14(FirmwareUpdateSsidFragment firmwareUpdateSsidFragment) {
        if (firmwareUpdateSsidFragment.ssidList.isEmpty()) {
            firmwareUpdateSsidFragment.dismissLoading();
            firmwareUpdateSsidFragment.endLoadingAnimation();
            new AlertDialog.Builder(firmwareUpdateSsidFragment.getContext()).setTitle("No Wi-Fi Networks Found").setMessage("Please try again").setNeutralButton("Dismiss", (DialogInterface.OnClickListener) null).setCancelable(false).show();
        }
    }

    void displayDeviceDisconnectedErrorAlert() {
        this.builder.show().dismiss();
        this.builder = null;
        this.progressBuilder.show().dismiss();
        this.progressBuilder = null;
        this.builder = new AlertDialog.Builder(getContext()).setTitle("Lost Connection").setMessage("Lost connection to the Treadly device. Please try again.").setNeutralButton("Dismiss", new DialogInterface.OnClickListener() { // from class: com.treadly.Treadly.UI.FirmwareUpdate.-$$Lambda$FirmwareUpdateSsidFragment$poiwYdc4HXwnb3nilzGd0wmCNCA
            @Override // android.content.DialogInterface.OnClickListener
            public final void onClick(DialogInterface dialogInterface, int i) {
                FirmwareUpdateSsidFragment.lambda$displayDeviceDisconnectedErrorAlert$15(FirmwareUpdateSsidFragment.this, dialogInterface, i);
            }
        }).setCancelable(false);
        stopOtaTimer();
        this.builder.show();
    }

    public static /* synthetic */ void lambda$displayDeviceDisconnectedErrorAlert$15(FirmwareUpdateSsidFragment firmwareUpdateSsidFragment, DialogInterface dialogInterface, int i) {
        firmwareUpdateSsidFragment.builder = null;
        if (firmwareUpdateSsidFragment.getActivity() != null) {
            firmwareUpdateSsidFragment.getActivity().getSupportFragmentManager().popBackStack();
        }
    }

    @Override // com.treadly.client.lib.sdk.Listeners.DeviceConnectionEventListener
    public void onDeviceConnectionChanged(DeviceConnectionEvent deviceConnectionEvent) {
        if (deviceConnectionEvent.getStatus() == DeviceConnectionStatus.notConnected || deviceConnectionEvent.getStatus() == DeviceConnectionStatus.disconnecting) {
            if (this.currentStage == OtaUpdateStage.updating || this.currentStage == OtaUpdateStage.done) {
                displayDeviceDisconnectedErrorAlert();
            } else if (getActivity() != null) {
                getActivity().getSupportFragmentManager().popBackStack();
            }
        }
    }
}
