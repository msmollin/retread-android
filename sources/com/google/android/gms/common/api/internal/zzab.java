package com.google.android.gms.common.api.internal;

import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.Status;
import java.util.Map;

/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: classes.dex */
public final class zzab implements PendingResult.StatusListener {
    private final /* synthetic */ BasePendingResult zzgy;
    private final /* synthetic */ zzaa zzgz;

    /* JADX INFO: Access modifiers changed from: package-private */
    public zzab(zzaa zzaaVar, BasePendingResult basePendingResult) {
        this.zzgz = zzaaVar;
        this.zzgy = basePendingResult;
    }

    @Override // com.google.android.gms.common.api.PendingResult.StatusListener
    public final void onComplete(Status status) {
        Map map;
        map = this.zzgz.zzgw;
        map.remove(this.zzgy);
    }
}
