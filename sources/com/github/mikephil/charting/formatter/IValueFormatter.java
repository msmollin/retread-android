package com.github.mikephil.charting.formatter;

import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.utils.ViewPortHandler;

@Deprecated
/* loaded from: classes.dex */
public interface IValueFormatter {
    @Deprecated
    String getFormattedValue(float f, Entry entry, int i, ViewPortHandler viewPortHandler);
}
