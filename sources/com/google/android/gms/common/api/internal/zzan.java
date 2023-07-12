package com.google.android.gms.common.api.internal;

import com.google.android.gms.common.ConnectionResult;
import javax.annotation.concurrent.GuardedBy;

/* loaded from: classes.dex */
final class zzan extends zzbe {
    private final /* synthetic */ ConnectionResult zzhy;
    private final /* synthetic */ zzam zzhz;

    /* JADX INFO: Access modifiers changed from: package-private */
    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public zzan(zzam zzamVar, zzbc zzbcVar, ConnectionResult connectionResult) {
        super(zzbcVar);
        this.zzhz = zzamVar;
        this.zzhy = connectionResult;
    }

    @Override // com.google.android.gms.common.api.internal.zzbe
    @GuardedBy("mLock")
    public final void zzaq() {
        this.zzhz.zzhv.zze(this.zzhy);
    }
}
