package com.treadly.client.lib.sdk.Managers;

import com.treadly.client.lib.sdk.Model.VersionInfo;
import java.security.MessageDigest;

/* loaded from: classes2.dex */
public class AuthenticateHelper {
    private static int PAYLOAD_SIZE = 16;
    private static int[] AUTH_PW = {84, 114, 101, 97, 100, 108, 121, 79, 110, 108, 121};
    public static final VersionInfo PRE_AUTHENTICATION_VERSION = new VersionInfo(2, 0, 0);

    /* JADX INFO: Access modifiers changed from: package-private */
    public static byte[] generateAuthenticateResponse(byte[] bArr) {
        if (bArr == null || bArr.length != PAYLOAD_SIZE) {
            return null;
        }
        byte[] bArr2 = new byte[AUTH_PW.length];
        for (int i = 0; i < bArr2.length; i++) {
            bArr2[i] = (byte) AUTH_PW[i];
        }
        try {
            MessageDigest messageDigest = MessageDigest.getInstance("MD5");
            byte[] bArr3 = new byte[bArr.length + AUTH_PW.length];
            System.arraycopy(bArr, 0, bArr3, 0, bArr.length);
            System.arraycopy(bArr2, 0, bArr3, bArr.length, bArr2.length);
            messageDigest.update(bArr3);
            return messageDigest.digest();
        } catch (Exception e) {
            System.out.println("AuthenticateHelper : generateAuthenticateResponse: " + e);
            return null;
        }
    }
}
