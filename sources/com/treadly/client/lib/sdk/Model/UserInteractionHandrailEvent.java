package com.treadly.client.lib.sdk.Model;

/* loaded from: classes2.dex */
public enum UserInteractionHandrailEvent {
    unknown(0),
    pause(1),
    speedUp(2),
    idleHandrailUp(3),
    stop(4),
    start(5),
    switchMode(6),
    speedDown(7),
    emergencyStop(8),
    idleHandrailDown(9),
    wakeUp(10);
    
    private int value;

    UserInteractionHandrailEvent(int i) {
        this.value = i;
    }

    public int value() {
        return this.value;
    }

    public static UserInteractionHandrailEvent fromValue(int i) {
        UserInteractionHandrailEvent[] values;
        for (UserInteractionHandrailEvent userInteractionHandrailEvent : values()) {
            if (userInteractionHandrailEvent.value == i) {
                return userInteractionHandrailEvent;
            }
        }
        return null;
    }
}
