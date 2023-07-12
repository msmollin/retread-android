package com.opentok.otc;

/* loaded from: classes.dex */
public class e implements f {
    public static int a(SWIGTYPE_p_otc_publisher sWIGTYPE_p_otc_publisher) {
        return opentokJNI.otc_publisher_delete(SWIGTYPE_p_otc_publisher.a(sWIGTYPE_p_otc_publisher));
    }

    public static int a(SWIGTYPE_p_otc_publisher sWIGTYPE_p_otc_publisher, int i) {
        return opentokJNI.otc_publisher_set_audio_fallback_enabled(SWIGTYPE_p_otc_publisher.a(sWIGTYPE_p_otc_publisher), i);
    }

    public static int a(SWIGTYPE_p_otc_publisher sWIGTYPE_p_otc_publisher, long j) {
        return opentokJNI.otc_publisher_set_max_audio_bitrate(SWIGTYPE_p_otc_publisher.a(sWIGTYPE_p_otc_publisher), j);
    }

    public static int a(SWIGTYPE_p_otc_publisher sWIGTYPE_p_otc_publisher, i iVar) {
        return opentokJNI.otc_publisher_set_video_type(SWIGTYPE_p_otc_publisher.a(sWIGTYPE_p_otc_publisher), iVar.a());
    }

    public static int a(SWIGTYPE_p_otc_publisher sWIGTYPE_p_otc_publisher, otc_publisher_rtc_stats_report_cb otc_publisher_rtc_stats_report_cbVar) {
        return opentokJNI.otc_publisher_set_rtc_stats_report_cb(SWIGTYPE_p_otc_publisher.a(sWIGTYPE_p_otc_publisher), otc_publisher_rtc_stats_report_cb.getCPtr(otc_publisher_rtc_stats_report_cbVar), otc_publisher_rtc_stats_report_cbVar);
    }

    public static int a(SWIGTYPE_p_otc_session sWIGTYPE_p_otc_session) {
        return opentokJNI.otc_session_delete(SWIGTYPE_p_otc_session.a(sWIGTYPE_p_otc_session));
    }

    public static int a(SWIGTYPE_p_otc_session sWIGTYPE_p_otc_session, SWIGTYPE_p_otc_publisher sWIGTYPE_p_otc_publisher) {
        return opentokJNI.otc_session_publish(SWIGTYPE_p_otc_session.a(sWIGTYPE_p_otc_session), SWIGTYPE_p_otc_publisher.a(sWIGTYPE_p_otc_publisher));
    }

    public static int a(SWIGTYPE_p_otc_session sWIGTYPE_p_otc_session, SWIGTYPE_p_otc_subscriber sWIGTYPE_p_otc_subscriber) {
        return opentokJNI.otc_session_subscribe(SWIGTYPE_p_otc_session.a(sWIGTYPE_p_otc_session), SWIGTYPE_p_otc_subscriber.a(sWIGTYPE_p_otc_subscriber));
    }

    public static int a(SWIGTYPE_p_otc_session sWIGTYPE_p_otc_session, String str) {
        return opentokJNI.otc_session_force_mute_stream(SWIGTYPE_p_otc_session.a(sWIGTYPE_p_otc_session), str);
    }

    public static int a(SWIGTYPE_p_otc_session sWIGTYPE_p_otc_session, String str, String str2) {
        return opentokJNI.otc_session_log_custom_client_event(SWIGTYPE_p_otc_session.a(sWIGTYPE_p_otc_session), str, str2);
    }

    public static int a(SWIGTYPE_p_otc_session sWIGTYPE_p_otc_session, String str, String str2, int i, int i2, String str3) {
        return opentokJNI.otc_session_connect_to_host(SWIGTYPE_p_otc_session.a(sWIGTYPE_p_otc_session), str, str2, i, i2, str3);
    }

    public static int a(SWIGTYPE_p_otc_session sWIGTYPE_p_otc_session, String str, String str2, SWIGTYPE_p_otc_connection sWIGTYPE_p_otc_connection) {
        return opentokJNI.otc_session_send_signal_to_connection(SWIGTYPE_p_otc_session.a(sWIGTYPE_p_otc_session), str, str2, SWIGTYPE_p_otc_connection.getCPtr(sWIGTYPE_p_otc_connection));
    }

    public static int a(SWIGTYPE_p_otc_session sWIGTYPE_p_otc_session, String str, String str2, SWIGTYPE_p_otc_connection sWIGTYPE_p_otc_connection, otc_signal_options otc_signal_optionsVar) {
        return opentokJNI.otc_session_send_signal_to_connection_with_options(SWIGTYPE_p_otc_session.a(sWIGTYPE_p_otc_session), str, str2, SWIGTYPE_p_otc_connection.getCPtr(sWIGTYPE_p_otc_connection), otc_signal_options.a(otc_signal_optionsVar), otc_signal_optionsVar);
    }

    public static int a(SWIGTYPE_p_otc_session sWIGTYPE_p_otc_session, String str, String str2, otc_signal_options otc_signal_optionsVar) {
        return opentokJNI.otc_session_send_signal_with_options(SWIGTYPE_p_otc_session.a(sWIGTYPE_p_otc_session), str, str2, otc_signal_options.a(otc_signal_optionsVar), otc_signal_optionsVar);
    }

    public static int a(SWIGTYPE_p_otc_session sWIGTYPE_p_otc_session, String[] strArr, int i) {
        return opentokJNI.otc_session_force_mute_all(SWIGTYPE_p_otc_session.a(sWIGTYPE_p_otc_session), strArr, i);
    }

    public static int a(SWIGTYPE_p_otc_subscriber sWIGTYPE_p_otc_subscriber) {
        return opentokJNI.otc_subscriber_delete(SWIGTYPE_p_otc_subscriber.a(sWIGTYPE_p_otc_subscriber));
    }

    public static int a(SWIGTYPE_p_otc_subscriber sWIGTYPE_p_otc_subscriber, float f) {
        return opentokJNI.otc_subscriber_set_preferred_framerate(SWIGTYPE_p_otc_subscriber.a(sWIGTYPE_p_otc_subscriber), f);
    }

    public static int a(SWIGTYPE_p_otc_subscriber sWIGTYPE_p_otc_subscriber, int i) {
        return opentokJNI.otc_subscriber_set_subscribe_to_audio(SWIGTYPE_p_otc_subscriber.a(sWIGTYPE_p_otc_subscriber), i);
    }

    public static int a(SWIGTYPE_p_otc_subscriber sWIGTYPE_p_otc_subscriber, long j, long j2) {
        return opentokJNI.otc_subscriber_set_preferred_resolution(SWIGTYPE_p_otc_subscriber.a(sWIGTYPE_p_otc_subscriber), j, j2);
    }

    public static int a(SWIGTYPE_p_otc_subscriber sWIGTYPE_p_otc_subscriber, otc_subscriber_rtc_stats_report_cb otc_subscriber_rtc_stats_report_cbVar) {
        return opentokJNI.otc_subscriber_set_rtc_stats_report_cb(SWIGTYPE_p_otc_subscriber.a(sWIGTYPE_p_otc_subscriber), otc_subscriber_rtc_stats_report_cb.getCPtr(otc_subscriber_rtc_stats_report_cbVar), otc_subscriber_rtc_stats_report_cbVar);
    }

    public static int a(SWIGTYPE_p_otc_subscriber sWIGTYPE_p_otc_subscriber, float[] fArr) {
        return opentokJNI.otc_subscriber_get_preferred_framerate(SWIGTYPE_p_otc_subscriber.a(sWIGTYPE_p_otc_subscriber), fArr);
    }

    public static int a(SWIGTYPE_p_otc_subscriber sWIGTYPE_p_otc_subscriber, long[] jArr, long[] jArr2) {
        return opentokJNI.otc_subscriber_get_preferred_resolution(SWIGTYPE_p_otc_subscriber.a(sWIGTYPE_p_otc_subscriber), jArr, jArr2);
    }

    public static int a(SWIGTYPE_p_otc_video_frame sWIGTYPE_p_otc_video_frame, k kVar) {
        return opentokJNI.otc_video_frame_get_plane_stride(SWIGTYPE_p_otc_video_frame.getCPtr(sWIGTYPE_p_otc_video_frame), kVar.a());
    }

    public static int a(a aVar) {
        return opentokJNI.otc_publisher_settings_delete(a.a(aVar));
    }

    public static int a(a aVar, int i) {
        return opentokJNI.otc_publisher_settings_set_audio_track(a.a(aVar), i);
    }

    public static int a(a aVar, otc_video_capturer_callbacks otc_video_capturer_callbacksVar) {
        return opentokJNI.otc_publisher_settings_set_video_capturer(a.a(aVar), otc_video_capturer_callbacks.getCPtr(otc_video_capturer_callbacksVar), otc_video_capturer_callbacksVar);
    }

    public static int a(a aVar, String str) {
        return opentokJNI.otc_publisher_settings_set_name(a.a(aVar), str);
    }

    public static int a(b bVar) {
        return opentokJNI.otc_session_settings_delete(b.a(bVar));
    }

    public static int a(b bVar, int i) {
        return opentokJNI.otc_session_settings_set_connection_events_suppressed(b.a(bVar), i);
    }

    public static int a(b bVar, int i, String[] strArr, String[] strArr2, String[] strArr3, int i2, int i3) {
        return opentokJNI.otc_session_settings_set_custom_ice_config_no_struct(b.a(bVar), i, strArr, strArr2, strArr3, i2, i3);
    }

    public static int a(b bVar, String str) {
        return opentokJNI.otc_session_settings_set_proxy_url(b.a(bVar), str);
    }

    public static long a(c cVar) {
        return opentokJNI.sizetPointer_value(c.a(cVar));
    }

    public static SWIGTYPE_p_otc_connection a(SWIGTYPE_p_otc_connection sWIGTYPE_p_otc_connection) {
        long otc_connection_copy = opentokJNI.otc_connection_copy(SWIGTYPE_p_otc_connection.getCPtr(sWIGTYPE_p_otc_connection));
        if (otc_connection_copy == 0) {
            return null;
        }
        return new SWIGTYPE_p_otc_connection(otc_connection_copy, false);
    }

    public static SWIGTYPE_p_otc_publisher a(otc_publisher_callbacks otc_publisher_callbacksVar, a aVar) {
        long otc_publisher_new_with_settings = opentokJNI.otc_publisher_new_with_settings(otc_publisher_callbacks.getCPtr(otc_publisher_callbacksVar), otc_publisher_callbacksVar, a.a(aVar));
        if (otc_publisher_new_with_settings == 0) {
            return null;
        }
        return new SWIGTYPE_p_otc_publisher(otc_publisher_new_with_settings, false);
    }

    public static SWIGTYPE_p_otc_session a(String str, String str2, otc_session_callbacks otc_session_callbacksVar, b bVar) {
        long otc_session_new_with_settings = opentokJNI.otc_session_new_with_settings(str, str2, otc_session_callbacks.getCPtr(otc_session_callbacksVar), otc_session_callbacksVar, b.a(bVar));
        if (otc_session_new_with_settings == 0) {
            return null;
        }
        return new SWIGTYPE_p_otc_session(otc_session_new_with_settings, false);
    }

    public static SWIGTYPE_p_otc_stream a(SWIGTYPE_p_otc_stream sWIGTYPE_p_otc_stream) {
        long otc_stream_copy = opentokJNI.otc_stream_copy(SWIGTYPE_p_otc_stream.getCPtr(sWIGTYPE_p_otc_stream));
        if (otc_stream_copy == 0) {
            return null;
        }
        return new SWIGTYPE_p_otc_stream(otc_stream_copy, false);
    }

    public static SWIGTYPE_p_otc_subscriber a(SWIGTYPE_p_otc_stream sWIGTYPE_p_otc_stream, otc_subscriber_callbacks otc_subscriber_callbacksVar) {
        long otc_subscriber_new = opentokJNI.otc_subscriber_new(SWIGTYPE_p_otc_stream.getCPtr(sWIGTYPE_p_otc_stream), otc_subscriber_callbacks.getCPtr(otc_subscriber_callbacksVar), otc_subscriber_callbacksVar);
        if (otc_subscriber_new == 0) {
            return null;
        }
        return new SWIGTYPE_p_otc_subscriber(otc_subscriber_new, false);
    }

    public static c a() {
        long new_sizetPointer = opentokJNI.new_sizetPointer();
        if (new_sizetPointer == 0) {
            return null;
        }
        return new c(new_sizetPointer, false);
    }

    public static d a(SWIGTYPE_p_otc_video_frame sWIGTYPE_p_otc_video_frame, c cVar) {
        long otc_video_frame_get_metadata = opentokJNI.otc_video_frame_get_metadata(SWIGTYPE_p_otc_video_frame.getCPtr(sWIGTYPE_p_otc_video_frame), c.a(cVar));
        if (otc_video_frame_get_metadata == 0) {
            return null;
        }
        return new d(otc_video_frame_get_metadata, false);
    }

    public static otc_video_frame_format a(SWIGTYPE_p_otc_video_frame sWIGTYPE_p_otc_video_frame) {
        return otc_video_frame_format.a(opentokJNI.otc_video_frame_get_format(SWIGTYPE_p_otc_video_frame.getCPtr(sWIGTYPE_p_otc_video_frame)));
    }

    public static short a(d dVar, int i) {
        return opentokJNI.uint8Array_getitem(d.a(dVar), i);
    }

    public static void a(SWIGTYPE_p_otc_session sWIGTYPE_p_otc_session, h hVar) {
        opentokJNI.otc_session_log_external_device_use(SWIGTYPE_p_otc_session.a(sWIGTYPE_p_otc_session), hVar.a());
    }

    public static String[] a(SWIGTYPE_p_otc_session sWIGTYPE_p_otc_session, int[] iArr) {
        return opentokJNI.otc_session_get_ice_config_credentials(SWIGTYPE_p_otc_session.a(sWIGTYPE_p_otc_session), iArr);
    }

    public static String[] a(int[] iArr) {
        return opentokJNI.otc_media_utils_get_supported_codecs_array(iArr);
    }

    public static int b(SWIGTYPE_p_otc_connection sWIGTYPE_p_otc_connection) {
        return opentokJNI.otc_connection_delete(SWIGTYPE_p_otc_connection.getCPtr(sWIGTYPE_p_otc_connection));
    }

    public static int b(SWIGTYPE_p_otc_publisher sWIGTYPE_p_otc_publisher) {
        return opentokJNI.otc_publisher_get_audio_fallback_enabled(SWIGTYPE_p_otc_publisher.a(sWIGTYPE_p_otc_publisher));
    }

    public static int b(SWIGTYPE_p_otc_publisher sWIGTYPE_p_otc_publisher, int i) {
        return opentokJNI.otc_publisher_set_publish_audio(SWIGTYPE_p_otc_publisher.a(sWIGTYPE_p_otc_publisher), i);
    }

    public static int b(SWIGTYPE_p_otc_session sWIGTYPE_p_otc_session) {
        return opentokJNI.otc_session_disable_force_mute(SWIGTYPE_p_otc_session.a(sWIGTYPE_p_otc_session));
    }

    public static int b(SWIGTYPE_p_otc_session sWIGTYPE_p_otc_session, SWIGTYPE_p_otc_publisher sWIGTYPE_p_otc_publisher) {
        return opentokJNI.otc_session_unpublish(SWIGTYPE_p_otc_session.a(sWIGTYPE_p_otc_session), SWIGTYPE_p_otc_publisher.a(sWIGTYPE_p_otc_publisher));
    }

    public static int b(SWIGTYPE_p_otc_session sWIGTYPE_p_otc_session, SWIGTYPE_p_otc_subscriber sWIGTYPE_p_otc_subscriber) {
        return opentokJNI.otc_session_unsubscribe(SWIGTYPE_p_otc_session.a(sWIGTYPE_p_otc_session), SWIGTYPE_p_otc_subscriber.a(sWIGTYPE_p_otc_subscriber));
    }

    public static int b(SWIGTYPE_p_otc_session sWIGTYPE_p_otc_session, String str, String str2) {
        return opentokJNI.otc_session_send_signal(SWIGTYPE_p_otc_session.a(sWIGTYPE_p_otc_session), str, str2);
    }

    public static int b(SWIGTYPE_p_otc_session sWIGTYPE_p_otc_session, int[] iArr) {
        return opentokJNI.otc_session_get_ice_config_server_count(SWIGTYPE_p_otc_session.a(sWIGTYPE_p_otc_session), iArr);
    }

    public static int b(SWIGTYPE_p_otc_stream sWIGTYPE_p_otc_stream) {
        return opentokJNI.otc_stream_delete(SWIGTYPE_p_otc_stream.getCPtr(sWIGTYPE_p_otc_stream));
    }

    public static int b(SWIGTYPE_p_otc_subscriber sWIGTYPE_p_otc_subscriber) {
        return opentokJNI.otc_subscriber_get_rtc_stats_report(SWIGTYPE_p_otc_subscriber.a(sWIGTYPE_p_otc_subscriber));
    }

    public static int b(SWIGTYPE_p_otc_subscriber sWIGTYPE_p_otc_subscriber, int i) {
        return opentokJNI.otc_subscriber_set_subscribe_to_video(SWIGTYPE_p_otc_subscriber.a(sWIGTYPE_p_otc_subscriber), i);
    }

    public static int b(SWIGTYPE_p_otc_video_frame sWIGTYPE_p_otc_video_frame) {
        return opentokJNI.otc_video_frame_get_height(SWIGTYPE_p_otc_video_frame.getCPtr(sWIGTYPE_p_otc_video_frame));
    }

    public static int b(a aVar, int i) {
        return opentokJNI.otc_publisher_settings_set_opus_dtx(a.a(aVar), i);
    }

    public static int b(b bVar, int i) {
        return opentokJNI.otc_session_settings_set_ip_whitelist(b.a(bVar), i);
    }

    public static a b() {
        long otc_publisher_settings_new = opentokJNI.otc_publisher_settings_new();
        if (otc_publisher_settings_new == 0) {
            return null;
        }
        return new a(otc_publisher_settings_new, false);
    }

    public static String b(SWIGTYPE_p_otc_session sWIGTYPE_p_otc_session, String str) {
        return opentokJNI.otc_session_report_issue_ex(SWIGTYPE_p_otc_session.a(sWIGTYPE_p_otc_session), str);
    }

    public static int c(SWIGTYPE_p_otc_publisher sWIGTYPE_p_otc_publisher, int i) {
        return opentokJNI.otc_publisher_set_publish_video(SWIGTYPE_p_otc_publisher.a(sWIGTYPE_p_otc_publisher), i);
    }

    public static int c(SWIGTYPE_p_otc_session sWIGTYPE_p_otc_session) {
        return opentokJNI.otc_session_disconnect(SWIGTYPE_p_otc_session.a(sWIGTYPE_p_otc_session));
    }

    public static int c(SWIGTYPE_p_otc_session sWIGTYPE_p_otc_session, String str) {
        return opentokJNI.otc_session_set_rtc_stats_reports_file_path(SWIGTYPE_p_otc_session.a(sWIGTYPE_p_otc_session), str);
    }

    public static int c(SWIGTYPE_p_otc_subscriber sWIGTYPE_p_otc_subscriber, int i) {
        return opentokJNI.otc_subscriber_set_video_data_callback_behavior(SWIGTYPE_p_otc_subscriber.a(sWIGTYPE_p_otc_subscriber), i);
    }

    public static int c(SWIGTYPE_p_otc_video_frame sWIGTYPE_p_otc_video_frame) {
        return opentokJNI.otc_video_frame_get_width(SWIGTYPE_p_otc_video_frame.getCPtr(sWIGTYPE_p_otc_video_frame));
    }

    public static int c(a aVar, int i) {
        return opentokJNI.otc_publisher_settings_set_video_track(a.a(aVar), i);
    }

    public static long c(SWIGTYPE_p_otc_connection sWIGTYPE_p_otc_connection) {
        return opentokJNI.otc_connection_get_creation_time(SWIGTYPE_p_otc_connection.getCPtr(sWIGTYPE_p_otc_connection));
    }

    public static SWIGTYPE_p_otc_connection c(SWIGTYPE_p_otc_stream sWIGTYPE_p_otc_stream) {
        long otc_stream_get_connection = opentokJNI.otc_stream_get_connection(SWIGTYPE_p_otc_stream.getCPtr(sWIGTYPE_p_otc_stream));
        if (otc_stream_get_connection == 0) {
            return null;
        }
        return new SWIGTYPE_p_otc_connection(otc_stream_get_connection, false);
    }

    public static SWIGTYPE_p_otc_session c(SWIGTYPE_p_otc_subscriber sWIGTYPE_p_otc_subscriber) {
        long otc_subscriber_get_session = opentokJNI.otc_subscriber_get_session(SWIGTYPE_p_otc_subscriber.a(sWIGTYPE_p_otc_subscriber));
        if (otc_subscriber_get_session == 0) {
            return null;
        }
        return new SWIGTYPE_p_otc_session(otc_subscriber_get_session, false);
    }

    public static b c() {
        long otc_session_settings_new = opentokJNI.otc_session_settings_new();
        if (otc_session_settings_new == 0) {
            return null;
        }
        return new b(otc_session_settings_new, false);
    }

    public static String c(SWIGTYPE_p_otc_publisher sWIGTYPE_p_otc_publisher) {
        return opentokJNI.otc_publisher_get_name(SWIGTYPE_p_otc_publisher.a(sWIGTYPE_p_otc_publisher));
    }

    public static String[] c(SWIGTYPE_p_otc_session sWIGTYPE_p_otc_session, int[] iArr) {
        return opentokJNI.otc_session_get_ice_config_server_urls(SWIGTYPE_p_otc_session.a(sWIGTYPE_p_otc_session), iArr);
    }

    public static int d(SWIGTYPE_p_otc_publisher sWIGTYPE_p_otc_publisher) {
        return opentokJNI.otc_publisher_get_publish_audio(SWIGTYPE_p_otc_publisher.a(sWIGTYPE_p_otc_publisher));
    }

    public static long d(SWIGTYPE_p_otc_stream sWIGTYPE_p_otc_stream) {
        return opentokJNI.otc_stream_get_creation_time(SWIGTYPE_p_otc_stream.getCPtr(sWIGTYPE_p_otc_stream));
    }

    public static SWIGTYPE_p_otc_stream d(SWIGTYPE_p_otc_subscriber sWIGTYPE_p_otc_subscriber) {
        long otc_subscriber_get_stream = opentokJNI.otc_subscriber_get_stream(SWIGTYPE_p_otc_subscriber.a(sWIGTYPE_p_otc_subscriber));
        if (otc_subscriber_get_stream == 0) {
            return null;
        }
        return new SWIGTYPE_p_otc_stream(otc_subscriber_get_stream, false);
    }

    public static otc_session_capabilities d(SWIGTYPE_p_otc_session sWIGTYPE_p_otc_session) {
        return new otc_session_capabilities(opentokJNI.otc_session_get_capabilities(SWIGTYPE_p_otc_session.a(sWIGTYPE_p_otc_session)), true);
    }

    public static String d(SWIGTYPE_p_otc_connection sWIGTYPE_p_otc_connection) {
        return opentokJNI.otc_connection_get_data(SWIGTYPE_p_otc_connection.getCPtr(sWIGTYPE_p_otc_connection));
    }

    public static String[] d(SWIGTYPE_p_otc_session sWIGTYPE_p_otc_session, int[] iArr) {
        return opentokJNI.otc_session_get_ice_config_users(SWIGTYPE_p_otc_session.a(sWIGTYPE_p_otc_session), iArr);
    }

    public static int e(SWIGTYPE_p_otc_publisher sWIGTYPE_p_otc_publisher) {
        return opentokJNI.otc_publisher_get_publish_video(SWIGTYPE_p_otc_publisher.a(sWIGTYPE_p_otc_publisher));
    }

    public static int e(SWIGTYPE_p_otc_subscriber sWIGTYPE_p_otc_subscriber) {
        return opentokJNI.otc_subscriber_get_subscribe_to_audio(SWIGTYPE_p_otc_subscriber.a(sWIGTYPE_p_otc_subscriber));
    }

    public static SWIGTYPE_p_otc_connection e(SWIGTYPE_p_otc_session sWIGTYPE_p_otc_session) {
        long otc_session_get_connection = opentokJNI.otc_session_get_connection(SWIGTYPE_p_otc_session.a(sWIGTYPE_p_otc_session));
        if (otc_session_get_connection == 0) {
            return null;
        }
        return new SWIGTYPE_p_otc_connection(otc_session_get_connection, false);
    }

    public static String e(SWIGTYPE_p_otc_connection sWIGTYPE_p_otc_connection) {
        return opentokJNI.otc_connection_get_id(SWIGTYPE_p_otc_connection.getCPtr(sWIGTYPE_p_otc_connection));
    }

    public static String e(SWIGTYPE_p_otc_stream sWIGTYPE_p_otc_stream) {
        return opentokJNI.otc_stream_get_id(SWIGTYPE_p_otc_stream.getCPtr(sWIGTYPE_p_otc_stream));
    }

    public static int f(SWIGTYPE_p_otc_publisher sWIGTYPE_p_otc_publisher) {
        return opentokJNI.otc_publisher_get_rtc_stats_report(SWIGTYPE_p_otc_publisher.a(sWIGTYPE_p_otc_publisher));
    }

    public static int f(SWIGTYPE_p_otc_subscriber sWIGTYPE_p_otc_subscriber) {
        return opentokJNI.otc_subscriber_get_subscribe_to_video(SWIGTYPE_p_otc_subscriber.a(sWIGTYPE_p_otc_subscriber));
    }

    public static String f(SWIGTYPE_p_otc_session sWIGTYPE_p_otc_session) {
        return opentokJNI.otc_session_get_id(SWIGTYPE_p_otc_session.a(sWIGTYPE_p_otc_session));
    }

    public static String f(SWIGTYPE_p_otc_stream sWIGTYPE_p_otc_stream) {
        return opentokJNI.otc_stream_get_name(SWIGTYPE_p_otc_stream.getCPtr(sWIGTYPE_p_otc_stream));
    }

    public static int g(SWIGTYPE_p_otc_session sWIGTYPE_p_otc_session) {
        return opentokJNI.otc_session_reconnect(SWIGTYPE_p_otc_session.a(sWIGTYPE_p_otc_session));
    }

    public static int g(SWIGTYPE_p_otc_stream sWIGTYPE_p_otc_stream) {
        return opentokJNI.otc_stream_get_video_height(SWIGTYPE_p_otc_stream.getCPtr(sWIGTYPE_p_otc_stream));
    }

    public static SWIGTYPE_p_otc_session g(SWIGTYPE_p_otc_publisher sWIGTYPE_p_otc_publisher) {
        long otc_publisher_get_session = opentokJNI.otc_publisher_get_session(SWIGTYPE_p_otc_publisher.a(sWIGTYPE_p_otc_publisher));
        if (otc_publisher_get_session == 0) {
            return null;
        }
        return new SWIGTYPE_p_otc_session(otc_publisher_get_session, false);
    }

    public static SWIGTYPE_p_otc_stream h(SWIGTYPE_p_otc_publisher sWIGTYPE_p_otc_publisher) {
        long otc_publisher_get_stream = opentokJNI.otc_publisher_get_stream(SWIGTYPE_p_otc_publisher.a(sWIGTYPE_p_otc_publisher));
        if (otc_publisher_get_stream == 0) {
            return null;
        }
        return new SWIGTYPE_p_otc_stream(otc_publisher_get_stream, false);
    }

    public static otc_stream_video_type h(SWIGTYPE_p_otc_stream sWIGTYPE_p_otc_stream) {
        return otc_stream_video_type.a(opentokJNI.otc_stream_get_video_type(SWIGTYPE_p_otc_stream.getCPtr(sWIGTYPE_p_otc_stream)));
    }

    public static int i(SWIGTYPE_p_otc_stream sWIGTYPE_p_otc_stream) {
        return opentokJNI.otc_stream_get_video_width(SWIGTYPE_p_otc_stream.getCPtr(sWIGTYPE_p_otc_stream));
    }

    public static i i(SWIGTYPE_p_otc_publisher sWIGTYPE_p_otc_publisher) {
        return i.a(opentokJNI.otc_publisher_get_video_type(SWIGTYPE_p_otc_publisher.a(sWIGTYPE_p_otc_publisher)));
    }

    public static int j(SWIGTYPE_p_otc_stream sWIGTYPE_p_otc_stream) {
        return opentokJNI.otc_stream_has_audio(SWIGTYPE_p_otc_stream.getCPtr(sWIGTYPE_p_otc_stream));
    }

    public static int k(SWIGTYPE_p_otc_stream sWIGTYPE_p_otc_stream) {
        return opentokJNI.otc_stream_has_video(SWIGTYPE_p_otc_stream.getCPtr(sWIGTYPE_p_otc_stream));
    }
}
