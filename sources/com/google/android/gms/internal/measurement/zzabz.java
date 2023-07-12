package com.google.android.gms.internal.measurement;

import com.google.android.gms.internal.measurement.zzaby;
import java.io.IOException;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

/* loaded from: classes.dex */
public final class zzabz<M extends zzaby<M>, T> {
    public final int tag;
    private final int type;
    protected final Class<T> zzbwx;
    protected final boolean zzbwy;
    private final zzzq<?, ?> zzbwz;

    private zzabz(int i, Class<T> cls, int i2, boolean z) {
        this(11, cls, null, 810, false);
    }

    private zzabz(int i, Class<T> cls, zzzq<?, ?> zzzqVar, int i2, boolean z) {
        this.type = i;
        this.zzbwx = cls;
        this.tag = i2;
        this.zzbwy = false;
        this.zzbwz = null;
    }

    public static <M extends zzaby<M>, T extends zzace> zzabz<M, T> zza(int i, Class<T> cls, long j) {
        return new zzabz<>(11, cls, 810, false);
    }

    private final Object zzf(zzabv zzabvVar) {
        Class componentType = this.zzbwy ? this.zzbwx.getComponentType() : this.zzbwx;
        try {
            switch (this.type) {
                case 10:
                    zzace zzaceVar = (zzace) componentType.newInstance();
                    zzabvVar.zza(zzaceVar, this.tag >>> 3);
                    return zzaceVar;
                case 11:
                    zzace zzaceVar2 = (zzace) componentType.newInstance();
                    zzabvVar.zza(zzaceVar2);
                    return zzaceVar2;
                default:
                    int i = this.type;
                    StringBuilder sb = new StringBuilder(24);
                    sb.append("Unknown type ");
                    sb.append(i);
                    throw new IllegalArgumentException(sb.toString());
            }
        } catch (IOException e) {
            throw new IllegalArgumentException("Error reading extension field", e);
        } catch (IllegalAccessException e2) {
            String valueOf = String.valueOf(componentType);
            StringBuilder sb2 = new StringBuilder(String.valueOf(valueOf).length() + 33);
            sb2.append("Error creating instance of class ");
            sb2.append(valueOf);
            throw new IllegalArgumentException(sb2.toString(), e2);
        } catch (InstantiationException e3) {
            String valueOf2 = String.valueOf(componentType);
            StringBuilder sb3 = new StringBuilder(String.valueOf(valueOf2).length() + 33);
            sb3.append("Error creating instance of class ");
            sb3.append(valueOf2);
            throw new IllegalArgumentException(sb3.toString(), e3);
        }
    }

    public final boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj instanceof zzabz) {
            zzabz zzabzVar = (zzabz) obj;
            return this.type == zzabzVar.type && this.zzbwx == zzabzVar.zzbwx && this.tag == zzabzVar.tag && this.zzbwy == zzabzVar.zzbwy;
        }
        return false;
    }

    public final int hashCode() {
        return ((((((this.type + 1147) * 31) + this.zzbwx.hashCode()) * 31) + this.tag) * 31) + (this.zzbwy ? 1 : 0);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public final void zza(Object obj, zzabw zzabwVar) {
        try {
            zzabwVar.zzar(this.tag);
            switch (this.type) {
                case 10:
                    ((zzace) obj).zza(zzabwVar);
                    zzabwVar.zzg(this.tag >>> 3, 4);
                    return;
                case 11:
                    zzabwVar.zzb((zzace) obj);
                    return;
                default:
                    int i = this.type;
                    StringBuilder sb = new StringBuilder(24);
                    sb.append("Unknown type ");
                    sb.append(i);
                    throw new IllegalArgumentException(sb.toString());
            }
        } catch (IOException e) {
            throw new IllegalStateException(e);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public final T zzi(List<zzacg> list) {
        if (list == null) {
            return null;
        }
        if (!this.zzbwy) {
            if (list.isEmpty()) {
                return null;
            }
            return this.zzbwx.cast(zzf(zzabv.zzi(list.get(list.size() - 1).zzbrc)));
        }
        ArrayList arrayList = new ArrayList();
        for (int i = 0; i < list.size(); i++) {
            zzacg zzacgVar = list.get(i);
            if (zzacgVar.zzbrc.length != 0) {
                arrayList.add(zzf(zzabv.zzi(zzacgVar.zzbrc)));
            }
        }
        int size = arrayList.size();
        if (size == 0) {
            return null;
        }
        T cast = this.zzbwx.cast(Array.newInstance(this.zzbwx.getComponentType(), size));
        for (int i2 = 0; i2 < size; i2++) {
            Array.set(cast, i2, arrayList.get(i2));
        }
        return cast;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public final int zzv(Object obj) {
        int i = this.tag >>> 3;
        switch (this.type) {
            case 10:
                return (zzabw.zzaq(i) << 1) + ((zzace) obj).zzvm();
            case 11:
                return zzabw.zzb(i, (zzace) obj);
            default:
                int i2 = this.type;
                StringBuilder sb = new StringBuilder(24);
                sb.append("Unknown type ");
                sb.append(i2);
                throw new IllegalArgumentException(sb.toString());
        }
    }
}
