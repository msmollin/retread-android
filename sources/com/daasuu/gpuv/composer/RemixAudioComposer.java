package com.daasuu.gpuv.composer;

import android.media.MediaCodec;
import android.media.MediaCrypto;
import android.media.MediaExtractor;
import android.media.MediaFormat;
import android.view.Surface;
import com.daasuu.gpuv.composer.MuxRender;
import java.io.IOException;

/* loaded from: classes.dex */
class RemixAudioComposer implements IAudioComposer {
    private static final int DRAIN_STATE_CONSUMED = 2;
    private static final int DRAIN_STATE_NONE = 0;
    private static final int DRAIN_STATE_SHOULD_RETRY_IMMEDIATELY = 1;
    private static final MuxRender.SampleType SAMPLE_TYPE = MuxRender.SampleType.AUDIO;
    private MediaFormat actualOutputFormat;
    private AudioChannel audioChannel;
    private MediaCodec decoder;
    private MediaCodecBufferCompatWrapper decoderBuffers;
    private boolean decoderStarted;
    private MediaCodec encoder;
    private MediaCodecBufferCompatWrapper encoderBuffers;
    private boolean encoderStarted;
    private final MediaExtractor extractor;
    private boolean isDecoderEOS;
    private boolean isEncoderEOS;
    private boolean isExtractorEOS;
    private final MuxRender muxer;
    private final MediaFormat outputFormat;
    private final int timeScale;
    private final int trackIndex;
    private long writtenPresentationTimeUs;
    private int muxCount = 1;
    private final MediaCodec.BufferInfo bufferInfo = new MediaCodec.BufferInfo();

    public RemixAudioComposer(MediaExtractor mediaExtractor, int i, MediaFormat mediaFormat, MuxRender muxRender, int i2) {
        this.extractor = mediaExtractor;
        this.trackIndex = i;
        this.outputFormat = mediaFormat;
        this.muxer = muxRender;
        this.timeScale = i2;
    }

    @Override // com.daasuu.gpuv.composer.IAudioComposer
    public void setup() {
        this.extractor.selectTrack(this.trackIndex);
        try {
            this.encoder = MediaCodec.createEncoderByType(this.outputFormat.getString("mime"));
            this.encoder.configure(this.outputFormat, (Surface) null, (MediaCrypto) null, 1);
            this.encoder.start();
            this.encoderStarted = true;
            this.encoderBuffers = new MediaCodecBufferCompatWrapper(this.encoder);
            MediaFormat trackFormat = this.extractor.getTrackFormat(this.trackIndex);
            try {
                this.decoder = MediaCodec.createDecoderByType(trackFormat.getString("mime"));
                this.decoder.configure(trackFormat, (Surface) null, (MediaCrypto) null, 0);
                this.decoder.start();
                this.decoderStarted = true;
                this.decoderBuffers = new MediaCodecBufferCompatWrapper(this.decoder);
                this.audioChannel = new AudioChannel(this.decoder, this.encoder, this.outputFormat);
            } catch (IOException e) {
                throw new IllegalStateException(e);
            }
        } catch (IOException e2) {
            throw new IllegalStateException(e2);
        }
    }

    @Override // com.daasuu.gpuv.composer.IAudioComposer
    public boolean stepPipeline() {
        int drainDecoder;
        boolean z = false;
        while (drainEncoder(0L) != 0) {
            z = true;
        }
        do {
            drainDecoder = drainDecoder(0L);
            if (drainDecoder != 0) {
                z = true;
                continue;
            }
        } while (drainDecoder == 1);
        while (this.audioChannel.feedEncoder(0L)) {
            z = true;
        }
        while (drainExtractor(0L) != 0) {
            z = true;
        }
        return z;
    }

    private int drainExtractor(long j) {
        int dequeueInputBuffer;
        if (this.isExtractorEOS) {
            return 0;
        }
        int sampleTrackIndex = this.extractor.getSampleTrackIndex();
        if ((sampleTrackIndex < 0 || sampleTrackIndex == this.trackIndex) && (dequeueInputBuffer = this.decoder.dequeueInputBuffer(j)) >= 0) {
            if (sampleTrackIndex < 0) {
                this.isExtractorEOS = true;
                this.decoder.queueInputBuffer(dequeueInputBuffer, 0, 0, 0L, 4);
                return 0;
            }
            this.decoder.queueInputBuffer(dequeueInputBuffer, 0, this.extractor.readSampleData(this.decoderBuffers.getInputBuffer(dequeueInputBuffer), 0), this.extractor.getSampleTime(), (this.extractor.getSampleFlags() & 1) != 0 ? 1 : 0);
            this.extractor.advance();
            return 2;
        }
        return 0;
    }

    /* JADX WARN: Can't fix incorrect switch cases order, some code will duplicate */
    private int drainDecoder(long j) {
        if (this.isDecoderEOS) {
            return 0;
        }
        int dequeueOutputBuffer = this.decoder.dequeueOutputBuffer(this.bufferInfo, j);
        switch (dequeueOutputBuffer) {
            case -3:
                break;
            case -2:
                this.audioChannel.setActualDecodedFormat(this.decoder.getOutputFormat());
                break;
            case -1:
                return 0;
            default:
                if ((this.bufferInfo.flags & 4) != 0) {
                    this.isDecoderEOS = true;
                    this.audioChannel.drainDecoderBufferAndQueue(-1, 0L);
                    return 2;
                } else if (this.bufferInfo.size > 0) {
                    this.audioChannel.drainDecoderBufferAndQueue(dequeueOutputBuffer, this.bufferInfo.presentationTimeUs / this.timeScale);
                    return 2;
                } else {
                    return 2;
                }
        }
        return 1;
    }

    private int drainEncoder(long j) {
        if (this.isEncoderEOS) {
            return 0;
        }
        int dequeueOutputBuffer = this.encoder.dequeueOutputBuffer(this.bufferInfo, j);
        switch (dequeueOutputBuffer) {
            case -3:
                this.encoderBuffers = new MediaCodecBufferCompatWrapper(this.encoder);
                return 1;
            case -2:
                if (this.actualOutputFormat != null) {
                    throw new RuntimeException("Audio output format changed twice.");
                }
                this.actualOutputFormat = this.encoder.getOutputFormat();
                this.muxer.setOutputFormat(SAMPLE_TYPE, this.actualOutputFormat);
                return 1;
            case -1:
                return 0;
            default:
                if (this.actualOutputFormat == null) {
                    throw new RuntimeException("Could not determine actual output format.");
                }
                if ((this.bufferInfo.flags & 4) != 0) {
                    this.isEncoderEOS = true;
                    this.bufferInfo.set(0, 0, 0L, this.bufferInfo.flags);
                }
                if ((this.bufferInfo.flags & 2) != 0) {
                    this.encoder.releaseOutputBuffer(dequeueOutputBuffer, false);
                    return 1;
                }
                if (this.muxCount == 1) {
                    this.muxer.writeSampleData(SAMPLE_TYPE, this.encoderBuffers.getOutputBuffer(dequeueOutputBuffer), this.bufferInfo);
                }
                if (this.muxCount < this.timeScale) {
                    this.muxCount++;
                } else {
                    this.muxCount = 1;
                }
                this.writtenPresentationTimeUs = this.bufferInfo.presentationTimeUs;
                this.encoder.releaseOutputBuffer(dequeueOutputBuffer, false);
                return 2;
        }
    }

    @Override // com.daasuu.gpuv.composer.IAudioComposer
    public long getWrittenPresentationTimeUs() {
        return this.writtenPresentationTimeUs;
    }

    @Override // com.daasuu.gpuv.composer.IAudioComposer
    public boolean isFinished() {
        return this.isEncoderEOS;
    }

    @Override // com.daasuu.gpuv.composer.IAudioComposer
    public void release() {
        if (this.decoder != null) {
            if (this.decoderStarted) {
                this.decoder.stop();
            }
            this.decoder.release();
            this.decoder = null;
        }
        if (this.encoder != null) {
            if (this.encoderStarted) {
                this.encoder.stop();
            }
            this.encoder.release();
            this.encoder = null;
        }
    }
}
