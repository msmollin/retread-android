package com.treadly.client.lib.sdk.Listeners;

import com.treadly.client.lib.sdk.Model.DeviceUserStatsLogInfo;
import com.treadly.client.lib.sdk.Model.DeviceUserStatsUnclaimedLogInfo;

/* loaded from: classes2.dex */
public class DeviceUserStatsLogEventAdapter implements DeviceUserStatsLogEventListener {
    @Override // com.treadly.client.lib.sdk.Listeners.DeviceUserStatsLogEventListener
    public void onDeviceUserStatsClaimUnclaimedUserStatsInfoResponse(boolean z) {
    }

    @Override // com.treadly.client.lib.sdk.Listeners.DeviceUserStatsLogEventListener
    public void onDeviceUserStatsLogData(DeviceUserStatsLogInfo deviceUserStatsLogInfo) {
    }

    @Override // com.treadly.client.lib.sdk.Listeners.DeviceUserStatsLogEventListener
    public void onDeviceUserStatsLogDataV2(byte[] bArr) {
    }

    @Override // com.treadly.client.lib.sdk.Listeners.DeviceUserStatsLogEventListener
    public void onDeviceUserStatsLogReady(int i) {
    }

    @Override // com.treadly.client.lib.sdk.Listeners.DeviceUserStatsLogEventListener
    public void onDeviceUserStatsLogReadyV2(int i) {
    }

    @Override // com.treadly.client.lib.sdk.Listeners.DeviceUserStatsLogEventListener
    public void onDeviceUserStatsUnclaimedActiveLog() {
    }

    @Override // com.treadly.client.lib.sdk.Listeners.DeviceUserStatsLogEventListener
    public void onDeviceUserStatsUnclaimedUserStatsInfo(DeviceUserStatsUnclaimedLogInfo[] deviceUserStatsUnclaimedLogInfoArr) {
    }
}
