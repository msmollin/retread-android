package com.daasuu.gpuv.composer;

import android.media.MediaCodec;
import android.media.MediaFormat;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.ShortBuffer;
import java.util.ArrayDeque;
import java.util.Queue;

/* loaded from: classes.dex */
class AudioChannel {
    static final int BUFFER_INDEX_END_OF_STREAM = -1;
    private static final int BYTES_PER_SHORT = 2;
    private static final long MICROSECS_PER_SEC = 1000000;
    private MediaFormat actualDecodedFormat;
    private final MediaCodec decoder;
    private final MediaCodecBufferCompatWrapper decoderBuffers;
    private final MediaFormat encodeFormat;
    private final MediaCodec encoder;
    private final MediaCodecBufferCompatWrapper encoderBuffers;
    private int inputChannelCount;
    private int inputSampleRate;
    private int outputChannelCount;
    private final Queue<AudioBuffer> emptyBuffers = new ArrayDeque();
    private final Queue<AudioBuffer> filledBuffers = new ArrayDeque();
    private final AudioBuffer overflowBuffer = new AudioBuffer();

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes.dex */
    public static class AudioBuffer {
        int bufferIndex;
        ShortBuffer data;
        long presentationTimeUs;

        private AudioBuffer() {
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public AudioChannel(MediaCodec mediaCodec, MediaCodec mediaCodec2, MediaFormat mediaFormat) {
        this.decoder = mediaCodec;
        this.encoder = mediaCodec2;
        this.encodeFormat = mediaFormat;
        this.decoderBuffers = new MediaCodecBufferCompatWrapper(this.decoder);
        this.encoderBuffers = new MediaCodecBufferCompatWrapper(this.encoder);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void setActualDecodedFormat(MediaFormat mediaFormat) {
        this.actualDecodedFormat = mediaFormat;
        this.inputSampleRate = this.actualDecodedFormat.getInteger("sample-rate");
        if (this.inputSampleRate != this.encodeFormat.getInteger("sample-rate")) {
            throw new UnsupportedOperationException("Audio sample rate conversion not supported yet.");
        }
        this.inputChannelCount = this.actualDecodedFormat.getInteger("channel-count");
        this.outputChannelCount = this.encodeFormat.getInteger("channel-count");
        if (this.inputChannelCount != 1 && this.inputChannelCount != 2) {
            throw new UnsupportedOperationException("Input channel count (" + this.inputChannelCount + ") not supported.");
        } else if (this.outputChannelCount != 1 && this.outputChannelCount != 2) {
            throw new UnsupportedOperationException("Output channel count (" + this.outputChannelCount + ") not supported.");
        } else {
            this.overflowBuffer.presentationTimeUs = 0L;
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void drainDecoderBufferAndQueue(int i, long j) {
        if (this.actualDecodedFormat == null) {
            throw new RuntimeException("Buffer received before format!");
        }
        ByteBuffer outputBuffer = i == -1 ? null : this.decoderBuffers.getOutputBuffer(i);
        AudioBuffer poll = this.emptyBuffers.poll();
        if (poll == null) {
            poll = new AudioBuffer();
        }
        poll.bufferIndex = i;
        poll.presentationTimeUs = j;
        poll.data = outputBuffer != null ? outputBuffer.asShortBuffer() : null;
        if (this.overflowBuffer.data == null) {
            this.overflowBuffer.data = ByteBuffer.allocateDirect(outputBuffer.capacity()).order(ByteOrder.nativeOrder()).asShortBuffer();
            this.overflowBuffer.data.clear().flip();
        }
        this.filledBuffers.add(poll);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean feedEncoder(long j) {
        int dequeueInputBuffer;
        boolean z = this.overflowBuffer.data != null && this.overflowBuffer.data.hasRemaining();
        if ((!this.filledBuffers.isEmpty() || z) && (dequeueInputBuffer = this.encoder.dequeueInputBuffer(j)) >= 0) {
            ShortBuffer asShortBuffer = this.encoderBuffers.getInputBuffer(dequeueInputBuffer).asShortBuffer();
            if (z) {
                this.encoder.queueInputBuffer(dequeueInputBuffer, 0, asShortBuffer.position() * 2, drainOverflow(asShortBuffer), 0);
                return true;
            }
            AudioBuffer poll = this.filledBuffers.poll();
            if (poll.bufferIndex == -1) {
                this.encoder.queueInputBuffer(dequeueInputBuffer, 0, 0, 0L, 4);
                return false;
            }
            this.encoder.queueInputBuffer(dequeueInputBuffer, 0, asShortBuffer.position() * 2, remixAndMaybeFillOverflow(poll, asShortBuffer), 0);
            if (poll != null) {
                this.decoder.releaseOutputBuffer(poll.bufferIndex, false);
                this.emptyBuffers.add(poll);
            }
            return true;
        }
        return false;
    }

    private static long sampleCountToDurationUs(int i, int i2, int i3) {
        return (i / (i2 * MICROSECS_PER_SEC)) / i3;
    }

    private long drainOverflow(ShortBuffer shortBuffer) {
        ShortBuffer shortBuffer2 = this.overflowBuffer.data;
        int limit = shortBuffer2.limit();
        int remaining = shortBuffer2.remaining();
        long sampleCountToDurationUs = this.overflowBuffer.presentationTimeUs + sampleCountToDurationUs(shortBuffer2.position(), this.inputSampleRate, this.outputChannelCount);
        shortBuffer.clear();
        shortBuffer2.limit(shortBuffer.capacity());
        shortBuffer.put(shortBuffer2);
        if (remaining >= shortBuffer.capacity()) {
            shortBuffer2.clear().limit(0);
        } else {
            shortBuffer2.limit(limit);
        }
        return sampleCountToDurationUs;
    }

    private long remixAndMaybeFillOverflow(AudioBuffer audioBuffer, ShortBuffer shortBuffer) {
        ShortBuffer shortBuffer2 = audioBuffer.data;
        ShortBuffer shortBuffer3 = this.overflowBuffer.data;
        shortBuffer.clear();
        shortBuffer2.clear();
        if (shortBuffer2.remaining() > shortBuffer.remaining()) {
            shortBuffer2.limit(shortBuffer.capacity());
            shortBuffer.put(shortBuffer2);
            shortBuffer2.limit(shortBuffer2.capacity());
            long sampleCountToDurationUs = sampleCountToDurationUs(shortBuffer2.position(), this.inputSampleRate, this.inputChannelCount);
            shortBuffer3.put(shortBuffer2);
            shortBuffer3.flip();
            this.overflowBuffer.presentationTimeUs = audioBuffer.presentationTimeUs + sampleCountToDurationUs;
        } else {
            shortBuffer.put(shortBuffer2);
        }
        return audioBuffer.presentationTimeUs;
    }
}
