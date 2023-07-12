package com.treadly.client.lib.sdk.Interfaces;

/* loaded from: classes2.dex */
public enum BaseEnum {
    ;
    
    private int value;

    BaseEnum(int i) {
        this.value = i;
    }

    public int value() {
        return this.value;
    }

    public static BaseEnum fromValue(int i) {
        BaseEnum[] values;
        for (BaseEnum baseEnum : values()) {
            if (baseEnum.value == i) {
                return baseEnum;
            }
        }
        return null;
    }
}
