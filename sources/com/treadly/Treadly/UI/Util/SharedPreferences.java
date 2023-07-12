package com.treadly.Treadly.UI.Util;

import android.app.Activity;
import android.content.SharedPreferences;
import com.treadly.client.lib.sdk.Model.AuthenticationState;
import java.util.Date;
import org.json.JSONException;
import org.json.JSONObject;

/* loaded from: classes2.dex */
public class SharedPreferences {
    private static int AUTHENTICATE_SECRET_LENGTH = 8;
    private static final String SHARED_PREFERENCES_KEY = "Share";
    public static final SharedPreferences shared = new SharedPreferences();
    private android.content.SharedPreferences preferences;

    private SharedPreferences() {
    }

    public void init(Activity activity) {
        this.preferences = activity.getSharedPreferences(SHARED_PREFERENCES_KEY, 0);
    }

    public String getConnectedDeviceName() {
        return this.preferences.getString("connected_device", null);
    }

    public void storeConnectedDeviceName(String str) {
        if (str == null) {
            return;
        }
        SharedPreferences.Editor edit = this.preferences.edit();
        edit.putString("connected_device", str);
        edit.apply();
    }

    public void removeConnectedDeviceName() {
        SharedPreferences.Editor edit = this.preferences.edit();
        edit.remove("connected_device");
        edit.apply();
    }

    public void storeUserEmail(String str) {
        SharedPreferences.Editor edit = this.preferences.edit();
        edit.putString("user_email", str);
        edit.apply();
    }

    public String getUserEmail() {
        return this.preferences.getString("user_email", null);
    }

    public void storeUserPassword(String str) {
        SharedPreferences.Editor edit = this.preferences.edit();
        edit.putString("user_password", str);
        edit.apply();
    }

    public String getUserPassword() {
        return this.preferences.getString("user_password", null);
    }

    public void storeFacebookToken(String str) {
        SharedPreferences.Editor edit = this.preferences.edit();
        edit.putString("facebook_token", str);
        edit.apply();
    }

    public void storeConnectedDeviceAuthenticationState(String str, AuthenticationState authenticationState) {
        SharedPreferences.Editor edit = this.preferences.edit();
        if (authenticationState != null) {
            edit.putInt(str + "_authentication", authenticationState.value());
        } else {
            edit.remove(str + "_authentication");
        }
        edit.apply();
    }

    public String getFacebookToken() {
        return this.preferences.getString("facebook_token", null);
    }

    public void storeFacebookUserId(String str) {
        SharedPreferences.Editor edit = this.preferences.edit();
        edit.putString("facebook_userId", str);
        edit.apply();
    }

    public String getFacebookUserId() {
        return this.preferences.getString("facebook_userId", null);
    }

    public AuthenticationState getConnectedDeviceAuthenticationState(String str) {
        android.content.SharedPreferences sharedPreferences = this.preferences;
        if (sharedPreferences.contains(str + "_authentication")) {
            android.content.SharedPreferences sharedPreferences2 = this.preferences;
            return AuthenticationState.fromValue(sharedPreferences2.getInt(str + "_authentication", -1));
        }
        return null;
    }

    public void storeVideoFileNameAlias(String str, String str2) {
        JSONObject videoFileNameAliasList = getVideoFileNameAliasList("video_file_name_alias_list");
        if (videoFileNameAliasList == null) {
            videoFileNameAliasList = new JSONObject();
        }
        try {
            videoFileNameAliasList.put(str, str2);
            storeVideoFileNameAliasList(videoFileNameAliasList, "video_file_name_alias_list");
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void removeVideoFileNameAlias(String str) {
        JSONObject videoFileNameAliasList = getVideoFileNameAliasList("video_file_name_alias_list");
        if (videoFileNameAliasList == null) {
            return;
        }
        videoFileNameAliasList.remove(str);
        storeVideoFileNameAliasList(videoFileNameAliasList, "video_file_name_alias_list");
    }

    public String getVideoFileNameAlias(String str) {
        JSONObject videoFileNameAliasList = getVideoFileNameAliasList("video_file_name_alias_list");
        if (videoFileNameAliasList == null) {
            return null;
        }
        return videoFileNameAliasList.optString(str);
    }

    private void storeVideoFileNameAliasList(JSONObject jSONObject, String str) {
        SharedPreferences.Editor edit = this.preferences.edit();
        edit.putString(str, jSONObject.toString());
        edit.apply();
    }

    private JSONObject getVideoFileNameAliasList(String str) {
        String string = this.preferences.getString(str, null);
        if (string != null) {
            try {
                return new JSONObject(string);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    public void storeUsingInviteCode(boolean z) {
        SharedPreferences.Editor edit = this.preferences.edit();
        edit.putBoolean("using_invite_code", z);
        edit.apply();
    }

    public boolean getUsingInviteCode() {
        return this.preferences.getBoolean("using_invite_code", false);
    }

    public void setRunningSessionLastTotalCalories(int i, String str) {
        SharedPreferences.Editor edit = this.preferences.edit();
        edit.putInt("running_session_last_total_calories_" + str, i);
        edit.apply();
    }

    public int getRunningSessionLastTotalCalories(String str) {
        android.content.SharedPreferences sharedPreferences = this.preferences;
        return sharedPreferences.getInt("running_session_last_total_calories_" + str, -1);
    }

    public void setRunningSessionLastTotalSteps(int i, String str) {
        SharedPreferences.Editor edit = this.preferences.edit();
        edit.putInt("running_session_last_total_steps_" + str, i);
        edit.apply();
    }

    public int getRunningSessionLastTotalSteps(String str) {
        android.content.SharedPreferences sharedPreferences = this.preferences;
        return sharedPreferences.getInt("running_session_last_total_steps_" + str, -1);
    }

    public void setRunningSessionLastTotalDistance(double d, String str) {
        SharedPreferences.Editor edit = this.preferences.edit();
        edit.putFloat("running_session_last_total_distance_" + str, (float) d);
        edit.apply();
    }

    public double getRunningSessionLastTotalDistance(String str) {
        android.content.SharedPreferences sharedPreferences = this.preferences;
        return sharedPreferences.getFloat("running_session_last_total_distance_" + str, -1.0f);
    }

    public void setRunningSessionLastTotalDuration(double d, String str) {
        SharedPreferences.Editor edit = this.preferences.edit();
        edit.putFloat("running_session_last_total_duration_" + str, (float) d);
        edit.apply();
    }

    public double getRunningSessionLastTotalDuration(String str) {
        android.content.SharedPreferences sharedPreferences = this.preferences;
        return sharedPreferences.getFloat("running_session_last_total_duration_" + str, -1.0f);
    }

    public void setRunningSessionLastDailyGoal(int i, String str) {
        SharedPreferences.Editor edit = this.preferences.edit();
        edit.putInt("running_session_last_daily_goal_" + str, i);
        edit.apply();
    }

    public int getRunningSessionLastDailyGoal(String str) {
        android.content.SharedPreferences sharedPreferences = this.preferences;
        return sharedPreferences.getInt("running_session_last_daily_goal_" + str, -1);
    }

    public void setRunningSessionLastDailyGoalTarget(String str, String str2) {
        SharedPreferences.Editor edit = this.preferences.edit();
        edit.putString("running_session_last_daily_goal_target_" + str2, str);
        edit.apply();
    }

    public String getRunningSessionLastDailyGoalTarget(String str) {
        android.content.SharedPreferences sharedPreferences = this.preferences;
        return sharedPreferences.getString("running_session_last_daily_goal_target_" + str, "");
    }

    public void setRunningSessionLastDailyGoalTime(Date date, String str) {
        SharedPreferences.Editor edit = this.preferences.edit();
        edit.putLong("running_session_last_daily_goal_time_" + str, date.getTime());
        edit.apply();
    }

    public Date getRunningSessionLastDailyGoalTime(String str) {
        android.content.SharedPreferences sharedPreferences = this.preferences;
        long j = sharedPreferences.getLong("running_session_last_daily_goal_time_" + str, 0L);
        if (j > 0) {
            return new Date(j);
        }
        return null;
    }

    public void setRunningSessionLastDailyGoalTargetTime(Date date, String str) {
        SharedPreferences.Editor edit = this.preferences.edit();
        edit.putLong("running_session_last_daily_goal_target_time_" + str, date.getTime());
        edit.apply();
    }

    public Date getRunningSessionLastDailyGoalTargetTime(String str) {
        android.content.SharedPreferences sharedPreferences = this.preferences;
        long j = sharedPreferences.getLong("running_session_last_daily_goal_target_time_" + str, 0L);
        if (j > 0) {
            return new Date(j);
        }
        return null;
    }

    public void setRunningSessionLastTotalCaloriesTime(Date date, String str) {
        SharedPreferences.Editor edit = this.preferences.edit();
        edit.putLong("running_session_last_total_calories_time_" + str, date.getTime());
        edit.apply();
    }

    public Date getRunningSessionLastTotalCaloriesTime(String str) {
        android.content.SharedPreferences sharedPreferences = this.preferences;
        long j = sharedPreferences.getLong("running_session_last_total_calories_time_" + str, 0L);
        if (j > 0) {
            return new Date(j);
        }
        return null;
    }

    public void setRunningSessionLastTotalStepsTime(Date date, String str) {
        SharedPreferences.Editor edit = this.preferences.edit();
        edit.putLong("running_session_last_total_steps_time_" + str, date.getTime());
        edit.apply();
    }

    public Date getRunningSessionLastTotalStepsTime(String str) {
        android.content.SharedPreferences sharedPreferences = this.preferences;
        long j = sharedPreferences.getLong("running_session_last_total_steps_time_" + str, 0L);
        if (j > 0) {
            return new Date(j);
        }
        return null;
    }

    public void setRunningSessionLastTotalDistanceTime(Date date, String str) {
        SharedPreferences.Editor edit = this.preferences.edit();
        edit.putLong("running_session_last_total_distance_time_" + str, date.getTime());
        edit.apply();
    }

    public Date getRunningSessionLastTotalDistanceTime(String str) {
        android.content.SharedPreferences sharedPreferences = this.preferences;
        long j = sharedPreferences.getLong("running_session_last_total_distance_time_" + str, 0L);
        if (j > 0) {
            return new Date(j);
        }
        return null;
    }

    public void setRunningSessionLastTotalDurationTime(Date date, String str) {
        SharedPreferences.Editor edit = this.preferences.edit();
        edit.putLong("running_session_last_total_duration_time_" + str, date.getTime());
        edit.apply();
    }

    public Date getRunningSessionLastTotalDurationTime(String str) {
        android.content.SharedPreferences sharedPreferences = this.preferences;
        long j = sharedPreferences.getLong("running_session_last_total_duration_time_" + str, 0L);
        if (j > 0) {
            return new Date(j);
        }
        return null;
    }
}
