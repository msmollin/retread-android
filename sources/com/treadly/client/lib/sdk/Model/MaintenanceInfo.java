package com.treadly.client.lib.sdk.Model;

/* loaded from: classes2.dex */
public class MaintenanceInfo {
    private boolean maintenaceRequired;
    private int maxThreshold;
    private int minThreshold;
    private int stepThreshold;
    private int steps;

    public MaintenanceInfo(int i, int i2, boolean z, int i3, int i4) {
        this.maxThreshold = 0;
        this.minThreshold = 0;
        this.stepThreshold = i;
        this.steps = i2;
        this.maintenaceRequired = z;
        this.maxThreshold = i3;
        this.minThreshold = i4;
    }

    public int getStepThreshold() {
        return this.stepThreshold;
    }

    public int getSteps() {
        return this.steps;
    }

    public boolean isMaintenaceRequired() {
        return this.maintenaceRequired;
    }

    public void setStepThreshold(int i) {
        this.stepThreshold = i;
    }

    public void setSteps(int i) {
        this.steps = i;
    }

    public void setMaintenaceRequired(boolean z) {
        this.maintenaceRequired = z;
    }

    public int getMaxThreshold() {
        return this.maxThreshold;
    }

    public int getMinThreshold() {
        return this.minThreshold;
    }

    public void setMaxThreshold(int i) {
        this.maxThreshold = i;
    }

    public void setMinThreshold(int i) {
        this.minThreshold = i;
    }
}
