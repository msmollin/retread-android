package com.opentok.android;

import android.annotation.TargetApi;
import android.media.MediaCodec;
import android.media.MediaCodecInfo;
import android.media.MediaCodecList;
import android.media.MediaCrypto;
import android.media.MediaFormat;
import android.opengl.GLES20;
import android.os.Build;
import android.os.Bundle;
import android.util.Pair;
import android.util.SparseArray;
import android.util.SparseIntArray;
import android.view.Surface;
import androidx.annotation.Keep;
import com.google.android.gms.common.Scopes;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.opentok.android.OtLog;
import java.nio.ByteBuffer;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Vector;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import org.otwebrtc.EglBase14;
import org.otwebrtc.GlRectDrawer;
import org.otwebrtc.ThreadUtils;

@Keep
@TargetApi(19)
/* loaded from: classes.dex */
class MediaCodecVideoEncoder {
    private static final int BITRATE_ADJUSTMENT_FPS = 30;
    private static final double BITRATE_CORRECTION_MAX_SCALE = 4.0d;
    private static final double BITRATE_CORRECTION_SEC = 3.0d;
    private static final int BITRATE_CORRECTION_STEPS = 20;
    private static final int DEQUEUE_TIMEOUT = 0;
    private static final String H264_MIME_TYPE = "video/avc";
    private static final int MAXIMUM_INITIAL_FPS = 30;
    private static final int MEDIA_CODEC_RELEASE_TIMEOUT_MS = 5000;
    private static final long QCOM_VP8_KEY_FRAME_INTERVAL_ANDROID_L_MS = 15000;
    private static final long QCOM_VP8_KEY_FRAME_INTERVAL_ANDROID_M_MS = 20000;
    private static final long QCOM_VP8_KEY_FRAME_INTERVAL_ANDROID_N_MS = 15000;
    private static final int VIDEO_AVCLevel3 = 256;
    private static final int VIDEO_AVCProfileHigh = 8;
    private static final int VIDEO_ControlRateConstant = 2;
    private static final String VP8_MIME_TYPE = "video/x-vnd.on2.vp8";
    private static final String VP9_MIME_TYPE = "video/x-vnd.on2.vp9";
    private double bitrateAccumulator;
    private double bitrateAccumulatorMax;
    private int bitrateAdjustmentScaleExp;
    private double bitrateObservationTimeMs;
    private int colorFormat;
    private GlRectDrawer drawer;
    private EglBase14 eglBase;
    private long forcedKeyFrameMs;
    private int height;
    private Surface inputSurface;
    private long lastKeyFrameMs;
    private MediaCodec mediaCodec;
    private Thread mediaCodecThread;
    private ByteBuffer[] outputBuffers;
    private int profile;
    private int targetBitrateBps;
    private int targetFps;
    private VideoCodecType type;
    private int width;
    private static final OtLog.LogToken log = OtLog.LogToken("[MediaCodecEncoder]");
    private static MediaCodecVideoEncoder runningInstance = null;
    private static MediaCodecVideoEncoderErrorCallback errorCallback = null;
    private static int codecErrors = 0;
    private static Set<String> hwEncoderDisabledTypes = new HashSet();
    private static final SupportedEncoderRecord[] VP8_SUPPORT = new SupportedEncoderRecord[0];
    private static final SupportedEncoderRecord[] VP9_SUPPORT = {new SupportedEncoderRecord("OMX.qcom.", 24, SupportedEncoderRecord.Priority.HARDWARE, BitrateAdjustmentType.NO_ADJUSTMENT, null), new SupportedEncoderRecord("OMX.Exynos.", 24, SupportedEncoderRecord.Priority.HARDWARE, BitrateAdjustmentType.FRAMERATE_ADJUSTMENT, null)};
    private static final SupportedEncoderRecord[] H264_SUPPORT = {new SupportedEncoderRecord("OMX.qcom.", 19, SupportedEncoderRecord.Priority.HARDWARE, BitrateAdjustmentType.NO_ADJUSTMENT, null), new SupportedEncoderRecord("OMX.Exynos.", 21, SupportedEncoderRecord.Priority.HARDWARE, BitrateAdjustmentType.FRAMERATE_ADJUSTMENT, new HashMap<String, Integer>() { // from class: com.opentok.android.MediaCodecVideoEncoder.1
        {
            put("HighProfile", 23);
        }
    }), new SupportedEncoderRecord("OMX.IMG.", 19, SupportedEncoderRecord.Priority.HARDWARE, BitrateAdjustmentType.NO_ADJUSTMENT, null), new SupportedEncoderRecord("OMX.MTK.", 19, SupportedEncoderRecord.Priority.HARDWARE, BitrateAdjustmentType.NO_ADJUSTMENT, null), new SupportedEncoderRecord("OMX.google.h264.", 23, SupportedEncoderRecord.Priority.SOFTWARE, BitrateAdjustmentType.NO_ADJUSTMENT, null)};
    private static final SparseArray<Pair<String, SupportedEncoderRecord[]>> SupportedEncoderTbl = new SparseArray<Pair<String, SupportedEncoderRecord[]>>() { // from class: com.opentok.android.MediaCodecVideoEncoder.2
        {
            append(VideoCodecType.VIDEO_CODEC_VP8.ordinal(), new Pair(MediaCodecVideoEncoder.VP8_MIME_TYPE, MediaCodecVideoEncoder.VP8_SUPPORT));
            append(VideoCodecType.VIDEO_CODEC_VP9.ordinal(), new Pair(MediaCodecVideoEncoder.VP9_MIME_TYPE, MediaCodecVideoEncoder.VP9_SUPPORT));
            append(VideoCodecType.VIDEO_CODEC_H264.ordinal(), new Pair(MediaCodecVideoEncoder.H264_MIME_TYPE, MediaCodecVideoEncoder.H264_SUPPORT));
        }
    };
    private static final SparseIntArray KeyFrameIntervalSecTbl = new SparseIntArray() { // from class: com.opentok.android.MediaCodecVideoEncoder.3
        {
            append(VideoCodecType.VIDEO_CODEC_VP8.ordinal(), 100);
            append(VideoCodecType.VIDEO_CODEC_VP9.ordinal(), 100);
            append(VideoCodecType.VIDEO_CODEC_H264.ordinal(), 20);
        }
    };
    private static final String[] H264_BLACKLIST = {"SAMSUNG-SGH-I337", "Nexus 7", "Nexus 4"};
    private static final int COLOR_QCOM_FORMATYUV420PackedSemiPlanar32m = 2141391876;
    private static final int[] supportedColorList = {19, 21, 2141391872, COLOR_QCOM_FORMATYUV420PackedSemiPlanar32m};
    private static final int[] supportedSurfaceColorList = {2130708361};
    private BitrateAdjustmentType bitrateAdjustmentType = BitrateAdjustmentType.NO_ADJUSTMENT;
    private ByteBuffer configData = null;

    /* JADX INFO: Access modifiers changed from: package-private */
    /* renamed from: com.opentok.android.MediaCodecVideoEncoder$1CaughtException  reason: invalid class name */
    /* loaded from: classes.dex */
    public class C1CaughtException {
        Exception e;

        C1CaughtException() {
        }
    }

    /* loaded from: classes.dex */
    public enum BitrateAdjustmentType {
        NO_ADJUSTMENT,
        FRAMERATE_ADJUSTMENT,
        DYNAMIC_ADJUSTMENT
    }

    /* loaded from: classes.dex */
    public static class EncoderProperties {
        public final BitrateAdjustmentType bitrateAdjustmentType;
        public final String codecName;
        public final int colorFormat;
        public final Map<String, Integer> meta;

        public EncoderProperties(String str, int i, BitrateAdjustmentType bitrateAdjustmentType, Map<String, Integer> map) {
            this.codecName = str;
            this.colorFormat = i;
            this.bitrateAdjustmentType = bitrateAdjustmentType;
            this.meta = map;
        }
    }

    /* loaded from: classes.dex */
    public enum H264Profile {
        CONSTRAINED_BASELINE(0),
        BASELINE(1),
        MAIN(2),
        CONSTRAINED_HIGH(3),
        HIGH(4);
        
        private final int value;

        H264Profile(int i) {
            this.value = i;
        }

        public int getValue() {
            return this.value;
        }
    }

    /* loaded from: classes.dex */
    public interface MediaCodecVideoEncoderErrorCallback {
        void onMediaCodecVideoEncoderCriticalError(int i);
    }

    /* loaded from: classes.dex */
    static class OutputBufferInfo {
        public final ByteBuffer buffer;
        public final int index;
        public final boolean isKeyFrame;
        public final long presentationTimestampUs;

        public OutputBufferInfo(int i, ByteBuffer byteBuffer, boolean z, long j) {
            this.index = i;
            this.buffer = byteBuffer;
            this.isKeyFrame = z;
            this.presentationTimestampUs = j;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes.dex */
    public static class SupportedEncoderRecord {
        public final BitrateAdjustmentType bitrateAdjustmentType;
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

        public SupportedEncoderRecord(String str, int i, Priority priority, BitrateAdjustmentType bitrateAdjustmentType, Map<String, Integer> map) {
            this.prefix = str;
            this.supportedVersion = i;
            this.priority = priority;
            this.bitrateAdjustmentType = bitrateAdjustmentType;
            this.meta = map == null ? new HashMap<>() : map;
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

    MediaCodecVideoEncoder() {
    }

    private void checkOnMediaCodecThread() {
        if (this.mediaCodecThread.getId() == Thread.currentThread().getId()) {
            return;
        }
        throw new RuntimeException("MediaCodecVideoEncoder previously operated on " + this.mediaCodecThread + " but is now called on " + Thread.currentThread());
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static MediaCodec createByCodecName(String str) {
        try {
            return MediaCodec.createByCodecName(str);
        } catch (Exception unused) {
            return null;
        }
    }

    private static List<Integer> createIntList(int[] iArr) {
        Vector vector = new Vector(iArr.length);
        for (int i : iArr) {
            vector.add(Integer.valueOf(i));
        }
        return vector;
    }

    public static void disableH264HwCodec() {
        log.w("H.264 encoding is disabled by application.", new Object[0]);
        hwEncoderDisabledTypes.add(H264_MIME_TYPE);
    }

    public static void disableVp8HwCodec() {
        log.w("VP8 encoding is disabled by application.", new Object[0]);
        hwEncoderDisabledTypes.add(VP8_MIME_TYPE);
    }

    public static void disableVp9HwCodec() {
        log.w("VP9 encoding is disabled by application.", new Object[0]);
        hwEncoderDisabledTypes.add(VP9_MIME_TYPE);
    }

    private static EncoderProperties[] findHwEncoder(String str, SupportedEncoderRecord[] supportedEncoderRecordArr, int[] iArr) {
        int i;
        int i2;
        MediaCodecInfo mediaCodecInfo;
        String str2 = str;
        int i3 = 0;
        log.d("Trying to find HW encoder for mime " + str2, new Object[0]);
        Vector<Pair> vector = new Vector();
        MediaCodecInfo[] codecList = getCodecList();
        int length = codecList.length;
        int i4 = 0;
        while (i4 < length) {
            MediaCodecInfo mediaCodecInfo2 = codecList[i4];
            if (Arrays.asList(mediaCodecInfo2.getSupportedTypes()).contains(str2)) {
                if (str2.equals(H264_MIME_TYPE) && Arrays.asList(H264_BLACKLIST).contains(Build.MODEL)) {
                    log.w("Model: " + Build.MODEL + " is blacklisted for H264.", new Object[i3]);
                } else {
                    try {
                        try {
                            List<Integer> createIntList = createIntList(mediaCodecInfo2.getCapabilitiesForType(str2).colorFormats);
                            int length2 = supportedEncoderRecordArr.length;
                            int i5 = i3;
                            while (i5 < length2) {
                                SupportedEncoderRecord supportedEncoderRecord = supportedEncoderRecordArr[i5];
                                if (Build.VERSION.SDK_INT >= supportedEncoderRecord.supportedVersion && mediaCodecInfo2.getName().startsWith(supportedEncoderRecord.prefix)) {
                                    int length3 = iArr.length;
                                    for (int i6 = i3; i6 < length3; i6++) {
                                        int i7 = iArr[i6];
                                        try {
                                            if (createIntList.contains(Integer.valueOf(i7))) {
                                                i = 0;
                                                try {
                                                    log.d("Found target encoder " + mediaCodecInfo2.getName() + ". Color: 0x" + Integer.toHexString(i7) + ". Bitrate adjustment: " + supportedEncoderRecord.bitrateAdjustmentType, new Object[0]);
                                                    mediaCodecInfo = mediaCodecInfo2;
                                                    vector.add(new Pair(new EncoderProperties(mediaCodecInfo2.getName(), i7, supportedEncoderRecord.bitrateAdjustmentType, supportedEncoderRecord.meta), supportedEncoderRecord.priority));
                                                    break;
                                                } catch (IllegalArgumentException e) {
                                                    e = e;
                                                    i2 = 1;
                                                    OtLog.LogToken logToken = log;
                                                    Object[] objArr = new Object[i2];
                                                    objArr[i] = e;
                                                    logToken.e("Cannot retreive decoder capabilities", objArr);
                                                    i4++;
                                                    i3 = i;
                                                    str2 = str;
                                                }
                                            }
                                        } catch (IllegalArgumentException e2) {
                                            e = e2;
                                            i2 = 1;
                                            i = 0;
                                            OtLog.LogToken logToken2 = log;
                                            Object[] objArr2 = new Object[i2];
                                            objArr2[i] = e;
                                            logToken2.e("Cannot retreive decoder capabilities", objArr2);
                                            i4++;
                                            i3 = i;
                                            str2 = str;
                                        }
                                    }
                                }
                                mediaCodecInfo = mediaCodecInfo2;
                                i5++;
                                mediaCodecInfo2 = mediaCodecInfo;
                                i3 = 0;
                            }
                        } catch (Exception e3) {
                            i = 0;
                            log.e("general error", e3);
                            e3.printStackTrace();
                        }
                    } catch (IllegalArgumentException e4) {
                        e = e4;
                        i = i3;
                    }
                }
            }
            i = i3;
            i4++;
            i3 = i;
            str2 = str;
        }
        int i8 = i3;
        Collections.sort(vector, new Comparator<Pair<EncoderProperties, SupportedEncoderRecord.Priority>>() { // from class: com.opentok.android.MediaCodecVideoEncoder.4
            @Override // java.util.Comparator
            public int compare(Pair<EncoderProperties, SupportedEncoderRecord.Priority> pair, Pair<EncoderProperties, SupportedEncoderRecord.Priority> pair2) {
                return ((SupportedEncoderRecord.Priority) pair.second).ordinal() - ((SupportedEncoderRecord.Priority) pair2.second).ordinal();
            }
        });
        EncoderProperties[] encoderPropertiesArr = new EncoderProperties[vector.size()];
        for (Pair pair : vector) {
            encoderPropertiesArr[i8] = (EncoderProperties) pair.first;
            i8++;
        }
        return encoderPropertiesArr;
    }

    private double getBitrateScale(int i) {
        return Math.pow(BITRATE_CORRECTION_MAX_SCALE, i / 20.0d);
    }

    private static MediaCodecInfo[] getCodecList() {
        if (Build.VERSION.SDK_INT < 19) {
            return new MediaCodecInfo[0];
        }
        Vector vector = new Vector();
        for (int i = 0; i < MediaCodecList.getCodecCount(); i++) {
            MediaCodecInfo codecInfoAt = MediaCodecList.getCodecInfoAt(i);
            if (codecInfoAt != null && codecInfoAt.isEncoder()) {
                vector.add(codecInfoAt);
            }
        }
        return (MediaCodecInfo[]) vector.toArray(new MediaCodecInfo[vector.size()]);
    }

    private boolean initEncoder(VideoCodecType videoCodecType, EncoderProperties encoderProperties, EglBase14.Context context, String str) {
        int i = KeyFrameIntervalSecTbl.get(videoCodecType.ordinal());
        boolean z = this.profile == H264Profile.CONSTRAINED_HIGH.getValue() && isH264HighProfileHwSupported();
        try {
            MediaFormat createVideoFormat = MediaFormat.createVideoFormat(str, this.width, this.height);
            createVideoFormat.setInteger("bitrate", this.targetBitrateBps);
            createVideoFormat.setInteger("bitrate-mode", 2);
            createVideoFormat.setInteger("color-format", encoderProperties.colorFormat);
            createVideoFormat.setInteger("frame-rate", this.targetFps);
            createVideoFormat.setInteger("i-frame-interval", i);
            if (z) {
                createVideoFormat.setInteger(Scopes.PROFILE, 8);
                createVideoFormat.setInteger(FirebaseAnalytics.Param.LEVEL, 256);
            }
            OtLog.LogToken logToken = log;
            logToken.d("  Format: " + createVideoFormat, new Object[0]);
            MediaCodec createByCodecName = createByCodecName(encoderProperties.codecName);
            this.mediaCodec = createByCodecName;
            this.type = videoCodecType;
            if (createByCodecName == null) {
                log.e("Can not create media encoder", new Object[0]);
                release();
                return false;
            }
            createByCodecName.configure(createVideoFormat, (Surface) null, (MediaCrypto) null, 1);
            this.mediaCodec.start();
            this.outputBuffers = this.mediaCodec.getOutputBuffers();
            OtLog.LogToken logToken2 = log;
            logToken2.d("Output buffers: " + this.outputBuffers.length, new Object[0]);
            return true;
        } catch (Exception e) {
            log.e("initEncode failed", e);
            release();
            return false;
        }
    }

    public static boolean isH264HighProfileHwSupported() {
        EncoderProperties[] findHwEncoder = findHwEncoder(H264_MIME_TYPE, H264_SUPPORT, supportedColorList);
        return findHwEncoder.length > 0 && findHwEncoder[0].meta.containsKey("HighProfile") && Build.VERSION.SDK_INT >= findHwEncoder[0].meta.get("HighProfile").intValue();
    }

    public static boolean isH264HwSupported() {
        return !hwEncoderDisabledTypes.contains(H264_MIME_TYPE) && findHwEncoder(H264_MIME_TYPE, H264_SUPPORT, supportedColorList).length > 0;
    }

    public static boolean isH264HwSupportedUsingTextures() {
        return !hwEncoderDisabledTypes.contains(H264_MIME_TYPE) && findHwEncoder(H264_MIME_TYPE, H264_SUPPORT, supportedSurfaceColorList).length > 0;
    }

    public static boolean isVp8HwSupported() {
        return !hwEncoderDisabledTypes.contains(VP8_MIME_TYPE) && findHwEncoder(VP8_MIME_TYPE, VP8_SUPPORT, supportedColorList).length > 0;
    }

    public static boolean isVp8HwSupportedUsingTextures() {
        return !hwEncoderDisabledTypes.contains(VP8_MIME_TYPE) && findHwEncoder(VP8_MIME_TYPE, VP8_SUPPORT, supportedSurfaceColorList).length > 0;
    }

    public static boolean isVp9HwSupported() {
        return !hwEncoderDisabledTypes.contains(VP9_MIME_TYPE) && findHwEncoder(VP9_MIME_TYPE, VP9_SUPPORT, supportedColorList).length > 0;
    }

    public static boolean isVp9HwSupportedUsingTextures() {
        return !hwEncoderDisabledTypes.contains(VP9_MIME_TYPE) && findHwEncoder(VP9_MIME_TYPE, VP9_SUPPORT, supportedSurfaceColorList).length > 0;
    }

    public static void printStackTrace() {
        Thread thread;
        MediaCodecVideoEncoder mediaCodecVideoEncoder = runningInstance;
        if (mediaCodecVideoEncoder == null || (thread = mediaCodecVideoEncoder.mediaCodecThread) == null) {
            return;
        }
        StackTraceElement[] stackTrace = thread.getStackTrace();
        if (stackTrace.length > 0) {
            log.d("MediaCodecVideoEncoder stacks trace:", new Object[0]);
            for (StackTraceElement stackTraceElement : stackTrace) {
                log.d(stackTraceElement.toString(), new Object[0]);
            }
        }
    }

    private void reportEncodedFrame(int i) {
        int i2 = this.targetFps;
        if (i2 == 0 || this.bitrateAdjustmentType != BitrateAdjustmentType.DYNAMIC_ADJUSTMENT) {
            return;
        }
        double d = i2;
        double d2 = this.bitrateAccumulator + (i - (this.targetBitrateBps / (8.0d * d)));
        this.bitrateAccumulator = d2;
        this.bitrateObservationTimeMs += 1000.0d / d;
        double d3 = this.bitrateAccumulatorMax * BITRATE_CORRECTION_SEC;
        double min = Math.min(d2, d3);
        this.bitrateAccumulator = min;
        this.bitrateAccumulator = Math.max(min, -d3);
        if (this.bitrateObservationTimeMs > 3000.0d) {
            log.d("Acc: " + ((int) this.bitrateAccumulator) + ". Max: " + ((int) this.bitrateAccumulatorMax) + ". ExpScale: " + this.bitrateAdjustmentScaleExp, new Object[0]);
            double d4 = this.bitrateAccumulator;
            double d5 = this.bitrateAccumulatorMax;
            boolean z = true;
            if (d4 > d5) {
                this.bitrateAdjustmentScaleExp -= (int) ((d4 / d5) + 0.5d);
                this.bitrateAccumulator = d5;
            } else {
                double d6 = -d5;
                if (d4 < d6) {
                    this.bitrateAdjustmentScaleExp += (int) (((-d4) / d5) + 0.5d);
                    this.bitrateAccumulator = d6;
                } else {
                    z = false;
                }
            }
            if (z) {
                int min2 = Math.min(this.bitrateAdjustmentScaleExp, 20);
                this.bitrateAdjustmentScaleExp = min2;
                this.bitrateAdjustmentScaleExp = Math.max(min2, -20);
                log.d("Adjusting bitrate scale to " + this.bitrateAdjustmentScaleExp + ". Value: " + getBitrateScale(this.bitrateAdjustmentScaleExp), new Object[0]);
                setRates(this.targetBitrateBps / 1000, this.targetFps);
            }
            this.bitrateObservationTimeMs = com.github.mikephil.charting.utils.Utils.DOUBLE_EPSILON;
        }
    }

    public static void setErrorCallback(MediaCodecVideoEncoderErrorCallback mediaCodecVideoEncoderErrorCallback) {
        log.d("Set error callback", new Object[0]);
        errorCallback = mediaCodecVideoEncoderErrorCallback;
    }

    private boolean setRates(int i, int i2) {
        OtLog.LogToken logToken;
        StringBuilder sb;
        checkOnMediaCodecThread();
        int i3 = i * 1000;
        if (this.bitrateAdjustmentType == BitrateAdjustmentType.DYNAMIC_ADJUSTMENT) {
            double d = i3;
            this.bitrateAccumulatorMax = d / 8.0d;
            int i4 = this.targetBitrateBps;
            if (i4 > 0 && i3 < i4) {
                this.bitrateAccumulator = (this.bitrateAccumulator * d) / i4;
            }
        }
        this.targetBitrateBps = i3;
        this.targetFps = i2;
        try {
            if (this.bitrateAdjustmentType == BitrateAdjustmentType.FRAMERATE_ADJUSTMENT && i2 > 0) {
                i3 = (i3 * 30) / i2;
                logToken = log;
                sb = new StringBuilder();
                sb.append("setRates: ");
                sb.append(i);
                sb.append(" -> ");
                i = i3 / 1000;
            } else if (this.bitrateAdjustmentType == BitrateAdjustmentType.DYNAMIC_ADJUSTMENT) {
                log.v("setRates: " + i + " kbps. Fps: " + this.targetFps + ". ExpScale: " + this.bitrateAdjustmentScaleExp, new Object[0]);
                int i5 = this.bitrateAdjustmentScaleExp;
                if (i5 != 0) {
                    i3 = (int) (i3 * getBitrateScale(i5));
                }
                Bundle bundle = new Bundle();
                bundle.putInt("video-bitrate", i3);
                this.mediaCodec.setParameters(bundle);
                return true;
            } else {
                logToken = log;
                sb = new StringBuilder();
                sb.append("setRates: ");
            }
            Bundle bundle2 = new Bundle();
            bundle2.putInt("video-bitrate", i3);
            this.mediaCodec.setParameters(bundle2);
            return true;
        } catch (IllegalStateException e) {
            log.e("setRates failed", e);
            return false;
        }
        sb.append(i);
        sb.append(" kbps. Fps: ");
        sb.append(this.targetFps);
        logToken.v(sb.toString(), new Object[0]);
    }

    /* JADX WARN: Removed duplicated region for block: B:16:0x0029  */
    /* JADX WARN: Removed duplicated region for block: B:18:0x0033  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    void checkKeyFrameRequired(boolean r7, long r8) {
        /*
            r6 = this;
            r0 = 500(0x1f4, double:2.47E-321)
            long r8 = r8 + r0
            r0 = 1000(0x3e8, double:4.94E-321)
            long r8 = r8 / r0
            long r0 = r6.lastKeyFrameMs
            r2 = 0
            int r0 = (r0 > r2 ? 1 : (r0 == r2 ? 0 : -1))
            if (r0 >= 0) goto L10
            r6.lastKeyFrameMs = r8
        L10:
            r0 = 0
            if (r7 != 0) goto L22
            long r4 = r6.forcedKeyFrameMs
            int r1 = (r4 > r2 ? 1 : (r4 == r2 ? 0 : -1))
            if (r1 <= 0) goto L22
            long r1 = r6.lastKeyFrameMs
            long r1 = r1 + r4
            int r1 = (r8 > r1 ? 1 : (r8 == r1 ? 0 : -1))
            if (r1 <= 0) goto L22
            r1 = 1
            goto L23
        L22:
            r1 = r0
        L23:
            if (r7 != 0) goto L27
            if (r1 == 0) goto L4b
        L27:
            if (r7 == 0) goto L33
            com.opentok.android.OtLog$LogToken r7 = com.opentok.android.MediaCodecVideoEncoder.log
            java.lang.Object[] r1 = new java.lang.Object[r0]
            java.lang.String r2 = "Sync frame request"
        L2f:
            r7.d(r2, r1)
            goto L3a
        L33:
            com.opentok.android.OtLog$LogToken r7 = com.opentok.android.MediaCodecVideoEncoder.log
            java.lang.Object[] r1 = new java.lang.Object[r0]
            java.lang.String r2 = "Sync frame forced"
            goto L2f
        L3a:
            android.os.Bundle r7 = new android.os.Bundle
            r7.<init>()
            java.lang.String r1 = "request-sync"
            r7.putInt(r1, r0)
            android.media.MediaCodec r0 = r6.mediaCodec
            r0.setParameters(r7)
            r6.lastKeyFrameMs = r8
        L4b:
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.opentok.android.MediaCodecVideoEncoder.checkKeyFrameRequired(boolean, long):void");
    }

    int dequeueInputBuffer() {
        checkOnMediaCodecThread();
        try {
            return this.mediaCodec.dequeueInputBuffer(0L);
        } catch (IllegalStateException e) {
            log.e("dequeueIntputBuffer failed", e);
            return -2;
        }
    }

    OutputBufferInfo dequeueOutputBuffer() {
        checkOnMediaCodecThread();
        try {
            MediaCodec.BufferInfo bufferInfo = new MediaCodec.BufferInfo();
            int dequeueOutputBuffer = this.mediaCodec.dequeueOutputBuffer(bufferInfo, 0L);
            if (dequeueOutputBuffer >= 0) {
                if ((bufferInfo.flags & 2) != 0) {
                    log.d("Config frame generated. Offset: " + bufferInfo.offset + ". Size: " + bufferInfo.size, new Object[0]);
                    this.configData = ByteBuffer.allocateDirect(bufferInfo.size);
                    this.outputBuffers[dequeueOutputBuffer].position(bufferInfo.offset);
                    this.outputBuffers[dequeueOutputBuffer].limit(bufferInfo.offset + bufferInfo.size);
                    this.configData.put(this.outputBuffers[dequeueOutputBuffer]);
                    String str = "";
                    int i = 0;
                    while (true) {
                        if (i >= (bufferInfo.size < 8 ? bufferInfo.size : 8)) {
                            break;
                        }
                        str = str + Integer.toHexString(this.configData.get(i) & 255) + " ";
                        i++;
                    }
                    log.d(str, new Object[0]);
                    this.mediaCodec.releaseOutputBuffer(dequeueOutputBuffer, false);
                    dequeueOutputBuffer = this.mediaCodec.dequeueOutputBuffer(bufferInfo, 0L);
                }
            }
            if (dequeueOutputBuffer < 0) {
                if (dequeueOutputBuffer == -3) {
                    this.outputBuffers = this.mediaCodec.getOutputBuffers();
                    return dequeueOutputBuffer();
                } else if (dequeueOutputBuffer == -2) {
                    return dequeueOutputBuffer();
                } else {
                    if (dequeueOutputBuffer == -1) {
                        return null;
                    }
                    throw new RuntimeException("dequeueOutputBuffer: " + dequeueOutputBuffer);
                }
            }
            ByteBuffer duplicate = this.outputBuffers[dequeueOutputBuffer].duplicate();
            duplicate.position(bufferInfo.offset);
            duplicate.limit(bufferInfo.offset + bufferInfo.size);
            reportEncodedFrame(bufferInfo.size);
            boolean z = (bufferInfo.flags & 1) != 0;
            if (z) {
                log.d("Sync frame generated", new Object[0]);
            }
            if (z && this.type == VideoCodecType.VIDEO_CODEC_H264) {
                log.d("Appending config frame of size " + this.configData.capacity() + " to output buffer with offset " + bufferInfo.offset + ", size " + bufferInfo.size, new Object[0]);
                ByteBuffer allocateDirect = ByteBuffer.allocateDirect(this.configData.capacity() + bufferInfo.size);
                this.configData.rewind();
                allocateDirect.put(this.configData);
                allocateDirect.put(duplicate);
                allocateDirect.position(0);
                return new OutputBufferInfo(dequeueOutputBuffer, allocateDirect, z, bufferInfo.presentationTimeUs);
            }
            return new OutputBufferInfo(dequeueOutputBuffer, duplicate.slice(), z, bufferInfo.presentationTimeUs);
        } catch (IllegalStateException e) {
            log.e("dequeueOutputBuffer failed", e);
            return new OutputBufferInfo(-1, null, false, -1L);
        }
    }

    boolean encodeBuffer(boolean z, int i, int i2, long j) {
        checkOnMediaCodecThread();
        try {
            checkKeyFrameRequired(z, j);
            this.mediaCodec.queueInputBuffer(i, 0, i2, j, 0);
            return true;
        } catch (IllegalStateException e) {
            log.e("encodeBuffer failed", e);
            return false;
        }
    }

    boolean encodeTexture(boolean z, int i, float[] fArr, long j) {
        checkOnMediaCodecThread();
        try {
            checkKeyFrameRequired(z, j);
            this.eglBase.makeCurrent();
            GLES20.glClear(16384);
            this.drawer.drawOes(i, fArr, this.width, this.height, 0, 0, this.width, this.height);
            this.eglBase.swapBuffers(TimeUnit.MICROSECONDS.toNanos(j));
            return true;
        } catch (RuntimeException e) {
            log.e("encodeTexture failed", e);
            return false;
        }
    }

    ByteBuffer[] getInputBuffers() {
        ByteBuffer[] inputBuffers = this.mediaCodec.getInputBuffers();
        OtLog.LogToken logToken = log;
        logToken.d("Input buffers: " + inputBuffers.length, new Object[0]);
        return inputBuffers;
    }

    /* JADX WARN: Code restructure failed: missing block: B:37:0x00df, code lost:
        if (r12 > 23) goto L33;
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    boolean initEncode(com.opentok.android.MediaCodecVideoEncoder.VideoCodecType r8, int r9, int r10, int r11, int r12, int r13, org.otwebrtc.EglBase14.Context r14) {
        /*
            Method dump skipped, instructions count: 388
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: com.opentok.android.MediaCodecVideoEncoder.initEncode(com.opentok.android.MediaCodecVideoEncoder$VideoCodecType, int, int, int, int, int, org.otwebrtc.EglBase14$Context):boolean");
    }

    void release() {
        boolean z;
        log.d("Java releaseEncoder", new Object[0]);
        checkOnMediaCodecThread();
        final C1CaughtException c1CaughtException = new C1CaughtException();
        if (this.mediaCodec != null) {
            final CountDownLatch countDownLatch = new CountDownLatch(1);
            new Thread(new Runnable() { // from class: com.opentok.android.MediaCodecVideoEncoder.5
                @Override // java.lang.Runnable
                public void run() {
                    MediaCodecVideoEncoder.log.d("Java releaseEncoder on release thread", new Object[0]);
                    try {
                        MediaCodecVideoEncoder.this.mediaCodec.stop();
                    } catch (Exception e) {
                        MediaCodecVideoEncoder.log.e("Media encoder stop failed", e);
                    }
                    try {
                        MediaCodecVideoEncoder.this.mediaCodec.release();
                    } catch (Exception e2) {
                        MediaCodecVideoEncoder.log.e("Media encoder release failed", e2);
                        c1CaughtException.e = e2;
                    }
                    MediaCodecVideoEncoder.log.d("Java releaseEncoder on release thread done", new Object[0]);
                    countDownLatch.countDown();
                }
            }).start();
            if (ThreadUtils.awaitUninterruptibly(countDownLatch, 5000L)) {
                z = false;
            } else {
                log.e("Media encoder release timeout", new Object[0]);
                z = true;
            }
            this.mediaCodec = null;
        } else {
            z = false;
        }
        this.mediaCodecThread = null;
        GlRectDrawer glRectDrawer = this.drawer;
        if (glRectDrawer != null) {
            glRectDrawer.release();
            this.drawer = null;
        }
        EglBase14 eglBase14 = this.eglBase;
        if (eglBase14 != null) {
            eglBase14.release();
            this.eglBase = null;
        }
        Surface surface = this.inputSurface;
        if (surface != null) {
            surface.release();
            this.inputSurface = null;
        }
        runningInstance = null;
        if (!z) {
            if (c1CaughtException.e != null) {
                throw new RuntimeException(c1CaughtException.e);
            }
            log.d("Java releaseEncoder done", new Object[0]);
            return;
        }
        codecErrors++;
        if (errorCallback != null) {
            log.e("Invoke codec error callback. Errors: " + codecErrors, new Object[0]);
            errorCallback.onMediaCodecVideoEncoderCriticalError(codecErrors);
        }
        throw new RuntimeException("Media encoder release timeout.");
    }

    boolean releaseOutputBuffer(int i) {
        checkOnMediaCodecThread();
        try {
            this.mediaCodec.releaseOutputBuffer(i, false);
            return true;
        } catch (IllegalStateException e) {
            log.e("releaseOutputBuffer failed", e);
            return false;
        }
    }
}
