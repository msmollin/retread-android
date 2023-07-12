package com.treadly.client.lib.sdk.Listeners;

import com.treadly.client.lib.sdk.Model.ActivationInfo;
import com.treadly.client.lib.sdk.Model.AuthenticateReferenceCodeInfo;
import com.treadly.client.lib.sdk.Model.AuthenticationState;

/* loaded from: classes2.dex */
public class DeviceActivationEventAdapter implements DeviceActivationEventListener {
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
}
