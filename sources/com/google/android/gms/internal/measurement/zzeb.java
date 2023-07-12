package com.google.android.gms.internal.measurement;

import com.github.mikephil.charting.utils.Utils;
import com.google.android.gms.common.internal.Preconditions;
import com.google.android.gms.common.util.VisibleForTesting;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: classes.dex */
public final class zzeb extends zzjq {
    /* JADX INFO: Access modifiers changed from: package-private */
    public zzeb(zzjr zzjrVar) {
        super(zzjrVar);
    }

    private final Boolean zza(double d, zzkg zzkgVar) {
        try {
            return zza(new BigDecimal(d), zzkgVar, Math.ulp(d));
        } catch (NumberFormatException unused) {
            return null;
        }
    }

    private final Boolean zza(long j, zzkg zzkgVar) {
        try {
            return zza(new BigDecimal(j), zzkgVar, (double) Utils.DOUBLE_EPSILON);
        } catch (NumberFormatException unused) {
            return null;
        }
    }

    @VisibleForTesting
    private static Boolean zza(Boolean bool, boolean z) {
        if (bool == null) {
            return null;
        }
        return Boolean.valueOf(bool.booleanValue() ^ z);
    }

    private final Boolean zza(String str, int i, boolean z, String str2, List<String> list, String str3) {
        boolean startsWith;
        if (str == null) {
            return null;
        }
        if (i == 6) {
            if (list == null || list.size() == 0) {
                return null;
            }
        } else if (str2 == null) {
            return null;
        }
        if (!z && i != 1) {
            str = str.toUpperCase(Locale.ENGLISH);
        }
        switch (i) {
            case 1:
                try {
                    return Boolean.valueOf(Pattern.compile(str3, z ? 0 : 66).matcher(str).matches());
                } catch (PatternSyntaxException unused) {
                    zzge().zzip().zzg("Invalid regular expression in REGEXP audience filter. expression", str3);
                    return null;
                }
            case 2:
                startsWith = str.startsWith(str2);
                break;
            case 3:
                startsWith = str.endsWith(str2);
                break;
            case 4:
                startsWith = str.contains(str2);
                break;
            case 5:
                startsWith = str.equals(str2);
                break;
            case 6:
                startsWith = list.contains(str);
                break;
            default:
                return null;
        }
        return Boolean.valueOf(startsWith);
    }

    private final Boolean zza(String str, zzkg zzkgVar) {
        if (zzka.zzck(str)) {
            try {
                return zza(new BigDecimal(str), zzkgVar, (double) Utils.DOUBLE_EPSILON);
            } catch (NumberFormatException unused) {
                return null;
            }
        }
        return null;
    }

    @VisibleForTesting
    private final Boolean zza(String str, zzki zzkiVar) {
        ArrayList arrayList;
        Preconditions.checkNotNull(zzkiVar);
        if (str == null || zzkiVar.zzash == null || zzkiVar.zzash.intValue() == 0) {
            return null;
        }
        if (zzkiVar.zzash.intValue() == 6) {
            if (zzkiVar.zzask == null || zzkiVar.zzask.length == 0) {
                return null;
            }
        } else if (zzkiVar.zzasi == null) {
            return null;
        }
        int intValue = zzkiVar.zzash.intValue();
        boolean z = zzkiVar.zzasj != null && zzkiVar.zzasj.booleanValue();
        String upperCase = (z || intValue == 1 || intValue == 6) ? zzkiVar.zzasi : zzkiVar.zzasi.toUpperCase(Locale.ENGLISH);
        if (zzkiVar.zzask == null) {
            arrayList = null;
        } else {
            String[] strArr = zzkiVar.zzask;
            if (z) {
                arrayList = Arrays.asList(strArr);
            } else {
                ArrayList arrayList2 = new ArrayList();
                for (String str2 : strArr) {
                    arrayList2.add(str2.toUpperCase(Locale.ENGLISH));
                }
                arrayList = arrayList2;
            }
        }
        return zza(str, intValue, z, upperCase, arrayList, intValue == 1 ? upperCase : null);
    }

    /* JADX WARN: Code restructure failed: missing block: B:35:0x0071, code lost:
        if (r3 != null) goto L24;
     */
    @com.google.android.gms.common.util.VisibleForTesting
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    private static java.lang.Boolean zza(java.math.BigDecimal r7, com.google.android.gms.internal.measurement.zzkg r8, double r9) {
        /*
            Method dump skipped, instructions count: 252
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.android.gms.internal.measurement.zzeb.zza(java.math.BigDecimal, com.google.android.gms.internal.measurement.zzkg, double):java.lang.Boolean");
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Removed duplicated region for block: B:106:0x02f9  */
    /* JADX WARN: Removed duplicated region for block: B:107:0x034b  */
    /* JADX WARN: Removed duplicated region for block: B:110:0x0371  */
    /* JADX WARN: Removed duplicated region for block: B:117:0x0391  */
    /* JADX WARN: Removed duplicated region for block: B:225:0x06ab  */
    /* JADX WARN: Removed duplicated region for block: B:226:0x06ae  */
    /* JADX WARN: Removed duplicated region for block: B:229:0x06b4  */
    /* JADX WARN: Removed duplicated region for block: B:230:0x06bc  */
    /* JADX WARN: Removed duplicated region for block: B:306:0x0970  */
    /* JADX WARN: Removed duplicated region for block: B:307:0x0973  */
    /* JADX WARN: Removed duplicated region for block: B:310:0x0979  */
    /* JADX WARN: Removed duplicated region for block: B:311:0x0982  */
    /* JADX WARN: Removed duplicated region for block: B:38:0x0145  */
    /* JADX WARN: Removed duplicated region for block: B:74:0x0227  */
    /* JADX WARN: Removed duplicated region for block: B:80:0x0244  */
    /* JADX WARN: Removed duplicated region for block: B:88:0x0263  */
    /* JADX WARN: Removed duplicated region for block: B:91:0x0295  */
    /* JADX WARN: Type inference failed for: r3v77 */
    /* JADX WARN: Type inference failed for: r3v78, types: [int] */
    /* JADX WARN: Type inference failed for: r3v79, types: [int] */
    /* JADX WARN: Type inference failed for: r4v15 */
    /* JADX WARN: Type inference failed for: r4v16, types: [int] */
    /* JADX WARN: Type inference failed for: r4v18 */
    /* JADX WARN: Type inference failed for: r4v19, types: [int] */
    /* JADX WARN: Type inference failed for: r5v34 */
    /* JADX WARN: Type inference failed for: r5v35, types: [int] */
    /* JADX WARN: Type inference failed for: r5v37, types: [int] */
    /* JADX WARN: Type inference failed for: r8v44 */
    /* JADX WARN: Type inference failed for: r8v45, types: [int] */
    @androidx.annotation.WorkerThread
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public final com.google.android.gms.internal.measurement.zzkm[] zza(java.lang.String r63, com.google.android.gms.internal.measurement.zzkn[] r64, com.google.android.gms.internal.measurement.zzks[] r65) {
        /*
            Method dump skipped, instructions count: 2804
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.android.gms.internal.measurement.zzeb.zza(java.lang.String, com.google.android.gms.internal.measurement.zzkn[], com.google.android.gms.internal.measurement.zzks[]):com.google.android.gms.internal.measurement.zzkm[]");
    }

    @Override // com.google.android.gms.internal.measurement.zzjq
    protected final boolean zzhf() {
        return false;
    }
}
