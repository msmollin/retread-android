package com.treadly.Treadly.UI.TreadlyVideo.Data;

import java.util.Date;

/* loaded from: classes2.dex */
public class VideoUploadInfo {
    public String compositeId;
    public Date createdAt;
    public String recordingId;
    public VideoServiceVideoService service;
    public String serviceId;
    public String userId;
    public String workoutId;

    public VideoUploadInfo(String str, String str2, VideoServiceVideoService videoServiceVideoService, Date date, String str3, String str4, String str5) {
        this.serviceId = str;
        this.workoutId = str2;
        this.service = videoServiceVideoService;
        this.createdAt = date;
        this.userId = str3;
        this.compositeId = str4;
        this.recordingId = str5;
    }
}
