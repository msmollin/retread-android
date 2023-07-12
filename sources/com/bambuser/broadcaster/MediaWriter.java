package com.bambuser.broadcaster;

import android.os.StatFs;
import android.os.SystemClock;
import android.util.Log;
import com.facebook.cache.disk.DefaultDiskStorage;
import com.facebook.drawee.generic.GenericDraweeHierarchyBuilder;
import java.io.File;
import java.nio.ByteBuffer;

/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: classes.dex */
public final class MediaWriter {
    private static final String CREATOR_TAG = "LibBambuser";
    private static final String LOGTAG = "MediaWriter";
    private Mp4Muxer mMuxer = null;
    private long mStatTime = 0;
    private StatFs mDriveStat = null;
    private boolean mGotVideoPackets = false;
    private boolean mGotAudioPackets = false;
    private boolean mVideoTrackAdded = false;
    private boolean mAudioTrackAdded = false;
    private String mOutputMp4Path = null;
    private String mTempMp4Path = null;

    /* JADX INFO: Access modifiers changed from: package-private */
    public synchronized boolean init(File file, String str) {
        close();
        if (file != null && !file.isDirectory()) {
            this.mOutputMp4Path = file.getAbsolutePath();
            this.mTempMp4Path = this.mOutputMp4Path + DefaultDiskStorage.FileType.TEMP;
            this.mMuxer = new Mp4Muxer();
            this.mMuxer.setChunkDuration(GenericDraweeHierarchyBuilder.DEFAULT_FADE_DURATION);
            this.mMuxer.setFragmentDuration(15000);
            this.mMuxer.setTitle(str);
            this.mMuxer.setCreator(CREATOR_TAG);
            boolean start = this.mMuxer.start(this.mTempMp4Path);
            if (!start) {
                Log.e(LOGTAG, "Unable to write to " + this.mTempMp4Path);
                close();
            }
            return start;
        }
        return false;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public synchronized boolean addAudioTrack(int i, int i2) {
        if (this.mMuxer != null && !this.mAudioTrackAdded) {
            this.mMuxer.addAudioTrack(i, i2);
            this.mAudioTrackAdded = true;
            return true;
        }
        return false;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public synchronized boolean addVideoTrack(int i, int i2, int i3) {
        if (this.mMuxer != null && !this.mVideoTrackAdded) {
            this.mMuxer.addVideoTrack(i, i2, i3);
            this.mVideoTrackAdded = true;
            return true;
        }
        return false;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public synchronized void setTitle(String str) {
        if (this.mMuxer != null) {
            this.mMuxer.setTitle(str);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public synchronized void setLocation(double d, double d2) {
        if (this.mMuxer != null) {
            this.mMuxer.setGeoLocation(d, d2);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public synchronized String close() {
        String str;
        if (this.mMuxer != null) {
            this.mMuxer.close();
        }
        this.mMuxer = null;
        if (this.mTempMp4Path != null) {
            if (!this.mGotVideoPackets && (this.mVideoTrackAdded || !this.mGotAudioPackets)) {
                new File(this.mTempMp4Path).delete();
                str = null;
                this.mOutputMp4Path = null;
                this.mTempMp4Path = null;
            }
            if (new File(this.mTempMp4Path).renameTo(new File(this.mOutputMp4Path))) {
                str = this.mOutputMp4Path;
            } else {
                Log.w(LOGTAG, "Unable to rename " + this.mTempMp4Path + " to " + this.mOutputMp4Path);
                str = this.mTempMp4Path;
            }
            this.mOutputMp4Path = null;
            this.mTempMp4Path = null;
        } else {
            str = null;
        }
        this.mVideoTrackAdded = false;
        this.mAudioTrackAdded = false;
        this.mGotVideoPackets = false;
        this.mGotAudioPackets = false;
        this.mOutputMp4Path = null;
        this.mTempMp4Path = null;
        return str;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public synchronized boolean acceptsData() {
        return this.mMuxer != null;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public synchronized boolean write(int i, ByteBuffer byteBuffer, long j) {
        if (this.mMuxer == null) {
            return false;
        }
        if (SystemClock.uptimeMillis() >= this.mStatTime + 1000) {
            this.mStatTime = SystemClock.uptimeMillis();
            if (freeBytes() < ((this.mMuxer.getFileDuration() * 500) / 1000) + MovinoFileUtils.MINIMUM_FREE_BYTES) {
                Log.w(LOGTAG, "Not enough free space on storage");
                return false;
            }
        }
        try {
            if (i == 36) {
                this.mMuxer.setAudioExtradata(byteBuffer);
            } else if (i == 37) {
                this.mMuxer.writeAudioPacket(byteBuffer, j);
                this.mGotAudioPackets = true;
            } else if (i == 39) {
                this.mMuxer.setVideoExtradata(byteBuffer);
            } else if (i == 48) {
                this.mMuxer.setHevcVideoExtradata(byteBuffer);
            } else {
                if (i != 24 && i != 47) {
                    Log.w(LOGTAG, "Unknown data type: " + i);
                }
                this.mMuxer.writeVideoPacket(byteBuffer, j);
                this.mGotVideoPackets = true;
            }
            return true;
        } catch (Exception e) {
            Log.w(LOGTAG, "Exception while writing media: " + e);
            return false;
        }
    }

    private synchronized long freeBytes() {
        if (this.mTempMp4Path == null) {
            return 0L;
        }
        try {
            if (this.mDriveStat == null) {
                this.mDriveStat = new StatFs(this.mTempMp4Path);
            } else {
                this.mDriveStat.restat(this.mTempMp4Path);
            }
            return this.mDriveStat.getAvailableBytes();
        } catch (IllegalArgumentException e) {
            Log.w(LOGTAG, "StatFs failed with: " + e);
            return 0L;
        }
    }
}
