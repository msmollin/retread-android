package com.treadly.client.lib.sdk.Model;

/* loaded from: classes2.dex */
public enum WifiStatus {
    noError(0),
    wifiError(6),
    unknown(100);
    
    private int value;

    WifiStatus(int i) {
        this.value = i;
    }

    public int value() {
        return this.value;
    }

    public static WifiStatus fromValue(int i) {
        WifiStatus[] values;
        for (WifiStatus wifiStatus : values()) {
            if (wifiStatus.value == i) {
                return wifiStatus;
            }
        }
        return null;
    }
}
