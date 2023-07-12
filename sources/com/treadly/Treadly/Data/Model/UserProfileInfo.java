package com.treadly.Treadly.Data.Model;

import com.github.mikephil.charting.utils.Utils;
import com.treadly.client.lib.sdk.Model.DistanceUnits;
import java.util.Locale;

/* loaded from: classes2.dex */
public class UserProfileInfo {
    private static final double miToKm = 1.609344d;
    public int age;
    public int birthdateYear;
    public int caloriesTotal;
    public String city;
    public int currentCaloriesGoal;
    public UserDailyGoalType currentDailyGoalType;
    public double currentDistanceGoal;
    public double currentDurationGoal;
    public int currentStepsGoal;
    public String descriptionProfile;
    public String deviceAddress;
    public double distanceTotal;
    public UserInfo[] friends;
    public int friendsCount;
    public String gender;
    public double height;
    public boolean isBlocked;
    public boolean isFriend;
    public String name;
    public int settingCaloriesGoal;
    public UserDailyGoalType settingDailyGoalType;
    public double settingDistanceGoal;
    public double settingDurationGoal;
    public int settingStepsGoal;
    public int stepsTotal;
    public String timezoneIdentifier;
    public DistanceUnits units;
    public String userId;
    public double weight;

    public int caloriesGoal() {
        return this.currentCaloriesGoal >= 0 ? this.currentCaloriesGoal : this.settingCaloriesGoal;
    }

    public int stepsGoal() {
        return this.currentStepsGoal >= 0 ? this.currentStepsGoal : this.settingStepsGoal;
    }

    public double distanceGoal() {
        return this.currentDistanceGoal >= Utils.DOUBLE_EPSILON ? this.currentDistanceGoal : this.settingDistanceGoal;
    }

    public double durationGoal() {
        return this.currentDurationGoal >= Utils.DOUBLE_EPSILON ? this.currentDurationGoal : this.settingDurationGoal;
    }

    public UserDailyGoalType dailyGoalType() {
        return this.currentDailyGoalType != UserDailyGoalType.none ? this.currentDailyGoalType : this.settingDailyGoalType;
    }

    public UserProfileInfo(String str, int i, double d, double d2, String str2, String str3, String str4, UserDailyGoalType userDailyGoalType, UserDailyGoalType userDailyGoalType2, int i2, int i3, int i4, int i5, double d3, double d4, double d5, double d6, int i6, int i7, double d7, int i8, String str5, DistanceUnits distanceUnits, String str6) {
        this.currentCaloriesGoal = -1;
        this.currentStepsGoal = -1;
        this.currentDistanceGoal = -1.0d;
        this.currentDurationGoal = -1.0d;
        this.currentDailyGoalType = UserDailyGoalType.none;
        this.name = str;
        this.age = i;
        this.weight = d;
        this.height = d2;
        this.gender = str2;
        this.descriptionProfile = str3;
        this.city = str4;
        this.currentDailyGoalType = userDailyGoalType2;
        this.settingDailyGoalType = userDailyGoalType;
        this.currentCaloriesGoal = i3;
        this.settingCaloriesGoal = i2;
        this.currentStepsGoal = i5;
        this.settingStepsGoal = i4;
        this.currentDistanceGoal = d4;
        this.settingDistanceGoal = d3;
        this.currentDurationGoal = d6;
        this.settingDurationGoal = d5;
        this.caloriesTotal = i6;
        this.stepsTotal = i7;
        this.distanceTotal = d7;
        this.birthdateYear = i8;
        this.timezoneIdentifier = str5;
        this.units = distanceUnits;
        this.deviceAddress = str6;
    }

    public String getAgeString() {
        return String.format(Locale.getDefault(), "%d", Integer.valueOf(this.age));
    }

    public String getWeightString() {
        return String.format(Locale.getDefault(), "%.0f", Double.valueOf(this.weight));
    }

    public String getHeightString() {
        return String.format(Locale.getDefault(), "%.1f", Double.valueOf(this.height));
    }

    public String getCaloriesGoalString() {
        return String.format(Locale.getDefault(), "%d", Integer.valueOf(caloriesGoal()));
    }

    public String getStepsGoalString() {
        return String.format(Locale.getDefault(), "%d", Integer.valueOf(stepsGoal()));
    }

    public String getCaloriesTotalString() {
        return String.format(Locale.getDefault(), "%d", Integer.valueOf(this.caloriesTotal));
    }

    public String getStepsTotalString() {
        return String.format(Locale.getDefault(), "%d", Integer.valueOf(this.stepsTotal));
    }

    public String getDistanceTotalString() {
        return String.format(Locale.getDefault(), "%.1f", Double.valueOf(this.distanceTotal));
    }

    public double getDailyGoal() {
        return getDailyGoal(true);
    }

    public double getDailyGoal(boolean z) {
        switch (dailyGoalType()) {
            case calories:
                return caloriesGoal();
            case steps:
                return stepsGoal();
            case distance:
                return (this.units != DistanceUnits.KM || z) ? distanceGoal() : distanceGoal() * miToKm;
            case duration:
                return durationGoal();
            default:
                return Utils.DOUBLE_EPSILON;
        }
    }
}
