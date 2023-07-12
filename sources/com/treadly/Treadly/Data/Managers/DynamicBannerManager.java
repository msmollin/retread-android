package com.treadly.Treadly.Data.Managers;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.treadly.Treadly.Data.Managers.DynamicBannerManager;
import com.treadly.Treadly.Data.Model.DynamicBanner.DynamicBannerAppPage;
import com.treadly.Treadly.Data.Model.DynamicBanner.DynamicBannerInfo;
import com.treadly.Treadly.Data.Model.DynamicBanner.DynamicBannerType;
import com.treadly.Treadly.Data.Model.DynamicBanner.DynamicBannerTypes.DefaultHtmlBannerInfo;
import com.treadly.Treadly.Data.Model.DynamicBanner.DynamicBannerTypes.DefaultHtmlOverlayInfo;
import com.treadly.Treadly.Data.Model.DynamicBanner.DynamicBannerTypes.DefaultMessageBannerInfo;
import com.treadly.Treadly.Data.Model.TreadlyNetworkResponse;
import com.treadly.Treadly.Data.Model.UserTokenInfo;
import com.treadly.Treadly.Data.Utility.RequestUtil;
import com.treadly.Treadly.UI.TreadlyBanners.Base.DynamicBannerView;
import com.treadly.Treadly.UI.TreadlyBanners.DefaultHtmlBanner.TreadlyDefaultHtmlBanner;
import com.treadly.Treadly.UI.TreadlyBanners.DefaultHtmlOverlay.TreadlyDefaultHtmlOverlay;
import com.treadly.Treadly.UI.TreadlyBanners.DefaultMessageOverlay.TreadlyDefaultMessageOverlay;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.TimeZone;
import org.joda.time.DateTimeConstants;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/* loaded from: classes2.dex */
public class DynamicBannerManager {
    private static final String GET_DYNAMIC_ANDROID_BANNER_INFO_ALL = "https://env-staging.treadly.co:8080/vendor/banners/android/v1/get/all";
    public static final String TAG = "dynamic_banner_manager";
    private static final String errorCouldNotConnectWithServer = "Could not connect with server";
    private static final String errorCouldNotSendRequest = "Could not send request";
    private static final String errorUserNotLoggedIn = "Error: No user currently logged in";
    public static final DynamicBannerManager instance = new DynamicBannerManager();
    private static final String keyAppPageId = "app_page_id";
    private static final String keyBannerData = "banner_data";
    private static final String keyBannerPriority = "banner_priority";
    private static final String keyBannerType = "banner_type";
    private static final String keyBannerVersion = "banner_version";
    private static final String keyData = "data";
    private static final String keyDisplayBannerTimer = "display_time";
    private static final String keyHeaderAuthorization = "Authorization";
    private static final String keyTimezoneOffset = "timezone_offset";
    private static final int supportedBannerVersion = 1;
    TreadlyDefaultHtmlBanner htmlBanner;
    TreadlyDefaultHtmlOverlay htmlOverlayBanner;
    TreadlyDefaultMessageOverlay messageOverlayBanner;

    /* loaded from: classes2.dex */
    public interface DynamicBannerResponse {
        void onResponse(@Nullable String str, @Nullable List<DynamicBannerInfo> list);
    }

    public static DynamicBannerManager getInstance() {
        return instance;
    }

    public static void getDynamicBanner(DynamicBannerAppPage dynamicBannerAppPage, final DynamicBannerResponse dynamicBannerResponse) {
        UserTokenInfo userTokenInfo = TreadlyServiceManager.getInstance().tokenInfo;
        if (userTokenInfo == null) {
            dynamicBannerResponse.onResponse("Error: No user currently logged in", null);
            return;
        }
        try {
            JSONObject jSONObject = new JSONObject();
            jSONObject.put(keyHeaderAuthorization, userTokenInfo.token);
            JSONObject dynamicBannerPayload = getDynamicBannerPayload();
            dynamicBannerPayload.put(keyAppPageId, dynamicBannerAppPage.getValue());
            dynamicBannerPayload.put(keyBannerVersion, 1);
            dynamicBannerPayload.put(keyTimezoneOffset, getLocaleTimezone());
            RequestUtil.shared.postJson(GET_DYNAMIC_ANDROID_BANNER_INFO_ALL, dynamicBannerPayload, jSONObject, new RequestUtil.RequestUtilListener() { // from class: com.treadly.Treadly.Data.Managers.-$$Lambda$DynamicBannerManager$DssypP45--hgi9U0Sr_tNF2M4gA
                @Override // com.treadly.Treadly.Data.Utility.RequestUtil.RequestUtilListener
                public final void onResponse(TreadlyNetworkResponse treadlyNetworkResponse) {
                    DynamicBannerManager.lambda$getDynamicBanner$3(DynamicBannerManager.DynamicBannerResponse.this, treadlyNetworkResponse);
                }
            });
        } catch (JSONException e) {
            e.printStackTrace();
            dynamicBannerResponse.onResponse(errorCouldNotSendRequest, null);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static /* synthetic */ void lambda$getDynamicBanner$3(final DynamicBannerResponse dynamicBannerResponse, TreadlyNetworkResponse treadlyNetworkResponse) throws JSONException {
        if (!treadlyNetworkResponse.ok() || treadlyNetworkResponse.response == null) {
            runOnMain(new Runnable() { // from class: com.treadly.Treadly.Data.Managers.-$$Lambda$DynamicBannerManager$M7MEUv0z-xHWpzegr1EPAPKjNSU
                @Override // java.lang.Runnable
                public final void run() {
                    DynamicBannerManager.DynamicBannerResponse.this.onResponse(DynamicBannerManager.errorCouldNotConnectWithServer, null);
                }
            });
            return;
        }
        final List<DynamicBannerInfo> parseGetDynamicBannerInfo = parseGetDynamicBannerInfo(treadlyNetworkResponse.response);
        if (parseGetDynamicBannerInfo != null) {
            runOnMain(new Runnable() { // from class: com.treadly.Treadly.Data.Managers.-$$Lambda$DynamicBannerManager$g4g5aIPVtQtGQp62fXcsgu-Fey8
                @Override // java.lang.Runnable
                public final void run() {
                    DynamicBannerManager.DynamicBannerResponse.this.onResponse(null, parseGetDynamicBannerInfo);
                }
            });
        } else {
            runOnMain(new Runnable() { // from class: com.treadly.Treadly.Data.Managers.-$$Lambda$DynamicBannerManager$7ZR5J-eAdOQK9HF7GfUC4FAywVc
                @Override // java.lang.Runnable
                public final void run() {
                    DynamicBannerManager.DynamicBannerResponse.this.onResponse("Error: Could not parse result", null);
                }
            });
        }
    }

    @Nullable
    static List<DynamicBannerInfo> parseGetDynamicBannerInfo(JSONObject jSONObject) {
        if (jSONObject.has("data")) {
            try {
                JSONArray jSONArray = jSONObject.getJSONArray("data");
                ArrayList arrayList = new ArrayList();
                for (int i = 0; i < jSONArray.length(); i++) {
                    if (jSONArray.getJSONObject(i) != null && jSONArray.getJSONObject(i).has(keyBannerType)) {
                        JSONObject jSONObject2 = jSONArray.getJSONObject(i);
                        try {
                            DynamicBannerType fromValue = DynamicBannerType.fromValue(jSONObject2.getInt(keyBannerType));
                            if (fromValue != null) {
                                switch (fromValue) {
                                    case defaultHtmlBanner:
                                    case defaultHtmlOverlay:
                                        break;
                                    case defaultMessageOverlay:
                                    case defaultMessageReleaseOverlay:
                                        DefaultMessageBannerInfo parseDefaultMessageBannerInfo = parseDefaultMessageBannerInfo(jSONObject2);
                                        if (parseDefaultMessageBannerInfo != null) {
                                            arrayList.add(parseDefaultMessageBannerInfo);
                                            break;
                                        } else {
                                            continue;
                                        }
                                    default:
                                        Log.d(TAG, "parseGetDynamicBannerInfo: Type not supported=" + jSONObject2.getInt(keyBannerType));
                                        continue;
                                }
                            }
                        } catch (JSONException unused) {
                        }
                    }
                }
                return arrayList;
            } catch (JSONException e) {
                e.printStackTrace();
                return null;
            }
        }
        return null;
    }

    @Nullable
    static DefaultHtmlBannerInfo parseDefaultHtmlBannerInfo(JSONObject jSONObject) {
        if (hasBannerInfo(jSONObject)) {
            return DefaultHtmlBannerInfo.fromBannerInfo(parseDynamicBannerInfo(jSONObject));
        }
        return null;
    }

    @Nullable
    static DefaultHtmlOverlayInfo parseDefaultHtmlOverlayInfo(JSONObject jSONObject) {
        if (hasBannerInfo(jSONObject)) {
            return DefaultHtmlOverlayInfo.fromBannerInfo(parseDynamicBannerInfo(jSONObject));
        }
        return null;
    }

    @Nullable
    static DefaultMessageBannerInfo parseDefaultMessageBannerInfo(JSONObject jSONObject) {
        if (hasBannerInfo(jSONObject)) {
            return DefaultMessageBannerInfo.fromBannerInfo(parseDynamicBannerInfo(jSONObject));
        }
        return null;
    }

    @Nullable
    static DynamicBannerInfo parseDynamicBannerInfo(JSONObject jSONObject) {
        try {
            return new DynamicBannerInfo(DynamicBannerAppPage.fromValue(jSONObject.getInt(keyAppPageId)), jSONObject.getInt(keyDisplayBannerTimer), DynamicBannerType.fromValue(jSONObject.getInt(keyBannerType)), jSONObject.getInt(keyBannerVersion), jSONObject.getInt(keyBannerPriority), jSONObject.getString(keyBannerData));
        } catch (JSONException unused) {
            return null;
        }
    }

    public void displayBannerInfo(Context context, DynamicBannerInfo dynamicBannerInfo) {
        switch (dynamicBannerInfo.bannerType) {
            case defaultHtmlBanner:
            case defaultHtmlOverlay:
            default:
                return;
            case defaultMessageOverlay:
            case defaultMessageReleaseOverlay:
                displayDefaultMessageOverlay(context, dynamicBannerInfo);
                return;
        }
    }

    void displayDefaultMessageOverlay(Context context, DynamicBannerInfo dynamicBannerInfo) {
        dismissBanners();
        this.messageOverlayBanner = new TreadlyDefaultMessageOverlay(context, dynamicBannerInfo);
        this.messageOverlayBanner.dynamicBannerDismissListener = new DynamicBannerView.DynamicBannerViewDismissListener() { // from class: com.treadly.Treadly.Data.Managers.-$$Lambda$DynamicBannerManager$EMwY3oxKdKhucBpMMucVLHUAKyM
            @Override // com.treadly.Treadly.UI.TreadlyBanners.Base.DynamicBannerView.DynamicBannerViewDismissListener
            public final void onDismiss() {
                DynamicBannerManager.this.messageOverlayBanner = null;
            }
        };
        this.messageOverlayBanner.showPopup();
    }

    void displayDefaultHtmlOverlay(Context context, DynamicBannerInfo dynamicBannerInfo) {
        dismissBanners();
        this.htmlOverlayBanner = new TreadlyDefaultHtmlOverlay(context, dynamicBannerInfo);
        this.htmlOverlayBanner.htmlOverlayListener = new TreadlyDefaultHtmlOverlay.DefaultHtmlOverlayListener() { // from class: com.treadly.Treadly.Data.Managers.DynamicBannerManager.1
            @Override // com.treadly.Treadly.UI.TreadlyBanners.DefaultHtmlOverlay.TreadlyDefaultHtmlOverlay.DefaultHtmlOverlayListener
            public void didPressHide() {
                System.out.println("NRS :: didPressHide");
                DynamicBannerManager.this.htmlOverlayBanner.dismissView();
                DynamicBannerManager.this.htmlOverlayBanner = null;
            }

            @Override // com.treadly.Treadly.UI.TreadlyBanners.DefaultHtmlOverlay.TreadlyDefaultHtmlOverlay.DefaultHtmlOverlayListener
            public void didPressClose() {
                System.out.println("NRS :: didPressClose");
                DynamicBannerManager.this.htmlOverlayBanner.dismissView();
                DynamicBannerManager.this.htmlOverlayBanner = null;
            }

            @Override // com.treadly.Treadly.UI.TreadlyBanners.DefaultHtmlOverlay.TreadlyDefaultHtmlOverlay.DefaultHtmlOverlayListener
            public void didPressUrl(URI uri) {
                DynamicBannerManager.this.dismissBanners();
            }
        };
        this.htmlOverlayBanner.dynamicBannerDismissListener = new DynamicBannerView.DynamicBannerViewDismissListener() { // from class: com.treadly.Treadly.Data.Managers.-$$Lambda$DynamicBannerManager$Y1OEvRNon2sgbuXN6uHlyqQu98c
            @Override // com.treadly.Treadly.UI.TreadlyBanners.Base.DynamicBannerView.DynamicBannerViewDismissListener
            public final void onDismiss() {
                DynamicBannerManager.this.htmlOverlayBanner = null;
            }
        };
        this.htmlOverlayBanner.showPopup();
    }

    void displayDefaultHtmlBanner(Context context, DynamicBannerInfo dynamicBannerInfo) {
        dismissBanners();
        this.htmlBanner = new TreadlyDefaultHtmlBanner(context, dynamicBannerInfo);
        this.htmlBanner.dynamicBannerDismissListener = new DynamicBannerView.DynamicBannerViewDismissListener() { // from class: com.treadly.Treadly.Data.Managers.-$$Lambda$DynamicBannerManager$M9lvf1H1lf-h-0KvbR_lv_Olgkw
            @Override // com.treadly.Treadly.UI.TreadlyBanners.Base.DynamicBannerView.DynamicBannerViewDismissListener
            public final void onDismiss() {
                DynamicBannerManager.this.htmlBanner = null;
            }
        };
        this.htmlBanner.showPopup();
    }

    public void dismissBanners() {
        System.out.println("NRS :: dismissBanners");
        if (this.messageOverlayBanner != null) {
            this.messageOverlayBanner.dismissView();
        }
        this.messageOverlayBanner = null;
        if (this.htmlOverlayBanner != null) {
            this.htmlOverlayBanner.dismissView();
        }
        this.htmlOverlayBanner = null;
        if (this.htmlBanner != null) {
            this.htmlBanner.dismissView();
        }
        this.htmlBanner = null;
    }

    @NonNull
    static JSONObject getDynamicBannerPayload() {
        return AppActivityManager.shared.getAppActivityJsonPayload();
    }

    static void runOnMain(Runnable runnable) {
        new Handler(Looper.getMainLooper()).post(runnable);
    }

    static int getLocaleTimezone() {
        return TimeZone.getDefault().getRawOffset() / DateTimeConstants.MILLIS_PER_HOUR;
    }

    static boolean hasBannerInfo(JSONObject jSONObject) {
        return jSONObject.has(keyBannerType) && jSONObject.has(keyAppPageId) && jSONObject.has(keyDisplayBannerTimer) && jSONObject.has(keyBannerVersion) && jSONObject.has(keyBannerPriority) && jSONObject.has(keyBannerData);
    }
}
