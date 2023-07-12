package com.bambuser.broadcaster;

import android.util.Log;
import com.bambuser.broadcaster.Capturer;
import com.bambuser.broadcaster.SentryLogger;
import com.bambuser.broadcaster.VideoEncoderBase;
import com.facebook.appevents.internal.ViewHierarchyConstants;
import com.google.android.gms.measurement.AppMeasurement;
import java.nio.ByteBuffer;
import org.json.JSONObject;

/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: classes.dex */
public final class FrameHandler {
    private static final int COMPLEMENT_PREVIEW_DELAY = 3000;
    private static final String LOGTAG = "FrameHandler";
    private static boolean sIH264FailureLogged = false;
    static boolean sMediaCodecFailureLogged = false;
    private int mAutoEncodingType;
    private int mLocalEncodingType;
    private Frame mScaledLiveFrame;
    private Capturer.EncodeInterface mObserver = null;
    private final VideoEncoderBase.DataHandler mLiveDataHandler = new VideoEncoderBase.DataHandler(null) { // from class: com.bambuser.broadcaster.FrameHandler.1
        /* JADX INFO: Access modifiers changed from: package-private */
        @Override // com.bambuser.broadcaster.VideoEncoderBase.DataHandler
        public void onEncodedData(ByteBuffer byteBuffer, long j, int i, boolean z) {
            if (FrameHandler.this.mObserver != null) {
                if (!z) {
                    FrameHandler.this.mObserver.onLiveRotation(j, i);
                }
                FrameHandler.this.mObserver.onSendData(true, j, z ? Movino.getHeaderType(FrameHandler.this.mAutoEncodingType) : FrameHandler.this.mAutoEncodingType, byteBuffer, !z);
            }
        }
    };
    private final VideoEncoderBase.DataHandler mComplementDataHandler = new VideoEncoderBase.DataHandler(null) { // from class: com.bambuser.broadcaster.FrameHandler.2
        /* JADX INFO: Access modifiers changed from: package-private */
        @Override // com.bambuser.broadcaster.VideoEncoderBase.DataHandler
        public void onEncodedData(ByteBuffer byteBuffer, long j, int i, boolean z) {
            if (FrameHandler.this.mObserver != null) {
                if (!z) {
                    FrameHandler.this.mObserver.onComplementRotation(j, i);
                }
                FrameHandler.this.mObserver.onComplementData(true, j, z ? Movino.getHeaderType(FrameHandler.this.mAutoEncodingType) : FrameHandler.this.mAutoEncodingType, byteBuffer, !z);
            }
        }
    };
    private final VideoEncoderBase.DataHandler mLocalDataHandler = new VideoEncoderBase.DataHandler(null) { // from class: com.bambuser.broadcaster.FrameHandler.3
        /* JADX INFO: Access modifiers changed from: package-private */
        @Override // com.bambuser.broadcaster.VideoEncoderBase.DataHandler
        public void onEncodedData(ByteBuffer byteBuffer, long j, int i, boolean z) {
            if (FrameHandler.this.mObserver != null) {
                FrameHandler.this.mObserver.onLocalData(j, z ? Movino.getHeaderType(FrameHandler.this.mLocalEncodingType) : FrameHandler.this.mLocalEncodingType, byteBuffer);
            }
        }
    };
    private JpegEncoder mJpegEncoder = null;
    private VideoEncoderBase mLiveVideoEncoder = null;
    private VideoEncoderBase mComplementVideoEncoder = null;
    private VideoEncoderBase mLocalVideoEncoder = null;
    private ByteBuffer mEncodeBuffer = null;
    private boolean mPreviewTaken = false;
    private int mMediaCodecEncodingFailures = 0;
    private Params mBaseParams = null;
    private Params mAutoParams = null;

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes.dex */
    public static class Params implements Comparable<Params> {
        final int bitrate;
        final int fps;
        final int height;
        final String mimeType;
        final int pixelformat;
        final int width;

        /* JADX INFO: Access modifiers changed from: package-private */
        public Params(String str, int i, int i2, int i3, int i4, int i5) {
            this.mimeType = str;
            this.width = i;
            this.height = i2;
            this.pixelformat = i3;
            this.fps = i4;
            this.bitrate = i5;
        }

        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            if (obj instanceof Params) {
                Params params = (Params) obj;
                return this.mimeType.equals(params.mimeType) && this.width == params.width && this.height == params.height && this.pixelformat == params.pixelformat && this.fps == params.fps && this.bitrate == params.bitrate;
            }
            return false;
        }

        public int hashCode() {
            return (((((((((this.mimeType.hashCode() * 31) + this.width) * 31) + this.height) * 31) + this.pixelformat) * 31) + this.fps) * 31) + this.bitrate;
        }

        @Override // java.lang.Comparable
        public int compareTo(Params params) {
            return this.bitrate - params.bitrate;
        }

        public String toString() {
            return this.mimeType + " " + this.width + "x" + this.height + " @ " + (this.bitrate / 1000) + " kbps";
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void close() {
        this.mObserver = null;
        closeVideoEncoders();
        this.mEncodeBuffer = null;
    }

    private void closeVideoEncoders() {
        if (this.mJpegEncoder != null) {
            this.mJpegEncoder.close();
        }
        this.mJpegEncoder = null;
        closeAutoEncoders();
        if (this.mLocalVideoEncoder != null) {
            this.mLocalVideoEncoder.close();
        }
        this.mLocalVideoEncoder = null;
    }

    private void closeAutoEncoders() {
        if (this.mLiveVideoEncoder != null) {
            this.mLiveVideoEncoder.close();
        }
        this.mLiveVideoEncoder = null;
        if (this.mComplementVideoEncoder != null) {
            this.mComplementVideoEncoder.close();
        }
        this.mComplementVideoEncoder = null;
        this.mScaledLiveFrame = null;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void stopLiveEncoder() {
        if (this.mLiveVideoEncoder != null) {
            this.mLiveVideoEncoder.close();
        }
        this.mLiveVideoEncoder = null;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void resumeLiveEncoder() {
        resetAutoEncoders(this.mAutoParams);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void resetAutoEncoders(Params params) {
        if (this.mLiveVideoEncoder != null && this.mLiveDataHandler != null) {
            this.mLiveVideoEncoder.finish(this.mLiveDataHandler);
        }
        boolean z = (this.mComplementVideoEncoder == null || this.mComplementDataHandler == null) ? false : true;
        if (z) {
            this.mComplementVideoEncoder.finish(this.mComplementDataHandler);
        }
        closeAutoEncoders();
        this.mAutoParams = params;
        this.mAutoEncodingType = 24;
        if (this.mAutoParams.mimeType.equals("video/hevc")) {
            this.mAutoEncodingType = 47;
        } else if (this.mAutoParams.mimeType.equals("video/x-vnd.on2.vp8")) {
            this.mAutoEncodingType = 45;
        }
        Log.i(LOGTAG, "switching to " + params);
        if (DeviceInfoHandler.isMediaCodecSupported() && this.mMediaCodecEncodingFailures < 2) {
            boolean z2 = params.pixelformat == 17 || params.pixelformat == 15731620;
            try {
                MediaCodecWrapper mediaCodecWrapper = new MediaCodecWrapper();
                this.mLiveVideoEncoder = mediaCodecWrapper;
                mediaCodecWrapper.init(params.mimeType, params.width, params.height, params.bitrate, params.fps, z2);
                if (z) {
                    MediaCodecWrapper mediaCodecWrapper2 = new MediaCodecWrapper();
                    this.mComplementVideoEncoder = mediaCodecWrapper2;
                    mediaCodecWrapper2.init(params.mimeType, params.width, params.height, params.bitrate, params.fps, z2);
                }
                Log.i(LOGTAG, "auto jellybean encoder reset");
            } catch (Exception e) {
                if (!sMediaCodecFailureLogged) {
                    sMediaCodecFailureLogged = true;
                    JSONObject jSONObject = new JSONObject();
                    try {
                        jSONObject.put("mimeType", params.mimeType);
                        jSONObject.put("prefersemiplanar", z2);
                        jSONObject.put("pixelformat", params.pixelformat);
                        jSONObject.put(ViewHierarchyConstants.DIMENSION_WIDTH_KEY, params.width);
                        jSONObject.put(ViewHierarchyConstants.DIMENSION_HEIGHT_KEY, params.height);
                        jSONObject.put("bitrate", params.bitrate);
                        jSONObject.put("fps", params.fps);
                    } catch (Exception unused) {
                    }
                    SentryLogger.asyncMessage("Exception when initializing MediaCodec", SentryLogger.Level.WARNING, jSONObject, e);
                }
                if (this.mObserver != null) {
                    Capturer.EncodeInterface encodeInterface = this.mObserver;
                    encodeInterface.onSendLogMessage("FrameHandler Exception when initializing MediaCodec: " + e);
                }
                Log.w(LOGTAG, "Exception when initializing MediaCodec", e);
                closeAutoEncoders();
            }
        }
        if (this.mLiveVideoEncoder == null) {
            resetEncodersToIH264(true, z, false);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes.dex */
    public static class EncoderFailedException extends RuntimeException {
        EncoderFailedException(String str) {
            super(str);
        }
    }

    private void resetEncodersToIH264(boolean z, boolean z2, boolean z3) {
        int cpuCount = NativeUtils.getCpuCount();
        if (cpuCount > 2) {
            cpuCount--;
        }
        if (z) {
            try {
                if (!this.mAutoParams.mimeType.equals("video/avc")) {
                    throw new EncoderFailedException("Encoder failed and no fallback available for " + this.mAutoParams.mimeType);
                }
                if (this.mLiveVideoEncoder != null) {
                    this.mLiveVideoEncoder.close();
                }
                this.mLiveVideoEncoder = new IH264Encoder(this.mAutoParams.width, this.mAutoParams.height, this.mAutoParams.fps, this.mAutoParams.bitrate, cpuCount);
                Log.i(LOGTAG, "internal live encoder reset");
            } catch (Exception e) {
                if (!sIH264FailureLogged) {
                    sIH264FailureLogged = true;
                    JSONObject jSONObject = new JSONObject();
                    try {
                        jSONObject.put("mimeType", this.mAutoParams.mimeType);
                        jSONObject.put("pixelformat", this.mAutoParams.pixelformat);
                        jSONObject.put(ViewHierarchyConstants.DIMENSION_WIDTH_KEY, this.mAutoParams.width);
                        jSONObject.put(ViewHierarchyConstants.DIMENSION_HEIGHT_KEY, this.mAutoParams.height);
                        jSONObject.put("bitrate", this.mAutoParams.bitrate);
                        jSONObject.put("fps", this.mAutoParams.fps);
                    } catch (Exception unused) {
                    }
                    SentryLogger.asyncMessage("Exception when initializing IH264Encoder", SentryLogger.Level.ERROR, jSONObject, e);
                }
                if (this.mObserver != null) {
                    this.mObserver.onSendLogMessage("FrameHandler Exception when resetting to IH264Encoder: " + e);
                }
                Log.w(LOGTAG, "Exception when initializing IH264Encoder", e);
                closeVideoEncoders();
                throw e;
            }
        }
        if (z2) {
            if (!this.mAutoParams.mimeType.equals("video/avc")) {
                throw new EncoderFailedException("Encoder failed and no fallback available for " + this.mAutoParams.mimeType);
            }
            if (this.mComplementVideoEncoder != null) {
                this.mComplementVideoEncoder.close();
            }
            this.mComplementVideoEncoder = new IH264Encoder(this.mAutoParams.width, this.mAutoParams.height, this.mAutoParams.fps, this.mAutoParams.bitrate, cpuCount);
            Log.i(LOGTAG, "internal complement encoder reset");
        }
        if (z3) {
            if (!this.mBaseParams.mimeType.equals("video/avc")) {
                throw new EncoderFailedException("Encoder failed and no fallback available for " + this.mBaseParams.mimeType);
            }
            if (this.mLocalVideoEncoder != null) {
                this.mLocalVideoEncoder.close();
            }
            this.mLocalVideoEncoder = new IH264Encoder(this.mBaseParams.width, this.mBaseParams.height, this.mBaseParams.fps, this.mBaseParams.bitrate, cpuCount);
            Log.i(LOGTAG, "internal local encoder reset");
        }
        Log.i(LOGTAG, "using IH264Encoder");
        this.mScaledLiveFrame = new Frame(17, this.mAutoParams.width, this.mAutoParams.height, true);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void setObserver(Capturer.EncodeInterface encodeInterface) {
        this.mObserver = encodeInterface;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void reset(Params params) {
        closeVideoEncoders();
        this.mBaseParams = params;
        this.mAutoParams = params;
        this.mLocalEncodingType = 24;
        this.mAutoEncodingType = 24;
        if (this.mBaseParams.mimeType.equals("video/hevc")) {
            this.mLocalEncodingType = 47;
            this.mAutoEncodingType = 47;
        } else if (this.mBaseParams.mimeType.equals("video/x-vnd.on2.vp8")) {
            this.mLocalEncodingType = 45;
            this.mAutoEncodingType = 45;
        }
        if (this.mObserver != null) {
            this.mObserver.onSendLogMessage("FrameHandler reset, using pixel format: " + params.pixelformat);
        }
        Log.i(LOGTAG, "preview pixel format is " + params.pixelformat);
        boolean z = false;
        boolean z2 = this.mObserver != null && this.mObserver.onCanWriteLocal();
        Log.i(LOGTAG, "cpu count: " + NativeUtils.getCpuCount());
        if (NativeUtils.hasArm64()) {
            Log.i(LOGTAG, "Arm 64-bit process");
        } else if (NativeUtils.hasArmv7()) {
            Log.i(LOGTAG, "Arm v7, has Neon: " + NativeUtils.hasNeon());
        }
        boolean z3 = this.mObserver != null && this.mObserver.onCanWriteComplement();
        if (DeviceInfoHandler.isMediaCodecSupported() && this.mMediaCodecEncodingFailures < 2) {
            if (params.pixelformat == 17 || params.pixelformat == 15731620) {
                z = true;
            }
            try {
                MediaCodecWrapper mediaCodecWrapper = new MediaCodecWrapper();
                this.mLiveVideoEncoder = mediaCodecWrapper;
                mediaCodecWrapper.init(params.mimeType, params.width, params.height, params.bitrate, params.fps, z);
                if (z3) {
                    MediaCodecWrapper mediaCodecWrapper2 = new MediaCodecWrapper();
                    this.mComplementVideoEncoder = mediaCodecWrapper2;
                    mediaCodecWrapper2.init(params.mimeType, params.width, params.height, params.bitrate, params.fps, z);
                }
                if (z2) {
                    MediaCodecWrapper mediaCodecWrapper3 = new MediaCodecWrapper();
                    this.mLocalVideoEncoder = mediaCodecWrapper3;
                    mediaCodecWrapper3.init(params.mimeType, params.width, params.height, params.bitrate, params.fps, z);
                }
                Log.i(LOGTAG, "jellybean encoders initialized");
            } catch (Exception e) {
                if (!sMediaCodecFailureLogged) {
                    sMediaCodecFailureLogged = true;
                    JSONObject jSONObject = new JSONObject();
                    try {
                        jSONObject.put("mimeType", params.mimeType);
                        jSONObject.put("prefersemiplanar", z);
                        jSONObject.put("pixelformat", params.pixelformat);
                        jSONObject.put(ViewHierarchyConstants.DIMENSION_WIDTH_KEY, params.width);
                        jSONObject.put(ViewHierarchyConstants.DIMENSION_HEIGHT_KEY, params.height);
                        jSONObject.put("bitrate", params.bitrate);
                        jSONObject.put("fps", params.fps);
                    } catch (Exception unused) {
                    }
                    SentryLogger.asyncMessage("Exception when initializing MediaCodec", SentryLogger.Level.WARNING, jSONObject, e);
                }
                if (this.mObserver != null) {
                    this.mObserver.onSendLogMessage("FrameHandler reset Exception: " + e);
                }
                Log.w(LOGTAG, "Exception when initializing MediaCodec", e);
                closeVideoEncoders();
            }
        }
        if (this.mLiveVideoEncoder == null) {
            resetEncodersToIH264(true, z3, z2);
        }
        this.mJpegEncoder = new JpegEncoder();
        this.mJpegEncoder.setQuality(55);
    }

    private static Frame scaleFrame(Frame frame, Frame frame2) {
        if (frame2 == null || (frame.mWidth == frame2.mWidth && frame.mHeight == frame2.mHeight)) {
            return frame;
        }
        ByteBuffer buffer = frame2.getBuffer();
        buffer.clear();
        NativeUtils.copyScaleFrame(frame.getBufferArray(), frame.getDetailArray(), frame.mWidth, frame.mHeight, frame2.getBufferArray(), frame2.getDetailArray(), frame2.mWidth, frame2.mHeight);
        buffer.flip();
        frame2.mTimestamp = frame.mTimestamp;
        frame2.mRotation = frame.mRotation;
        return frame2;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean processFrame(Frame frame) {
        if (this.mObserver == null) {
            return false;
        }
        boolean z = this.mObserver.onCanSendFrame() && this.mLiveVideoEncoder != null;
        boolean z2 = this.mObserver.onCanWriteComplement() && this.mComplementVideoEncoder != null;
        boolean z3 = this.mObserver.onCanWriteLocal() && this.mLocalVideoEncoder != null;
        this.mLiveDataHandler.mTempBuffer = this.mEncodeBuffer;
        this.mComplementDataHandler.mTempBuffer = this.mEncodeBuffer;
        this.mLocalDataHandler.mTempBuffer = this.mEncodeBuffer;
        if (z || z2 || z3) {
            if (frame.getBuffer() != null) {
                frame.getBuffer().clear();
            }
            if (z) {
                while (true) {
                    if (this.mEncodeBuffer != null) {
                        this.mEncodeBuffer.clear();
                    }
                    try {
                        this.mLiveVideoEncoder.encode(scaleFrame(frame, this.mScaledLiveFrame), this.mLiveDataHandler);
                        break;
                    } catch (Exception e) {
                        if (this.mLiveVideoEncoder instanceof MediaCodecWrapper) {
                            this.mMediaCodecEncodingFailures++;
                            Log.w(LOGTAG, "Exception from live MediaCodec in processFrame: " + e);
                            JSONObject jSONObject = new JSONObject();
                            try {
                                jSONObject.put("mimeType", this.mAutoParams.mimeType);
                                jSONObject.put(AppMeasurement.Param.TIMESTAMP, frame.mTimestamp);
                                jSONObject.put("encoder_failures", this.mMediaCodecEncodingFailures);
                            } catch (Exception unused) {
                            }
                            this.mObserver.onSendLogMessage("FrameHandler live MediaCodec encoding failed: " + e);
                            SentryLogger.asyncMessage("Live MediaCodec encoding failed", SentryLogger.Level.WARNING, jSONObject, e);
                            resetEncodersToIH264(this.mLiveVideoEncoder != null, this.mComplementVideoEncoder != null, this.mLocalVideoEncoder != null);
                        } else {
                            this.mObserver.onSendLogMessage("FrameHandler live software encoder failed: " + e);
                            SentryLogger.asyncMessage("Live software encoder failed", SentryLogger.Level.FATAL, null, e);
                            throw e;
                        }
                    }
                }
            } else if (z2) {
                while (true) {
                    if (this.mEncodeBuffer != null) {
                        this.mEncodeBuffer.clear();
                    }
                    try {
                        this.mComplementVideoEncoder.encode(scaleFrame(frame, this.mScaledLiveFrame), this.mComplementDataHandler);
                        break;
                    } catch (Exception e2) {
                        if (this.mComplementVideoEncoder instanceof MediaCodecWrapper) {
                            this.mMediaCodecEncodingFailures++;
                            Log.w(LOGTAG, "Exception from complement MediaCodec in processFrame: " + e2);
                            JSONObject jSONObject2 = new JSONObject();
                            try {
                                jSONObject2.put("mimeType", this.mAutoParams.mimeType);
                                jSONObject2.put(AppMeasurement.Param.TIMESTAMP, frame.mTimestamp);
                                jSONObject2.put("encoder_failures", this.mMediaCodecEncodingFailures);
                            } catch (Exception unused2) {
                            }
                            this.mObserver.onSendLogMessage("FrameHandler complement MediaCodec encoding failed: " + e2);
                            SentryLogger.asyncMessage("Complement MediaCodec encoding failed", SentryLogger.Level.WARNING, jSONObject2, e2);
                            resetEncodersToIH264(this.mLiveVideoEncoder != null, this.mComplementVideoEncoder != null, this.mLocalVideoEncoder != null);
                        } else {
                            this.mObserver.onSendLogMessage("FrameHandler complement software encoder failed: " + e2);
                            SentryLogger.asyncMessage("Complement software encoder failed", SentryLogger.Level.FATAL, null, e2);
                            throw e2;
                        }
                    }
                }
            }
            if (z3) {
                while (true) {
                    if (this.mEncodeBuffer != null) {
                        this.mEncodeBuffer.clear();
                    }
                    try {
                        this.mLocalVideoEncoder.encode(frame, this.mLocalDataHandler);
                        break;
                    } catch (Exception e3) {
                        if (this.mLocalVideoEncoder instanceof MediaCodecWrapper) {
                            this.mMediaCodecEncodingFailures++;
                            Log.w(LOGTAG, "Exception from local MediaCodec in processFrame: " + e3);
                            JSONObject jSONObject3 = new JSONObject();
                            try {
                                jSONObject3.put("mimeType", this.mBaseParams.mimeType);
                                jSONObject3.put(AppMeasurement.Param.TIMESTAMP, frame.mTimestamp);
                                jSONObject3.put("encoder_failures", this.mMediaCodecEncodingFailures);
                            } catch (Exception unused3) {
                            }
                            this.mObserver.onSendLogMessage("FrameHandler local MediaCodec encoding failed: " + e3);
                            SentryLogger.asyncMessage("Local MediaCodec encoding failed", SentryLogger.Level.WARNING, jSONObject3, e3);
                            resetEncodersToIH264(this.mLiveVideoEncoder != null, this.mComplementVideoEncoder != null, this.mLocalVideoEncoder != null);
                        } else {
                            this.mObserver.onSendLogMessage("FrameHandler local software encoder failed: " + e3);
                            SentryLogger.asyncMessage("Local software encoder failed", SentryLogger.Level.FATAL, null, e3);
                            throw e3;
                        }
                    }
                }
            }
            if (!this.mPreviewTaken && frame.mTimestamp >= 3000) {
                createPreview(frame);
            }
        }
        try {
            if (this.mLiveVideoEncoder != null) {
                this.mLiveVideoEncoder.flush(this.mLiveDataHandler);
            }
            if (this.mComplementVideoEncoder != null) {
                this.mComplementVideoEncoder.flush(this.mComplementDataHandler);
            }
            if (this.mLocalVideoEncoder != null) {
                this.mLocalVideoEncoder.flush(this.mLocalDataHandler);
            }
        } catch (Exception unused4) {
        }
        this.mEncodeBuffer = this.mLiveDataHandler.mTempBuffer;
        if (this.mEncodeBuffer == null || (this.mComplementDataHandler.mTempBuffer != null && this.mComplementDataHandler.mTempBuffer.capacity() > this.mEncodeBuffer.capacity())) {
            this.mEncodeBuffer = this.mComplementDataHandler.mTempBuffer;
        }
        if (this.mEncodeBuffer == null || (this.mLocalDataHandler.mTempBuffer != null && this.mLocalDataHandler.mTempBuffer.capacity() > this.mEncodeBuffer.capacity())) {
            this.mEncodeBuffer = this.mLocalDataHandler.mTempBuffer;
        }
        return z;
    }

    private void createPreview(Frame frame) {
        if (this.mJpegEncoder == null || this.mObserver == null) {
            return;
        }
        if (this.mEncodeBuffer != null) {
            this.mEncodeBuffer.clear();
        }
        this.mEncodeBuffer = this.mJpegEncoder.encode(frame.getBufferArray(), frame.getDetailArray(), frame.mWidth, frame.mHeight, this.mEncodeBuffer, false);
        this.mObserver.onPreviewTaken(this.mEncodeBuffer.array(), this.mEncodeBuffer.position(), frame.mRotation);
        this.mPreviewTaken = true;
    }
}
