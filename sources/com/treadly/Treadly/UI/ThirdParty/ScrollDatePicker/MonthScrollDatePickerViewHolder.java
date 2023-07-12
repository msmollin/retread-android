package com.treadly.Treadly.UI.ThirdParty.ScrollDatePicker;

import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;
import com.treadly.Treadly.R;
import org.joda.time.LocalDate;

/* loaded from: classes2.dex */
class MonthScrollDatePickerViewHolder extends RecyclerView.ViewHolder {
    private static OnChildDateSelectedListener dateSelectedListener;
    private Drawable background;
    private int baseTextColor;
    private OnChildClickedListener clickedListener;
    public TextView monthTextView;
    private Drawable selectedBackground;
    private int selectedTextColor;

    public MonthScrollDatePickerViewHolder(Style style, View view) {
        super(view);
        this.monthTextView = (TextView) view.findViewById(R.id.month_list_item_name);
        init(style);
        dateSelectedListener.onDateSelectedChild(null);
    }

    private void init(Style style) {
        this.selectedTextColor = style.getSelectedTextColor();
        this.baseTextColor = style.getBaseTextColor();
        this.selectedBackground = style.getSelectedBackground();
        this.background = style.getBackground();
    }

    public void styleViewSection(boolean z) {
        this.monthTextView.setTextColor(z ? this.selectedTextColor : this.baseTextColor);
        this.monthTextView.setBackground(z ? this.selectedBackground : this.background);
    }

    public static void onDateSelected(OnChildDateSelectedListener onChildDateSelectedListener) {
        dateSelectedListener = onChildDateSelectedListener;
    }

    public void onBind(final LocalDate localDate, OnChildClickedListener onChildClickedListener) {
        this.clickedListener = onChildClickedListener;
        this.clickedListener.onChildClick(false);
        Log.d("LOG", localDate.toString());
        styleViewSection(false);
        this.monthTextView.setText(localDate.toString("MMM"));
        this.monthTextView.setOnClickListener(new View.OnClickListener() { // from class: com.treadly.Treadly.UI.ThirdParty.ScrollDatePicker.MonthScrollDatePickerViewHolder.1
            @Override // android.view.View.OnClickListener
            public void onClick(View view) {
                MonthScrollDatePickerViewHolder.this.styleViewSection(true);
                MonthScrollDatePickerViewHolder.this.clickedListener.onChildClick(true);
                MonthScrollDatePickerViewHolder.dateSelectedListener.onDateSelectedChild(localDate);
            }
        });
    }
}
