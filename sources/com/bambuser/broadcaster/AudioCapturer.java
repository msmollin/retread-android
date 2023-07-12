package com.bambuser.broadcaster;

import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothProfile;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.AudioRecord;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;
import android.telephony.TelephonyManager;
import android.util.Log;
import com.facebook.internal.ServerProtocol;

/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: classes.dex */
public final class AudioCapturer {
    static final String INIT_BUFFER_SIZE = "buffer_size";
    static final String INIT_CAMERA_ID = "camera_id";
    static final String INIT_ENFORCE_SAMPLE_RATE = "enforce_sample_rate";
    static final String INIT_SAMPLE_RATE = "sample_rate";
    private static final String LOGTAG = "AudioCapturer";
    static final int SAMPLE_RATE_16K = 16000;
    static final int SAMPLE_RATE_8K = 8000;
    private final AudioHandler mAudioHandler;
    private final Capturer mCapturer;
    private final Context mContext;
    private volatile boolean mIsBtAdapterOn;
    private int mOutputRawBufferSize;
    private final BroadcastReceiver mHeadsetBroadcastReceiver = new BroadcastReceiver() { // from class: com.bambuser.broadcaster.AudioCapturer.2
        @Override // android.content.BroadcastReceiver
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals("android.intent.action.HEADSET_PLUG")) {
                boolean z = false;
                int intExtra = intent.getIntExtra(ServerProtocol.DIALOG_PARAM_STATE, 0);
                int intExtra2 = intent.getIntExtra("microphone", 0);
                Log.v(AudioCapturer.LOGTAG, "headset state: " + intExtra + ", microphone: " + intExtra2);
                AudioCapturer audioCapturer = AudioCapturer.this;
                if (intExtra > 0 && intExtra2 > 0) {
                    z = true;
                }
                audioCapturer.mHasWiredMicrophone = z;
                AudioCapturer.this.restartRecording();
            }
        }
    };
    private final BluetoothProfile.ServiceListener mBtServiceListener = new BluetoothProfile.ServiceListener() { // from class: com.bambuser.broadcaster.AudioCapturer.3
        @Override // android.bluetooth.BluetoothProfile.ServiceListener
        public void onServiceConnected(int i, BluetoothProfile bluetoothProfile) {
            if (AudioCapturer.this.mBtProfile != null) {
                Log.w(AudioCapturer.LOGTAG, "already have a profile proxy reference, closing it before keeping new reference, to avoid leak");
                BluetoothCompat.closeBtProfile(1, AudioCapturer.this.mBtProfile);
            }
            AudioCapturer.this.mBtProfile = bluetoothProfile;
            BluetoothDevice firstConnectedDevice = BluetoothCompat.getFirstConnectedDevice(AudioCapturer.this.mBtProfile);
            AudioCapturer.this.mIsBtHeadsetConnected = firstConnectedDevice != null;
            Log.v(AudioCapturer.LOGTAG, "onServiceConnected profile: " + i + ", proxy: " + bluetoothProfile + ", device: " + firstConnectedDevice + ", mBtScoState: " + AudioCapturer.this.mBtScoState);
            AudioCapturer.this.startBtScoDelayed();
        }

        @Override // android.bluetooth.BluetoothProfile.ServiceListener
        public void onServiceDisconnected(int i) {
            Log.v(AudioCapturer.LOGTAG, "onServiceDisconnected profile: " + i);
            AudioCapturer.this.mBtProfile = null;
            AudioCapturer.this.restartRecording();
        }
    };
    private final BroadcastReceiver mBtBroadcastReceiver = new BroadcastReceiver() { // from class: com.bambuser.broadcaster.AudioCapturer.4
        @Override // android.content.BroadcastReceiver
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            boolean isInitialStickyBroadcast = isInitialStickyBroadcast();
            StringBuilder sb = new StringBuilder();
            sb.append(action);
            sb.append(isInitialStickyBroadcast ? ", initial sticky" : "");
            Log.v(AudioCapturer.LOGTAG, sb.toString());
            if (action.equals("android.media.ACTION_SCO_AUDIO_STATE_UPDATED")) {
                int intExtra = intent.getIntExtra("android.media.extra.SCO_AUDIO_STATE", -1);
                int intExtra2 = intent.getIntExtra("android.media.extra.SCO_AUDIO_PREVIOUS_STATE", -1);
                AudioCapturer.this.mBtScoState = intExtra;
                if (isInitialStickyBroadcast && AudioCapturer.this.mBtScoState == 1) {
                    BluetoothCompat.startBtSco(AudioCapturer.this.mContext);
                }
                if (isInitialStickyBroadcast) {
                    return;
                }
                Log.v(AudioCapturer.LOGTAG, "bluetooth sco state changed from " + intExtra2 + " to " + intExtra);
                if (intExtra != 0) {
                    if (intExtra == 1) {
                        AudioCapturer.this.restartRecording();
                        return;
                    }
                    return;
                }
                BluetoothCompat.stopBtSco(AudioCapturer.this.mContext);
                if (intExtra2 != 1) {
                    AudioCapturer.this.startBtScoDelayed();
                }
                if (intExtra2 != 2) {
                    AudioCapturer.this.restartRecording();
                    return;
                }
                return;
            }
            if (action.equals("android.bluetooth.adapter.action.STATE_CHANGED")) {
                int intExtra3 = intent.getIntExtra("android.bluetooth.adapter.extra.STATE", 10);
                int intExtra4 = intent.getIntExtra("android.bluetooth.adapter.extra.PREVIOUS_STATE", 10);
                Log.v(AudioCapturer.LOGTAG, "bluetooth adapter state changed from " + intExtra4 + " to " + intExtra3);
                AudioCapturer.this.mIsBtAdapterOn = intExtra3 == 12;
                if (intExtra3 == 12) {
                    BluetoothCompat.getBtProfile(AudioCapturer.this.mContext, AudioCapturer.this.mBtServiceListener, 1);
                    return;
                }
                BluetoothCompat.stopBtSco(AudioCapturer.this.mContext);
                BluetoothCompat.closeBtProfile(1, AudioCapturer.this.mBtProfile);
                AudioCapturer.this.mBtProfile = null;
                AudioCapturer.this.restartRecording();
            } else if (action.equals("android.bluetooth.headset.profile.action.CONNECTION_STATE_CHANGED")) {
                int intExtra5 = intent.getIntExtra("android.bluetooth.profile.extra.STATE", 0);
                int intExtra6 = intent.getIntExtra("android.bluetooth.profile.extra.PREVIOUS_STATE", 0);
                Log.v(AudioCapturer.LOGTAG, "headset profile state changed from " + intExtra6 + " to " + intExtra5 + ", BtScoState: " + AudioCapturer.this.mBtScoState);
                AudioCapturer.this.mIsBtHeadsetConnected = BluetoothCompat.getFirstConnectedDevice(AudioCapturer.this.mBtProfile) != null;
                AudioCapturer.this.startBtScoDelayed();
            }
        }
    };
    private final BroadcastReceiver mTelephonyBroadcastReceiver = new BroadcastReceiver() { // from class: com.bambuser.broadcaster.AudioCapturer.5
        @Override // android.content.BroadcastReceiver
        public void onReceive(Context context, Intent intent) {
            int callState = ((TelephonyManager) AudioCapturer.this.mContext.getSystemService("phone")).getCallState();
            Log.v(AudioCapturer.LOGTAG, "call state: " + callState);
            if (callState == 1 || callState == 2) {
                AudioCapturer.this.stopAudioCapture();
            }
            if (callState == 0) {
                AudioCapturer.this.mCapturer.restartAudio(null);
            }
        }
    };
    private final ByteArrayPool mAudioByteArrayPool = new ByteArrayPool();
    private AudioRecord mAudioRecorder = null;
    private boolean mEnforceSampleRate = false;
    private int mSampleRate = 0;
    private int mOutputSampleRate = 0;
    private Resampler mResampler = null;
    private int mRawBufferSize = 2560;
    private AudioWrapper mOutputWrapper = null;
    private AudioWrapper mWrapper = null;
    private volatile BluetoothProfile mBtProfile = null;
    private volatile boolean mIsBtHeadsetConnected = false;
    private volatile int mBtScoState = 0;
    private volatile boolean mHasWiredMicrophone = false;

    /* loaded from: classes.dex */
    interface InitCallback {
        void onAudioRecorderReady(int i);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public AudioCapturer(Capturer capturer, Context context, boolean z) {
        this.mIsBtAdapterOn = false;
        this.mCapturer = capturer;
        this.mContext = context;
        this.mContext.registerReceiver(this.mHeadsetBroadcastReceiver, new IntentFilter("android.intent.action.HEADSET_PLUG"));
        if (z) {
            this.mIsBtAdapterOn = BluetoothCompat.getAdapterState() == 12;
            this.mContext.registerReceiver(this.mBtBroadcastReceiver, new IntentFilter("android.media.ACTION_SCO_AUDIO_STATE_UPDATED"));
            this.mContext.registerReceiver(this.mBtBroadcastReceiver, new IntentFilter("android.bluetooth.adapter.action.STATE_CHANGED"));
            this.mContext.registerReceiver(this.mBtBroadcastReceiver, new IntentFilter("android.bluetooth.headset.profile.action.CONNECTION_STATE_CHANGED"));
            if (this.mIsBtAdapterOn) {
                BluetoothCompat.getBtProfile(context, this.mBtServiceListener, 1);
            }
        }
        if (Build.VERSION.SDK_INT <= 22) {
            this.mContext.registerReceiver(this.mTelephonyBroadcastReceiver, new IntentFilter("android.intent.action.PHONE_STATE"));
        }
        HandlerThread handlerThread = new HandlerThread("AudioThread");
        handlerThread.start();
        handlerThread.setPriority(6);
        this.mAudioHandler = new AudioHandler(handlerThread.getLooper());
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void setupAudioCapture(Bundle bundle, InitCallback initCallback) {
        Message obtainMessage = this.mAudioHandler.obtainMessage(1, initCallback);
        obtainMessage.setData(bundle);
        obtainMessage.sendToTarget();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void startAudioCapture() {
        this.mAudioHandler.sendEmptyMessage(2);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void stopAudioCapture() {
        this.mAudioHandler.sendEmptyMessage(4);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void close() {
        if (Thread.currentThread() == this.mAudioHandler.getLooper().getThread()) {
            throw new RuntimeException("AudioCapturer.close() called on audio thread. can't wait for self!");
        }
        Object obj = new Object();
        synchronized (obj) {
            this.mAudioHandler.obtainMessage(5, obj).sendToTarget();
            while (true) {
                try {
                    obj.wait(500L);
                    break;
                } catch (InterruptedException unused) {
                }
            }
        }
        BluetoothCompat.stopBtSco(this.mContext);
        BluetoothCompat.closeBtProfile(1, this.mBtProfile);
        this.mBtProfile = null;
        if (Build.VERSION.SDK_INT <= 22) {
            this.mContext.unregisterReceiver(this.mTelephonyBroadcastReceiver);
        }
        try {
            this.mContext.unregisterReceiver(this.mBtBroadcastReceiver);
        } catch (Exception unused2) {
        }
        try {
            this.mContext.unregisterReceiver(this.mHeadsetBroadcastReceiver);
        } catch (Exception unused3) {
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void reuseWrapper(AudioWrapper audioWrapper) {
        if (audioWrapper != null) {
            this.mAudioByteArrayPool.add(audioWrapper.mBuffer);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void restartRecording() {
        if (this.mCapturer.isCapturing() && this.mAudioRecorder != null && this.mAudioRecorder.getRecordingState() == 3) {
            stopAudioCapture();
            this.mCapturer.restartAudio(null);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void startBtScoDelayed() {
        if (this.mIsBtAdapterOn && this.mIsBtHeadsetConnected && BluetoothCompat.isBtScoAvailable(this.mContext) && this.mBtScoState == 0) {
            this.mAudioHandler.postDelayed(new Runnable() { // from class: com.bambuser.broadcaster.AudioCapturer.1
                @Override // java.lang.Runnable
                public void run() {
                    BluetoothCompat.startBtSco(AudioCapturer.this.mContext);
                }
            }, 500L);
        } else if (this.mBtScoState == 1) {
            restartRecording();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes.dex */
    public class AudioHandler extends Handler {
        static final int CLOSE = 5;
        static final int INIT = 1;
        static final int READ = 3;
        static final int START = 2;
        static final int STOP = 4;

        AudioHandler(Looper looper) {
            super(looper);
        }

        @Override // android.os.Handler
        public void handleMessage(Message message) {
            switch (message.what) {
                case 1:
                    Bundle data = message.getData();
                    AudioCapturer.this.mRawBufferSize = data.getInt(AudioCapturer.INIT_BUFFER_SIZE);
                    AudioCapturer.this.mEnforceSampleRate = data.getBoolean(AudioCapturer.INIT_ENFORCE_SAMPLE_RATE, false);
                    boolean initRecorder = AudioCapturer.this.initRecorder(data.getInt(AudioCapturer.INIT_SAMPLE_RATE), data.getString(AudioCapturer.INIT_CAMERA_ID));
                    if (!initRecorder) {
                        initRecorder = AudioCapturer.this.initRecorder(AudioCapturer.SAMPLE_RATE_8K, data.getString(AudioCapturer.INIT_CAMERA_ID));
                    }
                    if (!initRecorder || message.obj == null) {
                        return;
                    }
                    ((InitCallback) message.obj).onAudioRecorderReady(AudioCapturer.this.mOutputSampleRate);
                    return;
                case 2:
                    if (AudioCapturer.this.mAudioRecorder == null || AudioCapturer.this.mAudioRecorder.getState() != 1) {
                        return;
                    }
                    try {
                        AudioCapturer.this.mAudioRecorder.startRecording();
                        sendEmptyMessage(3);
                        return;
                    } catch (Exception e) {
                        Log.w(AudioCapturer.LOGTAG, "Could not start recording: " + e);
                        sendEmptyMessage(4);
                        return;
                    }
                case 3:
                    if (AudioCapturer.this.read()) {
                        sendEmptyMessage(3);
                        return;
                    }
                    return;
                case 4:
                    AudioCapturer.this.stopRecorder();
                    return;
                case 5:
                    AudioCapturer.this.stopRecorder();
                    AudioCapturer.this.mAudioByteArrayPool.clear();
                    synchronized (message.obj) {
                        message.obj.notifyAll();
                    }
                    getLooper().quit();
                    return;
                default:
                    Log.d(AudioCapturer.LOGTAG, "AudioHandler got an unknown message");
                    return;
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean initRecorder(int i, String str) {
        stopRecorder();
        this.mOutputSampleRate = i;
        this.mOutputRawBufferSize = this.mRawBufferSize;
        boolean z = this.mIsBtAdapterOn && this.mBtProfile != null && this.mIsBtHeadsetConnected && this.mBtScoState == 1;
        CamInfo findCamInfo = DeviceInfoHandler.findCamInfo(this.mContext, str);
        int i2 = (z || this.mHasWiredMicrophone || !(findCamInfo != null && findCamInfo.isRear())) ? 1 : 5;
        if (z && i != SAMPLE_RATE_8K) {
            Log.i(LOGTAG, "sample rate " + i + " not supported with bluetooth headset, enforcing 8000");
            i = SAMPLE_RATE_8K;
        }
        int minBufferSize = AudioRecord.getMinBufferSize(i, 16, 2);
        if (minBufferSize <= 0) {
            Log.w(LOGTAG, "could not get minimum buffer size for audio recorder. got: " + minBufferSize);
            return false;
        }
        try {
            AudioRecord audioRecord = new AudioRecord(i2, i, 16, 2, Math.max(this.mRawBufferSize * 4, minBufferSize));
            if (audioRecord.getState() != 1) {
                Log.w(LOGTAG, "could not initialize AudioRecord");
                audioRecord.release();
                return false;
            }
            this.mSampleRate = audioRecord.getSampleRate();
            this.mAudioRecorder = audioRecord;
            if (!this.mEnforceSampleRate) {
                this.mOutputSampleRate = this.mSampleRate;
            }
            if (this.mSampleRate != this.mOutputSampleRate) {
                this.mRawBufferSize = (((((this.mOutputRawBufferSize / 2) * i) + this.mOutputSampleRate) - 1) / this.mOutputSampleRate) * 2;
                this.mResampler = new Resampler(i, this.mOutputSampleRate);
            }
            return true;
        } catch (IllegalArgumentException unused) {
            Log.w(LOGTAG, "could not initialize AudioRecord");
            return false;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean read() {
        if (this.mAudioRecorder == null) {
            return false;
        }
        if (this.mWrapper == null) {
            this.mWrapper = new AudioWrapper(this.mAudioByteArrayPool.getBuffer(this.mRawBufferSize));
        }
        int i = this.mRawBufferSize;
        if (this.mOutputWrapper != null) {
            i = ((((((this.mOutputRawBufferSize - this.mOutputWrapper.mIndex) / 2) * this.mSampleRate) + this.mOutputSampleRate) - 1) / this.mOutputSampleRate) * 2;
        }
        int read = this.mAudioRecorder.read(this.mWrapper.mBuffer, this.mWrapper.mIndex, i - this.mWrapper.mIndex);
        if (read > 0) {
            this.mWrapper.mIndex += read;
            if (this.mWrapper.mIndex == i) {
                this.mWrapper.mTimestamp = this.mCapturer.getCaptureDuration();
                if (this.mResampler == null) {
                    this.mCapturer.newAudio(this.mWrapper);
                    this.mWrapper = null;
                } else {
                    if (this.mOutputWrapper == null) {
                        this.mOutputWrapper = new AudioWrapper(this.mAudioByteArrayPool.getBuffer(this.mOutputRawBufferSize));
                    }
                    this.mOutputWrapper.mIndex += this.mResampler.resample(this.mWrapper.mBuffer, 0, this.mWrapper.mIndex, this.mOutputWrapper.mBuffer, this.mOutputWrapper.mIndex);
                    if (this.mOutputWrapper.mIndex == this.mOutputRawBufferSize) {
                        this.mOutputWrapper.mTimestamp = this.mWrapper.mTimestamp;
                        this.mCapturer.newAudio(this.mOutputWrapper);
                        this.mOutputWrapper = null;
                    }
                    int bytesConsumed = this.mResampler.getBytesConsumed();
                    if (bytesConsumed < this.mWrapper.mIndex) {
                        int i2 = this.mRawBufferSize - bytesConsumed;
                        System.arraycopy(this.mWrapper.mBuffer, bytesConsumed, this.mWrapper.mBuffer, 0, i2);
                        this.mWrapper.mIndex = i2;
                    } else {
                        this.mWrapper.mIndex = 0;
                    }
                }
            }
        } else if (read == 0) {
            Thread.yield();
        } else {
            Log.e(LOGTAG, "AudioRecord error in read(), code: " + read);
        }
        return read >= 0;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void stopRecorder() {
        this.mAudioHandler.removeMessages(3);
        if (this.mAudioRecorder != null) {
            try {
                this.mAudioRecorder.stop();
            } catch (IllegalStateException e) {
                Log.w(LOGTAG, "exception when stopping AudioRecord: " + e.getMessage());
            }
            this.mAudioRecorder.release();
        }
        this.mAudioRecorder = null;
        if (this.mResampler != null) {
            this.mResampler.close();
        }
        this.mResampler = null;
        this.mWrapper = null;
        this.mOutputWrapper = null;
        this.mSampleRate = 0;
    }
}
