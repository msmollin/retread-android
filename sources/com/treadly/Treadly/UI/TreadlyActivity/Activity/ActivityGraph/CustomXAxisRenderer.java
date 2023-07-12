package com.treadly.Treadly.UI.TreadlyActivity.Activity.ActivityGraph;

import android.graphics.Canvas;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.renderer.XAxisRenderer;
import com.github.mikephil.charting.utils.MPPointF;
import com.github.mikephil.charting.utils.Transformer;
import com.github.mikephil.charting.utils.Utils;
import com.github.mikephil.charting.utils.ViewPortHandler;

/* loaded from: classes2.dex */
public class CustomXAxisRenderer extends XAxisRenderer {
    public CustomXAxisRenderer(ViewPortHandler viewPortHandler, XAxis xAxis, Transformer transformer) {
        super(viewPortHandler, xAxis, transformer);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.github.mikephil.charting.renderer.XAxisRenderer
    public void drawLabel(Canvas canvas, String str, float f, float f2, MPPointF mPPointF, float f3) {
        String[] split = str.split("\n");
        if (split.length > 0) {
            Utils.drawXAxisValue(canvas, split[0], f, f2, this.mAxisLabelPaint, mPPointF, f3);
        }
        if (split.length > 1) {
            Utils.drawXAxisValue(canvas, split[1], f, f2 + this.mAxisLabelPaint.getTextSize(), this.mAxisLabelPaint, mPPointF, f3);
        }
    }
}
