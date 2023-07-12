package com.bambuser.broadcaster;

import android.os.AsyncTask;
import android.util.Log;
import android.util.Pair;
import com.github.mikephil.charting.utils.Utils;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import javax.net.ssl.HttpsURLConnection;
import org.eclipse.paho.client.mqttv3.MqttTopic;

/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: classes.dex */
public final class HlsVariant {
    private static final String LOGTAG = "HlsVariant";
    private static final int MAX_SEGMENT_BUFFERS = 100;
    long mBitrate;
    String mResolution;
    String mUrl;
    private final ByteBufferPool mDownloadPool = new ByteBufferPool(100);
    private volatile int mDownBufSize = 32768;
    int mTargetSegmentDuration = 0;
    double mTotalDuration = Utils.DOUBLE_EPSILON;
    double mSeekableDuration = Utils.DOUBLE_EPSILON;
    List<HlsSegment> mSegments = new ArrayList();
    int mFirstSegmentSeq = 0;
    boolean mFinished = false;
    private PlaylistLoadTask mPlaylistLoadTask = null;
    private SegmentFetchTask mSegmentFetcherTask = null;
    int mCurDownloadSegmentSeq = -1;

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes.dex */
    public interface PlaylistCallback {
        void onDownloadedBytes(long j);

        void onError();

        void onLoaded();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes.dex */
    public interface SegmentCallback {
        void onDownloadedBytes(long j);

        void onLoaded(int i, boolean z);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public HlsVariant() {
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public HlsVariant(String str, int i) {
        this.mUrl = str;
        this.mBitrate = i;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void close() {
        cancelReload();
        cancelSegment();
        synchronized (this) {
            notifyAll();
            this.mSegments.clear();
        }
        this.mDownloadPool.clear();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public synchronized void dropDataOutsideInterval(double d, double d2) {
        for (HlsSegment hlsSegment : this.mSegments) {
            if (hlsSegment.mStartTime + hlsSegment.mDuration < d || hlsSegment.mStartTime > d2) {
                if (!hlsSegment.isDownloading()) {
                    hlsSegment.mDownloadComplete = false;
                    hlsSegment.mDownloadBitrate = 0L;
                    Iterator<ByteBuffer> it = hlsSegment.mSegmentData.iterator();
                    while (it.hasNext()) {
                        this.mDownloadPool.add(it.next());
                    }
                    hlsSegment.mSegmentData.clear();
                    hlsSegment.mSegmentData.trimToSize();
                }
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void reload(PlaylistCallback playlistCallback) {
        if (this.mPlaylistLoadTask != null || playlistCallback == null) {
            return;
        }
        this.mPlaylistLoadTask = new PlaylistLoadTask(this, playlistCallback);
        this.mPlaylistLoadTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, new Void[0]);
    }

    private void cancelReload() {
        if (this.mPlaylistLoadTask != null) {
            this.mPlaylistLoadTask.cancel(false);
        }
        this.mPlaylistLoadTask = null;
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* JADX WARN: Multi-variable type inference failed */
    public static Playlist parseVariantPlaylist(String str, String str2) {
        float f;
        String[] split = str2.split("\\r?\\n");
        Playlist playlist = new Playlist();
        int length = split.length;
        int i = 0;
        boolean z = false;
        double d = 0.0d;
        float f2 = -1.0f;
        long j = -1;
        int i2 = -1;
        int i3 = 0;
        while (i3 < length) {
            String str3 = split[i3];
            boolean z2 = 1;
            if (str3.startsWith("#EXTM3U")) {
                playlist.mSeenEXTM3U = true;
            } else {
                if (str3.startsWith("#EXTINF:")) {
                    try {
                        f = Float.parseFloat(str3.substring("#EXTINF:".length()).replaceFirst("^([\\d.]*).*$", "$1"));
                    } catch (NumberFormatException unused) {
                        f = -1.0f;
                    }
                    f2 = f;
                } else if (str3.startsWith("#EXT-X-BAMBUSER-TIME:")) {
                    String substring = str3.substring("#EXT-X-BAMBUSER-TIME:".length());
                    int indexOf = substring.indexOf("CAPTURE=");
                    if (indexOf >= 0) {
                        try {
                            j = Long.parseLong(substring.substring(indexOf + "CAPTURE=".length()).replaceFirst("^(\\d*).*$", "$1"));
                        } catch (NumberFormatException unused2) {
                            j = -1;
                        }
                    }
                    int indexOf2 = substring.indexOf("UNCERTAINTY=");
                    if (indexOf2 >= 0) {
                        try {
                            i2 = Integer.parseInt(substring.substring(indexOf2 + "UNCERTAINTY=".length()).replaceFirst("^(-?\\d*).*$", "$1"));
                            z = true;
                        } catch (NumberFormatException unused3) {
                            i2 = -1;
                        }
                    }
                } else if (str3.startsWith("#EXT-X-MEDIA-SEQUENCE:")) {
                    try {
                        playlist.mFirstSegmentSeq = Integer.parseInt(str3.substring("#EXT-X-MEDIA-SEQUENCE:".length()).replaceFirst("^(\\d*).*$", "$1"));
                    } catch (NumberFormatException unused4) {
                    }
                } else if (str3.startsWith("#EXT-X-TARGETDURATION:")) {
                    try {
                        playlist.mTargetSegmentDuration = Integer.parseInt(str3.substring("#EXT-X-TARGETDURATION:".length()).replaceFirst("^(\\d*).*$", "$1"));
                    } catch (NumberFormatException unused5) {
                        playlist.mTargetSegmentDuration = i;
                    }
                } else if (str3.startsWith("#EXT-X-ENDLIST")) {
                    playlist.mFinished = true;
                } else if ((!str3.startsWith(MqttTopic.MULTI_LEVEL_WILDCARD) || str3.startsWith("#EXT-X-BAMBUSER-NEXT:")) && str3.length() > 0) {
                    if (str3.startsWith("#EXT-X-BAMBUSER-NEXT:")) {
                        str3 = str3.substring("#EXT-X-BAMBUSER-NEXT:".length());
                    } else {
                        z2 = i;
                    }
                    try {
                        try {
                            String url = new URL(new URL(str), str3).toString();
                            if (f2 <= 0.0f) {
                                f2 = playlist.mTargetSegmentDuration;
                            }
                            HlsSegment hlsSegment = new HlsSegment(f2, url, z2);
                            playlist.mSegments.add(hlsSegment);
                            hlsSegment.mStartTime = (playlist.mFirstSegmentSeq * playlist.mTargetSegmentDuration) + d;
                            d += f2;
                            hlsSegment.mCaptureTime = j;
                            hlsSegment.mCaptureTimeUncertainty = i2;
                            hlsSegment.mUncertaintyParsed = z;
                        } catch (MalformedURLException unused6) {
                        }
                    } catch (MalformedURLException unused7) {
                    }
                    f2 = -1.0f;
                    j = -1;
                    i2 = -1;
                    z = false;
                }
                i3++;
                i = 0;
            }
            i3++;
            i = 0;
        }
        playlist.mSeekableDuration = d;
        playlist.mTotalDuration = (playlist.mFirstSegmentSeq * playlist.mTargetSegmentDuration) + d;
        return playlist;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void onPlaylistLoaded(Playlist playlist, PlaylistCallback playlistCallback) {
        if (this.mPlaylistLoadTask == null) {
            return;
        }
        this.mPlaylistLoadTask = null;
        if (playlist.mSegments.isEmpty() && playlist.mTargetSegmentDuration <= 0 && !playlist.mSeenEXTM3U) {
            playlistCallback.onError();
        } else if (!playlist.mFinished && playlist.mFirstSegmentSeq <= this.mFirstSegmentSeq && playlist.mFirstSegmentSeq + playlist.mSegments.size() <= this.mFirstSegmentSeq + this.mSegments.size()) {
            playlistCallback.onLoaded();
        } else {
            synchronized (this) {
                for (int i = 0; i < playlist.mSegments.size(); i++) {
                    int i2 = playlist.mFirstSegmentSeq + i;
                    if (i2 >= this.mFirstSegmentSeq) {
                        if (i2 - this.mFirstSegmentSeq >= this.mSegments.size()) {
                            break;
                        }
                        HlsSegment hlsSegment = playlist.mSegments.get(i);
                        HlsSegment hlsSegment2 = this.mSegments.get(i2 - this.mFirstSegmentSeq);
                        hlsSegment.mSegmentData = hlsSegment2.mSegmentData;
                        hlsSegment.mDownloaderTask = hlsSegment2.mDownloaderTask;
                        hlsSegment.mDownloadComplete = hlsSegment2.mDownloadComplete;
                        hlsSegment.mDownloadBitrate = hlsSegment2.mDownloadBitrate;
                        hlsSegment2.mSegmentData = null;
                        hlsSegment2.mDownloaderTask = null;
                    }
                }
                this.mTargetSegmentDuration = playlist.mTargetSegmentDuration;
                this.mTotalDuration = playlist.mTotalDuration;
                this.mSeekableDuration = playlist.mSeekableDuration;
                this.mSegments = playlist.mSegments;
                this.mFirstSegmentSeq = playlist.mFirstSegmentSeq;
                this.mFinished = playlist.mFinished;
                this.mDownBufSize = 32768;
                if (this.mTargetSegmentDuration >= 1 && this.mBitrate >= 100000) {
                    long j = ((this.mTargetSegmentDuration * this.mBitrate) / 8) / 100;
                    while (this.mDownBufSize < j) {
                        this.mDownBufSize *= 2;
                    }
                }
            }
            playlistCallback.onLoaded();
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void downloadSegment(SegmentCallback segmentCallback) {
        int i;
        HlsSegment segment;
        boolean z;
        if (segmentCallback == null) {
            return;
        }
        cancelSegment();
        synchronized (this) {
            i = this.mCurDownloadSegmentSeq;
            segment = getSegment(i);
            if (segment.mDownloadComplete) {
                notifyAll();
                this.mCurDownloadSegmentSeq++;
                z = true;
            } else {
                if (!segment.mSegmentData.isEmpty()) {
                    segment.mSegmentData.clear();
                }
                z = false;
            }
        }
        if (z) {
            segmentCallback.onLoaded(i, true);
            return;
        }
        this.mSegmentFetcherTask = new SegmentFetchTask(this, segment.mUrl, i, segmentCallback);
        segment.mDownloaderTask = this.mSegmentFetcherTask;
        this.mSegmentFetcherTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, new Void[0]);
    }

    private synchronized void cancelSegment() {
        if (this.mSegmentFetcherTask != null) {
            this.mSegmentFetcherTask.cancel(false);
        }
        this.mSegmentFetcherTask = null;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public synchronized void onSegmentFetchProgress(SegmentFetchTask segmentFetchTask, int i, ByteBuffer byteBuffer, long j) {
        if (this.mSegmentFetcherTask == segmentFetchTask && isValidSeq(i)) {
            HlsSegment segment = getSegment(i);
            segment.mSegmentData.add(byteBuffer);
            segment.mDownloadBitrate = j;
        } else {
            segmentFetchTask.cancel(false);
        }
        notifyAll();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public synchronized void onSegmentFetched(SegmentFetchTask segmentFetchTask, int i, boolean z, SegmentCallback segmentCallback) {
        boolean z2 = this.mSegmentFetcherTask == segmentFetchTask;
        if (z2) {
            this.mSegmentFetcherTask = null;
        }
        if (isValidSeq(i)) {
            HlsSegment segment = getSegment(i);
            if (segment.mDownloaderTask == segmentFetchTask) {
                segment.mDownloadComplete = z;
                segment.mDownloaderTask = null;
                notifyAll();
            }
        }
        if (z2) {
            segmentCallback.onLoaded(i, z);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public synchronized void onSegmentFetchCancelled(SegmentFetchTask segmentFetchTask, int i) {
        if (this.mSegmentFetcherTask == segmentFetchTask) {
            this.mSegmentFetcherTask = null;
        }
        if (isValidSeq(i)) {
            HlsSegment segment = getSegment(i);
            if (segment.mDownloaderTask == segmentFetchTask) {
                segment.mDownloaderTask = null;
                notifyAll();
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean isFetching() {
        return this.mSegmentFetcherTask != null;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean isReloading() {
        return this.mPlaylistLoadTask != null;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean isValidSeq(int i) {
        return i >= this.mFirstSegmentSeq && i <= getLastSegmentSeq();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public HlsSegment getSegment(int i) {
        return this.mSegments.get(i - this.mFirstSegmentSeq);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public int getLastSegmentSeq() {
        return (this.mFirstSegmentSeq + this.mSegments.size()) - 1;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public int getBestStartupSegmentSeq(boolean z, boolean z2) {
        if (!z && this.mFinished) {
            return this.mFirstSegmentSeq;
        }
        int size = this.mSegments.size() - 1;
        int i = 0;
        for (int i2 = size; i2 >= 0 && this.mSegments.get(i2).mBambuserNext; i2--) {
            i++;
        }
        if (z2) {
            return this.mFirstSegmentSeq + Math.max(0, size - i);
        } else if (i > 1) {
            return this.mFirstSegmentSeq + (size - i) + 1;
        } else {
            return getLastSegmentSeq();
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean isPlayableSeq(int i) {
        if (isValidSeq(i)) {
            HlsSegment segment = getSegment(i);
            if (segment.mDownloadComplete || segment.isDownloading()) {
                return !segment.mSegmentData.isEmpty();
            }
            return false;
        }
        return false;
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes.dex */
    public static class Playlist {
        boolean mFinished;
        int mFirstSegmentSeq;
        double mSeekableDuration;
        boolean mSeenEXTM3U;
        List<HlsSegment> mSegments;
        int mTargetSegmentDuration;
        double mTotalDuration;

        private Playlist() {
            this.mSegments = new ArrayList();
            this.mFirstSegmentSeq = 0;
            this.mFinished = false;
            this.mSeenEXTM3U = false;
            this.mTargetSegmentDuration = 0;
            this.mTotalDuration = Utils.DOUBLE_EPSILON;
            this.mSeekableDuration = Utils.DOUBLE_EPSILON;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes.dex */
    public static class PlaylistLoadTask extends AsyncTask<Void, Void, Playlist> {
        private PlaylistCallback mCb;
        private HlsVariant mVariant;

        PlaylistLoadTask(HlsVariant hlsVariant, PlaylistCallback playlistCallback) {
            this.mVariant = hlsVariant;
            this.mCb = playlistCallback;
        }

        /* JADX INFO: Access modifiers changed from: protected */
        @Override // android.os.AsyncTask
        public Playlist doInBackground(Void... voidArr) {
            Pair<Integer, String> pair = BackendApi.get(this.mVariant.mUrl);
            if (((Integer) pair.first).intValue() == 200 && pair.second != null) {
                this.mCb.onDownloadedBytes(((String) pair.second).length());
                return HlsVariant.parseVariantPlaylist(this.mVariant.mUrl, (String) pair.second);
            }
            return new Playlist();
        }

        /* JADX INFO: Access modifiers changed from: protected */
        @Override // android.os.AsyncTask
        public void onPostExecute(Playlist playlist) {
            this.mVariant.onPlaylistLoaded(playlist, this.mCb);
            this.mVariant = null;
            this.mCb = null;
        }

        @Override // android.os.AsyncTask
        protected void onCancelled() {
            if (this.mVariant != null) {
                this.mVariant.mPlaylistLoadTask = null;
            }
            this.mVariant = null;
            this.mCb = null;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes.dex */
    public static class SegmentFetchTask extends AsyncTask<Void, Void, Boolean> {
        private SegmentCallback mCb;
        private int mSegmentSeq;
        private String mUrl;
        private HlsVariant mVariant;

        SegmentFetchTask(HlsVariant hlsVariant, String str, int i, SegmentCallback segmentCallback) {
            this.mVariant = hlsVariant;
            this.mUrl = str;
            this.mSegmentSeq = i;
            this.mCb = segmentCallback;
        }

        /* JADX INFO: Access modifiers changed from: protected */
        @Override // android.os.AsyncTask
        public Boolean doInBackground(Void... voidArr) {
            long currentTimeMillis = System.currentTimeMillis();
            boolean z = false;
            try {
                HttpURLConnection httpURLConnection = (HttpURLConnection) new URL(this.mUrl).openConnection();
                if (httpURLConnection instanceof HttpsURLConnection) {
                    ((HttpsURLConnection) httpURLConnection).setSSLSocketFactory(ModernTlsSocketFactory.getInstance());
                }
                httpURLConnection.setConnectTimeout(10000);
                httpURLConnection.setReadTimeout(10000);
                httpURLConnection.connect();
                InputStream inputStream = httpURLConnection.getInputStream();
                long j = 0;
                while (!isCancelled() && !z) {
                    ByteBuffer buffer = this.mVariant.mDownloadPool.getBuffer(this.mVariant.mDownBufSize);
                    while (true) {
                        if (!buffer.hasRemaining() || isCancelled()) {
                            break;
                        }
                        int position = buffer.position();
                        int read = inputStream.read(buffer.array(), position, buffer.remaining());
                        if (read < 0) {
                            z = true;
                            break;
                        } else if (read != 0) {
                            buffer.position(position + read);
                        }
                    }
                    boolean z2 = z;
                    try {
                        buffer.flip();
                        if (buffer.limit() == 0) {
                            this.mVariant.mDownloadPool.add(buffer);
                            z = z2;
                        } else {
                            long limit = buffer.limit();
                            long j2 = j + limit;
                            long currentTimeMillis2 = System.currentTimeMillis() - currentTimeMillis;
                            this.mVariant.onSegmentFetchProgress(this, this.mSegmentSeq, buffer, currentTimeMillis2 > 0 ? ((8 * j2) * 1000) / currentTimeMillis2 : 0L);
                            this.mCb.onDownloadedBytes(limit);
                            z = z2;
                            j = j2;
                        }
                    } catch (Exception e) {
                        e = e;
                        z = z2;
                        Log.w(HlsVariant.LOGTAG, "connection exception: " + e);
                        return Boolean.valueOf(z);
                    }
                }
                inputStream.close();
                httpURLConnection.disconnect();
            } catch (Exception e2) {
                e = e2;
            }
            return Boolean.valueOf(z);
        }

        /* JADX INFO: Access modifiers changed from: protected */
        @Override // android.os.AsyncTask
        public void onPostExecute(Boolean bool) {
            this.mVariant.onSegmentFetched(this, this.mSegmentSeq, bool.booleanValue(), this.mCb);
            this.mVariant = null;
            this.mCb = null;
        }

        @Override // android.os.AsyncTask
        protected void onCancelled() {
            if (this.mVariant != null) {
                this.mVariant.onSegmentFetchCancelled(this, this.mSegmentSeq);
            }
            this.mVariant = null;
            this.mCb = null;
        }
    }
}
