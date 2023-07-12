package com.treadly.Treadly.UI.Util;

import android.text.InputFilter;
import android.text.Spanned;

/* loaded from: classes2.dex */
public class InputFilterMinMax implements InputFilter {
    private int max;
    private int min;

    private boolean isInRange(int i, int i2, int i3) {
        if (i2 > i) {
            if (i3 >= i && i3 <= i2) {
                return true;
            }
        } else if (i3 >= i2 && i3 <= i) {
            return true;
        }
        return false;
    }

    public InputFilterMinMax(int i, int i2) {
        this.min = i;
        this.max = i2;
    }

    public InputFilterMinMax(String str, String str2) {
        this.min = Integer.parseInt(str);
        this.max = Integer.parseInt(str2);
    }

    @Override // android.text.InputFilter
    public CharSequence filter(CharSequence charSequence, int i, int i2, Spanned spanned, int i3, int i4) {
        try {
            String str = spanned.toString().substring(0, i3) + spanned.toString().substring(i4, spanned.toString().length());
            if (isInRange(this.min, this.max, Integer.parseInt(str.substring(0, i3) + charSequence.toString() + str.substring(i3, str.length())))) {
                return null;
            }
            return "";
        } catch (NumberFormatException unused) {
            return "";
        }
    }
}
