package org.otwebrtc;

import com.facebook.internal.NativeProtocol;

/* loaded from: classes2.dex */
public class Size {
    public int height;
    public int width;

    public Size(int i, int i2) {
        this.width = i;
        this.height = i2;
    }

    public boolean equals(Object obj) {
        if (obj instanceof Size) {
            Size size = (Size) obj;
            return this.width == size.width && this.height == size.height;
        }
        return false;
    }

    public int hashCode() {
        return (this.width * NativeProtocol.MESSAGE_GET_ACCESS_TOKEN_REPLY) + 1 + this.height;
    }

    public String toString() {
        return this.width + "x" + this.height;
    }
}
