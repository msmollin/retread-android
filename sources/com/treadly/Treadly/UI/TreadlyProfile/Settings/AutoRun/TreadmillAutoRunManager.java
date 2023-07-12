package com.treadly.Treadly.UI.TreadlyProfile.Settings.AutoRun;

import android.app.AlertDialog;
import android.content.DialogInterface;
import androidx.fragment.app.FragmentActivity;
import com.github.mikephil.charting.utils.Utils;
import com.treadly.client.lib.sdk.Listeners.DeviceConnectionEventAdapter;
import com.treadly.client.lib.sdk.Listeners.DeviceConnectionEventListener;
import com.treadly.client.lib.sdk.Listeners.RequestEventAdapter;
import com.treadly.client.lib.sdk.Listeners.RequestEventListener;
import com.treadly.client.lib.sdk.Model.ComponentVersionInfo;
import com.treadly.client.lib.sdk.Model.DeviceConnectionEvent;
import com.treadly.client.lib.sdk.Model.DeviceMode;
import com.treadly.client.lib.sdk.Model.DeviceStatus;
import com.treadly.client.lib.sdk.Model.SpeedInfo;
import com.treadly.client.lib.sdk.Model.VersionInfo;
import com.treadly.client.lib.sdk.TreadlyClientLib;
import java.util.Date;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

/* loaded from: classes2.dex */
public class TreadmillAutoRunManager {
    public static double maxRunTime = 5.0d;
    public static double minRunTime = 1.0d;
    public static final TreadmillAutoRunManager shared = new TreadmillAutoRunManager();
    public ComponentVersionInfo componentVersion;
    public DeviceStatus currentDeviceStatus;
    private AlertDialog progressDialog;
    public Timer speedTimer;
    public Date startTime;
    public boolean startTreadmill;
    public Date totalTime;
    private long runInterval = 4500;
    private double initialSpeed = 1.0d;
    public float setSpeedValue = 0.0f;
    public double queuedSpeedvalue = Utils.DOUBLE_EPSILON;
    public int statusCount = 0;
    private RequestEventListener requestEventListener = new RequestEventAdapter() { // from class: com.treadly.Treadly.UI.TreadlyProfile.Settings.AutoRun.TreadmillAutoRunManager.3
        @Override // com.treadly.client.lib.sdk.Listeners.RequestEventAdapter, com.treadly.client.lib.sdk.Listeners.RequestEventListener
        public void onRequestSetSpeedResponse(boolean z) {
            if (z) {
                TreadmillAutoRunManager.this.queuedSpeedvalue = Utils.DOUBLE_EPSILON;
            }
        }

        @Override // com.treadly.client.lib.sdk.Listeners.RequestEventAdapter, com.treadly.client.lib.sdk.Listeners.RequestEventListener
        public void onRequestStatusResponse(boolean z, DeviceStatus deviceStatus) {
            if (!z || deviceStatus == null) {
                return;
            }
            TreadmillAutoRunManager.this.currentDeviceStatus = deviceStatus;
            if (deviceStatus.isPoweredOn() && deviceStatus.getSpeedInfo().getTargetSpeed() > 0.0f && TreadmillAutoRunManager.this.queuedSpeedvalue > Utils.DOUBLE_EPSILON) {
                TreadlyClientLib.shared.setSpeed((float) TreadmillAutoRunManager.this.queuedSpeedvalue);
            } else if (TreadmillAutoRunManager.this.startTreadmill) {
                TreadmillAutoRunManager.this.startTreadmill = false;
                TreadmillAutoRunManager.this.setSpeed(TreadmillAutoRunManager.this.initialSpeed);
            }
        }
    };
    private DeviceConnectionEventListener deviceConnectionEventListener = new DeviceConnectionEventAdapter() { // from class: com.treadly.Treadly.UI.TreadlyProfile.Settings.AutoRun.TreadmillAutoRunManager.4
        @Override // com.treadly.client.lib.sdk.Listeners.DeviceConnectionEventAdapter, com.treadly.client.lib.sdk.Listeners.DeviceConnectionEventListener
        public void onDeviceConnectionChanged(DeviceConnectionEvent deviceConnectionEvent) {
            TreadmillAutoRunManager.this.stop();
        }
    };

    private void changeSpeed() {
    }

    private void updateProgress() {
    }

    public boolean start(ComponentVersionInfo componentVersionInfo, int i) {
        return false;
    }

    public void startTimer() {
    }

    private TreadmillAutoRunManager() {
    }

    public boolean start(ComponentVersionInfo componentVersionInfo, double d) {
        stop();
        this.setSpeedValue = (float) d;
        this.componentVersion = componentVersionInfo;
        startTimerEndless();
        TreadlyClientLib.shared.addRequestEventListener(this.requestEventListener);
        TreadlyClientLib.shared.addDeviceConnectionEventListener(this.deviceConnectionEventListener);
        this.startTreadmill = true;
        return true;
    }

    public void stop() {
        powerOffDevice();
        if (this.speedTimer != null) {
            this.speedTimer.cancel();
        }
        this.speedTimer = null;
        this.startTime = null;
        this.setSpeedValue = 0.0f;
        this.currentDeviceStatus = null;
        this.queuedSpeedvalue = Utils.DOUBLE_EPSILON;
        TreadlyClientLib.shared.removeRequestEventListener(this.requestEventListener);
        TreadlyClientLib.shared.removeDeviceConnectionEventListener(this.deviceConnectionEventListener);
    }

    public boolean isRunning() {
        return this.speedTimer != null;
    }

    public void startTimerEndless() {
        this.speedTimer = new Timer();
        this.speedTimer.scheduleAtFixedRate(new TimerTask() { // from class: com.treadly.Treadly.UI.TreadlyProfile.Settings.AutoRun.TreadmillAutoRunManager.1
            @Override // java.util.TimerTask, java.lang.Runnable
            public void run() {
                TreadmillAutoRunManager.this.changeSpeedEndless();
            }
        }, this.runInterval, this.runInterval);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void changeSpeedEndless() {
        if (this.currentDeviceStatus == null) {
            return;
        }
        SpeedInfo speedInfo = this.currentDeviceStatus.getSpeedInfo();
        float minimumSpeed = speedInfo.getMinimumSpeed();
        float maximumSpeed = speedInfo.getMaximumSpeed();
        if (this.setSpeedValue > Utils.DOUBLE_EPSILON) {
            if (this.setSpeedValue > maximumSpeed) {
                minimumSpeed = maximumSpeed;
            } else if (this.setSpeedValue >= minimumSpeed) {
                minimumSpeed = this.setSpeedValue;
            }
            setSpeed(minimumSpeed);
            return;
        }
        setSpeed(Math.round((minimumSpeed + ((maximumSpeed - minimumSpeed) * new Random().nextDouble())) * 10.0d) / 10.0d);
    }

    public void showProgress(FragmentActivity fragmentActivity) {
        if (fragmentActivity == null) {
            return;
        }
        this.progressDialog = new AlertDialog.Builder(fragmentActivity).setTitle("Auto Run Mode").setMessage("").setPositiveButton("Stop", new DialogInterface.OnClickListener() { // from class: com.treadly.Treadly.UI.TreadlyProfile.Settings.AutoRun.TreadmillAutoRunManager.2
            @Override // android.content.DialogInterface.OnClickListener
            public void onClick(DialogInterface dialogInterface, int i) {
                TreadmillAutoRunManager.this.stop();
            }
        }).setCancelable(false).show();
    }

    public void dismissProgress() {
        if (this.progressDialog != null) {
            this.progressDialog.dismiss();
            this.progressDialog = null;
        }
    }

    private void powerOnDevice() {
        if (this.currentDeviceStatus == null || this.currentDeviceStatus.isPoweredOn()) {
            return;
        }
        TreadlyClientLib.shared.powerDevice();
        if (this.currentDeviceStatus.getMode() == DeviceMode.IDLE) {
            TreadlyClientLib.shared.powerDevice();
        }
    }

    private void powerOffDevice() {
        if (this.currentDeviceStatus != null && this.currentDeviceStatus.isPoweredOn()) {
            if (this.componentVersion != null && this.componentVersion.isGreaterThan(new VersionInfo(2, 5, 0))) {
                TreadlyClientLib.shared.resetPower();
            } else {
                TreadlyClientLib.shared.powerDevice();
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void setSpeed(double d) {
        if (this.currentDeviceStatus == null || !TreadlyClientLib.shared.isDeviceConnected()) {
            return;
        }
        if (this.currentDeviceStatus.isPoweredOn() && this.currentDeviceStatus.getSpeedInfo().getTargetSpeed() > 0.0f) {
            TreadlyClientLib.shared.setSpeed((float) d);
            this.statusCount = 0;
            return;
        }
        powerOnDevice();
        this.queuedSpeedvalue = Utils.DOUBLE_EPSILON;
        this.statusCount = 0;
    }
}
