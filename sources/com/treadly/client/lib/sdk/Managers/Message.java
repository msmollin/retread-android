package com.treadly.client.lib.sdk.Managers;

import com.google.android.gms.common.internal.GmsClientSupervisor;
import com.treadly.client.lib.sdk.Model.BleRemoteTestError;
import com.treadly.client.lib.sdk.Model.BleRemoteTestResults;
import com.treadly.client.lib.sdk.Model.ComponentInfo;
import com.treadly.client.lib.sdk.Model.ComponentType;
import com.treadly.client.lib.sdk.Model.ComponentVersionInfo;
import com.treadly.client.lib.sdk.Model.DeviceDebugLogEvent;
import com.treadly.client.lib.sdk.Model.DeviceDebugLogInfo;
import com.treadly.client.lib.sdk.Model.DeviceIrDebugAction;
import com.treadly.client.lib.sdk.Model.DeviceIrDebugEventInfo;
import com.treadly.client.lib.sdk.Model.DeviceMode;
import com.treadly.client.lib.sdk.Model.DeviceSpeedZoneConfiguration;
import com.treadly.client.lib.sdk.Model.DeviceStatus;
import com.treadly.client.lib.sdk.Model.DeviceUserStatsLogInfo;
import com.treadly.client.lib.sdk.Model.DeviceUserStatsLogItem;
import com.treadly.client.lib.sdk.Model.DeviceUserStatsLogSegmentInfo;
import com.treadly.client.lib.sdk.Model.DeviceUserStatsUnclaimedLogInfo;
import com.treadly.client.lib.sdk.Model.DistanceUnits;
import com.treadly.client.lib.sdk.Model.GameModeType;
import com.treadly.client.lib.sdk.Model.HandrailStatus;
import com.treadly.client.lib.sdk.Model.IrMode;
import com.treadly.client.lib.sdk.Model.MaintenanceInfo;
import com.treadly.client.lib.sdk.Model.OtaUpdateInfo;
import com.treadly.client.lib.sdk.Model.PairingModeTriggerType;
import com.treadly.client.lib.sdk.Model.RemoteStatus;
import com.treadly.client.lib.sdk.Model.ResponseMessage;
import com.treadly.client.lib.sdk.Model.SpeedInfo;
import com.treadly.client.lib.sdk.Model.TemperatureInfo;
import com.treadly.client.lib.sdk.Model.TemperatureStatusCode;
import com.treadly.client.lib.sdk.Model.TestNotification;
import com.treadly.client.lib.sdk.Model.TotalStatus;
import com.treadly.client.lib.sdk.Model.UserInteractionHandrailEvent;
import com.treadly.client.lib.sdk.Model.UserInteractionHandrailStepEventInfo;
import com.treadly.client.lib.sdk.Model.UserInteractionStatus;
import com.treadly.client.lib.sdk.Model.UserInteractionStepInfo;
import com.treadly.client.lib.sdk.Model.UserInteractionSteps;
import com.treadly.client.lib.sdk.Model.VersionInfo;
import com.treadly.client.lib.sdk.Model.WifiApInfo;
import com.treadly.client.lib.sdk.Model.WifiUrlTestInfo;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;

/* loaded from: classes2.dex */
public class Message {
    private static int CMD_INDEX = 0;
    private static final byte DEVICE_DEBUG_GROUP_SIZE = 6;
    private static final byte DEVICE_IR_DEBUG_GROUP_SIZE = 8;
    private static int ID_INDEX = 2;
    public static final byte MESSAGE_ID_AUTHENTICATE = 19;
    public static final byte MESSAGE_ID_AUTHENTICATE_VERIFY = 20;
    public static final byte MESSAGE_ID_BLE_ENABLE_REQUEST = 30;
    public static final byte MESSAGE_ID_BLE_REMOTE_TEST_RESULTS = 80;
    public static final byte MESSAGE_ID_BROADCAST_DEVICE_STATUS = 51;
    public static final byte MESSAGE_ID_BROADCAST_VALIDATE_DEVICE = 32;
    public static final byte MESSAGE_ID_CLAIM_ACTIVE_LOG_USER_STATS = 77;
    public static final byte MESSAGE_ID_CLAIM_UNCLAIMED_USER_STATS_LOG_INFO = 87;
    public static final byte MESSAGE_ID_CLEAR_OTA_CONFIG = 94;
    public static final byte MESSAGE_ID_DEVICE_DEBUG_LOG = 57;
    public static final byte MESSAGE_ID_DEVICE_IR_DEBUG_LOG = 58;
    public static final byte MESSAGE_ID_EMERGENCY_STOP_REQUEST = 24;
    public static final byte MESSAGE_ID_FACTORY_RESET = 11;
    public static final byte MESSAGE_ID_GET_AVAILABLE_WIFI_AP_INFO = 46;
    public static final byte MESSAGE_ID_GET_BT_AUDIO_PASSWORD = 67;
    public static final byte MESSAGE_ID_GET_DEVICE_INFO = 16;
    public static final byte MESSAGE_ID_GET_OTA_CONFIG = 93;
    public static final byte MESSAGE_ID_GET_PROFILE_ID = 61;
    public static final byte MESSAGE_ID_GET_STATUS_IOT = 92;
    public static final byte MESSAGE_ID_GET_TIMESTAMP = 62;
    public static final byte MESSAGE_ID_GET_UNCLAIMED_USER_STATS_LOG_INFO = 86;
    public static final byte MESSAGE_ID_LOG_USER_STATS_DATA = 64;
    public static final byte MESSAGE_ID_LOG_USER_STATS_DELETE = 65;
    public static final byte MESSAGE_ID_LOG_USER_STATS_ENABLE = 60;
    public static final byte MESSAGE_ID_LOG_USER_STATS_READY = 63;
    public static final byte MESSAGE_ID_LOG_USER_STATS_V2_DATA = 90;
    public static final byte MESSAGE_ID_LOG_USER_STATS_V2_READY = 89;
    public static final byte MESSAGE_ID_MAC_ADDRESS = 43;
    public static final byte MESSAGE_ID_MAINTENANCE_RESET_REQUEST = 25;
    public static final byte MESSAGE_ID_MAINTENANCE_STEP_REQUEST = 26;
    public static final byte MESSAGE_ID_OTA_DONE_CONFIRMATION = 75;
    public static final byte MESSAGE_ID_OTA_IN_PROGRESS = 49;
    public static final byte MESSAGE_ID_OTA_SET_MODE = 50;
    public static final byte MESSAGE_ID_OTA_UPDATE = 48;
    public static final byte MESSAGE_ID_PAUSE = 66;
    public static final byte MESSAGE_ID_POWER = 1;
    public static final byte MESSAGE_ID_RESET_STOP = 55;
    public static final byte MESSAGE_ID_SECURE_AUTHENTICATE = 38;
    public static final byte MESSAGE_ID_SECURE_AUTHENTICATE_VERIFY = 39;
    public static final byte MESSAGE_ID_SET_ACCELERATE_ZONE_START = 21;
    public static final byte MESSAGE_ID_SET_BT_AUDIO_PASSWORD = 68;
    public static final byte MESSAGE_ID_SET_DECELERATE_ZONE_END = 22;
    public static final byte MESSAGE_ID_SET_DELETE_PAIRED_PHONES = 85;
    public static final byte MESSAGE_ID_SET_DEVICE_IR_DEBUG_LOG_MODE = 71;
    public static final byte MESSAGE_ID_SET_EMERG_HANDRAIL_ENABLED = 23;
    public static final byte MESSAGE_ID_SET_GAME_MODE = 78;
    public static final byte MESSAGE_ID_SET_GAME_MODE_DISPLAY = 81;
    public static final byte MESSAGE_ID_SET_IR_MODE = 42;
    public static final byte MESSAGE_ID_SET_OTA_CONFIG = 88;
    public static final byte MESSAGE_ID_SET_PAIRING_MODE_TRIGGER = 70;
    private static final byte MESSAGE_ID_SET_PAUSE_AFTER_IDLE = 83;
    public static final byte MESSAGE_ID_SET_REMOTE_STATUS = 54;
    public static final byte MESSAGE_ID_SET_SPEED = 10;
    public static final byte MESSAGE_ID_SET_TOTAL_STATUS = 53;
    public static final byte MESSAGE_ID_SET_UNIT_KILOMETERS = 8;
    public static final byte MESSAGE_ID_SET_UNIT_MILES = 9;
    public static final byte MESSAGE_ID_SPEED_DOWN = 3;
    public static final byte MESSAGE_ID_SPEED_UP = 2;
    public static final byte MESSAGE_ID_START_BLE_REMOTE_TEST = 79;
    public static final byte MESSAGE_ID_STATUS = 4;
    public static final byte MESSAGE_ID_STATUS_DIAGNOSTIC = 13;
    public static final byte MESSAGE_ID_STATUS_EX = 12;
    public static final byte MESSAGE_ID_STATUS_EX_2 = 84;
    public static final byte MESSAGE_ID_SUBSCRIBE_STATUS = 17;
    public static final byte MESSAGE_ID_TEST_NOTIFICATION = 37;
    public static final byte MESSAGE_ID_TEST_START = 40;
    public static final byte MESSAGE_ID_UNCLAIMED_ACTIVE_LOG_USER_STATS = 76;
    public static final byte MESSAGE_ID_USER_INTERACTION_HANDRAIL = 82;
    public static final byte MESSAGE_ID_USER_INTERACTION_SET_ENABLE = 74;
    public static final byte MESSAGE_ID_USER_INTERACTION_STATUS = 73;
    public static final byte MESSAGE_ID_USER_INTERACTION_STEPS = 72;
    public static final byte MESSAGE_ID_VERIFY_LED_CODE = 69;
    public static final byte MESSAGE_ID_VERIFY_MAC_ADDRESS = 44;
    public static final byte MESSAGE_ID_WIFI_AP_INFO = 47;
    public static final byte MESSAGE_ID_WIFI_URL_TEST = 56;
    private static int MESSAGE_SIZE = 20;
    public static byte MESSAGE_STATUS_REQUEST = 0;
    private static final byte OTA_UPDATE_INFO_GROUP_SIZE = 16;
    private static int PAYLOAD_END = 19;
    public static int PAYLOAD_SIZE = 16;
    private static int PAYLOAD_START = 3;
    private static int STATUS_INDEX = 1;
    private static int STATUS_RESPONSE_PAYLOAD_SIZE = 13;
    private static final byte WIFI_AP_INFO_GROUP_SIZE = 3;
    private static final byte WIFI_URL_TEST_INFO_GROUP_SIZE = 14;
    public static final byte MESSAGE_ID_LOG_USER_STATS_V2_DELETE = 91;
    private static byte[] MESSAGE_AUTHENTICATE_SECRET_KEY = {-90, MESSAGE_ID_LOG_USER_STATS_V2_DELETE, -14, -125};
    private static int SECURED_AUTHENTICATE_VERIFICATION_PAYLOAD_SIZE = 16;
    private static int BROADCAST_DEVICE_STATUS_PAYLOAD_SIZE = 16;
    private static float DISTANCE_TO_FLOAT = 100.0f;
    private static float SPEED_TO_FLOAT = 10.0f;
    private static float TEMP_TO_FLOAT = 10.0f;
    private static int MAINTENANCE_STEPS_TO_BYTE = 1000;
    private static int MAINTENANCE_STEPS_TO_BYTE_V2 = 10000;
    private static int MAINTENANCE_STEPS_TO_BYTE_V3 = 10000;
    private static int STATUS_SUCCESS = 0;
    private static int STATUS_SECURE_AUTHENTICATION_REQUIRED = GmsClientSupervisor.DEFAULT_BIND_FLAGS;
    private static float MILES_TO_KM_CONVERT = 1.6093f;
    private static float KM_TO_MILES_CONVERT = 0.6214f;

    private static float toFloat(byte b) {
        return b & 255;
    }

    private static float toFloat(int i) {
        return i;
    }

    private static int toInt(byte b) {
        return b & 255;
    }

    static boolean isValidMessage(byte[] bArr) {
        return bArr != null && bArr.length <= MESSAGE_SIZE && bArr.length >= 3;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static ResponseMessage parseMessage(byte[] bArr) {
        if (isValidMessage(bArr)) {
            return new ResponseMessage(bArr[CMD_INDEX], bArr[STATUS_INDEX], bArr[ID_INDEX], Arrays.copyOfRange(bArr, PAYLOAD_START, PAYLOAD_END));
        }
        return null;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static DeviceStatus parseStatusResponse(byte[] bArr, int i, VersionInfo versionInfo) {
        if (bArr == null || bArr.length < STATUS_RESPONSE_PAYLOAD_SIZE) {
            return null;
        }
        int i2 = toInt(bArr[0]) + (toInt(bArr[1]) << 8) + (toInt(bArr[2]) << 16);
        float f = (toFloat(bArr[3]) + toFloat(toInt(bArr[4]) << 8)) / DISTANCE_TO_FLOAT;
        int i3 = ((toInt(bArr[5]) + (toInt(bArr[6]) << 8)) * 60) + toInt(bArr[7]);
        DistanceUnits fromValue = DistanceUnits.fromValue(toInt(bArr[12]) & 1);
        if (fromValue == null) {
            fromValue = DistanceUnits.MI;
        }
        boolean z = ((toInt(bArr[12]) >> 1) & 1) == 0;
        DeviceMode fromValue2 = DeviceMode.fromValue((toInt(bArr[12]) >> 2) & 3);
        if (fromValue2 == null) {
            fromValue2 = DeviceMode.UNKNOWN;
        }
        DeviceMode deviceMode = fromValue2;
        boolean z2 = ((toInt(bArr[12]) >> 4) & 1) == 1;
        boolean z3 = ((toInt(bArr[12]) >> 5) & 1) == 1;
        HandrailStatus fromValue3 = HandrailStatus.fromValue((toInt(bArr[12]) >> 6) & 3);
        SpeedInfo speedInfo = new SpeedInfo(toFloat(bArr[8]) / SPEED_TO_FLOAT, toFloat(bArr[10]) / SPEED_TO_FLOAT, toFloat(bArr[9]) / SPEED_TO_FLOAT, fromValue);
        int i4 = toInt(bArr[11]);
        VersionInfo versionInfo2 = new VersionInfo(3, 25, 0);
        boolean z4 = (versionInfo.isGreaterThan(versionInfo2) || versionInfo.equals(versionInfo2)) ? false : true;
        VersionInfo versionInfo3 = new VersionInfo(3, 67, 0);
        return new DeviceStatus(i4, i2, f, fromValue, i3, speedInfo, i, deviceMode, z, z2, z3, fromValue3, z4, (versionInfo.isGreaterThan(versionInfo3) || versionInfo.equals(versionInfo3)) ? false : true, bArr);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static DeviceStatus parseDeviceStatusExtResponse(byte[] bArr, DeviceStatus deviceStatus, ComponentInfo componentInfo, VersionInfo versionInfo) {
        if (bArr == null || bArr.length < STATUS_RESPONSE_PAYLOAD_SIZE || deviceStatus == null) {
            return null;
        }
        int i = (toInt(bArr[2]) << 16) + toInt(bArr[0]) + (toInt(bArr[1]) << 8);
        float f = (((toFloat(bArr[3]) + toFloat(toInt(bArr[4]) << 8)) + toFloat(toInt(bArr[14]) << 16)) + toFloat(toInt(bArr[15]) << 24)) / DISTANCE_TO_FLOAT;
        DeviceSpeedZoneConfiguration deviceSpeedZoneConfiguration = new DeviceSpeedZoneConfiguration(toInt(bArr[7]), toInt(bArr[5]), toInt(bArr[6]), 0);
        int i2 = toInt(bArr[8]);
        int i3 = toInt(bArr[9]);
        if (deviceStatus.getMaintenanceInfo() != null) {
            if (supportsMaintenanceV3(versionInfo)) {
                int i4 = MAINTENANCE_STEPS_TO_BYTE_V3;
                deviceStatus.getMaintenanceInfo().setSteps(i3);
                deviceStatus.getMaintenanceInfo().setStepThreshold(i2 * i4);
                deviceStatus.getMaintenanceInfo().setMaxThreshold(i4 * 255);
                deviceStatus.getMaintenanceInfo().setMinThreshold(i4);
            } else if (supportsMaintenanceV2(versionInfo)) {
                int i5 = MAINTENANCE_STEPS_TO_BYTE_V2;
                deviceStatus.getMaintenanceInfo().setSteps(i3 * i5);
                deviceStatus.getMaintenanceInfo().setStepThreshold(i2 * i5);
                deviceStatus.getMaintenanceInfo().setMaxThreshold(i5 * 255);
                deviceStatus.getMaintenanceInfo().setMinThreshold(i5);
            } else {
                int i6 = MAINTENANCE_STEPS_TO_BYTE;
                deviceStatus.getMaintenanceInfo().setSteps(i3 * i6);
                deviceStatus.getMaintenanceInfo().setStepThreshold(i2 * i6);
                deviceStatus.getMaintenanceInfo().setMaxThreshold(i6 * 255);
                deviceStatus.getMaintenanceInfo().setMinThreshold(i6);
            }
        }
        boolean z = (toInt(bArr[10]) & 1) == 0;
        boolean z2 = (2 & toInt(bArr[10])) > 0;
        boolean z3 = (toInt(bArr[10]) & 4) > 0;
        boolean z4 = (toInt(bArr[10]) & 8) > 0;
        boolean z5 = (toInt(bArr[10]) & 16) > 0;
        PairingModeTriggerType fromValue = PairingModeTriggerType.fromValue(3 & (toInt(bArr[10]) >> 5));
        if (fromValue == null) {
            fromValue = PairingModeTriggerType.none;
        }
        PairingModeTriggerType pairingModeTriggerType = fromValue;
        boolean z6 = (bArr[10] & 128) > 0;
        IrMode fromValue2 = IrMode.fromValue(bArr[11]);
        if (fromValue2 == null) {
            fromValue2 = IrMode.UNKNOWN;
        }
        return new DeviceStatus(deviceStatus, f, i, deviceSpeedZoneConfiguration, z, z2, fromValue2, z3, z4, z5, pairingModeTriggerType, z6, bArr);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static DeviceStatus parseDeviceDiagnosticResponse(byte[] bArr, DeviceStatus deviceStatus) {
        if (bArr == null || bArr.length < STATUS_RESPONSE_PAYLOAD_SIZE || deviceStatus == null) {
            return null;
        }
        int i = toInt(bArr[0]);
        float f = toFloat(toInt(bArr[1]) + (toInt(bArr[2]) << 8)) / TEMP_TO_FLOAT;
        toFloat(toInt(bArr[3]) + (toInt(bArr[4]) << 8));
        float f2 = TEMP_TO_FLOAT;
        toFloat(toInt(bArr[5]) + (toInt(bArr[6]) << 8));
        float f3 = TEMP_TO_FLOAT;
        toFloat(toInt(bArr[7]) + (toInt(bArr[8]) << 8));
        float f4 = TEMP_TO_FLOAT;
        boolean z = (bArr[9] & 1) == 1;
        TemperatureStatusCode fromValue = TemperatureStatusCode.fromValue(3 & (toInt(bArr[9]) >> 1));
        if (fromValue == null) {
            fromValue = TemperatureStatusCode.TEMP_STATUS_UNKNOWN;
        }
        return new DeviceStatus(deviceStatus, new TemperatureInfo(f, fromValue, z), i, ((toInt(bArr[9]) >> 7) & 1) == 1, toInt(bArr[10]), (toInt(bArr[11]) & 1) == 1, (bArr[11] & 2) > 0, bArr[12], bArr);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public static DeviceStatus parseDeviceStatusExt2Response(byte[] bArr, DeviceStatus deviceStatus, VersionInfo versionInfo) {
        if (bArr.length < STATUS_RESPONSE_PAYLOAD_SIZE || deviceStatus == null) {
            return null;
        }
        int i = toInt(bArr[0]) + (toInt(bArr[1]) << 8) + (toInt(bArr[2]) << 16) + (toInt(bArr[3]) << 24);
        int i2 = toInt(bArr[4]);
        MaintenanceInfo maintenanceInfo = deviceStatus.getMaintenanceInfo();
        if (supportsMaintenanceV3(versionInfo) && maintenanceInfo != null) {
            maintenanceInfo.setSteps(maintenanceInfo.getSteps() + (toInt(bArr[5]) << 8) + (toInt(bArr[6]) << 16));
        }
        return new DeviceStatus(deviceStatus, i, i2, maintenanceInfo, bArr);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public static DeviceStatus parseDeviceStatusIotResponse(byte[] bArr, DeviceStatus deviceStatus) {
        if (bArr.length < STATUS_RESPONSE_PAYLOAD_SIZE || deviceStatus == null) {
            return null;
        }
        return new DeviceStatus(deviceStatus, bArr[0], bArr);
    }

    private static boolean supportsMaintenanceV3(VersionInfo versionInfo) {
        if (versionInfo == null) {
            return false;
        }
        VersionInfo versionInfo2 = new VersionInfo(3, 30, 0);
        return versionInfo.isGreaterThan(versionInfo2) || versionInfo.isEqual(versionInfo2);
    }

    private static boolean supportsMaintenanceV2(VersionInfo versionInfo) {
        if (versionInfo == null) {
            return false;
        }
        VersionInfo versionInfo2 = new VersionInfo(3, 20, 0);
        return versionInfo.isGreaterThan(versionInfo2) || versionInfo.isEqual(versionInfo2);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static byte[] parseSecureAuthenticateResponse(byte[] bArr) {
        if (bArr == null || bArr.length < SECURED_AUTHENTICATE_VERIFICATION_PAYLOAD_SIZE) {
            return null;
        }
        byte[] bArr2 = new byte[SECURED_AUTHENTICATE_VERIFICATION_PAYLOAD_SIZE];
        for (int i = 0; i < SECURED_AUTHENTICATE_VERIFICATION_PAYLOAD_SIZE; i++) {
            bArr2[i] = bArr[i];
        }
        return bArr2;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static byte[] parseSecureAuthenticateBroadcastResponse(byte[] bArr) {
        if (bArr == null || bArr.length < SECURED_AUTHENTICATE_VERIFICATION_PAYLOAD_SIZE) {
            return null;
        }
        byte[] bArr2 = new byte[SECURED_AUTHENTICATE_VERIFICATION_PAYLOAD_SIZE];
        for (int i = 0; i < SECURED_AUTHENTICATE_VERIFICATION_PAYLOAD_SIZE; i++) {
            bArr2[i] = bArr[i];
        }
        return bArr2;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public static byte[] getSetPauseAfterIdleState(boolean z, byte b) {
        byte[] baseRequest = getBaseRequest(MESSAGE_ID_SET_PAUSE_AFTER_IDLE);
        baseRequest[PAYLOAD_START] = z ? (byte) 1 : (byte) 0;
        baseRequest[PAYLOAD_START + 1] = b;
        return baseRequest;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public static ComponentInfo parseComponentInfoResponse(byte[] bArr) {
        if (bArr.length < STATUS_RESPONSE_PAYLOAD_SIZE) {
            return null;
        }
        int i = bArr[0] & 255;
        long j = (bArr[1] & 255) + ((bArr[2] & 255) << 8) + ((bArr[3] & 255) << 16) + ((bArr[4] & 255) << 24) + ((bArr[5] & 255) << 32) + ((bArr[6] & 255) << 40);
        long j2 = (toInt(bArr[1]) & 255) + (toInt(bArr[2]) << 8) + (toInt(bArr[3]) << 16);
        int i2 = bArr[7] & 255;
        int i3 = bArr[8] & 255;
        int i4 = bArr[9] & 255;
        boolean z = (bArr[10] & 1) == 1;
        String format = String.format("%012x", Long.valueOf(j));
        ComponentType fromValue = ComponentType.fromValue(i);
        return new ComponentInfo(fromValue, format, j2, z, new ComponentVersionInfo(fromValue, i2, i3, i4));
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public static TestNotification parseTestNotification(byte[] bArr) {
        if (bArr.length < STATUS_RESPONSE_PAYLOAD_SIZE) {
            return TestNotification.notificationUnknown;
        }
        TestNotification fromValue = TestNotification.fromValue(bArr[0] & 255);
        return fromValue != null ? fromValue : TestNotification.notificationUnknown;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public static WifiApInfo parseWifiApInfo(byte[] bArr) {
        if (bArr == null) {
            return null;
        }
        return new WifiApInfo(new String(Arrays.copyOf(bArr, 32)), bArr[32] & 255, (bArr[33] & 1) == 1, ((bArr[33] & 2) >> 1) == 1, ((bArr[33] & 4) >> 2) == 1);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public static int parseOtaUpdateProgress(byte[] bArr) {
        return bArr[0] & 255;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public static DeviceDebugLogInfo parseDeviceDebugLogMessage(byte[] bArr) {
        int i = (bArr[0] & 255) + ((bArr[1] & 255) << 8);
        int i2 = bArr[2] & 255;
        ArrayList arrayList = new ArrayList();
        for (int i3 = 0; i3 < i2; i3++) {
            int i4 = (i3 * 4) + 3;
            arrayList.add(new DeviceDebugLogEvent(bArr[i4] & 255, (bArr[i4 + 1] & 255) + ((bArr[i4 + 2] & 255) << 8) + ((bArr[i4 + 3] & 255) << 16)));
        }
        DeviceDebugLogEvent[] deviceDebugLogEventArr = new DeviceDebugLogEvent[arrayList.size()];
        arrayList.toArray(deviceDebugLogEventArr);
        return new DeviceDebugLogInfo(i, i2, deviceDebugLogEventArr);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public static DeviceIrDebugEventInfo parseDeviceIrDebugLogEventMessage(byte[] bArr) {
        DeviceIrDebugAction fromValue = DeviceIrDebugAction.fromValue(bArr[0] & 255);
        if (fromValue == null) {
            fromValue = DeviceIrDebugAction.none;
        }
        int i = (bArr[1] & 255) + ((bArr[2] & 255) << 8) + ((bArr[3] & 255) << 16) + ((bArr[4] & 255) << 24);
        int[] iArr = new int[bArr.length - 5];
        for (int i2 = 5; i2 < bArr.length; i2++) {
            iArr[i2 - 5] = bArr[i2];
        }
        return new DeviceIrDebugEventInfo(fromValue, i, iArr);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public static DeviceUserStatsLogItem parseDeviceUserStatsReadyEventMessage(byte[] bArr) {
        if (bArr == null) {
            return null;
        }
        return new DeviceUserStatsLogItem(bArr[0] & 255, bArr[1] & 255);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public static DeviceUserStatsUnclaimedLogInfo parseDeviceUnclaimedUserStatsLogInfoMessage(byte[] bArr) {
        if (bArr == null) {
            return null;
        }
        int i = bArr[0] & 255;
        byte b = bArr[1];
        return new DeviceUserStatsUnclaimedLogInfo(i, toInt(bArr[2]), toInt(bArr[3]), (bArr[4] & 255) + (bArr[5] << 8) + (bArr[6] << 16) + (bArr[7] << MESSAGE_ID_EMERGENCY_STOP_REQUEST), (bArr[8] & 255) + ((bArr[9] & 255) << 8) + ((bArr[10] & 255) << 16) + ((bArr[11] & 255) << 24));
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public static DeviceUserStatsLogInfo parseDeviceUserStatsDataEventMessage(byte[] bArr) {
        int i;
        long j;
        if (bArr == null) {
            return null;
        }
        int i2 = bArr[0] & 255;
        int i3 = bArr[1] & 255;
        long j2 = (bArr[2] & 255) + ((bArr[3] & 255) << 8) + ((bArr[4] & 255) << 16) + ((bArr[5] & 255) << 24);
        System.out.println("TS: initial " + j2);
        int i4 = (bArr[9] << MESSAGE_ID_EMERGENCY_STOP_REQUEST) + (bArr[6] & 255) + (bArr[7] << 8) + (bArr[8] << 16);
        int i5 = ((bArr[13] & 255) << 24) + (bArr[10] & 255) + ((bArr[11] & 255) << 8) + ((bArr[12] & 255) << 16);
        int i6 = (bArr[14] & 255) + ((bArr[15] & 255) << 8) + ((bArr[16] & 255) << 16) + ((bArr[17] & 255) << 24);
        int i7 = (bArr[18] & 255) + ((bArr[19] & 255) << 8) + ((bArr[20] & 255) << 16) + ((bArr[21] & 255) << 24);
        int i8 = ((bArr[23] & 255) << 8) + (bArr[22] & 255);
        long j3 = j2 * 1000;
        Date date = new Date(j3);
        System.out.println("TS: INITIALDATE: " + date);
        DeviceUserStatsLogInfo deviceUserStatsLogInfo = new DeviceUserStatsLogInfo(i2, i3, date, i4, i5, ((float) i6) / DISTANCE_TO_FLOAT, ((float) i7) / DISTANCE_TO_FLOAT);
        ArrayList arrayList = new ArrayList();
        int i9 = 24;
        for (int i10 = 0; i10 < i8; i10++) {
            if (i3 == 0) {
                j = (bArr[i9] & 255) + ((bArr[i9 + 1] & 255) << 8) + ((bArr[i9 + 2] & 255) << 16);
                i = bArr[i9 + 3] & 255;
            } else {
                int i11 = i9 + 3;
                long j4 = (bArr[i9] & 255) + ((bArr[i9 + 1] & 255) << 8) + ((bArr[i9 + 2] & 255) << 16) + ((bArr[i11] & 1) << 24);
                i = (bArr[i11] >> 1) & 127 & 255;
                j = j4;
            }
            System.out.println("TS: segments: " + j + ", " + i);
            Date date2 = new Date(j3 + j);
            System.out.println("TS: entryDate: " + date2);
            arrayList.add(new DeviceUserStatsLogSegmentInfo(date2, ((float) i) / SPEED_TO_FLOAT));
            i9 += 4;
        }
        deviceUserStatsLogInfo.segments = new DeviceUserStatsLogSegmentInfo[arrayList.size()];
        arrayList.toArray(deviceUserStatsLogInfo.segments);
        return deviceUserStatsLogInfo;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public static int[] parseBtAudioPassword(byte[] bArr) {
        if (bArr == null) {
            return null;
        }
        int i = 0;
        int i2 = bArr[0] & 255;
        if (i2 > bArr.length + 1) {
            return new int[0];
        }
        int[] iArr = new int[i2];
        while (i < i2) {
            int i3 = i + 1;
            iArr[i] = bArr[i3] & 255;
            i = i3;
        }
        return iArr;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public static UserInteractionStatus parseUserInteractionStatus(byte[] bArr) {
        if (bArr == null || bArr.length < 5) {
            return null;
        }
        int i = bArr[0] & 255;
        int i2 = bArr[1] & 255;
        return new UserInteractionStatus(bArr[4] > 0, bArr[2] & 255, bArr[3] & 255, i2, i);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public static UserInteractionHandrailStepEventInfo parseUserInteractionStepInfo(byte[] bArr) {
        if (bArr == null || bArr.length < 7) {
            return null;
        }
        int i = toInt(bArr[0]);
        int i2 = toInt(bArr[1]);
        UserInteractionStepInfo userInteractionStepInfo = new UserInteractionStepInfo(toInt(bArr[2]), toInt(bArr[3]));
        UserInteractionStepInfo userInteractionStepInfo2 = new UserInteractionStepInfo(toInt(bArr[4]), toInt(bArr[5]));
        int i3 = toInt(bArr[6]);
        return new UserInteractionHandrailStepEventInfo(new UserInteractionSteps(i, i2, userInteractionStepInfo, userInteractionStepInfo2), (i3 & 1) != 0 ? UserInteractionHandrailEvent.fromValue((i3 >> 1) & 127) : null);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static UserInteractionHandrailEvent parseUserInteractionHandrailEvent(byte[] bArr) {
        if (bArr == null || bArr.length < 2) {
            return null;
        }
        toInt(bArr[0]);
        return UserInteractionHandrailEvent.fromValue(toInt(bArr[1]));
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public static BleRemoteTestResults parseBleRemoteTestResults(byte[] bArr) {
        ComponentType fromValue = ComponentType.fromValue(bArr[0] & 255);
        if (fromValue == null) {
            fromValue = ComponentType.unknown;
        }
        ComponentType componentType = fromValue;
        String bytesToHex = bytesToHex(new byte[]{bArr[1], bArr[2], bArr[3], bArr[4], bArr[5], bArr[6]});
        int i = bArr[7] & 255;
        int i2 = bArr[8] & 255;
        int i3 = bArr[9] & 255;
        int i4 = bArr[10] & 255;
        boolean z = i4 == 1;
        return new BleRemoteTestResults(componentType, bytesToHex, i, i2, i3, z, i4, !z ? BleRemoteTestError.fromValue(bArr[11] & 255) : null);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static byte[] getAuthenticationRequest(byte[] bArr) {
        if (bArr == null) {
            return null;
        }
        byte[] baseRequest = getBaseRequest(MESSAGE_ID_AUTHENTICATE);
        byte[] bArr2 = MESSAGE_AUTHENTICATE_SECRET_KEY;
        for (int i = 0; i < bArr2.length; i++) {
            baseRequest[PAYLOAD_START + i + 0] = bArr2[i];
        }
        int length = bArr2.length;
        for (int i2 = 0; i2 < bArr.length; i2++) {
            baseRequest[PAYLOAD_START + i2 + length] = bArr[i2];
        }
        return baseRequest;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static byte[] getAuthenticateVerifyRequest(byte[] bArr) {
        return getBaseRequest(MESSAGE_ID_AUTHENTICATE_VERIFY, bArr);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static byte[] getSubscribeRequest() {
        return getBaseRequest(MESSAGE_ID_SUBSCRIBE_STATUS);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static byte[] getPowerRequest() {
        return getBaseRequest((byte) 1);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static byte[] getSpeedUpRequest() {
        return getBaseRequest((byte) 2);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static byte[] getSpeedDownRequest() {
        return getBaseRequest((byte) 3);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static byte[] getDeviceSecureAuthenticationRequest(byte[] bArr) {
        return getBaseRequest(MESSAGE_ID_SECURE_AUTHENTICATE, bArr);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static byte[] getDeviceSecureAuthenticationVerifyRequest(byte[] bArr) {
        return getBaseRequest(MESSAGE_ID_SECURE_AUTHENTICATE_VERIFY, bArr);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static byte[] getMacAddressVerifyRequest(String str) {
        byte[] baseRequest = getBaseRequest(MESSAGE_ID_VERIFY_MAC_ADDRESS);
        byte[] hexToBytes = hexToBytes(str);
        if (hexToBytes == null) {
            return null;
        }
        for (int i = 0; i < hexToBytes.length; i++) {
            baseRequest[PAYLOAD_START + i] = hexToBytes[i];
        }
        return baseRequest;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static byte[] getSetSpeedUnitRequest(DistanceUnits distanceUnits) {
        if (distanceUnits == null) {
            return null;
        }
        switch (distanceUnits) {
            case MI:
                return getBaseRequest((byte) 9);
            case KM:
                return getBaseRequest((byte) 8);
            default:
                return null;
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static byte[] getSetSpeedRequest(float f) {
        byte[] baseRequest = getBaseRequest((byte) 10);
        baseRequest[PAYLOAD_START] = (byte) (f * SPEED_TO_FLOAT);
        return baseRequest;
    }

    static byte[] getDeviceStatusRequest() {
        return getBaseRequest((byte) 4);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static byte[] getFactoryResetRequest() {
        return getBaseRequest((byte) 11);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static byte[] getDeviceInfoRequest() {
        return getBaseRequest((byte) 16);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static byte[] getSetRemoteStatusRequest(RemoteStatus remoteStatus) {
        if (remoteStatus == null) {
            return null;
        }
        byte[] baseRequest = getBaseRequest(MESSAGE_ID_SET_REMOTE_STATUS);
        baseRequest[PAYLOAD_START] = (byte) remoteStatus.value();
        return baseRequest;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public static byte[] getSetAccelerationZoneStart(int i) {
        byte[] baseRequest = getBaseRequest(MESSAGE_ID_SET_ACCELERATE_ZONE_START);
        int i2 = PAYLOAD_START;
        if ((i < 0 ? 0 : i) >= 255) {
            i = 255;
        }
        baseRequest[i2] = (byte) i;
        return baseRequest;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public static byte[] getSetDecelerationZoneEnd(int i) {
        byte[] baseRequest = getBaseRequest(MESSAGE_ID_SET_DECELERATE_ZONE_END);
        int i2 = PAYLOAD_START;
        if ((i < 0 ? 0 : i) >= 255) {
            i = 255;
        }
        baseRequest[i2] = (byte) i;
        return baseRequest;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public static byte[] getSetEmergencyHandrailEnabledState(boolean z) {
        byte[] baseRequest = getBaseRequest(MESSAGE_ID_SET_EMERG_HANDRAIL_ENABLED);
        baseRequest[PAYLOAD_START] = z ? (byte) 1 : (byte) 0;
        return baseRequest;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public static byte[] getEmergencyStopRequest() {
        return getBaseRequest(MESSAGE_ID_EMERGENCY_STOP_REQUEST);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public static byte[] getSetMaintenanceThresholdRequest(int i, VersionInfo versionInfo) {
        int i2;
        byte[] baseRequest = getBaseRequest(MESSAGE_ID_MAINTENANCE_STEP_REQUEST);
        VersionInfo versionInfo2 = new VersionInfo(3, 20, 0);
        if (versionInfo.isGreaterThan(versionInfo2) || versionInfo.isEqual(versionInfo2)) {
            i2 = MAINTENANCE_STEPS_TO_BYTE_V2;
        } else {
            i2 = MAINTENANCE_STEPS_TO_BYTE;
        }
        int i3 = i / i2;
        if (i3 < 1) {
            i3 = 1;
        }
        if (i3 > 255) {
            i3 = 255;
        }
        baseRequest[PAYLOAD_START] = (byte) i3;
        return baseRequest;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public static byte[] getResetMaintenanceRequest(boolean z) {
        byte[] baseRequest = getBaseRequest(MESSAGE_ID_MAINTENANCE_RESET_REQUEST);
        baseRequest[PAYLOAD_START] = z ? (byte) 1 : (byte) 0;
        return baseRequest;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public static byte[] getBleEnableRequest(boolean z) {
        byte[] baseRequest = getBaseRequest(MESSAGE_ID_BLE_ENABLE_REQUEST);
        baseRequest[PAYLOAD_START] = z ? (byte) 1 : (byte) 0;
        return baseRequest;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public static byte[] getTestStartRequest() {
        return getBaseRequest(MESSAGE_ID_TEST_START);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public static byte[] getSetIrModeRequest(IrMode irMode) {
        if (irMode == null) {
            return null;
        }
        byte[] baseRequest = getBaseRequest(MESSAGE_ID_SET_IR_MODE);
        baseRequest[PAYLOAD_START] = (byte) irMode.value();
        return baseRequest;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public static byte[] getStartWiFiApScanRequest() {
        return getBaseRequest(MESSAGE_ID_GET_AVAILABLE_WIFI_AP_INFO);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public static PacketizedMessage getOtaUpdatePacketizeRequest(OtaUpdateInfo otaUpdateInfo) {
        if (otaUpdateInfo == null || otaUpdateInfo.getSsid() == null || otaUpdateInfo.getPassword() == null || otaUpdateInfo.getUrl() == null) {
            return null;
        }
        byte[] bytes = otaUpdateInfo.getSsid().getBytes();
        byte[] bytes2 = otaUpdateInfo.getPassword().getBytes();
        byte[] bytes3 = otaUpdateInfo.getUrl().getBytes();
        byte[] hexToBytes = hexToBytes(otaUpdateInfo.clientSecret);
        if (hexToBytes == null) {
            return null;
        }
        byte[] bArr = new byte[256];
        System.arraycopy(bytes, 0, bArr, 0, bytes.length);
        System.arraycopy(bytes2, 0, bArr, 32, bytes2.length);
        System.arraycopy(bytes3, 0, bArr, 96, bytes3.length);
        bArr[223] = (byte) (!otaUpdateInfo.isPersistent());
        System.arraycopy(hexToBytes, 0, bArr, 224, hexToBytes.length);
        return new PacketizedMessage(MESSAGE_ID_OTA_UPDATE, MESSAGE_STATUS_REQUEST, bArr, (byte) 16);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public static PacketizedMessage getOtaConfigSettingsPacketizeRequest(OtaUpdateInfo otaUpdateInfo) {
        if (otaUpdateInfo == null || otaUpdateInfo.getSsid() == null || otaUpdateInfo.getPassword() == null) {
            return null;
        }
        byte[] bytes = otaUpdateInfo.getSsid().getBytes();
        byte[] bytes2 = otaUpdateInfo.getPassword().getBytes();
        byte[] bytes3 = otaUpdateInfo.getUrl().getBytes();
        byte[] hexToBytes = hexToBytes(otaUpdateInfo.clientSecret);
        byte[] bArr = new byte[256];
        System.arraycopy(bytes, 0, bArr, 0, bytes.length);
        System.arraycopy(bytes2, 0, bArr, 32, bytes2.length);
        System.arraycopy(bytes3, 0, bArr, 96, bytes3.length);
        bArr[233] = 0;
        System.arraycopy(hexToBytes, 0, bArr, 224, hexToBytes.length);
        return new PacketizedMessage(MESSAGE_ID_SET_OTA_CONFIG, MESSAGE_STATUS_REQUEST, bArr, (byte) 16);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public static byte[] getOtaConfigSettings() {
        return getBaseRequest(MESSAGE_ID_GET_OTA_CONFIG);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public static byte[] clearOtaConfigSettings() {
        return getBaseRequest(MESSAGE_ID_CLEAR_OTA_CONFIG);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public static byte[] getOtaUpdateSetMode() {
        return getBaseRequest(MESSAGE_ID_OTA_SET_MODE);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public static byte[] getBroadcastDeviceStatusConfirmationRequest(boolean z) {
        byte[] baseRequest = getBaseRequest(MESSAGE_ID_BROADCAST_DEVICE_STATUS);
        baseRequest[STATUS_INDEX] = (byte) (!z ? 1 : 0);
        return baseRequest;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public static byte[] getSetTotalStatusRequest(TotalStatus totalStatus) {
        if (totalStatus == null || totalStatus.distanceUnits == null) {
            return null;
        }
        byte[] baseRequest = getBaseRequest(MESSAGE_ID_SET_TOTAL_STATUS);
        float f = totalStatus.distanceUnits == DistanceUnits.KM ? totalStatus.totalDistance : totalStatus.totalDistance / KM_TO_MILES_CONVERT;
        baseRequest[PAYLOAD_START] = (byte) (totalStatus.totalSteps & 255);
        baseRequest[PAYLOAD_START + 1] = (byte) ((totalStatus.totalSteps >> 8) & 255);
        baseRequest[PAYLOAD_START + 2] = (byte) ((totalStatus.totalSteps >> 16) & 255);
        int i = (int) ((f * DISTANCE_TO_FLOAT) + 0.5f);
        baseRequest[PAYLOAD_START + 3] = (byte) (i & 255);
        baseRequest[PAYLOAD_START + 4] = (byte) ((i >> 8) & 255);
        baseRequest[PAYLOAD_START + 5] = (byte) ((i >> 16) & 255);
        baseRequest[PAYLOAD_START + 6] = (byte) ((i >> 24) & 255);
        return baseRequest;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public static byte[] getResetStopRequest() {
        return getBaseRequest(MESSAGE_ID_RESET_STOP);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public static PacketizedMessage getWifiUrlTestPacketizeRequest(WifiUrlTestInfo wifiUrlTestInfo) {
        if (wifiUrlTestInfo == null || wifiUrlTestInfo.getSsid() == null || wifiUrlTestInfo.getPassword() == null) {
            return null;
        }
        byte[] bArr = new byte[224];
        System.arraycopy(wifiUrlTestInfo.getSsid().getBytes(), 0, bArr, 0, wifiUrlTestInfo.getSsid().getBytes().length);
        System.arraycopy(wifiUrlTestInfo.getPassword().getBytes(), 0, bArr, 32, wifiUrlTestInfo.getPassword().getBytes().length);
        System.arraycopy(wifiUrlTestInfo.getUrl().getBytes(), 0, bArr, 96, wifiUrlTestInfo.getUrl().getBytes().length);
        return new PacketizedMessage(MESSAGE_ID_WIFI_URL_TEST, MESSAGE_STATUS_REQUEST, bArr, (byte) 14);
    }

    protected static byte[] getDeviceIrDebugLogRequest() {
        return getBaseRequest(MESSAGE_ID_DEVICE_IR_DEBUG_LOG);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public static byte[] getLogUserStatsEnableRequest(boolean z) {
        byte[] baseRequest = getBaseRequest(MESSAGE_ID_LOG_USER_STATS_ENABLE);
        baseRequest[PAYLOAD_START] = z ? (byte) 1 : (byte) 0;
        return baseRequest;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public static byte[] getProfileIdRequest(byte[] bArr) {
        if (bArr == null) {
            return null;
        }
        byte[] baseRequest = getBaseRequest(MESSAGE_ID_GET_PROFILE_ID);
        System.arraycopy(bArr, 0, baseRequest, PAYLOAD_START, bArr.length);
        return baseRequest;
    }

    private static void printPayload(byte[] bArr) {
        System.out.println("********* Payload ************");
        for (byte b : bArr) {
            System.out.print((b & 255) + " ");
        }
        System.out.print("\n********************************\n");
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public static byte[] getTimestampRequest() {
        byte[] baseRequest = getBaseRequest(MESSAGE_ID_GET_TIMESTAMP);
        long currentTimeMillis = System.currentTimeMillis() / 1000;
        PrintStream printStream = System.out;
        printStream.println("TS: gettimestamp: " + currentTimeMillis);
        baseRequest[PAYLOAD_START] = (byte) ((int) (currentTimeMillis & 255));
        baseRequest[PAYLOAD_START + 1] = (byte) ((int) ((currentTimeMillis >> 8) & 255));
        baseRequest[PAYLOAD_START + 2] = (byte) ((int) ((currentTimeMillis >> 16) & 255));
        baseRequest[PAYLOAD_START + 3] = (byte) ((currentTimeMillis >> 24) & 255);
        printPayload(baseRequest);
        return baseRequest;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public static byte[] getLogUserStatsDataRequest(int i) {
        byte[] baseRequest = getBaseRequest(MESSAGE_ID_LOG_USER_STATS_DATA);
        baseRequest[PAYLOAD_START] = (byte) i;
        return baseRequest;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public static byte[] getLogUserStatsDeleteRequest(int i) {
        byte[] baseRequest = getBaseRequest(MESSAGE_ID_LOG_USER_STATS_DELETE);
        baseRequest[PAYLOAD_START] = (byte) i;
        return baseRequest;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public static byte[] getLogUserStatsDataRequestV2(int i) {
        byte[] baseRequest = getBaseRequest(MESSAGE_ID_LOG_USER_STATS_V2_DATA);
        baseRequest[PAYLOAD_START] = (byte) i;
        return baseRequest;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public static byte[] getLogUserStatsDeleteRequestV2(int i) {
        byte[] baseRequest = getBaseRequest(MESSAGE_ID_LOG_USER_STATS_V2_DELETE);
        baseRequest[PAYLOAD_START] = (byte) i;
        return baseRequest;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public static byte[] getClaimActiveLogUserStatsRequest() {
        byte[] baseRequest = getBaseRequest(MESSAGE_ID_CLAIM_ACTIVE_LOG_USER_STATS);
        long currentTimeMillis = System.currentTimeMillis() / 1000;
        baseRequest[PAYLOAD_START] = (byte) (currentTimeMillis & 255);
        baseRequest[PAYLOAD_START + 1] = (byte) ((currentTimeMillis >> 8) & 255);
        baseRequest[PAYLOAD_START + 2] = (byte) ((currentTimeMillis >> 16) & 255);
        baseRequest[PAYLOAD_START + 3] = (byte) ((currentTimeMillis >> 24) & 255);
        return baseRequest;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public static byte[] getUnclaimedActivityInfoRequest() {
        byte[] baseRequest = getBaseRequest(MESSAGE_ID_GET_UNCLAIMED_USER_STATS_LOG_INFO);
        long currentTimeMillis = System.currentTimeMillis() * 1000;
        baseRequest[PAYLOAD_START] = (byte) (currentTimeMillis & 255);
        baseRequest[PAYLOAD_START + 1] = (byte) ((currentTimeMillis >> 8) & 255);
        baseRequest[PAYLOAD_START + 2] = (byte) ((currentTimeMillis >> 16) & 255);
        baseRequest[PAYLOAD_START + 3] = (byte) ((currentTimeMillis >> 24) & 255);
        return baseRequest;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public static byte[] claimUnclaimedLogUserStatsInfo(Date date, int i) {
        byte[] baseRequest = getBaseRequest(MESSAGE_ID_CLAIM_UNCLAIMED_USER_STATS_LOG_INFO);
        long time = date.getTime() / 1000;
        baseRequest[PAYLOAD_START] = (byte) (time & 255);
        baseRequest[PAYLOAD_START + 1] = (byte) ((time >> 8) & 255);
        baseRequest[PAYLOAD_START + 2] = (byte) ((time >> 16) & 255);
        baseRequest[PAYLOAD_START + 3] = (byte) ((time >> 24) & 255);
        baseRequest[PAYLOAD_START + 4] = (byte) i;
        return baseRequest;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public static byte[] getBtAudioPasswordRequest() {
        return getBaseRequest(MESSAGE_ID_GET_BT_AUDIO_PASSWORD);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public static byte[] getSetBtAudioPasswordRequest(byte[] bArr) {
        byte[] baseRequest = getBaseRequest(MESSAGE_ID_SET_BT_AUDIO_PASSWORD);
        if (bArr == null || bArr.length > PAYLOAD_SIZE - 1) {
            return baseRequest;
        }
        baseRequest[PAYLOAD_START] = (byte) bArr.length;
        for (int i = 0; i < bArr.length; i++) {
            baseRequest[PAYLOAD_START + i + 1] = bArr[i];
        }
        return baseRequest;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public static byte[] getVerifyLedCodeRequest(String str) {
        byte[] baseRequest = getBaseRequest(MESSAGE_ID_VERIFY_LED_CODE);
        if (str == null) {
            return baseRequest;
        }
        byte[] bytes = str.getBytes();
        if (bytes.length > PAYLOAD_SIZE) {
            return baseRequest;
        }
        for (int i = 0; i < bytes.length; i++) {
            baseRequest[PAYLOAD_START + i] = bytes[i];
        }
        return baseRequest;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public static byte[] getSetPairingModeTriggerType(PairingModeTriggerType pairingModeTriggerType) {
        byte[] baseRequest = getBaseRequest(MESSAGE_ID_SET_PAIRING_MODE_TRIGGER);
        baseRequest[PAYLOAD_START] = (byte) pairingModeTriggerType.value();
        return baseRequest;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public static byte[] getSetDeletePairedPhones(boolean z) {
        byte[] baseRequest = getBaseRequest(MESSAGE_ID_SET_DELETE_PAIRED_PHONES);
        if (z) {
            baseRequest[PAYLOAD_START] = 1;
        } else {
            baseRequest[PAYLOAD_START] = 0;
        }
        return baseRequest;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public static byte[] getSetDeviceIrDebugModeEnabledRequest(boolean z) {
        byte[] baseRequest = getBaseRequest(MESSAGE_ID_SET_DEVICE_IR_DEBUG_LOG_MODE);
        baseRequest[PAYLOAD_START] = z ? (byte) 1 : (byte) 0;
        return baseRequest;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public static byte[] getSetEnableUserInteraction(boolean z) {
        byte[] baseRequest = getBaseRequest(MESSAGE_ID_USER_INTERACTION_SET_ENABLE);
        baseRequest[PAYLOAD_START] = z ? (byte) 1 : (byte) 0;
        return baseRequest;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public static byte[] getUserInteractionStatusRequest() {
        return getBaseRequest(MESSAGE_ID_USER_INTERACTION_STATUS);
    }

    protected static byte[] getOtaDoneConfirmationRequest() {
        return getBaseRequest(MESSAGE_ID_OTA_DONE_CONFIRMATION);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public static byte[] getSetGameModeRequest(boolean z, GameModeType gameModeType) {
        if (gameModeType == null) {
            return null;
        }
        byte[] baseRequest = getBaseRequest(MESSAGE_ID_SET_GAME_MODE);
        baseRequest[PAYLOAD_START] = z ? (byte) 1 : (byte) 0;
        baseRequest[PAYLOAD_START + 1] = (byte) gameModeType.value();
        return baseRequest;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public static byte[] getSetGameModeDisplayRequest(boolean z, byte[] bArr) {
        byte[] baseRequest = getBaseRequest(MESSAGE_ID_SET_GAME_MODE_DISPLAY);
        if (bArr == null) {
            return baseRequest;
        }
        baseRequest[PAYLOAD_START] = z ? (byte) 1 : (byte) 0;
        for (int i = 0; i < bArr.length; i++) {
            baseRequest[PAYLOAD_START + i + 1] = bArr[i];
        }
        return baseRequest;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public static byte[] getStartBleRemoteTestRequest() {
        return getBaseRequest(MESSAGE_ID_START_BLE_REMOTE_TEST);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public static byte[] getPauseRequest() {
        return getBaseRequest(MESSAGE_ID_PAUSE);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static byte[] getBaseRequest(byte b) {
        byte[] bArr = new byte[MESSAGE_SIZE];
        bArr[CMD_INDEX] = b;
        bArr[STATUS_INDEX] = MESSAGE_STATUS_REQUEST;
        return bArr;
    }

    static byte[] getBaseRequest(byte b, byte[] bArr) {
        if (bArr == null) {
            return null;
        }
        byte[] baseRequest = getBaseRequest(b);
        for (int i = 0; i < bArr.length; i++) {
            baseRequest[PAYLOAD_START + i] = bArr[i];
        }
        return baseRequest;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static byte[] getBaseRequest(byte b, byte[] bArr, byte b2) {
        if (bArr == null) {
            return null;
        }
        byte[] baseRequest = getBaseRequest(b, bArr);
        baseRequest[ID_INDEX] = b2;
        return baseRequest;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public static byte getGroupCount(byte b) {
        if (b != 64) {
            if (b == 86) {
                return (byte) 1;
            }
            if (b == 88) {
                return (byte) 16;
            }
            if (b != 90) {
                switch (b) {
                    case 47:
                        return (byte) 3;
                    case 48:
                        return (byte) 16;
                    default:
                        switch (b) {
                            case 56:
                                return (byte) 14;
                            case 57:
                                return (byte) 6;
                            case 58:
                                return (byte) 8;
                            default:
                                return (byte) 0;
                        }
                }
            }
        }
        return (byte) DeviceUserStatsLogManager.sharedInstance.getExpectedLogItemGroupCount();
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public static String convertMacAddressToDeviceId(String str) {
        byte[] hexToBytes = hexToBytes(str);
        if (hexToBytes == null) {
            return null;
        }
        reverseByteArray(hexToBytes);
        return bytesToHex(new byte[]{(byte) (((hexToBytes[0] & 255) + OtaUpdateInfo.urlMaxSize) & 255), (byte) (((hexToBytes[1] & 255) + 160) & 255), (byte) (((hexToBytes[2] & 255) + 111) & 255), (byte) (((hexToBytes[0] & 255) + (hexToBytes[1] & 255) + hexToBytes[2]) & 255), (byte) (((hexToBytes[3] & 255) + 13) & 255), (byte) (((hexToBytes[4] & 255) + 70) & 255), (byte) (((hexToBytes[5] & 255) + 122) & 255), (byte) (((hexToBytes[3] & 255) + (hexToBytes[4] & 255) + hexToBytes[5]) & 255)});
    }

    static byte[] hexToBytes(String str) {
        if (str == null) {
            return null;
        }
        int length = str.length();
        byte[] bArr = new byte[length / 2];
        for (int i = 0; i < length; i += 2) {
            bArr[i / 2] = (byte) ((Character.digit(str.charAt(i), 16) << 4) + Character.digit(str.charAt(i + 1), 16));
        }
        return bArr;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static String bytesToHex(byte[] bArr) {
        if (bArr == null) {
            return null;
        }
        StringBuilder sb = new StringBuilder();
        int length = bArr.length;
        for (int i = 0; i < length; i++) {
            sb.append(String.format("%02x", Integer.valueOf(bArr[i] & 255)));
        }
        return sb.toString();
    }

    static void reverseByteArray(byte[] bArr) {
        for (int i = 0; i < bArr.length / 2; i++) {
            byte b = bArr[i];
            bArr[i] = bArr[(bArr.length - 1) - i];
            bArr[(bArr.length - 1) - i] = b;
        }
    }
}
