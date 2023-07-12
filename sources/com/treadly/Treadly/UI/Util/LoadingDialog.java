package com.treadly.Treadly.UI.Util;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.view.MotionEvent;
import android.widget.ImageView;
import com.airbnb.lottie.LottieAnimationView;
import com.treadly.Treadly.R;

/* loaded from: classes2.dex */
public class LoadingDialog extends ProgressDialog {
    private Context ctx;
    private LottieAnimationView loadingView;

    @Override // android.app.Dialog
    public boolean onTouchEvent(MotionEvent motionEvent) {
        return true;
    }

    public LoadingDialog(Context context) {
        super(context);
        this.ctx = context;
    }

    public LoadingDialog(Context context, int i) {
        super(context, i);
        this.ctx = context;
    }

    @Override // android.app.ProgressDialog, android.app.AlertDialog, android.app.Dialog
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.yck_diy_dialog_loading);
        this.loadingView = (LottieAnimationView) findViewById(R.id.loading_view);
        this.loadingView.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
        this.loadingView.setSpeed(0.5f);
        setCancelable(false);
    }

    @Override // android.app.Dialog
    public void show() {
        super.show();
        this.loadingView.playAnimation();
    }
}
