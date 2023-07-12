package com.opentok.android;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.PixelFormat;
import android.graphics.SurfaceTexture;
import android.hardware.Camera;
import android.os.Build;
import android.os.Handler;
import android.view.Display;
import android.view.WindowManager;
import com.facebook.imagepipeline.common.RotationOptions;
import com.opentok.android.BaseVideoCapturer;
import com.opentok.android.OtLog;
import com.opentok.android.Publisher;
import com.opentok.android.VideoUtils;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.locks.ReentrantLock;

/* JADX INFO: Access modifiers changed from: package-private */
@SuppressLint({"WrongConstant"})
/* loaded from: classes.dex */
public class DefaultVideoCapturer extends BaseVideoCapturer implements Camera.PreviewCallback, BaseVideoCapturer.CaptureSwitch {
    private static final int PIXEL_FORMAT = 17;
    private Camera camera;
    private int cameraIndex;
    private int[] captureFpsRange;
    private Display currentDisplay;
    int[] frame;
    private Publisher.CameraCaptureFrameRate preferredFramerate;
    private Publisher.CameraCaptureResolution preferredResolution;
    private SurfaceTexture surfaceTexture;
    private final OtLog.LogToken log = new OtLog.LogToken(this);
    private Camera.CameraInfo currentDeviceInfo = null;
    public ReentrantLock previewBufferLock = new ReentrantLock();
    PixelFormat pixelFormat = new PixelFormat();
    private boolean isCaptureStarted = false;
    private boolean isCaptureRunning = false;
    private final int numCaptureBuffers = 3;
    private int expectedFrameSize = 0;
    private int captureWidth = -1;
    private int captureHeight = -1;
    private boolean blackFrames = false;
    private boolean isCapturePaused = false;
    int fps = 1;
    int width = 0;
    int height = 0;
    Handler handler = new Handler();
    Runnable newFrame = new Runnable() { // from class: com.opentok.android.DefaultVideoCapturer.1
        @Override // java.lang.Runnable
        public void run() {
            if (DefaultVideoCapturer.this.isCaptureRunning) {
                if (DefaultVideoCapturer.this.frame == null) {
                    new VideoUtils.Size();
                    VideoUtils.Size preferredResolution = DefaultVideoCapturer.this.getPreferredResolution();
                    DefaultVideoCapturer defaultVideoCapturer = DefaultVideoCapturer.this;
                    defaultVideoCapturer.fps = defaultVideoCapturer.getPreferredFramerate();
                    DefaultVideoCapturer defaultVideoCapturer2 = DefaultVideoCapturer.this;
                    int i = preferredResolution.width;
                    defaultVideoCapturer2.width = i;
                    int i2 = preferredResolution.height;
                    defaultVideoCapturer2.height = i2;
                    defaultVideoCapturer2.frame = new int[i * i2];
                }
                DefaultVideoCapturer defaultVideoCapturer3 = DefaultVideoCapturer.this;
                defaultVideoCapturer3.provideIntArrayFrame(defaultVideoCapturer3.frame, 2, defaultVideoCapturer3.width, defaultVideoCapturer3.height, 0, false);
                DefaultVideoCapturer defaultVideoCapturer4 = DefaultVideoCapturer.this;
                defaultVideoCapturer4.handler.postDelayed(defaultVideoCapturer4.newFrame, 1000 / defaultVideoCapturer4.fps);
            }
        }
    };

    /* JADX INFO: Access modifiers changed from: package-private */
    /* renamed from: com.opentok.android.DefaultVideoCapturer$3  reason: invalid class name */
    /* loaded from: classes.dex */
    public static /* synthetic */ class AnonymousClass3 {
        static final /* synthetic */ int[] $SwitchMap$com$opentok$android$Publisher$CameraCaptureFrameRate;
        static final /* synthetic */ int[] $SwitchMap$com$opentok$android$Publisher$CameraCaptureResolution;

        static {
            int[] iArr = new int[Publisher.CameraCaptureFrameRate.values().length];
            $SwitchMap$com$opentok$android$Publisher$CameraCaptureFrameRate = iArr;
            try {
                iArr[Publisher.CameraCaptureFrameRate.FPS_30.ordinal()] = 1;
            } catch (NoSuchFieldError unused) {
            }
            try {
                $SwitchMap$com$opentok$android$Publisher$CameraCaptureFrameRate[Publisher.CameraCaptureFrameRate.FPS_15.ordinal()] = 2;
            } catch (NoSuchFieldError unused2) {
            }
            try {
                $SwitchMap$com$opentok$android$Publisher$CameraCaptureFrameRate[Publisher.CameraCaptureFrameRate.FPS_7.ordinal()] = 3;
            } catch (NoSuchFieldError unused3) {
            }
            try {
                $SwitchMap$com$opentok$android$Publisher$CameraCaptureFrameRate[Publisher.CameraCaptureFrameRate.FPS_1.ordinal()] = 4;
            } catch (NoSuchFieldError unused4) {
            }
            int[] iArr2 = new int[Publisher.CameraCaptureResolution.values().length];
            $SwitchMap$com$opentok$android$Publisher$CameraCaptureResolution = iArr2;
            try {
                iArr2[Publisher.CameraCaptureResolution.LOW.ordinal()] = 1;
            } catch (NoSuchFieldError unused5) {
            }
            try {
                $SwitchMap$com$opentok$android$Publisher$CameraCaptureResolution[Publisher.CameraCaptureResolution.MEDIUM.ordinal()] = 2;
            } catch (NoSuchFieldError unused6) {
            }
            try {
                $SwitchMap$com$opentok$android$Publisher$CameraCaptureResolution[Publisher.CameraCaptureResolution.HIGH.ordinal()] = 3;
            } catch (NoSuchFieldError unused7) {
            }
        }
    }

    public DefaultVideoCapturer(Context context, Publisher.CameraCaptureResolution cameraCaptureResolution, Publisher.CameraCaptureFrameRate cameraCaptureFrameRate) {
        this.cameraIndex = 0;
        this.preferredResolution = Publisher.CameraCaptureResolution.defaultResolution();
        this.preferredFramerate = Publisher.CameraCaptureFrameRate.defaultFrameRate();
        this.cameraIndex = getCameraIndexUsingFront(true);
        this.currentDisplay = ((WindowManager) context.getSystemService("window")).getDefaultDisplay();
        this.preferredFramerate = cameraCaptureFrameRate;
        this.preferredResolution = cameraCaptureResolution;
    }

    private void checkRangeWithWarning(int i, int[] iArr) {
        if (i < iArr[0] || i > iArr[1]) {
            this.log.w("Closest fps range found [%d-%d] for desired fps: %d", Integer.valueOf(iArr[0] / 1000), Integer.valueOf(iArr[1] / 1000), Integer.valueOf(i / 1000));
        }
    }

    private int compensateCameraRotation(int i) {
        int i2 = 0;
        switch (i) {
            case 1:
                i2 = RotationOptions.ROTATE_270;
                break;
            case 2:
                i2 = RotationOptions.ROTATE_180;
                break;
            case 3:
                i2 = 90;
                break;
        }
        int naturalCameraOrientation = getNaturalCameraOrientation();
        int roundRotation = roundRotation(i2);
        return isFrontCamera() ? (((360 - roundRotation) % 360) + naturalCameraOrientation) % 360 : (roundRotation + naturalCameraOrientation) % 360;
    }

    private VideoUtils.Size configureCaptureSize(int i, int i2) {
        int preferredFramerate = getPreferredFramerate();
        try {
            try {
                Camera.Parameters parameters = this.camera.getParameters();
                List<Camera.Size> supportedPreviewSizes = parameters.getSupportedPreviewSizes();
                this.captureFpsRange = findClosestEnclosingFpsRange(preferredFramerate * 1000, parameters.getSupportedPreviewFpsRange());
                int i3 = 0;
                int i4 = 0;
                for (int i5 = 0; i5 < supportedPreviewSizes.size(); i5++) {
                    Camera.Size size = supportedPreviewSizes.get(i5);
                    if (size.width >= i3 && size.height >= i4 && size.width <= i && size.height <= i2) {
                        i3 = size.width;
                        i4 = size.height;
                    }
                }
                if (i3 == 0 || i4 == 0) {
                    Camera.Size size2 = supportedPreviewSizes.get(0);
                    int i6 = size2.width;
                    i4 = size2.height;
                    i3 = i6;
                    for (int i7 = 1; i7 < supportedPreviewSizes.size(); i7++) {
                        Camera.Size size3 = supportedPreviewSizes.get(i7);
                        if (size3.width <= i3 && size3.height <= i4) {
                            i3 = size3.width;
                            i4 = size3.height;
                        }
                    }
                }
                this.captureWidth = i3;
                this.captureHeight = i4;
                return new VideoUtils.Size(i3, i4);
            } catch (RuntimeException e) {
                this.log.e(e, "Error configuring capture size", new Object[0]);
                error(e);
                return null;
            }
        } catch (Throwable unused) {
            return null;
        }
    }

    private int[] findClosestEnclosingFpsRange(final int i, List<int[]> list) {
        if (list == null || list.size() == 0) {
            return new int[]{0, 0};
        }
        if (isFrontCamera() && "samsung-sm-g900a".equals(Build.MODEL.toLowerCase(Locale.ROOT)) && 30000 == i) {
            i = 24000;
        }
        int[] iArr = (int[]) Collections.min(list, new Comparator<int[]>() { // from class: com.opentok.android.DefaultVideoCapturer.2
            private static final int MAX_FPS_DIFF_THRESHOLD = 5000;
            private static final int MAX_FPS_HIGH_DIFF_WEIGHT = 3;
            private static final int MAX_FPS_LOW_DIFF_WEIGHT = 1;
            private static final int MIN_FPS_HIGH_VALUE_WEIGHT = 4;
            private static final int MIN_FPS_LOW_VALUE_WEIGHT = 1;
            private static final int MIN_FPS_THRESHOLD = 8000;

            private int diff(int[] iArr2) {
                return progressivePenalty(iArr2[0], MIN_FPS_THRESHOLD, 1, 4) + progressivePenalty(Math.abs((i * 1000) - iArr2[1]), 5000, 1, 3);
            }

            private int progressivePenalty(int i2, int i3, int i4, int i5) {
                if (i2 < i3) {
                    return i2 * i4;
                }
                return ((i2 - i3) * i5) + (i4 * i3);
            }

            @Override // java.util.Comparator
            public int compare(int[] iArr2, int[] iArr3) {
                return diff(iArr2) - diff(iArr3);
            }
        });
        checkRangeWithWarning(i, iArr);
        return iArr;
    }

    private boolean forceCameraRelease(int i) {
        try {
            Camera open = Camera.open(i);
            if (open != null) {
                open.release();
                return false;
            }
            return false;
        } catch (RuntimeException unused) {
            return true;
        }
    }

    private static int getCameraIndexUsingFront(boolean z) {
        for (int i = 0; i < Camera.getNumberOfCameras(); i++) {
            Camera.CameraInfo cameraInfo = new Camera.CameraInfo();
            Camera.getCameraInfo(i, cameraInfo);
            if (z && cameraInfo.facing == 1) {
                return i;
            }
            if (!z && cameraInfo.facing == 0) {
                return i;
            }
        }
        return 0;
    }

    private int getNaturalCameraOrientation() {
        Camera.CameraInfo cameraInfo = this.currentDeviceInfo;
        if (cameraInfo != null) {
            return cameraInfo.orientation;
        }
        return 0;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public int getPreferredFramerate() {
        switch (AnonymousClass3.$SwitchMap$com$opentok$android$Publisher$CameraCaptureFrameRate[this.preferredFramerate.ordinal()]) {
            case 1:
                return 30;
            case 2:
                return 15;
            case 3:
                return 7;
            case 4:
                return 1;
            default:
                return 0;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public VideoUtils.Size getPreferredResolution() {
        int i;
        VideoUtils.Size size = new VideoUtils.Size();
        int i2 = AnonymousClass3.$SwitchMap$com$opentok$android$Publisher$CameraCaptureResolution[this.preferredResolution.ordinal()];
        if (i2 == 1) {
            size.width = 352;
            i = 288;
        } else if (i2 != 2) {
            if (i2 == 3) {
                size.width = 1280;
                i = 720;
            }
            return size;
        } else {
            size.width = 640;
            i = 480;
        }
        size.height = i;
        return size;
    }

    private static int roundRotation(int i) {
        return ((int) (Math.round(i / 90.0d) * 90)) % 360;
    }

    @Override // com.opentok.android.BaseVideoCapturer.CaptureSwitch
    public synchronized void cycleCamera() {
        swapCamera((getCameraIndex() + 1) % Camera.getNumberOfCameras());
    }

    @Override // com.opentok.android.BaseVideoCapturer
    public void destroy() {
    }

    @Override // com.opentok.android.BaseVideoCapturer.CaptureSwitch
    public int getCameraIndex() {
        return this.cameraIndex;
    }

    @Override // com.opentok.android.BaseVideoCapturer
    public BaseVideoCapturer.CaptureSettings getCaptureSettings() {
        BaseVideoCapturer.CaptureSettings captureSettings = new BaseVideoCapturer.CaptureSettings();
        VideoUtils.Size preferredResolution = getPreferredResolution();
        int preferredFramerate = getPreferredFramerate();
        if (this.camera == null || configureCaptureSize(preferredResolution.width, preferredResolution.height) == null) {
            captureSettings.fps = preferredFramerate;
            captureSettings.width = preferredResolution.width;
            captureSettings.height = preferredResolution.height;
            captureSettings.format = 2;
        } else {
            captureSettings.fps = preferredFramerate;
            captureSettings.width = preferredResolution.width;
            captureSettings.height = preferredResolution.height;
            captureSettings.format = 1;
            captureSettings.expectedDelay = 0;
        }
        captureSettings.mirrorInLocalRender = this.frameMirrorX;
        return captureSettings;
    }

    @Override // com.opentok.android.BaseVideoCapturer
    public synchronized void init() {
        this.log.d("init() enetered", new Object[0]);
        try {
            this.camera = Camera.open(this.cameraIndex);
        } catch (RuntimeException e) {
            this.log.e(e, "The camera is in use by another app", new Object[0]);
            error(e);
        }
        Camera.CameraInfo cameraInfo = new Camera.CameraInfo();
        this.currentDeviceInfo = cameraInfo;
        Camera.getCameraInfo(this.cameraIndex, cameraInfo);
        this.log.w("init() exit", new Object[0]);
    }

    @Override // com.opentok.android.BaseVideoCapturer
    public boolean isCaptureStarted() {
        return this.isCaptureStarted;
    }

    public boolean isFrontCamera() {
        Camera.CameraInfo cameraInfo = this.currentDeviceInfo;
        return cameraInfo != null && cameraInfo.facing == 1;
    }

    @Override // com.opentok.android.BaseVideoCapturer
    public synchronized void onPause() {
        if (this.isCaptureStarted) {
            this.isCapturePaused = true;
            stopCapture();
        }
    }

    @Override // android.hardware.Camera.PreviewCallback
    public void onPreviewFrame(byte[] bArr, Camera camera) {
        this.previewBufferLock.lock();
        if (this.isCaptureRunning && bArr.length == this.expectedFrameSize) {
            provideByteArrayFrame(bArr, 1, this.captureWidth, this.captureHeight, compensateCameraRotation(this.currentDisplay.getRotation()), isFrontCamera());
            camera.addCallbackBuffer(bArr);
        }
        this.previewBufferLock.unlock();
    }

    @Override // com.opentok.android.BaseVideoCapturer
    public void onResume() {
        if (this.isCapturePaused) {
            init();
            startCapture();
            this.isCapturePaused = false;
        }
    }

    public void setPublisher(Publisher publisher) {
        OtLog.LogToken logToken = this.log;
        logToken.d("setPublisher(" + publisher + ") called", new Object[0]);
        setPublisherKit(publisher);
    }

    @Override // com.opentok.android.BaseVideoCapturer
    public synchronized int startCapture() {
        this.log.d("startCapture() entered", new Object[0]);
        if (this.isCaptureStarted) {
            return -1;
        }
        if (this.camera != null) {
            VideoUtils.Size preferredResolution = getPreferredResolution();
            if (configureCaptureSize(preferredResolution.width, preferredResolution.height) == null) {
                return -1;
            }
            Camera.Parameters parameters = this.camera.getParameters();
            parameters.setPreviewSize(this.captureWidth, this.captureHeight);
            parameters.setPreviewFormat(17);
            int[] iArr = this.captureFpsRange;
            parameters.setPreviewFpsRange(iArr[0], iArr[1]);
            try {
                this.camera.setParameters(parameters);
                PixelFormat.getPixelFormatInfo(17, this.pixelFormat);
                int i = ((this.captureWidth * this.captureHeight) * this.pixelFormat.bitsPerPixel) / 8;
                for (int i2 = 0; i2 < 3; i2++) {
                    this.camera.addCallbackBuffer(new byte[i]);
                }
                try {
                    SurfaceTexture surfaceTexture = new SurfaceTexture(42);
                    this.surfaceTexture = surfaceTexture;
                    this.camera.setPreviewTexture(surfaceTexture);
                    this.camera.setPreviewCallbackWithBuffer(this);
                    this.camera.startPreview();
                    this.previewBufferLock.lock();
                    this.expectedFrameSize = i;
                    this.previewBufferLock.unlock();
                } catch (Exception e) {
                    error(e);
                    e.printStackTrace();
                    return -1;
                }
            } catch (RuntimeException e2) {
                this.log.e(e2, "Camera.setParameters(parameters) - failed", new Object[0]);
                error(e2);
                return -1;
            }
        } else {
            this.blackFrames = true;
            this.handler.postDelayed(this.newFrame, 1000 / this.fps);
        }
        this.isCaptureRunning = true;
        this.isCaptureStarted = true;
        this.log.d("startCapture() exit", new Object[0]);
        return 0;
    }

    @Override // com.opentok.android.BaseVideoCapturer
    public synchronized int stopCapture() {
        if (this.camera != null && this.isCaptureStarted) {
            this.previewBufferLock.lock();
            try {
                if (this.isCaptureRunning) {
                    this.isCaptureRunning = false;
                    this.camera.stopPreview();
                    this.camera.setPreviewCallbackWithBuffer(null);
                    this.camera.release();
                    this.camera = null;
                    this.log.d("Camera capture is stopped", new Object[0]);
                }
                this.previewBufferLock.unlock();
            } catch (RuntimeException e) {
                this.log.e(e, "stopPreview() failed ", new Object[0]);
                error(e);
                return -1;
            }
        }
        this.isCaptureStarted = false;
        if (this.blackFrames) {
            this.handler.removeCallbacks(this.newFrame);
        }
        return 0;
    }

    @Override // com.opentok.android.BaseVideoCapturer.CaptureSwitch
    @SuppressLint({"DefaultLocale"})
    public synchronized void swapCamera(int i) {
        boolean z = this.isCaptureStarted;
        if (this.camera != null) {
            stopCapture();
        }
        this.cameraIndex = i;
        if (z) {
            if (-1 != Build.MODEL.toLowerCase().indexOf("samsung")) {
                forceCameraRelease(i);
            }
            this.camera = Camera.open(i);
            Camera.CameraInfo cameraInfo = new Camera.CameraInfo();
            this.currentDeviceInfo = cameraInfo;
            Camera.getCameraInfo(i, cameraInfo);
            startCapture();
        }
    }
}
