package com.treadly.Treadly.Data.Model.DynamicBanner;

/* loaded from: classes2.dex */
public enum DynamicBannerAppPage {
    none(0),
    connectPage(1),
    groupRootPage(2),
    homeRootPage(3);
    
    private final int value;

    DynamicBannerAppPage(int i) {
        this.value = i;
    }

    public int getValue() {
        return this.value;
    }

    public static DynamicBannerAppPage fromValue(int i) {
        DynamicBannerAppPage[] values;
        for (DynamicBannerAppPage dynamicBannerAppPage : values()) {
            if (dynamicBannerAppPage.value == i) {
                return dynamicBannerAppPage;
            }
        }
        return null;
    }
}
