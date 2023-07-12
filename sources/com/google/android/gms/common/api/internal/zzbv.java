package com.google.android.gms.common.api.internal;

import androidx.annotation.NonNull;
import com.google.android.gms.common.api.Api;

/* loaded from: classes.dex */
public final class zzbv {
    public final RegisterListenerMethod<Api.AnyClient, ?> zzlt;
    public final UnregisterListenerMethod<Api.AnyClient, ?> zzlu;

    public zzbv(@NonNull RegisterListenerMethod<Api.AnyClient, ?> registerListenerMethod, @NonNull UnregisterListenerMethod<Api.AnyClient, ?> unregisterListenerMethod) {
        this.zzlt = registerListenerMethod;
        this.zzlu = unregisterListenerMethod;
    }
}
