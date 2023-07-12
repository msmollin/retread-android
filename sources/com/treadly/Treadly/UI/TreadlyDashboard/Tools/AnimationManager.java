package com.treadly.Treadly.UI.TreadlyDashboard.Tools;

import android.content.Context;
import android.net.Uri;
import android.widget.VideoView;

/* loaded from: classes2.dex */
public class AnimationManager {
    static AnimationManager shared = new AnimationManager();
    public Context context;
    public VideoView dailyGoal50Player = null;
    public VideoView dailyGoal100Player = null;

    public static AnimationManager getInstance() {
        return shared;
    }

    public AnimationManager() {
        loadDailyGoalAnimations();
    }

    public void loadDailyGoalAnimations() {
        if (Uri.parse("https://dgwxv5s2i5zkb.cloudfront.net/daily_goal_animations/dailyGoal50.mp4") != null) {
            this.dailyGoal50Player.start();
            this.dailyGoal50Player.pause();
        }
        if (Uri.parse("https://dgwxv5s2i5zkb.cloudfront.net/daily_goal_animations/dailyGoal100.mp4") != null) {
            this.dailyGoal100Player.start();
            this.dailyGoal100Player.pause();
        }
    }
}
