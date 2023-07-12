package com.opentok.otc;

/* loaded from: classes.dex */
public class otc_video_capturer_settings {
    protected transient boolean swigCMemOwn;
    private transient long swigCPtr;

    public otc_video_capturer_settings() {
        this(opentokJNI.new_otc_video_capturer_settings(), true);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public otc_video_capturer_settings(long j, boolean z) {
        this.swigCMemOwn = z;
        this.swigCPtr = j;
    }

    protected static long getCPtr(otc_video_capturer_settings otc_video_capturer_settingsVar) {
        if (otc_video_capturer_settingsVar == null) {
            return 0L;
        }
        return otc_video_capturer_settingsVar.swigCPtr;
    }

    public synchronized void delete() {
        long j = this.swigCPtr;
        if (j != 0) {
            if (this.swigCMemOwn) {
                this.swigCMemOwn = false;
                opentokJNI.delete_otc_video_capturer_settings(j);
            }
            this.swigCPtr = 0L;
        }
    }

    protected void finalize() {
        delete();
    }

    public int getExpected_delay() {
        return opentokJNI.otc_video_capturer_settings_expected_delay_get(this.swigCPtr, this);
    }

    public int getFormat() {
        return opentokJNI.otc_video_capturer_settings_format_get(this.swigCPtr, this);
    }

    public int getFps() {
        return opentokJNI.otc_video_capturer_settings_fps_get(this.swigCPtr, this);
    }

    public int getHeight() {
        return opentokJNI.otc_video_capturer_settings_height_get(this.swigCPtr, this);
    }

    public int getMirror_on_local_render() {
        return opentokJNI.otc_video_capturer_settings_mirror_on_local_render_get(this.swigCPtr, this);
    }

    public int getWidth() {
        return opentokJNI.otc_video_capturer_settings_width_get(this.swigCPtr, this);
    }

    public void setExpected_delay(int i) {
        opentokJNI.otc_video_capturer_settings_expected_delay_set(this.swigCPtr, this, i);
    }

    public void setFormat(int i) {
        opentokJNI.otc_video_capturer_settings_format_set(this.swigCPtr, this, i);
    }

    public void setFps(int i) {
        opentokJNI.otc_video_capturer_settings_fps_set(this.swigCPtr, this, i);
    }

    public void setHeight(int i) {
        opentokJNI.otc_video_capturer_settings_height_set(this.swigCPtr, this, i);
    }

    public void setMirror_on_local_render(int i) {
        opentokJNI.otc_video_capturer_settings_mirror_on_local_render_set(this.swigCPtr, this, i);
    }

    public void setWidth(int i) {
        opentokJNI.otc_video_capturer_settings_width_set(this.swigCPtr, this, i);
    }
}
