package com.treadly.Treadly.UI.TreadlyConnect.DailyGoalProgressView;

import android.animation.LayoutTransition;
import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import androidx.constraintlayout.widget.ConstraintLayout;
import com.github.mikephil.charting.utils.Utils;
import com.treadly.Treadly.R;
import java.io.PrintStream;

/* loaded from: classes2.dex */
public class DailyGoalProgressView extends FrameLayout {
    private ImageView candyCaneBackground;
    private ImageView candyCaneDarkBackground;
    private FrameLayout invertedProgressView;
    private View view;

    public DailyGoalProgressView(Context context) {
        super(context);
        initView();
    }

    public DailyGoalProgressView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        initView();
    }

    private void initView() {
        this.view = inflate(getContext(), R.layout.daily_goal_progress_view, this);
        this.invertedProgressView = (FrameLayout) this.view.findViewById(R.id.invert_progress_view);
        this.invertedProgressView.setClipToOutline(true);
        LayoutTransition layoutTransition = this.invertedProgressView.getLayoutTransition();
        layoutTransition.setDuration(500L);
        layoutTransition.enableTransitionType(4);
        this.candyCaneBackground = (ImageView) this.view.findViewById(R.id.candy_cane_background);
        this.candyCaneBackground.setClipToOutline(true);
        this.candyCaneDarkBackground = (ImageView) this.view.findViewById(R.id.candy_cane_dark_background);
        this.candyCaneDarkBackground.setClipToOutline(true);
    }

    public void setProgress(double d) {
        if (this.invertedProgressView == null) {
            return;
        }
        double max = Math.max(Math.min(1.0d, d), (double) Utils.DOUBLE_EPSILON);
        ConstraintLayout.LayoutParams layoutParams = (ConstraintLayout.LayoutParams) this.invertedProgressView.getLayoutParams();
        layoutParams.leftMargin = (int) (getWidth() * max);
        PrintStream printStream = System.out;
        printStream.println("LGK :: DAILY GOAL WIDTH: " + getWidth() + " PARAM: " + (getWidth() * max));
        this.invertedProgressView.setLayoutParams(layoutParams);
    }

    @Override // android.widget.FrameLayout, android.view.ViewGroup, android.view.View
    protected void onLayout(boolean z, int i, int i2, int i3, int i4) {
        super.onLayout(z, i, i2, i3, i4);
        if (this.candyCaneDarkBackground != null) {
            FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams) this.candyCaneDarkBackground.getLayoutParams();
            if (layoutParams.width != getWidth()) {
                layoutParams.width = getWidth();
                this.candyCaneDarkBackground.setLayoutParams(layoutParams);
            }
        }
    }
}
