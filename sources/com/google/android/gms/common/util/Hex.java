package com.google.android.gms.common.util;

/* loaded from: classes.dex */
public class Hex {
    private static final char[] zzzz = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'};
    private static final char[] zzaaa = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};

    public static String bytesToColonDelimitedStringLowercase(byte[] bArr) {
        if (bArr.length == 0) {
            return new String();
        }
        char[] cArr = new char[(bArr.length * 3) - 1];
        int i = 0;
        int i2 = 0;
        while (i < bArr.length - 1) {
            int i3 = bArr[i] & 255;
            int i4 = i2 + 1;
            cArr[i2] = zzaaa[i3 >>> 4];
            int i5 = i4 + 1;
            cArr[i4] = zzaaa[i3 & 15];
            cArr[i5] = ':';
            i++;
            i2 = i5 + 1;
        }
        int i6 = bArr[bArr.length - 1] & 255;
        cArr[i2] = zzaaa[i6 >>> 4];
        cArr[i2 + 1] = zzaaa[i6 & 15];
        return new String(cArr);
    }

    public static String bytesToColonDelimitedStringUppercase(byte[] bArr) {
        if (bArr.length == 0) {
            return new String();
        }
        char[] cArr = new char[(bArr.length * 3) - 1];
        int i = 0;
        int i2 = 0;
        while (i < bArr.length - 1) {
            int i3 = bArr[i] & 255;
            int i4 = i2 + 1;
            cArr[i2] = zzzz[i3 >>> 4];
            int i5 = i4 + 1;
            cArr[i4] = zzzz[i3 & 15];
            cArr[i5] = ':';
            i++;
            i2 = i5 + 1;
        }
        int i6 = bArr[bArr.length - 1] & 255;
        cArr[i2] = zzzz[i6 >>> 4];
        cArr[i2 + 1] = zzzz[i6 & 15];
        return new String(cArr);
    }

    public static String bytesToStringLowercase(byte[] bArr) {
        char[] cArr = new char[bArr.length << 1];
        int i = 0;
        for (byte b : bArr) {
            int i2 = b & 255;
            int i3 = i + 1;
            cArr[i] = zzaaa[i2 >>> 4];
            i = i3 + 1;
            cArr[i3] = zzaaa[i2 & 15];
        }
        return new String(cArr);
    }

    public static String bytesToStringUppercase(byte[] bArr) {
        return bytesToStringUppercase(bArr, false);
    }

    public static String bytesToStringUppercase(byte[] bArr, boolean z) {
        int length = bArr.length;
        StringBuilder sb = new StringBuilder(length << 1);
        for (int i = 0; i < length && (!z || i != length - 1 || (bArr[i] & 255) != 0); i++) {
            sb.append(zzzz[(bArr[i] & 240) >>> 4]);
            sb.append(zzzz[bArr[i] & 15]);
        }
        return sb.toString();
    }

    public static byte[] colonDelimitedStringToBytes(String str) {
        return stringToBytes(str.replace(":", ""));
    }

    public static byte[] stringToBytes(String str) throws IllegalArgumentException {
        int length = str.length();
        if (length % 2 == 0) {
            byte[] bArr = new byte[length / 2];
            int i = 0;
            while (i < length) {
                int i2 = i + 2;
                bArr[i / 2] = (byte) Integer.parseInt(str.substring(i, i2), 16);
                i = i2;
            }
            return bArr;
        }
        throw new IllegalArgumentException("Hex string has odd number of characters");
    }
}
