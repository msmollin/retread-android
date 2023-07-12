package com.treadly.client.lib.sdk.Listeners;

import com.treadly.client.lib.sdk.Model.FirmwareVersion;
import com.treadly.client.lib.sdk.Model.OtaUpdateStatus;
import com.treadly.client.lib.sdk.Model.WifiApInfo;
import com.treadly.client.lib.sdk.Model.WifiStatus;

/* loaded from: classes2.dex */
public class OtaUpdateRequestEventAdapter implements OtaUpdateRequestEventListener {
    @Override // com.treadly.client.lib.sdk.Listeners.OtaUpdateRequestEventListener
    public void onOtaInProgressResponse(int i) {
    }

    @Override // com.treadly.client.lib.sdk.Listeners.OtaUpdateRequestEventListener
    public void onOtaUpdateFirmwareVersionAvailable(boolean z, FirmwareVersion firmwareVersion, String[] strArr, boolean z2) {
    }

    @Override // com.treadly.client.lib.sdk.Listeners.OtaUpdateRequestEventListener
    public void onOtaUpdateResponse(OtaUpdateStatus otaUpdateStatus) {
    }

    @Override // com.treadly.client.lib.sdk.Listeners.OtaUpdateRequestEventListener
    public void onOtaUpdateSetMode(boolean z) {
    }

    @Override // com.treadly.client.lib.sdk.Listeners.OtaUpdateRequestEventListener
    public void onOtaUpdateWiFiApResponse(WifiApInfo wifiApInfo) {
    }

    @Override // com.treadly.client.lib.sdk.Listeners.OtaUpdateRequestEventListener
    public void onOtaUpdateWifiScanResponse(WifiStatus wifiStatus) {
    }
}
