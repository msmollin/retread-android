package com.daasuu.gpuv.composer;

import android.media.MediaCodec;
import android.media.MediaFormat;
import android.media.MediaMuxer;
import android.util.Log;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.ArrayList;
import java.util.List;

/* loaded from: classes.dex */
class MuxRender {
    private static final int BUFFER_SIZE = 65536;
    private static final String TAG = "MuxRender";
    private MediaFormat audioFormat;
    private int audioTrackIndex;
    private ByteBuffer byteBuffer;
    private final MediaMuxer muxer;
    private final List<SampleInfo> sampleInfoList = new ArrayList();
    private boolean started;
    private MediaFormat videoFormat;
    private int videoTrackIndex;

    /* loaded from: classes.dex */
    public enum SampleType {
        VIDEO,
        AUDIO
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public MuxRender(MediaMuxer mediaMuxer) {
        this.muxer = mediaMuxer;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void setOutputFormat(SampleType sampleType, MediaFormat mediaFormat) {
        switch (sampleType) {
            case VIDEO:
                this.videoFormat = mediaFormat;
                return;
            case AUDIO:
                this.audioFormat = mediaFormat;
                return;
            default:
                throw new AssertionError();
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void onSetOutputFormat() {
        if (this.videoFormat != null && this.audioFormat != null) {
            this.videoTrackIndex = this.muxer.addTrack(this.videoFormat);
            Log.v(TAG, "Added track #" + this.videoTrackIndex + " with " + this.videoFormat.getString("mime") + " to muxer");
            this.audioTrackIndex = this.muxer.addTrack(this.audioFormat);
            Log.v(TAG, "Added track #" + this.audioTrackIndex + " with " + this.audioFormat.getString("mime") + " to muxer");
        } else if (this.videoFormat != null) {
            this.videoTrackIndex = this.muxer.addTrack(this.videoFormat);
            Log.v(TAG, "Added track #" + this.videoTrackIndex + " with " + this.videoFormat.getString("mime") + " to muxer");
        }
        this.muxer.start();
        this.started = true;
        int i = 0;
        if (this.byteBuffer == null) {
            this.byteBuffer = ByteBuffer.allocate(0);
        }
        this.byteBuffer.flip();
        Log.v(TAG, "Output format determined, writing " + this.sampleInfoList.size() + " samples / " + this.byteBuffer.limit() + " bytes to muxer.");
        MediaCodec.BufferInfo bufferInfo = new MediaCodec.BufferInfo();
        for (SampleInfo sampleInfo : this.sampleInfoList) {
            sampleInfo.writeToBufferInfo(bufferInfo, i);
            this.muxer.writeSampleData(getTrackIndexForSampleType(sampleInfo.sampleType), this.byteBuffer, bufferInfo);
            i += sampleInfo.size;
        }
        this.sampleInfoList.clear();
        this.byteBuffer = null;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void writeSampleData(SampleType sampleType, ByteBuffer byteBuffer, MediaCodec.BufferInfo bufferInfo) {
        if (this.started) {
            this.muxer.writeSampleData(getTrackIndexForSampleType(sampleType), byteBuffer, bufferInfo);
            return;
        }
        byteBuffer.limit(bufferInfo.offset + bufferInfo.size);
        byteBuffer.position(bufferInfo.offset);
        if (this.byteBuffer == null) {
            this.byteBuffer = ByteBuffer.allocateDirect(65536).order(ByteOrder.nativeOrder());
        }
        this.byteBuffer.put(byteBuffer);
        this.sampleInfoList.add(new SampleInfo(sampleType, bufferInfo.size, bufferInfo));
    }

    private int getTrackIndexForSampleType(SampleType sampleType) {
        switch (sampleType) {
            case VIDEO:
                return this.videoTrackIndex;
            case AUDIO:
                return this.audioTrackIndex;
            default:
                throw new AssertionError();
        }
    }

    /* loaded from: classes.dex */
    private static class SampleInfo {
        private final int flags;
        private final long presentationTimeUs;
        private final SampleType sampleType;
        private final int size;

        private SampleInfo(SampleType sampleType, int i, MediaCodec.BufferInfo bufferInfo) {
            this.sampleType = sampleType;
            this.size = i;
            this.presentationTimeUs = bufferInfo.presentationTimeUs;
            this.flags = bufferInfo.flags;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void writeToBufferInfo(MediaCodec.BufferInfo bufferInfo, int i) {
            bufferInfo.set(i, this.size, this.presentationTimeUs, this.flags);
        }
    }
}
