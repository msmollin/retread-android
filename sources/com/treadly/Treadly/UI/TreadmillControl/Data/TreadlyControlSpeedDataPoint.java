package com.treadly.Treadly.UI.TreadmillControl.Data;

import com.github.mikephil.charting.utils.Utils;

/* loaded from: classes2.dex */
public class TreadlyControlSpeedDataPoint {
    public double speed;
    public int time;

    public TreadlyControlSpeedDataPoint(double d, int i) {
        this.speed = d;
        this.time = i;
    }

    public TreadlyControlSpeedDataPoint() {
        this.speed = Utils.DOUBLE_EPSILON;
        this.time = 0;
    }
}
