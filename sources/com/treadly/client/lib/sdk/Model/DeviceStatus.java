package com.treadly.client.lib.sdk.Model;

/* loaded from: classes2.dex */
public class DeviceStatus {
    private boolean audioPlayback;
    private boolean bleEnabled;
    private int bleOffSource;
    private int current;
    private boolean deviceIrDebugLogModeEnabled;
    private byte[] deviceStatusDiagnosticPayload;
    private byte[] deviceStatusExt2Payload;
    private byte[] deviceStatusExtPayload;
    private byte[] deviceStatusIotPayload;
    private byte[] deviceStatusPayload;
    private float distance;
    private DistanceUnits distanceUnits;
    private boolean emergencyHandrailStop;
    private boolean emergencyStop;
    private boolean gameMode;
    private HandrailStatus handrailStatus;
    public int id;
    private byte iotStatus;
    private boolean irEnabled;
    private IrMode irMode;
    private boolean isStatusDiagnosticValid;
    private boolean isStatusExt2Valid;
    private boolean isStatusExtValid;
    private boolean isStatusIotValid;
    private boolean isStatusValid;
    private MaintenanceInfo maintenanceInfo;
    private DeviceMode mode;
    private int pairedPhonesCount;
    private boolean pairingMode;
    private PairingModeTriggerType pairingTriggerType;
    private boolean pauseAfterIdleEnabled;
    private byte pauseAfterIdleTimeout;
    private boolean powerOn;
    private int seconds;
    private boolean sessionOwnership;
    private boolean sessionPaused;
    private SpeedInfo speed;
    private DeviceSpeedZoneConfiguration speedZoneConfiguration;
    public DeviceStatusCode status;
    private int steps;
    private TemperatureInfo temperatureInfo;
    private float totalDistance;
    private int totalSteps;

    public boolean isValid() {
        return this.isStatusValid && this.isStatusExtValid && this.isStatusDiagnosticValid && this.isStatusExt2Valid && this.isStatusIotValid;
    }

    public DeviceStatus(DeviceStatus deviceStatus, int i, int i2, MaintenanceInfo maintenanceInfo, byte[] bArr) {
        this.isStatusValid = false;
        this.isStatusExtValid = false;
        this.isStatusDiagnosticValid = false;
        this.isStatusExt2Valid = false;
        this.isStatusIotValid = false;
        this.status = deviceStatus.status;
        this.steps = deviceStatus.steps;
        this.distance = deviceStatus.distance;
        this.distanceUnits = deviceStatus.distanceUnits;
        this.seconds = deviceStatus.seconds;
        this.speed = deviceStatus.speed;
        this.mode = deviceStatus.mode;
        this.powerOn = deviceStatus.powerOn;
        this.irEnabled = deviceStatus.irEnabled;
        this.emergencyHandrailStop = deviceStatus.emergencyHandrailStop;
        this.handrailStatus = deviceStatus.handrailStatus;
        this.deviceStatusPayload = deviceStatus.deviceStatusPayload;
        this.totalSteps = i;
        this.totalDistance = deviceStatus.totalDistance;
        this.speedZoneConfiguration = deviceStatus.speedZoneConfiguration;
        this.bleEnabled = deviceStatus.bleEnabled;
        this.audioPlayback = deviceStatus.audioPlayback;
        this.irMode = deviceStatus.irMode;
        this.sessionPaused = deviceStatus.sessionPaused;
        this.pairingMode = deviceStatus.pairingMode;
        this.emergencyStop = deviceStatus.emergencyStop;
        this.pairingTriggerType = deviceStatus.pairingTriggerType;
        this.deviceStatusExtPayload = deviceStatus.deviceStatusExtPayload;
        this.sessionOwnership = deviceStatus.sessionOwnership;
        this.temperatureInfo = deviceStatus.temperatureInfo;
        this.current = deviceStatus.current;
        this.deviceIrDebugLogModeEnabled = deviceStatus.deviceIrDebugLogModeEnabled;
        this.bleOffSource = deviceStatus.bleOffSource;
        this.gameMode = deviceStatus.gameMode;
        this.deviceStatusDiagnosticPayload = deviceStatus.deviceStatusDiagnosticPayload;
        this.pairedPhonesCount = i2;
        this.maintenanceInfo = maintenanceInfo;
        this.deviceStatusExt2Payload = bArr;
        this.deviceStatusIotPayload = deviceStatus.deviceStatusIotPayload;
        this.iotStatus = deviceStatus.iotStatus;
        this.id = deviceStatus.id;
        this.isStatusValid = deviceStatus.isStatusValid;
        this.isStatusExtValid = deviceStatus.isStatusExtValid;
        this.isStatusDiagnosticValid = deviceStatus.isStatusDiagnosticValid;
        this.isStatusExt2Valid = true;
        this.isStatusIotValid = deviceStatus.isStatusIotValid;
    }

    public DeviceStatus(DeviceStatus deviceStatus, TemperatureInfo temperatureInfo, int i, boolean z, int i2, boolean z2, boolean z3, byte b, byte[] bArr) {
        this.isStatusValid = false;
        this.isStatusExtValid = false;
        this.isStatusDiagnosticValid = false;
        this.isStatusExt2Valid = false;
        this.isStatusIotValid = false;
        this.status = deviceStatus.status;
        this.steps = deviceStatus.steps;
        this.distance = deviceStatus.distance;
        this.distanceUnits = deviceStatus.distanceUnits;
        this.seconds = deviceStatus.seconds;
        this.speed = deviceStatus.speed;
        this.mode = deviceStatus.mode;
        this.powerOn = deviceStatus.powerOn;
        this.irEnabled = deviceStatus.irEnabled;
        this.emergencyHandrailStop = deviceStatus.emergencyHandrailStop;
        this.handrailStatus = deviceStatus.handrailStatus;
        this.deviceStatusPayload = deviceStatus.deviceStatusPayload;
        this.totalSteps = deviceStatus.totalSteps;
        this.totalDistance = deviceStatus.totalDistance;
        this.speedZoneConfiguration = deviceStatus.speedZoneConfiguration;
        this.maintenanceInfo = deviceStatus.maintenanceInfo;
        this.bleEnabled = deviceStatus.bleEnabled;
        this.audioPlayback = deviceStatus.audioPlayback;
        this.irMode = deviceStatus.irMode;
        this.sessionPaused = deviceStatus.sessionPaused;
        this.pairingMode = deviceStatus.pairingMode;
        this.emergencyStop = deviceStatus.emergencyStop;
        this.pairingTriggerType = deviceStatus.pairingTriggerType;
        this.deviceStatusExtPayload = deviceStatus.deviceStatusExtPayload;
        this.sessionOwnership = deviceStatus.sessionOwnership;
        this.temperatureInfo = temperatureInfo;
        this.current = i;
        this.deviceIrDebugLogModeEnabled = z;
        this.bleOffSource = i2;
        this.gameMode = z2;
        this.deviceStatusDiagnosticPayload = bArr;
        this.deviceStatusExt2Payload = deviceStatus.deviceStatusExt2Payload;
        this.pairedPhonesCount = deviceStatus.pairedPhonesCount;
        this.deviceStatusIotPayload = deviceStatus.deviceStatusIotPayload;
        this.iotStatus = deviceStatus.iotStatus;
        this.id = deviceStatus.id;
        this.isStatusValid = deviceStatus.isStatusValid;
        this.isStatusExtValid = deviceStatus.isStatusExtValid;
        this.isStatusDiagnosticValid = true;
        this.isStatusExt2Valid = deviceStatus.isStatusExt2Valid;
        this.isStatusIotValid = deviceStatus.isStatusIotValid;
        this.pauseAfterIdleTimeout = b;
        this.pauseAfterIdleEnabled = z3;
    }

    public DeviceStatus(DeviceStatus deviceStatus, byte b, byte[] bArr) {
        this.isStatusValid = false;
        this.isStatusExtValid = false;
        this.isStatusDiagnosticValid = false;
        this.isStatusExt2Valid = false;
        this.isStatusIotValid = false;
        this.status = deviceStatus.status;
        this.steps = deviceStatus.steps;
        this.distance = deviceStatus.distance;
        this.distanceUnits = deviceStatus.distanceUnits;
        this.seconds = deviceStatus.seconds;
        this.speed = deviceStatus.speed;
        this.mode = deviceStatus.mode;
        this.powerOn = deviceStatus.powerOn;
        this.irEnabled = deviceStatus.irEnabled;
        this.emergencyHandrailStop = deviceStatus.emergencyHandrailStop;
        this.handrailStatus = deviceStatus.handrailStatus;
        this.deviceStatusPayload = deviceStatus.deviceStatusPayload;
        this.totalSteps = deviceStatus.totalSteps;
        this.totalDistance = deviceStatus.totalDistance;
        this.speedZoneConfiguration = deviceStatus.speedZoneConfiguration;
        this.maintenanceInfo = deviceStatus.maintenanceInfo;
        this.bleEnabled = deviceStatus.bleEnabled;
        this.audioPlayback = deviceStatus.audioPlayback;
        this.irMode = deviceStatus.irMode;
        this.sessionPaused = deviceStatus.sessionPaused;
        this.pairingMode = deviceStatus.pairingMode;
        this.emergencyStop = deviceStatus.emergencyStop;
        this.pairingTriggerType = deviceStatus.pairingTriggerType;
        this.deviceStatusExtPayload = deviceStatus.deviceStatusExtPayload;
        this.sessionOwnership = deviceStatus.sessionOwnership;
        this.temperatureInfo = deviceStatus.temperatureInfo;
        this.current = deviceStatus.current;
        this.deviceIrDebugLogModeEnabled = deviceStatus.deviceIrDebugLogModeEnabled;
        this.bleOffSource = deviceStatus.bleOffSource;
        this.gameMode = deviceStatus.gameMode;
        this.deviceStatusDiagnosticPayload = deviceStatus.deviceStatusDiagnosticPayload;
        this.deviceStatusExt2Payload = deviceStatus.deviceStatusExt2Payload;
        this.pairedPhonesCount = deviceStatus.pairedPhonesCount;
        this.deviceStatusIotPayload = bArr;
        this.iotStatus = b;
        this.id = deviceStatus.id;
        this.isStatusValid = deviceStatus.isStatusValid;
        this.isStatusExtValid = deviceStatus.isStatusExtValid;
        this.isStatusDiagnosticValid = deviceStatus.isStatusDiagnosticValid;
        this.isStatusExt2Valid = deviceStatus.isStatusExt2Valid;
        this.isStatusIotValid = true;
        this.pauseAfterIdleEnabled = deviceStatus.pauseAfterIdleEnabled;
        this.pauseAfterIdleEnabled = deviceStatus.pauseAfterIdleEnabled;
    }

    public DeviceStatus(DeviceStatus deviceStatus, float f, int i, DeviceSpeedZoneConfiguration deviceSpeedZoneConfiguration, boolean z, boolean z2, IrMode irMode, boolean z3, boolean z4, boolean z5, PairingModeTriggerType pairingModeTriggerType, boolean z6, byte[] bArr) {
        this.isStatusValid = false;
        this.isStatusExtValid = false;
        this.isStatusDiagnosticValid = false;
        this.isStatusExt2Valid = false;
        this.isStatusIotValid = false;
        this.status = deviceStatus.status;
        this.steps = deviceStatus.steps;
        this.distance = deviceStatus.distance;
        this.distanceUnits = deviceStatus.distanceUnits;
        this.seconds = deviceStatus.seconds;
        this.speed = deviceStatus.speed;
        this.mode = deviceStatus.mode;
        this.powerOn = deviceStatus.powerOn;
        this.irEnabled = deviceStatus.irEnabled;
        this.emergencyHandrailStop = deviceStatus.emergencyHandrailStop;
        this.handrailStatus = deviceStatus.handrailStatus;
        this.deviceStatusPayload = deviceStatus.deviceStatusPayload;
        this.totalSteps = i;
        this.totalDistance = f;
        this.speedZoneConfiguration = deviceSpeedZoneConfiguration;
        this.maintenanceInfo = deviceStatus.maintenanceInfo;
        this.bleEnabled = z;
        this.audioPlayback = z2;
        this.irMode = irMode;
        this.sessionPaused = z3;
        this.pairingMode = z4;
        this.emergencyStop = z5;
        this.pairingTriggerType = pairingModeTriggerType;
        this.sessionOwnership = z6;
        this.deviceStatusExtPayload = bArr;
        this.temperatureInfo = deviceStatus.temperatureInfo;
        this.current = deviceStatus.current;
        this.deviceIrDebugLogModeEnabled = deviceStatus.deviceIrDebugLogModeEnabled;
        this.bleOffSource = deviceStatus.bleOffSource;
        this.gameMode = deviceStatus.gameMode;
        this.deviceStatusDiagnosticPayload = deviceStatus.deviceStatusDiagnosticPayload;
        this.deviceStatusExt2Payload = deviceStatus.deviceStatusExt2Payload;
        this.pairedPhonesCount = deviceStatus.pairedPhonesCount;
        this.deviceStatusIotPayload = deviceStatus.deviceStatusIotPayload;
        this.iotStatus = deviceStatus.iotStatus;
        this.id = deviceStatus.id;
        this.isStatusValid = deviceStatus.isStatusValid;
        this.isStatusExtValid = true;
        this.isStatusDiagnosticValid = deviceStatus.isStatusDiagnosticValid;
        this.isStatusExt2Valid = deviceStatus.isStatusExt2Valid;
        this.isStatusIotValid = deviceStatus.isStatusIotValid;
        this.pauseAfterIdleTimeout = deviceStatus.pauseAfterIdleTimeout;
        this.pauseAfterIdleEnabled = deviceStatus.pauseAfterIdleEnabled;
    }

    public byte[] getDeviceStatusExt2Payload() {
        return this.deviceStatusExt2Payload;
    }

    public DeviceStatus(int i, int i2, float f, DistanceUnits distanceUnits, int i3, SpeedInfo speedInfo, int i4, DeviceMode deviceMode, boolean z, boolean z2, boolean z3, HandrailStatus handrailStatus, boolean z4, boolean z5, byte[] bArr) {
        this.isStatusValid = false;
        this.isStatusExtValid = false;
        this.isStatusDiagnosticValid = false;
        this.isStatusExt2Valid = false;
        this.isStatusIotValid = false;
        this.status = DeviceStatusCode.fromValue(i);
        this.steps = i2;
        this.distance = f;
        this.distanceUnits = distanceUnits;
        this.seconds = i3;
        this.speed = speedInfo;
        this.mode = deviceMode;
        this.powerOn = deviceMode != null && deviceMode == DeviceMode.ACTIVE;
        this.irEnabled = z;
        this.emergencyHandrailStop = z2;
        this.handrailStatus = handrailStatus;
        this.deviceStatusPayload = bArr;
        this.totalDistance = 0.0f;
        this.totalSteps = 0;
        this.temperatureInfo = new TemperatureInfo(0.0f, TemperatureStatusCode.TEMP_STATUS_UNKNOWN, false);
        this.current = 0;
        this.id = i4;
        this.maintenanceInfo = new MaintenanceInfo(0, 0, z3, 0, 0);
        this.bleEnabled = false;
        this.audioPlayback = false;
        this.irMode = IrMode.GEN_1;
        this.sessionPaused = false;
        this.pairingMode = false;
        this.emergencyStop = false;
        this.pairingTriggerType = PairingModeTriggerType.none;
        this.deviceIrDebugLogModeEnabled = false;
        this.bleOffSource = 0;
        this.gameMode = false;
        this.sessionOwnership = false;
        this.deviceStatusDiagnosticPayload = new byte[0];
        this.deviceStatusExtPayload = new byte[0];
        this.deviceStatusExt2Payload = new byte[0];
        this.isStatusExt2Valid = z4;
        this.pairedPhonesCount = 0;
        this.deviceStatusIotPayload = new byte[0];
        this.isStatusIotValid = z5;
        this.iotStatus = (byte) 0;
        this.speedZoneConfiguration = new DeviceSpeedZoneConfiguration(0, 0, 0, 0);
        this.isStatusValid = true;
    }

    public DeviceStatusCode getStatusCode() {
        return this.status;
    }

    public int getSteps() {
        return this.steps;
    }

    public float getDistance() {
        return this.distance;
    }

    public DistanceUnits getDistanceUnits() {
        return this.distanceUnits;
    }

    public int getSeconds() {
        return this.seconds;
    }

    public SpeedInfo getSpeedInfo() {
        return this.speed;
    }

    public float getTotalDistance() {
        return this.totalDistance;
    }

    public int getTotalSteps() {
        return this.totalSteps;
    }

    public TemperatureInfo getTemperatureInfo() {
        return this.temperatureInfo;
    }

    public int getCurrent() {
        return this.current;
    }

    public DeviceSpeedZoneConfiguration getSpeedZoneConfiguration() {
        return this.speedZoneConfiguration;
    }

    public DeviceMode getMode() {
        return this.mode;
    }

    public boolean isPoweredOn() {
        return this.powerOn;
    }

    public boolean isIrEnabled() {
        return this.irEnabled;
    }

    public boolean isEmergencyHandrailEnabled() {
        return this.emergencyHandrailStop;
    }

    public MaintenanceInfo getMaintenanceInfo() {
        return this.maintenanceInfo;
    }

    public HandrailStatus getHandrailStatus() {
        return this.handrailStatus;
    }

    public boolean isBleEnabled() {
        return this.bleEnabled;
    }

    public boolean isAudioPlayback() {
        return this.audioPlayback;
    }

    public IrMode getIrMode() {
        return this.irMode;
    }

    public RemoteStatus getRemoteStatus() {
        return this.irEnabled ? RemoteStatus.automatic : RemoteStatus.manual;
    }

    public boolean getPauseState() {
        return this.sessionPaused;
    }

    public boolean getPairingModeState() {
        return this.pairingMode;
    }

    public boolean getEmergencyStopState() {
        return this.emergencyStop;
    }

    public PairingModeTriggerType getPairingModeTriggerType() {
        return this.pairingTriggerType;
    }

    public boolean isDeviceIrDebugLogModeEnabled() {
        return this.deviceIrDebugLogModeEnabled;
    }

    public int getBleOffSource() {
        return this.bleOffSource;
    }

    public boolean isSessionOwnership() {
        return this.sessionOwnership;
    }

    public byte[] getDeviceStatusPayload() {
        return this.deviceStatusPayload;
    }

    public byte[] getDeviceStatusExtPayload() {
        return this.deviceStatusExtPayload;
    }

    public byte[] getDeviceStatusDiagnosticPayload() {
        return this.deviceStatusDiagnosticPayload;
    }

    public boolean isGameModeEnabled() {
        return this.gameMode;
    }

    public boolean isPauseAfterIdleEnabled() {
        return this.pauseAfterIdleEnabled;
    }

    public byte getPauseAfterIdleTimeout() {
        return this.pauseAfterIdleTimeout;
    }

    public int getPairedPhonesCounter() {
        return this.pairedPhonesCount;
    }

    public byte[] getDeviceStatusIotPayload() {
        return this.deviceStatusIotPayload;
    }

    public int getIotStatus() {
        return this.iotStatus;
    }
}
