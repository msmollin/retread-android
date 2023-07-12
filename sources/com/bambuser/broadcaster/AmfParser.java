package com.bambuser.broadcaster;

import com.bumptech.glide.load.Key;
import java.io.UnsupportedEncodingException;
import java.nio.BufferUnderflowException;
import java.nio.ByteBuffer;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/* loaded from: classes.dex */
final class AmfParser {
    private static final int AMF_ARRAY = 10;
    private static final int AMF_BOOL = 1;
    private static final int AMF_DATE = 11;
    private static final int AMF_LONG_STRING = 12;
    private static final int AMF_MIXEDARRAY = 8;
    private static final int AMF_NUMBER = 0;
    private static final int AMF_OBJECT = 3;
    private static final int AMF_OBJECT_END = 9;
    private static final int AMF_STRING = 2;

    AmfParser() {
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static Object parseAmfObject(ByteBuffer byteBuffer) {
        try {
            int i = byteBuffer.get() & 255;
            if (i != 8) {
                switch (i) {
                    case 0:
                        return Double.valueOf(parseNumber(byteBuffer));
                    case 1:
                        return Boolean.valueOf(parseBool(byteBuffer));
                    case 2:
                        return parseString(byteBuffer);
                    case 3:
                        return parseMixedArray(byteBuffer, false);
                    default:
                        switch (i) {
                            case 10:
                                return parseArray(byteBuffer);
                            case 11:
                                return parseDate(byteBuffer);
                            case 12:
                                return parseStringInternal(byteBuffer, 4);
                            default:
                                return null;
                        }
                }
            }
            return parseMixedArray(byteBuffer, true);
        } catch (BufferUnderflowException unused) {
            byteBuffer.position(byteBuffer.limit());
            return null;
        }
    }

    private static String parseString(ByteBuffer byteBuffer) {
        return parseStringInternal(byteBuffer, 2);
    }

    private static String parseStringInternal(ByteBuffer byteBuffer, int i) {
        int i2;
        if (i == 2) {
            i2 = byteBuffer.getShort();
        } else {
            i2 = byteBuffer.getInt();
        }
        if (i2 > byteBuffer.remaining()) {
            throw new BufferUnderflowException();
        }
        try {
            String str = new String(byteBuffer.array(), byteBuffer.arrayOffset() + byteBuffer.position(), i2, Key.STRING_CHARSET_NAME);
            byteBuffer.position(byteBuffer.position() + i2);
            return str;
        } catch (UnsupportedEncodingException unused) {
            return null;
        }
    }

    private static double parseNumber(ByteBuffer byteBuffer) {
        return byteBuffer.getDouble();
    }

    private static boolean parseBool(ByteBuffer byteBuffer) {
        return (byteBuffer.get() & 255) != 0;
    }

    private static Map<String, Object> parseMixedArray(ByteBuffer byteBuffer, boolean z) {
        if (z) {
            byteBuffer.getInt();
        }
        HashMap hashMap = new HashMap();
        while (byteBuffer.remaining() > 0) {
            String parseString = parseString(byteBuffer);
            if (parseString == null) {
                return null;
            }
            if (parseString.equals("")) {
                break;
            }
            hashMap.put(parseString, parseAmfObject(byteBuffer));
        }
        if (byteBuffer.get() != 9) {
            return null;
        }
        return hashMap;
    }

    private static Object[] parseArray(ByteBuffer byteBuffer) {
        int i = byteBuffer.getInt();
        Object[] objArr = new Object[i];
        for (int i2 = 0; i2 < i; i2++) {
            objArr[i2] = parseAmfObject(byteBuffer);
            if (objArr[i2] == null) {
                return null;
            }
        }
        return objArr;
    }

    private static Date parseDate(ByteBuffer byteBuffer) {
        double parseNumber = parseNumber(byteBuffer);
        byteBuffer.getShort();
        return new Date((long) parseNumber);
    }
}
