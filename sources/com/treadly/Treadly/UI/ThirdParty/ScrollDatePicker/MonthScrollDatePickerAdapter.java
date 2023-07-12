package com.treadly.Treadly.UI.ThirdParty.ScrollDatePicker;

import android.view.LayoutInflater;
import android.view.ViewGroup;
import androidx.recyclerview.widget.RecyclerView;
import com.treadly.Treadly.R;
import org.joda.time.LocalDate;
import org.joda.time.Months;

/* loaded from: classes2.dex */
class MonthScrollDatePickerAdapter extends RecyclerView.Adapter<MonthScrollDatePickerViewHolder> {
    private TitleValueCallback callback;
    private LocalDate endDate;
    private Integer selectedPosition = null;
    private LocalDate startDate = new LocalDate();
    private Style style;

    public MonthScrollDatePickerAdapter(Style style, TitleValueCallback titleValueCallback) {
        this.callback = titleValueCallback;
        this.style = style;
        titleValueCallback.onTitleValueReturned(this.startDate);
    }

    public void setStartDate(int i, int i2) {
        this.startDate = this.startDate.withMonthOfYear(i).withYear(i2);
        this.callback.onTitleValueReturned(this.startDate);
    }

    public void setStartMonth(int i) {
        this.startDate = this.startDate.withMonthOfYear(i);
        this.callback.onTitleValueReturned(this.startDate);
    }

    public void setEndDate(int i, int i2) {
        this.endDate = this.startDate.withMonthOfYear(i).withYear(i2);
    }

    @Override // androidx.recyclerview.widget.RecyclerView.Adapter
    public MonthScrollDatePickerViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        return new MonthScrollDatePickerViewHolder(this.style, LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.month_list_item, viewGroup, false));
    }

    @Override // androidx.recyclerview.widget.RecyclerView.Adapter
    public void onBindViewHolder(final MonthScrollDatePickerViewHolder monthScrollDatePickerViewHolder, int i) {
        LocalDate plusMonths = this.startDate.plusMonths(i);
        this.callback.onTitleValueReturned(plusMonths);
        monthScrollDatePickerViewHolder.onBind(plusMonths, new OnChildClickedListener() { // from class: com.treadly.Treadly.UI.ThirdParty.ScrollDatePicker.MonthScrollDatePickerAdapter.1
            @Override // com.treadly.Treadly.UI.ThirdParty.ScrollDatePicker.OnChildClickedListener
            public void onChildClick(boolean z) {
                if (z) {
                    if (MonthScrollDatePickerAdapter.this.selectedPosition != null) {
                        MonthScrollDatePickerAdapter.this.notifyItemChanged(MonthScrollDatePickerAdapter.this.selectedPosition.intValue());
                    }
                    MonthScrollDatePickerAdapter.this.selectedPosition = Integer.valueOf(monthScrollDatePickerViewHolder.getAdapterPosition());
                }
            }
        });
        if (this.selectedPosition == null || this.selectedPosition.intValue() != monthScrollDatePickerViewHolder.getAdapterPosition()) {
            return;
        }
        monthScrollDatePickerViewHolder.styleViewSection(true);
    }

    @Override // androidx.recyclerview.widget.RecyclerView.Adapter
    public int getItemCount() {
        if (this.endDate != null) {
            return Months.monthsBetween(this.startDate, this.endDate).getMonths() + 1;
        }
        return Integer.MAX_VALUE;
    }
}
