package com.google.android.gms.common.api.internal;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.internal.BaseGmsClient;
import javax.annotation.concurrent.GuardedBy;

/* loaded from: classes.dex */
final class zzao extends zzbe {
    private final /* synthetic */ BaseGmsClient.ConnectionProgressReportCallbacks zzia;

    /* JADX INFO: Access modifiers changed from: package-private */
    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public zzao(zzam zzamVar, zzbc zzbcVar, BaseGmsClient.ConnectionProgressReportCallbacks connectionProgressReportCallbacks) {
        super(zzbcVar);
        this.zzia = connectionProgressReportCallbacks;
    }

    @Override // com.google.android.gms.common.api.internal.zzbe
    @GuardedBy("mLock")
    public final void zzaq() {
        this.zzia.onReportServiceBinding(new ConnectionResult(16, null));
    }
}
