package com.google.android.gms.internal.measurement;

import android.annotation.TargetApi;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.job.JobInfo;
import android.app.job.JobScheduler;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.PersistableBundle;
import androidx.core.app.NotificationCompat;
import com.facebook.internal.NativeProtocol;
import com.google.android.gms.common.util.Clock;

/* loaded from: classes.dex */
public final class zzjn extends zzjq {
    private final zzem zzapy;
    private final AlarmManager zzyi;
    private Integer zzyj;

    /* JADX INFO: Access modifiers changed from: protected */
    public zzjn(zzjr zzjrVar) {
        super(zzjrVar);
        this.zzyi = (AlarmManager) getContext().getSystemService(NotificationCompat.CATEGORY_ALARM);
        this.zzapy = new zzjo(this, zzjrVar.zzla(), zzjrVar);
    }

    private final int getJobId() {
        if (this.zzyj == null) {
            String valueOf = String.valueOf(getContext().getPackageName());
            this.zzyj = Integer.valueOf((valueOf.length() != 0 ? "measurement".concat(valueOf) : new String("measurement")).hashCode());
        }
        return this.zzyj.intValue();
    }

    private final PendingIntent zzek() {
        Intent className = new Intent().setClassName(getContext(), "com.google.android.gms.measurement.AppMeasurementReceiver");
        className.setAction("com.google.android.gms.measurement.UPLOAD");
        return PendingIntent.getBroadcast(getContext(), 0, className, 0);
    }

    @TargetApi(24)
    private final void zzkl() {
        zzge().zzit().zzg("Cancelling job. JobID", Integer.valueOf(getJobId()));
        ((JobScheduler) getContext().getSystemService("jobscheduler")).cancel(getJobId());
    }

    public final void cancel() {
        zzch();
        this.zzyi.cancel(zzek());
        this.zzapy.cancel();
        if (Build.VERSION.SDK_INT >= 24) {
            zzkl();
        }
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

    public final void zzh(long j) {
        zzch();
        if (!zzgb.zza(getContext())) {
            zzge().zzis().log("Receiver not registered/enabled");
        }
        if (!zzjc.zza(getContext(), false)) {
            zzge().zzis().log("Service not registered/enabled");
        }
        cancel();
        long elapsedRealtime = zzbt().elapsedRealtime() + j;
        if (j < Math.max(0L, zzew.zzahf.get().longValue()) && !this.zzapy.zzef()) {
            zzge().zzit().log("Scheduling upload with DelayedRunnable");
            this.zzapy.zzh(j);
        }
        if (Build.VERSION.SDK_INT < 24) {
            zzge().zzit().log("Scheduling upload with AlarmManager");
            this.zzyi.setInexactRepeating(2, elapsedRealtime, Math.max(zzew.zzaha.get().longValue(), j), zzek());
            return;
        }
        zzge().zzit().log("Scheduling upload with JobScheduler");
        JobInfo.Builder builder = new JobInfo.Builder(getJobId(), new ComponentName(getContext(), "com.google.android.gms.measurement.AppMeasurementJobService"));
        builder.setMinimumLatency(j);
        builder.setOverrideDeadline(j << 1);
        PersistableBundle persistableBundle = new PersistableBundle();
        persistableBundle.putString(NativeProtocol.WEB_DIALOG_ACTION, "com.google.android.gms.measurement.UPLOAD");
        builder.setExtras(persistableBundle);
        JobInfo build = builder.build();
        zzge().zzit().zzg("Scheduling job. JobID", Integer.valueOf(getJobId()));
        ((JobScheduler) getContext().getSystemService("jobscheduler")).schedule(build);
    }

    @Override // com.google.android.gms.internal.measurement.zzjq
    protected final boolean zzhf() {
        this.zzyi.cancel(zzek());
        if (Build.VERSION.SDK_INT >= 24) {
            zzkl();
            return false;
        }
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
}
