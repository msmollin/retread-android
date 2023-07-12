package com.treadly.Treadly.UI.TreadmillControl.Data;

import com.github.mikephil.charting.utils.Utils;
import com.treadly.client.lib.sdk.Model.AuthenticationState;
import com.treadly.client.lib.sdk.Model.DistanceUnits;
import java.util.ArrayList;

/* loaded from: classes2.dex */
public class TreadlyControlContent {
    public int stepsCount = 0;
    public int dailyStepsGoal = 0;
    public int elapsedTime = 0;
    public double walkDistance = Utils.DOUBLE_EPSILON;
    public int burnedCaloriesCount = 0;
    public int dailyCaloriesGoal = 0;
    public double minimumSpeedValue = Utils.DOUBLE_EPSILON;
    public double maximumSpeedValue = 5.0d;
    public double speedValue = Utils.DOUBLE_EPSILON;
    public double averageSpeed = Utils.DOUBLE_EPSILON;
    public ArrayList<TreadlyControlSpeedDataPoint> speedDataSet = new ArrayList<>();
    public DistanceUnits speedUnits = DistanceUnits.MI;
    public TreadlyRunState runState = TreadlyRunState.STOPPED;
    public boolean isPairing = false;
    public double queuedSpeed = Utils.DOUBLE_EPSILON;
    public AuthenticationState authenticationState = AuthenticationState.unknown;
    public boolean hasSessionOwnership = false;

    public double progress() {
        return this.dailyStepsGoal == 0 ? Utils.DOUBLE_EPSILON : this.stepsCount / this.dailyStepsGoal;
    }
}
