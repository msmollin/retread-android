package org.otwebrtc;

/* loaded from: classes2.dex */
public class LibvpxVp9Encoder extends WrappedNativeVideoEncoder {
    static native long nativeCreateEncoder();

    /* JADX INFO: Access modifiers changed from: package-private */
    public static native boolean nativeIsSupported();

    @Override // org.otwebrtc.WrappedNativeVideoEncoder, org.otwebrtc.VideoEncoder
    public long createNativeVideoEncoder() {
        return nativeCreateEncoder();
    }

    @Override // org.otwebrtc.WrappedNativeVideoEncoder, org.otwebrtc.VideoEncoder
    public boolean isHardwareEncoder() {
        return false;
    }
}
