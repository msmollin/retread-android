package com.daasuu.gpuv.camerarecorder.capture;

import android.media.MediaCodec;
import android.media.MediaFormat;
import android.media.MediaMuxer;
import android.util.Log;
import java.io.IOException;
import java.nio.ByteBuffer;

/* loaded from: classes.dex */
public class MediaMuxerCaptureWrapper {
    private static final String TAG = "MediaMuxerWrapper";
    private MediaEncoder audioEncoder;
    private final MediaMuxer mediaMuxer;
    private MediaEncoder videoEncoder;
    private long preventAudioPresentationTimeUs = -1;
    private int audioTrackIndex = -1;
    private int startedCount = 0;
    private int encoderCount = 0;
    private boolean isStarted = false;

    public MediaMuxerCaptureWrapper(String str) throws IOException {
        this.mediaMuxer = new MediaMuxer(str, 0);
    }

    public void prepare() throws IOException {
        if (this.videoEncoder != null) {
            this.videoEncoder.prepare();
        }
        if (this.audioEncoder != null) {
            this.audioEncoder.prepare();
        }
    }

    public void startRecording() {
        if (this.videoEncoder != null) {
            this.videoEncoder.startRecording();
        }
        if (this.audioEncoder != null) {
            this.audioEncoder.startRecording();
        }
    }

    public void stopRecording() {
        if (this.videoEncoder != null) {
            this.videoEncoder.stopRecording();
        }
        this.videoEncoder = null;
        if (this.audioEncoder != null) {
            this.audioEncoder.stopRecording();
        }
        this.audioEncoder = null;
    }

    public synchronized boolean isStarted() {
        return this.isStarted;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void addEncoder(MediaEncoder mediaEncoder) {
        if (mediaEncoder instanceof MediaVideoEncoder) {
            if (this.videoEncoder != null) {
                throw new IllegalArgumentException("Video encoder already added.");
            }
            this.videoEncoder = mediaEncoder;
        } else if (mediaEncoder instanceof MediaAudioEncoder) {
            if (this.audioEncoder != null) {
                throw new IllegalArgumentException("Video encoder already added.");
            }
            this.audioEncoder = mediaEncoder;
        } else {
            throw new IllegalArgumentException("unsupported encoder");
        }
        this.encoderCount = (this.videoEncoder != null ? 1 : 0) + (this.audioEncoder != null ? 1 : 0);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public synchronized boolean start() {
        Log.v(TAG, "start:");
        this.startedCount++;
        if (this.encoderCount > 0 && this.startedCount == this.encoderCount) {
            this.mediaMuxer.start();
            this.isStarted = true;
            notifyAll();
            Log.v(TAG, "MediaMuxer started:");
        }
        return this.isStarted;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public synchronized void stop() {
        Log.v(TAG, "stop:startedCount=" + this.startedCount);
        this.startedCount = this.startedCount + (-1);
        if (this.encoderCount > 0 && this.startedCount <= 0) {
            this.mediaMuxer.stop();
            this.mediaMuxer.release();
            this.isStarted = false;
            Log.v(TAG, "MediaMuxer stopped:");
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public synchronized int addTrack(MediaFormat mediaFormat) {
        int addTrack;
        if (this.isStarted) {
            throw new IllegalStateException("muxer already started");
        }
        addTrack = this.mediaMuxer.addTrack(mediaFormat);
        Log.i(TAG, "addTrack:trackNum=" + this.encoderCount + ",trackIx=" + addTrack + ",format=" + mediaFormat);
        if (!mediaFormat.getString("mime").startsWith("video/")) {
            this.audioTrackIndex = addTrack;
        }
        return addTrack;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public synchronized void writeSampleData(int i, ByteBuffer byteBuffer, MediaCodec.BufferInfo bufferInfo) {
        if (this.startedCount <= 0) {
            return;
        }
        if (this.audioTrackIndex == i) {
            if (this.preventAudioPresentationTimeUs < bufferInfo.presentationTimeUs) {
                this.mediaMuxer.writeSampleData(i, byteBuffer, bufferInfo);
                this.preventAudioPresentationTimeUs = bufferInfo.presentationTimeUs;
            }
        } else {
            this.mediaMuxer.writeSampleData(i, byteBuffer, bufferInfo);
        }
    }
}
