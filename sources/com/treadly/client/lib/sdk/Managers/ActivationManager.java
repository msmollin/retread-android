package com.treadly.client.lib.sdk.Managers;

import android.os.Handler;
import android.os.Looper;
import com.treadly.client.lib.sdk.Listeners.DeviceActivationEventListener;
import com.treadly.client.lib.sdk.Managers.CloudNetworkManager;
import com.treadly.client.lib.sdk.Model.ActivationInfo;
import com.treadly.client.lib.sdk.Model.AuthenticateReferenceCodeInfo;
import com.treadly.client.lib.sdk.Model.AuthenticationState;
import com.treadly.client.lib.sdk.Model.ComponentInfo;
import java.util.ArrayList;
import java.util.List;

/* loaded from: classes2.dex */
public class ActivationManager {
    public static final ActivationManager shared = new ActivationManager();
    private List<DeviceActivationEventListener> activationListeners = new ArrayList();
    protected ComponentInfo currentComponentInfo = null;

    private ActivationManager() {
    }

    public void addActivationEventListener(DeviceActivationEventListener deviceActivationEventListener) {
        boolean z = false;
        for (DeviceActivationEventListener deviceActivationEventListener2 : this.activationListeners) {
            if (deviceActivationEventListener2 == deviceActivationEventListener) {
                z = true;
            }
        }
        if (z) {
            return;
        }
        this.activationListeners.add(deviceActivationEventListener);
    }

    public void removeActivationEventListener(DeviceActivationEventListener deviceActivationEventListener) {
        this.activationListeners.remove(deviceActivationEventListener);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void sendDeviceActivationStartResponse(ActivationInfo activationInfo) {
        for (DeviceActivationEventListener deviceActivationEventListener : this.activationListeners) {
            deviceActivationEventListener.onDeviceActivationStartResponse(activationInfo);
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void sendDeviceActivationSubmitResponse(boolean z) {
        for (DeviceActivationEventListener deviceActivationEventListener : this.activationListeners) {
            deviceActivationEventListener.onDeviceActivationSubmitResponse(z);
        }
    }

    protected void sendDeviceAuthenticateReferenceCodeInfoResponse(boolean z, AuthenticateReferenceCodeInfo authenticateReferenceCodeInfo) {
        for (DeviceActivationEventListener deviceActivationEventListener : this.activationListeners) {
            deviceActivationEventListener.onDeviceAuthenticateReferenceCodeInfoResponse(z, authenticateReferenceCodeInfo);
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void sendDeviceAuthenticationStateResponse(AuthenticationState authenticationState) {
        for (DeviceActivationEventListener deviceActivationEventListener : this.activationListeners) {
            deviceActivationEventListener.onDeviceAuthenticationStateResponse(authenticationState);
        }
    }

    public void activationRequired(ComponentInfo componentInfo) {
        this.currentComponentInfo = componentInfo;
        startActivation();
    }

    public void startActivation() {
        CloudNetworkManager.shared.fetchActivatoinStartInfo();
    }

    public void submitActivationCode(String str, ComponentInfo componentInfo) {
        CloudNetworkManager.shared.submitActivationCode(str, componentInfo);
    }

    public boolean submitActivationCodeAcknowledgement(String str, ComponentInfo componentInfo) {
        return CloudNetworkManager.shared.submitActivationCodeAcknowledgement(str, componentInfo);
    }

    public void fetchAuthenticateReferenceCode(ComponentInfo componentInfo) {
        CloudNetworkManager.shared.fetchAuthenticateReferenceCode(componentInfo, new CloudNetworkManager.OnSuccessAuthenticateReferenceCodeResponse() { // from class: com.treadly.client.lib.sdk.Managers.ActivationManager.1
            @Override // com.treadly.client.lib.sdk.Managers.CloudNetworkManager.OnSuccessAuthenticateReferenceCodeResponse
            public void onSuccess(final boolean z, final AuthenticateReferenceCodeInfo authenticateReferenceCodeInfo) {
                ActivationManager.runOnMain(new Runnable() { // from class: com.treadly.client.lib.sdk.Managers.ActivationManager.1.1
                    @Override // java.lang.Runnable
                    public void run() {
                        ActivationManager.this.sendDeviceAuthenticateReferenceCodeInfoResponse(z, authenticateReferenceCodeInfo);
                    }
                });
            }
        });
    }

    public void reset() {
        this.currentComponentInfo = null;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static void runOnMain(Runnable runnable) {
        new Handler(Looper.getMainLooper()).post(runnable);
    }
}
