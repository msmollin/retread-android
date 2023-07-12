package com.bambuser.broadcaster;

import android.app.Activity;
import android.content.Context;
import android.location.Location;
import android.os.Build;
import android.util.Log;
import android.view.SurfaceView;
import com.bambuser.broadcaster.BroadcastController;
import com.bambuser.broadcaster.LinkTester;
import com.bambuser.broadcaster.MovinoConnectionHandler;
import com.bambuser.broadcaster.TalkbackController;
import com.bambuser.broadcaster.VideoCapturerBase;
import com.bambuser.broadcaster.XMLUtils;
import java.io.File;
import java.util.LinkedList;
import java.util.List;
import org.json.JSONObject;

/* loaded from: classes.dex */
public final class Broadcaster {
    static final String LIB_VERSION = "lib 0.9.22";
    private static final String LOGTAG = "Broadcaster";
    private final Activity mActivity;
    private BroadcastController mBroadcastController;
    private LocalMediaObserver mLocalMediaObserver;
    private final Observer mObserver;
    private boolean mResReceiverRegd;
    private final SettingsReader mSettingsReader;
    private SurfaceView mSurfaceView;
    private final BroadcastController.Observer mControllerObserver = new BroadcastController.Observer() { // from class: com.bambuser.broadcaster.Broadcaster.4
        @Override // com.bambuser.broadcaster.BroadcastController.Observer
        public void onCameraResolutionChanged(int i, int i2) {
        }

        @Override // com.bambuser.broadcaster.BroadcastController.Observer
        public void onCameraSettingChanged() {
        }

        @Override // com.bambuser.broadcaster.BroadcastController.Observer
        public void onComplementWritingFailed() {
        }

        @Override // com.bambuser.broadcaster.BroadcastController.Observer
        public void onNewComplementDataToUpload(MovinoData movinoData) {
        }

        @Override // com.bambuser.broadcaster.BroadcastController.Observer
        public void onPrivacySettingChanged() {
        }

        @Override // com.bambuser.broadcaster.BroadcastController.Observer
        public void onRemainingTimeEstimated(long j) {
        }

        @Override // com.bambuser.broadcaster.BroadcastController.Observer
        public void onSmallComplementDataUploaded() {
        }

        @Override // com.bambuser.broadcaster.BroadcastController.Observer
        public void onTitleSettingChanged() {
        }

        @Override // com.bambuser.broadcaster.BroadcastController.Observer
        public void onBroadcastTicketResponse(boolean z, JSONObject jSONObject) {
            JSONObject optJSONObject = jSONObject.optJSONObject("ingestChannel");
            JSONObject optJSONObject2 = jSONObject.optJSONObject("movinoServer");
            JSONObject optJSONObject3 = jSONObject.optJSONObject("uploadTest");
            String str = null;
            String optString = (optJSONObject == null || optJSONObject.isNull("name")) ? null : optJSONObject.optString("name");
            String optString2 = (optJSONObject == null || optJSONObject.isNull("token")) ? null : optJSONObject.optString("token");
            Broadcaster.this.mUploadTestUrl = (optJSONObject3 == null || optJSONObject3.isNull("uploadUrl")) ? null : optJSONObject3.optString("uploadUrl");
            if (!z) {
                Broadcaster.this.startUplinkTest();
            }
            if (Broadcaster.this.mUseBroadcastTicketCredentials) {
                if (!z || (optString != null && optString2 != null && optString.length() > 0 && optString2.length() > 0)) {
                    Broadcaster.this.mSettingsReader.setCredentials(optString, optString2);
                } else {
                    Broadcaster.this.mBroadcastController.clearBroadcastTicketResponse();
                    Broadcaster.this.mBroadcastController.abortConnection();
                    Broadcaster.this.mObserver.onConnectionError(ConnectionError.BAD_CREDENTIALS, "Server response did not contain ingest credentials for broadcasting");
                    return;
                }
            }
            String optString3 = (optJSONObject2 == null || optJSONObject2.isNull("host")) ? null : optJSONObject2.optString("host");
            if (optJSONObject2 != null && !optJSONObject2.isNull("port")) {
                str = optJSONObject2.optString("port");
            }
            boolean z2 = true;
            boolean z3 = optJSONObject2 != null && optJSONObject2.optBoolean("tls", false);
            if (optJSONObject2 == null || !optJSONObject2.optBoolean("httpUpgrade", false)) {
                z2 = false;
            }
            Broadcaster.this.mSettingsReader.setMovinoServer(optString3, str, z3, z2);
        }

        @Override // com.bambuser.broadcaster.BroadcastController.Observer
        public void onConnectionStatusChange(final BroadcastStatus broadcastStatus) {
            Broadcaster.this.mActivity.runOnUiThread(new Runnable() { // from class: com.bambuser.broadcaster.Broadcaster.4.1
                @Override // java.lang.Runnable
                public void run() {
                    Broadcaster.this.mObserver.onConnectionStatusChange(broadcastStatus);
                }
            });
        }

        @Override // com.bambuser.broadcaster.BroadcastController.Observer
        public void onStreamHealthUpdate(final int i, int i2) {
            Broadcaster.this.mActivity.runOnUiThread(new Runnable() { // from class: com.bambuser.broadcaster.Broadcaster.4.2
                @Override // java.lang.Runnable
                public void run() {
                    Broadcaster.this.mObserver.onStreamHealthUpdate(i);
                }
            });
        }

        @Override // com.bambuser.broadcaster.BroadcastController.Observer
        public void addChatMessage(final String str) {
            Broadcaster.this.mActivity.runOnUiThread(new Runnable() { // from class: com.bambuser.broadcaster.Broadcaster.4.3
                @Override // java.lang.Runnable
                public void run() {
                    Broadcaster.this.mObserver.onChatMessage(str);
                }
            });
        }

        @Override // com.bambuser.broadcaster.BroadcastController.Observer
        public void onCurrentViewersUpdated(final long j) {
            Broadcaster.this.mActivity.runOnUiThread(new Runnable() { // from class: com.bambuser.broadcaster.Broadcaster.4.4
                @Override // java.lang.Runnable
                public void run() {
                    if (Broadcaster.this.mViewerCountObserver != null) {
                        Broadcaster.this.mViewerCountObserver.onCurrentViewersUpdated(j);
                    }
                }
            });
        }

        @Override // com.bambuser.broadcaster.BroadcastController.Observer
        public void onTotalViewersUpdated(final long j) {
            Broadcaster.this.mActivity.runOnUiThread(new Runnable() { // from class: com.bambuser.broadcaster.Broadcaster.4.5
                @Override // java.lang.Runnable
                public void run() {
                    if (Broadcaster.this.mViewerCountObserver != null) {
                        Broadcaster.this.mViewerCountObserver.onTotalViewersUpdated(j);
                    }
                }
            });
        }

        @Override // com.bambuser.broadcaster.BroadcastController.Observer
        public void onCameraPreviewStateChanged() {
            Broadcaster.this.mActivity.runOnUiThread(new Runnable() { // from class: com.bambuser.broadcaster.Broadcaster.4.6
                @Override // java.lang.Runnable
                public void run() {
                    Broadcaster.this.mObserver.onCameraPreviewStateChanged();
                }
            });
        }

        @Override // com.bambuser.broadcaster.BroadcastController.Observer
        public void showConnectionDialog(MovinoConnectionHandler.Dialog dialog) {
            Log.w(Broadcaster.LOGTAG, "No credentials set, disconnecting");
            Broadcaster.this.mActivity.runOnUiThread(new Runnable() { // from class: com.bambuser.broadcaster.Broadcaster.4.7
                @Override // java.lang.Runnable
                public void run() {
                    if (Broadcaster.this.mBroadcastController != null) {
                        Broadcaster.this.mBroadcastController.abortConnection();
                    }
                }
            });
        }

        @Override // com.bambuser.broadcaster.BroadcastController.Observer
        public void onConnectionError(final ConnectionError connectionError, final String str) {
            Broadcaster.this.mActivity.runOnUiThread(new Runnable() { // from class: com.bambuser.broadcaster.Broadcaster.4.8
                @Override // java.lang.Runnable
                public void run() {
                    Broadcaster.this.mObserver.onConnectionError(connectionError, str);
                }
            });
        }

        @Override // com.bambuser.broadcaster.BroadcastController.Observer
        public void onCameraError(final CameraError cameraError) {
            Broadcaster.this.mActivity.runOnUiThread(new Runnable() { // from class: com.bambuser.broadcaster.Broadcaster.4.9
                @Override // java.lang.Runnable
                public void run() {
                    Broadcaster.this.mObserver.onCameraError(cameraError);
                }
            });
        }

        @Override // com.bambuser.broadcaster.BroadcastController.Observer
        public void onLocalMediaError() {
            Broadcaster.this.mActivity.runOnUiThread(new Runnable() { // from class: com.bambuser.broadcaster.Broadcaster.4.10
                @Override // java.lang.Runnable
                public void run() {
                    if (Broadcaster.this.mLocalMediaObserver != null) {
                        Broadcaster.this.mLocalMediaObserver.onLocalMediaError();
                    }
                }
            });
        }

        @Override // com.bambuser.broadcaster.BroadcastController.Observer
        public void onLocalMediaClosed(final String str) {
            Broadcaster.this.mActivity.runOnUiThread(new Runnable() { // from class: com.bambuser.broadcaster.Broadcaster.4.11
                @Override // java.lang.Runnable
                public void run() {
                    if (Broadcaster.this.mLocalMediaObserver != null) {
                        Broadcaster.this.mLocalMediaObserver.onLocalMediaClosed(str);
                    }
                }
            });
        }

        @Override // com.bambuser.broadcaster.BroadcastController.Observer
        public void onTooHighResolutionDetected() {
            if (Broadcaster.this.mBroadcastController != null) {
                Broadcaster.this.mBroadcastController.startStream();
            }
        }

        @Override // com.bambuser.broadcaster.BroadcastController.Observer
        public String onGetClientVersion() {
            return Broadcaster.getClientVersion(Broadcaster.this.mActivity);
        }

        @Override // com.bambuser.broadcaster.BroadcastController.Observer
        public void onEncoderError(Exception exc) {
            Broadcaster.this.mActivity.runOnUiThread(new Runnable() { // from class: com.bambuser.broadcaster.Broadcaster.4.12
                @Override // java.lang.Runnable
                public void run() {
                    Broadcaster.this.stopBroadcast();
                    if (Broadcaster.this.mObserver != null) {
                        Broadcaster.this.mObserver.onCameraError(CameraError.ENCODER_ERROR);
                    }
                }
            });
        }
    };
    private final VideoCapturerBase.ResolutionScanListener mResolutionsReceiver = new VideoCapturerBase.ResolutionScanListener() { // from class: com.bambuser.broadcaster.Broadcaster.5
        @Override // com.bambuser.broadcaster.VideoCapturerBase.ResolutionScanListener
        public void onResolutionsScanned() {
            if (Thread.currentThread() == Broadcaster.this.mActivity.getMainLooper().getThread()) {
                Broadcaster.this.mCameraInfoLoaded = true;
                Broadcaster.this.mObserver.onResolutionsScanned();
                return;
            }
            final Object obj = new Object();
            synchronized (obj) {
                Broadcaster.this.mActivity.runOnUiThread(new Runnable() { // from class: com.bambuser.broadcaster.Broadcaster.5.1
                    @Override // java.lang.Runnable
                    public void run() {
                        Broadcaster.this.mCameraInfoLoaded = true;
                        Broadcaster.this.mObserver.onResolutionsScanned();
                        synchronized (obj) {
                            obj.notifyAll();
                        }
                    }
                });
                while (true) {
                    try {
                        obj.wait(1000L);
                    } catch (InterruptedException unused) {
                    }
                }
            }
        }
    };
    private final XMLUtils.BroadcastInfoObserver mBroadcastInfoObserver = new XMLUtils.BroadcastInfoObserver() { // from class: com.bambuser.broadcaster.Broadcaster.6
        private String mLatestVid = "";

        @Override // com.bambuser.broadcaster.XMLUtils.BroadcastInfoObserver
        public void onNewInfo() {
            BroadcastElement broadcastInfo = XMLUtils.getBroadcastInfo();
            if (broadcastInfo == null) {
                return;
            }
            final String vid = broadcastInfo.getVid();
            final String url = broadcastInfo.getUrl();
            if (vid == null || this.mLatestVid.equals(vid)) {
                return;
            }
            this.mLatestVid = vid;
            final String broadcastId = broadcastInfo.getBroadcastId();
            Broadcaster.this.mActivity.runOnUiThread(new Runnable() { // from class: com.bambuser.broadcaster.Broadcaster.6.1
                @Override // java.lang.Runnable
                public void run() {
                    Broadcaster.this.mObserver.onBroadcastInfoAvailable(vid, url);
                    if (broadcastId == null || broadcastId.length() <= 0) {
                        return;
                    }
                    Broadcaster.this.mObserver.onBroadcastIdAvailable(broadcastId);
                }
            });
        }
    };
    private boolean mCameraInfoLoaded = false;
    private boolean mUseBroadcastTicketCredentials = true;
    private String mUploadTestUrl = null;
    private UplinkSpeedObserver mUplinkSpeedObserver = null;
    private ViewerCountObserver mViewerCountObserver = null;
    private LinkTester.UploadTest mUploadTest = null;

    /* loaded from: classes.dex */
    public enum AudioSetting {
        OFF,
        NORMAL_QUALITY,
        HIGH_QUALITY
    }

    /* loaded from: classes.dex */
    public interface LocalMediaObserver {
        void onLocalMediaClosed(String str);

        void onLocalMediaError();
    }

    /* loaded from: classes.dex */
    public interface Observer {
        void onBroadcastIdAvailable(String str);

        void onBroadcastInfoAvailable(String str, String str2);

        void onCameraError(CameraError cameraError);

        void onCameraPreviewStateChanged();

        void onChatMessage(String str);

        void onConnectionError(ConnectionError connectionError, String str);

        void onConnectionStatusChange(BroadcastStatus broadcastStatus);

        void onResolutionsScanned();

        void onStreamHealthUpdate(int i);
    }

    /* loaded from: classes.dex */
    public interface PictureObserver {
        void onPictureStored(File file);
    }

    /* loaded from: classes.dex */
    public interface TalkbackObserver {
        void onTalkbackStateChanged(TalkbackState talkbackState, int i, String str, String str2);
    }

    /* loaded from: classes.dex */
    public interface UplinkSpeedObserver {
        void onUplinkTestComplete(long j, boolean z);
    }

    /* loaded from: classes.dex */
    public interface ViewerCountObserver {
        void onCurrentViewersUpdated(long j);

        void onTotalViewersUpdated(long j);
    }

    /* loaded from: classes.dex */
    public class Camera {
        public static final String FRONT = "front";
        public static final String REAR = "rear";
        public final String facing;
        public final String id;

        Camera(String str, String str2) {
            this.id = str;
            this.facing = str2;
        }
    }

    public Broadcaster(Activity activity, String str, Observer observer) {
        this.mActivity = activity;
        this.mObserver = observer;
        SentryLogger.initLogger(activity);
        this.mSettingsReader = new SettingsReader(new TransientPreferences());
        this.mSettingsReader.setString("application_id", str);
        this.mSettingsReader.setBoolean("complement", false);
        this.mSettingsReader.setBoolean("vibration_key", false);
        this.mSettingsReader.setBoolean("auto_resolution", true);
        setResolution(0, 0);
        setAudioQuality(AudioSetting.HIGH_QUALITY);
        this.mBroadcastController = new BroadcastController(activity, this.mSettingsReader, this.mControllerObserver);
        VideoCapturer.registerResolutionScanListener(this.mResolutionsReceiver);
        this.mResReceiverRegd = true;
        XMLUtils.registerForNewInfoCallbacks(this.mBroadcastInfoObserver);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static String getClientVersion(Context context) {
        String str = "lib 0.9.22 (" + context.getPackageName();
        String appVersion = DeviceInfoHandler.getAppVersion(context);
        if (appVersion != null && appVersion.length() > 0) {
            str = str + " " + appVersion;
        }
        return str + ")";
    }

    public void setCameraSurface(SurfaceView surfaceView) {
        if (this.mBroadcastController == null) {
            Log.w(LOGTAG, "This Broadcaster was destroyed and must no longer be used.");
            return;
        }
        if (this.mSurfaceView != surfaceView) {
            this.mBroadcastController.sendLogMessage("SurfaceView replaced, disconnecting");
            this.mBroadcastController.abortConnection();
            this.mBroadcastController.setCapturer(null);
            this.mBroadcastController.setCapturer(new Capturer(this.mActivity, surfaceView, this.mSettingsReader.useBt()));
        }
        this.mSurfaceView = surfaceView;
    }

    public void setUplinkSpeedObserver(UplinkSpeedObserver uplinkSpeedObserver) {
        this.mUplinkSpeedObserver = uplinkSpeedObserver;
        startUplinkTest();
    }

    public long getUplinkSpeed() {
        if (this.mUploadTest != null) {
            return this.mUploadTest.getBitrate();
        }
        return 0L;
    }

    public boolean getUplinkRecommendation() {
        return getUplinkSpeed() > 400000;
    }

    public void startUplinkTest() {
        if (this.mUploadTest != null) {
            this.mUploadTest.cancel();
        }
        this.mUploadTest = null;
        if (this.mUploadTestUrl == null || !isBroadcastControllerIdle()) {
            return;
        }
        this.mUploadTest = LinkTester.post(this.mUploadTestUrl, 1000000, new LinkTester.Callback() { // from class: com.bambuser.broadcaster.Broadcaster.1
            @Override // com.bambuser.broadcaster.LinkTester.Callback
            public void onDone(long j) {
                if (Broadcaster.this.mUplinkSpeedObserver != null) {
                    Broadcaster.this.mUplinkSpeedObserver.onUplinkTestComplete(j, j > 400000);
                }
            }
        });
    }

    public void setTitle(String str) {
        this.mSettingsReader.setString("title", str);
    }

    public void setAuthor(String str) {
        this.mSettingsReader.setString(BackendApi.TICKET_FILE_AUTHOR, str);
    }

    public void setCustomData(String str) {
        this.mSettingsReader.setString(BackendApi.TICKET_FILE_CUSTOM_DATA, str);
    }

    public List<Resolution> getSupportedResolutions() {
        LinkedList linkedList = new LinkedList();
        CamInfo findCamInfo = DeviceInfoHandler.findCamInfo(this.mActivity, this.mSettingsReader.getCameraId());
        if (findCamInfo != null) {
            Resolution maxCameraResolution = getMaxCameraResolution();
            for (Resolution resolution : findCamInfo.getPreviewList()) {
                if (linkedList.isEmpty() || (resolution.getWidth() <= maxCameraResolution.getWidth() && resolution.getHeight() <= maxCameraResolution.getHeight())) {
                    linkedList.add(resolution);
                }
            }
        }
        return linkedList;
    }

    private static Resolution getMaxCameraResolution() {
        int i;
        int i2;
        int i3 = 480;
        if (NativeUtils.hasNeon() || NativeUtils.hasArm64() || NativeUtils.isX86_32() || NativeUtils.isX86_64()) {
            i = 1280;
            i2 = 720;
        } else {
            i2 = 320;
            i = 480;
        }
        if (DeviceInfoHandler.isMediaCodecSupported() || i <= 800) {
            i3 = i2;
        } else {
            i = 800;
        }
        return new Resolution(i, i3);
    }

    public List<Resolution> getSupportedPictureResolutions() {
        CamInfo findCamInfo = DeviceInfoHandler.findCamInfo(this.mActivity, this.mSettingsReader.getCameraId());
        return findCamInfo != null ? findCamInfo.getPictureList() : new LinkedList();
    }

    public Resolution getResolution() {
        if (this.mSettingsReader.resolutionStored()) {
            return this.mSettingsReader.getResolution(this.mActivity);
        }
        return null;
    }

    public void setResolution(int i, int i2) {
        if (isLocalRecordingInProgress()) {
            Log.w(LOGTAG, "Can not switch resolution while local recording is in progress");
            return;
        }
        Resolution maxCameraResolution = getMaxCameraResolution();
        if ((i <= 0 && i2 <= 0) || i > maxCameraResolution.getWidth() || i2 > maxCameraResolution.getHeight()) {
            i = maxCameraResolution.getWidth();
            i2 = maxCameraResolution.getHeight();
        }
        this.mSettingsReader.setResolution(new Resolution(i, i2));
    }

    public void setMaxLiveResolution(int i, int i2) {
        if (i <= 320 || i2 <= 240) {
            Log.w(LOGTAG, "" + i + "x" + i2 + " is too low. falling back to 320x240");
            i2 = 240;
            i = 320;
        }
        if (i > 1280 || i2 > 720) {
            Log.w(LOGTAG, "" + i + "x" + i2 + " is too high. falling back to 1280x720");
            i2 = 720;
            i = 1280;
        }
        this.mSettingsReader.setMaxLiveResolution(i, i2);
    }

    public void setRotation(int i) {
        setRotation(i, i);
    }

    public void setRotation(int i, int i2) {
        setRotation(i, i2, 0, 0);
    }

    public void setRotation(int i, int i2, int i3, int i4) {
        if (this.mBroadcastController == null) {
            Log.w(LOGTAG, "This Broadcaster was destroyed and must no longer be used.");
        } else if (isLocalRecordingInProgress()) {
            Log.w(LOGTAG, "Rotation and/or cropping can not be changed while recording a local copy.");
        } else {
            int frameRotation = DeviceInfoHandler.getFrameRotation(DeviceInfoHandler.findCamInfo(this.mActivity, this.mSettingsReader.getCameraId()), i2);
            if (frameRotation == 90 || frameRotation == 270) {
                i4 = i3;
                i3 = i4;
            }
            if (i != this.mSettingsReader.getPreviewRotation()) {
                this.mSettingsReader.setPreviewRotation(i);
            }
            Resolution resolution = new Resolution(i3, i4);
            if (!resolution.equals(this.mSettingsReader.getCropAspectRatio())) {
                this.mSettingsReader.setCaptureRotationAndCrop(i2, resolution);
            } else if (i2 != this.mSettingsReader.getCaptureRotation()) {
                this.mSettingsReader.setCaptureRotation(i2);
            }
        }
    }

    public int getPreviewRotation() {
        return this.mSettingsReader.getPreviewRotation();
    }

    public int getCaptureRotation() {
        return this.mSettingsReader.getCaptureRotation();
    }

    public boolean hasLocalMediaCapability() {
        return DeviceInfoHandler.isMediaCodecSupported();
    }

    public boolean storeLocalMedia(File file, LocalMediaObserver localMediaObserver) {
        if (!isBroadcastControllerIdle()) {
            Log.w(LOGTAG, "storeLocalMedia() has no effect while broadcasting");
            return false;
        } else if (!hasLocalMediaCapability()) {
            Log.w(LOGTAG, "storing a local media file is not supported on " + Build.MANUFACTURER + " " + Build.MODEL + " with API " + Build.VERSION.SDK_INT);
            return false;
        } else {
            this.mLocalMediaObserver = localMediaObserver;
            return this.mBroadcastController.initMediaWriter(file);
        }
    }

    public boolean hasTalkbackCapability() {
        return TalkbackController.getSupportedType() != 0;
    }

    public void setTalkbackObserver(final TalkbackObserver talkbackObserver) {
        if (!isBroadcastControllerIdle()) {
            Log.w(LOGTAG, "setTalkbackObserver() has no effect while broadcasting");
        } else if (hasTalkbackCapability()) {
            if (talkbackObserver == null) {
                this.mBroadcastController.getTalkbackController().setObserver(null);
            } else {
                this.mBroadcastController.getTalkbackController().setObserver(new TalkbackController.Observer() { // from class: com.bambuser.broadcaster.Broadcaster.2
                    @Override // com.bambuser.broadcaster.TalkbackController.Observer
                    public void onTalkbackStateChanged(final TalkbackState talkbackState, final int i, final String str, final String str2) {
                        Broadcaster.this.mActivity.runOnUiThread(new Runnable() { // from class: com.bambuser.broadcaster.Broadcaster.2.1
                            @Override // java.lang.Runnable
                            public void run() {
                                talkbackObserver.onTalkbackStateChanged(talkbackState, i, str, str2);
                            }
                        });
                    }
                });
            }
        } else {
            Log.w(LOGTAG, "receiving talkback streams is not supported on " + Build.MANUFACTURER + " " + Build.MODEL + " with API " + Build.VERSION.SDK_INT);
        }
    }

    public void setTalkbackMixin(boolean z) {
        if (!isBroadcastControllerIdle()) {
            Log.w(LOGTAG, "setTalkbackMixin() has no effect while broadcasting");
        } else if (!hasTalkbackCapability()) {
            Log.w(LOGTAG, "setTalkbackMixin() called on device where mix-in is not supported");
        } else {
            this.mSettingsReader.setBoolean("talkback_mixin", z);
            this.mSettingsReader.setLong("encoded_audio_channels", z ? 2L : 0L);
        }
    }

    public void setViewerCountObserver(ViewerCountObserver viewerCountObserver) {
        this.mViewerCountObserver = viewerCountObserver;
    }

    public void acceptTalkback(int i) {
        if (this.mBroadcastController != null) {
            this.mBroadcastController.getTalkbackController().accept(i);
        }
    }

    public void stopTalkback() {
        if (this.mBroadcastController != null) {
            this.mBroadcastController.getTalkbackController().stop();
        }
    }

    public void switchCamera() {
        if (isLocalRecordingInProgress() && !canSwitchCameraWithoutResolutionChange()) {
            Log.w(LOGTAG, "Can not switch camera while local recording is in progress");
        } else {
            this.mSettingsReader.switchCamera(this.mActivity);
        }
    }

    public int getCameraCount() {
        return DeviceInfoHandler.getSupportedCameras(this.mActivity).size();
    }

    public List<Camera> getSupportedCameras() {
        LinkedList linkedList = new LinkedList();
        for (CamInfo camInfo : DeviceInfoHandler.getSupportedCameras(this.mActivity)) {
            linkedList.add(new Camera(camInfo.mId, camInfo.isFront() ? Camera.FRONT : Camera.REAR));
        }
        return linkedList;
    }

    public String getCameraId() {
        return this.mSettingsReader.getCameraId();
    }

    public void setCameraId(String str) {
        if (isLocalRecordingInProgress() && !canSwitchCameraWithoutResolutionChange(str)) {
            Log.w(LOGTAG, "Can not switch camera while local recording is in progress");
            return;
        }
        for (CamInfo camInfo : DeviceInfoHandler.getSupportedCameras(this.mActivity)) {
            if (camInfo.mId.equals(str)) {
                this.mSettingsReader.setString("camera_selection", str);
                return;
            }
        }
        Log.w(LOGTAG, "Invalid camera id");
    }

    public boolean canSwitchCameraWithoutResolutionChange() {
        List<CamInfo> supportedCameras = DeviceInfoHandler.getSupportedCameras(this.mActivity);
        int size = supportedCameras.size();
        if (size < 2) {
            return false;
        }
        return canSwitchCameraWithoutResolutionChange(supportedCameras.get((supportedCameras.indexOf(DeviceInfoHandler.findCamInfo(this.mActivity, this.mSettingsReader.getCameraId())) + 1) % size).mId);
    }

    public boolean canSwitchCameraWithoutResolutionChange(String str) {
        if (DeviceInfoHandler.getSupportedCameras(this.mActivity).size() < 2) {
            return false;
        }
        CamInfo findCamInfo = DeviceInfoHandler.findCamInfo(this.mActivity, this.mSettingsReader.getCameraId());
        CamInfo findCamInfo2 = DeviceInfoHandler.findCamInfo(this.mActivity, str);
        if (findCamInfo2 == null) {
            Log.w(LOGTAG, "Invalid camera id");
            return false;
        }
        Resolution resolution = getResolution();
        int captureRotation = this.mSettingsReader.getCaptureRotation();
        if (captureRotation < 0 || !isLocalRecordingInProgress() || DeviceInfoHandler.getFrameRotation(findCamInfo, captureRotation) == DeviceInfoHandler.getFrameRotation(findCamInfo2, captureRotation)) {
            return findCamInfo2.getPreviewList().contains(resolution);
        }
        return false;
    }

    public void setAudioQuality(AudioSetting audioSetting) {
        if (isLocalRecordingInProgress()) {
            Log.w(LOGTAG, "Can not change audio quality while local recording is in progress");
            return;
        }
        switch (audioSetting) {
            case OFF:
                this.mSettingsReader.setString("audio_quality", "off");
                return;
            case NORMAL_QUALITY:
                this.mSettingsReader.setString("audio_quality", "normal");
                return;
            case HIGH_QUALITY:
                this.mSettingsReader.setString("audio_quality", "high");
                return;
            default:
                return;
        }
    }

    public boolean canDisableShutterSound() {
        CamInfo findCamInfo = DeviceInfoHandler.findCamInfo(this.mActivity, this.mSettingsReader.getCameraId());
        return findCamInfo != null && findCamInfo.mDisableShuttersound;
    }

    public void setCaptureSounds(boolean z) {
        this.mSettingsReader.setBoolean("capture_sound", z);
    }

    public void setSendPosition(boolean z) {
        this.mSettingsReader.setBoolean("location_key", z);
    }

    public Location getRecentPosition() {
        if (this.mBroadcastController == null) {
            Log.w(LOGTAG, "This Broadcaster was destroyed and must no longer be used.");
            return null;
        }
        return this.mBroadcastController.getLastKnownLocation();
    }

    public void setSaveOnServer(boolean z) {
        if (isBroadcastControllerIdle()) {
            this.mSettingsReader.setBoolean("archive", z);
        }
    }

    public boolean hasFocus() {
        if (this.mBroadcastController == null) {
            Log.w(LOGTAG, "This Broadcaster was destroyed and must no longer be used.");
            return false;
        }
        Capturer capturer = this.mBroadcastController.getCapturer();
        return capturer != null && capturer.hasFocus();
    }

    public void focus() {
        if (this.mBroadcastController == null) {
            Log.w(LOGTAG, "This Broadcaster was destroyed and must no longer be used.");
            return;
        }
        Capturer capturer = this.mBroadcastController.getCapturer();
        if (capturer != null) {
            capturer.focus();
        }
    }

    public boolean hasTorch() {
        if (this.mBroadcastController == null) {
            Log.w(LOGTAG, "This Broadcaster was destroyed and must no longer be used.");
            return false;
        }
        Capturer capturer = this.mBroadcastController.getCapturer();
        return capturer != null && capturer.hasTorch();
    }

    public void toggleTorch() {
        if (this.mBroadcastController == null) {
            Log.w(LOGTAG, "This Broadcaster was destroyed and must no longer be used.");
            return;
        }
        Capturer capturer = this.mBroadcastController.getCapturer();
        if (capturer != null) {
            capturer.toggleTorch();
        }
    }

    public boolean hasZoom() {
        if (this.mBroadcastController == null) {
            Log.w(LOGTAG, "This Broadcaster was destroyed and must no longer be used.");
            return false;
        }
        Capturer capturer = this.mBroadcastController.getCapturer();
        return capturer != null && capturer.hasZoom();
    }

    public int getZoom() {
        if (this.mBroadcastController == null) {
            Log.w(LOGTAG, "This Broadcaster was destroyed and must no longer be used.");
            return 100;
        }
        Capturer capturer = this.mBroadcastController.getCapturer();
        if (capturer != null) {
            return capturer.getZoom();
        }
        return 100;
    }

    public List<Integer> getZoomRatios() {
        if (this.mBroadcastController == null) {
            Log.w(LOGTAG, "This Broadcaster was destroyed and must no longer be used.");
            return null;
        }
        Capturer capturer = this.mBroadcastController.getCapturer();
        if (capturer != null) {
            return capturer.getZoomRatios();
        }
        return null;
    }

    public void setZoom(int i) {
        if (this.mBroadcastController == null) {
            Log.w(LOGTAG, "This Broadcaster was destroyed and must no longer be used.");
            return;
        }
        Capturer capturer = this.mBroadcastController.getCapturer();
        if (capturer != null) {
            capturer.setZoom(i);
        }
    }

    public void onActivityPause() {
        if (this.mBroadcastController == null) {
            Log.w(LOGTAG, "This Broadcaster was destroyed and must no longer be used.");
            return;
        }
        if (this.mUploadTest != null) {
            this.mUploadTest.cancel();
        }
        this.mUploadTest = null;
        this.mBroadcastController.sendLogMessage("onActivityPause");
        this.mBroadcastController.abortConnection();
        this.mBroadcastController.setCapturer(null);
        this.mBroadcastController.onActivityPause();
        VideoCapturer.unregisterResolutionScanListener(this.mResolutionsReceiver);
        this.mResReceiverRegd = false;
    }

    public void onActivityResume() {
        if (this.mBroadcastController == null) {
            Log.w(LOGTAG, "This Broadcaster was destroyed and must no longer be used.");
            return;
        }
        if (!this.mResReceiverRegd) {
            VideoCapturer.registerResolutionScanListener(this.mResolutionsReceiver);
            this.mResReceiverRegd = true;
        }
        this.mBroadcastController.onActivityResume();
        if (this.mBroadcastController.getCapturer() != null || this.mSurfaceView == null) {
            return;
        }
        this.mBroadcastController.setCapturer(new Capturer(this.mActivity, this.mSurfaceView, this.mSettingsReader.useBt()));
    }

    public void onActivityDestroy() {
        XMLUtils.unregisterFromNewInfoCallbacks(this.mBroadcastInfoObserver);
        if (this.mBroadcastController != null) {
            this.mBroadcastController.destroy();
        }
        this.mBroadcastController = null;
        this.mSurfaceView = null;
        if (this.mUploadTest != null) {
            this.mUploadTest.cancel();
        }
        this.mUploadTest = null;
    }

    private boolean isLocalRecordingInProgress() {
        return (this.mBroadcastController == null || !this.mBroadcastController.isMediaWriterAcceptingData() || this.mBroadcastController.canStart()) ? false : true;
    }

    public boolean canStartBroadcasting() {
        return isCameraInfoLoaded() && isBroadcastControllerIdle() && this.mBroadcastController.getCapturer() != null;
    }

    private boolean isCameraInfoLoaded() {
        return this.mCameraInfoLoaded;
    }

    private boolean isBroadcastControllerIdle() {
        return this.mBroadcastController != null && this.mBroadcastController.canStart();
    }

    void startBroadcast(String str, String str2) {
        this.mUseBroadcastTicketCredentials = false;
        this.mSettingsReader.setCredentials(str, str2);
        internalStartBroadcast();
    }

    private void internalStartBroadcast() {
        if (this.mBroadcastController == null) {
            Log.w(LOGTAG, "This Broadcaster was destroyed and must no longer be used.");
        } else if (!isCameraInfoLoaded()) {
            Log.w(LOGTAG, "The Broadcaster is not ready for broadcasting, camera info has not been loaded.");
        } else if (this.mBroadcastController.getCapturer() == null) {
            Log.w(LOGTAG, "The Broadcaster is not ready for broadcasting. SurfaceView not set, or Activity paused?");
        } else if (this.mBroadcastController.canStart()) {
            boolean hasPermission = DeviceInfoHandler.hasPermission(this.mActivity, "android.permission.CAMERA");
            boolean hasPermission2 = DeviceInfoHandler.hasPermission(this.mActivity, "android.permission.RECORD_AUDIO");
            if (!hasPermission || !hasPermission2) {
                Log.w(LOGTAG, "startBroadcast: Missing camera or audio permission.");
                return;
            }
            if (this.mUploadTest != null) {
                this.mUploadTest.cancel();
            }
            this.mBroadcastController.setUplinkEstimate(this.mUploadTest != null ? this.mUploadTest.getBitrate() : 0L, this.mUploadTest != null ? this.mUploadTest.getStartTime() : 0L);
            this.mUploadTest = null;
            this.mBroadcastController.start();
        }
    }

    public void startBroadcast() {
        String string = this.mSettingsReader.getString("application_id");
        if (string.isEmpty() || string.contains("CHANGEME") || string.contains("PLEASE")) {
            Log.w(LOGTAG, "Missing applicationId.");
            this.mObserver.onConnectionError(ConnectionError.BAD_CREDENTIALS, "Missing application id");
            return;
        }
        internalStartBroadcast();
    }

    public void stopBroadcast() {
        if (this.mBroadcastController == null) {
            Log.w(LOGTAG, "This Broadcaster was destroyed and must no longer be used.");
            return;
        }
        this.mBroadcastController.sendLogMessage("stopBroadcast");
        this.mBroadcastController.abortConnection();
        this.mBroadcastController.fetchBroadcastTicket();
    }

    public void takePicture(final File file, final Resolution resolution, final PictureObserver pictureObserver) {
        if (this.mBroadcastController == null) {
            Log.w(LOGTAG, "This Broadcaster was destroyed and must no longer be used.");
        } else if (!DeviceInfoHandler.hasPermission(this.mActivity, "android.permission.CAMERA")) {
            Log.w(LOGTAG, "takePicture: Missing camera permission.");
        } else {
            Capturer capturer = this.mBroadcastController.getCapturer();
            if (capturer == null) {
                Log.w(LOGTAG, "takePicture: Broadcaster not ready. SurfaceView not set, or Activity paused?");
            } else if (file == null) {
                Log.w(LOGTAG, "takePicture: File was null, no picture taken.");
            } else {
                try {
                    file.createNewFile();
                    capturer.takePicture(new PictureCallback() { // from class: com.bambuser.broadcaster.Broadcaster.3
                        @Override // com.bambuser.broadcaster.PictureCallback
                        public Resolution onGetResolution() {
                            return resolution != null ? resolution : new Resolution(1920, 1080);
                        }

                        @Override // com.bambuser.broadcaster.PictureCallback
                        public Location onGetLocation() {
                            return Broadcaster.this.getRecentPosition();
                        }

                        @Override // com.bambuser.broadcaster.PictureCallback
                        public File onGetFile() {
                            return file;
                        }

                        @Override // com.bambuser.broadcaster.PictureCallback
                        public void onPictureStored(final File file2) {
                            if (file2 == null) {
                                file.delete();
                            }
                            Broadcaster.this.mActivity.runOnUiThread(new Runnable() { // from class: com.bambuser.broadcaster.Broadcaster.3.1
                                @Override // java.lang.Runnable
                                public void run() {
                                    if (pictureObserver == null || file2 == null) {
                                        return;
                                    }
                                    pictureObserver.onPictureStored(file2);
                                }
                            });
                        }
                    });
                } catch (Exception e) {
                    Log.w(LOGTAG, "takePicture could not create file: " + e);
                }
            }
        }
    }
}
