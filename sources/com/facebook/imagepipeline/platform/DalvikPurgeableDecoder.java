package com.facebook.imagepipeline.platform;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;
import android.os.Build;
import com.facebook.common.internal.Preconditions;
import com.facebook.common.internal.Throwables;
import com.facebook.common.memory.PooledByteBuffer;
import com.facebook.common.references.CloseableReference;
import com.facebook.imagepipeline.common.TooManyBitmapsException;
import com.facebook.imagepipeline.image.EncodedImage;
import com.facebook.imagepipeline.memory.BitmapCounter;
import com.facebook.imagepipeline.memory.BitmapCounterProvider;
import com.facebook.imagepipeline.nativecode.Bitmaps;
import com.facebook.imageutils.BitmapUtil;
import java.util.Locale;
import javax.annotation.Nullable;

/* loaded from: classes.dex */
abstract class DalvikPurgeableDecoder implements PlatformDecoder {
    protected static final byte[] EOI = {-1, -39};
    private final BitmapCounter mUnpooledBitmapsCounter = BitmapCounterProvider.get();

    abstract Bitmap decodeByteArrayAsPurgeable(CloseableReference<PooledByteBuffer> closeableReference, BitmapFactory.Options options);

    abstract Bitmap decodeJPEGByteArrayAsPurgeable(CloseableReference<PooledByteBuffer> closeableReference, int i, BitmapFactory.Options options);

    @Override // com.facebook.imagepipeline.platform.PlatformDecoder
    public CloseableReference<Bitmap> decodeFromEncodedImage(EncodedImage encodedImage, Bitmap.Config config, @Nullable Rect rect) {
        BitmapFactory.Options bitmapFactoryOptions = getBitmapFactoryOptions(encodedImage.getSampleSize(), config);
        CloseableReference<PooledByteBuffer> byteBufferRef = encodedImage.getByteBufferRef();
        Preconditions.checkNotNull(byteBufferRef);
        try {
            return pinBitmap(decodeByteArrayAsPurgeable(byteBufferRef, bitmapFactoryOptions));
        } finally {
            CloseableReference.closeSafely(byteBufferRef);
        }
    }

    @Override // com.facebook.imagepipeline.platform.PlatformDecoder
    public CloseableReference<Bitmap> decodeJPEGFromEncodedImage(EncodedImage encodedImage, Bitmap.Config config, @Nullable Rect rect, int i) {
        BitmapFactory.Options bitmapFactoryOptions = getBitmapFactoryOptions(encodedImage.getSampleSize(), config);
        CloseableReference<PooledByteBuffer> byteBufferRef = encodedImage.getByteBufferRef();
        Preconditions.checkNotNull(byteBufferRef);
        try {
            return pinBitmap(decodeJPEGByteArrayAsPurgeable(byteBufferRef, i, bitmapFactoryOptions));
        } finally {
            CloseableReference.closeSafely(byteBufferRef);
        }
    }

    private static BitmapFactory.Options getBitmapFactoryOptions(int i, Bitmap.Config config) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inDither = true;
        options.inPreferredConfig = config;
        options.inPurgeable = true;
        options.inInputShareable = true;
        options.inSampleSize = i;
        if (Build.VERSION.SDK_INT >= 11) {
            options.inMutable = true;
        }
        return options;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public static boolean endsWithEOI(CloseableReference<PooledByteBuffer> closeableReference, int i) {
        PooledByteBuffer pooledByteBuffer = closeableReference.get();
        return i >= 2 && pooledByteBuffer.read(i + (-2)) == -1 && pooledByteBuffer.read(i - 1) == -39;
    }

    public CloseableReference<Bitmap> pinBitmap(Bitmap bitmap) {
        try {
            Bitmaps.pinBitmap(bitmap);
            if (!this.mUnpooledBitmapsCounter.increase(bitmap)) {
                int sizeInBytes = BitmapUtil.getSizeInBytes(bitmap);
                bitmap.recycle();
                throw new TooManyBitmapsException(String.format(Locale.US, "Attempted to pin a bitmap of size %d bytes. The current pool count is %d, the current pool size is %d bytes. The current pool max count is %d, the current pool max size is %d bytes.", Integer.valueOf(sizeInBytes), Integer.valueOf(this.mUnpooledBitmapsCounter.getCount()), Long.valueOf(this.mUnpooledBitmapsCounter.getSize()), Integer.valueOf(this.mUnpooledBitmapsCounter.getMaxCount()), Integer.valueOf(this.mUnpooledBitmapsCounter.getMaxSize())));
            }
            return CloseableReference.of(bitmap, this.mUnpooledBitmapsCounter.getReleaser());
        } catch (Exception e) {
            bitmap.recycle();
            throw Throwables.propagate(e);
        }
    }
}
