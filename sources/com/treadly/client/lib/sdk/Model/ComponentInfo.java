package com.treadly.client.lib.sdk.Model;

/* loaded from: classes2.dex */
public class ComponentInfo {
    private boolean connected;
    private String id;
    private long serialNumber;
    private ComponentType type;
    private ComponentVersionInfo versionInfo;

    public ComponentInfo(ComponentType componentType, String str, long j, boolean z, ComponentVersionInfo componentVersionInfo) {
        this.type = componentType;
        this.id = str;
        this.serialNumber = j;
        this.connected = z;
        this.versionInfo = componentVersionInfo;
    }

    public ComponentType getType() {
        return this.type;
    }

    public String getId() {
        return this.id;
    }

    public long getSerialNumber() {
        return this.serialNumber;
    }

    public boolean isConnected() {
        return this.connected;
    }

    public ComponentVersionInfo getVersionInfo() {
        return this.versionInfo;
    }
}
