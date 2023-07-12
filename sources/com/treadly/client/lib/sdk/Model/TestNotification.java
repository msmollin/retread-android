package com.treadly.client.lib.sdk.Model;

/* loaded from: classes2.dex */
public enum TestNotification {
    notificationPass(0),
    notificationHandrailDown(1),
    notificationUserIrStart(2),
    notificationWaitingForStop(3),
    notificationHandrailUp(4),
    notificationUnknown(5);
    
    private int value;

    TestNotification(int i) {
        this.value = i;
    }

    public int value() {
        return this.value;
    }

    public static TestNotification fromValue(int i) {
        TestNotification[] values;
        for (TestNotification testNotification : values()) {
            if (testNotification.value == i) {
                return testNotification;
            }
        }
        return null;
    }
}
