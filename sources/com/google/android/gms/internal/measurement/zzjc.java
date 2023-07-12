package com.google.android.gms.internal.measurement;

import android.annotation.TargetApi;
import android.app.job.JobParameters;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.IBinder;
import androidx.annotation.MainThread;
import com.facebook.internal.NativeProtocol;
import com.google.android.gms.common.internal.Preconditions;
import com.google.android.gms.internal.measurement.zzjg;

/* loaded from: classes.dex */
public final class zzjc<T extends Context & zzjg> {
    private final T zzabm;

    public zzjc(T t) {
        Preconditions.checkNotNull(t);
        this.zzabm = t;
    }

    public static boolean zza(Context context, boolean z) {
        Preconditions.checkNotNull(context);
        return zzka.zzc(context, Build.VERSION.SDK_INT >= 24 ? "com.google.android.gms.measurement.AppMeasurementJobService" : "com.google.android.gms.measurement.AppMeasurementService");
    }

    private final void zzb(Runnable runnable) {
        zzgl zzg = zzgl.zzg(this.zzabm);
        zzg.zzgd().zzc(new zzjf(this, zzg, runnable));
    }

    private final zzfg zzge() {
        return zzgl.zzg(this.zzabm).zzge();
    }

    @MainThread
    public final IBinder onBind(Intent intent) {
        if (intent == null) {
            zzge().zzim().log("onBind called with null intent");
            return null;
        }
        String action = intent.getAction();
        if ("com.google.android.gms.measurement.START".equals(action)) {
            return new zzgn(zzgl.zzg(this.zzabm));
        }
        zzge().zzip().zzg("onBind received unknown action", action);
        return null;
    }

    @MainThread
    public final void onCreate() {
        zzgl.zzg(this.zzabm).zzge().zzit().log("Local AppMeasurementService is starting up");
    }

    @MainThread
    public final void onDestroy() {
        zzgl.zzg(this.zzabm).zzge().zzit().log("Local AppMeasurementService is shutting down");
    }

    @MainThread
    public final void onRebind(Intent intent) {
        if (intent == null) {
            zzge().zzim().log("onRebind called with null intent");
            return;
        }
        zzge().zzit().zzg("onRebind called. action", intent.getAction());
    }

    @MainThread
    public final int onStartCommand(final Intent intent, int i, final int i2) {
        final zzfg zzge = zzgl.zzg(this.zzabm).zzge();
        if (intent == null) {
            zzge.zzip().log("AppMeasurementService started with null intent");
            return 2;
        }
        String action = intent.getAction();
        zzge.zzit().zze("Local AppMeasurementService called. startId, action", Integer.valueOf(i2), action);
        if ("com.google.android.gms.measurement.UPLOAD".equals(action)) {
            zzb(new Runnable(this, i2, zzge, intent) { // from class: com.google.android.gms.internal.measurement.zzjd
                private final int zzabp;
                private final zzjc zzapp;
                private final zzfg zzapq;
                private final Intent zzapr;

                /* JADX INFO: Access modifiers changed from: package-private */
                {
                    this.zzapp = this;
                    this.zzabp = i2;
                    this.zzapq = zzge;
                    this.zzapr = intent;
                }

                @Override // java.lang.Runnable
                public final void run() {
                    this.zzapp.zza(this.zzabp, this.zzapq, this.zzapr);
                }
            });
        }
        return 2;
    }

    @TargetApi(24)
    @MainThread
    public final boolean onStartJob(final JobParameters jobParameters) {
        final zzfg zzge = zzgl.zzg(this.zzabm).zzge();
        String string = jobParameters.getExtras().getString(NativeProtocol.WEB_DIALOG_ACTION);
        zzge.zzit().zzg("Local AppMeasurementJobService called. action", string);
        if ("com.google.android.gms.measurement.UPLOAD".equals(string)) {
            zzb(new Runnable(this, zzge, jobParameters) { // from class: com.google.android.gms.internal.measurement.zzje
                private final JobParameters zzabs;
                private final zzjc zzapp;
                private final zzfg zzaps;

                /* JADX INFO: Access modifiers changed from: package-private */
                {
                    this.zzapp = this;
                    this.zzaps = zzge;
                    this.zzabs = jobParameters;
                }

                @Override // java.lang.Runnable
                public final void run() {
                    this.zzapp.zza(this.zzaps, this.zzabs);
                }
            });
            return true;
        }
        return true;
    }

    @MainThread
    public final boolean onUnbind(Intent intent) {
        if (intent == null) {
            zzge().zzim().log("onUnbind called with null intent");
            return true;
        }
        zzge().zzit().zzg("onUnbind called for intent. action", intent.getAction());
        return true;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public final /* synthetic */ void zza(int i, zzfg zzfgVar, Intent intent) {
        if (this.zzabm.callServiceStopSelfResult(i)) {
            zzfgVar.zzit().zzg("Local AppMeasurementService processed last upload request. StartId", Integer.valueOf(i));
            zzge().zzit().log("Completed wakeful intent.");
            this.zzabm.zzb(intent);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public final /* synthetic */ void zza(zzfg zzfgVar, JobParameters jobParameters) {
        zzfgVar.zzit().log("AppMeasurementJobService processed last upload request.");
        this.zzabm.zza(jobParameters, false);
    }
}
