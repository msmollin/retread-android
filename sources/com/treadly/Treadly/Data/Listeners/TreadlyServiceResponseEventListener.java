package com.treadly.Treadly.Data.Listeners;

import com.treadly.Treadly.Data.Model.FollowInfo;
import com.treadly.Treadly.Data.Model.FriendInviteInfo;
import com.treadly.Treadly.Data.Model.FriendRequestInfo;
import com.treadly.Treadly.Data.Model.UserActivityInfo;
import com.treadly.Treadly.Data.Model.UserDiscoverInfo;
import com.treadly.Treadly.Data.Model.UserInfo;
import com.treadly.Treadly.Data.Model.UserProfileInfo;
import com.treadly.Treadly.Data.Model.UserRunningSessionInfo;
import com.treadly.Treadly.Data.Model.UserStatsInfo;
import com.treadly.Treadly.Data.Model.UserTokenInfo;
import java.util.ArrayList;
import java.util.List;
import org.json.JSONException;

/* loaded from: classes.dex */
public interface TreadlyServiceResponseEventListener {
    void onActivitiesInfo(String str, List<UserActivityInfo> list) throws JSONException;

    void onCreateFriendInviteToken(String str, String str2);

    void onDiscoverFollowersResponse(String str, List<UserDiscoverInfo> list);

    void onFollowResponse(String str);

    void onFriendInviteToken(String str, FriendInviteInfo friendInviteInfo);

    void onGetFollowersResponse(String str, List<FollowInfo> list);

    void onGetFollowingResponse(String str, List<FollowInfo> list);

    void onGetFriendRequests(String str, List<FriendRequestInfo> list);

    void onGetUnclaimedLogs(String str, List<UserRunningSessionInfo> list);

    void onSendDeviceLogSession(String str, String str2, String str3, UserRunningSessionInfo userRunningSessionInfo);

    void onSuccess(String str);

    void onUnfollowResponse(String str);

    void onUrlResponse(String str, String str2);

    void onUserFriendsInfo(String str, List<UserInfo> list) throws JSONException;

    void onUserInfo(String str, UserInfo userInfo);

    void onUserInfoProfile(String str, UserInfo userInfo, boolean z, boolean z2);

    void onUserProfile(String str, UserProfileInfo userProfileInfo);

    void onUserProfileRequest(String str);

    void onUserRunningSessionsInfo(String str, ArrayList<UserRunningSessionInfo> arrayList);

    void onUserStatsInfo(String str, ArrayList<UserStatsInfo> arrayList);

    void onUserTokenInfoProfile(String str, UserTokenInfo userTokenInfo, boolean z, boolean z2);
}
