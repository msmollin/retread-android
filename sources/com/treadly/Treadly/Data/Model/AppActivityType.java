package com.treadly.Treadly.Data.Model;

/* loaded from: classes2.dex */
public enum AppActivityType {
    appStart(0),
    deviceConnected(1),
    deviceDisconnected(2),
    homeTabSelected(3),
    groupTabSelected(4),
    connectTabSelected(5),
    inboxTabSelected(6),
    profileTabSelected(7),
    createGroupSelected(8),
    dailyGoal50(9),
    dailyGoal100(10),
    bannerInfoDisplayed(11),
    dailyGoalUpdated(12),
    lastRecorded7DaysChecked(13),
    lastRecorded14DaysChecked(14),
    lastRecorded30DaysChecked(15),
    androidBannerInfoDisplayed(16);
    
    private final int value;

    AppActivityType(int i) {
        this.value = i;
    }

    public static AppActivityType fromValue(int i) {
        AppActivityType[] values;
        for (AppActivityType appActivityType : values()) {
            if (appActivityType.value == i) {
                return appActivityType;
            }
        }
        return null;
    }

    public int getValue() {
        return this.value;
    }
}
