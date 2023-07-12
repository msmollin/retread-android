package com.google.android.gms.internal.measurement;

import android.content.ContentResolver;
import android.database.ContentObserver;
import android.database.Cursor;
import android.database.sqlite.SQLiteException;
import android.net.Uri;
import android.util.Log;
import androidx.annotation.GuardedBy;
import com.google.firebase.analytics.FirebaseAnalytics;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/* loaded from: classes.dex */
public final class zzwp {
    private static final ConcurrentHashMap<Uri, zzwp> zzbmt = new ConcurrentHashMap<>();
    private static final String[] zzbna = {"key", FirebaseAnalytics.Param.VALUE};
    private final Uri uri;
    private final ContentResolver zzbmu;
    private volatile Map<String, String> zzbmx;
    private final Object zzbmw = new Object();
    private final Object zzbmy = new Object();
    @GuardedBy("listenersLock")
    private final List<zzwr> zzbmz = new ArrayList();
    private final ContentObserver zzbmv = new zzwq(this, null);

    private zzwp(ContentResolver contentResolver, Uri uri) {
        this.zzbmu = contentResolver;
        this.uri = uri;
    }

    public static zzwp zza(ContentResolver contentResolver, Uri uri) {
        zzwp zzwpVar = zzbmt.get(uri);
        if (zzwpVar == null) {
            zzwp zzwpVar2 = new zzwp(contentResolver, uri);
            zzwp putIfAbsent = zzbmt.putIfAbsent(uri, zzwpVar2);
            if (putIfAbsent == null) {
                zzwpVar2.zzbmu.registerContentObserver(zzwpVar2.uri, false, zzwpVar2.zzbmv);
                return zzwpVar2;
            }
            return putIfAbsent;
        }
        return zzwpVar;
    }

    private final Map<String, String> zzrv() {
        try {
            HashMap hashMap = new HashMap();
            Cursor query = this.zzbmu.query(this.uri, zzbna, null, null, null);
            if (query != null) {
                while (query.moveToNext()) {
                    hashMap.put(query.getString(0), query.getString(1));
                }
                query.close();
            }
            return hashMap;
        } catch (SQLiteException | SecurityException unused) {
            Log.e("ConfigurationContentLoader", "PhenotypeFlag unable to load ContentProvider, using default values");
            return null;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public final void zzrw() {
        synchronized (this.zzbmy) {
            for (zzwr zzwrVar : this.zzbmz) {
                zzwrVar.zzrx();
            }
        }
    }

    public final Map<String, String> zzrt() {
        Map<String, String> zzrv = zzws.zzd("gms:phenotype:phenotype_flag:debug_disable_caching", false) ? zzrv() : this.zzbmx;
        if (zzrv == null) {
            synchronized (this.zzbmw) {
                zzrv = this.zzbmx;
                if (zzrv == null) {
                    zzrv = zzrv();
                    this.zzbmx = zzrv;
                }
            }
        }
        return zzrv != null ? zzrv : Collections.emptyMap();
    }

    public final void zzru() {
        synchronized (this.zzbmw) {
            this.zzbmx = null;
        }
    }
}
