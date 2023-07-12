package okio;

import com.treadly.client.lib.sdk.Managers.Message;
import java.io.UnsupportedEncodingException;

/* loaded from: classes2.dex */
final class Base64 {
    private static final byte[] MAP = {Message.MESSAGE_ID_LOG_USER_STATS_DELETE, Message.MESSAGE_ID_PAUSE, Message.MESSAGE_ID_GET_BT_AUDIO_PASSWORD, Message.MESSAGE_ID_SET_BT_AUDIO_PASSWORD, Message.MESSAGE_ID_VERIFY_LED_CODE, Message.MESSAGE_ID_SET_PAIRING_MODE_TRIGGER, Message.MESSAGE_ID_SET_DEVICE_IR_DEBUG_LOG_MODE, Message.MESSAGE_ID_USER_INTERACTION_STEPS, Message.MESSAGE_ID_USER_INTERACTION_STATUS, Message.MESSAGE_ID_USER_INTERACTION_SET_ENABLE, Message.MESSAGE_ID_OTA_DONE_CONFIRMATION, Message.MESSAGE_ID_UNCLAIMED_ACTIVE_LOG_USER_STATS, Message.MESSAGE_ID_CLAIM_ACTIVE_LOG_USER_STATS, Message.MESSAGE_ID_SET_GAME_MODE, Message.MESSAGE_ID_START_BLE_REMOTE_TEST, Message.MESSAGE_ID_BLE_REMOTE_TEST_RESULTS, Message.MESSAGE_ID_SET_GAME_MODE_DISPLAY, Message.MESSAGE_ID_USER_INTERACTION_HANDRAIL, 83, Message.MESSAGE_ID_STATUS_EX_2, Message.MESSAGE_ID_SET_DELETE_PAIRED_PHONES, Message.MESSAGE_ID_GET_UNCLAIMED_USER_STATS_LOG_INFO, Message.MESSAGE_ID_CLAIM_UNCLAIMED_USER_STATS_LOG_INFO, Message.MESSAGE_ID_SET_OTA_CONFIG, Message.MESSAGE_ID_LOG_USER_STATS_V2_READY, Message.MESSAGE_ID_LOG_USER_STATS_V2_DATA, 97, 98, 99, 100, 101, 102, 103, 104, 105, 106, 107, 108, 109, 110, 111, 112, 113, 114, 115, 116, 117, 118, 119, 120, 121, 122, Message.MESSAGE_ID_OTA_UPDATE, Message.MESSAGE_ID_OTA_IN_PROGRESS, Message.MESSAGE_ID_OTA_SET_MODE, Message.MESSAGE_ID_BROADCAST_DEVICE_STATUS, 52, Message.MESSAGE_ID_SET_TOTAL_STATUS, Message.MESSAGE_ID_SET_REMOTE_STATUS, Message.MESSAGE_ID_RESET_STOP, Message.MESSAGE_ID_WIFI_URL_TEST, Message.MESSAGE_ID_DEVICE_DEBUG_LOG, Message.MESSAGE_ID_MAC_ADDRESS, Message.MESSAGE_ID_WIFI_AP_INFO};
    private static final byte[] URL_MAP = {Message.MESSAGE_ID_LOG_USER_STATS_DELETE, Message.MESSAGE_ID_PAUSE, Message.MESSAGE_ID_GET_BT_AUDIO_PASSWORD, Message.MESSAGE_ID_SET_BT_AUDIO_PASSWORD, Message.MESSAGE_ID_VERIFY_LED_CODE, Message.MESSAGE_ID_SET_PAIRING_MODE_TRIGGER, Message.MESSAGE_ID_SET_DEVICE_IR_DEBUG_LOG_MODE, Message.MESSAGE_ID_USER_INTERACTION_STEPS, Message.MESSAGE_ID_USER_INTERACTION_STATUS, Message.MESSAGE_ID_USER_INTERACTION_SET_ENABLE, Message.MESSAGE_ID_OTA_DONE_CONFIRMATION, Message.MESSAGE_ID_UNCLAIMED_ACTIVE_LOG_USER_STATS, Message.MESSAGE_ID_CLAIM_ACTIVE_LOG_USER_STATS, Message.MESSAGE_ID_SET_GAME_MODE, Message.MESSAGE_ID_START_BLE_REMOTE_TEST, Message.MESSAGE_ID_BLE_REMOTE_TEST_RESULTS, Message.MESSAGE_ID_SET_GAME_MODE_DISPLAY, Message.MESSAGE_ID_USER_INTERACTION_HANDRAIL, 83, Message.MESSAGE_ID_STATUS_EX_2, Message.MESSAGE_ID_SET_DELETE_PAIRED_PHONES, Message.MESSAGE_ID_GET_UNCLAIMED_USER_STATS_LOG_INFO, Message.MESSAGE_ID_CLAIM_UNCLAIMED_USER_STATS_LOG_INFO, Message.MESSAGE_ID_SET_OTA_CONFIG, Message.MESSAGE_ID_LOG_USER_STATS_V2_READY, Message.MESSAGE_ID_LOG_USER_STATS_V2_DATA, 97, 98, 99, 100, 101, 102, 103, 104, 105, 106, 107, 108, 109, 110, 111, 112, 113, 114, 115, 116, 117, 118, 119, 120, 121, 122, Message.MESSAGE_ID_OTA_UPDATE, Message.MESSAGE_ID_OTA_IN_PROGRESS, Message.MESSAGE_ID_OTA_SET_MODE, Message.MESSAGE_ID_BROADCAST_DEVICE_STATUS, 52, Message.MESSAGE_ID_SET_TOTAL_STATUS, Message.MESSAGE_ID_SET_REMOTE_STATUS, Message.MESSAGE_ID_RESET_STOP, Message.MESSAGE_ID_WIFI_URL_TEST, Message.MESSAGE_ID_DEVICE_DEBUG_LOG, 45, 95};

    private Base64() {
    }

    public static byte[] decode(String str) {
        int i;
        char charAt;
        int length = str.length();
        while (length > 0 && ((charAt = str.charAt(length - 1)) == '=' || charAt == '\n' || charAt == '\r' || charAt == ' ' || charAt == '\t')) {
            length--;
        }
        byte[] bArr = new byte[(int) ((length * 6) / 8)];
        int i2 = 0;
        int i3 = 0;
        int i4 = 0;
        for (int i5 = 0; i5 < length; i5++) {
            char charAt2 = str.charAt(i5);
            if (charAt2 >= 'A' && charAt2 <= 'Z') {
                i = charAt2 - 'A';
            } else if (charAt2 >= 'a' && charAt2 <= 'z') {
                i = charAt2 - 'G';
            } else if (charAt2 >= '0' && charAt2 <= '9') {
                i = charAt2 + 4;
            } else if (charAt2 == '+' || charAt2 == '-') {
                i = 62;
            } else if (charAt2 == '/' || charAt2 == '_') {
                i = 63;
            } else {
                if (charAt2 != '\n' && charAt2 != '\r' && charAt2 != ' ' && charAt2 != '\t') {
                    return null;
                }
            }
            i3 = (i3 << 6) | ((byte) i);
            i2++;
            if (i2 % 4 == 0) {
                int i6 = i4 + 1;
                bArr[i4] = (byte) (i3 >> 16);
                int i7 = i6 + 1;
                bArr[i6] = (byte) (i3 >> 8);
                bArr[i7] = (byte) i3;
                i4 = i7 + 1;
            }
        }
        int i8 = i2 % 4;
        if (i8 == 1) {
            return null;
        }
        if (i8 == 2) {
            bArr[i4] = (byte) ((i3 << 12) >> 16);
            i4++;
        } else if (i8 == 3) {
            int i9 = i3 << 6;
            int i10 = i4 + 1;
            bArr[i4] = (byte) (i9 >> 16);
            i4 = i10 + 1;
            bArr[i10] = (byte) (i9 >> 8);
        }
        if (i4 == bArr.length) {
            return bArr;
        }
        byte[] bArr2 = new byte[i4];
        System.arraycopy(bArr, 0, bArr2, 0, i4);
        return bArr2;
    }

    public static String encode(byte[] bArr) {
        return encode(bArr, MAP);
    }

    public static String encodeUrl(byte[] bArr) {
        return encode(bArr, URL_MAP);
    }

    private static String encode(byte[] bArr, byte[] bArr2) {
        byte[] bArr3 = new byte[((bArr.length + 2) / 3) * 4];
        int length = bArr.length - (bArr.length % 3);
        int i = 0;
        for (int i2 = 0; i2 < length; i2 += 3) {
            int i3 = i + 1;
            bArr3[i] = bArr2[(bArr[i2] & 255) >> 2];
            int i4 = i3 + 1;
            int i5 = i2 + 1;
            bArr3[i3] = bArr2[((bArr[i2] & 3) << 4) | ((bArr[i5] & 255) >> 4)];
            int i6 = i4 + 1;
            int i7 = i2 + 2;
            bArr3[i4] = bArr2[((bArr[i5] & 15) << 2) | ((bArr[i7] & 255) >> 6)];
            i = i6 + 1;
            bArr3[i6] = bArr2[bArr[i7] & Message.MESSAGE_ID_LOG_USER_STATS_READY];
        }
        switch (bArr.length % 3) {
            case 1:
                int i8 = i + 1;
                bArr3[i] = bArr2[(bArr[length] & 255) >> 2];
                int i9 = i8 + 1;
                bArr3[i8] = bArr2[(bArr[length] & 3) << 4];
                bArr3[i9] = Message.MESSAGE_ID_GET_PROFILE_ID;
                bArr3[i9 + 1] = Message.MESSAGE_ID_GET_PROFILE_ID;
                break;
            case 2:
                int i10 = i + 1;
                bArr3[i] = bArr2[(bArr[length] & 255) >> 2];
                int i11 = i10 + 1;
                int i12 = length + 1;
                bArr3[i10] = bArr2[((bArr[length] & 3) << 4) | ((bArr[i12] & 255) >> 4)];
                bArr3[i11] = bArr2[(bArr[i12] & 15) << 2];
                bArr3[i11 + 1] = Message.MESSAGE_ID_GET_PROFILE_ID;
                break;
        }
        try {
            return new String(bArr3, "US-ASCII");
        } catch (UnsupportedEncodingException e) {
            throw new AssertionError(e);
        }
    }
}
