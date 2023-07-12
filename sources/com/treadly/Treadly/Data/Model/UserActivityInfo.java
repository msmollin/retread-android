package com.treadly.Treadly.Data.Model;

/* loaded from: classes2.dex */
public class UserActivityInfo {
    public String id;
    public boolean isBroadcasting;
    public boolean isOnline;

    public UserActivityInfo(String str, boolean z, boolean z2) {
        this.id = str;
        this.isOnline = z;
        this.isBroadcasting = z2;
    }
}
