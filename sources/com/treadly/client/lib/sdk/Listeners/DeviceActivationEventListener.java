package com.treadly.client.lib.sdk.Listeners;

import com.treadly.client.lib.sdk.Model.ActivationInfo;
import com.treadly.client.lib.sdk.Model.AuthenticateReferenceCodeInfo;
import com.treadly.client.lib.sdk.Model.AuthenticationState;

/* loaded from: classes2.dex */
public interface DeviceActivationEventListener {
    void onDeviceActivationStartResponse(ActivationInfo activationInfo);

    void onDeviceActivationSubmitResponse(boolean z);

    void onDeviceAuthenticateReferenceCodeInfoResponse(boolean z, AuthenticateReferenceCodeInfo authenticateReferenceCodeInfo);

    void onDeviceAuthenticationStateResponse(AuthenticationState authenticationState);
}
