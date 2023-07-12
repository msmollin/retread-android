package com.treadly.client.lib.sdk;

import android.app.Activity;
import com.treadly.client.lib.sdk.Bluetooth.BluetoothHandler;
import com.treadly.client.lib.sdk.Listeners.BluetoothConnectionEventListener;
import com.treadly.client.lib.sdk.Listeners.DeviceConnectionEventListener;
import com.treadly.client.lib.sdk.Listeners.DeviceUserStatsLogEventListener;
import com.treadly.client.lib.sdk.Listeners.GameModeEventListener;
import com.treadly.client.lib.sdk.Listeners.OtaUpdateRequestEventListener;
import com.treadly.client.lib.sdk.Listeners.RequestEventListener;
import com.treadly.client.lib.sdk.Listeners.TestRequestEventListener;
import com.treadly.client.lib.sdk.Listeners.UserInteractionEventListener;
import com.treadly.client.lib.sdk.Managers.ActivationManager;
import com.treadly.client.lib.sdk.Managers.ActiveDeviceManager;
import com.treadly.client.lib.sdk.Managers.AppManager;
import com.treadly.client.lib.sdk.Managers.BleConnectionManager;
import com.treadly.client.lib.sdk.Managers.CloudNetworkManager;
import com.treadly.client.lib.sdk.Managers.DeviceManager;
import com.treadly.client.lib.sdk.Managers.DeviceUserStatsLogManager;
import com.treadly.client.lib.sdk.Managers.OtaUpdateManager;
import com.treadly.client.lib.sdk.Managers.SharedPreferences;
import com.treadly.client.lib.sdk.Managers.TreadlyLogManager;
import com.treadly.client.lib.sdk.Model.AuthenticationState;
import com.treadly.client.lib.sdk.Model.BluetoothConnectionState;
import com.treadly.client.lib.sdk.Model.ComponentInfo;
import com.treadly.client.lib.sdk.Model.DeviceConnectionStatus;
import com.treadly.client.lib.sdk.Model.DeviceInfo;
import com.treadly.client.lib.sdk.Model.DistanceUnits;
import com.treadly.client.lib.sdk.Model.FactoryTestResults;
import com.treadly.client.lib.sdk.Model.GameModeSegmentDisplay;
import com.treadly.client.lib.sdk.Model.GameModeType;
import com.treadly.client.lib.sdk.Model.IrMode;
import com.treadly.client.lib.sdk.Model.PairingModeTriggerType;
import com.treadly.client.lib.sdk.Model.RemoteStatus;
import com.treadly.client.lib.sdk.Model.TotalStatus;
import com.treadly.client.lib.sdk.Model.VersionInfo;
import com.treadly.client.lib.sdk.Model.WifiApInfo;
import com.treadly.client.lib.sdk.Model.WifiUrlTestInfo;
import java.util.Date;

/* loaded from: classes2.dex */
public class TreadlyClientLib {
    public static TreadlyClientLib shared = new TreadlyClientLib();
    private boolean adminEnabled = false;
    private boolean factoryTestEnabled = false;

    private TreadlyClientLib() {
        CloudNetworkManager.shared.authenticateApp();
    }

    public void init(Activity activity) {
        BluetoothHandler.sharedInstance().init(activity);
        CloudNetworkManager.shared.init(activity);
        SharedPreferences.shared.init(activity);
        TreadlyLogManager.shared.init(activity);
        this.adminEnabled = CloudNetworkManager.shared.authenticateAdmin();
    }

    public boolean isAdminEnabled() {
        return this.adminEnabled;
    }

    public void addDeviceConnectionEventListener(DeviceConnectionEventListener deviceConnectionEventListener) {
        BleConnectionManager.shared.addDeviceConnectionEventListener(deviceConnectionEventListener);
    }

    public void removeDeviceConnectionEventListener(DeviceConnectionEventListener deviceConnectionEventListener) {
        BleConnectionManager.shared.removeDeviceConnectionEventListener(deviceConnectionEventListener);
    }

    public void addRequestEventListener(RequestEventListener requestEventListener) {
        DeviceManager.shared.addRequestEventListener(requestEventListener);
    }

    public void removeRequestEventListener(RequestEventListener requestEventListener) {
        DeviceManager.shared.removeRequestEventListener(requestEventListener);
    }

    public void addTestRequestEventListener(TestRequestEventListener testRequestEventListener) {
        DeviceManager.shared.addTestRequestEventListener(testRequestEventListener);
    }

    public void removeTestRequestEventListener(TestRequestEventListener testRequestEventListener) {
        DeviceManager.shared.removeTestRequestEventListener(testRequestEventListener);
    }

    public void addOtaUpdateRequestEventListener(OtaUpdateRequestEventListener otaUpdateRequestEventListener) {
        OtaUpdateManager.shared.addOtaUpdateEventListener(otaUpdateRequestEventListener);
    }

    public void removeOtaUpdateRequestEventListener(OtaUpdateRequestEventListener otaUpdateRequestEventListener) {
        OtaUpdateManager.shared.removeOtaUpdateEventlistener(otaUpdateRequestEventListener);
    }

    public void addBluetoothConnectionEventListener(BluetoothConnectionEventListener bluetoothConnectionEventListener) {
        BleConnectionManager.shared.addBluetoothConnectionEventListener(bluetoothConnectionEventListener);
    }

    public void removeBluetoothConnectionEventListener(BluetoothConnectionEventListener bluetoothConnectionEventListener) {
        BleConnectionManager.shared.removeBluetoothConnectionEventListener(bluetoothConnectionEventListener);
    }

    public void addDeviceUserStatsLogEventListener(DeviceUserStatsLogEventListener deviceUserStatsLogEventListener) {
        DeviceUserStatsLogManager.sharedInstance.addEventListener(deviceUserStatsLogEventListener);
    }

    public void removeDeviceUserStatsLogEventListener(DeviceUserStatsLogEventListener deviceUserStatsLogEventListener) {
        DeviceUserStatsLogManager.sharedInstance.removeEventListener(deviceUserStatsLogEventListener);
    }

    public void removeDeviceUserStatsLog(int i) {
        DeviceUserStatsLogManager.sharedInstance.removeDeviceUserStatsLog(i);
    }

    public void removeDeviceUserStatsLogV2(int i) {
        DeviceUserStatsLogManager.sharedInstance.removeDeviceUserStatsLogV2(i);
    }

    public void startScanning(long j) {
        startScanning(j, true);
    }

    public void startScanning(long j, boolean z) {
        BleConnectionManager.shared.startScanning(j, z);
    }

    public void stopScanning() {
        BleConnectionManager.shared.stopScanning();
    }

    public void connectDevice(DeviceInfo deviceInfo) {
        BleConnectionManager.shared.connectDevice(deviceInfo);
    }

    public void disconnect() {
        BleConnectionManager.shared.disconnect();
    }

    public boolean isDeviceConnected() {
        return BleConnectionManager.shared.isDeviceConnected();
    }

    public boolean isDeviceStatusConnected() {
        DeviceConnectionStatus deviceConnectionStatus = BleConnectionManager.shared.currentDeviceConnectionStatus;
        if (deviceConnectionStatus == null) {
            deviceConnectionStatus = DeviceConnectionStatus.notConnected;
        }
        return deviceConnectionStatus == DeviceConnectionStatus.connected;
    }

    public boolean powerDevice() {
        return DeviceManager.shared.sendPowerRequest();
    }

    public boolean pauseDevice() {
        return DeviceManager.shared.sendPauseRequest();
    }

    public boolean setSpeed(float f) {
        return DeviceManager.shared.sendSetSpeedRequest(f);
    }

    public boolean speedUp() {
        return DeviceManager.shared.sendSpeedUpRequest();
    }

    public boolean speedDown() {
        return DeviceManager.shared.sendSpeedDownRequest();
    }

    public boolean setUnits(DistanceUnits distanceUnits) {
        return DeviceManager.shared.sendSetSpeedUnitsRequest(distanceUnits);
    }

    public boolean factoryReset() {
        return DeviceManager.shared.sendFactoryResetRequest();
    }

    public boolean getDeviceStatus() {
        return DeviceManager.shared.getDeviceStatus();
    }

    public boolean getDeviceComponentList() {
        return DeviceManager.shared.getDeviceComponentInfo();
    }

    public boolean setAccelerationZone(int i) {
        return DeviceManager.shared.sendAccelerationZoneStart(i);
    }

    public boolean setDecelerationZone(int i) {
        return DeviceManager.shared.sendDecelerationZoneEnd(i);
    }

    public boolean emergencyStop() {
        return DeviceManager.shared.handleEmergencyStop();
    }

    public boolean setEmergencyHandrailStopEnabled(boolean z) {
        if (this.adminEnabled) {
            return DeviceManager.shared.setEmergencyHandrailStopEnabled(z);
        }
        return false;
    }

    public boolean setMaintenanceStepThreshold(int i) {
        return DeviceManager.shared.sendSetMaintenanceThreshold(i);
    }

    public boolean resetMaintenance(boolean z) {
        return DeviceManager.shared.sendResetMaintenance(z);
    }

    public boolean resetMaintenance() {
        return DeviceManager.shared.sendResetMaintenance();
    }

    public boolean setBleEnable(boolean z) {
        if (this.adminEnabled) {
            return DeviceManager.shared.sendBleEnableRequest(z);
        }
        return false;
    }

    public void startTest(FactoryTestResults factoryTestResults) {
        DeviceManager.shared.sendTestStartRequest();
    }

    public void sendFactoryTestLogs(FactoryTestResults factoryTestResults, CloudNetworkManager.OnSuccessListener onSuccessListener) {
        CloudNetworkManager.shared.sendFactoryTestResults(factoryTestResults, onSuccessListener);
    }

    public VersionInfo getLatestAppVersion() {
        return AppManager.shared.getLatestAppVersion();
    }

    public boolean setIrMode(IrMode irMode) {
        return DeviceManager.shared.sendSetIrModeRequest(irMode);
    }

    public boolean checkFirmwareUpdates(ComponentInfo componentInfo) {
        return checkFirmwareUpdates(componentInfo, false);
    }

    public boolean checkFirmwareUpdates(ComponentInfo componentInfo, boolean z) {
        return OtaUpdateManager.shared.checkAvailableUpdates(componentInfo, z);
    }

    public boolean startWifiApScan() {
        return OtaUpdateManager.shared.startWifiApScan();
    }

    public boolean otaUpdate(WifiApInfo wifiApInfo) {
        return OtaUpdateManager.shared.otaUpdate(wifiApInfo);
    }

    public boolean otaConfigSettings(WifiApInfo wifiApInfo) {
        return OtaUpdateManager.shared.otaConfigSettings(wifiApInfo);
    }

    public boolean getOtaConfigSettings() {
        return OtaUpdateManager.shared.getOtaConfigSettings();
    }

    public boolean clearOtaConfigSettings() {
        return OtaUpdateManager.shared.clearOtaConfigSettings();
    }

    public void otaUpdateSetMode() {
        OtaUpdateManager.shared.sendSetMode();
    }

    public boolean setTotalStatus(TotalStatus totalStatus) {
        return DeviceManager.shared.sendSetTotalStatusRequest(totalStatus);
    }

    public boolean setRemoteStatus(RemoteStatus remoteStatus) {
        return DeviceManager.shared.sendSetRemoteStatusRequest(remoteStatus);
    }

    public boolean resetPower() {
        return DeviceManager.shared.sendResetStopRequest();
    }

    public boolean setAuthenticationState(AuthenticationState authenticationState) {
        return DeviceManager.shared.sendAuthenticationStateChange(authenticationState);
    }

    public boolean getAuthenticationState() {
        return DeviceManager.shared.fetchAuthenticationState();
    }

    public void startActivation() {
        ActivationManager.shared.startActivation();
    }

    public void submitActivationCode(String str, ComponentInfo componentInfo) {
        ActivationManager.shared.submitActivationCode(str, componentInfo);
    }

    public boolean startWifiUrlTest(WifiUrlTestInfo wifiUrlTestInfo) {
        return DeviceManager.shared.sendStartWifiUrlTest(wifiUrlTestInfo);
    }

    public boolean sendGetDeviceDebugLogEvents() {
        return DeviceManager.shared.sendGetDeviceDebugLogEvents();
    }

    public boolean sendGetDeviceIrDebugLogEvents() {
        return DeviceManager.shared.sendGetDeviceIrDebugLogEvents();
    }

    public boolean setDeviceInactive() {
        return ActiveDeviceManager.sharedInstance.setDeviceInactive();
    }

    public boolean setDeviceActive(boolean z) {
        return ActiveDeviceManager.sharedInstance.setDeviceActive(z);
    }

    public boolean setDeviceActive() {
        return setDeviceActive(false);
    }

    public BluetoothConnectionState getBluetoothConnectionSTate() {
        return BleConnectionManager.shared.getBluetoothConnectionState();
    }

    public boolean setDeviceUserStatsLogs(String str, boolean z) {
        return DeviceUserStatsLogManager.sharedInstance.setDeviceUserStatsLogs(str, z);
    }

    public boolean getDeviceUserStatsLog(int i) {
        return DeviceUserStatsLogManager.sharedInstance.getDeviceUserStatsLog(i);
    }

    public boolean getDeviceUserStatsLogV2(int i) {
        return DeviceUserStatsLogManager.sharedInstance.getDeviceUserStatsLogV2(i);
    }

    public boolean claimDeviceUserStatsActiveLog() {
        return DeviceUserStatsLogManager.sharedInstance.claimDeviceUserSTatsActiveLog();
    }

    public boolean getUnclaimedUserStatsLogInfo() {
        return DeviceUserStatsLogManager.sharedInstance.getUnclaimedUserStatsLogInfo();
    }

    public boolean claimUnclaimedUsersStatsLogInfo(Date date, int i) {
        return DeviceUserStatsLogManager.sharedInstance.claimUnclaimedUserStatsLogInfo(date, i);
    }

    public boolean getBtAudioPassword() {
        return DeviceManager.shared.sendGetBtAudioPassword();
    }

    public boolean setBtAudioPassword(byte[] bArr) {
        return DeviceManager.shared.sendSetBtAudioPassword(bArr);
    }

    public boolean verifyLedCode(String str) {
        return DeviceManager.shared.sendVerifyLedCode(str);
    }

    public boolean setPairingModeTriggerType(PairingModeTriggerType pairingModeTriggerType) {
        return DeviceManager.shared.sendSetPairingModeTriggerType(pairingModeTriggerType);
    }

    public boolean setDeviceIrDebugMode(boolean z) {
        return DeviceManager.shared.sendSetDeviceIrDebugLogMode(z);
    }

    public void addUserInteractionEventListener(UserInteractionEventListener userInteractionEventListener) {
        DeviceManager.shared.addUserInteractionEventListener(userInteractionEventListener);
    }

    public void removeUserInteractionEventListener(UserInteractionEventListener userInteractionEventListener) {
        DeviceManager.shared.removeUserInteractionEventListener(userInteractionEventListener);
    }

    public boolean setEnableUserInteraction(boolean z) {
        return DeviceManager.shared.sendSetEnableUserInteraction(z);
    }

    public boolean getUserInteractionStatus() {
        return DeviceManager.shared.sendGetUserInteractionStatus();
    }

    public boolean sendOtaDoneConfirmation() {
        return DeviceManager.shared.sendOtaDoneConfirmation();
    }

    public boolean setGameMode(boolean z, GameModeType gameModeType) {
        return DeviceManager.shared.sendSetGameMode(z, gameModeType);
    }

    public boolean setGameModeDisplay(GameModeSegmentDisplay gameModeSegmentDisplay) {
        return DeviceManager.shared.sendSetGameModeDisplay(gameModeSegmentDisplay);
    }

    public void addGameModeEventListener(GameModeEventListener gameModeEventListener) {
        DeviceManager.shared.addGameModeEventListener(gameModeEventListener);
    }

    public void removeGameModeEventListener(GameModeEventListener gameModeEventListener) {
        DeviceManager.shared.removeGameModeEventListener(gameModeEventListener);
    }

    public boolean startBleRemoteTest() {
        return DeviceManager.shared.sendBleRemoteTestStart();
    }

    public boolean setIdlePauseConfig(boolean z, byte b) {
        return DeviceManager.shared.sendSetPauseAfterIdleResponse(z, b);
    }

    public boolean setDeletePairList(boolean z) {
        return DeviceManager.shared.sendDeletePairedPhones(z);
    }

    public void getAuthenticateReferenceCode(ComponentInfo componentInfo) {
        ActivationManager.shared.fetchAuthenticateReferenceCode(componentInfo);
    }
}
