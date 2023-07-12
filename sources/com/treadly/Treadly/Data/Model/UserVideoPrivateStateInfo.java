package com.treadly.Treadly.Data.Model;

/* loaded from: classes2.dex */
public class UserVideoPrivateStateInfo extends UserInfo {
    public boolean isTreadmillConnected;
    public String status;

    public UserVideoPrivateStateInfo(String str, String str2, String str3) {
        super(str, str2, str3);
    }

    public UserVideoPrivateStateInfo(String str, String str2, String str3, String str4, String str5) {
        super(str, str2, str3);
        this.status = str5;
        this.isTreadmillConnected = false;
        this.avatarPath = str4;
    }
}
