package com.opentok.otc;

/* loaded from: classes.dex */
public class opentokJNI {
    public static final native int IS_LINUX_get();

    public static final native int IS_MAC_OS_get();

    public static final native int OTC_FALSE_get();

    public static final native int OTC_PUBLISHER_VIDEO_TYPE_CAMERA_get();

    public static final native int OTC_PUBLISHER_VIDEO_TYPE_SCREEN_get();

    public static final native String OTC_STATS_KEY_ACTIVECONNECTION_get();

    public static final native String OTC_STATS_KEY_ACTUALENCBITRATE_get();

    public static final native String OTC_STATS_KEY_AUDIOINPUTLEVEL_get();

    public static final native String OTC_STATS_KEY_AUDIOOUTPUTLEVEL_get();

    public static final native String OTC_STATS_KEY_AVAILABLERECEIVEBANDWIDTH_get();

    public static final native String OTC_STATS_KEY_AVAILABLESENDBANDWIDTH_get();

    public static final native String OTC_STATS_KEY_BUCKETDELAY_get();

    public static final native String OTC_STATS_KEY_BYTESRECEIVED_get();

    public static final native String OTC_STATS_KEY_BYTESSENT_get();

    public static final native String OTC_STATS_KEY_CANDIDATEPAIR_get();

    public static final native String OTC_STATS_KEY_CHANNELID_get();

    public static final native String OTC_STATS_KEY_CODECNAME_get();

    public static final native String OTC_STATS_KEY_COMPONENT_get();

    public static final native String OTC_STATS_KEY_CONTENTNAME_get();

    public static final native String OTC_STATS_KEY_ECHOCANCELLATIONQUALITYMIN_get();

    public static final native String OTC_STATS_KEY_ECHODELAYMEDIAN_get();

    public static final native String OTC_STATS_KEY_ECHODELAYSTDDEV_get();

    public static final native String OTC_STATS_KEY_ECHORETURNLOSSENHANCEMENT_get();

    public static final native String OTC_STATS_KEY_ECHORETURNLOSS_get();

    public static final native String OTC_STATS_KEY_FIRSRECEIVED_get();

    public static final native String OTC_STATS_KEY_FIRSSENT_get();

    public static final native String OTC_STATS_KEY_FRAMEHEIGHTRECEIVED_get();

    public static final native String OTC_STATS_KEY_FRAMEHEIGHTSENT_get();

    public static final native String OTC_STATS_KEY_FRAMERATEDECODED_get();

    public static final native String OTC_STATS_KEY_FRAMERATEINPUT_get();

    public static final native String OTC_STATS_KEY_FRAMERATEOUTPUT_get();

    public static final native String OTC_STATS_KEY_FRAMERATERECEIVED_get();

    public static final native String OTC_STATS_KEY_FRAMERATESENT_get();

    public static final native String OTC_STATS_KEY_FRAMEWIDTHRECEIVED_get();

    public static final native String OTC_STATS_KEY_FRAMEWIDTHSENT_get();

    public static final native String OTC_STATS_KEY_INITIATOR_get();

    public static final native String OTC_STATS_KEY_JITTERRECEIVED_get();

    public static final native String OTC_STATS_KEY_LOCALADDRESS_get();

    public static final native String OTC_STATS_KEY_LOCALCANDIDATEPRIORITY_get();

    public static final native String OTC_STATS_KEY_LOCALCANDIDATETYPE_get();

    public static final native String OTC_STATS_KEY_LOCALRELAYPROTOCOL_get();

    public static final native String OTC_STATS_KEY_NACKSRECEIVED_get();

    public static final native String OTC_STATS_KEY_NACKSSENT_get();

    public static final native String OTC_STATS_KEY_PACKETSLOST_get();

    public static final native String OTC_STATS_KEY_PACKETSRECEIVED_get();

    public static final native String OTC_STATS_KEY_PACKETSSENT_get();

    public static final native String OTC_STATS_KEY_READABLE_get();

    public static final native String OTC_STATS_KEY_REMOTEADDRESS_get();

    public static final native String OTC_STATS_KEY_REMOTECANDIDATETYPE_get();

    public static final native String OTC_STATS_KEY_REMOTERELAYPROTOCOL_get();

    public static final native String OTC_STATS_KEY_RETRANSMITBITRATE_get();

    public static final native String OTC_STATS_KEY_RTT_get();

    public static final native String OTC_STATS_KEY_SSRC_get();

    public static final native String OTC_STATS_KEY_TARGETENCBITRATE_get();

    public static final native String OTC_STATS_KEY_TRACKID_get();

    public static final native String OTC_STATS_KEY_TRANSMITBITRATE_get();

    public static final native String OTC_STATS_KEY_TRANSPORTID_get();

    public static final native String OTC_STATS_KEY_TRANSPORTTYPE_get();

    public static final native String OTC_STATS_KEY_WRITABLE_get();

    public static final native int OTC_STREAM_VIDEO_TYPE_CAMERA_get();

    public static final native int OTC_STREAM_VIDEO_TYPE_CUSTOM_get();

    public static final native int OTC_STREAM_VIDEO_TYPE_SCREEN_get();

    public static final native int OTC_SUBSCRIBER_VIDEO_DATA_EVERY_FRAME_get();

    public static final native int OTC_SUCCESS_get();

    public static final native int OTC_TRUE_get();

    public static final native int OTC_VIDEO_FRAME_FORMAT_ABGR32_get();

    public static final native int OTC_VIDEO_FRAME_FORMAT_ARGB32_get();

    public static final native int OTC_VIDEO_FRAME_FORMAT_BGRA32_get();

    public static final native int OTC_VIDEO_FRAME_FORMAT_COMPRESSED_get();

    public static final native int OTC_VIDEO_FRAME_FORMAT_MJPEG_get();

    public static final native int OTC_VIDEO_FRAME_FORMAT_NV12_get();

    public static final native int OTC_VIDEO_FRAME_FORMAT_NV21_get();

    public static final native int OTC_VIDEO_FRAME_FORMAT_RGB24_get();

    public static final native int OTC_VIDEO_FRAME_FORMAT_RGBA32_get();

    public static final native int OTC_VIDEO_FRAME_FORMAT_UNKNOWN_get();

    public static final native int OTC_VIDEO_FRAME_FORMAT_UYVY_get();

    public static final native int OTC_VIDEO_FRAME_FORMAT_YUV420P_get();

    public static final native int OTC_VIDEO_FRAME_FORMAT_YUY2_get();

    public static final native int OTC_VIDEO_FRAME_METADATA_MAX_SIZE_get();

    public static final native int OTC_VIDEO_FRAME_PLANE_PACKED_get();

    public static final native int OTC_VIDEO_FRAME_PLANE_UV_INTERLEAVED_get();

    public static final native int OTC_VIDEO_FRAME_PLANE_U_get();

    public static final native int OTC_VIDEO_FRAME_PLANE_VU_INTERLEAVED_get();

    public static final native int OTC_VIDEO_FRAME_PLANE_V_get();

    public static final native int OTC_VIDEO_FRAME_PLANE_Y_get();

    public static final native void delete_otc_audio_device_settings(long j);

    public static final native void delete_otc_on_mute_forced_info(long j);

    public static final native void delete_otc_publisher_callbacks(long j);

    public static final native void delete_otc_publisher_rtc_stats_report_cb(long j);

    public static final native void delete_otc_session_callbacks(long j);

    public static final native void delete_otc_session_capabilities(long j);

    public static final native void delete_otc_signal_options(long j);

    public static final native void delete_otc_subscriber_callbacks(long j);

    public static final native void delete_otc_subscriber_rtc_stats_report_cb(long j);

    public static final native void delete_otc_video_capturer_callbacks(long j);

    public static final native void delete_otc_video_capturer_settings(long j);

    public static final native long new_otc_audio_device_settings();

    public static final native long new_otc_on_mute_forced_info();

    public static final native long new_otc_publisher_callbacks();

    public static final native long new_otc_publisher_rtc_stats_report_cb();

    public static final native long new_otc_session_callbacks();

    public static final native long new_otc_session_capabilities();

    public static final native long new_otc_signal_options();

    public static final native long new_otc_subscriber_callbacks();

    public static final native long new_otc_subscriber_rtc_stats_report_cb();

    public static final native long new_otc_video_capturer_callbacks();

    public static final native long new_otc_video_capturer_settings();

    public static final native long new_sizetPointer();

    public static final native int otc_audio_device_settings_number_of_channels_get(long j, otc_audio_device_settings otc_audio_device_settingsVar);

    public static final native void otc_audio_device_settings_number_of_channels_set(long j, otc_audio_device_settings otc_audio_device_settingsVar, int i);

    public static final native int otc_audio_device_settings_sampling_rate_get(long j, otc_audio_device_settings otc_audio_device_settingsVar);

    public static final native void otc_audio_device_settings_sampling_rate_set(long j, otc_audio_device_settings otc_audio_device_settingsVar, int i);

    public static final native long otc_connection_copy(long j);

    public static final native int otc_connection_delete(long j);

    public static final native long otc_connection_get_creation_time(long j);

    public static final native String otc_connection_get_data(long j);

    public static final native String otc_connection_get_id(long j);

    public static final native int otc_external_audio_get();

    public static final native int otc_external_video_get();

    public static final native int otc_internal_audio_get();

    public static final native int otc_internal_video_get();

    public static final native String[] otc_media_utils_get_supported_codecs_array(int[] iArr);

    public static final native int otc_on_mute_forced_info_active_get(long j, otc_on_mute_forced_info otc_on_mute_forced_infoVar);

    public static final native void otc_on_mute_forced_info_active_set(long j, otc_on_mute_forced_info otc_on_mute_forced_infoVar, int i);

    public static final native long otc_publisher_callbacks_on_audio_level_updated_get(long j, otc_publisher_callbacks otc_publisher_callbacksVar);

    public static final native void otc_publisher_callbacks_on_audio_level_updated_set(long j, otc_publisher_callbacks otc_publisher_callbacksVar, long j2);

    public static final native long otc_publisher_callbacks_on_audio_stats_get(long j, otc_publisher_callbacks otc_publisher_callbacksVar);

    public static final native void otc_publisher_callbacks_on_audio_stats_set(long j, otc_publisher_callbacks otc_publisher_callbacksVar, long j2);

    public static final native long otc_publisher_callbacks_on_error_get(long j, otc_publisher_callbacks otc_publisher_callbacksVar);

    public static final native void otc_publisher_callbacks_on_error_set(long j, otc_publisher_callbacks otc_publisher_callbacksVar, long j2);

    public static final native long otc_publisher_callbacks_on_publisher_mute_forced_get(long j, otc_publisher_callbacks otc_publisher_callbacksVar);

    public static final native void otc_publisher_callbacks_on_publisher_mute_forced_set(long j, otc_publisher_callbacks otc_publisher_callbacksVar, long j2);

    public static final native long otc_publisher_callbacks_on_render_frame_get(long j, otc_publisher_callbacks otc_publisher_callbacksVar);

    public static final native void otc_publisher_callbacks_on_render_frame_set(long j, otc_publisher_callbacks otc_publisher_callbacksVar, long j2);

    public static final native long otc_publisher_callbacks_on_stream_created_get(long j, otc_publisher_callbacks otc_publisher_callbacksVar);

    public static final native void otc_publisher_callbacks_on_stream_created_set(long j, otc_publisher_callbacks otc_publisher_callbacksVar, long j2);

    public static final native long otc_publisher_callbacks_on_stream_destroyed_get(long j, otc_publisher_callbacks otc_publisher_callbacksVar);

    public static final native void otc_publisher_callbacks_on_stream_destroyed_set(long j, otc_publisher_callbacks otc_publisher_callbacksVar, long j2);

    public static final native long otc_publisher_callbacks_on_video_stats_get(long j, otc_publisher_callbacks otc_publisher_callbacksVar);

    public static final native void otc_publisher_callbacks_on_video_stats_set(long j, otc_publisher_callbacks otc_publisher_callbacksVar, long j2);

    public static final native long otc_publisher_callbacks_reserved_get(long j, otc_publisher_callbacks otc_publisher_callbacksVar);

    public static final native void otc_publisher_callbacks_reserved_set(long j, otc_publisher_callbacks otc_publisher_callbacksVar, long j2);

    public static final native long otc_publisher_callbacks_user_data_get(long j, otc_publisher_callbacks otc_publisher_callbacksVar);

    public static final native void otc_publisher_callbacks_user_data_set(long j, otc_publisher_callbacks otc_publisher_callbacksVar, long j2);

    public static final native int otc_publisher_delete(long j);

    public static final native int otc_publisher_get_audio_fallback_enabled(long j);

    public static final native String otc_publisher_get_name(long j);

    public static final native int otc_publisher_get_publish_audio(long j);

    public static final native int otc_publisher_get_publish_video(long j);

    public static final native int otc_publisher_get_rtc_stats_report(long j);

    public static final native long otc_publisher_get_session(long j);

    public static final native long otc_publisher_get_stream(long j);

    public static final native int otc_publisher_get_video_type(long j);

    public static final native long otc_publisher_new_with_settings(long j, otc_publisher_callbacks otc_publisher_callbacksVar, long j2);

    public static final native long otc_publisher_rtc_stats_report_cb_on_rtc_stats_report_get(long j, otc_publisher_rtc_stats_report_cb otc_publisher_rtc_stats_report_cbVar);

    public static final native void otc_publisher_rtc_stats_report_cb_on_rtc_stats_report_set(long j, otc_publisher_rtc_stats_report_cb otc_publisher_rtc_stats_report_cbVar, long j2);

    public static final native long otc_publisher_rtc_stats_report_cb_reserved_get(long j, otc_publisher_rtc_stats_report_cb otc_publisher_rtc_stats_report_cbVar);

    public static final native void otc_publisher_rtc_stats_report_cb_reserved_set(long j, otc_publisher_rtc_stats_report_cb otc_publisher_rtc_stats_report_cbVar, long j2);

    public static final native long otc_publisher_rtc_stats_report_cb_user_data_get(long j, otc_publisher_rtc_stats_report_cb otc_publisher_rtc_stats_report_cbVar);

    public static final native void otc_publisher_rtc_stats_report_cb_user_data_set(long j, otc_publisher_rtc_stats_report_cb otc_publisher_rtc_stats_report_cbVar, long j2);

    public static final native int otc_publisher_set_audio_fallback_enabled(long j, int i);

    public static final native int otc_publisher_set_max_audio_bitrate(long j, long j2);

    public static final native int otc_publisher_set_publish_audio(long j, int i);

    public static final native int otc_publisher_set_publish_video(long j, int i);

    public static final native int otc_publisher_set_rtc_stats_report_cb(long j, long j2, otc_publisher_rtc_stats_report_cb otc_publisher_rtc_stats_report_cbVar);

    public static final native int otc_publisher_set_video_type(long j, int i);

    public static final native int otc_publisher_settings_delete(long j);

    public static final native long otc_publisher_settings_new();

    public static final native int otc_publisher_settings_set_audio_track(long j, int i);

    public static final native int otc_publisher_settings_set_name(long j, String str);

    public static final native int otc_publisher_settings_set_opus_dtx(long j, int i);

    public static final native int otc_publisher_settings_set_video_capturer(long j, long j2, otc_video_capturer_callbacks otc_video_capturer_callbacksVar);

    public static final native int otc_publisher_settings_set_video_track(long j, int i);

    public static final native long otc_session_callbacks_on_archive_started_get(long j, otc_session_callbacks otc_session_callbacksVar);

    public static final native void otc_session_callbacks_on_archive_started_set(long j, otc_session_callbacks otc_session_callbacksVar, long j2);

    public static final native long otc_session_callbacks_on_archive_stopped_get(long j, otc_session_callbacks otc_session_callbacksVar);

    public static final native void otc_session_callbacks_on_archive_stopped_set(long j, otc_session_callbacks otc_session_callbacksVar, long j2);

    public static final native long otc_session_callbacks_on_connected_get(long j, otc_session_callbacks otc_session_callbacksVar);

    public static final native void otc_session_callbacks_on_connected_set(long j, otc_session_callbacks otc_session_callbacksVar, long j2);

    public static final native long otc_session_callbacks_on_connection_created_get(long j, otc_session_callbacks otc_session_callbacksVar);

    public static final native void otc_session_callbacks_on_connection_created_set(long j, otc_session_callbacks otc_session_callbacksVar, long j2);

    public static final native long otc_session_callbacks_on_connection_dropped_get(long j, otc_session_callbacks otc_session_callbacksVar);

    public static final native void otc_session_callbacks_on_connection_dropped_set(long j, otc_session_callbacks otc_session_callbacksVar, long j2);

    public static final native long otc_session_callbacks_on_disconnected_get(long j, otc_session_callbacks otc_session_callbacksVar);

    public static final native void otc_session_callbacks_on_disconnected_set(long j, otc_session_callbacks otc_session_callbacksVar, long j2);

    public static final native long otc_session_callbacks_on_error_get(long j, otc_session_callbacks otc_session_callbacksVar);

    public static final native void otc_session_callbacks_on_error_set(long j, otc_session_callbacks otc_session_callbacksVar, long j2);

    public static final native long otc_session_callbacks_on_mute_forced_get(long j, otc_session_callbacks otc_session_callbacksVar);

    public static final native void otc_session_callbacks_on_mute_forced_set(long j, otc_session_callbacks otc_session_callbacksVar, long j2);

    public static final native long otc_session_callbacks_on_reconnected_get(long j, otc_session_callbacks otc_session_callbacksVar);

    public static final native void otc_session_callbacks_on_reconnected_set(long j, otc_session_callbacks otc_session_callbacksVar, long j2);

    public static final native long otc_session_callbacks_on_reconnection_started_get(long j, otc_session_callbacks otc_session_callbacksVar);

    public static final native void otc_session_callbacks_on_reconnection_started_set(long j, otc_session_callbacks otc_session_callbacksVar, long j2);

    public static final native long otc_session_callbacks_on_signal_received_get(long j, otc_session_callbacks otc_session_callbacksVar);

    public static final native void otc_session_callbacks_on_signal_received_set(long j, otc_session_callbacks otc_session_callbacksVar, long j2);

    public static final native long otc_session_callbacks_on_stream_dropped_get(long j, otc_session_callbacks otc_session_callbacksVar);

    public static final native void otc_session_callbacks_on_stream_dropped_set(long j, otc_session_callbacks otc_session_callbacksVar, long j2);

    public static final native long otc_session_callbacks_on_stream_has_audio_changed_get(long j, otc_session_callbacks otc_session_callbacksVar);

    public static final native void otc_session_callbacks_on_stream_has_audio_changed_set(long j, otc_session_callbacks otc_session_callbacksVar, long j2);

    public static final native long otc_session_callbacks_on_stream_has_video_changed_get(long j, otc_session_callbacks otc_session_callbacksVar);

    public static final native void otc_session_callbacks_on_stream_has_video_changed_set(long j, otc_session_callbacks otc_session_callbacksVar, long j2);

    public static final native long otc_session_callbacks_on_stream_received_get(long j, otc_session_callbacks otc_session_callbacksVar);

    public static final native void otc_session_callbacks_on_stream_received_set(long j, otc_session_callbacks otc_session_callbacksVar, long j2);

    public static final native long otc_session_callbacks_on_stream_video_dimensions_changed_get(long j, otc_session_callbacks otc_session_callbacksVar);

    public static final native void otc_session_callbacks_on_stream_video_dimensions_changed_set(long j, otc_session_callbacks otc_session_callbacksVar, long j2);

    public static final native long otc_session_callbacks_on_stream_video_type_changed_get(long j, otc_session_callbacks otc_session_callbacksVar);

    public static final native void otc_session_callbacks_on_stream_video_type_changed_set(long j, otc_session_callbacks otc_session_callbacksVar, long j2);

    public static final native long otc_session_callbacks_reserved_get(long j, otc_session_callbacks otc_session_callbacksVar);

    public static final native void otc_session_callbacks_reserved_set(long j, otc_session_callbacks otc_session_callbacksVar, long j2);

    public static final native long otc_session_callbacks_user_data_get(long j, otc_session_callbacks otc_session_callbacksVar);

    public static final native void otc_session_callbacks_user_data_set(long j, otc_session_callbacks otc_session_callbacksVar, long j2);

    public static final native int otc_session_capabilities_force_mute_get(long j, otc_session_capabilities otc_session_capabilitiesVar);

    public static final native int otc_session_capabilities_publish_get(long j, otc_session_capabilities otc_session_capabilitiesVar);

    public static final native int otc_session_connect_to_host(long j, String str, String str2, int i, int i2, String str3);

    public static final native int otc_session_delete(long j);

    public static final native int otc_session_disable_force_mute(long j);

    public static final native int otc_session_disconnect(long j);

    public static final native int otc_session_force_mute_all(long j, String[] strArr, int i);

    public static final native int otc_session_force_mute_stream(long j, String str);

    public static final native long otc_session_get_capabilities(long j);

    public static final native long otc_session_get_connection(long j);

    public static final native String[] otc_session_get_ice_config_credentials(long j, int[] iArr);

    public static final native int otc_session_get_ice_config_server_count(long j, int[] iArr);

    public static final native String[] otc_session_get_ice_config_server_urls(long j, int[] iArr);

    public static final native String[] otc_session_get_ice_config_users(long j, int[] iArr);

    public static final native String otc_session_get_id(long j);

    public static final native int otc_session_log_custom_client_event(long j, String str, String str2);

    public static final native void otc_session_log_external_device_use(long j, int i);

    public static final native long otc_session_new_with_settings(String str, String str2, long j, otc_session_callbacks otc_session_callbacksVar, long j2);

    public static final native int otc_session_publish(long j, long j2);

    public static final native int otc_session_reconnect(long j);

    public static final native String otc_session_report_issue_ex(long j, String str);

    public static final native int otc_session_send_signal(long j, String str, String str2);

    public static final native int otc_session_send_signal_to_connection(long j, String str, String str2, long j2);

    public static final native int otc_session_send_signal_to_connection_with_options(long j, String str, String str2, long j2, long j3, otc_signal_options otc_signal_optionsVar);

    public static final native int otc_session_send_signal_with_options(long j, String str, String str2, long j2, otc_signal_options otc_signal_optionsVar);

    public static final native int otc_session_set_rtc_stats_reports_file_path(long j, String str);

    public static final native int otc_session_settings_delete(long j);

    public static final native long otc_session_settings_new();

    public static final native int otc_session_settings_set_connection_events_suppressed(long j, int i);

    public static final native int otc_session_settings_set_custom_ice_config_no_struct(long j, int i, String[] strArr, String[] strArr2, String[] strArr3, int i2, int i3);

    public static final native int otc_session_settings_set_ip_whitelist(long j, int i);

    public static final native int otc_session_settings_set_proxy_url(long j, String str);

    public static final native int otc_session_subscribe(long j, long j2);

    public static final native int otc_session_unpublish(long j, long j2);

    public static final native int otc_session_unsubscribe(long j, long j2);

    public static final native void otc_signal_options_retry_after_reconnect_set(long j, otc_signal_options otc_signal_optionsVar, int i);

    public static final native long otc_stream_copy(long j);

    public static final native int otc_stream_delete(long j);

    public static final native long otc_stream_get_connection(long j);

    public static final native long otc_stream_get_creation_time(long j);

    public static final native String otc_stream_get_id(long j);

    public static final native String otc_stream_get_name(long j);

    public static final native int otc_stream_get_video_height(long j);

    public static final native int otc_stream_get_video_type(long j);

    public static final native int otc_stream_get_video_width(long j);

    public static final native int otc_stream_has_audio(long j);

    public static final native int otc_stream_has_video(long j);

    public static final native long otc_subscriber_callbacks_on_audio_disabled_get(long j, otc_subscriber_callbacks otc_subscriber_callbacksVar);

    public static final native void otc_subscriber_callbacks_on_audio_disabled_set(long j, otc_subscriber_callbacks otc_subscriber_callbacksVar, long j2);

    public static final native long otc_subscriber_callbacks_on_audio_enabled_get(long j, otc_subscriber_callbacks otc_subscriber_callbacksVar);

    public static final native void otc_subscriber_callbacks_on_audio_enabled_set(long j, otc_subscriber_callbacks otc_subscriber_callbacksVar, long j2);

    public static final native long otc_subscriber_callbacks_on_audio_level_updated_get(long j, otc_subscriber_callbacks otc_subscriber_callbacksVar);

    public static final native void otc_subscriber_callbacks_on_audio_level_updated_set(long j, otc_subscriber_callbacks otc_subscriber_callbacksVar, long j2);

    public static final native long otc_subscriber_callbacks_on_audio_stats_get(long j, otc_subscriber_callbacks otc_subscriber_callbacksVar);

    public static final native void otc_subscriber_callbacks_on_audio_stats_set(long j, otc_subscriber_callbacks otc_subscriber_callbacksVar, long j2);

    public static final native long otc_subscriber_callbacks_on_connected_get(long j, otc_subscriber_callbacks otc_subscriber_callbacksVar);

    public static final native void otc_subscriber_callbacks_on_connected_set(long j, otc_subscriber_callbacks otc_subscriber_callbacksVar, long j2);

    public static final native long otc_subscriber_callbacks_on_disconnected_get(long j, otc_subscriber_callbacks otc_subscriber_callbacksVar);

    public static final native void otc_subscriber_callbacks_on_disconnected_set(long j, otc_subscriber_callbacks otc_subscriber_callbacksVar, long j2);

    public static final native long otc_subscriber_callbacks_on_error_get(long j, otc_subscriber_callbacks otc_subscriber_callbacksVar);

    public static final native void otc_subscriber_callbacks_on_error_set(long j, otc_subscriber_callbacks otc_subscriber_callbacksVar, long j2);

    public static final native long otc_subscriber_callbacks_on_reconnected_get(long j, otc_subscriber_callbacks otc_subscriber_callbacksVar);

    public static final native void otc_subscriber_callbacks_on_reconnected_set(long j, otc_subscriber_callbacks otc_subscriber_callbacksVar, long j2);

    public static final native long otc_subscriber_callbacks_on_render_frame_get(long j, otc_subscriber_callbacks otc_subscriber_callbacksVar);

    public static final native void otc_subscriber_callbacks_on_render_frame_set(long j, otc_subscriber_callbacks otc_subscriber_callbacksVar, long j2);

    public static final native long otc_subscriber_callbacks_on_video_data_received_get(long j, otc_subscriber_callbacks otc_subscriber_callbacksVar);

    public static final native void otc_subscriber_callbacks_on_video_data_received_set(long j, otc_subscriber_callbacks otc_subscriber_callbacksVar, long j2);

    public static final native long otc_subscriber_callbacks_on_video_disable_warning_get(long j, otc_subscriber_callbacks otc_subscriber_callbacksVar);

    public static final native long otc_subscriber_callbacks_on_video_disable_warning_lifted_get(long j, otc_subscriber_callbacks otc_subscriber_callbacksVar);

    public static final native void otc_subscriber_callbacks_on_video_disable_warning_lifted_set(long j, otc_subscriber_callbacks otc_subscriber_callbacksVar, long j2);

    public static final native void otc_subscriber_callbacks_on_video_disable_warning_set(long j, otc_subscriber_callbacks otc_subscriber_callbacksVar, long j2);

    public static final native long otc_subscriber_callbacks_on_video_disabled_get(long j, otc_subscriber_callbacks otc_subscriber_callbacksVar);

    public static final native void otc_subscriber_callbacks_on_video_disabled_set(long j, otc_subscriber_callbacks otc_subscriber_callbacksVar, long j2);

    public static final native long otc_subscriber_callbacks_on_video_enabled_get(long j, otc_subscriber_callbacks otc_subscriber_callbacksVar);

    public static final native void otc_subscriber_callbacks_on_video_enabled_set(long j, otc_subscriber_callbacks otc_subscriber_callbacksVar, long j2);

    public static final native long otc_subscriber_callbacks_on_video_stats_get(long j, otc_subscriber_callbacks otc_subscriber_callbacksVar);

    public static final native void otc_subscriber_callbacks_on_video_stats_set(long j, otc_subscriber_callbacks otc_subscriber_callbacksVar, long j2);

    public static final native long otc_subscriber_callbacks_reserved_get(long j, otc_subscriber_callbacks otc_subscriber_callbacksVar);

    public static final native void otc_subscriber_callbacks_reserved_set(long j, otc_subscriber_callbacks otc_subscriber_callbacksVar, long j2);

    public static final native long otc_subscriber_callbacks_user_data_get(long j, otc_subscriber_callbacks otc_subscriber_callbacksVar);

    public static final native void otc_subscriber_callbacks_user_data_set(long j, otc_subscriber_callbacks otc_subscriber_callbacksVar, long j2);

    public static final native int otc_subscriber_delete(long j);

    public static final native int otc_subscriber_get_preferred_framerate(long j, float[] fArr);

    public static final native int otc_subscriber_get_preferred_resolution(long j, long[] jArr, long[] jArr2);

    public static final native int otc_subscriber_get_rtc_stats_report(long j);

    public static final native long otc_subscriber_get_session(long j);

    public static final native long otc_subscriber_get_stream(long j);

    public static final native int otc_subscriber_get_subscribe_to_audio(long j);

    public static final native int otc_subscriber_get_subscribe_to_video(long j);

    public static final native long otc_subscriber_new(long j, long j2, otc_subscriber_callbacks otc_subscriber_callbacksVar);

    public static final native long otc_subscriber_rtc_stats_report_cb_on_rtc_stats_report_get(long j, otc_subscriber_rtc_stats_report_cb otc_subscriber_rtc_stats_report_cbVar);

    public static final native void otc_subscriber_rtc_stats_report_cb_on_rtc_stats_report_set(long j, otc_subscriber_rtc_stats_report_cb otc_subscriber_rtc_stats_report_cbVar, long j2);

    public static final native long otc_subscriber_rtc_stats_report_cb_reserved_get(long j, otc_subscriber_rtc_stats_report_cb otc_subscriber_rtc_stats_report_cbVar);

    public static final native void otc_subscriber_rtc_stats_report_cb_reserved_set(long j, otc_subscriber_rtc_stats_report_cb otc_subscriber_rtc_stats_report_cbVar, long j2);

    public static final native long otc_subscriber_rtc_stats_report_cb_user_data_get(long j, otc_subscriber_rtc_stats_report_cb otc_subscriber_rtc_stats_report_cbVar);

    public static final native void otc_subscriber_rtc_stats_report_cb_user_data_set(long j, otc_subscriber_rtc_stats_report_cb otc_subscriber_rtc_stats_report_cbVar, long j2);

    public static final native int otc_subscriber_set_preferred_framerate(long j, float f);

    public static final native int otc_subscriber_set_preferred_resolution(long j, long j2, long j3);

    public static final native int otc_subscriber_set_rtc_stats_report_cb(long j, long j2, otc_subscriber_rtc_stats_report_cb otc_subscriber_rtc_stats_report_cbVar);

    public static final native int otc_subscriber_set_subscribe_to_audio(long j, int i);

    public static final native int otc_subscriber_set_subscribe_to_video(long j, int i);

    public static final native int otc_subscriber_set_video_data_callback_behavior(long j, int i);

    public static final native long otc_video_capturer_callbacks_destroy_get(long j, otc_video_capturer_callbacks otc_video_capturer_callbacksVar);

    public static final native void otc_video_capturer_callbacks_destroy_set(long j, otc_video_capturer_callbacks otc_video_capturer_callbacksVar, long j2);

    public static final native long otc_video_capturer_callbacks_get_capture_settings_get(long j, otc_video_capturer_callbacks otc_video_capturer_callbacksVar);

    public static final native void otc_video_capturer_callbacks_get_capture_settings_set(long j, otc_video_capturer_callbacks otc_video_capturer_callbacksVar, long j2);

    public static final native long otc_video_capturer_callbacks_init_get(long j, otc_video_capturer_callbacks otc_video_capturer_callbacksVar);

    public static final native void otc_video_capturer_callbacks_init_set(long j, otc_video_capturer_callbacks otc_video_capturer_callbacksVar, long j2);

    public static final native long otc_video_capturer_callbacks_reserved_get(long j, otc_video_capturer_callbacks otc_video_capturer_callbacksVar);

    public static final native void otc_video_capturer_callbacks_reserved_set(long j, otc_video_capturer_callbacks otc_video_capturer_callbacksVar, long j2);

    public static final native long otc_video_capturer_callbacks_start_get(long j, otc_video_capturer_callbacks otc_video_capturer_callbacksVar);

    public static final native void otc_video_capturer_callbacks_start_set(long j, otc_video_capturer_callbacks otc_video_capturer_callbacksVar, long j2);

    public static final native long otc_video_capturer_callbacks_stop_get(long j, otc_video_capturer_callbacks otc_video_capturer_callbacksVar);

    public static final native void otc_video_capturer_callbacks_stop_set(long j, otc_video_capturer_callbacks otc_video_capturer_callbacksVar, long j2);

    public static final native long otc_video_capturer_callbacks_user_data_get(long j, otc_video_capturer_callbacks otc_video_capturer_callbacksVar);

    public static final native void otc_video_capturer_callbacks_user_data_set(long j, otc_video_capturer_callbacks otc_video_capturer_callbacksVar, long j2);

    public static final native int otc_video_capturer_get_content_hint(long j);

    public static final native int otc_video_capturer_set_content_hint(long j, int i);

    public static final native int otc_video_capturer_settings_expected_delay_get(long j, otc_video_capturer_settings otc_video_capturer_settingsVar);

    public static final native void otc_video_capturer_settings_expected_delay_set(long j, otc_video_capturer_settings otc_video_capturer_settingsVar, int i);

    public static final native int otc_video_capturer_settings_format_get(long j, otc_video_capturer_settings otc_video_capturer_settingsVar);

    public static final native void otc_video_capturer_settings_format_set(long j, otc_video_capturer_settings otc_video_capturer_settingsVar, int i);

    public static final native int otc_video_capturer_settings_fps_get(long j, otc_video_capturer_settings otc_video_capturer_settingsVar);

    public static final native void otc_video_capturer_settings_fps_set(long j, otc_video_capturer_settings otc_video_capturer_settingsVar, int i);

    public static final native int otc_video_capturer_settings_height_get(long j, otc_video_capturer_settings otc_video_capturer_settingsVar);

    public static final native void otc_video_capturer_settings_height_set(long j, otc_video_capturer_settings otc_video_capturer_settingsVar, int i);

    public static final native int otc_video_capturer_settings_mirror_on_local_render_get(long j, otc_video_capturer_settings otc_video_capturer_settingsVar);

    public static final native void otc_video_capturer_settings_mirror_on_local_render_set(long j, otc_video_capturer_settings otc_video_capturer_settingsVar, int i);

    public static final native int otc_video_capturer_settings_width_get(long j, otc_video_capturer_settings otc_video_capturer_settingsVar);

    public static final native void otc_video_capturer_settings_width_set(long j, otc_video_capturer_settings otc_video_capturer_settingsVar, int i);

    public static final native int otc_video_frame_delete(long j);

    public static final native int otc_video_frame_get_format(long j);

    public static final native int otc_video_frame_get_height(long j);

    public static final native long otc_video_frame_get_metadata(long j, long j2);

    public static final native int otc_video_frame_get_plane_stride(long j, int i);

    public static final native int otc_video_frame_get_width(long j);

    public static final native long sizetPointer_value(long j);

    public static final native short uint8Array_getitem(long j, int i);
}
