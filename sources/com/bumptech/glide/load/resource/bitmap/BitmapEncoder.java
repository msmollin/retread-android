package com.bumptech.glide.load.resource.bitmap;

import android.graphics.Bitmap;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.bumptech.glide.load.EncodeStrategy;
import com.bumptech.glide.load.Option;
import com.bumptech.glide.load.Options;
import com.bumptech.glide.load.ResourceEncoder;
import com.bumptech.glide.load.engine.bitmap_recycle.ArrayPool;

/* loaded from: classes.dex */
public class BitmapEncoder implements ResourceEncoder<Bitmap> {
    private static final String TAG = "BitmapEncoder";
    @Nullable
    private final ArrayPool arrayPool;
    public static final Option<Integer> COMPRESSION_QUALITY = Option.memory("com.bumptech.glide.load.resource.bitmap.BitmapEncoder.CompressionQuality", 90);
    public static final Option<Bitmap.CompressFormat> COMPRESSION_FORMAT = Option.memory("com.bumptech.glide.load.resource.bitmap.BitmapEncoder.CompressionFormat");

    public BitmapEncoder(@NonNull ArrayPool arrayPool) {
        this.arrayPool = arrayPool;
    }

    @Deprecated
    public BitmapEncoder() {
        this.arrayPool = null;
    }

    /* JADX WARN: Code restructure failed: missing block: B:25:0x0068, code lost:
        if (r5 != null) goto L14;
     */
    @Override // com.bumptech.glide.load.Encoder
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public boolean encode(@androidx.annotation.NonNull com.bumptech.glide.load.engine.Resource<android.graphics.Bitmap> r8, @androidx.annotation.NonNull java.io.File r9, @androidx.annotation.NonNull com.bumptech.glide.load.Options r10) {
        /*
            r7 = this;
            java.lang.Object r8 = r8.get()
            android.graphics.Bitmap r8 = (android.graphics.Bitmap) r8
            android.graphics.Bitmap$CompressFormat r0 = r7.getFormat(r8, r10)
            java.lang.String r1 = "encode: [%dx%d] %s"
            int r2 = r8.getWidth()
            java.lang.Integer r2 = java.lang.Integer.valueOf(r2)
            int r3 = r8.getHeight()
            java.lang.Integer r3 = java.lang.Integer.valueOf(r3)
            com.bumptech.glide.util.pool.GlideTrace.beginSectionFormat(r1, r2, r3, r0)
            long r1 = com.bumptech.glide.util.LogTime.getLogTime()     // Catch: java.lang.Throwable -> Lc6
            com.bumptech.glide.load.Option<java.lang.Integer> r3 = com.bumptech.glide.load.resource.bitmap.BitmapEncoder.COMPRESSION_QUALITY     // Catch: java.lang.Throwable -> Lc6
            java.lang.Object r3 = r10.get(r3)     // Catch: java.lang.Throwable -> Lc6
            java.lang.Integer r3 = (java.lang.Integer) r3     // Catch: java.lang.Throwable -> Lc6
            int r3 = r3.intValue()     // Catch: java.lang.Throwable -> Lc6
            r4 = 0
            r5 = 0
            java.io.FileOutputStream r6 = new java.io.FileOutputStream     // Catch: java.lang.Throwable -> L55 java.io.IOException -> L57
            r6.<init>(r9)     // Catch: java.lang.Throwable -> L55 java.io.IOException -> L57
            com.bumptech.glide.load.engine.bitmap_recycle.ArrayPool r9 = r7.arrayPool     // Catch: java.lang.Throwable -> L4f java.io.IOException -> L52
            if (r9 == 0) goto L43
            com.bumptech.glide.load.data.BufferedOutputStream r9 = new com.bumptech.glide.load.data.BufferedOutputStream     // Catch: java.lang.Throwable -> L4f java.io.IOException -> L52
            com.bumptech.glide.load.engine.bitmap_recycle.ArrayPool r7 = r7.arrayPool     // Catch: java.lang.Throwable -> L4f java.io.IOException -> L52
            r9.<init>(r6, r7)     // Catch: java.lang.Throwable -> L4f java.io.IOException -> L52
            r5 = r9
            goto L44
        L43:
            r5 = r6
        L44:
            r8.compress(r0, r3, r5)     // Catch: java.lang.Throwable -> L55 java.io.IOException -> L57
            r5.close()     // Catch: java.lang.Throwable -> L55 java.io.IOException -> L57
            r4 = 1
        L4b:
            r5.close()     // Catch: java.io.IOException -> L6b java.lang.Throwable -> Lc6
            goto L6b
        L4f:
            r7 = move-exception
            r5 = r6
            goto Lc0
        L52:
            r7 = move-exception
            r5 = r6
            goto L58
        L55:
            r7 = move-exception
            goto Lc0
        L57:
            r7 = move-exception
        L58:
            java.lang.String r9 = "BitmapEncoder"
            r3 = 3
            boolean r9 = android.util.Log.isLoggable(r9, r3)     // Catch: java.lang.Throwable -> L55
            if (r9 == 0) goto L68
            java.lang.String r9 = "BitmapEncoder"
            java.lang.String r3 = "Failed to encode Bitmap"
            android.util.Log.d(r9, r3, r7)     // Catch: java.lang.Throwable -> L55
        L68:
            if (r5 == 0) goto L6b
            goto L4b
        L6b:
            java.lang.String r7 = "BitmapEncoder"
            r9 = 2
            boolean r7 = android.util.Log.isLoggable(r7, r9)     // Catch: java.lang.Throwable -> Lc6
            if (r7 == 0) goto Lbc
            java.lang.String r7 = "BitmapEncoder"
            java.lang.StringBuilder r9 = new java.lang.StringBuilder     // Catch: java.lang.Throwable -> Lc6
            r9.<init>()     // Catch: java.lang.Throwable -> Lc6
            java.lang.String r3 = "Compressed with type: "
            r9.append(r3)     // Catch: java.lang.Throwable -> Lc6
            r9.append(r0)     // Catch: java.lang.Throwable -> Lc6
            java.lang.String r0 = " of size "
            r9.append(r0)     // Catch: java.lang.Throwable -> Lc6
            int r0 = com.bumptech.glide.util.Util.getBitmapByteSize(r8)     // Catch: java.lang.Throwable -> Lc6
            r9.append(r0)     // Catch: java.lang.Throwable -> Lc6
            java.lang.String r0 = " in "
            r9.append(r0)     // Catch: java.lang.Throwable -> Lc6
            double r0 = com.bumptech.glide.util.LogTime.getElapsedMillis(r1)     // Catch: java.lang.Throwable -> Lc6
            r9.append(r0)     // Catch: java.lang.Throwable -> Lc6
            java.lang.String r0 = ", options format: "
            r9.append(r0)     // Catch: java.lang.Throwable -> Lc6
            com.bumptech.glide.load.Option<android.graphics.Bitmap$CompressFormat> r0 = com.bumptech.glide.load.resource.bitmap.BitmapEncoder.COMPRESSION_FORMAT     // Catch: java.lang.Throwable -> Lc6
            java.lang.Object r10 = r10.get(r0)     // Catch: java.lang.Throwable -> Lc6
            r9.append(r10)     // Catch: java.lang.Throwable -> Lc6
            java.lang.String r10 = ", hasAlpha: "
            r9.append(r10)     // Catch: java.lang.Throwable -> Lc6
            boolean r8 = r8.hasAlpha()     // Catch: java.lang.Throwable -> Lc6
            r9.append(r8)     // Catch: java.lang.Throwable -> Lc6
            java.lang.String r8 = r9.toString()     // Catch: java.lang.Throwable -> Lc6
            android.util.Log.v(r7, r8)     // Catch: java.lang.Throwable -> Lc6
        Lbc:
            com.bumptech.glide.util.pool.GlideTrace.endSection()
            return r4
        Lc0:
            if (r5 == 0) goto Lc5
            r5.close()     // Catch: java.io.IOException -> Lc5 java.lang.Throwable -> Lc6
        Lc5:
            throw r7     // Catch: java.lang.Throwable -> Lc6
        Lc6:
            r7 = move-exception
            com.bumptech.glide.util.pool.GlideTrace.endSection()
            throw r7
        */
        throw new UnsupportedOperationException("Method not decompiled: com.bumptech.glide.load.resource.bitmap.BitmapEncoder.encode(com.bumptech.glide.load.engine.Resource, java.io.File, com.bumptech.glide.load.Options):boolean");
    }

    private Bitmap.CompressFormat getFormat(Bitmap bitmap, Options options) {
        Bitmap.CompressFormat compressFormat = (Bitmap.CompressFormat) options.get(COMPRESSION_FORMAT);
        if (compressFormat != null) {
            return compressFormat;
        }
        if (bitmap.hasAlpha()) {
            return Bitmap.CompressFormat.PNG;
        }
        return Bitmap.CompressFormat.JPEG;
    }

    @Override // com.bumptech.glide.load.ResourceEncoder
    @NonNull
    public EncodeStrategy getEncodeStrategy(@NonNull Options options) {
        return EncodeStrategy.TRANSFORMED;
    }
}
