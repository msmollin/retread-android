package com.treadly.Treadly.UI.TreadlyBanners.DefaultHtmlOverlay;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.PopupWindow;
import androidx.core.content.ContextCompat;
import com.treadly.Treadly.Data.Model.DynamicBanner.DynamicBannerInfo;
import com.treadly.Treadly.Data.Model.DynamicBanner.DynamicBannerTypes.DefaultHtmlOverlayInfo;
import com.treadly.Treadly.R;
import com.treadly.Treadly.UI.TreadlyBanners.Base.DynamicBannerView;
import java.net.URI;

/* loaded from: classes2.dex */
public class TreadlyDefaultHtmlOverlay extends DynamicBannerView {
    PopupWindow blurBackgroundPopup;
    View blurView;
    DefaultHtmlOverlayInfo defaultHtmlBannerInfo;
    public DefaultHtmlOverlayListener htmlOverlayListener;
    View popupView;
    PopupWindow popupWindow;

    /* loaded from: classes2.dex */
    public interface DefaultHtmlOverlayListener {
        void didPressClose();

        void didPressHide();

        void didPressUrl(URI uri);
    }

    public TreadlyDefaultHtmlOverlay(Context context, DynamicBannerInfo dynamicBannerInfo) {
        super(context, dynamicBannerInfo);
        this.defaultHtmlBannerInfo = DefaultHtmlOverlayInfo.fromBannerInfo(dynamicBannerInfo);
        initializeWindow();
    }

    void initializeWindow() {
        LayoutInflater from = LayoutInflater.from(this.context);
        this.popupView = from.inflate(R.layout.default_html_overlay, (ViewGroup) null);
        ((ImageButton) this.popupView.findViewById(R.id.html_overlay_dismiss_button)).setOnClickListener(new View.OnClickListener() { // from class: com.treadly.Treadly.UI.TreadlyBanners.DefaultHtmlOverlay.-$$Lambda$TreadlyDefaultHtmlOverlay$55QWCG-sl7TAkmt6odAlDE1XjJk
            @Override // android.view.View.OnClickListener
            public final void onClick(View view) {
                TreadlyDefaultHtmlOverlay.this.dismissView();
            }
        });
        ((Button) this.popupView.findViewById(R.id.html_overlay_close_button)).setOnClickListener(new View.OnClickListener() { // from class: com.treadly.Treadly.UI.TreadlyBanners.DefaultHtmlOverlay.-$$Lambda$TreadlyDefaultHtmlOverlay$SIOXDlQxY8GjGEunGvQcz7gCOgo
            @Override // android.view.View.OnClickListener
            public final void onClick(View view) {
                TreadlyDefaultHtmlOverlay.lambda$initializeWindow$1(TreadlyDefaultHtmlOverlay.this, view);
            }
        });
        ((Button) this.popupView.findViewById(R.id.html_overlay_hide_button)).setOnClickListener(new View.OnClickListener() { // from class: com.treadly.Treadly.UI.TreadlyBanners.DefaultHtmlOverlay.-$$Lambda$TreadlyDefaultHtmlOverlay$raRko3wciZQtggJcehI_nR4R_N8
            @Override // android.view.View.OnClickListener
            public final void onClick(View view) {
                TreadlyDefaultHtmlOverlay.lambda$initializeWindow$2(TreadlyDefaultHtmlOverlay.this, view);
            }
        });
        ((WebView) this.popupView.findViewById(R.id.html_overlay_web_view)).loadData(this.defaultHtmlBannerInfo.getHtmlString(), null, null);
        this.blurView = from.inflate(R.layout.blur_popup, (ViewGroup) null);
        this.blurView.setOnClickListener(new View.OnClickListener() { // from class: com.treadly.Treadly.UI.TreadlyBanners.DefaultHtmlOverlay.-$$Lambda$TreadlyDefaultHtmlOverlay$ydLRXMbR5N9zl-s8qTj8eG2zCkk
            @Override // android.view.View.OnClickListener
            public final void onClick(View view) {
                TreadlyDefaultHtmlOverlay.this.popupWindow.dismiss();
            }
        });
        this.blurBackgroundPopup = new PopupWindow(this.blurView, -1, -1);
        this.popupWindow = new PopupWindow(this.popupView, (int) this.context.getResources().getDimension(R.dimen.html_overlay_width), (int) this.context.getResources().getDimension(R.dimen.html_overlay_height));
        this.popupWindow.setTouchable(true);
        this.popupWindow.setFocusable(true);
        this.popupWindow.setBackgroundDrawable(ContextCompat.getDrawable(this.context, R.drawable.unclaimed_list_background));
        this.popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() { // from class: com.treadly.Treadly.UI.TreadlyBanners.DefaultHtmlOverlay.-$$Lambda$_qWQHXIMCMiMpLNKGKhSbK6JjbY
            @Override // android.widget.PopupWindow.OnDismissListener
            public final void onDismiss() {
                TreadlyDefaultHtmlOverlay.this.handleDismiss();
            }
        });
    }

    public static /* synthetic */ void lambda$initializeWindow$1(TreadlyDefaultHtmlOverlay treadlyDefaultHtmlOverlay, View view) {
        if (treadlyDefaultHtmlOverlay.htmlOverlayListener != null) {
            treadlyDefaultHtmlOverlay.htmlOverlayListener.didPressClose();
        }
    }

    public static /* synthetic */ void lambda$initializeWindow$2(TreadlyDefaultHtmlOverlay treadlyDefaultHtmlOverlay, View view) {
        if (treadlyDefaultHtmlOverlay.htmlOverlayListener != null) {
            treadlyDefaultHtmlOverlay.htmlOverlayListener.didPressHide();
        }
    }

    public void showPopup() {
        if (this.blurBackgroundPopup == null || this.popupWindow == null) {
            return;
        }
        this.blurBackgroundPopup.showAsDropDown(this.blurView);
        this.popupWindow.showAtLocation(this.popupView, 48, 0, R.dimen._60ssp);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void handleDismiss() {
        if (this.blurBackgroundPopup != null) {
            this.blurBackgroundPopup.dismiss();
        }
        if (this.dynamicBannerDismissListener != null) {
            this.dynamicBannerDismissListener.onDismiss();
        }
    }
}
