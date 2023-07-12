package com.daasuu.gpuv.composer;

import android.annotation.SuppressLint;
import android.media.MediaCodec;
import android.media.MediaExtractor;
import android.media.MediaFormat;
import com.daasuu.gpuv.composer.MuxRender;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

/* loaded from: classes.dex */
class AudioComposer implements IAudioComposer {
    static final /* synthetic */ boolean $assertionsDisabled = false;
    private MediaFormat actualOutputFormat;
    private ByteBuffer buffer;
    private int bufferSize;
    private boolean isEOS;
    private final MediaExtractor mediaExtractor;
    private final MuxRender muxRender;
    private final int trackIndex;
    private long writtenPresentationTimeUs;
    private final MuxRender.SampleType sampleType = MuxRender.SampleType.AUDIO;
    private final MediaCodec.BufferInfo bufferInfo = new MediaCodec.BufferInfo();

    @Override // com.daasuu.gpuv.composer.IAudioComposer
    public void release() {
    }

    @Override // com.daasuu.gpuv.composer.IAudioComposer
    public void setup() {
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public AudioComposer(MediaExtractor mediaExtractor, int i, MuxRender muxRender) {
        this.mediaExtractor = mediaExtractor;
        this.trackIndex = i;
        this.muxRender = muxRender;
        this.actualOutputFormat = this.mediaExtractor.getTrackFormat(this.trackIndex);
        this.muxRender.setOutputFormat(this.sampleType, this.actualOutputFormat);
        this.bufferSize = this.actualOutputFormat.getInteger("max-input-size");
        this.buffer = ByteBuffer.allocateDirect(this.bufferSize).order(ByteOrder.nativeOrder());
    }

    @Override // com.daasuu.gpuv.composer.IAudioComposer
    @SuppressLint({"Assert"})
    public boolean stepPipeline() {
        if (this.isEOS) {
            return false;
        }
        int sampleTrackIndex = this.mediaExtractor.getSampleTrackIndex();
        if (sampleTrackIndex < 0) {
            this.buffer.clear();
            this.bufferInfo.set(0, 0, 0L, 4);
            this.muxRender.writeSampleData(this.sampleType, this.buffer, this.bufferInfo);
            this.isEOS = true;
            return true;
        } else if (sampleTrackIndex != this.trackIndex) {
            return false;
        } else {
            this.buffer.clear();
            this.bufferInfo.set(0, this.mediaExtractor.readSampleData(this.buffer, 0), this.mediaExtractor.getSampleTime(), (this.mediaExtractor.getSampleFlags() & 1) != 0 ? 1 : 0);
            this.muxRender.writeSampleData(this.sampleType, this.buffer, this.bufferInfo);
            this.writtenPresentationTimeUs = this.bufferInfo.presentationTimeUs;
            this.mediaExtractor.advance();
            return true;
        }
    }

    @Override // com.daasuu.gpuv.composer.IAudioComposer
    public long getWrittenPresentationTimeUs() {
        return this.writtenPresentationTimeUs;
    }

    @Override // com.daasuu.gpuv.composer.IAudioComposer
    public boolean isFinished() {
        return this.isEOS;
    }
}
