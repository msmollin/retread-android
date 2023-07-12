package com.google.android.gms.internal.measurement;

import android.os.RemoteException;
import android.text.TextUtils;

/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: classes.dex */
public final class zzir implements Runnable {
    private final /* synthetic */ zzdz zzane;
    private final /* synthetic */ zzii zzape;
    private final /* synthetic */ boolean zzapg = true;
    private final /* synthetic */ boolean zzaph;
    private final /* synthetic */ zzed zzapi;
    private final /* synthetic */ zzed zzapj;

    /* JADX INFO: Access modifiers changed from: package-private */
    public zzir(zzii zziiVar, boolean z, boolean z2, zzed zzedVar, zzdz zzdzVar, zzed zzedVar2) {
        this.zzape = zziiVar;
        this.zzaph = z2;
        this.zzapi = zzedVar;
        this.zzane = zzdzVar;
        this.zzapj = zzedVar2;
    }

    @Override // java.lang.Runnable
    public final void run() {
        zzey zzeyVar;
        zzeyVar = this.zzape.zzaoy;
        if (zzeyVar == null) {
            this.zzape.zzge().zzim().log("Discarding data. Failed to send conditional user property to service");
            return;
        }
        if (this.zzapg) {
            this.zzape.zza(zzeyVar, this.zzaph ? null : this.zzapi, this.zzane);
        } else {
            try {
                if (TextUtils.isEmpty(this.zzapj.packageName)) {
                    zzeyVar.zza(this.zzapi, this.zzane);
                } else {
                    zzeyVar.zzb(this.zzapi);
                }
            } catch (RemoteException e) {
                this.zzape.zzge().zzim().zzg("Failed to send conditional user property to the service", e);
            }
        }
        this.zzape.zzcu();
    }
}
