package com.opentok.android;

/* loaded from: classes.dex */
public class VideoUtils {

    /* loaded from: classes.dex */
    public static class Size {
        public int height;
        public int width;

        public Size() {
        }

        public Size(int i, int i2) {
            this.width = i;
            this.height = i2;
        }

        public Size(Size size) {
            this.width = size.width;
            this.height = size.height;
        }

        public final boolean equals(int i, int i2) {
            return this.width == i && this.height == i2;
        }

        public final boolean equals(Object obj) {
            if (obj instanceof Size) {
                if (obj != this) {
                    Size size = (Size) obj;
                    if (equals(size.width, size.height)) {
                    }
                }
                return true;
            }
            return false;
        }

        public final void set(int i, int i2) {
            this.width = i;
            this.height = i2;
        }

        public final void set(Size size) {
            this.width = size.width;
            this.height = size.height;
        }
    }
}
