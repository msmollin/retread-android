package com.treadly.Treadly.UI.ThirdParty.ScrollDatePicker;

import android.view.LayoutInflater;
import android.view.ViewGroup;
import androidx.recyclerview.widget.RecyclerView;
import com.treadly.Treadly.R;
import org.joda.time.Days;
import org.joda.time.LocalDate;

/* loaded from: classes2.dex */
class DayScrollDatePickerAdapter extends RecyclerView.Adapter<DayScrollDatePickerViewHolder> {
    private TitleValueCallback callback;
    private LocalDate endDate;
    private LocalDate initialDate;
    public Integer selectedPosition = null;
    private LocalDate startDate = new LocalDate();
    private Style style;

    @Override // androidx.recyclerview.widget.RecyclerView.Adapter
    public long getItemId(int i) {
        return i;
    }

    public DayScrollDatePickerAdapter(Style style, TitleValueCallback titleValueCallback) {
        this.callback = titleValueCallback;
        this.style = style;
        titleValueCallback.onTitleValueReturned(this.startDate);
    }

    public void setStartDate(int i, int i2, int i3) {
        this.startDate = this.startDate.withDayOfMonth(i).withMonthOfYear(i2).withYear(i3);
        this.callback.onTitleValueReturned(this.startDate);
    }

    public void setEndDate(int i, int i2, int i3) {
        this.endDate = this.startDate.withDayOfMonth(i).withMonthOfYear(i2).withYear(i3);
    }

    public void setInitialSelectedDate(int i, int i2, int i3) {
        this.initialDate = this.startDate.withDayOfMonth(i).withMonthOfYear(i2).withYear(i3);
        this.selectedPosition = Integer.valueOf(Days.daysBetween(this.initialDate, this.endDate).getDays());
        notifyItemChanged(this.selectedPosition.intValue());
    }

    @Override // androidx.recyclerview.widget.RecyclerView.Adapter
    public DayScrollDatePickerViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        return new DayScrollDatePickerViewHolder(this.style, LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.day_list_item, viewGroup, false));
    }

    @Override // androidx.recyclerview.widget.RecyclerView.Adapter
    public void onBindViewHolder(final DayScrollDatePickerViewHolder dayScrollDatePickerViewHolder, int i) {
        LocalDate minusDays = this.endDate.minusDays(i);
        this.callback.onTitleValueReturned(minusDays);
        dayScrollDatePickerViewHolder.onBind(minusDays, new OnChildClickedListener() { // from class: com.treadly.Treadly.UI.ThirdParty.ScrollDatePicker.DayScrollDatePickerAdapter.1
            @Override // com.treadly.Treadly.UI.ThirdParty.ScrollDatePicker.OnChildClickedListener
            public void onChildClick(boolean z) {
                if (z) {
                    if (DayScrollDatePickerAdapter.this.selectedPosition != null) {
                        DayScrollDatePickerAdapter.this.notifyItemChanged(DayScrollDatePickerAdapter.this.selectedPosition.intValue());
                    }
                    DayScrollDatePickerAdapter.this.selectedPosition = Integer.valueOf(dayScrollDatePickerViewHolder.getAdapterPosition());
                }
            }
        });
        if (this.selectedPosition == null || this.selectedPosition.intValue() != dayScrollDatePickerViewHolder.getAdapterPosition()) {
            return;
        }
        dayScrollDatePickerViewHolder.styleViewSection(true);
    }

    @Override // androidx.recyclerview.widget.RecyclerView.Adapter
    public int getItemCount() {
        if (this.endDate != null) {
            return Days.daysBetween(this.startDate, this.endDate).getDays() + 1;
        }
        return Integer.MAX_VALUE;
    }
}
