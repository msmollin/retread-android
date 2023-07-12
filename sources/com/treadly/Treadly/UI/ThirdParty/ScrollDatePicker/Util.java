package com.treadly.Treadly.UI.ThirdParty.ScrollDatePicker;

import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;

/* loaded from: classes2.dex */
class Util {
    Util() {
    }

    public static Drawable setDrawableBackgroundColor(Drawable drawable, int i) {
        GradientDrawable gradientDrawable = (GradientDrawable) drawable.mutate();
        gradientDrawable.setColor(i);
        return gradientDrawable;
    }
}
