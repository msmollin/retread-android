package com.opentok.android;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.util.SparseArray;
import android.view.View;
import com.opentok.android.BaseVideoRenderer;
import com.opentok.android.OpentokError;
import com.opentok.android.OtLog;
import com.opentok.android.Session;
import com.opentok.android.VideoUtils;
import com.opentok.otc.SWIGTYPE_p_otc_stream;
import com.opentok.otc.SWIGTYPE_p_otc_subscriber;
import com.opentok.otc.e;
import com.opentok.otc.g;
import com.opentok.otc.j;
import com.opentok.otc.otc_subscriber_callbacks;
import com.opentok.otc.otc_subscriber_rtc_stats_report_cb;
import java.util.Observable;
import java.util.Observer;

/* loaded from: classes.dex */
public class SubscriberKit implements Observer {
    public static final float NO_PREFERRED_FRAMERATE = Float.MAX_VALUE;
    public static final VideoUtils.Size NO_PREFERRED_RESOLUTION;
    public static final String VIDEO_REASON_CODEC_NOT_SUPPORTED = "codecNotSupported";
    public static final String VIDEO_REASON_PUBLISH_VIDEO = "publishVideo";
    public static final String VIDEO_REASON_QUALITY = "quality";
    public static final String VIDEO_REASON_SUBSCRIBE_TO_VIDEO = "subscribeToVideo";
    private static final SparseArray<String> VideoReasonMap;
    protected AudioLevelListener audioLevelListener;
    protected AudioStatsListener audioStatsListener;
    private Handler handler;
    boolean isPaused;
    private final OtLog.LogToken log;
    SWIGTYPE_p_otc_subscriber otc_subscriber;
    protected BaseVideoRenderer renderer;
    boolean resumeSubscribeToVideoOnSessionResume;
    protected SubscriberRtcStatsReportListener rtcStatsReportListener;
    swig_otc_rtc_stats_report_cb rtc_stats_report_cb;
    protected Session session;
    protected Stream stream;
    protected StreamListener streamListener;
    protected SubscriberListener subscriberListener;
    swig_otc_subscriber_cb subscriber_cb;
    protected VideoListener videoListener;
    protected VideoStatsListener videoStatsListener;

    /* loaded from: classes.dex */
    public interface AudioLevelListener {
        void onAudioLevelUpdated(SubscriberKit subscriberKit, float f);
    }

    /* loaded from: classes.dex */
    public interface AudioStatsListener {
        void onAudioStats(SubscriberKit subscriberKit, SubscriberAudioStats subscriberAudioStats);
    }

    /* loaded from: classes.dex */
    public static class Builder {
        Context context;
        BaseVideoRenderer renderer;
        Stream stream;

        public Builder(Context context, Stream stream) {
            this.context = context;
            this.stream = stream;
        }

        public SubscriberKit build() {
            return new SubscriberKit(this.context, this.stream, this.renderer);
        }

        public Builder renderer(BaseVideoRenderer baseVideoRenderer) {
            this.renderer = baseVideoRenderer;
            return this;
        }
    }

    /* loaded from: classes.dex */
    public interface StreamListener {
        default void onAudioDisabled(SubscriberKit subscriberKit) {
        }

        default void onAudioEnabled(SubscriberKit subscriberKit) {
        }

        void onDisconnected(SubscriberKit subscriberKit);

        void onReconnected(SubscriberKit subscriberKit);
    }

    /* loaded from: classes.dex */
    public static class SubscriberAudioStats {
        public int audioBytesReceived;
        public int audioPacketsLost;
        public int audioPacketsReceived;
        public double timeStamp;

        SubscriberAudioStats(int i, int i2, int i3, double d) {
            this.audioPacketsLost = i;
            this.audioPacketsReceived = i2;
            this.audioBytesReceived = i3;
            this.timeStamp = d;
        }
    }

    /* loaded from: classes.dex */
    public interface SubscriberListener {
        void onConnected(SubscriberKit subscriberKit);

        void onDisconnected(SubscriberKit subscriberKit);

        void onError(SubscriberKit subscriberKit, OpentokError opentokError);
    }

    /* loaded from: classes.dex */
    public interface SubscriberRtcStatsReportListener {
        void onRtcStatsReport(SubscriberKit subscriberKit, String str);
    }

    /* loaded from: classes.dex */
    public static class SubscriberVideoStats {
        public double timeStamp;
        public int videoBytesReceived;
        public int videoPacketsLost;
        public int videoPacketsReceived;

        SubscriberVideoStats(int i, int i2, int i3, double d) {
            this.videoPacketsLost = i;
            this.videoPacketsReceived = i2;
            this.videoBytesReceived = i3;
            this.timeStamp = d;
        }
    }

    /* loaded from: classes.dex */
    public interface VideoListener {
        void onVideoDataReceived(SubscriberKit subscriberKit);

        void onVideoDisableWarning(SubscriberKit subscriberKit);

        void onVideoDisableWarningLifted(SubscriberKit subscriberKit);

        void onVideoDisabled(SubscriberKit subscriberKit, String str);

        void onVideoEnabled(SubscriberKit subscriberKit, String str);
    }

    /* loaded from: classes.dex */
    public interface VideoStatsListener {
        void onVideoStats(SubscriberKit subscriberKit, SubscriberVideoStats subscriberVideoStats);
    }

    /* loaded from: classes.dex */
    static class swig_otc_rtc_stats_report_cb extends otc_subscriber_rtc_stats_report_cb {
        public swig_otc_rtc_stats_report_cb(long j) {
            super(j, true);
        }

        public long getCPointer() {
            return otc_subscriber_rtc_stats_report_cb.getCPtr(this);
        }
    }

    /* loaded from: classes.dex */
    static class swig_otc_subscriber_cb extends otc_subscriber_callbacks {
        public swig_otc_subscriber_cb(long j) {
            super(j, true);
        }

        public long getCPointer() {
            return otc_subscriber_callbacks.getCPtr(this);
        }
    }

    static {
        Loader.load();
        NO_PREFERRED_RESOLUTION = new VideoUtils.Size(Integer.MAX_VALUE, Integer.MAX_VALUE);
        VideoReasonMap = new SparseArray<String>() { // from class: com.opentok.android.SubscriberKit.1
            {
                append(1, SubscriberKit.VIDEO_REASON_PUBLISH_VIDEO);
                append(2, SubscriberKit.VIDEO_REASON_SUBSCRIBE_TO_VIDEO);
                append(3, SubscriberKit.VIDEO_REASON_QUALITY);
                append(4, SubscriberKit.VIDEO_REASON_CODEC_NOT_SUPPORTED);
            }
        };
    }

    @Deprecated
    public SubscriberKit(Context context, Stream stream) {
        this(context, stream, null);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public SubscriberKit(Context context, Stream stream, BaseVideoRenderer baseVideoRenderer) {
        this.resumeSubscribeToVideoOnSessionResume = false;
        this.isPaused = false;
        this.log = new OtLog.LogToken(this);
        this.handler = new Handler(Looper.myLooper());
        this.renderer = baseVideoRenderer;
        this.session = null;
        this.subscriber_cb = new swig_otc_subscriber_cb(build_native_subscriber_cb());
        SWIGTYPE_p_otc_subscriber a = e.a(stream.getOtc_stream(), this.subscriber_cb);
        this.otc_subscriber = a;
        e.c(a, j.d.a());
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void a(SubscriberAudioStats subscriberAudioStats) {
        AudioStatsListener audioStatsListener = this.audioStatsListener;
        if (audioStatsListener != null) {
            audioStatsListener.onAudioStats(this, subscriberAudioStats);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void a(SubscriberVideoStats subscriberVideoStats) {
        VideoStatsListener videoStatsListener = this.videoStatsListener;
        if (videoStatsListener != null) {
            videoStatsListener.onVideoStats(this, subscriberVideoStats);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void b() {
        StreamListener streamListener = this.streamListener;
        if (streamListener != null) {
            streamListener.onAudioDisabled(this);
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
        SubscriberListener subscriberListener = this.subscriberListener;
        if (subscriberListener != null) {
            subscriberListener.onError(this, opentokError);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void b(String str) {
        SubscriberRtcStatsReportListener subscriberRtcStatsReportListener = this.rtcStatsReportListener;
        if (subscriberRtcStatsReportListener != null) {
            subscriberRtcStatsReportListener.onRtcStatsReport(this, str);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void d() {
        StreamListener streamListener = this.streamListener;
        if (streamListener != null) {
            streamListener.onAudioEnabled(this);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void d(String str) {
        VideoListener videoListener = this.videoListener;
        if (videoListener != null) {
            videoListener.onVideoDisabled(this, str);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void f() {
        SubscriberListener subscriberListener = this.subscriberListener;
        if (subscriberListener != null) {
            subscriberListener.onConnected(this);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void f(String str) {
        VideoListener videoListener = this.videoListener;
        if (videoListener != null) {
            videoListener.onVideoEnabled(this, str);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void h() {
        StreamListener streamListener = this.streamListener;
        if (streamListener != null) {
            streamListener.onDisconnected(this);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void j() {
        StreamListener streamListener = this.streamListener;
        if (streamListener != null) {
            streamListener.onReconnected(this);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void l() {
        VideoListener videoListener = this.videoListener;
        if (videoListener != null) {
            videoListener.onVideoDataReceived(this);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void n() {
        VideoListener videoListener = this.videoListener;
        if (videoListener != null) {
            videoListener.onVideoDisableWarning(this);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void p() {
        VideoListener videoListener = this.videoListener;
        if (videoListener != null) {
            videoListener.onVideoDisableWarningLifted(this);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void q() {
        BaseVideoRenderer baseVideoRenderer = this.renderer;
        if (baseVideoRenderer != null) {
            baseVideoRenderer.onVideoPropertiesChanged(false);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void r() {
        BaseVideoRenderer baseVideoRenderer = this.renderer;
        if (baseVideoRenderer != null) {
            baseVideoRenderer.onVideoPropertiesChanged(true);
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void attachToSession(Session session) {
        this.session = session;
    }

    native long build_native_subscriber_cb();

    native long build_rtc_stats_report_cb();

    @Deprecated
    public void destroy() {
        this.log.d("Destroy(...) called", new Object[0]);
    }

    native void destroy_native_subscriber_cb(long j, long j2);

    /* JADX INFO: Access modifiers changed from: protected */
    public void detachFromSession(Session session) {
        this.session = null;
    }

    void error(SubscriberKit subscriberKit, int i, String str) {
        throwError(new OpentokError(OpentokError.Domain.SubscriberErrorDomain, i, str));
    }

    protected void finalize() {
        this.log.d("finalize()", new Object[0]);
        SWIGTYPE_p_otc_subscriber sWIGTYPE_p_otc_subscriber = this.otc_subscriber;
        if (sWIGTYPE_p_otc_subscriber != null) {
            e.a(sWIGTYPE_p_otc_subscriber);
            this.otc_subscriber = null;
            swig_otc_subscriber_cb swig_otc_subscriber_cbVar = this.subscriber_cb;
            if (swig_otc_subscriber_cbVar != null) {
                destroy_native_subscriber_cb(swig_otc_subscriber_cbVar.getCPointer(), this.rtc_stats_report_cb.getCPointer());
            }
        }
        super.finalize();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public SWIGTYPE_p_otc_subscriber getOtcSubscriber() {
        return this.otc_subscriber;
    }

    public float getPreferredFrameRate() {
        float[] fArr = new float[1];
        if (e.a(this.otc_subscriber, fArr) == g.c.a()) {
            return fArr[0];
        }
        return -1.0f;
    }

    public VideoUtils.Size getPreferredResolution() {
        long[] jArr = new long[1];
        long[] jArr2 = new long[1];
        return e.a(this.otc_subscriber, jArr, jArr2) == g.c.a() ? new VideoUtils.Size((int) jArr[0], (int) jArr2[0]) : NO_PREFERRED_RESOLUTION;
    }

    public BaseVideoRenderer getRenderer() {
        return this.renderer;
    }

    public void getRtcStatsReport() {
        e.b(this.otc_subscriber);
    }

    public Session getSession() {
        return new Session(e.c(this.otc_subscriber), false);
    }

    public Stream getStream() {
        SWIGTYPE_p_otc_stream d = e.d(this.otc_subscriber);
        if (d == null) {
            return null;
        }
        return new Stream(d, false);
    }

    public boolean getSubscribeToAudio() {
        return Utils.intToBoolean(e.e(this.otc_subscriber));
    }

    public boolean getSubscribeToVideo() {
        return Utils.intToBoolean(e.f(this.otc_subscriber));
    }

    public View getView() {
        BaseVideoRenderer baseVideoRenderer = this.renderer;
        if (baseVideoRenderer != null) {
            return baseVideoRenderer.getView();
        }
        return null;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    /* renamed from: onAudioDisabled */
    public void a() {
    }

    void onAudioDisabledJNI() {
        this.log.i("Stream: %s has audio disabled", getStream().getStreamId());
        this.handler.post(new Runnable() { // from class: com.opentok.android.-$$Lambda$SubscriberKit$A0nbt-FMvWqLTDOOOoCFxhPhvWE
            @Override // java.lang.Runnable
            public final void run() {
                SubscriberKit.this.a();
            }
        });
        if (this.streamListener != null) {
            this.handler.post(new Runnable() { // from class: com.opentok.android.-$$Lambda$SubscriberKit$qmdNKy7qfcAK-7ze9Hyp3JSIXA0
                @Override // java.lang.Runnable
                public final void run() {
                    SubscriberKit.this.b();
                }
            });
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    /* renamed from: onAudioEnabled */
    public void c() {
    }

    void onAudioEnabledJNI() {
        this.log.i("Stream: %s has audio enabled", getStream().getStreamId());
        this.handler.post(new Runnable() { // from class: com.opentok.android.-$$Lambda$SubscriberKit$gb9fp5v_1VUyMYfR98ipVAmE-T0
            @Override // java.lang.Runnable
            public final void run() {
                SubscriberKit.this.c();
            }
        });
        if (this.streamListener != null) {
            this.handler.post(new Runnable() { // from class: com.opentok.android.-$$Lambda$SubscriberKit$iTva-4MhAglOu4guyTYqSIzUbFk
                @Override // java.lang.Runnable
                public final void run() {
                    SubscriberKit.this.d();
                }
            });
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    /* renamed from: onAudioLevelUpdated */
    public void a(float f) {
    }

    void onAudioLevelUpdatedJNI(final float f) {
        this.handler.post(new Runnable() { // from class: com.opentok.android.-$$Lambda$SubscriberKit$H_5yBWwdCzHE5s4QjZaYiS_GkXo
            @Override // java.lang.Runnable
            public final void run() {
                SubscriberKit.this.a(f);
            }
        });
        if (this.audioLevelListener != null) {
            this.handler.post(new Runnable() { // from class: com.opentok.android.-$$Lambda$SubscriberKit$dayTN8s2WNDvEf8rkQbWnS0zyfQ
                @Override // java.lang.Runnable
                public final void run() {
                    SubscriberKit.this.b(f);
                }
            });
        }
    }

    void onAudioStatsJNI(final SubscriberAudioStats subscriberAudioStats) {
        if (this.audioStatsListener != null) {
            this.handler.post(new Runnable() { // from class: com.opentok.android.-$$Lambda$SubscriberKit$-9pt5ep_MxxSEqTi09LWifPZItY
                @Override // java.lang.Runnable
                public final void run() {
                    SubscriberKit.this.a(subscriberAudioStats);
                }
            });
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    /* renamed from: onConnected */
    public void e() {
    }

    void onConnectedJNI() {
        this.log.d("onConnected(...) called", new Object[0]);
        this.handler.post(new Runnable() { // from class: com.opentok.android.-$$Lambda$SubscriberKit$quLW3NMHVRAOzK4bY13yThHsb3U
            @Override // java.lang.Runnable
            public final void run() {
                SubscriberKit.this.e();
            }
        });
        if (this.subscriberListener != null) {
            this.handler.post(new Runnable() { // from class: com.opentok.android.-$$Lambda$SubscriberKit$SKmj8sU-0NO-ZzYOv9Wwt_yy6ts
                @Override // java.lang.Runnable
                public final void run() {
                    SubscriberKit.this.f();
                }
            });
        }
    }

    @Deprecated
    protected void onDisconnected() {
    }

    /* JADX INFO: Access modifiers changed from: protected */
    /* renamed from: onError */
    public void a(OpentokError opentokError) {
    }

    void onErrorJNI(String str, int i) {
        final OpentokError opentokError = new OpentokError(OpentokError.Domain.SubscriberErrorDomain, i, str);
        this.log.d("onError(...) called", new Object[0]);
        this.handler.post(new Runnable() { // from class: com.opentok.android.-$$Lambda$SubscriberKit$wkEnyeM5nAlBL_BrUOoONzSVu9Q
            @Override // java.lang.Runnable
            public final void run() {
                SubscriberKit.this.a(opentokError);
            }
        });
        if (this.subscriberListener != null) {
            this.handler.post(new Runnable() { // from class: com.opentok.android.-$$Lambda$SubscriberKit$zQYisf5XZPiE1SIh5XkH5xe6z0g
                @Override // java.lang.Runnable
                public final void run() {
                    SubscriberKit.this.b(opentokError);
                }
            });
        }
    }

    void onFrameJNI(long j) {
        BaseVideoRenderer baseVideoRenderer = this.renderer;
        if (baseVideoRenderer != null) {
            BaseVideoRenderer.Frame buildFrameInstance = baseVideoRenderer.buildFrameInstance();
            buildFrameInstance.copyOtcFrame(j, null);
            this.renderer.onFrame(buildFrameInstance);
        }
    }

    public void onPause() {
        this.log.d("onPause() called", new Object[0]);
        BaseVideoRenderer baseVideoRenderer = this.renderer;
        if (baseVideoRenderer != null) {
            baseVideoRenderer.onPause();
        }
        this.resumeSubscribeToVideoOnSessionResume = getSubscribeToVideo();
        setSubscribeToVideo(false);
        this.isPaused = true;
    }

    public void onResume() {
        this.log.d("onResume() called", new Object[0]);
        if (this.isPaused) {
            this.isPaused = false;
            setSubscribeToVideo(this.resumeSubscribeToVideoOnSessionResume);
            BaseVideoRenderer baseVideoRenderer = this.renderer;
            if (baseVideoRenderer != null) {
                baseVideoRenderer.onResume();
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    /* renamed from: onRtcStatsReport */
    public void a(String str) {
    }

    void onRtcStatsReportJNI(final String str) {
        this.log.d("onRtcStatsReportJNI(...) called", new Object[0]);
        this.handler.post(new Runnable() { // from class: com.opentok.android.-$$Lambda$SubscriberKit$aRaEqQ9YgWj1U1_RB1jACmnUOu4
            @Override // java.lang.Runnable
            public final void run() {
                SubscriberKit.this.a(str);
            }
        });
        if (this.rtcStatsReportListener != null) {
            this.handler.post(new Runnable() { // from class: com.opentok.android.-$$Lambda$SubscriberKit$jv6Tj7Qfj4M8ZoNkoFICmoVmopg
                @Override // java.lang.Runnable
                public final void run() {
                    SubscriberKit.this.b(str);
                }
            });
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    /* renamed from: onStreamDisconnected */
    public void g() {
    }

    void onStreamDisconnectedJNI() {
        this.log.d("onStreamDisconnected(...) called", new Object[0]);
        this.handler.post(new Runnable() { // from class: com.opentok.android.-$$Lambda$SubscriberKit$c20TGMg_q-NDy8I-wSJiQUl15t0
            @Override // java.lang.Runnable
            public final void run() {
                SubscriberKit.this.g();
            }
        });
        if (this.streamListener != null) {
            this.handler.post(new Runnable() { // from class: com.opentok.android.-$$Lambda$SubscriberKit$Jja06Em-tDChc8D76zLsayPIoRc
                @Override // java.lang.Runnable
                public final void run() {
                    SubscriberKit.this.h();
                }
            });
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    /* renamed from: onStreamReconnected */
    public void i() {
    }

    void onStreamReconnectedJNI() {
        this.log.d("onStreamReconnected(...) called", new Object[0]);
        this.handler.post(new Runnable() { // from class: com.opentok.android.-$$Lambda$SubscriberKit$UZtO4uOoMZf-zgpShpF1EdtrgmA
            @Override // java.lang.Runnable
            public final void run() {
                SubscriberKit.this.i();
            }
        });
        if (this.streamListener != null) {
            this.handler.post(new Runnable() { // from class: com.opentok.android.-$$Lambda$SubscriberKit$2MIMio4esQf77Gp6di1Arv-GocA
                @Override // java.lang.Runnable
                public final void run() {
                    SubscriberKit.this.j();
                }
            });
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    /* renamed from: onVideoDataReceived */
    public void k() {
    }

    void onVideoDataReceivedJNI() {
        this.handler.post(new Runnable() { // from class: com.opentok.android.-$$Lambda$SubscriberKit$YOwy6bCWBFhcFwH6FL_hYVsbq58
            @Override // java.lang.Runnable
            public final void run() {
                SubscriberKit.this.k();
            }
        });
        if (this.videoListener != null) {
            this.handler.post(new Runnable() { // from class: com.opentok.android.-$$Lambda$SubscriberKit$3JwSSP34ULH1w6PGvStBUPKd2iw
                @Override // java.lang.Runnable
                public final void run() {
                    SubscriberKit.this.l();
                }
            });
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    /* renamed from: onVideoDisableWarning */
    public void m() {
    }

    void onVideoDisableWarningJNI() {
        this.handler.post(new Runnable() { // from class: com.opentok.android.-$$Lambda$SubscriberKit$79F4cf_YBnQsOOyonACL_3p-EmM
            @Override // java.lang.Runnable
            public final void run() {
                SubscriberKit.this.m();
            }
        });
        if (this.videoListener != null) {
            this.handler.post(new Runnable() { // from class: com.opentok.android.-$$Lambda$SubscriberKit$F1GEhz9vE-PGh46Mya5oWYOM9jY
                @Override // java.lang.Runnable
                public final void run() {
                    SubscriberKit.this.n();
                }
            });
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    /* renamed from: onVideoDisableWarningLifted */
    public void o() {
    }

    void onVideoDisableWarningLiftedJNI() {
        this.handler.post(new Runnable() { // from class: com.opentok.android.-$$Lambda$SubscriberKit$pFxHaRJOf3y2g3o8C4VnKuhl9Mc
            @Override // java.lang.Runnable
            public final void run() {
                SubscriberKit.this.o();
            }
        });
        if (this.videoListener != null) {
            this.handler.post(new Runnable() { // from class: com.opentok.android.-$$Lambda$SubscriberKit$MUTlvVk8kqRflEF7tQdSYwXdb8I
                @Override // java.lang.Runnable
                public final void run() {
                    SubscriberKit.this.p();
                }
            });
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    /* renamed from: onVideoDisabled */
    public void c(String str) {
    }

    void onVideoDisabledJNI(int i) {
        this.log.d("onVideoDisabled(...) callled", new Object[0]);
        final String str = VideoReasonMap.get(i);
        this.handler.post(new Runnable() { // from class: com.opentok.android.-$$Lambda$SubscriberKit$15MGXzc8xCYo3JRj9jVMKwtVD_8
            @Override // java.lang.Runnable
            public final void run() {
                SubscriberKit.this.c(str);
            }
        });
        if (this.videoListener != null) {
            this.handler.post(new Runnable() { // from class: com.opentok.android.-$$Lambda$SubscriberKit$6USXDTr9of0J8BvZW0z_KXptEtU
                @Override // java.lang.Runnable
                public final void run() {
                    SubscriberKit.this.d(str);
                }
            });
        }
        if (this.renderer != null) {
            this.handler.post(new Runnable() { // from class: com.opentok.android.-$$Lambda$SubscriberKit$CMzXzySTFLDcjG08fIRms9Fv7Tg
                @Override // java.lang.Runnable
                public final void run() {
                    SubscriberKit.this.q();
                }
            });
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    /* renamed from: onVideoEnabled */
    public void e(String str) {
    }

    void onVideoEnabledJNI(int i) {
        this.log.d("onVideoEnabled(...) callled", new Object[0]);
        final String str = VideoReasonMap.get(i);
        this.handler.post(new Runnable() { // from class: com.opentok.android.-$$Lambda$SubscriberKit$ehDdishFz5U2gcQp-DO_vrxN0VA
            @Override // java.lang.Runnable
            public final void run() {
                SubscriberKit.this.e(str);
            }
        });
        if (this.videoListener != null) {
            this.handler.post(new Runnable() { // from class: com.opentok.android.-$$Lambda$SubscriberKit$-sspXIZgYqlSTjU7zmPh58Txsdk
                @Override // java.lang.Runnable
                public final void run() {
                    SubscriberKit.this.f(str);
                }
            });
        }
        if (this.renderer != null) {
            this.handler.post(new Runnable() { // from class: com.opentok.android.-$$Lambda$SubscriberKit$Asaf1cVU-wnU1Qxnv4VZCMLbyMw
                @Override // java.lang.Runnable
                public final void run() {
                    SubscriberKit.this.r();
                }
            });
        }
    }

    void onVideoStatsJNI(final SubscriberVideoStats subscriberVideoStats) {
        if (this.videoStatsListener != null) {
            this.handler.post(new Runnable() { // from class: com.opentok.android.-$$Lambda$SubscriberKit$QGlL5Ja6rhCuZkuHeOTQwccrR0Q
                @Override // java.lang.Runnable
                public final void run() {
                    SubscriberKit.this.a(subscriberVideoStats);
                }
            });
        }
    }

    public void setAudioLevelListener(AudioLevelListener audioLevelListener) {
        this.audioLevelListener = audioLevelListener;
    }

    public void setAudioStatsListener(AudioStatsListener audioStatsListener) {
        this.audioStatsListener = audioStatsListener;
    }

    public void setPreferredFrameRate(float f) {
        e.a(this.otc_subscriber, f);
    }

    public void setPreferredResolution(VideoUtils.Size size) {
        e.a(this.otc_subscriber, size.width, size.height);
    }

    @Deprecated
    public void setRenderer(BaseVideoRenderer baseVideoRenderer) {
        this.log.e("Renderer cannot be changed after the subscriber has been built. From 2.17 this method is empty", new Object[0]);
    }

    public void setRtcStatsReportListener(SubscriberRtcStatsReportListener subscriberRtcStatsReportListener) {
        swig_otc_rtc_stats_report_cb swig_otc_rtc_stats_report_cbVar = new swig_otc_rtc_stats_report_cb(build_rtc_stats_report_cb());
        this.rtc_stats_report_cb = swig_otc_rtc_stats_report_cbVar;
        e.a(this.otc_subscriber, swig_otc_rtc_stats_report_cbVar);
        this.rtcStatsReportListener = subscriberRtcStatsReportListener;
    }

    public void setStreamListener(StreamListener streamListener) {
        this.streamListener = streamListener;
    }

    public void setStyle(String str, String str2) {
        BaseVideoRenderer baseVideoRenderer = this.renderer;
        if (baseVideoRenderer != null) {
            baseVideoRenderer.setStyle(str, str2);
        }
    }

    public void setSubscribeToAudio(boolean z) {
        e.a(this.otc_subscriber, Utils.booleanToInt(z));
    }

    public void setSubscribeToVideo(boolean z) {
        e.b(this.otc_subscriber, Utils.booleanToInt(z));
    }

    public void setSubscriberListener(SubscriberListener subscriberListener) {
        this.subscriberListener = subscriberListener;
    }

    public void setVideoListener(VideoListener videoListener) {
        this.videoListener = videoListener;
    }

    public void setVideoStatsListener(VideoStatsListener videoStatsListener) {
        this.videoStatsListener = videoStatsListener;
    }

    void throwError(OpentokError opentokError) {
        onErrorJNI(opentokError.errorMessage, opentokError.errorCode.getErrorCode());
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
