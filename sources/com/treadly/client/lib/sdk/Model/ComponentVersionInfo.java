package com.treadly.client.lib.sdk.Model;

import java.util.Locale;

/* loaded from: classes2.dex */
public class ComponentVersionInfo {
    private ComponentType componentType;
    private int majorVersion;
    private int minorVersion;
    private int patchVersion;

    public ComponentVersionInfo(ComponentType componentType, int i, int i2, int i3) {
        this.componentType = componentType;
        this.majorVersion = i;
        this.minorVersion = i2;
        this.patchVersion = i3;
    }

    public ComponentType getComponentType() {
        return this.componentType;
    }

    public int getMajorVersion() {
        return this.majorVersion;
    }

    public int getMinorVersion() {
        return this.minorVersion;
    }

    public int getPatchVersion() {
        return this.patchVersion;
    }

    public VersionInfo getVersionInfo() {
        return new VersionInfo(this.majorVersion, this.minorVersion, this.patchVersion);
    }

    public String getVersion() {
        return String.format(Locale.getDefault(), "%d.%d.%d", Integer.valueOf(this.majorVersion), Integer.valueOf(this.minorVersion), Integer.valueOf(this.patchVersion));
    }

    public boolean isGreaterThan(VersionInfo versionInfo) {
        return versionInfo.versionMajor != this.majorVersion ? this.majorVersion > versionInfo.versionMajor : versionInfo.versionMinor != this.minorVersion ? this.minorVersion > versionInfo.versionMinor : versionInfo.versionPatch != this.patchVersion && this.patchVersion > versionInfo.versionPatch;
    }

    public boolean isEqual(VersionInfo versionInfo) {
        return versionInfo.versionMajor == this.majorVersion && versionInfo.versionMinor == this.minorVersion && versionInfo.versionPatch == this.patchVersion;
    }
}
