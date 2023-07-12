package com.treadly.Treadly.Data.Model;

import java.util.Date;

/* loaded from: classes2.dex */
public class UserComment {
    public String comment;
    public Date createdAt;
    public UserInfo user;

    public UserComment(UserInfo userInfo, String str) {
        this.user = userInfo;
        this.comment = str;
        this.createdAt = null;
    }

    public UserComment(UserInfo userInfo, String str, Date date) {
        this.user = userInfo;
        this.comment = str;
        this.createdAt = date;
    }
}
