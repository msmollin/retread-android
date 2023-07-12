package com.treadly.client.lib.sdk.Managers;

import android.os.Handler;
import android.os.Looper;
import android.util.SparseArray;
import com.treadly.client.lib.sdk.Listeners.DeviceUserStatsLogEventListener;
import com.treadly.client.lib.sdk.Model.DeviceUserStatsLogInfo;
import com.treadly.client.lib.sdk.Model.DeviceUserStatsLogItem;
import com.treadly.client.lib.sdk.Model.DeviceUserStatsUnclaimedLogInfo;
import com.treadly.client.lib.sdk.Model.ResponseMessage;
import com.treadly.client.lib.sdk.Model.ResponseStatus;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/* loaded from: classes2.dex */
public class DeviceUserStatsLogManager {
    private static final int expectUserIdArraySize = 12;
    private static final int expectUserIdSize = 24;
    public static final DeviceUserStatsLogManager sharedInstance = new DeviceUserStatsLogManager();
    private List<DeviceUserStatsLogEventListener> currentDeviceUserStatsLogEventListeners = new ArrayList();
    private boolean enableLogs = false;
    private String userId = "";
    SparseArray<DeviceUserStatsLogItem> logItemList = new SparseArray<>();
    private int expectedLogItemGroupCount = 0;
    SparseArray<DeviceUserStatsLogItem> unclaimedLogItemList = new SparseArray<>();
    private int expectedUnclamiedLogItemGroupCount = 0;
    private int previousUnclaimedLogitemGroupCount = 0;
    List<DeviceUserStatsUnclaimedLogInfo> unclaimedActivities = new ArrayList();

    private void addUnclaimed(DeviceUserStatsLogManager deviceUserStatsLogManager) {
        if (deviceUserStatsLogManager == null) {
        }
    }

    private void removeUnclaimedLogItem(int i) {
    }

    private DeviceUserStatsLogManager() {
    }

    public boolean setDeviceUserStatsLogs(String str, boolean z) {
        if (isValidUserId(str)) {
            if (this.userId.equals(str) || !z) {
                clearLogItem();
            }
            this.userId = str;
            this.enableLogs = z;
            return true;
        }
        return false;
    }

    public boolean getDeviceUserStatsLog(int i) {
        DeviceUserStatsLogItem logItem = getLogItem(i);
        if (logItem != null && DeviceManager.shared.sendLogUserStatsData(logItem.id)) {
            this.expectedLogItemGroupCount = logItem.groupCount;
            return true;
        }
        return false;
    }

    public boolean getDeviceUserStatsLogV2(int i) {
        DeviceUserStatsLogItem logItem = getLogItem(i);
        if (logItem != null && DeviceManager.shared.sendLogUserStatsDataV2(logItem.id)) {
            this.expectedLogItemGroupCount = logItem.groupCount;
            return true;
        }
        return false;
    }

    public boolean removeDeviceUserStatsLog(int i) {
        DeviceUserStatsLogItem logItem = getLogItem(i);
        if (logItem != null && DeviceManager.shared.sendLogUserStatsDelete(logItem.id)) {
            removeLogItem(logItem.id);
            return true;
        }
        return false;
    }

    public boolean removeDeviceUserStatsLogV2(int i) {
        DeviceUserStatsLogItem logItem = getLogItem(i);
        if (logItem != null && DeviceManager.shared.sendLogUserStatsDeleteV2(logItem.id)) {
            removeLogItem(logItem.id);
            return true;
        }
        return false;
    }

    public boolean claimDeviceUserSTatsActiveLog() {
        return DeviceManager.shared.sendClaimActiveLogUserStats();
    }

    public boolean getUnclaimedUserStatsLogInfo() {
        return DeviceManager.shared.sendGetUnclaimedLogUserStatsInfo();
    }

    public boolean claimUnclaimedUserStatsLogInfo(Date date, int i) {
        return DeviceManager.shared.sendClaimUnclaimedLogUserStatsInfo(date, i);
    }

    public boolean isEnabled() {
        return this.enableLogs;
    }

    public byte[] getUserIdArray() {
        return convertHexStringToByteArray(this.userId);
    }

    public DeviceUserStatsLogItem getLogItem(int i) {
        return this.logItemList.get(i);
    }

    public int getExpectedLogItemGroupCount() {
        return this.expectedLogItemGroupCount;
    }

    private boolean isValidUserId(String str) {
        byte[] convertHexStringToByteArray;
        return str != null && str.length() == 24 && (convertHexStringToByteArray = convertHexStringToByteArray(str)) != null && convertHexStringToByteArray.length == 12;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void addLogItem(DeviceUserStatsLogItem deviceUserStatsLogItem) {
        if (deviceUserStatsLogItem == null) {
            return;
        }
        this.logItemList.append(deviceUserStatsLogItem.id, deviceUserStatsLogItem);
    }

    private void removeLogItem(int i) {
        this.logItemList.remove(i);
    }

    private void clearLogItem() {
        this.logItemList.clear();
        this.expectedLogItemGroupCount = 0;
    }

    public void handleDeviceUserStatsReadyResponse(final ResponseMessage responseMessage) {
        runOnMain(new Runnable() { // from class: com.treadly.client.lib.sdk.Managers.DeviceUserStatsLogManager.1
            @Override // java.lang.Runnable
            public void run() {
                DeviceUserStatsLogItem parseDeviceUserStatsReadyEventMessage = Message.parseDeviceUserStatsReadyEventMessage(responseMessage.payload);
                if (parseDeviceUserStatsReadyEventMessage != null) {
                    DeviceUserStatsLogManager.this.addLogItem(parseDeviceUserStatsReadyEventMessage);
                    DeviceUserStatsLogManager.this.sendOnDeviceUserStatsLogReadyEvent(parseDeviceUserStatsReadyEventMessage.id);
                }
            }
        });
    }

    public void handleDeviceUserStatsDataResponse(final ResponseMessage responseMessage) {
        runOnMain(new Runnable() { // from class: com.treadly.client.lib.sdk.Managers.DeviceUserStatsLogManager.2
            @Override // java.lang.Runnable
            public void run() {
                PacketizedMessage read;
                DeviceUserStatsLogInfo parseDeviceUserStatsDataEventMessage;
                Packetizer.shared.write(responseMessage);
                if (!Packetizer.shared.isReady() || (read = Packetizer.shared.read()) == null || (parseDeviceUserStatsDataEventMessage = Message.parseDeviceUserStatsDataEventMessage(read.getPayload())) == null) {
                    return;
                }
                DeviceUserStatsLogManager.this.sendOnDeviceUserStatsLogDataEvent(parseDeviceUserStatsDataEventMessage);
            }
        });
    }

    public void handleDeviceUserStatsReadyV2Response(final ResponseMessage responseMessage) {
        runOnMain(new Runnable() { // from class: com.treadly.client.lib.sdk.Managers.DeviceUserStatsLogManager.3
            @Override // java.lang.Runnable
            public void run() {
                DeviceUserStatsLogItem parseDeviceUserStatsReadyEventMessage = Message.parseDeviceUserStatsReadyEventMessage(responseMessage.payload);
                if (parseDeviceUserStatsReadyEventMessage != null) {
                    DeviceUserStatsLogManager.this.addLogItem(parseDeviceUserStatsReadyEventMessage);
                    DeviceUserStatsLogManager.this.sendOnDeviceUserStatsLogReadyV2Event(parseDeviceUserStatsReadyEventMessage.id);
                }
            }
        });
    }

    public void handleDeviceUserStatsDataV2Response(final ResponseMessage responseMessage) {
        runOnMain(new Runnable() { // from class: com.treadly.client.lib.sdk.Managers.DeviceUserStatsLogManager.4
            @Override // java.lang.Runnable
            public void run() {
                PacketizedMessage read;
                Packetizer.shared.write(responseMessage);
                if (!Packetizer.shared.isReady() || (read = Packetizer.shared.read()) == null) {
                    return;
                }
                DeviceUserStatsLogManager.this.sendOnDeviceUserStatsLogDataV2Event(read.getPayload());
            }
        });
    }

    public void handleDeviceUserStatsUnclaimedActiveLogResponse(ResponseMessage responseMessage) {
        runOnMain(new Runnable() { // from class: com.treadly.client.lib.sdk.Managers.DeviceUserStatsLogManager.5
            @Override // java.lang.Runnable
            public void run() {
                DeviceUserStatsLogManager.this.sendOnDeviceUserStatsUnclaimedActiveLogEvent();
            }
        });
    }

    public void handleGetUnclaimedUserStatsLogInfoResponse(final ResponseMessage responseMessage) {
        runOnMain(new Runnable() { // from class: com.treadly.client.lib.sdk.Managers.DeviceUserStatsLogManager.6
            @Override // java.lang.Runnable
            public void run() {
                PacketizedMessage read;
                DeviceUserStatsUnclaimedLogInfo parseDeviceUnclaimedUserStatsLogInfoMessage;
                if (responseMessage.status == 0) {
                    Packetizer.shared.write(responseMessage);
                    if (!Packetizer.shared.isReady() || (read = Packetizer.shared.read()) == null || (parseDeviceUnclaimedUserStatsLogInfoMessage = Message.parseDeviceUnclaimedUserStatsLogInfoMessage(read.getPayload())) == null) {
                        return;
                    }
                    DeviceUserStatsLogManager.this.processUnclaimedUserStatsLogInfoEvent(parseDeviceUnclaimedUserStatsLogInfoMessage);
                } else if (responseMessage.status == 33) {
                    DeviceUserStatsLogManager.this.sendOnDeviceUserStatsUnclaimedUserStatsInfo(new DeviceUserStatsUnclaimedLogInfo[0]);
                } else {
                    byte b = responseMessage.status;
                }
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void processUnclaimedUserStatsLogInfoEvent(DeviceUserStatsUnclaimedLogInfo deviceUserStatsUnclaimedLogInfo) {
        DeviceUserStatsUnclaimedLogInfo lastDeviceUserStatsUnclaimedLogInfo = getLastDeviceUserStatsUnclaimedLogInfo();
        if (lastDeviceUserStatsUnclaimedLogInfo != null) {
            if (lastDeviceUserStatsUnclaimedLogInfo.totalSequence != deviceUserStatsUnclaimedLogInfo.totalSequence || deviceUserStatsUnclaimedLogInfo.sequence != lastDeviceUserStatsUnclaimedLogInfo.sequence + 1) {
                this.unclaimedActivities.clear();
            }
        } else {
            this.unclaimedActivities.clear();
        }
        this.unclaimedActivities.add(deviceUserStatsUnclaimedLogInfo);
        DeviceUserStatsUnclaimedLogInfo lastDeviceUserStatsUnclaimedLogInfo2 = getLastDeviceUserStatsUnclaimedLogInfo();
        if (lastDeviceUserStatsUnclaimedLogInfo2 == null || lastDeviceUserStatsUnclaimedLogInfo2.sequence != lastDeviceUserStatsUnclaimedLogInfo2.totalSequence) {
            return;
        }
        final DeviceUserStatsUnclaimedLogInfo[] deviceUserStatsUnclaimedLogInfoArr = new DeviceUserStatsUnclaimedLogInfo[this.unclaimedActivities.size()];
        this.unclaimedActivities.toArray(deviceUserStatsUnclaimedLogInfoArr);
        runOnMain(new Runnable() { // from class: com.treadly.client.lib.sdk.Managers.DeviceUserStatsLogManager.7
            @Override // java.lang.Runnable
            public void run() {
                DeviceUserStatsLogManager.this.sendOnDeviceUserStatsUnclaimedUserStatsInfo(deviceUserStatsUnclaimedLogInfoArr);
            }
        });
    }

    private DeviceUserStatsUnclaimedLogInfo getLastDeviceUserStatsUnclaimedLogInfo() {
        if (this.unclaimedActivities == null || this.unclaimedActivities.size() == 0) {
            return null;
        }
        return this.unclaimedActivities.get(this.unclaimedActivities.size() - 1);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void handleClaimUnclaimedUserStatsInfo(ResponseMessage responseMessage) {
        final boolean z = responseMessage.getStatus() == ResponseStatus.NO_ERROR.value();
        runOnMain(new Runnable() { // from class: com.treadly.client.lib.sdk.Managers.DeviceUserStatsLogManager.8
            @Override // java.lang.Runnable
            public void run() {
                DeviceUserStatsLogManager.this.sendOnDeviceUserStatsClaimUnclaimedUserStatsInfo(z);
            }
        });
    }

    public void addEventListener(DeviceUserStatsLogEventListener deviceUserStatsLogEventListener) {
        boolean z = false;
        for (DeviceUserStatsLogEventListener deviceUserStatsLogEventListener2 : this.currentDeviceUserStatsLogEventListeners) {
            if (deviceUserStatsLogEventListener2 == deviceUserStatsLogEventListener) {
                z = true;
            }
        }
        if (z) {
            return;
        }
        this.currentDeviceUserStatsLogEventListeners.add(deviceUserStatsLogEventListener);
    }

    public void removeEventListener(DeviceUserStatsLogEventListener deviceUserStatsLogEventListener) {
        this.currentDeviceUserStatsLogEventListeners.remove(deviceUserStatsLogEventListener);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void sendOnDeviceUserStatsLogReadyEvent(int i) {
        for (DeviceUserStatsLogEventListener deviceUserStatsLogEventListener : this.currentDeviceUserStatsLogEventListeners) {
            deviceUserStatsLogEventListener.onDeviceUserStatsLogReady(i);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void sendOnDeviceUserStatsLogReadyV2Event(int i) {
        for (DeviceUserStatsLogEventListener deviceUserStatsLogEventListener : this.currentDeviceUserStatsLogEventListeners) {
            deviceUserStatsLogEventListener.onDeviceUserStatsLogReadyV2(i);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void sendOnDeviceUserStatsLogDataEvent(DeviceUserStatsLogInfo deviceUserStatsLogInfo) {
        for (DeviceUserStatsLogEventListener deviceUserStatsLogEventListener : this.currentDeviceUserStatsLogEventListeners) {
            deviceUserStatsLogEventListener.onDeviceUserStatsLogData(deviceUserStatsLogInfo);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void sendOnDeviceUserStatsLogDataV2Event(byte[] bArr) {
        for (DeviceUserStatsLogEventListener deviceUserStatsLogEventListener : this.currentDeviceUserStatsLogEventListeners) {
            deviceUserStatsLogEventListener.onDeviceUserStatsLogDataV2(bArr);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void sendOnDeviceUserStatsUnclaimedActiveLogEvent() {
        for (DeviceUserStatsLogEventListener deviceUserStatsLogEventListener : this.currentDeviceUserStatsLogEventListeners) {
            deviceUserStatsLogEventListener.onDeviceUserStatsUnclaimedActiveLog();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void sendOnDeviceUserStatsUnclaimedUserStatsInfo(DeviceUserStatsUnclaimedLogInfo[] deviceUserStatsUnclaimedLogInfoArr) {
        for (DeviceUserStatsLogEventListener deviceUserStatsLogEventListener : this.currentDeviceUserStatsLogEventListeners) {
            deviceUserStatsLogEventListener.onDeviceUserStatsUnclaimedUserStatsInfo(deviceUserStatsUnclaimedLogInfoArr);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void sendOnDeviceUserStatsClaimUnclaimedUserStatsInfo(boolean z) {
        for (DeviceUserStatsLogEventListener deviceUserStatsLogEventListener : this.currentDeviceUserStatsLogEventListeners) {
            deviceUserStatsLogEventListener.onDeviceUserStatsClaimUnclaimedUserStatsInfoResponse(z);
        }
    }

    private static byte[] convertHexStringToByteArray(String str) {
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

    private static void runOnMain(Runnable runnable) {
        new Handler(Looper.getMainLooper()).post(runnable);
    }
}
