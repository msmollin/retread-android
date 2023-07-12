package com.treadly.Treadly.Data.Model;

/* loaded from: classes2.dex */
public class UserNotificationSettingInfo {
    public boolean dailyGoal100Achieved;
    public boolean dailyGoal50Achieved;
    public boolean enable;
    public boolean friendOnlineStatus;
    public boolean friendRequest;
    public boolean groupRequestApproval;
    public boolean likeToYourGroupPost;
    public boolean likeToYourVideo;
    public boolean newPostToYourGroup;
    public boolean replyToYourGroupComment;
    public boolean replyToYourGroupPost;
    public boolean videoInvite;
    public boolean workoutSchedule;

    public UserNotificationSettingInfo(boolean z, boolean z2, boolean z3, boolean z4, boolean z5, boolean z6, boolean z7, boolean z8, boolean z9, boolean z10, boolean z11, boolean z12, boolean z13) {
        this.enable = z;
        this.dailyGoal50Achieved = z2;
        this.dailyGoal100Achieved = z3;
        this.groupRequestApproval = z4;
        this.newPostToYourGroup = z5;
        this.workoutSchedule = z6;
        this.replyToYourGroupPost = z7;
        this.replyToYourGroupComment = z8;
        this.likeToYourVideo = z9;
        this.likeToYourGroupPost = z10;
        this.friendRequest = z11;
        this.videoInvite = z12;
        this.friendOnlineStatus = z13;
    }
}
