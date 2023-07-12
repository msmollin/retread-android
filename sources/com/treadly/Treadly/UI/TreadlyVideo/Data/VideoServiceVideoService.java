package com.treadly.Treadly.UI.TreadlyVideo.Data;

/* loaded from: classes2.dex */
public enum VideoServiceVideoService {
    tokbox("tokbox"),
    bambuser("bambuser"),
    s3TreadlyTokbox("s3-treadlytokbox"),
    s3Bambuser("s3-bambuser"),
    compositeTokbox("composite-tokbox"),
    compositeBambuser("composite-bambuser"),
    buddyProfileAudio("buddy-profile-audio"),
    buddyProfileVideo("buddy-profile-video");
    
    String name;

    VideoServiceVideoService(String str) {
        this.name = str;
    }

    public String getName() {
        return this.name;
    }

    public static VideoServiceVideoService fromString(String str) {
        VideoServiceVideoService[] values;
        for (VideoServiceVideoService videoServiceVideoService : values()) {
            if (videoServiceVideoService.name.equals(str)) {
                return videoServiceVideoService;
            }
        }
        return null;
    }
}
