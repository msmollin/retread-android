package com.treadly.client.lib.sdk.Model;

/* loaded from: classes2.dex */
public enum ConnectionStatusCode {
    noError(0),
    error(1),
    errorInvalidVersion(2);
    
    private int value;

    ConnectionStatusCode(int i) {
        this.value = i;
    }

    public int value() {
        return this.value;
    }

    public static ConnectionStatusCode fromValue(int i) {
        ConnectionStatusCode[] values;
        for (ConnectionStatusCode connectionStatusCode : values()) {
            if (connectionStatusCode.value == i) {
                return connectionStatusCode;
            }
        }
        return null;
    }
}
