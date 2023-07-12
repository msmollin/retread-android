package com.google.android.gms.internal.stable;

import android.content.ContentResolver;
import android.database.Cursor;
import android.database.SQLException;
import android.net.Uri;
import android.provider.BaseColumns;
import android.util.Log;
import com.google.firebase.analytics.FirebaseAnalytics;
import java.util.HashMap;

/* loaded from: classes.dex */
public final class zze {

    /* loaded from: classes.dex */
    public static class zza implements BaseColumns {
        private static HashMap<Uri, zzh> zzagq = new HashMap<>();

        private static zzh zza(ContentResolver contentResolver, Uri uri) {
            zzh zzhVar = zzagq.get(uri);
            if (zzhVar == null) {
                zzh zzhVar2 = new zzh();
                zzagq.put(uri, zzhVar2);
                contentResolver.registerContentObserver(uri, true, new zzf(null, zzhVar2));
                return zzhVar2;
            } else if (zzhVar.zzagu.getAndSet(false)) {
                synchronized (zzhVar) {
                    zzhVar.zzags.clear();
                    zzhVar.zzagt = new Object();
                }
                return zzhVar;
            } else {
                return zzhVar;
            }
        }

        /* JADX INFO: Access modifiers changed from: protected */
        public static String zza(ContentResolver contentResolver, Uri uri, String str) {
            zzh zza;
            String str2;
            Cursor query;
            synchronized (zza.class) {
                zza = zza(contentResolver, uri);
            }
            synchronized (zza) {
                Object obj = zza.zzagt;
                if (zza.zzags.containsKey(str)) {
                    return zza.zzags.get(str);
                }
                Cursor cursor = null;
                try {
                    try {
                        query = contentResolver.query(uri, new String[]{FirebaseAnalytics.Param.VALUE}, "name=?", new String[]{str}, null);
                    } catch (Throwable th) {
                        th = th;
                    }
                } catch (SQLException e) {
                    e = e;
                    str2 = null;
                }
                if (query != null) {
                    try {
                        try {
                        } catch (Throwable th2) {
                            th = th2;
                            cursor = query;
                            if (cursor != null) {
                                cursor.close();
                            }
                            throw th;
                        }
                    } catch (SQLException e2) {
                        e = e2;
                        str2 = null;
                    }
                    if (query.moveToFirst()) {
                        str2 = query.getString(0);
                        try {
                            zza(zza, obj, str, str2);
                            if (query != null) {
                                query.close();
                            }
                        } catch (SQLException e3) {
                            e = e3;
                            cursor = query;
                            Log.e("GoogleSettings", "Can't get key " + str + " from " + uri, e);
                            if (cursor != null) {
                                cursor.close();
                            }
                            return str2;
                        }
                        return str2;
                    }
                }
                zza(zza, obj, str, null);
                if (query != null) {
                    query.close();
                }
                return null;
            }
        }

        private static void zza(zzh zzhVar, Object obj, String str, String str2) {
            synchronized (zzhVar) {
                if (obj == zzhVar.zzagt) {
                    zzhVar.zzags.put(str, str2);
                }
            }
        }
    }
}
