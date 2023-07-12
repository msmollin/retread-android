package com.opentok.android;

import android.media.MediaCodec;
import android.media.MediaCodecInfo;
import android.media.MediaCodecList;
import android.media.MediaCrypto;
import android.media.MediaFormat;
import android.os.Build;
import android.os.SystemClock;
import android.util.Pair;
import android.util.SparseArray;
import android.view.Surface;
import androidx.annotation.Keep;
import com.opentok.android.OtLog;
import java.nio.ByteBuffer;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Set;
import java.util.Vector;
import java.util.concurrent.CountDownLatch;
import org.otwebrtc.SurfaceTextureHelper61;
import org.otwebrtc.ThreadUtils;

/* JADX INFO: Access modifiers changed from: package-private */
@Keep
/* loaded from: classes.dex */
public class MediaCodecVideoDecoder {
    private static final int DEQUEUE_INPUT_TIMEOUT = 500000;
    private static final String FORMAT_KEY_CROP_BOTTOM = "crop-bottom";
    private static final String FORMAT_KEY_CROP_LEFT = "crop-left";
    private static final String FORMAT_KEY_CROP_RIGHT = "crop-right";
    private static final String FORMAT_KEY_CROP_TOP = "crop-top";
    private static final String FORMAT_KEY_SLICE_HEIGHT = "slice-height";
    private static final String FORMAT_KEY_STRIDE = "stride";
    private static final String H264_MIME_TYPE = "video/avc";
    private static final long MAX_DECODE_TIME_MS = 200;
    private static final int MAX_QUEUED_OUTPUTBUFFERS = 3;
    private static final int MEDIA_CODEC_RELEASE_TIMEOUT_MS = 5000;
    private static final String VP8_MIME_TYPE = "video/x-vnd.on2.vp8";
    private static final String VP9_MIME_TYPE = "video/x-vnd.on2.vp9";
    private int colorFormat;
    private int droppedFrames;
    private boolean hasDecodedFirstFrame;
    private int height;
    private ByteBuffer[] inputBuffers;
    private MediaCodec mediaCodec;
    private Thread mediaCodecThread;
    private ByteBuffer[] outputBuffers;
    private int sliceHeight;
    private int stride;
    private TextureListener textureListener;
    private boolean useSurface;
    private int width;
    private static final OtLog.LogToken log = OtLog.LogToken("[MediaCodecDecoder]");
    private static MediaCodecVideoDecoder runningInstance = null;
    private static MediaCodecVideoDecoderErrorCallback errorCallback = null;
    private static int codecErrors = 0;
    private static Set<String> hwDecoderDisabledTypes = new HashSet();
    private static final SupportedDecoderRecord[] VP8_SUPPORT = new SupportedDecoderRecord[0];
    private static final SupportedDecoderRecord[] VP9_SUPPORT = {new SupportedDecoderRecord("OMX.qcom.", 19, SupportedDecoderRecord.Priority.HARDWARE, null), new SupportedDecoderRecord("OMX.Exynos.", 19, SupportedDecoderRecord.Priority.HARDWARE, null)};
    private static final SupportedDecoderRecord[] H264_SUPPORT = {new SupportedDecoderRecord("OMX.qcom.", 19, SupportedDecoderRecord.Priority.HARDWARE, new HashMap<String, Integer>() { // from class: com.opentok.android.MediaCodecVideoDecoder.1
        {
            put("HighProfile", 21);
        }
    }), new SupportedDecoderRecord("OMX.Intel.", 19, SupportedDecoderRecord.Priority.HARDWARE, null), new SupportedDecoderRecord("OMX.Exynos.", 19, SupportedDecoderRecord.Priority.HARDWARE, new HashMap<String, Integer>() { // from class: com.opentok.android.MediaCodecVideoDecoder.2
        {
            put("HighProfile", 23);
        }
    }), new SupportedDecoderRecord("OMX.IMG.", 19, SupportedDecoderRecord.Priority.HARDWARE, null), new SupportedDecoderRecord("OMX.MTK.", 19, SupportedDecoderRecord.Priority.HARDWARE, null), new SupportedDecoderRecord("OMX.google.h264.", 23, SupportedDecoderRecord.Priority.SOFTWARE, null)};
    private static final SparseArray<Pair<String, SupportedDecoderRecord[]>> SupportedDecoderTbl = new SparseArray<Pair<String, SupportedDecoderRecord[]>>() { // from class: com.opentok.android.MediaCodecVideoDecoder.3
        {
            append(VideoCodecType.VIDEO_CODEC_VP8.ordinal(), new Pair(MediaCodecVideoDecoder.VP8_MIME_TYPE, MediaCodecVideoDecoder.VP8_SUPPORT));
            append(VideoCodecType.VIDEO_CODEC_VP9.ordinal(), new Pair(MediaCodecVideoDecoder.VP9_MIME_TYPE, MediaCodecVideoDecoder.VP9_SUPPORT));
            append(VideoCodecType.VIDEO_CODEC_H264.ordinal(), new Pair(MediaCodecVideoDecoder.H264_MIME_TYPE, MediaCodecVideoDecoder.H264_SUPPORT));
        }
    };
    private static final int COLOR_QCOM_FORMATYVU420PackedSemiPlanar32m4ka = 2141391873;
    private static final int COLOR_QCOM_FORMATYVU420PackedSemiPlanar16m4ka = 2141391874;
    private static final int COLOR_QCOM_FORMATYVU420PackedSemiPlanar64x32Tile2m8ka = 2141391875;
    private static final int COLOR_QCOM_FORMATYUV420PackedSemiPlanar32m = 2141391876;
    private static final List<Integer> supportedColorList = Arrays.asList(19, 21, 2141391872, Integer.valueOf((int) COLOR_QCOM_FORMATYVU420PackedSemiPlanar32m4ka), Integer.valueOf((int) COLOR_QCOM_FORMATYVU420PackedSemiPlanar16m4ka), Integer.valueOf((int) COLOR_QCOM_FORMATYVU420PackedSemiPlanar64x32Tile2m8ka), Integer.valueOf((int) COLOR_QCOM_FORMATYUV420PackedSemiPlanar32m));
    private final Queue<TimeStamps> decodeStartTimeMs = new LinkedList();
    private Surface surface = null;
    private final Queue<DecodedOutputBuffer> dequeuedSurfaceOutputBuffers = new LinkedList();

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes.dex */
    public static class DecodedOutputBuffer {
        private final long decodeTimeMs;
        private final long endDecodeTimeMs;
        private final int index;
        private final long ntpTimeStampMs;
        private final int offset;
        private final long presentationTimeStampMs;
        private final int size;
        private final long timeStampMs;

        public DecodedOutputBuffer(int i, int i2, int i3, long j, long j2, long j3, long j4, long j5) {
            this.index = i;
            this.offset = i2;
            this.size = i3;
            this.presentationTimeStampMs = j;
            this.timeStampMs = j2;
            this.ntpTimeStampMs = j3;
            this.decodeTimeMs = j4;
            this.endDecodeTimeMs = j5;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes.dex */
    public static class DecodedTextureBuffer {
        private final long decodeTimeMs;
        private final long frameDelayMs;
        private final long ntpTimeStampMs;
        private final long presentationTimeStampMs;
        private final int textureID;
        private final long timeStampMs;
        private final float[] transformMatrix;

        public DecodedTextureBuffer(int i, float[] fArr, long j, long j2, long j3, long j4, long j5) {
            this.textureID = i;
            this.transformMatrix = fArr;
            this.presentationTimeStampMs = j;
            this.timeStampMs = j2;
            this.ntpTimeStampMs = j3;
            this.decodeTimeMs = j4;
            this.frameDelayMs = j5;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes.dex */
    public static class DecoderProperties {
        public final String codecName;
        public final int colorFormat;
        public final Map<String, Integer> meta;

        public DecoderProperties(String str, int i, Map<String, Integer> map) {
            this.codecName = str;
            this.colorFormat = i;
            this.meta = map;
        }
    }

    /* loaded from: classes.dex */
    public interface MediaCodecVideoDecoderErrorCallback {
        void onMediaCodecVideoDecoderCriticalError(int i);
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes.dex */
    public static class SupportedDecoderRecord {
        public final Map<String, Integer> meta;
        public final String prefix;
        public final Priority priority;
        public final int supportedVersion;

        /* loaded from: classes.dex */
        public enum Priority {
            HARDWARE(100),
            SOFTWARE(10);
            
            private final int priority;

            Priority(int i) {
                this.priority = i;
            }
        }

        public SupportedDecoderRecord(String str, int i, Priority priority, Map<String, Integer> map) {
            this.prefix = str;
            this.supportedVersion = i;
            this.priority = priority;
            this.meta = map == null ? new HashMap<>() : map;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes.dex */
    public static class TextureListener implements SurfaceTextureHelper61.OnTextureFrameAvailableListener {
        private DecodedOutputBuffer bufferToRender;
        private final Object newFrameLock = new Object();
        private DecodedTextureBuffer renderedBuffer;
        private final SurfaceTextureHelper61 surfaceTextureHelper;

        public TextureListener(SurfaceTextureHelper61 surfaceTextureHelper61) {
            this.surfaceTextureHelper = surfaceTextureHelper61;
            surfaceTextureHelper61.startListening(this);
        }

        public void addBufferToRender(DecodedOutputBuffer decodedOutputBuffer) {
            if (this.bufferToRender == null) {
                this.bufferToRender = decodedOutputBuffer;
            } else {
                MediaCodecVideoDecoder.log.e("Unexpected addBufferToRender() called while waiting for a texture.", new Object[0]);
                throw new IllegalStateException("Waiting for a texture.");
            }
        }

        public DecodedTextureBuffer dequeueTextureBuffer(int i) {
            DecodedTextureBuffer decodedTextureBuffer;
            synchronized (this.newFrameLock) {
                if (this.renderedBuffer == null && i > 0 && isWaitingForTexture()) {
                    try {
                        this.newFrameLock.wait(i);
                    } catch (InterruptedException unused) {
                        Thread.currentThread().interrupt();
                    }
                }
                decodedTextureBuffer = this.renderedBuffer;
                this.renderedBuffer = null;
            }
            return decodedTextureBuffer;
        }

        public boolean isWaitingForTexture() {
            boolean z;
            synchronized (this.newFrameLock) {
                z = this.bufferToRender != null;
            }
            return z;
        }

        @Override // org.otwebrtc.SurfaceTextureHelper61.OnTextureFrameAvailableListener
        public void onTextureFrameAvailable(int i, float[] fArr, long j) {
            synchronized (this.newFrameLock) {
                if (this.renderedBuffer != null) {
                    MediaCodecVideoDecoder.log.e("Unexpected onTextureFrameAvailable() called while already holding a texture.", new Object[0]);
                    throw new IllegalStateException("Already holding a texture.");
                }
                this.renderedBuffer = new DecodedTextureBuffer(i, fArr, this.bufferToRender.presentationTimeStampMs, this.bufferToRender.timeStampMs, this.bufferToRender.ntpTimeStampMs, this.bufferToRender.decodeTimeMs, SystemClock.elapsedRealtime() - this.bufferToRender.endDecodeTimeMs);
                this.bufferToRender = null;
                this.newFrameLock.notifyAll();
            }
        }

        public void release() {
            this.surfaceTextureHelper.stopListening();
            synchronized (this.newFrameLock) {
                if (this.renderedBuffer != null) {
                    this.surfaceTextureHelper.returnTextureFrame();
                    this.renderedBuffer = null;
                }
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes.dex */
    public static class TimeStamps {
        private final long decodeStartTimeMs;
        private final long ntpTimeStampMs;
        private final long timeStampMs;

        public TimeStamps(long j, long j2, long j3) {
            this.decodeStartTimeMs = j;
            this.timeStampMs = j2;
            this.ntpTimeStampMs = j3;
        }
    }

    /* loaded from: classes.dex */
    public enum VideoCodecType {
        VIDEO_CODEC_GENERIC,
        VIDEO_CODEC_VP8,
        VIDEO_CODEC_VP9,
        VIDEO_CODEC_AV1,
        VIDEO_CODEC_H264
    }

    MediaCodecVideoDecoder() {
    }

    private void MaybeRenderDecodedTextureBuffer() {
        if (this.dequeuedSurfaceOutputBuffers.isEmpty() || this.textureListener.isWaitingForTexture()) {
            return;
        }
        DecodedOutputBuffer remove = this.dequeuedSurfaceOutputBuffers.remove();
        this.textureListener.addBufferToRender(remove);
        this.mediaCodec.releaseOutputBuffer(remove.index, true);
    }

    private void checkOnMediaCodecThread() {
        if (this.mediaCodecThread.getId() == Thread.currentThread().getId()) {
            return;
        }
        throw new IllegalStateException("MediaCodecVideoDecoder previously operated on " + this.mediaCodecThread + " but is now called on " + Thread.currentThread());
    }

    private static List<Integer> createIntList(int[] iArr) {
        Vector vector = new Vector(iArr.length);
        for (int i : iArr) {
            vector.add(Integer.valueOf(i));
        }
        return vector;
    }

    private int dequeueInputBuffer() {
        checkOnMediaCodecThread();
        try {
            return this.mediaCodec.dequeueInputBuffer(500000L);
        } catch (IllegalStateException e) {
            log.e("dequeueIntputBuffer failed", e);
            return -2;
        }
    }

    /* JADX WARN: Code restructure failed: missing block: B:30:0x011c, code lost:
        throw new java.lang.RuntimeException("Unexpected size change. Configured " + r22.width + "*" + r22.height + ". New " + r7 + "*" + r8);
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    private com.opentok.android.MediaCodecVideoDecoder.DecodedOutputBuffer dequeueOutputBuffer(int r23) {
        /*
            Method dump skipped, instructions count: 566
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: com.opentok.android.MediaCodecVideoDecoder.dequeueOutputBuffer(int):com.opentok.android.MediaCodecVideoDecoder$DecodedOutputBuffer");
    }

    private DecodedTextureBuffer dequeueTextureBuffer(int i) {
        OtLog.LogToken logToken;
        StringBuilder sb;
        String str;
        checkOnMediaCodecThread();
        if (this.useSurface) {
            DecodedOutputBuffer dequeueOutputBuffer = dequeueOutputBuffer(i);
            if (dequeueOutputBuffer != null) {
                this.dequeuedSurfaceOutputBuffers.add(dequeueOutputBuffer);
            }
            MaybeRenderDecodedTextureBuffer();
            DecodedTextureBuffer dequeueTextureBuffer = this.textureListener.dequeueTextureBuffer(i);
            if (dequeueTextureBuffer != null) {
                MaybeRenderDecodedTextureBuffer();
                return dequeueTextureBuffer;
            } else if (this.dequeuedSurfaceOutputBuffers.size() >= Math.min(3, this.outputBuffers.length) || (i > 0 && !this.dequeuedSurfaceOutputBuffers.isEmpty())) {
                this.droppedFrames++;
                DecodedOutputBuffer remove = this.dequeuedSurfaceOutputBuffers.remove();
                if (i > 0) {
                    logToken = log;
                    sb = new StringBuilder();
                    str = "Draining decoder. Dropping frame with TS: ";
                } else {
                    logToken = log;
                    sb = new StringBuilder();
                    sb.append("Too many output buffers ");
                    sb.append(this.dequeuedSurfaceOutputBuffers.size());
                    str = ". Dropping frame with TS: ";
                }
                sb.append(str);
                sb.append(remove.presentationTimeStampMs);
                sb.append(". Total number of dropped frames: ");
                sb.append(this.droppedFrames);
                logToken.w(sb.toString(), new Object[0]);
                this.mediaCodec.releaseOutputBuffer(remove.index, false);
                return new DecodedTextureBuffer(0, null, remove.presentationTimeStampMs, remove.timeStampMs, remove.ntpTimeStampMs, remove.decodeTimeMs, SystemClock.elapsedRealtime() - remove.endDecodeTimeMs);
            } else {
                return null;
            }
        }
        throw new IllegalStateException("dequeueTexture() called for byte buffer decoding.");
    }

    public static void disableH264HwCodec() {
        log.w("H.264 decoding is disabled by application.", new Object[0]);
        hwDecoderDisabledTypes.add(H264_MIME_TYPE);
    }

    public static void disableVp8HwCodec() {
        log.w("VP8 decoding is disabled by application.", new Object[0]);
        hwDecoderDisabledTypes.add(VP8_MIME_TYPE);
    }

    public static void disableVp9HwCodec() {
        log.w("VP9 decoding is disabled by application.", new Object[0]);
        hwDecoderDisabledTypes.add(VP9_MIME_TYPE);
    }

    /* JADX WARN: Code restructure failed: missing block: B:21:0x009d, code lost:
        com.opentok.android.MediaCodecVideoDecoder.log.d("Found target decoder " + r0.getName() + ". Color: 0x" + java.lang.Integer.toHexString(r14), new java.lang.Object[r4]);
     */
    /* JADX WARN: Code restructure failed: missing block: B:22:0x00cd, code lost:
        r3.add(new android.util.Pair(new com.opentok.android.MediaCodecVideoDecoder.DecoderProperties(r0.getName(), r14, r12.meta), r12.priority));
     */
    /* JADX WARN: Code restructure failed: missing block: B:24:0x00db, code lost:
        r0 = e;
     */
    /* JADX WARN: Code restructure failed: missing block: B:25:0x00dc, code lost:
        r8 = 1;
        r9 = 0;
     */
    /* JADX WARN: Code restructure failed: missing block: B:31:0x00f8, code lost:
        r4 = com.opentok.android.MediaCodecVideoDecoder.log;
        r8 = new java.lang.Object[r8];
        r8[r9] = r0;
        r4.e("Cannot retreive decoder capabilities", r8);
     */
    /* JADX WARN: Code restructure failed: missing block: B:49:0x00df, code lost:
        continue;
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    private static com.opentok.android.MediaCodecVideoDecoder.DecoderProperties[] findDecoder(java.lang.String r16, com.opentok.android.MediaCodecVideoDecoder.SupportedDecoderRecord[] r17) {
        /*
            Method dump skipped, instructions count: 306
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: com.opentok.android.MediaCodecVideoDecoder.findDecoder(java.lang.String, com.opentok.android.MediaCodecVideoDecoder$SupportedDecoderRecord[]):com.opentok.android.MediaCodecVideoDecoder$DecoderProperties[]");
    }

    private static MediaCodecInfo[] getCodecList() {
        if (Build.VERSION.SDK_INT < 19) {
            return new MediaCodecInfo[0];
        }
        Vector vector = new Vector();
        for (int i = 0; i < MediaCodecList.getCodecCount(); i++) {
            MediaCodecInfo codecInfoAt = MediaCodecList.getCodecInfoAt(i);
            if (codecInfoAt != null && !codecInfoAt.isEncoder()) {
                vector.add(codecInfoAt);
            }
        }
        return (MediaCodecInfo[]) vector.toArray(new MediaCodecInfo[vector.size()]);
    }

    private boolean initDecode(VideoCodecType videoCodecType, int i, int i2, SurfaceTextureHelper61 surfaceTextureHelper61) {
        if (this.mediaCodecThread == null) {
            this.useSurface = surfaceTextureHelper61 != null;
            Pair<String, SupportedDecoderRecord[]> pair = SupportedDecoderTbl.get(videoCodecType.ordinal());
            DecoderProperties[] findDecoder = findDecoder((String) pair.first, (SupportedDecoderRecord[]) pair.second);
            if (findDecoder == null || findDecoder.length == 0) {
                throw new RuntimeException("Cannot find HW decoder for " + videoCodecType);
            }
            OtLog.LogToken logToken = log;
            logToken.d("Java initDecode: " + videoCodecType + " : " + i + " x " + i2 + ". Color: 0x" + Integer.toHexString(findDecoder[0].colorFormat) + ". Use Surface: " + this.useSurface, new Object[0]);
            runningInstance = this;
            this.mediaCodecThread = Thread.currentThread();
            this.width = i;
            this.height = i2;
            this.stride = i;
            this.sliceHeight = i2;
            MediaFormat createVideoFormat = MediaFormat.createVideoFormat((String) pair.first, i, i2);
            int length = findDecoder.length;
            boolean z = false;
            for (int i3 = 0; i3 < length; i3++) {
                DecoderProperties decoderProperties = findDecoder[i3];
                try {
                    if (this.useSurface) {
                        this.textureListener = new TextureListener(surfaceTextureHelper61);
                        this.surface = new Surface(surfaceTextureHelper61.getSurfaceTexture());
                    } else {
                        createVideoFormat.setInteger("color-format", decoderProperties.colorFormat);
                    }
                    OtLog.LogToken logToken2 = log;
                    logToken2.d("  Format: " + createVideoFormat, new Object[0]);
                    boolean initDecoder = initDecoder(decoderProperties, createVideoFormat);
                    if (true == initDecoder) {
                        try {
                            OtLog.LogToken logToken3 = log;
                            logToken3.d("using decoder: " + decoderProperties.codecName, new Object[0]);
                            return initDecoder;
                        } catch (Exception unused) {
                        }
                    }
                    z = initDecoder;
                } catch (Exception unused2) {
                }
            }
            return z;
        }
        throw new RuntimeException("initDecode: Forgot to release()?");
    }

    private boolean initDecoder(DecoderProperties decoderProperties, MediaFormat mediaFormat) {
        try {
            MediaCodec createByCodecName = MediaCodecVideoEncoder.createByCodecName(decoderProperties.codecName);
            this.mediaCodec = createByCodecName;
            if (createByCodecName == null) {
                log.e("Can not create media decoder", new Object[0]);
                return false;
            }
            createByCodecName.configure(mediaFormat, this.surface, (MediaCrypto) null, 0);
            this.mediaCodec.start();
            this.colorFormat = decoderProperties.colorFormat;
            this.outputBuffers = this.mediaCodec.getOutputBuffers();
            this.inputBuffers = this.mediaCodec.getInputBuffers();
            this.decodeStartTimeMs.clear();
            this.hasDecodedFirstFrame = false;
            this.dequeuedSurfaceOutputBuffers.clear();
            this.droppedFrames = 0;
            OtLog.LogToken logToken = log;
            logToken.d("Input buffers: " + this.inputBuffers.length + ". Output buffers: " + this.outputBuffers.length, new Object[0]);
            return true;
        } catch (IllegalStateException e) {
            log.e("initDecode failed", e);
            return false;
        }
    }

    public static boolean isH264HighProfileHwSupported() {
        DecoderProperties[] findDecoder = findDecoder(H264_MIME_TYPE, H264_SUPPORT);
        return findDecoder.length > 0 && findDecoder[0].meta.containsKey("HighProfile") && Build.VERSION.SDK_INT >= findDecoder[0].meta.get("HighProfile").intValue();
    }

    public static boolean isH264HwSupported() {
        return !hwDecoderDisabledTypes.contains(H264_MIME_TYPE) && findDecoder(H264_MIME_TYPE, H264_SUPPORT).length > 0;
    }

    public static boolean isVp8HwSupported() {
        return !hwDecoderDisabledTypes.contains(VP8_MIME_TYPE) && findDecoder(VP8_MIME_TYPE, VP8_SUPPORT).length > 0;
    }

    public static boolean isVp9HwSupported() {
        return !hwDecoderDisabledTypes.contains(VP9_MIME_TYPE) && findDecoder(VP9_MIME_TYPE, VP9_SUPPORT).length > 0;
    }

    public static void printStackTrace() {
        Thread thread;
        MediaCodecVideoDecoder mediaCodecVideoDecoder = runningInstance;
        if (mediaCodecVideoDecoder == null || (thread = mediaCodecVideoDecoder.mediaCodecThread) == null) {
            return;
        }
        StackTraceElement[] stackTrace = thread.getStackTrace();
        if (stackTrace.length > 0) {
            log.d("MediaCodecVideoDecoder stacks trace:", new Object[0]);
            for (StackTraceElement stackTraceElement : stackTrace) {
                log.d(stackTraceElement.toString(), new Object[0]);
            }
        }
    }

    private boolean queueInputBuffer(int i, int i2, long j, long j2, long j3) {
        checkOnMediaCodecThread();
        try {
            this.inputBuffers[i].position(0);
            this.inputBuffers[i].limit(i2);
            this.decodeStartTimeMs.add(new TimeStamps(SystemClock.elapsedRealtime(), j2, j3));
            this.mediaCodec.queueInputBuffer(i, 0, i2, j, 0);
            return true;
        } catch (IllegalStateException e) {
            log.e("decode failed", e);
            return false;
        }
    }

    private void release() {
        log.d("Java releaseDecoder. Total number of dropped frames: " + this.droppedFrames, new Object[0]);
        checkOnMediaCodecThread();
        final CountDownLatch countDownLatch = new CountDownLatch(1);
        new Thread(new Runnable() { // from class: com.opentok.android.MediaCodecVideoDecoder.5
            @Override // java.lang.Runnable
            public void run() {
                try {
                    MediaCodecVideoDecoder.log.d("Java releaseDecoder on release thread", new Object[0]);
                    MediaCodecVideoDecoder.this.mediaCodec.stop();
                    MediaCodecVideoDecoder.this.mediaCodec.release();
                    MediaCodecVideoDecoder.log.d("Java releaseDecoder on release thread done", new Object[0]);
                } catch (Exception e) {
                    MediaCodecVideoDecoder.log.e("Media decoder release failed", e);
                }
                countDownLatch.countDown();
            }
        }).start();
        if (!ThreadUtils.awaitUninterruptibly(countDownLatch, 5000L)) {
            log.e("Media decoder release timeout", new Object[0]);
            codecErrors++;
            if (errorCallback != null) {
                log.e("Invoke codec error callback. Errors: " + codecErrors, new Object[0]);
                errorCallback.onMediaCodecVideoDecoderCriticalError(codecErrors);
            }
        }
        this.mediaCodec = null;
        this.mediaCodecThread = null;
        runningInstance = null;
        if (this.useSurface) {
            this.surface.release();
            this.surface = null;
            this.textureListener.release();
        }
        log.d("Java releaseDecoder done", new Object[0]);
    }

    private void reset(int i, int i2) {
        if (this.mediaCodecThread == null || this.mediaCodec == null) {
            throw new RuntimeException("Incorrect reset call for non-initialized decoder.");
        }
        OtLog.LogToken logToken = log;
        logToken.d("Java reset: " + i + " x " + i2, new Object[0]);
        this.mediaCodec.flush();
        this.width = i;
        this.height = i2;
        this.decodeStartTimeMs.clear();
        this.dequeuedSurfaceOutputBuffers.clear();
        this.hasDecodedFirstFrame = false;
        this.droppedFrames = 0;
    }

    private void returnDecodedOutputBuffer(int i) {
        checkOnMediaCodecThread();
        if (this.useSurface) {
            throw new IllegalStateException("returnDecodedOutputBuffer() called for surface decoding.");
        }
        this.mediaCodec.releaseOutputBuffer(i, false);
    }

    public static void setErrorCallback(MediaCodecVideoDecoderErrorCallback mediaCodecVideoDecoderErrorCallback) {
        log.d("Set error callback", new Object[0]);
        errorCallback = mediaCodecVideoDecoderErrorCallback;
    }
}
