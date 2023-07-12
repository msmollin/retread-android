package com.google.android.gms.internal.measurement;

import android.app.Application;
import android.content.Context;
import android.text.TextUtils;
import androidx.annotation.WorkerThread;
import com.google.android.gms.common.api.internal.GoogleServices;
import com.google.android.gms.common.internal.Preconditions;
import com.google.android.gms.common.util.Clock;
import com.google.android.gms.common.util.DefaultClock;
import com.google.android.gms.common.wrappers.Wrappers;
import com.google.android.gms.measurement.AppMeasurement;
import com.google.firebase.analytics.FirebaseAnalytics;
import java.util.concurrent.atomic.AtomicReference;

/* loaded from: classes.dex */
public class zzgl extends zzjr implements zzhi {
    private static volatile zzgl zzame;
    private final long zzaem;
    private final zzef zzamf;
    private final zzfr zzamg;
    private final zzfg zzamh;
    private final zzgg zzami;
    private final zzjh zzamj;
    private final AppMeasurement zzamk;
    private final FirebaseAnalytics zzaml;
    private final zzka zzamm;
    private final zzfe zzamn;
    private final zzif zzamo;
    private final zzhk zzamp;
    private final zzdu zzamq;
    private zzfc zzamr;
    private zzii zzams;
    private zzeo zzamt;
    private zzfb zzamu;
    private zzfx zzamv;
    private Boolean zzamw;
    private long zzamx;
    private int zzamy;
    private int zzamz;
    private final Context zzqx;
    private final Clock zzro;
    private boolean zzvo = false;

    private zzgl(zzhj zzhjVar) {
        zzfi zzip;
        String str;
        Preconditions.checkNotNull(zzhjVar);
        zza(this);
        this.zzqx = zzhjVar.zzqx;
        zzws.init(this.zzqx);
        this.zzaqs = -1L;
        this.zzro = DefaultClock.getInstance();
        this.zzaem = this.zzro.currentTimeMillis();
        this.zzamf = new zzef(this);
        zzfr zzfrVar = new zzfr(this);
        zzfrVar.zzm();
        this.zzamg = zzfrVar;
        zzfg zzfgVar = new zzfg(this);
        zzfgVar.zzm();
        this.zzamh = zzfgVar;
        zzka zzkaVar = new zzka(this);
        zzkaVar.zzm();
        this.zzamm = zzkaVar;
        zzfe zzfeVar = new zzfe(this);
        zzfeVar.zzm();
        this.zzamn = zzfeVar;
        this.zzamq = new zzdu(this);
        zzif zzifVar = new zzif(this);
        zzifVar.zzm();
        this.zzamo = zzifVar;
        zzhk zzhkVar = new zzhk(this);
        zzhkVar.zzm();
        this.zzamp = zzhkVar;
        this.zzamk = new AppMeasurement(this);
        this.zzaml = new FirebaseAnalytics(this);
        zzjh zzjhVar = new zzjh(this);
        zzjhVar.zzm();
        this.zzamj = zzjhVar;
        zzgg zzggVar = new zzgg(this);
        zzggVar.zzm();
        this.zzami = zzggVar;
        if (this.zzqx.getApplicationContext() instanceof Application) {
            zzhk zzfu = zzfu();
            if (zzfu.getContext().getApplicationContext() instanceof Application) {
                Application application = (Application) zzfu.getContext().getApplicationContext();
                if (zzfu.zzanp == null) {
                    zzfu.zzanp = new zzid(zzfu, null);
                }
                application.unregisterActivityLifecycleCallbacks(zzfu.zzanp);
                application.registerActivityLifecycleCallbacks(zzfu.zzanp);
                zzip = zzfu.zzge().zzit();
                str = "Registered activity lifecycle callback";
            }
            zzfk zzfkVar = new zzfk(this);
            zzfkVar.zzm();
            this.zzaqb = zzfkVar;
            zzgf zzgfVar = new zzgf(this);
            zzgfVar.zzm();
            this.zzaqa = zzgfVar;
            this.zzami.zzc(new zzgm(this, zzhjVar));
        }
        zzip = zzge().zzip();
        str = "Application context is not an Application";
        zzip.log(str);
        zzfk zzfkVar2 = new zzfk(this);
        zzfkVar2.zzm();
        this.zzaqb = zzfkVar2;
        zzgf zzgfVar2 = new zzgf(this);
        zzgfVar2.zzm();
        this.zzaqa = zzgfVar2;
        this.zzami.zzc(new zzgm(this, zzhjVar));
    }

    private static void zza(zzhg zzhgVar) {
        if (zzhgVar == null) {
            throw new IllegalStateException("Component not created");
        }
    }

    private static void zza(zzhh zzhhVar) {
        if (zzhhVar == null) {
            throw new IllegalStateException("Component not created");
        }
        if (zzhhVar.isInitialized()) {
            return;
        }
        String valueOf = String.valueOf(zzhhVar.getClass());
        StringBuilder sb = new StringBuilder(String.valueOf(valueOf).length() + 27);
        sb.append("Component not initialized: ");
        sb.append(valueOf);
        throw new IllegalStateException(sb.toString());
    }

    private final void zzch() {
        if (!this.zzvo) {
            throw new IllegalStateException("AppMeasurement is not initialized");
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static void zzfr() {
        throw new IllegalStateException("Unexpected call on client side");
    }

    public static zzgl zzg(Context context) {
        Preconditions.checkNotNull(context);
        Preconditions.checkNotNull(context.getApplicationContext());
        if (zzame == null) {
            synchronized (zzgl.class) {
                if (zzame == null) {
                    zzame = new zzgl(new zzhj(context));
                }
            }
        }
        return zzame;
    }

    @Override // com.google.android.gms.internal.measurement.zzjr, com.google.android.gms.internal.measurement.zzec
    public final Context getContext() {
        return this.zzqx;
    }

    @WorkerThread
    public final boolean isEnabled() {
        zzab();
        zzch();
        boolean z = false;
        if (zzgg().zzhg()) {
            return false;
        }
        Boolean zzas = zzgg().zzas("firebase_analytics_collection_enabled");
        if (zzas != null) {
            z = zzas.booleanValue();
        } else if (!GoogleServices.isMeasurementExplicitlyDisabled()) {
            z = true;
        }
        return zzgf().zzg(z);
    }

    @Override // com.google.android.gms.internal.measurement.zzjr
    @WorkerThread
    protected final void start() {
        zzab();
        if (zzgf().zzaju.get() == 0) {
            zzgf().zzaju.set(zzbt().currentTimeMillis());
        }
        if (Long.valueOf(zzgf().zzajz.get()).longValue() == 0) {
            zzge().zzit().zzg("Persisting first open", Long.valueOf(this.zzaem));
            zzgf().zzajz.set(this.zzaem);
        }
        if (zzjv()) {
            if (!TextUtils.isEmpty(zzfv().getGmpAppId())) {
                String zziz = zzgf().zziz();
                if (zziz == null) {
                    zzgf().zzbq(zzfv().getGmpAppId());
                } else if (!zziz.equals(zzfv().getGmpAppId())) {
                    zzge().zzir().log("Rechecking which service to use due to a GMP App Id change");
                    zzgf().zzjc();
                    this.zzams.disconnect();
                    this.zzams.zzdf();
                    zzgf().zzbq(zzfv().getGmpAppId());
                    zzgf().zzajz.set(this.zzaem);
                    zzgf().zzakb.zzbs(null);
                }
            }
            zzfu().zzbr(zzgf().zzakb.zzjg());
            if (!TextUtils.isEmpty(zzfv().getGmpAppId())) {
                boolean isEnabled = isEnabled();
                if (!zzgf().zzjf() && !zzgg().zzhg()) {
                    zzgf().zzh(!isEnabled);
                }
                if (!zzgg().zzaz(zzfv().zzah()) || isEnabled) {
                    zzfu().zzkb();
                }
                zzfx().zza(new AtomicReference<>());
            }
        } else if (isEnabled()) {
            if (!zzgb().zzx("android.permission.INTERNET")) {
                zzge().zzim().log("App is missing INTERNET permission");
            }
            if (!zzgb().zzx("android.permission.ACCESS_NETWORK_STATE")) {
                zzge().zzim().log("App is missing ACCESS_NETWORK_STATE permission");
            }
            if (!Wrappers.packageManager(getContext()).isCallerInstantApp()) {
                if (!zzgb.zza(getContext())) {
                    zzge().zzim().log("AppMeasurementReceiver not registered/enabled");
                }
                if (!zzjc.zza(getContext(), false)) {
                    zzge().zzim().log("AppMeasurementService not registered/enabled");
                }
            }
            zzge().zzim().log("Uploading is not possible. App measurement disabled");
        }
        super.start();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @WorkerThread
    public final void zza(zzhj zzhjVar) {
        zzfi zzir;
        String concat;
        zzab();
        zzeo zzeoVar = new zzeo(this);
        zzeoVar.zzm();
        this.zzamt = zzeoVar;
        zzfb zzfbVar = new zzfb(this);
        zzfbVar.zzm();
        this.zzamu = zzfbVar;
        zzfc zzfcVar = new zzfc(this);
        zzfcVar.zzm();
        this.zzamr = zzfcVar;
        zzii zziiVar = new zzii(this);
        zziiVar.zzm();
        this.zzams = zziiVar;
        this.zzamm.zzjw();
        this.zzamg.zzjw();
        this.zzamv = new zzfx(this);
        this.zzamu.zzjw();
        zzge().zzir().zzg("App measurement is starting up, version", 12451L);
        zzge().zzir().log("To enable debug logging run: adb shell setprop log.tag.FA VERBOSE");
        String zzah = zzfbVar.zzah();
        if (zzgb().zzcj(zzah)) {
            zzir = zzge().zzir();
            concat = "Faster debug mode event logging enabled. To disable, run:\n  adb shell setprop debug.firebase.analytics.app .none.";
        } else {
            zzir = zzge().zzir();
            String valueOf = String.valueOf(zzah);
            concat = valueOf.length() != 0 ? "To enable faster debug mode event logging run:\n  adb shell setprop debug.firebase.analytics.app ".concat(valueOf) : new String("To enable faster debug mode event logging run:\n  adb shell setprop debug.firebase.analytics.app ");
        }
        zzir.log(concat);
        zzge().zzis().log("Debug-level message logging enabled");
        if (this.zzamy != this.zzamz) {
            zzge().zzim().zze("Not all components initialized", Integer.valueOf(this.zzamy), Integer.valueOf(this.zzamz));
        }
        super.zza((zzjw) zzhjVar);
        this.zzvo = true;
    }

    @Override // com.google.android.gms.internal.measurement.zzjr
    @WorkerThread
    public final void zzab() {
        zzgd().zzab();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public final void zzb(zzhh zzhhVar) {
        this.zzamy++;
    }

    @Override // com.google.android.gms.internal.measurement.zzjr, com.google.android.gms.internal.measurement.zzec
    public final Clock zzbt() {
        return this.zzro;
    }

    public final zzdu zzft() {
        zza(this.zzamq);
        return this.zzamq;
    }

    public final zzhk zzfu() {
        zza((zzhh) this.zzamp);
        return this.zzamp;
    }

    public final zzfb zzfv() {
        zza((zzhh) this.zzamu);
        return this.zzamu;
    }

    @Override // com.google.android.gms.internal.measurement.zzjr
    public final zzeo zzfw() {
        zza((zzhh) this.zzamt);
        return this.zzamt;
    }

    public final zzii zzfx() {
        zza((zzhh) this.zzams);
        return this.zzams;
    }

    public final zzif zzfy() {
        zza((zzhh) this.zzamo);
        return this.zzamo;
    }

    public final zzfc zzfz() {
        zza((zzhh) this.zzamr);
        return this.zzamr;
    }

    @Override // com.google.android.gms.internal.measurement.zzjr
    public final zzfe zzga() {
        zza((zzhg) this.zzamn);
        return this.zzamn;
    }

    @Override // com.google.android.gms.internal.measurement.zzjr
    public final zzka zzgb() {
        zza((zzhg) this.zzamm);
        return this.zzamm;
    }

    public final zzjh zzgc() {
        zza((zzhh) this.zzamj);
        return this.zzamj;
    }

    @Override // com.google.android.gms.internal.measurement.zzjr, com.google.android.gms.internal.measurement.zzec
    public final zzgg zzgd() {
        zza((zzhh) this.zzami);
        return this.zzami;
    }

    @Override // com.google.android.gms.internal.measurement.zzjr, com.google.android.gms.internal.measurement.zzec
    public final zzfg zzge() {
        zza((zzhh) this.zzamh);
        return this.zzamh;
    }

    @Override // com.google.android.gms.internal.measurement.zzjr
    public final zzfr zzgf() {
        zza((zzhg) this.zzamg);
        return this.zzamg;
    }

    @Override // com.google.android.gms.internal.measurement.zzjr
    public final zzef zzgg() {
        return this.zzamf;
    }

    public final zzfg zzjo() {
        if (this.zzamh == null || !this.zzamh.isInitialized()) {
            return null;
        }
        return this.zzamh;
    }

    public final zzfx zzjp() {
        return this.zzamv;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public final zzgg zzjq() {
        return this.zzami;
    }

    public final AppMeasurement zzjr() {
        return this.zzamk;
    }

    public final FirebaseAnalytics zzjs() {
        return this.zzaml;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public final long zzjt() {
        Long valueOf = Long.valueOf(zzgf().zzajz.get());
        return valueOf.longValue() == 0 ? this.zzaem : Math.min(this.zzaem, valueOf.longValue());
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public final void zzju() {
        this.zzamz++;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @WorkerThread
    public final boolean zzjv() {
        zzch();
        zzab();
        if (this.zzamw == null || this.zzamx == 0 || (this.zzamw != null && !this.zzamw.booleanValue() && Math.abs(zzbt().elapsedRealtime() - this.zzamx) > 1000)) {
            this.zzamx = zzbt().elapsedRealtime();
            boolean z = false;
            if (zzgb().zzx("android.permission.INTERNET") && zzgb().zzx("android.permission.ACCESS_NETWORK_STATE") && (Wrappers.packageManager(getContext()).isCallerInstantApp() || (zzgb.zza(getContext()) && zzjc.zza(getContext(), false)))) {
                z = true;
            }
            this.zzamw = Boolean.valueOf(z);
            if (this.zzamw.booleanValue()) {
                this.zzamw = Boolean.valueOf(zzgb().zzcg(zzfv().getGmpAppId()));
            }
        }
        return this.zzamw.booleanValue();
    }
}
