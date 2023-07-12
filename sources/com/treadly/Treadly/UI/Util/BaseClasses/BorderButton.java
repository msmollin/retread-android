package com.treadly.Treadly.UI.Util.BaseClasses;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import androidx.appcompat.widget.AppCompatButton;

/* loaded from: classes2.dex */
public class BorderButton extends AppCompatButton {
    public BorderButton() {
        super(null);
    }

    public BorderButton(Context context) {
        super(context);
    }

    public BorderButton(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
    }

    public BorderButton(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
    }

    @Override // android.widget.TextView, android.view.View
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
    }
}
