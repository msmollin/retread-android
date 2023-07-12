package com.treadly.client.lib.sdk.Managers;

import com.treadly.client.lib.sdk.Managers.CloudMessage;
import com.treadly.client.lib.sdk.Model.VersionInfo;
import org.json.JSONObject;

/* loaded from: classes2.dex */
public class AppManager {
    public static final AppManager shared = new AppManager();

    private AppManager() {
    }

    public VersionInfo getLatestAppVersion() {
        JSONObject postJson = RequestUtil.postJson("https://edge.treadly.co:51235/treadly/apps/version", CloudNetworkManager.shared.getAppVersionPayload());
        if (postJson == null || CloudMessage.StatusCode.fromValue(postJson.optInt("status", 1)) != CloudMessage.StatusCode.ok) {
            return null;
        }
        int optInt = postJson.optInt("major", 0);
        int optInt2 = postJson.optInt("minor", 0);
        int optInt3 = postJson.optInt("patch", 0);
        boolean optBoolean = postJson.optBoolean("mandatory", false);
        VersionInfo versionInfo = new VersionInfo();
        versionInfo.versionMajor = optInt;
        versionInfo.versionMinor = optInt2;
        versionInfo.versionPatch = optInt3;
        versionInfo.isMandatory = optBoolean;
        return versionInfo;
    }
}
