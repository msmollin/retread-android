package com.daasuu.gpuv.composer;

import android.media.MediaCodec;
import android.media.MediaCrypto;
import android.media.MediaExtractor;
import android.media.MediaFormat;
import android.util.Size;
import android.view.Surface;
import com.daasuu.gpuv.composer.MuxRender;
import com.daasuu.gpuv.egl.filter.GlFilter;
import java.io.IOException;
import java.nio.ByteBuffer;

/* loaded from: classes.dex */
class VideoComposer {
    private static final int DRAIN_STATE_CONSUMED = 2;
    private static final int DRAIN_STATE_NONE = 0;
    private static final int DRAIN_STATE_SHOULD_RETRY_IMMEDIATELY = 1;
    private static final String TAG = "VideoComposer";
    private MediaFormat actualOutputFormat;
    private final MediaCodec.BufferInfo bufferInfo = new MediaCodec.BufferInfo();
    private MediaCodec decoder;
    private ByteBuffer[] decoderInputBuffers;
    private boolean decoderStarted;
    private DecoderSurface decoderSurface;
    private MediaCodec encoder;
    private ByteBuffer[] encoderOutputBuffers;
    private boolean encoderStarted;
    private EncoderSurface encoderSurface;
    private boolean isDecoderEOS;
    private boolean isEncoderEOS;
    private boolean isExtractorEOS;
    private final MediaExtractor mediaExtractor;
    private final MuxRender muxRender;
    private final MediaFormat outputFormat;
    private final int timeScale;
    private final int trackIndex;
    private long writtenPresentationTimeUs;

    /* JADX INFO: Access modifiers changed from: package-private */
    public VideoComposer(MediaExtractor mediaExtractor, int i, MediaFormat mediaFormat, MuxRender muxRender, int i2) {
        this.mediaExtractor = mediaExtractor;
        this.trackIndex = i;
        this.outputFormat = mediaFormat;
        this.muxRender = muxRender;
        this.timeScale = i2;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void setUp(GlFilter glFilter, Rotation rotation, Size size, Size size2, FillMode fillMode, FillModeCustomItem fillModeCustomItem, boolean z, boolean z2) {
        this.mediaExtractor.selectTrack(this.trackIndex);
        try {
            this.encoder = MediaCodec.createEncoderByType(this.outputFormat.getString("mime"));
            this.encoder.configure(this.outputFormat, (Surface) null, (MediaCrypto) null, 1);
            this.encoderSurface = new EncoderSurface(this.encoder.createInputSurface());
            this.encoderSurface.makeCurrent();
            this.encoder.start();
            this.encoderStarted = true;
            this.encoderOutputBuffers = this.encoder.getOutputBuffers();
            MediaFormat trackFormat = this.mediaExtractor.getTrackFormat(this.trackIndex);
            if (trackFormat.containsKey("rotation-degrees")) {
                trackFormat.setInteger("rotation-degrees", 0);
            }
            this.decoderSurface = new DecoderSurface(glFilter);
            this.decoderSurface.setRotation(rotation);
            this.decoderSurface.setOutputResolution(size);
            this.decoderSurface.setInputResolution(size2);
            this.decoderSurface.setFillMode(fillMode);
            this.decoderSurface.setFillModeCustomItem(fillModeCustomItem);
            this.decoderSurface.setFlipHorizontal(z2);
            this.decoderSurface.setFlipVertical(z);
            this.decoderSurface.completeParams();
            try {
                this.decoder = MediaCodec.createDecoderByType(trackFormat.getString("mime"));
                this.decoder.configure(trackFormat, this.decoderSurface.getSurface(), (MediaCrypto) null, 0);
                this.decoder.start();
                this.decoderStarted = true;
                this.decoderInputBuffers = this.decoder.getInputBuffers();
            } catch (IOException e) {
                throw new IllegalStateException(e);
            }
        } catch (IOException e2) {
            throw new IllegalStateException(e2);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean stepPipeline() {
        int drainDecoder;
        boolean z = false;
        while (drainEncoder() != 0) {
            z = true;
        }
        do {
            drainDecoder = drainDecoder();
            if (drainDecoder != 0) {
                z = true;
                continue;
            }
        } while (drainDecoder == 1);
        while (drainExtractor() != 0) {
            z = true;
        }
        return z;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public long getWrittenPresentationTimeUs() {
        return this.writtenPresentationTimeUs;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean isFinished() {
        return this.isEncoderEOS;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void release() {
        if (this.decoderSurface != null) {
            this.decoderSurface.release();
            this.decoderSurface = null;
        }
        if (this.encoderSurface != null) {
            this.encoderSurface.release();
            this.encoderSurface = null;
        }
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

    private int drainExtractor() {
        int dequeueInputBuffer;
        if (this.isExtractorEOS) {
            return 0;
        }
        int sampleTrackIndex = this.mediaExtractor.getSampleTrackIndex();
        if ((sampleTrackIndex < 0 || sampleTrackIndex == this.trackIndex) && (dequeueInputBuffer = this.decoder.dequeueInputBuffer(0L)) >= 0) {
            if (sampleTrackIndex < 0) {
                this.isExtractorEOS = true;
                this.decoder.queueInputBuffer(dequeueInputBuffer, 0, 0, 0L, 4);
                return 0;
            }
            this.decoder.queueInputBuffer(dequeueInputBuffer, 0, this.mediaExtractor.readSampleData(this.decoderInputBuffers[dequeueInputBuffer], 0), this.mediaExtractor.getSampleTime() / this.timeScale, (this.mediaExtractor.getSampleFlags() & 1) != 0 ? 1 : 0);
            this.mediaExtractor.advance();
            return 2;
        }
        return 0;
    }

    private int drainDecoder() {
        if (this.isDecoderEOS) {
            return 0;
        }
        int dequeueOutputBuffer = this.decoder.dequeueOutputBuffer(this.bufferInfo, 0L);
        switch (dequeueOutputBuffer) {
            case -3:
            case -2:
                return 1;
            case -1:
                return 0;
            default:
                if ((this.bufferInfo.flags & 4) != 0) {
                    this.encoder.signalEndOfInputStream();
                    this.isDecoderEOS = true;
                    this.bufferInfo.size = 0;
                }
                boolean z = this.bufferInfo.size > 0;
                this.decoder.releaseOutputBuffer(dequeueOutputBuffer, z);
                if (z) {
                    this.decoderSurface.awaitNewImage();
                    this.decoderSurface.drawImage();
                    this.encoderSurface.setPresentationTime(this.bufferInfo.presentationTimeUs * 1000);
                    this.encoderSurface.swapBuffers();
                    return 2;
                }
                return 2;
        }
    }

    private int drainEncoder() {
        if (this.isEncoderEOS) {
            return 0;
        }
        int dequeueOutputBuffer = this.encoder.dequeueOutputBuffer(this.bufferInfo, 0L);
        switch (dequeueOutputBuffer) {
            case -3:
                this.encoderOutputBuffers = this.encoder.getOutputBuffers();
                return 1;
            case -2:
                if (this.actualOutputFormat != null) {
                    throw new RuntimeException("Video output format changed twice.");
                }
                this.actualOutputFormat = this.encoder.getOutputFormat();
                this.muxRender.setOutputFormat(MuxRender.SampleType.VIDEO, this.actualOutputFormat);
                this.muxRender.onSetOutputFormat();
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
                this.muxRender.writeSampleData(MuxRender.SampleType.VIDEO, this.encoderOutputBuffers[dequeueOutputBuffer], this.bufferInfo);
                this.writtenPresentationTimeUs = this.bufferInfo.presentationTimeUs;
                this.encoder.releaseOutputBuffer(dequeueOutputBuffer, false);
                return 2;
        }
    }
}
