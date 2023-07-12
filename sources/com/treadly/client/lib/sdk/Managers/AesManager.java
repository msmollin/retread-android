package com.treadly.client.lib.sdk.Managers;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

/* loaded from: classes2.dex */
public class AesManager {
    public static final AesManager shared = new AesManager();
    private static final byte[] preSharedIV = {111, 12, 110, -49, -84, -114, Message.MESSAGE_ID_LOG_USER_STATS_V2_DELETE, 109, -14, -18, -3, -32, -115, 111, Message.MESSAGE_ID_VERIFY_MAC_ADDRESS, 95};
    private static final byte[] preSharedKey = {-54, 3, -17, Message.MESSAGE_ID_TEST_START, -97, -43, 100, 31, 41, Message.MESSAGE_ID_MAINTENANCE_RESET_REQUEST, -125, -86, -86, -27, Message.MESSAGE_ID_SET_GAME_MODE, -100};

    private AesManager() {
    }

    public byte[] encrypt(byte[] bArr) {
        try {
            Cipher cipher = Cipher.getInstance("AES/CBC/NoPadding");
            cipher.init(1, new SecretKeySpec(preSharedKey, 0, preSharedKey.length, "AES"), new IvParameterSpec(preSharedIV));
            return cipher.doFinal(bArr);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public byte[] decrypt(byte[] bArr) {
        try {
            Cipher cipher = Cipher.getInstance("AES/CBC/NoPadding");
            cipher.init(2, new SecretKeySpec(preSharedKey, 0, preSharedKey.length, "AES"), new IvParameterSpec(preSharedIV));
            return cipher.doFinal(bArr);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
