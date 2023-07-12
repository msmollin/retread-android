package com.opentok.android;

import android.content.Context;
import com.opentok.android.BaseVideoCapturer;
import com.opentok.android.Camera2VideoCapturer;
import com.opentok.android.OpentokError;
import com.opentok.android.OtLog;
import com.opentok.android.PublisherKit;

/* loaded from: classes.dex */
public class Publisher extends PublisherKit {
    protected CameraCaptureFrameRate cameraFrameRate;
    protected CameraListener cameraListener;
    protected CameraCaptureResolution cameraResolution;
    private final OtLog.LogToken log;

    /* loaded from: classes.dex */
    public static class Builder extends PublisherKit.Builder {
        CameraCaptureFrameRate frameRate;
        CameraCaptureResolution resolution;

        public Builder(Context context) {
            super(context);
            this.resolution = null;
            this.frameRate = null;
        }

        @Override // com.opentok.android.PublisherKit.Builder
        public Builder audioBitrate(int i) {
            return (Builder) super.audioBitrate(i);
        }

        @Override // com.opentok.android.PublisherKit.Builder
        public Builder audioTrack(boolean z) {
            this.audioTrack = z;
            return this;
        }

        @Override // com.opentok.android.PublisherKit.Builder
        public Publisher build() {
            return new Publisher(this.context, this.name, this.audioTrack, this.audioBitrate, this.videoTrack, this.capturer, this.resolution, this.frameRate, this.renderer, this.enableOpusDtx);
        }

        @Override // com.opentok.android.PublisherKit.Builder
        public Builder capturer(BaseVideoCapturer baseVideoCapturer) {
            this.capturer = baseVideoCapturer;
            return this;
        }

        public Builder enableOpusDtx(boolean z) {
            this.enableOpusDtx = z;
            return this;
        }

        public Builder frameRate(CameraCaptureFrameRate cameraCaptureFrameRate) {
            this.frameRate = cameraCaptureFrameRate;
            return this;
        }

        @Override // com.opentok.android.PublisherKit.Builder
        public Builder name(String str) {
            this.name = str;
            return this;
        }

        @Override // com.opentok.android.PublisherKit.Builder
        public Builder renderer(BaseVideoRenderer baseVideoRenderer) {
            this.renderer = baseVideoRenderer;
            return this;
        }

        public Builder resolution(CameraCaptureResolution cameraCaptureResolution) {
            this.resolution = cameraCaptureResolution;
            return this;
        }

        @Override // com.opentok.android.PublisherKit.Builder
        public Builder videoTrack(boolean z) {
            this.videoTrack = z;
            return this;
        }
    }

    /* loaded from: classes.dex */
    public enum CameraCaptureFrameRate {
        FPS_30(0),
        FPS_15(1),
        FPS_7(2),
        FPS_1(3);
        
        private int captureFramerate;

        CameraCaptureFrameRate(int i) {
            this.captureFramerate = i;
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        public static CameraCaptureFrameRate defaultFrameRate() {
            return FPS_30;
        }

        static CameraCaptureFrameRate fromFramerate(int i) {
            CameraCaptureFrameRate[] values;
            for (CameraCaptureFrameRate cameraCaptureFrameRate : values()) {
                if (cameraCaptureFrameRate.getCaptureFrameRate() == i) {
                    return cameraCaptureFrameRate;
                }
            }
            throw new IllegalArgumentException("unknown capture framerate " + i);
        }

        int getCaptureFrameRate() {
            return this.captureFramerate;
        }
    }

    /* loaded from: classes.dex */
    public enum CameraCaptureResolution {
        LOW(0),
        MEDIUM(1),
        HIGH(2);
        
        private int captureResolution;

        CameraCaptureResolution(int i) {
            this.captureResolution = i;
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        public static CameraCaptureResolution defaultResolution() {
            return MEDIUM;
        }

        static CameraCaptureResolution fromResolution(int i) {
            CameraCaptureResolution[] values;
            for (CameraCaptureResolution cameraCaptureResolution : values()) {
                if (cameraCaptureResolution.getCaptureResolution() == i) {
                    return cameraCaptureResolution;
                }
            }
            throw new IllegalArgumentException("unknown capture resolution " + i);
        }

        int getCaptureResolution() {
            return this.captureResolution;
        }
    }

    /* loaded from: classes.dex */
    public interface CameraListener {
        void onCameraChanged(Publisher publisher, int i);

        void onCameraError(Publisher publisher, OpentokError opentokError);
    }

    @Deprecated
    public Publisher(Context context) {
        this(context, null, true, 0, true, null, null, null, null, false);
    }

    @Deprecated
    public Publisher(Context context, String str) {
        this(context, str, true, 0, true, null, null, null, null, false);
    }

    @Deprecated
    public Publisher(Context context, String str, BaseVideoCapturer baseVideoCapturer) {
        this(context, str, true, 0, true, baseVideoCapturer, null, null, null, false);
    }

    @Deprecated
    public Publisher(Context context, String str, CameraCaptureResolution cameraCaptureResolution, CameraCaptureFrameRate cameraCaptureFrameRate) {
        this(context, str, true, 0, true, null, cameraCaptureResolution, cameraCaptureFrameRate, null, false);
    }

    /* JADX WARN: Illegal instructions before constructor call */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    protected Publisher(android.content.Context r11, java.lang.String r12, boolean r13, int r14, boolean r15, com.opentok.android.BaseVideoCapturer r16, com.opentok.android.Publisher.CameraCaptureResolution r17, com.opentok.android.Publisher.CameraCaptureFrameRate r18, com.opentok.android.BaseVideoRenderer r19, boolean r20) {
        /*
            r10 = this;
            r9 = r10
            r1 = r11
            if (r16 != 0) goto L1f
            if (r1 == 0) goto L1f
            if (r17 == 0) goto Lb
            r2 = r17
            goto L10
        Lb:
            com.opentok.android.Publisher$CameraCaptureResolution r0 = com.opentok.android.Publisher.CameraCaptureResolution.defaultResolution()
            r2 = r0
        L10:
            if (r18 == 0) goto L15
            r0 = r18
            goto L19
        L15:
            com.opentok.android.Publisher$CameraCaptureFrameRate r0 = com.opentok.android.Publisher.CameraCaptureFrameRate.defaultFrameRate()
        L19:
            com.opentok.android.BaseVideoCapturer r0 = com.opentok.android.VideoCaptureFactory.constructCapturer(r11, r2, r0)
            r6 = r0
            goto L21
        L1f:
            r6 = r16
        L21:
            if (r19 != 0) goto L2b
            if (r1 == 0) goto L2b
            com.opentok.android.BaseVideoRenderer r0 = com.opentok.android.VideoRenderFactory.constructRenderer(r11)
            r7 = r0
            goto L2d
        L2b:
            r7 = r19
        L2d:
            r0 = r10
            r1 = r11
            r2 = r12
            r3 = r13
            r4 = r14
            r5 = r15
            r8 = r20
            r0.<init>(r1, r2, r3, r4, r5, r6, r7, r8)
            com.opentok.android.OtLog$LogToken r0 = new com.opentok.android.OtLog$LogToken
            r0.<init>(r10)
            r9.log = r0
            com.opentok.android.BaseVideoCapturer r0 = r10.getCapturer()
            r0.setPublisherKit(r10)
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.opentok.android.Publisher.<init>(android.content.Context, java.lang.String, boolean, int, boolean, com.opentok.android.BaseVideoCapturer, com.opentok.android.Publisher$CameraCaptureResolution, com.opentok.android.Publisher$CameraCaptureFrameRate, com.opentok.android.BaseVideoRenderer, boolean):void");
    }

    @Deprecated
    public Publisher(Context context, String str, boolean z, boolean z2) {
        this(context, str, z, 0, z2, null, null, null, null, false);
    }

    @Deprecated
    protected Publisher(Context context, String str, boolean z, boolean z2, BaseVideoCapturer baseVideoCapturer, CameraCaptureResolution cameraCaptureResolution, CameraCaptureFrameRate cameraCaptureFrameRate, BaseVideoRenderer baseVideoRenderer) {
        this(context, str, z, 0, z2, baseVideoCapturer, cameraCaptureResolution, cameraCaptureFrameRate, baseVideoRenderer, false);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void c(OpentokError opentokError) {
        CameraListener cameraListener = this.cameraListener;
        if (cameraListener != null) {
            cameraListener.onCameraError(this, opentokError);
        }
    }

    public void cycleCamera() {
        OtLog.LogToken logToken;
        String message;
        Object[] objArr;
        BaseVideoCapturer capturer = getCapturer();
        if (capturer == null) {
            this.log.e("cycleCamera() Capturer is not initialized yet.", new Object[0]);
            onCameraError(new OpentokError(OpentokError.Domain.PublisherErrorDomain, OpentokError.ErrorCode.VideoCaptureFailed.getErrorCode(), "cycleCamera() Capturer is not initialized yet."));
            return;
        }
        try {
            try {
                BaseVideoCapturer.CaptureSwitch captureSwitch = (BaseVideoCapturer.CaptureSwitch) capturer;
                if (captureSwitch == null) {
                    this.log.e("cycleCamera() captureSwitch object is null", new Object[0]);
                    onCameraError(new OpentokError(OpentokError.Domain.PublisherErrorDomain, OpentokError.ErrorCode.VideoCaptureFailed.getErrorCode(), "cycleCamera() captureSwitch object is null"));
                    return;
                }
                captureSwitch.cycleCamera();
                onPublisherCameraPositionChanged(this, captureSwitch.getCameraIndex());
            } catch (Camera2VideoCapturer.Camera2Exception | RuntimeException e) {
                e = e;
                logToken = this.log;
                message = e.getMessage();
                objArr = new Object[0];
                logToken.e(message, objArr);
                onCameraError(e);
            }
        } catch (ClassCastException e2) {
            e = e2;
            logToken = this.log;
            objArr = new Object[0];
            message = "cycleCamera() BaseVideoCapturer.CaptureSwitch interface not implemented";
            logToken.e(message, objArr);
            onCameraError(e);
        }
    }

    @Deprecated
    public int getCameraId() {
        BaseVideoCapturer capturer = getCapturer();
        if (capturer == null) {
            this.log.e("getCameraId() Capturer is not initialized yet.", new Object[0]);
            return -1;
        }
        try {
            BaseVideoCapturer.CaptureSwitch captureSwitch = (BaseVideoCapturer.CaptureSwitch) capturer;
            if (captureSwitch == null) {
                this.log.e("getCameraId() captureSwitch object is null", new Object[0]);
                return -1;
            }
            return captureSwitch.getCameraIndex();
        } catch (ClassCastException unused) {
            this.log.e("getCameraId() BaseVideoCapturer.CaptureSwitch interface not implemented", new Object[0]);
            return -1;
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    /* renamed from: onCameraChanged */
    public void a(int i) {
        CameraListener cameraListener = this.cameraListener;
        if (cameraListener != null) {
            cameraListener.onCameraChanged(this, i);
        }
    }

    protected void onCameraError(final OpentokError opentokError) {
        OtLog.LogToken logToken = this.log;
        logToken.d("onCameraError() Camera device has failed: " + opentokError.errorMessage, new Object[0]);
        if (this.cameraListener != null) {
            this.handler.post(new Runnable() { // from class: com.opentok.android.-$$Lambda$Publisher$X-cgQXL08sPInZYosrCq9XmVC5I
                @Override // java.lang.Runnable
                public final void run() {
                    Publisher.this.c(opentokError);
                }
            });
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void onCameraError(Exception exc) {
        onCameraError(new OpentokError(OpentokError.Domain.PublisherErrorDomain, OpentokError.ErrorCode.CameraFailed.getErrorCode(), exc));
    }

    void onPublisherCameraPositionChanged(Publisher publisher, final int i) {
        this.log.d("onPublisherCameraPositionChanged() Publisher has changed the camera position to: %d", Integer.valueOf(i));
        this.handler.post(new Runnable() { // from class: com.opentok.android.-$$Lambda$Publisher$9Eer0ts4p3U-R3iZIko7YOXmbH4
            @Override // java.lang.Runnable
            public final void run() {
                Publisher.this.a(i);
            }
        });
    }

    @Deprecated
    public void setCameraId(int i) {
        BaseVideoCapturer capturer = getCapturer();
        if (capturer == null) {
            this.log.e("setCameraId() Capturer is not initialized yet.", new Object[0]);
            onCameraError(new OpentokError(OpentokError.Domain.PublisherErrorDomain, OpentokError.ErrorCode.VideoCaptureFailed.getErrorCode(), "setCameraId() Capturer is not initialized yet."));
            return;
        }
        try {
            BaseVideoCapturer.CaptureSwitch captureSwitch = (BaseVideoCapturer.CaptureSwitch) capturer;
            if (captureSwitch == null) {
                this.log.e("setCameraId() captureSwitch object is null", new Object[0]);
                onCameraError(new OpentokError(OpentokError.Domain.PublisherErrorDomain, OpentokError.ErrorCode.VideoCaptureFailed.getErrorCode(), "setCameraId() captureSwitch object is null"));
                return;
            }
            captureSwitch.swapCamera(i);
            onPublisherCameraPositionChanged(this, i);
        } catch (ClassCastException e) {
            this.log.e("setCameraId() BaseVideoCapturer.CaptureSwitch interface not implemented", new Object[0]);
            onCameraError(e);
        }
    }

    public void setCameraListener(CameraListener cameraListener) {
        this.cameraListener = cameraListener;
    }

    @Deprecated
    public void startPreview() {
    }

    @Deprecated
    public void swapCamera() {
        if (getCapturer() != null) {
            cycleCamera();
            return;
        }
        this.log.e("swapCamera() Capturer is not initialized yet.", new Object[0]);
        onCameraError(new OpentokError(OpentokError.Domain.PublisherErrorDomain, OpentokError.ErrorCode.VideoCaptureFailed.getErrorCode(), "swapCamera() Capturer is not initialized yet."));
    }
}
