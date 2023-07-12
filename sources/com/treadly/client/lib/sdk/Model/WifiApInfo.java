package com.treadly.client.lib.sdk.Model;

/* loaded from: classes2.dex */
public class WifiApInfo {
    public boolean connected;
    public String password;
    public boolean passwordError;
    public boolean persistent;
    public int rssi;
    public String ssid;

    public WifiApInfo(String str, int i, boolean z, boolean z2, boolean z3) {
        this.ssid = str;
        this.password = "";
        this.rssi = i;
        this.persistent = z;
        this.connected = z2;
        this.passwordError = z3;
    }

    public WifiApInfo(String str, String str2, int i, boolean z) {
        this.ssid = str;
        this.password = str2;
        this.rssi = i;
        this.persistent = z;
    }
}
