package com.treadly.client.lib.sdk.Listeners;

import com.treadly.client.lib.sdk.Model.FirmwareVersion;
import com.treadly.client.lib.sdk.Model.OtaUpdateStatus;
import com.treadly.client.lib.sdk.Model.WifiApInfo;
import com.treadly.client.lib.sdk.Model.WifiStatus;

/* loaded from: classes2.dex */
public interface OtaUpdateRequestEventListener {
    void onOtaInProgressResponse(int i);

    void onOtaUpdateFirmwareVersionAvailable(boolean z, FirmwareVersion firmwareVersion, String[] strArr, boolean z2);

    void onOtaUpdateResponse(OtaUpdateStatus otaUpdateStatus);

    void onOtaUpdateSetMode(boolean z);

    void onOtaUpdateWiFiApResponse(WifiApInfo wifiApInfo);

    void onOtaUpdateWifiScanResponse(WifiStatus wifiStatus);
}
