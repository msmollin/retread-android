package com.treadly.Treadly.UI.ThirdParty.Calendar.views;

import android.content.Context;
import android.graphics.PorterDuff;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.TextView;
import androidx.annotation.AttrRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.viewpager.widget.ViewPager;
import com.github.mikephil.charting.utils.Utils;
import com.treadly.Treadly.Data.Model.UserProfileInfo;
import com.treadly.Treadly.Data.Model.UserStatsInfo;
import com.treadly.Treadly.R;
import com.treadly.Treadly.UI.ThirdParty.Calendar.adapters.CalendarViewPagerAdapter;
import com.treadly.Treadly.UI.ThirdParty.Calendar.interfaces.OnDateSelectedListener;
import com.treadly.Treadly.UI.ThirdParty.Calendar.objects.CalendarDate;
import com.treadly.Treadly.UI.ThirdParty.Calendar.objects.CalendarMonth;
import com.treadly.Treadly.UI.ThirdParty.Calendar.views.CalendarMonthView;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import org.eclipse.paho.client.mqttv3.MqttTopic;

/* loaded from: classes2.dex */
public class CustomCalendarView extends FrameLayout implements View.OnClickListener, CalendarMonthView.CalendarMonthViewListener {
    private List<UserStatsInfo> activityList;
    private ImageButton mButtonLeftArrow;
    private ImageButton mButtonRightArrow;
    private OnDateSelectedListener mListener;
    private ViewPager.OnPageChangeListener mPageChangeListener;
    private TextView mPagerTextMonth;
    private ViewPager mViewPager;
    private CalendarViewPagerAdapter mViewPagerAdapter;
    private UserProfileInfo userProfileInfo;

    public CustomCalendarView(@NonNull Context context) {
        super(context);
        this.activityList = new ArrayList();
        this.mPageChangeListener = new ViewPager.OnPageChangeListener() { // from class: com.treadly.Treadly.UI.ThirdParty.Calendar.views.CustomCalendarView.1
            @Override // androidx.viewpager.widget.ViewPager.OnPageChangeListener
            public void onPageScrolled(int i, float f, int i2) {
            }

            @Override // androidx.viewpager.widget.ViewPager.OnPageChangeListener
            public void onPageSelected(int i) {
                CustomCalendarView.this.updatePage(i);
            }

            @Override // androidx.viewpager.widget.ViewPager.OnPageChangeListener
            public void onPageScrollStateChanged(int i) {
                int currentItem = CustomCalendarView.this.mViewPager.getCurrentItem();
                CustomCalendarView.this.mPagerTextMonth.setText(CustomCalendarView.this.mViewPagerAdapter.getItemPageHeader(currentItem));
                if (i == 0) {
                    CustomCalendarView.this.mViewPagerAdapter.getCount();
                }
                CustomCalendarView.this.updatePage(currentItem);
            }
        };
        init();
    }

    public CustomCalendarView(@NonNull Context context, @Nullable AttributeSet attributeSet) {
        super(context, attributeSet);
        this.activityList = new ArrayList();
        this.mPageChangeListener = new ViewPager.OnPageChangeListener() { // from class: com.treadly.Treadly.UI.ThirdParty.Calendar.views.CustomCalendarView.1
            @Override // androidx.viewpager.widget.ViewPager.OnPageChangeListener
            public void onPageScrolled(int i, float f, int i2) {
            }

            @Override // androidx.viewpager.widget.ViewPager.OnPageChangeListener
            public void onPageSelected(int i) {
                CustomCalendarView.this.updatePage(i);
            }

            @Override // androidx.viewpager.widget.ViewPager.OnPageChangeListener
            public void onPageScrollStateChanged(int i) {
                int currentItem = CustomCalendarView.this.mViewPager.getCurrentItem();
                CustomCalendarView.this.mPagerTextMonth.setText(CustomCalendarView.this.mViewPagerAdapter.getItemPageHeader(currentItem));
                if (i == 0) {
                    CustomCalendarView.this.mViewPagerAdapter.getCount();
                }
                CustomCalendarView.this.updatePage(currentItem);
            }
        };
        init();
    }

    public CustomCalendarView(@NonNull Context context, @Nullable AttributeSet attributeSet, @AttrRes int i) {
        super(context, attributeSet, i);
        this.activityList = new ArrayList();
        this.mPageChangeListener = new ViewPager.OnPageChangeListener() { // from class: com.treadly.Treadly.UI.ThirdParty.Calendar.views.CustomCalendarView.1
            @Override // androidx.viewpager.widget.ViewPager.OnPageChangeListener
            public void onPageScrolled(int i2, float f, int i22) {
            }

            @Override // androidx.viewpager.widget.ViewPager.OnPageChangeListener
            public void onPageSelected(int i2) {
                CustomCalendarView.this.updatePage(i2);
            }

            @Override // androidx.viewpager.widget.ViewPager.OnPageChangeListener
            public void onPageScrollStateChanged(int i2) {
                int currentItem = CustomCalendarView.this.mViewPager.getCurrentItem();
                CustomCalendarView.this.mPagerTextMonth.setText(CustomCalendarView.this.mViewPagerAdapter.getItemPageHeader(currentItem));
                if (i2 == 0) {
                    CustomCalendarView.this.mViewPagerAdapter.getCount();
                }
                CustomCalendarView.this.updatePage(currentItem);
            }
        };
        init();
    }

    private void init() {
        inflate(getContext(), R.layout.view_custom_calendar, this);
        this.mViewPager = (ViewPager) findViewById(R.id.activity_main_view_pager);
        this.mPagerTextMonth = (TextView) findViewById(R.id.activity_main_pager_text_month);
        this.mPagerTextMonth.setText("");
        this.mButtonLeftArrow = (ImageButton) findViewById(R.id.activity_main_pager_button_left_arrow);
        this.mButtonRightArrow = (ImageButton) findViewById(R.id.activity_main_pager_button_right_arrow);
        this.mButtonLeftArrow.setOnClickListener(this);
        this.mButtonRightArrow.setOnClickListener(this);
        this.mButtonLeftArrow.setColorFilter(getContext().getColor(R.color.activity_calendar_button_disabled), PorterDuff.Mode.SRC_ATOP);
        this.mButtonRightArrow.setColorFilter(getContext().getColor(R.color.activity_calendar_button_disabled), PorterDuff.Mode.SRC_ATOP);
    }

    public void setData(List<UserStatsInfo> list, UserProfileInfo userProfileInfo) {
        this.activityList = list;
        this.userProfileInfo = userProfileInfo;
        updateCalendarView();
    }

    public void setOnDateSelectedListener(OnDateSelectedListener onDateSelectedListener) {
        this.mViewPagerAdapter.setOnDateSelectedListener(onDateSelectedListener);
    }

    @Override // android.view.View.OnClickListener
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.activity_main_pager_button_left_arrow /* 2131361874 */:
                this.mViewPager.setCurrentItem(this.mViewPager.getCurrentItem() - 1, true);
                return;
            case R.id.activity_main_pager_button_right_arrow /* 2131361875 */:
                this.mViewPager.setCurrentItem(this.mViewPager.getCurrentItem() + 1, true);
                return;
            default:
                return;
        }
    }

    private void buildCalendarView() {
        List<CalendarMonth> calendarMonths = getCalendarMonths();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(getActivityStart());
        this.mViewPagerAdapter = new CalendarViewPagerAdapter(calendarMonths, this.mViewPager, this, new CalendarDate(calendar), new CalendarDate(Calendar.getInstance()));
        this.mViewPager.setAdapter(this.mViewPagerAdapter);
        this.mViewPager.addOnPageChangeListener(this.mPageChangeListener);
        this.mViewPager.setOffscreenPageLimit(1);
        this.mViewPager.setCurrentItem(calendarMonths.size() > 0 ? calendarMonths.size() - 1 : 0);
        this.mPagerTextMonth.setText(this.mViewPagerAdapter.getItemPageHeader(calendarMonths.size() > 0 ? calendarMonths.size() - 1 : 0));
    }

    private void updateCalendarView() {
        buildCalendarView();
    }

    private List<CalendarMonth> getCalendarMonths() {
        ArrayList arrayList = new ArrayList();
        CalendarMonth calendarMonth = new CalendarMonth(Calendar.getInstance());
        int year = (calendarMonth.getYear() * 12) + calendarMonth.getMonth();
        Date activityStart = getActivityStart();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(activityStart);
        for (int i = ((calendar.get(1) * 12) + calendar.get(2)) - year; i < 0; i++) {
            arrayList.add(new CalendarMonth(calendarMonth, i));
        }
        arrayList.add(calendarMonth);
        return arrayList;
    }

    private Date getActivityStart() {
        UserStatsInfo userStatsInfo;
        Date date = new Date();
        if (this.activityList != null && this.activityList.size() > 0 && (userStatsInfo = this.activityList.get(this.activityList.size() - 1)) != null) {
            date = userStatsInfo.timestamp;
        }
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(5, 1);
        return calendar.getTime();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void updatePage(int i) {
        int color = getContext().getColor(R.color.activity_calendar_button_enabled);
        int color2 = getContext().getColor(R.color.activity_calendar_button_disabled);
        this.mButtonLeftArrow.setColorFilter((this.mViewPagerAdapter.getCount() <= 1 || i == 0) ? color2 : color, PorterDuff.Mode.SRC_ATOP);
        if (this.mViewPagerAdapter.getCount() > 1 && i != this.mViewPagerAdapter.getCount() - 1) {
            color2 = color;
        }
        this.mButtonRightArrow.setColorFilter(color2, PorterDuff.Mode.SRC_ATOP);
    }

    private void addNext() {
        this.mViewPagerAdapter.addNext(new CalendarMonth(this.mViewPagerAdapter.getItem(this.mViewPagerAdapter.getCount() - 1), 1));
    }

    private void addPrev() {
        this.mViewPagerAdapter.addPrev(new CalendarMonth(this.mViewPagerAdapter.getItem(0), -1));
    }

    @Override // com.treadly.Treadly.UI.ThirdParty.Calendar.views.CalendarMonthView.CalendarMonthViewListener
    public void onDecorateDayView(CalendarDayView calendarDayView, CalendarDate calendarDate, int i) {
        double d;
        double d2;
        boolean isToday = calendarDate.isToday();
        System.out.println("LGK :: onDecorateDay: " + calendarDate.getMonth() + MqttTopic.TOPIC_LEVEL_SEPARATOR + calendarDate.getDay() + MqttTopic.TOPIC_LEVEL_SEPARATOR + calendarDate.getYear() + ": " + i);
        boolean z = calendarDate.getMonth() == i;
        UserStatsInfo activityFor = getActivityFor(calendarDate);
        if (activityFor != null) {
            d = activityFor.getDailyGoal();
            d2 = activityFor.getDailyGoalProgress();
        } else {
            d = 0.0d;
            d2 = 0.0d;
        }
        calendarDayView.setDayView(isToday, d > Utils.DOUBLE_EPSILON ? ((float) d2) / ((float) d) : 0.0f, z);
    }

    public UserStatsInfo getActivityFor(CalendarDate calendarDate) {
        for (UserStatsInfo userStatsInfo : this.activityList) {
            if (userStatsInfo != null && userStatsInfo.timestamp != null) {
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(userStatsInfo.timestamp);
                int i = calendar.get(5);
                int i2 = calendar.get(2);
                int i3 = calendar.get(1);
                if (calendarDate.getDay() == i && calendarDate.getMonth() == i2 && calendarDate.getYear() == i3) {
                    return userStatsInfo;
                }
            }
        }
        return null;
    }
}
