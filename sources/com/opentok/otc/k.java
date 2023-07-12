package com.opentok.otc;

/* loaded from: classes.dex */
public final class k {
    public static final k c = new k("OTC_VIDEO_FRAME_PLANE_Y", opentokJNI.OTC_VIDEO_FRAME_PLANE_Y_get());
    public static final k d = new k("OTC_VIDEO_FRAME_PLANE_U", opentokJNI.OTC_VIDEO_FRAME_PLANE_U_get());
    public static final k e = new k("OTC_VIDEO_FRAME_PLANE_V", opentokJNI.OTC_VIDEO_FRAME_PLANE_V_get());
    public static final k f = new k("OTC_VIDEO_FRAME_PLANE_PACKED", opentokJNI.OTC_VIDEO_FRAME_PLANE_PACKED_get());
    public static final k g = new k("OTC_VIDEO_FRAME_PLANE_UV_INTERLEAVED", opentokJNI.OTC_VIDEO_FRAME_PLANE_UV_INTERLEAVED_get());
    public static final k h = new k("OTC_VIDEO_FRAME_PLANE_VU_INTERLEAVED", opentokJNI.OTC_VIDEO_FRAME_PLANE_VU_INTERLEAVED_get());
    private final int a;
    private final String b;

    private k(String str, int i) {
        this.b = str;
        this.a = i;
    }

    public final int a() {
        return this.a;
    }

    public String toString() {
        return this.b;
    }
}
