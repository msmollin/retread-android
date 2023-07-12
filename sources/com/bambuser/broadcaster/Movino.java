package com.bambuser.broadcaster;

/* loaded from: classes.dex */
final class Movino {
    public static final int COMPRESSION_NONE = 1;
    public static final int COMPRESSION_RAW_DEFLATE = 3;
    public static final int COMPRESSION_ZLIB = 2;
    public static final int DATA_AAC_LC_AUDIO = 37;
    public static final int DATA_AAC_LC_HEADER = 36;
    public static final int DATA_AMR_AUDIO = 6;
    public static final int DATA_AMR_WB_AUDIO = 34;
    public static final int DATA_AUDIO_FORMAT = 33;
    public static final int DATA_BROADCAST_INFO_UPDATE = 31;
    public static final int DATA_CAPTURE_TIME = 49;
    public static final int DATA_CLIENT_CAPABILITIES = 32;
    public static final int DATA_GARBAGE = 0;
    public static final int DATA_GSM_AUDIO = 19;
    public static final int DATA_H263_FRAME = 20;
    public static final int DATA_H264_FRAME = 24;
    public static final int DATA_H264_HEADER = 39;
    public static final int DATA_HANDSHAKE = 14;
    public static final int DATA_HEVC_FRAME = 47;
    public static final int DATA_HEVC_HEADER = 48;
    public static final int DATA_ILBC_AUDIO = 29;
    public static final int DATA_IMA4_AUDIO = 28;
    public static final int DATA_JPEG_DATA = 4;
    public static final int DATA_JPEG_HEADER = 3;
    public static final int DATA_LOG_MESSAGE = 46;
    public static final int DATA_MPEG2_FRAME = 30;
    public static final int DATA_MPEG4_FRAME = 21;
    public static final int DATA_MPEG4_HEADER = 41;
    public static final int DATA_MPEG_FRAME = 10;
    public static final int DATA_MULAW_AUDIO = 5;
    public static final int DATA_NELLYMOSER_AUDIO = 35;
    public static final int DATA_OVERRIDE_STREAMINFO = 42;
    public static final int DATA_PCM_AUDIO = 2;
    public static final int DATA_PING = 44;
    public static final int DATA_POSITION = 18;
    public static final int DATA_REAL_TIME = 43;
    public static final int DATA_START_DUMP = 25;
    public static final int DATA_START_MTU_MEASUREMENT = 7;
    public static final int DATA_STOP_MTU_MEASUREMENT = 8;
    public static final int DATA_STREAM_CLOSED = 16;
    public static final int DATA_STREAM_INFO = 15;
    public static final int DATA_TALKBACK_STATUS = 38;
    public static final int DATA_THEORA_FRAME = 13;
    public static final int DATA_THEORA_HEADER = 12;
    public static final int DATA_UPLOAD_DATA = 27;
    public static final int DATA_UPLOAD_START = 26;
    public static final int DATA_VIDEO_CHUNK = 11;
    public static final int DATA_VIDEO_FILE = 9;
    public static final int DATA_VIDEO_TRANSFORM = 40;
    public static final int DATA_VP8_FRAME = 45;
    public static final int DATA_WMV3_FRAME = 23;
    public static final int DATA_WMV3_HEADER = 22;
    public static final int DATA_YUV_FRAME = 1;
    public static final int DATA_YUV_SP_FRAME = 17;
    public static final int HANDSHAKE_MD5_CHALLENGE = 2;
    public static final int HANDSHAKE_NO_PASSWORD = 1;
    public static final int HANDSHAKE_NO_USERNAME = 1;
    public static final int HANDSHAKE_REQUIRE_USERNAME = 2;
    public static final int MESSAGE_BAD_CLIENT = 2;
    public static final int MESSAGE_BAD_PASSWORD = 1;
    public static final int MESSAGE_CLOSE_CONNECTION = 6;
    public static final int MESSAGE_CUSTOM_AUTH_ERROR = 4;
    public static final int MESSAGE_RECONNECT_FAILED = 5;
    public static final int MESSAGE_SERVER_FULL = 3;
    public static final int MESSAGE_STRING = 0;
    public static final long ONES_32 = 4294967295L;
    public static final long ONES_64 = -1;
    public static final int POSITION_DOUBLE_VALUE = 2;
    public static final int POSITION_FLOAT_VALUE = 1;
    public static final int POSITION_NO_VALUE = 0;
    public static final int REPLY_AAC_AUDIO = 13;
    public static final int REPLY_AAC_HEADER = 12;
    public static final int REPLY_AUDIO_FORMAT = 11;
    public static final int REPLY_BROADCAST_INFO = 8;
    public static final int REPLY_HANDSHAKE = 2;
    public static final int REPLY_HANDSHAKE_ACCEPTED = 7;
    public static final int REPLY_MESSAGE = 4;
    public static final int REPLY_MTU_SIZE = 1;
    public static final int REPLY_PING = 3;
    public static final int REPLY_RECONNECT_ACCEPTED = 15;
    public static final int REPLY_RECONNECT_INFO = 14;
    public static final int REPLY_TALKBACK_REQUEST = 10;
    public static final int REPLY_UPLOAD_FINISHED = 6;
    public static final int REPLY_UPLOAD_START_POS = 5;
    public static final int REPLY_VIEWER_COUNT = 9;
    public static final int TALKBACK_ACCEPTED = 5;
    public static final int TALKBACK_DENIED = 1;
    public static final int TALKBACK_ENDED = 4;
    public static final int TALKBACK_FAILED = 2;
    public static final int TALKBACK_INLINE_AAC = 2;
    public static final int TALKBACK_INLINE_AAC_LD = 4;
    public static final int TALKBACK_NONE = 0;
    public static final int TALKBACK_NOT_PLAYING = 0;
    public static final int TALKBACK_PLAYING = 3;
    public static final int TRANSFORM_NONE = 0;
    public static final int TRANSFORM_ROT_180 = 2;
    public static final int TRANSFORM_ROT_270 = 3;
    public static final int TRANSFORM_ROT_90 = 1;
    public static final int TRANSFORM_VFLIP = 4;
    public static final int UPLOAD_TYPE_COMPLEMENT = 0;
    public static final int UPLOAD_TYPE_METADATA_MOVINO = 2;
    public static final int UPLOAD_TYPE_STANDALONE_MOVINO = 1;
    public static final int UPLOAD_TYPE_THIRD_PARTY_FILE = 3;

    /* JADX INFO: Access modifiers changed from: package-private */
    public static int getHeaderType(int i) {
        if (i != 4) {
            if (i != 13) {
                if (i != 21) {
                    if (i != 37) {
                        if (i != 47) {
                            switch (i) {
                                case 23:
                                    return 22;
                                case 24:
                                    return 39;
                                default:
                                    return 0;
                            }
                        }
                        return 48;
                    }
                    return 36;
                }
                return 41;
            }
            return 12;
        }
        return 3;
    }

    private Movino() {
    }
}
