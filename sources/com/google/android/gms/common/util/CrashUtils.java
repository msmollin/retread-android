package com.google.android.gms.common.util;

import android.content.Context;
import android.os.DropBoxManager;
import android.text.TextUtils;
import android.util.Log;
import com.google.android.gms.common.internal.Preconditions;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.ArrayList;
import java.util.LinkedList;
import javax.annotation.concurrent.GuardedBy;

/* loaded from: classes.dex */
public final class CrashUtils {
    private static boolean zzzf;
    private static boolean zzzg;
    private static final String[] zzzc = {"android.", "com.android.", "dalvik.", "java.", "javax."};
    private static DropBoxManager zzzd = null;
    private static boolean zzze = false;
    private static int zzzh = -1;
    @GuardedBy("CrashUtils.class")
    private static int zzzi = 0;
    @GuardedBy("CrashUtils.class")
    private static int zzzj = 0;

    @Retention(RetentionPolicy.SOURCE)
    /* loaded from: classes.dex */
    public @interface ErrorDialogData {
        public static final int AVG_CRASH_FREQ = 2;
        public static final int BINDER_CRASH = 268435456;
        public static final int DYNAMITE_CRASH = 536870912;
        public static final int FORCED_SHUSHED_BY_WRAPPER = 4;
        public static final int NONE = 0;
        public static final int POPUP_FREQ = 1;
        public static final int SUPPRESSED = 1073741824;
    }

    public static boolean addDynamiteErrorToDropBox(Context context, Throwable th) {
        return addErrorToDropBoxInternal(context, th, ErrorDialogData.DYNAMITE_CRASH);
    }

    @Deprecated
    public static boolean addErrorToDropBox(Context context, Throwable th) {
        return addDynamiteErrorToDropBox(context, th);
    }

    public static boolean addErrorToDropBoxInternal(Context context, String str, String str2, int i) {
        return zza(context, str, str2, i, null);
    }

    public static boolean addErrorToDropBoxInternal(Context context, Throwable th, int i) {
        boolean z;
        try {
            Preconditions.checkNotNull(context);
            Preconditions.checkNotNull(th);
            if (isPackageSide()) {
                if (zzdb() || (th = zza(th)) != null) {
                    return zza(context, Log.getStackTraceString(th), ProcessUtils.getMyProcessName(), i, th);
                }
                return false;
            }
            return false;
        } catch (Exception e) {
            try {
                z = zzdb();
            } catch (Exception e2) {
                Log.e("CrashUtils", "Error determining which process we're running in!", e2);
                z = false;
            }
            if (z) {
                throw e;
            }
            Log.e("CrashUtils", "Error adding exception to DropBox!", e);
            return false;
        }
    }

    private static boolean isPackageSide() {
        if (zzze) {
            return zzzf;
        }
        return false;
    }

    public static boolean isSystemClassPrefixInternal(String str) {
        if (TextUtils.isEmpty(str)) {
            return false;
        }
        for (String str2 : zzzc) {
            if (str.startsWith(str2)) {
                return true;
            }
        }
        return false;
    }

    @VisibleForTesting
    public static synchronized void setTestVariables(DropBoxManager dropBoxManager, boolean z, boolean z2, int i) {
        synchronized (CrashUtils.class) {
            zzze = true;
            zzzd = dropBoxManager;
            zzzg = z;
            zzzf = z2;
            zzzh = i;
            zzzi = 0;
            zzzj = 0;
        }
    }

    /* JADX WARN: Removed duplicated region for block: B:19:0x005d A[Catch: all -> 0x0198, TryCatch #4 {, blocks: (B:4:0x0003, B:7:0x002c, B:8:0x003a, B:10:0x003e, B:16:0x0048, B:17:0x004f, B:19:0x005d, B:21:0x0065, B:23:0x006d, B:25:0x0075, B:26:0x007f, B:27:0x0089, B:28:0x0096, B:30:0x00b0, B:32:0x00b7, B:33:0x00c4, B:35:0x00cf, B:36:0x00d2, B:38:0x00d8, B:40:0x00de, B:44:0x00ed, B:56:0x0172, B:70:0x018e, B:71:0x0191, B:67:0x0188, B:72:0x0192, B:41:0x00e1), top: B:86:0x0003 }] */
    /* JADX WARN: Removed duplicated region for block: B:30:0x00b0 A[Catch: all -> 0x0198, TryCatch #4 {, blocks: (B:4:0x0003, B:7:0x002c, B:8:0x003a, B:10:0x003e, B:16:0x0048, B:17:0x004f, B:19:0x005d, B:21:0x0065, B:23:0x006d, B:25:0x0075, B:26:0x007f, B:27:0x0089, B:28:0x0096, B:30:0x00b0, B:32:0x00b7, B:33:0x00c4, B:35:0x00cf, B:36:0x00d2, B:38:0x00d8, B:40:0x00de, B:44:0x00ed, B:56:0x0172, B:70:0x018e, B:71:0x0191, B:67:0x0188, B:72:0x0192, B:41:0x00e1), top: B:86:0x0003 }] */
    /* JADX WARN: Removed duplicated region for block: B:32:0x00b7 A[Catch: all -> 0x0198, TryCatch #4 {, blocks: (B:4:0x0003, B:7:0x002c, B:8:0x003a, B:10:0x003e, B:16:0x0048, B:17:0x004f, B:19:0x005d, B:21:0x0065, B:23:0x006d, B:25:0x0075, B:26:0x007f, B:27:0x0089, B:28:0x0096, B:30:0x00b0, B:32:0x00b7, B:33:0x00c4, B:35:0x00cf, B:36:0x00d2, B:38:0x00d8, B:40:0x00de, B:44:0x00ed, B:56:0x0172, B:70:0x018e, B:71:0x0191, B:67:0x0188, B:72:0x0192, B:41:0x00e1), top: B:86:0x0003 }] */
    /* JADX WARN: Removed duplicated region for block: B:35:0x00cf A[Catch: all -> 0x0198, TryCatch #4 {, blocks: (B:4:0x0003, B:7:0x002c, B:8:0x003a, B:10:0x003e, B:16:0x0048, B:17:0x004f, B:19:0x005d, B:21:0x0065, B:23:0x006d, B:25:0x0075, B:26:0x007f, B:27:0x0089, B:28:0x0096, B:30:0x00b0, B:32:0x00b7, B:33:0x00c4, B:35:0x00cf, B:36:0x00d2, B:38:0x00d8, B:40:0x00de, B:44:0x00ed, B:56:0x0172, B:70:0x018e, B:71:0x0191, B:67:0x0188, B:72:0x0192, B:41:0x00e1), top: B:86:0x0003 }] */
    /* JADX WARN: Removed duplicated region for block: B:38:0x00d8 A[Catch: all -> 0x0198, TryCatch #4 {, blocks: (B:4:0x0003, B:7:0x002c, B:8:0x003a, B:10:0x003e, B:16:0x0048, B:17:0x004f, B:19:0x005d, B:21:0x0065, B:23:0x006d, B:25:0x0075, B:26:0x007f, B:27:0x0089, B:28:0x0096, B:30:0x00b0, B:32:0x00b7, B:33:0x00c4, B:35:0x00cf, B:36:0x00d2, B:38:0x00d8, B:40:0x00de, B:44:0x00ed, B:56:0x0172, B:70:0x018e, B:71:0x0191, B:67:0x0188, B:72:0x0192, B:41:0x00e1), top: B:86:0x0003 }] */
    /* JADX WARN: Removed duplicated region for block: B:42:0x00ea  */
    /* JADX WARN: Removed duplicated region for block: B:44:0x00ed A[Catch: all -> 0x0198, TRY_LEAVE, TryCatch #4 {, blocks: (B:4:0x0003, B:7:0x002c, B:8:0x003a, B:10:0x003e, B:16:0x0048, B:17:0x004f, B:19:0x005d, B:21:0x0065, B:23:0x006d, B:25:0x0075, B:26:0x007f, B:27:0x0089, B:28:0x0096, B:30:0x00b0, B:32:0x00b7, B:33:0x00c4, B:35:0x00cf, B:36:0x00d2, B:38:0x00d8, B:40:0x00de, B:44:0x00ed, B:56:0x0172, B:70:0x018e, B:71:0x0191, B:67:0x0188, B:72:0x0192, B:41:0x00e1), top: B:86:0x0003 }] */
    @com.google.android.gms.common.util.VisibleForTesting
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    private static synchronized java.lang.String zza(android.content.Context r7, java.lang.String r8, java.lang.String r9, int r10) {
        /*
            Method dump skipped, instructions count: 411
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.android.gms.common.util.CrashUtils.zza(android.content.Context, java.lang.String, java.lang.String, int):java.lang.String");
    }

    @VisibleForTesting
    private static synchronized Throwable zza(Throwable th) {
        synchronized (CrashUtils.class) {
            LinkedList linkedList = new LinkedList();
            while (th != null) {
                linkedList.push(th);
                th = th.getCause();
            }
            Throwable th2 = null;
            boolean z = false;
            while (!linkedList.isEmpty()) {
                Throwable th3 = (Throwable) linkedList.pop();
                StackTraceElement[] stackTrace = th3.getStackTrace();
                ArrayList arrayList = new ArrayList();
                arrayList.add(new StackTraceElement(th3.getClass().getName(), "<filtered>", "<filtered>", 1));
                boolean z2 = z;
                for (StackTraceElement stackTraceElement : stackTrace) {
                    String className = stackTraceElement.getClassName();
                    String fileName = stackTraceElement.getFileName();
                    boolean z3 = !TextUtils.isEmpty(fileName) && fileName.startsWith(":com.google.android.gms");
                    z2 |= z3;
                    if (!z3 && !isSystemClassPrefixInternal(className)) {
                        stackTraceElement = new StackTraceElement("<filtered>", "<filtered>", "<filtered>", 1);
                    }
                    arrayList.add(stackTraceElement);
                }
                th2 = th2 == null ? new Throwable("<filtered>") : new Throwable("<filtered>", th2);
                th2.setStackTrace((StackTraceElement[]) arrayList.toArray(new StackTraceElement[0]));
                z = z2;
            }
            if (z) {
                return th2;
            }
            return null;
        }
    }

    private static synchronized boolean zza(Context context, String str, String str2, int i, Throwable th) {
        synchronized (CrashUtils.class) {
            Preconditions.checkNotNull(context);
            if (isPackageSide() && !Strings.isEmptyOrWhitespace(str)) {
                int hashCode = str.hashCode();
                int hashCode2 = th == null ? zzzj : th.hashCode();
                if (zzzi == hashCode && zzzj == hashCode2) {
                    return false;
                }
                zzzi = hashCode;
                zzzj = hashCode2;
                DropBoxManager dropBoxManager = zzzd != null ? zzzd : (DropBoxManager) context.getSystemService("dropbox");
                if (dropBoxManager != null && dropBoxManager.isTagEnabled("system_app_crash")) {
                    dropBoxManager.addText("system_app_crash", zza(context, str, str2, i));
                    return true;
                }
                return false;
            }
            return false;
        }
    }

    private static boolean zzdb() {
        if (zzze) {
            return zzzg;
        }
        return false;
    }
}
