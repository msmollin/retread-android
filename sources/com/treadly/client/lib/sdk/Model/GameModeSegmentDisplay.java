package com.treadly.client.lib.sdk.Model;

/* loaded from: classes2.dex */
public class GameModeSegmentDisplay {
    public static final int segmentDisplayCount = 15;
    public boolean enable;
    public byte[] segmentDisplayValues = new byte[15];

    public GameModeSegmentDisplay(boolean z, byte[] bArr) {
        this.enable = z;
        if (bArr == null || bArr.length > 15) {
            return;
        }
        for (int i = 0; i < bArr.length; i++) {
            this.segmentDisplayValues[i] = bArr[i];
        }
    }
}
