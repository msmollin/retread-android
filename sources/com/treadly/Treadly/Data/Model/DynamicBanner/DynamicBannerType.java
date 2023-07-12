package com.treadly.Treadly.Data.Model.DynamicBanner;

/* loaded from: classes2.dex */
public enum DynamicBannerType {
    none(0),
    defaultHtmlBanner(1),
    defaultHtmlOverlay(2),
    defaultMessageOverlay(3),
    defaultMessageReleaseOverlay(4);
    
    private final int value;

    DynamicBannerType(int i) {
        this.value = i;
    }

    public int getValue() {
        return this.value;
    }

    public static DynamicBannerType fromValue(int i) {
        DynamicBannerType[] values;
        for (DynamicBannerType dynamicBannerType : values()) {
            if (i == dynamicBannerType.value) {
                return dynamicBannerType;
            }
        }
        return null;
    }
}
