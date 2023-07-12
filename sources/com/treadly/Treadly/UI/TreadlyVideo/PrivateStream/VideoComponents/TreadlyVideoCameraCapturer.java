package com.treadly.Treadly.UI.TreadlyVideo.PrivateStream.VideoComponents;

import android.annotation.SuppressLint;
import android.content.Context;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraCaptureSession;
import android.hardware.camera2.CameraCharacteristics;
import android.hardware.camera2.CameraDevice;
import android.hardware.camera2.CameraManager;
import android.hardware.camera2.CaptureRequest;
import android.hardware.camera2.params.StreamConfigurationMap;
import android.media.CamcorderProfile;
import android.media.Image;
import android.media.ImageReader;
import android.media.MediaRecorder;
import android.os.Handler;
import android.os.HandlerThread;
import android.util.Log;
import android.util.Range;
import android.util.Size;
import android.util.SparseArray;
import android.util.SparseIntArray;
import android.view.Display;
import android.view.WindowManager;
import com.facebook.imagepipeline.common.RotationOptions;
import com.opentok.android.BaseVideoCapturer;
import com.opentok.android.Publisher;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

/* loaded from: classes2.dex */
public class TreadlyVideoCameraCapturer extends BaseVideoCapturer implements BaseVideoCapturer.CaptureSwitch {
    private static final String LOG_TAG = "TreadlyVideoCameraCapturer";
    private static final int PIXEL_FORMAT = 35;
    private static final int PREFERRED_FACING_CAMERA = 0;
    private Range<Integer> camFps;
    private ImageReader cameraFrame;
    private int cameraIndex;
    private CameraManager cameraManager;
    private HandlerThread cameraThread;
    private Handler cameraThreadHandler;
    private CaptureRequest captureRequest;
    private CaptureRequest.Builder captureRequestBuilder;
    private CameraCaptureSession captureSession;
    private CameraInfoCache characteristics;
    private int desiredFps;
    private Display display;
    private DisplayOrientationCache displayOrientationCache;
    private Runnable executeAfterCameraOpened;
    private Runnable executeAfterClosed;
    private Size frameDimensions;
    private MediaRecorder mediaRecorder;
    private static final SparseIntArray rotationTable = new SparseIntArray() { // from class: com.treadly.Treadly.UI.TreadlyVideo.PrivateStream.VideoComponents.TreadlyVideoCameraCapturer.1
        {
            append(0, 0);
            append(1, 90);
            append(2, RotationOptions.ROTATE_180);
            append(3, RotationOptions.ROTATE_270);
        }
    };
    private static final SparseArray<Size> resolutionTable = new SparseArray<Size>() { // from class: com.treadly.Treadly.UI.TreadlyVideo.PrivateStream.VideoComponents.TreadlyVideoCameraCapturer.2
        {
            append(Publisher.CameraCaptureResolution.LOW.ordinal(), new Size(352, 288));
            append(Publisher.CameraCaptureResolution.MEDIUM.ordinal(), new Size(640, 480));
            append(Publisher.CameraCaptureResolution.HIGH.ordinal(), new Size(1280, 720));
        }
    };
    private static final SparseIntArray frameRateTable = new SparseIntArray() { // from class: com.treadly.Treadly.UI.TreadlyVideo.PrivateStream.VideoComponents.TreadlyVideoCameraCapturer.3
        {
            append(Publisher.CameraCaptureFrameRate.FPS_1.ordinal(), 1);
            append(Publisher.CameraCaptureFrameRate.FPS_7.ordinal(), 7);
            append(Publisher.CameraCaptureFrameRate.FPS_15.ordinal(), 15);
            append(Publisher.CameraCaptureFrameRate.FPS_30.ordinal(), 30);
        }
    };
    private CameraDevice.StateCallback cameraObserver = new CameraDevice.StateCallback() { // from class: com.treadly.Treadly.UI.TreadlyVideo.PrivateStream.VideoComponents.TreadlyVideoCameraCapturer.4
        @Override // android.hardware.camera2.CameraDevice.StateCallback
        public void onOpened(CameraDevice cameraDevice) {
            Log.d(TreadlyVideoCameraCapturer.LOG_TAG, "CameraDevice onOpened");
            TreadlyVideoCameraCapturer.this.cameraState = CameraState.OPEN;
            TreadlyVideoCameraCapturer.this.camera = cameraDevice;
            if (TreadlyVideoCameraCapturer.this.executeAfterCameraOpened != null) {
                TreadlyVideoCameraCapturer.this.executeAfterCameraOpened.run();
            }
        }

        @Override // android.hardware.camera2.CameraDevice.StateCallback
        public void onDisconnected(CameraDevice cameraDevice) {
            try {
                Log.d(TreadlyVideoCameraCapturer.LOG_TAG, "CameraDevice onDisconnected");
                TreadlyVideoCameraCapturer.this.camera.close();
            } catch (NullPointerException unused) {
            }
        }

        @Override // android.hardware.camera2.CameraDevice.StateCallback
        public void onError(CameraDevice cameraDevice, int i) {
            try {
                Log.d(TreadlyVideoCameraCapturer.LOG_TAG, "CameraDevice onError");
                TreadlyVideoCameraCapturer.this.camera.close();
            } catch (NullPointerException unused) {
            }
            TreadlyVideoCameraCapturer treadlyVideoCameraCapturer = TreadlyVideoCameraCapturer.this;
            treadlyVideoCameraCapturer.postAsyncException(new Camera2Exception("Camera Open Error: " + i));
        }

        @Override // android.hardware.camera2.CameraDevice.StateCallback
        public void onClosed(CameraDevice cameraDevice) {
            Log.d(TreadlyVideoCameraCapturer.LOG_TAG, "CameraDevice onClosed");
            super.onClosed(cameraDevice);
            TreadlyVideoCameraCapturer.this.cameraState = CameraState.CLOSED;
            TreadlyVideoCameraCapturer.this.camera = null;
            if (TreadlyVideoCameraCapturer.this.executeAfterClosed != null) {
                TreadlyVideoCameraCapturer.this.executeAfterClosed.run();
                TreadlyVideoCameraCapturer.this.executeAfterClosed = null;
            }
        }
    };
    private ImageReader.OnImageAvailableListener frameObserver = new ImageReader.OnImageAvailableListener() { // from class: com.treadly.Treadly.UI.TreadlyVideo.PrivateStream.VideoComponents.TreadlyVideoCameraCapturer.5
        @Override // android.media.ImageReader.OnImageAvailableListener
        public void onImageAvailable(ImageReader imageReader) {
            Image acquireNextImage = imageReader.acquireNextImage();
            if (acquireNextImage == null || ((acquireNextImage.getPlanes().length > 0 && acquireNextImage.getPlanes()[0].getBuffer() == null) || ((acquireNextImage.getPlanes().length > 1 && acquireNextImage.getPlanes()[1].getBuffer() == null) || (acquireNextImage.getPlanes().length > 2 && acquireNextImage.getPlanes()[2].getBuffer() == null)))) {
                Log.d(TreadlyVideoCameraCapturer.LOG_TAG, "onImageAvailable frame provided has no image data");
                return;
            }
            if (CameraState.CAPTURE == TreadlyVideoCameraCapturer.this.cameraState) {
                TreadlyVideoCameraCapturer.this.provideBufferFramePlanar(acquireNextImage.getPlanes()[0].getBuffer(), acquireNextImage.getPlanes()[1].getBuffer(), acquireNextImage.getPlanes()[2].getBuffer(), acquireNextImage.getPlanes()[0].getPixelStride(), acquireNextImage.getPlanes()[0].getRowStride(), acquireNextImage.getPlanes()[1].getPixelStride(), acquireNextImage.getPlanes()[1].getRowStride(), acquireNextImage.getPlanes()[2].getPixelStride(), acquireNextImage.getPlanes()[2].getRowStride(), acquireNextImage.getWidth(), acquireNextImage.getHeight(), TreadlyVideoCameraCapturer.this.calculateCamRotation(), TreadlyVideoCameraCapturer.this.isFrontCamera());
            }
            acquireNextImage.close();
        }
    };
    private CameraCaptureSession.StateCallback captureSessionObserver = new CameraCaptureSession.StateCallback() { // from class: com.treadly.Treadly.UI.TreadlyVideo.PrivateStream.VideoComponents.TreadlyVideoCameraCapturer.6
        @Override // android.hardware.camera2.CameraCaptureSession.StateCallback
        public void onConfigured(CameraCaptureSession cameraCaptureSession) {
            Log.d(TreadlyVideoCameraCapturer.LOG_TAG, "CaptureSession onConfigured");
            try {
                TreadlyVideoCameraCapturer.this.cameraState = CameraState.CAPTURE;
                TreadlyVideoCameraCapturer.this.captureSession = cameraCaptureSession;
                TreadlyVideoCameraCapturer.this.captureRequest = TreadlyVideoCameraCapturer.this.captureRequestBuilder.build();
                TreadlyVideoCameraCapturer.this.captureSession.setRepeatingRequest(TreadlyVideoCameraCapturer.this.captureRequest, TreadlyVideoCameraCapturer.this.captureNotification, TreadlyVideoCameraCapturer.this.cameraThreadHandler);
                if (TreadlyVideoCameraCapturer.this.isRecording) {
                    Log.i(TreadlyVideoCameraCapturer.LOG_TAG, "Recording has begun");
                    TreadlyVideoCameraCapturer.this.mediaRecorder.start();
                }
            } catch (CameraAccessException e) {
                e.printStackTrace();
            }
        }

        @Override // android.hardware.camera2.CameraCaptureSession.StateCallback
        public void onConfigureFailed(CameraCaptureSession cameraCaptureSession) {
            Log.d(TreadlyVideoCameraCapturer.LOG_TAG, "CaptureSession onFailed");
            TreadlyVideoCameraCapturer.this.cameraState = CameraState.ERROR;
            TreadlyVideoCameraCapturer.this.postAsyncException(new Camera2Exception("Camera session configuration failed"));
        }

        @Override // android.hardware.camera2.CameraCaptureSession.StateCallback
        public void onClosed(CameraCaptureSession cameraCaptureSession) {
            Log.d(TreadlyVideoCameraCapturer.LOG_TAG, "CaptureSession onClosed");
            if (TreadlyVideoCameraCapturer.this.camera != null) {
                TreadlyVideoCameraCapturer.this.camera.close();
            }
        }
    };
    private CameraCaptureSession.CaptureCallback captureNotification = new CameraCaptureSession.CaptureCallback() { // from class: com.treadly.Treadly.UI.TreadlyVideo.PrivateStream.VideoComponents.TreadlyVideoCameraCapturer.7
        @Override // android.hardware.camera2.CameraCaptureSession.CaptureCallback
        public void onCaptureStarted(CameraCaptureSession cameraCaptureSession, CaptureRequest captureRequest, long j, long j2) {
            super.onCaptureStarted(cameraCaptureSession, captureRequest, j, j2);
        }
    };
    boolean isRecording = false;
    boolean intentionalStop = false;
    private CameraDevice camera = null;
    private CameraState cameraState = CameraState.CLOSED;
    private ReentrantLock reentrantLock = new ReentrantLock();
    private Condition condition = this.reentrantLock.newCondition();
    private List<RuntimeException> runtimeExceptionList = new ArrayList();
    private boolean isPaused = false;

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes2.dex */
    public enum CameraState {
        CLOSED,
        CLOSING,
        SETUP,
        OPEN,
        CAPTURE,
        ERROR
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes2.dex */
    public static class CameraInfoCache {
        private boolean frontFacing;
        private CameraCharacteristics info;
        private int sensorOrientation;

        public CameraInfoCache(CameraCharacteristics cameraCharacteristics) {
            this.frontFacing = false;
            this.sensorOrientation = 0;
            this.frontFacing = ((Integer) cameraCharacteristics.get(CameraCharacteristics.LENS_FACING)).intValue() == 0;
            this.sensorOrientation = ((Integer) cameraCharacteristics.get(CameraCharacteristics.SENSOR_ORIENTATION)).intValue();
        }

        public <T> T get(CameraCharacteristics.Key<T> key) {
            return (T) this.info.get(key);
        }

        public boolean isFrontFacing() {
            return this.frontFacing;
        }

        public int sensorOrientation() {
            return this.sensorOrientation;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes2.dex */
    public static class DisplayOrientationCache implements Runnable {
        private static final int POLL_DELAY_MS = 750;
        private Display display;
        private int displayRotation;
        private Handler handler;

        public DisplayOrientationCache(Display display, Handler handler) {
            this.display = display;
            this.handler = handler;
            this.displayRotation = TreadlyVideoCameraCapturer.rotationTable.get(this.display.getRotation());
            this.handler.postDelayed(this, 750L);
        }

        public int getOrientation() {
            return this.displayRotation;
        }

        @Override // java.lang.Runnable
        public void run() {
            this.displayRotation = TreadlyVideoCameraCapturer.rotationTable.get(this.display.getRotation());
            this.handler.postDelayed(this, 750L);
        }
    }

    /* loaded from: classes2.dex */
    public static class Camera2Exception extends RuntimeException {
        public Camera2Exception(String str) {
            super(str);
        }
    }

    public TreadlyVideoCameraCapturer(Context context, Publisher.CameraCaptureResolution cameraCaptureResolution, Publisher.CameraCaptureFrameRate cameraCaptureFrameRate) {
        this.cameraManager = (CameraManager) context.getSystemService("camera");
        this.display = ((WindowManager) context.getSystemService("window")).getDefaultDisplay();
        this.frameDimensions = resolutionTable.get(cameraCaptureResolution.ordinal());
        this.desiredFps = frameRateTable.get(cameraCaptureFrameRate.ordinal());
        try {
            String selectCamera = selectCamera(0);
            if (selectCamera == null && this.cameraManager.getCameraIdList().length > 0) {
                selectCamera = this.cameraManager.getCameraIdList()[0];
            }
            this.cameraIndex = findCameraIndex(selectCamera);
        } catch (CameraAccessException e) {
            throw new Camera2Exception(e.getMessage());
        }
    }

    @Override // com.opentok.android.BaseVideoCapturer
    public synchronized void init() {
        Log.d(LOG_TAG, "init enter");
        this.characteristics = null;
        startCamThread();
        startDisplayOrientationCache();
        initCamera();
        this.mediaRecorder = new MediaRecorder();
        Log.d(LOG_TAG, "init exit");
    }

    /* JADX INFO: Access modifiers changed from: private */
    public int doStartCapture() {
        Log.d(LOG_TAG, "doStartCapture enter");
        try {
            if (isFrontCamera()) {
                this.captureRequestBuilder = this.camera.createCaptureRequest(1);
                this.captureRequestBuilder.addTarget(this.cameraFrame.getSurface());
                if (this.isRecording) {
                    this.captureRequestBuilder.addTarget(this.mediaRecorder.getSurface());
                }
                this.captureRequestBuilder.set(CaptureRequest.CONTROL_AE_TARGET_FPS_RANGE, this.camFps);
                this.captureRequestBuilder.set(CaptureRequest.CONTROL_MODE, 2);
                this.captureRequestBuilder.set(CaptureRequest.CONTROL_AF_MODE, 4);
                this.captureRequestBuilder.set(CaptureRequest.CONTROL_SCENE_MODE, 1);
                if (this.isRecording) {
                    this.camera.createCaptureSession(Arrays.asList(this.cameraFrame.getSurface(), this.mediaRecorder.getSurface()), this.captureSessionObserver, this.cameraThreadHandler);
                } else {
                    this.camera.createCaptureSession(Arrays.asList(this.cameraFrame.getSurface()), this.captureSessionObserver, this.cameraThreadHandler);
                }
            } else {
                this.captureRequestBuilder = this.camera.createCaptureRequest(3);
                this.captureRequestBuilder.addTarget(this.cameraFrame.getSurface());
                if (this.isRecording) {
                    this.captureRequestBuilder.addTarget(this.mediaRecorder.getSurface());
                }
                this.captureRequestBuilder.set(CaptureRequest.CONTROL_AE_TARGET_FPS_RANGE, this.camFps);
                this.captureRequestBuilder.set(CaptureRequest.CONTROL_AF_MODE, 4);
                if (this.isRecording) {
                    this.camera.createCaptureSession(Arrays.asList(this.cameraFrame.getSurface(), this.mediaRecorder.getSurface()), this.captureSessionObserver, this.cameraThreadHandler);
                } else {
                    this.camera.createCaptureSession(Arrays.asList(this.cameraFrame.getSurface()), this.captureSessionObserver, this.cameraThreadHandler);
                }
            }
            Log.d(LOG_TAG, "doStartCapture exit");
            return 0;
        } catch (CameraAccessException e) {
            throw new Camera2Exception(e.getMessage());
        }
    }

    @Override // com.opentok.android.BaseVideoCapturer
    public synchronized int startCapture() {
        String str = LOG_TAG;
        Log.d(str, "startCapture enter (cameraState: " + this.cameraState + ")");
        if (this.camera != null && CameraState.OPEN == this.cameraState) {
            return doStartCapture();
        } else if (CameraState.SETUP == this.cameraState) {
            Log.d(LOG_TAG, "camera not yet ready, queuing the start until camera is opened.");
            this.executeAfterCameraOpened = new Runnable() { // from class: com.treadly.Treadly.UI.TreadlyVideo.PrivateStream.VideoComponents.-$$Lambda$TreadlyVideoCameraCapturer$LzKb8JmMthOfusWxe2oqpZfubRY
                @Override // java.lang.Runnable
                public final void run() {
                    TreadlyVideoCameraCapturer.this.doStartCapture();
                }
            };
            Log.d(LOG_TAG, "startCapture exit");
            return 0;
        } else {
            throw new Camera2Exception("Start Capture called before init successfully completed.");
        }
    }

    @Override // com.opentok.android.BaseVideoCapturer
    public synchronized int stopCapture() {
        Log.d(LOG_TAG, "stopCapture enter");
        if (this.camera != null && this.captureSession != null && CameraState.CLOSED != this.cameraState) {
            this.cameraState = CameraState.CLOSING;
            try {
                this.captureSession.stopRepeating();
            } catch (CameraAccessException e) {
                e.printStackTrace();
            }
            this.captureSession.close();
            this.cameraFrame.close();
            this.characteristics = null;
        }
        if (this.mediaRecorder != null && !this.intentionalStop) {
            this.mediaRecorder.release();
        } else if (this.intentionalStop) {
            this.intentionalStop = false;
        }
        Log.d(LOG_TAG, "stopCapture exit");
        return 0;
    }

    @Override // com.opentok.android.BaseVideoCapturer
    public synchronized void destroy() {
        Log.d(LOG_TAG, "destroy enter");
        stopDisplayOrientationCache();
        stopCamThread();
        Log.d(LOG_TAG, "destroy exit");
    }

    @Override // com.opentok.android.BaseVideoCapturer
    public boolean isCaptureStarted() {
        return this.cameraState == CameraState.CAPTURE;
    }

    @Override // com.opentok.android.BaseVideoCapturer
    public synchronized BaseVideoCapturer.CaptureSettings getCaptureSettings() {
        BaseVideoCapturer.CaptureSettings captureSettings;
        captureSettings = new BaseVideoCapturer.CaptureSettings();
        captureSettings.fps = this.desiredFps;
        captureSettings.width = this.cameraFrame != null ? this.cameraFrame.getWidth() : 0;
        captureSettings.height = this.cameraFrame != null ? this.cameraFrame.getHeight() : 0;
        captureSettings.format = 1;
        captureSettings.expectedDelay = 0;
        return captureSettings;
    }

    @Override // com.opentok.android.BaseVideoCapturer
    public synchronized void onPause() {
        Log.d(LOG_TAG, "onPause");
        if (AnonymousClass10.$SwitchMap$com$treadly$Treadly$UI$TreadlyVideo$PrivateStream$VideoComponents$TreadlyVideoCameraCapturer$CameraState[this.cameraState.ordinal()] == 1) {
            stopCapture();
            this.isPaused = true;
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* renamed from: com.treadly.Treadly.UI.TreadlyVideo.PrivateStream.VideoComponents.TreadlyVideoCameraCapturer$10  reason: invalid class name */
    /* loaded from: classes2.dex */
    public static /* synthetic */ class AnonymousClass10 {
        static final /* synthetic */ int[] $SwitchMap$com$treadly$Treadly$UI$TreadlyVideo$PrivateStream$VideoComponents$TreadlyVideoCameraCapturer$CameraState = new int[CameraState.values().length];

        static {
            try {
                $SwitchMap$com$treadly$Treadly$UI$TreadlyVideo$PrivateStream$VideoComponents$TreadlyVideoCameraCapturer$CameraState[CameraState.CAPTURE.ordinal()] = 1;
            } catch (NoSuchFieldError unused) {
            }
            try {
                $SwitchMap$com$treadly$Treadly$UI$TreadlyVideo$PrivateStream$VideoComponents$TreadlyVideoCameraCapturer$CameraState[CameraState.SETUP.ordinal()] = 2;
            } catch (NoSuchFieldError unused2) {
            }
        }
    }

    @Override // com.opentok.android.BaseVideoCapturer
    public void onResume() {
        Log.d(LOG_TAG, "onResume");
        if (this.isPaused) {
            Runnable runnable = new Runnable() { // from class: com.treadly.Treadly.UI.TreadlyVideo.PrivateStream.VideoComponents.-$$Lambda$TreadlyVideoCameraCapturer$ar9KGKrn_hEetPVuXO2ZcM5GNKY
                @Override // java.lang.Runnable
                public final void run() {
                    TreadlyVideoCameraCapturer.lambda$onResume$1(TreadlyVideoCameraCapturer.this);
                }
            };
            if (this.cameraState == CameraState.CLOSING) {
                this.executeAfterClosed = runnable;
            } else if (this.cameraState == CameraState.CLOSED) {
                runnable.run();
            }
            this.isPaused = false;
            return;
        }
        Log.d(LOG_TAG, "Capturer was not paused when onResume was called");
    }

    public static /* synthetic */ void lambda$onResume$1(TreadlyVideoCameraCapturer treadlyVideoCameraCapturer) {
        treadlyVideoCameraCapturer.initCamera();
        treadlyVideoCameraCapturer.startCapture();
    }

    @Override // com.opentok.android.BaseVideoCapturer.CaptureSwitch
    public synchronized void cycleCamera() {
        try {
            swapCamera((this.cameraIndex + 1) % this.cameraManager.getCameraIdList().length);
        } catch (CameraAccessException e) {
            e.printStackTrace();
            throw new Camera2Exception(e.getMessage());
        }
    }

    @Override // com.opentok.android.BaseVideoCapturer.CaptureSwitch
    public int getCameraIndex() {
        return this.cameraIndex;
    }

    @Override // com.opentok.android.BaseVideoCapturer.CaptureSwitch
    public synchronized void swapCamera(int i) {
        final CameraState cameraState = this.cameraState;
        if (AnonymousClass10.$SwitchMap$com$treadly$Treadly$UI$TreadlyVideo$PrivateStream$VideoComponents$TreadlyVideoCameraCapturer$CameraState[cameraState.ordinal()] == 1) {
            this.intentionalStop = true;
            stopCapture();
        }
        this.cameraIndex = i;
        this.executeAfterClosed = new Runnable() { // from class: com.treadly.Treadly.UI.TreadlyVideo.PrivateStream.VideoComponents.-$$Lambda$TreadlyVideoCameraCapturer$20TegYbrUYGQbZCxsriw0tPDFVU
            @Override // java.lang.Runnable
            public final void run() {
                TreadlyVideoCameraCapturer.lambda$swapCamera$2(TreadlyVideoCameraCapturer.this, cameraState);
            }
        };
    }

    public static /* synthetic */ void lambda$swapCamera$2(TreadlyVideoCameraCapturer treadlyVideoCameraCapturer, CameraState cameraState) {
        if (AnonymousClass10.$SwitchMap$com$treadly$Treadly$UI$TreadlyVideo$PrivateStream$VideoComponents$TreadlyVideoCameraCapturer$CameraState[cameraState.ordinal()] != 1) {
            return;
        }
        treadlyVideoCameraCapturer.initCamera();
        treadlyVideoCameraCapturer.startCapture();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean isFrontCamera() {
        return this.characteristics != null && this.characteristics.isFrontFacing();
    }

    private void startCamThread() {
        this.cameraThread = new HandlerThread("Camera2VideoCapturer-Camera-Thread");
        this.cameraThread.start();
        this.cameraThreadHandler = new Handler(this.cameraThread.getLooper());
    }

    /* JADX WARN: Type inference failed for: r0v0, types: [android.os.HandlerThread, android.os.Handler] */
    private void stopCamThread() {
        try {
            try {
                this.cameraThread.quitSafely();
                this.cameraThread.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (NullPointerException unused) {
            }
        } finally {
            this.cameraThread = null;
            this.cameraThreadHandler = null;
        }
    }

    private String selectCamera(int i) throws CameraAccessException {
        String[] cameraIdList;
        CameraCharacteristics cameraCharacteristics;
        for (String str : this.cameraManager.getCameraIdList()) {
            if (i == ((Integer) this.cameraManager.getCameraCharacteristics(str).get(CameraCharacteristics.LENS_FACING)).intValue()) {
                Log.d(LOG_TAG, "selectCamera() Direction the camera faces relative to device screen: " + cameraCharacteristics.get(CameraCharacteristics.LENS_FACING));
                return str;
            }
        }
        return null;
    }

    private Range<Integer> selectCameraFpsRange(String str, final int i) throws CameraAccessException {
        String[] cameraIdList;
        for (String str2 : this.cameraManager.getCameraIdList()) {
            if (str2.equals(str)) {
                CameraCharacteristics cameraCharacteristics = this.cameraManager.getCameraCharacteristics(str2);
                ArrayList arrayList = new ArrayList();
                Collections.addAll(arrayList, (Object[]) cameraCharacteristics.get(CameraCharacteristics.CONTROL_AE_AVAILABLE_TARGET_FPS_RANGES));
                return (Range) Collections.min(arrayList, new Comparator<Range<Integer>>() { // from class: com.treadly.Treadly.UI.TreadlyVideo.PrivateStream.VideoComponents.TreadlyVideoCameraCapturer.8
                    @Override // java.util.Comparator
                    public int compare(Range<Integer> range, Range<Integer> range2) {
                        return calcError(range) - calcError(range2);
                    }

                    private int calcError(Range<Integer> range) {
                        return range.getLower().intValue() + Math.abs(range.getUpper().intValue() - i);
                    }
                });
            }
        }
        return null;
    }

    private int findCameraIndex(String str) throws CameraAccessException {
        String[] cameraIdList = this.cameraManager.getCameraIdList();
        for (int i = 0; i < cameraIdList.length; i++) {
            if (cameraIdList[i].equals(str)) {
                return i;
            }
        }
        return -1;
    }

    private Size selectPreferredSize(String str, final int i, final int i2, int i3) throws CameraAccessException {
        StreamConfigurationMap streamConfigurationMap = (StreamConfigurationMap) this.cameraManager.getCameraCharacteristics(str).get(CameraCharacteristics.SCALER_STREAM_CONFIGURATION_MAP);
        ArrayList arrayList = new ArrayList();
        streamConfigurationMap.getOutputFormats();
        Collections.addAll(arrayList, streamConfigurationMap.getOutputSizes(35));
        return (Size) Collections.min(arrayList, new Comparator<Size>() { // from class: com.treadly.Treadly.UI.TreadlyVideo.PrivateStream.VideoComponents.TreadlyVideoCameraCapturer.9
            @Override // java.util.Comparator
            public int compare(Size size, Size size2) {
                return (Math.abs(size.getWidth() - i) + Math.abs(size.getHeight() - i2)) - (Math.abs(size2.getWidth() - i) + Math.abs(size2.getHeight() - i2));
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public int calculateCamRotation() {
        if (this.characteristics != null) {
            int orientation = this.displayOrientationCache.getOrientation();
            int sensorOrientation = this.characteristics.sensorOrientation();
            if (!this.characteristics.isFrontFacing()) {
                return Math.abs((orientation - sensorOrientation) % 360);
            }
            return ((orientation + sensorOrientation) + 360) % 360;
        }
        return 0;
    }

    private void initMediaRecorder(File file) {
        String str = LOG_TAG;
        Log.d(str, "initMediaRecorder: " + file);
        this.mediaRecorder.setAudioSource(1);
        this.mediaRecorder.setVideoSource(2);
        this.mediaRecorder.setOutputFormat(2);
        this.mediaRecorder.setOutputFile(file.getAbsoluteFile());
        CamcorderProfile camcorderProfile = CamcorderProfile.get(4);
        this.mediaRecorder.setVideoFrameRate(camcorderProfile.videoFrameRate);
        this.mediaRecorder.setVideoSize(camcorderProfile.videoFrameWidth, camcorderProfile.videoFrameHeight);
        this.mediaRecorder.setVideoEncodingBitRate(camcorderProfile.videoBitRate);
        this.mediaRecorder.setVideoEncoder(2);
        this.mediaRecorder.setAudioEncoder(3);
        this.mediaRecorder.setAudioEncodingBitRate(camcorderProfile.audioBitRate);
        this.mediaRecorder.setAudioSamplingRate(camcorderProfile.audioSampleRate);
        this.mediaRecorder.setOrientationHint(RotationOptions.ROTATE_270);
        try {
            this.mediaRecorder.prepare();
        } catch (IOException unused) {
            Log.e(LOG_TAG, "Cannot record");
        }
    }

    public void startVideoRecoding(File file) {
        if (this.isRecording) {
            return;
        }
        Log.d(LOG_TAG, "startVideoRecording");
        this.isRecording = true;
        this.intentionalStop = true;
        stopCapture();
        initMediaRecorder(file);
        this.executeAfterClosed = new Runnable() { // from class: com.treadly.Treadly.UI.TreadlyVideo.PrivateStream.VideoComponents.-$$Lambda$TreadlyVideoCameraCapturer$hvk9Qu2fPojaXa-USZi664NH3D8
            @Override // java.lang.Runnable
            public final void run() {
                TreadlyVideoCameraCapturer.lambda$startVideoRecoding$3(TreadlyVideoCameraCapturer.this);
            }
        };
    }

    public static /* synthetic */ void lambda$startVideoRecoding$3(TreadlyVideoCameraCapturer treadlyVideoCameraCapturer) {
        treadlyVideoCameraCapturer.initCamera();
        treadlyVideoCameraCapturer.startCapture();
    }

    public void stopVideoRecording() {
        if (this.isRecording) {
            Log.d(LOG_TAG, "stopVideoRecording");
            this.isRecording = false;
            this.intentionalStop = true;
            stopCapture();
            this.mediaRecorder.stop();
            this.mediaRecorder.reset();
            Log.d(LOG_TAG, "Restarting capture");
            this.executeAfterClosed = new Runnable() { // from class: com.treadly.Treadly.UI.TreadlyVideo.PrivateStream.VideoComponents.-$$Lambda$TreadlyVideoCameraCapturer$o3DqMxLkZPRVubdp7S4X-mBs5_Y
                @Override // java.lang.Runnable
                public final void run() {
                    TreadlyVideoCameraCapturer.lambda$stopVideoRecording$4(TreadlyVideoCameraCapturer.this);
                }
            };
        }
    }

    public static /* synthetic */ void lambda$stopVideoRecording$4(TreadlyVideoCameraCapturer treadlyVideoCameraCapturer) {
        treadlyVideoCameraCapturer.initCamera();
        treadlyVideoCameraCapturer.startCapture();
    }

    @SuppressLint({"all"})
    private void initCamera() {
        Log.d(LOG_TAG, "initCamera()");
        try {
            this.cameraState = CameraState.SETUP;
            String str = this.cameraManager.getCameraIdList()[this.cameraIndex];
            this.camFps = selectCameraFpsRange(str, this.desiredFps);
            Size selectPreferredSize = selectPreferredSize(str, this.frameDimensions.getWidth(), this.frameDimensions.getHeight(), 35);
            this.cameraFrame = ImageReader.newInstance(selectPreferredSize.getWidth(), selectPreferredSize.getHeight(), 35, 3);
            this.cameraFrame.setOnImageAvailableListener(this.frameObserver, this.cameraThreadHandler);
            this.characteristics = new CameraInfoCache(this.cameraManager.getCameraCharacteristics(str));
            this.cameraManager.openCamera(str, this.cameraObserver, this.cameraThreadHandler);
        } catch (CameraAccessException e) {
            throw new Camera2Exception(e.getMessage());
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void postAsyncException(RuntimeException runtimeException) {
        this.runtimeExceptionList.add(runtimeException);
    }

    private void startDisplayOrientationCache() {
        this.displayOrientationCache = new DisplayOrientationCache(this.display, this.cameraThreadHandler);
    }

    private void stopDisplayOrientationCache() {
        this.cameraThreadHandler.removeCallbacks(this.displayOrientationCache);
    }
}
