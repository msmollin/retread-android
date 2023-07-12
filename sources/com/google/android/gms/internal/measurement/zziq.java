package com.google.android.gms.internal.measurement;

import android.os.RemoteException;
import android.text.TextUtils;

/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: classes.dex */
public final class zziq implements Runnable {
    private final /* synthetic */ zzdz zzane;
    private final /* synthetic */ String zzanj;
    private final /* synthetic */ zzeu zzank;
    private final /* synthetic */ zzii zzape;
    private final /* synthetic */ boolean zzapg = true;
    private final /* synthetic */ boolean zzaph;

    /* JADX INFO: Access modifiers changed from: package-private */
    public zziq(zzii zziiVar, boolean z, boolean z2, zzeu zzeuVar, zzdz zzdzVar, String str) {
        this.zzape = zziiVar;
        this.zzaph = z2;
        this.zzank = zzeuVar;
        this.zzane = zzdzVar;
        this.zzanj = str;
    }

    @Override // java.lang.Runnable
    public final void run() {
        zzey zzeyVar;
        zzeyVar = this.zzape.zzaoy;
        if (zzeyVar == null) {
            this.zzape.zzge().zzim().log("Discarding data. Failed to send event to service");
            return;
        }
        if (this.zzapg) {
            this.zzape.zza(zzeyVar, this.zzaph ? null : this.zzank, this.zzane);
        } else {
            try {
                if (TextUtils.isEmpty(this.zzanj)) {
                    zzeyVar.zza(this.zzank, this.zzane);
                } else {
                    zzeyVar.zza(this.zzank, this.zzanj, this.zzape.zzge().zziv());
                }
            } catch (RemoteException e) {
                this.zzape.zzge().zzim().zzg("Failed to send event to the service", e);
            }
        }
        this.zzape.zzcu();
    }
}
