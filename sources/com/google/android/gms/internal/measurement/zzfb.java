package com.google.android.gms.internal.measurement;

import android.content.Context;
import androidx.annotation.WorkerThread;
import com.google.android.gms.common.util.Clock;
import com.google.firebase.iid.FirebaseInstanceId;
import java.math.BigInteger;
import java.util.Locale;

/* loaded from: classes.dex */
public final class zzfb extends zzhh {
    private String zzadm;
    private String zzadt;
    private long zzadx;
    private int zzaen;
    private int zzaie;
    private long zzaif;
    private String zztg;
    private String zzth;
    private String zzti;

    /* JADX INFO: Access modifiers changed from: package-private */
    public zzfb(zzgl zzglVar) {
        super(zzglVar);
    }

    @WorkerThread
    private final String zzgj() {
        zzab();
        if (!zzgg().zzay(this.zzti) || this.zzacw.isEnabled()) {
            try {
                return FirebaseInstanceId.getInstance().getId();
            } catch (IllegalStateException unused) {
                zzge().zzip().log("Failed to retrieve Firebase Instance Id");
                return null;
            }
        }
        return null;
    }

    @Override // com.google.android.gms.internal.measurement.zzhg, com.google.android.gms.internal.measurement.zzec
    public final /* bridge */ /* synthetic */ Context getContext() {
        return super.getContext();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public final String getGmpAppId() {
        zzch();
        return this.zzadm;
    }

    @Override // com.google.android.gms.internal.measurement.zzhg
    public final /* bridge */ /* synthetic */ void zzab() {
        super.zzab();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public final String zzah() {
        zzch();
        return this.zzti;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @WorkerThread
    public final zzdz zzbi(String str) {
        zzab();
        String zzah = zzah();
        String gmpAppId = getGmpAppId();
        zzch();
        String str2 = this.zzth;
        long zzij = zzij();
        zzch();
        String str3 = this.zzadt;
        zzch();
        zzab();
        if (this.zzaif == 0) {
            this.zzaif = this.zzacw.zzgb().zzd(getContext(), getContext().getPackageName());
        }
        long j = this.zzaif;
        boolean isEnabled = this.zzacw.isEnabled();
        boolean z = true;
        boolean z2 = !zzgf().zzakn;
        String zzgj = zzgj();
        zzch();
        long zzjt = this.zzacw.zzjt();
        int zzik = zzik();
        Boolean zzas = zzgg().zzas("google_analytics_adid_collection_enabled");
        boolean booleanValue = Boolean.valueOf(zzas == null || zzas.booleanValue()).booleanValue();
        Boolean zzas2 = zzgg().zzas("google_analytics_ssaid_collection_enabled");
        if (zzas2 != null && !zzas2.booleanValue()) {
            z = false;
        }
        return new zzdz(zzah, gmpAppId, str2, zzij, str3, 12451L, j, str, isEnabled, z2, zzgj, 0L, zzjt, zzik, booleanValue, Boolean.valueOf(z).booleanValue(), zzgf().zzje());
    }

    @Override // com.google.android.gms.internal.measurement.zzhg, com.google.android.gms.internal.measurement.zzec
    public final /* bridge */ /* synthetic */ Clock zzbt() {
        return super.zzbt();
    }

    @Override // com.google.android.gms.internal.measurement.zzhg
    public final /* bridge */ /* synthetic */ void zzfr() {
        super.zzfr();
    }

    @Override // com.google.android.gms.internal.measurement.zzhg
    public final /* bridge */ /* synthetic */ void zzfs() {
        super.zzfs();
    }

    @Override // com.google.android.gms.internal.measurement.zzhg
    public final /* bridge */ /* synthetic */ zzdu zzft() {
        return super.zzft();
    }

    @Override // com.google.android.gms.internal.measurement.zzhg
    public final /* bridge */ /* synthetic */ zzhk zzfu() {
        return super.zzfu();
    }

    @Override // com.google.android.gms.internal.measurement.zzhg
    public final /* bridge */ /* synthetic */ zzfb zzfv() {
        return super.zzfv();
    }

    @Override // com.google.android.gms.internal.measurement.zzhg
    public final /* bridge */ /* synthetic */ zzeo zzfw() {
        return super.zzfw();
    }

    @Override // com.google.android.gms.internal.measurement.zzhg
    public final /* bridge */ /* synthetic */ zzii zzfx() {
        return super.zzfx();
    }

    @Override // com.google.android.gms.internal.measurement.zzhg
    public final /* bridge */ /* synthetic */ zzif zzfy() {
        return super.zzfy();
    }

    @Override // com.google.android.gms.internal.measurement.zzhg
    public final /* bridge */ /* synthetic */ zzfc zzfz() {
        return super.zzfz();
    }

    @Override // com.google.android.gms.internal.measurement.zzhg
    public final /* bridge */ /* synthetic */ zzfe zzga() {
        return super.zzga();
    }

    @Override // com.google.android.gms.internal.measurement.zzhg
    public final /* bridge */ /* synthetic */ zzka zzgb() {
        return super.zzgb();
    }

    @Override // com.google.android.gms.internal.measurement.zzhg
    public final /* bridge */ /* synthetic */ zzjh zzgc() {
        return super.zzgc();
    }

    @Override // com.google.android.gms.internal.measurement.zzhg, com.google.android.gms.internal.measurement.zzec
    public final /* bridge */ /* synthetic */ zzgg zzgd() {
        return super.zzgd();
    }

    @Override // com.google.android.gms.internal.measurement.zzhg, com.google.android.gms.internal.measurement.zzec
    public final /* bridge */ /* synthetic */ zzfg zzge() {
        return super.zzge();
    }

    @Override // com.google.android.gms.internal.measurement.zzhg
    public final /* bridge */ /* synthetic */ zzfr zzgf() {
        return super.zzgf();
    }

    @Override // com.google.android.gms.internal.measurement.zzhg
    public final /* bridge */ /* synthetic */ zzef zzgg() {
        return super.zzgg();
    }

    @Override // com.google.android.gms.internal.measurement.zzhh
    protected final boolean zzhf() {
        return true;
    }

    /* JADX WARN: Can't wrap try/catch for region: R(15:1|(1:3)(7:47|48|49|(1:51)(2:67|(1:69))|52|53|(6:55|(1:57)|58|59|60|61))|4|(1:46)(1:8)|(1:(1:11)(1:12))|(3:14|(1:16)(1:(1:(9:26|27|28|29|(1:31)|32|(1:34)|36|(2:38|39)(2:41|42))(1:25))(1:21))|17)|45|27|28|29|(0)|32|(0)|36|(0)(0)) */
    /* JADX WARN: Code restructure failed: missing block: B:56:0x015a, code lost:
        r0 = move-exception;
     */
    /* JADX WARN: Code restructure failed: missing block: B:57:0x015b, code lost:
        zzge().zzim().zze("getGoogleAppId or isMeasurementEnabled failed with exception. appId", com.google.android.gms.internal.measurement.zzfg.zzbm(r3), r0);
     */
    /* JADX WARN: Removed duplicated region for block: B:30:0x00b0  */
    /* JADX WARN: Removed duplicated region for block: B:34:0x00db  */
    /* JADX WARN: Removed duplicated region for block: B:51:0x0142  */
    /* JADX WARN: Removed duplicated region for block: B:54:0x0148 A[Catch: IllegalStateException -> 0x015a, TRY_LEAVE, TryCatch #2 {IllegalStateException -> 0x015a, blocks: (B:49:0x0138, B:52:0x0144, B:54:0x0148), top: B:69:0x0138 }] */
    /* JADX WARN: Removed duplicated region for block: B:60:0x0172  */
    /* JADX WARN: Removed duplicated region for block: B:62:0x017d  */
    @Override // com.google.android.gms.internal.measurement.zzhh
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    protected final void zzih() {
        /*
            Method dump skipped, instructions count: 384
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.android.gms.internal.measurement.zzfb.zzih():void");
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @WorkerThread
    public final String zzii() {
        byte[] bArr = new byte[16];
        zzgb().zzlc().nextBytes(bArr);
        return String.format(Locale.US, "%032x", new BigInteger(1, bArr));
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public final int zzij() {
        zzch();
        return this.zzaie;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public final int zzik() {
        zzch();
        return this.zzaen;
    }
}
