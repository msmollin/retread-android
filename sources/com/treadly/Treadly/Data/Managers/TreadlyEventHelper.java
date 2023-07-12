package com.treadly.Treadly.Data.Managers;

import android.util.Log;
import android.util.Pair;
import com.treadly.Treadly.Data.Model.TrainerModeEnabledState;
import com.treadly.Treadly.Data.Model.TrainerModeState;
import com.treadly.Treadly.Data.Model.UserComment;
import com.treadly.Treadly.Data.Model.UserInfo;
import com.treadly.Treadly.Data.Model.UserTrainerMode;
import com.treadly.Treadly.Data.Model.UserVideoPrivateStateInfo;
import com.treadly.Treadly.Data.Model.UsersTrainerModes;
import com.treadly.Treadly.UI.TreadlyVideo.Data.VideoServiceUserStatsInfo;
import com.treadly.Treadly.UI.TreadmillControl.Data.TreadlyControlSpeedDataPoint;
import com.treadly.client.lib.sdk.Model.DistanceUnits;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import org.eclipse.paho.client.mqttv3.IMqttToken;
import org.eclipse.paho.client.mqttv3.MqttTopic;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/* loaded from: classes2.dex */
public class TreadlyEventHelper {
    public static final String BASE_EVENT_TOPIC = "treadly";
    public static final String GROUP_EVENT_TOPIC = "group";
    public static final String GROUP_ROOM_EVENT_TOPIC = "group_room";
    public static final int MESSAGE_ID_GROUP_INFO_STATE_CHANGED = 301;
    public static final int MESSAGE_ID_GROUP_MEMBER_STATE_CHANGED = 302;
    public static final int MESSAGE_ID_GROUP_POST_STATE_CHANGED = 304;
    public static final int MESSAGE_ID_GROUP_VIDEO_STATE_CHANGED = 303;
    public static final int MESSAGE_ID_MQTT_NOTIFICATION_EVENT = 701;
    public static final int MESSAGE_ID_RUNNING_SESSION_PARTICIPANT_ADD = 40;
    public static final int MESSAGE_ID_RUNNING_SESSION_PARTICIPANT_START = 401;
    public static final int MESSAGE_ID_USER_BROADCAST_ENDED = 205;
    public static final int MESSAGE_ID_USER_BROADCAST_STARTED = 204;
    public static final int MESSAGE_ID_USER_FRIEND_ADDED = 206;
    public static final int MESSAGE_ID_USER_FRIEND_REMOVED = 207;
    public static final int MESSAGE_ID_USER_GROUP_MEMBER_REQUEST_STATE_CHANGED = 210;
    public static final int MESSAGE_ID_USER_ONLINE_STATUS = 0;
    public static final int MESSAGE_ID_USER_SESSION_EXPIRED = 201;
    public static final int MESSAGE_ID_USER_VIDEO_BROADCAST_INVITE_REQUEST = 202;
    public static final int MESSAGE_ID_USER_VIDEO_PRIVATE_INVITE_DECLINED = 209;
    public static final int MESSAGE_ID_USER_VIDEO_PRIVATE_INVITE_DELETED = 208;
    public static final int MESSAGE_ID_USER_VIDEO_PRIVATE_INVITE_REQUEST = 203;
    public static final int MESSAGE_ID_VIDEO_ARCHIVE_LIKE = 601;
    public static final int MESSAGE_ID_VIDEO_BROADCAST_COMMENT = 3;
    public static final int MESSAGE_ID_VIDEO_BROADCAST_ENDED = 4;
    public static final int MESSAGE_ID_VIDEO_BROADCAST_JOIN = 1;
    public static final int MESSAGE_ID_VIDEO_BROADCAST_LEAVE = 2;
    public static final int MESSAGE_ID_VIDEO_BROADCAST_LIKE = 6;
    public static final int MESSAGE_ID_VIDEO_BROADCAST_USER_STATS = 5;
    public static final int MESSAGE_ID_VIDEO_PRIVATE_CLIENT_FORCE_DISCONNECT = 108;
    public static final int MESSAGE_ID_VIDEO_PRIVATE_JOIN = 101;
    public static final int MESSAGE_ID_VIDEO_PRIVATE_LEAVE = 102;
    public static final int MESSAGE_ID_VIDEO_PRIVATE_LIKE = 118;
    public static final int MESSAGE_ID_VIDEO_PRIVATE_PAUSE = 110;
    public static final int MESSAGE_ID_VIDEO_PRIVATE_RECORDING_COMPOSITE_END = 117;
    public static final int MESSAGE_ID_VIDEO_PRIVATE_RECORDING_COMPOSITE_START = 116;
    public static final int MESSAGE_ID_VIDEO_PRIVATE_STUDENT_MODE_REQUEST = 106;
    public static final int MESSAGE_ID_VIDEO_PRIVATE_STUDENT_MODE_RESPONSE = 107;
    public static final int MESSAGE_ID_VIDEO_PRIVATE_TRAINER_MODE_REQUEST = 104;
    public static final int MESSAGE_ID_VIDEO_PRIVATE_TRAINER_MODE_RESPONSE = 105;
    public static final int MESSAGE_ID_VIDEO_PRIVATE_UNPAUSE = 111;
    public static final int MESSAGE_ID_VIDEO_PRIVATE_USERS_STATE_CHANGED = 112;
    public static final int MESSAGE_ID_VIDEO_PRIVATE_USERS_TRAINER_MODES = 109;
    public static final int MESSAGE_ID_VIDEO_PRIVATE_USER_CONNECTING = 113;
    public static final int MESSAGE_ID_VIDEO_PRIVATE_USER_STATS = 103;
    public static final int MESSAGE_ID_VIDEO_PRIVATE_USER_TREADMILL_CONNECTED = 114;
    public static final int MESSAGE_ID_VIDEO_PRIVATE_USER_TREADMILL_NOT_CONNECTED = 115;
    public static final int MESSAGE_ID_WAITING_ROOM_STATE_CHANGE = 501;
    public static final String NOTIFICATION_EVENT_TOPIC = "notificationevent";
    public static final String RUNNING_SESSION_EVENT_TOPIC = "runningsession";
    private static final String TAG = "EVENT_HELPER";
    public static final String USER_EVENT_TOPIC = "user";
    public static final String VIDEO_ARCHIVE_EVENT_TOPIC = "videoarchive";
    public static final String VIDEO_PRIVATE_EVENT_TOPIC = "videoprivate";
    public static final String VIDEO_PUBLIC_EVENT_TOPIC = "videopublic";
    public static final String keyApnBody = "apn_body";
    public static final String keyApnCategory = "apn_category";
    public static final String keyApnTitle = "apn_title";
    public static final String keyAvatar = "avatar";
    public static final String keyAverageSpeed = "avg_speed";
    public static final String keyCalories = "calories";
    public static final String keyClientId = "client_id";
    public static final String keyComment = "comment";
    public static final String keyCompositeId = "composite_id";
    public static final String keyDistance = "distance";
    public static final String keyDuration = "duration";
    public static final String keyGroupId = "group_id";
    public static final String keyIsOnline = "is_online";
    public static final String keyIsTreadmillConnected = "is_treadmill_connected";
    public static final String keyLikeCount = "like_count";
    public static final String keyMessageId = "message_id";
    public static final String keyName = "name";
    public static final String keyParticipantIds = "participant_ids";
    public static final String keyPostId = "post_id";
    public static final String keySessionId = "session_id";
    public static final String keySpeed = "speed";
    public static final String keySpeedData = "speed_data";
    public static final String keySpeedUnits = "speed_units";
    public static final String keyStatus = "status";
    public static final String keySteps = "steps";
    public static final String keyStudentModeState = "student_mode_state";
    public static final String keyTargetUserId = "target_user_id";
    public static final String keyTrainerModeEnabledState = "trainer_mode_enable_state";
    public static final String keyTrainerModeState = "trainer_mode_state";
    public static final String keyUserId = "user_id";
    public static final String keyUserList = "user_list";
    public static final String keyUsersTrainerModes = "users_trainer_modes";
    public static final String keyVideoId = "video_id";

    public static String createVideoBroadcastInviteRequestEventMessage(UserInfo userInfo) {
        return createBasicUserInfoRequestEventMessage(Integer.valueOf((int) MESSAGE_ID_USER_VIDEO_BROADCAST_INVITE_REQUEST).intValue(), userInfo);
    }

    public static UserInfo parseVideoBroadcastInviteRequestEventMessage(String str) {
        return parseBasicUserInfoRequestEventMessage(str);
    }

    public static String createJoinVideoBroadcastEventMessage(UserInfo userInfo) {
        return createBasicUserInfoRequestEventMessage(1, userInfo);
    }

    public static UserInfo parseJoinVideoBroadcastEventMessage(String str) {
        return parseBasicUserInfoRequestEventMessage(str);
    }

    public static String createLeaveVideoBroadcastEventMessage(UserInfo userInfo) {
        return createBasicUserInfoRequestEventMessage(2, userInfo);
    }

    public static UserInfo parseLeaveVideoBroadcastEventmessage(String str) {
        return parseBasicUserInfoRequestEventMessage(str);
    }

    public static String createVideoBroadcastCommentEventMessage(UserInfo userInfo, String str) {
        JSONObject jSONObject = new JSONObject();
        try {
            jSONObject.put(keyMessageId, 3);
            jSONObject.put("user_id", userInfo.id);
            jSONObject.put("name", userInfo.name);
            jSONObject.put(keyAvatar, userInfo.avatarPath);
            jSONObject.put(keyComment, str);
            return jSONObject.toString();
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String createVideoBroadcastUserStatsEventMessage(VideoServiceUserStatsInfo videoServiceUserStatsInfo) {
        return createUserStatsEventMessage(5, videoServiceUserStatsInfo);
    }

    public static String createUserStatsEventMessage(int i, VideoServiceUserStatsInfo videoServiceUserStatsInfo) {
        JSONObject jSONObject = new JSONObject();
        try {
            jSONObject.put(keyMessageId, i);
            jSONObject.put("user_id", videoServiceUserStatsInfo.userId);
            jSONObject.put("name", videoServiceUserStatsInfo.name);
            jSONObject.put(keyCalories, videoServiceUserStatsInfo.calories);
            jSONObject.put(keyDistance, videoServiceUserStatsInfo.distance);
            jSONObject.put(keySteps, videoServiceUserStatsInfo.steps);
            jSONObject.put(keySpeedUnits, videoServiceUserStatsInfo.speedUnits.value());
            jSONObject.put(keyAverageSpeed, videoServiceUserStatsInfo.averageSpeed);
            jSONObject.put(keyDuration, videoServiceUserStatsInfo.duration);
            jSONObject.put(keySpeed, videoServiceUserStatsInfo.speed);
            JSONArray jSONArray = new JSONArray();
            for (TreadlyControlSpeedDataPoint treadlyControlSpeedDataPoint : videoServiceUserStatsInfo.speedDataSet) {
                jSONArray.put(treadlyControlSpeedDataPoint.speed);
            }
            jSONObject.put(keySpeedData, jSONArray);
            return jSONObject.toString();
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static UserComment parseVideoBroadcastCommentEventMessage(String str) {
        try {
            JSONObject jSONObject = new JSONObject(str);
            String optString = jSONObject.optString("user_id");
            String optString2 = jSONObject.optString("name");
            String optString3 = jSONObject.optString(keyComment);
            if (optString == null || optString2 == null || optString3 == null) {
                return null;
            }
            UserInfo userInfo = new UserInfo(optString, optString2, "");
            userInfo.avatarPath = getUserAvatar(jSONObject);
            return new UserComment(userInfo, optString3);
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static VideoServiceUserStatsInfo parseVideoBroadcastUserStatsEventMessage(String str) {
        DistanceUnits distanceUnits;
        try {
            JSONObject jSONObject = new JSONObject(str);
            String optString = jSONObject.optString("user_id");
            String optString2 = jSONObject.optString("name");
            int optInt = jSONObject.optInt(keyCalories);
            double optDouble = jSONObject.optDouble(keyDistance);
            int optInt2 = jSONObject.optInt(keySteps);
            double optDouble2 = jSONObject.optDouble(keySpeed);
            double optDouble3 = jSONObject.optDouble(keyAverageSpeed);
            if (jSONObject.optInt(keySpeedUnits) == DistanceUnits.MI.value()) {
                distanceUnits = DistanceUnits.MI;
            } else {
                distanceUnits = DistanceUnits.KM;
            }
            return new VideoServiceUserStatsInfo(optString, optString2, optInt, optDouble, optInt2, optDouble2, optDouble3, new ArrayList(), distanceUnits, jSONObject.getInt(keyDuration));
        } catch (JSONException e) {
            e.printStackTrace();
            Log.e("public broadcast user stats::EventHelper", e.getMessage());
            return null;
        }
    }

    private static UserInfo parseBasicUserInfoRequestEventMessage(String str) {
        try {
            JSONObject jSONObject = new JSONObject(str);
            String optString = jSONObject.optString("user_id");
            String optString2 = jSONObject.optString("name");
            if (optString == null || optString2 == null) {
                return null;
            }
            UserInfo userInfo = new UserInfo(optString, optString2, "");
            userInfo.avatarPath = getUserAvatar(jSONObject);
            return userInfo;
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

    static String getUserAvatar(JSONObject jSONObject) {
        String optString = jSONObject.optString(keyAvatar);
        return (optString == null || optString.isEmpty()) ? new UserInfo("", "", "").avatarURL() : optString;
    }

    public static String createRunningSessionParticipantStartEventMessage(String str, ArrayList<String> arrayList) {
        return createRunningSessionParticipantEventMessage(MESSAGE_ID_RUNNING_SESSION_PARTICIPANT_START, str, arrayList);
    }

    public static String createRunningSessionParticipantEventMessage(int i, String str, ArrayList<String> arrayList) {
        JSONObject jSONObject = new JSONObject();
        try {
            jSONObject.put(keyMessageId, i);
            jSONObject.put("user_id", str);
            jSONObject.put(keyParticipantIds, arrayList);
            return jSONObject.toString();
        } catch (Exception e) {
            e.printStackTrace();
            return e.toString();
        }
    }

    public static String createUserBroadcastStartedEventMessage(UserInfo userInfo) {
        return createBasicUserInfoRequestEventMessage(MESSAGE_ID_USER_BROADCAST_STARTED, userInfo);
    }

    public static UserInfo parseUserBroadcastStartedEventMessage(String str) {
        return parseBasicUserInfoRequestEventMessage(str);
    }

    public static String createUserBroadcastEndedEventMessage(UserInfo userInfo) {
        return createBasicUserInfoRequestEventMessage(MESSAGE_ID_USER_BROADCAST_ENDED, userInfo);
    }

    public static UserInfo parseUserBroadcastEndedEventMessage(String str) {
        return parseBasicUserInfoRequestEventMessage(str);
    }

    public static String createUserFriendAddedEventMessage(UserInfo userInfo) {
        return createBasicUserInfoRequestEventMessage(MESSAGE_ID_USER_FRIEND_ADDED, userInfo);
    }

    public static UserInfo parseUserFriendAddedEventMessage(String str) {
        return parseBasicUserInfoRequestEventMessage(str);
    }

    public static String createVideoBroadcastEndedEventMessage() {
        JSONObject jSONObject = new JSONObject();
        try {
            jSONObject.put(keyMessageId, 4);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jSONObject.toString();
    }

    public static Integer parseMessageId(String str) {
        try {
            return Integer.valueOf(new JSONObject(str).getInt(keyMessageId));
        } catch (JSONException unused) {
            return -1;
        }
    }

    public static Integer parseVideoLikeEventMessage(String str) {
        try {
            Integer valueOf = Integer.valueOf(new JSONObject(str).optInt(keyLikeCount));
            if (valueOf != null) {
                return valueOf;
            }
            return null;
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static UserInfo parseUserOnlineStatus(String str) {
        try {
            JSONObject jSONObject = new JSONObject(str);
            String string = jSONObject.getString("user_id");
            Boolean valueOf = Boolean.valueOf(jSONObject.getBoolean("is_online"));
            UserInfo userInfo = new UserInfo(string, "", "");
            userInfo.online = valueOf.booleanValue();
            return userInfo;
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String parseMqttNotificationCategory(String str) {
        try {
            return new JSONObject(str).optString(keyApnCategory);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static JSONObject parseMqttNotificationPayload(String str) {
        try {
            return new JSONObject(str);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String parseMqttNotificationTitle(String str) {
        try {
            return new JSONObject(str).optString(keyApnTitle);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String parseMqttNotificationBody(String str) {
        try {
            return new JSONObject(str).optString(keyApnBody);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String[] getTopicLevels(String str) {
        return str.split(MqttTopic.TOPIC_LEVEL_SEPARATOR);
    }

    public static ArrayList<UserVideoPrivateStateInfo> parseVideoPrivateUserStateChangedEventMessage(String str) {
        try {
            try {
                JSONArray jSONArray = new JSONObject(str).getJSONArray(keyUserList);
                ArrayList<UserVideoPrivateStateInfo> arrayList = new ArrayList<>();
                for (int i = 0; i < jSONArray.length(); i++) {
                    JSONObject jSONObject = jSONArray.getJSONObject(i);
                    String optString = jSONObject.optString("user_id");
                    String optString2 = jSONObject.optString("name");
                    String optString3 = jSONObject.optString("status");
                    boolean optBoolean = jSONObject.optBoolean(keyIsTreadmillConnected);
                    UserVideoPrivateStateInfo userVideoPrivateStateInfo = new UserVideoPrivateStateInfo(optString, optString2, "", getUserAvatar(jSONObject), optString3);
                    userVideoPrivateStateInfo.isTreadmillConnected = optBoolean;
                    arrayList.add(userVideoPrivateStateInfo);
                }
                return arrayList;
            } catch (JSONException e) {
                Log.d(TAG, "User List does not exist");
                Log.e(TAG, e.getMessage());
                return null;
            }
        } catch (JSONException e2) {
            Log.e(TAG, e2.getMessage());
            return null;
        }
    }

    public static String createJoinVideoPrivateEventMessage(UserInfo userInfo) {
        return createBasicUserInfoRequestEventMessage(101, userInfo);
    }

    public static UserInfo parseJoinVideoPrivateEventMessage(String str) {
        return parseBasicUserInfoRequestEventMessage(str);
    }

    public static String createLeaveVideoPrivateEventMessage(UserInfo userInfo) {
        return createBasicUserInfoRequestEventMessage(102, userInfo);
    }

    public static UserInfo parseLeaveVideoPrivateEventMessage(String str) {
        return parseBasicUserInfoRequestEventMessage(str);
    }

    public static String createPauseVideoPrivateEventMessage(UserInfo userInfo) {
        return createBasicUserInfoRequestEventMessage(110, userInfo);
    }

    public static UserInfo parsePauseVideoPrivateEventMessage(String str) {
        return parseBasicUserInfoRequestEventMessage(str);
    }

    public static String createUnpauseVideoPrivateEventMessage(UserInfo userInfo) {
        return createBasicUserInfoRequestEventMessage(111, userInfo);
    }

    public static UserInfo parseUnpauseVideoPrivateEventMessage(String str) {
        return parseBasicUserInfoRequestEventMessage(str);
    }

    public static String createVideoPrivateUserStatsEventMessage(VideoServiceUserStatsInfo videoServiceUserStatsInfo) {
        return createUserStatsEventMessage(103, videoServiceUserStatsInfo);
    }

    public static VideoServiceUserStatsInfo parseVideoPrivateUserStatsEventMessage(String str) {
        return parseUserStatsEventMessage(str);
    }

    public static String createTrainerModeRequestEventMessage(TrainerModeState trainerModeState, UserInfo userInfo) {
        JSONObject jSONObject = new JSONObject();
        try {
            jSONObject.put(keyMessageId, 104);
            jSONObject.put(keyTrainerModeState, trainerModeState.getValue());
            jSONObject.put("user_id", userInfo.id);
            jSONObject.put("name", userInfo.name);
            jSONObject.put(keyAvatar, userInfo.avatarPath);
            return jSONObject.toString();
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String createTrainerModeResponseEventMessage(TrainerModeState trainerModeState, UserInfo userInfo) {
        JSONObject jSONObject = new JSONObject();
        try {
            jSONObject.put(keyMessageId, 105);
            jSONObject.put(keyTrainerModeState, trainerModeState.getValue());
            jSONObject.put("user_id", userInfo.id);
            jSONObject.put("name", userInfo.name);
            jSONObject.put(keyAvatar, userInfo.avatarPath);
            return jSONObject.toString();
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static UserTrainerMode parseUserTrainerModeEventMessage(String str) {
        try {
            JSONObject jSONObject = new JSONObject(str);
            String optString = jSONObject.optString("user_id");
            String optString2 = jSONObject.optString("name");
            TrainerModeState fromValue = TrainerModeState.fromValue(jSONObject.optInt(keyTrainerModeState));
            if (fromValue != null) {
                UserInfo userInfo = new UserInfo(optString, optString2, "");
                userInfo.avatarPath = getUserAvatar(jSONObject);
                return new UserTrainerMode(userInfo, fromValue);
            }
            return null;
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String createStudentModeRequestEventMessage(TrainerModeState trainerModeState, UserInfo userInfo, String str) {
        JSONObject jSONObject = new JSONObject();
        try {
            jSONObject.put(keyMessageId, 106);
            jSONObject.put(keyTrainerModeState, trainerModeState.getValue());
            jSONObject.put("user_id", userInfo.id);
            jSONObject.put("name", userInfo.name);
            jSONObject.put(keyAvatar, userInfo.avatarPath);
            jSONObject.put("target_user_id", str);
            return jSONObject.toString();
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String createStudentModeResponseEventMessage(TrainerModeState trainerModeState, UserInfo userInfo, String str) {
        JSONObject jSONObject = new JSONObject();
        try {
            jSONObject.put(keyMessageId, 107);
            jSONObject.put(keyTrainerModeState, trainerModeState.getValue());
            jSONObject.put("user_id", userInfo.id);
            jSONObject.put("name", userInfo.name);
            jSONObject.put(keyAvatar, userInfo.avatarPath);
            jSONObject.put("target_user_id", str);
            return jSONObject.toString();
        } catch (JSONException e) {
            e.getStackTrace();
            return null;
        }
    }

    public static Pair<UserTrainerMode, String> parseUserStudentModeEventMessage(String str) {
        try {
            JSONObject jSONObject = new JSONObject(str);
            String optString = jSONObject.optString("user_id");
            String optString2 = jSONObject.optString("name");
            String optString3 = jSONObject.optString("target_user_id");
            TrainerModeState fromValue = TrainerModeState.fromValue(jSONObject.optInt(keyTrainerModeState));
            if (fromValue != null) {
                UserInfo userInfo = new UserInfo(optString, optString2, "");
                userInfo.avatarPath = getUserAvatar(jSONObject);
                return new Pair<>(new UserTrainerMode(userInfo, fromValue), optString3);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return new Pair<>(null, null);
    }

    public static String createVideoPrivateUsersTrainerModesEventMessage(List<UsersTrainerModes> list) {
        JSONObject jSONObject = new JSONObject();
        try {
            jSONObject.put(keyMessageId, 109);
            JSONArray jSONArray = new JSONArray();
            for (UsersTrainerModes usersTrainerModes : list) {
                JSONObject jSONObject2 = new JSONObject();
                jSONObject2.put("user_id", usersTrainerModes.user.id);
                jSONObject2.put("name", usersTrainerModes.user.name);
                jSONObject2.put(keyTrainerModeState, usersTrainerModes.trainerModeState.getValue());
                jSONObject2.put(keyStudentModeState, usersTrainerModes.studentModeState.getValue());
                jSONObject2.put(keyTrainerModeEnabledState, usersTrainerModes.trainerModeEnabled.getValue());
                jSONArray.put(jSONObject2);
            }
            jSONObject.put(keyUsersTrainerModes, jSONArray);
            return jSONObject.toString();
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static List<UsersTrainerModes> parseVideoPrivateUsersTrainerModesEventMessage(String str) {
        try {
            JSONArray jSONArray = new JSONObject(str).getJSONArray(keyUsersTrainerModes);
            ArrayList arrayList = new ArrayList();
            for (int i = 0; i < jSONArray.length(); i++) {
                JSONObject jSONObject = jSONArray.getJSONObject(i);
                String optString = jSONObject.optString("user_id");
                String optString2 = jSONObject.optString("name");
                int optInt = jSONObject.optInt(keyTrainerModeState);
                int optInt2 = jSONObject.optInt(keyStudentModeState);
                if (!optString.isEmpty() && !optString2.isEmpty()) {
                    UsersTrainerModes usersTrainerModes = new UsersTrainerModes(new UserInfo(optString, optString2, ""), TrainerModeState.fromValue(optInt), TrainerModeState.fromValue(optInt2));
                    usersTrainerModes.trainerModeEnabled = TrainerModeEnabledState.fromValue(jSONObject.optInt(keyTrainerModeEnabledState));
                    arrayList.add(usersTrainerModes);
                }
            }
            return arrayList;
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String createVideoPrivateUserConnectingEventMessage(UserInfo userInfo) {
        return createBasicUserInfoRequestEventMessage(113, userInfo);
    }

    public static String createVideoPrivateInviteRequestEventMessage(UserInfo userInfo) {
        return createBasicUserInfoRequestEventMessage(MESSAGE_ID_USER_VIDEO_PRIVATE_INVITE_REQUEST, userInfo);
    }

    public static UserInfo parseVideoPrivateInviteRequestEventMessage(String str) {
        return parseBasicUserInfoRequestEventMessage(str);
    }

    public static String createVideoPrivateInviteDeletedEventMessage(UserInfo userInfo) {
        return createBasicUserInfoRequestEventMessage(208, userInfo);
    }

    public static UserInfo parseVideoPrivateInviteDeletedEventMessage(String str) {
        return parseBasicUserInfoRequestEventMessage(str);
    }

    public static String createVideoPrivateInviteDeclinedEventMessage(UserInfo userInfo) {
        return createBasicUserInfoRequestEventMessage(MESSAGE_ID_USER_VIDEO_PRIVATE_INVITE_DECLINED, userInfo);
    }

    public static UserInfo parseVideoPrivateInviteDeclinedEventMessage(String str) {
        return parseBasicUserInfoRequestEventMessage(str);
    }

    public static String createVideoPrivateUserTreadmillConnectedEventMessage(UserInfo userInfo) {
        return createBasicUserInfoRequestEventMessage(114, userInfo);
    }

    public static UserInfo parseVideoPrivateUserTreadmillConnectedEventMessage(String str) {
        return parseBasicUserInfoRequestEventMessage(str);
    }

    public static String createVideoPrivateUserTreadmillNotConnectedEventMessage(UserInfo userInfo) {
        return createBasicUserInfoRequestEventMessage(115, userInfo);
    }

    public static UserInfo parseVideoPrivateUserTreadmillNotConnectedEventMessage(String str) {
        return parseBasicUserInfoRequestEventMessage(str);
    }

    protected static VideoServiceUserStatsInfo parseUserStatsEventMessage(String str) {
        DistanceUnits distanceUnits;
        try {
            JSONObject jSONObject = new JSONObject(str);
            String optString = jSONObject.optString("user_id");
            String optString2 = jSONObject.optString("name");
            int optInt = jSONObject.optInt(keyCalories);
            double optDouble = jSONObject.optDouble(keyDistance);
            int optInt2 = jSONObject.optInt(keySteps);
            double optDouble2 = jSONObject.optDouble(keySpeed);
            double optDouble3 = jSONObject.optDouble(keyAverageSpeed);
            if (jSONObject.optInt(keySpeedUnits) == DistanceUnits.MI.value()) {
                distanceUnits = DistanceUnits.MI;
            } else {
                distanceUnits = DistanceUnits.KM;
            }
            return new VideoServiceUserStatsInfo(optString, optString2, optInt, optDouble, optInt2, optDouble2, optDouble3, new ArrayList(), distanceUnits, jSONObject.getInt(keyDuration));
        } catch (JSONException e) {
            e.printStackTrace();
            Log.e("private::EventHelper", e.getMessage());
            return null;
        }
    }

    protected static String createBasicUserInfoRequestEventMessage(int i, UserInfo userInfo) {
        JSONObject jSONObject = new JSONObject();
        try {
            jSONObject.put(keyMessageId, i);
            jSONObject.put("user_id", userInfo.id);
            jSONObject.put("name", userInfo.name);
            jSONObject.put(keyAvatar, userInfo.avatarPath);
            return jSONObject.toString();
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

    protected static String parseSessionId(String str) {
        String optString;
        String str2 = null;
        try {
            optString = new JSONObject(str).optString(keySessionId);
        } catch (JSONException e) {
            e = e;
        }
        try {
            if (!optString.isEmpty()) {
                return optString;
            }
        } catch (JSONException e2) {
            str2 = optString;
            e = e2;
            Log.e(TAG, (String) Objects.requireNonNull(e.getMessage()));
            return str2;
        }
        return str2;
    }

    public static void subscribeTopic(String str) {
        MqttConnectionManager.getInstance().subscribe(str);
    }

    public static void unsubscribeTopic(String str) {
        MqttConnectionManager.getInstance().unsubscribe(str);
    }

    public static IMqttToken publishMessage(String str, String str2) {
        return MqttConnectionManager.getInstance().publish(str, str2);
    }
}
