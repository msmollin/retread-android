package com.google.android.gms.common.api.internal;

import com.google.android.gms.common.api.Api;
import com.google.android.gms.common.api.Api.ApiOptions;
import com.google.android.gms.common.internal.Objects;

/* loaded from: classes.dex */
public final class zzh<O extends Api.ApiOptions> {
    private final Api<O> mApi;
    private final O zzcl;
    private final boolean zzeb;
    private final int zzec;

    private zzh(Api<O> api) {
        this.zzeb = true;
        this.mApi = api;
        this.zzcl = null;
        this.zzec = System.identityHashCode(this);
    }

    private zzh(Api<O> api, O o) {
        this.zzeb = false;
        this.mApi = api;
        this.zzcl = o;
        this.zzec = Objects.hashCode(this.mApi, this.zzcl);
    }

    public static <O extends Api.ApiOptions> zzh<O> zza(Api<O> api) {
        return new zzh<>(api);
    }

    public static <O extends Api.ApiOptions> zzh<O> zza(Api<O> api, O o) {
        return new zzh<>(api, o);
    }

    public final boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (obj instanceof zzh) {
            zzh zzhVar = (zzh) obj;
            return !this.zzeb && !zzhVar.zzeb && Objects.equal(this.mApi, zzhVar.mApi) && Objects.equal(this.zzcl, zzhVar.zzcl);
        }
        return false;
    }

    public final int hashCode() {
        return this.zzec;
    }

    public final String zzq() {
        return this.mApi.getName();
    }
}
