package com.treadly.Treadly.Data.Model.DynamicBanner.DynamicBannerTypes;

import androidx.annotation.Nullable;
import com.treadly.Treadly.Data.Model.DynamicBanner.DynamicBannerAppPage;
import com.treadly.Treadly.Data.Model.DynamicBanner.DynamicBannerInfo;
import com.treadly.Treadly.Data.Model.DynamicBanner.DynamicBannerType;

/* loaded from: classes2.dex */
public class DefaultHtmlBannerInfo extends DynamicBannerInfo {
    public DefaultHtmlBannerInfo(DynamicBannerAppPage dynamicBannerAppPage, int i, DynamicBannerType dynamicBannerType, int i2, int i3, String str) {
        super(dynamicBannerAppPage, i, dynamicBannerType, i2, i3, str);
    }

    public String getHtmlString() {
        return this.bannerData != null ? this.bannerData : "";
    }

    public static DefaultHtmlBannerInfo fromBannerInfo(@Nullable DynamicBannerInfo dynamicBannerInfo) {
        if (dynamicBannerInfo == null) {
            return null;
        }
        return new DefaultHtmlBannerInfo(dynamicBannerInfo.appPage, dynamicBannerInfo.displayTime, dynamicBannerInfo.bannerType, dynamicBannerInfo.bannerVersion, dynamicBannerInfo.bannerPriority, dynamicBannerInfo.bannerData);
    }
}
