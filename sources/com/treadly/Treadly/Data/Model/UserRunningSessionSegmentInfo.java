package com.treadly.Treadly.Data.Model;

import androidx.annotation.NonNull;
import java.util.Date;

/* loaded from: classes2.dex */
public class UserRunningSessionSegmentInfo implements Comparable<UserRunningSessionSegmentInfo> {
    public Date createdAt;
    public double speed;
    public int steps;

    public UserRunningSessionSegmentInfo(Date date, int i, double d) {
        this.createdAt = date;
        this.steps = i;
        this.speed = d;
    }

    @Override // java.lang.Comparable
    public int compareTo(@NonNull UserRunningSessionSegmentInfo userRunningSessionSegmentInfo) {
        return this.createdAt.compareTo(userRunningSessionSegmentInfo.createdAt);
    }
}
