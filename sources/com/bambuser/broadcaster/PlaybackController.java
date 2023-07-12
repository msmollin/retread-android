package com.bambuser.broadcaster;

import android.media.MediaCodec;
import android.media.MediaCrypto;
import android.media.MediaFormat;
import android.os.SystemClock;
import android.util.Log;
import android.view.Surface;
import com.bambuser.broadcaster.AudioTrackWriter;
import com.bambuser.broadcaster.MediaCodecAudioWrapper;
import com.bambuser.broadcaster.SentryLogger;
import com.github.mikephil.charting.utils.Utils;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.atomic.AtomicBoolean;
import org.json.JSONObject;

/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: classes.dex */
public final class PlaybackController implements PacketHandler {
    private static final long AUDIO_QUALITY_TIMER_INTERVAL = 76;
    private static final long DECODER_DEQUEUE_WAIT_MICROS = 40000;
    private static final String LOGTAG = "PlaybackController";
    private static final long QUEUE_HINT_INTERVAL = 4000;
    private static boolean sAudioDecoderFailed = false;
    private static boolean sVideoDecoderFailed = false;
    private int mAnchorMediaTime;
    private MediaCodecAudioWrapper mAudioDecoder;
    private AudioTrackWriter mAudioTrackWriter;
    private long mBroadcasterRealtime;
    private boolean mDoConvertH264;
    private double mDuration;
    private boolean mGotKeyframe;
    private long mLatestQueueEvalTime;
    private long mLatestQueueShrinkTime;
    private Observer mObserver;
    private int mOverflowCount;
    private long mPositionOffsetFromMediaTime;
    private final boolean mRealtime;
    private Surface mSurface;
    private MediaCodec mVideoDecoder;
    private ByteBuffer[] mVideoDecoderInput;
    private ByteBuffer mVideoHeader;
    private int mVideoHeight;
    private int mVideoWidth;
    private final AudioTrackWriter.PlaybackStateListener mAudioTrackPlaybackListener = new AudioTrackWriter.PlaybackStateListener() { // from class: com.bambuser.broadcaster.PlaybackController.5
        @Override // com.bambuser.broadcaster.AudioTrackWriter.PlaybackStateListener
        public void onStateChanged(AudioTrackWriter.PlaybackState playbackState) {
            if (playbackState == AudioTrackWriter.PlaybackState.BUFFERING) {
                if (PlaybackController.this.mObserver != null) {
                    PlaybackController.this.mObserver.onAudioBuffering();
                }
                synchronized (PlaybackController.this) {
                    PlaybackController.this.mMinAudioQueue = 0.0f;
                }
            } else if (playbackState == AudioTrackWriter.PlaybackState.PLAYING) {
                if (PlaybackController.this.mObserver != null) {
                    PlaybackController.this.mObserver.onAudioPlaying();
                }
            } else if (playbackState != AudioTrackWriter.PlaybackState.PAUSED || PlaybackController.this.mObserver == null) {
            } else {
                PlaybackController.this.mObserver.onAudioPaused();
            }
        }

        @Override // com.bambuser.broadcaster.AudioTrackWriter.PlaybackStateListener
        public void onOverflow() {
            if (PlaybackController.this.mObserver != null) {
                PlaybackController.this.mObserver.onAudioOverflow();
            }
            if (PlaybackController.this.mLargeBufferMode) {
                return;
            }
            synchronized (PlaybackController.this) {
                if (SystemClock.elapsedRealtime() - PlaybackController.this.mLatestQueueShrinkTime > 500) {
                    PlaybackController.access$808(PlaybackController.this);
                }
                AudioTrackWriter audioTrackWriter = PlaybackController.this.mAudioTrackWriter;
                if (audioTrackWriter != null && PlaybackController.this.mOverflowCount >= 3) {
                    int highWater = audioTrackWriter.getHighWater() + 100;
                    if (highWater <= 4000) {
                        Log.d(PlaybackController.LOGTAG, "raising highwater to " + highWater + " ms");
                        audioTrackWriter.setHighWater(highWater);
                    }
                    PlaybackController.this.mOverflowCount = 0;
                }
            }
        }
    };
    private final MediaCodecAudioWrapper.DataHandler mAudioDecoderHandler = new MediaCodecAudioWrapper.DataHandler() { // from class: com.bambuser.broadcaster.PlaybackController.6
        @Override // com.bambuser.broadcaster.MediaCodecAudioWrapper.DataHandler
        void onFormatChanged(int i, int i2) {
            if (PlaybackController.this.mAudioTrackWriter != null && (i != PlaybackController.this.mAudioTrackWriter.getSampleRate() || i2 != PlaybackController.this.mAudioTrackWriter.getChannelCount())) {
                PlaybackController.this.mAudioTrackWriter.close();
                PlaybackController.this.mAudioTrackWriter = null;
            }
            if (PlaybackController.this.mAudioTrackWriter == null) {
                PlaybackController.this.mAudioTrackWriter = new AudioTrackWriter(i, i2, PlaybackController.this.mRealtime, PlaybackController.this.mTimeline, PlaybackController.this.mLargeBufferMode ? 3800 : 600, PlaybackController.this.mLargeBufferMode ? 5000 : 1800);
                PlaybackController.this.mAudioTrackWriter.setAudioVolume(PlaybackController.this.mAudioVolume);
                PlaybackController.this.mAudioTrackWriter.setPlaybackStateListener(PlaybackController.this.mAudioTrackPlaybackListener);
            }
        }

        @Override // com.bambuser.broadcaster.MediaCodecAudioWrapper.DataHandler
        void onOutputData(ByteBuffer byteBuffer, long j, boolean z) {
            ByteBuffer buffer = PlaybackController.this.mAudioTrackWriter.getBuffer(byteBuffer.remaining());
            buffer.put(byteBuffer);
            buffer.flip();
            PlaybackController.this.mAudioTrackWriter.enqueue(buffer, j);
        }
    };
    private final TimerTask mAudioQualityTask = new TimerTask() { // from class: com.bambuser.broadcaster.PlaybackController.7
        @Override // java.util.TimerTask, java.lang.Runnable
        public void run() {
            synchronized (PlaybackController.this) {
                if (PlaybackController.this.mAudioTrackWriter != null && PlaybackController.this.mAudioTrackWriter.getPlaybackState() != AudioTrackWriter.PlaybackState.PAUSED) {
                    float queueDuration = PlaybackController.this.mAudioTrackWriter.getQueueDuration(false);
                    PlaybackController.this.mAvgAudioQueue = (PlaybackController.this.mAvgAudioQueue * 0.95f) + (0.05f * queueDuration);
                    if (PlaybackController.this.mRealtime && !PlaybackController.this.mLargeBufferMode) {
                        if (PlaybackController.this.mMinAudioQueue <= -0.5f || queueDuration < PlaybackController.this.mMinAudioQueue) {
                            PlaybackController.this.mMinAudioQueue = queueDuration;
                        }
                        long elapsedRealtime = SystemClock.elapsedRealtime();
                        if (PlaybackController.this.mLatestQueueEvalTime <= 0) {
                            PlaybackController.this.mLatestQueueEvalTime = elapsedRealtime;
                        }
                        if (elapsedRealtime - PlaybackController.this.mLatestQueueEvalTime > 5000) {
                            int highWater = PlaybackController.this.mAudioTrackWriter.getHighWater();
                            if (PlaybackController.this.mMinAudioQueue > 0.5f && PlaybackController.this.mAvgAudioQueue > 0.5f && highWater - 100 >= 1000) {
                                PlaybackController.this.mLatestQueueShrinkTime = elapsedRealtime;
                                int i = highWater - 100;
                                Log.d(PlaybackController.LOGTAG, "lowering highwater to " + i + " ms");
                                PlaybackController.this.mAudioTrackWriter.setHighWater(i);
                            }
                            PlaybackController.this.mMinAudioQueue = -1.0f;
                            PlaybackController.this.mLatestQueueEvalTime = elapsedRealtime;
                        }
                    }
                    if (PlaybackController.this.mObserver != null) {
                        float f = PlaybackController.this.mLargeBufferMode ? 1.0f : 0.15f;
                        float f2 = PlaybackController.this.mLargeBufferMode ? 3.5f : 0.5f;
                        if (PlaybackController.this.mAvgAudioQueue >= f) {
                            if (PlaybackController.this.mAvgAudioQueue > f2) {
                                long currentTimeMillis = System.currentTimeMillis();
                                if (currentTimeMillis > PlaybackController.this.mLatestQualityHint + PlaybackController.QUEUE_HINT_INTERVAL) {
                                    PlaybackController.this.mLatestQualityHint = currentTimeMillis;
                                    PlaybackController.this.mObserver.onQueueHigh();
                                }
                            }
                        } else {
                            long currentTimeMillis2 = System.currentTimeMillis();
                            if (currentTimeMillis2 > PlaybackController.this.mLatestQualityHint + PlaybackController.QUEUE_HINT_INTERVAL) {
                                PlaybackController.this.mLatestQualityHint = currentTimeMillis2;
                                PlaybackController.this.mObserver.onQueueLow();
                            }
                        }
                    }
                }
            }
        }
    };
    private final AtomicBoolean mStopDecoder = new AtomicBoolean(false);
    private final AtomicBoolean mDecoderOutputStopped = new AtomicBoolean(false);
    private final AtomicBoolean mDecoderInputStopped = new AtomicBoolean(false);
    private final LinkedList<Packet> mPacketQueue = new LinkedList<>();
    private final ByteBufferPool mPacketByteBufferPool = new ByteBufferPool();
    private boolean mLargeBufferMode = false;
    private float mAudioVolume = 1.0f;
    private final Timeline mTimeline = new Timeline();
    private final Object mRealtimeLock = new Object();
    private volatile int mBroadcasterUncertainty = -1;
    private boolean mUsingId3Pos = false;
    private float mAvgAudioQueue = 0.0f;
    private float mMinAudioQueue = -1.0f;
    private volatile boolean mEndOfData = false;
    private long mLatestQualityHint = System.currentTimeMillis();
    private Timer mAudioQualityTimer = new Timer("AudioQualityTimer");

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes.dex */
    public interface Observer {
        void onAudioBuffering();

        void onAudioOverflow();

        void onAudioPaused();

        void onAudioPlaying();

        void onDecoderFailure();

        void onPlaybackEnd();

        void onQueueHigh();

        void onQueueLow();

        void onResolutionChange(int i, int i2);
    }

    static /* synthetic */ int access$808(PlaybackController playbackController) {
        int i = playbackController.mOverflowCount;
        playbackController.mOverflowCount = i + 1;
        return i;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public PlaybackController(boolean z, Observer observer) {
        this.mRealtime = z;
        this.mObserver = observer;
        this.mAudioQualityTimer.schedule(this.mAudioQualityTask, AUDIO_QUALITY_TIMER_INTERVAL, AUDIO_QUALITY_TIMER_INTERVAL);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void setLargeBufferMode(boolean z) {
        this.mLargeBufferMode = z;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public synchronized void setSurface(Surface surface) {
        stopVideoDecoder();
        this.mSurface = surface;
        if (this.mVideoHeader != null && this.mSurface != null) {
            startVideoDecoder();
        }
    }

    @Override // com.bambuser.broadcaster.PacketHandler
    public synchronized void endOfData() {
        this.mEndOfData = true;
        if (this.mAudioQualityTimer != null) {
            this.mAudioQualityTimer.cancel();
        }
        this.mAudioQualityTimer = null;
        try {
            if (this.mAudioDecoder != null) {
                this.mAudioDecoder.flush(this.mAudioDecoderHandler);
            }
        } catch (Exception unused) {
        }
        if (this.mAudioTrackWriter != null) {
            this.mAudioTrackWriter.endOfData(new AudioTrackWriter.PlaybackEndListener() { // from class: com.bambuser.broadcaster.PlaybackController.1
                @Override // com.bambuser.broadcaster.AudioTrackWriter.PlaybackEndListener
                public void onPlaybackEnd() {
                    Observer observer = PlaybackController.this.mObserver;
                    if (observer != null) {
                        observer.onPlaybackEnd();
                    }
                }
            });
        } else if (this.mObserver != null) {
            this.mObserver.onPlaybackEnd();
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public int getAudioSessionId() {
        AudioTrackWriter audioTrackWriter;
        synchronized (this) {
            audioTrackWriter = this.mAudioTrackWriter;
        }
        if (audioTrackWriter != null) {
            return audioTrackWriter.getAudioSessionId();
        }
        return 0;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void setAudioVolume(float f) {
        this.mAudioVolume = f;
        synchronized (this) {
            if (this.mAudioTrackWriter != null) {
                this.mAudioTrackWriter.setAudioVolume(f);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public synchronized int getCurrentPosition() {
        int mediaTimeMillis = (int) (this.mTimeline.getMediaTimeMillis() + this.mPositionOffsetFromMediaTime);
        if (mediaTimeMillis >= 0) {
            return mediaTimeMillis;
        }
        return 0;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public synchronized int getDuration() {
        if (this.mRealtime || this.mDuration <= Utils.DOUBLE_EPSILON) {
            return -1;
        }
        return (int) (this.mDuration * 1000.0d);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public synchronized void flush() {
        this.mLatestQualityHint = System.currentTimeMillis();
        synchronized (this.mPacketQueue) {
            this.mPacketQueue.clear();
            this.mGotKeyframe = false;
        }
        stopVideoDecoder();
        if (this.mAudioDecoder != null) {
            this.mAudioDecoder.close();
        }
        this.mAudioDecoder = null;
        if (this.mAudioTrackWriter != null) {
            this.mAudioTrackWriter.flush();
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public synchronized void pause() {
        if (this.mAudioTrackWriter != null && this.mAudioTrackWriter.getPlaybackState() != AudioTrackWriter.PlaybackState.PAUSED) {
            this.mAudioTrackWriter.pause();
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public synchronized void start() {
        if (this.mAudioTrackWriter != null && this.mAudioTrackWriter.getPlaybackState() == AudioTrackWriter.PlaybackState.PAUSED) {
            this.mLatestQualityHint = System.currentTimeMillis();
            this.mAudioTrackWriter.unpause();
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean isEndOfData() {
        return this.mEndOfData;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public synchronized void prepareClose() {
        if (this.mAudioQualityTimer != null) {
            this.mAudioQualityTimer.cancel();
        }
        this.mAudioQualityTimer = null;
        if (this.mVideoDecoder != null) {
            this.mStopDecoder.set(true);
            synchronized (this.mTimeline) {
                this.mTimeline.notifyAll();
            }
            synchronized (this.mPacketQueue) {
                this.mPacketQueue.notifyAll();
            }
        }
        if (this.mAudioTrackWriter != null) {
            this.mAudioTrackWriter.prepareClose();
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public synchronized void close() {
        if (this.mAudioQualityTimer != null) {
            this.mAudioQualityTimer.cancel();
        }
        this.mAudioQualityTimer = null;
        stopVideoDecoder();
        this.mVideoHeader = null;
        if (this.mAudioDecoder != null) {
            this.mAudioDecoder.close();
        }
        this.mAudioDecoder = null;
        if (this.mAudioTrackWriter != null) {
            this.mAudioTrackWriter.close();
        }
        this.mAudioTrackWriter = null;
        this.mSurface = null;
        this.mPacketQueue.clear();
        this.mPacketByteBufferPool.clear();
        this.mObserver = null;
    }

    private synchronized void startVideoDecoder() {
        stopVideoDecoder();
        try {
            MediaFormat createVideoFormat = MediaFormat.createVideoFormat("video/avc", this.mVideoWidth, this.mVideoHeight);
            createVideoFormat.setByteBuffer("csd-0", this.mVideoHeader);
            this.mVideoDecoder = MediaCodec.createDecoderByType("video/avc");
            this.mVideoDecoder.configure(createVideoFormat, this.mSurface, (MediaCrypto) null, 0);
            this.mVideoDecoder.start();
            this.mVideoDecoderInput = this.mVideoDecoder.getInputBuffers();
            this.mGotKeyframe = false;
            this.mStopDecoder.set(false);
            this.mDecoderInputStopped.set(false);
            this.mDecoderOutputStopped.set(false);
            Thread thread = new Thread("VideoDecoderInput") { // from class: com.bambuser.broadcaster.PlaybackController.2
                @Override // java.lang.Thread, java.lang.Runnable
                public void run() {
                    PlaybackController.this.handleInput();
                }
            };
            Thread thread2 = new Thread("VideoDecoderOutput") { // from class: com.bambuser.broadcaster.PlaybackController.3
                @Override // java.lang.Thread, java.lang.Runnable
                public void run() {
                    PlaybackController.this.handleOutput();
                }
            };
            thread.start();
            thread2.start();
        } catch (Exception e) {
            if (!sVideoDecoderFailed) {
                sVideoDecoderFailed = true;
                SentryLogger.asyncMessage("PlaybackController startVideoDecoder exception", SentryLogger.Level.ERROR, null, e);
            }
            this.mStopDecoder.set(true);
            synchronized (this) {
                if (this.mObserver != null) {
                    this.mObserver.onDecoderFailure();
                }
                Log.e(LOGTAG, "Exception in startVideoDecoder: " + e);
            }
        }
    }

    /* JADX WARN: Type inference failed for: r1v1, types: [com.bambuser.broadcaster.PlaybackController$4] */
    private synchronized void stopVideoDecoder() {
        if (this.mVideoDecoder != null) {
            try {
                this.mStopDecoder.set(true);
                synchronized (this.mPacketQueue) {
                    this.mPacketQueue.notifyAll();
                }
                while (!this.mDecoderInputStopped.get()) {
                    wait();
                }
                while (!this.mDecoderOutputStopped.get()) {
                    wait();
                }
            } catch (InterruptedException unused) {
            }
            try {
                this.mVideoDecoder.stop();
            } catch (Exception unused2) {
            }
            final MediaCodec mediaCodec = this.mVideoDecoder;
            this.mVideoDecoder = null;
            this.mVideoDecoderInput = null;
            new Thread("CodecRelease") { // from class: com.bambuser.broadcaster.PlaybackController.4
                @Override // java.lang.Thread, java.lang.Runnable
                public void run() {
                    mediaCodec.release();
                }
            }.start();
        }
    }

    private ByteBuffer cloneBuffer(ByteBuffer byteBuffer) {
        ByteBuffer order = this.mPacketByteBufferPool.getBuffer(byteBuffer.remaining()).order(ByteOrder.BIG_ENDIAN);
        order.put(byteBuffer);
        order.flip();
        return order;
    }

    private ByteBuffer convertH264Header(ByteBuffer byteBuffer) {
        this.mDoConvertH264 = false;
        if (byteBuffer.remaining() < 11) {
            return cloneBuffer(byteBuffer);
        }
        if (byteBuffer.getInt(byteBuffer.position()) == 1) {
            return cloneBuffer(byteBuffer);
        }
        if (byteBuffer.get(byteBuffer.position()) != 1) {
            return cloneBuffer(byteBuffer);
        }
        short s = byteBuffer.getShort(byteBuffer.position() + 6);
        int i = s + 8;
        if (byteBuffer.remaining() < i || s < 0) {
            return cloneBuffer(byteBuffer);
        }
        short s2 = byteBuffer.getShort(byteBuffer.position() + 8 + s + 1);
        if (byteBuffer.remaining() < i + 3 + s2 || s2 < 0) {
            return cloneBuffer(byteBuffer);
        }
        this.mDoConvertH264 = true;
        ByteBuffer order = ByteBuffer.allocate(s + 4 + 4 + s2).order(ByteOrder.BIG_ENDIAN);
        order.putInt(1);
        order.put(byteBuffer.array(), byteBuffer.arrayOffset() + byteBuffer.position() + 8, s);
        order.putInt(1);
        order.put(byteBuffer.array(), byteBuffer.arrayOffset() + byteBuffer.position() + 8 + s + 3, s2);
        order.flip();
        return order;
    }

    private ByteBuffer convertH264Frame(ByteBuffer byteBuffer) {
        int i;
        if (!this.mDoConvertH264) {
            return cloneBuffer(byteBuffer);
        }
        ByteBuffer order = this.mPacketByteBufferPool.getBuffer(byteBuffer.remaining()).order(ByteOrder.BIG_ENDIAN);
        ByteBuffer slice = byteBuffer.slice();
        while (slice.remaining() > 4 && slice.remaining() >= (i = slice.getInt()) && i >= 0 && order.remaining() >= i + 4) {
            order.putInt(1);
            order.put(slice.array(), slice.arrayOffset() + slice.position(), i);
            slice.position(slice.position() + i);
        }
        order.flip();
        return order;
    }

    @Override // com.bambuser.broadcaster.PacketHandler
    public synchronized void onStreamParsePosition(double d, int i) {
        if (this.mUsingId3Pos) {
            return;
        }
        this.mPositionOffsetFromMediaTime = ((long) (d * 1000.0d)) - i;
    }

    @Override // com.bambuser.broadcaster.PacketHandler
    public synchronized void onStreamDuration(double d) {
        this.mDuration = d;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void setTentativePosition(long j) {
        if (this.mTimeline.isStarted()) {
            return;
        }
        this.mTimeline.setTime(System.nanoTime(), j, false);
    }

    @Override // com.bambuser.broadcaster.PacketHandler
    public synchronized void onVideoHeader(ByteBuffer byteBuffer, int i, int i2, int i3, int i4) {
        if (this.mEndOfData) {
            return;
        }
        ByteBuffer convertH264Header = convertH264Header(byteBuffer);
        if (this.mVideoDecoder != null) {
            HeaderPacket headerPacket = new HeaderPacket();
            headerPacket.mBuffer = convertH264Header;
            headerPacket.mWidth = i3;
            headerPacket.mHeight = i4;
            synchronized (this.mPacketQueue) {
                this.mPacketQueue.add(headerPacket);
                this.mPacketQueue.notifyAll();
            }
        } else {
            synchronized (this.mPacketQueue) {
                this.mPacketQueue.clear();
                this.mGotKeyframe = false;
            }
            applyVideoHeader(convertH264Header, i3, i4);
        }
    }

    /* JADX WARN: Removed duplicated region for block: B:16:0x0025 A[Catch: all -> 0x0036, TryCatch #0 {, blocks: (B:3:0x0001, B:5:0x000a, B:10:0x0012, B:13:0x0018, B:14:0x001d, B:16:0x0025, B:18:0x002a), top: B:24:0x0001 }] */
    /* JADX WARN: Removed duplicated region for block: B:18:0x002a A[Catch: all -> 0x0036, TRY_LEAVE, TryCatch #0 {, blocks: (B:3:0x0001, B:5:0x000a, B:10:0x0012, B:13:0x0018, B:14:0x001d, B:16:0x0025, B:18:0x002a), top: B:24:0x0001 }] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    private synchronized void applyVideoHeader(java.nio.ByteBuffer r2, int r3, int r4) {
        /*
            r1 = this;
            monitor-enter(r1)
            r1.mVideoHeader = r2     // Catch: java.lang.Throwable -> L36
            r1.stopVideoDecoder()     // Catch: java.lang.Throwable -> L36
            int r2 = r1.mVideoWidth     // Catch: java.lang.Throwable -> L36
            if (r3 != r2) goto L11
            int r2 = r1.mVideoHeight     // Catch: java.lang.Throwable -> L36
            if (r4 == r2) goto Lf
            goto L11
        Lf:
            r2 = 0
            goto L12
        L11:
            r2 = 1
        L12:
            com.bambuser.broadcaster.PlaybackController$Observer r0 = r1.mObserver     // Catch: java.lang.Throwable -> L36
            if (r0 == 0) goto L1d
            if (r2 == 0) goto L1d
            com.bambuser.broadcaster.PlaybackController$Observer r0 = r1.mObserver     // Catch: java.lang.Throwable -> L36
            r0.onResolutionChange(r3, r4)     // Catch: java.lang.Throwable -> L36
        L1d:
            r1.mVideoWidth = r3     // Catch: java.lang.Throwable -> L36
            r1.mVideoHeight = r4     // Catch: java.lang.Throwable -> L36
            android.view.Surface r3 = r1.mSurface     // Catch: java.lang.Throwable -> L36
            if (r3 == 0) goto L28
            r1.startVideoDecoder()     // Catch: java.lang.Throwable -> L36
        L28:
            if (r2 == 0) goto L34
            r2 = -1082130432(0xffffffffbf800000, float:-1.0)
            r1.mMinAudioQueue = r2     // Catch: java.lang.Throwable -> L36
            long r2 = android.os.SystemClock.elapsedRealtime()     // Catch: java.lang.Throwable -> L36
            r1.mLatestQueueEvalTime = r2     // Catch: java.lang.Throwable -> L36
        L34:
            monitor-exit(r1)
            return
        L36:
            r2 = move-exception
            monitor-exit(r1)
            throw r2
        */
        throw new UnsupportedOperationException("Method not decompiled: com.bambuser.broadcaster.PlaybackController.applyVideoHeader(java.nio.ByteBuffer, int, int):void");
    }

    @Override // com.bambuser.broadcaster.PacketHandler
    public void onVideoFrame(ByteBuffer byteBuffer, int i, int i2, boolean z) {
        if (this.mEndOfData) {
            return;
        }
        Packet packet = new Packet();
        packet.mBuffer = convertH264Frame(byteBuffer);
        packet.mPts = i2;
        packet.mKeyframe = z;
        synchronized (this.mPacketQueue) {
            int i3 = this.mLargeBufferMode ? 8000 : 5000;
            int i4 = (i3 / 1000) * 30;
            while (this.mPacketQueue.size() > i4 && this.mPacketQueue.peekLast().mPts - this.mPacketQueue.peekFirst().mPts > i3 && !(this.mPacketQueue.peekFirst() instanceof HeaderPacket)) {
                this.mPacketByteBufferPool.add(this.mPacketQueue.removeFirst().mBuffer);
                this.mGotKeyframe = false;
            }
            this.mPacketQueue.add(packet);
            this.mPacketQueue.notifyAll();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* JADX WARN: Code restructure failed: missing block: B:30:0x0054, code lost:
        r0 = -1;
     */
    /* JADX WARN: Code restructure failed: missing block: B:31:0x0055, code lost:
        r5 = r0;
     */
    /* JADX WARN: Removed duplicated region for block: B:93:0x012e A[EXC_TOP_SPLITTER, SYNTHETIC] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public void handleInput() {
        /*
            Method dump skipped, instructions count: 334
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: com.bambuser.broadcaster.PlaybackController.handleInput():void");
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void handleOutput() {
        int i;
        boolean z;
        boolean z2;
        Packet next;
        boolean z3;
        MediaCodec.BufferInfo bufferInfo = new MediaCodec.BufferInfo();
        long j = 0;
        long j2 = 0;
        while (!this.mStopDecoder.get()) {
            try {
                i = this.mVideoDecoder.dequeueOutputBuffer(bufferInfo, DECODER_DEQUEUE_WAIT_MICROS);
            } catch (Exception e) {
                Log.e(LOGTAG, "Video decoder exception: " + e);
                if (!sVideoDecoderFailed) {
                    sVideoDecoderFailed = true;
                    SentryLogger.asyncMessage("PlaybackController video decoder exception", SentryLogger.Level.ERROR, null, e);
                }
                this.mStopDecoder.set(true);
                synchronized (this) {
                    if (this.mObserver != null) {
                        this.mObserver.onDecoderFailure();
                    }
                    i = -1;
                }
            }
            if (i >= 0) {
                long waitUntilMediaTime = this.mTimeline.waitUntilMediaTime(bufferInfo.presentationTimeUs / 1000, this.mStopDecoder);
                long nanoTime = System.nanoTime();
                if (waitUntilMediaTime < j) {
                    z = false;
                } else if (waitUntilMediaTime > 100) {
                    boolean z4 = nanoTime - j2 > 100000000;
                    if (waitUntilMediaTime > 200) {
                        synchronized (this.mPacketQueue) {
                            Iterator<Packet> it = this.mPacketQueue.iterator();
                            while (true) {
                                if (!it.hasNext()) {
                                    z2 = z4;
                                    break;
                                }
                                if (it.next().mKeyframe) {
                                    z2 = z4;
                                    if (waitUntilMediaTime > next.mPts - (bufferInfo.presentationTimeUs / 1000)) {
                                        this.mGotKeyframe = false;
                                    }
                                }
                            }
                        }
                    } else {
                        z2 = z4;
                    }
                    z = z2;
                } else {
                    z = true;
                }
                try {
                    this.mVideoDecoder.releaseOutputBuffer(i, z);
                } catch (Exception e2) {
                    Log.e(LOGTAG, "Video decoder exception: " + e2);
                    if (sVideoDecoderFailed) {
                        z3 = true;
                    } else {
                        z3 = true;
                        sVideoDecoderFailed = true;
                        SentryLogger.asyncMessage("PlaybackController video decoder exception", SentryLogger.Level.ERROR, null, e2);
                    }
                    this.mStopDecoder.set(z3);
                    synchronized (this) {
                        if (this.mObserver != null) {
                            this.mObserver.onDecoderFailure();
                        }
                    }
                }
                if (z) {
                    j2 = nanoTime;
                }
            }
            j = 0;
        }
        synchronized (this) {
            this.mDecoderOutputStopped.set(true);
            notifyAll();
        }
    }

    @Override // com.bambuser.broadcaster.PacketHandler
    public synchronized void onAudioHeader(ByteBuffer byteBuffer, int i, int i2, int i3) {
        if (this.mEndOfData) {
            return;
        }
        if (this.mAudioDecoder != null) {
            this.mAudioDecoder.close();
        }
        this.mAudioDecoder = null;
        try {
            this.mAudioDecoder = new MediaCodecAudioWrapper();
            this.mAudioDecoder.initDecoder("audio/mp4a-latm", i2, i3, byteBuffer, false);
        } catch (Exception e) {
            if (this.mAudioDecoder != null) {
                this.mAudioDecoder.close();
            }
            this.mAudioDecoder = null;
            Log.w(LOGTAG, "Exception while decoding audio: " + e);
            if (!sAudioDecoderFailed) {
                sAudioDecoderFailed = true;
                SentryLogger.asyncMessage("PlaybackController audio decoder exception", SentryLogger.Level.ERROR, null, e);
            }
            if (this.mObserver != null) {
                this.mObserver.onDecoderFailure();
            }
        }
    }

    @Override // com.bambuser.broadcaster.PacketHandler
    public void onAudioFrame(ByteBuffer byteBuffer, int i) {
        MediaCodecAudioWrapper mediaCodecAudioWrapper = this.mAudioDecoder;
        if (mediaCodecAudioWrapper == null || this.mEndOfData) {
            return;
        }
        try {
            mediaCodecAudioWrapper.decode(byteBuffer.array(), byteBuffer.position() + byteBuffer.arrayOffset(), byteBuffer.remaining(), i, this.mAudioDecoderHandler);
            mediaCodecAudioWrapper.flush(this.mAudioDecoderHandler);
        } catch (Exception e) {
            Log.w(LOGTAG, "Exception while decoding audio: " + e);
            if (!sAudioDecoderFailed) {
                sAudioDecoderFailed = true;
                SentryLogger.asyncMessage("PlaybackController audio decoder exception", SentryLogger.Level.ERROR, null, e);
            }
            synchronized (this) {
                if (this.mObserver != null) {
                    this.mObserver.onDecoderFailure();
                }
            }
        }
    }

    @Override // com.bambuser.broadcaster.PacketHandler
    public void onRealtimePacket(long j, int i, int i2) {
        synchronized (this.mRealtimeLock) {
            this.mAnchorMediaTime = i2;
            this.mBroadcasterRealtime = j;
            this.mBroadcasterUncertainty = i;
        }
    }

    @Override // com.bambuser.broadcaster.PacketHandler
    public void onId3String(String str, int i) {
        try {
            double optDouble = new JSONObject(str).optDouble("pos");
            if (Double.isInfinite(optDouble) || Double.isNaN(optDouble)) {
                return;
            }
            synchronized (this) {
                this.mPositionOffsetFromMediaTime = ((long) (optDouble * 1000.0d)) - i;
                this.mUsingId3Pos = true;
            }
        } catch (Exception e) {
            Log.w(LOGTAG, "Exception when parsing incoming id3 metadata " + e);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public int getLatency(long j) {
        long j2;
        long j3;
        long mediaTimeMillis = this.mTimeline.getMediaTimeMillis();
        synchronized (this.mRealtimeLock) {
            j2 = this.mAnchorMediaTime;
            j3 = this.mBroadcasterRealtime;
        }
        return (int) (j - (j3 + (mediaTimeMillis - j2)));
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public int getBroadcasterUncertainty() {
        if (getCurrentPosition() <= 0) {
            return -1;
        }
        return this.mBroadcasterUncertainty;
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes.dex */
    public static class Packet {
        ByteBuffer mBuffer;
        boolean mKeyframe;
        int mPts;

        private Packet() {
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes.dex */
    public static class HeaderPacket extends Packet {
        int mHeight;
        int mWidth;

        private HeaderPacket() {
            super();
        }
    }
}
