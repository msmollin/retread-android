package org.otwebrtc;

import androidx.annotation.Nullable;
import org.otwebrtc.EncodedImage;

/* loaded from: classes2.dex */
public interface VideoEncoder {

    /* loaded from: classes2.dex */
    public static class BitrateAllocation {
        public final int[][] bitratesBbs;

        @CalledByNative("BitrateAllocation")
        public BitrateAllocation(int[][] iArr) {
            this.bitratesBbs = iArr;
        }

        public int getSum() {
            int[][] iArr = this.bitratesBbs;
            int length = iArr.length;
            int i = 0;
            int i2 = 0;
            while (i < length) {
                int i3 = i2;
                for (int i4 : iArr[i]) {
                    i3 += i4;
                }
                i++;
                i2 = i3;
            }
            return i2;
        }
    }

    /* loaded from: classes2.dex */
    public interface Callback {
        void onEncodedFrame(EncodedImage encodedImage, CodecSpecificInfo codecSpecificInfo);
    }

    /* loaded from: classes2.dex */
    public static class Capabilities {
        public final boolean lossNotification;

        @CalledByNative("Capabilities")
        public Capabilities(boolean z) {
            this.lossNotification = z;
        }
    }

    /* loaded from: classes2.dex */
    public static class CodecSpecificInfo {
    }

    /* loaded from: classes2.dex */
    public static class CodecSpecificInfoH264 extends CodecSpecificInfo {
    }

    /* loaded from: classes2.dex */
    public static class CodecSpecificInfoVP8 extends CodecSpecificInfo {
    }

    /* loaded from: classes2.dex */
    public static class CodecSpecificInfoVP9 extends CodecSpecificInfo {
    }

    /* loaded from: classes2.dex */
    public static class EncodeInfo {
        public final EncodedImage.FrameType[] frameTypes;

        @CalledByNative("EncodeInfo")
        public EncodeInfo(EncodedImage.FrameType[] frameTypeArr) {
            this.frameTypes = frameTypeArr;
        }
    }

    /* loaded from: classes2.dex */
    public static class ResolutionBitrateLimits {
        public final int frameSizePixels;
        public final int maxBitrateBps;
        public final int minBitrateBps;
        public final int minStartBitrateBps;

        public ResolutionBitrateLimits(int i, int i2, int i3, int i4) {
            this.frameSizePixels = i;
            this.minStartBitrateBps = i2;
            this.minBitrateBps = i3;
            this.maxBitrateBps = i4;
        }

        @CalledByNative("ResolutionBitrateLimits")
        public int getFrameSizePixels() {
            return this.frameSizePixels;
        }

        @CalledByNative("ResolutionBitrateLimits")
        public int getMaxBitrateBps() {
            return this.maxBitrateBps;
        }

        @CalledByNative("ResolutionBitrateLimits")
        public int getMinBitrateBps() {
            return this.minBitrateBps;
        }

        @CalledByNative("ResolutionBitrateLimits")
        public int getMinStartBitrateBps() {
            return this.minStartBitrateBps;
        }
    }

    /* loaded from: classes2.dex */
    public static class ScalingSettings {
        public static final ScalingSettings OFF = new ScalingSettings();
        @Nullable
        public final Integer high;
        @Nullable
        public final Integer low;
        public final boolean on;

        private ScalingSettings() {
            this.on = false;
            this.low = null;
            this.high = null;
        }

        public ScalingSettings(int i, int i2) {
            this.on = true;
            this.low = Integer.valueOf(i);
            this.high = Integer.valueOf(i2);
        }

        @Deprecated
        public ScalingSettings(boolean z) {
            this.on = z;
            this.low = null;
            this.high = null;
        }

        @Deprecated
        public ScalingSettings(boolean z, int i, int i2) {
            this.on = z;
            this.low = Integer.valueOf(i);
            this.high = Integer.valueOf(i2);
        }

        public String toString() {
            if (this.on) {
                return "[ " + this.low + ", " + this.high + " ]";
            }
            return "OFF";
        }
    }

    /* loaded from: classes2.dex */
    public static class Settings {
        public final boolean automaticResizeOn;
        public final Capabilities capabilities;
        public final int height;
        public final int maxFramerate;
        public final int numberOfCores;
        public final int numberOfSimulcastStreams;
        public final int startBitrate;
        public final int width;

        @Deprecated
        public Settings(int i, int i2, int i3, int i4, int i5, int i6, boolean z) {
            this(i, i2, i3, i4, i5, i6, z, new Capabilities(false));
        }

        @CalledByNative("Settings")
        public Settings(int i, int i2, int i3, int i4, int i5, int i6, boolean z, Capabilities capabilities) {
            this.numberOfCores = i;
            this.width = i2;
            this.height = i3;
            this.startBitrate = i4;
            this.maxFramerate = i5;
            this.numberOfSimulcastStreams = i6;
            this.automaticResizeOn = z;
            this.capabilities = capabilities;
        }
    }

    @CalledByNative
    default long createNativeVideoEncoder() {
        return 0L;
    }

    @CalledByNative
    VideoCodecStatus encode(VideoFrame videoFrame, EncodeInfo encodeInfo);

    @CalledByNative
    String getImplementationName();

    @CalledByNative
    default ResolutionBitrateLimits[] getResolutionBitrateLimits() {
        return new ResolutionBitrateLimits[0];
    }

    @CalledByNative
    ScalingSettings getScalingSettings();

    @CalledByNative
    VideoCodecStatus initEncode(Settings settings, Callback callback);

    @CalledByNative
    default boolean isHardwareEncoder() {
        return true;
    }

    @CalledByNative
    VideoCodecStatus release();

    @CalledByNative
    VideoCodecStatus setRateAllocation(BitrateAllocation bitrateAllocation, int i);
}
