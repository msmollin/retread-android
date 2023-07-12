package com.google.android.gms.internal.measurement;

/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: classes.dex */
public final class zziu implements Runnable {
    private final /* synthetic */ zzdz zzane;
    private final /* synthetic */ zzjx zzanl;
    private final /* synthetic */ zzii zzape;
    private final /* synthetic */ boolean zzaph;

    /* JADX INFO: Access modifiers changed from: package-private */
    public zziu(zzii zziiVar, boolean z, zzjx zzjxVar, zzdz zzdzVar) {
        this.zzape = zziiVar;
        this.zzaph = z;
        this.zzanl = zzjxVar;
        this.zzane = zzdzVar;
    }

    @Override // java.lang.Runnable
    public final void run() {
        zzey zzeyVar;
        zzeyVar = this.zzape.zzaoy;
        if (zzeyVar == null) {
            this.zzape.zzge().zzim().log("Discarding data. Failed to set user attribute");
            return;
        }
        this.zzape.zza(zzeyVar, this.zzaph ? null : this.zzanl, this.zzane);
        this.zzape.zzcu();
    }
}
