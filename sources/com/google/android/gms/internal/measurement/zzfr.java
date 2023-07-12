package com.google.android.gms.internal.measurement;

import android.content.SharedPreferences;
import android.os.Build;
import android.text.TextUtils;
import android.util.Pair;
import androidx.annotation.NonNull;
import androidx.annotation.WorkerThread;
import com.google.android.gms.ads.identifier.AdvertisingIdClient;
import com.google.android.gms.common.util.VisibleForTesting;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.util.Locale;

/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: classes.dex */
public final class zzfr extends zzhh {
    @VisibleForTesting
    static final Pair<String, Long> zzajs = new Pair<>("", 0L);
    private SharedPreferences zzabf;
    public zzfv zzajt;
    public final zzfu zzaju;
    public final zzfu zzajv;
    public final zzfu zzajw;
    public final zzfu zzajx;
    public final zzfu zzajy;
    public final zzfu zzajz;
    public final zzfu zzaka;
    public final zzfw zzakb;
    private String zzakc;
    private boolean zzakd;
    private long zzake;
    private String zzakf;
    private long zzakg;
    private final Object zzakh;
    public final zzfu zzaki;
    public final zzfu zzakj;
    public final zzft zzakk;
    public final zzfu zzakl;
    public final zzfu zzakm;
    public boolean zzakn;

    /* JADX INFO: Access modifiers changed from: package-private */
    public zzfr(zzgl zzglVar) {
        super(zzglVar);
        this.zzaju = new zzfu(this, "last_upload", 0L);
        this.zzajv = new zzfu(this, "last_upload_attempt", 0L);
        this.zzajw = new zzfu(this, "backoff", 0L);
        this.zzajx = new zzfu(this, "last_delete_stale", 0L);
        this.zzaki = new zzfu(this, "time_before_start", 10000L);
        this.zzakj = new zzfu(this, "session_timeout", 1800000L);
        this.zzakk = new zzft(this, "start_new_session", true);
        this.zzakl = new zzfu(this, "last_pause_time", 0L);
        this.zzakm = new zzfu(this, "time_active", 0L);
        this.zzajy = new zzfu(this, "midnight_offset", 0L);
        this.zzajz = new zzfu(this, "first_open_time", 0L);
        this.zzaka = new zzfu(this, "app_install_time", 0L);
        this.zzakb = new zzfw(this, "app_instance_id", null);
        this.zzakh = new Object();
    }

    /* JADX INFO: Access modifiers changed from: private */
    @WorkerThread
    public final SharedPreferences zziy() {
        zzab();
        zzch();
        return this.zzabf;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @WorkerThread
    public final void setMeasurementEnabled(boolean z) {
        zzab();
        zzge().zzit().zzg("Setting measurementEnabled", Boolean.valueOf(z));
        SharedPreferences.Editor edit = zziy().edit();
        edit.putBoolean("measurement_enabled", z);
        edit.apply();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @NonNull
    @WorkerThread
    public final Pair<String, Boolean> zzbo(String str) {
        zzab();
        long elapsedRealtime = zzbt().elapsedRealtime();
        if (this.zzakc == null || elapsedRealtime >= this.zzake) {
            this.zzake = elapsedRealtime + zzgg().zza(str, zzew.zzagj);
            AdvertisingIdClient.setShouldSkipGmsCoreVersionCheck(true);
            try {
                AdvertisingIdClient.Info advertisingIdInfo = AdvertisingIdClient.getAdvertisingIdInfo(getContext());
                if (advertisingIdInfo != null) {
                    this.zzakc = advertisingIdInfo.getId();
                    this.zzakd = advertisingIdInfo.isLimitAdTrackingEnabled();
                }
                if (this.zzakc == null) {
                    this.zzakc = "";
                }
            } catch (Exception e) {
                zzge().zzis().zzg("Unable to get advertising id", e);
                this.zzakc = "";
            }
            AdvertisingIdClient.setShouldSkipGmsCoreVersionCheck(false);
            return new Pair<>(this.zzakc, Boolean.valueOf(this.zzakd));
        }
        return new Pair<>(this.zzakc, Boolean.valueOf(this.zzakd));
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @WorkerThread
    public final String zzbp(String str) {
        zzab();
        String str2 = (String) zzbo(str).first;
        MessageDigest messageDigest = zzka.getMessageDigest("MD5");
        if (messageDigest == null) {
            return null;
        }
        return String.format(Locale.US, "%032X", new BigInteger(1, messageDigest.digest(str2.getBytes())));
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @WorkerThread
    public final void zzbq(String str) {
        zzab();
        SharedPreferences.Editor edit = zziy().edit();
        edit.putString("gmp_app_id", str);
        edit.apply();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public final void zzbr(String str) {
        synchronized (this.zzakh) {
            this.zzakf = str;
            this.zzakg = zzbt().elapsedRealtime();
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @WorkerThread
    public final void zzf(boolean z) {
        zzab();
        zzge().zzit().zzg("Setting useService", Boolean.valueOf(z));
        SharedPreferences.Editor edit = zziy().edit();
        edit.putBoolean("use_service", z);
        edit.apply();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @WorkerThread
    public final boolean zzg(boolean z) {
        zzab();
        return zziy().getBoolean("measurement_enabled", z);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @WorkerThread
    public final void zzh(boolean z) {
        zzab();
        zzge().zzit().zzg("Updating deferred analytics collection", Boolean.valueOf(z));
        SharedPreferences.Editor edit = zziy().edit();
        edit.putBoolean("deferred_analytics_collection", z);
        edit.apply();
    }

    @Override // com.google.android.gms.internal.measurement.zzhh
    protected final boolean zzhf() {
        return true;
    }

    @Override // com.google.android.gms.internal.measurement.zzhh
    @WorkerThread
    protected final void zzih() {
        this.zzabf = getContext().getSharedPreferences("com.google.android.gms.measurement.prefs", 0);
        this.zzakn = this.zzabf.getBoolean("has_been_opened", false);
        if (!this.zzakn) {
            SharedPreferences.Editor edit = this.zzabf.edit();
            edit.putBoolean("has_been_opened", true);
            edit.apply();
        }
        this.zzajt = new zzfv(this, "health_monitor", Math.max(0L, zzew.zzagk.get().longValue()));
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @WorkerThread
    public final String zziz() {
        zzab();
        return zziy().getString("gmp_app_id", null);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public final String zzja() {
        synchronized (this.zzakh) {
            if (Math.abs(zzbt().elapsedRealtime() - this.zzakg) < 1000) {
                return this.zzakf;
            }
            return null;
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @WorkerThread
    public final Boolean zzjb() {
        zzab();
        if (zziy().contains("use_service")) {
            return Boolean.valueOf(zziy().getBoolean("use_service", false));
        }
        return null;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @WorkerThread
    public final void zzjc() {
        zzab();
        zzge().zzit().log("Clearing collection preferences.");
        boolean contains = zziy().contains("measurement_enabled");
        boolean zzg = contains ? zzg(true) : true;
        SharedPreferences.Editor edit = zziy().edit();
        edit.clear();
        edit.apply();
        if (contains) {
            setMeasurementEnabled(zzg);
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @WorkerThread
    public final String zzjd() {
        zzab();
        String string = zziy().getString("previous_os_version", null);
        zzfw().zzch();
        String str = Build.VERSION.RELEASE;
        if (!TextUtils.isEmpty(str) && !str.equals(string)) {
            SharedPreferences.Editor edit = zziy().edit();
            edit.putString("previous_os_version", str);
            edit.apply();
        }
        return string;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @WorkerThread
    public final boolean zzje() {
        zzab();
        return zziy().getBoolean("deferred_analytics_collection", false);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @WorkerThread
    public final boolean zzjf() {
        return this.zzabf.contains("deferred_analytics_collection");
    }
}
