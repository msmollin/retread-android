package com.opentok.android;

import com.opentok.android.OpentokError;
import com.opentok.android.OtLog;
import com.opentok.impl.a;
import com.opentok.otc.opentokJNI;
import java.nio.ByteBuffer;
import java.util.HashMap;
import java.util.Map;

/* loaded from: classes.dex */
public abstract class BaseVideoCapturer {
    public static final int ABGR = 10;
    public static final int ARGB = 2;
    public static final int BGRA = 7;
    public static final int MJPEG = 9;
    public static final int NV12 = 4;
    public static final int NV21 = 1;
    public static final int RGB = 8;
    public static final int RGBA = 11;
    public static final int UYVY = 6;
    public static final int YUV420P = 3;
    public static final int YUY2 = 5;
    boolean frameMirrorX = false;
    boolean isStopped = false;
    private final OtLog.LogToken log = new OtLog.LogToken(this);
    private PublisherKit publisherKit;

    /* loaded from: classes.dex */
    public static class CaptureSettings {
        public int expectedDelay;
        public int format;
        public int fps;
        public int height;
        public boolean mirrorInLocalRender;
        public int width;
    }

    /* loaded from: classes.dex */
    public interface CaptureSwitch {
        void cycleCamera();

        int getCameraIndex();

        void swapCamera(int i);
    }

    /* loaded from: classes.dex */
    public enum VideoContentHint {
        NONE(0),
        MOTION(1),
        DETAIL(2),
        TEXT(3);
        
        private static Map map = new HashMap();
        private int value;

        static {
            VideoContentHint[] values;
            for (VideoContentHint videoContentHint : values()) {
                map.put(Integer.valueOf(videoContentHint.value), videoContentHint);
            }
        }

        VideoContentHint(int i) {
            this.value = i;
        }

        public static VideoContentHint valueOf(int i) {
            return (VideoContentHint) map.get(Integer.valueOf(i));
        }

        public int getValue() {
            return this.value;
        }
    }

    private int mapFormatToOTCFormat(int i) {
        switch (i) {
            case 1:
                return 3;
            case 2:
                return 6;
            case 3:
                return 1;
            case 4:
                return 2;
            case 5:
                return 4;
            case 6:
                return 5;
            case 7:
                return 7;
            case 8:
                return 8;
            case 9:
                return 10;
            case 10:
                return 9;
            case 11:
                return 11;
            default:
                throw new IllegalArgumentException("Invalid pixel format");
        }
    }

    private native void provideByteBufferFrameNative(long j, ByteBuffer byteBuffer, int i, int i2, int i3, int i4, byte[] bArr);

    private native void provideFramePlanarNative(long j, ByteBuffer byteBuffer, ByteBuffer byteBuffer2, ByteBuffer byteBuffer3, int i, int i2, int i3, int i4, int i5, int i6, int i7, int i8, int i9, byte[] bArr);

    private native void provideIntArrayFrameNative(long j, int[] iArr, int i, int i2, int i3, int i4, byte[] bArr);

    private native void proviveByteFrameNative(long j, byte[] bArr, int i, int i2, int i3, int i4, byte[] bArr2);

    public abstract void destroy();

    /* JADX INFO: Access modifiers changed from: package-private */
    public void error(Exception exc) {
        PublisherKit publisherKit = this.publisherKit;
        if (publisherKit instanceof Publisher) {
            ((Publisher) publisherKit).onCameraError(exc);
        } else {
            onCaptureError(exc);
        }
    }

    public abstract CaptureSettings getCaptureSettings();

    public VideoContentHint getVideoContentHint() {
        return VideoContentHint.valueOf(opentokJNI.otc_video_capturer_get_content_hint(this.publisherKit.otcCapturerHandle));
    }

    public abstract void init();

    public abstract boolean isCaptureStarted();

    void onCaptureError(Exception exc) {
        OtLog.i("Error on video capturer", new Object[0]);
        OpentokError opentokError = exc != null ? new OpentokError(OpentokError.Domain.PublisherErrorDomain, OpentokError.ErrorCode.VideoCaptureFailed.getErrorCode(), exc) : new a(OpentokError.Domain.PublisherErrorDomain, OpentokError.ErrorCode.VideoCaptureFailed.getErrorCode());
        PublisherKit publisherKit = this.publisherKit;
        if (publisherKit != null) {
            publisherKit.throwError(opentokError);
        }
    }

    public abstract void onPause();

    public void onRestart() {
        this.log.d("onRestart", new Object[0]);
        if (!this.isStopped) {
            this.log.e("onRestart called on a capturer not being stopped", new Object[0]);
            return;
        }
        init();
        startCapture();
        this.isStopped = false;
    }

    public abstract void onResume();

    public void onStop() {
        this.log.d("onStop", new Object[0]);
        if (this.isStopped) {
            return;
        }
        stopCapture();
        destroy();
        this.isStopped = true;
    }

    public void provideBufferFrame(ByteBuffer byteBuffer, int i, int i2, int i3, int i4, boolean z) {
        provideBufferFrame(byteBuffer, i, i2, i3, i4, z, null);
    }

    public void provideBufferFrame(ByteBuffer byteBuffer, int i, int i2, int i3, int i4, boolean z, byte[] bArr) {
        PublisherKit publisherKit = this.publisherKit;
        if (publisherKit == null) {
            return;
        }
        long j = publisherKit.otcCapturerHandle;
        if (j == 0) {
            return;
        }
        this.frameMirrorX = z;
        provideByteBufferFrameNative(j, byteBuffer, mapFormatToOTCFormat(i), i2, i3, i4, bArr);
    }

    public void provideBufferFramePlanar(ByteBuffer byteBuffer, ByteBuffer byteBuffer2, ByteBuffer byteBuffer3, int i, int i2, int i3, int i4, int i5, int i6, int i7, int i8, int i9, boolean z) {
        provideBufferFramePlanar(byteBuffer, byteBuffer2, byteBuffer3, i, i2, i3, i4, i5, i6, i7, i8, i9, z, null);
    }

    public void provideBufferFramePlanar(ByteBuffer byteBuffer, ByteBuffer byteBuffer2, ByteBuffer byteBuffer3, int i, int i2, int i3, int i4, int i5, int i6, int i7, int i8, int i9, boolean z, byte[] bArr) {
        PublisherKit publisherKit = this.publisherKit;
        if (publisherKit == null) {
            return;
        }
        long j = publisherKit.otcCapturerHandle;
        if (j == 0) {
            return;
        }
        this.frameMirrorX = z;
        provideFramePlanarNative(j, byteBuffer, byteBuffer2, byteBuffer3, i, i2, i3, i4, i5, i6, i7, i8, i9, bArr);
    }

    public void provideByteArrayFrame(byte[] bArr, int i, int i2, int i3, int i4, boolean z) {
        provideByteArrayFrame(bArr, i, i2, i3, i4, z, null);
    }

    public void provideByteArrayFrame(byte[] bArr, int i, int i2, int i3, int i4, boolean z, byte[] bArr2) {
        PublisherKit publisherKit = this.publisherKit;
        if (publisherKit == null) {
            return;
        }
        long j = publisherKit.otcCapturerHandle;
        if (j == 0) {
            return;
        }
        this.frameMirrorX = z;
        proviveByteFrameNative(j, bArr, mapFormatToOTCFormat(i), i2, i3, i4, bArr2);
    }

    public void provideIntArrayFrame(int[] iArr, int i, int i2, int i3, int i4, boolean z) {
        provideIntArrayFrame(iArr, i, i2, i3, i4, z, null);
    }

    public void provideIntArrayFrame(int[] iArr, int i, int i2, int i3, int i4, boolean z, byte[] bArr) {
        PublisherKit publisherKit = this.publisherKit;
        if (publisherKit == null) {
            return;
        }
        long j = publisherKit.otcCapturerHandle;
        if (j == 0) {
            return;
        }
        this.frameMirrorX = z;
        provideIntArrayFrameNative(j, iArr, mapFormatToOTCFormat(i), i2, i3, i4, bArr);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void setPublisherKit(PublisherKit publisherKit) {
        this.publisherKit = publisherKit;
    }

    public void setVideoContentHint(VideoContentHint videoContentHint) {
        opentokJNI.otc_video_capturer_set_content_hint(this.publisherKit.otcCapturerHandle, videoContentHint.getValue());
    }

    public abstract int startCapture();

    public abstract int stopCapture();
}
