package com.opentok.android;

import com.opentok.android.BaseAudioDevice;
import com.opentok.android.OtLog;

/* loaded from: classes.dex */
public class AudioDeviceManager {
    private static final OtLog.LogToken log = new OtLog.LogToken();
    private static BaseAudioDevice mBaseAudioDevice;

    public static BaseAudioDevice getAudioDevice() {
        log.d("getAudioDevice() called", new Object[0]);
        return mBaseAudioDevice;
    }

    public static void setAudioDevice(BaseAudioDevice baseAudioDevice) {
        log.d("setAudioDevice() called", new Object[0]);
        if (mBaseAudioDevice != null) {
            throw new IllegalStateException("AudioDevice can only be changed before initialization.");
        }
        if (baseAudioDevice != null && baseAudioDevice.getAudioBus() == null) {
            baseAudioDevice.setAudioBus(new BaseAudioDevice.AudioBus(baseAudioDevice));
        }
        mBaseAudioDevice = baseAudioDevice;
    }
}
