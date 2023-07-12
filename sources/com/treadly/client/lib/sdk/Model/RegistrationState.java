package com.treadly.client.lib.sdk.Model;

/* loaded from: classes2.dex */
public enum RegistrationState {
    active(0),
    inactive(1),
    chkAuthState(2),
    unknown(3);
    
    private int value;

    RegistrationState(int i) {
        this.value = i;
    }

    public int value() {
        return this.value;
    }

    public static RegistrationState fromValue(int i) {
        RegistrationState[] values;
        for (RegistrationState registrationState : values()) {
            if (registrationState.value == i) {
                return registrationState;
            }
        }
        return null;
    }
}
