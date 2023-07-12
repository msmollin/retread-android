package com.treadly.Treadly.Data.Model;

import androidx.annotation.Nullable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/* loaded from: classes2.dex */
public class UserRunningSessionInfo extends UserStatsInfo {
    public Date finishedAt;
    public String log_id;
    public List<UserRunningSessionParticipantInfo> particpants;
    public List<UserRunningSessionSegmentInfo> segments;

    public UserRunningSessionInfo(Date date, int i, double d, double d2, int i2) {
        super(date, 0, i, d, d2, i2);
        this.segments = new ArrayList();
        this.particpants = new ArrayList();
        this.finishedAt = date;
        this.log_id = "";
    }

    public UserRunningSessionInfo(Date date, int i, double d, double d2, int i2, String str) {
        super(date, 0, i, d, d2, i2);
        this.segments = new ArrayList();
        this.particpants = new ArrayList();
        this.finishedAt = date;
        this.log_id = str;
    }

    public UserRunningSessionInfo(Date date, int i, double d, double d2, int i2, int i3, int i4, double d3, double d4, UserDailyGoalType userDailyGoalType, @Nullable int i5) {
        super(date, 0, i, d, d2, i2, i3, i4, d3, d4, userDailyGoalType, i5);
        this.segments = new ArrayList();
        this.particpants = new ArrayList();
        this.finishedAt = date;
        this.log_id = "";
    }
}
