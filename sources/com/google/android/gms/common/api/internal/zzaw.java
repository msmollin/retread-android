package com.google.android.gms.common.api.internal;

import android.os.Bundle;
import com.google.android.gms.common.internal.GmsClientEventManager;

/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: classes.dex */
public final class zzaw implements GmsClientEventManager.GmsClientEventState {
    private final /* synthetic */ zzav zzit;

    /* JADX INFO: Access modifiers changed from: package-private */
    public zzaw(zzav zzavVar) {
        this.zzit = zzavVar;
    }

    @Override // com.google.android.gms.common.internal.GmsClientEventManager.GmsClientEventState
    public final Bundle getConnectionHint() {
        return null;
    }

    @Override // com.google.android.gms.common.internal.GmsClientEventManager.GmsClientEventState
    public final boolean isConnected() {
        return this.zzit.isConnected();
    }
}
