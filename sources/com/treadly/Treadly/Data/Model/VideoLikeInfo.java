package com.treadly.Treadly.Data.Model;

/* loaded from: classes2.dex */
public class VideoLikeInfo {
    public Boolean isLike;
    public Integer likeCount;
    public String serviceId;
    public String videoId;

    public VideoLikeInfo(String str, String str2, Integer num, Boolean bool) {
        this.videoId = str;
        this.serviceId = str2;
        this.likeCount = num;
        this.isLike = bool;
    }
}
