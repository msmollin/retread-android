package com.opentok.otc;

/* loaded from: classes.dex */
public class otc_on_mute_forced_info {
    protected transient boolean swigCMemOwn;
    private transient long swigCPtr;

    public otc_on_mute_forced_info() {
        this(opentokJNI.new_otc_on_mute_forced_info(), true);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public otc_on_mute_forced_info(long j, boolean z) {
        this.swigCMemOwn = z;
        this.swigCPtr = j;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public static long getCPtr(otc_on_mute_forced_info otc_on_mute_forced_infoVar) {
        if (otc_on_mute_forced_infoVar == null) {
            return 0L;
        }
        return otc_on_mute_forced_infoVar.swigCPtr;
    }

    public synchronized void delete() {
        long j = this.swigCPtr;
        if (j != 0) {
            if (this.swigCMemOwn) {
                this.swigCMemOwn = false;
                opentokJNI.delete_otc_on_mute_forced_info(j);
            }
            this.swigCPtr = 0L;
        }
    }

    protected void finalize() {
        delete();
    }

    public int getActive() {
        return opentokJNI.otc_on_mute_forced_info_active_get(this.swigCPtr, this);
    }

    public void setActive(int i) {
        opentokJNI.otc_on_mute_forced_info_active_set(this.swigCPtr, this, i);
    }
}
