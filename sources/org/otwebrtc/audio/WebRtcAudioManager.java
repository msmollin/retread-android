package org.otwebrtc.audio;

import android.content.Context;
import android.media.AudioManager;
import android.media.AudioRecord;
import android.media.AudioTrack;
import android.os.Build;
import org.otwebrtc.CalledByNative;
import org.otwebrtc.Logging;
import org.otwebrtc.MediaStreamTrack;

/* loaded from: classes2.dex */
class WebRtcAudioManager {
    private static final int BITS_PER_SAMPLE = 16;
    private static final int DEFAULT_FRAME_PER_BUFFER = 256;
    private static final int DEFAULT_SAMPLE_RATE_HZ = 16000;
    private static final String TAG = "WebRtcAudioManagerExternal";

    WebRtcAudioManager() {
    }

    @CalledByNative
    static AudioManager getAudioManager(Context context) {
        return (AudioManager) context.getSystemService(MediaStreamTrack.AUDIO_TRACK_KIND);
    }

    @CalledByNative
    static int getInputBufferSize(Context context, AudioManager audioManager, int i, int i2) {
        return isLowLatencyInputSupported(context) ? getLowLatencyFramesPerBuffer(audioManager) : getMinInputFrameSize(i, i2);
    }

    private static int getLowLatencyFramesPerBuffer(AudioManager audioManager) {
        String property;
        if (Build.VERSION.SDK_INT >= 17 && (property = audioManager.getProperty("android.media.property.OUTPUT_FRAMES_PER_BUFFER")) != null) {
            return Integer.parseInt(property);
        }
        return 256;
    }

    private static int getMinInputFrameSize(int i, int i2) {
        return AudioRecord.getMinBufferSize(i, i2 == 1 ? 16 : 12, 2) / (i2 * 2);
    }

    private static int getMinOutputFrameSize(int i, int i2) {
        return AudioTrack.getMinBufferSize(i, i2 == 1 ? 4 : 12, 2) / (i2 * 2);
    }

    @CalledByNative
    static int getOutputBufferSize(Context context, AudioManager audioManager, int i, int i2) {
        return isLowLatencyOutputSupported(context) ? getLowLatencyFramesPerBuffer(audioManager) : getMinOutputFrameSize(i, i2);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @CalledByNative
    public static int getSampleRate(AudioManager audioManager) {
        if (WebRtcAudioUtils.runningOnEmulator()) {
            Logging.d(TAG, "Running emulator, overriding sample rate to 8 kHz.");
            return 8000;
        }
        int sampleRateForApiLevel = getSampleRateForApiLevel(audioManager);
        Logging.d(TAG, "Sample rate is set to " + sampleRateForApiLevel + " Hz");
        return sampleRateForApiLevel;
    }

    private static int getSampleRateForApiLevel(AudioManager audioManager) {
        String property;
        return (Build.VERSION.SDK_INT >= 17 && (property = audioManager.getProperty("android.media.property.OUTPUT_SAMPLE_RATE")) != null) ? Integer.parseInt(property) : DEFAULT_SAMPLE_RATE_HZ;
    }

    private static boolean isLowLatencyInputSupported(Context context) {
        return Build.VERSION.SDK_INT >= 21 && isLowLatencyOutputSupported(context);
    }

    private static boolean isLowLatencyOutputSupported(Context context) {
        return context.getPackageManager().hasSystemFeature("android.hardware.audio.low_latency");
    }
}
