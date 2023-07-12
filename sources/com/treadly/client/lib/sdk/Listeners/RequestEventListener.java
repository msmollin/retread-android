package com.treadly.client.lib.sdk.Listeners;

import com.treadly.client.lib.sdk.Model.AuthenticationState;
import com.treadly.client.lib.sdk.Model.ComponentInfo;
import com.treadly.client.lib.sdk.Model.DeviceStatus;
import com.treadly.client.lib.sdk.Model.RegistrationInfo;

/* loaded from: classes2.dex */
public interface RequestEventListener {
    void onRequestBleEnableResponse(boolean z);

    void onRequestEmergencyHandrailStopEnabledResponse(boolean z);

    void onRequestEmergencyStopResponse(boolean z);

    void onRequestFactoryResetResponse(boolean z);

    void onRequestGetAuthenticationState(boolean z, AuthenticationState authenticationState);

    void onRequestGetBtAudioPAssword(boolean z, int[] iArr);

    void onRequestGetDeviceComponentsListResponse(boolean z, ComponentInfo[] componentInfoArr);

    void onRequestPauseResponse(boolean z);

    void onRequestPowerResponse(boolean z);

    void onRequestRegistrationRequiredEvent(RegistrationInfo registrationInfo);

    void onRequestResetMaintenancResponse(boolean z);

    void onRequestResetStopResponse(boolean z);

    void onRequestSetAccelerationZoneStartResponse(boolean z);

    void onRequestSetAuthenticationState(boolean z, AuthenticationState authenticationState);

    void onRequestSetBtAudioPassword(boolean z);

    void onRequestSetDecelerationZoneEndResponse(boolean z);

    void onRequestSetDeletePairList(boolean z);

    void onRequestSetDeviceIrDebugLogMode(boolean z);

    void onRequestSetMaintenanceStepThresholdResponse(boolean z);

    void onRequestSetPairingModeTriggerType(boolean z);

    void onRequestSetRemoteStatusResponse(boolean z);

    void onRequestSetSpeedResponse(boolean z);

    void onRequestSetTotalStatusResponse(boolean z);

    void onRequestSetUnitsResponse(boolean z);

    void onRequestSpeedDownResponse(boolean z);

    void onRequestSpeedUpResponse(boolean z);

    void onRequestStatusResponse(boolean z, DeviceStatus deviceStatus);

    void onRequestVerifyLedCode(boolean z);

    void onRequestWifiUrlTestResponse(boolean z);
}
