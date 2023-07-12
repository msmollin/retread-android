package com.treadly.Treadly.UI.ThirdParty.Calendar.objects;

import com.facebook.appevents.AppEventsConstants;
import com.treadly.Treadly.UI.ThirdParty.Calendar.utils.DateUtils;
import java.util.Calendar;
import org.eclipse.paho.client.mqttv3.MqttTopic;

/* loaded from: classes2.dex */
public class CalendarDate {
    private int mDay;
    private int mMonth;
    private int mYear;

    /* JADX INFO: Access modifiers changed from: package-private */
    public CalendarDate(int i, int i2, int i3) {
        this.mDay = i;
        this.mMonth = i2;
        this.mYear = i3;
    }

    public CalendarDate(Calendar calendar) {
        this(calendar.get(5), calendar.get(2), calendar.get(1));
    }

    public CalendarDate(CalendarDate calendarDate) {
        this(calendarDate.getDay(), calendarDate.getMonth(), calendarDate.getYear());
    }

    public int getDay() {
        return this.mDay;
    }

    public int getMonth() {
        return this.mMonth;
    }

    public int getYear() {
        return this.mYear;
    }

    public Calendar getCalender() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(11, 0);
        calendar.set(12, 0);
        calendar.set(13, 0);
        calendar.set(14, 0);
        calendar.set(this.mYear, this.mMonth, this.mDay);
        return calendar;
    }

    public long getMillis() {
        return getCalender().getTimeInMillis();
    }

    public int getDayOfWeek() {
        return getCalender().get(7);
    }

    public boolean isToday() {
        Calendar calendar = Calendar.getInstance();
        return this.mYear == calendar.get(1) && this.mMonth == calendar.get(2) && this.mDay == calendar.get(5);
    }

    public boolean isDateEqual(CalendarDate calendarDate) {
        return this.mYear == calendarDate.getYear() && this.mMonth == calendarDate.getMonth() && this.mDay == calendarDate.getDay();
    }

    public void addDays(int i) {
        Calendar calender = getCalender();
        calender.add(5, i);
        this.mDay = calender.get(5);
        this.mMonth = calender.get(2);
        this.mYear = calender.get(1);
    }

    public String toString() {
        return dayToString() + MqttTopic.TOPIC_LEVEL_SEPARATOR + monthToString() + MqttTopic.TOPIC_LEVEL_SEPARATOR + yearToString();
    }

    public String dayToString() {
        if (this.mDay < 10) {
            return AppEventsConstants.EVENT_PARAM_VALUE_NO + this.mDay;
        }
        return "" + this.mDay;
    }

    public String monthToString() {
        if (this.mMonth + 1 < 10) {
            return AppEventsConstants.EVENT_PARAM_VALUE_NO + (this.mMonth + 1);
        }
        return "" + (this.mMonth + 1);
    }

    public String yearToString() {
        return String.valueOf(this.mYear);
    }

    public String monthToStringName() {
        return DateUtils.monthToString(this.mMonth);
    }

    public String dayOfWeekToStringName() {
        return DateUtils.dayOfWeekToString(getCalender().get(7));
    }
}
