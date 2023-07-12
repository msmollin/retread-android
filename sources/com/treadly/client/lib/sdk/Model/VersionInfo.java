package com.treadly.client.lib.sdk.Model;

import java.util.Locale;

/* loaded from: classes2.dex */
public class VersionInfo {
    public boolean isMandatory;
    public int versionMajor;
    public int versionMinor;
    public int versionPatch;

    public VersionInfo() {
    }

    public VersionInfo(int i, int i2, int i3) {
        this.versionMajor = i;
        this.versionMinor = i2;
        this.versionPatch = i3;
    }

    public VersionInfo(String str) {
        if (str == null) {
            return;
        }
        String[] split = str.split("\\.");
        if (split.length == 3) {
            this.versionMajor = Integer.parseInt(split[0]);
            this.versionMinor = Integer.parseInt(split[1]);
            this.versionPatch = Integer.parseInt(split[2]);
        }
    }

    public String getVersion() {
        return String.format(Locale.getDefault(), "%d.%d.%d", Integer.valueOf(this.versionMajor), Integer.valueOf(this.versionMinor), Integer.valueOf(this.versionPatch));
    }

    public boolean isGreaterThan(VersionInfo versionInfo) {
        return versionInfo.versionMajor != this.versionMajor ? this.versionMajor > versionInfo.versionMajor : versionInfo.versionMinor != this.versionMinor ? this.versionMinor > versionInfo.versionMinor : versionInfo.versionPatch != this.versionPatch && this.versionPatch > versionInfo.versionPatch;
    }

    public boolean isEqual(VersionInfo versionInfo) {
        return versionInfo.versionMajor == this.versionMajor && versionInfo.versionMinor == this.versionMinor && versionInfo.versionPatch == this.versionPatch;
    }

    public boolean isGen(int i) {
        return this.versionMajor >= i;
    }

    public boolean isGen2() {
        return isGen(2);
    }

    public boolean hasSingleUserMode() {
        return this.versionMajor != 3 ? this.versionMajor >= 3 : this.versionMinor != 42 ? this.versionMinor >= 42 : this.versionPatch == 0 || this.versionPatch >= 0;
    }

    public boolean hasWifiConfiguration() {
        return this.versionMajor != 3 ? this.versionMajor >= 3 : this.versionMinor != 63 ? this.versionMinor >= 63 : this.versionPatch == 0 || this.versionPatch >= 0;
    }
}
