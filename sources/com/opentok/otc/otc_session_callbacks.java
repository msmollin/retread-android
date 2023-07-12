package com.opentok.otc;

/* loaded from: classes.dex */
public class otc_session_callbacks {
    protected transient boolean swigCMemOwn;
    private transient long swigCPtr;

    public otc_session_callbacks() {
        this(opentokJNI.new_otc_session_callbacks(), true);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public otc_session_callbacks(long j, boolean z) {
        this.swigCMemOwn = z;
        this.swigCPtr = j;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public static long getCPtr(otc_session_callbacks otc_session_callbacksVar) {
        if (otc_session_callbacksVar == null) {
            return 0L;
        }
        return otc_session_callbacksVar.swigCPtr;
    }

    public synchronized void delete() {
        long j = this.swigCPtr;
        if (j != 0) {
            if (this.swigCMemOwn) {
                this.swigCMemOwn = false;
                opentokJNI.delete_otc_session_callbacks(j);
            }
            this.swigCPtr = 0L;
        }
    }

    protected void finalize() {
        delete();
    }

    public SWIGTYPE_p_f_p_struct_otc_session_p_void_p_q_const__char_p_q_const__char__void getOn_archive_started() {
        long otc_session_callbacks_on_archive_started_get = opentokJNI.otc_session_callbacks_on_archive_started_get(this.swigCPtr, this);
        if (otc_session_callbacks_on_archive_started_get == 0) {
            return null;
        }
        return new SWIGTYPE_p_f_p_struct_otc_session_p_void_p_q_const__char_p_q_const__char__void(otc_session_callbacks_on_archive_started_get, false);
    }

    public SWIGTYPE_p_f_p_struct_otc_session_p_void_p_q_const__char__void getOn_archive_stopped() {
        long otc_session_callbacks_on_archive_stopped_get = opentokJNI.otc_session_callbacks_on_archive_stopped_get(this.swigCPtr, this);
        if (otc_session_callbacks_on_archive_stopped_get == 0) {
            return null;
        }
        return new SWIGTYPE_p_f_p_struct_otc_session_p_void_p_q_const__char__void(otc_session_callbacks_on_archive_stopped_get, false);
    }

    public SWIGTYPE_p_f_p_struct_otc_session_p_void__void getOn_connected() {
        long otc_session_callbacks_on_connected_get = opentokJNI.otc_session_callbacks_on_connected_get(this.swigCPtr, this);
        if (otc_session_callbacks_on_connected_get == 0) {
            return null;
        }
        return new SWIGTYPE_p_f_p_struct_otc_session_p_void__void(otc_session_callbacks_on_connected_get, false);
    }

    public SWIGTYPE_p_f_p_struct_otc_session_p_void_p_q_const__struct_otc_connection__void getOn_connection_created() {
        long otc_session_callbacks_on_connection_created_get = opentokJNI.otc_session_callbacks_on_connection_created_get(this.swigCPtr, this);
        if (otc_session_callbacks_on_connection_created_get == 0) {
            return null;
        }
        return new SWIGTYPE_p_f_p_struct_otc_session_p_void_p_q_const__struct_otc_connection__void(otc_session_callbacks_on_connection_created_get, false);
    }

    public SWIGTYPE_p_f_p_struct_otc_session_p_void_p_q_const__struct_otc_connection__void getOn_connection_dropped() {
        long otc_session_callbacks_on_connection_dropped_get = opentokJNI.otc_session_callbacks_on_connection_dropped_get(this.swigCPtr, this);
        if (otc_session_callbacks_on_connection_dropped_get == 0) {
            return null;
        }
        return new SWIGTYPE_p_f_p_struct_otc_session_p_void_p_q_const__struct_otc_connection__void(otc_session_callbacks_on_connection_dropped_get, false);
    }

    public SWIGTYPE_p_f_p_struct_otc_session_p_void__void getOn_disconnected() {
        long otc_session_callbacks_on_disconnected_get = opentokJNI.otc_session_callbacks_on_disconnected_get(this.swigCPtr, this);
        if (otc_session_callbacks_on_disconnected_get == 0) {
            return null;
        }
        return new SWIGTYPE_p_f_p_struct_otc_session_p_void__void(otc_session_callbacks_on_disconnected_get, false);
    }

    public SWIGTYPE_p_f_p_struct_otc_session_p_void_p_q_const__char_enum_otc_session_error_code__void getOn_error() {
        long otc_session_callbacks_on_error_get = opentokJNI.otc_session_callbacks_on_error_get(this.swigCPtr, this);
        if (otc_session_callbacks_on_error_get == 0) {
            return null;
        }
        return new SWIGTYPE_p_f_p_struct_otc_session_p_void_p_q_const__char_enum_otc_session_error_code__void(otc_session_callbacks_on_error_get, false);
    }

    public SWIGTYPE_p_f_p_struct_otc_session_p_void_p_struct_otc_on_mute_forced_info__void getOn_mute_forced() {
        long otc_session_callbacks_on_mute_forced_get = opentokJNI.otc_session_callbacks_on_mute_forced_get(this.swigCPtr, this);
        if (otc_session_callbacks_on_mute_forced_get == 0) {
            return null;
        }
        return new SWIGTYPE_p_f_p_struct_otc_session_p_void_p_struct_otc_on_mute_forced_info__void(otc_session_callbacks_on_mute_forced_get, false);
    }

    public SWIGTYPE_p_f_p_struct_otc_session_p_void__void getOn_reconnected() {
        long otc_session_callbacks_on_reconnected_get = opentokJNI.otc_session_callbacks_on_reconnected_get(this.swigCPtr, this);
        if (otc_session_callbacks_on_reconnected_get == 0) {
            return null;
        }
        return new SWIGTYPE_p_f_p_struct_otc_session_p_void__void(otc_session_callbacks_on_reconnected_get, false);
    }

    public SWIGTYPE_p_f_p_struct_otc_session_p_void__void getOn_reconnection_started() {
        long otc_session_callbacks_on_reconnection_started_get = opentokJNI.otc_session_callbacks_on_reconnection_started_get(this.swigCPtr, this);
        if (otc_session_callbacks_on_reconnection_started_get == 0) {
            return null;
        }
        return new SWIGTYPE_p_f_p_struct_otc_session_p_void__void(otc_session_callbacks_on_reconnection_started_get, false);
    }

    public SWIGTYPE_p_f_p_struct_otc_session_p_void_p_q_const__char_p_q_const__char_p_q_const__struct_otc_connection__void getOn_signal_received() {
        long otc_session_callbacks_on_signal_received_get = opentokJNI.otc_session_callbacks_on_signal_received_get(this.swigCPtr, this);
        if (otc_session_callbacks_on_signal_received_get == 0) {
            return null;
        }
        return new SWIGTYPE_p_f_p_struct_otc_session_p_void_p_q_const__char_p_q_const__char_p_q_const__struct_otc_connection__void(otc_session_callbacks_on_signal_received_get, false);
    }

    public SWIGTYPE_p_f_p_struct_otc_session_p_void_p_q_const__struct_otc_stream__void getOn_stream_dropped() {
        long otc_session_callbacks_on_stream_dropped_get = opentokJNI.otc_session_callbacks_on_stream_dropped_get(this.swigCPtr, this);
        if (otc_session_callbacks_on_stream_dropped_get == 0) {
            return null;
        }
        return new SWIGTYPE_p_f_p_struct_otc_session_p_void_p_q_const__struct_otc_stream__void(otc_session_callbacks_on_stream_dropped_get, false);
    }

    public SWIGTYPE_p_f_p_struct_otc_session_p_void_p_q_const__struct_otc_stream_int__void getOn_stream_has_audio_changed() {
        long otc_session_callbacks_on_stream_has_audio_changed_get = opentokJNI.otc_session_callbacks_on_stream_has_audio_changed_get(this.swigCPtr, this);
        if (otc_session_callbacks_on_stream_has_audio_changed_get == 0) {
            return null;
        }
        return new SWIGTYPE_p_f_p_struct_otc_session_p_void_p_q_const__struct_otc_stream_int__void(otc_session_callbacks_on_stream_has_audio_changed_get, false);
    }

    public SWIGTYPE_p_f_p_struct_otc_session_p_void_p_q_const__struct_otc_stream_int__void getOn_stream_has_video_changed() {
        long otc_session_callbacks_on_stream_has_video_changed_get = opentokJNI.otc_session_callbacks_on_stream_has_video_changed_get(this.swigCPtr, this);
        if (otc_session_callbacks_on_stream_has_video_changed_get == 0) {
            return null;
        }
        return new SWIGTYPE_p_f_p_struct_otc_session_p_void_p_q_const__struct_otc_stream_int__void(otc_session_callbacks_on_stream_has_video_changed_get, false);
    }

    public SWIGTYPE_p_f_p_struct_otc_session_p_void_p_q_const__struct_otc_stream__void getOn_stream_received() {
        long otc_session_callbacks_on_stream_received_get = opentokJNI.otc_session_callbacks_on_stream_received_get(this.swigCPtr, this);
        if (otc_session_callbacks_on_stream_received_get == 0) {
            return null;
        }
        return new SWIGTYPE_p_f_p_struct_otc_session_p_void_p_q_const__struct_otc_stream__void(otc_session_callbacks_on_stream_received_get, false);
    }

    public SWIGTYPE_p_f_p_struct_otc_session_p_void_p_q_const__struct_otc_stream_int_int__void getOn_stream_video_dimensions_changed() {
        long otc_session_callbacks_on_stream_video_dimensions_changed_get = opentokJNI.otc_session_callbacks_on_stream_video_dimensions_changed_get(this.swigCPtr, this);
        if (otc_session_callbacks_on_stream_video_dimensions_changed_get == 0) {
            return null;
        }
        return new SWIGTYPE_p_f_p_struct_otc_session_p_void_p_q_const__struct_otc_stream_int_int__void(otc_session_callbacks_on_stream_video_dimensions_changed_get, false);
    }

    public SWIGTYPE_p_f_p_struct_otc_session_p_void_p_q_const__struct_otc_stream_enum_otc_stream_video_type__void getOn_stream_video_type_changed() {
        long otc_session_callbacks_on_stream_video_type_changed_get = opentokJNI.otc_session_callbacks_on_stream_video_type_changed_get(this.swigCPtr, this);
        if (otc_session_callbacks_on_stream_video_type_changed_get == 0) {
            return null;
        }
        return new SWIGTYPE_p_f_p_struct_otc_session_p_void_p_q_const__struct_otc_stream_enum_otc_stream_video_type__void(otc_session_callbacks_on_stream_video_type_changed_get, false);
    }

    public SWIGTYPE_p_void getReserved() {
        long otc_session_callbacks_reserved_get = opentokJNI.otc_session_callbacks_reserved_get(this.swigCPtr, this);
        if (otc_session_callbacks_reserved_get == 0) {
            return null;
        }
        return new SWIGTYPE_p_void(otc_session_callbacks_reserved_get, false);
    }

    public SWIGTYPE_p_void getUser_data() {
        long otc_session_callbacks_user_data_get = opentokJNI.otc_session_callbacks_user_data_get(this.swigCPtr, this);
        if (otc_session_callbacks_user_data_get == 0) {
            return null;
        }
        return new SWIGTYPE_p_void(otc_session_callbacks_user_data_get, false);
    }

    public void setOn_archive_started(SWIGTYPE_p_f_p_struct_otc_session_p_void_p_q_const__char_p_q_const__char__void sWIGTYPE_p_f_p_struct_otc_session_p_void_p_q_const__char_p_q_const__char__void) {
        opentokJNI.otc_session_callbacks_on_archive_started_set(this.swigCPtr, this, SWIGTYPE_p_f_p_struct_otc_session_p_void_p_q_const__char_p_q_const__char__void.a(sWIGTYPE_p_f_p_struct_otc_session_p_void_p_q_const__char_p_q_const__char__void));
    }

    public void setOn_archive_stopped(SWIGTYPE_p_f_p_struct_otc_session_p_void_p_q_const__char__void sWIGTYPE_p_f_p_struct_otc_session_p_void_p_q_const__char__void) {
        opentokJNI.otc_session_callbacks_on_archive_stopped_set(this.swigCPtr, this, SWIGTYPE_p_f_p_struct_otc_session_p_void_p_q_const__char__void.a(sWIGTYPE_p_f_p_struct_otc_session_p_void_p_q_const__char__void));
    }

    public void setOn_connected(SWIGTYPE_p_f_p_struct_otc_session_p_void__void sWIGTYPE_p_f_p_struct_otc_session_p_void__void) {
        opentokJNI.otc_session_callbacks_on_connected_set(this.swigCPtr, this, SWIGTYPE_p_f_p_struct_otc_session_p_void__void.a(sWIGTYPE_p_f_p_struct_otc_session_p_void__void));
    }

    public void setOn_connection_created(SWIGTYPE_p_f_p_struct_otc_session_p_void_p_q_const__struct_otc_connection__void sWIGTYPE_p_f_p_struct_otc_session_p_void_p_q_const__struct_otc_connection__void) {
        opentokJNI.otc_session_callbacks_on_connection_created_set(this.swigCPtr, this, SWIGTYPE_p_f_p_struct_otc_session_p_void_p_q_const__struct_otc_connection__void.a(sWIGTYPE_p_f_p_struct_otc_session_p_void_p_q_const__struct_otc_connection__void));
    }

    public void setOn_connection_dropped(SWIGTYPE_p_f_p_struct_otc_session_p_void_p_q_const__struct_otc_connection__void sWIGTYPE_p_f_p_struct_otc_session_p_void_p_q_const__struct_otc_connection__void) {
        opentokJNI.otc_session_callbacks_on_connection_dropped_set(this.swigCPtr, this, SWIGTYPE_p_f_p_struct_otc_session_p_void_p_q_const__struct_otc_connection__void.a(sWIGTYPE_p_f_p_struct_otc_session_p_void_p_q_const__struct_otc_connection__void));
    }

    public void setOn_disconnected(SWIGTYPE_p_f_p_struct_otc_session_p_void__void sWIGTYPE_p_f_p_struct_otc_session_p_void__void) {
        opentokJNI.otc_session_callbacks_on_disconnected_set(this.swigCPtr, this, SWIGTYPE_p_f_p_struct_otc_session_p_void__void.a(sWIGTYPE_p_f_p_struct_otc_session_p_void__void));
    }

    public void setOn_error(SWIGTYPE_p_f_p_struct_otc_session_p_void_p_q_const__char_enum_otc_session_error_code__void sWIGTYPE_p_f_p_struct_otc_session_p_void_p_q_const__char_enum_otc_session_error_code__void) {
        opentokJNI.otc_session_callbacks_on_error_set(this.swigCPtr, this, SWIGTYPE_p_f_p_struct_otc_session_p_void_p_q_const__char_enum_otc_session_error_code__void.a(sWIGTYPE_p_f_p_struct_otc_session_p_void_p_q_const__char_enum_otc_session_error_code__void));
    }

    public void setOn_mute_forced(SWIGTYPE_p_f_p_struct_otc_session_p_void_p_struct_otc_on_mute_forced_info__void sWIGTYPE_p_f_p_struct_otc_session_p_void_p_struct_otc_on_mute_forced_info__void) {
        opentokJNI.otc_session_callbacks_on_mute_forced_set(this.swigCPtr, this, SWIGTYPE_p_f_p_struct_otc_session_p_void_p_struct_otc_on_mute_forced_info__void.a(sWIGTYPE_p_f_p_struct_otc_session_p_void_p_struct_otc_on_mute_forced_info__void));
    }

    public void setOn_reconnected(SWIGTYPE_p_f_p_struct_otc_session_p_void__void sWIGTYPE_p_f_p_struct_otc_session_p_void__void) {
        opentokJNI.otc_session_callbacks_on_reconnected_set(this.swigCPtr, this, SWIGTYPE_p_f_p_struct_otc_session_p_void__void.a(sWIGTYPE_p_f_p_struct_otc_session_p_void__void));
    }

    public void setOn_reconnection_started(SWIGTYPE_p_f_p_struct_otc_session_p_void__void sWIGTYPE_p_f_p_struct_otc_session_p_void__void) {
        opentokJNI.otc_session_callbacks_on_reconnection_started_set(this.swigCPtr, this, SWIGTYPE_p_f_p_struct_otc_session_p_void__void.a(sWIGTYPE_p_f_p_struct_otc_session_p_void__void));
    }

    public void setOn_signal_received(SWIGTYPE_p_f_p_struct_otc_session_p_void_p_q_const__char_p_q_const__char_p_q_const__struct_otc_connection__void sWIGTYPE_p_f_p_struct_otc_session_p_void_p_q_const__char_p_q_const__char_p_q_const__struct_otc_connection__void) {
        opentokJNI.otc_session_callbacks_on_signal_received_set(this.swigCPtr, this, SWIGTYPE_p_f_p_struct_otc_session_p_void_p_q_const__char_p_q_const__char_p_q_const__struct_otc_connection__void.a(sWIGTYPE_p_f_p_struct_otc_session_p_void_p_q_const__char_p_q_const__char_p_q_const__struct_otc_connection__void));
    }

    public void setOn_stream_dropped(SWIGTYPE_p_f_p_struct_otc_session_p_void_p_q_const__struct_otc_stream__void sWIGTYPE_p_f_p_struct_otc_session_p_void_p_q_const__struct_otc_stream__void) {
        opentokJNI.otc_session_callbacks_on_stream_dropped_set(this.swigCPtr, this, SWIGTYPE_p_f_p_struct_otc_session_p_void_p_q_const__struct_otc_stream__void.a(sWIGTYPE_p_f_p_struct_otc_session_p_void_p_q_const__struct_otc_stream__void));
    }

    public void setOn_stream_has_audio_changed(SWIGTYPE_p_f_p_struct_otc_session_p_void_p_q_const__struct_otc_stream_int__void sWIGTYPE_p_f_p_struct_otc_session_p_void_p_q_const__struct_otc_stream_int__void) {
        opentokJNI.otc_session_callbacks_on_stream_has_audio_changed_set(this.swigCPtr, this, SWIGTYPE_p_f_p_struct_otc_session_p_void_p_q_const__struct_otc_stream_int__void.a(sWIGTYPE_p_f_p_struct_otc_session_p_void_p_q_const__struct_otc_stream_int__void));
    }

    public void setOn_stream_has_video_changed(SWIGTYPE_p_f_p_struct_otc_session_p_void_p_q_const__struct_otc_stream_int__void sWIGTYPE_p_f_p_struct_otc_session_p_void_p_q_const__struct_otc_stream_int__void) {
        opentokJNI.otc_session_callbacks_on_stream_has_video_changed_set(this.swigCPtr, this, SWIGTYPE_p_f_p_struct_otc_session_p_void_p_q_const__struct_otc_stream_int__void.a(sWIGTYPE_p_f_p_struct_otc_session_p_void_p_q_const__struct_otc_stream_int__void));
    }

    public void setOn_stream_received(SWIGTYPE_p_f_p_struct_otc_session_p_void_p_q_const__struct_otc_stream__void sWIGTYPE_p_f_p_struct_otc_session_p_void_p_q_const__struct_otc_stream__void) {
        opentokJNI.otc_session_callbacks_on_stream_received_set(this.swigCPtr, this, SWIGTYPE_p_f_p_struct_otc_session_p_void_p_q_const__struct_otc_stream__void.a(sWIGTYPE_p_f_p_struct_otc_session_p_void_p_q_const__struct_otc_stream__void));
    }

    public void setOn_stream_video_dimensions_changed(SWIGTYPE_p_f_p_struct_otc_session_p_void_p_q_const__struct_otc_stream_int_int__void sWIGTYPE_p_f_p_struct_otc_session_p_void_p_q_const__struct_otc_stream_int_int__void) {
        opentokJNI.otc_session_callbacks_on_stream_video_dimensions_changed_set(this.swigCPtr, this, SWIGTYPE_p_f_p_struct_otc_session_p_void_p_q_const__struct_otc_stream_int_int__void.a(sWIGTYPE_p_f_p_struct_otc_session_p_void_p_q_const__struct_otc_stream_int_int__void));
    }

    public void setOn_stream_video_type_changed(SWIGTYPE_p_f_p_struct_otc_session_p_void_p_q_const__struct_otc_stream_enum_otc_stream_video_type__void sWIGTYPE_p_f_p_struct_otc_session_p_void_p_q_const__struct_otc_stream_enum_otc_stream_video_type__void) {
        opentokJNI.otc_session_callbacks_on_stream_video_type_changed_set(this.swigCPtr, this, SWIGTYPE_p_f_p_struct_otc_session_p_void_p_q_const__struct_otc_stream_enum_otc_stream_video_type__void.a(sWIGTYPE_p_f_p_struct_otc_session_p_void_p_q_const__struct_otc_stream_enum_otc_stream_video_type__void));
    }

    public void setReserved(SWIGTYPE_p_void sWIGTYPE_p_void) {
        opentokJNI.otc_session_callbacks_reserved_set(this.swigCPtr, this, SWIGTYPE_p_void.a(sWIGTYPE_p_void));
    }

    public void setUser_data(SWIGTYPE_p_void sWIGTYPE_p_void) {
        opentokJNI.otc_session_callbacks_user_data_set(this.swigCPtr, this, SWIGTYPE_p_void.a(sWIGTYPE_p_void));
    }
}
