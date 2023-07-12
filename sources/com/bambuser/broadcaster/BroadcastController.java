package com.bambuser.broadcaster;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.location.Location;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.os.PowerManager;
import android.util.Log;
import android.util.Pair;
import com.bambuser.broadcaster.Capturer;
import com.bambuser.broadcaster.LocationController;
import com.bambuser.broadcaster.MovinoConnectionHandler;
import com.bambuser.broadcaster.QualityRater;
import com.bambuser.broadcaster.RawPacket;
import com.facebook.appevents.AppEventsConstants;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;
import org.json.JSONObject;

/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: classes.dex */
public final class BroadcastController extends MovinoConnectionHandler implements LocationController.Observer, SharedPreferences.OnSharedPreferenceChangeListener {
    private static final String LOGTAG = "BroadcastController";
    private static final int UPLOAD_SEND_BUFFER_SIZE = 65536;
    private static final String WAKELOCKTAG = "bambuser:BroadcastController";
    private volatile BroadcastStatus mBroadcastStatus;
    private long mBroadcastTicketExpireTime;
    private JSONObject mBroadcastTicketResponse;
    private final Capturer.CameraInterface mCameraInterface;
    private final AtomicBoolean mCanStart;
    private final AtomicReference<Capturer> mCapturer;
    private final AtomicReference<MovinoData> mComplementData;
    private volatile boolean mComplementFailed;
    private final Context mContext;
    private final AtomicReference<PowerManager.WakeLock> mCpuWakeLock;
    private final AtomicReference<MovinoData> mCurrentlyUploading;
    private final Capturer.EncodeInterface mEncodeInterface;
    private final AtomicInteger mFramesInQueue;
    private final LocationController mLocationController;
    private final Handler mMainHandler;
    private volatile int mMaxFramesInQueue;
    private final MediaWriter mMediaWriter;
    private volatile int mMinFramesInQueue;
    private volatile int mMovinoComplementRotation;
    private volatile int mMovinoLiveRotation;
    private final Observer mObserver;
    private final AtomicBoolean mOfflineMode;
    private final AtomicReference<QualityRater> mQualityRater;
    private volatile String mReconnectHost;
    private volatile boolean mReconnectHttps;
    private volatile int mReconnectPort;
    private volatile String mReconnectToken;
    private volatile int mRetryCount;
    private volatile boolean mShouldStartBroadcast;
    private final Timer mStorageCheckTimer;
    private final TalkbackController mTalkbackController;
    private boolean mTicketRequestRunning;
    private long mUplinkTestBitrate;
    private long mUplinkTestStartTime;
    private final AtomicReference<WifiManager.WifiLock> mWifiLock;

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes.dex */
    public interface Observer {
        void addChatMessage(String str);

        void onBroadcastTicketResponse(boolean z, JSONObject jSONObject);

        void onCameraError(CameraError cameraError);

        void onCameraPreviewStateChanged();

        void onCameraResolutionChanged(int i, int i2);

        void onCameraSettingChanged();

        void onComplementWritingFailed();

        void onConnectionError(ConnectionError connectionError, String str);

        void onConnectionStatusChange(BroadcastStatus broadcastStatus);

        void onCurrentViewersUpdated(long j);

        void onEncoderError(Exception exc);

        String onGetClientVersion();

        void onLocalMediaClosed(String str);

        void onLocalMediaError();

        void onNewComplementDataToUpload(MovinoData movinoData);

        void onPrivacySettingChanged();

        void onRemainingTimeEstimated(long j);

        void onSmallComplementDataUploaded();

        void onStreamHealthUpdate(int i, int i2);

        void onTitleSettingChanged();

        void onTooHighResolutionDetected();

        void onTotalViewersUpdated(long j);

        void showConnectionDialog(MovinoConnectionHandler.Dialog dialog);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public BroadcastController(Context context, SettingsReader settingsReader, Observer observer) {
        super(settingsReader);
        this.mCameraInterface = new Capturer.CameraInterface() { // from class: com.bambuser.broadcaster.BroadcastController.6
            @Override // com.bambuser.broadcaster.Capturer.CameraInterface
            public String onGetCameraId() {
                String cameraId = BroadcastController.this.mSettings.getCameraId();
                if (cameraId.equals("-1") || DeviceInfoHandler.hasPermission(BroadcastController.this.mContext, "android.permission.CAMERA")) {
                    return cameraId;
                }
                Log.w(BroadcastController.LOGTAG, "Missing CAMERA permission");
                return "-1";
            }

            @Override // com.bambuser.broadcaster.Capturer.CameraInterface
            public Resolution onGetResolution() {
                CamInfo findCamInfo = DeviceInfoHandler.findCamInfo(BroadcastController.this.mContext, BroadcastController.this.mSettings.getCameraId());
                Resolution defaultResolution = DeviceInfoHandler.getDefaultResolution(findCamInfo);
                if (findCamInfo != null && BroadcastController.this.mSettings.resolutionStored()) {
                    Resolution resolution = BroadcastController.this.mSettings.getResolution(BroadcastController.this.mContext);
                    for (Resolution resolution2 : findCamInfo.getPreviewList()) {
                        if (resolution2.equals(resolution)) {
                            return resolution;
                        }
                        if (resolution2.compareTo(resolution) <= 0) {
                            defaultResolution = resolution2;
                        }
                    }
                }
                onResolutionChangeNeeded(defaultResolution);
                return defaultResolution;
            }

            @Override // com.bambuser.broadcaster.Capturer.CameraInterface
            public int onGetFrameRate() {
                return BroadcastController.this.mSettings.getOutputFrameRate();
            }

            @Override // com.bambuser.broadcaster.Capturer.CameraInterface
            public int onGetPreviewRotation() {
                return BroadcastController.this.mSettings.getPreviewRotation();
            }

            @Override // com.bambuser.broadcaster.Capturer.CameraInterface
            public int onGetCaptureRotation() {
                return BroadcastController.this.mSettings.getCaptureRotation();
            }

            @Override // com.bambuser.broadcaster.Capturer.CameraInterface
            public Resolution onGetCroppedResolution() {
                return BroadcastController.this.mSettings.getCroppedResolution(BroadcastController.this.mContext);
            }

            @Override // com.bambuser.broadcaster.Capturer.CameraInterface
            public void onResolutionChangeNeeded(Resolution resolution) {
                BroadcastController.this.mSettings.unregisterChangeListener(BroadcastController.this);
                boolean resolutionStored = BroadcastController.this.mSettings.resolutionStored();
                BroadcastController.this.mSettings.setResolution(resolution);
                BroadcastController.this.mSettings.registerChangeListener(BroadcastController.this);
                Capturer capturer = (Capturer) BroadcastController.this.mCapturer.get();
                if (capturer != null && capturer.isCapturing()) {
                    BroadcastController.this.sendStreamInfo();
                }
                if (resolutionStored) {
                    BroadcastController.this.mObserver.onCameraResolutionChanged(resolution.getWidth(), resolution.getHeight());
                }
            }

            @Override // com.bambuser.broadcaster.Capturer.CameraInterface
            public void onCameraPreviewStateChanged(boolean z) {
                BroadcastController.this.mObserver.onCameraPreviewStateChanged();
            }

            @Override // com.bambuser.broadcaster.Capturer.CameraInterface
            public void onCameraError(CameraError cameraError) {
                BroadcastController broadcastController = BroadcastController.this;
                broadcastController.sendLogMessage("BroadcastController camera error: " + cameraError);
                BroadcastController.this.mObserver.onCameraError(cameraError);
            }

            @Override // com.bambuser.broadcaster.Capturer.CameraInterface
            public boolean onGetCaptureSounds() {
                return BroadcastController.this.mSettings.getBoolean("capture_sound");
            }
        };
        this.mEncodeInterface = new Capturer.EncodeInterface() { // from class: com.bambuser.broadcaster.BroadcastController.7
            private final RawPacket.Observer mPacketObserver = new RawPacket.Observer() { // from class: com.bambuser.broadcaster.BroadcastController.7.1
                @Override // com.bambuser.broadcaster.RawPacket.Observer
                public void onBlockEnqueued() {
                    BroadcastController.this.mFramesInQueue.incrementAndGet();
                }

                @Override // com.bambuser.broadcaster.RawPacket.Observer
                public void onBlockSent() {
                    BroadcastController.this.mFramesInQueue.decrementAndGet();
                }
            };
            private int mSendQueueLimit = 16000;
            private int mFrameCount = 0;
            private int mSentVideoPacketCount = 0;

            @Override // com.bambuser.broadcaster.Capturer.EncodeInterface
            public Capturer.EncodeInterface.AudioFormat onGetAudioFormat() {
                if (!BroadcastController.this.mSettings.getString("audio_quality").equals("off")) {
                    if (!DeviceInfoHandler.hasPermission(BroadcastController.this.mContext, "android.permission.RECORD_AUDIO")) {
                        Log.w(BroadcastController.LOGTAG, "Missing RECORD_AUDIO permission");
                        return Capturer.EncodeInterface.AudioFormat.NONE;
                    }
                    return Capturer.EncodeInterface.AudioFormat.AAC;
                }
                return Capturer.EncodeInterface.AudioFormat.NONE;
            }

            @Override // com.bambuser.broadcaster.Capturer.EncodeInterface
            public int onGetAudioSampleRate() {
                return BroadcastController.this.getDesiredAudioSampleRate();
            }

            @Override // com.bambuser.broadcaster.Capturer.EncodeInterface
            public boolean onGetTalkbackMixin() {
                return BroadcastController.this.mSettings.getBoolean("talkback_mixin");
            }

            @Override // com.bambuser.broadcaster.Capturer.EncodeInterface
            public int onGetEncodedAudioChannelCount() {
                long j = BroadcastController.this.mSettings.getLong("encoded_audio_channels");
                if (j > 1) {
                    return (int) j;
                }
                return 1;
            }

            @Override // com.bambuser.broadcaster.Capturer.EncodeInterface
            public boolean onGetCaptureSounds() {
                return BroadcastController.this.mSettings.getBoolean("capture_sound");
            }

            @Override // com.bambuser.broadcaster.Capturer.EncodeInterface
            public String onGetCameraId() {
                String cameraId = BroadcastController.this.mSettings.getCameraId();
                if (cameraId.equals("-1") || DeviceInfoHandler.hasPermission(BroadcastController.this.mContext, "android.permission.CAMERA")) {
                    return cameraId;
                }
                Log.w(BroadcastController.LOGTAG, "Missing CAMERA permission");
                return "-1";
            }

            @Override // com.bambuser.broadcaster.Capturer.EncodeInterface
            public boolean onStartAutoRes() {
                return BroadcastController.this.mSettings.getBoolean("auto_resolution");
            }

            @Override // com.bambuser.broadcaster.Capturer.EncodeInterface
            @SuppressLint({"MissingPermission"})
            public NetworkInfo onGetNetworkInfo() {
                try {
                    return ((ConnectivityManager) BroadcastController.this.mContext.getSystemService("connectivity")).getActiveNetworkInfo();
                } catch (Exception unused) {
                    return null;
                }
            }

            @Override // com.bambuser.broadcaster.Capturer.EncodeInterface
            public long onGetUplinkBitrate() {
                if (System.currentTimeMillis() < BroadcastController.this.mUplinkTestStartTime + 60000) {
                    return BroadcastController.this.mUplinkTestBitrate;
                }
                return 0L;
            }

            @Override // com.bambuser.broadcaster.Capturer.EncodeInterface
            public Resolution onGetMaxLiveResolution() {
                return BroadcastController.this.mSettings.getMaxLiveResolution();
            }

            @Override // com.bambuser.broadcaster.Capturer.EncodeInterface
            public void onAudioInitialized(long j, int i, int i2, int i3) {
                Connection readyConnection = BroadcastController.this.getReadyConnection();
                if (readyConnection != null) {
                    readyConnection.send(new MovinoPacket(33, 12).writeUint32(j).writeUint32(i).writeUint32(i2));
                }
                BroadcastController.this.mMediaWriter.addAudioTrack(i, i2);
                this.mSendQueueLimit = i3;
            }

            @Override // com.bambuser.broadcaster.Capturer.EncodeInterface
            public void onVideoInitialized(InfoFrame infoFrame) {
                BroadcastController.this.sendStreamInfo(infoFrame.mRotation);
                BroadcastController.this.mMediaWriter.addVideoTrack(infoFrame.mWidth, infoFrame.mHeight, infoFrame.mRotation);
            }

            @Override // com.bambuser.broadcaster.Capturer.EncodeInterface
            public boolean onCanSendFrame() {
                if (BroadcastController.this.mBroadcastStatus == BroadcastStatus.RECONNECTING) {
                    return false;
                }
                this.mFrameCount++;
                int i = BroadcastController.this.mFramesInQueue.get();
                QualityRater qualityRater = (QualityRater) BroadcastController.this.mQualityRater.get();
                boolean z = i < BroadcastController.this.mMinFramesInQueue;
                if (!z && i < BroadcastController.this.mMaxFramesInQueue && qualityRater != null && qualityRater.getSeconds() > 3) {
                    int latestQuality = qualityRater.getLatestQuality();
                    z = this.mFrameCount % (latestQuality > 3 ? (int) Math.ceil(100.0d / ((double) latestQuality)) : 30) == 0;
                }
                return z && BroadcastController.this.getReadyConnection() != null;
            }

            @Override // com.bambuser.broadcaster.Capturer.EncodeInterface
            public boolean onCanSendAudio() {
                Connection readyConnection;
                return (BroadcastController.this.mBroadcastStatus == BroadcastStatus.RECONNECTING || (readyConnection = BroadcastController.this.getReadyConnection()) == null || readyConnection.getSendQueueSize() >= this.mSendQueueLimit) ? false : true;
            }

            @Override // com.bambuser.broadcaster.Capturer.EncodeInterface
            public boolean onCanWriteComplement() {
                return (BroadcastController.this.mComplementFailed || BroadcastController.this.mComplementData.get() == null) ? false : true;
            }

            @Override // com.bambuser.broadcaster.Capturer.EncodeInterface
            public boolean onCanWriteLocal() {
                return BroadcastController.this.mMediaWriter.acceptsData();
            }

            @Override // com.bambuser.broadcaster.Capturer.EncodeInterface
            public void onLiveRotation(long j, int i) {
                if (BroadcastController.this.mBroadcastStatus == BroadcastStatus.RECONNECTING || BroadcastController.this.mMovinoLiveRotation == i) {
                    return;
                }
                BroadcastController.this.mMovinoLiveRotation = i;
                Connection readyConnection = BroadcastController.this.getReadyConnection();
                if (readyConnection != null) {
                    readyConnection.send(MovinoUtils.createVideoTransformPacket(j, i));
                }
                BroadcastController.this.sendStreamInfo(i);
            }

            @Override // com.bambuser.broadcaster.Capturer.EncodeInterface
            public void onSendData(boolean z, long j, int i, ByteBuffer byteBuffer, boolean z2) {
                Connection readyConnection = BroadcastController.this.getReadyConnection();
                if (readyConnection == null) {
                    return;
                }
                MovinoPacket movinoPacket = new MovinoPacket(BroadcastController.this.mByteBufferPool, i, byteBuffer.remaining() + 4, z ? this.mPacketObserver : null);
                if (z2) {
                    movinoPacket.writeUint32(j);
                }
                movinoPacket.write(byteBuffer);
                if (z) {
                    if (this.mSentVideoPacketCount % 30 == 0) {
                        long syncedRealtime = BroadcastController.this.getSyncedRealtime();
                        int syncedRealtimeUncertainty = BroadcastController.this.getSyncedRealtimeUncertainty();
                        Capturer capturer = BroadcastController.this.getCapturer();
                        if (syncedRealtimeUncertainty >= 0 && capturer != null && capturer.isCapturing()) {
                            readyConnection.send(MovinoUtils.createCaptureTimePacket(syncedRealtime, syncedRealtimeUncertainty, capturer.getCaptureDuration()));
                        }
                    }
                    this.mSentVideoPacketCount++;
                }
                readyConnection.send(movinoPacket);
            }

            @Override // com.bambuser.broadcaster.Capturer.EncodeInterface
            public void onSendLogMessage(String str) {
                BroadcastController.this.sendLogMessage(str);
            }

            @Override // com.bambuser.broadcaster.Capturer.EncodeInterface
            public void onComplementRotation(long j, int i) {
                if (BroadcastController.this.mMovinoComplementRotation == i) {
                    return;
                }
                BroadcastController.this.mMovinoComplementRotation = i;
                write(MovinoUtils.createVideoTransformPacket(j, i));
            }

            @Override // com.bambuser.broadcaster.Capturer.EncodeInterface
            public void onComplementData(boolean z, long j, int i, ByteBuffer byteBuffer, boolean z2) {
                if (BroadcastController.this.mComplementFailed) {
                    return;
                }
                MovinoPacket movinoPacket = new MovinoPacket(BroadcastController.this.mByteBufferPool, i, byteBuffer.remaining() + 4, null);
                if (z2) {
                    movinoPacket.writeUint32(j);
                }
                movinoPacket.write(byteBuffer);
                write(movinoPacket);
            }

            @Override // com.bambuser.broadcaster.Capturer.EncodeInterface
            public void onLocalData(long j, int i, ByteBuffer byteBuffer) {
                if (BroadcastController.this.mMediaWriter.acceptsData() && !BroadcastController.this.mMediaWriter.write(i, byteBuffer, j)) {
                    String close = BroadcastController.this.mMediaWriter.close();
                    BroadcastController.this.mObserver.onLocalMediaError();
                    BroadcastController.this.mObserver.onLocalMediaClosed(close);
                }
            }

            @Override // com.bambuser.broadcaster.Capturer.EncodeInterface
            public void onPreviewTaken(byte[] bArr, int i, int i2) {
                MovinoData currentData = BroadcastController.this.getCurrentData();
                if (currentData != null) {
                    currentData.setPreview(bArr, 0, i, i2);
                    currentData.store();
                }
            }

            @Override // com.bambuser.broadcaster.Capturer.EncodeInterface
            public void onRawDataHandled(int i, boolean z) {
                QualityRater qualityRater = (QualityRater) BroadcastController.this.mQualityRater.get();
                if (qualityRater != null) {
                    qualityRater.addBytes(i, z);
                }
            }

            @Override // com.bambuser.broadcaster.Capturer.EncodeInterface
            public void onEncoderError(Exception exc) {
                BroadcastController.this.mObserver.onEncoderError(exc);
            }

            private void write(MovinoPacket movinoPacket) {
                MovinoData movinoData = (MovinoData) BroadcastController.this.mComplementData.get();
                if (movinoData == null) {
                    BroadcastController.this.mComplementFailed = true;
                }
                if (!BroadcastController.this.mComplementFailed) {
                    BroadcastController.this.mComplementFailed = !movinoData.write(movinoPacket);
                    if (BroadcastController.this.mComplementFailed) {
                        BroadcastController.this.mObserver.onComplementWritingFailed();
                    }
                }
                movinoPacket.releaseToPool();
            }
        };
        this.mStorageCheckTimer = new Timer("StorageCheckTimer");
        this.mMediaWriter = new MediaWriter();
        this.mTalkbackController = new TalkbackController();
        this.mCapturer = new AtomicReference<>();
        this.mFramesInQueue = new AtomicInteger(0);
        this.mMainHandler = new Handler(Looper.getMainLooper());
        this.mBroadcastStatus = BroadcastStatus.IDLE;
        this.mComplementFailed = false;
        this.mMovinoLiveRotation = 0;
        this.mMovinoComplementRotation = 0;
        this.mMinFramesInQueue = 0;
        this.mMaxFramesInQueue = 0;
        this.mQualityRater = new AtomicReference<>();
        this.mComplementData = new AtomicReference<>();
        this.mCurrentlyUploading = new AtomicReference<>();
        this.mCanStart = new AtomicBoolean(true);
        this.mOfflineMode = new AtomicBoolean(false);
        this.mCpuWakeLock = new AtomicReference<>();
        this.mWifiLock = new AtomicReference<>();
        this.mTicketRequestRunning = false;
        this.mShouldStartBroadcast = false;
        this.mBroadcastTicketResponse = null;
        this.mBroadcastTicketExpireTime = 0L;
        this.mUplinkTestBitrate = 0L;
        this.mUplinkTestStartTime = 0L;
        this.mRetryCount = 0;
        this.mObserver = observer;
        this.mContext = context;
        this.mLocationController = new LocationController(context);
        this.mSettings.registerChangeListener(this);
        this.mStorageCheckTimer.scheduleAtFixedRate(new TimerTask() { // from class: com.bambuser.broadcaster.BroadcastController.1
            @Override // java.util.TimerTask, java.lang.Runnable
            public void run() {
                MovinoData currentData = BroadcastController.this.getCurrentData();
                if (currentData != null) {
                    BroadcastController.this.mObserver.onRemainingTimeEstimated(currentData.estimateFreeSeconds());
                }
            }
        }, 5000L, 5000L);
        fetchBroadcastTicket();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void destroy() {
        this.mStorageCheckTimer.cancel();
        this.mSettings.unregisterChangeListener(this);
        this.mLocationController.stop();
        this.mIgnoreConnectionErrors = true;
        abortConnection();
        setCapturer(null);
        this.mTalkbackController.setObserver(null);
        this.mTalkbackController.setAudioReceiver(null);
        this.mByteBufferPool.clear();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void onActivityPause() {
        Capturer capturer = this.mCapturer.get();
        if (capturer == null || !capturer.isCapturing()) {
            this.mLocationController.stop();
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void onActivityResume() {
        startGeoLocation();
        Capturer capturer = this.mCapturer.get();
        if (capturer != null && capturer.isCapturing()) {
            capturer.restartAudio(null);
        } else {
            fetchBroadcastTicket();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void doDisconnect() {
        closeConnection();
        MovinoData andSet = this.mComplementData.getAndSet(null);
        if (andSet != null) {
            andSet.store();
        }
        this.mCurrentlyUploading.set(null);
        WifiManager.WifiLock andSet2 = this.mWifiLock.getAndSet(null);
        if (andSet2 != null && andSet2.isHeld()) {
            andSet2.release();
        }
        PowerManager.WakeLock andSet3 = this.mCpuWakeLock.getAndSet(null);
        if (andSet3 != null && andSet3.isHeld()) {
            andSet3.release();
        }
        this.mCanStart.set(true);
        setBroadcastStatus(BroadcastStatus.IDLE);
    }

    @Override // com.bambuser.broadcaster.LocationController.Observer
    public void onLocationChanged(double d, double d2, float f) {
        if (this.mSettings.getBoolean("location_key")) {
            this.mLocationController.sendLastKnownLocation(getReadyConnection());
            this.mMediaWriter.setLocation(d, d2);
        }
    }

    @Override // android.content.SharedPreferences.OnSharedPreferenceChangeListener
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String str) {
        if (str.equals("title")) {
            this.mObserver.onTitleSettingChanged();
            sendStreamInfo();
            MovinoData currentData = getCurrentData();
            if (currentData != null) {
                currentData.setTitle(this.mSettings.getString("title"));
            }
            this.mMediaWriter.setTitle(this.mSettings.getString("title"));
        } else if (str.equals(BackendApi.TICKET_FILE_AUTHOR) || str.equals(BackendApi.TICKET_FILE_CUSTOM_DATA)) {
            sendStreamInfo();
        } else if (str.equals("location_key")) {
            if (this.mSettings.getBoolean("location_key")) {
                startGeoLocation();
            } else {
                this.mLocationController.stop();
            }
        } else if (str.equals("video_resolution")) {
            Capturer capturer = this.mCapturer.get();
            if (capturer != null) {
                capturer.refreshPreviewResolution();
            }
        } else if (str.equals("output_frame_rate")) {
            Capturer capturer2 = this.mCapturer.get();
            if (capturer2 != null) {
                capturer2.refreshPreviewResolution();
            }
        } else if (str.equals("preview_rotation")) {
            Capturer capturer3 = this.mCapturer.get();
            if (capturer3 != null) {
                capturer3.refreshPreviewRotation();
            }
        } else if (str.equals("capture_rotation")) {
            Capturer capturer4 = this.mCapturer.get();
            if (capturer4 != null) {
                capturer4.refreshCaptureRotation();
            }
        } else if (str.equals("crop_aspect_ratio")) {
            Capturer capturer5 = this.mCapturer.get();
            if (capturer5 != null) {
                capturer5.refreshCropResolution();
            }
        } else if (str.equals("video_max_live_width") || str.equals("video_max_live_height")) {
            Capturer capturer6 = this.mCapturer.get();
            if (capturer6 != null) {
                capturer6.refreshPreviewResolution();
            }
        } else if (str.equals("camera_selection")) {
            Capturer capturer7 = this.mCapturer.get();
            if (capturer7 != null) {
                sendLogMessage("Switching to camera id " + this.mSettings.getCameraId());
                capturer7.restartCamera(true);
                capturer7.restartAudio(null);
            }
            this.mObserver.onCameraSettingChanged();
        } else if (str.equals("audio_quality")) {
            Capturer capturer8 = this.mCapturer.get();
            if (capturer8 != null) {
                capturer8.restartAudio(new Capturer.AudioRestartCallback() { // from class: com.bambuser.broadcaster.BroadcastController.2
                    @Override // com.bambuser.broadcaster.Capturer.AudioRestartCallback
                    public void onAudioSettingApplied() {
                        BroadcastController.this.sendStreamInfo();
                    }
                });
            }
        } else if (str.equals("private_key")) {
            this.mObserver.onPrivacySettingChanged();
        }
    }

    @Override // com.bambuser.broadcaster.MovinoConnectionHandler
    void handshakeDone() {
        if (this.mBroadcastStatus != BroadcastStatus.RECONNECTING) {
            setBroadcastStatus(BroadcastStatus.PREPARED);
            if (!isOfflineMode() && DeviceInfoHandler.isNetworkType(this.mContext, 0)) {
                Resolution croppedResolution = this.mSettings.getCroppedResolution(this.mContext);
                Resolution defaultResolution = DeviceInfoHandler.getDefaultResolution(DeviceInfoHandler.findCamInfo(this.mContext, this.mSettings.getCameraId()));
                if (croppedResolution.getWidth() > Math.max(320, defaultResolution.getWidth()) || croppedResolution.getHeight() > Math.max(240, defaultResolution.getHeight())) {
                    this.mObserver.onTooHighResolutionDetected();
                    return;
                }
            }
        }
        startStream();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean initMediaWriter(File file) {
        return this.mMediaWriter.init(file, this.mSettings.getString("title"));
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean isMediaWriterAcceptingData() {
        return this.mMediaWriter.acceptsData();
    }

    private void startGeoLocation() {
        if (this.mSettings.getBoolean("location_key")) {
            if (DeviceInfoHandler.hasPermission(this.mContext, "android.permission.ACCESS_FINE_LOCATION") || DeviceInfoHandler.hasPermission(this.mContext, "android.permission.ACCESS_COARSE_LOCATION")) {
                this.mLocationController.start();
                this.mLocationController.setObserver(this);
                return;
            }
            Log.w(LOGTAG, "Geo-location enabled but location permissions missing!");
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void startStream() {
        if (isConnectionReady()) {
            if (!isOfflineMode() && this.mBroadcastStatus != BroadcastStatus.RECONNECTING) {
                startComplement();
                QualityRater qualityRater = new QualityRater();
                qualityRater.setObserver(new QualityRater.Observer() { // from class: com.bambuser.broadcaster.BroadcastController.3
                    @Override // com.bambuser.broadcaster.QualityRater.Observer
                    public void onQualityEvaluated(int i, int i2) {
                        BroadcastController.this.mObserver.onStreamHealthUpdate(i, i2);
                    }
                });
                this.mQualityRater.set(qualityRater);
            }
            setFrameQueueLimits();
            sendStreamInfo();
            sendLogMessage("broadcast from camera id " + this.mSettings.getCameraId());
            sendLogMessage("local recording: " + this.mMediaWriter.acceptsData());
            this.mFramesInQueue.set(0);
            this.mComplementFailed = this.mComplementData.get() == null;
            Capturer capturer = this.mCapturer.get();
            if (capturer != null) {
                if (this.mBroadcastStatus == BroadcastStatus.RECONNECTING) {
                    capturer.resumeLiveCapture();
                } else {
                    capturer.startCapture();
                }
            }
            if (this.mSettings.getBoolean("location_key")) {
                this.mLocationController.sendLastKnownLocation(getReadyConnection());
                Location lastKnownLocation = this.mLocationController.getLastKnownLocation();
                if (lastKnownLocation != null) {
                    this.mMediaWriter.setLocation(lastKnownLocation.getLatitude(), lastKnownLocation.getLongitude());
                }
            }
            setBroadcastStatus(BroadcastStatus.CAPTURING);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public MovinoData getCurrentData() {
        if (isOfflineMode()) {
            Connection connection = this.mConnection.get();
            if (connection == null || !(connection instanceof FileConnection)) {
                return null;
            }
            return ((FileConnection) connection).getData();
        }
        return this.mComplementData.get();
    }

    private void startComplement() {
        Connection readyConnection = getReadyConnection();
        if (!this.mSettings.hasComplement() || readyConnection == null) {
            return;
        }
        String randomName = MovinoFileUtils.getRandomName();
        MovinoData createMovinoData = MovinoFileUtils.createMovinoData(this.mContext, randomName, this.mSettings.getString("title"), this.mSessionUsername);
        if (!createMovinoData.fileExists() || !createMovinoData.infoFileExists()) {
            createMovinoData.delete(false);
            this.mComplementFailed = true;
            this.mObserver.onComplementWritingFailed();
            return;
        }
        createMovinoData.setVisibility(this.mSettings.getBoolean("private_key") ? 2 : 1);
        this.mComplementData.set(createMovinoData);
        byte[] uTF8FromString = MovinoUtils.getUTF8FromString(randomName);
        MovinoPacket movinoPacket = new MovinoPacket(25, uTF8FromString.length + 2);
        movinoPacket.writeBinString(uTF8FromString);
        readyConnection.send(movinoPacket);
    }

    private void setFrameQueueLimits() {
        int outputFrameRate = this.mSettings.getOutputFrameRate();
        int i = 2;
        int i2 = 6;
        if (outputFrameRate > 30000) {
            float f = outputFrameRate / 30000.0f;
            i = Math.round(2 * f);
            i2 = Math.round(6 * f);
        }
        this.mMinFramesInQueue = i;
        this.mMaxFramesInQueue = i2;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void sendStreamInfo() {
        int captureRotation = this.mSettings.getCaptureRotation();
        sendStreamInfo(captureRotation >= 0 ? DeviceInfoHandler.getFrameRotation(DeviceInfoHandler.findCamInfo(this.mContext, this.mSettings.getCameraId()), captureRotation) : 0);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void sendStreamInfo(int i) {
        Connection readyConnection = getReadyConnection();
        if (readyConnection == null) {
            return;
        }
        if (isOfflineMode()) {
            readyConnection.send(new MovinoPacket(43, 8).writeUint64(System.currentTimeMillis() / 1000));
        }
        Resolution croppedResolution = this.mSettings.getCroppedResolution(this.mContext);
        Resolution maxLiveResolution = this.mSettings.getMaxLiveResolution();
        if (this.mSettings.getBoolean("auto_resolution")) {
            croppedResolution = Capturer.getMaxLiveResolutionFromPresets(croppedResolution.getWidth(), croppedResolution.getHeight(), maxLiveResolution.getWidth(), maxLiveResolution.getHeight());
        }
        Resolution resolution = (i == 90 || i == 270) ? new Resolution(croppedResolution.getHeight(), croppedResolution.getWidth()) : croppedResolution;
        byte[] uTF8FromString = MovinoUtils.getUTF8FromString(this.mSettings.getString(BackendApi.TICKET_FILE_AUTHOR));
        byte[] uTF8FromString2 = MovinoUtils.getUTF8FromString(this.mSettings.getString("title"));
        byte[] uTF8FromString3 = MovinoUtils.getUTF8FromString(DeviceInfoHandler.getModel());
        byte[] uTF8FromString4 = MovinoUtils.getUTF8FromString(this.mObserver.onGetClientVersion());
        byte[] uTF8FromString5 = MovinoUtils.getUTF8FromString(this.mSettings.getString(BackendApi.TICKET_FILE_CUSTOM_DATA));
        byte[] uTF8FromString6 = MovinoUtils.getUTF8FromString(this.mReconnectToken != null ? this.mReconnectToken : "");
        long j = this.mSettings.getLong("encoded_audio_channels");
        int i2 = 0;
        int i3 = j > 1 ? (int) j : 0;
        MovinoPacket movinoPacket = new MovinoPacket(15, uTF8FromString.length + 7 + 2 + uTF8FromString2.length + 1 + 2 + uTF8FromString3.length + 2 + uTF8FromString4.length + 1 + 2 + uTF8FromString5.length + 4 + 4 + 4 + 4 + 2 + uTF8FromString6.length);
        movinoPacket.writeUint16(resolution.getWidth());
        movinoPacket.writeUint16(resolution.getHeight());
        movinoPacket.writeUint8(this.mSettings.hasAudio() ? 1 : 0);
        movinoPacket.writeBinString(uTF8FromString);
        movinoPacket.writeBinString(uTF8FromString2);
        movinoPacket.writeUint8((isOfflineMode() || this.mSettings.getBoolean("archive")) ? 1 : 1);
        movinoPacket.writeBinString(uTF8FromString3);
        movinoPacket.writeBinString(uTF8FromString4);
        movinoPacket.writeUint8(this.mSettings.getBoolean("private_key") ? 1 : 0);
        movinoPacket.writeBinString(uTF8FromString5);
        movinoPacket.writeUint32(this.mSettings.getOutputFrameRate()).writeUint32(1000L);
        movinoPacket.writeUint32(0L).writeUint32(i3);
        movinoPacket.writeBinString(uTF8FromString6);
        readyConnection.send(movinoPacket);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void start() {
        if (this.mCanStart.compareAndSet(true, false)) {
            setBroadcastStatus(BroadcastStatus.STARTING);
            PowerManager powerManager = (PowerManager) this.mContext.getSystemService("power");
            if (DeviceInfoHandler.hasPermission(this.mContext, "android.permission.WAKE_LOCK")) {
                PowerManager.WakeLock newWakeLock = powerManager.newWakeLock(1, WAKELOCKTAG);
                newWakeLock.acquire();
                this.mCpuWakeLock.set(newWakeLock);
            }
            if (isOfflineMode()) {
                MovinoData createMovinoData = MovinoFileUtils.createMovinoData(this.mContext, MovinoFileUtils.getRandomName(), this.mSettings.getString("title"), this.mSettings.getString("username"));
                createMovinoData.setUploadType(1);
                createMovinoData.setVisibility(this.mSettings.getBoolean("private_key") ? 2 : 1);
                createMovinoData.store();
                if (!createMovinoData.fileExists() || !createMovinoData.infoFileExists()) {
                    createMovinoData.delete(false);
                    abortConnection();
                    this.mObserver.onConnectionError(ConnectionError.WRITE_FAILED, null);
                    return;
                }
                FileConnection fileConnection = new FileConnection(createMovinoData, this);
                this.mConnection.set(fileConnection);
                fileConnection.connect();
                return;
            }
            if (DeviceInfoHandler.isNetworkType(this.mContext, 1) && DeviceInfoHandler.hasPermission(this.mContext, "android.permission.WAKE_LOCK")) {
                WifiManager.WifiLock wifiLock = DeviceInfoHandler.getWifiLock(this.mContext);
                wifiLock.acquire();
                this.mWifiLock.set(wifiLock);
            }
            if (this.mSettings.getString("application_id").isEmpty()) {
                connectMovino();
                return;
            }
            this.mShouldStartBroadcast = true;
            fetchBroadcastTicket();
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* JADX WARN: Type inference failed for: r2v6, types: [com.bambuser.broadcaster.BroadcastController$4] */
    public void fetchBroadcastTicket() {
        if (this.mSettings.getBoolean("skip_broadcast_ticket")) {
            if (this.mShouldStartBroadcast) {
                this.mShouldStartBroadcast = false;
                connectMovino();
                return;
            }
            return;
        }
        String string = this.mSettings.getString("application_id");
        if (this.mTicketRequestRunning || string.isEmpty()) {
            return;
        }
        if (this.mShouldStartBroadcast && this.mBroadcastTicketResponse != null && System.currentTimeMillis() < this.mBroadcastTicketExpireTime) {
            this.mObserver.onBroadcastTicketResponse(this.mShouldStartBroadcast, this.mBroadcastTicketResponse);
            if (this.mShouldStartBroadcast) {
                clearBroadcastTicketResponse();
                this.mShouldStartBroadcast = false;
                connectMovino();
            }
        } else if (this.mBroadcastTicketResponse == null || System.currentTimeMillis() >= this.mBroadcastTicketExpireTime - 59000) {
            this.mTicketRequestRunning = true;
            final boolean z = !this.mShouldStartBroadcast;
            final HashMap hashMap = new HashMap();
            hashMap.put("Accept", "application/vnd.bambuser.cdn.v1+json");
            hashMap.put("X-Bambuser-ClientVersion", this.mObserver.onGetClientVersion());
            hashMap.put("X-Bambuser-ClientPlatform", "Android " + Build.VERSION.RELEASE);
            hashMap.put("X-Bambuser-ApplicationId", string);
            if (z) {
                hashMap.put("X-Bambuser-Prefetch", AppEventsConstants.EVENT_PARAM_VALUE_YES);
            }
            final JSONObject jSONObject = new JSONObject();
            try {
                jSONObject.put("httpUpgradeCapable", true);
                jSONObject.put("tlsCapable", true);
            } catch (Exception unused) {
            }
            new AsyncTask<Void, Void, Pair<Integer, JSONObject>>() { // from class: com.bambuser.broadcaster.BroadcastController.4
                /* JADX INFO: Access modifiers changed from: protected */
                @Override // android.os.AsyncTask
                public Pair<Integer, JSONObject> doInBackground(Void... voidArr) {
                    Pair<Integer, String> jsonEncodedRequest = BackendApi.jsonEncodedRequest("https://cdn.bambuser.net/broadcastTickets", jSONObject, "POST", hashMap);
                    try {
                        return new Pair<>(jsonEncodedRequest.first, new JSONObject((String) jsonEncodedRequest.second));
                    } catch (Exception unused2) {
                        return new Pair<>(jsonEncodedRequest.first, null);
                    }
                }

                /* JADX INFO: Access modifiers changed from: protected */
                @Override // android.os.AsyncTask
                public void onPostExecute(Pair<Integer, JSONObject> pair) {
                    BroadcastController.this.mTicketRequestRunning = false;
                    int intValue = ((Integer) pair.first).intValue();
                    JSONObject jSONObject2 = (JSONObject) pair.second;
                    if (intValue == 403) {
                        if (BroadcastController.this.mShouldStartBroadcast) {
                            if (z) {
                                BroadcastController.this.fetchBroadcastTicket();
                                return;
                            }
                            BroadcastController.this.abortConnection();
                            BroadcastController.this.mObserver.onConnectionError(ConnectionError.BAD_CREDENTIALS, "Invalid application id");
                        }
                    } else if (intValue == 0) {
                        if (BroadcastController.this.mShouldStartBroadcast) {
                            if (z) {
                                BroadcastController.this.fetchBroadcastTicket();
                                return;
                            }
                            BroadcastController.this.abortConnection();
                            BroadcastController.this.mObserver.onConnectionError(ConnectionError.CONNECT_FAILED, "Could not connect to login server");
                        }
                    } else if (jSONObject2 == null || intValue < 200 || intValue >= 400) {
                        if (BroadcastController.this.mShouldStartBroadcast) {
                            if (z) {
                                BroadcastController.this.fetchBroadcastTicket();
                                return;
                            }
                            BroadcastController.this.abortConnection();
                            Observer observer = BroadcastController.this.mObserver;
                            ConnectionError connectionError = ConnectionError.CONNECT_FAILED;
                            observer.onConnectionError(connectionError, "Unexpected response from login server, code " + intValue);
                        }
                    } else {
                        long optLong = jSONObject2.optLong("ttl", 60L);
                        BroadcastController.this.mBroadcastTicketResponse = jSONObject2;
                        BroadcastController.this.mBroadcastTicketExpireTime = System.currentTimeMillis() + (optLong * 1000);
                        BroadcastController.this.mObserver.onBroadcastTicketResponse(BroadcastController.this.mShouldStartBroadcast, BroadcastController.this.mBroadcastTicketResponse);
                        if (BroadcastController.this.mShouldStartBroadcast) {
                            BroadcastController.this.clearBroadcastTicketResponse();
                            BroadcastController.this.mShouldStartBroadcast = false;
                            BroadcastController.this.connectMovino();
                        }
                    }
                }
            }.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, new Void[0]);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void clearBroadcastTicketResponse() {
        this.mBroadcastTicketResponse = null;
        this.mBroadcastTicketExpireTime = 0L;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void setUplinkEstimate(long j, long j2) {
        this.mUplinkTestBitrate = j;
        this.mUplinkTestStartTime = j2;
    }

    @Override // com.bambuser.broadcaster.MovinoConnectionHandler
    boolean handleNetworkDisconnected() {
        if (!(this.mSettings.getString("authorization", "remember_both").equals("remember_both") && this.mSettings.getString("password").length() > 0)) {
            Log.w(LOGTAG, "connection lost, missing credentials for reconnect, reconnection not possible");
            return false;
        } else if (this.mReconnectToken == null || this.mReconnectToken.isEmpty() || this.mReconnectHost == null || this.mReconnectHost.isEmpty() || this.mReconnectPort <= 0 || (this.mBroadcastStatus != BroadcastStatus.CAPTURING && this.mBroadcastStatus != BroadcastStatus.RECONNECTING)) {
            Log.i(LOGTAG, "connection lost, reconnection not possible");
            return false;
        } else if (this.mRetryCount > 5) {
            Log.w(LOGTAG, "connection lost and too many reconnect attempts, giving up");
            return false;
        } else {
            this.mRetryCount++;
            Log.i(LOGTAG, "reconnecting, attempt " + this.mRetryCount);
            Capturer capturer = getCapturer();
            if (capturer != null) {
                capturer.stopLiveCapture();
            }
            this.mIgnoreConnectionErrors = true;
            closeConnection();
            this.mTalkbackController.stop();
            this.mTalkbackController.setAudioReceiver(null);
            this.mMovinoLiveRotation = 0;
            final MovinoUpgradeConnection movinoUpgradeConnection = new MovinoUpgradeConnection(this.mReconnectHost, this.mReconnectPort, this.mReconnectHttps, this);
            this.mConnection.set(movinoUpgradeConnection);
            this.mMainHandler.postDelayed(new Runnable() { // from class: com.bambuser.broadcaster.BroadcastController.5
                @Override // java.lang.Runnable
                public void run() {
                    if (BroadcastController.this.mConnection.get() == movinoUpgradeConnection && BroadcastController.this.mBroadcastStatus == BroadcastStatus.RECONNECTING) {
                        movinoUpgradeConnection.connect();
                    }
                }
            }, this.mRetryCount * 1000);
            setBroadcastStatus(BroadcastStatus.RECONNECTING);
            return true;
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // com.bambuser.broadcaster.MovinoConnectionHandler
    public void abortConnection() {
        this.mShouldStartBroadcast = false;
        if (this.mBroadcastStatus != BroadcastStatus.IDLE) {
            setBroadcastStatus(BroadcastStatus.FINISHING);
        }
        QualityRater andSet = this.mQualityRater.getAndSet(null);
        if (andSet != null) {
            andSet.stop();
        }
        Capturer capturer = this.mCapturer.get();
        boolean stopCapture = capturer != null ? capturer.stopCapture() : false;
        Connection readyConnection = getReadyConnection();
        this.mMovinoLiveRotation = 0;
        this.mMovinoComplementRotation = 0;
        this.mTalkbackController.stop();
        this.mTalkbackController.setAudioReceiver(null);
        boolean z = true;
        MovinoData andSet2 = this.mComplementData.getAndSet(null);
        if (andSet2 != null) {
            andSet2.lock();
            if (capturer != null) {
                andSet2.setLength(capturer.getCaptureDuration() / 1000);
            }
            andSet2.store();
            if (andSet2.getSize() < 50000 && readyConnection != null) {
                Log.i(LOGTAG, "starting automatic upload of small complement data");
                if (stopCapture) {
                    readyConnection.send(new MovinoPacket(16));
                }
                this.mCurrentlyUploading.set(andSet2);
                readyConnection.send(MovinoUtils.createUploadStartPacket(andSet2));
                z = false;
            } else {
                this.mObserver.onNewComplementDataToUpload(andSet2);
            }
        }
        if (z && stopCapture && readyConnection != null) {
            readyConnection.send(new MovinoPacket(null, 16, 0, new DeferredDisconnect(readyConnection)));
            z = false;
        }
        if (this.mMediaWriter.acceptsData()) {
            this.mObserver.onLocalMediaClosed(this.mMediaWriter.close());
        }
        if (z) {
            doDisconnect();
        }
        this.mReconnectToken = null;
        this.mReconnectHost = null;
        this.mReconnectPort = 0;
        this.mReconnectHttps = false;
        this.mRetryCount = 0;
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes.dex */
    public class DeferredDisconnect implements RawPacket.Observer, Runnable {
        private static final int TIMEOUT = 5000;
        private final Connection mConn;

        @Override // com.bambuser.broadcaster.RawPacket.Observer
        public void onBlockEnqueued() {
        }

        DeferredDisconnect(Connection connection) {
            this.mConn = connection;
            BroadcastController.this.mMainHandler.postDelayed(this, 5000L);
        }

        @Override // com.bambuser.broadcaster.RawPacket.Observer
        public void onBlockSent() {
            BroadcastController.this.mMainHandler.post(this);
        }

        @Override // java.lang.Runnable
        public void run() {
            if (BroadcastController.this.mConnection.get() == this.mConn) {
                BroadcastController.this.doDisconnect();
            }
            BroadcastController.this.mMainHandler.removeCallbacks(this);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public Location getLastKnownLocation() {
        if (this.mSettings.getBoolean("location_key")) {
            return this.mLocationController.getLastKnownLocation();
        }
        return null;
    }

    @Override // com.bambuser.broadcaster.MovinoConnectionHandler
    void handleMessage(String str) {
        this.mObserver.addChatMessage(str);
    }

    @Override // com.bambuser.broadcaster.MovinoConnectionHandler
    void uploadRejected() {
        doDisconnect();
    }

    @Override // com.bambuser.broadcaster.MovinoConnectionHandler
    void uploadStartPos(long j) {
        Connection readyConnection = getReadyConnection();
        MovinoData movinoData = this.mCurrentlyUploading.get();
        if (movinoData == null || readyConnection == null) {
            doDisconnect();
            return;
        }
        movinoData.setUploadedBytes(j);
        movinoData.setUploadStarted();
        movinoData.store();
        byte[] bArr = new byte[65536];
        try {
            movinoData.setInputPosition(j);
            BufferedInputStream inputStream = movinoData.getInputStream();
            int read = inputStream.read(bArr);
            if (read == -1) {
                readyConnection.send(new MovinoPacket(27));
            }
            while (read > 0) {
                readyConnection.send(new MovinoPacket(27, read).write(bArr, 0, read));
                read = inputStream.read(bArr);
            }
        } catch (IOException e) {
            Log.w(LOGTAG, "could not read or enqueue the data from complement file, exception: " + e);
            doDisconnect();
        }
    }

    @Override // com.bambuser.broadcaster.MovinoConnectionHandler
    void uploadFinished() {
        Log.i(LOGTAG, "finished automatic upload of small complement data");
        MovinoData andSet = this.mCurrentlyUploading.getAndSet(null);
        if (andSet != null) {
            andSet.delete();
        }
        doDisconnect();
        this.mObserver.onSmallComplementDataUploaded();
    }

    @Override // com.bambuser.broadcaster.MovinoConnectionHandler
    BroadcastElement broadcastInfo(ByteBuffer byteBuffer) {
        if (Log.isLoggable(LOGTAG, 3)) {
            Log.d(LOGTAG, "parsing incoming broadcastinfo");
        }
        return XMLUtils.parseBroadcastInfo(MovinoUtils.getInputStreamFromCompressedUTF8(byteBuffer));
    }

    @Override // com.bambuser.broadcaster.MovinoConnectionHandler
    void currentViewersUpdated(long j) {
        this.mObserver.onCurrentViewersUpdated(j);
    }

    @Override // com.bambuser.broadcaster.MovinoConnectionHandler
    void totalViewersUpdated(long j) {
        this.mObserver.onTotalViewersUpdated(j);
    }

    @Override // com.bambuser.broadcaster.MovinoConnectionHandler
    void onConnectionError(ConnectionError connectionError, String str) {
        this.mObserver.onConnectionError(connectionError, str);
    }

    @Override // com.bambuser.broadcaster.MovinoConnectionHandler
    void showConnectionDialog(MovinoConnectionHandler.Dialog dialog) {
        this.mObserver.showConnectionDialog(dialog);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // com.bambuser.broadcaster.MovinoConnectionHandler
    public int getEnabledTalkbackType() {
        if (this.mTalkbackController.isEnabled()) {
            return TalkbackController.getSupportedType();
        }
        return super.getEnabledTalkbackType();
    }

    @Override // com.bambuser.broadcaster.MovinoConnectionHandler
    void onTalkbackRequest(int i, String str, String str2, String str3) {
        this.mTalkbackController.init(i, str, getReadyConnection(), str2, str3);
        if (this.mSettings.getBoolean("talkback_mixin")) {
            this.mTalkbackController.setAudioReceiver(this.mCapturer.get());
        }
    }

    @Override // com.bambuser.broadcaster.MovinoConnectionHandler
    void onAudioFormat(int i, int i2) {
        this.mTalkbackController.handleAudioFormat(i, i2);
    }

    @Override // com.bambuser.broadcaster.MovinoConnectionHandler
    void onAudioData(int i, int i2, ByteBuffer byteBuffer) {
        this.mTalkbackController.handlePacket(i, i2, byteBuffer);
    }

    @Override // com.bambuser.broadcaster.MovinoConnectionHandler
    void onReconnectInfo(String str, String str2, int i, boolean z) {
        this.mReconnectToken = str;
        this.mReconnectHost = str2;
        this.mReconnectPort = i;
        this.mReconnectHttps = z;
    }

    @Override // com.bambuser.broadcaster.MovinoConnectionHandler
    void onReconnectAccepted() {
        this.mRetryCount = 0;
    }

    @Override // com.bambuser.broadcaster.MovinoConnectionHandler
    void onReconnectFailed() {
        Log.w(LOGTAG, "reconnect failed, stopping broadcast");
        abortConnection();
    }

    @Override // com.bambuser.broadcaster.MovinoConnectionHandler
    void onDisableReconnect() {
        Log.i(LOGTAG, "disabling reconnect per server instructions");
        this.mReconnectToken = null;
        this.mReconnectHost = null;
        this.mReconnectPort = 0;
        this.mReconnectHttps = false;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public TalkbackController getTalkbackController() {
        return this.mTalkbackController;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public Capturer getCapturer() {
        return this.mCapturer.get();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void setCapturer(Capturer capturer) {
        Capturer andSet = this.mCapturer.getAndSet(capturer);
        if (andSet == capturer) {
            return;
        }
        if (andSet != null) {
            andSet.close();
        }
        if (capturer != null) {
            capturer.setCameraObserver(this.mCameraInterface);
            capturer.setEncodeObserver(this.mEncodeInterface);
            capturer.restartCamera(false);
        }
    }

    boolean isOfflineMode() {
        return this.mOfflineMode.get();
    }

    void setOfflineMode(boolean z) {
        if (canStart()) {
            this.mOfflineMode.set(z);
            this.mObserver.onConnectionStatusChange(this.mBroadcastStatus);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean canStart() {
        return this.mCanStart.get();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public int getDesiredAudioSampleRate() {
        return this.mSettings.getString("audio_quality").equals("normal") ? 8000 : 16000;
    }

    private void setBroadcastStatus(BroadcastStatus broadcastStatus) {
        if (this.mBroadcastStatus == broadcastStatus) {
            return;
        }
        this.mBroadcastStatus = broadcastStatus;
        this.mObserver.onConnectionStatusChange(broadcastStatus);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void sendLogMessage(String str) {
        Connection readyConnection = getReadyConnection();
        if (readyConnection == null) {
            return;
        }
        readyConnection.send(MovinoUtils.createLogMessagePacket(str));
    }
}
