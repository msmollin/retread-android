package com.opentok.otc;

/* loaded from: classes.dex */
public class otc_subscriber_rtc_stats_report_cb {
    protected transient boolean swigCMemOwn;
    private transient long swigCPtr;

    public otc_subscriber_rtc_stats_report_cb() {
        this(opentokJNI.new_otc_subscriber_rtc_stats_report_cb(), true);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public otc_subscriber_rtc_stats_report_cb(long j, boolean z) {
        this.swigCMemOwn = z;
        this.swigCPtr = j;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public static long getCPtr(otc_subscriber_rtc_stats_report_cb otc_subscriber_rtc_stats_report_cbVar) {
        if (otc_subscriber_rtc_stats_report_cbVar == null) {
            return 0L;
        }
        return otc_subscriber_rtc_stats_report_cbVar.swigCPtr;
    }

    public synchronized void delete() {
        long j = this.swigCPtr;
        if (j != 0) {
            if (this.swigCMemOwn) {
                this.swigCMemOwn = false;
                opentokJNI.delete_otc_subscriber_rtc_stats_report_cb(j);
            }
            this.swigCPtr = 0L;
        }
    }

    protected void finalize() {
        delete();
    }

    public SWIGTYPE_p_f_p_struct_otc_subscriber_p_void_p_q_const__char__void getOn_rtc_stats_report() {
        long otc_subscriber_rtc_stats_report_cb_on_rtc_stats_report_get = opentokJNI.otc_subscriber_rtc_stats_report_cb_on_rtc_stats_report_get(this.swigCPtr, this);
        if (otc_subscriber_rtc_stats_report_cb_on_rtc_stats_report_get == 0) {
            return null;
        }
        return new SWIGTYPE_p_f_p_struct_otc_subscriber_p_void_p_q_const__char__void(otc_subscriber_rtc_stats_report_cb_on_rtc_stats_report_get, false);
    }

    public SWIGTYPE_p_void getReserved() {
        long otc_subscriber_rtc_stats_report_cb_reserved_get = opentokJNI.otc_subscriber_rtc_stats_report_cb_reserved_get(this.swigCPtr, this);
        if (otc_subscriber_rtc_stats_report_cb_reserved_get == 0) {
            return null;
        }
        return new SWIGTYPE_p_void(otc_subscriber_rtc_stats_report_cb_reserved_get, false);
    }

    public SWIGTYPE_p_void getUser_data() {
        long otc_subscriber_rtc_stats_report_cb_user_data_get = opentokJNI.otc_subscriber_rtc_stats_report_cb_user_data_get(this.swigCPtr, this);
        if (otc_subscriber_rtc_stats_report_cb_user_data_get == 0) {
            return null;
        }
        return new SWIGTYPE_p_void(otc_subscriber_rtc_stats_report_cb_user_data_get, false);
    }

    public void setOn_rtc_stats_report(SWIGTYPE_p_f_p_struct_otc_subscriber_p_void_p_q_const__char__void sWIGTYPE_p_f_p_struct_otc_subscriber_p_void_p_q_const__char__void) {
        opentokJNI.otc_subscriber_rtc_stats_report_cb_on_rtc_stats_report_set(this.swigCPtr, this, SWIGTYPE_p_f_p_struct_otc_subscriber_p_void_p_q_const__char__void.a(sWIGTYPE_p_f_p_struct_otc_subscriber_p_void_p_q_const__char__void));
    }

    public void setReserved(SWIGTYPE_p_void sWIGTYPE_p_void) {
        opentokJNI.otc_subscriber_rtc_stats_report_cb_reserved_set(this.swigCPtr, this, SWIGTYPE_p_void.a(sWIGTYPE_p_void));
    }

    public void setUser_data(SWIGTYPE_p_void sWIGTYPE_p_void) {
        opentokJNI.otc_subscriber_rtc_stats_report_cb_user_data_set(this.swigCPtr, this, SWIGTYPE_p_void.a(sWIGTYPE_p_void));
    }
}
