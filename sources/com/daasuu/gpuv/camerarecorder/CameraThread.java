package com.daasuu.gpuv.camerarecorder;

import android.annotation.SuppressLint;
import android.graphics.Rect;
import android.graphics.SurfaceTexture;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraCaptureSession;
import android.hardware.camera2.CameraCharacteristics;
import android.hardware.camera2.CameraDevice;
import android.hardware.camera2.CameraManager;
import android.hardware.camera2.CaptureRequest;
import android.hardware.camera2.params.MeteringRectangle;
import android.hardware.camera2.params.StreamConfigurationMap;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.util.Log;
import android.util.Size;
import android.view.Surface;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/* loaded from: classes.dex */
public class CameraThread extends Thread {
    private static final String TAG = "CameraThread";
    private CameraCaptureSession cameraCaptureSession;
    private CameraCaptureSession.StateCallback cameraCaptureSessionCallback;
    private CameraDevice cameraDevice;
    private CameraDevice.StateCallback cameraDeviceCallback;
    private final CameraManager cameraManager;
    private final CameraRecordListener cameraRecordListener;
    private Size cameraSize;
    private boolean flashSupport;
    private CameraHandler handler;
    private boolean isFlashTorch;
    volatile boolean isRunning;
    private final LensFacing lensFacing;
    private final OnStartPreviewListener listener;
    private final Object readyFence;
    private CaptureRequest.Builder requestBuilder;
    private Rect sensorArraySize;
    private SurfaceTexture surfaceTexture;

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes.dex */
    public interface OnStartPreviewListener {
        void onStart(Size size, boolean z);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public CameraThread(CameraRecordListener cameraRecordListener, OnStartPreviewListener onStartPreviewListener, SurfaceTexture surfaceTexture, CameraManager cameraManager, LensFacing lensFacing) {
        super("Camera thread");
        this.readyFence = new Object();
        this.isRunning = false;
        this.isFlashTorch = false;
        this.flashSupport = false;
        this.cameraDeviceCallback = new CameraDevice.StateCallback() { // from class: com.daasuu.gpuv.camerarecorder.CameraThread.1
            @Override // android.hardware.camera2.CameraDevice.StateCallback
            public void onOpened(CameraDevice cameraDevice) {
                Log.d(CameraThread.TAG, "cameraDeviceCallback onOpened");
                CameraThread.this.cameraDevice = cameraDevice;
                CameraThread.this.createCaptureSession();
            }

            @Override // android.hardware.camera2.CameraDevice.StateCallback
            public void onDisconnected(CameraDevice cameraDevice) {
                Log.d(CameraThread.TAG, "cameraDeviceCallback onDisconnected");
                cameraDevice.close();
                CameraThread.this.cameraDevice = null;
            }

            @Override // android.hardware.camera2.CameraDevice.StateCallback
            public void onError(CameraDevice cameraDevice, int i) {
                Log.d(CameraThread.TAG, "cameraDeviceCallback onError");
                cameraDevice.close();
                CameraThread.this.cameraDevice = null;
            }
        };
        this.cameraCaptureSessionCallback = new CameraCaptureSession.StateCallback() { // from class: com.daasuu.gpuv.camerarecorder.CameraThread.2
            @Override // android.hardware.camera2.CameraCaptureSession.StateCallback
            public void onConfigureFailed(CameraCaptureSession cameraCaptureSession) {
            }

            @Override // android.hardware.camera2.CameraCaptureSession.StateCallback
            public void onConfigured(CameraCaptureSession cameraCaptureSession) {
                CameraThread.this.cameraCaptureSession = cameraCaptureSession;
                CameraThread.this.updatePreview();
            }
        };
        this.listener = onStartPreviewListener;
        this.cameraRecordListener = cameraRecordListener;
        this.surfaceTexture = surfaceTexture;
        this.cameraManager = cameraManager;
        this.lensFacing = lensFacing;
    }

    public CameraHandler getHandler() {
        synchronized (this.readyFence) {
            try {
                this.readyFence.wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        return this.handler;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void updatePreview() {
        this.requestBuilder.set(CaptureRequest.CONTROL_AF_MODE, 3);
        HandlerThread handlerThread = new HandlerThread("CameraPreview");
        handlerThread.start();
        try {
            this.cameraCaptureSession.setRepeatingRequest(this.requestBuilder.build(), null, new Handler(handlerThread.getLooper()));
        } catch (CameraAccessException e) {
            e.printStackTrace();
        }
    }

    @Override // java.lang.Thread, java.lang.Runnable
    public void run() {
        Log.d(TAG, "Camera thread start");
        Looper.prepare();
        synchronized (this.readyFence) {
            this.handler = new CameraHandler(this);
            this.isRunning = true;
            this.readyFence.notify();
        }
        Looper.loop();
        Log.d(TAG, "Camera thread finish");
        if (this.cameraRecordListener != null) {
            this.cameraRecordListener.onCameraThreadFinish();
        }
        synchronized (this.readyFence) {
            this.handler = null;
            this.isRunning = false;
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @SuppressLint({"MissingPermission"})
    public final void startPreview(int i, int i2) {
        String[] cameraIdList;
        Log.v(TAG, "startPreview:");
        try {
            if (this.cameraManager == null) {
                return;
            }
            for (String str : this.cameraManager.getCameraIdList()) {
                CameraCharacteristics cameraCharacteristics = this.cameraManager.getCameraCharacteristics(str);
                if (cameraCharacteristics.get(CameraCharacteristics.LENS_FACING) != null && cameraCharacteristics.get(CameraCharacteristics.SENSOR_ORIENTATION) != null && ((Integer) cameraCharacteristics.get(CameraCharacteristics.LENS_FACING)).intValue() == this.lensFacing.getFacing()) {
                    this.sensorArraySize = (Rect) cameraCharacteristics.get(CameraCharacteristics.SENSOR_INFO_ACTIVE_ARRAY_SIZE);
                    this.flashSupport = ((Boolean) cameraCharacteristics.get(CameraCharacteristics.FLASH_INFO_AVAILABLE)).booleanValue();
                    StreamConfigurationMap streamConfigurationMap = (StreamConfigurationMap) cameraCharacteristics.get(CameraCharacteristics.SCALER_STREAM_CONFIGURATION_MAP);
                    if (i >= 0 && i2 >= 0) {
                        this.cameraSize = getClosestSupportedSize(Arrays.asList(streamConfigurationMap.getOutputSizes(SurfaceTexture.class)), i, i2);
                        Log.v(TAG, "cameraSize =" + this.cameraSize);
                        HandlerThread handlerThread = new HandlerThread("OpenCamera");
                        handlerThread.start();
                        this.cameraManager.openCamera(str, this.cameraDeviceCallback, new Handler(handlerThread.getLooper()));
                        return;
                    }
                    this.cameraSize = streamConfigurationMap.getOutputSizes(SurfaceTexture.class)[0];
                    Log.v(TAG, "cameraSize =" + this.cameraSize);
                    HandlerThread handlerThread2 = new HandlerThread("OpenCamera");
                    handlerThread2.start();
                    this.cameraManager.openCamera(str, this.cameraDeviceCallback, new Handler(handlerThread2.getLooper()));
                    return;
                }
            }
        } catch (CameraAccessException e) {
            e.printStackTrace();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void createCaptureSession() {
        this.surfaceTexture.setDefaultBufferSize(this.cameraSize.getWidth(), this.cameraSize.getHeight());
        Surface surface = new Surface(this.surfaceTexture);
        try {
            this.requestBuilder = this.cameraDevice.createCaptureRequest(3);
        } catch (CameraAccessException e) {
            e.printStackTrace();
        }
        this.requestBuilder.addTarget(surface);
        try {
            this.cameraDevice.createCaptureSession(Collections.singletonList(surface), this.cameraCaptureSessionCallback, null);
        } catch (CameraAccessException e2) {
            e2.printStackTrace();
        }
        this.listener.onStart(this.cameraSize, this.flashSupport);
    }

    private static Size getClosestSupportedSize(List<Size> list, final int i, final int i2) {
        return (Size) Collections.min(list, new Comparator<Size>() { // from class: com.daasuu.gpuv.camerarecorder.CameraThread.3
            private int diff(Size size) {
                return Math.abs(i - size.getWidth()) + Math.abs(i2 - size.getHeight());
            }

            @Override // java.util.Comparator
            public int compare(Size size, Size size2) {
                return diff(size) - diff(size2);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void stopPreview() {
        Log.v(TAG, "stopPreview:");
        this.isFlashTorch = false;
        if (this.requestBuilder != null) {
            this.requestBuilder.set(CaptureRequest.FLASH_MODE, 0);
            try {
                this.cameraCaptureSession.setRepeatingRequest(this.requestBuilder.build(), null, null);
                this.cameraDevice.close();
                Log.v(TAG, "stopPreview: cameraDevice.close()");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void changeManualFocusPoint(float f, float f2, int i, int i2) {
        this.requestBuilder.set(CaptureRequest.CONTROL_AF_REGIONS, new MeteringRectangle[]{new MeteringRectangle(Math.max(((int) ((f2 / i2) * this.sensorArraySize.width())) - 400, 0), Math.max(((int) ((f / i) * this.sensorArraySize.height())) - 400, 0), 800, 800, 999)});
        try {
            this.cameraCaptureSession.setRepeatingRequest(this.requestBuilder.build(), null, null);
        } catch (CameraAccessException e) {
            e.printStackTrace();
        }
        this.requestBuilder.set(CaptureRequest.CONTROL_MODE, 1);
        this.requestBuilder.set(CaptureRequest.CONTROL_AF_MODE, 1);
        this.requestBuilder.set(CaptureRequest.CONTROL_AF_TRIGGER, 1);
        try {
            this.cameraCaptureSession.setRepeatingRequest(this.requestBuilder.build(), null, null);
        } catch (CameraAccessException e2) {
            e2.printStackTrace();
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void switchFlashMode() {
        if (this.flashSupport) {
            try {
                if (this.isFlashTorch) {
                    this.isFlashTorch = false;
                    this.requestBuilder.set(CaptureRequest.FLASH_MODE, 0);
                } else {
                    this.isFlashTorch = true;
                    this.requestBuilder.set(CaptureRequest.FLASH_MODE, 2);
                }
                this.cameraCaptureSession.setRepeatingRequest(this.requestBuilder.build(), null, null);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void changeAutoFocus() {
        this.requestBuilder.set(CaptureRequest.CONTROL_AF_MODE, 3);
        this.requestBuilder.set(CaptureRequest.CONTROL_AF_TRIGGER, 2);
        try {
            this.cameraCaptureSession.setRepeatingRequest(this.requestBuilder.build(), null, null);
        } catch (CameraAccessException e) {
            e.printStackTrace();
        }
    }
}
