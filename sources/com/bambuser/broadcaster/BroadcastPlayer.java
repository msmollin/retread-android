package com.bambuser.broadcaster;

import android.content.Context;
import android.graphics.SurfaceTexture;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Handler;
import android.os.SystemClock;
import android.provider.Settings;
import android.util.Log;
import android.util.Pair;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.TextureView;
import android.widget.MediaController;
import com.bambuser.broadcaster.HlsParser;
import com.bambuser.broadcaster.LinkTester;
import com.bambuser.broadcaster.NiceMediaPlayer;
import com.bambuser.broadcaster.PlaybackController;
import com.bambuser.broadcaster.WebSocketConnection;
import com.facebook.appevents.internal.ViewHierarchyConstants;
import com.facebook.share.internal.MessengerShareContentUtility;
import com.treadly.Treadly.Data.Managers.TreadlyEventHelper;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.nio.ByteBuffer;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import javax.net.ssl.HttpsURLConnection;
import org.json.JSONArray;
import org.json.JSONObject;

/* loaded from: classes.dex */
public final class BroadcastPlayer implements MediaController.MediaPlayerControl {
    private static final String CLIENTINFO_URL = "https://clientinfo.bambuser.net/";
    private static final String LOGTAG = "BroadcastPlayer";
    private static final double NEXT_BITRATE_MARGIN = 1.2d;
    private static final String TELEMETRY_PROTOCOL = "bambuser-cdn-logger-v1";
    private static final String WEBSOCKET_USER_AGENT = "Bambuser Android lib 0.9.22";
    private int mActiveHeight;
    private int mActiveWidth;
    private final String mApplicationId;
    private final Context mContext;
    private String mCountryCode;
    private long mCurrentInterruptionStartTime;
    private long mFirstFrameTime;
    private volatile FlvInfo mFlvInfo;
    private int mHeight;
    private HlsParser mHlsParser;
    private long mInterruptionsDuration;
    private long mInterruptionsInterval;
    private long mInterruptionsStartedCount;
    private long mLastInterruptionsReportTime;
    private long mLastTelemetryReportTime;
    private long mLatencyInterval;
    private long mLoadStartTime;
    private final Handler mMainHandler;
    private NiceMediaPlayer mMediaPlayer;
    private Observer mObserver;
    private volatile PlaybackController mPlaybackController;
    private String mPreset;
    private String mResourceUri;
    private long mSeekStartTime;
    private Surface mSurface;
    private SurfaceView mSurfaceView;
    private long mSyncedOffsetFromMonotonic;
    private WebSocketConnection mTelemetryConnection;
    private long mTelemetryHelloMonotonicTime;
    private TextureView mTextureView;
    private int mWidth;
    private String mWssUrl;
    private static final char[] HEXCHARS = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};
    private static final String APP_SESSION_ID = createRandomId();
    private final Runnable mRetryCDNRequest = new Runnable() { // from class: com.bambuser.broadcaster.BroadcastPlayer.2
        @Override // java.lang.Runnable
        public void run() {
            if (BroadcastPlayer.this.mState == PlayerState.LOADING) {
                BroadcastPlayer.this.initialCDNRequest();
            }
        }
    };
    private final Runnable mVideoConsumedRunnable = new Runnable() { // from class: com.bambuser.broadcaster.BroadcastPlayer.11
        @Override // java.lang.Runnable
        public void run() {
            BroadcastPlayer.this.sendVideoConsumedReports();
            if (BroadcastPlayer.this.mVideoConsumedInterval <= 0 || BroadcastPlayer.this.mTelemetryConnection == null) {
                return;
            }
            BroadcastPlayer.this.mMainHandler.postDelayed(this, BroadcastPlayer.this.mVideoConsumedInterval);
        }
    };
    private final Runnable mInterruptionsRunnable = new Runnable() { // from class: com.bambuser.broadcaster.BroadcastPlayer.12
        @Override // java.lang.Runnable
        public void run() {
            BroadcastPlayer.this.sendInterruptionReports();
            if (BroadcastPlayer.this.mInterruptionsInterval <= 0 || BroadcastPlayer.this.mTelemetryConnection == null) {
                return;
            }
            BroadcastPlayer.this.mMainHandler.postDelayed(this, BroadcastPlayer.this.mInterruptionsInterval);
        }
    };
    private final Runnable mLatencyRunnable = new Runnable() { // from class: com.bambuser.broadcaster.BroadcastPlayer.13
        @Override // java.lang.Runnable
        public void run() {
            BroadcastPlayer.this.sendLatencyReports();
            if (BroadcastPlayer.this.mLatencyInterval <= 0 || BroadcastPlayer.this.mTelemetryConnection == null) {
                return;
            }
            BroadcastPlayer.this.mMainHandler.postDelayed(this, BroadcastPlayer.this.mLatencyInterval);
        }
    };
    private final HlsParser.Observer mHlsParserObserver = new HlsParser.Observer() { // from class: com.bambuser.broadcaster.BroadcastPlayer.14
        @Override // com.bambuser.broadcaster.HlsParser.Observer
        public void onDownloadedBytes(long j) {
            BroadcastPlayer.this.mConsumedBytes.addAndGet(j);
        }

        @Override // com.bambuser.broadcaster.HlsParser.Observer
        public void onError() {
            BroadcastPlayer.this.mMainHandler.post(new Runnable() { // from class: com.bambuser.broadcaster.BroadcastPlayer.14.1
                @Override // java.lang.Runnable
                public void run() {
                    if (BroadcastPlayer.this.mPlaybackController != null) {
                        BroadcastPlayer.this.mPlaybackController.prepareClose();
                    }
                    if (BroadcastPlayer.this.mHlsParser != null) {
                        BroadcastPlayer.this.mHlsParser.close();
                    }
                    BroadcastPlayer.this.mHlsParser = null;
                    if (BroadcastPlayer.this.mPlaybackController != null) {
                        BroadcastPlayer.this.mPlaybackController.close();
                    }
                    BroadcastPlayer.this.mPlaybackController = null;
                    if (BroadcastPlayer.this.mState != PlayerState.CLOSED) {
                        BroadcastPlayer.this.setState(PlayerState.ERROR);
                    }
                }
            });
        }
    };
    private final WebSocketConnection.Observer mWebSocketObserver = new WebSocketConnection.Observer() { // from class: com.bambuser.broadcaster.BroadcastPlayer.15
        @Override // com.bambuser.broadcaster.WebSocketConnection.Observer
        public void onReceivedBinary(ByteBuffer byteBuffer) {
        }

        @Override // com.bambuser.broadcaster.WebSocketConnection.Observer
        public void onReceivedString(String str) {
            if (str == null) {
                return;
            }
            try {
                final JSONObject jSONObject = new JSONObject(str);
                BroadcastPlayer.this.mMainHandler.post(new Runnable() { // from class: com.bambuser.broadcaster.BroadcastPlayer.15.1
                    @Override // java.lang.Runnable
                    public void run() {
                        BroadcastPlayer.this.handleIncomingTelemetryPacket(jSONObject);
                    }
                });
            } catch (Exception e) {
                Log.w(BroadcastPlayer.LOGTAG, "Exception when parsing incoming telemetry string: " + e);
            }
        }

        @Override // com.bambuser.broadcaster.WebSocketConnection.Observer
        public void onConnectionClosed() {
            Log.w(BroadcastPlayer.LOGTAG, "telemetry onConnectionClosed");
            BroadcastPlayer.this.mMainHandler.removeCallbacks(BroadcastPlayer.this.mVideoConsumedRunnable);
            BroadcastPlayer.this.mMainHandler.removeCallbacks(BroadcastPlayer.this.mInterruptionsRunnable);
            BroadcastPlayer.this.mMainHandler.removeCallbacks(BroadcastPlayer.this.mLatencyRunnable);
            BroadcastPlayer.this.mMainHandler.post(new Runnable() { // from class: com.bambuser.broadcaster.BroadcastPlayer.15.2
                @Override // java.lang.Runnable
                public void run() {
                    BroadcastPlayer.this.mVideoConsumedInterval = 0;
                    BroadcastPlayer.this.mInterruptionsInterval = 0L;
                    BroadcastPlayer.this.mLatencyInterval = 0L;
                    BroadcastPlayer.this.mTelemetryConnection = null;
                }
            });
            if (BroadcastPlayer.this.shouldTelemetryReconnect()) {
                if (BroadcastPlayer.this.mTelemetryBackoff < 60000) {
                    BroadcastPlayer.this.mTelemetryBackoff += 5000;
                }
                BroadcastPlayer.this.mMainHandler.postDelayed(BroadcastPlayer.this.mTelemetryReconnect, BroadcastPlayer.this.mTelemetryBackoff);
            }
        }

        @Override // com.bambuser.broadcaster.WebSocketConnection.Observer
        public void onConnected(String str) {
            BroadcastPlayer.this.mMainHandler.post(new Runnable() { // from class: com.bambuser.broadcaster.BroadcastPlayer.15.3
                @Override // java.lang.Runnable
                public void run() {
                    BroadcastPlayer.this.mTelemetryBackoff = 0;
                    BroadcastPlayer.this.sendTelemetryHello();
                    if (BroadcastPlayer.this.mCountryCode == null) {
                        BroadcastPlayer.this.clientInfoRequest();
                    } else {
                        BroadcastPlayer.this.sendTelemetryClientInfo();
                    }
                    BroadcastPlayer.this.sendTelemetryTimeToFirstFrame();
                    if (BroadcastPlayer.this.isPlaying()) {
                        BroadcastPlayer.this.startTelemetryView();
                    }
                }
            });
        }
    };
    private final Runnable mTelemetryReconnect = new Runnable() { // from class: com.bambuser.broadcaster.BroadcastPlayer.16
        @Override // java.lang.Runnable
        public void run() {
            if (BroadcastPlayer.this.shouldTelemetryReconnect()) {
                BroadcastPlayer.this.mTelemetryConnection = new WebSocketConnection(URI.create(BroadcastPlayer.this.mWssUrl), BroadcastPlayer.TELEMETRY_PROTOCOL, BroadcastPlayer.WEBSOCKET_USER_AGENT, BroadcastPlayer.this.mWebSocketObserver);
            }
        }
    };
    private final SurfaceHolder.Callback mSurfaceObserver = new SurfaceHolder.Callback() { // from class: com.bambuser.broadcaster.BroadcastPlayer.17
        @Override // android.view.SurfaceHolder.Callback
        public void surfaceChanged(SurfaceHolder surfaceHolder, int i, int i2, int i3) {
        }

        @Override // android.view.SurfaceHolder.Callback
        public void surfaceCreated(SurfaceHolder surfaceHolder) {
            BroadcastPlayer.this.setSurfaceInternal(surfaceHolder.getSurface());
        }

        @Override // android.view.SurfaceHolder.Callback
        public void surfaceDestroyed(SurfaceHolder surfaceHolder) {
            BroadcastPlayer.this.setSurfaceInternal(null);
        }
    };
    private final TextureView.SurfaceTextureListener mSurfaceTextureListener = new TextureView.SurfaceTextureListener() { // from class: com.bambuser.broadcaster.BroadcastPlayer.18
        @Override // android.view.TextureView.SurfaceTextureListener
        public void onSurfaceTextureSizeChanged(SurfaceTexture surfaceTexture, int i, int i2) {
        }

        @Override // android.view.TextureView.SurfaceTextureListener
        public void onSurfaceTextureUpdated(SurfaceTexture surfaceTexture) {
        }

        @Override // android.view.TextureView.SurfaceTextureListener
        public void onSurfaceTextureAvailable(SurfaceTexture surfaceTexture, int i, int i2) {
            BroadcastPlayer.this.setSurfaceInternal(new Surface(surfaceTexture));
        }

        @Override // android.view.TextureView.SurfaceTextureListener
        public boolean onSurfaceTextureDestroyed(SurfaceTexture surfaceTexture) {
            BroadcastPlayer.this.setSurfaceInternal(null);
            return true;
        }
    };
    private final PlaybackController.Observer mPlaybackControllerObserver = new PlaybackController.Observer() { // from class: com.bambuser.broadcaster.BroadcastPlayer.19
        @Override // com.bambuser.broadcaster.PlaybackController.Observer
        public void onResolutionChange(final int i, final int i2) {
            BroadcastPlayer.this.mMainHandler.post(new Runnable() { // from class: com.bambuser.broadcaster.BroadcastPlayer.19.1
                @Override // java.lang.Runnable
                public void run() {
                    if (BroadcastPlayer.this.mActiveWidth > 0 && BroadcastPlayer.this.mActiveHeight > 0 && (i != BroadcastPlayer.this.mActiveWidth || i2 != BroadcastPlayer.this.mActiveHeight)) {
                        BroadcastPlayer.this.sendVideoConsumedReports();
                        BroadcastPlayer.this.sendInterruptionReports();
                    }
                    Log.i(BroadcastPlayer.LOGTAG, "onResolutionChange: " + i + "x" + i2);
                    BroadcastPlayer.this.mActiveWidth = i;
                    BroadcastPlayer.this.mActiveHeight = i2;
                }
            });
        }

        @Override // com.bambuser.broadcaster.PlaybackController.Observer
        public void onQueueLow() {
            BroadcastPlayer.this.mMainHandler.post(new Runnable() { // from class: com.bambuser.broadcaster.BroadcastPlayer.19.2
                @Override // java.lang.Runnable
                public void run() {
                    BroadcastPlayer.this.considerVariant(false);
                }
            });
        }

        @Override // com.bambuser.broadcaster.PlaybackController.Observer
        public void onQueueHigh() {
            BroadcastPlayer.this.mMainHandler.post(new Runnable() { // from class: com.bambuser.broadcaster.BroadcastPlayer.19.3
                @Override // java.lang.Runnable
                public void run() {
                    BroadcastPlayer.this.considerVariant(true);
                }
            });
        }

        @Override // com.bambuser.broadcaster.PlaybackController.Observer
        public void onPlaybackEnd() {
            BroadcastPlayer.this.mMainHandler.post(new Runnable() { // from class: com.bambuser.broadcaster.BroadcastPlayer.19.4
                @Override // java.lang.Runnable
                public void run() {
                    if (BroadcastPlayer.this.mState == PlayerState.PLAYING || BroadcastPlayer.this.mState == PlayerState.BUFFERING) {
                        BroadcastPlayer.this.setState(PlayerState.COMPLETED);
                    }
                }
            });
        }

        @Override // com.bambuser.broadcaster.PlaybackController.Observer
        public void onAudioBuffering() {
            if (BroadcastPlayer.this.mState == PlayerState.PLAYING) {
                BroadcastPlayer.this.setState(PlayerState.BUFFERING);
            }
        }

        @Override // com.bambuser.broadcaster.PlaybackController.Observer
        public void onAudioPlaying() {
            if (BroadcastPlayer.this.mState == PlayerState.BUFFERING || BroadcastPlayer.this.mState == PlayerState.PAUSED) {
                BroadcastPlayer.this.setState(PlayerState.PLAYING);
            }
        }

        @Override // com.bambuser.broadcaster.PlaybackController.Observer
        public void onAudioPaused() {
            if (BroadcastPlayer.this.mState == PlayerState.BUFFERING || BroadcastPlayer.this.mState == PlayerState.PLAYING) {
                BroadcastPlayer.this.setState(PlayerState.PAUSED);
            }
        }

        @Override // com.bambuser.broadcaster.PlaybackController.Observer
        public void onDecoderFailure() {
            BroadcastPlayer.this.mMainHandler.post(new Runnable() { // from class: com.bambuser.broadcaster.BroadcastPlayer.19.5
                @Override // java.lang.Runnable
                public void run() {
                    if (BroadcastPlayer.this.mPlaybackController != null) {
                        BroadcastPlayer.this.mPlaybackController.prepareClose();
                    }
                    if (BroadcastPlayer.this.mFlvReaderThread != null) {
                        BroadcastPlayer.this.mFlvReaderThread.setController(null);
                    }
                    BroadcastPlayer.this.mFlvReaderThread = null;
                    if (BroadcastPlayer.this.mHlsParser != null) {
                        BroadcastPlayer.this.mHlsParser.close();
                    }
                    BroadcastPlayer.this.mHlsParser = null;
                    if (BroadcastPlayer.this.mPlaybackController != null) {
                        BroadcastPlayer.this.mPlaybackController.close();
                    }
                    BroadcastPlayer.this.mPlaybackController = null;
                    if (BroadcastPlayer.this.mState != PlayerState.CLOSED) {
                        BroadcastPlayer.this.setState(PlayerState.ERROR);
                    }
                }
            });
        }

        @Override // com.bambuser.broadcaster.PlaybackController.Observer
        public void onAudioOverflow() {
            BroadcastPlayer.access$6008(BroadcastPlayer.this);
        }
    };
    private final NiceMediaPlayer.Observer mMediaPlayerObserver = new NiceMediaPlayer.Observer() { // from class: com.bambuser.broadcaster.BroadcastPlayer.20
        @Override // com.bambuser.broadcaster.NiceMediaPlayer.Observer
        public void onStateChange(NiceMediaPlayer.State state) {
            if (state == NiceMediaPlayer.State.STARTED && BroadcastPlayer.this.mState != PlayerState.CLOSED && BroadcastPlayer.this.mState != PlayerState.ERROR) {
                if (BroadcastPlayer.this.mSurfaceView != null && !BroadcastPlayer.this.mSurfaceView.getKeepScreenOn()) {
                    BroadcastPlayer.this.mKeepingScreenOn = true;
                    BroadcastPlayer.this.mSurfaceView.setKeepScreenOn(true);
                }
                if (BroadcastPlayer.this.mTextureView != null && !BroadcastPlayer.this.mTextureView.getKeepScreenOn()) {
                    BroadcastPlayer.this.mKeepingScreenOn = true;
                    BroadcastPlayer.this.mTextureView.setKeepScreenOn(true);
                }
                BroadcastPlayer.this.setState(PlayerState.PLAYING);
            }
            if (state == NiceMediaPlayer.State.PAUSED && (BroadcastPlayer.this.mState == PlayerState.PLAYING || BroadcastPlayer.this.mState == PlayerState.BUFFERING)) {
                BroadcastPlayer.this.setState(PlayerState.PAUSED);
            }
            if (state == NiceMediaPlayer.State.PLAYBACK_COMPLETED && BroadcastPlayer.this.mState != PlayerState.CLOSED && BroadcastPlayer.this.mState != PlayerState.ERROR) {
                BroadcastPlayer.this.setState(PlayerState.COMPLETED);
            }
            if (state == NiceMediaPlayer.State.ERROR) {
                if (BroadcastPlayer.this.mMediaPlayer != null) {
                    BroadcastPlayer.this.mMediaPlayer.release();
                }
                BroadcastPlayer.this.mMediaPlayer = null;
                if (BroadcastPlayer.this.mState != PlayerState.CLOSED) {
                    BroadcastPlayer.this.setState(PlayerState.ERROR);
                }
            }
            if (state != NiceMediaPlayer.State.BUFFERING || BroadcastPlayer.this.mState == PlayerState.CLOSED || BroadcastPlayer.this.mState == PlayerState.ERROR) {
                return;
            }
            BroadcastPlayer.this.setState(PlayerState.BUFFERING);
        }

        @Override // com.bambuser.broadcaster.NiceMediaPlayer.Observer
        public void onResolutionChange(int i, int i2) {
            if (BroadcastPlayer.this.mActiveWidth > 0 && BroadcastPlayer.this.mActiveHeight > 0 && (i != BroadcastPlayer.this.mActiveWidth || i2 != BroadcastPlayer.this.mActiveHeight)) {
                BroadcastPlayer.this.sendVideoConsumedReports();
                BroadcastPlayer.this.sendInterruptionReports();
            }
            Log.i(BroadcastPlayer.LOGTAG, "onResolutionChange: " + i + "x" + i2);
            BroadcastPlayer.this.mActiveWidth = i;
            BroadcastPlayer.this.mActiveHeight = i2;
        }
    };
    private final String mPlayerInstanceId = createRandomId();
    private String mAcceptedType = "viewable";
    private final List<BenchmarkPayload> mBenchmarkPayloads = new LinkedList();
    private final Map<String, Long> mHlsBitrateMap = new HashMap();
    private ViewerCountObserver mViewerCountObserver = null;
    private String mBroadcastId = "";
    private boolean mClientInfoRequestRunning = false;
    private int mTelemetryBackoff = 0;
    private int mSyncedTimeUncertainty = -1;
    private int mVideoConsumedInterval = 0;
    private float mAudioVolume = 1.0f;
    private boolean mKeepingScreenOn = false;
    private boolean mLive = false;
    private boolean mTimeshiftMode = false;
    private LatencyMode mLatencyMode = LatencyMode.LOW;
    private long mLinkTestTime = 0;
    private long mLinkTestBackoff = 0;
    private final AtomicLong mConsumedBytes = new AtomicLong(0);
    private volatile boolean mUseBitrateTelemetry = false;
    private volatile boolean mVariantCDNRequestRunning = false;
    private volatile boolean mLinkTestActive = false;
    private volatile boolean mBenchmarkPayloadRequestRunning = false;
    private FlvReaderThread mFlvReaderThread = null;
    private volatile PlayerState mState = PlayerState.CONSTRUCTION;

    /* loaded from: classes.dex */
    public enum AcceptType {
        ANY,
        LIVE,
        ARCHIVED
    }

    /* loaded from: classes.dex */
    public enum LatencyMode {
        LOW,
        HIGH
    }

    /* loaded from: classes.dex */
    public interface Observer {
        void onBroadcastLoaded(boolean z, int i, int i2);

        void onStateChange(PlayerState playerState);
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes.dex */
    public interface RequestCallback {
        void onResult(int i, JSONObject jSONObject);
    }

    /* loaded from: classes.dex */
    public interface ViewerCountObserver {
        void onCurrentViewersUpdated(long j);

        void onTotalViewersUpdated(long j);
    }

    static /* synthetic */ long access$6008(BroadcastPlayer broadcastPlayer) {
        long j = broadcastPlayer.mInterruptionsStartedCount;
        broadcastPlayer.mInterruptionsStartedCount = 1 + j;
        return j;
    }

    public BroadcastPlayer(Context context, String str, String str2, Observer observer) {
        this.mMainHandler = new Handler(context.getMainLooper());
        this.mContext = context;
        this.mResourceUri = str;
        this.mApplicationId = str2;
        this.mObserver = observer;
        SentryLogger.initLogger(context);
        benchmarkPayloadsCDNRequest();
        clientInfoRequest();
    }

    public void setAcceptType(AcceptType acceptType) {
        if (this.mState != PlayerState.CONSTRUCTION) {
            Log.w(LOGTAG, "AcceptType can only be set before load()");
        } else if (acceptType == AcceptType.LIVE) {
            this.mAcceptedType = "live";
        } else if (acceptType == AcceptType.ARCHIVED) {
            this.mAcceptedType = "archived";
        } else {
            this.mAcceptedType = "viewable";
        }
    }

    public void setTimeshiftMode(boolean z) {
        if (this.mState != PlayerState.CONSTRUCTION) {
            Log.w(LOGTAG, "Timeshift mode can only be set before load()");
        } else {
            this.mTimeshiftMode = z;
        }
    }

    public void setLatencyMode(LatencyMode latencyMode) {
        if (this.mState != PlayerState.CONSTRUCTION) {
            Log.w(LOGTAG, "Latency mode can only be changed before load()");
        } else {
            this.mLatencyMode = latencyMode;
        }
    }

    public void setSurfaceView(SurfaceView surfaceView) {
        clearConsumers();
        this.mSurfaceView = surfaceView;
        if (this.mSurfaceView != null) {
            this.mSurfaceView.getHolder().addCallback(this.mSurfaceObserver);
            if (!this.mSurfaceView.getKeepScreenOn()) {
                this.mKeepingScreenOn = true;
                this.mSurfaceView.setKeepScreenOn(true);
            }
            if ((this.mSurfaceView instanceof SurfaceViewWithAutoAR) && this.mWidth > 0 && this.mHeight > 0) {
                ((SurfaceViewWithAutoAR) this.mSurfaceView).setAspectRatio(this.mWidth, this.mHeight);
            }
        }
        setSurfaceInternal(currentSurface());
    }

    public void setTextureView(TextureView textureView) {
        clearConsumers();
        this.mTextureView = textureView;
        if (this.mTextureView != null) {
            this.mTextureView.setSurfaceTextureListener(this.mSurfaceTextureListener);
            if (!this.mTextureView.getKeepScreenOn()) {
                this.mKeepingScreenOn = true;
                this.mTextureView.setKeepScreenOn(true);
            }
        }
        setSurfaceInternal(currentSurface());
    }

    public void setSurface(Surface surface) {
        clearConsumers();
        this.mSurface = surface;
        setSurfaceInternal(currentSurface());
    }

    public void load() {
        if (this.mState != PlayerState.CONSTRUCTION) {
            Log.w(LOGTAG, "load() call ignored. only valid during CONSTRUCTION state.");
        } else if (this.mApplicationId == null || this.mResourceUri == null || this.mApplicationId.isEmpty() || this.mResourceUri.isEmpty() || this.mApplicationId.contains("CHANGEME") || this.mApplicationId.contains("PLEASE")) {
            Log.w(LOGTAG, "load() missing applicationId or resourceUri");
            setState(PlayerState.ERROR);
        } else {
            this.mLoadStartTime = SystemClock.elapsedRealtime();
            initialCDNRequest();
        }
    }

    public void close() {
        if (this.mPlaybackController != null) {
            this.mPlaybackController.prepareClose();
        }
        if (this.mFlvReaderThread != null) {
            this.mFlvReaderThread.setController(null);
        }
        this.mFlvReaderThread = null;
        if (this.mHlsParser != null) {
            this.mHlsParser.close();
        }
        this.mHlsParser = null;
        clearConsumers();
        if (this.mMediaPlayer != null) {
            this.mMediaPlayer.release();
        }
        this.mMediaPlayer = null;
        this.mObserver = null;
        if (this.mPlaybackController != null) {
            this.mPlaybackController.close();
        }
        this.mPlaybackController = null;
        this.mFlvInfo = null;
        setState(PlayerState.CLOSED);
    }

    public boolean isTypeLive() {
        return this.mLive;
    }

    public String getBroadcastId() {
        return this.mBroadcastId;
    }

    public PlayerState getState() {
        return this.mState;
    }

    @Override // android.widget.MediaController.MediaPlayerControl
    public int getDuration() {
        if (!this.mLive && this.mMediaPlayer != null) {
            return this.mMediaPlayer.getDuration();
        }
        if (this.mPlaybackController != null) {
            return this.mPlaybackController.getDuration();
        }
        return -1;
    }

    @Override // android.widget.MediaController.MediaPlayerControl
    public int getCurrentPosition() {
        if (!this.mLive && this.mMediaPlayer != null) {
            return this.mMediaPlayer.getCurrentPosition();
        }
        if (this.mPlaybackController != null) {
            return this.mPlaybackController.getCurrentPosition();
        }
        return 0;
    }

    @Override // android.widget.MediaController.MediaPlayerControl
    public void seekTo(int i) {
        if (!this.mLive && this.mMediaPlayer != null) {
            endInterruption();
            this.mSeekStartTime = SystemClock.elapsedRealtime();
            this.mMediaPlayer.seekTo(i);
        }
        if (hasTimeshiftBroadcast()) {
            endInterruption();
            this.mSeekStartTime = SystemClock.elapsedRealtime();
            this.mHlsParser.stopPlayback();
            this.mPlaybackController.flush();
            long j = i;
            this.mPlaybackController.setTentativePosition(j);
            this.mHlsParser.seekTo(j);
        }
    }

    public LatencyMeasurement getEndToEndLatency() {
        if (this.mLive && this.mPlaybackController != null && isPlaying()) {
            int broadcasterUncertainty = this.mPlaybackController.getBroadcasterUncertainty();
            if (this.mSyncedTimeUncertainty < 0 || broadcasterUncertainty < 0) {
                return null;
            }
            return new LatencyMeasurement(this.mPlaybackController.getLatency(getSyncedRealTime()), this.mSyncedTimeUncertainty + broadcasterUncertainty);
        }
        return null;
    }

    @Override // android.widget.MediaController.MediaPlayerControl
    public boolean isPlaying() {
        return this.mState == PlayerState.PLAYING || this.mState == PlayerState.BUFFERING;
    }

    @Override // android.widget.MediaController.MediaPlayerControl
    public int getBufferPercentage() {
        if (this.mLive || this.mMediaPlayer == null) {
            return 0;
        }
        return this.mMediaPlayer.getBufferPercentage();
    }

    @Override // android.widget.MediaController.MediaPlayerControl
    public boolean canPause() {
        return hasArchivedBroadcast() || hasTimeshiftBroadcast();
    }

    @Override // android.widget.MediaController.MediaPlayerControl
    public boolean canSeekBackward() {
        return hasArchivedBroadcast() || hasTimeshiftBroadcast();
    }

    @Override // android.widget.MediaController.MediaPlayerControl
    public boolean canSeekForward() {
        return hasArchivedBroadcast() || hasTimeshiftBroadcast();
    }

    @Override // android.widget.MediaController.MediaPlayerControl
    public int getAudioSessionId() {
        if (this.mPlaybackController != null) {
            return this.mPlaybackController.getAudioSessionId();
        }
        if (this.mMediaPlayer != null) {
            return this.mMediaPlayer.getAudioSessionId();
        }
        return 0;
    }

    public void setViewerCountObserver(ViewerCountObserver viewerCountObserver) {
        this.mViewerCountObserver = viewerCountObserver;
    }

    public void setAudioVolume(float f) {
        if (Float.isNaN(f) || f > 1.0f) {
            f = 1.0f;
        }
        if (f < 0.0f) {
            f = 0.0f;
        }
        this.mAudioVolume = f;
        if (this.mPlaybackController != null) {
            this.mPlaybackController.setAudioVolume(f);
        }
        if (this.mMediaPlayer != null) {
            this.mMediaPlayer.setAudioVolume(f);
        }
    }

    @Override // android.widget.MediaController.MediaPlayerControl
    public void pause() {
        if (!this.mLive && this.mMediaPlayer != null) {
            this.mLoadStartTime = 0L;
            this.mMediaPlayer.pause();
        }
        if (hasTimeshiftBroadcast()) {
            this.mLoadStartTime = 0L;
            this.mHlsParser.pausePlayback();
            this.mPlaybackController.pause();
        }
    }

    @Override // android.widget.MediaController.MediaPlayerControl
    public void start() {
        if (!this.mLive && this.mMediaPlayer != null) {
            this.mMediaPlayer.start();
        }
        if (hasTimeshiftBroadcast()) {
            this.mPlaybackController.start();
            this.mHlsParser.unpausePlayback();
        }
    }

    private boolean hasArchivedBroadcast() {
        if (this.mLive || this.mMediaPlayer == null) {
            return false;
        }
        return this.mState == PlayerState.BUFFERING || this.mState == PlayerState.PLAYING || this.mState == PlayerState.PAUSED || this.mState == PlayerState.COMPLETED;
    }

    private boolean hasTimeshiftBroadcast() {
        if (!this.mTimeshiftMode || this.mHlsParser == null || this.mPlaybackController == null) {
            return false;
        }
        return this.mState == PlayerState.BUFFERING || this.mState == PlayerState.PLAYING || this.mState == PlayerState.PAUSED;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void setState(PlayerState playerState) {
        if (playerState == this.mState) {
            return;
        }
        preStateChange(playerState);
        this.mState = playerState;
        postStateChange();
    }

    private void preStateChange(PlayerState playerState) {
        if (playerState == PlayerState.PLAYING && this.mLoadStartTime > 0 && this.mFirstFrameTime == 0) {
            this.mFirstFrameTime = SystemClock.elapsedRealtime();
            sendTelemetryTimeToFirstFrame();
        }
        if (playerState == PlayerState.ERROR || playerState == PlayerState.CLOSED) {
            this.mMainHandler.removeCallbacks(this.mVideoConsumedRunnable);
            this.mMainHandler.removeCallbacks(this.mInterruptionsRunnable);
            this.mMainHandler.removeCallbacks(this.mLatencyRunnable);
            this.mMainHandler.removeCallbacks(this.mTelemetryReconnect);
            if (this.mState == PlayerState.PLAYING || this.mState == PlayerState.BUFFERING) {
                stopTelemetryView();
            }
            this.mVideoConsumedInterval = 0;
            this.mInterruptionsInterval = 0L;
            this.mLatencyInterval = 0L;
            if (this.mTelemetryConnection != null) {
                this.mTelemetryConnection.close();
            }
            this.mTelemetryConnection = null;
        } else if (this.mTelemetryConnection == null && this.mWssUrl != null) {
            this.mTelemetryConnection = new WebSocketConnection(URI.create(this.mWssUrl), TELEMETRY_PROTOCOL, WEBSOCKET_USER_AGENT, this.mWebSocketObserver);
        }
        if (playerState == PlayerState.PLAYING && this.mSeekStartTime > 0) {
            this.mSeekStartTime = 0L;
        }
        if ((playerState == PlayerState.PLAYING || playerState == PlayerState.BUFFERING) && (this.mState == PlayerState.LOADING || this.mState == PlayerState.COMPLETED || this.mState == PlayerState.PAUSED)) {
            this.mLastTelemetryReportTime = SystemClock.elapsedRealtime();
            this.mLastInterruptionsReportTime = this.mLastTelemetryReportTime;
            startTelemetryView();
        }
        if (playerState == PlayerState.BUFFERING && this.mState == PlayerState.PLAYING) {
            startInterruption();
        }
        if (this.mState == PlayerState.BUFFERING && (playerState == PlayerState.PLAYING || playerState == PlayerState.CLOSED || playerState == PlayerState.ERROR || playerState == PlayerState.PAUSED)) {
            endInterruption();
        }
        if (playerState == PlayerState.PAUSED || playerState == PlayerState.COMPLETED) {
            stopTelemetryView();
            this.mLastTelemetryReportTime = 0L;
            this.mLastInterruptionsReportTime = 0L;
        }
    }

    private void postStateChange() {
        if ((this.mState == PlayerState.ERROR || this.mState == PlayerState.COMPLETED) && this.mKeepingScreenOn) {
            if (this.mSurfaceView != null) {
                this.mSurfaceView.setKeepScreenOn(false);
            }
            if (this.mTextureView != null) {
                this.mTextureView.setKeepScreenOn(false);
            }
            this.mKeepingScreenOn = false;
        }
        if (this.mObserver != null) {
            this.mObserver.onStateChange(this.mState);
        }
    }

    private void startInterruption() {
        long elapsedRealtime = SystemClock.elapsedRealtime();
        if (elapsedRealtime - this.mSeekStartTime < 1000) {
            return;
        }
        this.mInterruptionsStartedCount++;
        this.mCurrentInterruptionStartTime = elapsedRealtime;
    }

    private void endInterruption() {
        if (this.mCurrentInterruptionStartTime <= 0) {
            return;
        }
        this.mInterruptionsDuration += SystemClock.elapsedRealtime() - this.mCurrentInterruptionStartTime;
        this.mCurrentInterruptionStartTime = 0L;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void initialCDNRequest() {
        setState(PlayerState.LOADING);
        JSONObject jSONObject = new JSONObject();
        try {
            jSONObject.put("resourceUri", this.mResourceUri);
            jSONObject.put("broadcastState", this.mAcceptedType);
            JSONArray jSONArray = new JSONArray();
            if (!"archived".equals(this.mAcceptedType) && !this.mTimeshiftMode) {
                jSONArray.put(new JSONObject().put("preset", "ll-hls"));
            }
            if (!"archived".equals(this.mAcceptedType) && !this.mTimeshiftMode) {
                jSONArray.put(new JSONObject().put("preset", "flv"));
            }
            jSONArray.put(new JSONObject().put("preset", "hls"));
            jSONObject.put("criteria", jSONArray);
        } catch (Exception unused) {
        }
        doCDNRequest("https://cdn.bambuser.net/contentRequests", jSONObject, new RequestCallback() { // from class: com.bambuser.broadcaster.BroadcastPlayer.1
            @Override // com.bambuser.broadcaster.BroadcastPlayer.RequestCallback
            public void onResult(int i, JSONObject jSONObject2) {
                String optString;
                if (BroadcastPlayer.this.mState != PlayerState.LOADING) {
                    return;
                }
                if (jSONObject2 == null) {
                    BroadcastPlayer.this.setState(PlayerState.ERROR);
                } else if (i == 503) {
                    BroadcastPlayer.this.mMainHandler.postDelayed(BroadcastPlayer.this.mRetryCDNRequest, jSONObject2.optInt("ttl", 5) * 1000);
                } else {
                    JSONObject optJSONObject = jSONObject2.optJSONObject(BroadcastElement.ELEMENT);
                    JSONObject optJSONObject2 = jSONObject2.optJSONObject("match");
                    JSONObject optJSONObject3 = jSONObject2.optJSONObject("matchFor");
                    if (optJSONObject == null || optJSONObject2 == null || optJSONObject3 == null) {
                        BroadcastPlayer.this.setState(PlayerState.ERROR);
                        return;
                    }
                    BroadcastPlayer.this.mBroadcastId = optJSONObject.optString("id");
                    BroadcastPlayer.this.mWidth = optJSONObject.optInt(ViewHierarchyConstants.DIMENSION_WIDTH_KEY);
                    BroadcastPlayer.this.mHeight = optJSONObject.optInt(ViewHierarchyConstants.DIMENSION_HEIGHT_KEY);
                    String optString2 = optJSONObject2.optString("resourceUri");
                    if (optString2 != null && !optString2.isEmpty()) {
                        BroadcastPlayer.this.mResourceUri = optString2;
                    }
                    if (BroadcastPlayer.this.mWidth > 0 && BroadcastPlayer.this.mHeight > 0 && (BroadcastPlayer.this.mSurfaceView instanceof SurfaceViewWithAutoAR)) {
                        ((SurfaceViewWithAutoAR) BroadcastPlayer.this.mSurfaceView).setAspectRatio(BroadcastPlayer.this.mWidth, BroadcastPlayer.this.mHeight);
                    }
                    BroadcastPlayer.this.mPreset = optJSONObject3.optString("preset");
                    String optString3 = optJSONObject2.optString("url");
                    BroadcastPlayer.this.mLive = optJSONObject2.optBoolean("live");
                    if (BroadcastPlayer.this.mLive && BroadcastPlayer.this.mTimeshiftMode) {
                        optString3 = optJSONObject2.optString("timeshiftUrl");
                    }
                    if (BroadcastPlayer.this.mPreset == null || BroadcastPlayer.this.mPreset.length() < 3 || optString3 == null || optString3.length() < 3) {
                        BroadcastPlayer.this.setState(PlayerState.ERROR);
                        return;
                    }
                    if (BroadcastPlayer.this.mObserver != null) {
                        BroadcastPlayer.this.mObserver.onBroadcastLoaded(BroadcastPlayer.this.mLive, BroadcastPlayer.this.mWidth, BroadcastPlayer.this.mHeight);
                    }
                    if (BroadcastPlayer.this.mState != PlayerState.LOADING) {
                        return;
                    }
                    JSONObject optJSONObject4 = jSONObject2.optJSONObject("telemetry");
                    if (optJSONObject4 != null && (optString = optJSONObject4.optString("wssUrl")) != null && optString.startsWith("wss://")) {
                        BroadcastPlayer.this.mWssUrl = optString;
                        if (BroadcastPlayer.this.mTelemetryConnection == null) {
                            BroadcastPlayer.this.mTelemetryConnection = new WebSocketConnection(URI.create(BroadcastPlayer.this.mWssUrl), BroadcastPlayer.TELEMETRY_PROTOCOL, BroadcastPlayer.WEBSOCKET_USER_AGENT, BroadcastPlayer.this.mWebSocketObserver);
                        }
                    }
                    if ("flv".equals(BroadcastPlayer.this.mPreset)) {
                        BroadcastPlayer.this.initFlvPlayback(optString3, jSONObject2.optString("contentSessionId"), jSONObject2.optJSONArray("variants"));
                    } else if ("ll-hls".equals(BroadcastPlayer.this.mPreset)) {
                        if (BroadcastPlayer.this.mLive) {
                            BroadcastPlayer.this.initHlsPlayer(optString3, true);
                            return;
                        }
                        BroadcastPlayer.this.doHLSRequest(optString3);
                        BroadcastPlayer.this.initMediaPlayer(optString3);
                    } else if ("hls".equals(BroadcastPlayer.this.mPreset)) {
                        if (!BroadcastPlayer.this.mLive || !BroadcastPlayer.this.mTimeshiftMode) {
                            BroadcastPlayer.this.doHLSRequest(optString3);
                            BroadcastPlayer.this.initMediaPlayer(optString3);
                            return;
                        }
                        BroadcastPlayer.this.initHlsPlayer(optString3, false);
                    }
                }
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void variantCDNRequest(FlvVariant flvVariant) {
        if (this.mVariantCDNRequestRunning) {
            return;
        }
        this.mVariantCDNRequestRunning = true;
        FlvInfo flvInfo = this.mFlvInfo;
        JSONObject jSONObject = new JSONObject();
        try {
            jSONObject.put("resourceUri", this.mResourceUri);
            jSONObject.put("broadcastState", this.mAcceptedType);
            if (flvInfo != null && flvInfo.mContentSessionId != null) {
                jSONObject.put("contentSessionId", flvInfo.mContentSessionId);
            }
            JSONArray jSONArray = new JSONArray();
            jSONArray.put(new JSONObject().put("preset", "flv").put("variant", flvVariant.mId));
            jSONObject.put("criteria", jSONArray);
        } catch (Exception unused) {
        }
        doCDNRequest("https://cdn.bambuser.net/contentRequests", jSONObject, new RequestCallback() { // from class: com.bambuser.broadcaster.BroadcastPlayer.3
            @Override // com.bambuser.broadcaster.BroadcastPlayer.RequestCallback
            public void onResult(int i, JSONObject jSONObject2) {
                BroadcastPlayer.this.mVariantCDNRequestRunning = false;
                if (BroadcastPlayer.this.isPlaying()) {
                    if (jSONObject2 == null) {
                        Log.w(BroadcastPlayer.LOGTAG, "variant request failed, staying on current variant");
                        return;
                    }
                    JSONObject optJSONObject = jSONObject2.optJSONObject("match");
                    JSONObject optJSONObject2 = jSONObject2.optJSONObject("matchFor");
                    if (optJSONObject == null || optJSONObject2 == null || !optJSONObject2.optString("preset").equals("flv")) {
                        Log.w(BroadcastPlayer.LOGTAG, "variant request failed, staying on current variant");
                        return;
                    }
                    BroadcastPlayer.this.initFlvVariantPlayback(optJSONObject.optString("url"));
                }
            }
        });
    }

    private void benchmarkPayloadsCDNRequest() {
        if (this.mBenchmarkPayloadRequestRunning) {
            return;
        }
        this.mBenchmarkPayloadRequestRunning = true;
        doCDNRequest("https://cdn.bambuser.net/benchmarkPayloads", null, new RequestCallback() { // from class: com.bambuser.broadcaster.BroadcastPlayer.4
            @Override // com.bambuser.broadcaster.BroadcastPlayer.RequestCallback
            public void onResult(int i, JSONObject jSONObject) {
                JSONArray optJSONArray;
                BroadcastPlayer.this.mBenchmarkPayloadRequestRunning = false;
                if (BroadcastPlayer.this.mState == PlayerState.CLOSED || jSONObject == null || (optJSONArray = jSONObject.optJSONArray("payloads")) == null) {
                    return;
                }
                BroadcastPlayer.this.mBenchmarkPayloads.clear();
                for (int i2 = 0; i2 < optJSONArray.length(); i2++) {
                    JSONObject optJSONObject = optJSONArray.optJSONObject(i2);
                    if (optJSONObject != null) {
                        String optString = optJSONObject.optString("url");
                        long optLong = optJSONObject.optLong("bytes");
                        if (optString != null && optString.length() >= 5 && optLong > 0) {
                            BroadcastPlayer.this.mBenchmarkPayloads.add(new BenchmarkPayload(optString, optLong));
                        }
                    }
                }
                Collections.sort(BroadcastPlayer.this.mBenchmarkPayloads);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* JADX WARN: Type inference failed for: r0v2, types: [com.bambuser.broadcaster.BroadcastPlayer$5] */
    public void clientInfoRequest() {
        if (this.mClientInfoRequestRunning) {
            return;
        }
        this.mClientInfoRequestRunning = true;
        new AsyncTask<Void, Void, JSONObject>() { // from class: com.bambuser.broadcaster.BroadcastPlayer.5
            /* JADX INFO: Access modifiers changed from: protected */
            @Override // android.os.AsyncTask
            public JSONObject doInBackground(Void... voidArr) {
                try {
                    return new JSONObject((String) BackendApi.get(BroadcastPlayer.CLIENTINFO_URL).second);
                } catch (Exception unused) {
                    return new JSONObject();
                }
            }

            /* JADX INFO: Access modifiers changed from: protected */
            @Override // android.os.AsyncTask
            public void onPostExecute(JSONObject jSONObject) {
                BroadcastPlayer.this.mClientInfoRequestRunning = false;
                if (BroadcastPlayer.this.mState == PlayerState.CLOSED || jSONObject == null) {
                    return;
                }
                String optString = jSONObject.optString("countryCode");
                if (optString != null) {
                    BroadcastPlayer.this.mCountryCode = optString;
                }
                BroadcastPlayer.this.sendTelemetryClientInfo();
            }
        }.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, new Void[0]);
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* JADX WARN: Type inference failed for: r0v0, types: [com.bambuser.broadcaster.BroadcastPlayer$6] */
    public void doHLSRequest(final String str) {
        new AsyncTask<Void, Void, Map<String, Long>>() { // from class: com.bambuser.broadcaster.BroadcastPlayer.6
            /* JADX INFO: Access modifiers changed from: protected */
            @Override // android.os.AsyncTask
            public Map<String, Long> doInBackground(Void... voidArr) {
                Pair<Integer, String> pair = BackendApi.get(str);
                if (pair.second == null || ((String) pair.second).isEmpty()) {
                    return new HashMap();
                }
                return HlsParser.getBitrateMap(str, (String) pair.second);
            }

            /* JADX INFO: Access modifiers changed from: protected */
            @Override // android.os.AsyncTask
            public void onPostExecute(Map<String, Long> map) {
                BroadcastPlayer.this.mHlsBitrateMap.putAll(map);
            }
        }.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, new Void[0]);
    }

    /* JADX WARN: Type inference failed for: r7v0, types: [com.bambuser.broadcaster.BroadcastPlayer$7] */
    private void doCDNRequest(final String str, final JSONObject jSONObject, final RequestCallback requestCallback) {
        final HashMap hashMap = new HashMap();
        hashMap.put("Accept", "application/vnd.bambuser.cdn.v2+json");
        hashMap.put("X-Bambuser-ClientVersion", Broadcaster.getClientVersion(this.mContext));
        hashMap.put("X-Bambuser-ClientPlatform", "Android " + Build.VERSION.RELEASE);
        hashMap.put("X-Bambuser-ApplicationId", this.mApplicationId);
        final String str2 = jSONObject != null ? "POST" : null;
        new AsyncTask<Void, Void, Pair<Integer, JSONObject>>() { // from class: com.bambuser.broadcaster.BroadcastPlayer.7
            /* JADX INFO: Access modifiers changed from: protected */
            @Override // android.os.AsyncTask
            public Pair<Integer, JSONObject> doInBackground(Void... voidArr) {
                JSONObject jSONObject2;
                Pair<Integer, String> jsonEncodedRequest = BackendApi.jsonEncodedRequest(str, jSONObject, str2, hashMap);
                try {
                    jSONObject2 = new JSONObject((String) jsonEncodedRequest.second);
                } catch (Exception unused) {
                    jSONObject2 = null;
                }
                return new Pair<>(jsonEncodedRequest.first, jSONObject2);
            }

            /* JADX INFO: Access modifiers changed from: protected */
            @Override // android.os.AsyncTask
            public void onPostExecute(Pair<Integer, JSONObject> pair) {
                requestCallback.onResult(((Integer) pair.first).intValue(), (JSONObject) pair.second);
            }
        }.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, new Void[0]);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void considerVariant(boolean z) {
        if (!isPlaying() || this.mLinkTestActive) {
            return;
        }
        if (this.mHlsParser != null) {
            considerHlsVariant(z);
        } else if (this.mFlvInfo == null || this.mFlvInfo.mVariants.isEmpty()) {
        } else {
            considerFlvVariant(z);
        }
    }

    private void considerHlsVariant(boolean z) {
        if (this.mHlsParser == null || this.mHlsParser.getInternalVariantSelection()) {
            return;
        }
        final List<HlsVariant> variants = this.mHlsParser.getVariants();
        if (variants.size() < 2) {
            return;
        }
        final HlsVariant hlsVariant = variants.get(this.mHlsParser.getDownloadVariantIndex());
        int i = 0;
        if (z) {
            if (this.mLinkTestTime + this.mLinkTestBackoff > SystemClock.elapsedRealtime()) {
                return;
            }
            final int i2 = -1;
            while (i < variants.size()) {
                HlsVariant hlsVariant2 = variants.get(i);
                if (hlsVariant2.mBitrate > hlsVariant.mBitrate && (i2 == -1 || variants.get(i2).mBitrate > hlsVariant2.mBitrate)) {
                    i2 = i;
                }
                i++;
            }
            if (i2 == -1) {
                return;
            }
            if (this.mBenchmarkPayloads.isEmpty()) {
                benchmarkPayloadsCDNRequest();
                return;
            }
            final long j = variants.get(i2).mBitrate;
            String str = null;
            for (BenchmarkPayload benchmarkPayload : this.mBenchmarkPayloads) {
                if (str == null || benchmarkPayload.mBytes < (2 * j) / 8) {
                    str = benchmarkPayload.mUrl;
                }
            }
            if (str == null) {
                return;
            }
            this.mLinkTestActive = true;
            LinkTester.fetch(str, new LinkTester.Callback() { // from class: com.bambuser.broadcaster.BroadcastPlayer.8
                @Override // com.bambuser.broadcaster.LinkTester.Callback
                public void onDone(long j2) {
                    BroadcastPlayer.this.bumpLinkTestBackoff();
                    BroadcastPlayer.this.mLinkTestActive = false;
                    if (j2 > 0 && BroadcastPlayer.this.mState == PlayerState.PLAYING && BroadcastPlayer.this.mHlsParser != null && j2 + hlsVariant.mBitrate > j * BroadcastPlayer.NEXT_BITRATE_MARGIN) {
                        BroadcastPlayer.this.resetLinkTestBackoff();
                        Log.i(BroadcastPlayer.LOGTAG, "switching from " + hlsVariant.mBitrate + " to " + ((HlsVariant) variants.get(i2)).mBitrate);
                        BroadcastPlayer.this.mHlsParser.setDownloadVariantIndex(i2);
                    }
                }
            });
            return;
        }
        int i3 = -1;
        while (i < variants.size()) {
            HlsVariant hlsVariant3 = variants.get(i);
            if (hlsVariant3.mBitrate < hlsVariant.mBitrate && (i3 == -1 || variants.get(i3).mBitrate < hlsVariant3.mBitrate)) {
                i3 = i;
            }
            i++;
        }
        if (i3 > -1) {
            Log.i(LOGTAG, "switching from " + hlsVariant.mBitrate + " to " + variants.get(i3).mBitrate);
            this.mHlsParser.setDownloadVariantIndex(i3);
        }
    }

    private void considerFlvVariant(boolean z) {
        if (this.mFlvInfo == null || this.mFlvInfo.mVariants.isEmpty()) {
            return;
        }
        if (z) {
            if (this.mLinkTestTime + this.mLinkTestBackoff <= SystemClock.elapsedRealtime() && this.mFlvInfo.mActiveVariantIndex != -1) {
                String str = null;
                if (this.mFlvInfo.mActiveVariantIndex == this.mFlvInfo.mVariants.size() - 1) {
                    if (this.mBenchmarkPayloads.isEmpty()) {
                        benchmarkPayloadsCDNRequest();
                        return;
                    }
                    for (BenchmarkPayload benchmarkPayload : this.mBenchmarkPayloads) {
                        if (str == null || benchmarkPayload.mBytes < (this.mFlvInfo.mBitrate * 2) / 8) {
                            str = benchmarkPayload.mUrl;
                        }
                    }
                    if (str == null) {
                        return;
                    }
                    this.mLinkTestActive = true;
                    LinkTester.fetch(str, new LinkTester.Callback() { // from class: com.bambuser.broadcaster.BroadcastPlayer.9
                        @Override // com.bambuser.broadcaster.LinkTester.Callback
                        public void onDone(long j) {
                            BroadcastPlayer.this.bumpLinkTestBackoff();
                            BroadcastPlayer.this.mLinkTestActive = false;
                            if (j > 0 && BroadcastPlayer.this.mState == PlayerState.PLAYING && BroadcastPlayer.this.mFlvInfo != null && j + BroadcastPlayer.this.mFlvInfo.mVariants.get(BroadcastPlayer.this.mFlvInfo.mActiveVariantIndex).mBitrate > BroadcastPlayer.this.mFlvInfo.mBitrate * BroadcastPlayer.NEXT_BITRATE_MARGIN) {
                                BroadcastPlayer.this.resetLinkTestBackoff();
                                BroadcastPlayer.this.mFlvInfo.mActiveVariantIndex = -1;
                                BroadcastPlayer.this.initFlvVariantPlayback(BroadcastPlayer.this.mFlvInfo.mUrl);
                            }
                        }
                    });
                } else if (this.mBenchmarkPayloads.isEmpty()) {
                    benchmarkPayloadsCDNRequest();
                } else {
                    final int i = this.mFlvInfo.mVariants.get(this.mFlvInfo.mActiveVariantIndex).mBitrate;
                    final int i2 = this.mFlvInfo.mActiveVariantIndex + 1;
                    final int i3 = this.mFlvInfo.mVariants.get(i2).mBitrate;
                    for (BenchmarkPayload benchmarkPayload2 : this.mBenchmarkPayloads) {
                        if (str == null || benchmarkPayload2.mBytes < (i3 * 2) / 8) {
                            str = benchmarkPayload2.mUrl;
                        }
                    }
                    if (str == null) {
                        return;
                    }
                    this.mLinkTestActive = true;
                    LinkTester.fetch(str, new LinkTester.Callback() { // from class: com.bambuser.broadcaster.BroadcastPlayer.10
                        @Override // com.bambuser.broadcaster.LinkTester.Callback
                        public void onDone(long j) {
                            BroadcastPlayer.this.bumpLinkTestBackoff();
                            BroadcastPlayer.this.mLinkTestActive = false;
                            if (j > 0 && BroadcastPlayer.this.mState == PlayerState.PLAYING && BroadcastPlayer.this.mFlvInfo != null && j + i > i3) {
                                BroadcastPlayer.this.resetLinkTestBackoff();
                                BroadcastPlayer.this.mFlvInfo.mActiveVariantIndex = i2;
                                BroadcastPlayer.this.variantCDNRequest(BroadcastPlayer.this.mFlvInfo.mVariants.get(i2));
                            }
                        }
                    });
                }
            }
        } else if (this.mFlvInfo.mActiveVariantIndex != 0) {
            if (this.mFlvInfo.mActiveVariantIndex == -1) {
                this.mFlvInfo.mActiveVariantIndex = this.mFlvInfo.mVariants.size() - 1;
                variantCDNRequest(this.mFlvInfo.mVariants.get(this.mFlvInfo.mActiveVariantIndex));
                return;
            }
            this.mFlvInfo.mActiveVariantIndex--;
            variantCDNRequest(this.mFlvInfo.mVariants.get(this.mFlvInfo.mActiveVariantIndex));
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void bumpLinkTestBackoff() {
        this.mLinkTestTime = SystemClock.elapsedRealtime();
        if (this.mLinkTestBackoff + 10000 <= 60000) {
            this.mLinkTestBackoff += 10000;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void resetLinkTestBackoff() {
        this.mLinkTestBackoff = 0L;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void startTelemetryView() {
        if (this.mTelemetryConnection == null || !this.mTelemetryConnection.isConnected()) {
            return;
        }
        JSONObject jSONObject = new JSONObject();
        try {
            jSONObject.put("type", "startView");
            JSONObject jSONObject2 = new JSONObject();
            jSONObject2.put("resourceUri", this.mResourceUri);
            jSONObject2.put("applicationId", this.mApplicationId);
            jSONObject.put(MessengerShareContentUtility.ATTACHMENT_PAYLOAD, jSONObject2);
        } catch (Exception unused) {
        }
        this.mTelemetryConnection.sendString(jSONObject.toString());
    }

    private void stopTelemetryView() {
        if (this.mTelemetryConnection == null || !this.mTelemetryConnection.isConnected()) {
            return;
        }
        sendVideoConsumedReports();
        sendInterruptionReports();
        sendLatencyReports();
        JSONObject jSONObject = new JSONObject();
        try {
            jSONObject.put("type", "stopView");
            JSONObject jSONObject2 = new JSONObject();
            jSONObject2.put("resourceUri", this.mResourceUri);
            jSONObject2.put("applicationId", this.mApplicationId);
            jSONObject.put(MessengerShareContentUtility.ATTACHMENT_PAYLOAD, jSONObject2);
        } catch (Exception unused) {
        }
        this.mTelemetryConnection.sendString(jSONObject.toString());
    }

    private String telemetryLatencyModeString() {
        if (this.mLive) {
            if ("ll-hls".equals(this.mPreset) || "flv".equals(this.mPreset)) {
                return this.mLatencyMode == LatencyMode.HIGH ? "high" : "low";
            }
            return null;
        }
        return null;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void sendTelemetryClientInfo() {
        if (this.mCountryCode == null || this.mTelemetryConnection == null || !this.mTelemetryConnection.isConnected()) {
            return;
        }
        try {
            JSONObject jSONObject = new JSONObject();
            jSONObject.put("type", "clientInfo");
            JSONObject jSONObject2 = new JSONObject();
            jSONObject2.put("countryCode", this.mCountryCode);
            jSONObject.put(MessengerShareContentUtility.ATTACHMENT_PAYLOAD, jSONObject2);
            this.mTelemetryConnection.sendString(jSONObject.toString());
        } catch (Exception unused) {
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void sendVideoConsumedReports() {
        if (this.mVideoConsumedInterval <= 0 || !isPlaying() || this.mTelemetryConnection == null || !this.mTelemetryConnection.isConnected()) {
            return;
        }
        long elapsedRealtime = SystemClock.elapsedRealtime();
        long j = this.mLastTelemetryReportTime;
        long j2 = elapsedRealtime - j;
        if (this.mLastTelemetryReportTime > 0) {
            this.mLastTelemetryReportTime = elapsedRealtime;
        }
        long andSet = this.mConsumedBytes.getAndSet(0L);
        try {
            JSONArray jSONArray = new JSONArray();
            if (andSet > 0) {
                JSONObject jSONObject = new JSONObject();
                jSONObject.put("type", "videoConsumed");
                jSONObject.put("resourceUri", this.mResourceUri);
                jSONObject.put("applicationId", this.mApplicationId);
                jSONObject.put("bytes", andSet);
                jSONArray.put(jSONObject);
            } else if (this.mUseBitrateTelemetry && this.mActiveWidth > 0 && this.mActiveHeight > 0 && j > 0 && j2 > 0) {
                JSONObject jSONObject2 = new JSONObject();
                jSONObject2.put("type", "videoConsumed");
                jSONObject2.put("resourceUri", this.mResourceUri);
                jSONObject2.put("applicationId", this.mApplicationId);
                jSONObject2.put(TreadlyEventHelper.keyDuration, j2);
                jSONObject2.put("bitrateSource", "playlist");
                Map<String, Long> map = this.mHlsBitrateMap;
                Long l = map.get(this.mActiveWidth + "x" + this.mActiveHeight);
                if (l == null || l.longValue() <= 0) {
                    l = Long.valueOf((long) ((this.mActiveWidth * 2.712d * this.mActiveHeight) + 16667.0d));
                    jSONObject2.put("bitrateSource", "client");
                }
                jSONObject2.put("bitrate", l);
                jSONArray.put(jSONObject2);
            }
            if (jSONArray.length() < 1) {
                return;
            }
            JSONObject jSONObject3 = new JSONObject();
            jSONObject3.put("type", "telemetryReports");
            jSONObject3.put(MessengerShareContentUtility.ATTACHMENT_PAYLOAD, new JSONObject().put("reports", jSONArray));
            this.mTelemetryConnection.sendString(jSONObject3.toString());
        } catch (Exception unused) {
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void sendInterruptionReports() {
        if (this.mInterruptionsInterval <= 0 || !isPlaying() || this.mTelemetryConnection == null || !this.mTelemetryConnection.isConnected()) {
            return;
        }
        long elapsedRealtime = SystemClock.elapsedRealtime();
        long j = this.mLastInterruptionsReportTime;
        long j2 = elapsedRealtime - j;
        if (this.mLastInterruptionsReportTime > 0) {
            this.mLastInterruptionsReportTime = elapsedRealtime;
        }
        long j3 = this.mInterruptionsStartedCount;
        this.mInterruptionsStartedCount = 0L;
        if (this.mCurrentInterruptionStartTime > 0) {
            this.mInterruptionsDuration += elapsedRealtime - this.mCurrentInterruptionStartTime;
            this.mCurrentInterruptionStartTime = elapsedRealtime;
        }
        long j4 = this.mInterruptionsDuration;
        this.mInterruptionsDuration = 0L;
        if (j4 > j2) {
            Log.w(LOGTAG, "clamping interruption duration " + j4 + " to interval " + j2);
            j4 = Math.max(0L, Math.min(j2, j4));
        }
        try {
            JSONArray jSONArray = new JSONArray();
            if (j > 0 && j2 > 0) {
                JSONObject jSONObject = new JSONObject();
                jSONObject.put("type", "interruptions");
                jSONObject.put("resourceUri", this.mResourceUri);
                jSONObject.put("preset", this.mPreset);
                jSONObject.put("broadcastType", this.mLive ? "live" : "archived");
                jSONObject.put("timeshiftMode", this.mTimeshiftMode);
                jSONObject.put("latencyMode", telemetryLatencyModeString());
                if (this.mActiveWidth > 0 && this.mActiveHeight > 0) {
                    jSONObject.put(ViewHierarchyConstants.DIMENSION_WIDTH_KEY, this.mActiveWidth).put(ViewHierarchyConstants.DIMENSION_HEIGHT_KEY, this.mActiveHeight);
                }
                jSONObject.put("interruptionsStarted", j3);
                jSONObject.put("interruptionsDuration", j4);
                jSONObject.put("intervalDuration", j2);
                jSONArray.put(jSONObject);
            }
            if (jSONArray.length() < 1) {
                return;
            }
            JSONObject jSONObject2 = new JSONObject();
            jSONObject2.put("type", "telemetryReports");
            jSONObject2.put(MessengerShareContentUtility.ATTACHMENT_PAYLOAD, new JSONObject().put("reports", jSONArray));
            this.mTelemetryConnection.sendString(jSONObject2.toString());
        } catch (Exception unused) {
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void sendLatencyReports() {
        if (!this.mLive || this.mTimeshiftMode || this.mPlaybackController == null || this.mLatencyInterval <= 0 || !isPlaying() || this.mTelemetryConnection == null || !this.mTelemetryConnection.isConnected()) {
            return;
        }
        if ("ll-hls".equals(this.mPreset) && this.mHlsParser != null && this.mHlsParser.isEndOfData()) {
            return;
        }
        int latency = this.mPlaybackController.getLatency(getSyncedRealTime());
        int broadcasterUncertainty = this.mPlaybackController.getBroadcasterUncertainty();
        int i = this.mSyncedTimeUncertainty;
        if (i < 0 || broadcasterUncertainty < 0) {
            return;
        }
        try {
            JSONArray jSONArray = new JSONArray();
            JSONObject jSONObject = new JSONObject();
            jSONObject.put("type", "latency");
            jSONObject.put("resourceUri", this.mResourceUri);
            jSONObject.put("preset", this.mPreset);
            jSONObject.put("latencyMode", telemetryLatencyModeString());
            jSONObject.put("time", latency);
            jSONObject.put("uncertainty", i);
            jSONObject.put("broadcasterUncertainty", broadcasterUncertainty);
            jSONArray.put(jSONObject);
            JSONObject jSONObject2 = new JSONObject();
            jSONObject2.put("type", "telemetryReports");
            jSONObject2.put(MessengerShareContentUtility.ATTACHMENT_PAYLOAD, new JSONObject().put("reports", jSONArray));
            this.mTelemetryConnection.sendString(jSONObject2.toString());
        } catch (Exception unused) {
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void sendTelemetryHello() {
        if (this.mTelemetryConnection == null || !this.mTelemetryConnection.isConnected()) {
            return;
        }
        JSONObject jSONObject = new JSONObject();
        try {
            jSONObject.put("type", "hello");
            JSONObject jSONObject2 = new JSONObject();
            jSONObject2.put("clientPlatform", "Android " + Build.VERSION.RELEASE);
            jSONObject2.put("clientVersion", Broadcaster.getClientVersion(this.mContext));
            jSONObject2.put("deviceId", Settings.Secure.getString(this.mContext.getContentResolver(), "android_id"));
            jSONObject2.put("sessionId", APP_SESSION_ID);
            jSONObject2.put("playerInstanceId", this.mPlayerInstanceId);
            jSONObject.put(MessengerShareContentUtility.ATTACHMENT_PAYLOAD, jSONObject2);
        } catch (Exception unused) {
        }
        this.mTelemetryHelloMonotonicTime = SystemClock.elapsedRealtime();
        this.mTelemetryConnection.sendString(jSONObject.toString());
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void sendTelemetryTimeToFirstFrame() {
        if (this.mTelemetryConnection == null || !this.mTelemetryConnection.isConnected() || this.mLoadStartTime <= 0 || this.mFirstFrameTime <= this.mLoadStartTime) {
            return;
        }
        long j = this.mFirstFrameTime - this.mLoadStartTime;
        this.mFirstFrameTime = 0L;
        this.mLoadStartTime = 0L;
        try {
            JSONObject jSONObject = new JSONObject();
            jSONObject.put("type", "timeToFirstFrame");
            jSONObject.put("preset", this.mPreset);
            jSONObject.put("broadcastType", this.mLive ? "live" : "archived");
            jSONObject.put("timeshiftMode", this.mTimeshiftMode);
            jSONObject.put("latencyMode", telemetryLatencyModeString());
            jSONObject.put("time", j);
            JSONArray jSONArray = new JSONArray();
            jSONArray.put(jSONObject);
            JSONObject jSONObject2 = new JSONObject();
            jSONObject2.put("type", "telemetryReports");
            jSONObject2.put(MessengerShareContentUtility.ATTACHMENT_PAYLOAD, new JSONObject().put("reports", jSONArray));
            this.mTelemetryConnection.sendString(jSONObject2.toString());
        } catch (Exception unused) {
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void initFlvPlayback(String str, String str2, JSONArray jSONArray) {
        int i;
        LinkedList linkedList = new LinkedList();
        if (jSONArray != null) {
            i = 0;
            for (int i2 = 0; i2 < jSONArray.length(); i2++) {
                JSONObject optJSONObject = jSONArray.optJSONObject(i2);
                if (optJSONObject != null) {
                    String optString = optJSONObject.optString("id");
                    String optString2 = optJSONObject.optString("size");
                    int optInt = optJSONObject.optInt("bitrate");
                    if (optJSONObject.optBoolean("original")) {
                        if (optInt > 0) {
                            i = optInt;
                        }
                    } else if (optString.length() >= 1 && optInt > 0) {
                        try {
                            String[] split = optString2.split("x", 2);
                            int parseInt = Integer.parseInt(split[0]);
                            int parseInt2 = Integer.parseInt(split[1]);
                            if (parseInt >= 16 && parseInt2 >= 16) {
                                linkedList.add(new FlvVariant(optString, parseInt, parseInt2, optInt));
                            }
                        } catch (Exception unused) {
                        }
                    }
                }
            }
            Collections.sort(linkedList);
        } else {
            i = 0;
        }
        this.mFlvInfo = new FlvInfo(str, i, str2, linkedList);
        this.mUseBitrateTelemetry = false;
        setState(PlayerState.BUFFERING);
        this.mPlaybackController = new PlaybackController(this.mLive, this.mPlaybackControllerObserver);
        this.mPlaybackController.setLargeBufferMode(this.mLatencyMode == LatencyMode.HIGH);
        this.mPlaybackController.setAudioVolume(this.mAudioVolume);
        this.mPlaybackController.setSurface(currentSurface());
        runFlvThread(str);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void initFlvVariantPlayback(String str) {
        if (str == null || str.length() < 5) {
            setState(PlayerState.ERROR);
            return;
        }
        if (this.mFlvReaderThread != null) {
            this.mFlvReaderThread.setController(null);
        }
        this.mFlvReaderThread = null;
        if (this.mPlaybackController == null || this.mPlaybackController.isEndOfData()) {
            return;
        }
        runFlvThread(str);
    }

    private void runFlvThread(String str) {
        this.mFlvReaderThread = new FlvReaderThread(str);
        this.mFlvReaderThread.setController(this.mPlaybackController);
        this.mFlvReaderThread.start();
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes.dex */
    public class FlvReaderThread extends Thread {
        private volatile PlaybackController mController;
        private final String mUrl;

        FlvReaderThread(String str) {
            super("FlvReaderThread");
            this.mController = null;
            this.mUrl = str;
        }

        @Override // java.lang.Thread, java.lang.Runnable
        public void run() {
            FlvParser flvParser = new FlvParser();
            flvParser.setHandler(this.mController);
            byte[] bArr = new byte[32768];
            try {
                HttpURLConnection httpURLConnection = (HttpURLConnection) new URL(this.mUrl).openConnection();
                if (httpURLConnection instanceof HttpsURLConnection) {
                    ((HttpsURLConnection) httpURLConnection).setSSLSocketFactory(ModernTlsSocketFactory.getInstance());
                }
                httpURLConnection.setConnectTimeout(10000);
                httpURLConnection.setReadTimeout(10000);
                httpURLConnection.connect();
                InputStream inputStream = httpURLConnection.getInputStream();
                while (true) {
                    if (this.mController == null || (BroadcastPlayer.this.mState != PlayerState.PLAYING && BroadcastPlayer.this.mState != PlayerState.BUFFERING)) {
                        break;
                    }
                    int read = inputStream.read(bArr);
                    if (read >= 0) {
                        if (read == 0) {
                            yield();
                        } else {
                            synchronized (this) {
                                if (this.mController != null) {
                                    BroadcastPlayer.this.mConsumedBytes.addAndGet(read);
                                    flvParser.handleData(bArr, 0, read);
                                }
                            }
                            break;
                        }
                    } else {
                        synchronized (this) {
                            if (this.mController != null) {
                                this.mController.endOfData();
                            }
                        }
                    }
                }
                inputStream.close();
                httpURLConnection.disconnect();
            } catch (Exception e) {
                Log.w(BroadcastPlayer.LOGTAG, "connection exception: " + e);
                BroadcastPlayer.this.mMainHandler.post(new Runnable() { // from class: com.bambuser.broadcaster.BroadcastPlayer.FlvReaderThread.1
                    @Override // java.lang.Runnable
                    public void run() {
                        synchronized (FlvReaderThread.this) {
                            if (FlvReaderThread.this.mController != null && BroadcastPlayer.this.mState != PlayerState.CLOSED && BroadcastPlayer.this.mState != PlayerState.COMPLETED) {
                                BroadcastPlayer.this.setState(PlayerState.ERROR);
                            }
                        }
                    }
                });
            }
            flvParser.setHandler(null);
        }

        synchronized void setController(PlaybackController playbackController) {
            this.mController = playbackController;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void handleIncomingTelemetryPacket(JSONObject jSONObject) {
        if (this.mTelemetryConnection == null || jSONObject == null || jSONObject.isNull("type")) {
            return;
        }
        String optString = jSONObject.optString("type");
        if ("telemetryConfig".equals(optString)) {
            handleTelemetryConfig(jSONObject.optJSONObject(MessengerShareContentUtility.ATTACHMENT_PAYLOAD));
        } else if ("viewers".equals(optString)) {
            handleTelemetryViewers(jSONObject.optJSONObject(MessengerShareContentUtility.ATTACHMENT_PAYLOAD));
        } else if ("clock".equals(optString)) {
            handleTelemetryClock(jSONObject.optJSONObject(MessengerShareContentUtility.ATTACHMENT_PAYLOAD));
        }
    }

    private void handleTelemetryConfig(JSONObject jSONObject) {
        if (jSONObject == null) {
            return;
        }
        JSONObject optJSONObject = jSONObject.optJSONObject("videoConsumed");
        if (optJSONObject != null) {
            this.mVideoConsumedInterval = optJSONObject.optInt("interval");
        }
        this.mMainHandler.removeCallbacks(this.mVideoConsumedRunnable);
        if (this.mVideoConsumedInterval > 0) {
            this.mMainHandler.postDelayed(this.mVideoConsumedRunnable, this.mVideoConsumedInterval);
        }
        JSONObject optJSONObject2 = jSONObject.optJSONObject("interruptions");
        if (optJSONObject2 != null) {
            this.mInterruptionsInterval = optJSONObject2.optInt("interval");
        }
        this.mMainHandler.removeCallbacks(this.mInterruptionsRunnable);
        if (this.mInterruptionsInterval > 0) {
            this.mMainHandler.postDelayed(this.mInterruptionsRunnable, this.mInterruptionsInterval);
        }
        JSONObject optJSONObject3 = jSONObject.optJSONObject("latency");
        if (optJSONObject3 != null) {
            this.mLatencyInterval = optJSONObject3.optInt("interval");
        }
        this.mMainHandler.removeCallbacks(this.mLatencyRunnable);
        if (this.mLatencyInterval <= 0 || !this.mLive || this.mTimeshiftMode) {
            return;
        }
        this.mMainHandler.postDelayed(this.mLatencyRunnable, this.mLatencyInterval);
    }

    private void handleTelemetryViewers(JSONObject jSONObject) {
        if (jSONObject == null || this.mViewerCountObserver == null) {
            return;
        }
        this.mViewerCountObserver.onCurrentViewersUpdated(jSONObject.optLong("current"));
        this.mViewerCountObserver.onTotalViewersUpdated(jSONObject.optLong("total"));
    }

    private void handleTelemetryClock(JSONObject jSONObject) {
        if (jSONObject == null) {
            return;
        }
        long elapsedRealtime = SystemClock.elapsedRealtime();
        long optLong = jSONObject.optLong("sent", -1L);
        long optLong2 = jSONObject.optLong("responseDelay", -1L);
        String optString = jSONObject.optString("inResponseTo", null);
        if (optLong <= 0 || optLong2 < 0 || optString == null || !optString.equals("hello")) {
            return;
        }
        long j = elapsedRealtime - this.mTelemetryHelloMonotonicTime;
        if (optLong2 > j) {
            Log.w(LOGTAG, "delay " + optLong2 + " larger than measured rtt " + j + ", sync rejected!");
            return;
        }
        long j2 = j - optLong2;
        this.mSyncedOffsetFromMonotonic = (optLong + (j2 / 2)) - elapsedRealtime;
        this.mSyncedTimeUncertainty = ((int) j2) / 2;
    }

    private long getSyncedRealTime() {
        return SystemClock.elapsedRealtime() + this.mSyncedOffsetFromMonotonic;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void initMediaPlayer(String str) {
        this.mUseBitrateTelemetry = true;
        this.mMediaPlayer = new NiceMediaPlayer();
        this.mMediaPlayer.setObserver(this.mMediaPlayerObserver);
        setState(PlayerState.BUFFERING);
        this.mMediaPlayer.init(currentSurface(), str);
        this.mMediaPlayer.setAudioVolume(this.mAudioVolume);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void initHlsPlayer(String str, boolean z) {
        this.mUseBitrateTelemetry = false;
        setState(PlayerState.BUFFERING);
        this.mPlaybackController = new PlaybackController(z, this.mPlaybackControllerObserver);
        this.mPlaybackController.setLargeBufferMode(this.mLatencyMode == LatencyMode.HIGH);
        this.mPlaybackController.setAudioVolume(this.mAudioVolume);
        this.mPlaybackController.setSurface(currentSurface());
        this.mHlsParser = new HlsParser();
        this.mHlsParser.setCaptureTimePairing(z);
        this.mHlsParser.setInternalVariantSelection(!z);
        this.mHlsParser.setRealtimeMode(z);
        this.mHlsParser.setLargeBufferMode(this.mLatencyMode == LatencyMode.HIGH);
        this.mHlsParser.setObserver(this.mHlsParserObserver);
        this.mHlsParser.setHandler(this.mPlaybackController);
        this.mHlsParser.playUrl(str);
    }

    private Surface currentSurface() {
        SurfaceTexture surfaceTexture;
        if (this.mSurfaceView != null) {
            SurfaceHolder holder = this.mSurfaceView.getHolder();
            Surface surface = holder != null ? holder.getSurface() : null;
            if (surface != null && surface.isValid()) {
                return surface;
            }
        }
        if (this.mTextureView != null && (surfaceTexture = this.mTextureView.getSurfaceTexture()) != null) {
            return new Surface(surfaceTexture);
        }
        if (this.mSurface == null || !this.mSurface.isValid()) {
            return null;
        }
        return this.mSurface;
    }

    private void clearConsumers() {
        if (this.mSurfaceView != null) {
            this.mSurfaceView.getHolder().removeCallback(this.mSurfaceObserver);
        }
        if (this.mSurfaceView != null && this.mKeepingScreenOn) {
            this.mSurfaceView.setKeepScreenOn(false);
        }
        this.mSurfaceView = null;
        if (this.mTextureView != null) {
            this.mTextureView.setSurfaceTextureListener(null);
        }
        if (this.mTextureView != null && this.mKeepingScreenOn) {
            this.mTextureView.setKeepScreenOn(false);
        }
        this.mTextureView = null;
        this.mKeepingScreenOn = false;
        this.mSurface = null;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void setSurfaceInternal(Surface surface) {
        if (this.mPlaybackController != null) {
            this.mPlaybackController.setSurface(surface);
        }
        if (this.mMediaPlayer != null) {
            this.mMediaPlayer.setSurface(surface);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes.dex */
    public static class FlvInfo {
        int mActiveVariantIndex = -1;
        final int mBitrate;
        final String mContentSessionId;
        final String mUrl;
        final List<FlvVariant> mVariants;

        FlvInfo(String str, int i, String str2, List<FlvVariant> list) {
            this.mUrl = str;
            this.mBitrate = i;
            this.mContentSessionId = str2;
            this.mVariants = list;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes.dex */
    public static class FlvVariant implements Comparable<FlvVariant> {
        final int mBitrate;
        final int mHeight;
        final String mId;
        final int mWidth;

        FlvVariant(String str, int i, int i2, int i3) {
            this.mId = str;
            this.mWidth = i;
            this.mHeight = i2;
            this.mBitrate = i3;
        }

        @Override // java.lang.Comparable
        public int compareTo(FlvVariant flvVariant) {
            return this.mBitrate - flvVariant.mBitrate;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes.dex */
    public static class BenchmarkPayload implements Comparable<BenchmarkPayload> {
        final long mBytes;
        final String mUrl;

        BenchmarkPayload(String str, long j) {
            this.mUrl = str;
            this.mBytes = j;
        }

        @Override // java.lang.Comparable
        public int compareTo(BenchmarkPayload benchmarkPayload) {
            return Long.signum(this.mBytes - benchmarkPayload.mBytes);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean shouldTelemetryReconnect() {
        return this.mTelemetryConnection == null && isPlaying() && this.mWssUrl != null;
    }

    private static String createRandomId() {
        Random random = new Random();
        StringBuilder sb = new StringBuilder(16);
        for (int i = 0; i < 16; i++) {
            sb.append(HEXCHARS[random.nextInt(HEXCHARS.length)]);
        }
        return sb.toString();
    }
}
