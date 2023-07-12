package com.treadly.Treadly.UI.PushNotifications.Firebase;

/* loaded from: classes2.dex */
public enum FirebaseNotificationCategory {
    none(""),
    privateVideoInvite("PRIVATE_VIDEO_INVITE_CATEGORY"),
    publicVideoInvite("PUBLIC_VIDEO_INVITE_CATEGORY"),
    groupRequest("GROUP_REQUEST_CATEGORY"),
    groupRequestApprove("GROUP_REQUEST_APPROVE"),
    groupInvite("GROUP_INVITE_CATEGORY"),
    groupInviteApprove("GROUP_INVITE_APPROVE_CATEGORY"),
    groupVideo("GROUP_VIDEO_CATEGORY"),
    groupPost("GROUP_POST_CATEGORY"),
    groupPostComment("GROUP_POST_COMMENT_CATEGORY"),
    groupPostSubComment("GROUP_POST_COMMENT_CATEGORY"),
    groupPostLike("GROUP_POST_LIKE_CATEGORY"),
    friendRequest("FRIEND_REQUEST_CATEGORY"),
    groupScheduleEvent("GROUP_SCHEDULE_EVENT_CATEGORY"),
    dailyGoal50("DAILY_GOAL_50_COMPLETE_CATEGORY"),
    dailyGoal100("DAILY_GOAL_100_COMPLETE_CATEGORY"),
    friendOnlineStatus("FRIEND_ONLINE_STATUS_CATEGORY"),
    videoLike("VIDEO_LIKE_CATEGORY");
    
    private String value;

    FirebaseNotificationCategory(String str) {
        this.value = str;
    }

    public static FirebaseNotificationCategory fromValue(String str) {
        FirebaseNotificationCategory[] values;
        if (str == null) {
            return null;
        }
        for (FirebaseNotificationCategory firebaseNotificationCategory : values()) {
            if (str.equals(firebaseNotificationCategory.value)) {
                return firebaseNotificationCategory;
            }
        }
        return null;
    }

    public static FirebaseNotificationCategory fromString(String str) {
        return fromValue(str);
    }
}
