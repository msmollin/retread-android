package com.daasuu.gpuv.camerarecorder;

import android.graphics.SurfaceTexture;
import android.hardware.camera2.CameraManager;
import android.opengl.GLSurfaceView;
import android.os.Handler;
import android.util.Log;
import android.util.Size;
import com.daasuu.gpuv.camerarecorder.CameraThread;
import com.daasuu.gpuv.camerarecorder.capture.MediaAudioEncoder;
import com.daasuu.gpuv.camerarecorder.capture.MediaEncoder;
import com.daasuu.gpuv.camerarecorder.capture.MediaMuxerCaptureWrapper;
import com.daasuu.gpuv.camerarecorder.capture.MediaVideoEncoder;
import com.daasuu.gpuv.egl.GlPreviewRenderer;
import com.daasuu.gpuv.egl.filter.GlFilter;

/* loaded from: classes.dex */
public class GPUCameraRecorder {
    private static final String TAG = "GPUCameraRecorder";
    private final int cameraHeight;
    private final CameraManager cameraManager;
    private final CameraRecordListener cameraRecordListener;
    private final int cameraWidth;
    private final int degrees;
    private final int fileHeight;
    private final int fileWidth;
    private final boolean flipHorizontal;
    private final boolean flipVertical;
    private GlPreviewRenderer glPreviewRenderer;
    private GLSurfaceView glSurfaceView;
    private final boolean isLandscapeDevice;
    private final LensFacing lensFacing;
    private final boolean mute;
    private MediaMuxerCaptureWrapper muxer;
    private final boolean recordNoFilter;
    private boolean started = false;
    private CameraHandler cameraHandler = null;
    private boolean flashSupport = false;
    private final MediaEncoder.MediaEncoderListener mediaEncoderListener = new MediaEncoder.MediaEncoderListener() { // from class: com.daasuu.gpuv.camerarecorder.GPUCameraRecorder.3
        private boolean audioStopped;
        private boolean videoStopped;

        @Override // com.daasuu.gpuv.camerarecorder.capture.MediaEncoder.MediaEncoderListener
        public void onPrepared(MediaEncoder mediaEncoder) {
            Log.v("TAG", "onPrepared:encoder=" + mediaEncoder);
            if (mediaEncoder instanceof MediaVideoEncoder) {
                this.videoStopped = false;
                if (GPUCameraRecorder.this.glPreviewRenderer != null) {
                    GPUCameraRecorder.this.glPreviewRenderer.setVideoEncoder((MediaVideoEncoder) mediaEncoder);
                }
            }
            if (mediaEncoder instanceof MediaAudioEncoder) {
                this.audioStopped = false;
            }
        }

        @Override // com.daasuu.gpuv.camerarecorder.capture.MediaEncoder.MediaEncoderListener
        public void onStopped(MediaEncoder mediaEncoder) {
            Log.v("TAG", "onStopped:encoder=" + mediaEncoder);
            if (mediaEncoder instanceof MediaVideoEncoder) {
                this.videoStopped = true;
                if (GPUCameraRecorder.this.glPreviewRenderer != null) {
                    GPUCameraRecorder.this.glPreviewRenderer.setVideoEncoder(null);
                }
            }
            if (mediaEncoder instanceof MediaAudioEncoder) {
                this.audioStopped = true;
            }
        }

        @Override // com.daasuu.gpuv.camerarecorder.capture.MediaEncoder.MediaEncoderListener
        public void onExit() {
            if (this.videoStopped && this.audioStopped) {
                GPUCameraRecorder.this.cameraRecordListener.onVideoFileReady();
            }
        }
    };

    /* JADX INFO: Access modifiers changed from: package-private */
    public GPUCameraRecorder(CameraRecordListener cameraRecordListener, GLSurfaceView gLSurfaceView, int i, int i2, int i3, int i4, LensFacing lensFacing, boolean z, boolean z2, boolean z3, CameraManager cameraManager, boolean z4, int i5, boolean z5) {
        this.cameraRecordListener = cameraRecordListener;
        gLSurfaceView.setDebugFlags(1);
        this.glSurfaceView = gLSurfaceView;
        this.fileWidth = i;
        this.fileHeight = i2;
        this.cameraWidth = i3;
        this.cameraHeight = i4;
        this.lensFacing = lensFacing;
        this.flipHorizontal = z;
        this.flipVertical = z2;
        this.mute = z3;
        this.cameraManager = cameraManager;
        this.isLandscapeDevice = z4;
        this.degrees = i5;
        this.recordNoFilter = z5;
        if (this.glPreviewRenderer == null) {
            this.glPreviewRenderer = new GlPreviewRenderer(gLSurfaceView);
        }
        this.glPreviewRenderer.setSurfaceCreateListener(new GlPreviewRenderer.SurfaceCreateListener() { // from class: com.daasuu.gpuv.camerarecorder.GPUCameraRecorder.1
            @Override // com.daasuu.gpuv.egl.GlPreviewRenderer.SurfaceCreateListener
            public void onCreated(SurfaceTexture surfaceTexture) {
                GPUCameraRecorder.this.startPreview(surfaceTexture);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public synchronized void startPreview(SurfaceTexture surfaceTexture) {
        if (this.cameraHandler == null) {
            CameraThread cameraThread = new CameraThread(this.cameraRecordListener, new CameraThread.OnStartPreviewListener() { // from class: com.daasuu.gpuv.camerarecorder.GPUCameraRecorder.2
                @Override // com.daasuu.gpuv.camerarecorder.CameraThread.OnStartPreviewListener
                public void onStart(Size size, boolean z) {
                    Log.d(GPUCameraRecorder.TAG, "previewSize : width " + size.getWidth() + " height = " + size.getHeight());
                    if (GPUCameraRecorder.this.glPreviewRenderer != null) {
                        GPUCameraRecorder.this.glPreviewRenderer.setCameraResolution(new Size(size.getWidth(), size.getHeight()));
                    }
                    GPUCameraRecorder.this.flashSupport = z;
                    if (GPUCameraRecorder.this.cameraRecordListener != null) {
                        GPUCameraRecorder.this.cameraRecordListener.onGetFlashSupport(GPUCameraRecorder.this.flashSupport);
                    }
                    final float width = size.getWidth();
                    final float height = size.getHeight();
                    GPUCameraRecorder.this.glSurfaceView.post(new Runnable() { // from class: com.daasuu.gpuv.camerarecorder.GPUCameraRecorder.2.1
                        @Override // java.lang.Runnable
                        public void run() {
                            if (GPUCameraRecorder.this.glPreviewRenderer != null) {
                                GPUCameraRecorder.this.glPreviewRenderer.setAngle(GPUCameraRecorder.this.degrees);
                                GPUCameraRecorder.this.glPreviewRenderer.onStartPreview(width, height, GPUCameraRecorder.this.isLandscapeDevice);
                            }
                        }
                    });
                    if (GPUCameraRecorder.this.glPreviewRenderer != null) {
                        GPUCameraRecorder.this.glPreviewRenderer.getPreviewTexture().getSurfaceTexture().setDefaultBufferSize(size.getWidth(), size.getHeight());
                    }
                }
            }, surfaceTexture, this.cameraManager, this.lensFacing);
            cameraThread.start();
            this.cameraHandler = cameraThread.getHandler();
        }
        this.cameraHandler.startPreview(this.cameraWidth, this.cameraHeight);
    }

    public void setFilter(GlFilter glFilter) {
        if (glFilter == null) {
            return;
        }
        this.glPreviewRenderer.setGlFilter(glFilter);
    }

    public void changeManualFocusPoint(float f, float f2, int i, int i2) {
        if (this.cameraHandler != null) {
            this.cameraHandler.changeManualFocusPoint(f, f2, i, i2);
        }
    }

    public void changeAutoFocus() {
        if (this.cameraHandler != null) {
            this.cameraHandler.changeAutoFocus();
        }
    }

    public void switchFlashMode() {
        if (this.flashSupport && this.cameraHandler != null) {
            this.cameraHandler.switchFlashMode();
        }
    }

    public void setGestureScale(float f) {
        if (this.glPreviewRenderer != null) {
            this.glPreviewRenderer.setGestureScale(f);
        }
    }

    public boolean isFlashSupport() {
        return this.flashSupport;
    }

    private void destroyPreview() {
        if (this.glPreviewRenderer != null) {
            this.glPreviewRenderer.release();
            this.glPreviewRenderer = null;
        }
        if (this.cameraHandler != null) {
            this.cameraHandler.stopPreview(false);
        }
    }

    public void start(final String str) {
        if (this.started) {
            return;
        }
        new Handler().post(new Runnable() { // from class: com.daasuu.gpuv.camerarecorder.GPUCameraRecorder.4
            @Override // java.lang.Runnable
            public void run() {
                try {
                    GPUCameraRecorder.this.muxer = new MediaMuxerCaptureWrapper(str);
                    new MediaVideoEncoder(GPUCameraRecorder.this.muxer, GPUCameraRecorder.this.mediaEncoderListener, GPUCameraRecorder.this.fileWidth, GPUCameraRecorder.this.fileHeight, GPUCameraRecorder.this.flipHorizontal, GPUCameraRecorder.this.flipVertical, GPUCameraRecorder.this.glSurfaceView.getMeasuredWidth(), GPUCameraRecorder.this.glSurfaceView.getMeasuredHeight(), GPUCameraRecorder.this.recordNoFilter, GPUCameraRecorder.this.glPreviewRenderer.getFilter());
                    if (!GPUCameraRecorder.this.mute) {
                        new MediaAudioEncoder(GPUCameraRecorder.this.muxer, GPUCameraRecorder.this.mediaEncoderListener);
                    }
                    GPUCameraRecorder.this.muxer.prepare();
                    GPUCameraRecorder.this.muxer.startRecording();
                    if (GPUCameraRecorder.this.cameraRecordListener != null) {
                        GPUCameraRecorder.this.cameraRecordListener.onRecordStart();
                    }
                } catch (Exception e) {
                    GPUCameraRecorder.this.notifyOnError(e);
                }
            }
        });
        this.started = true;
    }

    public void stop() {
        if (this.started) {
            try {
                new Handler().post(new Runnable() { // from class: com.daasuu.gpuv.camerarecorder.GPUCameraRecorder.5
                    @Override // java.lang.Runnable
                    public void run() {
                        try {
                            if (GPUCameraRecorder.this.muxer != null) {
                                GPUCameraRecorder.this.muxer.stopRecording();
                                GPUCameraRecorder.this.muxer = null;
                            }
                        } catch (Exception e) {
                            Log.d("TAG", "RuntimeException: stop() is called immediately after start()");
                            GPUCameraRecorder.this.notifyOnError(e);
                        }
                        GPUCameraRecorder.this.notifyOnDone();
                    }
                });
            } catch (Exception e) {
                notifyOnError(e);
                e.printStackTrace();
            }
            this.started = false;
        }
    }

    public void release() {
        try {
            if (this.muxer != null) {
                this.muxer.stopRecording();
                this.muxer = null;
            }
        } catch (Exception unused) {
            Log.d("TAG", "RuntimeException: stop() is called immediately after start()");
        }
        destroyPreview();
    }

    public boolean isStarted() {
        return this.started;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void notifyOnDone() {
        if (this.cameraRecordListener == null) {
            return;
        }
        this.cameraRecordListener.onRecordComplete();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void notifyOnError(Exception exc) {
        if (this.cameraRecordListener == null) {
            return;
        }
        this.cameraRecordListener.onError(exc);
    }
}
