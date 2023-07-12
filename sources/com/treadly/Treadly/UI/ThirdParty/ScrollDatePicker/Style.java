package com.treadly.Treadly.UI.ThirdParty.ScrollDatePicker;

import android.graphics.drawable.Drawable;

/* loaded from: classes2.dex */
class Style {
    private Drawable background;
    private int baseColor;
    private int baseTextColor;
    private Drawable selectedBackground;
    private int selectedColor;
    private int selectedTextColor;

    public Style(int i, int i2, int i3, int i4, Drawable drawable, Drawable drawable2) {
        this.selectedColor = i;
        this.baseColor = i2;
        this.selectedTextColor = i3;
        this.baseTextColor = i4;
        this.background = drawable;
        this.selectedBackground = drawable2;
    }

    public int getSelectedColor() {
        return this.selectedColor;
    }

    public int getBaseColor() {
        return this.baseColor;
    }

    public int getSelectedTextColor() {
        return this.selectedTextColor;
    }

    public int getBaseTextColor() {
        return this.baseTextColor;
    }

    public Drawable getBackground() {
        return this.background;
    }

    public Drawable getSelectedBackground() {
        return this.selectedBackground;
    }
}
