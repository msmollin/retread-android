package com.treadly.Treadly.UI.TreadlyVideo.Data;

/* loaded from: classes2.dex */
public enum VideoServiceVideoType {
    live("live"),
    archive("archive");
    
    private final String type;

    VideoServiceVideoType(String str) {
        this.type = str;
    }

    public static VideoServiceVideoType fromVideoServiceVideoType(String str) {
        VideoServiceVideoType[] values;
        for (VideoServiceVideoType videoServiceVideoType : values()) {
            if (videoServiceVideoType.type.equals(str)) {
                return videoServiceVideoType;
            }
        }
        return null;
    }
}
