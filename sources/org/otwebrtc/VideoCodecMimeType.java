package org.otwebrtc;

/* loaded from: classes2.dex */
enum VideoCodecMimeType {
    VP8("video/x-vnd.on2.vp8"),
    VP9("video/x-vnd.on2.vp9"),
    H264("video/avc");
    
    private final String mimeType;

    VideoCodecMimeType(String str) {
        this.mimeType = str;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public String mimeType() {
        return this.mimeType;
    }
}
