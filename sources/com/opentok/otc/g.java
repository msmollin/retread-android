package com.opentok.otc;

/* loaded from: classes.dex */
public final class g {
    public static final g c = new g("OTC_SUCCESS", opentokJNI.OTC_SUCCESS_get());
    public static final g d = new g("OTC_FALSE", opentokJNI.OTC_FALSE_get());
    public static final g e = new g("OTC_TRUE", opentokJNI.OTC_TRUE_get());
    private final int a;
    private final String b;

    private g(String str, int i) {
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
