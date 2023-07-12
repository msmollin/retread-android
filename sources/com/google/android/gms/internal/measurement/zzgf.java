package com.google.android.gms.internal.measurement;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteException;
import android.text.TextUtils;
import androidx.annotation.WorkerThread;
import androidx.collection.ArrayMap;
import com.facebook.appevents.AppEventsConstants;
import com.google.android.gms.common.internal.Preconditions;
import com.google.android.gms.common.util.Clock;
import com.google.android.gms.common.util.VisibleForTesting;
import com.google.android.gms.measurement.AppMeasurement;
import com.google.firebase.analytics.FirebaseAnalytics;
import java.io.IOException;
import java.util.Map;

/* loaded from: classes.dex */
public final class zzgf extends zzjq implements zzeh {
    @VisibleForTesting
    private static int zzalf = 65535;
    @VisibleForTesting
    private static int zzalg = 2;
    private final Map<String, Map<String, String>> zzalh;
    private final Map<String, Map<String, Boolean>> zzali;
    private final Map<String, Map<String, Boolean>> zzalj;
    private final Map<String, zzkk> zzalk;
    private final Map<String, Map<String, Integer>> zzall;
    private final Map<String, String> zzalm;

    /* JADX INFO: Access modifiers changed from: package-private */
    public zzgf(zzjr zzjrVar) {
        super(zzjrVar);
        this.zzalh = new ArrayMap();
        this.zzali = new ArrayMap();
        this.zzalj = new ArrayMap();
        this.zzalk = new ArrayMap();
        this.zzalm = new ArrayMap();
        this.zzall = new ArrayMap();
    }

    @WorkerThread
    private final zzkk zza(String str, byte[] bArr) {
        if (bArr == null) {
            return new zzkk();
        }
        zzabv zza = zzabv.zza(bArr, 0, bArr.length);
        zzkk zzkkVar = new zzkk();
        try {
            zzkkVar.zzb(zza);
            zzge().zzit().zze("Parsed config. version, gmp_app_id", zzkkVar.zzasp, zzkkVar.zzadm);
            return zzkkVar;
        } catch (IOException e) {
            zzge().zzip().zze("Unable to merge remote config. appId", zzfg.zzbm(str), e);
            return new zzkk();
        }
    }

    private static Map<String, String> zza(zzkk zzkkVar) {
        zzkl[] zzklVarArr;
        ArrayMap arrayMap = new ArrayMap();
        if (zzkkVar != null && zzkkVar.zzasr != null) {
            for (zzkl zzklVar : zzkkVar.zzasr) {
                if (zzklVar != null) {
                    arrayMap.put(zzklVar.zzny, zzklVar.value);
                }
            }
        }
        return arrayMap;
    }

    private final void zza(String str, zzkk zzkkVar) {
        zzkj[] zzkjVarArr;
        ArrayMap arrayMap = new ArrayMap();
        ArrayMap arrayMap2 = new ArrayMap();
        ArrayMap arrayMap3 = new ArrayMap();
        if (zzkkVar != null && zzkkVar.zzass != null) {
            for (zzkj zzkjVar : zzkkVar.zzass) {
                if (TextUtils.isEmpty(zzkjVar.name)) {
                    zzge().zzip().log("EventConfig contained null event name");
                } else {
                    String zzak = AppMeasurement.Event.zzak(zzkjVar.name);
                    if (!TextUtils.isEmpty(zzak)) {
                        zzkjVar.name = zzak;
                    }
                    arrayMap.put(zzkjVar.name, zzkjVar.zzasm);
                    arrayMap2.put(zzkjVar.name, zzkjVar.zzasn);
                    if (zzkjVar.zzaso != null) {
                        if (zzkjVar.zzaso.intValue() < zzalg || zzkjVar.zzaso.intValue() > zzalf) {
                            zzge().zzip().zze("Invalid sampling rate. Event name, sample rate", zzkjVar.name, zzkjVar.zzaso);
                        } else {
                            arrayMap3.put(zzkjVar.name, zzkjVar.zzaso);
                        }
                    }
                }
            }
        }
        this.zzali.put(str, arrayMap);
        this.zzalj.put(str, arrayMap2);
        this.zzall.put(str, arrayMap3);
    }

    @WorkerThread
    private final void zzbt(String str) {
        zzch();
        zzab();
        Preconditions.checkNotEmpty(str);
        if (this.zzalk.get(str) == null) {
            byte[] zzbe = zzix().zzbe(str);
            if (zzbe != null) {
                zzkk zza = zza(str, zzbe);
                this.zzalh.put(str, zza(zza));
                zza(str, zza);
                this.zzalk.put(str, zza);
                this.zzalm.put(str, null);
                return;
            }
            this.zzalh.put(str, null);
            this.zzali.put(str, null);
            this.zzalj.put(str, null);
            this.zzalk.put(str, null);
            this.zzalm.put(str, null);
            this.zzall.put(str, null);
        }
    }

    @Override // com.google.android.gms.internal.measurement.zzhg, com.google.android.gms.internal.measurement.zzec
    public final /* bridge */ /* synthetic */ Context getContext() {
        return super.getContext();
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @WorkerThread
    public final boolean zza(String str, byte[] bArr, String str2) {
        byte[] bArr2;
        ContentValues contentValues;
        zzkh[] zzkhVarArr;
        zzch();
        zzab();
        Preconditions.checkNotEmpty(str);
        zzkk zza = zza(str, bArr);
        int i = 0;
        if (zza == null) {
            return false;
        }
        zza(str, zza);
        this.zzalk.put(str, zza);
        this.zzalm.put(str, str2);
        this.zzalh.put(str, zza(zza));
        zzeb zziw = zziw();
        zzkd[] zzkdVarArr = zza.zzast;
        Preconditions.checkNotNull(zzkdVarArr);
        int length = zzkdVarArr.length;
        int i2 = 0;
        while (i2 < length) {
            zzkd zzkdVar = zzkdVarArr[i2];
            zzke[] zzkeVarArr = zzkdVar.zzarn;
            int length2 = zzkeVarArr.length;
            int i3 = i;
            while (i3 < length2) {
                zzke zzkeVar = zzkeVarArr[i3];
                String zzak = AppMeasurement.Event.zzak(zzkeVar.zzarq);
                if (zzak != null) {
                    zzkeVar.zzarq = zzak;
                }
                zzkf[] zzkfVarArr = zzkeVar.zzarr;
                int length3 = zzkfVarArr.length;
                for (int i4 = i; i4 < length3; i4++) {
                    zzkf zzkfVar = zzkfVarArr[i4];
                    String zzak2 = AppMeasurement.Param.zzak(zzkfVar.zzary);
                    if (zzak2 != null) {
                        zzkfVar.zzary = zzak2;
                    }
                }
                i3++;
                i = 0;
            }
            for (zzkh zzkhVar : zzkdVar.zzarm) {
                String zzak3 = AppMeasurement.UserProperty.zzak(zzkhVar.zzasf);
                if (zzak3 != null) {
                    zzkhVar.zzasf = zzak3;
                }
            }
            i2++;
            i = 0;
        }
        zziw.zzix().zza(str, zzkdVarArr);
        try {
            zza.zzast = null;
            bArr2 = new byte[zza.zzvm()];
            zza.zza(zzabw.zzb(bArr2, 0, bArr2.length));
        } catch (IOException e) {
            zzge().zzip().zze("Unable to serialize reduced-size config. Storing full config instead. appId", zzfg.zzbm(str), e);
            bArr2 = bArr;
        }
        zzei zzix = zzix();
        Preconditions.checkNotEmpty(str);
        zzix.zzab();
        zzix.zzch();
        new ContentValues().put("remote_config", bArr2);
        try {
            if (zzix.getWritableDatabase().update("apps", contentValues, "app_id = ?", new String[]{str}) == 0) {
                zzix.zzge().zzim().zzg("Failed to update remote config (got 0). appId", zzfg.zzbm(str));
            }
        } catch (SQLiteException e2) {
            zzix.zzge().zzim().zze("Error storing remote config. appId", zzfg.zzbm(str), e2);
        }
        return true;
    }

    @Override // com.google.android.gms.internal.measurement.zzhg
    public final /* bridge */ /* synthetic */ void zzab() {
        super.zzab();
    }

    @Override // com.google.android.gms.internal.measurement.zzhg, com.google.android.gms.internal.measurement.zzec
    public final /* bridge */ /* synthetic */ Clock zzbt() {
        return super.zzbt();
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @WorkerThread
    public final zzkk zzbu(String str) {
        zzch();
        zzab();
        Preconditions.checkNotEmpty(str);
        zzbt(str);
        return this.zzalk.get(str);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @WorkerThread
    public final String zzbv(String str) {
        zzab();
        return this.zzalm.get(str);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @WorkerThread
    public final void zzbw(String str) {
        zzab();
        this.zzalm.put(str, null);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @WorkerThread
    public final void zzbx(String str) {
        zzab();
        this.zzalk.remove(str);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public final boolean zzby(String str) {
        return AppEventsConstants.EVENT_PARAM_VALUE_YES.equals(zze(str, "measurement.upload.blacklist_internal"));
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public final boolean zzbz(String str) {
        return AppEventsConstants.EVENT_PARAM_VALUE_YES.equals(zze(str, "measurement.upload.blacklist_public"));
    }

    @Override // com.google.android.gms.internal.measurement.zzeh
    @WorkerThread
    public final String zze(String str, String str2) {
        zzab();
        zzbt(str);
        Map<String, String> map = this.zzalh.get(str);
        if (map != null) {
            return map.get(str2);
        }
        return null;
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

    @Override // com.google.android.gms.internal.measurement.zzjq
    protected final boolean zzhf() {
        return false;
    }

    @Override // com.google.android.gms.internal.measurement.zzjp
    public final /* bridge */ /* synthetic */ zzeb zziw() {
        return super.zziw();
    }

    @Override // com.google.android.gms.internal.measurement.zzjp
    public final /* bridge */ /* synthetic */ zzei zzix() {
        return super.zzix();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @WorkerThread
    public final boolean zzn(String str, String str2) {
        Boolean bool;
        zzab();
        zzbt(str);
        if (zzby(str) && zzka.zzci(str2)) {
            return true;
        }
        if (zzbz(str) && zzka.zzcc(str2)) {
            return true;
        }
        Map<String, Boolean> map = this.zzali.get(str);
        if (map == null || (bool = map.get(str2)) == null) {
            return false;
        }
        return bool.booleanValue();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @WorkerThread
    public final boolean zzo(String str, String str2) {
        Boolean bool;
        zzab();
        zzbt(str);
        if (FirebaseAnalytics.Event.ECOMMERCE_PURCHASE.equals(str2)) {
            return true;
        }
        Map<String, Boolean> map = this.zzalj.get(str);
        if (map == null || (bool = map.get(str2)) == null) {
            return false;
        }
        return bool.booleanValue();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @WorkerThread
    public final int zzp(String str, String str2) {
        Integer num;
        zzab();
        zzbt(str);
        Map<String, Integer> map = this.zzall.get(str);
        if (map == null || (num = map.get(str2)) == null) {
            return 1;
        }
        return num.intValue();
    }
}
