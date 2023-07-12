package com.treadly.Treadly.UI.ThirdParty.Calendar.views;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.GridLayout;
import android.widget.TextView;
import com.treadly.Treadly.R;
import com.treadly.Treadly.UI.ThirdParty.Calendar.interfaces.OnDayViewClickListener;
import com.treadly.Treadly.UI.ThirdParty.Calendar.objects.CalendarDate;
import com.treadly.Treadly.UI.ThirdParty.Calendar.objects.CalendarMonth;
import com.treadly.Treadly.UI.ThirdParty.Calendar.utils.Utils;

/* loaded from: classes2.dex */
public class CalendarMonthView extends FrameLayout implements View.OnClickListener {
    private CalendarDate endDate;
    private CalendarMonthViewListener listener;
    private GridLayout mGridLayout;
    private ViewGroup mLayoutDays;
    private OnDayViewClickListener mListener;
    private CalendarDate mSelectedDate;
    private CalendarDate startDate;

    /* loaded from: classes2.dex */
    public interface CalendarMonthViewListener {
        void onDecorateDayView(CalendarDayView calendarDayView, CalendarDate calendarDate, int i);
    }

    public CalendarMonthView(Context context, CalendarMonthViewListener calendarMonthViewListener, CalendarDate calendarDate, CalendarDate calendarDate2) {
        super(context);
        this.listener = calendarMonthViewListener;
        this.startDate = calendarDate;
        this.endDate = calendarDate2;
        init();
    }

    public void setOnDayViewClickListener(OnDayViewClickListener onDayViewClickListener) {
        this.mListener = onDayViewClickListener;
    }

    public void setSelectedDate(CalendarDate calendarDate) {
        this.mSelectedDate = calendarDate;
    }

    private void init() {
        inflate(getContext(), R.layout.view_calendar_month, this);
        this.mGridLayout = (GridLayout) findViewById(R.id.view_calendar_month_grid);
        this.mLayoutDays = (ViewGroup) findViewById(R.id.view_calendar_month_layout_days);
    }

    public void buildView(CalendarMonth calendarMonth) {
        buildDaysLayout();
        buildGridView(calendarMonth);
    }

    private void buildDaysLayout() {
        String[] stringArray = getResources().getStringArray(R.array.days_sunday_array);
        for (int i = 0; i < this.mLayoutDays.getChildCount(); i++) {
            ((TextView) this.mLayoutDays.getChildAt(i)).setText(stringArray[i]);
        }
    }

    private void buildGridView(CalendarMonth calendarMonth) {
        this.mGridLayout.setRowCount(6);
        this.mGridLayout.setColumnCount(7);
        int screenWidth = Utils.getScreenWidth(getContext()) / 7;
        for (CalendarDate calendarDate : calendarMonth.getDays()) {
            GridLayout.LayoutParams layoutParams = new GridLayout.LayoutParams();
            layoutParams.width = screenWidth;
            layoutParams.height = -2;
            CalendarDayView calendarDayView = new CalendarDayView(getContext(), calendarDate);
            calendarDayView.setContentDescription(calendarDate.toString());
            calendarDayView.setLayoutParams(layoutParams);
            calendarDayView.setOnClickListener(isWithinRange(calendarDate) ? this : null);
            decorateDayView(calendarDayView, calendarDate, calendarMonth.getMonth());
            this.mGridLayout.addView(calendarDayView);
        }
    }

    private boolean isWithinRange(CalendarDate calendarDate) {
        return (calendarDate == null || isBefore(calendarDate, this.startDate) || isAfter(calendarDate, this.endDate)) ? false : true;
    }

    private boolean isBefore(CalendarDate calendarDate, CalendarDate calendarDate2) {
        return calendarDate.getYear() != calendarDate2.getYear() ? calendarDate.getYear() < calendarDate2.getYear() : calendarDate.getMonth() != calendarDate2.getMonth() ? calendarDate.getMonth() < calendarDate2.getMonth() : calendarDate.getDay() != calendarDate2.getDay() && calendarDate.getDay() < calendarDate2.getDay();
    }

    private boolean isAfter(CalendarDate calendarDate, CalendarDate calendarDate2) {
        return calendarDate.getYear() != calendarDate2.getYear() ? calendarDate.getYear() > calendarDate2.getYear() : calendarDate.getMonth() != calendarDate2.getMonth() ? calendarDate.getMonth() > calendarDate2.getMonth() : calendarDate.getDay() != calendarDate2.getDay() && calendarDate.getDay() > calendarDate2.getDay();
    }

    private void decorateDayView(CalendarDayView calendarDayView, CalendarDate calendarDate, int i) {
        if (this.listener != null) {
            this.listener.onDecorateDayView(calendarDayView, calendarDate, i);
        }
    }

    @Override // android.view.View.OnClickListener
    public void onClick(View view) {
        if (this.mListener != null) {
            this.mListener.onDayViewClick((CalendarDayView) view);
        }
    }
}
