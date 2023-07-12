package com.treadly.Treadly.UI.TreadlyBanners.DefaultMessageOverlay;

import android.content.Context;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.PopupWindow;
import android.widget.TextView;
import androidx.core.content.ContextCompat;
import com.treadly.Treadly.Data.Model.DynamicBanner.DynamicBannerInfo;
import com.treadly.Treadly.Data.Model.DynamicBanner.DynamicBannerTypes.DefaultMessageBannerInfo;
import com.treadly.Treadly.R;
import com.treadly.Treadly.UI.TreadlyBanners.Base.DynamicBannerView;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.BiConsumer;

/* loaded from: classes2.dex */
public class TreadlyDefaultMessageOverlay extends DynamicBannerView {
    PopupWindow blurBackgroundPopup;
    View blurView;
    DefaultMessageBannerInfo messageBannerInfo;
    View popupView;
    PopupWindow popupWindow;

    public TreadlyDefaultMessageOverlay(Context context, DynamicBannerInfo dynamicBannerInfo) {
        super(context, dynamicBannerInfo);
        this.messageBannerInfo = null;
        this.messageBannerInfo = DefaultMessageBannerInfo.fromBannerInfo(dynamicBannerInfo);
        if (this.messageBannerInfo != null) {
            initializeWindow();
        }
    }

    private void initializeWindow() {
        LayoutInflater from = LayoutInflater.from(this.context);
        this.popupView = from.inflate(R.layout.default_message_overlay, (ViewGroup) null);
        ((ImageButton) this.popupView.findViewById(R.id.message_overlay_dismiss)).setOnClickListener(new View.OnClickListener() { // from class: com.treadly.Treadly.UI.TreadlyBanners.DefaultMessageOverlay.-$$Lambda$TreadlyDefaultMessageOverlay$KPw51g5XMOLQMTxAs0e_ESsWuPg
            @Override // android.view.View.OnClickListener
            public final void onClick(View view) {
                TreadlyDefaultMessageOverlay.this.popupWindow.dismiss();
            }
        });
        TextView textView = (TextView) this.popupView.findViewById(R.id.message_overlay_title);
        if (this.messageBannerInfo != null && this.messageBannerInfo.title != null) {
            textView.setText(this.messageBannerInfo.title);
        } else {
            textView.setText(" ");
        }
        final AtomicReference atomicReference = new AtomicReference("");
        if (this.messageBannerInfo != null && this.messageBannerInfo.body != null) {
            atomicReference.set(this.messageBannerInfo.body);
        } else {
            atomicReference.set(" ");
        }
        if (this.messageBannerInfo != null) {
            this.messageBannerInfo.links.forEach(new BiConsumer() { // from class: com.treadly.Treadly.UI.TreadlyBanners.DefaultMessageOverlay.-$$Lambda$TreadlyDefaultMessageOverlay$S3HGQXKOjHKz_xdaCFxH3fyPSso
                @Override // java.util.function.BiConsumer
                public final void accept(Object obj, Object obj2) {
                    r0.set(((String) atomicReference.get()).replace(r1, "<a href=\"" + ((String) obj2) + "\">" + ((String) obj) + "</a>"));
                }
            });
        }
        TextView textView2 = (TextView) this.popupView.findViewById(R.id.message_overlay_message);
        textView2.setText(Html.fromHtml((String) atomicReference.get(), 0));
        textView2.setMovementMethod(LinkMovementMethod.getInstance());
        this.blurView = from.inflate(R.layout.blur_popup, (ViewGroup) null);
        this.blurView.setOnClickListener(new View.OnClickListener() { // from class: com.treadly.Treadly.UI.TreadlyBanners.DefaultMessageOverlay.-$$Lambda$TreadlyDefaultMessageOverlay$W3McPJZbcbQVlFUvETgasVJM218
            @Override // android.view.View.OnClickListener
            public final void onClick(View view) {
                TreadlyDefaultMessageOverlay.this.popupWindow.dismiss();
            }
        });
        this.blurBackgroundPopup = new PopupWindow(this.blurView, -1, -1);
        this.popupWindow = new PopupWindow(this.popupView, (int) this.context.getResources().getDimension(R.dimen.message_overlay_width), (int) this.context.getResources().getDimension(R.dimen.message_overlay_height));
        this.popupWindow.setTouchable(true);
        this.popupWindow.setFocusable(true);
        this.popupWindow.setBackgroundDrawable(ContextCompat.getDrawable(this.context, R.drawable.unclaimed_list_background));
        this.popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() { // from class: com.treadly.Treadly.UI.TreadlyBanners.DefaultMessageOverlay.-$$Lambda$PZQN4ayfsfX8gVbtOau_5L9_ZiE
            @Override // android.widget.PopupWindow.OnDismissListener
            public final void onDismiss() {
                TreadlyDefaultMessageOverlay.this.handleDismiss();
            }
        });
    }

    public void showPopup() {
        if (this.blurBackgroundPopup == null || this.popupWindow == null) {
            return;
        }
        this.blurBackgroundPopup.showAsDropDown(this.blurView);
        this.popupWindow.showAtLocation(this.popupView, 48, 0, (int) this.context.getResources().getDimension(R.dimen._60ssp));
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
