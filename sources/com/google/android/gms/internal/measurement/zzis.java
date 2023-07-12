package com.google.android.gms.internal.measurement;

import android.os.RemoteException;
import android.text.TextUtils;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: classes.dex */
public final class zzis implements Runnable {
    private final /* synthetic */ zzdz zzane;
    private final /* synthetic */ String zzanh;
    private final /* synthetic */ String zzani;
    private final /* synthetic */ String zzanj;
    private final /* synthetic */ zzii zzape;
    private final /* synthetic */ AtomicReference zzapf;

    /* JADX INFO: Access modifiers changed from: package-private */
    public zzis(zzii zziiVar, AtomicReference atomicReference, String str, String str2, String str3, zzdz zzdzVar) {
        this.zzape = zziiVar;
        this.zzapf = atomicReference;
        this.zzanj = str;
        this.zzanh = str2;
        this.zzani = str3;
        this.zzane = zzdzVar;
    }

    @Override // java.lang.Runnable
    public final void run() {
        AtomicReference atomicReference;
        zzey zzeyVar;
        AtomicReference atomicReference2;
        List<zzed> zze;
        synchronized (this.zzapf) {
            try {
                zzeyVar = this.zzape.zzaoy;
            } catch (RemoteException e) {
                this.zzape.zzge().zzim().zzd("Failed to get conditional properties", zzfg.zzbm(this.zzanj), this.zzanh, e);
                this.zzapf.set(Collections.emptyList());
                atomicReference = this.zzapf;
            }
            if (zzeyVar == null) {
                this.zzape.zzge().zzim().zzd("Failed to get conditional properties", zzfg.zzbm(this.zzanj), this.zzanh, this.zzani);
                this.zzapf.set(Collections.emptyList());
                this.zzapf.notify();
                return;
            }
            if (TextUtils.isEmpty(this.zzanj)) {
                atomicReference2 = this.zzapf;
                zze = zzeyVar.zza(this.zzanh, this.zzani, this.zzane);
            } else {
                atomicReference2 = this.zzapf;
                zze = zzeyVar.zze(this.zzanj, this.zzanh, this.zzani);
            }
            atomicReference2.set(zze);
            this.zzape.zzcu();
            atomicReference = this.zzapf;
            atomicReference.notify();
        }
    }
}
