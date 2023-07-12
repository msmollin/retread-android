package com.bambuser.broadcaster;

import android.util.Log;
import androidx.recyclerview.widget.ItemTouchHelper;
import com.bambuser.broadcaster.MediaCodecAudioWrapper;
import com.bambuser.broadcaster.SentryLogger;
import java.nio.ByteBuffer;

/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: classes.dex */
public final class TalkbackController {
    private static final String LOGTAG = "TalkbackController";
    private AudioReceiver mAudioReceiver;
    private String mCaller;
    private Connection mConnection;
    private MediaCodecAudioWrapper mDecoder;
    private Observer mObserver;
    private String mRequest;
    private int mSessionId;
    private int mTalkbackType;
    private String mTalkbackUrl;
    private AudioTrackWriter mTrackWriter;
    private final MediaCodecAudioWrapper.DataHandler mDecoderHandler = new MediaCodecAudioWrapper.DataHandler() { // from class: com.bambuser.broadcaster.TalkbackController.1
        @Override // com.bambuser.broadcaster.MediaCodecAudioWrapper.DataHandler
        void onFormatChanged(int i, int i2) {
            if (i != TalkbackController.this.mTrackWriter.getSampleRate() || i2 != TalkbackController.this.mTrackWriter.getChannelCount()) {
                TalkbackController.this.mTrackWriter.close();
                TalkbackController.this.mTrackWriter = new AudioTrackWriter(i, i2, true, null, ItemTouchHelper.Callback.DEFAULT_DRAG_ANIMATION_DURATION, 1200);
                Log.d(TalkbackController.LOGTAG, "mismatched sample rate/channels");
                return;
            }
            Log.d(TalkbackController.LOGTAG, "got matching sample rate");
        }

        @Override // com.bambuser.broadcaster.MediaCodecAudioWrapper.DataHandler
        void onOutputData(ByteBuffer byteBuffer, long j, boolean z) {
            ByteBuffer buffer = TalkbackController.this.mTrackWriter.getBuffer(byteBuffer.remaining());
            buffer.put(byteBuffer);
            buffer.flip();
            synchronized (TalkbackController.this) {
                if (TalkbackController.this.mAudioReceiver != null) {
                    TalkbackController.this.mAudioReceiver.onDecodedAudio(buffer, TalkbackController.this.mTrackWriter.getSampleRate(), TalkbackController.this.mTrackWriter.getChannelCount());
                }
            }
            TalkbackController.this.mTrackWriter.enqueue(buffer);
        }
    };
    private TalkbackState mState = TalkbackState.IDLE;

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes.dex */
    public interface AudioReceiver {
        void onDecodedAudio(ByteBuffer byteBuffer, int i, int i2);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes.dex */
    public interface Observer {
        void onTalkbackStateChanged(TalkbackState talkbackState, int i, String str, String str2);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static int getSupportedType() {
        return 6;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public synchronized void setObserver(Observer observer) {
        this.mObserver = observer;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public synchronized void setAudioReceiver(AudioReceiver audioReceiver) {
        this.mAudioReceiver = audioReceiver;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public synchronized boolean isEnabled() {
        boolean z;
        if (this.mObserver != null) {
            z = getSupportedType() != 0;
        }
        return z;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public synchronized void init(int i, String str, Connection connection, String str2, String str3) {
        stop();
        if (i != 0 && str != null && str.length() != 0 && connection != null) {
            this.mConnection = connection;
            this.mTalkbackUrl = str;
            this.mCaller = str2;
            this.mRequest = str3;
            this.mTalkbackType = i;
            this.mSessionId++;
            setState(TalkbackState.NEEDS_ACCEPT);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public synchronized void accept(int i) {
        if (this.mState != TalkbackState.NEEDS_ACCEPT) {
            return;
        }
        if (i != this.mSessionId) {
            return;
        }
        this.mConnection.send(MovinoUtils.createTalkbackStatus(5, this.mTalkbackType, this.mTalkbackUrl));
        setState(TalkbackState.ACCEPTED);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public synchronized void handleAudioFormat(int i, int i2) {
        if (this.mState != TalkbackState.IDLE && this.mState != TalkbackState.NEEDS_ACCEPT) {
            if (i > 0) {
                if (this.mTrackWriter != null) {
                    this.mTrackWriter.close();
                }
                this.mTrackWriter = new AudioTrackWriter(i, i2, true, null, ItemTouchHelper.Callback.DEFAULT_DRAG_ANIMATION_DURATION, 1200);
                setState(TalkbackState.READY);
            } else {
                stop();
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public synchronized void stop() {
        if (this.mConnection != null) {
            this.mConnection.send(MovinoUtils.createTalkbackStatus(1, this.mTalkbackType, this.mTalkbackUrl));
        }
        this.mConnection = null;
        this.mTalkbackUrl = null;
        this.mTalkbackType = 0;
        if (this.mDecoder != null) {
            this.mDecoder.close();
        }
        this.mDecoder = null;
        if (this.mTrackWriter != null) {
            this.mTrackWriter.close();
        }
        this.mTrackWriter = null;
        setState(TalkbackState.IDLE);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public synchronized void handlePacket(int i, int i2, ByteBuffer byteBuffer) {
        if (this.mState == TalkbackState.READY || this.mState == TalkbackState.PLAYING) {
            if (i == 12) {
                if (this.mDecoder != null) {
                    this.mDecoder.close();
                }
                try {
                    this.mDecoder = new MediaCodecAudioWrapper();
                    this.mDecoder.initDecoder("audio/mp4a-latm", this.mTrackWriter.getSampleRate(), this.mTrackWriter.getChannelCount(), byteBuffer, true);
                    Log.i(LOGTAG, "MediaCodec audio decoder initialized");
                    this.mConnection.send(MovinoUtils.createLogMessagePacket("Talkback MediaCodec decoder initialized"));
                } catch (Exception e) {
                    Log.w(LOGTAG, "Exception when initializing MediaCodec", e);
                    SentryLogger.asyncMessage("Exception when initializing Talkback MediaCodec", SentryLogger.Level.ERROR, null, e);
                    Connection connection = this.mConnection;
                    connection.send(MovinoUtils.createLogMessagePacket("Exception when initializing Talkback MediaCodec: " + e));
                    stop();
                }
                return;
            }
            if (i == 13) {
                if (this.mDecoder == null) {
                    return;
                }
                if (this.mState == TalkbackState.READY) {
                    this.mConnection.send(MovinoUtils.createTalkbackStatus(3, this.mTalkbackType, this.mTalkbackUrl));
                    setState(TalkbackState.PLAYING);
                }
                try {
                    this.mDecoder.decode(byteBuffer.array(), byteBuffer.arrayOffset() + byteBuffer.position(), byteBuffer.remaining(), 0L, this.mDecoderHandler);
                    this.mDecoder.flush(this.mDecoderHandler);
                } catch (Exception e2) {
                    Log.w(LOGTAG, "Exception when decoding in Talkback MediaCodec", e2);
                    SentryLogger.asyncMessage("Exception when decoding in Talkback MediaCodec", SentryLogger.Level.ERROR, null, e2);
                    Connection connection2 = this.mConnection;
                    connection2.send(MovinoUtils.createLogMessagePacket("Exception when decoding in Talkback MediaCodec: " + e2));
                    stop();
                }
            }
            return;
        }
    }

    private void setState(TalkbackState talkbackState) {
        if (this.mState == talkbackState) {
            return;
        }
        this.mState = talkbackState;
        if (this.mObserver != null) {
            this.mObserver.onTalkbackStateChanged(this.mState, this.mSessionId, this.mCaller, this.mRequest);
        }
    }
}
