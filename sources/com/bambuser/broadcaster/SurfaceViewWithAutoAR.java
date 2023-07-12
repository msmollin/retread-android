package com.bambuser.broadcaster;

import android.content.Context;
import android.content.res.TypedArray;
import android.os.Handler;
import android.util.AttributeSet;
import android.util.Log;
import android.view.SurfaceView;
import android.view.View;
import com.bambuser.broadcaster.lib.R;

/* loaded from: classes.dex */
public class SurfaceViewWithAutoAR extends SurfaceView {
    private static final String LOGTAG = "SurfaceViewWithAutoAR";
    private boolean mCropToParent;
    private int mDenominator;
    private int mNumerator;

    public SurfaceViewWithAutoAR(Context context) {
        super(context);
        this.mNumerator = 4;
        this.mDenominator = 3;
        this.mCropToParent = true;
    }

    public SurfaceViewWithAutoAR(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        this.mNumerator = 4;
        this.mDenominator = 3;
        this.mCropToParent = true;
        initialize(context, attributeSet);
    }

    public SurfaceViewWithAutoAR(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        this.mNumerator = 4;
        this.mDenominator = 3;
        this.mCropToParent = true;
        initialize(context, attributeSet);
    }

    private void initialize(Context context, AttributeSet attributeSet) {
        TypedArray obtainStyledAttributes = context.obtainStyledAttributes(attributeSet, R.styleable.SurfaceViewWithAutoAR);
        this.mNumerator = obtainStyledAttributes.getInt(R.styleable.SurfaceViewWithAutoAR_numerator, 4);
        this.mDenominator = obtainStyledAttributes.getInt(R.styleable.SurfaceViewWithAutoAR_denominator, 3);
        this.mCropToParent = obtainStyledAttributes.getBoolean(R.styleable.SurfaceViewWithAutoAR_cropToParent, true);
        obtainStyledAttributes.recycle();
    }

    public void setCropToParent(boolean z) {
        boolean z2 = z != this.mCropToParent;
        this.mCropToParent = z;
        Handler handler = getHandler();
        if (!z2 || handler == null) {
            return;
        }
        handler.post(new Runnable() { // from class: com.bambuser.broadcaster.SurfaceViewWithAutoAR.1
            @Override // java.lang.Runnable
            public void run() {
                SurfaceViewWithAutoAR.this.requestLayout();
            }
        });
    }

    public boolean cropToParent() {
        return this.mCropToParent;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean setAspectRatio(int i, int i2) {
        if (i <= 0 || i2 <= 0) {
            Log.w(LOGTAG, "Illegal aspect ratio parameters, defaulting to 4:3");
            i = 4;
            i2 = 3;
        }
        boolean z = Math.abs((((double) i) / ((double) i2)) - (((double) this.mNumerator) / ((double) this.mDenominator))) > 0.01d;
        Log.i(LOGTAG, "need layout: " + z);
        this.mNumerator = i;
        this.mDenominator = i2;
        Handler handler = getHandler();
        if (z && handler != null) {
            handler.post(new Runnable() { // from class: com.bambuser.broadcaster.SurfaceViewWithAutoAR.2
                @Override // java.lang.Runnable
                public void run() {
                    SurfaceViewWithAutoAR.this.requestLayout();
                }
            });
        }
        return z;
    }

    @Override // android.view.SurfaceView, android.view.View
    protected void onMeasure(int i, int i2) {
        int defaultSize = getDefaultSize(0, i);
        int defaultSize2 = getDefaultSize(0, i2);
        if (Log.isLoggable(LOGTAG, 3)) {
            Log.d(LOGTAG, "Got measured width: " + defaultSize + " and height: " + defaultSize2);
            Log.d(LOGTAG, "measure spec mode: width: " + View.MeasureSpec.getMode(i) + " and height: " + View.MeasureSpec.getMode(i2));
        }
        if (this.mCropToParent) {
            if (View.MeasureSpec.getMode(i) != 1073741824 && (this.mDenominator * defaultSize) / this.mNumerator < defaultSize2) {
                defaultSize = (this.mNumerator * defaultSize2) / this.mDenominator;
            }
            if (View.MeasureSpec.getMode(i2) != 1073741824 && (this.mNumerator * defaultSize2) / this.mDenominator < defaultSize) {
                defaultSize2 = (this.mDenominator * defaultSize) / this.mNumerator;
            }
        } else {
            if (View.MeasureSpec.getMode(i) != 1073741824 && (this.mDenominator * defaultSize) / this.mNumerator > defaultSize2) {
                defaultSize = (this.mNumerator * defaultSize2) / this.mDenominator;
            }
            if (View.MeasureSpec.getMode(i2) != 1073741824 && (this.mNumerator * defaultSize2) / this.mDenominator > defaultSize) {
                defaultSize2 = (this.mDenominator * defaultSize) / this.mNumerator;
            }
        }
        if (Log.isLoggable(LOGTAG, 3)) {
            Log.d(LOGTAG, "Setting measured width: " + defaultSize + " and height: " + defaultSize2);
        }
        setMeasuredDimension(defaultSize, defaultSize2);
    }
}
