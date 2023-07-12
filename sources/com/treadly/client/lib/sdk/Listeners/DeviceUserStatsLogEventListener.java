package com.treadly.client.lib.sdk.Listeners;

import com.treadly.client.lib.sdk.Model.DeviceUserStatsLogInfo;
import com.treadly.client.lib.sdk.Model.DeviceUserStatsUnclaimedLogInfo;

/* loaded from: classes2.dex */
public interface DeviceUserStatsLogEventListener {
    void onDeviceUserStatsClaimUnclaimedUserStatsInfoResponse(boolean z);

    void onDeviceUserStatsLogData(DeviceUserStatsLogInfo deviceUserStatsLogInfo);

    void onDeviceUserStatsLogDataV2(byte[] bArr);

    void onDeviceUserStatsLogReady(int i);

    void onDeviceUserStatsLogReadyV2(int i);

    void onDeviceUserStatsUnclaimedActiveLog();

    void onDeviceUserStatsUnclaimedUserStatsInfo(DeviceUserStatsUnclaimedLogInfo[] deviceUserStatsUnclaimedLogInfoArr);
}
