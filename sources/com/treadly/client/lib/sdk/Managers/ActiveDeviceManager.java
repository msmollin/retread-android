package com.treadly.client.lib.sdk.Managers;

import com.treadly.client.lib.sdk.Model.DeviceConnectionStatus;
import com.treadly.client.lib.sdk.Model.DeviceInfo;
import java.util.Timer;

/* loaded from: classes2.dex */
public class ActiveDeviceManager {
    private static final int deviceActiveReconnectTimeout = 2000;
    public static final ActiveDeviceManager sharedInstance = new ActiveDeviceManager();
    protected boolean deviceInactiveFlag = false;
    protected boolean deviceActiveFlag = false;
    protected boolean allowDeviceActiveFlag = false;
    protected DeviceInfo lastActiveDeviceInfo = null;
    private Timer deviceActiveReconnectTimer = null;

    private ActiveDeviceManager() {
    }

    public void clearDeviceActiveFlags() {
        if (this.deviceActiveFlag) {
            BleConnectionManager.shared.clearReconnectDevice();
        }
        this.deviceInactiveFlag = false;
        this.deviceActiveFlag = false;
        this.lastActiveDeviceInfo = null;
        this.allowDeviceActiveFlag = false;
    }

    public boolean setDeviceInactive() {
        if (BleConnectionManager.shared.isDeviceConnected()) {
            this.lastActiveDeviceInfo = BleConnectionManager.shared.connectedBlePeripheral;
            if (DeviceManager.shared.currentConnectionStatus == DeviceConnectionStatus.connected) {
                this.allowDeviceActiveFlag = true;
            } else {
                this.allowDeviceActiveFlag = false;
            }
        } else if (BleConnectionManager.shared.isDeviceConnecting()) {
            this.lastActiveDeviceInfo = BleConnectionManager.shared.connectingBlePeripheral;
            this.allowDeviceActiveFlag = false;
        } else {
            this.lastActiveDeviceInfo = null;
            this.allowDeviceActiveFlag = false;
        }
        if (this.lastActiveDeviceInfo != null) {
            BleConnectionManager.shared.disconnect();
            this.deviceInactiveFlag = true;
            this.deviceActiveFlag = false;
            return true;
        }
        return false;
    }

    public boolean setDeviceActive(boolean z) {
        if (this.deviceInactiveFlag) {
            if (z || !BleConnectionManager.shared.isBluetoothPoweredOn()) {
                this.deviceInactiveFlag = false;
                BleConnectionManager.shared.sendStateChangeEvents();
                return false;
            }
            BleConnectionManager.shared.initReconnect(this.lastActiveDeviceInfo, deviceActiveReconnectTimeout);
            this.deviceInactiveFlag = false;
            this.deviceActiveFlag = this.allowDeviceActiveFlag;
            return true;
        }
        return false;
    }

    public boolean handleExpectedInactiveDisconnect() {
        return this.deviceInactiveFlag;
    }

    public boolean handleExpectedActiveConnect() {
        boolean z = this.deviceActiveFlag;
        this.deviceActiveFlag = false;
        return z;
    }

    public boolean handleIgnoringStateChange() {
        return this.deviceInactiveFlag;
    }
}
