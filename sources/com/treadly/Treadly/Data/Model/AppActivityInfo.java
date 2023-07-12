package com.treadly.Treadly.Data.Model;

import java.util.Date;

/* loaded from: classes2.dex */
public class AppActivityInfo {
    public String appVersion;
    public Date createdAt;
    public String customboardVersion;
    public String deviceAddress;
    public String genDescription;
    public String mainboardVersion;
    public String motorControllerVersion;
    public int referenceId;
    public String remoteVersion;
    public AppActivityType type;
    public String uuid;
    public String watchVersion;

    public AppActivityInfo(AppActivityType appActivityType, Date date, String str, String str2, String str3, String str4, String str5, String str6, String str7, String str8, String str9, int i) {
        this.type = appActivityType;
        this.createdAt = date;
        this.deviceAddress = str;
        this.customboardVersion = str2;
        this.mainboardVersion = str3;
        this.motorControllerVersion = str4;
        this.watchVersion = str5;
        this.remoteVersion = str6;
        this.appVersion = str7;
        this.uuid = str8;
        this.genDescription = str9;
        this.referenceId = i;
    }
}
