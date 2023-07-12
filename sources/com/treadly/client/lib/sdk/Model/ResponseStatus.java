package com.treadly.client.lib.sdk.Model;

import com.google.android.gms.common.internal.GmsClientSupervisor;

/* loaded from: classes2.dex */
public enum ResponseStatus {
    NO_ERROR(0),
    ERROR(1),
    REQUIRES_AUTHENTICATION(GmsClientSupervisor.DEFAULT_BIND_FLAGS),
    CUSTOM_BOARD_NOT_PAIRED(132),
    UNKNOWN(133);
    
    private int value;

    ResponseStatus(int i) {
        this.value = i;
    }

    public int value() {
        return this.value;
    }

    public static ResponseStatus fromValue(int i) {
        ResponseStatus[] values;
        for (ResponseStatus responseStatus : values()) {
            if (responseStatus.value == i) {
                return responseStatus;
            }
        }
        return null;
    }
}
