package com.daasuu.gpuv.composer;

import android.media.MediaExtractor;
import android.media.MediaFormat;
import android.media.MediaMetadataRetriever;
import android.media.MediaMuxer;
import android.util.Log;
import android.util.Size;
import com.daasuu.gpuv.egl.filter.GlFilter;
import java.io.FileDescriptor;
import java.io.IOException;

/* loaded from: classes.dex */
class GPUMp4ComposerEngine {
    private static final long PROGRESS_INTERVAL_STEPS = 10;
    private static final double PROGRESS_UNKNOWN = -1.0d;
    private static final long SLEEP_TO_WAIT_TRACK_TRANSCODERS = 10;
    private static final String TAG = "GPUMp4ComposerEngine";
    private IAudioComposer audioComposer;
    private long durationUs;
    private FileDescriptor inputFileDescriptor;
    private MediaExtractor mediaExtractor;
    private MediaMetadataRetriever mediaMetadataRetriever;
    private MediaMuxer mediaMuxer;
    private ProgressCallback progressCallback;
    private VideoComposer videoComposer;

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes.dex */
    public interface ProgressCallback {
        void onProgress(double d);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void setDataSource(FileDescriptor fileDescriptor) {
        this.inputFileDescriptor = fileDescriptor;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void setProgressCallback(ProgressCallback progressCallback) {
        this.progressCallback = progressCallback;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void compose(String str, Size size, GlFilter glFilter, int i, boolean z, Rotation rotation, Size size2, FillMode fillMode, FillModeCustomItem fillModeCustomItem, int i2, boolean z2, boolean z3) throws IOException {
        try {
            this.mediaExtractor = new MediaExtractor();
            this.mediaExtractor.setDataSource(this.inputFileDescriptor);
            int i3 = 0;
            this.mediaMuxer = new MediaMuxer(str, 0);
            this.mediaMetadataRetriever = new MediaMetadataRetriever();
            this.mediaMetadataRetriever.setDataSource(this.inputFileDescriptor);
            try {
                this.durationUs = Long.parseLong(this.mediaMetadataRetriever.extractMetadata(9)) * 1000;
            } catch (NumberFormatException unused) {
                this.durationUs = -1L;
            }
            Log.d(TAG, "Duration (us): " + this.durationUs);
            MediaFormat createVideoFormat = MediaFormat.createVideoFormat("video/avc", size.getWidth(), size.getHeight());
            createVideoFormat.setInteger("bitrate", i);
            createVideoFormat.setInteger("frame-rate", 30);
            int i4 = 1;
            createVideoFormat.setInteger("i-frame-interval", 1);
            createVideoFormat.setInteger("color-format", 2130708361);
            MuxRender muxRender = new MuxRender(this.mediaMuxer);
            if (!this.mediaExtractor.getTrackFormat(0).getString("mime").startsWith("video/")) {
                i4 = 0;
                i3 = 1;
            }
            this.videoComposer = new VideoComposer(this.mediaExtractor, i3, createVideoFormat, muxRender, i2);
            this.videoComposer.setUp(glFilter, rotation, size, size2, fillMode, fillModeCustomItem, z2, z3);
            this.mediaExtractor.selectTrack(i3);
            if (this.mediaMetadataRetriever.extractMetadata(16) != null && !z) {
                if (i2 < 2) {
                    this.audioComposer = new AudioComposer(this.mediaExtractor, i4, muxRender);
                } else {
                    this.audioComposer = new RemixAudioComposer(this.mediaExtractor, i4, this.mediaExtractor.getTrackFormat(i4), muxRender, i2);
                }
                this.audioComposer.setup();
                this.mediaExtractor.selectTrack(i4);
                runPipelines();
            } else {
                runPipelinesNoAudio();
            }
            this.mediaMuxer.stop();
            try {
                if (this.videoComposer != null) {
                    this.videoComposer.release();
                    this.videoComposer = null;
                }
                if (this.audioComposer != null) {
                    this.audioComposer.release();
                    this.audioComposer = null;
                }
                if (this.mediaExtractor != null) {
                    this.mediaExtractor.release();
                    this.mediaExtractor = null;
                }
                try {
                    if (this.mediaMuxer != null) {
                        this.mediaMuxer.release();
                        this.mediaMuxer = null;
                    }
                } catch (RuntimeException e) {
                    Log.e(TAG, "Failed to release mediaMuxer.", e);
                }
                try {
                    if (this.mediaMetadataRetriever != null) {
                        this.mediaMetadataRetriever.release();
                        this.mediaMetadataRetriever = null;
                    }
                } catch (RuntimeException e2) {
                    Log.e(TAG, "Failed to release mediaMetadataRetriever.", e2);
                }
            } catch (RuntimeException e3) {
                throw new Error("Could not shutdown mediaExtractor, codecs and mediaMuxer pipeline.", e3);
            }
        } catch (Throwable th) {
            try {
                if (this.videoComposer != null) {
                    this.videoComposer.release();
                    this.videoComposer = null;
                }
                if (this.audioComposer != null) {
                    this.audioComposer.release();
                    this.audioComposer = null;
                }
                if (this.mediaExtractor != null) {
                    this.mediaExtractor.release();
                    this.mediaExtractor = null;
                }
                try {
                    if (this.mediaMuxer != null) {
                        this.mediaMuxer.release();
                        this.mediaMuxer = null;
                    }
                } catch (RuntimeException e4) {
                    Log.e(TAG, "Failed to release mediaMuxer.", e4);
                }
                try {
                    if (this.mediaMetadataRetriever != null) {
                        this.mediaMetadataRetriever.release();
                        this.mediaMetadataRetriever = null;
                    }
                } catch (RuntimeException e5) {
                    Log.e(TAG, "Failed to release mediaMetadataRetriever.", e5);
                }
                throw th;
            } catch (RuntimeException e6) {
                throw new Error("Could not shutdown mediaExtractor, codecs and mediaMuxer pipeline.", e6);
            }
        }
    }

    private void runPipelines() {
        long j = 0;
        if (this.durationUs <= 0 && this.progressCallback != null) {
            this.progressCallback.onProgress(PROGRESS_UNKNOWN);
        }
        long j2 = 0;
        while (true) {
            if (this.videoComposer.isFinished() && this.audioComposer.isFinished()) {
                return;
            }
            boolean z = this.videoComposer.stepPipeline() || this.audioComposer.stepPipeline();
            j2++;
            if (this.durationUs > j && j2 % 10 == j) {
                double min = ((this.videoComposer.isFinished() ? 1.0d : Math.min(1.0d, this.videoComposer.getWrittenPresentationTimeUs() / this.durationUs)) + (this.audioComposer.isFinished() ? 1.0d : Math.min(1.0d, this.audioComposer.getWrittenPresentationTimeUs() / this.durationUs))) / 2.0d;
                if (this.progressCallback != null) {
                    this.progressCallback.onProgress(min);
                }
            }
            if (!z) {
                try {
                    Thread.sleep(10L);
                } catch (InterruptedException unused) {
                }
            }
            j = 0;
        }
    }

    private void runPipelinesNoAudio() {
        if (this.durationUs <= 0 && this.progressCallback != null) {
            this.progressCallback.onProgress(PROGRESS_UNKNOWN);
        }
        long j = 0;
        while (!this.videoComposer.isFinished()) {
            boolean stepPipeline = this.videoComposer.stepPipeline();
            j++;
            if (this.durationUs > 0 && j % 10 == 0) {
                double min = this.videoComposer.isFinished() ? 1.0d : Math.min(1.0d, this.videoComposer.getWrittenPresentationTimeUs() / this.durationUs);
                if (this.progressCallback != null) {
                    this.progressCallback.onProgress(min);
                }
            }
            if (!stepPipeline) {
                try {
                    Thread.sleep(10L);
                } catch (InterruptedException unused) {
                }
            }
        }
    }
}
