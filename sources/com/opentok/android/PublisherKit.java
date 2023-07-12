package com.opentok.android;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import com.opentok.android.BaseAudioDevice;
import com.opentok.android.BaseVideoCapturer;
import com.opentok.android.BaseVideoRenderer;
import com.opentok.android.OpentokError;
import com.opentok.android.OtLog;
import com.opentok.android.Session;
import com.opentok.otc.SWIGTYPE_p_otc_publisher;
import com.opentok.otc.SWIGTYPE_p_otc_stream;
import com.opentok.otc.a;
import com.opentok.otc.e;
import com.opentok.otc.i;
import com.opentok.otc.otc_publisher_callbacks;
import com.opentok.otc.otc_publisher_rtc_stats_report_cb;
import com.opentok.otc.otc_video_capturer_callbacks;
import com.opentok.otc.otc_video_capturer_settings;
import java.util.HashMap;
import java.util.Map;
import java.util.Observable;
import java.util.Observer;

/* loaded from: classes.dex */
public class PublisherKit implements Observer {
    private static final Map<PublisherKitVideoType, i> OTCPublisherVideoTypeMap;
    private static final Map<i, PublisherKitVideoType> PublisherKitVideoTypeMap;
    protected AudioLevelListener audioLevelListener;
    protected AudioStatsListener audioStatsListener;
    protected BaseVideoCapturer capturer;
    swig_otc_video_capturer_cb capturer_cb;
    protected Context context;
    protected Handler handler;
    boolean isPaused;
    private final OtLog.LogToken log;
    protected MuteListener muteListener;
    long otcCapturerHandle;
    SWIGTYPE_p_otc_publisher otc_publisher;
    protected PublisherListener publisherListener;
    swig_otc_publisher_cb publisher_cb;
    protected BaseVideoRenderer renderer;
    boolean resumePublishVideoOnSessionResume;
    protected PublisherRtcStatsReportListener rtcStatsReportListener;
    swig_otc_rtc_stats_report_cb rtc_stats_report_cb;
    protected VideoStatsListener videoStatsListener;

    /* loaded from: classes.dex */
    public interface AudioLevelListener {
        void onAudioLevelUpdated(PublisherKit publisherKit, float f);
    }

    /* loaded from: classes.dex */
    public interface AudioStatsListener {
        void onAudioStats(PublisherKit publisherKit, PublisherAudioStats[] publisherAudioStatsArr);
    }

    /* loaded from: classes.dex */
    public static class Builder {
        int audioBitrate;
        Context context;
        String name;
        boolean audioTrack = true;
        boolean videoTrack = true;
        boolean enableOpusDtx = false;
        BaseVideoCapturer capturer = null;
        BaseVideoRenderer renderer = null;

        public Builder(Context context) {
            this.context = context;
        }

        public Builder audioBitrate(int i) {
            this.audioBitrate = i;
            return this;
        }

        public Builder audioTrack(boolean z) {
            this.audioTrack = z;
            return this;
        }

        public PublisherKit build() {
            return new PublisherKit(this.context, this.name, this.audioTrack, this.audioBitrate, this.videoTrack, this.capturer, this.renderer, this.enableOpusDtx);
        }

        public Builder capturer(BaseVideoCapturer baseVideoCapturer) {
            this.capturer = baseVideoCapturer;
            return this;
        }

        public Builder name(String str) {
            this.name = str;
            return this;
        }

        public Builder renderer(BaseVideoRenderer baseVideoRenderer) {
            this.renderer = baseVideoRenderer;
            return this;
        }

        public Builder videoTrack(boolean z) {
            this.videoTrack = z;
            return this;
        }
    }

    /* loaded from: classes.dex */
    public interface MuteListener {
        void onMuteForced(PublisherKit publisherKit);
    }

    /* loaded from: classes.dex */
    public static class PublisherAudioStats {
        public long audioBytesSent;
        public long audioPacketsLost;
        public long audioPacketsSent;
        public String connectionId;
        public double startTime;
        public String subscriberId;
        public double timeStamp;

        PublisherAudioStats(String str, String str2, long j, long j2, long j3, double d, double d2) {
            this.connectionId = str;
            this.subscriberId = str2;
            this.audioPacketsLost = j;
            this.audioPacketsSent = j2;
            this.audioBytesSent = j3;
            this.timeStamp = d;
            this.startTime = d2;
        }
    }

    /* loaded from: classes.dex */
    public enum PublisherKitVideoType {
        PublisherKitVideoTypeCamera(1),
        PublisherKitVideoTypeScreen(2);
        
        private int videoType;

        PublisherKitVideoType(int i) {
            this.videoType = i;
        }

        static PublisherKitVideoType fromType(int i) {
            PublisherKitVideoType[] values;
            for (PublisherKitVideoType publisherKitVideoType : values()) {
                if (publisherKitVideoType.getVideoType() == i) {
                    return publisherKitVideoType;
                }
            }
            throw new IllegalArgumentException("unknown type " + i);
        }

        public int getVideoType() {
            return this.videoType;
        }
    }

    /* loaded from: classes.dex */
    public interface PublisherListener {
        void onError(PublisherKit publisherKit, OpentokError opentokError);

        void onStreamCreated(PublisherKit publisherKit, Stream stream);

        void onStreamDestroyed(PublisherKit publisherKit, Stream stream);
    }

    /* loaded from: classes.dex */
    public static class PublisherRtcStats {
        public String connectionId;
        public String jsonArrayOfReports;
    }

    /* loaded from: classes.dex */
    public interface PublisherRtcStatsReportListener {
        void onRtcStatsReport(PublisherKit publisherKit, PublisherRtcStats[] publisherRtcStatsArr);
    }

    /* loaded from: classes.dex */
    public static class PublisherVideoStats {
        public String connectionId;
        public double startTime;
        public String subscriberId;
        public double timeStamp;
        public long videoBytesSent;
        public long videoPacketsLost;
        public long videoPacketsSent;

        PublisherVideoStats(String str, String str2, long j, long j2, long j3, double d, double d2) {
            this.connectionId = str;
            this.subscriberId = str2;
            this.videoPacketsLost = j;
            this.videoPacketsSent = j2;
            this.videoBytesSent = j3;
            this.timeStamp = d;
            this.startTime = d2;
        }
    }

    /* loaded from: classes.dex */
    public interface VideoStatsListener {
        void onVideoStats(PublisherKit publisherKit, PublisherVideoStats[] publisherVideoStatsArr);
    }

    /* loaded from: classes.dex */
    static class swig_otc_publisher_cb extends otc_publisher_callbacks {
        public swig_otc_publisher_cb(long j) {
            super(j, true);
        }

        public long getCPointer() {
            return otc_publisher_callbacks.getCPtr(this);
        }
    }

    /* loaded from: classes.dex */
    static class swig_otc_rtc_stats_report_cb extends otc_publisher_rtc_stats_report_cb {
        public swig_otc_rtc_stats_report_cb(long j) {
            super(j, true);
        }

        public long getCPointer() {
            return otc_publisher_rtc_stats_report_cb.getCPtr(this);
        }
    }

    /* loaded from: classes.dex */
    static class swig_otc_video_capturer_cb extends otc_video_capturer_callbacks {
        public swig_otc_video_capturer_cb(long j) {
            super(j, true);
        }

        public long getCPointer() {
            return otc_video_capturer_callbacks.getCPtr(this);
        }
    }

    /* loaded from: classes.dex */
    static class swig_otc_video_capturer_settings extends otc_video_capturer_settings {
        public swig_otc_video_capturer_settings(long j, boolean z) {
            super(j, z);
        }

        public void updateValuesWithSettings(BaseVideoCapturer.CaptureSettings captureSettings) {
            setExpected_delay(captureSettings.expectedDelay);
            setFormat(captureSettings.format);
            setFps(captureSettings.fps);
            setHeight(captureSettings.height);
            setWidth(captureSettings.width);
            setMirror_on_local_render(Utils.booleanToInt(captureSettings.mirrorInLocalRender));
        }
    }

    static {
        Loader.load();
        OTCPublisherVideoTypeMap = new HashMap<PublisherKitVideoType, i>() { // from class: com.opentok.android.PublisherKit.1
            {
                put(PublisherKitVideoType.PublisherKitVideoTypeCamera, i.c);
                put(PublisherKitVideoType.PublisherKitVideoTypeScreen, i.d);
            }
        };
        PublisherKitVideoTypeMap = new HashMap<i, PublisherKitVideoType>() { // from class: com.opentok.android.PublisherKit.2
            {
                put(i.c, PublisherKitVideoType.PublisherKitVideoTypeCamera);
                put(i.d, PublisherKitVideoType.PublisherKitVideoTypeScreen);
            }
        };
    }

    @Deprecated
    public PublisherKit(Context context) {
        this(context, null, true, 0, true, null, null, false);
    }

    @Deprecated
    public PublisherKit(Context context, String str) {
        this(context, str, true, 0, true, null, null, false);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public PublisherKit(Context context, String str, boolean z, int i, boolean z2, BaseVideoCapturer baseVideoCapturer, BaseVideoRenderer baseVideoRenderer, boolean z3) {
        this.isPaused = false;
        this.otcCapturerHandle = 0L;
        this.log = new OtLog.LogToken(this);
        if (context == null) {
            throw new IllegalArgumentException("(Context context) is null, cannot create publisher object!");
        }
        this.context = context;
        if (AudioDeviceManager.getAudioDevice() == null) {
            DefaultAudioDevice defaultAudioDevice = new DefaultAudioDevice(context);
            defaultAudioDevice.setAudioBus(new BaseAudioDevice.AudioBus(defaultAudioDevice));
            AudioDeviceManager.setAudioDevice(defaultAudioDevice);
        }
        Utils.initOtcEngine(context, AudioDeviceManager.getAudioDevice(), OpenTokConfig.getJNILogs(), OpenTokConfig.getOTKitLogs(), OpenTokConfig.getWebRTCLogs(), OpenTokConfig.getDumpClientLoggingToFile());
        this.handler = new Handler(Looper.myLooper());
        this.renderer = baseVideoRenderer;
        this.capturer = baseVideoCapturer;
        this.publisher_cb = new swig_otc_publisher_cb(build_native_publisher_cb());
        this.capturer_cb = new swig_otc_video_capturer_cb(build_native_video_capturer_cb());
        a b = e.b();
        e.a(b, Utils.booleanToInt(z));
        e.c(b, Utils.booleanToInt(z2));
        e.a(b, str == null ? "" : str);
        e.a(b, this.capturer_cb);
        e.b(b, Utils.booleanToInt(z3));
        SWIGTYPE_p_otc_publisher a = e.a(this.publisher_cb, b);
        this.otc_publisher = a;
        e.a(a, i);
        e.a(b);
    }

    @Deprecated
    public PublisherKit(Context context, String str, boolean z, boolean z2) {
        this(context, str, z, 0, z2, null, null, false);
    }

    @Deprecated
    protected PublisherKit(Context context, String str, boolean z, boolean z2, BaseVideoCapturer baseVideoCapturer, BaseVideoRenderer baseVideoRenderer) {
        this(context, str, z, 0, z2, baseVideoCapturer, baseVideoRenderer, false);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void a() {
        BaseVideoCapturer baseVideoCapturer = this.capturer;
        if (baseVideoCapturer != null) {
            baseVideoCapturer.destroy();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void a(PublisherAudioStats[] publisherAudioStatsArr) {
        AudioStatsListener audioStatsListener = this.audioStatsListener;
        if (audioStatsListener != null) {
            audioStatsListener.onAudioStats(this, publisherAudioStatsArr);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void a(PublisherVideoStats[] publisherVideoStatsArr) {
        VideoStatsListener videoStatsListener = this.videoStatsListener;
        if (videoStatsListener != null) {
            videoStatsListener.onVideoStats(this, publisherVideoStatsArr);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void b() {
        BaseVideoCapturer baseVideoCapturer = this.capturer;
        if (baseVideoCapturer != null) {
            baseVideoCapturer.init();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void b(float f) {
        AudioLevelListener audioLevelListener = this.audioLevelListener;
        if (audioLevelListener != null) {
            audioLevelListener.onAudioLevelUpdated(this, f);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void b(OpentokError opentokError) {
        PublisherListener publisherListener = this.publisherListener;
        if (publisherListener != null) {
            publisherListener.onError(this, opentokError);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void b(Stream stream) {
        PublisherListener publisherListener = this.publisherListener;
        if (publisherListener != null) {
            publisherListener.onStreamCreated(this, stream);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void b(PublisherRtcStats[] publisherRtcStatsArr) {
        PublisherRtcStatsReportListener publisherRtcStatsReportListener = this.rtcStatsReportListener;
        if (publisherRtcStatsReportListener != null) {
            publisherRtcStatsReportListener.onRtcStatsReport(this, publisherRtcStatsArr);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void c() {
        BaseVideoCapturer baseVideoCapturer = this.capturer;
        if (baseVideoCapturer != null) {
            baseVideoCapturer.startCapture();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void d() {
        BaseVideoCapturer baseVideoCapturer = this.capturer;
        if (baseVideoCapturer != null) {
            baseVideoCapturer.stopCapture();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void d(Stream stream) {
        PublisherListener publisherListener = this.publisherListener;
        if (publisherListener != null) {
            publisherListener.onStreamDestroyed(this, stream);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void f() {
        MuteListener muteListener = this.muteListener;
        if (muteListener != null) {
            muteListener.onMuteForced(this);
        }
    }

    native long build_native_publisher_cb();

    native long build_native_video_capturer_cb();

    native long build_rtc_stats_report_cb();

    @Deprecated
    public void destroy() {
        this.log.d("Destroy(...) called", new Object[0]);
    }

    native void destroy_native_publisher_cbs(long j, long j2, long j3);

    protected void finalize() {
        this.log.d("finalize()", new Object[0]);
        SWIGTYPE_p_otc_publisher sWIGTYPE_p_otc_publisher = this.otc_publisher;
        if (sWIGTYPE_p_otc_publisher != null) {
            e.a(sWIGTYPE_p_otc_publisher);
            this.otc_publisher = null;
            swig_otc_publisher_cb swig_otc_publisher_cbVar = this.publisher_cb;
            if (swig_otc_publisher_cbVar != null && this.capturer_cb != null) {
                destroy_native_publisher_cbs(swig_otc_publisher_cbVar.getCPointer(), this.capturer_cb.getCPointer(), this.rtc_stats_report_cb.getCPointer());
            }
        }
        super.finalize();
    }

    public boolean getAudioFallbackEnabled() {
        return Utils.intToBoolean(e.b(this.otc_publisher));
    }

    public BaseVideoCapturer getCapturer() {
        return this.capturer;
    }

    public String getName() {
        return e.c(this.otc_publisher);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public SWIGTYPE_p_otc_publisher getOtcPublisher() {
        return this.otc_publisher;
    }

    public boolean getPublishAudio() {
        this.log.d("getPublishAudio() called", new Object[0]);
        return Utils.intToBoolean(e.d(this.otc_publisher));
    }

    public boolean getPublishVideo() {
        this.log.d("getPublishVideo() called", new Object[0]);
        return Utils.intToBoolean(e.e(this.otc_publisher));
    }

    public PublisherKitVideoType getPublisherVideoType() {
        return PublisherKitVideoTypeMap.get(e.i(this.otc_publisher));
    }

    public BaseVideoRenderer getRenderer() {
        return this.renderer;
    }

    public void getRtcStatsReport() {
        e.f(this.otc_publisher);
    }

    public Session getSession() {
        return new Session(e.g(this.otc_publisher), false);
    }

    public Stream getStream() {
        SWIGTYPE_p_otc_stream h = e.h(this.otc_publisher);
        if (h == null) {
            return null;
        }
        return new Stream(h, false);
    }

    public View getView() {
        if (getRenderer() != null) {
            return this.renderer.getView();
        }
        return null;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    /* renamed from: onAudioLevelUpdated */
    public void a(float f) {
    }

    void onAudioLevelUpdatedJNI(final float f) {
        this.handler.post(new Runnable() { // from class: com.opentok.android.-$$Lambda$PublisherKit$a-OXyQHzYYuaxF_sahH2dn8BBxw
            @Override // java.lang.Runnable
            public final void run() {
                PublisherKit.this.a(f);
            }
        });
        if (this.audioLevelListener != null) {
            this.handler.post(new Runnable() { // from class: com.opentok.android.-$$Lambda$PublisherKit$p9n3Y2HLKeztsC82fsL1PHH6sGk
                @Override // java.lang.Runnable
                public final void run() {
                    PublisherKit.this.b(f);
                }
            });
        }
    }

    void onAudioStatsJNI(final PublisherAudioStats[] publisherAudioStatsArr) {
        if (this.audioStatsListener != null) {
            this.handler.post(new Runnable() { // from class: com.opentok.android.-$$Lambda$PublisherKit$oroF737pMyFnAMKztev-7FZXA0M
                @Override // java.lang.Runnable
                public final void run() {
                    PublisherKit.this.a(publisherAudioStatsArr);
                }
            });
        }
    }

    boolean onCaptureDestroyJNI(long j) {
        this.log.d("onCaptureDestroy(...) called", new Object[0]);
        this.otcCapturerHandle = 0L;
        if (this.capturer != null) {
            this.handler.post(new Runnable() { // from class: com.opentok.android.-$$Lambda$PublisherKit$NKkXdIS5XOTBE3GOcXFDJzXmRz8
                @Override // java.lang.Runnable
                public final void run() {
                    PublisherKit.this.a();
                }
            });
            return true;
        }
        return true;
    }

    boolean onCaptureInitJNI(long j) {
        this.log.d("onCaptureInit(...) called", new Object[0]);
        this.otcCapturerHandle = j;
        if (this.capturer != null) {
            this.handler.post(new Runnable() { // from class: com.opentok.android.-$$Lambda$PublisherKit$QrWBua3wKivUVxDdHxeqs1viClE
                @Override // java.lang.Runnable
                public final void run() {
                    PublisherKit.this.b();
                }
            });
            return true;
        }
        return true;
    }

    boolean onCaptureSettingsJNI(long j) {
        this.log.d("onCaptureSettings(...) called", new Object[0]);
        if (this.capturer != null) {
            new swig_otc_video_capturer_settings(j, false).updateValuesWithSettings(this.capturer.getCaptureSettings());
            return true;
        }
        return false;
    }

    boolean onCaptureStartJNI(long j) {
        this.log.d("onCaptureStart(...) called", new Object[0]);
        if (this.capturer != null) {
            this.handler.post(new Runnable() { // from class: com.opentok.android.-$$Lambda$PublisherKit$FeEivHWrXoDIA6wdKOT7aEDHxEw
                @Override // java.lang.Runnable
                public final void run() {
                    PublisherKit.this.c();
                }
            });
            return true;
        }
        return true;
    }

    boolean onCaptureStopJNI(long j) {
        this.log.d("onCaptureStop(...) called", new Object[0]);
        if (this.capturer != null) {
            this.handler.post(new Runnable() { // from class: com.opentok.android.-$$Lambda$PublisherKit$MYuHCXsJa4pBi8xenH6eML8tPj4
                @Override // java.lang.Runnable
                public final void run() {
                    PublisherKit.this.d();
                }
            });
            return true;
        }
        return true;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    /* renamed from: onError */
    public void a(OpentokError opentokError) {
    }

    void onErrorJNI(String str, int i) {
        final OpentokError opentokError = new OpentokError(OpentokError.Domain.PublisherErrorDomain, i, str);
        this.handler.post(new Runnable() { // from class: com.opentok.android.-$$Lambda$PublisherKit$oarRg3_dHRlX3jHth_bHZdo2pYU
            @Override // java.lang.Runnable
            public final void run() {
                PublisherKit.this.a(opentokError);
            }
        });
        if (this.publisherListener != null) {
            this.handler.post(new Runnable() { // from class: com.opentok.android.-$$Lambda$PublisherKit$LNKcKYs_lYepbc61LUV3zAM6Hfk
                @Override // java.lang.Runnable
                public final void run() {
                    PublisherKit.this.b(opentokError);
                }
            });
        }
    }

    void onFrameJNI(long j) {
        BaseVideoRenderer baseVideoRenderer = this.renderer;
        if (baseVideoRenderer != null) {
            BaseVideoRenderer.Frame buildFrameInstance = baseVideoRenderer.buildFrameInstance();
            buildFrameInstance.copyOtcFrame(j, Boolean.valueOf(this.capturer.getCaptureSettings().mirrorInLocalRender));
            this.renderer.onFrame(buildFrameInstance);
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    /* renamed from: onMuteForced */
    public void e() {
    }

    void onMuteForcedJNI() {
        this.log.d("onMuteForcedJNI() called", new Object[0]);
        this.handler.post(new Runnable() { // from class: com.opentok.android.-$$Lambda$PublisherKit$2awStqjfU-nnD-5I2xxamHqWF-Q
            @Override // java.lang.Runnable
            public final void run() {
                PublisherKit.this.e();
            }
        });
        if (this.muteListener != null) {
            this.handler.post(new Runnable() { // from class: com.opentok.android.-$$Lambda$PublisherKit$b_AulPpPWSOVZyYJs4JP2Rcfu0I
                @Override // java.lang.Runnable
                public final void run() {
                    PublisherKit.this.f();
                }
            });
        }
    }

    public void onPause() {
        this.log.d("onPause() called", new Object[0]);
        BaseVideoRenderer baseVideoRenderer = this.renderer;
        if (baseVideoRenderer != null) {
            baseVideoRenderer.onPause();
        }
        BaseVideoCapturer baseVideoCapturer = this.capturer;
        if (baseVideoCapturer != null) {
            baseVideoCapturer.onPause();
        }
        this.resumePublishVideoOnSessionResume = getPublishVideo();
        setPublishVideo(false);
        this.isPaused = true;
    }

    public void onRestart() {
        BaseVideoCapturer capturer = getCapturer();
        if (capturer != null) {
            capturer.onRestart();
        }
    }

    public void onResume() {
        this.log.d("onResume() called", new Object[0]);
        if (this.isPaused) {
            this.isPaused = false;
            BaseVideoRenderer baseVideoRenderer = this.renderer;
            if (baseVideoRenderer != null) {
                baseVideoRenderer.onResume();
            }
            BaseVideoCapturer baseVideoCapturer = this.capturer;
            if (baseVideoCapturer != null) {
                baseVideoCapturer.onResume();
            }
            setPublishVideo(this.resumePublishVideoOnSessionResume);
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    /* renamed from: onRtcStatsReport */
    public void a(PublisherRtcStats[] publisherRtcStatsArr) {
    }

    void onRtcStatsReportsJNI(String[] strArr, String[] strArr2) {
        this.log.d("onRtcStatsReportsJNI(...) called", new Object[0]);
        if (strArr.length == 0) {
            this.log.e("onRtcStatsReportsJNI called with empty reports", new Object[0]);
            return;
        }
        final PublisherRtcStats[] publisherRtcStatsArr = new PublisherRtcStats[strArr.length];
        for (int i = 0; i < strArr.length; i++) {
            publisherRtcStatsArr[i] = new PublisherRtcStats();
            publisherRtcStatsArr[i].jsonArrayOfReports = strArr[i];
            publisherRtcStatsArr[i].connectionId = strArr2[i];
        }
        this.handler.post(new Runnable() { // from class: com.opentok.android.-$$Lambda$PublisherKit$n1LYGX_3vhRHIMMpGDfcmY5zafI
            @Override // java.lang.Runnable
            public final void run() {
                PublisherKit.this.a(publisherRtcStatsArr);
            }
        });
        if (this.rtcStatsReportListener != null) {
            this.handler.post(new Runnable() { // from class: com.opentok.android.-$$Lambda$PublisherKit$1fnLA3QArD-KRAqTlhhT67GQDxY
                @Override // java.lang.Runnable
                public final void run() {
                    PublisherKit.this.b(publisherRtcStatsArr);
                }
            });
        }
    }

    public void onStop() {
        BaseVideoCapturer capturer = getCapturer();
        if (capturer != null) {
            capturer.onStop();
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    /* renamed from: onStreamCreated */
    public void a(Stream stream) {
    }

    void onStreamCreatedJNI(long j) {
        this.log.d("onStreamCreated(...) called", new Object[0]);
        final Stream stream = new Stream(j, true);
        this.handler.post(new Runnable() { // from class: com.opentok.android.-$$Lambda$PublisherKit$sck0PBmb3EmBMJYcGs9Xb4W1TEU
            @Override // java.lang.Runnable
            public final void run() {
                PublisherKit.this.a(stream);
            }
        });
        if (this.publisherListener != null) {
            this.handler.post(new Runnable() { // from class: com.opentok.android.-$$Lambda$PublisherKit$XmmcKK6ibIxpvIrGS6U0D0T-ruE
                @Override // java.lang.Runnable
                public final void run() {
                    PublisherKit.this.b(stream);
                }
            });
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    /* renamed from: onStreamDestroyed */
    public void c(Stream stream) {
    }

    void onStreamDestroyedJNI(long j) {
        this.log.d("onStreamDestroyed(...) called", new Object[0]);
        final Stream stream = new Stream(j, true);
        this.handler.post(new Runnable() { // from class: com.opentok.android.-$$Lambda$PublisherKit$9UdhsrZx3DP0VSmXMEDgxHMnzqM
            @Override // java.lang.Runnable
            public final void run() {
                PublisherKit.this.c(stream);
            }
        });
        if (this.publisherListener != null) {
            this.handler.post(new Runnable() { // from class: com.opentok.android.-$$Lambda$PublisherKit$uTT7YFvbT6FC6cfgteo1HFipho4
                @Override // java.lang.Runnable
                public final void run() {
                    PublisherKit.this.d(stream);
                }
            });
        }
    }

    void onVideoStatsJNI(final PublisherVideoStats[] publisherVideoStatsArr) {
        if (this.videoStatsListener != null) {
            this.handler.post(new Runnable() { // from class: com.opentok.android.-$$Lambda$PublisherKit$7hvfr0Wc7KI0dO1gS_rzIqZDKng
                @Override // java.lang.Runnable
                public final void run() {
                    PublisherKit.this.a(publisherVideoStatsArr);
                }
            });
        }
    }

    public void setAudioFallbackEnabled(boolean z) {
        e.a(this.otc_publisher, Utils.booleanToInt(z));
    }

    public void setAudioLevelListener(AudioLevelListener audioLevelListener) {
        this.audioLevelListener = audioLevelListener;
    }

    public void setAudioStatsListener(AudioStatsListener audioStatsListener) {
        this.audioStatsListener = audioStatsListener;
    }

    @Deprecated
    public void setCapturer(BaseVideoCapturer baseVideoCapturer) {
    }

    public void setMuteListener(MuteListener muteListener) {
        this.muteListener = muteListener;
    }

    @Deprecated
    public void setName(String str) {
    }

    public void setPublishAudio(boolean z) {
        OtLog.LogToken logToken = this.log;
        logToken.d("setPublishAudio(" + z + ") called", new Object[0]);
        e.b(this.otc_publisher, Utils.booleanToInt(z));
    }

    public void setPublishVideo(boolean z) {
        OtLog.LogToken logToken = this.log;
        logToken.d("setPublishVideo(" + z + ") called", new Object[0]);
        e.c(this.otc_publisher, Utils.booleanToInt(z));
    }

    public void setPublisherListener(PublisherListener publisherListener) {
        this.publisherListener = publisherListener;
    }

    public void setPublisherVideoType(PublisherKitVideoType publisherKitVideoType) {
        e.a(this.otc_publisher, OTCPublisherVideoTypeMap.get(publisherKitVideoType));
    }

    @Deprecated
    public void setRenderer(BaseVideoRenderer baseVideoRenderer) {
    }

    public void setRtcStatsReportListener(PublisherRtcStatsReportListener publisherRtcStatsReportListener) {
        swig_otc_rtc_stats_report_cb swig_otc_rtc_stats_report_cbVar = new swig_otc_rtc_stats_report_cb(build_rtc_stats_report_cb());
        this.rtc_stats_report_cb = swig_otc_rtc_stats_report_cbVar;
        e.a(this.otc_publisher, swig_otc_rtc_stats_report_cbVar);
        this.rtcStatsReportListener = publisherRtcStatsReportListener;
    }

    public void setStyle(String str, String str2) {
        BaseVideoRenderer baseVideoRenderer = this.renderer;
        if (baseVideoRenderer != null) {
            baseVideoRenderer.setStyle(str, str2);
        }
    }

    public void setVideoStatsListener(VideoStatsListener videoStatsListener) {
        this.videoStatsListener = videoStatsListener;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void throwError(OpentokError opentokError) {
        onErrorJNI(opentokError.errorMessage, opentokError.errorCode.ordinal());
    }

    @Override // java.util.Observer
    public void update(Observable observable, Object obj) {
        if ((observable instanceof Session) && (obj instanceof Session.SessionPauseResumeEvent)) {
            if (((Session.SessionPauseResumeEvent) obj).isSessionPaused()) {
                onPause();
            } else {
                onResume();
            }
        }
    }
}
