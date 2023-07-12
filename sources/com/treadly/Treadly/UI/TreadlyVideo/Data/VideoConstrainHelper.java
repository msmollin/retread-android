package com.treadly.Treadly.UI.TreadlyVideo.Data;

import android.os.Build;
import android.transition.TransitionManager;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;

/* loaded from: classes2.dex */
public class VideoConstrainHelper {
    private int container;
    private ConstraintSet set = new ConstraintSet();

    public VideoConstrainHelper(int i) {
        this.container = i;
    }

    public void layoutViewAboveView(int i, int i2) {
        this.set.constrainHeight(i, 0);
        this.set.constrainHeight(i2, 0);
        this.set.connect(i, 4, i, 3);
        this.set.connect(i2, 3, i, 4);
    }

    public void layoutViewWithTopBound(int i, int i2) {
        this.set.connect(i, 3, i2, 3);
    }

    public void layoutViewWithBottomBound(int i, int i2) {
        this.set.connect(i, 4, i2, 4);
    }

    private void layoutViewNextToView(int i, int i2, int i3, int i4) {
        this.set.constrainWidth(i, 0);
        this.set.constrainWidth(i2, 0);
        this.set.connect(i, 1, i3, 1);
        this.set.connect(i, 2, i2, 1);
        this.set.connect(i2, 2, i4, 2);
        this.set.connect(i2, 1, i, 2);
    }

    private void layoutViewOccupyingAllRow(int i, int i2, int i3) {
        this.set.constrainWidth(i, 0);
        this.set.connect(i, 1, i2, 1);
        this.set.connect(i, 2, i3, 2);
    }

    public void layoutTwoViewsOccupyingAllRow(int i, int i2) {
        layoutViewNextToView(i, i2, this.container, this.container);
    }

    public void layoutViewAllContainerWide(int i, int i2) {
        layoutViewOccupyingAllRow(i, i2, i2);
    }

    public void applyToLayout(ConstraintLayout constraintLayout, boolean z) {
        if (z) {
            if (Build.VERSION.SDK_INT >= 19) {
                TransitionManager.beginDelayedTransition(constraintLayout);
            }
            this.set.applyTo(constraintLayout);
        }
    }

    public void layoutViewFullScreen(int i) {
        layoutViewAllContainerWide(i, this.container);
        layoutViewWithTopBound(i, this.container);
        layoutViewWithBottomBound(i, this.container);
    }

    public void layoutViewHeightPercent(int i, float f) {
        this.set.constrainPercentHeight(i, f);
    }

    public void layoutViewWidthPercent(int i, float f) {
        this.set.constrainPercentWidth(i, f);
    }
}
