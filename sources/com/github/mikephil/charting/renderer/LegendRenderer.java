package com.github.mikephil.charting.renderer;

import android.graphics.Canvas;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Typeface;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.LegendEntry;
import com.github.mikephil.charting.data.ChartData;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;
import com.github.mikephil.charting.interfaces.datasets.ICandleDataSet;
import com.github.mikephil.charting.interfaces.datasets.IPieDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.github.mikephil.charting.utils.FSize;
import com.github.mikephil.charting.utils.Utils;
import com.github.mikephil.charting.utils.ViewPortHandler;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/* loaded from: classes.dex */
public class LegendRenderer extends Renderer {
    protected List<LegendEntry> computedEntries;
    protected Paint.FontMetrics legendFontMetrics;
    protected Legend mLegend;
    protected Paint mLegendFormPaint;
    protected Paint mLegendLabelPaint;
    private Path mLineFormPath;

    public LegendRenderer(ViewPortHandler viewPortHandler, Legend legend) {
        super(viewPortHandler);
        this.computedEntries = new ArrayList(16);
        this.legendFontMetrics = new Paint.FontMetrics();
        this.mLineFormPath = new Path();
        this.mLegend = legend;
        this.mLegendLabelPaint = new Paint(1);
        this.mLegendLabelPaint.setTextSize(Utils.convertDpToPixel(9.0f));
        this.mLegendLabelPaint.setTextAlign(Paint.Align.LEFT);
        this.mLegendFormPaint = new Paint(1);
        this.mLegendFormPaint.setStyle(Paint.Style.FILL);
    }

    public Paint getLabelPaint() {
        return this.mLegendLabelPaint;
    }

    public Paint getFormPaint() {
        return this.mLegendFormPaint;
    }

    /* JADX WARN: Type inference failed for: r4v1, types: [com.github.mikephil.charting.interfaces.datasets.IDataSet] */
    /* JADX WARN: Type inference failed for: r7v1, types: [com.github.mikephil.charting.interfaces.datasets.IDataSet] */
    public void computeLegend(ChartData<?> chartData) {
        ChartData<?> chartData2;
        ChartData<?> chartData3 = chartData;
        if (!this.mLegend.isLegendCustom()) {
            this.computedEntries.clear();
            int i = 0;
            while (i < chartData.getDataSetCount()) {
                ?? dataSetByIndex = chartData3.getDataSetByIndex(i);
                List<Integer> colors = dataSetByIndex.getColors();
                int entryCount = dataSetByIndex.getEntryCount();
                if (dataSetByIndex instanceof IBarDataSet) {
                    IBarDataSet iBarDataSet = (IBarDataSet) dataSetByIndex;
                    if (iBarDataSet.isStacked()) {
                        String[] stackLabels = iBarDataSet.getStackLabels();
                        for (int i2 = 0; i2 < colors.size() && i2 < iBarDataSet.getStackSize(); i2++) {
                            this.computedEntries.add(new LegendEntry(stackLabels[i2 % stackLabels.length], dataSetByIndex.getForm(), dataSetByIndex.getFormSize(), dataSetByIndex.getFormLineWidth(), dataSetByIndex.getFormLineDashEffect(), colors.get(i2).intValue()));
                        }
                        if (iBarDataSet.getLabel() != null) {
                            this.computedEntries.add(new LegendEntry(dataSetByIndex.getLabel(), Legend.LegendForm.NONE, Float.NaN, Float.NaN, null, ColorTemplate.COLOR_NONE));
                        }
                        chartData2 = chartData3;
                        i++;
                        chartData3 = chartData2;
                    }
                }
                if (dataSetByIndex instanceof IPieDataSet) {
                    IPieDataSet iPieDataSet = (IPieDataSet) dataSetByIndex;
                    for (int i3 = 0; i3 < colors.size() && i3 < entryCount; i3++) {
                        this.computedEntries.add(new LegendEntry(iPieDataSet.getEntryForIndex(i3).getLabel(), dataSetByIndex.getForm(), dataSetByIndex.getFormSize(), dataSetByIndex.getFormLineWidth(), dataSetByIndex.getFormLineDashEffect(), colors.get(i3).intValue()));
                    }
                    if (iPieDataSet.getLabel() != null) {
                        this.computedEntries.add(new LegendEntry(dataSetByIndex.getLabel(), Legend.LegendForm.NONE, Float.NaN, Float.NaN, null, ColorTemplate.COLOR_NONE));
                    }
                } else {
                    if (dataSetByIndex instanceof ICandleDataSet) {
                        ICandleDataSet iCandleDataSet = (ICandleDataSet) dataSetByIndex;
                        if (iCandleDataSet.getDecreasingColor() != 1122867) {
                            int decreasingColor = iCandleDataSet.getDecreasingColor();
                            int increasingColor = iCandleDataSet.getIncreasingColor();
                            this.computedEntries.add(new LegendEntry(null, dataSetByIndex.getForm(), dataSetByIndex.getFormSize(), dataSetByIndex.getFormLineWidth(), dataSetByIndex.getFormLineDashEffect(), decreasingColor));
                            this.computedEntries.add(new LegendEntry(dataSetByIndex.getLabel(), dataSetByIndex.getForm(), dataSetByIndex.getFormSize(), dataSetByIndex.getFormLineWidth(), dataSetByIndex.getFormLineDashEffect(), increasingColor));
                        }
                    }
                    int i4 = 0;
                    while (i4 < colors.size() && i4 < entryCount) {
                        this.computedEntries.add(new LegendEntry((i4 >= colors.size() + (-1) || i4 >= entryCount + (-1)) ? chartData.getDataSetByIndex(i).getLabel() : null, dataSetByIndex.getForm(), dataSetByIndex.getFormSize(), dataSetByIndex.getFormLineWidth(), dataSetByIndex.getFormLineDashEffect(), colors.get(i4).intValue()));
                        i4++;
                    }
                }
                chartData2 = chartData;
                i++;
                chartData3 = chartData2;
            }
            if (this.mLegend.getExtraEntries() != null) {
                Collections.addAll(this.computedEntries, this.mLegend.getExtraEntries());
            }
            this.mLegend.setEntries(this.computedEntries);
        }
        Typeface typeface = this.mLegend.getTypeface();
        if (typeface != null) {
            this.mLegendLabelPaint.setTypeface(typeface);
        }
        this.mLegendLabelPaint.setTextSize(this.mLegend.getTextSize());
        this.mLegendLabelPaint.setColor(this.mLegend.getTextColor());
        this.mLegend.calculateDimensions(this.mLegendLabelPaint, this.mViewPortHandler);
    }

    public void renderLegend(Canvas canvas) {
        float f;
        float f2;
        float f3;
        float contentRight;
        float f4;
        float contentLeft;
        double d;
        float f5;
        float f6;
        List<FSize> list;
        float f7;
        List<FSize> list2;
        int i;
        Canvas canvas2;
        int i2;
        List<Boolean> list3;
        float f8;
        float f9;
        float f10;
        float contentTop;
        float contentBottom;
        Legend.LegendDirection legendDirection;
        float f11;
        LegendEntry legendEntry;
        float f12;
        float f13;
        if (this.mLegend.isEnabled()) {
            Typeface typeface = this.mLegend.getTypeface();
            if (typeface != null) {
                this.mLegendLabelPaint.setTypeface(typeface);
            }
            this.mLegendLabelPaint.setTextSize(this.mLegend.getTextSize());
            this.mLegendLabelPaint.setColor(this.mLegend.getTextColor());
            float lineHeight = Utils.getLineHeight(this.mLegendLabelPaint, this.legendFontMetrics);
            float lineSpacing = Utils.getLineSpacing(this.mLegendLabelPaint, this.legendFontMetrics) + Utils.convertDpToPixel(this.mLegend.getYEntrySpace());
            float calcTextHeight = lineHeight - (Utils.calcTextHeight(this.mLegendLabelPaint, "ABC") / 2.0f);
            LegendEntry[] entries = this.mLegend.getEntries();
            float convertDpToPixel = Utils.convertDpToPixel(this.mLegend.getFormToTextSpace());
            float convertDpToPixel2 = Utils.convertDpToPixel(this.mLegend.getXEntrySpace());
            Legend.LegendOrientation orientation = this.mLegend.getOrientation();
            Legend.LegendHorizontalAlignment horizontalAlignment = this.mLegend.getHorizontalAlignment();
            Legend.LegendVerticalAlignment verticalAlignment = this.mLegend.getVerticalAlignment();
            Legend.LegendDirection direction = this.mLegend.getDirection();
            float convertDpToPixel3 = Utils.convertDpToPixel(this.mLegend.getFormSize());
            float convertDpToPixel4 = Utils.convertDpToPixel(this.mLegend.getStackSpace());
            float yOffset = this.mLegend.getYOffset();
            float xOffset = this.mLegend.getXOffset();
            switch (horizontalAlignment) {
                case LEFT:
                    f = convertDpToPixel4;
                    f2 = lineHeight;
                    f3 = lineSpacing;
                    if (orientation != Legend.LegendOrientation.VERTICAL) {
                        xOffset += this.mViewPortHandler.contentLeft();
                    }
                    if (direction == Legend.LegendDirection.RIGHT_TO_LEFT) {
                        xOffset += this.mLegend.mNeededWidth;
                    }
                    f4 = xOffset;
                    break;
                case RIGHT:
                    f = convertDpToPixel4;
                    f2 = lineHeight;
                    f3 = lineSpacing;
                    if (orientation == Legend.LegendOrientation.VERTICAL) {
                        contentRight = this.mViewPortHandler.getChartWidth() - xOffset;
                    } else {
                        contentRight = this.mViewPortHandler.contentRight() - xOffset;
                    }
                    if (direction == Legend.LegendDirection.LEFT_TO_RIGHT) {
                        xOffset = contentRight - this.mLegend.mNeededWidth;
                        f4 = xOffset;
                        break;
                    }
                    f4 = contentRight;
                    break;
                case CENTER:
                    if (orientation == Legend.LegendOrientation.VERTICAL) {
                        contentLeft = this.mViewPortHandler.getChartWidth() / 2.0f;
                        f = convertDpToPixel4;
                    } else {
                        f = convertDpToPixel4;
                        contentLeft = this.mViewPortHandler.contentLeft() + (this.mViewPortHandler.contentWidth() / 2.0f);
                    }
                    contentRight = (direction == Legend.LegendDirection.LEFT_TO_RIGHT ? xOffset : -xOffset) + contentLeft;
                    if (orientation != Legend.LegendOrientation.VERTICAL) {
                        f2 = lineHeight;
                        f3 = lineSpacing;
                        f4 = contentRight;
                        break;
                    } else {
                        f3 = lineSpacing;
                        double d2 = contentRight;
                        if (direction == Legend.LegendDirection.LEFT_TO_RIGHT) {
                            f2 = lineHeight;
                            d = ((-this.mLegend.mNeededWidth) / 2.0d) + xOffset;
                        } else {
                            f2 = lineHeight;
                            d = (this.mLegend.mNeededWidth / 2.0d) - xOffset;
                        }
                        xOffset = (float) (d2 + d);
                        f4 = xOffset;
                        break;
                    }
                default:
                    f = convertDpToPixel4;
                    f2 = lineHeight;
                    f3 = lineSpacing;
                    f4 = 0.0f;
                    break;
            }
            switch (orientation) {
                case HORIZONTAL:
                    float f14 = f;
                    List<FSize> calculatedLineSizes = this.mLegend.getCalculatedLineSizes();
                    List<FSize> calculatedLabelSizes = this.mLegend.getCalculatedLabelSizes();
                    List<Boolean> calculatedLabelBreakPoints = this.mLegend.getCalculatedLabelBreakPoints();
                    switch (verticalAlignment) {
                        case TOP:
                            break;
                        case BOTTOM:
                            yOffset = (this.mViewPortHandler.getChartHeight() - yOffset) - this.mLegend.mNeededHeight;
                            break;
                        case CENTER:
                            yOffset += (this.mViewPortHandler.getChartHeight() - this.mLegend.mNeededHeight) / 2.0f;
                            break;
                        default:
                            yOffset = 0.0f;
                            break;
                    }
                    int length = entries.length;
                    float f15 = yOffset;
                    List<FSize> list4 = calculatedLabelSizes;
                    float f16 = f4;
                    int i3 = 0;
                    int i4 = 0;
                    while (i3 < length) {
                        float f17 = f14;
                        LegendEntry legendEntry2 = entries[i3];
                        int i5 = length;
                        boolean z = legendEntry2.form != Legend.LegendForm.NONE;
                        float convertDpToPixel5 = Float.isNaN(legendEntry2.formSize) ? convertDpToPixel3 : Utils.convertDpToPixel(legendEntry2.formSize);
                        if (i3 >= calculatedLabelBreakPoints.size() || !calculatedLabelBreakPoints.get(i3).booleanValue()) {
                            f5 = f15;
                        } else {
                            f5 = f15 + f2 + f3;
                            f16 = f4;
                        }
                        if (f16 == f4 && horizontalAlignment == Legend.LegendHorizontalAlignment.CENTER && i4 < calculatedLineSizes.size()) {
                            if (direction == Legend.LegendDirection.RIGHT_TO_LEFT) {
                                f10 = calculatedLineSizes.get(i4).width;
                            } else {
                                f10 = -calculatedLineSizes.get(i4).width;
                            }
                            f16 += f10 / 2.0f;
                            i4++;
                        }
                        int i6 = i4;
                        boolean z2 = legendEntry2.label == null;
                        if (z) {
                            if (direction == Legend.LegendDirection.RIGHT_TO_LEFT) {
                                f16 -= convertDpToPixel5;
                            }
                            i = i5;
                            f6 = f4;
                            i2 = i3;
                            list3 = calculatedLabelBreakPoints;
                            list = calculatedLineSizes;
                            list2 = list4;
                            f7 = calcTextHeight;
                            canvas2 = canvas;
                            drawForm(canvas, f16, f5 + calcTextHeight, legendEntry2, this.mLegend);
                            if (direction == Legend.LegendDirection.LEFT_TO_RIGHT) {
                                f16 += convertDpToPixel5;
                            }
                        } else {
                            f6 = f4;
                            list = calculatedLineSizes;
                            f7 = calcTextHeight;
                            list2 = list4;
                            i = i5;
                            canvas2 = canvas;
                            i2 = i3;
                            list3 = calculatedLabelBreakPoints;
                        }
                        if (!z2) {
                            if (z) {
                                f16 += direction == Legend.LegendDirection.RIGHT_TO_LEFT ? -convertDpToPixel : convertDpToPixel;
                            }
                            if (direction == Legend.LegendDirection.RIGHT_TO_LEFT) {
                                f16 -= list2.get(i2).width;
                            }
                            float f18 = f16;
                            drawLabel(canvas2, f18, f5 + f2, legendEntry2.label);
                            if (direction == Legend.LegendDirection.LEFT_TO_RIGHT) {
                                f18 += list2.get(i2).width;
                            }
                            f16 = f18 + (direction == Legend.LegendDirection.RIGHT_TO_LEFT ? -convertDpToPixel2 : convertDpToPixel2);
                            f8 = f17;
                        } else {
                            if (direction == Legend.LegendDirection.RIGHT_TO_LEFT) {
                                f8 = f17;
                                f9 = -f8;
                            } else {
                                f8 = f17;
                                f9 = f8;
                            }
                            f16 += f9;
                        }
                        i3 = i2 + 1;
                        f14 = f8;
                        list4 = list2;
                        f15 = f5;
                        i4 = i6;
                        length = i;
                        calculatedLabelBreakPoints = list3;
                        f4 = f6;
                        calculatedLineSizes = list;
                        calcTextHeight = f7;
                    }
                    return;
                case VERTICAL:
                    switch (verticalAlignment) {
                        case TOP:
                            contentTop = (horizontalAlignment == Legend.LegendHorizontalAlignment.CENTER ? 0.0f : this.mViewPortHandler.contentTop()) + yOffset;
                            break;
                        case BOTTOM:
                            if (horizontalAlignment == Legend.LegendHorizontalAlignment.CENTER) {
                                contentBottom = this.mViewPortHandler.getChartHeight();
                            } else {
                                contentBottom = this.mViewPortHandler.contentBottom();
                            }
                            contentTop = contentBottom - (this.mLegend.mNeededHeight + yOffset);
                            break;
                        case CENTER:
                            contentTop = ((this.mViewPortHandler.getChartHeight() / 2.0f) - (this.mLegend.mNeededHeight / 2.0f)) + this.mLegend.getYOffset();
                            break;
                        default:
                            contentTop = 0.0f;
                            break;
                    }
                    float f19 = contentTop;
                    float f20 = 0.0f;
                    int i7 = 0;
                    boolean z3 = false;
                    while (i7 < entries.length) {
                        LegendEntry legendEntry3 = entries[i7];
                        boolean z4 = legendEntry3.form != Legend.LegendForm.NONE;
                        float convertDpToPixel6 = Float.isNaN(legendEntry3.formSize) ? convertDpToPixel3 : Utils.convertDpToPixel(legendEntry3.formSize);
                        if (z4) {
                            f12 = direction == Legend.LegendDirection.LEFT_TO_RIGHT ? f4 + f20 : f4 - (convertDpToPixel6 - f20);
                            f11 = f;
                            legendDirection = direction;
                            drawForm(canvas, f12, f19 + calcTextHeight, legendEntry3, this.mLegend);
                            if (legendDirection == Legend.LegendDirection.LEFT_TO_RIGHT) {
                                f12 += convertDpToPixel6;
                            }
                            legendEntry = legendEntry3;
                        } else {
                            legendDirection = direction;
                            f11 = f;
                            legendEntry = legendEntry3;
                            f12 = f4;
                        }
                        if (legendEntry.label != null) {
                            if (!z4 || z3) {
                                f13 = z3 ? f4 : f12;
                            } else {
                                f13 = f12 + (legendDirection == Legend.LegendDirection.LEFT_TO_RIGHT ? convertDpToPixel : -convertDpToPixel);
                            }
                            if (legendDirection == Legend.LegendDirection.RIGHT_TO_LEFT) {
                                f13 -= Utils.calcTextWidth(this.mLegendLabelPaint, legendEntry.label);
                            }
                            if (!z3) {
                                drawLabel(canvas, f13, f19 + f2, legendEntry.label);
                            } else {
                                f19 += f2 + f3;
                                drawLabel(canvas, f13, f19 + f2, legendEntry.label);
                            }
                            f19 += f2 + f3;
                            f20 = 0.0f;
                        } else {
                            f20 += convertDpToPixel6 + f11;
                            z3 = true;
                        }
                        i7++;
                        f = f11;
                        direction = legendDirection;
                    }
                    return;
                default:
                    return;
            }
        }
    }

    protected void drawForm(Canvas canvas, float f, float f2, LegendEntry legendEntry, Legend legend) {
        if (legendEntry.formColor == 1122868 || legendEntry.formColor == 1122867 || legendEntry.formColor == 0) {
            return;
        }
        int save = canvas.save();
        Legend.LegendForm legendForm = legendEntry.form;
        if (legendForm == Legend.LegendForm.DEFAULT) {
            legendForm = legend.getForm();
        }
        this.mLegendFormPaint.setColor(legendEntry.formColor);
        float convertDpToPixel = Utils.convertDpToPixel(Float.isNaN(legendEntry.formSize) ? legend.getFormSize() : legendEntry.formSize);
        float f3 = convertDpToPixel / 2.0f;
        switch (legendForm) {
            case DEFAULT:
            case CIRCLE:
                this.mLegendFormPaint.setStyle(Paint.Style.FILL);
                canvas.drawCircle(f + f3, f2, f3, this.mLegendFormPaint);
                break;
            case SQUARE:
                this.mLegendFormPaint.setStyle(Paint.Style.FILL);
                canvas.drawRect(f, f2 - f3, f + convertDpToPixel, f2 + f3, this.mLegendFormPaint);
                break;
            case LINE:
                float convertDpToPixel2 = Utils.convertDpToPixel(Float.isNaN(legendEntry.formLineWidth) ? legend.getFormLineWidth() : legendEntry.formLineWidth);
                DashPathEffect formLineDashEffect = legendEntry.formLineDashEffect == null ? legend.getFormLineDashEffect() : legendEntry.formLineDashEffect;
                this.mLegendFormPaint.setStyle(Paint.Style.STROKE);
                this.mLegendFormPaint.setStrokeWidth(convertDpToPixel2);
                this.mLegendFormPaint.setPathEffect(formLineDashEffect);
                this.mLineFormPath.reset();
                this.mLineFormPath.moveTo(f, f2);
                this.mLineFormPath.lineTo(f + convertDpToPixel, f2);
                canvas.drawPath(this.mLineFormPath, this.mLegendFormPaint);
                break;
        }
        canvas.restoreToCount(save);
    }

    protected void drawLabel(Canvas canvas, float f, float f2, String str) {
        canvas.drawText(str, f, f2, this.mLegendLabelPaint);
    }
}
