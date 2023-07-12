package com.bambuser.broadcaster;

import android.util.Log;
import com.bambuser.broadcaster.VideoEncoderBase;
import java.nio.ByteBuffer;

/* loaded from: classes.dex */
final class JpegEncoder extends VideoEncoderBase {
    private static final String LOGTAG = "JpegEncoder";
    private long privData;

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // com.bambuser.broadcaster.VideoEncoderBase
    public native void close();

    /* JADX INFO: Access modifiers changed from: package-private */
    public native ByteBuffer encode(ByteBuffer[] byteBufferArr, int[] iArr, int i, int i2, ByteBuffer byteBuffer, boolean z);

    native void init(int i);

    native void setQuality(int i, ByteBuffer byteBuffer);

    /* JADX INFO: Access modifiers changed from: package-private */
    public native byte[] setQuality(int i);

    static {
        try {
            Class.forName("com.bambuser.broadcaster.NativeUtils");
        } catch (ClassNotFoundException e) {
            Log.w(LOGTAG, "ClassNotFoundException", e);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public JpegEncoder() {
        this(100);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public JpegEncoder(int i) {
        this.privData = 0L;
        init(i);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // com.bambuser.broadcaster.VideoEncoderBase
    public void encode(Frame frame, VideoEncoderBase.DataHandler dataHandler) {
        dataHandler.mTempBuffer = encode(frame.getBufferArray(), frame.getDetailArray(), frame.mWidth, frame.mHeight, dataHandler.mTempBuffer, true);
        dataHandler.mTempBuffer.flip();
        dataHandler.onEncodedData(dataHandler.mTempBuffer, frame.mTimestamp, frame.mRotation, false);
    }

    protected void finalize() {
        close();
    }
}
