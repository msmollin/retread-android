package com.bambuser.broadcaster;

import android.util.Log;
import java.nio.ByteBuffer;

/* loaded from: classes.dex */
class Mp4Muxer {
    private static final String LOGTAG = "Mp4Muxer";
    private long privData = 0;

    /* JADX INFO: Access modifiers changed from: package-private */
    public native void addAudioTrack(int i, int i2);

    /* JADX INFO: Access modifiers changed from: package-private */
    public native void addVideoTrack(int i, int i2, int i3);

    /* JADX INFO: Access modifiers changed from: package-private */
    public native void close();

    native long getCurFileSize();

    /* JADX INFO: Access modifiers changed from: package-private */
    public native long getFileDuration();

    native long getTotalPayloadSize();

    native long getUnwrittenSize();

    native void init();

    /* JADX INFO: Access modifiers changed from: package-private */
    public native void setAudioExtradata(ByteBuffer byteBuffer);

    /* JADX INFO: Access modifiers changed from: package-private */
    public native void setChunkDuration(int i);

    /* JADX INFO: Access modifiers changed from: package-private */
    public native void setCreator(String str);

    /* JADX INFO: Access modifiers changed from: package-private */
    public native void setFragmentDuration(int i);

    /* JADX INFO: Access modifiers changed from: package-private */
    public native void setGeoLocation(double d, double d2);

    /* JADX INFO: Access modifiers changed from: package-private */
    public native void setHevcVideoExtradata(ByteBuffer byteBuffer);

    /* JADX INFO: Access modifiers changed from: package-private */
    public native void setTitle(String str);

    /* JADX INFO: Access modifiers changed from: package-private */
    public native void setVideoExtradata(ByteBuffer byteBuffer);

    /* JADX INFO: Access modifiers changed from: package-private */
    public native boolean start(String str);

    /* JADX INFO: Access modifiers changed from: package-private */
    public native void writeAudioPacket(ByteBuffer byteBuffer, long j);

    /* JADX INFO: Access modifiers changed from: package-private */
    public native void writeVideoPacket(ByteBuffer byteBuffer, long j);

    static {
        try {
            Class.forName("com.bambuser.broadcaster.NativeUtils");
        } catch (ClassNotFoundException e) {
            Log.w(LOGTAG, "ClassNotFoundException", e);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public Mp4Muxer() {
        init();
    }

    protected void finalize() {
        close();
    }

    void addVideoTrack(int i, int i2) {
        addVideoTrack(i, i2, 0);
    }
}
