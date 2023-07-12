package com.google.android.gms.common.api.internal;

import com.google.android.gms.tasks.TaskCompletionSource;

/* loaded from: classes.dex */
final class zzae {
    private final zzh<?> zzhc;
    private final TaskCompletionSource<Boolean> zzhd = new TaskCompletionSource<>();

    public zzae(zzh<?> zzhVar) {
        this.zzhc = zzhVar;
    }

    public final TaskCompletionSource<Boolean> zzao() {
        return this.zzhd;
    }

    public final zzh<?> zzm() {
        return this.zzhc;
    }
}
