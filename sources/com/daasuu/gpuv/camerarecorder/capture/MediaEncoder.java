package com.daasuu.gpuv.camerarecorder.capture;

import android.media.MediaCodec;
import android.util.Log;
import java.io.IOException;
import java.lang.ref.WeakReference;
import java.nio.ByteBuffer;

/* loaded from: classes.dex */
public abstract class MediaEncoder implements Runnable {
    protected static final int TIMEOUT_USEC = 10000;
    private MediaCodec.BufferInfo bufferInfo;
    protected volatile boolean isCapturing;
    protected boolean isEOS;
    protected final MediaEncoderListener listener;
    protected MediaCodec mediaCodec;
    protected boolean muxerStarted;
    protected int requestDrain;
    protected volatile boolean requestStop;
    protected int trackIndex;
    protected final WeakReference<MediaMuxerCaptureWrapper> weakMuxer;
    private final String TAG = getClass().getSimpleName();
    protected final Object sync = new Object();
    private long prevOutputPTSUs = 0;

    /* loaded from: classes.dex */
    public interface MediaEncoderListener {
        void onExit();

        void onPrepared(MediaEncoder mediaEncoder);

        void onStopped(MediaEncoder mediaEncoder);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public abstract void prepare() throws IOException;

    /* JADX INFO: Access modifiers changed from: package-private */
    public MediaEncoder(MediaMuxerCaptureWrapper mediaMuxerCaptureWrapper, MediaEncoderListener mediaEncoderListener) {
        if (mediaEncoderListener == null) {
            throw new NullPointerException("MediaEncoderListener is null");
        }
        if (mediaMuxerCaptureWrapper == null) {
            throw new NullPointerException("MediaMuxerCaptureWrapper is null");
        }
        this.weakMuxer = new WeakReference<>(mediaMuxerCaptureWrapper);
        mediaMuxerCaptureWrapper.addEncoder(this);
        this.listener = mediaEncoderListener;
        synchronized (this.sync) {
            this.bufferInfo = new MediaCodec.BufferInfo();
            new Thread(this, getClass().getSimpleName()).start();
            try {
                this.sync.wait();
            } catch (InterruptedException unused) {
            }
        }
    }

    public boolean frameAvailableSoon() {
        synchronized (this.sync) {
            if (this.isCapturing && !this.requestStop) {
                this.requestDrain++;
                this.sync.notifyAll();
                return true;
            }
            return false;
        }
    }

    /* JADX WARN: Removed duplicated region for block: B:50:0x004f A[EXC_TOP_SPLITTER, SYNTHETIC] */
    @Override // java.lang.Runnable
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public void run() {
        /*
            r6 = this;
            java.lang.Object r0 = r6.sync
            monitor-enter(r0)
            r1 = 0
            r6.requestStop = r1     // Catch: java.lang.Throwable -> L62
            r6.requestDrain = r1     // Catch: java.lang.Throwable -> L62
            java.lang.Object r2 = r6.sync     // Catch: java.lang.Throwable -> L62
            r2.notify()     // Catch: java.lang.Throwable -> L62
            monitor-exit(r0)     // Catch: java.lang.Throwable -> L62
        Le:
            java.lang.Object r2 = r6.sync
            monitor-enter(r2)
            boolean r0 = r6.requestStop     // Catch: java.lang.Throwable -> L5f
            int r3 = r6.requestDrain     // Catch: java.lang.Throwable -> L5f
            r4 = 1
            if (r3 <= 0) goto L1a
            r3 = r4
            goto L1b
        L1a:
            r3 = r1
        L1b:
            if (r3 == 0) goto L22
            int r5 = r6.requestDrain     // Catch: java.lang.Throwable -> L5f
            int r5 = r5 - r4
            r6.requestDrain = r5     // Catch: java.lang.Throwable -> L5f
        L22:
            monitor-exit(r2)     // Catch: java.lang.Throwable -> L5f
            if (r0 == 0) goto L32
            r6.drain()
            r6.signalEndOfInputStream()
            r6.drain()
            r6.release()
            goto L45
        L32:
            if (r3 == 0) goto L38
            r6.drain()
            goto Le
        L38:
            java.lang.Object r0 = r6.sync
            monitor-enter(r0)
            java.lang.Object r2 = r6.sync     // Catch: java.lang.Throwable -> L42 java.lang.InterruptedException -> L44
            r2.wait()     // Catch: java.lang.Throwable -> L42 java.lang.InterruptedException -> L44
            monitor-exit(r0)     // Catch: java.lang.Throwable -> L42
            goto Le
        L42:
            r6 = move-exception
            goto L5d
        L44:
            monitor-exit(r0)     // Catch: java.lang.Throwable -> L42
        L45:
            java.lang.String r0 = r6.TAG
            java.lang.String r2 = "Encoder thread exiting"
            android.util.Log.d(r0, r2)
            java.lang.Object r2 = r6.sync
            monitor-enter(r2)
            r6.requestStop = r4     // Catch: java.lang.Throwable -> L5a
            r6.isCapturing = r1     // Catch: java.lang.Throwable -> L5a
            monitor-exit(r2)     // Catch: java.lang.Throwable -> L5a
            com.daasuu.gpuv.camerarecorder.capture.MediaEncoder$MediaEncoderListener r6 = r6.listener
            r6.onExit()
            return
        L5a:
            r6 = move-exception
            monitor-exit(r2)     // Catch: java.lang.Throwable -> L5a
            throw r6
        L5d:
            monitor-exit(r0)     // Catch: java.lang.Throwable -> L42
            throw r6
        L5f:
            r6 = move-exception
            monitor-exit(r2)     // Catch: java.lang.Throwable -> L5f
            throw r6
        L62:
            r6 = move-exception
            monitor-exit(r0)     // Catch: java.lang.Throwable -> L62
            throw r6
        */
        throw new UnsupportedOperationException("Method not decompiled: com.daasuu.gpuv.camerarecorder.capture.MediaEncoder.run():void");
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void startRecording() {
        Log.v(this.TAG, "startRecording");
        synchronized (this.sync) {
            this.isCapturing = true;
            this.requestStop = false;
            this.sync.notifyAll();
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void stopRecording() {
        Log.v(this.TAG, "stopRecording");
        synchronized (this.sync) {
            if (this.isCapturing && !this.requestStop) {
                this.requestStop = true;
                this.sync.notifyAll();
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void release() {
        Log.d(this.TAG, "release:");
        try {
            this.listener.onStopped(this);
        } catch (Exception e) {
            Log.e(this.TAG, "failed onStopped", e);
        }
        this.isCapturing = false;
        if (this.mediaCodec != null) {
            try {
                this.mediaCodec.stop();
                this.mediaCodec.release();
                this.mediaCodec = null;
            } catch (Exception e2) {
                Log.e(this.TAG, "failed releasing MediaCodec", e2);
            }
        }
        if (this.muxerStarted) {
            MediaMuxerCaptureWrapper mediaMuxerCaptureWrapper = this.weakMuxer != null ? this.weakMuxer.get() : null;
            if (mediaMuxerCaptureWrapper != null) {
                try {
                    mediaMuxerCaptureWrapper.stop();
                } catch (Exception e3) {
                    Log.e(this.TAG, "failed stopping muxer", e3);
                }
            }
        }
        this.bufferInfo = null;
    }

    protected void signalEndOfInputStream() {
        Log.d(this.TAG, "sending EOS to encoder");
        encode(null, 0, getPTSUs());
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void encode(ByteBuffer byteBuffer, int i, long j) {
        if (this.isCapturing) {
            while (this.isCapturing) {
                int dequeueInputBuffer = this.mediaCodec.dequeueInputBuffer(10000L);
                if (dequeueInputBuffer >= 0) {
                    ByteBuffer inputBuffer = this.mediaCodec.getInputBuffer(dequeueInputBuffer);
                    inputBuffer.clear();
                    if (byteBuffer != null) {
                        inputBuffer.put(byteBuffer);
                    }
                    if (i <= 0) {
                        this.isEOS = true;
                        Log.i(this.TAG, "send BUFFER_FLAG_END_OF_STREAM");
                        this.mediaCodec.queueInputBuffer(dequeueInputBuffer, 0, 0, j, 4);
                        return;
                    }
                    this.mediaCodec.queueInputBuffer(dequeueInputBuffer, 0, i, j, 0);
                    return;
                }
            }
        }
    }

    private void drain() {
        if (this.mediaCodec == null) {
            return;
        }
        MediaMuxerCaptureWrapper mediaMuxerCaptureWrapper = this.weakMuxer.get();
        if (mediaMuxerCaptureWrapper == null) {
            Log.w(this.TAG, "muxer is unexpectedly null");
            return;
        }
        int i = 0;
        while (this.isCapturing) {
            int dequeueOutputBuffer = this.mediaCodec.dequeueOutputBuffer(this.bufferInfo, 10000L);
            if (dequeueOutputBuffer == -1) {
                if (!this.isEOS && (i = i + 1) > 5) {
                    return;
                }
            } else if (dequeueOutputBuffer == -2) {
                Log.v(this.TAG, "INFO_OUTPUT_FORMAT_CHANGED");
                if (this.muxerStarted) {
                    throw new RuntimeException("format changed twice");
                }
                this.trackIndex = mediaMuxerCaptureWrapper.addTrack(this.mediaCodec.getOutputFormat());
                this.muxerStarted = true;
                if (mediaMuxerCaptureWrapper.start()) {
                    continue;
                } else {
                    synchronized (mediaMuxerCaptureWrapper) {
                        while (!mediaMuxerCaptureWrapper.isStarted()) {
                            try {
                                mediaMuxerCaptureWrapper.wait(100L);
                            } catch (InterruptedException unused) {
                                return;
                            }
                        }
                    }
                }
            } else if (dequeueOutputBuffer < 0) {
                Log.w(this.TAG, "drain:unexpected result from encoder#dequeueOutputBuffer: " + dequeueOutputBuffer);
            } else {
                ByteBuffer outputBuffer = this.mediaCodec.getOutputBuffer(dequeueOutputBuffer);
                if (outputBuffer == null) {
                    throw new RuntimeException("encoderOutputBuffer " + dequeueOutputBuffer + " was null");
                }
                if ((this.bufferInfo.flags & 2) != 0) {
                    Log.d(this.TAG, "drain:BUFFER_FLAG_CODEC_CONFIG");
                    this.bufferInfo.size = 0;
                }
                if (this.bufferInfo.size != 0) {
                    if (!this.muxerStarted) {
                        throw new RuntimeException("drain:muxer hasn't started");
                    }
                    this.bufferInfo.presentationTimeUs = getPTSUs();
                    mediaMuxerCaptureWrapper.writeSampleData(this.trackIndex, outputBuffer, this.bufferInfo);
                    this.prevOutputPTSUs = this.bufferInfo.presentationTimeUs;
                    i = 0;
                }
                this.mediaCodec.releaseOutputBuffer(dequeueOutputBuffer, false);
                if ((this.bufferInfo.flags & 4) != 0) {
                    this.isCapturing = false;
                    return;
                }
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public long getPTSUs() {
        long nanoTime = System.nanoTime() / 1000;
        return nanoTime < this.prevOutputPTSUs ? nanoTime + (this.prevOutputPTSUs - nanoTime) : nanoTime;
    }
}
