package com.google.android.gms.internal.measurement;

/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: classes.dex */
public final class zzhq implements Runnable {
    private final /* synthetic */ zzhk zzanw;
    private final /* synthetic */ long zzaoa;

    /* JADX INFO: Access modifiers changed from: package-private */
    public zzhq(zzhk zzhkVar, long j) {
        this.zzanw = zzhkVar;
        this.zzaoa = j;
    }

    @Override // java.lang.Runnable
    public final void run() {
        zzhk zzhkVar = this.zzanw;
        long j = this.zzaoa;
        zzhkVar.zzab();
        zzhkVar.zzch();
        zzhkVar.zzge().zzis().log("Resetting analytics data (FE)");
        zzhkVar.zzgc().zzkj();
        if (zzhkVar.zzgg().zzba(zzhkVar.zzfv().zzah())) {
            zzhkVar.zzgf().zzajz.set(j);
        }
        boolean isEnabled = zzhkVar.zzacw.isEnabled();
        if (!zzhkVar.zzgg().zzhg()) {
            zzhkVar.zzgf().zzh(!isEnabled);
        }
        zzhkVar.zzfx().resetAnalyticsData();
        zzhkVar.zzanu = !isEnabled;
    }
}
