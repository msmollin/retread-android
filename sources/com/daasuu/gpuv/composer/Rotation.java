package com.daasuu.gpuv.composer;

import com.facebook.imagepipeline.common.RotationOptions;

/* loaded from: classes.dex */
public enum Rotation {
    NORMAL(0),
    ROTATION_90(90),
    ROTATION_180(RotationOptions.ROTATE_180),
    ROTATION_270(RotationOptions.ROTATE_270);
    
    private final int rotation;

    Rotation(int i) {
        this.rotation = i;
    }

    public int getRotation() {
        return this.rotation;
    }

    public static Rotation fromInt(int i) {
        Rotation[] values;
        for (Rotation rotation : values()) {
            if (i == rotation.getRotation()) {
                return rotation;
            }
        }
        return NORMAL;
    }
}
