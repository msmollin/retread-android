package com.treadly.Treadly.UI.Util;

import android.graphics.Bitmap;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;

/* loaded from: classes2.dex */
public class QrHelper {
    public static Bitmap textToImage(String str, int i, int i2) throws WriterException, NullPointerException {
        try {
            MultiFormatWriter multiFormatWriter = new MultiFormatWriter();
            BarcodeFormat barcodeFormat = BarcodeFormat.DATA_MATRIX;
            BitMatrix encode = multiFormatWriter.encode(str, BarcodeFormat.QR_CODE, i, i2, null);
            int width = encode.getWidth();
            int height = encode.getHeight();
            int[] iArr = new int[width * height];
            for (int i3 = 0; i3 < height; i3++) {
                int i4 = i3 * width;
                for (int i5 = 0; i5 < width; i5++) {
                    iArr[i4 + i5] = encode.get(i5, i3) ? -16777216 : -1;
                }
            }
            Bitmap createBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_4444);
            createBitmap.setPixels(iArr, 0, i, 0, 0, width, height);
            return createBitmap;
        } catch (IllegalArgumentException unused) {
            return null;
        }
    }
}
