package com.bambuser.broadcaster;

import android.os.AsyncTask;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.util.Pair;
import android.util.SparseIntArray;
import com.bambuser.broadcaster.HlsVariant;
import com.github.mikephil.charting.utils.Utils;
import com.google.android.gms.common.ConnectionResult;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import org.eclipse.paho.client.mqttv3.MqttTopic;

/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: classes.dex */
public final class HlsParser {
    private static final String LOGTAG = "HlsParser";
    private static final int MSG_RELOAD_PLAYLIST = 1;
    private int mCurDownloadVariantIndex;
    private int mCurPlaybackVariantIndex;
    private volatile Observer mObserver;
    private PacketHandler mPacketHandler;
    private boolean mSegmentDownloaded;
    private final Runnable mTriggerSegmentDownload = new Runnable() { // from class: com.bambuser.broadcaster.HlsParser.5
        @Override // java.lang.Runnable
        public void run() {
            HlsParser.this.downloadSegment();
        }
    };
    private final Runnable mTriggerPlaylistReload = new Runnable() { // from class: com.bambuser.broadcaster.HlsParser.6
        @Override // java.lang.Runnable
        public void run() {
            HlsParser.this.reloadPlaylist();
        }
    };
    private final Object mParserBlockingLock = new Object();
    private final Object mPlaybackWaitLock = new Object();
    private volatile boolean mInternalVariantSelection = true;
    private volatile boolean mRealtimeMode = false;
    private volatile boolean mLargeBufferMode = false;
    private volatile boolean mCaptureTimePairing = false;
    private volatile double mStreamParsePosition = -1.0d;
    private int mSegmentErrorCount = 0;
    private int mPlaylistErrorCount = 0;
    private List<HlsVariant> mVariants = new ArrayList();
    private double mTotalDuration = Utils.DOUBLE_EPSILON;
    private volatile boolean mClosed = false;
    private volatile Thread mParserThread = null;
    private volatile boolean mStopPlayback = false;
    private volatile boolean mPlaybackPaused = false;
    private volatile boolean mEndOfData = false;
    private int mAwaitingMediaTimeForSegmentSeq = -1;
    private final SparseIntArray mSegmentStartTimes = new SparseIntArray();
    private volatile int mCurPlaySegmentSeq = -1;
    private final Handler mMainHandler = new Handler(Looper.getMainLooper());
    private MpegtsParser mParser = new MpegtsParser();

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes.dex */
    public interface Observer {
        void onDownloadedBytes(long j);

        void onError();
    }

    static /* synthetic */ int access$2508(HlsParser hlsParser) {
        int i = hlsParser.mSegmentErrorCount;
        hlsParser.mSegmentErrorCount = i + 1;
        return i;
    }

    static /* synthetic */ int access$708(HlsParser hlsParser) {
        int i = hlsParser.mPlaylistErrorCount;
        hlsParser.mPlaylistErrorCount = i + 1;
        return i;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static Map<String, Long> getBitrateMap(String str, String str2) {
        Long l;
        HashMap hashMap = new HashMap();
        for (HlsVariant hlsVariant : parseMainPlaylist(str, str2)) {
            if (hlsVariant.mResolution != null && hlsVariant.mBitrate > 0 && ((l = (Long) hashMap.get(hlsVariant.mResolution)) == null || l.longValue() < hlsVariant.mBitrate)) {
                hashMap.put(hlsVariant.mResolution, Long.valueOf(hlsVariant.mBitrate));
            }
        }
        return hashMap;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void setObserver(Observer observer) {
        this.mObserver = observer;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void setHandler(PacketHandler packetHandler) {
        PacketHandlerWrapper packetHandlerWrapper;
        synchronized (this.mParserBlockingLock) {
            if (packetHandler != null) {
                try {
                    packetHandlerWrapper = new PacketHandlerWrapper(packetHandler);
                } catch (Throwable th) {
                    throw th;
                }
            } else {
                packetHandlerWrapper = null;
            }
            this.mPacketHandler = packetHandlerWrapper;
            if (this.mParser != null) {
                this.mParser.setHandler(this.mPacketHandler);
            }
        }
        if (this.mPacketHandler == null || this.mTotalDuration <= Utils.DOUBLE_EPSILON) {
            return;
        }
        this.mPacketHandler.onStreamDuration(this.mTotalDuration);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void close() {
        this.mClosed = true;
        this.mStopPlayback = true;
        synchronized (this.mParserBlockingLock) {
            if (this.mParser != null) {
                this.mParser.flush();
                this.mParser.setHandler(null);
                this.mParser.close();
            }
            this.mParser = null;
            this.mParserThread = null;
        }
        this.mPacketHandler = null;
        for (HlsVariant hlsVariant : this.mVariants) {
            hlsVariant.close();
        }
        this.mVariants.clear();
        this.mObserver = null;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void setRealtimeMode(boolean z) {
        this.mRealtimeMode = z;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void setLargeBufferMode(boolean z) {
        this.mLargeBufferMode = z;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void setInternalVariantSelection(boolean z) {
        this.mInternalVariantSelection = z;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean getInternalVariantSelection() {
        return this.mInternalVariantSelection;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void setCaptureTimePairing(boolean z) {
        this.mCaptureTimePairing = z;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* JADX WARN: Type inference failed for: r0v0, types: [com.bambuser.broadcaster.HlsParser$1] */
    public void playUrl(final String str) {
        new AsyncTask<Void, Void, List<HlsVariant>>() { // from class: com.bambuser.broadcaster.HlsParser.1
            /* JADX INFO: Access modifiers changed from: protected */
            @Override // android.os.AsyncTask
            public List<HlsVariant> doInBackground(Void... voidArr) {
                Pair<Integer, String> pair = BackendApi.get(str);
                if (((Integer) pair.first).intValue() == 200 && pair.second != null) {
                    return HlsParser.parseMainPlaylist(str, (String) pair.second);
                }
                return new ArrayList();
            }

            /* JADX INFO: Access modifiers changed from: protected */
            @Override // android.os.AsyncTask
            public void onPostExecute(List<HlsVariant> list) {
                if (HlsParser.this.mClosed) {
                    return;
                }
                HlsParser.this.mVariants = list;
                if (!HlsParser.this.mVariants.isEmpty()) {
                    HlsParser.this.reloadPlaylist();
                    HlsParser.this.mParserThread = new Thread("ParserThread") { // from class: com.bambuser.broadcaster.HlsParser.1.1
                        @Override // java.lang.Thread, java.lang.Runnable
                        public void run() {
                            HlsParser.this.playSegmentData();
                        }
                    };
                    HlsParser.this.mParserThread.start();
                    return;
                }
                Log.w(HlsParser.LOGTAG, "Failed to load main playlist url: " + str);
                if (HlsParser.this.mObserver != null) {
                    HlsParser.this.mObserver.onError();
                }
            }
        }.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, new Void[0]);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void stopPlayback() {
        if (this.mClosed) {
            return;
        }
        this.mStopPlayback = true;
        HlsVariant hlsVariant = this.mVariants.get(this.mCurPlaybackVariantIndex);
        synchronized (hlsVariant) {
            hlsVariant.notifyAll();
        }
        synchronized (this.mPlaybackWaitLock) {
            this.mPlaybackWaitLock.notifyAll();
        }
        while (this.mParserThread != null) {
            try {
                this.mParserThread.join();
                this.mParserThread = null;
            } catch (InterruptedException unused) {
            }
        }
        if (this.mParser != null) {
            this.mParser.close();
        }
        this.mParser = null;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void pausePlayback() {
        if (this.mClosed) {
            return;
        }
        synchronized (this.mParserBlockingLock) {
            this.mPlaybackPaused = true;
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void unpausePlayback() {
        if (this.mClosed) {
            return;
        }
        synchronized (this.mParserBlockingLock) {
            this.mPlaybackPaused = false;
            this.mParserBlockingLock.notifyAll();
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void seekTo(long j) {
        if (this.mClosed) {
            return;
        }
        this.mEndOfData = false;
        this.mStopPlayback = false;
        this.mParser = new MpegtsParser();
        this.mParser.setHandler(this.mPacketHandler);
        double d = j / 1000.0d;
        HlsVariant hlsVariant = this.mVariants.get(this.mCurPlaybackVariantIndex);
        int i = 0;
        for (int i2 = 0; i2 < hlsVariant.mSegments.size(); i2++) {
            HlsSegment hlsSegment = hlsVariant.mSegments.get(i2);
            if (hlsSegment.mStartTime <= d) {
                i = i2;
            }
            if (hlsSegment.mStartTime + hlsSegment.mDuration > d) {
                break;
            }
        }
        this.mCurPlaySegmentSeq = hlsVariant.mFirstSegmentSeq + i;
        this.mVariants.get(this.mCurDownloadVariantIndex).mCurDownloadSegmentSeq = this.mCurPlaySegmentSeq;
        downloadSegment();
        this.mParserThread = new Thread("ParserThread") { // from class: com.bambuser.broadcaster.HlsParser.2
            @Override // java.lang.Thread, java.lang.Runnable
            public void run() {
                HlsParser.this.playSegmentData();
            }
        };
        this.mParserThread.start();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public List<HlsVariant> getVariants() {
        return this.mVariants;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public int getDownloadVariantIndex() {
        return this.mCurDownloadVariantIndex;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void setDownloadVariantIndex(int i) {
        if (this.mInternalVariantSelection) {
            Log.w(LOGTAG, "setDownloadVariantIndex ignored, internal variant selection active");
        } else if (i < 0 || i >= this.mVariants.size()) {
            Log.w(LOGTAG, "setDownloadVariantIndex out of bounds, index ignored");
        } else {
            if (i != this.mCurDownloadVariantIndex) {
                this.mVariants.get(i).mCurDownloadSegmentSeq = this.mVariants.get(this.mCurDownloadVariantIndex).mCurDownloadSegmentSeq + 1;
            }
            this.mCurDownloadVariantIndex = i;
            synchronized (this.mPlaybackWaitLock) {
                this.mPlaybackWaitLock.notifyAll();
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean isEndOfData() {
        return this.mEndOfData;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static List<HlsVariant> parseMainPlaylist(String str, String str2) {
        long j;
        String[] split = str2.split("\\r?\\n");
        ArrayList arrayList = new ArrayList();
        String str3 = null;
        long j2 = 0;
        boolean z = false;
        for (String str4 : split) {
            if (str4.startsWith("#EXT-X-STREAM-INF:")) {
                int indexOf = str4.indexOf("BANDWIDTH=");
                if (indexOf >= 0) {
                    try {
                        j = Integer.parseInt(str4.substring(indexOf + "BANDWIDTH=".length()).replaceFirst("^(\\d*).*$", "$1"));
                    } catch (NumberFormatException unused) {
                        j = 0;
                    }
                } else {
                    j = j2;
                }
                int indexOf2 = str4.indexOf("RESOLUTION=");
                if (indexOf2 >= 0) {
                    str3 = str4.substring(indexOf2 + "RESOLUTION=".length()).replaceFirst("^(\\d+[Xx]\\d+).*$", "$1").toLowerCase(Locale.US);
                }
                long j3 = j;
                z = true;
                j2 = j3;
            } else if (!str4.startsWith(MqttTopic.MULTI_LEVEL_WILDCARD) && str4.length() > 0) {
                if (!z) {
                    arrayList.add(new HlsVariant(str, 0));
                    return arrayList;
                }
                HlsVariant hlsVariant = new HlsVariant();
                hlsVariant.mBitrate = j2;
                hlsVariant.mResolution = str3;
                try {
                    hlsVariant.mUrl = new URL(new URL(str), str4).toString();
                } catch (MalformedURLException unused2) {
                }
                arrayList.add(hlsVariant);
                str3 = null;
                j2 = 0;
            }
        }
        return arrayList;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void reloadPlaylist() {
        if (this.mClosed || this.mEndOfData) {
            return;
        }
        final HlsVariant hlsVariant = this.mVariants.get(this.mCurDownloadVariantIndex);
        hlsVariant.reload(new HlsVariant.PlaylistCallback() { // from class: com.bambuser.broadcaster.HlsParser.3
            @Override // com.bambuser.broadcaster.HlsVariant.PlaylistCallback
            public void onDownloadedBytes(long j) {
                Observer observer = HlsParser.this.mObserver;
                if (observer != null) {
                    observer.onDownloadedBytes(j);
                }
            }

            @Override // com.bambuser.broadcaster.HlsVariant.PlaylistCallback
            public void onLoaded() {
                if (HlsParser.this.mClosed) {
                    return;
                }
                HlsParser.this.mPlaylistErrorCount = 0;
                if (!hlsVariant.mSegments.isEmpty()) {
                    if (HlsParser.this.mCurPlaySegmentSeq < 0) {
                        HlsParser.this.mCurPlaySegmentSeq = hlsVariant.getBestStartupSegmentSeq(HlsParser.this.mRealtimeMode, HlsParser.this.mLargeBufferMode);
                    }
                    if (HlsParser.this.mCurPlaySegmentSeq < hlsVariant.mFirstSegmentSeq) {
                        HlsParser.this.mCurPlaySegmentSeq = hlsVariant.mFirstSegmentSeq;
                    }
                    if (HlsParser.this.mRealtimeMode && !hlsVariant.mSegments.isEmpty() && !hlsVariant.mFinished) {
                        int bestStartupSegmentSeq = hlsVariant.getBestStartupSegmentSeq(HlsParser.this.mRealtimeMode, false);
                        int i = 2;
                        if (HlsParser.this.mLargeBufferMode && hlsVariant.mTargetSegmentDuration > 0) {
                            i = Math.max(2, 10 / hlsVariant.mTargetSegmentDuration);
                        }
                        if (HlsParser.this.mLargeBufferMode && i < 3) {
                            i++;
                        }
                        if (HlsParser.this.mCurPlaySegmentSeq <= bestStartupSegmentSeq - i) {
                            Log.w(HlsParser.LOGTAG, "playback falling behind, updating play segment seq from " + HlsParser.this.mCurPlaySegmentSeq + " to " + bestStartupSegmentSeq + ", large skipahead");
                            HlsParser.this.mCurPlaySegmentSeq = bestStartupSegmentSeq;
                            ((HlsVariant) HlsParser.this.mVariants.get(HlsParser.this.mCurDownloadVariantIndex)).mCurDownloadSegmentSeq = HlsParser.this.mCurPlaySegmentSeq;
                            synchronized (HlsParser.this.mPlaybackWaitLock) {
                                HlsParser.this.mPlaybackWaitLock.notifyAll();
                            }
                            HlsVariant hlsVariant2 = (HlsVariant) HlsParser.this.mVariants.get(HlsParser.this.mCurPlaybackVariantIndex);
                            synchronized (hlsVariant2) {
                                hlsVariant2.notifyAll();
                            }
                        }
                    }
                    if (HlsParser.this.mCaptureTimePairing) {
                        HlsParser.this.getLatestCaptureTime(hlsVariant);
                        HlsParser.this.pruneSegmentStartTimes(hlsVariant.mFirstSegmentSeq);
                    }
                }
                if (hlsVariant.mTotalDuration > HlsParser.this.mTotalDuration) {
                    HlsParser.this.mTotalDuration = hlsVariant.mTotalDuration;
                    if (HlsParser.this.mPacketHandler != null) {
                        HlsParser.this.mPacketHandler.onStreamDuration(HlsParser.this.mTotalDuration);
                    }
                }
                HlsParser.this.downloadSegment();
            }

            @Override // com.bambuser.broadcaster.HlsVariant.PlaylistCallback
            public void onError() {
                if (HlsParser.this.mClosed) {
                    return;
                }
                HlsParser.access$708(HlsParser.this);
                if (HlsParser.this.mPlaylistErrorCount < 3) {
                    if (HlsParser.this.mMainHandler.hasMessages(1)) {
                        return;
                    }
                    Message obtain = Message.obtain(HlsParser.this.mMainHandler, HlsParser.this.mTriggerPlaylistReload);
                    obtain.what = 1;
                    HlsParser.this.mMainHandler.sendMessageDelayed(obtain, 1000L);
                    return;
                }
                Log.w(HlsParser.LOGTAG, "Failed repeatedly to load a playlist from url: " + hlsVariant.mUrl);
                if (HlsParser.this.mObserver != null) {
                    HlsParser.this.mObserver.onError();
                }
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* JADX WARN: Code restructure failed: missing block: B:46:0x0082, code lost:
        r4 = r12.mParserBlockingLock;
     */
    /* JADX WARN: Code restructure failed: missing block: B:47:0x0084, code lost:
        monitor-enter(r4);
     */
    /* JADX WARN: Code restructure failed: missing block: B:49:0x0087, code lost:
        if (r12.mStopPlayback != false) goto L92;
     */
    /* JADX WARN: Code restructure failed: missing block: B:51:0x008b, code lost:
        if (r12.mPlaybackPaused == false) goto L81;
     */
    /* JADX WARN: Code restructure failed: missing block: B:52:0x008d, code lost:
        r12.mParserBlockingLock.wait();
     */
    /* JADX WARN: Code restructure failed: missing block: B:55:0x0095, code lost:
        if (r12.mStopPlayback != false) goto L89;
     */
    /* JADX WARN: Code restructure failed: missing block: B:57:0x0099, code lost:
        if (r12.mParser == null) goto L89;
     */
    /* JADX WARN: Code restructure failed: missing block: B:58:0x009b, code lost:
        if (r5 != false) goto L88;
     */
    /* JADX WARN: Code restructure failed: missing block: B:59:0x009d, code lost:
        r12.mAwaitingMediaTimeForSegmentSeq = r2;
        r12.mStreamParsePosition = r6;
        pruneBufferedData(r6, r8);
        r5 = true;
     */
    /* JADX WARN: Code restructure failed: missing block: B:60:0x00a5, code lost:
        r12.mParser.handleData(r10.array(), r10.arrayOffset() + r10.position(), r10.remaining());
     */
    /* JADX WARN: Code restructure failed: missing block: B:61:0x00bb, code lost:
        monitor-exit(r4);
     */
    /* JADX WARN: Code restructure failed: missing block: B:62:0x00bc, code lost:
        r3 = r3 + 1;
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public void playSegmentData() {
        /*
            Method dump skipped, instructions count: 257
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: com.bambuser.broadcaster.HlsParser.playSegmentData():void");
    }

    /* loaded from: classes.dex */
    private final class PacketHandlerWrapper implements PacketHandler {
        private final PacketHandler mDelegate;

        PacketHandlerWrapper(PacketHandler packetHandler) {
            this.mDelegate = packetHandler;
        }

        @Override // com.bambuser.broadcaster.PacketHandler
        public void onStreamParsePosition(double d, int i) {
            this.mDelegate.onStreamParsePosition(d, i);
        }

        @Override // com.bambuser.broadcaster.PacketHandler
        public void onStreamDuration(double d) {
            this.mDelegate.onStreamDuration(d);
        }

        @Override // com.bambuser.broadcaster.PacketHandler
        public void onVideoHeader(ByteBuffer byteBuffer, int i, int i2, int i3, int i4) {
            this.mDelegate.onVideoHeader(byteBuffer, i, i2, i3, i4);
        }

        @Override // com.bambuser.broadcaster.PacketHandler
        public void onVideoFrame(ByteBuffer byteBuffer, int i, int i2, boolean z) {
            onMediaTimestamp(i2);
            this.mDelegate.onVideoFrame(byteBuffer, i, i2, z);
        }

        @Override // com.bambuser.broadcaster.PacketHandler
        public void onAudioHeader(ByteBuffer byteBuffer, int i, int i2, int i3) {
            this.mDelegate.onAudioHeader(byteBuffer, i, i2, i3);
        }

        @Override // com.bambuser.broadcaster.PacketHandler
        public void onAudioFrame(ByteBuffer byteBuffer, int i) {
            this.mDelegate.onAudioFrame(byteBuffer, i);
        }

        @Override // com.bambuser.broadcaster.PacketHandler
        public void onRealtimePacket(long j, int i, int i2) {
            this.mDelegate.onRealtimePacket(j, i, i2);
        }

        @Override // com.bambuser.broadcaster.PacketHandler
        public void onId3String(String str, int i) {
            this.mDelegate.onId3String(str, i);
        }

        @Override // com.bambuser.broadcaster.PacketHandler
        public void endOfData() {
            this.mDelegate.endOfData();
        }

        private void onMediaTimestamp(final int i) {
            if (HlsParser.this.mAwaitingMediaTimeForSegmentSeq < 0) {
                return;
            }
            final int i2 = HlsParser.this.mAwaitingMediaTimeForSegmentSeq;
            if (HlsParser.this.mCaptureTimePairing) {
                HlsParser.this.mMainHandler.post(new Runnable() { // from class: com.bambuser.broadcaster.HlsParser.PacketHandlerWrapper.1
                    @Override // java.lang.Runnable
                    public void run() {
                        HlsParser.this.mSegmentStartTimes.put(i2, i);
                    }
                });
            }
            if (HlsParser.this.mStreamParsePosition >= Utils.DOUBLE_EPSILON) {
                this.mDelegate.onStreamParsePosition(HlsParser.this.mStreamParsePosition, i);
                HlsParser.this.mStreamParsePosition = -1.0d;
            }
            HlsParser.this.mAwaitingMediaTimeForSegmentSeq = -1;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void getLatestCaptureTime(HlsVariant hlsVariant) {
        int lastSegmentSeq = hlsVariant.getLastSegmentSeq();
        int max = Math.max(hlsVariant.mFirstSegmentSeq, lastSegmentSeq - 10);
        while (lastSegmentSeq >= max) {
            HlsSegment segment = hlsVariant.getSegment(lastSegmentSeq);
            if (segment.mUncertaintyParsed) {
                int i = this.mSegmentStartTimes.get(lastSegmentSeq, -1);
                boolean z = segment.mCaptureTimeUncertainty < 0 || segment.mCaptureTime > 0;
                if (this.mPacketHandler != null && z && i >= 0) {
                    this.mPacketHandler.onRealtimePacket(segment.mCaptureTime, segment.mCaptureTimeUncertainty, i);
                }
                if (i >= 0) {
                    return;
                }
            }
            lastSegmentSeq--;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void pruneSegmentStartTimes(int i) {
        int i2 = 0;
        while (i2 < this.mSegmentStartTimes.size()) {
            if (this.mSegmentStartTimes.keyAt(i2) < i) {
                this.mSegmentStartTimes.removeAt(i2);
            } else {
                i2++;
            }
        }
    }

    private void pruneBufferedData(final double d, final double d2) {
        this.mMainHandler.post(new Runnable() { // from class: com.bambuser.broadcaster.HlsParser.4
            @Override // java.lang.Runnable
            public void run() {
                double d3 = d - 5.0d;
                double max = d + Math.max(d2 + 1.0d, 60.0d);
                for (HlsVariant hlsVariant : HlsParser.this.mVariants) {
                    hlsVariant.dropDataOutsideInterval(d3, max);
                }
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void downloadSegment() {
        if (this.mClosed || this.mEndOfData) {
            return;
        }
        final HlsVariant hlsVariant = this.mVariants.get(this.mCurDownloadVariantIndex);
        if (hlsVariant.isFetching()) {
            return;
        }
        if (!hlsVariant.mSegments.isEmpty()) {
            if (this.mCurPlaySegmentSeq < hlsVariant.mFirstSegmentSeq) {
                this.mCurPlaySegmentSeq = hlsVariant.mFirstSegmentSeq;
            }
            if (hlsVariant.mCurDownloadSegmentSeq < this.mCurPlaySegmentSeq) {
                hlsVariant.mCurDownloadSegmentSeq = this.mCurPlaySegmentSeq;
            }
            if (hlsVariant.mCurDownloadSegmentSeq > this.mCurPlaySegmentSeq + 2) {
                return;
            }
        }
        if (this.mSegmentErrorCount <= 0 || !(hlsVariant.isReloading() || this.mMainHandler.hasMessages(1))) {
            final int max = Math.max((int) ConnectionResult.DRIVE_EXTERNAL_STORAGE_REQUIRED, (hlsVariant.mTargetSegmentDuration * 1000) / 2);
            boolean z = !hlsVariant.isValidSeq(hlsVariant.mCurDownloadSegmentSeq);
            if (z && hlsVariant.isReloading()) {
                return;
            }
            if (hlsVariant.mFinished) {
                if (z) {
                    this.mEndOfData = true;
                    synchronized (hlsVariant) {
                        hlsVariant.notifyAll();
                    }
                    synchronized (this.mPlaybackWaitLock) {
                        this.mPlaybackWaitLock.notifyAll();
                    }
                    return;
                }
            } else if (this.mSegmentDownloaded && z) {
                this.mMainHandler.removeMessages(1);
                reloadPlaylist();
            } else if (!this.mMainHandler.hasMessages(1)) {
                Message obtain = Message.obtain(this.mMainHandler, this.mTriggerPlaylistReload);
                obtain.what = 1;
                this.mMainHandler.sendMessageDelayed(obtain, max);
            }
            if (z) {
                this.mSegmentDownloaded = false;
                return;
            }
            this.mSegmentDownloaded = true;
            hlsVariant.downloadSegment(new HlsVariant.SegmentCallback() { // from class: com.bambuser.broadcaster.HlsParser.7
                @Override // com.bambuser.broadcaster.HlsVariant.SegmentCallback
                public void onDownloadedBytes(long j) {
                    Observer observer = HlsParser.this.mObserver;
                    if (observer != null) {
                        observer.onDownloadedBytes(j);
                    }
                    synchronized (HlsParser.this.mPlaybackWaitLock) {
                        HlsParser.this.mPlaybackWaitLock.notifyAll();
                    }
                }

                @Override // com.bambuser.broadcaster.HlsVariant.SegmentCallback
                public void onLoaded(int i, boolean z2) {
                    if (HlsParser.this.mClosed) {
                        return;
                    }
                    if (z2) {
                        HlsParser.this.mSegmentErrorCount = 0;
                    } else {
                        HlsParser.access$2508(HlsParser.this);
                        if (HlsParser.this.mSegmentErrorCount >= 3) {
                            Log.w(HlsParser.LOGTAG, "Segment download failed repeatedly, giving up");
                            if (HlsParser.this.mObserver != null) {
                                HlsParser.this.mObserver.onError();
                                return;
                            }
                            return;
                        }
                    }
                    if (z2 && hlsVariant.mCurDownloadSegmentSeq == i) {
                        HlsParser.this.updateCurrentDownloadVariant(i);
                        ((HlsVariant) HlsParser.this.mVariants.get(HlsParser.this.mCurDownloadVariantIndex)).mCurDownloadSegmentSeq = i + 1;
                    }
                    if (!z2) {
                        if (HlsParser.this.mSegmentErrorCount == 1) {
                            HlsParser.this.mMainHandler.removeMessages(1);
                            HlsParser.this.reloadPlaylist();
                            return;
                        } else if (HlsParser.this.mMainHandler.hasMessages(1)) {
                            return;
                        } else {
                            Message obtain2 = Message.obtain(HlsParser.this.mMainHandler, HlsParser.this.mTriggerPlaylistReload);
                            obtain2.what = 1;
                            HlsParser.this.mMainHandler.sendMessageDelayed(obtain2, max);
                            return;
                        }
                    }
                    HlsParser.this.downloadSegment();
                }
            });
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void updateCurrentDownloadVariant(int i) {
        if (this.mInternalVariantSelection && this.mCurDownloadVariantIndex < this.mVariants.size()) {
            HlsVariant hlsVariant = this.mVariants.get(this.mCurDownloadVariantIndex);
            if (hlsVariant.isValidSeq(i)) {
                HlsSegment segment = hlsVariant.getSegment(i);
                if (segment.mDownloadComplete) {
                    long j = -1;
                    int i2 = -1;
                    for (int i3 = 0; i3 < this.mVariants.size(); i3++) {
                        HlsVariant hlsVariant2 = this.mVariants.get(i3);
                        if (hlsVariant2.mBitrate < segment.mDownloadBitrate && hlsVariant2.mBitrate > j) {
                            j = hlsVariant2.mBitrate;
                            i2 = i3;
                        }
                    }
                    if (i2 < 0) {
                        for (int i4 = 0; i4 < this.mVariants.size(); i4++) {
                            HlsVariant hlsVariant3 = this.mVariants.get(i4);
                            if (j < 0 || hlsVariant3.mBitrate < j) {
                                j = hlsVariant3.mBitrate;
                                i2 = i4;
                            }
                        }
                    }
                    this.mCurDownloadVariantIndex = i2;
                }
            }
        }
    }

    private boolean updateCurrentPlaybackVariant() {
        long j = -1;
        int i = -1;
        for (int i2 = 0; i2 < this.mVariants.size(); i2++) {
            HlsVariant hlsVariant = this.mVariants.get(i2);
            if (hlsVariant.mBitrate >= j) {
                synchronized (hlsVariant) {
                    if (hlsVariant.isPlayableSeq(this.mCurPlaySegmentSeq)) {
                        j = hlsVariant.mBitrate;
                        i = i2;
                    }
                }
            }
        }
        if (i >= 0) {
            this.mCurPlaybackVariantIndex = i;
            return true;
        }
        return false;
    }
}
