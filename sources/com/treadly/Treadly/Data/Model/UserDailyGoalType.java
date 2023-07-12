package com.treadly.Treadly.Data.Model;

/* loaded from: classes2.dex */
public enum UserDailyGoalType {
    none(0),
    calories(1),
    steps(2),
    distance(3),
    duration(4);
    
    public static final int CALORIES_INCREMENT = 5;
    public static final int CALORIES_MAX = 5000;
    public static final float DISTANCE_INCREMENT = 0.1f;
    public static final float DISTANCE_MAX_KILOMETERS = 32.0f;
    public static final float DISTANCE_MAX_MILES = 20.0f;
    public static final int DURATION_INCREMENT = 5;
    public static final int DURATION_MAX = 1440;
    public static final int STEPS_INCREMENT = 100;
    public static final int STEPS_MAX = 100000;
    private int value;

    UserDailyGoalType(int i) {
        this.value = i;
    }

    public static UserDailyGoalType fromValue(int i) {
        UserDailyGoalType[] values;
        for (UserDailyGoalType userDailyGoalType : values()) {
            if (userDailyGoalType.value == i) {
                return userDailyGoalType;
            }
        }
        return null;
    }

    public int getValue() {
        return this.value;
    }
}
