package com.google.android.gms.common.util;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import com.google.android.gms.common.wrappers.Wrappers;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/* loaded from: classes.dex */
public class AndroidUtilsLight {
    public static final String DIGEST_ALGORITHM_SHA1 = "SHA1";
    public static final String DIGEST_ALGORITHM_SHA512 = "SHA-512";

    public static MessageDigest getMessageDigest(String str) {
        MessageDigest messageDigest;
        for (int i = 0; i < 2; i++) {
            try {
                messageDigest = MessageDigest.getInstance(str);
            } catch (NoSuchAlgorithmException unused) {
            }
            if (messageDigest != null) {
                return messageDigest;
            }
        }
        return null;
    }

    public static byte[] getPackageCertificateHashBytes(Context context, String str) throws PackageManager.NameNotFoundException {
        return getPackageCertificateHashBytes(context, str, DIGEST_ALGORITHM_SHA1);
    }

    public static byte[] getPackageCertificateHashBytes(Context context, String str, String str2) throws PackageManager.NameNotFoundException {
        MessageDigest messageDigest;
        PackageInfo packageInfo = Wrappers.packageManager(context).getPackageInfo(str, 64);
        if (packageInfo.signatures == null || packageInfo.signatures.length != 1 || (messageDigest = getMessageDigest(str2)) == null) {
            return null;
        }
        return messageDigest.digest(packageInfo.signatures[0].toByteArray());
    }
}
