package com.opentok.otc;

/* loaded from: classes.dex */
public final class i {
    public static final i c = new i("OTC_PUBLISHER_VIDEO_TYPE_CAMERA", opentokJNI.OTC_PUBLISHER_VIDEO_TYPE_CAMERA_get());
    public static final i d;
    private static i[] e;
    private final int a;
    private final String b;

    static {
        i iVar = new i("OTC_PUBLISHER_VIDEO_TYPE_SCREEN", opentokJNI.OTC_PUBLISHER_VIDEO_TYPE_SCREEN_get());
        d = iVar;
        e = new i[]{c, iVar};
    }

    private i(String str, int i) {
        this.b = str;
        this.a = i;
    }

    public static i a(int i) {
        i[] iVarArr = e;
        if (i < iVarArr.length && i >= 0 && iVarArr[i].a == i) {
            return iVarArr[i];
        }
        int i2 = 0;
        while (true) {
            i[] iVarArr2 = e;
            if (i2 >= iVarArr2.length) {
                throw new IllegalArgumentException("No enum " + i.class + " with value " + i);
            } else if (iVarArr2[i2].a == i) {
                return iVarArr2[i2];
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
