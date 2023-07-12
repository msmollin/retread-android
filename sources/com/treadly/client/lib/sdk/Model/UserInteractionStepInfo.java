package com.treadly.client.lib.sdk.Model;

/* loaded from: classes2.dex */
public class UserInteractionStepInfo {
    public int centerLocation;
    public int endLocation;
    public int startLocation;

    public UserInteractionStepInfo(int i, int i2) {
        this.startLocation = i;
        this.endLocation = i2;
        int i3 = i + i2;
        this.centerLocation = i3 > 0 ? i3 / 2 : 0;
    }
}
