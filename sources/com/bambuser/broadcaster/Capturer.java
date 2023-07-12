package com.bambuser.broadcaster;

import android.content.Context;
import android.location.Location;
import android.media.ExifInterface;
import android.media.MediaActionSound;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.SystemClock;
import android.util.Log;
import android.util.Pair;
import android.view.SurfaceView;
import androidx.recyclerview.widget.ItemTouchHelper;
import com.bambuser.broadcaster.AudioCapturer;
import com.bambuser.broadcaster.FrameHandler;
import com.bambuser.broadcaster.TalkbackController;
import com.bambuser.broadcaster.VideoCapturerBase;
import com.facebook.appevents.AppEventsConstants;
import com.facebook.login.widget.ToolTipPopup;
import com.github.mikephil.charting.utils.Utils;
import com.treadly.Treadly.Data.Model.UserDailyGoalType;
import java.nio.ByteBuffer;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicReference;
import org.eclipse.paho.client.mqttv3.MqttTopic;

/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: classes.dex */
public final class Capturer implements TalkbackController.AudioReceiver {
    private static final float AUTO_RESOLUTION_BITRATE_FACTOR = 0.85f;
    private static final int AUTO_RESOLUTION_CHANGE_BACKOFF_MS = 4000;
    private static final int AUTO_RESOLUTION_CHECK_INITIAL_BACKOFF_MS = 6000;
    private static final int AUTO_RESOLUTION_CHECK_INTERVAL_MS = 500;
    private static final float AUTO_RESOLUTION_TRESHOLD = 1.1f;
    private static final String LOGTAG = "Capturer";
    private static final int QUALITY_RATER_WINDOW_S = 5;
    private static Resolution sCamRes;
    private static SimpleDateFormat sDateTimeFormat;
    private static SimpleDateFormat sGpsDateFormat;
    private static SimpleDateFormat sGpsTimeFormat;
    private static Resolution sMaxLiveRes;
    private final AudioCapturer mAudioCapturer;
    private final Handler mEncoderHandler;
    private int mSampleRate;
    private int mTalkbackQueueSize;
    private Resampler mTalkbackResampler;
    private final VideoCapturerBase mVideoCapturer;
    private boolean mTalkbackMixin = false;
    private final LinkedList<ByteBuffer> mTalkbackQueue = new LinkedList<>();
    private boolean mTalkbackDropping = true;
    private int mEncodedAudioChannelsCount = 1;
    private final AtomicReference<QualityRater> mOverallQualityRater = new AtomicReference<>(null);
    private final AtomicReference<QualityRater> mNetworkQualityRater = new AtomicReference<>(null);
    private volatile boolean mCapturing = false;
    private volatile long mStartTime = 0;
    private final ConcurrentLinkedQueue<PictureCallback> mFrameCallbacks = new ConcurrentLinkedQueue<>();

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes.dex */
    public interface AudioRestartCallback {
        void onAudioSettingApplied();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes.dex */
    public interface CameraInterface {
        void onCameraError(CameraError cameraError);

        void onCameraPreviewStateChanged(boolean z);

        String onGetCameraId();

        int onGetCaptureRotation();

        boolean onGetCaptureSounds();

        Resolution onGetCroppedResolution();

        int onGetFrameRate();

        int onGetPreviewRotation();

        Resolution onGetResolution();

        void onResolutionChangeNeeded(Resolution resolution);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes.dex */
    public interface EncodeInterface {

        /* loaded from: classes.dex */
        public enum AudioFormat {
            NONE,
            AAC,
            MULAW
        }

        void onAudioInitialized(long j, int i, int i2, int i3);

        boolean onCanSendAudio();

        boolean onCanSendFrame();

        boolean onCanWriteComplement();

        boolean onCanWriteLocal();

        void onComplementData(boolean z, long j, int i, ByteBuffer byteBuffer, boolean z2);

        void onComplementRotation(long j, int i);

        void onEncoderError(Exception exc);

        AudioFormat onGetAudioFormat();

        int onGetAudioSampleRate();

        String onGetCameraId();

        boolean onGetCaptureSounds();

        int onGetEncodedAudioChannelCount();

        Resolution onGetMaxLiveResolution();

        NetworkInfo onGetNetworkInfo();

        boolean onGetTalkbackMixin();

        long onGetUplinkBitrate();

        void onLiveRotation(long j, int i);

        void onLocalData(long j, int i, ByteBuffer byteBuffer);

        void onPreviewTaken(byte[] bArr, int i, int i2);

        void onRawDataHandled(int i, boolean z);

        void onSendData(boolean z, long j, int i, ByteBuffer byteBuffer, boolean z2);

        void onSendLogMessage(String str);

        boolean onStartAutoRes();

        void onVideoInitialized(InfoFrame infoFrame);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public Capturer(Context context, SurfaceView surfaceView, boolean z) {
        HandlerThread handlerThread = new HandlerThread("EncoderThread");
        handlerThread.start();
        this.mEncoderHandler = new EncoderHandler(handlerThread.getLooper());
        this.mAudioCapturer = new AudioCapturer(this, context, z);
        if (Build.VERSION.SDK_INT > 21) {
            int camera2HardwareLevel = DeviceInfoHandler.getCamera2HardwareLevel(context, AppEventsConstants.EVENT_PARAM_VALUE_NO);
            Log.i(LOGTAG, "camera2 hardware level: " + camera2HardwareLevel);
        }
        if (DeviceInfoHandler.useCamera2API(context)) {
            this.mVideoCapturer = new VideoCapturerLollipop(this, context, surfaceView);
        } else {
            this.mVideoCapturer = new VideoCapturer(this, context, surfaceView);
        }
        this.mVideoCapturer.prepareSurface();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void close() {
        stopCapture();
        setCameraObserver(null);
        setEncodeObserver(null);
        this.mVideoCapturer.close();
        this.mAudioCapturer.close();
        this.mEncoderHandler.sendEmptyMessage(3);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void setCameraObserver(CameraInterface cameraInterface) {
        this.mVideoCapturer.setCameraObserver(cameraInterface);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void setEncodeObserver(EncodeInterface encodeInterface) {
        this.mEncoderHandler.obtainMessage(9, encodeInterface).sendToTarget();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void focus() {
        this.mVideoCapturer.focus();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean hasFocus() {
        return this.mVideoCapturer.hasFocus();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void takePicture(final PictureCallback pictureCallback) {
        if (this.mCapturing) {
            this.mFrameCallbacks.add(pictureCallback);
        } else {
            this.mVideoCapturer.takePicture(new VideoCapturerBase.TakePictureCallback() { // from class: com.bambuser.broadcaster.Capturer.1
                @Override // com.bambuser.broadcaster.VideoCapturerBase.TakePictureCallback
                public Resolution onGetResolution() {
                    return pictureCallback.onGetResolution();
                }

                @Override // com.bambuser.broadcaster.VideoCapturerBase.TakePictureCallback
                public Location onGetLocation() {
                    return pictureCallback.onGetLocation();
                }

                @Override // com.bambuser.broadcaster.VideoCapturerBase.TakePictureCallback
                public void onJpegData(ByteBuffer byteBuffer) {
                    Capturer.this.mEncoderHandler.obtainMessage(11, 0, 0, Pair.create(pictureCallback, byteBuffer)).sendToTarget();
                }
            });
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void onRawData(int i, boolean z, boolean z2) {
        QualityRater qualityRater;
        QualityRater qualityRater2 = this.mOverallQualityRater.get();
        if (qualityRater2 != null) {
            qualityRater2.addBytes(i, z2);
        }
        if (!z || (qualityRater = this.mNetworkQualityRater.get()) == null) {
            return;
        }
        qualityRater.addBytes(i, z2);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void newFrame(Frame frame) {
        this.mEncoderHandler.obtainMessage(1, frame).sendToTarget();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void newAudio(AudioWrapper audioWrapper) {
        this.mEncoderHandler.obtainMessage(2, audioWrapper).sendToTarget();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean isCapturing() {
        return this.mCapturing;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean stopCapture() {
        boolean z = this.mCapturing;
        this.mVideoCapturer.stopVideoCapture();
        this.mAudioCapturer.stopAudioCapture();
        if (Thread.currentThread() == this.mEncoderHandler.getLooper().getThread()) {
            throw new RuntimeException("stopCapture() called on encoder thread. can't wait for self!");
        }
        Object obj = new Object();
        synchronized (obj) {
            this.mEncoderHandler.sendMessageAtFrontOfQueue(this.mEncoderHandler.obtainMessage(5, obj));
            while (true) {
                try {
                    obj.wait();
                    break;
                } catch (InterruptedException unused) {
                }
            }
        }
        synchronized (this) {
            if (this.mTalkbackResampler != null) {
                this.mTalkbackResampler.close();
            }
            this.mTalkbackResampler = null;
        }
        return z;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void startCapture() {
        if (this.mCapturing) {
            return;
        }
        this.mEncoderHandler.sendEmptyMessage(6);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void stopLiveCapture() {
        if (this.mCapturing) {
            Object obj = new Object();
            synchronized (obj) {
                this.mEncoderHandler.sendMessageAtFrontOfQueue(this.mEncoderHandler.obtainMessage(13, obj));
                while (true) {
                    try {
                        obj.wait();
                        break;
                    } catch (InterruptedException unused) {
                    }
                }
            }
            synchronized (this) {
                if (this.mTalkbackResampler != null) {
                    this.mTalkbackResampler.close();
                }
                this.mTalkbackResampler = null;
            }
            synchronized (this.mTalkbackQueue) {
                this.mTalkbackQueue.clear();
                this.mTalkbackQueueSize = 0;
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void resumeLiveCapture() {
        if (this.mCapturing) {
            this.mEncoderHandler.sendEmptyMessage(14);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void refreshPreviewResolution() {
        this.mVideoCapturer.refreshPreviewResolution();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void refreshCropResolution() {
        this.mVideoCapturer.refreshCropResolution();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void restartCamera(boolean z) {
        this.mVideoCapturer.refreshCameraState(z);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void restartAudio(AudioRestartCallback audioRestartCallback) {
        this.mEncoderHandler.obtainMessage(4, audioRestartCallback).sendToTarget();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void postToEncoderThread(Runnable runnable) {
        this.mEncoderHandler.post(runnable);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void triggerMediaActionSound(int i) {
        this.mEncoderHandler.obtainMessage(12, i, 0).sendToTarget();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean hasZoom() {
        return this.mVideoCapturer.hasZoom();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public int getZoom() {
        return this.mVideoCapturer.getZoom();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public List<Integer> getZoomRatios() {
        return this.mVideoCapturer.getZoomRatios();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void setZoom(int i) {
        this.mVideoCapturer.setZoom(i);
    }

    void stepZoom(boolean z) {
        this.mVideoCapturer.stepZoom(z);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean hasTorch() {
        return this.mVideoCapturer.hasTorch();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void toggleTorch() {
        this.mVideoCapturer.toggleTorch();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void refreshPreviewRotation() {
        this.mVideoCapturer.refreshPreviewRotation();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void refreshCaptureRotation() {
        this.mVideoCapturer.refreshCaptureRotation();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public long getCaptureDuration() {
        return SystemClock.elapsedRealtime() - this.mStartTime;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void onCameraReadyForCapture(InfoFrame infoFrame, boolean z) {
        this.mEncoderHandler.obtainMessage(8, z ? 1 : 0, 0, infoFrame).sendToTarget();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static int getBitrate(int i, int i2, int i3, int i4) {
        if (i3 > 30) {
            i3 = ((i3 - 30) / 3) + 30;
        }
        return ((int) ((((i * 12) * i2) * i3) / i4)) + UserDailyGoalType.STEPS_MAX;
    }

    private static List<Resolution> getPossibleResolutions(int i, int i2, int i3, int i4) {
        ArrayList arrayList = new ArrayList();
        if (i <= i3 && i2 <= i4) {
            arrayList.add(new Resolution(i, i2));
        }
        int i5 = i * 3;
        int align = Frame.align(i5 / 4, 2);
        int i6 = i2 * 3;
        int align2 = Frame.align(i6 / 4, 2);
        if (align <= i3 && align2 <= i4 && align >= 176 && align2 >= 144) {
            arrayList.add(new Resolution(align, align2));
        }
        int align3 = Frame.align((i * 5) / 8, 2);
        int align4 = Frame.align((i2 * 5) / 8, 2);
        if (align3 <= i3 && align4 <= i4 && align3 >= 176 && align4 >= 144) {
            arrayList.add(new Resolution(align3, align4));
        }
        int align5 = Frame.align(i / 2, 2);
        int align6 = Frame.align(i2 / 2, 2);
        if (align5 <= i3 && align6 <= i4 && align5 >= 176 && align6 >= 144) {
            arrayList.add(new Resolution(align5, align6));
        }
        int align7 = Frame.align(i5 / 8, 2);
        int align8 = Frame.align(i6 / 8, 2);
        if (align7 <= i3 && align8 <= i4 && align7 >= 176 && align8 >= 144) {
            arrayList.add(new Resolution(align7, align8));
        }
        int align9 = Frame.align(i / 4, 2);
        int align10 = Frame.align(i2 / 4, 2);
        if (align9 <= i3 && align10 <= i4 && align9 >= 176 && align10 >= 144) {
            arrayList.add(new Resolution(align9, align10));
        }
        return arrayList;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static ArrayList<FrameHandler.Params> createLivePresets(int i, int i2, int i3, int i4, int i5, int i6) {
        int i7;
        int i8;
        int i9;
        int i10;
        int i11 = i6;
        ArrayList<FrameHandler.Params> arrayList = new ArrayList<>();
        String str = "video/avc";
        if (DeviceInfoHandler.isMediaCodecSupported() && MediaCodecWrapper.supportsCodec("video/hevc")) {
            str = "video/hevc";
            i7 = 80;
        } else {
            i7 = 48;
        }
        String str2 = str;
        int i12 = i7;
        List<Resolution> possibleResolutions = getPossibleResolutions(i, i2, i3, i4);
        int i13 = 0;
        while (i13 < possibleResolutions.size()) {
            int i14 = i13 + 1;
            if (i14 < possibleResolutions.size()) {
                Resolution resolution = possibleResolutions.get(i14);
                i8 = (int) (getBitrate(resolution.getWidth(), resolution.getHeight(), i11, i12) * AUTO_RESOLUTION_TRESHOLD);
            } else {
                i8 = 0;
            }
            Resolution resolution2 = possibleResolutions.get(i13);
            int width = resolution2.getWidth();
            int height = resolution2.getHeight();
            int bitrate = getBitrate(width, height, i11, i12);
            while (true) {
                if (bitrate <= i8) {
                    i9 = 300000;
                    i10 = bitrate;
                    break;
                }
                i9 = 300000;
                int i15 = bitrate;
                int i16 = height;
                arrayList.add(new FrameHandler.Params(str2, width, height, i5, i6, i15));
                i10 = i15;
                if (i10 < 300000) {
                    break;
                }
                bitrate = (int) (i10 * AUTO_RESOLUTION_BITRATE_FACTOR);
                height = i16;
            }
            if (i10 < i9) {
                break;
            }
            i13 = i14;
            i11 = i6;
        }
        Collections.sort(arrayList);
        return arrayList;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static Resolution getMaxLiveResolutionFromPresets(int i, int i2, int i3, int i4) {
        if (sCamRes == null || sCamRes.getWidth() != i || sCamRes.getHeight() != i2 || sMaxLiveRes == null || sMaxLiveRes.getWidth() != i3 || sMaxLiveRes.getHeight() != i4) {
            ArrayList<FrameHandler.Params> createLivePresets = createLivePresets(i, i2, i3, i4, 0, 30);
            FrameHandler.Params params = createLivePresets.get(0);
            Iterator<FrameHandler.Params> it = createLivePresets.iterator();
            while (it.hasNext()) {
                FrameHandler.Params next = it.next();
                if (next.width > params.width || next.height > params.height) {
                    params = next;
                }
            }
            sCamRes = new Resolution(i, i2);
            sMaxLiveRes = new Resolution(params.width, params.height);
        }
        return sMaxLiveRes;
    }

    /* loaded from: classes.dex */
    private class EncoderHandler extends Handler {
        static final int AUDIODATA = 2;
        static final int AUDIO_READY = 7;
        static final int CAMERA_READY = 8;
        static final int CLOSE = 3;
        static final int FRAMEDATA = 1;
        static final int PICTUREDATA = 11;
        static final int PICTUREDATA_ADDEXIF = 1;
        static final int PICTUREDATA_HASEXIF = 0;
        static final int PLAY_MEDIA_ACTION_SOUND = 12;
        static final int RESTART_AUDIO = 4;
        static final int RESUME_LIVE_CAPTURE = 14;
        static final int SET_ENCODE_OBSERVER = 9;
        static final int START = 6;
        static final int STOP = 5;
        static final int STOP_LIVE_CAPTURE = 13;
        static final int UPDATE_AUTO_RESOLUTION = 10;
        private AudioDataHandler mAudioDataHandler;
        private boolean mAutoRes;
        private EncodeInterface mEncodeObserver;
        private FrameHandler mFrameHandler;
        private long mLastPresetChange;
        private boolean mLiveCapturePaused;
        private final ArrayList<FrameHandler.Params> mLivePresets;
        private MediaActionSound mMediaActionSound;
        private int mPresetIndex;

        EncoderHandler(Looper looper) {
            super(looper);
            this.mFrameHandler = null;
            this.mAudioDataHandler = null;
            this.mMediaActionSound = null;
            this.mEncodeObserver = null;
            this.mPresetIndex = 0;
            this.mLastPresetChange = 0L;
            this.mAutoRes = false;
            this.mLiveCapturePaused = false;
            this.mLivePresets = new ArrayList<>();
        }

        private void addExif(String str, int i, Location location) {
            int i2 = i != 0 ? i != 90 ? i != 180 ? i != 270 ? 0 : 8 : 3 : 6 : 1;
            try {
                if (Capturer.sDateTimeFormat == null) {
                    SimpleDateFormat unused = Capturer.sDateTimeFormat = new SimpleDateFormat("yyyy:MM:dd HH:mm:ss", Locale.US);
                    Capturer.sDateTimeFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
                }
                ExifInterface exifInterface = new ExifInterface(str);
                exifInterface.setAttribute(androidx.exifinterface.media.ExifInterface.TAG_MAKE, Build.MANUFACTURER);
                exifInterface.setAttribute(androidx.exifinterface.media.ExifInterface.TAG_MODEL, Build.MODEL);
                exifInterface.setAttribute(androidx.exifinterface.media.ExifInterface.TAG_X_RESOLUTION, "72/1");
                exifInterface.setAttribute(androidx.exifinterface.media.ExifInterface.TAG_Y_RESOLUTION, "72/1");
                exifInterface.setAttribute(androidx.exifinterface.media.ExifInterface.TAG_RESOLUTION_UNIT, androidx.exifinterface.media.ExifInterface.GPS_MEASUREMENT_2D);
                exifInterface.setAttribute(androidx.exifinterface.media.ExifInterface.TAG_SOFTWARE, Build.DISPLAY);
                exifInterface.setAttribute(androidx.exifinterface.media.ExifInterface.TAG_DATETIME, Capturer.sDateTimeFormat.format(new Date()));
                exifInterface.setAttribute(androidx.exifinterface.media.ExifInterface.TAG_Y_CB_CR_POSITIONING, AppEventsConstants.EVENT_PARAM_VALUE_YES);
                exifInterface.setAttribute(androidx.exifinterface.media.ExifInterface.TAG_ORIENTATION, String.valueOf(i2));
                exifInterface.setAttribute(androidx.exifinterface.media.ExifInterface.TAG_METERING_MODE, AppEventsConstants.EVENT_PARAM_VALUE_NO);
                exifInterface.setAttribute(androidx.exifinterface.media.ExifInterface.TAG_LIGHT_SOURCE, AppEventsConstants.EVENT_PARAM_VALUE_NO);
                if (location != null && location.getTime() > 0 && location.getProvider() != null) {
                    exifInterface.setAttribute(androidx.exifinterface.media.ExifInterface.TAG_GPS_LATITUDE, gpsDoubleToRationalDMS(location.getLatitude()));
                    exifInterface.setAttribute(androidx.exifinterface.media.ExifInterface.TAG_GPS_LATITUDE_REF, location.getLatitude() < Utils.DOUBLE_EPSILON ? androidx.exifinterface.media.ExifInterface.LATITUDE_SOUTH : "N");
                    exifInterface.setAttribute(androidx.exifinterface.media.ExifInterface.TAG_GPS_LONGITUDE, gpsDoubleToRationalDMS(location.getLongitude()));
                    exifInterface.setAttribute(androidx.exifinterface.media.ExifInterface.TAG_GPS_LONGITUDE_REF, location.getLongitude() < Utils.DOUBLE_EPSILON ? androidx.exifinterface.media.ExifInterface.LONGITUDE_WEST : androidx.exifinterface.media.ExifInterface.LONGITUDE_EAST);
                    exifInterface.setAttribute(androidx.exifinterface.media.ExifInterface.TAG_GPS_ALTITUDE, gpsAltitudeToRational(location.getAltitude()));
                    exifInterface.setAttribute(androidx.exifinterface.media.ExifInterface.TAG_GPS_ALTITUDE_REF, location.getAltitude() < Utils.DOUBLE_EPSILON ? AppEventsConstants.EVENT_PARAM_VALUE_YES : AppEventsConstants.EVENT_PARAM_VALUE_NO);
                    exifInterface.setAttribute(androidx.exifinterface.media.ExifInterface.TAG_GPS_PROCESSING_METHOD, location.getProvider().toUpperCase());
                    exifInterface.setAttribute(androidx.exifinterface.media.ExifInterface.TAG_GPS_DATESTAMP, gpsDatestamp(location.getTime()));
                    exifInterface.setAttribute(androidx.exifinterface.media.ExifInterface.TAG_GPS_TIMESTAMP, gpsTimestamp(location.getTime()));
                }
                exifInterface.saveAttributes();
            } catch (Exception e) {
                Log.w(Capturer.LOGTAG, "Failed writing exif data: " + e);
            }
        }

        private String gpsDoubleToRationalDMS(double d) {
            double abs = Math.abs(d);
            int i = (int) abs;
            double d2 = (abs - i) * 60.0d;
            int i2 = (int) d2;
            return String.format(Locale.US, "%d/1,%d/1,%d/1000", Integer.valueOf(i), Integer.valueOf(i2), Integer.valueOf((int) ((d2 - i2) * 60.0d * 1000.0d)));
        }

        private String gpsAltitudeToRational(double d) {
            return String.format(Locale.US, "%d/100", Integer.valueOf((int) (Math.abs(d) * 100.0d)));
        }

        private String gpsDatestamp(long j) {
            if (Capturer.sGpsDateFormat == null) {
                SimpleDateFormat unused = Capturer.sGpsDateFormat = new SimpleDateFormat("yyyy:MM:dd", Locale.US);
                Capturer.sGpsDateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
            }
            return Capturer.sGpsDateFormat.format(new Date(j));
        }

        private String gpsTimestamp(long j) {
            if (Capturer.sGpsTimeFormat == null) {
                SimpleDateFormat unused = Capturer.sGpsTimeFormat = new SimpleDateFormat("HH:mm:ss", Locale.US);
                Capturer.sGpsTimeFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
            }
            return Capturer.sGpsTimeFormat.format(new Date(j));
        }

        /* JADX WARN: Removed duplicated region for block: B:144:0x038e  */
        /* JADX WARN: Removed duplicated region for block: B:177:? A[RETURN, SYNTHETIC] */
        @Override // android.os.Handler
        /*
            Code decompiled incorrectly, please refer to instructions dump.
            To view partially-correct code enable 'Show inconsistent code' option in preferences
        */
        public void handleMessage(android.os.Message r14) {
            /*
                Method dump skipped, instructions count: 948
                To view this dump change 'Code comments level' option to 'DEBUG'
            */
            throw new UnsupportedOperationException("Method not decompiled: com.bambuser.broadcaster.Capturer.EncoderHandler.handleMessage(android.os.Message):void");
        }

        private void resetAutoRes(InfoFrame infoFrame) {
            FrameHandler.Params params;
            if (this.mAutoRes) {
                if (Capturer.this.mOverallQualityRater.get() == null) {
                    Capturer.this.mOverallQualityRater.set(new QualityRater(5));
                }
                if (Capturer.this.mNetworkQualityRater.get() == null) {
                    Capturer.this.mNetworkQualityRater.set(new QualityRater(5));
                }
                int round = Math.round(infoFrame.mFrameRate / 1000.0f);
                Resolution onGetMaxLiveResolution = this.mEncodeObserver != null ? this.mEncodeObserver.onGetMaxLiveResolution() : null;
                ArrayList createLivePresets = Capturer.createLivePresets(infoFrame.mWidth, infoFrame.mHeight, onGetMaxLiveResolution != null ? onGetMaxLiveResolution.getWidth() : 1280, onGetMaxLiveResolution != null ? onGetMaxLiveResolution.getHeight() : 720, infoFrame.mPixelFormat, round);
                NetworkInfo onGetNetworkInfo = this.mEncodeObserver != null ? this.mEncodeObserver.onGetNetworkInfo() : null;
                boolean z = true;
                if (!this.mLivePresets.equals(createLivePresets)) {
                    this.mLivePresets.clear();
                    this.mLivePresets.addAll(createLivePresets);
                    this.mPresetIndex = 0;
                    if (onGetNetworkInfo != null && (onGetNetworkInfo.getType() == 1 || onGetNetworkInfo.getSubtype() == 13 || onGetNetworkInfo.getSubtype() == 15)) {
                        this.mPresetIndex = this.mLivePresets.size() / 2;
                    }
                }
                long onGetUplinkBitrate = this.mEncodeObserver != null ? this.mEncodeObserver.onGetUplinkBitrate() : 0L;
                if (onGetUplinkBitrate > 0) {
                    this.mPresetIndex = 0;
                    for (int i = 0; i < this.mLivePresets.size() && onGetUplinkBitrate >= this.mLivePresets.get(i).bitrate + UserDailyGoalType.STEPS_MAX; i++) {
                        this.mPresetIndex = i;
                    }
                }
                Iterator<FrameHandler.Params> it = this.mLivePresets.iterator();
                while (it.hasNext()) {
                    Log.i(Capturer.LOGTAG, it.next().toString());
                }
                this.mFrameHandler.resetAutoEncoders(this.mLivePresets.get(this.mPresetIndex));
                this.mLastPresetChange = SystemClock.uptimeMillis();
                if (this.mEncodeObserver != null) {
                    boolean z2 = onGetNetworkInfo != null && onGetNetworkInfo.getType() == 1;
                    if ((onGetNetworkInfo == null || onGetNetworkInfo.getSubtype() != 13) && onGetNetworkInfo.getSubtype() != 15) {
                        z = false;
                    }
                    this.mEncodeObserver.onSendLogMessage("starting at quality " + this.mPresetIndex + MqttTopic.TOPIC_LEVEL_SEPARATOR + this.mLivePresets.size() + " (" + params.width + "x" + params.height + " at " + params.bitrate + " bits/s), wifi " + z2 + ", lte " + z + ", approx link speed " + (onGetUplinkBitrate / 8) + " bytes/s");
                }
                if (hasMessages(10)) {
                    return;
                }
                sendEmptyMessageDelayed(10, ToolTipPopup.DEFAULT_POPUP_DISPLAY_TIME);
                return;
            }
            this.mLivePresets.clear();
            Compat.tryCall(Capturer.this.mOverallQualityRater.getAndSet(null), "stop");
            Compat.tryCall(Capturer.this.mNetworkQualityRater.getAndSet(null), "stop");
        }

        private void updateLivePreset() {
            if (SystemClock.uptimeMillis() - this.mLastPresetChange < 4000 || this.mFrameHandler == null || this.mLivePresets.isEmpty() || this.mLiveCapturePaused) {
                return;
            }
            QualityRater qualityRater = (QualityRater) Capturer.this.mOverallQualityRater.get();
            QualityRater qualityRater2 = (QualityRater) Capturer.this.mNetworkQualityRater.get();
            if (qualityRater == null || qualityRater2 == null) {
                return;
            }
            int latestQuality = qualityRater.getLatestQuality();
            int latestQuality2 = qualityRater2.getLatestQuality();
            int i = this.mPresetIndex;
            if (latestQuality < 30) {
                int i2 = this.mLivePresets.get(i).bitrate / 3;
                for (int i3 = i - 1; i3 >= 0; i3--) {
                    if (this.mLivePresets.get(i3).bitrate <= i2 || i3 == 0) {
                        i = i3;
                        break;
                    }
                }
            } else if (latestQuality < 80 && latestQuality2 < 82) {
                i--;
            } else if (latestQuality >= 98) {
                i++;
            } else if (latestQuality2 >= 98) {
                if (latestQuality < 70) {
                    FrameHandler.Params params = this.mLivePresets.get(i);
                    for (int i4 = i - 1; i4 >= 0; i4--) {
                        FrameHandler.Params params2 = this.mLivePresets.get(i4);
                        if (params2.width < params.width || params2.height < params.height) {
                            Log.i(Capturer.LOGTAG, "cpu limited, lower resolution found");
                            i = i4;
                            break;
                        }
                    }
                }
                int i5 = i + 1;
                if (i5 < this.mLivePresets.size()) {
                    FrameHandler.Params params3 = this.mLivePresets.get(i);
                    FrameHandler.Params params4 = this.mLivePresets.get(i5);
                    if (params4.width == params3.width && params4.height == params3.height) {
                        Log.i(Capturer.LOGTAG, "selecting higher bitrate of same resolution");
                        i = i5;
                    }
                }
            }
            int min = Math.min(Math.max(0, i), this.mLivePresets.size() - 1);
            if (min == this.mPresetIndex) {
                return;
            }
            this.mPresetIndex = min;
            this.mFrameHandler.resetAutoEncoders(this.mLivePresets.get(this.mPresetIndex));
            this.mLastPresetChange = SystemClock.uptimeMillis();
        }

        private void restartAudio(final AudioRestartCallback audioRestartCallback) {
            final EncodeInterface.AudioFormat onGetAudioFormat = this.mEncodeObserver != null ? this.mEncodeObserver.onGetAudioFormat() : EncodeInterface.AudioFormat.NONE;
            boolean z = false;
            if (onGetAudioFormat == EncodeInterface.AudioFormat.NONE) {
                Capturer.this.mAudioCapturer.stopAudioCapture();
                if (this.mAudioDataHandler != null) {
                    this.mAudioDataHandler.close();
                }
                this.mAudioDataHandler = null;
                if (audioRestartCallback != null) {
                    audioRestartCallback.onAudioSettingApplied();
                }
                synchronized (Capturer.this) {
                    Capturer.this.mSampleRate = 0;
                }
                return;
            }
            int onGetAudioSampleRate = this.mEncodeObserver != null ? this.mEncodeObserver.onGetAudioSampleRate() : 16000;
            Bundle bundle = new Bundle();
            if (this.mEncodeObserver != null && this.mEncodeObserver.onCanWriteLocal()) {
                z = true;
            }
            bundle.putBoolean("enforce_sample_rate", z);
            bundle.putInt("sample_rate", onGetAudioSampleRate);
            bundle.putInt("buffer_size", AudioDataHandler.getRawBufferSize(onGetAudioFormat));
            bundle.putString("camera_id", this.mEncodeObserver != null ? this.mEncodeObserver.onGetCameraId() : AppEventsConstants.EVENT_PARAM_VALUE_NO);
            Capturer.this.mAudioCapturer.setupAudioCapture(bundle, new AudioCapturer.InitCallback() { // from class: com.bambuser.broadcaster.Capturer.EncoderHandler.1
                @Override // com.bambuser.broadcaster.AudioCapturer.InitCallback
                public void onAudioRecorderReady(int i) {
                    Capturer.this.mEncoderHandler.obtainMessage(7, i, 0, new Object[]{onGetAudioFormat, audioRestartCallback}).sendToTarget();
                    synchronized (Capturer.this) {
                        Capturer.this.mSampleRate = i;
                    }
                    synchronized (Capturer.this.mTalkbackQueue) {
                        Capturer.this.mTalkbackQueue.clear();
                        Capturer.this.mTalkbackQueueSize = 0;
                    }
                }
            });
        }

        private void playMediaActionSound(int i) {
            try {
                if (this.mEncodeObserver == null || this.mEncodeObserver.onGetCaptureSounds()) {
                    if (this.mMediaActionSound == null) {
                        this.mMediaActionSound = new MediaActionSound();
                    }
                    this.mMediaActionSound.play(i);
                }
            } catch (Exception e) {
                Log.v(Capturer.LOGTAG, "Exception in playMediaActionSound: " + e);
            }
        }
    }

    @Override // com.bambuser.broadcaster.TalkbackController.AudioReceiver
    public synchronized void onDecodedAudio(ByteBuffer byteBuffer, int i, int i2) {
        if (this.mTalkbackResampler == null || this.mTalkbackResampler.getOutputRate() != this.mSampleRate || this.mTalkbackResampler.getInputRate() != i || this.mTalkbackResampler.getChannels() != i2) {
            if (this.mTalkbackResampler != null) {
                this.mTalkbackResampler.close();
            }
            this.mTalkbackResampler = new Resampler(i, this.mSampleRate, i2);
        }
        byte[] bArr = new byte[((((byteBuffer.limit() / 2) / i2) * ((this.mSampleRate + i) - 1)) / i) * 2 * i2];
        int resample = this.mTalkbackResampler.resample(byteBuffer.array(), 0, byteBuffer.limit(), bArr, 0, false);
        this.mTalkbackResampler.getBytesConsumed();
        if (i2 > 1) {
            NativeUtils.mixDown(bArr, (resample / 2) / i2, i2);
            resample /= i2;
        }
        synchronized (this.mTalkbackQueue) {
            this.mTalkbackQueue.add(ByteBuffer.wrap(bArr, 0, resample));
            this.mTalkbackQueueSize += resample;
            int i3 = ((this.mSampleRate * 1200) * 2) / 1000;
            if (this.mTalkbackQueueSize > i3) {
                int i4 = (i3 + (((this.mSampleRate * ItemTouchHelper.Callback.DEFAULT_DRAG_ANIMATION_DURATION) * 2) / 1000)) / 2;
                while (this.mTalkbackQueueSize > i4) {
                    this.mTalkbackQueueSize -= this.mTalkbackQueue.removeFirst().remaining();
                }
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void mixTalkback(AudioWrapper audioWrapper) {
        synchronized (this.mTalkbackQueue) {
            byte[] bArr = this.mEncodedAudioChannelsCount == 2 ? new byte[audioWrapper.mIndex * 2] : null;
            int i = 0;
            while (true) {
                if (i >= audioWrapper.mIndex) {
                    break;
                }
                int i2 = ((this.mSampleRate * ItemTouchHelper.Callback.DEFAULT_DRAG_ANIMATION_DURATION) * 2) / 1000;
                if (this.mTalkbackDropping && this.mTalkbackQueueSize >= i2) {
                    this.mTalkbackDropping = false;
                }
                ByteBuffer peekFirst = !this.mTalkbackDropping ? this.mTalkbackQueue.peekFirst() : null;
                if (peekFirst == null) {
                    if (this.mEncodedAudioChannelsCount == 2) {
                        NativeUtils.mixStereo(bArr, i * 2, audioWrapper.mBuffer, i, null, 0, (audioWrapper.mIndex - i) / 2);
                    }
                    this.mTalkbackDropping = true;
                } else {
                    int remaining = peekFirst.remaining();
                    if (remaining > audioWrapper.mIndex - i) {
                        remaining = audioWrapper.mIndex - i;
                    }
                    int i3 = remaining;
                    if (this.mEncodedAudioChannelsCount == 2) {
                        NativeUtils.mixStereo(bArr, i * 2, audioWrapper.mBuffer, i, peekFirst.array(), peekFirst.position(), i3 / 2);
                    } else {
                        NativeUtils.mix(audioWrapper.mBuffer, i, peekFirst.array(), peekFirst.position(), i3 / 2);
                    }
                    peekFirst.position(peekFirst.position() + i3);
                    this.mTalkbackQueueSize -= i3;
                    i += i3;
                    if (peekFirst.remaining() != 0) {
                        break;
                    }
                    this.mTalkbackQueue.removeFirst();
                }
            }
            if (this.mEncodedAudioChannelsCount == 2) {
                if (this.mAudioCapturer != null) {
                    this.mAudioCapturer.reuseWrapper(audioWrapper);
                }
                audioWrapper.mIndex = bArr.length;
                audioWrapper.mBuffer = bArr;
            }
        }
    }
}
