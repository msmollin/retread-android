package com.treadly.Treadly.Data.Managers;

import com.treadly.Treadly.MainActivity;
import com.treadly.Treadly.UI.Util.SharedPreferences;
import com.treadly.client.lib.sdk.Model.AuthenticationState;
import com.treadly.client.lib.sdk.TreadlyClientLib;

/* loaded from: classes2.dex */
public class TreadlyActivationManager {
    public static TreadlyActivationManager shared = new TreadlyActivationManager();
    public MainActivity activity = null;

    public void enableNotifications(boolean z) {
    }

    private TreadlyActivationManager() {
    }

    private String getPreviousPairedDeviceName() {
        return SharedPreferences.shared.getConnectedDeviceName();
    }

    private AuthenticationState getPreviousPairedDeviceActivation(String str) {
        return SharedPreferences.shared.getConnectedDeviceAuthenticationState(str);
    }

    public void setUsingInviteCode(boolean z) {
        SharedPreferences.shared.storeUsingInviteCode(z);
    }

    public boolean isUsingInviteCode() {
        return SharedPreferences.shared.getUsingInviteCode();
    }

    void handleDeviceAuthentification(String str, AuthenticationState authenticationState) {
        SharedPreferences.shared.storeConnectedDeviceAuthenticationState(str, authenticationState);
    }

    public boolean hasPreviouslyPairedDevice() {
        if (TreadlyClientLib.shared.isAdminEnabled()) {
            return true;
        }
        if (getPreviousPairedDeviceName() != null) {
            return !getPreviousPairedDeviceName().isEmpty();
        }
        return false;
    }

    public boolean hasActivatedDevice() {
        String previousPairedDeviceName = getPreviousPairedDeviceName();
        if (previousPairedDeviceName == null || previousPairedDeviceName.isEmpty() || getPreviousPairedDeviceActivation(previousPairedDeviceName) != AuthenticationState.active) {
            if (getPreviousPairedDeviceName() == null || getPreviousPairedDeviceActivation(getPreviousPairedDeviceName()) == null || getPreviousPairedDeviceActivation(getPreviousPairedDeviceName()) != AuthenticationState.active) {
                return TreadlyClientLib.shared.isAdminEnabled();
            }
            return true;
        }
        return true;
    }

    public void initializeTabs() {
        boolean hasActivatedDevice = hasActivatedDevice();
        enableNotifications(hasActivatedDevice);
        if (this.activity != null) {
            this.activity.enableActivationTabs(hasActivatedDevice);
        }
    }

    public void handleAuthenticationState(String str, AuthenticationState authenticationState) {
        SharedPreferences.shared.storeConnectedDeviceAuthenticationState(str, authenticationState);
        initializeTabs();
    }

    public void reset() {
        String previousPairedDeviceName = getPreviousPairedDeviceName();
        if (previousPairedDeviceName != null) {
            SharedPreferences.shared.storeConnectedDeviceAuthenticationState(previousPairedDeviceName, null);
        }
        SharedPreferences.shared.removeConnectedDeviceName();
    }
}
