package com.opentok.otc;

/* loaded from: classes.dex */
public final class j {
    public static final j c = new j("OTC_SUBSCRIBER_VIDEO_DATA_EVERY_FRAME", opentokJNI.OTC_SUBSCRIBER_VIDEO_DATA_EVERY_FRAME_get());
    public static final j d = new j("OTC_SUBSCRIBER_VIDEO_DATA_FIRST_FRAME");
    public static final j e = new j("OTC_SUBSCRIBER_VIDEO_DATA_MAX");
    private static int f = 0;
    private final int a;
    private final String b;

    private j(String str) {
        this.b = str;
        int i = f;
        f = i + 1;
        this.a = i;
    }

    private j(String str, int i) {
        this.b = str;
        this.a = i;
        f = i + 1;
    }

    public final int a() {
        return this.a;
    }

    public String toString() {
        return this.b;
    }
}
