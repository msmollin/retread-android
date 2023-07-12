package com.treadly.Treadly.Data.Model;

/* loaded from: classes2.dex */
public enum StreamPermission {
    publicStream(0),
    friendsStream(1),
    privateStream(2);
    
    private int value;

    StreamPermission(int i) {
        this.value = i;
    }

    public String title() {
        switch (this) {
            case publicStream:
                return "Public";
            case friendsStream:
                return "Friends";
            case privateStream:
                return "Private";
            default:
                return "Stream type does not exist.";
        }
    }

    public static StreamPermission fromValue(int i) {
        StreamPermission[] values;
        for (StreamPermission streamPermission : values()) {
            if (streamPermission.value == i) {
                return streamPermission;
            }
        }
        return null;
    }

    public int getValue() {
        return this.value;
    }
}
