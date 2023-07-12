package org.otwebrtc;

import com.github.mikephil.charting.utils.Utils;

/* loaded from: classes2.dex */
class DynamicBitrateAdjuster extends BaseBitrateAdjuster {
    private static final double BITRATE_ADJUSTMENT_MAX_SCALE = 4.0d;
    private static final double BITRATE_ADJUSTMENT_SEC = 3.0d;
    private static final int BITRATE_ADJUSTMENT_STEPS = 20;
    private static final double BITS_PER_BYTE = 8.0d;
    private int bitrateAdjustmentScaleExp;
    private double deviationBytes;
    private double timeSinceLastAdjustmentMs;

    private double getBitrateAdjustmentScale() {
        return Math.pow(BITRATE_ADJUSTMENT_MAX_SCALE, this.bitrateAdjustmentScaleExp / 20.0d);
    }

    @Override // org.otwebrtc.BaseBitrateAdjuster, org.otwebrtc.BitrateAdjuster
    public int getAdjustedBitrateBps() {
        return (int) (this.targetBitrateBps * getBitrateAdjustmentScale());
    }

    @Override // org.otwebrtc.BaseBitrateAdjuster, org.otwebrtc.BitrateAdjuster
    public void reportEncodedFrame(int i) {
        int i2 = this.targetFps;
        if (i2 == 0) {
            return;
        }
        double d = this.targetBitrateBps / BITS_PER_BYTE;
        double d2 = i2;
        double d3 = this.deviationBytes + (i - (d / d2));
        this.deviationBytes = d3;
        this.timeSinceLastAdjustmentMs += 1000.0d / d2;
        double d4 = BITRATE_ADJUSTMENT_SEC * d;
        double min = Math.min(d3, d4);
        this.deviationBytes = min;
        double max = Math.max(min, -d4);
        this.deviationBytes = max;
        if (this.timeSinceLastAdjustmentMs <= 3000.0d) {
            return;
        }
        if (max > d) {
            int i3 = this.bitrateAdjustmentScaleExp - ((int) ((max / d) + 0.5d));
            this.bitrateAdjustmentScaleExp = i3;
            this.bitrateAdjustmentScaleExp = Math.max(i3, -20);
            this.deviationBytes = d;
        } else {
            double d5 = -d;
            if (max < d5) {
                int i4 = this.bitrateAdjustmentScaleExp + ((int) (((-max) / d) + 0.5d));
                this.bitrateAdjustmentScaleExp = i4;
                this.bitrateAdjustmentScaleExp = Math.min(i4, 20);
                this.deviationBytes = d5;
            }
        }
        this.timeSinceLastAdjustmentMs = Utils.DOUBLE_EPSILON;
    }

    @Override // org.otwebrtc.BaseBitrateAdjuster, org.otwebrtc.BitrateAdjuster
    public void setTargets(int i, int i2) {
        int i3 = this.targetBitrateBps;
        if (i3 > 0 && i < i3) {
            this.deviationBytes = (this.deviationBytes * i) / i3;
        }
        super.setTargets(i, i2);
    }
}
