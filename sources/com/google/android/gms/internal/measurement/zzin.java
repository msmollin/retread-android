package com.google.android.gms.internal.measurement;

import android.os.RemoteException;

/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: classes.dex */
public final class zzin implements Runnable {
    private final /* synthetic */ zzie zzaow;
    private final /* synthetic */ zzii zzape;

    /* JADX INFO: Access modifiers changed from: package-private */
    public zzin(zzii zziiVar, zzie zzieVar) {
        this.zzape = zziiVar;
        this.zzaow = zzieVar;
    }

    @Override // java.lang.Runnable
    public final void run() {
        zzey zzeyVar;
        long j;
        String str;
        String str2;
        String packageName;
        zzeyVar = this.zzape.zzaoy;
        if (zzeyVar == null) {
            this.zzape.zzge().zzim().log("Failed to send current screen to service");
            return;
        }
        try {
            if (this.zzaow == null) {
                j = 0;
                str = null;
                str2 = null;
                packageName = this.zzape.getContext().getPackageName();
            } else {
                j = this.zzaow.zzaoj;
                str = this.zzaow.zzul;
                str2 = this.zzaow.zzaoi;
                packageName = this.zzape.getContext().getPackageName();
            }
            zzeyVar.zza(j, str, str2, packageName);
            this.zzape.zzcu();
        } catch (RemoteException e) {
            this.zzape.zzge().zzim().zzg("Failed to send current screen to the service", e);
        }
    }
}
