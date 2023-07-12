package com.treadly.client.lib.sdk.Managers;

import android.os.Handler;
import android.os.Looper;
import com.treadly.client.lib.sdk.Interfaces.ClientSystemQueue;
import com.treadly.client.lib.sdk.Listeners.BleRemoteTestEventListener;
import com.treadly.client.lib.sdk.Listeners.GameModeEventListener;
import com.treadly.client.lib.sdk.Listeners.RequestEventListener;
import com.treadly.client.lib.sdk.Listeners.TestRequestEventListener;
import com.treadly.client.lib.sdk.Listeners.UserInteractionEventListener;
import com.treadly.client.lib.sdk.Managers.CloudNetworkManager;
import com.treadly.client.lib.sdk.Model.AuthenticationState;
import com.treadly.client.lib.sdk.Model.BleRemoteTestResults;
import com.treadly.client.lib.sdk.Model.ComponentInfo;
import com.treadly.client.lib.sdk.Model.ComponentType;
import com.treadly.client.lib.sdk.Model.ConnectionStatusCode;
import com.treadly.client.lib.sdk.Model.DeviceConnectionEvent;
import com.treadly.client.lib.sdk.Model.DeviceConnectionStatus;
import com.treadly.client.lib.sdk.Model.DeviceInfo;
import com.treadly.client.lib.sdk.Model.DeviceSecureAuthenticateInfo;
import com.treadly.client.lib.sdk.Model.DeviceStatus;
import com.treadly.client.lib.sdk.Model.DistanceUnits;
import com.treadly.client.lib.sdk.Model.GameModeSegmentDisplay;
import com.treadly.client.lib.sdk.Model.GameModeType;
import com.treadly.client.lib.sdk.Model.IrMode;
import com.treadly.client.lib.sdk.Model.PairingModeTriggerType;
import com.treadly.client.lib.sdk.Model.RegistrationInfo;
import com.treadly.client.lib.sdk.Model.RegistrationState;
import com.treadly.client.lib.sdk.Model.RemoteStatus;
import com.treadly.client.lib.sdk.Model.ResponseMessage;
import com.treadly.client.lib.sdk.Model.ResponseStatus;
import com.treadly.client.lib.sdk.Model.TestNotification;
import com.treadly.client.lib.sdk.Model.TotalStatus;
import com.treadly.client.lib.sdk.Model.UserInteractionHandrailEvent;
import com.treadly.client.lib.sdk.Model.UserInteractionHandrailStepEventInfo;
import com.treadly.client.lib.sdk.Model.UserInteractionStatus;
import com.treadly.client.lib.sdk.Model.UserInteractionSteps;
import com.treadly.client.lib.sdk.Model.WifiUrlTestInfo;
import com.treadly.client.lib.sdk.TreadlyClientLib;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

/* loaded from: classes2.dex */
public class DeviceManager {
    public static final DeviceManager shared = new DeviceManager();
    private DeviceStatus currentDeviceStatusCache;
    private DeviceSecureAuthenticateInfo currentSecureAuthenticateInfo;
    protected ClientSystemQueue messageQueue = new DefaultClientSystemQueue();
    protected DeviceConnectionStatus currentConnectionStatus = DeviceConnectionStatus.notConnected;
    protected boolean currentAuthenticationStatus = false;
    private boolean isPendingBleEnable = false;
    private boolean pendingBleEnable = false;
    protected boolean isPendingRegistrationUpdate = false;
    protected String pendingActivationCodeSubmitAcknowledge = null;
    private List<RequestEventListener> currentRequestListeners = new ArrayList();
    private List<UserInteractionEventListener> currentUserInteractionListeners = new ArrayList();
    private List<TestRequestEventListener> currentTestRequestListeners = new ArrayList();
    private List<BleRemoteTestEventListener> currentBleRemoteTestListeners = new ArrayList();
    private List<GameModeEventListener> currentGameModeListeners = new ArrayList();
    private boolean managerEnabled = false;

    /* JADX INFO: Access modifiers changed from: protected */
    public void setManagerEnabled(boolean z) {
        this.managerEnabled = z;
    }

    public void addRequestEventListener(RequestEventListener requestEventListener) {
        boolean z = false;
        for (RequestEventListener requestEventListener2 : this.currentRequestListeners) {
            if (requestEventListener2 == requestEventListener) {
                z = true;
            }
        }
        if (z) {
            return;
        }
        this.currentRequestListeners.add(requestEventListener);
    }

    public void removeRequestEventListener(RequestEventListener requestEventListener) {
        this.currentRequestListeners.remove(requestEventListener);
    }

    public void sendOnRequestSetSpeedResponse(boolean z) {
        for (RequestEventListener requestEventListener : this.currentRequestListeners) {
            requestEventListener.onRequestSetSpeedResponse(z);
        }
    }

    public void sendOnRequestSpeedUpResponse(boolean z) {
        for (RequestEventListener requestEventListener : this.currentRequestListeners) {
            requestEventListener.onRequestSpeedUpResponse(z);
        }
    }

    public void sendOnRequestSpeedDownResponse(boolean z) {
        for (RequestEventListener requestEventListener : this.currentRequestListeners) {
            requestEventListener.onRequestSpeedDownResponse(z);
        }
    }

    public void sendRequestSetSpeedResponse(boolean z) {
        for (RequestEventListener requestEventListener : this.currentRequestListeners) {
            requestEventListener.onRequestSetSpeedResponse(z);
        }
    }

    public void sendRequestPowerEvent(boolean z) {
        for (RequestEventListener requestEventListener : this.currentRequestListeners) {
            requestEventListener.onRequestPowerResponse(z);
        }
    }

    public void sendRequestPauseEvent(boolean z) {
        for (RequestEventListener requestEventListener : this.currentRequestListeners) {
            requestEventListener.onRequestPauseResponse(z);
        }
    }

    public void sendRequestSpeedSetEvent(boolean z) {
        for (RequestEventListener requestEventListener : this.currentRequestListeners) {
            requestEventListener.onRequestSetSpeedResponse(z);
        }
    }

    public void sendRequestSpeedUpEvent(boolean z) {
        for (RequestEventListener requestEventListener : this.currentRequestListeners) {
            requestEventListener.onRequestSpeedUpResponse(z);
        }
    }

    public void sendRequestSpeedDownEvent(boolean z) {
        for (RequestEventListener requestEventListener : this.currentRequestListeners) {
            requestEventListener.onRequestSpeedDownResponse(z);
        }
    }

    public void sendRequestFactoryResetEvent(boolean z) {
        for (RequestEventListener requestEventListener : this.currentRequestListeners) {
            requestEventListener.onRequestFactoryResetResponse(z);
        }
    }

    public void sendRequestSetUnitsEvent(boolean z) {
        for (RequestEventListener requestEventListener : this.currentRequestListeners) {
            requestEventListener.onRequestSetUnitsResponse(z);
        }
    }

    public void sendOnRequestStatusEvent(boolean z, DeviceStatus deviceStatus) {
        for (RequestEventListener requestEventListener : this.currentRequestListeners) {
            requestEventListener.onRequestStatusResponse(z, deviceStatus);
        }
    }

    public void sendRequestGetDeviceComponentEvent(boolean z, ComponentInfo[] componentInfoArr) {
        for (RequestEventListener requestEventListener : this.currentRequestListeners) {
            requestEventListener.onRequestGetDeviceComponentsListResponse(z, componentInfoArr);
        }
    }

    public void sendRequestSetAccelerationStartEvent(boolean z) {
        for (RequestEventListener requestEventListener : this.currentRequestListeners) {
            requestEventListener.onRequestSetAccelerationZoneStartResponse(z);
        }
    }

    public void sendRequestSetDecelerationEndEvent(boolean z) {
        for (RequestEventListener requestEventListener : this.currentRequestListeners) {
            requestEventListener.onRequestSetDecelerationZoneEndResponse(z);
        }
    }

    public void sendRequestEmergencyStopResponse(boolean z) {
        for (RequestEventListener requestEventListener : this.currentRequestListeners) {
            requestEventListener.onRequestEmergencyStopResponse(z);
        }
    }

    public void sendRequestSetEmergencyHandrailStopEnableResponse(boolean z) {
        for (RequestEventListener requestEventListener : this.currentRequestListeners) {
            requestEventListener.onRequestEmergencyHandrailStopEnabledResponse(z);
        }
    }

    public void sendRequestSetMaintenanceStepThresholdResponse(boolean z) {
        for (RequestEventListener requestEventListener : this.currentRequestListeners) {
            requestEventListener.onRequestSetMaintenanceStepThresholdResponse(z);
        }
    }

    public void sendRequestResetMaintenanceResponse(boolean z) {
        for (RequestEventListener requestEventListener : this.currentRequestListeners) {
            requestEventListener.onRequestResetMaintenancResponse(z);
        }
    }

    public void sendRequestBleEnableResponse(boolean z) {
        for (RequestEventListener requestEventListener : this.currentRequestListeners) {
            requestEventListener.onRequestBleEnableResponse(z);
        }
    }

    public void sendRequestRegistrationRequiredResponse(RegistrationInfo registrationInfo) {
        for (RequestEventListener requestEventListener : this.currentRequestListeners) {
            requestEventListener.onRequestRegistrationRequiredEvent(registrationInfo);
        }
    }

    public void sendRequestSetTotalStatusResponse(boolean z) {
        for (RequestEventListener requestEventListener : this.currentRequestListeners) {
            requestEventListener.onRequestSetTotalStatusResponse(z);
        }
    }

    public void sendRequestSetRemoteStatusResponse(boolean z) {
        for (RequestEventListener requestEventListener : this.currentRequestListeners) {
            requestEventListener.onRequestSetRemoteStatusResponse(z);
        }
    }

    public void sendRequestResetStopResponse(boolean z) {
        for (RequestEventListener requestEventListener : this.currentRequestListeners) {
            requestEventListener.onRequestResetStopResponse(z);
        }
    }

    public void sendRequestSetAuthenticationState(boolean z, AuthenticationState authenticationState) {
        for (RequestEventListener requestEventListener : this.currentRequestListeners) {
            requestEventListener.onRequestSetAuthenticationState(z, authenticationState);
        }
    }

    public void sendRequestGetAuthenticationState(boolean z, AuthenticationState authenticationState) {
        for (RequestEventListener requestEventListener : this.currentRequestListeners) {
            requestEventListener.onRequestGetAuthenticationState(z, authenticationState);
        }
    }

    public void sendRequestWifiUrlTestResponse(boolean z) {
        for (RequestEventListener requestEventListener : this.currentRequestListeners) {
            requestEventListener.onRequestWifiUrlTestResponse(z);
        }
    }

    public void sendRequestGetBtAudioPasswordResponse(boolean z, int[] iArr) {
        for (RequestEventListener requestEventListener : this.currentRequestListeners) {
            requestEventListener.onRequestGetBtAudioPAssword(z, iArr);
        }
    }

    public void sendRequestSetBtAudioPasswordResponse(boolean z) {
        for (RequestEventListener requestEventListener : this.currentRequestListeners) {
            requestEventListener.onRequestSetBtAudioPassword(z);
        }
    }

    public void sendRequestVerifyLedResponse(boolean z) {
        for (RequestEventListener requestEventListener : this.currentRequestListeners) {
            requestEventListener.onRequestVerifyLedCode(z);
        }
    }

    public void sendRequestSetPairingModeTriggerTypeResponse(boolean z) {
        for (RequestEventListener requestEventListener : this.currentRequestListeners) {
            requestEventListener.onRequestSetPairingModeTriggerType(z);
        }
    }

    public void sendRequestSetDeletePairList(boolean z) {
        for (RequestEventListener requestEventListener : this.currentRequestListeners) {
            requestEventListener.onRequestSetDeletePairList(z);
        }
    }

    public void sendRequestSetDeviceIrDebugLogModeResponse(boolean z) {
        for (RequestEventListener requestEventListener : this.currentRequestListeners) {
            requestEventListener.onRequestSetDeviceIrDebugLogMode(z);
        }
    }

    protected void sendUserInteractionStepsEvent(boolean z, UserInteractionSteps userInteractionSteps) {
        for (UserInteractionEventListener userInteractionEventListener : this.currentUserInteractionListeners) {
            userInteractionEventListener.onUserInteractionStepsDetected(z, userInteractionSteps);
        }
    }

    protected void sendUserInteractionStatusEvent(boolean z, UserInteractionStatus userInteractionStatus) {
        for (UserInteractionEventListener userInteractionEventListener : this.currentUserInteractionListeners) {
            userInteractionEventListener.onUserInteractionStatus(z, userInteractionStatus);
        }
    }

    protected void sendUserInteractionHandrailEvent(boolean z, UserInteractionHandrailEvent userInteractionHandrailEvent) {
        for (UserInteractionEventListener userInteractionEventListener : this.currentUserInteractionListeners) {
            userInteractionEventListener.onUserInteractionHandrailEvent(z, userInteractionHandrailEvent);
        }
    }

    public void addUserInteractionEventListener(UserInteractionEventListener userInteractionEventListener) {
        boolean z = false;
        for (UserInteractionEventListener userInteractionEventListener2 : this.currentUserInteractionListeners) {
            if (userInteractionEventListener2 == userInteractionEventListener) {
                z = true;
            }
        }
        if (z) {
            return;
        }
        this.currentUserInteractionListeners.add(userInteractionEventListener);
    }

    public void removeUserInteractionEventListener(UserInteractionEventListener userInteractionEventListener) {
        this.currentUserInteractionListeners.remove(userInteractionEventListener);
    }

    public void addTestRequestEventListener(TestRequestEventListener testRequestEventListener) {
        boolean z = false;
        for (TestRequestEventListener testRequestEventListener2 : this.currentTestRequestListeners) {
            if (testRequestEventListener2 == testRequestEventListener) {
                z = true;
            }
        }
        if (z) {
            return;
        }
        this.currentTestRequestListeners.add(testRequestEventListener);
    }

    public void removeTestRequestEventListener(TestRequestEventListener testRequestEventListener) {
        this.currentTestRequestListeners.remove(testRequestEventListener);
    }

    protected void sendTestStartResponse(boolean z) {
        for (TestRequestEventListener testRequestEventListener : this.currentTestRequestListeners) {
            testRequestEventListener.onTestStartResponse(z);
        }
    }

    protected void sendTestNotificationResponse(TestNotification testNotification) {
        for (TestRequestEventListener testRequestEventListener : this.currentTestRequestListeners) {
            testRequestEventListener.onTestNotificationResponse(testNotification);
        }
    }

    protected void addBleRemoteTestEventListener(BleRemoteTestEventListener bleRemoteTestEventListener) {
        boolean z = false;
        for (BleRemoteTestEventListener bleRemoteTestEventListener2 : this.currentBleRemoteTestListeners) {
            if (bleRemoteTestEventListener2 == bleRemoteTestEventListener) {
                z = true;
            }
        }
        if (z) {
            return;
        }
        this.currentBleRemoteTestListeners.add(bleRemoteTestEventListener);
    }

    protected void removeBleRemoteTestEventListener(BleRemoteTestEventListener bleRemoteTestEventListener) {
        this.currentBleRemoteTestListeners.remove(bleRemoteTestEventListener);
    }

    protected void sendBleRemoteTestStartResponse(boolean z) {
        for (BleRemoteTestEventListener bleRemoteTestEventListener : this.currentBleRemoteTestListeners) {
            bleRemoteTestEventListener.onBleRemoteTestStartResponse(z);
        }
    }

    public boolean sendSetPauseAfterIdleResponse(boolean z, byte b) {
        if (this.managerEnabled && isConnected()) {
            sendRequest(Message.getSetPauseAfterIdleState(z, b));
            return true;
        }
        return false;
    }

    protected void sendBleRemoteTestResultsResponse(boolean z, BleRemoteTestResults bleRemoteTestResults) {
        for (BleRemoteTestEventListener bleRemoteTestEventListener : this.currentBleRemoteTestListeners) {
            bleRemoteTestEventListener.onBleRemoteTestResultsResponse(z, bleRemoteTestResults);
        }
    }

    public void addGameModeEventListener(GameModeEventListener gameModeEventListener) {
        boolean z = false;
        for (GameModeEventListener gameModeEventListener2 : this.currentGameModeListeners) {
            if (gameModeEventListener2 == gameModeEventListener) {
                z = true;
            }
        }
        if (z) {
            return;
        }
        this.currentGameModeListeners.add(gameModeEventListener);
    }

    public void removeGameModeEventListener(GameModeEventListener gameModeEventListener) {
        this.currentGameModeListeners.remove(gameModeEventListener);
    }

    protected void sendGameModeSetGameMode(boolean z) {
        for (GameModeEventListener gameModeEventListener : this.currentGameModeListeners) {
            gameModeEventListener.onGameModeSetGameModeResponse(z);
        }
    }

    protected void sendGameModeSetGameModeDisplay(boolean z) {
        for (GameModeEventListener gameModeEventListener : this.currentGameModeListeners) {
            gameModeEventListener.onGameModeSetGameModeDisplayResponse(z);
        }
    }

    public boolean isConnected() {
        return BleConnectionManager.shared.isDeviceConnected();
    }

    public void handleSubscribed(boolean z) {
        if (z) {
            sendGetDeviceComponentInfoRequest();
            return;
        }
        BleConnectionManager.shared.pendingDisconnectConnectionStatusCode = ConnectionStatusCode.error;
        BleConnectionManager.shared.disconnect();
    }

    public void handlePostConnect() {
        if (this.currentConnectionStatus == DeviceConnectionStatus.connected) {
            return;
        }
        DeviceConnectionEvent deviceConnectionEvent = new DeviceConnectionEvent(DeviceConnectionStatus.connected, ConnectionStatusCode.noError, BleConnectionManager.shared.connectedBlePeripheral);
        if (!isExpectingConnect()) {
            BleConnectionManager.shared.currentDeviceConnectionStatus = DeviceConnectionStatus.connected;
            BleConnectionManager.shared.sendOnDeviceConnectionChangedEvent(deviceConnectionEvent);
        }
        sendLogUserStatsEnable();
        this.currentConnectionStatus = DeviceConnectionStatus.connected;
        if (this.currentAuthenticationStatus) {
            return;
        }
        this.messageQueue.post(new Runnable() { // from class: com.treadly.client.lib.sdk.Managers.DeviceManager.1
            @Override // java.lang.Runnable
            public void run() {
                CloudNetworkManager.shared.secureAuthenticateDevice();
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void versionVerification(ComponentInfo componentInfo) {
        if (componentInfo == null || componentInfo.getType() != ComponentType.bleBoard || this.currentAuthenticationStatus) {
            return;
        }
        verifyCustomboardVersion(componentInfo);
    }

    private void verifyCustomboardVersion(ComponentInfo componentInfo) {
        CloudNetworkManager.shared.verifyRegistrationState(componentInfo, new CloudNetworkManager.OnSuccessRegistrationState() { // from class: com.treadly.client.lib.sdk.Managers.DeviceManager.2
            @Override // com.treadly.client.lib.sdk.Managers.CloudNetworkManager.OnSuccessRegistrationState
            public void onSuccess(final boolean z, RegistrationState registrationState) {
                DeviceManager.this.messageQueue.post(new Runnable() { // from class: com.treadly.client.lib.sdk.Managers.DeviceManager.2.1
                    @Override // java.lang.Runnable
                    public void run() {
                        if (z) {
                            DeviceManager.this.handlePostConnect();
                        } else {
                            BleConnectionManager.shared.disconnect();
                        }
                    }
                });
            }
        });
    }

    private void processPendingBle() {
        final DeviceStatus deviceStatus;
        if ((this.pendingBleEnable || this.isPendingRegistrationUpdate) && (deviceStatus = this.currentDeviceStatusCache) != null && deviceStatus.isValid()) {
            if (this.isPendingBleEnable && deviceStatus.isBleEnabled() != this.pendingBleEnable) {
                this.isPendingBleEnable = false;
                sendRequest(Message.getBleEnableRequest(this.pendingBleEnable));
            }
            if (this.isPendingRegistrationUpdate) {
                this.isPendingRegistrationUpdate = false;
                final ComponentInfo bleComponent = getBleComponent();
                if (bleComponent != null) {
                    CloudNetworkManager.shared.updateRegistrationState(bleComponent, deviceStatus.isBleEnabled() ? RegistrationState.active : RegistrationState.inactive, new CloudNetworkManager.OnSuccessListener() { // from class: com.treadly.client.lib.sdk.Managers.DeviceManager.3
                        @Override // com.treadly.client.lib.sdk.Managers.CloudNetworkManager.OnSuccessListener
                        public void onSuccess(boolean z) {
                            if (!z || deviceStatus.isBleEnabled()) {
                                return;
                            }
                            ActivationManager.shared.activationRequired(bleComponent);
                        }
                    });
                }
            }
        }
    }

    private void processActivationCodeSubmitAcknowledge() {
        DeviceStatus deviceStatus;
        ComponentInfo bleComponent;
        String str = this.pendingActivationCodeSubmitAcknowledge;
        if (str == null || (deviceStatus = this.currentDeviceStatusCache) == null || !deviceStatus.isValid() || (bleComponent = getBleComponent()) == null || !ActivationManager.shared.submitActivationCodeAcknowledgement(str, bleComponent)) {
            return;
        }
        this.pendingActivationCodeSubmitAcknowledge = null;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public ComponentInfo getBleComponent() {
        ComponentInfo[] components;
        if (currentDeviceInfo() == null || (components = currentDeviceInfo().getComponents()) == null) {
            return null;
        }
        for (ComponentInfo componentInfo : components) {
            if (componentInfo.getType() != null && componentInfo.getType() == ComponentType.bleBoard) {
                return componentInfo;
            }
        }
        return null;
    }

    private boolean isExpectingRestart() {
        return OtaUpdateManager.shared.isRestartExpected();
    }

    private boolean isExpectingConnect() {
        return ActiveDeviceManager.sharedInstance.handleExpectedActiveConnect() || OtaUpdateManager.shared.handleExpectedConnect();
    }

    public boolean isExpectingDisconnect() {
        return ActiveDeviceManager.sharedInstance.handleExpectedInactiveDisconnect() || OtaUpdateManager.shared.handleExpectedDisconnect();
    }

    public boolean sendFactoryResetRequest() {
        if (this.managerEnabled && isConnected()) {
            sendRequest(Message.getFactoryResetRequest());
            return true;
        }
        return false;
    }

    public boolean getDeviceStatus() {
        if (this.managerEnabled && isConnected() && this.currentDeviceStatusCache != null && this.currentDeviceStatusCache.isValid()) {
            sendOnRequestStatusEvent(true, this.currentDeviceStatusCache);
            return true;
        }
        return false;
    }

    public boolean getDeviceComponentInfo() {
        if (this.managerEnabled && isConnected() && currentDeviceInfo() != null) {
            sendRequestGetDeviceComponentEvent(true, currentDeviceInfo().getComponents());
            return true;
        }
        return false;
    }

    public ComponentInfo getCustomboardInfo() {
        ComponentInfo[] components;
        DeviceInfo currentDeviceInfo = currentDeviceInfo();
        ComponentInfo componentInfo = null;
        if (currentDeviceInfo == null || (components = currentDeviceInfo.getComponents()) == null) {
            return null;
        }
        for (ComponentInfo componentInfo2 : components) {
            if (componentInfo2.getType() == ComponentType.bleBoard) {
                componentInfo = componentInfo2;
            }
        }
        return componentInfo;
    }

    public ComponentInfo getComponentInfo(ComponentType componentType) {
        ComponentInfo[] components;
        DeviceInfo currentDeviceInfo = currentDeviceInfo();
        ComponentInfo componentInfo = null;
        if (currentDeviceInfo == null || (components = currentDeviceInfo.getComponents()) == null) {
            return null;
        }
        for (ComponentInfo componentInfo2 : components) {
            if (componentInfo2.getType() == componentType) {
                componentInfo = componentInfo2;
            }
        }
        return componentInfo;
    }

    public void sendGetDeviceComponentInfoRequest() {
        if (this.managerEnabled && isConnected()) {
            sendRequest(Message.getDeviceInfoRequest());
        }
    }

    public void sendAuthenticationRequest() {
        String name;
        byte[] authenticateSecret;
        if (currentDeviceInfo() == null || (name = currentDeviceInfo().getName()) == null || (authenticateSecret = SharedPreferences.shared.getAuthenticateSecret(name)) == null) {
            return;
        }
        sendRequest(Message.getAuthenticationRequest(authenticateSecret));
    }

    public boolean sendPowerRequest() {
        if (this.managerEnabled && isConnected()) {
            sendRequest(Message.getPowerRequest());
            return true;
        }
        return false;
    }

    public boolean sendSpeedUpRequest() {
        if (this.managerEnabled && isConnected()) {
            sendRequest(Message.getSpeedUpRequest());
            return true;
        }
        return false;
    }

    public boolean sendSpeedDownRequest() {
        if (this.managerEnabled && isConnected()) {
            sendRequest(Message.getSpeedDownRequest());
            return true;
        }
        return false;
    }

    public boolean sendSetSpeedRequest(float f) {
        if (this.managerEnabled && isConnected()) {
            sendRequest(Message.getSetSpeedRequest(f));
            return true;
        }
        return false;
    }

    public boolean sendSetSpeedUnitsRequest(DistanceUnits distanceUnits) {
        if (this.managerEnabled && isConnected()) {
            sendRequest(Message.getSetSpeedUnitRequest(distanceUnits));
            return true;
        }
        return false;
    }

    public boolean sendSetRemoteStatusRequest(RemoteStatus remoteStatus) {
        if (this.managerEnabled && isConnected()) {
            sendRequest(Message.getSetRemoteStatusRequest(remoteStatus));
            return true;
        }
        return false;
    }

    private void sendSubscriberRequest() {
        sendRequest(Message.getSubscribeRequest());
    }

    private void sendMacAddressVerify() {
        if (currentDeviceInfo().getName() == null) {
            return;
        }
        System.out.println("LGK :: sendMacAddressVerify");
        String macAddress = SharedPreferences.shared.getMacAddress(currentDeviceInfo().getName());
        if (isConnected()) {
            sendRequest(Message.getMacAddressVerifyRequest(macAddress));
        }
    }

    private void sendAuthenticateVerifyRequest(byte[] bArr) {
        sendRequest(Message.getAuthenticateVerifyRequest(bArr));
    }

    public boolean sendAccelerationZoneStart(int i) {
        if (this.managerEnabled && isConnected()) {
            sendRequest(Message.getSetAccelerationZoneStart(i));
            return true;
        }
        return false;
    }

    public boolean sendDecelerationZoneEnd(int i) {
        if (this.managerEnabled && isConnected()) {
            sendRequest(Message.getSetDecelerationZoneEnd(i));
            return true;
        }
        return false;
    }

    public boolean setEmergencyHandrailStopEnabled(boolean z) {
        if (this.managerEnabled && isConnected()) {
            sendRequest(Message.getSetEmergencyHandrailEnabledState(z));
            return true;
        }
        return false;
    }

    public boolean handleEmergencyStop() {
        if (this.managerEnabled && isConnected()) {
            sendRequest(Message.getEmergencyStopRequest());
            return true;
        }
        return false;
    }

    public boolean sendSetMaintenanceThreshold(int i) {
        ComponentInfo bleComponent;
        if (this.managerEnabled && isConnected() && (bleComponent = getBleComponent()) != null) {
            sendRequest(Message.getSetMaintenanceThresholdRequest(i, bleComponent.getVersionInfo().getVersionInfo()));
            return true;
        }
        return false;
    }

    public boolean sendResetMaintenance() {
        return sendResetMaintenance(false);
    }

    public boolean sendResetMaintenance(boolean z) {
        if (this.managerEnabled && isConnected()) {
            sendRequest(Message.getResetMaintenanceRequest(z));
            return true;
        }
        return false;
    }

    public boolean sendBleEnableRequest(boolean z) {
        if (this.managerEnabled && isConnected() && TreadlyClientLib.shared.isAdminEnabled()) {
            sendRequest(Message.getBleEnableRequest(z));
            return true;
        }
        return false;
    }

    public boolean sendAuthenticationStateChange(AuthenticationState authenticationState) {
        final ComponentInfo bleComponent;
        if (this.managerEnabled && isConnected() && TreadlyClientLib.shared.isAdminEnabled() && (bleComponent = getBleComponent()) != null) {
            if (!bleComponent.getVersionInfo().isGreaterThan(AuthenticateHelper.PRE_AUTHENTICATION_VERSION) && authenticationState == AuthenticationState.disabled) {
                authenticationState = AuthenticationState.inactive;
            }
            final AuthenticationState authenticationState2 = authenticationState;
            CloudNetworkManager.shared.sendAuthenticationUpdateRequest(bleComponent.getId(), authenticationState2, bleComponent.getSerialNumber(), new CloudNetworkManager.OnSuccessListener() { // from class: com.treadly.client.lib.sdk.Managers.DeviceManager.4
                @Override // com.treadly.client.lib.sdk.Managers.CloudNetworkManager.OnSuccessListener
                public void onSuccess(boolean z) {
                    DeviceManager.this.sendRequestSetAuthenticationState(z, z ? authenticationState2 : AuthenticationState.unknown);
                    if (z) {
                        if (bleComponent.getVersionInfo().isGreaterThan(AuthenticateHelper.PRE_AUTHENTICATION_VERSION)) {
                            CloudNetworkManager.shared.secureAuthenticateDevice();
                        } else {
                            DeviceManager.this.sendRequest(Message.getBleEnableRequest(authenticationState2 == AuthenticationState.active));
                        }
                    }
                }
            });
            return true;
        }
        return false;
    }

    public void authenticateDevice() {
        ComponentInfo bleComponent = getBleComponent();
        if (bleComponent != null && bleComponent.getVersionInfo().isGreaterThan(AuthenticateHelper.PRE_AUTHENTICATION_VERSION)) {
            CloudNetworkManager.shared.secureAuthenticateDevice();
        }
    }

    public boolean fetchAuthenticationState() {
        if (this.managerEnabled && isConnected() && getBleComponent() != null) {
            ComponentInfo bleComponent = getBleComponent();
            CloudNetworkManager.shared.sendGetAuthenticationRequest(bleComponent.getId(), bleComponent.getSerialNumber(), new CloudNetworkManager.OnSuccessAuthenticationState() { // from class: com.treadly.client.lib.sdk.Managers.DeviceManager.5
                @Override // com.treadly.client.lib.sdk.Managers.CloudNetworkManager.OnSuccessAuthenticationState
                public void onSuccess(boolean z, AuthenticationState authenticationState) {
                    DeviceManager.this.sendRequestGetAuthenticationState(z, authenticationState);
                }
            });
            return true;
        }
        return false;
    }

    public void sendTestStartRequest() {
        if (this.managerEnabled && isConnected()) {
            sendRequest(Message.getTestStartRequest());
        }
    }

    public boolean sendSetIrModeRequest(IrMode irMode) {
        if (this.managerEnabled && isConnected() && TreadlyClientLib.shared.isAdminEnabled()) {
            sendRequest(Message.getSetIrModeRequest(irMode));
            return true;
        }
        return false;
    }

    public boolean sendSetTotalStatusRequest(TotalStatus totalStatus) {
        if (this.managerEnabled && isConnected()) {
            sendRequest(Message.getSetTotalStatusRequest(totalStatus));
            return true;
        }
        return false;
    }

    public boolean sendResetStopRequest() {
        if (this.managerEnabled && isConnected()) {
            sendRequest(Message.getResetStopRequest());
            return true;
        }
        return false;
    }

    public boolean sendPauseRequest() {
        if (this.managerEnabled && isConnected()) {
            sendRequest(Message.getPauseRequest());
            return true;
        }
        return false;
    }

    public boolean sendGetDeviceDebugLogEvents() {
        if (this.managerEnabled && isConnected()) {
            sendRequest(Message.getBaseRequest(Message.MESSAGE_ID_DEVICE_DEBUG_LOG));
            return true;
        }
        return false;
    }

    public boolean sendGetDeviceIrDebugLogEvents() {
        if (this.managerEnabled && isConnected()) {
            sendRequest(Message.getBaseRequest(Message.MESSAGE_ID_DEVICE_IR_DEBUG_LOG));
            return true;
        }
        return false;
    }

    public boolean sendStartWifiUrlTest(WifiUrlTestInfo wifiUrlTestInfo) {
        byte[][] packetize;
        if (!this.managerEnabled || !isConnected() || wifiUrlTestInfo == null || wifiUrlTestInfo.getSsid() == null || wifiUrlTestInfo.getUrl() == null || wifiUrlTestInfo.getSsid().isEmpty() || wifiUrlTestInfo.getUrl().isEmpty()) {
            return false;
        }
        for (final byte[] bArr : Packetizer.shared.packetize(Message.getWifiUrlTestPacketizeRequest(wifiUrlTestInfo), true)) {
            this.messageQueue.post(new Runnable() { // from class: com.treadly.client.lib.sdk.Managers.DeviceManager.6
                @Override // java.lang.Runnable
                public void run() {
                    DeviceManager.shared.sendRequest(bArr);
                }
            });
        }
        return true;
    }

    public boolean sendLogUserStatsEnable() {
        if (this.managerEnabled && isConnected()) {
            sendRequest(Message.getLogUserStatsEnableRequest(DeviceUserStatsLogManager.sharedInstance.isEnabled()));
            return true;
        }
        return false;
    }

    public boolean sendProfileId() {
        if (this.managerEnabled && isConnected() && DeviceUserStatsLogManager.sharedInstance.isEnabled()) {
            sendRequest(Message.getProfileIdRequest(DeviceUserStatsLogManager.sharedInstance.getUserIdArray()));
            return true;
        }
        return false;
    }

    public boolean sendTimestamp() {
        if (this.managerEnabled && isConnected()) {
            sendRequest(Message.getTimestampRequest());
            return true;
        }
        return false;
    }

    public boolean sendLogUserStatsData(int i) {
        if (this.managerEnabled && isConnected() && DeviceUserStatsLogManager.sharedInstance.isEnabled()) {
            sendRequest(Message.getLogUserStatsDataRequest(i));
            return true;
        }
        return false;
    }

    public boolean sendLogUserStatsDataV2(int i) {
        if (this.managerEnabled && isConnected() && DeviceUserStatsLogManager.sharedInstance.isEnabled()) {
            sendRequest(Message.getLogUserStatsDataRequestV2(i));
            return true;
        }
        return false;
    }

    public boolean sendLogUserStatsDelete(int i) {
        if (this.managerEnabled && isConnected() && DeviceUserStatsLogManager.sharedInstance.isEnabled()) {
            sendRequest(Message.getLogUserStatsDeleteRequest(i));
            return false;
        }
        return false;
    }

    public boolean sendLogUserStatsDeleteV2(int i) {
        if (this.managerEnabled && isConnected() && DeviceUserStatsLogManager.sharedInstance.isEnabled()) {
            sendRequest(Message.getLogUserStatsDeleteRequestV2(i));
            return false;
        }
        return false;
    }

    public boolean sendClaimActiveLogUserStats() {
        if (this.managerEnabled && isConnected() && DeviceUserStatsLogManager.sharedInstance.isEnabled()) {
            sendRequest(Message.getClaimActiveLogUserStatsRequest());
            return true;
        }
        return false;
    }

    public boolean sendGetUnclaimedLogUserStatsInfo() {
        if (this.managerEnabled && isConnected() && DeviceUserStatsLogManager.sharedInstance.isEnabled()) {
            sendRequest(Message.getUnclaimedActivityInfoRequest());
            return true;
        }
        return false;
    }

    public boolean sendClaimUnclaimedLogUserStatsInfo(Date date, int i) {
        if (this.managerEnabled && isConnected() && DeviceUserStatsLogManager.sharedInstance.isEnabled()) {
            sendRequest(Message.claimUnclaimedLogUserStatsInfo(date, i));
            return true;
        }
        return false;
    }

    public boolean sendGetBtAudioPassword() {
        if (this.managerEnabled && isConnected()) {
            sendRequest(Message.getBtAudioPasswordRequest());
            return true;
        }
        return false;
    }

    public boolean sendSetBtAudioPassword(byte[] bArr) {
        if (this.managerEnabled && isConnected()) {
            sendRequest(Message.getSetBtAudioPasswordRequest(bArr));
            return true;
        }
        return false;
    }

    public boolean sendVerifyLedCode(String str) {
        if (this.managerEnabled && isConnected()) {
            sendRequest(Message.getVerifyLedCodeRequest(str));
            return true;
        }
        return false;
    }

    public boolean sendSetPairingModeTriggerType(PairingModeTriggerType pairingModeTriggerType) {
        if (this.managerEnabled && isConnected()) {
            sendRequest(Message.getSetPairingModeTriggerType(pairingModeTriggerType));
            return true;
        }
        return false;
    }

    public boolean sendDeletePairedPhones(boolean z) {
        if (this.managerEnabled && isConnected()) {
            sendRequest(Message.getSetDeletePairedPhones(z));
            return true;
        }
        return false;
    }

    public boolean sendSetDeviceIrDebugLogMode(boolean z) {
        if (this.managerEnabled && isConnected()) {
            sendRequest(Message.getSetDeviceIrDebugModeEnabledRequest(z));
            return true;
        }
        return false;
    }

    public boolean sendSetEnableUserInteraction(boolean z) {
        if (this.managerEnabled && isConnected()) {
            sendRequest(Message.getSetEnableUserInteraction(z));
            return true;
        }
        return false;
    }

    public boolean sendSetGameMode(boolean z, GameModeType gameModeType) {
        if (this.managerEnabled && isConnected()) {
            sendRequest(Message.getSetGameModeRequest(z, gameModeType));
            return true;
        }
        return false;
    }

    public boolean sendSetGameModeDisplay(GameModeSegmentDisplay gameModeSegmentDisplay) {
        if (!this.managerEnabled || !isConnected() || gameModeSegmentDisplay == null || gameModeSegmentDisplay.segmentDisplayValues == null) {
            return false;
        }
        sendRequest(Message.getSetGameModeDisplayRequest(gameModeSegmentDisplay.enable, gameModeSegmentDisplay.segmentDisplayValues));
        return true;
    }

    public boolean sendGetUserInteractionStatus() {
        if (this.managerEnabled && isConnected()) {
            sendRequest(Message.getUserInteractionStatusRequest());
            return true;
        }
        return false;
    }

    public boolean sendOtaDoneConfirmation() {
        return this.managerEnabled && isConnected();
    }

    public boolean sendBleRemoteTestStart() {
        if (this.managerEnabled && isConnected()) {
            sendRequest(Message.getStartBleRemoteTestRequest());
            return true;
        }
        return false;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void sendRequest(final byte[] bArr) {
        if (bArr == null) {
            return;
        }
        this.messageQueue.post(new Runnable() { // from class: com.treadly.client.lib.sdk.Managers.DeviceManager.7
            @Override // java.lang.Runnable
            public void run() {
                System.out.println("LGK :: SEND REQUEST");
                BleConnectionManager.shared.sendBytes(bArr);
            }
        });
    }

    public void handleSerialIsReady() {
        sendAuthenticationRequest();
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void handleSerialDidDisconnect() {
        reset();
    }

    public void handleProcessMessage(final ResponseMessage responseMessage) {
        if (responseMessage == null) {
            return;
        }
        this.messageQueue.post(new Runnable() { // from class: com.treadly.client.lib.sdk.Managers.DeviceManager.8
            @Override // java.lang.Runnable
            public void run() {
                DeviceManager.this.processMessage(responseMessage);
            }
        });
    }

    /* JADX WARN: Can't fix incorrect switch cases order, some code will duplicate */
    public void processMessage(ResponseMessage responseMessage) {
        if (responseMessage == null) {
            return;
        }
        byte b = responseMessage.cmd;
        ResponseStatus fromValue = ResponseStatus.fromValue(responseMessage.getStatus());
        System.out.println("LGK :: ID: " + ((int) b) + " Response Status: " + responseMessage.getStatus());
        if (fromValue == null) {
            fromValue = ResponseStatus.UNKNOWN;
        }
        boolean z = fromValue == ResponseStatus.NO_ERROR;
        byte b2 = responseMessage.msgId;
        switch (b) {
            case 1:
                sendRequestPowerEvent(z);
                return;
            case 2:
                break;
            case 3:
                sendRequestSpeedDownEvent(z);
                return;
            case 4:
                if (!z || getBleComponent() == null) {
                    return;
                }
                this.currentDeviceStatusCache = Message.parseStatusResponse(responseMessage.payload, b2, getBleComponent().getVersionInfo().getVersionInfo());
                return;
            case 5:
            case 6:
            case 7:
            case 8:
            case 9:
            case 14:
            case 15:
            case 18:
            case 27:
            case 28:
            case 29:
            case 31:
            case 33:
            case 34:
            case 35:
            case 36:
            case 41:
            case 42:
            case 45:
            case 46:
            case 50:
            case 52:
            case 59:
            case 60:
            case 65:
            case 74:
            case 75:
            case 77:
            case 83:
            case 88:
            case 91:
            default:
                System.out.println("Message ID unsupported: " + ((int) responseMessage.msgId));
                return;
            case 10:
                sendRequestSetSpeedResponse(z);
                return;
            case 11:
                sendRequestFactoryResetEvent(z);
                return;
            case 12:
                if (!z || this.currentDeviceStatusCache == null || this.currentDeviceStatusCache.id != b2 || getBleComponent() == null || getBleComponent() == null) {
                    return;
                }
                this.currentDeviceStatusCache = Message.parseDeviceStatusExtResponse(responseMessage.payload, this.currentDeviceStatusCache, getBleComponent(), getBleComponent().getVersionInfo().getVersionInfo());
                return;
            case 13:
                if (z && this.currentDeviceStatusCache != null && this.currentDeviceStatusCache.id == b2) {
                    this.currentDeviceStatusCache = Message.parseDeviceDiagnosticResponse(responseMessage.payload, this.currentDeviceStatusCache);
                    boolean z2 = this.currentDeviceStatusCache != null && this.currentDeviceStatusCache.isValid();
                    if (z2) {
                        if (!z || !z2) {
                            r3 = false;
                        }
                        sendOnRequestStatusEvent(r3, this.currentDeviceStatusCache);
                        return;
                    }
                    return;
                }
                return;
            case 16:
                final ComponentInfo parseComponentInfoResponse = Message.parseComponentInfoResponse(responseMessage.payload);
                if (parseComponentInfoResponse == null || currentDeviceInfo() == null) {
                    return;
                }
                if (!currentDeviceInfo().containsComponent(parseComponentInfoResponse.getType(), parseComponentInfoResponse.getId())) {
                    currentDeviceInfo().addComponent(parseComponentInfoResponse);
                } else {
                    currentDeviceInfo().removeComponent(parseComponentInfoResponse);
                    currentDeviceInfo().addComponent(parseComponentInfoResponse);
                }
                this.messageQueue.post(new Runnable() { // from class: com.treadly.client.lib.sdk.Managers.DeviceManager.10
                    @Override // java.lang.Runnable
                    public void run() {
                        DeviceManager.this.versionVerification(parseComponentInfoResponse);
                    }
                });
                return;
            case 17:
                handleSubscribed(z);
                return;
            case 19:
                if (fromValue == ResponseStatus.CUSTOM_BOARD_NOT_PAIRED) {
                    sendMacAddressVerify();
                    return;
                }
                byte[] generateAuthenticateResponse = AuthenticateHelper.generateAuthenticateResponse(responseMessage.payload);
                if (generateAuthenticateResponse != null) {
                    sendAuthenticateVerifyRequest(generateAuthenticateResponse);
                    return;
                }
                System.out.println("Authenticate response error, disconnect");
                BleConnectionManager.shared.disconnect();
                return;
            case 20:
                if (fromValue == ResponseStatus.REQUIRES_AUTHENTICATION) {
                    CloudNetworkManager.shared.secureAuthenticateDevice();
                    return;
                } else if (z) {
                    sendSubscriberRequest();
                    return;
                } else {
                    return;
                }
            case 21:
                sendRequestSetAccelerationStartEvent(z);
                return;
            case 22:
                sendRequestSetDecelerationEndEvent(z);
                return;
            case 23:
                sendRequestSetEmergencyHandrailStopEnableResponse(z);
                return;
            case 24:
                sendRequestEmergencyStopResponse(z);
                return;
            case 25:
                sendRequestResetMaintenanceResponse(z);
                return;
            case 26:
                sendRequestSetMaintenanceStepThresholdResponse(z);
                return;
            case 30:
                sendRequestBleEnableResponse(z);
                return;
            case 32:
                if (this.currentSecureAuthenticateInfo == null) {
                    this.currentSecureAuthenticateInfo = new DeviceSecureAuthenticateInfo();
                }
                this.currentSecureAuthenticateInfo.deviceIdBytes = Message.parseSecureAuthenticateBroadcastResponse(responseMessage.payload);
                final ComponentInfo bleComponent = getBleComponent();
                if (this.currentSecureAuthenticateInfo.deviceIdBytes == null || this.currentSecureAuthenticateInfo.deviceRandNum == null || bleComponent == null) {
                    return;
                }
                this.messageQueue.post(new Runnable() { // from class: com.treadly.client.lib.sdk.Managers.DeviceManager.9
                    @Override // java.lang.Runnable
                    public void run() {
                        CloudNetworkManager.shared.sendSecureAuthenticateVerifyRequest(DeviceManager.this.currentSecureAuthenticateInfo, bleComponent);
                    }
                });
                return;
            case 37:
                sendTestNotificationResponse(Message.parseTestNotification(responseMessage.payload));
                return;
            case 38:
                if (this.currentSecureAuthenticateInfo == null) {
                    this.currentSecureAuthenticateInfo = new DeviceSecureAuthenticateInfo();
                }
                this.currentSecureAuthenticateInfo.deviceRandNum = Message.parseSecureAuthenticateResponse(responseMessage.payload);
                this.currentSecureAuthenticateInfo.deviceIdBytes = null;
                return;
            case 39:
                this.currentAuthenticationStatus = true;
                sendSubscriberRequest();
                return;
            case 40:
                sendTestStartResponse(z);
                return;
            case 43:
                if (currentDeviceInfo() == null || currentDeviceInfo().getName() == null || !z) {
                    return;
                }
                String name = currentDeviceInfo().getName();
                String bytesToHex = Message.bytesToHex(Arrays.copyOfRange(responseMessage.payload, 0, 6));
                if (bytesToHex != null) {
                    SharedPreferences.shared.saveMacAddress(name, bytesToHex);
                    return;
                }
                return;
            case 44:
                if (z) {
                    sendAuthenticationRequest();
                    return;
                }
                return;
            case 47:
                OtaUpdateManager.shared.handleWifiApResponse(responseMessage);
                return;
            case 48:
                OtaUpdateManager.shared.handleOtaUpdateResponse(responseMessage);
                return;
            case 49:
                OtaUpdateManager.shared.handleOtaInProgressResponse(responseMessage);
                return;
            case 51:
                byte[] bArr = this.currentSecureAuthenticateInfo.deviceRandNum;
                if (bArr == null) {
                    sendRequest(Message.getBroadcastDeviceStatusConfirmationRequest(false));
                    return;
                } else {
                    CloudNetworkManager.shared.sendBroadcastDeviceStatusRequest(responseMessage.payload, bArr);
                    return;
                }
            case 53:
                sendRequestSetTotalStatusResponse(z);
                return;
            case 54:
                sendRequestSetRemoteStatusResponse(z);
                return;
            case 55:
                sendRequestResetStopResponse(z);
                return;
            case 56:
                sendRequestWifiUrlTestResponse(z);
                return;
            case 57:
                ComponentInfo componentInfo = getComponentInfo(ComponentType.mainBoard);
                ComponentInfo customboardInfo = getCustomboardInfo();
                if (componentInfo == null || customboardInfo == null) {
                    return;
                }
                TreadlyLogManager.shared.handleDeviceDebugLogResponse(responseMessage, customboardInfo.getSerialNumber(), customboardInfo.getId(), customboardInfo, componentInfo);
                return;
            case 58:
                ComponentInfo componentInfo2 = getComponentInfo(ComponentType.mainBoard);
                ComponentInfo customboardInfo2 = getCustomboardInfo();
                if (componentInfo2 == null || customboardInfo2 == null) {
                    return;
                }
                TreadlyLogManager.shared.handleDeviceIrDebugLogResponse(responseMessage, customboardInfo2.getSerialNumber(), customboardInfo2.getId(), customboardInfo2, componentInfo2);
                return;
            case 61:
                sendProfileId();
                return;
            case 62:
                sendTimestamp();
                return;
            case 63:
                System.out.println("I am at message 63.");
                DeviceUserStatsLogManager.sharedInstance.handleDeviceUserStatsReadyResponse(responseMessage);
                return;
            case 64:
                DeviceUserStatsLogManager.sharedInstance.handleDeviceUserStatsDataResponse(responseMessage);
                return;
            case 66:
                sendRequestPauseEvent(z);
                return;
            case 67:
                if (z) {
                    sendRequestGetBtAudioPasswordResponse(true, Message.parseBtAudioPassword(responseMessage.payload));
                    return;
                } else {
                    sendRequestGetBtAudioPasswordResponse(false, null);
                    return;
                }
            case 68:
                sendRequestSetBtAudioPasswordResponse(z);
                return;
            case 69:
                sendRequestVerifyLedResponse(z);
                return;
            case 70:
                sendRequestSetPairingModeTriggerTypeResponse(z);
                return;
            case 71:
                sendRequestSetDeviceIrDebugLogModeResponse(z);
                return;
            case 72:
                if (z) {
                    UserInteractionHandrailStepEventInfo parseUserInteractionStepInfo = Message.parseUserInteractionStepInfo(responseMessage.payload);
                    if (parseUserInteractionStepInfo != null) {
                        UserInteractionSteps userInteractionSteps = parseUserInteractionStepInfo.userInteractionSteps;
                        sendUserInteractionStepsEvent(userInteractionSteps != null, userInteractionSteps);
                        return;
                    }
                    sendUserInteractionStepsEvent(false, null);
                    return;
                }
                sendUserInteractionStepsEvent(false, null);
                return;
            case 73:
                if (z) {
                    UserInteractionStatus parseUserInteractionStatus = Message.parseUserInteractionStatus(responseMessage.payload);
                    sendUserInteractionStatusEvent(parseUserInteractionStatus != null, parseUserInteractionStatus);
                    return;
                }
                sendUserInteractionStatusEvent(false, null);
                return;
            case 76:
                DeviceUserStatsLogManager.sharedInstance.handleDeviceUserStatsUnclaimedActiveLogResponse(responseMessage);
                return;
            case 78:
                sendGameModeSetGameMode(z);
                return;
            case 79:
                sendBleRemoteTestStartResponse(z);
                return;
            case 80:
                sendBleRemoteTestResultsResponse(z, Message.parseBleRemoteTestResults(responseMessage.payload));
                return;
            case 81:
                sendGameModeSetGameModeDisplay(z);
                return;
            case 82:
                if (z) {
                    UserInteractionHandrailEvent parseUserInteractionHandrailEvent = Message.parseUserInteractionHandrailEvent(responseMessage.payload);
                    sendUserInteractionHandrailEvent(parseUserInteractionHandrailEvent != null, parseUserInteractionHandrailEvent);
                    return;
                }
                sendUserInteractionHandrailEvent(false, null);
                return;
            case 84:
                if (!z || this.currentDeviceStatusCache == null || this.currentDeviceStatusCache.id != b2 || getBleComponent() == null || this.currentDeviceStatusCache == null) {
                    return;
                }
                this.currentDeviceStatusCache = Message.parseDeviceStatusExt2Response(responseMessage.payload, this.currentDeviceStatusCache, getBleComponent().getVersionInfo().getVersionInfo());
                boolean isValid = this.currentDeviceStatusCache.isValid();
                if (isValid) {
                    if (!z || !isValid) {
                        r3 = false;
                    }
                    sendOnRequestStatusEvent(r3, this.currentDeviceStatusCache);
                    return;
                }
                return;
            case 85:
                sendRequestSetDeletePairList(z);
                return;
            case 86:
                System.out.println("I am at message 86.");
                DeviceUserStatsLogManager.sharedInstance.handleGetUnclaimedUserStatsLogInfoResponse(responseMessage);
                return;
            case 87:
                DeviceUserStatsLogManager.sharedInstance.handleClaimUnclaimedUserStatsInfo(responseMessage);
                return;
            case 89:
                DeviceUserStatsLogManager.sharedInstance.handleDeviceUserStatsReadyV2Response(responseMessage);
                return;
            case 90:
                DeviceUserStatsLogManager.sharedInstance.handleDeviceUserStatsDataV2Response(responseMessage);
                return;
            case 92:
                if (((this.currentDeviceStatusCache != null) & z) && this.currentDeviceStatusCache.id == b2) {
                    if (getBleComponent() != null && this.currentDeviceStatusCache != null) {
                        getBleComponent().getVersionInfo().getVersionInfo();
                        this.currentDeviceStatusCache = Message.parseDeviceStatusIotResponse(responseMessage.payload, this.currentDeviceStatusCache);
                        boolean isValid2 = this.currentDeviceStatusCache.isValid();
                        if (isValid2) {
                            if (!z || !isValid2) {
                                r3 = false;
                            }
                            sendOnRequestStatusEvent(r3, this.currentDeviceStatusCache);
                            break;
                        }
                    } else {
                        return;
                    }
                }
                break;
        }
        sendRequestSpeedUpEvent(z);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void reset() {
        this.currentDeviceStatusCache = null;
        this.currentSecureAuthenticateInfo = null;
        this.currentConnectionStatus = DeviceConnectionStatus.notConnected;
        this.currentAuthenticationStatus = false;
        this.isPendingBleEnable = false;
        this.isPendingRegistrationUpdate = false;
        this.pendingActivationCodeSubmitAcknowledge = null;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public DeviceInfo currentDeviceInfo() {
        return BleConnectionManager.shared.connectedBlePeripheral;
    }

    private void printPayload(byte[] bArr) {
        System.out.println("********* Payload ************");
        for (byte b : bArr) {
            System.out.print((b & 255) + " ");
        }
        System.out.print("\n********************************\n");
    }

    static void runOnMain(Runnable runnable) {
        new Handler(Looper.getMainLooper()).post(runnable);
    }
}
