package com.treadly.client.lib.sdk.Listeners;

import com.treadly.client.lib.sdk.Model.UserInteractionHandrailEvent;
import com.treadly.client.lib.sdk.Model.UserInteractionStatus;
import com.treadly.client.lib.sdk.Model.UserInteractionSteps;

/* loaded from: classes2.dex */
public interface UserInteractionEventListener {
    void onUserInteractionHandrailEvent(boolean z, UserInteractionHandrailEvent userInteractionHandrailEvent);

    void onUserInteractionStatus(boolean z, UserInteractionStatus userInteractionStatus);

    void onUserInteractionStepsDetected(boolean z, UserInteractionSteps userInteractionSteps);
}
