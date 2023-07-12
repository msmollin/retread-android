package com.bambuser.broadcaster;

import android.media.Image;
import android.os.Build;
import android.util.Log;
import java.nio.ByteBuffer;
import java.util.concurrent.atomic.AtomicReference;

/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: classes.dex */
public final class Frame {
    static final int ImageFormat_NV12 = 15731620;
    private static final String LOGTAG = "Frame";
    private volatile ByteBuffer[] mBufferArray;
    private final int[] mCroppedDetailArray;
    private final int[] mDetailArray;
    private final AtomicReference<ByteBuffer> mFrameBuffer;
    volatile int mHeight;
    private final AtomicReference<Image> mImage;
    final int mOriginalHeight;
    final int mOriginalWidth;
    volatile int mRotation;
    volatile long mTimestamp;
    volatile int mWidth;

    /* JADX INFO: Access modifiers changed from: package-private */
    public Frame(int i, int i2, int i3) {
        this(i, i2, i3, false);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public Frame(int i, int i2, int i3, boolean z) {
        this.mImage = new AtomicReference<>();
        this.mFrameBuffer = new AtomicReference<>();
        this.mRotation = 0;
        this.mTimestamp = 0L;
        this.mOriginalWidth = i2;
        this.mOriginalHeight = i3;
        this.mWidth = i2;
        this.mHeight = i3;
        this.mDetailArray = NativeUtils.fillImageFormatBufferDetails(null, i2, i3, i);
        this.mCroppedDetailArray = NativeUtils.fillImageFormatBufferDetails(null, i2, i3, i);
        if (z) {
            this.mFrameBuffer.set(ByteBuffer.allocate(neededSize(i, i2, i3)));
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public Frame(int i, int i2) {
        this.mImage = new AtomicReference<>();
        this.mFrameBuffer = new AtomicReference<>();
        this.mRotation = 0;
        this.mTimestamp = 0L;
        this.mOriginalWidth = i;
        this.mOriginalHeight = i2;
        this.mWidth = i;
        this.mHeight = i2;
        this.mDetailArray = new int[12];
        this.mCroppedDetailArray = new int[12];
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static int align(int i, int i2) {
        return i2 <= 0 ? i : (((i + i2) - 1) / i2) * i2;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static int neededSize(int i, int i2, int i3) {
        switch (i) {
            case 16:
                return i2 * 2 * i3;
            case 17:
            case ImageFormat_NV12 /* 15731620 */:
                return ((i2 * 3) * i3) / 2;
            case 842094169:
                int align = align(i2, 16);
                return (align * i3) + (((align(align / 2, 16) * i3) / 2) * 2);
            default:
                Log.w(LOGTAG, "Unsupported pixel format: " + i);
                return i2 * 4 * i3;
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void setCropRect(int i, int i2, int i3, int i4) {
        this.mWidth = i3;
        this.mHeight = i4;
        int i5 = (this.mDetailArray[6] * i) + (this.mDetailArray[3] * i2);
        int i6 = (i + 1) / 2;
        int i7 = (i6 * this.mDetailArray[7]) + (((i2 + 1) / 2) * this.mDetailArray[4]);
        this.mCroppedDetailArray[0] = this.mDetailArray[0] + i5;
        this.mCroppedDetailArray[1] = this.mDetailArray[1] + i7;
        this.mCroppedDetailArray[2] = this.mDetailArray[2] + i7;
        this.mCroppedDetailArray[9] = this.mHeight;
        this.mCroppedDetailArray[10] = (this.mHeight + 1) / 2;
        this.mCroppedDetailArray[11] = (this.mHeight + 1) / 2;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public ByteBuffer getBuffer() {
        return this.mFrameBuffer.get();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public int[] getDetailArray() {
        return this.mCroppedDetailArray;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public int[] getOriginalDetailArray() {
        return this.mDetailArray;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public ByteBuffer[] getBufferArray() {
        Image image;
        if (Build.VERSION.SDK_INT >= 21 && (image = this.mImage.get()) != null) {
            this.mBufferArray = NativeUtils.getImageByteBufferArray(this.mBufferArray, image);
            return this.mBufferArray;
        }
        this.mBufferArray = NativeUtils.getByteBufferArray(this.mBufferArray, this.mFrameBuffer.get());
        return this.mBufferArray;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void setImage(Image image) {
        this.mImage.set(image);
        NativeUtils.fillImageDetails(this.mDetailArray, image);
        NativeUtils.fillImageDetails(this.mCroppedDetailArray, image);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public Image removeImage() {
        return this.mImage.getAndSet(null);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void setBuffer(byte[] bArr) {
        this.mFrameBuffer.set(bArr == null ? null : ByteBuffer.wrap(bArr));
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public byte[] removeBuffer() {
        ByteBuffer andSet = this.mFrameBuffer.getAndSet(null);
        if (andSet != null) {
            return andSet.array();
        }
        return null;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public int capacity() {
        Image image;
        if (Build.VERSION.SDK_INT >= 21 && (image = this.mImage.get()) != null) {
            return ((image.getWidth() * image.getHeight()) * 3) / 2;
        }
        ByteBuffer byteBuffer = this.mFrameBuffer.get();
        if (byteBuffer != null) {
            return byteBuffer.capacity();
        }
        return 0;
    }
}
