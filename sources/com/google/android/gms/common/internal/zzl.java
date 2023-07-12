package com.google.android.gms.common.internal;

import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.common.internal.PendingResultUtil;
import com.google.android.gms.tasks.TaskCompletionSource;
import java.util.concurrent.TimeUnit;

/* loaded from: classes.dex */
final class zzl implements PendingResult.StatusListener {
    private final /* synthetic */ PendingResult zzuo;
    private final /* synthetic */ TaskCompletionSource zzup;
    private final /* synthetic */ PendingResultUtil.ResultConverter zzuq;
    private final /* synthetic */ PendingResultUtil.StatusConverter zzur;

    /* JADX INFO: Access modifiers changed from: package-private */
    public zzl(PendingResult pendingResult, TaskCompletionSource taskCompletionSource, PendingResultUtil.ResultConverter resultConverter, PendingResultUtil.StatusConverter statusConverter) {
        this.zzuo = pendingResult;
        this.zzup = taskCompletionSource;
        this.zzuq = resultConverter;
        this.zzur = statusConverter;
    }

    @Override // com.google.android.gms.common.api.PendingResult.StatusListener
    public final void onComplete(Status status) {
        if (!status.isSuccess()) {
            this.zzup.setException(this.zzur.convert(status));
            return;
        }
        this.zzup.setResult(this.zzuq.convert(this.zzuo.await(0L, TimeUnit.MILLISECONDS)));
    }
}
