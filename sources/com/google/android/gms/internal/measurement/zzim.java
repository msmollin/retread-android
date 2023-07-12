package com.google.android.gms.internal.measurement;

import android.os.RemoteException;

/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: classes.dex */
public final class zzim implements Runnable {
    private final /* synthetic */ zzdz zzane;
    private final /* synthetic */ zzii zzape;

    /* JADX INFO: Access modifiers changed from: package-private */
    public zzim(zzii zziiVar, zzdz zzdzVar) {
        this.zzape = zziiVar;
        this.zzane = zzdzVar;
    }

    @Override // java.lang.Runnable
    public final void run() {
        zzey zzeyVar;
        zzeyVar = this.zzape.zzaoy;
        if (zzeyVar == null) {
            this.zzape.zzge().zzim().log("Discarding data. Failed to send app launch");
            return;
        }
        try {
            zzeyVar.zza(this.zzane);
            this.zzape.zza(zzeyVar, null, this.zzane);
            this.zzape.zzcu();
        } catch (RemoteException e) {
            this.zzape.zzge().zzim().zzg("Failed to send app launch to the service", e);
        }
    }
}
