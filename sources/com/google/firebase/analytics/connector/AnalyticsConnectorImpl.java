package com.google.firebase.analytics.connector;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresPermission;
import androidx.annotation.Size;
import androidx.annotation.WorkerThread;
import com.google.android.gms.common.annotation.KeepForSdk;
import com.google.android.gms.common.internal.Preconditions;
import com.google.android.gms.common.util.VisibleForTesting;
import com.google.android.gms.measurement.AppMeasurement;
import com.google.firebase.FirebaseApp;
import com.google.firebase.analytics.connector.AnalyticsConnector;
import com.google.firebase.analytics.connector.internal.Adapter;
import com.google.firebase.analytics.connector.internal.zzb;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/* loaded from: classes.dex */
public class AnalyticsConnectorImpl implements AnalyticsConnector {
    private static volatile AnalyticsConnector zzbof;
    private final AppMeasurement zzboe;
    @VisibleForTesting
    final Map<String, Adapter> zzbog;

    private AnalyticsConnectorImpl(AppMeasurement appMeasurement) {
        Preconditions.checkNotNull(appMeasurement);
        this.zzboe = appMeasurement;
        this.zzbog = new ConcurrentHashMap();
    }

    @KeepForSdk
    public static AnalyticsConnector getInstance() {
        return getInstance(FirebaseApp.getInstance());
    }

    @RequiresPermission(allOf = {"android.permission.INTERNET", "android.permission.ACCESS_NETWORK_STATE", "android.permission.WAKE_LOCK"})
    @KeepForSdk
    public static AnalyticsConnector getInstance(Context context) {
        Preconditions.checkNotNull(context);
        Preconditions.checkNotNull(context.getApplicationContext());
        if (zzbof == null) {
            synchronized (AnalyticsConnector.class) {
                if (zzbof == null) {
                    zzbof = new AnalyticsConnectorImpl(AppMeasurement.getInstance(context));
                }
            }
        }
        return zzbof;
    }

    @KeepForSdk
    public static AnalyticsConnector getInstance(FirebaseApp firebaseApp) {
        return (AnalyticsConnector) firebaseApp.get(AnalyticsConnector.class);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public final boolean zzfc(@NonNull String str) {
        return (str.isEmpty() || !this.zzbog.containsKey(str) || this.zzbog.get(str) == null) ? false : true;
    }

    @Override // com.google.firebase.analytics.connector.AnalyticsConnector
    @KeepForSdk
    public void clearConditionalUserProperty(@NonNull @Size(max = 24, min = 1) String str, @Nullable String str2, @Nullable Bundle bundle) {
        if (str2 == null || zzb.zza(str2, bundle)) {
            this.zzboe.clearConditionalUserProperty(str, str2, bundle);
        } else {
            Log.d("FA-C", "Event or Params not allowed");
        }
    }

    @VisibleForTesting
    protected Adapter createAdapter(@NonNull String str, AppMeasurement appMeasurement, AnalyticsConnector.AnalyticsConnectorListener analyticsConnectorListener) {
        char c;
        String str2;
        String str3;
        int hashCode = str.hashCode();
        if (hashCode == 3308) {
            if (str.equals("gs")) {
                c = 3;
            }
            c = 65535;
        } else if (hashCode == 101200) {
            if (str.equals(AppMeasurement.FCM_ORIGIN)) {
                c = 0;
            }
            c = 65535;
        } else if (hashCode == 101230) {
            if (str.equals("fdl")) {
                c = 1;
            }
            c = 65535;
        } else if (hashCode != 101655) {
            if (hashCode == 94921639 && str.equals(AppMeasurement.CRASH_ORIGIN)) {
                c = 4;
            }
            c = 65535;
        } else {
            if (str.equals("frc")) {
                c = 2;
            }
            c = 65535;
        }
        switch (c) {
            case 0:
                str2 = "FA-C";
                str3 = "FCM Adapter not implemented";
                break;
            case 1:
                str2 = "FA-C";
                str3 = "FDL Adapter not implemented";
                break;
            case 2:
                str2 = "FA-C";
                str3 = "FRC Adapter not implemented";
                break;
            case 3:
                str2 = "FA-C";
                str3 = "Search Adapter not implemented";
                break;
            case 4:
                str2 = "FA-C";
                str3 = "Crash Adapter not implemented";
                break;
            default:
                String valueOf = String.valueOf(str);
                Log.d("FA-C", valueOf.length() != 0 ? "Adapter not defined for ".concat(valueOf) : new String("Adapter not defined for "));
                return null;
        }
        Log.d(str2, str3);
        return null;
    }

    @Override // com.google.firebase.analytics.connector.AnalyticsConnector
    @KeepForSdk
    @WorkerThread
    public List<AnalyticsConnector.ConditionalUserProperty> getConditionalUserProperties(@NonNull String str, @Nullable @Size(max = 23, min = 1) String str2) {
        ArrayList arrayList = new ArrayList();
        for (AppMeasurement.ConditionalUserProperty conditionalUserProperty : this.zzboe.getConditionalUserProperties(str, str2)) {
            arrayList.add(zzb.zzd(conditionalUserProperty));
        }
        return arrayList;
    }

    @Override // com.google.firebase.analytics.connector.AnalyticsConnector
    @KeepForSdk
    @WorkerThread
    public int getMaxUserProperties(@NonNull @Size(min = 1) String str) {
        return this.zzboe.getMaxUserProperties(str);
    }

    @Override // com.google.firebase.analytics.connector.AnalyticsConnector
    @KeepForSdk
    @WorkerThread
    public Map<String, Object> getUserProperties(boolean z) {
        return this.zzboe.getUserProperties(z);
    }

    @Override // com.google.firebase.analytics.connector.AnalyticsConnector
    @KeepForSdk
    @WorkerThread
    public void logEvent(@NonNull String str, @NonNull String str2, Bundle bundle) {
        if (bundle == null) {
            bundle = new Bundle();
        }
        if (!zzb.zzfd(str)) {
            String valueOf = String.valueOf(str);
            Log.d("FA-C", valueOf.length() != 0 ? "Origin not allowed : ".concat(valueOf) : new String("Origin not allowed : "));
        } else if (!zzb.zza(str2, bundle)) {
            Log.d("FA-C", "Event or Params not allowed");
        } else if (zzb.zzb(str, str2, bundle)) {
            this.zzboe.logEventInternal(str, str2, bundle);
        } else {
            Log.d("FA-C", "Campaign events not allowed");
        }
    }

    @Override // com.google.firebase.analytics.connector.AnalyticsConnector
    @KeepForSdk
    @WorkerThread
    public AnalyticsConnector.AnalyticsConnectorHandle registerAnalyticsConnectorListener(@NonNull String str, AnalyticsConnector.AnalyticsConnectorListener analyticsConnectorListener) {
        String str2;
        String str3;
        Preconditions.checkNotNull(analyticsConnectorListener);
        if (!zzb.zzfd(str)) {
            str2 = "FA-C";
            str3 = "Registering with non allowed origin";
        } else if (!zzfc(str)) {
            Adapter createAdapter = createAdapter(str, this.zzboe, analyticsConnectorListener);
            if (createAdapter != null) {
                this.zzbog.put(str, createAdapter);
                return new zza(this, str);
            }
            return null;
        } else {
            str2 = "FA-C";
            str3 = "Registering duplicate listener";
        }
        Log.d(str2, str3);
        return null;
    }

    @Override // com.google.firebase.analytics.connector.AnalyticsConnector
    @KeepForSdk
    public void setConditionalUserProperty(@NonNull AnalyticsConnector.ConditionalUserProperty conditionalUserProperty) {
        if (zzb.zza(conditionalUserProperty)) {
            this.zzboe.setConditionalUserProperty(zzb.zzb(conditionalUserProperty));
        } else {
            Log.d("FA-C", "Conditional user property has invalid event or params");
        }
    }

    @Override // com.google.firebase.analytics.connector.AnalyticsConnector
    @KeepForSdk
    public void setUserProperty(@NonNull String str, @NonNull String str2, Object obj) {
        if (!zzb.zzfd(str)) {
            String valueOf = String.valueOf(str);
            Log.d("FA-C", valueOf.length() != 0 ? "Origin not allowed : ".concat(valueOf) : new String("Origin not allowed : "));
        } else if (!zzb.zzfe(str2)) {
            String valueOf2 = String.valueOf(str2);
            Log.d("FA-C", valueOf2.length() != 0 ? "User Property not allowed : ".concat(valueOf2) : new String("User Property not allowed : "));
        } else if ((!str2.equals("_ce1") && !str2.equals("_ce2")) || str.equals(AppMeasurement.FCM_ORIGIN) || str.equals("frc")) {
            this.zzboe.setUserPropertyInternal(str, str2, obj);
        } else {
            String valueOf3 = String.valueOf(str2);
            Log.d("FA-C", valueOf3.length() != 0 ? "User Property not allowed for this origin: ".concat(valueOf3) : new String("User Property not allowed for this origin: "));
        }
    }
}
