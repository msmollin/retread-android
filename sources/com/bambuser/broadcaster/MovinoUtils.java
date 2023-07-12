package com.bambuser.broadcaster;

import android.util.Log;
import com.bumptech.glide.load.Key;
import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import java.util.zip.DataFormatException;
import java.util.zip.Deflater;
import java.util.zip.DeflaterOutputStream;
import java.util.zip.Inflater;
import java.util.zip.InflaterInputStream;

/* loaded from: classes.dex */
final class MovinoUtils {
    private static final String LOGTAG = "MovinoUtils";

    private MovinoUtils() {
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static byte[] getUTF8FromString(String str) {
        try {
            return str.getBytes(Key.STRING_CHARSET_NAME);
        } catch (UnsupportedEncodingException e) {
            Log.e(LOGTAG, "exception when converting to utf8: " + e);
            return new byte[0];
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static String getStringFromLengthUTF8(ByteBuffer byteBuffer) {
        if (byteBuffer.remaining() < 2) {
            return "";
        }
        int i = byteBuffer.getShort();
        if (i > byteBuffer.remaining()) {
            i = byteBuffer.remaining();
        }
        String stringFromUTF8 = getStringFromUTF8((ByteBuffer) byteBuffer.slice().limit(i));
        byteBuffer.position(byteBuffer.position() + i);
        return stringFromUTF8;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static String getStringFromUTF8(ByteBuffer byteBuffer) {
        try {
            return new String(byteBuffer.array(), byteBuffer.arrayOffset() + byteBuffer.position(), byteBuffer.remaining(), Key.STRING_CHARSET_NAME);
        } catch (UnsupportedEncodingException e) {
            Log.e(LOGTAG, "exception when converting from utf8: " + e);
            return "";
        }
    }

    static String getStringFromCompressedUTF8(ByteBuffer byteBuffer) {
        if (byteBuffer.remaining() < 5) {
            return "";
        }
        int i = byteBuffer.get() & 255;
        int i2 = byteBuffer.getInt();
        if (i2 <= 0 || i2 > byteBuffer.remaining()) {
            return "";
        }
        switch (i) {
            case 1:
                return getStringFromUTF8((ByteBuffer) byteBuffer.slice().limit(i2));
            case 2:
            case 3:
                int i3 = byteBuffer.getInt();
                byte[] bArr = new byte[i3];
                Inflater inflater = new Inflater(i == 3);
                inflater.setInput(byteBuffer.array(), byteBuffer.arrayOffset() + byteBuffer.position(), i2 - 4);
                try {
                    inflater.inflate(bArr, 0, i3);
                    inflater.end();
                    return getStringFromUTF8(ByteBuffer.wrap(bArr));
                } catch (DataFormatException e) {
                    Log.w(LOGTAG, "exception when inflating compressed data: " + e);
                    inflater.end();
                    return "";
                }
            default:
                Log.w(LOGTAG, "unknown compression type!");
                return "";
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static InputStream getInputStreamFromCompressedUTF8(ByteBuffer byteBuffer) {
        if (byteBuffer.remaining() < 5) {
            return null;
        }
        int i = byteBuffer.get() & 255;
        int i2 = byteBuffer.getInt();
        if (i2 <= 0 || i2 > byteBuffer.remaining()) {
            return null;
        }
        switch (i) {
            case 1:
                return new ByteArrayInputStream(byteBuffer.array(), byteBuffer.arrayOffset() + byteBuffer.position(), i2);
            case 2:
            case 3:
                byteBuffer.getInt();
                return new BufferedInputStream(new InflaterInputStream(new ByteArrayInputStream(byteBuffer.array(), byteBuffer.arrayOffset() + byteBuffer.position(), i2 - 4), new Inflater(i == 3)), 4096);
            default:
                Log.w(LOGTAG, "unknown compression type!");
                return null;
        }
    }

    static MovinoPacket createBroadcastInfoPacket(int i, byte[] bArr) {
        if (bArr.length > 1024) {
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            DeflaterOutputStream deflaterOutputStream = new DeflaterOutputStream(byteArrayOutputStream, new Deflater(9, true));
            try {
                deflaterOutputStream.write(bArr);
                deflaterOutputStream.finish();
            } catch (IOException e) {
                Log.w(LOGTAG, "exception when deflating uncompressed data: " + e);
            }
            byte[] byteArray = byteArrayOutputStream.toByteArray();
            try {
                deflaterOutputStream.close();
            } catch (IOException e2) {
                Log.w(LOGTAG, "exception when closing deflateroutputstream: " + e2);
            }
            return new MovinoPacket(31, byteArray.length + 13).writeUint32(i).writeUint8(3).writeUint32(byteArray.length + 4).writeUint32(bArr.length).write(byteArray);
        }
        return new MovinoPacket(31, bArr.length + 9).writeUint32(i).writeUint8(1).writeUint32(bArr.length).write(bArr);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static MovinoPacket createUploadStartPacket(MovinoData movinoData) {
        byte[] uTF8FromString = getUTF8FromString(movinoData.getId());
        byte[] uTF8FromString2 = getUTF8FromString(movinoData.getTitle());
        return new MovinoPacket(26, uTF8FromString.length + 2 + 8 + 1 + 1 + 2 + uTF8FromString2.length).writeBinString(uTF8FromString).writeUint64(movinoData.getSize()).writeUint8(movinoData.getUploadType()).writeUint8(!movinoData.getUploadStarted()).writeBinString(uTF8FromString2);
    }

    static MovinoPacket createOverrideTitlePacket(String str) {
        byte[] uTF8FromString = getUTF8FromString(str);
        return new MovinoPacket(42, uTF8FromString.length + 3 + 1).writeUint8(1).writeBinString(uTF8FromString).writeUint8(0);
    }

    static MovinoPacket createOverrideVisibilityPacket(boolean z) {
        return new MovinoPacket(42, 3).writeUint8(0).writeUint8(1).writeUint8(z ? 1 : 0);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static MovinoPacket createCapabilitiesPacket(int i, int i2) {
        return new MovinoPacket(32, 11).writeUint32(i).writeUint8(1).writeUint8(1).writeUint32(i2).writeUint8(1);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static MovinoPacket createTalkbackStatus(int i, int i2, String str) {
        byte[] uTF8FromString = getUTF8FromString(str);
        return new MovinoPacket(38, uTF8FromString.length + 10).writeUint32(i).writeUint32(i2).writeBinString(uTF8FromString);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static MovinoPacket createVideoTransformPacket(long j, int i) {
        return new MovinoPacket(40, 8).writeUint32(j).writeUint32(i != 90 ? i != 180 ? i != 270 ? 0 : 3 : 2 : 1);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static MovinoPacket createCaptureTimePacket(long j, int i, long j2) {
        return new MovinoPacket(49, 16).writeUint32(j2).writeUint64(j).writeUint32(i);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static MovinoPacket createLogMessagePacket(String str) {
        byte[] uTF8FromString = getUTF8FromString(str);
        return new MovinoPacket(46, uTF8FromString.length + 2).writeBinString(uTF8FromString);
    }
}
