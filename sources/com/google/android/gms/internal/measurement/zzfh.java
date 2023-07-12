package com.google.android.gms.internal.measurement;

import androidx.exifinterface.media.ExifInterface;

/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: classes.dex */
public final class zzfh implements Runnable {
    private final /* synthetic */ int zzaix;
    private final /* synthetic */ String zzaiy;
    private final /* synthetic */ Object zzaiz;
    private final /* synthetic */ Object zzaja;
    private final /* synthetic */ Object zzajb;
    private final /* synthetic */ zzfg zzajc;

    /* JADX INFO: Access modifiers changed from: package-private */
    public zzfh(zzfg zzfgVar, int i, String str, Object obj, Object obj2, Object obj3) {
        this.zzajc = zzfgVar;
        this.zzaix = i;
        this.zzaiy = str;
        this.zzaiz = obj;
        this.zzaja = obj2;
        this.zzajb = obj3;
    }

    @Override // java.lang.Runnable
    public final void run() {
        char c;
        long j;
        char c2;
        long j2;
        zzfg zzfgVar;
        char c3;
        zzfr zzgf = this.zzajc.zzacw.zzgf();
        if (!zzgf.isInitialized()) {
            this.zzajc.zza(6, "Persisted config not initialized. Not logging error/warn");
            return;
        }
        c = this.zzajc.zzaim;
        if (c == 0) {
            if (this.zzajc.zzgg().zzds()) {
                zzfgVar = this.zzajc;
                c3 = 'C';
            } else {
                zzfgVar = this.zzajc;
                c3 = 'c';
            }
            zzfgVar.zzaim = c3;
        }
        j = this.zzajc.zzadu;
        if (j < 0) {
            zzfg.zza(this.zzajc, 12451L);
        }
        char charAt = "01VDIWEA?".charAt(this.zzaix);
        c2 = this.zzajc.zzaim;
        j2 = this.zzajc.zzadu;
        String zza = zzfg.zza(true, this.zzaiy, this.zzaiz, this.zzaja, this.zzajb);
        StringBuilder sb = new StringBuilder(String.valueOf(zza).length() + 24);
        sb.append(ExifInterface.GPS_MEASUREMENT_2D);
        sb.append(charAt);
        sb.append(c2);
        sb.append(j2);
        sb.append(":");
        sb.append(zza);
        String sb2 = sb.toString();
        if (sb2.length() > 1024) {
            sb2 = this.zzaiy.substring(0, 1024);
        }
        zzgf.zzajt.zzc(sb2, 1L);
    }
}
