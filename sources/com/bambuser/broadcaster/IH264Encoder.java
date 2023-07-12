package com.bambuser.broadcaster;

import android.util.Log;
import com.bambuser.broadcaster.VideoEncoderBase;
import java.nio.ByteBuffer;

/* loaded from: classes.dex */
final class IH264Encoder extends VideoEncoderBase {
    private static final String LOGTAG = "IH264Encoder";
    private boolean mOnetimeSpsPps;
    private long privData = 0;

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // com.bambuser.broadcaster.VideoEncoderBase
    public native void close();

    native ByteBuffer encode(ByteBuffer[] byteBufferArr, int[] iArr, ByteBuffer byteBuffer);

    native ByteBuffer encodeSpsPps(ByteBuffer byteBuffer);

    native void init(int i, int i2, int i3, int i4, int i5);

    static {
        try {
            Class.forName("com.bambuser.broadcaster.NativeUtils");
        } catch (ClassNotFoundException e) {
            Log.w(LOGTAG, "ClassNotFoundException", e);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public IH264Encoder(int i, int i2, int i3, int i4, int i5) {
        this.mOnetimeSpsPps = false;
        init(i, i2, i3, i4, i5);
        this.mOnetimeSpsPps = true;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // com.bambuser.broadcaster.VideoEncoderBase
    public void encode(Frame frame, VideoEncoderBase.DataHandler dataHandler) {
        if (this.mOnetimeSpsPps) {
            dataHandler.mTempBuffer = encodeSpsPps(dataHandler.mTempBuffer);
            dataHandler.mTempBuffer.flip();
            dataHandler.onEncodedData(dataHandler.mTempBuffer, frame.mTimestamp, frame.mRotation, true);
            this.mOnetimeSpsPps = false;
            dataHandler.mTempBuffer.clear();
        }
        dataHandler.mTempBuffer = encode(frame.getBufferArray(), frame.getDetailArray(), dataHandler.mTempBuffer);
        dataHandler.mTempBuffer.flip();
        dataHandler.onEncodedData(dataHandler.mTempBuffer, frame.mTimestamp, frame.mRotation, false);
    }

    protected void finalize() {
        close();
    }
}
