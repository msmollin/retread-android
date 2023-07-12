package com.bambuser.broadcaster;

import java.nio.ByteBuffer;

/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: classes.dex */
public interface PacketHandler {
    void endOfData();

    void onAudioFrame(ByteBuffer byteBuffer, int i);

    void onAudioHeader(ByteBuffer byteBuffer, int i, int i2, int i3);

    void onId3String(String str, int i);

    void onRealtimePacket(long j, int i, int i2);

    void onStreamDuration(double d);

    void onStreamParsePosition(double d, int i);

    void onVideoFrame(ByteBuffer byteBuffer, int i, int i2, boolean z);

    void onVideoHeader(ByteBuffer byteBuffer, int i, int i2, int i3, int i4);
}
