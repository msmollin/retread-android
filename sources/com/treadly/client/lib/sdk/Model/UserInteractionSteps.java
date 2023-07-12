package com.treadly.client.lib.sdk.Model;

/* loaded from: classes2.dex */
public class UserInteractionSteps {
    public int centerStride;
    public int sequence;
    public UserInteractionStepInfo stepOne;
    public UserInteractionStepInfo stepTwo;

    public UserInteractionSteps(int i, int i2, UserInteractionStepInfo userInteractionStepInfo, UserInteractionStepInfo userInteractionStepInfo2) {
        this.sequence = i;
        this.centerStride = i2;
        this.stepOne = userInteractionStepInfo;
        this.stepTwo = userInteractionStepInfo2;
    }
}
