package com.treadly.Treadly.UI.TreadlyVideo.Data;

import android.net.Uri;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import com.treadly.Treadly.Data.Managers.TreadlyEventManager;
import com.treadly.Treadly.Data.Managers.TreadlyServiceManager;
import com.treadly.Treadly.Data.Model.BuddyProfileInfo;
import com.treadly.Treadly.Data.Model.ReportReasonInfo;
import com.treadly.Treadly.Data.Model.StreamPermission;
import com.treadly.Treadly.Data.Model.TrainerModeEnabledState;
import com.treadly.Treadly.Data.Model.TreadlyNetworkResponse;
import com.treadly.Treadly.Data.Model.UserNotificationSettingInfo;
import com.treadly.Treadly.Data.Model.UserNotificationSettingRequest;
import com.treadly.Treadly.Data.Model.UserRunningSessionParticipantInfo;
import com.treadly.Treadly.Data.Model.UserTokenInfo;
import com.treadly.Treadly.Data.Model.VideoLikeInfo;
import com.treadly.Treadly.Data.Model.VideoLikeUserInfo;
import com.treadly.Treadly.Data.Model.WorkoutInfo;
import com.treadly.Treadly.Data.Utility.RequestUtil;
import com.treadly.Treadly.UI.TreadlyVideo.Data.VideoServiceHelper;
import java.io.File;
import java.io.FileInputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/* loaded from: classes2.dex */
public class VideoServiceHelper {
    public static final String SERVICE_BAMBUSER_ARCHIVE_UPLOAD_ROUTE = "https://env-staging.treadly.co:8080/vendor/bambuser/v1/archive/upload";
    public static final String SERVICE_BAMBUSER_URI_ROUTE_START_SESSION_BROADCAST = "https://env-staging.treadly.co:8080/vendor/bambuser/v1/start_session_broadcast";
    public static final String SERVICE_BAMBUSER_URI_ROUTE_STOP_SESSION_BROADCAST = "https://env-staging.treadly.co:8080/vendor/bambuser/v1/stop_session_broadcast";
    public static final String SERVICE_TOKBOX_ARCHIVE_UPLOAD_ROUTE = "https://env-staging.treadly.co:8080/vendor/tokbox/v1/archive/upload";
    public static final String SERVICE_URI_BASE = "https://env-staging.treadly.co:8080/vendor/tokbox/v1";
    public static final String SERVICE_URI_ROUTE_CREATE_SESSION = "https://env-staging.treadly.co:8080/vendor/tokbox/v1/create_session";
    public static final String SERVICE_URI_ROUTE_DELETE_ARCHIVE = "https://env-staging.treadly.co:8080/vendor/tokbox/v1/delete_archive";
    public static final String SERVICE_URI_ROUTE_GET_ARCHIVE_LIST = "https://env-staging.treadly.co:8080/vendor/tokbox/v1/get_archive_list";
    public static final String SERVICE_URI_ROUTE_GET_BROADCAST_ID = "https://env-staging.treadly.co:8080/vendor/tokbox/v1/get_broadcast_id";
    public static final String SERVICE_URI_ROUTE_GET_CREDENTIALS = "https://env-staging.treadly.co:8080/vendor/tokbox/v1/get_credentials";
    public static final String SERVICE_URI_ROUTE_GET_SESSION_BROADCAST_INFO = "https://env-staging.treadly.co:8080/vendor/tokbox/v1/get_session_broadcast_info";
    public static final String SERVICE_URI_ROUTE_GET_SESSION_ID = "https://env-staging.treadly.co:8080/vendor/tokbox/v1/get_session_id";
    public static final String SERVICE_URI_ROUTE_GET_SESSION_INFO_ALL = "https://env-staging.treadly.co:8080/vendor/tokbox/v1/get_session_info/all";
    public static final String SERVICE_URI_ROUTE_GET_SESSION_INFO_USER = "https://env-staging.treadly.co:8080/vendor/tokbox/v1/get_session_info/user";
    public static final String SERVICE_URI_ROUTE_GET_SESSION_TOKEN_PUBLISHER = "https://env-staging.treadly.co:8080/vendor/tokbox/v1/get_session_token/publisher";
    public static final String SERVICE_URI_ROUTE_GET_SESSION_TOKEN_PUBLISHER_HOST = "https://env-staging.treadly.co:8080/vendor/tokbox/v1/get_session_token/publisher/host";
    public static final String SERVICE_URI_ROUTE_GET_SESSION_TOKEN_SUBSCRIBER = "https://env-staging.treadly.co:8080/vendor/tokbox/v1/get_session_token/subscriber";
    public static final String SERVICE_URI_ROUTE_START_SESSION_ARCHIVE = "https://env-staging.treadly.co:8080/vendor/tokbox/v1/start_session_archive";
    public static final String SERVICE_URI_ROUTE_START_SESSION_BROADCAST = "https://env-staging.treadly.co:8080/vendor/tokbox/v1/start_session_broadcast";
    public static final String SERVICE_URI_ROUTE_STOP_SESSION_ARCHIVE = "https://env-staging.treadly.co:8080/vendor/tokbox/v1/stop_session_archive";
    public static final String SERVICE_URI_ROUTE_STOP_SESSION_BROADCAST = "https://env-staging.treadly.co:8080/vendor/tokbox/v1/stop_session_broadcast";
    public static final String SERVICE_VIDEO_BLACKLIST_WORDS_ALL = "https://env-staging.treadly.co:8080/vendor/blacklist/v1/all";
    public static final String SERVICE_VIDEO_BUDDY_PROFILE_AUDIO_UPLOAD_ROUTE = "https://env-staging.treadly.co:8080/vendor/buddy/profile/audio/upload";
    public static final String SERVICE_VIDEO_BUDDY_PROFILE_CREATE_ROUTE = "https://env-staging.treadly.co:8080/vendor/buddy/profile/create";
    public static final String SERVICE_VIDEO_BUDDY_PROFILE_DELETE_ROUTE = "https://env-staging.treadly.co:8080/vendor/buddy/profile/delete";
    public static final String SERVICE_VIDEO_BUDDY_PROFILE_GET_ALL_ROUTE = "https://env-staging.treadly.co:8080/vendor/buddy/profile/get/all";
    public static final String SERVICE_VIDEO_BUDDY_PROFILE_GET_DAY_TIME_TYPES_ROUTE = "https://env-staging.treadly.co:8080/vendor/buddy/tod/get/all";
    public static final String SERVICE_VIDEO_BUDDY_PROFILE_GET_FILTERS_ROUTE = "https://env-staging.treadly.co:8080/vendor/buddy/filters/get/all";
    public static final String SERVICE_VIDEO_BUDDY_PROFILE_GET_INTERESTS_ROUTE = "https://env-staging.treadly.co:8080/vendor/buddy/interest/get/all";
    public static final String SERVICE_VIDEO_BUDDY_PROFILE_GET_ROUTE = "https://env-staging.treadly.co:8080/vendor/buddy/profile/get";
    public static final String SERVICE_VIDEO_BUDDY_PROFILE_UPDATE_ROUTE = "https://env-staging.treadly.co:8080/vendor/buddy/profile/update";
    public static final String SERVICE_VIDEO_BUDDY_PROFILE_VIDEO_UPLOAD_ROUTE = "https://env-staging.treadly.co:8080/vendor/buddy/profile/video/upload";
    public static final String SERVICE_VIDEO_CREATE_WORKOUT_ROUTE = "https://env-staging.treadly.co:8080/vendor/video/v1/create_workout";
    public static final String SERVICE_VIDEO_FEED_HOME_GET_ROUTE = "https://env-staging.treadly.co:8080/vendor/video/v1/feed/home/get";
    public static final String SERVICE_VIDEO_GET_TRAINER_MODE = "https://env-staging.treadly.co:8080/vendor/users/trainer_mode/get";
    public static final String SERVICE_VIDEO_GET_VIDEO_FEED = "https://env-staging.treadly.co:8080/vendor/video/v1/get_video_feed";
    public static final String SERVICE_VIDEO_GET_VIDEO_LIST = "https://env-staging.treadly.co:8080/vendor/video/v1/get_video_list";
    public static final String SERVICE_VIDEO_GROUP_ALL_ROUTE = "https://env-staging.treadly.co:8080/vendor/video/v1/group/all";
    public static final String SERVICE_VIDEO_GROUP_CREATE_ROUTE = "https://env-staging.treadly.co:8080/vendor/video/v1/group/create";
    public static final String SERVICE_VIDEO_GROUP_CURRENT_ROUTE = "https://env-staging.treadly.co:8080/vendor/video/v1/group/current";
    public static final String SERVICE_VIDEO_GROUP_DELETE_ROUTE = "https://env-staging.treadly.co:8080/vendor/video/v1/group/delete";
    public static final String SERVICE_VIDEO_GROUP_GET_ROUTE = "https://env-staging.treadly.co:8080/vendor/video/v1/group/get";
    public static final String SERVICE_VIDEO_GROUP_MEMBER_DELETE_ROUTE = "https://env-staging.treadly.co:8080/vendor/video/v1/group/member/delete";
    public static final String SERVICE_VIDEO_GROUP_MEMBER_GET_ROUTE = "https://env-staging.treadly.co:8080/vendor/video/v1/group/member/get";
    public static final String SERVICE_VIDEO_GROUP_MEMBER_INVITE_CREATE_ROUTE = "https://env-staging.treadly.co:8080/vendor/video/v1/group/member/invite/create";
    public static final String SERVICE_VIDEO_GROUP_MEMBER_INVITE_DELETE_ROUTE = "https://env-staging.treadly.co:8080/vendor/video/v1/group/member/invite/delete";
    public static final String SERVICE_VIDEO_GROUP_MEMBER_INVITE_GET_ROUTE = "https://env-staging.treadly.co:8080/vendor/video/v1/group/member/invite/get";
    public static final String SERVICE_VIDEO_GROUP_MEMBER_INVITE_PENDING_APPROVE_ROUTE = "https://env-staging.treadly.co:8080/vendor/video/v1/group/member/invite/pending/approve";
    public static final String SERVICE_VIDEO_GROUP_MEMBER_INVITE_PENDING_DECLINE_ROUTE = "https://env-staging.treadly.co:8080/vendor/video/v1/group/member/invite/pending/decline";
    public static final String SERVICE_VIDEO_GROUP_MEMBER_INVITE_PENDING_GET_ROUTE = "https://env-staging.treadly.co:8080/vendor/video/v1/group/member/invite/pending/get";
    public static final String SERVICE_VIDEO_GROUP_MEMBER_JOIN_ROUTE = "https://env-staging.treadly.co:8080/vendor/video/v1/group/member/join";
    public static final String SERVICE_VIDEO_GROUP_MEMBER_LEAVE_ROUTE = "https://env-staging.treadly.co:8080/vendor/video/v1/group/member/leave";
    public static final String SERVICE_VIDEO_GROUP_MEMBER_REQUEST_CREATE_ROUTE = "https://env-staging.treadly.co:8080/vendor/video/v1/group/member/request/create";
    public static final String SERVICE_VIDEO_GROUP_MEMBER_REQUEST_DELETE_ROUTE = "https://env-staging.treadly.co:8080/vendor/video/v1/group/member/request/delete";
    public static final String SERVICE_VIDEO_GROUP_MEMBER_REQUEST_GET_ROUTE = "https://env-staging.treadly.co:8080/vendor/video/v1/group/member/request/get";
    public static final String SERVICE_VIDEO_GROUP_MEMBER_REQUEST_PENDING_APPROVE_ROUTE = "https://env-staging.treadly.co:8080/vendor/video/v1/group/member/request/pending/approve";
    public static final String SERVICE_VIDEO_GROUP_MEMBER_REQUEST_PENDING_DECLINE_ROUTE = "https://env-staging.treadly.co:8080/vendor/video/v1/group/member/request/pending/decline";
    public static final String SERVICE_VIDEO_GROUP_MEMBER_REQUEST_PENDING_GET_ROUTE = "https://env-staging.treadly.co:8080/vendor/video/v1/group/member/request/pending/get";
    public static final String SERVICE_VIDEO_GROUP_NEARBY_ROUTE = "https://env-staging.treadly.co:8080/vendor/video/v1/group/nearby";
    public static final String SERVICE_VIDEO_GROUP_POPULAR_ROUTE = "https://env-staging.treadly.co:8080/vendor/video/v1/group/popular";
    public static final String SERVICE_VIDEO_GROUP_POST_ALL_ROUTE = "https://env-staging.treadly.co:8080/vendor/video/v1/group/post/all";
    public static final String SERVICE_VIDEO_GROUP_POST_COMMENT_ALL_ROUTE = "https://env-staging.treadly.co:8080/vendor/video/v1/group/post/comment/all";
    public static final String SERVICE_VIDEO_GROUP_POST_COMMENT_CREATE_ROUTE = "https://env-staging.treadly.co:8080/vendor/video/v1/group/post/comment/create";
    public static final String SERVICE_VIDEO_GROUP_POST_COMMENT_DELETE_ROUTE = "https://env-staging.treadly.co:8080/vendor/video/v1/group/post/comment/delete";
    public static final String SERVICE_VIDEO_GROUP_POST_COMMENT_FLAG_CREATE_ROUTE = "https://env-staging.treadly.co:8080/vendor/video/v1/group/post/comment/flag/create";
    public static final String SERVICE_VIDEO_GROUP_POST_COMMENT_FLAG_DELETE_ROUTE = "https://env-staging.treadly.co:8080/vendor/video/v1/group/post/comment/flag/delete";
    public static final String SERVICE_VIDEO_GROUP_POST_COMMENT_REPORT_CREATE_ROUTE = "https://env-staging.treadly.co:8080/vendor/video/v1/group/post/comment/report/create";
    public static final String SERVICE_VIDEO_GROUP_POST_CREATE_ROUTE = "https://env-staging.treadly.co:8080/vendor/video/v1/group/post/create";
    public static final String SERVICE_VIDEO_GROUP_POST_DELETE_ROUTE = "https://env-staging.treadly.co:8080/vendor/video/v1/group/post/delete";
    public static final String SERVICE_VIDEO_GROUP_POST_GET_ROUTE = "https://env-staging.treadly.co:8080/vendor/video/v1/group/post/get";
    public static final String SERVICE_VIDEO_GROUP_POST_LIKE_CREATE_ROUTE = "https://env-staging.treadly.co:8080/vendor/video/v1/group/post/like/create";
    public static final String SERVICE_VIDEO_GROUP_POST_LIKE_DELETE_ROUTE = "https://env-staging.treadly.co:8080/vendor/video/v1/group/post/like/delete";
    public static final String SERVICE_VIDEO_GROUP_POST_REPORT_CREATE_ROUTE = "https://env-staging.treadly.co:8080/vendor/video/v1/group/post/report/create";
    public static final String SERVICE_VIDEO_GROUP_POST_UPDATE_ROUTE = "https://env-staging.treadly.co:8080/vendor/video/v1/group/post/update";
    public static final String SERVICE_VIDEO_GROUP_SCHEDULE_FOLLOW_CREATE_ROUTE = "https://env-staging.treadly.co:8080/vendor/video/v1/group/schedule/follow/create";
    public static final String SERVICE_VIDEO_GROUP_SCHEDULE_FOLLOW_DELETE_ROUTE = "https://env-staging.treadly.co:8080/vendor/video/v1/group/schedule/follow/delete";
    public static final String SERVICE_VIDEO_GROUP_UPDATE_ROUTE = "https://env-staging.treadly.co:8080/vendor/video/v1/group/update";
    public static final String SERVICE_VIDEO_GROUP_VIDEO_GET_ROUTE = "https://env-staging.treadly.co:8080/vendor/video/v1/group/video/get";
    public static final String SERVICE_VIDEO_GROUP_WAITING_ROOM_GET_ROUTE = "https://env-staging.treadly.co:8080/vendor/video/v1/group/waiting_room/get";
    public static final String SERVICE_VIDEO_INBOX_MESSAGE_ALL_ROUTE = "https://env-staging.treadly.co:8080/vendor/video/v1/inbox/message/all";
    public static final String SERVICE_VIDEO_INBOX_MESSAGE_DELETE_ALL_ROUTE = "https://env-staging.treadly.co:8080/vendor/video/v1/inbox/message/delete/all";
    public static final String SERVICE_VIDEO_INBOX_MESSAGE_DELETE_ROUTE = "https://env-staging.treadly.co:8080/vendor/video/v1/inbox/message/delete";
    public static final String SERVICE_VIDEO_INBOX_MESSAGE_UPDATE_READ_ROUTE = "https://env-staging.treadly.co:8080/vendor/video/v1/inbox/message/update/read";
    public static final String SERVICE_VIDEO_INVITE_CODE_ACTIVATE = "https://env-staging.treadly.co:8080/vendor/users/invite_code/activate";
    public static final String SERVICE_VIDEO_NOTIFICATION_REGISTER_ROUTE = "https://env-staging.treadly.co:8080/vendor/video/v1/notification/android/register";
    public static final String SERVICE_VIDEO_NOTIFICATION_SETTING_GET_ROUTE = "https://env-staging.treadly.co:8080/vendor/video/v1/notification/setting/get";
    public static final String SERVICE_VIDEO_NOTIFICATION_SETTING_UPDATE_ROUTE = "https://env-staging.treadly.co:8080/vendor/video/v1/notification/setting/update";
    public static final String SERVICE_VIDEO_NOTIFICATION_UNREGISTER_ROUTE = "https://env-staging.treadly.co:8080/vendor/video/v1/notification/android/unregister";
    public static final String SERVICE_VIDEO_PRELAUNCH_GET_ROUTE = "https://env-staging.treadly.co:8080/vendor/video/v1/prelaunch/get";
    public static final String SERVICE_VIDEO_REPORT_REASON_GET_ROUTE = "https://env-staging.treadly.co:8080/vendor/video/v1/report/reason/get";
    public static final String SERVICE_VIDEO_RUNNING_SESSION_PARTICIPANT_GET_ROUTE = "https://env-staging.treadly.co:8080/vendor/video/v1/running_session/participant/get";
    public static final String SERVICE_VIDEO_SEARCH_VIDEO_LIST = "https://env-staging.treadly.co:8080/vendor/video/v1/search_video_list";
    public static final String SERVICE_VIDEO_URI_BASE = "https://env-staging.treadly.co:8080";
    public static final String SERVICE_VIDEO_USER_BLOCK_CREATE_ROUTE = "https://env-staging.treadly.co:8080/vendor/video/v1/user/block/create";
    public static final String SERVICE_VIDEO_USER_BLOCK_DELETE_ROUTE = "https://env-staging.treadly.co:8080/vendor/video/v1/user/block/delete";
    public static final String SERVICE_VIDEO_USER_REPORT_CREATE_ROUTE = "https://env-staging.treadly.co:8080/vendor/video/v1/user/report/create";
    public static final String SERVICE_VIDEO_VIDEO_DELETE_ROUTE = "https://env-staging.treadly.co:8080/vendor/video/v1/video/delete";
    public static final String SERVICE_VIDEO_VIDEO_GET_ROUTE = "https://env-staging.treadly.co:8080/vendor/video/v1/video/get";
    public static final String SERVICE_VIDEO_VIDEO_LIKE_CREATE_ROUTE = "https://env-staging.treadly.co:8080/vendor/video/v1/video/like/create";
    public static final String SERVICE_VIDEO_VIDEO_LIKE_DELETE_ROUTE = "https://env-staging.treadly.co:8080/vendor/video/v1/video/like/delete";
    public static final String SERVICE_VIDEO_VIDEO_LIKE_GET_ROUTE = "https://env-staging.treadly.co:8080/vendor/video/v1/video/like/get";
    public static final String SERVICE_VIDEO_VIDEO_LIKE_USER_GET_ROUTE = "https://env-staging.treadly.co:8080/vendor/video/v1/video/like/users/get";
    public static final String SERVICE_VIDEO_VIDEO_RECORDING_COMPOSITE_ALL_ROUTE = "https://env-staging.treadly.co:8080/vendor/video/v1/video/recording/composite/all";
    public static final String SERVICE_VIDEO_VIDEO_RECORDING_COMPOSITE_CREATE_ROUTE = "https://env-staging.treadly.co:8080/vendor/video/v1/video/recording/composite/create";
    public static final String SERVICE_VIDEO_VIDEO_RECORDING_COMPOSITE_GET_ROUTE = "https://env-staging.treadly.co:8080/vendor/video/v1/video/recording/composite/get";
    public static final String SERVICE_VIDEO_VIDEO_RECORDING_COMPOSITE_PARTICIPANT_CREATE_ROUTE = "https://env-staging.treadly.co:8080/vendor/video/v1/video/recording/composite/participant/create";
    public static final String SERVICE_VIDEO_VIDEO_RECORDING_COMPOSITE_UPDATE_ROUTE = "https://env-staging.treadly.co:8080/vendor/video/v1/video/recording/composite/update";
    public static final String SERVICE_VIDEO_VIDEO_RECORDING_COMPOSITE_UPLOAD_ROUTE = "https://env-staging.treadly.co:8080/vendor/video/v1/video/recording/composite/upload";
    public static final String SERVICE_VIDEO_VIDEO_REPORT_CREATE_ROUTE = "https://env-staging.treadly.co:8080/vendor/video/v1/video/report/create";
    public static final String SERVICE_VIDEO_VIDEO_UPDATE_PLACEHOLDER_ROUTE = "https://env-staging.treadly.co:8080/vendor/video/v1/video/update/placeholder";
    public static final String SERVICE_VIDEO_VIDEO_UPDATE_ROUTE = "https://env-staging.treadly.co:8080/vendor/video/v1/video/update";
    public static final String SERVICE_VIDEO_VIDEO_UPDATE_THUMBNAIL_PLACEHOLDER_ROUTE = "https://env-staging.treadly.co:8080/vendor/video/v1/video/update/;thumbnail_placeholder";
    public static final String SERVICE_VIDEO_VIDEO_UPDATE_THUMBNAIL_ROUTE = "https://env-staging.treadly.co:8080/vendor/video/v1/video/update/thumbnail";
    protected static final String defaultAvatarPath = "https://dgwxv5s2i5zkb.cloudfront.net/avatar/default.png";
    protected static final String errorCouldNotCommunicateWithServer = "Could not communicate with server";
    protected static final String errorCouldNotConnectWithServer = "Could not connect with server";
    protected static final String errorCouldNotCreateBuddyProfile = "There was an error creating your profile. Please try again.";
    protected static final String errorCouldNotUpdateBuddyProfile = "There was an error updating your profile. Please try again.";
    protected static final String errorNoBuddyProfileFound = "Error: User has no buddy profile";
    protected static final String errorUserNotLoggedIn = "Error: No user currently logged in";
    protected static final String keyAllowComments = "allow_comments";
    protected static final String keyAllowMemberComment = "allow_member_comment";
    protected static final String keyAllowMemberPost = "allow_member_post";
    protected static final String keyAllowVoiceMessage = "allow_voice_message";
    protected static final String keyApiKey = "api_key";
    protected static final String keyApprovedAt = "approved_at";
    protected static final String keyArchiveId = "archive_id";
    protected static final String keyArchiveListId = "archive_list";
    protected static final String keyAudioUrl = "audio_url";
    protected static final String keyAuthUserCanFollow = "auth_user_can_follow";
    protected static final String keyAuthUserIsFollow = "auth_user_is_follow";
    protected static final String keyAverageSpeed = "avg_speed";
    protected static final String keyBannerUrl = "banner_url";
    protected static final String keyBroadcastId = "broadcast_id";
    protected static final String keyBroadcastUrl = "broadcast_url";
    protected static final String keyBuddyProfileId = "buddy_profile_id";
    protected static final String keyBuddyProfiles = "buddy_profiles";
    protected static final String keyBundleId = "bundle_id";
    protected static final String keyCalories = "calories";
    protected static final String keyCommentCount = "comment_count";
    protected static final String keyCommentId = "comment_id";
    protected static final String keyCommentList = "comment_list";
    protected static final String keyCompact = "compact";
    protected static final String keyCompositeId = "composite_id";
    protected static final String keyCompositeList = "composite_list";
    protected static final String keyCreatedAt = "created_at";
    protected static final String keyDailyGoal100Achieved = "daily_goal_100_achieved";
    protected static final String keyDailyGoal50Achieved = "daily_goal_50_achieved";
    protected static final String keyData = "data";
    protected static final String keyDataPayload = "data_payload";
    protected static final String keyDayOfWeek = "day_of_week";
    protected static final String keyDaysOfWeek = "days_of_week";
    protected static final String keyDescription = "description";
    protected static final String keyDeviceToken = "device_token";
    protected static final String keyDistance = "distance";
    protected static final String keyDuration = "duration";
    protected static final String keyEnable = "enable";
    protected static final String keyEndTime = "end_time";
    protected static final String keyExpired = "expired";
    protected static final String keyExpiresAt = "expires_at";
    protected static final String keyFilterFriends = "filter_friends";
    protected static final String keyFilterLive = "filter_live";
    protected static final String keyFollowCount = "follow_count";
    protected static final String keyFriendOnlineStatus = "friend_online_status";
    protected static final String keyFriendRequest = "friend_request";
    protected static final String keyGroup = "group";
    protected static final String keyGroupBannerUrl = "group_banner_url";
    protected static final String keyGroupCount = "group_count";
    protected static final String keyGroupId = "group_id";
    protected static final String keyGroupInfo = "group_info";
    protected static final String keyGroupList = "group_list";
    protected static final String keyGroupRequestApproval = "group_request_approval";
    protected static final String keyGroupTitle = "group_title";
    protected static final String keyGroupUserAvatar = "group_user_avatar";
    protected static final String keyGroupUserId = "group_user_id";
    protected static final String keyGroupUserName = "group_user_name";
    protected static final String keyGroupWaitingRoom = "group_waiting_room";
    protected static final String keyHasGroupContent = "has_group_content";
    protected static final String keyHasHomeContent = "has_home_content";
    protected static final String keyHeaderAuthorization = "Authorization";
    protected static final String keyId = "id";
    protected static final String keyImageFile = "image_file";
    protected static final String keyImageUrl = "image_url";
    protected static final String keyIncludeJoinGroup = "include_join_group";
    protected static final String keyInterestList = "interest_list";
    protected static final String keyInterests = "interests";
    protected static final String keyInviteCode = "invite_code";
    protected static final String keyInviteId = "invite_id";
    protected static final String keyInviteList = "invite_list";
    protected static final String keyInvites = "invites";
    protected static final String keyIsBroadcasting = "is_broadcasting";
    protected static final String keyIsComposable = "is_composable";
    protected static final String keyIsDeleted = "is_deleted";
    protected static final String keyIsFlag = "is_flag";
    protected static final String keyIsFollow = "is_follow";
    protected static final String keyIsInvite = "is_invite";
    protected static final String keyIsLike = "is_like";
    protected static final String keyIsMember = "is_member";
    protected static final String keyIsPrelaunch = "is_prelaunch";
    protected static final String keyIsRead = "is_read";
    protected static final String keyIsRequest = "is_request";
    protected static final String keyJoinedAt = "joined_at";
    protected static final String keyLikeCount = "like_count";
    protected static final String keyLikeToYourGroupPost = "like_to_your_group_post";
    protected static final String keyLikeToYourVideo = "like_to_your_video";
    protected static final String keyLimit = "limit";
    protected static final String keyLocation = "location";
    protected static final String keyLocationLatitude = "location_latitude";
    protected static final String keyLocationLongitude = "location_longitude";
    protected static final String keyLocationName = "location_name";
    protected static final String keyLookingForMessage = "looking_for_message";
    protected static final String keyMemberCount = "member_count";
    protected static final String keyMemberList = "member_list";
    protected static final String keyMessage = "message";
    protected static final String keyMessageIdList = "message_id_list";
    protected static final String keyMessageList = "message_list";
    protected static final String keyName = "name";
    protected static final String keyNewPostToYourGroup = "new_post_to_your_group";
    protected static final String keyNotification = "notification";
    protected static final String keyObjectList = "object_list";
    protected static final String keyOffset = "offset";
    protected static final String keyParentId = "parent_id";
    protected static final String keyParticipantId = "participant_id";
    protected static final String keyParticipantList = "participant_list";
    protected static final String keyParticipants = "participants";
    protected static final String keyPayload = "payload";
    protected static final String keyPendingVideos = "pending_videos";
    protected static final String keyPermission = "permission";
    protected static final String keyPermissionJoin = "permission_join";
    protected static final String keyPermissionRecord = "permission_record";
    protected static final String keyPlaceholderImageFile = "placeholder_image_file";
    protected static final String keyPlaceholderUrl = "placeholder_url";
    protected static final String keyPost = "post";
    protected static final String keyPostCount = "post_count";
    protected static final String keyPostId = "post_id";
    protected static final String keyPostList = "post_list";
    protected static final String keyReasonDescription = "reason_description";
    protected static final String keyReasonId = "reason_id";
    protected static final String keyReasonList = "reason_list";
    protected static final String keyRecordingId = "recording_id";
    protected static final String keyReplyToYourGroupComment = "reply_to_your_group_comment";
    protected static final String keyReplyToYourGroupPost = "reply_to_your_group_post";
    protected static final String keyRequestId = "request_id";
    protected static final String keyRequestList = "request_list";
    protected static final String keyRequireMemberRequest = "require_member_request";
    protected static final String keyScheduleEndTime = "schedule_end_time";
    protected static final String keyScheduleId = "schedule_id";
    protected static final String keyScheduleRepeatingDays = "schedule_repeating_days";
    protected static final String keyScheduleStartTime = "schedule_start_time";
    protected static final String keyScheduleTimezone = "schedule_timezone";
    protected static final String keyScheduleTimezoneIdentifier = "schedule_timezone_identifier";
    protected static final String keySchedules = "schedules";
    protected static final String keySearchTerm = "search_term";
    protected static final String keyService = "service";
    protected static final String keyServiceId = "service_id";
    protected static final String keySessionId = "session_id";
    protected static final String keySessionInfoList = "session_info_list";
    protected static final String keySize = "size";
    protected static final String keySpeed = "speed";
    protected static final String keySpeedData = "speed_data";
    protected static final String keySpeedUnits = "speed_units";
    protected static final String keyStartTime = "start_time";
    protected static final String keyStatus = "status";
    protected static final String keyStatusList = "status_list";
    protected static final String keySteps = "steps";
    protected static final String keyStudentModeRequest = "student_mode_request";
    protected static final String keyStudentModeResponse = "student_mode_response";
    protected static final String keyTargetId = "target_id";
    protected static final String keyTargetUserId = "target_user_id";
    protected static final String keyThumbnailImageFile = "thumbnail_image_file";
    protected static final String keyThumbnailUrl = "thumbnail_url";
    protected static final String keyTimeId = "time_id";
    protected static final String keyTimeOfDayList = "time_of_day_list";
    protected static final String keyTimeTitle = "time_title";
    protected static final String keyTimes = "times";
    protected static final String keyTitle = "title";
    protected static final String keyToken = "token";
    protected static final String keyTotalVideos = "total_videos";
    protected static final String keyTrainerModeRequest = "trainer_mode_request";
    protected static final String keyTrainerModeResponse = "trainer_mode_response";
    protected static final String keyTrainerModeState = "trainer_mode_state";
    protected static final String keyType = "type";
    protected static final String keyUrl = "url";
    protected static final String keyUseSandbox = "use_sandbox";
    protected static final String keyUserAvatar = "user_avatar";
    protected static final String keyUserId = "user_id";
    protected static final String keyUserName = "user_name";
    protected static final String keyVideo = "video";
    protected static final String keyVideoCount = "video_count";
    protected static final String keyVideoCreatedAt = "video_created_at";
    protected static final String keyVideoFile = "video_file";
    protected static final String keyVideoId = "video_id";
    protected static final String keyVideoInvite = "video_invite";
    protected static final String keyVideoList = "video_list";
    protected static final String keyVideoThumbnailUrl = "video_thumbnail_url";
    protected static final String keyVideoUrl = "video_url";
    protected static final String keyVideos = "videos";
    protected static final String keyWorkoutId = "workout_id";
    protected static final String keyWorkoutSchedule = "workout_schedule";
    protected static final String keyWorkoutType = "workout_type";

    /* loaded from: classes2.dex */
    public interface BuddyCreateProfileListener {
        void onResponse(String str, String str2);
    }

    /* loaded from: classes2.dex */
    public interface BuddyDeleteProfileListener {
        void onResponse(String str);
    }

    /* loaded from: classes2.dex */
    public interface BuddyFileUploadListener {
        void onResponse(String str);
    }

    /* loaded from: classes2.dex */
    public interface BuddyGetInterestsListener {
        void onResponse(String str, List<BuddyProfileInfo.BuddyInterest> list);
    }

    /* loaded from: classes2.dex */
    public interface BuddyGetProfileListener {
        void onResponse(String str, BuddyProfileInfo buddyProfileInfo);
    }

    /* loaded from: classes2.dex */
    public interface BuddyGetTimeOfDayListener {
        void onResponse(String str, List<BuddyProfileInfo.BuddyDayTimeType> list);
    }

    /* loaded from: classes2.dex */
    public interface BuddyListAllListener {
        void onResponse(String str, List<BuddyProfileInfo> list);
    }

    /* loaded from: classes2.dex */
    public interface BuddyUpdateProfileListener {
        void onResponse(String str, String str2);
    }

    /* loaded from: classes2.dex */
    public interface VideoGetBroadcastUrlListener {
        void onResponse(String str, String str2);
    }

    /* loaded from: classes2.dex */
    public interface VideoGetReportReasonsListener {
        void onResponse(String str, List<ReportReasonInfo> list);
    }

    /* loaded from: classes2.dex */
    public interface VideoGetTrainerModeEnabled {
        void onResponse(String str, TrainerModeEnabledState trainerModeEnabledState);
    }

    /* loaded from: classes2.dex */
    public interface VideoLikeListener {
        void onCreateVideoLike(String str);

        void onDeleteVideoLike(String str);

        void onGetVideoLikeInfo(String str, VideoLikeInfo videoLikeInfo);

        void onGetVideoLikeUserInfo(String str, List<VideoLikeUserInfo> list);
    }

    /* loaded from: classes2.dex */
    public interface VideoNotificationsListener {
        void onResponse(String str, UserNotificationSettingInfo userNotificationSettingInfo);
    }

    /* loaded from: classes2.dex */
    public interface VideoResponseListener {
        void onResponse(String str);
    }

    /* loaded from: classes2.dex */
    public interface VideoServiceCredentialsListener {
        void onResponse(String str, String str2);
    }

    /* loaded from: classes2.dex */
    public interface VideoServiceSessionInfoListener {
        void onResponse(String str, VideoServiceSessionInfo videoServiceSessionInfo);
    }

    /* loaded from: classes2.dex */
    public interface VideoServiceSessionListener {
        void onResponse(String str, String str2) throws JSONException;
    }

    /* loaded from: classes2.dex */
    public interface VideoServiceSessionTokenListener {
        void onResponse(String str, String str2);
    }

    /* loaded from: classes2.dex */
    public interface VideoStartSessionArchiveListener {
        void onResponse(String str, String str2);
    }

    /* loaded from: classes2.dex */
    public interface VideoStartSessionListener {
        void onResponse(String str, String str2, String str3);
    }

    /* loaded from: classes2.dex */
    public interface VideoStopSessionListener {
        void onResponse(String str);
    }

    /* loaded from: classes2.dex */
    public interface VideoUploadArchiveListener {
        void onResponse(String str);
    }

    /* loaded from: classes2.dex */
    public interface VideoWorkoutInfoListener {
        void onResponse(String str, Integer num);
    }

    /* loaded from: classes2.dex */
    public interface videoInfoResultsListener {
        void onVideoInfoResponse(String str, VideoServiceVideoInfo[] videoServiceVideoInfoArr);
    }

    /* loaded from: classes2.dex */
    public interface videoParticipantsListener {
        void onParticipantsInfoResponse(String str, ArrayList<UserRunningSessionParticipantInfo> arrayList);
    }

    public static void getCredentials(final VideoServiceCredentialsListener videoServiceCredentialsListener) {
        RequestUtil.shared.postJson(SERVICE_URI_ROUTE_GET_CREDENTIALS, (JSONObject) null, new RequestUtil.RequestUtilListener() { // from class: com.treadly.Treadly.UI.TreadlyVideo.Data.VideoServiceHelper.1
            @Override // com.treadly.Treadly.Data.Utility.RequestUtil.RequestUtilListener
            public void onResponse(TreadlyNetworkResponse treadlyNetworkResponse) throws JSONException {
                if (!treadlyNetworkResponse.ok() || treadlyNetworkResponse.response == null) {
                    VideoServiceCredentialsListener.this.onResponse("Bad response", "");
                    return;
                }
                VideoServiceCredentialsListener.this.onResponse(null, treadlyNetworkResponse.response.optString(VideoServiceHelper.keyApiKey));
            }
        });
    }

    public static void createSession(String str, final VideoServiceSessionListener videoServiceSessionListener) throws JSONException {
        JSONObject jSONObject = new JSONObject();
        jSONObject.put("user_id", str);
        RequestUtil.shared.postJson(SERVICE_URI_ROUTE_CREATE_SESSION, jSONObject, new RequestUtil.RequestUtilListener() { // from class: com.treadly.Treadly.UI.TreadlyVideo.Data.VideoServiceHelper.2
            @Override // com.treadly.Treadly.Data.Utility.RequestUtil.RequestUtilListener
            public void onResponse(TreadlyNetworkResponse treadlyNetworkResponse) throws JSONException {
                if (!treadlyNetworkResponse.ok() || treadlyNetworkResponse.response == null) {
                    VideoServiceSessionListener.this.onResponse("Error Could not make request", null);
                    return;
                }
                VideoServiceSessionListener.this.onResponse(null, treadlyNetworkResponse.response.optString("session_id"));
            }
        });
    }

    public static void getSessionToken(String str, boolean z, boolean z2, final VideoServiceSessionTokenListener videoServiceSessionTokenListener) throws JSONException {
        String str2 = z ? z2 ? SERVICE_URI_ROUTE_GET_SESSION_TOKEN_PUBLISHER_HOST : SERVICE_URI_ROUTE_GET_SESSION_TOKEN_PUBLISHER : SERVICE_URI_ROUTE_GET_SESSION_TOKEN_SUBSCRIBER;
        JSONObject jSONObject = new JSONObject();
        jSONObject.put("session_id", str);
        RequestUtil.shared.postJson(str2, jSONObject, new RequestUtil.RequestUtilListener() { // from class: com.treadly.Treadly.UI.TreadlyVideo.Data.VideoServiceHelper.3
            @Override // com.treadly.Treadly.Data.Utility.RequestUtil.RequestUtilListener
            public void onResponse(TreadlyNetworkResponse treadlyNetworkResponse) throws JSONException {
                if (!treadlyNetworkResponse.ok() || treadlyNetworkResponse.response == null) {
                    VideoServiceHelper.runOnMain(new Runnable() { // from class: com.treadly.Treadly.UI.TreadlyVideo.Data.VideoServiceHelper.3.1
                        @Override // java.lang.Runnable
                        public void run() {
                            VideoServiceSessionTokenListener.this.onResponse("Error Could not make request", null);
                        }
                    });
                    return;
                }
                VideoServiceSessionTokenListener.this.onResponse(null, treadlyNetworkResponse.response.optString(VideoServiceHelper.keyToken));
            }
        });
    }

    public static void getSessionInfoByUser(String str, final VideoServiceSessionInfoListener videoServiceSessionInfoListener) {
        JSONObject jSONObject = new JSONObject();
        try {
            jSONObject.put("user_id", str);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        RequestUtil.shared.postJson(SERVICE_URI_ROUTE_GET_SESSION_INFO_USER, jSONObject, new RequestUtil.RequestUtilListener() { // from class: com.treadly.Treadly.UI.TreadlyVideo.Data.VideoServiceHelper.4
            @Override // com.treadly.Treadly.Data.Utility.RequestUtil.RequestUtilListener
            public void onResponse(TreadlyNetworkResponse treadlyNetworkResponse) throws JSONException {
                if (!treadlyNetworkResponse.ok() || treadlyNetworkResponse.response == null) {
                    VideoServiceHelper.runOnMain(new Runnable() { // from class: com.treadly.Treadly.UI.TreadlyVideo.Data.VideoServiceHelper.4.1
                        @Override // java.lang.Runnable
                        public void run() {
                            VideoServiceSessionInfoListener.this.onResponse("Error: Can not send response", null);
                        }
                    });
                    return;
                }
                JSONObject jSONObject2 = treadlyNetworkResponse.response;
                final VideoServiceSessionInfo videoServiceSessionInfo = new VideoServiceSessionInfo(jSONObject2.optString("user_id"), jSONObject2.optString("session_id"), jSONObject2.optString(VideoServiceHelper.keyBroadcastId), jSONObject2.optString(VideoServiceHelper.keyBroadcastUrl));
                VideoServiceHelper.runOnMain(new Runnable() { // from class: com.treadly.Treadly.UI.TreadlyVideo.Data.VideoServiceHelper.4.2
                    @Override // java.lang.Runnable
                    public void run() {
                        VideoServiceSessionInfoListener.this.onResponse(null, videoServiceSessionInfo);
                    }
                });
            }
        });
    }

    /* renamed from: com.treadly.Treadly.UI.TreadlyVideo.Data.VideoServiceHelper$5  reason: invalid class name */
    /* loaded from: classes2.dex */
    static class AnonymousClass5 implements VideoGetBroadcastUrlListener {
        final /* synthetic */ String val$broadcastId;
        final /* synthetic */ StreamPermission val$broadcastPermission;
        final /* synthetic */ VideoStartSessionListener val$listener;
        final /* synthetic */ String val$scheduleId;
        final /* synthetic */ String val$userId;
        final /* synthetic */ Integer val$workoutId;

        AnonymousClass5(String str, String str2, Integer num, String str3, VideoStartSessionListener videoStartSessionListener, StreamPermission streamPermission) {
            this.val$userId = str;
            this.val$broadcastId = str2;
            this.val$workoutId = num;
            this.val$scheduleId = str3;
            this.val$listener = videoStartSessionListener;
            this.val$broadcastPermission = streamPermission;
        }

        @Override // com.treadly.Treadly.UI.TreadlyVideo.Data.VideoServiceHelper.VideoGetBroadcastUrlListener
        public void onResponse(String str, String str2) {
            if (str2 != null) {
                try {
                    JSONObject jSONObject = new JSONObject();
                    jSONObject.put("user_id", this.val$userId);
                    jSONObject.put(VideoServiceHelper.keyBroadcastId, this.val$broadcastId);
                    jSONObject.put(VideoServiceHelper.keyBroadcastUrl, str2);
                    if (this.val$workoutId != null) {
                        jSONObject.put(VideoServiceHelper.keyWorkoutId, this.val$workoutId);
                    }
                    if (this.val$scheduleId != null) {
                        jSONObject.put(VideoServiceHelper.keyScheduleId, this.val$scheduleId);
                    }
                    RequestUtil.shared.postJson(VideoServiceHelper.SERVICE_BAMBUSER_URI_ROUTE_START_SESSION_BROADCAST, jSONObject, new RequestUtil.RequestUtilListener() { // from class: com.treadly.Treadly.UI.TreadlyVideo.Data.VideoServiceHelper.5.1
                        @Override // com.treadly.Treadly.Data.Utility.RequestUtil.RequestUtilListener
                        public void onResponse(TreadlyNetworkResponse treadlyNetworkResponse) throws JSONException {
                            if (!treadlyNetworkResponse.ok() || treadlyNetworkResponse.response == null) {
                                VideoServiceHelper.runOnMain(new Runnable() { // from class: com.treadly.Treadly.UI.TreadlyVideo.Data.VideoServiceHelper.5.1.1
                                    @Override // java.lang.Runnable
                                    public void run() {
                                        AnonymousClass5.this.val$listener.onResponse("Error: Could not make request", null, null);
                                    }
                                });
                                return;
                            }
                            JSONObject jSONObject2 = treadlyNetworkResponse.response;
                            final String optString = jSONObject2.optString(VideoServiceHelper.keyBroadcastId);
                            final String optString2 = jSONObject2.optString(VideoServiceHelper.keyBroadcastUrl);
                            if (optString != null && optString2 != null) {
                                VideoServiceHelper.runOnMain(new Runnable() { // from class: com.treadly.Treadly.UI.TreadlyVideo.Data.VideoServiceHelper.5.1.2
                                    @Override // java.lang.Runnable
                                    public void run() {
                                        if (AnonymousClass5.this.val$broadcastPermission != null && (AnonymousClass5.this.val$broadcastPermission == StreamPermission.publicStream || AnonymousClass5.this.val$broadcastPermission == StreamPermission.friendsStream)) {
                                            TreadlyEventManager.getInstance().sendUserBroadcastStarted();
                                        }
                                        AnonymousClass5.this.val$listener.onResponse(null, optString, optString2);
                                    }
                                });
                            } else {
                                VideoServiceHelper.runOnMain(new Runnable() { // from class: com.treadly.Treadly.UI.TreadlyVideo.Data.VideoServiceHelper.5.1.3
                                    @Override // java.lang.Runnable
                                    public void run() {
                                        AnonymousClass5.this.val$listener.onResponse("Error: Could not parse results", null, null);
                                    }
                                });
                            }
                        }
                    });
                    return;
                } catch (JSONException e) {
                    e.printStackTrace();
                    return;
                }
            }
            VideoServiceHelper.runOnMain(new Runnable() { // from class: com.treadly.Treadly.UI.TreadlyVideo.Data.VideoServiceHelper.5.2
                @Override // java.lang.Runnable
                public void run() {
                    AnonymousClass5.this.val$listener.onResponse("Error: Could not parse results", null, null);
                }
            });
        }
    }

    public static void startBambuserSessionBroadcast(String str, String str2, StreamPermission streamPermission, Integer num, String str3, VideoStartSessionListener videoStartSessionListener) throws JSONException {
        getBambuserSessionBroadcastUrl(str2, new AnonymousClass5(str, str2, num, str3, videoStartSessionListener, streamPermission));
    }

    public static void getBambuserSessionBroadcastUrl(String str, final VideoGetBroadcastUrlListener videoGetBroadcastUrlListener) throws JSONException {
        JSONObject jSONObject = new JSONObject();
        jSONObject.put("Accept", "application/vnd.bambuser.v1+json");
        jSONObject.put(keyHeaderAuthorization, "Bearer 33XtkTMwEF879f4DqeymaJ");
        RequestUtil.shared.getJson("https://api.bambuser.com/broadcasts/" + str, jSONObject, new RequestUtil.RequestUtilListener() { // from class: com.treadly.Treadly.UI.TreadlyVideo.Data.VideoServiceHelper.6
            @Override // com.treadly.Treadly.Data.Utility.RequestUtil.RequestUtilListener
            public void onResponse(TreadlyNetworkResponse treadlyNetworkResponse) throws JSONException {
                if (!treadlyNetworkResponse.ok() || treadlyNetworkResponse.response == null) {
                    VideoServiceHelper.runOnMain(new Runnable() { // from class: com.treadly.Treadly.UI.TreadlyVideo.Data.VideoServiceHelper.6.1
                        @Override // java.lang.Runnable
                        public void run() {
                            VideoGetBroadcastUrlListener.this.onResponse("Error: Could not make request", null);
                        }
                    });
                    return;
                }
                final String optString = treadlyNetworkResponse.response.optString("resourceUri");
                if (optString != null) {
                    VideoServiceHelper.runOnMain(new Runnable() { // from class: com.treadly.Treadly.UI.TreadlyVideo.Data.VideoServiceHelper.6.2
                        @Override // java.lang.Runnable
                        public void run() {
                            VideoGetBroadcastUrlListener.this.onResponse(null, optString);
                        }
                    });
                } else {
                    VideoServiceHelper.runOnMain(new Runnable() { // from class: com.treadly.Treadly.UI.TreadlyVideo.Data.VideoServiceHelper.6.3
                        @Override // java.lang.Runnable
                        public void run() {
                            VideoGetBroadcastUrlListener.this.onResponse("Error: Could not parse results", null);
                        }
                    });
                }
            }
        });
    }

    public static void stopBambuserSessionBroadcast(final String str, final StreamPermission streamPermission, String str2, final VideoStopSessionListener videoStopSessionListener) throws JSONException {
        JSONObject jSONObject = new JSONObject();
        jSONObject.put(keyBroadcastId, str);
        if (str2 != null) {
            jSONObject.put(keyScheduleId, str2);
        }
        requestBasicUsersPost(SERVICE_BAMBUSER_URI_ROUTE_STOP_SESSION_BROADCAST, jSONObject, new VideoResponseListener() { // from class: com.treadly.Treadly.UI.TreadlyVideo.Data.VideoServiceHelper.7
            @Override // com.treadly.Treadly.UI.TreadlyVideo.Data.VideoServiceHelper.VideoResponseListener
            public void onResponse(String str3) {
                if (str3 == null) {
                    TreadlyEventManager.getInstance().sendVideoBroadcastEnded(str);
                    if (streamPermission != null && (streamPermission == StreamPermission.publicStream || streamPermission == StreamPermission.friendsStream)) {
                        TreadlyEventManager.getInstance().sendUserBroadcastEnded();
                    }
                }
                videoStopSessionListener.onResponse(str3);
            }
        });
    }

    public static void uploadBambuserArchive(String str, String str2, Date date, Uri uri, Integer num, Integer num2, final VideoUploadArchiveListener videoUploadArchiveListener) throws JSONException {
        UserTokenInfo userTokenInfo = TreadlyServiceManager.getInstance().tokenInfo;
        if (userTokenInfo == null) {
            videoUploadArchiveListener.onResponse("Error: No user currently logged in");
        }
        JSONObject jSONObject = new JSONObject();
        jSONObject.put(keyHeaderAuthorization, userTokenInfo.token);
        JSONObject jSONObject2 = new JSONObject();
        jSONObject2.put(keyServiceId, str);
        jSONObject2.put(keyWorkoutId, str2);
        jSONObject2.put(keyCreatedAt, convertDateToServiceTimestamp(date));
        if (num != null) {
            jSONObject2.put("duration", num);
        }
        if (num2 != null) {
            jSONObject2.put(keySize, num2);
        }
        RequestUtil.shared.postJsonFileUpload(SERVICE_BAMBUSER_ARCHIVE_UPLOAD_ROUTE, convertJsonToRequestData(jSONObject2), jSONObject, new File(uri.toString()), new RequestUtil.RequestUtilListener() { // from class: com.treadly.Treadly.UI.TreadlyVideo.Data.VideoServiceHelper.8
            @Override // com.treadly.Treadly.Data.Utility.RequestUtil.RequestUtilListener
            public void onResponse(TreadlyNetworkResponse treadlyNetworkResponse) throws JSONException {
                if (treadlyNetworkResponse == null || !treadlyNetworkResponse.ok() || treadlyNetworkResponse.response == null) {
                    VideoServiceHelper.runOnMain(new Runnable() { // from class: com.treadly.Treadly.UI.TreadlyVideo.Data.VideoServiceHelper.8.1
                        @Override // java.lang.Runnable
                        public void run() {
                            VideoUploadArchiveListener.this.onResponse("Error: Could not make request");
                        }
                    });
                    return;
                }
                String optString = treadlyNetworkResponse.response.optString("status");
                if (optString != null && optString.equals("ok")) {
                    VideoServiceHelper.runOnMain(new Runnable() { // from class: com.treadly.Treadly.UI.TreadlyVideo.Data.VideoServiceHelper.8.2
                        @Override // java.lang.Runnable
                        public void run() {
                            VideoUploadArchiveListener.this.onResponse(null);
                        }
                    });
                } else {
                    VideoServiceHelper.runOnMain(new Runnable() { // from class: com.treadly.Treadly.UI.TreadlyVideo.Data.VideoServiceHelper.8.3
                        @Override // java.lang.Runnable
                        public void run() {
                            VideoUploadArchiveListener.this.onResponse("Error: Could not parse results");
                        }
                    });
                }
            }
        });
    }

    public static void uploadTokboxArchive(String str, Date date, Uri uri, Integer num, Integer num2, final VideoUploadArchiveListener videoUploadArchiveListener) throws JSONException {
        UserTokenInfo userTokenInfo = TreadlyServiceManager.getInstance().tokenInfo;
        if (userTokenInfo == null) {
            videoUploadArchiveListener.onResponse("Error: No user currently logged in");
            return;
        }
        JSONObject jSONObject = new JSONObject();
        jSONObject.put(keyHeaderAuthorization, userTokenInfo.token);
        JSONObject jSONObject2 = new JSONObject();
        jSONObject2.put(keyServiceId, str);
        jSONObject2.put(keyCreatedAt, convertDateToServiceTimestamp(date));
        if (num != null) {
            jSONObject2.put(keyDailyGoal50Achieved, num);
        }
        if (num2 != null) {
            jSONObject2.put(keySize, num2);
        }
        RequestUtil.shared.postJsonFileUpload(SERVICE_TOKBOX_ARCHIVE_UPLOAD_ROUTE, convertJsonToRequestData(jSONObject2), jSONObject, new File(uri.toString()), new RequestUtil.RequestUtilListener() { // from class: com.treadly.Treadly.UI.TreadlyVideo.Data.VideoServiceHelper.9
            @Override // com.treadly.Treadly.Data.Utility.RequestUtil.RequestUtilListener
            public void onResponse(TreadlyNetworkResponse treadlyNetworkResponse) throws JSONException {
                if (!treadlyNetworkResponse.ok() || treadlyNetworkResponse.response == null) {
                    VideoServiceHelper.runOnMain(new Runnable() { // from class: com.treadly.Treadly.UI.TreadlyVideo.Data.VideoServiceHelper.9.1
                        @Override // java.lang.Runnable
                        public void run() {
                            VideoUploadArchiveListener.this.onResponse("Error: Could not make request");
                        }
                    });
                } else if (treadlyNetworkResponse.response.optString("status").equals("ok")) {
                    VideoServiceHelper.runOnMain(new Runnable() { // from class: com.treadly.Treadly.UI.TreadlyVideo.Data.VideoServiceHelper.9.2
                        @Override // java.lang.Runnable
                        public void run() {
                            VideoUploadArchiveListener.this.onResponse(null);
                        }
                    });
                } else {
                    VideoServiceHelper.runOnMain(new Runnable() { // from class: com.treadly.Treadly.UI.TreadlyVideo.Data.VideoServiceHelper.9.3
                        @Override // java.lang.Runnable
                        public void run() {
                            VideoUploadArchiveListener.this.onResponse("Error: Could not parse results");
                        }
                    });
                }
            }
        });
    }

    public static void createWorkoutInfo(WorkoutInfo workoutInfo, String str, final VideoWorkoutInfoListener videoWorkoutInfoListener) {
        try {
            JSONObject jSONObject = new JSONObject();
            jSONObject.put("user_id", workoutInfo.userId);
            jSONObject.put("description", workoutInfo.description);
            jSONObject.put(keyPermissionJoin, workoutInfo.permissionJoin.getValue());
            jSONObject.put(keyPermissionRecord, workoutInfo.permissionRecord.getValue());
            jSONObject.put(keyAllowComments, workoutInfo.allowComments ? 1 : 0);
            jSONObject.put(keyAllowVoiceMessage, workoutInfo.allowVoiceMessage ? 1 : 0);
            jSONObject.put("group_id", str);
            RequestUtil.shared.postJson(SERVICE_VIDEO_CREATE_WORKOUT_ROUTE, jSONObject, new RequestUtil.RequestUtilListener() { // from class: com.treadly.Treadly.UI.TreadlyVideo.Data.VideoServiceHelper.10
                @Override // com.treadly.Treadly.Data.Utility.RequestUtil.RequestUtilListener
                public void onResponse(TreadlyNetworkResponse treadlyNetworkResponse) throws JSONException {
                    final Integer valueOf;
                    if (!treadlyNetworkResponse.ok() || treadlyNetworkResponse.response == null) {
                        VideoServiceHelper.runOnMain(new Runnable() { // from class: com.treadly.Treadly.UI.TreadlyVideo.Data.VideoServiceHelper.10.1
                            @Override // java.lang.Runnable
                            public void run() {
                                VideoWorkoutInfoListener.this.onResponse(VideoServiceHelper.errorCouldNotConnectWithServer, null);
                            }
                        });
                    }
                    JSONObject jSONObject2 = treadlyNetworkResponse.response;
                    String optString = jSONObject2.optString("status");
                    if (optString != null && optString.equals("ok") && (valueOf = Integer.valueOf(jSONObject2.optInt(VideoServiceHelper.keyWorkoutId))) != null) {
                        VideoServiceHelper.runOnMain(new Runnable() { // from class: com.treadly.Treadly.UI.TreadlyVideo.Data.VideoServiceHelper.10.2
                            @Override // java.lang.Runnable
                            public void run() {
                                VideoWorkoutInfoListener.this.onResponse(null, valueOf);
                            }
                        });
                    } else {
                        VideoServiceHelper.runOnMain(new Runnable() { // from class: com.treadly.Treadly.UI.TreadlyVideo.Data.VideoServiceHelper.10.3
                            @Override // java.lang.Runnable
                            public void run() {
                                VideoWorkoutInfoListener.this.onResponse("Error creating workout", null);
                            }
                        });
                    }
                }
            });
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public static void startSessionBroadcast(String str, final StreamPermission streamPermission, Integer num, String str2, final VideoStartSessionListener videoStartSessionListener) throws JSONException {
        JSONObject jSONObject = new JSONObject();
        jSONObject.put("session_id", str);
        if (num != null) {
            jSONObject.put(keyWorkoutId, num);
        }
        if (str2 != null) {
            jSONObject.put(keyScheduleId, str2);
        }
        RequestUtil.shared.postJson(SERVICE_URI_ROUTE_START_SESSION_BROADCAST, jSONObject, new RequestUtil.RequestUtilListener() { // from class: com.treadly.Treadly.UI.TreadlyVideo.Data.VideoServiceHelper.11
            @Override // com.treadly.Treadly.Data.Utility.RequestUtil.RequestUtilListener
            public void onResponse(TreadlyNetworkResponse treadlyNetworkResponse) throws JSONException {
                if (!treadlyNetworkResponse.ok() || treadlyNetworkResponse.response == null) {
                    VideoServiceHelper.runOnMain(new Runnable() { // from class: com.treadly.Treadly.UI.TreadlyVideo.Data.VideoServiceHelper.11.1
                        @Override // java.lang.Runnable
                        public void run() {
                            VideoStartSessionListener.this.onResponse("Error: Could not make request", null, null);
                        }
                    });
                    return;
                }
                JSONObject jSONObject2 = treadlyNetworkResponse.response;
                final String optString = jSONObject2.optString(VideoServiceHelper.keyBroadcastId);
                final String optString2 = jSONObject2.optString(VideoServiceHelper.keyBroadcastUrl);
                if (optString != null && optString2 != null) {
                    VideoServiceHelper.runOnMain(new Runnable() { // from class: com.treadly.Treadly.UI.TreadlyVideo.Data.VideoServiceHelper.11.2
                        @Override // java.lang.Runnable
                        public void run() {
                            if (streamPermission != null && streamPermission == StreamPermission.publicStream) {
                                TreadlyEventManager.getInstance().sendUserBroadcastStarted();
                            }
                            VideoStartSessionListener.this.onResponse(null, optString, optString2);
                        }
                    });
                } else {
                    VideoServiceHelper.runOnMain(new Runnable() { // from class: com.treadly.Treadly.UI.TreadlyVideo.Data.VideoServiceHelper.11.3
                        @Override // java.lang.Runnable
                        public void run() {
                            VideoStartSessionListener.this.onResponse("Error: Could not parse results", null, null);
                        }
                    });
                }
            }
        });
    }

    public static void stopSessionBroadcast(final String str, final StreamPermission streamPermission, String str2, final VideoStopSessionListener videoStopSessionListener) throws JSONException {
        JSONObject jSONObject = new JSONObject();
        jSONObject.put(keyBroadcastId, str);
        if (str2 != null) {
            jSONObject.put(keyScheduleId, str2);
        }
        requestBasicUsersPost(SERVICE_URI_ROUTE_STOP_SESSION_BROADCAST, jSONObject, new VideoResponseListener() { // from class: com.treadly.Treadly.UI.TreadlyVideo.Data.VideoServiceHelper.12
            @Override // com.treadly.Treadly.UI.TreadlyVideo.Data.VideoServiceHelper.VideoResponseListener
            public void onResponse(String str3) {
                if (str3 == null) {
                    TreadlyEventManager.getInstance().sendVideoBroadcastEnded(str);
                    if (streamPermission != null && streamPermission == StreamPermission.publicStream) {
                        TreadlyEventManager.getInstance().sendUserBroadcastEnded();
                    }
                }
                videoStopSessionListener.onResponse(str3);
            }
        });
    }

    public static void createVideoArchiveLike(String str, String str2, VideoResponseListener videoResponseListener) {
        createVideoLike("archive", str, str2, videoResponseListener);
    }

    public static void deleteVideoArchiveLike(String str, String str2, VideoResponseListener videoResponseListener) {
        deleteVideoLike("archive", str, str2, videoResponseListener);
    }

    public static void getVideoArchiveLikeInfo(String str, String str2, VideoLikeListener videoLikeListener) {
        getVideoLikeInfo(str, str2, videoLikeListener);
    }

    public static void getVideoArchiveLikeUserInfo(String str, String str2, VideoLikeListener videoLikeListener) {
        getVideoLikeUserInfo(str, str2, videoLikeListener);
    }

    public static void createVideoBroadcastLike(String str, VideoResponseListener videoResponseListener) {
        createVideoLike("public", null, str, videoResponseListener);
    }

    public static void deleteVideoBroadcastLike(String str, VideoResponseListener videoResponseListener) {
        deleteVideoLike("public", null, str, videoResponseListener);
    }

    public static void getVideoBroadcastLikeInfo(String str, VideoLikeListener videoLikeListener) {
        getVideoLikeInfo(null, str, videoLikeListener);
    }

    public static void getVideoBroadcastLikeUserInfo(String str, VideoLikeListener videoLikeListener) {
        getVideoLikeUserInfo(null, str, videoLikeListener);
    }

    public static void getBuddyProfileInfo(String str, final BuddyGetProfileListener buddyGetProfileListener) {
        UserTokenInfo userTokenInfo = TreadlyServiceManager.getInstance().tokenInfo;
        if (userTokenInfo == null) {
            buddyGetProfileListener.onResponse("Error: No user currently logged in", null);
            return;
        }
        JSONObject jSONObject = new JSONObject();
        JSONObject jSONObject2 = new JSONObject();
        try {
            jSONObject.put(keyHeaderAuthorization, userTokenInfo.token);
            jSONObject2.put("target_user_id", str);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        RequestUtil.shared.postJson(SERVICE_VIDEO_BUDDY_PROFILE_GET_ROUTE, jSONObject2, jSONObject, new RequestUtil.RequestUtilListener() { // from class: com.treadly.Treadly.UI.TreadlyVideo.Data.-$$Lambda$VideoServiceHelper$neZDPEpada0LybckyJrCFbQ2rj4
            @Override // com.treadly.Treadly.Data.Utility.RequestUtil.RequestUtilListener
            public final void onResponse(TreadlyNetworkResponse treadlyNetworkResponse) {
                VideoServiceHelper.lambda$getBuddyProfileInfo$5(VideoServiceHelper.BuddyGetProfileListener.this, treadlyNetworkResponse);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static /* synthetic */ void lambda$getBuddyProfileInfo$5(final BuddyGetProfileListener buddyGetProfileListener, TreadlyNetworkResponse treadlyNetworkResponse) throws JSONException {
        if (!treadlyNetworkResponse.ok() || treadlyNetworkResponse.response == null) {
            runOnMain(new Runnable() { // from class: com.treadly.Treadly.UI.TreadlyVideo.Data.-$$Lambda$VideoServiceHelper$zMTJgZSvb7CyUHXe0wVdZavO5MI
                @Override // java.lang.Runnable
                public final void run() {
                    VideoServiceHelper.BuddyGetProfileListener.this.onResponse(VideoServiceHelper.errorCouldNotConnectWithServer, null);
                }
            });
            return;
        }
        try {
            JSONObject jSONObject = treadlyNetworkResponse.response;
            if (jSONObject.has("status") && jSONObject.optString("status").equals("error")) {
                runOnMain(new Runnable() { // from class: com.treadly.Treadly.UI.TreadlyVideo.Data.-$$Lambda$VideoServiceHelper$MASisviUJuVxnpwVfsbrGtM0ItQ
                    @Override // java.lang.Runnable
                    public final void run() {
                        VideoServiceHelper.BuddyGetProfileListener.this.onResponse(VideoServiceHelper.errorNoBuddyProfileFound, null);
                    }
                });
                return;
            }
            final BuddyProfileInfo processBuddyProfileInfo = processBuddyProfileInfo(jSONObject);
            if (processBuddyProfileInfo != null) {
                runOnMain(new Runnable() { // from class: com.treadly.Treadly.UI.TreadlyVideo.Data.-$$Lambda$VideoServiceHelper$WOlnj9rWNBdjdeHYhlqlhmEhIFg
                    @Override // java.lang.Runnable
                    public final void run() {
                        VideoServiceHelper.BuddyGetProfileListener.this.onResponse(null, processBuddyProfileInfo);
                    }
                });
            } else {
                runOnMain(new Runnable() { // from class: com.treadly.Treadly.UI.TreadlyVideo.Data.-$$Lambda$VideoServiceHelper$HsuVWAxiNlWW5aHeNCgJpYEW-38
                    @Override // java.lang.Runnable
                    public final void run() {
                        VideoServiceHelper.BuddyGetProfileListener.this.onResponse(VideoServiceHelper.errorCouldNotConnectWithServer, null);
                    }
                });
            }
        } catch (JSONException unused) {
            runOnMain(new Runnable() { // from class: com.treadly.Treadly.UI.TreadlyVideo.Data.-$$Lambda$VideoServiceHelper$tUJbwbOPdK1AAkHOaWLllYNLH3w
                @Override // java.lang.Runnable
                public final void run() {
                    VideoServiceHelper.BuddyGetProfileListener.this.onResponse(VideoServiceHelper.errorCouldNotConnectWithServer, null);
                }
            });
        }
    }

    public static void getBuddyProfileInfoAll(final BuddyListAllListener buddyListAllListener) {
        UserTokenInfo userTokenInfo = TreadlyServiceManager.getInstance().tokenInfo;
        if (userTokenInfo == null) {
            buddyListAllListener.onResponse("Error: No user currently logged in", null);
            return;
        }
        JSONObject jSONObject = new JSONObject();
        JSONObject jSONObject2 = new JSONObject();
        try {
            jSONObject.put(keyHeaderAuthorization, userTokenInfo.token);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        RequestUtil.shared.postJson(SERVICE_VIDEO_BUDDY_PROFILE_GET_ALL_ROUTE, jSONObject2, jSONObject, new RequestUtil.RequestUtilListener() { // from class: com.treadly.Treadly.UI.TreadlyVideo.Data.-$$Lambda$VideoServiceHelper$yFcOau-n9FN6YpEucJDpiWYOTWo
            @Override // com.treadly.Treadly.Data.Utility.RequestUtil.RequestUtilListener
            public final void onResponse(TreadlyNetworkResponse treadlyNetworkResponse) {
                VideoServiceHelper.lambda$getBuddyProfileInfoAll$9(VideoServiceHelper.BuddyListAllListener.this, treadlyNetworkResponse);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static /* synthetic */ void lambda$getBuddyProfileInfoAll$9(final BuddyListAllListener buddyListAllListener, TreadlyNetworkResponse treadlyNetworkResponse) throws JSONException {
        if (!treadlyNetworkResponse.ok() || treadlyNetworkResponse.response == null) {
            runOnMain(new Runnable() { // from class: com.treadly.Treadly.UI.TreadlyVideo.Data.-$$Lambda$VideoServiceHelper$mVnYF3hVEf93LFRLrKLoHECpu8s
                @Override // java.lang.Runnable
                public final void run() {
                    VideoServiceHelper.BuddyListAllListener.this.onResponse(VideoServiceHelper.errorCouldNotConnectWithServer, null);
                }
            });
            return;
        }
        try {
            JSONArray jSONArray = treadlyNetworkResponse.response.getJSONArray(keyBuddyProfiles);
            final ArrayList arrayList = new ArrayList();
            if (jSONArray != null) {
                for (int i = 0; i < jSONArray.length(); i++) {
                    BuddyProfileInfo processBuddyProfileInfo = processBuddyProfileInfo(jSONArray.optJSONObject(i));
                    if (processBuddyProfileInfo != null) {
                        arrayList.add(processBuddyProfileInfo);
                    }
                }
            }
            runOnMain(new Runnable() { // from class: com.treadly.Treadly.UI.TreadlyVideo.Data.-$$Lambda$VideoServiceHelper$V6hiypWVBuITDWNTAW7zV0KwGD8
                @Override // java.lang.Runnable
                public final void run() {
                    VideoServiceHelper.BuddyListAllListener.this.onResponse(null, arrayList);
                }
            });
        } catch (JSONException e) {
            e.printStackTrace();
            runOnMain(new Runnable() { // from class: com.treadly.Treadly.UI.TreadlyVideo.Data.-$$Lambda$VideoServiceHelper$NDM3wKS_i1kj3SUFTsOIOMT4YvI
                @Override // java.lang.Runnable
                public final void run() {
                    VideoServiceHelper.BuddyListAllListener.this.onResponse(VideoServiceHelper.errorCouldNotConnectWithServer, null);
                }
            });
        }
    }

    private static BuddyProfileInfo processBuddyProfileInfo(JSONObject jSONObject) throws JSONException {
        JSONArray jSONArray;
        String str;
        String str2;
        String str3;
        ArrayList arrayList;
        String str4;
        String str5;
        if (jSONObject != null && jSONObject.has("id") && jSONObject.has("user_id") && jSONObject.has(keyUserName) && jSONObject.has("steps") && jSONObject.has(keyLookingForMessage) && jSONObject.has("location") && jSONObject.has(keyGroupCount) && jSONObject.has(keyUserAvatar) && jSONObject.has(keyInterests) && jSONObject.has(keyDaysOfWeek) && jSONObject.has(keyTimes)) {
            String optString = jSONObject.optString("id");
            String optString2 = jSONObject.optString("user_id");
            String optString3 = jSONObject.optString(keyUserName);
            int optInt = jSONObject.optInt("steps");
            String optString4 = jSONObject.optString(keyUserAvatar);
            if (optString4.length() == 0) {
                optString4 = "https://dgwxv5s2i5zkb.cloudfront.net/avatar/default.png";
            }
            String str6 = optString4;
            String optString5 = jSONObject.optString(keyLookingForMessage);
            String optString6 = jSONObject.optString("location");
            int i = jSONObject.has(keyGroupCount) ? jSONObject.getInt(keyGroupCount) : 0;
            String optString7 = jSONObject.has(keyAudioUrl) ? jSONObject.optString(keyAudioUrl) : "";
            String optString8 = jSONObject.has(keyVideoThumbnailUrl) ? jSONObject.optString(keyVideoThumbnailUrl) : "";
            String optString9 = jSONObject.has(keyVideoUrl) ? jSONObject.optString(keyVideoUrl) : "";
            ArrayList arrayList2 = new ArrayList();
            jSONObject.has(keyParticipants);
            JSONArray optJSONArray = jSONObject.optJSONArray(keyInterests);
            ArrayList arrayList3 = new ArrayList();
            if (optJSONArray != null) {
                int i2 = 0;
                while (i2 < optJSONArray.length()) {
                    JSONObject optJSONObject = optJSONArray.optJSONObject(i2);
                    JSONArray jSONArray2 = optJSONArray;
                    if (optJSONObject.has("id") && optJSONObject.has("title")) {
                        str5 = optString8;
                        str4 = optString9;
                        arrayList3.add(new BuddyProfileInfo.BuddyInterest(optJSONObject.optString("title"), optJSONObject.optString("id")));
                    } else {
                        str4 = optString9;
                        str5 = optString8;
                    }
                    i2++;
                    optJSONArray = jSONArray2;
                    optString8 = str5;
                    optString9 = str4;
                }
            }
            String str7 = optString9;
            String str8 = optString8;
            JSONArray optJSONArray2 = jSONObject.optJSONArray(keyDaysOfWeek);
            ArrayList arrayList4 = new ArrayList();
            if (optJSONArray2 != null) {
                int i3 = 0;
                while (i3 < optJSONArray2.length()) {
                    JSONObject optJSONObject2 = optJSONArray2.optJSONObject(i3);
                    if (optJSONObject2.has("id") && optJSONObject2.has(keyDayOfWeek) && optJSONObject2.has(keyTimeId) && optJSONObject2.has(keyTimeTitle)) {
                        jSONArray = optJSONArray2;
                        arrayList = arrayList3;
                        str3 = optString7;
                        str2 = optString6;
                        str = optString5;
                        arrayList4.add(new BuddyProfileInfo.BuddyDayTime(optJSONObject2.optString("id"), optJSONObject2.optInt(keyDayOfWeek), new BuddyProfileInfo.BuddyDayTimeType(optJSONObject2.optString(keyTimeTitle), optJSONObject2.optString(keyTimeId))));
                    } else {
                        jSONArray = optJSONArray2;
                        str = optString5;
                        str2 = optString6;
                        str3 = optString7;
                        arrayList = arrayList3;
                    }
                    i3++;
                    optJSONArray2 = jSONArray;
                    arrayList3 = arrayList;
                    optString7 = str3;
                    optString6 = str2;
                    optString5 = str;
                }
            }
            String str9 = optString5;
            String str10 = optString6;
            String str11 = optString7;
            ArrayList arrayList5 = arrayList3;
            JSONArray optJSONArray3 = jSONObject.optJSONArray(keyTimes);
            ArrayList arrayList6 = new ArrayList();
            if (optJSONArray3 != null) {
                for (int i4 = 0; i4 < optJSONArray3.length(); i4++) {
                    JSONObject optJSONObject3 = optJSONArray3.optJSONObject(i4);
                    if (optJSONObject3.has("id") && optJSONObject3.has("title")) {
                        arrayList6.add(new BuddyProfileInfo.BuddyDayTimeType(optJSONObject3.optString("title"), optJSONObject3.optString("id")));
                    }
                }
            }
            return new BuddyProfileInfo(optString, optString2, optString3, str6, optInt, i, str9, str9, str10, str11, str7, str8, arrayList5, arrayList4, arrayList6, arrayList2);
        }
        return null;
    }

    public static void createBuddyProfile(BuddyProfileInfo buddyProfileInfo, final BuddyCreateProfileListener buddyCreateProfileListener) {
        UserTokenInfo userTokenInfo = TreadlyServiceManager.getInstance().tokenInfo;
        if (userTokenInfo == null) {
            buddyCreateProfileListener.onResponse("Error: No user currently logged in", null);
            return;
        }
        JSONObject jSONObject = new JSONObject();
        JSONObject jSONObject2 = new JSONObject();
        try {
            jSONObject.put(keyHeaderAuthorization, userTokenInfo.token);
            jSONObject2.put(keyLookingForMessage, buddyProfileInfo.lookingForMessage);
            JSONArray jSONArray = new JSONArray();
            for (BuddyProfileInfo.BuddyInterest buddyInterest : buddyProfileInfo.interests) {
                JSONObject jSONObject3 = new JSONObject();
                jSONObject3.put("id", buddyInterest.id);
                jSONObject3.put("title", buddyInterest.title);
                jSONArray.put(jSONObject3);
            }
            jSONObject2.put(keyInterests, jSONArray);
            if (buddyProfileInfo.videoPath != null) {
                jSONObject2.put(keyVideoUrl, buddyProfileInfo.videoPath);
            }
            if (buddyProfileInfo.videoThumbnailPath != null) {
                jSONObject2.put(keyVideoThumbnailUrl, buddyProfileInfo.videoThumbnailPath);
            }
            if (buddyProfileInfo.audioPath != null) {
                jSONObject2.put(keyAudioUrl, buddyProfileInfo.audioPath);
            }
            JSONArray jSONArray2 = new JSONArray();
            for (BuddyProfileInfo.BuddyDayTime buddyDayTime : buddyProfileInfo.daysOfWeek) {
                JSONObject jSONObject4 = new JSONObject();
                jSONObject4.put(keyTimeId, buddyDayTime.dayTime.id);
                jSONObject4.put(keyTimeTitle, buddyDayTime.dayTime.title);
                jSONObject4.put(keyDayOfWeek, buddyDayTime.dayOfWeek);
                jSONArray2.put(jSONObject4);
            }
            jSONObject2.put(keyDaysOfWeek, jSONArray2);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        RequestUtil.shared.postJson(SERVICE_VIDEO_BUDDY_PROFILE_CREATE_ROUTE, jSONObject2, jSONObject, new RequestUtil.RequestUtilListener() { // from class: com.treadly.Treadly.UI.TreadlyVideo.Data.-$$Lambda$VideoServiceHelper$5hM5W3pjDcs_LHd4Li9IQa-E_GI
            @Override // com.treadly.Treadly.Data.Utility.RequestUtil.RequestUtilListener
            public final void onResponse(TreadlyNetworkResponse treadlyNetworkResponse) {
                VideoServiceHelper.lambda$createBuddyProfile$14(VideoServiceHelper.BuddyCreateProfileListener.this, treadlyNetworkResponse);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static /* synthetic */ void lambda$createBuddyProfile$14(final BuddyCreateProfileListener buddyCreateProfileListener, TreadlyNetworkResponse treadlyNetworkResponse) throws JSONException {
        if (!treadlyNetworkResponse.ok() || treadlyNetworkResponse.response == null) {
            runOnMain(new Runnable() { // from class: com.treadly.Treadly.UI.TreadlyVideo.Data.-$$Lambda$VideoServiceHelper$eRJoG2Av7j3cvRVr83rvFbEjQcQ
                @Override // java.lang.Runnable
                public final void run() {
                    VideoServiceHelper.BuddyCreateProfileListener.this.onResponse(VideoServiceHelper.errorCouldNotCommunicateWithServer, null);
                }
            });
            return;
        }
        final JSONObject jSONObject = treadlyNetworkResponse.response;
        if (jSONObject.has("id")) {
            runOnMain(new Runnable() { // from class: com.treadly.Treadly.UI.TreadlyVideo.Data.-$$Lambda$VideoServiceHelper$kwAkcqMsDPmAvURRPUmrLlkUCYw
                @Override // java.lang.Runnable
                public final void run() {
                    VideoServiceHelper.BuddyCreateProfileListener.this.onResponse(null, jSONObject.optString("id"));
                }
            });
        } else if (jSONObject.has("status")) {
            runOnMain(new Runnable() { // from class: com.treadly.Treadly.UI.TreadlyVideo.Data.-$$Lambda$VideoServiceHelper$PvTvYX5XW5Gj7SvZzfoLe_RXdJo
                @Override // java.lang.Runnable
                public final void run() {
                    VideoServiceHelper.BuddyCreateProfileListener.this.onResponse(jSONObject.optString("status"), null);
                }
            });
        } else {
            runOnMain(new Runnable() { // from class: com.treadly.Treadly.UI.TreadlyVideo.Data.-$$Lambda$VideoServiceHelper$wVb5VpOr3VQIMLvm3r6aj31aomg
                @Override // java.lang.Runnable
                public final void run() {
                    VideoServiceHelper.BuddyCreateProfileListener.this.onResponse(VideoServiceHelper.errorCouldNotCreateBuddyProfile, null);
                }
            });
        }
    }

    public static void updateBuddyProfile(BuddyProfileInfo buddyProfileInfo, final BuddyUpdateProfileListener buddyUpdateProfileListener) {
        UserTokenInfo userTokenInfo = TreadlyServiceManager.getInstance().tokenInfo;
        if (userTokenInfo == null) {
            buddyUpdateProfileListener.onResponse("Error: No user currently logged in", null);
            return;
        }
        JSONObject jSONObject = new JSONObject();
        JSONObject jSONObject2 = new JSONObject();
        try {
            jSONObject.put(keyHeaderAuthorization, userTokenInfo.token);
            jSONObject2.put("id", buddyProfileInfo.id);
            jSONObject2.put(keyLookingForMessage, buddyProfileInfo.lookingForMessage);
            JSONArray jSONArray = new JSONArray();
            for (BuddyProfileInfo.BuddyInterest buddyInterest : buddyProfileInfo.interests) {
                JSONObject jSONObject3 = new JSONObject();
                jSONObject3.put("id", buddyInterest.id);
                jSONObject3.put("title", buddyInterest.title);
                jSONArray.put(jSONObject3);
            }
            jSONObject2.put(keyInterests, jSONArray);
            if (buddyProfileInfo.videoPath != null) {
                jSONObject2.put(keyVideoUrl, buddyProfileInfo.videoPath);
            }
            if (buddyProfileInfo.videoThumbnailPath != null) {
                jSONObject2.put(keyVideoThumbnailUrl, buddyProfileInfo.videoThumbnailPath);
            }
            if (buddyProfileInfo.audioPath != null) {
                jSONObject2.put(keyAudioUrl, buddyProfileInfo.audioPath);
            }
            JSONArray jSONArray2 = new JSONArray();
            for (BuddyProfileInfo.BuddyDayTime buddyDayTime : buddyProfileInfo.daysOfWeek) {
                JSONObject jSONObject4 = new JSONObject();
                jSONObject4.put(keyTimeId, buddyDayTime.dayTime.id);
                jSONObject4.put(keyTimeTitle, buddyDayTime.dayTime.title);
                jSONObject4.put(keyDayOfWeek, buddyDayTime.dayOfWeek);
                jSONArray2.put(jSONObject4);
            }
            jSONObject2.put(keyDaysOfWeek, jSONArray2);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        RequestUtil.shared.postJson(SERVICE_VIDEO_BUDDY_PROFILE_UPDATE_ROUTE, jSONObject2, jSONObject, new RequestUtil.RequestUtilListener() { // from class: com.treadly.Treadly.UI.TreadlyVideo.Data.-$$Lambda$VideoServiceHelper$Ag57nuH28h9pOPcjFC5GrBRXUqY
            @Override // com.treadly.Treadly.Data.Utility.RequestUtil.RequestUtilListener
            public final void onResponse(TreadlyNetworkResponse treadlyNetworkResponse) {
                VideoServiceHelper.lambda$updateBuddyProfile$19(VideoServiceHelper.BuddyUpdateProfileListener.this, treadlyNetworkResponse);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static /* synthetic */ void lambda$updateBuddyProfile$19(final BuddyUpdateProfileListener buddyUpdateProfileListener, TreadlyNetworkResponse treadlyNetworkResponse) throws JSONException {
        if (!treadlyNetworkResponse.ok() || treadlyNetworkResponse.response == null) {
            runOnMain(new Runnable() { // from class: com.treadly.Treadly.UI.TreadlyVideo.Data.-$$Lambda$VideoServiceHelper$6fiC34sx97-upAjx_hDuhGv9WuE
                @Override // java.lang.Runnable
                public final void run() {
                    VideoServiceHelper.BuddyUpdateProfileListener.this.onResponse(VideoServiceHelper.errorCouldNotCommunicateWithServer, null);
                }
            });
            return;
        }
        final JSONObject jSONObject = treadlyNetworkResponse.response;
        if (jSONObject.has("id")) {
            runOnMain(new Runnable() { // from class: com.treadly.Treadly.UI.TreadlyVideo.Data.-$$Lambda$VideoServiceHelper$wEnAYIBq8z0c6LahaxHC9NhM95M
                @Override // java.lang.Runnable
                public final void run() {
                    VideoServiceHelper.BuddyUpdateProfileListener.this.onResponse(null, jSONObject.optString("id"));
                }
            });
        } else if (jSONObject.has("status")) {
            runOnMain(new Runnable() { // from class: com.treadly.Treadly.UI.TreadlyVideo.Data.-$$Lambda$VideoServiceHelper$acQFSZGzKiucjU7tcI29kYUM10k
                @Override // java.lang.Runnable
                public final void run() {
                    VideoServiceHelper.BuddyUpdateProfileListener.this.onResponse(jSONObject.optString("status"), null);
                }
            });
        } else {
            runOnMain(new Runnable() { // from class: com.treadly.Treadly.UI.TreadlyVideo.Data.-$$Lambda$VideoServiceHelper$esxW4RR9FtETyttmydUR-R2H85s
                @Override // java.lang.Runnable
                public final void run() {
                    VideoServiceHelper.BuddyUpdateProfileListener.this.onResponse(VideoServiceHelper.errorCouldNotUpdateBuddyProfile, null);
                }
            });
        }
    }

    public static void deleteBuddyProfile(final BuddyDeleteProfileListener buddyDeleteProfileListener) {
        UserTokenInfo userTokenInfo = TreadlyServiceManager.getInstance().tokenInfo;
        if (userTokenInfo == null) {
            buddyDeleteProfileListener.onResponse("Error: No user currently logged in");
            return;
        }
        JSONObject jSONObject = new JSONObject();
        try {
            jSONObject.put(keyHeaderAuthorization, userTokenInfo.token);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        RequestUtil.shared.postJson(SERVICE_VIDEO_BUDDY_PROFILE_DELETE_ROUTE, new JSONObject(), jSONObject, new RequestUtil.RequestUtilListener() { // from class: com.treadly.Treadly.UI.TreadlyVideo.Data.-$$Lambda$VideoServiceHelper$DXLaDqZwWqbHu500U6cqyveL4uA
            @Override // com.treadly.Treadly.Data.Utility.RequestUtil.RequestUtilListener
            public final void onResponse(TreadlyNetworkResponse treadlyNetworkResponse) {
                VideoServiceHelper.lambda$deleteBuddyProfile$23(VideoServiceHelper.BuddyDeleteProfileListener.this, treadlyNetworkResponse);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static /* synthetic */ void lambda$deleteBuddyProfile$23(final BuddyDeleteProfileListener buddyDeleteProfileListener, TreadlyNetworkResponse treadlyNetworkResponse) throws JSONException {
        if (!treadlyNetworkResponse.ok() || treadlyNetworkResponse.response == null) {
            runOnMain(new Runnable() { // from class: com.treadly.Treadly.UI.TreadlyVideo.Data.-$$Lambda$VideoServiceHelper$q6rJMS4l_-3Jps39fiKqIGUeJ34
                @Override // java.lang.Runnable
                public final void run() {
                    VideoServiceHelper.BuddyDeleteProfileListener.this.onResponse(VideoServiceHelper.errorCouldNotCommunicateWithServer);
                }
            });
            return;
        }
        JSONObject jSONObject = treadlyNetworkResponse.response;
        if (jSONObject.has("status") && jSONObject.optString("status").equals("ok")) {
            runOnMain(new Runnable() { // from class: com.treadly.Treadly.UI.TreadlyVideo.Data.-$$Lambda$VideoServiceHelper$2143TrR4j04VQgnNVyPpgDNDvag
                @Override // java.lang.Runnable
                public final void run() {
                    VideoServiceHelper.BuddyDeleteProfileListener.this.onResponse(null);
                }
            });
        } else {
            runOnMain(new Runnable() { // from class: com.treadly.Treadly.UI.TreadlyVideo.Data.-$$Lambda$VideoServiceHelper$OJHXgoTk2Q6Ey3j6FIK1weqzW3Q
                @Override // java.lang.Runnable
                public final void run() {
                    VideoServiceHelper.BuddyDeleteProfileListener.this.onResponse(VideoServiceHelper.errorCouldNotUpdateBuddyProfile);
                }
            });
        }
    }

    public static void getBuddyProfileInterests(final BuddyGetInterestsListener buddyGetInterestsListener) {
        UserTokenInfo userTokenInfo = TreadlyServiceManager.getInstance().tokenInfo;
        if (userTokenInfo == null) {
            buddyGetInterestsListener.onResponse("Error: No user currently logged in", null);
            return;
        }
        JSONObject jSONObject = new JSONObject();
        try {
            jSONObject.put(keyHeaderAuthorization, userTokenInfo.token);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        RequestUtil.shared.postJson(SERVICE_VIDEO_BUDDY_PROFILE_GET_INTERESTS_ROUTE, new JSONObject(), jSONObject, new RequestUtil.RequestUtilListener() { // from class: com.treadly.Treadly.UI.TreadlyVideo.Data.-$$Lambda$VideoServiceHelper$83Yhjb68dzXRoPZvi7KTmhQ5-IE
            @Override // com.treadly.Treadly.Data.Utility.RequestUtil.RequestUtilListener
            public final void onResponse(TreadlyNetworkResponse treadlyNetworkResponse) {
                VideoServiceHelper.lambda$getBuddyProfileInterests$27(VideoServiceHelper.BuddyGetInterestsListener.this, treadlyNetworkResponse);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static /* synthetic */ void lambda$getBuddyProfileInterests$27(final BuddyGetInterestsListener buddyGetInterestsListener, TreadlyNetworkResponse treadlyNetworkResponse) throws JSONException {
        if (!treadlyNetworkResponse.ok() || treadlyNetworkResponse.response == null) {
            runOnMain(new Runnable() { // from class: com.treadly.Treadly.UI.TreadlyVideo.Data.-$$Lambda$VideoServiceHelper$F2Nn425eJ_bFw9pS24FYE91ixSI
                @Override // java.lang.Runnable
                public final void run() {
                    VideoServiceHelper.BuddyGetInterestsListener.this.onResponse(VideoServiceHelper.errorCouldNotCommunicateWithServer, null);
                }
            });
            return;
        }
        try {
            JSONArray optJSONArray = treadlyNetworkResponse.response.optJSONArray(keyInterestList);
            if (optJSONArray != null) {
                final ArrayList arrayList = new ArrayList();
                for (int i = 0; i < optJSONArray.length(); i++) {
                    JSONObject optJSONObject = optJSONArray.optJSONObject(i);
                    if (optJSONObject.has("id") && optJSONObject.has("title")) {
                        arrayList.add(new BuddyProfileInfo.BuddyInterest(optJSONObject.optString("title"), optJSONObject.optString("id")));
                    }
                }
                runOnMain(new Runnable() { // from class: com.treadly.Treadly.UI.TreadlyVideo.Data.-$$Lambda$VideoServiceHelper$_RKNeplNKZEMCSvLDDMUWdyb70g
                    @Override // java.lang.Runnable
                    public final void run() {
                        VideoServiceHelper.BuddyGetInterestsListener.this.onResponse(null, arrayList);
                    }
                });
                return;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        runOnMain(new Runnable() { // from class: com.treadly.Treadly.UI.TreadlyVideo.Data.-$$Lambda$VideoServiceHelper$zalFJ9W186swxXXlec3iEZpWi3w
            @Override // java.lang.Runnable
            public final void run() {
                VideoServiceHelper.BuddyGetInterestsListener.this.onResponse(VideoServiceHelper.errorCouldNotCommunicateWithServer, null);
            }
        });
    }

    public static void getBuddyProfileTimesOfDay(final BuddyGetTimeOfDayListener buddyGetTimeOfDayListener) {
        UserTokenInfo userTokenInfo = TreadlyServiceManager.getInstance().tokenInfo;
        if (userTokenInfo == null) {
            buddyGetTimeOfDayListener.onResponse("Error: No user currently logged in", null);
            return;
        }
        JSONObject jSONObject = new JSONObject();
        try {
            jSONObject.put(keyHeaderAuthorization, userTokenInfo.token);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        RequestUtil.shared.postJson(SERVICE_VIDEO_BUDDY_PROFILE_GET_DAY_TIME_TYPES_ROUTE, new JSONObject(), jSONObject, new RequestUtil.RequestUtilListener() { // from class: com.treadly.Treadly.UI.TreadlyVideo.Data.-$$Lambda$VideoServiceHelper$DOx0eTFeouKGDG20XBs4It1iBJs
            @Override // com.treadly.Treadly.Data.Utility.RequestUtil.RequestUtilListener
            public final void onResponse(TreadlyNetworkResponse treadlyNetworkResponse) {
                VideoServiceHelper.lambda$getBuddyProfileTimesOfDay$31(VideoServiceHelper.BuddyGetTimeOfDayListener.this, treadlyNetworkResponse);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static /* synthetic */ void lambda$getBuddyProfileTimesOfDay$31(final BuddyGetTimeOfDayListener buddyGetTimeOfDayListener, TreadlyNetworkResponse treadlyNetworkResponse) throws JSONException {
        if (!treadlyNetworkResponse.ok() || treadlyNetworkResponse.response == null) {
            runOnMain(new Runnable() { // from class: com.treadly.Treadly.UI.TreadlyVideo.Data.-$$Lambda$VideoServiceHelper$Az0MimQnx9gotqcsNRjlZ5iWZMo
                @Override // java.lang.Runnable
                public final void run() {
                    VideoServiceHelper.BuddyGetTimeOfDayListener.this.onResponse(VideoServiceHelper.errorCouldNotCommunicateWithServer, null);
                }
            });
            return;
        }
        try {
            JSONArray optJSONArray = treadlyNetworkResponse.response.optJSONArray(keyTimeOfDayList);
            if (optJSONArray != null) {
                final ArrayList arrayList = new ArrayList();
                for (int i = 0; i < optJSONArray.length(); i++) {
                    JSONObject optJSONObject = optJSONArray.optJSONObject(i);
                    if (optJSONObject.has("id") && optJSONObject.has("title")) {
                        arrayList.add(new BuddyProfileInfo.BuddyDayTimeType(optJSONObject.optString("title"), optJSONObject.optString("id")));
                    }
                }
                runOnMain(new Runnable() { // from class: com.treadly.Treadly.UI.TreadlyVideo.Data.-$$Lambda$VideoServiceHelper$ImGvHlS8KGdfFKDwPmIEPd0uiLM
                    @Override // java.lang.Runnable
                    public final void run() {
                        VideoServiceHelper.BuddyGetTimeOfDayListener.this.onResponse(null, arrayList);
                    }
                });
                return;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        runOnMain(new Runnable() { // from class: com.treadly.Treadly.UI.TreadlyVideo.Data.-$$Lambda$VideoServiceHelper$VPL0YMv1sVGSr0aVyLbmu3r2pDA
            @Override // java.lang.Runnable
            public final void run() {
                VideoServiceHelper.BuddyGetTimeOfDayListener.this.onResponse(VideoServiceHelper.errorCouldNotCommunicateWithServer, null);
            }
        });
    }

    public static void uploadBuddyProfileVideo(String str, Uri uri, BuddyFileUploadListener buddyFileUploadListener) throws JSONException {
        uploadBuddyProfileMedia(str, uri, SERVICE_VIDEO_BUDDY_PROFILE_VIDEO_UPLOAD_ROUTE, buddyFileUploadListener);
    }

    public static void uploadBuddyProfileAudio(String str, Uri uri, BuddyFileUploadListener buddyFileUploadListener) throws JSONException {
        uploadBuddyProfileMedia(str, uri, SERVICE_VIDEO_BUDDY_PROFILE_AUDIO_UPLOAD_ROUTE, buddyFileUploadListener);
    }

    private static void uploadBuddyProfileMedia(String str, Uri uri, String str2, final BuddyFileUploadListener buddyFileUploadListener) throws JSONException {
        UserTokenInfo userTokenInfo = TreadlyServiceManager.getInstance().tokenInfo;
        if (userTokenInfo == null) {
            buddyFileUploadListener.onResponse("Error: No user currently logged in");
        }
        new JSONObject().put(keyHeaderAuthorization, userTokenInfo.token);
        File file = new File(uri.toString());
        byte[] bArr = new byte[(int) file.length()];
        try {
            FileInputStream fileInputStream = new FileInputStream(file);
            fileInputStream.read(bArr);
            fileInputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        RequestUtil.shared.postOctet(str2 + "?buddy_profile_id=" + str, bArr, new RequestUtil.RequestUtilListener() { // from class: com.treadly.Treadly.UI.TreadlyVideo.Data.-$$Lambda$VideoServiceHelper$4QgCd0Y921JjMnhZCof5AkMLlrs
            @Override // com.treadly.Treadly.Data.Utility.RequestUtil.RequestUtilListener
            public final void onResponse(TreadlyNetworkResponse treadlyNetworkResponse) {
                VideoServiceHelper.lambda$uploadBuddyProfileMedia$35(VideoServiceHelper.BuddyFileUploadListener.this, treadlyNetworkResponse);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static /* synthetic */ void lambda$uploadBuddyProfileMedia$35(final BuddyFileUploadListener buddyFileUploadListener, TreadlyNetworkResponse treadlyNetworkResponse) throws JSONException {
        if (treadlyNetworkResponse == null || !treadlyNetworkResponse.ok() || treadlyNetworkResponse.response == null) {
            runOnMain(new Runnable() { // from class: com.treadly.Treadly.UI.TreadlyVideo.Data.-$$Lambda$VideoServiceHelper$816T0nAE5q8tAd3YygOG-AeeTAA
                @Override // java.lang.Runnable
                public final void run() {
                    VideoServiceHelper.BuddyFileUploadListener.this.onResponse("Error: Could not make request");
                }
            });
            return;
        }
        String optString = treadlyNetworkResponse.response.optString("status");
        if (optString != null && optString.equals("ok")) {
            runOnMain(new Runnable() { // from class: com.treadly.Treadly.UI.TreadlyVideo.Data.-$$Lambda$VideoServiceHelper$DwnL4qrnESYAf-1ZfEHlnHP03lA
                @Override // java.lang.Runnable
                public final void run() {
                    VideoServiceHelper.BuddyFileUploadListener.this.onResponse(null);
                }
            });
        } else {
            runOnMain(new Runnable() { // from class: com.treadly.Treadly.UI.TreadlyVideo.Data.-$$Lambda$VideoServiceHelper$XkRBtxTF1qx8gAA3AHLQvNfJsXg
                @Override // java.lang.Runnable
                public final void run() {
                    VideoServiceHelper.BuddyFileUploadListener.this.onResponse("Error: Could not parse results");
                }
            });
        }
    }

    private static void createVideoLike(String str, String str2, String str3, VideoResponseListener videoResponseListener) {
        UserTokenInfo userTokenInfo = TreadlyServiceManager.getInstance().tokenInfo;
        if (userTokenInfo == null) {
            videoResponseListener.onResponse("Error: No user currently logged in");
            return;
        }
        JSONObject jSONObject = new JSONObject();
        try {
            jSONObject.put("type", str);
            jSONObject.put("video_id", str2);
            jSONObject.put(keyServiceId, str3);
            requestBasicAuthPost(SERVICE_VIDEO_VIDEO_LIKE_CREATE_ROUTE, userTokenInfo.token, jSONObject, videoResponseListener);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private static void deleteVideoLike(String str, String str2, String str3, VideoResponseListener videoResponseListener) {
        UserTokenInfo userTokenInfo = TreadlyServiceManager.getInstance().tokenInfo;
        if (userTokenInfo == null) {
            videoResponseListener.onResponse("Error: No user currently logged in");
            return;
        }
        JSONObject jSONObject = new JSONObject();
        try {
            jSONObject.put("type", str);
            jSONObject.put("video_id", str2);
            jSONObject.put(keyServiceId, str3);
            requestBasicAuthPost(SERVICE_VIDEO_VIDEO_LIKE_DELETE_ROUTE, userTokenInfo.token, jSONObject, videoResponseListener);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private static void getVideoLikeInfo(String str, String str2, final VideoLikeListener videoLikeListener) {
        UserTokenInfo userTokenInfo = TreadlyServiceManager.getInstance().tokenInfo;
        if (userTokenInfo == null) {
            videoLikeListener.onGetVideoLikeInfo("Error: No user currently logged in", null);
            return;
        }
        JSONObject jSONObject = new JSONObject();
        JSONObject jSONObject2 = new JSONObject();
        try {
            jSONObject.put(keyHeaderAuthorization, userTokenInfo.token);
            jSONObject2.put("video_id", str);
            jSONObject2.put(keyServiceId, str2);
            RequestUtil.shared.postJson(SERVICE_VIDEO_VIDEO_LIKE_GET_ROUTE, jSONObject2, jSONObject, new RequestUtil.RequestUtilListener() { // from class: com.treadly.Treadly.UI.TreadlyVideo.Data.VideoServiceHelper.13
                @Override // com.treadly.Treadly.Data.Utility.RequestUtil.RequestUtilListener
                public void onResponse(TreadlyNetworkResponse treadlyNetworkResponse) throws JSONException {
                    if (!treadlyNetworkResponse.ok() || treadlyNetworkResponse.response == null) {
                        VideoServiceHelper.runOnMain(new Runnable() { // from class: com.treadly.Treadly.UI.TreadlyVideo.Data.VideoServiceHelper.13.1
                            @Override // java.lang.Runnable
                            public void run() {
                                VideoLikeListener.this.onGetVideoLikeInfo("Error: Could not make request", null);
                            }
                        });
                        return;
                    }
                    JSONObject optJSONObject = treadlyNetworkResponse.response.optJSONObject("data");
                    if (optJSONObject != null) {
                        final VideoLikeInfo videoLikeInfo = new VideoLikeInfo(optJSONObject.optString("video_id"), optJSONObject.optString(VideoServiceHelper.keyServiceId), Integer.valueOf(optJSONObject.optInt("like_count")), Boolean.valueOf(optJSONObject.optBoolean(VideoServiceHelper.keyIsLike)));
                        VideoServiceHelper.runOnMain(new Runnable() { // from class: com.treadly.Treadly.UI.TreadlyVideo.Data.VideoServiceHelper.13.2
                            @Override // java.lang.Runnable
                            public void run() {
                                VideoLikeListener.this.onGetVideoLikeInfo(null, videoLikeInfo);
                            }
                        });
                        return;
                    }
                    VideoServiceHelper.runOnMain(new Runnable() { // from class: com.treadly.Treadly.UI.TreadlyVideo.Data.VideoServiceHelper.13.3
                        @Override // java.lang.Runnable
                        public void run() {
                            VideoLikeListener.this.onGetVideoLikeInfo("Error: Could not parse results", null);
                        }
                    });
                }
            });
        } catch (JSONException e) {
            e.printStackTrace();
            runOnMain(new Runnable() { // from class: com.treadly.Treadly.UI.TreadlyVideo.Data.VideoServiceHelper.14
                @Override // java.lang.Runnable
                public void run() {
                    VideoLikeListener.this.onGetVideoLikeInfo(e.toString(), null);
                }
            });
        }
    }

    private static void getVideoLikeUserInfo(String str, String str2, final VideoLikeListener videoLikeListener) {
        UserTokenInfo userTokenInfo = TreadlyServiceManager.getInstance().tokenInfo;
        if (userTokenInfo == null) {
            videoLikeListener.onGetVideoLikeUserInfo("Error: No user currently logged in", null);
            return;
        }
        JSONObject jSONObject = new JSONObject();
        JSONObject jSONObject2 = new JSONObject();
        try {
            jSONObject.put(keyHeaderAuthorization, userTokenInfo.token);
            jSONObject2.put("video_id", str);
            jSONObject2.put(keyServiceId, str2);
            RequestUtil.shared.postJson(SERVICE_VIDEO_VIDEO_LIKE_USER_GET_ROUTE, jSONObject2, jSONObject, new RequestUtil.RequestUtilListener() { // from class: com.treadly.Treadly.UI.TreadlyVideo.Data.VideoServiceHelper.15
                @Override // com.treadly.Treadly.Data.Utility.RequestUtil.RequestUtilListener
                public void onResponse(TreadlyNetworkResponse treadlyNetworkResponse) throws JSONException {
                    if (!treadlyNetworkResponse.ok() || treadlyNetworkResponse.response == null) {
                        VideoServiceHelper.runOnMain(new Runnable() { // from class: com.treadly.Treadly.UI.TreadlyVideo.Data.VideoServiceHelper.15.1
                            @Override // java.lang.Runnable
                            public void run() {
                                VideoLikeListener.this.onGetVideoLikeUserInfo("Error: Could not make request", null);
                            }
                        });
                        return;
                    }
                    JSONArray optJSONArray = treadlyNetworkResponse.response.optJSONArray("data");
                    if (optJSONArray != null) {
                        final ArrayList arrayList = new ArrayList();
                        for (int i = 0; i < optJSONArray.length(); i++) {
                            JSONObject optJSONObject = optJSONArray.optJSONObject(i);
                            String optString = optJSONObject.optString("user_id");
                            String optString2 = optJSONObject.optString(VideoServiceHelper.keyUserName);
                            String optString3 = optJSONObject.optString(VideoServiceHelper.keyCreatedAt);
                            String optString4 = optJSONObject.optString(VideoServiceHelper.keyUserAvatar);
                            if (optString4.isEmpty()) {
                                optString4 = "https://dgwxv5s2i5zkb.cloudfront.net/avatar/default.png";
                            }
                            arrayList.add(new VideoLikeUserInfo(optString, optString2, optString4, optString3));
                        }
                        VideoServiceHelper.runOnMain(new Runnable() { // from class: com.treadly.Treadly.UI.TreadlyVideo.Data.VideoServiceHelper.15.2
                            @Override // java.lang.Runnable
                            public void run() {
                                VideoLikeListener.this.onGetVideoLikeUserInfo(null, arrayList);
                            }
                        });
                        return;
                    }
                    VideoServiceHelper.runOnMain(new Runnable() { // from class: com.treadly.Treadly.UI.TreadlyVideo.Data.VideoServiceHelper.15.3
                        @Override // java.lang.Runnable
                        public void run() {
                            VideoLikeListener.this.onGetVideoLikeUserInfo("Error: Could not parse results", null);
                        }
                    });
                }
            });
        } catch (JSONException e) {
            e.printStackTrace();
            runOnMain(new Runnable() { // from class: com.treadly.Treadly.UI.TreadlyVideo.Data.VideoServiceHelper.16
                @Override // java.lang.Runnable
                public void run() {
                    VideoLikeListener.this.onGetVideoLikeUserInfo(e.toString(), null);
                }
            });
        }
    }

    public static void startSessionArchive(String str, int i, String str2, final VideoStartSessionArchiveListener videoStartSessionArchiveListener) {
        JSONObject jSONObject = new JSONObject();
        try {
            jSONObject.put("session_id", str);
            jSONObject.put("name", str2);
            if (i > 0) {
                jSONObject.put(keyWorkoutId, i);
            }
            RequestUtil.shared.postJson(SERVICE_URI_ROUTE_START_SESSION_ARCHIVE, jSONObject, new RequestUtil.RequestUtilListener() { // from class: com.treadly.Treadly.UI.TreadlyVideo.Data.VideoServiceHelper.17
                @Override // com.treadly.Treadly.Data.Utility.RequestUtil.RequestUtilListener
                public void onResponse(TreadlyNetworkResponse treadlyNetworkResponse) throws JSONException {
                    if (!treadlyNetworkResponse.ok() || treadlyNetworkResponse.response == null) {
                        VideoServiceHelper.runOnMain(new Runnable() { // from class: com.treadly.Treadly.UI.TreadlyVideo.Data.VideoServiceHelper.17.1
                            @Override // java.lang.Runnable
                            public void run() {
                                VideoStartSessionArchiveListener.this.onResponse("Error: Could not make request", null);
                            }
                        });
                        return;
                    }
                    final String optString = treadlyNetworkResponse.response.optString(VideoServiceHelper.keyArchiveId);
                    if (!optString.isEmpty()) {
                        VideoServiceHelper.runOnMain(new Runnable() { // from class: com.treadly.Treadly.UI.TreadlyVideo.Data.VideoServiceHelper.17.2
                            @Override // java.lang.Runnable
                            public void run() {
                                VideoStartSessionArchiveListener.this.onResponse(null, optString);
                            }
                        });
                    } else {
                        VideoServiceHelper.runOnMain(new Runnable() { // from class: com.treadly.Treadly.UI.TreadlyVideo.Data.VideoServiceHelper.17.3
                            @Override // java.lang.Runnable
                            public void run() {
                                VideoStartSessionArchiveListener.this.onResponse("Error: Could not parse results", null);
                            }
                        });
                    }
                }
            });
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public static void stopSessionArchive(String str, VideoResponseListener videoResponseListener) {
        JSONObject jSONObject = new JSONObject();
        try {
            jSONObject.put(keyArchiveId, str);
            requestBasicUsersPost(SERVICE_URI_ROUTE_STOP_SESSION_ARCHIVE, jSONObject, videoResponseListener);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public static void deleteArchive(String str, VideoResponseListener videoResponseListener) {
        JSONObject jSONObject = new JSONObject();
        try {
            jSONObject.put(keyArchiveId, str);
            requestBasicUsersPost(SERVICE_URI_ROUTE_DELETE_ARCHIVE, jSONObject, videoResponseListener);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public static void deleteVideo(String str, VideoResponseListener videoResponseListener) {
        UserTokenInfo userTokenInfo = TreadlyServiceManager.getInstance().tokenInfo;
        if (userTokenInfo == null || userTokenInfo.token == null) {
            videoResponseListener.onResponse("Error: No user currently logged in");
            return;
        }
        JSONObject jSONObject = new JSONObject();
        try {
            jSONObject.put("video_id", str);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        requestBasicAuthPost(SERVICE_VIDEO_VIDEO_DELETE_ROUTE, userTokenInfo.token, jSONObject, videoResponseListener);
    }

    public static void getVideoInfoList(String str, String str2, final videoInfoResultsListener videoinforesultslistener) {
        try {
            JSONObject jSONObject = new JSONObject();
            jSONObject.put("user_id", str);
            if (str2 != null) {
                jSONObject.put(keyTargetId, str2);
            }
            RequestUtil.shared.postJson(SERVICE_VIDEO_GET_VIDEO_LIST, jSONObject, new RequestUtil.RequestUtilListener() { // from class: com.treadly.Treadly.UI.TreadlyVideo.Data.VideoServiceHelper.18
                @Override // com.treadly.Treadly.Data.Utility.RequestUtil.RequestUtilListener
                public void onResponse(TreadlyNetworkResponse treadlyNetworkResponse) {
                    if (treadlyNetworkResponse == null || !treadlyNetworkResponse.ok() || treadlyNetworkResponse.response == null) {
                        VideoServiceHelper.runOnMain(new Runnable() { // from class: com.treadly.Treadly.UI.TreadlyVideo.Data.VideoServiceHelper.18.1
                            @Override // java.lang.Runnable
                            public void run() {
                                videoInfoResultsListener.this.onVideoInfoResponse("Error: Could not make request", null);
                            }
                        });
                        return;
                    }
                    JSONArray optJSONArray = treadlyNetworkResponse.response.optJSONArray(VideoServiceHelper.keyVideoList);
                    if (optJSONArray == null) {
                        return;
                    }
                    final VideoServiceVideoInfo[] videoServiceVideoInfoArr = new VideoServiceVideoInfo[optJSONArray.length()];
                    for (int i = 0; i < optJSONArray.length(); i++) {
                        try {
                            videoServiceVideoInfoArr[i] = VideoServiceHelper.parseVideoInfo(optJSONArray.getJSONObject(i));
                        } catch (JSONException e) {
                            e.printStackTrace();
                            VideoServiceHelper.runOnMain(new Runnable() { // from class: com.treadly.Treadly.UI.TreadlyVideo.Data.VideoServiceHelper.18.2
                                @Override // java.lang.Runnable
                                public void run() {
                                    videoInfoResultsListener.this.onVideoInfoResponse("Error: Could not parse results", null);
                                }
                            });
                            return;
                        }
                    }
                    VideoServiceHelper.runOnMain(new Runnable() { // from class: com.treadly.Treadly.UI.TreadlyVideo.Data.VideoServiceHelper.18.3
                        @Override // java.lang.Runnable
                        public void run() {
                            videoInfoResultsListener.this.onVideoInfoResponse(null, videoServiceVideoInfoArr);
                        }
                    });
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static VideoServiceVideoInfo parseVideoInfo(JSONObject jSONObject) {
        String optString;
        String str;
        String optString2 = jSONObject.optString("id");
        String optString3 = jSONObject.optString("type");
        String optString4 = jSONObject.optString("user_id");
        String optString5 = jSONObject.optString("service");
        String optString6 = jSONObject.optString(keyServiceId);
        String optString7 = jSONObject.optString("url");
        String optString8 = jSONObject.optString("description");
        String optString9 = jSONObject.optString(keyCreatedAt);
        int optInt = jSONObject.optInt(keyPermission);
        if (optString2 == null || optString3 == null || optString4 == null || optString5 == null || optString6 == null || optString7 == null || optString8 == null || optString9 == null || !jSONObject.has(keyPermission)) {
            return null;
        }
        if (StreamPermission.fromValue(optInt) == null) {
            return null;
        }
        StreamPermission fromValue = StreamPermission.fromValue(optInt);
        String optString10 = jSONObject.optString(keyUserName);
        String optString11 = jSONObject.optString(keyUserAvatar);
        String str2 = (optString11 == null || optString11.length() == 0) ? "https://dgwxv5s2i5zkb.cloudfront.net/avatar/default.png" : "https://dgwxv5s2i5zkb.cloudfront.net/avatar/default.png";
        String optString12 = jSONObject.optString(keyThumbnailUrl);
        String optString13 = jSONObject.optString(keyPlaceholderUrl);
        int optInt2 = jSONObject.optInt("duration");
        int optInt3 = jSONObject.optInt(keySize);
        boolean z = jSONObject.optInt(keyAllowComments, 0) == 1;
        boolean z2 = jSONObject.optInt(keyAllowVoiceMessage, 0) == 1;
        Date date = getDate(optString9, new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()));
        JSONObject optJSONObject = jSONObject.optJSONObject(keyGroupInfo);
        if (optJSONObject == null) {
            str = null;
            optString = null;
        } else {
            String optString14 = optJSONObject.optString("id");
            optString = optJSONObject.optString("title");
            str = optString14;
        }
        return new VideoServiceVideoInfo(optString2, optString4, optString10, str2, optString3, optString5, optString6, optString7, optString12, optString13, optString8, optInt2, optInt3, date, z, z2, fromValue, str, optString);
    }

    public static void createUserBlock(String str, VideoResponseListener videoResponseListener) {
        UserTokenInfo userTokenInfo = TreadlyServiceManager.getInstance().tokenInfo;
        if (userTokenInfo == null || userTokenInfo.token == null) {
            videoResponseListener.onResponse("Error: No user currently logged in");
            return;
        }
        JSONObject jSONObject = new JSONObject();
        try {
            jSONObject.put("user_id", str);
            requestBasicAuthPost(SERVICE_VIDEO_USER_BLOCK_CREATE_ROUTE, userTokenInfo.token, jSONObject, videoResponseListener);
        } catch (JSONException e) {
            e.printStackTrace();
            videoResponseListener.onResponse(e.toString());
        }
    }

    public static void deleteUserBlock(String str, VideoResponseListener videoResponseListener) {
        UserTokenInfo userTokenInfo = TreadlyServiceManager.getInstance().tokenInfo;
        if (userTokenInfo == null || userTokenInfo.token == null) {
            videoResponseListener.onResponse("Error: No user currently logged in");
            return;
        }
        JSONObject jSONObject = new JSONObject();
        try {
            jSONObject.put("user_id", str);
            requestBasicAuthPost(SERVICE_VIDEO_USER_BLOCK_DELETE_ROUTE, userTokenInfo.token, jSONObject, videoResponseListener);
        } catch (JSONException e) {
            e.printStackTrace();
            videoResponseListener.onResponse(e.toString());
        }
    }

    public static void createVideoReport(String str, String str2, String str3, VideoResponseListener videoResponseListener) {
        UserTokenInfo userTokenInfo = TreadlyServiceManager.getInstance().tokenInfo;
        if (userTokenInfo == null || userTokenInfo.token == null) {
            videoResponseListener.onResponse("Error: No user currently logged in");
            return;
        }
        JSONObject jSONObject = new JSONObject();
        try {
            jSONObject.put("video_id", str);
            jSONObject.put(keyReasonId, str2);
            jSONObject.put(keyReasonDescription, str3);
            requestBasicAuthPost(SERVICE_VIDEO_VIDEO_REPORT_CREATE_ROUTE, userTokenInfo.token, jSONObject, videoResponseListener);
        } catch (JSONException e) {
            e.printStackTrace();
            videoResponseListener.onResponse(e.toString());
        }
    }

    public static void createUserReport(String str, String str2, String str3, VideoResponseListener videoResponseListener) {
        UserTokenInfo userTokenInfo = TreadlyServiceManager.getInstance().tokenInfo;
        if (userTokenInfo == null || userTokenInfo.token == null) {
            videoResponseListener.onResponse("Error: No user currently logged in");
            return;
        }
        JSONObject jSONObject = new JSONObject();
        try {
            jSONObject.put("user_id", str);
            jSONObject.put(keyReasonId, str2);
            jSONObject.put(keyReasonDescription, str3);
            requestBasicAuthPost(SERVICE_VIDEO_USER_REPORT_CREATE_ROUTE, userTokenInfo.token, jSONObject, videoResponseListener);
        } catch (JSONException e) {
            e.printStackTrace();
            videoResponseListener.onResponse(e.toString());
        }
    }

    public static void getReportReasons(final VideoGetReportReasonsListener videoGetReportReasonsListener) {
        UserTokenInfo userTokenInfo = TreadlyServiceManager.getInstance().tokenInfo;
        if (userTokenInfo == null || userTokenInfo.token == null) {
            videoGetReportReasonsListener.onResponse("Error: No user currently logged in", null);
            return;
        }
        JSONObject jSONObject = new JSONObject();
        try {
            jSONObject.put(keyHeaderAuthorization, userTokenInfo.token);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        RequestUtil.shared.postJson(SERVICE_VIDEO_REPORT_REASON_GET_ROUTE, new JSONObject(), jSONObject, new RequestUtil.RequestUtilListener() { // from class: com.treadly.Treadly.UI.TreadlyVideo.Data.VideoServiceHelper.19
            @Override // com.treadly.Treadly.Data.Utility.RequestUtil.RequestUtilListener
            public void onResponse(TreadlyNetworkResponse treadlyNetworkResponse) throws JSONException {
                if (treadlyNetworkResponse == null || !treadlyNetworkResponse.ok() || treadlyNetworkResponse.response == null) {
                    VideoServiceHelper.runOnMain(new Runnable() { // from class: com.treadly.Treadly.UI.TreadlyVideo.Data.VideoServiceHelper.19.1
                        @Override // java.lang.Runnable
                        public void run() {
                            VideoGetReportReasonsListener.this.onResponse("Error: Could not make request", null);
                        }
                    });
                    return;
                }
                JSONArray optJSONArray = treadlyNetworkResponse.response.optJSONArray(VideoServiceHelper.keyReasonList);
                if (optJSONArray != null) {
                    final ArrayList arrayList = new ArrayList();
                    for (int i = 0; i < optJSONArray.length(); i++) {
                        JSONObject optJSONObject = optJSONArray.optJSONObject(i);
                        String optString = optJSONObject.optString("id");
                        String optString2 = optJSONObject.optString("title");
                        if (optString != null && optString2 != null) {
                            arrayList.add(new ReportReasonInfo(optString, optString2));
                        }
                    }
                    VideoServiceHelper.runOnMain(new Runnable() { // from class: com.treadly.Treadly.UI.TreadlyVideo.Data.VideoServiceHelper.19.2
                        @Override // java.lang.Runnable
                        public void run() {
                            VideoGetReportReasonsListener.this.onResponse(null, arrayList);
                        }
                    });
                    return;
                }
                VideoServiceHelper.runOnMain(new Runnable() { // from class: com.treadly.Treadly.UI.TreadlyVideo.Data.VideoServiceHelper.19.3
                    @Override // java.lang.Runnable
                    public void run() {
                        VideoGetReportReasonsListener.this.onResponse("Error: Could not parse results", null);
                    }
                });
            }
        });
    }

    public static void registerForNotification(String str, String str2, Boolean bool, VideoResponseListener videoResponseListener) {
        UserTokenInfo userTokenInfo = TreadlyServiceManager.getInstance().tokenInfo;
        if (userTokenInfo == null) {
            videoResponseListener.onResponse("Error: No user currently logged in");
            return;
        }
        JSONObject jSONObject = new JSONObject();
        try {
            jSONObject.put(keyDeviceToken, str);
            jSONObject.put(keyBundleId, str2);
            jSONObject.put(keyUseSandbox, bool);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        requestBasicAuthPost(SERVICE_VIDEO_NOTIFICATION_REGISTER_ROUTE, userTokenInfo.token, jSONObject, videoResponseListener);
    }

    public static void unregisterForNotification(VideoResponseListener videoResponseListener) {
        UserTokenInfo userTokenInfo = TreadlyServiceManager.getInstance().tokenInfo;
        if (userTokenInfo == null) {
            videoResponseListener.onResponse("Error: No user currently logged in");
            return;
        }
        requestBasicAuthPost(SERVICE_VIDEO_NOTIFICATION_UNREGISTER_ROUTE, userTokenInfo.token, new JSONObject(), videoResponseListener);
    }

    static Date getDate(String str, SimpleDateFormat simpleDateFormat) {
        try {
            return simpleDateFormat.parse(str);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static void getRunningSessionParticipants(Date date, Date date2, final videoParticipantsListener videoparticipantslistener) {
        JSONObject jSONObject;
        UserTokenInfo userTokenInfo = TreadlyServiceManager.getInstance().tokenInfo;
        if (userTokenInfo == null) {
            videoparticipantslistener.onParticipantsInfoResponse("Error: No user currently logged in", null);
        }
        JSONObject jSONObject2 = new JSONObject();
        try {
            jSONObject = jSONObject2.put(keyHeaderAuthorization, userTokenInfo.token);
        } catch (Exception e) {
            e.printStackTrace();
            jSONObject = jSONObject2;
        }
        JSONObject jSONObject3 = new JSONObject();
        try {
            jSONObject3.put(keyStartTime, convertDateToServiceTimestamp(date));
            jSONObject3.put(keyEndTime, convertDateToServiceTimestamp(date2));
        } catch (Exception e2) {
            e2.printStackTrace();
        }
        RequestUtil.shared.postJson(SERVICE_VIDEO_RUNNING_SESSION_PARTICIPANT_GET_ROUTE, jSONObject3, jSONObject, new RequestUtil.RequestUtilListener() { // from class: com.treadly.Treadly.UI.TreadlyVideo.Data.VideoServiceHelper.20
            @Override // com.treadly.Treadly.Data.Utility.RequestUtil.RequestUtilListener
            public void onResponse(TreadlyNetworkResponse treadlyNetworkResponse) {
                if (!treadlyNetworkResponse.ok() || treadlyNetworkResponse.response == null) {
                    VideoServiceHelper.runOnMain(new Runnable() { // from class: com.treadly.Treadly.UI.TreadlyVideo.Data.VideoServiceHelper.20.1
                        @Override // java.lang.Runnable
                        public void run() {
                            videoParticipantsListener.this.onParticipantsInfoResponse("Error, Could not make request", null);
                        }
                    });
                    return;
                }
                JSONArray optJSONArray = treadlyNetworkResponse.response.optJSONArray(VideoServiceHelper.keyParticipantList);
                if (optJSONArray != null) {
                    final ArrayList arrayList = new ArrayList();
                    for (int i = 0; i < optJSONArray.length(); i++) {
                        try {
                            JSONObject jSONObject4 = optJSONArray.getJSONObject(i);
                            String optString = jSONObject4.optString(VideoServiceHelper.keyParticipantId);
                            String optString2 = jSONObject4.optString(VideoServiceHelper.keyJoinedAt);
                            if (optString != null && optString2 != null) {
                                arrayList.add(new UserRunningSessionParticipantInfo(VideoServiceHelper.getDate(optString2, new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())), optString));
                            }
                        } catch (Exception e3) {
                            e3.printStackTrace();
                        }
                    }
                    VideoServiceHelper.runOnMain(new Runnable() { // from class: com.treadly.Treadly.UI.TreadlyVideo.Data.VideoServiceHelper.20.2
                        @Override // java.lang.Runnable
                        public void run() {
                            videoParticipantsListener.this.onParticipantsInfoResponse(null, arrayList);
                        }
                    });
                    return;
                }
                VideoServiceHelper.runOnMain(new Runnable() { // from class: com.treadly.Treadly.UI.TreadlyVideo.Data.VideoServiceHelper.20.3
                    @Override // java.lang.Runnable
                    public void run() {
                        videoParticipantsListener.this.onParticipantsInfoResponse("Error: Could not parse results", null);
                    }
                });
            }
        });
    }

    public static void getNotificationSettings(final VideoNotificationsListener videoNotificationsListener) {
        UserTokenInfo userTokenInfo = TreadlyServiceManager.getInstance().tokenInfo;
        if (userTokenInfo == null) {
            videoNotificationsListener.onResponse("Error: No user currently logged in", null);
            return;
        }
        RequestUtil.shared.postJson(SERVICE_VIDEO_NOTIFICATION_SETTING_GET_ROUTE, new JSONObject(), getHeader(userTokenInfo.token), new RequestUtil.RequestUtilListener() { // from class: com.treadly.Treadly.UI.TreadlyVideo.Data.VideoServiceHelper.21
            @Override // com.treadly.Treadly.Data.Utility.RequestUtil.RequestUtilListener
            public void onResponse(TreadlyNetworkResponse treadlyNetworkResponse) {
                final UserNotificationSettingInfo parseUserNotificationSettingInfo;
                if (!treadlyNetworkResponse.ok() || treadlyNetworkResponse.response == null) {
                    VideoServiceHelper.runOnMain(new Runnable() { // from class: com.treadly.Treadly.UI.TreadlyVideo.Data.VideoServiceHelper.21.1
                        @Override // java.lang.Runnable
                        public void run() {
                            VideoNotificationsListener.this.onResponse("Error: Could not make request", null);
                        }
                    });
                    return;
                }
                JSONObject optJSONObject = treadlyNetworkResponse.response.optJSONObject(VideoServiceHelper.keyNotification);
                if (optJSONObject != null && (parseUserNotificationSettingInfo = VideoServiceHelper.parseUserNotificationSettingInfo(optJSONObject)) != null) {
                    VideoServiceHelper.runOnMain(new Runnable() { // from class: com.treadly.Treadly.UI.TreadlyVideo.Data.VideoServiceHelper.21.2
                        @Override // java.lang.Runnable
                        public void run() {
                            VideoNotificationsListener.this.onResponse(null, parseUserNotificationSettingInfo);
                        }
                    });
                } else {
                    VideoServiceHelper.runOnMain(new Runnable() { // from class: com.treadly.Treadly.UI.TreadlyVideo.Data.VideoServiceHelper.21.3
                        @Override // java.lang.Runnable
                        public void run() {
                            VideoNotificationsListener.this.onResponse("Error: Could not parse results", null);
                        }
                    });
                }
            }
        });
    }

    public static void updateNotificationSettings(UserNotificationSettingRequest userNotificationSettingRequest, final VideoResponseListener videoResponseListener) {
        UserTokenInfo userTokenInfo = TreadlyServiceManager.getInstance().tokenInfo;
        if (userTokenInfo == null) {
            videoResponseListener.onResponse("Error: No user currently logged in");
            return;
        }
        try {
            JSONObject jSONObject = new JSONObject();
            jSONObject.put(keyDailyGoal50Achieved, userNotificationSettingRequest.dailyGoal50Achieved);
            jSONObject.put(keyDailyGoal100Achieved, userNotificationSettingRequest.dailyGoal100Achieved);
            jSONObject.put(keyGroupRequestApproval, userNotificationSettingRequest.groupRequestApproval);
            jSONObject.put(keyNewPostToYourGroup, userNotificationSettingRequest.newPostToYourGroup);
            jSONObject.put(keyWorkoutSchedule, userNotificationSettingRequest.workoutSchedule);
            jSONObject.put(keyReplyToYourGroupPost, userNotificationSettingRequest.replyToYourGroupPost);
            jSONObject.put(keyReplyToYourGroupComment, userNotificationSettingRequest.replyToYourGroupComment);
            jSONObject.put(keyLikeToYourGroupPost, userNotificationSettingRequest.likeToYourGroupPost);
            jSONObject.put(keyFriendRequest, userNotificationSettingRequest.friendRequest);
            jSONObject.put(keyVideoInvite, userNotificationSettingRequest.videoInvite);
            jSONObject.put(keyFriendOnlineStatus, userNotificationSettingRequest.friendOnlineStatus);
            jSONObject.put(keyLikeToYourVideo, userNotificationSettingRequest.likeToYourVideo);
            requestBasicAuthPost(SERVICE_VIDEO_NOTIFICATION_SETTING_UPDATE_ROUTE, userTokenInfo.token, jSONObject, videoResponseListener);
        } catch (Exception e) {
            e.printStackTrace();
            runOnMain(new Runnable() { // from class: com.treadly.Treadly.UI.TreadlyVideo.Data.-$$Lambda$VideoServiceHelper$e7iUZ9GBYOkjUdgqEpknPVufu7o
                @Override // java.lang.Runnable
                public final void run() {
                    VideoServiceHelper.VideoResponseListener.this.onResponse("Error: Could not make the request");
                }
            });
        }
    }

    public static void activateInviteCode(String str, final VideoResponseListener videoResponseListener) {
        UserTokenInfo userTokenInfo = TreadlyServiceManager.getInstance().tokenInfo;
        if (userTokenInfo == null) {
            videoResponseListener.onResponse("Error: No user currently logged in");
            return;
        }
        try {
            JSONObject jSONObject = new JSONObject();
            jSONObject.put(keyHeaderAuthorization, userTokenInfo.token);
            JSONObject jSONObject2 = new JSONObject();
            jSONObject2.put(keyInviteCode, str);
            RequestUtil.shared.postJson(SERVICE_VIDEO_INVITE_CODE_ACTIVATE, jSONObject2, jSONObject, new RequestUtil.RequestUtilListener() { // from class: com.treadly.Treadly.UI.TreadlyVideo.Data.-$$Lambda$VideoServiceHelper$X7A17GkNXNKM2jxflaN32kXZWoE
                @Override // com.treadly.Treadly.Data.Utility.RequestUtil.RequestUtilListener
                public final void onResponse(TreadlyNetworkResponse treadlyNetworkResponse) {
                    VideoServiceHelper.lambda$activateInviteCode$39(VideoServiceHelper.VideoResponseListener.this, treadlyNetworkResponse);
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
            runOnMain(new Runnable() { // from class: com.treadly.Treadly.UI.TreadlyVideo.Data.-$$Lambda$VideoServiceHelper$TUdeOH_cTdeUTaTb87tJ33jkaUU
                @Override // java.lang.Runnable
                public final void run() {
                    VideoServiceHelper.VideoResponseListener.this.onResponse("Error: Could not make request");
                }
            });
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static /* synthetic */ void lambda$activateInviteCode$39(final VideoResponseListener videoResponseListener, TreadlyNetworkResponse treadlyNetworkResponse) throws JSONException {
        if (!treadlyNetworkResponse.ok() || treadlyNetworkResponse.response == null) {
            runOnMain(new Runnable() { // from class: com.treadly.Treadly.UI.TreadlyVideo.Data.-$$Lambda$VideoServiceHelper$QkYK0JRx_JPndqCKtYoeWzkugJQ
                @Override // java.lang.Runnable
                public final void run() {
                    VideoServiceHelper.VideoResponseListener.this.onResponse("Error: Could not make request");
                }
            });
            return;
        }
        JSONObject optJSONObject = treadlyNetworkResponse.response.optJSONObject("data");
        final String str = null;
        if (optJSONObject != null) {
            int i = optJSONObject.getInt("invite_code_valid");
            optJSONObject.getInt("invite_code_valid");
            Log.d("video_service_helper", "activateInviteCode: NRS " + optJSONObject.toString());
            if (i == 0) {
                str = "Error: Invite code invalid";
            }
        } else {
            str = "Error: Could not make request";
        }
        runOnMain(new Runnable() { // from class: com.treadly.Treadly.UI.TreadlyVideo.Data.-$$Lambda$VideoServiceHelper$eDdEnFSDTEYQ_xiuZP8teIfL5TM
            @Override // java.lang.Runnable
            public final void run() {
                VideoServiceHelper.VideoResponseListener.this.onResponse(str);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static UserNotificationSettingInfo parseUserNotificationSettingInfo(JSONObject jSONObject) {
        if (jSONObject != null && jSONObject.has(keyEnable) && jSONObject.has(keyDailyGoal50Achieved) && jSONObject.has(keyDailyGoal100Achieved) && jSONObject.has(keyGroupRequestApproval) && jSONObject.has(keyNewPostToYourGroup) && jSONObject.has(keyWorkoutSchedule) && jSONObject.has(keyReplyToYourGroupPost) && jSONObject.has(keyReplyToYourGroupComment) && jSONObject.has(keyLikeToYourGroupPost) && jSONObject.has(keyFriendRequest) && jSONObject.has(keyVideoInvite) && jSONObject.has(keyFriendOnlineStatus) && jSONObject.has(keyLikeToYourVideo)) {
            return new UserNotificationSettingInfo(jSONObject.optBoolean(keyEnable), jSONObject.optBoolean(keyDailyGoal50Achieved), jSONObject.optBoolean(keyDailyGoal100Achieved), jSONObject.optBoolean(keyGroupRequestApproval), jSONObject.optBoolean(keyNewPostToYourGroup), jSONObject.optBoolean(keyWorkoutSchedule), jSONObject.optBoolean(keyReplyToYourGroupPost), jSONObject.optBoolean(keyReplyToYourGroupComment), jSONObject.optBoolean(keyLikeToYourGroupPost), jSONObject.optBoolean(keyFriendRequest), jSONObject.optBoolean(keyVideoInvite), jSONObject.optBoolean(keyFriendOnlineStatus), jSONObject.optBoolean(keyLikeToYourVideo));
        }
        return null;
    }

    public static void getTrainerModeEnabled(String str, final VideoGetTrainerModeEnabled videoGetTrainerModeEnabled) {
        UserTokenInfo userTokenInfo = TreadlyServiceManager.getInstance().tokenInfo;
        if (userTokenInfo == null) {
            videoGetTrainerModeEnabled.onResponse("Error: No user currently logged in", null);
            return;
        }
        JSONObject jSONObject = new JSONObject();
        JSONObject jSONObject2 = new JSONObject();
        try {
            jSONObject.put(keyHeaderAuthorization, userTokenInfo.token);
            jSONObject2.put("user_id", str);
            RequestUtil.shared.postJson(SERVICE_VIDEO_GET_TRAINER_MODE, jSONObject2, jSONObject, new RequestUtil.RequestUtilListener() { // from class: com.treadly.Treadly.UI.TreadlyVideo.Data.-$$Lambda$VideoServiceHelper$gv1mYFkh7h5g4VICG8NHUlzDmU8
                @Override // com.treadly.Treadly.Data.Utility.RequestUtil.RequestUtilListener
                public final void onResponse(TreadlyNetworkResponse treadlyNetworkResponse) {
                    VideoServiceHelper.lambda$getTrainerModeEnabled$43(VideoServiceHelper.VideoGetTrainerModeEnabled.this, treadlyNetworkResponse);
                }
            });
        } catch (JSONException e) {
            e.printStackTrace();
            Log.e("trainer", "Unable to make request");
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static /* synthetic */ void lambda$getTrainerModeEnabled$43(final VideoGetTrainerModeEnabled videoGetTrainerModeEnabled, TreadlyNetworkResponse treadlyNetworkResponse) throws JSONException {
        if (!treadlyNetworkResponse.ok() || treadlyNetworkResponse.response == null) {
            runOnMain(new Runnable() { // from class: com.treadly.Treadly.UI.TreadlyVideo.Data.-$$Lambda$VideoServiceHelper$kp1qcIuTxKcsQ8aZwyCgbjYfzrk
                @Override // java.lang.Runnable
                public final void run() {
                    VideoServiceHelper.VideoGetTrainerModeEnabled.this.onResponse("Error: could not make request", null);
                }
            });
            return;
        }
        final TrainerModeEnabledState fromValue = TrainerModeEnabledState.fromValue(treadlyNetworkResponse.response.optInt("data"));
        runOnMain(new Runnable() { // from class: com.treadly.Treadly.UI.TreadlyVideo.Data.-$$Lambda$VideoServiceHelper$_yMFzSbSa-9MubQSeyuhJ41ooDA
            @Override // java.lang.Runnable
            public final void run() {
                VideoServiceHelper.VideoGetTrainerModeEnabled.this.onResponse(null, fromValue);
            }
        });
    }

    private static void requestBasicUsersPost(String str, JSONObject jSONObject, final VideoResponseListener videoResponseListener) {
        RequestUtil.shared.postJson(str, jSONObject, new RequestUtil.RequestUtilListener() { // from class: com.treadly.Treadly.UI.TreadlyVideo.Data.VideoServiceHelper.22
            @Override // com.treadly.Treadly.Data.Utility.RequestUtil.RequestUtilListener
            public void onResponse(TreadlyNetworkResponse treadlyNetworkResponse) throws JSONException {
                if (!treadlyNetworkResponse.ok() || treadlyNetworkResponse.response == null) {
                    VideoServiceHelper.runOnMain(new Runnable() { // from class: com.treadly.Treadly.UI.TreadlyVideo.Data.VideoServiceHelper.22.1
                        @Override // java.lang.Runnable
                        public void run() {
                            VideoResponseListener.this.onResponse("Error: Could not make request");
                        }
                    });
                } else {
                    VideoServiceHelper.runOnMain(new Runnable() { // from class: com.treadly.Treadly.UI.TreadlyVideo.Data.VideoServiceHelper.22.2
                        @Override // java.lang.Runnable
                        public void run() {
                            VideoResponseListener.this.onResponse(null);
                        }
                    });
                }
            }
        });
    }

    private static void requestBasicAuthPost(String str, String str2, JSONObject jSONObject, final VideoResponseListener videoResponseListener) {
        RequestUtil.shared.postJson(str, jSONObject, getHeader(str2), new RequestUtil.RequestUtilListener() { // from class: com.treadly.Treadly.UI.TreadlyVideo.Data.VideoServiceHelper.23
            @Override // com.treadly.Treadly.Data.Utility.RequestUtil.RequestUtilListener
            public void onResponse(TreadlyNetworkResponse treadlyNetworkResponse) {
                if (treadlyNetworkResponse == null || !treadlyNetworkResponse.ok() || treadlyNetworkResponse.response == null) {
                    VideoServiceHelper.runOnMain(new Runnable() { // from class: com.treadly.Treadly.UI.TreadlyVideo.Data.VideoServiceHelper.23.1
                        @Override // java.lang.Runnable
                        public void run() {
                            VideoResponseListener.this.onResponse("Error: Could not make request");
                        }
                    });
                } else if (treadlyNetworkResponse.response.optString("status", "").equals("ok")) {
                    VideoServiceHelper.runOnMain(new Runnable() { // from class: com.treadly.Treadly.UI.TreadlyVideo.Data.VideoServiceHelper.23.2
                        @Override // java.lang.Runnable
                        public void run() {
                            VideoResponseListener.this.onResponse(null);
                        }
                    });
                } else {
                    VideoServiceHelper.runOnMain(new Runnable() { // from class: com.treadly.Treadly.UI.TreadlyVideo.Data.VideoServiceHelper.23.3
                        @Override // java.lang.Runnable
                        public void run() {
                            VideoResponseListener.this.onResponse("Error: Could not parse results");
                        }
                    });
                }
            }
        });
    }

    private static JSONObject getHeader(String str) {
        JSONObject jSONObject = new JSONObject();
        try {
            jSONObject.put(keyHeaderAuthorization, str);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return jSONObject;
    }

    private static String convertDateToServiceTimestamp(Date date) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS", Locale.getDefault());
        simpleDateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
        String format = simpleDateFormat.format(date);
        return format + "Z";
    }

    private static JSONObject convertJsonToRequestData(JSONObject jSONObject) throws JSONException {
        JSONObject jSONObject2 = new JSONObject();
        jSONObject2.put(keyDataPayload, jSONObject);
        return jSONObject2;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static void runOnMain(Runnable runnable) {
        new Handler(Looper.getMainLooper()).post(runnable);
    }
}
