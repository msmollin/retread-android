package com.bambuser.broadcaster;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.ActivityManager;
import android.content.Context;
import android.graphics.Rect;
import android.hardware.camera2.CameraCaptureSession;
import android.hardware.camera2.CameraCharacteristics;
import android.hardware.camera2.CameraDevice;
import android.hardware.camera2.CameraManager;
import android.hardware.camera2.CaptureFailure;
import android.hardware.camera2.CaptureRequest;
import android.hardware.camera2.CaptureResult;
import android.location.Location;
import android.media.Image;
import android.media.ImageReader;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;
import android.os.SystemClock;
import android.util.Log;
import android.util.Range;
import android.util.Size;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import com.bambuser.broadcaster.Capturer;
import com.bambuser.broadcaster.SentryLogger;
import com.bambuser.broadcaster.VideoCapturerBase;
import com.facebook.appevents.AppEventsConstants;
import com.facebook.imagepipeline.producers.HttpUrlConnectionNetworkFetcher;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/* JADX INFO: Access modifiers changed from: package-private */
@TargetApi(21)
/* loaded from: classes.dex */
public final class VideoCapturerLollipop extends VideoCapturerBase implements View.OnLayoutChangeListener {
    private static final String LOGTAG = "VideoCapturerLollipop";
    private final Object mCameraCloseLock;
    private final CameraHandler mCameraHandler;
    private volatile boolean mCameraMustClose;
    private volatile boolean mHasFocus;
    private volatile boolean mHasPreviewSurface;
    private volatile boolean mHasTorch;
    private final int mMaxFrames;
    private volatile float mMaxZoom;
    private final Object mPreviewSurfaceLock;
    private volatile boolean mResolutionChangeNeedsRestart;
    private volatile Resolution mSurfaceResolution;
    private volatile float mZoom;

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes.dex */
    public enum CameraState {
        CLOSED,
        CAMERA_OPENING,
        PREPARED_FOR_SESSION,
        SESSION_OPENING,
        READY,
        CLOSING,
        FAILED
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public VideoCapturerLollipop(Capturer capturer, Context context, SurfaceView surfaceView) {
        super(capturer, context, surfaceView);
        this.mPreviewSurfaceLock = new Object();
        this.mCameraCloseLock = new Object();
        boolean z = false;
        this.mCameraMustClose = false;
        this.mHasPreviewSurface = false;
        this.mSurfaceResolution = null;
        this.mResolutionChangeNeedsRestart = false;
        this.mHasFocus = false;
        this.mHasTorch = false;
        this.mMaxZoom = 1.0f;
        this.mZoom = 1.0f;
        this.mPreviewSurfaceView.addOnLayoutChangeListener(this);
        Surface surface = this.mPreviewSurfaceHolder.getSurface();
        if (surface != null && surface.isValid()) {
            z = true;
        }
        this.mHasPreviewSurface = z;
        int memoryClass = ((ActivityManager) this.mContext.getSystemService("activity")).getMemoryClass();
        if (memoryClass > 128) {
            this.mMaxFrames = 6;
        } else if (memoryClass >= 64) {
            this.mMaxFrames = 4;
        } else {
            this.mMaxFrames = 2;
        }
        Log.i(LOGTAG, "memory class " + memoryClass + ", allowing " + this.mMaxFrames + " frames in encoder pipeline");
        HandlerThread handlerThread = new HandlerThread("CameraThread");
        handlerThread.start();
        this.mCameraHandler = new CameraHandler(handlerThread.getLooper());
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // com.bambuser.broadcaster.VideoCapturerBase
    public void close() {
        super.close();
        this.mPreviewSurfaceView.removeOnLayoutChangeListener(this);
        if (Thread.currentThread() == this.mCameraHandler.getLooper().getThread()) {
            throw new RuntimeException("VideoCapturerLollipop.close() called on camera thread. can't wait for self!");
        }
        waitForCameraClosed();
        this.mPreviewSurfaceHolder.setSizeFromLayout();
        this.mCameraHandler.sendEmptyMessage(5);
    }

    private void waitForCameraClosed() {
        synchronized (this.mCameraCloseLock) {
            this.mCameraMustClose = true;
            long elapsedRealtime = SystemClock.elapsedRealtime();
            while (!this.mCameraHandler.isCameraClosed()) {
                try {
                    this.mCameraHandler.removeMessages(1);
                    this.mCameraHandler.sendEmptyMessage(4);
                    long elapsedRealtime2 = 2000 - (SystemClock.elapsedRealtime() - elapsedRealtime);
                    if (elapsedRealtime2 > 0) {
                        this.mCameraCloseLock.wait(elapsedRealtime2);
                    }
                } catch (InterruptedException unused) {
                }
                if (SystemClock.elapsedRealtime() - elapsedRealtime >= 2000) {
                    break;
                }
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // com.bambuser.broadcaster.VideoCapturerBase
    public void setCameraObserver(Capturer.CameraInterface cameraInterface) {
        this.mCameraHandler.obtainMessage(3, cameraInterface).sendToTarget();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // com.bambuser.broadcaster.VideoCapturerBase
    public void initVideoCapture() {
        this.mCameraHandler.sendEmptyMessage(6);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // com.bambuser.broadcaster.VideoCapturerBase
    public void startVideoCapture(InfoFrame infoFrame) {
        this.mCameraHandler.obtainMessage(7, infoFrame).sendToTarget();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // com.bambuser.broadcaster.VideoCapturerBase
    public void stopVideoCapture() {
        this.mCameraHandler.sendEmptyMessage(10);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // com.bambuser.broadcaster.VideoCapturerBase
    public void refreshPreviewResolution() {
        if (this.mResolutionChangeNeedsRestart) {
            refreshCameraState(false);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // com.bambuser.broadcaster.VideoCapturerBase
    public void refreshCropResolution() {
        this.mCameraHandler.sendEmptyMessage(9);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // com.bambuser.broadcaster.VideoCapturerBase
    public void refreshPreviewRotation() {
        this.mCameraHandler.sendEmptyMessage(11);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // com.bambuser.broadcaster.VideoCapturerBase
    public void refreshCaptureRotation() {
        this.mCameraHandler.sendEmptyMessage(12);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // com.bambuser.broadcaster.VideoCapturerBase
    public void refreshCameraState(boolean z) {
        this.mCameraHandler.removeMessages(1);
        this.mCameraHandler.sendEmptyMessage(1);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // com.bambuser.broadcaster.VideoCapturerBase
    public boolean hasFocus() {
        return this.mHasFocus;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // com.bambuser.broadcaster.VideoCapturerBase
    public void focus() {
        this.mCameraHandler.sendEmptyMessage(13);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // com.bambuser.broadcaster.VideoCapturerBase
    public boolean hasZoom() {
        return this.mMaxZoom > 1.0f;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // com.bambuser.broadcaster.VideoCapturerBase
    public int getZoom() {
        return Math.round(this.mZoom * 100.0f);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // com.bambuser.broadcaster.VideoCapturerBase
    public List<Integer> getZoomRatios() {
        int round = Math.round(this.mMaxZoom * 100.0f);
        if (round > 100) {
            ArrayList arrayList = new ArrayList(((round - 100) / 10) + 1);
            for (int i = 100; i <= round; i += 10) {
                arrayList.add(Integer.valueOf(i));
            }
            return arrayList;
        }
        return null;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // com.bambuser.broadcaster.VideoCapturerBase
    public void setZoom(int i) {
        this.mCameraHandler.obtainMessage(15, i, 0).sendToTarget();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // com.bambuser.broadcaster.VideoCapturerBase
    public void stepZoom(boolean z) {
        this.mCameraHandler.obtainMessage(16, z ? 1 : 0, 0).sendToTarget();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // com.bambuser.broadcaster.VideoCapturerBase
    public boolean hasTorch() {
        return this.mHasTorch;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // com.bambuser.broadcaster.VideoCapturerBase
    public void toggleTorch() {
        this.mCameraHandler.sendEmptyMessage(2);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // com.bambuser.broadcaster.VideoCapturerBase
    public void takePicture(VideoCapturerBase.TakePictureCallback takePictureCallback) {
        this.mCameraHandler.obtainMessage(14, takePictureCallback).sendToTarget();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // com.bambuser.broadcaster.VideoCapturerBase
    public void reuseFrame(Frame frame) {
        this.mCameraHandler.obtainMessage(8, frame).sendToTarget();
    }

    @Override // android.view.SurfaceHolder.Callback
    public void surfaceCreated(SurfaceHolder surfaceHolder) {
        this.mHasPreviewSurface = surfaceHolder.getSurface() != null && surfaceHolder.getSurface().isValid();
    }

    @Override // android.view.SurfaceHolder.Callback
    public void surfaceChanged(SurfaceHolder surfaceHolder, int i, int i2, int i3) {
        Log.i(LOGTAG, "surfaceChanged callback width: " + i2 + " height: " + i3);
        this.mHasPreviewSurface = surfaceHolder.getSurface() != null && surfaceHolder.getSurface().isValid();
        this.mSurfaceResolution = new Resolution(i2, i3);
        this.mCameraHandler.removeMessages(1);
        this.mCameraHandler.sendEmptyMessage(1);
    }

    @Override // android.view.SurfaceHolder.Callback
    public void surfaceDestroyed(SurfaceHolder surfaceHolder) {
        Log.i(LOGTAG, "surfaceDestroyed callback");
        this.mHasPreviewSurface = false;
        synchronized (this.mPreviewSurfaceLock) {
            this.mCameraHandler.removeMessages(1);
            this.mCameraHandler.sendEmptyMessage(1);
            while (true) {
                try {
                    this.mPreviewSurfaceLock.wait(2000L);
                } catch (InterruptedException unused) {
                }
            }
        }
    }

    @Override // android.view.View.OnLayoutChangeListener
    public void onLayoutChange(View view, int i, int i2, int i3, int i4, int i5, int i6, int i7, int i8) {
        if (Log.isLoggable(LOGTAG, 3)) {
            Log.d(LOGTAG, "onLayoutChange callback, left: " + i + " (" + i5 + "), top: " + i2 + " (" + i6 + "), right: " + i3 + " (" + i7 + "), bottom: " + i4 + " (" + i8 + ")");
        }
        if (i == i5 && i2 == i6 && i3 == i7 && i4 == i8) {
            return;
        }
        this.mCameraHandler.removeMessages(1);
        this.mCameraHandler.sendEmptyMessage(1);
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes.dex */
    public class CameraHandler extends Handler implements ImageReader.OnImageAvailableListener {
        static final int CLOSE = 5;
        static final int FOCUS = 13;
        static final int INIT_CAPTURE = 6;
        static final int REFRESH_CROP_RESOLUTION = 9;
        static final int RELOAD_CAMERA = 1;
        static final int REUSE_FRAME = 8;
        static final int SET_CAMERA_OBSERVER = 3;
        static final int SET_CAPTURE_ROTATION = 12;
        static final int SET_PREVIEW_ROTATION = 11;
        static final int SET_ZOOM = 15;
        static final int START_CAPTURE = 7;
        static final int STEP_ZOOM = 16;
        static final int STOP_CAMERA = 4;
        static final int STOP_CAPTURE = 10;
        static final int TAKEPICTURE = 14;
        static final int TOGGLE_TORCH = 2;
        private CameraDevice mCamera;
        private CameraCaptureSession mCameraCaptureSession;
        private boolean mCameraFailed;
        private int mCameraFrameRotation;
        private final CameraManager mCameraManager;
        private Capturer.CameraInterface mCameraObserver;
        private int mCameraPreviewRotation;
        private CameraState mCameraState;
        private final CameraDevice.StateCallback mCameraStateCallback;
        private final CameraCaptureSession.CaptureCallback mCaptureCallback;
        private Range<Integer> mCaptureFpsRange;
        private CaptureRequest.Builder mCaptureRequestBuilder;
        private Resolution mCaptureResolution;
        private int mCaptureRotationSetting;
        private final Rect mCropRect;
        private boolean mFocusTriggered;
        private final ArrayList<Frame> mFrameHolder;
        private ImageReader mImageReader;
        private long mImageTimelineOffset;
        private int mLatestInitIndex;
        private boolean mNeedCameraReload;
        private int mPreviewRotationSetting;
        private boolean mReuseFrames;
        private int mSelectedFocusMode;
        private TakePictureRequest mTakePictureRequest;
        private boolean mToggleAutoFocus;
        private boolean mTorchOn;
        private volatile boolean mWaitingForEncoder;
        private volatile boolean mWaitingForSurfaceSize;

        CameraHandler(Looper looper) {
            super(looper);
            this.mCameraStateCallback = new CameraDevice.StateCallback() { // from class: com.bambuser.broadcaster.VideoCapturerLollipop.CameraHandler.4
                @Override // android.hardware.camera2.CameraDevice.StateCallback
                public void onOpened(CameraDevice cameraDevice) {
                    synchronized (VideoCapturerLollipop.this.mCameraCloseLock) {
                        CameraHandler.this.mCameraState = CameraState.PREPARED_FOR_SESSION;
                        CameraHandler.this.mCamera = cameraDevice;
                        if (VideoCapturerLollipop.this.mCameraMustClose) {
                            VideoCapturerLollipop.this.mCameraCloseLock.notifyAll();
                            return;
                        }
                        VideoCapturerBase.internalBroadcastResolutions();
                        CameraHandler.this.internalReloadCamera();
                    }
                }

                @Override // android.hardware.camera2.CameraDevice.StateCallback
                public void onDisconnected(CameraDevice cameraDevice) {
                    Log.w(VideoCapturerLollipop.LOGTAG, "CameraDevice onDisconnected: " + cameraDevice);
                    synchronized (VideoCapturerLollipop.this.mCameraCloseLock) {
                        CameraHandler.this.mCameraState = CameraState.FAILED;
                        CameraHandler.this.mCameraFailed = true;
                    }
                    CameraHandler.this.internalCloseCamera();
                }

                @Override // android.hardware.camera2.CameraDevice.StateCallback
                public void onError(CameraDevice cameraDevice, int i) {
                    Log.w(VideoCapturerLollipop.LOGTAG, "CameraDevice error: " + i);
                    SentryLogger.asyncMessage("CameraDevice onError " + i, SentryLogger.Level.ERROR, null, null);
                    synchronized (VideoCapturerLollipop.this.mCameraCloseLock) {
                        CameraHandler.this.mCameraState = CameraState.FAILED;
                        CameraHandler.this.mCameraFailed = true;
                        VideoCapturerLollipop.this.mCameraCloseLock.notifyAll();
                    }
                    if (CameraHandler.this.mCameraObserver != null) {
                        CameraHandler.this.mCameraObserver.onCameraError(CameraError.ANDROID_INTERNAL_ERROR);
                    }
                }

                @Override // android.hardware.camera2.CameraDevice.StateCallback
                public void onClosed(CameraDevice cameraDevice) {
                    boolean z = CameraHandler.this.mCameraFailed || CameraHandler.this.mCameraState == CameraState.FAILED;
                    CameraHandler.this.mCameraFailed = false;
                    if (CameraHandler.this.mCamera == cameraDevice) {
                        Log.i(VideoCapturerLollipop.LOGTAG, "camera closed");
                        synchronized (VideoCapturerLollipop.this.mCameraCloseLock) {
                            CameraHandler.this.mCameraState = CameraState.CLOSED;
                            CameraHandler.this.mCamera = null;
                            VideoCapturerLollipop.this.mCameraCloseLock.notifyAll();
                        }
                    } else {
                        Log.w(VideoCapturerLollipop.LOGTAG, "camera closed mismatch with current camera reference!");
                    }
                    if (VideoCapturerLollipop.this.mCameraMustClose || z) {
                        return;
                    }
                    VideoCapturerLollipop.this.refreshCameraState(false);
                }
            };
            this.mCaptureCallback = new CameraCaptureSession.CaptureCallback() { // from class: com.bambuser.broadcaster.VideoCapturerLollipop.CameraHandler.5
                @Override // android.hardware.camera2.CameraCaptureSession.CaptureCallback
                public void onCaptureProgressed(CameraCaptureSession cameraCaptureSession, CaptureRequest captureRequest, CaptureResult captureResult) {
                    Integer num = (Integer) captureResult.get(CaptureResult.CONTROL_AF_MODE);
                    if (CameraHandler.this.mCaptureRequestBuilder == null || num == null || num.intValue() != 1 || !CameraHandler.this.mToggleAutoFocus) {
                        return;
                    }
                    CameraHandler.this.mToggleAutoFocus = false;
                    CameraHandler.this.internalTriggerFocus(cameraCaptureSession);
                    try {
                        cameraCaptureSession.setRepeatingRequest(CameraHandler.this.mCaptureRequestBuilder.build(), null, CameraHandler.this);
                    } catch (Exception unused) {
                    }
                }
            };
            this.mFrameHolder = new ArrayList<>(16);
            this.mCropRect = new Rect();
            this.mCameraState = CameraState.CLOSED;
            this.mCameraFailed = false;
            this.mImageTimelineOffset = Long.MIN_VALUE;
            this.mLatestInitIndex = 0;
            this.mCaptureResolution = null;
            this.mSelectedFocusMode = 3;
            this.mToggleAutoFocus = false;
            this.mFocusTriggered = false;
            this.mTorchOn = false;
            this.mPreviewRotationSetting = -1;
            this.mCaptureRotationSetting = -1;
            this.mCaptureFpsRange = null;
            this.mNeedCameraReload = false;
            this.mReuseFrames = false;
            this.mWaitingForSurfaceSize = false;
            this.mWaitingForEncoder = false;
            this.mCameraManager = (CameraManager) VideoCapturerLollipop.this.mContext.getSystemService("camera");
        }

        @Override // android.os.Handler
        public void handleMessage(Message message) {
            switch (message.what) {
                case 1:
                    internalReloadCamera();
                    return;
                case 2:
                    internalToggleTorch();
                    return;
                case 3:
                    this.mCameraObserver = (Capturer.CameraInterface) message.obj;
                    return;
                case 4:
                    internalCloseCamera();
                    return;
                case 5:
                    internalCloseCamera();
                    if (this.mImageReader != null) {
                        this.mImageReader.close();
                        this.mImageReader = null;
                    }
                    if (this.mTakePictureRequest != null) {
                        this.mTakePictureRequest.finish(null);
                    }
                    this.mTakePictureRequest = null;
                    getLooper().quit();
                    return;
                case 6:
                    internalInitVideoCapture(true);
                    return;
                case 7:
                    internalSetCaptureRequests(true);
                    internalStartVideoCapture((InfoFrame) message.obj);
                    return;
                case 8:
                    if (VideoCapturerLollipop.this.mCapturer.isCapturing()) {
                        internalReuseFrame((Frame) message.obj);
                        return;
                    }
                    return;
                case 9:
                    this.mReuseFrames = false;
                    this.mFrameHolder.clear();
                    internalSetCaptureRequests(false);
                    internalSetCropping();
                    internalInitVideoCapture(false);
                    return;
                case 10:
                    this.mReuseFrames = false;
                    this.mFrameHolder.clear();
                    internalSetCaptureRequests(false);
                    this.mImageTimelineOffset = Long.MIN_VALUE;
                    return;
                case 11:
                    internalSetPreviewRotation();
                    internalRefreshPreviewSurfaceLayout();
                    return;
                case 12:
                    internalSetCaptureRotation();
                    return;
                case 13:
                    internalFocus();
                    return;
                case 14:
                    internalTakePicture((VideoCapturerBase.TakePictureCallback) message.obj);
                    return;
                case 15:
                    internalSetZoom(message.arg1 / 100.0f);
                    return;
                case 16:
                    internalStepZoom(message.arg1 > 0);
                    return;
                default:
                    return;
            }
        }

        boolean isCameraClosed() {
            boolean z;
            synchronized (VideoCapturerLollipop.this.mCameraCloseLock) {
                z = this.mCamera == null && this.mCameraState == CameraState.CLOSED;
            }
            return z;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void internalCloseCamera() {
            if (this.mCameraState == CameraState.CLOSING || this.mCameraState == CameraState.CLOSED) {
                return;
            }
            Log.i(VideoCapturerLollipop.LOGTAG, "internalCloseCamera");
            this.mReuseFrames = false;
            this.mFrameHolder.clear();
            if (this.mCameraCaptureSession != null) {
                this.mCameraCaptureSession.close();
                this.mCameraCaptureSession = null;
            }
            if (this.mCamera != null) {
                this.mCameraState = CameraState.CLOSING;
                this.mCamera.close();
                if (this.mCameraObserver != null) {
                    this.mCameraObserver.onCameraPreviewStateChanged(false);
                }
            } else if (this.mCameraState != CameraState.CAMERA_OPENING) {
                this.mCameraState = CameraState.CLOSED;
                this.mCameraFailed = false;
            }
            this.mCaptureRequestBuilder = null;
            this.mCaptureResolution = null;
            VideoCapturerLollipop.this.mHasFocus = false;
            this.mToggleAutoFocus = false;
            this.mFocusTriggered = false;
            this.mSelectedFocusMode = 3;
            VideoCapturerLollipop.this.mHasTorch = false;
            VideoCapturerLollipop.this.mMaxZoom = 1.0f;
            VideoCapturerLollipop.this.mResolutionChangeNeedsRestart = false;
            this.mImageTimelineOffset = Long.MIN_VALUE;
            notifyPreviewSurfaceNotInUse();
            synchronized (VideoCapturerLollipop.this.mCameraCloseLock) {
                VideoCapturerLollipop.this.mCameraCloseLock.notifyAll();
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        @SuppressLint({"MissingPermission"})
        public void internalReloadCamera() {
            if (VideoCapturerLollipop.this.mCameraMustClose || this.mCameraState == CameraState.CLOSING || this.mCameraState == CameraState.FAILED || this.mCameraFailed || this.mWaitingForEncoder || this.mWaitingForSurfaceSize) {
                return;
            }
            if (this.mCameraState == CameraState.CAMERA_OPENING || this.mCameraState == CameraState.SESSION_OPENING) {
                this.mNeedCameraReload = true;
                return;
            }
            boolean z = false;
            this.mNeedCameraReload = false;
            Surface surface = VideoCapturerLollipop.this.mPreviewSurfaceHolder.getSurface();
            final boolean z2 = VideoCapturerLollipop.this.mHasPreviewSurface && surface != null;
            boolean isCapturing = VideoCapturerLollipop.this.mCapturer.isCapturing();
            if (!z2 && !isCapturing) {
                Log.i(VideoCapturerLollipop.LOGTAG, "No capture or preview active, close any camera for now");
                internalCloseCamera();
                return;
            }
            String onGetCameraId = this.mCameraObserver != null ? this.mCameraObserver.onGetCameraId() : AppEventsConstants.EVENT_PARAM_VALUE_NO;
            if (onGetCameraId.equals("-1") || (this.mCamera != null && !onGetCameraId.equals(this.mCamera.getId()))) {
                internalCloseCamera();
                VideoCapturerLollipop.this.mZoom = 1.0f;
            } else if (DeviceInfoHandler.isCameraDisabledByDPM(VideoCapturerLollipop.this.mContext)) {
                internalCloseCamera();
                if (this.mCameraObserver != null) {
                    this.mCameraObserver.onCameraError(CameraError.DISABLED_BY_DPM);
                }
            } else if (this.mCamera == null) {
                Log.i(VideoCapturerLollipop.LOGTAG, "opening a camera");
                synchronized (VideoCapturerLollipop.this.mCameraCloseLock) {
                    this.mCameraState = CameraState.CAMERA_OPENING;
                }
                try {
                    this.mCameraManager.openCamera(onGetCameraId, this.mCameraStateCallback, this);
                } catch (Exception e) {
                    synchronized (VideoCapturerLollipop.this.mCameraCloseLock) {
                        this.mCameraState = CameraState.FAILED;
                        this.mCameraFailed = true;
                        VideoCapturerLollipop.this.mCameraCloseLock.notifyAll();
                        Log.w(VideoCapturerLollipop.LOGTAG, "openCamera exception " + e);
                        SentryLogger.asyncMessage("Exception when opening camera", SentryLogger.Level.ERROR, null, e);
                        if (this.mCameraObserver != null) {
                            this.mCameraObserver.onCameraError(CameraError.COULD_NOT_OPEN);
                        }
                    }
                }
            } else {
                Log.i(VideoCapturerLollipop.LOGTAG, "Configuring open camera");
                CamInfo findCamInfo = DeviceInfoHandler.findCamInfo(VideoCapturerLollipop.this.mContext, this.mCamera.getId());
                List<Resolution> previewList = findCamInfo != null ? findCamInfo.getPreviewList() : null;
                if (previewList != null && previewList.size() >= 1) {
                    VideoCapturerLollipop.this.mResolutionChangeNeedsRestart = true;
                    this.mCaptureResolution = this.mCameraObserver != null ? this.mCameraObserver.onGetResolution() : DeviceInfoHandler.getDefaultResolution(findCamInfo);
                    if (this.mImageReader != null && (this.mImageReader.getWidth() != this.mCaptureResolution.getWidth() || this.mImageReader.getHeight() != this.mCaptureResolution.getHeight() || this.mImageReader.getImageFormat() != 35)) {
                        if (this.mCameraCaptureSession != null) {
                            try {
                                this.mCameraCaptureSession.abortCaptures();
                            } catch (Exception unused) {
                            }
                        }
                        this.mReuseFrames = false;
                        this.mFrameHolder.clear();
                        final ImageReader imageReader = this.mImageReader;
                        this.mImageReader = null;
                        this.mWaitingForEncoder = true;
                        VideoCapturerLollipop.this.mCapturer.postToEncoderThread(new Runnable() { // from class: com.bambuser.broadcaster.VideoCapturerLollipop.CameraHandler.1
                            @Override // java.lang.Runnable
                            public void run() {
                                imageReader.close();
                                CameraHandler.this.mWaitingForEncoder = false;
                                VideoCapturerLollipop.this.refreshCameraState(false);
                            }
                        });
                        return;
                    }
                    if (this.mImageReader == null) {
                        Log.i(VideoCapturerLollipop.LOGTAG, "Allocating new imagereader size " + this.mCaptureResolution.getWidth() + "x" + this.mCaptureResolution.getHeight());
                        this.mImageReader = ImageReader.newInstance(this.mCaptureResolution.getWidth(), this.mCaptureResolution.getHeight(), 35, VideoCapturerLollipop.this.mMaxFrames + 2);
                    }
                    this.mImageReader.setOnImageAvailableListener(this, this);
                    internalSetPreviewRotation();
                    internalSetCaptureRotation();
                    internalSetCropping();
                    if (internalRefreshPreviewSurfaceLayout()) {
                        return;
                    }
                    ArrayList arrayList = new ArrayList();
                    arrayList.add(this.mImageReader.getSurface());
                    if (z2) {
                        final Resolution chooseOptimalSize = VideoCapturerLollipop.chooseOptimalSize(previewList, VideoCapturerLollipop.this.mPreviewSurfaceView.getWidth(), VideoCapturerLollipop.this.mPreviewSurfaceView.getHeight(), this.mCaptureResolution);
                        if (VideoCapturerLollipop.this.mSurfaceResolution == null || !VideoCapturerLollipop.this.mSurfaceResolution.equals(chooseOptimalSize)) {
                            this.mWaitingForSurfaceSize = true;
                            VideoCapturerLollipop.this.mPreviewSurfaceView.getHandler().post(new Runnable() { // from class: com.bambuser.broadcaster.VideoCapturerLollipop.CameraHandler.2
                                @Override // java.lang.Runnable
                                public void run() {
                                    boolean z3 = chooseOptimalSize.equals(VideoCapturerLollipop.this.mSurfaceResolution) && !CameraHandler.this.mWaitingForSurfaceSize;
                                    CameraHandler.this.mWaitingForSurfaceSize = false;
                                    if (z3) {
                                        Log.i(VideoCapturerLollipop.LOGTAG, "an earlier event set the surface size. just refresh camera state");
                                        VideoCapturerLollipop.this.refreshCameraState(false);
                                        return;
                                    }
                                    Rect surfaceFrame = VideoCapturerLollipop.this.mPreviewSurfaceHolder.getSurfaceFrame();
                                    if (surfaceFrame.width() == chooseOptimalSize.getWidth() && surfaceFrame.height() == chooseOptimalSize.getHeight()) {
                                        if (VideoCapturerLollipop.this.mSurfaceResolution == null) {
                                            VideoCapturerLollipop.this.mSurfaceResolution = new Resolution(surfaceFrame.width(), surfaceFrame.height());
                                        }
                                        Log.i(VideoCapturerLollipop.LOGTAG, "surface is already the optimal size. making it fixed and refreshing camera");
                                        VideoCapturerLollipop.this.mPreviewSurfaceHolder.setFixedSize(chooseOptimalSize.getWidth(), chooseOptimalSize.getHeight());
                                        VideoCapturerLollipop.this.refreshCameraState(false);
                                        return;
                                    }
                                    Log.i(VideoCapturerLollipop.LOGTAG, "changing surface size from " + VideoCapturerLollipop.this.mSurfaceResolution + " to " + chooseOptimalSize);
                                    VideoCapturerLollipop.this.mPreviewSurfaceHolder.setFixedSize(chooseOptimalSize.getWidth(), chooseOptimalSize.getHeight());
                                }
                            });
                            return;
                        }
                        Log.i(VideoCapturerLollipop.LOGTAG, "Adding preview surface " + surface);
                        arrayList.add(surface);
                    }
                    try {
                        CameraCharacteristics cameraCharacteristics = this.mCameraManager.getCameraCharacteristics(this.mCamera.getId());
                        int[] iArr = (int[]) cameraCharacteristics.get(CameraCharacteristics.CONTROL_AF_AVAILABLE_MODES);
                        Float f = (Float) cameraCharacteristics.get(CameraCharacteristics.LENS_INFO_MINIMUM_FOCUS_DISTANCE);
                        Boolean bool = (Boolean) cameraCharacteristics.get(CameraCharacteristics.FLASH_INFO_AVAILABLE);
                        Float f2 = (Float) cameraCharacteristics.get(CameraCharacteristics.SCALER_AVAILABLE_MAX_DIGITAL_ZOOM);
                        VideoCapturerLollipop.this.mHasFocus = f != null && f.floatValue() > 0.0f && VideoCapturerLollipop.contains(iArr, 1);
                        VideoCapturerLollipop videoCapturerLollipop = VideoCapturerLollipop.this;
                        if (bool != null && bool.booleanValue()) {
                            z = true;
                        }
                        videoCapturerLollipop.mHasTorch = z;
                        VideoCapturerLollipop.this.mMaxZoom = f2 != null ? f2.floatValue() : 1.0f;
                        VideoCapturerLollipop.this.mZoom = Math.min(VideoCapturerLollipop.this.mMaxZoom, VideoCapturerLollipop.this.mZoom);
                        Log.i(VideoCapturerLollipop.LOGTAG, "max digital zoom: " + f2 + ", min focus distance: " + f + ", flash available: " + bool);
                    } catch (Exception unused2) {
                    }
                    final Object obj = new Object();
                    final TakePictureRequest takePictureRequest = this.mTakePictureRequest;
                    if (takePictureRequest != null && takePictureRequest.mJpegImageReader != null) {
                        takePictureRequest.mJpegImageReaderOwner = obj;
                        arrayList.add(takePictureRequest.mJpegImageReader.getSurface());
                    }
                    final long currentTimeMillis = System.currentTimeMillis();
                    CameraCaptureSession.StateCallback stateCallback = new CameraCaptureSession.StateCallback() { // from class: com.bambuser.broadcaster.VideoCapturerLollipop.CameraHandler.3
                        @Override // android.hardware.camera2.CameraCaptureSession.StateCallback
                        public void onConfigured(CameraCaptureSession cameraCaptureSession) {
                            long currentTimeMillis2 = System.currentTimeMillis();
                            Log.i(VideoCapturerLollipop.LOGTAG, "capture session configured, took " + (currentTimeMillis2 - currentTimeMillis) + " ms");
                            CameraHandler.this.mCameraCaptureSession = cameraCaptureSession;
                            CameraHandler.this.mCameraState = CameraState.READY;
                            if (!z2) {
                                CameraHandler.this.notifyPreviewSurfaceNotInUse();
                            }
                            if (!CameraHandler.this.mNeedCameraReload && (!z2 || VideoCapturerLollipop.this.mHasPreviewSurface)) {
                                CameraHandler.this.mReuseFrames = false;
                                CameraHandler.this.mFrameHolder.clear();
                                if (takePictureRequest != null && takePictureRequest == CameraHandler.this.mTakePictureRequest) {
                                    Log.i(VideoCapturerLollipop.LOGTAG, "starting still capture");
                                    try {
                                        CameraCharacteristics cameraCharacteristics2 = CameraHandler.this.mCameraManager.getCameraCharacteristics(CameraHandler.this.mCamera.getId());
                                        CaptureRequest.Builder createCaptureRequest = CameraHandler.this.mCamera.createCaptureRequest(2);
                                        CameraHandler.this.internalSetSharedParameters(createCaptureRequest, cameraCharacteristics2);
                                        createCaptureRequest.addTarget(takePictureRequest.mJpegImageReader.getSurface());
                                        createCaptureRequest.set(CaptureRequest.JPEG_ORIENTATION, Integer.valueOf(CameraHandler.this.mCameraFrameRotation));
                                        createCaptureRequest.set(CaptureRequest.JPEG_QUALITY, Byte.valueOf((byte) com.treadly.client.lib.sdk.Managers.Message.MESSAGE_ID_LOG_USER_STATS_V2_DATA));
                                        createCaptureRequest.set(CaptureRequest.JPEG_THUMBNAIL_SIZE, new Size(0, 0));
                                        Location onGetLocation = takePictureRequest.mTakePictureCallback.onGetLocation();
                                        if (onGetLocation != null) {
                                            createCaptureRequest.set(CaptureRequest.JPEG_GPS_LOCATION, onGetLocation);
                                        }
                                        CameraHandler.this.mCameraCaptureSession.capture(createCaptureRequest.build(), takePictureRequest, CameraHandler.this);
                                    } catch (Exception e2) {
                                        Log.w(VideoCapturerLollipop.LOGTAG, "failed to start still capture: " + e2);
                                        SentryLogger.asyncMessage("Failed to start still capture", SentryLogger.Level.WARNING, null, e2);
                                        takePictureRequest.finish(null);
                                    }
                                }
                                if (VideoCapturerLollipop.this.mCapturer.isCapturing()) {
                                    CameraHandler.this.internalInitVideoCapture(false);
                                    return;
                                } else {
                                    CameraHandler.this.internalSetCaptureRequests(false);
                                    return;
                                }
                            }
                            Log.i(VideoCapturerLollipop.LOGTAG, "camera configured, but some change happened. reload camera");
                            if (DeviceInfoHandler.getCamera2HardwareLevel(VideoCapturerLollipop.this.mContext, CameraHandler.this.mCamera.getId()) == 2) {
                                CameraHandler.this.sendEmptyMessage(4);
                            }
                            CameraHandler.this.removeMessages(1);
                            CameraHandler.this.sendEmptyMessage(1);
                        }

                        @Override // android.hardware.camera2.CameraCaptureSession.StateCallback
                        public void onConfigureFailed(CameraCaptureSession cameraCaptureSession) {
                            CameraHandler.this.mCameraState = CameraState.FAILED;
                            CameraHandler.this.mCameraFailed = true;
                            Log.w(VideoCapturerLollipop.LOGTAG, "createCaptureSession onConfigureFailed");
                            SentryLogger.asyncMessage("createCaptureSession onConfigureFailed", SentryLogger.Level.ERROR, null, null);
                            CameraHandler.this.internalCloseCamera();
                            if (CameraHandler.this.mCameraObserver != null) {
                                CameraHandler.this.mCameraObserver.onCameraError(CameraError.COULD_NOT_OPEN);
                            }
                        }

                        @Override // android.hardware.camera2.CameraCaptureSession.StateCallback
                        public void onClosed(CameraCaptureSession cameraCaptureSession) {
                            Log.i(VideoCapturerLollipop.LOGTAG, "CameraCaptureSession onClosed");
                            if (takePictureRequest == null || takePictureRequest.mJpegImageReaderOwner != obj) {
                                return;
                            }
                            if (takePictureRequest.mJpegImageReader != null && takePictureRequest.mTakePictureCallback != null) {
                                Log.i(VideoCapturerLollipop.LOGTAG, "closing jpeg ImageReader");
                                takePictureRequest.mJpegImageReader.close();
                            }
                            takePictureRequest.mJpegImageReader = null;
                            takePictureRequest.mJpegImageReaderOwner = null;
                        }
                    };
                    Log.i(VideoCapturerLollipop.LOGTAG, "Creating new capture session");
                    this.mCameraState = CameraState.SESSION_OPENING;
                    try {
                        this.mCamera.createCaptureSession(arrayList, stateCallback, VideoCapturerLollipop.this.mCameraHandler);
                        return;
                    } catch (Exception e2) {
                        Log.w(VideoCapturerLollipop.LOGTAG, "createCaptureSession exception: " + e2);
                        SentryLogger.asyncMessage("createCaptureSession exception", SentryLogger.Level.ERROR, null, e2);
                        this.mCameraState = CameraState.FAILED;
                        this.mCameraFailed = true;
                        internalCloseCamera();
                        if (this.mCameraObserver != null) {
                            this.mCameraObserver.onCameraError(CameraError.COULD_NOT_OPEN);
                            return;
                        }
                        return;
                    }
                }
                Log.w(VideoCapturerLollipop.LOGTAG, "No preview resolutions");
                SentryLogger.asyncMessage("No preview resolutions", SentryLogger.Level.ERROR, null, null);
                this.mCameraState = CameraState.FAILED;
                this.mCameraFailed = true;
                internalCloseCamera();
                if (this.mCameraObserver != null) {
                    this.mCameraObserver.onCameraError(CameraError.PREVIEW_FAILED);
                }
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void notifyPreviewSurfaceNotInUse() {
            synchronized (VideoCapturerLollipop.this.mPreviewSurfaceLock) {
                VideoCapturerLollipop.this.mPreviewSurfaceLock.notifyAll();
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void internalSetCaptureRequests(boolean z) {
            if (this.mCamera == null || this.mCameraCaptureSession == null || this.mCameraState != CameraState.READY) {
                return;
            }
            Surface surface = VideoCapturerLollipop.this.mPreviewSurfaceHolder.getSurface();
            Surface surface2 = this.mImageReader != null ? this.mImageReader.getSurface() : null;
            boolean z2 = false;
            boolean z3 = z && surface2 != null;
            if (VideoCapturerLollipop.this.mHasPreviewSurface && surface != null) {
                z2 = true;
            }
            Log.i(VideoCapturerLollipop.LOGTAG, "internalSetCaptureRequests, preview: " + z2 + ", capture: " + z3);
            if (!z2 && !z3) {
                try {
                    this.mCameraCaptureSession.stopRepeating();
                } catch (Exception unused) {
                }
                internalCloseCamera();
                return;
            }
            try {
                this.mCaptureRequestBuilder = this.mCamera.createCaptureRequest(z3 ? 3 : 1);
                if (z2) {
                    this.mCaptureRequestBuilder.addTarget(surface);
                }
                if (z3) {
                    this.mCaptureRequestBuilder.addTarget(surface2);
                }
                CameraCharacteristics cameraCharacteristics = this.mCameraManager.getCameraCharacteristics(this.mCamera.getId());
                internalSetCaptureFpsRange(this.mCaptureRequestBuilder, cameraCharacteristics);
                internalSetSharedParameters(this.mCaptureRequestBuilder, cameraCharacteristics);
                this.mCameraCaptureSession.setRepeatingRequest(this.mCaptureRequestBuilder.build(), null, this);
                if (this.mCameraObserver != null) {
                    this.mCameraObserver.onCameraPreviewStateChanged(z2);
                }
            } catch (Exception e) {
                Log.w(VideoCapturerLollipop.LOGTAG, "set capture request exception: " + e);
                SentryLogger.asyncMessage("set capture request exception", SentryLogger.Level.ERROR, null, e);
                internalCloseCamera();
                if (this.mCameraObserver != null) {
                    this.mCameraObserver.onCameraError(CameraError.PREVIEW_FAILED);
                }
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void internalSetSharedParameters(CaptureRequest.Builder builder, CameraCharacteristics cameraCharacteristics) {
            int[] iArr = (int[]) cameraCharacteristics.get(CameraCharacteristics.CONTROL_AF_AVAILABLE_MODES);
            if (!VideoCapturerLollipop.contains(iArr, this.mSelectedFocusMode) && VideoCapturerLollipop.contains(iArr, 1)) {
                this.mSelectedFocusMode = 1;
            }
            if (VideoCapturerLollipop.contains(iArr, this.mSelectedFocusMode)) {
                builder.set(CaptureRequest.CONTROL_AF_MODE, Integer.valueOf(this.mSelectedFocusMode));
            }
            this.mToggleAutoFocus = false;
            if (VideoCapturerLollipop.this.mHasTorch) {
                builder.set(CaptureRequest.FLASH_MODE, Integer.valueOf(this.mTorchOn ? 2 : 0));
            }
            internalApplyZoom(builder, cameraCharacteristics);
        }

        private void internalSetCaptureFpsRange(CaptureRequest.Builder builder, CameraCharacteristics cameraCharacteristics) {
            this.mCaptureFpsRange = null;
            int round = Math.round((this.mCameraObserver != null ? this.mCameraObserver.onGetFrameRate() : HttpUrlConnectionNetworkFetcher.HTTP_DEFAULT_TIMEOUT) / 1000.0f);
            Range<Integer>[] rangeArr = (Range[]) cameraCharacteristics.get(CameraCharacteristics.CONTROL_AE_AVAILABLE_TARGET_FPS_RANGES);
            if (rangeArr == null || rangeArr.length <= 0) {
                return;
            }
            int i = 0;
            Range<Integer> range = rangeArr[0];
            int length = rangeArr.length;
            while (true) {
                if (i >= length) {
                    break;
                }
                Range<Integer> range2 = rangeArr[i];
                if (range2.getUpper().intValue() == round) {
                    range = range2;
                    break;
                }
                if (Math.abs(range2.getUpper().intValue() - round) < Math.abs(range.getUpper().intValue() - round)) {
                    range = range2;
                }
                i++;
            }
            builder.set(CaptureRequest.CONTROL_AE_TARGET_FPS_RANGE, range);
            this.mCaptureFpsRange = range;
        }

        private void internalFocus() {
            if (this.mCamera == null || this.mCameraCaptureSession == null || this.mCaptureRequestBuilder == null || this.mCameraState != CameraState.READY || !VideoCapturerLollipop.this.mHasFocus) {
                return;
            }
            try {
                int[] iArr = (int[]) this.mCameraManager.getCameraCharacteristics(this.mCamera.getId()).get(CameraCharacteristics.CONTROL_AF_AVAILABLE_MODES);
                if (this.mSelectedFocusMode == 3) {
                    if (VideoCapturerLollipop.contains(iArr, 1)) {
                        Log.i(VideoCapturerLollipop.LOGTAG, "focus called in continuous mode, selecting auto mode");
                        this.mSelectedFocusMode = 1;
                        this.mToggleAutoFocus = true;
                        this.mCaptureRequestBuilder.set(CaptureRequest.CONTROL_AF_MODE, Integer.valueOf(this.mSelectedFocusMode));
                        this.mCameraCaptureSession.setRepeatingRequest(this.mCaptureRequestBuilder.build(), this.mCaptureCallback, this);
                    }
                } else if (this.mSelectedFocusMode == 1) {
                    if (VideoCapturerLollipop.contains(iArr, 3)) {
                        internalCancelFocus(this.mCameraCaptureSession);
                        Log.i(VideoCapturerLollipop.LOGTAG, "focus called in auto mode, selecting continuous video mode");
                        this.mSelectedFocusMode = 3;
                        this.mToggleAutoFocus = false;
                        this.mCaptureRequestBuilder.set(CaptureRequest.CONTROL_AF_MODE, Integer.valueOf(this.mSelectedFocusMode));
                        this.mCameraCaptureSession.setRepeatingRequest(this.mCaptureRequestBuilder.build(), null, this);
                    } else if (this.mFocusTriggered) {
                        internalCancelFocus(this.mCameraCaptureSession);
                    } else {
                        internalTriggerFocus(this.mCameraCaptureSession);
                    }
                }
            } catch (Exception unused) {
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void internalTriggerFocus(CameraCaptureSession cameraCaptureSession) {
            Log.i(VideoCapturerLollipop.LOGTAG, "doing a single capture with focus trigger");
            this.mCaptureRequestBuilder.set(CaptureRequest.CONTROL_AF_TRIGGER, 1);
            try {
                cameraCaptureSession.capture(this.mCaptureRequestBuilder.build(), null, this);
            } catch (Exception unused) {
            }
            this.mCaptureRequestBuilder.set(CaptureRequest.CONTROL_AF_TRIGGER, 0);
            this.mFocusTriggered = true;
        }

        private void internalCancelFocus(CameraCaptureSession cameraCaptureSession) {
            Log.i(VideoCapturerLollipop.LOGTAG, "doing a single capture with focus cancel");
            this.mCaptureRequestBuilder.set(CaptureRequest.CONTROL_AF_TRIGGER, 2);
            try {
                cameraCaptureSession.capture(this.mCaptureRequestBuilder.build(), null, this);
            } catch (Exception unused) {
            }
            this.mCaptureRequestBuilder.set(CaptureRequest.CONTROL_AF_TRIGGER, 0);
            this.mFocusTriggered = false;
        }

        private void internalToggleTorch() {
            this.mTorchOn = !this.mTorchOn;
            if (VideoCapturerLollipop.this.mHasTorch) {
                try {
                    this.mCaptureRequestBuilder.set(CaptureRequest.FLASH_MODE, Integer.valueOf(this.mTorchOn ? 2 : 0));
                    this.mCameraCaptureSession.setRepeatingRequest(this.mCaptureRequestBuilder.build(), null, this);
                } catch (Exception unused) {
                }
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void internalInitVideoCapture(boolean z) {
            if (this.mCamera == null || this.mCameraState != CameraState.READY || this.mImageReader == null) {
                VideoCapturerLollipop.this.refreshCameraState(false);
                return;
            }
            this.mReuseFrames = false;
            this.mFrameHolder.clear();
            internalSetCaptureRotation();
            int intValue = this.mCaptureFpsRange != null ? this.mCaptureFpsRange.getUpper().intValue() * 1000 : HttpUrlConnectionNetworkFetcher.HTTP_DEFAULT_TIMEOUT;
            InfoFrame infoFrame = new InfoFrame(this.mImageReader.getImageFormat(), this.mCropRect.width(), this.mCropRect.height(), this.mCameraFrameRotation);
            int i = this.mLatestInitIndex + 1;
            this.mLatestInitIndex = i;
            infoFrame.mIndex = i;
            infoFrame.mFrameRate = intValue;
            VideoCapturerLollipop.this.mCapturer.onCameraReadyForCapture(infoFrame, z);
        }

        private void internalStartVideoCapture(InfoFrame infoFrame) {
            if (infoFrame.mIndex == this.mLatestInitIndex && VideoCapturerLollipop.this.mCapturer.isCapturing()) {
                Log.i(VideoCapturerLollipop.LOGTAG, "allocating Frame objects");
                for (int i = 0; i < VideoCapturerLollipop.this.mMaxFrames; i++) {
                    this.mFrameHolder.add(new Frame(infoFrame.mWidth, infoFrame.mHeight));
                }
                this.mReuseFrames = true;
            }
        }

        private void internalSetPreviewRotation() {
            int onGetPreviewRotation;
            if (this.mCamera == null) {
                return;
            }
            if (this.mCameraObserver != null && (onGetPreviewRotation = this.mCameraObserver.onGetPreviewRotation()) >= 0) {
                this.mPreviewRotationSetting = onGetPreviewRotation;
            }
            if (this.mPreviewRotationSetting < 0) {
                return;
            }
            CamInfo findCamInfo = DeviceInfoHandler.findCamInfo(VideoCapturerLollipop.this.mContext, this.mCamera.getId());
            this.mCameraPreviewRotation = DeviceInfoHandler.getFrameRotation(findCamInfo, this.mPreviewRotationSetting);
            if (findCamInfo == null || !findCamInfo.isFront()) {
                return;
            }
            this.mCameraPreviewRotation = (360 - this.mCameraPreviewRotation) % 360;
        }

        private boolean internalRefreshPreviewSurfaceLayout() {
            boolean z = false;
            if (!(VideoCapturerLollipop.this.mPreviewSurfaceView instanceof SurfaceViewWithAutoAR) || this.mCaptureResolution == null) {
                return false;
            }
            z = (this.mCameraPreviewRotation == 90 || this.mCameraPreviewRotation == 270) ? true : true;
            return ((SurfaceViewWithAutoAR) VideoCapturerLollipop.this.mPreviewSurfaceView).setAspectRatio(z ? this.mCaptureResolution.getHeight() : this.mCaptureResolution.getWidth(), z ? this.mCaptureResolution.getWidth() : this.mCaptureResolution.getHeight());
        }

        private void internalSetCaptureRotation() {
            int onGetCaptureRotation;
            if (this.mCamera == null) {
                return;
            }
            if (this.mCameraObserver != null && (onGetCaptureRotation = this.mCameraObserver.onGetCaptureRotation()) >= 0) {
                this.mCaptureRotationSetting = onGetCaptureRotation;
            }
            if (this.mCaptureRotationSetting < 0) {
                return;
            }
            this.mCameraFrameRotation = DeviceInfoHandler.getFrameRotation(DeviceInfoHandler.findCamInfo(VideoCapturerLollipop.this.mContext, this.mCamera.getId()), this.mCaptureRotationSetting);
        }

        private void internalTakePicture(VideoCapturerBase.TakePictureCallback takePictureCallback) {
            if (this.mTakePictureRequest != null) {
                takePictureCallback.onJpegData(null);
                return;
            }
            this.mTakePictureRequest = new TakePictureRequest(takePictureCallback);
            Log.i(VideoCapturerLollipop.LOGTAG, "internalTakePicture preparing new capture");
            this.mTakePictureRequest.prepare();
            if (this.mTakePictureRequest != null) {
                VideoCapturerLollipop.this.refreshCameraState(false);
            }
        }

        private void internalSetZoom(float f) {
            float max = Math.max(Math.min(f, VideoCapturerLollipop.this.mMaxZoom), 1.0f);
            if (Math.abs(max - VideoCapturerLollipop.this.mZoom) > 0.01f) {
                VideoCapturerLollipop.this.mZoom = max;
                try {
                    internalApplyZoom(this.mCaptureRequestBuilder, this.mCameraManager.getCameraCharacteristics(this.mCamera.getId()));
                    this.mCameraCaptureSession.setRepeatingRequest(this.mCaptureRequestBuilder.build(), null, this);
                } catch (Exception unused) {
                }
            }
        }

        private void internalStepZoom(boolean z) {
            float max = Math.max(0.5f, (VideoCapturerLollipop.this.mMaxZoom - 1.0f) / (VideoCapturerLollipop.this.mMaxZoom > 10.0f ? 20 : 10));
            float f = VideoCapturerLollipop.this.mZoom;
            if (!z) {
                max = -max;
            }
            internalSetZoom(f + max);
        }

        private void internalApplyZoom(CaptureRequest.Builder builder, CameraCharacteristics cameraCharacteristics) {
            Rect rect = (Rect) cameraCharacteristics.get(CameraCharacteristics.SENSOR_INFO_ACTIVE_ARRAY_SIZE);
            if (rect != null) {
                int width = (int) (rect.width() / VideoCapturerLollipop.this.mZoom);
                int height = (int) (rect.height() / VideoCapturerLollipop.this.mZoom);
                int height2 = (rect.height() - height) / 2;
                int width2 = (rect.width() - width) / 2;
                builder.set(CaptureRequest.SCALER_CROP_REGION, new Rect(width2, height2, width + width2, height + height2));
                return;
            }
            Log.w(VideoCapturerLollipop.LOGTAG, "internalApplyZoom missing activeArraySize");
        }

        private void internalReuseFrame(Frame frame) {
            try {
                frame.removeImage().close();
            } catch (Exception unused) {
            }
            if (this.mReuseFrames && this.mFrameHolder.size() < VideoCapturerLollipop.this.mMaxFrames) {
                this.mFrameHolder.add(frame);
            }
        }

        @Override // android.media.ImageReader.OnImageAvailableListener
        public void onImageAvailable(ImageReader imageReader) {
            Image image;
            try {
                image = imageReader.acquireNextImage();
            } catch (Exception unused) {
                image = null;
            }
            if (image == null) {
                return;
            }
            long timestamp = image.getTimestamp() / 1000000;
            long captureDuration = timestamp - VideoCapturerLollipop.this.mCapturer.getCaptureDuration();
            if (captureDuration > this.mImageTimelineOffset && this.mReuseFrames) {
                this.mImageTimelineOffset = captureDuration;
            }
            if (!this.mFrameHolder.isEmpty()) {
                Frame remove = this.mFrameHolder.remove(this.mFrameHolder.size() - 1);
                remove.mTimestamp = timestamp - this.mImageTimelineOffset;
                remove.mRotation = this.mCameraFrameRotation;
                remove.setImage(image);
                remove.setCropRect(this.mCropRect.top, this.mCropRect.left, this.mCropRect.width(), this.mCropRect.height());
                VideoCapturerLollipop.this.mCapturer.newFrame(remove);
                return;
            }
            VideoCapturerLollipop.this.mCapturer.onRawData(((image.getWidth() * image.getHeight()) * 3) / 2, false, false);
            image.close();
        }

        private void internalSetCropping() {
            if (this.mImageReader == null) {
                return;
            }
            Resolution onGetCroppedResolution = this.mCameraObserver != null ? this.mCameraObserver.onGetCroppedResolution() : null;
            if (onGetCroppedResolution == null) {
                onGetCroppedResolution = new Resolution(this.mImageReader.getWidth(), this.mImageReader.getHeight());
            }
            this.mCropRect.top = (this.mImageReader.getHeight() - onGetCroppedResolution.getHeight()) / 2;
            this.mCropRect.top = (this.mCropRect.top / 2) * 2;
            this.mCropRect.left = (this.mImageReader.getWidth() - onGetCroppedResolution.getWidth()) / 2;
            this.mCropRect.left = (this.mCropRect.left / 2) * 2;
            this.mCropRect.right = this.mCropRect.left + onGetCroppedResolution.getWidth();
            this.mCropRect.bottom = this.mCropRect.top + onGetCroppedResolution.getHeight();
        }

        /* JADX INFO: Access modifiers changed from: private */
        /* loaded from: classes.dex */
        public class TakePictureRequest extends CameraCaptureSession.CaptureCallback implements ImageReader.OnImageAvailableListener {
            private ImageReader mJpegImageReader;
            Object mJpegImageReaderOwner = null;
            private VideoCapturerBase.TakePictureCallback mTakePictureCallback;

            TakePictureRequest(VideoCapturerBase.TakePictureCallback takePictureCallback) {
                this.mTakePictureCallback = takePictureCallback;
            }

            void prepare() {
                CamInfo findCamInfo = DeviceInfoHandler.findCamInfo(VideoCapturerLollipop.this.mContext, CameraHandler.this.mCamera.getId());
                Resolution onGetResolution = this.mTakePictureCallback.onGetResolution();
                if (onGetResolution == null) {
                    onGetResolution = new Resolution(1920, 1080);
                }
                List<Resolution> pictureList = findCamInfo != null ? findCamInfo.getPictureList() : null;
                if (pictureList == null || pictureList.size() < 1) {
                    finish(null);
                    return;
                }
                Resolution resolution = pictureList.get(0);
                for (Resolution resolution2 : pictureList) {
                    if (resolution2.getWidth() >= resolution.getWidth() && resolution2.getHeight() >= resolution.getHeight() && resolution2.getWidth() <= onGetResolution.getWidth() && resolution2.getHeight() <= onGetResolution.getHeight()) {
                        resolution = resolution2;
                    }
                }
                this.mJpegImageReader = ImageReader.newInstance(resolution.getWidth(), resolution.getHeight(), 256, 1);
                this.mJpegImageReader.setOnImageAvailableListener(this, CameraHandler.this);
            }

            void finish(ByteBuffer byteBuffer) {
                if (this.mTakePictureCallback != null) {
                    this.mTakePictureCallback.onJpegData(byteBuffer);
                }
                this.mTakePictureCallback = null;
                if (this.mJpegImageReader != null && this.mJpegImageReaderOwner == null) {
                    this.mJpegImageReader.close();
                    this.mJpegImageReader = null;
                }
                CameraHandler.this.mTakePictureRequest = null;
            }

            @Override // android.hardware.camera2.CameraCaptureSession.CaptureCallback
            public void onCaptureStarted(CameraCaptureSession cameraCaptureSession, CaptureRequest captureRequest, long j, long j2) {
                VideoCapturerLollipop.this.mCapturer.triggerMediaActionSound(0);
            }

            @Override // android.hardware.camera2.CameraCaptureSession.CaptureCallback
            public void onCaptureFailed(CameraCaptureSession cameraCaptureSession, CaptureRequest captureRequest, CaptureFailure captureFailure) {
                Log.w(VideoCapturerLollipop.LOGTAG, "takePicture onCaptureFailed");
                finish(null);
                if (DeviceInfoHandler.getCamera2HardwareLevel(VideoCapturerLollipop.this.mContext, CameraHandler.this.mCamera.getId()) == 2) {
                    CameraHandler.this.sendEmptyMessage(4);
                }
                VideoCapturerLollipop.this.refreshCameraState(false);
            }

            /* JADX WARN: Removed duplicated region for block: B:13:0x0066  */
            @Override // android.media.ImageReader.OnImageAvailableListener
            /*
                Code decompiled incorrectly, please refer to instructions dump.
                To view partially-correct code enable 'Show inconsistent code' option in preferences
            */
            public void onImageAvailable(android.media.ImageReader r7) {
                /*
                    r6 = this;
                    r7 = 0
                    r0 = 0
                    java.lang.String r1 = "VideoCapturerLollipop"
                    java.lang.String r2 = "onImageAvailable copying jpeg"
                    android.util.Log.i(r1, r2)     // Catch: java.lang.Exception -> L2d
                    android.media.ImageReader r1 = r6.mJpegImageReader     // Catch: java.lang.Exception -> L2d
                    android.media.Image r1 = r1.acquireNextImage()     // Catch: java.lang.Exception -> L2d
                    android.media.Image$Plane[] r2 = r1.getPlanes()     // Catch: java.lang.Exception -> L2d
                    r2 = r2[r7]     // Catch: java.lang.Exception -> L2d
                    java.nio.ByteBuffer r2 = r2.getBuffer()     // Catch: java.lang.Exception -> L2d
                    int r3 = r2.remaining()     // Catch: java.lang.Exception -> L2d
                    java.nio.ByteBuffer r3 = java.nio.ByteBuffer.allocate(r3)     // Catch: java.lang.Exception -> L2d
                    r3.put(r2)     // Catch: java.lang.Exception -> L2b
                    r3.flip()     // Catch: java.lang.Exception -> L2b
                    r1.close()     // Catch: java.lang.Exception -> L2b
                    goto L4c
                L2b:
                    r1 = move-exception
                    goto L2f
                L2d:
                    r1 = move-exception
                    r3 = r0
                L2f:
                    java.lang.String r2 = "VideoCapturerLollipop"
                    java.lang.StringBuilder r4 = new java.lang.StringBuilder
                    r4.<init>()
                    java.lang.String r5 = "onImageAvailable failed to read jpeg image: "
                    r4.append(r5)
                    r4.append(r1)
                    java.lang.String r4 = r4.toString()
                    android.util.Log.w(r2, r4)
                    java.lang.String r2 = "onImageAvailable failed to read jpeg image"
                    com.bambuser.broadcaster.SentryLogger$Level r4 = com.bambuser.broadcaster.SentryLogger.Level.ERROR
                    com.bambuser.broadcaster.SentryLogger.asyncMessage(r2, r4, r0, r1)
                L4c:
                    r6.finish(r3)
                    com.bambuser.broadcaster.VideoCapturerLollipop$CameraHandler r0 = com.bambuser.broadcaster.VideoCapturerLollipop.CameraHandler.this
                    com.bambuser.broadcaster.VideoCapturerLollipop r0 = com.bambuser.broadcaster.VideoCapturerLollipop.this
                    android.content.Context r0 = r0.mContext
                    com.bambuser.broadcaster.VideoCapturerLollipop$CameraHandler r1 = com.bambuser.broadcaster.VideoCapturerLollipop.CameraHandler.this
                    android.hardware.camera2.CameraDevice r1 = com.bambuser.broadcaster.VideoCapturerLollipop.CameraHandler.access$1900(r1)
                    java.lang.String r1 = r1.getId()
                    int r0 = com.bambuser.broadcaster.DeviceInfoHandler.getCamera2HardwareLevel(r0, r1)
                    r1 = 2
                    if (r0 != r1) goto L6c
                    com.bambuser.broadcaster.VideoCapturerLollipop$CameraHandler r0 = com.bambuser.broadcaster.VideoCapturerLollipop.CameraHandler.this
                    r1 = 4
                    r0.sendEmptyMessage(r1)
                L6c:
                    com.bambuser.broadcaster.VideoCapturerLollipop$CameraHandler r6 = com.bambuser.broadcaster.VideoCapturerLollipop.CameraHandler.this
                    com.bambuser.broadcaster.VideoCapturerLollipop r6 = com.bambuser.broadcaster.VideoCapturerLollipop.this
                    r6.refreshCameraState(r7)
                    return
                */
                throw new UnsupportedOperationException("Method not decompiled: com.bambuser.broadcaster.VideoCapturerLollipop.CameraHandler.TakePictureRequest.onImageAvailable(android.media.ImageReader):void");
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static Resolution chooseOptimalSize(List<Resolution> list, int i, int i2, Resolution resolution) {
        ArrayList arrayList = new ArrayList();
        ArrayList arrayList2 = new ArrayList();
        double width = resolution.getWidth() / resolution.getHeight();
        for (Resolution resolution2 : list) {
            if (resolution2.getWidth() <= 1920 && resolution2.getHeight() <= 1920 && Math.abs((resolution2.getWidth() / resolution2.getHeight()) - width) <= 0.1d) {
                if (resolution2.getWidth() >= i && resolution2.getHeight() >= i2) {
                    arrayList.add(resolution2);
                } else {
                    arrayList2.add(resolution2);
                }
            }
        }
        if (arrayList.size() > 0) {
            return (Resolution) Collections.min(arrayList);
        }
        if (arrayList2.size() > 0) {
            return (Resolution) Collections.max(arrayList2);
        }
        Log.w(LOGTAG, "Couldn't find any suitable preview size");
        SentryLogger.asyncMessage("Couldn't find any suitable preview size", SentryLogger.Level.WARNING, null, null);
        return list.get(0);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static boolean contains(int[] iArr, int i) {
        if (iArr != null) {
            for (int i2 : iArr) {
                if (i2 == i) {
                    return true;
                }
            }
        }
        return false;
    }
}
