package com.bambuser.broadcaster;

/* loaded from: classes.dex */
public final class Resolution implements Comparable<Resolution> {
    private final int mHeight;
    private final int mWidth;

    /* JADX INFO: Access modifiers changed from: package-private */
    public Resolution(int i, int i2) {
        this.mWidth = i;
        this.mHeight = i2;
    }

    @Override // java.lang.Comparable
    public int compareTo(Resolution resolution) {
        return (this.mWidth * this.mHeight) - (resolution.mWidth * resolution.mHeight);
    }

    public boolean equals(Object obj) {
        if (obj instanceof Resolution) {
            Resolution resolution = (Resolution) obj;
            return this.mWidth == resolution.mWidth && this.mHeight == resolution.mHeight;
        }
        return false;
    }

    public int hashCode() {
        return this.mHeight ^ (this.mWidth * 33);
    }

    public int getWidth() {
        return this.mWidth;
    }

    public int getHeight() {
        return this.mHeight;
    }

    public String toString() {
        return this.mWidth + "x" + this.mHeight;
    }
}
