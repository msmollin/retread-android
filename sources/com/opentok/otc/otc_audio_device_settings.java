package com.opentok.otc;

/* loaded from: classes.dex */
public class otc_audio_device_settings {
    protected transient boolean swigCMemOwn;
    private transient long swigCPtr;

    public otc_audio_device_settings() {
        this(opentokJNI.new_otc_audio_device_settings(), true);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public otc_audio_device_settings(long j, boolean z) {
        this.swigCMemOwn = z;
        this.swigCPtr = j;
    }

    protected static long getCPtr(otc_audio_device_settings otc_audio_device_settingsVar) {
        if (otc_audio_device_settingsVar == null) {
            return 0L;
        }
        return otc_audio_device_settingsVar.swigCPtr;
    }

    public synchronized void delete() {
        long j = this.swigCPtr;
        if (j != 0) {
            if (this.swigCMemOwn) {
                this.swigCMemOwn = false;
                opentokJNI.delete_otc_audio_device_settings(j);
            }
            this.swigCPtr = 0L;
        }
    }

    protected void finalize() {
        delete();
    }

    public int getNumber_of_channels() {
        return opentokJNI.otc_audio_device_settings_number_of_channels_get(this.swigCPtr, this);
    }

    public int getSampling_rate() {
        return opentokJNI.otc_audio_device_settings_sampling_rate_get(this.swigCPtr, this);
    }

    public void setNumber_of_channels(int i) {
        opentokJNI.otc_audio_device_settings_number_of_channels_set(this.swigCPtr, this, i);
    }

    public void setSampling_rate(int i) {
        opentokJNI.otc_audio_device_settings_sampling_rate_set(this.swigCPtr, this, i);
    }
}
