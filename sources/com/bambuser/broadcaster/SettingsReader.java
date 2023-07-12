package com.bambuser.broadcaster;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import com.facebook.internal.ServerProtocol;
import java.util.List;

/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: classes.dex */
public final class SettingsReader {
    static final String APPLICATION_ID = "application_id";
    static final String AUDIO_QUALITY_HIGH = "high";
    static final String AUTHOR = "author";
    static final String AUTHORIZATION_REMEMBER_BOTH = "remember_both";
    static final String CAM_OFF = "-1";
    static final String CAPTURE_ROTATION = "capture_rotation";
    static final String CROP_ASPECT_RATIO = "crop_aspect_ratio";
    static final String CUSTOM_DATA = "custom_data";
    static final String DEFAULT_AUDIO_QUALITY = "high";
    static final String DEFAULT_AUTHORIZATION = "remember_both";
    static final String DEFAULT_CAMERA = "0";
    static final int DEFAULT_MAX_LIVE_HEIGHT = 720;
    static final int DEFAULT_MAX_LIVE_WIDTH = 1280;
    static final int DEFAULT_OUTPUT_FRAME_RATE = 30000;
    static final String ENCODED_AUDIO_CHANNELS = "encoded_audio_channels";
    static final String FIRST_LAUNCH_DONE = "first_launch_done";
    static final String HINT_DECREASE_QUALITY = "hint_decrease_quality";
    static final String HINT_INCREASE_QUALITY = "hint_increase_quality";
    static final String MOVINO_HTTP_UPGRADE = "movino_http_upgrade";
    static final String MOVINO_TLS = "movino_tls";
    static final String OUTPUT_FRAME_RATE = "output_frame_rate";
    static final String PASSWORD = "password";
    static final String PREVIEW_ROTATION = "preview_rotation";
    static final String ROAMING_ACCEPT_TIME = "roaming_accept_time";
    static final String SKIP_BROADCAST_TICKET = "skip_broadcast_ticket";
    static final String TALKBACK_MIXIN = "talkback_mixin";
    static final String TITLE = "title";
    static final String TOO_HIGH_RES_TIME = "too_high_res_time";
    static final String USERNAME = "username";
    static final String VIDEO_MAX_LIVE_HEIGHT = "video_max_live_height";
    static final String VIDEO_MAX_LIVE_WIDTH = "video_max_live_width";
    static final String VIDEO_RESOLUTION = "video_resolution";
    private final SharedPreferences mSettings;
    static final String AUDIO_OFF = "off";
    static final String AUDIO_QUALITY_NORMAL = "normal";
    static final String[] AUDIO_QUALITY_VALUES = {AUDIO_OFF, AUDIO_QUALITY_NORMAL, "high"};
    static final String AUTHORIZATION_FORGET = "forget";
    static final String AUTHORIZATION_REMEMBER_USER = "remember_username";
    static final String[] AUTHORIZATION_VALUES = {AUTHORIZATION_FORGET, AUTHORIZATION_REMEMBER_USER, "remember_both"};
    static final String CAMERA_SELECTION = "camera_selection";
    static final String AUDIO_QUALITY = "audio_quality";
    static final String AUDIO_BLUETOOTH = "audio_bluetooth";
    static final String VIDEO_AUTO_RESOLUTION = "auto_resolution";
    static final String COMPLEMENT = "complement";
    static final String ARCHIVE = "archive";
    static final String PRIVATE = "private_key";
    static final String LOCATION = "location_key";
    static final String VIBRATION = "vibration_key";
    static final String CAPTURE_SOUNDS = "capture_sound";
    static final String HOSTNAME = "hostname";
    static final String DEFAULT_MOVINO_HOST = "video.bambuser.com";
    static final String PORT = "port";
    static final String DEFAULT_MOVINO_PORT = "443";
    static final String AUTHORIZATION = "authorization";
    private static final String[] DEFAULTS = {CAMERA_SELECTION, "0", AUDIO_QUALITY, "high", AUDIO_BLUETOOTH, ServerProtocol.DIALOG_RETURN_SCOPES_TRUE, VIDEO_AUTO_RESOLUTION, "false", COMPLEMENT, ServerProtocol.DIALOG_RETURN_SCOPES_TRUE, ARCHIVE, ServerProtocol.DIALOG_RETURN_SCOPES_TRUE, PRIVATE, "false", LOCATION, "false", VIBRATION, ServerProtocol.DIALOG_RETURN_SCOPES_TRUE, CAPTURE_SOUNDS, ServerProtocol.DIALOG_RETURN_SCOPES_TRUE, HOSTNAME, DEFAULT_MOVINO_HOST, PORT, DEFAULT_MOVINO_PORT, AUTHORIZATION, "remember_both"};

    SettingsReader(Context context) {
        this(PreferenceManager.getDefaultSharedPreferences(context));
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public SettingsReader(SharedPreferences sharedPreferences) {
        this.mSettings = sharedPreferences;
        int i = 0;
        while (true) {
            int i2 = i + 1;
            if (i2 >= DEFAULTS.length) {
                return;
            }
            String str = DEFAULTS[i];
            String str2 = DEFAULTS[i2];
            if (!hasKey(str)) {
                if (str2.equals(ServerProtocol.DIALOG_RETURN_SCOPES_TRUE) || str2.equals("false")) {
                    setBoolean(str, str2.equals(ServerProtocol.DIALOG_RETURN_SCOPES_TRUE));
                } else {
                    setString(str, str2);
                }
            }
            i += 2;
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void registerChangeListener(SharedPreferences.OnSharedPreferenceChangeListener onSharedPreferenceChangeListener) {
        this.mSettings.registerOnSharedPreferenceChangeListener(onSharedPreferenceChangeListener);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void unregisterChangeListener(SharedPreferences.OnSharedPreferenceChangeListener onSharedPreferenceChangeListener) {
        this.mSettings.unregisterOnSharedPreferenceChangeListener(onSharedPreferenceChangeListener);
    }

    boolean hasKey(String str) {
        return this.mSettings.contains(str);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public String getString(String str) {
        return this.mSettings.getString(str, "");
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public String getString(String str, String str2) {
        return this.mSettings.getString(str, str2);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void setString(String str, String str2) {
        this.mSettings.edit().putString(str, str2).commit();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean getBoolean(String str) {
        return this.mSettings.getBoolean(str, false);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void setBoolean(String str, boolean z) {
        this.mSettings.edit().putBoolean(str, z).commit();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public long getLong(String str) {
        return this.mSettings.getLong(str, 0L);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void setLong(String str, long j) {
        this.mSettings.edit().putLong(str, j).commit();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public int getInt(String str) {
        try {
            return Integer.parseInt(getString(str));
        } catch (NumberFormatException unused) {
            return 0;
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean resolutionStored() {
        return this.mSettings.contains(VIDEO_RESOLUTION);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public Resolution getResolution(Context context) {
        try {
            String[] split = this.mSettings.getString(VIDEO_RESOLUTION, null).split("x", 3);
            return new Resolution(Integer.parseInt(split[0]), Integer.parseInt(split[1]));
        } catch (Exception unused) {
            return DeviceInfoHandler.getDefaultResolution(DeviceInfoHandler.findCamInfo(context, getCameraId()));
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void setResolution(Resolution resolution) {
        setString(VIDEO_RESOLUTION, resolution.toString());
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public Resolution getCropAspectRatio() {
        int i;
        int i2;
        try {
            String[] split = this.mSettings.getString(CROP_ASPECT_RATIO, null).split("x", 2);
            i = Integer.parseInt(split[0]);
            try {
                i2 = Integer.parseInt(split[1]);
            } catch (Exception unused) {
                i2 = 0;
                return new Resolution(i, i2);
            }
        } catch (Exception unused2) {
            i = 0;
        }
        return new Resolution(i, i2);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public Resolution getCroppedResolution(Context context) {
        Resolution resolution = getResolution(context);
        int width = resolution.getWidth();
        int height = resolution.getHeight();
        Resolution cropAspectRatio = getCropAspectRatio();
        int width2 = cropAspectRatio.getWidth();
        int height2 = cropAspectRatio.getHeight();
        if (width2 > 0 && height2 > 0) {
            int i = (width * height2) / width2;
            if (height > i) {
                height = (i & 1) == 1 ? i + 1 : i;
            } else {
                int i2 = (width2 * height) / height2;
                if (width > i2) {
                    width = (i2 & 1) == 1 ? i2 + 1 : i2;
                }
            }
        }
        return new Resolution(width, height);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public Resolution getMaxLiveResolution() {
        return new Resolution(this.mSettings.getInt(VIDEO_MAX_LIVE_WIDTH, DEFAULT_MAX_LIVE_WIDTH), this.mSettings.getInt(VIDEO_MAX_LIVE_HEIGHT, DEFAULT_MAX_LIVE_HEIGHT));
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void setMaxLiveResolution(int i, int i2) {
        this.mSettings.edit().putInt(VIDEO_MAX_LIVE_WIDTH, i).putInt(VIDEO_MAX_LIVE_HEIGHT, i2).commit();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public int getPreviewRotation() {
        return this.mSettings.getInt(PREVIEW_ROTATION, -1);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public int getCaptureRotation() {
        return this.mSettings.getInt(CAPTURE_ROTATION, -1);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void setPreviewRotation(int i) {
        this.mSettings.edit().putInt(PREVIEW_ROTATION, i).commit();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void setCaptureRotation(int i) {
        this.mSettings.edit().putInt(CAPTURE_ROTATION, i).commit();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void setCaptureRotationAndCrop(int i, Resolution resolution) {
        this.mSettings.edit().putInt(CAPTURE_ROTATION, i).putString(CROP_ASPECT_RATIO, resolution.toString()).commit();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public String getCameraId() {
        return getString(CAMERA_SELECTION, "0");
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void switchCamera(Context context) {
        List<CamInfo> supportedCameras = DeviceInfoHandler.getSupportedCameras(context);
        int size = supportedCameras.size();
        if (size > 0) {
            setString(CAMERA_SELECTION, supportedCameras.get((supportedCameras.indexOf(DeviceInfoHandler.findCamInfo(context, getCameraId())) + 1) % size).mId);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public int getOutputFrameRate() {
        return this.mSettings.getInt(OUTPUT_FRAME_RATE, 30000);
    }

    void setOutputFrameRate(int i) {
        this.mSettings.edit().putInt(OUTPUT_FRAME_RATE, i).commit();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void setMovinoServer(String str, String str2, boolean z, boolean z2) {
        if (str == null || str2 == null || str.length() <= 0 || str2.length() <= 0) {
            str = DEFAULT_MOVINO_HOST;
            str2 = DEFAULT_MOVINO_PORT;
            z = false;
            z2 = false;
        }
        this.mSettings.edit().putString(HOSTNAME, str).putString(PORT, str2).putBoolean(MOVINO_TLS, z).putBoolean(MOVINO_HTTP_UPGRADE, z2).commit();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void setCredentials(String str, String str2) {
        String string = getString(AUTHORIZATION, "remember_both");
        if (string.equals(AUTHORIZATION_REMEMBER_USER)) {
            str2 = "";
        } else if (string.equals(AUTHORIZATION_FORGET)) {
            str = "";
            str2 = "";
        }
        this.mSettings.edit().putString(USERNAME, str).putString(PASSWORD, str2).commit();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean hasAudio() {
        return !getString(AUDIO_QUALITY, "high").equals(AUDIO_OFF);
    }

    boolean hasVibration() {
        return getBoolean(VIBRATION);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean hasComplement() {
        return getBoolean(ARCHIVE) && getBoolean(COMPLEMENT);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean useBt() {
        return getBoolean(AUDIO_BLUETOOTH);
    }
}
