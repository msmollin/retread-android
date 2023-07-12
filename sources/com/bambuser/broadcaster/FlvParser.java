package com.bambuser.broadcaster;

import com.facebook.appevents.UserDataStore;
import com.facebook.appevents.internal.ViewHierarchyConstants;
import com.treadly.Treadly.Data.Managers.TreadlyEventHelper;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.text.SimpleDateFormat;
import java.util.Locale;
import java.util.Map;
import java.util.TimeZone;

/* loaded from: classes.dex */
final class FlvParser {
    private static final SimpleDateFormat sDateTimeFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss.SSS", Locale.US);
    private int mChannels;
    private PacketHandler mHandler;
    private int mHeight;
    private int mSampleRate;
    private int mWidth;
    private ByteBuffer mParseBuffer = ByteBuffer.allocate(1024);
    private boolean mFailed = false;
    private boolean mGotHeader = false;

    static {
        sDateTimeFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void handleData(byte[] bArr, int i, int i2) {
        if (this.mFailed) {
            return;
        }
        if (this.mParseBuffer.remaining() < i2) {
            this.mParseBuffer.flip();
            this.mParseBuffer = ByteBuffer.allocate((this.mParseBuffer.capacity() + i2) * 2).order(ByteOrder.BIG_ENDIAN).put(this.mParseBuffer);
        }
        this.mParseBuffer.put(bArr, i, i2);
        this.mParseBuffer.flip();
        do {
        } while (parsePacket());
        this.mParseBuffer.compact();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void setHandler(PacketHandler packetHandler) {
        this.mHandler = packetHandler;
    }

    private boolean parsePacket() {
        if (this.mFailed) {
            return false;
        }
        if (!this.mGotHeader) {
            if (this.mParseBuffer.remaining() < 13) {
                return false;
            }
            byte[] bArr = new byte[13];
            this.mParseBuffer.get(bArr);
            if (bArr[0] != 70 || bArr[1] != 76 || bArr[2] != 86) {
                this.mFailed = true;
                return false;
            }
            this.mGotHeader = true;
        }
        if (this.mParseBuffer.remaining() < 11) {
            return false;
        }
        this.mParseBuffer.mark();
        int i = this.mParseBuffer.get() & 255;
        int int24 = getInt24(this.mParseBuffer);
        int int242 = getInt24(this.mParseBuffer) | ((this.mParseBuffer.get() & 255) << 24);
        getInt24(this.mParseBuffer);
        if (this.mParseBuffer.remaining() < int24 + 4) {
            this.mParseBuffer.reset();
            return false;
        }
        handlePacket(i, int242, (ByteBuffer) this.mParseBuffer.slice().limit(int24));
        this.mParseBuffer.reset().position(this.mParseBuffer.position() + 11 + int24 + 4);
        return true;
    }

    private void handlePacket(int i, int i2, ByteBuffer byteBuffer) {
        if (i == 18) {
            Object parseAmfObject = AmfParser.parseAmfObject(byteBuffer);
            Object parseAmfObject2 = AmfParser.parseAmfObject(byteBuffer);
            if (parseAmfObject != null && parseAmfObject.equals("onMetaData") && (parseAmfObject2 instanceof Map)) {
                Map map = (Map) parseAmfObject2;
                this.mWidth = getIntValue(map, ViewHierarchyConstants.DIMENSION_WIDTH_KEY, 0);
                this.mHeight = getIntValue(map, ViewHierarchyConstants.DIMENSION_HEIGHT_KEY, 0);
                this.mSampleRate = getIntValue(map, "audiosamplerate", 0);
                this.mChannels = getBoolValue(map, "stereo", false) ? 2 : 1;
                Object obj = map.get(TreadlyEventHelper.keyDuration);
                if (obj instanceof Double) {
                    this.mHandler.onStreamDuration(((Double) obj).doubleValue());
                }
            } else if (parseAmfObject != null && parseAmfObject.equals("onFI") && (parseAmfObject2 instanceof Map)) {
                Map map2 = (Map) parseAmfObject2;
                boolean isNumber = isNumber(map2, "uncertainty");
                String string = getString(map2, "sd", null);
                String string2 = getString(map2, UserDataStore.STATE, null);
                int intValue = getIntValue(map2, "uncertainty", -1);
                long j = -1;
                if (string != null && string2 != null) {
                    try {
                        SimpleDateFormat simpleDateFormat = sDateTimeFormat;
                        j = simpleDateFormat.parse(string + " " + string2).getTime();
                    } catch (Exception unused) {
                    }
                }
                if (isNumber) {
                    if (intValue < 0 || j > 0) {
                        this.mHandler.onRealtimePacket(j, intValue, i2);
                    }
                }
            }
        } else if (i == 9 && byteBuffer.remaining() > 2) {
            int i3 = byteBuffer.get() & 255;
            int i4 = i3 & 15;
            int i5 = i3 >> 4;
            if (i4 != 7 || byteBuffer.remaining() <= 4) {
                return;
            }
            int i6 = byteBuffer.get() & 255;
            int int24 = i2 + getInt24(byteBuffer);
            if (i6 == 0) {
                this.mHandler.onVideoHeader(byteBuffer, i2, int24, this.mWidth, this.mHeight);
            } else {
                this.mHandler.onVideoFrame(byteBuffer, i2, int24, i5 == 1);
            }
        } else if (i != 8 || byteBuffer.remaining() <= 1 || ((byteBuffer.get() & 240) >> 4) != 10 || byteBuffer.remaining() <= 1) {
        } else {
            if ((byteBuffer.get() & 255) == 0) {
                this.mHandler.onAudioHeader(byteBuffer, i2, this.mSampleRate, this.mChannels);
            } else {
                this.mHandler.onAudioFrame(byteBuffer, i2);
            }
        }
    }

    private boolean isNumber(Map map, String str) {
        return map.get(str) instanceof Double;
    }

    private int getIntValue(Map map, String str, int i) {
        Object obj = map.get(str);
        return !(obj instanceof Double) ? i : ((Double) obj).intValue();
    }

    private boolean getBoolValue(Map map, String str, boolean z) {
        Object obj = map.get(str);
        return !(obj instanceof Boolean) ? z : ((Boolean) obj).booleanValue();
    }

    private int getInt24(ByteBuffer byteBuffer) {
        return ((byteBuffer.get() & 255) << 16) | (byteBuffer.getShort() & 65535);
    }

    private String getString(Map map, String str, String str2) {
        Object obj = map.get(str);
        return !(obj instanceof String) ? str2 : (String) obj;
    }
}
