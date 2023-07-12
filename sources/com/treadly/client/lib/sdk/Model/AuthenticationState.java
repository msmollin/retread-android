package com.treadly.client.lib.sdk.Model;

/* loaded from: classes2.dex */
public enum AuthenticationState {
    active(0),
    inactive(1),
    disabled(2),
    unknown(3);
    
    private int value;

    AuthenticationState(int i) {
        this.value = i;
    }

    public int value() {
        return this.value;
    }

    public static AuthenticationState fromValue(int i) {
        AuthenticationState[] values;
        for (AuthenticationState authenticationState : values()) {
            if (authenticationState.value == i) {
                return authenticationState;
            }
        }
        return null;
    }
}
