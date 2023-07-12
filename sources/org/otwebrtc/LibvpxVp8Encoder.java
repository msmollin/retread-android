package org.otwebrtc;

/* loaded from: classes2.dex */
public class LibvpxVp8Encoder extends WrappedNativeVideoEncoder {
    static native long nativeCreateEncoder();

    @Override // org.otwebrtc.WrappedNativeVideoEncoder, org.otwebrtc.VideoEncoder
    public long createNativeVideoEncoder() {
        return nativeCreateEncoder();
    }

    @Override // org.otwebrtc.WrappedNativeVideoEncoder, org.otwebrtc.VideoEncoder
    public boolean isHardwareEncoder() {
        return false;
    }
}
