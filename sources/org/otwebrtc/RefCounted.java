package org.otwebrtc;

/* loaded from: classes2.dex */
public interface RefCounted {
    @CalledByNative
    void release();

    @CalledByNative
    void retain();
}
