package com.opentok.android;

import com.opentok.otc.otc_audio_device_settings;
import java.nio.Buffer;
import java.nio.ByteBuffer;
import java.nio.ShortBuffer;

/* loaded from: classes.dex */
public abstract class BaseAudioDevice {
    private AudioBus audioBus;
    private OutputMode outputMode = OutputMode.SpeakerPhone;

    /* loaded from: classes.dex */
    public static class AudioBus {
        private BaseAudioDevice device;

        /* JADX INFO: Access modifiers changed from: package-private */
        public AudioBus(BaseAudioDevice baseAudioDevice) {
            this.device = baseAudioDevice;
        }

        private static byte[] adjustArray(byte[] bArr, int i) {
            return (bArr == null || bArr.length != i) ? new byte[i] : bArr;
        }

        private static native void array2BufferNative(Buffer buffer, byte[] bArr);

        private static native void buffer2ArrayNative(Buffer buffer, byte[] bArr);

        private native int readDataNative(Buffer buffer, int i);

        private int readToBuffer(Buffer buffer, int i) {
            if (buffer.isDirect()) {
                try {
                    return readDataNative(buffer, i);
                } catch (Exception e) {
                    e.printStackTrace();
                    return 0;
                }
            }
            throw new RuntimeException("ByteBuffer should be allocated using allocateDirect method");
        }

        private native void writeDataNative(Buffer buffer, int i);

        private void writeFromBuffer(Buffer buffer, int i) {
            try {
                if (!buffer.isDirect()) {
                    throw new RuntimeException("ByteBuffer should be allocated using allocateDirect method");
                }
                writeDataNative(buffer, i);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        public int readRenderData(ByteBuffer byteBuffer, int i) {
            return readToBuffer(byteBuffer, i);
        }

        public int readRenderData(ShortBuffer shortBuffer, int i) {
            return readToBuffer(shortBuffer, i);
        }

        public void writeCaptureData(ByteBuffer byteBuffer, int i) {
            writeFromBuffer(byteBuffer, i);
        }

        public void writeCaptureData(ShortBuffer shortBuffer, int i) {
            writeFromBuffer(shortBuffer, i);
        }
    }

    /* loaded from: classes.dex */
    public static class AudioSettings {
        int numChannels;
        int sampleRate;

        public AudioSettings(int i, int i2) {
            this.sampleRate = i;
            this.numChannels = i2;
        }

        public int getNumChannels() {
            return this.numChannels;
        }

        public int getSampleRate() {
            return this.sampleRate;
        }

        public void setNumChannels(int i) {
            this.numChannels = i;
        }

        public void setSampleRate(int i) {
            this.sampleRate = i;
        }
    }

    /* loaded from: classes.dex */
    public enum BluetoothState {
        Connected,
        Disconnected
    }

    /* loaded from: classes.dex */
    public enum OutputMode {
        SpeakerPhone,
        Handset
    }

    /* loaded from: classes.dex */
    static class otc_audio_settings extends otc_audio_device_settings {
        otc_audio_settings(long j) {
            super(j, false);
        }

        void updateWithSettings(AudioSettings audioSettings) {
            setNumber_of_channels(audioSettings.getNumChannels());
            setSampling_rate(audioSettings.getSampleRate());
        }
    }

    public abstract boolean destroyCapturer();

    public abstract boolean destroyRenderer();

    protected void finalize() {
        super.finalize();
    }

    public AudioBus getAudioBus() {
        return this.audioBus;
    }

    public BluetoothState getBluetoothState() {
        throw new UnsupportedOperationException("Audio Device implementation does not support getting bluetooth state");
    }

    public abstract AudioSettings getCaptureSettings();

    boolean getCaptureSettingsFromNative(long j) {
        new otc_audio_settings(j).updateWithSettings(getCaptureSettings());
        return true;
    }

    public abstract int getEstimatedCaptureDelay();

    public abstract int getEstimatedRenderDelay();

    public OutputMode getOutputMode() {
        return this.outputMode;
    }

    public abstract AudioSettings getRenderSettings();

    boolean getRenderSettingsFromNative(long j) {
        new otc_audio_settings(j).updateWithSettings(getRenderSettings());
        return true;
    }

    public abstract boolean initCapturer();

    public abstract boolean initRenderer();

    public abstract void onPause();

    public abstract void onResume();

    /* JADX INFO: Access modifiers changed from: package-private */
    public void setAudioBus(AudioBus audioBus) {
        this.audioBus = audioBus;
    }

    public boolean setOutputMode(OutputMode outputMode) {
        this.outputMode = outputMode;
        return true;
    }

    public abstract boolean startCapturer();

    public abstract boolean startRenderer();

    public abstract boolean stopCapturer();

    public abstract boolean stopRenderer();
}
