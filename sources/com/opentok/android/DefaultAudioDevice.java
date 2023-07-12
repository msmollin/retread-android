package com.opentok.android;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothProfile;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.AudioDeviceInfo;
import android.media.AudioManager;
import android.media.AudioRecord;
import android.media.AudioTrack;
import android.media.audiofx.AcousticEchoCanceler;
import android.media.audiofx.NoiseSuppressor;
import android.os.Build;
import android.os.Handler;
import android.os.Process;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import com.opentok.android.BaseAudioDevice;
import com.opentok.android.DefaultAudioDevice;
import com.opentok.android.OtLog;
import java.nio.ByteBuffer;
import java.util.Locale;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: classes.dex */
public class DefaultAudioDevice extends BaseAudioDevice {
    private static final int DEFAULT_BLUETOOTH_SCO_START_DELAY = 2000;
    private static final int DEFAULT_BUFFER_SIZE = 1760;
    private static final int DEFAULT_SAMPLES_PER_BUFFER = 440;
    private static final int DEFAULT_SAMPLE_RATE = 44100;
    private static final int DEFAULT_START_RENDERER_AND_CAPTURER_DELAY = 5000;
    private static final String HEADSET_PLUG_STATE_KEY = "state";
    private static final int NUM_CHANNELS_CAPTURING = 1;
    private static final int NUM_CHANNELS_RENDERING = 1;
    private static final int SAMPLE_SIZE_IN_BYTES = 2;
    private static final int STEREO_CHANNELS = 2;
    private AudioManager.OnAudioFocusChangeListener audioFocusChangeListener;
    private AudioManager audioManager;
    private AudioManagerMode audioManagerMode;
    private OutputType audioOutputType;
    private AudioRecord audioRecord;
    private AudioState audioState;
    private AudioTrack audioTrack;
    private BluetoothAdapter bluetoothAdapter;
    private final BroadcastReceiver bluetoothBroadcastReceiver;
    private final BroadcastReceiver bluetoothHeadsetReceiver;
    private final Object bluetoothLock;
    private BluetoothProfile bluetoothProfile;
    private final BluetoothProfile.ServiceListener bluetoothProfileServiceListener;
    private BaseAudioDevice.BluetoothState bluetoothState;
    private int bufferedPlaySamples;
    private final Condition captureEvent;
    private final ReentrantLock captureLock;
    private int captureSamplingRate;
    private BaseAudioDevice.AudioSettings captureSettings;
    private Runnable captureThread;
    private Context context;
    private AcousticEchoCanceler echoCanceler;
    private int estimatedCaptureDelay;
    private int estimatedRenderDelay;
    private BroadcastReceiver headsetBroadcastReceiver;
    private boolean isBluetoothHeadSetReceiverRegistered;
    private volatile boolean isCapturing;
    private boolean isHeadsetReceiverRegistered;
    private boolean isPaused;
    private boolean isPhoneStateListenerRegistered;
    private volatile boolean isRendering;
    private final OtLog.LogToken log = new OtLog.LogToken(this);
    private NoiseSuppressor noiseSuppressor;
    private int outputSamplingRate;
    private PhoneStateListener phoneStateListener;
    private ByteBuffer playBuffer;
    private int playPosition;
    private ByteBuffer recBuffer;
    private final Condition renderEvent;
    private Runnable renderThread;
    private final ReentrantLock rendererLock;
    private BaseAudioDevice.AudioSettings rendererSettings;
    private int samplesPerBuffer;
    private volatile boolean shutdownCaptureThread;
    private volatile boolean shutdownRenderThread;
    private TelephonyManager telephonyManager;
    private byte[] tempBufPlay;
    private byte[] tempBufRec;
    private boolean wasCapturing;
    private boolean wasRendering;

    /* JADX INFO: Access modifiers changed from: package-private */
    /* renamed from: com.opentok.android.DefaultAudioDevice$3  reason: invalid class name */
    /* loaded from: classes.dex */
    public class AnonymousClass3 extends BroadcastReceiver {
        AnonymousClass3() {
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void a() {
            DefaultAudioDevice.this.startBluetoothDevice();
        }

        @Override // android.content.BroadcastReceiver
        public void onReceive(Context context, Intent intent) {
            OtLog.LogToken logToken;
            Object[] objArr;
            String str;
            String action = intent.getAction();
            if (action != null && action.equals("android.bluetooth.headset.profile.action.CONNECTION_STATE_CHANGED")) {
                int intExtra = intent.getIntExtra("android.bluetooth.profile.extra.STATE", -1);
                if (intExtra == 0) {
                    DefaultAudioDevice.this.log.d("bluetoothBroadcastReceiver.onReceive(): BluetoothHeadset.STATE_DISCONNECTED", new Object[0]);
                    DefaultAudioDevice.this.stopBluetoothDevice();
                    return;
                } else if (intExtra == 2) {
                    DefaultAudioDevice.this.log.d("bluetoothBroadcastReceiver.onReceive(): BluetoothHeadset.STATE_CONNECTED", new Object[0]);
                    new Handler().postDelayed(new Runnable() { // from class: com.opentok.android.-$$Lambda$DefaultAudioDevice$3$fc91bL9weLHpdSEPcpZaBDGeUy8
                        @Override // java.lang.Runnable
                        public final void run() {
                            DefaultAudioDevice.AnonymousClass3.this.a();
                        }
                    }, 2000L);
                    return;
                } else if (intExtra != 3) {
                    return;
                } else {
                    logToken = DefaultAudioDevice.this.log;
                    objArr = new Object[0];
                    str = "bluetoothBroadcastReceiver.onReceive(): BluetoothHeadset.STATE_DISCONNECTING";
                }
            } else if (action == null || !action.equals("android.media.ACTION_SCO_AUDIO_STATE_UPDATED")) {
                return;
            } else {
                switch (intent.getIntExtra("android.media.extra.SCO_AUDIO_STATE", -1)) {
                    case -1:
                        logToken = DefaultAudioDevice.this.log;
                        objArr = new Object[0];
                        str = "bluetoothBroadcastReceiver.onReceive(): AudioManager.SCO_AUDIO_STATE_ERROR";
                        break;
                    case 0:
                        DefaultAudioDevice.this.log.d("bluetoothBroadcastReceiver.onReceive(): AudioManager.SCO_AUDIO_STATE_DISCONNECTED", new Object[0]);
                        DefaultAudioDevice.this.restoreAudioAfterBluetoothDisconnect();
                        DefaultAudioDevice.this.bluetoothState = BaseAudioDevice.BluetoothState.Disconnected;
                        return;
                    case 1:
                        DefaultAudioDevice.this.log.d("bluetoothBroadcastReceiver.onReceive(): AudioManager.SCO_AUDIO_STATE_CONNECTED", new Object[0]);
                        DefaultAudioDevice.this.bluetoothState = BaseAudioDevice.BluetoothState.Connected;
                        DefaultAudioDevice.this.setOutputType(OutputType.BLUETOOTH);
                        DefaultAudioDevice.super.setOutputMode(BaseAudioDevice.OutputMode.Handset);
                        DefaultAudioDevice.this.startBluetoothDevice();
                        return;
                    case 2:
                        logToken = DefaultAudioDevice.this.log;
                        objArr = new Object[0];
                        str = "bluetoothBroadcastReceiver.onReceive(): AudioManager.SCO_AUDIO_STATE_CONNECTING";
                        break;
                    default:
                        return;
                }
            }
            logToken.d(str, objArr);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* renamed from: com.opentok.android.DefaultAudioDevice$4  reason: invalid class name */
    /* loaded from: classes.dex */
    public class AnonymousClass4 extends PhoneStateListener {
        AnonymousClass4() {
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void a() {
            DefaultAudioDevice.this.startRendererAndCapturer();
        }

        @Override // android.telephony.PhoneStateListener
        public void onCallStateChanged(int i, String str) {
            OtLog.LogToken logToken;
            Object[] objArr;
            String str2;
            DefaultAudioDevice.this.log.d("PhoneStateListener.onCallStateChanged() enter <-", new Object[0]);
            super.onCallStateChanged(i, str);
            if (i != 0) {
                if (i == 1) {
                    logToken = DefaultAudioDevice.this.log;
                    objArr = new Object[0];
                    str2 = "PhoneStateListener.onCallStateChanged(): TelephonyManager.CALL_STATE_RINGING";
                } else if (i != 2) {
                    DefaultAudioDevice.this.log.d("PhoneStateListener.onCallStateChanged() default", new Object[0]);
                } else {
                    logToken = DefaultAudioDevice.this.log;
                    objArr = new Object[0];
                    str2 = "PhoneStateListener.onCallStateChanged(): TelephonyManager.CALL_STATE_OFFHOOK";
                }
                logToken.d(str2, objArr);
                DefaultAudioDevice.this.stopRendererAndCapturer();
            } else {
                DefaultAudioDevice.this.log.d("PhoneStateListener.onCallStateChanged(): TelephonyManager.CALL_STATE_IDLE", new Object[0]);
                new Handler().postDelayed(new Runnable() { // from class: com.opentok.android.-$$Lambda$DefaultAudioDevice$4$69GhwQ-tgQH_M_utYP8OVOffsOA
                    @Override // java.lang.Runnable
                    public final void run() {
                        DefaultAudioDevice.AnonymousClass4.this.a();
                    }
                }, 5000L);
            }
            DefaultAudioDevice.this.log.d("PhoneStateListener.onCallStateChanged() exit ->", new Object[0]);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes.dex */
    public static class AudioManagerMode {
        private final OtLog.LogToken log = new OtLog.LogToken(this);
        private int oldMode = 0;
        private int naquire = 0;

        AudioManagerMode() {
        }

        void acquireMode(AudioManager audioManager) {
            this.log.d("AudioManagerMode.acquireMode() called", new Object[0]);
            int i = this.naquire;
            this.naquire = i + 1;
            if (i == 0) {
                this.oldMode = audioManager.getMode();
                audioManager.setMode(3);
            }
        }

        void releaseMode(AudioManager audioManager) {
            this.log.d("AudioManagerMode.releaseMode() called", new Object[0]);
            int i = this.naquire - 1;
            this.naquire = i;
            if (i == 0) {
                audioManager.setMode(this.oldMode);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes.dex */
    public static class AudioState {
        private int lastKnownFocusState;
        private OutputType lastOutputType;
        private int lastStreamVolume;
        private final OtLog.LogToken log;

        private AudioState() {
            this.lastStreamVolume = 0;
            this.lastKnownFocusState = 0;
            this.lastOutputType = OutputType.SPEAKER_PHONE;
            this.log = new OtLog.LogToken(this);
        }

        int getLastKnownFocusState() {
            return this.lastKnownFocusState;
        }

        OutputType getLastOutputType() {
            OtLog.LogToken logToken = this.log;
            logToken.d("AudioState.getLastOutputType() called.. LastOutputType = " + this.lastOutputType, new Object[0]);
            return this.lastOutputType;
        }

        int getLastStreamVolume() {
            return this.lastStreamVolume;
        }

        void setLastKnownFocusState(int i) {
            this.lastKnownFocusState = i;
        }

        void setLastOutputType(OutputType outputType) {
            OtLog.LogToken logToken = this.log;
            logToken.d("AudioState.setLastOutputType(" + outputType + ") called", new Object[0]);
            this.lastOutputType = outputType;
        }

        void setLastStreamVolume(int i) {
            this.lastStreamVolume = i;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes.dex */
    public enum OutputType {
        SPEAKER_PHONE,
        EAR_PIECE,
        HEAD_PHONES,
        BLUETOOTH
    }

    /* JADX WARN: Code restructure failed: missing block: B:10:0x00ec, code lost:
        if (r5 == 0) goto L9;
     */
    /* JADX WARN: Code restructure failed: missing block: B:17:0x0110, code lost:
        if (r9.outputSamplingRate != 0) goto L10;
     */
    /* JADX WARN: Code restructure failed: missing block: B:18:0x0112, code lost:
        r9.outputSamplingRate = com.opentok.android.DefaultAudioDevice.DEFAULT_SAMPLE_RATE;
     */
    /* JADX WARN: Code restructure failed: missing block: B:19:0x0114, code lost:
        r2 = java.lang.Integer.parseInt(r9.audioManager.getProperty("android.media.property.OUTPUT_FRAMES_PER_BUFFER"));
        r9.samplesPerBuffer = r2;
     */
    /* JADX WARN: Code restructure failed: missing block: B:20:0x0122, code lost:
        r2 = (r2 * 2) * 1;
     */
    /* JADX WARN: Code restructure failed: missing block: B:21:0x0125, code lost:
        if (r2 != 0) goto L15;
     */
    /* JADX WARN: Code restructure failed: missing block: B:22:0x0127, code lost:
        r9.samplesPerBuffer = com.opentok.android.DefaultAudioDevice.DEFAULT_SAMPLES_PER_BUFFER;
     */
    /* JADX WARN: Code restructure failed: missing block: B:23:0x012a, code lost:
        r4 = r2;
     */
    /* JADX WARN: Code restructure failed: missing block: B:26:0x012e, code lost:
        r2 = move-exception;
     */
    /* JADX WARN: Code restructure failed: missing block: B:27:0x012f, code lost:
        r3 = r9.log;
        r3.e("DefaultAudioDevice(): " + r2.getMessage(), new java.lang.Object[0]);
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public DefaultAudioDevice(android.content.Context r10) {
        /*
            Method dump skipped, instructions count: 429
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: com.opentok.android.DefaultAudioDevice.<init>(android.content.Context):void");
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void a() {
        int i = this.captureSamplingRate / 100;
        try {
            Process.setThreadPriority(-19);
        } catch (Exception e) {
            OtLog.LogToken logToken = this.log;
            logToken.e("android.os.Process.setThreadPriority(): " + e.getMessage(), new Object[0]);
        }
        while (!this.shutdownCaptureThread) {
            this.captureLock.lock();
            try {
                if (!this.isCapturing) {
                    this.captureEvent.await();
                } else if (this.audioRecord != null) {
                    int read = this.audioRecord.read(this.tempBufRec, 0, (i << 1) * 1);
                    if (read < 0) {
                        if (read == -3) {
                            throw new RuntimeException("captureThread(): AudioRecord.ERROR_INVALID_OPERATION");
                        }
                        if (read == -2) {
                            throw new RuntimeException("captureThread(): AudioRecord.ERROR_BAD_VALUE");
                        }
                        throw new RuntimeException("captureThread(): AudioRecord.ERROR or default");
                    }
                    this.recBuffer.rewind();
                    this.recBuffer.put(this.tempBufRec);
                    int i2 = (read >> 1) / 1;
                    this.captureLock.unlock();
                    getAudioBus().writeCaptureData(this.recBuffer, i2);
                    this.estimatedCaptureDelay = (i2 * 1000) / this.captureSamplingRate;
                }
            } catch (Exception e2) {
                OtLog.LogToken logToken2 = this.log;
                logToken2.e("Audio device capture error: " + e2.getMessage(), new Object[0]);
                return;
            } finally {
                this.captureLock.unlock();
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void b() {
        int i = this.samplesPerBuffer;
        try {
            Process.setThreadPriority(-19);
        } catch (Exception e) {
            this.log.e("android.os.Process.setThreadPriority(): " + e.getMessage(), new Object[0]);
        }
        while (!this.shutdownRenderThread) {
            this.rendererLock.lock();
            try {
                if (this.isRendering) {
                    this.rendererLock.unlock();
                    this.playBuffer.clear();
                    int readRenderData = getAudioBus().readRenderData(this.playBuffer, i);
                    this.rendererLock.lock();
                    if (this.audioTrack != null && this.isRendering) {
                        int i2 = (readRenderData << 1) * 1;
                        this.playBuffer.get(this.tempBufPlay, 0, i2);
                        int write = this.audioTrack.write(this.tempBufPlay, 0, i2);
                        if (write <= 0) {
                            if (write == -3) {
                                throw new RuntimeException("renderThread(): AudioTrack.ERROR_INVALID_OPERATION");
                            }
                            if (write == -2) {
                                throw new RuntimeException("renderThread(): AudioTrack.ERROR_BAD_VALUE");
                            }
                            throw new RuntimeException("renderThread(): AudioTrack.ERROR or default");
                        }
                        this.bufferedPlaySamples += (write >> 1) / 1;
                        int playbackHeadPosition = this.audioTrack.getPlaybackHeadPosition();
                        if (playbackHeadPosition < this.playPosition) {
                            this.playPosition = 0;
                        }
                        int i3 = this.bufferedPlaySamples - (playbackHeadPosition - this.playPosition);
                        this.bufferedPlaySamples = i3;
                        this.playPosition = playbackHeadPosition;
                        this.estimatedRenderDelay = (i3 * 1000) / this.outputSamplingRate;
                    }
                } else {
                    this.renderEvent.await();
                }
            } catch (Exception e2) {
                this.log.e("Audio device capture error: " + e2.getMessage(), new Object[0]);
                return;
            } finally {
                this.rendererLock.unlock();
            }
        }
    }

    private void destroyAudioTrack() {
        this.rendererLock.lock();
        this.audioTrack.release();
        this.audioTrack = null;
        this.shutdownRenderThread = true;
        this.renderEvent.signal();
        this.rendererLock.unlock();
    }

    private void disableBluetoothEvents() {
        BluetoothAdapter bluetoothAdapter;
        this.log.d("disableBluetoothEvents() called", new Object[0]);
        BluetoothProfile bluetoothProfile = this.bluetoothProfile;
        if (bluetoothProfile != null && (bluetoothAdapter = this.bluetoothAdapter) != null) {
            bluetoothAdapter.closeProfileProxy(1, bluetoothProfile);
        }
        unregisterBtReceiver();
        Intent intent = new Intent("android.bluetooth.headset.profile.action.CONNECTION_STATE_CHANGED");
        intent.putExtra("android.bluetooth.profile.extra.STATE", 0);
        this.bluetoothBroadcastReceiver.onReceive(this.context, intent);
    }

    private void enableBluetoothEvents() {
        this.log.d("enableBluetoothEvents() called", new Object[0]);
        if (this.audioManager.isBluetoothScoAvailableOffCall()) {
            registerBtReceiver();
            startBluetoothDevice();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void forceInvokeConnectBluetooth() {
        this.log.d("forceConnectBluetooth() called", new Object[0]);
        synchronized (this.bluetoothLock) {
            this.bluetoothState = BaseAudioDevice.BluetoothState.Disconnected;
            if (this.bluetoothAdapter != null) {
                this.bluetoothAdapter.getProfileProxy(this.context, this.bluetoothProfileServiceListener, 1);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public OutputType getOutputType() {
        return this.audioOutputType;
    }

    private boolean isWiredHeadSetConnected() {
        AudioDeviceInfo[] devices;
        if (Build.VERSION.SDK_INT < 23) {
            return this.audioManager.isWiredHeadsetOn();
        }
        for (AudioDeviceInfo audioDeviceInfo : this.audioManager.getDevices(2)) {
            if (audioDeviceInfo.getType() == 4 || audioDeviceInfo.getType() == 3 || audioDeviceInfo.getType() == 22) {
                return true;
            }
        }
        return false;
    }

    private void registerBtReceiver() {
        OtLog.LogToken logToken = this.log;
        logToken.d("registerBtReceiver() called .. isBluetoothHeadSetReceiverRegistered = " + this.isBluetoothHeadSetReceiverRegistered, new Object[0]);
        if (this.isBluetoothHeadSetReceiverRegistered) {
            return;
        }
        IntentFilter intentFilter = new IntentFilter("android.bluetooth.headset.profile.action.CONNECTION_STATE_CHANGED");
        intentFilter.addAction("android.media.ACTION_SCO_AUDIO_STATE_UPDATED");
        this.context.registerReceiver(this.bluetoothBroadcastReceiver, intentFilter);
        this.context.registerReceiver(this.bluetoothHeadsetReceiver, new IntentFilter("android.bluetooth.headset.profile.action.AUDIO_STATE_CHANGED"));
        this.isBluetoothHeadSetReceiverRegistered = true;
    }

    private void registerHeadsetReceiver() {
        OtLog.LogToken logToken = this.log;
        logToken.d("registerHeadsetReceiver() called ... isHeadsetReceiverRegistered = " + this.isHeadsetReceiverRegistered, new Object[0]);
        if (this.isHeadsetReceiverRegistered) {
            return;
        }
        this.context.registerReceiver(this.headsetBroadcastReceiver, new IntentFilter("android.intent.action.HEADSET_PLUG"));
        this.isHeadsetReceiverRegistered = true;
    }

    private void registerPhoneStateListener() {
        TelephonyManager telephonyManager;
        this.log.d("registerPhoneStateListener() called", new Object[0]);
        if (this.isPhoneStateListenerRegistered || (telephonyManager = this.telephonyManager) == null) {
            return;
        }
        telephonyManager.listen(this.phoneStateListener, 32);
        this.isPhoneStateListenerRegistered = true;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void restoreAudioAfterBluetoothDisconnect() {
        if (isWiredHeadSetConnected()) {
            setOutputType(OutputType.HEAD_PHONES);
        } else {
            OutputType lastOutputType = this.audioState.getLastOutputType();
            OutputType outputType = OutputType.SPEAKER_PHONE;
            if (lastOutputType == outputType) {
                setOutputType(outputType);
                super.setOutputMode(BaseAudioDevice.OutputMode.SpeakerPhone);
                this.audioManager.setSpeakerphoneOn(true);
                return;
            }
            OutputType lastOutputType2 = this.audioState.getLastOutputType();
            OutputType outputType2 = OutputType.EAR_PIECE;
            if (lastOutputType2 != outputType2) {
                return;
            }
            setOutputType(outputType2);
            super.setOutputMode(BaseAudioDevice.OutputMode.Handset);
        }
        this.audioManager.setSpeakerphoneOn(false);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void setOutputType(OutputType outputType) {
        OtLog.LogToken logToken = this.log;
        logToken.d("setOutputType() called .. OutputType = " + outputType, new Object[0]);
        this.audioOutputType = outputType;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void startBluetoothDevice() {
        this.log.d("startBluetoothDevice() enter <-", new Object[0]);
        this.audioManager.setBluetoothScoOn(true);
        try {
            this.audioManager.startBluetoothSco();
        } catch (NullPointerException e) {
            OtLog.LogToken logToken = this.log;
            logToken.e("startBluetoothSco(): " + e.getMessage(), new Object[0]);
        }
        this.log.d("startBluetoothDevice() exit ->", new Object[0]);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void startRendererAndCapturer() {
        this.log.d("startRendererAndCapturer() enter <-", new Object[0]);
        if (this.wasRendering) {
            startRenderer();
        }
        if (this.wasCapturing) {
            startCapturer();
        }
        this.log.d("startRendererAndCapturer() exit ->", new Object[0]);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void stopBluetoothDevice() {
        this.log.d("stopBluetoothDevice() enter <-", new Object[0]);
        this.audioManager.setBluetoothScoOn(false);
        try {
            this.audioManager.stopBluetoothSco();
        } catch (NullPointerException e) {
            OtLog.LogToken logToken = this.log;
            logToken.e("stopBluetoothSco(): " + e.getMessage(), new Object[0]);
        }
        this.log.d("stopBluetoothDevice() exit ->", new Object[0]);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void stopRendererAndCapturer() {
        this.log.d("stopRendererAndCapturer() enter <-", new Object[0]);
        if (this.isRendering) {
            stopRenderer();
            this.wasRendering = true;
        }
        if (this.isCapturing) {
            stopCapturer();
            this.wasCapturing = true;
        }
        this.log.d("stopRendererAndCapturer() exit ->", new Object[0]);
    }

    private void unRegisterPhoneStateListener() {
        TelephonyManager telephonyManager;
        this.log.d("unRegisterPhoneStateListener() called", new Object[0]);
        if (this.isPhoneStateListenerRegistered && (telephonyManager = this.telephonyManager) != null) {
            telephonyManager.listen(this.phoneStateListener, 0);
            this.isPhoneStateListenerRegistered = false;
        }
    }

    private void unregisterBtReceiver() {
        OtLog.LogToken logToken = this.log;
        logToken.d("unregisterBtReceiver() called .. bluetoothHeadSetReceiverRegistered = " + this.isBluetoothHeadSetReceiverRegistered, new Object[0]);
        if (this.isBluetoothHeadSetReceiverRegistered) {
            this.context.unregisterReceiver(this.bluetoothBroadcastReceiver);
            this.context.unregisterReceiver(this.bluetoothHeadsetReceiver);
            this.isBluetoothHeadSetReceiverRegistered = false;
        }
    }

    private void unregisterHeadsetReceiver() {
        OtLog.LogToken logToken = this.log;
        logToken.d("unregisterHeadsetReceiver() called .. isHeadsetReceiverRegistered = " + this.isHeadsetReceiverRegistered, new Object[0]);
        if (this.isHeadsetReceiverRegistered) {
            this.context.unregisterReceiver(this.headsetBroadcastReceiver);
            this.isHeadsetReceiverRegistered = false;
        }
    }

    @Override // com.opentok.android.BaseAudioDevice
    public boolean destroyCapturer() {
        this.log.d("destroyCapturer() enter <-", new Object[0]);
        this.captureLock.lock();
        AcousticEchoCanceler acousticEchoCanceler = this.echoCanceler;
        if (acousticEchoCanceler != null) {
            acousticEchoCanceler.release();
            this.echoCanceler = null;
        }
        NoiseSuppressor noiseSuppressor = this.noiseSuppressor;
        if (noiseSuppressor != null) {
            noiseSuppressor.release();
            this.noiseSuppressor = null;
        }
        this.audioRecord.release();
        this.audioRecord = null;
        this.shutdownCaptureThread = true;
        this.captureEvent.signal();
        this.captureLock.unlock();
        unRegisterPhoneStateListener();
        this.wasCapturing = false;
        this.log.d("destroyCapturer() exit ->", new Object[0]);
        return true;
    }

    @Override // com.opentok.android.BaseAudioDevice
    public boolean destroyRenderer() {
        this.log.d("destroyRenderer() enter <-", new Object[0]);
        destroyAudioTrack();
        disableBluetoothEvents();
        unregisterHeadsetReceiver();
        this.audioManager.setSpeakerphoneOn(false);
        this.audioManager.abandonAudioFocus(this.audioFocusChangeListener);
        unRegisterPhoneStateListener();
        this.wasRendering = false;
        this.log.d("destroyRenderer() exit ->", new Object[0]);
        return true;
    }

    @Override // com.opentok.android.BaseAudioDevice
    public BaseAudioDevice.BluetoothState getBluetoothState() {
        return this.bluetoothState;
    }

    @Override // com.opentok.android.BaseAudioDevice
    public BaseAudioDevice.AudioSettings getCaptureSettings() {
        return this.captureSettings;
    }

    @Override // com.opentok.android.BaseAudioDevice
    public int getEstimatedCaptureDelay() {
        return this.estimatedCaptureDelay;
    }

    @Override // com.opentok.android.BaseAudioDevice
    public int getEstimatedRenderDelay() {
        return this.estimatedRenderDelay;
    }

    @Override // com.opentok.android.BaseAudioDevice
    public BaseAudioDevice.AudioSettings getRenderSettings() {
        return this.rendererSettings;
    }

    @Override // com.opentok.android.BaseAudioDevice
    public boolean initCapturer() {
        this.log.d("initCapturer() enter <-", new Object[0]);
        int minBufferSize = AudioRecord.getMinBufferSize(this.captureSettings.getSampleRate(), 16, 2);
        int i = minBufferSize * 2;
        NoiseSuppressor noiseSuppressor = this.noiseSuppressor;
        if (noiseSuppressor != null) {
            noiseSuppressor.release();
            this.noiseSuppressor = null;
        }
        AcousticEchoCanceler acousticEchoCanceler = this.echoCanceler;
        if (acousticEchoCanceler != null) {
            acousticEchoCanceler.release();
            this.echoCanceler = null;
        }
        AudioRecord audioRecord = this.audioRecord;
        if (audioRecord != null) {
            audioRecord.release();
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
                this.log.e(format, new Object[0]);
                throw new RuntimeException(format);
            }
            registerPhoneStateListener();
            this.shutdownCaptureThread = false;
            new Thread(this.captureThread).start();
            this.log.d("initCapturer() exit ->", new Object[0]);
            return true;
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    @Override // com.opentok.android.BaseAudioDevice
    public boolean initRenderer() {
        this.log.d("initRenderer() enter <-", new Object[0]);
        if (this.audioManager.requestAudioFocus(this.audioFocusChangeListener, 0, 1) != 1) {
            this.log.d("initRenderer(): !AudioManager.AUDIOFOCUS_REQUEST_GRANTED", new Object[0]);
            return false;
        }
        this.log.d("initRenderer(): AudioManager.AUDIOFOCUS_REQUEST_GRANTED", new Object[0]);
        this.bluetoothState = BaseAudioDevice.BluetoothState.Disconnected;
        enableBluetoothEvents();
        int minBufferSize = AudioTrack.getMinBufferSize(this.rendererSettings.getSampleRate(), 4, 2);
        AudioTrack audioTrack = this.audioTrack;
        if (audioTrack != null) {
            audioTrack.release();
            this.audioTrack = null;
        }
        try {
            int sampleRate = this.rendererSettings.getSampleRate();
            if (minBufferSize < 6000) {
                minBufferSize *= 2;
            }
            AudioTrack audioTrack2 = new AudioTrack(0, sampleRate, 4, 2, minBufferSize, 1);
            this.audioTrack = audioTrack2;
            if (audioTrack2.getState() != 1) {
                throw new RuntimeException("Audio renderer not initialized " + this.rendererSettings.getSampleRate());
            }
            this.bufferedPlaySamples = 0;
            registerPhoneStateListener();
            this.shutdownRenderThread = false;
            new Thread(this.renderThread).start();
            this.log.d("initRenderer() exit ->", new Object[0]);
            return true;
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    @Override // com.opentok.android.BaseAudioDevice
    public synchronized void onPause() {
        this.log.d("onPause() enter <-", new Object[0]);
        this.audioState.setLastOutputType(getOutputType());
        unregisterBtReceiver();
        unregisterHeadsetReceiver();
        this.isPaused = true;
        this.log.d("onPause() exit ->", new Object[0]);
    }

    @Override // com.opentok.android.BaseAudioDevice
    public synchronized void onResume() {
        this.log.d("onResume() enter <-", new Object[0]);
        if (!this.isPaused) {
            this.log.d("onResume() returns as not in paused state", new Object[0]);
            return;
        }
        if (this.bluetoothState == BaseAudioDevice.BluetoothState.Disconnected && this.isRendering && this.audioState.getLastOutputType() == OutputType.SPEAKER_PHONE && !isWiredHeadSetConnected()) {
            this.log.d("onResume() - Set Speaker Phone ON True", new Object[0]);
            this.audioManager.setSpeakerphoneOn(true);
        }
        registerBtReceiver();
        registerHeadsetReceiver();
        forceInvokeConnectBluetooth();
        startBluetoothDevice();
        this.isPaused = false;
        this.log.d("onResume() exit ->", new Object[0]);
    }

    @Override // com.opentok.android.BaseAudioDevice
    public boolean setOutputMode(BaseAudioDevice.OutputMode outputMode) {
        OtLog.LogToken logToken = this.log;
        logToken.d("Audio Output mode set to --> " + outputMode, new Object[0]);
        super.setOutputMode(outputMode);
        if (BaseAudioDevice.OutputMode.SpeakerPhone == outputMode) {
            this.audioState.setLastOutputType(getOutputType());
            setOutputType(OutputType.SPEAKER_PHONE);
            this.audioManager.setSpeakerphoneOn(true);
        } else if (this.audioState.getLastOutputType() == OutputType.BLUETOOTH || this.bluetoothState == BaseAudioDevice.BluetoothState.Connected) {
            startBluetoothDevice();
            return true;
        } else {
            this.audioState.setLastOutputType(getOutputType());
            this.audioManager.setSpeakerphoneOn(false);
            setOutputType(OutputType.EAR_PIECE);
        }
        stopBluetoothDevice();
        return true;
    }

    @Override // com.opentok.android.BaseAudioDevice
    public boolean startCapturer() {
        this.log.d("startCapturer() enter <-", new Object[0]);
        AudioRecord audioRecord = this.audioRecord;
        if (audioRecord != null) {
            try {
                audioRecord.startRecording();
                this.captureLock.lock();
                this.isCapturing = true;
                this.captureEvent.signal();
                this.captureLock.unlock();
                this.audioManagerMode.acquireMode(this.audioManager);
                this.log.d("startCapturer() exit ->", new Object[0]);
                return true;
            } catch (IllegalStateException e) {
                throw new RuntimeException(e.getMessage());
            }
        }
        throw new IllegalStateException("startCapturer(): startRecording() called on an uninitialized AudioRecord.");
    }

    @Override // com.opentok.android.BaseAudioDevice
    public boolean startRenderer() {
        this.log.d("startRenderer() enter <-", new Object[0]);
        synchronized (this.bluetoothLock) {
            if (BaseAudioDevice.BluetoothState.Connected != this.bluetoothState) {
                if (isWiredHeadSetConnected()) {
                    this.log.d("startRenderer(): Turn off Speaker phone", new Object[0]);
                    this.audioManager.setSpeakerphoneOn(false);
                } else {
                    this.log.d("startRenderer(): Turn on Speaker phone", new Object[0]);
                    if (getOutputType() == OutputType.SPEAKER_PHONE) {
                        this.audioManager.setSpeakerphoneOn(true);
                    }
                }
            }
        }
        AudioTrack audioTrack = this.audioTrack;
        if (audioTrack != null) {
            try {
                audioTrack.play();
                this.rendererLock.lock();
                this.isRendering = true;
                this.renderEvent.signal();
                this.rendererLock.unlock();
                this.audioManagerMode.acquireMode(this.audioManager);
                registerBtReceiver();
                registerHeadsetReceiver();
                this.log.d("startRenderer() exit ->", new Object[0]);
                return true;
            } catch (IllegalStateException e) {
                throw new RuntimeException(e.getMessage());
            }
        }
        throw new IllegalStateException("startRenderer(): play() called on uninitialized AudioTrack.");
    }

    @Override // com.opentok.android.BaseAudioDevice
    public boolean stopCapturer() {
        this.log.d("stopCapturer() enter <-", new Object[0]);
        if (this.audioRecord != null) {
            this.captureLock.lock();
            try {
                try {
                    if (this.audioRecord.getRecordingState() == 3) {
                        this.audioRecord.stop();
                    }
                    this.isCapturing = false;
                    this.captureLock.unlock();
                    this.audioManagerMode.releaseMode(this.audioManager);
                    stopBluetoothDevice();
                    this.log.d("stopCapturer() exit ->", new Object[0]);
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
        throw new IllegalStateException("stopCapturer(): stop() called on an uninitialized AudioRecord.");
    }

    @Override // com.opentok.android.BaseAudioDevice
    public boolean stopRenderer() {
        this.log.d("stopRenderer() enter <-", new Object[0]);
        if (this.audioTrack != null) {
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
                    stopBluetoothDevice();
                    this.log.d("stopRenderer() exit ->", new Object[0]);
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
        throw new IllegalStateException("stopRenderer(): stop() called on uninitialized AudioTrack.");
    }
}
