package com.treadly.Treadly.UI.Util;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.GradientDrawable;
import android.util.AttributeSet;
import androidx.appcompat.widget.AppCompatButton;

/* loaded from: classes2.dex */
public class RoundedButton extends AppCompatButton {
    private static final String TAG = "RoundedImageView";
    private int centerX;
    private int centerY;
    private Paint circlePaint;
    private Paint focusPaint;
    private int outerRadius;

    public RoundedButton(Context context) {
        super(context);
    }

    public RoundedButton(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
    }

    public RoundedButton(Context context, AttributeSet attributeSet, int i, float f) {
        super(context, attributeSet, i);
    }

    @Override // android.widget.TextView, android.view.View
    protected void onDraw(Canvas canvas) {
        GradientDrawable gradientDrawable = new GradientDrawable();
        gradientDrawable.setShape(0);
        gradientDrawable.setCornerRadius(getHeight());
        if (getBackground() instanceof ColorDrawable) {
            gradientDrawable.setColor(((ColorDrawable) getBackground()).getColor());
            setBackground(null);
        }
        setBackgroundDrawable(gradientDrawable);
        super.onDraw(canvas);
    }
}
