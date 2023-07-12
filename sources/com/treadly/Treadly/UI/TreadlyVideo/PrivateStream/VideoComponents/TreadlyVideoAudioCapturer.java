package com.treadly.Treadly.UI.TreadlyVideo.PrivateStream.VideoComponents;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothProfile;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.AudioManager;
import android.media.AudioRecord;
import android.media.AudioTrack;
import android.media.MediaRouter;
import android.media.audiofx.AcousticEchoCanceler;
import android.media.audiofx.NoiseSuppressor;
import android.os.Build;
import android.os.Handler;
import android.os.Process;
import android.telephony.TelephonyManager;
import android.util.Log;
import com.opentok.android.BaseAudioDevice;
import java.nio.ByteBuffer;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;
import org.otwebrtc.MediaStreamTrack;

/* loaded from: classes2.dex */
public class TreadlyVideoAudioCapturer extends BaseAudioDevice {
    private static final int DEFAULT_BLUETOOTH_SCO_START_DELAY = 2000;
    private static final int DEFAULT_BUFFER_SIZE = 1760;
    private static final int DEFAULT_SAMPLES_PER_BUFFER = 440;
    private static final int DEFAULT_SAMPLE_RATE = 44100;
    private static final int DEFAULT_START_RENDERER_AND_CAPTURER_DELAY = 5000;
    private static final String HEADSET_PLUG_STATE_KEY = "state";
    private static final String LOG_TAG = "TreadlyVideoAudioCapturer";
    private static final int NUM_CHANNELS_CAPTURING = 1;
    private static final int NUM_CHANNELS_RENDERING = 1;
    private static final int SAMPLE_SIZE_IN_BYTES = 2;
    private static final int STEREO_CHANNELS = 2;
    private AudioManager audioManager;
    private AudioRecord audioRecord;
    private AudioTrack audioTrack;
    private BluetoothAdapter bluetoothAdapter;
    private BluetoothProfile bluetoothProfile;
    private BluetoothState bluetoothState;
    private BaseAudioDevice.AudioSettings captureSettings;
    private Context context;
    private AcousticEchoCanceler echoCanceler;
    private boolean isBluetoothHeadSetReceiverRegistered;
    private boolean isHeadsetReceiverRegistered;
    private boolean isPaused;
    private boolean isPhoneStateListenerRegistered;
    private MediaRouter mediaRouter;
    private NoiseSuppressor noiseSuppressor;
    private int outputSamplingRate;
    private ByteBuffer playBuffer;
    private ByteBuffer recBuffer;
    private BaseAudioDevice.AudioSettings rendererSettings;
    private int samplesPerBuffer;
    private TelephonyManager telephonyManager;
    private byte[] tempBufPlay;
    private byte[] tempBufRec;
    private boolean wasCapturing;
    private boolean wasRendering;
    private final ReentrantLock rendererLock = new ReentrantLock(true);
    private final Condition renderEvent = this.rendererLock.newCondition();
    private volatile boolean isRendering = false;
    private volatile boolean shutdownRenderThread = false;
    private final ReentrantLock captureLock = new ReentrantLock(true);
    private final Condition captureEvent = this.captureLock.newCondition();
    private volatile boolean isCapturing = false;
    private volatile boolean shutdownCaptureThread = false;
    private int estimatedCaptureDelay = 0;
    private int bufferedPlaySamples = 0;
    private int playPosition = 0;
    private int estimatedRenderDelay = 0;
    private AudioManagerMode audioManagerMode = new AudioManagerMode();
    private int captureSamplingRate = DEFAULT_SAMPLE_RATE;
    private final Object bluetoothLock = new Object();
    private OutputType audioOutputType = OutputType.SPEAKER_PHONE;
    private AudioState audioState = new AudioState();
    private BroadcastReceiver headsetBroadcastReceiver = new BroadcastReceiver() { // from class: com.treadly.Treadly.UI.TreadlyVideo.PrivateStream.VideoComponents.TreadlyVideoAudioCapturer.1
        @Override // android.content.BroadcastReceiver
        public void onReceive(Context context, Intent intent) {
            Log.d(TreadlyVideoAudioCapturer.LOG_TAG, "headsetBroadcastReceiver.onReceive()");
            if (intent.getAction().equals("android.intent.action.HEADSET_PLUG")) {
                if (intent.getIntExtra("state", 0) == 1) {
                    Log.d(TreadlyVideoAudioCapturer.LOG_TAG, "headsetBroadcastReceiver.onReceive():  Headphones connected");
                    TreadlyVideoAudioCapturer.this.audioState.setLastOutputType(TreadlyVideoAudioCapturer.this.getOutputType());
                    TreadlyVideoAudioCapturer.this.setOutputType(OutputType.HEAD_PHONES);
                    TreadlyVideoAudioCapturer.this.audioManager.setSpeakerphoneOn(false);
                    TreadlyVideoAudioCapturer.this.audioManager.setBluetoothScoOn(false);
                    return;
                }
                Log.d(TreadlyVideoAudioCapturer.LOG_TAG, "headsetBroadcastReceiver.onReceive():  Headphones disconnected");
                if (TreadlyVideoAudioCapturer.this.getOutputType() == OutputType.HEAD_PHONES) {
                    if (TreadlyVideoAudioCapturer.this.audioState.getLastOutputType() != OutputType.BLUETOOTH || BluetoothState.Connected != TreadlyVideoAudioCapturer.this.bluetoothState) {
                        if (TreadlyVideoAudioCapturer.this.audioState.getLastOutputType() == OutputType.SPEAKER_PHONE) {
                            TreadlyVideoAudioCapturer.this.setOutputType(OutputType.SPEAKER_PHONE);
                            TreadlyVideoAudioCapturer.this.audioManager.setSpeakerphoneOn(true);
                        }
                        if (TreadlyVideoAudioCapturer.this.audioState.getLastOutputType() == OutputType.EAR_PIECE) {
                            TreadlyVideoAudioCapturer.this.setOutputType(OutputType.EAR_PIECE);
                            TreadlyVideoAudioCapturer.this.audioManager.setSpeakerphoneOn(false);
                            return;
                        }
                        return;
                    }
                    TreadlyVideoAudioCapturer.this.setOutputType(OutputType.BLUETOOTH);
                }
            }
        }
    };
    private final BroadcastReceiver bluetoothHeadsetReceiver = new BroadcastReceiver() { // from class: com.treadly.Treadly.UI.TreadlyVideo.PrivateStream.VideoComponents.TreadlyVideoAudioCapturer.2
        @Override // android.content.BroadcastReceiver
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals("android.bluetooth.headset.profile.action.AUDIO_STATE_CHANGED")) {
                switch (intent.getIntExtra("android.bluetooth.profile.extra.STATE", -1)) {
                    case 10:
                        Log.d(TreadlyVideoAudioCapturer.LOG_TAG, "bluetoothHeadsetReceiver.onReceive(): STATE_AUDIO_DISCONNECTED");
                        return;
                    case 11:
                        Log.d(TreadlyVideoAudioCapturer.LOG_TAG, "bluetoothHeadsetReceiver.onReceive(): STATE_AUDIO_CONNECTING");
                        return;
                    case 12:
                        Log.d(TreadlyVideoAudioCapturer.LOG_TAG, "bluetoothHeadsetReceiver.onReceive(): STATE_AUDIO_CONNECTED");
                        return;
                    default:
                        return;
                }
            }
        }
    };
    private final BroadcastReceiver bluetoothSpeakerReceiver = new BroadcastReceiver() { // from class: com.treadly.Treadly.UI.TreadlyVideo.PrivateStream.VideoComponents.TreadlyVideoAudioCapturer.3
        @Override // android.content.BroadcastReceiver
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals("android.bluetooth.a2dp.profile.action.PLAYING_STATE_CHANGED")) {
                switch (intent.getIntExtra("android.bluetooth.profile.extra.STATE", -1)) {
                    case 10:
                        Log.d(TreadlyVideoAudioCapturer.LOG_TAG, "bluetoothSpeakerReceiver.onReceive(): STATE_PLAYING");
                        return;
                    case 11:
                        Log.d(TreadlyVideoAudioCapturer.LOG_TAG, "bluetoothSpeakerReceiver.onReceive(): STATE_NOT_PLAYING");
                        return;
                    default:
                        return;
                }
            }
        }
    };
    private final BroadcastReceiver bluetoothBroadcastReceiver = new AnonymousClass4();
    private AudioManager.OnAudioFocusChangeListener audioFocusChangeListener = new AudioManager.OnAudioFocusChangeListener() { // from class: com.treadly.Treadly.UI.TreadlyVideo.PrivateStream.VideoComponents.TreadlyVideoAudioCapturer.5
        @Override // android.media.AudioManager.OnAudioFocusChangeListener
        public void onAudioFocusChange(int i) {
            String str = TreadlyVideoAudioCapturer.LOG_TAG;
            Log.d(str, "AudioManager.OnAudioFocusChangeListener.onAudioFocusChange(" + i + ")");
            switch (i) {
                case -3:
                    String str2 = TreadlyVideoAudioCapturer.LOG_TAG;
                    Log.d(str2, "AudioManager.OnAudioFocusChangeListener.onAudioFocusChange(" + i + "): AudioManager.AUDIOFOCUS_LOSS_TRANSIENT_CAN_DUCK");
                    TreadlyVideoAudioCapturer.this.audioState.setLastStreamVolume(TreadlyVideoAudioCapturer.this.audioManager.getStreamVolume(0));
                    TreadlyVideoAudioCapturer.this.audioManager.setStreamVolume(0, 0, 0);
                    break;
                case -2:
                    String str3 = TreadlyVideoAudioCapturer.LOG_TAG;
                    Log.d(str3, "AudioManager.OnAudioFocusChangeListener.onAudioFocusChange(" + i + "): AudioManager.AUDIOFOCUS_LOSS_TRANSIENT");
                    break;
                case -1:
                    String str4 = TreadlyVideoAudioCapturer.LOG_TAG;
                    Log.d(str4, "AudioManager.OnAudioFocusChangeListener.onAudioFocusChange(" + i + "): AudioManager.AUDIOFOCUS_LOSS");
                    break;
                case 0:
                    String str5 = TreadlyVideoAudioCapturer.LOG_TAG;
                    Log.d(str5, "AudioManager.OnAudioFocusChangeListener.onAudioFocusChange(" + i + "): AudioManager.AUDIOFOCUS_NONE");
                    break;
                case 1:
                    String str6 = TreadlyVideoAudioCapturer.LOG_TAG;
                    Log.d(str6, "AudioManager.OnAudioFocusChangeListener.onAudioFocusChange(" + i + "): ");
                    switch (TreadlyVideoAudioCapturer.this.audioState.getLastKnownFocusState()) {
                        case -3:
                            TreadlyVideoAudioCapturer.this.audioManager.setStreamVolume(0, TreadlyVideoAudioCapturer.this.audioState.getLastStreamVolume(), 0);
                            break;
                        case -2:
                        case -1:
                            break;
                        default:
                            String str7 = TreadlyVideoAudioCapturer.LOG_TAG;
                            Log.d(str7, "focusChange = " + i);
                            break;
                    }
                    TreadlyVideoAudioCapturer.this.setOutputType(TreadlyVideoAudioCapturer.this.audioState.getLastOutputType());
                    TreadlyVideoAudioCapturer.this.connectBluetooth();
                    TreadlyVideoAudioCapturer.this.forceInvokeConnectBluetooth();
                    break;
                default:
                    String str8 = TreadlyVideoAudioCapturer.LOG_TAG;
                    Log.d(str8, "AudioManager.OnAudioFocusChangeListener.onAudioFocusChange(" + i + "): default");
                    break;
            }
            TreadlyVideoAudioCapturer.this.audioState.setLastOutputType(TreadlyVideoAudioCapturer.this.getOutputType());
            TreadlyVideoAudioCapturer.this.audioState.setLastKnownFocusState(i);
        }
    };
    private Runnable captureThread = new Runnable() { // from class: com.treadly.Treadly.UI.TreadlyVideo.PrivateStream.VideoComponents.-$$Lambda$TreadlyVideoAudioCapturer$8ijHb2m5-_XW1DbvCVWXwSKeKrU
        @Override // java.lang.Runnable
        public final void run() {
            TreadlyVideoAudioCapturer.lambda$new$0(TreadlyVideoAudioCapturer.this);
        }
    };
    private Runnable renderThread = new Runnable() { // from class: com.treadly.Treadly.UI.TreadlyVideo.PrivateStream.VideoComponents.-$$Lambda$TreadlyVideoAudioCapturer$BjqQyxMRb83oa_mROO-8hR6yIG4
        @Override // java.lang.Runnable
        public final void run() {
            TreadlyVideoAudioCapturer.lambda$new$1(TreadlyVideoAudioCapturer.this);
        }
    };
    private final BluetoothProfile.ServiceListener bluetoothProfileServiceListener = new BluetoothProfile.ServiceListener() { // from class: com.treadly.Treadly.UI.TreadlyVideo.PrivateStream.VideoComponents.TreadlyVideoAudioCapturer.6
        @Override // android.bluetooth.BluetoothProfile.ServiceListener
        public void onServiceConnected(int i, BluetoothProfile bluetoothProfile) {
            Log.d(TreadlyVideoAudioCapturer.LOG_TAG, "BluetoothProfile.ServiceListener.onServiceConnected()");
            if (1 == i) {
                TreadlyVideoAudioCapturer.this.bluetoothProfile = bluetoothProfile;
                List<BluetoothDevice> connectedDevices = bluetoothProfile.getConnectedDevices();
                Log.d(TreadlyVideoAudioCapturer.LOG_TAG, "Service Proxy Connected");
                if (connectedDevices.isEmpty() || 2 != bluetoothProfile.getConnectionState(connectedDevices.get(0))) {
                    return;
                }
                Intent intent = new Intent("android.bluetooth.headset.profile.action.CONNECTION_STATE_CHANGED");
                intent.putExtra("android.bluetooth.profile.extra.STATE", 2);
                TreadlyVideoAudioCapturer.this.bluetoothBroadcastReceiver.onReceive(TreadlyVideoAudioCapturer.this.context, intent);
            }
        }

        @Override // android.bluetooth.BluetoothProfile.ServiceListener
        public void onServiceDisconnected(int i) {
            Log.d(TreadlyVideoAudioCapturer.LOG_TAG, "BluetoothProfile.ServiceListener.onServiceDisconnected()");
        }
    };

    /* loaded from: classes2.dex */
    public enum BluetoothState {
        Connected,
        Disconnected
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes2.dex */
    public enum OutputType {
        SPEAKER_PHONE,
        EAR_PIECE,
        HEAD_PHONES,
        BLUETOOTH
    }

    /* JADX INFO: Access modifiers changed from: private */
    public OutputType getOutputType() {
        return this.audioOutputType;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void setOutputType(OutputType outputType) {
        this.audioOutputType = outputType;
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes2.dex */
    public static class AudioManagerMode {
        private int oldMode = 0;
        private int naquire = 0;

        AudioManagerMode() {
        }

        void acquireMode(AudioManager audioManager) {
            int i = this.naquire;
            this.naquire = i + 1;
            if (i == 0) {
                this.oldMode = audioManager.getMode();
                audioManager.setMode(3);
            }
        }

        void releaseMode(AudioManager audioManager) {
            int i = this.naquire - 1;
            this.naquire = i;
            if (i == 0) {
                audioManager.setMode(this.oldMode);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes2.dex */
    public static class AudioState {
        private int lastKnownFocusState;
        private OutputType lastOutputType;
        private int lastStreamVolume;

        private AudioState() {
            this.lastStreamVolume = 0;
            this.lastKnownFocusState = 0;
            this.lastOutputType = OutputType.SPEAKER_PHONE;
        }

        int getLastStreamVolume() {
            return this.lastStreamVolume;
        }

        void setLastStreamVolume(int i) {
            this.lastStreamVolume = i;
        }

        int getLastKnownFocusState() {
            return this.lastKnownFocusState;
        }

        void setLastKnownFocusState(int i) {
            this.lastKnownFocusState = i;
        }

        OutputType getLastOutputType() {
            return this.lastOutputType;
        }

        void setLastOutputType(OutputType outputType) {
            this.lastOutputType = outputType;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void restoreAudioAfterBluetoothDisconnect() {
        Log.d(LOG_TAG, "restoreAudioAfterBluetoothDisconnect()");
        if (this.audioManager.isWiredHeadsetOn()) {
            Log.d(LOG_TAG, "wiredHeadsetOn");
            setOutputType(OutputType.HEAD_PHONES);
            this.audioManager.setSpeakerphoneOn(false);
            return;
        }
        Log.d(LOG_TAG, "wireHeadsetOff");
        if (this.audioState.getLastOutputType() == OutputType.SPEAKER_PHONE) {
            Log.d(LOG_TAG, "lastOutputType speakerPhone");
            setOutputType(OutputType.SPEAKER_PHONE);
            super.setOutputMode(BaseAudioDevice.OutputMode.SpeakerPhone);
            this.audioManager.setSpeakerphoneOn(true);
        } else if (this.audioState.getLastOutputType() == OutputType.EAR_PIECE) {
            Log.d(LOG_TAG, "lastOutputType earPiece");
            setOutputType(OutputType.EAR_PIECE);
            super.setOutputMode(BaseAudioDevice.OutputMode.Handset);
            this.audioManager.setSpeakerphoneOn(false);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* renamed from: com.treadly.Treadly.UI.TreadlyVideo.PrivateStream.VideoComponents.TreadlyVideoAudioCapturer$4  reason: invalid class name */
    /* loaded from: classes2.dex */
    public class AnonymousClass4 extends BroadcastReceiver {
        AnonymousClass4() {
        }

        @Override // android.content.BroadcastReceiver
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (action != null && action.equals("android.bluetooth.headset.profile.action.CONNECTION_STATE_CHANGED")) {
                int intExtra = intent.getIntExtra("android.bluetooth.profile.extra.STATE", -1);
                if (intExtra != 0) {
                    switch (intExtra) {
                        case 2:
                            Log.d(TreadlyVideoAudioCapturer.LOG_TAG, "bluetoothBroadcastReceiver.onReceive(): BluetoothHeadset.STATE_CONNECTED");
                            new Handler().postDelayed(new Runnable() { // from class: com.treadly.Treadly.UI.TreadlyVideo.PrivateStream.VideoComponents.-$$Lambda$TreadlyVideoAudioCapturer$4$wz3bNGmbzEuAQVzKdfFUkg_iXsA
                                @Override // java.lang.Runnable
                                public final void run() {
                                    TreadlyVideoAudioCapturer.this.connectBluetooth();
                                }
                            }, 2000L);
                            return;
                        case 3:
                            Log.d(TreadlyVideoAudioCapturer.LOG_TAG, "bluetoothBroadcastReceiver.onReceive(): BluetoothHeadset.STATE_DISCONNECTING");
                            return;
                        default:
                            return;
                    }
                }
                Log.d(TreadlyVideoAudioCapturer.LOG_TAG, "bluetoothBroadcastReceiver.onReceive(): BluetoothHeadset.STATE_DISCONNECTED");
                TreadlyVideoAudioCapturer.this.stopBluetoothSco();
                TreadlyVideoAudioCapturer.this.audioManager.setBluetoothScoOn(false);
            } else if (action == null || !action.equals("android.media.ACTION_SCO_AUDIO_STATE_UPDATED")) {
            } else {
                switch (intent.getIntExtra("android.media.extra.SCO_AUDIO_STATE", -1)) {
                    case -1:
                        Log.d(TreadlyVideoAudioCapturer.LOG_TAG, "bluetoothBroadcastReceiver.onReceive(): AudioManager.SCO_AUDIO_STATE_ERROR");
                        return;
                    case 0:
                        Log.d(TreadlyVideoAudioCapturer.LOG_TAG, "bluetoothBroadcastReceiver.onReceive(): AudioManager.SCO_AUDIO_STATE_DISCONNECTED");
                        if (TreadlyVideoAudioCapturer.this.mediaRouter.getSelectedRoute(1).equals(TreadlyVideoAudioCapturer.this.mediaRouter.getDefaultRoute())) {
                            Log.d(TreadlyVideoAudioCapturer.LOG_TAG, "default route used");
                            TreadlyVideoAudioCapturer.this.restoreAudioAfterBluetoothDisconnect();
                            TreadlyVideoAudioCapturer.this.bluetoothState = BluetoothState.Disconnected;
                            return;
                        }
                        String str = TreadlyVideoAudioCapturer.LOG_TAG;
                        Log.d(str, "non default route: " + ((Object) TreadlyVideoAudioCapturer.this.mediaRouter.getSelectedRoute(1).getName()));
                        String str2 = TreadlyVideoAudioCapturer.LOG_TAG;
                        Log.d(str2, "default: " + ((Object) TreadlyVideoAudioCapturer.this.mediaRouter.getDefaultRoute().getName()));
                        TreadlyVideoAudioCapturer.this.bluetoothState = BluetoothState.Connected;
                        return;
                    case 1:
                        Log.d(TreadlyVideoAudioCapturer.LOG_TAG, "bluetoothBroadcastReceiver.onReceive(): AudioManager.SCO_AUDIO_STATE_CONNECTED");
                        TreadlyVideoAudioCapturer.this.bluetoothState = BluetoothState.Connected;
                        TreadlyVideoAudioCapturer.this.setOutputType(OutputType.BLUETOOTH);
                        TreadlyVideoAudioCapturer.super.setOutputMode(BaseAudioDevice.OutputMode.Handset);
                        return;
                    case 2:
                        Log.d(TreadlyVideoAudioCapturer.LOG_TAG, "bluetoothBroadcastReceiver.onReceive(): AudioManager.SCO_AUDIO_STATE_CONNECTING");
                        return;
                    default:
                        return;
                }
            }
        }
    }

    private void startRendererAndCapturer() {
        if (this.wasRendering) {
            startRenderer();
        }
        if (this.wasCapturing) {
            startCapturer();
        }
    }

    private void stopRendererAndCapturer() {
        if (this.isRendering) {
            stopRenderer();
            this.wasRendering = true;
        }
        if (this.isCapturing) {
            stopCapturer();
            this.wasCapturing = true;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void connectBluetooth() {
        Log.d(LOG_TAG, "connectBluetooth() called");
    }

    public TreadlyVideoAudioCapturer(Context context) {
        this.outputSamplingRate = DEFAULT_SAMPLE_RATE;
        this.samplesPerBuffer = DEFAULT_SAMPLES_PER_BUFFER;
        this.context = context;
        int i = DEFAULT_BUFFER_SIZE;
        try {
            this.recBuffer = ByteBuffer.allocateDirect(DEFAULT_BUFFER_SIZE);
        } catch (Exception e) {
            Log.e(LOG_TAG, e.getMessage());
        }
        this.tempBufRec = new byte[DEFAULT_BUFFER_SIZE];
        this.audioManager = (AudioManager) context.getSystemService(MediaStreamTrack.AUDIO_TRACK_KIND);
        this.mediaRouter = (MediaRouter) context.getSystemService("media_router");
        this.bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        this.bluetoothProfile = null;
        if (Build.VERSION.SDK_INT > 16) {
            try {
                this.outputSamplingRate = Integer.parseInt(this.audioManager.getProperty("android.media.property.OUTPUT_SAMPLE_RATE"));
                try {
                    this.samplesPerBuffer = Integer.parseInt(this.audioManager.getProperty("android.media.property.OUTPUT_FRAMES_PER_BUFFER"));
                    int i2 = this.samplesPerBuffer * 2 * 1;
                    if (i2 == 0) {
                        this.samplesPerBuffer = DEFAULT_SAMPLES_PER_BUFFER;
                    } else {
                        i = i2;
                    }
                } catch (NumberFormatException e2) {
                    String str = LOG_TAG;
                    Log.e(str, "DefaultAudioDevice(): " + e2.getMessage());
                }
            } finally {
                if (this.outputSamplingRate == 0) {
                    this.outputSamplingRate = DEFAULT_SAMPLE_RATE;
                }
            }
        }
        try {
            this.playBuffer = ByteBuffer.allocateDirect(i);
        } catch (Exception e3) {
            Log.e(LOG_TAG, e3.getMessage());
        }
        this.tempBufPlay = new byte[i];
        this.captureSettings = new BaseAudioDevice.AudioSettings(this.captureSamplingRate, 1);
        this.rendererSettings = new BaseAudioDevice.AudioSettings(this.outputSamplingRate, 1);
        try {
            this.telephonyManager = (TelephonyManager) context.getSystemService("phone");
        } catch (SecurityException e4) {
            e4.printStackTrace();
        }
        this.isPhoneStateListenerRegistered = false;
        this.wasCapturing = false;
        this.wasRendering = false;
        this.isPaused = false;
        String str2 = LOG_TAG;
        Log.d(str2, "DefaultAudioDevice() exit  " + this);
    }

    @Override // com.opentok.android.BaseAudioDevice
    public boolean initCapturer() {
        int minBufferSize = AudioRecord.getMinBufferSize(this.captureSettings.getSampleRate(), 16, 2);
        int i = minBufferSize * 2;
        if (this.noiseSuppressor != null) {
            this.noiseSuppressor.release();
            this.noiseSuppressor = null;
        }
        if (this.echoCanceler != null) {
            this.echoCanceler.release();
            this.echoCanceler = null;
        }
        if (this.audioRecord != null) {
            this.audioRecord.release();
            this.audioRecord = null;
        }
        try {
            this.audioRecord = new AudioRecord(7, this.captureSettings.getSampleRate(), 16, 2, i);
            if (NoiseSuppressor.isAvailable()) {
                this.noiseSuppressor = NoiseSuppressor.create(this.audioRecord.getAudioSessionId());
            }
            if (AcousticEchoCanceler.isAvailable()) {
                this.echoCanceler = AcousticEchoCanceler.create(this.audioRecord.getAudioSessionId());
            }
            if (this.audioRecord.getState() != 1) {
                String format = String.format(Locale.getDefault(), "Audio capture could not be initialized.\nRequested parameters\n  Sampling Rate: %d\n  Number of channels: %d\n  Buffer size: %d\n", Integer.valueOf(this.captureSettings.getSampleRate()), Integer.valueOf(this.captureSettings.getNumChannels()), Integer.valueOf(minBufferSize));
                Log.e(LOG_TAG, format);
                throw new RuntimeException(format);
            }
            this.shutdownCaptureThread = false;
            new Thread(this.captureThread).start();
            return true;
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    @Override // com.opentok.android.BaseAudioDevice
    public boolean destroyCapturer() {
        this.captureLock.lock();
        if (this.echoCanceler != null) {
            this.echoCanceler.release();
            this.echoCanceler = null;
        }
        if (this.noiseSuppressor != null) {
            this.noiseSuppressor.release();
            this.noiseSuppressor = null;
        }
        this.audioRecord.release();
        this.audioRecord = null;
        this.shutdownCaptureThread = true;
        this.captureEvent.signal();
        this.captureLock.unlock();
        this.wasCapturing = false;
        return true;
    }

    @Override // com.opentok.android.BaseAudioDevice
    public int getEstimatedCaptureDelay() {
        return this.estimatedCaptureDelay;
    }

    @Override // com.opentok.android.BaseAudioDevice
    public boolean startCapturer() {
        if (this.audioRecord == null) {
            throw new IllegalStateException("startCapturer(): startRecording() called on an uninitialized AudioRecord.");
        }
        try {
            this.audioRecord.startRecording();
            this.captureLock.lock();
            this.isCapturing = true;
            this.captureEvent.signal();
            this.captureLock.unlock();
            return true;
        } catch (IllegalStateException e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    @Override // com.opentok.android.BaseAudioDevice
    public boolean stopCapturer() {
        if (this.audioRecord == null) {
            throw new IllegalStateException("stopCapturer(): stop() called on an uninitialized AudioRecord.");
        }
        this.captureLock.lock();
        try {
            try {
                if (this.audioRecord.getRecordingState() == 3) {
                    this.audioRecord.stop();
                }
                this.isCapturing = false;
                this.captureLock.unlock();
                return true;
            } catch (Exception e) {
                throw new RuntimeException(e.getMessage());
            }
        } catch (Throwable th) {
            this.isCapturing = false;
            this.captureLock.unlock();
            throw th;
        }
    }

    public static /* synthetic */ void lambda$new$0(TreadlyVideoAudioCapturer treadlyVideoAudioCapturer) {
        int i = treadlyVideoAudioCapturer.captureSamplingRate / 100;
        try {
            Process.setThreadPriority(-19);
        } catch (Exception e) {
            String str = LOG_TAG;
            Log.e(str, "android.os.Process.setThreadPriority(): " + e.getMessage());
        }
        while (!treadlyVideoAudioCapturer.shutdownCaptureThread) {
            treadlyVideoAudioCapturer.captureLock.lock();
            try {
                try {
                    if (!treadlyVideoAudioCapturer.isCapturing) {
                        treadlyVideoAudioCapturer.captureEvent.await();
                    } else if (treadlyVideoAudioCapturer.audioRecord != null) {
                        int read = treadlyVideoAudioCapturer.audioRecord.read(treadlyVideoAudioCapturer.tempBufRec, 0, (i << 1) * 1);
                        if (read >= 0) {
                            treadlyVideoAudioCapturer.recBuffer.rewind();
                            treadlyVideoAudioCapturer.recBuffer.put(treadlyVideoAudioCapturer.tempBufRec);
                            int i2 = (read >> 1) / 1;
                            treadlyVideoAudioCapturer.captureLock.unlock();
                            treadlyVideoAudioCapturer.getAudioBus().writeCaptureData(treadlyVideoAudioCapturer.recBuffer, i2);
                            treadlyVideoAudioCapturer.estimatedCaptureDelay = (i2 * 1000) / treadlyVideoAudioCapturer.captureSamplingRate;
                        } else {
                            switch (read) {
                                case -3:
                                    throw new RuntimeException("captureThread(): AudioRecord.ERROR_INVALID_OPERATION");
                                case -2:
                                    throw new RuntimeException("captureThread(): AudioRecord.ERROR_BAD_VALUE");
                                default:
                                    throw new RuntimeException("captureThread(): AudioRecord.ERROR or default");
                            }
                        }
                    }
                } catch (Exception e2) {
                    throw new RuntimeException(e2.getMessage());
                }
            } finally {
                treadlyVideoAudioCapturer.captureLock.unlock();
            }
        }
    }

    @Override // com.opentok.android.BaseAudioDevice
    public boolean initRenderer() {
        if (this.audioManager.requestAudioFocus(this.audioFocusChangeListener, 0, 3) == 1) {
            Log.d("AUDIO_FOCUS", "Audio Focus request GRANTED !");
            this.bluetoothState = BluetoothState.Disconnected;
            enableBluetoothEvents();
            int minBufferSize = AudioTrack.getMinBufferSize(this.rendererSettings.getSampleRate(), 4, 2);
            if (this.audioTrack != null) {
                this.audioTrack.release();
                this.audioTrack = null;
            }
            try {
                int sampleRate = this.rendererSettings.getSampleRate();
                if (minBufferSize < 6000) {
                    minBufferSize *= 2;
                }
                this.audioTrack = new AudioTrack(0, sampleRate, 4, 2, minBufferSize, 1);
                if (this.audioTrack.getState() != 1) {
                    throw new RuntimeException("Audio renderer not initialized " + this.rendererSettings.getSampleRate());
                }
                this.bufferedPlaySamples = 0;
                this.shutdownRenderThread = false;
                new Thread(this.renderThread).start();
                return true;
            } catch (Exception e) {
                throw new RuntimeException(e.getMessage());
            }
        }
        Log.e("AUDIO_FOCUS", "Audio Focus request DENIED !");
        return false;
    }

    private void destroyAudioTrack() {
        this.rendererLock.lock();
        this.audioTrack.release();
        this.audioTrack = null;
        this.shutdownRenderThread = true;
        this.renderEvent.signal();
        this.rendererLock.unlock();
    }

    @Override // com.opentok.android.BaseAudioDevice
    public boolean destroyRenderer() {
        destroyAudioTrack();
        disableBluetoothEvents();
        unregisterHeadsetReceiver();
        this.audioManager.setSpeakerphoneOn(false);
        this.audioManager.abandonAudioFocus(this.audioFocusChangeListener);
        this.wasRendering = false;
        return true;
    }

    @Override // com.opentok.android.BaseAudioDevice
    public int getEstimatedRenderDelay() {
        return this.estimatedRenderDelay;
    }

    @Override // com.opentok.android.BaseAudioDevice
    public boolean startRenderer() {
        Log.d("AUDIO_FOCUS", "Start Renderer");
        synchronized (this.bluetoothLock) {
            if (BluetoothState.Connected != this.bluetoothState) {
                if (this.audioManager.isWiredHeadsetOn()) {
                    Log.d(LOG_TAG, "Turn off Speaker phone");
                    this.audioManager.setSpeakerphoneOn(false);
                } else if (this.mediaRouter.getDefaultRoute().equals(this.mediaRouter.getSelectedRoute(1))) {
                    Log.d(LOG_TAG, "Turn on Speaker phone");
                    if (getOutputType() == OutputType.SPEAKER_PHONE) {
                        this.audioManager.setSpeakerphoneOn(true);
                    }
                }
            }
        }
        if (this.audioTrack == null) {
            throw new IllegalStateException("startRenderer(): play() called on uninitialized AudioTrack.");
        }
        try {
            this.audioTrack.play();
            this.rendererLock.lock();
            this.isRendering = true;
            this.renderEvent.signal();
            this.rendererLock.unlock();
            registerBtReceiver();
            registerHeadsetReceiver();
            return true;
        } catch (IllegalStateException e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    @Override // com.opentok.android.BaseAudioDevice
    public boolean stopRenderer() {
        Log.d("AUDIO_FOCUS", "Stop Renderer");
        if (this.audioTrack == null) {
            throw new IllegalStateException("stopRenderer(): stop() called on uninitialized AudioTrack.");
        }
        this.rendererLock.lock();
        try {
            try {
                if (this.audioTrack.getPlayState() == 3) {
                    this.audioTrack.stop();
                }
                this.audioTrack.flush();
                this.isRendering = false;
                this.rendererLock.unlock();
                this.audioManagerMode.releaseMode(this.audioManager);
                unregisterHeadsetReceiver();
                unregisterBtReceiver();
                return true;
            } catch (Exception e) {
                throw new RuntimeException(e.getMessage());
            }
        } catch (Throwable th) {
            this.isRendering = false;
            this.rendererLock.unlock();
            throw th;
        }
    }

    public static /* synthetic */ void lambda$new$1(TreadlyVideoAudioCapturer treadlyVideoAudioCapturer) {
        int i = treadlyVideoAudioCapturer.samplesPerBuffer;
        try {
            Process.setThreadPriority(-19);
        } catch (Exception e) {
            Log.e(LOG_TAG, "android.os.Process.setThreadPriority(): " + e.getMessage());
        }
        while (!treadlyVideoAudioCapturer.shutdownRenderThread) {
            treadlyVideoAudioCapturer.rendererLock.lock();
            try {
                try {
                    if (!treadlyVideoAudioCapturer.isRendering) {
                        treadlyVideoAudioCapturer.renderEvent.await();
                    } else {
                        treadlyVideoAudioCapturer.rendererLock.unlock();
                        treadlyVideoAudioCapturer.playBuffer.clear();
                        int readRenderData = treadlyVideoAudioCapturer.getAudioBus().readRenderData(treadlyVideoAudioCapturer.playBuffer, i);
                        treadlyVideoAudioCapturer.rendererLock.lock();
                        if (treadlyVideoAudioCapturer.audioTrack != null && treadlyVideoAudioCapturer.isRendering) {
                            int i2 = (readRenderData << 1) * 1;
                            treadlyVideoAudioCapturer.playBuffer.get(treadlyVideoAudioCapturer.tempBufPlay, 0, i2);
                            int write = treadlyVideoAudioCapturer.audioTrack.write(treadlyVideoAudioCapturer.tempBufPlay, 0, i2);
                            if (write > 0) {
                                treadlyVideoAudioCapturer.bufferedPlaySamples += (write >> 1) / 1;
                                int playbackHeadPosition = treadlyVideoAudioCapturer.audioTrack.getPlaybackHeadPosition();
                                if (playbackHeadPosition < treadlyVideoAudioCapturer.playPosition) {
                                    treadlyVideoAudioCapturer.playPosition = 0;
                                }
                                treadlyVideoAudioCapturer.bufferedPlaySamples -= playbackHeadPosition - treadlyVideoAudioCapturer.playPosition;
                                treadlyVideoAudioCapturer.playPosition = playbackHeadPosition;
                                treadlyVideoAudioCapturer.estimatedRenderDelay = (treadlyVideoAudioCapturer.bufferedPlaySamples * 1000) / treadlyVideoAudioCapturer.outputSamplingRate;
                            } else {
                                switch (write) {
                                    case -3:
                                        throw new RuntimeException("renderThread(): AudioTrack.ERROR_INVALID_OPERATION");
                                    case -2:
                                        throw new RuntimeException("renderThread(): AudioTrack.ERROR_BAD_VALUE");
                                    default:
                                        throw new RuntimeException("renderThread(): AudioTrack.ERROR or default");
                                }
                            }
                        }
                    }
                } finally {
                    treadlyVideoAudioCapturer.rendererLock.unlock();
                }
            } catch (Exception e2) {
                throw new RuntimeException(e2.getMessage());
            }
        }
    }

    @Override // com.opentok.android.BaseAudioDevice
    public BaseAudioDevice.AudioSettings getCaptureSettings() {
        return this.captureSettings;
    }

    @Override // com.opentok.android.BaseAudioDevice
    public BaseAudioDevice.AudioSettings getRenderSettings() {
        return this.rendererSettings;
    }

    @Override // com.opentok.android.BaseAudioDevice
    public boolean setOutputMode(BaseAudioDevice.OutputMode outputMode) {
        Log.d("AUDIO_FOCUS", "outputmode set to : " + outputMode);
        super.setOutputMode(outputMode);
        if (BaseAudioDevice.OutputMode.SpeakerPhone == outputMode) {
            this.audioState.setLastOutputType(getOutputType());
            setOutputType(OutputType.SPEAKER_PHONE);
            this.audioManager.setSpeakerphoneOn(true);
            stopBluetoothSco();
            this.audioManager.setBluetoothScoOn(false);
        } else if (this.audioState.getLastOutputType() == OutputType.BLUETOOTH || this.bluetoothState == BluetoothState.Connected) {
            connectBluetooth();
        } else {
            this.audioState.setLastOutputType(getOutputType());
            this.audioManager.setSpeakerphoneOn(false);
            setOutputType(OutputType.EAR_PIECE);
            stopBluetoothSco();
            this.audioManager.setBluetoothScoOn(false);
        }
        return true;
    }

    private void registerHeadsetReceiver() {
        String str = LOG_TAG;
        Log.d(str, "registerHeadsetReceiver() called ... isHeadsetReceiverRegistered = " + this.isHeadsetReceiverRegistered);
        if (this.isHeadsetReceiverRegistered) {
            return;
        }
        this.context.registerReceiver(this.headsetBroadcastReceiver, new IntentFilter("android.intent.action.HEADSET_PLUG"));
        this.isHeadsetReceiverRegistered = true;
    }

    private void unregisterHeadsetReceiver() {
        String str = LOG_TAG;
        Log.d(str, "unregisterHeadsetReceiver() called .. isHeadsetReceiverRegistered = " + this.isHeadsetReceiverRegistered);
        if (this.isHeadsetReceiverRegistered) {
            this.context.unregisterReceiver(this.headsetBroadcastReceiver);
            this.isHeadsetReceiverRegistered = false;
        }
    }

    private void registerBtReceiver() {
        String str = LOG_TAG;
        Log.d(str, "registerBtReceiver() called .. isBluetoothHeadSetReceiverRegistered = " + this.isBluetoothHeadSetReceiverRegistered);
        if (this.isBluetoothHeadSetReceiverRegistered) {
            return;
        }
        IntentFilter intentFilter = new IntentFilter("android.bluetooth.headset.profile.action.CONNECTION_STATE_CHANGED");
        intentFilter.addAction("android.media.ACTION_SCO_AUDIO_STATE_UPDATED");
        intentFilter.addAction("android.bluetooth.a2dp.profile.action.CONNECTION_STATE_CHANGED");
        this.context.registerReceiver(this.bluetoothBroadcastReceiver, intentFilter);
        this.context.registerReceiver(this.bluetoothHeadsetReceiver, new IntentFilter("android.bluetooth.headset.profile.action.AUDIO_STATE_CHANGED"));
        this.context.registerReceiver(this.bluetoothSpeakerReceiver, new IntentFilter("android.bluetooth.a2dp.profile.action.PLAYING_STATE_CHANGED"));
        this.isBluetoothHeadSetReceiverRegistered = true;
    }

    private void unregisterBtReceiver() {
        String str = LOG_TAG;
        Log.d(str, "unregisterBtReceiver() called .. bluetoothHeadSetReceiverRegistered = " + this.isBluetoothHeadSetReceiverRegistered);
        if (this.isBluetoothHeadSetReceiverRegistered) {
            this.context.unregisterReceiver(this.bluetoothBroadcastReceiver);
            this.context.unregisterReceiver(this.bluetoothHeadsetReceiver);
            this.context.unregisterReceiver(this.bluetoothSpeakerReceiver);
            this.isBluetoothHeadSetReceiverRegistered = false;
        }
    }

    @Override // com.opentok.android.BaseAudioDevice
    public synchronized void onPause() {
        this.audioState.setLastOutputType(getOutputType());
        unregisterBtReceiver();
        unregisterHeadsetReceiver();
        this.isPaused = true;
    }

    @Override // com.opentok.android.BaseAudioDevice
    public synchronized void onResume() {
        Log.d(LOG_TAG, "onResume() called");
        if (this.isPaused) {
            if (this.bluetoothState == BluetoothState.Disconnected && this.isRendering && this.audioState.getLastOutputType() == OutputType.SPEAKER_PHONE && !this.audioManager.isWiredHeadsetOn()) {
                Log.d(LOG_TAG, "onResume() - Set Speaker Phone ON True");
                this.audioManager.setSpeakerphoneOn(true);
            }
            registerBtReceiver();
            registerHeadsetReceiver();
            connectBluetooth();
            forceInvokeConnectBluetooth();
            this.isPaused = false;
        }
    }

    private void enableBluetoothEvents() {
        if (this.audioManager.isBluetoothScoAvailableOffCall()) {
            registerBtReceiver();
        }
    }

    private void disableBluetoothEvents() {
        if (this.bluetoothProfile != null && this.bluetoothAdapter != null) {
            this.bluetoothAdapter.closeProfileProxy(1, this.bluetoothProfile);
        }
        unregisterBtReceiver();
        Intent intent = new Intent("android.bluetooth.headset.profile.action.CONNECTION_STATE_CHANGED");
        intent.putExtra("android.bluetooth.profile.extra.STATE", 0);
        this.bluetoothBroadcastReceiver.onReceive(this.context, intent);
    }

    private void startBluetoothSco() {
        try {
            this.audioManager.startBluetoothSco();
        } catch (NullPointerException e) {
            Log.d(LOG_TAG, e.getMessage());
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void stopBluetoothSco() {
        try {
            this.audioManager.stopBluetoothSco();
        } catch (NullPointerException e) {
            Log.d(LOG_TAG, e.getMessage());
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void forceInvokeConnectBluetooth() {
        Log.d(LOG_TAG, "forceConnectBluetooth() called");
        synchronized (this.bluetoothLock) {
            this.bluetoothState = BluetoothState.Disconnected;
            if (this.bluetoothAdapter != null) {
                this.bluetoothAdapter.getProfileProxy(this.context, this.bluetoothProfileServiceListener, 1);
            }
        }
    }
}
