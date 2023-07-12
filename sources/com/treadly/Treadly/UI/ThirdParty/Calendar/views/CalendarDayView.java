package com.treadly.Treadly.UI.ThirdParty.Calendar.views;

import android.content.Context;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.core.content.ContextCompat;
import com.treadly.Treadly.R;
import com.treadly.Treadly.UI.ThirdParty.Calendar.objects.CalendarDate;
import com.treadly.Treadly.UI.TreadlyActivity.ActivityCalendar.TreadlyActivityCalendarProgressRing;

/* loaded from: classes2.dex */
public class CalendarDayView extends LinearLayout {
    private View completeView;
    private View defaultRingView;
    private CalendarDate mCalendarDate;
    private View mLayoutBackground;
    private TextView mTextDay;
    private TreadlyActivityCalendarProgressRing progressRingView;
    private View todayRingView;

    public CalendarDayView(Context context, CalendarDate calendarDate) {
        super(context);
        this.mCalendarDate = calendarDate;
        init();
    }

    private void init() {
        inflate(getContext(), R.layout.view_calendar_day, this);
        this.mLayoutBackground = findViewById(R.id.view_calendar_day_layout_background);
        this.mLayoutBackground.setBackgroundResource(R.drawable.activity_calendar_layout_drawable);
        this.defaultRingView = findViewById(R.id.view_calendar_day_default_ring);
        this.progressRingView = (TreadlyActivityCalendarProgressRing) findViewById(R.id.view_calendar_day_layout_progress);
        this.progressRingView.setMax(1);
        this.progressRingView.setMin(0);
        this.progressRingView.setStrokeWidth(16.0f);
        this.progressRingView.setColor(getContext().getColor(R.color.activity_calendar_progress_ring));
        this.todayRingView = findViewById(R.id.view_calendar_today_ring);
        this.completeView = findViewById(R.id.view_calendar_day_complete);
        this.mTextDay = (TextView) findViewById(R.id.view_calendar_day_text);
        TextView textView = this.mTextDay;
        textView.setText("" + this.mCalendarDate.getDay());
    }

    public CalendarDate getDate() {
        return this.mCalendarDate;
    }

    public void setThisMothTextColor() {
        this.mTextDay.setTextColor(ContextCompat.getColor(getContext(), R.color.white));
    }

    public void setOtherMothTextColor() {
        this.mTextDay.setTextColor(ContextCompat.getColor(getContext(), R.color.white));
    }

    public void setPurpleSolidOvalBackground() {
        this.completeView.setVisibility(0);
    }

    public void setDayView(boolean z, float f, boolean z2) {
        int i = (f > 1.0f ? 1 : (f == 1.0f ? 0 : -1));
        this.completeView.setVisibility(i >= 0 ? 0 : 8);
        this.progressRingView.setVisibility(i >= 0 ? 4 : 0);
        this.progressRingView.setProgress(f);
        this.todayRingView.setVisibility(z ? 0 : 8);
        if (i >= 0) {
            this.mTextDay.setTextColor(getContext().getColor(R.color.activity_calendar_complete_text));
        } else if (z) {
            this.mTextDay.setTextColor(getContext().getColor(R.color.activity_calendar_today_text));
        } else {
            this.mTextDay.setTextColor(getContext().getColor(R.color.activity_calendar_text));
        }
        setVisibility(z2 ? 0 : 4);
    }
}
