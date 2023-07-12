package com.opentok.otc;

/* loaded from: classes.dex */
public final class otc_stream_video_type {
    public static final otc_stream_video_type c = new otc_stream_video_type("OTC_STREAM_VIDEO_TYPE_CAMERA", opentokJNI.OTC_STREAM_VIDEO_TYPE_CAMERA_get());
    public static final otc_stream_video_type d = new otc_stream_video_type("OTC_STREAM_VIDEO_TYPE_SCREEN", opentokJNI.OTC_STREAM_VIDEO_TYPE_SCREEN_get());
    public static final otc_stream_video_type e;
    private static otc_stream_video_type[] f;
    private final int a;
    private final String b;

    static {
        otc_stream_video_type otc_stream_video_typeVar = new otc_stream_video_type("OTC_STREAM_VIDEO_TYPE_CUSTOM", opentokJNI.OTC_STREAM_VIDEO_TYPE_CUSTOM_get());
        e = otc_stream_video_typeVar;
        f = new otc_stream_video_type[]{c, d, otc_stream_video_typeVar};
    }

    private otc_stream_video_type(String str, int i) {
        this.b = str;
        this.a = i;
    }

    public static otc_stream_video_type a(int i) {
        otc_stream_video_type[] otc_stream_video_typeVarArr = f;
        if (i < otc_stream_video_typeVarArr.length && i >= 0 && otc_stream_video_typeVarArr[i].a == i) {
            return otc_stream_video_typeVarArr[i];
        }
        int i2 = 0;
        while (true) {
            otc_stream_video_type[] otc_stream_video_typeVarArr2 = f;
            if (i2 >= otc_stream_video_typeVarArr2.length) {
                throw new IllegalArgumentException("No enum " + otc_stream_video_type.class + " with value " + i);
            } else if (otc_stream_video_typeVarArr2[i2].a == i) {
                return otc_stream_video_typeVarArr2[i2];
            } else {
                i2++;
            }
        }
    }

    public final int a() {
        return this.a;
    }

    public String toString() {
        return this.b;
    }
}
