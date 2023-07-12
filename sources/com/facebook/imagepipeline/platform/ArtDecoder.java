package com.facebook.imagepipeline.platform;

import android.annotation.TargetApi;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;
import androidx.core.util.Pools;
import com.facebook.common.internal.Preconditions;
import com.facebook.common.internal.VisibleForTesting;
import com.facebook.common.references.CloseableReference;
import com.facebook.common.streams.LimitedInputStream;
import com.facebook.common.streams.TailAppendingInputStream;
import com.facebook.imagepipeline.image.EncodedImage;
import com.facebook.imagepipeline.memory.BitmapPool;
import java.io.InputStream;
import java.nio.ByteBuffer;
import javax.annotation.Nullable;
import javax.annotation.concurrent.ThreadSafe;

@ThreadSafe
@TargetApi(21)
/* loaded from: classes.dex */
public class ArtDecoder implements PlatformDecoder {
    private static final int DECODE_BUFFER_SIZE = 16384;
    private final BitmapPool mBitmapPool;
    @VisibleForTesting
    final Pools.SynchronizedPool<ByteBuffer> mDecodeBuffers;
    private static final Class<?> TAG = ArtDecoder.class;
    private static final byte[] EOI_TAIL = {-1, -39};

    public ArtDecoder(BitmapPool bitmapPool, int i, Pools.SynchronizedPool synchronizedPool) {
        this.mBitmapPool = bitmapPool;
        this.mDecodeBuffers = synchronizedPool;
        for (int i2 = 0; i2 < i; i2++) {
            this.mDecodeBuffers.release(ByteBuffer.allocate(16384));
        }
    }

    @Override // com.facebook.imagepipeline.platform.PlatformDecoder
    public CloseableReference<Bitmap> decodeFromEncodedImage(EncodedImage encodedImage, Bitmap.Config config, @Nullable Rect rect) {
        BitmapFactory.Options decodeOptionsForStream = getDecodeOptionsForStream(encodedImage, config);
        boolean z = decodeOptionsForStream.inPreferredConfig != Bitmap.Config.ARGB_8888;
        try {
            return decodeStaticImageFromStream(encodedImage.getInputStream(), decodeOptionsForStream, rect);
        } catch (RuntimeException e) {
            if (z) {
                return decodeFromEncodedImage(encodedImage, Bitmap.Config.ARGB_8888, rect);
            }
            throw e;
        }
    }

    @Override // com.facebook.imagepipeline.platform.PlatformDecoder
    public CloseableReference<Bitmap> decodeJPEGFromEncodedImage(EncodedImage encodedImage, Bitmap.Config config, @Nullable Rect rect, int i) {
        boolean isCompleteAt = encodedImage.isCompleteAt(i);
        BitmapFactory.Options decodeOptionsForStream = getDecodeOptionsForStream(encodedImage, config);
        InputStream inputStream = encodedImage.getInputStream();
        Preconditions.checkNotNull(inputStream);
        if (encodedImage.getSize() > i) {
            inputStream = new LimitedInputStream(inputStream, i);
        }
        InputStream tailAppendingInputStream = !isCompleteAt ? new TailAppendingInputStream(inputStream, EOI_TAIL) : inputStream;
        boolean z = decodeOptionsForStream.inPreferredConfig != Bitmap.Config.ARGB_8888;
        try {
            return decodeStaticImageFromStream(tailAppendingInputStream, decodeOptionsForStream, rect);
        } catch (RuntimeException e) {
            if (z) {
                return decodeFromEncodedImage(encodedImage, Bitmap.Config.ARGB_8888, rect);
            }
            throw e;
        }
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Removed duplicated region for block: B:27:0x0069 A[Catch: all -> 0x0090, RuntimeException -> 0x0092, IllegalArgumentException -> 0x0099, TryCatch #3 {RuntimeException -> 0x0092, blocks: (B:10:0x0033, B:17:0x004c, B:31:0x0070, B:24:0x0062, B:27:0x0069, B:28:0x006c), top: B:57:0x0033, outer: #1 }] */
    /* JADX WARN: Removed duplicated region for block: B:31:0x0070 A[Catch: all -> 0x0090, RuntimeException -> 0x0092, IllegalArgumentException -> 0x0099, TRY_LEAVE, TryCatch #3 {RuntimeException -> 0x0092, blocks: (B:10:0x0033, B:17:0x004c, B:31:0x0070, B:24:0x0062, B:27:0x0069, B:28:0x006c), top: B:57:0x0033, outer: #1 }] */
    /* JADX WARN: Removed duplicated region for block: B:34:0x007b  */
    /* JADX WARN: Removed duplicated region for block: B:36:0x0082  */
    /* JADX WARN: Type inference failed for: r0v1, types: [int] */
    /* JADX WARN: Type inference failed for: r0v2 */
    /* JADX WARN: Type inference failed for: r0v8 */
    /* JADX WARN: Type inference failed for: r0v9 */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    protected com.facebook.common.references.CloseableReference<android.graphics.Bitmap> decodeStaticImageFromStream(java.io.InputStream r9, android.graphics.BitmapFactory.Options r10, @javax.annotation.Nullable android.graphics.Rect r11) {
        /*
            r8 = this;
            com.facebook.common.internal.Preconditions.checkNotNull(r9)
            int r0 = r10.outWidth
            int r1 = r10.outHeight
            if (r11 == 0) goto L11
            int r0 = r11.width()
            int r1 = r11.height()
        L11:
            android.graphics.Bitmap$Config r2 = r10.inPreferredConfig
            int r2 = com.facebook.imageutils.BitmapUtil.getSizeInByteForBitmap(r0, r1, r2)
            com.facebook.imagepipeline.memory.BitmapPool r3 = r8.mBitmapPool
            java.lang.Object r2 = r3.get(r2)
            android.graphics.Bitmap r2 = (android.graphics.Bitmap) r2
            if (r2 == 0) goto Lbe
            r10.inBitmap = r2
            androidx.core.util.Pools$SynchronizedPool<java.nio.ByteBuffer> r3 = r8.mDecodeBuffers
            java.lang.Object r3 = r3.acquire()
            java.nio.ByteBuffer r3 = (java.nio.ByteBuffer) r3
            if (r3 != 0) goto L33
            r3 = 16384(0x4000, float:2.2959E-41)
            java.nio.ByteBuffer r3 = java.nio.ByteBuffer.allocate(r3)
        L33:
            byte[] r4 = r3.array()     // Catch: java.lang.Throwable -> L90 java.lang.RuntimeException -> L92 java.lang.IllegalArgumentException -> L99
            r10.inTempStorage = r4     // Catch: java.lang.Throwable -> L90 java.lang.RuntimeException -> L92 java.lang.IllegalArgumentException -> L99
            r4 = 0
            if (r11 == 0) goto L6d
            r5 = 1
            android.graphics.Bitmap$Config r6 = r10.inPreferredConfig     // Catch: java.lang.Throwable -> L50 java.io.IOException -> L53
            r2.reconfigure(r0, r1, r6)     // Catch: java.lang.Throwable -> L50 java.io.IOException -> L53
            android.graphics.BitmapRegionDecoder r0 = android.graphics.BitmapRegionDecoder.newInstance(r9, r5)     // Catch: java.lang.Throwable -> L50 java.io.IOException -> L53
            android.graphics.Bitmap r1 = r0.decodeRegion(r11, r10)     // Catch: java.io.IOException -> L54 java.lang.Throwable -> L66
            if (r0 == 0) goto L6e
            r0.recycle()     // Catch: java.lang.Throwable -> L90 java.lang.RuntimeException -> L92 java.lang.IllegalArgumentException -> L99
            goto L6e
        L50:
            r10 = move-exception
            r0 = r4
            goto L67
        L53:
            r0 = r4
        L54:
            java.lang.Class<?> r1 = com.facebook.imagepipeline.platform.ArtDecoder.TAG     // Catch: java.lang.Throwable -> L66
            java.lang.String r6 = "Could not decode region %s, decoding full bitmap instead."
            java.lang.Object[] r5 = new java.lang.Object[r5]     // Catch: java.lang.Throwable -> L66
            r7 = 0
            r5[r7] = r11     // Catch: java.lang.Throwable -> L66
            com.facebook.common.logging.FLog.e(r1, r6, r5)     // Catch: java.lang.Throwable -> L66
            if (r0 == 0) goto L6d
            r0.recycle()     // Catch: java.lang.Throwable -> L90 java.lang.RuntimeException -> L92 java.lang.IllegalArgumentException -> L99
            goto L6d
        L66:
            r10 = move-exception
        L67:
            if (r0 == 0) goto L6c
            r0.recycle()     // Catch: java.lang.Throwable -> L90 java.lang.RuntimeException -> L92 java.lang.IllegalArgumentException -> L99
        L6c:
            throw r10     // Catch: java.lang.Throwable -> L90 java.lang.RuntimeException -> L92 java.lang.IllegalArgumentException -> L99
        L6d:
            r1 = r4
        L6e:
            if (r1 != 0) goto L74
            android.graphics.Bitmap r1 = android.graphics.BitmapFactory.decodeStream(r9, r4, r10)     // Catch: java.lang.Throwable -> L90 java.lang.RuntimeException -> L92 java.lang.IllegalArgumentException -> L99
        L74:
            androidx.core.util.Pools$SynchronizedPool<java.nio.ByteBuffer> r9 = r8.mDecodeBuffers
            r9.release(r3)
            if (r2 != r1) goto L82
            com.facebook.imagepipeline.memory.BitmapPool r8 = r8.mBitmapPool
            com.facebook.common.references.CloseableReference r8 = com.facebook.common.references.CloseableReference.of(r1, r8)
            return r8
        L82:
            com.facebook.imagepipeline.memory.BitmapPool r8 = r8.mBitmapPool
            r8.release(r2)
            r1.recycle()
            java.lang.IllegalStateException r8 = new java.lang.IllegalStateException
            r8.<init>()
            throw r8
        L90:
            r9 = move-exception
            goto Lb8
        L92:
            r9 = move-exception
            com.facebook.imagepipeline.memory.BitmapPool r10 = r8.mBitmapPool     // Catch: java.lang.Throwable -> L90
            r10.release(r2)     // Catch: java.lang.Throwable -> L90
            throw r9     // Catch: java.lang.Throwable -> L90
        L99:
            r10 = move-exception
            com.facebook.imagepipeline.memory.BitmapPool r11 = r8.mBitmapPool     // Catch: java.lang.Throwable -> L90
            r11.release(r2)     // Catch: java.lang.Throwable -> L90
            r9.reset()     // Catch: java.lang.Throwable -> L90 java.io.IOException -> Lb7
            android.graphics.Bitmap r9 = android.graphics.BitmapFactory.decodeStream(r9)     // Catch: java.lang.Throwable -> L90 java.io.IOException -> Lb7
            if (r9 == 0) goto Lb6
            com.facebook.imagepipeline.bitmaps.SimpleBitmapReleaser r11 = com.facebook.imagepipeline.bitmaps.SimpleBitmapReleaser.getInstance()     // Catch: java.lang.Throwable -> L90 java.io.IOException -> Lb7
            com.facebook.common.references.CloseableReference r9 = com.facebook.common.references.CloseableReference.of(r9, r11)     // Catch: java.lang.Throwable -> L90 java.io.IOException -> Lb7
            androidx.core.util.Pools$SynchronizedPool<java.nio.ByteBuffer> r8 = r8.mDecodeBuffers
            r8.release(r3)
            return r9
        Lb6:
            throw r10     // Catch: java.lang.Throwable -> L90 java.io.IOException -> Lb7
        Lb7:
            throw r10     // Catch: java.lang.Throwable -> L90
        Lb8:
            androidx.core.util.Pools$SynchronizedPool<java.nio.ByteBuffer> r8 = r8.mDecodeBuffers
            r8.release(r3)
            throw r9
        Lbe:
            java.lang.NullPointerException r8 = new java.lang.NullPointerException
            java.lang.String r9 = "BitmapPool.get returned null"
            r8.<init>(r9)
            throw r8
        */
        throw new UnsupportedOperationException("Method not decompiled: com.facebook.imagepipeline.platform.ArtDecoder.decodeStaticImageFromStream(java.io.InputStream, android.graphics.BitmapFactory$Options, android.graphics.Rect):com.facebook.common.references.CloseableReference");
    }

    private static BitmapFactory.Options getDecodeOptionsForStream(EncodedImage encodedImage, Bitmap.Config config) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inSampleSize = encodedImage.getSampleSize();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeStream(encodedImage.getInputStream(), null, options);
        if (options.outWidth == -1 || options.outHeight == -1) {
            throw new IllegalArgumentException();
        }
        options.inJustDecodeBounds = false;
        options.inDither = true;
        options.inPreferredConfig = config;
        options.inMutable = true;
        return options;
    }
}
