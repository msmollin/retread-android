package com.github.mikephil.charting.renderer;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.os.Build;
import android.text.Layout;
import android.text.StaticLayout;
import android.text.TextPaint;
import androidx.core.view.ViewCompat;
import com.github.mikephil.charting.animation.ChartAnimator;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.interfaces.datasets.IPieDataSet;
import com.github.mikephil.charting.utils.MPPointF;
import com.github.mikephil.charting.utils.Utils;
import com.github.mikephil.charting.utils.ViewPortHandler;
import java.lang.ref.WeakReference;

/* loaded from: classes.dex */
public class PieChartRenderer extends DataRenderer {
    protected Canvas mBitmapCanvas;
    private RectF mCenterTextLastBounds;
    private CharSequence mCenterTextLastValue;
    private StaticLayout mCenterTextLayout;
    private TextPaint mCenterTextPaint;
    protected PieChart mChart;
    protected WeakReference<Bitmap> mDrawBitmap;
    protected Path mDrawCenterTextPathBuffer;
    protected RectF mDrawHighlightedRectF;
    private Paint mEntryLabelsPaint;
    private Path mHoleCirclePath;
    protected Paint mHolePaint;
    private RectF mInnerRectBuffer;
    private Path mPathBuffer;
    private RectF[] mRectBuffer;
    protected Paint mTransparentCirclePaint;
    protected Paint mValueLinePaint;

    @Override // com.github.mikephil.charting.renderer.DataRenderer
    public void initBuffers() {
    }

    public PieChartRenderer(PieChart pieChart, ChartAnimator chartAnimator, ViewPortHandler viewPortHandler) {
        super(chartAnimator, viewPortHandler);
        this.mCenterTextLastBounds = new RectF();
        this.mRectBuffer = new RectF[]{new RectF(), new RectF(), new RectF()};
        this.mPathBuffer = new Path();
        this.mInnerRectBuffer = new RectF();
        this.mHoleCirclePath = new Path();
        this.mDrawCenterTextPathBuffer = new Path();
        this.mDrawHighlightedRectF = new RectF();
        this.mChart = pieChart;
        this.mHolePaint = new Paint(1);
        this.mHolePaint.setColor(-1);
        this.mHolePaint.setStyle(Paint.Style.FILL);
        this.mTransparentCirclePaint = new Paint(1);
        this.mTransparentCirclePaint.setColor(-1);
        this.mTransparentCirclePaint.setStyle(Paint.Style.FILL);
        this.mTransparentCirclePaint.setAlpha(105);
        this.mCenterTextPaint = new TextPaint(1);
        this.mCenterTextPaint.setColor(ViewCompat.MEASURED_STATE_MASK);
        this.mCenterTextPaint.setTextSize(Utils.convertDpToPixel(12.0f));
        this.mValuePaint.setTextSize(Utils.convertDpToPixel(13.0f));
        this.mValuePaint.setColor(-1);
        this.mValuePaint.setTextAlign(Paint.Align.CENTER);
        this.mEntryLabelsPaint = new Paint(1);
        this.mEntryLabelsPaint.setColor(-1);
        this.mEntryLabelsPaint.setTextAlign(Paint.Align.CENTER);
        this.mEntryLabelsPaint.setTextSize(Utils.convertDpToPixel(13.0f));
        this.mValueLinePaint = new Paint(1);
        this.mValueLinePaint.setStyle(Paint.Style.STROKE);
    }

    public Paint getPaintHole() {
        return this.mHolePaint;
    }

    public Paint getPaintTransparentCircle() {
        return this.mTransparentCirclePaint;
    }

    public TextPaint getPaintCenterText() {
        return this.mCenterTextPaint;
    }

    public Paint getPaintEntryLabels() {
        return this.mEntryLabelsPaint;
    }

    @Override // com.github.mikephil.charting.renderer.DataRenderer
    public void drawData(Canvas canvas) {
        int chartWidth = (int) this.mViewPortHandler.getChartWidth();
        int chartHeight = (int) this.mViewPortHandler.getChartHeight();
        Bitmap bitmap = this.mDrawBitmap == null ? null : this.mDrawBitmap.get();
        if (bitmap == null || bitmap.getWidth() != chartWidth || bitmap.getHeight() != chartHeight) {
            if (chartWidth <= 0 || chartHeight <= 0) {
                return;
            }
            bitmap = Bitmap.createBitmap(chartWidth, chartHeight, Bitmap.Config.ARGB_4444);
            this.mDrawBitmap = new WeakReference<>(bitmap);
            this.mBitmapCanvas = new Canvas(bitmap);
        }
        bitmap.eraseColor(0);
        for (IPieDataSet iPieDataSet : ((PieData) this.mChart.getData()).getDataSets()) {
            if (iPieDataSet.isVisible() && iPieDataSet.getEntryCount() > 0) {
                drawDataSet(canvas, iPieDataSet);
            }
        }
    }

    protected float calculateMinimumRadiusForSpacedSlice(MPPointF mPPointF, float f, float f2, float f3, float f4, float f5, float f6) {
        double d = (f5 + f6) * 0.017453292f;
        float cos = mPPointF.x + (((float) Math.cos(d)) * f);
        float sin = mPPointF.y + (((float) Math.sin(d)) * f);
        double d2 = (f5 + (f6 / 2.0f)) * 0.017453292f;
        return (float) ((f - ((float) ((Math.sqrt(Math.pow(cos - f3, 2.0d) + Math.pow(sin - f4, 2.0d)) / 2.0d) * Math.tan(((180.0d - f2) / 2.0d) * 0.017453292519943295d)))) - Math.sqrt(Math.pow((mPPointF.x + (((float) Math.cos(d2)) * f)) - ((cos + f3) / 2.0f), 2.0d) + Math.pow((mPPointF.y + (((float) Math.sin(d2)) * f)) - ((sin + f4) / 2.0f), 2.0d)));
    }

    protected float getSliceSpace(IPieDataSet iPieDataSet) {
        if (!iPieDataSet.isAutomaticallyDisableSliceSpacingEnabled()) {
            return iPieDataSet.getSliceSpace();
        }
        if (iPieDataSet.getSliceSpace() / this.mViewPortHandler.getSmallestContentExtension() > (iPieDataSet.getYMin() / ((PieData) this.mChart.getData()).getYValueSum()) * 2.0f) {
            return 0.0f;
        }
        return iPieDataSet.getSliceSpace();
    }

    protected void drawDataSet(Canvas canvas, IPieDataSet iPieDataSet) {
        int i;
        int i2;
        int i3;
        float f;
        float f2;
        float[] fArr;
        float f3;
        RectF rectF;
        int i4;
        int i5;
        float f4;
        RectF rectF2;
        MPPointF mPPointF;
        float f5;
        MPPointF mPPointF2;
        RectF rectF3;
        int i6;
        float f6;
        MPPointF mPPointF3;
        IPieDataSet iPieDataSet2 = iPieDataSet;
        float rotationAngle = this.mChart.getRotationAngle();
        float phaseX = this.mAnimator.getPhaseX();
        float phaseY = this.mAnimator.getPhaseY();
        RectF circleBox = this.mChart.getCircleBox();
        int entryCount = iPieDataSet.getEntryCount();
        float[] drawAngles = this.mChart.getDrawAngles();
        MPPointF centerCircleBox = this.mChart.getCenterCircleBox();
        float radius = this.mChart.getRadius();
        boolean z = this.mChart.isDrawHoleEnabled() && !this.mChart.isDrawSlicesUnderHoleEnabled();
        float holeRadius = z ? (this.mChart.getHoleRadius() / 100.0f) * radius : 0.0f;
        float holeRadius2 = (radius - ((this.mChart.getHoleRadius() * radius) / 100.0f)) / 2.0f;
        RectF rectF4 = new RectF();
        boolean z2 = z && this.mChart.isDrawRoundedSlicesEnabled();
        int i7 = 0;
        for (int i8 = 0; i8 < entryCount; i8++) {
            if (Math.abs(iPieDataSet2.getEntryForIndex(i8).getY()) > Utils.FLOAT_EPSILON) {
                i7++;
            }
        }
        float sliceSpace = i7 <= 1 ? 0.0f : getSliceSpace(iPieDataSet2);
        int i9 = 0;
        float f7 = 0.0f;
        while (i9 < entryCount) {
            float f8 = drawAngles[i9];
            if (Math.abs(iPieDataSet2.getEntryForIndex(i9).getY()) <= Utils.FLOAT_EPSILON) {
                f7 += f8 * phaseX;
            } else if (!this.mChart.needsHighlight(i9) || z2) {
                boolean z3 = sliceSpace > 0.0f && f8 <= 180.0f;
                i = entryCount;
                this.mRenderPaint.setColor(iPieDataSet2.getColor(i9));
                float f9 = i7 == 1 ? 0.0f : sliceSpace / (radius * 0.017453292f);
                float f10 = rotationAngle + ((f7 + (f9 / 2.0f)) * phaseY);
                float f11 = (f8 - f9) * phaseY;
                float f12 = f11 < 0.0f ? 0.0f : f11;
                this.mPathBuffer.reset();
                if (z2) {
                    float f13 = radius - holeRadius2;
                    i2 = i9;
                    i3 = i7;
                    double d = f10 * 0.017453292f;
                    f = rotationAngle;
                    f2 = phaseX;
                    float cos = centerCircleBox.x + (((float) Math.cos(d)) * f13);
                    float sin = centerCircleBox.y + (f13 * ((float) Math.sin(d)));
                    rectF4.set(cos - holeRadius2, sin - holeRadius2, cos + holeRadius2, sin + holeRadius2);
                } else {
                    i2 = i9;
                    i3 = i7;
                    f = rotationAngle;
                    f2 = phaseX;
                }
                double d2 = f10 * 0.017453292f;
                float f14 = holeRadius;
                float cos2 = centerCircleBox.x + (((float) Math.cos(d2)) * radius);
                float sin2 = centerCircleBox.y + (((float) Math.sin(d2)) * radius);
                int i10 = (f12 > 360.0f ? 1 : (f12 == 360.0f ? 0 : -1));
                if (i10 >= 0 && f12 % 360.0f <= Utils.FLOAT_EPSILON) {
                    fArr = drawAngles;
                    this.mPathBuffer.addCircle(centerCircleBox.x, centerCircleBox.y, radius, Path.Direction.CW);
                } else {
                    fArr = drawAngles;
                    if (z2) {
                        this.mPathBuffer.arcTo(rectF4, f10 + 180.0f, -180.0f);
                    }
                    this.mPathBuffer.arcTo(circleBox, f10, f12);
                }
                RectF rectF5 = rectF4;
                this.mInnerRectBuffer.set(centerCircleBox.x - f14, centerCircleBox.y - f14, centerCircleBox.x + f14, centerCircleBox.y + f14);
                if (!z) {
                    f3 = radius;
                    rectF = circleBox;
                    i4 = i2;
                    i5 = i3;
                    f4 = f14;
                    rectF2 = rectF5;
                    mPPointF = centerCircleBox;
                    f5 = 360.0f;
                } else if (f14 > 0.0f || z3) {
                    if (z3) {
                        i5 = i3;
                        rectF = circleBox;
                        i4 = i2;
                        f4 = f14;
                        rectF3 = rectF5;
                        i6 = 1;
                        f3 = radius;
                        mPPointF2 = centerCircleBox;
                        float calculateMinimumRadiusForSpacedSlice = calculateMinimumRadiusForSpacedSlice(centerCircleBox, radius, f8 * phaseY, cos2, sin2, f10, f12);
                        if (calculateMinimumRadiusForSpacedSlice < 0.0f) {
                            calculateMinimumRadiusForSpacedSlice = -calculateMinimumRadiusForSpacedSlice;
                        }
                        f6 = Math.max(f4, calculateMinimumRadiusForSpacedSlice);
                    } else {
                        f3 = radius;
                        mPPointF2 = centerCircleBox;
                        rectF = circleBox;
                        i4 = i2;
                        i5 = i3;
                        f4 = f14;
                        rectF3 = rectF5;
                        i6 = 1;
                        f6 = f4;
                    }
                    float f15 = (i5 == i6 || f6 == 0.0f) ? 0.0f : sliceSpace / (f6 * 0.017453292f);
                    float f16 = f + ((f7 + (f15 / 2.0f)) * phaseY);
                    float f17 = (f8 - f15) * phaseY;
                    if (f17 < 0.0f) {
                        f17 = 0.0f;
                    }
                    float f18 = f16 + f17;
                    if (i10 >= 0 && f12 % 360.0f <= Utils.FLOAT_EPSILON) {
                        this.mPathBuffer.addCircle(mPPointF2.x, mPPointF2.y, f6, Path.Direction.CCW);
                        mPPointF3 = mPPointF2;
                        rectF2 = rectF3;
                    } else {
                        if (z2) {
                            float f19 = f3 - holeRadius2;
                            double d3 = 0.017453292f * f18;
                            float cos3 = mPPointF2.x + (((float) Math.cos(d3)) * f19);
                            mPPointF3 = mPPointF2;
                            float sin3 = mPPointF3.y + (f19 * ((float) Math.sin(d3)));
                            rectF2 = rectF3;
                            rectF2.set(cos3 - holeRadius2, sin3 - holeRadius2, cos3 + holeRadius2, sin3 + holeRadius2);
                            this.mPathBuffer.arcTo(rectF2, f18, 180.0f);
                        } else {
                            mPPointF3 = mPPointF2;
                            rectF2 = rectF3;
                            double d4 = f18 * 0.017453292f;
                            this.mPathBuffer.lineTo(mPPointF3.x + (((float) Math.cos(d4)) * f6), mPPointF3.y + (f6 * ((float) Math.sin(d4))));
                        }
                        this.mPathBuffer.arcTo(this.mInnerRectBuffer, f18, -f17);
                    }
                    mPPointF = mPPointF3;
                    this.mPathBuffer.close();
                    this.mBitmapCanvas.drawPath(this.mPathBuffer, this.mRenderPaint);
                    f7 += f8 * f2;
                    i9 = i4 + 1;
                    holeRadius = f4;
                    rectF4 = rectF2;
                    centerCircleBox = mPPointF;
                    i7 = i5;
                    radius = f3;
                    entryCount = i;
                    rotationAngle = f;
                    phaseX = f2;
                    drawAngles = fArr;
                    circleBox = rectF;
                    iPieDataSet2 = iPieDataSet;
                } else {
                    f3 = radius;
                    rectF = circleBox;
                    i4 = i2;
                    i5 = i3;
                    f4 = f14;
                    rectF2 = rectF5;
                    f5 = 360.0f;
                    mPPointF = centerCircleBox;
                }
                if (f12 % f5 > Utils.FLOAT_EPSILON) {
                    if (z3) {
                        float calculateMinimumRadiusForSpacedSlice2 = calculateMinimumRadiusForSpacedSlice(mPPointF, f3, f8 * phaseY, cos2, sin2, f10, f12);
                        double d5 = 0.017453292f * (f10 + (f12 / 2.0f));
                        this.mPathBuffer.lineTo(mPPointF.x + (((float) Math.cos(d5)) * calculateMinimumRadiusForSpacedSlice2), mPPointF.y + (calculateMinimumRadiusForSpacedSlice2 * ((float) Math.sin(d5))));
                    } else {
                        this.mPathBuffer.lineTo(mPPointF.x, mPPointF.y);
                    }
                }
                this.mPathBuffer.close();
                this.mBitmapCanvas.drawPath(this.mPathBuffer, this.mRenderPaint);
                f7 += f8 * f2;
                i9 = i4 + 1;
                holeRadius = f4;
                rectF4 = rectF2;
                centerCircleBox = mPPointF;
                i7 = i5;
                radius = f3;
                entryCount = i;
                rotationAngle = f;
                phaseX = f2;
                drawAngles = fArr;
                circleBox = rectF;
                iPieDataSet2 = iPieDataSet;
            } else {
                f7 += f8 * phaseX;
            }
            i4 = i9;
            f3 = radius;
            f = rotationAngle;
            f2 = phaseX;
            rectF = circleBox;
            i = entryCount;
            fArr = drawAngles;
            i5 = i7;
            rectF2 = rectF4;
            f4 = holeRadius;
            mPPointF = centerCircleBox;
            i9 = i4 + 1;
            holeRadius = f4;
            rectF4 = rectF2;
            centerCircleBox = mPPointF;
            i7 = i5;
            radius = f3;
            entryCount = i;
            rotationAngle = f;
            phaseX = f2;
            drawAngles = fArr;
            circleBox = rectF;
            iPieDataSet2 = iPieDataSet;
        }
        MPPointF.recycleInstance(centerCircleBox);
    }

    /* JADX WARN: Removed duplicated region for block: B:104:0x0363 A[ADDED_TO_REGION] */
    /* JADX WARN: Removed duplicated region for block: B:110:0x0383  */
    /* JADX WARN: Removed duplicated region for block: B:114:0x0394  */
    /* JADX WARN: Removed duplicated region for block: B:119:0x03af  */
    /* JADX WARN: Removed duplicated region for block: B:39:0x01b6  */
    /* JADX WARN: Removed duplicated region for block: B:42:0x01be  */
    /* JADX WARN: Removed duplicated region for block: B:45:0x01c4  */
    /* JADX WARN: Removed duplicated region for block: B:48:0x01cd  */
    /* JADX WARN: Removed duplicated region for block: B:51:0x01d3 A[ADDED_TO_REGION] */
    /* JADX WARN: Removed duplicated region for block: B:56:0x0211  */
    /* JADX WARN: Removed duplicated region for block: B:57:0x021a  */
    /* JADX WARN: Removed duplicated region for block: B:60:0x0222  */
    /* JADX WARN: Removed duplicated region for block: B:61:0x0230  */
    /* JADX WARN: Removed duplicated region for block: B:64:0x0267  */
    /* JADX WARN: Removed duplicated region for block: B:72:0x0293  */
    /* JADX WARN: Removed duplicated region for block: B:76:0x02a6  */
    /* JADX WARN: Removed duplicated region for block: B:80:0x02dc  */
    /* JADX WARN: Removed duplicated region for block: B:82:0x02ef A[ADDED_TO_REGION] */
    /* JADX WARN: Removed duplicated region for block: B:90:0x031c  */
    /* JADX WARN: Removed duplicated region for block: B:94:0x032d  */
    /* JADX WARN: Removed duplicated region for block: B:98:0x0344 A[ADDED_TO_REGION] */
    @Override // com.github.mikephil.charting.renderer.DataRenderer
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public void drawValues(android.graphics.Canvas r56) {
        /*
            Method dump skipped, instructions count: 1091
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: com.github.mikephil.charting.renderer.PieChartRenderer.drawValues(android.graphics.Canvas):void");
    }

    @Override // com.github.mikephil.charting.renderer.DataRenderer
    public void drawValue(Canvas canvas, String str, float f, float f2, int i) {
        this.mValuePaint.setColor(i);
        canvas.drawText(str, f, f2, this.mValuePaint);
    }

    protected void drawEntryLabel(Canvas canvas, String str, float f, float f2) {
        canvas.drawText(str, f, f2, this.mEntryLabelsPaint);
    }

    @Override // com.github.mikephil.charting.renderer.DataRenderer
    public void drawExtras(Canvas canvas) {
        drawHole(canvas);
        canvas.drawBitmap(this.mDrawBitmap.get(), 0.0f, 0.0f, (Paint) null);
        drawCenterText(canvas);
    }

    protected void drawHole(Canvas canvas) {
        if (!this.mChart.isDrawHoleEnabled() || this.mBitmapCanvas == null) {
            return;
        }
        float radius = this.mChart.getRadius();
        float holeRadius = (this.mChart.getHoleRadius() / 100.0f) * radius;
        MPPointF centerCircleBox = this.mChart.getCenterCircleBox();
        if (Color.alpha(this.mHolePaint.getColor()) > 0) {
            this.mBitmapCanvas.drawCircle(centerCircleBox.x, centerCircleBox.y, holeRadius, this.mHolePaint);
        }
        if (Color.alpha(this.mTransparentCirclePaint.getColor()) > 0 && this.mChart.getTransparentCircleRadius() > this.mChart.getHoleRadius()) {
            int alpha = this.mTransparentCirclePaint.getAlpha();
            float transparentCircleRadius = radius * (this.mChart.getTransparentCircleRadius() / 100.0f);
            this.mTransparentCirclePaint.setAlpha((int) (alpha * this.mAnimator.getPhaseX() * this.mAnimator.getPhaseY()));
            this.mHoleCirclePath.reset();
            this.mHoleCirclePath.addCircle(centerCircleBox.x, centerCircleBox.y, transparentCircleRadius, Path.Direction.CW);
            this.mHoleCirclePath.addCircle(centerCircleBox.x, centerCircleBox.y, holeRadius, Path.Direction.CCW);
            this.mBitmapCanvas.drawPath(this.mHoleCirclePath, this.mTransparentCirclePaint);
            this.mTransparentCirclePaint.setAlpha(alpha);
        }
        MPPointF.recycleInstance(centerCircleBox);
    }

    protected void drawCenterText(Canvas canvas) {
        float radius;
        MPPointF mPPointF;
        CharSequence centerText = this.mChart.getCenterText();
        if (!this.mChart.isDrawCenterTextEnabled() || centerText == null) {
            return;
        }
        MPPointF centerCircleBox = this.mChart.getCenterCircleBox();
        MPPointF centerTextOffset = this.mChart.getCenterTextOffset();
        float f = centerCircleBox.x + centerTextOffset.x;
        float f2 = centerCircleBox.y + centerTextOffset.y;
        if (this.mChart.isDrawHoleEnabled() && !this.mChart.isDrawSlicesUnderHoleEnabled()) {
            radius = this.mChart.getRadius() * (this.mChart.getHoleRadius() / 100.0f);
        } else {
            radius = this.mChart.getRadius();
        }
        RectF rectF = this.mRectBuffer[0];
        rectF.left = f - radius;
        rectF.top = f2 - radius;
        rectF.right = f + radius;
        rectF.bottom = f2 + radius;
        RectF rectF2 = this.mRectBuffer[1];
        rectF2.set(rectF);
        float centerTextRadiusPercent = this.mChart.getCenterTextRadiusPercent() / 100.0f;
        if (centerTextRadiusPercent > Utils.DOUBLE_EPSILON) {
            rectF2.inset((rectF2.width() - (rectF2.width() * centerTextRadiusPercent)) / 2.0f, (rectF2.height() - (rectF2.height() * centerTextRadiusPercent)) / 2.0f);
        }
        if (centerText.equals(this.mCenterTextLastValue) && rectF2.equals(this.mCenterTextLastBounds)) {
            mPPointF = centerTextOffset;
        } else {
            this.mCenterTextLastBounds.set(rectF2);
            this.mCenterTextLastValue = centerText;
            mPPointF = centerTextOffset;
            this.mCenterTextLayout = new StaticLayout(centerText, 0, centerText.length(), this.mCenterTextPaint, (int) Math.max(Math.ceil(this.mCenterTextLastBounds.width()), 1.0d), Layout.Alignment.ALIGN_CENTER, 1.0f, 0.0f, false);
        }
        float height = this.mCenterTextLayout.getHeight();
        canvas.save();
        if (Build.VERSION.SDK_INT >= 18) {
            Path path = this.mDrawCenterTextPathBuffer;
            path.reset();
            path.addOval(rectF, Path.Direction.CW);
            canvas.clipPath(path);
        }
        canvas.translate(rectF2.left, rectF2.top + ((rectF2.height() - height) / 2.0f));
        this.mCenterTextLayout.draw(canvas);
        canvas.restore();
        MPPointF.recycleInstance(centerCircleBox);
        MPPointF.recycleInstance(mPPointF);
    }

    @Override // com.github.mikephil.charting.renderer.DataRenderer
    public void drawHighlighted(Canvas canvas, Highlight[] highlightArr) {
        IPieDataSet dataSetByIndex;
        float f;
        int i;
        float[] fArr;
        float f2;
        int i2;
        boolean z;
        RectF rectF;
        MPPointF mPPointF;
        int i3;
        float f3;
        float[] fArr2;
        float f4;
        float f5;
        float f6;
        float f7;
        Highlight[] highlightArr2 = highlightArr;
        boolean z2 = this.mChart.isDrawHoleEnabled() && !this.mChart.isDrawSlicesUnderHoleEnabled();
        if (z2 && this.mChart.isDrawRoundedSlicesEnabled()) {
            return;
        }
        float phaseX = this.mAnimator.getPhaseX();
        float phaseY = this.mAnimator.getPhaseY();
        float rotationAngle = this.mChart.getRotationAngle();
        float[] drawAngles = this.mChart.getDrawAngles();
        float[] absoluteAngles = this.mChart.getAbsoluteAngles();
        MPPointF centerCircleBox = this.mChart.getCenterCircleBox();
        float radius = this.mChart.getRadius();
        float holeRadius = z2 ? (this.mChart.getHoleRadius() / 100.0f) * radius : 0.0f;
        RectF rectF2 = this.mDrawHighlightedRectF;
        rectF2.set(0.0f, 0.0f, 0.0f, 0.0f);
        int i4 = 0;
        while (i4 < highlightArr2.length) {
            int x = (int) highlightArr2[i4].getX();
            if (x < drawAngles.length && (dataSetByIndex = ((PieData) this.mChart.getData()).getDataSetByIndex(highlightArr2[i4].getDataSetIndex())) != null && dataSetByIndex.isHighlightEnabled()) {
                int entryCount = dataSetByIndex.getEntryCount();
                int i5 = 0;
                for (int i6 = 0; i6 < entryCount; i6++) {
                    if (Math.abs(dataSetByIndex.getEntryForIndex(i6).getY()) > Utils.FLOAT_EPSILON) {
                        i5++;
                    }
                }
                if (x == 0) {
                    i = 1;
                    f = 0.0f;
                } else {
                    f = absoluteAngles[x - 1] * phaseX;
                    i = 1;
                }
                float sliceSpace = i5 <= i ? 0.0f : dataSetByIndex.getSliceSpace();
                float f8 = drawAngles[x];
                float selectionShift = dataSetByIndex.getSelectionShift();
                int i7 = i4;
                float f9 = radius + selectionShift;
                float f10 = holeRadius;
                rectF2.set(this.mChart.getCircleBox());
                float f11 = -selectionShift;
                rectF2.inset(f11, f11);
                boolean z3 = sliceSpace > 0.0f && f8 <= 180.0f;
                this.mRenderPaint.setColor(dataSetByIndex.getColor(x));
                float f12 = i5 == 1 ? 0.0f : sliceSpace / (radius * 0.017453292f);
                float f13 = i5 == 1 ? 0.0f : sliceSpace / (f9 * 0.017453292f);
                float f14 = rotationAngle + (((f12 / 2.0f) + f) * phaseY);
                float f15 = (f8 - f12) * phaseY;
                float f16 = f15 < 0.0f ? 0.0f : f15;
                float f17 = (((f13 / 2.0f) + f) * phaseY) + rotationAngle;
                float f18 = (f8 - f13) * phaseY;
                if (f18 < 0.0f) {
                    f18 = 0.0f;
                }
                this.mPathBuffer.reset();
                int i8 = (f16 > 360.0f ? 1 : (f16 == 360.0f ? 0 : -1));
                if (i8 >= 0 && f16 % 360.0f <= Utils.FLOAT_EPSILON) {
                    this.mPathBuffer.addCircle(centerCircleBox.x, centerCircleBox.y, f9, Path.Direction.CW);
                    fArr = drawAngles;
                    f2 = f;
                    i2 = i5;
                    z = z2;
                } else {
                    fArr = drawAngles;
                    f2 = f;
                    double d = f17 * 0.017453292f;
                    i2 = i5;
                    z = z2;
                    this.mPathBuffer.moveTo(centerCircleBox.x + (((float) Math.cos(d)) * f9), centerCircleBox.y + (f9 * ((float) Math.sin(d))));
                    this.mPathBuffer.arcTo(rectF2, f17, f18);
                }
                if (z3) {
                    double d2 = f14 * 0.017453292f;
                    i3 = i7;
                    rectF = rectF2;
                    f3 = f10;
                    mPPointF = centerCircleBox;
                    fArr2 = fArr;
                    f4 = calculateMinimumRadiusForSpacedSlice(centerCircleBox, radius, f8 * phaseY, (((float) Math.cos(d2)) * radius) + centerCircleBox.x, centerCircleBox.y + (((float) Math.sin(d2)) * radius), f14, f16);
                } else {
                    rectF = rectF2;
                    mPPointF = centerCircleBox;
                    i3 = i7;
                    f3 = f10;
                    fArr2 = fArr;
                    f4 = 0.0f;
                }
                this.mInnerRectBuffer.set(mPPointF.x - f3, mPPointF.y - f3, mPPointF.x + f3, mPPointF.y + f3);
                if (z && (f3 > 0.0f || z3)) {
                    if (z3) {
                        if (f4 < 0.0f) {
                            f4 = -f4;
                        }
                        f7 = Math.max(f3, f4);
                    } else {
                        f7 = f3;
                    }
                    float f19 = (i2 == 1 || f7 == 0.0f) ? 0.0f : sliceSpace / (f7 * 0.017453292f);
                    float f20 = ((f2 + (f19 / 2.0f)) * phaseY) + rotationAngle;
                    float f21 = (f8 - f19) * phaseY;
                    if (f21 < 0.0f) {
                        f21 = 0.0f;
                    }
                    float f22 = f20 + f21;
                    if (i8 >= 0 && f16 % 360.0f <= Utils.FLOAT_EPSILON) {
                        this.mPathBuffer.addCircle(mPPointF.x, mPPointF.y, f7, Path.Direction.CCW);
                        f5 = phaseX;
                        f6 = phaseY;
                    } else {
                        double d3 = f22 * 0.017453292f;
                        f5 = phaseX;
                        f6 = phaseY;
                        this.mPathBuffer.lineTo(mPPointF.x + (((float) Math.cos(d3)) * f7), mPPointF.y + (f7 * ((float) Math.sin(d3))));
                        this.mPathBuffer.arcTo(this.mInnerRectBuffer, f22, -f21);
                    }
                } else {
                    f5 = phaseX;
                    f6 = phaseY;
                    if (f16 % 360.0f > Utils.FLOAT_EPSILON) {
                        if (z3) {
                            double d4 = (f14 + (f16 / 2.0f)) * 0.017453292f;
                            this.mPathBuffer.lineTo(mPPointF.x + (((float) Math.cos(d4)) * f4), mPPointF.y + (f4 * ((float) Math.sin(d4))));
                        } else {
                            this.mPathBuffer.lineTo(mPPointF.x, mPPointF.y);
                        }
                    }
                }
                this.mPathBuffer.close();
                this.mBitmapCanvas.drawPath(this.mPathBuffer, this.mRenderPaint);
            } else {
                i3 = i4;
                rectF = rectF2;
                f3 = holeRadius;
                fArr2 = drawAngles;
                z = z2;
                f5 = phaseX;
                f6 = phaseY;
                mPPointF = centerCircleBox;
            }
            i4 = i3 + 1;
            rectF2 = rectF;
            holeRadius = f3;
            centerCircleBox = mPPointF;
            drawAngles = fArr2;
            z2 = z;
            phaseX = f5;
            phaseY = f6;
            highlightArr2 = highlightArr;
        }
        MPPointF.recycleInstance(centerCircleBox);
    }

    protected void drawRoundedSlices(Canvas canvas) {
        float f;
        float[] fArr;
        float f2;
        if (this.mChart.isDrawRoundedSlicesEnabled()) {
            IPieDataSet dataSet = ((PieData) this.mChart.getData()).getDataSet();
            if (dataSet.isVisible()) {
                float phaseX = this.mAnimator.getPhaseX();
                float phaseY = this.mAnimator.getPhaseY();
                MPPointF centerCircleBox = this.mChart.getCenterCircleBox();
                float radius = this.mChart.getRadius();
                float holeRadius = (radius - ((this.mChart.getHoleRadius() * radius) / 100.0f)) / 2.0f;
                float[] drawAngles = this.mChart.getDrawAngles();
                float rotationAngle = this.mChart.getRotationAngle();
                int i = 0;
                while (i < dataSet.getEntryCount()) {
                    float f3 = drawAngles[i];
                    if (Math.abs(dataSet.getEntryForIndex(i).getY()) > Utils.FLOAT_EPSILON) {
                        double d = radius - holeRadius;
                        double d2 = (rotationAngle + f3) * phaseY;
                        f = phaseY;
                        fArr = drawAngles;
                        f2 = rotationAngle;
                        float cos = (float) (centerCircleBox.x + (Math.cos(Math.toRadians(d2)) * d));
                        float sin = (float) ((d * Math.sin(Math.toRadians(d2))) + centerCircleBox.y);
                        this.mRenderPaint.setColor(dataSet.getColor(i));
                        this.mBitmapCanvas.drawCircle(cos, sin, holeRadius, this.mRenderPaint);
                    } else {
                        f = phaseY;
                        fArr = drawAngles;
                        f2 = rotationAngle;
                    }
                    rotationAngle = f2 + (f3 * phaseX);
                    i++;
                    phaseY = f;
                    drawAngles = fArr;
                }
                MPPointF.recycleInstance(centerCircleBox);
            }
        }
    }

    public void releaseBitmap() {
        if (this.mBitmapCanvas != null) {
            this.mBitmapCanvas.setBitmap(null);
            this.mBitmapCanvas = null;
        }
        if (this.mDrawBitmap != null) {
            Bitmap bitmap = this.mDrawBitmap.get();
            if (bitmap != null) {
                bitmap.recycle();
            }
            this.mDrawBitmap.clear();
            this.mDrawBitmap = null;
        }
    }
}
