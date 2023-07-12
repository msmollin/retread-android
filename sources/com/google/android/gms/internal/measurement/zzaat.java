package com.google.android.gms.internal.measurement;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/* loaded from: classes.dex */
final class zzaat {
    private static final zzaat zzbtq = new zzaat();
    private final zzaaw zzbtr;
    private final ConcurrentMap<Class<?>, zzaav<?>> zzbts = new ConcurrentHashMap();

    private zzaat() {
        String[] strArr = {"com.google.protobuf.AndroidProto3SchemaFactory"};
        zzaaw zzaawVar = null;
        for (int i = 0; i <= 0; i++) {
            zzaawVar = zzfl(strArr[0]);
            if (zzaawVar != null) {
                break;
            }
        }
        this.zzbtr = zzaawVar == null ? new zzaad() : zzaawVar;
    }

    private static zzaaw zzfl(String str) {
        try {
            return (zzaaw) Class.forName(str).getConstructor(new Class[0]).newInstance(new Object[0]);
        } catch (Throwable unused) {
            return null;
        }
    }

    public static zzaat zzud() {
        return zzbtq;
    }

    public final <T> zzaav<T> zzt(T t) {
        Class<?> cls = t.getClass();
        zzzr.zza(cls, "messageType");
        zzaav<T> zzaavVar = (zzaav<T>) this.zzbts.get(cls);
        if (zzaavVar == null) {
            zzaav<T> zzg = this.zzbtr.zzg(cls);
            zzzr.zza(cls, "messageType");
            zzzr.zza(zzg, "schema");
            zzaav<T> zzaavVar2 = (zzaav<T>) this.zzbts.putIfAbsent(cls, zzg);
            return zzaavVar2 != null ? zzaavVar2 : zzg;
        }
        return zzaavVar;
    }
}
