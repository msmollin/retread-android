package com.google.android.gms.common.internal;

import com.google.android.gms.common.api.Response;
import com.google.android.gms.common.api.Result;
import com.google.android.gms.common.internal.PendingResultUtil;

/* JADX INFO: Add missing generic type declarations: [R, T] */
/* loaded from: classes.dex */
final class zzm<R, T> implements PendingResultUtil.ResultConverter<R, T> {
    private final /* synthetic */ Response zzus;

    /* JADX INFO: Access modifiers changed from: package-private */
    public zzm(Response response) {
        this.zzus = response;
    }

    @Override // com.google.android.gms.common.internal.PendingResultUtil.ResultConverter
    public final /* synthetic */ Object convert(Result result) {
        this.zzus.setResult(result);
        return this.zzus;
    }
}
