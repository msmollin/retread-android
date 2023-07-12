package com.google.android.gms.common.internal;

import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.common.internal.PendingResultUtil;

/* loaded from: classes.dex */
final class zzk implements PendingResultUtil.StatusConverter {
    @Override // com.google.android.gms.common.internal.PendingResultUtil.StatusConverter
    public final ApiException convert(Status status) {
        return ApiExceptionUtil.fromStatus(status);
    }
}
