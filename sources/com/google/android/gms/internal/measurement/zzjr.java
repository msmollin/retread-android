package com.google.android.gms.internal.measurement;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Pair;
import androidx.annotation.NonNull;
import androidx.annotation.Size;
import androidx.annotation.WorkerThread;
import androidx.collection.ArrayMap;
import com.facebook.appevents.codeless.internal.Constants;
import com.facebook.internal.AnalyticsEvents;
import com.github.mikephil.charting.utils.Utils;
import com.google.android.gms.common.internal.Preconditions;
import com.google.android.gms.common.util.Clock;
import com.google.android.gms.common.util.VisibleForTesting;
import com.google.android.gms.common.wrappers.Wrappers;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.treadly.Treadly.Data.Managers.TreadlyEventHelper;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.FileLock;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import org.joda.time.DateTimeConstants;

/* loaded from: classes.dex */
public class zzjr implements zzec {
    private zzgl zzacw;
    zzgf zzaqa;
    zzfk zzaqb;
    private zzei zzaqc;
    private zzfp zzaqd;
    private zzjn zzaqe;
    private zzeb zzaqf;
    private boolean zzaqg;
    @VisibleForTesting
    private long zzaqh;
    private List<Runnable> zzaqi;
    private int zzaqj;
    private int zzaqk;
    private boolean zzaql;
    private boolean zzaqm;
    private boolean zzaqn;
    private FileLock zzaqo;
    private FileChannel zzaqp;
    private List<Long> zzaqq;
    private List<Long> zzaqr;
    long zzaqs;
    private boolean zzvo = false;

    @VisibleForTesting
    @WorkerThread
    private final int zza(FileChannel fileChannel) {
        zzab();
        if (fileChannel == null || !fileChannel.isOpen()) {
            zzge().zzim().log("Bad channel to read from");
            return 0;
        }
        ByteBuffer allocate = ByteBuffer.allocate(4);
        try {
            fileChannel.position(0L);
            int read = fileChannel.read(allocate);
            if (read == 4) {
                allocate.flip();
                return allocate.getInt();
            }
            if (read != -1) {
                zzge().zzip().zzg("Unexpected data length. Bytes read", Integer.valueOf(read));
            }
            return 0;
        } catch (IOException e) {
            zzge().zzim().zzg("Failed to read from channel", e);
            return 0;
        }
    }

    private final zzdz zza(Context context, String str, String str2, boolean z, boolean z2, boolean z3, long j) {
        int i;
        String str3 = AnalyticsEvents.PARAMETER_DIALOG_OUTCOME_VALUE_UNKNOWN;
        String str4 = AnalyticsEvents.PARAMETER_DIALOG_OUTCOME_VALUE_UNKNOWN;
        String str5 = AnalyticsEvents.PARAMETER_DIALOG_OUTCOME_VALUE_UNKNOWN;
        PackageManager packageManager = context.getPackageManager();
        if (packageManager == null) {
            zzge().zzim().log("PackageManager is null, can not log app install information");
            return null;
        }
        try {
            str3 = packageManager.getInstallerPackageName(str);
        } catch (IllegalArgumentException unused) {
            zzge().zzim().zzg("Error retrieving installer package name. appId", zzfg.zzbm(str));
        }
        if (str3 == null) {
            str3 = "manual_install";
        } else if ("com.android.vending".equals(str3)) {
            str3 = "";
        }
        String str6 = str3;
        try {
            PackageInfo packageInfo = Wrappers.packageManager(context).getPackageInfo(str, 0);
            if (packageInfo != null) {
                CharSequence applicationLabel = Wrappers.packageManager(context).getApplicationLabel(str);
                if (!TextUtils.isEmpty(applicationLabel)) {
                    str5 = applicationLabel.toString();
                }
                str4 = packageInfo.versionName;
                i = packageInfo.versionCode;
            } else {
                i = Integer.MIN_VALUE;
            }
            return new zzdz(str, str2, str4, i, str6, 12451L, zzgb().zzd(context, str), (String) null, z, false, "", 0L, zzgg().zzba(str) ? j : 0L, 0, z2, z3, false);
        } catch (PackageManager.NameNotFoundException unused2) {
            zzge().zzim().zze("Error retrieving newly installed package info. appId, appName", zzfg.zzbm(str), str5);
            return null;
        }
    }

    private static void zza(zzjq zzjqVar) {
        if (zzjqVar == null) {
            throw new IllegalStateException("Upload component not created");
        }
        if (zzjqVar.isInitialized()) {
            return;
        }
        String valueOf = String.valueOf(zzjqVar.getClass());
        StringBuilder sb = new StringBuilder(String.valueOf(valueOf).length() + 27);
        sb.append("Component not initialized: ");
        sb.append(valueOf);
        throw new IllegalStateException(sb.toString());
    }

    @VisibleForTesting
    @WorkerThread
    private final boolean zza(int i, FileChannel fileChannel) {
        zzab();
        if (fileChannel == null || !fileChannel.isOpen()) {
            zzge().zzim().log("Bad channel to read from");
            return false;
        }
        ByteBuffer allocate = ByteBuffer.allocate(4);
        allocate.putInt(i);
        allocate.flip();
        try {
            fileChannel.truncate(0L);
            fileChannel.write(allocate);
            fileChannel.force(true);
            if (fileChannel.size() != 4) {
                zzge().zzim().zzg("Error writing to channel. Bytes written", Long.valueOf(fileChannel.size()));
            }
            return true;
        } catch (IOException e) {
            zzge().zzim().zzg("Failed to write to channel", e);
            return false;
        }
    }

    private final boolean zza(String str, zzeu zzeuVar) {
        long longValue;
        zzjz zzjzVar;
        String string = zzeuVar.zzafq.getString(FirebaseAnalytics.Param.CURRENCY);
        if (FirebaseAnalytics.Event.ECOMMERCE_PURCHASE.equals(zzeuVar.name)) {
            double doubleValue = zzeuVar.zzafq.zzbh(FirebaseAnalytics.Param.VALUE).doubleValue() * 1000000.0d;
            if (doubleValue == Utils.DOUBLE_EPSILON) {
                doubleValue = zzeuVar.zzafq.getLong(FirebaseAnalytics.Param.VALUE).longValue() * 1000000.0d;
            }
            if (doubleValue > 9.223372036854776E18d || doubleValue < -9.223372036854776E18d) {
                zzge().zzip().zze("Data lost. Currency value is too big. appId", zzfg.zzbm(str), Double.valueOf(doubleValue));
                return false;
            }
            longValue = Math.round(doubleValue);
        } else {
            longValue = zzeuVar.zzafq.getLong(FirebaseAnalytics.Param.VALUE).longValue();
        }
        if (!TextUtils.isEmpty(string)) {
            String upperCase = string.toUpperCase(Locale.US);
            if (upperCase.matches("[A-Z]{3}")) {
                String valueOf = String.valueOf("_ltv_");
                String valueOf2 = String.valueOf(upperCase);
                String concat = valueOf2.length() != 0 ? valueOf.concat(valueOf2) : new String(valueOf);
                zzjz zzh = zzix().zzh(str, concat);
                if (zzh == null || !(zzh.value instanceof Long)) {
                    zzei zzix = zzix();
                    int zzb = zzgg().zzb(str, zzew.zzahm) - 1;
                    Preconditions.checkNotEmpty(str);
                    zzix.zzab();
                    zzix.zzch();
                    try {
                        zzix.getWritableDatabase().execSQL("delete from user_attributes where app_id=? and name in (select name from user_attributes where app_id=? and name like '_ltv_%' order by set_timestamp desc limit ?,10);", new String[]{str, str, String.valueOf(zzb)});
                    } catch (SQLiteException e) {
                        zzix.zzge().zzim().zze("Error pruning currencies. appId", zzfg.zzbm(str), e);
                    }
                    zzjzVar = new zzjz(str, zzeuVar.origin, concat, zzbt().currentTimeMillis(), Long.valueOf(longValue));
                } else {
                    zzjzVar = new zzjz(str, zzeuVar.origin, concat, zzbt().currentTimeMillis(), Long.valueOf(((Long) zzh.value).longValue() + longValue));
                }
                if (!zzix().zza(zzjzVar)) {
                    zzge().zzim().zzd("Too many unique user properties are set. Ignoring user property. appId", zzfg.zzbm(str), zzga().zzbl(zzjzVar.name), zzjzVar.value);
                    zzgb().zza(str, 9, (String) null, (String) null, 0);
                }
            }
        }
        return true;
    }

    private final zzkm[] zza(String str, zzks[] zzksVarArr, zzkn[] zzknVarArr) {
        Preconditions.checkNotEmpty(str);
        return zziw().zza(str, zzknVarArr, zzksVarArr);
    }

    @WorkerThread
    private final void zzb(zzdy zzdyVar) {
        ArrayMap arrayMap;
        zzab();
        if (TextUtils.isEmpty(zzdyVar.getGmpAppId())) {
            zzb(zzdyVar.zzah(), TreadlyEventHelper.MESSAGE_ID_USER_BROADCAST_STARTED, null, null, null);
            return;
        }
        String gmpAppId = zzdyVar.getGmpAppId();
        String appInstanceId = zzdyVar.getAppInstanceId();
        Uri.Builder builder = new Uri.Builder();
        Uri.Builder encodedAuthority = builder.scheme(zzew.zzagm.get()).encodedAuthority(zzew.zzagn.get());
        String valueOf = String.valueOf(gmpAppId);
        encodedAuthority.path(valueOf.length() != 0 ? "config/app/".concat(valueOf) : new String("config/app/")).appendQueryParameter("app_instance_id", appInstanceId).appendQueryParameter("platform", Constants.PLATFORM).appendQueryParameter("gmp_version", "12451");
        String uri = builder.build().toString();
        try {
            URL url = new URL(uri);
            zzge().zzit().zzg("Fetching remote configuration", zzdyVar.zzah());
            zzkk zzbu = zzkm().zzbu(zzdyVar.zzah());
            String zzbv = zzkm().zzbv(zzdyVar.zzah());
            if (zzbu == null || TextUtils.isEmpty(zzbv)) {
                arrayMap = null;
            } else {
                ArrayMap arrayMap2 = new ArrayMap();
                arrayMap2.put("If-Modified-Since", zzbv);
                arrayMap = arrayMap2;
            }
            this.zzaql = true;
            zzfk zzkn = zzkn();
            String zzah = zzdyVar.zzah();
            zzjt zzjtVar = new zzjt(this);
            zzkn.zzab();
            zzkn.zzch();
            Preconditions.checkNotNull(url);
            Preconditions.checkNotNull(zzjtVar);
            zzkn.zzgd().zzd(new zzfo(zzkn, zzah, url, null, arrayMap, zzjtVar));
        } catch (MalformedURLException unused) {
            zzge().zzim().zze("Failed to parse config URL. Not fetching. appId", zzfg.zzbm(zzdyVar.zzah()), uri);
        }
    }

    @WorkerThread
    private final Boolean zzc(zzdy zzdyVar) {
        try {
            if (zzdyVar.zzgm() != -2147483648L) {
                if (zzdyVar.zzgm() == Wrappers.packageManager(getContext()).getPackageInfo(zzdyVar.zzah(), 0).versionCode) {
                    return true;
                }
            } else {
                String str = Wrappers.packageManager(getContext()).getPackageInfo(zzdyVar.zzah(), 0).versionName;
                if (zzdyVar.zzag() != null && zzdyVar.zzag().equals(str)) {
                    return true;
                }
            }
            return false;
        } catch (PackageManager.NameNotFoundException unused) {
            return null;
        }
    }

    /* JADX WARN: Code restructure failed: missing block: B:133:0x0544, code lost:
        r4 = true;
     */
    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r2v4 */
    /* JADX WARN: Type inference failed for: r2v5, types: [int] */
    @androidx.annotation.WorkerThread
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    private final void zzc(com.google.android.gms.internal.measurement.zzeu r35, com.google.android.gms.internal.measurement.zzdz r36) {
        /*
            Method dump skipped, instructions count: 1528
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.android.gms.internal.measurement.zzjr.zzc(com.google.android.gms.internal.measurement.zzeu, com.google.android.gms.internal.measurement.zzdz):void");
    }

    /* JADX WARN: Code restructure failed: missing block: B:117:0x027a, code lost:
        if (r9 != 0) goto L394;
     */
    /* JADX WARN: Code restructure failed: missing block: B:24:0x0085, code lost:
        if (r3 != null) goto L18;
     */
    /* JADX WARN: Code restructure failed: missing block: B:74:0x01cc, code lost:
        if (r5 != null) goto L369;
     */
    /* JADX WARN: Code restructure failed: missing block: B:87:0x021b, code lost:
        if (r5 != null) goto L369;
     */
    /* JADX WARN: Code restructure failed: missing block: B:96:0x023d, code lost:
        if (r9 == 0) goto L370;
     */
    /* JADX WARN: Code restructure failed: missing block: B:97:0x023f, code lost:
        r9.close();
     */
    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Removed duplicated region for block: B:121:0x0281 A[Catch: all -> 0x0ad3, TryCatch #11 {all -> 0x0ad3, blocks: (B:3:0x0009, B:25:0x0087, B:119:0x027d, B:121:0x0281, B:127:0x028f, B:128:0x02aa, B:130:0x02b4, B:132:0x02cc, B:134:0x02f9, B:140:0x030d, B:142:0x0317, B:220:0x058d, B:144:0x0332, B:146:0x0342, B:206:0x0536, B:208:0x0540, B:210:0x0544, B:213:0x054a, B:215:0x0557, B:216:0x0569, B:217:0x056d, B:218:0x0573, B:219:0x0586, B:150:0x0352, B:152:0x0356, B:153:0x035b, B:155:0x0364, B:157:0x0376, B:161:0x0390, B:158:0x037e, B:160:0x0388, B:165:0x039f, B:167:0x03db, B:168:0x0415, B:171:0x0447, B:173:0x044c, B:175:0x045a, B:177:0x0463, B:178:0x0469, B:180:0x046c, B:181:0x0475, B:182:0x0478, B:185:0x047f, B:188:0x0489, B:190:0x04ba, B:192:0x04d7, B:198:0x04f4, B:195:0x04e9, B:201:0x04ff, B:203:0x0512, B:204:0x051f, B:221:0x0593, B:223:0x059d, B:225:0x05a9, B:227:0x05b7, B:230:0x05bc, B:232:0x05fa, B:233:0x0618, B:235:0x061d, B:237:0x062b, B:241:0x0637, B:244:0x0657, B:238:0x0631, B:231:0x05df, B:245:0x066d, B:247:0x0687, B:249:0x06a0, B:251:0x06ac, B:253:0x06bf, B:254:0x06ce, B:256:0x06d2, B:258:0x06dc, B:259:0x06eb, B:261:0x06ef, B:263:0x06f7, B:264:0x0708, B:332:0x08cb, B:266:0x0719, B:270:0x0728, B:272:0x072e, B:274:0x073c, B:276:0x0740, B:291:0x0770, B:294:0x0782, B:296:0x079c, B:298:0x07a6, B:300:0x07b6, B:301:0x07ec, B:305:0x07fc, B:307:0x0803, B:309:0x080d, B:311:0x0811, B:313:0x0815, B:315:0x0819, B:316:0x0820, B:317:0x0825, B:319:0x082b, B:321:0x0847, B:322:0x0850, B:323:0x085d, B:325:0x0872, B:327:0x089f, B:328:0x08ad, B:329:0x08bb, B:331:0x08c1, B:278:0x0748, B:280:0x074c, B:282:0x0754, B:284:0x0758, B:287:0x0762, B:333:0x08d5, B:335:0x08da, B:336:0x08e2, B:337:0x08ea, B:339:0x08f0, B:340:0x0904, B:341:0x0918, B:343:0x091d, B:345:0x0931, B:346:0x0935, B:348:0x0945, B:349:0x0949, B:350:0x094c, B:352:0x095a, B:368:0x09cc, B:370:0x09d1, B:372:0x09df, B:375:0x09e4, B:376:0x09e6, B:381:0x0a0f, B:377:0x09e9, B:379:0x09f3, B:380:0x09fa, B:382:0x0a18, B:383:0x0a2f, B:386:0x0a37, B:387:0x0a3c, B:388:0x0a4c, B:390:0x0a66, B:391:0x0a7f, B:392:0x0a87, B:397:0x0aa9, B:396:0x0a98, B:353:0x0970, B:355:0x0975, B:357:0x097f, B:359:0x0985, B:365:0x0997, B:367:0x099d, B:400:0x0ab9, B:51:0x0134, B:75:0x01ce, B:406:0x0acf, B:407:0x0ad2, B:97:0x023f), top: B:422:0x0009, inners: #4 }] */
    /* JADX WARN: Removed duplicated region for block: B:127:0x028f A[Catch: all -> 0x0ad3, TryCatch #11 {all -> 0x0ad3, blocks: (B:3:0x0009, B:25:0x0087, B:119:0x027d, B:121:0x0281, B:127:0x028f, B:128:0x02aa, B:130:0x02b4, B:132:0x02cc, B:134:0x02f9, B:140:0x030d, B:142:0x0317, B:220:0x058d, B:144:0x0332, B:146:0x0342, B:206:0x0536, B:208:0x0540, B:210:0x0544, B:213:0x054a, B:215:0x0557, B:216:0x0569, B:217:0x056d, B:218:0x0573, B:219:0x0586, B:150:0x0352, B:152:0x0356, B:153:0x035b, B:155:0x0364, B:157:0x0376, B:161:0x0390, B:158:0x037e, B:160:0x0388, B:165:0x039f, B:167:0x03db, B:168:0x0415, B:171:0x0447, B:173:0x044c, B:175:0x045a, B:177:0x0463, B:178:0x0469, B:180:0x046c, B:181:0x0475, B:182:0x0478, B:185:0x047f, B:188:0x0489, B:190:0x04ba, B:192:0x04d7, B:198:0x04f4, B:195:0x04e9, B:201:0x04ff, B:203:0x0512, B:204:0x051f, B:221:0x0593, B:223:0x059d, B:225:0x05a9, B:227:0x05b7, B:230:0x05bc, B:232:0x05fa, B:233:0x0618, B:235:0x061d, B:237:0x062b, B:241:0x0637, B:244:0x0657, B:238:0x0631, B:231:0x05df, B:245:0x066d, B:247:0x0687, B:249:0x06a0, B:251:0x06ac, B:253:0x06bf, B:254:0x06ce, B:256:0x06d2, B:258:0x06dc, B:259:0x06eb, B:261:0x06ef, B:263:0x06f7, B:264:0x0708, B:332:0x08cb, B:266:0x0719, B:270:0x0728, B:272:0x072e, B:274:0x073c, B:276:0x0740, B:291:0x0770, B:294:0x0782, B:296:0x079c, B:298:0x07a6, B:300:0x07b6, B:301:0x07ec, B:305:0x07fc, B:307:0x0803, B:309:0x080d, B:311:0x0811, B:313:0x0815, B:315:0x0819, B:316:0x0820, B:317:0x0825, B:319:0x082b, B:321:0x0847, B:322:0x0850, B:323:0x085d, B:325:0x0872, B:327:0x089f, B:328:0x08ad, B:329:0x08bb, B:331:0x08c1, B:278:0x0748, B:280:0x074c, B:282:0x0754, B:284:0x0758, B:287:0x0762, B:333:0x08d5, B:335:0x08da, B:336:0x08e2, B:337:0x08ea, B:339:0x08f0, B:340:0x0904, B:341:0x0918, B:343:0x091d, B:345:0x0931, B:346:0x0935, B:348:0x0945, B:349:0x0949, B:350:0x094c, B:352:0x095a, B:368:0x09cc, B:370:0x09d1, B:372:0x09df, B:375:0x09e4, B:376:0x09e6, B:381:0x0a0f, B:377:0x09e9, B:379:0x09f3, B:380:0x09fa, B:382:0x0a18, B:383:0x0a2f, B:386:0x0a37, B:387:0x0a3c, B:388:0x0a4c, B:390:0x0a66, B:391:0x0a7f, B:392:0x0a87, B:397:0x0aa9, B:396:0x0a98, B:353:0x0970, B:355:0x0975, B:357:0x097f, B:359:0x0985, B:365:0x0997, B:367:0x099d, B:400:0x0ab9, B:51:0x0134, B:75:0x01ce, B:406:0x0acf, B:407:0x0ad2, B:97:0x023f), top: B:422:0x0009, inners: #4 }] */
    /* JADX WARN: Removed duplicated region for block: B:215:0x0557 A[Catch: all -> 0x0ad3, TryCatch #11 {all -> 0x0ad3, blocks: (B:3:0x0009, B:25:0x0087, B:119:0x027d, B:121:0x0281, B:127:0x028f, B:128:0x02aa, B:130:0x02b4, B:132:0x02cc, B:134:0x02f9, B:140:0x030d, B:142:0x0317, B:220:0x058d, B:144:0x0332, B:146:0x0342, B:206:0x0536, B:208:0x0540, B:210:0x0544, B:213:0x054a, B:215:0x0557, B:216:0x0569, B:217:0x056d, B:218:0x0573, B:219:0x0586, B:150:0x0352, B:152:0x0356, B:153:0x035b, B:155:0x0364, B:157:0x0376, B:161:0x0390, B:158:0x037e, B:160:0x0388, B:165:0x039f, B:167:0x03db, B:168:0x0415, B:171:0x0447, B:173:0x044c, B:175:0x045a, B:177:0x0463, B:178:0x0469, B:180:0x046c, B:181:0x0475, B:182:0x0478, B:185:0x047f, B:188:0x0489, B:190:0x04ba, B:192:0x04d7, B:198:0x04f4, B:195:0x04e9, B:201:0x04ff, B:203:0x0512, B:204:0x051f, B:221:0x0593, B:223:0x059d, B:225:0x05a9, B:227:0x05b7, B:230:0x05bc, B:232:0x05fa, B:233:0x0618, B:235:0x061d, B:237:0x062b, B:241:0x0637, B:244:0x0657, B:238:0x0631, B:231:0x05df, B:245:0x066d, B:247:0x0687, B:249:0x06a0, B:251:0x06ac, B:253:0x06bf, B:254:0x06ce, B:256:0x06d2, B:258:0x06dc, B:259:0x06eb, B:261:0x06ef, B:263:0x06f7, B:264:0x0708, B:332:0x08cb, B:266:0x0719, B:270:0x0728, B:272:0x072e, B:274:0x073c, B:276:0x0740, B:291:0x0770, B:294:0x0782, B:296:0x079c, B:298:0x07a6, B:300:0x07b6, B:301:0x07ec, B:305:0x07fc, B:307:0x0803, B:309:0x080d, B:311:0x0811, B:313:0x0815, B:315:0x0819, B:316:0x0820, B:317:0x0825, B:319:0x082b, B:321:0x0847, B:322:0x0850, B:323:0x085d, B:325:0x0872, B:327:0x089f, B:328:0x08ad, B:329:0x08bb, B:331:0x08c1, B:278:0x0748, B:280:0x074c, B:282:0x0754, B:284:0x0758, B:287:0x0762, B:333:0x08d5, B:335:0x08da, B:336:0x08e2, B:337:0x08ea, B:339:0x08f0, B:340:0x0904, B:341:0x0918, B:343:0x091d, B:345:0x0931, B:346:0x0935, B:348:0x0945, B:349:0x0949, B:350:0x094c, B:352:0x095a, B:368:0x09cc, B:370:0x09d1, B:372:0x09df, B:375:0x09e4, B:376:0x09e6, B:381:0x0a0f, B:377:0x09e9, B:379:0x09f3, B:380:0x09fa, B:382:0x0a18, B:383:0x0a2f, B:386:0x0a37, B:387:0x0a3c, B:388:0x0a4c, B:390:0x0a66, B:391:0x0a7f, B:392:0x0a87, B:397:0x0aa9, B:396:0x0a98, B:353:0x0970, B:355:0x0975, B:357:0x097f, B:359:0x0985, B:365:0x0997, B:367:0x099d, B:400:0x0ab9, B:51:0x0134, B:75:0x01ce, B:406:0x0acf, B:407:0x0ad2, B:97:0x023f), top: B:422:0x0009, inners: #4 }] */
    /* JADX WARN: Removed duplicated region for block: B:217:0x056d A[Catch: all -> 0x0ad3, TryCatch #11 {all -> 0x0ad3, blocks: (B:3:0x0009, B:25:0x0087, B:119:0x027d, B:121:0x0281, B:127:0x028f, B:128:0x02aa, B:130:0x02b4, B:132:0x02cc, B:134:0x02f9, B:140:0x030d, B:142:0x0317, B:220:0x058d, B:144:0x0332, B:146:0x0342, B:206:0x0536, B:208:0x0540, B:210:0x0544, B:213:0x054a, B:215:0x0557, B:216:0x0569, B:217:0x056d, B:218:0x0573, B:219:0x0586, B:150:0x0352, B:152:0x0356, B:153:0x035b, B:155:0x0364, B:157:0x0376, B:161:0x0390, B:158:0x037e, B:160:0x0388, B:165:0x039f, B:167:0x03db, B:168:0x0415, B:171:0x0447, B:173:0x044c, B:175:0x045a, B:177:0x0463, B:178:0x0469, B:180:0x046c, B:181:0x0475, B:182:0x0478, B:185:0x047f, B:188:0x0489, B:190:0x04ba, B:192:0x04d7, B:198:0x04f4, B:195:0x04e9, B:201:0x04ff, B:203:0x0512, B:204:0x051f, B:221:0x0593, B:223:0x059d, B:225:0x05a9, B:227:0x05b7, B:230:0x05bc, B:232:0x05fa, B:233:0x0618, B:235:0x061d, B:237:0x062b, B:241:0x0637, B:244:0x0657, B:238:0x0631, B:231:0x05df, B:245:0x066d, B:247:0x0687, B:249:0x06a0, B:251:0x06ac, B:253:0x06bf, B:254:0x06ce, B:256:0x06d2, B:258:0x06dc, B:259:0x06eb, B:261:0x06ef, B:263:0x06f7, B:264:0x0708, B:332:0x08cb, B:266:0x0719, B:270:0x0728, B:272:0x072e, B:274:0x073c, B:276:0x0740, B:291:0x0770, B:294:0x0782, B:296:0x079c, B:298:0x07a6, B:300:0x07b6, B:301:0x07ec, B:305:0x07fc, B:307:0x0803, B:309:0x080d, B:311:0x0811, B:313:0x0815, B:315:0x0819, B:316:0x0820, B:317:0x0825, B:319:0x082b, B:321:0x0847, B:322:0x0850, B:323:0x085d, B:325:0x0872, B:327:0x089f, B:328:0x08ad, B:329:0x08bb, B:331:0x08c1, B:278:0x0748, B:280:0x074c, B:282:0x0754, B:284:0x0758, B:287:0x0762, B:333:0x08d5, B:335:0x08da, B:336:0x08e2, B:337:0x08ea, B:339:0x08f0, B:340:0x0904, B:341:0x0918, B:343:0x091d, B:345:0x0931, B:346:0x0935, B:348:0x0945, B:349:0x0949, B:350:0x094c, B:352:0x095a, B:368:0x09cc, B:370:0x09d1, B:372:0x09df, B:375:0x09e4, B:376:0x09e6, B:381:0x0a0f, B:377:0x09e9, B:379:0x09f3, B:380:0x09fa, B:382:0x0a18, B:383:0x0a2f, B:386:0x0a37, B:387:0x0a3c, B:388:0x0a4c, B:390:0x0a66, B:391:0x0a7f, B:392:0x0a87, B:397:0x0aa9, B:396:0x0a98, B:353:0x0970, B:355:0x0975, B:357:0x097f, B:359:0x0985, B:365:0x0997, B:367:0x099d, B:400:0x0ab9, B:51:0x0134, B:75:0x01ce, B:406:0x0acf, B:407:0x0ad2, B:97:0x023f), top: B:422:0x0009, inners: #4 }] */
    /* JADX WARN: Removed duplicated region for block: B:235:0x061d A[Catch: all -> 0x0ad3, TryCatch #11 {all -> 0x0ad3, blocks: (B:3:0x0009, B:25:0x0087, B:119:0x027d, B:121:0x0281, B:127:0x028f, B:128:0x02aa, B:130:0x02b4, B:132:0x02cc, B:134:0x02f9, B:140:0x030d, B:142:0x0317, B:220:0x058d, B:144:0x0332, B:146:0x0342, B:206:0x0536, B:208:0x0540, B:210:0x0544, B:213:0x054a, B:215:0x0557, B:216:0x0569, B:217:0x056d, B:218:0x0573, B:219:0x0586, B:150:0x0352, B:152:0x0356, B:153:0x035b, B:155:0x0364, B:157:0x0376, B:161:0x0390, B:158:0x037e, B:160:0x0388, B:165:0x039f, B:167:0x03db, B:168:0x0415, B:171:0x0447, B:173:0x044c, B:175:0x045a, B:177:0x0463, B:178:0x0469, B:180:0x046c, B:181:0x0475, B:182:0x0478, B:185:0x047f, B:188:0x0489, B:190:0x04ba, B:192:0x04d7, B:198:0x04f4, B:195:0x04e9, B:201:0x04ff, B:203:0x0512, B:204:0x051f, B:221:0x0593, B:223:0x059d, B:225:0x05a9, B:227:0x05b7, B:230:0x05bc, B:232:0x05fa, B:233:0x0618, B:235:0x061d, B:237:0x062b, B:241:0x0637, B:244:0x0657, B:238:0x0631, B:231:0x05df, B:245:0x066d, B:247:0x0687, B:249:0x06a0, B:251:0x06ac, B:253:0x06bf, B:254:0x06ce, B:256:0x06d2, B:258:0x06dc, B:259:0x06eb, B:261:0x06ef, B:263:0x06f7, B:264:0x0708, B:332:0x08cb, B:266:0x0719, B:270:0x0728, B:272:0x072e, B:274:0x073c, B:276:0x0740, B:291:0x0770, B:294:0x0782, B:296:0x079c, B:298:0x07a6, B:300:0x07b6, B:301:0x07ec, B:305:0x07fc, B:307:0x0803, B:309:0x080d, B:311:0x0811, B:313:0x0815, B:315:0x0819, B:316:0x0820, B:317:0x0825, B:319:0x082b, B:321:0x0847, B:322:0x0850, B:323:0x085d, B:325:0x0872, B:327:0x089f, B:328:0x08ad, B:329:0x08bb, B:331:0x08c1, B:278:0x0748, B:280:0x074c, B:282:0x0754, B:284:0x0758, B:287:0x0762, B:333:0x08d5, B:335:0x08da, B:336:0x08e2, B:337:0x08ea, B:339:0x08f0, B:340:0x0904, B:341:0x0918, B:343:0x091d, B:345:0x0931, B:346:0x0935, B:348:0x0945, B:349:0x0949, B:350:0x094c, B:352:0x095a, B:368:0x09cc, B:370:0x09d1, B:372:0x09df, B:375:0x09e4, B:376:0x09e6, B:381:0x0a0f, B:377:0x09e9, B:379:0x09f3, B:380:0x09fa, B:382:0x0a18, B:383:0x0a2f, B:386:0x0a37, B:387:0x0a3c, B:388:0x0a4c, B:390:0x0a66, B:391:0x0a7f, B:392:0x0a87, B:397:0x0aa9, B:396:0x0a98, B:353:0x0970, B:355:0x0975, B:357:0x097f, B:359:0x0985, B:365:0x0997, B:367:0x099d, B:400:0x0ab9, B:51:0x0134, B:75:0x01ce, B:406:0x0acf, B:407:0x0ad2, B:97:0x023f), top: B:422:0x0009, inners: #4 }] */
    /* JADX WARN: Removed duplicated region for block: B:241:0x0637 A[Catch: all -> 0x0ad3, TryCatch #11 {all -> 0x0ad3, blocks: (B:3:0x0009, B:25:0x0087, B:119:0x027d, B:121:0x0281, B:127:0x028f, B:128:0x02aa, B:130:0x02b4, B:132:0x02cc, B:134:0x02f9, B:140:0x030d, B:142:0x0317, B:220:0x058d, B:144:0x0332, B:146:0x0342, B:206:0x0536, B:208:0x0540, B:210:0x0544, B:213:0x054a, B:215:0x0557, B:216:0x0569, B:217:0x056d, B:218:0x0573, B:219:0x0586, B:150:0x0352, B:152:0x0356, B:153:0x035b, B:155:0x0364, B:157:0x0376, B:161:0x0390, B:158:0x037e, B:160:0x0388, B:165:0x039f, B:167:0x03db, B:168:0x0415, B:171:0x0447, B:173:0x044c, B:175:0x045a, B:177:0x0463, B:178:0x0469, B:180:0x046c, B:181:0x0475, B:182:0x0478, B:185:0x047f, B:188:0x0489, B:190:0x04ba, B:192:0x04d7, B:198:0x04f4, B:195:0x04e9, B:201:0x04ff, B:203:0x0512, B:204:0x051f, B:221:0x0593, B:223:0x059d, B:225:0x05a9, B:227:0x05b7, B:230:0x05bc, B:232:0x05fa, B:233:0x0618, B:235:0x061d, B:237:0x062b, B:241:0x0637, B:244:0x0657, B:238:0x0631, B:231:0x05df, B:245:0x066d, B:247:0x0687, B:249:0x06a0, B:251:0x06ac, B:253:0x06bf, B:254:0x06ce, B:256:0x06d2, B:258:0x06dc, B:259:0x06eb, B:261:0x06ef, B:263:0x06f7, B:264:0x0708, B:332:0x08cb, B:266:0x0719, B:270:0x0728, B:272:0x072e, B:274:0x073c, B:276:0x0740, B:291:0x0770, B:294:0x0782, B:296:0x079c, B:298:0x07a6, B:300:0x07b6, B:301:0x07ec, B:305:0x07fc, B:307:0x0803, B:309:0x080d, B:311:0x0811, B:313:0x0815, B:315:0x0819, B:316:0x0820, B:317:0x0825, B:319:0x082b, B:321:0x0847, B:322:0x0850, B:323:0x085d, B:325:0x0872, B:327:0x089f, B:328:0x08ad, B:329:0x08bb, B:331:0x08c1, B:278:0x0748, B:280:0x074c, B:282:0x0754, B:284:0x0758, B:287:0x0762, B:333:0x08d5, B:335:0x08da, B:336:0x08e2, B:337:0x08ea, B:339:0x08f0, B:340:0x0904, B:341:0x0918, B:343:0x091d, B:345:0x0931, B:346:0x0935, B:348:0x0945, B:349:0x0949, B:350:0x094c, B:352:0x095a, B:368:0x09cc, B:370:0x09d1, B:372:0x09df, B:375:0x09e4, B:376:0x09e6, B:381:0x0a0f, B:377:0x09e9, B:379:0x09f3, B:380:0x09fa, B:382:0x0a18, B:383:0x0a2f, B:386:0x0a37, B:387:0x0a3c, B:388:0x0a4c, B:390:0x0a66, B:391:0x0a7f, B:392:0x0a87, B:397:0x0aa9, B:396:0x0a98, B:353:0x0970, B:355:0x0975, B:357:0x097f, B:359:0x0985, B:365:0x0997, B:367:0x099d, B:400:0x0ab9, B:51:0x0134, B:75:0x01ce, B:406:0x0acf, B:407:0x0ad2, B:97:0x023f), top: B:422:0x0009, inners: #4 }] */
    /* JADX WARN: Removed duplicated region for block: B:244:0x0657 A[Catch: all -> 0x0ad3, TryCatch #11 {all -> 0x0ad3, blocks: (B:3:0x0009, B:25:0x0087, B:119:0x027d, B:121:0x0281, B:127:0x028f, B:128:0x02aa, B:130:0x02b4, B:132:0x02cc, B:134:0x02f9, B:140:0x030d, B:142:0x0317, B:220:0x058d, B:144:0x0332, B:146:0x0342, B:206:0x0536, B:208:0x0540, B:210:0x0544, B:213:0x054a, B:215:0x0557, B:216:0x0569, B:217:0x056d, B:218:0x0573, B:219:0x0586, B:150:0x0352, B:152:0x0356, B:153:0x035b, B:155:0x0364, B:157:0x0376, B:161:0x0390, B:158:0x037e, B:160:0x0388, B:165:0x039f, B:167:0x03db, B:168:0x0415, B:171:0x0447, B:173:0x044c, B:175:0x045a, B:177:0x0463, B:178:0x0469, B:180:0x046c, B:181:0x0475, B:182:0x0478, B:185:0x047f, B:188:0x0489, B:190:0x04ba, B:192:0x04d7, B:198:0x04f4, B:195:0x04e9, B:201:0x04ff, B:203:0x0512, B:204:0x051f, B:221:0x0593, B:223:0x059d, B:225:0x05a9, B:227:0x05b7, B:230:0x05bc, B:232:0x05fa, B:233:0x0618, B:235:0x061d, B:237:0x062b, B:241:0x0637, B:244:0x0657, B:238:0x0631, B:231:0x05df, B:245:0x066d, B:247:0x0687, B:249:0x06a0, B:251:0x06ac, B:253:0x06bf, B:254:0x06ce, B:256:0x06d2, B:258:0x06dc, B:259:0x06eb, B:261:0x06ef, B:263:0x06f7, B:264:0x0708, B:332:0x08cb, B:266:0x0719, B:270:0x0728, B:272:0x072e, B:274:0x073c, B:276:0x0740, B:291:0x0770, B:294:0x0782, B:296:0x079c, B:298:0x07a6, B:300:0x07b6, B:301:0x07ec, B:305:0x07fc, B:307:0x0803, B:309:0x080d, B:311:0x0811, B:313:0x0815, B:315:0x0819, B:316:0x0820, B:317:0x0825, B:319:0x082b, B:321:0x0847, B:322:0x0850, B:323:0x085d, B:325:0x0872, B:327:0x089f, B:328:0x08ad, B:329:0x08bb, B:331:0x08c1, B:278:0x0748, B:280:0x074c, B:282:0x0754, B:284:0x0758, B:287:0x0762, B:333:0x08d5, B:335:0x08da, B:336:0x08e2, B:337:0x08ea, B:339:0x08f0, B:340:0x0904, B:341:0x0918, B:343:0x091d, B:345:0x0931, B:346:0x0935, B:348:0x0945, B:349:0x0949, B:350:0x094c, B:352:0x095a, B:368:0x09cc, B:370:0x09d1, B:372:0x09df, B:375:0x09e4, B:376:0x09e6, B:381:0x0a0f, B:377:0x09e9, B:379:0x09f3, B:380:0x09fa, B:382:0x0a18, B:383:0x0a2f, B:386:0x0a37, B:387:0x0a3c, B:388:0x0a4c, B:390:0x0a66, B:391:0x0a7f, B:392:0x0a87, B:397:0x0aa9, B:396:0x0a98, B:353:0x0970, B:355:0x0975, B:357:0x097f, B:359:0x0985, B:365:0x0997, B:367:0x099d, B:400:0x0ab9, B:51:0x0134, B:75:0x01ce, B:406:0x0acf, B:407:0x0ad2, B:97:0x023f), top: B:422:0x0009, inners: #4 }] */
    /* JADX WARN: Removed duplicated region for block: B:291:0x0770 A[Catch: all -> 0x0ad3, TryCatch #11 {all -> 0x0ad3, blocks: (B:3:0x0009, B:25:0x0087, B:119:0x027d, B:121:0x0281, B:127:0x028f, B:128:0x02aa, B:130:0x02b4, B:132:0x02cc, B:134:0x02f9, B:140:0x030d, B:142:0x0317, B:220:0x058d, B:144:0x0332, B:146:0x0342, B:206:0x0536, B:208:0x0540, B:210:0x0544, B:213:0x054a, B:215:0x0557, B:216:0x0569, B:217:0x056d, B:218:0x0573, B:219:0x0586, B:150:0x0352, B:152:0x0356, B:153:0x035b, B:155:0x0364, B:157:0x0376, B:161:0x0390, B:158:0x037e, B:160:0x0388, B:165:0x039f, B:167:0x03db, B:168:0x0415, B:171:0x0447, B:173:0x044c, B:175:0x045a, B:177:0x0463, B:178:0x0469, B:180:0x046c, B:181:0x0475, B:182:0x0478, B:185:0x047f, B:188:0x0489, B:190:0x04ba, B:192:0x04d7, B:198:0x04f4, B:195:0x04e9, B:201:0x04ff, B:203:0x0512, B:204:0x051f, B:221:0x0593, B:223:0x059d, B:225:0x05a9, B:227:0x05b7, B:230:0x05bc, B:232:0x05fa, B:233:0x0618, B:235:0x061d, B:237:0x062b, B:241:0x0637, B:244:0x0657, B:238:0x0631, B:231:0x05df, B:245:0x066d, B:247:0x0687, B:249:0x06a0, B:251:0x06ac, B:253:0x06bf, B:254:0x06ce, B:256:0x06d2, B:258:0x06dc, B:259:0x06eb, B:261:0x06ef, B:263:0x06f7, B:264:0x0708, B:332:0x08cb, B:266:0x0719, B:270:0x0728, B:272:0x072e, B:274:0x073c, B:276:0x0740, B:291:0x0770, B:294:0x0782, B:296:0x079c, B:298:0x07a6, B:300:0x07b6, B:301:0x07ec, B:305:0x07fc, B:307:0x0803, B:309:0x080d, B:311:0x0811, B:313:0x0815, B:315:0x0819, B:316:0x0820, B:317:0x0825, B:319:0x082b, B:321:0x0847, B:322:0x0850, B:323:0x085d, B:325:0x0872, B:327:0x089f, B:328:0x08ad, B:329:0x08bb, B:331:0x08c1, B:278:0x0748, B:280:0x074c, B:282:0x0754, B:284:0x0758, B:287:0x0762, B:333:0x08d5, B:335:0x08da, B:336:0x08e2, B:337:0x08ea, B:339:0x08f0, B:340:0x0904, B:341:0x0918, B:343:0x091d, B:345:0x0931, B:346:0x0935, B:348:0x0945, B:349:0x0949, B:350:0x094c, B:352:0x095a, B:368:0x09cc, B:370:0x09d1, B:372:0x09df, B:375:0x09e4, B:376:0x09e6, B:381:0x0a0f, B:377:0x09e9, B:379:0x09f3, B:380:0x09fa, B:382:0x0a18, B:383:0x0a2f, B:386:0x0a37, B:387:0x0a3c, B:388:0x0a4c, B:390:0x0a66, B:391:0x0a7f, B:392:0x0a87, B:397:0x0aa9, B:396:0x0a98, B:353:0x0970, B:355:0x0975, B:357:0x097f, B:359:0x0985, B:365:0x0997, B:367:0x099d, B:400:0x0ab9, B:51:0x0134, B:75:0x01ce, B:406:0x0acf, B:407:0x0ad2, B:97:0x023f), top: B:422:0x0009, inners: #4 }] */
    /* JADX WARN: Removed duplicated region for block: B:292:0x077f  */
    /* JADX WARN: Removed duplicated region for block: B:294:0x0782 A[Catch: all -> 0x0ad3, TryCatch #11 {all -> 0x0ad3, blocks: (B:3:0x0009, B:25:0x0087, B:119:0x027d, B:121:0x0281, B:127:0x028f, B:128:0x02aa, B:130:0x02b4, B:132:0x02cc, B:134:0x02f9, B:140:0x030d, B:142:0x0317, B:220:0x058d, B:144:0x0332, B:146:0x0342, B:206:0x0536, B:208:0x0540, B:210:0x0544, B:213:0x054a, B:215:0x0557, B:216:0x0569, B:217:0x056d, B:218:0x0573, B:219:0x0586, B:150:0x0352, B:152:0x0356, B:153:0x035b, B:155:0x0364, B:157:0x0376, B:161:0x0390, B:158:0x037e, B:160:0x0388, B:165:0x039f, B:167:0x03db, B:168:0x0415, B:171:0x0447, B:173:0x044c, B:175:0x045a, B:177:0x0463, B:178:0x0469, B:180:0x046c, B:181:0x0475, B:182:0x0478, B:185:0x047f, B:188:0x0489, B:190:0x04ba, B:192:0x04d7, B:198:0x04f4, B:195:0x04e9, B:201:0x04ff, B:203:0x0512, B:204:0x051f, B:221:0x0593, B:223:0x059d, B:225:0x05a9, B:227:0x05b7, B:230:0x05bc, B:232:0x05fa, B:233:0x0618, B:235:0x061d, B:237:0x062b, B:241:0x0637, B:244:0x0657, B:238:0x0631, B:231:0x05df, B:245:0x066d, B:247:0x0687, B:249:0x06a0, B:251:0x06ac, B:253:0x06bf, B:254:0x06ce, B:256:0x06d2, B:258:0x06dc, B:259:0x06eb, B:261:0x06ef, B:263:0x06f7, B:264:0x0708, B:332:0x08cb, B:266:0x0719, B:270:0x0728, B:272:0x072e, B:274:0x073c, B:276:0x0740, B:291:0x0770, B:294:0x0782, B:296:0x079c, B:298:0x07a6, B:300:0x07b6, B:301:0x07ec, B:305:0x07fc, B:307:0x0803, B:309:0x080d, B:311:0x0811, B:313:0x0815, B:315:0x0819, B:316:0x0820, B:317:0x0825, B:319:0x082b, B:321:0x0847, B:322:0x0850, B:323:0x085d, B:325:0x0872, B:327:0x089f, B:328:0x08ad, B:329:0x08bb, B:331:0x08c1, B:278:0x0748, B:280:0x074c, B:282:0x0754, B:284:0x0758, B:287:0x0762, B:333:0x08d5, B:335:0x08da, B:336:0x08e2, B:337:0x08ea, B:339:0x08f0, B:340:0x0904, B:341:0x0918, B:343:0x091d, B:345:0x0931, B:346:0x0935, B:348:0x0945, B:349:0x0949, B:350:0x094c, B:352:0x095a, B:368:0x09cc, B:370:0x09d1, B:372:0x09df, B:375:0x09e4, B:376:0x09e6, B:381:0x0a0f, B:377:0x09e9, B:379:0x09f3, B:380:0x09fa, B:382:0x0a18, B:383:0x0a2f, B:386:0x0a37, B:387:0x0a3c, B:388:0x0a4c, B:390:0x0a66, B:391:0x0a7f, B:392:0x0a87, B:397:0x0aa9, B:396:0x0a98, B:353:0x0970, B:355:0x0975, B:357:0x097f, B:359:0x0985, B:365:0x0997, B:367:0x099d, B:400:0x0ab9, B:51:0x0134, B:75:0x01ce, B:406:0x0acf, B:407:0x0ad2, B:97:0x023f), top: B:422:0x0009, inners: #4 }] */
    /* JADX WARN: Removed duplicated region for block: B:296:0x079c A[Catch: all -> 0x0ad3, TryCatch #11 {all -> 0x0ad3, blocks: (B:3:0x0009, B:25:0x0087, B:119:0x027d, B:121:0x0281, B:127:0x028f, B:128:0x02aa, B:130:0x02b4, B:132:0x02cc, B:134:0x02f9, B:140:0x030d, B:142:0x0317, B:220:0x058d, B:144:0x0332, B:146:0x0342, B:206:0x0536, B:208:0x0540, B:210:0x0544, B:213:0x054a, B:215:0x0557, B:216:0x0569, B:217:0x056d, B:218:0x0573, B:219:0x0586, B:150:0x0352, B:152:0x0356, B:153:0x035b, B:155:0x0364, B:157:0x0376, B:161:0x0390, B:158:0x037e, B:160:0x0388, B:165:0x039f, B:167:0x03db, B:168:0x0415, B:171:0x0447, B:173:0x044c, B:175:0x045a, B:177:0x0463, B:178:0x0469, B:180:0x046c, B:181:0x0475, B:182:0x0478, B:185:0x047f, B:188:0x0489, B:190:0x04ba, B:192:0x04d7, B:198:0x04f4, B:195:0x04e9, B:201:0x04ff, B:203:0x0512, B:204:0x051f, B:221:0x0593, B:223:0x059d, B:225:0x05a9, B:227:0x05b7, B:230:0x05bc, B:232:0x05fa, B:233:0x0618, B:235:0x061d, B:237:0x062b, B:241:0x0637, B:244:0x0657, B:238:0x0631, B:231:0x05df, B:245:0x066d, B:247:0x0687, B:249:0x06a0, B:251:0x06ac, B:253:0x06bf, B:254:0x06ce, B:256:0x06d2, B:258:0x06dc, B:259:0x06eb, B:261:0x06ef, B:263:0x06f7, B:264:0x0708, B:332:0x08cb, B:266:0x0719, B:270:0x0728, B:272:0x072e, B:274:0x073c, B:276:0x0740, B:291:0x0770, B:294:0x0782, B:296:0x079c, B:298:0x07a6, B:300:0x07b6, B:301:0x07ec, B:305:0x07fc, B:307:0x0803, B:309:0x080d, B:311:0x0811, B:313:0x0815, B:315:0x0819, B:316:0x0820, B:317:0x0825, B:319:0x082b, B:321:0x0847, B:322:0x0850, B:323:0x085d, B:325:0x0872, B:327:0x089f, B:328:0x08ad, B:329:0x08bb, B:331:0x08c1, B:278:0x0748, B:280:0x074c, B:282:0x0754, B:284:0x0758, B:287:0x0762, B:333:0x08d5, B:335:0x08da, B:336:0x08e2, B:337:0x08ea, B:339:0x08f0, B:340:0x0904, B:341:0x0918, B:343:0x091d, B:345:0x0931, B:346:0x0935, B:348:0x0945, B:349:0x0949, B:350:0x094c, B:352:0x095a, B:368:0x09cc, B:370:0x09d1, B:372:0x09df, B:375:0x09e4, B:376:0x09e6, B:381:0x0a0f, B:377:0x09e9, B:379:0x09f3, B:380:0x09fa, B:382:0x0a18, B:383:0x0a2f, B:386:0x0a37, B:387:0x0a3c, B:388:0x0a4c, B:390:0x0a66, B:391:0x0a7f, B:392:0x0a87, B:397:0x0aa9, B:396:0x0a98, B:353:0x0970, B:355:0x0975, B:357:0x097f, B:359:0x0985, B:365:0x0997, B:367:0x099d, B:400:0x0ab9, B:51:0x0134, B:75:0x01ce, B:406:0x0acf, B:407:0x0ad2, B:97:0x023f), top: B:422:0x0009, inners: #4 }] */
    /* JADX WARN: Removed duplicated region for block: B:400:0x0ab9 A[Catch: all -> 0x0ad3, TRY_ENTER, TRY_LEAVE, TryCatch #11 {all -> 0x0ad3, blocks: (B:3:0x0009, B:25:0x0087, B:119:0x027d, B:121:0x0281, B:127:0x028f, B:128:0x02aa, B:130:0x02b4, B:132:0x02cc, B:134:0x02f9, B:140:0x030d, B:142:0x0317, B:220:0x058d, B:144:0x0332, B:146:0x0342, B:206:0x0536, B:208:0x0540, B:210:0x0544, B:213:0x054a, B:215:0x0557, B:216:0x0569, B:217:0x056d, B:218:0x0573, B:219:0x0586, B:150:0x0352, B:152:0x0356, B:153:0x035b, B:155:0x0364, B:157:0x0376, B:161:0x0390, B:158:0x037e, B:160:0x0388, B:165:0x039f, B:167:0x03db, B:168:0x0415, B:171:0x0447, B:173:0x044c, B:175:0x045a, B:177:0x0463, B:178:0x0469, B:180:0x046c, B:181:0x0475, B:182:0x0478, B:185:0x047f, B:188:0x0489, B:190:0x04ba, B:192:0x04d7, B:198:0x04f4, B:195:0x04e9, B:201:0x04ff, B:203:0x0512, B:204:0x051f, B:221:0x0593, B:223:0x059d, B:225:0x05a9, B:227:0x05b7, B:230:0x05bc, B:232:0x05fa, B:233:0x0618, B:235:0x061d, B:237:0x062b, B:241:0x0637, B:244:0x0657, B:238:0x0631, B:231:0x05df, B:245:0x066d, B:247:0x0687, B:249:0x06a0, B:251:0x06ac, B:253:0x06bf, B:254:0x06ce, B:256:0x06d2, B:258:0x06dc, B:259:0x06eb, B:261:0x06ef, B:263:0x06f7, B:264:0x0708, B:332:0x08cb, B:266:0x0719, B:270:0x0728, B:272:0x072e, B:274:0x073c, B:276:0x0740, B:291:0x0770, B:294:0x0782, B:296:0x079c, B:298:0x07a6, B:300:0x07b6, B:301:0x07ec, B:305:0x07fc, B:307:0x0803, B:309:0x080d, B:311:0x0811, B:313:0x0815, B:315:0x0819, B:316:0x0820, B:317:0x0825, B:319:0x082b, B:321:0x0847, B:322:0x0850, B:323:0x085d, B:325:0x0872, B:327:0x089f, B:328:0x08ad, B:329:0x08bb, B:331:0x08c1, B:278:0x0748, B:280:0x074c, B:282:0x0754, B:284:0x0758, B:287:0x0762, B:333:0x08d5, B:335:0x08da, B:336:0x08e2, B:337:0x08ea, B:339:0x08f0, B:340:0x0904, B:341:0x0918, B:343:0x091d, B:345:0x0931, B:346:0x0935, B:348:0x0945, B:349:0x0949, B:350:0x094c, B:352:0x095a, B:368:0x09cc, B:370:0x09d1, B:372:0x09df, B:375:0x09e4, B:376:0x09e6, B:381:0x0a0f, B:377:0x09e9, B:379:0x09f3, B:380:0x09fa, B:382:0x0a18, B:383:0x0a2f, B:386:0x0a37, B:387:0x0a3c, B:388:0x0a4c, B:390:0x0a66, B:391:0x0a7f, B:392:0x0a87, B:397:0x0aa9, B:396:0x0a98, B:353:0x0970, B:355:0x0975, B:357:0x097f, B:359:0x0985, B:365:0x0997, B:367:0x099d, B:400:0x0ab9, B:51:0x0134, B:75:0x01ce, B:406:0x0acf, B:407:0x0ad2, B:97:0x023f), top: B:422:0x0009, inners: #4 }] */
    /* JADX WARN: Removed duplicated region for block: B:406:0x0acf A[Catch: all -> 0x0ad3, TRY_ENTER, TryCatch #11 {all -> 0x0ad3, blocks: (B:3:0x0009, B:25:0x0087, B:119:0x027d, B:121:0x0281, B:127:0x028f, B:128:0x02aa, B:130:0x02b4, B:132:0x02cc, B:134:0x02f9, B:140:0x030d, B:142:0x0317, B:220:0x058d, B:144:0x0332, B:146:0x0342, B:206:0x0536, B:208:0x0540, B:210:0x0544, B:213:0x054a, B:215:0x0557, B:216:0x0569, B:217:0x056d, B:218:0x0573, B:219:0x0586, B:150:0x0352, B:152:0x0356, B:153:0x035b, B:155:0x0364, B:157:0x0376, B:161:0x0390, B:158:0x037e, B:160:0x0388, B:165:0x039f, B:167:0x03db, B:168:0x0415, B:171:0x0447, B:173:0x044c, B:175:0x045a, B:177:0x0463, B:178:0x0469, B:180:0x046c, B:181:0x0475, B:182:0x0478, B:185:0x047f, B:188:0x0489, B:190:0x04ba, B:192:0x04d7, B:198:0x04f4, B:195:0x04e9, B:201:0x04ff, B:203:0x0512, B:204:0x051f, B:221:0x0593, B:223:0x059d, B:225:0x05a9, B:227:0x05b7, B:230:0x05bc, B:232:0x05fa, B:233:0x0618, B:235:0x061d, B:237:0x062b, B:241:0x0637, B:244:0x0657, B:238:0x0631, B:231:0x05df, B:245:0x066d, B:247:0x0687, B:249:0x06a0, B:251:0x06ac, B:253:0x06bf, B:254:0x06ce, B:256:0x06d2, B:258:0x06dc, B:259:0x06eb, B:261:0x06ef, B:263:0x06f7, B:264:0x0708, B:332:0x08cb, B:266:0x0719, B:270:0x0728, B:272:0x072e, B:274:0x073c, B:276:0x0740, B:291:0x0770, B:294:0x0782, B:296:0x079c, B:298:0x07a6, B:300:0x07b6, B:301:0x07ec, B:305:0x07fc, B:307:0x0803, B:309:0x080d, B:311:0x0811, B:313:0x0815, B:315:0x0819, B:316:0x0820, B:317:0x0825, B:319:0x082b, B:321:0x0847, B:322:0x0850, B:323:0x085d, B:325:0x0872, B:327:0x089f, B:328:0x08ad, B:329:0x08bb, B:331:0x08c1, B:278:0x0748, B:280:0x074c, B:282:0x0754, B:284:0x0758, B:287:0x0762, B:333:0x08d5, B:335:0x08da, B:336:0x08e2, B:337:0x08ea, B:339:0x08f0, B:340:0x0904, B:341:0x0918, B:343:0x091d, B:345:0x0931, B:346:0x0935, B:348:0x0945, B:349:0x0949, B:350:0x094c, B:352:0x095a, B:368:0x09cc, B:370:0x09d1, B:372:0x09df, B:375:0x09e4, B:376:0x09e6, B:381:0x0a0f, B:377:0x09e9, B:379:0x09f3, B:380:0x09fa, B:382:0x0a18, B:383:0x0a2f, B:386:0x0a37, B:387:0x0a3c, B:388:0x0a4c, B:390:0x0a66, B:391:0x0a7f, B:392:0x0a87, B:397:0x0aa9, B:396:0x0a98, B:353:0x0970, B:355:0x0975, B:357:0x097f, B:359:0x0985, B:365:0x0997, B:367:0x099d, B:400:0x0ab9, B:51:0x0134, B:75:0x01ce, B:406:0x0acf, B:407:0x0ad2, B:97:0x023f), top: B:422:0x0009, inners: #4 }] */
    /* JADX WARN: Removed duplicated region for block: B:424:0x0144 A[EXC_TOP_SPLITTER, SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:449:0x0634 A[SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:49:0x0121 A[Catch: all -> 0x0139, SQLiteException -> 0x013f, TRY_ENTER, TRY_LEAVE, TryCatch #18 {SQLiteException -> 0x013f, all -> 0x0139, blocks: (B:49:0x0121, B:61:0x015b, B:65:0x0177), top: B:433:0x011f }] */
    /* JADX WARN: Type inference failed for: r0v12, types: [android.database.sqlite.SQLiteException] */
    /* JADX WARN: Type inference failed for: r0v14, types: [android.database.sqlite.SQLiteException] */
    /* JADX WARN: Type inference failed for: r0v16, types: [android.database.sqlite.SQLiteException] */
    /* JADX WARN: Type inference failed for: r0v19, types: [android.database.sqlite.SQLiteException] */
    /* JADX WARN: Type inference failed for: r0v2, types: [android.database.sqlite.SQLiteException] */
    /* JADX WARN: Type inference failed for: r0v21, types: [android.database.sqlite.SQLiteException] */
    /* JADX WARN: Type inference failed for: r0v22, types: [android.database.sqlite.SQLiteException] */
    /* JADX WARN: Type inference failed for: r0v5 */
    /* JADX WARN: Type inference failed for: r0v6, types: [android.database.sqlite.SQLiteException] */
    /* JADX WARN: Type inference failed for: r0v9, types: [android.database.sqlite.SQLiteException] */
    /* JADX WARN: Type inference failed for: r4v0, types: [com.google.android.gms.internal.measurement.zzei, com.google.android.gms.internal.measurement.zzhg, com.google.android.gms.internal.measurement.zzjq] */
    /* JADX WARN: Type inference failed for: r4v1 */
    /* JADX WARN: Type inference failed for: r4v2 */
    /* JADX WARN: Type inference failed for: r4v3 */
    /* JADX WARN: Type inference failed for: r4v6, types: [com.google.android.gms.internal.measurement.zzfi] */
    /* JADX WARN: Type inference failed for: r4v72 */
    /* JADX WARN: Type inference failed for: r4v73 */
    /* JADX WARN: Type inference failed for: r4v8 */
    /* JADX WARN: Type inference failed for: r9v0 */
    /* JADX WARN: Type inference failed for: r9v1 */
    /* JADX WARN: Type inference failed for: r9v2 */
    /* JADX WARN: Type inference failed for: r9v3 */
    /* JADX WARN: Type inference failed for: r9v37 */
    /* JADX WARN: Type inference failed for: r9v4 */
    /* JADX WARN: Type inference failed for: r9v47, types: [boolean] */
    /* JADX WARN: Type inference failed for: r9v5 */
    /* JADX WARN: Type inference failed for: r9v51, types: [boolean] */
    /* JADX WARN: Type inference failed for: r9v52 */
    /* JADX WARN: Type inference failed for: r9v53, types: [java.lang.String] */
    /* JADX WARN: Type inference failed for: r9v58 */
    /* JADX WARN: Type inference failed for: r9v6, types: [android.database.Cursor] */
    /* JADX WARN: Type inference failed for: r9v60 */
    /* JADX WARN: Type inference failed for: r9v61 */
    /* JADX WARN: Type inference failed for: r9v62 */
    /* JADX WARN: Type inference failed for: r9v7 */
    /* JADX WARN: Type inference failed for: r9v9 */
    /* JADX WARN: Unsupported multi-entry loop pattern (BACK_EDGE: B:105:0x024b -> B:15:0x0049). Please submit an issue!!! */
    @androidx.annotation.WorkerThread
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    private final boolean zzd(java.lang.String r64, long r65) {
        /*
            Method dump skipped, instructions count: 2781
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.android.gms.internal.measurement.zzjr.zzd(java.lang.String, long):boolean");
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* JADX WARN: Removed duplicated region for block: B:32:0x00cc  */
    /* JADX WARN: Removed duplicated region for block: B:40:0x00f2  */
    /* JADX WARN: Removed duplicated region for block: B:43:0x0100  */
    /* JADX WARN: Removed duplicated region for block: B:51:0x012a  */
    /* JADX WARN: Removed duplicated region for block: B:54:0x0138  */
    /* JADX WARN: Removed duplicated region for block: B:57:0x0146  */
    /* JADX WARN: Removed duplicated region for block: B:59:0x014e  */
    @androidx.annotation.WorkerThread
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public final com.google.android.gms.internal.measurement.zzdy zzg(com.google.android.gms.internal.measurement.zzdz r8) {
        /*
            Method dump skipped, instructions count: 342
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.android.gms.internal.measurement.zzjr.zzg(com.google.android.gms.internal.measurement.zzdz):com.google.android.gms.internal.measurement.zzdy");
    }

    private final zzgf zzkm() {
        zza(this.zzaqa);
        return this.zzaqa;
    }

    private final zzfp zzko() {
        if (this.zzaqd != null) {
            return this.zzaqd;
        }
        throw new IllegalStateException("Network broadcast receiver not created");
    }

    private final zzjn zzkp() {
        zza(this.zzaqe);
        return this.zzaqe;
    }

    private final long zzkr() {
        long currentTimeMillis = zzbt().currentTimeMillis();
        zzfr zzgf = zzgf();
        zzgf.zzch();
        zzgf.zzab();
        long j = zzgf.zzajy.get();
        if (j == 0) {
            j = 1 + zzgf.zzgb().zzlc().nextInt(DateTimeConstants.MILLIS_PER_DAY);
            zzgf.zzajy.set(j);
        }
        return ((((currentTimeMillis + j) / 1000) / 60) / 60) / 24;
    }

    private final boolean zzkt() {
        zzab();
        zzkq();
        return zzix().zzhs() || !TextUtils.isEmpty(zzix().zzhn());
    }

    /* JADX WARN: Removed duplicated region for block: B:57:0x0169  */
    /* JADX WARN: Removed duplicated region for block: B:59:0x0185  */
    @androidx.annotation.WorkerThread
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    private final void zzku() {
        /*
            Method dump skipped, instructions count: 574
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.android.gms.internal.measurement.zzjr.zzku():void");
    }

    @WorkerThread
    private final void zzkv() {
        zzab();
        if (this.zzaql || this.zzaqm || this.zzaqn) {
            zzge().zzit().zzd("Not stopping services. fetch, network, upload", Boolean.valueOf(this.zzaql), Boolean.valueOf(this.zzaqm), Boolean.valueOf(this.zzaqn));
            return;
        }
        zzge().zzit().log("Stopping uploading service(s)");
        if (this.zzaqi == null) {
            return;
        }
        for (Runnable runnable : this.zzaqi) {
            runnable.run();
        }
        this.zzaqi.clear();
    }

    @VisibleForTesting
    @WorkerThread
    private final boolean zzkw() {
        zzfi zzim;
        String str;
        zzab();
        try {
            this.zzaqp = new RandomAccessFile(new File(getContext().getFilesDir(), "google_app_measurement.db"), "rw").getChannel();
            this.zzaqo = this.zzaqp.tryLock();
            if (this.zzaqo != null) {
                zzge().zzit().log("Storage concurrent access okay");
                return true;
            }
            zzge().zzim().log("Storage concurrent data access panic");
            return false;
        } catch (FileNotFoundException e) {
            e = e;
            zzim = zzge().zzim();
            str = "Failed to acquire storage lock";
            zzim.zzg(str, e);
            return false;
        } catch (IOException e2) {
            e = e2;
            zzim = zzge().zzim();
            str = "Failed to access storage lock file";
            zzim.zzg(str, e);
            return false;
        }
    }

    @WorkerThread
    private final boolean zzky() {
        zzab();
        zzkq();
        return this.zzaqg;
    }

    @Override // com.google.android.gms.internal.measurement.zzec
    public Context getContext() {
        return this.zzacw.getContext();
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @WorkerThread
    public void start() {
        zzab();
        zzix().zzhp();
        if (zzgf().zzaju.get() == 0) {
            zzgf().zzaju.set(zzbt().currentTimeMillis());
        }
        zzku();
    }

    /* JADX INFO: Access modifiers changed from: protected */
    /* JADX WARN: Code restructure failed: missing block: B:54:0x0148, code lost:
        zzgf().zzajw.set(zzbt().currentTimeMillis());
     */
    @com.google.android.gms.common.util.VisibleForTesting
    @androidx.annotation.WorkerThread
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public final void zza(int r10, java.lang.Throwable r11, byte[] r12, java.lang.String r13) {
        /*
            Method dump skipped, instructions count: 377
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.android.gms.internal.measurement.zzjr.zza(int, java.lang.Throwable, byte[], java.lang.String):void");
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public final void zza(zzgl zzglVar) {
        this.zzacw = zzglVar;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @WorkerThread
    public final void zza(zzjw zzjwVar) {
        zzab();
        zzei zzeiVar = new zzei(this.zzacw);
        zzeiVar.zzm();
        this.zzaqc = zzeiVar;
        zzgg().zza(this.zzaqa);
        zzeb zzebVar = new zzeb(this.zzacw);
        zzebVar.zzm();
        this.zzaqf = zzebVar;
        zzjn zzjnVar = new zzjn(this.zzacw);
        zzjnVar.zzm();
        this.zzaqe = zzjnVar;
        this.zzaqd = new zzfp(this.zzacw);
        if (this.zzaqj != this.zzaqk) {
            zzge().zzim().zze("Not all upload components initialized", Integer.valueOf(this.zzaqj), Integer.valueOf(this.zzaqk));
        }
        this.zzvo = true;
    }

    /* JADX WARN: Multi-variable type inference failed */
    @WorkerThread
    public final byte[] zza(@NonNull zzeu zzeuVar, @Size(min = 1) String str) {
        zzjz zzjzVar;
        Bundle bundle;
        zzkq zzkqVar;
        zzdy zzdyVar;
        zzkp zzkpVar;
        byte[] bArr;
        long j;
        zzfi zzip;
        String str2;
        Object zzbm;
        zzkq();
        zzab();
        zzgl.zzfr();
        Preconditions.checkNotNull(zzeuVar);
        Preconditions.checkNotEmpty(str);
        zzkp zzkpVar2 = new zzkp();
        zzix().beginTransaction();
        try {
            zzdy zzbc = zzix().zzbc(str);
            if (zzbc == null) {
                zzge().zzis().zzg("Log and bundle not available. package_name", str);
            } else if (zzbc.isMeasurementEnabled()) {
                if (("_iap".equals(zzeuVar.name) || FirebaseAnalytics.Event.ECOMMERCE_PURCHASE.equals(zzeuVar.name)) && !zza(str, zzeuVar)) {
                    zzge().zzip().zzg("Failed to handle purchase event at single event bundle creation. appId", zzfg.zzbm(str));
                }
                boolean zzav = zzgg().zzav(str);
                Long l = 0L;
                if (zzav && "_e".equals(zzeuVar.name)) {
                    if (zzeuVar.zzafq != null && zzeuVar.zzafq.size() != 0) {
                        if (zzeuVar.zzafq.getLong("_et") == null) {
                            zzip = zzge().zzip();
                            str2 = "The engagement event does not include duration. appId";
                            zzbm = zzfg.zzbm(str);
                            zzip.zzg(str2, zzbm);
                        } else {
                            l = zzeuVar.zzafq.getLong("_et");
                        }
                    }
                    zzip = zzge().zzip();
                    str2 = "The engagement event does not contain any parameters. appId";
                    zzbm = zzfg.zzbm(str);
                    zzip.zzg(str2, zzbm);
                }
                zzkq zzkqVar2 = new zzkq();
                zzkpVar2.zzatf = new zzkq[]{zzkqVar2};
                zzkqVar2.zzath = 1;
                zzkqVar2.zzatp = Constants.PLATFORM;
                zzkqVar2.zzti = zzbc.zzah();
                zzkqVar2.zzadt = zzbc.zzgn();
                zzkqVar2.zzth = zzbc.zzag();
                long zzgm = zzbc.zzgm();
                zzkqVar2.zzaub = zzgm == -2147483648L ? null : Integer.valueOf((int) zzgm);
                zzkqVar2.zzatt = Long.valueOf(zzbc.zzgo());
                zzkqVar2.zzadm = zzbc.getGmpAppId();
                zzkqVar2.zzatx = Long.valueOf(zzbc.zzgp());
                if (this.zzacw.isEnabled() && zzef.zzhk() && zzgg().zzat(zzkqVar2.zzti)) {
                    zzkqVar2.zzauh = null;
                }
                Pair<String, Boolean> zzbo = zzgf().zzbo(zzbc.zzah());
                if (zzbc.zzhd() && zzbo != null && !TextUtils.isEmpty((CharSequence) zzbo.first)) {
                    zzkqVar2.zzatv = (String) zzbo.first;
                    zzkqVar2.zzatw = (Boolean) zzbo.second;
                }
                zzfw().zzch();
                zzkqVar2.zzatr = Build.MODEL;
                zzfw().zzch();
                zzkqVar2.zzatq = Build.VERSION.RELEASE;
                zzkqVar2.zzats = Integer.valueOf((int) zzfw().zzic());
                zzkqVar2.zzafn = zzfw().zzid();
                zzkqVar2.zzadl = zzbc.getAppInstanceId();
                zzkqVar2.zzado = zzbc.zzgj();
                List<zzjz> zzbb = zzix().zzbb(zzbc.zzah());
                zzkqVar2.zzatj = new zzks[zzbb.size()];
                if (zzav) {
                    zzjzVar = zzix().zzh(zzkqVar2.zzti, "_lte");
                    if (zzjzVar != null && zzjzVar.value != null) {
                        if (l.longValue() > 0) {
                            zzjzVar = new zzjz(zzkqVar2.zzti, "auto", "_lte", zzbt().currentTimeMillis(), Long.valueOf(((Long) zzjzVar.value).longValue() + l.longValue()));
                        }
                    }
                    zzjzVar = new zzjz(zzkqVar2.zzti, "auto", "_lte", zzbt().currentTimeMillis(), l);
                } else {
                    zzjzVar = null;
                }
                zzks zzksVar = null;
                for (int i = 0; i < zzbb.size(); i++) {
                    zzks zzksVar2 = new zzks();
                    zzkqVar2.zzatj[i] = zzksVar2;
                    zzksVar2.name = zzbb.get(i).name;
                    zzksVar2.zzaun = Long.valueOf(zzbb.get(i).zzaqz);
                    zzgb().zza(zzksVar2, zzbb.get(i).value);
                    if (zzav && "_lte".equals(zzksVar2.name)) {
                        zzksVar2.zzate = (Long) zzjzVar.value;
                        zzksVar2.zzaun = Long.valueOf(zzbt().currentTimeMillis());
                        zzksVar = zzksVar2;
                    }
                }
                if (zzav && zzksVar == null) {
                    zzks zzksVar3 = new zzks();
                    zzksVar3.name = "_lte";
                    zzksVar3.zzaun = Long.valueOf(zzbt().currentTimeMillis());
                    zzksVar3.zzate = (Long) zzjzVar.value;
                    zzkqVar2.zzatj = (zzks[]) Arrays.copyOf(zzkqVar2.zzatj, zzkqVar2.zzatj.length + 1);
                    zzkqVar2.zzatj[zzkqVar2.zzatj.length - 1] = zzksVar3;
                }
                if (l.longValue() > 0) {
                    zzix().zza(zzjzVar);
                }
                Bundle zzif = zzeuVar.zzafq.zzif();
                if ("_iap".equals(zzeuVar.name)) {
                    zzif.putLong("_c", 1L);
                    zzge().zzis().log("Marking in-app purchase as real-time");
                    zzif.putLong("_r", 1L);
                }
                zzif.putString("_o", zzeuVar.origin);
                if (zzgb().zzcj(zzkqVar2.zzti)) {
                    zzgb().zza(zzif, "_dbg", (Object) 1L);
                    zzgb().zza(zzif, "_r", (Object) 1L);
                }
                zzeq zzf = zzix().zzf(str, zzeuVar.name);
                if (zzf == null) {
                    bundle = zzif;
                    bArr = null;
                    zzkqVar = zzkqVar2;
                    zzdyVar = zzbc;
                    zzkpVar = zzkpVar2;
                    zzix().zza(new zzeq(str, zzeuVar.name, 1L, 0L, zzeuVar.zzagb, 0L, null, null, null));
                    j = 0;
                } else {
                    bundle = zzif;
                    zzkqVar = zzkqVar2;
                    zzdyVar = zzbc;
                    zzkpVar = zzkpVar2;
                    bArr = null;
                    long j2 = zzf.zzaft;
                    zzix().zza(zzf.zzac(zzeuVar.zzagb).zzie());
                    j = j2;
                }
                zzep zzepVar = new zzep(this.zzacw, zzeuVar.origin, str, zzeuVar.name, zzeuVar.zzagb, j, bundle);
                zzkn zzknVar = new zzkn();
                zzkq zzkqVar3 = zzkqVar;
                zzkqVar3.zzati = new zzkn[]{zzknVar};
                zzknVar.zzatb = Long.valueOf(zzepVar.timestamp);
                zzknVar.name = zzepVar.name;
                zzknVar.zzatc = Long.valueOf(zzepVar.zzafp);
                zzknVar.zzata = new zzko[zzepVar.zzafq.size()];
                Iterator<String> it = zzepVar.zzafq.iterator();
                int i2 = 0;
                while (it.hasNext()) {
                    String next = it.next();
                    zzko zzkoVar = new zzko();
                    zzknVar.zzata[i2] = zzkoVar;
                    zzkoVar.name = next;
                    zzgb().zza(zzkoVar, zzepVar.zzafq.get(next));
                    i2++;
                }
                zzkqVar3.zzaua = zza(zzdyVar.zzah(), zzkqVar3.zzatj, zzkqVar3.zzati);
                zzkqVar3.zzatl = zzknVar.zzatb;
                zzkqVar3.zzatm = zzknVar.zzatb;
                long zzgl = zzdyVar.zzgl();
                zzkqVar3.zzato = zzgl != 0 ? Long.valueOf(zzgl) : bArr;
                long zzgk = zzdyVar.zzgk();
                if (zzgk != 0) {
                    zzgl = zzgk;
                }
                zzkqVar3.zzatn = zzgl != 0 ? Long.valueOf(zzgl) : bArr;
                zzdyVar.zzgt();
                zzkqVar3.zzaty = Integer.valueOf((int) zzdyVar.zzgq());
                zzkqVar3.zzatu = 12451L;
                zzkqVar3.zzatk = Long.valueOf(zzbt().currentTimeMillis());
                zzkqVar3.zzatz = Boolean.TRUE;
                zzdy zzdyVar2 = zzdyVar;
                zzdyVar2.zzm(zzkqVar3.zzatl.longValue());
                zzdyVar2.zzn(zzkqVar3.zzatm.longValue());
                zzix().zza(zzdyVar2);
                zzix().setTransactionSuccessful();
                try {
                    byte[] bArr2 = new byte[zzkpVar.zzvm()];
                    zzabw zzb = zzabw.zzb(bArr2, 0, bArr2.length);
                    zzkpVar.zza(zzb);
                    zzb.zzve();
                    return zzgb().zza(bArr2);
                } catch (IOException e) {
                    zzge().zzim().zze("Data loss. Failed to bundle and serialize. appId", zzfg.zzbm(str), e);
                    return bArr;
                }
            } else {
                zzge().zzis().zzg("Log and bundle disabled. package_name", str);
            }
            return new byte[0];
        } finally {
            zzix().endTransaction();
        }
    }

    @WorkerThread
    public void zzab() {
        zzgd().zzab();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @WorkerThread
    public final void zzb(zzed zzedVar, zzdz zzdzVar) {
        zzfi zzim;
        String str;
        Object zzbm;
        String zzbl;
        Object value;
        zzfi zzim2;
        String str2;
        Object zzbm2;
        String zzbl2;
        Object obj;
        Preconditions.checkNotNull(zzedVar);
        Preconditions.checkNotEmpty(zzedVar.packageName);
        Preconditions.checkNotNull(zzedVar.origin);
        Preconditions.checkNotNull(zzedVar.zzaep);
        Preconditions.checkNotEmpty(zzedVar.zzaep.name);
        zzab();
        zzkq();
        if (TextUtils.isEmpty(zzdzVar.zzadm)) {
            return;
        }
        if (!zzdzVar.zzadw) {
            zzg(zzdzVar);
            return;
        }
        zzed zzedVar2 = new zzed(zzedVar);
        boolean z = false;
        zzedVar2.active = false;
        zzix().beginTransaction();
        try {
            zzed zzi = zzix().zzi(zzedVar2.packageName, zzedVar2.zzaep.name);
            if (zzi != null && !zzi.origin.equals(zzedVar2.origin)) {
                zzge().zzip().zzd("Updating a conditional user property with different origin. name, origin, origin (from DB)", zzga().zzbl(zzedVar2.zzaep.name), zzedVar2.origin, zzi.origin);
            }
            if (zzi != null && zzi.active) {
                zzedVar2.origin = zzi.origin;
                zzedVar2.creationTimestamp = zzi.creationTimestamp;
                zzedVar2.triggerTimeout = zzi.triggerTimeout;
                zzedVar2.triggerEventName = zzi.triggerEventName;
                zzedVar2.zzaer = zzi.zzaer;
                zzedVar2.active = zzi.active;
                zzedVar2.zzaep = new zzjx(zzedVar2.zzaep.name, zzi.zzaep.zzaqz, zzedVar2.zzaep.getValue(), zzi.zzaep.origin);
            } else if (TextUtils.isEmpty(zzedVar2.triggerEventName)) {
                zzedVar2.zzaep = new zzjx(zzedVar2.zzaep.name, zzedVar2.creationTimestamp, zzedVar2.zzaep.getValue(), zzedVar2.zzaep.origin);
                zzedVar2.active = true;
                z = true;
            }
            if (zzedVar2.active) {
                zzjx zzjxVar = zzedVar2.zzaep;
                zzjz zzjzVar = new zzjz(zzedVar2.packageName, zzedVar2.origin, zzjxVar.name, zzjxVar.zzaqz, zzjxVar.getValue());
                if (zzix().zza(zzjzVar)) {
                    zzim2 = zzge().zzis();
                    str2 = "User property updated immediately";
                    zzbm2 = zzedVar2.packageName;
                    zzbl2 = zzga().zzbl(zzjzVar.name);
                    obj = zzjzVar.value;
                } else {
                    zzim2 = zzge().zzim();
                    str2 = "(2)Too many active user properties, ignoring";
                    zzbm2 = zzfg.zzbm(zzedVar2.packageName);
                    zzbl2 = zzga().zzbl(zzjzVar.name);
                    obj = zzjzVar.value;
                }
                zzim2.zzd(str2, zzbm2, zzbl2, obj);
                if (z && zzedVar2.zzaer != null) {
                    zzc(new zzeu(zzedVar2.zzaer, zzedVar2.creationTimestamp), zzdzVar);
                }
            }
            if (zzix().zza(zzedVar2)) {
                zzim = zzge().zzis();
                str = "Conditional property added";
                zzbm = zzedVar2.packageName;
                zzbl = zzga().zzbl(zzedVar2.zzaep.name);
                value = zzedVar2.zzaep.getValue();
            } else {
                zzim = zzge().zzim();
                str = "Too many conditional properties, ignoring";
                zzbm = zzfg.zzbm(zzedVar2.packageName);
                zzbl = zzga().zzbl(zzedVar2.zzaep.name);
                value = zzedVar2.zzaep.getValue();
            }
            zzim.zzd(str, zzbm, zzbl, value);
            zzix().setTransactionSuccessful();
        } finally {
            zzix().endTransaction();
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @WorkerThread
    public final void zzb(zzeu zzeuVar, zzdz zzdzVar) {
        List<zzed> zzb;
        List<zzed> zzb2;
        List<zzed> zzb3;
        zzfi zzim;
        String str;
        Object zzbm;
        String zzbl;
        Object obj;
        Preconditions.checkNotNull(zzdzVar);
        Preconditions.checkNotEmpty(zzdzVar.packageName);
        zzab();
        zzkq();
        String str2 = zzdzVar.packageName;
        long j = zzeuVar.zzagb;
        zzgb();
        if (zzka.zzd(zzeuVar, zzdzVar)) {
            if (!zzdzVar.zzadw) {
                zzg(zzdzVar);
                return;
            }
            zzix().beginTransaction();
            try {
                zzei zzix = zzix();
                Preconditions.checkNotEmpty(str2);
                zzix.zzab();
                zzix.zzch();
                int i = (j > 0L ? 1 : (j == 0L ? 0 : -1));
                if (i < 0) {
                    zzix.zzge().zzip().zze("Invalid time querying timed out conditional properties", zzfg.zzbm(str2), Long.valueOf(j));
                    zzb = Collections.emptyList();
                } else {
                    zzb = zzix.zzb("active=0 and app_id=? and abs(? - creation_timestamp) > trigger_timeout", new String[]{str2, String.valueOf(j)});
                }
                for (zzed zzedVar : zzb) {
                    if (zzedVar != null) {
                        zzge().zzis().zzd("User property timed out", zzedVar.packageName, zzga().zzbl(zzedVar.zzaep.name), zzedVar.zzaep.getValue());
                        if (zzedVar.zzaeq != null) {
                            zzc(new zzeu(zzedVar.zzaeq, j), zzdzVar);
                        }
                        zzix().zzj(str2, zzedVar.zzaep.name);
                    }
                }
                zzei zzix2 = zzix();
                Preconditions.checkNotEmpty(str2);
                zzix2.zzab();
                zzix2.zzch();
                if (i < 0) {
                    zzix2.zzge().zzip().zze("Invalid time querying expired conditional properties", zzfg.zzbm(str2), Long.valueOf(j));
                    zzb2 = Collections.emptyList();
                } else {
                    zzb2 = zzix2.zzb("active<>0 and app_id=? and abs(? - triggered_timestamp) > time_to_live", new String[]{str2, String.valueOf(j)});
                }
                ArrayList arrayList = new ArrayList(zzb2.size());
                for (zzed zzedVar2 : zzb2) {
                    if (zzedVar2 != null) {
                        zzge().zzis().zzd("User property expired", zzedVar2.packageName, zzga().zzbl(zzedVar2.zzaep.name), zzedVar2.zzaep.getValue());
                        zzix().zzg(str2, zzedVar2.zzaep.name);
                        if (zzedVar2.zzaes != null) {
                            arrayList.add(zzedVar2.zzaes);
                        }
                        zzix().zzj(str2, zzedVar2.zzaep.name);
                    }
                }
                ArrayList arrayList2 = arrayList;
                int size = arrayList2.size();
                int i2 = 0;
                while (i2 < size) {
                    Object obj2 = arrayList2.get(i2);
                    i2++;
                    zzc(new zzeu((zzeu) obj2, j), zzdzVar);
                }
                zzei zzix3 = zzix();
                String str3 = zzeuVar.name;
                Preconditions.checkNotEmpty(str2);
                Preconditions.checkNotEmpty(str3);
                zzix3.zzab();
                zzix3.zzch();
                if (i < 0) {
                    zzix3.zzge().zzip().zzd("Invalid time querying triggered conditional properties", zzfg.zzbm(str2), zzix3.zzga().zzbj(str3), Long.valueOf(j));
                    zzb3 = Collections.emptyList();
                } else {
                    zzb3 = zzix3.zzb("active=0 and app_id=? and trigger_event_name=? and abs(? - creation_timestamp) <= trigger_timeout", new String[]{str2, str3, String.valueOf(j)});
                }
                ArrayList arrayList3 = new ArrayList(zzb3.size());
                for (zzed zzedVar3 : zzb3) {
                    if (zzedVar3 != null) {
                        zzjx zzjxVar = zzedVar3.zzaep;
                        zzjz zzjzVar = new zzjz(zzedVar3.packageName, zzedVar3.origin, zzjxVar.name, j, zzjxVar.getValue());
                        if (zzix().zza(zzjzVar)) {
                            zzim = zzge().zzis();
                            str = "User property triggered";
                            zzbm = zzedVar3.packageName;
                            zzbl = zzga().zzbl(zzjzVar.name);
                            obj = zzjzVar.value;
                        } else {
                            zzim = zzge().zzim();
                            str = "Too many active user properties, ignoring";
                            zzbm = zzfg.zzbm(zzedVar3.packageName);
                            zzbl = zzga().zzbl(zzjzVar.name);
                            obj = zzjzVar.value;
                        }
                        zzim.zzd(str, zzbm, zzbl, obj);
                        if (zzedVar3.zzaer != null) {
                            arrayList3.add(zzedVar3.zzaer);
                        }
                        zzedVar3.zzaep = new zzjx(zzjzVar);
                        zzedVar3.active = true;
                        zzix().zza(zzedVar3);
                    }
                }
                zzc(zzeuVar, zzdzVar);
                ArrayList arrayList4 = arrayList3;
                int size2 = arrayList4.size();
                int i3 = 0;
                while (i3 < size2) {
                    Object obj3 = arrayList4.get(i3);
                    i3++;
                    zzc(new zzeu((zzeu) obj3, j), zzdzVar);
                }
                zzix().setTransactionSuccessful();
            } finally {
                zzix().endTransaction();
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public final void zzb(zzjq zzjqVar) {
        this.zzaqj++;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @WorkerThread
    public final void zzb(zzjx zzjxVar, zzdz zzdzVar) {
        zzab();
        zzkq();
        if (TextUtils.isEmpty(zzdzVar.zzadm)) {
            return;
        }
        if (!zzdzVar.zzadw) {
            zzg(zzdzVar);
            return;
        }
        int zzcf = zzgb().zzcf(zzjxVar.name);
        int i = 0;
        if (zzcf != 0) {
            zzgb();
            zzgb().zza(zzdzVar.packageName, zzcf, "_ev", zzka.zza(zzjxVar.name, 24, true), zzjxVar.name != null ? zzjxVar.name.length() : 0);
            return;
        }
        int zzi = zzgb().zzi(zzjxVar.name, zzjxVar.getValue());
        if (zzi != 0) {
            zzgb();
            String zza = zzka.zza(zzjxVar.name, 24, true);
            Object value = zzjxVar.getValue();
            if (value != null && ((value instanceof String) || (value instanceof CharSequence))) {
                i = String.valueOf(value).length();
            }
            zzgb().zza(zzdzVar.packageName, zzi, "_ev", zza, i);
            return;
        }
        Object zzj = zzgb().zzj(zzjxVar.name, zzjxVar.getValue());
        if (zzj == null) {
            return;
        }
        zzjz zzjzVar = new zzjz(zzdzVar.packageName, zzjxVar.origin, zzjxVar.name, zzjxVar.zzaqz, zzj);
        zzge().zzis().zze("Setting user property", zzga().zzbl(zzjzVar.name), zzj);
        zzix().beginTransaction();
        try {
            zzg(zzdzVar);
            boolean zza2 = zzix().zza(zzjzVar);
            zzix().setTransactionSuccessful();
            if (zza2) {
                zzge().zzis().zze("User property set", zzga().zzbl(zzjzVar.name), zzjzVar.value);
            } else {
                zzge().zzim().zze("Too many unique user properties are set. Ignoring user property", zzga().zzbl(zzjzVar.name), zzjzVar.value);
                zzgb().zza(zzdzVar.packageName, 9, (String) null, (String) null, 0);
            }
        } finally {
            zzix().endTransaction();
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* JADX WARN: Code restructure failed: missing block: B:29:0x00a8, code lost:
        zzgf().zzajw.set(zzbt().currentTimeMillis());
     */
    /* JADX WARN: Removed duplicated region for block: B:57:0x0124 A[Catch: all -> 0x0167, TryCatch #1 {all -> 0x000f, blocks: (B:4:0x000c, B:7:0x0012, B:65:0x0162, B:46:0x00ee, B:45:0x00ea, B:53:0x010b, B:8:0x002b, B:17:0x0047, B:64:0x015b, B:22:0x0061, B:29:0x00a8, B:30:0x00b9, B:33:0x00c1, B:36:0x00cd, B:38:0x00d3, B:43:0x00e0, B:55:0x0110, B:57:0x0124, B:59:0x0148, B:61:0x0152, B:63:0x0158, B:58:0x0132, B:49:0x00f7, B:51:0x0101), top: B:71:0x000c }] */
    /* JADX WARN: Removed duplicated region for block: B:58:0x0132 A[Catch: all -> 0x0167, TryCatch #1 {all -> 0x000f, blocks: (B:4:0x000c, B:7:0x0012, B:65:0x0162, B:46:0x00ee, B:45:0x00ea, B:53:0x010b, B:8:0x002b, B:17:0x0047, B:64:0x015b, B:22:0x0061, B:29:0x00a8, B:30:0x00b9, B:33:0x00c1, B:36:0x00cd, B:38:0x00d3, B:43:0x00e0, B:55:0x0110, B:57:0x0124, B:59:0x0148, B:61:0x0152, B:63:0x0158, B:58:0x0132, B:49:0x00f7, B:51:0x0101), top: B:71:0x000c }] */
    @com.google.android.gms.common.util.VisibleForTesting
    @androidx.annotation.WorkerThread
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public final void zzb(java.lang.String r7, int r8, java.lang.Throwable r9, byte[] r10, java.util.Map<java.lang.String, java.util.List<java.lang.String>> r11) {
        /*
            Method dump skipped, instructions count: 374
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.android.gms.internal.measurement.zzjr.zzb(java.lang.String, int, java.lang.Throwable, byte[], java.util.Map):void");
    }

    @Override // com.google.android.gms.internal.measurement.zzec
    public Clock zzbt() {
        return this.zzacw.zzbt();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @WorkerThread
    public final void zzc(zzed zzedVar, zzdz zzdzVar) {
        Preconditions.checkNotNull(zzedVar);
        Preconditions.checkNotEmpty(zzedVar.packageName);
        Preconditions.checkNotNull(zzedVar.zzaep);
        Preconditions.checkNotEmpty(zzedVar.zzaep.name);
        zzab();
        zzkq();
        if (TextUtils.isEmpty(zzdzVar.zzadm)) {
            return;
        }
        if (!zzdzVar.zzadw) {
            zzg(zzdzVar);
            return;
        }
        zzix().beginTransaction();
        try {
            zzg(zzdzVar);
            zzed zzi = zzix().zzi(zzedVar.packageName, zzedVar.zzaep.name);
            if (zzi != null) {
                zzge().zzis().zze("Removing conditional user property", zzedVar.packageName, zzga().zzbl(zzedVar.zzaep.name));
                zzix().zzj(zzedVar.packageName, zzedVar.zzaep.name);
                if (zzi.active) {
                    zzix().zzg(zzedVar.packageName, zzedVar.zzaep.name);
                }
                if (zzedVar.zzaes != null) {
                    zzc(zzgb().zza(zzedVar.zzaes.name, zzedVar.zzaes.zzafq != null ? zzedVar.zzaes.zzafq.zzif() : null, zzi.origin, zzedVar.zzaes.zzagb, true, false), zzdzVar);
                }
            } else {
                zzge().zzip().zze("Conditional user property doesn't exist", zzfg.zzbm(zzedVar.packageName), zzga().zzbl(zzedVar.zzaep.name));
            }
            zzix().setTransactionSuccessful();
        } finally {
            zzix().endTransaction();
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @WorkerThread
    public final void zzc(zzeu zzeuVar, String str) {
        zzdy zzbc = zzix().zzbc(str);
        if (zzbc == null || TextUtils.isEmpty(zzbc.zzag())) {
            zzge().zzis().zzg("No app data available; dropping event", str);
            return;
        }
        Boolean zzc = zzc(zzbc);
        if (zzc == null) {
            if (!"_ui".equals(zzeuVar.name)) {
                zzge().zzip().zzg("Could not find package. appId", zzfg.zzbm(str));
            }
        } else if (!zzc.booleanValue()) {
            zzge().zzim().zzg("App version does not match; dropping event. appId", zzfg.zzbm(str));
            return;
        }
        zzb(zzeuVar, new zzdz(str, zzbc.getGmpAppId(), zzbc.zzag(), zzbc.zzgm(), zzbc.zzgn(), zzbc.zzgo(), zzbc.zzgp(), (String) null, zzbc.isMeasurementEnabled(), false, zzbc.zzgj(), zzbc.zzhc(), 0L, 0, zzbc.zzhd(), zzbc.zzhe(), false));
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @WorkerThread
    public final void zzc(zzjx zzjxVar, zzdz zzdzVar) {
        zzab();
        zzkq();
        if (TextUtils.isEmpty(zzdzVar.zzadm)) {
            return;
        }
        if (!zzdzVar.zzadw) {
            zzg(zzdzVar);
            return;
        }
        zzge().zzis().zzg("Removing user property", zzga().zzbl(zzjxVar.name));
        zzix().beginTransaction();
        try {
            zzg(zzdzVar);
            zzix().zzg(zzdzVar.packageName, zzjxVar.name);
            zzix().setTransactionSuccessful();
            zzge().zzis().zzg("User property removed", zzga().zzbl(zzjxVar.name));
        } finally {
            zzix().endTransaction();
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @WorkerThread
    public final zzdz zzcb(String str) {
        zzfi zzis;
        String str2;
        Object obj;
        String str3 = str;
        zzdy zzbc = zzix().zzbc(str3);
        if (zzbc == null || TextUtils.isEmpty(zzbc.zzag())) {
            zzis = zzge().zzis();
            str2 = "No app data available; dropping";
            obj = str3;
        } else {
            Boolean zzc = zzc(zzbc);
            if (zzc == null || zzc.booleanValue()) {
                return new zzdz(str, zzbc.getGmpAppId(), zzbc.zzag(), zzbc.zzgm(), zzbc.zzgn(), zzbc.zzgo(), zzbc.zzgp(), (String) null, zzbc.isMeasurementEnabled(), false, zzbc.zzgj(), zzbc.zzhc(), 0L, 0, zzbc.zzhd(), zzbc.zzhe(), false);
            }
            zzis = zzge().zzim();
            str2 = "App version does not match; dropping. appId";
            obj = zzfg.zzbm(str);
        }
        zzis.zzg(str2, obj);
        return null;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @VisibleForTesting
    @WorkerThread
    public final void zzd(zzdz zzdzVar) {
        if (this.zzaqq != null) {
            this.zzaqr = new ArrayList();
            this.zzaqr.addAll(this.zzaqq);
        }
        zzei zzix = zzix();
        String str = zzdzVar.packageName;
        Preconditions.checkNotEmpty(str);
        zzix.zzab();
        zzix.zzch();
        try {
            SQLiteDatabase writableDatabase = zzix.getWritableDatabase();
            String[] strArr = {str};
            int delete = writableDatabase.delete("apps", "app_id=?", strArr) + 0 + writableDatabase.delete("events", "app_id=?", strArr) + writableDatabase.delete("user_attributes", "app_id=?", strArr) + writableDatabase.delete("conditional_properties", "app_id=?", strArr) + writableDatabase.delete("raw_events", "app_id=?", strArr) + writableDatabase.delete("raw_events_metadata", "app_id=?", strArr) + writableDatabase.delete("queue", "app_id=?", strArr) + writableDatabase.delete("audience_filter_values", "app_id=?", strArr) + writableDatabase.delete("main_event_params", "app_id=?", strArr);
            if (delete > 0) {
                zzix.zzge().zzit().zze("Reset analytics data. app, records", str, Integer.valueOf(delete));
            }
        } catch (SQLiteException e) {
            zzix.zzge().zzim().zze("Error resetting analytics data. appId, error", zzfg.zzbm(str), e);
        }
        zzdz zza = zza(getContext(), zzdzVar.packageName, zzdzVar.zzadm, zzdzVar.zzadw, zzdzVar.zzady, zzdzVar.zzadz, zzdzVar.zzaem);
        if (!zzgg().zzaz(zzdzVar.packageName) || zzdzVar.zzadw) {
            zzf(zza);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public final void zze(zzdz zzdzVar) {
        zzab();
        zzkq();
        Preconditions.checkNotEmpty(zzdzVar.packageName);
        zzg(zzdzVar);
    }

    /* JADX WARN: Removed duplicated region for block: B:111:0x0398 A[Catch: all -> 0x03c1, TryCatch #0 {all -> 0x03c1, blocks: (B:25:0x008d, B:27:0x009b, B:29:0x00a1, B:31:0x00ad, B:32:0x00d3, B:34:0x012f, B:37:0x0142, B:40:0x0157, B:42:0x0162, B:44:0x016c, B:45:0x018a, B:46:0x018e, B:48:0x0194, B:50:0x01a0, B:51:0x01bf, B:53:0x01c4, B:54:0x01cc, B:59:0x01e0, B:61:0x01eb, B:63:0x0236, B:65:0x023a, B:66:0x023f, B:68:0x0249, B:97:0x02f3, B:99:0x030e, B:100:0x0313, B:109:0x037a, B:110:0x0394, B:114:0x03b2, B:70:0x025e, B:75:0x0286, B:77:0x028c, B:79:0x0294, B:81:0x029c, B:85:0x02a5, B:86:0x02b5, B:89:0x02c6, B:91:0x02dd, B:93:0x02e3, B:94:0x02e8, B:96:0x02ee, B:73:0x026f, B:103:0x032b, B:105:0x035e, B:107:0x0362, B:108:0x0367, B:111:0x0398, B:113:0x039c, B:56:0x01d3), top: B:120:0x008d, inners: #1, #2, #3 }] */
    /* JADX WARN: Removed duplicated region for block: B:59:0x01e0 A[Catch: all -> 0x03c1, TryCatch #0 {all -> 0x03c1, blocks: (B:25:0x008d, B:27:0x009b, B:29:0x00a1, B:31:0x00ad, B:32:0x00d3, B:34:0x012f, B:37:0x0142, B:40:0x0157, B:42:0x0162, B:44:0x016c, B:45:0x018a, B:46:0x018e, B:48:0x0194, B:50:0x01a0, B:51:0x01bf, B:53:0x01c4, B:54:0x01cc, B:59:0x01e0, B:61:0x01eb, B:63:0x0236, B:65:0x023a, B:66:0x023f, B:68:0x0249, B:97:0x02f3, B:99:0x030e, B:100:0x0313, B:109:0x037a, B:110:0x0394, B:114:0x03b2, B:70:0x025e, B:75:0x0286, B:77:0x028c, B:79:0x0294, B:81:0x029c, B:85:0x02a5, B:86:0x02b5, B:89:0x02c6, B:91:0x02dd, B:93:0x02e3, B:94:0x02e8, B:96:0x02ee, B:73:0x026f, B:103:0x032b, B:105:0x035e, B:107:0x0362, B:108:0x0367, B:111:0x0398, B:113:0x039c, B:56:0x01d3), top: B:120:0x008d, inners: #1, #2, #3 }] */
    @androidx.annotation.WorkerThread
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public final void zzf(com.google.android.gms.internal.measurement.zzdz r19) {
        /*
            Method dump skipped, instructions count: 970
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.android.gms.internal.measurement.zzjr.zzf(com.google.android.gms.internal.measurement.zzdz):void");
    }

    public zzeo zzfw() {
        return this.zzacw.zzfw();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @WorkerThread
    public final void zzg(Runnable runnable) {
        zzab();
        if (this.zzaqi == null) {
            this.zzaqi = new ArrayList();
        }
        this.zzaqi.add(runnable);
    }

    public zzfe zzga() {
        return this.zzacw.zzga();
    }

    public zzka zzgb() {
        return this.zzacw.zzgb();
    }

    @Override // com.google.android.gms.internal.measurement.zzec
    public zzgg zzgd() {
        return this.zzacw.zzgd();
    }

    @Override // com.google.android.gms.internal.measurement.zzec
    public zzfg zzge() {
        return this.zzacw.zzge();
    }

    public zzfr zzgf() {
        return this.zzacw.zzgf();
    }

    public zzef zzgg() {
        return this.zzacw.zzgg();
    }

    public final String zzh(zzdz zzdzVar) {
        try {
            return (String) zzgd().zzb(new zzju(this, zzdzVar)).get(30000L, TimeUnit.MILLISECONDS);
        } catch (InterruptedException | ExecutionException | TimeoutException e) {
            zzge().zzim().zze("Failed to get app instance id. appId", zzfg.zzbm(zzdzVar.packageName), e);
            return null;
        }
    }

    public final zzeb zziw() {
        zza(this.zzaqf);
        return this.zzaqf;
    }

    public final zzei zzix() {
        zza(this.zzaqc);
        return this.zzaqc;
    }

    public final zzfk zzkn() {
        zza(this.zzaqb);
        return this.zzaqb;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public final void zzkq() {
        if (!this.zzvo) {
            throw new IllegalStateException("UploadController is not initialized");
        }
    }

    @WorkerThread
    public final void zzks() {
        zzdy zzbc;
        String str;
        zzfi zzit;
        String str2;
        zzab();
        zzkq();
        this.zzaqn = true;
        try {
            Boolean zzkf = this.zzacw.zzfx().zzkf();
            if (zzkf == null) {
                zzit = zzge().zzip();
                str2 = "Upload data called on the client side before use of service was decided";
            } else if (!zzkf.booleanValue()) {
                if (this.zzaqh <= 0) {
                    zzab();
                    if (this.zzaqq != null) {
                        zzit = zzge().zzit();
                        str2 = "Uploading requested multiple times";
                    } else if (zzkn().zzex()) {
                        long currentTimeMillis = zzbt().currentTimeMillis();
                        zzd(null, currentTimeMillis - zzef.zzhi());
                        long j = zzgf().zzaju.get();
                        if (j != 0) {
                            zzge().zzis().zzg("Uploading events. Elapsed time since last upload attempt (ms)", Long.valueOf(Math.abs(currentTimeMillis - j)));
                        }
                        String zzhn = zzix().zzhn();
                        if (TextUtils.isEmpty(zzhn)) {
                            this.zzaqs = -1L;
                            String zzab = zzix().zzab(currentTimeMillis - zzef.zzhi());
                            if (!TextUtils.isEmpty(zzab) && (zzbc = zzix().zzbc(zzab)) != null) {
                                zzb(zzbc);
                            }
                        } else {
                            if (this.zzaqs == -1) {
                                this.zzaqs = zzix().zzhu();
                            }
                            List<Pair<zzkq, Long>> zzb = zzix().zzb(zzhn, zzgg().zzb(zzhn, zzew.zzago), Math.max(0, zzgg().zzb(zzhn, zzew.zzagp)));
                            if (!zzb.isEmpty()) {
                                Iterator<Pair<zzkq, Long>> it = zzb.iterator();
                                while (true) {
                                    if (!it.hasNext()) {
                                        str = null;
                                        break;
                                    }
                                    zzkq zzkqVar = (zzkq) it.next().first;
                                    if (!TextUtils.isEmpty(zzkqVar.zzatv)) {
                                        str = zzkqVar.zzatv;
                                        break;
                                    }
                                }
                                if (str != null) {
                                    int i = 0;
                                    while (true) {
                                        if (i >= zzb.size()) {
                                            break;
                                        }
                                        zzkq zzkqVar2 = (zzkq) zzb.get(i).first;
                                        if (!TextUtils.isEmpty(zzkqVar2.zzatv) && !zzkqVar2.zzatv.equals(str)) {
                                            zzb = zzb.subList(0, i);
                                            break;
                                        }
                                        i++;
                                    }
                                }
                                zzkp zzkpVar = new zzkp();
                                zzkpVar.zzatf = new zzkq[zzb.size()];
                                ArrayList arrayList = new ArrayList(zzb.size());
                                boolean z = zzef.zzhk() && zzgg().zzat(zzhn);
                                for (int i2 = 0; i2 < zzkpVar.zzatf.length; i2++) {
                                    zzkpVar.zzatf[i2] = (zzkq) zzb.get(i2).first;
                                    arrayList.add((Long) zzb.get(i2).second);
                                    zzkpVar.zzatf[i2].zzatu = 12451L;
                                    zzkpVar.zzatf[i2].zzatk = Long.valueOf(currentTimeMillis);
                                    zzkpVar.zzatf[i2].zzatz = false;
                                    if (!z) {
                                        zzkpVar.zzatf[i2].zzauh = null;
                                    }
                                }
                                String zza = zzge().isLoggable(2) ? zzga().zza(zzkpVar) : null;
                                byte[] zzb2 = zzgb().zzb(zzkpVar);
                                String str3 = zzew.zzagy.get();
                                try {
                                    URL url = new URL(str3);
                                    Preconditions.checkArgument(!arrayList.isEmpty());
                                    if (this.zzaqq != null) {
                                        zzge().zzim().log("Set uploading progress before finishing the previous upload");
                                    } else {
                                        this.zzaqq = new ArrayList(arrayList);
                                    }
                                    zzgf().zzajv.set(currentTimeMillis);
                                    zzge().zzit().zzd("Uploading data. app, uncompressed size, data", zzkpVar.zzatf.length > 0 ? zzkpVar.zzatf[0].zzti : "?", Integer.valueOf(zzb2.length), zza);
                                    this.zzaqm = true;
                                    zzfk zzkn = zzkn();
                                    zzjs zzjsVar = new zzjs(this, zzhn);
                                    zzkn.zzab();
                                    zzkn.zzch();
                                    Preconditions.checkNotNull(url);
                                    Preconditions.checkNotNull(zzb2);
                                    Preconditions.checkNotNull(zzjsVar);
                                    zzkn.zzgd().zzd(new zzfo(zzkn, zzhn, url, zzb2, null, zzjsVar));
                                } catch (MalformedURLException unused) {
                                    zzge().zzim().zze("Failed to parse upload URL. Not uploading. appId", zzfg.zzbm(zzhn), str3);
                                }
                            }
                        }
                    } else {
                        zzge().zzit().log("Network not connected, ignoring upload request");
                    }
                }
                zzku();
            } else {
                zzit = zzge().zzim();
                str2 = "Upload called in the client side when service should be used";
            }
            zzit.log(str2);
        } finally {
            this.zzaqn = false;
            zzkv();
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @WorkerThread
    public final void zzkx() {
        zzfi zzim;
        String str;
        zzab();
        zzkq();
        if (this.zzaqg) {
            return;
        }
        zzge().zzir().log("This instance being marked as an uploader");
        zzab();
        zzkq();
        if (zzky() && zzkw()) {
            int zza = zza(this.zzaqp);
            int zzij = this.zzacw.zzfv().zzij();
            zzab();
            if (zza > zzij) {
                zzim = zzge().zzim();
                str = "Panic: can't downgrade version. Previous, current version";
            } else if (zza < zzij) {
                if (zza(zzij, this.zzaqp)) {
                    zzim = zzge().zzit();
                    str = "Storage version upgraded. Previous, current version";
                } else {
                    zzim = zzge().zzim();
                    str = "Storage version upgrade failed. Previous, current version";
                }
            }
            zzim.zze(str, Integer.valueOf(zza), Integer.valueOf(zzij));
        }
        this.zzaqg = true;
        zzku();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public final void zzkz() {
        this.zzaqk++;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public final zzgl zzla() {
        return this.zzacw;
    }

    public final void zzm(boolean z) {
        zzku();
    }
}
