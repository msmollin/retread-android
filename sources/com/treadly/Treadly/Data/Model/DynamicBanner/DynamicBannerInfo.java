package com.treadly.Treadly.Data.Model.DynamicBanner;

/* loaded from: classes2.dex */
public class DynamicBannerInfo {
    public DynamicBannerAppPage appPage;
    public String bannerData;
    public int bannerPriority;
    public DynamicBannerType bannerType;
    public int bannerVersion;
    public int displayTime;

    public DynamicBannerInfo(DynamicBannerAppPage dynamicBannerAppPage, int i, DynamicBannerType dynamicBannerType, int i2, int i3, String str) {
        this.appPage = dynamicBannerAppPage;
        this.displayTime = i;
        this.bannerType = dynamicBannerType;
        this.bannerVersion = i2;
        this.bannerPriority = i3;
        this.bannerData = str;
    }
}
