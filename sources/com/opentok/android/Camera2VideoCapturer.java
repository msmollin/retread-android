package com.opentok.android;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.Context;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraCaptureSession;
import android.hardware.camera2.CameraCharacteristics;
import android.hardware.camera2.CameraDevice;
import android.hardware.camera2.CameraManager;
import android.hardware.camera2.CaptureRequest;
import android.hardware.camera2.params.StreamConfigurationMap;
import android.media.Image;
import android.media.ImageReader;
import android.os.Handler;
import android.os.HandlerThread;
import android.util.Range;
import android.util.Size;
import android.util.SparseArray;
import android.util.SparseIntArray;
import android.view.Display;
import android.view.Surface;
import android.view.WindowManager;
import androidx.annotation.RequiresApi;
import com.facebook.imagepipeline.common.RotationOptions;
import com.opentok.android.BaseVideoCapturer;
import com.opentok.android.OtLog;
import com.opentok.android.Publisher;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

/* JADX INFO: Access modifiers changed from: package-private */
@RequiresApi(21)
@TargetApi(21)
/* loaded from: classes.dex */
public class Camera2VideoCapturer extends BaseVideoCapturer implements BaseVideoCapturer.CaptureSwitch {
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
    private Condition condition;
    private int desiredFps;
    private Display display;
    private DisplayOrientationCache displayOrientationCache;
    private Runnable executeAfterCameraOpened;
    private Runnable executeAfterClosed;
    private Size frameDimensions;
    private boolean isPaused;
    private ReentrantLock reentrantLock;
    private List<RuntimeException> runtimeExceptionList;
    private static final SparseIntArray rotationTable = new SparseIntArray() { // from class: com.opentok.android.Camera2VideoCapturer.1
        {
            append(0, 0);
            append(1, 90);
            append(2, RotationOptions.ROTATE_180);
            append(3, RotationOptions.ROTATE_270);
        }
    };
    private static final SparseArray<Size> resolutionTable = new SparseArray<Size>() { // from class: com.opentok.android.Camera2VideoCapturer.2
        {
            append(Publisher.CameraCaptureResolution.LOW.ordinal(), new Size(352, 288));
            append(Publisher.CameraCaptureResolution.MEDIUM.ordinal(), new Size(640, 480));
            append(Publisher.CameraCaptureResolution.HIGH.ordinal(), new Size(1280, 720));
        }
    };
    private static final SparseIntArray frameRateTable = new SparseIntArray() { // from class: com.opentok.android.Camera2VideoCapturer.3
        {
            append(Publisher.CameraCaptureFrameRate.FPS_1.ordinal(), 1);
            append(Publisher.CameraCaptureFrameRate.FPS_7.ordinal(), 7);
            append(Publisher.CameraCaptureFrameRate.FPS_15.ordinal(), 15);
            append(Publisher.CameraCaptureFrameRate.FPS_30.ordinal(), 30);
        }
    };
    private final OtLog.LogToken log = new OtLog.LogToken(this);
    private CameraDevice.StateCallback cameraObserver = new CameraDevice.StateCallback() { // from class: com.opentok.android.Camera2VideoCapturer.4
        @Override // android.hardware.camera2.CameraDevice.StateCallback
        public void onClosed(CameraDevice cameraDevice) {
            Camera2VideoCapturer.this.log.d("CameraDevice onClosed", new Object[0]);
            super.onClosed(cameraDevice);
            Camera2VideoCapturer.this.cameraState = CameraState.CLOSED;
            Camera2VideoCapturer.this.camera = null;
            if (Camera2VideoCapturer.this.executeAfterClosed != null) {
                Camera2VideoCapturer.this.executeAfterClosed.run();
            }
        }

        @Override // android.hardware.camera2.CameraDevice.StateCallback
        public void onDisconnected(CameraDevice cameraDevice) {
            try {
                Camera2VideoCapturer.this.log.d("CameraDevice onDisconnected", new Object[0]);
                Camera2VideoCapturer.this.camera.close();
            } catch (NullPointerException unused) {
            }
        }

        @Override // android.hardware.camera2.CameraDevice.StateCallback
        public void onError(CameraDevice cameraDevice, int i) {
            try {
                Camera2VideoCapturer.this.log.d("CameraDevice onError", new Object[0]);
                Camera2VideoCapturer.this.camera.close();
            } catch (NullPointerException unused) {
            }
            Camera2VideoCapturer camera2VideoCapturer = Camera2VideoCapturer.this;
            camera2VideoCapturer.postAsyncException(new Camera2Exception("Camera Open Error: " + i));
        }

        @Override // android.hardware.camera2.CameraDevice.StateCallback
        public void onOpened(CameraDevice cameraDevice) {
            Camera2VideoCapturer.this.log.d("CameraDevice onOpened", new Object[0]);
            Camera2VideoCapturer.this.cameraState = CameraState.OPEN;
            Camera2VideoCapturer.this.camera = cameraDevice;
            if (Camera2VideoCapturer.this.executeAfterCameraOpened != null) {
                Camera2VideoCapturer.this.executeAfterCameraOpened.run();
            }
        }
    };
    private ImageReader.OnImageAvailableListener frameObserver = new ImageReader.OnImageAvailableListener() { // from class: com.opentok.android.Camera2VideoCapturer.5
        @Override // android.media.ImageReader.OnImageAvailableListener
        public void onImageAvailable(ImageReader imageReader) {
            Image acquireNextImage = imageReader.acquireNextImage();
            if (acquireNextImage == null || ((acquireNextImage.getPlanes().length > 0 && acquireNextImage.getPlanes()[0].getBuffer() == null) || ((acquireNextImage.getPlanes().length > 1 && acquireNextImage.getPlanes()[1].getBuffer() == null) || (acquireNextImage.getPlanes().length > 2 && acquireNextImage.getPlanes()[2].getBuffer() == null)))) {
                Camera2VideoCapturer.this.log.d("onImageAvailable frame provided has no image data", new Object[0]);
                return;
            }
            if (CameraState.CAPTURE == Camera2VideoCapturer.this.cameraState) {
                Camera2VideoCapturer.this.provideBufferFramePlanar(acquireNextImage.getPlanes()[0].getBuffer(), acquireNextImage.getPlanes()[1].getBuffer(), acquireNextImage.getPlanes()[2].getBuffer(), acquireNextImage.getPlanes()[0].getPixelStride(), acquireNextImage.getPlanes()[0].getRowStride(), acquireNextImage.getPlanes()[1].getPixelStride(), acquireNextImage.getPlanes()[1].getRowStride(), acquireNextImage.getPlanes()[2].getPixelStride(), acquireNextImage.getPlanes()[2].getRowStride(), acquireNextImage.getWidth(), acquireNextImage.getHeight(), Camera2VideoCapturer.this.calculateCamRotation(), Camera2VideoCapturer.this.isFrontCamera());
            }
            acquireNextImage.close();
        }
    };
    private CameraCaptureSession.StateCallback captureSessionObserver = new CameraCaptureSession.StateCallback() { // from class: com.opentok.android.Camera2VideoCapturer.6
        @Override // android.hardware.camera2.CameraCaptureSession.StateCallback
        public void onClosed(CameraCaptureSession cameraCaptureSession) {
            Camera2VideoCapturer.this.log.d("CaptureSession onClosed", new Object[0]);
            if (Camera2VideoCapturer.this.camera != null) {
                Camera2VideoCapturer.this.camera.close();
            }
        }

        @Override // android.hardware.camera2.CameraCaptureSession.StateCallback
        public void onConfigureFailed(CameraCaptureSession cameraCaptureSession) {
            Camera2VideoCapturer.this.log.d("CaptureSession onFailed", new Object[0]);
            Camera2VideoCapturer.this.cameraState = CameraState.ERROR;
            Camera2VideoCapturer.this.postAsyncException(new Camera2Exception("Camera session configuration failed"));
        }

        @Override // android.hardware.camera2.CameraCaptureSession.StateCallback
        public void onConfigured(CameraCaptureSession cameraCaptureSession) {
            Camera2VideoCapturer.this.log.d("CaptureSession onConfigured", new Object[0]);
            try {
                Camera2VideoCapturer.this.cameraState = CameraState.CAPTURE;
                Camera2VideoCapturer.this.captureSession = cameraCaptureSession;
                Camera2VideoCapturer.this.captureRequest = Camera2VideoCapturer.this.captureRequestBuilder.build();
                Camera2VideoCapturer.this.captureSession.setRepeatingRequest(Camera2VideoCapturer.this.captureRequest, Camera2VideoCapturer.this.captureNotification, null);
            } catch (CameraAccessException e) {
                e.printStackTrace();
            }
        }
    };
    private CameraCaptureSession.CaptureCallback captureNotification = new CameraCaptureSession.CaptureCallback() { // from class: com.opentok.android.Camera2VideoCapturer.7
        @Override // android.hardware.camera2.CameraCaptureSession.CaptureCallback
        public void onCaptureStarted(CameraCaptureSession cameraCaptureSession, CaptureRequest captureRequest, long j, long j2) {
            super.onCaptureStarted(cameraCaptureSession, captureRequest, j, j2);
        }
    };
    private CameraDevice camera = null;
    private CameraState cameraState = CameraState.CLOSED;

    /* JADX INFO: Access modifiers changed from: package-private */
    /* renamed from: com.opentok.android.Camera2VideoCapturer$10  reason: invalid class name */
    /* loaded from: classes.dex */
    public static /* synthetic */ class AnonymousClass10 {
        static final /* synthetic */ int[] $SwitchMap$com$opentok$android$Camera2VideoCapturer$CameraState;

        static {
            int[] iArr = new int[CameraState.values().length];
            $SwitchMap$com$opentok$android$Camera2VideoCapturer$CameraState = iArr;
            try {
                iArr[CameraState.CAPTURE.ordinal()] = 1;
            } catch (NoSuchFieldError unused) {
            }
            try {
                $SwitchMap$com$opentok$android$Camera2VideoCapturer$CameraState[CameraState.SETUP.ordinal()] = 2;
            } catch (NoSuchFieldError unused2) {
            }
        }
    }

    /* loaded from: classes.dex */
    public static class Camera2Exception extends RuntimeException {
        public Camera2Exception(String str) {
            super(str);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes.dex */
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
    /* loaded from: classes.dex */
    public enum CameraState {
        CLOSED,
        CLOSING,
        SETUP,
        OPEN,
        CAPTURE,
        ERROR
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes.dex */
    public static class DisplayOrientationCache implements Runnable {
        private static final int POLL_DELAY_MS = 750;
        private Display display;
        private int displayRotation;
        private Handler handler;

        public DisplayOrientationCache(Display display, Handler handler) {
            this.display = display;
            this.handler = handler;
            this.displayRotation = Camera2VideoCapturer.rotationTable.get(this.display.getRotation());
            this.handler.postDelayed(this, 750L);
        }

        public int getOrientation() {
            return this.displayRotation;
        }

        @Override // java.lang.Runnable
        public void run() {
            this.displayRotation = Camera2VideoCapturer.rotationTable.get(this.display.getRotation());
            this.handler.postDelayed(this, 750L);
        }
    }

    public Camera2VideoCapturer(Context context, Publisher.CameraCaptureResolution cameraCaptureResolution, Publisher.CameraCaptureFrameRate cameraCaptureFrameRate) {
        this.cameraManager = (CameraManager) context.getSystemService("camera");
        this.display = ((WindowManager) context.getSystemService("window")).getDefaultDisplay();
        ReentrantLock reentrantLock = new ReentrantLock();
        this.reentrantLock = reentrantLock;
        this.condition = reentrantLock.newCondition();
        this.frameDimensions = resolutionTable.get(cameraCaptureResolution.ordinal());
        this.desiredFps = frameRateTable.get(cameraCaptureFrameRate.ordinal());
        this.runtimeExceptionList = new ArrayList();
        this.isPaused = false;
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

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void a(CameraState cameraState) {
        if (AnonymousClass10.$SwitchMap$com$opentok$android$Camera2VideoCapturer$CameraState[cameraState.ordinal()] != 1) {
            return;
        }
        initCamera();
        startCapture();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void b() {
        initCamera();
        startCapture();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public int calculateCamRotation() {
        if (this.characteristics != null) {
            int orientation = this.displayOrientationCache.getOrientation();
            int sensorOrientation = this.characteristics.sensorOrientation();
            return !this.characteristics.isFrontFacing() ? Math.abs((orientation - sensorOrientation) % 360) : ((orientation + sensorOrientation) + 360) % 360;
        }
        return 0;
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* renamed from: doInit */
    public void a() {
        this.log.d("init enter", new Object[0]);
        this.characteristics = null;
        startCamThread();
        startDisplayOrientationCache();
        initCamera();
        this.log.d("init exit", new Object[0]);
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* renamed from: doStartCapture */
    public int c() {
        CameraDevice cameraDevice;
        List<Surface> asList;
        this.log.d("doStartCapture enter", new Object[0]);
        try {
            if (isFrontCamera()) {
                CaptureRequest.Builder createCaptureRequest = this.camera.createCaptureRequest(1);
                this.captureRequestBuilder = createCaptureRequest;
                createCaptureRequest.addTarget(this.cameraFrame.getSurface());
                this.captureRequestBuilder.set(CaptureRequest.CONTROL_AE_TARGET_FPS_RANGE, this.camFps);
                this.captureRequestBuilder.set(CaptureRequest.CONTROL_MODE, 2);
                this.captureRequestBuilder.set(CaptureRequest.CONTROL_AF_MODE, 4);
                this.captureRequestBuilder.set(CaptureRequest.CONTROL_SCENE_MODE, 1);
                cameraDevice = this.camera;
                asList = Arrays.asList(this.cameraFrame.getSurface());
            } else {
                CaptureRequest.Builder createCaptureRequest2 = this.camera.createCaptureRequest(3);
                this.captureRequestBuilder = createCaptureRequest2;
                createCaptureRequest2.addTarget(this.cameraFrame.getSurface());
                this.captureRequestBuilder.set(CaptureRequest.CONTROL_AE_TARGET_FPS_RANGE, this.camFps);
                this.captureRequestBuilder.set(CaptureRequest.CONTROL_AF_MODE, 4);
                cameraDevice = this.camera;
                asList = Arrays.asList(this.cameraFrame.getSurface());
            }
            cameraDevice.createCaptureSession(asList, this.captureSessionObserver, null);
            this.log.d("doStartCapture exit", new Object[0]);
            return 0;
        } catch (CameraAccessException e) {
            throw new Camera2Exception(e.getMessage());
        }
    }

    private int findCameraIndex(String str) {
        String[] cameraIdList = this.cameraManager.getCameraIdList();
        for (int i = 0; i < cameraIdList.length; i++) {
            if (cameraIdList[i].equals(str)) {
                return i;
            }
        }
        return -1;
    }

    private Size[] getCameraOutputSizes(String str) {
        return ((StreamConfigurationMap) this.cameraManager.getCameraCharacteristics(str).get(CameraCharacteristics.SCALER_STREAM_CONFIGURATION_MAP)).getOutputSizes(35);
    }

    private int getNextSupportedCameraIndex() {
        String[] cameraIdList = this.cameraManager.getCameraIdList();
        int length = cameraIdList.length;
        for (int i = 0; i < length; i++) {
            boolean z = true;
            int i2 = ((this.cameraIndex + i) + 1) % length;
            Size[] cameraOutputSizes = getCameraOutputSizes(cameraIdList[i2]);
            if ((cameraOutputSizes == null || cameraOutputSizes.length <= 0) ? false : false) {
                return i2;
            }
        }
        return -1;
    }

    @SuppressLint({"all"})
    private void initCamera() {
        this.log.d("initCamera()", new Object[0]);
        try {
            this.cameraState = CameraState.SETUP;
            String str = this.cameraManager.getCameraIdList()[this.cameraIndex];
            this.camFps = selectCameraFpsRange(str, this.desiredFps);
            Size selectPreferredSize = selectPreferredSize(str, this.frameDimensions.getWidth(), this.frameDimensions.getHeight());
            ImageReader newInstance = ImageReader.newInstance(selectPreferredSize.getWidth(), selectPreferredSize.getHeight(), 35, 3);
            this.cameraFrame = newInstance;
            newInstance.setOnImageAvailableListener(this.frameObserver, this.cameraThreadHandler);
            this.characteristics = new CameraInfoCache(this.cameraManager.getCameraCharacteristics(str));
            this.cameraManager.openCamera(str, this.cameraObserver, (Handler) null);
        } catch (CameraAccessException e) {
            throw new Camera2Exception(e.getMessage());
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean isFrontCamera() {
        CameraInfoCache cameraInfoCache = this.characteristics;
        return cameraInfoCache != null && cameraInfoCache.isFrontFacing();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void postAsyncException(RuntimeException runtimeException) {
        this.runtimeExceptionList.add(runtimeException);
    }

    private String selectCamera(int i) {
        String[] cameraIdList;
        CameraCharacteristics cameraCharacteristics;
        for (String str : this.cameraManager.getCameraIdList()) {
            if (i == ((Integer) this.cameraManager.getCameraCharacteristics(str).get(CameraCharacteristics.LENS_FACING)).intValue()) {
                this.log.d("selectCamera() Direction the camera faces relative to device screen: " + cameraCharacteristics.get(CameraCharacteristics.LENS_FACING), new Object[0]);
                return str;
            }
        }
        return null;
    }

    private Range<Integer> selectCameraFpsRange(String str, final int i) {
        String[] cameraIdList;
        for (String str2 : this.cameraManager.getCameraIdList()) {
            if (str2.equals(str)) {
                CameraCharacteristics cameraCharacteristics = this.cameraManager.getCameraCharacteristics(str2);
                ArrayList arrayList = new ArrayList();
                Collections.addAll(arrayList, (Object[]) cameraCharacteristics.get(CameraCharacteristics.CONTROL_AE_AVAILABLE_TARGET_FPS_RANGES));
                return (Range) Collections.min(arrayList, new Comparator<Range<Integer>>() { // from class: com.opentok.android.Camera2VideoCapturer.8
                    private int calcError(Range<Integer> range) {
                        return range.getLower().intValue() + Math.abs(range.getUpper().intValue() - i);
                    }

                    @Override // java.util.Comparator
                    public int compare(Range<Integer> range, Range<Integer> range2) {
                        return calcError(range) - calcError(range2);
                    }
                });
            }
        }
        return null;
    }

    private Size selectPreferredSize(String str, final int i, final int i2) {
        Size[] cameraOutputSizes = getCameraOutputSizes(str);
        ArrayList arrayList = new ArrayList();
        Collections.addAll(arrayList, cameraOutputSizes);
        return (Size) Collections.min(arrayList, new Comparator<Size>() { // from class: com.opentok.android.Camera2VideoCapturer.9
            @Override // java.util.Comparator
            public int compare(Size size, Size size2) {
                return (Math.abs(size.getWidth() - i) + Math.abs(size.getHeight() - i2)) - (Math.abs(size2.getWidth() - i) + Math.abs(size2.getHeight() - i2));
            }
        });
    }

    private void startCamThread() {
        HandlerThread handlerThread = new HandlerThread("Camera2VideoCapturer-Camera-Thread");
        this.cameraThread = handlerThread;
        handlerThread.start();
        this.cameraThreadHandler = new Handler(this.cameraThread.getLooper());
    }

    private void startDisplayOrientationCache() {
        this.displayOrientationCache = new DisplayOrientationCache(this.display, this.cameraThreadHandler);
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

    private void stopDisplayOrientationCache() {
        this.cameraThreadHandler.removeCallbacks(this.displayOrientationCache);
    }

    @Override // com.opentok.android.BaseVideoCapturer.CaptureSwitch
    public synchronized void cycleCamera() {
        try {
            int nextSupportedCameraIndex = getNextSupportedCameraIndex();
            if (!(nextSupportedCameraIndex != -1)) {
                throw new CameraAccessException(3, "No cameras with supported outputs found");
            }
            this.cameraIndex = nextSupportedCameraIndex;
            swapCamera(nextSupportedCameraIndex);
        } catch (CameraAccessException e) {
            e.printStackTrace();
            throw new Camera2Exception(e.getMessage());
        }
    }

    @Override // com.opentok.android.BaseVideoCapturer
    public synchronized void destroy() {
        this.log.d("destroy enter", new Object[0]);
        stopDisplayOrientationCache();
        stopCamThread();
        this.log.d("destroy exit", new Object[0]);
    }

    @Override // com.opentok.android.BaseVideoCapturer.CaptureSwitch
    public int getCameraIndex() {
        return this.cameraIndex;
    }

    @Override // com.opentok.android.BaseVideoCapturer
    public synchronized BaseVideoCapturer.CaptureSettings getCaptureSettings() {
        BaseVideoCapturer.CaptureSettings captureSettings;
        captureSettings = new BaseVideoCapturer.CaptureSettings();
        captureSettings.fps = this.desiredFps;
        ImageReader imageReader = this.cameraFrame;
        captureSettings.width = imageReader != null ? imageReader.getWidth() : 0;
        ImageReader imageReader2 = this.cameraFrame;
        captureSettings.height = imageReader2 != null ? imageReader2.getHeight() : 0;
        captureSettings.format = 1;
        captureSettings.expectedDelay = 0;
        captureSettings.mirrorInLocalRender = this.frameMirrorX;
        return captureSettings;
    }

    @Override // com.opentok.android.BaseVideoCapturer
    public synchronized void init() {
        if (this.cameraState == CameraState.CLOSING) {
            this.executeAfterClosed = new Runnable() { // from class: com.opentok.android.-$$Lambda$Camera2VideoCapturer$NidJxcKsJ-EvOrDvPlm3o0lz2jg
                @Override // java.lang.Runnable
                public final void run() {
                    Camera2VideoCapturer.this.a();
                }
            };
        } else {
            a();
        }
        this.cameraState = CameraState.SETUP;
    }

    @Override // com.opentok.android.BaseVideoCapturer
    public boolean isCaptureStarted() {
        return this.cameraState == CameraState.CAPTURE;
    }

    @Override // com.opentok.android.BaseVideoCapturer
    public synchronized void onPause() {
        this.log.d("onPause", new Object[0]);
        if (AnonymousClass10.$SwitchMap$com$opentok$android$Camera2VideoCapturer$CameraState[this.cameraState.ordinal()] == 1) {
            stopCapture();
            this.isPaused = true;
        }
    }

    @Override // com.opentok.android.BaseVideoCapturer
    public void onResume() {
        this.log.d("onResume", new Object[0]);
        if (!this.isPaused) {
            this.log.d("Capturer was not paused when onResume was called", new Object[0]);
            return;
        }
        Runnable runnable = new Runnable() { // from class: com.opentok.android.-$$Lambda$Camera2VideoCapturer$fqVl7NLwTykv-Hh9JqjeIqpXLcs
            @Override // java.lang.Runnable
            public final void run() {
                Camera2VideoCapturer.this.b();
            }
        };
        CameraState cameraState = this.cameraState;
        if (cameraState == CameraState.CLOSING) {
            this.executeAfterClosed = runnable;
        } else if (cameraState == CameraState.CLOSED) {
            runnable.run();
        }
        this.isPaused = false;
    }

    @Override // com.opentok.android.BaseVideoCapturer
    public synchronized int startCapture() {
        OtLog.LogToken logToken = this.log;
        logToken.d("startCapture enter (cameraState: " + this.cameraState + ")", new Object[0]);
        if (this.camera != null && CameraState.OPEN == this.cameraState) {
            return c();
        } else if (CameraState.SETUP == this.cameraState) {
            this.log.d("camera not yet ready, queuing the start until camera is opened.", new Object[0]);
            this.executeAfterCameraOpened = new Runnable() { // from class: com.opentok.android.-$$Lambda$Camera2VideoCapturer$dKCCSxa0GLLUACgL6BNYiMAFs2s
                @Override // java.lang.Runnable
                public final void run() {
                    Camera2VideoCapturer.this.c();
                }
            };
            this.log.d("startCapture exit", new Object[0]);
            return 0;
        } else {
            throw new Camera2Exception("Start Capture called before init successfully completed.");
        }
    }

    @Override // com.opentok.android.BaseVideoCapturer
    public synchronized int stopCapture() {
        CameraCaptureSession cameraCaptureSession;
        this.log.d("stopCapture enter", new Object[0]);
        if (this.camera != null && (cameraCaptureSession = this.captureSession) != null && CameraState.CLOSED != this.cameraState) {
            this.cameraState = CameraState.CLOSING;
            try {
                cameraCaptureSession.stopRepeating();
            } catch (CameraAccessException e) {
                e.printStackTrace();
            }
            this.captureSession.close();
            this.cameraFrame.close();
            this.characteristics = null;
        }
        this.log.d("stopCapture exit", new Object[0]);
        return 0;
    }

    @Override // com.opentok.android.BaseVideoCapturer.CaptureSwitch
    public synchronized void swapCamera(int i) {
        final CameraState cameraState = this.cameraState;
        if (AnonymousClass10.$SwitchMap$com$opentok$android$Camera2VideoCapturer$CameraState[cameraState.ordinal()] == 1) {
            stopCapture();
        }
        this.cameraIndex = i;
        this.executeAfterClosed = new Runnable() { // from class: com.opentok.android.-$$Lambda$Camera2VideoCapturer$TZgbvw-gdifJQ5Kvcn_L2Rhyct4
            @Override // java.lang.Runnable
            public final void run() {
                Camera2VideoCapturer.this.a(cameraState);
            }
        };
    }
}
