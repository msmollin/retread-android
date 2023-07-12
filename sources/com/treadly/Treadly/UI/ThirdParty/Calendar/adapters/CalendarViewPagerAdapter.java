package com.treadly.Treadly.UI.ThirdParty.Calendar.adapters;

import android.view.View;
import android.view.ViewGroup;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;
import com.treadly.Treadly.UI.ThirdParty.Calendar.interfaces.OnDateSelectedListener;
import com.treadly.Treadly.UI.ThirdParty.Calendar.interfaces.OnDayViewClickListener;
import com.treadly.Treadly.UI.ThirdParty.Calendar.objects.CalendarDate;
import com.treadly.Treadly.UI.ThirdParty.Calendar.objects.CalendarMonth;
import com.treadly.Treadly.UI.ThirdParty.Calendar.views.CalendarDayView;
import com.treadly.Treadly.UI.ThirdParty.Calendar.views.CalendarMonthView;
import java.util.Calendar;
import java.util.List;

/* loaded from: classes2.dex */
public class CalendarViewPagerAdapter extends PagerAdapter implements OnDayViewClickListener {
    private CalendarMonthView.CalendarMonthViewListener calendarMonthViewListener;
    private CalendarDate endDate;
    private List<CalendarMonth> mData;
    private OnDateSelectedListener mListener;
    private CalendarDate mSelectedDate = new CalendarDate(Calendar.getInstance());
    private ViewPager mViewPager;
    private CalendarDate startDate;

    private void decorateSelection(String str, boolean z) {
    }

    @Override // androidx.viewpager.widget.PagerAdapter
    public boolean isViewFromObject(View view, Object obj) {
        return view == obj;
    }

    public CalendarViewPagerAdapter(List<CalendarMonth> list, ViewPager viewPager, CalendarMonthView.CalendarMonthViewListener calendarMonthViewListener, CalendarDate calendarDate, CalendarDate calendarDate2) {
        this.mData = list;
        this.mViewPager = viewPager;
        this.calendarMonthViewListener = calendarMonthViewListener;
        this.startDate = calendarDate;
        this.endDate = calendarDate2;
    }

    @Override // androidx.viewpager.widget.PagerAdapter
    public Object instantiateItem(ViewGroup viewGroup, int i) {
        CalendarMonth calendarMonth = this.mData.get(i);
        CalendarMonthView calendarMonthView = new CalendarMonthView(viewGroup.getContext(), this.calendarMonthViewListener, this.startDate, this.endDate);
        calendarMonthView.setSelectedDate(this.mSelectedDate);
        calendarMonthView.setOnDayViewClickListener(this);
        calendarMonthView.buildView(calendarMonth);
        viewGroup.addView(calendarMonthView, 0);
        calendarMonthView.setTag(calendarMonth);
        return calendarMonthView;
    }

    @Override // androidx.viewpager.widget.PagerAdapter
    public int getCount() {
        return this.mData.size();
    }

    @Override // androidx.viewpager.widget.PagerAdapter
    public void destroyItem(ViewGroup viewGroup, int i, Object obj) {
        viewGroup.removeView((View) obj);
    }

    @Override // androidx.viewpager.widget.PagerAdapter
    public int getItemPosition(Object obj) {
        int indexOf = this.mData.indexOf((CalendarMonth) ((View) obj).getTag());
        if (indexOf >= 0) {
            return indexOf;
        }
        return -2;
    }

    public void addNext(CalendarMonth calendarMonth) {
        this.mData.add(calendarMonth);
        notifyDataSetChanged();
    }

    public void addPrev(CalendarMonth calendarMonth) {
        this.mData.add(0, calendarMonth);
        notifyDataSetChanged();
    }

    public String getItemPageHeader(int i) {
        return this.mData.get(i).toString();
    }

    public CalendarMonth getItem(int i) {
        return this.mData.get(i);
    }

    public void setOnDateSelectedListener(OnDateSelectedListener onDateSelectedListener) {
        this.mListener = onDateSelectedListener;
    }

    @Override // com.treadly.Treadly.UI.ThirdParty.Calendar.interfaces.OnDayViewClickListener
    public void onDayViewClick(CalendarDayView calendarDayView) {
        decorateSelection(this.mSelectedDate.toString(), false);
        this.mSelectedDate = calendarDayView.getDate();
        decorateSelection(this.mSelectedDate.toString(), true);
        if (this.mListener != null) {
            this.mListener.onDateSelected(new CalendarDate(this.mSelectedDate));
        }
    }

    public void updateCalendarMonths(List<CalendarMonth> list) {
        this.mData = list;
        notifyDataSetChanged();
    }
}
