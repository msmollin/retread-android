package com.treadly.client.lib.sdk.Model;

import java.util.Locale;

/* loaded from: classes2.dex */
public class FirmwareVersion {
    public int major;
    public int minor;
    public int patch;
    public String version;

    public FirmwareVersion(int i, int i2, int i3) {
        this.major = i;
        this.minor = i2;
        this.patch = i3;
        this.version = String.format(Locale.getDefault(), "%d.%d.%d", Integer.valueOf(i), Integer.valueOf(i2), Integer.valueOf(i3));
    }
}
