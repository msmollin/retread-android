package com.treadly.Treadly.UI.ThirdParty.ScrollDatePicker;

import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;
import com.treadly.Treadly.R;
import org.joda.time.LocalDate;

/* loaded from: classes2.dex */
class DayScrollDatePickerViewHolder extends RecyclerView.ViewHolder {
    private static OnChildDateSelectedListener dateSelectedListener;
    private Drawable background;
    private int baseTextColor;
    private OnChildClickedListener clickedListener;
    public TextView dayNameTextView;
    public TextView dayValueTextView;
    private Drawable selectedBackground;
    private int selectedColor;
    private int selectedTextColor;

    public DayScrollDatePickerViewHolder(Style style, View view) {
        super(view);
        this.dayNameTextView = (TextView) view.findViewById(R.id.day_name);
        this.dayValueTextView = (TextView) view.findViewById(R.id.day_value);
        init(style);
        dateSelectedListener.onDateSelectedChild(null);
    }

    private void init(Style style) {
        this.selectedTextColor = style.getSelectedTextColor();
        this.baseTextColor = style.getBaseTextColor();
        this.selectedBackground = style.getSelectedBackground();
        this.background = style.getBackground();
        this.selectedColor = style.getSelectedColor();
    }

    public void styleViewSection(boolean z) {
        this.dayNameTextView.setTextColor(z ? this.selectedColor : this.baseTextColor);
        this.dayValueTextView.setTextColor(z ? this.selectedTextColor : this.baseTextColor);
        this.dayValueTextView.setBackground(z ? this.selectedBackground : this.background);
        this.itemView.setBackgroundResource(z ? R.drawable.scroll_date_picker_backround_holder : R.color.transparent);
    }

    public static void onDateSelected(OnChildDateSelectedListener onChildDateSelectedListener) {
        dateSelectedListener = onChildDateSelectedListener;
    }

    public void onBind(final LocalDate localDate, OnChildClickedListener onChildClickedListener) {
        this.clickedListener = onChildClickedListener;
        this.clickedListener.onChildClick(false);
        Log.d("LOG", localDate.toString());
        styleViewSection(false);
        this.dayNameTextView.setText(localDate.toString("EEE"));
        this.dayValueTextView.setText(localDate.toString("dd"));
        this.dayValueTextView.setOnClickListener(new View.OnClickListener() { // from class: com.treadly.Treadly.UI.ThirdParty.ScrollDatePicker.DayScrollDatePickerViewHolder.1
            @Override // android.view.View.OnClickListener
            public void onClick(View view) {
                DayScrollDatePickerViewHolder.this.styleViewSection(true);
                DayScrollDatePickerViewHolder.this.clickedListener.onChildClick(true);
                DayScrollDatePickerViewHolder.dateSelectedListener.onDateSelectedChild(localDate);
            }
        });
    }
}
