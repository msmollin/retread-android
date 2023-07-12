package com.treadly.client.lib.sdk.Listeners;

import com.treadly.client.lib.sdk.Model.BleRemoteTestResults;

/* loaded from: classes2.dex */
public interface BleRemoteTestEventListener {
    void onBleRemoteTestResultsResponse(boolean z, BleRemoteTestResults bleRemoteTestResults);

    void onBleRemoteTestStartResponse(boolean z);
}
