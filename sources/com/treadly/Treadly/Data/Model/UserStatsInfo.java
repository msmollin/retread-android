package com.treadly.Treadly.Data.Model;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.github.mikephil.charting.utils.Utils;
import java.util.Date;

/* loaded from: classes2.dex */
public class UserStatsInfo implements Comparable<UserStatsInfo> {
    public int calories;
    public int caloriesGoal;
    public UserDailyGoalType dailyGoalType;
    public int dayOfWeek;
    public double distance;
    public double distanceGoal;
    public double duration;
    public double durationGoal;
    public int statsCount;
    public int steps;
    public int stepsGoal;
    public Date timestamp;

    public UserStatsInfo(Date date, int i, int i2, double d, double d2, int i3) {
        this.calories = i2;
        this.duration = d2;
        this.steps = i3;
        this.distance = d;
        this.timestamp = date;
        this.dayOfWeek = i;
        this.caloriesGoal = 0;
        this.stepsGoal = 0;
        this.distanceGoal = Utils.DOUBLE_EPSILON;
        this.durationGoal = Utils.DOUBLE_EPSILON;
        this.dailyGoalType = UserDailyGoalType.none;
    }

    public UserStatsInfo(Date date, int i, int i2, double d, double d2, int i3, int i4, int i5, double d3, double d4, UserDailyGoalType userDailyGoalType, @Nullable int i6) {
        this.calories = i2;
        this.duration = d2;
        this.steps = i3;
        this.distance = d;
        this.timestamp = date;
        this.dayOfWeek = i;
        this.caloriesGoal = i4;
        this.stepsGoal = i5;
        this.distanceGoal = d3;
        this.durationGoal = d4;
        this.dailyGoalType = userDailyGoalType;
        this.statsCount = i6;
    }

    @Override // java.lang.Comparable
    public int compareTo(@NonNull UserStatsInfo userStatsInfo) {
        return userStatsInfo.timestamp.compareTo(this.timestamp);
    }

    public double getDailyGoal() {
        switch (this.dailyGoalType) {
            case calories:
                return this.caloriesGoal;
            case steps:
                return this.stepsGoal;
            case distance:
                return this.distanceGoal;
            case duration:
                return this.durationGoal;
            default:
                return Utils.DOUBLE_EPSILON;
        }
    }

    public double getDailyGoalProgress() {
        switch (this.dailyGoalType) {
            case calories:
                return this.calories;
            case steps:
                return this.steps;
            case distance:
                return this.distance;
            case duration:
                return this.duration;
            default:
                return Utils.DOUBLE_EPSILON;
        }
    }
}
