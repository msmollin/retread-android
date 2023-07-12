package com.bambuser.broadcaster;

import android.annotation.TargetApi;
import android.media.Image;
import java.nio.ByteBuffer;

/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: classes.dex */
public final class NativeUtils {
    static final int COLOR_FormatNV21 = 1593835521;
    static final int COLOR_FormatYV12 = 1593835522;

    /* JADX INFO: Access modifiers changed from: package-private */
    public static native int copyScaleFrame(ByteBuffer[] byteBufferArr, int[] iArr, int i, int i2, ByteBuffer[] byteBufferArr2, int[] iArr2, int i3, int i4);

    /* JADX INFO: Access modifiers changed from: package-private */
    public static native int encodeMuLaw(byte[] bArr, int i, int i2, byte[] bArr2);

    /* JADX INFO: Access modifiers changed from: package-private */
    public static native int getCpuCount();

    /* JADX INFO: Access modifiers changed from: package-private */
    public static native boolean hasArm64();

    /* JADX INFO: Access modifiers changed from: package-private */
    public static native boolean hasArmv7();

    /* JADX INFO: Access modifiers changed from: package-private */
    public static native boolean hasNeon();

    /* JADX INFO: Access modifiers changed from: package-private */
    public static native boolean isX86_32();

    /* JADX INFO: Access modifiers changed from: package-private */
    public static native boolean isX86_64();

    /* JADX INFO: Access modifiers changed from: package-private */
    public static native void mix(byte[] bArr, int i, byte[] bArr2, int i2, int i3);

    /* JADX INFO: Access modifiers changed from: package-private */
    public static native void mixDown(byte[] bArr, int i, int i2);

    /* JADX INFO: Access modifiers changed from: package-private */
    public static native void mixStereo(byte[] bArr, int i, byte[] bArr2, int i2, byte[] bArr3, int i3, int i4);

    /* JADX INFO: Access modifiers changed from: package-private */
    public static native ByteBuffer setH264Cropping(ByteBuffer byteBuffer, int i, int i2, int i3, int i4, ByteBuffer byteBuffer2);

    /* JADX INFO: Access modifiers changed from: package-private */
    public static native ByteBuffer setHEVCCropping(ByteBuffer byteBuffer, int i, int i2, int i3, int i4, ByteBuffer byteBuffer2);

    static {
        System.loadLibrary("bambuser");
    }

    private NativeUtils() {
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static ByteBuffer[] getByteBufferArray(ByteBuffer[] byteBufferArr, ByteBuffer byteBuffer) {
        if (byteBufferArr == null || byteBufferArr.length < 3) {
            byteBufferArr = new ByteBuffer[3];
        }
        byteBufferArr[2] = byteBuffer;
        byteBufferArr[1] = byteBuffer;
        byteBufferArr[0] = byteBuffer;
        return byteBufferArr;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @TargetApi(19)
    public static ByteBuffer[] getImageByteBufferArray(ByteBuffer[] byteBufferArr, Image image) {
        if (byteBufferArr == null || byteBufferArr.length < 3) {
            byteBufferArr = new ByteBuffer[3];
        }
        Image.Plane[] planes = image.getPlanes();
        for (int i = 0; i < 3; i++) {
            byteBufferArr[i] = planes[i].getBuffer();
        }
        return byteBufferArr;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @TargetApi(19)
    public static int[] fillImageDetails(int[] iArr, Image image) {
        if (iArr == null || iArr.length < 12) {
            iArr = new int[12];
        }
        iArr[2] = 0;
        iArr[1] = 0;
        iArr[0] = 0;
        Image.Plane[] planes = image.getPlanes();
        int height = image.getHeight();
        iArr[3] = planes[0].getPixelStride();
        iArr[4] = planes[1].getPixelStride();
        iArr[5] = planes[2].getPixelStride();
        iArr[6] = planes[0].getRowStride();
        iArr[7] = planes[1].getRowStride();
        iArr[8] = planes[2].getRowStride();
        iArr[9] = height;
        int i = (height + 1) / 2;
        iArr[10] = i;
        iArr[11] = i;
        return iArr;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static int[] fillImageFormatBufferDetails(int[] iArr, int i, int i2, int i3) {
        switch (i3) {
            case 16:
                return fillOmxBufferDetails(iArr, i, i2, 0, 24);
            case 17:
                return fillOmxBufferDetails(iArr, i, i2, 0, COLOR_FormatNV21);
            case 15731620:
                return fillOmxBufferDetails(iArr, i, i2, 0, 21);
            case 842094169:
                return fillOmxBufferDetails(iArr, Frame.align(i, 16), i2, 0, COLOR_FormatYV12);
            default:
                return null;
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static int[] fillOmxBufferDetails(int[] iArr, int i, int i2, int i3, int i4) {
        char c;
        int i5;
        int i6;
        int i7;
        int[] iArr2 = iArr;
        if (iArr2 == null || iArr2.length < 12) {
            iArr2 = new int[12];
        }
        char c2 = 2;
        int i8 = (i + 1) / 2;
        int i9 = (i2 + 1) / 2;
        if (i4 != 19) {
            if (i4 != 21) {
                if (i4 != 24) {
                    switch (i4) {
                        case COLOR_FormatYV12 /* 1593835522 */:
                            i8 = Frame.align(i / 2, 16);
                            c = 1;
                            break;
                    }
                    return iArr2;
                }
                i8 = i;
                i5 = i8;
                i7 = 0;
                i6 = 1;
            } else {
                i5 = i8;
                i6 = 0;
                i7 = 1;
            }
            iArr2[0] = 0;
            int i10 = i * i2;
            iArr2[1] = iArr2[0] + Frame.align(i10, i3) + i6;
            iArr2[2] = iArr2[0] + Frame.align(i10, i3) + i7;
            iArr2[3] = 1;
            iArr2[4] = 2;
            iArr2[5] = 2;
            iArr2[6] = i;
            int i11 = i5 * 2;
            iArr2[7] = i11;
            iArr2[8] = i11;
            iArr2[9] = i2;
            iArr2[10] = i9;
            iArr2[11] = i9;
            return iArr2;
        }
        c = 2;
        c2 = 1;
        iArr2[0] = 0;
        iArr2[c2] = iArr2[0] + Frame.align(i * i2, i3);
        iArr2[c] = iArr2[c2] + Frame.align(i8 * i9, i3);
        iArr2[3] = 1;
        iArr2[4] = 1;
        iArr2[5] = 1;
        iArr2[6] = i;
        iArr2[7] = i8;
        iArr2[8] = i8;
        iArr2[9] = i2;
        iArr2[10] = i9;
        iArr2[11] = i9;
        return iArr2;
    }
}
