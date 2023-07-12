package com.opentok.android;

import android.content.Context;
import android.media.MediaCodecInfo;
import android.media.MediaCodecList;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.facebook.common.util.UriUtil;
import com.opentok.android.BaseAudioDevice;
import com.opentok.android.OpenTokConfig;
import com.opentok.android.OpentokError;
import com.opentok.android.OtLog;
import com.opentok.android.Stream;
import com.opentok.impl.a;
import com.opentok.otc.SWIGTYPE_p_otc_session;
import com.opentok.otc.b;
import com.opentok.otc.e;
import com.opentok.otc.g;
import com.opentok.otc.h;
import com.opentok.otc.otc_on_mute_forced_info;
import com.opentok.otc.otc_session_callbacks;
import com.opentok.otc.otc_session_capabilities;
import com.opentok.otc.otc_signal_options;
import com.opentok.otc.otc_stream_video_type;
import java.lang.reflect.Field;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Observable;
import org.json.JSONArray;
import org.json.JSONObject;

/* loaded from: classes.dex */
public class Session extends Observable {
    private static final int AUDIO_TYPE = 1;
    private static final String DEFAULT_API_URL = "https://api.opentok.com";
    private static final int VIDEO_TYPE = 0;
    protected String apiKey;
    protected URL apiUrl;
    protected ArchiveListener archiveListener;
    protected ConnectionListener connectionListener;
    private Handler handler;
    private final OtLog.LogToken log;
    protected MuteListener muteListener;
    private SWIGTYPE_p_otc_session otc_session;
    protected ReconnectionListener reconnectionListener;
    protected String sessionId;
    protected SessionListener sessionListener;
    private swig_otc_session_cb session_cb;
    private boolean shouldDestroyOnFinalize;
    protected SignalListener signalListener;
    protected StreamPropertiesListener streamPropertiesListener;

    /* loaded from: classes.dex */
    public interface ArchiveListener {
        void onArchiveStarted(Session session, String str, String str2);

        void onArchiveStopped(Session session, String str);
    }

    /* loaded from: classes.dex */
    public static class Builder {
        String apiKey;
        URL apiUrl;
        Context context;
        String proxyUrl;
        String sessionId;
        boolean connectionEventsSuppressed = false;
        SessionOptions sessionOptions = new SessionOptions() { // from class: com.opentok.android.Session.Builder.1
        };
        TransportPolicy turnRouting = TransportPolicy.All;
        IceServer[] iceServers = new IceServer[0];
        IncludeServers turnServerConfig = IncludeServers.All;
        boolean ipWhitelist = false;

        /* loaded from: classes.dex */
        public static class IceServer {
            public String credential;
            public String url;
            public String user;

            public IceServer(String str, String str2, String str3) {
                this.url = "";
                this.user = "";
                this.credential = "";
                this.url = str;
                this.user = str2;
                this.credential = str3;
            }

            public boolean equals(Object obj) {
                if (obj instanceof IceServer) {
                    IceServer iceServer = (IceServer) obj;
                    if (this.url.equals(iceServer.url) && this.user.equals(iceServer.user) && this.credential.equals(iceServer.credential)) {
                        return true;
                    }
                }
                return false;
            }

            public String toString() {
                return "[url: " + this.url + ", user: " + this.user + ", credential: " + this.credential + "]";
            }
        }

        /* loaded from: classes.dex */
        public enum IncludeServers {
            All(0),
            Custom(1);
            
            private int val;

            IncludeServers(int i) {
                this.val = i;
            }
        }

        /* loaded from: classes.dex */
        public enum TransportPolicy {
            All(0),
            Relay(1);
            
            private int val;

            TransportPolicy(int i) {
                this.val = i;
            }
        }

        public Builder(Context context, String str, String str2) {
            this.context = context;
            this.apiKey = str;
            this.sessionId = str2;
        }

        public Session build() {
            return new Session(this.context, this.apiKey, this.sessionId, this.connectionEventsSuppressed, this.sessionOptions, this.turnRouting, this.turnServerConfig, this.iceServers, this.apiUrl, this.ipWhitelist, this.proxyUrl);
        }

        public Builder connectionEventsSuppressed(Boolean bool) {
            this.connectionEventsSuppressed = bool.booleanValue();
            return this;
        }

        public Builder sessionOptions(SessionOptions sessionOptions) {
            this.sessionOptions = sessionOptions;
            return this;
        }

        @Deprecated
        public Builder setApiUrl(URL url) {
            this.apiUrl = url;
            return this;
        }

        public Builder setCustomIceServers(List<IceServer> list, IncludeServers includeServers) {
            this.iceServers = (IceServer[]) list.toArray(new IceServer[list.size()]);
            this.turnServerConfig = includeServers;
            return this;
        }

        public Builder setIceRouting(TransportPolicy transportPolicy) {
            this.turnRouting = transportPolicy;
            return this;
        }

        public Builder setIpWhitelist(boolean z) {
            this.ipWhitelist = z;
            return this;
        }

        public Builder setProxyUrl(String str) {
            this.proxyUrl = str;
            return this;
        }
    }

    /* loaded from: classes.dex */
    public static class Capabilities {
        public boolean canForceMute;
        public boolean canPublish;
        public boolean canSubscribe;
        private final OtLog.LogToken log = new OtLog.LogToken(this);

        public String toString() {
            Field[] fields;
            StringBuilder sb = new StringBuilder("[\n");
            for (Field field : Capabilities.class.getFields()) {
                try {
                    sb.append(String.format("\t%s = %b\n", field.getName(), field.get(this)));
                } catch (IllegalAccessException unused) {
                    this.log.e("Error converting Capabilities to String", new Object[0]);
                }
            }
            sb.append(']');
            return sb.toString();
        }
    }

    /* loaded from: classes.dex */
    protected static class ConfigurableSessionOptions extends SessionOptions {
        private boolean hwDecCapable;

        ConfigurableSessionOptions(boolean z) {
            this.hwDecCapable = false;
            this.hwDecCapable = z;
        }

        @Override // com.opentok.android.Session.SessionOptions
        public boolean isHwDecodingSupported() {
            return this.hwDecCapable;
        }
    }

    /* loaded from: classes.dex */
    public interface ConnectionListener {
        void onConnectionCreated(Session session, Connection connection);

        void onConnectionDestroyed(Session session, Connection connection);
    }

    /* loaded from: classes.dex */
    public interface MuteListener {
        void onMuteForced(Session session, MuteForcedInfo muteForcedInfo);
    }

    /* loaded from: classes.dex */
    public interface ReconnectionListener {
        void onReconnected(Session session);

        void onReconnecting(Session session);
    }

    /* loaded from: classes.dex */
    public interface SessionListener {
        void onConnected(Session session);

        void onDisconnected(Session session);

        void onError(Session session, OpentokError opentokError);

        void onStreamDropped(Session session, Stream stream);

        void onStreamReceived(Session session, Stream stream);
    }

    /* loaded from: classes.dex */
    public static abstract class SessionOptions {
        private final Map<String, Boolean> cam2EnableList = new HashMap<String, Boolean>() { // from class: com.opentok.android.Session.SessionOptions.1
            {
                put("nexus 4", Boolean.TRUE);
                put("nexus 5", Boolean.TRUE);
                put("nexus 5x", Boolean.TRUE);
                put("nexus 6", Boolean.TRUE);
                put("nexus 6p", Boolean.TRUE);
                put("nexus 7", Boolean.TRUE);
                put("nexus 10", Boolean.TRUE);
                put("pixel", Boolean.TRUE);
                put("pixel 4", Boolean.TRUE);
                put("gt-i9300", Boolean.TRUE);
                put("samsung-sm-g925a", Boolean.TRUE);
                put("samsung-sm-g935a", Boolean.TRUE);
                put("samsung-sm-t817a", Boolean.TRUE);
                put("sm-g900h", Boolean.TRUE);
                put("sm-j106h", Boolean.TRUE);
                put("lgus991", Boolean.TRUE);
                put("lg-h810", Boolean.TRUE);
                put("lg-k430", Boolean.TRUE);
                put("xt1058", Boolean.TRUE);
                put("aquaris e5", Boolean.TRUE);
                put("c6602", Boolean.TRUE);
            }
        };

        public boolean isCamera2Capable() {
            return this.cam2EnableList.containsKey(Build.MODEL.toLowerCase(Locale.ROOT));
        }

        @Deprecated
        public boolean isHwDecodingSupported() {
            return false;
        }

        public boolean useTextureViews() {
            return false;
        }
    }

    @Deprecated
    /* loaded from: classes.dex */
    public interface SessionOptionsProvider {
        @Deprecated
        boolean isHwDecodingSupported();
    }

    /* loaded from: classes.dex */
    static class SessionPauseResumeEvent {
        boolean mIsSessionPaused;

        SessionPauseResumeEvent(boolean z) {
            this.mIsSessionPaused = z;
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        public boolean isSessionPaused() {
            return this.mIsSessionPaused;
        }
    }

    /* loaded from: classes.dex */
    public interface SignalListener {
        void onSignalReceived(Session session, String str, String str2, Connection connection);
    }

    /* loaded from: classes.dex */
    public interface StreamPropertiesListener {
        void onStreamHasAudioChanged(Session session, Stream stream, boolean z);

        void onStreamHasVideoChanged(Session session, Stream stream, boolean z);

        void onStreamVideoDimensionsChanged(Session session, Stream stream, int i, int i2);

        void onStreamVideoTypeChanged(Session session, Stream stream, Stream.StreamVideoType streamVideoType);
    }

    /* loaded from: classes.dex */
    static class swig_on_mute_forced_info extends otc_on_mute_forced_info {
        public swig_on_mute_forced_info(long j) {
            super(j, false);
        }

        public long getCPointer() {
            return otc_on_mute_forced_info.getCPtr(this);
        }
    }

    /* loaded from: classes.dex */
    static class swig_otc_session_cb extends otc_session_callbacks {
        public swig_otc_session_cb(long j) {
            super(j, true);
        }

        public long getCPointer() {
            return otc_session_callbacks.getCPtr(this);
        }
    }

    static {
        Loader.load();
    }

    @Deprecated
    public Session(Context context, String str, String str2) {
        this(context, str, str2, new SessionOptions() { // from class: com.opentok.android.Session.1
        });
    }

    @Deprecated
    public Session(Context context, String str, String str2, SessionOptions sessionOptions) {
        this(context, str, str2, false, sessionOptions, Builder.TransportPolicy.All, Builder.IncludeServers.All, new Builder.IceServer[0], null, false, null);
    }

    @Deprecated
    public Session(Context context, String str, String str2, SessionOptionsProvider sessionOptionsProvider) {
        this(context, str, str2, new ConfigurableSessionOptions(sessionOptionsProvider.isHwDecodingSupported()));
    }

    protected Session(Context context, String str, String str2, boolean z, SessionOptions sessionOptions, Builder.TransportPolicy transportPolicy, Builder.IncludeServers includeServers, Builder.IceServer[] iceServerArr, URL url, boolean z2, String str3) {
        URL url2;
        boolean z3 = true;
        this.shouldDestroyOnFinalize = true;
        this.log = new OtLog.LogToken(this);
        if (AudioDeviceManager.getAudioDevice() == null) {
            DefaultAudioDevice defaultAudioDevice = new DefaultAudioDevice(context);
            defaultAudioDevice.setAudioBus(new BaseAudioDevice.AudioBus(defaultAudioDevice));
            AudioDeviceManager.setAudioDevice(defaultAudioDevice);
        }
        Utils.initOtcEngine(context, AudioDeviceManager.getAudioDevice(), OpenTokConfig.getJNILogs(), OpenTokConfig.getOTKitLogs(), OpenTokConfig.getWebRTCLogs(), OpenTokConfig.getDumpClientLoggingToFile());
        if (OpenTokConfig.getPreferH264Codec() != OpenTokConfig.PreferH264.NOT_SET) {
            set_prefer_h264(OpenTokConfig.getPreferH264Codec() == OpenTokConfig.PreferH264.ENABLE);
        }
        b c = e.c();
        e.a(c, str3);
        e.a(c, z ? 1 : 0);
        e.b(c, z2 ? 1 : 0);
        if (iceServerArr.length > 0) {
            String[] strArr = new String[iceServerArr.length];
            String[] strArr2 = new String[iceServerArr.length];
            String[] strArr3 = new String[iceServerArr.length];
            int i = 0;
            for (Builder.IceServer iceServer : iceServerArr) {
                this.log.d("Using custom ICE server " + iceServer.toString(), new Object[0]);
                strArr[i] = iceServer.url;
                strArr2[i] = iceServer.user;
                strArr3[i] = iceServer.credential;
                z3 = true;
                i++;
            }
            e.a(c, iceServerArr.length, strArr, strArr2, strArr3, transportPolicy.val, includeServers.val);
        }
        swig_otc_session_cb swig_otc_session_cbVar = new swig_otc_session_cb(build_native_session_cb());
        this.session_cb = swig_otc_session_cbVar;
        this.otc_session = e.a(str, str2, swig_otc_session_cbVar, c);
        e.a(c);
        this.handler = new Handler(Looper.myLooper());
        VideoCaptureFactory.enableCamera2api((Build.VERSION.SDK_INT < 21 || !sessionOptions.isCamera2Capable()) ? false : z3);
        VideoRenderFactory.useTextureViews(sessionOptions.useTextureViews());
        this.apiKey = str;
        this.sessionId = str2;
        if (url != null) {
            url2 = url;
        } else {
            try {
                url2 = new URL("https://api.opentok.com");
            } catch (MalformedURLException unused) {
                return;
            }
        }
        this.apiUrl = url2;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public Session(SWIGTYPE_p_otc_session sWIGTYPE_p_otc_session, boolean z) {
        this.shouldDestroyOnFinalize = true;
        this.log = new OtLog.LogToken(this);
        this.otc_session = sWIGTYPE_p_otc_session;
        this.shouldDestroyOnFinalize = z;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void a() {
        String[] supportedTypes;
        String[] supportedTypes2;
        if (Build.VERSION.SDK_INT >= 19) {
            try {
                JSONObject jSONObject = new JSONObject();
                JSONArray jSONArray = new JSONArray();
                JSONArray jSONArray2 = new JSONArray();
                jSONObject.put("encoders", jSONArray);
                jSONObject.put("decoders", jSONArray2);
                for (int i = 0; i < MediaCodecList.getCodecCount(); i++) {
                    MediaCodecInfo codecInfoAt = MediaCodecList.getCodecInfoAt(i);
                    if (codecInfoAt != null && codecInfoAt.isEncoder()) {
                        for (String str : codecInfoAt.getSupportedTypes()) {
                            if (str.equals("video/avc")) {
                                MediaCodecInfo.CodecCapabilities capabilitiesForType = codecInfoAt.getCapabilitiesForType(str);
                                JSONObject jSONObject2 = new JSONObject();
                                jSONObject2.put("codec", codecInfoAt.getName());
                                jSONObject2.put("color_format", Arrays.toString(capabilitiesForType.colorFormats));
                                jSONArray.put(jSONObject2);
                            }
                        }
                    } else if (codecInfoAt != null) {
                        for (String str2 : codecInfoAt.getSupportedTypes()) {
                            if (str2.equals("video/avc")) {
                                MediaCodecInfo.CodecCapabilities capabilitiesForType2 = codecInfoAt.getCapabilitiesForType(str2);
                                JSONObject jSONObject3 = new JSONObject();
                                jSONObject3.put("codec", codecInfoAt.getName());
                                jSONObject3.put("color_format", Arrays.toString(capabilitiesForType2.colorFormats));
                                jSONArray2.put(jSONObject3);
                            }
                        }
                    }
                }
                this.log.i("Log custom client event: \"codec-avail\" " + jSONObject.toString(), new Object[0]);
                e.a(this.otc_session, "codec-avail", jSONObject.toString());
            } catch (Exception e) {
                this.log.w("Failed to analyze codec list: " + e.getMessage(), new Object[0]);
                e.printStackTrace();
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void b(Connection connection) {
        ConnectionListener connectionListener = this.connectionListener;
        if (connectionListener != null) {
            connectionListener.onConnectionCreated(this, connection);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void b(MuteForcedInfo muteForcedInfo) {
        MuteListener muteListener = this.muteListener;
        if (muteListener != null) {
            muteListener.onMuteForced(this, muteForcedInfo);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void b(OpentokError opentokError) {
        SessionListener sessionListener = this.sessionListener;
        if (sessionListener != null) {
            sessionListener.onError(this, opentokError);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void b(Stream stream) {
        SessionListener sessionListener = this.sessionListener;
        if (sessionListener != null) {
            sessionListener.onStreamDropped(this, stream);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void b(Stream stream, int i) {
        StreamPropertiesListener streamPropertiesListener = this.streamPropertiesListener;
        if (streamPropertiesListener != null) {
            streamPropertiesListener.onStreamHasAudioChanged(this, stream, Utils.intToBoolean(i));
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void b(Stream stream, int i, int i2) {
        StreamPropertiesListener streamPropertiesListener = this.streamPropertiesListener;
        if (streamPropertiesListener != null) {
            streamPropertiesListener.onStreamVideoDimensionsChanged(this, stream, i, i2);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void b(String str) {
        ArchiveListener archiveListener = this.archiveListener;
        if (archiveListener != null) {
            archiveListener.onArchiveStopped(this, str);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void b(String str, String str2) {
        ArchiveListener archiveListener = this.archiveListener;
        if (archiveListener != null) {
            archiveListener.onArchiveStarted(this, str, str2);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void b(String str, String str2, Connection connection) {
        SignalListener signalListener = this.signalListener;
        if (signalListener != null) {
            signalListener.onSignalReceived(this, str, str2, connection);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void c() {
        SessionListener sessionListener = this.sessionListener;
        if (sessionListener != null) {
            sessionListener.onConnected(this);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void d() {
        String rTCStatsReportFilePath;
        if (this.otc_session == null || (rTCStatsReportFilePath = OpenTokConfig.getRTCStatsReportFilePath()) == null || rTCStatsReportFilePath.length() <= 0) {
            return;
        }
        e.c(this.otc_session, rTCStatsReportFilePath);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void d(Connection connection) {
        ConnectionListener connectionListener = this.connectionListener;
        if (connectionListener != null) {
            connectionListener.onConnectionDestroyed(this, connection);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void d(Stream stream) {
        SessionListener sessionListener = this.sessionListener;
        if (sessionListener != null) {
            sessionListener.onStreamReceived(this, stream);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void d(Stream stream, int i) {
        StreamPropertiesListener streamPropertiesListener = this.streamPropertiesListener;
        if (streamPropertiesListener != null) {
            streamPropertiesListener.onStreamHasVideoChanged(this, stream, Utils.intToBoolean(i));
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void f() {
        SessionListener sessionListener = this.sessionListener;
        if (sessionListener != null) {
            sessionListener.onDisconnected(this);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void f(Stream stream, int i) {
        StreamPropertiesListener streamPropertiesListener = this.streamPropertiesListener;
        if (streamPropertiesListener != null) {
            streamPropertiesListener.onStreamVideoTypeChanged(this, stream, Stream.StreamVideoType.fromSwig(otc_stream_video_type.a(i)));
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void h() {
        ReconnectionListener reconnectionListener = this.reconnectionListener;
        if (reconnectionListener != null) {
            reconnectionListener.onReconnected(this);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void j() {
        ReconnectionListener reconnectionListener = this.reconnectionListener;
        if (reconnectionListener != null) {
            reconnectionListener.onReconnecting(this);
        }
    }

    native long build_native_session_cb();

    public void connect(String str) {
        this.log.d("Connect(...) called", new Object[0]);
        String str2 = this.apiKey;
        if (str2 == null || str2.isEmpty()) {
            throwError(new a(OpentokError.Domain.SessionErrorDomain, OpentokError.ErrorCode.AuthorizationFailure.getErrorCode()));
            return;
        }
        String str3 = this.sessionId;
        if (str3 == null || str3.isEmpty()) {
            throwError(new a(OpentokError.Domain.SessionErrorDomain, OpentokError.ErrorCode.InvalidSessionId.getErrorCode()));
            return;
        }
        int a = e.a(this.otc_session, this.apiUrl.getHost(), this.apiUrl.getPath(), this.apiUrl.getPort() == -1 ? this.apiUrl.getDefaultPort() : this.apiUrl.getPort(), Utils.booleanToInt(this.apiUrl.getProtocol().equals(UriUtil.HTTPS_SCHEME)), str);
        if (a != g.c.a()) {
            throwError(new a(OpentokError.Domain.SessionErrorDomain, a));
        } else if (AudioDeviceManager.getAudioDevice() instanceof DefaultAudioDevice) {
        } else {
            reportExternalDeviceUsage(1);
        }
    }

    native void destroy_native_session_cb(long j);

    public void disableForceMute() {
        int b = e.b(this.otc_session);
        if (b == g.c.a()) {
            return;
        }
        throwError(new a(OpentokError.Domain.SessionErrorDomain, b));
    }

    public void disconnect() {
        this.log.d("Disconnect(...) called", new Object[0]);
        int c = e.c(this.otc_session);
        if (c == g.c.a()) {
            deleteObservers();
        } else {
            throwError(new a(OpentokError.Domain.SessionErrorDomain, c));
        }
    }

    protected void finalize() {
        this.log.d("finalize()", new Object[0]);
        SWIGTYPE_p_otc_session sWIGTYPE_p_otc_session = this.otc_session;
        if (sWIGTYPE_p_otc_session != null && this.shouldDestroyOnFinalize) {
            e.a(sWIGTYPE_p_otc_session);
            this.otc_session = null;
            swig_otc_session_cb swig_otc_session_cbVar = this.session_cb;
            if (swig_otc_session_cbVar != null) {
                destroy_native_session_cb(swig_otc_session_cbVar.getCPointer());
            }
        }
        super.finalize();
    }

    public void forceMuteAll(@Nullable Iterable<Stream> iterable) {
        ArrayList arrayList = new ArrayList();
        if (iterable != null) {
            for (Stream stream : iterable) {
                arrayList.add(stream.getStreamId());
            }
        }
        int a = e.a(this.otc_session, (String[]) Arrays.copyOf(arrayList.toArray(), arrayList.size(), String[].class), arrayList.size());
        if (a == g.c.a()) {
            return;
        }
        throwError(new a(OpentokError.Domain.SessionErrorDomain, a));
    }

    public void forceMuteStream(@NonNull Stream stream) {
        if (stream == null) {
            throwError(new a(OpentokError.Domain.SessionErrorDomain, OpentokError.ErrorCode.SessionNullOrInvalidParameter.getErrorCode()));
            return;
        }
        OtLog.LogToken logToken = this.log;
        logToken.d("forceMuteStream() called with streamID = " + stream.getStreamId(), new Object[0]);
        int a = e.a(this.otc_session, stream.getStreamId());
        if (a != g.c.a()) {
            throwError(new a(OpentokError.Domain.SessionErrorDomain, a));
        }
    }

    public Capabilities getCapabilities() {
        otc_session_capabilities d = e.d(this.otc_session);
        Capabilities capabilities = new Capabilities();
        capabilities.canPublish = Utils.intToBoolean(d.c());
        capabilities.canForceMute = Utils.intToBoolean(d.b());
        return capabilities;
    }

    public Connection getConnection() {
        return new Connection(e.e(this.otc_session), true);
    }

    Builder.IceServer[] getIceServers() {
        int[] iArr = new int[1];
        if (e.b(this.otc_session, iArr) != g.c.a()) {
            return new Builder.IceServer[0];
        }
        if (iArr[0] == 0) {
            return new Builder.IceServer[0];
        }
        String[] c = e.c(this.otc_session, iArr);
        String[] d = e.d(this.otc_session, iArr);
        String[] a = e.a(this.otc_session, iArr);
        Builder.IceServer[] iceServerArr = new Builder.IceServer[iArr[0]];
        for (int i = 0; i < iArr[0]; i++) {
            iceServerArr[i] = new Builder.IceServer(c[i], d[i], a[i]);
        }
        return iceServerArr;
    }

    public String getSessionId() {
        return e.f(this.otc_session);
    }

    native void init_otc_engine(Context context, BaseAudioDevice baseAudioDevice, boolean z, boolean z2, boolean z3, boolean z4);

    boolean isSessionConnected() {
        return e.e(this.otc_session) != null;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    /* renamed from: onArchiveStarted */
    public void a(String str, String str2) {
    }

    void onArchiveStartedJNI(final String str, final String str2) {
        this.handler.post(new Runnable() { // from class: com.opentok.android.-$$Lambda$Session$AxzHQxB-ZfVp1NQbjRXU9Ghqp6I
            @Override // java.lang.Runnable
            public final void run() {
                Session.this.a(str, str2);
            }
        });
        if (this.archiveListener != null) {
            this.handler.post(new Runnable() { // from class: com.opentok.android.-$$Lambda$Session$EjBYPQRGyhIL5DZ2321l3ozh7M0
                @Override // java.lang.Runnable
                public final void run() {
                    Session.this.b(str, str2);
                }
            });
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    /* renamed from: onArchiveStopped */
    public void a(String str) {
    }

    void onArchiveStoppedJNI(final String str) {
        this.handler.post(new Runnable() { // from class: com.opentok.android.-$$Lambda$Session$GfzCgDNDLua3imw_TjBmREFCsg8
            @Override // java.lang.Runnable
            public final void run() {
                Session.this.a(str);
            }
        });
        if (this.archiveListener != null) {
            this.handler.post(new Runnable() { // from class: com.opentok.android.-$$Lambda$Session$tdi82JLiK-Ols63KFTqbfWsoSJ0
                @Override // java.lang.Runnable
                public final void run() {
                    Session.this.b(str);
                }
            });
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    /* renamed from: onConnected */
    public void b() {
    }

    void onConnectedJNI() {
        this.handler.post(new Runnable() { // from class: com.opentok.android.-$$Lambda$Session$wbvaOQEimTeM6RO0gBAg9bAjEzY
            @Override // java.lang.Runnable
            public final void run() {
                Session.this.a();
            }
        });
        this.handler.post(new Runnable() { // from class: com.opentok.android.-$$Lambda$Session$6t-9B_-ubS3ZRvftOhWcuD9HaY4
            @Override // java.lang.Runnable
            public final void run() {
                Session.this.b();
            }
        });
        if (this.sessionListener != null) {
            this.handler.post(new Runnable() { // from class: com.opentok.android.-$$Lambda$Session$zw2Bge9QIqwyFHcv5ty-4bQ2xSA
                @Override // java.lang.Runnable
                public final void run() {
                    Session.this.c();
                }
            });
        }
        this.handler.post(new Runnable() { // from class: com.opentok.android.-$$Lambda$Session$i10CkV0v7e63OUHrlWSW3Gziuxc
            @Override // java.lang.Runnable
            public final void run() {
                Session.this.d();
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: protected */
    /* renamed from: onConnectionCreated */
    public void a(Connection connection) {
    }

    void onConnectionCreatedJNI(long j) {
        final Connection connection = new Connection(j, true);
        this.handler.post(new Runnable() { // from class: com.opentok.android.-$$Lambda$Session$wo9aZaKyVQp88HPHStqp-iHNXPw
            @Override // java.lang.Runnable
            public final void run() {
                Session.this.a(connection);
            }
        });
        if (this.connectionListener != null) {
            this.handler.post(new Runnable() { // from class: com.opentok.android.-$$Lambda$Session$RWqic6tIWrfEartEOAEE6wpDIx4
                @Override // java.lang.Runnable
                public final void run() {
                    Session.this.b(connection);
                }
            });
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    /* renamed from: onConnectionDestroyed */
    public void c(Connection connection) {
    }

    void onConnectionDroppedJNI(long j) {
        final Connection connection = new Connection(j, true);
        this.handler.post(new Runnable() { // from class: com.opentok.android.-$$Lambda$Session$Ixk5doJbnvMjixLhgNo5SH8VRjk
            @Override // java.lang.Runnable
            public final void run() {
                Session.this.c(connection);
            }
        });
        if (this.connectionListener != null) {
            this.handler.post(new Runnable() { // from class: com.opentok.android.-$$Lambda$Session$qRC1L7WXSgemx5o-xTS4nItWiKs
                @Override // java.lang.Runnable
                public final void run() {
                    Session.this.d(connection);
                }
            });
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    /* renamed from: onDisconnected */
    public void e() {
    }

    void onDisconnectedJNI() {
        this.handler.post(new Runnable() { // from class: com.opentok.android.-$$Lambda$Session$01jdAVEs0tlMfShxApHIqZ15XeE
            @Override // java.lang.Runnable
            public final void run() {
                Session.this.e();
            }
        });
        if (this.sessionListener != null) {
            this.handler.post(new Runnable() { // from class: com.opentok.android.-$$Lambda$Session$PCKJqHI5PiAqQ0zhTNZrNulQjek
                @Override // java.lang.Runnable
                public final void run() {
                    Session.this.f();
                }
            });
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    /* renamed from: onError */
    public void a(OpentokError opentokError) {
    }

    void onErrorJNI(String str, int i) {
        final OpentokError opentokError = new OpentokError(OpentokError.Domain.SessionErrorDomain, i, str);
        this.handler.post(new Runnable() { // from class: com.opentok.android.-$$Lambda$Session$qm5Hu1nZkeLxgpKxjHYP91bG7Oc
            @Override // java.lang.Runnable
            public final void run() {
                Session.this.a(opentokError);
            }
        });
        if (this.sessionListener != null) {
            this.handler.post(new Runnable() { // from class: com.opentok.android.-$$Lambda$Session$DHPNjroySA4rGdbK9nKWieasGe0
                @Override // java.lang.Runnable
                public final void run() {
                    Session.this.b(opentokError);
                }
            });
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    /* renamed from: onMuteForced */
    public void a(MuteForcedInfo muteForcedInfo) {
    }

    void onMuteForcedJNI(long j) {
        this.log.d("onMuteForcedJNI(long) called", new Object[0]);
        final MuteForcedInfo muteForcedInfo = new MuteForcedInfo(new swig_on_mute_forced_info(j).getActive() == 1);
        this.handler.post(new Runnable() { // from class: com.opentok.android.-$$Lambda$Session$2DYcn1vDRQLW4N7N21lESmZHNHA
            @Override // java.lang.Runnable
            public final void run() {
                Session.this.a(muteForcedInfo);
            }
        });
        if (this.muteListener != null) {
            this.handler.post(new Runnable() { // from class: com.opentok.android.-$$Lambda$Session$MZC5GVvxqTJsyJ4R_8AyQ3iueVs
                @Override // java.lang.Runnable
                public final void run() {
                    Session.this.b(muteForcedInfo);
                }
            });
        }
    }

    public void onPause() {
        this.log.d("onPause() called", new Object[0]);
        BaseAudioDevice audioDevice = AudioDeviceManager.getAudioDevice();
        if (audioDevice != null) {
            audioDevice.onPause();
        }
        setChanged();
        notifyObservers(new SessionPauseResumeEvent(true));
        clearChanged();
    }

    /* JADX INFO: Access modifiers changed from: protected */
    /* renamed from: onReconnected */
    public void g() {
    }

    void onReconnectedJNI() {
        this.handler.post(new Runnable() { // from class: com.opentok.android.-$$Lambda$Session$JXpTjVyPOQ9x3eDIW8Aue5NA1ok
            @Override // java.lang.Runnable
            public final void run() {
                Session.this.g();
            }
        });
        if (this.reconnectionListener != null) {
            this.handler.post(new Runnable() { // from class: com.opentok.android.-$$Lambda$Session$-mgWPsk8VcVTmXF5QBzYK5RvCvE
                @Override // java.lang.Runnable
                public final void run() {
                    Session.this.h();
                }
            });
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    /* renamed from: onReconnecting */
    public void i() {
    }

    void onReconnectingJNI() {
        this.handler.post(new Runnable() { // from class: com.opentok.android.-$$Lambda$Session$3qpDf1rA7Aif11rQrn7qc1lidVA
            @Override // java.lang.Runnable
            public final void run() {
                Session.this.i();
            }
        });
        if (this.reconnectionListener != null) {
            this.handler.post(new Runnable() { // from class: com.opentok.android.-$$Lambda$Session$Es3xiv4y8ahLStHxPEHpn0C62pc
                @Override // java.lang.Runnable
                public final void run() {
                    Session.this.j();
                }
            });
        }
    }

    public void onResume() {
        this.log.d("onResume() called", new Object[0]);
        BaseAudioDevice audioDevice = AudioDeviceManager.getAudioDevice();
        if (audioDevice != null) {
            audioDevice.onResume();
        }
        setChanged();
        notifyObservers(new SessionPauseResumeEvent(false));
        clearChanged();
    }

    /* JADX INFO: Access modifiers changed from: protected */
    /* renamed from: onSignalReceived */
    public void a(String str, String str2, Connection connection) {
    }

    void onSignalReceivedJNI(final String str, final String str2, long j) {
        final Connection connection = new Connection(j, true);
        this.handler.post(new Runnable() { // from class: com.opentok.android.-$$Lambda$Session$jbiBbMC90AdPizm61eI-GMWHwfw
            @Override // java.lang.Runnable
            public final void run() {
                Session.this.a(str, str2, connection);
            }
        });
        if (this.signalListener != null) {
            this.handler.post(new Runnable() { // from class: com.opentok.android.-$$Lambda$Session$V31XGyeLNBKzidH-w4tM_foG4Io
                @Override // java.lang.Runnable
                public final void run() {
                    Session.this.b(str, str2, connection);
                }
            });
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    /* renamed from: onStreamDropped */
    public void a(Stream stream) {
    }

    void onStreamDroppedJNI(long j) {
        final Stream stream = new Stream(j, true);
        this.handler.post(new Runnable() { // from class: com.opentok.android.-$$Lambda$Session$JW-lynerJX9FXblY6l9cLFzaqg0
            @Override // java.lang.Runnable
            public final void run() {
                Session.this.a(stream);
            }
        });
        if (this.sessionListener != null) {
            this.handler.post(new Runnable() { // from class: com.opentok.android.-$$Lambda$Session$XcZSOYrJMUjKBTVPTxPnP7btNyY
                @Override // java.lang.Runnable
                public final void run() {
                    Session.this.b(stream);
                }
            });
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    /* renamed from: onStreamHasAudioChanged */
    public void a(Stream stream, int i) {
    }

    void onStreamHasAudioChangedJNI(long j, final int i) {
        final Stream stream = new Stream(j, true);
        this.handler.post(new Runnable() { // from class: com.opentok.android.-$$Lambda$Session$h6dvm3btlRoOCS76jSxF4FhzL_4
            @Override // java.lang.Runnable
            public final void run() {
                Session.this.a(stream, i);
            }
        });
        if (this.streamPropertiesListener != null) {
            this.handler.post(new Runnable() { // from class: com.opentok.android.-$$Lambda$Session$h8nXcQ8ZKREqlhhNxW0aOFIXYH8
                @Override // java.lang.Runnable
                public final void run() {
                    Session.this.b(stream, i);
                }
            });
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    /* renamed from: onStreamHasVideoChanged */
    public void c(Stream stream, int i) {
    }

    void onStreamHasVideoChangedJNI(long j, final int i) {
        final Stream stream = new Stream(j, true);
        this.handler.post(new Runnable() { // from class: com.opentok.android.-$$Lambda$Session$1vqa0745iR6R2tNIy6zF4bqGLOc
            @Override // java.lang.Runnable
            public final void run() {
                Session.this.c(stream, i);
            }
        });
        if (this.streamPropertiesListener != null) {
            this.handler.post(new Runnable() { // from class: com.opentok.android.-$$Lambda$Session$6BhHEG3O7XuuipmbQ3omSwfY3Bc
                @Override // java.lang.Runnable
                public final void run() {
                    Session.this.d(stream, i);
                }
            });
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    /* renamed from: onStreamReceived */
    public void c(Stream stream) {
    }

    void onStreamReceivedJNI(long j) {
        final Stream stream = new Stream(j, true);
        this.handler.post(new Runnable() { // from class: com.opentok.android.-$$Lambda$Session$zMmXaDSlLkBxd8tFjmaFBXTSCB8
            @Override // java.lang.Runnable
            public final void run() {
                Session.this.c(stream);
            }
        });
        if (this.sessionListener != null) {
            this.handler.post(new Runnable() { // from class: com.opentok.android.-$$Lambda$Session$dRQIjxqq5VlGKu-zBXWDoXNAoOY
                @Override // java.lang.Runnable
                public final void run() {
                    Session.this.d(stream);
                }
            });
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    /* renamed from: onStreamVideoDimensionsChanged */
    public void a(Stream stream, int i, int i2) {
    }

    void onStreamVideoDimensionsChangedJNI(long j, final int i, final int i2) {
        final Stream stream = new Stream(j, true);
        this.handler.post(new Runnable() { // from class: com.opentok.android.-$$Lambda$Session$eFvvNvqxflcvw5T-IRynqSt-ROo
            @Override // java.lang.Runnable
            public final void run() {
                Session.this.a(stream, i, i2);
            }
        });
        if (this.streamPropertiesListener != null) {
            this.handler.post(new Runnable() { // from class: com.opentok.android.-$$Lambda$Session$PtS0JObLTKQna32soSnAOwOlptk
                @Override // java.lang.Runnable
                public final void run() {
                    Session.this.b(stream, i, i2);
                }
            });
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    /* renamed from: onStreamVideoTypeChanged */
    public void e(Stream stream, int i) {
    }

    void onStreamVideoTypeChangedJNI(long j, final int i) {
        final Stream stream = new Stream(j, true);
        this.handler.post(new Runnable() { // from class: com.opentok.android.-$$Lambda$Session$hpyRgJF99zxy3NOMJmUI_J3_IwE
            @Override // java.lang.Runnable
            public final void run() {
                Session.this.e(stream, i);
            }
        });
        if (this.streamPropertiesListener != null) {
            this.handler.post(new Runnable() { // from class: com.opentok.android.-$$Lambda$Session$liCA9YSqv_5gx4jPpVLc8W3z6sM
                @Override // java.lang.Runnable
                public final void run() {
                    Session.this.f(stream, i);
                }
            });
        }
    }

    public void publish(PublisherKit publisherKit) {
        a aVar;
        this.log.d("publish(...) called", new Object[0]);
        if (publisherKit == null || publisherKit.getOtcPublisher() == null) {
            aVar = new a(OpentokError.Domain.SessionErrorDomain, OpentokError.ErrorCode.SessionNullOrInvalidParameter.getErrorCode());
        } else if (isSessionConnected()) {
            int a = e.a(this.otc_session, publisherKit.getOtcPublisher());
            if (a == g.c.a()) {
                if (!(publisherKit.getCapturer() instanceof DefaultVideoCapturer) || !(publisherKit.getRenderer() instanceof DefaultVideoRenderer)) {
                    reportExternalDeviceUsage(0);
                }
                addObserver(publisherKit);
                return;
            }
            aVar = new a(OpentokError.Domain.SessionErrorDomain, a);
        } else {
            aVar = new a(OpentokError.Domain.SessionErrorDomain, OpentokError.ErrorCode.SessionDisconnected.getErrorCode());
        }
        throwError(aVar);
    }

    void reportExternalDeviceUsage(int i) {
        e.a(this.otc_session, h.a(i));
    }

    public String reportIssue() {
        String b = e.b(this.otc_session, "");
        if (b == null && b.equals("")) {
            return null;
        }
        return b;
    }

    public void sendSignal(String str, String str2) {
        int b = e.b(this.otc_session, str, str2);
        if (b == g.c.a()) {
            return;
        }
        throwError(new a(OpentokError.Domain.SessionErrorDomain, b));
    }

    public void sendSignal(String str, String str2, Connection connection) {
        int a = e.a(this.otc_session, str, str2, connection.getOtcConnection());
        if (a == g.c.a()) {
            return;
        }
        throwError(new a(OpentokError.Domain.SessionErrorDomain, a));
    }

    public void sendSignal(String str, String str2, Connection connection, boolean z) {
        otc_signal_options otc_signal_optionsVar = new otc_signal_options();
        otc_signal_optionsVar.a(z ? 1 : 0);
        int a = e.a(this.otc_session, str, str2, connection.getOtcConnection(), otc_signal_optionsVar);
        if (a == g.c.a()) {
            return;
        }
        throwError(new a(OpentokError.Domain.SessionErrorDomain, a));
    }

    public void sendSignal(String str, String str2, boolean z) {
        otc_signal_options otc_signal_optionsVar = new otc_signal_options();
        otc_signal_optionsVar.a(z ? 1 : 0);
        int a = e.a(this.otc_session, str, str2, otc_signal_optionsVar);
        if (a == g.c.a()) {
            return;
        }
        throwError(new a(OpentokError.Domain.SessionErrorDomain, a));
    }

    public void setArchiveListener(ArchiveListener archiveListener) {
        this.archiveListener = archiveListener;
    }

    public void setConnectionListener(ConnectionListener connectionListener) {
        this.connectionListener = connectionListener;
    }

    public void setMuteListener(MuteListener muteListener) {
        this.muteListener = muteListener;
    }

    public void setReconnectionListener(ReconnectionListener reconnectionListener) {
        this.reconnectionListener = reconnectionListener;
    }

    public void setSessionListener(SessionListener sessionListener) {
        this.sessionListener = sessionListener;
    }

    public void setSignalListener(SignalListener signalListener) {
        this.signalListener = signalListener;
    }

    public void setStreamPropertiesListener(StreamPropertiesListener streamPropertiesListener) {
        this.streamPropertiesListener = streamPropertiesListener;
    }

    native void set_prefer_h264(boolean z);

    void simulateReconnect() {
        if (e.g(this.otc_session) != g.c.a()) {
            this.log.e("Error when simulating a client reconnection.", new Object[0]);
        }
    }

    public void subscribe(SubscriberKit subscriberKit) {
        a aVar;
        this.log.d("subscribe(...) called", new Object[0]);
        if (subscriberKit == null || subscriberKit.getOtcSubscriber() == null) {
            aVar = new a(OpentokError.Domain.SessionErrorDomain, OpentokError.ErrorCode.SessionNullOrInvalidParameter.getErrorCode());
        } else if (isSessionConnected()) {
            int a = e.a(this.otc_session, subscriberKit.getOtcSubscriber());
            if (a == g.c.a()) {
                subscriberKit.attachToSession(this);
                addObserver(subscriberKit);
                if (subscriberKit.getRenderer() instanceof DefaultVideoRenderer) {
                    return;
                }
                reportExternalDeviceUsage(0);
                return;
            }
            aVar = new a(OpentokError.Domain.SessionErrorDomain, a);
        } else {
            aVar = new a(OpentokError.Domain.SessionErrorDomain, OpentokError.ErrorCode.SessionDisconnected.getErrorCode());
        }
        throwError(aVar);
    }

    void throwError(final OpentokError opentokError) {
        if (this.sessionListener != null) {
            this.handler.post(new Runnable() { // from class: com.opentok.android.Session.2
                @Override // java.lang.Runnable
                public void run() {
                    synchronized (this) {
                        if (Session.this.sessionListener != null) {
                            Session.this.sessionListener.onError(Session.this, opentokError);
                        }
                    }
                }
            });
        }
    }

    public void unpublish(PublisherKit publisherKit) {
        a aVar;
        this.log.d("unpublish(...) called", new Object[0]);
        if (publisherKit == null || publisherKit.getOtcPublisher() == null) {
            aVar = new a(OpentokError.Domain.SessionErrorDomain, OpentokError.ErrorCode.SessionNullOrInvalidParameter.getErrorCode());
        } else if (isSessionConnected()) {
            int b = e.b(this.otc_session, publisherKit.getOtcPublisher());
            if (b == g.c.a()) {
                deleteObserver(publisherKit);
                return;
            }
            aVar = new a(OpentokError.Domain.SessionErrorDomain, b);
        } else {
            aVar = new a(OpentokError.Domain.SessionErrorDomain, OpentokError.ErrorCode.SessionDisconnected.getErrorCode());
        }
        throwError(aVar);
    }

    public void unsubscribe(SubscriberKit subscriberKit) {
        this.log.d("unsubscribe(...) called", new Object[0]);
        if (subscriberKit == null || subscriberKit.getOtcSubscriber() == null) {
            throwError(new a(OpentokError.Domain.SessionErrorDomain, OpentokError.ErrorCode.SessionNullOrInvalidParameter.getErrorCode()));
        } else if (!isSessionConnected()) {
            throwError(new a(OpentokError.Domain.SessionErrorDomain, OpentokError.ErrorCode.SessionDisconnected.getErrorCode()));
        } else {
            int b = e.b(this.otc_session, subscriberKit.getOtcSubscriber());
            if (b != g.c.a()) {
                throwError(new a(OpentokError.Domain.SessionErrorDomain, b));
                return;
            }
            deleteObserver(subscriberKit);
            subscriberKit.detachFromSession(this);
        }
    }
}
