package com.bambuser.broadcaster;

import android.app.ActivityManager;
import android.content.Context;
import android.graphics.Rect;
import android.hardware.Camera;
import android.location.Location;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import com.bambuser.broadcaster.Capturer;
import com.bambuser.broadcaster.VideoCapturerBase;
import com.facebook.appevents.AppEventsConstants;
import com.facebook.imagepipeline.producers.HttpUrlConnectionNetworkFetcher;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/* loaded from: classes.dex */
final class VideoCapturer extends VideoCapturerBase {
    private static final String LOGTAG = "VideoCapturer";
    private Camera mCamera;
    private int mCameraFrameRotation;
    private final CameraHandler mCameraHandler;
    private Capturer.CameraInterface mCameraObserver;
    private boolean mCameraParamsLogged;
    private int mCameraPreviewRotation;
    private int mCaptureRotationSetting;
    private final Rect mCropRect;
    private CamInfo mCurrentInfo;
    private boolean mFocusing;
    private final ArrayList<Frame> mFrameHolder;
    private volatile boolean mHasFocus;
    private volatile boolean mHasSurface;
    private volatile boolean mHasTorch;
    private int mLatestInitIndex;
    private final int mMaxFrames;
    private int mPreviewRotationSetting;
    private volatile boolean mPreviewRunning;
    private volatile boolean mResolutionChangeNeedsRestart;
    private boolean mReuseFrames;
    private boolean mTakingPicture;
    private volatile boolean mWaitingForSurface;
    private volatile int mZoom;
    private volatile List<Integer> mZoomRatios;

    /* JADX INFO: Access modifiers changed from: package-private */
    public VideoCapturer(Capturer capturer, Context context, SurfaceView surfaceView) {
        super(capturer, context, surfaceView);
        this.mFrameHolder = new ArrayList<>(16);
        this.mCropRect = new Rect();
        this.mCameraObserver = null;
        boolean z = false;
        this.mReuseFrames = false;
        this.mLatestInitIndex = 0;
        this.mHasSurface = false;
        this.mWaitingForSurface = true;
        this.mPreviewRunning = false;
        this.mResolutionChangeNeedsRestart = false;
        this.mFocusing = false;
        this.mTakingPicture = false;
        this.mCameraParamsLogged = false;
        this.mPreviewRotationSetting = -1;
        this.mCaptureRotationSetting = -1;
        this.mZoom = 0;
        this.mZoomRatios = null;
        this.mHasTorch = false;
        this.mHasFocus = false;
        Surface surface = this.mPreviewSurfaceHolder.getSurface();
        if (surface != null && surface.isValid()) {
            z = true;
        }
        this.mHasSurface = z;
        this.mWaitingForSurface = !this.mHasSurface;
        int memoryClass = ((ActivityManager) this.mContext.getSystemService("activity")).getMemoryClass();
        if (memoryClass > 128) {
            this.mMaxFrames = 8;
        } else if (memoryClass >= 64) {
            this.mMaxFrames = 6;
        } else {
            this.mMaxFrames = 2;
        }
        Log.i(LOGTAG, "memory class " + memoryClass + ", allowing " + this.mMaxFrames + " frames in encoder pipeline");
        HandlerThread handlerThread = new HandlerThread("CameraThread");
        handlerThread.start();
        this.mCameraHandler = new CameraHandler(handlerThread.getLooper());
    }

    @Override // com.bambuser.broadcaster.VideoCapturerBase
    void close() {
        super.close();
        if (Thread.currentThread() == this.mCameraHandler.getLooper().getThread()) {
            throw new RuntimeException("VideoCapturer.close() called on camera thread. can't wait for self!");
        }
        Object obj = new Object();
        synchronized (obj) {
            this.mCameraHandler.obtainMessage(10, obj).sendToTarget();
            while (true) {
                try {
                    obj.wait(2000L);
                } catch (InterruptedException unused) {
                }
            }
        }
    }

    @Override // com.bambuser.broadcaster.VideoCapturerBase
    void setCameraObserver(Capturer.CameraInterface cameraInterface) {
        this.mCameraHandler.obtainMessage(12, cameraInterface).sendToTarget();
    }

    @Override // com.bambuser.broadcaster.VideoCapturerBase
    void initVideoCapture() {
        this.mCameraHandler.sendEmptyMessage(3);
    }

    @Override // com.bambuser.broadcaster.VideoCapturerBase
    void startVideoCapture(InfoFrame infoFrame) {
        this.mCameraHandler.obtainMessage(4, infoFrame).sendToTarget();
    }

    @Override // com.bambuser.broadcaster.VideoCapturerBase
    void stopVideoCapture() {
        this.mCameraHandler.sendEmptyMessage(5);
    }

    @Override // com.bambuser.broadcaster.VideoCapturerBase
    void focus() {
        this.mCameraHandler.sendEmptyMessage(7);
    }

    @Override // com.bambuser.broadcaster.VideoCapturerBase
    void setZoom(int i) {
        this.mCameraHandler.obtainMessage(8, i, 0).sendToTarget();
    }

    @Override // com.bambuser.broadcaster.VideoCapturerBase
    void stepZoom(boolean z) {
        this.mCameraHandler.obtainMessage(9, z ? 1 : 0, 0).sendToTarget();
    }

    @Override // com.bambuser.broadcaster.VideoCapturerBase
    void reuseFrame(Frame frame) {
        this.mCameraHandler.obtainMessage(1, frame).sendToTarget();
    }

    @Override // com.bambuser.broadcaster.VideoCapturerBase
    void refreshPreviewResolution() {
        if (this.mResolutionChangeNeedsRestart) {
            refreshCameraState(false);
        }
    }

    @Override // com.bambuser.broadcaster.VideoCapturerBase
    void refreshCameraState(boolean z) {
        if (z) {
            this.mCameraHandler.removeMessages(2);
        }
        this.mCameraHandler.obtainMessage(2, z ? 1 : 0, 0).sendToTarget();
    }

    @Override // com.bambuser.broadcaster.VideoCapturerBase
    boolean hasZoom() {
        List<Integer> list = this.mZoomRatios;
        return list != null && list.size() > 1;
    }

    @Override // com.bambuser.broadcaster.VideoCapturerBase
    int getZoom() {
        int i = this.mZoom;
        List<Integer> list = this.mZoomRatios;
        if (list == null || i >= list.size()) {
            return 100;
        }
        return list.get(i).intValue();
    }

    @Override // com.bambuser.broadcaster.VideoCapturerBase
    List<Integer> getZoomRatios() {
        return this.mZoomRatios;
    }

    @Override // com.bambuser.broadcaster.VideoCapturerBase
    void toggleTorch() {
        this.mCameraHandler.sendEmptyMessage(11);
    }

    @Override // com.bambuser.broadcaster.VideoCapturerBase
    boolean hasTorch() {
        return this.mHasTorch;
    }

    @Override // com.bambuser.broadcaster.VideoCapturerBase
    boolean hasFocus() {
        return this.mHasFocus;
    }

    @Override // com.bambuser.broadcaster.VideoCapturerBase
    void takePicture(VideoCapturerBase.TakePictureCallback takePictureCallback) {
        this.mCameraHandler.obtainMessage(13, takePictureCallback).sendToTarget();
    }

    @Override // com.bambuser.broadcaster.VideoCapturerBase
    void refreshPreviewRotation() {
        this.mCameraHandler.sendEmptyMessage(14);
    }

    @Override // com.bambuser.broadcaster.VideoCapturerBase
    void refreshCaptureRotation() {
        this.mCameraHandler.sendEmptyMessage(15);
    }

    @Override // com.bambuser.broadcaster.VideoCapturerBase
    void refreshCropResolution() {
        this.mCameraHandler.sendEmptyMessage(16);
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes.dex */
    public class CameraHandler extends Handler implements Camera.PreviewCallback, Camera.ErrorCallback {
        static final int CLOSE = 10;
        static final int FOCUS = 7;
        static final int INIT_CAPTURE = 3;
        static final int REFRESH_CAMERA_STATE = 2;
        static final int REFRESH_CROP_RESOLUTION = 16;
        static final int REUSE_FRAME = 1;
        static final int SET_CAMERA_OBSERVER = 12;
        static final int SET_CAPTURE_ROTATION = 15;
        static final int SET_PREVIEW_ROTATION = 14;
        static final int SET_ZOOM = 8;
        static final int START_CAPTURE = 4;
        static final int STEP_ZOOM = 9;
        static final int STOP_CAMERA = 6;
        static final int STOP_CAPTURE = 5;
        static final int TAKEPICTURE = 13;
        static final int TOGGLE_TORCH = 11;

        CameraHandler(Looper looper) {
            super(looper);
        }

        @Override // android.os.Handler
        public void handleMessage(Message message) {
            switch (message.what) {
                case 1:
                    if (VideoCapturer.this.mCapturer.isCapturing()) {
                        VideoCapturer.this.internalReuseFrame((Frame) message.obj);
                        return;
                    }
                    return;
                case 2:
                    VideoCapturer.this.internalRefreshCamera(message.arg1 > 0);
                    return;
                case 3:
                    VideoCapturer.this.internalInitVideoCapture(true);
                    return;
                case 4:
                    VideoCapturer.this.internalStartVideoCapture((InfoFrame) message.obj);
                    return;
                case 5:
                    VideoCapturer.this.internalStopCapture();
                    return;
                case 6:
                    VideoCapturer.this.internalStopCamera(message.obj);
                    return;
                case 7:
                    VideoCapturer.this.internalFocus();
                    return;
                case 8:
                    VideoCapturer.this.internalSetZoom(message.arg1);
                    return;
                case 9:
                    VideoCapturer.this.internalStepZoom(message.arg1 > 0);
                    return;
                case 10:
                    VideoCapturer.this.internalStopCamera(message.obj);
                    getLooper().quit();
                    return;
                case 11:
                    VideoCapturer.this.internalToggleTorch();
                    return;
                case 12:
                    VideoCapturer.this.mCameraObserver = (Capturer.CameraInterface) message.obj;
                    return;
                case 13:
                    VideoCapturer.this.internalTakePicture((VideoCapturerBase.TakePictureCallback) message.obj);
                    return;
                case 14:
                    VideoCapturer.this.internalSetPreviewRotation();
                    return;
                case 15:
                    VideoCapturer.this.internalSetCaptureRotation();
                    return;
                case 16:
                    if (VideoCapturer.this.mCamera == null) {
                        return;
                    }
                    VideoCapturer.this.internalStopCapture();
                    VideoCapturer.this.internalSetCropping(VideoCapturer.this.tryGetParameters());
                    VideoCapturer.this.internalInitVideoCapture(false);
                    return;
                default:
                    Log.d(VideoCapturer.LOGTAG, "CameraHandler got an unknown message");
                    return;
            }
        }

        @Override // android.hardware.Camera.PreviewCallback
        public void onPreviewFrame(byte[] bArr, Camera camera) {
            if (bArr == null) {
                return;
            }
            if (!VideoCapturer.this.mFrameHolder.isEmpty()) {
                Frame frame = (Frame) VideoCapturer.this.mFrameHolder.remove(VideoCapturer.this.mFrameHolder.size() - 1);
                frame.mTimestamp = VideoCapturer.this.mCapturer.getCaptureDuration();
                frame.mRotation = VideoCapturer.this.mCameraFrameRotation;
                frame.setBuffer(bArr);
                VideoCapturer.this.mCapturer.newFrame(frame);
            } else if (VideoCapturer.this.mCamera != null) {
                VideoCapturer.this.mCapturer.onRawData(bArr.length, false, false);
                VideoCapturer.this.mCamera.addCallbackBuffer(bArr);
            }
        }

        @Override // android.hardware.Camera.ErrorCallback
        public void onError(int i, Camera camera) {
            if (i != 0) {
                Log.e(VideoCapturer.LOGTAG, "camera error callback, code: " + i);
                if (VideoCapturer.this.mCameraObserver != null) {
                    VideoCapturer.this.mCameraObserver.onCameraError(CameraError.ANDROID_INTERNAL_ERROR);
                }
                VideoCapturer.this.mCameraHandler.sendEmptyMessage(6);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void internalRefreshCamera(boolean z) {
        if (z || (!this.mHasSurface && !this.mCapturer.isCapturing())) {
            internalStopCamera(null);
        }
        if (this.mHasSurface) {
            internalStartCamera();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void internalInitVideoCapture(boolean z) {
        if (this.mPreviewRunning && this.mCamera != null && this.mCapturer.isCapturing()) {
            internalSetCaptureRotation();
            Camera.Parameters tryGetParameters = tryGetParameters();
            if (tryGetParameters != null) {
                InfoFrame infoFrame = new InfoFrame(tryGetParameters.getPreviewFormat(), this.mCropRect.width(), this.mCropRect.height(), this.mCameraFrameRotation);
                int i = this.mLatestInitIndex + 1;
                this.mLatestInitIndex = i;
                infoFrame.mIndex = i;
                int[] iArr = new int[2];
                tryGetParameters.getPreviewFpsRange(iArr);
                infoFrame.mFrameRate = iArr[1];
                if (infoFrame.mFrameRate >= 1 && infoFrame.mFrameRate <= 120) {
                    infoFrame.mFrameRate *= 1000;
                }
                infoFrame.mFrameRate = Math.max(infoFrame.mFrameRate, 1000);
                infoFrame.mFrameRate = Math.min(infoFrame.mFrameRate, 120000);
                this.mCapturer.onCameraReadyForCapture(infoFrame, z);
                return;
            }
            Log.w(LOGTAG, "Camera missing parameters, can not init capture!");
            if (this.mCameraObserver != null) {
                this.mCameraObserver.onCameraError(CameraError.CAPTURE_FAILED);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void internalStartVideoCapture(InfoFrame infoFrame) {
        if (infoFrame.mIndex != this.mLatestInitIndex) {
            return;
        }
        if (this.mCamera == null) {
            Log.w(LOGTAG, "Can't start capture, camera is null. preview not started?");
            if (this.mCameraObserver != null) {
                this.mCameraObserver.onCameraError(CameraError.CAPTURE_FAILED);
                return;
            }
            return;
        }
        Camera.Parameters tryGetParameters = tryGetParameters();
        if (tryGetParameters == null) {
            Log.w(LOGTAG, "Can't start capture, camera parameters missing.");
            if (this.mCameraObserver != null) {
                this.mCameraObserver.onCameraError(CameraError.CAPTURE_FAILED);
                return;
            }
            return;
        }
        Camera.Size previewSize = tryGetParameters.getPreviewSize();
        if (previewSize == null) {
            Log.w(LOGTAG, "Camera parameters missing preview size, can not start capture!");
            if (this.mCameraObserver != null) {
                this.mCameraObserver.onCameraError(CameraError.CAPTURE_FAILED);
                return;
            }
            return;
        }
        int previewFormat = tryGetParameters.getPreviewFormat();
        for (int i = 0; i < this.mMaxFrames; i++) {
            this.mFrameHolder.add(new Frame(previewFormat, previewSize.width, previewSize.height));
        }
        if (this.mCropRect.width() != previewSize.width || this.mCropRect.height() != previewSize.height) {
            for (int i2 = 0; i2 < this.mMaxFrames; i2++) {
                this.mFrameHolder.get(i2).setCropRect(this.mCropRect.top, this.mCropRect.left, this.mCropRect.width(), this.mCropRect.height());
            }
        }
        int neededSize = Frame.neededSize(previewFormat, previewSize.width, previewSize.height);
        for (int i3 = 0; i3 < this.mMaxFrames + 2; i3++) {
            this.mCamera.addCallbackBuffer(new byte[neededSize]);
        }
        this.mReuseFrames = true;
        this.mCamera.setPreviewCallbackWithBuffer(this.mCameraHandler);
    }

    @Override // android.view.SurfaceHolder.Callback
    public void surfaceChanged(SurfaceHolder surfaceHolder, int i, int i2, int i3) {
        boolean z;
        if (Log.isLoggable(LOGTAG, 3)) {
            Log.d(LOGTAG, "surfaceChanged callback");
        }
        synchronized (this) {
            z = this.mWaitingForSurface;
            this.mHasSurface = true;
            this.mWaitingForSurface = false;
        }
        if (!this.mPreviewRunning || z) {
            refreshCameraState(false);
        }
    }

    @Override // android.view.SurfaceHolder.Callback
    public void surfaceCreated(SurfaceHolder surfaceHolder) {
        if (Log.isLoggable(LOGTAG, 3)) {
            Log.d(LOGTAG, "surfaceCreated callback");
        }
        synchronized (this) {
            this.mHasSurface = true;
        }
    }

    @Override // android.view.SurfaceHolder.Callback
    public void surfaceDestroyed(SurfaceHolder surfaceHolder) {
        if (Log.isLoggable(LOGTAG, 3)) {
            Log.d(LOGTAG, "surfaceDestroyed callback");
        }
        synchronized (this) {
            this.mHasSurface = false;
            this.mWaitingForSurface = true;
        }
        if (!this.mCapturer.isCapturing() || DeviceInfoHandler.isGoogleN7() || DeviceInfoHandler.isSamsGalaxyTabLTE()) {
            stopVideoCapture();
            Object obj = new Object();
            synchronized (obj) {
                this.mCameraHandler.obtainMessage(6, obj).sendToTarget();
                while (true) {
                    try {
                        obj.wait(2000L);
                    } catch (InterruptedException unused) {
                    }
                }
            }
        }
    }

    private void logParams(Camera.Parameters parameters) {
        this.mCameraParamsLogged = true;
        try {
            String flatten = parameters.flatten();
            int i = 0;
            while (i < flatten.length()) {
                int i2 = i + 100;
                Log.v(LOGTAG, flatten.substring(i, Math.min(i2, flatten.length())));
                i = i2;
            }
        } catch (Exception e) {
            Log.v(LOGTAG, "Exception when trying to flatten Parameters for logging: " + e);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public Camera.Parameters tryGetParameters() {
        try {
            return this.mCamera.getParameters();
        } catch (Exception e) {
            Log.w(LOGTAG, "Could not get camera parameters: " + e);
            return null;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void internalSetZoom(int i) {
        List<Integer> list = this.mZoomRatios;
        if (this.mCamera == null || list == null || list.size() < 2) {
            return;
        }
        int size = list.size() - 1;
        int max = Math.max(100, Math.min(list.get(size).intValue(), i));
        int i2 = 0;
        for (int i3 = 1; i3 <= size; i3++) {
            if (list.get(i3).intValue() <= max) {
                i2 = i3;
            }
        }
        if (i2 != this.mZoom) {
            internalSetZoomValue(tryGetParameters(), i2);
        }
    }

    private void internalSetZoomValue(Camera.Parameters parameters, int i) {
        if (i > this.mZoomRatios.size() - 1) {
            i = this.mZoomRatios.size() - 1;
        }
        this.mZoom = i;
        if (parameters != null) {
            parameters.setZoom(this.mZoom);
            this.mCamera.setParameters(parameters);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void internalStepZoom(boolean z) {
        List<Integer> list = this.mZoomRatios;
        if (this.mCamera == null || list == null || list.size() < 2) {
            return;
        }
        int size = list.size() - 1;
        int max = Math.max(1, (size + 9) / 10);
        if (list.get(size).intValue() >= 1000) {
            max = Math.max(1, (size + 19) / 20);
        }
        int i = this.mZoom;
        if (!z) {
            max = -max;
        }
        int max2 = Math.max(0, Math.min(size, i + max));
        if (max2 != this.mZoom) {
            internalSetZoomValue(tryGetParameters(), max2);
        }
    }

    private void internalStartCamera() {
        boolean z;
        if (this.mWaitingForSurface) {
            return;
        }
        String onGetCameraId = this.mCameraObserver != null ? this.mCameraObserver.onGetCameraId() : AppEventsConstants.EVENT_PARAM_VALUE_NO;
        if (onGetCameraId.equals("-1")) {
            internalStopCamera(null);
        } else if (DeviceInfoHandler.isCameraDisabledByDPM(this.mContext)) {
            internalStopCamera(null);
            if (this.mCameraObserver != null) {
                this.mCameraObserver.onCameraError(CameraError.DISABLED_BY_DPM);
            }
        } else {
            if (this.mCamera == null) {
                for (CamInfo camInfo : DeviceInfoHandler.getSupportedCameras(this.mContext)) {
                    if (!camInfo.mId.equals(onGetCameraId) && camInfo.getPreviewList().isEmpty()) {
                        DeviceInfoHandler.scanResolutions(camInfo);
                    }
                    if (camInfo.mId.equals(onGetCameraId)) {
                        this.mCurrentInfo = camInfo;
                    }
                }
                if (Log.isLoggable(LOGTAG, 3)) {
                    Log.d(LOGTAG, "starting camera");
                }
                this.mCameraParamsLogged = false;
                try {
                    this.mCamera = Camera.open(Integer.parseInt(onGetCameraId));
                    if (this.mCamera == null) {
                        Log.w(LOGTAG, "camera id " + onGetCameraId + " could not be opened, got null");
                        if (this.mCameraObserver != null) {
                            this.mCameraObserver.onCameraError(CameraError.COULD_NOT_OPEN);
                            return;
                        }
                        return;
                    }
                    Camera.Parameters tryGetParameters = tryGetParameters();
                    if (tryGetParameters == null) {
                        internalStopCamera(null);
                        if (this.mCameraObserver != null) {
                            this.mCameraObserver.onCameraError(CameraError.COULD_NOT_OPEN);
                            return;
                        }
                        return;
                    }
                    DeviceInfoHandler.findSupportedResolutions(tryGetParameters, this.mCurrentInfo);
                    internalBroadcastResolutions();
                } catch (Exception e) {
                    Log.w(LOGTAG, "camera could not be opened: " + e);
                    if (this.mCameraObserver != null) {
                        this.mCameraObserver.onCameraError(CameraError.COULD_NOT_OPEN);
                        return;
                    }
                    return;
                }
            } else {
                internalStopCapture();
                if (this.mPreviewRunning) {
                    this.mPreviewRunning = false;
                    this.mCamera.stopPreview();
                }
            }
            this.mResolutionChangeNeedsRestart = true;
            Resolution defaultResolution = DeviceInfoHandler.getDefaultResolution(this.mCurrentInfo);
            Resolution onGetResolution = this.mCameraObserver != null ? this.mCameraObserver.onGetResolution() : defaultResolution;
            Camera.Parameters tryGetParameters2 = tryGetParameters();
            if (tryGetParameters2 == null) {
                internalStopCamera(null);
                if (this.mCameraObserver != null) {
                    this.mCameraObserver.onCameraError(CameraError.COULD_NOT_OPEN);
                    return;
                }
                return;
            }
            if (!this.mCameraParamsLogged) {
                logParams(tryGetParameters2);
            }
            int previewFormat = tryGetParameters2.getPreviewFormat();
            tryGetParameters2.setPreviewSize(onGetResolution.getWidth(), onGetResolution.getHeight());
            internalSetSuitableFpsRange(tryGetParameters2);
            switch (previewFormat) {
                case 16:
                case 17:
                    break;
                default:
                    Log.w(LOGTAG, "incompatible pixelformat: " + previewFormat + ". trying NV21");
                    tryGetParameters2.setPreviewFormat(17);
                    break;
            }
            List<String> supportedFocusModes = tryGetParameters2.getSupportedFocusModes();
            if (supportedFocusModes != null && supportedFocusModes.contains("continuous-video")) {
                tryGetParameters2.setFocusMode("continuous-video");
            }
            internalSetPictureParameters(tryGetParameters2, null, null);
            if (DeviceInfoHandler.useRecordingHint()) {
                try {
                    tryGetParameters2.setRecordingHint(true);
                } catch (Exception e2) {
                    Log.w(LOGTAG, "exception when trying to set recording hint to true: " + e2);
                }
            }
            try {
                this.mCamera.setParameters(tryGetParameters2);
                z = true;
            } catch (RuntimeException e3) {
                Log.w(LOGTAG, "setParameters failed: " + e3);
                z = false;
            }
            if (!z && !onGetResolution.equals(defaultResolution)) {
                Log.d(LOGTAG, "fallback from " + onGetResolution + " to default resolution " + defaultResolution);
                tryGetParameters2.setPreviewSize(defaultResolution.getWidth(), defaultResolution.getHeight());
                try {
                    this.mCamera.setParameters(tryGetParameters2);
                    z = true;
                } catch (RuntimeException e4) {
                    Log.w(LOGTAG, "setParameters failed: " + e4);
                }
            }
            if (!z) {
                internalStopCamera(null);
                if (this.mCameraObserver != null) {
                    this.mCameraObserver.onCameraError(CameraError.INVALID_PARAMETERS);
                    return;
                }
                return;
            }
            Camera.Parameters tryGetParameters3 = tryGetParameters();
            if (tryGetParameters3 == null) {
                internalStopCamera(null);
                if (this.mCameraObserver != null) {
                    this.mCameraObserver.onCameraError(CameraError.PREVIEW_FAILED);
                    return;
                }
                return;
            }
            this.mZoomRatios = (!tryGetParameters3.isZoomSupported() || tryGetParameters3.getMaxZoom() <= 0) ? null : tryGetParameters3.getZoomRatios();
            if (this.mZoomRatios != null && this.mZoomRatios.size() > 1) {
                internalSetZoomValue(tryGetParameters3, this.mZoom);
            }
            Camera.Size previewSize = tryGetParameters3.getPreviewSize();
            if (previewSize == null) {
                Log.w(LOGTAG, "Camera parameters missing preview size, can not start camera!");
                internalStopCamera(null);
                if (this.mCameraObserver != null) {
                    this.mCameraObserver.onCameraError(CameraError.PREVIEW_FAILED);
                    return;
                }
                return;
            }
            if ((previewSize.width != onGetResolution.getWidth() || previewSize.height != onGetResolution.getHeight()) && this.mCameraObserver != null) {
                this.mCameraObserver.onResolutionChangeNeeded(new Resolution(previewSize.width, previewSize.height));
            }
            internalCheckCameraFeatures(tryGetParameters3);
            this.mCamera.setErrorCallback(this.mCameraHandler);
            synchronized (this) {
                if (this.mHasSurface) {
                    internalSetPreviewRotation();
                    internalSetCaptureRotation();
                    internalSetCropping(tryGetParameters3);
                    if (internalRefreshPreviewSurface(previewSize)) {
                        this.mWaitingForSurface = true;
                        return;
                    }
                    try {
                        this.mCamera.setPreviewDisplay(this.mPreviewSurfaceHolder);
                        this.mCamera.startPreview();
                        this.mPreviewRunning = true;
                        if (this.mCameraObserver != null) {
                            this.mCameraObserver.onCameraPreviewStateChanged(true);
                        }
                    } catch (Exception e5) {
                        Log.w(LOGTAG, "exception when starting camera preview: " + e5);
                        internalStopCamera(null);
                        if (this.mCameraObserver != null) {
                            this.mCameraObserver.onCameraError(CameraError.PREVIEW_FAILED);
                        }
                        return;
                    }
                } else {
                    Log.w(LOGTAG, "Preview surface doesn't exist, app probably shutting down. not starting preview");
                    internalStopCamera(null);
                }
                internalInitVideoCapture(false);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void internalStopCamera(Object obj) {
        internalStopCapture();
        boolean z = this.mPreviewRunning;
        if (this.mCamera != null) {
            this.mCamera.setErrorCallback(null);
            if (this.mPreviewRunning) {
                this.mCamera.stopPreview();
            }
            this.mCamera.release();
            this.mCamera = null;
        }
        this.mCurrentInfo = null;
        this.mZoomRatios = null;
        this.mZoom = 0;
        this.mPreviewRunning = false;
        this.mResolutionChangeNeedsRestart = false;
        this.mFocusing = false;
        this.mHasFocus = false;
        this.mHasTorch = false;
        this.mTakingPicture = false;
        if (z && this.mCameraObserver != null) {
            this.mCameraObserver.onCameraPreviewStateChanged(false);
        }
        if (obj != null) {
            synchronized (obj) {
                obj.notifyAll();
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void internalStopCapture() {
        this.mReuseFrames = false;
        if (this.mCamera != null) {
            this.mCamera.setPreviewCallback(null);
        }
        this.mFrameHolder.clear();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void internalSetPreviewRotation() {
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
        this.mCameraPreviewRotation = DeviceInfoHandler.getFrameRotation(this.mCurrentInfo, this.mPreviewRotationSetting);
        if (this.mCurrentInfo != null && this.mCurrentInfo.isFront()) {
            this.mCameraPreviewRotation = (360 - this.mCameraPreviewRotation) % 360;
        }
        try {
            this.mCamera.setDisplayOrientation(this.mCameraPreviewRotation);
            Camera.Parameters tryGetParameters = tryGetParameters();
            if (!this.mPreviewRunning || tryGetParameters == null) {
                return;
            }
            internalRefreshPreviewSurface(tryGetParameters.getPreviewSize());
        } catch (Exception e) {
            Log.w(LOGTAG, "camera setDisplayOrientation failed: " + e);
            internalStopCamera(null);
            if (this.mCameraObserver != null) {
                this.mCameraObserver.onCameraError(CameraError.PREVIEW_FAILED);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void internalSetCaptureRotation() {
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
        this.mCameraFrameRotation = DeviceInfoHandler.getFrameRotation(this.mCurrentInfo, this.mCaptureRotationSetting);
        Camera.Parameters tryGetParameters = tryGetParameters();
        if (tryGetParameters != null) {
            tryGetParameters.setRotation(this.mCameraFrameRotation);
            this.mCamera.setParameters(tryGetParameters);
        }
    }

    private boolean internalRefreshPreviewSurface(Camera.Size size) {
        boolean z = false;
        if (this.mPreviewSurfaceView instanceof SurfaceViewWithAutoAR) {
            z = (this.mCameraPreviewRotation == 90 || this.mCameraPreviewRotation == 270) ? true : true;
            return ((SurfaceViewWithAutoAR) this.mPreviewSurfaceView).setAspectRatio(z ? size.height : size.width, z ? size.width : size.height);
        }
        return false;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void internalSetCropping(Camera.Parameters parameters) {
        Camera.Size previewSize;
        if (parameters == null || (previewSize = parameters.getPreviewSize()) == null) {
            return;
        }
        Resolution onGetCroppedResolution = this.mCameraObserver != null ? this.mCameraObserver.onGetCroppedResolution() : null;
        if (onGetCroppedResolution == null) {
            onGetCroppedResolution = new Resolution(previewSize.width, previewSize.height);
        }
        this.mCropRect.top = (previewSize.height - onGetCroppedResolution.getHeight()) / 2;
        this.mCropRect.top = (this.mCropRect.top / 2) * 2;
        this.mCropRect.left = (previewSize.width - onGetCroppedResolution.getWidth()) / 2;
        this.mCropRect.left = (this.mCropRect.left / 2) * 2;
        this.mCropRect.right = this.mCropRect.left + onGetCroppedResolution.getWidth();
        this.mCropRect.bottom = this.mCropRect.top + onGetCroppedResolution.getHeight();
    }

    private void internalSetSuitableFpsRange(Camera.Parameters parameters) {
        List<int[]> supportedPreviewFpsRange = parameters.getSupportedPreviewFpsRange();
        if (supportedPreviewFpsRange == null || supportedPreviewFpsRange.size() < 2) {
            return;
        }
        int onGetFrameRate = this.mCameraObserver != null ? this.mCameraObserver.onGetFrameRate() : HttpUrlConnectionNetworkFetcher.HTTP_DEFAULT_TIMEOUT;
        int[] iArr = supportedPreviewFpsRange.get(0);
        Iterator<int[]> it = supportedPreviewFpsRange.iterator();
        while (true) {
            if (!it.hasNext()) {
                break;
            }
            int[] next = it.next();
            if (next[1] == onGetFrameRate) {
                iArr = next;
                break;
            } else if (Math.abs(next[1] - onGetFrameRate) < Math.abs(iArr[1] - onGetFrameRate)) {
                iArr = next;
            }
        }
        parameters.setPreviewFpsRange(iArr[0], iArr[1]);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void internalFocus() {
        if (this.mCamera == null || !this.mPreviewRunning || this.mFocusing || !this.mHasFocus) {
            return;
        }
        this.mCamera.cancelAutoFocus();
        Camera.Parameters tryGetParameters = tryGetParameters();
        if (tryGetParameters == null) {
            return;
        }
        List<String> supportedFocusModes = tryGetParameters.getSupportedFocusModes();
        String focusMode = tryGetParameters.getFocusMode();
        if (focusMode == null || supportedFocusModes == null || supportedFocusModes.isEmpty()) {
            return;
        }
        if (focusMode.equals("auto") && supportedFocusModes.contains("continuous-video")) {
            tryGetParameters.setFocusMode("continuous-video");
            this.mCamera.setParameters(tryGetParameters);
            return;
        }
        if (focusMode.equals("continuous-video") && supportedFocusModes.contains("auto")) {
            tryGetParameters.setFocusMode("auto");
            this.mCamera.setParameters(tryGetParameters);
        }
        this.mFocusing = true;
        this.mCamera.autoFocus(new Camera.AutoFocusCallback() { // from class: com.bambuser.broadcaster.VideoCapturer.1
            @Override // android.hardware.Camera.AutoFocusCallback
            public void onAutoFocus(boolean z, Camera camera) {
                VideoCapturer.this.mFocusing = false;
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void internalToggleTorch() {
        if (this.mCamera == null || !this.mHasTorch) {
            return;
        }
        Camera.Parameters tryGetParameters = tryGetParameters();
        String flashMode = tryGetParameters != null ? tryGetParameters.getFlashMode() : null;
        if (flashMode == null) {
            return;
        }
        if (flashMode.equalsIgnoreCase("torch")) {
            tryGetParameters.setFlashMode("off");
        } else {
            tryGetParameters.setFlashMode("torch");
        }
        this.mCamera.setParameters(tryGetParameters);
    }

    private static void internalSetPictureParameters(Camera.Parameters parameters, Resolution resolution, Location location) {
        if (resolution == null) {
            resolution = new Resolution(1920, 1080);
        }
        List<Camera.Size> supportedPictureSizes = parameters.getSupportedPictureSizes();
        if (supportedPictureSizes == null) {
            return;
        }
        Camera.Size size = supportedPictureSizes.get(0);
        for (Camera.Size size2 : supportedPictureSizes) {
            if (size2.width < size.width || size2.height < size.height) {
                size = size2;
            }
        }
        for (Camera.Size size3 : supportedPictureSizes) {
            if (size3.height <= resolution.getHeight() && size3.width <= resolution.getWidth() && (size3.width > size.width || size3.height > size.height)) {
                size = size3;
            }
        }
        parameters.setPictureSize(size.width, size.height);
        parameters.setJpegThumbnailSize(0, 0);
        parameters.setJpegQuality(90);
        parameters.removeGpsData();
        if (location == null || location.getTime() <= 0 || location.getProvider() == null) {
            return;
        }
        parameters.setGpsLatitude(location.getLatitude());
        parameters.setGpsLongitude(location.getLongitude());
        parameters.setGpsAltitude(location.getAltitude());
        parameters.setGpsTimestamp(location.getTime() / 1000);
        parameters.setGpsProcessingMethod(location.getProvider().toUpperCase());
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void internalTakePicture(final VideoCapturerBase.TakePictureCallback takePictureCallback) {
        if (this.mCamera == null || !this.mPreviewRunning || this.mTakingPicture) {
            takePictureCallback.onJpegData(null);
            return;
        }
        this.mTakingPicture = true;
        try {
            Camera.Parameters parameters = this.mCamera.getParameters();
            internalSetPictureParameters(parameters, takePictureCallback.onGetResolution(), takePictureCallback.onGetLocation());
            this.mCamera.setParameters(parameters);
            if (this.mCameraObserver != null) {
                this.mCamera.enableShutterSound(this.mCameraObserver.onGetCaptureSounds());
            }
            this.mCamera.takePicture(new Camera.ShutterCallback() { // from class: com.bambuser.broadcaster.VideoCapturer.2
                @Override // android.hardware.Camera.ShutterCallback
                public void onShutter() {
                }
            }, null, null, new Camera.PictureCallback() { // from class: com.bambuser.broadcaster.VideoCapturer.3
                @Override // android.hardware.Camera.PictureCallback
                public void onPictureTaken(byte[] bArr, Camera camera) {
                    VideoCapturer.this.mTakingPicture = false;
                    if (VideoCapturer.this.mCamera != null && VideoCapturer.this.mCamera == camera) {
                        VideoCapturer.this.mCamera.startPreview();
                    }
                    takePictureCallback.onJpegData(bArr != null ? ByteBuffer.wrap(bArr) : null);
                }
            });
        } catch (RuntimeException e) {
            takePictureCallback.onJpegData(null);
            this.mTakingPicture = false;
            Log.w(LOGTAG, "camera takePicture failed: " + e);
            internalStopCamera(null);
            if (this.mCameraObserver != null) {
                this.mCameraObserver.onCameraError(CameraError.ANDROID_INTERNAL_ERROR);
            }
        }
    }

    private void internalCheckCameraFeatures(Camera.Parameters parameters) {
        List<String> supportedFlashModes = parameters != null ? parameters.getSupportedFlashModes() : null;
        String focusMode = parameters != null ? parameters.getFocusMode() : null;
        boolean z = true;
        this.mHasTorch = parameters != null && supportedFlashModes != null && supportedFlashModes.contains("torch") && supportedFlashModes.contains("off");
        if (focusMode == null || (!focusMode.equals("auto") && !focusMode.equals("continuous-picture") && !focusMode.equals("continuous-video") && !focusMode.equals("macro"))) {
            z = false;
        }
        this.mHasFocus = z;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void internalReuseFrame(Frame frame) {
        if (this.mReuseFrames) {
            byte[] removeBuffer = frame.removeBuffer();
            if (this.mCamera != null) {
                this.mCamera.addCallbackBuffer(removeBuffer);
            }
            if (this.mFrameHolder.size() < this.mMaxFrames) {
                this.mFrameHolder.add(frame);
            }
        }
    }
}
