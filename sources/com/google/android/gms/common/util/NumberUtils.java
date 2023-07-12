package com.google.android.gms.common.util;

@VisibleForTesting
/* loaded from: classes.dex */
public class NumberUtils {
    private NumberUtils() {
    }

    public static int compare(int i, int i2) {
        if (i < i2) {
            return -1;
        }
        return i == i2 ? 0 : 1;
    }

    public static int compare(long j, long j2) {
        int i = (j > j2 ? 1 : (j == j2 ? 0 : -1));
        if (i < 0) {
            return -1;
        }
        return i == 0 ? 0 : 1;
    }

    public static boolean isNumeric(String str) {
        try {
            Long.parseLong(str);
            return true;
        } catch (NumberFormatException unused) {
            return false;
        }
    }

    public static long parseHexLong(String str) {
        if (str.length() <= 16) {
            if (str.length() == 16) {
                return (Long.parseLong(str.substring(0, 8), 16) << 32) | Long.parseLong(str.substring(8), 16);
            }
            return Long.parseLong(str, 16);
        }
        StringBuilder sb = new StringBuilder(String.valueOf(str).length() + 37);
        sb.append("Invalid input: ");
        sb.append(str);
        sb.append(" exceeds 16 characters");
        throw new NumberFormatException(sb.toString());
    }
}
