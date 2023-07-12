package com.treadly.Treadly.UI.TreadlyBanners.Base;

import android.content.Context;
import com.treadly.Treadly.Data.Model.DynamicBanner.DynamicBannerInfo;
import java.util.Timer;
import java.util.TimerTask;

/* loaded from: classes2.dex */
public class DynamicBannerView {
    DynamicBannerInfo baseBannerInfo;
    protected Context context;
    Timer dismissTimer;
    public DynamicBannerViewDismissListener dynamicBannerDismissListener;

    /* loaded from: classes2.dex */
    public interface DynamicBannerViewDismissListener {
        void onDismiss();
    }

    public DynamicBannerView(Context context, DynamicBannerInfo dynamicBannerInfo) {
        this.context = context;
        configure(dynamicBannerInfo);
    }

    public void configure(DynamicBannerInfo dynamicBannerInfo) {
        this.baseBannerInfo = dynamicBannerInfo;
    }

    public void startTimer(int i) {
        if (i > 0 && this.dismissTimer == null) {
            this.dismissTimer = new Timer();
            this.dismissTimer.schedule(new TimerTask() { // from class: com.treadly.Treadly.UI.TreadlyBanners.Base.DynamicBannerView.1
                @Override // java.util.TimerTask, java.lang.Runnable
                public void run() {
                    DynamicBannerView.this.dismissView();
                }
            }, i);
        }
    }

    public void stopTimer() {
        if (this.dismissTimer != null) {
            this.dismissTimer.cancel();
            this.dismissTimer.purge();
        }
        this.dismissTimer = null;
    }

    public void dismissView() {
        stopTimer();
        if (this.dynamicBannerDismissListener != null) {
            this.dynamicBannerDismissListener.onDismiss();
        }
    }
}
