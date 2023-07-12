package com.treadly.client.lib.sdk.Listeners;

import com.treadly.client.lib.sdk.Model.TestNotification;

/* loaded from: classes2.dex */
public interface TestRequestEventListener {
    void onTestNotificationResponse(TestNotification testNotification);

    void onTestStartResponse(boolean z);
}
