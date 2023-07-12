package com.google.android.gms.internal.measurement;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;
import android.util.Pair;
import androidx.annotation.GuardedBy;
import com.google.android.gms.common.internal.Preconditions;
import com.google.android.gms.common.util.Clock;
import com.google.android.gms.common.util.VisibleForTesting;
import com.google.android.gms.measurement.AppMeasurement;

/* loaded from: classes.dex */
public final class zzfg extends zzhh {
    private long zzadu;
    private char zzaim;
    @GuardedBy("this")
    private String zzain;
    private final zzfi zzaio;
    private final zzfi zzaip;
    private final zzfi zzaiq;
    private final zzfi zzair;
    private final zzfi zzais;
    private final zzfi zzait;
    private final zzfi zzaiu;
    private final zzfi zzaiv;
    private final zzfi zzaiw;

    /* JADX INFO: Access modifiers changed from: package-private */
    public zzfg(zzgl zzglVar) {
        super(zzglVar);
        this.zzaim = (char) 0;
        this.zzadu = -1L;
        this.zzaio = new zzfi(this, 6, false, false);
        this.zzaip = new zzfi(this, 6, true, false);
        this.zzaiq = new zzfi(this, 6, false, true);
        this.zzair = new zzfi(this, 5, false, false);
        this.zzais = new zzfi(this, 5, true, false);
        this.zzait = new zzfi(this, 5, false, true);
        this.zzaiu = new zzfi(this, 4, false, false);
        this.zzaiv = new zzfi(this, 3, false, false);
        this.zzaiw = new zzfi(this, 2, false, false);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static /* synthetic */ long zza(zzfg zzfgVar, long j) {
        zzfgVar.zzadu = 12451L;
        return 12451L;
    }

    @VisibleForTesting
    private static String zza(boolean z, Object obj) {
        String str;
        StackTraceElement[] stackTrace;
        String className;
        if (obj == null) {
            return "";
        }
        if (obj instanceof Integer) {
            obj = Long.valueOf(((Integer) obj).intValue());
        }
        if (obj instanceof Long) {
            if (z) {
                Long l = (Long) obj;
                if (Math.abs(l.longValue()) < 100) {
                    return String.valueOf(obj);
                }
                String str2 = String.valueOf(obj).charAt(0) == '-' ? "-" : "";
                String valueOf = String.valueOf(Math.abs(l.longValue()));
                long round = Math.round(Math.pow(10.0d, valueOf.length() - 1));
                long round2 = Math.round(Math.pow(10.0d, valueOf.length()) - 1.0d);
                StringBuilder sb = new StringBuilder(String.valueOf(str2).length() + 43 + String.valueOf(str2).length());
                sb.append(str2);
                sb.append(round);
                sb.append("...");
                sb.append(str2);
                sb.append(round2);
                return sb.toString();
            }
            return String.valueOf(obj);
        } else if (obj instanceof Boolean) {
            return String.valueOf(obj);
        } else {
            if (!(obj instanceof Throwable)) {
                if (!(obj instanceof zzfj)) {
                    return z ? "-" : String.valueOf(obj);
                }
                str = ((zzfj) obj).zzajf;
                return str;
            }
            Throwable th = (Throwable) obj;
            StringBuilder sb2 = new StringBuilder(z ? th.getClass().getName() : th.toString());
            String zzbn = zzbn(AppMeasurement.class.getCanonicalName());
            String zzbn2 = zzbn(zzgl.class.getCanonicalName());
            for (StackTraceElement stackTraceElement : th.getStackTrace()) {
                if (!stackTraceElement.isNativeMethod() && (className = stackTraceElement.getClassName()) != null) {
                    String zzbn3 = zzbn(className);
                    if (zzbn3.equals(zzbn) || zzbn3.equals(zzbn2)) {
                        sb2.append(": ");
                        sb2.append(stackTraceElement);
                        break;
                    }
                }
            }
            return sb2.toString();
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static String zza(boolean z, String str, Object obj, Object obj2, Object obj3) {
        if (str == null) {
            str = "";
        }
        String zza = zza(z, obj);
        String zza2 = zza(z, obj2);
        String zza3 = zza(z, obj3);
        StringBuilder sb = new StringBuilder();
        String str2 = "";
        if (!TextUtils.isEmpty(str)) {
            sb.append(str);
            str2 = ": ";
        }
        if (!TextUtils.isEmpty(zza)) {
            sb.append(str2);
            sb.append(zza);
            str2 = ", ";
        }
        if (!TextUtils.isEmpty(zza2)) {
            sb.append(str2);
            sb.append(zza2);
            str2 = ", ";
        }
        if (!TextUtils.isEmpty(zza3)) {
            sb.append(str2);
            sb.append(zza3);
        }
        return sb.toString();
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public static Object zzbm(String str) {
        if (str == null) {
            return null;
        }
        return new zzfj(str);
    }

    private static String zzbn(String str) {
        if (TextUtils.isEmpty(str)) {
            return "";
        }
        int lastIndexOf = str.lastIndexOf(46);
        return lastIndexOf == -1 ? str : str.substring(0, lastIndexOf);
    }

    private final String zziu() {
        String str;
        synchronized (this) {
            if (this.zzain == null) {
                this.zzain = zzew.zzagi.get();
            }
            str = this.zzain;
        }
        return str;
    }

    @Override // com.google.android.gms.internal.measurement.zzhg, com.google.android.gms.internal.measurement.zzec
    public final /* bridge */ /* synthetic */ Context getContext() {
        return super.getContext();
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @VisibleForTesting
    public final boolean isLoggable(int i) {
        return Log.isLoggable(zziu(), i);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @VisibleForTesting
    public final void zza(int i, String str) {
        Log.println(i, zziu(), str);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public final void zza(int i, boolean z, boolean z2, String str, Object obj, Object obj2, Object obj3) {
        String str2;
        if (!z && isLoggable(i)) {
            zza(i, zza(false, str, obj, obj2, obj3));
        }
        if (z2 || i < 5) {
            return;
        }
        Preconditions.checkNotNull(str);
        zzgg zzjq = this.zzacw.zzjq();
        if (zzjq == null) {
            str2 = "Scheduler not set. Not logging error/warn";
        } else if (zzjq.isInitialized()) {
            if (i < 0) {
                i = 0;
            }
            if (i >= 9) {
                i = 8;
            }
            zzjq.zzc(new zzfh(this, i, str, obj, obj2, obj3));
            return;
        } else {
            str2 = "Scheduler not initialized. Not logging error/warn";
        }
        zza(6, str2);
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

    @Override // com.google.android.gms.internal.measurement.zzhh
    protected final boolean zzhf() {
        return false;
    }

    public final zzfi zzim() {
        return this.zzaio;
    }

    public final zzfi zzin() {
        return this.zzaip;
    }

    public final zzfi zzio() {
        return this.zzaiq;
    }

    public final zzfi zzip() {
        return this.zzair;
    }

    public final zzfi zziq() {
        return this.zzait;
    }

    public final zzfi zzir() {
        return this.zzaiu;
    }

    public final zzfi zzis() {
        return this.zzaiv;
    }

    public final zzfi zzit() {
        return this.zzaiw;
    }

    public final String zziv() {
        Pair<String, Long> zzfi = zzgf().zzajt.zzfi();
        if (zzfi == null || zzfi == zzfr.zzajs) {
            return null;
        }
        String valueOf = String.valueOf(zzfi.second);
        String str = (String) zzfi.first;
        StringBuilder sb = new StringBuilder(String.valueOf(valueOf).length() + 1 + String.valueOf(str).length());
        sb.append(valueOf);
        sb.append(":");
        sb.append(str);
        return sb.toString();
    }
}
