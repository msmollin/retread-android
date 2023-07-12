package com.treadly.Treadly.Data.Model;

/* loaded from: classes2.dex */
public class UserDiscoverInfo {
    public static final String defaultAvatar = "https://dgwxv5s2i5zkb.cloudfront.net/avatar/default.png";
    public String avatarPath;
    public String descriptionProfile;
    public String id;
    public boolean isPrivate;
    public String name;
    public int stepsTotal;

    public String avatarURL() {
        return (this.avatarPath != null || this.avatarPath.isEmpty()) ? this.avatarPath : "https://dgwxv5s2i5zkb.cloudfront.net/avatar/default.png";
    }

    public UserDiscoverInfo(String str, String str2, String str3, String str4, int i, boolean z) {
        this.id = str;
        this.name = str2;
        this.avatarPath = str3;
        this.descriptionProfile = str4;
        this.stepsTotal = i;
        this.isPrivate = z;
    }
}
