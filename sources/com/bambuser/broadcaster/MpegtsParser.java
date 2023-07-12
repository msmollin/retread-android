package com.bambuser.broadcaster;

import android.util.Log;

/* loaded from: classes.dex */
final class MpegtsParser {
    private static final String LOGTAG = "MpegtsParser";
    private PacketHandler mHandler;
    private long privData = 0;

    /* JADX INFO: Access modifiers changed from: package-private */
    public native void close();

    /* JADX INFO: Access modifiers changed from: package-private */
    public native void flush();

    /* JADX INFO: Access modifiers changed from: package-private */
    public native void handleData(byte[] bArr, int i, int i2);

    native void init();

    static {
        try {
            Class.forName("com.bambuser.broadcaster.NativeUtils");
        } catch (ClassNotFoundException e) {
            Log.w(LOGTAG, "ClassNotFoundException", e);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public MpegtsParser() {
        init();
    }

    protected void finalize() {
        close();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void setHandler(PacketHandler packetHandler) {
        this.mHandler = packetHandler;
    }
}
