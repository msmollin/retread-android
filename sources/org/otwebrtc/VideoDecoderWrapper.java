package org.otwebrtc;

import org.otwebrtc.VideoDecoder;

/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: classes2.dex */
public class VideoDecoderWrapper {
    VideoDecoderWrapper() {
    }

    @CalledByNative
    static VideoDecoder.Callback createDecoderCallback(final long j) {
        return new VideoDecoder.Callback() { // from class: org.otwebrtc.-$$Lambda$VideoDecoderWrapper$zbGUGjTKGpbocwPR_9dyB7NvU3Q
            @Override // org.otwebrtc.VideoDecoder.Callback
            public final void onDecodedFrame(VideoFrame videoFrame, Integer num, Integer num2) {
                VideoDecoderWrapper.nativeOnDecodedFrame(j, videoFrame, num, num2);
            }
        };
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static native void nativeOnDecodedFrame(long j, VideoFrame videoFrame, Integer num, Integer num2);
}
