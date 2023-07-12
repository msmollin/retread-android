package org.otwebrtc;

/* loaded from: classes2.dex */
public class LibvpxVp8Decoder extends WrappedNativeVideoDecoder {
    static native long nativeCreateDecoder();

    @Override // org.otwebrtc.WrappedNativeVideoDecoder, org.otwebrtc.VideoDecoder
    public long createNativeVideoDecoder() {
        return nativeCreateDecoder();
    }
}
