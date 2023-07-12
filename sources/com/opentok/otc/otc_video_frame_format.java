package com.opentok.otc;

/* loaded from: classes.dex */
public final class otc_video_frame_format {
    public static final otc_video_frame_format c = new otc_video_frame_format("OTC_VIDEO_FRAME_FORMAT_UNKNOWN", opentokJNI.OTC_VIDEO_FRAME_FORMAT_UNKNOWN_get());
    public static final otc_video_frame_format d = new otc_video_frame_format("OTC_VIDEO_FRAME_FORMAT_YUV420P", opentokJNI.OTC_VIDEO_FRAME_FORMAT_YUV420P_get());
    public static final otc_video_frame_format e = new otc_video_frame_format("OTC_VIDEO_FRAME_FORMAT_NV12", opentokJNI.OTC_VIDEO_FRAME_FORMAT_NV12_get());
    public static final otc_video_frame_format f = new otc_video_frame_format("OTC_VIDEO_FRAME_FORMAT_NV21", opentokJNI.OTC_VIDEO_FRAME_FORMAT_NV21_get());
    public static final otc_video_frame_format g = new otc_video_frame_format("OTC_VIDEO_FRAME_FORMAT_YUY2", opentokJNI.OTC_VIDEO_FRAME_FORMAT_YUY2_get());
    public static final otc_video_frame_format h = new otc_video_frame_format("OTC_VIDEO_FRAME_FORMAT_UYVY", opentokJNI.OTC_VIDEO_FRAME_FORMAT_UYVY_get());
    public static final otc_video_frame_format i = new otc_video_frame_format("OTC_VIDEO_FRAME_FORMAT_ARGB32", opentokJNI.OTC_VIDEO_FRAME_FORMAT_ARGB32_get());
    public static final otc_video_frame_format j = new otc_video_frame_format("OTC_VIDEO_FRAME_FORMAT_BGRA32", opentokJNI.OTC_VIDEO_FRAME_FORMAT_BGRA32_get());
    public static final otc_video_frame_format k = new otc_video_frame_format("OTC_VIDEO_FRAME_FORMAT_RGB24", opentokJNI.OTC_VIDEO_FRAME_FORMAT_RGB24_get());
    public static final otc_video_frame_format l = new otc_video_frame_format("OTC_VIDEO_FRAME_FORMAT_ABGR32", opentokJNI.OTC_VIDEO_FRAME_FORMAT_ABGR32_get());
    public static final otc_video_frame_format m = new otc_video_frame_format("OTC_VIDEO_FRAME_FORMAT_MJPEG", opentokJNI.OTC_VIDEO_FRAME_FORMAT_MJPEG_get());
    public static final otc_video_frame_format n = new otc_video_frame_format("OTC_VIDEO_FRAME_FORMAT_RGBA32", opentokJNI.OTC_VIDEO_FRAME_FORMAT_RGBA32_get());
    public static final otc_video_frame_format o = new otc_video_frame_format("OTC_VIDEO_FRAME_FORMAT_MAX");
    public static final otc_video_frame_format p;
    private static otc_video_frame_format[] q;
    private static int r;
    private final int a;
    private final String b;

    static {
        otc_video_frame_format otc_video_frame_formatVar = new otc_video_frame_format("OTC_VIDEO_FRAME_FORMAT_COMPRESSED", opentokJNI.OTC_VIDEO_FRAME_FORMAT_COMPRESSED_get());
        p = otc_video_frame_formatVar;
        q = new otc_video_frame_format[]{c, d, e, f, g, h, i, j, k, l, m, n, o, otc_video_frame_formatVar};
        r = 0;
    }

    private otc_video_frame_format(String str) {
        this.b = str;
        int i2 = r;
        r = i2 + 1;
        this.a = i2;
    }

    private otc_video_frame_format(String str, int i2) {
        this.b = str;
        this.a = i2;
        r = i2 + 1;
    }

    public static otc_video_frame_format a(int i2) {
        otc_video_frame_format[] otc_video_frame_formatVarArr = q;
        if (i2 < otc_video_frame_formatVarArr.length && i2 >= 0 && otc_video_frame_formatVarArr[i2].a == i2) {
            return otc_video_frame_formatVarArr[i2];
        }
        int i3 = 0;
        while (true) {
            otc_video_frame_format[] otc_video_frame_formatVarArr2 = q;
            if (i3 >= otc_video_frame_formatVarArr2.length) {
                throw new IllegalArgumentException("No enum " + otc_video_frame_format.class + " with value " + i2);
            } else if (otc_video_frame_formatVarArr2[i3].a == i2) {
                return otc_video_frame_formatVarArr2[i3];
            } else {
                i3++;
            }
        }
    }

    public String toString() {
        return this.b;
    }
}
