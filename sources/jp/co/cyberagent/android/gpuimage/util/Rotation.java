package jp.co.cyberagent.android.gpuimage.util;

import com.facebook.imagepipeline.common.RotationOptions;

/* loaded from: classes2.dex */
public enum Rotation {
    NORMAL,
    ROTATION_90,
    ROTATION_180,
    ROTATION_270;

    public int asInt() {
        switch (this) {
            case NORMAL:
                return 0;
            case ROTATION_90:
                return 90;
            case ROTATION_180:
                return RotationOptions.ROTATE_180;
            case ROTATION_270:
                return RotationOptions.ROTATE_270;
            default:
                throw new IllegalStateException("Unknown Rotation!");
        }
    }

    public static Rotation fromInt(int i) {
        if (i != 0) {
            if (i != 90) {
                if (i != 180) {
                    if (i != 270) {
                        if (i == 360) {
                            return NORMAL;
                        }
                        throw new IllegalStateException(i + " is an unknown rotation. Needs to be either 0, 90, 180 or 270!");
                    }
                    return ROTATION_270;
                }
                return ROTATION_180;
            }
            return ROTATION_90;
        }
        return NORMAL;
    }
}
