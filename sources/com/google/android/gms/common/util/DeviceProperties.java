package com.google.android.gms.common.util;

import android.annotation.TargetApi;
import android.app.ActivityManager;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import com.google.android.gms.common.GooglePlayServicesUtilLight;
import com.google.android.gms.common.internal.Objects;
import com.treadly.Treadly.Data.Managers.TreadlyEventHelper;

/* loaded from: classes.dex */
public final class DeviceProperties {
    public static final String FEATURE_AUTO = "android.hardware.type.automotive";
    public static final String FEATURE_CHROME_OS = "org.chromium.arc";
    public static final String FEATURE_EMBEDDED = "android.hardware.type.embedded";
    public static final String FEATURE_IOT = "android.hardware.type.iot";
    public static final String FEATURE_LATCHSKY = "cn.google.services";
    public static final String FEATURE_PIXEL = "com.google.android.feature.PIXEL_EXPERIENCE";
    public static final String FEATURE_SIDEWINDER = "cn.google";
    public static final String FEATURE_TV_1 = "com.google.android.tv";
    public static final String FEATURE_TV_2 = "android.hardware.type.television";
    public static final String FEATURE_TV_3 = "android.software.leanback";
    private static Boolean zzzl;
    private static Boolean zzzm;
    private static Boolean zzzn;
    private static Boolean zzzo;
    private static Boolean zzzp;
    private static Boolean zzzq;
    private static Boolean zzzr;
    private static Boolean zzzs;
    private static Boolean zzzt;
    private static Boolean zzzu;
    private static Boolean zzzv;

    private DeviceProperties() {
    }

    public static boolean isAuto(Context context) {
        if (zzzt == null) {
            zzzt = Boolean.valueOf(PlatformVersion.isAtLeastO() && context.getPackageManager().hasSystemFeature(FEATURE_AUTO));
        }
        return zzzt.booleanValue();
    }

    public static boolean isChromeOsDevice(Context context) {
        if (zzzs == null) {
            zzzs = Boolean.valueOf(context.getPackageManager().hasSystemFeature(FEATURE_CHROME_OS));
        }
        return zzzs.booleanValue();
    }

    public static boolean isIoT(Context context) {
        if (zzzr == null) {
            zzzr = Boolean.valueOf(context.getPackageManager().hasSystemFeature(FEATURE_IOT) || context.getPackageManager().hasSystemFeature(FEATURE_EMBEDDED));
        }
        return zzzr.booleanValue();
    }

    public static boolean isLatchsky(Context context) {
        if (zzzp == null) {
            zzzp = Boolean.valueOf(PlatformVersion.isAtLeastM() && context.getPackageManager().hasSystemFeature(FEATURE_LATCHSKY));
        }
        return zzzp.booleanValue();
    }

    public static boolean isLowRamOrPreKitKat(Context context) {
        ActivityManager activityManager;
        if (Build.VERSION.SDK_INT < 19) {
            return true;
        }
        if (zzzq == null && (activityManager = (ActivityManager) context.getSystemService("activity")) != null) {
            zzzq = Boolean.valueOf(activityManager.isLowRamDevice());
        }
        return Objects.equal(zzzq, Boolean.TRUE);
    }

    public static boolean isPixelDevice(Context context) {
        if (zzzv == null) {
            zzzv = Boolean.valueOf(context.getPackageManager().hasSystemFeature(FEATURE_PIXEL));
        }
        return zzzv.booleanValue();
    }

    @TargetApi(21)
    public static boolean isSidewinder(Context context) {
        if (zzzo == null) {
            zzzo = Boolean.valueOf(PlatformVersion.isAtLeastLollipop() && context.getPackageManager().hasSystemFeature("cn.google"));
        }
        return zzzo.booleanValue();
    }

    /* JADX WARN: Code restructure failed: missing block: B:22:0x003c, code lost:
        if (com.google.android.gms.common.util.DeviceProperties.zzzm.booleanValue() != false) goto L23;
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public static boolean isTablet(android.content.res.Resources r4) {
        /*
            r0 = 0
            if (r4 != 0) goto L4
            return r0
        L4:
            java.lang.Boolean r1 = com.google.android.gms.common.util.DeviceProperties.zzzl
            if (r1 != 0) goto L45
            android.content.res.Configuration r1 = r4.getConfiguration()
            int r1 = r1.screenLayout
            r1 = r1 & 15
            r2 = 3
            r3 = 1
            if (r1 <= r2) goto L16
            r1 = r3
            goto L17
        L16:
            r1 = r0
        L17:
            if (r1 != 0) goto L3e
            java.lang.Boolean r1 = com.google.android.gms.common.util.DeviceProperties.zzzm
            if (r1 != 0) goto L36
            android.content.res.Configuration r4 = r4.getConfiguration()
            int r1 = r4.screenLayout
            r1 = r1 & 15
            if (r1 > r2) goto L2f
            int r4 = r4.smallestScreenWidthDp
            r1 = 600(0x258, float:8.41E-43)
            if (r4 < r1) goto L2f
            r4 = r3
            goto L30
        L2f:
            r4 = r0
        L30:
            java.lang.Boolean r4 = java.lang.Boolean.valueOf(r4)
            com.google.android.gms.common.util.DeviceProperties.zzzm = r4
        L36:
            java.lang.Boolean r4 = com.google.android.gms.common.util.DeviceProperties.zzzm
            boolean r4 = r4.booleanValue()
            if (r4 == 0) goto L3f
        L3e:
            r0 = r3
        L3f:
            java.lang.Boolean r4 = java.lang.Boolean.valueOf(r0)
            com.google.android.gms.common.util.DeviceProperties.zzzl = r4
        L45:
            java.lang.Boolean r4 = com.google.android.gms.common.util.DeviceProperties.zzzl
            boolean r4 = r4.booleanValue()
            return r4
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.android.gms.common.util.DeviceProperties.isTablet(android.content.res.Resources):boolean");
    }

    public static boolean isTv(Context context) {
        if (zzzu == null) {
            PackageManager packageManager = context.getPackageManager();
            zzzu = Boolean.valueOf(packageManager.hasSystemFeature(FEATURE_TV_1) || packageManager.hasSystemFeature(FEATURE_TV_2) || packageManager.hasSystemFeature(FEATURE_TV_3));
        }
        return zzzu.booleanValue();
    }

    public static boolean isUserBuild() {
        return GooglePlayServicesUtilLight.sIsTestMode ? GooglePlayServicesUtilLight.sTestIsUserBuild : TreadlyEventHelper.USER_EVENT_TOPIC.equals(Build.TYPE);
    }

    @TargetApi(20)
    public static boolean isWearable(Context context) {
        if (zzzn == null) {
            zzzn = Boolean.valueOf(PlatformVersion.isAtLeastKitKatWatch() && context.getPackageManager().hasSystemFeature("android.hardware.type.watch"));
        }
        return zzzn.booleanValue();
    }

    @TargetApi(24)
    public static boolean isWearableWithoutPlayStore(Context context) {
        return (!PlatformVersion.isAtLeastN() || isSidewinder(context)) && isWearable(context);
    }

    @VisibleForTesting
    public static void resetForTest() {
        zzzm = null;
        zzzl = null;
        zzzn = null;
        zzzo = null;
        zzzp = null;
        zzzq = null;
        zzzr = null;
        zzzs = null;
        zzzt = null;
        zzzu = null;
        zzzv = null;
    }

    @VisibleForTesting
    public static void setIsAutoForTest(boolean z) {
        zzzt = Boolean.valueOf(z);
    }

    @VisibleForTesting
    public static void setIsIoTForTest(boolean z) {
        zzzr = Boolean.valueOf(z);
    }

    @VisibleForTesting
    public static void setIsLatchskyForTest(boolean z) {
        zzzp = Boolean.valueOf(z);
    }

    @VisibleForTesting
    public static void setIsLowRamForTest(boolean z) {
        zzzq = Boolean.valueOf(z);
    }

    @VisibleForTesting
    public static void setIsPixelForTest(boolean z) {
        zzzv = Boolean.valueOf(z);
    }

    @VisibleForTesting
    public static void setIsSideWinderForTest(boolean z) {
        zzzo = Boolean.valueOf(z);
    }

    @VisibleForTesting
    public static void setIsTvForTest(boolean z) {
        zzzu = Boolean.valueOf(z);
    }

    @VisibleForTesting
    public static void setIsWearableForTest(boolean z) {
        zzzn = Boolean.valueOf(z);
    }
}
