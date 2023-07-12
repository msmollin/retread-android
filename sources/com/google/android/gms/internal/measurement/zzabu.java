package com.google.android.gms.internal.measurement;

import com.github.mikephil.charting.utils.Utils;

/* loaded from: classes.dex */
public enum zzabu {
    INT(0),
    LONG(0L),
    FLOAT(Float.valueOf(0.0f)),
    DOUBLE(Double.valueOf((double) Utils.DOUBLE_EPSILON)),
    BOOLEAN(false),
    STRING(""),
    BYTE_STRING(zzyw.zzbqx),
    ENUM(null),
    MESSAGE(null);
    
    private final Object zzbwn;

    zzabu(Object obj) {
        this.zzbwn = obj;
    }
}
