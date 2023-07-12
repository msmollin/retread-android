package com.treadly.Treadly.Data.Model;

import java.util.Date;

/* loaded from: classes2.dex */
public class FriendInviteInfo {
    public Date createdAt;
    public String id;
    public String token;
    public String userAvatar;
    public String userId;
    public String userName;

    public FriendInviteInfo(String str, String str2, String str3, String str4, String str5, Date date) {
        this.id = str;
        this.userId = str2;
        this.userName = str3;
        this.userAvatar = str4;
        this.token = str5;
        this.createdAt = date;
    }
}
