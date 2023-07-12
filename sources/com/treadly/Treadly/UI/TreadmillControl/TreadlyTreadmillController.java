package com.treadly.Treadly.UI.TreadmillControl;

import android.util.Log;
import com.github.mikephil.charting.utils.Utils;
import com.treadly.Treadly.Data.Managers.RunningSessionManager;
import com.treadly.Treadly.Data.Model.UserProfileInfo;
import com.treadly.Treadly.MainActivity;
import com.treadly.Treadly.UI.TreadlyProfile.Settings.AutoRun.TreadmillAutoRunManager;
import com.treadly.Treadly.UI.TreadlyVideo.Data.VideoUploaderManager;
import com.treadly.Treadly.UI.TreadmillControl.Data.TreadlyControlContent;
import com.treadly.Treadly.UI.TreadmillControl.Data.TreadlyControlSpeedDataPoint;
import com.treadly.Treadly.UI.TreadmillControl.Data.TreadlyRunState;
import com.treadly.client.lib.sdk.Listeners.DeviceConnectionEventListener;
import com.treadly.client.lib.sdk.Listeners.RequestEventListener;
import com.treadly.client.lib.sdk.Model.AuthenticationState;
import com.treadly.client.lib.sdk.Model.ComponentInfo;
import com.treadly.client.lib.sdk.Model.ComponentType;
import com.treadly.client.lib.sdk.Model.DeviceConnectionEvent;
import com.treadly.client.lib.sdk.Model.DeviceConnectionStatus;
import com.treadly.client.lib.sdk.Model.DeviceInfo;
import com.treadly.client.lib.sdk.Model.DeviceMode;
import com.treadly.client.lib.sdk.Model.DeviceStatus;
import com.treadly.client.lib.sdk.Model.DistanceUnits;
import com.treadly.client.lib.sdk.Model.RegistrationInfo;
import com.treadly.client.lib.sdk.Model.VersionInfo;
import com.treadly.client.lib.sdk.TreadlyClientLib;
import java.util.ArrayList;

/* loaded from: classes2.dex */
public class TreadlyTreadmillController {
    private TreadlyControlContent content;
    private boolean isConnectedToDevice;
    public DeviceStatus status;
    private String targetDeviceName;
    private final String TAG = "TreadmillController";
    public TreadlyTreadmillAdapter adapter = null;
    public double queuedSpeedValue = Utils.DOUBLE_EPSILON;
    public double walkDuration = Utils.DOUBLE_EPSILON;
    public int statusCount = 0;
    public ComponentInfo[] componentList = null;
    public VersionInfo customBoardVersion = null;
    public double averageSpeed = Utils.DOUBLE_EPSILON;
    public ArrayList<TreadlyControlSpeedDataPoint> speedDataSet = new ArrayList<>();
    public ArrayList<Double> averageSpeedList = new ArrayList<>();
    public ArrayList<Double> averageSpeedMinuteList = new ArrayList<>();
    public int speedDataStartTime = 0;
    public int speedDataCurrentTime = 0;
    public TreadlyRunState runState = TreadlyRunState.STOPPED;
    public DistanceUnits speedUnits = DistanceUnits.MI;
    private int fetchComponentAttempts = 0;
    private int fetchComponentLimit = 10;
    public AuthenticationState mAuthenticationState = AuthenticationState.unknown;
    public boolean hasSessionOwnership = false;
    RequestEventListener requestAdapter = new RequestEventListener() { // from class: com.treadly.Treadly.UI.TreadmillControl.TreadlyTreadmillController.2
        @Override // com.treadly.client.lib.sdk.Listeners.RequestEventListener
        public void onRequestBleEnableResponse(boolean z) {
        }

        @Override // com.treadly.client.lib.sdk.Listeners.RequestEventListener
        public void onRequestEmergencyHandrailStopEnabledResponse(boolean z) {
        }

        @Override // com.treadly.client.lib.sdk.Listeners.RequestEventListener
        public void onRequestEmergencyStopResponse(boolean z) {
        }

        @Override // com.treadly.client.lib.sdk.Listeners.RequestEventListener
        public void onRequestFactoryResetResponse(boolean z) {
        }

        @Override // com.treadly.client.lib.sdk.Listeners.RequestEventListener
        public void onRequestGetBtAudioPAssword(boolean z, int[] iArr) {
        }

        @Override // com.treadly.client.lib.sdk.Listeners.RequestEventListener
        public void onRequestPauseResponse(boolean z) {
        }

        @Override // com.treadly.client.lib.sdk.Listeners.RequestEventListener
        public void onRequestPowerResponse(boolean z) {
        }

        @Override // com.treadly.client.lib.sdk.Listeners.RequestEventListener
        public void onRequestRegistrationRequiredEvent(RegistrationInfo registrationInfo) {
        }

        @Override // com.treadly.client.lib.sdk.Listeners.RequestEventListener
        public void onRequestResetMaintenancResponse(boolean z) {
        }

        @Override // com.treadly.client.lib.sdk.Listeners.RequestEventListener
        public void onRequestResetStopResponse(boolean z) {
        }

        @Override // com.treadly.client.lib.sdk.Listeners.RequestEventListener
        public void onRequestSetAccelerationZoneStartResponse(boolean z) {
        }

        @Override // com.treadly.client.lib.sdk.Listeners.RequestEventListener
        public void onRequestSetBtAudioPassword(boolean z) {
        }

        @Override // com.treadly.client.lib.sdk.Listeners.RequestEventListener
        public void onRequestSetDecelerationZoneEndResponse(boolean z) {
        }

        @Override // com.treadly.client.lib.sdk.Listeners.RequestEventListener
        public void onRequestSetDeletePairList(boolean z) {
        }

        @Override // com.treadly.client.lib.sdk.Listeners.RequestEventListener
        public void onRequestSetDeviceIrDebugLogMode(boolean z) {
        }

        @Override // com.treadly.client.lib.sdk.Listeners.RequestEventListener
        public void onRequestSetMaintenanceStepThresholdResponse(boolean z) {
        }

        @Override // com.treadly.client.lib.sdk.Listeners.RequestEventListener
        public void onRequestSetPairingModeTriggerType(boolean z) {
        }

        @Override // com.treadly.client.lib.sdk.Listeners.RequestEventListener
        public void onRequestSetRemoteStatusResponse(boolean z) {
        }

        @Override // com.treadly.client.lib.sdk.Listeners.RequestEventListener
        public void onRequestSetTotalStatusResponse(boolean z) {
        }

        @Override // com.treadly.client.lib.sdk.Listeners.RequestEventListener
        public void onRequestSetUnitsResponse(boolean z) {
        }

        @Override // com.treadly.client.lib.sdk.Listeners.RequestEventListener
        public void onRequestSpeedDownResponse(boolean z) {
        }

        @Override // com.treadly.client.lib.sdk.Listeners.RequestEventListener
        public void onRequestSpeedUpResponse(boolean z) {
        }

        @Override // com.treadly.client.lib.sdk.Listeners.RequestEventListener
        public void onRequestVerifyLedCode(boolean z) {
        }

        @Override // com.treadly.client.lib.sdk.Listeners.RequestEventListener
        public void onRequestWifiUrlTestResponse(boolean z) {
        }

        @Override // com.treadly.client.lib.sdk.Listeners.RequestEventListener
        public void onRequestSetSpeedResponse(boolean z) {
            if (z) {
                TreadlyTreadmillController.this.queuedSpeedValue = Utils.DOUBLE_EPSILON;
            }
        }

        @Override // com.treadly.client.lib.sdk.Listeners.RequestEventListener
        public void onRequestStatusResponse(boolean z, DeviceStatus deviceStatus) {
            if (z && deviceStatus != null) {
                TreadlyTreadmillController.this.setStatus(deviceStatus);
                TreadlyTreadmillController.this.updateRunState(TreadlyTreadmillController.this.status);
                TreadlyTreadmillController.this.updateContent(TreadlyTreadmillController.this.status);
                TreadlyTreadmillController.this.speedUnits = TreadlyTreadmillController.this.status.getSpeedInfo().getUnits();
            }
        }

        @Override // com.treadly.client.lib.sdk.Listeners.RequestEventListener
        public void onRequestGetDeviceComponentsListResponse(boolean z, ComponentInfo[] componentInfoArr) {
            if (z) {
                TreadlyTreadmillController.this.componentList = componentInfoArr;
                for (ComponentInfo componentInfo : componentInfoArr) {
                    if (componentInfo.getType() == ComponentType.bleBoard) {
                        TreadlyTreadmillController.this.customBoardVersion = componentInfo.getVersionInfo().getVersionInfo();
                    }
                }
                TreadlyTreadmillController.this.setAuthenticationState();
            }
        }

        @Override // com.treadly.client.lib.sdk.Listeners.RequestEventListener
        public void onRequestSetAuthenticationState(boolean z, AuthenticationState authenticationState) {
            if (z && TreadlyTreadmillController.this.isGen2()) {
                TreadlyTreadmillController.this.mAuthenticationState = authenticationState;
            }
        }

        @Override // com.treadly.client.lib.sdk.Listeners.RequestEventListener
        public void onRequestGetAuthenticationState(boolean z, AuthenticationState authenticationState) {
            if (!z || !TreadlyTreadmillController.this.isGen2()) {
                if (z || !TreadlyTreadmillController.this.isGen2()) {
                    return;
                }
                TreadlyTreadmillController.this.mAuthenticationState = AuthenticationState.disabled;
                return;
            }
            TreadlyTreadmillController.this.mAuthenticationState = authenticationState;
        }
    };
    DeviceConnectionEventListener deviceConnectionAdapter = new DeviceConnectionEventListener() { // from class: com.treadly.Treadly.UI.TreadmillControl.TreadlyTreadmillController.3
        @Override // com.treadly.client.lib.sdk.Listeners.DeviceConnectionEventListener
        public void onDeviceConnectionChanged(DeviceConnectionEvent deviceConnectionEvent) {
            TreadlyTreadmillController.this.targetDeviceName = null;
            if (deviceConnectionEvent.getDeviceInfo() != null) {
                TreadlyTreadmillController.this.setIsConnectedToDevice(deviceConnectionEvent.getStatus() == DeviceConnectionStatus.connected);
                if (TreadlyTreadmillController.this.isConnectedToDevice) {
                    TreadlyClientLib.shared.stopScanning();
                    TreadlyClientLib.shared.getDeviceComponentList();
                }
            }
        }

        @Override // com.treadly.client.lib.sdk.Listeners.DeviceConnectionEventListener
        public void onDeviceConnectionDeviceDiscovered(DeviceInfo deviceInfo) {
            String name = deviceInfo.getName();
            if (name == null || !name.equals(TreadlyTreadmillController.this.targetDeviceName)) {
                return;
            }
            TreadlyClientLib.shared.connectDevice(deviceInfo);
        }
    };

    /* loaded from: classes2.dex */
    public interface TreadlyTreadmillAdapter {
        void didChangeContent(TreadlyControlContent treadlyControlContent);

        void didChangeDeviceConnection(boolean z);

        void didChangeStatus(DeviceStatus deviceStatus);
    }

    public void attachCaloriesCalculator(UserProfileInfo userProfileInfo) {
    }

    public void connectToDevice() {
    }

    public void disconnectFromDevice() {
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void setIsConnectedToDevice(boolean z) {
        this.isConnectedToDevice = z;
        if (this.adapter != null) {
            this.adapter.didChangeDeviceConnection(z);
        } else {
            Log.e("TreadmillController", "Adapter not set");
        }
    }

    public boolean getIsConnectedToDevice() {
        return TreadlyClientLib.shared.isDeviceConnected();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void setStatus(DeviceStatus deviceStatus) {
        this.status = deviceStatus;
        if (this.status != null) {
            if (this.adapter != null) {
                this.adapter.didChangeStatus(this.status);
            } else {
                Log.e("TreadmillController", "Adapter not set");
            }
        }
    }

    private void setContent(TreadlyControlContent treadlyControlContent) {
        this.content = this.content;
    }

    public TreadlyControlContent getContent() {
        return this.content;
    }

    public TreadlyTreadmillController(TreadlyControlContent treadlyControlContent) {
        this.content = treadlyControlContent;
        setStatus(null);
        TreadlyClientLib.shared.addRequestEventListener(this.requestAdapter);
        TreadlyClientLib.shared.addDeviceConnectionEventListener(this.deviceConnectionAdapter);
        TreadlyClientLib.shared.getDeviceComponentList();
        setAuthenticationState();
    }

    public void finalize() {
        releaseTreadmillControl();
    }

    public void releaseTreadmillControl() {
        TreadlyClientLib.shared.removeRequestEventListener(this.requestAdapter);
        TreadlyClientLib.shared.removeDeviceConnectionEventListener(this.deviceConnectionAdapter);
    }

    public void start() {
        if (this.runState == TreadlyRunState.STOPPED) {
            powerOnDevice();
        }
    }

    public void start(double d) {
        if (this.runState == TreadlyRunState.STOPPED) {
            setSpeed(d);
        }
    }

    public void pause() {
        if (this.runState == TreadlyRunState.STARTED) {
            powerOffDevice();
        }
    }

    public void resume() {
        if (this.runState == TreadlyRunState.STOPPED) {
            powerOnDevice();
        }
    }

    public void stop() {
        if (this.runState == TreadlyRunState.STARTED) {
            powerOffDevice();
        }
    }

    public void speedUp() {
        TreadlyClientLib.shared.speedUp();
    }

    public void speedDown() {
        TreadlyClientLib.shared.speedDown();
    }

    public void startDeviceMonitoring() {
        TreadlyClientLib.shared.addDeviceConnectionEventListener(this.deviceConnectionAdapter);
    }

    public void stopDeviceMonitoring() {
        TreadlyClientLib.shared.removeDeviceConnectionEventListener(this.deviceConnectionAdapter);
    }

    public void stopDevice() {
        if (this.hasSessionOwnership) {
            TreadmillAutoRunManager.shared.stop();
            if (this.customBoardVersion != null && this.customBoardVersion.isGreaterThan(new VersionInfo(2, 5, 0))) {
                TreadlyClientLib.shared.resetPower();
            }
            TreadlyClientLib.shared.powerDevice();
        }
    }

    public void powerOffDevice() {
        if (this.hasSessionOwnership) {
            if (this.customBoardVersion.isGreaterThan(new VersionInfo(2, 14, 3))) {
                TreadlyClientLib.shared.pauseDevice();
            } else {
                TreadlyClientLib.shared.powerDevice();
            }
        }
    }

    public boolean powerOnDevice() {
        if (this.status == null || this.status.isPoweredOn()) {
            return false;
        }
        boolean powerDevice = TreadlyClientLib.shared.powerDevice();
        Log.i("TreadmillController", "powering device");
        if (this.status.getMode() == DeviceMode.IDLE) {
            boolean powerDevice2 = TreadlyClientLib.shared.powerDevice();
            Log.i("TreadmillController", "idle");
            return powerDevice2;
        }
        return powerDevice;
    }

    public void setSpeed(double d) {
        if (this.status != null && TreadlyClientLib.shared.isDeviceConnected()) {
            Log.i("WG control", "Set Speed: " + d);
            float f = (float) d;
            if (d > this.status.getSpeedInfo().getMaximumSpeed()) {
                f = this.status.getSpeedInfo().getMaximumSpeed();
            }
            int i = (d > Utils.DOUBLE_EPSILON ? 1 : (d == Utils.DOUBLE_EPSILON ? 0 : -1));
            if (i > 0 && d < this.status.getSpeedInfo().getMinimumSpeed()) {
                f = this.status.getSpeedInfo().getMinimumSpeed();
            }
            if (i == 0) {
                Log.i("TreadmillController", "Speed: 0 -> stopping");
                stop();
            } else if (this.status.isPoweredOn() && this.status.getSpeedInfo().getTargetSpeed() > Utils.DOUBLE_EPSILON) {
                Log.i("WG control", "setting speed: " + f);
                TreadlyClientLib.shared.setSpeed(f);
                this.statusCount = 2;
            } else if (this.queuedSpeedValue == Utils.DOUBLE_EPSILON || this.runState != TreadlyRunState.STARTING || this.runState != TreadlyRunState.STARTED) {
                Log.i("WG control", "powering on");
                powerOnDevice();
                this.queuedSpeedValue = f;
            } else {
                Log.i("WG control", "queueing");
                this.queuedSpeedValue = f;
            }
        }
    }

    public void updateContent(DeviceStatus deviceStatus) {
        updateSpeed(deviceStatus);
        this.content.stepsCount = deviceStatus.getSteps();
        this.content.walkDistance = deviceStatus.getDistance();
        this.content.elapsedTime = deviceStatus.getSeconds();
        this.content.burnedCaloriesCount = RunningSessionManager.getInstance().calories;
        if (RunningSessionManager.getInstance().userProfile != null) {
            this.content.dailyCaloriesGoal = RunningSessionManager.getInstance().userProfile.caloriesGoal();
        }
        this.content.speedValue = deviceStatus.getSpeedInfo().getTargetSpeed();
        this.content.queuedSpeed = this.queuedSpeedValue;
        this.content.runState = this.runState;
        this.content.speedUnits = deviceStatus.getDistanceUnits();
        this.content.isPairing = deviceStatus.getPairingModeState();
        this.content.authenticationState = this.mAuthenticationState;
        updateSpeedData(deviceStatus);
        this.content.averageSpeed = RunningSessionManager.getInstance().averageSpeed;
        this.content.speedDataSet = this.speedDataSet;
        this.hasSessionOwnership = checkSessionOwner(deviceStatus);
        this.content.hasSessionOwnership = this.hasSessionOwnership;
        this.adapter.didChangeContent(this.content);
    }

    public boolean checkSessionOwner(DeviceStatus deviceStatus) {
        VersionInfo versionInfo = new VersionInfo(3, 14, 0);
        if (this.customBoardVersion == null) {
            return false;
        }
        if ((this.customBoardVersion.isGreaterThan(versionInfo) || this.customBoardVersion.equals(versionInfo)) && deviceStatus.isPoweredOn() && deviceStatus.getSpeedInfo().getTargetSpeed() > Utils.DOUBLE_EPSILON) {
            return deviceStatus.isSessionOwnership();
        }
        return true;
    }

    public void updateSpeed(DeviceStatus deviceStatus) {
        if (deviceStatus.isPoweredOn() && deviceStatus.getSpeedInfo().getTargetSpeed() > Utils.DOUBLE_EPSILON && this.queuedSpeedValue > Utils.DOUBLE_EPSILON) {
            TreadlyClientLib.shared.setSpeed((float) this.queuedSpeedValue);
        }
        if (this.queuedSpeedValue == Utils.DOUBLE_EPSILON) {
            if (this.statusCount == 0) {
                this.content.speedValue = deviceStatus.getSpeedInfo().getTargetSpeed();
                this.content.maximumSpeedValue = deviceStatus.getSpeedInfo().getMaximumSpeed();
                this.content.minimumSpeedValue = deviceStatus.getSpeedInfo().getMinimumSpeed();
            } else if (this.statusCount > 0) {
                this.statusCount--;
            }
        }
    }

    public void updateSpeedData(DeviceStatus deviceStatus) {
        double targetSpeed = deviceStatus.getSpeedInfo().getTargetSpeed();
        ArrayList<TreadlyControlSpeedDataPoint> arrayList = new ArrayList<>();
        arrayList.add(new TreadlyControlSpeedDataPoint(targetSpeed, 0));
        for (int i = 0; i < 6; i++) {
            if (i < this.speedDataSet.size()) {
                arrayList.add(new TreadlyControlSpeedDataPoint(this.speedDataSet.get(i).speed, i + 1));
            } else {
                arrayList.add(new TreadlyControlSpeedDataPoint(Utils.DOUBLE_EPSILON, i + 1));
            }
        }
        this.speedDataSet = arrayList;
    }

    public void updateRunState(DeviceStatus deviceStatus) {
        if (deviceStatus.getMode() == DeviceMode.ACTIVE) {
            if (deviceStatus.getSpeedInfo().getTargetSpeed() == Utils.DOUBLE_EPSILON) {
                this.runState = this.runState == TreadlyRunState.STOPPED || this.runState == TreadlyRunState.STARTING ? TreadlyRunState.STARTING : TreadlyRunState.STOPPING;
            } else {
                this.runState = TreadlyRunState.STARTED;
            }
        }
    }

    public void setAuthenticationState() {
        if (this.customBoardVersion == null) {
            MainActivity mainActivity = (MainActivity) VideoUploaderManager.context;
            if (mainActivity == null) {
                return;
            }
            mainActivity.runOnUiThread(new Runnable() { // from class: com.treadly.Treadly.UI.TreadmillControl.TreadlyTreadmillController.1
                @Override // java.lang.Runnable
                public void run() {
                    TreadlyClientLib.shared.getDeviceComponentList();
                }
            });
            return;
        }
        this.fetchComponentAttempts = 0;
        if (isGen2()) {
            TreadlyClientLib.shared.getAuthenticationState();
        } else {
            this.mAuthenticationState = AuthenticationState.active;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean isGen2() {
        if (this.customBoardVersion == null) {
            return false;
        }
        VersionInfo versionInfo = new VersionInfo(2, 0, 0);
        return this.customBoardVersion.isGreaterThan(versionInfo) || this.customBoardVersion.isEqual(versionInfo);
    }
}
