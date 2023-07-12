package com.treadly.Treadly.UI.TreadlyVideo.Data;

import com.treadly.Treadly.UI.TreadmillControl.Data.TreadlyControlSpeedDataPoint;
import com.treadly.client.lib.sdk.Model.DistanceUnits;
import java.util.List;

/* loaded from: classes2.dex */
public class VideoServiceUserStatsInfo {
    public double averageSpeed;
    public int calories;
    public double distance;
    public int duration;
    public String name;
    public int order = 0;
    public double speed;
    public List<TreadlyControlSpeedDataPoint> speedDataSet;
    public DistanceUnits speedUnits;
    public int steps;
    public String userId;

    public VideoServiceUserStatsInfo(String str, String str2, int i, double d, int i2, double d2, double d3, List<TreadlyControlSpeedDataPoint> list, DistanceUnits distanceUnits, int i3) {
        this.userId = str;
        this.name = str2;
        this.calories = i;
        this.distance = d;
        this.steps = i2;
        this.duration = i3;
        this.speed = d2;
        this.speedUnits = distanceUnits;
        this.averageSpeed = d3;
        this.speedDataSet = list;
    }
}
