package com.google.android.gms.internal.measurement;

import android.os.RemoteException;

/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: classes.dex */
public final class zzik implements Runnable {
    private final /* synthetic */ zzdz zzane;
    private final /* synthetic */ zzii zzape;

    /* JADX INFO: Access modifiers changed from: package-private */
    public zzik(zzii zziiVar, zzdz zzdzVar) {
        this.zzape = zziiVar;
        this.zzane = zzdzVar;
    }

    @Override // java.lang.Runnable
    public final void run() {
        zzey zzeyVar;
        zzeyVar = this.zzape.zzaoy;
        if (zzeyVar == null) {
            this.zzape.zzge().zzim().log("Failed to reset data on the service; null service");
            return;
        }
        try {
            zzeyVar.zzd(this.zzane);
        } catch (RemoteException e) {
            this.zzape.zzge().zzim().zzg("Failed to reset data on the service", e);
        }
        this.zzape.zzcu();
    }
}
