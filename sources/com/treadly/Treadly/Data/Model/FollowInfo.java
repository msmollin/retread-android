package com.treadly.Treadly.Data.Model;

/* loaded from: classes2.dex */
public class FollowInfo {
    public boolean isFollower;
    public boolean isFollowing;
    public int steps;
    public String userDescription;
    public UserInfo userInfo;

    public FollowInfo(String str, String str2, String str3, String str4, int i, boolean z, boolean z2) {
        this.userInfo = new UserInfo(str, str2);
        this.userInfo.avatarPath = str3;
        this.userDescription = str4;
        this.steps = i;
        this.isFollowing = z;
        this.isFollower = z2;
    }
}
