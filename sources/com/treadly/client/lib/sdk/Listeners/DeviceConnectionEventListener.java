package com.treadly.client.lib.sdk.Listeners;

import com.treadly.client.lib.sdk.Model.DeviceConnectionEvent;
import com.treadly.client.lib.sdk.Model.DeviceInfo;

/* loaded from: classes2.dex */
public interface DeviceConnectionEventListener {
    void onDeviceConnectionChanged(DeviceConnectionEvent deviceConnectionEvent);

    void onDeviceConnectionDeviceDiscovered(DeviceInfo deviceInfo);
}
