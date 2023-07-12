package com.treadly.client.lib.sdk.Managers;

import android.os.Handler;
import android.os.Looper;
import com.treadly.client.lib.sdk.Listeners.OtaUpdateRequestEventListener;
import com.treadly.client.lib.sdk.Managers.CloudMessage;
import com.treadly.client.lib.sdk.Model.ComponentInfo;
import com.treadly.client.lib.sdk.Model.ComponentVersionInfo;
import com.treadly.client.lib.sdk.Model.DeviceInfo;
import com.treadly.client.lib.sdk.Model.FirmwareVersion;
import com.treadly.client.lib.sdk.Model.OtaUpdateInfo;
import com.treadly.client.lib.sdk.Model.OtaUpdateStatus;
import com.treadly.client.lib.sdk.Model.ResponseMessage;
import com.treadly.client.lib.sdk.Model.VersionInfo;
import com.treadly.client.lib.sdk.Model.WifiApInfo;
import com.treadly.client.lib.sdk.Model.WifiStatus;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import org.json.JSONArray;
import org.json.JSONObject;

/* loaded from: classes2.dex */
public class OtaUpdateManager {
    private static final int WEB_REQUEST_THREAD_POOL_THREADS = 16;
    private static final int restartDelay = 180000;
    public static final OtaUpdateManager shared = new OtaUpdateManager();
    private Timer restartTimer;
    private List<OtaUpdateRequestEventListener> currentOtaUpdateListeners = new ArrayList();
    private String updateUrl = null;
    private String updateVersion = null;
    private String updateClientSecret = null;
    private ComponentInfo updateComponentInfo = null;
    private boolean expectingRestart = false;
    private boolean expectingOtaModeRestart = false;
    private ExecutorService webThreadPool = Executors.newFixedThreadPool(16);

    private String getSdkVersion() {
        return "1.1.8";
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes2.dex */
    public enum OtaUpdateLogReason {
        none(0),
        otaUpdateError(1),
        reconnectTimeout(2),
        deviceIdNotFound(3);
        
        private OtaUpdateStatus updateStatus = OtaUpdateStatus.unknown;
        private int value;

        OtaUpdateLogReason(int i) {
            this.value = i;
        }

        public int value() {
            return this.value;
        }

        public static OtaUpdateLogReason fromValue(int i) {
            OtaUpdateLogReason[] values;
            for (OtaUpdateLogReason otaUpdateLogReason : values()) {
                if (otaUpdateLogReason.value == i) {
                    return otaUpdateLogReason;
                }
            }
            return null;
        }

        public void setUpdateStatus(OtaUpdateStatus otaUpdateStatus) {
            this.updateStatus = otaUpdateStatus;
        }

        public String description() {
            switch (this) {
                case none:
                    return "";
                case otaUpdateError:
                    StringBuilder sb = new StringBuilder();
                    sb.append("Ota update error");
                    sb.append(this.updateStatus != null ? String.format(Locale.getDefault(), "%d", Integer.valueOf(this.updateStatus.value())) : "");
                    return sb.toString();
                case reconnectTimeout:
                    return "Reconnect timeout";
                case deviceIdNotFound:
                    return "Device ID not found";
                default:
                    return "";
            }
        }
    }

    private OtaUpdateManager() {
    }

    public void addOtaUpdateEventListener(OtaUpdateRequestEventListener otaUpdateRequestEventListener) {
        boolean z = false;
        for (OtaUpdateRequestEventListener otaUpdateRequestEventListener2 : this.currentOtaUpdateListeners) {
            if (otaUpdateRequestEventListener2 == otaUpdateRequestEventListener) {
                z = true;
            }
        }
        if (z) {
            return;
        }
        this.currentOtaUpdateListeners.add(otaUpdateRequestEventListener);
    }

    public void removeOtaUpdateEventlistener(OtaUpdateRequestEventListener otaUpdateRequestEventListener) {
        this.currentOtaUpdateListeners.remove(otaUpdateRequestEventListener);
    }

    protected void sendOtaUpdateFirmwareVersionAvailableResponse(boolean z, FirmwareVersion firmwareVersion, String[] strArr, boolean z2) {
        for (OtaUpdateRequestEventListener otaUpdateRequestEventListener : this.currentOtaUpdateListeners) {
            otaUpdateRequestEventListener.onOtaUpdateFirmwareVersionAvailable(z, firmwareVersion, strArr, z2);
        }
    }

    protected void sendOtaUpdateWiFiApInfoResponse(WifiApInfo wifiApInfo) {
        for (OtaUpdateRequestEventListener otaUpdateRequestEventListener : this.currentOtaUpdateListeners) {
            otaUpdateRequestEventListener.onOtaUpdateWiFiApResponse(wifiApInfo);
        }
    }

    protected void sendOtaUpdateWifiScanResponse(WifiStatus wifiStatus) {
        for (OtaUpdateRequestEventListener otaUpdateRequestEventListener : this.currentOtaUpdateListeners) {
            otaUpdateRequestEventListener.onOtaUpdateWifiScanResponse(wifiStatus);
        }
    }

    protected void sendOtaUpdateResponse(OtaUpdateStatus otaUpdateStatus) {
        for (OtaUpdateRequestEventListener otaUpdateRequestEventListener : this.currentOtaUpdateListeners) {
            otaUpdateRequestEventListener.onOtaUpdateResponse(otaUpdateStatus);
        }
    }

    protected void sendOtaupdateSetModeResponse(boolean z) {
        for (OtaUpdateRequestEventListener otaUpdateRequestEventListener : this.currentOtaUpdateListeners) {
            otaUpdateRequestEventListener.onOtaUpdateSetMode(z);
        }
    }

    protected void sendOtaInProgressResponse(int i) {
        for (OtaUpdateRequestEventListener otaUpdateRequestEventListener : this.currentOtaUpdateListeners) {
            otaUpdateRequestEventListener.onOtaInProgressResponse(i);
        }
    }

    protected boolean checkDeviceId(ComponentInfo componentInfo) {
        JSONObject postJson;
        return DeviceManager.shared.isConnected() && (postJson = RequestUtil.postJson("https://edge.treadly.co:51235/treadly/ota/check_device_id", CloudNetworkManager.shared.getOtaUpdateCheckDeviceId(componentInfo))) != null && CloudMessage.StatusCode.fromValue(postJson.optInt("status", 1)) == CloudMessage.StatusCode.ok;
    }

    public boolean checkAvailableUpdates(final ComponentInfo componentInfo, boolean z) {
        if (DeviceManager.shared.isConnected() && componentInfo != null) {
            final String str = z ? "https://edge-eng.treadly.co:51235/treadly/ota/qa/check_version" : "https://edge.treadly.co:51235/treadly/ota/check_version";
            final boolean z2 = z || checkDeviceId(componentInfo);
            final JSONObject otaUpdateCheckUpdates = CloudNetworkManager.shared.getOtaUpdateCheckUpdates(componentInfo);
            this.updateUrl = null;
            this.updateVersion = null;
            this.updateComponentInfo = componentInfo;
            this.webThreadPool.execute(new Runnable() { // from class: com.treadly.client.lib.sdk.Managers.OtaUpdateManager.1
                @Override // java.lang.Runnable
                public void run() {
                    final FirmwareVersion firmwareVersion;
                    final String[] strArr;
                    final boolean z3;
                    final boolean z4;
                    String[] strArr2;
                    String[] split;
                    JSONObject postJson = RequestUtil.postJson(str, otaUpdateCheckUpdates);
                    if (postJson == null) {
                        OtaUpdateManager.runOnMain(new Runnable() { // from class: com.treadly.client.lib.sdk.Managers.OtaUpdateManager.1.1
                            @Override // java.lang.Runnable
                            public void run() {
                                OtaUpdateManager.this.sendOtaUpdateFirmwareVersionAvailableResponse(false, null, null, false);
                            }
                        });
                        return;
                    }
                    if (CloudMessage.StatusCode.fromValue(postJson.optInt("status", 1)) == CloudMessage.StatusCode.ok) {
                        z3 = postJson.optBoolean("update_available");
                        if (z3) {
                            int optInt = postJson.optInt("major", 0);
                            int optInt2 = postJson.optInt("minor", 0);
                            int optInt3 = postJson.optInt("patch", 0);
                            JSONArray optJSONArray = postJson.optJSONArray("update_descriptions");
                            if (optJSONArray != null) {
                                strArr2 = new String[optJSONArray.length()];
                                for (int i = 0; i < optJSONArray.length(); i++) {
                                    strArr2[i] = optJSONArray.optString(i);
                                }
                            } else {
                                strArr2 = null;
                            }
                            FirmwareVersion firmwareVersion2 = new FirmwareVersion(optInt, optInt2, optInt3);
                            String optString = postJson.optString("minimum_firmware_version");
                            boolean isGreaterThan = (optString == null || (split = optString.split("\\.")) == null || split.length != 3) ? false : new VersionInfo(Integer.parseInt(split[0]), Integer.parseInt(split[1]), Integer.parseInt(split[2])).isGreaterThan(componentInfo.getVersionInfo().getVersionInfo());
                            OtaUpdateManager.this.updateVersion = firmwareVersion2.version;
                            if (z2) {
                                OtaUpdateManager.this.updateUrl = postJson.optString("update_url");
                                OtaUpdateManager.this.updateClientSecret = postJson.optString("client_secret");
                                z4 = isGreaterThan;
                                firmwareVersion = firmwareVersion2;
                                strArr = strArr2;
                            } else {
                                OtaUpdateManager.this.sendOtaUpdateLog(false, OtaUpdateLogReason.deviceIdNotFound.description());
                                OtaUpdateManager.this.updateVersion = null;
                                OtaUpdateManager.runOnMain(new Runnable() { // from class: com.treadly.client.lib.sdk.Managers.OtaUpdateManager.1.2
                                    @Override // java.lang.Runnable
                                    public void run() {
                                        OtaUpdateManager.this.sendOtaUpdateFirmwareVersionAvailableResponse(false, null, null, false);
                                    }
                                });
                                return;
                            }
                        } else {
                            firmwareVersion = null;
                            strArr = null;
                            z4 = false;
                        }
                    } else {
                        firmwareVersion = null;
                        strArr = null;
                        z3 = false;
                        z4 = false;
                    }
                    OtaUpdateManager.runOnMain(new Runnable() { // from class: com.treadly.client.lib.sdk.Managers.OtaUpdateManager.1.3
                        @Override // java.lang.Runnable
                        public void run() {
                            OtaUpdateManager.this.sendOtaUpdateFirmwareVersionAvailableResponse(z3, firmwareVersion, strArr, z4);
                        }
                    });
                }
            });
            return true;
        }
        return false;
    }

    public boolean otaUpdate(WifiApInfo wifiApInfo) {
        String str;
        String str2;
        if (!DeviceManager.shared.isConnected() || (str = this.updateUrl) == null || (str2 = this.updateClientSecret) == null || wifiApInfo == null || wifiApInfo.ssid == null || wifiApInfo.ssid.isEmpty()) {
            return false;
        }
        for (byte[] bArr : Packetizer.shared.packetize(Message.getOtaUpdatePacketizeRequest(new OtaUpdateInfo(wifiApInfo.ssid, wifiApInfo.password, str, str2, wifiApInfo.persistent)), true)) {
            DeviceManager.shared.sendRequest(bArr);
        }
        return true;
    }

    public boolean otaConfigSettings(WifiApInfo wifiApInfo) {
        if (!DeviceManager.shared.isConnected() || wifiApInfo == null || wifiApInfo.ssid == null || wifiApInfo.ssid.isEmpty()) {
            return false;
        }
        for (byte[] bArr : Packetizer.shared.packetize(Message.getOtaConfigSettingsPacketizeRequest(new OtaUpdateInfo(wifiApInfo.ssid, wifiApInfo.password, "", "", false)), true)) {
            DeviceManager.shared.sendRequest(bArr);
        }
        return true;
    }

    public boolean getOtaConfigSettings() {
        if (DeviceManager.shared.isConnected()) {
            DeviceManager.shared.sendRequest(Message.getOtaConfigSettings());
            return true;
        }
        return false;
    }

    public boolean clearOtaConfigSettings() {
        if (DeviceManager.shared.isConnected()) {
            DeviceManager.shared.sendRequest(Message.clearOtaConfigSettings());
            return true;
        }
        return false;
    }

    public boolean startWifiApScan() {
        if (DeviceManager.shared.isConnected()) {
            DeviceManager.shared.sendRequest(Message.getStartWiFiApScanRequest());
            return true;
        }
        return false;
    }

    public void sendSetMode() {
        this.expectingRestart = true;
        this.expectingOtaModeRestart = true;
        startRestartTimer();
        DeviceManager.shared.sendRequest(Message.getOtaUpdateSetMode());
    }

    protected void handleStartWifiScanResponse(ResponseMessage responseMessage) {
        final WifiStatus fromValue = WifiStatus.fromValue(responseMessage.status & 255);
        runOnMain(new Runnable() { // from class: com.treadly.client.lib.sdk.Managers.OtaUpdateManager.2
            @Override // java.lang.Runnable
            public void run() {
                OtaUpdateManager.this.sendOtaUpdateWifiScanResponse(fromValue != null ? fromValue : WifiStatus.unknown);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void handleWifiApResponse(final ResponseMessage responseMessage) {
        runOnMain(new Runnable() { // from class: com.treadly.client.lib.sdk.Managers.OtaUpdateManager.3
            @Override // java.lang.Runnable
            public void run() {
                PacketizedMessage read;
                Packetizer.shared.write(responseMessage);
                if (!Packetizer.shared.isReady() || (read = Packetizer.shared.read()) == null) {
                    return;
                }
                OtaUpdateManager.this.sendOtaUpdateWiFiApInfoResponse(Message.parseWifiApInfo(read.getPayload()));
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void handleOtaUpdateResponse(final ResponseMessage responseMessage) {
        DeviceManager.shared.messageQueue.post(new Runnable() { // from class: com.treadly.client.lib.sdk.Managers.OtaUpdateManager.4
            @Override // java.lang.Runnable
            public void run() {
                OtaUpdateStatus fromValue = OtaUpdateStatus.fromValue(responseMessage.status);
                if (fromValue == null) {
                    fromValue = OtaUpdateStatus.unknown;
                }
                if (fromValue == OtaUpdateStatus.noError) {
                    OtaUpdateManager.this.expectingRestart = true;
                    OtaUpdateManager.this.startRestartTimer();
                    DeviceInfo currentDeviceInfo = DeviceManager.shared.currentDeviceInfo();
                    if (currentDeviceInfo != null) {
                        String name = currentDeviceInfo.getName();
                        ComponentInfo bleComponent = DeviceManager.shared.getBleComponent();
                        if (name != null && bleComponent != null && bleComponent.getVersionInfo() != null) {
                            ComponentVersionInfo versionInfo = bleComponent.getVersionInfo();
                            if (versionInfo.getVersion() != null) {
                                SharedPreferences.shared.storeOtaPreUpdateVersion(name, versionInfo.getVersionInfo());
                            }
                        }
                    }
                } else {
                    OtaUpdateLogReason otaUpdateLogReason = OtaUpdateLogReason.otaUpdateError;
                    otaUpdateLogReason.setUpdateStatus(fromValue);
                    OtaUpdateManager.this.sendOtaUpdateLog(false, otaUpdateLogReason.description());
                }
                OtaUpdateManager.this.sendOtaUpdateResponse(fromValue);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void handleOtaInProgressResponse(final ResponseMessage responseMessage) {
        runOnMain(new Runnable() { // from class: com.treadly.client.lib.sdk.Managers.OtaUpdateManager.5
            @Override // java.lang.Runnable
            public void run() {
                OtaUpdateManager.this.sendOtaInProgressResponse(Message.parseOtaUpdateProgress(responseMessage.payload));
            }
        });
    }

    protected void checkVersion() {
        String name;
        VersionInfo otaPreUpdateVersion;
        ComponentInfo bleComponent;
        DeviceInfo currentDeviceInfo = DeviceManager.shared.currentDeviceInfo();
        if (currentDeviceInfo == null || (name = currentDeviceInfo.getName()) == null || (otaPreUpdateVersion = SharedPreferences.shared.getOtaPreUpdateVersion(name)) == null || (bleComponent = DeviceManager.shared.getBleComponent()) == null || bleComponent.getVersionInfo() == null) {
            return;
        }
        OtaUpdateStatus otaUpdateStatus = bleComponent.getVersionInfo().isGreaterThan(otaPreUpdateVersion) ? OtaUpdateStatus.versionSuccess : OtaUpdateStatus.versionError;
        sendOtaUpdateResponse(otaUpdateStatus);
        SharedPreferences.shared.storeOtaPreUpdateVersion(name, null);
        boolean z = otaUpdateStatus == OtaUpdateStatus.versionSuccess;
        OtaUpdateLogReason otaUpdateLogReason = OtaUpdateLogReason.otaUpdateError;
        otaUpdateLogReason.setUpdateStatus(otaUpdateStatus);
        sendOtaUpdateLog(z, otaUpdateLogReason.description());
    }

    protected void sendOtaUpdateLog(boolean z, String str) {
        ComponentInfo componentInfo = this.updateComponentInfo;
        if (componentInfo == null) {
            return;
        }
        JSONObject postJson = RequestUtil.postJson("https://edge.treadly.co:51235/treadly/ota/log", CloudNetworkManager.shared.getOtaLogPayload(componentInfo, z, str, this.updateVersion != null ? this.updateVersion : ""));
        if (postJson == null) {
            System.out.println("Error sending OTA update log");
            return;
        }
        CloudMessage.StatusCode fromValue = CloudMessage.StatusCode.fromValue(postJson.optInt("status", 1));
        PrintStream printStream = System.out;
        printStream.println("Send OTA update log status code " + fromValue + " " + fromValue.value());
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public boolean isRestartExpected() {
        return this.expectingRestart;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public boolean handleExpectedConnect() {
        boolean z = this.expectingRestart;
        if (this.expectingOtaModeRestart) {
            sendOtaupdateSetModeResponse(true);
        } else {
            checkVersion();
        }
        this.expectingRestart = false;
        this.expectingOtaModeRestart = false;
        if (this.restartTimer != null) {
            this.restartTimer.cancel();
        }
        return z;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public boolean handleExpectedDisconnect() {
        DeviceInfo currentDeviceInfo;
        if (this.expectingRestart && (currentDeviceInfo = DeviceManager.shared.currentDeviceInfo()) != null) {
            BleConnectionManager.shared.initReconnect(currentDeviceInfo, restartDelay);
            return true;
        }
        return false;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void startRestartTimer() {
        stopRestartTimer();
        this.restartTimer = new Timer();
        this.restartTimer.schedule(new TimerTask() { // from class: com.treadly.client.lib.sdk.Managers.OtaUpdateManager.6
            @Override // java.util.TimerTask, java.lang.Runnable
            public void run() {
                OtaUpdateManager.this.restartTimeout();
            }
        }, 180000L);
    }

    private void stopRestartTimer() {
        if (this.restartTimer != null) {
            this.restartTimer.cancel();
        }
        this.restartTimer = null;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void restartTimeout() {
        this.expectingRestart = false;
        sendOtaUpdateResponse(OtaUpdateStatus.reconnectError);
        sendOtaUpdateLog(false, OtaUpdateLogReason.reconnectTimeout.description());
    }

    static void runOnMain(Runnable runnable) {
        new Handler(Looper.getMainLooper()).post(runnable);
    }
}
