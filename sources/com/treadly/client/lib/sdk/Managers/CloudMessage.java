package com.treadly.client.lib.sdk.Managers;

/* loaded from: classes2.dex */
public class CloudMessage {
    protected static final String eSmartCloudBaseUrl = "https://edge.treadly.co:51235";
    protected static final String eSmartCloudGenRandNumRoute = "/treadly/authenticate/gen_rand_num";
    protected static final String eSmartCloudValidate = "/treadly/authenticate/validate";
    protected static final String esmartCloudActivationStartRoute = "/treadly/activation/start";
    protected static final String esmartCloudActivationSubmitAckRoute = "/treadly/activation/code/submit/ack";
    protected static final String esmartCloudActivationSubmitRoute = "/treadly/activation/code/submit";
    protected static final String esmartCloudAuthenticateAdmin = "/treadly/authenticate/admin";
    protected static final String esmartCloudAuthenticateFactoryTest = "/treadly/authenticate/factory_test";
    protected static final String esmartCloudAuthenticateReferenceCodeGetRoute = "/treadly/authenticate/reference_code/get";
    protected static final String esmartCloudAuthenticateRoute = "/treadly/authenticate/app";
    protected static final String esmartCloudAuthenticationGetRoute = "/treadly/authenticate/admin/get";
    protected static final String esmartCloudAuthenticationUpdateRoute = "/treadly/authenticate/admin/update";
    protected static final String esmartCloudBroadcastDeviceStatusRoute = "/treadly/authenticate/broadcast/status";
    protected static final String esmartCloudFactoryQaAddDevice = "/treadly/factory_qa/add_device_id";
    protected static final String esmartCloudFactoryQaGetApprovals = "/treadly/factory_qa/approval_limit";
    protected static final String esmartCloudFactoryQaUpdateApprovals = "/treadly/factory_qa/update_approval_limit";
    protected static final String esmartCloudFactoryTestLogRoute = "/treadly/log/factory_test/results";
    protected static final String esmartCloudOtaCheckDeviceIdRoute = "/treadly/ota/check_device_id";
    protected static final String esmartCloudOtaCheckVersionRoute = "/treadly/ota/check_version";
    protected static final String esmartCloudOtaLogRoute = "/treadly/ota/log";
    protected static final String esmartCloudOtaQaCheckVersionRoute = "/treadly/ota/qa/check_version";
    protected static final String esmartCloudRegistrationRoute = "/treadly/registration/validate";
    protected static final String esmartCloudRegistrationUpdateRoute = "/treadly/registration/update";
    protected static final String esmartCloudUpdateDeviceLogRoute = "/treadly/device/debug/log/upload";
    protected static final String esmartCloudUpdateLogRoute = "/treadly/log/upload";
    protected static final String esmartCloudVersionRoute = "/treadly/apps/version";
    private static final byte[] clientSecretValue = {Message.MESSAGE_ID_SET_BT_AUDIO_PASSWORD, Message.MESSAGE_ID_SET_REMOTE_STATUS, Message.MESSAGE_ID_OTA_UPDATE, Message.MESSAGE_ID_OTA_SET_MODE, Message.MESSAGE_ID_PAUSE, Message.MESSAGE_ID_OTA_SET_MODE, 52, Message.MESSAGE_ID_BROADCAST_DEVICE_STATUS, Message.MESSAGE_ID_SET_PAIRING_MODE_TRIGGER, Message.MESSAGE_ID_RESET_STOP, Message.MESSAGE_ID_GET_BT_AUDIO_PASSWORD, Message.MESSAGE_ID_SET_PAIRING_MODE_TRIGGER, Message.MESSAGE_ID_OTA_SET_MODE, Message.MESSAGE_ID_GET_BT_AUDIO_PASSWORD, Message.MESSAGE_ID_GET_BT_AUDIO_PASSWORD, Message.MESSAGE_ID_SET_BT_AUDIO_PASSWORD, Message.MESSAGE_ID_OTA_UPDATE, Message.MESSAGE_ID_GET_BT_AUDIO_PASSWORD, Message.MESSAGE_ID_GET_BT_AUDIO_PASSWORD, Message.MESSAGE_ID_SET_BT_AUDIO_PASSWORD, Message.MESSAGE_ID_SET_TOTAL_STATUS, Message.MESSAGE_ID_BROADCAST_DEVICE_STATUS, Message.MESSAGE_ID_PAUSE, Message.MESSAGE_ID_RESET_STOP, Message.MESSAGE_ID_PAUSE, Message.MESSAGE_ID_OTA_UPDATE, Message.MESSAGE_ID_PAUSE, Message.MESSAGE_ID_BROADCAST_DEVICE_STATUS, Message.MESSAGE_ID_OTA_SET_MODE, Message.MESSAGE_ID_SET_REMOTE_STATUS, Message.MESSAGE_ID_SET_TOTAL_STATUS, Message.MESSAGE_ID_WIFI_URL_TEST, Message.MESSAGE_ID_SET_TOTAL_STATUS, Message.MESSAGE_ID_OTA_IN_PROGRESS, Message.MESSAGE_ID_SET_REMOTE_STATUS, Message.MESSAGE_ID_SET_PAIRING_MODE_TRIGGER, Message.MESSAGE_ID_OTA_SET_MODE, Message.MESSAGE_ID_BROADCAST_DEVICE_STATUS, Message.MESSAGE_ID_OTA_UPDATE, Message.MESSAGE_ID_LOG_USER_STATS_DELETE, Message.MESSAGE_ID_BROADCAST_DEVICE_STATUS, Message.MESSAGE_ID_SET_PAIRING_MODE_TRIGGER, Message.MESSAGE_ID_DEVICE_DEBUG_LOG, Message.MESSAGE_ID_PAUSE, Message.MESSAGE_ID_PAUSE, 52, Message.MESSAGE_ID_SET_PAIRING_MODE_TRIGGER, Message.MESSAGE_ID_SET_REMOTE_STATUS, Message.MESSAGE_ID_SET_TOTAL_STATUS, Message.MESSAGE_ID_VERIFY_LED_CODE, Message.MESSAGE_ID_SET_BT_AUDIO_PASSWORD, Message.MESSAGE_ID_SET_BT_AUDIO_PASSWORD, Message.MESSAGE_ID_OTA_UPDATE, Message.MESSAGE_ID_OTA_IN_PROGRESS, 52, Message.MESSAGE_ID_SET_PAIRING_MODE_TRIGGER, Message.MESSAGE_ID_VERIFY_LED_CODE, Message.MESSAGE_ID_VERIFY_LED_CODE, Message.MESSAGE_ID_LOG_USER_STATS_DELETE, Message.MESSAGE_ID_VERIFY_LED_CODE, Message.MESSAGE_ID_LOG_USER_STATS_DELETE, Message.MESSAGE_ID_PAUSE, Message.MESSAGE_ID_DEVICE_DEBUG_LOG, Message.MESSAGE_ID_BROADCAST_DEVICE_STATUS};
    protected static final String clientSecret = new String(clientSecretValue);

    /* loaded from: classes2.dex */
    protected class Keys {
        protected static final String activationCode = "activation_code";
        protected static final String activationErrorMessage = "activation_error_message";
        protected static final String activationErrorTitle = "activation_error_title";
        protected static final String activationMessage = "activation_message";
        protected static final String activationStatus = "activation_state";
        protected static final String activationSubmitMessage = "activation_submit_message";
        protected static final String activationSubmitTitle = "activation_submit_title";
        protected static final String activationSuccessMessage = "activation_success_message";
        protected static final String activationSuccessTitle = "activation_success_title";
        protected static final String activationTitle = "activation_title";
        protected static final String activationUrl = "activation_url";
        protected static final String appType = "app_type";
        protected static final String appVersion = "app_version";
        protected static final String approvalLimitCurrent = "approval_limit_current";
        protected static final String approvalLimitMax = "approval_limit_max";
        protected static final String authenticationState = "authentication_state";
        protected static final String client_secret = "client_secret";
        protected static final String customBoard = "custom_board";
        protected static final String customBoardVersion = "custom_board_version";
        protected static final String description = "description";
        protected static final String deviceId = "device_id";
        protected static final String deviceType = "device_type";
        protected static final String device_status = "device_status";
        protected static final String file = "file";
        protected static final String id = "id";
        protected static final String macAddress = "mac_address";
        protected static final String mainBoardVersion = "main_board_version";
        protected static final String major = "major";
        protected static final String mandatory = "mandatory";
        protected static final String message = "message";
        protected static final String minimumFirmwareVersion = "minimum_firmware_version";
        protected static final String minor = "minor";
        protected static final String pass = "pass";
        protected static final String patch = "patch";
        protected static final String post_message = "post_message";
        protected static final String post_title = "post_title";
        protected static final String pre_message = "pre_message";
        protected static final String pre_title = "pre_title";
        protected static final String randNumCloudPrime = "random_number_cloud_p";
        protected static final String randNumCustomBoardPrime = "random_number_custom_board_p";
        protected static final String reason = "reason";
        protected static final String referenceCode = "reference_code";
        protected static final String registrationCode = "registration_code";
        protected static final String registrationStatus = "registration_state";
        protected static final String registrationUrl = "registration_url";
        protected static final String sdkVersion = "sdk_version";
        protected static final String serialNumber = "serial_number";
        protected static final String status = "status";
        protected static final String statusError = "error";
        protected static final String statusOk = "ok";
        protected static final String success = "success";
        protected static final String targetVersion = "target_version";
        protected static final String test_case_accelerate_up = "accelerate_up";
        protected static final String test_case_acclerate_down = "accelerate_down";
        protected static final String test_case_factory_reset = "factory_reset";
        protected static final String test_case_handrail_down = "handrail_down";
        protected static final String test_case_handrail_up = "handrail_up";
        protected static final String test_case_start = "start";
        protected static final String test_case_start_down = "start_down";
        protected static final String test_case_start_up = "start_up";
        protected static final String test_case_stop_down = "stop_down";
        protected static final String test_case_stop_up = "stop_up";
        protected static final String test_case_temperature = "temperature";
        protected static final String test_cases = "test_cases";
        protected static final String test_results = "test_results";
        protected static final String timestamp = "timestamp";
        protected static final String title = "title";
        protected static final String treadlyBarcode = "treadly_barcode";
        protected static final String update_available = "update_available";
        protected static final String update_descriptions = "update_descriptions";
        protected static final String update_url = "update_url";
        protected static final String uuid = "uuid";
        protected static final String validation = "validation";
        protected static final String version = "version";

        protected Keys() {
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    /* loaded from: classes2.dex */
    public enum StatusCode {
        ok(0),
        unknown(1),
        macAddressNotFound(2),
        randomNumberExpired(3),
        authenticationError(4),
        authenticationInvalid(5),
        appVersionError(6),
        registrationRequired(7),
        noOtaUpdateAvailable(8),
        registrationUpdateRequired(9),
        approvalLimitReached(10),
        invalidRequest(11),
        deviceNotFound(12);
        
        private int value;

        StatusCode(int i) {
            this.value = i;
        }

        public int value() {
            return this.value;
        }

        public static StatusCode fromValue(int i) {
            StatusCode[] values;
            for (StatusCode statusCode : values()) {
                if (statusCode.value == i) {
                    return statusCode;
                }
            }
            return null;
        }
    }
}
