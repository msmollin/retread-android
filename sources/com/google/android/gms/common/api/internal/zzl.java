package com.google.android.gms.common.api.internal;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.internal.Preconditions;

/* loaded from: classes.dex */
final class zzl {
    private final int zzet;
    private final ConnectionResult zzeu;

    /* JADX INFO: Access modifiers changed from: package-private */
    public zzl(ConnectionResult connectionResult, int i) {
        Preconditions.checkNotNull(connectionResult);
        this.zzeu = connectionResult;
        this.zzet = i;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public final ConnectionResult getConnectionResult() {
        return this.zzeu;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public final int zzu() {
        return this.zzet;
    }
}
