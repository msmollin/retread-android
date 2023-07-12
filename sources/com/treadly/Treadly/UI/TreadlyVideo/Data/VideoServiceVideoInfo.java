package com.treadly.Treadly.UI.TreadlyVideo.Data;

import androidx.annotation.NonNull;
import com.treadly.Treadly.Data.Model.StreamPermission;
import java.util.Date;

/* loaded from: classes2.dex */
public class VideoServiceVideoInfo implements Comparable<VideoServiceVideoInfo> {
    public boolean allowComments;
    public boolean allowVoiceMessage;
    public Date createdAt;
    public String description;
    public int duration;
    public String groupId;
    public String groupTitle;
    public String id;
    public StreamPermission permission;
    public String placeholderUrl;
    public String service;
    public String serviceId;
    public int size;
    public String thumbnailUrl;
    public String type;
    public String url;
    public String userAvatar;
    public String userId;
    public String userName;

    public VideoServiceVideoInfo(String str, String str2, String str3, String str4, String str5, String str6, String str7, String str8, String str9, String str10, String str11, int i, int i2, Date date, boolean z, boolean z2, StreamPermission streamPermission, String str12, String str13) {
        this.id = str;
        this.userId = str2;
        this.userName = str3;
        this.userAvatar = str4;
        this.type = str5;
        this.service = str6;
        this.serviceId = str7;
        this.url = str8;
        this.thumbnailUrl = str9;
        this.placeholderUrl = str10;
        this.description = str11;
        this.duration = i;
        this.size = i2;
        this.createdAt = date;
        this.allowComments = z;
        this.allowVoiceMessage = z2;
        this.permission = streamPermission;
        this.groupId = str12;
        this.groupTitle = str13;
    }

    @Override // java.lang.Comparable
    public int compareTo(@NonNull VideoServiceVideoInfo videoServiceVideoInfo) {
        return videoServiceVideoInfo.createdAt.compareTo(this.createdAt);
    }
}
