package com.treadly.client.lib.sdk.Model;

/* loaded from: classes2.dex */
public class BleRemoteTestResults {
    public String deviceId;
    public ComponentType deviceType;
    public int statusValue;
    public boolean success;
    public BleRemoteTestError testError;
    public int versionMajor;
    public int versionMinor;
    public int versionPatch;

    public BleRemoteTestResults(ComponentType componentType, String str, int i, int i2, int i3, boolean z, int i4, BleRemoteTestError bleRemoteTestError) {
        this.deviceType = componentType;
        this.deviceId = str;
        this.versionMajor = i;
        this.versionMinor = i2;
        this.versionPatch = i3;
        this.success = z;
        this.statusValue = i4;
        this.testError = bleRemoteTestError;
    }
}
