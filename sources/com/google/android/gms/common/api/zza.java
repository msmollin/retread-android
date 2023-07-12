package com.google.android.gms.common.api;

import com.google.android.gms.common.api.PendingResult;

/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: classes.dex */
public final class zza implements PendingResult.StatusListener {
    private final /* synthetic */ Batch zzch;

    /* JADX INFO: Access modifiers changed from: package-private */
    public zza(Batch batch) {
        this.zzch = batch;
    }

    @Override // com.google.android.gms.common.api.PendingResult.StatusListener
    public final void onComplete(Status status) {
        Object obj;
        int i;
        boolean z;
        boolean z2;
        PendingResult[] pendingResultArr;
        obj = this.zzch.mLock;
        synchronized (obj) {
            if (this.zzch.isCanceled()) {
                return;
            }
            if (status.isCanceled()) {
                Batch.zza(this.zzch, true);
            } else if (!status.isSuccess()) {
                Batch.zzb(this.zzch, true);
            }
            Batch.zzb(this.zzch);
            i = this.zzch.zzcd;
            if (i == 0) {
                z = this.zzch.zzcf;
                if (z) {
                    super/*com.google.android.gms.common.api.internal.BasePendingResult*/.cancel();
                } else {
                    z2 = this.zzch.zzce;
                    Status status2 = z2 ? new Status(13) : Status.RESULT_SUCCESS;
                    Batch batch = this.zzch;
                    pendingResultArr = this.zzch.zzcg;
                    batch.setResult(new BatchResult(status2, pendingResultArr));
                }
            }
        }
    }
}
