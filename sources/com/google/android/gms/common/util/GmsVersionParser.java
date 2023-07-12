package com.google.android.gms.common.util;

import com.treadly.Treadly.Data.Model.UserDailyGoalType;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/* loaded from: classes.dex */
public final class GmsVersionParser {
    public static final int UNKNOWN = -1;
    private static Pattern zzzy;

    private GmsVersionParser() {
    }

    public static int parseBuildMajorVersion(int i) {
        if (i == -1) {
            return -1;
        }
        return i / UserDailyGoalType.STEPS_MAX;
    }

    public static long parseBuildNumber(String str) {
        if (str == null) {
            return -1L;
        }
        Matcher matcher = zzdc().matcher(str);
        if (matcher.find()) {
            try {
                return Long.parseLong(matcher.group(2));
            } catch (NumberFormatException unused) {
            }
        }
        return -1L;
    }

    public static int parseBuildType(String str) {
        long parseVariantCode = parseVariantCode(str);
        if (parseVariantCode == -1) {
            return -1;
        }
        return (int) (parseVariantCode / 10000);
    }

    public static int parseBuildVersion(int i) {
        if (i == -1) {
            return -1;
        }
        return i / 1000;
    }

    public static int parseScreenDensity(String str) {
        long parseVariantCode = parseVariantCode(str);
        if (parseVariantCode == -1) {
            return -1;
        }
        return (int) (parseVariantCode % 100);
    }

    public static int parseTargetArchitecture(String str) {
        long parseVariantCode = parseVariantCode(str);
        if (parseVariantCode == -1) {
            return -1;
        }
        return (int) ((parseVariantCode / 100) % 100);
    }

    public static long parseVariantCode(String str) {
        if (str == null) {
            return -1L;
        }
        Matcher matcher = zzdc().matcher(str);
        if (matcher.find()) {
            try {
                return Long.parseLong(matcher.group(1));
            } catch (NumberFormatException unused) {
            }
        }
        return -1L;
    }

    private static Pattern zzdc() {
        if (zzzy == null) {
            zzzy = Pattern.compile("\\((?:eng-)?(\\d+)-(.+?)[-)$]");
        }
        return zzzy;
    }
}
