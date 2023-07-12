package com.bambuser.broadcaster;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.os.StatFs;
import android.os.SystemClock;
import android.support.v4.media.session.PlaybackStateCompat;
import android.text.format.DateUtils;
import android.util.Log;
import com.bambuser.broadcaster.SentryLogger;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.text.DateFormat;
import java.util.Date;
import java.util.HashMap;
import org.json.JSONObject;

/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: classes.dex */
public final class MovinoData implements Comparable<MovinoData> {
    private static final String ID_KEY = "id";
    private static final String LENGTH_KEY = "length";
    private static final String LOGTAG = "MovinoData";
    private static final String PREVIEW_KEY = "preview";
    private static final int PREVIEW_WIDTH = 144;
    private static final String TIMESTAMP_KEY = "timestamp";
    private static final String TITLE_KEY = "title";
    private static final String UPLOADED_BYTES_KEY = "uploaded_bytes";
    private static final String UPLOAD_STARTED_KEY = "upload_started";
    private static final String UPLOAD_TYPE_KEY = "upload_type";
    private static final String USERNAME_KEY = "username";
    private static final String VISIBILITY_KEY = "visibility";
    static final int VISIBILITY_MIXED = 3;
    static final int VISIBILITY_PRIVATE = 2;
    static final int VISIBILITY_PUBLIC = 1;
    static final int VISIBILITY_UNKNOWN = 0;
    private final HashMap<String, Object> mMetadata = new HashMap<>();
    private File mMovinoFile = null;
    private BufferedOutputStream mOut = null;
    private BufferedInputStream mIn = null;
    private long mStatTime = 0;
    private StatFs mDriveStat = null;
    private File mInfoFile = null;
    private long mUploadedBytes = 0;
    private boolean mLocked = false;

    /* JADX INFO: Access modifiers changed from: package-private */
    public MovinoData(String str) {
        this.mMetadata.put("id", str);
    }

    protected void finalize() throws Throwable {
        closeOutput();
        closeInput();
        super.finalize();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public synchronized void store() {
        if (this.mInfoFile == null) {
            return;
        }
        this.mMetadata.put(UPLOADED_BYTES_KEY, Long.valueOf(this.mUploadedBytes));
        try {
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(new FileOutputStream(this.mInfoFile));
            objectOutputStream.writeObject(this.mMetadata);
            objectOutputStream.close();
        } catch (Exception e) {
            Log.w(LOGTAG, "could not write metadata", e);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public synchronized void load() {
        if (!fileExists()) {
            delete();
        }
        if (this.mInfoFile == null || this.mInfoFile.length() == 0) {
            this.mMetadata.clear();
            return;
        }
        try {
            ObjectInputStream objectInputStream = new ObjectInputStream(new FileInputStream(this.mInfoFile));
            Object readObject = objectInputStream.readObject();
            objectInputStream.close();
            if (readObject != null && (readObject instanceof HashMap)) {
                this.mMetadata.putAll((HashMap) readObject);
            }
        } catch (Exception e) {
            Log.w(LOGTAG, "could not read metadata: " + e.toString());
        }
        if (this.mMetadata.containsKey(UPLOADED_BYTES_KEY)) {
            setUploadedBytes(((Long) this.mMetadata.get(UPLOADED_BYTES_KEY)).longValue());
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void delete() {
        delete(true);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public synchronized void delete(boolean z) {
        closeOutput();
        closeInput();
        if (this.mMovinoFile != null && this.mMovinoFile.exists() && !this.mMovinoFile.delete() && z) {
            JSONObject jSONObject = new JSONObject();
            try {
                jSONObject.put("path", this.mMovinoFile.getAbsolutePath());
                jSONObject.put("thread", Thread.currentThread().toString());
                jSONObject.put("file_exists", this.mMovinoFile.exists());
                jSONObject.put("file_length", this.mMovinoFile.length());
            } catch (Exception unused) {
            }
            SentryLogger.asyncMessage("Failed to delete movino file", SentryLogger.Level.WARNING, jSONObject, new Exception("Stack trace"));
            Log.w(LOGTAG, "failed to delete " + this.mMovinoFile.getAbsolutePath());
            return;
        }
        this.mMovinoFile = null;
        if (this.mInfoFile != null && this.mInfoFile.exists() && !this.mInfoFile.delete() && z) {
            JSONObject jSONObject2 = new JSONObject();
            try {
                jSONObject2.put("path", this.mInfoFile.getAbsolutePath());
                jSONObject2.put("thread", Thread.currentThread().toString());
                jSONObject2.put("file_exists", this.mInfoFile.exists());
                jSONObject2.put("file_length", this.mInfoFile.length());
            } catch (Exception unused2) {
            }
            SentryLogger.asyncMessage("Failed to delete info file", SentryLogger.Level.WARNING, jSONObject2, new Exception("Stack trace"));
            Log.w(LOGTAG, "failed to delete " + this.mInfoFile.getAbsolutePath());
        }
        this.mInfoFile = null;
    }

    synchronized void closeOutput() {
        if (this.mOut == null) {
            return;
        }
        try {
            this.mOut.flush();
        } catch (IOException e) {
            Log.w(LOGTAG, "could not flush data to file", e);
        }
        try {
            this.mOut.close();
        } catch (IOException e2) {
            Log.w(LOGTAG, "could not close movino file properly", e2);
        }
        this.mOut = null;
    }

    private synchronized void closeInput() {
        if (this.mIn == null) {
            return;
        }
        try {
            this.mIn.close();
        } catch (IOException e) {
            Log.w(LOGTAG, "could not close movino file properly", e);
        }
        this.mIn = null;
    }

    private synchronized BufferedOutputStream getOutputStream() {
        if (this.mOut != null) {
            return this.mOut;
        }
        closeInput();
        if (fileExists() && this.mMovinoFile.canWrite()) {
            try {
                this.mOut = new BufferedOutputStream(new FileOutputStream(this.mMovinoFile, true), 65536);
            } catch (FileNotFoundException e) {
                Log.e(LOGTAG, "could not open data file for writing", e);
            }
        } else {
            Log.w(LOGTAG, "The data file (" + this.mMovinoFile + ") doesn't exist or can't be opened for writing");
        }
        return this.mOut;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public synchronized boolean fileExists() {
        boolean z;
        if (this.mMovinoFile != null && this.mMovinoFile.exists()) {
            z = this.mMovinoFile.isFile();
        }
        return z;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public synchronized boolean infoFileExists() {
        boolean z;
        if (this.mInfoFile != null && this.mInfoFile.exists()) {
            z = this.mInfoFile.isFile();
        }
        return z;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public synchronized BufferedInputStream getInputStream() {
        if (this.mIn != null) {
            return this.mIn;
        }
        closeOutput();
        if (fileExists() && this.mMovinoFile.canRead()) {
            try {
                this.mIn = new BufferedInputStream(new FileInputStream(this.mMovinoFile), 8192);
            } catch (FileNotFoundException e) {
                Log.e(LOGTAG, "could not open data file for reading", e);
            }
        } else {
            Log.w(LOGTAG, "The data file (" + this.mMovinoFile + ") doesn't exist or can't be opened for reading");
        }
        return this.mIn;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public synchronized void setInputPosition(long j) throws IOException {
        closeInput();
        BufferedInputStream inputStream = getInputStream();
        if (inputStream == null) {
            throw new IOException("could not get inputstream");
        }
        long j2 = 0;
        while (j2 < j) {
            long skip = inputStream.skip(j - j2);
            if (skip <= 0) {
                throw new IOException("could not skip " + j + " bytes data, skip returned " + skip);
            }
            j2 += skip;
            if (j2 < j) {
                Log.d(LOGTAG, "has skipped only " + j2 + " of requested " + j + ", retrying");
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public synchronized boolean write(RawPacket rawPacket) {
        if (rawPacket != null) {
            if (!this.mLocked) {
                if (SystemClock.uptimeMillis() >= this.mStatTime + 1000) {
                    this.mStatTime = SystemClock.uptimeMillis();
                    if (freeBytes() < MovinoFileUtils.MINIMUM_FREE_BYTES) {
                        lock();
                        return false;
                    }
                }
                try {
                    getOutputStream().write(rawPacket.getData(), 0, rawPacket.size());
                    return true;
                } catch (IOException unused) {
                    lock();
                    Log.w(LOGTAG, "data writing failing");
                    return false;
                }
            }
        }
        return true;
    }

    private synchronized long freeBytes() {
        if (this.mMovinoFile == null) {
            return 0L;
        }
        try {
            if (this.mDriveStat == null) {
                this.mDriveStat = new StatFs(this.mMovinoFile.getPath());
            } else {
                this.mDriveStat.restat(this.mMovinoFile.getPath());
            }
            return this.mDriveStat.getAvailableBytes();
        } catch (IllegalArgumentException e) {
            Log.w(LOGTAG, "external media is supposed to be mounted but failed with: " + e);
            return 0L;
        }
    }

    private synchronized long getLength() {
        if (this.mMetadata.containsKey(LENGTH_KEY)) {
            return ((Long) this.mMetadata.get(LENGTH_KEY)).longValue();
        }
        return (System.currentTimeMillis() - getTimestamp()) / 1000;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public long estimateFreeSeconds() {
        long freeBytes = freeBytes() - MovinoFileUtils.MINIMUM_FREE_BYTES;
        if (freeBytes <= 0) {
            return 0L;
        }
        long length = getLength();
        long size = getSize(false);
        if (length <= 10 || size <= PlaybackStateCompat.ACTION_SET_CAPTIONING_ENABLED) {
            return -1L;
        }
        return (long) (freeBytes / (size / length));
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public synchronized void lock() {
        this.mLocked = true;
        closeOutput();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public synchronized void setMovinoFile(File file) {
        this.mDriveStat = null;
        this.mMovinoFile = file;
        closeOutput();
        closeInput();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public synchronized void setInfoFile(File file) {
        this.mInfoFile = file;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public synchronized void setTitle(String str) {
        this.mMetadata.put("title", str);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public synchronized void setUsername(String str) {
        this.mMetadata.put(USERNAME_KEY, str);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public synchronized void setTimestamp(long j) {
        this.mMetadata.put("timestamp", Long.valueOf(j));
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public synchronized void setUploadType(int i) {
        this.mMetadata.put(UPLOAD_TYPE_KEY, Integer.valueOf(i));
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public synchronized void setUploadStarted() {
        this.mMetadata.put(UPLOAD_STARTED_KEY, Boolean.TRUE);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public synchronized void setLength(long j) {
        this.mMetadata.put(LENGTH_KEY, Long.valueOf(j));
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public synchronized void setVisibility(int i) {
        this.mMetadata.put("visibility", Integer.valueOf(i));
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public synchronized void incrementUploadedBytes(long j) {
        this.mUploadedBytes += j;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public synchronized void setUploadedBytes(long j) {
        this.mUploadedBytes = j;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public synchronized void setPreview(byte[] bArr, int i, int i2, int i3) {
        this.mMetadata.remove(PREVIEW_KEY);
        if (bArr == null) {
            return;
        }
        BitmapFactory.Options options = new BitmapFactory.Options();
        int i4 = 1;
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeByteArray(bArr, i, i2, options);
        while (true) {
            int i5 = i4 << 1;
            if (options.outWidth / i5 < PREVIEW_WIDTH) {
                break;
            }
            i4 = i5;
        }
        options.inSampleSize = i4;
        options.inJustDecodeBounds = false;
        Bitmap decodeByteArray = BitmapFactory.decodeByteArray(bArr, i, i2, options);
        if (decodeByteArray == null) {
            return;
        }
        if (i3 == 90 || i3 == 180 || i3 == 270) {
            Matrix matrix = new Matrix();
            matrix.setRotate(i3);
            decodeByteArray = Bitmap.createBitmap(decodeByteArray, 0, 0, decodeByteArray.getWidth(), decodeByteArray.getHeight(), matrix, false);
        }
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream(4096);
        decodeByteArray.compress(Bitmap.CompressFormat.PNG, 95, byteArrayOutputStream);
        this.mMetadata.put(PREVIEW_KEY, byteArrayOutputStream.toByteArray());
    }

    synchronized String getBasename() {
        return this.mMovinoFile != null ? MovinoFileUtils.getBasename(this.mMovinoFile.getName()) : null;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public synchronized String getId() {
        return this.mMetadata.containsKey("id") ? (String) this.mMetadata.get("id") : "";
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public long getSize() {
        return getSize(true);
    }

    private synchronized long getSize(boolean z) {
        if (this.mMovinoFile == null) {
            return 0L;
        }
        if (this.mOut != null && z) {
            try {
                this.mOut.flush();
            } catch (IOException e) {
                Log.w(LOGTAG, "could not flush data to file", e);
            }
        }
        return this.mMovinoFile.length();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public synchronized String getTitle() {
        String str;
        str = (String) this.mMetadata.get("title");
        if (str == null) {
            str = "";
        }
        return str;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public synchronized String getUsername() {
        return this.mMetadata.containsKey(USERNAME_KEY) ? (String) this.mMetadata.get(USERNAME_KEY) : "";
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public synchronized long getTimestamp() {
        return this.mMetadata.containsKey("timestamp") ? ((Long) this.mMetadata.get("timestamp")).longValue() : 0L;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public synchronized int getUploadType() {
        return this.mMetadata.containsKey(UPLOAD_TYPE_KEY) ? ((Integer) this.mMetadata.get(UPLOAD_TYPE_KEY)).intValue() : 0;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public synchronized boolean getUploadStarted() {
        return this.mMetadata.containsKey(UPLOAD_STARTED_KEY) ? ((Boolean) this.mMetadata.get(UPLOAD_STARTED_KEY)).booleanValue() : false;
    }

    synchronized String getTimestampString(String str) {
        if (this.mMetadata.containsKey("timestamp")) {
            return DateFormat.getDateTimeInstance(2, 2).format(new Date(getTimestamp()));
        }
        return str;
    }

    synchronized String getLengthString(String str) {
        if (this.mMetadata.containsKey(LENGTH_KEY)) {
            return DateUtils.formatElapsedTime(((Long) this.mMetadata.get(LENGTH_KEY)).longValue());
        }
        return str;
    }

    synchronized int getVisibility() {
        return this.mMetadata.containsKey("visibility") ? ((Integer) this.mMetadata.get("visibility")).intValue() : 0;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public synchronized long getUploadedBytes() {
        return this.mUploadedBytes;
    }

    synchronized BitmapDrawable getPreview() {
        if (this.mMetadata.containsKey(PREVIEW_KEY)) {
            byte[] bArr = (byte[]) this.mMetadata.get(PREVIEW_KEY);
            if (bArr == null) {
                return null;
            }
            Bitmap decodeByteArray = BitmapFactory.decodeByteArray(bArr, 0, bArr.length, null);
            if (decodeByteArray == null) {
                return null;
            }
            decodeByteArray.setDensity(160);
            return new BitmapDrawable(decodeByteArray);
        }
        return null;
    }

    public String toString() {
        return getTitle();
    }

    @Override // java.lang.Comparable
    public int compareTo(MovinoData movinoData) {
        return Long.signum(getTimestamp() - movinoData.getTimestamp());
    }
}
