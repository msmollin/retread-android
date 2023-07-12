package com.treadly.client.lib.sdk.Model;

/* loaded from: classes2.dex */
public class WifiUrlTestInfo {
    public static final int passwordMaxSize = 64;
    public static final int ssidMaxSize = 32;
    public static final int urlMaxSize = 128;
    public String password;
    public String ssid;
    public String url;

    public WifiUrlTestInfo(String str, String str2, String str3) {
        this.ssid = str;
        this.password = str2;
        this.url = str3;
    }

    public String getSsid() {
        if (this.ssid == null) {
            return null;
        }
        return this.ssid.length() > 32 ? this.ssid.substring(0, 32) : this.ssid;
    }

    public String getPassword() {
        if (this.password == null) {
            return null;
        }
        return this.password.length() > 64 ? this.password.substring(0, 64) : this.password;
    }

    public String getUrl() {
        if (this.url == null) {
            return null;
        }
        return this.url.length() > 128 ? this.url.substring(0, 128) : this.url;
    }
}
