package com.treadly.Treadly.Data.Managers;

import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import com.facebook.appevents.AppEventsConstants;
import com.github.mikephil.charting.utils.Utils;
import com.treadly.Treadly.Data.Listeners.TreadlyServiceResponseEventAdapter;
import com.treadly.Treadly.Data.Listeners.TreadlyServiceResponseEventListener;
import com.treadly.Treadly.Data.Model.FollowInfo;
import com.treadly.Treadly.Data.Model.FriendInviteInfo;
import com.treadly.Treadly.Data.Model.TreadlyNetworkResponse;
import com.treadly.Treadly.Data.Model.UserDailyGoalType;
import com.treadly.Treadly.Data.Model.UserDiscoverInfo;
import com.treadly.Treadly.Data.Model.UserInfo;
import com.treadly.Treadly.Data.Model.UserProfileInfo;
import com.treadly.Treadly.Data.Model.UserProfileRequest;
import com.treadly.Treadly.Data.Model.UserRunningSessionInfo;
import com.treadly.Treadly.Data.Model.UserRunningSessionParticipantInfo;
import com.treadly.Treadly.Data.Model.UserRunningSessionSegmentInfo;
import com.treadly.Treadly.Data.Model.UserStatsInfo;
import com.treadly.Treadly.Data.Model.UserTokenInfo;
import com.treadly.Treadly.Data.Utility.RequestUtil;
import com.treadly.client.lib.sdk.Model.DistanceUnits;
import java.io.File;
import java.io.PrintStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Iterator;
import java.util.Locale;
import java.util.TimeZone;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import okhttp3.Headers;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/* loaded from: classes2.dex */
public class TreadlyServiceHelper {
    protected static final String SERVICE_URI_BASE = "https://env-staging.treadly.co:7001/api";
    protected static final String SERVICE_URI_ROUTE_DISCOVER_FOLLOWERS_GET = "https://env-staging.treadly.co:7001/api/users/follow/discover";
    protected static final String SERVICE_URI_ROUTE_PROFILE = "https://env-staging.treadly.co:7001/api/profile/v2";
    protected static final String SERVICE_URI_ROUTE_PROFILE_USER_ID = "https://env-staging.treadly.co:7001/api/profile/v2/user/%s?timezone=%d";
    protected static final String SERVICE_URI_ROUTE_STATS = "https://env-staging.treadly.co:7001/api/stats";
    protected static final String SERVICE_URI_ROUTE_STATS_DAY = "https://env-staging.treadly.co:7001/api/stats/day?timezone=%d";
    protected static final String SERVICE_URI_ROUTE_STATS_DAY_SELECTED = "https://env-staging.treadly.co:7001/api/stats/day/selected?day=%d&month=%d&year=%d&timezone=%d";
    protected static final String SERVICE_URI_ROUTE_STATS_DAY_SELECTED_SUMMARY = "https://env-staging.treadly.co:7001/api/stats/day/selected/summary?day=%d&month=%d&year=%d&timezone=%d";
    protected static final String SERVICE_URI_ROUTE_STATS_DETAILED = "https://env-staging.treadly.co:7001/api/stats/detailed";
    protected static final String SERVICE_URI_ROUTE_STATS_DEVICE = "https://env-staging.treadly.co:7001/api/statsdevice";
    protected static final String SERVICE_URI_ROUTE_STATS_DEVICE_CLAIM = "https://env-staging.treadly.co:7001/api/statsdevice/claim";
    protected static final String SERVICE_URI_ROUTE_STATS_DEVICE_CLAIM_ALL = "https://env-staging.treadly.co:7001/api/statsdevice/claim/all";
    protected static final String SERVICE_URI_ROUTE_STATS_DEVICE_CLAIM_DELETE = "https://env-staging.treadly.co:7001/api/statsdevice/claim/delete";
    protected static final String SERVICE_URI_ROUTE_STATS_DEVICE_COMPLETE = "https://env-staging.treadly.co:7001/api/statsdevice/complete";
    protected static final String SERVICE_URI_ROUTE_STATS_DEVICE_COMPLETE_POST_CREATE = "https://env-staging.treadly.co:7001/api/statsdevice/complete/post/create";
    protected static final String SERVICE_URI_ROUTE_STATS_DEVICE_COMPLETE_POST_UPLOAD = "https://env-staging.treadly.co:7001/api/statsdevice/complete/post/upload";
    protected static final String SERVICE_URI_ROUTE_STATS_DEVICE_COMPLETE_V2 = "https://env-staging.treadly.co:7001/api/statsdevice/complete_v2";
    protected static final String SERVICE_URI_ROUTE_STATS_DEVICE_DAY = "https://env-staging.treadly.co:7001/api/statsdevice/v2/day?timezone=%d";
    protected static final String SERVICE_URI_ROUTE_STATS_DEVICE_DAY_SELECTED = "https://env-staging.treadly.co:7001/api/statsdevice/v2/day/selected?day=%d&month=%d&year=%d&timezone=%d";
    protected static final String SERVICE_URI_ROUTE_STATS_DEVICE_DAY_SELECTED_SUMMARY = "https://env-staging.treadly.co:7001/api/statsdevice/day/selected/summary?day=%d&month=%d&year=%d&timezone=%d";
    protected static final String SERVICE_URI_ROUTE_STATS_DEVICE_DETAILED = "https://env-staging.treadly.co:7001/api/statsdevice/detailed";
    protected static final String SERVICE_URI_ROUTE_STATS_DEVICE_UNCLAIMED = "https://env-staging.treadly.co:7001/api/statsdevice/unclaimed";
    protected static final String SERVICE_URI_ROUTE_USERS_ALL = "https://env-staging.treadly.co:7001/api/users/all";
    protected static final String SERVICE_URI_ROUTE_USERS_AUTH_APPLE_TOKEN = "https://env-staging.treadly.co:7001/api/users/auth/apple/token";
    protected static final String SERVICE_URI_ROUTE_USERS_AUTH_FACEBOOK_TOKEN = "https://env-staging.treadly.co:7001/api/users/auth/facebook/token";
    protected static final String SERVICE_URI_ROUTE_USERS_AUTH_INSTAGRAM_TOKEN = "https://env-staging.treadly.co:7001/api/users/auth/instagram/token";
    protected static final String SERVICE_URI_ROUTE_USERS_AVATAR = "https://env-staging.treadly.co:7001/api/users/avatar";
    protected static final String SERVICE_URI_ROUTE_USERS_CHANGEPASSWORD = "https://env-staging.treadly.co:7001/api/users/changepassword";
    protected static final String SERVICE_URI_ROUTE_USERS_CURRENT = "https://env-staging.treadly.co:7001/api/users/current";
    protected static final String SERVICE_URI_ROUTE_USERS_DEVICE = "https://env-staging.treadly.co:7001/api/users/device/%s";
    protected static final String SERVICE_URI_ROUTE_USERS_FOLLOWERS = "https://env-staging.treadly.co:7001/api/users/followers";
    protected static final String SERVICE_URI_ROUTE_USERS_FOLLOWING_MEMBERS = "https://env-staging.treadly.co:7001/api/users/following";
    protected static final String SERVICE_URI_ROUTE_USERS_FOLLOW_REQUEST_ADD = "https://env-staging.treadly.co:7001/api/users/follow";
    protected static final String SERVICE_URI_ROUTE_USERS_FRIENDS = "https://env-staging.treadly.co:7001/api/users/friends";
    protected static final String SERVICE_URI_ROUTE_USERS_FRIEND_DELETE = "https://env-staging.treadly.co:7001/api/users/friend/delete";
    protected static final String SERVICE_URI_ROUTE_USERS_FRIEND_INVITE_APPROVE = "https://env-staging.treadly.co:7001/api/users/friendinvite/approve";
    protected static final String SERVICE_URI_ROUTE_USERS_FRIEND_INVITE_CREATE = "https://env-staging.treadly.co:7001/api/users/friendinvite/create";
    protected static final String SERVICE_URI_ROUTE_USERS_FRIEND_INVITE_DECLINE = "https://env-staging.treadly.co:7001/api/users/friendinvite/decline";
    protected static final String SERVICE_URI_ROUTE_USERS_FRIEND_INVITE_GET = "https://env-staging.treadly.co:7001/api/users/friendinvite/get";
    protected static final String SERVICE_URI_ROUTE_USERS_FRIEND_REQUEST_ADD = "https://env-staging.treadly.co:7001/api/users/friendrequest/add";
    protected static final String SERVICE_URI_ROUTE_USERS_FRIEND_REQUEST_APPROVE = "https://env-staging.treadly.co:7001/api/users/friendrequest/approve";
    protected static final String SERVICE_URI_ROUTE_USERS_FRIEND_REQUEST_DECLINE = "https://env-staging.treadly.co:7001/api/users/friendrequest/decline";
    protected static final String SERVICE_URI_ROUTE_USERS_FRIEND_REQUEST_DELETE = "https://env-staging.treadly.co:7001/api/users/friendrequest/delete";
    protected static final String SERVICE_URI_ROUTE_USERS_FRIEND_REQUEST_RECEIVED = "https://env-staging.treadly.co:7001/api/users/friendrequest/received";
    protected static final String SERVICE_URI_ROUTE_USERS_FRIEND_REQUEST_SENT = "https://env-staging.treadly.co:7001/api/users/friendrequest/sent";
    protected static final String SERVICE_URI_ROUTE_USERS_LOGIN = "https://env-staging.treadly.co:7001/api/users/login";
    protected static final String SERVICE_URI_ROUTE_USERS_ONBOARDED_UPDATE = "https://env-staging.treadly.co:7001/api/users/onboarded";
    protected static final String SERVICE_URI_ROUTE_USERS_REGISTER = "https://env-staging.treadly.co:7001/api/users/register";
    protected static final String SERVICE_URI_ROUTE_USERS_RESET_PASSWORD = "https://env-staging.treadly.co:7001/api/users/resetpassword";
    protected static final String SERVICE_URI_ROUTE_USERS_SEARCH_NAME = "https://env-staging.treadly.co:7001/api/users/search?name=%s";
    protected static final String SERVICE_URI_ROUTE_USERS_SINGLE_ADD = "https://env-staging.treadly.co:7001/api/users/single/add";
    protected static final String SERVICE_URI_ROUTE_USERS_SINGLE_GET = "https://env-staging.treadly.co:7001/api/users/single/get";
    protected static final String SERVICE_URI_ROUTE_USERS_SINGLE_REMOVE = "https://env-staging.treadly.co:7001/api/users/single/remove";
    protected static final String SERVICE_URI_ROUTE_USERS_SINGLE_RESET = "https://env-staging.treadly.co:7001/api/users/single/reset";
    protected static final String SERVICE_URI_ROUTE_USERS_UNFOLLOW = "https://env-staging.treadly.co:7001/api/users/follow/remove";
    protected static final String SERVICE_URI_ROUTE_USERS_USERNAME = "https://env-staging.treadly.co:7001/api/users/username";
    private static final int WEB_REQUEST_THREAD_POOL_THREADS = 16;
    protected static final String defaultAvatarPath = "https://dgwxv5s2i5zkb.cloudfront.net/avatar/default.png";
    protected static final String errorCouldNotCommunicateWithServer = "Could not communicate with server";
    protected static final String errorCouldNotConnectWithServer = "Could not connect with server";
    protected static final String errorCouldNotParseResults = "Could not parse results";
    protected static final String errorEmailAlreadyExist = "Email already exists";
    protected static final String errorEmailFieldIsRequired = "Email field is required";
    protected static final String errorEmailIsInvalid = "Email is invalid";
    protected static final String errorEmailOrPasswordIsInvalid = "Email or password is invalid";
    protected static final String errorMessageNotSent = "Message not sent";
    protected static final String errorNameEmailOrPasswordIsInvalid = "Name, email or password is invalid";
    protected static final String errorPasswordMustBeAtLeast6Characters = "Password must be at least 6 characters";
    protected static final String errorThirdPartyCredentialsAreInvalid = "Third-party credentials are invalid";
    protected static final String errorUserNotFound = "User not found";
    protected static final String errorUsernameAlreadyExists = "Username already exists";
    protected static final String errorUsernameCharacterSize = "Username must be between 2 and 30 characters";
    protected static final String keyAccessToken = "access_token";
    protected static final String keyAge = "age";
    protected static final String keyAvatar = "avatar";
    protected static final String keyBirthdateYear = "birthdate_year";
    protected static final String keyCalories = "calories";
    protected static final String keyCaloriesGoal = "calories_goal";
    protected static final String keyCaloriesGoalCurrent = "current_calories_goal";
    protected static final String keyCaloriesTotal = "calories_total";
    protected static final String keyCity = "city";
    protected static final String keyComment = "comment";
    protected static final String keyCreatedAt = "createdAt";
    protected static final String keyDailyGoalType = "goal_type";
    protected static final String keyData = "data";
    protected static final String keyDate = "date";
    protected static final String keyDay = "day";
    protected static final String keyDayOfWeek = "dayOfWeek";
    protected static final String keyDescription = "description";
    protected static final String keyDetailed = "detailed";
    protected static final String keyDeviceAddress = "device_address";
    protected static final String keyDistance = "distance";
    protected static final String keyDistanceGoal = "distance_goal";
    protected static final String keyDistanceGoalCurrent = "current_distance_goal";
    protected static final String keyDistanceTotal = "distance_total";
    protected static final String keyDuration = "duration";
    protected static final String keyDurationGoal = "duration_goal";
    protected static final String keyDurationGoalCurrent = "current_duration_goal";
    protected static final String keyEmail = "email";
    protected static final String keyError = "error";
    protected static final String keyFilterSnapshots = "filterSnapshots";
    protected static final String keyFinishedAt = "finishedAt";
    protected static final String keyFirstName = "first_name";
    protected static final String keyFriendCount = "friendCount";
    protected static final String keyFriendId = "friend_id";
    protected static final String keyGenType = "gen_type";
    protected static final String keyGender = "gender";
    protected static final String keyGoalType = "goal_type";
    protected static final String keyGoalTypeCurrent = "current_goal_type";
    protected static final String keyHeaderAuthorization = "Authorization";
    protected static final String keyHeight = "height";
    protected static final String keyId = "id";
    protected static final String keyIdentityToken = "identity_token";
    protected static final String keyImage = "image";
    protected static final String keyIsBlocked = "isBlocked";
    protected static final String keyIsFollower = "isFollower";
    protected static final String keyIsFollowing = "isFollowing";
    protected static final String keyIsFriend = "isFriend";
    protected static final String keyJoinedAt = "joinedAt";
    protected static final String keyLastName = "last_name";
    protected static final String keyLogId = "log_id";
    protected static final String keyMonth = "month";
    protected static final String keyName = "name";
    protected static final String keyOnboarded = "onboarded";
    protected static final String keyParticipant = "participant";
    protected static final String keyParticipantId = "participant_id";
    protected static final String keyParticipants = "participants";
    protected static final String keyPassword = "password";
    protected static final String keyPrivate = "private";
    protected static final String keyProfile = "profile";
    protected static final String keyReceiver = "receiver";
    protected static final String keySender = "sender";
    protected static final String keySpeed = "speed";
    protected static final String keyStatsCount = "stats_count";
    protected static final String keySteps = "steps";
    protected static final String keyStepsGoal = "steps_goal";
    protected static final String keyStepsGoalCurrent = "current_steps_goal";
    protected static final String keyStepsTotal = "steps_total";
    protected static final String keySuccess = "success";
    protected static final String keyTimezoneIdentifier = "timezone_identifier";
    protected static final String keyToken = "token";
    protected static final String keyUnits = "units";
    protected static final String keyUser = "user";
    protected static final String keyUserId = "user_id";
    protected static final String keyUsername = "username";
    protected static final String keyUsers = "users";
    protected static final String keyWeight = "weight";
    protected static final String keyYear = "year";
    protected static final String key_Id = "_id";
    private static ExecutorService webThreadPool = Executors.newFixedThreadPool(16);
    private static final OkHttpClient client = new OkHttpClient();

    static void authenticateWithApple(String str, String str2, String str3, TreadlyServiceResponseEventListener treadlyServiceResponseEventListener) {
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public static void register(String str, String str2, String str3, final TreadlyServiceResponseEventListener treadlyServiceResponseEventListener) {
        try {
            JSONObject jSONObject = new JSONObject();
            jSONObject.put("name", str);
            jSONObject.put("email", str2);
            jSONObject.put(keyPassword, str3);
            RequestUtil.shared.postJson(SERVICE_URI_ROUTE_USERS_REGISTER, jSONObject, new RequestUtil.RequestUtilListener() { // from class: com.treadly.Treadly.Data.Managers.-$$Lambda$TreadlyServiceHelper$Zb9mifsKUNcoFXYCujgQpo6eKxs
                @Override // com.treadly.Treadly.Data.Utility.RequestUtil.RequestUtilListener
                public final void onResponse(TreadlyNetworkResponse treadlyNetworkResponse) {
                    TreadlyServiceHelper.lambda$register$7(TreadlyServiceResponseEventListener.this, treadlyNetworkResponse);
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
            treadlyServiceResponseEventListener.onUserInfo("Error with request", null);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static /* synthetic */ void lambda$register$7(final TreadlyServiceResponseEventListener treadlyServiceResponseEventListener, TreadlyNetworkResponse treadlyNetworkResponse) throws JSONException {
        if (treadlyNetworkResponse == null || !treadlyNetworkResponse.ok() || treadlyNetworkResponse.response == null) {
            JSONObject jSONObject = treadlyNetworkResponse.response;
            if (jSONObject != null) {
                final String optString = jSONObject.optString("error");
                String optString2 = jSONObject.optString("email");
                String optString3 = jSONObject.optString(keyPassword);
                final String optString4 = jSONObject.optString("name");
                if (optString != null && (optString.equals(errorEmailAlreadyExist) || optString.equals(errorUsernameAlreadyExists))) {
                    runOnMain(new Runnable() { // from class: com.treadly.Treadly.Data.Managers.-$$Lambda$TreadlyServiceHelper$FNwgeTW36P0GXIL2C5PHb25kYqA
                        @Override // java.lang.Runnable
                        public final void run() {
                            TreadlyServiceResponseEventListener.this.onUserInfo(optString, null);
                        }
                    });
                    return;
                } else if (optString2 != null && optString2.equals(errorEmailIsInvalid)) {
                    runOnMain(new Runnable() { // from class: com.treadly.Treadly.Data.Managers.-$$Lambda$TreadlyServiceHelper$NrTqQzy6CtAexdoLL8ZxREQa3No
                        @Override // java.lang.Runnable
                        public final void run() {
                            TreadlyServiceResponseEventListener.this.onUserInfo(TreadlyServiceHelper.errorEmailIsInvalid, null);
                        }
                    });
                    return;
                } else if (optString3 != null && optString3.equals(errorPasswordMustBeAtLeast6Characters)) {
                    runOnMain(new Runnable() { // from class: com.treadly.Treadly.Data.Managers.-$$Lambda$TreadlyServiceHelper$0RDuvkzf3yuhXzWGy0rEdWiqiYk
                        @Override // java.lang.Runnable
                        public final void run() {
                            TreadlyServiceResponseEventListener.this.onUserInfo(TreadlyServiceHelper.errorPasswordMustBeAtLeast6Characters, null);
                        }
                    });
                    return;
                } else if (optString4 != null) {
                    runOnMain(new Runnable() { // from class: com.treadly.Treadly.Data.Managers.-$$Lambda$TreadlyServiceHelper$X7NTH6e2Y84DSFiyXG05KW5fQXU
                        @Override // java.lang.Runnable
                        public final void run() {
                            TreadlyServiceResponseEventListener.this.onUserInfo(optString4, null);
                        }
                    });
                    return;
                }
            }
            runOnMain(new Runnable() { // from class: com.treadly.Treadly.Data.Managers.-$$Lambda$TreadlyServiceHelper$wZJUutlpOVp-Awm8K8ELKWPSV9E
                @Override // java.lang.Runnable
                public final void run() {
                    TreadlyServiceResponseEventListener.this.onUserInfo(TreadlyServiceHelper.errorCouldNotCommunicateWithServer, null);
                }
            });
            return;
        }
        JSONObject optJSONObject = treadlyNetworkResponse.response.optJSONObject("data");
        if (optJSONObject != null) {
            String optString5 = optJSONObject.optString(key_Id);
            String optString6 = optJSONObject.optString("name");
            String optString7 = optJSONObject.optString("email");
            if (optString5 != null && optString6 != null && optString7 != null) {
                final UserInfo userInfo = new UserInfo(optString5, optString6, optString7);
                runOnMain(new Runnable() { // from class: com.treadly.Treadly.Data.Managers.-$$Lambda$TreadlyServiceHelper$htM0FQQo5Fa5oCJvf5ClSNQ-d24
                    @Override // java.lang.Runnable
                    public final void run() {
                        TreadlyServiceResponseEventListener.this.onUserInfo(null, userInfo);
                    }
                });
                return;
            }
        }
        runOnMain(new Runnable() { // from class: com.treadly.Treadly.Data.Managers.-$$Lambda$TreadlyServiceHelper$587edYDu0Sl-4t88mzwg8UUtXt4
            @Override // java.lang.Runnable
            public final void run() {
                TreadlyServiceResponseEventListener.this.onUserInfo(TreadlyServiceHelper.errorCouldNotCommunicateWithServer, null);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static void login(String str, String str2, final TreadlyServiceResponseEventListener treadlyServiceResponseEventListener) {
        try {
            JSONObject jSONObject = new JSONObject();
            jSONObject.put("email", str);
            jSONObject.put(keyPassword, str2);
            RequestUtil.shared.postJson(SERVICE_URI_ROUTE_USERS_LOGIN, jSONObject, new RequestUtil.RequestUtilListener() { // from class: com.treadly.Treadly.Data.Managers.-$$Lambda$TreadlyServiceHelper$6m1NQWhI-liMCoC33eoVh7qONV8
                @Override // com.treadly.Treadly.Data.Utility.RequestUtil.RequestUtilListener
                public final void onResponse(TreadlyNetworkResponse treadlyNetworkResponse) {
                    TreadlyServiceHelper.lambda$login$12(TreadlyServiceResponseEventListener.this, treadlyNetworkResponse);
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
            treadlyServiceResponseEventListener.onUserInfoProfile("Error", null, false, false);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static /* synthetic */ void lambda$login$12(final TreadlyServiceResponseEventListener treadlyServiceResponseEventListener, TreadlyNetworkResponse treadlyNetworkResponse) throws JSONException {
        if (treadlyNetworkResponse == null || !treadlyNetworkResponse.ok() || treadlyNetworkResponse.response == null) {
            if (treadlyNetworkResponse == null || treadlyNetworkResponse.response != null) {
                runOnMain(new Runnable() { // from class: com.treadly.Treadly.Data.Managers.-$$Lambda$TreadlyServiceHelper$9xy7dHWKGxwOEY9mTG3onQyV5gk
                    @Override // java.lang.Runnable
                    public final void run() {
                        TreadlyServiceResponseEventListener.this.onUserTokenInfoProfile(TreadlyServiceHelper.errorEmailOrPasswordIsInvalid, null, false, false);
                    }
                });
                return;
            } else {
                runOnMain(new Runnable() { // from class: com.treadly.Treadly.Data.Managers.-$$Lambda$TreadlyServiceHelper$jsywErzwL9D-CQshEMcp79dHZxQ
                    @Override // java.lang.Runnable
                    public final void run() {
                        TreadlyServiceResponseEventListener.this.onUserTokenInfoProfile(TreadlyServiceHelper.errorCouldNotCommunicateWithServer, null, false, false);
                    }
                });
                return;
            }
        }
        JSONObject optJSONObject = treadlyNetworkResponse.response.optJSONObject("data");
        if (optJSONObject != null) {
            String optString = optJSONObject.optString("user");
            String optString2 = optJSONObject.optString(keyToken);
            final boolean optBoolean = optJSONObject.optBoolean("profile");
            final boolean optBoolean2 = optJSONObject.optBoolean(keyOnboarded);
            if (optString != null && optString2 != null && optJSONObject.has("profile")) {
                final UserTokenInfo userTokenInfo = new UserTokenInfo(optString, optString2);
                runOnMain(new Runnable() { // from class: com.treadly.Treadly.Data.Managers.-$$Lambda$TreadlyServiceHelper$yXLwSs_V0IelvQwKZQQDQH76G3M
                    @Override // java.lang.Runnable
                    public final void run() {
                        TreadlyServiceResponseEventListener.this.onUserTokenInfoProfile(null, userTokenInfo, optBoolean, optBoolean2);
                    }
                });
                return;
            }
        }
        runOnMain(new Runnable() { // from class: com.treadly.Treadly.Data.Managers.-$$Lambda$TreadlyServiceHelper$6n_AA3fAEJQSsBQFzsduxz0WjOw
            @Override // java.lang.Runnable
            public final void run() {
                TreadlyServiceResponseEventListener.this.onUserTokenInfoProfile(TreadlyServiceHelper.errorCouldNotCommunicateWithServer, null, false, false);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static void getCurrentUserInfo(String str, final TreadlyServiceResponseEventListener treadlyServiceResponseEventListener) {
        try {
            JSONObject jSONObject = new JSONObject();
            jSONObject.put(keyHeaderAuthorization, str);
            RequestUtil.shared.getJson(SERVICE_URI_ROUTE_USERS_CURRENT, jSONObject, new RequestUtil.RequestUtilListener() { // from class: com.treadly.Treadly.Data.Managers.TreadlyServiceHelper.1
                @Override // com.treadly.Treadly.Data.Utility.RequestUtil.RequestUtilListener
                public void onResponse(TreadlyNetworkResponse treadlyNetworkResponse) {
                    if (treadlyNetworkResponse == null || !treadlyNetworkResponse.ok() || treadlyNetworkResponse.response == null) {
                        TreadlyServiceHelper.runOnMain(new Runnable() { // from class: com.treadly.Treadly.Data.Managers.TreadlyServiceHelper.1.1
                            @Override // java.lang.Runnable
                            public void run() {
                                TreadlyServiceResponseEventListener.this.onUserInfo(TreadlyServiceHelper.errorCouldNotCommunicateWithServer, null);
                            }
                        });
                        return;
                    }
                    JSONObject optJSONObject = treadlyNetworkResponse.response.optJSONObject("data");
                    if (optJSONObject != null) {
                        String optString = optJSONObject.optString(TreadlyServiceHelper.key_Id);
                        String optString2 = optJSONObject.optString("name");
                        if (optString != null && optString2 != null) {
                            final UserInfo userInfo = new UserInfo(optString, optString2, optJSONObject.optString("email", ""));
                            String optString3 = optJSONObject.optString("avatar", "");
                            if (!optString3.isEmpty()) {
                                userInfo.avatarPath = optString3;
                            } else {
                                userInfo.avatarPath = "https://dgwxv5s2i5zkb.cloudfront.net/avatar/default.png";
                            }
                            TreadlyServiceHelper.runOnMain(new Runnable() { // from class: com.treadly.Treadly.Data.Managers.TreadlyServiceHelper.1.2
                                @Override // java.lang.Runnable
                                public void run() {
                                    TreadlyServiceResponseEventListener.this.onUserInfo(null, userInfo);
                                }
                            });
                            return;
                        }
                    }
                    TreadlyServiceHelper.runOnMain(new Runnable() { // from class: com.treadly.Treadly.Data.Managers.TreadlyServiceHelper.1.3
                        @Override // java.lang.Runnable
                        public void run() {
                            TreadlyServiceResponseEventListener.this.onUserInfo(TreadlyServiceHelper.errorCouldNotCommunicateWithServer, null);
                        }
                    });
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
            runOnMain(new Runnable() { // from class: com.treadly.Treadly.Data.Managers.TreadlyServiceHelper.2
                @Override // java.lang.Runnable
                public void run() {
                    TreadlyServiceResponseEventListener.this.onUserInfo(TreadlyServiceHelper.errorCouldNotCommunicateWithServer, null);
                }
            });
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static void getCurrentUserFriendsInfo(String str, final TreadlyServiceResponseEventListener treadlyServiceResponseEventListener) throws JSONException {
        JSONObject jSONObject = new JSONObject();
        jSONObject.put(keyHeaderAuthorization, str);
        RequestUtil.shared.getJson(SERVICE_URI_ROUTE_USERS_FRIENDS, jSONObject, new RequestUtil.RequestUtilListener() { // from class: com.treadly.Treadly.Data.Managers.TreadlyServiceHelper.3
            @Override // com.treadly.Treadly.Data.Utility.RequestUtil.RequestUtilListener
            public void onResponse(TreadlyNetworkResponse treadlyNetworkResponse) throws JSONException {
                if (treadlyNetworkResponse == null || !treadlyNetworkResponse.ok() || treadlyNetworkResponse.response == null) {
                    TreadlyServiceHelper.runOnMain(new Runnable() { // from class: com.treadly.Treadly.Data.Managers.TreadlyServiceHelper.3.1
                        @Override // java.lang.Runnable
                        public void run() {
                            TreadlyServiceResponseEventListener.this.onUserInfo(TreadlyServiceHelper.errorCouldNotCommunicateWithServer, null);
                        }
                    });
                    return;
                }
                JSONArray jSONArray = treadlyNetworkResponse.response.getJSONArray("data");
                if (jSONArray != null) {
                    final ArrayList arrayList = new ArrayList();
                    for (int i = 0; i < jSONArray.length(); i++) {
                        JSONObject optJSONObject = jSONArray.optJSONObject(i).optJSONObject("user");
                        if (optJSONObject != null) {
                            UserInfo userInfo = new UserInfo(optJSONObject.optString(TreadlyServiceHelper.key_Id), optJSONObject.optString("name"), "");
                            if (optJSONObject.optString("avatar") == null || optJSONObject.optString("avatar").isEmpty()) {
                                userInfo.avatarPath = "https://dgwxv5s2i5zkb.cloudfront.net/avatar/default.png";
                            } else {
                                userInfo.avatarPath = optJSONObject.optString("avatar");
                            }
                            arrayList.add(userInfo);
                        }
                    }
                    TreadlyServiceHelper.runOnMain(new Runnable() { // from class: com.treadly.Treadly.Data.Managers.TreadlyServiceHelper.3.2
                        @Override // java.lang.Runnable
                        public void run() {
                            try {
                                TreadlyServiceResponseEventListener.this.onUserFriendsInfo(null, arrayList);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    });
                    return;
                }
                TreadlyServiceHelper.runOnMain(new Runnable() { // from class: com.treadly.Treadly.Data.Managers.TreadlyServiceHelper.3.3
                    @Override // java.lang.Runnable
                    public void run() {
                        try {
                            TreadlyServiceResponseEventListener.this.onUserFriendsInfo(TreadlyServiceHelper.errorCouldNotCommunicateWithServer, null);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static void authenticateWithFacebook(String str, TreadlyServiceResponseEventListener treadlyServiceResponseEventListener) {
        requestAuthenticateWithThirdParty(SERVICE_URI_ROUTE_USERS_AUTH_FACEBOOK_TOKEN, str, treadlyServiceResponseEventListener);
    }

    static void authenticateWithInstagram(String str, TreadlyServiceResponseEventListener treadlyServiceResponseEventListener) {
        requestAuthenticateWithThirdParty(SERVICE_URI_ROUTE_USERS_AUTH_INSTAGRAM_TOKEN, str, treadlyServiceResponseEventListener);
    }

    private static void requestAuthenticateWithThirdParty(String str, String str2, final TreadlyServiceResponseEventListener treadlyServiceResponseEventListener) {
        try {
            JSONObject jSONObject = new JSONObject();
            jSONObject.put("access_token", str2);
            RequestUtil.shared.postJson(str, jSONObject, new RequestUtil.RequestUtilListener() { // from class: com.treadly.Treadly.Data.Managers.-$$Lambda$TreadlyServiceHelper$VGktmeQIxGTea15VAfuQ5n28-cM
                @Override // com.treadly.Treadly.Data.Utility.RequestUtil.RequestUtilListener
                public final void onResponse(TreadlyNetworkResponse treadlyNetworkResponse) {
                    TreadlyServiceHelper.lambda$requestAuthenticateWithThirdParty$17(TreadlyServiceResponseEventListener.this, treadlyNetworkResponse);
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
            runOnMain(new Runnable() { // from class: com.treadly.Treadly.Data.Managers.-$$Lambda$TreadlyServiceHelper$FW5-jbTJGpGPfe5hSUGCUdSo-2E
                @Override // java.lang.Runnable
                public final void run() {
                    TreadlyServiceResponseEventListener.this.onUserTokenInfoProfile(TreadlyServiceHelper.errorCouldNotCommunicateWithServer, null, false, false);
                }
            });
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static /* synthetic */ void lambda$requestAuthenticateWithThirdParty$17(final TreadlyServiceResponseEventListener treadlyServiceResponseEventListener, TreadlyNetworkResponse treadlyNetworkResponse) throws JSONException {
        if (treadlyNetworkResponse == null || !treadlyNetworkResponse.ok() || treadlyNetworkResponse.response == null) {
            runOnMain(new Runnable() { // from class: com.treadly.Treadly.Data.Managers.-$$Lambda$TreadlyServiceHelper$VUwHnEOISf6u7JyIxQWJTpddvh8
                @Override // java.lang.Runnable
                public final void run() {
                    TreadlyServiceResponseEventListener.this.onUserTokenInfoProfile(TreadlyServiceHelper.errorCouldNotConnectWithServer, null, false, false);
                }
            });
        } else if (treadlyNetworkResponse.response.optString("error").length() != 0) {
            runOnMain(new Runnable() { // from class: com.treadly.Treadly.Data.Managers.-$$Lambda$TreadlyServiceHelper$G_DaYrGB92CaQ69fA75XgCjN-_4
                @Override // java.lang.Runnable
                public final void run() {
                    TreadlyServiceResponseEventListener.this.onUserTokenInfoProfile(TreadlyServiceHelper.errorThirdPartyCredentialsAreInvalid, null, false, false);
                }
            });
        } else {
            JSONObject optJSONObject = treadlyNetworkResponse.response.optJSONObject("data");
            if (optJSONObject != null) {
                String optString = optJSONObject.optString("user");
                String optString2 = optJSONObject.optString(keyToken);
                if (optString != null && optString2 != null && optJSONObject.has("profile")) {
                    final boolean optBoolean = optJSONObject.optBoolean("profile");
                    final boolean optBoolean2 = optJSONObject.optBoolean(keyOnboarded);
                    final UserTokenInfo userTokenInfo = new UserTokenInfo(optString, optString2);
                    runOnMain(new Runnable() { // from class: com.treadly.Treadly.Data.Managers.-$$Lambda$TreadlyServiceHelper$xGb3L-o3SXzTd2hFjHe0IkfC8Bs
                        @Override // java.lang.Runnable
                        public final void run() {
                            TreadlyServiceResponseEventListener.this.onUserTokenInfoProfile("", userTokenInfo, optBoolean, optBoolean2);
                        }
                    });
                    return;
                }
            }
            runOnMain(new Runnable() { // from class: com.treadly.Treadly.Data.Managers.-$$Lambda$TreadlyServiceHelper$Ry8JoZLhKW1jLx9wwCvrCSknr-w
                @Override // java.lang.Runnable
                public final void run() {
                    TreadlyServiceResponseEventListener.this.onUserTokenInfoProfile(TreadlyServiceHelper.errorCouldNotCommunicateWithServer, null, false, false);
                }
            });
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public static void resetPassword(String str, final TreadlyServiceResponseEventListener treadlyServiceResponseEventListener) {
        try {
            JSONObject jSONObject = new JSONObject();
            jSONObject.put("email", str);
            RequestUtil.shared.postJson(SERVICE_URI_ROUTE_USERS_RESET_PASSWORD, jSONObject, new RequestUtil.RequestUtilListener() { // from class: com.treadly.Treadly.Data.Managers.-$$Lambda$TreadlyServiceHelper$011awtG-jC9RRP01IhmNmxbNXr4
                @Override // com.treadly.Treadly.Data.Utility.RequestUtil.RequestUtilListener
                public final void onResponse(TreadlyNetworkResponse treadlyNetworkResponse) {
                    TreadlyServiceHelper.lambda$resetPassword$25(TreadlyServiceResponseEventListener.this, treadlyNetworkResponse);
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
            runOnMain(new Runnable() { // from class: com.treadly.Treadly.Data.Managers.-$$Lambda$TreadlyServiceHelper$V2X-R2UQNsc-mHv7eYXzl7ctk7c
                @Override // java.lang.Runnable
                public final void run() {
                    TreadlyServiceResponseEventListener.this.onUserTokenInfoProfile(TreadlyServiceHelper.errorCouldNotCommunicateWithServer, null, false, false);
                }
            });
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static /* synthetic */ void lambda$resetPassword$25(final TreadlyServiceResponseEventListener treadlyServiceResponseEventListener, TreadlyNetworkResponse treadlyNetworkResponse) throws JSONException {
        if (treadlyNetworkResponse == null || !treadlyNetworkResponse.ok() || treadlyNetworkResponse.response == null) {
            JSONObject jSONObject = treadlyNetworkResponse.response;
            if (jSONObject != null) {
                final String optString = jSONObject.optString("error");
                String optString2 = jSONObject.optString("email");
                if (optString != null && optString.equals(errorMessageNotSent)) {
                    runOnMain(new Runnable() { // from class: com.treadly.Treadly.Data.Managers.-$$Lambda$TreadlyServiceHelper$PwPhz7A-DSLoj8LaOfF3kpgcX3E
                        @Override // java.lang.Runnable
                        public final void run() {
                            TreadlyServiceResponseEventListener.this.onSuccess(optString);
                        }
                    });
                    return;
                } else if (optString2 != null) {
                    if (optString2.equals(errorEmailFieldIsRequired) || optString2.equals(errorEmailIsInvalid)) {
                        runOnMain(new Runnable() { // from class: com.treadly.Treadly.Data.Managers.-$$Lambda$TreadlyServiceHelper$RcW7nEWTrX1_kbhhmkcRAISHMrk
                            @Override // java.lang.Runnable
                            public final void run() {
                                TreadlyServiceResponseEventListener.this.onSuccess(TreadlyServiceHelper.errorUserNotFound);
                            }
                        });
                        return;
                    } else if (optString2.equals(errorUserNotFound)) {
                        runOnMain(new Runnable() { // from class: com.treadly.Treadly.Data.Managers.-$$Lambda$TreadlyServiceHelper$6qjLjfJ8-n9ofASCsINRHYDiAII
                            @Override // java.lang.Runnable
                            public final void run() {
                                TreadlyServiceResponseEventListener.this.onSuccess(TreadlyServiceHelper.errorUserNotFound);
                            }
                        });
                        return;
                    }
                }
            }
            runOnMain(new Runnable() { // from class: com.treadly.Treadly.Data.Managers.-$$Lambda$TreadlyServiceHelper$F6Ku_EW4dLXEY4KVW915YQLdJ7g
                @Override // java.lang.Runnable
                public final void run() {
                    TreadlyServiceResponseEventListener.this.onSuccess(TreadlyServiceHelper.errorCouldNotConnectWithServer);
                }
            });
            return;
        }
        JSONObject optJSONObject = treadlyNetworkResponse.response.optJSONObject("data");
        if (optJSONObject != null && optJSONObject.optBoolean("success", false)) {
            runOnMain(new Runnable() { // from class: com.treadly.Treadly.Data.Managers.-$$Lambda$TreadlyServiceHelper$ndBmzcCOGWeJwIYTV8_zn5OEhgw
                @Override // java.lang.Runnable
                public final void run() {
                    TreadlyServiceResponseEventListener.this.onSuccess(null);
                }
            });
        } else {
            runOnMain(new Runnable() { // from class: com.treadly.Treadly.Data.Managers.-$$Lambda$TreadlyServiceHelper$3gIwsvjP08mKIAF7njmhXYAGQm8
                @Override // java.lang.Runnable
                public final void run() {
                    TreadlyServiceResponseEventListener.this.onSuccess(TreadlyServiceHelper.errorCouldNotCommunicateWithServer);
                }
            });
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public static void changeUsername(String str, String str2, final TreadlyServiceResponseEventListener treadlyServiceResponseEventListener) {
        try {
            JSONObject jSONObject = new JSONObject();
            jSONObject.put(keyHeaderAuthorization, str2);
            JSONObject jSONObject2 = new JSONObject();
            jSONObject2.put("name", str);
            RequestUtil.shared.postJson(SERVICE_URI_ROUTE_USERS_USERNAME, jSONObject2, jSONObject, new RequestUtil.RequestUtilListener() { // from class: com.treadly.Treadly.Data.Managers.TreadlyServiceHelper.4
                @Override // com.treadly.Treadly.Data.Utility.RequestUtil.RequestUtilListener
                public void onResponse(TreadlyNetworkResponse treadlyNetworkResponse) {
                    final String optString;
                    if (treadlyNetworkResponse == null || !treadlyNetworkResponse.ok() || treadlyNetworkResponse.response == null) {
                        if (treadlyNetworkResponse != null && treadlyNetworkResponse.response != null && (optString = treadlyNetworkResponse.response.optString("error")) != null && treadlyNetworkResponse.statusCode == 400) {
                            TreadlyServiceHelper.runOnMain(new Runnable() { // from class: com.treadly.Treadly.Data.Managers.TreadlyServiceHelper.4.1
                                @Override // java.lang.Runnable
                                public void run() {
                                    TreadlyServiceResponseEventListener.this.onSuccess(optString);
                                }
                            });
                            return;
                        } else {
                            TreadlyServiceHelper.runOnMain(new Runnable() { // from class: com.treadly.Treadly.Data.Managers.TreadlyServiceHelper.4.2
                                @Override // java.lang.Runnable
                                public void run() {
                                    TreadlyServiceResponseEventListener.this.onSuccess(TreadlyServiceHelper.errorCouldNotConnectWithServer);
                                }
                            });
                            return;
                        }
                    }
                    JSONObject optJSONObject = treadlyNetworkResponse.response.optJSONObject("data");
                    if (optJSONObject != null && optJSONObject.optBoolean("success", false)) {
                        TreadlyServiceHelper.runOnMain(new Runnable() { // from class: com.treadly.Treadly.Data.Managers.TreadlyServiceHelper.4.3
                            @Override // java.lang.Runnable
                            public void run() {
                                TreadlyServiceResponseEventListener.this.onSuccess(null);
                            }
                        });
                    } else {
                        TreadlyServiceHelper.runOnMain(new Runnable() { // from class: com.treadly.Treadly.Data.Managers.TreadlyServiceHelper.4.4
                            @Override // java.lang.Runnable
                            public void run() {
                                TreadlyServiceResponseEventListener.this.onSuccess(TreadlyServiceHelper.errorCouldNotCommunicateWithServer);
                            }
                        });
                    }
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
            runOnMain(new Runnable() { // from class: com.treadly.Treadly.Data.Managers.TreadlyServiceHelper.5
                @Override // java.lang.Runnable
                public void run() {
                    TreadlyServiceResponseEventListener.this.onSuccess(TreadlyServiceHelper.errorCouldNotCommunicateWithServer);
                }
            });
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public static void changePassword(String str, String str2, final TreadlyServiceResponseEventListener treadlyServiceResponseEventListener) {
        try {
            JSONObject jSONObject = new JSONObject();
            jSONObject.put(keyHeaderAuthorization, str2);
            JSONObject jSONObject2 = new JSONObject();
            jSONObject2.put(keyPassword, str);
            RequestUtil.shared.postJson(SERVICE_URI_ROUTE_USERS_CHANGEPASSWORD, jSONObject2, jSONObject, new RequestUtil.RequestUtilListener() { // from class: com.treadly.Treadly.Data.Managers.TreadlyServiceHelper.6
                @Override // com.treadly.Treadly.Data.Utility.RequestUtil.RequestUtilListener
                public void onResponse(TreadlyNetworkResponse treadlyNetworkResponse) {
                    if (treadlyNetworkResponse == null || !treadlyNetworkResponse.ok() || treadlyNetworkResponse.response == null) {
                        if (treadlyNetworkResponse.response != null) {
                            JSONObject jSONObject3 = treadlyNetworkResponse.response;
                            if (jSONObject3.optString(TreadlyServiceHelper.keyPassword) != null) {
                                final String optString = jSONObject3.optString(TreadlyServiceHelper.keyPassword);
                                if (optString.equals(TreadlyServiceHelper.errorPasswordMustBeAtLeast6Characters)) {
                                    TreadlyServiceHelper.runOnMain(new Runnable() { // from class: com.treadly.Treadly.Data.Managers.TreadlyServiceHelper.6.1
                                        @Override // java.lang.Runnable
                                        public void run() {
                                            TreadlyServiceResponseEventListener.this.onSuccess(optString);
                                        }
                                    });
                                    return;
                                }
                            }
                        }
                        TreadlyServiceHelper.runOnMain(new Runnable() { // from class: com.treadly.Treadly.Data.Managers.TreadlyServiceHelper.6.2
                            @Override // java.lang.Runnable
                            public void run() {
                                TreadlyServiceResponseEventListener.this.onSuccess(TreadlyServiceHelper.errorCouldNotCommunicateWithServer);
                            }
                        });
                        return;
                    }
                    JSONObject jSONObject4 = treadlyNetworkResponse.response;
                    if (jSONObject4.optJSONObject("data") != null && jSONObject4.optJSONObject("data").optBoolean("success")) {
                        TreadlyServiceHelper.runOnMain(new Runnable() { // from class: com.treadly.Treadly.Data.Managers.TreadlyServiceHelper.6.3
                            @Override // java.lang.Runnable
                            public void run() {
                                TreadlyServiceResponseEventListener.this.onSuccess(null);
                            }
                        });
                    } else {
                        TreadlyServiceHelper.runOnMain(new Runnable() { // from class: com.treadly.Treadly.Data.Managers.TreadlyServiceHelper.6.4
                            @Override // java.lang.Runnable
                            public void run() {
                                TreadlyServiceResponseEventListener.this.onSuccess(TreadlyServiceHelper.errorCouldNotCommunicateWithServer);
                            }
                        });
                    }
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public static void updateUserProfileDescription(String str, String str2, TreadlyServiceResponseEventListener treadlyServiceResponseEventListener) {
        JSONObject jSONObject = new JSONObject();
        try {
            jSONObject.put("description", str);
        } catch (Exception e) {
            e.printStackTrace();
        }
        requestBasicAuthPost(SERVICE_URI_ROUTE_PROFILE, str2, jSONObject, treadlyServiceResponseEventListener);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static void getUserProfileInfo(String str, String str2, int i, final TreadlyServiceResponseEventListener treadlyServiceResponseEventListener) {
        String profileUserIDUri = getProfileUserIDUri(str2, i);
        JSONObject jSONObject = new JSONObject();
        try {
            jSONObject.put(keyHeaderAuthorization, str);
        } catch (Exception e) {
            e.printStackTrace();
        }
        RequestUtil.shared.getJson(profileUserIDUri, jSONObject, new RequestUtil.RequestUtilListener() { // from class: com.treadly.Treadly.Data.Managers.-$$Lambda$TreadlyServiceHelper$8yys1q1koJYazeyYffLUPsl7c1o
            @Override // com.treadly.Treadly.Data.Utility.RequestUtil.RequestUtilListener
            public final void onResponse(TreadlyNetworkResponse treadlyNetworkResponse) {
                TreadlyServiceHelper.lambda$getUserProfileInfo$30(TreadlyServiceResponseEventListener.this, treadlyNetworkResponse);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static /* synthetic */ void lambda$getUserProfileInfo$30(final TreadlyServiceResponseEventListener treadlyServiceResponseEventListener, TreadlyNetworkResponse treadlyNetworkResponse) throws JSONException {
        if (!treadlyNetworkResponse.ok() || treadlyNetworkResponse.response == null) {
            runOnMain(new Runnable() { // from class: com.treadly.Treadly.Data.Managers.-$$Lambda$TreadlyServiceHelper$EYKLmWwOo5E02SyOdXMR8UKQ0QA
                @Override // java.lang.Runnable
                public final void run() {
                    TreadlyServiceResponseEventListener.this.onSuccess(TreadlyServiceHelper.errorCouldNotCommunicateWithServer);
                }
            });
            return;
        }
        JSONObject optJSONObject = treadlyNetworkResponse.response.optJSONObject("data");
        JSONObject optJSONObject2 = optJSONObject.optJSONObject("user");
        String optString = optJSONObject2.optString("name");
        int optInt = optJSONObject.optInt(keyAge);
        double optDouble = optJSONObject.optDouble(keyWeight);
        double optDouble2 = optJSONObject.optDouble("height");
        String optString2 = optJSONObject.optString(keyGender);
        String optString3 = optJSONObject.optString("description");
        String optString4 = optJSONObject.optString(keyCity);
        int optInt2 = optJSONObject.optInt(keyCaloriesGoal);
        int optInt3 = optJSONObject.optInt(keyCaloriesGoalCurrent, -1);
        int optInt4 = optJSONObject.optInt(keyStepsGoal);
        int optInt5 = optJSONObject.optInt(keyStepsGoalCurrent, -1);
        double optDouble3 = optJSONObject.optDouble(keyDistanceGoal);
        double optDouble4 = optJSONObject.optDouble(keyDistanceGoalCurrent, -1.0d);
        double optDouble5 = optJSONObject.optDouble(keyDurationGoal);
        double optDouble6 = optJSONObject.optDouble(keyDurationGoalCurrent, -1.0d);
        int optInt6 = optJSONObject.optInt(keyCaloriesTotal);
        int optInt7 = optJSONObject.optInt(keyStepsTotal);
        double optDouble7 = optJSONObject.optDouble(keyDistanceTotal);
        int optInt8 = optJSONObject.optInt(keyUnits);
        int optInt9 = optJSONObject.optInt("goal_type");
        int optInt10 = optJSONObject.optInt(keyGoalTypeCurrent);
        int optInt11 = optJSONObject.optInt(keyBirthdateYear);
        String optString5 = optJSONObject.optString(keyTimezoneIdentifier);
        boolean optBoolean = optJSONObject.optBoolean(keyIsFriend);
        boolean optBoolean2 = optJSONObject.optBoolean(keyIsBlocked);
        int optInt12 = optJSONObject.optInt(keyFriendCount);
        String optString6 = optJSONObject.optString(keyDeviceAddress);
        String str = optString6.toLowerCase().equals("null") ? "" : optString6;
        if (optJSONObject2 != null && optString != null && optJSONObject.has(keyAge) && optJSONObject.has(keyWeight) && optJSONObject.has("height") && optString2 != null && optString3 != null && optString4 != null && optJSONObject.has("goal_type") && optJSONObject.has(keyCaloriesGoal) && optJSONObject.has(keyStepsGoal) && optJSONObject.has(keyDistanceGoal) && optJSONObject.has(keyDurationGoal) && optJSONObject.has(keyCaloriesTotal) && optJSONObject.has(keyStepsTotal) && optJSONObject.has(keyDistanceTotal) && optJSONObject.has(keyUnits) && optJSONObject.has(keyBirthdateYear) && optString5 != null && optJSONObject.has(keyIsFriend) && optJSONObject.has(keyIsFriend) && optJSONObject.has(keyFriendCount)) {
            final UserProfileInfo userProfileInfo = new UserProfileInfo(optString, optInt, optDouble, optDouble2, optString2, optString3, optString4, UserDailyGoalType.fromValue(optInt9), UserDailyGoalType.fromValue(optInt10), optInt2, optInt3, optInt4, optInt5, optDouble3, optDouble4, optDouble5, optDouble6, optInt6, optInt7, optDouble7, optInt11, optString5, DistanceUnits.fromValue(optInt8), str);
            userProfileInfo.friendsCount = optInt12;
            userProfileInfo.isFriend = optBoolean;
            userProfileInfo.isBlocked = optBoolean2;
            runOnMain(new Runnable() { // from class: com.treadly.Treadly.Data.Managers.-$$Lambda$TreadlyServiceHelper$vI1H08wcscPT3Rd3IgdxOiTzKDA
                @Override // java.lang.Runnable
                public final void run() {
                    TreadlyServiceResponseEventListener.this.onUserProfile(null, userProfileInfo);
                }
            });
            return;
        }
        runOnMain(new Runnable() { // from class: com.treadly.Treadly.Data.Managers.-$$Lambda$TreadlyServiceHelper$bXNGqOvHcjIjN1G-3LHKmDJUzlw
            @Override // java.lang.Runnable
            public final void run() {
                TreadlyServiceResponseEventListener.this.onUserProfile(TreadlyServiceHelper.errorCouldNotCommunicateWithServer, null);
            }
        });
    }

    public static void getUserAll(String str, String str2, final TreadlyServiceResponseEventListener treadlyServiceResponseEventListener) {
        String userSearchByNameUri = getUserSearchByNameUri(str2);
        JSONObject jSONObject = new JSONObject();
        try {
            jSONObject.put(keyHeaderAuthorization, str);
            RequestUtil.shared.getJson(userSearchByNameUri, jSONObject, new RequestUtil.RequestUtilListener() { // from class: com.treadly.Treadly.Data.Managers.TreadlyServiceHelper.7
                @Override // com.treadly.Treadly.Data.Utility.RequestUtil.RequestUtilListener
                public void onResponse(TreadlyNetworkResponse treadlyNetworkResponse) throws JSONException {
                    JSONArray jSONArray;
                    if (!treadlyNetworkResponse.ok() || treadlyNetworkResponse.response == null) {
                        TreadlyServiceHelper.runOnMain(new Runnable() { // from class: com.treadly.Treadly.Data.Managers.TreadlyServiceHelper.7.1
                            @Override // java.lang.Runnable
                            public void run() {
                                try {
                                    TreadlyServiceResponseEventListener.this.onUserFriendsInfo(TreadlyServiceHelper.errorCouldNotCommunicateWithServer, null);
                                } catch (JSONException e) {
                                    Log.e("ServiceManager", e.getMessage());
                                    e.printStackTrace();
                                }
                            }
                        });
                        return;
                    }
                    try {
                        jSONArray = treadlyNetworkResponse.response.getJSONArray("data");
                    } catch (JSONException unused) {
                        TreadlyServiceHelper.runOnMain(new Runnable() { // from class: com.treadly.Treadly.Data.Managers.TreadlyServiceHelper.7.2
                            @Override // java.lang.Runnable
                            public void run() {
                                try {
                                    TreadlyServiceResponseEventListener.this.onUserFriendsInfo(TreadlyServiceHelper.errorCouldNotCommunicateWithServer, null);
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        });
                        jSONArray = null;
                    }
                    final ArrayList arrayList = new ArrayList();
                    for (int i = 0; i < jSONArray.length(); i++) {
                        JSONObject jSONObject2 = jSONArray.getJSONObject(i);
                        String optString = jSONObject2.optString(TreadlyServiceHelper.key_Id);
                        String optString2 = jSONObject2.optString("name");
                        String optString3 = jSONObject2.optString("avatar");
                        UserInfo userInfo = new UserInfo(optString, optString2, "");
                        if (optString3.isEmpty()) {
                            optString3 = "https://dgwxv5s2i5zkb.cloudfront.net/avatar/default.png";
                        }
                        userInfo.avatarPath = optString3;
                        arrayList.add(userInfo);
                    }
                    TreadlyServiceHelper.runOnMain(new Runnable() { // from class: com.treadly.Treadly.Data.Managers.TreadlyServiceHelper.7.3
                        @Override // java.lang.Runnable
                        public void run() {
                            try {
                                TreadlyServiceResponseEventListener.this.onUserFriendsInfo(null, arrayList);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    });
                }
            });
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public static void addFriendRequest(String str, String str2, TreadlyServiceResponseEventListener treadlyServiceResponseEventListener) {
        JSONObject jSONObject = new JSONObject();
        try {
            jSONObject.put(keyUsers, str2);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        requestBasicAuthPost(SERVICE_URI_ROUTE_USERS_FRIEND_REQUEST_ADD, str, jSONObject, treadlyServiceResponseEventListener);
    }

    public static void createFriendInviteToken(String str, final TreadlyServiceResponseEventListener treadlyServiceResponseEventListener) {
        JSONObject jSONObject = new JSONObject();
        try {
            jSONObject.put(keyHeaderAuthorization, str);
            RequestUtil.shared.postJson(SERVICE_URI_ROUTE_USERS_FRIEND_INVITE_CREATE, null, jSONObject, new RequestUtil.RequestUtilListener() { // from class: com.treadly.Treadly.Data.Managers.-$$Lambda$TreadlyServiceHelper$JTydPcSegLff1pW2KkcmZdWEB-I
                @Override // com.treadly.Treadly.Data.Utility.RequestUtil.RequestUtilListener
                public final void onResponse(TreadlyNetworkResponse treadlyNetworkResponse) {
                    TreadlyServiceHelper.lambda$createFriendInviteToken$34(TreadlyServiceResponseEventListener.this, treadlyNetworkResponse);
                }
            });
        } catch (JSONException e) {
            e.printStackTrace();
            treadlyServiceResponseEventListener.onCreateFriendInviteToken("Error: Token issue", null);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static /* synthetic */ void lambda$createFriendInviteToken$34(final TreadlyServiceResponseEventListener treadlyServiceResponseEventListener, TreadlyNetworkResponse treadlyNetworkResponse) throws JSONException {
        if (!treadlyNetworkResponse.ok() || treadlyNetworkResponse.response == null) {
            runOnMain(new Runnable() { // from class: com.treadly.Treadly.Data.Managers.-$$Lambda$TreadlyServiceHelper$JZOxAUmYsVVyS7Y0g4la-DSbnhs
                @Override // java.lang.Runnable
                public final void run() {
                    TreadlyServiceResponseEventListener.this.onCreateFriendInviteToken(TreadlyServiceHelper.errorCouldNotCommunicateWithServer, null);
                }
            });
            return;
        }
        try {
            final String optString = treadlyNetworkResponse.response.getJSONObject("data").optString(keyToken);
            runOnMain(new Runnable() { // from class: com.treadly.Treadly.Data.Managers.-$$Lambda$TreadlyServiceHelper$k7aXLMQtYMzY1naryMKbqSXH8o0
                @Override // java.lang.Runnable
                public final void run() {
                    TreadlyServiceResponseEventListener.this.onCreateFriendInviteToken(null, optString);
                }
            });
        } catch (JSONException e) {
            e.printStackTrace();
            runOnMain(new Runnable() { // from class: com.treadly.Treadly.Data.Managers.-$$Lambda$TreadlyServiceHelper$nLqV-k2dCjYHlig89k4BdwcTtN8
                @Override // java.lang.Runnable
                public final void run() {
                    TreadlyServiceResponseEventListener.this.onCreateFriendInviteToken(TreadlyServiceHelper.errorCouldNotCommunicateWithServer, null);
                }
            });
        }
    }

    public static void getFriendInviteInfoByToken(String str, String str2, final TreadlyServiceResponseEventListener treadlyServiceResponseEventListener) {
        JSONObject jSONObject = new JSONObject();
        JSONObject jSONObject2 = new JSONObject();
        try {
            jSONObject.put(keyHeaderAuthorization, str);
            jSONObject2.put(keyToken, str2);
        } catch (JSONException e) {
            e.printStackTrace();
            treadlyServiceResponseEventListener.onFriendInviteToken("Error sending request", null);
        }
        RequestUtil.shared.postJson(SERVICE_URI_ROUTE_USERS_FRIEND_INVITE_GET, jSONObject2, jSONObject, new RequestUtil.RequestUtilListener() { // from class: com.treadly.Treadly.Data.Managers.-$$Lambda$TreadlyServiceHelper$M8pJulgiXi0JVBlzRvOMOSLBUlQ
            @Override // com.treadly.Treadly.Data.Utility.RequestUtil.RequestUtilListener
            public final void onResponse(TreadlyNetworkResponse treadlyNetworkResponse) {
                TreadlyServiceHelper.lambda$getFriendInviteInfoByToken$38(TreadlyServiceResponseEventListener.this, treadlyNetworkResponse);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static /* synthetic */ void lambda$getFriendInviteInfoByToken$38(final TreadlyServiceResponseEventListener treadlyServiceResponseEventListener, TreadlyNetworkResponse treadlyNetworkResponse) throws JSONException {
        if (!treadlyNetworkResponse.ok() || treadlyNetworkResponse.response == null) {
            runOnMain(new Runnable() { // from class: com.treadly.Treadly.Data.Managers.-$$Lambda$TreadlyServiceHelper$I4seWH7Sz0PaSogMwJ9hAv1JF-A
                @Override // java.lang.Runnable
                public final void run() {
                    TreadlyServiceResponseEventListener.this.onFriendInviteToken(TreadlyServiceHelper.errorCouldNotCommunicateWithServer, null);
                }
            });
            return;
        }
        JSONObject optJSONObject = treadlyNetworkResponse.response.optJSONObject("data");
        if (optJSONObject != null) {
            String optString = optJSONObject.optString(key_Id);
            String optString2 = optJSONObject.optString(keyToken);
            String optString3 = optJSONObject.optString(keyCreatedAt);
            JSONObject optJSONObject2 = optJSONObject.optJSONObject("user");
            if (optJSONObject2 != null) {
                String optString4 = optJSONObject2.optString(key_Id);
                String optString5 = optJSONObject2.optString("name");
                String optString6 = optJSONObject2.optString("avatar");
                if (optString6.isEmpty()) {
                    optString6 = "https://dgwxv5s2i5zkb.cloudfront.net/avatar/default.png";
                }
                final FriendInviteInfo friendInviteInfo = new FriendInviteInfo(optString, optString4, optString5, optString6, optString2, convertTimeStampToDate(optString3));
                runOnMain(new Runnable() { // from class: com.treadly.Treadly.Data.Managers.-$$Lambda$TreadlyServiceHelper$BZ2wP-CYq4s4ksn_Z-at83RQY8c
                    @Override // java.lang.Runnable
                    public final void run() {
                        TreadlyServiceResponseEventListener.this.onFriendInviteToken(null, friendInviteInfo);
                    }
                });
                return;
            }
        }
        runOnMain(new Runnable() { // from class: com.treadly.Treadly.Data.Managers.-$$Lambda$TreadlyServiceHelper$iI5Bhk2xPs3GscjAcyopGCTXp-8
            @Override // java.lang.Runnable
            public final void run() {
                TreadlyServiceResponseEventListener.this.onFriendInviteToken("Error: Parsing invite info", null);
            }
        });
    }

    public static void acceptFriendInvite(String str, String str2, final String str3, final TreadlyServiceResponseEventListener treadlyServiceResponseEventListener) {
        JSONObject jSONObject = new JSONObject();
        try {
            jSONObject.put(keyToken, str2);
            requestBasicAuthPost(SERVICE_URI_ROUTE_USERS_FRIEND_INVITE_APPROVE, str, jSONObject, new TreadlyServiceResponseEventAdapter() { // from class: com.treadly.Treadly.Data.Managers.TreadlyServiceHelper.8
                @Override // com.treadly.Treadly.Data.Listeners.TreadlyServiceResponseEventAdapter, com.treadly.Treadly.Data.Listeners.TreadlyServiceResponseEventListener
                public void onSuccess(String str4) {
                    super.onSuccess(str4);
                    if (str4 == null) {
                        TreadlyEventManager.getInstance().sendUserFriendAdded(str3);
                    }
                    treadlyServiceResponseEventListener.onSuccess(str4);
                }
            });
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public static void declineFriendInvite(String str, String str2, String str3, TreadlyServiceResponseEventListener treadlyServiceResponseEventListener) {
        JSONObject jSONObject = new JSONObject();
        try {
            jSONObject.put(keyToken, str2);
            requestBasicAuthPost(SERVICE_URI_ROUTE_USERS_FRIEND_INVITE_DECLINE, str, jSONObject, treadlyServiceResponseEventListener);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public static void acceptFriendRequest(String str, final String str2, final TreadlyServiceResponseEventListener treadlyServiceResponseEventListener) {
        JSONObject jSONObject = new JSONObject();
        try {
            jSONObject.put(keyUsers, str2);
            requestBasicAuthPost(SERVICE_URI_ROUTE_USERS_FRIEND_REQUEST_ADD, str, jSONObject, new TreadlyServiceResponseEventAdapter() { // from class: com.treadly.Treadly.Data.Managers.TreadlyServiceHelper.9
                @Override // com.treadly.Treadly.Data.Listeners.TreadlyServiceResponseEventAdapter, com.treadly.Treadly.Data.Listeners.TreadlyServiceResponseEventListener
                public void onSuccess(String str3) {
                    super.onSuccess(str3);
                    if (str3 == null) {
                        TreadlyEventManager.getInstance().sendUserFriendAdded(str2);
                    }
                    treadlyServiceResponseEventListener.onSuccess(str3);
                }
            });
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public static void declineFriendRequest(String str, String str2, TreadlyServiceResponseEventListener treadlyServiceResponseEventListener) {
        JSONObject jSONObject = new JSONObject();
        try {
            jSONObject.put(keyUsers, str2);
            requestBasicAuthPost(SERVICE_URI_ROUTE_USERS_FRIEND_REQUEST_DECLINE, str, jSONObject, treadlyServiceResponseEventListener);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private static void requestGetUserStatsInfo(String str, String str2, final TreadlyServiceResponseEventListener treadlyServiceResponseEventListener) {
        JSONObject jSONObject = new JSONObject();
        try {
            jSONObject.put(keyHeaderAuthorization, str2);
        } catch (Exception e) {
            e.printStackTrace();
        }
        RequestUtil.shared.getJson(str, jSONObject, new RequestUtil.RequestUtilListener() { // from class: com.treadly.Treadly.Data.Managers.TreadlyServiceHelper.10
            @Override // com.treadly.Treadly.Data.Utility.RequestUtil.RequestUtilListener
            public void onResponse(TreadlyNetworkResponse treadlyNetworkResponse) {
                JSONArray jSONArray;
                Date convertDateComponentsToDate;
                if (!treadlyNetworkResponse.ok() || treadlyNetworkResponse.response == null) {
                    TreadlyServiceHelper.runOnMain(new Runnable() { // from class: com.treadly.Treadly.Data.Managers.TreadlyServiceHelper.10.1
                        @Override // java.lang.Runnable
                        public void run() {
                            TreadlyServiceResponseEventListener.this.onUserStatsInfo(TreadlyServiceHelper.errorCouldNotConnectWithServer, null);
                        }
                    });
                    return;
                }
                JSONArray optJSONArray = treadlyNetworkResponse.response.optJSONArray("data");
                if (optJSONArray != null) {
                    final ArrayList arrayList = new ArrayList();
                    if (optJSONArray.length() > 0) {
                        int i = 0;
                        while (i < optJSONArray.length()) {
                            try {
                                JSONObject jSONObject2 = optJSONArray.getJSONObject(i);
                                int optInt = jSONObject2.optInt("calories");
                                double optDouble = jSONObject2.optDouble("distance");
                                double optDouble2 = jSONObject2.optDouble("duration");
                                int optInt2 = jSONObject2.optInt("steps");
                                int optInt3 = jSONObject2.optInt(TreadlyServiceHelper.keyCaloriesGoal);
                                int optInt4 = jSONObject2.optInt(TreadlyServiceHelper.keyStepsGoal);
                                double optDouble3 = jSONObject2.optDouble(TreadlyServiceHelper.keyDistanceGoal);
                                double optDouble4 = jSONObject2.optDouble(TreadlyServiceHelper.keyDurationGoal);
                                UserDailyGoalType fromValue = UserDailyGoalType.fromValue(jSONObject2.optInt("goal_type", UserDailyGoalType.none.getValue()));
                                if (jSONObject2.has("calories") && jSONObject2.has("distance") && jSONObject2.has("duration") && jSONObject2.has("steps") && jSONObject2.has(TreadlyServiceHelper.keyCaloriesGoal) && jSONObject2.has(TreadlyServiceHelper.keyStepsGoal)) {
                                    int optInt5 = jSONObject2.optInt(TreadlyServiceHelper.keyStatsCount);
                                    JSONObject optJSONObject = jSONObject2.optJSONObject(TreadlyServiceHelper.keyDate);
                                    int optInt6 = optJSONObject.optInt(TreadlyServiceHelper.keyMonth) - 1;
                                    int optInt7 = optJSONObject.optInt(TreadlyServiceHelper.keyDay);
                                    int optInt8 = optJSONObject.optInt(TreadlyServiceHelper.keyYear);
                                    int optInt9 = optJSONObject.optInt(TreadlyServiceHelper.keyDayOfWeek);
                                    jSONArray = optJSONArray;
                                    try {
                                        if (optJSONObject.has(TreadlyServiceHelper.keyMonth) && optJSONObject.has(TreadlyServiceHelper.keyDay) && optJSONObject.has(TreadlyServiceHelper.keyYear) && optJSONObject.has(TreadlyServiceHelper.keyDayOfWeek) && (convertDateComponentsToDate = TreadlyServiceHelper.convertDateComponentsToDate(optInt6, optInt7, optInt8, optInt9)) != null) {
                                            arrayList.add(new UserStatsInfo(convertDateComponentsToDate, optInt9, optInt, optDouble, optDouble2, optInt2, optInt3, optInt4, optDouble3, optDouble4, fromValue, optInt5));
                                        }
                                    } catch (Exception e2) {
                                        e = e2;
                                        e.printStackTrace();
                                        i++;
                                        optJSONArray = jSONArray;
                                    }
                                } else {
                                    jSONArray = optJSONArray;
                                }
                            } catch (Exception e3) {
                                e = e3;
                                jSONArray = optJSONArray;
                            }
                            i++;
                            optJSONArray = jSONArray;
                        }
                    }
                    TreadlyServiceHelper.runOnMain(new Runnable() { // from class: com.treadly.Treadly.Data.Managers.TreadlyServiceHelper.10.2
                        @Override // java.lang.Runnable
                        public void run() {
                            TreadlyServiceResponseEventListener.this.onUserStatsInfo(null, arrayList);
                        }
                    });
                    return;
                }
                TreadlyServiceHelper.runOnMain(new Runnable() { // from class: com.treadly.Treadly.Data.Managers.TreadlyServiceHelper.10.3
                    @Override // java.lang.Runnable
                    public void run() {
                        TreadlyServiceResponseEventListener.this.onUserStatsInfo(TreadlyServiceHelper.errorCouldNotCommunicateWithServer, null);
                    }
                });
            }
        });
    }

    public static void getSingleUserMode(String str, String str2, final TreadlyServiceResponseEventListener treadlyServiceResponseEventListener) {
        JSONObject jSONObject = new JSONObject();
        JSONObject jSONObject2 = new JSONObject();
        try {
            jSONObject.put(keyHeaderAuthorization, str);
            jSONObject2.put(keyDeviceAddress, str2.toUpperCase());
            RequestUtil.shared.postJson(SERVICE_URI_ROUTE_USERS_SINGLE_GET, jSONObject2, jSONObject, new RequestUtil.RequestUtilListener() { // from class: com.treadly.Treadly.Data.Managers.-$$Lambda$TreadlyServiceHelper$z6jrcr47zRDflAEYbOx991Id_aE
                @Override // com.treadly.Treadly.Data.Utility.RequestUtil.RequestUtilListener
                public final void onResponse(TreadlyNetworkResponse treadlyNetworkResponse) {
                    TreadlyServiceHelper.lambda$getSingleUserMode$41(TreadlyServiceResponseEventListener.this, treadlyNetworkResponse);
                }
            });
        } catch (JSONException e) {
            e.printStackTrace();
            runOnMain(new Runnable() { // from class: com.treadly.Treadly.Data.Managers.-$$Lambda$TreadlyServiceHelper$hCkXwz4zUmuswSKh9y0pGlbbFd4
                @Override // java.lang.Runnable
                public final void run() {
                    TreadlyServiceResponseEventListener.this.onUserInfo("Issue with response", null);
                }
            });
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static /* synthetic */ void lambda$getSingleUserMode$41(final TreadlyServiceResponseEventListener treadlyServiceResponseEventListener, TreadlyNetworkResponse treadlyNetworkResponse) throws JSONException {
        if (!treadlyNetworkResponse.ok() || treadlyNetworkResponse.response == null) {
            runOnMain(new Runnable() { // from class: com.treadly.Treadly.Data.Managers.-$$Lambda$TreadlyServiceHelper$rSNVHygwqTMrIqbH0furF10XJpI
                @Override // java.lang.Runnable
                public final void run() {
                    TreadlyServiceResponseEventListener.this.onUserInfo(TreadlyServiceHelper.errorCouldNotCommunicateWithServer, null);
                }
            });
            return;
        }
        JSONObject jSONObject = treadlyNetworkResponse.response.getJSONObject("data");
        final String optString = jSONObject.optString("id");
        final String optString2 = jSONObject.optString("name");
        final String optString3 = jSONObject.optString("email");
        runOnMain(new Runnable() { // from class: com.treadly.Treadly.Data.Managers.-$$Lambda$TreadlyServiceHelper$cHhImi1hxiPyGBabwoqre94aS7M
            @Override // java.lang.Runnable
            public final void run() {
                TreadlyServiceResponseEventListener.this.onUserInfo(null, new UserInfo(optString, optString2, optString3));
            }
        });
    }

    public static void addSingleUserMode(String str, String str2, final TreadlyServiceResponseEventListener treadlyServiceResponseEventListener) {
        JSONObject jSONObject = new JSONObject();
        JSONObject jSONObject2 = new JSONObject();
        try {
            jSONObject.put(keyHeaderAuthorization, str);
            jSONObject2.put(keyDeviceAddress, str2.toUpperCase());
            RequestUtil.shared.postJson(SERVICE_URI_ROUTE_USERS_SINGLE_ADD, jSONObject2, jSONObject, new RequestUtil.RequestUtilListener() { // from class: com.treadly.Treadly.Data.Managers.-$$Lambda$TreadlyServiceHelper$NyqZaPPC7m9UeGjhMdgM7Is0Sq4
                @Override // com.treadly.Treadly.Data.Utility.RequestUtil.RequestUtilListener
                public final void onResponse(TreadlyNetworkResponse treadlyNetworkResponse) {
                    TreadlyServiceHelper.lambda$addSingleUserMode$45(TreadlyServiceResponseEventListener.this, treadlyNetworkResponse);
                }
            });
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static /* synthetic */ void lambda$addSingleUserMode$45(final TreadlyServiceResponseEventListener treadlyServiceResponseEventListener, TreadlyNetworkResponse treadlyNetworkResponse) throws JSONException {
        if (!treadlyNetworkResponse.ok() || treadlyNetworkResponse.response == null) {
            runOnMain(new Runnable() { // from class: com.treadly.Treadly.Data.Managers.-$$Lambda$TreadlyServiceHelper$w1_Wlz4Qg-ieroo0TngZISZCcKM
                @Override // java.lang.Runnable
                public final void run() {
                    TreadlyServiceResponseEventListener.this.onSuccess(TreadlyServiceHelper.errorCouldNotCommunicateWithServer);
                }
            });
        } else {
            runOnMain(new Runnable() { // from class: com.treadly.Treadly.Data.Managers.-$$Lambda$TreadlyServiceHelper$dfGVXk4XYHTKAIYOx_e67xOfAmw
                @Override // java.lang.Runnable
                public final void run() {
                    TreadlyServiceResponseEventListener.this.onSuccess(null);
                }
            });
        }
    }

    public static void removeSingleUserMode(String str, String str2, final TreadlyServiceResponseEventListener treadlyServiceResponseEventListener) {
        JSONObject jSONObject = new JSONObject();
        JSONObject jSONObject2 = new JSONObject();
        try {
            jSONObject.put(keyHeaderAuthorization, str);
            jSONObject2.put(keyDeviceAddress, str2.toUpperCase());
            RequestUtil.shared.postJson(SERVICE_URI_ROUTE_USERS_SINGLE_REMOVE, jSONObject2, jSONObject, new RequestUtil.RequestUtilListener() { // from class: com.treadly.Treadly.Data.Managers.-$$Lambda$TreadlyServiceHelper$CzQHm1gJLl-5-Lbr8B5fPr42Ahs
                @Override // com.treadly.Treadly.Data.Utility.RequestUtil.RequestUtilListener
                public final void onResponse(TreadlyNetworkResponse treadlyNetworkResponse) {
                    TreadlyServiceHelper.lambda$removeSingleUserMode$48(TreadlyServiceResponseEventListener.this, treadlyNetworkResponse);
                }
            });
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static /* synthetic */ void lambda$removeSingleUserMode$48(final TreadlyServiceResponseEventListener treadlyServiceResponseEventListener, TreadlyNetworkResponse treadlyNetworkResponse) throws JSONException {
        if (!treadlyNetworkResponse.ok() || treadlyNetworkResponse.response == null) {
            runOnMain(new Runnable() { // from class: com.treadly.Treadly.Data.Managers.-$$Lambda$TreadlyServiceHelper$eoo0IiqpxOCE0bilttDkJl_cnR0
                @Override // java.lang.Runnable
                public final void run() {
                    TreadlyServiceResponseEventListener.this.onSuccess(TreadlyServiceHelper.errorCouldNotCommunicateWithServer);
                }
            });
        } else {
            runOnMain(new Runnable() { // from class: com.treadly.Treadly.Data.Managers.-$$Lambda$TreadlyServiceHelper$WoLsi1TO3CIIPz8fE5zfJECFF7M
                @Override // java.lang.Runnable
                public final void run() {
                    TreadlyServiceResponseEventListener.this.onSuccess(null);
                }
            });
        }
    }

    public static void resetSingleUserMode(String str, String str2, TreadlyServiceResponseEventListener treadlyServiceResponseEventListener) {
        JSONObject jSONObject = new JSONObject();
        try {
            jSONObject.put(keyDeviceAddress, str2.toUpperCase());
            requestBasicAuthPost(SERVICE_URI_ROUTE_USERS_SINGLE_RESET, str, jSONObject, treadlyServiceResponseEventListener);
        } catch (JSONException e) {
            e.printStackTrace();
            treadlyServiceResponseEventListener.onSuccess(e.getMessage());
        }
    }

    private static String getDeviceAddressUrl(String str) {
        return String.format(SERVICE_URI_ROUTE_USERS_DEVICE, str);
    }

    public static void updateOnboardingStatus(String str, TreadlyServiceResponseEventListener treadlyServiceResponseEventListener) {
        requestBasicAuthPost(SERVICE_URI_ROUTE_USERS_ONBOARDED_UPDATE, str, new JSONObject(), treadlyServiceResponseEventListener);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static void sendUserAvatar(String str, String str2, final File file, final TreadlyServiceResponseEventListener treadlyServiceResponseEventListener) {
        final JSONObject jSONObject = new JSONObject();
        try {
            jSONObject.put(keyHeaderAuthorization, str2);
        } catch (Exception e) {
            e.printStackTrace();
        }
        final JSONObject jSONObject2 = new JSONObject();
        try {
            jSONObject2.put(key_Id, str);
            webThreadPool.execute(new Runnable() { // from class: com.treadly.Treadly.Data.Managers.TreadlyServiceHelper.11
                @Override // java.lang.Runnable
                public void run() {
                    if (TreadlyServiceHelper.doJsonRequestFileUpload(TreadlyServiceHelper.SERVICE_URI_ROUTE_USERS_AVATAR, jSONObject2, jSONObject, file) == null) {
                        TreadlyServiceHelper.runOnMain(new Runnable() { // from class: com.treadly.Treadly.Data.Managers.TreadlyServiceHelper.11.1
                            @Override // java.lang.Runnable
                            public void run() {
                                treadlyServiceResponseEventListener.onSuccess(TreadlyServiceHelper.errorCouldNotCommunicateWithServer);
                            }
                        });
                    } else {
                        treadlyServiceResponseEventListener.onSuccess(null);
                    }
                }
            });
        } catch (Exception e2) {
            e2.printStackTrace();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static JSONObject doJsonRequestFileUpload(String str, JSONObject jSONObject, JSONObject jSONObject2, File file) {
        String string;
        try {
            MultipartBody.Builder addFormDataPart = new MultipartBody.Builder().setType(MultipartBody.FORM).addFormDataPart("image", file.getName(), RequestBody.create(MediaType.parse("image/png"), file));
            Iterator<String> keys = jSONObject.keys();
            while (keys.hasNext()) {
                String next = keys.next();
                String optString = jSONObject.optString(next, null);
                if (optString != null) {
                    addFormDataPart.addFormDataPart(next, optString);
                }
            }
            Headers.Builder builder = new Headers.Builder();
            if (jSONObject2 != null) {
                Iterator<String> keys2 = jSONObject2.keys();
                while (keys2.hasNext()) {
                    String next2 = keys2.next();
                    String optString2 = jSONObject2.optString(next2, null);
                    if (optString2 != null) {
                        builder.add(next2, optString2);
                    }
                }
            }
            Response execute = client.newCall(new Request.Builder().url(str).post(addFormDataPart.build()).headers(builder.build()).build()).execute();
            if (execute == null || !execute.isSuccessful() || execute.body() == null || (string = execute.body().string()) == null) {
                return null;
            }
            PrintStream printStream = System.out;
            printStream.println("LGK :: Response String : " + string);
            return new JSONObject(string);
        } catch (Error e) {
            e.printStackTrace();
            return null;
        } catch (Exception e2) {
            e2.printStackTrace();
            return null;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static Date convertDateComponentsToDate(int i, int i2, int i3, int i4) {
        return new GregorianCalendar(i3, i, i2).getTime();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static void getDeviceUserStatsInfo(String str, int i, String str2, boolean z, TreadlyServiceResponseEventListener treadlyServiceResponseEventListener) {
        requestGetUserStatsInfo(getDeviceUserStatsInfoUri(i, str2, z), str, treadlyServiceResponseEventListener);
    }

    private static String getDeviceUserStatsInfoUri(int i, String str, boolean z) {
        return appendTargetUserIdToUri(appendQueryParameterToUri(String.format(SERVICE_URI_ROUTE_STATS_DEVICE_DAY, Integer.valueOf(i)), keyFilterSnapshots, z ? AppEventsConstants.EVENT_PARAM_VALUE_YES : AppEventsConstants.EVENT_PARAM_VALUE_NO), str);
    }

    private static String appendTargetUserIdToUri(String str, String str2) {
        return str2 == null ? str : appendQueryParameterToUri(str, "userid", str2);
    }

    private static String appendQueryParameterToUri(String str, String str2, String str3) {
        return str.contains("?") ? String.format("%s&%s=%s", str, str2, str3) : String.format("%s?%s=%s", str, str2, str3);
    }

    private static String getProfileUserIDUri(String str, int i) {
        return String.format(SERVICE_URI_ROUTE_PROFILE_USER_ID, str, Integer.valueOf(i));
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static void sendDeviceRunningSession(String str, UserRunningSessionInfo userRunningSessionInfo, ArrayList<UserRunningSessionParticipantInfo> arrayList, String str2, TreadlyServiceResponseEventListener treadlyServiceResponseEventListener) {
        JSONObject jSONObject = new JSONObject();
        try {
            jSONObject.put(keyFinishedAt, convertDateToServiceTimestamp(userRunningSessionInfo.finishedAt));
            jSONObject.put("calories", userRunningSessionInfo.calories);
            jSONObject.put("distance", userRunningSessionInfo.distance);
            jSONObject.put("duration", userRunningSessionInfo.duration);
            jSONObject.put("steps", userRunningSessionInfo.steps);
            if (str2 != null) {
                jSONObject.put(keyGenType, str2);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        JSONArray jSONArray = new JSONArray();
        for (UserRunningSessionSegmentInfo userRunningSessionSegmentInfo : userRunningSessionInfo.segments) {
            JSONObject jSONObject2 = new JSONObject();
            try {
                jSONObject2.put(keyCreatedAt, convertDateToServiceTimestamp(userRunningSessionSegmentInfo.createdAt));
                jSONObject2.put("steps", userRunningSessionSegmentInfo.steps);
                jSONObject2.put("speed", userRunningSessionSegmentInfo.speed);
                jSONArray.put(jSONObject2);
            } catch (Exception e2) {
                e2.printStackTrace();
            }
        }
        try {
            jSONObject.put(keyDetailed, jSONArray);
        } catch (Exception e3) {
            e3.printStackTrace();
        }
        JSONArray jSONArray2 = new JSONArray();
        Iterator<UserRunningSessionParticipantInfo> it = arrayList.iterator();
        while (it.hasNext()) {
            UserRunningSessionParticipantInfo next = it.next();
            JSONObject jSONObject3 = new JSONObject();
            try {
                jSONObject3.put(keyJoinedAt, convertDateToServiceTimestamp(next.joinedAt));
                jSONObject3.put(keyParticipantId, next.participantId);
                jSONArray2.put(jSONObject3);
            } catch (Exception e4) {
                e4.printStackTrace();
            }
        }
        try {
            jSONObject.put(keyParticipants, jSONArray2);
        } catch (Exception e5) {
            e5.printStackTrace();
        }
        requestBasicAuthPost(SERVICE_URI_ROUTE_STATS_DEVICE_COMPLETE, str, jSONObject, treadlyServiceResponseEventListener);
    }

    /* renamed from: com.treadly.Treadly.Data.Managers.TreadlyServiceHelper$12  reason: invalid class name */
    /* loaded from: classes2.dex */
    static class AnonymousClass12 implements RequestUtil.RequestUtilListener {
        final /* synthetic */ TreadlyServiceResponseEventListener val$listener;

        AnonymousClass12(TreadlyServiceResponseEventListener treadlyServiceResponseEventListener) {
            this.val$listener = treadlyServiceResponseEventListener;
        }

        @Override // com.treadly.Treadly.Data.Utility.RequestUtil.RequestUtilListener
        public void onResponse(TreadlyNetworkResponse treadlyNetworkResponse) throws JSONException {
            if (!treadlyNetworkResponse.ok() || treadlyNetworkResponse.response == null) {
                final TreadlyServiceResponseEventListener treadlyServiceResponseEventListener = this.val$listener;
                TreadlyServiceHelper.runOnMain(new Runnable() { // from class: com.treadly.Treadly.Data.Managers.-$$Lambda$TreadlyServiceHelper$12$dwuq9-ltFc7knqAsDKF8eZCes0Q
                    @Override // java.lang.Runnable
                    public final void run() {
                        TreadlyServiceResponseEventListener.this.onSendDeviceLogSession(TreadlyServiceHelper.errorCouldNotCommunicateWithServer, null, null, null);
                    }
                });
                return;
            }
            JSONObject optJSONObject = treadlyNetworkResponse.response.optJSONObject("data");
            if (optJSONObject != null) {
                final String optString = optJSONObject.optString("user_id");
                final String optString2 = optJSONObject.optString(TreadlyServiceHelper.key_Id);
                final UserRunningSessionInfo parseUserRunningSessionInfo = TreadlyServiceHelper.parseUserRunningSessionInfo(optJSONObject);
                final TreadlyServiceResponseEventListener treadlyServiceResponseEventListener2 = this.val$listener;
                TreadlyServiceHelper.runOnMain(new Runnable() { // from class: com.treadly.Treadly.Data.Managers.-$$Lambda$TreadlyServiceHelper$12$ONa3m3KVilesKBZVkVPSF2p1FUI
                    @Override // java.lang.Runnable
                    public final void run() {
                        TreadlyServiceResponseEventListener.this.onSendDeviceLogSession(null, optString, optString2, parseUserRunningSessionInfo);
                    }
                });
                return;
            }
            final TreadlyServiceResponseEventListener treadlyServiceResponseEventListener3 = this.val$listener;
            TreadlyServiceHelper.runOnMain(new Runnable() { // from class: com.treadly.Treadly.Data.Managers.-$$Lambda$TreadlyServiceHelper$12$VMT7DgTctKRED7Skrm9LRV421Rk
                @Override // java.lang.Runnable
                public final void run() {
                    TreadlyServiceResponseEventListener.this.onSendDeviceLogSession(null, null, null, null);
                }
            });
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static void sendDeviceLogSession(String str, byte[] bArr, TreadlyServiceResponseEventListener treadlyServiceResponseEventListener) {
        RequestUtil.shared.postOctet(SERVICE_URI_ROUTE_STATS_DEVICE_COMPLETE_V2, bArr, new AnonymousClass12(treadlyServiceResponseEventListener));
    }

    private static String convertDateToServiceTimestamp(Date date) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS", Locale.getDefault());
        simpleDateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
        String format = simpleDateFormat.format(date);
        return format + "Z";
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static Date convertTimeStampToDate(String str) {
        if (str == null) {
            return null;
        }
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault());
        simpleDateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
        try {
            return simpleDateFormat.parse(str);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static void getUnclaimedLogInfo(String str, String str2, final TreadlyServiceResponseEventListener treadlyServiceResponseEventListener) {
        JSONObject jSONObject = new JSONObject();
        JSONObject jSONObject2 = new JSONObject();
        try {
            jSONObject.put(keyDeviceAddress, str2);
            jSONObject2.put(keyHeaderAuthorization, str);
            RequestUtil.shared.postJson(SERVICE_URI_ROUTE_STATS_DEVICE_UNCLAIMED, jSONObject, jSONObject2, new RequestUtil.RequestUtilListener() { // from class: com.treadly.Treadly.Data.Managers.-$$Lambda$TreadlyServiceHelper$NEyyO_MeOoeVpXU7a4whgLhTPIc
                @Override // com.treadly.Treadly.Data.Utility.RequestUtil.RequestUtilListener
                public final void onResponse(TreadlyNetworkResponse treadlyNetworkResponse) {
                    TreadlyServiceHelper.lambda$getUnclaimedLogInfo$52(TreadlyServiceResponseEventListener.this, treadlyNetworkResponse);
                }
            });
        } catch (JSONException e) {
            e.printStackTrace();
            treadlyServiceResponseEventListener.onGetUnclaimedLogs(e.getMessage(), null);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static /* synthetic */ void lambda$getUnclaimedLogInfo$52(final TreadlyServiceResponseEventListener treadlyServiceResponseEventListener, TreadlyNetworkResponse treadlyNetworkResponse) throws JSONException {
        if (!treadlyNetworkResponse.ok() || treadlyNetworkResponse.response == null) {
            runOnMain(new Runnable() { // from class: com.treadly.Treadly.Data.Managers.-$$Lambda$TreadlyServiceHelper$HJqgaidwb5D3bXpXpT83nBFxdcg
                @Override // java.lang.Runnable
                public final void run() {
                    TreadlyServiceResponseEventListener.this.onGetUnclaimedLogs(TreadlyServiceHelper.errorCouldNotCommunicateWithServer, null);
                }
            });
            return;
        }
        JSONArray jSONArray = treadlyNetworkResponse.response.getJSONArray("data");
        final ArrayList arrayList = new ArrayList();
        if (jSONArray.length() == 0) {
            runOnMain(new Runnable() { // from class: com.treadly.Treadly.Data.Managers.-$$Lambda$TreadlyServiceHelper$yPj9AmQ8Zc9nKiGotCvYT2hd8qc
                @Override // java.lang.Runnable
                public final void run() {
                    TreadlyServiceResponseEventListener.this.onGetUnclaimedLogs(null, arrayList);
                }
            });
        }
        for (int i = 0; i < jSONArray.length(); i++) {
            JSONObject jSONObject = jSONArray.getJSONObject(i);
            String optString = jSONObject.optString(key_Id);
            arrayList.add(new UserRunningSessionInfo(convertTimeStampToDate(jSONObject.optString(keyFinishedAt)), 0, Utils.DOUBLE_EPSILON, Utils.DOUBLE_EPSILON, jSONObject.optInt("steps"), optString));
        }
        runOnMain(new Runnable() { // from class: com.treadly.Treadly.Data.Managers.-$$Lambda$TreadlyServiceHelper$rJzwZjE8KAukvGh9OYwhDe2L1Gw
            @Override // java.lang.Runnable
            public final void run() {
                TreadlyServiceResponseEventListener.this.onGetUnclaimedLogs(null, arrayList);
            }
        });
    }

    public static void claimUnclaimedSession(String str, UserRunningSessionInfo userRunningSessionInfo, final TreadlyServiceResponseEventListener treadlyServiceResponseEventListener) {
        if (userRunningSessionInfo == null || userRunningSessionInfo.log_id.isEmpty()) {
            return;
        }
        JSONObject jSONObject = new JSONObject();
        JSONObject jSONObject2 = new JSONObject();
        try {
            jSONObject.put(key_Id, userRunningSessionInfo.log_id);
            jSONObject.put(keyFinishedAt, convertDateToServiceTimestamp(userRunningSessionInfo.timestamp));
            jSONObject2.put(keyHeaderAuthorization, str);
            RequestUtil.shared.postJson(SERVICE_URI_ROUTE_STATS_DEVICE_CLAIM, jSONObject, jSONObject2, new RequestUtil.RequestUtilListener() { // from class: com.treadly.Treadly.Data.Managers.-$$Lambda$TreadlyServiceHelper$FImeMRTQRiHprPB_YO3FLZsB8iU
                @Override // com.treadly.Treadly.Data.Utility.RequestUtil.RequestUtilListener
                public final void onResponse(TreadlyNetworkResponse treadlyNetworkResponse) {
                    TreadlyServiceHelper.lambda$claimUnclaimedSession$55(TreadlyServiceResponseEventListener.this, treadlyNetworkResponse);
                }
            });
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static /* synthetic */ void lambda$claimUnclaimedSession$55(final TreadlyServiceResponseEventListener treadlyServiceResponseEventListener, TreadlyNetworkResponse treadlyNetworkResponse) throws JSONException {
        if (!treadlyNetworkResponse.ok() || treadlyNetworkResponse.response == null) {
            runOnMain(new Runnable() { // from class: com.treadly.Treadly.Data.Managers.-$$Lambda$TreadlyServiceHelper$fiICybN0ys8oYqwsTk2aUrAA2qU
                @Override // java.lang.Runnable
                public final void run() {
                    TreadlyServiceResponseEventListener.this.onSuccess(TreadlyServiceHelper.errorCouldNotCommunicateWithServer);
                }
            });
        } else {
            runOnMain(new Runnable() { // from class: com.treadly.Treadly.Data.Managers.-$$Lambda$TreadlyServiceHelper$tcRdT4GXtxQbMwob43EGvdGtGzM
                @Override // java.lang.Runnable
                public final void run() {
                    TreadlyServiceResponseEventListener.this.onSuccess(null);
                }
            });
        }
    }

    public static void claimAllUnclaimedSessions(String str, String str2, TreadlyServiceResponseEventListener treadlyServiceResponseEventListener) {
        if (str2 == null || str2.isEmpty()) {
            return;
        }
        JSONObject jSONObject = new JSONObject();
        try {
            jSONObject.put(keyDeviceAddress, str2);
            requestBasicAuthPost(SERVICE_URI_ROUTE_STATS_DEVICE_CLAIM_ALL, str, jSONObject, treadlyServiceResponseEventListener);
        } catch (JSONException e) {
            e.printStackTrace();
            treadlyServiceResponseEventListener.onSuccess(e.getMessage());
        }
    }

    public static void deleteUnclaimedSession(String str, UserRunningSessionInfo userRunningSessionInfo, TreadlyServiceResponseEventListener treadlyServiceResponseEventListener) {
        if (userRunningSessionInfo == null || userRunningSessionInfo.log_id.isEmpty()) {
            return;
        }
        JSONObject jSONObject = new JSONObject();
        try {
            jSONObject.put(key_Id, userRunningSessionInfo.log_id);
            requestBasicAuthPost(SERVICE_URI_ROUTE_STATS_DEVICE_CLAIM_DELETE, str, jSONObject, treadlyServiceResponseEventListener);
        } catch (JSONException e) {
            e.printStackTrace();
            treadlyServiceResponseEventListener.onSuccess(e.getMessage());
        }
    }

    private static void requestBasicAuthPost(String str, String str2, JSONObject jSONObject, final TreadlyServiceResponseEventListener treadlyServiceResponseEventListener) {
        JSONObject jSONObject2 = new JSONObject();
        try {
            jSONObject2.put(keyHeaderAuthorization, str2);
        } catch (Exception e) {
            e.printStackTrace();
        }
        RequestUtil.shared.postJson(str, jSONObject, jSONObject2, new RequestUtil.RequestUtilListener() { // from class: com.treadly.Treadly.Data.Managers.TreadlyServiceHelper.13
            @Override // com.treadly.Treadly.Data.Utility.RequestUtil.RequestUtilListener
            public void onResponse(TreadlyNetworkResponse treadlyNetworkResponse) {
                if (!treadlyNetworkResponse.ok() || treadlyNetworkResponse.response == null) {
                    TreadlyServiceHelper.runOnMain(new Runnable() { // from class: com.treadly.Treadly.Data.Managers.TreadlyServiceHelper.13.1
                        @Override // java.lang.Runnable
                        public void run() {
                            TreadlyServiceResponseEventListener.this.onSuccess(TreadlyServiceHelper.errorCouldNotConnectWithServer);
                        }
                    });
                } else {
                    TreadlyServiceHelper.runOnMain(new Runnable() { // from class: com.treadly.Treadly.Data.Managers.TreadlyServiceHelper.13.2
                        @Override // java.lang.Runnable
                        public void run() {
                            TreadlyServiceResponseEventListener.this.onSuccess(null);
                        }
                    });
                }
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static void getDeviceUserRunningSessionsInfoByDate(String str, int i, int i2, int i3, int i4, String str2, TreadlyServiceResponseEventListener treadlyServiceResponseEventListener) {
        requestGetUserRunningSessionsInfo(getDeviceUserRunningSessionsInfoByDateUri(i, i2, i3, i4, str2), str, treadlyServiceResponseEventListener);
    }

    private static String getDeviceUserRunningSessionsInfoByDateUri(int i, int i2, int i3, int i4, String str) {
        return appendTargetUserIdToUri(String.format(SERVICE_URI_ROUTE_STATS_DEVICE_DAY_SELECTED, Integer.valueOf(i), Integer.valueOf(i2), Integer.valueOf(i3), Integer.valueOf(i4)), str);
    }

    private static void requestGetUserRunningSessionsInfo(String str, String str2, final TreadlyServiceResponseEventListener treadlyServiceResponseEventListener) {
        JSONObject jSONObject = new JSONObject();
        try {
            jSONObject.put(keyHeaderAuthorization, str2);
        } catch (Exception e) {
            e.printStackTrace();
        }
        RequestUtil.shared.getJson(str, jSONObject, new RequestUtil.RequestUtilListener() { // from class: com.treadly.Treadly.Data.Managers.TreadlyServiceHelper.14
            @Override // com.treadly.Treadly.Data.Utility.RequestUtil.RequestUtilListener
            public void onResponse(TreadlyNetworkResponse treadlyNetworkResponse) {
                if (!treadlyNetworkResponse.ok() || treadlyNetworkResponse.response == null) {
                    TreadlyServiceHelper.runOnMain(new Runnable() { // from class: com.treadly.Treadly.Data.Managers.TreadlyServiceHelper.14.1
                        @Override // java.lang.Runnable
                        public void run() {
                            TreadlyServiceResponseEventListener.this.onUserStatsInfo(TreadlyServiceHelper.errorCouldNotConnectWithServer, null);
                        }
                    });
                    return;
                }
                JSONArray optJSONArray = treadlyNetworkResponse.response.optJSONArray("data");
                if (optJSONArray != null) {
                    final ArrayList arrayList = new ArrayList();
                    if (optJSONArray.length() > 0) {
                        for (int i = 0; i < optJSONArray.length(); i++) {
                            try {
                                JSONObject jSONObject2 = optJSONArray.getJSONObject(i);
                                String optString = jSONObject2.optString(TreadlyServiceHelper.keyFinishedAt);
                                System.out.println("HELPER: FINISHED AT: " + optString);
                                int optInt = jSONObject2.optInt("calories");
                                double optDouble = jSONObject2.optDouble("distance");
                                double optDouble2 = jSONObject2.optDouble("duration");
                                int optInt2 = jSONObject2.optInt("steps");
                                int optInt3 = jSONObject2.optInt(TreadlyServiceHelper.keyCaloriesGoal);
                                int optInt4 = jSONObject2.optInt(TreadlyServiceHelper.keyStepsGoal);
                                double optDouble3 = jSONObject2.optDouble(TreadlyServiceHelper.keyDistanceGoal);
                                double optDouble4 = jSONObject2.optDouble(TreadlyServiceHelper.keyDurationGoal);
                                UserDailyGoalType fromValue = UserDailyGoalType.fromValue(jSONObject2.optInt("goal_type", UserDailyGoalType.none.getValue()));
                                if (jSONObject2.has(TreadlyServiceHelper.keyFinishedAt) && jSONObject2.has("calories") && jSONObject2.has("distance") && jSONObject2.has("duration") && jSONObject2.has("steps") && jSONObject2.has(TreadlyServiceHelper.keyCaloriesGoal) && jSONObject2.has(TreadlyServiceHelper.keyStepsGoal)) {
                                    Date convertTimeStampToDate = TreadlyServiceHelper.convertTimeStampToDate(optString);
                                    System.out.println("HELPER: AFTER CONVERT: " + convertTimeStampToDate);
                                    UserRunningSessionInfo userRunningSessionInfo = new UserRunningSessionInfo(convertTimeStampToDate, optInt, optDouble, optDouble2, optInt2, optInt3, optInt4, optDouble3, optDouble4, fromValue, 0);
                                    JSONArray optJSONArray2 = jSONObject2.optJSONArray(TreadlyServiceHelper.keyDetailed);
                                    if (optJSONArray2 != null) {
                                        for (int i2 = 0; i2 < optJSONArray2.length(); i2++) {
                                            JSONObject jSONObject3 = optJSONArray2.getJSONObject(i2);
                                            String optString2 = jSONObject3.optString(TreadlyServiceHelper.keyCreatedAt);
                                            int optInt5 = jSONObject3.optInt("steps");
                                            double optDouble5 = jSONObject3.optDouble("speed");
                                            if (jSONObject3.has(TreadlyServiceHelper.keyCreatedAt) && jSONObject3.has("steps") && jSONObject3.has("speed")) {
                                                userRunningSessionInfo.segments.add(new UserRunningSessionSegmentInfo(TreadlyServiceHelper.convertTimeStampToDate(optString2), optInt5, optDouble5));
                                            }
                                        }
                                    }
                                    JSONArray optJSONArray3 = jSONObject2.optJSONArray(TreadlyServiceHelper.keyParticipants);
                                    if (optJSONArray3 != null) {
                                        for (int i3 = 0; i3 < optJSONArray3.length(); i3++) {
                                            JSONObject jSONObject4 = optJSONArray3.getJSONObject(i3);
                                            String optString3 = jSONObject4.optString(TreadlyServiceHelper.keyJoinedAt);
                                            JSONObject optJSONObject = jSONObject4.optJSONObject(TreadlyServiceHelper.keyParticipant);
                                            if (jSONObject4.has(TreadlyServiceHelper.keyJoinedAt) && jSONObject4.has(TreadlyServiceHelper.keyParticipant)) {
                                                Date convertTimeStampToDate2 = TreadlyServiceHelper.convertTimeStampToDate(optString3);
                                                if (optJSONObject.has("id") && optJSONObject.has("name") && optJSONObject.has("avatar")) {
                                                    String optString4 = optJSONObject.optString("id");
                                                    String optString5 = optJSONObject.optString("name");
                                                    String optString6 = optJSONObject.optString("avatar");
                                                    if (optString6.length() == 0) {
                                                        optString6 = "https://dgwxv5s2i5zkb.cloudfront.net/avatar/default.png";
                                                    }
                                                    userRunningSessionInfo.particpants.add(new UserRunningSessionParticipantInfo(convertTimeStampToDate2, optString4, optString5, optString6));
                                                }
                                            }
                                        }
                                    }
                                    arrayList.add(userRunningSessionInfo);
                                }
                            } catch (Exception e2) {
                                e2.printStackTrace();
                            }
                        }
                        TreadlyServiceHelper.runOnMain(new Runnable() { // from class: com.treadly.Treadly.Data.Managers.TreadlyServiceHelper.14.2
                            @Override // java.lang.Runnable
                            public void run() {
                                TreadlyServiceResponseEventListener.this.onUserRunningSessionsInfo(null, (ArrayList) arrayList);
                            }
                        });
                    }
                }
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static void updateUserProfile(String str, UserProfileRequest userProfileRequest, TreadlyServiceResponseEventListener treadlyServiceResponseEventListener) {
        JSONObject jSONObject = new JSONObject();
        try {
            jSONObject.put(keyAge, userProfileRequest.age);
            jSONObject.put(keyWeight, userProfileRequest.weight);
            jSONObject.put("height", userProfileRequest.height);
            jSONObject.put(keyGender, userProfileRequest.gender);
            jSONObject.put("goal_type", userProfileRequest.goalType.getValue());
            jSONObject.put(keyCaloriesGoal, userProfileRequest.caloriesGoal);
            jSONObject.put(keyStepsGoal, userProfileRequest.stepsGoal);
            jSONObject.put(keyDistanceGoal, userProfileRequest.distanceGoal);
            jSONObject.put(keyDurationGoal, userProfileRequest.durationGoal);
            jSONObject.put(keyBirthdateYear, userProfileRequest.birthdateYear);
            jSONObject.put(keyUnits, userProfileRequest.units.value());
            jSONObject.put(keyDeviceAddress, userProfileRequest.deviceAddress);
            requestBasicAuthPost(SERVICE_URI_ROUTE_PROFILE, str, jSONObject, treadlyServiceResponseEventListener);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static String getUserSearchByNameUri(String str) {
        return String.format(SERVICE_URI_ROUTE_USERS_SEARCH_NAME, str);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static void sendActivtyPostImage(String str, String str2, File file, TreadlyServiceResponseEventListener treadlyServiceResponseEventListener) {
        JSONObject jSONObject = new JSONObject();
        try {
            jSONObject.put(keyHeaderAuthorization, str);
        } catch (Exception e) {
            e.printStackTrace();
        }
        JSONObject jSONObject2 = new JSONObject();
        try {
            jSONObject2.put(key_Id, str2);
            webThreadPool.execute(new AnonymousClass15(jSONObject2, jSONObject, file, treadlyServiceResponseEventListener));
        } catch (Exception e2) {
            e2.printStackTrace();
        }
    }

    /* renamed from: com.treadly.Treadly.Data.Managers.TreadlyServiceHelper$15  reason: invalid class name */
    /* loaded from: classes2.dex */
    static class AnonymousClass15 implements Runnable {
        final /* synthetic */ JSONObject val$header;
        final /* synthetic */ File val$image;
        final /* synthetic */ TreadlyServiceResponseEventListener val$listener;
        final /* synthetic */ JSONObject val$request;

        AnonymousClass15(JSONObject jSONObject, JSONObject jSONObject2, File file, TreadlyServiceResponseEventListener treadlyServiceResponseEventListener) {
            this.val$request = jSONObject;
            this.val$header = jSONObject2;
            this.val$image = file;
            this.val$listener = treadlyServiceResponseEventListener;
        }

        @Override // java.lang.Runnable
        public void run() {
            JSONObject doJsonRequestFileUpload = TreadlyServiceHelper.doJsonRequestFileUpload(TreadlyServiceHelper.SERVICE_URI_ROUTE_STATS_DEVICE_COMPLETE_POST_UPLOAD, this.val$request, this.val$header, this.val$image);
            if (doJsonRequestFileUpload == null) {
                TreadlyServiceHelper.runOnMain(new Runnable() { // from class: com.treadly.Treadly.Data.Managers.TreadlyServiceHelper.15.1
                    @Override // java.lang.Runnable
                    public void run() {
                        AnonymousClass15.this.val$listener.onUrlResponse(TreadlyServiceHelper.errorCouldNotCommunicateWithServer, null);
                    }
                });
                return;
            }
            final JSONObject optJSONObject = doJsonRequestFileUpload.optJSONObject("data");
            if (optJSONObject != null) {
                final TreadlyServiceResponseEventListener treadlyServiceResponseEventListener = this.val$listener;
                TreadlyServiceHelper.runOnMain(new Runnable() { // from class: com.treadly.Treadly.Data.Managers.-$$Lambda$TreadlyServiceHelper$15$p1rj0jO1aL6N4sv6DtJSdDz6OXI
                    @Override // java.lang.Runnable
                    public final void run() {
                        TreadlyServiceResponseEventListener.this.onUrlResponse(null, optJSONObject.optString("image"));
                    }
                });
                return;
            }
            final TreadlyServiceResponseEventListener treadlyServiceResponseEventListener2 = this.val$listener;
            TreadlyServiceHelper.runOnMain(new Runnable() { // from class: com.treadly.Treadly.Data.Managers.-$$Lambda$TreadlyServiceHelper$15$SEro4jZ7VFq6sjk5A0_DEC53MRQ
                @Override // java.lang.Runnable
                public final void run() {
                    TreadlyServiceResponseEventListener.this.onUrlResponse(TreadlyServiceHelper.errorCouldNotParseResults, null);
                }
            });
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static void sendActivtyPost(String str, String str2, String str3, String str4, final TreadlyServiceResponseEventListener treadlyServiceResponseEventListener) {
        try {
            JSONObject jSONObject = new JSONObject();
            jSONObject.put(key_Id, str2);
            jSONObject.put("comment", (str3 == null || str3.isEmpty()) ? "" : "");
            if (str4 != null) {
                jSONObject.put("image", str4);
            }
            JSONObject jSONObject2 = new JSONObject();
            try {
                jSONObject2.put(keyHeaderAuthorization, str);
            } catch (Exception e) {
                e.printStackTrace();
            }
            RequestUtil.shared.postJson(SERVICE_URI_ROUTE_STATS_DEVICE_COMPLETE_POST_CREATE, jSONObject, jSONObject2, new RequestUtil.RequestUtilListener() { // from class: com.treadly.Treadly.Data.Managers.TreadlyServiceHelper.16
                @Override // com.treadly.Treadly.Data.Utility.RequestUtil.RequestUtilListener
                public void onResponse(TreadlyNetworkResponse treadlyNetworkResponse) {
                    if (!treadlyNetworkResponse.ok() || treadlyNetworkResponse.response == null) {
                        TreadlyServiceHelper.runOnMain(new Runnable() { // from class: com.treadly.Treadly.Data.Managers.TreadlyServiceHelper.16.1
                            @Override // java.lang.Runnable
                            public void run() {
                                TreadlyServiceResponseEventListener.this.onSuccess(TreadlyServiceHelper.errorCouldNotConnectWithServer);
                            }
                        });
                    } else {
                        TreadlyServiceHelper.runOnMain(new Runnable() { // from class: com.treadly.Treadly.Data.Managers.TreadlyServiceHelper.16.2
                            @Override // java.lang.Runnable
                            public void run() {
                                TreadlyServiceResponseEventListener.this.onSuccess(null);
                            }
                        });
                    }
                }
            });
        } catch (JSONException e2) {
            System.out.println("sendingActivtyPost: error");
            e2.printStackTrace();
            runOnMain(new Runnable() { // from class: com.treadly.Treadly.Data.Managers.TreadlyServiceHelper.17
                @Override // java.lang.Runnable
                public void run() {
                    TreadlyServiceResponseEventListener.this.onSuccess("error");
                }
            });
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static UserRunningSessionInfo parseUserRunningSessionInfo(JSONObject jSONObject) {
        try {
            String optString = jSONObject.optString(keyFinishedAt);
            int optInt = jSONObject.optInt("calories");
            double optDouble = jSONObject.optDouble("distance");
            double optDouble2 = jSONObject.optDouble("duration");
            int optInt2 = jSONObject.optInt("steps");
            int optInt3 = jSONObject.optInt(keyCaloriesGoal);
            int optInt4 = jSONObject.optInt(keyStepsGoal);
            double optDouble3 = jSONObject.optDouble(keyDistanceGoal);
            double optDouble4 = jSONObject.optDouble(keyDurationGoal);
            UserDailyGoalType fromValue = UserDailyGoalType.fromValue(jSONObject.optInt("goal_type", UserDailyGoalType.none.getValue()));
            if (jSONObject.has(keyFinishedAt) && jSONObject.has("calories") && jSONObject.has("distance") && jSONObject.has("duration") && jSONObject.has("steps") && jSONObject.has(keyCaloriesGoal) && jSONObject.has(keyStepsGoal)) {
                Date convertTimeStampToDate = convertTimeStampToDate(optString);
                System.out.println("HELPER: AFTER CONVERT: " + convertTimeStampToDate);
                UserRunningSessionInfo userRunningSessionInfo = new UserRunningSessionInfo(convertTimeStampToDate, optInt, optDouble, optDouble2, optInt2, optInt3, optInt4, optDouble3, optDouble4, fromValue, 0);
                JSONArray optJSONArray = jSONObject.optJSONArray(keyDetailed);
                if (optJSONArray != null) {
                    for (int i = 0; i < optJSONArray.length(); i++) {
                        JSONObject jSONObject2 = optJSONArray.getJSONObject(i);
                        String optString2 = jSONObject2.optString(keyCreatedAt);
                        int optInt5 = jSONObject2.optInt("steps");
                        double optDouble5 = jSONObject2.optDouble("speed");
                        if (jSONObject2.has(keyCreatedAt) && jSONObject2.has("steps") && jSONObject2.has("speed")) {
                            userRunningSessionInfo.segments.add(new UserRunningSessionSegmentInfo(convertTimeStampToDate(optString2), optInt5, optDouble5));
                        }
                    }
                }
                JSONArray optJSONArray2 = jSONObject.optJSONArray(keyParticipants);
                if (optJSONArray2 != null) {
                    for (int i2 = 0; i2 < optJSONArray2.length(); i2++) {
                        JSONObject jSONObject3 = optJSONArray2.getJSONObject(i2);
                        String optString3 = jSONObject3.optString(keyJoinedAt);
                        JSONObject optJSONObject = jSONObject3.optJSONObject(keyParticipant);
                        if (jSONObject3.has(keyJoinedAt) && jSONObject3.has(keyParticipant)) {
                            Date convertTimeStampToDate2 = convertTimeStampToDate(optString3);
                            if (optJSONObject.has("id") && optJSONObject.has("name") && optJSONObject.has("avatar")) {
                                String optString4 = optJSONObject.optString("id");
                                String optString5 = optJSONObject.optString("name");
                                String optString6 = optJSONObject.optString("avatar");
                                if (optString6.length() == 0) {
                                    optString6 = "https://dgwxv5s2i5zkb.cloudfront.net/avatar/default.png";
                                }
                                userRunningSessionInfo.particpants.add(new UserRunningSessionParticipantInfo(convertTimeStampToDate2, optString4, optString5, optString6));
                            }
                        }
                    }
                }
                return userRunningSessionInfo;
            }
            return null;
        } catch (JSONException e) {
            System.out.println("parseUserRunningSessionInfo: error parsing");
            e.printStackTrace();
            return null;
        }
    }

    public static void discoverFollowers(String str, final TreadlyServiceResponseEventListener treadlyServiceResponseEventListener) {
        JSONObject jSONObject = new JSONObject();
        JSONObject jSONObject2 = new JSONObject();
        try {
            jSONObject.put(keyHeaderAuthorization, str);
        } catch (Exception e) {
            e.printStackTrace();
        }
        RequestUtil.shared.postJson(SERVICE_URI_ROUTE_DISCOVER_FOLLOWERS_GET, jSONObject2, jSONObject, new RequestUtil.RequestUtilListener() { // from class: com.treadly.Treadly.Data.Managers.-$$Lambda$TreadlyServiceHelper$SYluXCGehx6dU2TtaIDNPmW-T9g
            @Override // com.treadly.Treadly.Data.Utility.RequestUtil.RequestUtilListener
            public final void onResponse(TreadlyNetworkResponse treadlyNetworkResponse) {
                TreadlyServiceHelper.lambda$discoverFollowers$59(TreadlyServiceResponseEventListener.this, treadlyNetworkResponse);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static /* synthetic */ void lambda$discoverFollowers$59(final TreadlyServiceResponseEventListener treadlyServiceResponseEventListener, TreadlyNetworkResponse treadlyNetworkResponse) throws JSONException {
        if (!treadlyNetworkResponse.ok() || treadlyNetworkResponse.response == null) {
            runOnMain(new Runnable() { // from class: com.treadly.Treadly.Data.Managers.-$$Lambda$TreadlyServiceHelper$E0zC60fxwCnODaMadSsQYMJPJkg
                @Override // java.lang.Runnable
                public final void run() {
                    TreadlyServiceResponseEventListener.this.onDiscoverFollowersResponse(TreadlyServiceHelper.errorCouldNotCommunicateWithServer, null);
                }
            });
            return;
        }
        JSONArray jSONArray = treadlyNetworkResponse.response.getJSONArray("data");
        final ArrayList arrayList = new ArrayList();
        if (jSONArray != null) {
            try {
                if (jSONArray.length() != 0) {
                    for (int i = 0; i < jSONArray.length(); i++) {
                        JSONObject optJSONObject = jSONArray.optJSONObject(i);
                        JSONObject optJSONObject2 = optJSONObject.optJSONObject("user");
                        if (optJSONObject2 != null && optJSONObject2.has(key_Id) && optJSONObject2.has("name") && optJSONObject2.has("avatar") && optJSONObject.has(keyStepsTotal) && optJSONObject.has(keyPrivate)) {
                            String optString = optJSONObject2.optString(key_Id);
                            String optString2 = optJSONObject2.optString("name");
                            String optString3 = optJSONObject2.optString("avatar");
                            if (optString3.length() == 0) {
                                optString3 = "https://dgwxv5s2i5zkb.cloudfront.net/avatar/default.png";
                            }
                            arrayList.add(new UserDiscoverInfo(optString, optString2, optString3, optJSONObject.optString("description"), optJSONObject.optInt(keyStepsTotal), optJSONObject.optBoolean(keyPrivate)));
                            if (arrayList.size() >= 50) {
                                break;
                            }
                        }
                    }
                    runOnMain(new Runnable() { // from class: com.treadly.Treadly.Data.Managers.-$$Lambda$TreadlyServiceHelper$CbrJtPxhZm6Fy77ZykjApSerzdc
                        @Override // java.lang.Runnable
                        public final void run() {
                            TreadlyServiceResponseEventListener.this.onDiscoverFollowersResponse(null, arrayList);
                        }
                    });
                    return;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        runOnMain(new Runnable() { // from class: com.treadly.Treadly.Data.Managers.-$$Lambda$TreadlyServiceHelper$neYtPRsWM6mUhc6TLMp7Y6KWifA
            @Override // java.lang.Runnable
            public final void run() {
                TreadlyServiceResponseEventListener.this.onDiscoverFollowersResponse(TreadlyServiceHelper.errorCouldNotCommunicateWithServer, null);
            }
        });
    }

    public static void addFollowRequest(String str, String str2, final TreadlyServiceResponseEventListener treadlyServiceResponseEventListener) {
        JSONObject jSONObject = new JSONObject();
        JSONObject jSONObject2 = new JSONObject();
        try {
            jSONObject.put(keyHeaderAuthorization, str);
            jSONObject2.put("user", str2);
        } catch (Exception e) {
            e.printStackTrace();
        }
        RequestUtil.shared.postJson(SERVICE_URI_ROUTE_USERS_FOLLOW_REQUEST_ADD, jSONObject2, jSONObject, new RequestUtil.RequestUtilListener() { // from class: com.treadly.Treadly.Data.Managers.-$$Lambda$TreadlyServiceHelper$Z7ltQsVqSXE4e0VgjnKpcotYsWA
            @Override // com.treadly.Treadly.Data.Utility.RequestUtil.RequestUtilListener
            public final void onResponse(TreadlyNetworkResponse treadlyNetworkResponse) {
                TreadlyServiceHelper.lambda$addFollowRequest$61(TreadlyServiceResponseEventListener.this, treadlyNetworkResponse);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static /* synthetic */ void lambda$addFollowRequest$61(final TreadlyServiceResponseEventListener treadlyServiceResponseEventListener, TreadlyNetworkResponse treadlyNetworkResponse) throws JSONException {
        if (!treadlyNetworkResponse.ok() || treadlyNetworkResponse.response == null) {
            runOnMain(new Runnable() { // from class: com.treadly.Treadly.Data.Managers.-$$Lambda$TreadlyServiceHelper$NTTVAKpZtECApjnAM3bbGfc82q0
                @Override // java.lang.Runnable
                public final void run() {
                    TreadlyServiceResponseEventListener.this.onFollowResponse(TreadlyServiceHelper.errorCouldNotCommunicateWithServer);
                }
            });
        } else {
            treadlyServiceResponseEventListener.onFollowResponse(null);
        }
    }

    public static void unfollowUser(String str, String str2, final TreadlyServiceResponseEventListener treadlyServiceResponseEventListener) {
        JSONObject jSONObject = new JSONObject();
        JSONObject jSONObject2 = new JSONObject();
        try {
            jSONObject.put(keyHeaderAuthorization, str);
            jSONObject2.put(keyFriendId, str2);
        } catch (Exception e) {
            e.printStackTrace();
        }
        RequestUtil.shared.postJson(SERVICE_URI_ROUTE_USERS_UNFOLLOW, jSONObject2, jSONObject, new RequestUtil.RequestUtilListener() { // from class: com.treadly.Treadly.Data.Managers.-$$Lambda$TreadlyServiceHelper$DNSzsirJtx8_oW5XRszp8Q21yMI
            @Override // com.treadly.Treadly.Data.Utility.RequestUtil.RequestUtilListener
            public final void onResponse(TreadlyNetworkResponse treadlyNetworkResponse) {
                TreadlyServiceHelper.lambda$unfollowUser$63(TreadlyServiceResponseEventListener.this, treadlyNetworkResponse);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static /* synthetic */ void lambda$unfollowUser$63(final TreadlyServiceResponseEventListener treadlyServiceResponseEventListener, TreadlyNetworkResponse treadlyNetworkResponse) throws JSONException {
        if (!treadlyNetworkResponse.ok() || treadlyNetworkResponse.response == null) {
            runOnMain(new Runnable() { // from class: com.treadly.Treadly.Data.Managers.-$$Lambda$TreadlyServiceHelper$IQj9EaWPsN_jsBO2--8RhpH-ZcA
                @Override // java.lang.Runnable
                public final void run() {
                    TreadlyServiceResponseEventListener.this.onUnfollowResponse(TreadlyServiceHelper.errorCouldNotCommunicateWithServer);
                }
            });
        } else {
            treadlyServiceResponseEventListener.onUnfollowResponse(null);
        }
    }

    public static void getCurrentUserFollowersInfo(String str, String str2, final TreadlyServiceResponseEventListener treadlyServiceResponseEventListener) {
        JSONObject jSONObject = new JSONObject();
        JSONObject jSONObject2 = new JSONObject();
        try {
            jSONObject.put(keyHeaderAuthorization, str);
            jSONObject2.put("user", str2);
        } catch (Exception e) {
            e.printStackTrace();
        }
        RequestUtil.shared.postJson(SERVICE_URI_ROUTE_USERS_FOLLOWERS, jSONObject2, jSONObject, new RequestUtil.RequestUtilListener() { // from class: com.treadly.Treadly.Data.Managers.-$$Lambda$TreadlyServiceHelper$w36X9jYxdcmMttg7WMFkgJ6sG2s
            @Override // com.treadly.Treadly.Data.Utility.RequestUtil.RequestUtilListener
            public final void onResponse(TreadlyNetworkResponse treadlyNetworkResponse) {
                TreadlyServiceHelper.lambda$getCurrentUserFollowersInfo$67(TreadlyServiceResponseEventListener.this, treadlyNetworkResponse);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static /* synthetic */ void lambda$getCurrentUserFollowersInfo$67(final TreadlyServiceResponseEventListener treadlyServiceResponseEventListener, TreadlyNetworkResponse treadlyNetworkResponse) throws JSONException {
        if (!treadlyNetworkResponse.ok() || treadlyNetworkResponse.response == null) {
            runOnMain(new Runnable() { // from class: com.treadly.Treadly.Data.Managers.-$$Lambda$TreadlyServiceHelper$wyy6lm6qvlOnKWy2yosAb_sb2a4
                @Override // java.lang.Runnable
                public final void run() {
                    TreadlyServiceResponseEventListener.this.onGetFollowersResponse(TreadlyServiceHelper.errorCouldNotCommunicateWithServer, null);
                }
            });
            return;
        }
        final ArrayList arrayList = new ArrayList();
        JSONArray jSONArray = treadlyNetworkResponse.response.getJSONArray("data");
        if (jSONArray != null) {
            for (int i = 0; i < jSONArray.length(); i++) {
                try {
                    JSONObject optJSONObject = jSONArray.optJSONObject(i);
                    JSONObject optJSONObject2 = optJSONObject.optJSONObject("user");
                    if (optJSONObject2 != null && optJSONObject2.has("id") && optJSONObject2.has("name") && optJSONObject2.has("avatar") && optJSONObject.has("description") && optJSONObject.has("steps") && optJSONObject.has(keyIsFollowing) && optJSONObject.has(keyIsFollower)) {
                        String optString = optJSONObject2.optString("id");
                        String optString2 = optJSONObject2.optString("name");
                        String optString3 = optJSONObject2.optString("avatar");
                        if (optString3.length() == 0) {
                            optString3 = "https://dgwxv5s2i5zkb.cloudfront.net/avatar/default.png";
                        }
                        arrayList.add(new FollowInfo(optString, optString2, optString3, optJSONObject.optString("description"), optJSONObject.optInt("steps"), optJSONObject.optBoolean(keyIsFollowing), optJSONObject.optBoolean(keyIsFollower)));
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            runOnMain(new Runnable() { // from class: com.treadly.Treadly.Data.Managers.-$$Lambda$TreadlyServiceHelper$shsN3dUE9hvZIGLcYzy92aJS-lw
                @Override // java.lang.Runnable
                public final void run() {
                    TreadlyServiceResponseEventListener.this.onGetFollowersResponse(null, arrayList);
                }
            });
            return;
        }
        runOnMain(new Runnable() { // from class: com.treadly.Treadly.Data.Managers.-$$Lambda$TreadlyServiceHelper$mjdZ7zhOvlDLz5LbgW4wND4EkA8
            @Override // java.lang.Runnable
            public final void run() {
                TreadlyServiceResponseEventListener.this.onGetFollowersResponse(TreadlyServiceHelper.errorCouldNotCommunicateWithServer, null);
            }
        });
    }

    public static void getCurrentUserFollowingInfo(String str, String str2, final TreadlyServiceResponseEventListener treadlyServiceResponseEventListener) {
        JSONObject jSONObject = new JSONObject();
        JSONObject jSONObject2 = new JSONObject();
        try {
            jSONObject.put(keyHeaderAuthorization, str);
            jSONObject2.put("user", str2);
        } catch (Exception e) {
            e.printStackTrace();
        }
        RequestUtil.shared.postJson(SERVICE_URI_ROUTE_USERS_FOLLOWING_MEMBERS, jSONObject2, jSONObject, new RequestUtil.RequestUtilListener() { // from class: com.treadly.Treadly.Data.Managers.-$$Lambda$TreadlyServiceHelper$g4enhMbdib4Q_vv4MqjhtBKLHUE
            @Override // com.treadly.Treadly.Data.Utility.RequestUtil.RequestUtilListener
            public final void onResponse(TreadlyNetworkResponse treadlyNetworkResponse) {
                TreadlyServiceHelper.lambda$getCurrentUserFollowingInfo$71(TreadlyServiceResponseEventListener.this, treadlyNetworkResponse);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static /* synthetic */ void lambda$getCurrentUserFollowingInfo$71(final TreadlyServiceResponseEventListener treadlyServiceResponseEventListener, TreadlyNetworkResponse treadlyNetworkResponse) throws JSONException {
        if (!treadlyNetworkResponse.ok() || treadlyNetworkResponse.response == null) {
            runOnMain(new Runnable() { // from class: com.treadly.Treadly.Data.Managers.-$$Lambda$TreadlyServiceHelper$5kTTsnn-PH8AYdKSzYXiH6Yaep8
                @Override // java.lang.Runnable
                public final void run() {
                    TreadlyServiceResponseEventListener.this.onGetFollowingResponse(TreadlyServiceHelper.errorCouldNotCommunicateWithServer, null);
                }
            });
            return;
        }
        final ArrayList arrayList = new ArrayList();
        JSONArray jSONArray = treadlyNetworkResponse.response.getJSONArray("data");
        if (jSONArray != null) {
            for (int i = 0; i < jSONArray.length(); i++) {
                try {
                    JSONObject optJSONObject = jSONArray.optJSONObject(i);
                    JSONObject optJSONObject2 = optJSONObject.optJSONObject("user");
                    if (optJSONObject2 != null && optJSONObject2.has(key_Id) && optJSONObject2.has("name") && optJSONObject2.has("avatar") && optJSONObject.has("description") && optJSONObject.has("steps") && optJSONObject.has(keyIsFollowing) && optJSONObject.has(keyIsFollower)) {
                        String optString = optJSONObject2.optString(key_Id);
                        String optString2 = optJSONObject2.optString("name");
                        String optString3 = optJSONObject2.optString("avatar");
                        if (optString3.length() == 0) {
                            optString3 = "https://dgwxv5s2i5zkb.cloudfront.net/avatar/default.png";
                        }
                        arrayList.add(new FollowInfo(optString, optString2, optString3, optJSONObject.optString("description"), optJSONObject.optInt("steps"), optJSONObject.optBoolean(keyIsFollowing), optJSONObject.optBoolean(keyIsFollower)));
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            runOnMain(new Runnable() { // from class: com.treadly.Treadly.Data.Managers.-$$Lambda$TreadlyServiceHelper$CbtnsRSYkjkeIVdQhFGcVUxwW5I
                @Override // java.lang.Runnable
                public final void run() {
                    TreadlyServiceResponseEventListener.this.onGetFollowingResponse(null, arrayList);
                }
            });
            return;
        }
        runOnMain(new Runnable() { // from class: com.treadly.Treadly.Data.Managers.-$$Lambda$TreadlyServiceHelper$Jie12axt2B1BfxT97JhKnV-vsR0
            @Override // java.lang.Runnable
            public final void run() {
                TreadlyServiceResponseEventListener.this.onGetFollowingResponse(TreadlyServiceHelper.errorCouldNotCommunicateWithServer, null);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static void runOnMain(Runnable runnable) {
        new Handler(Looper.getMainLooper()).post(runnable);
    }
}
