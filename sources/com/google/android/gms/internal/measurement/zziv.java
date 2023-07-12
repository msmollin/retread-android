package com.google.android.gms.internal.measurement;

import android.os.RemoteException;
import java.util.concurrent.atomic.AtomicReference;

/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: classes.dex */
public final class zziv implements Runnable {
    private final /* synthetic */ zzdz zzane;
    private final /* synthetic */ boolean zzanz;
    private final /* synthetic */ zzii zzape;
    private final /* synthetic */ AtomicReference zzapf;

    /* JADX INFO: Access modifiers changed from: package-private */
    public zziv(zzii zziiVar, AtomicReference atomicReference, zzdz zzdzVar, boolean z) {
        this.zzape = zziiVar;
        this.zzapf = atomicReference;
        this.zzane = zzdzVar;
        this.zzanz = z;
    }

    @Override // java.lang.Runnable
    public final void run() {
        AtomicReference atomicReference;
        zzey zzeyVar;
        synchronized (this.zzapf) {
            try {
                zzeyVar = this.zzape.zzaoy;
            } catch (RemoteException e) {
                this.zzape.zzge().zzim().zzg("Failed to get user properties", e);
                atomicReference = this.zzapf;
            }
            if (zzeyVar == null) {
                this.zzape.zzge().zzim().log("Failed to get user properties");
                this.zzapf.notify();
                return;
            }
            this.zzapf.set(zzeyVar.zza(this.zzane, this.zzanz));
            this.zzape.zzcu();
            atomicReference = this.zzapf;
            atomicReference.notify();
        }
    }
}
