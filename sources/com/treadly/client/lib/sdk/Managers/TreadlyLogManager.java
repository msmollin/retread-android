package com.treadly.client.lib.sdk.Managers;

import android.app.Activity;
import android.content.pm.PackageManager;
import android.os.Handler;
import android.os.Looper;
import android.provider.Settings;
import com.treadly.client.lib.sdk.Model.ComponentInfo;
import com.treadly.client.lib.sdk.Model.DeviceDebugLogEvent;
import com.treadly.client.lib.sdk.Model.DeviceDebugLogInfo;
import com.treadly.client.lib.sdk.Model.DeviceIrDebugEventInfo;
import com.treadly.client.lib.sdk.Model.DeviceIrDebugLogInfo;
import com.treadly.client.lib.sdk.Model.DeviceLogInfo;
import com.treadly.client.lib.sdk.Model.DeviceStatus;
import com.treadly.client.lib.sdk.Model.FactoryTestResults;
import com.treadly.client.lib.sdk.Model.ResponseMessage;
import com.treadly.client.lib.sdk.Model.TreadlyDeviceLogInfo;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.PrintStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import org.eclipse.paho.client.mqttv3.MqttTopic;
import org.joda.time.DateTimeConstants;
import org.json.JSONObject;

/* loaded from: classes2.dex */
public class TreadlyLogManager {
    private static final int WEB_REQUEST_THREAD_POOL_THREADS = 16;
    private static final String deviceIrLogFileNameFormat = "Treadly_Device_IR_Log_%s_%s.txt";
    private static final String deviceLogFileNameFormat = "Treadly_Device_Log_%s_%s.txt";
    private static final String fileNameFormat = "Treadly_log_%s_%s.csv";
    public static final TreadlyLogManager shared = new TreadlyLogManager();
    private String appVersion;
    private String csvText;
    private TreadlyDeviceLogInfo currentDeviceLogInformation;
    private String currentDeviceName;
    private String currentMacAddress;
    private String currentSerialNumber;
    private File fileDirectory;
    private File logPath;
    private String uuid;
    private boolean managerEnabled = false;
    public boolean isLogging = false;
    private List<DeviceIrDebugEventInfo> deviceIrDebugEvents = new ArrayList();
    private ExecutorService webThreadPool = Executors.newFixedThreadPool(16);

    /* loaded from: classes2.dex */
    public interface TreadlyLogManagerSendFileListener {
        void onSuccess(boolean z);
    }

    public void init(Activity activity) {
        setUuid(activity);
        if (activity != null) {
            try {
                this.fileDirectory = activity.getFilesDir();
                PrintStream printStream = System.out;
                printStream.println("FILEDIRECTORY: " + this.fileDirectory);
                setAppVersion(activity.getPackageManager().getPackageInfo(activity.getPackageName(), 0).versionName);
            } catch (PackageManager.NameNotFoundException e) {
                e.printStackTrace();
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void setManagerEnabled(boolean z) {
        this.managerEnabled = z;
    }

    public void startLogging(String str, String str2, String str3, String str4, String str5, String str6) {
        if (this.managerEnabled) {
            if (this.isLogging) {
                finishLogging();
            }
            this.currentDeviceName = str;
            this.currentSerialNumber = str2;
            this.currentMacAddress = str3;
            this.currentDeviceLogInformation = new TreadlyDeviceLogInfo(str2, str3, str4, str5, str6);
            String format = String.format(fileNameFormat, str2, getTimeStamp());
            File file = new File(getDocumentsDirectory() + "/logs/" + str2 + MqttTopic.TOPIC_LEVEL_SEPARATOR);
            if (!file.exists()) {
                if (!file.mkdirs()) {
                    return;
                }
                this.logPath = new File(file, format);
            } else {
                this.logPath = new File(file, format);
            }
            this.csvText = "TimeStamp,DeviceName,Steps,Distance,Units,Time,Seconds,Speed,MaxSpeed,MinSpeed,StatusCode,TotalDistance,TotalSteps,Temp,Current,TempStatus,TempValid,MaintenanceSteps,MaintenanceStepsThreshold,MaintenanceFlag,BleOffSource,DeviceStatusPayload,DeviceStatusExtPayload,DeviceStatusDiagnosticPayload,DeviceStatusExt2Payload,IotStatus\n";
            this.isLogging = true;
        }
    }

    public void appendToLog(DeviceStatus deviceStatus) {
        if (this.isLogging) {
            String bytesToHex = Message.bytesToHex(deviceStatus.getDeviceStatusPayload());
            String bytesToHex2 = Message.bytesToHex(deviceStatus.getDeviceStatusExtPayload());
            String bytesToHex3 = Message.bytesToHex(deviceStatus.getDeviceStatusDiagnosticPayload());
            String bytesToHex4 = Message.bytesToHex(deviceStatus.getDeviceStatusExt2Payload());
            Message.bytesToHex(deviceStatus.getDeviceStatusIotPayload());
            this.csvText += (getTimeStamp() + "," + this.currentDeviceName + "," + deviceStatus.getSteps() + "," + deviceStatus.getDistance() + "," + deviceStatus.getDistanceUnits() + "," + convertSecondsToTimeString(deviceStatus.getSeconds()) + "," + deviceStatus.getSeconds() + "," + deviceStatus.getSpeedInfo().getTargetSpeed() + "," + deviceStatus.getSpeedInfo().getMaximumSpeed() + "," + deviceStatus.getSpeedInfo().getMinimumSpeed() + "," + deviceStatus.getStatusCode() + "," + deviceStatus.getTotalDistance() + "," + deviceStatus.getTotalSteps() + "," + deviceStatus.getTemperatureInfo().getTemperature() + "," + deviceStatus.getCurrent() + "," + deviceStatus.getTemperatureInfo().getStatusCode() + "," + deviceStatus.getTemperatureInfo().isValid() + "," + deviceStatus.getMaintenanceInfo().getSteps() + "," + deviceStatus.getMaintenanceInfo().getStepThreshold() + "," + deviceStatus.getMaintenanceInfo().isMaintenaceRequired() + "," + deviceStatus.getBleOffSource() + "," + bytesToHex + "," + bytesToHex2 + "," + bytesToHex3 + "," + bytesToHex4 + "," + deviceStatus.getIotStatus() + "\n");
        }
    }

    public boolean finishLogging() {
        try {
            if (this.managerEnabled) {
                if (this.isLogging || !(this.csvText == null || this.csvText.isEmpty())) {
                    if (this.logPath != null) {
                        BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(this.logPath));
                        bufferedWriter.write(this.csvText);
                        bufferedWriter.close();
                    }
                    if (this.currentDeviceLogInformation != null) {
                        processStoredLogs(this.currentDeviceLogInformation);
                    }
                    this.isLogging = false;
                    this.currentDeviceName = null;
                    this.currentDeviceLogInformation = null;
                    this.csvText = "";
                    return true;
                }
                return false;
            }
            return false;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void writeDeviceLogToFile(String str, String str2, String str3, String str4, String str5, String str6) {
        writeDeviceDebugLogToFile(str, new TreadlyDeviceLogInfo(str2, str3, str4, str5, str6), new DeviceLogInfo("device_logs", deviceLogFileNameFormat) { // from class: com.treadly.client.lib.sdk.Managers.TreadlyLogManager.1
            @Override // com.treadly.client.lib.sdk.Model.DeviceLogInfo
            public String getTimeStamp() {
                return TreadlyLogManager.this.getDeviceLogTimeStamp();
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void writeDeviceIrLogToFile(String str, String str2, String str3, String str4, String str5, String str6) {
        writeDeviceDebugLogToFile(str, new TreadlyDeviceLogInfo(str2, str3, str4, str5, str6), new DeviceLogInfo("device_ir_log", deviceIrLogFileNameFormat) { // from class: com.treadly.client.lib.sdk.Managers.TreadlyLogManager.2
            @Override // com.treadly.client.lib.sdk.Model.DeviceLogInfo
            public String getTimeStamp() {
                return TreadlyLogManager.this.getTimeStamp();
            }
        });
    }

    private void writeDeviceDebugLogToFile(String str, TreadlyDeviceLogInfo treadlyDeviceLogInfo, DeviceLogInfo deviceLogInfo) {
        try {
            File file = new File(getDocumentsDirectory() + MqttTopic.TOPIC_LEVEL_SEPARATOR + deviceLogInfo.directoryName + MqttTopic.TOPIC_LEVEL_SEPARATOR + treadlyDeviceLogInfo.serialNumber + MqttTopic.TOPIC_LEVEL_SEPARATOR);
            String format = String.format(deviceLogInfo.fileNameFormat, treadlyDeviceLogInfo.serialNumber, deviceLogInfo.getTimeStamp());
            if (file.exists() || file.mkdirs()) {
                BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(new File(file, format)));
                bufferedWriter.write(str);
                bufferedWriter.close();
                processStoredDevicelogs(treadlyDeviceLogInfo, deviceLogInfo);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void processStoredLogs(TreadlyDeviceLogInfo treadlyDeviceLogInfo) {
        try {
            File[] listFiles = new File(getDocumentsDirectory() + "/logs/" + treadlyDeviceLogInfo.serialNumber + MqttTopic.TOPIC_LEVEL_SEPARATOR).listFiles();
            if (listFiles == null) {
                return;
            }
            for (final File file : listFiles) {
                sendFile(file, treadlyDeviceLogInfo, new TreadlyLogManagerSendFileListener() { // from class: com.treadly.client.lib.sdk.Managers.TreadlyLogManager.3
                    @Override // com.treadly.client.lib.sdk.Managers.TreadlyLogManager.TreadlyLogManagerSendFileListener
                    public void onSuccess(boolean z) {
                        if (z) {
                            if (file.exists()) {
                                boolean delete = file.delete();
                                PrintStream printStream = System.out;
                                printStream.println("Delete file: " + delete);
                                return;
                            }
                            return;
                        }
                        System.out.println("Error: File not successfully sent");
                    }
                });
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void processStoredDeviceLogs(TreadlyDeviceLogInfo treadlyDeviceLogInfo) {
        try {
            File[] listFiles = new File(getDocumentsDirectory() + "/device_logs/" + treadlyDeviceLogInfo.serialNumber + MqttTopic.TOPIC_LEVEL_SEPARATOR).listFiles();
            if (listFiles == null) {
                return;
            }
            for (final File file : listFiles) {
                sendDeviceLogFile(file, treadlyDeviceLogInfo, new TreadlyLogManagerSendFileListener() { // from class: com.treadly.client.lib.sdk.Managers.TreadlyLogManager.4
                    @Override // com.treadly.client.lib.sdk.Managers.TreadlyLogManager.TreadlyLogManagerSendFileListener
                    public void onSuccess(boolean z) {
                        if (z) {
                            if (file.exists()) {
                                boolean delete = file.delete();
                                PrintStream printStream = System.out;
                                printStream.println("Delete file: " + delete);
                                return;
                            }
                            return;
                        }
                        System.out.println("Error: File not successfully sent");
                    }
                });
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void processStoredDevicelogs(TreadlyDeviceLogInfo treadlyDeviceLogInfo, DeviceLogInfo deviceLogInfo) {
        try {
            File[] listFiles = new File(getDocumentsDirectory() + MqttTopic.TOPIC_LEVEL_SEPARATOR + deviceLogInfo.directoryName + MqttTopic.TOPIC_LEVEL_SEPARATOR + treadlyDeviceLogInfo.serialNumber + MqttTopic.TOPIC_LEVEL_SEPARATOR).listFiles();
            if (listFiles == null) {
                return;
            }
            for (final File file : listFiles) {
                sendDeviceLogFile(file, treadlyDeviceLogInfo, new TreadlyLogManagerSendFileListener() { // from class: com.treadly.client.lib.sdk.Managers.TreadlyLogManager.5
                    @Override // com.treadly.client.lib.sdk.Managers.TreadlyLogManager.TreadlyLogManagerSendFileListener
                    public void onSuccess(boolean z) {
                        if (z) {
                            if (file.exists()) {
                                boolean delete = file.delete();
                                PrintStream printStream = System.out;
                                printStream.println("Delete file: " + delete);
                                return;
                            }
                            return;
                        }
                        System.out.println("Error: File not successfully sent");
                    }
                });
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private File getDocumentsDirectory() {
        return this.fileDirectory;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public String getDeviceLogTimeStamp() {
        return new SimpleDateFormat("yyyy-MM-dd_HH:mm:ss", Locale.getDefault()).format(new Date());
    }

    private String convertSecondsToTimeString(int i) {
        return String.format(Locale.getDefault(), "%02d:%02d", Integer.valueOf((i / 60) % 100), Integer.valueOf((i % DateTimeConstants.SECONDS_PER_HOUR) % 60));
    }

    protected void sendFile(final File file, TreadlyDeviceLogInfo treadlyDeviceLogInfo, final TreadlyLogManagerSendFileListener treadlyLogManagerSendFileListener) {
        final JSONObject dataPayload = getDataPayload(treadlyDeviceLogInfo);
        this.webThreadPool.execute(new Runnable() { // from class: com.treadly.client.lib.sdk.Managers.TreadlyLogManager.6
            @Override // java.lang.Runnable
            public void run() {
                JSONObject postJsonFileUpload = RequestUtil.postJsonFileUpload("https://edge.treadly.co:51235/treadly/log/upload", dataPayload, file);
                if (postJsonFileUpload == null) {
                    TreadlyLogManager.runOnMain(new Runnable() { // from class: com.treadly.client.lib.sdk.Managers.TreadlyLogManager.6.1
                        @Override // java.lang.Runnable
                        public void run() {
                            treadlyLogManagerSendFileListener.onSuccess(false);
                        }
                    });
                    return;
                }
                final boolean equals = postJsonFileUpload.optString("status", "").equals("ok");
                TreadlyLogManager.runOnMain(new Runnable() { // from class: com.treadly.client.lib.sdk.Managers.TreadlyLogManager.6.2
                    @Override // java.lang.Runnable
                    public void run() {
                        treadlyLogManagerSendFileListener.onSuccess(equals);
                    }
                });
            }
        });
    }

    protected void sendDeviceLogFile(final File file, TreadlyDeviceLogInfo treadlyDeviceLogInfo, final TreadlyLogManagerSendFileListener treadlyLogManagerSendFileListener) {
        final JSONObject dataPayload = getDataPayload(treadlyDeviceLogInfo);
        this.webThreadPool.execute(new Runnable() { // from class: com.treadly.client.lib.sdk.Managers.TreadlyLogManager.7
            @Override // java.lang.Runnable
            public void run() {
                JSONObject postJsonFileUpload = RequestUtil.postJsonFileUpload("https://edge.treadly.co:51235/treadly/device/debug/log/upload", dataPayload, file);
                if (postJsonFileUpload == null) {
                    TreadlyLogManager.runOnMain(new Runnable() { // from class: com.treadly.client.lib.sdk.Managers.TreadlyLogManager.7.1
                        @Override // java.lang.Runnable
                        public void run() {
                            treadlyLogManagerSendFileListener.onSuccess(false);
                        }
                    });
                    return;
                }
                final boolean equals = postJsonFileUpload.optString("status", "").equals("ok");
                TreadlyLogManager.runOnMain(new Runnable() { // from class: com.treadly.client.lib.sdk.Managers.TreadlyLogManager.7.2
                    @Override // java.lang.Runnable
                    public void run() {
                        treadlyLogManagerSendFileListener.onSuccess(equals);
                    }
                });
            }
        });
    }

    protected JSONObject getDataPayload(TreadlyDeviceLogInfo treadlyDeviceLogInfo) {
        try {
            JSONObject jSONObject = new JSONObject();
            jSONObject.put("client_secret", CloudMessage.clientSecret);
            jSONObject.put("serial_number", treadlyDeviceLogInfo.serialNumber);
            jSONObject.put("mac_address", treadlyDeviceLogInfo.macAddress);
            jSONObject.put("app_version", treadlyDeviceLogInfo.appVersion);
            jSONObject.put("custom_board_version", treadlyDeviceLogInfo.customboardVersion);
            jSONObject.put("main_board_version", treadlyDeviceLogInfo.mainboardVersion);
            jSONObject.put("id", getUuid());
            return jSONObject;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public String getUuid() {
        return this.uuid;
    }

    public void setUuid(Activity activity) {
        String string;
        if (activity == null || (string = Settings.Secure.getString(activity.getContentResolver(), "android_id")) == null || string.length() < 8) {
            return;
        }
        this.uuid = string.substring(0, 8);
    }

    public void storeFactoryTestLog(FactoryTestResults factoryTestResults) {
        try {
            String format = String.format(Locale.getDefault(), "%08d", Long.valueOf(factoryTestResults.customBoardSerialNumber));
            String format2 = String.format(Locale.getDefault(), "Treadly_Factory_Test_Results_%s_%s", format, getTimeStamp());
            File file = new File(getDocumentsDirectory() + "/factory_test_logs/" + format + MqttTopic.TOPIC_LEVEL_SEPARATOR);
            if (file.exists() || file.mkdirs()) {
                BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(new File(file, format2)));
                bufferedWriter.write(factoryTestResults.serialize());
                bufferedWriter.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public FactoryTestResults[] getStoredFactoryTestResults(long j) {
        try {
            ArrayList arrayList = new ArrayList();
            File[] listFiles = new File(getDocumentsDirectory() + "/factory_test_logs//" + String.format(Locale.getDefault(), "%08d", Long.valueOf(j)) + MqttTopic.TOPIC_LEVEL_SEPARATOR).listFiles();
            if (listFiles == null) {
                return new FactoryTestResults[0];
            }
            for (File file : listFiles) {
                StringBuffer stringBuffer = new StringBuffer();
                BufferedReader bufferedReader = new BufferedReader(new FileReader(file));
                char[] cArr = new char[1024];
                while (true) {
                    int read = bufferedReader.read(cArr);
                    if (read == -1) {
                        break;
                    }
                    stringBuffer.append(String.valueOf(cArr, 0, read));
                }
                bufferedReader.close();
                FactoryTestResults deserialize = FactoryTestResults.deserialize(stringBuffer.toString());
                if (deserialize != null) {
                    arrayList.add(deserialize);
                }
            }
            FactoryTestResults[] factoryTestResultsArr = new FactoryTestResults[arrayList.size()];
            arrayList.toArray(factoryTestResultsArr);
            return factoryTestResultsArr;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void handleDeviceDebugLogResponse(final ResponseMessage responseMessage, final long j, final String str, final ComponentInfo componentInfo, final ComponentInfo componentInfo2) {
        runOnMain(new Runnable() { // from class: com.treadly.client.lib.sdk.Managers.TreadlyLogManager.8
            @Override // java.lang.Runnable
            public void run() {
                PacketizedMessage read;
                Packetizer.shared.write(responseMessage);
                if (!Packetizer.shared.isReady() || (read = Packetizer.shared.read()) == null) {
                    return;
                }
                DeviceDebugLogInfo parseDeviceDebugLogMessage = Message.parseDeviceDebugLogMessage(read.getPayload());
                String format = String.format(Locale.getDefault(), "%08d", Long.valueOf(j));
                TreadlyLogManager.shared.writeDeviceLogToFile(TreadlyLogManager.this.convertDeviceDebugLogMessage(format, str, parseDeviceDebugLogMessage, componentInfo), format, str, TreadlyLogManager.this.getAppVersion(), componentInfo.getVersionInfo().getVersion(), componentInfo2.getVersionInfo().getVersion());
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void handleDeviceIrDebugLogResponse(final ResponseMessage responseMessage, final long j, final String str, final ComponentInfo componentInfo, final ComponentInfo componentInfo2) {
        runOnMain(new Runnable() { // from class: com.treadly.client.lib.sdk.Managers.TreadlyLogManager.9
            @Override // java.lang.Runnable
            public void run() {
                PacketizedMessage read;
                Packetizer.shared.write(responseMessage);
                if (!Packetizer.shared.isReady() || (read = Packetizer.shared.read()) == null) {
                    return;
                }
                DeviceIrDebugEventInfo parseDeviceIrDebugLogEventMessage = Message.parseDeviceIrDebugLogEventMessage(read.getPayload());
                switch (parseDeviceIrDebugLogEventMessage.action) {
                    case start:
                        TreadlyLogManager.this.deviceIrDebugEvents.clear();
                        TreadlyLogManager.this.deviceIrDebugEvents.add(parseDeviceIrDebugLogEventMessage);
                        return;
                    case append:
                        TreadlyLogManager.this.deviceIrDebugEvents.add(parseDeviceIrDebugLogEventMessage);
                        return;
                    case end:
                        TreadlyLogManager.this.deviceIrDebugEvents.add(parseDeviceIrDebugLogEventMessage);
                        String format = String.format(Locale.getDefault(), "%08d", Long.valueOf(j));
                        DeviceIrDebugEventInfo[] deviceIrDebugEventInfoArr = new DeviceIrDebugEventInfo[TreadlyLogManager.this.deviceIrDebugEvents.size()];
                        TreadlyLogManager.this.deviceIrDebugEvents.toArray(deviceIrDebugEventInfoArr);
                        TreadlyLogManager.this.writeDeviceIrLogToFile(TreadlyLogManager.this.convertDeviceIrDebugLogMessage(format, componentInfo.getId(), TreadlyLogManager.this.convertDeviceIrDebugLogEvents(deviceIrDebugEventInfoArr, componentInfo), componentInfo), format, str, TreadlyLogManager.this.getAppVersion(), componentInfo.getVersionInfo().getVersion(), componentInfo2.getVersionInfo().getVersion());
                        return;
                    default:
                        return;
                }
            }
        });
    }

    protected String convertDeviceDebugLogMessage(String str, String str2, DeviceDebugLogInfo deviceDebugLogInfo, ComponentInfo componentInfo) {
        String str3 = (((("S/N: " + str + "\n") + "Device ID: " + str2 + "\n") + "Customboard Version: " + componentInfo.getVersionInfo().getVersion() + "\n") + "Version: " + deviceDebugLogInfo.version + "\n") + "Count: " + deviceDebugLogInfo.count + "\n";
        DeviceDebugLogEvent[] deviceDebugLogEventArr = deviceDebugLogInfo.events;
        for (DeviceDebugLogEvent deviceDebugLogEvent : deviceDebugLogEventArr) {
            str3 = str3 + deviceDebugLogEvent.eventId + " " + deviceDebugLogEvent.parameter + "\n";
        }
        return str3;
    }

    protected DeviceIrDebugLogInfo convertDeviceIrDebugLogEvents(DeviceIrDebugEventInfo[] deviceIrDebugEventInfoArr, ComponentInfo componentInfo) {
        return new DeviceIrDebugLogInfo(componentInfo.getVersionInfo().getVersion(), deviceIrDebugEventInfoArr);
    }

    protected String convertDeviceIrDebugLogMessage(String str, String str2, DeviceIrDebugLogInfo deviceIrDebugLogInfo, ComponentInfo componentInfo) {
        int[] iArr;
        String str3 = (("S/N: " + str + "\n") + "Device ID: " + str2 + "\n") + "Customboard Version: " + componentInfo.getVersionInfo().getVersion() + "\n";
        for (DeviceIrDebugEventInfo deviceIrDebugEventInfo : deviceIrDebugLogInfo.irEvents) {
            String str4 = str3 + String.format(Locale.getDefault(), "%d ", Integer.valueOf(deviceIrDebugEventInfo.timestamp));
            String str5 = str4;
            for (int i : deviceIrDebugEventInfo.adcValues) {
                str5 = str5 + String.format(Locale.getDefault(), "%d ", Integer.valueOf(i));
            }
            str3 = str5 + "\n";
        }
        return str3;
    }

    private void setAppVersion(String str) {
        this.appVersion = str;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public String getAppVersion() {
        return this.appVersion;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public String getTimeStamp() {
        return new SimpleDateFormat("yyyy-MM-dd_HH:mm:ss", Locale.getDefault()).format(new Date());
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static void runOnMain(Runnable runnable) {
        new Handler(Looper.getMainLooper()).post(runnable);
    }
}
