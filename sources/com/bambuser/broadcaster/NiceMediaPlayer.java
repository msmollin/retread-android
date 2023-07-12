package com.bambuser.broadcaster;

import android.media.MediaPlayer;
import android.util.Log;
import android.view.Surface;
import com.bambuser.broadcaster.SentryLogger;
import org.json.JSONObject;

/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: classes.dex */
public final class NiceMediaPlayer {
    private static final String LOGTAG = "NiceMediaPlayer";
    private MediaPlayer mMediaPlayer;
    private Observer mObserver;
    private final MediaPlayer.OnErrorListener mMediaPlayerErrorListener = new MediaPlayer.OnErrorListener() { // from class: com.bambuser.broadcaster.NiceMediaPlayer.1
        @Override // android.media.MediaPlayer.OnErrorListener
        public boolean onError(MediaPlayer mediaPlayer, int i, int i2) {
            if (NiceMediaPlayer.this.mMediaPlayer == null || mediaPlayer != NiceMediaPlayer.this.mMediaPlayer) {
                return true;
            }
            JSONObject jSONObject = new JSONObject();
            try {
                jSONObject.put("wrapper_state", NiceMediaPlayer.this.mState);
                jSONObject.put("error_what", i);
                jSONObject.put("error_extra", i2);
            } catch (Exception unused) {
            }
            SentryLogger.asyncMessage("Android MediaPlayer onError", SentryLogger.Level.ERROR, jSONObject, null);
            Log.w(NiceMediaPlayer.LOGTAG, "Android MediaPlayer onError what: " + i + ", extra: " + i2);
            NiceMediaPlayer.this.mMediaPlayer.release();
            NiceMediaPlayer.this.mMediaPlayer = null;
            NiceMediaPlayer.this.setState(State.ERROR);
            return true;
        }
    };
    private final MediaPlayer.OnCompletionListener mMediaPlayerCompletionListener = new MediaPlayer.OnCompletionListener() { // from class: com.bambuser.broadcaster.NiceMediaPlayer.2
        @Override // android.media.MediaPlayer.OnCompletionListener
        public void onCompletion(MediaPlayer mediaPlayer) {
            if (NiceMediaPlayer.this.mMediaPlayer == null || mediaPlayer != NiceMediaPlayer.this.mMediaPlayer) {
                return;
            }
            NiceMediaPlayer.this.setState(State.PLAYBACK_COMPLETED);
        }
    };
    private final MediaPlayer.OnPreparedListener mMediaPlayerPreparedListener = new MediaPlayer.OnPreparedListener() { // from class: com.bambuser.broadcaster.NiceMediaPlayer.3
        @Override // android.media.MediaPlayer.OnPreparedListener
        public void onPrepared(MediaPlayer mediaPlayer) {
            if (NiceMediaPlayer.this.mMediaPlayer == null || mediaPlayer != NiceMediaPlayer.this.mMediaPlayer) {
                return;
            }
            NiceMediaPlayer.this.setState(State.PREPARED);
            NiceMediaPlayer.this.start();
        }
    };
    private final MediaPlayer.OnInfoListener mMediaPlayerInfoListener = new MediaPlayer.OnInfoListener() { // from class: com.bambuser.broadcaster.NiceMediaPlayer.4
        @Override // android.media.MediaPlayer.OnInfoListener
        public boolean onInfo(MediaPlayer mediaPlayer, int i, int i2) {
            if (NiceMediaPlayer.this.mMediaPlayer == null || mediaPlayer != NiceMediaPlayer.this.mMediaPlayer) {
                return false;
            }
            if (i == 701) {
                if (NiceMediaPlayer.this.mState == State.STARTED) {
                    NiceMediaPlayer.this.setState(State.BUFFERING);
                    return false;
                }
                return false;
            } else if (i == 702 && NiceMediaPlayer.this.mState == State.BUFFERING) {
                NiceMediaPlayer.this.setState(State.STARTED);
                return false;
            } else {
                return false;
            }
        }
    };
    private final MediaPlayer.OnVideoSizeChangedListener mMediaPlayerVideoSizeListener = new MediaPlayer.OnVideoSizeChangedListener() { // from class: com.bambuser.broadcaster.NiceMediaPlayer.5
        @Override // android.media.MediaPlayer.OnVideoSizeChangedListener
        public void onVideoSizeChanged(MediaPlayer mediaPlayer, int i, int i2) {
            if (NiceMediaPlayer.this.mMediaPlayer == null || mediaPlayer != NiceMediaPlayer.this.mMediaPlayer || NiceMediaPlayer.this.mObserver == null) {
                return;
            }
            NiceMediaPlayer.this.mObserver.onResolutionChange(i, i2);
        }
    };
    private final MediaPlayer.OnBufferingUpdateListener mMediaPlayerBufferingListener = new MediaPlayer.OnBufferingUpdateListener() { // from class: com.bambuser.broadcaster.NiceMediaPlayer.6
        @Override // android.media.MediaPlayer.OnBufferingUpdateListener
        public void onBufferingUpdate(MediaPlayer mediaPlayer, int i) {
            if (NiceMediaPlayer.this.mMediaPlayer == null || mediaPlayer != NiceMediaPlayer.this.mMediaPlayer) {
                return;
            }
            NiceMediaPlayer.this.mBufferPercent = i;
        }
    };
    private int mBufferPercent = 0;
    private State mState = State.IDLE;

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes.dex */
    public interface Observer {
        void onResolutionChange(int i, int i2);

        void onStateChange(State state);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes.dex */
    public enum State {
        IDLE,
        PREPARING,
        PREPARED,
        STARTED,
        BUFFERING,
        PAUSED,
        PLAYBACK_COMPLETED,
        ERROR
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void setObserver(Observer observer) {
        this.mObserver = observer;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void init(Surface surface, String str) {
        if (this.mMediaPlayer != null) {
            this.mMediaPlayer.release();
        }
        this.mMediaPlayer = null;
        this.mBufferPercent = 0;
        setState(State.IDLE);
        this.mMediaPlayer = new MediaPlayer();
        this.mMediaPlayer.setOnErrorListener(this.mMediaPlayerErrorListener);
        this.mMediaPlayer.setOnInfoListener(this.mMediaPlayerInfoListener);
        this.mMediaPlayer.setOnCompletionListener(this.mMediaPlayerCompletionListener);
        this.mMediaPlayer.setOnPreparedListener(this.mMediaPlayerPreparedListener);
        this.mMediaPlayer.setOnVideoSizeChangedListener(this.mMediaPlayerVideoSizeListener);
        this.mMediaPlayer.setOnBufferingUpdateListener(this.mMediaPlayerBufferingListener);
        try {
            this.mMediaPlayer.setDataSource(str);
            this.mMediaPlayer.setSurface(surface);
            setState(State.PREPARING);
            this.mMediaPlayer.prepareAsync();
        } catch (Exception e) {
            Log.w(LOGTAG, "Android MediaPlayer exception: " + e);
            this.mMediaPlayer.release();
            this.mMediaPlayer = null;
            setState(State.ERROR);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void setAudioVolume(float f) {
        if (this.mMediaPlayer != null) {
            this.mMediaPlayer.setVolume(f, f);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void release() {
        if (this.mMediaPlayer != null) {
            this.mMediaPlayer.release();
        }
        this.mMediaPlayer = null;
        this.mBufferPercent = 0;
        this.mObserver = null;
        setState(State.IDLE);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void setSurface(Surface surface) {
        if (this.mMediaPlayer != null) {
            this.mMediaPlayer.setSurface(surface);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public int getDuration() {
        if (this.mMediaPlayer == null || this.mState == State.IDLE || this.mState == State.ERROR || this.mState == State.PREPARING) {
            return -1;
        }
        return this.mMediaPlayer.getDuration();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void seekTo(int i) {
        if (i < 0 || this.mMediaPlayer == null || i >= getDuration()) {
            return;
        }
        this.mMediaPlayer.seekTo(i);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public int getCurrentPosition() {
        if (this.mMediaPlayer == null || this.mState == State.IDLE || this.mState == State.ERROR || this.mState == State.PREPARING) {
            return 0;
        }
        return this.mMediaPlayer.getCurrentPosition();
    }

    int getVideoWidth() {
        if (this.mMediaPlayer == null || this.mState == State.ERROR) {
            return 0;
        }
        return this.mMediaPlayer.getVideoWidth();
    }

    int getVideoHeight() {
        if (this.mMediaPlayer == null || this.mState == State.ERROR) {
            return 0;
        }
        return this.mMediaPlayer.getVideoHeight();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public int getBufferPercentage() {
        return this.mBufferPercent;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void start() {
        if (this.mMediaPlayer != null) {
            if (this.mState == State.PREPARED || this.mState == State.BUFFERING || this.mState == State.PAUSED || this.mState == State.PLAYBACK_COMPLETED) {
                this.mMediaPlayer.start();
                setState(State.STARTED);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void pause() {
        if (this.mMediaPlayer != null) {
            if (this.mState == State.STARTED || this.mState == State.BUFFERING) {
                this.mMediaPlayer.pause();
                setState(State.PAUSED);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public int getAudioSessionId() {
        if (this.mMediaPlayer != null) {
            return this.mMediaPlayer.getAudioSessionId();
        }
        return 0;
    }

    State getState() {
        return this.mState;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void setState(State state) {
        if (this.mState == state) {
            return;
        }
        this.mState = state;
        if (this.mObserver != null) {
            this.mObserver.onStateChange(state);
        }
    }
}
