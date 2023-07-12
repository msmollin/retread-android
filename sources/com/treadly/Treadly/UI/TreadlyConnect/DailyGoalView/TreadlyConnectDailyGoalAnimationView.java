package com.treadly.Treadly.UI.TreadlyConnect.DailyGoalView;

import android.content.Context;
import android.media.MediaPlayer;
import android.net.Uri;
import android.util.AttributeSet;
import com.yqritc.scalablevideoview.ScalableType;
import com.yqritc.scalablevideoview.ScalableVideoView;
import java.io.IOException;

/* loaded from: classes2.dex */
public class TreadlyConnectDailyGoalAnimationView extends ScalableVideoView {
    String currentFilename;
    boolean isPlaying;
    boolean isPrepared;

    public TreadlyConnectDailyGoalAnimationView(Context context) {
        super(context);
        this.isPlaying = false;
        this.isPrepared = false;
    }

    public TreadlyConnectDailyGoalAnimationView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        this.isPlaying = false;
        this.isPrepared = false;
    }

    public void configure(TreadlyDailyGoalType treadlyDailyGoalType) {
        try {
            setDataSource(getContext(), getGoalUrl(treadlyDailyGoalType));
            prepare(new MediaPlayer.OnPreparedListener() { // from class: com.treadly.Treadly.UI.TreadlyConnect.DailyGoalView.TreadlyConnectDailyGoalAnimationView.1
                @Override // android.media.MediaPlayer.OnPreparedListener
                public void onPrepared(MediaPlayer mediaPlayer) {
                    TreadlyConnectDailyGoalAnimationView.this.isPrepared = true;
                    if (TreadlyConnectDailyGoalAnimationView.this.isPlaying) {
                        TreadlyConnectDailyGoalAnimationView.this.start();
                    }
                }
            });
            setScalableType(ScalableType.CENTER_CROP);
            invalidate();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void play() {
        if (this.isPlaying) {
            return;
        }
        if (this.isPrepared) {
            start();
        }
        this.isPlaying = true;
    }

    @Override // com.yqritc.scalablevideoview.ScalableVideoView
    public void stop() {
        pause();
        this.isPlaying = false;
    }

    Uri getGoalUrl(TreadlyDailyGoalType treadlyDailyGoalType) {
        switch (treadlyDailyGoalType) {
            case dailyGoal50:
                return Uri.parse("https://dgwxv5s2i5zkb.cloudfront.net/daily_goal_animations/dailyGoal50_android.mp4");
            case dailyGoal100:
                return Uri.parse("https://dgwxv5s2i5zkb.cloudfront.net/daily_goal_animations/dailyGoal100_android.mp4");
            default:
                return null;
        }
    }
}
