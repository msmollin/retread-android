package com.google.android.gms.common.api;

import java.util.Map;
import java.util.WeakHashMap;
import javax.annotation.concurrent.GuardedBy;

/* loaded from: classes.dex */
public abstract class zzc {
    @GuardedBy("sLock")
    private static final Map<Object, zzc> zzdo = new WeakHashMap();
    private static final Object sLock = new Object();

    public abstract void remove(int i);
}
