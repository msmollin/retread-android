package com.treadly.Treadly.UI.TreadlyConnect.SpeedOption;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import com.treadly.Treadly.R;

/* loaded from: classes2.dex */
public class TreadlySpeedOptionView extends ConstraintLayout {
    public TreadlySpeedOptionViewListener listener;
    private Button speed1;
    private Button speed2;
    private Button speed3;
    private Button speed4;
    private Button speed5;

    /* loaded from: classes2.dex */
    public interface TreadlySpeedOptionViewListener {
        void onSpeedSelected(int i);
    }

    public TreadlySpeedOptionView(@NonNull Context context) {
        super(context);
        configure();
    }

    public TreadlySpeedOptionView(@NonNull Context context, @Nullable AttributeSet attributeSet) {
        super(context, attributeSet);
        configure();
    }

    public TreadlySpeedOptionView(@NonNull Context context, @Nullable AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        configure();
    }

    private void configure() {
        View inflate = inflate(getContext(), R.layout.layout_treadly_speed_option, this);
        this.speed1 = configureButton(inflate, R.id.speed_1_button, 1);
        this.speed2 = configureButton(inflate, R.id.speed_2_button, 2);
        this.speed3 = configureButton(inflate, R.id.speed_3_button, 3);
        this.speed4 = configureButton(inflate, R.id.speed_4_button, 4);
        this.speed5 = configureButton(inflate, R.id.speed_5_button, 5);
    }

    @SuppressLint({"ClickableViewAccessibility"})
    private Button configureButton(View view, int i, int i2) {
        Button button = (Button) view.findViewById(i);
        button.setTag(Integer.valueOf(i2));
        button.setOnTouchListener(new View.OnTouchListener() { // from class: com.treadly.Treadly.UI.TreadlyConnect.SpeedOption.TreadlySpeedOptionView.1
            @Override // android.view.View.OnTouchListener
            public boolean onTouch(View view2, MotionEvent motionEvent) {
                switch (motionEvent.getAction()) {
                    case 0:
                        TreadlySpeedOptionView.this.onSpeedTouchDown((Button) view2);
                        return false;
                    case 1:
                        TreadlySpeedOptionView.this.onSpeedTouchUp((Button) view2);
                        return false;
                    default:
                        return false;
                }
            }
        });
        return button;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void onSpeedTouchDown(Button button) {
        button.setBackgroundResource(R.drawable.speed_option_press_down);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void onSpeedTouchUp(Button button) {
        if (this.listener != null) {
            this.listener.onSpeedSelected(((Integer) button.getTag()).intValue());
        }
        button.setBackgroundResource(R.drawable.transparent_drawable);
    }

    private void onSpeedTouchCancel(Button button) {
        button.setBackgroundResource(R.drawable.transparent_drawable);
    }
}
