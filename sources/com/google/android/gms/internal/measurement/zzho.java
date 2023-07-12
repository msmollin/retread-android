package com.google.android.gms.internal.measurement;

import java.util.concurrent.Callable;

/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: classes.dex */
public final class zzho implements Callable<String> {
    private final /* synthetic */ zzhk zzanw;

    /* JADX INFO: Access modifiers changed from: package-private */
    public zzho(zzhk zzhkVar) {
        this.zzanw = zzhkVar;
    }

    /* JADX WARN: Removed duplicated region for block: B:19:0x0068  */
    /* JADX WARN: Removed duplicated region for block: B:21:0x0072  */
    @Override // java.util.concurrent.Callable
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public final /* synthetic */ java.lang.String call() throws java.lang.Exception {
        /*
            r8 = this;
            com.google.android.gms.internal.measurement.zzhk r0 = r8.zzanw
            com.google.android.gms.internal.measurement.zzfr r0 = r0.zzgf()
            java.lang.String r0 = r0.zzja()
            if (r0 == 0) goto Ld
            return r0
        Ld:
            com.google.android.gms.internal.measurement.zzhk r0 = r8.zzanw
            com.google.android.gms.internal.measurement.zzhk r0 = r0.zzfu()
            com.google.android.gms.internal.measurement.zzgg r1 = r0.zzgd()
            boolean r1 = r1.zzjk()
            r2 = 0
            if (r1 == 0) goto L2c
            com.google.android.gms.internal.measurement.zzfg r0 = r0.zzge()
            com.google.android.gms.internal.measurement.zzfi r0 = r0.zzim()
            java.lang.String r1 = "Cannot retrieve app instance id from analytics worker thread"
        L28:
            r0.log(r1)
            goto L66
        L2c:
            r0.zzgd()
            boolean r1 = com.google.android.gms.internal.measurement.zzgg.isMainThread()
            if (r1 == 0) goto L40
            com.google.android.gms.internal.measurement.zzfg r0 = r0.zzge()
            com.google.android.gms.internal.measurement.zzfi r0 = r0.zzim()
            java.lang.String r1 = "Cannot retrieve app instance id from main thread"
            goto L28
        L40:
            com.google.android.gms.common.util.Clock r1 = r0.zzbt()
            long r1 = r1.elapsedRealtime()
            r3 = 120000(0x1d4c0, double:5.9288E-319)
            java.lang.String r5 = r0.zzae(r3)
            com.google.android.gms.common.util.Clock r6 = r0.zzbt()
            long r6 = r6.elapsedRealtime()
            long r6 = r6 - r1
            if (r5 != 0) goto L65
            int r1 = (r6 > r3 ? 1 : (r6 == r3 ? 0 : -1))
            if (r1 >= 0) goto L65
            long r3 = r3 - r6
            java.lang.String r0 = r0.zzae(r3)
            r2 = r0
            goto L66
        L65:
            r2 = r5
        L66:
            if (r2 == 0) goto L72
            com.google.android.gms.internal.measurement.zzhk r8 = r8.zzanw
            com.google.android.gms.internal.measurement.zzfr r8 = r8.zzgf()
            r8.zzbr(r2)
            return r2
        L72:
            java.util.concurrent.TimeoutException r8 = new java.util.concurrent.TimeoutException
            r8.<init>()
            throw r8
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.android.gms.internal.measurement.zzho.call():java.lang.Object");
    }
}
