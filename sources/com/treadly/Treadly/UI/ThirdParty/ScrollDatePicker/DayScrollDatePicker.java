package com.treadly.Treadly.UI.ThirdParty.ScrollDatePicker;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.treadly.Treadly.R;
import java.util.Calendar;
import org.joda.time.LocalDate;

/* loaded from: classes2.dex */
public class DayScrollDatePicker extends LinearLayout implements TitleValueCallback, OnChildDateSelectedListener {
    private LinearLayoutManager layoutManager;
    private DayScrollDatePickerAdapter mAdapter;
    private int mBaseTextColor;
    private RecyclerView mDayRecyclerView;
    private TextView mFullDateTextView;
    private OnDateSelectedListener mListener;
    private TextView mMonthTextView;
    private boolean mShowFullDate;
    private boolean mShowTitle;
    private Style mStyle;

    public DayScrollDatePicker(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        LayoutInflater.from(context).inflate(R.layout.date_picker_scroll_day, (ViewGroup) this, true);
        getViewElements();
        setAttributeValues(context.getTheme().obtainStyledAttributes(attributeSet, R.styleable.ScrollDatePicker, 0, 0));
        DayScrollDatePickerViewHolder.onDateSelected(this);
        initView();
    }

    private void getViewElements() {
        this.mMonthTextView = (TextView) findViewById(R.id.date_picker_scroll_day_month);
        this.mFullDateTextView = (TextView) findViewById(R.id.date_picker_scroll_day_full_date);
        this.mDayRecyclerView = (RecyclerView) findViewById(R.id.date_picker_scroll_day_recycler_view);
    }

    private void setAttributeValues(TypedArray typedArray) {
        try {
            this.mBaseTextColor = typedArray.getColor(1, getResources().getColor(R.color.default_base_text));
            int color = typedArray.getColor(0, getResources().getColor(R.color.default_base));
            int color2 = typedArray.getColor(3, getResources().getColor(R.color.default_selected_text));
            int color3 = typedArray.getColor(2, getResources().getColor(R.color.default_selected));
            this.mShowTitle = typedArray.getBoolean(5, true);
            this.mShowFullDate = typedArray.getBoolean(4, true);
            typedArray.recycle();
            this.mStyle = new Style(color3, color, color2, this.mBaseTextColor, Util.setDrawableBackgroundColor(getResources().getDrawable(R.drawable.bg_circle_drawable), color), Util.setDrawableBackgroundColor(getResources().getDrawable(R.drawable.bg_circle_drawable), color3));
        } catch (Throwable th) {
            typedArray.recycle();
            throw th;
        }
    }

    private void initView() {
        setShowTitle(this.mShowTitle);
        setShowFullDate(this.mShowFullDate);
        setTextColor();
        initRecyclerView();
    }

    private void initRecyclerView() {
        this.mAdapter = new DayScrollDatePickerAdapter(this.mStyle, this);
        this.mDayRecyclerView.setAdapter(this.mAdapter);
        this.layoutManager = new LinearLayoutManager(getContext(), 0, true);
        this.layoutManager.setStackFromEnd(true);
        this.mDayRecyclerView.setLayoutManager(this.layoutManager);
    }

    public void setShowTitle(boolean z) {
        if (z) {
            this.mMonthTextView.setVisibility(0);
        } else {
            this.mMonthTextView.setVisibility(8);
        }
    }

    public void setShowFullDate(boolean z) {
        if (z) {
            this.mFullDateTextView.setVisibility(0);
        } else {
            this.mFullDateTextView.setVisibility(8);
        }
    }

    private void setTextColor() {
        this.mMonthTextView.setTextColor(this.mBaseTextColor);
        this.mFullDateTextView.setTextColor(this.mBaseTextColor);
    }

    public void setStartDate(int i, int i2, int i3) {
        this.mAdapter.setStartDate(i, i2, i3);
        this.mAdapter.notifyDataSetChanged();
    }

    public void setEndDate(int i, int i2, int i3) {
        this.mAdapter.setEndDate(i, i2, i3);
        this.mAdapter.notifyDataSetChanged();
    }

    public void getSelectedDate(OnDateSelectedListener onDateSelectedListener) {
        this.mListener = onDateSelectedListener;
    }

    @Override // com.treadly.Treadly.UI.ThirdParty.ScrollDatePicker.OnChildDateSelectedListener
    public void onDateSelectedChild(@Nullable LocalDate localDate) {
        if (localDate != null) {
            this.mFullDateTextView.setText(String.format("%s %s %s", localDate.toString("dd"), localDate.toString("MMMM"), localDate.toString("yyyy")));
            this.mListener.onDateSelected(localDate.toDate());
        }
    }

    @Override // com.treadly.Treadly.UI.ThirdParty.ScrollDatePicker.TitleValueCallback
    public void onTitleValueReturned(LocalDate localDate) {
        this.mMonthTextView.setText(localDate.toString("MMMM"));
    }

    public void scrollToDate(int i) {
        int intValue = this.mAdapter.selectedPosition.intValue();
        if (this.layoutManager != null) {
            this.layoutManager.scrollToPosition(intValue);
        }
    }

    public void scrollToSelectedDate() {
        if (this.mAdapter == null || this.layoutManager == null) {
            return;
        }
        this.layoutManager.scrollToPositionWithOffset(this.mAdapter.selectedPosition.intValue(), 0);
    }

    public void setInitialSelectedDate(int i, int i2, int i3) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(i3, i2, i);
        calendar.getTime();
        this.mAdapter.setInitialSelectedDate(i, i2, i3);
        this.mAdapter.notifyDataSetChanged();
    }
}
