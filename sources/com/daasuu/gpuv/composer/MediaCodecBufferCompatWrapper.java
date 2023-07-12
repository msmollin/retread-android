package com.daasuu.gpuv.composer;

import android.media.MediaCodec;
import android.os.Build;
import java.nio.ByteBuffer;

/* loaded from: classes.dex */
class MediaCodecBufferCompatWrapper {
    private final ByteBuffer[] inputBuffers;
    private final MediaCodec mediaCodec;
    private final ByteBuffer[] putputBuffers;

    /* JADX INFO: Access modifiers changed from: package-private */
    public MediaCodecBufferCompatWrapper(MediaCodec mediaCodec) {
        this.mediaCodec = mediaCodec;
        if (Build.VERSION.SDK_INT < 21) {
            this.inputBuffers = mediaCodec.getInputBuffers();
            this.putputBuffers = mediaCodec.getOutputBuffers();
            return;
        }
        this.putputBuffers = null;
        this.inputBuffers = null;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public ByteBuffer getInputBuffer(int i) {
        if (Build.VERSION.SDK_INT >= 21) {
            return this.mediaCodec.getInputBuffer(i);
        }
        return this.inputBuffers[i];
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public ByteBuffer getOutputBuffer(int i) {
        if (Build.VERSION.SDK_INT >= 21) {
            return this.mediaCodec.getOutputBuffer(i);
        }
        return this.putputBuffers[i];
    }
}
