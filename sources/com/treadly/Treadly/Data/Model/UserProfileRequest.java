package com.treadly.Treadly.Data.Model;

import com.treadly.client.lib.sdk.Model.DistanceUnits;

/* loaded from: classes2.dex */
public class UserProfileRequest {
    public int age;
    public int birthdateYear;
    public int caloriesGoal;
    public String deviceAddress;
    public double distanceGoal;
    public double durationGoal;
    public String gender;
    public UserDailyGoalType goalType;
    public double height;
    public int stepsGoal;
    public DistanceUnits units;
    public double weight;

    public UserProfileRequest(int i, double d, double d2, String str, UserDailyGoalType userDailyGoalType, int i2, int i3, double d3, double d4, int i4, DistanceUnits distanceUnits, String str2) {
        this.age = i;
        this.weight = d;
        this.height = d2;
        this.gender = str;
        this.caloriesGoal = i2;
        this.stepsGoal = i3;
        this.distanceGoal = d3;
        this.durationGoal = d4;
        this.birthdateYear = i4;
        this.units = distanceUnits;
        this.goalType = userDailyGoalType;
        this.deviceAddress = str2;
    }
}
