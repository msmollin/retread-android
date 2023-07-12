package com.treadly.client.lib.sdk.Managers;

import android.app.Activity;
import android.content.pm.PackageManager;
import android.provider.Settings;
import com.facebook.share.internal.ShareConstants;
import com.google.android.gms.measurement.AppMeasurement;
import com.treadly.client.lib.sdk.Managers.CloudMessage;
import com.treadly.client.lib.sdk.Model.ActivationInfo;
import com.treadly.client.lib.sdk.Model.AppType;
import com.treadly.client.lib.sdk.Model.AuthenticateReferenceCodeInfo;
import com.treadly.client.lib.sdk.Model.AuthenticationState;
import com.treadly.client.lib.sdk.Model.ComponentInfo;
import com.treadly.client.lib.sdk.Model.DeviceSecureAuthenticateInfo;
import com.treadly.client.lib.sdk.Model.FactoryTestResults;
import com.treadly.client.lib.sdk.Model.RegistrationInfo;
import com.treadly.client.lib.sdk.Model.RegistrationState;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import org.json.JSONArray;
import org.json.JSONObject;

/* loaded from: classes2.dex */
public class CloudNetworkManager {
    private static final int WEB_REQUEST_THREAD_POOL_THREADS = 16;
    public static final CloudNetworkManager shared = new CloudNetworkManager();
    private String UUID;
    private String appVersion;
    private ExecutorService webThreadPool = Executors.newFixedThreadPool(16);

    /* JADX INFO: Access modifiers changed from: protected */
    /* loaded from: classes2.dex */
    public interface OnSuccessAuthenticateReferenceCodeResponse {
        void onSuccess(boolean z, AuthenticateReferenceCodeInfo authenticateReferenceCodeInfo);
    }

    /* loaded from: classes2.dex */
    protected interface OnSuccessAuthenticationState {
        void onSuccess(boolean z, AuthenticationState authenticationState);
    }

    /* loaded from: classes2.dex */
    public interface OnSuccessListener {
        void onSuccess(boolean z);
    }

    /* loaded from: classes2.dex */
    protected interface OnSuccessRegistrationState {
        void onSuccess(boolean z, RegistrationState registrationState);
    }

    public String getSdkVersion() {
        return "1.1.8";
    }

    private CloudNetworkManager() {
    }

    public void init(Activity activity) {
        setUuid(activity);
        if (activity != null) {
            try {
                setAppVersion(activity.getPackageManager().getPackageInfo(activity.getPackageName(), 0).versionName);
            } catch (PackageManager.NameNotFoundException e) {
                e.printStackTrace();
            }
        }
    }

    public boolean authenticateApp() {
        CloudMessage.StatusCode fromValue;
        JSONObject postJson = RequestUtil.postJson("https://edge.treadly.co:51235/treadly/authenticate/app", getAuthenticateAppPayload());
        boolean z = false;
        if (postJson != null && (fromValue = CloudMessage.StatusCode.fromValue(postJson.optInt("status", 1))) != null && fromValue == CloudMessage.StatusCode.ok) {
            z = true;
        }
        BleConnectionManager.shared.setManagerEnabled(z);
        DeviceManager.shared.setManagerEnabled(z);
        TreadlyLogManager.shared.setManagerEnabled(z);
        return z;
    }

    public boolean authenticateAdmin() {
        CloudMessage.StatusCode fromValue;
        JSONObject postJson = RequestUtil.postJson("https://edge.treadly.co:51235/treadly/authenticate/admin", getAuthenticateAdminPayload(getUuid()));
        return (postJson == null || (fromValue = CloudMessage.StatusCode.fromValue(postJson.optInt("status", 1))) == null || fromValue != CloudMessage.StatusCode.ok) ? false : true;
    }

    protected boolean authenticateFactoryTest() {
        CloudMessage.StatusCode fromValue;
        JSONObject postJson = RequestUtil.postJson("https://edge.treadly.co:51235/treadly/authenticate/factory_test", getAuthenticateFactoryTestPayload(getUuid()));
        return (postJson == null || (fromValue = CloudMessage.StatusCode.fromValue(postJson.optInt("status", 1))) == null || fromValue != CloudMessage.StatusCode.ok) ? false : true;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void updateRegistrationState(ComponentInfo componentInfo, RegistrationState registrationState, final OnSuccessListener onSuccessListener) {
        final JSONObject updateRegistrationStatePayload = getUpdateRegistrationStatePayload(componentInfo, registrationState);
        this.webThreadPool.execute(new Runnable() { // from class: com.treadly.client.lib.sdk.Managers.CloudNetworkManager.1
            @Override // java.lang.Runnable
            public void run() {
                JSONObject postJson = RequestUtil.postJson("https://edge.treadly.co:51235/treadly/registration/update", updateRegistrationStatePayload);
                if (postJson != null) {
                    onSuccessListener.onSuccess(CloudMessage.StatusCode.fromValue(postJson.optInt("status", 1)) == CloudMessage.StatusCode.ok);
                    return;
                }
                onSuccessListener.onSuccess(false);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void verifyRegistrationState(ComponentInfo componentInfo, final OnSuccessRegistrationState onSuccessRegistrationState) {
        final JSONObject verifyRegistrationStatePayload = getVerifyRegistrationStatePayload(componentInfo);
        this.webThreadPool.execute(new Runnable() { // from class: com.treadly.client.lib.sdk.Managers.CloudNetworkManager.2
            @Override // java.lang.Runnable
            public void run() {
                JSONObject postJson = RequestUtil.postJson("https://edge.treadly.co:51235/treadly/registration/validate", verifyRegistrationStatePayload);
                boolean z = false;
                if (postJson != null) {
                    CloudMessage.StatusCode fromValue = CloudMessage.StatusCode.fromValue(postJson.optInt("status", 1));
                    if (fromValue == CloudMessage.StatusCode.registrationRequired) {
                        DeviceManager.shared.sendRequestRegistrationRequiredResponse(new RegistrationInfo(postJson.optString("registration_url"), postJson.optString("registration_code", ""), postJson.optString("title"), postJson.optString(ShareConstants.WEB_DIALOG_PARAM_MESSAGE)));
                        onSuccessRegistrationState.onSuccess(false, null);
                        return;
                    }
                    if (fromValue == CloudMessage.StatusCode.registrationUpdateRequired) {
                        DeviceManager.shared.isPendingRegistrationUpdate = true;
                        z = true;
                    } else if (fromValue == CloudMessage.StatusCode.ok) {
                        onSuccessRegistrationState.onSuccess(true, RegistrationState.fromValue(postJson.optInt("registration_state", 3)));
                        return;
                    }
                    onSuccessRegistrationState.onSuccess(z, null);
                    return;
                }
                onSuccessRegistrationState.onSuccess(false, null);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void fetchActivatoinStartInfo() {
        final JSONObject activationStartPayload = getActivationStartPayload();
        this.webThreadPool.execute(new Runnable() { // from class: com.treadly.client.lib.sdk.Managers.CloudNetworkManager.3
            @Override // java.lang.Runnable
            public void run() {
                JSONObject postJson = RequestUtil.postJson("https://edge.treadly.co:51235/treadly/activation/start", activationStartPayload);
                if (postJson != null && CloudMessage.StatusCode.fromValue(postJson.optInt("status", 1)) == CloudMessage.StatusCode.ok) {
                    ActivationInfo activationInfo = new ActivationInfo();
                    activationInfo.activationUrl = postJson.optString("activation_url");
                    activationInfo.activationMessage = postJson.optString("activation_message");
                    activationInfo.activationTitle = postJson.optString("activation_title");
                    activationInfo.activationSubmitMessage = postJson.optString("activation_submit_message");
                    activationInfo.activationSubmitTitle = postJson.optString("activation_submit_title");
                    activationInfo.activationSuccessMessage = postJson.optString("activation_success_message");
                    activationInfo.activationSuccessTitle = postJson.optString("activation_success_title");
                    activationInfo.activationErrorMessage = postJson.optString("activation_error_message");
                    activationInfo.activationErrorTitle = postJson.optString("activation_error_title");
                    ActivationManager.shared.sendDeviceActivationStartResponse(activationInfo);
                    return;
                }
                ActivationManager.shared.sendDeviceActivationStartResponse(null);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void submitActivationCode(final String str, ComponentInfo componentInfo) {
        final JSONObject activationCodeSubmitPayload = getActivationCodeSubmitPayload(str, componentInfo);
        this.webThreadPool.execute(new Runnable() { // from class: com.treadly.client.lib.sdk.Managers.CloudNetworkManager.4
            @Override // java.lang.Runnable
            public void run() {
                JSONObject postJson = RequestUtil.postJson("https://edge.treadly.co:51235/treadly/activation/code/submit", activationCodeSubmitPayload);
                if (postJson == null) {
                    ActivationManager.shared.sendDeviceActivationSubmitResponse(false);
                    return;
                }
                boolean z = CloudMessage.StatusCode.fromValue(postJson.optInt("status", 1)) == CloudMessage.StatusCode.ok;
                if (z) {
                    if (DeviceManager.shared.isConnected()) {
                        DeviceManager.shared.sendRequest(Message.getBleEnableRequest(true));
                        DeviceManager.shared.authenticateDevice();
                        DeviceManager.shared.pendingActivationCodeSubmitAcknowledge = str;
                    } else {
                        z = false;
                    }
                }
                ActivationManager.shared.sendDeviceActivationSubmitResponse(z);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public boolean submitActivationCodeAcknowledgement(String str, ComponentInfo componentInfo) {
        JSONObject postJson = RequestUtil.postJson("https://edge.treadly.co:51235/treadly/activation/code/submit/ack", getActivationCodeSubmitPayload(str, componentInfo));
        return postJson != null && CloudMessage.StatusCode.fromValue(postJson.optInt("status", 1)) == CloudMessage.StatusCode.ok;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void secureAuthenticateDevice() {
        byte[] fetchRandomNumber = fetchRandomNumber(getUuid());
        if (fetchRandomNumber == null || fetchRandomNumber.length <= 1) {
            return;
        }
        DeviceManager.shared.sendRequest(Message.getDeviceSecureAuthenticationRequest(fetchRandomNumber));
    }

    protected byte[] fetchRandomNumber(String str) {
        JSONArray optJSONArray;
        if (str == null) {
            return null;
        }
        try {
            JSONObject postJson = RequestUtil.postJson("https://edge.treadly.co:51235/treadly/authenticate/gen_rand_num", getGenRandomNumPayload(str));
            if (postJson != null && CloudMessage.StatusCode.fromValue(postJson.optInt("status", 1)) == CloudMessage.StatusCode.ok && (optJSONArray = postJson.optJSONArray("random_number_cloud_p")) != null) {
                byte[] bArr = new byte[optJSONArray.length()];
                for (int i = 0; i < optJSONArray.length(); i++) {
                    bArr[i] = (byte) optJSONArray.optInt(i);
                }
                return bArr;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void sendSecureAuthenticateVerifyRequest(DeviceSecureAuthenticateInfo deviceSecureAuthenticateInfo, ComponentInfo componentInfo) {
        JSONObject postJson = RequestUtil.postJson("https://edge.treadly.co:51235/treadly/authenticate/validate", getSecureAuthenticateVerifyPayload(deviceSecureAuthenticateInfo));
        if (postJson != null) {
            CloudMessage.StatusCode fromValue = CloudMessage.StatusCode.fromValue(postJson.optInt("status", 1));
            if ((fromValue == CloudMessage.StatusCode.ok || fromValue == CloudMessage.StatusCode.authenticationInvalid) && postJson.has("validation")) {
                AuthenticationState fromValue2 = AuthenticationState.fromValue(postJson.optInt("authentication_state", 3));
                JSONArray optJSONArray = postJson.optJSONArray("validation");
                if (optJSONArray != null && optJSONArray.length() > 0) {
                    byte[] bArr = new byte[optJSONArray.length()];
                    for (int i = 0; i < optJSONArray.length(); i++) {
                        bArr[i] = (byte) optJSONArray.optInt(i);
                    }
                    DeviceManager.shared.sendRequest(Message.getDeviceSecureAuthenticationVerifyRequest(bArr));
                }
                if (fromValue2 == AuthenticationState.inactive && !DeviceManager.shared.currentAuthenticationStatus) {
                    ActivationManager.shared.activationRequired(componentInfo);
                }
                ActivationManager.shared.sendDeviceAuthenticationStateResponse(fromValue2);
                return;
            }
            secureAuthenticateDevice();
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void sendBroadcastDeviceStatusRequest(byte[] bArr, byte[] bArr2) {
        JSONObject postJson = RequestUtil.postJson("https://edge.treadly.co:51235/treadly/authenticate/broadcast/status", getBroadcastDeviceStatusPayload(bArr, bArr2));
        if (postJson == null) {
            DeviceManager.shared.sendRequest(Message.getBroadcastDeviceStatusConfirmationRequest(false));
        } else {
            DeviceManager.shared.sendRequest(Message.getBroadcastDeviceStatusConfirmationRequest(CloudMessage.StatusCode.fromValue(postJson.optInt("status", 1)) == CloudMessage.StatusCode.ok));
        }
    }

    public void sendFactoryTestResults(final FactoryTestResults factoryTestResults, final OnSuccessListener onSuccessListener) {
        ArrayList arrayList = new ArrayList();
        arrayList.add(factoryTestResults);
        FactoryTestResults[] storedFactoryTestResults = TreadlyLogManager.shared.getStoredFactoryTestResults(factoryTestResults.customBoardSerialNumber);
        if (storedFactoryTestResults != null && storedFactoryTestResults.length > 0) {
            arrayList.addAll(Arrays.asList(storedFactoryTestResults));
        }
        FactoryTestResults[] factoryTestResultsArr = new FactoryTestResults[arrayList.size()];
        arrayList.toArray(factoryTestResultsArr);
        final JSONObject sendFactoryTestResultsPayload = getSendFactoryTestResultsPayload(factoryTestResultsArr);
        this.webThreadPool.execute(new Runnable() { // from class: com.treadly.client.lib.sdk.Managers.CloudNetworkManager.5
            @Override // java.lang.Runnable
            public void run() {
                JSONObject postJson = RequestUtil.postJson("https://edge.treadly.co:51235/treadly/log/factory_test/results", sendFactoryTestResultsPayload);
                if (postJson == null) {
                    TreadlyLogManager.shared.storeFactoryTestLog(factoryTestResults);
                    onSuccessListener.onSuccess(false);
                    return;
                }
                boolean z = CloudMessage.StatusCode.fromValue(postJson.optInt("status", 1)) == CloudMessage.StatusCode.ok;
                if (!z) {
                    TreadlyLogManager.shared.storeFactoryTestLog(factoryTestResults);
                }
                onSuccessListener.onSuccess(z);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void sendAuthenticationUpdateRequest(String str, AuthenticationState authenticationState, long j, final OnSuccessListener onSuccessListener) {
        final JSONObject authenticationUpdatePayload = getAuthenticationUpdatePayload(authenticationState, str, j);
        this.webThreadPool.execute(new Runnable() { // from class: com.treadly.client.lib.sdk.Managers.CloudNetworkManager.6
            @Override // java.lang.Runnable
            public void run() {
                JSONObject postJson = RequestUtil.postJson("https://edge.treadly.co:51235/treadly/authenticate/admin/update", authenticationUpdatePayload);
                if (postJson == null) {
                    onSuccessListener.onSuccess(false);
                } else {
                    onSuccessListener.onSuccess(CloudMessage.StatusCode.fromValue(postJson.optInt("status", 1)) == CloudMessage.StatusCode.ok);
                }
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void sendGetAuthenticationRequest(String str, long j, final OnSuccessAuthenticationState onSuccessAuthenticationState) {
        final JSONObject authenticationFetchPayload = getAuthenticationFetchPayload(str, j);
        this.webThreadPool.execute(new Runnable() { // from class: com.treadly.client.lib.sdk.Managers.CloudNetworkManager.7
            @Override // java.lang.Runnable
            public void run() {
                JSONObject postJson = RequestUtil.postJson("https://edge.treadly.co:51235/treadly/authenticate/admin/get", authenticationFetchPayload);
                boolean z = false;
                if (postJson == null) {
                    onSuccessAuthenticationState.onSuccess(false, AuthenticationState.unknown);
                    return;
                }
                AuthenticationState fromValue = AuthenticationState.fromValue(postJson.optInt("authentication_state", -1));
                if (fromValue == null) {
                    fromValue = AuthenticationState.unknown;
                }
                CloudMessage.StatusCode fromValue2 = CloudMessage.StatusCode.fromValue(postJson.optInt("status", 1));
                if (fromValue2 == CloudMessage.StatusCode.ok || fromValue2 == CloudMessage.StatusCode.authenticationError) {
                    z = true;
                }
                onSuccessAuthenticationState.onSuccess(z, fromValue);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void fetchAuthenticateReferenceCode(final ComponentInfo componentInfo, final OnSuccessAuthenticateReferenceCodeResponse onSuccessAuthenticateReferenceCodeResponse) {
        try {
            final JSONObject authenticateReferenceCodeGetPayload = getAuthenticateReferenceCodeGetPayload(componentInfo);
            this.webThreadPool.execute(new Runnable() { // from class: com.treadly.client.lib.sdk.Managers.CloudNetworkManager.8
                @Override // java.lang.Runnable
                public void run() {
                    JSONObject postJson = RequestUtil.postJson("https://edge.treadly.co:51235/treadly/authenticate/reference_code/get", authenticateReferenceCodeGetPayload);
                    AuthenticateReferenceCodeInfo authenticateReferenceCodeInfo = null;
                    if (postJson == null) {
                        onSuccessAuthenticateReferenceCodeResponse.onSuccess(false, null);
                        return;
                    }
                    boolean z = CloudMessage.StatusCode.fromValue(postJson.optInt("status", 1)) == CloudMessage.StatusCode.ok;
                    String optString = postJson.optString("reference_code", null);
                    if (z && optString != null) {
                        String optString2 = postJson.optString("title");
                        String optString3 = postJson.optString(ShareConstants.WEB_DIALOG_PARAM_MESSAGE);
                        if (optString2.equals("null")) {
                            optString2 = null;
                        }
                        if (optString3.equals("null")) {
                            optString3 = null;
                        }
                        if (optString3 != null) {
                            optString3 = optString3.replace("<REFERENCE_CODE>", optString.toUpperCase()).replace("<reference_code>", optString.toLowerCase());
                        }
                        authenticateReferenceCodeInfo = new AuthenticateReferenceCodeInfo(componentInfo.getId(), optString, optString2, optString3);
                    }
                    onSuccessAuthenticateReferenceCodeResponse.onSuccess(z, authenticateReferenceCodeInfo);
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
            onSuccessAuthenticateReferenceCodeResponse.onSuccess(false, null);
        }
    }

    protected JSONObject getSecureAuthenticateVerifyPayload(DeviceSecureAuthenticateInfo deviceSecureAuthenticateInfo) {
        try {
            JSONObject jSONObject = new JSONObject();
            jSONObject.put("client_secret", CloudMessage.clientSecret);
            JSONArray jSONArray = new JSONArray();
            for (byte b : deviceSecureAuthenticateInfo.deviceIdBytes) {
                jSONArray.put(b & 255);
            }
            jSONObject.put("device_id", jSONArray);
            JSONArray jSONArray2 = new JSONArray();
            for (byte b2 : deviceSecureAuthenticateInfo.deviceRandNum) {
                jSONArray2.put(b2 & 255);
            }
            jSONObject.put("random_number_custom_board_p", jSONArray2);
            jSONObject.put("uuid", getUuid());
            return jSONObject;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    protected JSONObject getBroadcastDeviceStatusPayload(byte[] bArr, byte[] bArr2) {
        try {
            JSONObject jSONObject = new JSONObject();
            jSONObject.put("client_secret", CloudMessage.clientSecret);
            JSONArray jSONArray = new JSONArray();
            for (byte b : bArr) {
                jSONArray.put(b & 255);
            }
            jSONObject.put("device_status", jSONArray);
            JSONArray jSONArray2 = new JSONArray();
            for (byte b2 : bArr2) {
                jSONArray2.put(b2 & 255);
            }
            jSONObject.put("random_number_custom_board_p", jSONArray2);
            jSONObject.put("uuid", getUuid());
            return jSONObject;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    protected JSONObject getGenRandomNumPayload(String str) {
        try {
            JSONObject jSONObject = new JSONObject();
            jSONObject.put("client_secret", CloudMessage.clientSecret);
            jSONObject.put("uuid", getUuid());
            return jSONObject;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    protected JSONObject getAuthenticateAdminPayload(String str) {
        try {
            JSONObject jSONObject = new JSONObject();
            jSONObject.put("client_secret", CloudMessage.clientSecret);
            jSONObject.put("uuid", str.toUpperCase());
            return jSONObject;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    protected JSONObject getAuthenticateFactoryTestPayload(String str) {
        try {
            JSONObject jSONObject = new JSONObject();
            jSONObject.put("client_secret", CloudMessage.clientSecret);
            jSONObject.put("uuid", str);
            return jSONObject;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    protected JSONObject getAuthenticateAppPayload() {
        try {
            JSONObject jSONObject = new JSONObject();
            jSONObject.put("client_secret", CloudMessage.clientSecret);
            return jSONObject;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public JSONObject getAppVersionPayload() {
        try {
            JSONObject jSONObject = new JSONObject();
            jSONObject.put("client_secret", CloudMessage.clientSecret);
            jSONObject.put("app_version", getAppVersion());
            jSONObject.put("sdk_version", getSdkVersion());
            jSONObject.put("app_type", AppType.android.value());
            return jSONObject;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    protected JSONObject getSendFactoryTestResultsPayload(FactoryTestResults[] factoryTestResultsArr) {
        if (factoryTestResultsArr == null) {
            return null;
        }
        try {
            JSONArray jSONArray = new JSONArray();
            for (FactoryTestResults factoryTestResults : factoryTestResultsArr) {
                JSONObject jSONObject = new JSONObject();
                jSONObject.put(AppMeasurement.Param.TIMESTAMP, factoryTestResults.time.getTime());
                jSONObject.put("uuid", factoryTestResults.uuid);
                jSONObject.put("app_version", factoryTestResults.appVersion);
                jSONObject.put("pass", factoryTestResults.pass);
                jSONObject.put("treadly_barcode", factoryTestResults.treadlyBarcode);
                JSONObject jSONObject2 = new JSONObject();
                jSONObject2.put("serial_number", factoryTestResults.customBoardSerialNumber);
                jSONObject2.put("mac_address", factoryTestResults.customBoardMacAddress);
                jSONObject2.put("version", factoryTestResults.customBoardVersion);
                jSONObject.put("custom_board", jSONObject2);
                JSONObject jSONObject3 = new JSONObject();
                jSONObject3.put("start", factoryTestResults.testCaseResults.start);
                jSONObject3.put("start_down", factoryTestResults.testCaseResults.startDown);
                jSONObject3.put("handrail_down", factoryTestResults.testCaseResults.handrailDown);
                jSONObject3.put("accelerate_down", factoryTestResults.testCaseResults.accelerateDown);
                jSONObject3.put("accelerate_up", factoryTestResults.testCaseResults.accelerateUp);
                jSONObject3.put("stop_up", factoryTestResults.testCaseResults.stopUp);
                jSONObject3.put("temperature", factoryTestResults.testCaseResults.temperature);
                jSONObject3.put("factory_reset", factoryTestResults.testCaseResults.factoryReset);
                jSONObject.put("test_cases", jSONObject3);
                jSONArray.put(jSONObject);
            }
            JSONObject jSONObject4 = new JSONObject();
            jSONObject4.put("client_secret", CloudMessage.clientSecret);
            jSONObject4.put("test_results", jSONArray);
            return jSONObject4;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    protected JSONObject getVerifyRegistrationStatePayload(ComponentInfo componentInfo) {
        if (componentInfo != null) {
            try {
                if (componentInfo.getVersionInfo() != null && componentInfo.getType() != null) {
                    JSONObject jSONObject = new JSONObject();
                    jSONObject.put("client_secret", CloudMessage.clientSecret);
                    jSONObject.put("major", componentInfo.getVersionInfo().getMajorVersion());
                    jSONObject.put("minor", componentInfo.getVersionInfo().getMinorVersion());
                    jSONObject.put("patch", componentInfo.getVersionInfo().getPatchVersion());
                    jSONObject.put("device_type", componentInfo.getType().value());
                    jSONObject.put("serial_number", componentInfo.getSerialNumber());
                    jSONObject.put("mac_address", componentInfo.getId());
                    return jSONObject;
                }
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }
        return null;
    }

    protected JSONObject getUpdateRegistrationStatePayload(ComponentInfo componentInfo, RegistrationState registrationState) {
        if (componentInfo != null) {
            try {
                if (componentInfo.getId() != null && registrationState != null) {
                    JSONObject jSONObject = new JSONObject();
                    jSONObject.put("client_secret", CloudMessage.clientSecret);
                    jSONObject.put("serial_number", componentInfo.getSerialNumber());
                    jSONObject.put("mac_address", componentInfo.getId());
                    jSONObject.put("registration_state", registrationState.value());
                    return jSONObject;
                }
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }
        return null;
    }

    protected JSONObject getActivationStartPayload() {
        try {
            JSONObject jSONObject = new JSONObject();
            jSONObject.put("client_secret", CloudMessage.clientSecret);
            return jSONObject;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    protected JSONObject getActivationCodeSubmitPayload(String str, ComponentInfo componentInfo) {
        if (str != null && componentInfo != null) {
            try {
                if (componentInfo.getId() != null) {
                    JSONObject jSONObject = new JSONObject();
                    jSONObject.put("client_secret", CloudMessage.clientSecret);
                    jSONObject.put("mac_address", componentInfo.getId());
                    jSONObject.put("serial_number", componentInfo.getSerialNumber());
                    jSONObject.put("activation_code", str);
                    jSONObject.put("major", componentInfo.getVersionInfo().getMajorVersion());
                    jSONObject.put("minor", componentInfo.getVersionInfo().getMinorVersion());
                    jSONObject.put("patch", componentInfo.getVersionInfo().getPatchVersion());
                    return jSONObject;
                }
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }
        return null;
    }

    protected JSONObject getActivationCodeSubmitPayload(String str) {
        if (str == null) {
            return null;
        }
        try {
            JSONObject jSONObject = new JSONObject();
            jSONObject.put("client_secret", CloudMessage.clientSecret);
            jSONObject.put("activation_code", str);
            return jSONObject;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    protected JSONObject getAuthenticationUpdatePayload(AuthenticationState authenticationState, String str, long j) {
        if (authenticationState == null) {
            return null;
        }
        try {
            JSONObject jSONObject = new JSONObject();
            jSONObject.put("client_secret", CloudMessage.clientSecret);
            jSONObject.put("uuid", getUuid());
            jSONObject.put("mac_address", str);
            jSONObject.put("authentication_state", authenticationState.value());
            jSONObject.put("serial_number", j);
            return jSONObject;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    protected JSONObject getAuthenticationFetchPayload(String str, long j) {
        if (str == null) {
            return null;
        }
        try {
            JSONObject jSONObject = new JSONObject();
            jSONObject.put("client_secret", CloudMessage.clientSecret);
            jSONObject.put("uuid", getUuid());
            jSONObject.put("mac_address", str);
            jSONObject.put("serial_number", j);
            return jSONObject;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public JSONObject getOtaUpdateCheckUpdates(ComponentInfo componentInfo) {
        try {
            JSONObject jSONObject = new JSONObject();
            jSONObject.put("client_secret", CloudMessage.clientSecret);
            jSONObject.put("major", componentInfo.getVersionInfo().getMajorVersion());
            jSONObject.put("minor", componentInfo.getVersionInfo().getMinorVersion());
            jSONObject.put("patch", componentInfo.getVersionInfo().getPatchVersion());
            jSONObject.put("device_type", componentInfo.getType().value());
            jSONObject.put("sdk_version", getSdkVersion());
            jSONObject.put("app_version", getAppVersion());
            jSONObject.put("app_type", AppType.android.value());
            return jSONObject;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public JSONObject getOtaUpdateCheckDeviceId(ComponentInfo componentInfo) {
        try {
            JSONObject jSONObject = new JSONObject();
            jSONObject.put("client_secret", CloudMessage.clientSecret);
            jSONObject.put("device_id", Message.convertMacAddressToDeviceId(componentInfo.getId()));
            jSONObject.put("mac_address", componentInfo.getId());
            return jSONObject;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public JSONObject getOtaLogPayload(ComponentInfo componentInfo, boolean z, String str, String str2) {
        if (componentInfo != null && str != null && str2 != null) {
            try {
                if (componentInfo.getId() != null) {
                    JSONObject jSONObject = new JSONObject();
                    jSONObject.put("client_secret", CloudMessage.clientSecret);
                    jSONObject.put("device_id", Message.convertMacAddressToDeviceId(componentInfo.getId()));
                    jSONObject.put("mac_address", componentInfo.getId());
                    jSONObject.put("uuid", getUuid());
                    jSONObject.put("app_version", getAppVersion());
                    jSONObject.put("sdk_version", getSdkVersion());
                    jSONObject.put("success", z);
                    jSONObject.put("version", componentInfo.getVersionInfo().getVersion());
                    jSONObject.put("target_version", str2);
                    return jSONObject;
                }
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }
        return null;
    }

    protected JSONObject getAuthenticateReferenceCodeGetPayload(ComponentInfo componentInfo) {
        try {
            JSONObject jSONObject = new JSONObject();
            jSONObject.put("client_secret", CloudMessage.clientSecret);
            jSONObject.put("mac_address", componentInfo.getId());
            return jSONObject;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private String getUuid() {
        return this.UUID;
    }

    private void setUuid(Activity activity) {
        String string;
        if (activity == null || (string = Settings.Secure.getString(activity.getContentResolver(), "android_id")) == null || string.length() < 8) {
            return;
        }
        this.UUID = string.substring(0, 8);
    }

    private void setAppVersion(String str) {
        this.appVersion = str;
    }

    public String getAppVersion() {
        return this.appVersion;
    }
}
