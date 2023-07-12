package com.github.mikephil.charting.renderer;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import com.github.mikephil.charting.animation.ChartAnimator;
import com.github.mikephil.charting.buffer.BarBuffer;
import com.github.mikephil.charting.buffer.HorizontalBarBuffer;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.interfaces.dataprovider.BarDataProvider;
import com.github.mikephil.charting.interfaces.dataprovider.ChartInterface;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;
import com.github.mikephil.charting.utils.MPPointF;
import com.github.mikephil.charting.utils.Transformer;
import com.github.mikephil.charting.utils.Utils;
import com.github.mikephil.charting.utils.ViewPortHandler;
import java.util.List;

/* loaded from: classes.dex */
public class HorizontalBarChartRenderer extends BarChartRenderer {
    private RectF mBarShadowRectBuffer;

    public HorizontalBarChartRenderer(BarDataProvider barDataProvider, ChartAnimator chartAnimator, ViewPortHandler viewPortHandler) {
        super(barDataProvider, chartAnimator, viewPortHandler);
        this.mBarShadowRectBuffer = new RectF();
        this.mValuePaint.setTextAlign(Paint.Align.LEFT);
    }

    @Override // com.github.mikephil.charting.renderer.BarChartRenderer, com.github.mikephil.charting.renderer.DataRenderer
    public void initBuffers() {
        BarData barData = this.mChart.getBarData();
        this.mBarBuffers = new HorizontalBarBuffer[barData.getDataSetCount()];
        for (int i = 0; i < this.mBarBuffers.length; i++) {
            IBarDataSet iBarDataSet = (IBarDataSet) barData.getDataSetByIndex(i);
            this.mBarBuffers[i] = new HorizontalBarBuffer(iBarDataSet.getEntryCount() * 4 * (iBarDataSet.isStacked() ? iBarDataSet.getStackSize() : 1), barData.getDataSetCount(), iBarDataSet.isStacked());
        }
    }

    @Override // com.github.mikephil.charting.renderer.BarChartRenderer
    protected void drawDataSet(Canvas canvas, IBarDataSet iBarDataSet, int i) {
        Transformer transformer = this.mChart.getTransformer(iBarDataSet.getAxisDependency());
        this.mBarBorderPaint.setColor(iBarDataSet.getBarBorderColor());
        this.mBarBorderPaint.setStrokeWidth(Utils.convertDpToPixel(iBarDataSet.getBarBorderWidth()));
        boolean z = iBarDataSet.getBarBorderWidth() > 0.0f;
        float phaseX = this.mAnimator.getPhaseX();
        float phaseY = this.mAnimator.getPhaseY();
        if (this.mChart.isDrawBarShadowEnabled()) {
            this.mShadowPaint.setColor(iBarDataSet.getBarShadowColor());
            float barWidth = this.mChart.getBarData().getBarWidth() / 2.0f;
            int min = Math.min((int) Math.ceil(iBarDataSet.getEntryCount() * phaseX), iBarDataSet.getEntryCount());
            for (int i2 = 0; i2 < min; i2++) {
                float x = ((BarEntry) iBarDataSet.getEntryForIndex(i2)).getX();
                this.mBarShadowRectBuffer.top = x - barWidth;
                this.mBarShadowRectBuffer.bottom = x + barWidth;
                transformer.rectValueToPixel(this.mBarShadowRectBuffer);
                if (this.mViewPortHandler.isInBoundsTop(this.mBarShadowRectBuffer.bottom)) {
                    if (!this.mViewPortHandler.isInBoundsBottom(this.mBarShadowRectBuffer.top)) {
                        break;
                    }
                    this.mBarShadowRectBuffer.left = this.mViewPortHandler.contentLeft();
                    this.mBarShadowRectBuffer.right = this.mViewPortHandler.contentRight();
                    canvas.drawRect(this.mBarShadowRectBuffer, this.mShadowPaint);
                }
            }
        }
        BarBuffer barBuffer = this.mBarBuffers[i];
        barBuffer.setPhases(phaseX, phaseY);
        barBuffer.setDataSet(i);
        barBuffer.setInverted(this.mChart.isInverted(iBarDataSet.getAxisDependency()));
        barBuffer.setBarWidth(this.mChart.getBarData().getBarWidth());
        barBuffer.feed(iBarDataSet);
        transformer.pointValuesToPixel(barBuffer.buffer);
        boolean z2 = iBarDataSet.getColors().size() == 1;
        if (z2) {
            this.mRenderPaint.setColor(iBarDataSet.getColor());
        }
        for (int i3 = 0; i3 < barBuffer.size(); i3 += 4) {
            int i4 = i3 + 3;
            if (!this.mViewPortHandler.isInBoundsTop(barBuffer.buffer[i4])) {
                return;
            }
            int i5 = i3 + 1;
            if (this.mViewPortHandler.isInBoundsBottom(barBuffer.buffer[i5])) {
                if (!z2) {
                    this.mRenderPaint.setColor(iBarDataSet.getColor(i3 / 4));
                }
                int i6 = i3 + 2;
                canvas.drawRect(barBuffer.buffer[i3], barBuffer.buffer[i5], barBuffer.buffer[i6], barBuffer.buffer[i4], this.mRenderPaint);
                if (z) {
                    canvas.drawRect(barBuffer.buffer[i3], barBuffer.buffer[i5], barBuffer.buffer[i6], barBuffer.buffer[i4], this.mBarBorderPaint);
                }
            }
        }
    }

    @Override // com.github.mikephil.charting.renderer.BarChartRenderer, com.github.mikephil.charting.renderer.DataRenderer
    public void drawValues(Canvas canvas) {
        List list;
        int i;
        MPPointF mPPointF;
        int i2;
        float[] fArr;
        BarEntry barEntry;
        float f;
        boolean z;
        float f2;
        float f3;
        int i3;
        float[] fArr2;
        BarEntry barEntry2;
        BarEntry barEntry3;
        boolean z2;
        String str;
        float f4;
        MPPointF mPPointF2;
        float f5;
        int i4;
        List list2;
        int i5;
        float f6;
        MPPointF mPPointF3;
        BarBuffer barBuffer;
        ValueFormatter valueFormatter;
        if (isDrawingValuesAllowed(this.mChart)) {
            List dataSets = this.mChart.getBarData().getDataSets();
            float convertDpToPixel = Utils.convertDpToPixel(5.0f);
            boolean isDrawValueAboveBarEnabled = this.mChart.isDrawValueAboveBarEnabled();
            int i6 = 0;
            while (i6 < this.mChart.getBarData().getDataSetCount()) {
                IBarDataSet iBarDataSet = (IBarDataSet) dataSets.get(i6);
                if (shouldDrawValues(iBarDataSet)) {
                    boolean isInverted = this.mChart.isInverted(iBarDataSet.getAxisDependency());
                    applyValueTextStyle(iBarDataSet);
                    float f7 = 2.0f;
                    float calcTextHeight = Utils.calcTextHeight(this.mValuePaint, "10") / 2.0f;
                    ValueFormatter valueFormatter2 = iBarDataSet.getValueFormatter();
                    BarBuffer barBuffer2 = this.mBarBuffers[i6];
                    float phaseY = this.mAnimator.getPhaseY();
                    MPPointF mPPointF4 = MPPointF.getInstance(iBarDataSet.getIconsOffset());
                    mPPointF4.x = Utils.convertDpToPixel(mPPointF4.x);
                    mPPointF4.y = Utils.convertDpToPixel(mPPointF4.y);
                    if (!iBarDataSet.isStacked()) {
                        int i7 = 0;
                        while (i7 < barBuffer2.buffer.length * this.mAnimator.getPhaseX()) {
                            int i8 = i7 + 1;
                            float f8 = (barBuffer2.buffer[i8] + barBuffer2.buffer[i7 + 3]) / f7;
                            if (!this.mViewPortHandler.isInBoundsTop(barBuffer2.buffer[i8])) {
                                break;
                            }
                            if (this.mViewPortHandler.isInBoundsX(barBuffer2.buffer[i7]) && this.mViewPortHandler.isInBoundsBottom(barBuffer2.buffer[i8])) {
                                BarEntry barEntry4 = (BarEntry) iBarDataSet.getEntryForIndex(i7 / 4);
                                float y = barEntry4.getY();
                                String barLabel = valueFormatter2.getBarLabel(barEntry4);
                                float calcTextWidth = Utils.calcTextWidth(this.mValuePaint, barLabel);
                                if (isDrawValueAboveBarEnabled) {
                                    str = barLabel;
                                    f4 = convertDpToPixel;
                                } else {
                                    str = barLabel;
                                    f4 = -(calcTextWidth + convertDpToPixel);
                                }
                                if (isDrawValueAboveBarEnabled) {
                                    mPPointF2 = mPPointF4;
                                    f5 = -(calcTextWidth + convertDpToPixel);
                                } else {
                                    mPPointF2 = mPPointF4;
                                    f5 = convertDpToPixel;
                                }
                                if (isInverted) {
                                    f4 = (-f4) - calcTextWidth;
                                    f5 = (-f5) - calcTextWidth;
                                }
                                float f9 = f4;
                                float f10 = f5;
                                if (iBarDataSet.isDrawValuesEnabled()) {
                                    i4 = i7;
                                    list2 = dataSets;
                                    mPPointF3 = mPPointF2;
                                    i5 = i6;
                                    barBuffer = barBuffer2;
                                    f6 = calcTextHeight;
                                    valueFormatter = valueFormatter2;
                                    drawValue(canvas, str, barBuffer2.buffer[i7 + 2] + (y >= 0.0f ? f9 : f10), f8 + calcTextHeight, iBarDataSet.getValueTextColor(i7 / 2));
                                } else {
                                    i4 = i7;
                                    list2 = dataSets;
                                    i5 = i6;
                                    f6 = calcTextHeight;
                                    mPPointF3 = mPPointF2;
                                    barBuffer = barBuffer2;
                                    valueFormatter = valueFormatter2;
                                }
                                if (barEntry4.getIcon() != null && iBarDataSet.isDrawIconsEnabled()) {
                                    Drawable icon = barEntry4.getIcon();
                                    float f11 = barBuffer.buffer[i4 + 2];
                                    if (y < 0.0f) {
                                        f9 = f10;
                                    }
                                    Utils.drawImage(canvas, icon, (int) (f11 + f9 + mPPointF3.x), (int) (f8 + mPPointF3.y), icon.getIntrinsicWidth(), icon.getIntrinsicHeight());
                                }
                            } else {
                                i4 = i7;
                                list2 = dataSets;
                                i5 = i6;
                                f6 = calcTextHeight;
                                mPPointF3 = mPPointF4;
                                barBuffer = barBuffer2;
                                valueFormatter = valueFormatter2;
                            }
                            i7 = i4 + 4;
                            mPPointF4 = mPPointF3;
                            barBuffer2 = barBuffer;
                            valueFormatter2 = valueFormatter;
                            dataSets = list2;
                            i6 = i5;
                            calcTextHeight = f6;
                            f7 = 2.0f;
                        }
                        list = dataSets;
                        i = i6;
                        mPPointF = mPPointF4;
                    } else {
                        list = dataSets;
                        i = i6;
                        mPPointF = mPPointF4;
                        Transformer transformer = this.mChart.getTransformer(iBarDataSet.getAxisDependency());
                        int i9 = 0;
                        int i10 = 0;
                        while (i9 < iBarDataSet.getEntryCount() * this.mAnimator.getPhaseX()) {
                            BarEntry barEntry5 = (BarEntry) iBarDataSet.getEntryForIndex(i9);
                            int valueTextColor = iBarDataSet.getValueTextColor(i9);
                            float[] yVals = barEntry5.getYVals();
                            if (yVals == null) {
                                int i11 = i10 + 1;
                                if (!this.mViewPortHandler.isInBoundsTop(barBuffer2.buffer[i11])) {
                                    break;
                                } else if (this.mViewPortHandler.isInBoundsX(barBuffer2.buffer[i10]) && this.mViewPortHandler.isInBoundsBottom(barBuffer2.buffer[i11])) {
                                    String barLabel2 = valueFormatter2.getBarLabel(barEntry5);
                                    float calcTextWidth2 = Utils.calcTextWidth(this.mValuePaint, barLabel2);
                                    float f12 = isDrawValueAboveBarEnabled ? convertDpToPixel : -(calcTextWidth2 + convertDpToPixel);
                                    float f13 = isDrawValueAboveBarEnabled ? -(calcTextWidth2 + convertDpToPixel) : convertDpToPixel;
                                    if (isInverted) {
                                        f12 = (-f12) - calcTextWidth2;
                                        f13 = (-f13) - calcTextWidth2;
                                    }
                                    float f14 = f12;
                                    float f15 = f13;
                                    if (iBarDataSet.isDrawValuesEnabled()) {
                                        i2 = i9;
                                        fArr = yVals;
                                        barEntry3 = barEntry5;
                                        drawValue(canvas, barLabel2, barBuffer2.buffer[i10 + 2] + (barEntry5.getY() >= 0.0f ? f14 : f15), barBuffer2.buffer[i11] + calcTextHeight, valueTextColor);
                                    } else {
                                        barEntry3 = barEntry5;
                                        i2 = i9;
                                        fArr = yVals;
                                    }
                                    if (barEntry3.getIcon() != null && iBarDataSet.isDrawIconsEnabled()) {
                                        Drawable icon2 = barEntry3.getIcon();
                                        float f16 = barBuffer2.buffer[i10 + 2];
                                        if (barEntry3.getY() < 0.0f) {
                                            f14 = f15;
                                        }
                                        Utils.drawImage(canvas, icon2, (int) (f16 + f14 + mPPointF.x), (int) (barBuffer2.buffer[i11] + mPPointF.y), icon2.getIntrinsicWidth(), icon2.getIntrinsicHeight());
                                    }
                                }
                            } else {
                                BarEntry barEntry6 = barEntry5;
                                i2 = i9;
                                fArr = yVals;
                                float[] fArr3 = new float[fArr.length * 2];
                                float f17 = -barEntry6.getNegativeSum();
                                float f18 = 0.0f;
                                int i12 = 0;
                                int i13 = 0;
                                while (i12 < fArr3.length) {
                                    float f19 = fArr[i13];
                                    int i14 = (f19 > 0.0f ? 1 : (f19 == 0.0f ? 0 : -1));
                                    if (i14 != 0 || (f18 != 0.0f && f17 != 0.0f)) {
                                        if (i14 >= 0) {
                                            f19 = f18 + f19;
                                            f18 = f19;
                                        } else {
                                            float f20 = f17;
                                            f17 -= f19;
                                            f19 = f20;
                                        }
                                    }
                                    fArr3[i12] = f19 * phaseY;
                                    i12 += 2;
                                    i13++;
                                }
                                transformer.pointValuesToPixel(fArr3);
                                int i15 = 0;
                                while (i15 < fArr3.length) {
                                    float f21 = fArr[i15 / 2];
                                    BarEntry barEntry7 = barEntry6;
                                    String barStackedLabel = valueFormatter2.getBarStackedLabel(f21, barEntry7);
                                    float calcTextWidth3 = Utils.calcTextWidth(this.mValuePaint, barStackedLabel);
                                    if (isDrawValueAboveBarEnabled) {
                                        barEntry = barEntry7;
                                        f = convertDpToPixel;
                                    } else {
                                        barEntry = barEntry7;
                                        f = -(calcTextWidth3 + convertDpToPixel);
                                    }
                                    if (isDrawValueAboveBarEnabled) {
                                        z = isDrawValueAboveBarEnabled;
                                        f2 = -(calcTextWidth3 + convertDpToPixel);
                                    } else {
                                        z = isDrawValueAboveBarEnabled;
                                        f2 = convertDpToPixel;
                                    }
                                    if (isInverted) {
                                        f = (-f) - calcTextWidth3;
                                        f2 = (-f2) - calcTextWidth3;
                                    }
                                    boolean z3 = (f21 == 0.0f && f17 == 0.0f && f18 > 0.0f) || f21 < 0.0f;
                                    float f22 = fArr3[i15];
                                    if (z3) {
                                        f = f2;
                                    }
                                    float f23 = f22 + f;
                                    float f24 = (barBuffer2.buffer[i10 + 1] + barBuffer2.buffer[i10 + 3]) / 2.0f;
                                    if (!this.mViewPortHandler.isInBoundsTop(f24)) {
                                        break;
                                    }
                                    if (this.mViewPortHandler.isInBoundsX(f23) && this.mViewPortHandler.isInBoundsBottom(f24)) {
                                        if (iBarDataSet.isDrawValuesEnabled()) {
                                            f3 = f24;
                                            barEntry2 = barEntry;
                                            i3 = i15;
                                            fArr2 = fArr3;
                                            drawValue(canvas, barStackedLabel, f23, f24 + calcTextHeight, valueTextColor);
                                        } else {
                                            f3 = f24;
                                            i3 = i15;
                                            fArr2 = fArr3;
                                            barEntry2 = barEntry;
                                        }
                                        if (barEntry2.getIcon() != null && iBarDataSet.isDrawIconsEnabled()) {
                                            Drawable icon3 = barEntry2.getIcon();
                                            Utils.drawImage(canvas, icon3, (int) (f23 + mPPointF.x), (int) (f3 + mPPointF.y), icon3.getIntrinsicWidth(), icon3.getIntrinsicHeight());
                                        }
                                    } else {
                                        i3 = i15;
                                        fArr2 = fArr3;
                                        barEntry2 = barEntry;
                                    }
                                    i15 = i3 + 2;
                                    fArr3 = fArr2;
                                    barEntry6 = barEntry2;
                                    isDrawValueAboveBarEnabled = z;
                                }
                            }
                            z = isDrawValueAboveBarEnabled;
                            i10 = fArr == null ? i10 + 4 : i10 + (fArr.length * 4);
                            i9 = i2 + 1;
                            isDrawValueAboveBarEnabled = z;
                        }
                    }
                    z2 = isDrawValueAboveBarEnabled;
                    MPPointF.recycleInstance(mPPointF);
                } else {
                    list = dataSets;
                    z2 = isDrawValueAboveBarEnabled;
                    i = i6;
                }
                i6 = i + 1;
                dataSets = list;
                isDrawValueAboveBarEnabled = z2;
            }
        }
    }

    @Override // com.github.mikephil.charting.renderer.BarChartRenderer, com.github.mikephil.charting.renderer.DataRenderer
    public void drawValue(Canvas canvas, String str, float f, float f2, int i) {
        this.mValuePaint.setColor(i);
        canvas.drawText(str, f, f2, this.mValuePaint);
    }

    @Override // com.github.mikephil.charting.renderer.BarChartRenderer
    protected void prepareBarHighlight(float f, float f2, float f3, float f4, Transformer transformer) {
        this.mBarRect.set(f2, f - f4, f3, f + f4);
        transformer.rectToPixelPhaseHorizontal(this.mBarRect, this.mAnimator.getPhaseY());
    }

    @Override // com.github.mikephil.charting.renderer.BarChartRenderer
    protected void setHighlightDrawPos(Highlight highlight, RectF rectF) {
        highlight.setDraw(rectF.centerY(), rectF.right);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.github.mikephil.charting.renderer.DataRenderer
    public boolean isDrawingValuesAllowed(ChartInterface chartInterface) {
        return ((float) chartInterface.getData().getEntryCount()) < ((float) chartInterface.getMaxVisibleCount()) * this.mViewPortHandler.getScaleY();
    }
}
