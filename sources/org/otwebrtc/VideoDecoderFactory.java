package org.otwebrtc;

import androidx.annotation.Nullable;

/* loaded from: classes2.dex */
public interface VideoDecoderFactory {
    @Nullable
    @Deprecated
    default VideoDecoder createDecoder(String str) {
        throw new UnsupportedOperationException("Deprecated and not implemented.");
    }

    @Nullable
    @CalledByNative
    default VideoDecoder createDecoder(VideoCodecInfo videoCodecInfo) {
        return createDecoder(videoCodecInfo.getName());
    }

    @CalledByNative
    default VideoCodecInfo[] getSupportedCodecs() {
        return new VideoCodecInfo[0];
    }
}
