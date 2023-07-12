package com.google.android.gms.internal.measurement;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Application;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import androidx.annotation.MainThread;

/* JADX INFO: Access modifiers changed from: package-private */
@TargetApi(14)
@MainThread
/* loaded from: classes.dex */
public final class zzid implements Application.ActivityLifecycleCallbacks {
    private final /* synthetic */ zzhk zzanw;

    private zzid(zzhk zzhkVar) {
        this.zzanw = zzhkVar;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public /* synthetic */ zzid(zzhk zzhkVar, zzhl zzhlVar) {
        this(zzhkVar);
    }

    @Override // android.app.Application.ActivityLifecycleCallbacks
    public final void onActivityCreated(Activity activity, Bundle bundle) {
        Uri data;
        try {
            this.zzanw.zzge().zzit().log("onActivityCreated");
            Intent intent = activity.getIntent();
            if (intent != null && (data = intent.getData()) != null && data.isHierarchical()) {
                if (bundle == null) {
                    Bundle zza = this.zzanw.zzgb().zza(data);
                    this.zzanw.zzgb();
                    String str = zzka.zzd(intent) ? "gs" : "auto";
                    if (zza != null) {
                        this.zzanw.logEvent(str, "_cmp", zza);
                    }
                }
                String queryParameter = data.getQueryParameter("referrer");
                if (TextUtils.isEmpty(queryParameter)) {
                    return;
                }
                if (!(queryParameter.contains("gclid") && (queryParameter.contains("utm_campaign") || queryParameter.contains("utm_source") || queryParameter.contains("utm_medium") || queryParameter.contains("utm_term") || queryParameter.contains("utm_content")))) {
                    this.zzanw.zzge().zzis().log("Activity created with data 'referrer' param without gclid and at least one utm field");
                    return;
                }
                this.zzanw.zzge().zzis().zzg("Activity created with referrer", queryParameter);
                if (!TextUtils.isEmpty(queryParameter)) {
                    this.zzanw.setUserProperty("auto", "_ldl", queryParameter);
                }
            }
        } catch (Exception e) {
            this.zzanw.zzge().zzim().zzg("Throwable caught in onActivityCreated", e);
        }
        this.zzanw.zzfy().onActivityCreated(activity, bundle);
    }

    @Override // android.app.Application.ActivityLifecycleCallbacks
    public final void onActivityDestroyed(Activity activity) {
        this.zzanw.zzfy().onActivityDestroyed(activity);
    }

    @Override // android.app.Application.ActivityLifecycleCallbacks
    @MainThread
    public final void onActivityPaused(Activity activity) {
        this.zzanw.zzfy().onActivityPaused(activity);
        zzjh zzgc = this.zzanw.zzgc();
        zzgc.zzgd().zzc(new zzjl(zzgc, zzgc.zzbt().elapsedRealtime()));
    }

    @Override // android.app.Application.ActivityLifecycleCallbacks
    @MainThread
    public final void onActivityResumed(Activity activity) {
        this.zzanw.zzfy().onActivityResumed(activity);
        zzjh zzgc = this.zzanw.zzgc();
        zzgc.zzgd().zzc(new zzjk(zzgc, zzgc.zzbt().elapsedRealtime()));
    }

    @Override // android.app.Application.ActivityLifecycleCallbacks
    public final void onActivitySaveInstanceState(Activity activity, Bundle bundle) {
        this.zzanw.zzfy().onActivitySaveInstanceState(activity, bundle);
    }

    @Override // android.app.Application.ActivityLifecycleCallbacks
    public final void onActivityStarted(Activity activity) {
    }

    @Override // android.app.Application.ActivityLifecycleCallbacks
    public final void onActivityStopped(Activity activity) {
    }
}
