package com.google.android.gms.internal.measurement;

import android.os.RemoteException;
import java.util.concurrent.atomic.AtomicReference;

/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: classes.dex */
public final class zzil implements Runnable {
    private final /* synthetic */ zzdz zzane;
    private final /* synthetic */ zzii zzape;
    private final /* synthetic */ AtomicReference zzapf;

    /* JADX INFO: Access modifiers changed from: package-private */
    public zzil(zzii zziiVar, AtomicReference atomicReference, zzdz zzdzVar) {
        this.zzape = zziiVar;
        this.zzapf = atomicReference;
        this.zzane = zzdzVar;
    }

    @Override // java.lang.Runnable
    public final void run() {
        AtomicReference atomicReference;
        zzey zzeyVar;
        synchronized (this.zzapf) {
            try {
                zzeyVar = this.zzape.zzaoy;
            } catch (RemoteException e) {
                this.zzape.zzge().zzim().zzg("Failed to get app instance id", e);
                atomicReference = this.zzapf;
            }
            if (zzeyVar == null) {
                this.zzape.zzge().zzim().log("Failed to get app instance id");
                this.zzapf.notify();
                return;
            }
            this.zzapf.set(zzeyVar.zzc(this.zzane));
            String str = (String) this.zzapf.get();
            if (str != null) {
                this.zzape.zzfu().zzbr(str);
                this.zzape.zzgf().zzakb.zzbs(str);
            }
            this.zzape.zzcu();
            atomicReference = this.zzapf;
            atomicReference.notify();
        }
    }
}
