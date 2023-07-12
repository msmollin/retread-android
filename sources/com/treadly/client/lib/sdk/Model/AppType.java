package com.treadly.client.lib.sdk.Model;

/* loaded from: classes2.dex */
public enum AppType {
    unknown(0),
    iOS(1),
    android(2);
    
    private int value;

    AppType(int i) {
        this.value = i;
    }

    public int value() {
        return this.value;
    }

    public static AppType fromValue(int i) {
        AppType[] values;
        for (AppType appType : values()) {
            if (appType.value == i) {
                return appType;
            }
        }
        return null;
    }
}
