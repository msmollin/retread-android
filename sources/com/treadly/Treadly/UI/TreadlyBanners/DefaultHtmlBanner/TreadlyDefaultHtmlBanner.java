package com.treadly.Treadly.UI.TreadlyBanners.DefaultHtmlBanner;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.PopupWindow;
import androidx.core.content.ContextCompat;
import com.treadly.Treadly.Data.Model.DynamicBanner.DynamicBannerInfo;
import com.treadly.Treadly.Data.Model.DynamicBanner.DynamicBannerTypes.DefaultHtmlBannerInfo;
import com.treadly.Treadly.R;
import com.treadly.Treadly.UI.TreadlyBanners.Base.DynamicBannerView;

/* loaded from: classes2.dex */
public class TreadlyDefaultHtmlBanner extends DynamicBannerView {
    PopupWindow blurBackgroundPopup;
    View blurView;
    DefaultHtmlBannerInfo htmlBannerInfo;
    View popupView;
    PopupWindow popupWindow;

    public TreadlyDefaultHtmlBanner(Context context, DynamicBannerInfo dynamicBannerInfo) {
        super(context, dynamicBannerInfo);
        this.htmlBannerInfo = DefaultHtmlBannerInfo.fromBannerInfo(dynamicBannerInfo);
        initializeWindow();
    }

    void initializeWindow() {
        LayoutInflater from = LayoutInflater.from(this.context);
        this.popupView = from.inflate(R.layout.default_html_banner, (ViewGroup) null);
        ((ImageButton) this.popupView.findViewById(R.id.html_banner_dismiss_button)).setOnClickListener(new View.OnClickListener() { // from class: com.treadly.Treadly.UI.TreadlyBanners.DefaultHtmlBanner.-$$Lambda$TreadlyDefaultHtmlBanner$nGZ2jnGXa4LM03uKlKA26oPz-5o
            @Override // android.view.View.OnClickListener
            public final void onClick(View view) {
                TreadlyDefaultHtmlBanner.this.handleDismiss();
            }
        });
        this.blurView = from.inflate(R.layout.blur_popup, (ViewGroup) null);
        this.blurView.setOnClickListener(new View.OnClickListener() { // from class: com.treadly.Treadly.UI.TreadlyBanners.DefaultHtmlBanner.-$$Lambda$TreadlyDefaultHtmlBanner$g1TrYt7Joct_u6Lb68TTU8cq7bk
            @Override // android.view.View.OnClickListener
            public final void onClick(View view) {
                TreadlyDefaultHtmlBanner.this.popupWindow.dismiss();
            }
        });
        this.blurBackgroundPopup = new PopupWindow(this.blurView, -1, -1);
        this.popupWindow = new PopupWindow(this.popupView, (int) this.context.getResources().getDimension(R.dimen.html_banner_width), (int) this.context.getResources().getDimension(R.dimen.html_banner_height));
        this.popupWindow.setTouchable(true);
        this.popupWindow.setFocusable(true);
        this.popupWindow.setBackgroundDrawable(ContextCompat.getDrawable(this.context, R.drawable.unclaimed_list_background));
        this.popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() { // from class: com.treadly.Treadly.UI.TreadlyBanners.DefaultHtmlBanner.-$$Lambda$HcAtj6thtSooTph1vg11Ikiy1vw
            @Override // android.widget.PopupWindow.OnDismissListener
            public final void onDismiss() {
                TreadlyDefaultHtmlBanner.this.handleDismiss();
            }
        });
    }

    public void showPopup() {
        if (this.blurBackgroundPopup == null || this.popupWindow == null) {
            return;
        }
        this.blurBackgroundPopup.showAsDropDown(this.blurView);
        this.popupWindow.showAtLocation(this.popupView, 48, 0, R.dimen._60ssp);
    }

    public void handleDismiss() {
        if (this.blurBackgroundPopup != null) {
            this.blurBackgroundPopup.dismiss();
        }
        if (this.dynamicBannerDismissListener != null) {
            this.dynamicBannerDismissListener.onDismiss();
        }
    }
}
