package com.google.android.gms.internal.measurement;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.text.TextUtils;
import androidx.annotation.WorkerThread;
import com.google.android.gms.common.internal.Preconditions;
import com.google.android.gms.common.util.VisibleForTesting;
import com.google.android.gms.measurement.AppMeasurement;
import com.google.firebase.analytics.FirebaseAnalytics;
import java.io.File;
import java.io.IOException;
import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: classes.dex */
public final class zzei extends zzjq {
    private static final String[] zzaev = {"last_bundled_timestamp", "ALTER TABLE events ADD COLUMN last_bundled_timestamp INTEGER;", "last_sampled_complex_event_id", "ALTER TABLE events ADD COLUMN last_sampled_complex_event_id INTEGER;", "last_sampling_rate", "ALTER TABLE events ADD COLUMN last_sampling_rate INTEGER;", "last_exempt_from_sampling", "ALTER TABLE events ADD COLUMN last_exempt_from_sampling INTEGER;"};
    private static final String[] zzaew = {FirebaseAnalytics.Param.ORIGIN, "ALTER TABLE user_attributes ADD COLUMN origin TEXT;"};
    private static final String[] zzaex = {"app_version", "ALTER TABLE apps ADD COLUMN app_version TEXT;", "app_store", "ALTER TABLE apps ADD COLUMN app_store TEXT;", "gmp_version", "ALTER TABLE apps ADD COLUMN gmp_version INTEGER;", "dev_cert_hash", "ALTER TABLE apps ADD COLUMN dev_cert_hash INTEGER;", "measurement_enabled", "ALTER TABLE apps ADD COLUMN measurement_enabled INTEGER;", "last_bundle_start_timestamp", "ALTER TABLE apps ADD COLUMN last_bundle_start_timestamp INTEGER;", "day", "ALTER TABLE apps ADD COLUMN day INTEGER;", "daily_public_events_count", "ALTER TABLE apps ADD COLUMN daily_public_events_count INTEGER;", "daily_events_count", "ALTER TABLE apps ADD COLUMN daily_events_count INTEGER;", "daily_conversions_count", "ALTER TABLE apps ADD COLUMN daily_conversions_count INTEGER;", "remote_config", "ALTER TABLE apps ADD COLUMN remote_config BLOB;", "config_fetched_time", "ALTER TABLE apps ADD COLUMN config_fetched_time INTEGER;", "failed_config_fetch_time", "ALTER TABLE apps ADD COLUMN failed_config_fetch_time INTEGER;", "app_version_int", "ALTER TABLE apps ADD COLUMN app_version_int INTEGER;", "firebase_instance_id", "ALTER TABLE apps ADD COLUMN firebase_instance_id TEXT;", "daily_error_events_count", "ALTER TABLE apps ADD COLUMN daily_error_events_count INTEGER;", "daily_realtime_events_count", "ALTER TABLE apps ADD COLUMN daily_realtime_events_count INTEGER;", "health_monitor_sample", "ALTER TABLE apps ADD COLUMN health_monitor_sample TEXT;", "android_id", "ALTER TABLE apps ADD COLUMN android_id INTEGER;", "adid_reporting_enabled", "ALTER TABLE apps ADD COLUMN adid_reporting_enabled INTEGER;", "ssaid_reporting_enabled", "ALTER TABLE apps ADD COLUMN ssaid_reporting_enabled INTEGER;"};
    private static final String[] zzaey = {"realtime", "ALTER TABLE raw_events ADD COLUMN realtime INTEGER;"};
    private static final String[] zzaez = {"has_realtime", "ALTER TABLE queue ADD COLUMN has_realtime INTEGER;", "retry_count", "ALTER TABLE queue ADD COLUMN retry_count INTEGER;"};
    private static final String[] zzafa = {"previous_install_count", "ALTER TABLE app2 ADD COLUMN previous_install_count INTEGER;"};
    private final zzel zzafb;
    private final zzjm zzafc;

    /* JADX INFO: Access modifiers changed from: package-private */
    public zzei(zzjr zzjrVar) {
        super(zzjrVar);
        this.zzafc = new zzjm(zzbt());
        this.zzafb = new zzel(this, getContext(), "google_app_measurement.db");
    }

    @WorkerThread
    private final long zza(String str, String[] strArr) {
        Cursor cursor;
        Cursor cursor2 = null;
        try {
            try {
                cursor = getWritableDatabase().rawQuery(str, strArr);
                try {
                    if (cursor.moveToFirst()) {
                        long j = cursor.getLong(0);
                        if (cursor != null) {
                            cursor.close();
                        }
                        return j;
                    }
                    throw new SQLiteException("Database returned empty set");
                } catch (SQLiteException e) {
                    e = e;
                    cursor2 = cursor;
                    zzge().zzim().zze("Database error", str, e);
                    throw e;
                } catch (Throwable th) {
                    th = th;
                    if (cursor != null) {
                        cursor.close();
                    }
                    throw th;
                }
            } catch (Throwable th2) {
                th = th2;
                cursor = cursor2;
            }
        } catch (SQLiteException e2) {
            e = e2;
        }
    }

    @WorkerThread
    private final long zza(String str, String[] strArr, long j) {
        Cursor cursor = null;
        try {
            try {
                Cursor rawQuery = getWritableDatabase().rawQuery(str, strArr);
                try {
                    if (!rawQuery.moveToFirst()) {
                        if (rawQuery != null) {
                            rawQuery.close();
                        }
                        return j;
                    }
                    long j2 = rawQuery.getLong(0);
                    if (rawQuery != null) {
                        rawQuery.close();
                    }
                    return j2;
                } catch (SQLiteException e) {
                    e = e;
                    cursor = rawQuery;
                    zzge().zzim().zze("Database error", str, e);
                    throw e;
                } catch (Throwable th) {
                    th = th;
                    cursor = rawQuery;
                    if (cursor != null) {
                        cursor.close();
                    }
                    throw th;
                }
            } catch (Throwable th2) {
                th = th2;
            }
        } catch (SQLiteException e2) {
            e = e2;
        }
    }

    @VisibleForTesting
    @WorkerThread
    private final Object zza(Cursor cursor, int i) {
        int type = cursor.getType(i);
        switch (type) {
            case 0:
                zzge().zzim().log("Loaded invalid null value from database");
                return null;
            case 1:
                return Long.valueOf(cursor.getLong(i));
            case 2:
                return Double.valueOf(cursor.getDouble(i));
            case 3:
                return cursor.getString(i);
            case 4:
                zzge().zzim().log("Loaded invalid blob type value, ignoring it");
                return null;
            default:
                zzge().zzim().zzg("Loaded invalid unknown value type, ignoring it", Integer.valueOf(type));
                return null;
        }
    }

    @WorkerThread
    private static void zza(ContentValues contentValues, String str, Object obj) {
        Preconditions.checkNotEmpty(str);
        Preconditions.checkNotNull(obj);
        if (obj instanceof String) {
            contentValues.put(str, (String) obj);
        } else if (obj instanceof Long) {
            contentValues.put(str, (Long) obj);
        } else if (!(obj instanceof Double)) {
            throw new IllegalArgumentException("Invalid value type");
        } else {
            contentValues.put(str, (Double) obj);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static void zza(zzfg zzfgVar, SQLiteDatabase sQLiteDatabase) {
        if (zzfgVar == null) {
            throw new IllegalArgumentException("Monitor must not be null");
        }
        File file = new File(sQLiteDatabase.getPath());
        if (!file.setReadable(false, false)) {
            zzfgVar.zzip().log("Failed to turn off database read permission");
        }
        if (!file.setWritable(false, false)) {
            zzfgVar.zzip().log("Failed to turn off database write permission");
        }
        if (!file.setReadable(true, true)) {
            zzfgVar.zzip().log("Failed to turn on database read permission for owner");
        }
        if (file.setWritable(true, true)) {
            return;
        }
        zzfgVar.zzip().log("Failed to turn on database write permission for owner");
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @WorkerThread
    public static void zza(zzfg zzfgVar, SQLiteDatabase sQLiteDatabase, String str, String str2, String str3, String[] strArr) throws SQLiteException {
        String[] split;
        if (zzfgVar == null) {
            throw new IllegalArgumentException("Monitor must not be null");
        }
        if (!zza(zzfgVar, sQLiteDatabase, str)) {
            sQLiteDatabase.execSQL(str2);
        }
        try {
            if (zzfgVar == null) {
                throw new IllegalArgumentException("Monitor must not be null");
            }
            Set<String> zzb = zzb(sQLiteDatabase, str);
            for (String str4 : str3.split(",")) {
                if (!zzb.remove(str4)) {
                    StringBuilder sb = new StringBuilder(String.valueOf(str).length() + 35 + String.valueOf(str4).length());
                    sb.append("Table ");
                    sb.append(str);
                    sb.append(" is missing required column: ");
                    sb.append(str4);
                    throw new SQLiteException(sb.toString());
                }
            }
            if (strArr != null) {
                for (int i = 0; i < strArr.length; i += 2) {
                    if (!zzb.remove(strArr[i])) {
                        sQLiteDatabase.execSQL(strArr[i + 1]);
                    }
                }
            }
            if (zzb.isEmpty()) {
                return;
            }
            zzfgVar.zzip().zze("Table has extra columns. table, columns", str, TextUtils.join(", ", zzb));
        } catch (SQLiteException e) {
            zzfgVar.zzim().zzg("Failed to verify columns on table that was just created", str);
            throw e;
        }
    }

    @WorkerThread
    private static boolean zza(zzfg zzfgVar, SQLiteDatabase sQLiteDatabase, String str) {
        if (zzfgVar == null) {
            throw new IllegalArgumentException("Monitor must not be null");
        }
        Cursor cursor = null;
        try {
            try {
                Cursor query = sQLiteDatabase.query("SQLITE_MASTER", new String[]{"name"}, "name=?", new String[]{str}, null, null, null);
                try {
                    boolean moveToFirst = query.moveToFirst();
                    if (query != null) {
                        query.close();
                    }
                    return moveToFirst;
                } catch (SQLiteException e) {
                    cursor = query;
                    e = e;
                    zzfgVar.zzip().zze("Error querying for table", str, e);
                    if (cursor != null) {
                        cursor.close();
                    }
                    return false;
                } catch (Throwable th) {
                    th = th;
                    cursor = query;
                    if (cursor != null) {
                        cursor.close();
                    }
                    throw th;
                }
            } catch (Throwable th2) {
                th = th2;
            }
        } catch (SQLiteException e2) {
            e = e2;
        }
    }

    @WorkerThread
    private final boolean zza(String str, int i, zzke zzkeVar) {
        zzch();
        zzab();
        Preconditions.checkNotEmpty(str);
        Preconditions.checkNotNull(zzkeVar);
        if (TextUtils.isEmpty(zzkeVar.zzarq)) {
            zzge().zzip().zzd("Event filter had no event name. Audience definition ignored. appId, audienceId, filterId", zzfg.zzbm(str), Integer.valueOf(i), String.valueOf(zzkeVar.zzarp));
            return false;
        }
        try {
            byte[] bArr = new byte[zzkeVar.zzvm()];
            zzabw zzb = zzabw.zzb(bArr, 0, bArr.length);
            zzkeVar.zza(zzb);
            zzb.zzve();
            ContentValues contentValues = new ContentValues();
            contentValues.put("app_id", str);
            contentValues.put("audience_id", Integer.valueOf(i));
            contentValues.put("filter_id", zzkeVar.zzarp);
            contentValues.put("event_name", zzkeVar.zzarq);
            contentValues.put("data", bArr);
            try {
                if (getWritableDatabase().insertWithOnConflict("event_filters", null, contentValues, 5) == -1) {
                    zzge().zzim().zzg("Failed to insert event filter (got -1). appId", zzfg.zzbm(str));
                    return true;
                }
                return true;
            } catch (SQLiteException e) {
                zzge().zzim().zze("Error storing event filter. appId", zzfg.zzbm(str), e);
                return false;
            }
        } catch (IOException e2) {
            zzge().zzim().zze("Configuration loss. Failed to serialize event filter. appId", zzfg.zzbm(str), e2);
            return false;
        }
    }

    @WorkerThread
    private final boolean zza(String str, int i, zzkh zzkhVar) {
        zzch();
        zzab();
        Preconditions.checkNotEmpty(str);
        Preconditions.checkNotNull(zzkhVar);
        if (TextUtils.isEmpty(zzkhVar.zzasf)) {
            zzge().zzip().zzd("Property filter had no property name. Audience definition ignored. appId, audienceId, filterId", zzfg.zzbm(str), Integer.valueOf(i), String.valueOf(zzkhVar.zzarp));
            return false;
        }
        try {
            byte[] bArr = new byte[zzkhVar.zzvm()];
            zzabw zzb = zzabw.zzb(bArr, 0, bArr.length);
            zzkhVar.zza(zzb);
            zzb.zzve();
            ContentValues contentValues = new ContentValues();
            contentValues.put("app_id", str);
            contentValues.put("audience_id", Integer.valueOf(i));
            contentValues.put("filter_id", zzkhVar.zzarp);
            contentValues.put("property_name", zzkhVar.zzasf);
            contentValues.put("data", bArr);
            try {
                if (getWritableDatabase().insertWithOnConflict("property_filters", null, contentValues, 5) == -1) {
                    zzge().zzim().zzg("Failed to insert property filter (got -1). appId", zzfg.zzbm(str));
                    return false;
                }
                return true;
            } catch (SQLiteException e) {
                zzge().zzim().zze("Error storing property filter. appId", zzfg.zzbm(str), e);
                return false;
            }
        } catch (IOException e2) {
            zzge().zzim().zze("Configuration loss. Failed to serialize property filter. appId", zzfg.zzbm(str), e2);
            return false;
        }
    }

    private final boolean zza(String str, List<Integer> list) {
        Preconditions.checkNotEmpty(str);
        zzch();
        zzab();
        SQLiteDatabase writableDatabase = getWritableDatabase();
        try {
            long zza = zza("select count(1) from audience_filter_values where app_id=?", new String[]{str});
            int max = Math.max(0, Math.min(2000, zzgg().zzb(str, zzew.zzahn)));
            if (zza <= max) {
                return false;
            }
            ArrayList arrayList = new ArrayList();
            for (int i = 0; i < list.size(); i++) {
                Integer num = list.get(i);
                if (num == null || !(num instanceof Integer)) {
                    return false;
                }
                arrayList.add(Integer.toString(num.intValue()));
            }
            String join = TextUtils.join(",", arrayList);
            StringBuilder sb = new StringBuilder(String.valueOf(join).length() + 2);
            sb.append("(");
            sb.append(join);
            sb.append(")");
            String sb2 = sb.toString();
            StringBuilder sb3 = new StringBuilder(String.valueOf(sb2).length() + 140);
            sb3.append("audience_id in (select audience_id from audience_filter_values where app_id=? and audience_id not in ");
            sb3.append(sb2);
            sb3.append(" order by rowid desc limit -1 offset ?)");
            return writableDatabase.delete("audience_filter_values", sb3.toString(), new String[]{str, Integer.toString(max)}) > 0;
        } catch (SQLiteException e) {
            zzge().zzim().zze("Database error querying filters. appId", zzfg.zzbm(str), e);
            return false;
        }
    }

    @WorkerThread
    private static Set<String> zzb(SQLiteDatabase sQLiteDatabase, String str) {
        HashSet hashSet = new HashSet();
        StringBuilder sb = new StringBuilder(String.valueOf(str).length() + 22);
        sb.append("SELECT * FROM ");
        sb.append(str);
        sb.append(" LIMIT 0");
        Cursor rawQuery = sQLiteDatabase.rawQuery(sb.toString(), null);
        try {
            Collections.addAll(hashSet, rawQuery.getColumnNames());
            return hashSet;
        } finally {
            rawQuery.close();
        }
    }

    private final boolean zzhv() {
        return getContext().getDatabasePath("google_app_measurement.db").exists();
    }

    @WorkerThread
    public final void beginTransaction() {
        zzch();
        getWritableDatabase().beginTransaction();
    }

    @WorkerThread
    public final void endTransaction() {
        zzch();
        getWritableDatabase().endTransaction();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @VisibleForTesting
    @WorkerThread
    public final SQLiteDatabase getWritableDatabase() {
        zzab();
        try {
            return this.zzafb.getWritableDatabase();
        } catch (SQLiteException e) {
            zzge().zzip().zzg("Error opening database", e);
            throw e;
        }
    }

    @WorkerThread
    public final void setTransactionSuccessful() {
        zzch();
        getWritableDatabase().setTransactionSuccessful();
    }

    public final long zza(zzkq zzkqVar) throws IOException {
        long zzc;
        zzab();
        zzch();
        Preconditions.checkNotNull(zzkqVar);
        Preconditions.checkNotEmpty(zzkqVar.zzti);
        try {
            byte[] bArr = new byte[zzkqVar.zzvm()];
            zzabw zzb = zzabw.zzb(bArr, 0, bArr.length);
            zzkqVar.zza(zzb);
            zzb.zzve();
            zzka zzgb = zzgb();
            Preconditions.checkNotNull(bArr);
            zzgb.zzab();
            MessageDigest messageDigest = zzka.getMessageDigest("MD5");
            if (messageDigest == null) {
                zzgb.zzge().zzim().log("Failed to get MD5");
                zzc = 0;
            } else {
                zzc = zzka.zzc(messageDigest.digest(bArr));
            }
            ContentValues contentValues = new ContentValues();
            contentValues.put("app_id", zzkqVar.zzti);
            contentValues.put("metadata_fingerprint", Long.valueOf(zzc));
            contentValues.put("metadata", bArr);
            try {
                getWritableDatabase().insertWithOnConflict("raw_events_metadata", null, contentValues, 4);
                return zzc;
            } catch (SQLiteException e) {
                zzge().zzim().zze("Error storing raw event metadata. appId", zzfg.zzbm(zzkqVar.zzti), e);
                throw e;
            }
        } catch (IOException e2) {
            zzge().zzim().zze("Data loss. Failed to serialize event metadata. appId", zzfg.zzbm(zzkqVar.zzti), e2);
            throw e2;
        }
    }

    /* JADX WARN: Removed duplicated region for block: B:33:0x008f  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public final android.util.Pair<com.google.android.gms.internal.measurement.zzkn, java.lang.Long> zza(java.lang.String r8, java.lang.Long r9) {
        /*
            r7 = this;
            r7.zzab()
            r7.zzch()
            r0 = 0
            android.database.sqlite.SQLiteDatabase r1 = r7.getWritableDatabase()     // Catch: java.lang.Throwable -> L74 android.database.sqlite.SQLiteException -> L77
            java.lang.String r2 = "select main_event, children_to_process from main_event_params where app_id=? and event_id=?"
            r3 = 2
            java.lang.String[] r3 = new java.lang.String[r3]     // Catch: java.lang.Throwable -> L74 android.database.sqlite.SQLiteException -> L77
            r4 = 0
            r3[r4] = r8     // Catch: java.lang.Throwable -> L74 android.database.sqlite.SQLiteException -> L77
            java.lang.String r5 = java.lang.String.valueOf(r9)     // Catch: java.lang.Throwable -> L74 android.database.sqlite.SQLiteException -> L77
            r6 = 1
            r3[r6] = r5     // Catch: java.lang.Throwable -> L74 android.database.sqlite.SQLiteException -> L77
            android.database.Cursor r1 = r1.rawQuery(r2, r3)     // Catch: java.lang.Throwable -> L74 android.database.sqlite.SQLiteException -> L77
            boolean r2 = r1.moveToFirst()     // Catch: android.database.sqlite.SQLiteException -> L72 java.lang.Throwable -> L8c
            if (r2 != 0) goto L37
            com.google.android.gms.internal.measurement.zzfg r8 = r7.zzge()     // Catch: android.database.sqlite.SQLiteException -> L72 java.lang.Throwable -> L8c
            com.google.android.gms.internal.measurement.zzfi r8 = r8.zzit()     // Catch: android.database.sqlite.SQLiteException -> L72 java.lang.Throwable -> L8c
            java.lang.String r9 = "Main event not found"
            r8.log(r9)     // Catch: android.database.sqlite.SQLiteException -> L72 java.lang.Throwable -> L8c
            if (r1 == 0) goto L36
            r1.close()
        L36:
            return r0
        L37:
            byte[] r2 = r1.getBlob(r4)     // Catch: android.database.sqlite.SQLiteException -> L72 java.lang.Throwable -> L8c
            long r5 = r1.getLong(r6)     // Catch: android.database.sqlite.SQLiteException -> L72 java.lang.Throwable -> L8c
            java.lang.Long r3 = java.lang.Long.valueOf(r5)     // Catch: android.database.sqlite.SQLiteException -> L72 java.lang.Throwable -> L8c
            int r5 = r2.length     // Catch: android.database.sqlite.SQLiteException -> L72 java.lang.Throwable -> L8c
            com.google.android.gms.internal.measurement.zzabv r2 = com.google.android.gms.internal.measurement.zzabv.zza(r2, r4, r5)     // Catch: android.database.sqlite.SQLiteException -> L72 java.lang.Throwable -> L8c
            com.google.android.gms.internal.measurement.zzkn r4 = new com.google.android.gms.internal.measurement.zzkn     // Catch: android.database.sqlite.SQLiteException -> L72 java.lang.Throwable -> L8c
            r4.<init>()     // Catch: android.database.sqlite.SQLiteException -> L72 java.lang.Throwable -> L8c
            r4.zzb(r2)     // Catch: java.io.IOException -> L5a android.database.sqlite.SQLiteException -> L72 java.lang.Throwable -> L8c
            android.util.Pair r8 = android.util.Pair.create(r4, r3)     // Catch: android.database.sqlite.SQLiteException -> L72 java.lang.Throwable -> L8c
            if (r1 == 0) goto L59
            r1.close()
        L59:
            return r8
        L5a:
            r2 = move-exception
            com.google.android.gms.internal.measurement.zzfg r3 = r7.zzge()     // Catch: android.database.sqlite.SQLiteException -> L72 java.lang.Throwable -> L8c
            com.google.android.gms.internal.measurement.zzfi r3 = r3.zzim()     // Catch: android.database.sqlite.SQLiteException -> L72 java.lang.Throwable -> L8c
            java.lang.String r4 = "Failed to merge main event. appId, eventId"
            java.lang.Object r8 = com.google.android.gms.internal.measurement.zzfg.zzbm(r8)     // Catch: android.database.sqlite.SQLiteException -> L72 java.lang.Throwable -> L8c
            r3.zzd(r4, r8, r9, r2)     // Catch: android.database.sqlite.SQLiteException -> L72 java.lang.Throwable -> L8c
            if (r1 == 0) goto L71
            r1.close()
        L71:
            return r0
        L72:
            r8 = move-exception
            goto L79
        L74:
            r7 = move-exception
            r1 = r0
            goto L8d
        L77:
            r8 = move-exception
            r1 = r0
        L79:
            com.google.android.gms.internal.measurement.zzfg r7 = r7.zzge()     // Catch: java.lang.Throwable -> L8c
            com.google.android.gms.internal.measurement.zzfi r7 = r7.zzim()     // Catch: java.lang.Throwable -> L8c
            java.lang.String r9 = "Error selecting main event"
            r7.zzg(r9, r8)     // Catch: java.lang.Throwable -> L8c
            if (r1 == 0) goto L8b
            r1.close()
        L8b:
            return r0
        L8c:
            r7 = move-exception
        L8d:
            if (r1 == 0) goto L92
            r1.close()
        L92:
            throw r7
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.android.gms.internal.measurement.zzei.zza(java.lang.String, java.lang.Long):android.util.Pair");
    }

    @WorkerThread
    public final zzej zza(long j, String str, boolean z, boolean z2, boolean z3, boolean z4, boolean z5) {
        Cursor cursor;
        Preconditions.checkNotEmpty(str);
        zzab();
        zzch();
        String[] strArr = {str};
        zzej zzejVar = new zzej();
        Cursor cursor2 = null;
        try {
            try {
                SQLiteDatabase writableDatabase = getWritableDatabase();
                cursor = writableDatabase.query("apps", new String[]{"day", "daily_events_count", "daily_public_events_count", "daily_conversions_count", "daily_error_events_count", "daily_realtime_events_count"}, "app_id=?", new String[]{str}, null, null, null);
                try {
                    if (!cursor.moveToFirst()) {
                        zzge().zzip().zzg("Not updating daily counts, app is not known. appId", zzfg.zzbm(str));
                        if (cursor != null) {
                            cursor.close();
                        }
                        return zzejVar;
                    }
                    if (cursor.getLong(0) == j) {
                        zzejVar.zzafe = cursor.getLong(1);
                        zzejVar.zzafd = cursor.getLong(2);
                        zzejVar.zzaff = cursor.getLong(3);
                        zzejVar.zzafg = cursor.getLong(4);
                        zzejVar.zzafh = cursor.getLong(5);
                    }
                    if (z) {
                        zzejVar.zzafe++;
                    }
                    if (z2) {
                        zzejVar.zzafd++;
                    }
                    if (z3) {
                        zzejVar.zzaff++;
                    }
                    if (z4) {
                        zzejVar.zzafg++;
                    }
                    if (z5) {
                        zzejVar.zzafh++;
                    }
                    ContentValues contentValues = new ContentValues();
                    contentValues.put("day", Long.valueOf(j));
                    contentValues.put("daily_public_events_count", Long.valueOf(zzejVar.zzafd));
                    contentValues.put("daily_events_count", Long.valueOf(zzejVar.zzafe));
                    contentValues.put("daily_conversions_count", Long.valueOf(zzejVar.zzaff));
                    contentValues.put("daily_error_events_count", Long.valueOf(zzejVar.zzafg));
                    contentValues.put("daily_realtime_events_count", Long.valueOf(zzejVar.zzafh));
                    writableDatabase.update("apps", contentValues, "app_id=?", strArr);
                    if (cursor != null) {
                        cursor.close();
                    }
                    return zzejVar;
                } catch (SQLiteException e) {
                    e = e;
                    cursor2 = cursor;
                    zzge().zzim().zze("Error updating daily counts. appId", zzfg.zzbm(str), e);
                    if (cursor2 != null) {
                        cursor2.close();
                    }
                    return zzejVar;
                } catch (Throwable th) {
                    th = th;
                    if (cursor != null) {
                        cursor.close();
                    }
                    throw th;
                }
            } catch (Throwable th2) {
                th = th2;
                cursor = cursor2;
            }
        } catch (SQLiteException e2) {
            e = e2;
        }
    }

    @WorkerThread
    public final void zza(zzdy zzdyVar) {
        Preconditions.checkNotNull(zzdyVar);
        zzab();
        zzch();
        ContentValues contentValues = new ContentValues();
        contentValues.put("app_id", zzdyVar.zzah());
        contentValues.put("app_instance_id", zzdyVar.getAppInstanceId());
        contentValues.put("gmp_app_id", zzdyVar.getGmpAppId());
        contentValues.put("resettable_device_id_hash", zzdyVar.zzgi());
        contentValues.put("last_bundle_index", Long.valueOf(zzdyVar.zzgq()));
        contentValues.put("last_bundle_start_timestamp", Long.valueOf(zzdyVar.zzgk()));
        contentValues.put("last_bundle_end_timestamp", Long.valueOf(zzdyVar.zzgl()));
        contentValues.put("app_version", zzdyVar.zzag());
        contentValues.put("app_store", zzdyVar.zzgn());
        contentValues.put("gmp_version", Long.valueOf(zzdyVar.zzgo()));
        contentValues.put("dev_cert_hash", Long.valueOf(zzdyVar.zzgp()));
        contentValues.put("measurement_enabled", Boolean.valueOf(zzdyVar.isMeasurementEnabled()));
        contentValues.put("day", Long.valueOf(zzdyVar.zzgu()));
        contentValues.put("daily_public_events_count", Long.valueOf(zzdyVar.zzgv()));
        contentValues.put("daily_events_count", Long.valueOf(zzdyVar.zzgw()));
        contentValues.put("daily_conversions_count", Long.valueOf(zzdyVar.zzgx()));
        contentValues.put("config_fetched_time", Long.valueOf(zzdyVar.zzgr()));
        contentValues.put("failed_config_fetch_time", Long.valueOf(zzdyVar.zzgs()));
        contentValues.put("app_version_int", Long.valueOf(zzdyVar.zzgm()));
        contentValues.put("firebase_instance_id", zzdyVar.zzgj());
        contentValues.put("daily_error_events_count", Long.valueOf(zzdyVar.zzgz()));
        contentValues.put("daily_realtime_events_count", Long.valueOf(zzdyVar.zzgy()));
        contentValues.put("health_monitor_sample", zzdyVar.zzha());
        contentValues.put("android_id", Long.valueOf(zzdyVar.zzhc()));
        contentValues.put("adid_reporting_enabled", Boolean.valueOf(zzdyVar.zzhd()));
        contentValues.put("ssaid_reporting_enabled", Boolean.valueOf(zzdyVar.zzhe()));
        try {
            SQLiteDatabase writableDatabase = getWritableDatabase();
            if (writableDatabase.update("apps", contentValues, "app_id = ?", new String[]{zzdyVar.zzah()}) == 0 && writableDatabase.insertWithOnConflict("apps", null, contentValues, 5) == -1) {
                zzge().zzim().zzg("Failed to insert/update app (got -1). appId", zzfg.zzbm(zzdyVar.zzah()));
            }
        } catch (SQLiteException e) {
            zzge().zzim().zze("Error storing app. appId", zzfg.zzbm(zzdyVar.zzah()), e);
        }
    }

    @WorkerThread
    public final void zza(zzeq zzeqVar) {
        Preconditions.checkNotNull(zzeqVar);
        zzab();
        zzch();
        ContentValues contentValues = new ContentValues();
        contentValues.put("app_id", zzeqVar.zzti);
        contentValues.put("name", zzeqVar.name);
        contentValues.put("lifetime_count", Long.valueOf(zzeqVar.zzafr));
        contentValues.put("current_bundle_count", Long.valueOf(zzeqVar.zzafs));
        contentValues.put("last_fire_timestamp", Long.valueOf(zzeqVar.zzaft));
        contentValues.put("last_bundled_timestamp", Long.valueOf(zzeqVar.zzafu));
        contentValues.put("last_sampled_complex_event_id", zzeqVar.zzafv);
        contentValues.put("last_sampling_rate", zzeqVar.zzafw);
        contentValues.put("last_exempt_from_sampling", (zzeqVar.zzafx == null || !zzeqVar.zzafx.booleanValue()) ? null : 1L);
        try {
            if (getWritableDatabase().insertWithOnConflict("events", null, contentValues, 5) == -1) {
                zzge().zzim().zzg("Failed to insert/update event aggregates (got -1). appId", zzfg.zzbm(zzeqVar.zzti));
            }
        } catch (SQLiteException e) {
            zzge().zzim().zze("Error storing event aggregates. appId", zzfg.zzbm(zzeqVar.zzti), e);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @WorkerThread
    public final void zza(String str, zzkd[] zzkdVarArr) {
        boolean z;
        zzfi zzip;
        String str2;
        Object zzbm;
        Integer num;
        zzch();
        zzab();
        Preconditions.checkNotEmpty(str);
        Preconditions.checkNotNull(zzkdVarArr);
        SQLiteDatabase writableDatabase = getWritableDatabase();
        writableDatabase.beginTransaction();
        try {
            zzch();
            zzab();
            Preconditions.checkNotEmpty(str);
            SQLiteDatabase writableDatabase2 = getWritableDatabase();
            writableDatabase2.delete("property_filters", "app_id=?", new String[]{str});
            writableDatabase2.delete("event_filters", "app_id=?", new String[]{str});
            for (zzkd zzkdVar : zzkdVarArr) {
                zzch();
                zzab();
                Preconditions.checkNotEmpty(str);
                Preconditions.checkNotNull(zzkdVar);
                Preconditions.checkNotNull(zzkdVar.zzarn);
                Preconditions.checkNotNull(zzkdVar.zzarm);
                if (zzkdVar.zzarl != null) {
                    int intValue = zzkdVar.zzarl.intValue();
                    zzke[] zzkeVarArr = zzkdVar.zzarn;
                    int length = zzkeVarArr.length;
                    int i = 0;
                    while (true) {
                        if (i >= length) {
                            for (zzkh zzkhVar : zzkdVar.zzarm) {
                                if (zzkhVar.zzarp == null) {
                                    zzip = zzge().zzip();
                                    str2 = "Property filter with no ID. Audience definition ignored. appId, audienceId";
                                    zzbm = zzfg.zzbm(str);
                                    num = zzkdVar.zzarl;
                                }
                            }
                            zzke[] zzkeVarArr2 = zzkdVar.zzarn;
                            int length2 = zzkeVarArr2.length;
                            int i2 = 0;
                            while (true) {
                                if (i2 >= length2) {
                                    z = true;
                                    break;
                                } else if (!zza(str, intValue, zzkeVarArr2[i2])) {
                                    z = false;
                                    break;
                                } else {
                                    i2++;
                                }
                            }
                            if (z) {
                                zzkh[] zzkhVarArr = zzkdVar.zzarm;
                                int length3 = zzkhVarArr.length;
                                int i3 = 0;
                                while (true) {
                                    if (i3 >= length3) {
                                        break;
                                    } else if (!zza(str, intValue, zzkhVarArr[i3])) {
                                        z = false;
                                        break;
                                    } else {
                                        i3++;
                                    }
                                }
                            }
                            if (!z) {
                                zzch();
                                zzab();
                                Preconditions.checkNotEmpty(str);
                                SQLiteDatabase writableDatabase3 = getWritableDatabase();
                                writableDatabase3.delete("property_filters", "app_id=? and audience_id=?", new String[]{str, String.valueOf(intValue)});
                                writableDatabase3.delete("event_filters", "app_id=? and audience_id=?", new String[]{str, String.valueOf(intValue)});
                            }
                        } else if (zzkeVarArr[i].zzarp == null) {
                            zzip = zzge().zzip();
                            str2 = "Event filter with no ID. Audience definition ignored. appId, audienceId";
                            zzbm = zzfg.zzbm(str);
                            num = zzkdVar.zzarl;
                            break;
                        } else {
                            i++;
                        }
                    }
                    zzip.zze(str2, zzbm, num);
                    break;
                } else {
                    zzge().zzip().zzg("Audience with no ID. appId", zzfg.zzbm(str));
                }
            }
            ArrayList arrayList = new ArrayList();
            for (zzkd zzkdVar2 : zzkdVarArr) {
                arrayList.add(zzkdVar2.zzarl);
            }
            zza(str, arrayList);
            writableDatabase.setTransactionSuccessful();
        } finally {
            writableDatabase.endTransaction();
        }
    }

    @WorkerThread
    public final boolean zza(zzed zzedVar) {
        Preconditions.checkNotNull(zzedVar);
        zzab();
        zzch();
        if (zzh(zzedVar.packageName, zzedVar.zzaep.name) != null || zza("SELECT COUNT(1) FROM conditional_properties WHERE app_id=?", new String[]{zzedVar.packageName}) < 1000) {
            ContentValues contentValues = new ContentValues();
            contentValues.put("app_id", zzedVar.packageName);
            contentValues.put(FirebaseAnalytics.Param.ORIGIN, zzedVar.origin);
            contentValues.put("name", zzedVar.zzaep.name);
            zza(contentValues, FirebaseAnalytics.Param.VALUE, zzedVar.zzaep.getValue());
            contentValues.put("active", Boolean.valueOf(zzedVar.active));
            contentValues.put("trigger_event_name", zzedVar.triggerEventName);
            contentValues.put("trigger_timeout", Long.valueOf(zzedVar.triggerTimeout));
            zzgb();
            contentValues.put("timed_out_event", zzka.zza(zzedVar.zzaeq));
            contentValues.put("creation_timestamp", Long.valueOf(zzedVar.creationTimestamp));
            zzgb();
            contentValues.put("triggered_event", zzka.zza(zzedVar.zzaer));
            contentValues.put("triggered_timestamp", Long.valueOf(zzedVar.zzaep.zzaqz));
            contentValues.put("time_to_live", Long.valueOf(zzedVar.timeToLive));
            zzgb();
            contentValues.put("expired_event", zzka.zza(zzedVar.zzaes));
            try {
                if (getWritableDatabase().insertWithOnConflict("conditional_properties", null, contentValues, 5) == -1) {
                    zzge().zzim().zzg("Failed to insert/update conditional user property (got -1)", zzfg.zzbm(zzedVar.packageName));
                }
            } catch (SQLiteException e) {
                zzge().zzim().zze("Error storing conditional user property", zzfg.zzbm(zzedVar.packageName), e);
            }
            return true;
        }
        return false;
    }

    public final boolean zza(zzep zzepVar, long j, boolean z) {
        zzfi zzim;
        String str;
        zzab();
        zzch();
        Preconditions.checkNotNull(zzepVar);
        Preconditions.checkNotEmpty(zzepVar.zzti);
        zzkn zzknVar = new zzkn();
        zzknVar.zzatc = Long.valueOf(zzepVar.zzafp);
        zzknVar.zzata = new zzko[zzepVar.zzafq.size()];
        Iterator<String> it = zzepVar.zzafq.iterator();
        int i = 0;
        while (it.hasNext()) {
            String next = it.next();
            zzko zzkoVar = new zzko();
            zzknVar.zzata[i] = zzkoVar;
            zzkoVar.name = next;
            zzgb().zza(zzkoVar, zzepVar.zzafq.get(next));
            i++;
        }
        try {
            byte[] bArr = new byte[zzknVar.zzvm()];
            zzabw zzb = zzabw.zzb(bArr, 0, bArr.length);
            zzknVar.zza(zzb);
            zzb.zzve();
            zzge().zzit().zze("Saving event, name, data size", zzga().zzbj(zzepVar.name), Integer.valueOf(bArr.length));
            ContentValues contentValues = new ContentValues();
            contentValues.put("app_id", zzepVar.zzti);
            contentValues.put("name", zzepVar.name);
            contentValues.put(AppMeasurement.Param.TIMESTAMP, Long.valueOf(zzepVar.timestamp));
            contentValues.put("metadata_fingerprint", Long.valueOf(j));
            contentValues.put("data", bArr);
            contentValues.put("realtime", Integer.valueOf(z ? 1 : 0));
            try {
                if (getWritableDatabase().insert("raw_events", null, contentValues) == -1) {
                    zzge().zzim().zzg("Failed to insert raw event (got -1). appId", zzfg.zzbm(zzepVar.zzti));
                    return false;
                }
                return true;
            } catch (SQLiteException e) {
                e = e;
                zzim = zzge().zzim();
                str = "Error storing raw event. appId";
                zzim.zze(str, zzfg.zzbm(zzepVar.zzti), e);
                return false;
            }
        } catch (IOException e2) {
            e = e2;
            zzim = zzge().zzim();
            str = "Data loss. Failed to serialize event params/data. appId";
        }
    }

    @WorkerThread
    public final boolean zza(zzjz zzjzVar) {
        Preconditions.checkNotNull(zzjzVar);
        zzab();
        zzch();
        if (zzh(zzjzVar.zzti, zzjzVar.name) == null) {
            if (zzka.zzcc(zzjzVar.name)) {
                if (zza("select count(1) from user_attributes where app_id=? and name not like '!_%' escape '!'", new String[]{zzjzVar.zzti}) >= 25) {
                    return false;
                }
            } else if (zza("select count(1) from user_attributes where app_id=? and origin=? AND name like '!_%' escape '!'", new String[]{zzjzVar.zzti, zzjzVar.origin}) >= 25) {
                return false;
            }
        }
        ContentValues contentValues = new ContentValues();
        contentValues.put("app_id", zzjzVar.zzti);
        contentValues.put(FirebaseAnalytics.Param.ORIGIN, zzjzVar.origin);
        contentValues.put("name", zzjzVar.name);
        contentValues.put("set_timestamp", Long.valueOf(zzjzVar.zzaqz));
        zza(contentValues, FirebaseAnalytics.Param.VALUE, zzjzVar.value);
        try {
            if (getWritableDatabase().insertWithOnConflict("user_attributes", null, contentValues, 5) == -1) {
                zzge().zzim().zzg("Failed to insert/update user property (got -1). appId", zzfg.zzbm(zzjzVar.zzti));
            }
        } catch (SQLiteException e) {
            zzge().zzim().zze("Error storing user property. appId", zzfg.zzbm(zzjzVar.zzti), e);
        }
        return true;
    }

    @WorkerThread
    public final boolean zza(zzkq zzkqVar, boolean z) {
        zzfi zzim;
        String str;
        zzab();
        zzch();
        Preconditions.checkNotNull(zzkqVar);
        Preconditions.checkNotEmpty(zzkqVar.zzti);
        Preconditions.checkNotNull(zzkqVar.zzatm);
        zzhp();
        long currentTimeMillis = zzbt().currentTimeMillis();
        if (zzkqVar.zzatm.longValue() < currentTimeMillis - zzef.zzhh() || zzkqVar.zzatm.longValue() > zzef.zzhh() + currentTimeMillis) {
            zzge().zzip().zzd("Storing bundle outside of the max uploading time span. appId, now, timestamp", zzfg.zzbm(zzkqVar.zzti), Long.valueOf(currentTimeMillis), zzkqVar.zzatm);
        }
        try {
            byte[] bArr = new byte[zzkqVar.zzvm()];
            zzabw zzb = zzabw.zzb(bArr, 0, bArr.length);
            zzkqVar.zza(zzb);
            zzb.zzve();
            byte[] zza = zzgb().zza(bArr);
            zzge().zzit().zzg("Saving bundle, size", Integer.valueOf(zza.length));
            ContentValues contentValues = new ContentValues();
            contentValues.put("app_id", zzkqVar.zzti);
            contentValues.put("bundle_end_timestamp", zzkqVar.zzatm);
            contentValues.put("data", zza);
            contentValues.put("has_realtime", Integer.valueOf(z ? 1 : 0));
            if (zzkqVar.zzauj != null) {
                contentValues.put("retry_count", zzkqVar.zzauj);
            }
            try {
                if (getWritableDatabase().insert("queue", null, contentValues) == -1) {
                    zzge().zzim().zzg("Failed to insert bundle (got -1). appId", zzfg.zzbm(zzkqVar.zzti));
                    return false;
                }
                return true;
            } catch (SQLiteException e) {
                e = e;
                zzim = zzge().zzim();
                str = "Error storing bundle. appId";
                zzim.zze(str, zzfg.zzbm(zzkqVar.zzti), e);
                return false;
            }
        } catch (IOException e2) {
            e = e2;
            zzim = zzge().zzim();
            str = "Data loss. Failed to serialize bundle. appId";
        }
    }

    public final boolean zza(String str, Long l, long j, zzkn zzknVar) {
        zzab();
        zzch();
        Preconditions.checkNotNull(zzknVar);
        Preconditions.checkNotEmpty(str);
        Preconditions.checkNotNull(l);
        try {
            byte[] bArr = new byte[zzknVar.zzvm()];
            zzabw zzb = zzabw.zzb(bArr, 0, bArr.length);
            zzknVar.zza(zzb);
            zzb.zzve();
            zzge().zzit().zze("Saving complex main event, appId, data size", zzga().zzbj(str), Integer.valueOf(bArr.length));
            ContentValues contentValues = new ContentValues();
            contentValues.put("app_id", str);
            contentValues.put("event_id", l);
            contentValues.put("children_to_process", Long.valueOf(j));
            contentValues.put("main_event", bArr);
            try {
                if (getWritableDatabase().insertWithOnConflict("main_event_params", null, contentValues, 5) == -1) {
                    zzge().zzim().zzg("Failed to insert complex main event (got -1). appId", zzfg.zzbm(str));
                    return false;
                }
                return true;
            } catch (SQLiteException e) {
                zzge().zzim().zze("Error storing complex main event. appId", zzfg.zzbm(str), e);
                return false;
            }
        } catch (IOException e2) {
            zzge().zzim().zzd("Data loss. Failed to serialize event params/data. appId, eventId", zzfg.zzbm(str), l, e2);
            return false;
        }
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Removed duplicated region for block: B:26:0x005b  */
    /* JADX WARN: Type inference failed for: r5v0, types: [long] */
    /* JADX WARN: Type inference failed for: r5v1 */
    /* JADX WARN: Type inference failed for: r5v3, types: [android.database.Cursor] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public final java.lang.String zzab(long r5) {
        /*
            r4 = this;
            r4.zzab()
            r4.zzch()
            r0 = 0
            android.database.sqlite.SQLiteDatabase r1 = r4.getWritableDatabase()     // Catch: java.lang.Throwable -> L40 android.database.sqlite.SQLiteException -> L43
            java.lang.String r2 = "select app_id from apps where app_id in (select distinct app_id from raw_events) and config_fetched_time < ? order by failed_config_fetch_time limit 1;"
            r3 = 1
            java.lang.String[] r3 = new java.lang.String[r3]     // Catch: java.lang.Throwable -> L40 android.database.sqlite.SQLiteException -> L43
            java.lang.String r5 = java.lang.String.valueOf(r5)     // Catch: java.lang.Throwable -> L40 android.database.sqlite.SQLiteException -> L43
            r6 = 0
            r3[r6] = r5     // Catch: java.lang.Throwable -> L40 android.database.sqlite.SQLiteException -> L43
            android.database.Cursor r5 = r1.rawQuery(r2, r3)     // Catch: java.lang.Throwable -> L40 android.database.sqlite.SQLiteException -> L43
            boolean r1 = r5.moveToFirst()     // Catch: android.database.sqlite.SQLiteException -> L3e java.lang.Throwable -> L58
            if (r1 != 0) goto L34
            com.google.android.gms.internal.measurement.zzfg r6 = r4.zzge()     // Catch: android.database.sqlite.SQLiteException -> L3e java.lang.Throwable -> L58
            com.google.android.gms.internal.measurement.zzfi r6 = r6.zzit()     // Catch: android.database.sqlite.SQLiteException -> L3e java.lang.Throwable -> L58
            java.lang.String r1 = "No expired configs for apps with pending events"
            r6.log(r1)     // Catch: android.database.sqlite.SQLiteException -> L3e java.lang.Throwable -> L58
            if (r5 == 0) goto L33
            r5.close()
        L33:
            return r0
        L34:
            java.lang.String r6 = r5.getString(r6)     // Catch: android.database.sqlite.SQLiteException -> L3e java.lang.Throwable -> L58
            if (r5 == 0) goto L3d
            r5.close()
        L3d:
            return r6
        L3e:
            r6 = move-exception
            goto L45
        L40:
            r4 = move-exception
            r5 = r0
            goto L59
        L43:
            r6 = move-exception
            r5 = r0
        L45:
            com.google.android.gms.internal.measurement.zzfg r4 = r4.zzge()     // Catch: java.lang.Throwable -> L58
            com.google.android.gms.internal.measurement.zzfi r4 = r4.zzim()     // Catch: java.lang.Throwable -> L58
            java.lang.String r1 = "Error selecting expired configs"
            r4.zzg(r1, r6)     // Catch: java.lang.Throwable -> L58
            if (r5 == 0) goto L57
            r5.close()
        L57:
            return r0
        L58:
            r4 = move-exception
        L59:
            if (r5 == 0) goto L5e
            r5.close()
        L5e:
            throw r4
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.android.gms.internal.measurement.zzei.zzab(long):java.lang.String");
    }

    /* JADX WARN: Removed duplicated region for block: B:41:0x00c7  */
    /* JADX WARN: Removed duplicated region for block: B:64:0x00c5 A[EDGE_INSN: B:64:0x00c5->B:40:0x00c5 ?: BREAK  , SYNTHETIC] */
    @androidx.annotation.WorkerThread
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public final java.util.List<android.util.Pair<com.google.android.gms.internal.measurement.zzkq, java.lang.Long>> zzb(java.lang.String r13, int r14, int r15) {
        /*
            Method dump skipped, instructions count: 245
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.android.gms.internal.measurement.zzei.zzb(java.lang.String, int, int):java.util.List");
    }

    /* JADX WARN: Code restructure failed: missing block: B:23:0x0097, code lost:
        zzge().zzim().zzg("Read more than the max allowed user properties, ignoring excess", 1000);
     */
    /* JADX WARN: Removed duplicated region for block: B:52:0x011a  */
    /* JADX WARN: Removed duplicated region for block: B:57:0x0122  */
    @androidx.annotation.WorkerThread
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public final java.util.List<com.google.android.gms.internal.measurement.zzjz> zzb(java.lang.String r22, java.lang.String r23, java.lang.String r24) {
        /*
            Method dump skipped, instructions count: 294
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.android.gms.internal.measurement.zzei.zzb(java.lang.String, java.lang.String, java.lang.String):java.util.List");
    }

    /* JADX WARN: Code restructure failed: missing block: B:11:0x0059, code lost:
        zzge().zzim().zzg("Read more than the max allowed conditional properties, ignoring extra", 1000);
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public final java.util.List<com.google.android.gms.internal.measurement.zzed> zzb(java.lang.String r25, java.lang.String[] r26) {
        /*
            Method dump skipped, instructions count: 309
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.android.gms.internal.measurement.zzei.zzb(java.lang.String, java.lang.String[]):java.util.List");
    }

    /* JADX WARN: Removed duplicated region for block: B:33:0x00a3  */
    @androidx.annotation.WorkerThread
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public final java.util.List<com.google.android.gms.internal.measurement.zzjz> zzbb(java.lang.String r14) {
        /*
            r13 = this;
            com.google.android.gms.common.internal.Preconditions.checkNotEmpty(r14)
            r13.zzab()
            r13.zzch()
            java.util.ArrayList r0 = new java.util.ArrayList
            r0.<init>()
            r1 = 0
            android.database.sqlite.SQLiteDatabase r2 = r13.getWritableDatabase()     // Catch: java.lang.Throwable -> L84 android.database.sqlite.SQLiteException -> L87
            java.lang.String r3 = "user_attributes"
            java.lang.String r4 = "name"
            java.lang.String r5 = "origin"
            java.lang.String r6 = "set_timestamp"
            java.lang.String r7 = "value"
            java.lang.String[] r4 = new java.lang.String[]{r4, r5, r6, r7}     // Catch: java.lang.Throwable -> L84 android.database.sqlite.SQLiteException -> L87
            java.lang.String r5 = "app_id=?"
            r11 = 1
            java.lang.String[] r6 = new java.lang.String[r11]     // Catch: java.lang.Throwable -> L84 android.database.sqlite.SQLiteException -> L87
            r12 = 0
            r6[r12] = r14     // Catch: java.lang.Throwable -> L84 android.database.sqlite.SQLiteException -> L87
            r7 = 0
            r8 = 0
            java.lang.String r9 = "rowid"
            java.lang.String r10 = "1000"
            android.database.Cursor r2 = r2.query(r3, r4, r5, r6, r7, r8, r9, r10)     // Catch: java.lang.Throwable -> L84 android.database.sqlite.SQLiteException -> L87
            boolean r3 = r2.moveToFirst()     // Catch: android.database.sqlite.SQLiteException -> L82 java.lang.Throwable -> La0
            if (r3 != 0) goto L41
            if (r2 == 0) goto L40
            r2.close()
        L40:
            return r0
        L41:
            java.lang.String r7 = r2.getString(r12)     // Catch: android.database.sqlite.SQLiteException -> L82 java.lang.Throwable -> La0
            java.lang.String r3 = r2.getString(r11)     // Catch: android.database.sqlite.SQLiteException -> L82 java.lang.Throwable -> La0
            if (r3 != 0) goto L4d
            java.lang.String r3 = ""
        L4d:
            r6 = r3
            r3 = 2
            long r8 = r2.getLong(r3)     // Catch: android.database.sqlite.SQLiteException -> L82 java.lang.Throwable -> La0
            r3 = 3
            java.lang.Object r10 = r13.zza(r2, r3)     // Catch: android.database.sqlite.SQLiteException -> L82 java.lang.Throwable -> La0
            if (r10 != 0) goto L6c
            com.google.android.gms.internal.measurement.zzfg r3 = r13.zzge()     // Catch: android.database.sqlite.SQLiteException -> L82 java.lang.Throwable -> La0
            com.google.android.gms.internal.measurement.zzfi r3 = r3.zzim()     // Catch: android.database.sqlite.SQLiteException -> L82 java.lang.Throwable -> La0
            java.lang.String r4 = "Read invalid user property value, ignoring it. appId"
            java.lang.Object r5 = com.google.android.gms.internal.measurement.zzfg.zzbm(r14)     // Catch: android.database.sqlite.SQLiteException -> L82 java.lang.Throwable -> La0
            r3.zzg(r4, r5)     // Catch: android.database.sqlite.SQLiteException -> L82 java.lang.Throwable -> La0
            goto L76
        L6c:
            com.google.android.gms.internal.measurement.zzjz r3 = new com.google.android.gms.internal.measurement.zzjz     // Catch: android.database.sqlite.SQLiteException -> L82 java.lang.Throwable -> La0
            r4 = r3
            r5 = r14
            r4.<init>(r5, r6, r7, r8, r10)     // Catch: android.database.sqlite.SQLiteException -> L82 java.lang.Throwable -> La0
            r0.add(r3)     // Catch: android.database.sqlite.SQLiteException -> L82 java.lang.Throwable -> La0
        L76:
            boolean r3 = r2.moveToNext()     // Catch: android.database.sqlite.SQLiteException -> L82 java.lang.Throwable -> La0
            if (r3 != 0) goto L41
            if (r2 == 0) goto L81
            r2.close()
        L81:
            return r0
        L82:
            r0 = move-exception
            goto L89
        L84:
            r13 = move-exception
            r2 = r1
            goto La1
        L87:
            r0 = move-exception
            r2 = r1
        L89:
            com.google.android.gms.internal.measurement.zzfg r13 = r13.zzge()     // Catch: java.lang.Throwable -> La0
            com.google.android.gms.internal.measurement.zzfi r13 = r13.zzim()     // Catch: java.lang.Throwable -> La0
            java.lang.String r3 = "Error querying user properties. appId"
            java.lang.Object r14 = com.google.android.gms.internal.measurement.zzfg.zzbm(r14)     // Catch: java.lang.Throwable -> La0
            r13.zze(r3, r14, r0)     // Catch: java.lang.Throwable -> La0
            if (r2 == 0) goto L9f
            r2.close()
        L9f:
            return r1
        La0:
            r13 = move-exception
        La1:
            if (r2 == 0) goto La6
            r2.close()
        La6:
            throw r13
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.android.gms.internal.measurement.zzei.zzbb(java.lang.String):java.util.List");
    }

    /* JADX WARN: Removed duplicated region for block: B:20:0x0114  */
    /* JADX WARN: Removed duplicated region for block: B:21:0x0118 A[Catch: SQLiteException -> 0x019e, all -> 0x01c2, TryCatch #1 {all -> 0x01c2, blocks: (B:4:0x0058, B:9:0x0064, B:11:0x0068, B:13:0x00c9, B:18:0x00d3, B:22:0x011d, B:26:0x0153, B:28:0x015e, B:33:0x0168, B:35:0x0173, B:39:0x017b, B:41:0x0187, B:25:0x014f, B:21:0x0118, B:53:0x01ab), top: B:61:0x000c }] */
    /* JADX WARN: Removed duplicated region for block: B:24:0x014c  */
    /* JADX WARN: Removed duplicated region for block: B:25:0x014f A[Catch: SQLiteException -> 0x019e, all -> 0x01c2, TryCatch #1 {all -> 0x01c2, blocks: (B:4:0x0058, B:9:0x0064, B:11:0x0068, B:13:0x00c9, B:18:0x00d3, B:22:0x011d, B:26:0x0153, B:28:0x015e, B:33:0x0168, B:35:0x0173, B:39:0x017b, B:41:0x0187, B:25:0x014f, B:21:0x0118, B:53:0x01ab), top: B:61:0x000c }] */
    /* JADX WARN: Removed duplicated region for block: B:35:0x0173 A[Catch: SQLiteException -> 0x019e, all -> 0x01c2, TryCatch #1 {all -> 0x01c2, blocks: (B:4:0x0058, B:9:0x0064, B:11:0x0068, B:13:0x00c9, B:18:0x00d3, B:22:0x011d, B:26:0x0153, B:28:0x015e, B:33:0x0168, B:35:0x0173, B:39:0x017b, B:41:0x0187, B:25:0x014f, B:21:0x0118, B:53:0x01ab), top: B:61:0x000c }] */
    /* JADX WARN: Removed duplicated region for block: B:41:0x0187 A[Catch: SQLiteException -> 0x019e, all -> 0x01c2, TRY_LEAVE, TryCatch #1 {all -> 0x01c2, blocks: (B:4:0x0058, B:9:0x0064, B:11:0x0068, B:13:0x00c9, B:18:0x00d3, B:22:0x011d, B:26:0x0153, B:28:0x015e, B:33:0x0168, B:35:0x0173, B:39:0x017b, B:41:0x0187, B:25:0x014f, B:21:0x0118, B:53:0x01ab), top: B:61:0x000c }] */
    /* JADX WARN: Removed duplicated region for block: B:43:0x019a  */
    /* JADX WARN: Removed duplicated region for block: B:59:0x01c5  */
    @androidx.annotation.WorkerThread
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public final com.google.android.gms.internal.measurement.zzdy zzbc(java.lang.String r31) {
        /*
            Method dump skipped, instructions count: 457
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.android.gms.internal.measurement.zzei.zzbc(java.lang.String):com.google.android.gms.internal.measurement.zzdy");
    }

    public final long zzbd(String str) {
        Preconditions.checkNotEmpty(str);
        zzab();
        zzch();
        try {
            return getWritableDatabase().delete("raw_events", "rowid in (select rowid from raw_events where app_id=? order by rowid desc limit -1 offset ?)", new String[]{str, String.valueOf(Math.max(0, Math.min(1000000, zzgg().zzb(str, zzew.zzagx))))});
        } catch (SQLiteException e) {
            zzge().zzim().zze("Error deleting over the limit events. appId", zzfg.zzbm(str), e);
            return 0L;
        }
    }

    /* JADX WARN: Removed duplicated region for block: B:27:0x0073  */
    @androidx.annotation.WorkerThread
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public final byte[] zzbe(java.lang.String r11) {
        /*
            r10 = this;
            com.google.android.gms.common.internal.Preconditions.checkNotEmpty(r11)
            r10.zzab()
            r10.zzch()
            r0 = 0
            android.database.sqlite.SQLiteDatabase r1 = r10.getWritableDatabase()     // Catch: java.lang.Throwable -> L54 android.database.sqlite.SQLiteException -> L57
            java.lang.String r2 = "apps"
            java.lang.String r3 = "remote_config"
            java.lang.String[] r3 = new java.lang.String[]{r3}     // Catch: java.lang.Throwable -> L54 android.database.sqlite.SQLiteException -> L57
            java.lang.String r4 = "app_id=?"
            r5 = 1
            java.lang.String[] r5 = new java.lang.String[r5]     // Catch: java.lang.Throwable -> L54 android.database.sqlite.SQLiteException -> L57
            r9 = 0
            r5[r9] = r11     // Catch: java.lang.Throwable -> L54 android.database.sqlite.SQLiteException -> L57
            r6 = 0
            r7 = 0
            r8 = 0
            android.database.Cursor r1 = r1.query(r2, r3, r4, r5, r6, r7, r8)     // Catch: java.lang.Throwable -> L54 android.database.sqlite.SQLiteException -> L57
            boolean r2 = r1.moveToFirst()     // Catch: android.database.sqlite.SQLiteException -> L52 java.lang.Throwable -> L70
            if (r2 != 0) goto L31
            if (r1 == 0) goto L30
            r1.close()
        L30:
            return r0
        L31:
            byte[] r2 = r1.getBlob(r9)     // Catch: android.database.sqlite.SQLiteException -> L52 java.lang.Throwable -> L70
            boolean r3 = r1.moveToNext()     // Catch: android.database.sqlite.SQLiteException -> L52 java.lang.Throwable -> L70
            if (r3 == 0) goto L4c
            com.google.android.gms.internal.measurement.zzfg r3 = r10.zzge()     // Catch: android.database.sqlite.SQLiteException -> L52 java.lang.Throwable -> L70
            com.google.android.gms.internal.measurement.zzfi r3 = r3.zzim()     // Catch: android.database.sqlite.SQLiteException -> L52 java.lang.Throwable -> L70
            java.lang.String r4 = "Got multiple records for app config, expected one. appId"
            java.lang.Object r5 = com.google.android.gms.internal.measurement.zzfg.zzbm(r11)     // Catch: android.database.sqlite.SQLiteException -> L52 java.lang.Throwable -> L70
            r3.zzg(r4, r5)     // Catch: android.database.sqlite.SQLiteException -> L52 java.lang.Throwable -> L70
        L4c:
            if (r1 == 0) goto L51
            r1.close()
        L51:
            return r2
        L52:
            r2 = move-exception
            goto L59
        L54:
            r10 = move-exception
            r1 = r0
            goto L71
        L57:
            r2 = move-exception
            r1 = r0
        L59:
            com.google.android.gms.internal.measurement.zzfg r10 = r10.zzge()     // Catch: java.lang.Throwable -> L70
            com.google.android.gms.internal.measurement.zzfi r10 = r10.zzim()     // Catch: java.lang.Throwable -> L70
            java.lang.String r3 = "Error querying remote config. appId"
            java.lang.Object r11 = com.google.android.gms.internal.measurement.zzfg.zzbm(r11)     // Catch: java.lang.Throwable -> L70
            r10.zze(r3, r11, r2)     // Catch: java.lang.Throwable -> L70
            if (r1 == 0) goto L6f
            r1.close()
        L6f:
            return r0
        L70:
            r10 = move-exception
        L71:
            if (r1 == 0) goto L76
            r1.close()
        L76:
            throw r10
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.android.gms.internal.measurement.zzei.zzbe(java.lang.String):byte[]");
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* JADX WARN: Removed duplicated region for block: B:32:0x0098  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public final java.util.Map<java.lang.Integer, com.google.android.gms.internal.measurement.zzkr> zzbf(java.lang.String r12) {
        /*
            r11 = this;
            r11.zzch()
            r11.zzab()
            com.google.android.gms.common.internal.Preconditions.checkNotEmpty(r12)
            android.database.sqlite.SQLiteDatabase r0 = r11.getWritableDatabase()
            r8 = 0
            java.lang.String r1 = "audience_filter_values"
            java.lang.String r2 = "audience_id"
            java.lang.String r3 = "current_results"
            java.lang.String[] r2 = new java.lang.String[]{r2, r3}     // Catch: java.lang.Throwable -> L79 android.database.sqlite.SQLiteException -> L7c
            java.lang.String r3 = "app_id=?"
            r9 = 1
            java.lang.String[] r4 = new java.lang.String[r9]     // Catch: java.lang.Throwable -> L79 android.database.sqlite.SQLiteException -> L7c
            r10 = 0
            r4[r10] = r12     // Catch: java.lang.Throwable -> L79 android.database.sqlite.SQLiteException -> L7c
            r5 = 0
            r6 = 0
            r7 = 0
            android.database.Cursor r0 = r0.query(r1, r2, r3, r4, r5, r6, r7)     // Catch: java.lang.Throwable -> L79 android.database.sqlite.SQLiteException -> L7c
            boolean r1 = r0.moveToFirst()     // Catch: android.database.sqlite.SQLiteException -> L77 java.lang.Throwable -> L95
            if (r1 != 0) goto L33
            if (r0 == 0) goto L32
            r0.close()
        L32:
            return r8
        L33:
            androidx.collection.ArrayMap r1 = new androidx.collection.ArrayMap     // Catch: android.database.sqlite.SQLiteException -> L77 java.lang.Throwable -> L95
            r1.<init>()     // Catch: android.database.sqlite.SQLiteException -> L77 java.lang.Throwable -> L95
        L38:
            int r2 = r0.getInt(r10)     // Catch: android.database.sqlite.SQLiteException -> L77 java.lang.Throwable -> L95
            byte[] r3 = r0.getBlob(r9)     // Catch: android.database.sqlite.SQLiteException -> L77 java.lang.Throwable -> L95
            int r4 = r3.length     // Catch: android.database.sqlite.SQLiteException -> L77 java.lang.Throwable -> L95
            com.google.android.gms.internal.measurement.zzabv r3 = com.google.android.gms.internal.measurement.zzabv.zza(r3, r10, r4)     // Catch: android.database.sqlite.SQLiteException -> L77 java.lang.Throwable -> L95
            com.google.android.gms.internal.measurement.zzkr r4 = new com.google.android.gms.internal.measurement.zzkr     // Catch: android.database.sqlite.SQLiteException -> L77 java.lang.Throwable -> L95
            r4.<init>()     // Catch: android.database.sqlite.SQLiteException -> L77 java.lang.Throwable -> L95
            r4.zzb(r3)     // Catch: java.io.IOException -> L55 android.database.sqlite.SQLiteException -> L77 java.lang.Throwable -> L95
            java.lang.Integer r2 = java.lang.Integer.valueOf(r2)     // Catch: android.database.sqlite.SQLiteException -> L77 java.lang.Throwable -> L95
            r1.put(r2, r4)     // Catch: android.database.sqlite.SQLiteException -> L77 java.lang.Throwable -> L95
            goto L6b
        L55:
            r3 = move-exception
            com.google.android.gms.internal.measurement.zzfg r4 = r11.zzge()     // Catch: android.database.sqlite.SQLiteException -> L77 java.lang.Throwable -> L95
            com.google.android.gms.internal.measurement.zzfi r4 = r4.zzim()     // Catch: android.database.sqlite.SQLiteException -> L77 java.lang.Throwable -> L95
            java.lang.String r5 = "Failed to merge filter results. appId, audienceId, error"
            java.lang.Object r6 = com.google.android.gms.internal.measurement.zzfg.zzbm(r12)     // Catch: android.database.sqlite.SQLiteException -> L77 java.lang.Throwable -> L95
            java.lang.Integer r2 = java.lang.Integer.valueOf(r2)     // Catch: android.database.sqlite.SQLiteException -> L77 java.lang.Throwable -> L95
            r4.zzd(r5, r6, r2, r3)     // Catch: android.database.sqlite.SQLiteException -> L77 java.lang.Throwable -> L95
        L6b:
            boolean r2 = r0.moveToNext()     // Catch: android.database.sqlite.SQLiteException -> L77 java.lang.Throwable -> L95
            if (r2 != 0) goto L38
            if (r0 == 0) goto L76
            r0.close()
        L76:
            return r1
        L77:
            r1 = move-exception
            goto L7e
        L79:
            r11 = move-exception
            r0 = r8
            goto L96
        L7c:
            r1 = move-exception
            r0 = r8
        L7e:
            com.google.android.gms.internal.measurement.zzfg r11 = r11.zzge()     // Catch: java.lang.Throwable -> L95
            com.google.android.gms.internal.measurement.zzfi r11 = r11.zzim()     // Catch: java.lang.Throwable -> L95
            java.lang.String r2 = "Database error querying filter results. appId"
            java.lang.Object r12 = com.google.android.gms.internal.measurement.zzfg.zzbm(r12)     // Catch: java.lang.Throwable -> L95
            r11.zze(r2, r12, r1)     // Catch: java.lang.Throwable -> L95
            if (r0 == 0) goto L94
            r0.close()
        L94:
            return r8
        L95:
            r11 = move-exception
        L96:
            if (r0 == 0) goto L9b
            r0.close()
        L9b:
            throw r11
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.android.gms.internal.measurement.zzei.zzbf(java.lang.String):java.util.Map");
    }

    public final long zzbg(String str) {
        Preconditions.checkNotEmpty(str);
        return zza("select count(1) from events where app_id=? and name not like '!_%' escape '!'", new String[]{str}, 0L);
    }

    @WorkerThread
    public final List<zzed> zzc(String str, String str2, String str3) {
        Preconditions.checkNotEmpty(str);
        zzab();
        zzch();
        ArrayList arrayList = new ArrayList(3);
        arrayList.add(str);
        StringBuilder sb = new StringBuilder("app_id=?");
        if (!TextUtils.isEmpty(str2)) {
            arrayList.add(str2);
            sb.append(" and origin=?");
        }
        if (!TextUtils.isEmpty(str3)) {
            arrayList.add(String.valueOf(str3).concat("*"));
            sb.append(" and name glob ?");
        }
        return zzb(sb.toString(), (String[]) arrayList.toArray(new String[arrayList.size()]));
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @VisibleForTesting
    @WorkerThread
    public final void zzc(List<Long> list) {
        zzab();
        zzch();
        Preconditions.checkNotNull(list);
        Preconditions.checkNotZero(list.size());
        if (zzhv()) {
            String join = TextUtils.join(",", list);
            StringBuilder sb = new StringBuilder(String.valueOf(join).length() + 2);
            sb.append("(");
            sb.append(join);
            sb.append(")");
            String sb2 = sb.toString();
            StringBuilder sb3 = new StringBuilder(String.valueOf(sb2).length() + 80);
            sb3.append("SELECT COUNT(1) FROM queue WHERE rowid IN ");
            sb3.append(sb2);
            sb3.append(" AND retry_count =  2147483647 LIMIT 1");
            if (zza(sb3.toString(), (String[]) null) > 0) {
                zzge().zzip().log("The number of upload retries exceeds the limit. Will remain unchanged.");
            }
            try {
                SQLiteDatabase writableDatabase = getWritableDatabase();
                StringBuilder sb4 = new StringBuilder(String.valueOf(sb2).length() + 127);
                sb4.append("UPDATE queue SET retry_count = IFNULL(retry_count, 0) + 1 WHERE rowid IN ");
                sb4.append(sb2);
                sb4.append(" AND (retry_count IS NULL OR retry_count < 2147483647)");
                writableDatabase.execSQL(sb4.toString());
            } catch (SQLiteException e) {
                zzge().zzim().zzg("Error incrementing retry count. error", e);
            }
        }
    }

    @WorkerThread
    public final zzeq zzf(String str, String str2) {
        Cursor cursor;
        Cursor cursor2;
        Cursor query;
        Boolean bool;
        Preconditions.checkNotEmpty(str);
        Preconditions.checkNotEmpty(str2);
        zzab();
        zzch();
        try {
            try {
                query = getWritableDatabase().query("events", new String[]{"lifetime_count", "current_bundle_count", "last_fire_timestamp", "last_bundled_timestamp", "last_sampled_complex_event_id", "last_sampling_rate", "last_exempt_from_sampling"}, "app_id=? and name=?", new String[]{str, str2}, null, null, null);
            } catch (SQLiteException e) {
                e = e;
                cursor2 = null;
            } catch (Throwable th) {
                th = th;
                cursor = null;
            }
            try {
                if (!query.moveToFirst()) {
                    if (query != null) {
                        query.close();
                    }
                    return null;
                }
                long j = query.getLong(0);
                long j2 = query.getLong(1);
                long j3 = query.getLong(2);
                long j4 = query.isNull(3) ? 0L : query.getLong(3);
                Long valueOf = query.isNull(4) ? null : Long.valueOf(query.getLong(4));
                Long valueOf2 = query.isNull(5) ? null : Long.valueOf(query.getLong(5));
                if (query.isNull(6)) {
                    bool = null;
                } else {
                    bool = Boolean.valueOf(query.getLong(6) == 1);
                }
                cursor2 = query;
                try {
                    zzeq zzeqVar = new zzeq(str, str2, j, j2, j3, j4, valueOf, valueOf2, bool);
                    if (cursor2.moveToNext()) {
                        zzge().zzim().zzg("Got multiple records for event aggregates, expected one. appId", zzfg.zzbm(str));
                    }
                    if (cursor2 != null) {
                        cursor2.close();
                    }
                    return zzeqVar;
                } catch (SQLiteException e2) {
                    e = e2;
                    zzge().zzim().zzd("Error querying events. appId", zzfg.zzbm(str), zzga().zzbj(str2), e);
                    if (cursor2 != null) {
                        cursor2.close();
                    }
                    return null;
                }
            } catch (SQLiteException e3) {
                e = e3;
                cursor2 = query;
            } catch (Throwable th2) {
                th = th2;
                cursor = query;
                if (cursor != null) {
                    cursor.close();
                }
                throw th;
            }
        } catch (Throwable th3) {
            th = th3;
        }
    }

    @WorkerThread
    public final void zzg(String str, String str2) {
        Preconditions.checkNotEmpty(str);
        Preconditions.checkNotEmpty(str2);
        zzab();
        zzch();
        try {
            zzge().zzit().zzg("Deleted user attribute rows", Integer.valueOf(getWritableDatabase().delete("user_attributes", "app_id=? and name=?", new String[]{str, str2})));
        } catch (SQLiteException e) {
            zzge().zzim().zzd("Error deleting user attribute. appId", zzfg.zzbm(str), zzga().zzbl(str2), e);
        }
    }

    @WorkerThread
    public final zzjz zzh(String str, String str2) {
        Cursor cursor;
        Preconditions.checkNotEmpty(str);
        Preconditions.checkNotEmpty(str2);
        zzab();
        zzch();
        try {
            cursor = getWritableDatabase().query("user_attributes", new String[]{"set_timestamp", FirebaseAnalytics.Param.VALUE, FirebaseAnalytics.Param.ORIGIN}, "app_id=? and name=?", new String[]{str, str2}, null, null, null);
            try {
                try {
                    if (!cursor.moveToFirst()) {
                        if (cursor != null) {
                            cursor.close();
                        }
                        return null;
                    }
                    try {
                        zzjz zzjzVar = new zzjz(str, cursor.getString(2), str2, cursor.getLong(0), zza(cursor, 1));
                        if (cursor.moveToNext()) {
                            zzge().zzim().zzg("Got multiple records for user property, expected one. appId", zzfg.zzbm(str));
                        }
                        if (cursor != null) {
                            cursor.close();
                        }
                        return zzjzVar;
                    } catch (SQLiteException e) {
                        e = e;
                        zzge().zzim().zzd("Error querying user property. appId", zzfg.zzbm(str), zzga().zzbl(str2), e);
                        if (cursor != null) {
                            cursor.close();
                        }
                        return null;
                    }
                } catch (Throwable th) {
                    th = th;
                    if (cursor != null) {
                        cursor.close();
                    }
                    throw th;
                }
            } catch (SQLiteException e2) {
                e = e2;
            }
        } catch (SQLiteException e3) {
            e = e3;
            cursor = null;
        } catch (Throwable th2) {
            th = th2;
            cursor = null;
        }
    }

    @Override // com.google.android.gms.internal.measurement.zzjq
    protected final boolean zzhf() {
        return false;
    }

    /* JADX WARN: Removed duplicated region for block: B:25:0x003f  */
    @androidx.annotation.WorkerThread
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public final java.lang.String zzhn() {
        /*
            r4 = this;
            android.database.sqlite.SQLiteDatabase r0 = r4.getWritableDatabase()
            r1 = 0
            java.lang.String r2 = "select app_id from queue order by has_realtime desc, rowid asc limit 1;"
            android.database.Cursor r0 = r0.rawQuery(r2, r1)     // Catch: java.lang.Throwable -> L24 android.database.sqlite.SQLiteException -> L27
            boolean r2 = r0.moveToFirst()     // Catch: android.database.sqlite.SQLiteException -> L22 java.lang.Throwable -> L3c
            if (r2 == 0) goto L1c
            r2 = 0
            java.lang.String r2 = r0.getString(r2)     // Catch: android.database.sqlite.SQLiteException -> L22 java.lang.Throwable -> L3c
            if (r0 == 0) goto L1b
            r0.close()
        L1b:
            return r2
        L1c:
            if (r0 == 0) goto L21
            r0.close()
        L21:
            return r1
        L22:
            r2 = move-exception
            goto L29
        L24:
            r4 = move-exception
            r0 = r1
            goto L3d
        L27:
            r2 = move-exception
            r0 = r1
        L29:
            com.google.android.gms.internal.measurement.zzfg r4 = r4.zzge()     // Catch: java.lang.Throwable -> L3c
            com.google.android.gms.internal.measurement.zzfi r4 = r4.zzim()     // Catch: java.lang.Throwable -> L3c
            java.lang.String r3 = "Database error getting next bundle app id"
            r4.zzg(r3, r2)     // Catch: java.lang.Throwable -> L3c
            if (r0 == 0) goto L3b
            r0.close()
        L3b:
            return r1
        L3c:
            r4 = move-exception
        L3d:
            if (r0 == 0) goto L42
            r0.close()
        L42:
            throw r4
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.android.gms.internal.measurement.zzei.zzhn():java.lang.String");
    }

    public final boolean zzho() {
        return zza("select count(1) > 0 from queue where has_realtime = 1", (String[]) null) != 0;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @WorkerThread
    public final void zzhp() {
        int delete;
        zzab();
        zzch();
        if (zzhv()) {
            long j = zzgf().zzajx.get();
            long elapsedRealtime = zzbt().elapsedRealtime();
            if (Math.abs(elapsedRealtime - j) > zzew.zzahg.get().longValue()) {
                zzgf().zzajx.set(elapsedRealtime);
                zzab();
                zzch();
                if (!zzhv() || (delete = getWritableDatabase().delete("queue", "abs(bundle_end_timestamp - ?) > cast(? as integer)", new String[]{String.valueOf(zzbt().currentTimeMillis()), String.valueOf(zzef.zzhh())})) <= 0) {
                    return;
                }
                zzge().zzit().zzg("Deleted stale rows. rowsDeleted", Integer.valueOf(delete));
            }
        }
    }

    @WorkerThread
    public final long zzhq() {
        return zza("select max(bundle_end_timestamp) from queue", (String[]) null, 0L);
    }

    @WorkerThread
    public final long zzhr() {
        return zza("select max(timestamp) from raw_events", (String[]) null, 0L);
    }

    public final boolean zzhs() {
        return zza("select count(1) > 0 from raw_events", (String[]) null) != 0;
    }

    public final boolean zzht() {
        return zza("select count(1) > 0 from raw_events where realtime = 1", (String[]) null) != 0;
    }

    public final long zzhu() {
        Cursor cursor = null;
        try {
            try {
                Cursor rawQuery = getWritableDatabase().rawQuery("select rowid from raw_events order by rowid desc limit 1;", null);
                try {
                    if (!rawQuery.moveToFirst()) {
                        if (rawQuery != null) {
                            rawQuery.close();
                        }
                        return -1L;
                    }
                    long j = rawQuery.getLong(0);
                    if (rawQuery != null) {
                        rawQuery.close();
                    }
                    return j;
                } catch (SQLiteException e) {
                    e = e;
                    cursor = rawQuery;
                    zzge().zzim().zzg("Error querying raw events", e);
                    if (cursor != null) {
                        cursor.close();
                    }
                    return -1L;
                } catch (Throwable th) {
                    th = th;
                    cursor = rawQuery;
                    if (cursor != null) {
                        cursor.close();
                    }
                    throw th;
                }
            } catch (SQLiteException e2) {
                e = e2;
            }
        } catch (Throwable th2) {
            th = th2;
        }
    }

    /* JADX WARN: Removed duplicated region for block: B:35:0x0126  */
    @androidx.annotation.WorkerThread
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public final com.google.android.gms.internal.measurement.zzed zzi(java.lang.String r30, java.lang.String r31) {
        /*
            Method dump skipped, instructions count: 298
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.android.gms.internal.measurement.zzei.zzi(java.lang.String, java.lang.String):com.google.android.gms.internal.measurement.zzed");
    }

    @WorkerThread
    public final int zzj(String str, String str2) {
        Preconditions.checkNotEmpty(str);
        Preconditions.checkNotEmpty(str2);
        zzab();
        zzch();
        try {
            return getWritableDatabase().delete("conditional_properties", "app_id=? and name=?", new String[]{str, str2});
        } catch (SQLiteException e) {
            zzge().zzim().zzd("Error deleting conditional property", zzfg.zzbm(str), zzga().zzbl(str2), e);
            return 0;
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Removed duplicated region for block: B:35:0x00b2  */
    /* JADX WARN: Type inference failed for: r14v1 */
    /* JADX WARN: Type inference failed for: r14v3, types: [android.database.Cursor] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public final java.util.Map<java.lang.Integer, java.util.List<com.google.android.gms.internal.measurement.zzke>> zzk(java.lang.String r13, java.lang.String r14) {
        /*
            r12 = this;
            r12.zzch()
            r12.zzab()
            com.google.android.gms.common.internal.Preconditions.checkNotEmpty(r13)
            com.google.android.gms.common.internal.Preconditions.checkNotEmpty(r14)
            androidx.collection.ArrayMap r0 = new androidx.collection.ArrayMap
            r0.<init>()
            android.database.sqlite.SQLiteDatabase r1 = r12.getWritableDatabase()
            r9 = 0
            java.lang.String r2 = "event_filters"
            java.lang.String r3 = "audience_id"
            java.lang.String r4 = "data"
            java.lang.String[] r3 = new java.lang.String[]{r3, r4}     // Catch: java.lang.Throwable -> L93 android.database.sqlite.SQLiteException -> L96
            java.lang.String r4 = "app_id=? AND event_name=?"
            r5 = 2
            java.lang.String[] r5 = new java.lang.String[r5]     // Catch: java.lang.Throwable -> L93 android.database.sqlite.SQLiteException -> L96
            r10 = 0
            r5[r10] = r13     // Catch: java.lang.Throwable -> L93 android.database.sqlite.SQLiteException -> L96
            r11 = 1
            r5[r11] = r14     // Catch: java.lang.Throwable -> L93 android.database.sqlite.SQLiteException -> L96
            r6 = 0
            r7 = 0
            r8 = 0
            android.database.Cursor r14 = r1.query(r2, r3, r4, r5, r6, r7, r8)     // Catch: java.lang.Throwable -> L93 android.database.sqlite.SQLiteException -> L96
            boolean r1 = r14.moveToFirst()     // Catch: android.database.sqlite.SQLiteException -> L91 java.lang.Throwable -> Laf
            if (r1 != 0) goto L42
            java.util.Map r0 = java.util.Collections.emptyMap()     // Catch: android.database.sqlite.SQLiteException -> L91 java.lang.Throwable -> Laf
            if (r14 == 0) goto L41
            r14.close()
        L41:
            return r0
        L42:
            byte[] r1 = r14.getBlob(r11)     // Catch: android.database.sqlite.SQLiteException -> L91 java.lang.Throwable -> Laf
            int r2 = r1.length     // Catch: android.database.sqlite.SQLiteException -> L91 java.lang.Throwable -> Laf
            com.google.android.gms.internal.measurement.zzabv r1 = com.google.android.gms.internal.measurement.zzabv.zza(r1, r10, r2)     // Catch: android.database.sqlite.SQLiteException -> L91 java.lang.Throwable -> Laf
            com.google.android.gms.internal.measurement.zzke r2 = new com.google.android.gms.internal.measurement.zzke     // Catch: android.database.sqlite.SQLiteException -> L91 java.lang.Throwable -> Laf
            r2.<init>()     // Catch: android.database.sqlite.SQLiteException -> L91 java.lang.Throwable -> Laf
            r2.zzb(r1)     // Catch: java.io.IOException -> L73 android.database.sqlite.SQLiteException -> L91 java.lang.Throwable -> Laf
            int r1 = r14.getInt(r10)     // Catch: android.database.sqlite.SQLiteException -> L91 java.lang.Throwable -> Laf
            java.lang.Integer r3 = java.lang.Integer.valueOf(r1)     // Catch: android.database.sqlite.SQLiteException -> L91 java.lang.Throwable -> Laf
            java.lang.Object r3 = r0.get(r3)     // Catch: android.database.sqlite.SQLiteException -> L91 java.lang.Throwable -> Laf
            java.util.List r3 = (java.util.List) r3     // Catch: android.database.sqlite.SQLiteException -> L91 java.lang.Throwable -> Laf
            if (r3 != 0) goto L6f
            java.util.ArrayList r3 = new java.util.ArrayList     // Catch: android.database.sqlite.SQLiteException -> L91 java.lang.Throwable -> Laf
            r3.<init>()     // Catch: android.database.sqlite.SQLiteException -> L91 java.lang.Throwable -> Laf
            java.lang.Integer r1 = java.lang.Integer.valueOf(r1)     // Catch: android.database.sqlite.SQLiteException -> L91 java.lang.Throwable -> Laf
            r0.put(r1, r3)     // Catch: android.database.sqlite.SQLiteException -> L91 java.lang.Throwable -> Laf
        L6f:
            r3.add(r2)     // Catch: android.database.sqlite.SQLiteException -> L91 java.lang.Throwable -> Laf
            goto L85
        L73:
            r1 = move-exception
            com.google.android.gms.internal.measurement.zzfg r2 = r12.zzge()     // Catch: android.database.sqlite.SQLiteException -> L91 java.lang.Throwable -> Laf
            com.google.android.gms.internal.measurement.zzfi r2 = r2.zzim()     // Catch: android.database.sqlite.SQLiteException -> L91 java.lang.Throwable -> Laf
            java.lang.String r3 = "Failed to merge filter. appId"
            java.lang.Object r4 = com.google.android.gms.internal.measurement.zzfg.zzbm(r13)     // Catch: android.database.sqlite.SQLiteException -> L91 java.lang.Throwable -> Laf
            r2.zze(r3, r4, r1)     // Catch: android.database.sqlite.SQLiteException -> L91 java.lang.Throwable -> Laf
        L85:
            boolean r1 = r14.moveToNext()     // Catch: android.database.sqlite.SQLiteException -> L91 java.lang.Throwable -> Laf
            if (r1 != 0) goto L42
            if (r14 == 0) goto L90
            r14.close()
        L90:
            return r0
        L91:
            r0 = move-exception
            goto L98
        L93:
            r12 = move-exception
            r14 = r9
            goto Lb0
        L96:
            r0 = move-exception
            r14 = r9
        L98:
            com.google.android.gms.internal.measurement.zzfg r12 = r12.zzge()     // Catch: java.lang.Throwable -> Laf
            com.google.android.gms.internal.measurement.zzfi r12 = r12.zzim()     // Catch: java.lang.Throwable -> Laf
            java.lang.String r1 = "Database error querying filters. appId"
            java.lang.Object r13 = com.google.android.gms.internal.measurement.zzfg.zzbm(r13)     // Catch: java.lang.Throwable -> Laf
            r12.zze(r1, r13, r0)     // Catch: java.lang.Throwable -> Laf
            if (r14 == 0) goto Lae
            r14.close()
        Lae:
            return r9
        Laf:
            r12 = move-exception
        Lb0:
            if (r14 == 0) goto Lb5
            r14.close()
        Lb5:
            throw r12
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.android.gms.internal.measurement.zzei.zzk(java.lang.String, java.lang.String):java.util.Map");
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Removed duplicated region for block: B:35:0x00b2  */
    /* JADX WARN: Type inference failed for: r14v1 */
    /* JADX WARN: Type inference failed for: r14v3, types: [android.database.Cursor] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public final java.util.Map<java.lang.Integer, java.util.List<com.google.android.gms.internal.measurement.zzkh>> zzl(java.lang.String r13, java.lang.String r14) {
        /*
            r12 = this;
            r12.zzch()
            r12.zzab()
            com.google.android.gms.common.internal.Preconditions.checkNotEmpty(r13)
            com.google.android.gms.common.internal.Preconditions.checkNotEmpty(r14)
            androidx.collection.ArrayMap r0 = new androidx.collection.ArrayMap
            r0.<init>()
            android.database.sqlite.SQLiteDatabase r1 = r12.getWritableDatabase()
            r9 = 0
            java.lang.String r2 = "property_filters"
            java.lang.String r3 = "audience_id"
            java.lang.String r4 = "data"
            java.lang.String[] r3 = new java.lang.String[]{r3, r4}     // Catch: java.lang.Throwable -> L93 android.database.sqlite.SQLiteException -> L96
            java.lang.String r4 = "app_id=? AND property_name=?"
            r5 = 2
            java.lang.String[] r5 = new java.lang.String[r5]     // Catch: java.lang.Throwable -> L93 android.database.sqlite.SQLiteException -> L96
            r10 = 0
            r5[r10] = r13     // Catch: java.lang.Throwable -> L93 android.database.sqlite.SQLiteException -> L96
            r11 = 1
            r5[r11] = r14     // Catch: java.lang.Throwable -> L93 android.database.sqlite.SQLiteException -> L96
            r6 = 0
            r7 = 0
            r8 = 0
            android.database.Cursor r14 = r1.query(r2, r3, r4, r5, r6, r7, r8)     // Catch: java.lang.Throwable -> L93 android.database.sqlite.SQLiteException -> L96
            boolean r1 = r14.moveToFirst()     // Catch: android.database.sqlite.SQLiteException -> L91 java.lang.Throwable -> Laf
            if (r1 != 0) goto L42
            java.util.Map r0 = java.util.Collections.emptyMap()     // Catch: android.database.sqlite.SQLiteException -> L91 java.lang.Throwable -> Laf
            if (r14 == 0) goto L41
            r14.close()
        L41:
            return r0
        L42:
            byte[] r1 = r14.getBlob(r11)     // Catch: android.database.sqlite.SQLiteException -> L91 java.lang.Throwable -> Laf
            int r2 = r1.length     // Catch: android.database.sqlite.SQLiteException -> L91 java.lang.Throwable -> Laf
            com.google.android.gms.internal.measurement.zzabv r1 = com.google.android.gms.internal.measurement.zzabv.zza(r1, r10, r2)     // Catch: android.database.sqlite.SQLiteException -> L91 java.lang.Throwable -> Laf
            com.google.android.gms.internal.measurement.zzkh r2 = new com.google.android.gms.internal.measurement.zzkh     // Catch: android.database.sqlite.SQLiteException -> L91 java.lang.Throwable -> Laf
            r2.<init>()     // Catch: android.database.sqlite.SQLiteException -> L91 java.lang.Throwable -> Laf
            r2.zzb(r1)     // Catch: java.io.IOException -> L73 android.database.sqlite.SQLiteException -> L91 java.lang.Throwable -> Laf
            int r1 = r14.getInt(r10)     // Catch: android.database.sqlite.SQLiteException -> L91 java.lang.Throwable -> Laf
            java.lang.Integer r3 = java.lang.Integer.valueOf(r1)     // Catch: android.database.sqlite.SQLiteException -> L91 java.lang.Throwable -> Laf
            java.lang.Object r3 = r0.get(r3)     // Catch: android.database.sqlite.SQLiteException -> L91 java.lang.Throwable -> Laf
            java.util.List r3 = (java.util.List) r3     // Catch: android.database.sqlite.SQLiteException -> L91 java.lang.Throwable -> Laf
            if (r3 != 0) goto L6f
            java.util.ArrayList r3 = new java.util.ArrayList     // Catch: android.database.sqlite.SQLiteException -> L91 java.lang.Throwable -> Laf
            r3.<init>()     // Catch: android.database.sqlite.SQLiteException -> L91 java.lang.Throwable -> Laf
            java.lang.Integer r1 = java.lang.Integer.valueOf(r1)     // Catch: android.database.sqlite.SQLiteException -> L91 java.lang.Throwable -> Laf
            r0.put(r1, r3)     // Catch: android.database.sqlite.SQLiteException -> L91 java.lang.Throwable -> Laf
        L6f:
            r3.add(r2)     // Catch: android.database.sqlite.SQLiteException -> L91 java.lang.Throwable -> Laf
            goto L85
        L73:
            r1 = move-exception
            com.google.android.gms.internal.measurement.zzfg r2 = r12.zzge()     // Catch: android.database.sqlite.SQLiteException -> L91 java.lang.Throwable -> Laf
            com.google.android.gms.internal.measurement.zzfi r2 = r2.zzim()     // Catch: android.database.sqlite.SQLiteException -> L91 java.lang.Throwable -> Laf
            java.lang.String r3 = "Failed to merge filter"
            java.lang.Object r4 = com.google.android.gms.internal.measurement.zzfg.zzbm(r13)     // Catch: android.database.sqlite.SQLiteException -> L91 java.lang.Throwable -> Laf
            r2.zze(r3, r4, r1)     // Catch: android.database.sqlite.SQLiteException -> L91 java.lang.Throwable -> Laf
        L85:
            boolean r1 = r14.moveToNext()     // Catch: android.database.sqlite.SQLiteException -> L91 java.lang.Throwable -> Laf
            if (r1 != 0) goto L42
            if (r14 == 0) goto L90
            r14.close()
        L90:
            return r0
        L91:
            r0 = move-exception
            goto L98
        L93:
            r12 = move-exception
            r14 = r9
            goto Lb0
        L96:
            r0 = move-exception
            r14 = r9
        L98:
            com.google.android.gms.internal.measurement.zzfg r12 = r12.zzge()     // Catch: java.lang.Throwable -> Laf
            com.google.android.gms.internal.measurement.zzfi r12 = r12.zzim()     // Catch: java.lang.Throwable -> Laf
            java.lang.String r1 = "Database error querying filters. appId"
            java.lang.Object r13 = com.google.android.gms.internal.measurement.zzfg.zzbm(r13)     // Catch: java.lang.Throwable -> Laf
            r12.zze(r1, r13, r0)     // Catch: java.lang.Throwable -> Laf
            if (r14 == 0) goto Lae
            r14.close()
        Lae:
            return r9
        Laf:
            r12 = move-exception
        Lb0:
            if (r14 == 0) goto Lb5
            r14.close()
        Lb5:
            throw r12
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.android.gms.internal.measurement.zzei.zzl(java.lang.String, java.lang.String):java.util.Map");
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @VisibleForTesting
    @WorkerThread
    public final long zzm(String str, String str2) {
        long j;
        ContentValues contentValues;
        Preconditions.checkNotEmpty(str);
        Preconditions.checkNotEmpty(str2);
        zzab();
        zzch();
        SQLiteDatabase writableDatabase = getWritableDatabase();
        writableDatabase.beginTransaction();
        try {
            try {
                StringBuilder sb = new StringBuilder(String.valueOf(str2).length() + 32);
                sb.append("select ");
                sb.append(str2);
                sb.append(" from app2 where app_id=?");
                j = zza(sb.toString(), new String[]{str}, -1L);
                if (j == -1) {
                    ContentValues contentValues2 = new ContentValues();
                    contentValues2.put("app_id", str);
                    contentValues2.put("first_open_count", (Integer) 0);
                    contentValues2.put("previous_install_count", (Integer) 0);
                    if (writableDatabase.insertWithOnConflict("app2", null, contentValues2, 5) == -1) {
                        zzge().zzim().zze("Failed to insert column (got -1). appId", zzfg.zzbm(str), str2);
                        return -1L;
                    }
                    j = 0;
                }
                try {
                    contentValues = new ContentValues();
                    contentValues.put("app_id", str);
                    contentValues.put(str2, Long.valueOf(1 + j));
                } catch (SQLiteException e) {
                    e = e;
                    zzge().zzim().zzd("Error inserting column. appId", zzfg.zzbm(str), str2, e);
                    return j;
                }
            } catch (SQLiteException e2) {
                e = e2;
                j = 0;
            }
            if (writableDatabase.update("app2", contentValues, "app_id = ?", new String[]{str}) == 0) {
                zzge().zzim().zze("Failed to update column (got 0). appId", zzfg.zzbm(str), str2);
                return -1L;
            }
            writableDatabase.setTransactionSuccessful();
            return j;
        } finally {
            writableDatabase.endTransaction();
        }
    }
}
