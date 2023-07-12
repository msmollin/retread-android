package com.treadly.Treadly.UI.TreadlyActivity.ActivityCalendar;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import androidx.core.app.NotificationCompat;
import com.treadly.Treadly.R;
import java.io.PrintStream;

/* loaded from: classes2.dex */
public class TreadlyActivityCalendarProgressRing extends View {
    private Paint backgroundPaint;
    private int color;
    private Paint foregroundPaint;
    private int max;
    private int min;
    private float progress;
    private RectF rectF;
    private int startAngle;
    private float strokeWidth;

    public float getStrokeWidth() {
        return this.strokeWidth;
    }

    public void setStrokeWidth(float f) {
        this.strokeWidth = f;
        this.backgroundPaint.setStrokeWidth(f);
        this.foregroundPaint.setStrokeWidth(f);
        invalidate();
        requestLayout();
    }

    public float getProgress() {
        return this.progress;
    }

    public void setProgress(float f) {
        this.progress = f;
        invalidate();
    }

    public int getMin() {
        return this.min;
    }

    public void setMin(int i) {
        this.min = i;
        invalidate();
    }

    public int getMax() {
        return this.max;
    }

    public void setMax(int i) {
        this.max = i;
        invalidate();
    }

    public int getColor() {
        return this.color;
    }

    public void setColor(int i) {
        this.color = i;
        this.foregroundPaint.setColor(i);
        invalidate();
        requestLayout();
    }

    public TreadlyActivityCalendarProgressRing(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        this.strokeWidth = 4.0f;
        this.progress = 0.0f;
        this.min = 0;
        this.max = 100;
        this.startAngle = -90;
        this.color = -12303292;
        init(context, attributeSet);
    }

    private void init(Context context, AttributeSet attributeSet) {
        this.rectF = new RectF();
        TypedArray obtainStyledAttributes = context.getTheme().obtainStyledAttributes(attributeSet, R.styleable.CircleProgressBar, 0, 0);
        try {
            this.strokeWidth = obtainStyledAttributes.getDimension(3, this.strokeWidth);
            this.progress = obtainStyledAttributes.getFloat(2, this.progress);
            this.color = obtainStyledAttributes.getInt(4, this.color);
            this.min = obtainStyledAttributes.getInt(1, this.min);
            this.max = obtainStyledAttributes.getInt(0, this.max);
            obtainStyledAttributes.recycle();
            this.backgroundPaint = new Paint(1);
            this.backgroundPaint.setColor(adjustAlpha(getContext().getColor(R.color.clear), 0.3f));
            this.backgroundPaint.setStyle(Paint.Style.STROKE);
            this.backgroundPaint.setStrokeWidth(this.strokeWidth);
            this.foregroundPaint = new Paint(1);
            this.foregroundPaint.setColor(this.color);
            this.foregroundPaint.setStyle(Paint.Style.STROKE);
            this.foregroundPaint.setStrokeWidth(this.strokeWidth);
            this.foregroundPaint.setStrokeCap(Paint.Cap.ROUND);
        } catch (Throwable th) {
            obtainStyledAttributes.recycle();
            throw th;
        }
    }

    @Override // android.view.View
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        PrintStream printStream = System.out;
        printStream.println("LGK :: RECT l: " + this.rectF.left + " r: " + this.rectF.right + " t: " + this.rectF.top + " b:" + this.rectF.bottom);
        canvas.drawOval(this.rectF, this.backgroundPaint);
        canvas.drawArc(this.rectF, (float) this.startAngle, (this.progress * 360.0f) / ((float) this.max), false, this.foregroundPaint);
    }

    @Override // android.view.View
    protected void onMeasure(int i, int i2) {
        int min = Math.min(getDefaultSize(getSuggestedMinimumWidth(), i), getDefaultSize(getSuggestedMinimumHeight(), i2));
        setMeasuredDimension(min, min);
        float f = min;
        this.rectF.set((this.strokeWidth / 2.0f) + 0.0f, (this.strokeWidth / 2.0f) + 0.0f, f - (this.strokeWidth / 2.0f), f - (this.strokeWidth / 2.0f));
    }

    public int lightenColor(int i, float f) {
        return Color.argb(Color.alpha(i), Math.min(255, (int) (Color.red(i) * f)), Math.min(255, (int) (Color.green(i) * f)), Math.min(255, (int) (Color.blue(i) * f)));
    }

    public int adjustAlpha(int i, float f) {
        return Color.argb(Math.round(Color.alpha(i) * f), Color.red(i), Color.green(i), Color.blue(i));
    }

    public void setProgressWithAnimation(float f) {
        ObjectAnimator ofFloat = ObjectAnimator.ofFloat(this, NotificationCompat.CATEGORY_PROGRESS, f);
        ofFloat.setDuration(1500L);
        ofFloat.setInterpolator(new DecelerateInterpolator());
        ofFloat.start();
    }
}
