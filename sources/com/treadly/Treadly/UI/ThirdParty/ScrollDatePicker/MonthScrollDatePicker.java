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
import org.joda.time.LocalDate;

/* loaded from: classes2.dex */
public class MonthScrollDatePicker extends LinearLayout implements TitleValueCallback, OnChildDateSelectedListener {
    private MonthScrollDatePickerAdapter mAdapter;
    private int mBaseTextColor;
    private TextView mFullDateTextView;
    private OnDateSelectedListener mListener;
    private RecyclerView mMonthRecyclerView;
    private boolean mShowFullDate;
    private boolean mShowTitle;
    private Style mStyle;
    private TextView mYearTextView;

    public MonthScrollDatePicker(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        LayoutInflater.from(context).inflate(R.layout.date_picker_scroll_month, (ViewGroup) this, true);
        getViewElements();
        setAttributeValues(context.getTheme().obtainStyledAttributes(attributeSet, R.styleable.ScrollDatePicker, 0, 0));
        MonthScrollDatePickerViewHolder.onDateSelected(this);
        initView();
    }

    private void getViewElements() {
        this.mYearTextView = (TextView) findViewById(R.id.date_picker_scroll_month_year);
        this.mFullDateTextView = (TextView) findViewById(R.id.date_picker_scroll_month_full_date);
        this.mMonthRecyclerView = (RecyclerView) findViewById(R.id.date_picker_scroll_month_recycler_view);
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
        this.mAdapter = new MonthScrollDatePickerAdapter(this.mStyle, this);
        this.mMonthRecyclerView.setAdapter(this.mAdapter);
        this.mMonthRecyclerView.setLayoutManager(new LinearLayoutManager(getContext(), 0, false));
    }

    private void setShowTitle(boolean z) {
        if (z) {
            this.mYearTextView.setVisibility(0);
        } else {
            this.mYearTextView.setVisibility(8);
        }
    }

    private void setShowFullDate(boolean z) {
        if (z) {
            this.mFullDateTextView.setVisibility(0);
        } else {
            this.mFullDateTextView.setVisibility(8);
        }
    }

    private void setTextColor() {
        this.mYearTextView.setTextColor(this.mBaseTextColor);
        this.mFullDateTextView.setTextColor(this.mBaseTextColor);
    }

    public void getSelectedDate(OnDateSelectedListener onDateSelectedListener) {
        this.mListener = onDateSelectedListener;
    }

    public void setStartMonth(int i) {
        this.mAdapter.setStartMonth(i);
        this.mAdapter.notifyDataSetChanged();
    }

    public void setStartDate(int i, int i2) {
        this.mAdapter.setStartDate(i, i2);
        this.mAdapter.notifyDataSetChanged();
    }

    public void setEndDate(int i, int i2) {
        this.mAdapter.setEndDate(i, i2);
        this.mAdapter.notifyDataSetChanged();
    }

    @Override // com.treadly.Treadly.UI.ThirdParty.ScrollDatePicker.TitleValueCallback
    public void onTitleValueReturned(LocalDate localDate) {
        this.mYearTextView.setText(localDate.toString("yyyy"));
    }

    @Override // com.treadly.Treadly.UI.ThirdParty.ScrollDatePicker.OnChildDateSelectedListener
    public void onDateSelectedChild(@Nullable LocalDate localDate) {
        if (localDate != null) {
            this.mFullDateTextView.setText(String.format("%s %s", localDate.toString("MMMM"), localDate.toString("yyyy")));
            this.mListener.onDateSelected(localDate.toDate());
        }
    }
}
