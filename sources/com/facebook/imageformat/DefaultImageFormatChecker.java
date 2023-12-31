package com.facebook.imageformat;

import com.facebook.common.internal.Ints;
import com.facebook.common.internal.Preconditions;
import com.facebook.common.webp.WebpSupportStatus;
import com.facebook.imageformat.ImageFormat;
import com.treadly.client.lib.sdk.Managers.Message;
import javax.annotation.Nullable;

/* loaded from: classes.dex */
public class DefaultImageFormatChecker implements ImageFormat.FormatChecker {
    private static final int EXTENDED_WEBP_HEADER_LENGTH = 21;
    private static final int GIF_HEADER_LENGTH = 6;
    private static final int SIMPLE_WEBP_HEADER_LENGTH = 20;
    final int MAX_HEADER_LENGTH = Ints.max(21, 20, JPEG_HEADER_LENGTH, PNG_HEADER_LENGTH, 6, BMP_HEADER_LENGTH, HEIF_HEADER_LENGTH);
    private static final byte[] JPEG_HEADER = {-1, -40, -1};
    private static final int JPEG_HEADER_LENGTH = JPEG_HEADER.length;
    private static final byte[] PNG_HEADER = {-119, Message.MESSAGE_ID_BLE_REMOTE_TEST_RESULTS, Message.MESSAGE_ID_SET_GAME_MODE, Message.MESSAGE_ID_SET_DEVICE_IR_DEBUG_LOG_MODE, 13, 10, Message.MESSAGE_ID_MAINTENANCE_STEP_REQUEST, 10};
    private static final int PNG_HEADER_LENGTH = PNG_HEADER.length;
    private static final byte[] GIF_HEADER_87A = ImageFormatCheckerUtils.asciiBytes("GIF87a");
    private static final byte[] GIF_HEADER_89A = ImageFormatCheckerUtils.asciiBytes("GIF89a");
    private static final byte[] BMP_HEADER = ImageFormatCheckerUtils.asciiBytes("BM");
    private static final int BMP_HEADER_LENGTH = BMP_HEADER.length;
    private static final String[] HEIF_HEADER_SUFFIXES = {"heic", "heix", "hevc", "hevx"};
    private static final String HEIF_HEADER_PREFIX = "ftyp";
    private static final int HEIF_HEADER_LENGTH = ImageFormatCheckerUtils.asciiBytes(HEIF_HEADER_PREFIX + HEIF_HEADER_SUFFIXES[0]).length;

    @Override // com.facebook.imageformat.ImageFormat.FormatChecker
    public int getHeaderSize() {
        return this.MAX_HEADER_LENGTH;
    }

    @Override // com.facebook.imageformat.ImageFormat.FormatChecker
    @Nullable
    public final ImageFormat determineFormat(byte[] bArr, int i) {
        Preconditions.checkNotNull(bArr);
        if (WebpSupportStatus.isWebpHeader(bArr, 0, i)) {
            return getWebpFormat(bArr, i);
        }
        if (isJpegHeader(bArr, i)) {
            return DefaultImageFormats.JPEG;
        }
        if (isPngHeader(bArr, i)) {
            return DefaultImageFormats.PNG;
        }
        if (isGifHeader(bArr, i)) {
            return DefaultImageFormats.GIF;
        }
        if (isBmpHeader(bArr, i)) {
            return DefaultImageFormats.BMP;
        }
        if (isHeifHeader(bArr, i)) {
            return DefaultImageFormats.HEIF;
        }
        return ImageFormat.UNKNOWN;
    }

    private static ImageFormat getWebpFormat(byte[] bArr, int i) {
        Preconditions.checkArgument(WebpSupportStatus.isWebpHeader(bArr, 0, i));
        if (WebpSupportStatus.isSimpleWebpHeader(bArr, 0)) {
            return DefaultImageFormats.WEBP_SIMPLE;
        }
        if (WebpSupportStatus.isLosslessWebpHeader(bArr, 0)) {
            return DefaultImageFormats.WEBP_LOSSLESS;
        }
        if (WebpSupportStatus.isExtendedWebpHeader(bArr, 0, i)) {
            if (WebpSupportStatus.isAnimatedWebpHeader(bArr, 0)) {
                return DefaultImageFormats.WEBP_ANIMATED;
            }
            if (WebpSupportStatus.isExtendedWebpHeaderWithAlpha(bArr, 0)) {
                return DefaultImageFormats.WEBP_EXTENDED_WITH_ALPHA;
            }
            return DefaultImageFormats.WEBP_EXTENDED;
        }
        return ImageFormat.UNKNOWN;
    }

    private static boolean isJpegHeader(byte[] bArr, int i) {
        return i >= JPEG_HEADER.length && ImageFormatCheckerUtils.startsWithPattern(bArr, JPEG_HEADER);
    }

    private static boolean isPngHeader(byte[] bArr, int i) {
        return i >= PNG_HEADER.length && ImageFormatCheckerUtils.startsWithPattern(bArr, PNG_HEADER);
    }

    private static boolean isGifHeader(byte[] bArr, int i) {
        if (i < 6) {
            return false;
        }
        return ImageFormatCheckerUtils.startsWithPattern(bArr, GIF_HEADER_87A) || ImageFormatCheckerUtils.startsWithPattern(bArr, GIF_HEADER_89A);
    }

    private static boolean isBmpHeader(byte[] bArr, int i) {
        if (i < BMP_HEADER.length) {
            return false;
        }
        return ImageFormatCheckerUtils.startsWithPattern(bArr, BMP_HEADER);
    }

    private static boolean isHeifHeader(byte[] bArr, int i) {
        String[] strArr;
        if (i >= HEIF_HEADER_LENGTH && bArr[3] >= 8) {
            for (String str : HEIF_HEADER_SUFFIXES) {
                if (ImageFormatCheckerUtils.indexOfPattern(bArr, bArr.length, ImageFormatCheckerUtils.asciiBytes(HEIF_HEADER_PREFIX + str), HEIF_HEADER_LENGTH) > -1) {
                    return true;
                }
            }
            return false;
        }
        return false;
    }
}
