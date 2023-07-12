package com.treadly.client.lib.sdk.Model;

/* loaded from: classes2.dex */
public class TreadlyDeviceLogInfo {
    public String appVersion;
    public String customboardVersion;
    public String macAddress;
    public String mainboardVersion;
    public String serialNumber;

    public TreadlyDeviceLogInfo(String str, String str2, String str3, String str4, String str5) {
        this.serialNumber = str;
        this.macAddress = str2;
        this.appVersion = str3;
        this.customboardVersion = str4;
        this.mainboardVersion = str5;
    }
}
