package com.treadly.client.lib.sdk.Listeners;

import com.treadly.client.lib.sdk.Model.AuthenticationState;
import com.treadly.client.lib.sdk.Model.ComponentInfo;
import com.treadly.client.lib.sdk.Model.DeviceStatus;
import com.treadly.client.lib.sdk.Model.RegistrationInfo;

/* loaded from: classes2.dex */
public class RequestEventAdapter implements RequestEventListener {
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
    public void onRequestGetAuthenticationState(boolean z, AuthenticationState authenticationState) {
    }

    @Override // com.treadly.client.lib.sdk.Listeners.RequestEventListener
    public void onRequestGetBtAudioPAssword(boolean z, int[] iArr) {
    }

    @Override // com.treadly.client.lib.sdk.Listeners.RequestEventListener
    public void onRequestGetDeviceComponentsListResponse(boolean z, ComponentInfo[] componentInfoArr) {
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
    public void onRequestSetAuthenticationState(boolean z, AuthenticationState authenticationState) {
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
    public void onRequestSetSpeedResponse(boolean z) {
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
    public void onRequestStatusResponse(boolean z, DeviceStatus deviceStatus) {
    }

    @Override // com.treadly.client.lib.sdk.Listeners.RequestEventListener
    public void onRequestVerifyLedCode(boolean z) {
    }

    @Override // com.treadly.client.lib.sdk.Listeners.RequestEventListener
    public void onRequestWifiUrlTestResponse(boolean z) {
    }
}
