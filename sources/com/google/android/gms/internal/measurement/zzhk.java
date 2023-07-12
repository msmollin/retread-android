package com.google.android.gms.internal.measurement;

import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.os.Parcelable;
import android.text.TextUtils;
import androidx.annotation.Nullable;
import androidx.annotation.WorkerThread;
import androidx.collection.ArrayMap;
import androidx.core.app.NotificationCompat;
import com.google.android.gms.common.internal.Preconditions;
import com.google.android.gms.common.util.Clock;
import com.google.android.gms.common.util.CollectionUtils;
import com.google.android.gms.common.util.VisibleForTesting;
import com.google.android.gms.measurement.AppMeasurement;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.concurrent.atomic.AtomicReference;

/* loaded from: classes.dex */
public final class zzhk extends zzhh {
    @VisibleForTesting
    protected zzid zzanp;
    private AppMeasurement.EventInterceptor zzanq;
    private final Set<AppMeasurement.OnEventListener> zzanr;
    private boolean zzans;
    private final AtomicReference<String> zzant;
    @VisibleForTesting
    protected boolean zzanu;

    /* JADX INFO: Access modifiers changed from: protected */
    public zzhk(zzgl zzglVar) {
        super(zzglVar);
        this.zzanr = new CopyOnWriteArraySet();
        this.zzanu = true;
        this.zzant = new AtomicReference<>();
    }

    private final void zza(AppMeasurement.ConditionalUserProperty conditionalUserProperty) {
        long currentTimeMillis = zzbt().currentTimeMillis();
        Preconditions.checkNotNull(conditionalUserProperty);
        Preconditions.checkNotEmpty(conditionalUserProperty.mName);
        Preconditions.checkNotEmpty(conditionalUserProperty.mOrigin);
        Preconditions.checkNotNull(conditionalUserProperty.mValue);
        conditionalUserProperty.mCreationTimestamp = currentTimeMillis;
        String str = conditionalUserProperty.mName;
        Object obj = conditionalUserProperty.mValue;
        if (zzgb().zzcf(str) != 0) {
            zzge().zzim().zzg("Invalid conditional user property name", zzga().zzbl(str));
        } else if (zzgb().zzi(str, obj) != 0) {
            zzge().zzim().zze("Invalid conditional user property value", zzga().zzbl(str), obj);
        } else {
            Object zzj = zzgb().zzj(str, obj);
            if (zzj == null) {
                zzge().zzim().zze("Unable to normalize conditional user property value", zzga().zzbl(str), obj);
                return;
            }
            conditionalUserProperty.mValue = zzj;
            long j = conditionalUserProperty.mTriggerTimeout;
            if (!TextUtils.isEmpty(conditionalUserProperty.mTriggerEventName) && (j > 15552000000L || j < 1)) {
                zzge().zzim().zze("Invalid conditional user property timeout", zzga().zzbl(str), Long.valueOf(j));
                return;
            }
            long j2 = conditionalUserProperty.mTimeToLive;
            if (j2 > 15552000000L || j2 < 1) {
                zzge().zzim().zze("Invalid conditional user property time to live", zzga().zzbl(str), Long.valueOf(j2));
            } else {
                zzgd().zzc(new zzhr(this, conditionalUserProperty));
            }
        }
    }

    private final void zza(String str, String str2, long j, Bundle bundle, boolean z, boolean z2, boolean z3, String str3) {
        Bundle bundle2;
        if (bundle == null) {
            bundle2 = new Bundle();
        } else {
            Bundle bundle3 = new Bundle(bundle);
            for (String str4 : bundle3.keySet()) {
                Object obj = bundle3.get(str4);
                if (obj instanceof Bundle) {
                    bundle3.putBundle(str4, new Bundle((Bundle) obj));
                } else {
                    int i = 0;
                    if (obj instanceof Parcelable[]) {
                        Parcelable[] parcelableArr = (Parcelable[]) obj;
                        while (i < parcelableArr.length) {
                            if (parcelableArr[i] instanceof Bundle) {
                                parcelableArr[i] = new Bundle((Bundle) parcelableArr[i]);
                            }
                            i++;
                        }
                    } else if (obj instanceof ArrayList) {
                        ArrayList arrayList = (ArrayList) obj;
                        while (i < arrayList.size()) {
                            Object obj2 = arrayList.get(i);
                            if (obj2 instanceof Bundle) {
                                arrayList.set(i, new Bundle((Bundle) obj2));
                            }
                            i++;
                        }
                    }
                }
            }
            bundle2 = bundle3;
        }
        zzgd().zzc(new zzic(this, str, str2, j, bundle2, z, z2, z3, str3));
    }

    private final void zza(String str, String str2, long j, Object obj) {
        zzgd().zzc(new zzhm(this, str, str2, obj, j));
    }

    private final void zza(String str, String str2, Bundle bundle, boolean z, boolean z2, boolean z3, String str3) {
        zza(str, str2, zzbt().currentTimeMillis(), bundle, true, z2, z3, null);
    }

    /* JADX INFO: Access modifiers changed from: private */
    @WorkerThread
    public final void zza(String str, String str2, Object obj, long j) {
        Preconditions.checkNotEmpty(str);
        Preconditions.checkNotEmpty(str2);
        zzab();
        zzch();
        if (!this.zzacw.isEnabled()) {
            zzge().zzis().log("User property not set since app measurement is disabled");
        } else if (this.zzacw.zzjv()) {
            zzge().zzis().zze("Setting user property (FE)", zzga().zzbj(str2), obj);
            zzfx().zzb(new zzjx(str2, j, obj, str));
        }
    }

    private final void zza(String str, String str2, String str3, Bundle bundle) {
        long currentTimeMillis = zzbt().currentTimeMillis();
        Preconditions.checkNotEmpty(str2);
        AppMeasurement.ConditionalUserProperty conditionalUserProperty = new AppMeasurement.ConditionalUserProperty();
        conditionalUserProperty.mAppId = str;
        conditionalUserProperty.mName = str2;
        conditionalUserProperty.mCreationTimestamp = currentTimeMillis;
        if (str3 != null) {
            conditionalUserProperty.mExpiredEventName = str3;
            conditionalUserProperty.mExpiredEventParams = bundle;
        }
        zzgd().zzc(new zzhs(this, conditionalUserProperty));
    }

    @VisibleForTesting
    private final Map<String, Object> zzb(String str, String str2, String str3, boolean z) {
        zzfi zzip;
        String str4;
        if (zzgd().zzjk()) {
            zzip = zzge().zzim();
            str4 = "Cannot get user properties from analytics worker thread";
        } else {
            zzgd();
            if (zzgg.isMainThread()) {
                zzip = zzge().zzim();
                str4 = "Cannot get user properties from main thread";
            } else {
                AtomicReference atomicReference = new AtomicReference();
                synchronized (atomicReference) {
                    this.zzacw.zzgd().zzc(new zzhu(this, atomicReference, str, str2, str3, z));
                    try {
                        atomicReference.wait(5000L);
                    } catch (InterruptedException e) {
                        zzge().zzip().zzg("Interrupted waiting for get user properties", e);
                    }
                }
                List<zzjx> list = (List) atomicReference.get();
                if (list != null) {
                    ArrayMap arrayMap = new ArrayMap(list.size());
                    for (zzjx zzjxVar : list) {
                        arrayMap.put(zzjxVar.name, zzjxVar.getValue());
                    }
                    return arrayMap;
                }
                zzip = zzge().zzip();
                str4 = "Timed out waiting for get user properties";
            }
        }
        zzip.log(str4);
        return Collections.emptyMap();
    }

    /* JADX INFO: Access modifiers changed from: private */
    @WorkerThread
    public final void zzb(AppMeasurement.ConditionalUserProperty conditionalUserProperty) {
        zzab();
        zzch();
        Preconditions.checkNotNull(conditionalUserProperty);
        Preconditions.checkNotEmpty(conditionalUserProperty.mName);
        Preconditions.checkNotEmpty(conditionalUserProperty.mOrigin);
        Preconditions.checkNotNull(conditionalUserProperty.mValue);
        if (!this.zzacw.isEnabled()) {
            zzge().zzis().log("Conditional property not sent since Firebase Analytics is disabled");
            return;
        }
        zzjx zzjxVar = new zzjx(conditionalUserProperty.mName, conditionalUserProperty.mTriggeredTimestamp, conditionalUserProperty.mValue, conditionalUserProperty.mOrigin);
        try {
            zzeu zza = zzgb().zza(conditionalUserProperty.mTriggeredEventName, conditionalUserProperty.mTriggeredEventParams, conditionalUserProperty.mOrigin, 0L, true, false);
            zzfx().zzd(new zzed(conditionalUserProperty.mAppId, conditionalUserProperty.mOrigin, zzjxVar, conditionalUserProperty.mCreationTimestamp, false, conditionalUserProperty.mTriggerEventName, zzgb().zza(conditionalUserProperty.mTimedOutEventName, conditionalUserProperty.mTimedOutEventParams, conditionalUserProperty.mOrigin, 0L, true, false), conditionalUserProperty.mTriggerTimeout, zza, conditionalUserProperty.mTimeToLive, zzgb().zza(conditionalUserProperty.mExpiredEventName, conditionalUserProperty.mExpiredEventParams, conditionalUserProperty.mOrigin, 0L, true, false)));
        } catch (IllegalArgumentException unused) {
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    @WorkerThread
    public final void zzb(String str, String str2, long j, Bundle bundle, boolean z, boolean z2, boolean z3, String str3) {
        List<String> list;
        int i;
        int i2;
        long j2;
        Bundle bundle2;
        Preconditions.checkNotEmpty(str);
        Preconditions.checkNotEmpty(str2);
        Preconditions.checkNotNull(bundle);
        zzab();
        zzch();
        if (!this.zzacw.isEnabled()) {
            zzge().zzis().log("Event not sent since app measurement is disabled");
            return;
        }
        zzie zzieVar = null;
        if (!this.zzans) {
            this.zzans = true;
            try {
                try {
                    Class.forName("com.google.android.gms.tagmanager.TagManagerService").getDeclaredMethod("initialize", Context.class).invoke(null, getContext());
                } catch (Exception e) {
                    zzge().zzip().zzg("Failed to invoke Tag Manager's initialize() method", e);
                }
            } catch (ClassNotFoundException unused) {
                zzge().zzir().log("Tag Manager is not found and thus will not be used");
            }
        }
        if (z3 && !"_iap".equals(str2)) {
            zzka zzgb = this.zzacw.zzgb();
            int i3 = 2;
            if (zzgb.zzq(NotificationCompat.CATEGORY_EVENT, str2)) {
                if (!zzgb.zza(NotificationCompat.CATEGORY_EVENT, AppMeasurement.Event.zzacx, str2)) {
                    i3 = 13;
                } else if (zzgb.zza(NotificationCompat.CATEGORY_EVENT, 40, str2)) {
                    i3 = 0;
                }
            }
            if (i3 != 0) {
                zzge().zzio().zzg("Invalid public event name. Event will not be logged (FE)", zzga().zzbj(str2));
                this.zzacw.zzgb();
                this.zzacw.zzgb().zza(i3, "_ev", zzka.zza(str2, 40, true), str2 != null ? str2.length() : 0);
                return;
            }
        }
        zzie zzkc = zzfy().zzkc();
        if (zzkc != null && !bundle.containsKey("_sc")) {
            zzkc.zzaok = true;
        }
        zzif.zza(zzkc, bundle, z && z3);
        boolean equals = "am".equals(str);
        boolean zzci = zzka.zzci(str2);
        if (z && this.zzanq != null && !zzci && !equals) {
            zzge().zzis().zze("Passing event to registered event handler (FE)", zzga().zzbj(str2), zzga().zzb(bundle));
            this.zzanq.interceptEvent(str, str2, bundle, j);
        } else if (this.zzacw.zzjv()) {
            int zzcd = zzgb().zzcd(str2);
            if (zzcd != 0) {
                zzge().zzio().zzg("Invalid event name. Event will not be logged (FE)", zzga().zzbj(str2));
                zzgb();
                this.zzacw.zzgb().zza(str3, zzcd, "_ev", zzka.zza(str2, 40, true), str2 != null ? str2.length() : 0);
                return;
            }
            List<String> listOf = CollectionUtils.listOf((Object[]) new String[]{"_o", "_sn", "_sc", "_si"});
            Bundle zza = zzgb().zza(str2, bundle, listOf, z3, true);
            if (zza != null && zza.containsKey("_sc") && zza.containsKey("_si")) {
                zzieVar = new zzie(zza.getString("_sn"), zza.getString("_sc"), Long.valueOf(zza.getLong("_si")).longValue());
            }
            if (zzieVar == null) {
                zzieVar = zzkc;
            }
            ArrayList arrayList = new ArrayList();
            arrayList.add(zza);
            long nextLong = zzgb().zzlc().nextLong();
            String[] strArr = (String[]) zza.keySet().toArray(new String[bundle.size()]);
            Arrays.sort(strArr);
            int length = strArr.length;
            int i4 = 0;
            int i5 = 0;
            while (i5 < length) {
                String str4 = strArr[i5];
                Object obj = zza.get(str4);
                zzgb();
                Bundle[] zze = zzka.zze(obj);
                if (zze != null) {
                    int i6 = i4;
                    zza.putInt(str4, zze.length);
                    int i7 = i5;
                    int i8 = 0;
                    while (i8 < zze.length) {
                        Bundle bundle3 = zze[i8];
                        int i9 = i8;
                        zzif.zza(zzieVar, bundle3, true);
                        long j3 = nextLong;
                        Bundle zza2 = zzgb().zza("_ep", bundle3, listOf, z3, false);
                        zza2.putString("_en", str2);
                        zza2.putLong("_eid", j3);
                        zza2.putString("_gn", str4);
                        zza2.putInt("_ll", zze.length);
                        zza2.putInt("_i", i9);
                        arrayList.add(zza2);
                        i8 = i9 + 1;
                        zza = zza;
                        nextLong = j3;
                        length = length;
                        i7 = i7;
                        i6 = i6;
                        listOf = listOf;
                    }
                    list = listOf;
                    i2 = length;
                    j2 = nextLong;
                    bundle2 = zza;
                    int i10 = i6;
                    i = i7;
                    i4 = zze.length + i10;
                } else {
                    list = listOf;
                    i = i5;
                    i2 = length;
                    j2 = nextLong;
                    bundle2 = zza;
                }
                i5 = i + 1;
                zza = bundle2;
                nextLong = j2;
                length = i2;
                listOf = list;
            }
            int i11 = i4;
            long j4 = nextLong;
            Bundle bundle4 = zza;
            if (i11 != 0) {
                bundle4.putLong("_eid", j4);
                bundle4.putInt("_epc", i11);
            }
            int i12 = 0;
            while (i12 < arrayList.size()) {
                Bundle bundle5 = (Bundle) arrayList.get(i12);
                String str5 = i12 != 0 ? "_ep" : str2;
                bundle5.putString("_o", str);
                if (z2) {
                    bundle5 = zzgb().zzd(bundle5);
                }
                Bundle bundle6 = bundle5;
                zzge().zzis().zze("Logging event (FE)", zzga().zzbj(str2), zzga().zzb(bundle6));
                zzfx().zzb(new zzeu(str5, new zzer(bundle6), str, j), str3);
                if (!equals) {
                    for (AppMeasurement.OnEventListener onEventListener : this.zzanr) {
                        onEventListener.onEvent(str, str2, new Bundle(bundle6), j);
                    }
                }
                i12++;
            }
            if (zzfy().zzkc() == null || !AppMeasurement.Event.APP_EXCEPTION.equals(str2)) {
                return;
            }
            zzgc().zzl(true);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    @WorkerThread
    public final void zzc(AppMeasurement.ConditionalUserProperty conditionalUserProperty) {
        zzab();
        zzch();
        Preconditions.checkNotNull(conditionalUserProperty);
        Preconditions.checkNotEmpty(conditionalUserProperty.mName);
        if (!this.zzacw.isEnabled()) {
            zzge().zzis().log("Conditional property not cleared since Firebase Analytics is disabled");
            return;
        }
        try {
            zzfx().zzd(new zzed(conditionalUserProperty.mAppId, conditionalUserProperty.mOrigin, new zzjx(conditionalUserProperty.mName, 0L, null, null), conditionalUserProperty.mCreationTimestamp, conditionalUserProperty.mActive, conditionalUserProperty.mTriggerEventName, null, conditionalUserProperty.mTriggerTimeout, null, conditionalUserProperty.mTimeToLive, zzgb().zza(conditionalUserProperty.mExpiredEventName, conditionalUserProperty.mExpiredEventParams, conditionalUserProperty.mOrigin, conditionalUserProperty.mCreationTimestamp, true, false)));
        } catch (IllegalArgumentException unused) {
        }
    }

    @VisibleForTesting
    private final List<AppMeasurement.ConditionalUserProperty> zzf(String str, String str2, String str3) {
        zzfi zzim;
        String str4;
        if (zzgd().zzjk()) {
            zzim = zzge().zzim();
            str4 = "Cannot get conditional user properties from analytics worker thread";
        } else {
            zzgd();
            if (!zzgg.isMainThread()) {
                AtomicReference atomicReference = new AtomicReference();
                synchronized (atomicReference) {
                    this.zzacw.zzgd().zzc(new zzht(this, atomicReference, str, str2, str3));
                    try {
                        atomicReference.wait(5000L);
                    } catch (InterruptedException e) {
                        zzge().zzip().zze("Interrupted waiting for get conditional user properties", str, e);
                    }
                }
                List<zzed> list = (List) atomicReference.get();
                if (list == null) {
                    zzge().zzip().zzg("Timed out waiting for get conditional user properties", str);
                    return Collections.emptyList();
                }
                ArrayList arrayList = new ArrayList(list.size());
                for (zzed zzedVar : list) {
                    AppMeasurement.ConditionalUserProperty conditionalUserProperty = new AppMeasurement.ConditionalUserProperty();
                    conditionalUserProperty.mAppId = str;
                    conditionalUserProperty.mOrigin = str2;
                    conditionalUserProperty.mCreationTimestamp = zzedVar.creationTimestamp;
                    conditionalUserProperty.mName = zzedVar.zzaep.name;
                    conditionalUserProperty.mValue = zzedVar.zzaep.getValue();
                    conditionalUserProperty.mActive = zzedVar.active;
                    conditionalUserProperty.mTriggerEventName = zzedVar.triggerEventName;
                    if (zzedVar.zzaeq != null) {
                        conditionalUserProperty.mTimedOutEventName = zzedVar.zzaeq.name;
                        if (zzedVar.zzaeq.zzafq != null) {
                            conditionalUserProperty.mTimedOutEventParams = zzedVar.zzaeq.zzafq.zzif();
                        }
                    }
                    conditionalUserProperty.mTriggerTimeout = zzedVar.triggerTimeout;
                    if (zzedVar.zzaer != null) {
                        conditionalUserProperty.mTriggeredEventName = zzedVar.zzaer.name;
                        if (zzedVar.zzaer.zzafq != null) {
                            conditionalUserProperty.mTriggeredEventParams = zzedVar.zzaer.zzafq.zzif();
                        }
                    }
                    conditionalUserProperty.mTriggeredTimestamp = zzedVar.zzaep.zzaqz;
                    conditionalUserProperty.mTimeToLive = zzedVar.timeToLive;
                    if (zzedVar.zzaes != null) {
                        conditionalUserProperty.mExpiredEventName = zzedVar.zzaes.name;
                        if (zzedVar.zzaes.zzafq != null) {
                            conditionalUserProperty.mExpiredEventParams = zzedVar.zzaes.zzafq.zzif();
                        }
                    }
                    arrayList.add(conditionalUserProperty);
                }
                return arrayList;
            }
            zzim = zzge().zzim();
            str4 = "Cannot get conditional user properties from main thread";
        }
        zzim.log(str4);
        return Collections.emptyList();
    }

    /* JADX INFO: Access modifiers changed from: private */
    @WorkerThread
    public final void zzi(boolean z) {
        zzab();
        zzch();
        zzge().zzis().zzg("Setting app measurement enabled (FE)", Boolean.valueOf(z));
        zzgf().setMeasurementEnabled(z);
        if (!zzgg().zzaz(zzfv().zzah())) {
            zzfx().zzke();
        } else if (!this.zzacw.isEnabled() || !this.zzanu) {
            zzfx().zzke();
        } else {
            zzge().zzis().log("Recording app launch after enabling measurement for the first time (FE)");
            zzkb();
        }
    }

    public final void clearConditionalUserProperty(String str, String str2, Bundle bundle) {
        zza((String) null, str, str2, bundle);
    }

    public final void clearConditionalUserPropertyAs(String str, String str2, String str3, Bundle bundle) {
        Preconditions.checkNotEmpty(str);
        zzfr();
        zza(str, str2, str3, bundle);
    }

    public final Task<String> getAppInstanceId() {
        try {
            String zzja = zzgf().zzja();
            return zzja != null ? Tasks.forResult(zzja) : Tasks.call(zzgd().zzjl(), new zzho(this));
        } catch (Exception e) {
            zzge().zzip().log("Failed to schedule task for getAppInstanceId");
            return Tasks.forException(e);
        }
    }

    public final List<AppMeasurement.ConditionalUserProperty> getConditionalUserProperties(String str, String str2) {
        return zzf(null, str, str2);
    }

    public final List<AppMeasurement.ConditionalUserProperty> getConditionalUserPropertiesAs(String str, String str2, String str3) {
        Preconditions.checkNotEmpty(str);
        zzfr();
        return zzf(str, str2, str3);
    }

    @Override // com.google.android.gms.internal.measurement.zzhg, com.google.android.gms.internal.measurement.zzec
    public final /* bridge */ /* synthetic */ Context getContext() {
        return super.getContext();
    }

    public final Map<String, Object> getUserProperties(String str, String str2, boolean z) {
        return zzb(null, str, str2, z);
    }

    public final Map<String, Object> getUserPropertiesAs(String str, String str2, String str3, boolean z) {
        Preconditions.checkNotEmpty(str);
        zzfr();
        return zzb(str, str2, str3, z);
    }

    public final void logEvent(String str, String str2, Bundle bundle) {
        zza(str, str2, bundle, true, this.zzanq == null || zzka.zzci(str2), false, null);
    }

    public final void registerOnMeasurementEventListener(AppMeasurement.OnEventListener onEventListener) {
        zzch();
        Preconditions.checkNotNull(onEventListener);
        if (this.zzanr.add(onEventListener)) {
            return;
        }
        zzge().zzip().log("OnEventListener already registered");
    }

    public final void resetAnalyticsData() {
        zzgd().zzc(new zzhq(this, zzbt().currentTimeMillis()));
    }

    public final void setConditionalUserProperty(AppMeasurement.ConditionalUserProperty conditionalUserProperty) {
        Preconditions.checkNotNull(conditionalUserProperty);
        AppMeasurement.ConditionalUserProperty conditionalUserProperty2 = new AppMeasurement.ConditionalUserProperty(conditionalUserProperty);
        if (!TextUtils.isEmpty(conditionalUserProperty2.mAppId)) {
            zzge().zzip().log("Package name should be null when calling setConditionalUserProperty");
        }
        conditionalUserProperty2.mAppId = null;
        zza(conditionalUserProperty2);
    }

    public final void setConditionalUserPropertyAs(AppMeasurement.ConditionalUserProperty conditionalUserProperty) {
        Preconditions.checkNotNull(conditionalUserProperty);
        Preconditions.checkNotEmpty(conditionalUserProperty.mAppId);
        zzfr();
        zza(new AppMeasurement.ConditionalUserProperty(conditionalUserProperty));
    }

    @WorkerThread
    public final void setEventInterceptor(AppMeasurement.EventInterceptor eventInterceptor) {
        zzab();
        zzch();
        if (eventInterceptor != null && eventInterceptor != this.zzanq) {
            Preconditions.checkState(this.zzanq == null, "EventInterceptor already set.");
        }
        this.zzanq = eventInterceptor;
    }

    public final void setMeasurementEnabled(boolean z) {
        zzch();
        zzgd().zzc(new zzhz(this, z));
    }

    public final void setMinimumSessionDuration(long j) {
        zzgd().zzc(new zzia(this, j));
    }

    public final void setSessionTimeoutDuration(long j) {
        zzgd().zzc(new zzib(this, j));
    }

    public final void setUserProperty(String str, String str2, Object obj) {
        Preconditions.checkNotEmpty(str);
        long currentTimeMillis = zzbt().currentTimeMillis();
        int zzcf = zzgb().zzcf(str2);
        if (zzcf != 0) {
            zzgb();
            this.zzacw.zzgb().zza(zzcf, "_ev", zzka.zza(str2, 24, true), str2 != null ? str2.length() : 0);
        } else if (obj == null) {
            zza(str, str2, currentTimeMillis, (Object) null);
        } else {
            int zzi = zzgb().zzi(str2, obj);
            if (zzi == 0) {
                Object zzj = zzgb().zzj(str2, obj);
                if (zzj != null) {
                    zza(str, str2, currentTimeMillis, zzj);
                    return;
                }
                return;
            }
            zzgb();
            String zza = zzka.zza(str2, 24, true);
            if ((obj instanceof String) || (obj instanceof CharSequence)) {
                r1 = String.valueOf(obj).length();
            }
            this.zzacw.zzgb().zza(zzi, "_ev", zza, r1);
        }
    }

    public final void unregisterOnMeasurementEventListener(AppMeasurement.OnEventListener onEventListener) {
        zzch();
        Preconditions.checkNotNull(onEventListener);
        if (this.zzanr.remove(onEventListener)) {
            return;
        }
        zzge().zzip().log("OnEventListener had not been registered");
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @WorkerThread
    public final void zza(String str, String str2, Bundle bundle) {
        zzab();
        zzb(str, str2, zzbt().currentTimeMillis(), bundle, true, this.zzanq == null || zzka.zzci(str2), false, null);
    }

    public final void zza(String str, String str2, Bundle bundle, long j) {
        zza(str, str2, j, bundle, false, true, true, null);
    }

    public final void zza(String str, String str2, Bundle bundle, boolean z) {
        zza(str, str2, bundle, true, this.zzanq == null || zzka.zzci(str2), true, null);
    }

    @Override // com.google.android.gms.internal.measurement.zzhg
    public final /* bridge */ /* synthetic */ void zzab() {
        super.zzab();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Nullable
    public final String zzae(long j) {
        AtomicReference atomicReference = new AtomicReference();
        synchronized (atomicReference) {
            zzgd().zzc(new zzhp(this, atomicReference));
            try {
                atomicReference.wait(j);
            } catch (InterruptedException unused) {
                zzge().zzip().log("Interrupted waiting for app instance id");
                return null;
            }
        }
        return (String) atomicReference.get();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public final void zzbr(@Nullable String str) {
        this.zzant.set(str);
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

    public final String zzhm() {
        AtomicReference atomicReference = new AtomicReference();
        return (String) zzgd().zza(atomicReference, 15000L, "String test flag value", new zzhv(this, atomicReference));
    }

    public final List<zzjx> zzj(boolean z) {
        zzfi zzip;
        String str;
        zzch();
        zzge().zzis().log("Fetching user attributes (FE)");
        if (zzgd().zzjk()) {
            zzip = zzge().zzim();
            str = "Cannot get all user properties from analytics worker thread";
        } else {
            zzgd();
            if (zzgg.isMainThread()) {
                zzip = zzge().zzim();
                str = "Cannot get all user properties from main thread";
            } else {
                AtomicReference atomicReference = new AtomicReference();
                synchronized (atomicReference) {
                    this.zzacw.zzgd().zzc(new zzhn(this, atomicReference, z));
                    try {
                        atomicReference.wait(5000L);
                    } catch (InterruptedException e) {
                        zzge().zzip().zzg("Interrupted waiting for get user properties", e);
                    }
                }
                List<zzjx> list = (List) atomicReference.get();
                if (list != null) {
                    return list;
                }
                zzip = zzge().zzip();
                str = "Timed out waiting for get user properties";
            }
        }
        zzip.log(str);
        return Collections.emptyList();
    }

    @Nullable
    public final String zzja() {
        return this.zzant.get();
    }

    public final Boolean zzjx() {
        AtomicReference atomicReference = new AtomicReference();
        return (Boolean) zzgd().zza(atomicReference, 15000L, "boolean test flag value", new zzhl(this, atomicReference));
    }

    public final Long zzjy() {
        AtomicReference atomicReference = new AtomicReference();
        return (Long) zzgd().zza(atomicReference, 15000L, "long test flag value", new zzhw(this, atomicReference));
    }

    public final Integer zzjz() {
        AtomicReference atomicReference = new AtomicReference();
        return (Integer) zzgd().zza(atomicReference, 15000L, "int test flag value", new zzhx(this, atomicReference));
    }

    public final Double zzka() {
        AtomicReference atomicReference = new AtomicReference();
        return (Double) zzgd().zza(atomicReference, 15000L, "double test flag value", new zzhy(this, atomicReference));
    }

    @WorkerThread
    public final void zzkb() {
        zzab();
        zzch();
        if (this.zzacw.zzjv()) {
            zzfx().zzkb();
            this.zzanu = false;
            String zzjd = zzgf().zzjd();
            if (TextUtils.isEmpty(zzjd)) {
                return;
            }
            zzfw().zzch();
            if (zzjd.equals(Build.VERSION.RELEASE)) {
                return;
            }
            Bundle bundle = new Bundle();
            bundle.putString("_po", zzjd);
            logEvent("auto", "_ou", bundle);
        }
    }
}
