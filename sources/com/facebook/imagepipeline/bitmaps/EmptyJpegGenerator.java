package com.facebook.imagepipeline.bitmaps;

import com.facebook.common.memory.PooledByteBuffer;
import com.facebook.common.memory.PooledByteBufferFactory;
import com.facebook.common.memory.PooledByteBufferOutputStream;
import com.facebook.common.references.CloseableReference;
import com.treadly.client.lib.sdk.Managers.Message;
import java.io.IOException;

/* loaded from: classes.dex */
public class EmptyJpegGenerator {
    private static final byte[] EMPTY_JPEG_PREFIX = {-1, -40, -1, -37, 0, Message.MESSAGE_ID_GET_BT_AUDIO_PASSWORD, 0, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -64, 0, Message.MESSAGE_ID_SUBSCRIBE_STATUS, 8};
    private static final byte[] EMPTY_JPEG_SUFFIX = {3, 1, 34, 0, 2, Message.MESSAGE_ID_SUBSCRIBE_STATUS, 0, 3, Message.MESSAGE_ID_SUBSCRIBE_STATUS, 0, -1, -60, 0, 31, 0, 0, 1, 5, 1, 1, 1, 1, 1, 1, 0, 0, 0, 0, 0, 0, 0, 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, -1, -60, 0, -75, Message.MESSAGE_ID_GET_DEVICE_INFO, 0, 2, 1, 3, 3, 2, 4, 3, 5, 5, 4, 4, 0, 0, 1, 125, 1, 2, 3, 0, 4, Message.MESSAGE_ID_SUBSCRIBE_STATUS, 5, 18, 33, Message.MESSAGE_ID_OTA_IN_PROGRESS, Message.MESSAGE_ID_LOG_USER_STATS_DELETE, 6, Message.MESSAGE_ID_AUTHENTICATE, Message.MESSAGE_ID_SET_GAME_MODE_DISPLAY, 97, 7, 34, 113, Message.MESSAGE_ID_AUTHENTICATE_VERIFY, Message.MESSAGE_ID_OTA_SET_MODE, -127, -111, -95, 8, 35, Message.MESSAGE_ID_PAUSE, -79, -63, Message.MESSAGE_ID_SET_ACCELERATE_ZONE_START, Message.MESSAGE_ID_USER_INTERACTION_HANDRAIL, -47, -16, 36, Message.MESSAGE_ID_BROADCAST_DEVICE_STATUS, 98, 114, -126, 9, 10, Message.MESSAGE_ID_SET_DECELERATE_ZONE_END, Message.MESSAGE_ID_SET_EMERG_HANDRAIL_ENABLED, Message.MESSAGE_ID_EMERGENCY_STOP_REQUEST, Message.MESSAGE_ID_MAINTENANCE_RESET_REQUEST, Message.MESSAGE_ID_MAINTENANCE_STEP_REQUEST, Message.MESSAGE_ID_TEST_NOTIFICATION, Message.MESSAGE_ID_SECURE_AUTHENTICATE, Message.MESSAGE_ID_SECURE_AUTHENTICATE_VERIFY, Message.MESSAGE_ID_TEST_START, 41, Message.MESSAGE_ID_SET_IR_MODE, 52, Message.MESSAGE_ID_SET_TOTAL_STATUS, Message.MESSAGE_ID_SET_REMOTE_STATUS, Message.MESSAGE_ID_RESET_STOP, Message.MESSAGE_ID_WIFI_URL_TEST, Message.MESSAGE_ID_DEVICE_DEBUG_LOG, Message.MESSAGE_ID_DEVICE_IR_DEBUG_LOG, Message.MESSAGE_ID_GET_BT_AUDIO_PASSWORD, Message.MESSAGE_ID_SET_BT_AUDIO_PASSWORD, Message.MESSAGE_ID_VERIFY_LED_CODE, Message.MESSAGE_ID_SET_PAIRING_MODE_TRIGGER, Message.MESSAGE_ID_SET_DEVICE_IR_DEBUG_LOG_MODE, Message.MESSAGE_ID_USER_INTERACTION_STEPS, Message.MESSAGE_ID_USER_INTERACTION_STATUS, Message.MESSAGE_ID_USER_INTERACTION_SET_ENABLE, 83, Message.MESSAGE_ID_STATUS_EX_2, Message.MESSAGE_ID_SET_DELETE_PAIRED_PHONES, Message.MESSAGE_ID_GET_UNCLAIMED_USER_STATS_LOG_INFO, Message.MESSAGE_ID_CLAIM_UNCLAIMED_USER_STATS_LOG_INFO, Message.MESSAGE_ID_SET_OTA_CONFIG, Message.MESSAGE_ID_LOG_USER_STATS_V2_READY, Message.MESSAGE_ID_LOG_USER_STATS_V2_DATA, 99, 100, 101, 102, 103, 104, 105, 106, 115, 116, 117, 118, 119, 120, 121, 122, -125, -124, -123, -122, -121, -120, -119, -118, -110, -109, -108, -107, -106, -105, -104, -103, -102, -94, -93, -92, -91, -90, -89, -88, -87, -86, -78, -77, -76, -75, -74, -73, -72, -71, -70, -62, -61, -60, -59, -58, -57, -56, -55, -54, -46, -45, -44, -43, -42, -41, -40, -39, -38, -31, -30, -29, -28, -27, -26, -25, -24, -23, -22, -15, -14, -13, -12, -11, -10, -9, -8, -7, -6, -1, -60, 0, 31, 1, 0, 3, 1, 1, 1, 1, 1, 1, 1, 1, 1, 0, 0, 0, 0, 0, 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, -1, -60, 0, -75, Message.MESSAGE_ID_SUBSCRIBE_STATUS, 0, 2, 1, 2, 4, 4, 3, 4, 7, 5, 4, 4, 0, 1, 2, 119, 0, 1, 2, 3, Message.MESSAGE_ID_SUBSCRIBE_STATUS, 4, 5, 33, Message.MESSAGE_ID_OTA_IN_PROGRESS, 6, 18, Message.MESSAGE_ID_LOG_USER_STATS_DELETE, Message.MESSAGE_ID_SET_GAME_MODE_DISPLAY, 7, 97, 113, Message.MESSAGE_ID_AUTHENTICATE, 34, Message.MESSAGE_ID_OTA_SET_MODE, -127, 8, Message.MESSAGE_ID_AUTHENTICATE_VERIFY, Message.MESSAGE_ID_PAUSE, -111, -95, -79, -63, 9, 35, Message.MESSAGE_ID_BROADCAST_DEVICE_STATUS, Message.MESSAGE_ID_USER_INTERACTION_HANDRAIL, -16, Message.MESSAGE_ID_SET_ACCELERATE_ZONE_START, 98, 114, -47, 10, Message.MESSAGE_ID_SET_DECELERATE_ZONE_END, 36, 52, -31, Message.MESSAGE_ID_TEST_NOTIFICATION, -15, Message.MESSAGE_ID_SET_EMERG_HANDRAIL_ENABLED, Message.MESSAGE_ID_EMERGENCY_STOP_REQUEST, Message.MESSAGE_ID_MAINTENANCE_RESET_REQUEST, Message.MESSAGE_ID_MAINTENANCE_STEP_REQUEST, Message.MESSAGE_ID_SECURE_AUTHENTICATE, Message.MESSAGE_ID_SECURE_AUTHENTICATE_VERIFY, Message.MESSAGE_ID_TEST_START, 41, Message.MESSAGE_ID_SET_IR_MODE, Message.MESSAGE_ID_SET_TOTAL_STATUS, Message.MESSAGE_ID_SET_REMOTE_STATUS, Message.MESSAGE_ID_RESET_STOP, Message.MESSAGE_ID_WIFI_URL_TEST, Message.MESSAGE_ID_DEVICE_DEBUG_LOG, Message.MESSAGE_ID_DEVICE_IR_DEBUG_LOG, Message.MESSAGE_ID_GET_BT_AUDIO_PASSWORD, Message.MESSAGE_ID_SET_BT_AUDIO_PASSWORD, Message.MESSAGE_ID_VERIFY_LED_CODE, Message.MESSAGE_ID_SET_PAIRING_MODE_TRIGGER, Message.MESSAGE_ID_SET_DEVICE_IR_DEBUG_LOG_MODE, Message.MESSAGE_ID_USER_INTERACTION_STEPS, Message.MESSAGE_ID_USER_INTERACTION_STATUS, Message.MESSAGE_ID_USER_INTERACTION_SET_ENABLE, 83, Message.MESSAGE_ID_STATUS_EX_2, Message.MESSAGE_ID_SET_DELETE_PAIRED_PHONES, Message.MESSAGE_ID_GET_UNCLAIMED_USER_STATS_LOG_INFO, Message.MESSAGE_ID_CLAIM_UNCLAIMED_USER_STATS_LOG_INFO, Message.MESSAGE_ID_SET_OTA_CONFIG, Message.MESSAGE_ID_LOG_USER_STATS_V2_READY, Message.MESSAGE_ID_LOG_USER_STATS_V2_DATA, 99, 100, 101, 102, 103, 104, 105, 106, 115, 116, 117, 118, 119, 120, 121, 122, -126, -125, -124, -123, -122, -121, -120, -119, -118, -110, -109, -108, -107, -106, -105, -104, -103, -102, -94, -93, -92, -91, -90, -89, -88, -87, -86, -78, -77, -76, -75, -74, -73, -72, -71, -70, -62, -61, -60, -59, -58, -57, -56, -55, -54, -46, -45, -44, -43, -42, -41, -40, -39, -38, -30, -29, -28, -27, -26, -25, -24, -23, -22, -14, -13, -12, -11, -10, -9, -8, -7, -6, -1, -38, 0, 12, 3, 1, 0, 2, Message.MESSAGE_ID_SUBSCRIBE_STATUS, 3, Message.MESSAGE_ID_SUBSCRIBE_STATUS, 0, Message.MESSAGE_ID_LOG_USER_STATS_READY, 0, -114, -118, Message.MESSAGE_ID_TEST_START, -96, 15, -1, -39};
    private final PooledByteBufferFactory mPooledByteBufferFactory;

    public EmptyJpegGenerator(PooledByteBufferFactory pooledByteBufferFactory) {
        this.mPooledByteBufferFactory = pooledByteBufferFactory;
    }

    public CloseableReference<PooledByteBuffer> generate(short s, short s2) {
        PooledByteBufferOutputStream pooledByteBufferOutputStream;
        PooledByteBufferOutputStream pooledByteBufferOutputStream2 = null;
        try {
            try {
                pooledByteBufferOutputStream = this.mPooledByteBufferFactory.newOutputStream(EMPTY_JPEG_PREFIX.length + EMPTY_JPEG_SUFFIX.length + 4);
                try {
                    pooledByteBufferOutputStream.write(EMPTY_JPEG_PREFIX);
                    pooledByteBufferOutputStream.write((byte) (s2 >> 8));
                    pooledByteBufferOutputStream.write((byte) (s2 & 255));
                    pooledByteBufferOutputStream.write((byte) (s >> 8));
                    pooledByteBufferOutputStream.write((byte) (s & 255));
                    pooledByteBufferOutputStream.write(EMPTY_JPEG_SUFFIX);
                    CloseableReference<PooledByteBuffer> of = CloseableReference.of(pooledByteBufferOutputStream.toByteBuffer());
                    if (pooledByteBufferOutputStream != null) {
                        pooledByteBufferOutputStream.close();
                    }
                    return of;
                } catch (IOException e) {
                    e = e;
                    pooledByteBufferOutputStream2 = pooledByteBufferOutputStream;
                    throw new RuntimeException(e);
                } catch (Throwable th) {
                    th = th;
                    if (pooledByteBufferOutputStream != null) {
                        pooledByteBufferOutputStream.close();
                    }
                    throw th;
                }
            } catch (Throwable th2) {
                th = th2;
                pooledByteBufferOutputStream = pooledByteBufferOutputStream2;
            }
        } catch (IOException e2) {
            e = e2;
        }
    }
}
