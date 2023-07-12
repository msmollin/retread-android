package com.treadly.Treadly.UI.TreadlyDashboard.Tools;

import com.github.mikephil.charting.utils.Utils;
import com.treadly.client.lib.sdk.Model.DeviceStatus;
import com.treadly.client.lib.sdk.Model.DeviceStatusCode;
import com.treadly.client.lib.sdk.Model.DistanceUnits;
import com.treadly.client.lib.sdk.Model.HandrailStatus;
import com.treadly.client.lib.sdk.Model.MaintenanceInfo;
import com.treadly.client.lib.sdk.Model.RemoteStatus;
import com.treadly.client.lib.sdk.Model.VersionInfo;

/* loaded from: classes2.dex */
public class DeviceStatusVerification {
    static final double DISTANCE_CHANGE_THRESHOLD = 10.0d;
    static final int SECONDS_CHANGE_THRESHOLD = 10;
    static final int STEPS_CHANGE_THRESHOLD = 20;
    public static DeviceStatusVerification shared = new DeviceStatusVerification();
    VersionInfo minGen2Version = new VersionInfo(2, 0, 0);
    public int maintenanceRequiredCount = 0;
    public boolean maintenanceRequired = false;
    Integer seconds = null;
    public Integer steps = null;
    public Integer totalSteps = null;
    public Float distance = null;
    public Float totalDistance = null;
    public DistanceUnits distanceUnits = null;
    public DeviceStatusCode deviceStatus = null;
    public boolean irEnabled = false;
    public HandrailStatus handrailStatus = null;
    public boolean emergencyHandrailStop = false;
    public RemoteStatus remoteStatus = RemoteStatus.unknown;
    int deviceStatusCount = 0;
    int irEnabledCount = 0;
    int handrailStatusCount = 0;
    int emergencyHandrailStopCount = 0;
    int remoteStatusCount = 0;
    public VersionInfo customBoardVersion = null;
    public VersionInfo mainBoardVersion = null;

    public void verifyDeviceStatus(DeviceStatus deviceStatus) {
        if (deviceStatus == null) {
            return;
        }
        verifyMaintenanceInformation(deviceStatus.getMaintenanceInfo());
        verifySteps(Integer.valueOf(deviceStatus.getSteps()), Integer.valueOf(deviceStatus.getTotalSteps()));
        verifySeconds(deviceStatus.getSeconds());
        verifyDistance(deviceStatus.getDistance(), deviceStatus.getTotalDistance(), deviceStatus.getDistanceUnits());
        verifyStates(deviceStatus.isIrEnabled(), deviceStatus.getHandrailStatus(), deviceStatus.isEmergencyHandrailEnabled(), deviceStatus.getRemoteStatus(), deviceStatus.getStatusCode());
    }

    protected void verifyMaintenanceInformation(MaintenanceInfo maintenanceInfo) {
        if (this.maintenanceRequired != maintenanceInfo.isMaintenaceRequired()) {
            if (this.maintenanceRequiredCount < 3) {
                this.maintenanceRequiredCount++;
                return;
            }
            this.maintenanceRequired = maintenanceInfo.isMaintenaceRequired();
            this.maintenanceRequiredCount = 0;
            return;
        }
        this.maintenanceRequiredCount = 0;
    }

    public void reset() {
        resetSteps();
        resetDistance();
        resetStates();
        resetSeconds();
    }

    public void clear() {
        this.customBoardVersion = null;
        this.mainBoardVersion = null;
        resetMaintenanceInformation();
        reset();
    }

    protected void resetMaintenanceInformation() {
        this.maintenanceRequiredCount = 0;
        this.maintenanceRequired = false;
    }

    protected void verifySteps(Integer num, Integer num2) {
        if (this.steps == null) {
            this.steps = num;
        }
        if (this.totalSteps == null) {
            this.totalSteps = num2;
        }
        int intValue = this.steps.intValue() + 20;
        int intValue2 = this.steps.intValue() - 20;
        if ((num.intValue() < intValue && num.intValue() > intValue2) || num.intValue() == 0 || num.intValue() == 1) {
            this.steps = num;
        }
        int intValue3 = this.totalSteps.intValue() + 20;
        int intValue4 = this.totalSteps.intValue() - 20;
        if ((num2.intValue() <= intValue3 && num2.intValue() >= intValue4) || num2.intValue() == 0 || num2.intValue() == 1) {
            this.totalSteps = num2;
        }
    }

    protected void resetSteps() {
        this.steps = null;
        this.totalSteps = null;
    }

    protected void verifyDistance(float f, float f2, DistanceUnits distanceUnits) {
        if (this.distance == null) {
            this.distance = Float.valueOf(f);
        }
        if (this.totalDistance == null) {
            this.totalDistance = Float.valueOf(f2);
        }
        if (this.distanceUnits == null) {
            this.distanceUnits = distanceUnits;
        }
        if (this.distanceUnits != distanceUnits) {
            this.distance = Float.valueOf(f);
            this.totalDistance = Float.valueOf(f2);
            this.distanceUnits = distanceUnits;
            return;
        }
        double d = f;
        if ((d < this.distance.floatValue() + DISTANCE_CHANGE_THRESHOLD && d > this.distance.floatValue() - DISTANCE_CHANGE_THRESHOLD) || d == Utils.DOUBLE_EPSILON) {
            this.distance = Float.valueOf(f);
        }
        double d2 = f2;
        if (d2 >= this.totalDistance.floatValue() + DISTANCE_CHANGE_THRESHOLD || d2 <= this.totalDistance.floatValue() - DISTANCE_CHANGE_THRESHOLD) {
            return;
        }
        this.totalDistance = Float.valueOf(f2);
    }

    protected void resetDistance() {
        this.distance = null;
        this.totalDistance = null;
        this.distanceUnits = null;
    }

    protected void verifySeconds(int i) {
        if (this.seconds == null) {
            this.seconds = Integer.valueOf(i);
        }
        if ((i < this.seconds.intValue() + 10 && i > this.seconds.intValue() - 10) || i == 0 || i == 1) {
            this.seconds = Integer.valueOf(i);
        }
    }

    protected void resetSeconds() {
        this.seconds = null;
    }

    protected void verifyStates(boolean z, HandrailStatus handrailStatus, boolean z2, RemoteStatus remoteStatus, DeviceStatusCode deviceStatusCode) {
        if (this.handrailStatus == null) {
            this.handrailStatus = handrailStatus;
        }
        if (this.deviceStatus == null) {
            this.deviceStatus = deviceStatusCode;
        }
        if (this.irEnabled != z) {
            if (this.irEnabledCount < 3) {
                this.irEnabledCount++;
            } else {
                this.irEnabled = z;
                this.irEnabledCount = 0;
            }
        } else {
            this.irEnabledCount = 0;
        }
        if (this.remoteStatus != remoteStatus && !isGen2Version()) {
            if (this.remoteStatusCount < 3) {
                this.remoteStatusCount++;
            } else {
                this.remoteStatus = remoteStatus;
                this.remoteStatusCount = 0;
            }
        } else {
            this.remoteStatusCount = 0;
            this.remoteStatus = remoteStatus;
        }
        if (this.handrailStatus != handrailStatus) {
            if (this.handrailStatusCount < 3) {
                this.handrailStatusCount++;
            } else {
                this.handrailStatus = handrailStatus;
                this.handrailStatusCount = 0;
            }
        } else {
            this.handrailStatusCount = 0;
        }
        if (this.emergencyHandrailStop != z2) {
            if (this.emergencyHandrailStopCount < 3) {
                this.emergencyHandrailStopCount++;
            } else {
                this.emergencyHandrailStop = z2;
                this.emergencyHandrailStopCount = 0;
            }
        } else {
            this.emergencyHandrailStopCount = 0;
        }
        if (this.deviceStatus != deviceStatusCode && !isGen2Version()) {
            if (this.deviceStatusCount < 3) {
                this.deviceStatusCount++;
                return;
            }
            this.deviceStatus = deviceStatusCode;
            this.deviceStatusCount = 0;
            return;
        }
        this.deviceStatus = deviceStatusCode;
        this.deviceStatusCount = 0;
    }

    private boolean isGen2Version() {
        if (this.customBoardVersion == null) {
            return false;
        }
        return this.customBoardVersion.isGreaterThan(this.minGen2Version);
    }

    protected void resetStates() {
        this.deviceStatus = null;
        this.irEnabled = false;
        this.handrailStatus = null;
        this.emergencyHandrailStop = false;
        this.deviceStatusCount = 0;
        this.irEnabledCount = 0;
        this.handrailStatusCount = 0;
        this.emergencyHandrailStopCount = 0;
    }
}
