package com.bambuser.broadcaster;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Iterator;

/* loaded from: classes.dex */
final class HlsSegment {
    final boolean mBambuserNext;
    final float mDuration;
    double mStartTime;
    final String mUrl;
    ArrayList<ByteBuffer> mSegmentData = new ArrayList<>();
    Object mDownloaderTask = null;
    boolean mDownloadComplete = false;
    long mDownloadBitrate = 0;
    long mCaptureTime = -1;
    int mCaptureTimeUncertainty = -1;
    boolean mUncertaintyParsed = false;

    /* JADX INFO: Access modifiers changed from: package-private */
    public HlsSegment(float f, String str, boolean z) {
        this.mDuration = f;
        this.mUrl = str;
        this.mBambuserNext = z;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean isDownloading() {
        return this.mDownloaderTask != null;
    }

    long getSegmentDataSize() {
        Iterator<ByteBuffer> it = this.mSegmentData.iterator();
        long j = 0;
        while (it.hasNext()) {
            j += it.next().remaining();
        }
        return j;
    }
}
