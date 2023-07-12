package com.bambuser.broadcaster;

import android.media.AudioTrack;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import java.nio.ByteBuffer;
import java.util.LinkedList;

/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: classes.dex */
public final class AudioTrackWriter extends Thread {
    private static final String LOGTAG = "AudioTrackWriter";
    private final long mAudioSystemLatencyNanos;
    private final ByteBufferPool mByteBufferPool;
    private long mBytesWritten;
    private volatile boolean mEndOfData;
    private boolean mFlushWhilePaused;
    private volatile int mHighWater;
    private final int mLowWater;
    private final Handler mMainHandler;
    private final Runnable mOverflowCallback;
    private volatile PlaybackEndListener mPlaybackEndListener;
    private volatile PlaybackState mPlaybackState;
    private volatile PlaybackStateListener mPlaybackStateListener;
    private final LinkedList<ByteBuffer> mQueue;
    private int mQueueSize;
    private final boolean mRealtime;
    private volatile boolean mRun;
    private volatile boolean mRunning;
    private final Runnable mSetStateBuffering;
    private final Runnable mSetStatePlaying;
    private final Timeline mTimeline;
    private final LinkedList<Long> mTimestampQueue;
    private final AudioTrack mTrack;

    /* loaded from: classes.dex */
    interface PlaybackEndListener {
        void onPlaybackEnd();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes.dex */
    public enum PlaybackState {
        BUFFERING,
        PLAYING,
        PAUSED
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes.dex */
    public interface PlaybackStateListener {
        void onOverflow();

        void onStateChanged(PlaybackState playbackState);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public AudioTrackWriter(int i, int i2, boolean z, Timeline timeline, int i3, int i4) {
        super(LOGTAG);
        this.mSetStateBuffering = new Runnable() { // from class: com.bambuser.broadcaster.AudioTrackWriter.2
            @Override // java.lang.Runnable
            public void run() {
                if (AudioTrackWriter.this.mTimeline != null) {
                    AudioTrackWriter.this.mTimeline.setStarted(false);
                }
                if (AudioTrackWriter.this.mPlaybackState != PlaybackState.PAUSED) {
                    AudioTrackWriter.this.setPlaybackState(PlaybackState.BUFFERING);
                }
            }
        };
        this.mSetStatePlaying = new Runnable() { // from class: com.bambuser.broadcaster.AudioTrackWriter.3
            @Override // java.lang.Runnable
            public void run() {
                AudioTrackWriter.this.setPlaybackState(PlaybackState.PLAYING);
            }
        };
        this.mOverflowCallback = new Runnable() { // from class: com.bambuser.broadcaster.AudioTrackWriter.4
            @Override // java.lang.Runnable
            public void run() {
                PlaybackStateListener playbackStateListener = AudioTrackWriter.this.mPlaybackStateListener;
                if (playbackStateListener != null) {
                    playbackStateListener.onOverflow();
                }
            }
        };
        this.mMainHandler = new Handler(Looper.getMainLooper());
        this.mByteBufferPool = new ByteBufferPool();
        this.mQueue = new LinkedList<>();
        this.mTimestampQueue = new LinkedList<>();
        int i5 = 0;
        this.mFlushWhilePaused = false;
        this.mPlaybackState = PlaybackState.BUFFERING;
        int i6 = i * i2 * 2;
        this.mLowWater = (i6 * i3) / 1000;
        this.mHighWater = (i6 * i4) / 1000;
        int i7 = i2 == 2 ? 12 : 4;
        int minBufferSize = AudioTrack.getMinBufferSize(i, i7, 2);
        int i8 = (i6 * 100) / 1000;
        int i9 = i8 > minBufferSize ? i8 : minBufferSize;
        this.mTrack = new AudioTrack(3, i, i7, 2, i9, 1);
        this.mTrack.play();
        Integer num = (Integer) Compat.tryCall(this.mTrack, "getLatency");
        if (num != null) {
            i5 = Math.max(0, num.intValue() - ((i9 * 1000) / i6));
            Log.i(LOGTAG, "Audio system latency: " + i5);
        }
        this.mAudioSystemLatencyNanos = i5 * 1000000;
        this.mRun = true;
        this.mRunning = true;
        this.mRealtime = z;
        this.mTimeline = timeline;
        start();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void setPlaybackStateListener(PlaybackStateListener playbackStateListener) {
        this.mPlaybackStateListener = playbackStateListener;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public PlaybackState getPlaybackState() {
        return this.mPlaybackState;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void setPlaybackState(PlaybackState playbackState) {
        if (this.mPlaybackState == playbackState) {
            return;
        }
        this.mPlaybackState = playbackState;
        PlaybackStateListener playbackStateListener = this.mPlaybackStateListener;
        if (playbackStateListener != null) {
            playbackStateListener.onStateChanged(playbackState);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public synchronized void endOfData(PlaybackEndListener playbackEndListener) {
        this.mPlaybackEndListener = playbackEndListener;
        this.mEndOfData = true;
        notifyAll();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void prepareClose() {
        this.mPlaybackEndListener = null;
        this.mPlaybackStateListener = null;
        synchronized (this) {
            this.mRun = false;
            notifyAll();
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void close() {
        this.mMainHandler.removeCallbacks(this.mSetStateBuffering);
        this.mPlaybackEndListener = null;
        this.mPlaybackStateListener = null;
        synchronized (this) {
            this.mRun = false;
            notifyAll();
        }
        while (this.mRunning) {
            try {
                join();
            } catch (InterruptedException unused) {
            }
        }
        this.mTrack.stop();
        this.mTrack.release();
        this.mByteBufferPool.clear();
        this.mQueue.clear();
        this.mTimestampQueue.clear();
        this.mQueueSize = 0;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public synchronized void flush() {
        this.mQueue.clear();
        this.mTimestampQueue.clear();
        this.mQueueSize = 0;
        this.mEndOfData = false;
        this.mPlaybackEndListener = null;
        this.mTrack.pause();
        this.mTrack.flush();
        if (this.mPlaybackState != PlaybackState.PAUSED) {
            this.mTrack.play();
            this.mBytesWritten = (this.mTrack.getPlaybackHeadPosition() & Movino.ONES_32) * 2 * getChannelCount();
        } else {
            this.mFlushWhilePaused = true;
        }
        notifyAll();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public synchronized void pause() {
        if (this.mPlaybackState == PlaybackState.PAUSED) {
            return;
        }
        setPlaybackState(PlaybackState.PAUSED);
        this.mTrack.pause();
        if (this.mTimeline != null) {
            this.mTimeline.setStarted(false);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public synchronized void unpause() {
        if (this.mPlaybackState != PlaybackState.PAUSED) {
            return;
        }
        setPlaybackState(PlaybackState.PLAYING);
        this.mTrack.play();
        if (this.mFlushWhilePaused) {
            this.mBytesWritten = (this.mTrack.getPlaybackHeadPosition() & Movino.ONES_32) * 2 * getChannelCount();
        }
        this.mFlushWhilePaused = false;
        notifyAll();
    }

    /* JADX WARN: Code restructure failed: missing block: B:55:0x00b0, code lost:
        r14.mMainHandler.removeCallbacks(r14.mSetStateBuffering);
     */
    /* JADX WARN: Code restructure failed: missing block: B:56:0x00bb, code lost:
        if (r14.mPlaybackState != com.bambuser.broadcaster.AudioTrackWriter.PlaybackState.BUFFERING) goto L62;
     */
    /* JADX WARN: Code restructure failed: missing block: B:57:0x00bd, code lost:
        r14.mMainHandler.post(r14.mSetStatePlaying);
     */
    /* JADX WARN: Code restructure failed: missing block: B:58:0x00c4, code lost:
        r2 = r14.mTrack.write(r4.array(), r4.arrayOffset() + r4.position(), r4.remaining());
     */
    /* JADX WARN: Code restructure failed: missing block: B:59:0x00de, code lost:
        if (r2 >= 0) goto L65;
     */
    /* JADX WARN: Code restructure failed: missing block: B:60:0x00e0, code lost:
        r2 = 0;
     */
    /* JADX WARN: Code restructure failed: missing block: B:62:0x00e3, code lost:
        if (r14.mTimeline == null) goto L72;
     */
    /* JADX WARN: Code restructure failed: missing block: B:63:0x00e5, code lost:
        r6 = r14.mTimeline;
        r10 = (java.lang.System.nanoTime() + (getTrackQueueDuration() * 1.0E9f)) + r14.mAudioSystemLatencyNanos;
     */
    /* JADX WARN: Code restructure failed: missing block: B:64:0x00fc, code lost:
        if (r14.mPlaybackState == com.bambuser.broadcaster.AudioTrackWriter.PlaybackState.PAUSED) goto L71;
     */
    /* JADX WARN: Code restructure failed: missing block: B:65:0x00fe, code lost:
        r12 = true;
     */
    /* JADX WARN: Code restructure failed: missing block: B:66:0x0100, code lost:
        r12 = false;
     */
    /* JADX WARN: Code restructure failed: missing block: B:67:0x0101, code lost:
        r6.setTime(r10, r8, r12);
     */
    /* JADX WARN: Code restructure failed: missing block: B:68:0x0107, code lost:
        r14.mByteBufferPool.add(r4);
     */
    @Override // java.lang.Thread, java.lang.Runnable
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public void run() {
        /*
            Method dump skipped, instructions count: 305
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: com.bambuser.broadcaster.AudioTrackWriter.run():void");
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public synchronized void enqueue(ByteBuffer byteBuffer) {
        enqueue(byteBuffer, 0L);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public synchronized void enqueue(ByteBuffer byteBuffer, long j) {
        this.mQueueSize += byteBuffer.remaining();
        this.mQueue.add(byteBuffer);
        if (this.mTimeline != null) {
            this.mTimestampQueue.add(Long.valueOf(j));
        }
        notifyAll();
        while (this.mRun && this.mQueueSize > this.mHighWater) {
            if (this.mRealtime) {
                long j2 = (this.mHighWater + this.mLowWater) / 2;
                long j3 = 0;
                while (this.mQueueSize > j2) {
                    ByteBuffer remove = this.mQueue.remove();
                    if (this.mTimeline != null) {
                        this.mTimestampQueue.remove();
                    }
                    this.mQueueSize -= remove.remaining();
                    j3 += remove.remaining();
                    this.mByteBufferPool.add(remove);
                }
                Log.d(LOGTAG, "dropped " + j3);
                this.mMainHandler.post(this.mOverflowCallback);
            } else {
                try {
                    wait();
                } catch (InterruptedException unused) {
                }
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public int getAudioSessionId() {
        return this.mTrack.getAudioSessionId();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void setAudioVolume(float f) {
        this.mTrack.setStereoVolume(f, f);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public synchronized float getQueueDuration(boolean z) {
        int channelCount;
        long playbackHeadPosition;
        long j;
        channelCount = getChannelCount() * 2;
        playbackHeadPosition = this.mTrack.getPlaybackHeadPosition() & Movino.ONES_32;
        j = this.mBytesWritten;
        if (!z) {
            j += this.mQueueSize;
        }
        return ((float) ((j / channelCount) - playbackHeadPosition)) / getSampleRate();
    }

    private float getTrackQueueDuration() {
        return getQueueDuration(true);
    }

    private float getFullQueueDuration() {
        return getQueueDuration(false);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public int getSampleRate() {
        return this.mTrack.getSampleRate();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public int getChannelCount() {
        return this.mTrack.getChannelCount();
    }

    private int getByteRate() {
        return getSampleRate() * getChannelCount() * 2;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public int getHighWater() {
        return (this.mHighWater * 1000) / getByteRate();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void setHighWater(int i) {
        this.mHighWater = (i * getByteRate()) / 1000;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public ByteBuffer getBuffer(int i) {
        return this.mByteBufferPool.getBuffer(i);
    }
}
