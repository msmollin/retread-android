package com.treadly.Treadly.UI.ThirdParty.Calendar.objects;

import com.treadly.Treadly.UI.ThirdParty.Calendar.utils.DateUtils;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/* loaded from: classes2.dex */
public class CalendarMonth {
    public static final int NUMBER_OF_DAYS_IN_WEEK = 7;
    public static final int NUMBER_OF_WEEKS_IN_MONTH = 6;
    private List<CalendarDate> mDays;
    private int mMonth;
    private int mYear;

    public CalendarMonth(Calendar calendar) {
        this.mMonth = calendar.get(2);
        this.mYear = calendar.get(1);
        createMonthDays();
    }

    public CalendarMonth(CalendarMonth calendarMonth, int i) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(calendarMonth.getYear(), calendarMonth.getMonth(), 1);
        calendar.add(2, i);
        this.mMonth = calendar.get(2);
        this.mYear = calendar.get(1);
        createMonthDays();
    }

    private void createMonthDays() {
        CalendarDate calendarDate = new CalendarDate(1, this.mMonth, this.mYear);
        calendarDate.addDays(1 - calendarDate.getDayOfWeek());
        this.mDays = new ArrayList();
        for (int i = 0; i < 42; i++) {
            this.mDays.add(new CalendarDate(calendarDate.getDay(), calendarDate.getMonth(), calendarDate.getYear()));
            calendarDate.addDays(1);
        }
    }

    public int getMonth() {
        return this.mMonth;
    }

    public int getYear() {
        return this.mYear;
    }

    public List<CalendarDate> getDays() {
        return this.mDays;
    }

    public String toString() {
        return DateUtils.monthToString(this.mMonth) + "  " + this.mYear;
    }
}
