package com.treadly.Treadly.UI.TreadlyVideo.PublicStream.Components.HostInfo;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.FrameLayout;
import android.widget.TextView;
import com.treadly.Treadly.Data.Model.UserInfo;
import com.treadly.Treadly.R;
import com.treadly.Treadly.UI.TreadlyVideo.Data.VideoServiceUserStatsInfo;
import java.text.DecimalFormat;
import java.util.Locale;
import org.joda.time.DateTimeConstants;

/* loaded from: classes2.dex */
public class TreadlyPublicStreamUserStatsInfoView extends FrameLayout {
    private TextView caloriesTextView;
    private DecimalFormat distanceFormatter;
    private TextView distanceTextView;
    private TextView durationTextView;
    private DecimalFormat onlineFormatter;
    private TextView onlineTextView;
    private DecimalFormat stepsFormatter;
    private TextView stepsTextView;
    private TextView titleTextView;

    public TreadlyPublicStreamUserStatsInfoView(Context context) {
        super(context);
        this.distanceFormatter = new DecimalFormat("0.0");
        this.stepsFormatter = new DecimalFormat("#,###");
        this.onlineFormatter = new DecimalFormat("#,###");
        init();
    }

    public TreadlyPublicStreamUserStatsInfoView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        this.distanceFormatter = new DecimalFormat("0.0");
        this.stepsFormatter = new DecimalFormat("#,###");
        this.onlineFormatter = new DecimalFormat("#,###");
        init();
    }

    private void init() {
        if (getContext() == null) {
            return;
        }
        inflate(getContext(), R.layout.view_treadly_public_stream_user_stats_info, this);
        this.titleTextView = (TextView) findViewById(R.id.title_text_view);
        this.durationTextView = (TextView) findViewById(R.id.duration_text_view);
        this.onlineTextView = (TextView) findViewById(R.id.online_text_view);
        this.caloriesTextView = (TextView) findViewById(R.id.calories_text_view);
        this.distanceTextView = (TextView) findViewById(R.id.distance_text_view);
        this.stepsTextView = (TextView) findViewById(R.id.steps_text_view);
        this.distanceTextView.setText("Distance\n");
        this.stepsTextView.setText("Steps\n");
        this.caloriesTextView.setText("Cal\n");
        this.durationTextView.setText("Duration\n");
    }

    public void setHostInfo(UserInfo userInfo, int i) {
        this.onlineTextView.setVisibility(0);
        this.titleTextView.setText(userInfo.name);
        this.onlineTextView.setText(String.format("Online\n%s", this.onlineFormatter.format(i)));
    }

    public void setUserInfo(UserInfo userInfo) {
        this.onlineTextView.setVisibility(4);
        this.titleTextView.setText(userInfo.name);
    }

    public void setHostStats(VideoServiceUserStatsInfo videoServiceUserStatsInfo) {
        setStats(videoServiceUserStatsInfo);
    }

    public void setUserStats(VideoServiceUserStatsInfo videoServiceUserStatsInfo) {
        setStats(videoServiceUserStatsInfo);
    }

    public void setStats(VideoServiceUserStatsInfo videoServiceUserStatsInfo) {
        if (videoServiceUserStatsInfo == null) {
            return;
        }
        this.distanceTextView.setText(String.format("Distance\n%s", this.distanceFormatter.format(videoServiceUserStatsInfo.distance)));
        this.stepsTextView.setText(String.format("Steps\n%s", this.stepsFormatter.format(videoServiceUserStatsInfo.steps)));
        this.caloriesTextView.setText(String.format("Cal\n%s", this.stepsFormatter.format(videoServiceUserStatsInfo.calories)));
        this.durationTextView.setText(String.format(Locale.getDefault(), "Duration\n%02d:%02d", Integer.valueOf(videoServiceUserStatsInfo.duration / 60), Integer.valueOf((videoServiceUserStatsInfo.duration % DateTimeConstants.SECONDS_PER_HOUR) % 60)));
    }
}
