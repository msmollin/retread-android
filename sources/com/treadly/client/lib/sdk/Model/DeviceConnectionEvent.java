package com.treadly.client.lib.sdk.Model;

/* loaded from: classes2.dex */
public class DeviceConnectionEvent {
    private DeviceInfo deviceInfo;
    private DeviceConnectionStatus status;
    private ConnectionStatusCode statusCode;

    public DeviceConnectionEvent(DeviceConnectionStatus deviceConnectionStatus, ConnectionStatusCode connectionStatusCode, DeviceInfo deviceInfo) {
        this.status = deviceConnectionStatus;
        this.statusCode = connectionStatusCode;
        this.deviceInfo = deviceInfo;
    }

    public DeviceConnectionStatus getStatus() {
        return this.status;
    }

    public DeviceInfo getDeviceInfo() {
        return this.deviceInfo;
    }

    public ConnectionStatusCode getStatusCode() {
        return this.statusCode;
    }
}
