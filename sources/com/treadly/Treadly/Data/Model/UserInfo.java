package com.treadly.Treadly.Data.Model;

/* loaded from: classes2.dex */
public class UserInfo {
    public static final String defaultAvatar = "https://dgwxv5s2i5zkb.cloudfront.net/avatar/default.png";
    public String avatarPath;
    public boolean broadcasting;
    public String email;
    public String id;
    public String name;
    public boolean online;

    public String avatarURL() {
        return (this.avatarPath != null || this.avatarPath.isEmpty()) ? this.avatarPath : "https://dgwxv5s2i5zkb.cloudfront.net/avatar/default.png";
    }

    public UserInfo(String str, String str2) {
        this.id = str;
        this.name = str2;
    }

    public UserInfo(String str, String str2, String str3) {
        this.id = str;
        this.name = str2;
        this.email = str3;
    }
}
