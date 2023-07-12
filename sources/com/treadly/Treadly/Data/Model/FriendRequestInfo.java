package com.treadly.Treadly.Data.Model;

/* loaded from: classes2.dex */
public class FriendRequestInfo {
    public UserInfo receiver;
    public UserInfo sender;

    public FriendRequestInfo(UserInfo userInfo, UserInfo userInfo2) {
        this.sender = userInfo;
        this.receiver = userInfo2;
    }
}
