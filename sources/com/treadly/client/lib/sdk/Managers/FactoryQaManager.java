package com.treadly.client.lib.sdk.Managers;

import com.treadly.client.lib.sdk.Managers.CloudMessage;
import org.json.JSONObject;

/* loaded from: classes2.dex */
public class FactoryQaManager {
    public static final FactoryQaManager shared = new FactoryQaManager();

    /* loaded from: classes2.dex */
    public class FactoryQaTestApprovalInfo {
        public String appVersion;
        public int currentApprovals;
        public int maxApprovals;

        public FactoryQaTestApprovalInfo() {
        }
    }

    /* loaded from: classes2.dex */
    public enum FactoryQaTestError {
        none(0),
        error(1),
        approvalLimitReached(2),
        errorUpdatingCloud(3);
        
        private int value;

        FactoryQaTestError(int i) {
            this.value = i;
        }

        public int value() {
            return this.value;
        }

        public static FactoryQaTestError fromValue(int i) {
            FactoryQaTestError[] values;
            for (FactoryQaTestError factoryQaTestError : values()) {
                if (factoryQaTestError.value == i) {
                    return factoryQaTestError;
                }
            }
            return null;
        }

        public String description() {
            switch (this) {
                case none:
                    return "";
                case error:
                    return "Error";
                case approvalLimitReached:
                    return "Approval limit reached";
                case errorUpdatingCloud:
                    return "Error updating cloud";
                default:
                    return "";
            }
        }
    }

    private FactoryQaManager() {
    }

    public FactoryQaTestError handleFactoryQaTestApproved(String str, String str2) {
        return handleFactoryQaTestApproved(str, str2, "Factory QA Test");
    }

    public FactoryQaTestError handleFactoryQaTestApproved(String str, String str2, String str3) {
        FactoryQaTestError factoryQaTestError;
        FactoryQaTestError factoryQaTestError2 = FactoryQaTestError.none;
        FactoryQaTestApprovalInfo factoryQaTestApprovalInfo = getFactoryQaTestApprovalInfo(str);
        if (factoryQaTestApprovalInfo != null) {
            if (factoryQaTestApprovalInfo.currentApprovals < factoryQaTestApprovalInfo.maxApprovals) {
                factoryQaTestError = updateFactoryQaTestApprovalInfo(str) == CloudMessage.StatusCode.ok ? FactoryQaTestError.none : FactoryQaTestError.approvalLimitReached;
            } else {
                factoryQaTestError = FactoryQaTestError.approvalLimitReached;
            }
        } else {
            factoryQaTestError = FactoryQaTestError.error;
        }
        if (factoryQaTestError == FactoryQaTestError.none) {
            String convertMacAddressToDeviceId = Message.convertMacAddressToDeviceId(str2);
            if (convertMacAddressToDeviceId != null) {
                return !addDeviceInfoToCloud(convertMacAddressToDeviceId, str2, str3) ? FactoryQaTestError.errorUpdatingCloud : factoryQaTestError;
            }
            return FactoryQaTestError.errorUpdatingCloud;
        }
        return factoryQaTestError;
    }

    public FactoryQaTestApprovalInfo getFactoryQaTestApprovalInfo(String str) {
        JSONObject postJson;
        if (str == null || (postJson = RequestUtil.postJson("https://edge.treadly.co:51235/treadly/factory_qa/approval_limit", getFactoryQaApprovalPayload(str))) == null || CloudMessage.StatusCode.fromValue(postJson.optInt("status", 1)) != CloudMessage.StatusCode.ok) {
            return null;
        }
        FactoryQaTestApprovalInfo factoryQaTestApprovalInfo = new FactoryQaTestApprovalInfo();
        factoryQaTestApprovalInfo.appVersion = str;
        factoryQaTestApprovalInfo.currentApprovals = postJson.optInt("approval_limit_current", 0);
        factoryQaTestApprovalInfo.maxApprovals = postJson.optInt("approval_limit_max", 0);
        return factoryQaTestApprovalInfo;
    }

    public CloudMessage.StatusCode updateFactoryQaTestApprovalInfo(String str) {
        JSONObject postJson = RequestUtil.postJson("https://edge.treadly.co:51235/treadly/factory_qa/update_approval_limit", getFactoryQaApprovalPayload(str));
        if (postJson == null) {
            return null;
        }
        return CloudMessage.StatusCode.fromValue(postJson.optInt("status", 1));
    }

    public boolean addDeviceInfoToCloud(String str, String str2) {
        return addDeviceInfoToCloud(str, str2, "Factory QA Test");
    }

    public boolean addDeviceInfoToCloud(String str, String str2, String str3) {
        JSONObject postJson = RequestUtil.postJson("https://edge.treadly.co:51235/treadly/factory_qa/add_device_id", getFactoryQaAddDevicePayload(str, str2, str3));
        return postJson != null && CloudMessage.StatusCode.fromValue(postJson.optInt("status", 1)) == CloudMessage.StatusCode.ok;
    }

    private JSONObject getFactoryQaApprovalPayload(String str) {
        try {
            JSONObject jSONObject = new JSONObject();
            jSONObject.put("client_secret", CloudMessage.clientSecret);
            jSONObject.put("app_version", str);
            return jSONObject;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public JSONObject getFactoryQaAddDevicePayload(String str, String str2) {
        return getFactoryQaAddDevicePayload(str, str2, "Factory QA Test");
    }

    public JSONObject getFactoryQaAddDevicePayload(String str, String str2, String str3) {
        try {
            JSONObject jSONObject = new JSONObject();
            jSONObject.put("client_secret", CloudMessage.clientSecret);
            jSONObject.put("device_id", str.toLowerCase());
            jSONObject.put("mac_address", str2.toLowerCase());
            jSONObject.put("description", str3);
            return jSONObject;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
