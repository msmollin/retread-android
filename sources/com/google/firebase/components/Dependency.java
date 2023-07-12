package com.google.firebase.components;

import com.google.android.gms.common.annotation.KeepForSdk;

@KeepForSdk
/* loaded from: classes.dex */
public final class Dependency {
    private final Class<?> zzam;
    private final int zzan;
    private final int zzao;

    private Dependency(Class<?> cls, int i, int i2) {
        this.zzam = (Class) zzk.zza(cls, "Null dependency interface.");
        this.zzan = i;
        this.zzao = i2;
    }

    @KeepForSdk
    public static Dependency optional(Class<?> cls) {
        return new Dependency(cls, 0, 0);
    }

    @KeepForSdk
    public static Dependency optionalProvider(Class<?> cls) {
        return new Dependency(cls, 0, 1);
    }

    @KeepForSdk
    public static Dependency required(Class<?> cls) {
        return new Dependency(cls, 1, 0);
    }

    @KeepForSdk
    public static Dependency requiredProvider(Class<?> cls) {
        return new Dependency(cls, 1, 1);
    }

    public final boolean equals(Object obj) {
        if (obj instanceof Dependency) {
            Dependency dependency = (Dependency) obj;
            if (this.zzam == dependency.zzam && this.zzan == dependency.zzan && this.zzao == dependency.zzao) {
                return true;
            }
        }
        return false;
    }

    public final int hashCode() {
        return this.zzao ^ ((((this.zzam.hashCode() ^ 1000003) * 1000003) ^ this.zzan) * 1000003);
    }

    public final String toString() {
        StringBuilder sb = new StringBuilder("Dependency{interface=");
        sb.append(this.zzam);
        sb.append(", required=");
        sb.append(this.zzan == 1);
        sb.append(", direct=");
        sb.append(this.zzao == 0);
        sb.append("}");
        return sb.toString();
    }

    public final Class<?> zzn() {
        return this.zzam;
    }

    public final boolean zzo() {
        return this.zzan == 1;
    }

    public final boolean zzp() {
        return this.zzao == 0;
    }
}
