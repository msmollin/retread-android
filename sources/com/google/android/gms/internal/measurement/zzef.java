package com.google.android.gms.internal.measurement;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.text.TextUtils;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.Size;
import androidx.annotation.WorkerThread;
import com.facebook.appevents.AppEventsConstants;
import com.google.android.gms.common.internal.Preconditions;
import com.google.android.gms.common.util.Clock;
import com.google.android.gms.common.util.ProcessUtils;
import com.google.android.gms.common.util.VisibleForTesting;
import com.google.android.gms.common.wrappers.Wrappers;
import java.lang.reflect.InvocationTargetException;

/* loaded from: classes.dex */
public final class zzef extends zzhg {
    @NonNull
    private zzeh zzaet;
    private Boolean zzxz;

    /* JADX INFO: Access modifiers changed from: package-private */
    public zzef(zzgl zzglVar) {
        super(zzglVar);
        this.zzaet = zzeg.zzaeu;
    }

    public static long zzhh() {
        return zzew.zzahl.get().longValue();
    }

    public static long zzhi() {
        return zzew.zzagl.get().longValue();
    }

    public static boolean zzhk() {
        return zzew.zzagh.get().booleanValue();
    }

    @Override // com.google.android.gms.internal.measurement.zzhg, com.google.android.gms.internal.measurement.zzec
    public final /* bridge */ /* synthetic */ Context getContext() {
        return super.getContext();
    }

    @WorkerThread
    public final long zza(String str, @NonNull zzex<Long> zzexVar) {
        if (str != null) {
            String zze = this.zzaet.zze(str, zzexVar.getKey());
            if (!TextUtils.isEmpty(zze)) {
                try {
                    return zzexVar.get(Long.valueOf(Long.parseLong(zze))).longValue();
                } catch (NumberFormatException unused) {
                }
            }
        }
        return zzexVar.get().longValue();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public final void zza(@NonNull zzeh zzehVar) {
        this.zzaet = zzehVar;
    }

    @Override // com.google.android.gms.internal.measurement.zzhg
    public final /* bridge */ /* synthetic */ void zzab() {
        super.zzab();
    }

    @WorkerThread
    public final int zzar(@Size(min = 1) String str) {
        return zzb(str, zzew.zzagw);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Nullable
    @VisibleForTesting
    public final Boolean zzas(@Size(min = 1) String str) {
        Preconditions.checkNotEmpty(str);
        try {
            if (getContext().getPackageManager() == null) {
                zzge().zzim().log("Failed to load metadata: PackageManager is null");
                return null;
            }
            ApplicationInfo applicationInfo = Wrappers.packageManager(getContext()).getApplicationInfo(getContext().getPackageName(), 128);
            if (applicationInfo == null) {
                zzge().zzim().log("Failed to load metadata: ApplicationInfo is null");
                return null;
            } else if (applicationInfo.metaData == null) {
                zzge().zzim().log("Failed to load metadata: Metadata bundle is null");
                return null;
            } else if (applicationInfo.metaData.containsKey(str)) {
                return Boolean.valueOf(applicationInfo.metaData.getBoolean(str));
            } else {
                return null;
            }
        } catch (PackageManager.NameNotFoundException e) {
            zzge().zzim().zzg("Failed to load metadata: Package name not found", e);
            return null;
        }
    }

    public final boolean zzat(String str) {
        return AppEventsConstants.EVENT_PARAM_VALUE_YES.equals(this.zzaet.zze(str, "gaia_collection_enabled"));
    }

    public final boolean zzau(String str) {
        return AppEventsConstants.EVENT_PARAM_VALUE_YES.equals(this.zzaet.zze(str, "measurement.event_sampling_enabled"));
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @WorkerThread
    public final boolean zzav(String str) {
        return zzd(str, zzew.zzahu);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @WorkerThread
    public final boolean zzaw(String str) {
        return zzd(str, zzew.zzahw);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @WorkerThread
    public final boolean zzax(String str) {
        return zzd(str, zzew.zzahx);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public final boolean zzay(String str) {
        return zzd(str, zzew.zzahy);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @WorkerThread
    public final boolean zzaz(String str) {
        return zzd(str, zzew.zzahz);
    }

    @WorkerThread
    public final int zzb(String str, @NonNull zzex<Integer> zzexVar) {
        if (str != null) {
            String zze = this.zzaet.zze(str, zzexVar.getKey());
            if (!TextUtils.isEmpty(zze)) {
                try {
                    return zzexVar.get(Integer.valueOf(Integer.parseInt(zze))).intValue();
                } catch (NumberFormatException unused) {
                }
            }
        }
        return zzexVar.get().intValue();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @WorkerThread
    public final boolean zzba(String str) {
        return zzd(str, zzew.zzaic);
    }

    @Override // com.google.android.gms.internal.measurement.zzhg, com.google.android.gms.internal.measurement.zzec
    public final /* bridge */ /* synthetic */ Clock zzbt() {
        return super.zzbt();
    }

    @WorkerThread
    public final double zzc(String str, @NonNull zzex<Double> zzexVar) {
        if (str != null) {
            String zze = this.zzaet.zze(str, zzexVar.getKey());
            if (!TextUtils.isEmpty(zze)) {
                try {
                    return zzexVar.get(Double.valueOf(Double.parseDouble(zze))).doubleValue();
                } catch (NumberFormatException unused) {
                }
            }
        }
        return zzexVar.get().doubleValue();
    }

    @WorkerThread
    public final boolean zzd(String str, @NonNull zzex<Boolean> zzexVar) {
        Boolean bool;
        if (str != null) {
            String zze = this.zzaet.zze(str, zzexVar.getKey());
            if (!TextUtils.isEmpty(zze)) {
                bool = zzexVar.get(Boolean.valueOf(Boolean.parseBoolean(zze)));
                return bool.booleanValue();
            }
        }
        bool = zzexVar.get();
        return bool.booleanValue();
    }

    public final boolean zzds() {
        if (this.zzxz == null) {
            synchronized (this) {
                if (this.zzxz == null) {
                    ApplicationInfo applicationInfo = getContext().getApplicationInfo();
                    String myProcessName = ProcessUtils.getMyProcessName();
                    if (applicationInfo != null) {
                        String str = applicationInfo.processName;
                        this.zzxz = Boolean.valueOf(str != null && str.equals(myProcessName));
                    }
                    if (this.zzxz == null) {
                        this.zzxz = Boolean.TRUE;
                        zzge().zzim().log("My process not in the list of running processes");
                    }
                }
            }
        }
        return this.zzxz.booleanValue();
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

    public final boolean zzhg() {
        Boolean zzas = zzas("firebase_analytics_collection_deactivated");
        return zzas != null && zzas.booleanValue();
    }

    public final String zzhj() {
        zzfi zzim;
        String str;
        try {
            return (String) Class.forName("android.os.SystemProperties").getMethod("get", String.class, String.class).invoke(null, "debug.firebase.analytics.app", "");
        } catch (ClassNotFoundException e) {
            e = e;
            zzim = zzge().zzim();
            str = "Could not find SystemProperties class";
            zzim.zzg(str, e);
            return "";
        } catch (IllegalAccessException e2) {
            e = e2;
            zzim = zzge().zzim();
            str = "Could not access SystemProperties.get()";
            zzim.zzg(str, e);
            return "";
        } catch (NoSuchMethodException e3) {
            e = e3;
            zzim = zzge().zzim();
            str = "Could not find SystemProperties.get() method";
            zzim.zzg(str, e);
            return "";
        } catch (InvocationTargetException e4) {
            e = e4;
            zzim = zzge().zzim();
            str = "SystemProperties.get() threw an exception";
            zzim.zzg(str, e);
            return "";
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @WorkerThread
    public final boolean zzhl() {
        return zzd(zzfv().zzah(), zzew.zzahp);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @WorkerThread
    public final String zzhm() {
        String zzah = zzfv().zzah();
        zzex<String> zzexVar = zzew.zzahq;
        return zzah == null ? zzexVar.get() : zzexVar.get(this.zzaet.zze(zzah, zzexVar.getKey()));
    }
}
