package com.opentok.otc;

/* loaded from: classes.dex */
public final class h {
    public static final h c = new h("otc_external_video", opentokJNI.otc_external_video_get());
    public static final h d = new h("otc_external_audio", opentokJNI.otc_external_audio_get());
    public static final h e = new h("otc_internal_video", opentokJNI.otc_internal_video_get());
    public static final h f;
    private static h[] g;
    private final int a;
    private final String b;

    static {
        h hVar = new h("otc_internal_audio", opentokJNI.otc_internal_audio_get());
        f = hVar;
        g = new h[]{c, d, e, hVar};
    }

    private h(String str, int i) {
        this.b = str;
        this.a = i;
    }

    public static h a(int i) {
        h[] hVarArr = g;
        if (i < hVarArr.length && i >= 0 && hVarArr[i].a == i) {
            return hVarArr[i];
        }
        int i2 = 0;
        while (true) {
            h[] hVarArr2 = g;
            if (i2 >= hVarArr2.length) {
                throw new IllegalArgumentException("No enum " + h.class + " with value " + i);
            } else if (hVarArr2[i2].a == i) {
                return hVarArr2[i2];
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
