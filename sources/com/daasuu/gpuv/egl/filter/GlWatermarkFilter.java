package com.daasuu.gpuv.egl.filter;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;

/* loaded from: classes.dex */
public class GlWatermarkFilter extends GlOverlayFilter {
    private Bitmap bitmap;
    private Position position;

    /* loaded from: classes.dex */
    public enum Position {
        LEFT_TOP,
        LEFT_BOTTOM,
        RIGHT_TOP,
        RIGHT_BOTTOM
    }

    public GlWatermarkFilter(Bitmap bitmap) {
        this.position = Position.LEFT_TOP;
        this.bitmap = bitmap;
    }

    public GlWatermarkFilter(Bitmap bitmap, Position position) {
        this.position = Position.LEFT_TOP;
        this.bitmap = bitmap;
        this.position = position;
    }

    @Override // com.daasuu.gpuv.egl.filter.GlOverlayFilter
    protected void drawCanvas(Canvas canvas) {
        if (this.bitmap == null || this.bitmap.isRecycled()) {
            return;
        }
        switch (this.position) {
            case LEFT_TOP:
                canvas.drawBitmap(this.bitmap, 0.0f, 0.0f, (Paint) null);
                return;
            case LEFT_BOTTOM:
                canvas.drawBitmap(this.bitmap, 0.0f, canvas.getHeight() - this.bitmap.getHeight(), (Paint) null);
                return;
            case RIGHT_TOP:
                canvas.drawBitmap(this.bitmap, canvas.getWidth() - this.bitmap.getWidth(), 0.0f, (Paint) null);
                return;
            case RIGHT_BOTTOM:
                canvas.drawBitmap(this.bitmap, canvas.getWidth() - this.bitmap.getWidth(), canvas.getHeight() - this.bitmap.getHeight(), (Paint) null);
                return;
            default:
                return;
        }
    }
}
