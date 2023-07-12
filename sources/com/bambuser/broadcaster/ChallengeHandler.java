package com.bambuser.broadcaster;

import android.util.Log;
import java.nio.ByteBuffer;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/* loaded from: classes.dex */
class ChallengeHandler {
    private static final String LOGTAG = "ChallengeHandler";
    final byte[] mChallenge;
    final int mPasswordType;
    final int mUsernameType;

    /* JADX INFO: Access modifiers changed from: package-private */
    public ChallengeHandler(int i, int i2, ByteBuffer byteBuffer) {
        this.mUsernameType = i;
        this.mPasswordType = i2;
        this.mChallenge = new byte[byteBuffer.remaining()];
        byteBuffer.get(this.mChallenge);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public MovinoPacket createHandshake(String str, String str2) {
        byte[] uTF8FromString = MovinoUtils.getUTF8FromString(str);
        byte[] uTF8FromString2 = MovinoUtils.getUTF8FromString(str2);
        try {
            MessageDigest messageDigest = MessageDigest.getInstance("MD5");
            messageDigest.update(uTF8FromString2);
            byte[] digest = messageDigest.digest();
            messageDigest.reset();
            messageDigest.update(digest);
            messageDigest.update(this.mChallenge);
            byte[] digest2 = messageDigest.digest();
            MovinoPacket movinoPacket = new MovinoPacket(14, uTF8FromString.length + 4 + 2 + digest2.length);
            movinoPacket.writeUint8(this.mUsernameType);
            movinoPacket.writeUint8(this.mPasswordType);
            movinoPacket.writeBinString(uTF8FromString);
            movinoPacket.writeBinString(digest2);
            return movinoPacket;
        } catch (NoSuchAlgorithmException e) {
            Log.e(LOGTAG, "exception when initializing MD5 engine", e);
            return null;
        }
    }
}
