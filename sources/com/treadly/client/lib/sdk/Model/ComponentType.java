package com.treadly.client.lib.sdk.Model;

/* loaded from: classes2.dex */
public enum ComponentType {
    mainBoard(0),
    bleBoard(1),
    watch(2),
    motorController(3),
    app(4),
    unknown(5),
    remote(6);
    
    private int value;

    ComponentType(int i) {
        this.value = i;
    }

    public int value() {
        return this.value;
    }

    public static ComponentType fromValue(int i) {
        ComponentType[] values;
        for (ComponentType componentType : values()) {
            if (componentType.value == i) {
                return componentType;
            }
        }
        return null;
    }
}
