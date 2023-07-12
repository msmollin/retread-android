package com.treadly.client.lib.sdk.Model;

/* loaded from: classes2.dex */
public class OtaUpdateInfo {
    public static final VersionInfo minVersion = new VersionInfo(2, 0, 0);
    public static final int passwordMaxSize = 63;
    public static final int ssidMaxSize = 31;
    public static final int urlMaxSize = 126;
    public String clientSecret;
    public String password;
    public boolean persistent;
    public String ssid;
    public String url;

    public OtaUpdateInfo(String str, String str2, String str3, String str4, boolean z) {
        this.ssid = str;
        this.password = str2;
        this.url = str3;
        this.clientSecret = str4;
        this.persistent = z;
    }

    public String getSsid() {
        if (this.ssid == null) {
            return null;
        }
        return this.ssid.length() > 31 ? this.ssid.substring(0, 31) : this.ssid;
    }

    public String getPassword() {
        if (this.password == null) {
            return null;
        }
        return this.password.length() > 63 ? this.password.substring(0, 63) : this.password;
    }

    public String getUrl() {
        if (this.url == null) {
            return null;
        }
        return this.url.length() > 126 ? this.url.substring(0, urlMaxSize) : this.url;
    }

    public boolean isPersistent() {
        return this.persistent;
    }
}
