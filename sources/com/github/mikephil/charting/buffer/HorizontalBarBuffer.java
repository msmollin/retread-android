package com.github.mikephil.charting.buffer;

import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;

/* loaded from: classes.dex */
public class HorizontalBarBuffer extends BarBuffer {
    public HorizontalBarBuffer(int i, int i2, boolean z) {
        super(i, i2, z);
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // com.github.mikephil.charting.buffer.BarBuffer, com.github.mikephil.charting.buffer.AbstractBuffer
    public void feed(IBarDataSet iBarDataSet) {
        float f;
        float f2;
        float f3;
        float f4;
        float f5;
        float entryCount = iBarDataSet.getEntryCount() * this.phaseX;
        float f6 = this.mBarWidth / 2.0f;
        for (int i = 0; i < entryCount; i++) {
            BarEntry barEntry = (BarEntry) iBarDataSet.getEntryForIndex(i);
            if (barEntry != null) {
                float x = barEntry.getX();
                float y = barEntry.getY();
                float[] yVals = barEntry.getYVals();
                if (!this.mContainsStacks || yVals == null) {
                    float f7 = x - f6;
                    float f8 = x + f6;
                    if (this.mInverted) {
                        float f9 = y >= 0.0f ? y : 0.0f;
                        if (y > 0.0f) {
                            y = 0.0f;
                        }
                        float f10 = y;
                        y = f9;
                        f = f10;
                    } else {
                        f = y >= 0.0f ? y : 0.0f;
                        if (y > 0.0f) {
                            y = 0.0f;
                        }
                    }
                    if (f > 0.0f) {
                        f *= this.phaseY;
                    } else {
                        y *= this.phaseY;
                    }
                    addBar(y, f8, f, f7);
                } else {
                    float f11 = -barEntry.getNegativeSum();
                    float f12 = 0.0f;
                    int i2 = 0;
                    while (i2 < yVals.length) {
                        float f13 = yVals[i2];
                        if (f13 >= 0.0f) {
                            f4 = f13 + f12;
                            f3 = f11;
                            f2 = f4;
                        } else {
                            float abs = Math.abs(f13) + f11;
                            float abs2 = Math.abs(f13) + f11;
                            float f14 = f11;
                            f2 = f12;
                            f12 = f14;
                            f3 = abs2;
                            f4 = abs;
                        }
                        float f15 = x - f6;
                        float f16 = x + f6;
                        if (this.mInverted) {
                            float f17 = f12 >= f4 ? f12 : f4;
                            if (f12 > f4) {
                                f12 = f4;
                            }
                            float f18 = f12;
                            f12 = f17;
                            f5 = f18;
                        } else {
                            f5 = f12 >= f4 ? f12 : f4;
                            if (f12 > f4) {
                                f12 = f4;
                            }
                        }
                        addBar(f12 * this.phaseY, f16, f5 * this.phaseY, f15);
                        i2++;
                        f12 = f2;
                        f11 = f3;
                    }
                }
            }
        }
        reset();
    }
}
