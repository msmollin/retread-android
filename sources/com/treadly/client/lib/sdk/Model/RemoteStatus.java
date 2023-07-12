package com.treadly.client.lib.sdk.Model;

/* loaded from: classes2.dex */
public enum RemoteStatus {
    automatic(0),
    manual(1),
    unknown(2);
    
    private int value;

    RemoteStatus(int i) {
        this.value = i;
    }

    public int value() {
        return this.value;
    }

    public static RemoteStatus fromValue(int i) {
        RemoteStatus[] values;
        for (RemoteStatus remoteStatus : values()) {
            if (remoteStatus.value == i) {
                return remoteStatus;
            }
        }
        return null;
    }
}
