package com.bambuser.broadcaster;

import android.media.MediaCodec;
import android.media.MediaCodecInfo;
import android.media.MediaCodecList;
import android.media.MediaCrypto;
import android.media.MediaFormat;
import android.util.Log;
import android.view.Surface;
import java.io.IOException;
import java.nio.ByteBuffer;

/* loaded from: classes.dex */
class MediaCodecAudioWrapper {
    private static final String LOGTAG = "MediaCodecAudioWrapper";
    private MediaCodec.BufferInfo mBufferInfo;
    private MediaCodec mCodec;
    private ByteBuffer[] mInputBuffers;
    private ByteBuffer[] mOutputBuffers;

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes.dex */
    public static class DataHandler {
        void onFormatChanged(int i, int i2) {
        }

        void onOutputData(ByteBuffer byteBuffer, long j, boolean z) {
        }
    }

    private MediaCodecInfo findCodec(String str, boolean z, boolean z2) {
        int codecCount = MediaCodecList.getCodecCount();
        for (int i = 0; i < codecCount; i++) {
            MediaCodecInfo codecInfoAt = MediaCodecList.getCodecInfoAt(i);
            if (z == codecInfoAt.isEncoder() && (codecInfoAt.getName().startsWith("OMX.google") || !z2)) {
                for (String str2 : codecInfoAt.getSupportedTypes()) {
                    if (str2.equals(str)) {
                        return codecInfoAt;
                    }
                }
                continue;
            }
        }
        throw new IllegalArgumentException("No supported codec found");
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void init(String str, int i, int i2, int i3, boolean z) throws IllegalArgumentException, IOException {
        MediaCodecInfo findCodec = findCodec(str, true, z);
        Log.d(LOGTAG, "Using codec " + findCodec.getName());
        this.mCodec = MediaCodec.createByCodecName(findCodec.getName());
        if (this.mCodec == null) {
            throw new IllegalArgumentException("Encoder not supported");
        }
        MediaFormat createAudioFormat = MediaFormat.createAudioFormat(str, i, i2);
        createAudioFormat.setInteger("bitrate", i3);
        this.mCodec.configure(createAudioFormat, (Surface) null, (MediaCrypto) null, 1);
        this.mCodec.start();
        this.mInputBuffers = this.mCodec.getInputBuffers();
        this.mOutputBuffers = this.mCodec.getOutputBuffers();
        this.mBufferInfo = new MediaCodec.BufferInfo();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void initDecoder(String str, int i, int i2, ByteBuffer byteBuffer, boolean z) throws IllegalArgumentException, IOException {
        MediaCodecInfo findCodec = findCodec(str, false, z);
        Log.d(LOGTAG, "Using codec " + findCodec.getName());
        this.mCodec = MediaCodec.createByCodecName(findCodec.getName());
        if (this.mCodec == null) {
            throw new IllegalArgumentException("Decoder not supported");
        }
        MediaFormat createAudioFormat = MediaFormat.createAudioFormat(str, i, i2);
        if (byteBuffer != null) {
            ByteBuffer allocate = ByteBuffer.allocate(byteBuffer.remaining());
            allocate.put(byteBuffer);
            allocate.flip();
            createAudioFormat.setByteBuffer("csd-0", allocate);
        }
        this.mCodec.configure(createAudioFormat, (Surface) null, (MediaCrypto) null, 0);
        this.mCodec.start();
        this.mInputBuffers = this.mCodec.getInputBuffers();
        this.mOutputBuffers = this.mCodec.getOutputBuffers();
        this.mBufferInfo = new MediaCodec.BufferInfo();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void close() {
        if (this.mCodec != null) {
            try {
                this.mCodec.stop();
            } catch (Exception unused) {
            }
            this.mCodec.release();
            this.mCodec = null;
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void decode(byte[] bArr, int i, int i2, long j, DataHandler dataHandler) {
        encode(bArr, i, i2, j, dataHandler);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void encode(byte[] bArr, int i, int i2, long j, DataHandler dataHandler) {
        while (true) {
            int dequeueInputBuffer = this.mCodec.dequeueInputBuffer(150000L);
            if (dequeueInputBuffer < 0) {
                flush(dataHandler);
            } else {
                ByteBuffer byteBuffer = this.mInputBuffers[dequeueInputBuffer];
                byteBuffer.clear();
                byteBuffer.put(bArr, i, Math.min(i2, byteBuffer.remaining()));
                this.mCodec.queueInputBuffer(dequeueInputBuffer, 0, byteBuffer.position(), j * 1000, 0);
                return;
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void flush(DataHandler dataHandler) {
        while (true) {
            int dequeueOutputBuffer = this.mCodec.dequeueOutputBuffer(this.mBufferInfo, 0L);
            if (dequeueOutputBuffer == -3) {
                this.mOutputBuffers = this.mCodec.getOutputBuffers();
            } else if (dequeueOutputBuffer == -2) {
                MediaFormat outputFormat = this.mCodec.getOutputFormat();
                dataHandler.onFormatChanged(outputFormat.getInteger("sample-rate"), outputFormat.getInteger("channel-count"));
            } else if (dequeueOutputBuffer < 0) {
                return;
            } else {
                ByteBuffer byteBuffer = this.mOutputBuffers[dequeueOutputBuffer];
                byteBuffer.limit(this.mBufferInfo.offset + this.mBufferInfo.size);
                byteBuffer.position(this.mBufferInfo.offset);
                dataHandler.onOutputData(byteBuffer, this.mBufferInfo.presentationTimeUs / 1000, (this.mBufferInfo.flags & 2) != 0);
                this.mCodec.releaseOutputBuffer(dequeueOutputBuffer, false);
            }
        }
    }
}
