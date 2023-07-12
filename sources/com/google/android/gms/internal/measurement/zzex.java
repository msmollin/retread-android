package com.google.android.gms.internal.measurement;

import com.google.android.gms.common.internal.Preconditions;
import com.google.android.gms.common.util.VisibleForTesting;

@VisibleForTesting
/* loaded from: classes.dex */
public final class zzex<V> {
    private final zzws<V> zzaid;
    private final String zzny;

    private zzex(String str, zzws<V> zzwsVar) {
        Preconditions.checkNotNull(zzwsVar);
        this.zzaid = zzwsVar;
        this.zzny = str;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static zzex<Double> zza(String str, double d, double d2) {
        zzxc zzxcVar;
        zzxcVar = zzew.zzagc;
        return new zzex<>(str, zzxcVar.zzb(str, -3.0d));
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static zzex<Long> zzb(String str, long j, long j2) {
        zzxc zzxcVar;
        zzxcVar = zzew.zzagc;
        return new zzex<>(str, zzxcVar.zze(str, j));
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static zzex<Boolean> zzb(String str, boolean z, boolean z2) {
        zzxc zzxcVar;
        zzxcVar = zzew.zzagc;
        return new zzex<>(str, zzxcVar.zzf(str, z));
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static zzex<Integer> zzc(String str, int i, int i2) {
        zzxc zzxcVar;
        zzxcVar = zzew.zzagc;
        return new zzex<>(str, zzxcVar.zzd(str, i));
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static zzex<String> zzd(String str, String str2, String str3) {
        zzxc zzxcVar;
        zzxcVar = zzew.zzagc;
        return new zzex<>(str, zzxcVar.zzv(str, str2));
    }

    public final V get() {
        return this.zzaid.get();
    }

    public final V get(V v) {
        return v != null ? v : this.zzaid.get();
    }

    public final String getKey() {
        return this.zzny;
    }
}
