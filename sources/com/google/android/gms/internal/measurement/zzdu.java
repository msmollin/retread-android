package com.google.android.gms.internal.measurement;

import android.content.Context;
import android.os.Bundle;
import androidx.annotation.WorkerThread;
import androidx.collection.ArrayMap;
import com.google.android.gms.common.internal.Preconditions;
import com.google.android.gms.common.util.Clock;
import java.util.Map;

/* loaded from: classes.dex */
public final class zzdu extends zzhg {
    private final Map<String, Long> zzadf;
    private final Map<String, Integer> zzadg;
    private long zzadh;

    public zzdu(zzgl zzglVar) {
        super(zzglVar);
        this.zzadg = new ArrayMap();
        this.zzadf = new ArrayMap();
    }

    @WorkerThread
    private final void zza(long j, zzie zzieVar) {
        if (zzieVar == null) {
            zzge().zzit().log("Not logging ad exposure. No active activity");
        } else if (j < 1000) {
            zzge().zzit().zzg("Not logging ad exposure. Less than 1000 ms. exposure", Long.valueOf(j));
        } else {
            Bundle bundle = new Bundle();
            bundle.putLong("_xt", j);
            zzif.zza(zzieVar, bundle, true);
            zzfu().logEvent("am", "_xa", bundle);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    @WorkerThread
    public final void zza(String str, long j) {
        zzab();
        Preconditions.checkNotEmpty(str);
        if (this.zzadg.isEmpty()) {
            this.zzadh = j;
        }
        Integer num = this.zzadg.get(str);
        if (num != null) {
            this.zzadg.put(str, Integer.valueOf(num.intValue() + 1));
        } else if (this.zzadg.size() >= 100) {
            zzge().zzip().log("Too many ads visible");
        } else {
            this.zzadg.put(str, 1);
            this.zzadf.put(str, Long.valueOf(j));
        }
    }

    @WorkerThread
    private final void zza(String str, long j, zzie zzieVar) {
        if (zzieVar == null) {
            zzge().zzit().log("Not logging ad unit exposure. No active activity");
        } else if (j < 1000) {
            zzge().zzit().zzg("Not logging ad unit exposure. Less than 1000 ms. exposure", Long.valueOf(j));
        } else {
            Bundle bundle = new Bundle();
            bundle.putString("_ai", str);
            bundle.putLong("_xt", j);
            zzif.zza(zzieVar, bundle, true);
            zzfu().logEvent("am", "_xu", bundle);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    @WorkerThread
    public final void zzb(String str, long j) {
        zzab();
        Preconditions.checkNotEmpty(str);
        Integer num = this.zzadg.get(str);
        if (num == null) {
            zzge().zzim().zzg("Call to endAdUnitExposure for unknown ad unit id", str);
            return;
        }
        zzie zzkc = zzfy().zzkc();
        int intValue = num.intValue() - 1;
        if (intValue != 0) {
            this.zzadg.put(str, Integer.valueOf(intValue));
            return;
        }
        this.zzadg.remove(str);
        Long l = this.zzadf.get(str);
        if (l == null) {
            zzge().zzim().log("First ad unit exposure time was never set");
        } else {
            this.zzadf.remove(str);
            zza(str, j - l.longValue(), zzkc);
        }
        if (this.zzadg.isEmpty()) {
            if (this.zzadh == 0) {
                zzge().zzim().log("First ad exposure time was never set");
                return;
            }
            zza(j - this.zzadh, zzkc);
            this.zzadh = 0L;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    @WorkerThread
    public final void zzl(long j) {
        for (String str : this.zzadf.keySet()) {
            this.zzadf.put(str, Long.valueOf(j));
        }
        if (this.zzadf.isEmpty()) {
            return;
        }
        this.zzadh = j;
    }

    public final void beginAdUnitExposure(String str) {
        if (str == null || str.length() == 0) {
            zzge().zzim().log("Ad unit id must be a non-empty string");
            return;
        }
        zzgd().zzc(new zzdv(this, str, zzbt().elapsedRealtime()));
    }

    public final void endAdUnitExposure(String str) {
        if (str == null || str.length() == 0) {
            zzge().zzim().log("Ad unit id must be a non-empty string");
            return;
        }
        zzgd().zzc(new zzdw(this, str, zzbt().elapsedRealtime()));
    }

    @Override // com.google.android.gms.internal.measurement.zzhg, com.google.android.gms.internal.measurement.zzec
    public final /* bridge */ /* synthetic */ Context getContext() {
        return super.getContext();
    }

    @Override // com.google.android.gms.internal.measurement.zzhg
    public final /* bridge */ /* synthetic */ void zzab() {
        super.zzab();
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

    @WorkerThread
    public final void zzk(long j) {
        zzie zzkc = zzfy().zzkc();
        for (String str : this.zzadf.keySet()) {
            zza(str, j - this.zzadf.get(str).longValue(), zzkc);
        }
        if (!this.zzadf.isEmpty()) {
            zza(j - this.zzadh, zzkc);
        }
        zzl(j);
    }
}
